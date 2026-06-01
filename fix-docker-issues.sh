#!/bin/bash
# Script para corrigir problemas do Docker com Keycloak e Kafka

set -e  # Parar em caso de erro

echo "=== Parando todos os containers ==="
docker compose down

echo ""
echo "=== Removendo volumes (isso apagará todos os dados dos bancos) ==="
read -p "Confirma a remoção dos volumes? (s/N) " -n 1 -r
echo
if [[ $REPLY =~ ^[Ss]$ ]]; then
    docker compose down -v
    echo "Volumes removidos!"
else
    echo "Mantendo volumes existentes..."
fi

echo ""
echo "=== Removendo volumes órfãos ==="
docker volume prune -f

echo ""
echo "=== Limpando containers parados ==="
docker container prune -f

echo ""
echo "=== Verificando se as imagens do Keycloak existem ==="
if docker images | grep -q "quay.io/keycloak/keycloak"; then
    echo "Imagem do Keycloak já existe localmente"
else
    echo "Baixando imagem do Keycloak..."
    docker pull quay.io/keycloak/keycloak:26.5.4
fi

echo ""
echo "=== Construindo imagens dos serviços ==="
docker compose build

echo ""
echo "=== Iniciando serviços em ordem ==="
echo "1. Bancos de dados e infraestrutura..."
docker compose up -d keycloak-postgres zookeeper redis \
    auth-service-db triage-postgres appointment-postgres medical-record-postgres

echo "Aguardando bancos de dados iniciarem (20s)..."
sleep 20

echo ""
echo "2. Kafka..."
docker compose up -d kafka

echo "Aguardando Kafka iniciar (20s)..."
sleep 20

echo ""
echo "3. Keycloak..."
docker compose up -d keycloak

echo "Aguardando Keycloak iniciar (30s)..."
sleep 30

echo ""
echo "4. Config Server e Service Registry..."
docker compose up -d config-server service-registry

echo "Aguardando Config Server e Registry (20s)..."
sleep 20

echo ""
echo "5. API Gateway e demais serviços..."
docker compose up -d

echo ""
echo "=== Status dos containers ==="
docker compose ps

echo ""
echo "=== Verificação de saúde ==="
echo ""
echo "Para monitorar logs em tempo real:"
echo "  docker compose logs -f"
echo ""
echo "Para verificar logs de um serviço específico:"
echo "  docker compose logs -f keycloak"
echo "  docker compose logs -f kafka"
echo ""
echo "Para verificar status de saúde:"
echo "  docker compose ps"
echo ""
echo "Se algum serviço falhar, execute:"
echo "  docker compose logs <nome-do-servico>"
