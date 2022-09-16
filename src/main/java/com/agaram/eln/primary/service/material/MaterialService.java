package com.agaram.eln.primary.service.material;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.agaram.eln.primary.model.material.MappedTemplateFieldPropsMaterial;
import com.agaram.eln.primary.model.material.Material;
import com.agaram.eln.primary.model.material.MaterialCategory;
import com.agaram.eln.primary.model.material.MaterialConfig;
import com.agaram.eln.primary.model.material.MaterialType;
import com.agaram.eln.primary.repository.material.MappedTemplateFieldPropsMaterialRepository;
import com.agaram.eln.primary.repository.material.MaterialCategoryRepository;
import com.agaram.eln.primary.repository.material.MaterialConfigRepository;
import com.agaram.eln.primary.repository.material.MaterialRepository;
import com.agaram.eln.primary.repository.material.MaterialTypeRepository;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class MaterialService {

	@Autowired
	MaterialTypeRepository materialTypeRepository;
	@Autowired
	MaterialCategoryRepository materialCategoryRepository;
	@Autowired
	MaterialRepository materialRepository;
	@Autowired
	MaterialConfigRepository materialConfigRepository;
	@Autowired
	MappedTemplateFieldPropsMaterialRepository mappedTemplateFieldPropsMaterialRepository;

	public ResponseEntity<Object> getMaterialcombo(Integer nmaterialtypecode) {

		Map<String, Object> objmap = new LinkedHashMap<String, Object>();

		List<MaterialCategory> lstMaterialCategory = materialCategoryRepository
				.findByNmaterialtypecode(nmaterialtypecode);

		objmap.put("MaterialCategoryMain", lstMaterialCategory);
		return new ResponseEntity<>(objmap, HttpStatus.OK);
	}

	@SuppressWarnings("unchecked")
	public ResponseEntity<Object> getMaterialType() throws JsonParseException, JsonMappingException, IOException {
		Map<String, Object> objmap = new LinkedHashMap<String, Object>();

		List<MaterialType> lstMaterialType = materialTypeRepository.findAll();

		objmap.put("MaterialType", lstMaterialType);
		objmap.put("SelectedMaterialType", lstMaterialType);

		List<MaterialCategory> lstMaterialCategory = materialCategoryRepository
				.findByNmaterialtypecode(lstMaterialType.get(0).getNmaterialtypecode());

		if (!lstMaterialCategory.isEmpty()) {
			objmap.put("MaterialCategoryMain", lstMaterialCategory);
			objmap.put("nmaterialcatcode", lstMaterialCategory.get(0).getNmaterialcatcode());
		}
		objmap.put("nmaterialtypecode", lstMaterialType.get(0).getNmaterialtypecode());
		objmap.putAll((Map<String, Object>) getMaterialByTypeCode(objmap).getBody());
//		objmap.putAll((Map<String, Object>) getMaterialAdd((Integer)objmap.get("nmaterialtypecode")).getBody());

		return new ResponseEntity<>(objmap, HttpStatus.OK);
	}

	@SuppressWarnings("unchecked")
	public ResponseEntity<Object> getMaterialByTypeCode(Map<String, Object> inputMap) throws JsonParseException, JsonMappingException, IOException {

		Map<String, Object> objmap = new LinkedHashMap<String, Object>();
		
		
		final List<Material> lstMaterial = materialRepository.
				findByNmaterialcatcodeAndNmaterialtypecodeAndNstatus(
						(Integer)inputMap.get("nmaterialcatcode"),(Integer)inputMap.get("nmaterialtypecode"),1);
		
		if (lstMaterial != null) {
			
			if (inputMap.containsKey("nmaterialcatcode")) {

				objmap.put("Material", lstMaterial);
				
				List<Map<String, Object>> lstMatObject = new ArrayList<Map<String, Object>>();

				objmap.put("tabScreen", "IDS_MATERIALSECTION");

				if (!lstMaterial.isEmpty()) {
					
					Map<String,Object> selectedMaterial = new ObjectMapper().readValue(lstMaterial.get(lstMaterial.size() - 1).getJsonuidata(), Map.class);
					
					objmap.put("SelectedMaterial", selectedMaterial);
					objmap.put("nmaterialcode", (int) lstMaterial.get(lstMaterial.size() - 1).getNmaterialcode());
					
					lstMaterial.stream().peek(f -> {
										
						try {
							Map<String,Object> result = new ObjectMapper().readValue(f.getJsonuidata(), Map.class);
						
							lstMatObject.add(result);
						} catch (IOException e) {
							
							e.printStackTrace();
						}
						
					}).collect(Collectors.toList());
					
					objmap.put("Material", lstMatObject);
				} else {
					objmap.put("SelectedMaterial", lstMaterial);
				}

				List<MaterialType> lstMaterialType = materialTypeRepository
						.findByNmaterialtypecode((Integer) inputMap.get("nmaterialtypecode"));
				objmap.put("SelectedMaterialType", lstMaterialType);

				List<MaterialCategory> objLstMaterialCategory = new ArrayList<>();

				if (inputMap.containsKey("nmaterialcatcode")) {
					objLstMaterialCategory = materialCategoryRepository
							.findByNmaterialcatcode((Integer) inputMap.get("nmaterialcatcode"));
				} else {
					objLstMaterialCategory = materialCategoryRepository
							.findByNmaterialtypecode((Integer) inputMap.get("nmaterialtypecode"));
				}
				List<MaterialCategory> lstMaterialCategory = objLstMaterialCategory;

				if (!lstMaterialCategory.isEmpty()) {
					if (inputMap.containsKey("nmaterialcatcode")) {
						objmap.put("SelectedMaterialCategory", lstMaterialCategory.get(0));
					} else {
						objmap.put("SelectedMaterialCategory", lstMaterialCategory.get(0));
					}
				}
				
//				List<MappedTemplateFieldPropsMaterial> lstMappedTemplate = 
//						mappedTemplateFieldPropsMaterialRepository.findByNmaterialconfigcode(1);
//				
//				Map<String,Object> designObject = new HashMap<String, Object>();
//				
//				Map<String,Object> designChildObject = new HashMap<String, Object>();
//				
//				designChildObject.put("type", "jsonb");
//				designChildObject.put("null", true);
//				designChildObject.put("value", lstMappedTemplate.get(0).getJsondata());
//				
//				designObject.put("jsondata", designChildObject);
				
				objmap.put("DesignMappedFeilds",getTemplateDesignForMaterial(1,23));
 
			} else {

				List<MaterialType> lstMaterialType = materialTypeRepository
						.findByNmaterialtypecode((Integer) inputMap.get("nmaterialtypecode"));
				List<MaterialType> lstActiontype = new ArrayList<MaterialType>();

				objmap.put("SelectedMaterialType", lstMaterialType);
				objmap.put("Material", lstActiontype);
				objmap.put("SelectedMaterial", lstActiontype);
				objmap.put("SelectedMaterialCategory", lstActiontype);
			}
		}

		List<MaterialConfig> lstMaterialConfig = materialConfigRepository.findByNformcode(40);
		objmap.put("selectedTemplate", lstMaterialConfig);

		return new ResponseEntity<>(objmap, HttpStatus.OK);
	}

	@SuppressWarnings("unchecked")
	public ResponseEntity<Object> createMaterial(Map<String, Object> inputMap) throws JsonParseException, JsonMappingException, IOException {
		Map<String, Object> objmap = new LinkedHashMap<String, Object>();
		JSONObject actionType = new JSONObject();
		JSONArray jsonUidataarray = new JSONArray();
		new ObjectMapper();
		
		JSONObject jsonObject = new JSONObject(inputMap.get("materialJson").toString());
		JSONObject jsonuidata = new JSONObject(inputMap.get("jsonuidata").toString());
		
		String strJsonObj = inputMap.get("materialJson").toString();
		String strJsonUiData = inputMap.get("jsonuidata").toString();
		
		inputMap.get("DateList");

		final Material ObjMatNamecheck = materialRepository
				.findBySmaterialnameAndNstatus((String) jsonObject.get("Material Name"), 1);

		if (ObjMatNamecheck == null) {
			if (jsonObject.has("Prefix")) {

			}
		} else {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
		
		Material objMaterial = new Material();
		
		objMaterial.setNmaterialcatcode((Integer) jsonObject.get("nmaterialcatcode"));
		objMaterial.setSmaterialname(jsonObject.getString("Material Name"));
		objMaterial.setNmaterialtypecode((Integer)jsonObject.get("nmaterialtypecode"));
		objMaterial.setNstatus(1);
		objMaterial.setNtransactionstatus((Integer) inputMap.get("ntransactionstatus"));
		objMaterial.setJsondata(strJsonObj);
		objMaterial.setJsonuidata(strJsonUiData);

		materialRepository.save(objMaterial);
		
//		if ((int) inputMap.get("needsectionwise") == Enumeration.TransactionStatus.YES.gettransactionstatus()) {
//			jsonObject = new JSONObject(inputMap.get("materialSectionJson").toString());
//			jsonObject.put("nmaterialcode", objMaterial.getNmaterialcode());
//			inputMap.put("nmaterialcode", objMaterial.getNmaterialcode());
//			inputMap.put("materialSectionJson", jsonObject);
////			objMaterialSectionDAO.createMaterialSection(inputMap);
//		}

		inputMap.put("nmaterialconfigcode", jsonuidata.get("nmaterialconfigcode"));
		objmap.putAll((Map<String, Object>) getMaterialByTypeCode(inputMap).getBody());
		// Audit For Add
		objmap.put("nregtypecode", -1);

		objmap.put("nregsubtypecode", -1);
		objmap.put("ndesigntemplatemappingcode", jsonuidata.get("nmaterialconfigcode"));
		actionType.put("Material", "IDS_ADDMATERIAL");
		jsonUidataarray.put(jsonuidata);
//		lstaudit.add((Map<String, Object>) objmap.get("SelectedMaterial"));
//		jsonAuditObject.put("Material", lstaudit);
//		fnJsonArrayAudit(jsonAuditObject, null, actionType, objmap, false);
		return new ResponseEntity<>(objmap, HttpStatus.OK);
	}

	@SuppressWarnings("unchecked")
	public ResponseEntity<Object> getMaterialDetails(Map<String, Object> inputMap) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objmapper = new ObjectMapper();
		Map<String, Object> objmap = new LinkedHashMap<String, Object>();

		final Material objMatDetails = materialRepository.findByNstatusAndNmaterialcode(1,(Integer) inputMap.get("nmaterialcode"));
		
		Map<String, Object> objMaterial = objmapper.readValue(objMatDetails.getJsonuidata(),
				new TypeReference<Map<String, Object>>() { });
		
		List<Map<String, Object>> lstMaterial = new LinkedList<Map<String,Object>>();
		
		lstMaterial.add(0, objMaterial);
		
		objmap.put("SelectedMaterial", lstMaterial.get(lstMaterial.size() - 1));
		
//		objmap.putAll((Map<String, Object>) objMaterialSectionDAO
//				.getMaterialSectionByMaterialCode((int) inputMap.get("nmaterialcode"), inputMap).getBody());
		objmap.put("nmaterialcode", inputMap.get("nmaterialcode"));
//		objmap.putAll((Map<String, Object>) getMaterialSafetyInstructions(objmap).getBody());
//		objmap.putAll((Map<String, Object>) getMaterialFile((int) inputMap.get("nmaterialcode"), userInfo));
		objmap.putAll((Map<String, Object>) getMaterialcombo((int) inputMap.get("nmaterialtypecode")).getBody());
		objmap.put("DesignMappedFeilds", getTemplateDesignForMaterial(1,23));
		return new ResponseEntity<>(objmap, HttpStatus.OK);
	}

	public Map<String, Object> getTemplateDesignForMaterial(int nmaterialconfigcode, int nformcode){

		List<MappedTemplateFieldPropsMaterial> lstMappedTemplate = 
				mappedTemplateFieldPropsMaterialRepository.findByNmaterialconfigcode(1);
		
		Map<String,Object> designObject = new HashMap<String, Object>();
		
		Map<String,Object> designChildObject = new HashMap<String, Object>();
		
		designChildObject.put("type", "jsonb");
		designChildObject.put("null", true);
		designChildObject.put("value", lstMappedTemplate.get(0).getJsondata());
		
		designObject.put("jsondata", designChildObject);
		
		return designObject;
	} 
	
//	public ResponseEntity<Object> getMaterialEdit(Map<String, Object> inputMap) {
//		Map<String, Object> objmap = new LinkedHashMap<String, Object>();
//		ObjectMapper objmapper = new ObjectMapper();
//		List<Map<String, Object>> lstMaterial = new ArrayList<Map<String, Object>>();
//		List<Integer> lstcount = new ArrayList<Integer>();
////		List<MappedTemplateFieldPropsMaterial> lstsearchFields = new LinkedList<>();
//		List<String> lstsearchField = new LinkedList()<String>();
//		List<String> submittedDateFeilds = new LinkedList<String>();
//
//		String type = (int) inputMap.get("nmaterialtypecode") == Enumeration.TransactionStatus.STANDARDTYPE
//				.gettransactionstatus() ? "MaterialStandard"
//						: (int) inputMap.get("nmaterialtypecode") == Enumeration.TransactionStatus.VOLUMETRICTYPE
//								.gettransactionstatus() ? "MaterialVolumetric" : "MaterialMaterialInventory";
//		
//		int typeCode = (int) inputMap.get("nmaterialtypecode") == Enumeration.TransactionStatus.STANDARDTYPE
//				.gettransactionstatus() ? 1
//						: (int) inputMap.get("nmaterialtypecode") == Enumeration.TransactionStatus.VOLUMETRICTYPE
//								.gettransactionstatus() ? 2 : 3;
//		
//		lstcount = materialRepository.findByNmaterialcodeAndNstatus((Integer)inputMap.get("nmaterialcode"),-1);
//		
//		if (lstcount.size() == 0) {
//			
//			String selectionKeyName1 = "select (jsondata->'" + userInfo.getNformcode() + "'->'" + type
//					+ "datefields')::jsonb as jsondata from mappedtemplatefieldpropsmaterial  where   nstatus="
//					+ Enumeration.TransactionStatus.ACTIVE.gettransactionstatus() + " and nmaterialconfigcode="
//					+ typeCode;
//			
//			lstsearchFields = jdbcTemplate.query(selectionKeyName1, new MappedTemplateFieldPropsMaterial());
//
//			JSONArray objarray = new JSONArray(lstsearchFields.get(0).getJsondata());
//			for (int i = 0; i < objarray.length(); i++) {
//				JSONObject jsonobject = new JSONObject(objarray.get(i).toString());
//				if (jsonobject.has("2")) {
//					lstsearchField.add((String) jsonobject.get("2"));
//				}
//			}
//			objmap.put("DateFeildsMaterial", lstsearchField);
//
//			String query1 = " select json_agg(a.jsondata) from(select jsondata||json_build_object('needsectionwise',mc.needsectionwise)::jsonb as jsondata  from  material m,materialcategory"
//					+ " mc where m.nstatus=" + Enumeration.TransactionStatus.ACTIVE.gettransactionstatus()
//					+ " and m.nmaterialcode in (" + inputMap.get("nmaterialcode")
//					+ ") and (jsondata->'nmaterialcatcode')::int=mc.nmaterialcatcode)a;";
//			// List<Material> objMaterial = jdbcTemplate.query(query1, new Material());
//			String objMaterial = jdbcTemplate.queryForObject(query1, String.class);
//			lstMaterial = objmapper.convertValue(
//					getSiteLocalTimeFromUTCForRegTmplate(objMaterial, userInfo, true,
//							(int) inputMap.get("nmaterialtypecode") == Enumeration.TransactionStatus.STANDARDTYPE
//									.gettransactionstatus()
//											? 1
//											: (int) inputMap.get(
//													"nmaterialtypecode") == Enumeration.TransactionStatus.VOLUMETRICTYPE
//															.gettransactionstatus() ? 2 : 3,
//							type),
//					new TypeReference<List<Map<String, Object>>>() {
//					});
//			if (!lstMaterial.isEmpty()) {
//				for (int i = 0; i < lstsearchField.size(); i++) {
//					if (lstMaterial.get(i).get(lstsearchField.get(i)) != null) {
//						submittedDateFeilds.add(lstsearchField.get(i));
//					}
//				}
//			}
//			if (!submittedDateFeilds.isEmpty()) {
//				objmap.put("MaterialDateFeild", submittedDateFeilds);
//			}
//
//			objmap.put("EditedMaterial", lstMaterial);
//			return new ResponseEntity<>(objmap, HttpStatus.OK);
//		} else {
//			return new ResponseEntity<>(
//					commonFunction.getMultilingualMessage("IDS_ALREADYDELETED", userInfo.getSlanguagefilename()),
//					HttpStatus.CONFLICT);
//		}
//	}

}
