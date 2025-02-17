package com.agaram.eln.primary.service.sample;

import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.persistence.Query;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.agaram.eln.primary.commonfunction.commonfunction;
import com.agaram.eln.primary.global.Enumeration;
import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.sample.SampleCategory;
import com.agaram.eln.primary.model.sample.SampleType;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.repository.sample.SampleCategoryRepository;
import com.agaram.eln.primary.repository.sample.SampleTypeRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class SampleCategoryService {
	
	@Autowired
	SampleTypeRepository sampleTypeRepository;
	@Autowired
	SampleCategoryRepository sampleCategoryRepository;
	
	public ResponseEntity<Object> getSampleType(Integer nsitecode) {

		List<SampleType> lstgetSampleType = sampleTypeRepository
				.findByNsampletypecodeNotAndNstatusAndNsitecodeOrNsampletypecodeNotAndNstatusAndNdefaultstatusOrderByNsampletypecodeDesc(-1,
						1, nsitecode, -1, 1, 4);
		return new ResponseEntity<>(lstgetSampleType, HttpStatus.OK);
	}

	public ResponseEntity<Object> getSampleCategory(Integer nsitecode) {

		List<SampleCategory> lstgetSampleCategory = sampleCategoryRepository
				.findByNsitecodeOrNdefaultstatusOrderByNsamplecatcodeDesc(nsitecode, 3);
		return new ResponseEntity<>(lstgetSampleCategory, HttpStatus.OK);
	}

	public ResponseEntity<Object> createSampleCategory(Map<String, Object> inputMap) throws Exception {
		ObjectMapper objmapper = new ObjectMapper();
		final SampleCategory sampleCategory = objmapper.convertValue(inputMap.get("SampleCategory"),
				SampleCategory.class);
		sampleCategory.setResponse(new Response());

		List<SampleCategory> lstgetSampleCategory = sampleCategoryRepository
				.findBySsamplecatnameIgnoreCaseAndNsitecode(sampleCategory.getSsamplecatname(),
						sampleCategory.getNsitecode());

		LSuserMaster objMaster = new LSuserMaster();
		objMaster.setUsercode(sampleCategory.getObjsilentaudit().getLsuserMaster());

		if (lstgetSampleCategory.isEmpty()) {

			sampleCategory.setSsampletypename(sampleCategory.getSsampletypename());
			sampleCategory.setNsampletypecode(sampleCategory.getNsampletypecode());
			sampleCategory.setSsamplecatname(sampleCategory.getSsamplecatname());
			sampleCategory.setSdescription(sampleCategory.getSdescription());
			sampleCategory.setNsitecode(sampleCategory.getNsitecode());
			sampleCategory.setNactivestatus(0);
			sampleCategory.setNuserrolecode(0);
			sampleCategory.setCreateby(objMaster);
			sampleCategory.setCreatedate(commonfunction.getCurrentUtcTime());
			sampleCategoryRepository.save(sampleCategory);

			sampleCategory.getResponse().setStatus(true);
			sampleCategory.getResponse().setInformation("IDS_SUCCESS");
			return new ResponseEntity<>(sampleCategory, HttpStatus.OK);

		} else {
			sampleCategory.getResponse().setStatus(false);
			sampleCategory.getResponse().setInformation("IDS_ALREADYEXIST");
			return new ResponseEntity<>(sampleCategory, HttpStatus.OK);
		}
	}
	
	public ResponseEntity<Object> updateSampleCategory(SampleCategory sampleCategory) throws ParseException {
		final SampleCategory objSampleCategory = sampleCategoryRepository
				.findByNsamplecatcode(sampleCategory.getNsamplecatcode());
		sampleCategory.setResponse(new Response());
		if (objSampleCategory == null) {
			sampleCategory.getResponse().setStatus(false);
			sampleCategory.getResponse().setInformation("IDS_ALREADYDELETED");
			return new ResponseEntity<>(sampleCategory, HttpStatus.OK);
		} else {
			final SampleCategory sampleCategoryObj = sampleCategoryRepository
					.findByNsitecodeAndSsamplecatnameIgnoreCase(sampleCategory.getNsitecode(),
							sampleCategory.getSsamplecatname());

			if (sampleCategoryObj == null
					|| (sampleCategoryObj.getNsamplecatcode().equals(sampleCategory.getNsamplecatcode()))) {
				sampleCategory.setModifieddate(commonfunction.getCurrentUtcTime());
				sampleCategoryRepository.save(sampleCategory);
				sampleCategory.getResponse().setStatus(true);
				sampleCategory.getResponse().setInformation("IDS_SUCCESS");
				return new ResponseEntity<>(sampleCategory, HttpStatus.OK);
			} else {
				sampleCategory.getResponse().setStatus(false);
				sampleCategory.getResponse().setInformation("IDS_ALREADYEXIST");
				return new ResponseEntity<>(sampleCategory, HttpStatus.OK);
			}
		}
	}
	
	public ResponseEntity<Object> retireSampleCategory(SampleCategory sampleCategory, LScfttransaction obj) {
		final SampleCategory objSampleCategory = sampleCategoryRepository
				.findByNsamplecatcode(sampleCategory.getNsamplecatcode());
		if (objSampleCategory == null) {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		} else {
			objSampleCategory.setNstatus(Enumeration.TransactionStatus.DELETED.gettransactionstatus());
			sampleCategoryRepository.save(objSampleCategory);
			sampleCategory.setObjsilentaudit(obj);
			return getSampleCategory(sampleCategory.getNsitecode());
		}
	}
}