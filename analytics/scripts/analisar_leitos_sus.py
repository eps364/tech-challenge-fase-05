from __future__ import annotations

import csv
import gzip
import html
import json
import zipfile
from collections import defaultdict
from dataclasses import dataclass, field
from pathlib import Path


ROOT = Path(__file__).resolve().parents[2]
RAW = ROOT / "data" / "raw"
PROCESSED = ROOT / "data" / "processed"
REPORTS = ROOT / "analytics" / "reports"

LEITOS_ZIP = RAW / "leitos_sus_2026_csv.zip"
POP_MUNICIPIO_JSON = RAW / "ibge_populacao_municipio_2022_tabela_4714.json"
POP_UF_JSON = RAW / "ibge_populacao_uf_2022_tabela_4714.json"
ESTADOS_JSON = RAW / "ibge_localidades_estados.json"


@dataclass
class Aggregate:
    regiao: str = ""
    uf: str = ""
    uf_nome: str = ""
    co_ibge6: str = ""
    municipio: str = ""
    populacao_2022: int = 0
    registros: int = 0
    cnes: set[str] = field(default_factory=set)
    municipios_com_registro: set[str] = field(default_factory=set)
    leitos_existentes: int = 0
    leitos_sus: int = 0
    uti_total_exist: int = 0
    uti_total_sus: int = 0


def parse_int(value: str | int | float | None) -> int:
    if value is None:
        return 0
    if isinstance(value, int):
        return value
    if isinstance(value, float):
        return int(value)
    cleaned = str(value).strip().replace(".", "").replace(",", ".")
    if not cleaned or cleaned == "-":
        return 0
    try:
        return int(float(cleaned))
    except ValueError:
        return 0


def read_json(path: Path) -> object:
    raw = path.read_bytes()
    if raw.startswith(b"\x1f\x8b"):
        raw = gzip.decompress(raw)
    return json.loads(raw.decode("utf-8"))


def fmt_int(value: int | float) -> str:
    return f"{value:,.0f}".replace(",", ".")


def fmt_float(value: float, digits: int = 2) -> str:
    return f"{value:.{digits}f}".replace(".", ",")


def pct(numerator: int, denominator: int) -> float:
    return (numerator / denominator * 100) if denominator else 0.0


def rate(numerator: int, denominator: int, multiplier: int) -> float:
    return (numerator / denominator * multiplier) if denominator else 0.0


def load_estados() -> dict[str, dict[str, str]]:
    data = read_json(ESTADOS_JSON)
    estados: dict[str, dict[str, str]] = {}
    for item in data:
        code = str(item["id"])
        estados[code] = {
            "sigla": item["sigla"],
            "nome": item["nome"],
            "regiao": item["regiao"]["nome"].upper(),
        }
    return estados


def load_populacao(path: Path, estados: dict[str, dict[str, str]], municipal: bool) -> dict[str, dict[str, str | int]]:
    data = read_json(path)
    rows = data[1:]
    out: dict[str, dict[str, str | int]] = {}
    for row in rows:
        code = str(row["D1C"])
        key = code[:6] if municipal else code
        uf_code = code[:2] if municipal else code
        estado = estados.get(uf_code, {})
        out[key] = {
            "codigo": code,
            "nome": row["D1N"],
            "populacao": parse_int(row["V"]),
            "uf": estado.get("sigla", ""),
            "uf_nome": estado.get("nome", ""),
            "regiao": estado.get("regiao", ""),
        }
    return out


def read_leitos_rows() -> list[dict[str, str]]:
    with zipfile.ZipFile(LEITOS_ZIP) as archive:
        names = archive.namelist()
        if len(names) != 1:
            raise RuntimeError(f"Esperado 1 CSV no ZIP, encontrados: {names}")
        with archive.open(names[0]) as raw_file:
            text_file = (line.decode("latin-1") for line in raw_file)
            reader = csv.DictReader(text_file, delimiter=";")
            return list(reader)


def add_to_aggregate(agg: Aggregate, row: dict[str, str]) -> None:
    agg.registros += 1
    agg.cnes.add(row.get("CNES", "").strip())
    co_ibge6 = row.get("CO_IBGE", "").strip().zfill(6)
    if co_ibge6:
        agg.municipios_com_registro.add(co_ibge6)
    agg.leitos_existentes += parse_int(row.get("LEITOS_EXISTENTES"))
    agg.leitos_sus += parse_int(row.get("LEITOS_SUS"))
    agg.uti_total_exist += parse_int(row.get("UTI_TOTAL_EXIST"))
    agg.uti_total_sus += parse_int(row.get("UTI_TOTAL_SUS"))


def aggregate_data(
    pop_municipio: dict[str, dict[str, str | int]],
    pop_uf: dict[str, dict[str, str | int]],
) -> tuple[dict[str, Aggregate], dict[str, Aggregate], dict[str, Aggregate], set[str], str]:
    by_municipio: dict[str, Aggregate] = {}
    by_uf: dict[str, Aggregate] = {}
    by_regiao: dict[str, Aggregate] = {}
    rows = read_leitos_rows()
    competencias = {row.get("COMP", "").strip() for row in rows if row.get("COMP", "").strip()}
    selected_competencia = max(competencias) if competencias else ""

    for code6, pop in pop_municipio.items():
        by_municipio[code6] = Aggregate(
            regiao=str(pop["regiao"]),
            uf=str(pop["uf"]),
            uf_nome=str(pop["uf_nome"]),
            co_ibge6=code6,
            municipio=str(pop["nome"]),
            populacao_2022=int(pop["populacao"]),
        )

    for uf_code, pop in pop_uf.items():
        uf = str(pop["uf"])
        by_uf[uf] = Aggregate(
            regiao=str(pop["regiao"]),
            uf=uf,
            uf_nome=str(pop["nome"]),
            populacao_2022=int(pop["populacao"]),
        )

    for row in rows:
        if row.get("COMP", "").strip() != selected_competencia:
            continue
        co_ibge6 = row.get("CO_IBGE", "").strip().zfill(6)
        uf = row.get("UF", "").strip()
        regiao = row.get("REGIAO", "").strip()
        municipio = row.get("MUNICIPIO", "").strip()

        if co_ibge6 not in by_municipio:
            by_municipio[co_ibge6] = Aggregate(
                regiao=regiao,
                uf=uf,
                co_ibge6=co_ibge6,
                municipio=municipio,
            )
        add_to_aggregate(by_municipio[co_ibge6], row)

        if uf not in by_uf:
            by_uf[uf] = Aggregate(regiao=regiao, uf=uf)
        add_to_aggregate(by_uf[uf], row)

        if regiao not in by_regiao:
            by_regiao[regiao] = Aggregate(regiao=regiao)
        add_to_aggregate(by_regiao[regiao], row)

    for agg in by_uf.values():
        if agg.regiao not in by_regiao:
            by_regiao[agg.regiao] = Aggregate(regiao=agg.regiao)
        by_regiao[agg.regiao].populacao_2022 += agg.populacao_2022

    return by_municipio, by_uf, by_regiao, competencias, selected_competencia


def agg_to_row(agg: Aggregate, level: str, national_rates: dict[str, float] | None = None) -> dict[str, str | int | float]:
    national_rates = national_rates or {}
    leitos_sus_10k = rate(agg.leitos_sus, agg.populacao_2022, 10_000)
    uti_sus_100k = rate(agg.uti_total_sus, agg.populacao_2022, 100_000)
    gap_leitos = max(0.0, national_rates.get("leitos_sus_10k", 0.0) - leitos_sus_10k)
    gap_uti = max(0.0, national_rates.get("uti_sus_100k", 0.0) - uti_sus_100k)
    return {
        "nivel": level,
        "regiao": agg.regiao,
        "uf": agg.uf,
        "uf_nome": agg.uf_nome,
        "co_ibge6": agg.co_ibge6,
        "municipio": agg.municipio,
        "populacao_2022": agg.populacao_2022,
        "registros": agg.registros,
        "estabelecimentos_cnes": len({c for c in agg.cnes if c}),
        "municipios_com_registro": len(agg.municipios_com_registro),
        "leitos_existentes": agg.leitos_existentes,
        "leitos_sus": agg.leitos_sus,
        "uti_total_exist": agg.uti_total_exist,
        "uti_total_sus": agg.uti_total_sus,
        "pct_leitos_sus": pct(agg.leitos_sus, agg.leitos_existentes),
        "pct_uti_sus": pct(agg.uti_total_sus, agg.uti_total_exist),
        "leitos_existentes_por_10k_hab": rate(agg.leitos_existentes, agg.populacao_2022, 10_000),
        "leitos_sus_por_10k_hab": leitos_sus_10k,
        "uti_existente_por_100k_hab": rate(agg.uti_total_exist, agg.populacao_2022, 100_000),
        "uti_sus_por_100k_hab": uti_sus_100k,
        "gap_estimado_leitos_sus_para_media_nacional": gap_leitos * agg.populacao_2022 / 10_000,
        "gap_estimado_uti_sus_para_media_nacional": gap_uti * agg.populacao_2022 / 100_000,
    }


def write_csv(path: Path, rows: list[dict[str, str | int | float]]) -> None:
    if not rows:
        return
    path.parent.mkdir(parents=True, exist_ok=True)
    with path.open("w", newline="", encoding="utf-8") as file:
        writer = csv.DictWriter(file, fieldnames=list(rows[0].keys()))
        writer.writeheader()
        writer.writerows(rows)


def classify_opportunities(rows: list[dict[str, str | int | float]], national: dict[str, float]) -> list[dict[str, str | int | float]]:
    out: list[dict[str, str | int | float]] = []
    for row in rows:
        pop = int(row["populacao_2022"])
        if pop < 50_000:
            continue
        leitos_sus = int(row["leitos_sus"])
        uti_sus = int(row["uti_total_sus"])
        leitos_rate = float(row["leitos_sus_por_10k_hab"])
        uti_rate = float(row["uti_sus_por_100k_hab"])
        pct_leitos = float(row["pct_leitos_sus"])
        flags: list[str] = []
        score = 0
        if leitos_sus == 0:
            flags.append("municipio_50k_mais_sem_leitos_sus")
            score += 5
        if pop >= 100_000 and uti_sus == 0:
            flags.append("municipio_100k_mais_sem_uti_sus")
            score += 4
        if leitos_rate < national["leitos_sus_10k"] * 0.5:
            flags.append("leitos_sus_por_10k_abaixo_de_50pct_media_nacional")
            score += 3
        if pop >= 100_000 and uti_rate < national["uti_sus_100k"] * 0.5:
            flags.append("uti_sus_por_100k_abaixo_de_50pct_media_nacional")
            score += 3
        if int(row["leitos_existentes"]) > 0 and pct_leitos < 30:
            flags.append("baixa_participacao_sus_na_capacidade_existente")
            score += 2
        if not flags:
            continue
        candidate = dict(row)
        candidate["sinais_de_oportunidade"] = ";".join(flags)
        candidate["score_heuristico_oportunidade"] = score
        out.append(candidate)
    return sorted(
        out,
        key=lambda r: (
            -int(r["score_heuristico_oportunidade"]),
            -float(r["gap_estimado_leitos_sus_para_media_nacional"]),
            -int(r["populacao_2022"]),
        ),
    )


def table_md(rows: list[dict[str, str | int | float]], columns: list[str], limit: int = 10) -> str:
    selected = rows[:limit]
    header = "| " + " | ".join(columns) + " |"
    sep = "| " + " | ".join(["---"] * len(columns)) + " |"
    body = []
    for row in selected:
        values = []
        for col in columns:
            value = row[col]
            if isinstance(value, float):
                values.append(fmt_float(value))
            elif isinstance(value, int):
                values.append(fmt_int(value))
            else:
                values.append(str(value))
        body.append("| " + " | ".join(values) + " |")
    return "\n".join([header, sep, *body])


def bar_svg(rows: list[dict[str, str | int | float]], label_col: str, value_col: str, title: str, width: int = 980) -> str:
    rows = rows[:]
    height = 34 * len(rows) + 70
    max_value = max((float(row[value_col]) for row in rows), default=1.0) or 1.0
    label_width = 170
    chart_width = width - label_width - 110
    parts = [
        f'<svg viewBox="0 0 {width} {height}" width="{width}" height="{height}" role="img" aria-label="{html.escape(title)}">',
        '<style>text{font-family:Arial,sans-serif;font-size:12px}.title{font-size:16px;font-weight:700}.bar{fill:#227c9d}.axis{stroke:#d0d7de;stroke-width:1}</style>',
        f'<text class="title" x="0" y="20">{html.escape(title)}</text>',
    ]
    y = 46
    for row in rows:
        label = html.escape(str(row[label_col]))
        value = float(row[value_col])
        bar_width = max(2, value / max_value * chart_width)
        parts.append(f'<text x="0" y="{y + 14}">{label}</text>')
        parts.append(f'<rect class="bar" x="{label_width}" y="{y}" width="{bar_width:.1f}" height="18" rx="2"></rect>')
        parts.append(f'<text x="{label_width + bar_width + 8:.1f}" y="{y + 14}">{fmt_float(value)}</text>')
        y += 34
    parts.append("</svg>")
    return "\n".join(parts)


def html_table(rows: list[dict[str, str | int | float]], columns: list[str], limit: int = 10) -> str:
    head = "".join(f"<th>{html.escape(col)}</th>" for col in columns)
    body = []
    for row in rows[:limit]:
        cells = []
        for col in columns:
            value = row[col]
            if isinstance(value, float):
                text = fmt_float(value)
            elif isinstance(value, int):
                text = fmt_int(value)
            else:
                text = str(value)
            cells.append(f"<td>{html.escape(text)}</td>")
        body.append("<tr>" + "".join(cells) + "</tr>")
    return "<table><thead><tr>" + head + "</tr></thead><tbody>" + "".join(body) + "</tbody></table>"


def write_reports(
    municipio_rows: list[dict[str, str | int | float]],
    uf_rows: list[dict[str, str | int | float]],
    regiao_rows: list[dict[str, str | int | float]],
    oportunidades: list[dict[str, str | int | float]],
    competencias: set[str],
    selected_competencia: str,
    national: dict[str, float],
) -> None:
    REPORTS.mkdir(parents=True, exist_ok=True)
    total_pop = sum(int(row["populacao_2022"]) for row in uf_rows)
    total_leitos = sum(int(row["leitos_sus"]) for row in uf_rows)
    total_uti = sum(int(row["uti_total_sus"]) for row in uf_rows)
    total_cnes = sum(int(row["estabelecimentos_cnes"]) for row in uf_rows)
    municipios_50k_zero = sum(
        1 for row in municipio_rows if int(row["populacao_2022"]) >= 50_000 and int(row["leitos_sus"]) == 0
    )
    municipios_100k_sem_uti = sum(
        1 for row in municipio_rows if int(row["populacao_2022"]) >= 100_000 and int(row["uti_total_sus"]) == 0
    )

    uf_low_leitos = sorted(uf_rows, key=lambda r: float(r["leitos_sus_por_10k_hab"]))
    uf_low_uti = sorted(uf_rows, key=lambda r: float(r["uti_sus_por_100k_hab"]))
    regiao_rank = sorted(regiao_rows, key=lambda r: float(r["leitos_sus_por_10k_hab"]))

    md = f"""# Analise exploratoria - Hospitais e Leitos SUS 2026

Data de acesso: 2026-07-18

## Objetivo

Investigar, com dados publicos oficiais, sinais de desigualdade de capacidade hospitalar SUS que possam indicar oportunidades de melhoria no atendimento, regulacao, transparencia operacional ou planejamento de capacidade. Esta analise nao escolhe ainda o problema final nem propoe solucao fechada.

## Bases utilizadas

- Ministerio da Saude / Portal de Dados Abertos do SUS: Hospitais e Leitos, recurso Leitos 2026 CSV.
- IBGE / SIDRA: Censo Demografico 2022, tabela 4714, variavel 93, populacao residente por municipio e UF.
- IBGE / API de Localidades: lista de estados para compatibilizar codigos e siglas.

## Escopo dos dados

- Competencias encontradas no CSV de leitos: {", ".join(sorted(competencias)) or "nao identificada"}.
- Competencia analisada: {selected_competencia or "nao identificada"} (maior competencia disponivel no arquivo).
- Populacao usada como denominador: Censo 2022.
- Abrangencia geografica: Brasil, UFs e municipios.

## Indicadores principais

- Populacao 2022 considerada: {fmt_int(total_pop)} pessoas.
- Estabelecimentos hospitalares com registro no arquivo de leitos: {fmt_int(total_cnes)}.
- Leitos SUS: {fmt_int(total_leitos)}.
- UTI SUS: {fmt_int(total_uti)}.
- Leitos SUS por 10 mil habitantes: {fmt_float(national["leitos_sus_10k"])}.
- UTI SUS por 100 mil habitantes: {fmt_float(national["uti_sus_100k"])}.
- Municipios com 50 mil habitantes ou mais e zero leitos SUS no arquivo: {fmt_int(municipios_50k_zero)}.
- Municipios com 100 mil habitantes ou mais e zero UTI SUS no arquivo: {fmt_int(municipios_100k_sem_uti)}.

## UFs com menor taxa de leitos SUS por 10 mil habitantes

{table_md(uf_low_leitos, ["uf", "uf_nome", "populacao_2022", "leitos_sus", "leitos_sus_por_10k_hab"], 10)}

## UFs com menor taxa de UTI SUS por 100 mil habitantes

{table_md(uf_low_uti, ["uf", "uf_nome", "populacao_2022", "uti_total_sus", "uti_sus_por_100k_hab"], 10)}

## Comparacao regional

{table_md(regiao_rank, ["regiao", "populacao_2022", "leitos_sus", "leitos_sus_por_10k_hab", "uti_total_sus", "uti_sus_por_100k_hab"], 10)}

## Municipios priorizados pela heuristica de oportunidade

{table_md(oportunidades, ["uf", "municipio", "populacao_2022", "leitos_sus", "leitos_sus_por_10k_hab", "uti_total_sus", "uti_sus_por_100k_hab", "sinais_de_oportunidade", "score_heuristico_oportunidade"], 15)}

## Interpretacao inicial

- Fato/dado: a distribuicao de leitos SUS e UTI SUS por habitante varia bastante entre UFs e municipios.
- Interpretacao: localidades com populacao relevante, baixa taxa per capita e/ou ausencia de UTI SUS podem representar gargalos de acesso, regulacao, referenciamento ou transparencia para pacientes e gestores.
- Hipotese: uma solucao focada em visibilidade de capacidade, priorizacao ou apoio ao encaminhamento poderia gerar valor onde ha maior assimetria entre demanda populacional e oferta SUS registrada.
- Limitacao: a base de leitos mede capacidade cadastrada, nao mede fila, tempo de espera, ocupacao em tempo real, qualidade assistencial ou deslocamento efetivo do paciente.
- Risco de interpretacao: baixa taxa de leitos em um municipio pode ser compensada por rede regional de referencia; portanto, a analise municipal precisa ser cruzada com regioes de saude e fluxos de atendimento antes de virar conclusao.

## Arquivos gerados

- data/processed/leitos_uf_indicadores_2026.csv
- data/processed/leitos_regiao_indicadores_2026.csv
- data/processed/leitos_municipio_indicadores_2026.csv
- data/processed/leitos_oportunidades_municipio_2026.csv
- analytics/reports/analise_leitos_sus_2026.html
"""
    (REPORTS / "analise_leitos_sus_2026.md").write_text(md, encoding="utf-8")

    html_doc = f"""<!doctype html>
<html lang="pt-BR">
<head>
  <meta charset="utf-8">
  <title>Analise exploratoria - Leitos SUS 2026</title>
  <style>
    body {{ font-family: Arial, sans-serif; margin: 32px; color: #17202a; }}
    main {{ max-width: 1180px; margin: auto; }}
    h1, h2 {{ color: #113b53; }}
    .cards {{ display: grid; grid-template-columns: repeat(auto-fit, minmax(210px, 1fr)); gap: 12px; }}
    .card {{ border: 1px solid #d0d7de; border-radius: 6px; padding: 12px; background: #f8fafc; }}
    .metric {{ font-size: 24px; font-weight: 700; color: #155e75; }}
    table {{ border-collapse: collapse; width: 100%; margin: 12px 0 28px; font-size: 13px; }}
    th, td {{ border: 1px solid #d0d7de; padding: 6px 8px; text-align: left; vertical-align: top; }}
    th {{ background: #edf2f7; }}
    .note {{ background: #fff7ed; border-left: 4px solid #f97316; padding: 10px 14px; }}
    .chart {{ overflow-x: auto; margin: 18px 0; }}
  </style>
</head>
<body>
<main>
  <h1>Analise exploratoria - Hospitais e Leitos SUS 2026</h1>
  <p><strong>Data de acesso:</strong> 2026-07-18. <strong>Competencia analisada:</strong> {html.escape(selected_competencia or "nao identificada")}. Esta analise identifica oportunidades; nao prova causalidade nem escolhe a solucao final.</p>
  <section class="cards">
    <div class="card"><div>Populacao 2022</div><div class="metric">{fmt_int(total_pop)}</div></div>
    <div class="card"><div>Leitos SUS</div><div class="metric">{fmt_int(total_leitos)}</div></div>
    <div class="card"><div>Leitos SUS / 10 mil hab.</div><div class="metric">{fmt_float(national["leitos_sus_10k"])}</div></div>
    <div class="card"><div>UTI SUS / 100 mil hab.</div><div class="metric">{fmt_float(national["uti_sus_100k"])}</div></div>
    <div class="card"><div>Municipios 50k+ sem leitos SUS</div><div class="metric">{fmt_int(municipios_50k_zero)}</div></div>
    <div class="card"><div>Municipios 100k+ sem UTI SUS</div><div class="metric">{fmt_int(municipios_100k_sem_uti)}</div></div>
  </section>

  <h2>UFs - leitos SUS por 10 mil habitantes</h2>
  <div class="chart">{bar_svg(uf_low_leitos, "uf", "leitos_sus_por_10k_hab", "UFs ordenadas por leitos SUS por 10 mil habitantes")}</div>

  <h2>UFs - UTI SUS por 100 mil habitantes</h2>
  <div class="chart">{bar_svg(uf_low_uti, "uf", "uti_sus_por_100k_hab", "UFs ordenadas por UTI SUS por 100 mil habitantes")}</div>

  <h2>Comparacao regional</h2>
  {html_table(regiao_rank, ["regiao", "populacao_2022", "leitos_sus", "leitos_sus_por_10k_hab", "uti_total_sus", "uti_sus_por_100k_hab"], 10)}

  <h2>Municipios priorizados pela heuristica de oportunidade</h2>
  {html_table(oportunidades, ["uf", "municipio", "populacao_2022", "leitos_sus", "leitos_sus_por_10k_hab", "uti_total_sus", "uti_sus_por_100k_hab", "sinais_de_oportunidade", "score_heuristico_oportunidade"], 30)}

  <h2>Limites de interpretacao</h2>
  <div class="note">
    <p>A base de leitos representa capacidade cadastrada no CNES, nao tempo de espera, ocupacao em tempo real, qualidade assistencial ou necessidade clinica. A leitura municipal precisa ser cruzada com regioes de saude, fluxos de referencia e dados de demanda antes de virar decisao de produto.</p>
  </div>
</main>
</body>
</html>
"""
    (REPORTS / "analise_leitos_sus_2026.html").write_text(html_doc, encoding="utf-8")


def main() -> None:
    PROCESSED.mkdir(parents=True, exist_ok=True)
    estados = load_estados()
    pop_municipio = load_populacao(POP_MUNICIPIO_JSON, estados, municipal=True)
    pop_uf = load_populacao(POP_UF_JSON, estados, municipal=False)
    by_municipio, by_uf, by_regiao, competencias, selected_competencia = aggregate_data(pop_municipio, pop_uf)

    total_pop = sum(agg.populacao_2022 for agg in by_uf.values())
    total_leitos_sus = sum(agg.leitos_sus for agg in by_uf.values())
    total_uti_sus = sum(agg.uti_total_sus for agg in by_uf.values())
    national = {
        "leitos_sus_10k": rate(total_leitos_sus, total_pop, 10_000),
        "uti_sus_100k": rate(total_uti_sus, total_pop, 100_000),
    }

    uf_rows = [agg_to_row(agg, "uf", national) for agg in by_uf.values()]
    regiao_rows = [agg_to_row(agg, "regiao", national) for agg in by_regiao.values() if agg.regiao]
    municipio_rows = [agg_to_row(agg, "municipio", national) for agg in by_municipio.values()]

    uf_rows = sorted(uf_rows, key=lambda row: str(row["uf"]))
    regiao_rows = sorted(regiao_rows, key=lambda row: str(row["regiao"]))
    municipio_rows = sorted(municipio_rows, key=lambda row: (str(row["uf"]), str(row["municipio"])))
    oportunidades = classify_opportunities(municipio_rows, national)

    write_csv(PROCESSED / "leitos_uf_indicadores_2026.csv", uf_rows)
    write_csv(PROCESSED / "leitos_regiao_indicadores_2026.csv", regiao_rows)
    write_csv(PROCESSED / "leitos_municipio_indicadores_2026.csv", municipio_rows)
    write_csv(PROCESSED / "leitos_oportunidades_municipio_2026.csv", oportunidades)
    write_reports(municipio_rows, uf_rows, regiao_rows, oportunidades, competencias, selected_competencia, national)

    print("Analise concluida")
    print(f"UFs: {len(uf_rows)}")
    print(f"Municipios: {len(municipio_rows)}")
    print(f"Oportunidades sinalizadas: {len(oportunidades)}")
    print(f"Relatorio: {REPORTS / 'analise_leitos_sus_2026.md'}")


if __name__ == "__main__":
    main()
