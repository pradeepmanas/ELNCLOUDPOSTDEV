package com.agaram.eln.primary.service.methodsetup;


import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

//import org.apache.commons.lang3.builder.DiffResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.agaram.eln.primary.commonfunction.commonfunction;
import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.methodsetup.Delimiter;
import com.agaram.eln.primary.model.methodsetup.MethodDelimiter;
import com.agaram.eln.primary.model.methodsetup.ParserField;
import com.agaram.eln.primary.model.methodsetup.ParserMethod;
import com.agaram.eln.primary.model.methodsetup.SubParserTechnique;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.repository.cfr.LScfttransactionRepository;
import com.agaram.eln.primary.repository.methodsetup.DelimiterRepository;
import com.agaram.eln.primary.repository.methodsetup.MethodDelimiterRepository;
import com.agaram.eln.primary.repository.methodsetup.ParserFieldRepository;
import com.agaram.eln.primary.repository.methodsetup.ParserMethodRepository;
import com.agaram.eln.primary.repository.methodsetup.SubParserTechniqueRepository;
import com.agaram.eln.primary.repository.usermanagement.LSuserMasterRepository;

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
	
	@Autowired
	LScfttransactionRepository lscfttransactionrepo;
	
	/**
	 * This method is used to retrieve list of active MethodDelimiter entities.
	 * @return response object  with retrieved list of active MethodDelimiter entities
	 */
	@Transactional
	public ResponseEntity<Object> getMethodDelimiterList(LSSiteMaster mobj){
		//final List<MethodDelimiter> delimiterList = methodDelimiterRepo.findByStatusAndLssitemaster(1,mobj,new Sort(Sort.Direction.DESC, "methoddelimiterkey"));
		final List<MethodDelimiter> delimiterList = methodDelimiterRepo.findByStatusAndLssitemasterOrLssitemasterIsNull(1,mobj,new Sort(Sort.Direction.DESC, "methoddelimiterkey"));
		//final List<MethodDelimiter> delimiterList = methodDelimiterRepo.findByLssitemaster(mobj,new Sort(Sort.Direction.DESC, "methoddelimiterkey"));
		return new ResponseEntity<>(delimiterList, HttpStatus.OK);
	}
	
	@Transactional
	public ResponseEntity<Object> getMethodDelimiter(LSSiteMaster mobj){
		//final List<MethodDelimiter> delimiterList = methodDelimiterRepo.findByStatusAndLssitemaster(1,mobj,new Sort(Sort.Direction.DESC, "methoddelimiterkey"));
		//final List<MethodDelimiter> delimiterList = methodDelimiterRepo.findByLssitemaster(mobj,new Sort(Sort.Direction.DESC, "methoddelimiterkey"));
		final List<MethodDelimiter> delimiterList = methodDelimiterRepo.findByLssitemasterOrLssitemasterIsNull(mobj,new Sort(Sort.Direction.DESC, "methoddelimiterkey"));
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
			    final HttpServletRequest request,MethodDelimiter auditdetails)
		{ 
		
		try {
			methodDelimiter.setCreateddate(commonfunction.getCurrentUtcTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		    boolean saveAuditTrial = true;
		   final ParserMethod parserMethod = parserMethodRepo.findOne(methodDelimiter.getParsermethod().getParsermethodkey());
		   final Delimiter delimiter = delimiterRepo.findOne(methodDelimiter.getDelimiter().getDelimiterkey());
		   
		   //Checking for Duplicate MethodDelimiter with same parsermethod and same delimiter
		   final Optional<MethodDelimiter> methodDelimiterByDelimiter = methodDelimiterRepo
	 				 .findByParsermethodAndDelimiterAndStatusAndLssitemaster(parserMethod, delimiter, 1,methodDelimiter.getLssitemaster());
		   
		   final Optional<MethodDelimiter> meddelimiterdefault = methodDelimiterRepo.findByParsermethodAndDelimiterAndStatusAndDefaultvalue(parserMethod, delimiter, 1,1);
		   
		   final LSuserMaster createdUser = getCreatedUserByKey(methodDelimiter.getCreatedby().getUsercode());
	    	if(methodDelimiterByDelimiter.isPresent() || meddelimiterdefault.isPresent())
	    	{
	    		//LScfttransaction LScfttransaction = new LScfttransaction();
	    		//Conflict = 409 - Duplicate entry
	    		if (saveAuditTrial == true)
				{						
//					final String comments = "Create Failed for duplicate entry - " + methodDelimiterByDelimiter.get().getDelimiter().getDelimitername()
//		  					+" for " + methodDelimiterByDelimiter.get().getParsermethod().getParsermethodname();
					
//					LScfttransaction.setActions("Insert");
//					LScfttransaction.setComments("Duplicate Entry  -"+methodDelimiterByDelimiter.get().getDelimiter().getDelimitername()
//		  					+" for " + methodDelimiterByDelimiter.get().getParsermethod().getParsermethodname());
//					LScfttransaction.setLssitemaster(site.getSitecode());
//					LScfttransaction.setLsuserMaster(methodDelimiterByDelimiter.get().getCreatedby().getUsercode());
//					LScfttransaction.setManipulatetype("View/Load");
//					LScfttransaction.setModuleName("Method Delimiter");
//					LScfttransaction.setTransactiondate(methodDelimiterByDelimiter.get().getCreateddate());
//					LScfttransaction.setUsername(methodDelimiterByDelimiter.get().getCreatedby().getUserfullname());
//					LScfttransaction.setTableName("Methoddelimiter");
//					LScfttransaction.setSystemcoments("System Generated");
//					
//					lscfttransactionrepo.save(LScfttransaction);
					
//					cfrTransService.saveCfrTransaction(page, EnumerationInfo.CFRActionType.SYSTEM.getActionType(),
//							"Create", comments, site, "",
//							createdUser, request.getRemoteAddr());
	    			if(methodDelimiterByDelimiter.isPresent()) {
	    			methodDelimiter.setInfo("Duplicate Entry - " + methodDelimiterByDelimiter.get().getDelimiter().getDelimitername()
	  					+" for " + methodDelimiterByDelimiter.get().getParsermethod().getParsermethodname());
	    			
	    			}else {
	    			methodDelimiter.setInfo("Duplicate Entry - " + meddelimiterdefault.get().getDelimiter().getDelimitername()
	  					+" for " + meddelimiterdefault.get().getParsermethod().getParsermethodname());
	    			}
	    			methodDelimiter.setObjsilentaudit(auditdetails.getObjsilentaudit());
				}
//	  			return new ResponseEntity<>("Duplicate Entry - " + methodDelimiterByDelimiter.get().getDelimiter().getDelimitername()
//	  					+" for " + methodDelimiterByDelimiter.get().getParsermethod().getParsermethodname(), 
//	  					 HttpStatus.CONFLICT);
	  			return new ResponseEntity<>(methodDelimiter, HttpStatus.CONFLICT);
	    	}
	    	else
	    	{    		
	    		methodDelimiter.setCreatedby(createdUser);
	    		methodDelimiter.setParsermethod(parserMethod);
	    		methodDelimiter.setDelimiter(delimiter);
	    			
	    		final MethodDelimiter savedMethodDelimiter = methodDelimiterRepo.save(methodDelimiter);
	    		savedMethodDelimiter.setDisplayvalue(savedMethodDelimiter.getParsermethod().getParsermethodname());
	    		savedMethodDelimiter.setScreenname("MethodDelimiter");
	    	//	savedMethodDelimiter.setObjmanualaudit(methodDelimiter.getObjmanualaudit());
	    		savedMethodDelimiter.setObjsilentaudit(auditdetails.getObjsilentaudit());
	    		
		
					final Map<String, String> fieldMap = new HashMap<String, String>();
					fieldMap.put("createdby", "loginid");
					fieldMap.put("parsermethod", "parsermethodname");
					fieldMap.put("delimiter", "delimitername");
		
	      		
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
		   final String comments, final HttpServletRequest request, final int doneByUserKey,MethodDelimiter auditdetails)
   {	   
	   try {
		methodDelimiter.setCreateddate(commonfunction.getCurrentUtcTime());
	} catch (ParseException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	   Boolean saveAuditTrail= true;
	   final Optional<MethodDelimiter> methodDelimiterByKey = methodDelimiterRepo.findByMethoddelimiterkeyAndStatusAndLssitemaster(methodDelimiter.getMethoddelimiterkey(), 1,methodDelimiter.getLssitemaster());
//	   final LSuserMaster createdUser = getCreatedUserByKey(doneByUserKey);
	   
	   if(methodDelimiterByKey.isPresent()) {	
		   
		   final List<ParserField> parserFieldList = parserFieldRepo.findByMethoddelimiterAndStatus(methodDelimiterByKey.get(), 1);
		   final List<SubParserTechnique> subParserTechList = subParserTechRepo.findByMethoddelimiterAndStatus(methodDelimiterByKey.get(), 1);
		   
		   if (parserFieldList.isEmpty() && subParserTechList.isEmpty()) {
			   //Eligible to update as not associated with any child

				   final ParserMethod parserMethod = parserMethodRepo.findOne(methodDelimiter.getParsermethod().getParsermethodkey());
				   final Delimiter delimiter = delimiterRepo.findOne(methodDelimiter.getDelimiter().getDelimiterkey());
				   
				   //Checking for Duplicate MethodDelimiter with same parsermethod and same delimiter
				   final Optional<MethodDelimiter> methodDelimiterByDelimiter = methodDelimiterRepo
			 				 .findByParsermethodAndDelimiterAndStatusAndLssitemaster(parserMethod, delimiter, 1,methodDelimiter.getLssitemaster());
				  					   
				  	if(methodDelimiterByDelimiter.isPresent())
					{
				  		methodDelimiter.setParsermethod(parserMethod);
			    		methodDelimiter.setDelimiter(delimiter);
			    		
					    //methodDelimiter already available	
//					    if (methodDelimiterByDelimiter.get().getMethoddelimiterkey().equals(methodDelimiter.getMethoddelimiterkey()))
//					    	{   		    			
//					    		//copy of object for using 'Diffable' to compare objects
//					    		final MethodDelimiter delimitersBeforeSave = new MethodDelimiter(methodDelimiterByDelimiter.get());
//				    			
//					    		final MethodDelimiter savedMethodDelimiter = methodDelimiterRepo.save(methodDelimiter);
//					    					    			
//				    			if (saveAuditTrail)
//				    			{
////					    			final String xmlData = convertMethodDelimiterObjectToXML(delimitersBeforeSave, savedMethodDelimiter);
////					    					    			
////					    			cfrTransService.saveCfrTransaction(page, EnumerationInfo.CFRActionType.USER.getActionType(), 
////					    					"Edit", comments, site, xmlData, createdUser, request.getRemoteAddr());		    			
//					    		}
//					    			
//					       		return new ResponseEntity<>(savedMethodDelimiter, HttpStatus.OK);     		
//				    		}
//				    		else
//				    		{ 	
				    			//Conflict =409 - Duplicate entry
				    		
				    			methodDelimiter.setInfo("Duplicate Entry - " + methodDelimiterByDelimiter.get().getDelimiter().getDelimitername() +" for Delimiter : " + methodDelimiterByDelimiter.get().getParsermethod().getParsermethodname());
				    			
				    			methodDelimiter.setObjsilentaudit(auditdetails.getObjsilentaudit());
//				    			return new ResponseEntity<>("Duplicate Entry - " + methodDelimiterByDelimiter.get().getDelimiter().getDelimitername() +" for Delimiter : " + methodDelimiterByDelimiter.get().getParsermethod().getParsermethodname(),
//				    					HttpStatus.CONFLICT);    
				    			return new ResponseEntity<>(methodDelimiter,HttpStatus.CONFLICT);  
				    		//}
				    	}
				    	else
				    	{			    		
				    		//copy of object for using 'Diffable' to compare objects
			    			
				    	
//                            final MethodDelimiter delimiterBeforeSave = new MethodDelimiter(methodDelimiterByKey.get());
			    			
				    		final MethodDelimiter savedMethodDelimiter = methodDelimiterRepo.save(methodDelimiter);
				    		
				    		savedMethodDelimiter.setDisplayvalue(savedMethodDelimiter.getParsermethod().getParsermethodname());
				    		savedMethodDelimiter.setScreenname("MethodDelimiter");
				    		savedMethodDelimiter.setObjsilentaudit(auditdetails.getObjsilentaudit());
	
				    		return new ResponseEntity<>(savedMethodDelimiter , HttpStatus.OK);			    		
				    	}	
		   		}
		   		else {
		   		 //Not Eligible to update as associated with some childR
		   			if (saveAuditTrail)
		   			{
//					   final String sysComments = "Update Failed as methoddelimiter is associated with either ParserField /SubParserTechnique";
		   			
//						cfrTransService.saveCfrTransaction(page, EnumerationInfo.CFRActionType.SYSTEM.getActionType(),
//								"Edit", sysComments, 
//								site, "", createdUser, request.getRemoteAddr());
					   
					   LScfttransaction LScfttransaction = new LScfttransaction();
						
						LScfttransaction.setActions("Update"); 
						LScfttransaction.setComments("Associated " + methodDelimiterByKey.get().getParsermethod().getParsermethodname() +"-" + methodDelimiterByKey.get().getDelimiter().getDelimitername()  );
						LScfttransaction.setLssitemaster(site.getSitecode());
						LScfttransaction.setLsuserMaster(methodDelimiter.getCreatedby().getUsercode());
						LScfttransaction.setManipulatetype("View/Load");
						LScfttransaction.setModuleName("Method Delimiter");
						LScfttransaction.setTransactiondate(methodDelimiter.getTransactiondate());
						LScfttransaction.setUsername(methodDelimiter.getUsername());
						LScfttransaction.setTableName("Methoddelimiter");
						LScfttransaction.setSystemcoments("System Generated");
						
						try {
							LScfttransaction.setTransactiondate(commonfunction.getCurrentUtcTime());
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						lscfttransactionrepo.save(LScfttransaction);
						
				   }
		   			methodDelimiter.setInfo(methodDelimiterByKey.get().getParsermethod().getParsermethodname() +"-" + methodDelimiterByKey.get().getDelimiter().getDelimitername());

	    			methodDelimiter.setObjsilentaudit(auditdetails.getObjsilentaudit());
//				   return new ResponseEntity<>(methodDelimiterByKey.get().getParsermethod().getParsermethodname() +"-" + methodDelimiterByKey.get().getDelimiter().getDelimitername() ,
//						   HttpStatus.IM_USED);//status code - 226
		   			return new ResponseEntity<>(methodDelimiter ,HttpStatus.IM_USED);//status code - 226
		   		}
		   }
		   else
		   {
			   //Invalid methoddelimiterkey		   
//			   if (saveAuditTrail) {				
//					cfrTransService.saveCfrTransaction(page, EnumerationInfo.CFRActionType.SYSTEM.getActionType(),
//							"Edit", "Update Failed - MethodDelimiter Not Found", site, "", createdUser, request.getRemoteAddr());
//	   		    }			
			   
			   LScfttransaction LScfttransaction = new LScfttransaction();
				
				LScfttransaction.setActions("Update"); 
				LScfttransaction.setComments("Update Failed - MethodDelimiter Not Found" );
				LScfttransaction.setLssitemaster(site.getSitecode());
				LScfttransaction.setLsuserMaster(methodDelimiter.getCreatedby().getUsercode());
				LScfttransaction.setManipulatetype("View/Load");
				LScfttransaction.setModuleName("Method Delimiter");
				LScfttransaction.setTransactiondate(methodDelimiter.getTransactiondate());
				LScfttransaction.setUsername(methodDelimiter.getUsername());
				LScfttransaction.setTableName("Methoddelimiter");
				LScfttransaction.setSystemcoments("System Generated");
				try {
					LScfttransaction.setTransactiondate(commonfunction.getCurrentUtcTime());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				lscfttransactionrepo.save(LScfttransaction);
				
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
		   final LSSiteMaster site, final String comments, final int doneByUserKey, final HttpServletRequest request,final MethodDelimiter otherdetails,MethodDelimiter auditdetails)
 {	   
	   Boolean saveAuditTrial = true;
	   final Optional<MethodDelimiter> delimiterByKey = methodDelimiterRepo.findByMethoddelimiterkeyAndStatusAndLssitemaster(methodDelimiterKey, 1,site);
//	   final LSuserMaster createdUser = getCreatedUserByKey(doneByUserKey);
	   
	   if(delimiterByKey.isPresent()) {

		   if(delimiterByKey.get().getDefaultvalue() == null) {
		   final MethodDelimiter delimiter = delimiterByKey.get();
		   
		   final List<ParserField> parserFieldList = parserFieldRepo.findByMethoddelimiterAndStatus(delimiter, 1);
		   final List<SubParserTechnique> subParserTechList = subParserTechRepo.findByMethoddelimiterAndStatus(delimiter, 1);
		   
		   if (parserFieldList.isEmpty() && subParserTechList.isEmpty()) {
			   //copy of object for using 'Diffable' to compare objects
			   
			   if(methodDelimiterKey == 1) {
				   
				   LScfttransaction LScfttransaction = new LScfttransaction();
					
					LScfttransaction.setActions("Delete"); 
					LScfttransaction.setComments("Default cannot be deleted" );
					LScfttransaction.setLssitemaster(site.getSitecode());
					LScfttransaction.setLsuserMaster(doneByUserKey);
					LScfttransaction.setManipulatetype("View/Load");
					LScfttransaction.setModuleName("Method Delimiter");
					LScfttransaction.setTransactiondate(otherdetails.getTransactiondate());
					LScfttransaction.setUsername(otherdetails.getUsername());
					LScfttransaction.setTableName("Methoddelimiter");
					LScfttransaction.setSystemcoments("System Generated");
					
					try {
						LScfttransaction.setTransactiondate(commonfunction.getCurrentUtcTime());
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					lscfttransactionrepo.save(LScfttransaction);
				   
				   return new ResponseEntity<>("Delete Failed -", HttpStatus.LOCKED);
			   }
			   else {
			   

//			    final MethodDelimiter delimitersBeforeSave = new MethodDelimiter(delimiter); 
				
				   //Its not associated in transaction
				   delimiter.setStatus(-1);
				   delimiter.setMethoddelimiterstatus("D");
				   final MethodDelimiter savedDelimiters = methodDelimiterRepo.save(delimiter);   
			   
				   savedDelimiters.setDisplayvalue(savedDelimiters.getParsermethod().getParsermethodname());
				   savedDelimiters.setScreenname("MethodDelimiter");
		    		
				   savedDelimiters.setObjsilentaudit(auditdetails.getObjsilentaudit());

			   return new ResponseEntity<>(savedDelimiters, HttpStatus.OK);  
		   }
		 }
		   else {
			   //Associated with ParserField or SubParserTechnique
//			   if (saveAuditTrial)
//			   {
//	
//                  LScfttransaction LScfttransaction = new LScfttransaction();
//					
//					LScfttransaction.setActions("Delete"); 
//					LScfttransaction.setComments("Associated : " +delimiter.getParsermethod().getParsermethodname() +"-" + delimiter.getDelimiter().getDelimitername());
//					LScfttransaction.setLssitemaster(site.getSitecode());
//					LScfttransaction.setLsuserMaster(doneByUserKey);
//					LScfttransaction.setManipulatetype("View/Load");
//					LScfttransaction.setModuleName("Method Delimiter");
//					LScfttransaction.setTransactiondate(otherdetails.getTransactiondate());
//					LScfttransaction.setUsername(otherdetails.getUsername());
//					LScfttransaction.setTableName("Methoddelimiter");
//					LScfttransaction.setSystemcoments("System Generated");
//					
//					lscfttransactionrepo.save(LScfttransaction);
//			   }
			   delimiter.setInfo("Associated : " + delimiter.getParsermethod().getParsermethodname() +"-" + delimiter.getDelimiter().getDelimitername());
			   delimiter.setObjsilentaudit(auditdetails.getObjsilentaudit());
			   
//			   return new ResponseEntity<>(delimiter.getParsermethod().getParsermethodname() +"-" + delimiter.getDelimiter().getDelimitername() ,
//					   HttpStatus.IM_USED);//status code - 226	
			   return new ResponseEntity<>(delimiter ,HttpStatus.IM_USED);//status code - 226	
		   }
	   }
		   else {
			   return new ResponseEntity<>(delimiterByKey.get().getParsermethod().getParsermethodname() +"-" + delimiterByKey.get().getDelimiter().getDelimitername(), HttpStatus.LOCKED);
		   }
	   }
	   else
	   {
		   //Invalid MethodDelimiterkey
		   if (saveAuditTrial) {				
//				cfrTransService.saveCfrTransaction(page, EnumerationInfo.CFRActionType.SYSTEM.getActionType(),
//						"Delete", "Delete Failed - MethodDelimiter Not Found", site, "", 
//						createdUser, request.getRemoteAddr());
			   

              LScfttransaction LScfttransaction = new LScfttransaction();
				
				LScfttransaction.setActions("Delete"); 
				LScfttransaction.setComments("MethodDelimiter Not Found");
				LScfttransaction.setLssitemaster(site.getSitecode());
				LScfttransaction.setLsuserMaster(doneByUserKey);
				LScfttransaction.setManipulatetype("View/Load");
				LScfttransaction.setModuleName("Method Delimiter");
				LScfttransaction.setTransactiondate(otherdetails.getTransactiondate());
				LScfttransaction.setUsername(otherdetails.getUsername());
				LScfttransaction.setTableName("Methoddelimiter");
				
				try {
					LScfttransaction.setTransactiondate(commonfunction.getCurrentUtcTime());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				lscfttransactionrepo.save(LScfttransaction);
			   
			   
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
		
//		final DiffResult diffResult = delimtersBeforeSave.diff(savedDelimiter);        			
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
