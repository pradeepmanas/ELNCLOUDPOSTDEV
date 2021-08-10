package com.agaram.eln.primary.controller.methodsetup;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.agaram.eln.primary.model.methodsetup.CustomField;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.service.methodsetup.CustomFieldService;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This controller is used to dispatch the input request to its relevant method to access
 * the CustomField Service methods.
 * @author ATE153
 * @version 1.0.0
 * @since   18- Apr- 2020
 */
@RestController
public class CustomFieldController {
	
	@Autowired
	CustomFieldService customFieldService;
	
	/**
	 * This method is used to retrieve list of active custom fields for the specified site.
	 * @param mapObject [Map] Object with 'Site' object as a key
	 * @return response entity with retrieved list of custom fields.
	 */
	@PostMapping(value = "/getCustomField")
	public ResponseEntity<Object> getCustomFieldByMethod(@Valid @RequestBody Map<String, Object> mapObject) {	
		 final ObjectMapper mapper = new ObjectMapper();		
		 final LSSiteMaster site = mapper.convertValue(mapObject.get("site"), LSSiteMaster.class);
		 return customFieldService.getCustomFieldBySite(site);
	}
	
	/**
	   * This method is used to add new CustomField for the specified Method in a Site.
	   * @param request [HttpServletRequest] Request object
	   * @param mapObject [Map] object with keys 'customfield'- [Method] Entity ,'saveAuditTrail' and  'modulePage'.
	   * @return response entity of newly added CustomField entity
	   */
	  @PostMapping(value = "/createCustomField")
	  public ResponseEntity<Object> createCustomField(final HttpServletRequest request, @Valid @RequestBody Map<String, Object> mapObject) {
		  final ObjectMapper mapper = new ObjectMapper();		
		  final CustomField customField = mapper.convertValue(mapObject.get("customfield"), CustomField.class);
		  final Boolean saveAuditTrail = mapper.convertValue(mapObject.get("saveAuditTrail"), Boolean.class);
//		  final Page page = mapper.convertValue(mapObject.get("modulePage"), Page.class);
		  final LSSiteMaster site = mapper.convertValue(mapObject.get("site"), LSSiteMaster.class);
		  
		  return customFieldService.createCustomField(customField, site, saveAuditTrail, request);
	  }
	  
	  /**
	   * This method is used to update selected CustomField's detail of the specified Method in a site.
	   * @param request [HttpServletRequest] Request object
	   * @param mapObject [Map] object to update
	   * @return response entity of updated CustomField entity
	   */
	  @PostMapping(value = "/updateCustomField")
	  public ResponseEntity<Object> updateCustomField(final HttpServletRequest request, @Valid @RequestBody Map<String, Object> mapObject) {
		  final ObjectMapper mapper = new ObjectMapper();	
		  final CustomField customField = mapper.convertValue(mapObject.get("customfield"), CustomField.class);
		  final Boolean saveAuditTrail = mapper.convertValue(mapObject.get("saveAuditTrail"), Boolean.class);
		 // final Page page = mapper.convertValue(mapObject.get("modulePage"), Page.class);
		  final LSSiteMaster site = mapper.convertValue(mapObject.get("site"), LSSiteMaster.class);
		  final int doneByUserKey = (Integer) mapObject.get("doneByUserKey");
		  final String comments = mapper.convertValue(mapObject.get("comments"), String.class);
		  
		  return customFieldService.updateCustomField(customField, site, comments, saveAuditTrail, request, doneByUserKey);
	  }
	  
	  /**
	   * This method is used to delete the CustomField details of the specified method in a site.
	   * @param request [HttpServletRequest] Request object
	   * @param mapObject [Map] Object with the selected customfieldkey -to delete, saveAuditTrail, modulePage,
	   * comments and doneByUserKey.
	   * @return Response Entity relevant to deleted entity 
	   */
	  @PostMapping(value = "/updateCustomFieldStatus")
	  public ResponseEntity<Object> deleteCustomField(final HttpServletRequest request, @Valid @RequestBody Map<String, Object> mapObject) {
		  final ObjectMapper mapper = new ObjectMapper();	
		 
		  final int customFieldKey = mapper.convertValue(mapObject.get("customfieldkey"), Integer.class);
		  final Boolean saveAuditTrail = mapper.convertValue(mapObject.get("saveAuditTrail"), Boolean.class);
		  //final Page page = mapper.convertValue(mapObject.get("modulePage"), Page.class);
		  final LSSiteMaster site = mapper.convertValue(mapObject.get("site"), LSSiteMaster.class);
		  final int doneByUserKey = (Integer) mapObject.get("doneByUserKey");
		  final String comments = mapper.convertValue(mapObject.get("comments"), String.class);
		  
		  return customFieldService.deleteCustomField(customFieldKey, site, comments, doneByUserKey,
				  saveAuditTrail, request);
	  }
}

