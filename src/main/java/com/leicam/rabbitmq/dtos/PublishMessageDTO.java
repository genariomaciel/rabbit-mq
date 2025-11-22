package com.leicam.rabbitmq.dtos;

public record PublishMessageDTO(
    String exchangeType,
    String message,
    String routingKey
) {
}
