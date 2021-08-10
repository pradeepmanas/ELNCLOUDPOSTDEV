package com.agaram.eln.primary.controller.methodsetup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.agaram.eln.primary.service.methodsetup.AppMasterService;

/**
 * This controller is used to dispatch the input request to its relevant method to access
 * the AppMaster Service methods.
 * @author ATE153
 * @version 1.0.0
 * @since   18- Apr- 2020
 */
@RestController
public class AppMasterController {
	
	@Autowired
	AppMasterService appMasterService;
	
	/**
	 * This method is used to retrieve list of all AppMasters.
	 * @return list of appmaster entities.
	 */
	@PostMapping(value = "/getAppMaster")
	public ResponseEntity<Object> getAppMaster() {	
		 return appMasterService.getAppMaster();
	}

}
