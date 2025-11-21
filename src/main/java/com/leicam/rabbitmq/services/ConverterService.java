package com.leicam.rabbitmq.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class ConverterService {
  private final ObjectMapper objectMapper;

  public ConverterService(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  public String toJson(Object message) {
    try {
      return objectMapper.writeValueAsString(message);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Falha ao serializar objeto para JSON: " + e.getMessage());
    }
  }
}
