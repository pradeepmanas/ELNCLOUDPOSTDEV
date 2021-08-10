package com.agaram.eln.primary.controller.methodsetup;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.agaram.eln.primary.service.methodsetup.SampleLineSplitService;


/**
 * This controller is used to dispatch the input request to its relevant method to access
 * the SampleLineSplit Service methods.
 * @author ATE153
 * @version 1.0.0
 * @since   18- Feb- 2020
 */
@RestController
public class SampleLineSplitController {
	
	@Autowired
	SampleLineSplitService lineSplitService;
	
	/**
	 * This method is used to retrieve list of active SampleLineSplit entities for the specified 'Method' Object
	 * @param mapObject [Map] Object with 'methodKey' object as a key
	 * @return response object with list of active SampleLineSplit entities
	 */
	@PostMapping(value = "/getSampleLineSplitByMethod")
	public ResponseEntity<Object> getSampleLineSplitByMethod(@Valid @RequestBody Map<String, Object> mapObject)
	{    	
		final int methodKey = (Integer) mapObject.get("methodKey");
		return lineSplitService.getSampleLineSplitByMethod(methodKey);
	}

}

