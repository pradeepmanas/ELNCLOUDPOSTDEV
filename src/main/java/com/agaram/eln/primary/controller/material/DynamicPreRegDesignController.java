package com.agaram.eln.primary.controller.material;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.agaram.eln.primary.service.material.DynamicPreRegDesignService;

@RestController
@RequestMapping(value = "dynamicpreregdesign")
public class DynamicPreRegDesignController {

	@Autowired
	private DynamicPreRegDesignService dynamicPreRegDesignService;
	
	@PostMapping(path = "getComboValues")
	public ResponseEntity<Object> getComboValues(@RequestBody Map<String, Object> inputMap) throws Exception{
		
		return dynamicPreRegDesignService.getComboValues(inputMap);
		
	}
	
	@PostMapping(path = "getChildValues")
	public ResponseEntity<Object> getChildValues(@RequestBody Map<String, Object> inputMap) throws Exception{
		
		return dynamicPreRegDesignService.getChildValues(inputMap);
	}
}