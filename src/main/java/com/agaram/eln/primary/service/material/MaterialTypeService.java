package com.agaram.eln.primary.service.material;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.agaram.eln.primary.commonfunction.commonfunction;
import com.agaram.eln.primary.model.material.MaterialConfig;
import com.agaram.eln.primary.model.material.MaterialType;
import com.agaram.eln.primary.model.sheetManipulation.LSsamplemaster;
import com.agaram.eln.primary.repository.material.MaterialConfigRepository;
import com.agaram.eln.primary.repository.material.MaterialTypeRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LSsamplemasterRepository;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Service
public class MaterialTypeService {
	
	@Autowired
	MaterialTypeRepository materialTypeRepository;
	@Autowired
	MaterialConfigRepository materialConfigRepository;
	@Autowired
	LSsamplemasterRepository lssamplemasterRepository;

	public ResponseEntity<Object> getMaterialType(MaterialType objMaterialType) {
		return new ResponseEntity<>(materialTypeRepository.
				findByNmaterialtypecodeNotAndNstatusAndNsitecodeOrNmaterialtypecodeNotAndNstatusAndNdefaultstatusOrderByNmaterialtypecodeDesc(-1,1,objMaterialType.getNsitecode(),-1,1,4), HttpStatus.OK);
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
	
	public ResponseEntity<Object> createMaterialType(MaterialType objMaterialType) throws JsonParseException, JsonMappingException, IOException, ParseException {
		
		if(objMaterialType.getNmaterialtypecode() == null) {
			List<MaterialType> objlstTypes = materialTypeRepository.findByAndSmaterialtypenameIgnoreCaseAndNsitecodeOrderByNmaterialtypecode(objMaterialType.getSmaterialtypename(),objMaterialType.getNsitecode());
			
			if(objlstTypes.isEmpty()) {
				
//				List<MaterialType> objlstTypes1 = materialTypeRepository.findAll();
				
//				objMaterialType.setNmaterialtypecode(objlstTypes1.size()+1);
				objMaterialType.setNdefaultstatus(3);
				objMaterialType.setNstatus(1);
				objMaterialType.setCreatedate(commonfunction.getCurrentUtcTime());
				objMaterialType.setCreateby(objMaterialType.getCreateby());
				
				materialTypeRepository.save(objMaterialType);
				objMaterialType.setInfo("IDS_SUCCESS");
				return new ResponseEntity<>(objMaterialType, HttpStatus.OK);
			}else {
				
				objMaterialType.setInfo("IDS_FAIL_ALREADY_EXIST");
				return new ResponseEntity<>(objMaterialType, HttpStatus.OK);
			}
		}
		else {
			List<MaterialType> objlstTypes = materialTypeRepository.findByAndSmaterialtypenameIgnoreCaseAndNsitecodeAndNmaterialtypecodeNot(
					objMaterialType.getSmaterialtypename(),objMaterialType.getNsitecode(),objMaterialType.getNmaterialtypecode());
			
			if(objlstTypes.isEmpty()) {
				
				MaterialType objMType = materialTypeRepository.findByNmaterialtypecodeAndNstatus(objMaterialType.getNmaterialtypecode(), 1);				
				
				objMaterialType.setNdefaultstatus(objMType.getNdefaultstatus());
				objMaterialType.setNstatus(1);
				objMaterialType.setCreatedate(objMType.getCreatedate());
				objMaterialType.setCreateby(objMType.getCreateby());
				
				materialTypeRepository.save(objMaterialType);
				
				objMaterialType.setInfo("IDS_SUCCESS");
				return new ResponseEntity<>(objMaterialType, HttpStatus.OK);
			}else {
				
				objMaterialType.setInfo("IDS_FAIL_ALREADY_EXIST");
				return new ResponseEntity<>(objMaterialType, HttpStatus.OK);
			}
		}				
		
	}

	public ResponseEntity<Object> syncSamplestoType() throws ParseException {
		
		List<MaterialType> objTypes = new ArrayList<MaterialType>();
		List<LSsamplemaster> objList = lssamplemasterRepository.findBystatus(1);
		Date createdDate = commonfunction.getCurrentUtcTime();
		
		objList.stream().peek(f -> {
			MaterialType objType = new MaterialType();
			
			objType.setSmaterialtypename(f.getSamplename());
			objType.setNstatus(1);
			objType.setCreatedate(createdDate);
			
		}).collect(Collectors.toList());
		
		return null;
	}
}