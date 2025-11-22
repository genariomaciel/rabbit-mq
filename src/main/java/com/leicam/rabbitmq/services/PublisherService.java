package com.leicam.rabbitmq.services;

import java.util.UUID;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.leicam.rabbitmq.config.RabbitProperties;

@Service
public class PublisherService {

  private final RabbitTemplate rabbitTemplate;
  private final RabbitProperties rabbitProperties;

  public PublisherService(RabbitTemplate rabbitTemplate, RabbitProperties rabbitProperties) {
    this.rabbitTemplate = rabbitTemplate;
    this.rabbitProperties = rabbitProperties;
  }

  /**
   * Publica mensagem no exchange DEFAULT
   */
  public void publishDefault(String message) {
    String exchangeName = rabbitProperties.getExchanges().getDefaultExchange();
    String routingKey = rabbitProperties.getQueues().getDefaultQueue();
    publishMessage(exchangeName, routingKey, message);
  }

  /**
   * Publica mensagem no exchange FANOUT
   */
  public void publishFanout(String message) {
    String exchangeName = rabbitProperties.getExchanges().getFanout();
    publishMessage(exchangeName, "", message);
  }

  /**
   * Publica mensagem no exchange TOPIC com routing key para pedidos
   */
  public void publishTopicOrders(String message, String orderType) {
    String exchangeName = rabbitProperties.getExchanges().getTopic();
    String routingKey = "orders." + orderType;
    publishMessage(exchangeName, routingKey, message);
  }

  /**
   * Publica mensagem no exchange TOPIC com routing key para notificações
   */
  public void publishTopicNotifications(String message, String notificationType) {
    String exchangeName = rabbitProperties.getExchanges().getTopic();
    String routingKey = "notifications." + notificationType;
    publishMessage(exchangeName, routingKey, message);
  }

  /**
   * Publica mensagem genérica em um exchange com routing key específica
   */
  public void publishMessage(String exchangeName, String routingKey, Object message) {
    CorrelationData correlationData = createCorrelationData();
    
    rabbitTemplate.convertAndSend(exchangeName, routingKey, message, correlationData);
    System.out.println("Mensagem publicada | Exchange: " + exchangeName + 
        " | RoutingKey: " + routingKey + 
        " | CorrelationId: " + correlationData.getId());
  }

  /**
   * Publica no exchange default (compatibilidade com versão anterior)
   */
  public void publish(Object message) {
    publishDefault((String) message);
  }

  private CorrelationData createCorrelationData() {
    String correlationId = UUID.randomUUID().toString();
    return new CorrelationData(correlationId);
  }
}

