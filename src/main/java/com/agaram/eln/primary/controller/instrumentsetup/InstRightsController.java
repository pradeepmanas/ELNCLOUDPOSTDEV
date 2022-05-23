package com.agaram.eln.primary.controller.instrumentsetup;


import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.service.instrumentsetup.InstRightsService;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This controller is used to dispatch the input request to its relevant method to access
 * the InstRights Service methods.
 * @author ATE153
 * @version 1.0.0
 * @since   11- Nov- 2019
 */
@RestController
public class InstRightsController {
		
	@Autowired
	InstRightsService rightsService;

	 /**
	  * This method is used to assign, reassign and remove instrument rights associated for the user.
	  * Also fetches the list of instruments associated for the next selected user.
	  * @param request [HttpServletRequest] Request object
	  * @param rightsObject [Map] map object holding 'site', 'roleKey', 'saveAuditTrail', 'comments', 
	  * 			'modulePage', 'userSiteKey', 'instToSave', 'getListUserSiteKey' and 'doneByUserKey' as keys.
	  * @return response object with saved list of instrument rights for the selected user
	  */
	@PostMapping(value = "/saveInstRights")
	public ResponseEntity<Object> saveInstRights(final HttpServletRequest request, @Valid @RequestBody Map<String, Object> rightsObject)throws Exception{
		return rightsService.saveInstRights(rightsObject, request);
	}
	
	 /**
	  * This method is used to fetch the list of instruments associated for the selected user.
	  * With initialGet = true fetches list of users available with the logged-in site and also the
	  * instruments associated for the first user in the user list.
	  * @param userObj [Map] map object holding 'site', 'initialGet' and 'getListUserSiteKey' as keys.
	  * @return response object with list of instrument rights for the selected user
	  */
	@PostMapping(value = "/getInstRightsByUser")
	public ResponseEntity<Object> getInstRightsByUser(@Valid @RequestBody Map<String, Object> userObj)throws Exception {
		
	   final ObjectMapper mapper = new ObjectMapper();	
	   final LSSiteMaster site = mapper.convertValue(userObj.get("site"), LSSiteMaster.class);
	   final Boolean initialGet = mapper.convertValue(userObj.get("initialGet"), Boolean.class);
	   return rightsService.getInstRightsByUserSite(Integer.parseInt(userObj.get("getListUserSiteKey").toString()), 
			   site, initialGet);
	}
	
}
