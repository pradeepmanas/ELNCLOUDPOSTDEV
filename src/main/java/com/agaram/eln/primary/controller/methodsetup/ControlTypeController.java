package com.agaram.eln.primary.controller.methodsetup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.agaram.eln.primary.service.methodsetup.ControlTypeService;

/**
 * This controller is used to dispatch the input request to its relevant method to access
 * the ControlType Service methods.
 * @author ATE153
 * @version 1.0.0
 * @since   18- Apr- 2020
 */
@RestController
public class ControlTypeController {
	
	@Autowired
	ControlTypeService controlTypeService;
	
	/**
	 * This method is used to retrieve list of all control types.
	 * @return list of control types entities.
	 */
	@PostMapping(value = "/getControlType")
	public ResponseEntity<Object> getControlType()throws Exception {	
		 return controlTypeService.getControlType();
	}

}
