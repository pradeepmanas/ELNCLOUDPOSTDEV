package com.agaram.eln.primary.service.equipment;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.agaram.eln.primary.commonfunction.commonfunction;
import com.agaram.eln.primary.global.Enumeration;
import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.equipment.EquipmentCategory;
import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.repository.equipment.EquipmentCategoryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class EquipmentCategoryService {

	@Autowired
	EquipmentCategoryRepository equipmentCategoryRepository;

	public ResponseEntity<Object> getequipmentCategory(Integer nsiteInteger) {
		List<EquipmentCategory> equipmentCategories = equipmentCategoryRepository.findByNsitecodeOrderByNequipmentcatcodeDesc(nsiteInteger);
		return new ResponseEntity<>(equipmentCategories, HttpStatus.OK);
	}

	public ResponseEntity<Object> createEquipmentCategory(Map<String, Object> inputMap) throws ParseException { 
		
		ObjectMapper objmapper = new ObjectMapper();
		final EquipmentCategory objEquipmentCategory = objmapper.convertValue(inputMap.get("EquipmentCategory"),EquipmentCategory.class);
		objEquipmentCategory.setResponse(new Response());
		
		List<EquipmentCategory> lstgetMaterialCategory = equipmentCategoryRepository.findBySequipmentcatnameIgnoreCaseAndNsitecode(objEquipmentCategory.getSequipmentcatname(),objEquipmentCategory.getNsitecode());
		
		LSuserMaster objMaster = new LSuserMaster();
		objMaster.setUsercode(objEquipmentCategory.getObjsilentaudit().getLsuserMaster());
		
		if (lstgetMaterialCategory.isEmpty()) {

			objEquipmentCategory.setEquipmenttype(objEquipmentCategory.getEquipmenttype());
			objEquipmentCategory.setSequipmentcatname(objEquipmentCategory.getSequipmentcatname());
			objEquipmentCategory.setSdescription(objEquipmentCategory.getSdescription());
			objEquipmentCategory.setNsitecode(objEquipmentCategory.getNsitecode());
			objEquipmentCategory.setCreateby(objMaster);
			objEquipmentCategory.setCreatedate(commonfunction.getCurrentUtcTime());
			equipmentCategoryRepository.save(objEquipmentCategory);
			
			objEquipmentCategory.getResponse().setStatus(true);
			objEquipmentCategory.getResponse().setInformation("IDS_SUCCESS");
			return new ResponseEntity<>(objEquipmentCategory, HttpStatus.OK);

		} else {
			objEquipmentCategory.getResponse().setStatus(false);
			objEquipmentCategory.getResponse().setInformation("IDS_ALREADYEXIST");
			return new ResponseEntity<>(objEquipmentCategory, HttpStatus.OK);
		}
	}

	public ResponseEntity<Object> updateEquipmentCategory(EquipmentCategory materialCategory) {
		final EquipmentCategory objMaterialCategory = equipmentCategoryRepository.findByNequipmentcatcode(materialCategory.getNequipmentcatcode());
		materialCategory.setResponse(new Response());
		if (objMaterialCategory == null) {
			materialCategory.getResponse().setStatus(false);
			materialCategory.getResponse().setInformation("IDS_ALREADYDELETED");
			return new ResponseEntity<>(materialCategory, HttpStatus.OK);
		} else {
			final EquipmentCategory materialCategoryObj = equipmentCategoryRepository.findByNsitecodeAndSequipmentcatnameIgnoreCase(materialCategory.getNsitecode(),materialCategory.getSequipmentcatname());
			
			if (materialCategoryObj == null || (materialCategoryObj.getNequipmentcatcode().equals(materialCategory.getNequipmentcatcode()))) {
				equipmentCategoryRepository.save(materialCategory);
				materialCategory.getResponse().setStatus(true);
				materialCategory.getResponse().setInformation("IDS_SUCCESS");
				return new ResponseEntity<>(materialCategory, HttpStatus.OK);
			} else {
				materialCategory.getResponse().setStatus(false);
				materialCategory.getResponse().setInformation("IDS_ALREADYEXIST");
				return new ResponseEntity<>(materialCategory, HttpStatus.OK);
			}
		}
	}

	public ResponseEntity<Object> deleteEquipmentCategory(EquipmentCategory objEquipmentCategory, LScfttransaction objsilentaudit) {
		final EquipmentCategory objMaterialCategory = equipmentCategoryRepository.findByNequipmentcatcode(objEquipmentCategory.getNequipmentcatcode());
		if (objMaterialCategory == null) {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		} else {
			objMaterialCategory.setNstatus(Enumeration.TransactionStatus.DELETED.gettransactionstatus());
			equipmentCategoryRepository.save(objMaterialCategory);
			objEquipmentCategory.setObjsilentaudit(objsilentaudit);
			return getequipmentCategory(objEquipmentCategory.getNsitecode());
		}
	}
}
