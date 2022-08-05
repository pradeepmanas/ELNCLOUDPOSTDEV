package com.agaram.eln.primary.service.SampleStorageLocation;

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

import com.agaram.eln.primary.model.SampleStorageLocation.SampleStorageLocation;
import com.agaram.eln.primary.model.SampleStorageLocation.SampleStorageVersion;
import com.agaram.eln.primary.repository.SampleStorageLocation.SampleStorageLocationRepository;
import com.agaram.eln.primary.repository.SampleStorageLocation.SampleStorageVersionRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Service
public class SampleStorageLocationService {

	@Autowired
	SampleStorageLocationRepository sampleStorageLocationRepository;
	@Autowired
	SampleStorageVersionRepository sampleStorageVersionRepository;

	public ResponseEntity<Object> createSampleStorageLocation(final SampleStorageLocation sampleStorageLocation,
			final SampleStorageVersion sampleStorageVersion) throws JsonMappingException, JsonProcessingException {
		sampleStorageLocationRepository.save(sampleStorageLocation);
		sampleStorageVersion.setSampleStorageLocation(sampleStorageLocation);
		sampleStorageVersionRepository.save(sampleStorageVersion);

		return new ResponseEntity<>(getAllActiveSampleStorageLocation().getBody(), HttpStatus.OK);
	}

	public ResponseEntity<Object> updateSampleStorageLocation(final SampleStorageLocation sampleStorageLocation,
			final SampleStorageVersion sampleStorageVersion) throws JsonMappingException, JsonProcessingException {
		sampleStorageLocationRepository.save(sampleStorageLocation);
		if (sampleStorageVersion.getApprovalstatus() == 26) {
			int maxVersion = sampleStorageVersionRepository
					.getMaxVersionNo(sampleStorageLocation.getSamplestoragelocationkey());
			sampleStorageVersion.setSamplestorageversionkey(null);
			sampleStorageVersion.setVersionno(maxVersion);
			sampleStorageVersion.setApprovalstatus(8);
			sampleStorageVersionRepository.save(sampleStorageVersion);
			return new ResponseEntity<>(
					sampleStorageVersionRepository.findBySampleStorageLocation(sampleStorageLocation), HttpStatus.OK);
		} else {
			sampleStorageVersionRepository.save(sampleStorageVersion);
			return new ResponseEntity<>(sampleStorageVersion, HttpStatus.OK);
		}

	}

	public ResponseEntity<Object> deleteSampleStorageLocation(final SampleStorageVersion sampleStorageVersion)
			throws JsonMappingException, JsonProcessingException {
		List<Integer> lstVersionNoIntegers = new ArrayList<>();
		lstVersionNoIntegers.add(sampleStorageVersion.getVersionno());

		List<SampleStorageVersion> sampleStorageVersionList = sampleStorageVersionRepository
				.findBySampleStorageLocationAndVersionnoNotIn(sampleStorageVersion.getSampleStorageLocation(),
						lstVersionNoIntegers);

		Optional<SampleStorageVersion> sampleStorageVersionDelete = sampleStorageVersionRepository
				.findBySampleStorageLocationAndVersionno(sampleStorageVersion.getSampleStorageLocation(),
						sampleStorageVersion.getVersionno());
		if (sampleStorageVersionList.size() > 0) {

			if (sampleStorageVersionDelete.isPresent()) {

				sampleStorageVersionRepository.delete(sampleStorageVersionDelete.get());

				return new ResponseEntity<>(getAllActiveSampleStorageLocation().getBody(), HttpStatus.OK);
			}
		} else {
			if (sampleStorageVersionDelete.isPresent()) {

				Optional<SampleStorageLocation> sampleStorageLocationDelete = sampleStorageLocationRepository
						.findByStatusAndSamplestoragelocationkey(1,
								sampleStorageVersion.getSampleStorageLocation().getSamplestoragelocationkey());

				if (sampleStorageLocationDelete.isPresent()) {
					final SampleStorageLocation sampleStorageLocationItem = sampleStorageLocationDelete.get();
					sampleStorageLocationItem.setStatus(-1);
					sampleStorageLocationRepository.save(sampleStorageLocationItem);
					sampleStorageVersionRepository.delete(sampleStorageVersionDelete.get());
					return new ResponseEntity<>(getAllActiveSampleStorageLocation().getBody(), HttpStatus.OK);
				}
			}
		}

		return new ResponseEntity<>("Delete Failed - Result Not Found", HttpStatus.NOT_FOUND);

	}

	public ResponseEntity<Object> getAllActiveSampleStorageLocation() {
//		List<SampleStorageLocation> sampleStorageLocationList = 
//				sampleStorageLocationRepository.findByStatus(1,Sort(Sort.Direction.DESC, "samplestoragelocationkey"));

		List<SampleStorageLocation> sampleStorageLocationList = sampleStorageLocationRepository.findByStatus(1);

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

	
	public ResponseEntity<Object> getActiveSampleStorageLocationByKey(final int sampleStorageLocationKey) {
		return new ResponseEntity<>(
				sampleStorageLocationRepository.findByStatusAndSamplestoragelocationkey(1, sampleStorageLocationKey),
				HttpStatus.OK);
	}
}
