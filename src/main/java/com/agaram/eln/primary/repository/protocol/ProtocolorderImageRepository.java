package com.agaram.eln.primary.repository.protocol;



import org.springframework.data.mongodb.repository.MongoRepository;

import com.agaram.eln.primary.model.protocols.ProtocolorderImage;

public interface ProtocolorderImageRepository extends MongoRepository<ProtocolorderImage, String> {

	ProtocolorderImage findByFileid(String fileid);

}
