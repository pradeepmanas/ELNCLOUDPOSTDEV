package com.agaram.eln.primary.controller.methodsetup;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.agaram.eln.primary.model.methodsetup.Delimiter;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.service.methodsetup.DelimiterService;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This controller is used to dispatch the input request to its relevant method to access
 * the Delimters Service methods.
 * @author ATE153
 * @version 1.0.0
 * @since   07- Feb- 2020
 */
@RestController
public class DelimiterController {

	@Autowired
	DelimiterService delimitersService;	
	
	/**
	  * This method is used to retrieve list of active delimiters.
	  * @param mapObject [Map] object with keys 'sortOrder' to sort list 
	  * @return list of active delimiters.
	  */
	@PostMapping(value = "/getDelimiters")
	public ResponseEntity<Object> getActiveDelimiters(@RequestBody Map<String, Object> mapObject)throws Exception {	
//		final ObjectMapper mapper = new ObjectMapper();	
//		final String sortBy = mapper.convertValue(mapObject.get("sortOrder"), String.class); 
//		return delimitersService.getActiveDelimiters(sortBy);
		final ObjectMapper mapper = new ObjectMapper();	
        Map<String, Object> obj = (Map<String, Object>) mapObject.get("inputData");
  	 if(obj == null)
  	 {
  		 
  		 final String sortBy = mapper.convertValue(mapObject.get("sortOrder"), String.class);
  		 return delimitersService.getActiveDelimiters(sortBy);
  	 }
  	 else
  	 {
  		 final String sortBy = (String) obj.get("sortOrder");
  			//final String sortBy = mapper.convertValue(mapObject.get("sortOrder"), String.class); 
  			return delimitersService.getActiveDelimiters(sortBy);	 
  	 }
	}
	
	/**
	   * This method is used to add new delimiter.
	   * @param request [HttpServletRequest] Request object
	   * @param mapObject [Map] object with keys 'delimiters'- [Delimiters] Entity with 'delimitername'(m), 
	   * 		'actualdelimiter'(m), createdby(m),'saveAuditTrail' and  'modulePage'.
	   * @return response of Newly added delimiter entity
	   */
	  @PostMapping(value = "/createDelimiters")
	  public ResponseEntity<Object> createDelimiters(final HttpServletRequest request, @Valid @RequestBody Map<String, Object> mapObject)throws Exception {
		  final ObjectMapper mapper = new ObjectMapper();		
		  final Delimiter delimiters = mapper.convertValue(mapObject.get("delimiters"), Delimiter.class);
		  final Boolean saveAuditTrail = mapper.convertValue(mapObject.get("saveAuditTrail"), Boolean.class);
		  
		  final LSSiteMaster site = mapper.convertValue(mapObject.get("site"), LSSiteMaster.class);
		  
		  return delimitersService
				  .createDelimiters(delimiters, site, saveAuditTrail, request);
	  }
	  
	  /**
	   * This method is used to update specific Delimiter Object.
	   * @param request  [HttpServletRequest] Request object
	   * @param mapObject [Map] Object holding details to update
	   * @return response entity with updated object
	   */
	  @PostMapping(value = "/updateDelimiters")
	  public ResponseEntity<Object> updateDelimiters(final HttpServletRequest request, @Valid @RequestBody Map<String, Object> mapObject)throws Exception {
		  final ObjectMapper mapper = new ObjectMapper();	
		  final Delimiter delimiters = mapper.convertValue(mapObject.get("delimiters"), Delimiter.class);
		  final Boolean saveAuditTrail = mapper.convertValue(mapObject.get("saveAuditTrail"), Boolean.class);
		  
		  final LSSiteMaster site = mapper.convertValue(mapObject.get("site"), LSSiteMaster.class);
//		  final int doneByUserKey = (Integer) mapObject.get("doneByUserKey");
		  final String comments = mapper.convertValue(mapObject.get("comments"), String.class);
		  
		  String strUserKey = (String) mapObject.get("doneByUserKey");
		  
		  final int doneByUserKey = Integer.parseInt(strUserKey);
		  
		  return delimitersService.updateDelimiters(delimiters, site, comments, 
				  saveAuditTrail,  request, doneByUserKey);
	  }
	  
	  /**
	   * This method is used to delete specified delimiter.
	   * @param request   [HttpServletRequest] Request object
	   * @param mapObject [Map] Object holding details to delete specified delimiter
	   * @return response of deleted entity with its updated status.
	   */
	  @PostMapping(value = "/updateDelimitersStatus")
	  public ResponseEntity<Object> deleteDelimiters(final HttpServletRequest request, @Valid @RequestBody Map<String, Object> mapObject)throws Exception {
		  final ObjectMapper mapper = new ObjectMapper();	
		 
		  final int delimiterKey = mapper.convertValue(mapObject.get("delimiterkey"), Integer.class);
		  final Boolean saveAuditTrail = mapper.convertValue(mapObject.get("saveAuditTrail"), Boolean.class);
		  final LSSiteMaster site = mapper.convertValue(mapObject.get("site"), LSSiteMaster.class);
//		  final int doneByUserKey = (Integer) mapObject.get("doneByUserKey");
		  final String comments = mapper.convertValue(mapObject.get("comments"), String.class);
		  
		  String strUserKey = (String) mapObject.get("doneByUserKey");
		  
		  final int doneByUserKey = Integer.parseInt(strUserKey);
		  
		  final Delimiter otherdetails = mapper.convertValue(mapObject.get("otherdetails"), Delimiter.class);
	  		  
		  return delimitersService.deleteDelimters(delimiterKey, site, comments, doneByUserKey, saveAuditTrail, request,otherdetails);
	  }
}
