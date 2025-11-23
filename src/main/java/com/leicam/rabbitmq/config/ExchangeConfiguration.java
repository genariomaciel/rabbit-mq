package com.leicam.rabbitmq.config;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExchangeConfiguration {

  // ============ EXCHANGES ============
  private final RabbitProperties rabbitProperties;

  public ExchangeConfiguration(RabbitProperties rabbitProperties) {
    this.rabbitProperties = rabbitProperties;
  }

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
}
