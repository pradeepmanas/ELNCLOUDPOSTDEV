package com.agaram.eln.primary.service.methodsetup;

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

import com.agaram.eln.primary.model.methodsetup.AppMaster;
import com.agaram.eln.primary.model.methodsetup.GeneralField;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.repository.methodsetup.AppMasterRepository;
import com.agaram.eln.primary.repository.methodsetup.GeneralFieldRepository;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.repository.usermanagement.LSuserMasterRepository;

import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.repository.usermanagement.LSSiteMasterRepository;

/**
 * This Service class is used to access the GeneralFieldRepository to fetch details
 * from the 'generalfield' table through GeneralField Entity relevant to the input request.
 * @author ATE153
 * @version 1.0.0
 * @since   18- Apr- 2020
 */
@Service
public class GeneralFieldService {

	@Autowired
	GeneralFieldRepository generalFieldRepo;
	
	@Autowired
	AppMasterRepository appMasterRepo;
	
	@Autowired
	LSuserMasterRepository usersRepo;
	

	/**
	 * This method is used to retrieve list of GeneralField entiti
	 * @param site [Site] site object for which general fields are to be fetched
	 * @return list of active general fields based on Site sorted in desc order of its primary key
	 */
	@Transactional
	public ResponseEntity<Object> getGeneralFieldBySite(final LSSiteMaster site){
		final List<GeneralField> generalFieldList = generalFieldRepo.findBySiteAndStatus(site, 1,new Sort(Sort.Direction.DESC, "generalfieldkey"));
		
		return new ResponseEntity<>(generalFieldList, HttpStatus.OK);
	}
	
	/**
	 * This method is used to add new GeneralField object.
	 * Need to check for duplicate entry of generalfield with same appmasterkey and same generalfieldname
	 * before saving into database. 
	 * @param generalField [GeneralField] object holding details of all fields of MethodDelimiter entity
	 * @param saveAuditTrial [boolean] 'true' -to record audit trial, 'false' - not to record audit trial
     * @param page [Page] entity relating to 'GeneralField'
     * @param request [HttpServletRequest] Request object to ip address of remote client
     * @return Response of newly added GeneralField entity
	 */
	@Transactional
	public ResponseEntity<Object> createGeneralField(final GeneralField generalField, //final Site site,
		   final boolean saveAuditTrial, final HttpServletRequest request)
	{ 
	   final AppMaster appMaster = appMasterRepo.findOne(generalField.getAppmaster().getAppmasterkey());
	   
	   //Checking for Duplicate GeneralField with same appmasterkey and same generalfieldname for the specified site
	   final Optional<GeneralField> generalFieldByProperty = generalFieldRepo
 				 .findByAppmasterAndGeneralfieldnameAndSiteAndStatus(appMaster, generalField.getGeneralfieldname(), generalField.getSite(), 1);
	      
	   final LSuserMaster createdUser = getCreatedUserByKey(generalField.getCreatedby().getUsercode());
    	if(generalFieldByProperty.isPresent())
    	{
    		//Conflict = 409 - Duplicate entry
    		if (saveAuditTrial == true)
			{						
				final String comments = "Create Failed for duplicate field name - "+ generalField.getGeneralfieldname();
								
//				cfrTransService.saveCfrTransaction(page, EnumerationInfo.CFRActionType.SYSTEM.getActionType(),
//						"Create", comments, generalField.getSite(), "",
//						createdUser, request.getRemoteAddr());
			}
  			return new ResponseEntity<>("Duplicate Entry - " + generalField.getGeneralfieldname(), 
  					 HttpStatus.CONFLICT);
    	}
    	else
    	{    		
    		generalField.setCreatedby(createdUser);
    		generalField.setAppmaster(appMaster);
    			
    		final GeneralField savedGeneralField = generalFieldRepo.save(generalField);
    		
    		if (saveAuditTrial)
			{   			
				final Map<String, String> fieldMap = new HashMap<String, String>();
				fieldMap.put("createdby", "loginid");
				fieldMap.put("appmaster", "appmastername");
				fieldMap.put("site", "sitename");
				
//				final String xmlData = readWriteXML.saveXML(savedGeneralField, GeneralField.class, null, "individualpojo", fieldMap);
//								
//				final String actionType = EnumerationInfo.CFRActionType.SYSTEM.getActionType();
//				cfrTransService.saveCfrTransaction(page, actionType, "Create", "", 
//						generalField.getSite(), xmlData, createdUser, request.getRemoteAddr());
			}
      		
    		return new ResponseEntity<>(savedGeneralField , HttpStatus.OK);
    	} 
   }  
	
	/**
	 * This method is used to update the selected GeneralField entity.
	 * Need to check for duplicate entry of GeneralField with same appmasterkey and 
	 * same generalfieldname for the specified sitebefore saving into database. 
	 * @param generalField [MethodDelimiter] object holding details of all fields of MethodDelimiter entity	
	 * @param comments [String] comments given for audit trail recording
	 * @param saveAuditTrail [boolean] 'true' -to record audit trial, 'false' - not to record audit trial
     * @param page [Page] entity relating to 'Delimiters'
     * @param request [HttpServletRequest] Request object to ip address of remote client
     * @param doneByUserKey [int] primary key of logged-in user who done this task
     * @return Response of updated GeneralField entity
	 */
   @Transactional
   public ResponseEntity<Object> updateGeneralField(final GeneralField generalField, //final Site site,
		   final String comments, final boolean saveAuditTrail, 
		   final HttpServletRequest request, final int doneByUserKey)
   {	   
	   final Optional<GeneralField> generalFieldByKey = generalFieldRepo.findByGeneralfieldkeyAndStatus(generalField.getGeneralfieldkey(), 1);
	   
	   final LSuserMaster createdUser = getCreatedUserByKey(doneByUserKey);
	   
	   if(generalFieldByKey.isPresent()) {		   

		   final AppMaster appMaster = appMasterRepo.findOne(generalField.getAppmaster().getAppmasterkey());
		   	   
		   //Checking for Duplicate GeneralField with same appmasterkey and same generalfieldname for the specified site
		   final Optional<GeneralField> generalFieldByProperty = generalFieldRepo
	 				 .findByAppmasterAndGeneralfieldnameAndSiteAndStatus(appMaster, generalField.getGeneralfieldname(), generalField.getSite(), 1);
		 		
		   generalField.setAppmaster(appMaster);
		  	if(generalFieldByProperty.isPresent())
			{		  		    		
			    //methodDelimiter already available	
			    if (generalFieldByProperty.get().getGeneralfieldkey().equals(generalField.getGeneralfieldkey()))
			    	{   		    			
			    		//copy of object for using 'Diffable' to compare objects
			    		final GeneralField generalFieldBeforeSave = new GeneralField(generalFieldByProperty.get());
		    			
			    		final GeneralField savedGeneralField = generalFieldRepo.save(generalField);
			    					    			
		    			if (saveAuditTrail)
		    			{
			    			final String xmlData = convertGeneralFieldObjectToXML(generalFieldBeforeSave, savedGeneralField);
//			    					    			
//			    			cfrTransService.saveCfrTransaction(page, EnumerationInfo.CFRActionType.USER.getActionType(), 
//			    					"Edit", comments, generalField.getSite(), xmlData, createdUser, request.getRemoteAddr());		    			
			    		}
			    			
			       		return new ResponseEntity<>(savedGeneralField, HttpStatus.OK);     		
		    		}
		    		else
		    		{ 	
		    			//Conflict =409 - Duplicate entry
		    			if (saveAuditTrail == true)
		    			{						
		    				final String sysComments = "Update Failed for duplicate field name - "+ generalField.getGeneralfieldname();
//		    				
//		    				cfrTransService.saveCfrTransaction(page, EnumerationInfo.CFRActionType.SYSTEM.getActionType(),
//		    						"Create", sysComments, generalField.getSite(), "",createdUser, request.getRemoteAddr());
		    			}
		    			
		    			return new ResponseEntity<>("Duplicate Entry - " +generalField.getGeneralfieldname(), 
		    					 HttpStatus.CONFLICT);      			
		    		}
		    	}
		    	else
		    	{			    		
		    		//copy of object for using 'Diffable' to compare objects
	    			final GeneralField generalFieldBeforeSave = new GeneralField(generalFieldByKey.get());
	    			
		    		final GeneralField savedGeneralField = generalFieldRepo.save(generalField);
		    		
		    		if (saveAuditTrail)
	    			{
		    			final String xmlData = convertGeneralFieldObjectToXML(generalFieldBeforeSave, savedGeneralField);
		    			
//		    			cfrTransService.saveCfrTransaction(page, EnumerationInfo.CFRActionType.USER.getActionType(),
//								"Edit", comments,generalField.getSite(), xmlData, createdUser, request.getRemoteAddr());
	    			}
		    		
		    		return new ResponseEntity<>(savedGeneralField , HttpStatus.OK);			    		
		    	}			  
		   }
		   else
		   {
			   //Invalid methoddelimiterkey		   
			   if (saveAuditTrail) {				
//					cfrTransService.saveCfrTransaction(page, EnumerationInfo.CFRActionType.SYSTEM.getActionType(),
//							"Edit", "Update Failed - General Field Not Found", generalField.getSite(), "", createdUser, request.getRemoteAddr());
	   		    }			
				return new ResponseEntity<>("Update Failed - General Field Not Found", HttpStatus.NOT_FOUND);
		   }
   }   
   
   /**
	 * This method is used to delete the selected GeneralField entity with its primary key.
	 * @param generalFieldKey [int] primary key of GeneralField object to delete
	 * @param site [Site] object for which the audittrail recording is to be done
	 * @param comments [String] comments given for audit trail recording
	 * @param doneByUserKey [int] primary key of logged-in user who done this task
	 * @param saveAuditTrial [boolean] 'true' -to record audit trial, 'false' - not to record audit trial
	 * @param page [Page] entity relating to 'Delimiters'
	 * @param request [HttpServletRequest] Request object to ip address of remote client
	 * @return Response of deleted GeneralField entity
	 */
  @Transactional
  public ResponseEntity<Object> deleteGeneralField(final int generalFieldKey, 
		   final LSSiteMaster site, final String comments, final int doneByUserKey, 
		   final boolean saveAuditTrial, final HttpServletRequest request)
  {	   
	   final Optional<GeneralField> generalFieldbyKey = generalFieldRepo.findByGeneralfieldkeyAndStatus(generalFieldKey, 1);
	   final LSuserMaster createdUser = getCreatedUserByKey(doneByUserKey);
	   
	   if(generalFieldbyKey.isPresent()) {

		   final GeneralField generalField = generalFieldbyKey.get();
		   //if (child not present)
		  	   //copy of object for using 'Diffable' to compare objects
			   final GeneralField generalFieldBeforeSave = new GeneralField(generalField); 
	
			   //Its not associated in transaction
			   generalField.setStatus(-1);
			   final GeneralField savedGeneralField = generalFieldRepo.save(generalField);   
					   
			    if (saveAuditTrial)
				{				    	
			    	final String xmlData = convertGeneralFieldObjectToXML(generalFieldBeforeSave, savedGeneralField);
	   			
//			    	cfrTransService.saveCfrTransaction(page, 
//							EnumerationInfo.CFRActionType.USER.getActionType(), "Delete", comments, 
//							site, xmlData, createdUser, request.getRemoteAddr());
				}
			   
			   return new ResponseEntity<>(savedGeneralField, HttpStatus.OK);  
		   //}
		   //else {
			   //Associated with Child
			 
		   //}
	   }
	   else
	   {
		   //Invalid MethodDelimiterkey
		   if (saveAuditTrial) {				
//				cfrTransService.saveCfrTransaction(page, EnumerationInfo.CFRActionType.SYSTEM.getActionType(),
//						"Delete", "Delete Failed - GeneralField Not Found", site, "", 
//						createdUser, request.getRemoteAddr());
 		    }			
			return new ResponseEntity<>("Delete Failed - GeneralField Not Found", HttpStatus.NOT_FOUND);
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
	 * This method is used to convert the GeneralField entity to xml with the difference in object
	 * before and after update for recording in Audit Trial
	 * @param generalFieldBeforeSave [GeneralField] Object before update
	 * @param savedGeneralField [GeneralField] object after update
	 * @return string formatted xml data
	 */
   public String convertGeneralFieldObjectToXML(
		   final GeneralField generalFieldBeforeSave, final GeneralField savedGeneralField)
   {
	  	final Map<Integer, Map<String, Object>> dataModified = new HashMap<Integer, Map<String, Object>>();
		final Map<String, Object> diffObject = new HashMap<String, Object>();    			
		
		final DiffResult diffResult = generalFieldBeforeSave.diff(savedGeneralField);        			
//		for(Diff<?> d: diffResult.getDiffs()) {		    					
//			diffObject.put(d.getFieldName(), d.getKey()+" -> "+d.getValue());
//		}
		
		dataModified.put(savedGeneralField.getGeneralfieldkey(), diffObject);
		
		final Map<String, String> fieldMap = new HashMap<String, String>();
		fieldMap.put("createdby", "loginid");
		fieldMap.put("appmaster", "appmastername");
		fieldMap.put("site", "sitename");
			
//		return readWriteXML.saveXML(new GeneralField(savedGeneralField), GeneralField.class, dataModified, "individualpojo", fieldMap);	
		return "";
		     	
   }	
}
