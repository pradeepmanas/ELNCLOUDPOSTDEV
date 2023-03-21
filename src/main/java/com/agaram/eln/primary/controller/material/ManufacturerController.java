package com.agaram.eln.primary.controller.material;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.agaram.eln.primary.model.material.Manufacturer;
import com.agaram.eln.primary.service.material.ManufacturerService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping(value = "/manufacturer", method = RequestMethod.POST)
public class ManufacturerController {
	
	@Autowired
	private ManufacturerService manufacturerService;
	
	ObjectMapper objMapper = new ObjectMapper();
	
	@RequestMapping(value = "/getManufacturer", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getManufacturer(@RequestBody Map<String, Object> inputMap) throws Exception {
		Integer nsiteInteger = (Integer) inputMap.get("nsitecode");
		return (ResponseEntity<Object>) manufacturerService.getManufacturer(nsiteInteger);
	}
	
	@RequestMapping(value = "/createManufacturer", method = RequestMethod.POST)
	public ResponseEntity<Object> createManufacturer(@RequestBody Map<String, Object> inputMap) throws Exception {
		Manufacturer objManufacturer = objMapper.convertValue(inputMap.get("manufacturer"), new TypeReference<Manufacturer>() {});
		return manufacturerService.createManufacturer(objManufacturer);
	}

	@RequestMapping(value = "/updateManufacturer", method = RequestMethod.POST)
	public ResponseEntity<Object> updateManufacturer(@RequestBody Map<String, Object> inputMap) throws Exception {
		Manufacturer objManufacturer = objMapper.convertValue(inputMap.get("manufacturer"), new TypeReference<Manufacturer>() {	});
		return manufacturerService.updateManufacturer(objManufacturer);
	}

	@RequestMapping(value = "/deleteManufacturer", method = RequestMethod.POST)
	public ResponseEntity<Object> deleteManufacturer(@RequestBody Map<String, Object> inputMap) throws Exception {
		Manufacturer objManufacturer = objMapper.convertValue(inputMap.get("manufacturer"), new TypeReference<Manufacturer>() { });
		return manufacturerService.deleteManufacturer(objManufacturer);
	}

	@RequestMapping(value = "/getActiveManufacturerById", method = RequestMethod.POST)
	public Manufacturer getActiveManufacturerById(@RequestBody Map<String, Object> inputMap) throws Exception {
		int nunitCode = (int) inputMap.get("nsuppliercode");
		return manufacturerService.getActiveManufacturerById(nunitCode);
	}
}