package com.agaram.eln.primary.controller.sample;

import java.text.ParseException;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.sample.SampleCategory;
import com.agaram.eln.primary.service.sample.SampleCategoryService;

@RestController
@RequestMapping(value = "/SampleCategory", method = RequestMethod.POST)
public class SampleCategoryController {
	
	@Autowired
	SampleCategoryService sampleCategoryService;
	
	@PostMapping(value = "/getSampleType")
	public ResponseEntity<Object> getSampleType(@RequestBody Map<String, Object> inputMap) throws Exception {
		Integer nsiteInteger = (Integer) inputMap.get("nsitecode");
		return sampleCategoryService.getSampleType(nsiteInteger);
	}
	
	@PostMapping(value = "/getSampleCategory")
	public ResponseEntity<Object> getSampleCategory(@RequestBody Map<String, Object> inputMap) throws Exception {
		
		Integer nsiteInteger = (Integer) inputMap.get("nsitecode");
		
		return sampleCategoryService.getSampleCategory(nsiteInteger);
	}

	@PostMapping(value = "/createSampleCategory")
	public ResponseEntity<Object> createSampleCategory(@RequestBody Map<String, Object> inputMap) throws Exception {
		return sampleCategoryService.createSampleCategory(inputMap);
	}
	
	@PostMapping(value = "/updateSampleCategory")
	public ResponseEntity<Object> updateSampleCategory(@RequestBody Map<String, Object> inputMap) throws Exception {
		final ObjectMapper objmapper = new ObjectMapper();
		final SampleCategory sampleCategory = objmapper.convertValue(inputMap.get("SampleCategory"),SampleCategory.class);
		
		return sampleCategoryService.updateSampleCategory(sampleCategory);
	}
	
	@PostMapping(value = "/retireSampleCategory")
	public ResponseEntity<Object> retireSampleCategory(@RequestBody Map<String, Object> inputMap) throws Exception {
		final ObjectMapper objmapper = new ObjectMapper();
		final SampleCategory materialCategory = objmapper.convertValue(inputMap.get("SampleCategory"),SampleCategory.class);
		final LScfttransaction objsilentaudit = objmapper.convertValue(inputMap.get("objsilentaudit"),LScfttransaction.class);
		return sampleCategoryService.retireSampleCategory(materialCategory,objsilentaudit);
	}
}