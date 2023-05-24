package com.agaram.eln.primary.repository.fileManipulation;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.agaram.eln.primary.model.fileManipulation.Fileimages;

public interface FileimagesRepository extends MongoRepository<Fileimages, String> {

	Fileimages findByFileid(String fileid);

}
