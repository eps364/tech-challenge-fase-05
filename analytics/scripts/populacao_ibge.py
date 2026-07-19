from __future__ import annotations

from collections import defaultdict
from dataclasses import dataclass
from pathlib import Path
from zipfile import ZipFile
from xml.etree import ElementTree as ET


TABLE_NS = "urn:oasis:names:tc:opendocument:xmlns:table:1.0"
TABLE = f"{{{TABLE_NS}}}"


@dataclass(frozen=True)
class PopulationEstimate:
    co_ibge6: str
    municipio: str
    populacao_2025: int


def parse_population(value: str) -> int:
    digits = "".join(char for char in value if char.isdigit())
    return int(digits) if digits else 0


def cell_texts(row: ET.Element) -> list[str]:
    values: list[str] = []
    for cell in row.findall(f"{TABLE}table-cell"):
        repeat = int(cell.attrib.get(f"{TABLE}number-columns-repeated", "1"))
        value = "".join(cell.itertext()).strip()
        values.extend([value] * repeat)
    return values


def load_municipality_population_estimates(path: Path) -> dict[str, PopulationEstimate]:
    with ZipFile(path) as archive:
        root = ET.fromstring(archive.read("content.xml"))

    municipality_sheet = next(
        (
            sheet
            for sheet in root.findall(f".//{TABLE}table")
            if sheet.attrib.get(f"{TABLE}name", "").lower().startswith("munic")
        ),
        None,
    )
    if municipality_sheet is None:
        raise RuntimeError("Planilha de municipios nao encontrada na estimativa IBGE")

    estimates: dict[str, PopulationEstimate] = {}
    for row in municipality_sheet.findall(f"{TABLE}table-row"):
        values = cell_texts(row)
        if len(values) < 5 or values[0] == "UF":
            continue

        uf_code = "".join(char for char in values[1] if char.isdigit()).zfill(2)
        municipality_code = "".join(char for char in values[2] if char.isdigit()).zfill(5)
        population = parse_population(values[4])
        if not uf_code or not municipality_code or not population:
            continue

        code7 = f"{uf_code}{municipality_code}"
        estimates[code7[:6]] = PopulationEstimate(
            co_ibge6=code7[:6],
            municipio=values[3],
            populacao_2025=population,
        )

    if not estimates:
        raise RuntimeError("Nenhuma estimativa municipal foi lida do arquivo IBGE")
    return estimates


def aggregate_population_by_uf(estimates: dict[str, PopulationEstimate]) -> dict[str, int]:
    totals: dict[str, int] = defaultdict(int)
    for estimate in estimates.values():
        totals[estimate.co_ibge6[:2]] += estimate.populacao_2025
    return dict(totals)
