package com.agaram.eln.primary.service.material;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.agaram.eln.primary.commonfunction.commonfunction;
import com.agaram.eln.primary.global.Enumeration;
import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.instrumentDetails.LsOrderattachments;
import com.agaram.eln.primary.model.material.MappedTemplateFieldPropsMaterial;
import com.agaram.eln.primary.model.material.Material;
import com.agaram.eln.primary.model.material.MaterialCategory;
import com.agaram.eln.primary.model.material.MaterialConfig;
import com.agaram.eln.primary.model.material.MaterialType;
import com.agaram.eln.primary.repository.instrumentDetails.LsOrderattachmentsRepository;
import com.agaram.eln.primary.repository.material.MappedTemplateFieldPropsMaterialRepository;
import com.agaram.eln.primary.repository.material.MaterialCategoryRepository;
import com.agaram.eln.primary.repository.material.MaterialConfigRepository;
import com.agaram.eln.primary.repository.material.MaterialInventoryRepository;
import com.agaram.eln.primary.repository.material.MaterialRepository;
import com.agaram.eln.primary.repository.material.MaterialTypeRepository;
import com.agaram.eln.primary.repository.usermanagement.LSuserMasterRepository;
import com.agaram.eln.primary.service.cloudFileManip.CloudFileManipulationservice;
import com.agaram.eln.primary.service.fileManipulation.FileManipulationservice;
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
	private LSuserMasterRepository lsuserMasterRepository;
	@Autowired
	private LsOrderattachmentsRepository lsOrderattachmentsRepository;
	@Autowired
	private CloudFileManipulationservice cloudFileManipulationservice;	
	@Autowired
	private FileManipulationservice fileManipulationservice;
	@Autowired
	MaterialConfigRepository materialConfigRepository;
	@Autowired
	MappedTemplateFieldPropsMaterialRepository mappedTemplateFieldPropsMaterialRepository;
	@Autowired
	MaterialInventoryRepository materialInventoryRepository;

	public ResponseEntity<Object> getMaterialcombo(Integer nmaterialtypecode, Integer nsitecode) {

		Map<String, Object> objmap = new LinkedHashMap<String, Object>();

		List<MaterialCategory> lstMaterialCategory = materialCategoryRepository
				.findByNmaterialtypecodeAndNsitecodeAndNstatusOrderByNmaterialcatcode(nmaterialtypecode, nsitecode, 1);

		objmap.put("MaterialCategoryMain", lstMaterialCategory);
		return new ResponseEntity<>(objmap, HttpStatus.OK);
	}

	@SuppressWarnings("unchecked")
	public ResponseEntity<Object> getMaterialType(Integer nsiteInteger) throws JsonParseException, JsonMappingException, IOException {
		
		Map<String, Object> objmap = new LinkedHashMap<String, Object>();

		List<MaterialType> lstMaterialType = materialTypeRepository.findByNmaterialtypecodeNotOrderByNmaterialtypecode(-1);
		
		objmap.put("MaterialType", lstMaterialType);
		objmap.put("SelectedMaterialType", lstMaterialType);
		
		if(!lstMaterialType.isEmpty()) {
			List<MaterialCategory> lstMaterialCategory = materialCategoryRepository
					.findByNmaterialtypecodeAndNsitecodeAndNstatus(lstMaterialType.get(0).getNmaterialtypecode(), nsiteInteger,1);

			if (!lstMaterialCategory.isEmpty()) {
				objmap.put("MaterialCategoryMain", lstMaterialCategory);
				objmap.put("nmaterialcatcode", lstMaterialCategory.get(0).getNmaterialcatcode());
			}
			objmap.put("nmaterialtypecode", lstMaterialType.get(0).getNmaterialtypecode());
			objmap.put("nsitecode", nsiteInteger);
			objmap.putAll((Map<String, Object>) getMaterialByTypeCode(objmap).getBody());
//			objmap.putAll((Map<String, Object>) getMaterialAdd((Integer)objmap.get("nmaterialtypecode")).getBody());
		}

		return new ResponseEntity<>(objmap, HttpStatus.OK);
	}

	@SuppressWarnings("unchecked")
	public ResponseEntity<Object> getMaterialByTypeCode(Map<String, Object> inputMap)
			throws JsonParseException, JsonMappingException, IOException {
		final ObjectMapper objmapper = new ObjectMapper();
		Map<String, Object> objmap = new LinkedHashMap<String, Object>();

		Integer nsiteInteger = (Integer) inputMap.get("nsitecode");

		final LScfttransaction cft = objmapper.convertValue(inputMap.get("objsilentaudit"), LScfttransaction.class);
		final List<Material> lstMaterial = materialRepository.findByNmaterialcatcodeAndNmaterialtypecodeAndNsitecode(
				(Integer) inputMap.get("nmaterialcatcode"), (Integer) inputMap.get("nmaterialtypecode"), nsiteInteger);

		if (lstMaterial != null) {

			if (inputMap.containsKey("nmaterialcatcode")) {

				objmap.put("Material", lstMaterial);

				List<Map<String, Object>> lstMatObject = new ArrayList<Map<String, Object>>();

				objmap.put("tabScreen", "IDS_MATERIALSECTION");

				if (!lstMaterial.isEmpty()) {

					Map<String, Object> selectedMaterial = new ObjectMapper()
							.readValue(lstMaterial.get(lstMaterial.size() - 1).getJsonuidata(), Map.class);

					selectedMaterial.put("nmaterialcode",
							(int) lstMaterial.get(lstMaterial.size() - 1).getNmaterialcode());
					selectedMaterial.put("nstatus", (int) lstMaterial.get(lstMaterial.size() - 1).getNstatus());

					objmap.put("SelectedMaterial", selectedMaterial);
					objmap.put("nmaterialcode", (int) lstMaterial.get(lstMaterial.size() - 1).getNmaterialcode());

					lstMaterial.stream().peek(f -> {

						try {
							Map<String, Object> result = new ObjectMapper().readValue(f.getJsonuidata(), Map.class);

							result.put("nmaterialcode", f.getNmaterialcode());
							result.put("nstatus", f.getNstatus());
							result.put("status", f.getNstatus() == -1 ? "Retired" : "Active");
							
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
					MaterialCategory objMaterialCategory = materialCategoryRepository
							.findByNmaterialcatcodeAndNstatus((Integer) inputMap.get("nmaterialcatcode"), 1);

					objLstMaterialCategory.add(objMaterialCategory);
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

				objmap.put("DesignMappedFeilds", getTemplateDesignForMaterial(
						(int) inputMap.get("nmaterialtypecode") == Enumeration.TransactionStatus.STANDARDTYPE
								.gettransactionstatus()
										? 1
										: (int) inputMap.get(
												"nmaterialtypecode") == Enumeration.TransactionStatus.VOLUMETRICTYPE
														.gettransactionstatus() ? 2 : 3,
						40));

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

		List<MaterialConfig> lstMaterialConfig = materialConfigRepository
				.findByNmaterialtypecodeAndNformcode((Integer) inputMap.get("nmaterialtypecode"), 40);
		objmap.put("selectedTemplate", lstMaterialConfig);
		if(!lstMaterialConfig.isEmpty()) {
			objmap.put("selectedGridProps", lstMaterialConfig.get(0));
		}
		if (cft != null) {
			objmap.put("objsilentaudit", cft);
		}
		return new ResponseEntity<>(objmap, HttpStatus.OK);
	}

	@SuppressWarnings("unchecked")
	public ResponseEntity<Object> getMaterialByTypeCodeByDate(Map<String, Object> inputMap)
			throws JsonParseException, JsonMappingException, IOException, ParseException {
		final ObjectMapper objmapper = new ObjectMapper();
		Map<String, Object> objmap = new LinkedHashMap<String, Object>();

		Integer nsiteInteger = (Integer) inputMap.get("nsitecode");

		DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

		Date fromDate = simpleDateFormat.parse((String) inputMap.get("fromDate"));
		Date toDate = simpleDateFormat.parse((String) inputMap.get("toDate"));

		final Material cft = objmapper.convertValue(inputMap.get("objsilentaudit"), Material.class);

		final List<Material> lstMaterial = materialRepository
				.findByNmaterialcatcodeAndNmaterialtypecodeAndNsitecodeAndCreateddateBetween(
						(Integer) inputMap.get("nmaterialcatcode"), (Integer) inputMap.get("nmaterialtypecode"),
						nsiteInteger, fromDate, toDate);

		if (lstMaterial != null) {

			if (inputMap.containsKey("nmaterialcatcode")) {

				objmap.put("Material", lstMaterial);

				List<Map<String, Object>> lstMatObject = new ArrayList<Map<String, Object>>();

				objmap.put("tabScreen", "IDS_MATERIALSECTION");

				if (!lstMaterial.isEmpty()) {

					Map<String, Object> selectedMaterial = new ObjectMapper()
							.readValue(lstMaterial.get(lstMaterial.size() - 1).getJsonuidata(), Map.class);

					selectedMaterial.put("nmaterialcode",
							(int) lstMaterial.get(lstMaterial.size() - 1).getNmaterialcode());
					selectedMaterial.put("nstatus", (int) lstMaterial.get(lstMaterial.size() - 1).getNstatus());
					objmap.put("SelectedMaterial", selectedMaterial);
					objmap.put("nmaterialcode", (int) lstMaterial.get(lstMaterial.size() - 1).getNmaterialcode());

					lstMaterial.stream().peek(f -> {

						try {
							Map<String, Object> result = new ObjectMapper().readValue(f.getJsonuidata(), Map.class);

							result.put("nmaterialcode", f.getNmaterialcode());
							result.put("nstatus", f.getNstatus());
							result.put("status", f.getNstatus() == -1 ? "Retired" : "Active");
							
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
					MaterialCategory objMaterialCategory = materialCategoryRepository
							.findByNmaterialcatcodeAndNstatus((Integer) inputMap.get("nmaterialcatcode"), 1);

					objLstMaterialCategory.add(objMaterialCategory);
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

				objmap.put("DesignMappedFeilds", getTemplateDesignForMaterial(
						(int) inputMap.get("nmaterialtypecode") == Enumeration.TransactionStatus.STANDARDTYPE
								.gettransactionstatus()
										? 1
										: (int) inputMap.get(
												"nmaterialtypecode") == Enumeration.TransactionStatus.VOLUMETRICTYPE
														.gettransactionstatus() ? 2 : 3,
						40));

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

		List<MaterialConfig> lstMaterialConfig = materialConfigRepository
				.findByNmaterialtypecodeAndNformcode((Integer) inputMap.get("nmaterialtypecode"), 40);
		objmap.put("selectedTemplate", lstMaterialConfig);
		objmap.put("selectedGridProps", lstMaterialConfig.get(0));
		if (cft != null) {
			objmap.put("objsilentaudit", cft.getObjsilentaudit());
		}
		return new ResponseEntity<>(objmap, HttpStatus.OK);
	}

	@SuppressWarnings({ "unchecked", "unused" })
	public ResponseEntity<Object> createMaterial(Map<String, Object> inputMap)
			throws JsonParseException, JsonMappingException, IOException {

		final ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> objmap = new LinkedHashMap<String, Object>();
		JSONObject actionType = new JSONObject();
		JSONArray jsonUidataarray = new JSONArray();

		Material objMaterial = new Material();

		new ObjectMapper();

		final LScfttransaction cft = mapper.convertValue(inputMap.get("objsilentaudit"), LScfttransaction.class);

		JSONObject jsonObject = new JSONObject(inputMap.get("materialJson").toString());
		JSONObject jsonuidata = new JSONObject(inputMap.get("jsonuidata").toString());

		String strJsonObj = inputMap.get("materialJson").toString();
		String strJsonUiData = inputMap.get("jsonuidata").toString();

		Integer nsiteInteger = (Integer) inputMap.get("nsitecode");

		inputMap.get("DateList");
		boolean nflag = false;

		final Material ObjMatNamecheck = materialRepository.findBySmaterialnameAndNmaterialtypecodeAndNsitecode((String) jsonObject.get("Material Name"), (Integer) jsonObject.get("nmaterialtypecode"), nsiteInteger);

		if (ObjMatNamecheck == null) {

			if (jsonObject.has("Prefix")) {
				objMaterial.setSprefix((String) jsonObject.get("Prefix"));
				nflag = true;

				final Material objMaterialClass = materialRepository.findByNstatusAndSprefixAndNsitecode(1,
						(String) jsonObject.get("Prefix"),nsiteInteger);

				if (objMaterialClass != null) {

					objMaterial.setInfo("Duplicate Entry:  Prefix - " + (String) jsonObject.get("Prefix"));
					objMaterial.setObjsilentaudit(cft);
//					return new ResponseEntity<>("IDS_PREFIXALREADYEXISTS", HttpStatus.CONFLICT);
					return new ResponseEntity<>(objMaterial, HttpStatus.CONFLICT);
				}
			}
		} else {
			objMaterial.setInfo("Duplicate Entry:  Material - " + (String) jsonObject.get("Material Name"));
			objMaterial.setObjsilentaudit(cft);
			return new ResponseEntity<>(objMaterial, HttpStatus.CONFLICT);
		}

		objMaterial.setNmaterialcatcode((Integer) jsonObject.get("nmaterialcatcode"));
		objMaterial.setSmaterialname(jsonObject.getString("Material Name"));
		objMaterial.setNmaterialtypecode((Integer) jsonObject.get("nmaterialtypecode"));
		objMaterial.setNstatus(1);
		objMaterial.setNtransactionstatus((Integer) inputMap.get("ntransactionstatus"));
		objMaterial.setJsondata(strJsonObj);
		objMaterial.setJsonuidata(strJsonUiData);
		objMaterial.setNsitecode(nsiteInteger);
//		objMaterial.setCreateddate(new Date());
		
		try {
			objMaterial.setCreateddate(commonfunction.getCurrentUtcTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		materialRepository.save(objMaterial);

		inputMap.put("nmaterialconfigcode", jsonuidata.get("nmaterialconfigcode"));
		objmap.put("ndesigntemplatemappingcode", jsonuidata.get("nmaterialconfigcode"));
		objmap.put("objsilentaudit", cft);
		objmap.putAll((Map<String, Object>) getMaterialByTypeCode(inputMap).getBody());
		return new ResponseEntity<>(objmap, HttpStatus.OK);
	}

	@SuppressWarnings("unchecked")
	public ResponseEntity<Object> getMaterialDetails(Map<String, Object> inputMap)
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objmapper = new ObjectMapper();
		Map<String, Object> objmap = new LinkedHashMap<String, Object>();

		final Material objMatDetails = materialRepository.findByNmaterialcode((Integer) inputMap.get("nmaterialcode"));

		Map<String, Object> objMaterial = objmapper.readValue(objMatDetails.getJsonuidata(),new TypeReference<Map<String, Object>>() {});

		objMaterial.put("nmaterialcode", objMatDetails.getNmaterialcode());
		objMaterial.put("nstatus", objMatDetails.getNstatus());

		objmap.put("SelectedMaterial", objMaterial);

//		objmap.putAll((Map<String, Object>) objMaterialSectionDAO
//				.getMaterialSectionByMaterialCode((int) inputMap.get("nmaterialcode"), inputMap).getBody());
		objmap.put("nmaterialcode", inputMap.get("nmaterialcode"));
		objmap.put("nstatus", objMatDetails.getNstatus());
//		objmap.putAll((Map<String, Object>) getMaterialSafetyInstructions(objmap).getBody());
//		objmap.putAll((Map<String, Object>) getMaterialFile((int) inputMap.get("nmaterialcode"), userInfo));
		objmap.putAll((Map<String, Object>) getMaterialcombo((int) inputMap.get("nmaterialtypecode"),
				(int) inputMap.get("nsitecode")).getBody());
		objmap.put("DesignMappedFeilds", getTemplateDesignForMaterial(
				(int) inputMap.get("nmaterialtypecode") == Enumeration.TransactionStatus.STANDARDTYPE
						.gettransactionstatus()
								? 1
								: (int) inputMap
										.get("nmaterialtypecode") == Enumeration.TransactionStatus.VOLUMETRICTYPE
												.gettransactionstatus() ? 2 : 3,
				40));
		return new ResponseEntity<>(objmap, HttpStatus.OK);
	}

	@SuppressWarnings("unused")
	private HttpEntity<Object> getMaterialSafetyInstructions(Map<String, Object> inputMap) {
		Map<String, Object> objmap = new LinkedHashMap<String, Object>();

//		ObjectMapper Objmapper = new ObjectMapper();

		int countcheck = materialRepository.countByNmaterialcodeAndNstatus((Integer) inputMap.get("nmaterialcode"), 1);

		if (countcheck > 0) {
			if (inputMap.containsKey("nflag")) {
				if ((int) inputMap.get("nflag") == 1) {
//					final String strQuery = "select * from materialsafetyinstructions where nstatus="
//							+ Enumeration.TransactionStatus.ACTIVE.gettransactionstatus() + " and  nmaterialcode="
//							+ inputMap.get("nmaterialcode");
//					List<MaterialSafetyInstructions> lstMaterial = jdbcTemplate.query(strQuery,
//							new MaterialSafetyInstructions());
//					objmap.put("MaterialSafetyInstructions", lstMaterial);
//					String comboget = "SELECT jsondata" + " from materialconfig" + " where "
//							+ "  nstatus=1 and nmaterialconfigcode=4 ;";
//					List<MaterialConfig> lstMaterialCategory = jdbcTemplate.query(comboget, new MaterialConfig());
//					objmap.put("selectedTemplateSafetyInstructions", lstMaterialCategory);

					List<MaterialConfig> lstMaterialCategory = materialConfigRepository.findByNmaterialconfigcode(4);
					objmap.put("selectedTemplateSafetyInstructions", lstMaterialCategory.get(0).getJsondata());

				} else if ((int) inputMap.get("nflag") == 2) {
					List<Map<String, Object>> lstMaterial = new ArrayList<Map<String, Object>>();

//					final String strQuery = "select json_agg(a.*) from (select * from materialproperties where nstatus="
//							+ Enumeration.TransactionStatus.ACTIVE.gettransactionstatus() + " and  nmaterialcode="
//							+ inputMap.get("nmaterialcode") + ")a";
//
//					
//					String strMaterial = jdbcTemplate.queryForObject(strQuery, String.class);
//
//					if (strMaterial != null)
//						lstMaterial = Objmapper.convertValue(getSiteLocalTimeFromUTCForRegTmplate(strMaterial,
//								objUserInfo, true, 5, "MaterialSafety"), new TypeReference<List<MaterialProperties>>() {
//								});

					objmap.put("MaterialProperties", lstMaterial);

					List<MaterialConfig> lstMaterialCategory = materialConfigRepository.findByNmaterialconfigcode(5);
					objmap.put("selectedTemplateProperties", lstMaterialCategory.get(0).getJsondata());
				}
			} else {
//				final String strQuery1 = "select * from materialsafetyinstructions where nstatus="
//						+ Enumeration.TransactionStatus.ACTIVE.gettransactionstatus() + " and  nmaterialcode="
//						+ inputMap.get("nmaterialcode");
//				List<MaterialSafetyInstructions> lstMaterial1 = jdbcTemplate.query(strQuery1,
//						new MaterialSafetyInstructions());
//				objmap.put("MaterialSafetyInstructions", lstMaterial1);
//				final String strQuery2 = "select * from materialproperties where nstatus="
//						+ Enumeration.TransactionStatus.ACTIVE.gettransactionstatus() + " and  nmaterialcode="
//						+ inputMap.get("nmaterialcode");
//				List<MaterialProperties> lstMaterial2 = jdbcTemplate.query(strQuery2, new MaterialProperties());
//				objmap.put("MaterialProperties", lstMaterial2);

				List<MaterialConfig> lstMaterialCategory = materialConfigRepository.findByNmaterialconfigcode(4);
				objmap.put("selectedTemplateSafetyInstructions", lstMaterialCategory.get(0).getJsondata());
				List<MaterialConfig> lstMaterialCategory1 = materialConfigRepository.findByNmaterialconfigcode(5);
				objmap.put("selectedTemplateProperties", lstMaterialCategory1.get(0).getJsondata());
			}
			return new ResponseEntity<>(objmap, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(Enumeration.ReturnStatus.ALREADYDELETED.getreturnstatus(), HttpStatus.OK);
		}

	}

	public Map<String, Object> getTemplateDesignForMaterial(int nmaterialconfigcode, int nformcode) {

		List<MappedTemplateFieldPropsMaterial> lstMappedTemplate = mappedTemplateFieldPropsMaterialRepository
				.findByNmaterialconfigcode(nmaterialconfigcode);

		Map<String, Object> designObject = new HashMap<String, Object>();

		Map<String, Object> designChildObject = new HashMap<String, Object>();

		Map<String, Object> mapJsonData = commonfunction
				.getInventoryValuesFromJsonString(lstMappedTemplate.get(0).getJsondata(), nformcode + "");

		designChildObject.put("type", "jsonb");
		designChildObject.put("null", true);
		designChildObject.put("value", mapJsonData.get("rtnObj").toString());

		designObject.put("jsondata", designChildObject);

		return designObject;
	}

	public ResponseEntity<Object> getMaterialEdit(Map<String, Object> inputMap)
			throws JsonParseException, JsonMappingException, IOException {
		Map<String, Object> objmap = new LinkedHashMap<String, Object>();
		ObjectMapper objmapper = new ObjectMapper();
		List<Map<String, Object>> lstMaterial = new ArrayList<Map<String, Object>>();
		List<Integer> lstcount = new ArrayList<Integer>();
//		List<MappedTemplateFieldPropsMaterial> lstsearchFields = new LinkedList<>();
//		List<String> lstsearchField = new LinkedList()<String>();
//		List<String> submittedDateFeilds = new LinkedList<String>();

//		String type = (int) inputMap.get("nmaterialtypecode") == Enumeration.TransactionStatus.STANDARDTYPE
//				.gettransactionstatus() ? "MaterialStandard"
//						: (int) inputMap.get("nmaterialtypecode") == Enumeration.TransactionStatus.VOLUMETRICTYPE
//								.gettransactionstatus() ? "MaterialVolumetric" : "MaterialMaterialInventory";
//
//		int typeCode = (int) inputMap.get("nmaterialtypecode") == Enumeration.TransactionStatus.STANDARDTYPE
//				.gettransactionstatus() ? 1
//						: (int) inputMap.get("nmaterialtypecode") == Enumeration.TransactionStatus.VOLUMETRICTYPE
//								.gettransactionstatus() ? 2 : 3;

		lstcount = materialRepository.findByNmaterialcodeAndNstatus((Integer) inputMap.get("nmaterialcode"), -1);

		if (lstcount.size() == 0) {

//			String selectionKeyName1 = "select (jsondata->'" + userInfo.getNformcode() + "'->'" + type
//					+ "datefields')::jsonb as jsondata from mappedtemplatefieldpropsmaterial  where   nstatus="
//					+ Enumeration.TransactionStatus.ACTIVE.gettransactionstatus() + " and nmaterialconfigcode="
//					+ typeCode;
//			
//			lstsearchFields = jdbcTemplate.query(selectionKeyName1, new MappedTemplateFieldPropsMaterial());
//			
//			MappedTemplateFieldPropsMaterial objMaterialFieldPropsMaterial = 
//					mappedTemplateFieldPropsMaterialRepository.findByNmaterialconfigcodeAndNstatus(typeCode,1);
//
//			JSONArray objarray = new JSONArray(lstsearchFields.get(0).getJsondata());
//			for (int i = 0; i < objarray.length(); i++) {
//				JSONObject jsonobject = new JSONObject(objarray.get(i).toString());
//				if (jsonobject.has("2")) {
//					lstsearchField.add((String) jsonobject.get("2"));
//				}
//			}
//			objmap.put("DateFeildsMaterial", lstsearchField);

//			String query1 = " select json_agg(a.jsondata) from(select jsondata||json_build_object('needsectionwise',mc.needsectionwise)::jsonb as jsondata  from  material m,materialcategory"
//					+ " mc where m.nstatus=" + Enumeration.TransactionStatus.ACTIVE.gettransactionstatus()
//					+ " and m.nmaterialcode in (" + inputMap.get("nmaterialcode")
//					+ ") and (jsondata->'nmaterialcatcode')::int=mc.nmaterialcatcode)a;";
			// List<Material> objMaterial = jdbcTemplate.query(query1, new Material());
//			String objMaterial = jdbcTemplate.queryForObject(query1, String.class);

			final Material objMaterial = materialRepository.findByNstatusAndNmaterialcode(1,
					(Integer) inputMap.get("nmaterialcode"));

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

			Map<String, Object> objMaterialMap = objmapper.readValue(objMaterial.getJsondata(),
					new TypeReference<Map<String, Object>>() {
					});

			lstMaterial.add(0, objMaterialMap);

			objmap.put("EditedMaterial", lstMaterial);
			return new ResponseEntity<>(objmap, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
	}

	public ResponseEntity<Object> deleteMaterial(Map<String, Object> inputMap)
			throws JsonParseException, JsonMappingException, IOException {
		final ObjectMapper mapper = new ObjectMapper();
		int countMaterial = materialRepository.countByNmaterialcodeAndNstatus((Integer) inputMap.get("nmaterialcode"),
				1);

		int countMaterialInvent = materialInventoryRepository
				.countByNmaterialcodeAndNstatus((Integer) inputMap.get("nmaterialcode"), 1);

		Material objMaterial = materialRepository.findByNstatusAndNmaterialcode(1,
				(Integer) inputMap.get("nmaterialcode"));

		final LScfttransaction cft = mapper.convertValue(inputMap.get("objsilentaudit"), LScfttransaction.class);
		objMaterial.setObjsilentaudit(cft);
		if (countMaterial != 0) {
			if (countMaterialInvent == 0) {
				objMaterial.setNstatus(-1);
				materialRepository.save(objMaterial);
				return getMaterialByTypeCode(inputMap);

			} else {
				objMaterial.setInfo("Material already in transaction - " + objMaterial.getSmaterialname());
//				return new ResponseEntity<>("Material already in transaction", HttpStatus.CONFLICT);
				return new ResponseEntity<>(objMaterial, HttpStatus.CONFLICT);
			}

		} else {
			return new ResponseEntity<>("Material already in deleted", HttpStatus.CONFLICT);
		}
	}

	@SuppressWarnings("unchecked")
	public ResponseEntity<Object> getMaterialByID(Map<String, Object> inputMap)
			throws JsonParseException, JsonMappingException, IOException {
		Map<String, Object> objmap = new LinkedHashMap<String, Object>();
		List<Map<String, Object>> lstMaterial = new ArrayList<Map<String, Object>>();
//		ObjectMapper objMapper = new ObjectMapper();

		final Material objMatDetails = materialRepository.findByNstatusAndNmaterialcode(1,
				(Integer) inputMap.get("nmaterialcode"));

		if (objMatDetails != null) {
			Map<String, Object> objMaterial = new ObjectMapper().readValue(objMatDetails.getJsonuidata(), Map.class);

			objMaterial.put("nmaterialcode", objMatDetails.getNmaterialcode());

			lstMaterial.add(objMaterial);

			objmap.put("SelectedMaterial", lstMaterial.get(lstMaterial.size() - 1));
		}

//		objmap.putAll((Map<String, Object>) objMaterialSectionDAO
//				.getMaterialSectionByMaterialCode((int) inputMap.get("nmaterialcode"), inputMap).getBody());
//		objmap.putAll((Map<String, Object>) getMaterialFile((int) inputMap.get("nmaterialcode"), userInfo));
		return new ResponseEntity<>(objmap, HttpStatus.OK);
	}

	@SuppressWarnings({ "unchecked", "unused", })
	public ResponseEntity<Object> UpdateMaterial(Map<String, Object> inputMap)
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objmapper = new ObjectMapper();

		Map<String, Object> objmap = new LinkedHashMap<String, Object>();

		JSONObject jsonObject = new JSONObject(inputMap.get("materialJson").toString());
		JSONObject jsonuidata = new JSONObject(inputMap.get("jsonuidata").toString());

		String strJsonObj = inputMap.get("materialJson").toString();
		String strJsonUiData = inputMap.get("jsonuidata").toString();

		Material matobj = new Material();

		final LScfttransaction cft = objmapper.convertValue(inputMap.get("objsilentaudit"), LScfttransaction.class);
		Map<String, Object> objMat = (Map<String, Object>) inputMap.get("Material");
		Integer nsiteInteger = (Integer) inputMap.get("nsitecode");
		final Material objMaterial = materialRepository.findByNstatusAndNmaterialcode(1,
				(Integer) inputMap.get("nmaterialcode"));

		Material ObjMatNamecheck = materialRepository.findBySmaterialnameAndNmaterialtypecode(
				(String) objMat.get("Material Name"), (Integer) inputMap.get("nmaterialtypecode"));

		if (ObjMatNamecheck == null) {
			if (jsonObject.has("Prefix")) {
//
				List<Material> lstcheckPrefix = materialRepository.findByNmaterialtypecodeAndSprefix((Integer) inputMap.get("nmaterialtypecode"),(String) jsonObject.get("Prefix"));

				if (objMaterial.getNmaterialcode() != lstcheckPrefix.get(0).getNmaterialcode()) {
					if (lstcheckPrefix != null && ObjMatNamecheck != null) {
						matobj.setSprefix(ObjMatNamecheck.getSprefix());
						matobj.setObjsilentaudit(cft);
						matobj.setInfo("Duplicate Entry : Material- " + ObjMatNamecheck.getSprefix());
						return new ResponseEntity<>(matobj, HttpStatus.CONFLICT);
					}
				}
			}
		} else {
			if (objMaterial.getNmaterialcode() != ObjMatNamecheck.getNmaterialcode()) {
				matobj.setSmaterialname(ObjMatNamecheck.getSmaterialname());
				matobj.setObjsilentaudit(cft);
				matobj.setInfo("Duplicate Entry : Material- " + ObjMatNamecheck.getSmaterialname());
				return new ResponseEntity<>(matobj, HttpStatus.CONFLICT);
			}
		}

		objMaterial.setJsondata(strJsonObj.toString());
		objMaterial.setJsonuidata(strJsonUiData.toString());
		objMaterial.setNmaterialcatcode((Integer) jsonObject.get("nmaterialcatcode"));
		objMaterial.setNtransactionstatus((Integer) inputMap.get("ntransactionstatus"));
		objMaterial.setNmaterialcode((Integer) inputMap.get("nmaterialcode"));
		objMaterial.setObjsilentaudit(cft);
		objMaterial.setSmaterialname((String) objMat.get("Material Name"));
		objMaterial.setSprefix((String) objMat.get("Prefix"));
		materialRepository.save(objMaterial);

		if (inputMap.containsKey("needsectionwise")
				&& (int) inputMap.get("needsectionwise") == Enumeration.TransactionStatus.YES.gettransactionstatus()) {
			jsonObject = new JSONObject(inputMap.get("materialSectionJson").toString());
			jsonObject.put("nmaterialcode", inputMap.get("nmaterialcode"));
			inputMap.put("nmaterialcode", inputMap.get("nmaterialcode"));
			inputMap.put("materialSectionJson", jsonObject);
		}
		List<Map<String, Object>> lstMaterial = new ArrayList<Map<String, Object>>();
		Map<String, Object> objMaterialMap = objmapper.readValue(objMaterial.getJsondata(),
				new TypeReference<Map<String, Object>>() {
				});
		lstMaterial.add(0, objMaterialMap);
		objmap.put("SelectedMaterial", lstMaterial.get(0));
		List<Material> materialLst = materialRepository
				.findByNmaterialcatcodeAndNmaterialtypecodeAndNsitecodeAndNstatus(
						(Integer) inputMap.get("nmaterialcatcode"), (Integer) inputMap.get("nmaterialtypecode"),
						nsiteInteger, 1);
		List<MaterialType> lstMaterialType = materialTypeRepository
				.findByNmaterialtypecode((Integer) inputMap.get("nmaterialtypecode"));
		objmap.put("SelectedMaterialType", lstMaterialType);
		objmap.put("Material", materialLst);
		MaterialCategory objMaterialCategory = materialCategoryRepository.findByNmaterialcatcode((Integer) inputMap.get("nmaterialcatcode"));
		objmap.put("SelectedMaterialCategory", objMaterialCategory);
		return getMaterialByTypeCode(inputMap);
//		return new ResponseEntity<>(getMaterialByTypeCode(inputMap), HttpStatus.OK);
	}
	public Material CloudUploadattachments(MultipartFile file, Integer nmaterialcatcode, String filename,
			String fileexe, Integer usercode, Date currentdate,Integer isMultitenant) throws IOException {
		Material objmaterial = materialRepository.findOne(nmaterialcatcode);
		LsOrderattachments objattachment = new LsOrderattachments();
		if(isMultitenant == 0) {
			if (fileManipulationservice.storeLargeattachment(filename, file) != null) {
				objattachment.setFileid(fileManipulationservice.storeLargeattachment(filename, file));
			}	
		}
		
     	objattachment.setFilename(filename);
		objattachment.setFileextension(fileexe);
		objattachment.setCreateby(lsuserMasterRepository.findByusercode(usercode));
		objattachment.setCreatedate(currentdate);
		objattachment.setNmaterialcode(objmaterial.getNmaterialcode());
		if (objmaterial != null && objmaterial.getLsOrderattachments() != null) {
			objmaterial.getLsOrderattachments().add(objattachment);
		} else {
			objmaterial.setLsOrderattachments(new ArrayList<LsOrderattachments>());
			objmaterial.getLsOrderattachments().add(objattachment);
		}
		lsOrderattachmentsRepository.save(objmaterial.getLsOrderattachments());		
		if(isMultitenant != 0) {
		String filenameval = "attach_"+ objmaterial.getNmaterialcode() + "_" + objmaterial.getLsOrderattachments()
		.get(objmaterial.getLsOrderattachments().lastIndexOf(objattachment)).getAttachmentcode()+ "_" + filename;
			String id = cloudFileManipulationservice.storeLargeattachment(filenameval, file);
			if (id != null) {
				objattachment.setFileid(id);
		}
	
		lsOrderattachmentsRepository.save(objmaterial.getLsOrderattachments());
		}	

		return objmaterial;
	}

	public Map<String, Object> getAttachments(Map<String, Object> objMap) {
		Map<String, Object> obj = new HashMap<>();
		List<LsOrderattachments> lstattach = lsOrderattachmentsRepository.findByNmaterialcodeOrderByAttachmentcodeDesc(objMap.get("nmaterialcode"));
		obj.put("lsOrderattachments", lstattach);			
		return obj;

	}
}