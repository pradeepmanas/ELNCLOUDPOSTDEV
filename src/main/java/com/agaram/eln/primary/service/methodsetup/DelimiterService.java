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
import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.methodsetup.Delimiter;
import com.agaram.eln.primary.model.methodsetup.MethodDelimiter;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.repository.cfr.LScfttransactionRepository;
import com.agaram.eln.primary.repository.methodsetup.DelimiterRepository;
import com.agaram.eln.primary.repository.methodsetup.MethodDelimiterRepository;
import com.agaram.eln.primary.repository.usermanagement.LSSiteMasterRepository;

/**
 * This Service class is used to access the DelimitersRepository to fetch details
 * from the 'delimiters' table through Delimiters Entity relevant to the input request.
 * @author ATE153
 * @version 1.0.0
 * @since   07- Feb- 2020
 */
@Service
public class DelimiterService {

	@Autowired
	DelimiterRepository delimitersRepo;
	
	@Autowired
	LSuserMasterRepository usersRepo;
	
//	@Autowired
//	CfrTransactionService cfrTransService;
	
//	@Autowired
//	ReadWriteXML readWriteXML;
	
	@Autowired
	MethodDelimiterRepository methodDelimiterRepo;
	
	@Autowired
	LScfttransactionRepository lscfttransactionrepo;
	
	/**
	 * This method is used to retrieve list of active delimiters.
	 * @param sortOrder [String] "ASC"/"DESC" based on which the list is to be sorted
	 * @return response entity with list of active delimiters
	 */
	@Transactional
	public ResponseEntity<Object> getActiveDelimiters(final String sortOrder){
		Sort.Direction sortBy = Sort.Direction.DESC;
		if (sortOrder.equalsIgnoreCase("ASC")){
			sortBy = Sort.Direction.ASC;
		}
		return new ResponseEntity<>(delimitersRepo.findByStatus(1,
			  new Sort(sortBy, "delimiterkey")), HttpStatus.OK);
	}
	
	/**
	 * This method is used to add new delimiter object.
	 * Need to check for duplicate 'delimitername' before saving into database. 
	 * @param delimiters [Delimiters] object holding details of all fields of Delimiters entity
	 * @param site [Site] object for which the audittrail recording is to be done
	 * @param saveAuditTrial [boolean] 'true' -to record audit trial, 'false' - not to record audit trial
     * @param page [Page] entity relating to 'Delimiters'
     * @param request [HttpServletRequest] Request object to ip address of remote client
     * @return Response of newly added Delimiters entity
	 */
	@Transactional
	public ResponseEntity<Object> createDelimiters(final Delimiter delimiters, final LSSiteMaster site,

			   final boolean saveAuditTrial, final HttpServletRequest request)
		{ 
		   //Checking for Duplicate delimitername 
		   final Optional<Delimiter> delimiterByName = delimitersRepo
	 				 .findByDelimiternameAndStatus(delimiters.getDelimitername(), 1);
		      
		   final LSuserMaster createdUser = getCreatedUserByKey(delimiters.getCreatedby().getUsercode());
	    	if(delimiterByName.isPresent())
	    	{
	    		
	    		//Conflict = 409 - Duplicate entry
	    		if (saveAuditTrial == true)
				{						
					final String comments = "Create Failed for duplicate delimiter name -"+ delimiters.getDelimitername();
					
					LScfttransaction LScfttransaction = new LScfttransaction();
					
					LScfttransaction.setActions("Insert");
					LScfttransaction.setComments("Duplicate Entry -"+ delimiters.getDelimitername() );
					LScfttransaction.setLssitemaster(site.getSitecode());
					LScfttransaction.setLsuserMaster(delimiters.getCreatedby().getUsercode());
					LScfttransaction.setManipulatetype("View/Load");
					LScfttransaction.setModuleName("Delimiter");
					LScfttransaction.setTransactiondate(delimiters.getCreateddate());
					LScfttransaction.setUsername(delimiters.getUsername());
					LScfttransaction.setTableName("delimiter");
					LScfttransaction.setSystemcoments("System Generated");
					
					lscfttransactionrepo.save(LScfttransaction);
					
//					cfrTransService.saveCfrTransaction(page, EnumerationInfo.CFRActionType.SYSTEM.getActionType(),
//							"Create", comments, site, "",
//							createdUser, request.getRemoteAddr());
				}
	  			return new ResponseEntity<>("Duplicate Entry - " + delimiters.getDelimitername(), 
	  					 HttpStatus.CONFLICT);
	    	}
	    	else
	    	{    		
	    		delimiters.setCreatedby(createdUser);
	    			
	    		final Delimiter savedPolicy = delimitersRepo.save(delimiters);
	    		

				//List<LScfttransaction> lscfttransaction = new ArrayList<LScfttransaction>();
				
	    		LScfttransaction LScfttransaction = new LScfttransaction();
	    		
	    		if (saveAuditTrial)
				{   			
					final Map<String, String> fieldMap = new HashMap<String, String>();
					fieldMap.put("createdby", "loginid");
										
					
					LScfttransaction.setActions("Insert");
					LScfttransaction.setComments(delimiters.getActualdelimiter()+ " was created by  "+delimiters.getUsername());
					LScfttransaction.setLssitemaster(site.getSitecode());
					LScfttransaction.setLsuserMaster(delimiters.getCreatedby().getUsercode());
					LScfttransaction.setManipulatetype("View/Load");
					LScfttransaction.setModuleName("Delimiter");
					LScfttransaction.setTransactiondate(delimiters.getCreateddate());
					LScfttransaction.setUsername(delimiters.getUsername());
					LScfttransaction.setTableName("delimiter");
					LScfttransaction.setSystemcoments("System Generated");
					
					lscfttransactionrepo.save(LScfttransaction);
					
//					final String xmlData = readWriteXML.saveXML(savedPolicy, Delimiter.class, null, "individualpojo", fieldMap);
//									
//					final String actionType = EnumerationInfo.CFRActionType.SYSTEM.getActionType();
//					cfrTransService.saveCfrTransaction(page, actionType, "Create", "", 
//							site, xmlData, createdUser, request.getRemoteAddr());
				}
	      		
	    		return new ResponseEntity<>(savedPolicy , HttpStatus.OK);
	    	} 
	   }  
	
	/**
	 * This method is used to update the selected Delimiters entity.
	 * Need to check for duplicate 'delimitername' before saving into database.
	 * @param delimiters [Delimiters] object holding details of all fields of Delimiters entity
	 * @param site [Site] object for which the audittrail recording is to be done
	 * @param comments [String] comments given for audit trail recording
	 * @param saveAuditTrail [boolean] 'true' -to record audit trial, 'false' - not to record audit trial
     * @param page [Page] entity relating to 'Delimiters'
     * @param request [HttpServletRequest] Request object to ip address of remote client
     * @param doneByUserKey [int] primary key of logged-in user who done this task
     * @return Response of updated Delimiters entity
	 */
   @Transactional
//   public ResponseEntity<Object> updateDelimiters(final Delimiter delimiters, final LSSiteMaster site,
//
//		   final String comments, final boolean saveAuditTrail, 
//		   final HttpServletRequest request, final int doneByUserKey)
//   {	   
//	   final Optional<Delimiter> delimiterByKey = delimitersRepo.findByDelimiterkeyAndStatus(delimiters.getDelimiterkey(), 1);
//	   
//	   final LSuserMaster createdUser = getCreatedUserByKey(doneByUserKey);
//	   
//	   if(delimiterByKey.isPresent()) {		   
//
//		   final List<MethodDelimiter> methodDelimiterList = methodDelimiterRepo.findByDelimiterAndStatus(delimiterByKey.get(), 1);
//		   
//		   if (methodDelimiterList.isEmpty()) {
//			   final Optional<Delimiter> delimiterByName = delimitersRepo
//	 				 .findByDelimiternameAndStatus(delimiters.getDelimitername(), 1);   
//			   final Optional<Delimiter> actualdelimiterByName = delimitersRepo
//		 				 .findByActualdelimiterAndStatus(delimiters.getActualdelimiter(), 1);
//					   
//			  	//Delimiters name should be unique
//				if(delimiterByName.isPresent())
//				{
//				    //Delimiters already available with this name	    		
//				    if (delimiterByName.get().getDelimiterkey().equals(delimiters.getDelimiterkey()) &&
//				    		actualdelimiterByName.get().getDelimiterkey().equals(delimiters.getDelimiterkey()))
//				    	{   		    			
//				    						    					    			
//				    	
//				    		
//			    			if (saveAuditTrail)
//			    			{
//				    			//final String xmlData = convertDelimterObjectToXML(delimitersBeforeSave, savedDelimiters);
//				    			
//				    		    LScfttransaction LScfttransaction = new LScfttransaction();
//				    			LScfttransaction.setActions("update");
//								LScfttransaction.setComments(delimiterByKey.get().getActualdelimiter() +" was updated to "+delimiters.getActualdelimiter());
//								LScfttransaction.setLssitemaster(site.getSitecode());
//								LScfttransaction.setLsuserMaster(delimiters.getCreatedby().getUsercode());
//								LScfttransaction.setManipulatetype("View/Load");
//								LScfttransaction.setModuleName("Delimiter");
//								LScfttransaction.setTransactiondate(delimiters.getCreateddate());
//								LScfttransaction.setUsername(delimiters.getCreatedby().getUserfullname());
//								LScfttransaction.setTableName("Delimiter");
//								LScfttransaction.setSystemcoments("System Generated");
//								
//								lscfttransactionrepo.save(LScfttransaction);
//														    			
//				    		}
//			    			//copy of object for using 'Diffable' to compare objects
//				    		final Delimiter delimitersBeforeSave = new Delimiter(delimiterByName.get());
//				    			
//				    		// Updating fields with existing delimter name
//				    		//  Updating rest of the fields other than delimiter name		    			
//				    		final Delimiter savedDelimiters = delimitersRepo.save(delimiters);
//				    			
//				       		return new ResponseEntity<>(savedDelimiters, HttpStatus.OK);     		
//			    		}
//			    		else
//			    		{ 	
//			    			
//			    	
//			    			
//			    			//Conflict =409 - Duplicate entry
//			    			if (saveAuditTrail == true)
//			    			{		
//			    			    LScfttransaction LScfttransaction = new LScfttransaction();
   
//			    				LScfttransaction.setActions("update");
//								LScfttransaction.setComments("Duplicate Entry "+delimiters.getActualdelimiter());
//								LScfttransaction.setLssitemaster(site.getSitecode());
//								LScfttransaction.setLsuserMaster(delimiters.getCreatedby().getUsercode());
//								LScfttransaction.setManipulatetype("View/Load");
//								LScfttransaction.setModuleName("delimiter");
//								LScfttransaction.setTransactiondate(delimiters.getCreateddate());
//								LScfttransaction.setUsername(delimiters.getCreatedby().getUserfullname());
//								LScfttransaction.setTableName("delimiters");
//								LScfttransaction.setSystemcoments("System Generated");
//								
//								lscfttransactionrepo.save(LScfttransaction);
//								
//			    				
//			    				
//			    				final String sysComments = "Update Failed for duplicate delimiter name -"+ delimiters.getDelimitername();
//			    				
////			    				cfrTransService.saveCfrTransaction(page, EnumerationInfo.CFRActionType.SYSTEM.getActionType(),
////			    						"Create", sysComments, site, "",createdUser, request.getRemoteAddr());
//			    			}
//			    			
//			    			return new ResponseEntity<>("Duplicate Entry ",HttpStatus.CONFLICT);      			
//			    		}
//			    	}
//			    	else
//			    	{			    		
//			    					    		
//			    		LScfttransaction LScfttransaction = new LScfttransaction();
//			    		
//			    		if (saveAuditTrail)
//		    			{
//			    		//	final String xmlData = convertDelimterObjectToXML(delimiterBeforeSave, savedMethod);
//			    			
//			    			LScfttransaction.setActions("update");
//							LScfttransaction.setComments(delimiterByKey.get().getDelimitername() 
//									+" was updated to "+delimiters.getDelimitername());
//							LScfttransaction.setLssitemaster(site.getSitecode());
//							LScfttransaction.setLsuserMaster(delimiters.getCreatedby().getUsercode());
//							LScfttransaction.setManipulatetype("View/Load");
//							LScfttransaction.setModuleName("delimiter");
//							LScfttransaction.setTransactiondate(delimiters.getCreateddate());
//							LScfttransaction.setUsername(delimiters.getCreatedby().getUserfullname());
//							LScfttransaction.setTableName("delimiters");
//							LScfttransaction.setSystemcoments("System Generated");
//							
//							lscfttransactionrepo.save(LScfttransaction);
//					
//			    			
//			    			
////			    			cfrTransService.saveCfrTransaction(page, EnumerationInfo.CFRActionType.USER.getActionType(),
////									"Edit", comments, site, xmlData, createdUser, request.getRemoteAddr());
//		    			}
//			    		//copy of object for using 'Diffable' to compare objects
//		    			final Delimiter delimiterBeforeSave = new Delimiter(delimiterByKey.get());
//		    			
//			    		//Updating fields with a new delimiter name
//		    			
//			    		final Delimiter savedMethod = delimitersRepo.save(delimiters);
//			    		
//			    		return new ResponseEntity<>(savedMethod , HttpStatus.OK);			    		
//			    	}	
//		   		}
//		   		else {
//		   			//Associated with child- cannot be updated
//		   			LScfttransaction LScfttransaction = new LScfttransaction();
//		   			if (saveAuditTrail)
//				    {
//					   final String sysComments = "Update Failed as delimiter - "+ delimiterByKey.get().getDelimitername() + " is associated with Method Delimiter";
//		   			
////						cfrTransService.saveCfrTransaction(page, EnumerationInfo.CFRActionType.SYSTEM.getActionType(),
////								"Edit", sysComments, 
////								site, "", createdUser, request.getRemoteAddr());
//					   
//
//						LScfttransaction.setActions("update");
//						LScfttransaction.setComments("Associated "+ delimiterByKey.get().getDelimitername() );
//						LScfttransaction.setLssitemaster(site.getSitecode());
//						LScfttransaction.setLsuserMaster(delimiters.getCreatedby().getUsercode());
//						LScfttransaction.setManipulatetype("View/Load");
//						LScfttransaction.setModuleName("Delimiter");
//						LScfttransaction.setTransactiondate(delimiters.getCreateddate());
//						LScfttransaction.setUsername(delimiters.getCreatedby().getUserfullname());
//						LScfttransaction.setTableName("Delimiter");
//						LScfttransaction.setSystemcoments("System Generated");
//						
//						lscfttransactionrepo.save(LScfttransaction);
//					   
//				   }
//				   return new ResponseEntity<>(delimiterByKey.get().getDelimitername() , HttpStatus.IM_USED);//status code - 226		    		
//		   		}
//		   }
//		   else
//		   {
//			   //Invalid delimiterkey		   
////			   if (saveAuditTrail) {				
////					cfrTransService.saveCfrTransaction(page, EnumerationInfo.CFRActionType.SYSTEM.getActionType(),
////							"Edit", "Update Failed - Delimiter Not Found", site, "", createdUser, request.getRemoteAddr());
////	   		    }			
//	   			LScfttransaction LScfttransaction = new LScfttransaction();
//
//			    LScfttransaction.setActions("update");
//				LScfttransaction.setComments("Update Failed - Delimiter Not Found");
//				LScfttransaction.setLssitemaster(site.getSitecode());
//				LScfttransaction.setLsuserMaster(delimiters.getCreatedby().getUsercode());
//				LScfttransaction.setManipulatetype("View/Load");
//				LScfttransaction.setModuleName("Delimiter");
//				LScfttransaction.setTransactiondate(delimiters.getCreateddate());
//				LScfttransaction.setUsername(delimiters.getCreatedby().getUserfullname());
//				LScfttransaction.setTableName("Delimiter");
//				LScfttransaction.setSystemcoments("System Generated");
//				
//				lscfttransactionrepo.save(LScfttransaction);
//				return new ResponseEntity<>("Update Failed - Delimiter Not Found", HttpStatus.NOT_FOUND);
//		   }
//   }   
   
   
   public ResponseEntity<Object> updateDelimiters(final Delimiter delimiters, final LSSiteMaster site,
		   final String comments, final boolean saveAuditTrail, 
		   final HttpServletRequest request, final int doneByUserKey)
   {	   
	   final Optional<Delimiter> delimiterByKey = delimitersRepo.findByDelimiterkeyAndStatus(delimiters.getDelimiterkey(), 1);
	   
	   final LSuserMaster createdUser = getCreatedUserByKey(doneByUserKey);
	   
	   if(delimiterByKey.isPresent()) {		   

		   final List<MethodDelimiter> methodDelimiterList = methodDelimiterRepo.findByDelimiterAndStatus(delimiterByKey.get(), 1);
		   
		   if (methodDelimiterList.isEmpty()) {
			   final Optional<Delimiter> delimiterByName = delimitersRepo
	 				 .findByDelimiternameAndStatus(delimiters.getDelimitername(), 1);   
					   
			  	//Delimiters name should be unique
				if(delimiterByName.isPresent())
				{
				    //Delimiters already available with this name	    		
				    if (delimiterByName.get().getDelimiterkey().equals(delimiters.getDelimiterkey()))
				    	{   		    			
				    					    					    			
			    			if (saveAuditTrail)
			    			{
				    			//final String xmlData = convertDelimterObjectToXML(delimitersBeforeSave, savedDelimiters);
				    					    			
//				    			cfrTransService.saveCfrTransaction(page, EnumerationInfo.CFRActionType.USER.getActionType(), 
//				    					"Edit", comments, site, xmlData, createdUser, request.getRemoteAddr());		
				    			
				    			LScfttransaction LScfttransaction = new LScfttransaction();
				    			LScfttransaction.setActions("Update");
								LScfttransaction.setComments(delimiterByKey.get().getActualdelimiter() +" was updated to "+delimiters.getActualdelimiter());
								LScfttransaction.setLssitemaster(site.getSitecode());
								LScfttransaction.setLsuserMaster(delimiters.getCreatedby().getUsercode());
								LScfttransaction.setManipulatetype("View/Load");
								LScfttransaction.setModuleName("Delimiter");
								LScfttransaction.setTransactiondate(delimiters.getTransactiondate());
								LScfttransaction.setUsername(delimiters.getUsername());
								LScfttransaction.setTableName("Delimiter");
								LScfttransaction.setSystemcoments("System Generated");
								
								lscfttransactionrepo.save(LScfttransaction);
				    		}
			    			//copy of object for using 'Diffable' to compare objects
				    		final Delimiter delimitersBeforeSave = new Delimiter(delimiterByName.get());
				    			
				    		// Updating fields with existing delimter name
				    		//  Updating rest of the fields other than delimiter name		    			
				    		final Delimiter savedDelimiters = delimitersRepo.save(delimiters);
				       		return new ResponseEntity<>(savedDelimiters, HttpStatus.OK);     		
			    		}
			    		else
			    		{ 	
			    			//Conflict =409 - Duplicate entry
			    			if (saveAuditTrail == true)
			    			{						
			    				final String sysComments = "Update Failed for duplicate delimiter name -"+ delimiters.getDelimitername();
			    				
//			    				cfrTransService.saveCfrTransaction(page, EnumerationInfo.CFRActionType.SYSTEM.getActionType(),
//			    						"Create", sysComments, site, "",createdUser, request.getRemoteAddr());
			    				
			    				 LScfttransaction LScfttransaction = new LScfttransaction();
			    				   
				    				LScfttransaction.setActions("Update");
									LScfttransaction.setComments("Duplicate Entry "+delimiters.getActualdelimiter());
									LScfttransaction.setLssitemaster(site.getSitecode());
									LScfttransaction.setLsuserMaster(delimiters.getCreatedby().getUsercode());
									LScfttransaction.setManipulatetype("View/Load");
									LScfttransaction.setModuleName("Delimiter");
									LScfttransaction.setTransactiondate(delimiters.getTransactiondate());
									LScfttransaction.setUsername(delimiters.getUsername());
									LScfttransaction.setTableName("Delimiter");
									LScfttransaction.setSystemcoments("System Generated");
									
									lscfttransactionrepo.save(LScfttransaction);
			    				 
			    			}
			    			
			    			return new ResponseEntity<>("Duplicate Entry - " + delimiters.getDelimitername(), 
			    					 HttpStatus.CONFLICT);      			
			    		}
			    	}
			    	else
			    	{			    		
			    		
			    		if (saveAuditTrail)
		    			{
			    			//final String xmlData = convertDelimterObjectToXML(delimiterBeforeSave, savedMethod);
			    			
//			    			cfrTransService.saveCfrTransaction(page, EnumerationInfo.CFRActionType.USER.getActionType(),
//									"Edit", comments, site, xmlData, createdUser, request.getRemoteAddr());
			    			 LScfttransaction LScfttransaction = new LScfttransaction();
			    			 
			    			LScfttransaction.setActions("Update");
							LScfttransaction.setComments(delimiterByKey.get().getDelimitername() 
									+" was updated to "+delimiters.getDelimitername());
							LScfttransaction.setLssitemaster(site.getSitecode());
							LScfttransaction.setLsuserMaster(delimiters.getCreatedby().getUsercode());
							LScfttransaction.setManipulatetype("View/Load");
							LScfttransaction.setModuleName("Delimiter");
							LScfttransaction.setTransactiondate(delimiters.getTransactiondate());
							LScfttransaction.setUsername(delimiters.getUsername());
							LScfttransaction.setTableName("Delimiter");
							LScfttransaction.setSystemcoments("System Generated");
							
							lscfttransactionrepo.save(LScfttransaction);
//					
		    			}
			    		
			    		//copy of object for using 'Diffable' to compare objects
		    			final Delimiter delimiterBeforeSave = new Delimiter(delimiterByKey.get());
		    			
			    		//Updating fields with a new delimiter name
		    			
			    		final Delimiter savedMethod = delimitersRepo.save(delimiters);
			    		
			    		return new ResponseEntity<>(savedMethod , HttpStatus.OK);			    		
			    	}	
		   		}
		   		else {
		   			//Associated with child- cannot be updated
		   			if (saveAuditTrail)
				    {
					   final String sysComments = "Update Failed as delimiter - "+ delimiterByKey.get().getDelimitername() + " is associated with Method Delimiter";
		   			
//						cfrTransService.saveCfrTransaction(page, EnumerationInfo.CFRActionType.SYSTEM.getActionType(),
//								"Edit", sysComments, 
//								site, "", createdUser, request.getRemoteAddr());
					   LScfttransaction LScfttransaction = new LScfttransaction();
					   
					   LScfttransaction.setActions("Update");
						LScfttransaction.setComments("Associated "+ delimiterByKey.get().getDelimitername() );
						LScfttransaction.setLssitemaster(site.getSitecode());
						LScfttransaction.setLsuserMaster(delimiters.getCreatedby().getUsercode());
						LScfttransaction.setManipulatetype("View/Load");
						LScfttransaction.setModuleName("Delimiter");
						LScfttransaction.setTransactiondate(delimiters.getTransactiondate());
						LScfttransaction.setUsername(delimiters.getUsername());
						LScfttransaction.setTableName("Delimiter");
						LScfttransaction.setSystemcoments("System Generated");
						
						lscfttransactionrepo.save(LScfttransaction);
				   }
				   return new ResponseEntity<>(delimiterByKey.get().getDelimitername() , HttpStatus.IM_USED);//status code - 226		    		
		   		}
		   }
		   else
		   {
			   //Invalid delimiterkey		   
//			   if (saveAuditTrail) {				
//					cfrTransService.saveCfrTransaction(page, EnumerationInfo.CFRActionType.SYSTEM.getActionType(),
//							"Edit", "Update Failed - Delimiter Not Found", site, "", createdUser, request.getRemoteAddr());
//	   		    }			
				return new ResponseEntity<>("Update Failed - Delimiter Not Found", HttpStatus.NOT_FOUND);
		   }
   }
   
   
   /**
	 * This method is used to delete the selected Delimiters entity with its primary key.
	 * @param delimiterKey [int] primary key of Delimiter object to delete
	 * @param site [Site] object for which the audittrail recording is to be done
	 * @param comments [String] comments given for audit trail recording
	 * @param doneByUserKey [int] primary key of logged-in user who done this task
	 * @param saveAuditTrial [boolean] 'true' -to record audit trial, 'false' - not to record audit trial
	 * @param page [Page] entity relating to 'Delimiters'
	 * @param request [HttpServletRequest] Request object to ip address of remote client
	 * @return Response of deleted Delimiters entity
	 */
  @Transactional
  public ResponseEntity<Object> deleteDelimters(final int delimiterKey, 

		   final LSSiteMaster site, final String comments, final int doneByUserKey, 
		   final boolean saveAuditTrial, final HttpServletRequest request, Delimiter otherdetails)
 {	   
	   final Optional<Delimiter> delimiterByKey = delimitersRepo.findByDelimiterkeyAndStatus(delimiterKey, 1);
	   final LSuserMaster createdUser = getCreatedUserByKey(doneByUserKey);
	   
	   if(delimiterByKey.isPresent()) {

		   final Delimiter delimiter = delimiterByKey.get();
		   
		   final List<MethodDelimiter> methodDelimiterList = methodDelimiterRepo.findByDelimiterAndStatus(delimiter, 1);
		   
		   if (methodDelimiterList.isEmpty()) {
			  			   
			 
					   
			    if (saveAuditTrial)
				{				    	
			    	//final String xmlData = convertDelimterObjectToXML(delimitersBeforeSave, savedDelimiters);
	   			
			    	LScfttransaction LScfttransaction = new LScfttransaction();
			    	  
			    	LScfttransaction.setActions("Delete");
					LScfttransaction.setComments(delimiter.getDelimitername()+" was deleted by "+otherdetails.getUsername());
					LScfttransaction.setLssitemaster(site.getSitecode());
					LScfttransaction.setLsuserMaster(2);
					LScfttransaction.setManipulatetype("View/Load");
					LScfttransaction.setModuleName("Delimiter");
					LScfttransaction.setTransactiondate(otherdetails.getTransactiondate());
					LScfttransaction.setUsername(otherdetails.getUsername());
					LScfttransaction.setTableName("Delimiter");
					LScfttransaction.setSystemcoments("System Generated");
					
					lscfttransactionrepo.save(LScfttransaction);
			    	
			    	
			    	
//			    	cfrTransService.saveCfrTransaction(page, 
//							EnumerationInfo.CFRActionType.USER.getActionType(), "Delete", comments, 
//							site, xmlData, createdUser, request.getRemoteAddr());
				}
			    
			    //copy of object for using 'Diffable' to compare objects
				   final Delimiter delimitersBeforeSave = new Delimiter(delimiter); 	
				   //Its not associated in transaction
				   delimiter.setStatus(-1);
				   final Delimiter savedDelimiters = delimitersRepo.save(delimiter);  
				   
			   return new ResponseEntity<>(savedDelimiters, HttpStatus.OK);  
		   }
		   else {
			   //Associated with Method Delimiter
			   if (saveAuditTrial)
			   {
				 //  final String sysComments = "Delete Failed as delimiter - "+ delimiter.getDelimitername() + " is associated with Method Delimiter";
	   			
//					cfrTransService.saveCfrTransaction(page, EnumerationInfo.CFRActionType.SYSTEM.getActionType(),
//							"Delete", sysComments, 
//				site, "", createdUser, request.getRemoteAddr());

			    	LScfttransaction LScfttransaction = new LScfttransaction();
			    	  
			    	LScfttransaction.setActions("Delete");
					LScfttransaction.setComments(" Delimiter Associated : " +delimiter.getDelimitername() );
					LScfttransaction.setLssitemaster(site.getSitecode());
					LScfttransaction.setLsuserMaster(2);
					LScfttransaction.setManipulatetype("View/Load");
					LScfttransaction.setModuleName("Delimiter");
					LScfttransaction.setTransactiondate(otherdetails.getTransactiondate());
					LScfttransaction.setUsername(otherdetails.getUsername());
					LScfttransaction.setTableName("Delimiter");
					LScfttransaction.setSystemcoments("System Generated");
					
					lscfttransactionrepo.save(LScfttransaction);
			    	
			   }
			   return new ResponseEntity<>(delimiter.getDelimitername() , HttpStatus.IM_USED);//status code - 226	
			   
		   }
	   }
	   else
	   {
		   //Invalid delimiterkey
		   if (saveAuditTrial) {				
//				cfrTransService.saveCfrTransaction(page, EnumerationInfo.CFRActionType.SYSTEM.getActionType(),
//						"Delete", "Delete Failed - Delimiter Not Found", site, "", 
//						createdUser, request.getRemoteAddr());
		 
			   LScfttransaction LScfttransaction = new LScfttransaction();
		    	  
		    	LScfttransaction.setActions("Delete");
				LScfttransaction.setComments("Delimiter Not Found " );
				LScfttransaction.setLssitemaster(site.getSitecode());
				LScfttransaction.setLsuserMaster(2);
				LScfttransaction.setManipulatetype("View/Load");
				LScfttransaction.setModuleName("Delimiter");
				LScfttransaction.setTransactiondate(otherdetails.getTransactiondate());
				LScfttransaction.setUsername(otherdetails.getUsername());
				LScfttransaction.setTableName("Delimiter");
				LScfttransaction.setSystemcoments("System Generated");
				
				lscfttransactionrepo.save(LScfttransaction);
			   
		    }			
		   
			return new ResponseEntity<>("Delete Failed - Delimiter Not Found", HttpStatus.NOT_FOUND);
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
	 * This method is used to convert the Delimiters entity to xml with the difference in object
	 * before and after update for recording in Audit Trial
	 * @param delimtersBeforeSave [Delimiter] Object before update
	 * @param savedDelimiter [Delimiter] object after update
	 * @return string formatted xml data
	 */
   public String convertDelimterObjectToXML(
		   final Delimiter delimtersBeforeSave, final Delimiter savedDelimiter)
   {
	  	final Map<Integer, Map<String, Object>> dataModified = new HashMap<Integer, Map<String, Object>>();
		final Map<String, Object> diffObject = new HashMap<String, Object>();    			
		
		final DiffResult diffResult = delimtersBeforeSave.diff(savedDelimiter);        			
//		for(Diff<?> d: diffResult.getDiffs()) {		    					
//			diffObject.put(d.getFieldName(), d.getKey()+" -> "+d.getValue());
//		}
		
		dataModified.put(savedDelimiter.getDelimiterkey(), diffObject);
		
		final Map<String, String> fieldMap = new HashMap<String, String>();
		fieldMap.put("createdby", "loginid");
			
//		return readWriteXML.saveXML(new Delimiter(savedDelimiter), Delimiter.class, dataModified, "individualpojo", fieldMap);
		return "";
		     	
   }
	
}
