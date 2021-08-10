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
import com.agaram.eln.primary.model.methodsetup.SampleExtract;
import com.agaram.eln.primary.model.methodsetup.SampleExtracts;
import com.agaram.eln.primary.model.methodsetup.SampleLineSplit;
import com.agaram.eln.primary.model.methodsetup.SampleTextSplit;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.repository.methodsetup.MethodRepository;
import com.agaram.eln.primary.repository.methodsetup.SampleExtractRepository;
import com.agaram.eln.primary.repository.usermanagement.LSuserMasterRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This Service class is used to access the SampleExtractRepository to fetch details
 * from the 'sampleextract' table through SampleExtract Entity relevant to the input request.
 * @author ATE153
 * @version 1.0.0
 * @since   13- Feb- 2020
 */
@Service
public class SampleExtractService {

	@Autowired
	MethodRepository methodRepo;
	
	@Autowired
	SampleExtractRepository extractRepo;
	
	@Autowired
	LSuserMasterRepository userRepo;
	
	
	/**
	 * This method is used to fetch list of SampleExtract entities based on 'Method' object
	 * @param methodKey [int] primary key of Method Object
	 * @return response object with list of SampleExtract entities
	 */
	@Transactional
	public ResponseEntity<Object> getSampleExtractByMethod(final int methodKey)
	{
		final Optional<Method> methodByKey = methodRepo.findByMethodkeyAndStatus(methodKey, 1);
		if (methodByKey.isPresent())
		{
			final List<SampleExtract> extractList = extractRepo.findByMethodAndStatus(methodByKey.get(), 1);
			
			return new ResponseEntity<>(extractList, HttpStatus.OK);
		}
		else
		{
		   return new ResponseEntity<>("Method Not Found", HttpStatus.NOT_FOUND);
		}
	}
	
	/**
	 * This method is used to save list of SampleExtract entities.
	 * @param extractMap [Map] object holding SampleExtract entities to save
	 * @param mapper [ObjectMapper]to convert list of Object to List of ParserTechnique entities
	 * @param createdUser  [CreatedUser] entity referring the user done the task.
	 * @param method [Method] entity for which the SampleExtract list is to be saved
	 * @param savedTextList [List]  object referring SampleTextSplit to map SampleExtracts
	 * @param savedLineList [List]  object referring SampleLineSplit to map SampleExtracts
	 * @return  map object with saved SampleExtract entities.
	 */
	public Map<String, List<SampleExtract>> saveSampleExtract(final Map<String, SampleExtract> extractMap, ObjectMapper mapper,
			final LSuserMaster createdUser, final Method method, List<SampleTextSplit> savedTextList,
			List<SampleLineSplit> savedLineList){
		final Map<String, List<SampleExtract>>   returnObject = new HashMap<String, List<SampleExtract>>();
		
		final Collection<SampleExtract> extractList = extractMap.values();	
		if (extractList.size() == 0){
			returnObject.put("ExtractListBeforeSave", new ArrayList<SampleExtract>());
			returnObject.put("ExtractListAfterSave", new ArrayList<SampleExtract>());
			return returnObject;
		}
		else
		{
			final List<SampleExtract> sampleToSave = mapper.convertValue(extractList, 
				  	new TypeReference<List<SampleExtract>>() {});
		   
			final List<Integer> idList = new ArrayList<Integer>();	
			
			sampleToSave.forEach(item->{				
				item.setMethod(method);	
				if (item.getSampleextractkey() == null || item.getSampleextractkey() == 0){
						item.setCreatedby(createdUser);						
				}	
				else
				{
					idList.add(item.getSampleextractkey());
				}
			
				if (item.getSamplelinesplit()!= null )
				{					
					final SampleLineSplit sampleLineSplit = savedLineList.stream()
							.filter(lineSplit -> item.getSamplelinesplit().getExtractblock().equalsIgnoreCase(lineSplit.getExtractblock()))
					  .findAny()
					  .orElse(null);
					
					if (sampleLineSplit != null) {
						item.setSamplelinesplit(sampleLineSplit);
					}					
				}
				else if (item.getSampletextsplit() != null)
				{
					final SampleTextSplit sampleTextSplit = savedTextList.stream()
							  .filter(textSplit -> item.getSampletextsplit().getExtractblock().equalsIgnoreCase(textSplit.getExtractblock()))
							  .findAny()
							  .orElse(null);					
					if (sampleTextSplit != null) {
						item.setSampletextsplit(sampleTextSplit);
					}
				}
			});	
			
			final List<SampleExtract> listBeforeSave = extractRepo.findBySampleextractkeyInAndStatus(idList, 1);
		    
			final List<SampleExtract> cloneListBeforeSave	= listBeforeSave.stream().map(SampleExtract::new).collect(Collectors.toList());
			
			final List<SampleExtract> savedList  = extractRepo.save(sampleToSave);
			returnObject.put("ExtractListBeforeSave", cloneListBeforeSave);
			returnObject.put("ExtractListAfterSave", savedList);
		}
		return returnObject;
	}
	
	/**
	 * This method is used to convert the list of SampleExtract entities to xml with the difference in object
	 * before and after update for recording in Audit Trial
	 * @param listBeforeSave list of SampleExtract details before saving to DB
	 * @param savedList list of SampleExtract details after saving to DB
	 * @return string formatted xml data
	 */
	public String convertSampleExtractListToXML(final List<SampleExtract> listBeforeSave, 
			final List<SampleExtract> savedList)
	{			
		try {
			
			final SampleExtracts afterSaveList = new SampleExtracts(); 
			afterSaveList.setSampleextracts(savedList);	
			Map<Integer, Map<String, Object>> dataModified = new HashMap<Integer, Map<String, Object>>();
			for (SampleExtract objAfterSave : savedList)
			{					
				for (SampleExtract objBeforeSave : listBeforeSave)
				{
					if (objBeforeSave.getSampleextractkey().equals(objAfterSave.getSampleextractkey()))
					{				
						final DiffResult diffResult = objBeforeSave.diff(objAfterSave);
							
						Map<String, Object> diffObject = new HashMap<String, Object>();
						
//						for(Diff<?> d: diffResult.getDiffs()) {
//							diffObject.put(d.getFieldName(), d.getKey()+" -> "+d.getValue());
//						}
						dataModified.put(objBeforeSave.getSampleextractkey(), diffObject);
					}						
				}
			}
	
			
			final Map<String, String> fieldMap = new HashMap<String, String>();			
			fieldMap.put("createdby", "loginid");			
			fieldMap.put("method", "methodname");
			fieldMap.put("sampletextsplit", "extractblock");
			fieldMap.put("samplelinesplit", "extractblock");
			
//			final ReadWriteXML readWriteXML =  new ReadWriteXML();
//			return readWriteXML.saveXML(afterSaveList, SampleExtracts.class, dataModified, "listpojo", fieldMap);	
			return "";
		
		} catch ( SecurityException | IllegalArgumentException e) {
			return null;
		}

	}

}
