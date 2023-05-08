package com.agaram.eln.primary.service.material;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.agaram.eln.primary.model.material.MaterialConfig;
import com.agaram.eln.primary.model.material.MaterialType;
import com.agaram.eln.primary.repository.material.MaterialConfigRepository;
import com.agaram.eln.primary.repository.material.MaterialTypeRepository;

@Service
public class MaterialTypeService {
	
	@Autowired
	MaterialTypeRepository materialTypeRepository;
	@Autowired
	MaterialConfigRepository materialConfigRepository;

	public ResponseEntity<Object> getMaterialType() {
		return new ResponseEntity<>(materialTypeRepository.findByNmaterialtypecodeNotAndNstatusOrderByNmaterialtypecode(-1,1), HttpStatus.OK);
	}

	public ResponseEntity<Object> getMaterialTypeField(MaterialType objMaterialType) {
		MaterialConfig objConfig = materialConfigRepository.findByNformcodeAndNmaterialtypecodeAndNstatus(40, objMaterialType.getNmaterialtypecode(), 1);
		if(objConfig != null) {
			return new ResponseEntity<>(objConfig.getJsondata(),HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

	public ResponseEntity<Object> updateMaterialTypeField(MaterialConfig objMaterialType) {
		MaterialConfig objConfig = materialConfigRepository.findByNformcodeAndNmaterialtypecodeAndNstatus(40, objMaterialType.getNmaterialtypecode(), 1);
		if(objConfig != null) {
			objConfig.setJsondata(objMaterialType.getJsondata());
			materialConfigRepository.save(objConfig);
		}else {
			
			if(objMaterialType.getNmaterialtypecode() == 5) {
				objMaterialType.setNmaterialconfigcode(12);
			}else if(objMaterialType.getNmaterialtypecode() == 6) {
				objMaterialType.setNmaterialconfigcode(13);
			}else if(objMaterialType.getNmaterialtypecode() == 7) {
				objMaterialType.setNmaterialconfigcode(14);
			}
			objMaterialType.setNformcode(40);
			objMaterialType.setNstatus(1);
			materialConfigRepository.save(objMaterialType);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
}