package com.leicam.rabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BindingConfiguration {
  
  // ============ BINDINGS ============

  private final RabbitProperties rabbitProperties;
  
  public BindingConfiguration(RabbitProperties rabbitProperties) {
    this.rabbitProperties = rabbitProperties;
  }
  
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
  public Binding fanoutBinding3(Queue fanoutQueue3, FanoutExchange fanoutExchange) {
    return BindingBuilder.bind(fanoutQueue3)
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
