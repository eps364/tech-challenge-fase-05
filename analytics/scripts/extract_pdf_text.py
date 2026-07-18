from __future__ import annotations

import argparse
import re
import zlib
from pathlib import Path


STREAM_RE = re.compile(rb"stream\r?\n(.*?)\r?\nendstream", re.S)
BT_RE = re.compile(rb"BT(.*?)ET", re.S)
TM_RE = re.compile(rb"([-+]?\d*\.?\d+)\s+([-+]?\d*\.?\d+)\s+([-+]?\d*\.?\d+)\s+([-+]?\d*\.?\d+)\s+([-+]?\d*\.?\d+)\s+([-+]?\d*\.?\d+)\s+Tm")
PDF_STRING_OR_HEX_RE = re.compile(rb"\((?:\\.|[^\\)])*\)|<([0-9A-Fa-f\s]+)>")


def _decode_pdf_literal(raw: bytes) -> str:
    assert raw.startswith(b"(") and raw.endswith(b")")
    body = raw[1:-1]
    out = bytearray()
    i = 0
    escapes = {
        ord("n"): ord("\n"),
        ord("r"): ord("\r"),
        ord("t"): ord("\t"),
        ord("b"): ord("\b"),
        ord("f"): ord("\f"),
        ord("("): ord("("),
        ord(")"): ord(")"),
        ord("\\"): ord("\\"),
    }
    while i < len(body):
        c = body[i]
        if c != ord("\\"):
            out.append(c)
            i += 1
            continue
        i += 1
        if i >= len(body):
            break
        esc = body[i]
        if esc in escapes:
            out.append(escapes[esc])
            i += 1
        elif 48 <= esc <= 55:
            octal = bytes([esc])
            i += 1
            for _ in range(2):
                if i < len(body) and 48 <= body[i] <= 55:
                    octal += bytes([body[i]])
                    i += 1
                else:
                    break
            out.append(int(octal, 8))
        else:
            out.append(esc)
            i += 1
    return out.decode("latin-1", errors="replace")


def _decode_pdf_hex(raw: bytes) -> str:
    hex_bytes = re.sub(rb"\s+", b"", raw[1:-1])
    if len(hex_bytes) % 2:
        hex_bytes += b"0"
    data = bytes.fromhex(hex_bytes.decode("ascii"))
    if data.startswith(b"\xfe\xff"):
        return data[2:].decode("utf-16-be", errors="replace")
    return data.decode("latin-1", errors="replace")


def _extract_strings(block: bytes) -> str:
    parts: list[str] = []
    for match in PDF_STRING_OR_HEX_RE.finditer(block):
        token = match.group(0)
        if token.startswith(b"("):
            text = _decode_pdf_literal(token)
        else:
            text = _decode_pdf_hex(token)
        if text.strip():
            parts.append(text)
        elif text:
            parts.append(" ")
    return "".join(parts)


def _fix_encoding(text: str) -> str:
    text = text.replace("\x00", "")
    text = text.replace("\x01Â”", "-").replace("\x01|", "-")
    if "Ã" not in text and "Â" not in text:
        return text
    try:
        fixed = text.encode("latin-1").decode("utf-8")
    except UnicodeError:
        fixed = text.encode("latin-1", errors="ignore").decode("utf-8", errors="replace")
    return fixed or text


def _extract_text_blocks(stream: bytes, page: int) -> list[tuple[int, float, float, str]]:
    blocks: list[tuple[int, float, float, str]] = []
    for bt in BT_RE.finditer(stream):
        block = bt.group(1)
        tm = TM_RE.search(block)
        if not tm:
            continue
        x = float(tm.group(5))
        y = float(tm.group(6))
        text = _fix_encoding(re.sub(r"\s+", " ", _extract_strings(block))).strip()
        if text:
            blocks.append((page, y, x, text))
    return blocks


def extract_pdf_text(pdf_path: Path) -> str:
    data = pdf_path.read_bytes()
    pages: list[list[tuple[int, float, float, str]]] = []
    page_number = 0
    for match in STREAM_RE.finditer(data):
        header = data[max(0, match.start() - 300) : match.start()]
        if b"/FlateDecode" not in header:
            continue
        try:
            stream = zlib.decompress(match.group(1))
        except zlib.error:
            continue
        blocks = _extract_text_blocks(stream, page_number + 1)
        if blocks:
            page_number += 1
            pages.append(blocks)

    output: list[str] = []
    for page_index, blocks in enumerate(pages, 1):
        output.append(f"\n\n--- PAGINA {page_index} ---\n")
        current_y: float | None = None
        current_line: list[str] = []
        for _, y, _x, text in sorted(blocks, key=lambda item: (-item[1], item[2])):
            if current_y is None or abs(current_y - y) <= 2.5:
                current_line.append(text)
                current_y = y if current_y is None else current_y
            else:
                output.append(" ".join(current_line).strip())
                current_line = [text]
                current_y = y
        if current_line:
            output.append(" ".join(current_line).strip())
    return "\n".join(output).strip() + "\n"


def main() -> None:
    parser = argparse.ArgumentParser()
    parser.add_argument("pdf", type=Path)
    parser.add_argument("output", type=Path)
    args = parser.parse_args()

    text = extract_pdf_text(args.pdf)
    args.output.parent.mkdir(parents=True, exist_ok=True)
    args.output.write_text(text, encoding="utf-8")
    print(f"Texto extraido para {args.output}")


if __name__ == "__main__":
    main()
