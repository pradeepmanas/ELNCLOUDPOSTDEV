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
import com.agaram.eln.primary.model.methodsetup.SubParserField;
import com.agaram.eln.primary.model.methodsetup.SubParserTechnique;
import com.agaram.eln.primary.model.methodsetup.SubParserTechniques;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.repository.methodsetup.MethodRepository;
import com.agaram.eln.primary.repository.methodsetup.ParserBlockRepository;
import com.agaram.eln.primary.repository.methodsetup.ParserFieldRepository;
import com.agaram.eln.primary.repository.methodsetup.SubParserTechniqueRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This Service class is used to access the SubParserTechniqueRepository to fetch details
 * from the 'subparsertechnique' table through SubParserTechnique Entity relevant to the input request.
 * @author ATE153
 * @version 1.0.0
 * @since   19- Apr- 2020
 */
@Service
public class SubParserTechniqueService {
	
	@Autowired
	SubParserTechniqueRepository subParserTechniqueRepo;
	
	@Autowired
	ParserFieldRepository parserFieldRepo;
	
	@Autowired
	MethodRepository methodRepo;
	
	@Autowired
	ParserBlockRepository parserBlockRepo;

	/**
	 * This method is used to save List of SubParserTechnique entities.
	 * @param fieldList [List] SubParserTechnique entities to save
	 * @param mapper to convert list of Object to List of subParserTechnique entities
	 * @param createdUser [CreatedUser] entity referring the user done the task.
	 * @param parserFieldMap [Map] object referring ParserField to map Sub Parser Techniques
	 * @return map object with list of saved entities
	 */
	public Map<String, List<SubParserTechnique>> saveSubParserTechnique(final List<SubParserTechnique> fieldList, ObjectMapper mapper,
			final LSuserMaster createdUser, final Map<String, ParserField> parserFieldMap)
	{
		final Map<String, List<SubParserTechnique>>  returnObject = new HashMap<String, List<SubParserTechnique>>();
		
		final List<SubParserTechnique> listToSave = mapper.convertValue(fieldList, 
			  	new TypeReference<List<SubParserTechnique>>() {});	
		
		final List<Integer> idList = new ArrayList<Integer>();

		for(SubParserTechnique item :listToSave) {
			item.setParserfield(parserFieldMap.get(item.getParserfield().getFieldid()));
			
			if (item.getSubparsertechniquekey() == null || item.getSubparsertechniquekey() == 0){
					item.setCreatedby(createdUser);					
			}	
			else
			{
				idList.add(item.getSubparsertechniquekey());
			}			
		
		}	
	
	    List<SubParserTechnique> cloneListBeforeSave	= new ArrayList<SubParserTechnique>();
		if (!idList.isEmpty()) {
			final List<SubParserTechnique> listBeforeSave = subParserTechniqueRepo.findBySubparsertechniquekeyInAndStatus(idList, 1);

		   cloneListBeforeSave	= listBeforeSave.stream().map(SubParserTechnique::new).collect(Collectors.toList());
		
		}
		
		final List<SubParserTechnique> savedList  = subParserTechniqueRepo.save(listToSave);
		returnObject.put("SubParserTechniqueListBeforeSave", cloneListBeforeSave);
		returnObject.put("SubParserTechniqueListAfterSave", savedList);
		
		return returnObject;
		
	}

	/**
	 * This method is used to retrieve list of active SubParserTechnique entities for the specified 'Method' object.
	 * @param methodKey [int] primary key of Method Object for which the SubParserTechnique entities are to be fetched
	 * @return list of SubParserTechnique entities for the specified method
	 */
	@Transactional
	public ResponseEntity<Object> getSubParserTechniqueByMethodKey(final int methodKey)
	{
		final Method method = methodRepo.findOne(methodKey);
		
		List<ParserBlock> lstparserblock = new ArrayList<ParserBlock>();
		List<ParserField> lstparserfield = new ArrayList<ParserField>();
		List<SubParserTechnique> lstsubparsertech = new ArrayList<SubParserTechnique>();
		
		if(method != null)
		{
			lstparserblock = parserBlockRepo.findByMethodAndStatus(method, 1);
			
			if(lstparserblock != null)
			{
				lstparserfield = parserFieldRepo.findByParserblockInAndStatus(lstparserblock, 1);
				
				if(lstparserfield != null)
				{
					lstsubparsertech = subParserTechniqueRepo.findByParserfieldInAndStatus(lstparserfield, 1);
				}
			}
		}
		return new ResponseEntity<>(lstsubparsertech, HttpStatus.OK); 
	}
	
	/**
	 * This method is used to convert the list of SubParserTechnique entities to xml with the difference in object
	 * before and after update for recording in Audit Trial
	 * @param listBeforeSave list of SubParserTechnique details before saving to DB
	 * @param savedList list of SubParserTechnique details after saving to DB
	 * @return string formatted xml data
	 */
	public String convertSubParserTechniqueListToXML(final List<SubParserTechnique> listBeforeSave, 
			final List<SubParserTechnique> savedList)
	{			
		try {			
			final SubParserTechniques afterSaveList = new SubParserTechniques(); 
			afterSaveList.setSubparsertechniques(savedList);	
			Map<Integer, Map<String, Object>> dataModified = new HashMap<Integer, Map<String, Object>>();
			for (SubParserTechnique objAfterSave : savedList)
			{					
				for (SubParserTechnique objBeforeSave : listBeforeSave)
				{
					if (objBeforeSave.getSubparsertechniquekey().equals(objAfterSave.getSubparsertechniquekey()))
					{				
						final DiffResult diffResult = objBeforeSave.diff(objAfterSave);
							
						Map<String, Object> diffObject = new HashMap<String, Object>();
						
//						for(Diff<?> d: diffResult.getDiffs()) {
//								diffObject.put(d.getFieldName(), d.getKey()+" -> "+d.getValue());
//						}
						dataModified.put(objBeforeSave.getSubparsertechniquekey(), diffObject);
												
					}			
				}
			}
			
			final Map<String, String> fieldMap = new HashMap<String, String>();		
			fieldMap.put("createdby", "loginid");		
			fieldMap.put("parserfield","parserfieldname");
			fieldMap.put("methoddelimiter", "delimitername");
			
//			final ReadWriteXML readWriteXML =  new ReadWriteXML();
//			return readWriteXML.saveXML(afterSaveList, SubParserTechniques.class, dataModified, "listpojo", fieldMap);	
			return "";
		
		} catch ( SecurityException | IllegalArgumentException e) {
			return null;
		}
	}

}
