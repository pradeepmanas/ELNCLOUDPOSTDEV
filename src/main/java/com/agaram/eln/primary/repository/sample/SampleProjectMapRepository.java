package com.agaram.eln.primary.repository.sample;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.sample.SampleProjectMap;

public interface SampleProjectMapRepository extends JpaRepository<SampleProjectMap, Integer>{
	List<SampleProjectMap> findBySamplecodeOrderBySampleprojectcode(Integer samplecode);
}
