package com.agaram.eln.primary.controller.instrumentsetup;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.agaram.eln.primary.model.instrumentsetup.InstrumentCategory;
import com.agaram.eln.primary.service.instrumentsetup.InstCategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * This controller is used to dispatch the input request to its relevant method to access
 * the InstCategory Service methods.
 * @author ATE153
 * @version 1.0.0
 * @since   11- Nov- 2019
 */
@RestController
public class InstCategoryController {
	
	@Autowired
	InstCategoryService categoryService;
	
	 /**
     * This method is used to retrieve list of active instrument categories (status=1)
     * @return list of active instrument categories.
     */
    @PostMapping(value = "/getInstCategory")
    public ResponseEntity<Object> getInstCategory()throws Exception {
        return  categoryService.getInstCategory();
    }
 
    /**
     * This method is used to add new instrument category.
     * @param mapObject [Map] object with "instCategory" -  holding details of all fields in 'InstrumentCategory' Pojo,
     * 				"saveAuditTrail" and "modulePage" as keys.
     *  @param request [HttpServletRequest] Request object
     * @return response entity with newly added instrument category object
     */
    @PostMapping(value = "/createInstCategory")
    public ResponseEntity<Object> createInstrumentCategory(final HttpServletRequest request, @Valid @RequestBody Map<String, Object> mapObject)throws Exception
    {
       final ObjectMapper mapper = new ObjectMapper();		
       final InstrumentCategory category = mapper.convertValue(mapObject.get("instCategory"), InstrumentCategory.class);
       
//       final Boolean saveAuditTrail = mapper.convertValue(mapObject.get("saveAuditTrail"), Boolean.class);
       
       final Boolean saveAuditTrail = false;
       
       //final Page page = mapper.convertValue(mapObject.get("modulePage"), Page.class);
	  
       return  categoryService.createInstCategory(category, saveAuditTrail, request);
    }

    /**
     * This method is used to update the selected 'instrument category' details based on provided 
     * primary key id and details of the  updated fields.
   	 * @param mapObject [Map] object with "instCategory" -  holding details of all fields in 'InstrumentCategory' Pojo,
     * 				"saveAuditTrail", "modulePage" and "comments" as keys to update.
     *  @param request [HttpServletRequest] Request object
     * @return response entity with updated 'instrument category' object
     */
    @PostMapping(value = "/updateInstCategory")
    public ResponseEntity<Object> updateInstCategory(final HttpServletRequest request, @Valid @RequestBody Map<String, Object> mapObject)throws Exception{
	 	  final ObjectMapper mapper = new ObjectMapper();
		  final InstrumentCategory category = mapper.convertValue(mapObject.get("instCategory"), InstrumentCategory.class);
		  final Boolean saveAuditTrail = mapper.convertValue(mapObject.get("saveAuditTrail"), Boolean.class);
		 
		  final String comments = mapper.convertValue(mapObject.get("comments"), String.class);			
		  
		  return categoryService.updateInstCategory(category, saveAuditTrail, comments,  request);
    }   
    
    /**
     * This method is used to remove the selected instrument category from user view by updating status to '-1' 
     *  @param mapObject [Map] object with "instcatkey" -  primary key of 'InstrumentCategory' to delete,
     * 				"saveAuditTrail","modulePage", "comments" and "doneByUserKey" as keys.
     *  @param request [HttpServletRequest] Request object
     * @return response entity with deleted status entity.
     */
    @PostMapping(value = "/updateInstCategoryStatus")
    public ResponseEntity<Object> deleteInstCategory( final HttpServletRequest request, @Valid @RequestBody Map<String, Object> mapObject) throws Exception
    {
	
//		  final Boolean saveAuditTrail = (Boolean)mapObject.get("saveAuditTrail");
    		final Boolean saveAuditTrail = false;
		  
		  String strUserKey = (String) mapObject.get("doneByUserKey");
		  
		  int userKey = Integer.parseInt(strUserKey);
		  
		  return categoryService.deleteInstCategory((Integer) mapObject.get("instcatkey"), saveAuditTrail, 
				   (String)mapObject.get("comments"), userKey, request);
    }
}


