package com.agaram.eln.primary.repository.protocol;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.agaram.eln.primary.model.protocols.LSprotocolfilesContent;
import com.agaram.eln.primary.model.protocols.LSprotocolorderfilesContent;

public interface LSprotocolorderfilesContentRepository extends MongoRepository<LSprotocolorderfilesContent, String>{

	LSprotocolfilesContent findByFileid(String fileid);

}
