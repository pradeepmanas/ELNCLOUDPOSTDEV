package com.agaram.eln.primary.controller.methodsetup;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.agaram.eln.primary.service.methodsetup.SubParserFieldService;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This controller is used to dispatch the input request to its relevant method to access
 * the SubParserField Service methods.
 * @author ATE153
 * @version 1.0.0
 * @since   19- Apr- 2020
 */
@RestController
public class SubParserFieldController {

	@Autowired
	SubParserFieldService subParserFieldService;
	
	/**
	 * This method is used to retrieve list of active SubParserField entities for the specified 'Method' Object
	 * @param mapObject [Map] Object with 'methodKey' object as a key
	 * @return response object with list of active SubParserField entities
	 */
	@PostMapping(value = "/getSubParserFieldByMethodKey")
	public ResponseEntity<Object> getSubParserFieldByMethodKey(@RequestBody Map<String, Object> mapObject){
		 final ObjectMapper mapper = new ObjectMapper();		 
		 final int methodKey = mapper.convertValue(mapObject.get("methodKey"), Integer.class);
		
		 return subParserFieldService.getSubParserFieldByMethodKey(methodKey);
	}
}
