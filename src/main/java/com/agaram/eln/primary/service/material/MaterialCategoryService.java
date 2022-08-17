package com.agaram.eln.primary.service.material;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
		Map<String, Object> objmap = new LinkedHashMap<String, Object>();

		List<MaterialType> lstgetMaterialType = MaterialTypeRepository.findAll();
		objmap.put("getMaterialType", lstgetMaterialType);
		return new ResponseEntity<>(objmap, HttpStatus.OK);
	}

	public ResponseEntity<Object> getMaterialCategory(Map<String, Object> inputMap) {

		Map<String, Object> objmap = new LinkedHashMap<String, Object>();

		List<MaterialCategory> lstgetMaterialCategory = MaterialCategoryRepository.findAll();
		objmap.put("getMaterialCategory", lstgetMaterialCategory);
		return new ResponseEntity<>(objmap, HttpStatus.OK);
	}

	public ResponseEntity<Object> createMaterialCategory(Map<String, Object> inputMap) throws Exception {
		ObjectMapper objmapper = new ObjectMapper();
		Map<String, Object> returnmsg = new HashMap<>();
		final MaterialCategory materialCategory = objmapper.convertValue(inputMap.get("materialcategory"),
				MaterialCategory.class);
		List<MaterialCategory> lstgetMaterialCategory = MaterialCategoryRepository
				.findBySmaterialcatname(materialCategory.getSmaterialcatname());
		if (lstgetMaterialCategory.isEmpty()) {

			materialCategory.setNmaterialtypecode(materialCategory.getNmaterialtypecode());
			materialCategory.setSmaterialcatname(materialCategory.getSmaterialcatname());
			materialCategory.setSdescription(materialCategory.getSdescription());
			materialCategory.setNactivestatus(0);
			materialCategory.setNuserrolecode(0);
			MaterialCategoryRepository.save(materialCategory);

			List<MaterialCategory> lstgetallMaterialCategory = MaterialCategoryRepository.findAll();
			return new ResponseEntity<>(lstgetallMaterialCategory, HttpStatus.OK);

		} else {
//			return "ALREADYEXISTS";
			return (ResponseEntity<Object>) returnmsg.put("alreadyexists", returnmsg);
		}
//		final List<MaterialCategory> materialCategoryListByName = getMaterialCategoryListByName(
//				((MaterialCategory) inputMap).getSmaterialcatname(), ((Lsmaterialcategory) inputMap).getNsitecode(),
//				((Lsmaterialcategory) inputMap).getNmaterialtypecode());
//		if (materialCategoryListByName.isEmpty()) {
//			if (materialCategory.getNdefaultstatus() == Enumeration.TransactionStatus.YES.gettransactionstatus()) {
//				final MaterialCategory defaultMaterialCategory = getMaterialCategoryByDefaultStatus(
//						materialCategory.getNsitecode(), materialCategory.getNmaterialtypecode());
//				if (defaultMaterialCategory != null) {
//					final MaterialCategory materialCategoryBeforeSave = new MaterialCategory(defaultMaterialCategory);
//					final List<Object> defaultListBeforeSave = new ArrayList<>();
//					defaultListBeforeSave.add(materialCategoryBeforeSave);
//					defaultMaterialCategory
//							.setNdefaultstatus((short) Enumeration.TransactionStatus.NO.gettransactionstatus());
//					final String updateQueryString = " update materialcategory set ndefaultstatus="
//							+ Enumeration.TransactionStatus.NO.gettransactionstatus() + " where nmaterialcatcode ="
//							+ defaultMaterialCategory.getNmaterialcatcode();
//					getJdbcTemplate().execute(updateQueryString);
//					final List<Object> defaultListAfterSave = new ArrayList<>();
//					defaultListAfterSave.add(defaultMaterialCategory);
//					multilingualIDList.add("IDS_EDITMATERIALCATEGORY");
//					fnInsertAuditAction(defaultListAfterSave, 2, defaultListBeforeSave, multilingualIDList, userInfo);
//				}
//			}
//			String sequencequery = "select nsequenceno from SeqNoMaterialManagement where stablename ='materialcategory'";
//			int nsequenceno = getJdbcTemplate().queryForObject(sequencequery, Integer.class);
//			nsequenceno++;
//
//			String insertquery = "Insert into materialcategory (nmaterialcatcode,nmaterialtypecode,nuserrolecode,nbarcode,ncategorybasedflow,nactivestatus,smaterialcatname,sdescription,ndefaultstatus,nsitecode,needSectionwise,nstatus)"
//					+ "values(" + nsequenceno + "," + materialCategory.getNmaterialtypecode() + ","
//					+ materialCategory.getNuserrolecode() + "," + materialCategory.getNbarcode() + ","
//					+ materialCategory.getNcategorybasedflow() + "," + materialCategory.getNactivestatus() + ",N'"
//					+ ReplaceQuote(materialCategory.getSmaterialcatname()) + "',N'"
//					+ ReplaceQuote(materialCategory.getSdescription()) + "'," + materialCategory.getNdefaultstatus()
//					+ "," + userInfo.getNmastersitecode() + "," + materialCategory.getNeedSectionwise() + ","
//					+ Enumeration.TransactionStatus.ACTIVE.gettransactionstatus() + ")";
//			getJdbcTemplate().execute(insertquery);
//
//			String updatequery = "update SeqNoMaterialManagement set nsequenceno =" + nsequenceno
//					+ " where stablename ='materialcategory'";
//			getJdbcTemplate().execute(updatequery);
//			
//			
//			String sequencenoquery1 ="select nsequenceno from SeqNoConfigurationMaster where stablename ='treetemplatemaster'";
//			int nsequenceno1 =getJdbcTemplate().queryForObject(sequencenoquery1, Integer.class);
//			nsequenceno1++;
//			String insertquery1= "Insert into treetemplatemaster (ntemplatecode,ncategorycode,sdescription,nrootcode,nformcode,nstatus)"
//							  + "values("+nsequenceno1+","+nsequenceno+",N'root',"+Enumeration.TransactionStatus.NA.gettransactionstatus()+","
//							  + " "+userInfo.getNformcode()+","+Enumeration.TransactionStatus.ACTIVE.gettransactionstatus()+")";
//			getJdbcTemplate().execute(insertquery1);
//
//			String updatequery1 ="update SeqNoConfigurationMaster set nsequenceno ="+nsequenceno1+"where stablename ='treetemplatemaster'";
//			getJdbcTemplate().execute(updatequery1);
//			
//
//			savedMaterialCategoryList.add(materialCategory);
//			multilingualIDList.add("IDS_ADDMATERIALCATEGORY");
//			fnInsertAuditAction(savedMaterialCategoryList, 1, null, multilingualIDList, userInfo);
//			return getMaterialCategory(userInfo);
//		} else {
//			return new ResponseEntity<>(
//					commonFunctionObject.getMultilingu	alMessage(
//							Enumeration.ReturnStatus.ALREADYEXISTS.getreturnstatus(), userInfo.getSlanguagefilename()),
//					HttpStatus.CONFLICT);
//		
//		return null;
	}

//	public ResponseEntity<Object> createMaterialCategory(Map<String, Object> inputMap) {
//		int materialcatcode =inputMap.get(inputMap)
//		String materialcatname = inputMap.getSmaterialcatname();
//		List<MaterialCategory> lstgetMaterialCategory =MaterialCategoryRepository.findByNmaterialcatcodeAndSmaterialcatname(materialcatcode,materialcatname);
//		if (lstgetMaterialCategory.isEmpty()) {
//			return null;	
//		}
//		else {
//		
//		}
//		return null;
//	}

}
