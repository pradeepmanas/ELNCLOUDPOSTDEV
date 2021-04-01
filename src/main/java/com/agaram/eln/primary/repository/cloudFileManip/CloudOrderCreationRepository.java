package com.agaram.eln.primary.repository.cloudFileManip;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.cloudFileManip.CloudOrderCreation;

public interface CloudOrderCreationRepository  extends JpaRepository<CloudOrderCreation, Long> {
	public CloudOrderCreation findById(Long Id);
	public List<CloudOrderCreation> findByContentvaluesContainingIgnoreCase(String Value);
	public List<CloudOrderCreation> findByContentparameterContainingIgnoreCase(String Value);
}
