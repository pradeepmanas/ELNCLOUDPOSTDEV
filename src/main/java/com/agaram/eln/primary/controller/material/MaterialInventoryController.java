package com.agaram.eln.primary.controller.material;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.agaram.eln.primary.service.material.MaterialInventoryService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping(value = "materialinventory")
public class MaterialInventoryController {
	
	@Autowired
	private MaterialInventoryService objMaterialInventoryService;

	@RequestMapping(value = "/getMaterialInventory", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getMaterialInventory(@RequestBody Map<String, Object> inputMap) throws Exception {
		ObjectMapper objMapper = new ObjectMapper();
		return (ResponseEntity<Object>) objMaterialInventoryService.getMaterialInventory();
	}

}
