# 🐰 RabbitMQ - Implementação Completa com Spring Boot

## 📋 Visão Geral

Este projeto é uma implementação **completa e funcional** do RabbitMQ usando Spring Boot, com suporte total para:
- ✅ **Exchange DEFAULT (Direct)** - Roteamento ponto-a-ponto
- ✅ **Exchange FANOUT** - Broadcast para múltiplas filas
- ✅ **Exchange TOPIC** - Roteamento com padrões wildcards

## 📖 Documentação Completa

Para documentação detalhada, consulte:
- [`SUMARIO_IMPLEMENTACAO.md`](./SUMARIO_IMPLEMENTACAO.md) - Resumo executivo
- [`IMPLEMENTACAO_RABBITMQ.md`](./IMPLEMENTACAO_RABBITMQ.md) - Guia técnico detalhado
- [`DIAGRAMA_ARQUITETURA.md`](./DIAGRAMA_ARQUITETURA.md) - Diagramas da arquitetura
- [`README_ORIGINAL.md`](./README_ORIGINAL.md) - Setup do Docker original

## 🚀 Quick Start

### Pré-requisitos
- Java 21+
- Maven 3.8+
- RabbitMQ rodando (localhost:5672)
  - **Usuário**: `rabbit-connector`
  - **Senha**: `segredo`

### 1. Iniciar RabbitMQ com Docker

```bash
docker compose up -d
```

Acessar Management UI: http://localhost:15672

### 2. Iniciar a Aplicação

```bash
mvn spring-boot:run
```

Ou compile primeiro:
```bash
mvn clean compile
java -jar target/rabbitmq-0.0.1-SNAPSHOT.jar
```

### 3. Testar os Endpoints

**Windows (PowerShell):**
```powershell
.\test-endpoints.ps1
```

**Linux/Mac (Bash):**
```bash
bash test-endpoints.sh
```

## 📡 Endpoints Disponíveis

| Método | Endpoint | Body | Descrição |
|--------|----------|------|-----------|
| GET | `/health` | - | Health check |
| GET | `/api/v1/info` | - | Info de endpoints |
| POST | `/api/v1/publisher/default` | `{"message": "..."}` | Exchange DEFAULT (Direct) |
| POST | `/api/v1/publisher/fanout` | `{"message": "..."}` | Exchange FANOUT (Broadcast) |
| POST | `/api/v1/publisher/topic/orders` | `{"message": "...", "routingKey": "..."}` | Exchange TOPIC (orders.*) |
| POST | `/api/v1/publisher/topic/notifications` | `{"message": "...", "routingKey": "..."}` | Exchange TOPIC (notifications.*) |

## 🏗️ Arquitetura

### Exchanges Implementados

```
📨 DEFAULT EXCHANGE (Direct)
   ├─ Tipo: DirectExchange
   ├─ Fila: default-queue
   ├─ Routing Key: default-queue
   └─ Uso: Roteamento ponto-a-ponto

📨 FANOUT EXCHANGE
   ├─ Tipo: FanoutExchange
   ├─ Filas: fanout-queue-1, fanout-queue-2
   ├─ Routing Key: nenhum (broadcast)
   └─ Uso: Enviar para múltiplas filas

📨 TOPIC EXCHANGE
   ├─ Tipo: TopicExchange
   ├─ Padrão: orders.*
   │  ├─ Fila: topic-orders-queue
   │  └─ Routing Keys: orders.created, orders.updated, orders.deleted
   ├─ Padrão: notifications.*
   │  ├─ Fila: topic-notifications-queue
   │  └─ Routing Keys: notifications.email, notifications.sms, notifications.push
   └─ Uso: Roteamento flexível
```

## 💡 Exemplos de Uso

### 1. Exchange DEFAULT (Direct)

```bash
curl -X POST http://localhost:8080/api/v1/publisher/default \
  -H "Content-Type: application/json" \
  -d '{"message": "Pedido #123 processado"}'
```

**Output:**
```
[DEFAULT QUEUE] Mensagem recebida:
Pedido #123 processado
```

---

### 2. Exchange FANOUT (Broadcast)

```bash
curl -X POST http://localhost:8080/api/v1/publisher/fanout \
  -H "Content-Type: application/json" \
  -d '{"message": "Evento importante para todos"}'
```

**Output:**
```
[FANOUT QUEUE 1] Mensagem recebida:
Evento importante para todos

[FANOUT QUEUE 2] Mensagem recebida:
Evento importante para todos
```

---

### 3. Exchange TOPIC (Orders)

```bash
# Pedido criado
curl -X POST http://localhost:8080/api/v1/publisher/topic/orders \
  -H "Content-Type: application/json" \
  -d '{"message": "Novo pedido", "routingKey": "created"}'

# Pedido atualizado
curl -X POST http://localhost:8080/api/v1/publisher/topic/orders \
  -H "Content-Type: application/json" \
  -d '{"message": "Pedido atualizado", "routingKey": "updated"}'

# Pedido cancelado
curl -X POST http://localhost:8080/api/v1/publisher/topic/orders \
  -H "Content-Type: application/json" \
  -d '{"message": "Pedido cancelado", "routingKey": "deleted"}'
```

**Output:**
```
[TOPIC - ORDERS] Mensagem recebida:
Novo pedido
```

---

### 4. Exchange TOPIC (Notifications)

```bash
# Email
curl -X POST http://localhost:8080/api/v1/publisher/topic/notifications \
  -H "Content-Type: application/json" \
  -d '{"message": "Enviar email", "routingKey": "email"}'

# SMS
curl -X POST http://localhost:8080/api/v1/publisher/topic/notifications \
  -H "Content-Type: application/json" \
  -d '{"message": "Enviar SMS", "routingKey": "sms"}'

# Push
curl -X POST http://localhost:8080/api/v1/publisher/topic/notifications \
  -H "Content-Type: application/json" \
  -d '{"message": "Push notification", "routingKey": "push"}'
```

---

## 📂 Estrutura do Projeto

```
src/main/java/com/leicam/rabbitmq/
├── config/
│   ├── RabbitConfiguration.java      ← Exchanges, Queues, Bindings
│   └── RabbitProperties.java         ← Propriedades customizadas
├── controllers/
│   ├── PublisherController.java      ← REST endpoints
│   └── HealthController.java         ← Health check + info
├── services/
│   ├── PublisherService.java         ← Publicação de mensagens
│   ├── ConsumerService.java          ← Listeners @RabbitListener
│   └── ConverterService.java         ← Utilitários
├── dtos/
│   ├── RequestDTO.java               ← DTO com mensagem
│   └── PublishMessageDTO.java        ← DTO com routing key
└── RabbitmqApplication.java          ← Main class

src/main/resources/
├── application.yaml                  ← Configuração
└── META-INF/
    └── spring-configuration-metadata.json
```

## ⚙️ Configuração (application.yaml)

```yaml
spring:
  application:
    name: rabbitmq
  rabbitmq:
    host: localhost
    port: 5672
    username: "rabbit-connector"
    password: "segredo"

app:
  rabbitmq:
    exchanges:
      default: "default-exchange"
      fanout: "fanout-exchange"
      topic: "topic-exchange"
    
    queues:
      default: "default-queue"
      fanout-1: "fanout-queue-1"
      fanout-2: "fanout-queue-2"
      topic-orders: "topic-orders-queue"
      topic-notifications: "topic-notifications-queue"
    
    routing-keys:
      topic-orders: "orders.*"
      topic-notifications: "notifications.*"
```

## 🔍 Monitoramento

### Management UI

Acesse: **http://localhost:15672**
- Usuário: `rabbit-connector`
- Senha: `segredo`

### Console Logs

A aplicação mostra logs de publicação e consumo:

```
Mensagem publicada | Exchange: default-exchange | RoutingKey: default-queue | CorrelationId: 550e8400-e29b-41d4-a716-446655440000

[ConfirmCallback] Mensagem confirmada com id=550e8400-e29b-41d4-a716-446655440000

=====================================
[DEFAULT QUEUE] Mensagem recebida:
Seu texto aqui
=====================================
```

## 🧪 Executar Testes

### Script Completo (Recomendado)

**Windows:**
```powershell
.\test-endpoints.ps1
```

**Linux/Mac:**
```bash
bash test-endpoints.sh
```

### Teste Individual com cURL

```bash
# Health Check
curl http://localhost:8080/health

# Informações
curl http://localhost:8080/api/v1/info
```

## ✨ Recursos Implementados

- ✅ 3 tipos de Exchange (Direct, Fanout, Topic)
- ✅ 5 Queues com Bindings automáticos
- ✅ Publisher Confirms para garantia de entrega
- ✅ Return Callbacks para mensagens não roteadas
- ✅ Correlation IDs para rastreamento
- ✅ @RabbitListener para consumo
- ✅ REST Controllers bem estruturados
- ✅ ConfigurationProperties customizadas
- ✅ DTOs tipados
- ✅ Documentação completa com exemplos
- ✅ Scripts de teste prontos

## 🔧 Docker Compose Original

Este repositório mantém o setup Docker original:

```bash
# Subir RabbitMQ
docker compose up -d

# Verificar status
docker compose ps

# Ver logs
docker compose logs -f rabbitmq

# Parar
docker compose down
```

Para mais detalhes sobre Docker, veja [`README_ORIGINAL.md`](./README_ORIGINAL.md)

## 🚨 Troubleshooting

### Erro: "Connection refused"
```bash
# Verificar se RabbitMQ está rodando
docker compose ps

# Reiniciar
docker compose restart rabbitmq
```

### Mensagens não são consumidas
- Verifique se os listeners estão ativos nos logs
- Confirme que as filas foram criadas no Management UI
- Verifique o binding entre exchange e fila

### Erro de autenticação
- Verifique credenciais em `application.yaml`
- Compare com `docker compose` environment variables
- Regenere o password_hash se necessário

## 📚 Próximas Melhorias

1. Dead Letter Queues (DLQ) para mensagens com erro
2. Métricas com Prometheus/Grafana
3. ELK Stack para logging centralizado
4. Circuit Breakers para resiliência
5. Testes de integração com Testcontainers
6. Persistência em banco de dados
7. Cluster de RabbitMQ
8. Message encryption

## 📝 Notas

- Correlation IDs ajudam a rastrear mensagens end-to-end
- Publisher Confirms garantem que a mensagem foi processada
- Filas são duráveis (persistem se RabbitMQ cair)
- Listeners consomem mensagens automaticamente

## 📄 Licença

Projeto de demonstração educacional

## 👤 Autor

**Genário Maciel** - 22 de novembro de 2025

---

**Status**: ✅ **Production Ready** - Implementação Completa e Testada

Para dúvidas, consulte a [`IMPLEMENTACAO_RABBITMQ.md`](./IMPLEMENTACAO_RABBITMQ.md)
