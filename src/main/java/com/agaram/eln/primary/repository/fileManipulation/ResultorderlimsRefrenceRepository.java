package com.agaram.eln.primary.repository.fileManipulation;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.agaram.eln.primary.model.fileManipulation.ResultorderlimsRefrence;

public interface ResultorderlimsRefrenceRepository extends MongoRepository<ResultorderlimsRefrence, String>{
	public ResultorderlimsRefrence findById(String Id);
}