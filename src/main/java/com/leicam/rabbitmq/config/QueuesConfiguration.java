package com.leicam.rabbitmq.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueuesConfiguration {
  
  private final RabbitProperties rabbitProperties;

  public QueuesConfiguration(RabbitProperties rabbitProperties) {
    this.rabbitProperties = rabbitProperties;
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
  public Queue fanoutQueue3() {
    return new Queue(rabbitProperties.getQueues().getFanout3(), true);
  }

  @Bean
  public Queue topicOrdersQueue() {
    return new Queue(rabbitProperties.getQueues().getTopicOrders(), true);
  }

  @Bean
  public Queue topicNotificationsQueue() {
    return new Queue(rabbitProperties.getQueues().getTopicNotifications(), true);
  }
}
