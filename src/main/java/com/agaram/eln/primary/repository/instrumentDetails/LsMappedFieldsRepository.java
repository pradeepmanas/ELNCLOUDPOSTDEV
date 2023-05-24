package com.agaram.eln.primary.repository.instrumentDetails;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.instrumentDetails.LsMappedFields;

public interface LsMappedFieldsRepository extends JpaRepository<LsMappedFields, String>{
	public List<LsMappedFields> findBysInstrumentIDNotIn(List<String> lsInst);

}
