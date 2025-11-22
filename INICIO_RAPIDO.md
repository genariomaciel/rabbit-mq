# 🚀 INÍCIO RÁPIDO - RabbitMQ com Spring Boot

## ⚡ 3 Passos para Começar

### Passo 1: Iniciar o Docker
```bash
docker compose up -d
```

### Passo 2: Compilar e Executar
```bash
mvn spring-boot:run
```

### Passo 3: Testar (Escolha uma opção)

**Windows - PowerShell:**
```powershell
.\test-endpoints.ps1
```

**Linux/Mac - Bash:**
```bash
bash test-endpoints.sh
```

**Manual - cURL:**
```bash
curl -X POST http://localhost:8080/api/v1/publisher/default \
  -H "Content-Type: application/json" \
  -d '{"message": "Olá RabbitMQ!"}'
```

---

## 📋 Checklist Inicial

- [ ] Docker compose rodando (`docker compose ps`)
- [ ] Aplicação iniciada (veja na console: "Started RabbitmqApplication")
- [ ] Teste de saúde bem-sucedido (`curl http://localhost:8080/health`)
- [ ] Mensagens sendo consumidas (veja output no console)

---

## 🎯 Primeiros Testes

### 1. Health Check
```bash
curl http://localhost:8080/health
```
Resposta esperada: `Aplicação RabbitMQ está rodando!`

---

### 2. DEFAULT Exchange
```bash
curl -X POST http://localhost:8080/api/v1/publisher/default \
  -H "Content-Type: application/json" \
  -d '{"message": "Teste Direct"}'
```
Resposta esperada: `Mensagem publicada no exchange DEFAULT`

Console mostrará:
```
[DEFAULT QUEUE] Mensagem recebida:
Teste Direct
```

---

### 3. FANOUT Exchange
```bash
curl -X POST http://localhost:8080/api/v1/publisher/fanout \
  -H "Content-Type: application/json" \
  -d '{"message": "Broadcast para todos"}'
```
Console mostrará mensagem em AMBAS as filas:
```
[FANOUT QUEUE 1] Mensagem recebida:
Broadcast para todos

[FANOUT QUEUE 2] Mensagem recebida:
Broadcast para todos
```

---

### 4. TOPIC Exchange (Orders)
```bash
curl -X POST http://localhost:8080/api/v1/publisher/topic/orders \
  -H "Content-Type: application/json" \
  -d '{"message": "Novo pedido", "routingKey": "created"}'
```
Console mostrará:
```
[TOPIC - ORDERS] Mensagem recebida:
Novo pedido
```

---

### 5. TOPIC Exchange (Notifications)
```bash
curl -X POST http://localhost:8080/api/v1/publisher/topic/notifications \
  -H "Content-Type: application/json" \
  -d '{"message": "Notificação", "routingKey": "email"}'
```
Console mostrará:
```
[TOPIC - NOTIFICATIONS] Mensagem recebida:
Notificação
```

---

## 🔍 Monitorar em Tempo Real

### Management UI do RabbitMQ
Abra no navegador: **http://localhost:15672**
- Usuário: `rabbit-connector`
- Senha: `segredo`

**O que ver lá:**
- Exchanges criados (default, fanout, topic)
- Filas criadas (5 filas)
- Bindings configurados
- Gráficos de publicação/consumo em tempo real

---

## 📚 Próximos Passos para Aprender

1. **Leia o README.md** - Visão geral do projeto
2. **Estude IMPLEMENTACAO_RABBITMQ.md** - Detalhes técnicos
3. **Examine DIAGRAMA_ARQUITETURA.md** - Topologia visual
4. **Customize application.yaml** - Mude nomes de exchanges/filas
5. **Explore o código** - Veja como funciona por dentro

---

## 🛠️ Troubleshooting Rápido

### Erro: "Connection refused"
```bash
# Verificar se RabbitMQ está rodando
docker compose ps

# Se não estiver, reiniciar
docker compose restart rabbitmq
```

### Erro: "No connection available"
- Espere 5 segundos após `docker compose up`
- RabbitMQ demora para inicializar

### Mensagens não aparecem no console
- Verifique se os listeners foram iniciados (veja os logs)
- Certifique-se que as filas foram criadas (check Management UI)

---

## 📊 Estrutura do Projeto

```
c:\dev\source\rabbit-mq\
├── src/main/java/com/leicam/rabbitmq/
│   ├── config/
│   │   ├── RabbitConfiguration.java    ← Exchanges, Filas, Bindings
│   │   └── RabbitProperties.java       ← Configurações customizadas
│   ├── controllers/
│   │   ├── PublisherController.java    ← Endpoints REST
│   │   └── HealthController.java       ← Health check
│   ├── services/
│   │   ├── PublisherService.java       ← Publicação
│   │   ├── ConsumerService.java        ← Consumo (@RabbitListener)
│   │   └── ConverterService.java       ← Utilitários
│   └── dtos/
│       ├── RequestDTO.java
│       └── PublishMessageDTO.java
├── src/main/resources/
│   ├── application.yaml                ← Configuração
│   └── META-INF/
│       └── spring-configuration-metadata.json
├── README.md                           ← Documentação principal
├── test-endpoints.ps1                  ← Script Windows
├── test-endpoints.sh                   ← Script Linux/Mac
└── pom.xml                             ← Dependências Maven
```

---

## 💻 Comandos Úteis

### Compilar
```bash
mvn clean compile
```

### Executar
```bash
mvn spring-boot:run
```

### Parar RabbitMQ
```bash
docker compose down
```

### Ver logs do RabbitMQ
```bash
docker compose logs -f rabbitmq
```

### Resetar tudo (começar do zero)
```bash
docker compose down -v  # Remove volumes
docker compose up -d    # Sobe novamente
```

---

## 🎓 Exemplo Completo (Windows PowerShell)

```powershell
# 1. Inicie o Docker
docker compose up -d

# 2. Aguarde alguns segundos
Start-Sleep -Seconds 5

# 3. Inicie a aplicação (em outro terminal)
mvn spring-boot:run

# 4. Em outro terminal, execute testes
.\test-endpoints.ps1

# 5. Abra Management UI
Start-Process "http://localhost:15672"
```

---

## 📞 Links Rápidos

- **Management UI**: http://localhost:15672
- **Health Check**: http://localhost:8080/health
- **Info de Endpoints**: http://localhost:8080/api/v1/info

---

## 📖 Documentação Disponível

- `README.md` - Início e visão geral
- `IMPLEMENTACAO_RABBITMQ.md` - Guia técnico
- `DIAGRAMA_ARQUITETURA.md` - Diagramas
- `CHECKLIST_IMPLEMENTACAO.md` - Requisitos
- `RESUMO_FINAL.md` - Status do projeto

---

## ✅ Pronto?

Siga os **3 passos no topo** e você estará consumindo e publicando mensagens RabbitMQ em menos de 5 minutos! 🚀

**Bom desenvolvimento!** 😊
