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

import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.repository.usermanagement.LSuserMasterRepository;
import com.agaram.eln.primary.model.methodsetup.Delimiter;
import com.agaram.eln.primary.model.methodsetup.MethodDelimiter;
import com.agaram.eln.primary.model.methodsetup.ParserField;
import com.agaram.eln.primary.model.methodsetup.ParserMethod;
import com.agaram.eln.primary.model.methodsetup.SubParserTechnique;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.repository.methodsetup.DelimiterRepository;
import com.agaram.eln.primary.repository.methodsetup.MethodDelimiterRepository;
import com.agaram.eln.primary.repository.methodsetup.ParserFieldRepository;
import com.agaram.eln.primary.repository.methodsetup.ParserMethodRepository;
import com.agaram.eln.primary.repository.methodsetup.SubParserTechniqueRepository;
import com.agaram.eln.primary.repository.usermanagement.LSSiteMasterRepository;

/**
 * This Service class is used to access the MethodDelimiterRepository to fetch details
 * from the 'methoddelimiter' table through MethodDelimiter Entity relevant to the input request.
 * @author ATE153
 * @version 1.0.0
 * @since   19- Apr- 2020
 */
@Service
public class MethodDelimiterService {

	@Autowired
	MethodDelimiterRepository methodDelimiterRepo;
	
	@Autowired
	DelimiterRepository delimiterRepo;
	
	@Autowired
	ParserMethodRepository parserMethodRepo;	
	
	@Autowired
	LSuserMasterRepository usersRepo;
	
	@Autowired
	ParserFieldRepository parserFieldRepo;
	
	@Autowired
	SubParserTechniqueRepository subParserTechRepo;
	
	/**
	 * This method is used to retrieve list of active MethodDelimiter entities.
	 * @return response object  with retrieved list of active MethodDelimiter entities
	 */
	@Transactional
	public ResponseEntity<Object> getMethodDelimiterList(){
		final List<MethodDelimiter> delimiterList = methodDelimiterRepo.findByStatus(1,new Sort(Sort.Direction.DESC, "methoddelimiterkey"));
		
		return new ResponseEntity<>(delimiterList, HttpStatus.OK);
	}
	
	/**
	 * This method is used to add new MethodDelimiter object.
	 * Need to check for duplicate entry of delimiter for the specified ParserMethod before saving into database. 
	 * @param methodDelimiter [MethodDelimiter] object holding details of all fields of MethodDelimiter entity
	 *  @param site [Site] object for which the audittrail recording is to be done
	 * @param saveAuditTrial [boolean] 'true' -to record audit trial, 'false' - not to record audit trial
     * @param page [Page] entity relating to 'MethodDelimiter'
     * @param request [HttpServletRequest] Request object to ip address of remote client
     * @return Response of newly added MethodDelimiter entity
	 */
	@Transactional
	public ResponseEntity<Object> createMethodDelimiter(final MethodDelimiter methodDelimiter, final LSSiteMaster site,
		   final boolean saveAuditTrial, final HttpServletRequest request)
	{ 
	   final ParserMethod parserMethod = parserMethodRepo.findOne(methodDelimiter.getParsermethod().getParsermethodkey());
	   final Delimiter delimiter = delimiterRepo.findOne(methodDelimiter.getDelimiter().getDelimiterkey());
	   
	   //Checking for Duplicate MethodDelimiter with same parsermethod and same delimiter
	   final Optional<MethodDelimiter> methodDelimiterByDelimiter = methodDelimiterRepo
 				 .findByParsermethodAndDelimiterAndStatus(parserMethod, delimiter, 1);
	      
	   final LSuserMaster createdUser = getCreatedUserByKey(methodDelimiter.getCreatedby().getUsercode());
    	if(methodDelimiterByDelimiter.isPresent())
    	{
    		//Conflict = 409 - Duplicate entry
    		if (saveAuditTrial == true)
			{						
				final String comments = "Create Failed for duplicate entry - " + methodDelimiterByDelimiter.get().getDelimiter().getDelimitername()
	  					+" for " + methodDelimiterByDelimiter.get().getParsermethod().getParsermethodname();
				
//				cfrTransService.saveCfrTransaction(page, EnumerationInfo.CFRActionType.SYSTEM.getActionType(),
//						"Create", comments, site, "",
//						createdUser, request.getRemoteAddr());
			}
  			return new ResponseEntity<>("Duplicate Entry - " + methodDelimiterByDelimiter.get().getDelimiter().getDelimitername()
  					+" for " + methodDelimiterByDelimiter.get().getParsermethod().getParsermethodname(), 
  					 HttpStatus.CONFLICT);
    	}
    	else
    	{    		
    		methodDelimiter.setCreatedby(createdUser);
    		methodDelimiter.setParsermethod(parserMethod);
    		methodDelimiter.setDelimiter(delimiter);
    			
    		final MethodDelimiter savedMethodDelimiter = methodDelimiterRepo.save(methodDelimiter);
    		
    		if (saveAuditTrial)
			{   			
				final Map<String, String> fieldMap = new HashMap<String, String>();
				fieldMap.put("createdby", "loginid");
				fieldMap.put("parsermethod", "parsermethodname");
				fieldMap.put("delimiter", "delimitername");
				
//				final String xmlData = readWriteXML.saveXML(savedMethodDelimiter, MethodDelimiter.class, null, "individualpojo", fieldMap);
//								
//				final String actionType = EnumerationInfo.CFRActionType.SYSTEM.getActionType();
//				cfrTransService.saveCfrTransaction(page, actionType, "Create", "", 
//						site, xmlData, createdUser, request.getRemoteAddr());
			}
      		
    		return new ResponseEntity<>(savedMethodDelimiter , HttpStatus.OK);
    	} 
   }  
	
	/**
	 * This method is used to update the selected MethodDelimiter entity.
	 * Need to check for duplicate entry of delimiter for the specified ParserMethod before saving into database. 
	 * @param methodDelimiter [MethodDelimiter] object holding details of all fields of MethodDelimiter entity
	 * @param site [Site] object for which the audittrail recording is to be done
	 * @param comments [String] comments given for audit trail recording
	 * @param saveAuditTrail [boolean] 'true' -to record audit trial, 'false' - not to record audit trial
     * @param page [Page] entity relating to 'Delimiters'
     * @param request [HttpServletRequest] Request object to ip address of remote client
     * @param doneByUserKey [int] primary key of logged-in user who done this task
     * @return Response of updated MethodDelimiter entity
	 */
   @Transactional
   public ResponseEntity<Object> updateMethodDelimiter(final MethodDelimiter methodDelimiter, final LSSiteMaster site,
		   final String comments, final boolean saveAuditTrail,
		   final HttpServletRequest request, final int doneByUserKey)
   {	   
	   final Optional<MethodDelimiter> methodDelimiterByKey = methodDelimiterRepo.findByMethoddelimiterkeyAndStatus(methodDelimiter.getMethoddelimiterkey(), 1);
	   final LSuserMaster createdUser = getCreatedUserByKey(doneByUserKey);
	   
	   if(methodDelimiterByKey.isPresent()) {	
		   
		   final List<ParserField> parserFieldList = parserFieldRepo.findByMethoddelimiterAndStatus(methodDelimiterByKey.get(), 1);
		   final List<SubParserTechnique> subParserTechList = subParserTechRepo.findByMethoddelimiterAndStatus(methodDelimiterByKey.get(), 1);
		   
		   if (parserFieldList.isEmpty() && subParserTechList.isEmpty()) {
			   //Eligible to update as not associated with any child

				   final ParserMethod parserMethod = parserMethodRepo.findOne(methodDelimiter.getParsermethod().getParsermethodkey());
				   final Delimiter delimiter = delimiterRepo.findOne(methodDelimiter.getDelimiter().getDelimiterkey());
				   
				   //Checking for Duplicate MethodDelimiter with same parsermethod and same delimiter
				   final Optional<MethodDelimiter> methodDelimiterByDelimiter = methodDelimiterRepo
			 				 .findByParsermethodAndDelimiterAndStatus(parserMethod, delimiter, 1);
				  					   
				  	if(methodDelimiterByDelimiter.isPresent())
					{
				  		methodDelimiter.setParsermethod(parserMethod);
			    		methodDelimiter.setDelimiter(delimiter);
			    		
					    //methodDelimiter already available	
					    if (methodDelimiterByDelimiter.get().getMethoddelimiterkey().equals(methodDelimiter.getMethoddelimiterkey()))
					    	{   		    			
					    		//copy of object for using 'Diffable' to compare objects
					    		final MethodDelimiter delimitersBeforeSave = new MethodDelimiter(methodDelimiterByDelimiter.get());
				    			
					    		final MethodDelimiter savedMethodDelimiter = methodDelimiterRepo.save(methodDelimiter);
					    					    			
				    			if (saveAuditTrail)
				    			{
//					    			final String xmlData = convertMethodDelimiterObjectToXML(delimitersBeforeSave, savedMethodDelimiter);
//					    					    			
//					    			cfrTransService.saveCfrTransaction(page, EnumerationInfo.CFRActionType.USER.getActionType(), 
//					    					"Edit", comments, site, xmlData, createdUser, request.getRemoteAddr());		    			
					    		}
					    			
					       		return new ResponseEntity<>(savedMethodDelimiter, HttpStatus.OK);     		
				    		}
				    		else
				    		{ 	
				    			//Conflict =409 - Duplicate entry
				    			if (saveAuditTrail == true)
				    			{						
//				    				final String sysComments = "Update Failed for duplicate entry - " + methodDelimiterByDelimiter.get().getDelimiter().getDelimitername()
//				    	  					+" for " + methodDelimiterByDelimiter.get().getParsermethod().getParsermethodname();
//				    				
//				    				cfrTransService.saveCfrTransaction(page, EnumerationInfo.CFRActionType.SYSTEM.getActionType(),
//				    						"Create", sysComments, site, "",createdUser, request.getRemoteAddr());
				    			}
				    			
				    			return new ResponseEntity<>("Duplicate Entry - " + methodDelimiterByDelimiter.get().getDelimiter().getDelimitername()
				      					+" for " + methodDelimiterByDelimiter.get().getParsermethod().getParsermethodname(), HttpStatus.CONFLICT);      			
				    		}
				    	}
				    	else
				    	{			    		
				    		//copy of object for using 'Diffable' to compare objects
			    			final MethodDelimiter delimiterBeforeSave = new MethodDelimiter(methodDelimiterByKey.get());
			    			
				    		final MethodDelimiter savedMethodDelimiter = methodDelimiterRepo.save(methodDelimiter);
				    		
				    		if (saveAuditTrail)
			    			{
//				    			final String xmlData = convertMethodDelimiterObjectToXML(delimiterBeforeSave, savedMethodDelimiter);
//				    			
//				    			cfrTransService.saveCfrTransaction(page, EnumerationInfo.CFRActionType.USER.getActionType(),
//										"Edit", comments, site, xmlData, createdUser, request.getRemoteAddr());
			    			}
				    		
				    		return new ResponseEntity<>(savedMethodDelimiter , HttpStatus.OK);			    		
				    	}	
		   		}
		   		else {
		   		 //Not Eligible to update as associated with some child
		   			if (saveAuditTrail)
		   			{
					   final String sysComments = "Update Failed as methoddelimiter is associated with either ParserField /SubParserTechnique";
		   			
//						cfrTransService.saveCfrTransaction(page, EnumerationInfo.CFRActionType.SYSTEM.getActionType(),
//								"Edit", sysComments, 
//								site, "", createdUser, request.getRemoteAddr());
				   }
				   return new ResponseEntity<>(methodDelimiterByKey.get().getParsermethod().getParsermethodname() +"-" + methodDelimiterByKey.get().getDelimiter().getDelimitername() , HttpStatus.IM_USED);//status code - 226
		   		}
		   }
		   else
		   {
			   //Invalid methoddelimiterkey		   
//			   if (saveAuditTrail) {				
//					cfrTransService.saveCfrTransaction(page, EnumerationInfo.CFRActionType.SYSTEM.getActionType(),
//							"Edit", "Update Failed - MethodDelimiter Not Found", site, "", createdUser, request.getRemoteAddr());
//	   		    }			
				return new ResponseEntity<>("Update Failed - MethodDelimiter Not Found", HttpStatus.NOT_FOUND);
		   }
   }   
   
   /**
	 * This method is used to delete the selected MethodDelimiter entity with its primary key.N
	 * Need to validate whether it is not associated with ParserField or SubParserField entities.
	 * @param methodDelimiterKey [int] primary key of MethodDelimiter object to delete
	 * @param site [Site] object for which the audittrail recording is to be done
	 * @param comments [String] comments given for audit trail recording
	 * @param doneByUserKey [int] primary key of logged-in user who done this task
	 * @param saveAuditTrial [boolean] 'true' -to record audit trial, 'false' - not to record audit trial
	 * @param page [Page] entity relating to 'Delimiters'
	 * @param request [HttpServletRequest] Request object to ip address of remote client
	 * @return Response of deleted MethodDelimiter entity
	 */
  @Transactional
  public ResponseEntity<Object> deleteMethodDelimiter(final int methodDelimiterKey, 
		   final LSSiteMaster site, final String comments, final int doneByUserKey, 
		   final boolean saveAuditTrial, final HttpServletRequest request)
  {	   
	   final Optional<MethodDelimiter> delimiterByKey = methodDelimiterRepo.findByMethoddelimiterkeyAndStatus(methodDelimiterKey, 1);
	   final LSuserMaster createdUser = getCreatedUserByKey(doneByUserKey);
	   
	   if(delimiterByKey.isPresent()) {

		   final MethodDelimiter delimiter = delimiterByKey.get();
		   
		   final List<ParserField> parserFieldList = parserFieldRepo.findByMethoddelimiterAndStatus(delimiter, 1);
		   final List<SubParserTechnique> subParserTechList = subParserTechRepo.findByMethoddelimiterAndStatus(delimiter, 1);
		   
		   if (parserFieldList.isEmpty() && subParserTechList.isEmpty()) {
			   //copy of object for using 'Diffable' to compare objects
			   final MethodDelimiter delimitersBeforeSave = new MethodDelimiter(delimiter); 
	
			   //Its not associated in transaction
			   delimiter.setStatus(-1);
			   final MethodDelimiter savedDelimiters = methodDelimiterRepo.save(delimiter);   
					   
			    if (saveAuditTrial)
				{				    	
			    	final String xmlData = convertMethodDelimiterObjectToXML(delimitersBeforeSave, savedDelimiters);
	   			
//			    	cfrTransService.saveCfrTransaction(page, 
//							EnumerationInfo.CFRActionType.USER.getActionType(), "Delete", comments, 
//							site, xmlData, createdUser, request.getRemoteAddr());
				}
			   
			   return new ResponseEntity<>(savedDelimiters, HttpStatus.OK);  
		   }
		   else {
			   //Associated with ParserField or SubParserTechnique
			   if (saveAuditTrial)
			   {
				   final String sysComments = "Delete Failed as methoddelimiter is associated with either ParserField /SubParserTechnique";
	   			
//					cfrTransService.saveCfrTransaction(page, EnumerationInfo.CFRActionType.SYSTEM.getActionType(),
//							"Delete", sysComments, 
//							site, "", createdUser, request.getRemoteAddr());
			   }
			   return new ResponseEntity<>(delimiter.getParsermethod().getParsermethodname() +"-" + delimiter.getDelimiter().getDelimitername() , HttpStatus.IM_USED);//status code - 226		    		
		   }
	   }
	   else
	   {
		   //Invalid MethodDelimiterkey
		   if (saveAuditTrial) {				
//				cfrTransService.saveCfrTransaction(page, EnumerationInfo.CFRActionType.SYSTEM.getActionType(),
//						"Delete", "Delete Failed - MethodDelimiter Not Found", site, "", 
//						createdUser, request.getRemoteAddr());
 		    }			
			return new ResponseEntity<>("Delete Failed - MethodDelimiter Not Found", HttpStatus.NOT_FOUND);
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
	 * This method is used to convert the MethodDelimiter entity to xml with the difference in object
	 * before and after update for recording in Audit Trial
	 * @param delimtersBeforeSave [MethodDelimiter] Object before update
	 * @param savedDelimiter [MethodDelimiter] object after update
	 * @return string formatted xml data
	 */
   public String convertMethodDelimiterObjectToXML(
		   final MethodDelimiter delimtersBeforeSave, final MethodDelimiter savedDelimiter)
   {
	  	final Map<Integer, Map<String, Object>> dataModified = new HashMap<Integer, Map<String, Object>>();
		final Map<String, Object> diffObject = new HashMap<String, Object>();    			
		
		final DiffResult diffResult = delimtersBeforeSave.diff(savedDelimiter);        			
//		for(Diff<?> d: diffResult.getDiffs()) {		    					
//			diffObject.put(d.getFieldName(), d.getKey()+" -> "+d.getValue());
//		}
		
		dataModified.put(savedDelimiter.getMethoddelimiterkey(), diffObject);
		
		final Map<String, String> fieldMap = new HashMap<String, String>();
		fieldMap.put("createdby", "loginid");
		fieldMap.put("parsermethod", "parsermethodname");
		fieldMap.put("delimiter", "delimitername");
			
//		return readWriteXML.saveXML(new MethodDelimiter(savedDelimiter), MethodDelimiter.class, dataModified, "individualpojo", fieldMap);
		return "";
		     	
   }
	
}
