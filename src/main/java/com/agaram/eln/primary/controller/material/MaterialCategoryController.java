package com.agaram.eln.primary.controller.material;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.agaram.eln.primary.model.material.MaterialCategory;
import com.agaram.eln.primary.service.material.MaterialCategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;



@RestController
@RequestMapping(value = "/materialcategory", method = RequestMethod.POST)
public class MaterialCategoryController {
	
	@Autowired	
	 private MaterialCategoryService materialcategoryservice;
	
	
	@PostMapping(value = "/getMaterialType")
	public ResponseEntity<Object> getMaterialType(@RequestBody Map<String, Object> inputMap) throws Exception {
//		ObjectMapper objMapper = new ObjectMapper();
//		UserInfo userInfo = objMapper.convertValue(inputMap.get("userinfo"), new TypeReference<UserInfo>() {});
//		requestContext.setUserInfo(userInfo);
		return materialcategoryservice.getMaterialType(inputMap);
	}
	
	@PostMapping(value = "/getMaterialCategory")
	public ResponseEntity<Object> getMaterialCategory(@RequestBody Map<String, Object> inputMap) throws Exception {
//		ObjectMapper objMapper = new ObjectMapper();
//		UserInfo userInfo = objMapper.convertValue(inputMap.get("userinfo"), new TypeReference<UserInfo>() {});
//		requestContext.setUserInfo(userInfo);
		return materialcategoryservice.getMaterialCategory(inputMap);
	}
	
	@PostMapping(value = "/createMaterialCategory")
	public ResponseEntity<Object> createMaterialCategory(@RequestBody Map<String, Object> inputMap) throws Exception {
				return materialcategoryservice.createMaterialCategory(inputMap);

	}

}

