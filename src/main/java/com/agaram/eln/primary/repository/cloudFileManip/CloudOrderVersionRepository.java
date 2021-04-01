package com.agaram.eln.primary.repository.cloudFileManip;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.cloudFileManip.CloudOrderVersion;

public interface CloudOrderVersionRepository extends JpaRepository<CloudOrderVersion, Long> {
	public CloudOrderVersion findById(Long Id);
}
