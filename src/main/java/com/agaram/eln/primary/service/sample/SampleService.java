package com.agaram.eln.primary.service.sample;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.agaram.eln.primary.model.sample.DerivedSamples;
import com.agaram.eln.primary.model.sample.Sample;
import com.agaram.eln.primary.model.sample.SampleCategory;
import com.agaram.eln.primary.model.sheetManipulation.LSfiletest;
import com.agaram.eln.primary.repository.sample.DerivedSamplesRepository;
import com.agaram.eln.primary.repository.sample.SampleRepository;

@Service
public class SampleService{
	
	@Autowired
	private SampleRepository samplerepository;
	
	@Autowired
	private DerivedSamplesRepository derivedsamplesrepository;

	public ResponseEntity<Object> getSampleonCategory(SampleCategory objsamplecat){
			
			List<Sample> lstsample = samplerepository.findBySamplecategoryAndNsitecodeOrderBySamplecodeDesc(objsamplecat,objsamplecat.getNsitecode());
			return new ResponseEntity<>(lstsample, HttpStatus.OK);
	}
	
	public ResponseEntity<Object> createSample(Sample sample)
	{
		if(sample.getDerivedtype() == 3)
		{
			derivedsamplesrepository.save(sample.getParentsamples());
		}
		samplerepository.save(sample);
		return new ResponseEntity<>(sample, HttpStatus.OK);
	}
	
	public ResponseEntity<Object> updateSample(Sample sample)
	{
		samplerepository.save(sample);
		return new ResponseEntity<>(sample, HttpStatus.OK);
	}
	
	public ResponseEntity<Object> getchildsample(List<Sample> sample)
	{
		List<DerivedSamples> derived = derivedsamplesrepository.findByParentsampleInOrderByDerivedsamplecode(sample);
		List<Integer> result = derived.stream()
                .map(DerivedSamples::getSamplecode)
                .collect(Collectors.toList());
		List<Sample> lstsample = samplerepository.findBysamplecodeInOrderBySamplecodeDesc(result);
		return new ResponseEntity<>(lstsample, HttpStatus.OK);
	}
}
