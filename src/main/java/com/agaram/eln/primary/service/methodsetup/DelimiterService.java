package com.agaram.eln.primary.service.methodsetup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.methodsetup.Delimiter;
import com.agaram.eln.primary.model.methodsetup.MethodDelimiter;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.repository.cfr.LScfttransactionRepository;
import com.agaram.eln.primary.repository.methodsetup.DelimiterRepository;
import com.agaram.eln.primary.repository.methodsetup.MethodDelimiterRepository;
import com.agaram.eln.primary.repository.usermanagement.LSuserMasterRepository;

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
	public ResponseEntity<Object> getActiveDelimiters(final String sortOrder,LSSiteMaster del){
		Sort.Direction sortBy = Sort.Direction.DESC;
		if (sortOrder.equalsIgnoreCase("ASC")){
			sortBy = Sort.Direction.ASC;
		}
		List<Delimiter> delimiter =delimitersRepo.findByLssitemasterOrLssitemasterIsNull(del,new Sort(sortBy, "delimiterkey"));
		return new ResponseEntity<>(delimiter, HttpStatus.OK);	
	
	}
	@Transactional
	public ResponseEntity<Object> getActiveDelimiterdata(final String sortOrder,LSSiteMaster del){
		Sort.Direction sortBy = Sort.Direction.DESC;
		if (sortOrder.equalsIgnoreCase("ASC")){
			sortBy = Sort.Direction.ASC;
		}

		List<Delimiter> delimiter =delimitersRepo.findByLssitemasterAndStatusOrLssitemasterIsNull(del,1,new Sort(sortBy, "delimiterkey"));
		return new ResponseEntity<>(delimiter, HttpStatus.OK);	
	
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
		final Delimiter auditdetails,  final HttpServletRequest request)
		{ 
		
		   //Checking for Duplicate delimitername 
//		   boolean saveAuditTrial = true;
		   final Optional<Delimiter> delimiterByName = delimitersRepo
	 				 .findByDelimiternameIgnoreCaseAndStatusAndLssitemaster(delimiters.getDelimitername(), 1,delimiters.getLssitemaster());
		   
//for defaultvalue
		   final Optional<Delimiter> delimiterdefault = delimitersRepo.findByDelimiternameIgnoreCaseAndDefaultvalue(delimiters.getDelimitername(),1);
		      
		   final LSuserMaster createdUser = getCreatedUserByKey(delimiters.getCreatedby().getUsercode());
	    	if(delimiterByName.isPresent() || delimiterdefault.isPresent())
	    	{
	    		
	    		//Conflict = 409 - Duplicate entry

	    		delimiters.setInfo("Duplicate Entry - " + delimiters.getDelimitername());
	    		delimiters.setObjsilentaudit(auditdetails.getObjsilentaudit());
	  			return new ResponseEntity<>(delimiters, HttpStatus.CONFLICT);
	    	}
	    	else
	    	{    		
	    		delimiters.setCreatedby(createdUser);
	    			
	    		final Delimiter savedPolicy = delimitersRepo.save(delimiters);
	    		savedPolicy.setDisplayvalue("Delimiter "+savedPolicy.getDelimitername());
	    		savedPolicy.setScreenname("Delimiter");
	    		savedPolicy.setObjsilentaudit(auditdetails.getObjsilentaudit());
	    		
					final Map<String, String> fieldMap = new HashMap<String, String>();
					fieldMap.put("createdby", "loginid");
				
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
	 * @param Objsilentaudit 
     * @return Response of updated Delimiters entity
	 */
   @Transactional
   
   
   public ResponseEntity<Object> updateDelimiters(final Delimiter delimiters, final LSSiteMaster site, 
		   final int doneByUserKey,final Delimiter auditdetails, final HttpServletRequest request)
   {	   
	   final Optional<Delimiter> delimiterByKey = delimitersRepo.findByDelimiterkeyAndStatusAndLssitemaster(delimiters.getDelimiterkey(), 1,delimiters.getLssitemaster());
	   
	   if(delimiterByKey.isPresent()) {		   
		   final List<MethodDelimiter> methodDelimiterList = methodDelimiterRepo.findByDelimiterAndStatusAndLssitemaster(delimiterByKey.get(), 1,delimiters.getLssitemaster());
		   
		   if (methodDelimiterList.isEmpty()) {
			   final Optional<Delimiter> delimiterByName = delimitersRepo
	 				 .findByDelimiternameAndStatus(delimiters.getDelimitername(), 1);   
					   
			  	//Delimiters name should be unique
				if(delimiterByName.isPresent())
				{
				    //Delimiters already available with this name	    		
				    if (delimiterByName.get().getDelimiterkey().equals(delimiters.getDelimiterkey()))
				    	{   		    			
			
				    	delimiters.getObjsilentaudit().setModuleName("Delimiter");
				
				    	delimiters.getObjsilentaudit().setTableName("Delimiter");
					
				    		final Delimiter savedDelimiters = delimitersRepo.save(delimiters);
				    		
				    		savedDelimiters.setDisplayvalue("Delimiter "+savedDelimiters.getDelimitername());
				    		savedDelimiters.setScreenname("Delimiter");

				    		savedDelimiters.setObjsilentaudit(auditdetails.getObjsilentaudit());

				    		
				       		return new ResponseEntity<>(savedDelimiters, HttpStatus.OK);     		
			    		}
			    		else
			    		{ 	
			    			//Conflict =409 - Duplicate entry
			    		
			    			delimiters.setInfo("Duplicate Entry - " + delimiters.getDelimitername());
			    			delimiters.setObjsilentaudit(auditdetails.getObjsilentaudit());
			    			
			    			return new ResponseEntity<>(delimiters, 
			    					 HttpStatus.CONFLICT);      			
			    		}
			    	}
			    	else
			    	{			    		

			    		
			    		//copy of object for using 'Diffable' to compare objects
//		    			final Delimiter delimiterBeforeSave = new Delimiter(delimiterByKey.get());
		    			
			    		//Updating fields with a new delimiter name
		    			
			    		final Delimiter savedMethod = delimitersRepo.save(delimiters);
			    		
			    		savedMethod.setDisplayvalue("Delimiter "+savedMethod.getDelimitername());
			    		savedMethod.setScreenname("Delimiter");
			    		savedMethod.setObjsilentaudit(auditdetails.getObjsilentaudit());

			    		return new ResponseEntity<>(savedMethod , HttpStatus.OK);			    		
			    	}	
		   		}
		   		else {
		   			//Associated with child- cannot be updated
		   			
		   			delimiters.setInfo("Delimiter Associated - " + delimiters.getDelimitername());
		   			delimiters.setObjsilentaudit(auditdetails.getObjsilentaudit());
				   return new ResponseEntity<>(delimiters, HttpStatus.IM_USED);//status code - 226		    		
		   		}
		   }
		   else
		   {
		
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
		   final HttpServletRequest request, Delimiter otherdetails,Delimiter auditdetails)
 {	   
	  Boolean saveAuditTrial = true;
	   final Optional<Delimiter> delimiterByKey = delimitersRepo.findByDelimiterkeyAndStatus(delimiterKey, 1);
//	   final LSuserMaster createdUser = getCreatedUserByKey(doneByUserKey);
	   
	   if(delimiterByKey.isPresent()) {

		   if(delimiterByKey.get().getDefaultvalue() == null) {
			   		   
		   final Delimiter delimiter = delimiterByKey.get();
		   
		   final List<MethodDelimiter> methodDelimiterList = methodDelimiterRepo.findByDelimiterAndStatusAndLssitemaster(delimiter, 1,site);
		   
		   if (methodDelimiterList.isEmpty()) {
			
			    
			    //copy of object for using 'Diffable' to compare objects
//				   final Delimiter delimitersBeforeSave = new Delimiter(delimiter); 	
				   //Its not associated in transaction
				   delimiter.setStatus(-1);
				   delimiter.setDelimiterstatus("D");
				   final Delimiter savedDelimiters = delimitersRepo.save(delimiter);  
				   
				   savedDelimiters.setDisplayvalue("Delimiter "+savedDelimiters.getDelimitername());
				   savedDelimiters.setScreenname("Delimiter");
				   //savedDelimiters.setObjmanualaudit(delimiters.getObjmanualaudit());
				   savedDelimiters.setObjsilentaudit(auditdetails.getObjsilentaudit());

			   return new ResponseEntity<>(savedDelimiters, HttpStatus.OK);  
		   }
		   else {
			   //Associated with Method Delimiter
			
			   delimiter.setObjsilentaudit(auditdetails.getObjsilentaudit());
			   delimiter.setInfo("Delimiter : " +delimiter.getDelimitername()+ " is used");
			 //  return new ResponseEntity<>(delimiter.getDelimitername() , HttpStatus.IM_USED);//status code - 226	
			   return new ResponseEntity<>(delimiter , HttpStatus.IM_USED);//status code - 226	
			   
		   }
		 }
		   else {
			   return new ResponseEntity<>(delimiterByKey.get().getDelimitername(), HttpStatus.LOCKED);
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
		
//		final DiffResult diffResult = 
				delimtersBeforeSave.diff(savedDelimiter);        			
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
