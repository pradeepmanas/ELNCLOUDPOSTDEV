package com.agaram.eln.primary.service.methodsetup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.builder.Diff;
import org.apache.commons.lang3.builder.DiffResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.agaram.eln.primary.model.methodsetup.Method;
import com.agaram.eln.primary.model.methodsetup.SampleTextSplit;
import com.agaram.eln.primary.model.methodsetup.SampleTextSplits;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.repository.methodsetup.MethodRepository;
import com.agaram.eln.primary.repository.methodsetup.SampleTextSplitRepository;
import com.agaram.eln.primary.repository.usermanagement.LSuserMasterRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This Service class is used to access the SampleTextSplitRepository to fetch details
 * from the 'smpletextsplit' table through SampleTextSplit Entity relevant to the input request.
 * @author ATE153
 * @version 1.0.0
 * @since   13- Feb- 2020
 */
@Service
public class SampleTextSplitService {

	@Autowired
	MethodRepository methodRepo;
	
	@Autowired
	SampleTextSplitRepository textSplitRepo;
	
	@Autowired
	LSuserMasterRepository userRepo;
	
	/**
	 * This method is used to fetch list of SampleTextSplit entities based on 'Method' object
	 * @param methodKey [int] primary key of Method Object
	 * @return response object with list of SampleTextSplit entities
	 */
	@Transactional
	public ResponseEntity<Object> getSampleTextSplitByMethod(final int methodKey)
	{
		final Optional<Method> methodByKey = methodRepo.findByMethodkeyAndStatus(methodKey, 1);
		if (methodByKey.isPresent())
		{
			final List<SampleTextSplit> textList = textSplitRepo.findByMethodAndStatus(methodByKey.get(), 1);
			
			return new ResponseEntity<>(textList, HttpStatus.OK);
		}
		else
		{
		   return new ResponseEntity<>("Method Not Found", HttpStatus.NOT_FOUND);
		}
	}
	
	/**
	 * This method is used to save list of SampleTextSplit entities.
	 * @param textSplitMap [Map] object holding SampleTextSplit entities to save
	 * @param mapper [ObjectMapper] to convert list of Object to List of SampleTextSplit entities
	 * @param createdUser [CreatedUser] entity referring the user done the task.
	 * @param method [Method] entity for which the SampleTextSplit list is to be saved
	 * @return  map object with saved SampleTextSplit entities.
	 */
	public Map<String, List<SampleTextSplit>> saveSampleTextSplit(final Map<String, SampleTextSplit> textSplitMap, ObjectMapper mapper,
			final LSuserMaster createdUser, final Method method)
	{
		final Map<String, List<SampleTextSplit>>   returnObject = new HashMap<String, List<SampleTextSplit>>();
		
		final Collection<SampleTextSplit> textSplitList = textSplitMap.values();
		final List<SampleTextSplit> listToSave = mapper.convertValue(textSplitList, 
			  	new TypeReference<List<SampleTextSplit>>() {});		
		
		final List<Integer> idList = new ArrayList<Integer>();
		
		listToSave.forEach(item->{
			item.setMethod(method);
			
			if (item.getSampletextsplitkey() == null || item.getSampletextsplitkey() == 0){
					item.setCreatedby(createdUser);					
			}	
			else
			{
				idList.add(item.getSampletextsplitkey());
			}			
				
		});	
		final List<SampleTextSplit> splitListBeforeSave = textSplitRepo.findBySampletextsplitkeyInAndStatus(idList, 1);

	    final List<SampleTextSplit> cloneListBeforeSave	= splitListBeforeSave.stream().map(SampleTextSplit::new).collect(Collectors.toList());
		final List<SampleTextSplit> savedList  = textSplitRepo.save(listToSave);
		returnObject.put("TextListBeforeSave", cloneListBeforeSave);
		returnObject.put("TextListAfterSave", savedList);
		
		return returnObject;
	}
	
	/**
	 * This method is used to convert the list of SampleTextSplit entities to xml with the difference in object
	 * before and after update for recording in Audit Trial
	 * @param listBeforeSave list of SampleTextSplit details before saving to DB
	 * @param savedList list of SampleTextSplit details after saving to DB
	 * @return string formatted xml data
	 */
	public String convertSampleTextSplitListToXML(final List<SampleTextSplit> listBeforeSave, 
			final List<SampleTextSplit> savedList)
	{			
		try {			
			final SampleTextSplits afterSaveList = new SampleTextSplits(); 
			afterSaveList.setSampletextsplits(savedList);	
			Map<Integer, Map<String, Object>> dataModified = new HashMap<Integer, Map<String, Object>>();
			for (SampleTextSplit objAfterSave : savedList)
			{					
				for (SampleTextSplit objBeforeSave : listBeforeSave)
				{
					if (objBeforeSave.getSampletextsplitkey().equals(objAfterSave.getSampletextsplitkey()))
					{				
						final DiffResult diffResult = objBeforeSave.diff(objAfterSave);
							
						Map<String, Object> diffObject = new HashMap<String, Object>();
						
//						for(Diff<?> d: diffResult.getDiffs()) {
//								diffObject.put(d.getFieldName(), d.getKey()+" -> "+d.getValue());
//						}
						dataModified.put(objBeforeSave.getSampletextsplitkey(), diffObject);												
					}			
				}
			}
	
			
			final Map<String, String> fieldMap = new HashMap<String, String>();		
			fieldMap.put("createdby", "loginid");		
			fieldMap.put("method", "methodname");
			
//			final ReadWriteXML readWriteXML =  new ReadWriteXML();
//			return readWriteXML.saveXML(afterSaveList, SampleTextSplits.class, dataModified, "listpojo", fieldMap);
			return "";
		
		} catch ( SecurityException | IllegalArgumentException e) {
			return null;
		}
	}

}
