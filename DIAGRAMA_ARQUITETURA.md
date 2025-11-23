# Diagrama da Arquitetura RabbitMQ

## 1. EXCHANGE DEFAULT (Direct)

```
┌─────────────┐
│   Cliente   │
└──────┬──────┘
       │ POST /api/v1/publisher/default
       │ { "message": "..." }
       ▼
┌──────────────────────────────────────────┐
│ PublisherService.publishDefault()        │
└──────────────────┬───────────────────────┘
                   │
                   ▼
┌──────────────────────────────────────────────────────────┐
│ RabbitTemplate.convertAndSend(                           │
│   "default-exchange",                                    │
│   "default-queue",  ← routing key                        │
│   message                                                │
│ )                                                        │
└──────────────────┬───────────────────────────────────────┘
                   │
    ┌──────────────▼──────────────┐
    │   default-exchange (Direct) │
    └──────────────┬──────────────┘
                   │
                   │ routing key match: "default-queue"
                   │
                   ▼
    ┌───────────────────────────┐
    │   default-queue           │
    └───────────────┬───────────┘
                    │
                    ▼
    ┌───────────────────────────────────────────┐
    │ ConsumerService.listenDefaultQueue()      │
    │ [DEFAULT QUEUE] Mensagem recebida: ...    │
    └───────────────────────────────────────────┘
```

---

## 2. EXCHANGE FANOUT

```
┌─────────────┐
│   Cliente   │
└──────┬──────┘
       │ POST /api/v1/publisher/fanout
       │ { "message": "..." }
       ▼
┌──────────────────────────────────────────┐
│ PublisherService.publishFanout()         │
└──────────────────┬───────────────────────┘
                   │
                   ▼
┌──────────────────────────────────────────────────────────┐
│ RabbitTemplate.convertAndSend(                           │
│   "fanout-exchange",                                     │
│   "",  ← sem routing key (broadcast)                     │
│   message                                                │
│ )                                                        │
└──────────────────┬───────────────────────────────────────┘
                   │
    ┌──────────────▼──────────────┐
    │   fanout-exchange (Fanout)  │
    └──────────────┬──────────────┘
                   │
        ┌──────────┴────────────┬──────────────────┐
        │                       │                  │
        ▼                       ▼                  ▼
    ┌───────────────┐    ┌───────────────┐    ┌───────────────┐
    │ fanout-queue-1│    │ fanout-queue-2│    │ fanout-queue-2│
    └────────┬──────┘    └───────┬───────┘    └────────┬──────┘
             │                   │                     │
             ▼                   ▼                     ▼
    ┌────────────────────┐ ┌────────────────────┐ ┌────────────────────┐
    │ listenFanoutQueue1 │ │ listenFanoutQueue2 │ │ listenFanoutQueue3 │
    │ [FANOUT Q1] ...    │ │ [FANOUT Q2] ...    │ │ [FANOUT Q3] ...    │
    └────────────────────┘ └────────────────────┘ └────────────────────┘

Note: Broadcast - a mesma mensagem vai para AMBAS as filas!
```

---

## 3. EXCHANGE TOPIC (Orders)

```
┌─────────────┐
│   Cliente   │
└──────┬──────┘
       │ POST /api/v1/publisher/topic/orders
       │ { "message": "...", "routingKey": "created" }
       ▼
┌──────────────────────────────────────────┐
│ PublisherService.publishTopicOrders()    │
└──────────────────┬───────────────────────┘
                   │
                   ▼
┌──────────────────────────────────────────────────────────┐
│ RabbitTemplate.convertAndSend(                           │
│   "topic-exchange",                                      │
│   "orders.created",  ← routing key                       │
│   message                                                │
│ )                                                        │
└──────────────────┬───────────────────────────────────────┘
                   │
    ┌──────────────▼──────────────┐
    │   topic-exchange (Topic)    │
    │                             │
    │ Pattern Matching:           │
    │ - orders.* pattern exists   │
    └──────────────┬──────────────┘
                   │
                   │ Match: orders.* == orders.created ✓
                   │
                   ▼
    ┌───────────────────────────┐
    │   topic-orders-queue      │
    │   (pattern: orders.*)      │
    └───────────────┬───────────┘
                    │
                    ▼
    ┌─────────────────────────────────────────┐
    │ ConsumerService.listenTopicOrders()     │
    │ [TOPIC - ORDERS] Mensagem recebida: ... │
    └─────────────────────────────────────────┘
```

---

## 4. EXCHANGE TOPIC (Notifications)

```
┌─────────────┐
│   Cliente   │
└──────┬──────┘
       │ POST /api/v1/publisher/topic/notifications
       │ { "message": "...", "routingKey": "email" }
       ▼
┌──────────────────────────────────────────────┐
│ PublisherService.publishTopicNotifications() │
└──────────────────┬──────────────────────────┘
                   │
                   ▼
┌────────────────────────────────────────────────────────────┐
│ RabbitTemplate.convertAndSend(                             │
│   "topic-exchange",                                        │
│   "notifications.email",  ← routing key                    │
│   message                                                  │
│ )                                                          │
└──────────────────┬─────────────────────────────────────────┘
                   │
    ┌──────────────▼──────────────┐
    │   topic-exchange (Topic)    │
    │                             │
    │ Pattern Matching:           │
    │ - notifications.* exists    │
    └──────────────┬──────────────┘
                   │
                   │ Match: notifications.* == notifications.email ✓
                   │
                   ▼
    ┌──────────────────────────────┐
    │   topic-notifications-queue  │
    │   (pattern: notifications.*) │
    └───────────────┬──────────────┘
                    │
                    ▼
    ┌──────────────────────────────────────────────┐
    │ ConsumerService.listenTopicNotifications()   │
    │ [TOPIC - NOTIFICATIONS] Mensagem: ...        │
    └──────────────────────────────────────────────┘
```

---

## Comparação dos Exchanges

| Tipo | Propósito | Roteamento | Quando Usar |
|------|-----------|-----------|------------|
| **Direct** | Ponto-a-ponto | Routing key exata | Uma fila deve receber mensagens específicas |
| **Fanout** | Broadcast | Nenhum (vai para todas) | Múltiplas filas recebem a mesma mensagem |
| **Topic** | Baseado em Padrão | Wildcards (*.pattern) | Roteamento flexível com múltiplas variações |

---

## Fluxo de Dados Completo

```
┌────────────────────────────────────────────────────────────────┐
│                          CLIENTE HTTP                           │
│  POST /api/v1/publisher/{endpoint}                             │
└────────────────────┬───────────────────────────────────────────┘
                     │
        ┌────────────┼────────────┐
        │            │            │
        ▼            ▼            ▼
   /default      /fanout    /topic/*
        │            │            │
        ▼            ▼            ▼
    ┌────────────────────────────────────────┐
    │   PublisherController                  │
    │ ├─ publishDefault()                    │
    │ ├─ publishFanout()                     │
    │ ├─ publishTopicOrders()                │
    │ └─ publishTopicNotifications()         │
    └────────────────┬───────────────────────┘
                     │
                     ▼
    ┌────────────────────────────────────────┐
    │   PublisherService                     │
    │ ├─ publishDefault(msg)                 │
    │ ├─ publishFanout(msg)                  │
    │ ├─ publishTopicOrders(msg, key)        │
    │ ├─ publishTopicNotifications(msg, key) │
    │ └─ publishMessage(exch, key, msg)      │
    └────────────────┬───────────────────────┘
                     │
                     ▼
    ┌────────────────────────────────────────┐
    │   RabbitTemplate                       │
    │   convertAndSend(exchange, key, msg)   │
    └────────────────┬───────────────────────┘
                     │
        ┌────────────┼────────────┐
        │            │            │
        ▼            ▼            ▼
  ┌──────────┐ ┌───────────┐ ┌────────────┐
  │ Default  │ │  Fanout   │ │   Topic    │
  │ Exchange │ │ Exchange  │ │ Exchange   │
  └────┬─────┘ └─────┬─────┘ └──────┬─────┘
       │             │              │
       ▼             ▼              ▼
  ┌──────────┐ ┌─────────────┐ ┌─────────────┐
  │   Queue  │ │ Queue1 Q2   │ │ Orders Q    │
  └────┬─────┘ └──┬────┬──┘  │ │ Notif Q     │
       │          │    │     │  └────┬────┬──┘
       └──────────┘    └─────┘       │    │
              │                      │    │
    ┌─────────┴──────────────────────┴────┴─────────┐
    │                                               │
    ▼ ▼ ▼ ▼ ▼                                      ▼
 ConsumerService - @RabbitListener                (mais listeners)
 ├─ listenDefaultQueue()
 ├─ listenFanoutQueue1/2()
 ├─ listenTopicOrders()
 └─ listenTopicNotifications()
            │
            ▼
     [CONSOLE OUTPUT]
     =====================================
     [EXCHANGE_TYPE] Mensagem recebida:
     Seu texto aqui
     =====================================
```

---

## Configurações por Exchange

### DEFAULT EXCHANGE
- Arquivo: `RabbitConfiguration.java`
- Bean: `DirectExchange defaultExchange()`
- Queue: `defaultQueue()`
- Binding: `defaultBinding()`
- Listener: `ConsumerService.listenDefaultQueue()`

### FANOUT EXCHANGE
- Arquivo: `RabbitConfiguration.java`
- Bean: `FanoutExchange fanoutExchange()`
- Queues: `fanoutQueue1()`, `fanoutQueue2()`
- Bindings: `fanoutBinding1()`, `fanoutBinding2()`
- Listeners: `ConsumerService.listenFanoutQueue1/2()`

### TOPIC EXCHANGE
- Arquivo: `RabbitConfiguration.java`
- Bean: `TopicExchange topicExchange()`
- Queues: `topicOrdersQueue()`, `topicNotificationsQueue()`
- Bindings com patterns: `topicOrdersBinding()`, `topicNotificationsBinding()`
- Listeners: `ConsumerService.listenTopicOrders()`, `listenTopicNotifications()`
