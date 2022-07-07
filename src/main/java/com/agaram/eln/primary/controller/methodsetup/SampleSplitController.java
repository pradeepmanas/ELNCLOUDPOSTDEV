package com.agaram.eln.primary.controller.methodsetup;

import java.sql.Date;
import java.util.Map;


import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.methodsetup.Delimiter;
import com.agaram.eln.primary.model.methodsetup.SampleExtract;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.service.methodsetup.SampleSplitService;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This controller is used to dispatch the input request to its relevant method to access
 * the SampleSplit Service methods.
 * @author ATE153
 * @version 1.0.0
 * @since   14- Feb- 2020	
 */
@RestController
public class SampleSplitController {	
	
	@Autowired
	SampleSplitService sampleSplitService;
	
	/**
	 * This method is used to save SampleSplit techniques.
	 * @param request [HttpServletRequest] Request object
	 * @param mapObject [Map] object holding sample split techniques and other details relating to audit trail to save
	 * @return response of updated 'Method' object
	 */
	@PostMapping(value = "/createSampleSplit")
	public ResponseEntity<Object> saveSampleSplit(final HttpServletRequest request, 
			@Valid @RequestBody Map<String, Object> mapObject)throws Exception	{    		

		return sampleSplitService.saveSampleSplit(request, mapObject);
	}

}
