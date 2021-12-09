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
import com.agaram.eln.primary.model.instrumentsetup.InstrumentMaster;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.service.instrumentsetup.InstMasterService;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This controller is used to dispatch the input request to its relevant method to access
 * the InstrumentMaster Service methods.
 * @author ATE153
 * @version 1.0.0
 * @since   11- Nov- 2019
 */
@RestController
public class InstMasterController {
	
	 @Autowired
	 InstMasterService masterService;
	 
	 /**
     * This method is used to retrieve list of active instrument masters (status=1)
     *  Eg: Input Format
	 * { "site": {
            "sitekey": 1,
            "sitename": "Chennai",
            "status": 1
        }}
	  * @param siteObj [Map] Object with 'Site' object as a key
     * @return list of active instruments.
     */
    @SuppressWarnings("unchecked")
	@PostMapping(value = "/getInstMaster")
    public ResponseEntity<Object> getInstMaster(@Valid @RequestBody Map<String, LSSiteMaster> siteObj) {
    	@SuppressWarnings("unchecked")
		Map<String, Object> obj = (Map<String, Object>) siteObj.get("inputData");
    	Map<String, Object> objsite = null ;
    	if(obj == null)
    	{
    		objsite = (Map<String, Object>) siteObj.get("site");
    		//return  masterService.getInstMaster(siteObj.get("site"));
    		LSSiteMaster site = new LSSiteMaster();
    		int nSitecode = (int) objsite.get("sitecode");
    		site.setSitecode(nSitecode);
            
    		return  masterService.getInstMaster(site);
    	}
    	else
    	{
    		objsite = (Map<String, Object>) obj.get("site");
    	
    	LSSiteMaster site = new LSSiteMaster();
    	String sSitecode = (String) objsite.get("sitecode");
		
		int sitecode = Integer.parseInt(sSitecode);
		site.setSitecode(sitecode);
        //return  masterService.getInstMaster(siteObj.get("site"));
		return  masterService.getInstMaster(site);
    	}
    }
 
    /**
     * This method is used to add new instrument master.
     * @param mapObject [Map] object with keys 'instMaster'- [InstrumentMaster] Entity,
	 *       	 'saveAuditTrail' and  'modulePage'.
	 * @param request [HttpServletRequest] Request object
     * @return response entity with newly added instrument master object
     */
    @PostMapping(value = "/createInstMaster")    
    public ResponseEntity<Object> createInstrumentMaster(final HttpServletRequest request, @Valid @RequestBody Map<String, Object> mapObject)
    {    		
	      final ObjectMapper mapper = new ObjectMapper();		
		  final InstrumentMaster master = mapper.convertValue(mapObject.get("instMaster"), InstrumentMaster.class);
		  final Boolean saveAuditTrail = mapper.convertValue(mapObject.get("saveAuditTrail"), Boolean.class);
		  //final Page page = mapper.convertValue(mapObject.get("modulePage"), Page.class);
		  
		  return  masterService.createInstMaster(master, saveAuditTrail, request);
   }

    /**
     * This method is used to update the selected 'instrument master' details based on provided 
     * primary key id and details of the  updated fields.
     * @param mapObject [Map] object with keys "instMaster"- [InstrumentMaster] Entity with  details to update,
     * 		'saveAuditTrail' , 'modulePage' and 'comments' - for audit trail recording
     * @param request [HttpServletRequest] Request object
     * @return response entity with updated 'instrument master' object
     */
    @PostMapping(value = "/updateInstMaster")
    public ResponseEntity<Object> updateInstMaster(final HttpServletRequest request, @Valid @RequestBody Map<String, Object> mapObject) {
    	
    	  final ObjectMapper mapper = new ObjectMapper();
    	 // mapper.registerModule(new JavaTimeModule());
    	  final InstrumentMaster master = mapper.convertValue(mapObject.get("instMaster"), InstrumentMaster.class);
		  final Boolean saveAuditTrail = mapper.convertValue(mapObject.get("saveAuditTrail"), Boolean.class);
//		  final Page page = mapper.convertValue(mapObject.get("modulePage"), Page.class);
		  final String comments = mapper.convertValue(mapObject.get("comments"), String.class);	
		  
          return  masterService.updateInstMaster(master, saveAuditTrail, comments, request);
    }   
    
    /**
     * This method is used to delete the selected instrument along with its associated insttype. 
     * @param mapObject [Map] Object with the selected "instmastkey" -to delete, 'saveAuditTrail', 'modulePage',
	 * 'comments' and 'doneByUserKey'.
	 * @param request [HttpServletRequest] Request object
     * @return Response Entity relevant to delete response 
     */
    @PostMapping(value = "/updateInstMasterStatus")
    public ResponseEntity<Object> deleteInstMaster(final HttpServletRequest request, @Valid @RequestBody Map<String, Object> mapObject) {
    	
//    	  final ObjectMapper mapper = new ObjectMapper();	
		  final Boolean saveAuditTrail = (Boolean)mapObject.get("saveAuditTrail");
		 // final Page page = mapper.convertValue(mapObject.get("modulePage"), Page.class);
		  
		  String strUserKey = (String) mapObject.get("doneByUserKey");
		  
		  final int doneByUserKey = Integer.parseInt(strUserKey);
		  
		  return masterService.deleteInstMaster((Integer) mapObject.get("instmastkey"), saveAuditTrail, 
				   (String)mapObject.get("comments"),doneByUserKey,  request);
    }
    
    /**
     * This method is used to get list of instruments available in the selected category of the site.
     * @param mapObject [Map] object holding details of site and instrument category
     * @return response object with list of instruments
     */
    @PostMapping(value = "/getInstListByCategoryAndSite")
    public ResponseEntity<Object> getInstListByCategoryAndSite(@Valid @RequestBody Map<String, Object> mapObject) {
    
    	  final ObjectMapper mapper = new ObjectMapper();	
    	  final LSSiteMaster site = mapper.convertValue(mapObject.get("site"), LSSiteMaster.class);
    	  final InstrumentCategory instCategory = mapper.convertValue(mapObject.get("instCategory"), InstrumentCategory.class);
		  
		  return masterService.getInstListByCategoryAndSite(instCategory, site);
    }
}


