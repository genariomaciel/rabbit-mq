# RabbitMQ - Implementação Completa

## 📋 Resumo da Implementação

Este projeto implementa um sistema completo de publicação e consumo de mensagens usando RabbitMQ com suporte para:
- ✅ **Exchange DEFAULT (Direct)** - Roteamento baseado em routing key exata
- ✅ **Exchange FANOUT** - Broadcast para todas as filas conectadas
- ✅ **Exchange TOPIC** - Roteamento com wildcards

## 🏗️ Arquitetura Implementada

### Exchanges Criados:
1. **default-exchange** (Direct)
   - Fila: `default-queue`
   - Routing Key: `default-queue`

2. **fanout-exchange** (Fanout)
   - Filas: `fanout-queue-1`, `fanout-queue-2`, `fanout-queue-3`
   - Sem routing key (recebem tudo)

3. **topic-exchange** (Topic)
   - Fila `topic-orders-queue` → pattern `orders.*`
   - Fila `topic-notifications-queue` → pattern `notifications.*`

## 🚀 Como Usar

### Pré-requisitos
- Java 21+
- Maven
- RabbitMQ rodando localmente (localhost:5672)
- Credenciais: usuario=rabbit-connector, senha=segredo

### Inicie a Aplicação
```bash
mvn spring-boot:run
```

### Teste de Saúde
```bash
curl http://localhost:8080/health
```

## 📡 Endpoints Disponíveis

### 1. Exchange DEFAULT (Direct)
```bash
curl -X POST http://localhost:8080/api/v1/publisher/default \
  -H "Content-Type: application/json" \
  -d '{"message": "Teste Direct Exchange"}'
```
**Uso**: Roteamento ponto-a-ponto baseado em routing key exata.

---

### 2. Exchange FANOUT
```bash
curl -X POST http://localhost:8080/api/v1/publisher/fanout \
  -H "Content-Type: application/json" \
  -d '{"message": "Mensagem para todas as filas fanout"}'
```
**Uso**: Broadcast - a mensagem vai para `fanout-queue-1` E `fanout-queue-2`.

---

### 3. Exchange TOPIC - Pedidos
```bash
curl -X POST http://localhost:8080/api/v1/publisher/topic/orders \
  -H "Content-Type: application/json" \
  -d '{"message": "Pedido criado", "routingKey": "created"}'
```
**Routing Keys aceitas**: `created`, `updated`, `deleted`

Exemplos:
- `orders.created` → vai para `topic-orders-queue`
- `orders.updated` → vai para `topic-orders-queue`
- `orders.deleted` → vai para `topic-orders-queue`

---

### 4. Exchange TOPIC - Notificações
```bash
curl -X POST http://localhost:8080/api/v1/publisher/topic/notifications \
  -H "Content-Type: application/json" \
  -d '{"message": "Enviar email", "routingKey": "email"}'
```
**Routing Keys aceitas**: `email`, `sms`, `push`

Exemplos:
- `notifications.email` → vai para `topic-notifications-queue`
- `notifications.sms` → vai para `topic-notifications-queue`
- `notifications.push` → vai para `topic-notifications-queue`

---

## 📦 Estrutura de Classes

### Configuração
- `RabbitProperties.java` - Classe de configuração com properties do YAML
- `RabbitConfiguration.java` - Definição de exchanges, filas e bindings

### Serviços
- `PublisherService.java` - Métodos para publicar em diferentes exchanges
- `ConsumerService.java` - Listeners que consomem as mensagens

### Controllers
- `PublisherController.java` - Endpoints para publicação
- `HealthController.java` - Endpoints de saúde e informações

### DTOs
- `RequestDTO.java` - DTO simples com mensagem
- `PublishMessageDTO.java` - DTO com mensagem e routing key

## 📊 Fluxo de Mensagens

### Exemplo 1: Direct Exchange
```
Cliente → POST /api/v1/publisher/default
  ↓
PublisherService.publishDefault()
  ↓
RabbitTemplate.convertAndSend("default-exchange", "default-queue", message)
  ↓
default-queue recebe
  ↓
ConsumerService.listenDefaultQueue() processa
```

### Exemplo 2: Fanout Exchange
```
Cliente → POST /api/v1/publisher/fanout
  ↓
PublisherService.publishFanout()
  ↓
RabbitTemplate.convertAndSend("fanout-exchange", "", message)
  ↓
Broadcast para TODAS as filas conectadas
  ├→ fanout-queue-1 recebe → ConsumerService.listenFanoutQueue1()
  └→ fanout-queue-2 recebe → ConsumerService.listenFanoutQueue2()
```

### Exemplo 3: Topic Exchange
```
Cliente → POST /api/v1/publisher/topic/orders
  ↓
PublisherService.publishTopicOrders(msg, "created")
  ↓
RabbitTemplate.convertAndSend("topic-exchange", "orders.created", message)
  ↓
Matching com pattern "orders.*"
  ├→ topic-orders-queue recebe → ConsumerService.listenTopicOrders()
```

## 🔍 Monitoramento

Os listeners mostram mensagens no console:
```
=====================================
[DEFAULT QUEUE] Mensagem recebida:
Seu texto aqui
=====================================
```

## 🔧 Configurações (application.yaml)

```yaml
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

## 📝 Padrões de Design Utilizados

1. **Dependency Injection** - Todas as dependências injetadas via construtor
2. **Properties-based Configuration** - Uso de `@ConfigurationProperties`
3. **Publisher-Subscriber** - Padrão implementado com RabbitMQ
4. **Listener Pattern** - `@RabbitListener` para consumir mensagens
5. **DTO Pattern** - Transfer Objects para comunicação

## 🎯 Casos de Uso

### Direct Exchange - Use quando:
- Precisa rotear para uma fila específica
- Comunicação ponto-a-ponto
- Ex: Processamento de pedidos

### Fanout Exchange - Use quando:
- Quer enviar a mesma mensagem para múltiplos consumidores
- Notificações de eventos para múltiplos sistemas
- Ex: Evento de usuário criado → logs, email, analytics

### Topic Exchange - Use quando:
- Precisa de roteamento flexível com padrões
- Múltiplas filas interessadas em diferentes tipos de eventos
- Ex: Sistema de notificações (email, SMS, push)

---

**Desenvolvido em**: 22 de novembro de 2025
