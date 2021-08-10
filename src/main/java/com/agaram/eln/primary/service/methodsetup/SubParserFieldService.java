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
import com.agaram.eln.primary.model.methodsetup.SubParserField;
import com.agaram.eln.primary.model.methodsetup.SubParserFields;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.repository.methodsetup.MethodRepository;
import com.agaram.eln.primary.repository.methodsetup.ParserBlockRepository;
import com.agaram.eln.primary.repository.methodsetup.ParserFieldRepository;
import com.agaram.eln.primary.repository.methodsetup.SubParserFieldRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This Service class is used to access the SubParserFieldRepository to fetch details
 * from the 'subparserfield' table through SubParserField Entity relevant to the input request.
 * @author ATE153
 * @version 1.0.0
 * @since   19- Apr- 2020
 */
@Service
public class SubParserFieldService {
	
	@Autowired
	SubParserFieldRepository subParserFieldRepo;
	
	@Autowired
	ParserFieldRepository parserFieldRepo;
	
	@Autowired
	MethodRepository methodRepo;
	
	@Autowired
	ParserBlockRepository parserBlockRepo;

	/**
	 * This method is used to save List of SubParserField entities.
	 * @param fieldList [List] SubParserField entities to save
	 * @param mapper to convert list of Object to List of SubParserField entities
	 * @param createdUser [CreatedUser] entity referring the user done the task.
	 * @param fieldMap [Map] object referring ParserField to map Sub Parser Fields
	 * @return map object with list of saved entities
	 */
	public Map<String, List<SubParserField>> saveSubParserField(final List<SubParserField> fieldList, ObjectMapper mapper,
			final LSuserMaster createdUser, final Map<String, ParserField> fieldMap)
	{
		final Map<String, List<SubParserField>>  returnObject = new HashMap<String, List<SubParserField>>();
		
		final List<SubParserField> listToSave = mapper.convertValue(fieldList, 
			  	new TypeReference<List<SubParserField>>() {});	
		
		final List<Integer> idList = new ArrayList<Integer>();

		for(SubParserField item :listToSave) {
			item.setParserfield(fieldMap.get(item.getParserfield().getFieldid()));		
			if (item.getSubparserfieldkey() == null || item.getSubparserfieldkey() == 0){
					item.setCreatedby(createdUser);					
			}	
			else
			{
				idList.add(item.getSubparserfieldkey());
			}			
		
		}	
	
		List<SubParserField> cloneListBeforeSave	= new ArrayList<SubParserField>();
		if (!idList.isEmpty()) {
			final List<SubParserField> listBeforeSave = subParserFieldRepo.findBySubparserfieldkeyInAndStatus(idList, 1);
			cloneListBeforeSave	= listBeforeSave.stream().map(SubParserField::new).collect(Collectors.toList());
		}
		
		final List<SubParserField> savedList  = subParserFieldRepo.save(listToSave);
		returnObject.put("SubParserFieldListBeforeSave", cloneListBeforeSave);
		returnObject.put("SubParserFieldListAfterSave", savedList);
		
		return returnObject;		
	}
	
	/**
	 * This method is used to retrieve list of active SubParserField entities for the specified 'Method' object.
	 * @param methodKey [int] primary key of Method Object for which the SubParserField entities are to be fetched
	 * @return list of SubParserField entities for the specified method
	 */
	@Transactional
	public ResponseEntity<Object> getSubParserFieldByMethodKey(final int methodKey)
	{
		final Method method = methodRepo.findOne(methodKey);
		
		List<ParserBlock> lstparserblock = new ArrayList<ParserBlock>();
		List<ParserField> lstparserfield = new ArrayList<ParserField>();
		List<SubParserField> lstsubparserfield = new ArrayList<SubParserField>();
		
		if(method != null)
		{
			lstparserblock = parserBlockRepo.findByMethodAndStatus(method, 1);
			
			if(lstparserblock != null)
			{
				lstparserfield = parserFieldRepo.findByParserblockInAndStatus(lstparserblock, 1);
				
				if(lstparserfield != null)
				{
					lstsubparserfield = subParserFieldRepo.findByParserfieldInAndStatus(lstparserfield, 1);
				}
			}
		}
		return new ResponseEntity<>(lstsubparserfield, HttpStatus.OK); 
	}
	
	/**
	 * This method is used to convert the list of SubParserField entities to xml with the difference in object
	 * before and after update for recording in Audit Trial
	 * @param listBeforeSave list of SubParserField details before saving to DB
	 * @param savedList list of SubParserField details after saving to DB
	 * @return string formatted xml data
	 */
	public String convertSubParserFieldListToXML(final List<SubParserField> listBeforeSave, 
			final List<SubParserField> savedList)
	{			
		try {			
			final SubParserFields afterSaveList = new SubParserFields(); 
			afterSaveList.setSubparserfields(savedList);	
			Map<Integer, Map<String, Object>> dataModified = new HashMap<Integer, Map<String, Object>>();
			for (SubParserField objAfterSave : savedList)
			{					
				for (SubParserField objBeforeSave : listBeforeSave)
				{
					if (objBeforeSave.getSubparserfieldkey().equals(objAfterSave.getSubparserfieldkey()))
					{				
						final DiffResult diffResult = objBeforeSave.diff(objAfterSave);
							
						Map<String, Object> diffObject = new HashMap<String, Object>();
						
//						for(Diff<?> d: diffResult.getDiffs()) {
//								diffObject.put(d.getFieldName(), d.getKey()+" -> "+d.getValue());
//						}
						dataModified.put(objBeforeSave.getSubparserfieldkey(), diffObject);
												
					}			
				}
			}
			
			final Map<String, String> fieldMap = new HashMap<String, String>();		
			fieldMap.put("createdby", "loginid");		
			fieldMap.put("parserfield","parserfieldname");
				
//			final ReadWriteXML readWriteXML =  new ReadWriteXML();
//			return readWriteXML.saveXML(afterSaveList, SubParserFields.class, dataModified, "listpojo", fieldMap);	
			return "";
		
		} catch ( SecurityException | IllegalArgumentException e) {
			return null;
		}
	}


}
