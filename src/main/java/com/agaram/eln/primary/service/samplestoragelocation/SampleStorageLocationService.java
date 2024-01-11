package com.agaram.eln.primary.service.samplestoragelocation;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.agaram.eln.primary.commonfunction.commonfunction;
import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.material.ElnmaterialInventory;
import com.agaram.eln.primary.model.material.MaterialInventory;
import com.agaram.eln.primary.model.samplestoragelocation.SampleStorageLocation;
import com.agaram.eln.primary.model.samplestoragelocation.SampleStorageVersion;
import com.agaram.eln.primary.model.samplestoragelocation.SelectedInventoryMapped;
import com.agaram.eln.primary.repository.cfr.LScfttransactionRepository;
import com.agaram.eln.primary.repository.material.ElnmaterialInventoryRepository;
import com.agaram.eln.primary.repository.material.MaterialInventoryRepository;
import com.agaram.eln.primary.repository.samplestoragelocation.SampleStorageLocationRepository;
import com.agaram.eln.primary.repository.samplestoragelocation.SampleStorageVersionRepository;
import com.agaram.eln.primary.repository.samplestoragelocation.SelectedInventoryMappedRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class SampleStorageLocationService {

	@Autowired
	SampleStorageLocationRepository sampleStorageLocationRepository;
	@Autowired
	SampleStorageVersionRepository sampleStorageVersionRepository;
	@Autowired
	LScfttransactionRepository lScfttransactionRepository;
	@Autowired
	MaterialInventoryRepository MaterialInventoryRepository;
	@Autowired
	SelectedInventoryMappedRepository selectedInventoryMappedRepository;
	@Autowired
	ElnmaterialInventoryRepository elnmaterialInventoryRepository;

	public ResponseEntity<Object> createSampleStorageLocation(final SampleStorageLocation sampleStorageLocation, final SampleStorageVersion sampleStorageVersion, LScfttransaction Auditobj) throws JsonMappingException, JsonProcessingException {
		
		SampleStorageLocation objLocation = sampleStorageLocationRepository.findBySamplestoragelocationnameAndSitekeyAndStatus(sampleStorageLocation.getSamplestoragelocationname(), sampleStorageLocation.getSitekey(),1);

		if (objLocation == null) {
			sampleStorageLocation.setObjsilentaudit(Auditobj);
			sampleStorageLocation.setCreatedby(Auditobj.getUsername().toLowerCase());
			sampleStorageVersion.setCreatedby(Auditobj.getUsername().toLowerCase());
			sampleStorageLocationRepository.save(sampleStorageLocation);
			sampleStorageVersion.setSampleStorageLocation(sampleStorageLocation);
			try {
				Auditobj.setTransactiondate(commonfunction.getCurrentUtcTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			lScfttransactionRepository.save(Auditobj);
			sampleStorageVersionRepository.save(sampleStorageVersion);

			return new ResponseEntity<>(getAllActiveSampleStorageLocation(sampleStorageLocation.getSitekey()).getBody(),HttpStatus.OK);
		} else {
			sampleStorageLocation.setInfo("IDS_EXIST");
			return new ResponseEntity<>(sampleStorageLocation,HttpStatus.OK);
		}
	}

	public ResponseEntity<Object> updateSampleStorageLocation(final SampleStorageLocation sampleStorageLocation,final SampleStorageVersion sampleStorageVersion, LScfttransaction Auditobj)throws JsonMappingException, JsonProcessingException {
		sampleStorageLocation.setObjsilentaudit(Auditobj);
		sampleStorageLocation.setCreatedby(Auditobj.getUsername());
		sampleStorageVersion.setCreatedby(Auditobj.getUsername());
		sampleStorageLocationRepository.save(sampleStorageLocation);
		
		List<SampleStorageVersion> sampleStorageVersionList = sampleStorageVersionRepository
				.findFirstBySampleStorageLocationOrderBySamplestorageversionkeyDesc(sampleStorageLocation);
		
		sampleStorageVersionList.get(0).setJsonbresult(sampleStorageVersion.getJsonbresult());
		
		sampleStorageVersionRepository.save(sampleStorageVersionList);
		try {
			Auditobj.setTransactiondate(commonfunction.getCurrentUtcTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		lScfttransactionRepository.save(Auditobj);
		return new ResponseEntity<>(getAllActiveSampleStorageLocationWithSelectedRecord(sampleStorageLocation.getSitekey(),sampleStorageLocation.getSamplestoragelocationkey()).getBody(),HttpStatus.OK);
	}

	public ResponseEntity<Object> deleteSampleStorageLocation(final SampleStorageVersion sampleStorageVersion,LScfttransaction Auditobj) throws JsonMappingException, JsonProcessingException {

		List<Integer> lstVersionNoIntegers = new ArrayList<>();
		lstVersionNoIntegers.add(sampleStorageVersion.getVersionno());

		List<SampleStorageVersion> sampleStorageVersionList = sampleStorageVersionRepository
				.findBySampleStorageLocationAndVersionnoNotIn(sampleStorageVersion.getSampleStorageLocation(),lstVersionNoIntegers);

		Optional<SampleStorageVersion> sampleStorageVersionDelete = sampleStorageVersionRepository
				.findBySampleStorageLocationAndVersionno(sampleStorageVersion.getSampleStorageLocation(),sampleStorageVersion.getVersionno());

		Optional<SampleStorageLocation> sampleStorageLocationDelete = sampleStorageLocationRepository
				.findBySitekeyAndSamplestoragelocationkey(sampleStorageVersion.getSitekey(),sampleStorageVersion.getSampleStorageLocation().getSamplestoragelocationkey());

		final SampleStorageLocation sampleStorageLocationItem = sampleStorageLocationDelete.get();
		
		List<SelectedInventoryMapped> lstInventoryMappeds = selectedInventoryMappedRepository.findBySamplestoragelocationkey(sampleStorageLocationItem);

		if(!lstInventoryMappeds.isEmpty()) {
			
			sampleStorageLocationItem.setInfo("Delete Failed - Storage mapped with inventory");
			return new ResponseEntity<>(sampleStorageLocationItem, HttpStatus.OK);
//			return new ResponseEntity<>("Delete Failed - Storage mapped with inventory", HttpStatus.NOT_FOUND);
		}else {
			if (sampleStorageVersionList.size() > 0) {

				if (sampleStorageVersionDelete.isPresent()) {

					sampleStorageLocationItem.setStatus(-1);
					sampleStorageLocationRepository.save(sampleStorageLocationItem);
					try {
						Auditobj.setTransactiondate(commonfunction.getCurrentUtcTime());
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					lScfttransactionRepository.save(Auditobj);
					return new ResponseEntity<>(getAllActiveSampleStorageLocation(sampleStorageVersion.getSitekey()).getBody(), HttpStatus.OK);
				}
			} else {
				if (sampleStorageVersionDelete.isPresent()) {
					if (sampleStorageLocationDelete.isPresent()) {
						sampleStorageLocationItem.setStatus(-1);
						sampleStorageLocationRepository.save(sampleStorageLocationItem);
						try {
							Auditobj.setTransactiondate(commonfunction.getCurrentUtcTime());
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						lScfttransactionRepository.save(Auditobj);
						return new ResponseEntity<>(getAllActiveSampleStorageLocation(sampleStorageVersion.getSitekey()).getBody(),HttpStatus.OK);
					}
				}
			}
		}
		sampleStorageLocationItem.setInfo("Delete Failed - Result Not Found");
		return new ResponseEntity<>(sampleStorageLocationItem, HttpStatus.OK);
//		return new ResponseEntity<>("Delete Failed - Result Not Found", HttpStatus.NOT_FOUND);
	}

	public ResponseEntity<Object> getAllActiveSampleStorageLocation(Integer nsiteInteger) {

		List<SampleStorageLocation> sampleStorageLocationList = sampleStorageLocationRepository
				.findBySamplestoragelocationkeyOrSitekeyOrderBySamplestoragelocationkeyDesc(-1,nsiteInteger);

		Map<String, Object> objMap = new LinkedHashMap<String, Object>();

		if (sampleStorageLocationList != null && sampleStorageLocationList.size() > 0) {
			objMap.put("sampleStorageLocation", sampleStorageLocationList);
			objMap.put("selectedSampleStorageLocation", sampleStorageLocationList.get(0));

			List<SampleStorageVersion> sampleStorageVersionList = sampleStorageVersionRepository
					.findFirstBySampleStorageLocationOrderBySamplestorageversionkeyDesc(sampleStorageLocationList.get(0));
			objMap.put("sampleStorageVersion", sampleStorageVersionList);
			if (sampleStorageVersionList != null && sampleStorageVersionList.size() > 0) {
				objMap.put("selectedSampleStorageVersion", sampleStorageVersionList.get(0));
			}

		}
		return new ResponseEntity<>(objMap, HttpStatus.OK);
	}
	
	public ResponseEntity<Object> getActiveSampleStorageLocation(Integer nsiteInteger) {
		
		List<SampleStorageLocation> sampleStorageLocationList = sampleStorageLocationRepository
				.findBySamplestoragelocationkeyOrSitekeyAndStatusOrderBySamplestoragelocationkey(-1,nsiteInteger,1);

		Map<String, Object> objMap = new LinkedHashMap<String, Object>();

		if (sampleStorageLocationList != null && sampleStorageLocationList.size() > 0) {
			objMap.put("sampleStorageLocation", sampleStorageLocationList);
			objMap.put("selectedSampleStorageLocation", sampleStorageLocationList.get(0));

			List<SampleStorageVersion> sampleStorageVersionList = sampleStorageVersionRepository
					.findFirstBySampleStorageLocationOrderBySamplestorageversionkeyDesc(sampleStorageLocationList.get(0));
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
					.findFirstBySampleStorageLocationOrderBySamplestorageversionkeyDesc(sampleStorageLocationList.get(0));
			objMap.put("sampleStorageVersion", sampleStorageVersionList);
		}
		SampleStorageLocation objStorageLocation = sampleStorageLocationRepository
				.findBySamplestoragelocationkey(sampleStorageLocationKey);
		if (objStorageLocation != null) {
			List<SampleStorageVersion> sampleStorageVersionList = sampleStorageVersionRepository
					.findFirstBySampleStorageLocationOrderBySamplestorageversionkeyDesc(objStorageLocation);
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
					.findFirstBySampleStorageLocationOrderBySamplestorageversionkeyDesc(objStorageLocation);
			objMap.put("sampleStorageVersion", sampleStorageVersionList);

			if (sampleStorageVersionList != null && sampleStorageVersionList.size() > 0) {
				objMap.put("selectedSampleStorageVersion", sampleStorageVersionList.get(0));
			}
		}
		return new ResponseEntity<>(objMap, HttpStatus.OK);
	}
	
	public ResponseEntity<Object> setStorageLocationOnNode(SampleStorageLocation objStorageLocation,ElnmaterialInventory objInventory,
			Map<String, Object> selectedStorageId,List<SampleStorageVersion> sampleStorageVersionList) {

		SelectedInventoryMapped inventoryMapped = new SelectedInventoryMapped(); 

		if (sampleStorageVersionList != null && sampleStorageVersionList.size() > 0) {
			String path = commonfunction.findPath(sampleStorageVersionList.get(0).getJsonbresult(),selectedStorageId.get("id").toString());
			List<SelectedInventoryMapped> lstInventoryMappeds1 = selectedInventoryMappedRepository.findByNmaterialinventorycodeOrderByMappedidDesc(objInventory.getNmaterialinventorycode());
			
			if(lstInventoryMappeds1.isEmpty()) {
				
				inventoryMapped.setNmaterialinventorycode(objInventory.getNmaterialinventorycode());
				inventoryMapped.setSamplestoragelocationkey(objStorageLocation);
				inventoryMapped.setId(selectedStorageId.get("id").toString());
				inventoryMapped.setStoragepath(path);
				selectedInventoryMappedRepository.save(inventoryMapped);
			}else {
				
				lstInventoryMappeds1.get(0).setSamplestoragelocationkey(objStorageLocation);
				lstInventoryMappeds1.get(0).setId(selectedStorageId.get("id").toString());
				lstInventoryMappeds1.get(0).setStoragepath(path);
				selectedInventoryMappedRepository.save(lstInventoryMappeds1);
			}
		}
		
		return new ResponseEntity<>(HttpStatus.OK);
	}

	public Boolean getSelectedStorageItem(Map<String, Object> selectedStorageId) throws JsonProcessingException {
		List<SelectedInventoryMapped> lstItemMapped = selectedInventoryMappedRepository.findByIdOrderByMappedidDesc(selectedStorageId.get("id").toString());
		
		if(lstItemMapped.isEmpty()) {
			return true;
		}else {
			
			// Create an ObjectMapper instance
	        ObjectMapper objectMapper = new ObjectMapper();

	        // Convert the Map to a JSON string
	        String jsonString = objectMapper.writeValueAsString(selectedStorageId);
			
			JSONObject jsonObject = new JSONObject(jsonString);
	        List<String> idList = extractIds(jsonObject);
	        
	        List<SelectedInventoryMapped> lstItemMapped1 = selectedInventoryMappedRepository.findByIdIn(idList);
	        
	        if(lstItemMapped1.isEmpty()) {
	        	return true;
	        }
			
			return false;
		}
	}
	
	public static List<String> extractIds(JSONObject jsonObject) {
        List<String> idList = new ArrayList<>();

        if (jsonObject.has("id")) {
            idList.add(jsonObject.getString("id"));
        }

        if (jsonObject.has("items")) {
            JSONArray items = jsonObject.getJSONArray("items");
            for (int i = 0; i < items.length(); i++) {
                JSONObject childObject = items.getJSONObject(i);
                idList.addAll(extractIds(childObject));
            }
        }

        return idList;
    }

	public ResponseEntity<Object> getStorageIdBasedOnInvent(int inventoryCode,int nsiteInteger) {
		Map<String, Object> objMap = new LinkedHashMap<String, Object>();

		MaterialInventory objInventory = new MaterialInventory();
		objInventory.setNmaterialinventorycode(inventoryCode);

		SelectedInventoryMapped objInventoryMapped = selectedInventoryMappedRepository.findByNmaterialinventorycode(objInventory.getNmaterialinventorycode());

		if (objInventoryMapped != null) {

			objMap.put("selectedSampleStorageLocation", objInventoryMapped.getSamplestoragelocationkey());
			objMap.put("selectedStoragePath", objInventoryMapped.getStoragepath());
			objMap.put("selectedStorageId", objInventoryMapped.getId());
			
			List<SampleStorageVersion> sampleStorageVersionList = sampleStorageVersionRepository.findFirstBySampleStorageLocationOrderBySamplestorageversionkeyDesc(objInventoryMapped.getSamplestoragelocationkey());
			if (sampleStorageVersionList != null && sampleStorageVersionList.size() > 0) {
				objMap.put("selectedSampleStorageVersion", sampleStorageVersionList.get(0));
			}
		}
		List<SampleStorageLocation> sampleStorageLocationList = sampleStorageLocationRepository.findBySitekeyOrderBySamplestoragelocationkeyDesc(nsiteInteger);

		if (sampleStorageLocationList != null && sampleStorageLocationList.size() > 0) {
			objMap.put("sampleStorageLocation", sampleStorageLocationList);
		}
		return new ResponseEntity<>(objMap, HttpStatus.OK);
	}
	
}