package com.agaram.eln.primary.repository.protocol;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.agaram.eln.primary.model.protocols.Protocolordervideos;

public interface ProtocolordervideosRepository extends MongoRepository<Protocolordervideos, String>{

	Protocolordervideos findByFileid(String fileid);

}
