package com.agaram.eln.primary.service.samplestoragelocation;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.samplestoragelocation.SampleStorageLocation;
import com.agaram.eln.primary.model.samplestoragelocation.SampleStorageVersion;
import com.agaram.eln.primary.repository.samplestoragelocation.SampleStorageLocationRepository;
import com.agaram.eln.primary.repository.samplestoragelocation.SampleStorageVersionRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Service
public class SampleStorageLocationService {

	@Autowired
	SampleStorageLocationRepository sampleStorageLocationRepository;
	@Autowired
	SampleStorageVersionRepository sampleStorageVersionRepository;

//	@SuppressWarnings("unchecked")
	public ResponseEntity<Object> createSampleStorageLocation(final SampleStorageLocation sampleStorageLocation,
			final SampleStorageVersion sampleStorageVersion, LScfttransaction Auditobj)
			throws JsonMappingException, JsonProcessingException {
//		Map<String, Object> objmap = new LinkedHashMap<String, Object>();
		SampleStorageLocation objLocation = sampleStorageLocationRepository.findBySamplestoragelocationnameAndSitekey(
				sampleStorageLocation.getSamplestoragelocationname(), sampleStorageLocation.getSitekey());

		if (objLocation == null) {
			sampleStorageLocation.setObjsilentaudit(Auditobj);
			sampleStorageLocationRepository.save(sampleStorageLocation);
			sampleStorageVersion.setSampleStorageLocation(sampleStorageLocation);
			sampleStorageVersion.setObjsilentaudit(Auditobj);
			sampleStorageVersionRepository.save(sampleStorageVersion);

//			objmap.putAll((Map<String, Object>) getAllActiveSampleStorageLocation(sampleStorageLocation.getSitekey()).getBody());
//			objmap.put("objsilentaudit",Auditobj);
			return new ResponseEntity<>(getAllActiveSampleStorageLocation(sampleStorageLocation.getSitekey()).getBody(),
					HttpStatus.OK);
//			return new ResponseEntity<>(objmap, HttpStatus.OK);
		} else {
			// return new ResponseEntity<>(objmap, HttpStatus.OK);
			return new ResponseEntity<>(getAllActiveSampleStorageLocation(sampleStorageLocation.getSitekey()).getBody(),
					HttpStatus.CONFLICT);
		}
	}

	public ResponseEntity<Object> updateSampleStorageLocation(final SampleStorageLocation sampleStorageLocation,
			final SampleStorageVersion sampleStorageVersion, LScfttransaction Auditobj)
			throws JsonMappingException, JsonProcessingException {
		sampleStorageLocation.setObjsilentaudit(Auditobj);
		sampleStorageLocationRepository.save(sampleStorageLocation);
//		if (sampleStorageVersion.getApprovalstatus() == 26) {
//			int maxVersion = sampleStorageVersionRepository
//					.getMaxVersionNo(sampleStorageLocation.getSamplestoragelocationkey());
//			sampleStorageVersion.setSamplestorageversionkey(null);
//			sampleStorageVersion.setVersionno(maxVersion);
//			sampleStorageVersion.setApprovalstatus(8);
//			sampleStorageVersionRepository.save(sampleStorageVersion);
//			return new ResponseEntity<>(
//					sampleStorageVersionRepository.findBySampleStorageLocation(sampleStorageLocation), HttpStatus.OK);
//		} else {
		sampleStorageVersion.setObjsilentaudit(Auditobj);
		sampleStorageVersionRepository.save(sampleStorageVersion);
		return new ResponseEntity<>(
				getAllActiveSampleStorageLocationWithSelectedRecord(sampleStorageLocation.getSitekey(),
						sampleStorageLocation.getSamplestoragelocationkey()).getBody(),
				HttpStatus.OK);
//		return new ResponseEntity<>(sampleStorageVersion, HttpStatus.OK);
//		}
	}

//	@SuppressWarnings("unchecked")
	public ResponseEntity<Object> deleteSampleStorageLocation(final SampleStorageVersion sampleStorageVersion,
			LScfttransaction Auditobj) throws JsonMappingException, JsonProcessingException {
//		Map<String, Object> objmap = new LinkedHashMap<String, Object>();
		List<Integer> lstVersionNoIntegers = new ArrayList<>();
		lstVersionNoIntegers.add(sampleStorageVersion.getVersionno());

		List<SampleStorageVersion> sampleStorageVersionList = sampleStorageVersionRepository
				.findBySampleStorageLocationAndVersionnoNotIn(sampleStorageVersion.getSampleStorageLocation(),
						lstVersionNoIntegers);

		Optional<SampleStorageVersion> sampleStorageVersionDelete = sampleStorageVersionRepository
				.findBySampleStorageLocationAndVersionno(sampleStorageVersion.getSampleStorageLocation(),
						sampleStorageVersion.getVersionno());

		Optional<SampleStorageLocation> sampleStorageLocationDelete = sampleStorageLocationRepository
				.findBySitekeyAndSamplestoragelocationkey(sampleStorageVersion.getSitekey(),
						sampleStorageVersion.getSampleStorageLocation().getSamplestoragelocationkey());

		final SampleStorageLocation sampleStorageLocationItem = sampleStorageLocationDelete.get();

		if (sampleStorageVersionList.size() > 0) {

			if (sampleStorageVersionDelete.isPresent()) {

				sampleStorageLocationItem.setStatus(-1);
				sampleStorageLocationRepository.save(sampleStorageLocationItem);
//				sampleStorageVersionRepository.delete(sampleStorageVersionDelete.get());

//				objmap.putAll((Map<String, Object>)getAllActiveSampleStorageLocation(sampleStorageVersion.getSitekey()).getBody());
//				objmap.put("objsilentaudit",Auditobj);
//				return new ResponseEntity<>(objmap,HttpStatus.OK);
				return new ResponseEntity<>(
						getAllActiveSampleStorageLocation(sampleStorageVersion.getSitekey()).getBody(), HttpStatus.OK);
			}
		} else {
			if (sampleStorageVersionDelete.isPresent()) {

				if (sampleStorageLocationDelete.isPresent()) {

					sampleStorageLocationItem.setStatus(-1);
					sampleStorageLocationRepository.save(sampleStorageLocationItem);
//					sampleStorageVersionRepository.delete(sampleStorageVersionDelete.get());
//					objmap.putAll((Map<String, Object>) getAllActiveSampleStorageLocation(sampleStorageVersion.getSitekey()).getBody());
//					objmap.put("objsilentaudit",Auditobj);
					return new ResponseEntity<>(
							getAllActiveSampleStorageLocation(sampleStorageVersion.getSitekey()).getBody(),
							HttpStatus.OK);
//					return new ResponseEntity<>(objmap,HttpStatus.OK);
				}
			}
		}

		return new ResponseEntity<>("Delete Failed - Result Not Found", HttpStatus.NOT_FOUND);

	}

	public ResponseEntity<Object> getAllActiveSampleStorageLocation(Integer nsiteInteger) {

		List<SampleStorageLocation> sampleStorageLocationList = sampleStorageLocationRepository
				.findBySitekeyOrderBySamplestoragelocationkeyDesc(nsiteInteger);

		Map<String, Object> objMap = new LinkedHashMap<String, Object>();

		if (sampleStorageLocationList != null && sampleStorageLocationList.size() > 0) {
			objMap.put("sampleStorageLocation", sampleStorageLocationList);
			objMap.put("selectedSampleStorageLocation", sampleStorageLocationList.get(0));

			List<SampleStorageVersion> sampleStorageVersionList = sampleStorageVersionRepository
					.findBySampleStorageLocation(sampleStorageLocationList.get(0));
			objMap.put("sampleStorageVersion", sampleStorageVersionList);
			if (sampleStorageVersionList != null && sampleStorageVersionList.size() > 0) {
				objMap.put("selectedSampleStorageVersion", sampleStorageVersionList.get(0));
			}

		}
		return new ResponseEntity<>(objMap, HttpStatus.OK);
	}

	public ResponseEntity<Object> getAllActiveSampleStorageLocationWithSelectedRecord(Integer nsiteInteger,
			Integer sampleStorageLocationKey) {
		Map<String, Object> objMap = new LinkedHashMap<String, Object>();
		List<SampleStorageLocation> sampleStorageLocationList = sampleStorageLocationRepository
				.findBySitekeyOrderBySamplestoragelocationkeyDesc(nsiteInteger);
		objMap.put("sampleStorageLocation", sampleStorageLocationList);
		if (sampleStorageLocationList != null && sampleStorageLocationList.size() > 0) {
			List<SampleStorageVersion> sampleStorageVersionList = sampleStorageVersionRepository
					.findBySampleStorageLocation(sampleStorageLocationList.get(0));
			objMap.put("sampleStorageVersion", sampleStorageVersionList);
		}
		SampleStorageLocation objStorageLocation = sampleStorageLocationRepository
				.findBySamplestoragelocationkey(sampleStorageLocationKey);
		if (objStorageLocation != null) {
			List<SampleStorageVersion> sampleStorageVersionList = sampleStorageVersionRepository
					.findBySampleStorageLocation(objStorageLocation);
			objStorageLocation.setSampleStorageVersion(sampleStorageVersionList);
			objMap.put("selectedSampleStorageLocation", objStorageLocation);
			objMap.put("sampleStorageVersion", sampleStorageVersionList);
			if (sampleStorageVersionList != null && sampleStorageVersionList.size() > 0) {
				objMap.put("selectedSampleStorageVersion", sampleStorageVersionList.get(0));
			}
		}
		return new ResponseEntity<>(objMap, HttpStatus.OK);
	}

	public ResponseEntity<Object> getActiveSampleStorageLocationByKey(final int sampleStorageLocationKey) {

		Map<String, Object> objMap = new LinkedHashMap<String, Object>();

		SampleStorageLocation objStorageLocation = sampleStorageLocationRepository
				.findBySamplestoragelocationkey(sampleStorageLocationKey);

		if (objStorageLocation != null) {

			objMap.put("selectedSampleStorageLocation", objStorageLocation);

			List<SampleStorageVersion> sampleStorageVersionList = sampleStorageVersionRepository
					.findBySampleStorageLocation(objStorageLocation);
			objMap.put("sampleStorageVersion", sampleStorageVersionList);

			if (sampleStorageVersionList != null && sampleStorageVersionList.size() > 0) {
				objMap.put("selectedSampleStorageVersion", sampleStorageVersionList.get(0));
			}
		}
		return new ResponseEntity<>(objMap, HttpStatus.OK);
	}
}
