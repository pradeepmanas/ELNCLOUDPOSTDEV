package com.agaram.eln.primary.controller.methodsetup;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.agaram.eln.primary.model.methodsetup.MethodDelimiter;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.service.methodsetup.MethodDelimiterService;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This controller is used to dispatch the input request to its relevant method to access
 * the MethodDelimiter Service methods.
 * @author ATE153
 * @version 1.0.0
 * @since   19- Apr- 2020
 */
@RestController
public class MethodDelimiterController {
	
	@Autowired
	MethodDelimiterService methodDelimiterService;
	
	/**
	 * This method is used to retrieve list of active MethodDelimiter entities.
	 * @return response entity with retrieved list of Method delimiters.
	 */
	@PostMapping(value = "/getMethodDelimiter")
	public ResponseEntity<Object> getMethodDelimiter() {	
		 return methodDelimiterService.getMethodDelimiterList();
	}
	
	/**
	   * This method is used to add new MethodDelimiter.
	   * @param request [HttpServletRequest] Request object
	   * @param mapObject [Map] object with keys 'methoddelimiter'- [MethodDelimiter] Entity , 'saveAuditTrail' and  'modulePage'.
	   * @return response of Newly added MethodDelimiter entity
	   */
	  @PostMapping(value = "/createMethodDelimiter")
	  public ResponseEntity<Object> createMethodDelimiter(final HttpServletRequest request, @Valid @RequestBody Map<String, Object> mapObject) {
		  final ObjectMapper mapper = new ObjectMapper();		
		  final MethodDelimiter methodDelimiter = mapper.convertValue(mapObject.get("methoddelimiter"), MethodDelimiter.class);
		  final Boolean saveAuditTrail = mapper.convertValue(mapObject.get("saveAuditTrail"), Boolean.class);
		  final LSSiteMaster site = mapper.convertValue(mapObject.get("site"), LSSiteMaster.class);
		  
		  return methodDelimiterService
				  .createMethodDelimiter(methodDelimiter, site, saveAuditTrail, request);
	  }
	  
	  /**
	   * This method is used to update specific MethodDelimiter Object.
	   * @param request  [HttpServletRequest] Request object
	   * @param mapObject [Map] Object holding details to update
	   * @return response entity with updated object
	   */
	  @PostMapping(value = "/updateMethodDelimiter")
	  public ResponseEntity<Object> updateMethodDelimiter(final HttpServletRequest request, @Valid @RequestBody Map<String, Object> mapObject) {
		  final ObjectMapper mapper = new ObjectMapper();	
		  final MethodDelimiter methodDelimiter = mapper.convertValue(mapObject.get("methoddelimiter"), MethodDelimiter.class);
		  final Boolean saveAuditTrail = mapper.convertValue(mapObject.get("saveAuditTrail"), Boolean.class);
		  final LSSiteMaster site = mapper.convertValue(mapObject.get("site"), LSSiteMaster.class);
//		  final int doneByUserKey = (Integer) mapObject.get("doneByUserKey");
		  String strUserKey = (String) mapObject.get("doneByUserKey");
		  
		  final int doneByUserKey = Integer.parseInt(strUserKey);
		  final String comments = mapper.convertValue(mapObject.get("comments"), String.class);
		  
		  return methodDelimiterService.updateMethodDelimiter(methodDelimiter, site, comments, 
				  saveAuditTrail, request, doneByUserKey);
	  }
	  
	  /**
	   * This method is used to delete specified MethodDelimiter.
	   * @param request   [HttpServletRequest] Request object
	   * @param mapObject [Map] Object holding details to delete specified MethodDelimiter
	   * @return response of deleted entity with its updated status.
	   */
	  @PostMapping(value = "/updateMethodDelimiterStatus")
	  public ResponseEntity<Object> deleteMethodDelimiter(final HttpServletRequest request, @Valid @RequestBody Map<String, Object> mapObject) {
		  final ObjectMapper mapper = new ObjectMapper();	
		 
		  final int methodDelimiterKey = mapper.convertValue(mapObject.get("methoddelimiterkey"), Integer.class);
		  final Boolean saveAuditTrail = mapper.convertValue(mapObject.get("saveAuditTrail"), Boolean.class);
		  final LSSiteMaster site = mapper.convertValue(mapObject.get("site"), LSSiteMaster.class);
//		  final int doneByUserKey = (Integer) mapObject.get("doneByUserKey");
		  final String comments = mapper.convertValue(mapObject.get("comments"), String.class);
		  
		  String strUserKey = (String) mapObject.get("doneByUserKey");
		  
		  final int doneByUserKey = Integer.parseInt(strUserKey);
		  
		  return methodDelimiterService.deleteMethodDelimiter(methodDelimiterKey, site, comments, doneByUserKey, saveAuditTrail,  request);
	  }

}
