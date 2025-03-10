package com.agaram.eln.primary.controller.sample;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.agaram.eln.primary.model.sample.SampleType;
import com.agaram.eln.primary.service.sample.SampleTypeService;


@RestController
@RequestMapping("/SampleType")
public class SampleTypeController {

	@Autowired
	SampleTypeService sampleTypeService;
	
	@RequestMapping(value = "/getSampleType", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getSampleType(@RequestBody SampleType objSampleType) throws Exception {		
		return (ResponseEntity<Object>) sampleTypeService.getSampleType(objSampleType);
	}
	@PostMapping(value = "/getActiveSampleType")
	public ResponseEntity<Object> getActiveSampleType(@RequestBody Map<String, Object> inputMap) throws Exception {
		Integer nsiteInteger = (Integer) inputMap.get("nsitecode");
		return sampleTypeService.getActiveSampleType(nsiteInteger);
	}
	
	@RequestMapping(value = "/modifySampleType", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> modifySampleType(@RequestBody SampleType objSampleType) throws Exception {		
		return (ResponseEntity<Object>) sampleTypeService.modifySampleType(objSampleType);
	}
	
	@PostMapping("/RetiredSample")
	public SampleType RetiredSample(@RequestBody SampleType objSampleType) throws Exception {
		return sampleTypeService.RetiredSample(objSampleType);
	}
	
	@RequestMapping(value = "/getSampleTypeonId", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getSampleTypeonId(@RequestBody SampleType objSampleType) throws Exception {		
		return (ResponseEntity<Object>) sampleTypeService.getMaterialTypeonId(objSampleType);
	}
}