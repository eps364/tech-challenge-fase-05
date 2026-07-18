from __future__ import annotations

import csv
import gzip
import html
import json
import zipfile
from collections import defaultdict
from dataclasses import dataclass
from pathlib import Path


ROOT = Path(__file__).resolve().parents[2]
RAW = ROOT / "data" / "raw"
PROCESSED = ROOT / "data" / "processed"
REPORTS = ROOT / "analytics" / "reports"

UBS_ZIP = RAW / "unidades_basicas_saude_ubs_csv.zip"
CADASTRO_ZIP = RAW / "sisab_cadastro_vinculado_202412_csv.zip"
INDICADORES_ZIP = RAW / "sisab_indicador_desempenho_2024Q3_csv.zip"
POP_MUNICIPIO_JSON = RAW / "ibge_populacao_municipio_2022_tabela_4714.json"
ESTADOS_JSON = RAW / "ibge_localidades_estados.json"


INDICATOR_LABELS = {
    "10": "pre_natal_6_consultas",
    "20": "gestantes_sifilis_hiv",
    "30": "gestantes_atendimento_odontologico",
    "40": "citopatologico",
    "50": "vacinacao_criancas_1_ano",
    "60": "hipertensao_pa_aferida",
    "70": "diabetes_hemoglobina_glicada",
}


@dataclass
class Municipality:
    co_ibge6: str
    municipio: str
    uf: str
    uf_nome: str
    regiao: str
    populacao_2022: int
    ubs: int = 0
    ubs_com_coord: int = 0
    cadastro_populacao: int = 0
    pessoas_vinculadas: float = 0.0
    indicadores_percentuais: dict[str, float] | None = None

    def __post_init__(self) -> None:
        if self.indicadores_percentuais is None:
            self.indicadores_percentuais = {}


def read_json(path: Path) -> object:
    raw = path.read_bytes()
    if raw.startswith(b"\x1f\x8b"):
        raw = gzip.decompress(raw)
    return json.loads(raw.decode("utf-8"))


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


def parse_float(value: str | int | float | None) -> float:
    if value is None:
        return 0.0
    if isinstance(value, (int, float)):
        return float(value)
    cleaned = str(value).strip()
    if not cleaned or cleaned == "-":
        return 0.0
    if "," in cleaned:
        cleaned = cleaned.replace(".", "").replace(",", ".")
    elif cleaned.count(".") > 1:
        cleaned = cleaned.replace(".", "")
    try:
        return float(cleaned)
    except ValueError:
        return 0.0


def rate(numerator: float, denominator: float, multiplier: int) -> float:
    return numerator / denominator * multiplier if denominator else 0.0


def pct(numerator: float, denominator: float) -> float:
    return numerator / denominator * 100 if denominator else 0.0


def fmt_int(value: int | float) -> str:
    return f"{value:,.0f}".replace(",", ".")


def fmt_float(value: float, digits: int = 2) -> str:
    return f"{value:.{digits}f}".replace(".", ",")


def load_estados() -> dict[str, dict[str, str]]:
    data = read_json(ESTADOS_JSON)
    return {
        str(item["id"]): {
            "sigla": item["sigla"],
            "nome": item["nome"],
            "regiao": item["regiao"]["nome"].upper(),
        }
        for item in data
    }


def load_municipalities() -> dict[str, Municipality]:
    estados = load_estados()
    data = read_json(POP_MUNICIPIO_JSON)[1:]
    municipalities: dict[str, Municipality] = {}
    for row in data:
        code = str(row["D1C"])[:6]
        uf_code = code[:2]
        estado = estados.get(uf_code, {})
        municipalities[code] = Municipality(
            co_ibge6=code,
            municipio=str(row["D1N"]),
            uf=estado.get("sigla", ""),
            uf_nome=estado.get("nome", ""),
            regiao=estado.get("regiao", ""),
            populacao_2022=parse_int(row["V"]),
        )
    return municipalities


def iter_zip_csv(path: Path, encoding: str = "latin-1") -> tuple[str, list[dict[str, str]]]:
    with zipfile.ZipFile(path) as archive:
        names = archive.namelist()
        if len(names) != 1:
            raise RuntimeError(f"Esperado 1 CSV no ZIP {path}, encontrados: {names}")
        name = names[0]
        with archive.open(name) as raw_file:
            text_file = (line.decode(encoding) for line in raw_file)
            return name, list(csv.DictReader(text_file, delimiter=";"))


def add_ubs(municipalities: dict[str, Municipality]) -> int:
    _, rows = iter_zip_csv(UBS_ZIP, encoding="latin-1")
    for row in rows:
        code = row.get("IBGE", "").strip().zfill(6)
        if code not in municipalities:
            continue
        municipalities[code].ubs += 1
        if row.get("LATITUDE", "").strip() and row.get("LONGITUDE", "").strip():
            municipalities[code].ubs_com_coord += 1
    return len(rows)


def add_cadastro_vinculado(municipalities: dict[str, Municipality]) -> int:
    _, rows = iter_zip_csv(CADASTRO_ZIP, encoding="latin-1")
    per_municipality: dict[str, dict[str, float]] = defaultdict(lambda: {"pop": 0.0, "vinc": 0.0})
    for row in rows:
        if row.get("condicao_equipe", "").strip().lower() != "todas":
            continue
        if row.get("criterio_ponderacao", "").strip().lower() != "sim":
            continue
        code = row.get("ibge", "").strip().zfill(6)
        per_municipality[code]["pop"] = max(per_municipality[code]["pop"], parse_float(row.get("populacao")))
        per_municipality[code]["vinc"] += parse_float(row.get("qt_pessoas_vinculadas"))
    for code, values in per_municipality.items():
        if code not in municipalities:
            continue
        municipalities[code].cadastro_populacao = int(values["pop"])
        municipalities[code].pessoas_vinculadas = values["vinc"]
    return len(rows)


def add_indicadores(municipalities: dict[str, Municipality]) -> int:
    _, rows = iter_zip_csv(INDICADORES_ZIP, encoding="utf-8")
    for row in rows:
        if row.get("visao_equipe", "").strip().lower() != "geral":
            continue
        code = row.get("ibge", "").strip().zfill(6)
        if code not in municipalities:
            continue
        indicator = row.get("co_tipo_indicador", "").strip()
        label = INDICATOR_LABELS.get(indicator, f"indicador_{indicator}")
        municipalities[code].indicadores_percentuais[label] = parse_float(row.get("vl_perc_quad"))
    return len(rows)


def municipality_row(m: Municipality, national_ubs_10k: float) -> dict[str, str | int | float]:
    indicator_values = list(m.indicadores_percentuais.values()) if m.indicadores_percentuais else []
    avg_indicator = sum(indicator_values) / len(indicator_values) if indicator_values else 0.0
    below_50 = sum(1 for value in indicator_values if value < 50)
    below_30 = sum(1 for value in indicator_values if value < 30)
    cadastro_denominator = m.cadastro_populacao or m.populacao_2022
    cadastro_pct = pct(m.pessoas_vinculadas, cadastro_denominator)
    ubs_10k = rate(m.ubs, m.populacao_2022, 10_000)
    return {
        "co_ibge6": m.co_ibge6,
        "municipio": m.municipio,
        "uf": m.uf,
        "uf_nome": m.uf_nome,
        "regiao": m.regiao,
        "populacao_2022": m.populacao_2022,
        "ubs": m.ubs,
        "ubs_com_coord": m.ubs_com_coord,
        "ubs_por_10k_hab": ubs_10k,
        "habitantes_por_ubs": m.populacao_2022 / m.ubs if m.ubs else 0.0,
        "gap_ubs_para_media_nacional": max(0.0, national_ubs_10k - ubs_10k) * m.populacao_2022 / 10_000,
        "cadastro_populacao_base": cadastro_denominator,
        "pessoas_vinculadas_aps": m.pessoas_vinculadas,
        "pct_populacao_vinculada_aps": cadastro_pct,
        "qtd_indicadores_previne": len(indicator_values),
        "media_indicadores_previne": avg_indicator,
        "indicadores_abaixo_50": below_50,
        "indicadores_abaixo_30": below_30,
        **{f"previne_{key}": value for key, value in sorted(m.indicadores_percentuais.items())},
    }


def score_opportunity(row: dict[str, str | int | float], national_ubs_10k: float) -> tuple[int, list[str]]:
    pop = int(row["populacao_2022"])
    score = 0
    flags: list[str] = []
    ubs = int(row["ubs"])
    ubs_10k = float(row["ubs_por_10k_hab"])
    cadastro = float(row["pct_populacao_vinculada_aps"])
    avg_prev = float(row["media_indicadores_previne"])
    below_50 = int(row["indicadores_abaixo_50"])
    below_30 = int(row["indicadores_abaixo_30"])

    if pop >= 5_000 and ubs == 0:
        flags.append("municipio_5k_mais_sem_ubs_no_arquivo")
        score += 5
    if pop >= 20_000 and ubs_10k < national_ubs_10k * 0.5:
        flags.append("ubs_por_10k_abaixo_de_50pct_media_nacional")
        score += 3
    if pop >= 20_000 and ubs and float(row["habitantes_por_ubs"]) > 10_000:
        flags.append("mais_de_10k_habitantes_por_ubs")
        score += 2
    if pop >= 20_000 and cadastro < 50:
        flags.append("vinculo_aps_abaixo_50pct")
        score += 4
    elif pop >= 20_000 and cadastro < 70:
        flags.append("vinculo_aps_entre_50_70pct")
        score += 2
    if pop >= 20_000 and avg_prev and avg_prev < 40:
        flags.append("media_indicadores_previne_abaixo_40")
        score += 4
    if pop >= 20_000 and below_50 >= 4:
        flags.append("quatro_ou_mais_indicadores_abaixo_50")
        score += 3
    if pop >= 20_000 and below_30 >= 3:
        flags.append("tres_ou_mais_indicadores_abaixo_30")
        score += 2
    return score, flags


def write_csv(path: Path, rows: list[dict[str, str | int | float]]) -> None:
    if not rows:
        return
    path.parent.mkdir(parents=True, exist_ok=True)
    fields: list[str] = []
    seen = set()
    for row in rows:
        for key in row:
            if key not in seen:
                fields.append(key)
                seen.add(key)
    with path.open("w", newline="", encoding="utf-8") as file:
        writer = csv.DictWriter(file, fieldnames=fields)
        writer.writeheader()
        writer.writerows(rows)


def aggregate_by(rows: list[dict[str, str | int | float]], key: str) -> list[dict[str, str | int | float]]:
    groups: dict[str, dict[str, float | str]] = {}
    for row in rows:
        name = str(row[key])
        if name not in groups:
            groups[name] = {
                key: name,
                "populacao_2022": 0.0,
                "ubs": 0.0,
                "pessoas_vinculadas_aps": 0.0,
                "cadastro_populacao_base": 0.0,
                "media_indicadores_previne_ponderada": 0.0,
                "municipios": 0.0,
            }
        g = groups[name]
        pop = float(row["populacao_2022"])
        g["populacao_2022"] = float(g["populacao_2022"]) + pop
        g["ubs"] = float(g["ubs"]) + float(row["ubs"])
        g["pessoas_vinculadas_aps"] = float(g["pessoas_vinculadas_aps"]) + float(row["pessoas_vinculadas_aps"])
        g["cadastro_populacao_base"] = float(g["cadastro_populacao_base"]) + float(row["cadastro_populacao_base"])
        g["media_indicadores_previne_ponderada"] = float(g["media_indicadores_previne_ponderada"]) + float(row["media_indicadores_previne"]) * pop
        g["municipios"] = float(g["municipios"]) + 1

    out: list[dict[str, str | int | float]] = []
    for g in groups.values():
        pop = float(g["populacao_2022"])
        cad_pop = float(g["cadastro_populacao_base"])
        out.append(
            {
                key: g[key],
                "municipios": int(g["municipios"]),
                "populacao_2022": int(pop),
                "ubs": int(g["ubs"]),
                "ubs_por_10k_hab": rate(float(g["ubs"]), pop, 10_000),
                "habitantes_por_ubs": pop / float(g["ubs"]) if float(g["ubs"]) else 0.0,
                "pct_populacao_vinculada_aps": pct(float(g["pessoas_vinculadas_aps"]), cad_pop),
                "media_indicadores_previne_ponderada": float(g["media_indicadores_previne_ponderada"]) / pop if pop else 0.0,
            }
        )
    return sorted(out, key=lambda row: str(row[key]))


def table_md(rows: list[dict[str, str | int | float]], columns: list[str], limit: int = 10) -> str:
    selected = rows[:limit]
    header = "| " + " | ".join(columns) + " |"
    sep = "| " + " | ".join(["---"] * len(columns)) + " |"
    body = []
    for row in selected:
        values = []
        for col in columns:
            value = row.get(col, "")
            if isinstance(value, float):
                values.append(fmt_float(value))
            elif isinstance(value, int):
                values.append(fmt_int(value))
            else:
                values.append(str(value))
        body.append("| " + " | ".join(values) + " |")
    return "\n".join([header, sep, *body])


def html_table(rows: list[dict[str, str | int | float]], columns: list[str], limit: int = 15) -> str:
    head = "".join(f"<th>{html.escape(col)}</th>" for col in columns)
    body = []
    for row in rows[:limit]:
        cells = []
        for col in columns:
            value = row.get(col, "")
            if isinstance(value, float):
                text = fmt_float(value)
            elif isinstance(value, int):
                text = fmt_int(value)
            else:
                text = str(value)
            cells.append(f"<td>{html.escape(text)}</td>")
        body.append("<tr>" + "".join(cells) + "</tr>")
    return "<table><thead><tr>" + head + "</tr></thead><tbody>" + "".join(body) + "</tbody></table>"


def bar_svg(rows: list[dict[str, str | int | float]], label_col: str, value_col: str, title: str, width: int = 980) -> str:
    height = 34 * len(rows) + 70
    max_value = max((float(row[value_col]) for row in rows), default=1.0) or 1.0
    label_width = 180
    chart_width = width - label_width - 130
    parts = [
        f'<svg viewBox="0 0 {width} {height}" width="{width}" height="{height}" role="img" aria-label="{html.escape(title)}">',
        '<style>text{font-family:Arial,sans-serif;font-size:12px}.title{font-size:16px;font-weight:700}.bar{fill:#2a9d8f}</style>',
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


def write_reports(
    rows: list[dict[str, str | int | float]],
    uf_rows: list[dict[str, str | int | float]],
    regiao_rows: list[dict[str, str | int | float]],
    opportunities: list[dict[str, str | int | float]],
    total_ubs_rows: int,
    total_cadastro_rows: int,
    total_indicador_rows: int,
    national_ubs_10k: float,
) -> None:
    REPORTS.mkdir(parents=True, exist_ok=True)
    total_pop = sum(int(row["populacao_2022"]) for row in rows)
    total_ubs = sum(int(row["ubs"]) for row in rows)
    municipalities_with_ubs = sum(1 for row in rows if int(row["ubs"]) > 0)
    municipalities_no_ubs_5k = sum(1 for row in rows if int(row["populacao_2022"]) >= 5_000 and int(row["ubs"]) == 0)
    municipalities_low_vinculo = sum(
        1 for row in rows if int(row["populacao_2022"]) >= 20_000 and float(row["pct_populacao_vinculada_aps"]) < 50
    )
    municipalities_low_previne = sum(
        1 for row in rows if int(row["populacao_2022"]) >= 20_000 and float(row["media_indicadores_previne"]) < 40
    )
    weighted_previne = sum(float(row["media_indicadores_previne"]) * int(row["populacao_2022"]) for row in rows) / total_pop
    pct_vinculo = pct(
        sum(float(row["pessoas_vinculadas_aps"]) for row in rows),
        sum(float(row["cadastro_populacao_base"]) for row in rows),
    )

    low_ubs = sorted(rows, key=lambda row: (float(row["ubs_por_10k_hab"]), -int(row["populacao_2022"])))
    low_vinculo = sorted(rows, key=lambda row: (float(row["pct_populacao_vinculada_aps"]), -int(row["populacao_2022"])))
    low_previne = sorted(rows, key=lambda row: (float(row["media_indicadores_previne"]), -int(row["populacao_2022"])))
    regiao_ubs = sorted(regiao_rows, key=lambda row: float(row["ubs_por_10k_hab"]))
    uf_ubs = sorted(uf_rows, key=lambda row: float(row["ubs_por_10k_hab"]))

    md = f"""# Analise complementar - Atencao Primaria SUS

Data de acesso: 2026-07-18

## Objetivo

Ampliar a investigacao para oportunidades relacionadas a atencao primaria, antes de escolher problema ou solucao final.

## Bases utilizadas

- Ministerio da Saude / Portal de Dados Abertos do SUS: Unidades Basicas de Saude - UBS, CSV atualizado em julho/2026.
- Ministerio da Saude / Sisab: Cadastro Vinculado do Programa Previne Brasil, competencia 202412.
- Ministerio da Saude / Sisab: Indicadores de Desempenho do Programa Previne Brasil, quadrimestre 2024Q3.
- IBGE / SIDRA: Censo Demografico 2022, populacao residente por municipio.

## Volume analisado

- Registros brutos de UBS: {fmt_int(total_ubs_rows)}.
- Registros brutos de Cadastro Vinculado: {fmt_int(total_cadastro_rows)}.
- Registros brutos de Indicadores de Desempenho: {fmt_int(total_indicador_rows)}.
- Municipios analisados: {fmt_int(len(rows))}.
- Populacao considerada: {fmt_int(total_pop)}.

## Indicadores principais

- UBS cadastradas no arquivo: {fmt_int(total_ubs)}.
- Municipios com ao menos uma UBS no arquivo: {fmt_int(municipalities_with_ubs)}.
- UBS por 10 mil habitantes, media nacional calculada: {fmt_float(national_ubs_10k)}.
- Municipios com 5 mil habitantes ou mais e zero UBS no arquivo: {fmt_int(municipalities_no_ubs_5k)}.
- Populacao vinculada na APS, percentual aproximado nacional: {fmt_float(pct_vinculo)}%.
- Media ponderada dos indicadores Previne Brasil analisados: {fmt_float(weighted_previne)}.
- Municipios com 20 mil habitantes ou mais e vinculo APS abaixo de 50%: {fmt_int(municipalities_low_vinculo)}.
- Municipios com 20 mil habitantes ou mais e media Previne abaixo de 40: {fmt_int(municipalities_low_previne)}.

## Regioes com menor taxa de UBS por 10 mil habitantes

{table_md(regiao_ubs, ["regiao", "populacao_2022", "ubs", "ubs_por_10k_hab", "pct_populacao_vinculada_aps", "media_indicadores_previne_ponderada"], 10)}

## UFs com menor taxa de UBS por 10 mil habitantes

{table_md(uf_ubs, ["uf", "populacao_2022", "ubs", "ubs_por_10k_hab", "pct_populacao_vinculada_aps", "media_indicadores_previne_ponderada"], 10)}

## Municipios com menor densidade de UBS

{table_md(low_ubs, ["uf", "municipio", "populacao_2022", "ubs", "ubs_por_10k_hab", "habitantes_por_ubs"], 15)}

## Municipios com menor vinculo aproximado na APS

{table_md(low_vinculo, ["uf", "municipio", "populacao_2022", "pct_populacao_vinculada_aps", "pessoas_vinculadas_aps"], 15)}

## Municipios com menor media dos indicadores Previne

{table_md(low_previne, ["uf", "municipio", "populacao_2022", "media_indicadores_previne", "indicadores_abaixo_50", "indicadores_abaixo_30"], 15)}

## Municipios priorizados pela heuristica de oportunidade

{table_md(opportunities, ["uf", "municipio", "populacao_2022", "ubs_por_10k_hab", "pct_populacao_vinculada_aps", "media_indicadores_previne", "sinais_de_oportunidade", "score_heuristico_oportunidade"], 20)}

## Interpretacao inicial

- Fato/dado: ha variacao territorial em densidade de UBS, vinculo cadastral APS e desempenho dos indicadores Previne.
- Interpretacao: oportunidades podem estar no acompanhamento ativo de pacientes, qualidade/registro da APS, priorizacao de visitas/cadastros e gestao territorial das UBS.
- Hipotese: municipios com baixa densidade de UBS, baixo vinculo e baixo desempenho em indicadores de cuidado podem se beneficiar de uma ferramenta de priorizacao operacional para equipes ou gestores.
- Limitacao: essas bases nao medem diretamente fila de consulta, agenda disponivel, qualidade clinica, deslocamento ou satisfacao do paciente.

## Arquivos gerados

- data/processed/aps_municipio_indicadores.csv
- data/processed/aps_uf_indicadores.csv
- data/processed/aps_regiao_indicadores.csv
- data/processed/aps_oportunidades_municipio.csv
- analytics/reports/analise_aps_sus.md
- analytics/reports/analise_aps_sus.html
"""
    (REPORTS / "analise_aps_sus.md").write_text(md, encoding="utf-8")

    html_doc = f"""<!doctype html>
<html lang="pt-BR">
<head>
  <meta charset="utf-8">
  <title>Analise complementar - APS SUS</title>
  <style>
    body {{ font-family: Arial, sans-serif; margin: 32px; color: #17202a; }}
    main {{ max-width: 1180px; margin: auto; }}
    h1, h2 {{ color: #164e63; }}
    .cards {{ display: grid; grid-template-columns: repeat(auto-fit, minmax(210px, 1fr)); gap: 12px; }}
    .card {{ border: 1px solid #d0d7de; border-radius: 6px; padding: 12px; background: #f8fafc; }}
    .metric {{ font-size: 24px; font-weight: 700; color: #0f766e; }}
    table {{ border-collapse: collapse; width: 100%; margin: 12px 0 28px; font-size: 13px; }}
    th, td {{ border: 1px solid #d0d7de; padding: 6px 8px; text-align: left; vertical-align: top; }}
    th {{ background: #edf2f7; }}
    .note {{ background: #fff7ed; border-left: 4px solid #f97316; padding: 10px 14px; }}
    .chart {{ overflow-x: auto; margin: 18px 0; }}
  </style>
</head>
<body>
<main>
  <h1>Analise complementar - Atencao Primaria SUS</h1>
  <p><strong>Data de acesso:</strong> 2026-07-18. Esta analise identifica sinais de oportunidade; nao prova causalidade.</p>
  <section class="cards">
    <div class="card"><div>UBS no arquivo</div><div class="metric">{fmt_int(total_ubs)}</div></div>
    <div class="card"><div>UBS / 10 mil hab.</div><div class="metric">{fmt_float(national_ubs_10k)}</div></div>
    <div class="card"><div>Vinculo APS aprox.</div><div class="metric">{fmt_float(pct_vinculo)}%</div></div>
    <div class="card"><div>Media Previne ponderada</div><div class="metric">{fmt_float(weighted_previne)}</div></div>
    <div class="card"><div>Municipios 20k+ vinculo &lt; 50%</div><div class="metric">{fmt_int(municipalities_low_vinculo)}</div></div>
    <div class="card"><div>Municipios 20k+ Previne &lt; 40</div><div class="metric">{fmt_int(municipalities_low_previne)}</div></div>
  </section>
  <h2>Regioes - UBS por 10 mil habitantes</h2>
  <div class="chart">{bar_svg(regiao_ubs, "regiao", "ubs_por_10k_hab", "Regioes por UBS por 10 mil habitantes")}</div>
  <h2>UFs com menor taxa de UBS</h2>
  {html_table(uf_ubs, ["uf", "populacao_2022", "ubs", "ubs_por_10k_hab", "pct_populacao_vinculada_aps", "media_indicadores_previne_ponderada"], 12)}
  <h2>Municipios priorizados</h2>
  {html_table(opportunities, ["uf", "municipio", "populacao_2022", "ubs_por_10k_hab", "pct_populacao_vinculada_aps", "media_indicadores_previne", "sinais_de_oportunidade", "score_heuristico_oportunidade"], 30)}
  <h2>Limites de interpretacao</h2>
  <div class="note">UBS cadastrada, vinculo cadastral e indicadores de desempenho sao proxies operacionais. Eles nao medem agenda disponivel, tempo de espera, qualidade clinica, deslocamento, satisfacao ou necessidade individual do paciente.</div>
</main>
</body>
</html>
"""
    (REPORTS / "analise_aps_sus.html").write_text(html_doc, encoding="utf-8")


def main() -> None:
    PROCESSED.mkdir(parents=True, exist_ok=True)
    municipalities = load_municipalities()
    total_ubs_rows = add_ubs(municipalities)
    total_cadastro_rows = add_cadastro_vinculado(municipalities)
    total_indicador_rows = add_indicadores(municipalities)

    total_pop = sum(m.populacao_2022 for m in municipalities.values())
    total_ubs = sum(m.ubs for m in municipalities.values())
    national_ubs_10k = rate(total_ubs, total_pop, 10_000)

    rows = [municipality_row(m, national_ubs_10k) for m in municipalities.values()]
    rows = sorted(rows, key=lambda row: (str(row["uf"]), str(row["municipio"])))
    opportunities: list[dict[str, str | int | float]] = []
    for row in rows:
        score, flags = score_opportunity(row, national_ubs_10k)
        if not flags:
            continue
        opportunity = dict(row)
        opportunity["sinais_de_oportunidade"] = ";".join(flags)
        opportunity["score_heuristico_oportunidade"] = score
        opportunities.append(opportunity)
    opportunities = sorted(
        opportunities,
        key=lambda row: (
            -int(row["score_heuristico_oportunidade"]),
            float(row["media_indicadores_previne"]),
            float(row["pct_populacao_vinculada_aps"]),
            -int(row["populacao_2022"]),
        ),
    )

    uf_rows = aggregate_by(rows, "uf")
    regiao_rows = aggregate_by(rows, "regiao")

    write_csv(PROCESSED / "aps_municipio_indicadores.csv", rows)
    write_csv(PROCESSED / "aps_uf_indicadores.csv", uf_rows)
    write_csv(PROCESSED / "aps_regiao_indicadores.csv", regiao_rows)
    write_csv(PROCESSED / "aps_oportunidades_municipio.csv", opportunities)
    write_reports(
        rows,
        uf_rows,
        regiao_rows,
        opportunities,
        total_ubs_rows,
        total_cadastro_rows,
        total_indicador_rows,
        national_ubs_10k,
    )

    print("Analise APS concluida")
    print(f"Municipios: {len(rows)}")
    print(f"UBS no arquivo: {total_ubs_rows}")
    print(f"Oportunidades sinalizadas: {len(opportunities)}")
    print(f"Relatorio: {REPORTS / 'analise_aps_sus.md'}")


if __name__ == "__main__":
    main()
