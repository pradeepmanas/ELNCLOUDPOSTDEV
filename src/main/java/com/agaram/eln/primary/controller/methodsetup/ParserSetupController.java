package com.agaram.eln.primary.controller.methodsetup;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.agaram.eln.primary.service.methodsetup.ParserSetupService;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This controller is used to dispatch the input request to its relevant method to access
 * the ParserSetup Service methods.
 * @author ATE153
 * @version 1.0.0
 * @since   21- Apr- 2020	
 */
@RestController
public class ParserSetupController {
	
	@Autowired
	ParserSetupService parserService;
	
	/**
	 * This method is used to retrieve blockwise data  which is provided as input for ParserSetup
	 * for the specified method.
	 * @param mapObject [Map] object holding 'methodKey' for which the parser techniques are to be 
	 * created
	 * @return response object with list of extracted blockwise data.
	 */
	@PostMapping(value = "/getParserData")
	public ResponseEntity<Object> getParserData(@Valid @RequestBody Map<String, Object> mapObject)
	{    	
		//final int methodKey = (Integer) mapObject.get("methodKey");
        Map<String, Object> obj = (Map<String, Object>) mapObject.get("inputData");
    	   	
		final int methodKey = (Integer) obj.get("methodKey");
		return parserService.getParserData(methodKey, false, null);
	}
	
	/**
	 * This method is used to fetch the ParserMethod entities relating to SubParserTechnique.
	 * @return response object with list of ParserMethod entities
	 */
	@PostMapping(value = "/getSubParserMethod")
	public ResponseEntity<Object> getSubParserMethod(){    	
		return parserService.getSubParserMethod();
	}

	/**
	 * This method is used to save Parser techniques relating for the specified 'Method'.
	 * @param request [HttpServletRequest] Request object
	 * @param mapObject [Map] object holding details of list of entities(ParserBlock,ParserField, ParserTechnique,
	 * SubParserField and SubParserTechnique] to be saved and other details relating to audit trail
	 * @return response of updated 'Method' object
	 */
	@PostMapping(value = "/createParserSetup")
	public ResponseEntity<Object> saveParserSetup(final HttpServletRequest request, 
			@Valid @RequestBody Map<String, Object> mapObject)	{		
		return parserService.saveParserData(request, mapObject);
	}
	
	/**
	 * This method is used to fetch all ParserTechnique details available for the specified 'Method'.
	 * @param request [HttpServletRequest] Request object
	 * @param mapObject [Map] object holding 'methodKey' for which the parser techniques are to be 
	 * fetched
	 * @return response object with list of parser techniques
	 */
	@PostMapping(value = "/getParserFieldTechniqueListByMethodKey")
	public ResponseEntity<Object> getParserFieldTechniqueListByMethodKey(final HttpServletRequest request, 
			@Valid @RequestBody Map<String, Object> mapObject){
		final ObjectMapper mapper = new ObjectMapper();		 
		//final int methodKey = mapper.convertValue(mapObject.get("methodKey"), Integer.class);
        Map<String, Object> obj = (Map<String, Object>) mapObject.get("inputData");
    	            	
    	
		final int methodKey = (Integer) obj.get("methodKey");
		 
		return parserService.getParserFieldTechniqueList(methodKey);
	}
}
