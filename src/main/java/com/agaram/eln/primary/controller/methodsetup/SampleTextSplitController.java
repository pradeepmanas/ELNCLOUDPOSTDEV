package com.agaram.eln.primary.controller.methodsetup;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.agaram.eln.primary.service.methodsetup.SampleTextSplitService;

/**
 * This controller is used to dispatch the input request to its relevant method to access
 * the SampleTextSplit Service methods.
 * @author ATE153
 * @version 1.0.0
 * @since   14- Feb- 2020	
 */
@RestController
public class SampleTextSplitController {
	
	@Autowired
	SampleTextSplitService textSplitService;
	
	/**
	 * This method is used to retrieve list of active SampleTextSplit entities for the specified 'Method' Object
	 * @param mapObject [Map] Object with 'methodKey' object as a key
	 * @return response object with list of active SampleTextSplit entities
	 */
	@PostMapping(value = "/getSampleTextSplitByMethod")
	public ResponseEntity<Object> getSampleTextSplitByMethod(@Valid @RequestBody Map<String, Object> mapObject)
	{    	
		//final int methodKey = (Integer) mapObject.get("methodKey");
		Map<String, Object> obj = (Map<String, Object>) mapObject.get("inputData");
    	  	
		final int methodKey = (Integer) obj.get("methodKey");
		return textSplitService.getSampleTextSplitByMethod(methodKey);
	}

}
