package com.agaram.eln.primary.controller.Material;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;

import com.agaram.eln.primary.service.Material.MaterialService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/material")
public class MaterialController {
	
	@Autowired
	private MaterialService objMaterialService;
	
	@RequestMapping(value = "/getMaterial", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getMaterialType(@RequestBody Map<String, Object> inputMap) throws Exception {
		
		return (ResponseEntity<Object>) objMaterialService.getMaterialType();
	}
	
//	@RequestMapping(value = "/getMaterialByTypeCode", method = RequestMethod.POST)
//	public ResponseEntity<Object> getMaterialByTypeCode(@RequestBody Map<String, Object> inputMap) throws Exception {
//		
//		return objMaterialService.getMaterialByTypeCode(inputMap);
//	}
	
	@RequestMapping(value = "/getMaterialcombo", method = RequestMethod.POST)
	public ResponseEntity<Object> getMaterialcombo(@RequestBody Map<String, Object> inputMap) throws Exception {
		Integer nmaterialtypecode = (Integer) inputMap.get("nmaterialtypecode");
		return (ResponseEntity<Object>) objMaterialService.getMaterialcombo(nmaterialtypecode);
	}

}
