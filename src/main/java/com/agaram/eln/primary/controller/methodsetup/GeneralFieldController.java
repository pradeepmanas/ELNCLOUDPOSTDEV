package com.agaram.eln.primary.controller.methodsetup;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.agaram.eln.primary.model.methodsetup.GeneralField;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.service.methodsetup.GeneralFieldService;
import com.fasterxml.jackson.databind.ObjectMapper;
/**
 * This controller is used to dispatch the input request to its relevant method to access
 * the GeneralField Service methods.
 * @author ATE153
 * @version 1.0.0
 * @since   18- Apr- 2020
 */
@RestController
public class GeneralFieldController {
	
	@Autowired
	GeneralFieldService generalFieldService;
	
	/**
	 * This method is used to retrieve list of active general fields for the specified site.
	 * @param mapObject [Map] Object with 'Site' object as a key
	 * @return response entity with retrieved list of general fields.
	 */
	@PostMapping(value = "/getGeneralField")
	public ResponseEntity<Object> getGeneralFieldBySite(@Valid @RequestBody Map<String, Object> mapObject)throws Exception {	
		 final ObjectMapper mapper = new ObjectMapper();		
		 final LSSiteMaster site = mapper.convertValue(mapObject.get("site"), LSSiteMaster.class);
		 return generalFieldService.getGeneralFieldBySite(site);
	}
	
	/**
	   * This method is used to add new GeneralField for the specified site.
	   * @param request [HttpServletRequest] Request object
	   * @param mapObject [Map] object with keys 'generalfield'- [GeneralField] Entity ,'saveAuditTrail' and  'modulePage'.
	   * @return response of Newly added GeneralField entity
	   */
	  @PostMapping(value = "/createGeneralField")
	  public ResponseEntity<Object> createGeneralField(final HttpServletRequest request, @Valid @RequestBody Map<String, Object> mapObject)throws Exception {
		  final ObjectMapper mapper = new ObjectMapper();		
		  final GeneralField generalField = mapper.convertValue(mapObject.get("generalfield"), GeneralField.class);
		  final Boolean saveAuditTrail = mapper.convertValue(mapObject.get("saveAuditTrail"), Boolean.class);
		  
		  return generalFieldService.createGeneralField(generalField, saveAuditTrail, request);
	  }
	  
	  /**
	   * This method is used to update selected GeneralField's detail for the specified site.
	   * @param request [HttpServletRequest] Request object
	   * @param mapObject [Map] object to update
	   * @return response entity of updated GeneralField entity
	   */
	  @PostMapping(value = "/updateGeneralField")
	  public ResponseEntity<Object> updateGeneralField(final HttpServletRequest request, @Valid @RequestBody Map<String, Object> mapObject)throws Exception {
		  final ObjectMapper mapper = new ObjectMapper();	
		  final GeneralField generalField = mapper.convertValue(mapObject.get("generalfield"), GeneralField.class);
		  final Boolean saveAuditTrail = mapper.convertValue(mapObject.get("saveAuditTrail"), Boolean.class);
		  final int doneByUserKey = (Integer) mapObject.get("doneByUserKey");
		  final String comments = mapper.convertValue(mapObject.get("comments"), String.class);
		  
		  return generalFieldService.updateGeneralField(generalField,comments, saveAuditTrail, request, doneByUserKey);
	  }
	  
	  /**
	   * This method is used to delete the Site's GeneralField details.
	   * @param request [HttpServletRequest] Request object
	   * @param mapObject [Map] Object with the selected generalfieldkey -to delete, saveAuditTrail, modulePage,
	   * comments and doneByUserKey.
	   * @return Response Entity relevant to delete general entity
	   */
	  @PostMapping(value = "/updateGeneralFieldStatus")
	  public ResponseEntity<Object> deleteGeneralField(final HttpServletRequest request, @Valid @RequestBody Map<String, Object> mapObject)throws Exception {
		  final ObjectMapper mapper = new ObjectMapper();	
		 
		  final int generalFieldKey = mapper.convertValue(mapObject.get("generalfieldkey"), Integer.class);
		  final Boolean saveAuditTrail = mapper.convertValue(mapObject.get("saveAuditTrail"), Boolean.class);
		  final LSSiteMaster site = mapper.convertValue(mapObject.get("site"), LSSiteMaster.class);
		  final int doneByUserKey = (Integer) mapObject.get("doneByUserKey");
		  final String comments = mapper.convertValue(mapObject.get("comments"), String.class);
		  
		  return generalFieldService.deleteGeneralField(generalFieldKey, site, comments, doneByUserKey,
				  saveAuditTrail, request);
	  }
}
