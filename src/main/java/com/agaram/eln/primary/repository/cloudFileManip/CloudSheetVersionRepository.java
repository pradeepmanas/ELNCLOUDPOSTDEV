package com.agaram.eln.primary.repository.cloudFileManip;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.cloudFileManip.CloudSheetVersion;

public interface CloudSheetVersionRepository extends JpaRepository<CloudSheetVersion, Long> {
	public CloudSheetVersion findById(Long Id);
}
