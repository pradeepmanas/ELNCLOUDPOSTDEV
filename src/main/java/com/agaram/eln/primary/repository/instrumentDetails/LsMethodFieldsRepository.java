package com.agaram.eln.primary.repository.instrumentDetails;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.instrumentDetails.LsMethodFields;

public interface LsMethodFieldsRepository extends JpaRepository<LsMethodFields, String> {
	public List<LsMethodFields> findByinstrumentidNotIn(List<String> lsInst);
}