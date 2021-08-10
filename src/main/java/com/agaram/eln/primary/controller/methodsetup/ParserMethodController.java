package com.agaram.eln.primary.controller.methodsetup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.agaram.eln.primary.service.methodsetup.ParserMethodService;

/**
 * This controller is used to dispatch the input request to its relevant method to access
 * the ParserMethod Service methods.
 * @author ATE153
 * @version 1.0.0
 * @since   31- Mar- 2020
 */
@RestController
public class ParserMethodController {

	@Autowired
	ParserMethodService parserMethodService;	
	
	/**
	  * This method is used to retrieve list of ParserMethod entities.
	  * @return list of ParserMethod entities
	  */
	@PostMapping(value = "/getParserMethod")
	public ResponseEntity<Object> getParserMethod()	{
		return parserMethodService.getParserMethod();
	}
}
