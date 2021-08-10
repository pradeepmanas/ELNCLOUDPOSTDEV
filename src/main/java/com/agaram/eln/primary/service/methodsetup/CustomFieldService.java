package com.agaram.eln.primary.service.methodsetup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.builder.Diff;
import org.apache.commons.lang3.builder.DiffResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.agaram.eln.primary.model.methodsetup.ControlType;
import com.agaram.eln.primary.model.methodsetup.CustomField;
import com.agaram.eln.primary.model.methodsetup.CustomFields;
import com.agaram.eln.primary.model.methodsetup.Method;
//import com.agaram.lleln.cfrpart11.cfrtransaction.CfrTransactionService;
//import com.agaram.lleln.jaxb.ReadWriteXML;
//import com.agaram.lleln.page.Page;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.repository.methodsetup.ControlTypeRepository;
import com.agaram.eln.primary.repository.methodsetup.CustomFieldRepository;
import com.agaram.eln.primary.repository.methodsetup.MethodRepository;
import com.agaram.eln.primary.repository.usermanagement.LSuserMasterRepository;
//import com.agaram.lleln.util.EnumerationInfo;

/**
 * This Service class is used to access the CustomFieldRepository to fetch details
 * from the 'customfield' table through CustomField Entity relevant to the input request.
 * @author ATE153
 * @version 1.0.0
 * @since   18- Apr- 2020
 */
@Service
public class CustomFieldService {
	
	@Autowired
	CustomFieldRepository customFieldRepo;
	
	@Autowired
	ControlTypeRepository controlTypeRepo;
	
	@Autowired
	LSuserMasterRepository usersRepo;
	
//	@Autowired
//	CfrTransactionService cfrTransService;
//	
//	@Autowired
//	ReadWriteXML readWriteXML;
	
	@Autowired
	MethodRepository methodRepo;
	
	/**
	 * This method is used to retrieve list of active custom fields based on specified site.
	 * @param site [Site] site object for which custom fields are to be fetched
	 * @return list of active custom fields available for the site
	 */
	@Transactional
	public ResponseEntity<Object> getCustomFieldBySite(final LSSiteMaster site){
		final List<CustomField> customFieldList = new ArrayList<CustomField>();
				//customFieldRepo.getCustomFieldbySite(site.getSitecode(), new Sort(Sort.Direction.DESC, "customfieldkey"));
		
		return new ResponseEntity<>(customFieldList, HttpStatus.OK);
	}
	
	/**
	 * This method is used to retrieve list of active custom fields based on Method.
	 * @param method [Method] Object for which custom fields are to be fetched.
	 * @return list of active custom fields associated for the Method
	 */
	@Transactional
	public ResponseEntity<Object> getCustomFieldByMethod(final Method method){
		final List<CustomField> customFieldList = customFieldRepo.findByMethodAndStatus(method, 1);		
		return new ResponseEntity<>(customFieldList, HttpStatus.OK);
	}
	
	/**
	 * This method is used to add new CustomField object for the specified method.
	 * Need to check for duplicate entry of customfield with for the same method, controlkey and same customfieldname
	 * before saving into database. 
	 * @param customField [CustomField] object holding details of all fields of CustomField entity
	 * @param site [Site] site object for which custom fields are to be fetched
	 * @param saveAuditTrial [boolean] 'true' -to record audit trial, 'false' - not to record audit trial
     * @param page [Page] entity relating to 'CustomField'
     * @param request [HttpServletRequest] Request object to ip address of remote client
     * @return Response of newly added CustomField entity
	 */
	@Transactional
	public ResponseEntity<Object> createCustomField(final CustomField customField, final LSSiteMaster site,
		   final boolean saveAuditTrial, final HttpServletRequest request)
	{ 
	   final ControlType controlType = controlTypeRepo.findOne(customField.getControltype().getControltypekey());
	   final Optional<Method> methodByKey = methodRepo.findByMethodkeyAndStatus(customField.getMethod().getMethodkey(), 1);
		
	   //Checking for Duplicate CustomField with same controlkey, method and same customfieldname for the specified site
	   final Optional<CustomField> customFieldByProperty = customFieldRepo
 				 .findByControltypeAndCustomfieldnameAndMethodAndStatus(controlType, customField.getCustomfieldname(), methodByKey.get(), 1);
	      
	   final LSuserMaster createdUser = getCreatedUserByKey(customField.getCreatedby().getUsercode());
    	if(customFieldByProperty.isPresent())
    	{
    		//Conflict = 409 - Duplicate entry
    		if (saveAuditTrial == true)
			{						
				final String comments = "Create Failed for duplicate field name - "+ customField.getCustomfieldname();
								
//				cfrTransService.saveCfrTransaction(page, EnumerationInfo.CFRActionType.SYSTEM.getActionType(),
//						"Create", comments, site, "",
//						createdUser, request.getRemoteAddr());
			}
  			return new ResponseEntity<>("Duplicate Entry - " + customField.getCustomfieldname(), 
  					 HttpStatus.CONFLICT);
    	}
    	else
    	{    		
    		customField.setCreatedby(createdUser);
    		customField.setControltype(controlType);
    		customField.setMethod(methodByKey.get());
    			
    		final CustomField savedCustomField = customFieldRepo.save(customField);
    		
    		if (saveAuditTrial)
			{   			
				final Map<String, String> fieldMap = new HashMap<String, String>();
				fieldMap.put("createdby", "loginid");
				fieldMap.put("method", "methodname");
				fieldMap.put("controltype", "controltypename");
				
//				final String xmlData = readWriteXML.saveXML(savedCustomField, CustomField.class, null, "individualpojo", fieldMap);
//								
//				final String actionType = EnumerationInfo.CFRActionType.SYSTEM.getActionType();
//				cfrTransService.saveCfrTransaction(page, actionType, "Create", "", 
//						site, xmlData, createdUser, request.getRemoteAddr());
			}
      		
    		return new ResponseEntity<>(savedCustomField , HttpStatus.OK);
    	} 
   }  
	
	/**
	 * This method is used to update the selected CustomField entity.
	 * Need to check for duplicate entry of CustomField with same controlkey and 
	 * same customfieldname for the specified site method before saving into database. 
	 * @param customField [CustomField] object holding details of all fields of MethodDelimiter entity
	 * @param site [Site] object for which the audittrail recording is to be done
	 * @param comments [String] comments given for audit trail recording
	 * @param saveAuditTrail [boolean] 'true' -to record audit trial, 'false' - not to record audit trial
     * @param page [Page] entity relating to 'Delimiters'
     * @param request [HttpServletRequest] Request object to ip address of remote client
     * @param doneByUserKey [int] primary key of logged-in user who done this task
     * @return Response of updated custom field entity
	 */
   @Transactional
   public ResponseEntity<Object> updateCustomField(final CustomField customField, final LSSiteMaster site,
		   final String comments, final boolean saveAuditTrail,  
		   final HttpServletRequest request, final int doneByUserKey)
   {	   
	   final Optional<CustomField> customFieldByKey = customFieldRepo.findByCustomfieldkeyAndStatus(customField.getCustomfieldkey(), 1);
	   final Optional<Method> methodByKey = methodRepo.findByMethodkeyAndStatus(customField.getMethod().getMethodkey(), 1);
	   
	   final LSuserMaster createdUser = getCreatedUserByKey(doneByUserKey);
	   
	   if(customFieldByKey.isPresent()) {		   

		   final ControlType controlType = controlTypeRepo.findOne(customField.getControltype().getControltypekey());
		   	   
		   //Checking for Duplicate CustomField with same controlkey, method and same customfieldname for the specified site
		   final Optional<CustomField> customFieldByProperty = customFieldRepo
	 				 .findByControltypeAndCustomfieldnameAndMethodAndStatus(controlType, customField.getCustomfieldname(), methodByKey.get(), 1);
		 		  
	  		
		  	if(customFieldByProperty.isPresent())
			{ 		
		  		 customField.setControltype(controlType);
				 customField.setMethod(methodByKey.get());
			     //already available	
			     if (customFieldByProperty.get().getCustomfieldkey().equals(customField.getCustomfieldkey()))
			     {   		    			
			    		//copy of object for using 'Diffable' to compare objects
			    		final CustomField customFieldBeforeSave = new CustomField(customFieldByProperty.get());
		    			
			    		final CustomField savedCustomField = customFieldRepo.save(customField);
			    					    			
		    			if (saveAuditTrail)
		    			{
			    			final String xmlData = convertCustomFieldObjectToXML(customFieldBeforeSave, savedCustomField);
			    					    			
//			    			cfrTransService.saveCfrTransaction(page, EnumerationInfo.CFRActionType.USER.getActionType(), 
//			    					"Edit", comments, site, xmlData, createdUser, request.getRemoteAddr());		    			
			    		}
			    			
			       		return new ResponseEntity<>(savedCustomField, HttpStatus.OK);     		
		    		}
		    		else
		    		{ 	
		    			//Conflict =409 - Duplicate entry
		    			if (saveAuditTrail == true)
		    			{						
		    				final String sysComments = "Update Failed for duplicate field name - "+ customField.getCustomfieldname();
		    				
//		    				cfrTransService.saveCfrTransaction(page, EnumerationInfo.CFRActionType.SYSTEM.getActionType(),
//		    						"Create", sysComments, site, "",createdUser, request.getRemoteAddr());
		    			}
		    			
		    			return new ResponseEntity<>("Duplicate Entry - " +customField.getCustomfieldname(), 
		    					 HttpStatus.CONFLICT);      			
		    		}
		    	}
		    	else
		    	{			    		
		    		//copy of object for using 'Diffable' to compare objects
	    			final CustomField customFieldBeforeSave = new CustomField(customFieldByKey.get());
	    			
		    		final CustomField savedCustomField = customFieldRepo.save(customField);
		    		
		    		if (saveAuditTrail)
	    			{
		    			final String xmlData = convertCustomFieldObjectToXML(customFieldBeforeSave, savedCustomField);
		    			
//		    			cfrTransService.saveCfrTransaction(page, EnumerationInfo.CFRActionType.USER.getActionType(),
//								"Edit", comments, site, xmlData, createdUser, request.getRemoteAddr());
	    			}
		    		
		    		return new ResponseEntity<>(savedCustomField , HttpStatus.OK);			    		
		    	}			  
		   }
		   else
		   {
//			   //Invalid methoddelimiterkey		   
//			   if (saveAuditTrail) {				
//					cfrTransService.saveCfrTransaction(page, EnumerationInfo.CFRActionType.SYSTEM.getActionType(),
//							"Edit", "Update Failed - Custom Field Not Found", site, "", createdUser, request.getRemoteAddr());
//	   		    }			
				return new ResponseEntity<>("Update Failed - Custom Field Not Found", HttpStatus.NOT_FOUND);
		   }
   }   
   
   /**
	 * This method is used to delete the selected CustomField entity with its primary key.
	 * @param customFieldKey [int] primary key of CustomField object to delete
	 * @param site [Site] object for which the audittrail recording is to be done
	 * @param comments [String] comments given for audit trail recording
	 * @param doneByUserKey [int] primary key of logged-in user who done this task
	 * @param saveAuditTrial [boolean] 'true' -to record audit trial, 'false' - not to record audit trial
	 * @param page [Page] entity relating to 'Delimiters'
	 * @param request [HttpServletRequest] Request object to ip address of remote client
	 * @return Response of deleted customfield entity
	 */
  @Transactional
  public ResponseEntity<Object> deleteCustomField(final int customFieldKey, 
		   final LSSiteMaster site, final String comments, final int doneByUserKey, 
		   final boolean saveAuditTrial, final HttpServletRequest request)
  {	   
	   final Optional<CustomField> customFieldbyKey = customFieldRepo.findByCustomfieldkeyAndStatus(customFieldKey, 1);
	   final LSuserMaster createdUser = getCreatedUserByKey(doneByUserKey);
	   
	   if(customFieldbyKey.isPresent()) {

		   final CustomField customField = customFieldbyKey.get();
		   //if (child not present)
		  	   //copy of object for using 'Diffable' to compare objects
			   final CustomField customFieldBeforeSave = new CustomField(customField); 
	
			   //Its not associated in transaction
			   customField.setStatus(-1);
			   final CustomField savedCustomField = customFieldRepo.save(customField);   
					   
			    if (saveAuditTrial)
				{				    	
			    	final String xmlData = convertCustomFieldObjectToXML(customFieldBeforeSave, savedCustomField);
	   			
//			    	cfrTransService.saveCfrTransaction(page, 
//							EnumerationInfo.CFRActionType.USER.getActionType(), "Delete", comments, 
//							site, xmlData, createdUser, request.getRemoteAddr());
				}
			   
			   return new ResponseEntity<>(savedCustomField, HttpStatus.OK);  
		   //}
		   //else {
			   //Associated with Child
			 
		   //}
	   }
	   else
	   {
		   //Invalid MethodDelimiterkey
//		   if (saveAuditTrial) {				
//				cfrTransService.saveCfrTransaction(page, EnumerationInfo.CFRActionType.SYSTEM.getActionType(),
//						"Delete", "Delete Failed - CustomField Not Found", site, "", 
//						createdUser, request.getRemoteAddr());
// 		    }			
			return new ResponseEntity<>("Delete Failed - CustomField Not Found", HttpStatus.NOT_FOUND);
	   }
  }  
	  
	/**
	 * This method is used to retrieve the 'Users' details based on the
	 * input primary key- userKey.
	 * @param userKey [int] primary key of Users entity
	 * @return Users Object relating to the userKey
	 */
	private LSuserMaster getCreatedUserByKey(final int userKey)
	{
		final LSuserMaster createdBy =  usersRepo.findOne(userKey);		
		
		final LSuserMaster createdUser = new LSuserMaster();
		createdUser.setUsername(createdBy.getUsername());
		createdUser.setUsercode(createdBy.getUsercode());
		
		return createdUser;
	}
	/**
	 * This method is used to convert the CustomField entity to xml with the difference in object
	 * before and after update for recording in Audit Trial
	 * @param customFieldBeforeSave [CustomField] Object before update
	 * @param savedCustomField [CustomField] object after update
	 * @return string formatted xml data
	 */
   public String convertCustomFieldObjectToXML(
		   final CustomField customFieldBeforeSave, final CustomField savedCustomField)
   {
	  	final Map<Integer, Map<String, Object>> dataModified = new HashMap<Integer, Map<String, Object>>();
		final Map<String, Object> diffObject = new HashMap<String, Object>();    			
		
		final DiffResult diffResult = customFieldBeforeSave.diff(savedCustomField);        			
//		for(Diff<?> d: diffResult.getDiffs()) {		    					
//			diffObject.put(d.getFieldName(), d.getKey()+" -> "+d.getValue());
//		}
		
		dataModified.put(savedCustomField.getCustomfieldkey(), diffObject);
		
		final Map<String, String> fieldMap = new HashMap<String, String>();
		fieldMap.put("createdby", "loginid");
		fieldMap.put("method", "methodname");
		fieldMap.put("controltype", "controltypename");
			
//		return readWriteXML.saveXML(new CustomField(savedCustomField), CustomField.class, dataModified, "individualpojo", fieldMap);
		
		return "";
		     	
   }
   
   /**
	 * This method is used to convert the list of CustomField entities to xml with the difference in object
	 * before and after update for recording in Audit Trial
	 * @param listBeforeSave list of CustomField details before saving to DB
	 * @param savedList list of CustomField details after saving to DB
	 * @return string formatted xml data
	 */
	public String convertCustomFieldListToXML(final List<CustomField> listBeforeSave, 
			final List<CustomField> savedList)
	{			
		try {			
			final CustomFields afterSaveList = new CustomFields(); 
			afterSaveList.setCustomfields(savedList);	
			Map<Integer, Map<String, Object>> dataModified = new HashMap<Integer, Map<String, Object>>();
			for (CustomField objAfterSave : savedList)
			{					
				for (CustomField objBeforeSave : listBeforeSave)
				{
					if (objBeforeSave.getCustomfieldkey().equals(objAfterSave.getCustomfieldkey()))
					{				
						final DiffResult diffResult = objBeforeSave.diff(objAfterSave);
							
						Map<String, Object> diffObject = new HashMap<String, Object>();
						
//						for(Diff<?> d: diffResult.getDiffs()) {
//								diffObject.put(d.getFieldName(), d.getKey()+" -> "+d.getValue());
//						}
						dataModified.put(objBeforeSave.getCustomfieldkey(), diffObject);
												
					}			
				}
			}
			
			final Map<String, String> fieldMap = new HashMap<String, String>();		
			fieldMap.put("createdby", "loginid");
			fieldMap.put("method", "methodname");
			fieldMap.put("controltype", "controltypename");
			
//			final ReadWriteXML readWriteXML =  new ReadWriteXML();
//			return readWriteXML.saveXML(afterSaveList, CustomFields.class, dataModified, "listpojo", fieldMap);	
			return "";
		
		} catch ( SecurityException | IllegalArgumentException e) {
			// TODO Auto-generated catch block
			return null;
		}
	}
	
}
