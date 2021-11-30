package com.agaram.eln.primary.repository.protocol;

import java.io.ByteArrayInputStream;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.agaram.eln.primary.model.protocols.LSprotocolfilesContent;

public interface LSprotocolfilesContentRepository extends MongoRepository<LSprotocolfilesContent, String>{

	LSprotocolfilesContent findByFileid(String fileid);

}
