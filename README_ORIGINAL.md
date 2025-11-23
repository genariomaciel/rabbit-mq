# RabbitMQ com Management

## Detalhando o repostitório
Este repositório contém um `docker-compose.yml` que sobe um RabbitMQ com o plugin Management habilitado e cria automaticamente uma fila no exchange default.

Arquivos criados:
- `docker-compose.yml` — definições do serviço `rabbitmq`.
- `definitions.json` — definições para criação da fila `rabbit-connector-queue`.
- `rabbitmq.conf` — que vincula o definitions.json ao docker compose.

## Como usar 

### Subindo container com dados defaults:

Comando
```bash
docker compose up -d
```

### Subindo container com variáveis de ambiente customizadas:

Variáveis utilizadas
```
  RABBITMQ_DEFAULT_USER=rabbit-connector
  RABBITMQ_DEFAULT_PASS=segredo
  RABBITMQ_QUEUE=rabbit-connector-queue
```

Commando\
Git Bash
```bash
RABBITMQ_DEFAULT_USER=rabbit-connector RABBITMQ_DEFAULT_PASS=segredo docker compose up -d
```

PowerShell
```powershell
$env:RABBITMQ_DEFAULT_USER="rabbit-connector"; $env:RABBITMQ_DEFAULT_PASS="segredo"; docker compose up -d
```

### Plugin Management
A interface web do Management fica disponível em:

http://localhost:15672  (user/senha conforme variáveis acima)

### Logs do serviço RabbitMQ:

```bash
docker compose logs rabbitmq
```


## Criando o `passowrd_hash` para o arquivo `definitions.json`
- Gerar o hash usando o container RabbitMQ já em execução
  Executar o commando
  ```bash
  docker exec rabbitmq rabbitmqctl hash_password segredo
  ```
  Copiar a saída (exatamente o que foi gerado, toda a string).

- Gerar o hash usando um container temporário da mesma imagem (se o container principal não estiver rodando)
  Executar o commando
  ```bash
    docker run --rm rabbitmq:3-management rabbitmqctl hash_password segredo
  ```

  Copiar a saída (exatamente o que foi gerado, toda a string).

- Substitua o valor atual de "password_hash" pelo hash gerado. Exemplo (hipotético):

  De
  ```json
  "password_hash": ""
  ```
  Para
  ```json
  "password_hash": "PBKDF2$sha256$10000$...hash-gerado..."
  ```

