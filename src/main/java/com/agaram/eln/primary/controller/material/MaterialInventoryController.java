package com.agaram.eln.primary.controller.material;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.agaram.eln.primary.service.material.MaterialInventoryService;

@RestController
@RequestMapping(value = "materialinventory")
public class MaterialInventoryController {

	@Autowired
	private MaterialInventoryService materialInventoryService;

	@RequestMapping(value = "/getMaterialInventory", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getMaterialInventory(@RequestBody Map<String, Object> inputMap) throws Exception {

		return (ResponseEntity<Object>) materialInventoryService.getMaterialInventory();
	}

	@RequestMapping(value = "/getMaterialInventorycombo", method = RequestMethod.POST)
	public ResponseEntity<Object> getMaterialInventorycombo(@RequestBody Map<String, Object> inputMap)
			throws Exception {
		Integer nmaterialtypecode = (Integer) inputMap.get("nmaterialtypecode");
		Integer nmaterialcatcode = null;
		if (inputMap.containsKey("nmaterialcatcode")) {
			nmaterialcatcode = (int) inputMap.get("nmaterialcatcode");
		}
		return (ResponseEntity<Object>) materialInventoryService.getMaterialInventorycombo(nmaterialtypecode,
				nmaterialcatcode);
	}

	@RequestMapping(value = "/getMaterialInventoryByID", method = RequestMethod.POST)
	public ResponseEntity<Object> getMaterialInventoryByID(@RequestBody Map<String, Object> inputMap) throws Exception {
		
		return materialInventoryService.getMaterialInventoryByID(inputMap);
	}
	
	@RequestMapping(value = "/createMaterialInventory", method = RequestMethod.POST)
	public ResponseEntity<Object> createMaterialInventory(@RequestBody Map<String, Object> inputMap) throws Exception {

		return materialInventoryService.createMaterialInventory(inputMap);
	}

}
