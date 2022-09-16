package com.agaram.eln.primary.controller.material;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.agaram.eln.primary.service.material.MaterialService;

@RestController
@RequestMapping("/material")
public class MaterialController {
	
	@Autowired
	private MaterialService objMaterialService;
	
	@RequestMapping(value = "/getMaterial", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getMaterialType(@RequestBody Map<String, Object> inputMap) throws Exception {
		
		return (ResponseEntity<Object>) objMaterialService.getMaterialType();
	}
	
	@RequestMapping(value = "/getMaterialByTypeCode", method = RequestMethod.POST)
	public ResponseEntity<Object> getMaterialByTypeCode(@RequestBody Map<String, Object> inputMap) throws Exception {
		
		return objMaterialService.getMaterialByTypeCode(inputMap);
	}
	
	@RequestMapping(value = "/getMaterialcombo", method = RequestMethod.POST)
	public ResponseEntity<Object> getMaterialcombo(@RequestBody Map<String, Object> inputMap) throws Exception {
		Integer nmaterialtypecode = (Integer) inputMap.get("nmaterialtypecode");
		return (ResponseEntity<Object>) objMaterialService.getMaterialcombo(nmaterialtypecode);
	}
	
	@RequestMapping(value = "/createMaterial", method = RequestMethod.POST)
	public ResponseEntity<Object> createMaterial(@RequestBody Map<String, Object> inputMap) throws Exception {
		
		return objMaterialService.createMaterial(inputMap);
	}
	
	@RequestMapping(value = "/getMaterialDetails", method = RequestMethod.POST)
	public ResponseEntity<Object> getMaterialDetails(@RequestBody Map<String, Object> inputMap) throws Exception {
		return objMaterialService.getMaterialDetails(inputMap);
	}
	
//	@RequestMapping(value = "/deleteMaterial", method = RequestMethod.POST)
//	public ResponseEntity<Object> deleteMaterial(@RequestBody Map<String, Object> inputMap) throws Exception {
//		ObjectMapper objMapper = new ObjectMapper();
//		final UserInfo userInfo = objMapper.convertValue(inputMap.get("userinfo"), UserInfo.class);
//		requestContext.setUserInfo(userInfo);
//		return objMaterialService.deleteMaterial(inputMap, userInfo);
//	}

//	@RequestMapping(value = "/getMaterialEdit", method = RequestMethod.POST)
//	public ResponseEntity<Object> getMaterialEdit(@RequestBody Map<String, Object> inputMap) throws Exception {
//		
//		return objMaterialService.getMaterialEdit(inputMap);
//	}

}
