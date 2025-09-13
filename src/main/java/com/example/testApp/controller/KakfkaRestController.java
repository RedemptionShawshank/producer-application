package com.example.testApp.controller;


import com.example.testApp.kafka.KafkaProducerService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kafka")
public class KakfkaRestController {

    private final KafkaProducerService producerService;

    public KakfkaRestController(KafkaProducerService producerService) {
        this.producerService = producerService;
    }

    @PostMapping("/send/{message}")
    public String sendMessage(@PathVariable String message) {
        producerService.sendMessage("test-topic", message);
        return "Message sent: " + message;
    }
}
