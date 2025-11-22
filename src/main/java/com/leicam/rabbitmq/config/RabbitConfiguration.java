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

  // ============ EXCHANGES ============

  @Bean
  public DirectExchange defaultExchange() {
    return new DirectExchange(rabbitProperties.getExchanges().getDefaultExchange(), true, false);
  }

  @Bean
  public FanoutExchange fanoutExchange() {
    return new FanoutExchange(rabbitProperties.getExchanges().getFanout(), true, false);
  }

  @Bean
  public TopicExchange topicExchange() {
    return new TopicExchange(rabbitProperties.getExchanges().getTopic(), true, false);
  }

  // ============ QUEUES ============

  @Bean
  public Queue defaultQueue() {
    return new Queue(rabbitProperties.getQueues().getDefaultQueue(), true);
  }

  @Bean
  public Queue fanoutQueue1() {
    return new Queue(rabbitProperties.getQueues().getFanout1(), true);
  }

  @Bean
  public Queue fanoutQueue2() {
    return new Queue(rabbitProperties.getQueues().getFanout2(), true);
  }

  @Bean
  public Queue topicOrdersQueue() {
    return new Queue(rabbitProperties.getQueues().getTopicOrders(), true);
  }

  @Bean
  public Queue topicNotificationsQueue() {
    return new Queue(rabbitProperties.getQueues().getTopicNotifications(), true);
  }

  // ============ BINDINGS ============

  @Bean
  public Binding defaultBinding(Queue defaultQueue, DirectExchange defaultExchange) {
    return BindingBuilder.bind(defaultQueue)
        .to(defaultExchange)
        .with(rabbitProperties.getQueues().getDefaultQueue());
  }

  @Bean
  public Binding fanoutBinding1(Queue fanoutQueue1, FanoutExchange fanoutExchange) {
    return BindingBuilder.bind(fanoutQueue1)
        .to(fanoutExchange);
  }

  @Bean
  public Binding fanoutBinding2(Queue fanoutQueue2, FanoutExchange fanoutExchange) {
    return BindingBuilder.bind(fanoutQueue2)
        .to(fanoutExchange);
  }

  @Bean
  public Binding topicOrdersBinding(Queue topicOrdersQueue, TopicExchange topicExchange) {
    return BindingBuilder.bind(topicOrdersQueue)
        .to(topicExchange)
        .with("orders.*");
  }

  @Bean
  public Binding topicNotificationsBinding(Queue topicNotificationsQueue, TopicExchange topicExchange) {
    return BindingBuilder.bind(topicNotificationsQueue)
        .to(topicExchange)
        .with("notifications.*");
  }

}
