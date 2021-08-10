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
import com.agaram.eln.primary.model.methodsetup.ParserFields;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.repository.methodsetup.MethodRepository;
import com.agaram.eln.primary.repository.methodsetup.ParserBlockRepository;
import com.agaram.eln.primary.repository.methodsetup.ParserFieldRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This Service class is used to access the ParserFieldRepository to fetch details
 * from the 'parserfield' table through ParserField Entity relevant to the input request.
 * @author ATE153
 * @version 1.0.0
 * @since   19- Apr- 2020
 */
@Service
public class ParserFieldService {
	
	@Autowired
	ParserFieldRepository parserFieldRepo;
	
	@Autowired
	MethodRepository methodRepo;
	
	@Autowired
	ParserBlockRepository parserBlockRepo;

	/**
	 * This method is used to save List of ParserField entities.
	 * @param fieldList [List] ParserField entities to save
	 * @param mapper [ObjectMapper] to convert list of Object to List of ParserField entities
	 * @param createdUser [CreatedUser] entity referring the user done the task.
	 * @param parserBlockMap [Map] object referring ParserBlock to map ParserFields
	 * @return map object with list of saved entities
	 */
	public Map<String, List<ParserField>> saveParserField(final List<ParserField> fieldList, ObjectMapper mapper,
			final LSuserMaster createdUser, final Map<String, ParserBlock> parserBlockMap)
	{
		final Map<String, List<ParserField>>  returnObject = new HashMap<String, List<ParserField>>();
		
		final List<ParserField> listToSave = mapper.convertValue(fieldList, 
			  	new TypeReference<List<ParserField>>() {});	
		
		final List<Integer> idList = new ArrayList<Integer>();

		for(ParserField item :listToSave) {
			item.setParserblock(parserBlockMap.get(item.getParserblock().getParserblockname()));		
			if (item.getParserfieldkey() == null || item.getParserfieldkey() == 0){
					item.setCreatedby(createdUser);					
			}	
			else
			{
				idList.add(item.getParserfieldkey());
			}			
		
		}	
		
		List<ParserField> cloneListBeforeSave	= new ArrayList<ParserField>();
		if (!idList.isEmpty()) {
			final List<ParserField> listBeforeSave = parserFieldRepo.findByParserfieldkeyInAndStatus(idList, 1);
			cloneListBeforeSave	= listBeforeSave.stream().map(ParserField::new).collect(Collectors.toList());
		}
	
		final List<ParserField> savedList  = parserFieldRepo.save(listToSave);
		returnObject.put("ParserFieldListBeforeSave", cloneListBeforeSave);
		returnObject.put("ParserFieldListAfterSave", savedList);
		
		return returnObject;
		
	}
	
	/**
	 * This method is used to retrieve list of active ParserField entities for the specified 'Method' object.
	 * @param methodKey [int] primary key of Method Object for which the ParserFields are to be fetched
	 * @return list of ParserField entities for the specified method
	 */
	@Transactional
	public ResponseEntity<Object> getParserFieldByMethodKey(final int methodKey)
	{
		final Method method = methodRepo.findOne(methodKey);
		
		List<ParserBlock> lstparserblock = new ArrayList<ParserBlock>();
		List<ParserField> lstparserfield = new ArrayList<ParserField>();
		
		if(method != null)
		{
			lstparserblock = parserBlockRepo.findByMethodAndStatus(method, 1);
			
			if(lstparserblock != null)
			{
				lstparserfield = parserFieldRepo.findByParserblockInAndStatus(lstparserblock, 1);
			}
		}
		return new ResponseEntity<>(lstparserfield, HttpStatus.OK); 
	}

	/**
	 * This method is used to convert the list of ParserField entities to xml with the difference in object
	 * before and after update for recording in Audit Trial
	 * @param listBeforeSave list of ParserField details before saving to DB
	 * @param savedList list of ParserField details after saving to DB
	 * @return string formatted xml data
	 */
	public String convertParserFieldListToXML(final List<ParserField> listBeforeSave, 
			final List<ParserField> savedList)
	{			
		try {			
			final ParserFields afterSaveList = new ParserFields(); 
			afterSaveList.setParserfields(savedList);	
			Map<Integer, Map<String, Object>> dataModified = new HashMap<Integer, Map<String, Object>>();
			for (ParserField objAfterSave : savedList)
			{					
				for (ParserField objBeforeSave : listBeforeSave)
				{
					if (objBeforeSave.getParserfieldkey().equals(objAfterSave.getParserfieldkey()))
					{				
						final DiffResult diffResult = objBeforeSave.diff(objAfterSave);
							
						Map<String, Object> diffObject = new HashMap<String, Object>();
						
//						for(Diff<?> d: diffResult.getDiffs()) {
//								diffObject.put(d.getFieldName(), d.getKey()+" -> "+d.getValue());
//						}
						dataModified.put(objBeforeSave.getParserfieldkey(), diffObject);
												
					}			
				}
			}
			
			final Map<String, String> fieldMap = new HashMap<String, String>();		
			fieldMap.put("createdby", "loginid");		
			fieldMap.put("parserblock","parserblockname");
			fieldMap.put("methoddelimiter", "delimitername");
			
//			final ReadWriteXML readWriteXML =  new ReadWriteXML();
//			return readWriteXML.saveXML(afterSaveList, ParserFields.class, dataModified, "listpojo", fieldMap);	
			return "";
		
		} catch ( SecurityException | IllegalArgumentException e) {
			return null;
		}
	}

}
