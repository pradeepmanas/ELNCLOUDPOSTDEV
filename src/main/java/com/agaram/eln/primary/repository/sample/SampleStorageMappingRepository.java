package com.agaram.eln.primary.repository.sample;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.sample.SampleStorageMapping;

public interface SampleStorageMappingRepository  extends JpaRepository<SampleStorageMapping, Integer>{

}
