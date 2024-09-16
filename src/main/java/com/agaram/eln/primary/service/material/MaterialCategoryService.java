package com.agaram.eln.primary.service.material;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.agaram.eln.primary.commonfunction.commonfunction;
import com.agaram.eln.primary.global.Enumeration;
import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.material.MaterialCategory;
import com.agaram.eln.primary.model.material.MaterialType;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.repository.material.MaterialCategoryRepository;
import com.agaram.eln.primary.repository.material.MaterialTypeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class MaterialCategoryService {

	@Autowired
	MaterialTypeRepository MaterialTypeRepository;

	@Autowired
	MaterialCategoryRepository MaterialCategoryRepository;

	public ResponseEntity<Object> getMaterialType(Integer nsitecode) {

		List<MaterialType> lstgetMaterialType = MaterialTypeRepository.findByNmaterialtypecodeNotAndNstatusAndNsitecodeOrNmaterialtypecodeNotAndNstatusAndNdefaultstatus(-1,1,nsitecode,-1,1,4);
		return new ResponseEntity<>(lstgetMaterialType, HttpStatus.OK);
	}

	public ResponseEntity<Object> getMaterialCategory(Integer nsitecode) {

		List<MaterialCategory> lstgetMaterialCategory = MaterialCategoryRepository
				.findByNsitecodeOrNdefaultstatusOrderByNmaterialcatcodeDesc(nsitecode,3);
		return new ResponseEntity<>(lstgetMaterialCategory, HttpStatus.OK);
	}

	public ResponseEntity<Object> createMaterialCategory(Map<String, Object> inputMap) throws Exception {
		ObjectMapper objmapper = new ObjectMapper();
		final MaterialCategory materialCategory = objmapper.convertValue(inputMap.get("materialcategory"), MaterialCategory.class);
		materialCategory.setResponse(new Response());
		
		List<MaterialCategory> lstgetMaterialCategory = MaterialCategoryRepository.findBySmaterialcatnameIgnoreCaseAndNsitecode(materialCategory.getSmaterialcatname(),materialCategory.getNsitecode());
		
		LSuserMaster objMaster = new LSuserMaster();
		objMaster.setUsercode(materialCategory.getObjsilentaudit().getLsuserMaster());
		
		if (lstgetMaterialCategory.isEmpty()) {

			materialCategory.setSmaterialtypename(materialCategory.getSmaterialtypename());
			materialCategory.setNmaterialtypecode(materialCategory.getNmaterialtypecode());
			materialCategory.setSmaterialcatname(materialCategory.getSmaterialcatname());
			materialCategory.setSdescription(materialCategory.getSdescription());
			materialCategory.setNsitecode(materialCategory.getNsitecode());
			materialCategory.setNactivestatus(0);
			materialCategory.setNuserrolecode(0);
			materialCategory.setCreateby(objMaster);
			materialCategory.setCreatedate(commonfunction.getCurrentUtcTime());
			MaterialCategoryRepository.save(materialCategory);
			
			materialCategory.getResponse().setStatus(true);
			materialCategory.getResponse().setInformation("IDS_SUCCESS");
			return new ResponseEntity<>(materialCategory, HttpStatus.OK);

		} else {
			materialCategory.getResponse().setStatus(false);
			materialCategory.getResponse().setInformation("IDS_ALREADYEXIST");
			return new ResponseEntity<>(materialCategory, HttpStatus.OK);
		}
	}

	public ResponseEntity<Object> deleteMaterialCategory(MaterialCategory materialCategory, LScfttransaction obj) {
		final MaterialCategory objMaterialCategory = MaterialCategoryRepository.findByNmaterialcatcode(materialCategory.getNmaterialcatcode());
		if (objMaterialCategory == null) {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		} else {
			objMaterialCategory.setNstatus(Enumeration.TransactionStatus.DELETED.gettransactionstatus());
			MaterialCategoryRepository.save(objMaterialCategory);
			materialCategory.setObjsilentaudit(obj);
			return getMaterialCategory(materialCategory.getNsitecode());
		}
	}

	public ResponseEntity<Object> updateMaterialCategory(MaterialCategory materialCategory) {
		final MaterialCategory objMaterialCategory = MaterialCategoryRepository
				.findByNmaterialcatcode(materialCategory.getNmaterialcatcode());
		materialCategory.setResponse(new Response());
		if (objMaterialCategory == null) {
			materialCategory.getResponse().setStatus(false);
			materialCategory.getResponse().setInformation("IDS_ALREADYDELETED");
			return new ResponseEntity<>(materialCategory, HttpStatus.OK);
		} else {
			final MaterialCategory materialCategoryObj = MaterialCategoryRepository.findByNsitecodeAndSmaterialcatnameIgnoreCase(materialCategory.getNsitecode(),materialCategory.getSmaterialcatname());
			
			if (materialCategoryObj == null || (materialCategoryObj.getNmaterialcatcode().equals(materialCategory.getNmaterialcatcode()))) {
				MaterialCategoryRepository.save(materialCategory);
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
}