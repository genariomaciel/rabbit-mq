package com.leicam.rabbitmq.services;

import java.util.UUID;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PublisherService {

  @Value("${spring.rabbitmq.queue-name}")
  private String queueName;

  private final RabbitTemplate rabbitTemplate;

  public PublisherService(RabbitTemplate rabbitTemplate) {
    this.rabbitTemplate = rabbitTemplate;
  }

  public void publish(Object message) {
    CorrelationData correlationData = createCorrelationData();
    
    rabbitTemplate.convertAndSend(queueName, message, correlationData);
    System.out.println("Mensagem enviada com correlationId=" + correlationData.getId());
  }

  private CorrelationData createCorrelationData() {
    String correlationId = UUID.randomUUID().toString();
    return new CorrelationData(correlationId);
  }
  
}
