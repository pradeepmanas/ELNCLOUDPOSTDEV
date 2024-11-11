package com.agaram.eln.primary.service.equipment;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.agaram.eln.primary.commonfunction.commonfunction;
import com.agaram.eln.primary.model.communicationsetting.CommunicationSetting;
import com.agaram.eln.primary.model.communicationsetting.ConversionType;
import com.agaram.eln.primary.model.communicationsetting.HandShake;
import com.agaram.eln.primary.model.communicationsetting.Parity;
import com.agaram.eln.primary.model.communicationsetting.ResultSampleFrom;
import com.agaram.eln.primary.model.communicationsetting.StopBits;
import com.agaram.eln.primary.model.equipment.ElnresultEquipment;
import com.agaram.eln.primary.model.equipment.Equipment;
import com.agaram.eln.primary.model.equipment.EquipmentCategory;
import com.agaram.eln.primary.model.equipment.EquipmentHistory;
import com.agaram.eln.primary.model.equipment.EquipmentType;
import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.instrumentDetails.LSlogilablimsorderdetail;
import com.agaram.eln.primary.model.instrumentsetup.InstrumentType;
import com.agaram.eln.primary.model.masters.Lslogbooks;
import com.agaram.eln.primary.model.material.Period;
import com.agaram.eln.primary.model.protocols.LSlogilabprotocoldetail;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.repository.communicationsetting.CommunicationRepository;
import com.agaram.eln.primary.repository.communicationsetting.ConversionTypeRepository;
import com.agaram.eln.primary.repository.communicationsetting.HandShakeRepository;
import com.agaram.eln.primary.repository.communicationsetting.ParityRepository;
import com.agaram.eln.primary.repository.communicationsetting.ResultSampleFromRepository;
import com.agaram.eln.primary.repository.communicationsetting.StopBistsRepository;
import com.agaram.eln.primary.repository.equipment.ElnresultEquipmentRepository;
import com.agaram.eln.primary.repository.equipment.EquipmentCategoryRepository;
import com.agaram.eln.primary.repository.equipment.EquipmentHistoryRepository;
import com.agaram.eln.primary.repository.equipment.EquipmentRepository;
import com.agaram.eln.primary.repository.equipment.EquipmentTypeRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LSlogilablimsorderdetailRepository;
import com.agaram.eln.primary.repository.instrumentsetup.InstTypeRepository;
import com.agaram.eln.primary.repository.masters.LslogbooksRepository;
import com.agaram.eln.primary.repository.material.PeriodRepository;
import com.agaram.eln.primary.repository.protocol.LSlogilabprotocoldetailRepository;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class EquipmentService {

	@Autowired
	EquipmentRepository equipmentRepository;
	@Autowired
	CommunicationRepository communicationRepository;
	@Autowired
	InstTypeRepository insttypeRepository;
	@Autowired
	ParityRepository parityRepository;
	@Autowired
	StopBistsRepository stopBistsRepository;
	@Autowired
	ResultSampleFromRepository resultSampleFromRepository;
	@Autowired
	ConversionTypeRepository conversionTypeRepository;
	@Autowired
	HandShakeRepository handShakeRepository; 
	@Autowired
	EquipmentCategoryRepository equipmentCategoryRepository;
	@Autowired
	EquipmentTypeRepository equipmentTypeRepository;
	@Autowired
	PeriodRepository periodRepository;
	@Autowired
	EquipmentHistoryRepository equipmentHistoryRepository;
	@Autowired
	LSlogilabprotocoldetailRepository lslogilabprotocoldetailRepository;
	@Autowired
	ElnresultEquipmentRepository elnresultEquipmentRepository;
	@Autowired
	LSlogilablimsorderdetailRepository lslogilablimsorderdetailRepository;
	@Autowired
	LslogbooksRepository lslogbooksRepository;

	public ResponseEntity<Object> getEquipment(Map<String, Object> inputMap) throws ParseException {
		Map<String, Object> objmap = new LinkedHashMap<String, Object>();
		
		Integer nsiteInteger = (Integer) inputMap.get("nsitecode");		
		long longFromValue = Long.parseLong(inputMap.get("fromdate").toString());
		long longToValue = Long.parseLong(inputMap.get("todate").toString());
		
		Date fromDate = new Date(longFromValue);
		Date toDate = new Date(longToValue);
		
		List<Equipment> lstEquipment = equipmentRepository
				.findByNsitecodeAndCreateddateBetweenOrderByNequipmentcodeDesc(nsiteInteger,fromDate,toDate);
		
		List<InstrumentType> lstCmmType = insttypeRepository.findAll();
		List<Parity> lstParity = parityRepository.findAll();
		List<ResultSampleFrom> lstRSIDFrom = resultSampleFromRepository.findAll();
		List<HandShake> lstHandShake = handShakeRepository.findAll();
		List<StopBits> lstStopBits= stopBistsRepository.findAll();
		List<ConversionType> lstConversionType= conversionTypeRepository.findAll();
		
		
		objmap.put("lstCmmType", lstCmmType);
		objmap.put("lstParity", lstParity);
		objmap.put("lstRSIDFrom", lstRSIDFrom);
		objmap.put("lstHandShake", lstHandShake);
		objmap.put("lstStopBits", lstStopBits);
		objmap.put("lstConversionType", lstConversionType);
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
				.findByEquipmentusedAndNsitecodeAndNstatusAndCreateddateBetweenAndLastmaintainedNotNullAndLastcallibratedNotNullOrderByNequipmentcodeDesc(true,nsiteInteger,1,fromDate,toDate);
		
		lstEquipment.addAll(equipmentRepository.findByEquipmentusedAndNsitecodeAndNstatusAndReqcalibrationAndLastmaintainedNotNullOrderByNequipmentcodeDesc(true, nsiteInteger, 1, false));
		lstEquipment.addAll(equipmentRepository.findByEquipmentusedAndNsitecodeAndNstatusAndReqmaintananceAndLastcallibratedNotNullOrderByNequipmentcodeDesc(true, nsiteInteger, 1, false));
		lstEquipment.addAll(equipmentRepository.findByEquipmentusedAndNsitecodeAndNstatusAndReqmaintananceAndReqcalibrationOrderByNequipmentcodeDesc(true, nsiteInteger, 1, false,false));
		
		objmap.put("lstEquipment", lstEquipment);
		objmap.put("objsilentaudit", inputMap.get("objsilentaudit"));
		
		return new ResponseEntity<>(objmap, HttpStatus.OK);
	}

	public ResponseEntity<Object> getEquipmentPropsForFilter(Integer nsiteInteger) {
		Map<String, Object> objmap = new LinkedHashMap<String, Object>();
		
		List<EquipmentType> lstTypes =  new ArrayList<EquipmentType>();
		List<EquipmentCategory> lstCategories = new ArrayList<EquipmentCategory>();
		List<Equipment> lstEquipments = new ArrayList<Equipment>();
		
		lstTypes = equipmentTypeRepository.findByNequipmenttypecodeNotAndNstatusAndNsitecodeOrderByNequipmenttypecodeDesc(-1,1,nsiteInteger);
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
		
		List<InstrumentType> lstCmmType = insttypeRepository.findAll();
		List<Parity> lstParity = parityRepository.findAll();
		List<ResultSampleFrom> lstRSIDFrom = resultSampleFromRepository.findAll();
		List<HandShake> lstHandShake = handShakeRepository.findAll();
		List<StopBits> lstStopBits= stopBistsRepository.findAll();
		List<ConversionType> lstConversionType= conversionTypeRepository.findAll();
		
		
		objmap.put("lstCmmType", lstCmmType);
		objmap.put("lstParity", lstParity);
		objmap.put("lstRSIDFrom", lstRSIDFrom);
		objmap.put("lstHandShake", lstHandShake);
		objmap.put("lstStopBits", lstStopBits);
		objmap.put("lstConversionType", lstConversionType);
		
		objmap.put("lstEquipment", lstEquipment);
		objmap.put("objsilentaudit", inputMap.get("objsilentaudit"));
		return new ResponseEntity<>(objmap, HttpStatus.OK);
	}

	public ResponseEntity<Object> getEquipmentProps(Integer nsiteInteger) {
		
		Map<String, Object> objmap = new LinkedHashMap<String, Object>();
		
		List<EquipmentType> lstTypes =  new ArrayList<EquipmentType>();
		List<EquipmentCategory> lstCategories = new ArrayList<EquipmentCategory>();
		
		lstTypes = equipmentTypeRepository.findByNequipmenttypecodeNotAndNstatusAndNsitecodeOrderByNequipmenttypecodeDesc(-1,1,nsiteInteger);
		if(!lstTypes.isEmpty()) {
			lstCategories = equipmentCategoryRepository.findByNsitecodeAndNstatusOrderByNequipmentcatcodeDesc(nsiteInteger, 1);
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
		
		lstTypes = equipmentTypeRepository.findByNequipmenttypecodeNotAndNstatusAndNsitecodeOrderByNequipmenttypecodeDesc(-1,1,nsiteInteger);
		if(!lstTypes.isEmpty()) {
			lstCategories = equipmentCategoryRepository.findByEquipmenttypeAndNsitecodeAndNstatusOrderByNequipmentcatcodeDesc(lstTypes.get(0),nsiteInteger, 1);
			if(!lstCategories.isEmpty()) {
				lstEquipments = equipmentRepository.findByEquipmentusedAndEquipmentcategoryAndNsitecodeAndNstatusAndLastmaintainedNotNullAndLastcallibratedNotNullOrderByNequipmentcodeDesc(true,lstCategories.get(0),nsiteInteger, 1);
				lstEquipments.addAll(equipmentRepository.findByEquipmentusedAndEquipmentcategoryAndNsitecodeAndNstatusAndReqcalibrationAndLastmaintainedNotNullOrderByNequipmentcodeDesc(true, lstCategories.get(0),nsiteInteger, 1, false));
				lstEquipments.addAll(equipmentRepository.findByEquipmentusedAndEquipmentcategoryAndNsitecodeAndNstatusAndReqmaintananceAndLastcallibratedNotNullOrderByNequipmentcodeDesc(true, lstCategories.get(0),nsiteInteger, 1, false));
				lstEquipments.addAll(equipmentRepository.findByEquipmentusedAndEquipmentcategoryAndNsitecodeAndNstatusAndReqmaintananceAndReqcalibrationOrderByNequipmentcodeDesc(true, lstCategories.get(0),nsiteInteger, 1, false,false));
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
				lstCategories = equipmentCategoryRepository.findByEquipmenttypeAndNsitecodeAndNstatusOrderByNequipmentcatcodeDesc(lstTypes.get(0), nsiteInteger, 1);
				if(!lstCategories.isEmpty()) {
					lstEquipments = equipmentRepository.findByEquipmentusedAndEquipmentcategoryAndNsitecodeAndNstatusOrderByNequipmentcodeDesc(true,lstCategories.get(0),nsiteInteger, 1);
				}	
			}
		}
		objmap.put("lstEquipments", lstEquipments);
		objmap.put("lstCategories", lstCategories);
		return new ResponseEntity<>(objmap, HttpStatus.OK);
	}
	
	public ResponseEntity<Object> getEquipmentTypeBasedCatOnTrans(Map<String, Object> inputMap) {
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
					lstEquipments = equipmentRepository.findByEquipmentusedAndEquipmentcategoryAndNsitecodeAndNstatusAndLastmaintainedNotNullAndLastcallibratedNotNull(true,lstCategories.get(0),nsiteInteger, 1);
					
					lstEquipments.addAll(equipmentRepository.findByEquipmentusedAndEquipmentcategoryAndNsitecodeAndNstatusAndReqcalibrationAndLastmaintainedNotNull(true,lstCategories.get(0),nsiteInteger, 1, false));
					lstEquipments.addAll(equipmentRepository.findByEquipmentusedAndEquipmentcategoryAndNsitecodeAndNstatusAndReqmaintananceAndLastcallibratedNotNull(true,lstCategories.get(0),nsiteInteger, 1, false));
					lstEquipments.addAll(equipmentRepository.findByEquipmentusedAndEquipmentcategoryAndNsitecodeAndNstatusAndReqmaintananceAndReqcalibration(true,lstCategories.get(0),nsiteInteger, 1, false,false));
					
				}	
			}
		}
		objmap.put("lstEquipments", lstEquipments);
		objmap.put("lstCategories", lstCategories);
		return new ResponseEntity<>(objmap, HttpStatus.OK);
	}
	
	public ResponseEntity<Object> getEquipmentCatBasedOnTrans(Map<String, Object> inputMap) {
		Map<String, Object> objmap = new LinkedHashMap<String, Object>();
		
		Integer nsiteInteger = (Integer) inputMap.get("nsitecode");
		Integer ncatcode = (Integer) inputMap.get("ncatcode");
		
		EquipmentCategory objCategory = equipmentCategoryRepository.findByNequipmentcatcode(ncatcode);
		
		List<Equipment> lstEquipments = new ArrayList<Equipment>();
		
		lstEquipments = equipmentRepository.findByEquipmentusedAndEquipmentcategoryAndNsitecodeAndNstatusAndLastmaintainedNotNullAndLastcallibratedNotNull(true,objCategory,nsiteInteger, 1);
		lstEquipments.addAll(equipmentRepository.findByEquipmentusedAndEquipmentcategoryAndNsitecodeAndNstatusAndReqcalibrationAndLastmaintainedNotNullOrderByNequipmentcodeDesc(true, objCategory,nsiteInteger, 1, false));
		lstEquipments.addAll(equipmentRepository.findByEquipmentusedAndEquipmentcategoryAndNsitecodeAndNstatusAndReqmaintananceAndLastcallibratedNotNullOrderByNequipmentcodeDesc(true, objCategory,nsiteInteger, 1, false));
		lstEquipments.addAll(equipmentRepository.findByEquipmentusedAndEquipmentcategoryAndNsitecodeAndNstatusAndReqmaintananceAndReqcalibrationOrderByNequipmentcodeDesc(true, objCategory,nsiteInteger, 1, false,false));
		
//		 List<Equipment> lstEquipments = Stream.of(
//	                equipmentRepository.findByEquipmentusedAndEquipmentcategoryAndNsitecodeAndNstatusAndLastmaintainedNotNullAndLastcallibratedNotNull(
//	                        true, objCategory, nsiteInteger, 1),
//	                equipmentRepository.findByEquipmentusedAndEquipmentcategoryAndNsitecodeAndNstatusAndReqcalibrationAndLastmaintainedNotNullOrderByNequipmentcodeDesc(
//	                        true, objCategory, nsiteInteger, 1, false),
//	                equipmentRepository.findByEquipmentusedAndEquipmentcategoryAndNsitecodeAndNstatusAndReqmaintananceAndLastcallibratedNotNullOrderByNequipmentcodeDesc(
//	                        true, objCategory, nsiteInteger, 1, false),
//	                equipmentRepository.findByEquipmentusedAndEquipmentcategoryAndNsitecodeAndNstatusAndReqmaintananceAndReqcalibrationOrderByNequipmentcodeDesc(
//	                        true, objCategory, nsiteInteger, 1, false, false)
//	        ).flatMap(Collection::stream)
//	         .collect(Collectors.toList());
		
		objmap.put("lstEquipments", lstEquipments);
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
		
		Equipment objEquipment = equipmentRepository.findByNsitecodeAndSequipmentnameAndEquipmentcategoryAndNstatus(obj.getNsitecode(),obj.getSequipmentname(),obj.getEquipmentcategory(),1);
		
		obj.setResponse(new Response());
		String sformattype = "{yyyy}/{99999}";

		if(objEquipment == null) {

			obj.setCreateddate(commonfunction.getCurrentUtcTime());
			equipmentRepository.save(obj);
			
			String stridformat = returnSubstring(obj.getSequipmentname()) + "/" + getfnFormat(obj.getNequipmentcode(), sformattype);
	
			obj.setSequipmentid(stridformat);
			
			equipmentRepository.save(obj);
			if(obj.getCmmsetting().equals(true)) {
				
				
				communicationRepository.save(obj.getCommunicationsetting());
			}
//			else {
//				obj.setCommunicationsetting(null);
//				obj.setCmmsetting(null);
//				equipmentRepository.save(obj);
//				
//				communicationRepository.delete(obj.getCommunicationsetting());
//			}
			
			obj.getResponse().setInformation("IDS_SAVE_SUCCEED");
			obj.getResponse().setStatus(true);
			
			return new ResponseEntity<>(obj, HttpStatus.OK);
		}else {
			obj.getResponse().setInformation("IDS_SAVE_FAIL");
			obj.getResponse().setStatus(false);
			return new ResponseEntity<>(obj, HttpStatus.OK);
		}
	}
	
	public ResponseEntity<Object> updateELNEquipment(Equipment obj) {
		
		Equipment objElnmaterial = equipmentRepository.findByNsitecodeAndSequipmentnameAndEquipmentcategoryAndNequipmentcodeNotAndNstatus(obj.getNsitecode(),obj.getSequipmentname(),obj.getEquipmentcategory(),obj.getNequipmentcode(),1);
		
		obj.setResponse(new Response());
		
		if(objElnmaterial == null) {
			
			Equipment objMaterial = equipmentRepository.findOne(obj.getNequipmentcode());

			obj.setSequipmentid(objMaterial.getSequipmentid());
			obj.setCreateddate(objMaterial.getCreateddate());
			
			if(obj.getReqcalibration()) {
				obj.setLastcallibrated(obj.getLastcallibrated());
				obj.setCallibrationdate(obj.getCallibrationdate());
			}else {
				obj.setLastcallibrated(obj.getLastcallibrated());
				obj.setCallibrationdate(obj.getCallibrationdate());
			}
			
			obj.setCallibrationvalue(obj.getCallibrationvalue());
			obj.setCallibrationperiod(obj.getCallibrationperiod());
			
			if(obj.getReqmaintanance()) {
				obj.setLastmaintained(obj.getLastmaintained());
				obj.setManintanancedate(obj.getManintanancedate());
			}else {
				obj.setLastmaintained(obj.getLastmaintained());
				obj.setManintanancedate(obj.getManintanancedate());
			}
			
			obj.setManintanancevalue(obj.getManintanancevalue());
			obj.setMaintananceperiod(obj.getMaintananceperiod());
			
			if(Boolean.FALSE.equals(obj.getCmmsetting()) && objMaterial.getCommunicationsetting() != null ) {
				
				CommunicationSetting cmsTemp = objMaterial.getCommunicationsetting();
				
				obj.setCommunicationsetting(null);
				obj.setCmmsetting(null);
				equipmentRepository.save(obj);
				
				/**
				 * Find by cmmsetting if its present it delete
				 */
				communicationRepository.findByCmmsettingcode(cmsTemp.getCmmsettingcode()).ifPresent(communicationRepository::delete);
				
			}else {
				equipmentRepository.save(obj);
				
				communicationRepository.save(obj.getCommunicationsetting());
			}
			
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
			
			createEquipmentHistory(obj,1);
			
			return new ResponseEntity<>(objEquipment, HttpStatus.OK);
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

	public ResponseEntity<Object> OsearchEquipment(Map<String, Object> inputMap) {
		Map<String, Object> objmap = new LinkedHashMap<String, Object>();
		Integer nsiteInteger = new ObjectMapper().convertValue(inputMap.get("nsitecode"), Integer.class);
	    String searchString = (String) inputMap.get("searchString"); 
		List<Equipment> lstEquipments = equipmentRepository.findBySequipmentnameStartingWithIgnoreCaseAndNsitecodeOrderByNequipmentcodeDesc(searchString,nsiteInteger);
		objmap.put("lstEquipment", lstEquipments);		
		return new ResponseEntity<>(objmap, HttpStatus.OK);
	}

	@SuppressWarnings({ "unchecked", "unused" })
	public ResponseEntity<Object> onGetEquipmentSelect(Map<String, Object> inputMap) throws JsonParseException, JsonMappingException, IOException {
		Map<String, Object> objmap = new LinkedHashMap<String, Object>();
		ObjectMapper Objmapper = new ObjectMapper();
		Integer user = (Integer) inputMap.get("user");
		Integer selectedScreen = (Integer) inputMap.get("selectedScreen");
		Map<String, Object> objResultMap = (Map<String, Object>) inputMap.get("selectedProtocol"); 
		List<Integer> nequipmentcode = (List<Integer>) inputMap.get("nequipmentcode");
		
		List<Equipment> lstEquipments = equipmentRepository.findByNequipmentcodeIn(nequipmentcode);
		objmap.put("lstEquipment", lstEquipments);
		
		if(!lstEquipments.isEmpty() && selectedScreen != null && selectedScreen == 2) {
			LSuserMaster objUser = new LSuserMaster();
			objUser.setUsercode(user);
			LSlogilabprotocoldetail objDetail = lslogilabprotocoldetailRepository.findOne(Long.valueOf(objResultMap.get("protocolordercode").toString()));
			createEquipmentResultUsedLst(objDetail,objUser,lstEquipments);	
		}
		
		return new ResponseEntity<>(objmap, HttpStatus.OK);
	}
	
	public void createEquipmentResultUsedLst(LSlogilabprotocoldetail objDetail,LSuserMaster objUser,List<Equipment> lstEquipments) throws JsonParseException, JsonMappingException, IOException {

		List<ElnresultEquipment> lstEquipments2 = new ArrayList<ElnresultEquipment>(); 
		
		lstEquipments.stream().peek(objInventory -> {
			
			ElnresultEquipment resultEquipment = new ElnresultEquipment();
			resultEquipment.setLstestmasterlocal(null);
			
			resultEquipment.setCreatedby(objUser);
			resultEquipment.setBatchid(objDetail.getProtoclordername());
			resultEquipment.setNequipmentcode(objInventory.getNequipmentcode());
			resultEquipment.setNequipmentcatcode(objInventory.getEquipmentcategory().getNequipmentcatcode());
			resultEquipment.setNequipmenttypecode(objInventory.getEquipmenttype().getNequipmenttypecode());
			resultEquipment.setOrdercode(Long.valueOf(objDetail.getProtocolordercode()));
			resultEquipment.setTransactionscreen(3);
			resultEquipment.setTemplatecode(Integer.parseInt(objDetail.getProtocolordercode().toString()));
			resultEquipment.setNstatus(1);
			resultEquipment.setResponse(new Response());
			resultEquipment.getResponse().setStatus(true);
			try {
				resultEquipment.setCreateddate(commonfunction.getCurrentUtcTime());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			lstEquipments2.add(resultEquipment);
			
		}).collect(Collectors.toList());
	
		if(!lstEquipments2.isEmpty()) {
			elnresultEquipmentRepository.save(lstEquipments2);
		}
	}

	public Boolean onCheckItsUsed(Equipment objEquipment) {
//		Equipment objEquipment = equipmentRepository.findOne(obj.getNequipmentcode());
		objEquipment.setResponse(new Response());
		
		List<ElnresultEquipment> objEquipments = elnresultEquipmentRepository.findByNequipmentcode(objEquipment.getNequipmentcode());
		
		if(!objEquipments.isEmpty()) {
			
			List<String> batchid = objEquipments.stream().map(ElnresultEquipment::getBatchid).distinct().collect(Collectors.toList());
			
			List<LSlogilablimsorderdetail> lstOrders = lslogilablimsorderdetailRepository.findByBatchidInAndOrderflag(batchid,"N");
			List<LSlogilabprotocoldetail> lstPro = lslogilabprotocoldetailRepository.findByProtoclordernameInAndOrderflag(batchid,"N");
			List<Lslogbooks> lstLog = lslogbooksRepository.findByLogbookidIn(batchid);
			
			if(!lstOrders.isEmpty() || !lstPro.isEmpty() || !lstLog.isEmpty()){
				return true;
			}else {
				return false;
			}
			
		}else {
			return false;
		}
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
