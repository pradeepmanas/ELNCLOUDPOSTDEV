package com.agaram.eln.primary.service.methodsetup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.agaram.eln.primary.model.methodsetup.CommonFunction;
import com.agaram.eln.primary.model.methodsetup.CustomField;
import com.agaram.eln.primary.model.methodsetup.GeneralField;
import com.agaram.eln.primary.model.methodsetup.Method;
import com.agaram.eln.primary.model.methodsetup.MethodFieldTechnique;
import com.agaram.eln.primary.model.methodsetup.ParserBlock;
import com.agaram.eln.primary.model.methodsetup.ParserField;
import com.agaram.eln.primary.model.methodsetup.ParserIgnoreChars;
import com.agaram.eln.primary.model.methodsetup.ParserTechnique;
import com.agaram.eln.primary.model.methodsetup.SubParserField;
import com.agaram.eln.primary.model.methodsetup.SubParserTechnique;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.repository.methodsetup.MethodRepository;

/**
 * This Service class holds methods that is used to validate the Parser result
 * to save/ retrieve all types of sample split techniques for the specified method.
 * @author ATE153
 * @version 1.0.0
 * @since  20- Apr- 2020
 */
@Service
public class EvaluateParserService {
	
	@Autowired
	MethodRepository methodRepo;
	
	@Autowired
	ParserSetupService parserSetupService;
	
	@Autowired
	MethodService methodService;
		
	@Autowired
	GeneralFieldService generalFieldService;
	
	@Autowired
	CustomFieldService customFieldService;
	
   /**
    * This method is used  to retrieve list of Method entities for the specified site
    * for which parsing is done. It also retrieves list of instrument fields, custom fields and 
    * general fields for the first Method available in the list.
    * @param site [Site] object for which list is to be fetched
    * @return list of Method entities
    */
   @SuppressWarnings({ "unchecked" })
   @Transactional
   public ResponseEntity<Object> getLabSheetMethodList(final LSSiteMaster site){
	   
	   final List<Method> methodList = methodRepo.findByParserAndSiteAndStatus(1, site, 1);
	   final Map<String, Object> returnObject = new HashMap<String, Object>();
	   returnObject.put("PDMethodList", methodList);
	   
	   if (!methodList.isEmpty()) {
		 
	   	   final Method method = methodList.get(0);	   	  	 
	   	   
	   	   final Map<String, Object> methodData =  (Map<String, Object>)getMethodFieldList(method.getMethodkey(), site, "").getBody();
      	   returnObject.putAll(methodData);
	   	   
		   final List<GeneralField> generalFieldList = (List<GeneralField>)generalFieldService.getGeneralFieldBySite(site).getBody();
		   returnObject.put("PDGeneralFieldList", generalFieldList);
		  
	   }
	   return new ResponseEntity<>(returnObject, HttpStatus.OK);
   }
	   
	   /**
	    * This method is used to retrieve list of instrument fields, custom fields and general fields
	    * for the specified Method entity.
	    * @param methodKey [int] primary key of Method Object
	    * @param site [Site] object for which the fields are to be fetched
	    * @param rawData [String] raw data source to be used in case of stringcontent
	    * @return list of fields relating the method and the site.
	    */
	   @SuppressWarnings({ "unchecked" })
	   @Transactional
	   public ResponseEntity<Object> getMethodFieldList(final int methodKey, final LSSiteMaster site, 
			   final String rawData){
		   
		   final Optional<Method> methodByKey = methodRepo.findByMethodkeyAndStatus(methodKey, 1);
		   if (methodByKey.isPresent()) {
			   
			   final Map<String, Object> parserMapObject = (Map<String, Object>)parserSetupService.getParserFieldTechniqueList(methodByKey.get().getMethodkey()).getBody();
			   
			   final List<ParserBlock> parserBlockList = (List<ParserBlock>) parserMapObject.get("ParserBlockList");
			   final List<ParserField> parserFieldList = (List<ParserField>) parserMapObject.get("ParserFieldList");
			   final List<ParserTechnique> parserTechniqueList = (List<ParserTechnique>) parserMapObject.get("ParserTechniqueList");
			   final List<SubParserField> subParserFieldList = (List<SubParserField>)parserMapObject.get("SubParserFieldList");
			   final List<SubParserTechnique> subParserTechniqueList = (List<SubParserTechnique>)parserMapObject.get("SubParserTechniqueList");	
	   		   final List<ParserIgnoreChars> ignoreList = (List<ParserIgnoreChars>) parserMapObject.get("PDIgnoreCharsList");
			   
			   final Map<String, Object> returnObject = new HashMap<String, Object>();
			   final Map<String, List<MethodFieldTechnique>> blockMap = new HashMap<String, List<MethodFieldTechnique>>();
			   if (parserBlockList.size() > 0){				  
				   for (final ParserBlock parserBlock: parserBlockList) {		        		 
					  
					   final List<MethodFieldTechnique> fieldList = new ArrayList<MethodFieldTechnique>();
					  
					   for (final ParserField parserField: parserFieldList) {
						   if (parserField.getParserblock().equals(parserBlock))
						   {        
							   final List<ParserTechnique> fieldPTList = new ArrayList<ParserTechnique>();
							   for (ParserTechnique parserTechnique: parserTechniqueList) {
								   if(parserTechnique.getParserfield().equals(parserField)) {
									   fieldPTList.add(parserTechnique);
								    }
							   }
							   
							   final List<SubParserTechnique> fieldSPTList = new ArrayList<SubParserTechnique>();
							   for (SubParserTechnique subParserTechnique: subParserTechniqueList) {
								   if(subParserTechnique.getParserfield().equals(parserField)) {
									   fieldSPTList.add(subParserTechnique);
								    }
							   }
							   
							   final List<SubParserField> fieldSPFList = new ArrayList<SubParserField>();
							   for (SubParserField subParserField: subParserFieldList) {
								   if(subParserField.getParserfield().equals(parserField)) {
									   fieldSPFList.add(subParserField);
								    }
							   }
							   
							   final List<MethodFieldTechnique> subParserNameList = new ArrayList<MethodFieldTechnique>();
							   
							   for (final SubParserField subParserField :subParserFieldList) {
								   if (parserField.equals(subParserField.getParserfield())) {
									  
									   final MethodFieldTechnique fieldTech = new MethodFieldTechnique();
									   fieldTech.setFieldid(subParserField.getFieldid());
									   fieldTech.setFieldkey(subParserField.getSubparserfieldkey());
									   fieldTech.setFieldname(subParserField.getSubparserfieldname());
									   fieldTech.setFieldtype("SubParserField");
									   fieldTech.setParsertechniques(fieldPTList);
									   fieldTech.setSubparsertechniques(fieldSPTList);
									   fieldTech.setParserfield(parserField);
									   fieldTech.setSubparserfields(fieldSPFList);
									   
									   subParserNameList.add(fieldTech);
								   }
							   }
							   if (subParserNameList.isEmpty()) {		  
								   final MethodFieldTechnique fieldTech = new MethodFieldTechnique();
								   fieldTech.setFieldid(parserField.getFieldid());
								   fieldTech.setFieldkey(parserField.getParserfieldkey());
								   fieldTech.setFieldname(parserField.getParserfieldname());
								   fieldTech.setFieldtype("ParserField");
								   fieldTech.setParsertechniques(fieldPTList);
								   fieldTech.setSubparsertechniques(fieldSPTList);
								   fieldTech.setParserfield(parserField);
								   fieldTech.setSubparserfields(fieldSPFList);
								   
								   fieldList.add(fieldTech);
								   
							  }
							   else
							   {
								   fieldList.addAll(subParserNameList);							   
							   }        				 
						   }
					   }
					   blockMap.put(parserBlock.getParserblockname()+","+parserBlock.getParserblockkey(),fieldList);
					}
			   }
			   returnObject.put("PDInstFieldList", blockMap);
			   returnObject.put("PDMethod", methodByKey.get());
	   
			   final List<CustomField> customFieldList = (List<CustomField>) customFieldService.getCustomFieldByMethod(methodByKey.get()).getBody();
			   returnObject.put("PDCustomFieldList", customFieldList); 

			   final Map<String, List<List<MethodFieldTechnique>>> outputMap = parseDataForInstFields(blockMap, methodByKey.get().getMethodkey(), 
				   		rawData, ignoreList);
			   returnObject.put("PDInstFieldDataMap", outputMap);  		  
		   
			   return new ResponseEntity<>(returnObject, HttpStatus.OK);
		   }
		   else {
			   return new ResponseEntity<>("Method Not Found", HttpStatus.NOT_FOUND);
		   } 		    
	   }
	
	/**
	 * This method is used to used to evaluate the parser for the provided file content.
	 * @param methodKey [int] Method key from which the techniques are to applied
	 * @param site [Site] for which the evaluate parser is handled
	 * @param rawDataContent [String] raw data source to be used in case of stringcontent
	 * @return map object holding parsed data
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public ResponseEntity<Object> evaluateParser(final int methodKey, final LSSiteMaster site, final String rawDataContent)
	{
		final Optional<Method> methodByKey = methodRepo.findByMethodkeyAndStatus(methodKey, 1);				   
		if(methodByKey.isPresent()) 
		{			
			final Map<String, Object> parserObjectMap = (Map<String, Object>) getMethodFieldList(methodKey, site, rawDataContent).getBody();
		
			final Map<String, List<List<MethodFieldTechnique>>> parsedData = (Map<String, List<List<MethodFieldTechnique>>>) parserObjectMap.get("PDInstFieldDataMap");
			return new ResponseEntity<>(parsedData, HttpStatus.OK);
			
		}
		else {
			return new ResponseEntity<>("Method Not Found", HttpStatus.NOT_FOUND);
		}
	}
	
	/**
	 * This method is used to get the Parser techniques based on which the parser data is fetched for the
	 * instrument fields.
	 * @param blockMap [Map] object holding parser block details and its MethodFieldTechnique list
	 * @param methodKey [int] Method key from which the techniques are to applied
	 * @param rawDataContent [String] raw data source to be used in case of contenttype=stringcontent
	 * @param ignoreList [List] holding ParserIgnoreChars entity list that will be ignored in parsed data
	 * @return map object holding field id and its parsed data
	 */
	@SuppressWarnings("unchecked")
	public Map<String, List<List<MethodFieldTechnique>>> parseDataForInstFields(final Map<String, List<MethodFieldTechnique>> blockMap,
			final int methodKey, final String rawDataContent, final List<ParserIgnoreChars> ignoreList) {
		
		final Map <String, Object> extractedBlock =  (Map <String, Object>) parserSetupService.getParserData(
				methodKey, true, rawDataContent).getBody();
		
		final Map<String, List<List<MethodFieldTechnique>>> outputDataMap =  new HashMap<String, List<List<MethodFieldTechnique>>>();
		
		for(Map.Entry<String, List<MethodFieldTechnique>> blockFieldMap :blockMap.entrySet()) 
		{
			final String blockNameKey = blockFieldMap.getKey();
			List<MethodFieldTechnique> blockFieldList = blockFieldMap.getValue();
			
			if (extractedBlock.get(blockNameKey.split(",")[0]) instanceof String)
			{
				final String blockData = (String) extractedBlock.get(blockNameKey.split(",")[0]);
				final List<MethodFieldTechnique> parsedData = getFieldData(blockFieldList, blockData, //parsedData, 
						ignoreList);
				List<List<MethodFieldTechnique>> list = new ArrayList<List<MethodFieldTechnique>>();
				list.add(parsedData);
				outputDataMap.put(blockNameKey, list);
		
			}
			else {
				List<List<MethodFieldTechnique>> list = new ArrayList<List<MethodFieldTechnique>>();
				for (String blockData : (List<String>)extractedBlock.get(blockNameKey.split(",")[0])) {					
					final List<MethodFieldTechnique> parsedData = getFieldData(blockFieldList, blockData, //parsedData, 
							ignoreList);
					list.add(parsedData);					
				}
				outputDataMap.put(blockNameKey, list);
			}				
		}
		return outputDataMap;
	}
	
	/**
	 * This method invokes methods from CommonFunction to parse data from single valued and multi valued fields.
	 * @param blockFieldList [List] holding list of MethodFieldTechnqiue entities
	 * @param blockData [String] data block on which parsing is to be done
	 * @param parsedData [Map] map object holding field id and its parsed data
	 * @param ignoreList [List] holding ParserIgnoreChars entity list that will be ignored in parsed data
	 * @return map object holding field id and its parsed data
	 */
	private List<MethodFieldTechnique> getFieldData(final List<MethodFieldTechnique> blockFieldList,
				final String blockData,
				final List<ParserIgnoreChars> ignoreList) {
		final CommonFunction commonFunction = new CommonFunction();		
		
		final List<MethodFieldTechnique> parsedFieldList = new ArrayList<MethodFieldTechnique>();
		for(MethodFieldTechnique methodFieldTech: blockFieldList) {
			
			final MethodFieldTechnique techData = new MethodFieldTechnique(methodFieldTech);
		
			List<List<String>> dataBlock = commonFunction.getMvfData(methodFieldTech.getParsertechniques(), blockData, 
				methodFieldTech.getParserfield().getMethoddelimiter().getDelimiter().getActualdelimiter(), ignoreList);
			
			if (!methodFieldTech.getSubparsertechniques().isEmpty()) {
				for(final SubParserTechnique subParserTechnique : methodFieldTech.getSubparsertechniques())
				{
					 if(subParserTechnique.getMethoddelimiter().getParsermethod().getParsermethodname().equalsIgnoreCase("merge")){
//	                     dataBlock = commonFunction.mergeFields(dataBlock, 0, 0, 0, 0, subParserTechnique, 0);
						 dataBlock = commonFunction.mergeFields(dataBlock, subParserTechnique);
	                 }
	                 else if (subParserTechnique.getMethoddelimiter().getParsermethod().getParsermethodname().equalsIgnoreCase("split")){
//	                	 dataBlock =  commonFunction.splitField(dataBlock, 0, 0, 0, 0, subParserTechnique, 0);
	                	 dataBlock =  commonFunction.splitField(dataBlock, subParserTechnique);	                	 
	                 }
	                 else if (subParserTechnique.getMethoddelimiter().getParsermethod().getParsermethodname().equalsIgnoreCase("shift")) 
	                 {
	                	 dataBlock =  commonFunction.shiftFieldParserFunction(dataBlock, 0, 0, 0, subParserTechnique.getInputfields(), "","",0, true);
	                 }
				}
			
			}
			
			final List<String> fieldData = new ArrayList<String>();
			
			if(methodFieldTech.getFieldtype().equalsIgnoreCase("SubParserField")) {
				for(final SubParserField subParserField :methodFieldTech.getSubparserfields())
				{
					if (subParserField.getFieldid().equals(methodFieldTech.getFieldid()))
                    {               
                        if (subParserField.getSubparserfieldtype().equalsIgnoreCase("col")) {
                        	for(final List<String> rowData : dataBlock) {
                        		if (rowData.size() <= Integer.parseInt(subParserField.getSubparserfieldposition())){
                        			fieldData.add("");
                        		}
                        		else {
                        			fieldData.add(rowData.get(Integer.parseInt(subParserField.getSubparserfieldposition())));
                        		}
                        	}
                        } 
                        else if (subParserField.getSubparserfieldtype().equalsIgnoreCase("cell")) {
                         	final String cellData = dataBlock.get(Integer.parseInt(subParserField.getSubparserfieldposition().split(",")[1]))
                        			.get(Integer.parseInt(subParserField.getSubparserfieldposition().split(",")[0]));
                        	fieldData.add(cellData);                        	
                        }
                    }
				}				
				techData.setParseddata(fieldData);
			}
			else
			{				
				for (List<String> dataList : dataBlock) {
					for (String data : dataList) {
						fieldData.add(data);
					}
				}
				techData.setParseddata(fieldData);
			}
			parsedFieldList.add(techData);
		}
		return parsedFieldList;
	}
	
}
