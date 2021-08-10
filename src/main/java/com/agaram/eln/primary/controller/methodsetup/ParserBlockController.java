package com.agaram.eln.primary.controller.methodsetup;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.agaram.eln.primary.service.methodsetup.ParserBlockService;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This controller is used to dispatch the input request to its relevant method to access
 * the ParserBlock Service methods.
 * @author ATE153
 * @version 1.0.0
 * @since   19- Apr- 2020
 */
@RestController
public class ParserBlockController {
	
	@Autowired
	ParserBlockService parserBlockService;

	/**
	 * This method is used to retrieve list of active PaserBlock entities for the specified 'Method'.
	 * @param mapObject [Map] Object with 'methodKey' object as a key
	 * @return retrieve list of active PaserBlock entities
	 */
	@PostMapping(value = "/getParserBlockByMethodKey")
	public ResponseEntity<Object> getParserBlockByMethodKey(@RequestBody Map<String, Object> mapObject){
		 final ObjectMapper mapper = new ObjectMapper();		 
		 final int methodKey = mapper.convertValue(mapObject.get("methodKey"), Integer.class);
		
		 return parserBlockService.getParserBlockByMethodKey(methodKey);
	}
	
}
