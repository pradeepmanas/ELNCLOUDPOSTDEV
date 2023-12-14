package com.agaram.eln.primary.service.equipment;

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.agaram.eln.primary.commonfunction.commonfunction;
import com.agaram.eln.primary.model.equipment.EquipmentType;
import com.agaram.eln.primary.repository.equipment.EquipmentTypeRepository;

@Service
public class EquipmentTypeService {

	@Autowired
	EquipmentTypeRepository equipmentTypeRepository;
	
	public ResponseEntity<Object> getEquipmentype(EquipmentType objMaterialType) {
		return new ResponseEntity<>(equipmentTypeRepository.
				findByNequipmenttypecodeNotAndNstatusAndNsitecodeOrNequipmenttypecodeNotAndNstatusAndNdefaultstatusOrderByNequipmenttypecodeDesc(-1,1,objMaterialType.getNsitecode(),-1,1,4), HttpStatus.OK);
	}

	public ResponseEntity<Object> createEquipmentType(EquipmentType objMaterialType) throws ParseException {
		
		if(objMaterialType.getNequipmenttypecode() == null) {
			List<EquipmentType> objlstTypes = equipmentTypeRepository.findBySequipmenttypenameIgnoreCaseAndNsitecodeOrderByNequipmenttypecode(objMaterialType.getSequipmenttypename(),objMaterialType.getNsitecode());
			
			if(objlstTypes.isEmpty()) {
				
				objMaterialType.setNdefaultstatus(3);
				objMaterialType.setNstatus(1);
				objMaterialType.setCreatedate(commonfunction.getCurrentUtcTime());
				objMaterialType.setCreateby(objMaterialType.getCreateby());
				
				equipmentTypeRepository.save(objMaterialType);
				objMaterialType.setInfo("IDS_SUCCESS");
				return new ResponseEntity<>(objMaterialType, HttpStatus.OK);
			}else {
				
				objMaterialType.setInfo("IDS_FAIL_ALREADY_EXIST");
				return new ResponseEntity<>(objMaterialType, HttpStatus.OK);
			}
		}
		else {
			List<EquipmentType> objlstTypes = equipmentTypeRepository.findBySequipmenttypenameIgnoreCaseAndNsitecodeAndNequipmenttypecodeNot(objMaterialType.getSequipmenttypename(),objMaterialType.getNsitecode(),objMaterialType.getNequipmenttypecode());
			
			if(objlstTypes.isEmpty()) {
				
				EquipmentType objMType = equipmentTypeRepository.findByNequipmenttypecodeAndNstatus(objMaterialType.getNequipmenttypecode(), 1);				
				
				objMaterialType.setNdefaultstatus(objMType.getNdefaultstatus());
				objMaterialType.setNstatus(1);
				objMaterialType.setCreatedate(objMType.getCreatedate());
				objMaterialType.setCreateby(objMType.getCreateby());
				
				equipmentTypeRepository.save(objMaterialType);
				
				objMaterialType.setInfo("IDS_SUCCESS");
				return new ResponseEntity<>(objMaterialType, HttpStatus.OK);
			}else {
				
				objMaterialType.setInfo("IDS_FAIL_ALREADY_EXIST");
				return new ResponseEntity<>(objMaterialType, HttpStatus.OK);
			}
		}	
	}

	public ResponseEntity<Object> getEquipmentTypeField(EquipmentType objMaterialType) {
		// TODO Auto-generated method stub
		return null;
	}

}
