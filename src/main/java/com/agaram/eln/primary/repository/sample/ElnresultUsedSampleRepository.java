package com.agaram.eln.primary.repository.sample;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.sample.ElnresultUsedSample;

public interface ElnresultUsedSampleRepository extends JpaRepository<ElnresultUsedSample, Integer> {

	List<ElnresultUsedSample> findBySamplecode(Integer samplecode);

}
