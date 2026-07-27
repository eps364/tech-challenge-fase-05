#!/usr/bin/env bash
# Gera docs/document.pdf a partir de docs/document.md
# Dependencias: pandoc, wkhtmltopdf
# Uso: bash scripts/generate-pdf.sh

set -euo pipefail

REPO_ROOT="$(cd "$(dirname "$0")/.." && pwd)"
MD_FILE="$REPO_ROOT/docs/document.md"
CSS_FILE="$REPO_ROOT/.vscode/markdown-pdf-plus-abnt.css"
HTML_TMP="$(mktemp /tmp/document_aps_XXXX.html)"
PDF_FILE="$REPO_ROOT/docs/document.pdf"

echo "==> Convertendo Markdown para HTML..."
pandoc "$MD_FILE" \
  --standalone \
  --embed-resources \
  --css "$CSS_FILE" \
  --resource-path="$REPO_ROOT/docs" \
  --metadata title="Relatorio de Projeto - SUS-Connect APS" \
  -o "$HTML_TMP"

echo "==> Gerando PDF com wkhtmltopdf..."
wkhtmltopdf \
  --page-size A4 \
  --margin-top 30mm \
  --margin-bottom 20mm \
  --margin-left 30mm \
  --margin-right 20mm \
  --encoding utf-8 \
  --enable-local-file-access \
  --quiet \
  "$HTML_TMP" \
  "$PDF_FILE"

rm -f "$HTML_TMP"
echo "==> PDF gerado: $PDF_FILE ($(du -sh "$PDF_FILE" | cut -f1))"
