package com.leicam.rabbitmq.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.leicam.rabbitmq.dtos.RequestDTO;
import com.leicam.rabbitmq.services.PublisherService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/api/v1/publisher")
public class PublisherController {

  private final PublisherService publisherService;
  public PublisherController(PublisherService publisherService) {
    this.publisherService = publisherService;
  }

  @PostMapping()
  public ResponseEntity<String> postMethodName(@RequestBody RequestDTO message) {
      publisherService.publish(message.message());
      return ResponseEntity.ok("sent");
  }
  

}
