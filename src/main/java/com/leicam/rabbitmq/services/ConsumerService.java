package com.leicam.rabbitmq.services;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class ConsumerService {

  @RabbitListener(queues = "#{@rabbitProperties.getQueues().getDefaultQueue()}")
  public void listenDefaultQueue(String message) {
    System.out.println("=====================================");
    System.out.println("[DEFAULT QUEUE] Mensagem recebida:");
    System.out.println(message);
    System.out.println("=====================================");
  }

  @RabbitListener(queues = "#{@rabbitProperties.getQueues().getFanout1()}")
  public void listenFanoutQueue1(String message) {
    System.out.println("=====================================");
    System.out.println("[FANOUT QUEUE 1] Mensagem recebida:");
    System.out.println(message);
    System.out.println("=====================================");
  }

  @RabbitListener(queues = "#{@rabbitProperties.getQueues().getFanout2()}")
  public void listenFanoutQueue2(String message) {
    System.out.println("=====================================");
    System.out.println("[FANOUT QUEUE 2] Mensagem recebida:");
    System.out.println(message);
    System.out.println("=====================================");
  }

  @RabbitListener(queues = "#{@rabbitProperties.getQueues().getFanout3()}")
  public void listenFanoutQueue3(String message) {
    System.out.println("=====================================");
    System.out.println("[FANOUT QUEUE 3] Mensagem recebida:");
    System.out.println(message);
    System.out.println("=====================================");
  }

  @RabbitListener(queues = "#{@rabbitProperties.getQueues().getTopicOrders()}")
  public void listenTopicOrders(String message) {
    System.out.println("=====================================");
    System.out.println("[TOPIC - ORDERS] Mensagem recebida:");
    System.out.println(message);
    System.out.println("=====================================");
  }

  @RabbitListener(queues = "#{@rabbitProperties.getQueues().getTopicNotifications()}")
  public void listenTopicNotifications(String message) {
    System.out.println("=====================================");
    System.out.println("[TOPIC - NOTIFICATIONS] Mensagem recebida:");
    System.out.println(message);
    System.out.println("=====================================");
  }
}
