package com.leicam.rabbitmq.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

  @GetMapping("/health")
  public ResponseEntity<String> health() {
    return ResponseEntity.ok("Aplicação RabbitMQ está rodando!");
  }

  @GetMapping("/api/v1/info")
  public ResponseEntity<String> info() {
    return ResponseEntity.ok("""
        Endpoints disponíveis:
        
        1. DEFAULT EXCHANGE (Direct):
           POST /api/v1/publisher/default
           Body: { "message": "seu texto aqui" }
        
        2. FANOUT EXCHANGE:
           POST /api/v1/publisher/fanout
           Body: { "message": "seu texto aqui" }
           Nota: A mensagem será entregue a TODAS as filas ligadas ao exchange
        
        3. TOPIC EXCHANGE (Orders):
           POST /api/v1/publisher/topic/orders
           Body: { "message": "seu texto aqui", "routingKey": "created" }
           RoutingKey aceita: created, updated, deleted
        
        4. TOPIC EXCHANGE (Notifications):
           POST /api/v1/publisher/topic/notifications
           Body: { "message": "seu texto aqui", "routingKey": "email" }
           RoutingKey aceita: email, sms, push
        """);
  }
}
