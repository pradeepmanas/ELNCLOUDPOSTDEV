package com.agaram.eln.primary.controller.material;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.agaram.eln.primary.model.material.MaterialConfig;
import com.agaram.eln.primary.model.material.MaterialType;
import com.agaram.eln.primary.service.material.MaterialTypeService;

@RestController
@RequestMapping("/materialType")
public class MaterialTypeController {
	@Autowired
	private MaterialTypeService objMaterialTypeService;
	
	@RequestMapping(value = "/getMaterialType", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getMaterialType(@RequestBody Map<String, Object> inputMap) throws Exception {		
		return (ResponseEntity<Object>) objMaterialTypeService.getMaterialType();
	}
	
	@RequestMapping(value = "/getMaterialTypeField", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getMaterialTypeField(@RequestBody MaterialType objMaterialType) throws Exception {		
		return (ResponseEntity<Object>) objMaterialTypeService.getMaterialTypeField(objMaterialType);
	}
	
	@RequestMapping(value = "/createMaterialType", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> createMaterialType(@RequestBody MaterialType objMaterialType) throws Exception {		
		return (ResponseEntity<Object>) objMaterialTypeService.createMaterialType(objMaterialType);
	}
	
	@RequestMapping(value = "/updateMaterialTypeField", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> updateMaterialTypeField(@RequestBody MaterialConfig objMaterialType) throws Exception {		
		return (ResponseEntity<Object>) objMaterialTypeService.updateMaterialTypeField(objMaterialType);
	}
}
