# RabbitMQ com management + fila criada via docker-compose

## Detalhando o repostitório
Este repositório contém um `docker-compose.yml` que sobe um RabbitMQ com o plugin Management habilitado e cria automaticamente uma fila no exchange default.

Arquivos criados:
- `docker-compose.yml` — define o serviço `rabbitmq`.
- `definitions.json` — define os parametros para criação da fila `rabbit-connector-queue`.
- `rabbitmq.conf` — que vincula o definitions.json ao docker compose.

## Como usar 

### Subindo container com dados defaults:

```bash
docker compose up -d
```

### Subindo container com variáveis de ambiente customizadas:
- `RABBITMQ_DEFAULT_USER` — usuário admin (padrão: `rabbit-connector`)
- `RABBITMQ_DEFAULT_PASS` — senha admin (padrão: `segredo`)
- `RABBITMQ_QUEUE` — nome da fila a ser criada (padrão: `rabbit-connector-queue`)

```bash
RABBITMQ_DEFAULT_USER=rabbit-connector RABBITMQ_DEFAULT_PASS=segredo docker compose up -d
```

### Plugin Management
A interface web do Management fica disponível em:

http://localhost:15672  (user/senha conforme variáveis acima)

Notas e troubleshooting
- Se o `rabbit-init` falhar por timeout, verifique os logs do serviço RabbitMQ:

```bash
docker compose logs rabbitmq
```

- Para executar novamente a criação da fila, remova o container `rabbit-init` e execute o serviço manualmente ou altere `RABBITMQ_QUEUE` e suba de novo.

Compatibilidade
- Testado com `rabbitmq:3-management` e Docker Compose v2/v1 (comando `docker compose`).



## Configurando o `passowrd_hash` no arquivo `difinitions.json`
- Gerar o hash usando o container RabbitMQ já em execução
  - Executar o commando: `docker exec rabbitmq rabbitmqctl hash_password segredo`
  - Copiar a saída (exatamente o que foi gerado, toda a string).

- Gerar o hash usando um container temporário da mesma imagem (se o container principal não estiver rodando)
  - Executar o commando: `docker run --rm rabbitmq:3-management rabbitmqctl hash_password segredo`
  - Copiar a saída (exatamente o que foi gerado, toda a string).

- Substitua o valor atual de "password_hash" pelo hash gerado. Exemplo (hipotético):
  - Antes: `"password_hash": ""`\
  - Depois: `"password_hash": "PBKDF2$sha256$10000$...hash-gerado..."`