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

import com.agaram.eln.primary.model.material.MaterialCategory;
import com.agaram.eln.primary.service.material.MaterialCategoryService;

@RestController
@RequestMapping(value = "/materialcategory", method = RequestMethod.POST)
public class MaterialCategoryController {

	@Autowired
	private MaterialCategoryService materialcategoryservice;

	@PostMapping(value = "/getMaterialType")
	public ResponseEntity<Object> getMaterialType(@RequestBody Map<String, Object> inputMap) throws Exception {
		return materialcategoryservice.getMaterialType(inputMap);
	}

	@PostMapping(value = "/getMaterialCategory")
	public ResponseEntity<Object> getMaterialCategory(@RequestBody Map<String, Object> inputMap) throws Exception {
		return materialcategoryservice.getMaterialCategory(inputMap);
	}

	@PostMapping(value = "/createMaterialCategory")
	public ResponseEntity<Object> createMaterialCategory(@RequestBody Map<String, Object> inputMap) throws Exception {
		return materialcategoryservice.createMaterialCategory(inputMap);
	}

	@PostMapping(value = "/getActiveMaterialCategoryById")
	public ResponseEntity<Object> getActiveMaterialCategoryById(@RequestBody Map<String, Object> inputMap)throws Exception {
		
		final int nmaterialcatcode = (Integer) inputMap.get("nmaterialcatcode");
		
		return materialcategoryservice.getActiveMaterialCategoryById(nmaterialcatcode);
	}
	
	@PostMapping(value = "/deleteMaterialCategory")
	public ResponseEntity<Object> deleteMaterialCategory(@RequestBody Map<String, Object> inputMap) throws Exception {
		final ObjectMapper objmapper = new ObjectMapper();
		final MaterialCategory materialCategory = objmapper.convertValue(inputMap.get("materialcategory"),MaterialCategory.class);
		return materialcategoryservice.deleteMaterialCategory(materialCategory);
	}
}
