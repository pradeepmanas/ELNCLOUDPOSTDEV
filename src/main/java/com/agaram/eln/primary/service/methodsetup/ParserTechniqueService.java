package com.agaram.eln.primary.service.methodsetup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;

import org.apache.commons.lang3.builder.Diff;
import org.apache.commons.lang3.builder.DiffResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.agaram.eln.primary.model.methodsetup.Method;
import com.agaram.eln.primary.model.methodsetup.ParserBlock;
import com.agaram.eln.primary.model.methodsetup.ParserField;
import com.agaram.eln.primary.model.methodsetup.ParserTechnique;
import com.agaram.eln.primary.model.methodsetup.ParserTechniques;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.repository.methodsetup.MethodRepository;
import com.agaram.eln.primary.repository.methodsetup.ParserBlockRepository;
import com.agaram.eln.primary.repository.methodsetup.ParserFieldRepository;
import com.agaram.eln.primary.repository.methodsetup.ParserTechniqueRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This Service class is used to access the ParserTechniqueRepository to fetch details
 * from the 'parsertechnique' table through ParserTechnique Entity relevant to the input request.
 * @author ATE153
 * @version 1.0.0
 * @since   19- Apr- 2020
 */
@Service
public class ParserTechniqueService {
	
	@Autowired
	ParserTechniqueRepository parserTechniqueRepo;
	
	@Autowired
	ParserFieldRepository parserFieldRepo;
	
	@Autowired
	MethodRepository methodRepo;
	
	@Autowired
	ParserBlockRepository parserBlockRepo;


	/**
	 * This method is used to save List of ParserTechnique entities.
	 * @param techniqueList [List] ParserTechnique entities to save
	 * @param mapper [ObjectMapper]to convert list of Object to List of ParserTechnique entities
	 * @param createdUser [CreatedUser] entity referring the user done the task.
	 * @param parserFieldMap [Map] object referring ParserField to map Parser Techniques
	 * @return map object with list of saved entities
	 */
	public Map<String, List<ParserTechnique>> saveParserTechnique(final List<ParserTechnique> techniqueList, ObjectMapper mapper,
			final LSuserMaster createdUser, final Map<String, ParserField> parserFieldMap)
	{
		final Map<String, List<ParserTechnique>> returnObject = new HashMap<String, List<ParserTechnique>>();
		
		final List<ParserTechnique> listToSave = mapper.convertValue(techniqueList, 
			  	new TypeReference<List<ParserTechnique>>() {});	
		
		final List<Integer> idList = new ArrayList<Integer>();

		for(ParserTechnique item :listToSave) {
			item.setParserfield(parserFieldMap.get(item.getParserfield().getFieldid()));	
								
			if (item.getParsertechniquekey() == null || item.getParsertechniquekey() == 0){
				item.setCreatedby(createdUser);					
			}	
			else
			{
				idList.add(item.getParsertechniquekey());
			}
		}	
	
	    List<ParserTechnique> cloneListBeforeSave	= new ArrayList<ParserTechnique>();
		if (!idList.isEmpty()) {
			final List<ParserTechnique> listBeforeSave = parserTechniqueRepo.findByParsertechniquekeyInAndStatus(idList, 1);
			cloneListBeforeSave	= listBeforeSave.stream().map(ParserTechnique::new).collect(Collectors.toList());
		}
		final List<ParserTechnique> savedList  = parserTechniqueRepo.save(listToSave);
		returnObject.put("ParserTechniqueListBeforeSave", cloneListBeforeSave);
		returnObject.put("ParserTechniqueListAfterSave", savedList);
		
		return returnObject;
		
	}


	/**
	 * This method is used to retrieve list of active ParserTechnique entities for the specified 'Method' object.
	 * @param methodKey [int] primary key of Method Object for which the ParserTechnique entities are to be fetched
	 * @return list of ParserTechnique entities for the specified method
	 */
	@Transactional
	public ResponseEntity<Object> getParserTechniqueByMethodKey(final int methodKey)
	{
		final Method method = methodRepo.findOne(methodKey);
		
		List<ParserBlock> lstparserblock = new ArrayList<ParserBlock>();
		List<ParserField> lstparserfield = new ArrayList<ParserField>();
		List<ParserTechnique> lstparsertech = new ArrayList<ParserTechnique>();
		
		if(method != null)
		{
			lstparserblock = parserBlockRepo.findByMethodAndStatus(method, 1);
			
			if(lstparserblock != null)
			{
				lstparserfield = parserFieldRepo.findByParserblockInAndStatus(lstparserblock, 1);
				
				if(lstparserfield != null)
				{
					lstparsertech = parserTechniqueRepo.findByParserfieldInAndStatus(lstparserfield, 1);
				}
			}
		}
		
		return new ResponseEntity<>(lstparsertech, HttpStatus.OK); 
	}
	
	/**
	 * This method is used to convert the list of ParserTechnique entities to xml with the difference in object
	 * before and after update for recording in Audit Trial
	 * @param listBeforeSave list of ParserTechnique details before saving to DB
	 * @param savedList list of ParserTechnique details after saving to DB
	 * @return string formatted xml data
	 */
	public String convertParserTechniqueListToXML(final List<ParserTechnique> listBeforeSave, 
			final List<ParserTechnique> savedList)
	{			
		try {			
			final ParserTechniques afterSaveList = new ParserTechniques(); 
			afterSaveList.setParsertechniques(savedList);	
			Map<Integer, Map<String, Object>> dataModified = new HashMap<Integer, Map<String, Object>>();
			for (ParserTechnique objAfterSave : savedList)
			{					
				for (ParserTechnique objBeforeSave : listBeforeSave)
				{
					if (objBeforeSave.getParsertechniquekey().equals(objAfterSave.getParsertechniquekey()))
					{				
						final DiffResult diffResult = objBeforeSave.diff(objAfterSave);
							
						Map<String, Object> diffObject = new HashMap<String, Object>();
						
//						for(Diff<?> d: diffResult.getDiffs()) {
//								diffObject.put(d.getFieldName(), d.getKey()+" -> "+d.getValue());
//						}
						dataModified.put(objBeforeSave.getParsertechniquekey(), diffObject);
												
					}			
				}
			}
			
			final Map<String, String> fieldMap = new HashMap<String, String>();		
			fieldMap.put("createdby", "loginid");		
			fieldMap.put("parserfield","parserfieldname");
			fieldMap.put("parsermethod", "parsermethodname");
			
//			final ReadWriteXML readWriteXML =  new ReadWriteXML();
//			return readWriteXML.saveXML(afterSaveList, ParserTechniques.class, dataModified, "listpojo", fieldMap);	
			return "";
		
		} catch ( SecurityException | IllegalArgumentException e) {
			return null;
		}
	}

}
