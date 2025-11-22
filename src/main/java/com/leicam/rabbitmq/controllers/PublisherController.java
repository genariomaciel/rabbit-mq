package com.leicam.rabbitmq.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.leicam.rabbitmq.dtos.RequestDTO;
import com.leicam.rabbitmq.dtos.PublishMessageDTO;
import com.leicam.rabbitmq.services.PublisherService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/api/v1/publisher")
public class PublisherController {

  private final PublisherService publisherService;
  
  public PublisherController(PublisherService publisherService) {
    this.publisherService = publisherService;
  }

  @PostMapping()
  public ResponseEntity<String> postMethodName(@RequestBody RequestDTO message) {
      publisherService.publish(message.message());
      return ResponseEntity.ok("sent");
  }

  @PostMapping("/default")
  public ResponseEntity<String> publishDefault(@RequestBody RequestDTO message) {
    publisherService.publishDefault(message.message());
    return ResponseEntity.ok("Mensagem publicada no exchange DEFAULT");
  }

  @PostMapping("/fanout")
  public ResponseEntity<String> publishFanout(@RequestBody RequestDTO message) {
    publisherService.publishFanout(message.message());
    return ResponseEntity.ok("Mensagem publicada no exchange FANOUT para todas as filas");
  }

  @PostMapping("/topic/orders")
  public ResponseEntity<String> publishTopicOrders(@RequestBody PublishMessageDTO messageDTO) {
    publisherService.publishTopicOrders(messageDTO.message(), messageDTO.routingKey());
    return ResponseEntity.ok("Mensagem publicada no exchange TOPIC (orders)");
  }

  @PostMapping("/topic/notifications")
  public ResponseEntity<String> publishTopicNotifications(@RequestBody PublishMessageDTO messageDTO) {
    publisherService.publishTopicNotifications(messageDTO.message(), messageDTO.routingKey());
    return ResponseEntity.ok("Mensagem publicada no exchange TOPIC (notifications)");
  }

}
