package com.example.bbb.service;

import com.example.bbb.model.ParticipantModel;
import com.example.bbb.model.VotingModel;
import com.example.bbb.repository.VotingRespository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import java.util.Date;

@Service
@Slf4j
@AllArgsConstructor
public class VotingService {
    private final VotingRespository votingRespository;
    @KafkaListener(topics = "voting", groupId = "MicroServiceVoting")
    private void execute(ConsumerRecord<String, String> register){

        String participantStr = register.value();
        log.info("Received vote = {}", participantStr);

        ObjectMapper mapper = new ObjectMapper();
        ParticipantModel participantModel = null;

        try {
            participantModel = mapper.readValue(participantStr, ParticipantModel.class);
        } catch (JsonProcessingException ex) {
            log.error("Error converting the voting [{}]", participantStr, ex);
            return;
        }

        VotingModel voting = new VotingModel(null, participantModel, new Date());
        VotingModel entity = votingRespository.save(voting);

        log.info("Vote registered successfully [id={}, dataHora{}]", entity.getId(), entity.getDataHora());
    }
}
