package com.agaram.eln.primary.repository.protocol;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.agaram.eln.primary.model.protocols.ProtocolImage;

public interface ProtocolImageRepository extends MongoRepository<ProtocolImage, String>{

	ProtocolImage findByFileid(String fileid);

}
