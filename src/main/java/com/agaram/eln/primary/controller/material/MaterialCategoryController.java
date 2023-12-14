package com.agaram.eln.primary.controller.material;

import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.material.MaterialCategory;
import com.agaram.eln.primary.service.material.MaterialCategoryService;

@RestController
@RequestMapping(value = "/materialcategory", method = RequestMethod.POST)
public class MaterialCategoryController {

	@Autowired
	private MaterialCategoryService materialcategoryservice;

	@PostMapping(value = "/getMaterialType")
	public ResponseEntity<Object> getMaterialType(@RequestBody Map<String, Object> inputMap) throws Exception {
		Integer nsiteInteger = (Integer) inputMap.get("nsitecode");
		return materialcategoryservice.getMaterialType(nsiteInteger);
	}

	@PostMapping(value = "/getMaterialCategory")
	public ResponseEntity<Object> getMaterialCategory(@RequestBody Map<String, Object> inputMap) throws Exception {
		
		Integer nsiteInteger = (Integer) inputMap.get("nsitecode");
		
		return materialcategoryservice.getMaterialCategory(nsiteInteger);
	}

	@PostMapping(value = "/createMaterialCategory")
	public ResponseEntity<Object> createMaterialCategory(@RequestBody Map<String, Object> inputMap) throws Exception {
		return materialcategoryservice.createMaterialCategory(inputMap);
	}

	@PostMapping(value = "/updateMaterialCategory")
	public ResponseEntity<Object> updateMaterialCategory(@RequestBody Map<String, Object> inputMap) throws Exception {
		final ObjectMapper objmapper = new ObjectMapper();
		final MaterialCategory materialCategory = objmapper.convertValue(inputMap.get("materialcategory"),MaterialCategory.class);
		
		return materialcategoryservice.updateMaterialCategory(materialCategory);
	}
		
	@PostMapping(value = "/deleteMaterialCategory")
	public ResponseEntity<Object> deleteMaterialCategory(@RequestBody Map<String, Object> inputMap) throws Exception {
		final ObjectMapper objmapper = new ObjectMapper();
		final MaterialCategory materialCategory = objmapper.convertValue(inputMap.get("materialcategory"),MaterialCategory.class);
		final LScfttransaction objsilentaudit = objmapper.convertValue(inputMap.get("objsilentaudit"),LScfttransaction.class);
		return materialcategoryservice.deleteMaterialCategory(materialCategory,objsilentaudit);
	}
}
