package com.agaram.eln.primary.repository.protocol;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.agaram.eln.primary.model.protocols.Protocolvideos;

public interface ProtocolvideosRepository extends MongoRepository<Protocolvideos, String> {

	Protocolvideos findByFileid(String fileid);

}
