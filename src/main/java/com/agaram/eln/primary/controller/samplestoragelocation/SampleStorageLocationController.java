package com.agaram.eln.primary.controller.samplestoragelocation;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.agaram.eln.primary.model.samplestoragelocation.SampleStorageLocation;
import com.agaram.eln.primary.model.samplestoragelocation.SampleStorageVersion;
import com.agaram.eln.primary.service.samplestoragelocation.SampleStorageLocationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@RestController
@RequestMapping(value = "/sampleStorage")
public class SampleStorageLocationController {

	@Autowired
	SampleStorageLocationService sampleStorageLocationService;

	@PostMapping(value = "/createSampleStorageLocation")
	public ResponseEntity<Object> createSampleStorageLocation(@Validated @RequestBody Map<String, Object> mapObject)
			throws JsonProcessingException {
		final ObjectMapper mapper = new ObjectMapper();
//		mapper.registerModule(new JavaTimeModule());
		final SampleStorageLocation sampleStorageLocation = mapper.convertValue(mapObject.get("samplestoragelocation"),
				SampleStorageLocation.class);
		final SampleStorageVersion sampleStorageVersion = mapper.convertValue(mapObject.get("samplestorageversion"),
				SampleStorageVersion.class);
		return sampleStorageLocationService.createSampleStorageLocation(sampleStorageLocation, sampleStorageVersion);
	}

	@PostMapping(value = "/updateSampleStorageLocation")
	public ResponseEntity<Object> updateSampleStorageLocation(@Validated @RequestBody Map<String, Object> mapObject)
			throws JsonProcessingException {
		final ObjectMapper mapper = new ObjectMapper();
//		mapper.registerModule(new JavaTimeModule());
		final SampleStorageLocation sampleStorageLocation = mapper.convertValue(mapObject.get("samplestoragelocation"),
				SampleStorageLocation.class);
		final SampleStorageVersion sampleStorageVersion = mapper.convertValue(mapObject.get("samplestorageversion"),
				SampleStorageVersion.class);
		return sampleStorageLocationService.updateSampleStorageLocation(sampleStorageLocation, sampleStorageVersion);
	}

	@PostMapping(value = "/deleteSampleStorageLocation")
	public ResponseEntity<Object> deleteSampleStorageLocation(@Validated @RequestBody Map<String, Object> mapObject)
			throws JsonProcessingException {
		final ObjectMapper mapper = new ObjectMapper();
//		mapper.registerModule(new JavaTimeModule());
		final SampleStorageVersion sampleStorageVersion = mapper.convertValue(mapObject.get("samplestorageversion"),
				SampleStorageVersion.class);

		return sampleStorageLocationService.deleteSampleStorageLocation(sampleStorageVersion);
	}
	
	@PostMapping(value = "/getAllActiveSampleStorageLocation")
	public ResponseEntity<Object> getAllActiveSampleStorageLocation(@RequestBody Map<String, Object> mapObject) {
		
		Integer nsiteInteger = (Integer) mapObject.get("sitekey");
		
		return sampleStorageLocationService.getAllActiveSampleStorageLocation(nsiteInteger);
	}
	
	@PostMapping(value = "/getActiveSampleStorageLocationByKey")
	public ResponseEntity<Object> getActiveSampleStorageLocationByKey(@Validated @RequestBody Map<String, Object> mapObject) {

		final int sampleStorageLocationKey = (Integer) mapObject.get("samplestoragelocationkey");
		return sampleStorageLocationService.getActiveSampleStorageLocationByKey(sampleStorageLocationKey);
	}
}
