package com.example.testApp.service;

import com.example.testApp.entity.OutboxTable;
import com.example.testApp.kafka.KafkaProducerService;
import com.example.testApp.repository.OutboxRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Slf4j
public class ProducerService {



    private final KafkaProducerService kafkaProducerService;

    private final OutboxRepository outboxRepository;

    public ProducerService(KafkaProducerService kafkaProducerService, OutboxRepository outboxRepository){
        this.kafkaProducerService = kafkaProducerService;
        this.outboxRepository = outboxRepository;
        System.out.println("ProducerService created");
    }


    @Scheduled(fixedRate = 5000)
    @Transactional
    public void pollAndSend(){
        //
        Pageable pageable = PageRequest.of(0, 5, Sort.by("createdAt").ascending());
        List<OutboxTable>events = outboxRepository.get50records(false,pageable);

        for(OutboxTable event: events){
            try{//
                kafkaProducerService.sendMessage("test-topic",event.getMessage());
                event.setProcessed(true);
                outboxRepository.save(event);
            }catch(Exception ex){
                log.warn(ex.getMessage());
            }
        }
    }
}
