package com.agaram.eln.primary.service.instrumentsetup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.builder.Diff;
import org.apache.commons.lang3.builder.DiffResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//import com.agaram.lleln.cfrpart11.cfrtransaction.CfrTransactionService;
//import com.agaram.lleln.jaxb.ReadWriteXML;
//import com.agaram.lleln.page.Page;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.repository.usermanagement.LSuserMasterRepository;
import com.agaram.eln.primary.model.instrumentsetup.InstRightsEssential;
import com.agaram.eln.primary.model.instrumentsetup.InstrumentMaster;
import com.agaram.eln.primary.model.instrumentsetup.InstrumentRights;
import com.agaram.eln.primary.model.instrumentsetup.InstrumentRightsCollection;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.repository.instrumentsetup.InstMasterRepository;
import com.agaram.eln.primary.repository.instrumentsetup.InstRightsRepository;
import com.agaram.eln.primary.repository.usermanagement.LSSiteMasterRepository;
//import com.agaram.lleln.util.EnumerationInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * This Service class is used to access the InstRightsRepository to fetch details
 * from the 'instrumentrights' table through InstrumentRights Entity relevant to the input request.
 * @author ATE153
 * @version 1.0.0
 * @since   11- Nov- 2019
 */
@Service
public class InstRightsService {
	
	@Autowired
	InstRightsRepository rightsRepo;
	
	@Autowired
	InstMasterRepository masterRepo;
	
	@Autowired
	LSuserMasterRepository usersRepo;
	
	@Autowired
	LSSiteMasterRepository userSiteRepo;
	
//	@Autowired
//	CfrTransactionService cfrTransService;
	
	/**
	 * This method is used to save the rights assigned to use the instruments by the specified user.
	 * @param rightsObject [Map] object holding detail of rights to save.
	 * @param request [HttpServletRequest] Request object to ip address of remote client
	 * @return response entity of save rights details for the next selected user
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public ResponseEntity<Object> saveInstRights(final Map<String, Object> rightsObject, final HttpServletRequest request) 
	{
	    final ObjectMapper mapper = new ObjectMapper();		
	    
	    final Boolean initialGet = mapper.convertValue(rightsObject.get("initialGet"), Boolean.class);
	    final Boolean saveAuditTrail = mapper.convertValue(rightsObject.get("saveAuditTrail"), Boolean.class);
//	    final Page page = mapper.convertValue(rightsObject.get("modulePage"), Page.class);
		final LSSiteMaster site =  mapper.convertValue(rightsObject.get("site"), LSSiteMaster.class);
	    String comments = "";
	    if(rightsObject.get("comments") != null)
	    {
	    	comments = (String) rightsObject.get("comments");
	    }
	    
	 	int createdBy = (Integer)rightsObject.get("createdby");
	 		
 	    List<InstRightsEssential> instToSave = new ArrayList<InstRightsEssential>();
	    if(rightsObject.get("instToSave") != null)
	    {
		    instToSave = mapper.convertValue(rightsObject.get("instToSave"), 
			  	new TypeReference<List<InstRightsEssential>>() {});
	    }

	 	int userSiteKey = (Integer)rightsObject.get("userSiteKey");
	 	int getListUserSiteKey = (Integer)rightsObject.get("getListUserSiteKey");
	 	
		final LSSiteMaster userSite = userSiteRepo.findOne(userSiteKey);	 
		final List<InstrumentRights> rightsBeforeSave = rightsRepo.findByUsersite(userSite);
		List<InstrumentRights> cloneListBeforeSave = new ArrayList<InstrumentRights>();
		
		if(saveAuditTrail)
		{				
			cloneListBeforeSave = rightsBeforeSave.stream().map(InstrumentRights::new).collect(Collectors.toList());
		}
		
		final List<InstrumentRights> instRightsList = new ArrayList<InstrumentRights>();
		final LSuserMaster createdUser = getCreatedUserByKey(createdBy);

		 for (InstRightsEssential rightsEssential :instToSave)
		 {
			 boolean validInsert = false;
			 if (rightsEssential.getInstrightskey() == null)
			 {
				 if (rightsEssential.getStatus()!= null && !rightsEssential.getStatus().equals(-1))
				 {
					 validInsert = true;
				 }
			 }
			 else
			 {
				 validInsert = true;
			 }
			 if (validInsert)
			 {		
				InstrumentRights rights = null;
				for (InstrumentRights instRights :rightsBeforeSave)
				{
					if (instRights.getInstrightskey().equals(rightsEssential.getInstrightskey()))
					{
						rights = instRights;
						break;
					}
				}
				if (rights == null)
				{
					rights = new InstrumentRights();
					rights.setCreatedby(createdUser);
					rights.setInstrightskey(0);
					rights.setMaster(rightsEssential.getMaster());
					rights.setUsersite(userSite);
				}				
				
				rights.setStatus(rightsEssential.getStatus());			
				
				instRightsList.add(rights);
			 }
		}
		
		if(!instRightsList.isEmpty())
		{
		 	final List<InstrumentRights> listAfterSave = rightsRepo.save(instRightsList);
		 		
		 	if (saveAuditTrail)
			{
	        	final List<InstrumentRights> savedCloneList = listAfterSave.stream().map(InstrumentRights::new)
						 .collect(Collectors.toList());					
			
	        	final String xmlData = convertInstrumentRightsListToXML(cloneListBeforeSave, savedCloneList);
								
//				String actionType = "";
//			    String action = "";
//			    if (cloneListBeforeSave.isEmpty())
//			    {
//			    	actionType = EnumerationInfo.CFRActionType.SYSTEM.getActionType();
//				    action = "Create";
//			    }
//			    else
//			    {
//			    	actionType = EnumerationInfo.CFRActionType.USER.getActionType();
//				    action = "Edit";
//			    }
//
//    			cfrTransService.saveCfrTransaction(page, actionType, action, comments, 
//    					    page.getModule().getSite(), xmlData, createdUser, request.getRemoteAddr());				 
			}
	 	}
	 	final Map<String, Object> mapObject = new HashMap<String, Object>();	 
	 	
	 	mapObject.putAll((Map<String, Object>) getInstRightsByUserSite(getListUserSiteKey, site, initialGet).getBody());	
    	return new ResponseEntity<>(mapObject, HttpStatus.OK);     
		
	 }
    
	/**
	 * This method is used to fetch list of rights assigned to use instruments by the specified user.
	 * @param userSiteKey [int] primary key of site user to whom the rights is to be fetched.
	 * @param site [Site] object for which the result is to be fetched
	 * @param initialGet [boolean] whethet this fetch is for 'page load' or o 'user onchange'
	 * @return list of instruments with assigned status.
	 */
	@Transactional
    public ResponseEntity<Object> getInstRightsByUserSite(final int userSiteKey, final LSSiteMaster site,
    		final Boolean initialGet) {   
    	
    	final Map<String, Object> mapObject = new HashMap<String, Object>();
    	LSSiteMaster selectedUserSite = null;
    	if (initialGet)
    	{
    		final List<LSSiteMaster> userSiteList = userSiteRepo.findAll();
    		mapObject.put("InstRightsUserSiteList", userSiteList);
    		if (!userSiteList.isEmpty())
    		{
    			selectedUserSite = userSiteList.get(0);
    		}
    	}
    	else
    	{
    		selectedUserSite =  userSiteRepo.findOne(userSiteKey);
    	}
    	
    	if (selectedUserSite != null)
    	{
    		final List<InstRightsEssential> list  = new ArrayList<InstRightsEssential>();
    		//rightsRepo.getInstRightsByUserSite(selectedUserSite.getSitecode(),site.getSitecode());
        	mapObject.put("InstRightsAssignedList", list);
        	mapObject.put("InstRightsSelectedUserSite", selectedUserSite);
    	}    	
    
    	return new ResponseEntity<>(mapObject, HttpStatus.OK);
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
   	 * This method is used to convert the list of InstrumentRights entities to xml with the difference in object
   	 * before and after update for recording in Audit Trial
   	 * @param listBeforeSave list of instrument rights details before saving to DB
   	 * @param savedInstRightsList list of user site role details after saving to DB
   	 * @return string formatted xml data
   	 */
   	public String convertInstrumentRightsListToXML(final List<InstrumentRights> listBeforeSave, 
   			final List<InstrumentRights> savedInstRightsList)
   	{
   	   		try {				
   			
   			final InstrumentRightsCollection afterSaveList = new InstrumentRightsCollection(); 
   			afterSaveList.setInstRightsCollection(savedInstRightsList);				
   		
   			final Map<Integer, Map<String, Object>> dataModified = new HashMap<Integer, Map<String, Object>>();
   			for (InstrumentRights savedRights : savedInstRightsList)
   			{						
   				for (InstrumentRights beforeSaveRights : listBeforeSave)
   				{
   					if (beforeSaveRights.getInstrightskey().equals(savedRights.getInstrightskey()))
   					{							
   						final DiffResult diffResult = beforeSaveRights.diff(savedRights);					
   						
   						final Map<String, Object> diffObject = new HashMap<String, Object>();
   						
//   						for(Diff<?> d: diffResult.getDiffs()) 
//   						{												
//   							if(d.getType() instanceof InstrumentMaster ||d.getType() instanceof CreatedUser)
//   							{								     
//   								final String diffKey =  getFieldValue(d.getFieldName(), d.getKey());								
//   								final String diffValue = getFieldValue(d.getFieldName(), d.getValue());								
//   								
//   								diffObject.put(d.getFieldName(), diffKey+" -> "+diffValue);
//   							}
//   							else
//   							{
//   								diffObject.put(d.getFieldName(), d.getKey()+" -> "+d.getValue());
//   							}
//   							
//   						}
   						dataModified.put(beforeSaveRights.getInstrightskey(), diffObject);
   					}						
   				}
   			}
   			
   		
   			final Map<String, String> fieldMap = new HashMap<String, String>();   			
   			fieldMap.put("usersite", "sitename");   		
   			fieldMap.put("createdby", "loginid");   			
   			fieldMap.put("master", "instrumentcode");

//   			final ReadWriteXML readWriteXML =  new ReadWriteXML();
//   			return readWriteXML.saveXML(afterSaveList, InstrumentRightsCollection.class, dataModified, "listpojo",
//   					fieldMap);	
   			return "";
   		
   		} catch (SecurityException | IllegalArgumentException e) {
   		   return null;
   		}

   	}	
   	
   	/**
	 * This method is used to access the deeply nested properties of the object
	 * @param fieldName [String] name of the property
	 * @param data [Object] from which data is to be fetched
	 * @return value of deeply nested property
	 */
	private static String getFieldValue(String fieldName, Object data)
	{
		if (fieldName.equalsIgnoreCase("master")) 
		  return ((InstrumentMaster)data).getInstrumentcode();
		else if (fieldName.equalsIgnoreCase("createdby"))
			return ((LSuserMaster)data).getUsername();
		else if (fieldName.equalsIgnoreCase("usersite"))
			return ((LSSiteMaster)data).getSitename();
		else
			return "";
	}
   	
}
   