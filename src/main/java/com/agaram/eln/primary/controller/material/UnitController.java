package com.agaram.eln.primary.controller.material;

import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.material.Unit;
import com.agaram.eln.primary.service.material.UnitService;

@RestController
@RequestMapping("/unit")
public class UnitController {

	@Autowired
	private UnitService unitService;

	@RequestMapping(value = "/getUnit", method = RequestMethod.POST)
	public ResponseEntity<Object> getUnit(@RequestBody Map<String, Object> inputMap) throws Exception {

		Integer nsiteInteger = (Integer) inputMap.get("nsitecode");
		
		return unitService.getUnit(nsiteInteger);

	}

	@RequestMapping(value = "/createUnit", method = RequestMethod.POST)
	public ResponseEntity<Object> createUnit(@RequestBody Map<String, Object> inputMap) throws Exception {

		ObjectMapper objMapper = new ObjectMapper();
		Unit objUnit = objMapper.convertValue(inputMap.get("unit"), new TypeReference<Unit>() {
		});

		return unitService.createUnit(objUnit);

	}

	@RequestMapping(value = "/updateUnit", method = RequestMethod.POST)
	public ResponseEntity<Object> updateUnit(@RequestBody Map<String, Object> inputMap) throws Exception {

		ObjectMapper objMapper = new ObjectMapper();
		Unit objUnit = objMapper.convertValue(inputMap.get("unit"), new TypeReference<Unit>() {
		});

		return unitService.updateUnit(objUnit);

	}

	@RequestMapping(value = "/deleteUnit", method = RequestMethod.POST)
	public ResponseEntity<Object> deleteUnit(@RequestBody Map<String, Object> inputMap) throws Exception {

		ObjectMapper objMapper = new ObjectMapper();
		Unit objUnit = objMapper.convertValue(inputMap.get("unit"), new TypeReference<Unit>() {
		});

		return unitService.deleteUnit(objUnit);

	}

	@RequestMapping(value = "/getActiveUnitById", method = RequestMethod.POST)
	public Unit getActiveUnitById(@RequestBody Map<String, Object> inputMap) throws Exception {

		int nunitCode = (int) inputMap.get("nunitcode");

		return unitService.getActiveUnitById(nunitCode);

	}

}