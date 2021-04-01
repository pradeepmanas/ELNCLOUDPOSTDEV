package com.agaram.eln.primary.repository.cloudFileManip;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.cloudFileManip.CloudSheetCreation;

public interface CloudSheetCreationRepository extends JpaRepository<CloudSheetCreation, Long> {
	public CloudSheetCreation findById(Long Id);
}
