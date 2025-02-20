package com.agaram.eln.primary.service.sample;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.agaram.eln.primary.commonfunction.commonfunction;
import com.agaram.eln.primary.model.sample.SampleType;
import com.agaram.eln.primary.repository.sample.SampleBarcodeMapRepository;
import com.agaram.eln.primary.repository.sample.SampleTypeRepository;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import io.jsonwebtoken.lang.Arrays;

@Service
public class SampleTypeService {
	
	@Autowired
	SampleTypeRepository sampleTypeRepository;
	@Autowired
	SampleBarcodeMapRepository sampleBarcodeMapRepository;
	
	public ResponseEntity<Object> getSampleType(SampleType objSampleType) {
		List<SampleType> lstsampletype = sampleTypeRepository.
				findByNsampletypecodeNotAndNsitecodeOrNsampletypecodeNotAndNdefaultstatusOrderByNsampletypecodeDesc(-1,objSampleType.getNsitecode(),-1,4);
		return new ResponseEntity<>(lstsampletype, HttpStatus.OK);
	}
	
	public ResponseEntity<Object> modifySampleType(SampleType objSampleType) throws JsonParseException, JsonMappingException, IOException, ParseException {
		
		if(objSampleType.getNsampletypecode() == null) {
			
			List<SampleType> objlstTypes = sampleTypeRepository.
					findBySsampletypenameIgnoreCaseAndNsitecodeOrderByNsampletypecode(objSampleType.getSsampletypename(),objSampleType.getNsitecode());
			
			if(objlstTypes.isEmpty()) {
				
//				List<SampleType> objlstTypes1 = sampleTypeRepository.findAll();
//				
//				objSampleType.setNsampletypecode(objlstTypes1.size()+1);
				objSampleType.setNdefaultstatus(3);
				objSampleType.setNstatus(1);
				objSampleType.setCreatedate(commonfunction.getCurrentUtcTime());
				objSampleType.setCreateby(objSampleType.getCreateby());
				objSampleType.setInfo("IDS_SUCCESS");
				sampleBarcodeMapRepository.save(objSampleType.getLstbarcodes());
				sampleTypeRepository.save(objSampleType);
				return new ResponseEntity<>(objSampleType, HttpStatus.OK);
				
			}else {
				
				objSampleType.setInfo("IDS_FAIL_ALREADY_EXIST");
				return new ResponseEntity<>(objSampleType, HttpStatus.OK);
			}
		}
		else {
			List<SampleType> objlstTypes = sampleTypeRepository.findBySsampletypenameIgnoreCaseAndNsitecodeAndNsampletypecodeNot(
					objSampleType.getSsampletypename(),objSampleType.getNsitecode(),objSampleType.getNsampletypecode());
			
			if(objlstTypes.isEmpty()) {
				
				SampleType objMType = sampleTypeRepository.findByNsampletypecodeAndNstatus(objSampleType.getNsampletypecode(), 1);				
				
				objSampleType.setNdefaultstatus(objMType.getNdefaultstatus());
				objSampleType.setNstatus(1);
				objSampleType.setCreatedate(objMType.getCreatedate());
				objSampleType.setCreateby(objMType.getCreateby());
				objSampleType.setModifieddate(commonfunction.getCurrentUtcTime());
				
				sampleBarcodeMapRepository.save(objSampleType.getLstbarcodes());
				sampleTypeRepository.save(objSampleType);
				
				objSampleType.setInfo("IDS_SUCCESS");
				return new ResponseEntity<>(objSampleType, HttpStatus.OK);
			}else {
				
				objSampleType.setInfo("IDS_FAIL_ALREADY_EXIST");
				return new ResponseEntity<>(objSampleType, HttpStatus.OK);
			}
		}				
		
	}
	
	public SampleType RetiredSample(SampleType objSampleType) {

		objSampleType.setNstatus(-1);
		sampleTypeRepository.save(objSampleType);  	
		return objSampleType;
	}
	
	public ResponseEntity<Object> getMaterialTypeonId(SampleType objSampleType) {
		SampleType lsSampleType = sampleTypeRepository.findOne(objSampleType.getNsampletypecode());
		return new ResponseEntity<>(lsSampleType, HttpStatus.OK);
	}
}