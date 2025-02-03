package com.agaram.eln.primary.repository.sample;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.sample.Sample;
import com.agaram.eln.primary.model.sample.SampleCategory;

public interface SampleRepository  extends JpaRepository<Sample,Integer>{

	List<Sample> findBySamplecategoryAndNsitecodeOrderBySamplecodeDesc(SampleCategory objsamplecat,Integer nsiteInteger);
	
	List<Sample> findBysamplecodeInOrderBySamplecodeDesc(List<Integer> samplelist);

	List<Sample> findByNsitecodeOrderBySamplecodeDesc(Integer nsitecode);

	List<Sample> findByNsitecodeAndCreateddateBetweenOrderBySamplecodeDesc(Integer nsiteInteger, Date fromDate,
			Date toDate);

}
