package com.agaram.eln.primary.repository.sample;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.sample.SampleProjectMap;
import com.agaram.eln.primary.model.usermanagement.LSprojectmaster;

public interface SampleProjectMapRepository extends JpaRepository<SampleProjectMap, Integer>{
	List<SampleProjectMap> findBySamplecodeOrderBySampleprojectcode(Integer samplecode);

	List<SampleProjectMap> findByLsproject(LSprojectmaster project);
}
