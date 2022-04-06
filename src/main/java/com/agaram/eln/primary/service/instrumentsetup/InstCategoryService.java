package com.agaram.eln.primary.service.instrumentsetup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.builder.DiffResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.agaram.eln.primary.model.instrumentsetup.InstrumentCategory;
import com.agaram.eln.primary.model.instrumentsetup.InstrumentMaster;
//import com.agaram.lleln.jaxb.ReadWriteXML;
//import com.agaram.lleln.page.Page;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.repository.instrumentsetup.InstCategoryRepository;
//import com.agaram.lleln.cfrpart11.cfrtransaction.CfrTransactionService;
import com.agaram.eln.primary.repository.instrumentsetup.InstMasterRepository;
import com.agaram.eln.primary.repository.usermanagement.LSuserMasterRepository;
//import com.agaram.lleln.users.CreatedUser;
//import com.agaram.lleln.util.EnumerationInfo;

/**
 * This Service class is used to access the InstCategoryRepository to fetch details
 * from the 'instrumentcategory' table through 'InstrumentCategory' Entity relevant to the input request.
 * @author ATE153
 * @version 1.0.0
 * @since   11- Nov- 2019
 */
@Service
public class InstCategoryService {
	
	@Autowired
	InstCategoryRepository categoryRepo;
	
	@Autowired
	LSuserMasterRepository userRepo;
	
//	@Autowired
//	ReadWriteXML readWriteXML;
//	
//	@Autowired
//	CfrTransactionService cfrTransService;
	
	@Autowired
	InstMasterRepository instMasterRepo;
	
	/**
     * This method is used to add new instrument category.
     * @param category [InstrumentCategory] -  holding details of all fields in 'InstrumentCategory' Pojo.
     * @param saveAuditTrial [boolean] 'true' -to record audit trial, 'false' - not to record audit trial
     * @param page [Page] entity relating to 'InstrumentCategory'
     * @param request [HttpServletRequest] Request object to ip address of remote client   
     * @return newly added InstrumentCategoryo object with status Message
     */
	@Transactional
    public ResponseEntity<Object> createInstCategory(final InstrumentCategory category, 
    		final boolean saveAuditTrial, final HttpServletRequest request) {    	
    	
    	final Optional<InstrumentCategory> categoryByName = categoryRepo.findByInstcatnameAndStatus(
    			category.getInstcatname(),1);
		
		if (categoryByName.isPresent())
		{
			//Conflict =409 - Duplicate entry
  			return new ResponseEntity<>("Duplicate Entry - " + categoryByName.get().getInstcatname(), 
  					 HttpStatus.CONFLICT);
//			return new ResponseEntity<>("Duplicate Entry", 
// 					 HttpStatus.OK);
		}
		else
		{	
			final LSuserMaster createdUser = getCreatedUserByKey(category.getCreatedby().getUsercode());			
			category.setCreatedby(createdUser);
			
			final InstrumentCategory savedCategory = categoryRepo.save(category);				
			
			if (saveAuditTrial == true)
			{
				final Map<String, String> fieldMap = new HashMap<String, String>();
				fieldMap.put("createdby", "loginid");
				
//				final String xmlData = readWriteXML.saveXML(savedCategory, InstrumentCategory.class, 
//						null, "individualpojo", fieldMap);
//						
//				final String actionType = EnumerationInfo.CFRActionType.SYSTEM.getActionType();	
//				
//				cfrTransService.saveCfrTransaction(page, actionType, "Create", "", page.getModule().getSite(),
//						xmlData, createdUser, request.getRemoteAddr());
		
			}
			
			return new ResponseEntity<>( savedCategory, HttpStatus.OK);
		}				
    }

    /**
     * This method is used to update the selected 'category' details based on provided 
     * primary key id and details of the  updated fields.
     * @param category [InstrumentCategory] Object with details to update
     * @param saveAuditTrial [boolean] 'true' -to record audit trial, 'false' - not to record audit trial
     * @param comments [String] comments given for audit trail recording
     * @param page [Page] entity relating to 'InstrumentCategory'   
     * @param request [HttpServletRequest] Request object to ip address of remote client
     * @return updated 'category' object
     */
	@Transactional
    public ResponseEntity<Object> updateInstCategory(final InstrumentCategory category, 
    		final boolean saveAuditTrial,  final String comments, final HttpServletRequest request) {
    
         if (category.getInstcatkey() == 1)
        {
        	//statuscode =423
        	//Default Category cannot be deleted
     		return new ResponseEntity<>(category.getInstcatname() 
     				+ " - Default Category Cannot be Updated!", HttpStatus.LOCKED);//status code - 423
        }
        else
        {
	        final Optional<InstrumentCategory> categoryByName = categoryRepo.findByInstcatnameAndStatus(
	    			category.getInstcatname(),1);
			
			if (categoryByName.isPresent())
	 	    {		   
	     		//category already available		
	     		if (categoryByName.get().getInstcatkey().equals(category.getInstcatkey()))
	     		{   
	     			final InstrumentCategory categoryToSave = categoryByName.get();
	     			
	     			//copy of categoryToSave object for using 'Diffable' to compare objects
	     			final InstrumentCategory categoryBeforeSave = new InstrumentCategory(categoryToSave); 
	
	     			/*
	     			 *  Update other fields with existing category name
	     			 *  ok=200
	     			 */
	     			
	     			final InstrumentCategory savedCategory = categoryRepo.save(category); 
	     			
	     			if (saveAuditTrial)
	     			{
	     				final String xmlData = convertCategoryToXML(categoryBeforeSave, savedCategory);
	     				
//	     				final String actionType = EnumerationInfo.CFRActionType.USER.getActionType();
//	     				cfrTransService.saveCfrTransaction(page, actionType, "Edit", comments, 
//	     						page.getModule().getSite(), xmlData, category.getCreatedby(), request.getRemoteAddr());
//	     				
	     			}
	     			
	 	       		return new ResponseEntity<>(savedCategory, HttpStatus.OK);     		
	     		}
	     		else
	     		{ 	
	     			//Conflict =409 - Duplicate entry
	     			return new ResponseEntity<>("Duplicate Entry - " + category.getInstcatname(), 
	     					 HttpStatus.CONFLICT);      			
	     		}
	     	}
	     	else
	     	{
	     		//Updating record with new category name
	     		//HttpStatus.OK-200    		
	     		final InstrumentCategory categoryByKey = categoryRepo.findOne(category.getInstcatkey());
	     		
	     		//copy of roleByKey object for using 'Diffable' to compare objects
	     		final InstrumentCategory categoryBeforeSave = new InstrumentCategory(categoryByKey); 
	 			
	     		final InstrumentCategory savedCategory = categoryRepo.save(category);
	     		
	     		if (saveAuditTrial) {
	     			final String xmlData = convertCategoryToXML(categoryBeforeSave, savedCategory);
	
//	 				final String actionType = EnumerationInfo.CFRActionType.USER.getActionType();
//	 				cfrTransService.saveCfrTransaction(page, actionType, "Edit", comments, 
//	 						page.getModule().getSite(), xmlData, category.getCreatedby(), request.getRemoteAddr());
	     		}
	 			
	     		return new ResponseEntity<>(savedCategory , HttpStatus.OK);
	     	}
        }
        
    }
	
	
    /**
     * This method is used to retrieve list of active category (status=1)
     * @return list of active category.
     */
	@Transactional
    public ResponseEntity<Object> getInstCategory() {     	
    	return new ResponseEntity<>(categoryRepo.findByStatus(1, new Sort(Sort.Direction.DESC, "instcatkey")),
    			HttpStatus.OK);
    }
    
    /**
     * This method is used to update the selected category's status to '-1' 
     * @param categoryKey [int] selected category id to delete
     * @param saveAuditTrial [boolean] 'true' -to record audit trial, 'false' - not to record audit trial
     * @param comments [String] comments given for audit trail recording
     * @param userKey [int] primary key of logged-in user who done this task
     * @param page [Page] entity relating to 'InstrumentCategory'
     * @param request [HttpServletRequest] Request object to ip address of remote client
     * @return Response entity object relevant to delete response
     */
	@Transactional
//    public ResponseEntity<Object> deleteInstCategory(final Integer categoryKey, final boolean saveAuditTrial,
// 		   final String comments, final Integer userKey, final HttpServletRequest request) 
//	{
//        final InstrumentCategory category = categoryRepo.findOne(categoryKey);
//        
//        if (category != null) {
//        	final InstrumentCategory categoryObj = category;
//        	
//        	//if (categoryObj.getInstcatname().equalsIgnoreCase("General"))
//        	if (categoryObj.getInstcatkey() == 1)
//        	{
//        		//Default Category cannot be deleted
//        		return new ResponseEntity<>(categoryObj.getInstcatname(), HttpStatus.LOCKED);//status code - 423
//        	}
//        	else {
//        		List<InstrumentMaster> instMasterList = instMasterRepo.findByInstcategoryAndStatus(categoryObj, 1);
//        		if (instMasterList.size() > 0) {
//        	
//	        		//Has child relation
//	        		return new ResponseEntity<>(categoryObj.getInstcatname(), HttpStatus.IM_USED);//status code - 226
//	        	}        	
//	        	else
//	        	{
//	        		//copy of category object for using 'Diffable' to compare objects
//	 			    final InstrumentCategory categoryBeforeSave = new InstrumentCategory(categoryObj); 		
//	 			   
//	        		//No child mapping
//		        	categoryObj.setStatus(-1);
//		        	final InstrumentCategory savedCategory = categoryRepo.save(categoryObj);
//		        	
//		        	if (saveAuditTrial)
//	     			{
//	     				final String xmlData = convertCategoryToXML(categoryBeforeSave, savedCategory);
////	     				final CreatedUser createdUser = getCreatedUserByKey(userKey);	
////	     				
////	     				final String actionType = EnumerationInfo.CFRActionType.USER.getActionType();
////	     				cfrTransService.saveCfrTransaction(page, actionType, "Delete", comments, 
////	     						page.getModule().getSite(), xmlData, createdUser, request.getRemoteAddr());
//	     				
//	     			}
//		        	        	
//		        	return new ResponseEntity<>(categoryObj, HttpStatus.OK);
//	        	}
//        	}
//        } else {
//            return new ResponseEntity<>("Instrument Category not found", HttpStatus.NOT_FOUND);
//        }
//           
//    }
	
	public ResponseEntity<Object> deleteInstCategory(final Integer categoryKey, final boolean saveAuditTrial,
	 		   final String comments, final Integer userKey, final HttpServletRequest request) 
		{
			Map<String, Object> mapOrders = new HashMap<String, Object>();
	        final InstrumentCategory category = categoryRepo.findOne(categoryKey);
	        
	        ArrayList<String> InstrumentCategory = new ArrayList<String>();
	        
	        if (category != null) {
	        	final InstrumentCategory categoryObj = category;
	        	
	        	//if (categoryObj.getInstcatname().equalsIgnoreCase("General"))
	        	if (categoryObj.getInstcatkey() == 1)
	        	{
	        	      		         	        	
	        		
	        	//	return new ResponseEntity<>(categoryObj.getInstcatname() + " - Default Category Cannot be deleted!", HttpStatus.LOCKED);//status code - 423
	        		
	        		//Default Category cannot be d
	  			   return new ResponseEntity<>(categoryObj.getInstcatname() , HttpStatus.LOCKED);//status code - 423
	  			      
	        		
	        	}
	        	else {
	        		List<InstrumentMaster> instMasterList = instMasterRepo.findByInstcategoryAndStatus(categoryObj, 1);
	        		if (instMasterList.size() > 0) {
	        	        		        			
		        		//return new ResponseEntity<>(categoryObj.getInstcatname(), HttpStatus.IM_USED);//status code - 226
		        		
		        		//Has child relation
		        		return new ResponseEntity<>(categoryObj.getInstcatname(), HttpStatus.LOCKED);
		        	}        	
		        	else
		        	{
		        		//copy of category object for using 'Diffable' to compare objects
		 			    final InstrumentCategory categoryBeforeSave = new InstrumentCategory(categoryObj); 		
		 			   
		        		//No child mapping
			        	categoryObj.setStatus(-1);
			        	final InstrumentCategory savedCategory = categoryRepo.save(categoryObj);
			        	
			        	if (saveAuditTrial)
		     			{
		     				final String xmlData = convertCategoryToXML(categoryBeforeSave, savedCategory);
//		     				final CreatedUser createdUser = getCreatedUserByKey(userKey);	
//		     				
//		     				final String actionType = EnumerationInfo.CFRActionType.USER.getActionType();
//		     				cfrTransService.saveCfrTransaction(page, actionType, "Delete", comments, 
//		     						page.getModule().getSite(), xmlData, createdUser, request.getRemoteAddr());
		     				
		     			}
			        	        	
			        	return new ResponseEntity<>(categoryObj, HttpStatus.OK);
		        	}
	        	}
	        } else {
	            return new ResponseEntity<>("Instrument Category not found", HttpStatus.NOT_FOUND);
	        }
	           
	    }
    
    /**
	 * This method is used to convert the category entity to xml with the difference in object
	 * before and after update for recording in Audit Trial
	 * @param categoryBeforeSave [InstrumentCategory] Object before update
	 * @param savedCategory [InstrumentCategory] object after update
	 * @return string formatted xml data
	 */
	public String convertCategoryToXML(final InstrumentCategory categoryBeforeSave,
			final InstrumentCategory savedCategory) {
   				
		final Map<Integer, Map<String, Object>> categoryModified = new HashMap<Integer, Map<String, Object>>();
		final Map<String, Object> diffObject = new HashMap<String, Object>();    			
		
		final DiffResult diffResult = categoryBeforeSave.diff(savedCategory);        			
		
//		for(Diff<?> d: diffResult.getDiffs()) {
//			diffObject.put(d.getFieldName(), d.getKey()+" -> "+d.getValue());
//		}
		
		categoryModified.put(savedCategory.getInstcatkey(), diffObject);
		
		final Map<String, String> fieldMap = new HashMap<String, String>();
		fieldMap.put("createdby", "loginid");

//		return readWriteXML.saveXML(new InstrumentCategory(savedCategory), InstrumentCategory.class, 
//				categoryModified, "individualpojo", fieldMap);
		
		return "";
	}
	
    
    /**
	 * This method is used to retrieve the 'Users' details based on the
	 * input primary key- userKey.
	 * @param userKey [int] primary key of Users entity
	 * @return Users Object relating to the userKey
	 */
	private LSuserMaster getCreatedUserByKey(final int userKey)
	{
		final LSuserMaster createdBy =  userRepo.findOne(userKey);		
		
		final LSuserMaster createdUser = new LSuserMaster();
		createdUser.setUsername(createdBy.getUsername());
		createdUser.setUsercode(createdBy.getUsercode());
		
		return createdUser;
	}
}
