from __future__ import annotations

import csv
import html
from collections import defaultdict
from pathlib import Path


ROOT = Path(__file__).resolve().parents[2]
PROCESSED = ROOT / "data" / "processed"
REPORTS = ROOT / "analytics" / "reports"

APS_CSV = PROCESSED / "aps_municipio_indicadores.csv"
LEITOS_CSV = PROCESSED / "leitos_municipio_indicadores_2026.csv"


def parse_int(value: str | int | float | None) -> int:
    if value is None:
        return 0
    if isinstance(value, int):
        return value
    if isinstance(value, float):
        return int(value)
    cleaned = str(value).strip()
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


def fmt_int(value: int | float) -> str:
    return f"{value:,.0f}".replace(",", ".")


def fmt_float(value: float, digits: int = 2) -> str:
    return f"{value:.{digits}f}".replace(".", ",")


def rate(numerator: float, denominator: float, multiplier: int) -> float:
    return numerator / denominator * multiplier if denominator else 0.0


def pct(numerator: float, denominator: float) -> float:
    return numerator / denominator * 100 if denominator else 0.0


def read_csv(path: Path) -> list[dict[str, str]]:
    with path.open(newline="", encoding="utf-8") as file:
        return list(csv.DictReader(file))


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


def html_table(rows: list[dict[str, str | int | float]], columns: list[str], limit: int = 20) -> str:
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
    label_width = 220
    chart_width = width - label_width - 120
    parts = [
        f'<svg viewBox="0 0 {width} {height}" width="{width}" height="{height}" role="img" aria-label="{html.escape(title)}">',
        '<style>text{font-family:Arial,sans-serif;font-size:12px}.title{font-size:16px;font-weight:700}.bar{fill:#2563eb}</style>',
        f'<text class="title" x="0" y="20">{html.escape(title)}</text>',
    ]
    y = 46
    for row in rows:
        label = html.escape(str(row[label_col]))
        value = float(row[value_col])
        bar_width = max(2, value / max_value * chart_width)
        parts.append(f'<text x="0" y="{y + 14}">{label}</text>')
        parts.append(f'<rect class="bar" x="{label_width}" y="{y}" width="{bar_width:.1f}" height="18" rx="2"></rect>')
        parts.append(f'<text x="{label_width + bar_width + 8:.1f}" y="{y + 14}">{fmt_int(value)}</text>')
        y += 34
    parts.append("</svg>")
    return "\n".join(parts)


def classify(
    aps: dict[str, str],
    leitos: dict[str, str],
    national_ubs_10k: float,
    national_leitos_10k: float,
    national_uti_100k: float,
) -> tuple[int, list[str], list[str], list[str], list[str]]:
    pop = parse_int(aps["populacao_2022"])
    ubs = parse_int(aps["ubs"])
    ubs_10k = parse_float(aps["ubs_por_10k_hab"])
    vinculo = parse_float(aps["pct_populacao_vinculada_aps"])
    previne = parse_float(aps["media_indicadores_previne"])
    leitos_sus = parse_int(leitos["leitos_sus"])
    uti_sus = parse_int(leitos["uti_total_sus"])
    leitos_10k = parse_float(leitos["leitos_sus_por_10k_hab"])
    uti_100k = parse_float(leitos["uti_sus_por_100k_hab"])

    score = 0
    sinais_aps: list[str] = []
    sinais_hospitalares: list[str] = []
    sinais_integrados: list[str] = []
    oportunidades: list[str] = []

    low_ubs = pop >= 20_000 and ubs_10k < national_ubs_10k * 0.5
    low_vinculo = pop >= 20_000 and vinculo < 50
    mid_vinculo = pop >= 20_000 and 50 <= vinculo < 70
    low_previne = pop >= 20_000 and previne < 40
    low_beds = pop >= 20_000 and leitos_10k < national_leitos_10k * 0.75
    very_low_beds = pop >= 20_000 and leitos_10k < national_leitos_10k * 0.5
    low_uti = pop >= 100_000 and uti_100k < national_uti_100k * 0.5

    if pop >= 5_000 and ubs == 0:
        sinais_aps.append("municipio_5k_mais_sem_ubs_no_arquivo")
        score += 5
    if low_ubs:
        sinais_aps.append("ubs_por_10k_abaixo_de_50pct_media_nacional")
        score += 3
    if pop >= 20_000 and ubs and parse_float(aps["habitantes_por_ubs"]) > 10_000:
        sinais_aps.append("mais_de_10k_habitantes_por_ubs")
        score += 2
    if low_vinculo:
        sinais_aps.append("vinculo_aps_abaixo_50pct")
        score += 4
    elif mid_vinculo:
        sinais_aps.append("vinculo_aps_entre_50_70pct")
        score += 2
    if low_previne:
        sinais_aps.append("media_indicadores_previne_abaixo_40")
        score += 4

    if pop >= 20_000 and leitos_sus == 0:
        sinais_hospitalares.append("municipio_20k_mais_sem_leitos_sus_no_arquivo")
        score += 4
    elif very_low_beds:
        sinais_hospitalares.append("leitos_sus_por_10k_abaixo_de_50pct_media_nacional")
        score += 4
    elif low_beds:
        sinais_hospitalares.append("leitos_sus_por_10k_entre_50_75pct_media_nacional")
        score += 2
    if pop >= 100_000 and uti_sus == 0:
        sinais_hospitalares.append("municipio_100k_mais_sem_uti_sus_no_arquivo")
        score += 5
    elif low_uti:
        sinais_hospitalares.append("uti_sus_por_100k_abaixo_de_50pct_media_nacional")
        score += 3

    if (low_vinculo or low_previne) and low_beds:
        sinais_integrados.append("aps_fragil_e_leitos_sus_baixos")
        oportunidades.append("regulacao_integrada_aps_e_retaguarda")
        score += 3
    if low_previne and (low_uti or (pop >= 100_000 and uti_sus == 0)):
        sinais_integrados.append("desempenho_aps_baixo_e_uti_sus_critica")
        oportunidades.append("monitoramento_risco_e_encaminhamento_precoce")
        score += 4
    if low_ubs and low_vinculo:
        sinais_integrados.append("baixa_densidade_ubs_e_baixo_vinculo")
        oportunidades.append("gestao_territorial_e_busca_ativa_aps")
        score += 3
    if low_vinculo and low_previne:
        sinais_integrados.append("baixo_vinculo_e_baixo_desempenho_previne")
        oportunidades.append("gestao_de_carteira_e_qualidade_do_registro")
        score += 3
    if (ubs == 0 and pop >= 5_000) or (leitos_sus == 0 and pop >= 20_000):
        oportunidades.append("validacao_cadastral_cnes_e_mapa_de_oferta")

    return score, sinais_aps, sinais_hospitalares, sinais_integrados, sorted(set(oportunidades))


def aggregate_uf(rows: list[dict[str, str | int | float]]) -> list[dict[str, str | int | float]]:
    groups: dict[str, dict[str, float | str]] = defaultdict(
        lambda: {
            "uf": "",
            "regiao": "",
            "municipios_priorizados": 0.0,
            "municipios_alta_prioridade": 0.0,
            "populacao_priorizada": 0.0,
            "populacao_alta_prioridade": 0.0,
            "score_pop": 0.0,
            "total_pop": 0.0,
        }
    )
    for row in rows:
        uf = str(row["uf"])
        g = groups[uf]
        g["uf"] = uf
        g["regiao"] = str(row["regiao"])
        pop = float(row["populacao_2022"])
        score = float(row["score_integrado"])
        g["municipios_priorizados"] = float(g["municipios_priorizados"]) + 1
        g["populacao_priorizada"] = float(g["populacao_priorizada"]) + pop
        g["score_pop"] = float(g["score_pop"]) + score * pop
        g["total_pop"] = float(g["total_pop"]) + pop
        if score >= 14:
            g["municipios_alta_prioridade"] = float(g["municipios_alta_prioridade"]) + 1
            g["populacao_alta_prioridade"] = float(g["populacao_alta_prioridade"]) + pop

    return sorted(
        [
            {
                "uf": g["uf"],
                "regiao": g["regiao"],
                "municipios_priorizados": int(g["municipios_priorizados"]),
                "municipios_alta_prioridade": int(g["municipios_alta_prioridade"]),
                "populacao_priorizada": int(g["populacao_priorizada"]),
                "populacao_alta_prioridade": int(g["populacao_alta_prioridade"]),
                "score_medio_ponderado": float(g["score_pop"]) / float(g["total_pop"]) if float(g["total_pop"]) else 0.0,
            }
            for g in groups.values()
        ],
        key=lambda row: (-int(row["populacao_alta_prioridade"]), -float(row["score_medio_ponderado"])),
    )


def write_reports(
    rows: list[dict[str, str | int | float]],
    uf_rows: list[dict[str, str | int | float]],
    national_ubs_10k: float,
    national_leitos_10k: float,
    national_uti_100k: float,
) -> None:
    REPORTS.mkdir(parents=True, exist_ok=True)
    total_pop = sum(int(row["populacao_2022"]) for row in rows)
    high_priority = [row for row in rows if int(row["score_integrado"]) >= 14]
    aps_beds = [
        row
        for row in rows
        if "aps_fragil_e_leitos_sus_baixos" in str(row["sinais_integrados"]).split(";")
    ]
    aps_uti = [
        row
        for row in rows
        if "desempenho_aps_baixo_e_uti_sus_critica" in str(row["sinais_integrados"]).split(";")
    ]
    large = sorted(
        [row for row in rows if int(row["populacao_2022"]) >= 100_000],
        key=lambda row: (-int(row["score_integrado"]), -int(row["populacao_2022"])),
    )
    top = sorted(rows, key=lambda row: (-int(row["score_integrado"]), -int(row["populacao_2022"])))

    columns = [
        "uf",
        "municipio",
        "populacao_2022",
        "ubs_por_10k_hab",
        "pct_populacao_vinculada_aps",
        "media_indicadores_previne",
        "leitos_sus_por_10k_hab",
        "uti_sus_por_100k_hab",
        "score_integrado",
        "oportunidades_geradas",
    ]
    uf_columns = [
        "uf",
        "regiao",
        "municipios_priorizados",
        "municipios_alta_prioridade",
        "populacao_alta_prioridade",
        "score_medio_ponderado",
    ]

    md = f"""# Sintese integrada de oportunidades SUS

Data de acesso: 2026-07-18

## Objetivo

Cruzar capacidade hospitalar SUS, densidade de UBS, vinculo cadastral APS e desempenho SISAB/Previne para procurar oportunidades mais fortes antes da escolha de solucao.

## Indicadores nacionais calculados

- UBS por 10 mil habitantes: {fmt_float(national_ubs_10k)}.
- Leitos SUS por 10 mil habitantes: {fmt_float(national_leitos_10k)}.
- UTI SUS por 100 mil habitantes: {fmt_float(national_uti_100k)}.

## Sinais integrados

- Municipios com algum sinal integrado: {fmt_int(len(rows))}.
- Populacao nesses municipios: {fmt_int(total_pop)}.
- Municipios de alta prioridade heuristica, score >= 14: {fmt_int(len(high_priority))}.
- Populacao em municipios de alta prioridade: {fmt_int(sum(int(row["populacao_2022"]) for row in high_priority))}.
- Municipios com APS fragil e leitos SUS baixos: {fmt_int(len(aps_beds))}.
- Populacao em municipios com APS fragil e leitos SUS baixos: {fmt_int(sum(int(row["populacao_2022"]) for row in aps_beds))}.
- Municipios com desempenho APS baixo e UTI SUS critica: {fmt_int(len(aps_uti))}.
- Populacao em municipios com desempenho APS baixo e UTI SUS critica: {fmt_int(sum(int(row["populacao_2022"]) for row in aps_uti))}.

## Municipios priorizados pelo cruzamento

{table_md(top, columns, 20)}

## Municipios grandes priorizados

{table_md(large, columns, 20)}

## UFs com maior populacao em municipios de alta prioridade

{table_md(uf_rows, uf_columns, 15)}

## Oportunidades reforcadas

- Regulacao integrada APS e retaguarda: priorizar encaminhamentos, referencias e disponibilidade de leitos onde a porta de entrada e a capacidade hospitalar sao simultaneamente pressionadas.
- Gestao ativa de carteira APS: buscar pacientes sem acompanhamento consistente, especialmente onde vinculo cadastral e indicadores Previne aparecem baixos.
- Mapa de oferta e validacao cadastral: investigar municipios com zero UBS ou zero leitos SUS no arquivo, separando ausencia real de oferta, cadastro incompleto e dependencia regional.
- Monitoramento de risco e encaminhamento precoce: usar indicadores APS baixos como alerta para evitar agravamento e reduzir demanda evitavel por urgencia/hospital.
- Planejamento regional: apoiar pactuacao entre municipios quando a demanda local depende de retaguarda fora do proprio municipio.

## Limites

- A heuristica nao mede tempo de espera, absenteismo, agenda, qualidade clinica, deslocamento real nem satisfacao do paciente.
- Zero UBS, zero leitos ou zero UTI no arquivo deve ser tratado como sinal para validacao com CNES/gestao local, nao como conclusao isolada.
- Populacao usa Censo 2022 e as bases operacionais usam competencias diferentes: leitos 202605, UBS julho/2026, SISAB 2024Q3/202412.
"""
    (REPORTS / "sintese_oportunidades_integradas_sus.md").write_text(md, encoding="utf-8")

    html_doc = f"""<!doctype html>
<html lang="pt-BR">
<head>
  <meta charset="utf-8">
  <title>Sintese integrada de oportunidades SUS</title>
  <style>
    body {{ font-family: Arial, sans-serif; margin: 32px; color: #17202a; }}
    main {{ max-width: 1220px; margin: auto; }}
    h1, h2 {{ color: #1f2937; }}
    .cards {{ display: grid; grid-template-columns: repeat(auto-fit, minmax(220px, 1fr)); gap: 12px; }}
    .card {{ border: 1px solid #d0d7de; border-radius: 6px; padding: 12px; background: #f8fafc; }}
    .metric {{ font-size: 24px; font-weight: 700; color: #1d4ed8; }}
    table {{ border-collapse: collapse; width: 100%; margin: 12px 0 28px; font-size: 13px; }}
    th, td {{ border: 1px solid #d0d7de; padding: 6px 8px; text-align: left; vertical-align: top; }}
    th {{ background: #e5e7eb; }}
    .note {{ background: #fff7ed; border-left: 4px solid #f97316; padding: 10px 14px; }}
    .chart {{ overflow-x: auto; margin: 18px 0; }}
  </style>
</head>
<body>
<main>
  <h1>Sintese integrada de oportunidades SUS</h1>
  <p><strong>Data de acesso:</strong> 2026-07-18. O cruzamento e uma priorizacao exploratoria, nao uma prova causal.</p>
  <section class="cards">
    <div class="card"><div>Municipios com sinal integrado</div><div class="metric">{fmt_int(len(rows))}</div></div>
    <div class="card"><div>Alta prioridade</div><div class="metric">{fmt_int(len(high_priority))}</div></div>
    <div class="card"><div>Pop. alta prioridade</div><div class="metric">{fmt_int(sum(int(row["populacao_2022"]) for row in high_priority))}</div></div>
    <div class="card"><div>APS fragil + leitos baixos</div><div class="metric">{fmt_int(len(aps_beds))}</div></div>
    <div class="card"><div>APS baixa + UTI critica</div><div class="metric">{fmt_int(len(aps_uti))}</div></div>
  </section>
  <h2>UFs com maior populacao em alta prioridade</h2>
  <div class="chart">{bar_svg(uf_rows[:12], "uf", "populacao_alta_prioridade", "Populacao em municipios de alta prioridade")}</div>
  <h2>Municipios priorizados</h2>
  {html_table(top, columns, 30)}
  <h2>Municipios grandes priorizados</h2>
  {html_table(large, columns, 30)}
  <h2>Limites</h2>
  <div class="note">Os sinais devem orientar investigacao e desenho de MVP. Eles nao substituem dados locais de fila, agenda, deslocamento, absenteismo ou validacao cadastral.</div>
</main>
</body>
</html>
"""
    (REPORTS / "sintese_oportunidades_integradas_sus.html").write_text(html_doc, encoding="utf-8")


def main() -> None:
    aps_rows = read_csv(APS_CSV)
    leitos_rows = {row["co_ibge6"].zfill(6): row for row in read_csv(LEITOS_CSV)}

    total_pop = sum(parse_int(row["populacao_2022"]) for row in aps_rows)
    national_ubs_10k = rate(sum(parse_int(row["ubs"]) for row in aps_rows), total_pop, 10_000)
    national_leitos_10k = rate(sum(parse_int(row["leitos_sus"]) for row in leitos_rows.values()), total_pop, 10_000)
    national_uti_100k = rate(sum(parse_int(row["uti_total_sus"]) for row in leitos_rows.values()), total_pop, 100_000)

    opportunities: list[dict[str, str | int | float]] = []
    for aps in aps_rows:
        code = aps["co_ibge6"].zfill(6)
        leitos = leitos_rows.get(code)
        if not leitos:
            continue
        score, sinais_aps, sinais_hospitalares, sinais_integrados, oportunidades_geradas = classify(
            aps,
            leitos,
            national_ubs_10k,
            national_leitos_10k,
            national_uti_100k,
        )
        if not sinais_integrados and score < 10:
            continue
        opportunities.append(
            {
                "co_ibge6": code,
                "municipio": aps["municipio"],
                "uf": aps["uf"],
                "regiao": aps["regiao"],
                "populacao_2022": parse_int(aps["populacao_2022"]),
                "ubs": parse_int(aps["ubs"]),
                "ubs_por_10k_hab": parse_float(aps["ubs_por_10k_hab"]),
                "pct_populacao_vinculada_aps": parse_float(aps["pct_populacao_vinculada_aps"]),
                "media_indicadores_previne": parse_float(aps["media_indicadores_previne"]),
                "leitos_sus": parse_int(leitos["leitos_sus"]),
                "leitos_sus_por_10k_hab": parse_float(leitos["leitos_sus_por_10k_hab"]),
                "uti_total_sus": parse_int(leitos["uti_total_sus"]),
                "uti_sus_por_100k_hab": parse_float(leitos["uti_sus_por_100k_hab"]),
                "gap_estimado_leitos_sus_para_media_nacional": parse_float(leitos["gap_estimado_leitos_sus_para_media_nacional"]),
                "gap_estimado_uti_sus_para_media_nacional": parse_float(leitos["gap_estimado_uti_sus_para_media_nacional"]),
                "sinais_aps": ";".join(sinais_aps),
                "sinais_hospitalares": ";".join(sinais_hospitalares),
                "sinais_integrados": ";".join(sinais_integrados),
                "oportunidades_geradas": ";".join(oportunidades_geradas),
                "score_integrado": score,
            }
        )

    opportunities = sorted(
        opportunities,
        key=lambda row: (-int(row["score_integrado"]), -int(row["populacao_2022"])),
    )
    uf_rows = aggregate_uf(opportunities)

    write_csv(PROCESSED / "oportunidades_integradas_municipio.csv", opportunities)
    write_csv(PROCESSED / "oportunidades_integradas_uf.csv", uf_rows)
    write_reports(opportunities, uf_rows, national_ubs_10k, national_leitos_10k, national_uti_100k)

    print("Analise integrada concluida")
    print(f"Municipios com sinais integrados: {len(opportunities)}")
    print(f"Relatorio: {REPORTS / 'sintese_oportunidades_integradas_sus.md'}")


if __name__ == "__main__":
    main()
