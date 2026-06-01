#!/bin/bash
# Rebuild completo do triage-service sem cache

set -e

echo "=== Parando triage-service ==="
docker compose stop triage-service

echo ""
echo "=== Removendo container antigo ==="
docker compose rm -f triage-service

echo ""
echo "=== Removendo imagem antiga ==="
docker rmi tech-challenge-fase-05-triage-service 2>/dev/null || echo "Imagem não existe ainda"

echo ""
echo "=== Compilando projeto Maven ==="
cd /home/emerson/projetos/fiap/tech-challenge-fase-05
mvn clean package -pl triage-service -am -DskipTests -Dspotless.check.skip=true

echo ""
echo "=== Construindo nova imagem Docker (sem cache) ==="
docker compose build --no-cache triage-service

echo ""
echo "=== Iniciando triage-service ==="
docker compose up -d triage-service

echo ""
echo "=== Aguardando 5 segundos ==="
sleep 5

echo ""
echo "=== Logs do triage-service ==="
docker compose logs --tail=50 triage-service

echo ""
echo "=== Para acompanhar logs em tempo real, execute: ==="
echo "docker compose logs -f triage-service"
