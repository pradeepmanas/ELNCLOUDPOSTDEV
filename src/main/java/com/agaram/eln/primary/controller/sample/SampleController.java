package com.agaram.eln.primary.controller.sample;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.agaram.eln.primary.model.sample.Sample;
import com.agaram.eln.primary.model.sample.SampleCategory;
import com.agaram.eln.primary.service.sample.SampleService;

@RestController
@RequestMapping("/sample")
public class SampleController {
	
	@Autowired
	private SampleService objSampleService;
	
	@RequestMapping(value = "/getSampleonCategory", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getSampleonCategory(@RequestBody SampleCategory objsamplecat) throws Exception {

		return (ResponseEntity<Object>) objSampleService.getSampleonCategory(objsamplecat);
	}
	
	@RequestMapping(value = "/createSample", method = RequestMethod.POST)
	public ResponseEntity<Object> createSample(@RequestBody Sample obj) throws Exception {

		return objSampleService.createSample(obj);
	}
	
	@RequestMapping(value = "/updateSample", method = RequestMethod.POST)
	public ResponseEntity<Object> updateSample(@RequestBody Sample obj) throws Exception {

		return objSampleService.updateSample(obj);
	}
	
	@RequestMapping(value = "/getchildsample", method = RequestMethod.POST)
	public ResponseEntity<Object> getchildsample(@RequestBody Sample[] obj) throws Exception {
		return objSampleService.getchildsample(Arrays.asList(obj));
	}
}
