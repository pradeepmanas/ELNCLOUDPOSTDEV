package com.agaram.eln.primary.service.methodsetup;

//import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.methodsetup.Method;
import com.agaram.eln.primary.model.methodsetup.SampleExtract;
import com.agaram.eln.primary.model.methodsetup.SampleLineSplit;
import com.agaram.eln.primary.model.methodsetup.SampleTextSplit;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.repository.cfr.LScfttransactionRepository;
import com.agaram.eln.primary.repository.methodsetup.MethodRepository;
import com.agaram.eln.primary.repository.usermanagement.LSuserMasterRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Date;


/**
 * This Service class holds methods that is used to invoke the respective classes
 * to save/ retrieve all types of sample split techniques for the specified method.
 * @author ATE153
 * @version 1.0.0
 * @since  20- Apr- 2020
 */
@Service
public class SampleSplitService {

	@Autowired
	MethodRepository methodRepo;	

	@Autowired
	LSuserMasterRepository userRepo;
	
	@Autowired
	SampleTextSplitService sampleTextSplitService;
	
	@Autowired
	SampleLineSplitService sampleLineSplitService;
	
	@Autowired	
	SampleExtractService sampleExtractService;

	@Autowired
	LScfttransactionRepository lscfttransactionrepo;
	
	/**
	 * This method receives input details from the controller that contains all information
	 * relevant to SampleTextSplit, SampleLineSplit and SampleExtract. With this input, it invokes the method
	 * that returns the saved detail list after saving techniques through its appropriate classes.
	 * It also records audit trail for all the saved entries based on whether audit trail is enabled for the site.
	 * @param request [HttpServletRequest] Request object to ip address of remote client
	 * @param mapObject [Map] object holding details of site, comments, sample split techniques, ...
	 * @return response object with updated 'Method' detail.
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@Transactional
	public ResponseEntity<Object> saveSampleSplit(final HttpServletRequest request, final Map<String, Object> mapObject)
	{		
		final ObjectMapper mapper = new ObjectMapper();
		
		final Boolean saveAuditTrail = mapper.convertValue(mapObject.get("saveAuditTrail"), Boolean.class);
		final LSSiteMaster site = mapper.convertValue(mapObject.get("site"), LSSiteMaster.class);
		
		String someValue =  (String) mapObject.get("doneByUserKey");
		
		final int doneByUserKey = Integer.parseInt(someValue);
//		final Page page = mapper.convertValue(mapObject.get("modulePage"), Page.class);
		final int methodKey = (Integer) mapObject.get("methodKey");
		final String comments = (String) mapObject.get("comments");
		
		
		
		final Map<String, SampleTextSplit> textSplitMap = mapper.convertValue(mapObject.get("sampleTextSplitList"), Map.class);		
		final Map<String, SampleLineSplit> lineSplitMap = mapper.convertValue(mapObject.get("sampleLineSplitList"), Map.class);		
		final Map<String, SampleExtract> sampleExtractMap = mapper.convertValue(mapObject.get("sampleExtractList"), Map.class);
	
		
//		final Collection<SampleTextSplit> textSplitMap1 = textSplitMap.values();
//		final List<SampleTextSplit> listToSave = mapper.convertValue(textSplitList1, 
//			  	new TypeReference<List<SampleTextSplit>>() {});		
//		
//		final List<Integer> idList = new ArrayList<Integer>();
//		
//		listToSave.forEach(item->{
//			
//		}
		
		final LSuserMaster createdUser = getCreatedUserByKey(doneByUserKey);
		final Optional<Method> methodByKey = methodRepo.findByMethodkeyAndStatus(methodKey, 1);
		if (methodByKey.isPresent())
		{
			final Method method = methodByKey.get();
			mapObject.put("createdUser", createdUser);
			
			final Map<String, Object> savedObjectMap = saveSampleSplitTechniques(createdUser, textSplitMap, lineSplitMap, sampleExtractMap, method, mapper);			
	
		    Integer sampleSplit = null; 
			
		    Date currentDateTime = new Date();
		   
		    if(((List<SampleTextSplit>)savedObjectMap.get("TextListAfterSave")).isEmpty() 
		    		&& ((List<SampleLineSplit>)savedObjectMap.get("LineListAfterSave")).isEmpty()
		    		&& ((List<SampleExtract>)savedObjectMap.get("ExtractListAfterSave")).isEmpty()) {
		    	sampleSplit = 0;
		    }
		    else {
//		    	sampleSplit = 1;
		    	List<SampleTextSplit> textList = (List<SampleTextSplit>) savedObjectMap.get("TextListAfterSave");
		    	List<SampleLineSplit> lineList = (List<SampleLineSplit>)savedObjectMap.get("LineListAfterSave");
		    	List<SampleExtract> extractList = (List<SampleExtract>)savedObjectMap.get("ExtractListAfterSave");
		    	
		    	List<SampleTextSplit> textCountList = textList.stream().filter(item-> item.getStatus() == 1).collect(Collectors.toList());
		    	List<SampleLineSplit> lineCountList = lineList.stream().filter(item->item.getStatus() == 1).collect(Collectors.toList());
		    	List<SampleExtract> extractCountList = extractList.stream().filter(item->item.getStatus() == 1).collect(Collectors.toList());
		    	
		    	
		    	if (textCountList.size() == 0 && lineCountList.size() == 0 && extractCountList.size() == 0) {
		    		sampleSplit = 0;
		    	}
		    	else {
		    		sampleSplit = 1;
		    	}		    	
		    }
		    
		    Method savedMethod = method;
		    if(sampleSplit != method.getSamplesplit()){
		    	method.setSamplesplit(sampleSplit);
		    	savedMethod = methodRepo.save(method);
		    } 
		    
		    if (saveAuditTrail)
			{
		    	final Map<String, String> xmlMap = getXMLData(savedObjectMap);
		    	String textXML = (String) xmlMap.get("textXML");
		    	String lineXML = (String) xmlMap.get("lineXML");
		    	String extractXML = (String) xmlMap.get("extractXML");
		    	
		    	final StringBuffer xmlDataBuffer = new StringBuffer();	
		    	if (textXML.length() != 0 && textXML.contains("<?xml")){
					textXML = textXML.substring(textXML.indexOf("?>")+2);
					textXML = textXML.replace("<sampletextsplits>", "").replace("</sampletextsplits>", "");									
				}
				if (lineXML.length() != 0 && lineXML.contains("<?xml")){
					lineXML = lineXML.substring(lineXML.indexOf("?>")+2);
					lineXML = lineXML.replace("<samplelinesplits>", "").replace("</samplelinesplits>", "");
					
				}
				if (extractXML.length() != 0 && extractXML.contains("<?xml")){
					extractXML = extractXML.substring(extractXML.indexOf("?>")+2);
					extractXML = extractXML.replace("<sampleextracts>", "").replace("</sampleextracts>", "");
				}
				xmlDataBuffer.append("<?xml version=\"1.0\" encoding=\"UTF-16\"?><combinedxml>" + textXML + lineXML + 
						extractXML + "</combinedxml>");				
				
//				String message = "";
//				String action = "Edit";
//				String actionType = EnumerationInfo.CFRActionType.USER.getActionType();
//				if (comments ==  null || comments.length() == 0)
//				{
//					actionType = EnumerationInfo.CFRActionType.SYSTEM.getActionType();
//					action = "Create";
//				}
//				else
//				{
//					message = comments;
//				}
//				cfrTransService.saveCfrTransaction(page, actionType, action, message, 
//							site, xmlDataBuffer.toString(), createdUser, request.getRemoteAddr());

				
				LScfttransaction LScfttransaction = new LScfttransaction();
				LScfttransaction.setActions("Insert");
				LScfttransaction.setComments("Sample Split Done");
				LScfttransaction.setLssitemaster(site.getSitecode());
				LScfttransaction.setLsuserMaster(doneByUserKey);
				LScfttransaction.setManipulatetype("View/Load");
				LScfttransaction.setModuleName("Method Master");
				LScfttransaction.setUsername(createdUser.getUsername());

				LScfttransaction.setTransactiondate(currentDateTime);
				LScfttransaction.setTableName("SampleExtract");
				LScfttransaction.setSystemcoments("System Generated");
				
				lscfttransactionrepo.save(LScfttransaction);
				
			}
			return new ResponseEntity<>(savedMethod, HttpStatus.OK);	
		}
		else
		{
		   //Invalid methodkey
//		   if (saveAuditTrail) {				
//				cfrTransService.saveCfrTransaction(page, EnumerationInfo.CFRActionType.SYSTEM.getActionType(),
//						"Create", "Create Failed - Method Not Found", site, "", 
//						createdUser, request.getRemoteAddr());
//  		    }			
			return new ResponseEntity<>("Create Failed - Method Not Found", HttpStatus.NOT_FOUND);
		}		 
	}	
	/**
	 * This method is used to save all SampleSplitting techniques by invoking its appropriate class.
	 * @param createdUser [CreatedUser] entity referring the user done the task.
	 * @param textSplitMap [Map] object holding details of SampleTextSplit entities
	 * @param lineSplitMap [Map] object holding details of SampleLineSplit entities
	 * @param sampleExtractMap [Map] object holding details of SampleExtract entities
	 * @param method [Method] object for which SampleSplitting is done
	 * @param mapper [ObjectMapper] to convert list of Object to List of appropriate entities
	 * @return map object holding details of all types of SampleSplitTechniques before and after saving
	 * into database
	 */
	public Map<String, Object> saveSampleSplitTechniques(final LSuserMaster createdUser,
			final Map<String, SampleTextSplit> textSplitMap, final Map<String, SampleLineSplit> lineSplitMap, 
			final Map<String, SampleExtract> sampleExtractMap, final Method method, final ObjectMapper mapper)
	{
		final Map<String, List<SampleTextSplit>> textMapObject = sampleTextSplitService.saveSampleTextSplit(textSplitMap, mapper, 
	    		createdUser, method); 
		final List<SampleTextSplit> savedTextList = textMapObject.get("TextListAfterSave");		
		    
		final Map<String, List<SampleLineSplit>> lineMapObject = sampleLineSplitService.saveSampleLineSplit(lineSplitMap, mapper, 
				createdUser, method); 
		final List<SampleLineSplit> savedLineList = lineMapObject.get("LineListAfterSave");	
	    
	    final Map<String, List<SampleExtract>> extractMapObject = sampleExtractService.saveSampleExtract(sampleExtractMap, mapper, 
	    		createdUser, method, savedTextList, savedLineList);

	    final Map<String, Object> returnObject = new HashMap<String, Object>();
	    returnObject.putAll(textMapObject);
	    returnObject.putAll(lineMapObject);
	    returnObject.putAll(extractMapObject);
		    
		return returnObject;
	}
	
	/**
	 * This method invokes the relevant methods from SampleTextSplitService, SampleLineSplitService and SampleExtractService 
	 * that convert the pojo object to XML data.
	 * @param mapObject [Map] object holding entity details before and updating the entity
	 * @return map object holding xml data of pojo object
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getXMLData(final Map<String, Object> mapObject) {
		
		final List<SampleTextSplit>  textListBeforeSave =  (List<SampleTextSplit>)mapObject.get("TextListBeforeSave");
		final List<SampleTextSplit> savedTextList =  (List<SampleTextSplit>)mapObject.get("TextListAfterSave");
		
		final List<SampleLineSplit> lineListBeforeSave = (List<SampleLineSplit>)mapObject.get("LineListBeforeSave");
	    final List<SampleLineSplit> savedLineList = (List<SampleLineSplit>)mapObject.get("LineListAfterSave");
	    
	    final List<SampleExtract> savedExtractList = (List<SampleExtract>)mapObject.get("ExtractListAfterSave");
	    final List<SampleExtract>  extractListBeforeSave = (List<SampleExtract>) mapObject.get("ExtractListBeforeSave");

		final Map<String, String> xmlMap = new HashMap<String, String>();			 
		String textXML =  "";				
		if (textListBeforeSave != null && savedTextList != null)
		{
			textXML =  sampleTextSplitService.convertSampleTextSplitListToXML(textListBeforeSave, savedTextList);
		}
		
		String lineXML = "";
		if (lineListBeforeSave != null && savedLineList != null)
		{
			lineXML = sampleLineSplitService.convertSampleLineSplitListToXML(lineListBeforeSave, savedLineList);
		}
	 	
	    String extractXML = "";
	    if (extractListBeforeSave != null && savedExtractList != null) {
	    	extractXML = sampleExtractService.convertSampleExtractListToXML(extractListBeforeSave, savedExtractList);
	    }
	    
	    xmlMap.put("textXML", textXML);
	    xmlMap.put("lineXML", lineXML);
	    xmlMap.put("extractXML", extractXML);

	    return xmlMap;
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
