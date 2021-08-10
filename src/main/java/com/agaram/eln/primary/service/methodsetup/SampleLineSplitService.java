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
import com.agaram.eln.primary.model.methodsetup.SampleLineSplit;
import com.agaram.eln.primary.model.methodsetup.SampleLineSplits;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.repository.methodsetup.MethodRepository;
import com.agaram.eln.primary.repository.methodsetup.SampleLineSplitRepository;
import com.agaram.eln.primary.repository.usermanagement.LSuserMasterRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This Service class is used to access the SampleLineSplitRepository to fetch details
 * from the 'samplelinesplit' table through SampleLineSplit Entity relevant to the input request.
 * @author ATE153
 * @version 1.0.0
 * @since   18- Feb- 2020
 */
@Service
public class SampleLineSplitService {

	@Autowired
	MethodRepository methodRepo;
	
	@Autowired
	SampleLineSplitRepository lineSplitRepo;
	
	@Autowired
	LSuserMasterRepository userRepo;
	
	/**
	 * This method is used to fetch list of SampleLineSplit entities based on 'Method' object
	 * @param methodKey [int] primary key of Method Object
	 * @return response object with list of SampleLineSplit entities
	 */
	@Transactional
	public ResponseEntity<Object> getSampleLineSplitByMethod(final int methodKey)
	{
		final Optional<Method> methodByKey = methodRepo.findByMethodkeyAndStatus(methodKey, 1);
		if (methodByKey.isPresent())
		{
			final List<SampleLineSplit> lineList = lineSplitRepo.findByMethodAndStatus(methodByKey.get(), 1);
			
			return new ResponseEntity<>(lineList, HttpStatus.OK);
		}
		else
		{
		   return new ResponseEntity<>("Method Not Found", HttpStatus.NOT_FOUND);
		}
	}
	
	/**
	 * This method is used to save list of SampleLineSplit entities.
	 * @param lineSplitMap [Map] object holding SampleLineSplit entities to save
	 * @param mapper [ObjectMapper] to convert list of Object to List of SampleLineSplit entities
	 * @param createdUser [CreatedUser] entity referring the user done the task.
	 * @param method [Method] entity for which the SampleLineSplit list is to be saved
	 * @return  map object with saved SampleLineSplit entities.
	 */
	public Map<String, List<SampleLineSplit>> saveSampleLineSplit(final Map<String, SampleLineSplit> lineSplitMap, ObjectMapper mapper,
			final LSuserMaster createdUser, final Method method){
		final Map<String, List<SampleLineSplit>>   returnObject = new HashMap<String, List<SampleLineSplit>>();
		
		final Collection<SampleLineSplit> lineSplitList = lineSplitMap.values();
	
		final List<SampleLineSplit> linesToSave = mapper.convertValue(lineSplitList, 
				  	new TypeReference<List<SampleLineSplit>>() {});
						
		final List<Integer> idList = new ArrayList<Integer>();	 
		linesToSave.forEach(item->{				 
				item.setMethod(method);	
				if (item.getSamplelinesplitkey() == null || item.getSamplelinesplitkey() == 0){
					item.setCreatedby(createdUser);						
				}
				else
				{
					idList.add(item.getSamplelinesplitkey());
				}
					
		});	
		final List<SampleLineSplit> splitListBeforeSave = lineSplitRepo.findBySamplelinesplitkeyInAndStatus(idList, 1);	
		final List<SampleLineSplit> cloneListBeforeSave	= splitListBeforeSave.stream().map(SampleLineSplit::new).collect(Collectors.toList());
		final List<SampleLineSplit> savedList  = lineSplitRepo.save(linesToSave);
		returnObject.put("LineListBeforeSave", cloneListBeforeSave);
		returnObject.put("LineListAfterSave", savedList);
		
		return returnObject;
	}	

	/**
	 * This method is used to convert the list of SampleLineSplit entities to xml with the difference in object
	 * before and after update for recording in Audit Trial
	 * @param listBeforeSave list of SampleLineSplit details before saving to DB
	 * @param savedList list of SampleLineSplit details after saving to DB
	 * @return string formatted xml data
	 */
	public String convertSampleLineSplitListToXML(final List<SampleLineSplit> listBeforeSave, 
			final List<SampleLineSplit> savedList)
	{			
		try {
			
			final SampleLineSplits afterSaveList = new SampleLineSplits(); 
			afterSaveList.setSamplelinesplits(savedList);	
			Map<Integer, Map<String, Object>> dataModified = new HashMap<Integer, Map<String, Object>>();
			for (SampleLineSplit objAfterSave : savedList)
			{					
				for (SampleLineSplit objBeforeSave : listBeforeSave)
				{
					if (objBeforeSave.getSamplelinesplitkey().equals(objAfterSave.getSamplelinesplitkey()))
					{				
						final DiffResult diffResult = objBeforeSave.diff(objAfterSave);
							
						Map<String, Object> diffObject = new HashMap<String, Object>();
						
//						for(Diff<?> d: diffResult.getDiffs()) {
//								diffObject.put(d.getFieldName(), d.getKey()+" -> "+d.getValue());
//
//						}
						dataModified.put(objBeforeSave.getSamplelinesplitkey(), diffObject);
					}						
				}
			}
	
		
			final Map<String, String> fieldMap = new HashMap<String, String>();
			fieldMap.put("createdby", "loginid");		
			fieldMap.put("method", "methodname");
			
//			final ReadWriteXML readWriteXML =  new ReadWriteXML();
//			return readWriteXML.saveXML(afterSaveList, SampleLineSplits.class, dataModified, "listpojo", fieldMap);	
			return "";
		
		} catch ( SecurityException | IllegalArgumentException e) {
			return null;
		}
	}

}
