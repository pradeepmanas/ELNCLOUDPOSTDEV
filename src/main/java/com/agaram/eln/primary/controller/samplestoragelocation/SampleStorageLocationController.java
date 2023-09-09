package com.agaram.eln.primary.controller.samplestoragelocation;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.agaram.eln.primary.model.cfr.LScfttransaction;
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
		final SampleStorageLocation sampleStorageLocation = mapper.convertValue(mapObject.get("samplestoragelocation"),SampleStorageLocation.class);
		final SampleStorageVersion sampleStorageVersion = mapper.convertValue(mapObject.get("samplestorageversion"),SampleStorageVersion.class);
		final LScfttransaction Auditobj = mapper.convertValue(mapObject.get("objsilentaudit"),LScfttransaction.class);
		return sampleStorageLocationService.createSampleStorageLocation(sampleStorageLocation, sampleStorageVersion,Auditobj);
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
		final LScfttransaction Auditobj = mapper.convertValue(mapObject.get("objsilentaudit"),
				LScfttransaction.class);
		return sampleStorageLocationService.updateSampleStorageLocation(sampleStorageLocation, sampleStorageVersion,Auditobj);
	}

	@PostMapping(value = "/deleteSampleStorageLocation")
	public ResponseEntity<Object> deleteSampleStorageLocation(@Validated @RequestBody Map<String, Object> mapObject)
			throws JsonProcessingException {
		final ObjectMapper mapper = new ObjectMapper();
		final SampleStorageVersion sampleStorageVersion = mapper.convertValue(mapObject.get("samplestorageversion"),SampleStorageVersion.class);
		final LScfttransaction Auditobj = mapper.convertValue(mapObject.get("objsilentaudit"),LScfttransaction.class);
		return sampleStorageLocationService.deleteSampleStorageLocation(sampleStorageVersion,Auditobj);
	}
	
	@PostMapping(value = "/getAllActiveSampleStorageLocation")
	public ResponseEntity<Object> getAllActiveSampleStorageLocation(@RequestBody Map<String, Object> mapObject) {
		
		Integer nsiteInteger = (Integer) mapObject.get("sitekey");
		
		return sampleStorageLocationService.getAllActiveSampleStorageLocation(nsiteInteger);
	}
	
	@PostMapping(value = "/getActiveSampleStorageLocation")
	public ResponseEntity<Object> getActiveSampleStorageLocation(@RequestBody Map<String, Object> mapObject) {
		
		Integer nsiteInteger = (Integer) mapObject.get("sitekey");
		
		return sampleStorageLocationService.getActiveSampleStorageLocation(nsiteInteger);
	}
	
	@PostMapping(value = "/getActiveSampleStorageLocationByKey")
	public ResponseEntity<Object> getActiveSampleStorageLocationByKey(@Validated @RequestBody Map<String, Object> mapObject) {

		final int sampleStorageLocationKey = (Integer) mapObject.get("samplestoragelocationkey");
		return sampleStorageLocationService.getActiveSampleStorageLocationByKey(sampleStorageLocationKey);
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping(value = "/setStorageLocationOnNode")
	public ResponseEntity<Object> setStorageLocationOnNode(@Validated @RequestBody Map<String, Object> mapObject) {

		final int sampleStorageLocationKey =  Integer.parseInt(mapObject.get("samplestoragelocationkey").toString());
		final int inventoryCode = Integer.parseInt(mapObject.get("selectedMaterialInventory").toString());
		final String jsobString = mapObject.get("jsonbresult").toString();
		final Map<String, Object> selectedStorageId = (Map<String, Object>) mapObject.get("selectedStorageId");
		
		return sampleStorageLocationService.setStorageLocationOnNode(sampleStorageLocationKey,inventoryCode,selectedStorageId,jsobString);
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping(value = "/getSelectedStorageItem")
	public Boolean getSelectedStorageItem(@Validated @RequestBody Map<String, Object> mapObject) throws JsonProcessingException {

		final Map<String, Object> selectedStorageId = (Map<String, Object>) mapObject.get("selectedStorageId");
		
		return sampleStorageLocationService.getSelectedStorageItem(selectedStorageId);
	}
	
	@PostMapping(value = "/getStorageIdBasedOnInvent")
	public ResponseEntity<Object> getStorageIdBasedOnInvent(@Validated @RequestBody Map<String, Object> mapObject) {

		Integer nsiteInteger = (Integer) mapObject.get("sitekey");
		final int inventoryCode = Integer.parseInt(mapObject.get("nmaterialinventorycode").toString());
		
		return sampleStorageLocationService.getStorageIdBasedOnInvent(inventoryCode,nsiteInteger);
	}
}
