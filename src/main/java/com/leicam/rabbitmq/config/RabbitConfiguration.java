package com.leicam.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.leicam.rabbitmq.services.ConverterService;

@Configuration
public class RabbitConfiguration {

  @Value("${spring.rabbitmq.host}")
  private String host;
  @Value("${spring.rabbitmq.port}")
  private int port;
  @Value("${spring.rabbitmq.username}")
  private String username;
  @Value("${spring.rabbitmq.password}")
  private String password;

  private final RabbitProperties rabbitProperties;

  public RabbitConfiguration(RabbitProperties rabbitProperties) {
    this.rabbitProperties = rabbitProperties;
  }

  @Bean
  public CachingConnectionFactory connectionFactory() {
    CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(this.host, this.port);
    cachingConnectionFactory.setUsername(this.username);
    cachingConnectionFactory.setPassword(this.password);
    cachingConnectionFactory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.CORRELATED);
    cachingConnectionFactory.setPublisherReturns(true);
    return cachingConnectionFactory;
  }

  @Bean
  public RabbitTemplate rabbitTemplate(CachingConnectionFactory connectionFactory, ConverterService converterService) {
    RabbitTemplate template = new RabbitTemplate(connectionFactory);
    template.setMandatory(true);

    template.setConfirmCallback((CorrelationData correlationData, boolean ack, String cause) -> {
      String id = (correlationData != null) ? correlationData.getId() : "<no-correlation-id>";
      if (ack) {
        System.out.println("[ConfirmCallback] Mensagem confirmada com id=" + id);
      } else {
        System.out.println("[ConfirmCallback] Mensagem NÃO confirmada id=" + id + " cause=" + cause);
      }
    });

    template.setReturnsCallback((ReturnedMessage returned) -> {
      System.out.println(converterService.toJson(returned));
    });

    return template;
  }

  

  




}
