package com.agaram.eln.primary.repository.fileManipulation;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.agaram.eln.primary.model.fileManipulation.UserSignature;

public interface UserSignatureRepository extends MongoRepository<UserSignature, Integer> {

	UserSignature findById(Integer id);
	Long deleteById(Integer id);
	
}