# Execucao E2E da APS

O executor E2E usa a API real em containers Docker e PostgreSQL dedicado. Ele
nao usa `MockMvc`, H2 ou a porta do ambiente demonstrativo local.

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\run-e2e.ps1
```

O comando sobe `docker-compose.e2e.yml` na porta `8215`, desabilita a massa
demonstrativa, reinicia apenas o volume `aps_e2e_postgres_data` e executa:

1. criacao de territorio em prioridade alta;
2. criacao de acao territorial;
3. atualizacao do progresso agregado;
4. tentativa invalida de conclusao sem contatos;
5. leitura persistida do territorio;
6. consulta ao painel consolidado.

Ao final, o script sobrescreve `relatorio_execucao_e2e.md`. Esse arquivo contem
o JSON recebido pela API, o caso de uso/regra aplicado, o JSON devolvido e as
assercoes de cada etapa.

Para parar somente o ambiente E2E e remover sua base descartavel:

```powershell
docker compose -f docker-compose.e2e.yml down --volumes --remove-orphans
```
