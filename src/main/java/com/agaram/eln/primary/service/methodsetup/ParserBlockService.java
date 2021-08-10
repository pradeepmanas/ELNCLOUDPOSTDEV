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
import com.agaram.eln.primary.model.methodsetup.ParserBlocks;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.repository.methodsetup.MethodRepository;
import com.agaram.eln.primary.repository.methodsetup.ParserBlockRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This Service class is used to access the ParserBlockRepository to fetch details
 * from the 'parserblock' table through ParserBlock Entity relevant to the input request.
 * @author ATE153
 * @version 1.0.0
 * @since   18- Apr- 2020
 */
@Service
public class ParserBlockService {

	@Autowired
	ParserBlockRepository blockRepo;
	
	@Autowired
	MethodRepository methodrepository;

	
	/**
	 * This method is used to save List of ParserBlock entities.
	 * @param parserBlockList [List] entities to save.
	 * @param mapper [ObjectMapper] to convert list of Object to List of ParserBlock entities
	 * @param createdUser [CreatedUser] entity referring the user done the task.
	 * @param method [Method] entity for which the ParserBlock list is to be saved
	 * @return map object with list of saved entities
	 */
	public Map<String, List<ParserBlock>> saveParserBlock(final List<ParserBlock> parserBlockList, ObjectMapper mapper,
			final LSuserMaster createdUser, final Method method)
	{
		final Map<String, List<ParserBlock>>  returnObject = new HashMap<String, List<ParserBlock>>();
		
		final List<ParserBlock> listToSave = mapper.convertValue(parserBlockList, 
			  	new TypeReference<List<ParserBlock>>() {});		
		final List<Integer> idList = new ArrayList<Integer>();
		
		listToSave.forEach(item->{
			item.setMethod(method);
			
			if (item.getParserblockkey() == null || item.getParserblockkey() == 0){
					item.setCreatedby(createdUser);					
			}	
			else
			{
				idList.add(item.getParserblockkey());
			}			
				
		});	
		
	    List<ParserBlock> cloneListBeforeSave	= new ArrayList<ParserBlock>();
		if (!idList.isEmpty()) {
			final List<ParserBlock> listBeforeSave = blockRepo.findByParserblockkeyInAndStatus(idList, 1);
			cloneListBeforeSave	= listBeforeSave.stream().map(ParserBlock::new).collect(Collectors.toList());
		}
	    final List<ParserBlock> savedList  = blockRepo.save(listToSave);
		returnObject.put("ParserBlockListBeforeSave", cloneListBeforeSave);
		returnObject.put("ParserBlockListAfterSave", savedList);
		
		return returnObject;
		
	}	
	
	/**
	 * This method is used to retrieve list of active ParserBlock entities for the specified 'Method' object.
	 * @param methodKey [int] primary key of Method Object for which the ParserBlocks are to be fetched
	 * @return list of ParserBlock entities for the specified method
	 */
	@Transactional
	public ResponseEntity<Object> getParserBlockByMethodKey(final int methodKey)
	{
		Method objmethod = methodrepository.findOne(methodKey);	
		return new ResponseEntity<>(blockRepo.findByMethodAndStatus(objmethod, 1), HttpStatus.OK);
//		return new ResponseEntity<>(blockRepo.findByMethodAndStatus(methodKey,1), HttpStatus.OK);
		
	}
	
	/**
	 * This method is used to convert the list of ParserBlock entities to xml with the difference in object
	 * before and after update for recording in Audit Trial
	 * @param listBeforeSave list of ParserBlock details before saving to DB
	 * @param savedList list of ParserBlock details after saving to DB
	 * @return string formatted xml data
	 */
	public String convertParserBlockListToXML(final List<ParserBlock> listBeforeSave, 
			final List<ParserBlock> savedList)
	{			
		try {			
			final ParserBlocks afterSaveList = new ParserBlocks(); 
			afterSaveList.setParserblocks(savedList);	
			Map<Integer, Map<String, Object>> dataModified = new HashMap<Integer, Map<String, Object>>();
			for (ParserBlock objAfterSave : savedList)
			{					
				for (ParserBlock objBeforeSave : listBeforeSave)
				{
					if (objBeforeSave.getParserblockkey().equals(objAfterSave.getParserblockkey()))
					{				
						final DiffResult diffResult = objBeforeSave.diff(objAfterSave);
							
						Map<String, Object> diffObject = new HashMap<String, Object>();
						
//						for(Diff<?> d: diffResult.getDiffs()) {
//								diffObject.put(d.getFieldName(), d.getKey()+" -> "+d.getValue());
//						}
						dataModified.put(objBeforeSave.getParserblockkey(), diffObject);
												
					}			
				}
			}
			
			final Map<String, String> fieldMap = new HashMap<String, String>();		
			fieldMap.put("createdby", "loginid");		
			fieldMap.put("method", "methodname");
			
//			final ReadWriteXML readWriteXML =  new ReadWriteXML();
//			return readWriteXML.saveXML(afterSaveList, ParserBlocks.class, dataModified, "listpojo", fieldMap);	
			return "";
		
		} catch ( SecurityException | IllegalArgumentException e) {
			return null;
		}
	}
}
