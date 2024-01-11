package com.agaram.eln.primary.service.equipment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.agaram.eln.primary.commonfunction.commonfunction;
import com.agaram.eln.primary.model.equipment.Equipment;
import com.agaram.eln.primary.model.equipment.EquipmentCategory;
import com.agaram.eln.primary.model.equipment.EquipmentHistory;
import com.agaram.eln.primary.model.equipment.EquipmentType;
import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.material.Period;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.repository.equipment.EquipmentCategoryRepository;
import com.agaram.eln.primary.repository.equipment.EquipmentHistoryRepository;
import com.agaram.eln.primary.repository.equipment.EquipmentRepository;
import com.agaram.eln.primary.repository.equipment.EquipmentTypeRepository;
import com.agaram.eln.primary.repository.material.PeriodRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class EquipmentService {

	@Autowired
	EquipmentRepository equipmentRepository;
	@Autowired
	EquipmentCategoryRepository equipmentCategoryRepository;
	@Autowired
	EquipmentTypeRepository equipmentTypeRepository;
	@Autowired
	PeriodRepository periodRepository;
	@Autowired
	EquipmentHistoryRepository equipmentHistoryRepository;

	public ResponseEntity<Object> getEquipment(Map<String, Object> inputMap) throws ParseException {
		Map<String, Object> objmap = new LinkedHashMap<String, Object>();
		
		Integer nsiteInteger = (Integer) inputMap.get("nsitecode");		
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
	
	public ResponseEntity<Object> getEquipmentOnTransaction(Map<String, Object> inputMap) throws ParseException {
		Map<String, Object> objmap = new LinkedHashMap<String, Object>();
		
		Integer nsiteInteger = (Integer) inputMap.get("nsitecode");		
		long longFromValue = Long.parseLong(inputMap.get("fromdate").toString());
		long longToValue = Long.parseLong(inputMap.get("todate").toString());
		
		Date fromDate = new Date(longFromValue);
		Date toDate = new Date(longToValue);
		
		List<Equipment> lstEquipment = equipmentRepository
				.findByEquipmentusedAndNsitecodeAndCreateddateBetweenOrderByNequipmentcodeDesc(true,nsiteInteger,fromDate,toDate);
		
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
		
		List<Period> lstPeriods = periodRepository.findByNstatusOrderByNperiodcode(1);
		objmap.put("lstPeriods", lstPeriods);
		objmap.put("lstCategories", lstCategories);
		objmap.put("lstType", lstTypes);
		
		return new ResponseEntity<>(objmap, HttpStatus.OK);
	}
	
	public ResponseEntity<Object> getEquipmentTransactionProps(Integer nsiteInteger) {
		
		Map<String, Object> objmap = new LinkedHashMap<String, Object>();
		
		List<EquipmentType> lstTypes =  new ArrayList<EquipmentType>();
		List<EquipmentCategory> lstCategories = new ArrayList<EquipmentCategory>();
		List<Equipment> lstEquipments = new ArrayList<Equipment>();
		
		lstTypes = equipmentTypeRepository.findByNequipmenttypecodeNotAndNstatusAndNsitecodeOrNequipmenttypecodeNotAndNstatusAndNdefaultstatus(-1,1,nsiteInteger,-1,1,4);
		if(!lstTypes.isEmpty()) {
			lstCategories = equipmentCategoryRepository.findByEquipmenttypeAndNsitecodeAndNstatus(lstTypes.get(0),nsiteInteger, 1);
			if(!lstCategories.isEmpty()) {
				lstEquipments = equipmentRepository.findByEquipmentusedAndEquipmentcategoryAndNsitecodeAndNstatus(true,lstCategories.get(0),nsiteInteger, 1);
			}	
		}	
		
		objmap.put("lstEquipments", lstEquipments);
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
		List<Equipment> lstEquipments = new ArrayList<Equipment>();
		
		if(ntypecode==-1) {
			lstCategories = equipmentCategoryRepository.findByNsitecodeAndNstatus(nsiteInteger, 1);
		}else {
			if(!lstTypes.isEmpty()) {
				lstCategories = equipmentCategoryRepository.findByEquipmenttypeAndNsitecodeAndNstatus(lstTypes.get(0), nsiteInteger, 1);
				if(!lstCategories.isEmpty()) {
					lstEquipments = equipmentRepository.findByEquipmentusedAndEquipmentcategoryAndNsitecodeAndNstatus(true,lstCategories.get(0),nsiteInteger, 1);
				}	
			}
		}
		objmap.put("lstEquipments", lstEquipments);
		objmap.put("lstCategories", lstCategories);
		return new ResponseEntity<>(objmap, HttpStatus.OK);
	}
	
	public ResponseEntity<Object> getEquipmentCatBased(Map<String, Object> inputMap) {
		Map<String, Object> objmap = new LinkedHashMap<String, Object>();
		
		Integer nsiteInteger = (Integer) inputMap.get("nsitecode");
		Integer ncatcode = (Integer) inputMap.get("ncatcode");
		
		EquipmentCategory objCategory = equipmentCategoryRepository.findByNequipmentcatcode(ncatcode);
		List<Equipment> lstEquipments = new ArrayList<Equipment>();
		
		lstEquipments = equipmentRepository.findByEquipmentusedAndEquipmentcategoryAndNsitecodeAndNstatus(true,objCategory,nsiteInteger, 1);
				
		objmap.put("lstEquipments", lstEquipments);
		return new ResponseEntity<>(objmap, HttpStatus.OK);
	}

	public ResponseEntity<Object> createEquipment(Equipment obj) throws Exception {
		
		Equipment objEquipment = equipmentRepository.findByNsitecodeAndSequipmentnameAndEquipmentcategory(obj.getNsitecode(),obj.getSequipmentname(),obj.getEquipmentcategory());
		
		obj.setResponse(new Response());
		String sformattype = "{yyyy}/{99999}";
		LSuserMaster objMaster = new LSuserMaster();
		objMaster.setUsercode(obj.getObjsilentaudit().getLsuserMaster());
		
		if(objEquipment == null) {

			obj.setCreateby(objMaster);
			obj.setCreateddate(commonfunction.getCurrentUtcTime());
			equipmentRepository.save(obj);
			
			String stridformat = returnSubstring(obj.getSequipmentname()) + "/" + getfnFormat(obj.getNequipmentcode(), sformattype);
	
			obj.setSequipmentid(stridformat);
			
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
	
	public String returnSubstring (String name) {
		if (name.length() > 3) {
			return name.substring(0, 3);
        } else {
        	return name;
        }
	}
	
	public String getfnFormat(int sequenceno, String sFormat) throws Exception {
		// init(timestamp);

		if (sFormat != null) {
			while (sFormat.contains("{")) {
				int start = sFormat.indexOf('{');
				int end = sFormat.indexOf('}');

				String subString = sFormat.substring(start + 1, end);
				if (subString.equals("yy") || subString.equals("YY")) {
					SimpleDateFormat sdf = new SimpleDateFormat("yy", Locale.getDefault());
					sdf.toPattern();
					Date date = new Date();
					String replaceString = sdf.format(date);
					sFormat = sFormat.replace('{' + subString + '}', replaceString);
				} else if (subString.equals("yyyy") || subString.equals("YYYY")) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy", Locale.getDefault());
					sdf.toPattern();
					Date date = new Date();
					String replaceString = sdf.format(date);
					sFormat = sFormat.replace('{' + subString + '}', replaceString);
				} else if (subString.equals("mm") || subString.equals("MM")) {
					SimpleDateFormat sdf = new SimpleDateFormat("MM", Locale.getDefault());
					sdf.toPattern();
					Date date = new Date();
					String replaceString = sdf.format(date);
					sFormat = sFormat.replace('{' + subString + '}', replaceString);
				} else if (subString.equals("mon") || subString.equals("MON")) {
					SimpleDateFormat sdf = new SimpleDateFormat("MON", Locale.getDefault());
					sdf.toPattern();
					Date date = new Date();
					String replaceString = sdf.format(date);
					sFormat = sFormat.replace('{' + subString + '}', replaceString);
				} else if (subString.equals("dd") || subString.equals("DD")) {
					SimpleDateFormat sdf = new SimpleDateFormat("dd", Locale.getDefault());
					sdf.toPattern();
					Date date = new Date();
					String replaceString = sdf.format(date);
					sFormat = sFormat.replace('{' + subString + '}', replaceString);
				} else if (subString.matches("9+")) {
					String seqPadding = "%0" + subString.length() + "d";
					String replaceString = String.format(seqPadding, sequenceno);
					sFormat = sFormat.replace('{' + subString + '}', replaceString);
				}

				else {
					sFormat = sFormat.replace('{' + subString + '}', subString);
				}
			}
		}
		return sFormat;
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
	
	@SuppressWarnings("unused")
	public ResponseEntity<Object> updateStatus(Equipment obj) {
		
		Equipment objEquipment = equipmentRepository.findOne(obj.getNequipmentcode());
		
		objEquipment.setResponse(new Response());
		
		if(objEquipment != null) {
			
			objEquipment.setEquipmentused(obj.getEquipmentused() ? false : true);
			equipmentRepository.save(objEquipment);
			
			objEquipment.getResponse().setInformation("IDS_SAVE_SUCCEED");
			objEquipment.getResponse().setStatus(true);
			
			return new ResponseEntity<>(objEquipment, HttpStatus.OK);
		}else {
			objEquipment.getResponse().setInformation("IDS_SAVE_FAIL");
			objEquipment.getResponse().setStatus(false);
			return new ResponseEntity<>(obj, HttpStatus.OK);
		}
	}
	
	@SuppressWarnings("unused")
	public ResponseEntity<Object> updateElnEquipmentCallibrate(Equipment obj) throws ParseException {
		
		Equipment objEquipment = equipmentRepository.findOne(obj.getNequipmentcode());
		
		objEquipment.setResponse(new Response());
		
		if(objEquipment != null) {
			
			objEquipment.setLastcallibrated(obj.getLastcallibrated());
			objEquipment.setCallibrationdate(obj.getCallibrationdate());
			equipmentRepository.save(objEquipment);
			
			objEquipment.getResponse().setInformation("IDS_SAVE_SUCCEED");
			objEquipment.getResponse().setStatus(true);
			
			createEquipmentHistory(objEquipment,1);
			
			return new ResponseEntity<>(obj, HttpStatus.OK);
		}else {
			objEquipment.getResponse().setInformation("IDS_SAVE_FAIL");
			objEquipment.getResponse().setStatus(false);
			return new ResponseEntity<>(obj, HttpStatus.OK);
		}
	}

	@SuppressWarnings("unused")
	public ResponseEntity<Object> updateElnEquipmentMaintanance(Equipment obj) throws ParseException {
		
		Equipment objEquipment = equipmentRepository.findOne(obj.getNequipmentcode());
		
		objEquipment.setResponse(new Response());
		
		if(objEquipment != null) {
			
			objEquipment.setLastmaintained(obj.getLastmaintained());
			objEquipment.setManintanancedate(obj.getManintanancedate());
			equipmentRepository.save(objEquipment);
			
			objEquipment.getResponse().setInformation("IDS_SAVE_SUCCEED");
			objEquipment.getResponse().setStatus(true);
			
			createEquipmentHistory(obj,2);
			
			return new ResponseEntity<>(objEquipment, HttpStatus.OK);
		}else {
			objEquipment.getResponse().setInformation("IDS_SAVE_FAIL");
			objEquipment.getResponse().setStatus(false);
			return new ResponseEntity<>(obj, HttpStatus.OK);
		}
	}
	
	public void createEquipmentHistory(Equipment obj,Integer historytype) throws ParseException {
		
		EquipmentHistory objHistory = new EquipmentHistory();
		
		objHistory.setHistorytype(historytype);
		objHistory.setNequipmentcode(obj.getNequipmentcode());
		objHistory.setCreatedby(obj.getCreateby());
		objHistory.setLastcallibrated(obj.getLastcallibrated());
		objHistory.setCallibrationdate(obj.getCallibrationdate());
		objHistory.setLastmaintained(obj.getLastmaintained());
		objHistory.setManintanancedate(obj.getManintanancedate());
		objHistory.setNstatus(1);
		objHistory.setCreateddate(commonfunction.getCurrentUtcTime());
		
		equipmentHistoryRepository.save(objHistory);
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
	
//	public ResponseEntity<Object> createEquipmentResultUsed(Map<String, Object> inputMap) throws JsonParseException, JsonMappingException, IOException {

//		ObjectMapper Objmapper = new ObjectMapper();

//		final LScfttransaction cft = Objmapper.convertValue(inputMap.get("silentAudit"), LScfttransaction.class);
//		final Equipment objEquipFromMap = Objmapper.convertValue(inputMap.get("selectedEquipment"),Equipment.class);
//		final Map<String, Object> objResultMap = (Map<String, Object>) inputMap.get("resultObject");
		
//		final LStestmasterlocal objTest = new LStestmasterlocal();
//		objTest.setTestcode((Integer) objResultMap.get("testcode"));

//		Equipment objInventory = equipmentRepository.findOne(objEquipFromMap.getNequipmentcode());
//
//		Integer user = (Integer) inputMap.get("user");
//		
//		LSuserMaster objUser = new LSuserMaster();
//		objUser.setUsercode(user);
//
//		ElnresultEquipment resultEquipment = new ElnresultEquipment();
//		
//		resultEquipment.setLstestmasterlocal(null);
//		resultEquipment.setCreatedby(objUser);
//		resultEquipment.setBatchid(objResultMap.get("batchid").toString());
//		resultEquipment.setNequipmentcode(objInventory.getNequipmentcode());
//		resultEquipment.setNequipmentcatcode(objInventory.getEquipmentcategory().getNequipmentcatcode());
//		resultEquipment.setNequipmenttypecode(objInventory.getEquipmenttype().getNequipmenttypecode());
//		resultEquipment.setOrdercode(Long.valueOf(objResultMap.get("ordercode").toString()));
//		resultEquipment.setTransactionscreen(Integer.parseInt(objResultMap.get("transactionscreen").toString()));
//		resultEquipment.setTemplatecode(Integer.parseInt(objResultMap.get("templatecode").toString()));
//		resultEquipment.setNstatus(1);
//		resultEquipment.setResponse(new Response());
//		resultEquipment.getResponse().setStatus(true);
//		try {
//			resultEquipment.setCreateddate(commonfunction.getCurrentUtcTime());
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//		
//		elnresultEquipmentRepository.save(resultEquipment);

//		return new ResponseEntity<>(resultEquipment, HttpStatus.OK);
//	}
}
