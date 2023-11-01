package com.agaram.eln.primary.service.material;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.agaram.eln.primary.model.material.MaterialConfig;
import com.agaram.eln.primary.model.material.MaterialType;
import com.agaram.eln.primary.repository.material.MaterialConfigRepository;
import com.agaram.eln.primary.repository.material.MaterialTypeRepository;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

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

	
//	public ResponseEntity<Object> createMaterialType(MaterialType objMaterialType) throws JsonParseException, JsonMappingException, IOException {
//		List<MaterialConfig> objLstConfig = materialConfigRepository.findAllByOrderByNmaterialconfigcodeDesc();
//		
//		if(!objLstConfig.isEmpty()) {
//			List<MaterialType> objlstTypes = materialTypeRepository.findAllByOrderByNmaterialtypecodeDesc();
//			if(!objlstTypes.isEmpty()) {
//				ObjectMapper objectMapper = new ObjectMapper();
//				List<Object> dataList = objectMapper.readValue(objMaterialType.getJsonconfigdata(), List.class);
//				
//				objMaterialType.setNmaterialtypecode(objlstTypes.get(0).getNmaterialtypecode() + 1);
//				objMaterialType.setNdefaultstatus(4);
//				objMaterialType.setNstatus(1);
//				materialTypeRepository.save(objMaterialType);
//				
//				MaterialConfig objConfig = new MaterialConfig();
//				objConfig.setNmaterialconfigcode(objLstConfig.get(0).getNmaterialconfigcode()+1);
//				objConfig.setNmaterialtypecode(objMaterialType.getNmaterialtypecode());
//				objConfig.setNformcode(40);
//				objConfig.setNstatus(1);
//				objConfig.setJsondata(dataList);
//				
//				materialConfigRepository.save(objConfig);
//				
//				return new ResponseEntity<>(objMaterialType, HttpStatus.OK);
//			}			
//		}
//		
//		return new ResponseEntity<>(HttpStatus.OK);
//	}
	
	public ResponseEntity<Object> createMaterialType(MaterialType objMaterialType) throws JsonParseException, JsonMappingException, IOException {
		
		
		if(objMaterialType.getNmaterialtypecode() == null) {
			List<MaterialType> objlstTypes = materialTypeRepository.findByAndSmaterialtypenameAndNdefaultstatusAndNsitecodeOrderByNmaterialtypecode(
					objMaterialType.getSmaterialtypename(),4,objMaterialType.getNsitecode());
			
			if(objlstTypes.isEmpty()) {
				
				List<MaterialType> objlstTypes1 = materialTypeRepository.findAll();
				
				objMaterialType.setNmaterialtypecode(objlstTypes1.size()+1);
				objMaterialType.setNdefaultstatus(3);
				objMaterialType.setNstatus(1);
				
				materialTypeRepository.save(objMaterialType);
				objMaterialType.setInfo("IDS_SUCCESS");
				return new ResponseEntity<>(objMaterialType, HttpStatus.OK);
			}else {
				
				objMaterialType.setInfo("IDS_FAIL");
				return new ResponseEntity<>(objMaterialType, HttpStatus.OK);
			}
		}
		else {
			List<MaterialType> objlstTypes = materialTypeRepository.findByAndSmaterialtypenameAndNdefaultstatusAndNsitecodeAndNmaterialtypecodeNot(
					objMaterialType.getSmaterialtypename(),4,objMaterialType.getNsitecode(),objMaterialType.getNmaterialtypecode());
			
			if(objlstTypes.isEmpty()) {
				
				MaterialType objMType = materialTypeRepository.findByNmaterialtypecodeAndNstatus(objMaterialType.getNmaterialtypecode(), 1);				
				
				objMaterialType.setNdefaultstatus(objMType.getNdefaultstatus());
				objMaterialType.setNstatus(1);
				materialTypeRepository.save(objMaterialType);
				
				objMaterialType.setInfo("IDS_SUCCESS");
				return new ResponseEntity<>(objMaterialType, HttpStatus.OK);
			}else {
				
				objMaterialType.setInfo("IDS_FAIL");
				return new ResponseEntity<>(objMaterialType, HttpStatus.OK);
			}
		}				
		
	}
}