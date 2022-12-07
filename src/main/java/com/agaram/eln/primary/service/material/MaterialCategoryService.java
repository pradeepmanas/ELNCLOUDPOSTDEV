package com.agaram.eln.primary.service.material;

//import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.agaram.eln.primary.global.Enumeration;
import com.agaram.eln.primary.model.material.MaterialCategory;
import com.agaram.eln.primary.model.material.MaterialType;
import com.agaram.eln.primary.repository.material.MaterialCategoryRepository;
import com.agaram.eln.primary.repository.material.MaterialTypeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class MaterialCategoryService {

	@Autowired
	MaterialTypeRepository MaterialTypeRepository;

	@Autowired
	MaterialCategoryRepository MaterialCategoryRepository;

	public ResponseEntity<Object> getMaterialType(Map<String, Object> inputMap) {

		List<MaterialType> lstgetMaterialType = MaterialTypeRepository.findByNstatusOrderByNmaterialtypecode(1);
		return new ResponseEntity<>(lstgetMaterialType, HttpStatus.OK);
	}

	public ResponseEntity<Object> getMaterialCategory(Map<String, Object> objmap) {
		List<Object> lstgetMaterialCategory = MaterialCategoryRepository.findByNstatusOrderByNmaterialcatcodeDesc(1);
		return new ResponseEntity<>(lstgetMaterialCategory, HttpStatus.OK);
	}

	@SuppressWarnings("unchecked")
	public ResponseEntity<Object> createMaterialCategory(Map<String, Object> inputMap) throws Exception {
		ObjectMapper objmapper = new ObjectMapper();
		Map<String, Object> returnmsg = new HashMap<>();
		final MaterialCategory materialCategory = objmapper.convertValue(inputMap.get("materialcategory"),
				MaterialCategory.class);
		List<MaterialCategory> lstgetMaterialCategory = MaterialCategoryRepository
				.findBySmaterialcatname(materialCategory.getSmaterialcatname());
		if (lstgetMaterialCategory.isEmpty()) {

			materialCategory.setSmaterialtypename(materialCategory.getSmaterialtypename());
			materialCategory.setNmaterialtypecode(materialCategory.getNmaterialtypecode());
			materialCategory.setSmaterialcatname(materialCategory.getSmaterialcatname());
			materialCategory.setSdescription(materialCategory.getSdescription());
			materialCategory.setNactivestatus(0);
			materialCategory.setNuserrolecode(0);
			MaterialCategoryRepository.save(materialCategory);

			return getMaterialCategory(inputMap);

		} else {
			return (ResponseEntity<Object>) returnmsg.put("alreadyexists", returnmsg);
		}
	}

	public ResponseEntity<Object> getActiveMaterialCategoryById(int nmaterialcatcode) {

//		MaterialCategory objMaterialCategory = new MaterialCategory();

//		Integer needSectionCheck = jdbcTemplate
//				.queryForObject("select count(*) from materialinventory where (jsondata->'nmaterialcatcode')::int"
//						+ "  in (select nmaterialcatcode from materialcategory where  nmaterialcatcode="
//						+ nmaterialcatcode + " and nstatus="+Enumeration.TransactionStatus.ACTIVE.gettransactionstatus()
//						+") and nstatus="+Enumeration.TransactionStatus.ACTIVE.gettransactionstatus(), Integer.class);

		final MaterialCategory objMaterialCategory = MaterialCategoryRepository
				.findByNmaterialcatcodeAndNstatus(nmaterialcatcode, 1);

//		objMaterialCategory = (MaterialCategory) jdbcQueryForObject(strQuery, MaterialCategory.class);
//		if (needSectionCheck > 0) {
//			objMaterialCategory.setNeedSectionwisedisabled(true);
//		}
//		return (MaterialCategory)  objMaterialCategory;

		if (objMaterialCategory == null) {
			return new ResponseEntity<>(objMaterialCategory, HttpStatus.EXPECTATION_FAILED);
		}

		return new ResponseEntity<>(objMaterialCategory, HttpStatus.OK);
	}

	public ResponseEntity<Object> deleteMaterialCategory(MaterialCategory materialCategory) {

		Map<String, Object> inputMap = new HashMap<>();

		final ObjectMapper objmapper = new ObjectMapper();

		final ResponseEntity<Object> materialCategoryByID = getActiveMaterialCategoryById(
				materialCategory.getNmaterialcatcode());

		if (materialCategoryByID == null) {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		} else {

			final MaterialCategory objMaterialCategory = objmapper.convertValue(materialCategoryByID.getBody(),
					MaterialCategory.class);

			objMaterialCategory.setNstatus(Enumeration.TransactionStatus.DELETED.gettransactionstatus());

			MaterialCategoryRepository.save(objMaterialCategory);

			return getMaterialCategory(inputMap);

		}
	}

	public ResponseEntity<Object> updateMaterialCategory(MaterialCategory materialCategory) {

		Map<String, Object> inputMap = new HashMap<>();

		final MaterialCategory objMaterialCategory = MaterialCategoryRepository
				.findByNmaterialcatcodeAndNstatus(materialCategory.getNmaterialcatcode(), 1);

//		final List<String> multilingualIDList = new ArrayList<>();
//		final List<Object> listAfterUpdate = new ArrayList<>();
//		final List<Object> listBeforeUpdate = new ArrayList<>();

		if (objMaterialCategory == null) {
			return new ResponseEntity<>(Enumeration.ReturnStatus.ALREADYDELETED.getreturnstatus(),
					HttpStatus.EXPECTATION_FAILED);
		} else {

			final MaterialCategory materialCategoryObj = MaterialCategoryRepository
					.findBySmaterialcatnameAndNmaterialcatcodeAndNstatus(materialCategory.getSmaterialcatname(),
							materialCategory.getNmaterialcatcode(), 1);

			if (materialCategoryObj != null) {

				materialCategoryObj.setSmaterialcatname(materialCategory.getSmaterialcatname());
				materialCategoryObj.setSdescription(materialCategory.getSdescription());
				materialCategoryObj.setNdefaultstatus(materialCategory.getNdefaultstatus());
				materialCategoryObj.setNmaterialtypecode(materialCategory.getNmaterialtypecode());
				materialCategoryObj.setSmaterialtypename(materialCategory.getSmaterialtypename());

				MaterialCategoryRepository.save(materialCategoryObj);

				return getMaterialCategory(inputMap);
			} else {
				return new ResponseEntity<>(Enumeration.ReturnStatus.ALREADYEXISTS.getreturnstatus(),
						HttpStatus.CONFLICT);
			}
		}
	}
}
