package com.agaram.eln.primary.repository.material;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.agaram.eln.primary.model.material.MaterialChemicalDiag;

public interface MaterialChemicalDiagRepository extends MongoRepository<MaterialChemicalDiag, String>{
	MaterialChemicalDiag findByFileid(String fileid);
}
