package com.agaram.eln.primary.service.sample;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.agaram.eln.primary.model.sample.Sample;
import com.agaram.eln.primary.model.sample.SampleCategory;
import com.agaram.eln.primary.repository.sample.SampleRepository;

@Service
public class SampleService{
	
	@Autowired
	private SampleRepository samplerepository;

	public ResponseEntity<Object> getSampleonCategory(SampleCategory objsamplecat){
			
			List<Sample> lstsample = samplerepository.findBySamplecategoryAndNsitecodeOrderBySamplecodeDesc(objsamplecat,objsamplecat.getNsitecode());
			return new ResponseEntity<>(lstsample, HttpStatus.OK);
	}
	
	public ResponseEntity<Object> createSample(Sample sample)
	{
		samplerepository.save(sample);
		return new ResponseEntity<>(sample, HttpStatus.OK);
	}
	
	public ResponseEntity<Object> updateSample(Sample sample)
	{
		samplerepository.save(sample);
		return new ResponseEntity<>(sample, HttpStatus.OK);
	}
}
