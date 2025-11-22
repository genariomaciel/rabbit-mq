#!/bin/bash

# Script de Testes - RabbitMQ
# Todos os endpoints disponíveis para teste

BASE_URL="http://localhost:8080"

echo "================================"
echo "Testando Health Check"
echo "================================"
curl -s "$BASE_URL/health" | jq .
echo -e "\n"

echo "================================"
echo "Informações de Endpoints"
echo "================================"
curl -s "$BASE_URL/api/v1/info"
echo -e "\n"

# ====== DEFAULT EXCHANGE ======
echo -e "\n\n================================"
echo "1. Testando DEFAULT EXCHANGE"
echo "================================"
curl -X POST "$BASE_URL/api/v1/publisher/default" \
  -H "Content-Type: application/json" \
  -d '{"message": "Teste do Direct Exchange - Mensagem 1"}' \
  -s | jq .
sleep 1

echo -e "\nEnviando mais uma mensagem DEFAULT..."
curl -X POST "$BASE_URL/api/v1/publisher/default" \
  -H "Content-Type: application/json" \
  -d '{"message": "Teste do Direct Exchange - Mensagem 2"}' \
  -s | jq .
sleep 1

# ====== FANOUT EXCHANGE ======
echo -e "\n\n================================"
echo "2. Testando FANOUT EXCHANGE"
echo "================================"
echo "Enviando mensagem para TODAS as filas fanout..."
curl -X POST "$BASE_URL/api/v1/publisher/fanout" \
  -H "Content-Type: application/json" \
  -d '{"message": "Broadcast Fanout - vai para fanout-queue-1 E fanout-queue-2"}' \
  -s | jq .
sleep 1

echo -e "\nEnviando mais uma mensagem Fanout..."
curl -X POST "$BASE_URL/api/v1/publisher/fanout" \
  -H "Content-Type: application/json" \
  -d '{"message": "Segundo broadcast para todas as filas"}' \
  -s | jq .
sleep 1

# ====== TOPIC EXCHANGE - ORDERS ======
echo -e "\n\n================================"
echo "3. Testando TOPIC EXCHANGE - ORDERS"
echo "================================"

echo -e "\nEnviando orders.created..."
curl -X POST "$BASE_URL/api/v1/publisher/topic/orders" \
  -H "Content-Type: application/json" \
  -d '{"message": "Novo pedido criado", "routingKey": "created"}' \
  -s | jq .
sleep 1

echo -e "\nEnviando orders.updated..."
curl -X POST "$BASE_URL/api/v1/publisher/topic/orders" \
  -H "Content-Type: application/json" \
  -d '{"message": "Pedido foi atualizado", "routingKey": "updated"}' \
  -s | jq .
sleep 1

echo -e "\nEnviando orders.deleted..."
curl -X POST "$BASE_URL/api/v1/publisher/topic/orders" \
  -H "Content-Type: application/json" \
  -d '{"message": "Pedido foi cancelado", "routingKey": "deleted"}' \
  -s | jq .
sleep 1

# ====== TOPIC EXCHANGE - NOTIFICATIONS ======
echo -e "\n\n================================"
echo "4. Testando TOPIC EXCHANGE - NOTIFICATIONS"
echo "================================"

echo -e "\nEnviando notifications.email..."
curl -X POST "$BASE_URL/api/v1/publisher/topic/notifications" \
  -H "Content-Type: application/json" \
  -d '{"message": "Enviar notificação por email", "routingKey": "email"}' \
  -s | jq .
sleep 1

echo -e "\nEnviando notifications.sms..."
curl -X POST "$BASE_URL/api/v1/publisher/topic/notifications" \
  -H "Content-Type: application/json" \
  -d '{"message": "Enviar notificação por SMS", "routingKey": "sms"}' \
  -s | jq .
sleep 1

echo -e "\nEnviando notifications.push..."
curl -X POST "$BASE_URL/api/v1/publisher/topic/notifications" \
  -H "Content-Type: application/json" \
  -d '{"message": "Enviar notificação Push", "routingKey": "push"}' \
  -s | jq .
sleep 1

echo -e "\n\n================================"
echo "✅ Testes Completados!"
echo "================================"
echo "Verifique os logs da aplicação para ver os consumers processando as mensagens"
