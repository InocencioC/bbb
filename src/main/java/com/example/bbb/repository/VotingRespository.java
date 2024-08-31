package com.example.bbb.repository;

import com.example.bbb.model.VotingModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VotingRespository extends MongoRepository<VotingModel, String> {

}
