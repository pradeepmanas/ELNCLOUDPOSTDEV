package com.agaram.eln.primary.service.equipment;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.agaram.eln.primary.commonfunction.commonfunction;
import com.agaram.eln.primary.model.equipment.Equipment;
import com.agaram.eln.primary.model.equipment.EquipmentCategory;
import com.agaram.eln.primary.model.equipment.EquipmentType;
import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.repository.equipment.EquipmentCategoryRepository;
import com.agaram.eln.primary.repository.equipment.EquipmentRepository;
import com.agaram.eln.primary.repository.equipment.EquipmentTypeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class EquipmentService {

	@Autowired
	EquipmentRepository equipmentRepository;
	
	@Autowired
	EquipmentCategoryRepository equipmentCategoryRepository;
	
	@Autowired
	EquipmentTypeRepository equipmentTypeRepository;

	public ResponseEntity<Object> getEquipment(Map<String, Object> inputMap) throws ParseException {
		Map<String, Object> objmap = new LinkedHashMap<String, Object>();
//		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		
		Integer nsiteInteger = (Integer) inputMap.get("nsitecode");
//		Date fromDate = simpleDateFormat.parse((String) inputMap.get("fromdate"));
//		Date toDate = simpleDateFormat.parse((String) inputMap.get("todate"));
		
		long longFromValue = Long.parseLong(inputMap.get("fromdate").toString());
		long longToValue = Long.parseLong(inputMap.get("todate").toString());
		
		Date fromDate = new Date(longFromValue);
		Date toDate = new Date(longToValue);
		
		List<Equipment> lstEquipment = equipmentRepository
				.findByNsitecodeAndCreateddateBetweenOrderByNequipmentcodeDesc(nsiteInteger,fromDate,toDate);
		
		objmap.put("lstEquipment", lstEquipment);
		objmap.put("objsilentaudit", inputMap.get("objsilentaudit"));
		
		return new ResponseEntity<>(objmap, HttpStatus.OK);
	}

	public ResponseEntity<Object> getEquipmentPropsForFilter(Integer nsiteInteger) {
		Map<String, Object> objmap = new LinkedHashMap<String, Object>();
		
		List<EquipmentType> lstTypes =  new ArrayList<EquipmentType>();
		List<EquipmentCategory> lstCategories = new ArrayList<EquipmentCategory>();
		List<Equipment> lstEquipments = new ArrayList<Equipment>();
		
		lstTypes = equipmentTypeRepository.findByNequipmenttypecodeNotAndNstatusAndNsitecodeOrNequipmenttypecodeNotAndNstatusAndNdefaultstatus(-1,1,nsiteInteger,-1,1,4);
		if(!lstTypes.isEmpty()) {
			lstCategories = equipmentCategoryRepository.findByNsitecodeAndNstatus(nsiteInteger, 1);
			if(!lstCategories.isEmpty()) {
				lstEquipments = equipmentRepository.findByNsitecodeAndNstatusOrderByNequipmentcodeDesc(nsiteInteger,1);
			}
		}	
		
		objmap.put("lstEquipment", lstEquipments);		
		objmap.put("lstCategories", lstCategories);
		objmap.put("lstType", lstTypes);
		
		return new ResponseEntity<>(objmap, HttpStatus.OK);
	}

	public ResponseEntity<Object> getEquipmentByFilter(Map<String, Object> inputMap) {
		Map<String, Object> objmap = new LinkedHashMap<String, Object>();
		ObjectMapper objmapper = new ObjectMapper();
		
		long longFromValue = Long.parseLong(inputMap.get("fromdate").toString());
		long longToValue = Long.parseLong(inputMap.get("todate").toString());
		
		Integer nsiteInteger = (Integer) inputMap.get("nsitecode");
		Date fromDate = new Date(longFromValue);
		Date toDate = new Date(longToValue);
		
		EquipmentType objType = objmapper.convertValue(inputMap.get("equipmenttype"), EquipmentType.class);
		EquipmentCategory objCategory = objmapper.convertValue(inputMap.get("equipmentcategory"), EquipmentCategory.class);
		
		List<Equipment> lstEquipment = new ArrayList<>();
		
		if((objType == null || objType.getNequipmenttypecode() == null || objType.getNequipmenttypecode() == -1) && 
				(objCategory == null || objCategory.getNequipmentcatcode() == null || objCategory.getNequipmentcatcode() == -1)) {
			lstEquipment = 
					equipmentRepository.findByNsitecodeAndCreateddateBetweenOrderByNequipmentcodeDesc(
							nsiteInteger,fromDate,toDate);
		}else if((objType == null || objType.getNequipmenttypecode() == null || objType.getNequipmenttypecode() == -1) 
				&& objCategory != null && objCategory.getNequipmentcatcode() != -1) {
			lstEquipment = 
					equipmentRepository.findByEquipmentcategoryAndNsitecodeAndCreateddateBetweenOrderByNequipmentcodeDesc(
							objCategory,nsiteInteger,fromDate,toDate);
		}else if(objType != null && objType.getNequipmenttypecode() != -1 &&
				(objCategory == null || objCategory.getNequipmentcatcode() == null || objCategory.getNequipmentcatcode() == -1)) {
			lstEquipment = 
					equipmentRepository.findByEquipmenttypeAndNsitecodeAndCreateddateBetweenOrderByNequipmentcodeDesc(
							objType,nsiteInteger,fromDate,toDate);
		}else {
			lstEquipment = 
					equipmentRepository.findByEquipmenttypeAndEquipmentcategoryAndNsitecodeAndCreateddateBetweenOrderByNequipmentcodeDesc(
							objType,objCategory,nsiteInteger,fromDate,toDate);
		}		
		
		objmap.put("lstEquipment", lstEquipment);
		objmap.put("objsilentaudit", inputMap.get("objsilentaudit"));
		return new ResponseEntity<>(objmap, HttpStatus.OK);
	}

	public ResponseEntity<Object> getEquipmentProps(Integer nsiteInteger) {
		
		Map<String, Object> objmap = new LinkedHashMap<String, Object>();
		
		List<EquipmentType> lstTypes =  new ArrayList<EquipmentType>();
		List<EquipmentCategory> lstCategories = new ArrayList<EquipmentCategory>();
		
		lstTypes = equipmentTypeRepository.findByNequipmenttypecodeNotAndNstatusAndNsitecodeOrNequipmenttypecodeNotAndNstatusAndNdefaultstatus(-1,1,nsiteInteger,-1,1,4);
		if(!lstTypes.isEmpty()) {
			lstCategories = equipmentCategoryRepository.findByNsitecodeAndNstatus(nsiteInteger, 1);
		}	
			
		objmap.put("lstCategories", lstCategories);
		objmap.put("lstType", lstTypes);
		
		return new ResponseEntity<>(objmap, HttpStatus.OK);
	}

	public ResponseEntity<Object> getEquipmentTypeBasedCat(Map<String, Object> inputMap) {
		Map<String, Object> objmap = new LinkedHashMap<String, Object>();
		
		Integer nsiteInteger = (Integer) inputMap.get("nsitecode");
		Integer ntypecode = (Integer) inputMap.get("ntypecode");
		
		List<EquipmentType> lstTypes = equipmentTypeRepository.findByNequipmenttypecode(ntypecode);
		List<EquipmentCategory> lstCategories = new ArrayList<EquipmentCategory>();
		
		if(ntypecode==-1) {
			lstCategories = equipmentCategoryRepository.findByNsitecodeAndNstatus(nsiteInteger, 1);
		}else {
			if(!lstTypes.isEmpty()) {
				lstCategories = equipmentCategoryRepository.findByEquipmenttypeAndNsitecodeAndNstatus(lstTypes.get(0), nsiteInteger, 1);		
			}
		}
		
		objmap.put("lstCategories", lstCategories);
		return new ResponseEntity<>(objmap, HttpStatus.OK);
	}

	public ResponseEntity<Object> createEquipment(Equipment obj) throws ParseException {
		
		Equipment objEquipment = equipmentRepository.findByNsitecodeAndSequipmentnameAndEquipmentcategory(obj.getNsitecode(),obj.getSequipmentname(),obj.getEquipmentcategory());
		
		obj.setResponse(new Response());
		
		LSuserMaster objMaster = new LSuserMaster();
		objMaster.setUsercode(obj.getObjsilentaudit().getLsuserMaster());
		
		if(objEquipment == null) {

			obj.setCreateby(objMaster);
			obj.setCreateddate(commonfunction.getCurrentUtcTime());
			equipmentRepository.save(obj);
			
			obj.getResponse().setInformation("IDS_SAVE_SUCCEED");
			obj.getResponse().setStatus(true);
			
			return new ResponseEntity<>(obj, HttpStatus.OK);
		}else {
			obj.getResponse().setInformation("IDS_SAVE_FAIL");
			obj.getResponse().setStatus(false);
			return new ResponseEntity<>(obj, HttpStatus.OK);
		}
	}

	public ResponseEntity<Object> updateEquipment(Equipment obj) {
		
		Equipment objEquipment = equipmentRepository.findByNsitecodeAndSequipmentnameAndEquipmentcategoryAndNequipmentcodeNot(obj.getNsitecode(),obj.getSequipmentname(),obj.getEquipmentcategory(),obj.getNequipmentcode());
		
		obj.setResponse(new Response());
		
		if(objEquipment == null) {
			
			Equipment objClass = equipmentRepository.findOne(obj.getNequipmentcode());

			obj.setCreateddate(objClass.getCreateddate());
			equipmentRepository.save(obj);
			
			obj.getResponse().setInformation("IDS_SAVE_SUCCEED");
			obj.getResponse().setStatus(true);
			
			return new ResponseEntity<>(obj, HttpStatus.OK);
		}else {
			obj.getResponse().setInformation("IDS_SAVE_FAIL");
			obj.getResponse().setStatus(false);
			return new ResponseEntity<>(obj, HttpStatus.OK);
		}
	}

	public ResponseEntity<Object> getEquipmentBySearchField(Map<String, Object> inputMap) {
		Map<String, Object> objmap = new LinkedHashMap<String, Object>();
		Integer nsiteInteger = (Integer) inputMap.get("nsitecode");
	    String searchString = (String) inputMap.get("searchString");
	    
	    List<Equipment> lstEquipments = equipmentRepository.findBySequipmentnameStartingWithIgnoreCaseAndNsitecode(searchString,nsiteInteger);
			
		objmap.put("lstEquipment", lstEquipments);		
		return new ResponseEntity<>(objmap, HttpStatus.OK);
	}

	public ResponseEntity<Object> OsearchEquipment(String searchname) {
		Map<String, Object> objmap = new LinkedHashMap<String, Object>();
		 List<Equipment> lstEquipments = equipmentRepository.findBySequipmentnameStartingWithIgnoreCase(searchname);
		objmap.put("lstEquipment", lstEquipments);		
		return new ResponseEntity<>(objmap, HttpStatus.OK);
	}

	public ResponseEntity<Object> onGetEquipmentSelect(Map<String, Object> inputMap) {
		Map<String, Object> objmap = new LinkedHashMap<String, Object>();
		@SuppressWarnings("unchecked")
		List<Integer> nequipmentcode = (List<Integer>) inputMap.get("nequipmentcode");
		List<Equipment> lstEquipments = equipmentRepository.findByNequipmentcodeIn(nequipmentcode);
		objmap.put("lstEquipment", lstEquipments);
		return new ResponseEntity<>(objmap, HttpStatus.OK);
	}
}
