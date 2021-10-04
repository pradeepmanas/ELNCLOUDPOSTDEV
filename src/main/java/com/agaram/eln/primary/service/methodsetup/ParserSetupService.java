package com.agaram.eln.primary.service.methodsetup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.agaram.eln.primary.model.methodsetup.CommonFunction;
import com.agaram.eln.primary.model.methodsetup.Method;
import com.agaram.eln.primary.model.methodsetup.ParserBlock;
import com.agaram.eln.primary.model.methodsetup.ParserField;
import com.agaram.eln.primary.model.methodsetup.ParserIgnoreChars;
import com.agaram.eln.primary.model.methodsetup.ParserTechnique;
import com.agaram.eln.primary.model.methodsetup.SampleExtract;
import com.agaram.eln.primary.model.methodsetup.SampleLineSplit;
import com.agaram.eln.primary.model.methodsetup.SampleTextSplit;
import com.agaram.eln.primary.model.methodsetup.SubParserField;
import com.agaram.eln.primary.model.methodsetup.SubParserTechnique;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.repository.methodsetup.MethodRepository;
import com.agaram.eln.primary.repository.methodsetup.ParserFieldRepository;
import com.agaram.eln.primary.repository.methodsetup.ParserIgnoreCharsRepository;
import com.agaram.eln.primary.repository.methodsetup.ParserMethodRepository;
import com.agaram.eln.primary.repository.usermanagement.LSuserMasterRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
/**
 * This Service class holds methods that is used to invoke the respective classes
 * to save/ retrieve all types of ParserSetup techniques for the specified method.
 * @author ATE153
 * @version 1.0.0
 * @since  21- Apr- 2020
 */
@Service
public class ParserSetupService {
		
	@Autowired
	SampleTextSplitService textSplitService;
	
	@Autowired
	SampleLineSplitService lineSplitService;
	
	@Autowired
	SampleExtractService sampleExtractService;
	
	@Autowired
	MethodService methodService;
	
	@Autowired
	ParserMethodRepository parserRepo;
	
	@Autowired
	LSuserMasterRepository userRepo;
	
	@Autowired
	ParserBlockService parserBlockService;
	
	@Autowired
	ParserFieldService parserFieldService;
	
	@Autowired
	ParserTechniqueService parserTechniqueService;
	
	@Autowired
	SubParserFieldService subParserFieldService;
	
	@Autowired
	SubParserTechniqueService subParserTechniqueService;
	
	@Autowired
	MethodRepository methodRepo;	

	@Autowired
	ParserIgnoreCharsRepository parserIgnoreRepo;
	
	@Autowired
	ParserFieldRepository parserFieldRepo;
	
	/**
	 * This method is used to generate blockwise data from the raw data file.
	 * If sample splitting is done for that Method, all the techniques of SampleSplit has to be applied
	 * and then the extracted blocks has to be returned to provide as input for ParserSetup or as
	 * input to evaluate parser.
	 * If this output is to be used to evaluate parser then the extracted block should holds details of all
	 * of its  extracted samples.
	 * If this output is to be used for ParserSetup, then first sample detail of each of the extracted block
	 * is sufficient.
	 * @param methodKey [int] primary key of Method object for which extracted blockwise data
	 * 					is to be fetched
	 * @param evaluateParser [boolean] true - extracted block is used to evaluate parser, 
	 * 								  false - extracted block is used as input for ParserSetup
	 * @param rawDataContent [String] raw data source to be used 
	 * @return map object holding block name and list of extracted blockwise data.
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public ResponseEntity<Object> getParserData(final int methodKey, final Boolean evaluateParser,
			final String rawDataContent)
	{		
		final Method method = (Method)methodService.findById(methodKey).getBody();
		
		String rawDataText = "";
		if (rawDataContent == null || rawDataContent.isEmpty()) {
			rawDataText = methodService.getFileData(method.getInstrawdataurl());   
		}
		else {
			rawDataText = rawDataContent;   
		}		   
		
		if (method.getSamplesplit() == null || method.getSamplesplit() == 0) {
			final Map <String, Object> extractedBlock = new HashMap<String, Object>();
            	 extractedBlock.put("TextBlock_1", "$$BEGINSAMPLE$$\n"+rawDataText+"\n$$ENDSAMPLE$$");			

			 return new ResponseEntity<>(extractedBlock, HttpStatus.OK);
		}
		else {
			
			final StringBuffer dataBuffer = new StringBuffer();
			dataBuffer.append("$$BEGINSAMPLE$$\n");
			dataBuffer.append(rawDataText);
			dataBuffer.append("\n$$ENDSAMPLE$$");
			
			String rawData = dataBuffer.toString();	
		
			final List<SampleTextSplit> textSplitList = (List<SampleTextSplit>)textSplitService.getSampleTextSplitByMethod(methodKey).getBody();
			final List<SampleLineSplit> lineSplitList = (List<SampleLineSplit>)lineSplitService.getSampleLineSplitByMethod(methodKey).getBody();
			final List<SampleExtract> extractList = (List<SampleExtract>)sampleExtractService.getSampleExtractByMethod(methodKey).getBody();
					
			final CommonFunction commonFunction = new CommonFunction();		
			
			final List<SampleTextSplit> removeSTSList = new ArrayList<SampleTextSplit>(); 
			final List<SampleLineSplit> removeSLSList	= new ArrayList<SampleLineSplit>();
			final List<SampleTextSplit> extractSTSList = new ArrayList<SampleTextSplit>(); 
			final List<SampleLineSplit> extractSLSList	= new ArrayList<SampleLineSplit>();
						
	        if (!textSplitList.isEmpty()) {
	        	textSplitList.forEach(item -> {
	        		if (item.getRemoveorextracttext() == 0 )
	        			removeSTSList.add(item);
	        			else
	        				extractSTSList.add(item);         			
	        	});
	        }
	        if (!lineSplitList.isEmpty()) {
	        	lineSplitList.forEach(item -> {
	        		if (item.getRemoveorextractlines() == 0 )
	        			removeSLSList.add(item);
	        		else
	        			extractSLSList.add(item);
	        	});
	        }
	     
	        if (removeSTSList.size() > 0 || removeSLSList.size() > 0) {
	            rawData = commonFunction.removeRawData(rawData, removeSTSList, removeSLSList);
	        }
	        String removedData = rawData;
	        final Map <String, Object> extractedBlock = new HashMap<String, Object>();
	        if (!extractSTSList.isEmpty()){
	        	for (SampleTextSplit item :extractSTSList) {
	        		//final List<String> rawDataList = new ArrayList<String>();
					final String extractedData = "$$BEGINSAMPLE$$\n"+commonFunction.extractRawDataBySTS(removedData,item, dataBuffer.toString())+"\n$$ENDSAMPLE$$";
	                //rawDataList.add(extractedData);
	        		extractedBlock.put(item.getExtractblock(), extractedData);
	            }
	        }
	       
	        if (!extractSLSList.isEmpty()){
	        	for (SampleLineSplit item :extractSLSList) {	        		
	        		final String extractedData = "$$BEGINSAMPLE$$\n"+commonFunction.extractRawDataBySLS(removedData,item, dataBuffer.toString())+"\n$$ENDSAMPLE$$";
	        		
	        		extractedBlock.put(item.getExtractblock(), extractedData);
	            }
	        }	      	
	    
	        if (!extractList.isEmpty()) {        	
	        
	            extractList.forEach(item -> {            	
	            	 final String extractBlock = item.getSampletextsplit() != null ? item.getSampletextsplit().getExtractblock()
	                         : item.getSamplelinesplit().getExtractblock();
	            	
	            	 List<String> rawDataList = new ArrayList<String>();
	                 if( item.getExtracttextorlines() == 0){
	                     rawDataList = commonFunction.applyMatchTextExtract((String)extractedBlock.get(extractBlock), item);
	                 }
	                 else { 
	                	 rawDataList = commonFunction.applyAbsoluteLinesExtract((String)extractedBlock.get(extractBlock), item);
	                 }
	                
	                 if (evaluateParser) {
	                	extractedBlock.replace(extractBlock, rawDataList);
	                 }
	                 else {
	                	 extractedBlock.replace(extractBlock, rawDataList.get(0));
	                 }
	            });
	        }
         
	        return new ResponseEntity<>(extractedBlock, HttpStatus.OK);
		}
	}	
	
	
	/**
	 * This method is used to fetch the list of ParserMethod entities relating to SubParserTechnique.
	 * @return response object with list of ParserMethod entities
	 */
	@Transactional
	public ResponseEntity<Object> getSubParserMethod(){
		return  new ResponseEntity<>(parserRepo.findByParsermethodtype(2), HttpStatus.OK);
	}
	
	/**
	 * This method receives input details from the controller that contains all information
	 * relevant to Parser Setup tables. With this input, it invokes the method
	 * that returns the saved detail list after saving entries through its appropriate classes.
	 * It also records audit trail for all the saved entries based on whether audit trail is enabled for the site.
	 * @param request [HttpServletRequest] Request object to ip address of remote client
	 * @param mapObject [Map] object holding details relevant to save Parser setup entries
	 * @return response object with updated 'Method' detail.
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	@Transactional
	public ResponseEntity<Object> saveParserData(final HttpServletRequest request, final Map<String, Object> mapObject) {
		final ObjectMapper mapper = new ObjectMapper();
		
		final Boolean saveAuditTrail = mapper.convertValue(mapObject.get("saveAuditTrail"), Boolean.class);
		final LSSiteMaster site = mapper.convertValue(mapObject.get("site"), LSSiteMaster.class);
		String someValue =  (String) mapObject.get("doneByUserKey");
		
		final int doneByUserKey = Integer.parseInt(someValue);
//		final Page page = mapper.convertValue(mapObject.get("modulePage"), Page.class);
		final int methodKey = (Integer) mapObject.get("methodKey");
		final String comments = (String) mapObject.get("comments"); 
	
		final List<ParserBlock> parserBlockList = mapper.convertValue(mapObject.get("parserBlockList"),List.class);		
		final List<ParserField> parserFieldList = mapper.convertValue(mapObject.get("parserFieldList"),List.class);		
		final List<ParserTechnique> parserTechniqueList = mapper.convertValue(mapObject.get("parserTechniqueList"),List.class);
		final List<SubParserField> subParserFieldList = mapper.convertValue(mapObject.get("subParserFieldList"),List.class);	
		final List<SubParserTechnique> subParserTechniqueList = mapper.convertValue(mapObject.get("subParserTechniqueList"),List.class);		
		
		final LSuserMaster createdUser = getCreatedUserByKey(doneByUserKey);
		final Method method = (Method)methodService.findById(methodKey).getBody();
		
		final Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("parserBlockList", parserBlockList);
		inputMap.put("parserFieldList", parserFieldList);
		inputMap.put("parserTechniqueList", parserTechniqueList);
		inputMap.put("subParserFieldList", subParserFieldList);
		inputMap.put("subParserTechniqueList", subParserTechniqueList);
		
		final Map<String, Object> savedParserMap = saveParserSetupEntities(method, createdUser, mapper, inputMap);
		Integer parser = null; 
	  
		if(((List<ParserBlock>)savedParserMap.get("ParserBlockListAfterSave")).isEmpty()) {
			parser = 0;
	    }
	    else {
//	    	parser = 1;
	    	final List<ParserBlock> blockList = (List<ParserBlock>)savedParserMap.get("ParserBlockListAfterSave");
	    	final List<Integer> blockKeyList = blockList.stream().map(item->item.getParserblockkey()).collect(Collectors.toList());
	    	final List<ParserField> fieldList = parserFieldRepo.findByParserblockInAndStatus(blockList, 1);
			if (fieldList.isEmpty()) {
				parser = 0;
			}
			else {
				parser = 1;
			}
	    }
	    
	    Method savedMethod = method;
	    if(parser != method.getParser()){
	    	method.setParser(parser);
	    	savedMethod = methodRepo.save(method);
	    } 
		    
		if (saveAuditTrail)
		{
			final StringBuffer xmlDataBuffer = new StringBuffer();				 
			final Map<String, String> xmlMap = getXMLData(savedParserMap);
	    	
			String blockXML = (String) xmlMap.get("blockXML");
	    	String parserFieldXML = (String) xmlMap.get("parserFieldXML");
	    	String parserTechniqueXML = (String) xmlMap.get("parserTechniqueXML");
	    	String subParserFieldXML = (String) xmlMap.get("subParserFieldXML");
	    	String subParserTechXML = (String) xmlMap.get("subParserTechXML");
	    			    
			if (blockXML.length() != 0 && blockXML.contains("<?xml")){
				blockXML = blockXML.substring(blockXML.indexOf("?>")+2);
				blockXML = blockXML.replace("<parserblocks>", "").replace("</parserblocks>", "");									
			}
			if (parserFieldXML.length() != 0 && parserFieldXML.contains("<?xml")){
				parserFieldXML = parserFieldXML.substring(parserFieldXML.indexOf("?>")+2);
				parserFieldXML = parserFieldXML.replace("<parserfields>", "").replace("</parserfields>", "");
				
			}
			if (parserTechniqueXML.length() != 0 && parserTechniqueXML.contains("<?xml")){
				parserTechniqueXML = parserTechniqueXML.substring(parserTechniqueXML.indexOf("?>")+2);
				parserTechniqueXML = parserTechniqueXML.replace("<parsertechniques>", "").replace("</parsertechniques>", "");
			}
			if (subParserTechXML.length() != 0 && subParserTechXML.contains("<?xml")){
				subParserTechXML = subParserTechXML.substring(subParserTechXML.indexOf("?>")+2);
				subParserTechXML = subParserTechXML.replace("<subparsertechniques>", "").replace("</subparsertechniques>", "");
			}
			if (subParserFieldXML.length() != 0 && subParserFieldXML.contains("<?xml")){
				subParserFieldXML = subParserFieldXML.substring(subParserFieldXML.indexOf("?>")+2);
				subParserFieldXML = subParserFieldXML.replace("<subparserfields>", "").replace("</subparserfields>", "");
			}
						
			xmlDataBuffer.append("<?xml version=\"1.0\" encoding=\"UTF-16\"?><combinedxml>" + blockXML + parserFieldXML + 
					parserTechniqueXML + subParserTechXML + subParserFieldXML + "</combinedxml>");				
			
			String message = "";
			String action = "Edit";
//			String actionType = EnumerationInfo.CFRActionType.USER.getActionType();
//			if (comments ==  null || comments.length() == 0)
//			{
//				actionType = EnumerationInfo.CFRActionType.SYSTEM.getActionType();
//				action = "Create";
//			}
//			else
//			{
//				message = comments;
//			}
//			cfrTransService.saveCfrTransaction(page, actionType, action, message, 
//						site, xmlDataBuffer.toString(), createdUser, request.getRemoteAddr());
		}
		return new ResponseEntity<>(method, HttpStatus.OK);
	}
	
	/**
	 * This method is used to save all Parser setup entries by invoking its appropriate class.
	 * @param method [Method] object for which Parsing is done
	 * @param createdUser [CreatedUser] entity referring the user done the task.
	 * @param mapper [ObjectMapper] to convert list of Object to List of appropriate entities
	 * @param mapObject [Map] object holding details of list of entities(ParserBlock,ParserField, ParserTechnique,
	 * SubParserField and SubParserTechnique] to be saved
	 * @return map object holding details of all types of SampleSplitTechniques before and after saving
	 * into database
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> saveParserSetupEntities(final Method method, final LSuserMaster createdUser, final ObjectMapper mapper, 
			Map<String, Object> mapObject) {
		
		final Map<String, Object> returnObject  = new HashMap<String, Object>();
		final Map<String, List<ParserBlock>> blockMapObject = parserBlockService.saveParserBlock((List<ParserBlock>)mapObject.get("parserBlockList"), mapper, createdUser, method); 
		final List<ParserBlock> savedBlockList = blockMapObject.get("ParserBlockListAfterSave");	  	    			
		
		final Map<String, ParserBlock> blockMap = new HashMap<String, ParserBlock>();
		savedBlockList.forEach(item->{			
			blockMap.put(item.getParserblockname(), item);
		});
	
		final Map<String, List<ParserField>> fieldMapObject = parserFieldService.saveParserField((List<ParserField>)mapObject.get("parserFieldList"), mapper, createdUser, blockMap); 
		final List<ParserField> savedParserFieldList = fieldMapObject.get("ParserFieldListAfterSave");	  	    			
				
		final Map<String, ParserField> fieldMap = new HashMap<String, ParserField>();
		savedParserFieldList.forEach(item->{			
			fieldMap.put(item.getFieldid(), item);
		});		

		final Map<String, List<ParserTechnique>> techniqueMapObject = parserTechniqueService.saveParserTechnique((List<ParserTechnique>)mapObject.get("parserTechniqueList"), mapper, createdUser, fieldMap); 
		final Map<String, List<SubParserField>> subParserFieldMapObject = subParserFieldService.saveSubParserField((List<SubParserField>)mapObject.get("subParserFieldList"), mapper, createdUser, fieldMap); 
		final Map<String, List<SubParserTechnique>> subParserTechniqueMapObject = subParserTechniqueService.saveSubParserTechnique((List<SubParserTechnique>)mapObject.get("subParserTechniqueList"), mapper, createdUser, fieldMap); 
				
		
		returnObject.putAll(blockMapObject);
		returnObject.putAll(fieldMapObject);
		returnObject.putAll(techniqueMapObject);
		returnObject.putAll(subParserFieldMapObject);
		returnObject.putAll(subParserTechniqueMapObject);
		
		return returnObject;
	}
	
	/**
	 * This method invokes the relevant methods from ParserBlock, ParserField, ParserTechnique, SubParserField, SubParserTechnique 
	 * that convert the pojo object to XML data.
	 * @param mapObject [Map] object holding entity details before and updating the entity
	 * @return map object holding xml data of pojo object
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getXMLData(final Map<String, Object> mapObject){
		
		final List<ParserBlock> savedBlockList = (List<ParserBlock>)mapObject.get("ParserBlockListAfterSave");	  	    			
		final List<ParserBlock>  blockListBeforSave = (List<ParserBlock>)mapObject.get("ParserBlockListBeforeSave");
	   
		final List<ParserField> savedParserFieldList = (List<ParserField>)mapObject.get("ParserFieldListAfterSave");	  	    			
		final List<ParserField>  fieldListBeforSave = (List<ParserField>)mapObject.get("ParserFieldListBeforeSave");
		
		final List<ParserTechnique> savedParserTechniqueList = (List<ParserTechnique>)mapObject.get("ParserTechniqueListAfterSave");	  	    			
		final List<ParserTechnique>  techniqueListBeforSave = (List<ParserTechnique>)mapObject.get("ParserTechniqueListBeforeSave");
				
		final List<SubParserField> savedSubParserFieldList = (List<SubParserField>)mapObject.get("SubParserFieldListAfterSave");	  	    			
		final List<SubParserField>  subParserFieldListBeforSave = (List<SubParserField>)mapObject.get("SubParserFieldListBeforeSave");
				
		final List<SubParserTechnique> savedSubParserTechniqueList = (List<SubParserTechnique>)mapObject.get("SubParserTechniqueListAfterSave");	  	    			
		final List<SubParserTechnique>  subParserTechniqueListBeforSave = (List<SubParserTechnique>)mapObject.get("SubParserTechniqueListBeforeSave");
			
		String blockXML =  "";				
		if (blockListBeforSave != null && savedBlockList != null)
		{
			blockXML =  parserBlockService.convertParserBlockListToXML(blockListBeforSave, savedBlockList);
		}
		
		String parserFieldXML =  "";
		if (fieldListBeforSave != null && savedParserFieldList != null)
		{
			parserFieldXML =  parserFieldService.convertParserFieldListToXML(fieldListBeforSave, savedParserFieldList);
		}
		
		String parserTechniqueXML =  "";
		if (techniqueListBeforSave != null && savedParserTechniqueList != null)
		{
			parserTechniqueXML =  parserTechniqueService.convertParserTechniqueListToXML(techniqueListBeforSave, savedParserTechniqueList);
		}
		
		String subParserFieldXML =  "";
		if (subParserFieldListBeforSave != null && savedSubParserFieldList != null)
		{
			subParserFieldXML =  subParserFieldService.convertSubParserFieldListToXML(subParserFieldListBeforSave, savedSubParserFieldList);
		}
		
		String subParserTechXML =  "";
		if (subParserTechniqueListBeforSave != null && savedSubParserTechniqueList  != null)
		{
			subParserTechXML =  subParserTechniqueService.convertSubParserTechniqueListToXML(subParserTechniqueListBeforSave, savedSubParserTechniqueList );
		}	
		
		final Map<String, String> xmlMap = new HashMap<String, String>();
		xmlMap.put("blockXML", blockXML);
		xmlMap.put("parserFieldXML", parserFieldXML);
		xmlMap.put("parserTechniqueXML", parserTechniqueXML);
		xmlMap.put("subParserFieldXML", subParserFieldXML);
		xmlMap.put("subParserTechXML", subParserTechXML);
		
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
	
	/**
	 * This method is used to retrieve list of details from  ParserBlock, ParserField, ParserTechnique, SubParserField, 
	 * SubParserTechnique relevant to ParserSetup for the specified 'Method'.
	 * @param methodKey [int] primary key of Method object for which the details are to be fetched
	 * @return map object holding details of ParserSetup relevant entities
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public ResponseEntity<Object> getParserFieldTechniqueList(final int methodKey){
		
		final List<ParserBlock> parserBlockList = (List<ParserBlock>) parserBlockService.getParserBlockByMethodKey(methodKey).getBody();
		final List<ParserField> parserFieldList = (List<ParserField>)parserFieldService.getParserFieldByMethodKey(methodKey).getBody();
		final List<ParserTechnique> parserTechniqueList = (List<ParserTechnique>)parserTechniqueService.getParserTechniqueByMethodKey(methodKey).getBody();
		final List<SubParserField> subParserFieldList = (List<SubParserField>)subParserFieldService.getSubParserFieldByMethodKey(methodKey).getBody();
		final List<SubParserTechnique> subParserTechniqueList = (List<SubParserTechnique>)subParserTechniqueService.getSubParserTechniqueByMethodKey(methodKey).getBody();
        final List<ParserIgnoreChars> ignoreList = parserIgnoreRepo.findAll();		
		   
		final Map<String, Object> returnObject = new HashMap<String, Object>();
		returnObject.put("ParserBlockList", parserBlockList);
		returnObject.put("ParserFieldList", parserFieldList);
		returnObject.put("ParserTechniqueList", parserTechniqueList);
		returnObject.put("SubParserFieldList", subParserFieldList);
		returnObject.put("SubParserTechniqueList", subParserTechniqueList);	
		returnObject.put("PDIgnoreCharsList", ignoreList);

		return new ResponseEntity<> (returnObject, HttpStatus.OK);
	}

}
