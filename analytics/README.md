# Analises reproduziveis

Esta pasta separa processamento de dados da documentacao de produto.

- `scripts/`: scripts Python que leem os arquivos preservados em `data/raw` e
  produzem os indicadores.
- `reports/`: relatorios Markdown e HTML regenerados pelos scripts.
- `data/processed/`: saidas tabulares usadas nas sinteses e verificacoes.

Execute, a partir da raiz do repositorio:

```powershell
python analytics/scripts/analisar_aps_sus.py
python analytics/scripts/analisar_leitos_sus.py
python analytics/scripts/analisar_oportunidades_integradas_sus.py
```

As fontes, competencias, hashes e limites de interpretacao estao em
[`docs/dados/fontes-oficiais.md`](../docs/dados/fontes-oficiais.md). Os
resultados sao evidencias agregadas para investigacao territorial; nao devem ser
usados como classificacao clinica individual.
