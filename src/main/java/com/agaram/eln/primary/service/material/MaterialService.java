package com.agaram.eln.primary.service.material;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import com.agaram.eln.primary.commonfunction.commonfunction;
import com.agaram.eln.primary.global.Enumeration;
import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.cloudFileManip.CloudOrderAttachment;
import com.agaram.eln.primary.model.equipment.EquipmentCategory;
import com.agaram.eln.primary.model.equipment.EquipmentType;
import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.instrumentDetails.LsOrderattachments;
import com.agaram.eln.primary.model.material.Elnmaterial;
import com.agaram.eln.primary.model.material.ElnresultUsedMaterial;
import com.agaram.eln.primary.model.material.MappedTemplateFieldPropsMaterial;
import com.agaram.eln.primary.model.material.Material;
import com.agaram.eln.primary.model.material.MaterialAttachments;
import com.agaram.eln.primary.model.material.MaterialCategory;
import com.agaram.eln.primary.model.material.MaterialConfig;
import com.agaram.eln.primary.model.material.MaterialLinks;
import com.agaram.eln.primary.model.material.MaterialProjectHistory;
import com.agaram.eln.primary.model.material.MaterialProjectMap;
import com.agaram.eln.primary.model.material.MaterialType;
import com.agaram.eln.primary.model.material.Period;
import com.agaram.eln.primary.model.material.Section;
import com.agaram.eln.primary.model.material.Unit;
import com.agaram.eln.primary.model.sample.ElnresultUsedSample;
import com.agaram.eln.primary.model.sample.Sample;
import com.agaram.eln.primary.model.sample.SampleAttachments;
import com.agaram.eln.primary.model.sample.SampleCategory;
import com.agaram.eln.primary.model.sample.SampleProjectMap;
import com.agaram.eln.primary.model.sample.SampleType;
import com.agaram.eln.primary.model.sequence.SequenceTable;
import com.agaram.eln.primary.model.sequence.SequenceTableProjectLevel;
import com.agaram.eln.primary.model.sequence.SequenceTableSite;
import com.agaram.eln.primary.model.sequence.SequenceTableTaskLevel;
import com.agaram.eln.primary.model.usermanagement.LSprojectmaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.repository.cloudFileManip.CloudOrderAttachmentRepository;
import com.agaram.eln.primary.repository.equipment.EquipmentCategoryRepository;
import com.agaram.eln.primary.repository.equipment.EquipmentTypeRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LsOrderattachmentsRepository;
import com.agaram.eln.primary.repository.material.ElnmaterialRepository;
import com.agaram.eln.primary.repository.material.MappedTemplateFieldPropsMaterialRepository;
import com.agaram.eln.primary.repository.material.MaterialAttachmentsRepository;
import com.agaram.eln.primary.repository.material.MaterialCategoryRepository;
import com.agaram.eln.primary.repository.material.MaterialConfigRepository;
import com.agaram.eln.primary.repository.material.MaterialInventoryRepository;
import com.agaram.eln.primary.repository.material.MaterialLinksRepository;
import com.agaram.eln.primary.repository.material.MaterialProjectHistoryRepository;
import com.agaram.eln.primary.repository.material.MaterialProjectMapRepository;
import com.agaram.eln.primary.repository.material.MaterialRepository;
import com.agaram.eln.primary.repository.material.MaterialTypeRepository;
import com.agaram.eln.primary.repository.material.PeriodRepository;
import com.agaram.eln.primary.repository.material.SectionRepository;
import com.agaram.eln.primary.repository.material.UnitRepository;
import com.agaram.eln.primary.repository.sample.SampleAttachementsRepository;
import com.agaram.eln.primary.repository.sample.SampleProjectHistoryRepository;
import com.agaram.eln.primary.repository.sample.SampleProjectMapRepository;
import com.agaram.eln.primary.repository.sample.SampleRepository;
import com.agaram.eln.primary.repository.sequence.SequenceTableRepository;
import com.agaram.eln.primary.repository.sequence.SequenceTableSiteRepository;
import com.agaram.eln.primary.repository.usermanagement.LSuserMasterRepository;
import com.agaram.eln.primary.service.cloudFileManip.CloudFileManipulationservice;
import com.agaram.eln.primary.service.fileManipulation.FileManipulationservice;
import com.agaram.eln.primary.service.protocol.Commonservice;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
//import com.google.gson.Gson;

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
	@Autowired
	ElnmaterialRepository elnmaterialRepository;
	@Autowired
	UnitRepository unitRepository;
	@Autowired
	SectionRepository sectionRepository;
	@Autowired
	PeriodRepository periodRepository;
	@Autowired
	EquipmentTypeRepository equipmentTypeRepository;
	@Autowired
	EquipmentCategoryRepository equipmentCategoryRepository;
	@Autowired
	private CloudOrderAttachmentRepository CloudOrderAttachmentRepository;
	@Autowired
	private MaterialAttachmentsRepository materialAttachmentsRepository;
	@Autowired
	private SequenceTableRepository sequencetableRepository;
	@Autowired
	private SequenceTableSiteRepository sequencetablesiteRepository;
	@Autowired
	private MaterialLinksRepository materiallinksrepository;
	@Autowired
	private MaterialProjectHistoryRepository materialprojecthistoryrepository;
	@Autowired
	private SampleRepository SampleRepository;
	@Autowired
	private SampleAttachementsRepository SampleAttachementsRepository;
	@Autowired
	private SampleProjectHistoryRepository SampleProjectHistoryRepository;
	@Autowired
	private MaterialProjectMapRepository materialprojectmapRepository;
	@Autowired
	private Commonservice commonService;
	@Autowired
	private SequenceTableRepository sequenceTableRepository;

	@Autowired
	private SampleProjectMapRepository SampleProjectMapRepository;

	public ResponseEntity<Object> getMaterialcombo(Integer nmaterialtypecode, Integer nsitecode) {

		Map<String, Object> objmap = new LinkedHashMap<String, Object>();

		List<MaterialCategory> lstMaterialCategory = materialCategoryRepository
				.findByNmaterialtypecodeAndNsitecodeAndNstatusOrderByNmaterialcatcode(nmaterialtypecode, nsitecode, 1);

		if (nmaterialtypecode == -1) {

			List<MaterialCategory> objLstCategories = new ArrayList<MaterialCategory>();

			MaterialCategory objCategory = new MaterialCategory();
			objCategory.setNmaterialcatcode(-1);
			objCategory.setSmaterialcatname("ALL");
			objCategory.setSdescription("ALL");
			objLstCategories.add(objCategory);

			objmap.put("MaterialCategoryMain", objLstCategories);
			return new ResponseEntity<>(objmap, HttpStatus.OK);
		} else {

			MaterialCategory objCategory = new MaterialCategory();
			objCategory.setNmaterialcatcode(-1);
			objCategory.setSmaterialcatname("ALL");
			objCategory.setSdescription("ALL");
			lstMaterialCategory.add(objCategory);

			// Define a custom comparator to sort by nmaterialcatcode
			Comparator<MaterialCategory> comparator = Comparator.comparingInt(MaterialCategory::getNmaterialcatcode);

			// Sort the list using the custom comparator
			Collections.sort(lstMaterialCategory, comparator);

			objmap.put("MaterialCategoryMain", lstMaterialCategory);
			return new ResponseEntity<>(objmap, HttpStatus.OK);
		}
	}

	@SuppressWarnings("unchecked")
	public ResponseEntity<Object> getMaterialType(Integer nsiteInteger)
			throws JsonParseException, JsonMappingException, IOException {

		Map<String, Object> objmap = new LinkedHashMap<String, Object>();

		List<MaterialType> lstMaterialType = materialTypeRepository.findByNstatusOrderByNmaterialtypecode(1);

		objmap.put("MaterialType", lstMaterialType);
		objmap.put("SelectedMaterialType", lstMaterialType);

		if (lstMaterialType.get(0).getNmaterialtypecode() == -1) {
			List<MaterialCategory> objLstCategories = new ArrayList<MaterialCategory>();

			MaterialCategory objCategory = new MaterialCategory();
			objCategory.setNmaterialcatcode(-1);
			objCategory.setSmaterialcatname("ALL");
			objCategory.setSdescription("ALL");
			objLstCategories.add(objCategory);

			objmap.put("MaterialCategoryMain", objLstCategories);
			objmap.put("nmaterialcatcode", -1);
			objmap.put("nmaterialtypecode", lstMaterialType.get(0).getNmaterialtypecode());
			objmap.put("nsitecode", nsiteInteger);
			objmap.put("SelectedMaterialCategory", objCategory);
			objmap.putAll((Map<String, Object>) getMaterialInitiallyByAll(objmap).getBody());
			return new ResponseEntity<>(objmap, HttpStatus.OK);
		} else {
			if (!lstMaterialType.isEmpty()) {
				List<MaterialCategory> lstMaterialCategory = materialCategoryRepository
						.findByNmaterialtypecodeAndNsitecodeAndNstatus(lstMaterialType.get(0).getNmaterialtypecode(),
								nsiteInteger, 1);

				if (!lstMaterialCategory.isEmpty()) {
					objmap.put("MaterialCategoryMain", lstMaterialCategory);
					objmap.put("nmaterialcatcode", lstMaterialCategory.get(0).getNmaterialcatcode());
				}
				objmap.put("nmaterialtypecode", lstMaterialType.get(0).getNmaterialtypecode());
				objmap.put("nsitecode", nsiteInteger);
				objmap.putAll((Map<String, Object>) getMaterialInitial(objmap).getBody());
			}
		}

		return new ResponseEntity<>(objmap, HttpStatus.OK);
	}

	public ResponseEntity<Object> getMaterialonCategory(@RequestBody Map<String, Object> inputMap) {

		final ObjectMapper objmapper = new ObjectMapper();
		Boolean isallproject = (Boolean) inputMap.get("isallproject");
		Boolean isgeneralproject = (Boolean) inputMap.get("isgeneralproject");
		List<Integer> objlstProject = objmapper.convertValue(inputMap.get("projects"),
				new TypeReference<List<Integer>>() {
				});
		MaterialCategory objCategory = objmapper.convertValue(inputMap.get("category"), MaterialCategory.class);
		Date fromdate = objmapper.convertValue(inputMap.get("fromdate"), Date.class);
		Date todate = objmapper.convertValue(inputMap.get("todate"), Date.class);

		List<Elnmaterial> lstMaterial = new ArrayList<Elnmaterial>();
		if (isallproject) {
			lstMaterial = elnmaterialRepository
					.findByMaterialcategoryAndNsitecodeAndCreateddateBetweenOrderByNmaterialcodeDesc(objCategory,
							objCategory.getNsitecode(), fromdate, todate);
		} else if (isgeneralproject) {
			lstMaterial = elnmaterialRepository.getMaterialOnGeneralProjects(objCategory.getNmaterialcatcode(),
					objCategory.getNsitecode(), fromdate, todate);
		} else {
			lstMaterial = elnmaterialRepository.getMaterialOnProjects(objCategory.getNmaterialcatcode(),
					objCategory.getNsitecode(), fromdate, todate, objlstProject);
		}

		return new ResponseEntity<>(lstMaterial, HttpStatus.OK);
	}

	public ResponseEntity<Object> getMaterialDesign(Integer ntypecode)
			throws JsonParseException, JsonMappingException, IOException {

		Map<String, Object> objmap = new LinkedHashMap<String, Object>();

		Material objMaterial = materialRepository.findByNmaterialcode(ntypecode);

		List<MaterialConfig> lstMaterialConfig = materialConfigRepository
				.findByNmaterialtypecodeAndNformcode(objMaterial.getNmaterialtypecode(), 40);
		objmap.put("selectedTemplate", lstMaterialConfig);

		return new ResponseEntity<>(objmap, HttpStatus.OK);
	}

	public ResponseEntity<Object> getMaterialTypeDesign(Integer ntypecode)
			throws JsonParseException, JsonMappingException, IOException {

		Map<String, Object> objmap = new LinkedHashMap<String, Object>();

		List<MaterialConfig> lstMaterialConfig = materialConfigRepository.findByNmaterialtypecodeAndNformcode(ntypecode,
				40);
		objmap.put("selectedTemplate", lstMaterialConfig);
		objmap.put("DesignMappedFeilds", getTemplateDesignForMaterial(
				ntypecode == Enumeration.TransactionStatus.STANDARDTYPE.gettransactionstatus() ? 1
						: ntypecode == Enumeration.TransactionStatus.VOLUMETRICTYPE.gettransactionstatus() ? 2 : 3,
				40));

//		if(!lstMaterialConfig.isEmpty()) {
//			objmap.put("selectedGridProps", lstMaterialConfig.get(0));
//		}

		return new ResponseEntity<>(objmap, HttpStatus.OK);
	}

	@SuppressWarnings("unchecked")
	public ResponseEntity<Object> getMaterialInitiallyByAll(Map<String, Object> inputMap)
			throws JsonParseException, JsonMappingException, IOException {
		final ObjectMapper objmapper = new ObjectMapper();
		Map<String, Object> objmap = new LinkedHashMap<String, Object>();
		Map<String, Object> selectedMaterialObj = new LinkedHashMap<String, Object>();
		Integer nsiteInteger = (Integer) inputMap.get("nsitecode");

		final LScfttransaction cft = objmapper.convertValue(inputMap.get("objsilentaudit"), LScfttransaction.class);
		final List<Material> lstMaterial = materialRepository.findByNsitecodeOrderByNmaterialcodeDesc(nsiteInteger);

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

					objmap.put("SelectedMaterial", selectedMaterialObj);
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
					objmap.put("SelectedMaterial", selectedMaterialObj);
				}

				List<MaterialType> lstMaterialType = materialTypeRepository
						.findByNmaterialtypecode((Integer) inputMap.get("nmaterialtypecode"));
				objmap.put("SelectedMaterialType", lstMaterialType);

//				List<MaterialCategory> objLstMaterialCategory = new ArrayList<>();
//
//				if (inputMap.containsKey("nmaterialcatcode")) {
//					MaterialCategory objMaterialCategory = materialCategoryRepository
//							.findByNmaterialcatcodeAndNstatus((Integer) inputMap.get("nmaterialcatcode"), 1);
//
//					objLstMaterialCategory.add(objMaterialCategory);
//				} else {
//					objLstMaterialCategory = materialCategoryRepository
//							.findByNmaterialtypecode((Integer) inputMap.get("nmaterialtypecode"));
//				}
//				List<MaterialCategory> lstMaterialCategory = objLstMaterialCategory;
//
//				if (!lstMaterialCategory.isEmpty()) {
//					if (inputMap.containsKey("nmaterialcatcode")) {
//						objmap.put("SelectedMaterialCategory", lstMaterialCategory.get(0));
//					} else {
//						objmap.put("SelectedMaterialCategory", lstMaterialCategory.get(0));
//					}
//				}

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
//				objmap.put("SelectedMaterial", lstActiontype);
//				objmap.put("SelectedMaterialCategory", lstActiontype);

			}
		}

		List<MaterialConfig> lstMaterialConfig = materialConfigRepository
				.findByNmaterialtypecodeAndNformcode((Integer) inputMap.get("nmaterialtypecode"), 40);
		objmap.put("selectedTemplate", lstMaterialConfig);
		if (!lstMaterialConfig.isEmpty()) {
			objmap.put("selectedGridProps", lstMaterialConfig.get(0));
		}
		if (cft != null) {
			objmap.put("objsilentaudit", cft);
		}
		return new ResponseEntity<>(objmap, HttpStatus.OK);
	}

	@SuppressWarnings("unchecked")
	public ResponseEntity<Object> getMaterialInitial(Map<String, Object> inputMap)
			throws JsonParseException, JsonMappingException, IOException {
		final ObjectMapper objmapper = new ObjectMapper();
		Map<String, Object> objmap = new LinkedHashMap<String, Object>();
		Map<String, Object> selectedMaterialObj = new LinkedHashMap<String, Object>();
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

					objmap.put("SelectedMaterial", selectedMaterialObj);
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
					objmap.put("SelectedMaterial", selectedMaterialObj);
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
//				objmap.put("SelectedMaterial", lstActiontype);
				objmap.put("SelectedMaterialCategory", lstActiontype);

			}
		}

		List<MaterialConfig> lstMaterialConfig = materialConfigRepository
				.findByNmaterialtypecodeAndNformcode((Integer) inputMap.get("nmaterialtypecode"), 40);
		objmap.put("selectedTemplate", lstMaterialConfig);
		if (!lstMaterialConfig.isEmpty()) {
			objmap.put("selectedGridProps", lstMaterialConfig.get(0));
		}
		if (cft != null) {
			objmap.put("objsilentaudit", cft);
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
//		final List<Material> lstMaterial = materialRepository.findByNmaterialcatcodeAndNmaterialtypecodeAndNsitecode(
//				(Integer) inputMap.get("nmaterialcatcode"), (Integer) inputMap.get("nmaterialtypecode"), nsiteInteger);

		List<Material> lstMaterial = new ArrayList<Material>();

		if ((Integer) inputMap.get("nmaterialtypecode") == -1) {
//			List<MaterialCategory> objLstCategories= new ArrayList<MaterialCategory>();
//			MaterialCategory objCategory = new MaterialCategory();
//			objCategory.setNmaterialcatcode(-1);
//			objCategory.setSmaterialcatname("ALL");
//			objCategory.setSdescription("ALL");
//			objLstCategories.add(objCategory);
//			
//			objmap.put("MaterialCategoryMain", objLstCategories);
//			
//			objmap.put("SelectedMaterialCategory",objCategory);
//			 
//			lstMaterial = materialRepository.findByNsitecodeOrderByNmaterialcodeDesc(nsiteInteger);

			List<MaterialCategory> objLstCategories = new ArrayList<MaterialCategory>();

			MaterialCategory objCategory = new MaterialCategory();
			objCategory.setNmaterialcatcode(-1);
			objCategory.setSmaterialcatname("ALL");
			objCategory.setSdescription("ALL");
			objLstCategories.add(objCategory);

			objmap.put("MaterialCategoryMain", objLstCategories);
			objmap.put("nmaterialcatcode", -1);
			objmap.put("nmaterialtypecode", -1);
			objmap.put("nsitecode", nsiteInteger);
			objmap.put("SelectedMaterialCategory", objCategory);
			objmap.putAll((Map<String, Object>) getMaterialInitiallyByAll(objmap).getBody());
			return new ResponseEntity<>(objmap, HttpStatus.OK);
		} else {
			if ((Integer) inputMap.get("nmaterialcatcode") == -1) {
				lstMaterial = materialRepository.findByNmaterialtypecodeAndNsitecodeOrderByNmaterialcodeDesc(
						(Integer) inputMap.get("nmaterialtypecode"), nsiteInteger);
			} else {
				lstMaterial = materialRepository
						.findByNmaterialcatcodeAndNmaterialtypecodeAndNsitecodeOrderByNmaterialcodeDesc(
								(Integer) inputMap.get("nmaterialcatcode"), (Integer) inputMap.get("nmaterialtypecode"),
								nsiteInteger);
			}
		}

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

					if ((Integer) inputMap.get("nmaterialcatcode") == -1) {

						MaterialCategory objCategory = new MaterialCategory();
						objCategory.setNmaterialcatcode(-1);
						objCategory.setSmaterialcatname("ALL");
						objCategory.setSdescription("ALL");

						objLstMaterialCategory.add(objCategory);
					} else {
						objLstMaterialCategory.add(objMaterialCategory);
					}
				} else {
					objLstMaterialCategory = materialCategoryRepository
							.findByNmaterialtypecode((Integer) inputMap.get("nmaterialtypecode"));
				}
				List<MaterialCategory> lstMaterialCategory = objLstMaterialCategory;

				if (!lstMaterialCategory.isEmpty()) {
					if (inputMap.containsKey("nmaterialcatcode")) {
						objmap.put("SelectedMaterialCategory", lstMaterialCategory.get(0));
						if ((Integer) inputMap.get("nmaterialcatcode") == -1) {
							MaterialCategory objCategory = new MaterialCategory();
							objCategory.setNmaterialcatcode(-1);
							objCategory.setSmaterialcatname("ALL");
							objCategory.setSdescription("ALL");
							objmap.put("SelectedMaterialCategory", objCategory);
						}
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
		if (!lstMaterialConfig.isEmpty()) {
			objmap.put("selectedGridProps", lstMaterialConfig.get(0));
		}
		if (cft != null) {
			objmap.put("objsilentaudit", cft);
		}
		return new ResponseEntity<>(objmap, HttpStatus.OK);
	}

	@SuppressWarnings("unchecked")
	public ResponseEntity<Object> getMaterialBySearchField(Map<String, Object> inputMap)
			throws JsonParseException, JsonMappingException, IOException {

		final ObjectMapper objmapper = new ObjectMapper();
		Map<String, Object> objmap = new LinkedHashMap<String, Object>();
		Integer nsiteInteger = (Integer) inputMap.get("nsitecode");
		final LScfttransaction cft = objmapper.convertValue(inputMap.get("objsilentaudit"), LScfttransaction.class);
		String searchString = (String) inputMap.get("searchString");

		List<Material> lstMaterial = new ArrayList<Material>();

		if ((Integer) inputMap.get("nmaterialtypecode") != -1) {
			if (searchString.trim() != "") {
				lstMaterial = materialRepository
						.findBySmaterialnameStartingWithIgnoreCaseAndNmaterialcatcodeAndNmaterialtypecodeAndNsitecode(
								searchString, (Integer) inputMap.get("nmaterialcatcode"),
								(Integer) inputMap.get("nmaterialtypecode"), nsiteInteger);
			} else {
				lstMaterial = materialRepository.findByNmaterialcatcodeAndNmaterialtypecodeAndNsitecode(
						(Integer) inputMap.get("nmaterialcatcode"), (Integer) inputMap.get("nmaterialtypecode"),
						nsiteInteger);
			}
		} else {
			if (searchString.trim() != "") {
				lstMaterial = materialRepository.findBySmaterialnameStartingWithIgnoreCaseAndNsitecode(searchString,
						nsiteInteger);
			} else {
				lstMaterial = materialRepository.findByNsitecode(nsiteInteger);
			}
		}

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
		if (!lstMaterialConfig.isEmpty()) {
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

		List<Material> lstMaterial = new ArrayList<Material>();

//		final List<Material> lstMaterial = materialRepository
//				.findByNmaterialcatcodeAndNmaterialtypecodeAndNsitecodeAndCreateddateBetween(
//						(Integer) inputMap.get("nmaterialcatcode"), (Integer) inputMap.get("nmaterialtypecode"),
//						nsiteInteger, fromDate, toDate);

		if ((Integer) inputMap.get("nmaterialtypecode") != -1 && (Integer) inputMap.get("nmaterialcatcode") == -1) {
			lstMaterial = materialRepository.findByNmaterialtypecodeAndNsitecodeAndCreateddateBetween(
					(Integer) inputMap.get("nmaterialtypecode"), nsiteInteger, fromDate, toDate);

		} else if ((Integer) inputMap.get("nmaterialtypecode") == -1
				&& (Integer) inputMap.get("nmaterialcatcode") == -1) {
			lstMaterial = materialRepository.findByNsitecodeAndCreateddateBetween(nsiteInteger, fromDate, toDate);

		} else {
			lstMaterial = materialRepository
					.findByNmaterialcatcodeAndNmaterialtypecodeAndNsitecodeAndCreateddateBetween(
							(Integer) inputMap.get("nmaterialcatcode"), (Integer) inputMap.get("nmaterialtypecode"),
							nsiteInteger, fromDate, toDate);
		}

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

//				List<MaterialCategory> lstMaterialCategory = objLstMaterialCategory;
//
//				if (!lstMaterialCategory.isEmpty()) {
//					if (inputMap.containsKey("nmaterialcatcode")) {
//						objmap.put("SelectedMaterialCategory", lstMaterialCategory.get(0));
//					} else {
//						objmap.put("SelectedMaterialCategory", lstMaterialCategory.get(0));
//					}
//				}

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
//				objmap.put("SelectedMaterialCategory", lstActiontype);

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

		Map<String, Object> objMaterialProps = (Map<String, Object>) inputMap.get("Material");

		inputMap.get("DateList");
		boolean nflag = false;

		final Material ObjMatNamecheck = materialRepository.findBySmaterialnameAndNmaterialtypecodeAndNsitecode(
				(String) jsonObject.get("Material Name"), (Integer) jsonObject.get("nmaterialtypecode"), nsiteInteger);

		if (ObjMatNamecheck == null) {

			if (jsonObject.has("Prefix")) {
				objMaterial.setSprefix((String) jsonObject.get("Prefix"));
				nflag = true;

				final Material objMaterialClass = materialRepository.findByNstatusAndSprefixAndNsitecode(1,
						(String) jsonObject.get("Prefix"), nsiteInteger);

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

		/**
		 * Added for validate inventory expiry Start
		 */

		Boolean expiryPolicy = objMaterialProps.get("Expiry Validations") == null ? false
				: objMaterialProps.get("Expiry Validations").toString().equalsIgnoreCase("Expiry policy") ? true
						: false;
		Boolean openExpiry = objMaterialProps.get("Open Expiry Need") == null ? false
				: objMaterialProps.get("Open Expiry Need").toString().equalsIgnoreCase("3") ? true : false;
		Boolean nextValidation = objMaterialProps.get("Next Validation Need") == null ? false
				: objMaterialProps.get("Next Validation Need").toString().equalsIgnoreCase("3") ? true : false;

		String expiryValue = objMaterialProps.get("Expiry Policy Days") == null ? ""
				: objMaterialProps.get("Expiry Policy Days").toString();
		String openValue = objMaterialProps.get("Open Expiry") == null ? ""
				: objMaterialProps.get("Open Expiry").toString();
		String nextValValue = objMaterialProps.get("Next Validation") == null ? ""
				: objMaterialProps.get("Next Validation").toString();

		Map<String, Object> expiryPeriodMap = new HashMap<>();
		Map<String, Object> openPeriodMap = new HashMap<>();
		Map<String, Object> nextPeriodMap = new HashMap<>();
		String expiryPeriod = "";
		String openPeriod = "";
		String nextPeriod = "";

		if (expiryPolicy) {
			expiryPeriodMap = (Map<String, Object>) objMaterialProps.get("Expiry Policy Period");
			expiryPeriod = expiryPeriodMap.get("label").toString();
		}
		if (openExpiry) {
			openPeriodMap = (Map<String, Object>) objMaterialProps.get("Open Expiry Period");
			openPeriod = openPeriodMap.get("label").toString();
		}
		if (nextValidation) {
			nextPeriodMap = (Map<String, Object>) objMaterialProps.get("Next Validation Period");
			nextPeriod = nextPeriodMap.get("label").toString();
		}

		objMaterial.setExpirypolicy(expiryPolicy);
		objMaterial.setExpirypolicyvalue(expiryValue);
		objMaterial.setExpirypolicyperiod(expiryPeriod);

		objMaterial.setOpenexpiry(openExpiry);
		objMaterial.setOpenexpiryvalue(openValue);
		objMaterial.setOpenexpiryperiod(openPeriod);

		objMaterial.setNextvalidation(nextValidation);
		objMaterial.setNextvalidationvalue(nextValValue);
		objMaterial.setNextvalidationperiod(nextPeriod);

		/**
		 * End
		 */

		objMaterial.setNmaterialcatcode((Integer) jsonObject.get("nmaterialcatcode"));
		objMaterial.setSmaterialname(jsonObject.getString("Material Name"));
		objMaterial.setNmaterialtypecode((Integer) jsonObject.get("nmaterialtypecode"));
		objMaterial.setNstatus(1);
		objMaterial.setNtransactionstatus(
				inputMap.get("ntransactionstatus") == null ? 4 : (Integer) inputMap.get("ntransactionstatus"));
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

	public SequenceTable validateandupdatematerialsequencenumber(Elnmaterial objInv,
			SequenceTableProjectLevel objprojectseq, SequenceTableTaskLevel objtaskseq) throws ParseException {
		SequenceTable seqorder = new SequenceTable();
		int sequence = 3;
		seqorder = sequencetableRepository.findOne(sequence);
		if (seqorder != null && seqorder.getApplicationsequence() == -1) {
			long appcount = elnmaterialRepository.count();
			sequencetableRepository.setinitialapplicationsequence(appcount, sequence);
			seqorder.setApplicationsequence(appcount);
		}

		Date currentdate = commonfunction.getCurrentUtcTime();
		SimpleDateFormat day = new SimpleDateFormat("dd");
		SimpleDateFormat month = new SimpleDateFormat("MM");
		SimpleDateFormat year = new SimpleDateFormat("yyyy");
		if (seqorder.getSequenceday() == 0) {
			seqorder.setSequenceday(Integer.parseInt(day.format(currentdate)));
		}

		if (seqorder.getSequencemonth() == 0) {
			seqorder.setSequencemonth(Integer.parseInt(month.format(currentdate)));
		}

		if (seqorder.getSequenceyear() == 0) {
			seqorder.setSequenceyear(Integer.parseInt(year.format(currentdate)));
		}

		if (sequencetablesiteRepository.findBySequencecodeAndSitecode(sequence, objInv.getNsitecode()) == null) {
			SequenceTableSite objsiteseq = new SequenceTableSite();
			objsiteseq.setSequencecode(sequence);
			objsiteseq.setSitesequence((long) 0);
			objsiteseq.setSitecode(objInv.getNsitecode());
			sequencetablesiteRepository.save(objsiteseq);

			if (seqorder.getSequencetablesite() != null) {
				seqorder.getSequencetablesite().add(objsiteseq);
			} else {
				List<SequenceTableSite> lstseq = new ArrayList<SequenceTableSite>();
				lstseq.add(objsiteseq);
				seqorder.setSequencetablesite(lstseq);
			}
		}

		return seqorder;
	}

	public Elnmaterial GetSequences(Elnmaterial objInv, SequenceTable seqorder, SequenceTableProjectLevel objprojectseq,
			SequenceTableTaskLevel objtaskseq) throws ParseException {
		SequenceTable sqa = seqorder;

		if (sqa != null) {
			objInv.setApplicationsequence(sqa.getApplicationsequence() + 1);

			if (objInv != null && objInv.getNsitecode() != null) {
				SequenceTableSite sqsite = sqa.getSequencetablesite().stream()
						.filter(sq -> sq.getSitecode().equals(objInv.getNsitecode())
								&& sq.getSequencecode().equals(sqa.getSequencecode()))
						.findFirst().orElse(null);
				if (sqsite != null) {
					objInv.setSitesequence(sqsite.getSitesequence() + 1);
				}
			}

			String sequence = objInv.getSequenceid();
			String sequencetext = sequence;
			if (sequence.contains("{s&") && sequence.contains("$s}")) {
				sequencetext = sequence.substring(sequence.indexOf("{s&") + 3, sequence.indexOf("$s}"));
				String replacedseq = "";
				if (sqa.getSequenceview().equals(2) && objInv.getApplicationsequence() != null
						&& !sequencetext.equals("")) {
					replacedseq = String.format("%0" + sequencetext.length() + "d", objInv.getApplicationsequence());
				} else if (sqa.getSequenceview().equals(3) && objInv.getSitesequence() != null
						&& !sequencetext.equals("")) {
					replacedseq = String.format("%0" + sequencetext.length() + "d", objInv.getSitesequence());

				} else if (!sequencetext.equals("") && objInv.getApplicationsequence() != null) {
					replacedseq = String.format("%0" + sequencetext.length() + "d", objInv.getApplicationsequence());
				}

				if (!sequencetext.equals("") && !replacedseq.equals("")) {
					sequencetext = sequence.substring(0, sequence.indexOf("{s&")) + replacedseq
							+ sequence.substring(sequence.indexOf("$s}") + 3, sequence.length());
				}
			}

			sequencetext = commonfunction.Updatedatesinsequence(sequence, sequencetext);

			objInv.setSequenceid(sequencetext);
		}
		return objInv;
	}

	public void updatesequencefordefault(List<Elnmaterial> objInv) {

		String seqid = "Mat_" + objInv.get(0).getNmaterialcode();
		objInv.get(0).setSequenceid(seqid);
		elnmaterialRepository.save(objInv.get(0));
	}

	public void updatesequence(Integer sequenceno, Elnmaterial objInv) {

		if (objInv.getApplicationsequence() != null) {
			sequencetableRepository.setinitialapplicationsequence(objInv.getApplicationsequence(), sequenceno);
		}

		if (objInv.getSitesequence() != null) {
			sequencetablesiteRepository.setinitialsitesequence(objInv.getSitesequence(), sequenceno,
					objInv.getNsitecode());
		}
	}

	public ResponseEntity<Object> createElnMaterial(Elnmaterial obj) throws ParseException, JsonProcessingException {

		SequenceTable seqobj = sequenceTableRepository.findBySequencecodeOrderBySequencecode(3);
		Boolean Applicationseq = seqobj.getSequenceview().equals(1) ? true : false;

		List<Elnmaterial> matlist = new ArrayList<Elnmaterial>();

		Elnmaterial objElnmaterial = elnmaterialRepository.findByNsitecodeAndSmaterialnameIgnoreCaseAndMaterialcategory(
				obj.getNsitecode(), obj.getSmaterialname(), obj.getMaterialcategory());
		SequenceTableProjectLevel objprojectseq = new SequenceTableProjectLevel();
		SequenceTableTaskLevel objtaskseq = new SequenceTableTaskLevel();
		SequenceTable seqorder = null;

		if (!Applicationseq)
			seqorder = validateandupdatematerialsequencenumber(obj, objprojectseq, objtaskseq);

		obj.setResponse(new Response());

		LSuserMaster objMaster = new LSuserMaster();
		objMaster.setUsercode(obj.getObjsilentaudit().getLsuserMaster());

		if (objElnmaterial == null) {

			obj.setCreateby(objMaster);
			obj.setCreateddate(commonfunction.getCurrentUtcTime());

			if (!Applicationseq)
				GetSequences(obj, seqorder, objprojectseq, objtaskseq);

			if (obj.getMaterialprojecthistory() != null) {
				obj.getMaterialprojecthistory().forEach(history -> {
					try {
						history.setCreateddate(commonfunction.getCurrentUtcTime());
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
			}
			materialprojecthistoryrepository.save(obj.getMaterialprojecthistory());
			if (obj.getMaterialprojectmap() != null) {
				materialprojectmapRepository.save(obj.getMaterialprojectmap());
			}
			matlist.add(elnmaterialRepository.save(obj));

			if (Applicationseq)
				updatesequencefordefault(matlist);
			if (!Applicationseq)
				updatesequence(3, obj);

			obj.getResponse().setInformation("IDS_SAVE_SUCCEED");
			obj.getResponse().setStatus(true);

			return new ResponseEntity<>(obj, HttpStatus.OK);
		} else {
			obj.getResponse().setInformation("IDS_SAVE_FAIL");
			obj.getResponse().setStatus(false);
			return new ResponseEntity<>(obj, HttpStatus.OK);
		}
	}

	public ResponseEntity<Object> updateElnMaterial(Elnmaterial obj) throws ParseException, JsonProcessingException {

		Elnmaterial objElnmaterial = elnmaterialRepository
				.findByNsitecodeAndSmaterialnameAndMaterialcategoryAndNmaterialcodeNot(obj.getNsitecode(),
						obj.getSmaterialname(), obj.getMaterialcategory(), obj.getNmaterialcode());

		obj.setResponse(new Response());
		Elnmaterial objMaterial = elnmaterialRepository.findOne(obj.getNmaterialcode());

		if (objElnmaterial == null) {

			objMaterial.setAssignedproject(obj.getAssignedproject());
			objMaterial.setElnmaterialchemdiagref(obj.getElnmaterialchemdiagref());
			objMaterial.setModifiedby(obj.getModifiedby());
			objMaterial.setModifieddate(obj.getModifieddate());
			objMaterial.setJsondata(obj.getJsondata());
			objMaterial.setExpirytype(obj.getExpirytype());
			objMaterial.setOpenexpiry(obj.getOpenexpiry());
			objMaterial.setOpenexpiryperiod(obj.getOpenexpiryperiod());
			objMaterial.setOpenexpiryvalue(obj.getOpenexpiryvalue());
			objMaterial.setQuarantine(obj.getQuarantine());
			objMaterial.setUsageoption(obj.getUsageoption());
			objMaterial.setTrackconsumption(obj.getTrackconsumption());
			objMaterial.setReusable(obj.getReusable());
			objMaterial.setReusabletype(obj.getReusabletype());
			if (obj.getMaterialprojectmap() != null && obj.getMaterialprojectmap().size() > 0) {
				objMaterial.setMaterialprojectmap(obj.getMaterialprojectmap());
				materialprojectmapRepository.save(objMaterial.getMaterialprojectmap());
			}
			if (obj.getMaterialprojecthistory() != null) {
				obj.getMaterialprojecthistory().forEach(history -> {
					try {
						history.setCreateddate(commonfunction.getCurrentUtcTime());
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
				materialprojecthistoryrepository.save(obj.getMaterialprojecthistory());
			}

			elnmaterialRepository.save(objMaterial);
			obj.getResponse().setInformation("IDS_SAVE_SUCCEED");
			obj.getResponse().setStatus(true);

			return new ResponseEntity<>(obj, HttpStatus.OK);
		} else {
			obj.getResponse().setInformation("IDS_SAVE_FAIL");
			obj.getResponse().setStatus(false);
			return new ResponseEntity<>(obj, HttpStatus.OK);
		}
	}

	@SuppressWarnings("unchecked")
	public ResponseEntity<Object> getMaterialDetails(Map<String, Object> inputMap)
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objmapper = new ObjectMapper();
		Map<String, Object> objmap = new LinkedHashMap<String, Object>();

		final Material objMatDetails = materialRepository.findByNmaterialcode((Integer) inputMap.get("nmaterialcode"));

		Map<String, Object> objMaterial = objmapper.readValue(objMatDetails.getJsonuidata(),
				new TypeReference<Map<String, Object>>() {
				});

		objMaterial.put("nmaterialcode", objMatDetails.getNmaterialcode());
		objMaterial.put("nstatus", objMatDetails.getNstatus());
		objMaterial.put("nmaterialtypecode", objMatDetails.getNmaterialtypecode());

		objmap.put("SelectedMaterial", objMaterial);
		objmap.put("nmaterialcode", inputMap.get("nmaterialcode"));
		objmap.put("nstatus", objMatDetails.getNstatus());
		objmap.putAll((Map<String, Object>) getMaterialcombo(objMatDetails.getNmaterialtypecode(),
				(int) inputMap.get("nsitecode")).getBody());

		List<MaterialConfig> lstMaterialConfig = materialConfigRepository
				.findByNmaterialtypecodeAndNformcode(objMatDetails.getNmaterialtypecode(), 40);
		objmap.put("selectedTemplate", lstMaterialConfig);
		objmap.put("DesignMappedFeilds",
				getTemplateDesignForMaterial(objMatDetails
						.getNmaterialtypecode() == Enumeration.TransactionStatus.STANDARDTYPE.gettransactionstatus() ? 1
								: objMatDetails.getNmaterialtypecode() == Enumeration.TransactionStatus.VOLUMETRICTYPE
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

		Material objMaterial = materialRepository.findByNstatusAndNmaterialcode(1,
				(Integer) inputMap.get("nmaterialcode"));
		int countMaterial = materialRepository.countByNmaterialcodeAndNstatus((Integer) inputMap.get("nmaterialcode"),
				1);

		int countMaterialInvent = materialInventoryRepository
				.countByNmaterialcodeAndNstatus(objMaterial.getNmaterialcode(), 1);

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

		final Elnmaterial objMatDetails = elnmaterialRepository.findByNstatusAndNmaterialcode(1,
				(Integer) inputMap.get("nmaterialcode"));

		if (objMatDetails != null) {
			Map<String, Object> objMaterial = new ObjectMapper().readValue(objMatDetails.getJsondata(), Map.class);

			objMaterial.put("nmaterialcode", objMatDetails.getNmaterialcode());

			lstMaterial.add(objMaterial);

			objmap.put("SelectedMaterial", objMatDetails);
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
				List<Material> lstcheckPrefix = materialRepository.findByNmaterialtypecodeAndSprefix(
						(Integer) inputMap.get("nmaterialtypecode"), (String) jsonObject.get("Prefix"));

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

		/**
		 * Added for validate inventory expiry Start
		 */
		Map<String, Object> objMaterialProps = (Map<String, Object>) inputMap.get("Material");

		Boolean expiryPolicy = objMaterialProps.get("Expiry Validations") == null ? false
				: objMaterialProps.get("Expiry Validations").toString().equalsIgnoreCase("Expiry policy") ? true
						: false;
		Boolean openExpiry = objMaterialProps.get("Open Expiry Need") == null ? false
				: objMaterialProps.get("Open Expiry Need").toString().equalsIgnoreCase("3") ? true : false;
		Boolean nextValidation = objMaterialProps.get("Next Validation Need") == null ? false
				: objMaterialProps.get("Next Validation Need").toString().equalsIgnoreCase("3") ? true : false;

		String expiryValue = objMaterialProps.get("Expiry Policy Days") == null ? ""
				: objMaterialProps.get("Expiry Policy Days").toString();
		String openValue = objMaterialProps.get("Open Expiry") == null ? ""
				: objMaterialProps.get("Open Expiry").toString();
		String nextValValue = objMaterialProps.get("Next Validation") == null ? ""
				: objMaterialProps.get("Next Validation").toString();

		Map<String, Object> expiryPeriodMap = new HashMap<>();
		Map<String, Object> openPeriodMap = new HashMap<>();
		Map<String, Object> nextPeriodMap = new HashMap<>();

		String expiryPeriod = "";
		String openPeriod = "";
		String nextPeriod = "";

		if (expiryPolicy) {
			expiryPeriodMap = (Map<String, Object>) objMaterialProps.get("Expiry Policy Period");
			expiryPeriod = expiryPeriodMap.get("label").toString();
		}
		if (openExpiry) {
			openPeriodMap = (Map<String, Object>) objMaterialProps.get("Open Expiry Period");
			openPeriod = openPeriodMap.get("label").toString();
		}
		if (nextValidation) {
			nextPeriodMap = (Map<String, Object>) objMaterialProps.get("Next Validation Period");
			nextPeriod = nextPeriodMap.get("label").toString();
		}

		objMaterial.setExpirypolicy(expiryPolicy);
		objMaterial.setExpirypolicyvalue(expiryValue);
		objMaterial.setExpirypolicyperiod(expiryPeriod);

		objMaterial.setOpenexpiry(openExpiry);
		objMaterial.setOpenexpiryvalue(openValue);
		objMaterial.setOpenexpiryperiod(openPeriod);

		objMaterial.setNextvalidation(nextValidation);
		objMaterial.setNextvalidationvalue(nextValValue);
		objMaterial.setNextvalidationperiod(nextPeriod);

		/**
		 * End
		 */

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
		MaterialCategory objMaterialCategory = materialCategoryRepository
				.findByNmaterialcatcode((Integer) inputMap.get("nmaterialcatcode"));
		objmap.put("SelectedMaterialCategory", objMaterialCategory);
		return getMaterialByTypeCode(inputMap);
//		return new ResponseEntity<>(getMaterialByTypeCode(inputMap), HttpStatus.OK);
	}

	public Elnmaterial CloudUploadattachments(MultipartFile file, Integer nmaterialcatcode, String filename,
			String fileexe, Integer usercode, Date currentdate, Integer isMultitenant) throws IOException {
		Elnmaterial objmaterial = elnmaterialRepository.findOne(nmaterialcatcode);
		LsOrderattachments objattachment = new LsOrderattachments();
		if (isMultitenant == 0) {
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
		if (isMultitenant != 0) {
			String filenameval = "attach_" + objmaterial.getNmaterialcode() + "_"
					+ objmaterial.getLsOrderattachments()
							.get(objmaterial.getLsOrderattachments().lastIndexOf(objattachment)).getAttachmentcode()
					+ "_" + filename;
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
		List<LsOrderattachments> lstattach = lsOrderattachmentsRepository
				.findByNmaterialcodeOrderByAttachmentcodeDesc(objMap.get("nmaterialcode"));
		obj.put("lsOrderattachments", lstattach);
		return obj;

	}

	public Material cloudUploadFilesWithTags(MultipartFile file, Integer nmaterialcatcode, String filename,
			String fileexe, Integer usercode, Date currentdate, Integer isMultitenant) throws IOException {
		Material objmaterial = materialRepository.findOne(nmaterialcatcode);
		LsOrderattachments objattachment = new LsOrderattachments();

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
		if (isMultitenant != 0) {
			String filenameval = "attach_" + objmaterial.getNmaterialcode() + "_"
					+ objmaterial.getLsOrderattachments()
							.get(objmaterial.getLsOrderattachments().lastIndexOf(objattachment)).getAttachmentcode()
					+ "_" + filename;
			String id = cloudFileManipulationservice.storeFileWithTags(filenameval, file,
					"Material_" + nmaterialcatcode);
			if (id != null) {
				objattachment.setFileid(id);
			}

			lsOrderattachmentsRepository.save(objmaterial.getLsOrderattachments());
		}

		return objmaterial;
	}

	public ResponseEntity<Object> getElnMaterial(Map<String, Object> inputMap) throws ParseException {

		Map<String, Object> objmap = new LinkedHashMap<String, Object>();
		Integer nsiteInteger = (Integer) inputMap.get("nsitecode");
		if (inputMap.get("fromdate") != null) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

			Date fromDate = simpleDateFormat.parse((String) inputMap.get("fromdate"));
			Date toDate = simpleDateFormat.parse((String) inputMap.get("todate"));

			List<Elnmaterial> lstElnmaterials = elnmaterialRepository
					.findByNsitecodeAndCreateddateBetweenOrderByNmaterialcodeDesc(nsiteInteger, fromDate, toDate);
			objmap.put("lstMaterial", lstElnmaterials);
		} else {
			List<Elnmaterial> lstElnmaterials = elnmaterialRepository
					.findByNsitecodeOrderByNmaterialcodeDesc(nsiteInteger);
			objmap.put("lstMaterial", lstElnmaterials);
		}

		objmap.put("objsilentaudit", inputMap.get("objsilentaudit"));

		return new ResponseEntity<>(objmap, HttpStatus.OK);
	}

	public ResponseEntity<Object> getElnMaterialByFilter(Map<String, Object> inputMap) throws ParseException {

		Map<String, Object> objmap = new LinkedHashMap<String, Object>();
		ObjectMapper objmapper = new ObjectMapper();

		long longFromValue = Long.parseLong(inputMap.get("fromdate").toString());
		long longToValue = Long.parseLong(inputMap.get("todate").toString());

		Integer nsiteInteger = (Integer) inputMap.get("nsitecode");
		Date fromDate = new Date(longFromValue);
		Date toDate = new Date(longToValue);

		MaterialType objMaterialType = objmapper.convertValue(inputMap.get("materialtype"), MaterialType.class);
		MaterialCategory objMaterialCategory = objmapper.convertValue(inputMap.get("materialcategory"),
				MaterialCategory.class);

		List<Elnmaterial> lstElnmaterials = new ArrayList<>();

		if ((objMaterialType == null || objMaterialType.getNmaterialtypecode() == null
				|| objMaterialType.getNmaterialtypecode() == -1)
				&& (objMaterialCategory == null || objMaterialCategory.getNmaterialcatcode() == null
						|| objMaterialCategory.getNmaterialcatcode() == -1)) {
			lstElnmaterials = elnmaterialRepository
					.findByNsitecodeAndCreateddateBetweenOrderByNmaterialcodeDesc(nsiteInteger, fromDate, toDate);
		} else if ((objMaterialType == null || objMaterialType.getNmaterialtypecode() == null
				|| objMaterialType.getNmaterialtypecode() == -1) && objMaterialCategory != null
				&& objMaterialCategory.getNmaterialcatcode() != -1) {
			lstElnmaterials = elnmaterialRepository
					.findByMaterialcategoryAndNsitecodeAndCreateddateBetweenOrderByNmaterialcodeDesc(
							objMaterialCategory, nsiteInteger, fromDate, toDate);
		} else if (objMaterialType != null && objMaterialType.getNmaterialtypecode() != -1
				&& (objMaterialCategory == null || objMaterialCategory.getNmaterialcatcode() == null
						|| objMaterialCategory.getNmaterialcatcode() == -1)) {
			lstElnmaterials = elnmaterialRepository
					.findByMaterialtypeAndNsitecodeAndCreateddateBetweenOrderByNmaterialcodeDesc(objMaterialType,
							nsiteInteger, fromDate, toDate);
		} else {
			lstElnmaterials = elnmaterialRepository
					.findByMaterialtypeAndMaterialcategoryAndNsitecodeAndCreateddateBetweenOrderByNmaterialcodeDesc(
							objMaterialType, objMaterialCategory, nsiteInteger, fromDate, toDate);
		}

		objmap.put("lstMaterial", lstElnmaterials);
		objmap.put("objsilentaudit", inputMap.get("objsilentaudit"));
		return new ResponseEntity<>(objmap, HttpStatus.OK);
	}

	public ResponseEntity<Object> getMaterialProps(Integer nsiteInteger) {

		Map<String, Object> objmap = new LinkedHashMap<String, Object>();

		List<MaterialType> lstMaterialTypes = new ArrayList<MaterialType>();
		List<MaterialCategory> lstCategories = new ArrayList<MaterialCategory>();
		List<Elnmaterial> lstElnmaterials = new ArrayList<Elnmaterial>();

		lstMaterialTypes = materialTypeRepository
				.findByNmaterialtypecodeNotAndNstatusAndNsitecodeOrNmaterialtypecodeNotAndNstatusAndNdefaultstatusOrderByNmaterialtypecodeDesc(
						-1, 1, nsiteInteger, -1, 1, 4);
		if (!lstMaterialTypes.isEmpty()) {
			lstCategories = materialCategoryRepository
					.findByNmaterialtypecodeAndNsitecodeAndNstatusOrderByNmaterialcatcodeDesc(
							lstMaterialTypes.get(0).getNmaterialtypecode(), nsiteInteger, 1);
			if (!lstCategories.isEmpty()) {
				lstElnmaterials = elnmaterialRepository
						.findByMaterialcategoryAndNsitecodeAndNstatusOrderByNmaterialcodeDesc(lstCategories.get(0),
								nsiteInteger, 1);
			}
		}

		List<MaterialType> lstMaterialTypesFilter = materialTypeRepository
				.findByNsitecodeOrderByNmaterialtypecodeDesc(nsiteInteger);
		List<MaterialCategory> lstCategoriesFilter = materialCategoryRepository
				.findByNsitecodeOrderByNmaterialcatcodeDesc(nsiteInteger);

		objmap.put("lstMaterial", lstElnmaterials);
		objmap.put("lstCategories", lstCategories);
		objmap.put("lstType", lstMaterialTypes);
		objmap.put("lstCategoriesFilter", lstCategoriesFilter);
		objmap.put("lstTypeFilter", lstMaterialTypesFilter);

		List<Unit> lstUnits = unitRepository.findByNsitecodeAndNstatusOrderByNunitcodeDesc(nsiteInteger, 1);
		List<Section> lstSec = sectionRepository.findByNsitecodeAndNstatusOrderByNsectioncodeDesc(nsiteInteger, 1);
		List<Period> lstPeriods = periodRepository.findByNstatusOrderByNperiodcode(1);

		objmap.put("lstUnit", lstUnits);
		objmap.put("lstSection", lstSec);
		objmap.put("lstPeriods", lstPeriods);

		return new ResponseEntity<>(objmap, HttpStatus.OK);
	}

	public ResponseEntity<Object> getMaterialTypeBasedCat(Map<String, Object> inputMap) {
		Map<String, Object> objmap = new LinkedHashMap<String, Object>();

		Integer nsiteInteger = (Integer) inputMap.get("nsitecode");
		Integer ntypecode = (Integer) inputMap.get("ntypecode");

		List<MaterialType> lstMaterialTypes = materialTypeRepository.findByNmaterialtypecode(ntypecode);
		List<MaterialCategory> lstCategories = new ArrayList<MaterialCategory>();
		List<Elnmaterial> lstElnmaterials = new ArrayList<Elnmaterial>();

		if (ntypecode == -1) {
			lstCategories = materialCategoryRepository
					.findByNsitecodeAndNstatusOrNstatusAndNdefaultstatusOrderByNmaterialcatcodeDesc(nsiteInteger, 1, 1,
							3);

			if (!lstCategories.isEmpty()) {
				lstElnmaterials = elnmaterialRepository
						.findByMaterialcategoryAndNsitecodeOrderByNmaterialcodeDesc(lstCategories.get(0), nsiteInteger);
			}
		} else {
			if (!lstMaterialTypes.isEmpty()) {
				lstCategories = materialCategoryRepository
						.findByNmaterialtypecodeAndNsitecodeAndNstatusOrNmaterialtypecodeAndNstatusAndNdefaultstatusOrderByNmaterialcatcodeDesc(
								lstMaterialTypes.get(0).getNmaterialtypecode(), nsiteInteger, 1,
								lstMaterialTypes.get(0).getNmaterialtypecode(), 1, 3);

				if (!lstCategories.isEmpty()) {
					lstElnmaterials = elnmaterialRepository.findByMaterialcategoryAndNsitecodeOrderByNmaterialcodeDesc(
							lstCategories.get(0), nsiteInteger);
				}
			}
		}

		objmap.put("lstCategories", lstCategories);
		objmap.put("lstMaterial", lstElnmaterials);
		return new ResponseEntity<>(objmap, HttpStatus.OK);
	}

	public ResponseEntity<Object> getELNMaterialBySearchField(Map<String, Object> inputMap) {

		Map<String, Object> objmap = new LinkedHashMap<String, Object>();
		Integer nsiteInteger = new ObjectMapper().convertValue(inputMap.get("nsitecode"), Integer.class);
		String searchString = (String) inputMap.get("searchString");

		List<Elnmaterial> lstMaterial = elnmaterialRepository
				.findBySmaterialnameStartingWithIgnoreCaseAndNsitecode(searchString, nsiteInteger);

		objmap.put("lstMaterial", lstMaterial);
		return new ResponseEntity<>(objmap, HttpStatus.OK);
	}

	public ResponseEntity<Object> getElnMaterialOnProtocol(ElnresultUsedMaterial inputMap) throws ParseException {

		Map<String, Object> objmap = new LinkedHashMap<String, Object>();
		Integer ScreenType = Integer.parseInt(inputMap.getCustomobject().get("ScreenType").toString());

		Integer nsiteInteger = (Integer) inputMap.getCustomobject().get("nsitecode");
		List<Elnmaterial> lstMaterial = new ArrayList<>();
		Boolean isFilter = (Boolean) inputMap.getCustomobject().get("isFilter");
		ObjectMapper obj = new ObjectMapper();
		if (!isFilter) {

			if (ScreenType != 1) {
				LSprojectmaster project = null;
				if (inputMap.getCustomobject().get("project") != null) {
					Object projectData = inputMap.getCustomobject().get("project");
					project = !projectData.toString().isEmpty() ? obj.convertValue(projectData, LSprojectmaster.class)
							: null;

				}
				if (ScreenType == 2) {
					if (project != null) {
						lstMaterial = elnmaterialRepository.findElnmaterialsWithProjectMaps(nsiteInteger,
								inputMap.getFromdate(), inputMap.getTodate(), project);
					}
				} else {
					if (project != null) {
						lstMaterial = elnmaterialRepository.findElnmaterialsWithProjectMaps(nsiteInteger,
								inputMap.getFromdate(), inputMap.getTodate(), project);
					}
						ScreenType = 1;
				}
			}
			if (ScreenType == 1) {
				lstMaterial.addAll(elnmaterialRepository.findElnmaterialsWithoutProjectMaps(nsiteInteger,
						inputMap.getFromdate(), inputMap.getTodate()));

			}
		} else {
			Object materialtype = inputMap.getCustomobject().get("materialtype");
			MaterialType objmaterialtype = obj.convertValue(materialtype, new TypeReference<MaterialType>() {
			});
			Object materialcategory = inputMap.getCustomobject().get("materialcategory");
			MaterialCategory objMaterialCategory = obj.convertValue(materialcategory,
					new TypeReference<MaterialCategory>() {
					});

			if (ScreenType != 1) {

				LSprojectmaster project = null;
				if (inputMap.getCustomobject().get("project") != null) {
					Object projectData = inputMap.getCustomobject().get("project");
					project = !projectData.toString().isEmpty() ? obj.convertValue(projectData, LSprojectmaster.class)
							: null;

				}
				if (objmaterialtype.getNmaterialtypecode() != null
						&& objMaterialCategory.getNmaterialcatcode() != null) {

					if (ScreenType == 2) {
						if (project != null) {
							lstMaterial = elnmaterialRepository.findElnmaterialsWithProjectMapsforfilter(nsiteInteger,
									inputMap.getFromdate(), inputMap.getTodate(), project, objmaterialtype,
									objMaterialCategory);
						}

					} else {

						ScreenType = 1;
						if (project != null) {
							lstMaterial.addAll(elnmaterialRepository.findElnmaterialsWithProjectMapsforfilter(
									nsiteInteger, inputMap.getFromdate(), inputMap.getTodate(), project,
									objmaterialtype, objMaterialCategory));
						}
					}
				} else if (objmaterialtype.getNmaterialtypecode() != null) {
					if (ScreenType == 2) {
						if (project != null) {
							lstMaterial = elnmaterialRepository.findElnmaterialsWithProjectMapsforfilterformattype(
									nsiteInteger, inputMap.getFromdate(), inputMap.getTodate(), project,
									objmaterialtype);
						}

					} else {

						ScreenType = 1;
						if (project != null) {
							lstMaterial.addAll(elnmaterialRepository.findElnmaterialsWithProjectMapsforfilterformattype(
									nsiteInteger, inputMap.getFromdate(), inputMap.getTodate(), project,
									objmaterialtype));
						}
					}
				} else {
					if (ScreenType == 2) {
						if (project != null) {
							lstMaterial = elnmaterialRepository.findElnmaterialsWithProjectMapsforfilterformatcat(
									nsiteInteger, inputMap.getFromdate(), inputMap.getTodate(), project,
									objMaterialCategory);
						}

					} else {

						ScreenType = 1;
						if (project != null) {
							lstMaterial.addAll(elnmaterialRepository.findElnmaterialsWithProjectMapsforfilterformatcat(
									nsiteInteger, inputMap.getFromdate(), inputMap.getTodate(), project,
									objMaterialCategory));
						}

					}
				}

			}

			if (ScreenType == 1) {

				if (objmaterialtype.getNmaterialtypecode() != null
						&& objMaterialCategory.getNmaterialcatcode() != null) {
					lstMaterial.addAll(elnmaterialRepository.findElnmaterialsWithoutProjectMapsforfilter(nsiteInteger,
							inputMap.getFromdate(), inputMap.getTodate(), objmaterialtype, objMaterialCategory));
				} else if (objmaterialtype.getNmaterialtypecode() != null) {
					lstMaterial.addAll(elnmaterialRepository.findElnmaterialsWithoutProjectMapsforfilterformaterialtype(
							nsiteInteger, inputMap.getFromdate(), inputMap.getTodate(), objmaterialtype));
				} else {
					lstMaterial.addAll(elnmaterialRepository.findElnmaterialsWithoutProjectMapsforfilterforcat(
							nsiteInteger, inputMap.getFromdate(), inputMap.getTodate(), objMaterialCategory));
				}

			} else {

			}
		}

		objmap.put("lstMaterial", lstMaterial);
		return new ResponseEntity<>(objmap, HttpStatus.OK);
	}

	public ResponseEntity<Object> getELNMaterialPropsForFilter(Integer nsiteInteger) {

		Map<String, Object> objmap = new LinkedHashMap<String, Object>();

		List<MaterialType> lstMaterialTypes = new ArrayList<MaterialType>();
		List<MaterialCategory> lstCategories = new ArrayList<MaterialCategory>();
		List<Elnmaterial> lstElnmaterials = new ArrayList<Elnmaterial>();

		lstMaterialTypes = materialTypeRepository
				.findByNmaterialtypecodeNotAndNstatusAndNsitecodeOrNmaterialtypecodeNotAndNstatusAndNdefaultstatus(-1,
						1, nsiteInteger, -1, 1, 4);
		if (!lstMaterialTypes.isEmpty()) {
			lstCategories = materialCategoryRepository
					.findByNsitecodeAndNstatusOrNstatusAndNdefaultstatusOrderByNmaterialcatcodeDesc(nsiteInteger, 1, 1,
							3);
			if (!lstCategories.isEmpty()) {
				lstElnmaterials = elnmaterialRepository.findByNsitecodeAndNstatusOrderByNmaterialcodeDesc(nsiteInteger,
						1);
			}
		}

		objmap.put("lstMaterial", lstElnmaterials);
		objmap.put("lstCategories", lstCategories);
		objmap.put("lstType", lstMaterialTypes);

		List<Unit> lstUnits = unitRepository.findByNsitecodeAndNstatusOrderByNunitcodeDesc(nsiteInteger, 1);
		List<Section> lstSec = sectionRepository.findByNsitecodeAndNstatusOrderByNsectioncodeDesc(nsiteInteger, 1);
		List<Period> lstPeriods = periodRepository.findByNstatusOrderByNperiodcode(1);

		objmap.put("lstUnit", lstUnits);
		objmap.put("lstSection", lstSec);
		objmap.put("lstPeriods", lstPeriods);

		List<EquipmentType> lstEquipmentTypes = equipmentTypeRepository
				.findByNequipmenttypecodeNotAndNstatusAndNsitecodeOrderByNequipmenttypecodeDesc(-1, 1, nsiteInteger);
		List<EquipmentCategory> lstEquipmentCategories = equipmentCategoryRepository
				.findByNsitecodeAndNstatus(nsiteInteger, 1);

		objmap.put("lstEquipmentTypes", lstEquipmentTypes);
		objmap.put("lstEquipmentCategories", lstEquipmentCategories);

		return new ResponseEntity<>(objmap, HttpStatus.OK);
	}

	public ResponseEntity<Object> getELNPropsAll(Integer nsiteInteger) {

		Map<String, Object> objmap = new LinkedHashMap<String, Object>();

		List<MaterialType> lstMaterialTypes = new ArrayList<MaterialType>();
		List<MaterialCategory> lstCategories = new ArrayList<MaterialCategory>();

		lstMaterialTypes = materialTypeRepository
				.findByNmaterialtypecodeNotAndNstatusAndNsitecodeOrNmaterialtypecodeNotAndNstatusAndNdefaultstatus(-1,
						1, nsiteInteger, -1, 1, 4);
		lstCategories = materialCategoryRepository.findByNsitecodeAndNstatusOrderByNmaterialcatcodeDesc(nsiteInteger,
				1);
		List<Unit> lstUnits = unitRepository.findByNsitecodeAndNstatusOrderByNunitcodeDesc(nsiteInteger, 1);

		objmap.put("lstCategories", lstCategories);
		objmap.put("lstType", lstMaterialTypes);
		objmap.put("lstUnit", lstUnits);

		return new ResponseEntity<>(objmap, HttpStatus.OK);
	}

	public Elnmaterial materialCloudUploadattachments(MultipartFile file, Integer nmaterialtypecode,
			Integer nmaterialcatcode, Integer nmaterialcode, String filename, String fileexe, Integer usercode,
			Date currentdate, Integer isMultitenant, Integer nsitecode) throws IOException {
		Elnmaterial objAttach = elnmaterialRepository.findOne(nmaterialcode);
		MaterialAttachments objattachment = new MaterialAttachments();
		if (isMultitenant == 0) {
			if (fileManipulationservice.storeLargeattachment(filename, file) != null) {
				objattachment.setFileid(fileManipulationservice.storeLargeattachment(filename, file));
			}
		}

		objattachment.setFilename(filename);
		objattachment.setFileextension(fileexe);
		objattachment.setCreateby(lsuserMasterRepository.findByusercode(usercode));
		objattachment.setCreateddate(currentdate);
		objattachment.setNmaterialcode(nmaterialcode);
		objattachment.setNmaterialcatcode(nmaterialcatcode);
		objattachment.setNmaterialtypecode(nmaterialtypecode);
		objattachment.setNstatus(1);
		objattachment.setNsitecode(nsitecode);
		objattachment.setFilesize(commonService.formatFileSize(file.getSize()));

		if (objAttach != null && objAttach.getlsMaterialAttachments() != null) {
			objAttach.getlsMaterialAttachments().add(objattachment);
		} else {
			objAttach.setlsMaterialAttachments(new ArrayList<MaterialAttachments>());
			objAttach.getlsMaterialAttachments().add(objattachment);
		}
		materialAttachmentsRepository.save(objAttach.getlsMaterialAttachments());
		if (isMultitenant != 0) {
			String filenameval = "attach_" + objAttach.getNmaterialcode() + "_" + objAttach.getlsMaterialAttachments()
					.get(objAttach.getlsMaterialAttachments().lastIndexOf(objattachment)).getNmaterialattachcode() + "_"
					+ filename;
			String id = cloudFileManipulationservice.storeLargeattachment(filenameval, file);
			if (id != null) {
				objattachment.setFileid(id);
			}

			materialAttachmentsRepository.save(objAttach.getlsMaterialAttachments());
		}
		List<MaterialAttachments> objFilels = materialAttachmentsRepository.findByNmaterialcode(nmaterialcode);
		objAttach.setlsMaterialAttachments(objFilels);
		return objAttach;
	}

	public Map<String, Object> geMaterialtAttachments(Map<String, Object> inputMap) {
		Map<String, Object> objMap = new HashMap<>();
		int nmaterialcode = (int) inputMap.get("nmaterialcode");
		List<MaterialAttachments> objFilels = materialAttachmentsRepository.findByNmaterialcode(nmaterialcode);
		objMap.put("lsMaterialAttachments", objFilels);
		return objMap;
	}

	public ResponseEntity<InputStreamResource> materialView(String param, String fileid) throws IOException {
		if (param == null) {
			return null;
		}

		MaterialAttachments objFile = materialAttachmentsRepository.findByFileid(fileid);
		SampleAttachments objsFile = SampleAttachementsRepository.findByFileid(fileid);
		HttpHeaders header = new HttpHeaders();

		if (objFile != null) {
			header.set("Content-Disposition", "attachment; filename=" + objFile.getFilename());
		} else {
			header.set("Content-Disposition", "attachment; filename=" + objsFile.getFilename());
		}

		if (Integer.parseInt(param) == 0) {
			CloudOrderAttachment objfile = CloudOrderAttachmentRepository.findByFileid(fileid);
			InputStreamResource resource = null;
			byte[] content = objfile.getFile().getData();
			int size = content.length;
			InputStream is = null;
			try {
				is = new ByteArrayInputStream(content);
				resource = new InputStreamResource(is);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (is != null)
						is.close();
				} catch (Exception ex) {

				}
			}
			header.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			header.setContentLength(size);
			return new ResponseEntity<>(resource, header, HttpStatus.OK);
		} else if (Integer.parseInt(param) == 1) {
			InputStream fileDtream = cloudFileManipulationservice.retrieveLargeFile(fileid);

			InputStreamResource resource = null;
			byte[] content = IOUtils.toByteArray(fileDtream);
			int size = content.length;
			InputStream is = null;
			try {
				is = new ByteArrayInputStream(content);
				resource = new InputStreamResource(is);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (is != null)
						is.close();
				} catch (Exception ex) {

				}
			}

			header.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			header.setContentLength(size);
			return new ResponseEntity<>(resource, header, HttpStatus.OK);
		}
		return null;

	}

	public ResponseEntity<Object> updateAssignedTaskOnMaterial(Elnmaterial material) {

//		Integer selectedMaterial = Integer.parseInt(inputMap.get("selectedMaterial").toString());
//		String task = inputMap.get("task").toString();

		Elnmaterial objElnmaterial = elnmaterialRepository.findOne(material.getNmaterialcode());
		objElnmaterial.setMaterialprojectmap(material.getMaterialprojectmap());
		materialprojectmapRepository.save(objElnmaterial.getMaterialprojectmap());
		elnmaterialRepository.save(objElnmaterial);
		return new ResponseEntity<>(objElnmaterial, HttpStatus.OK);
	}

	public ResponseEntity<Object> getAssignedTaskOnMaterial(Elnmaterial material) {

		List<MaterialProjectMap> assignedproject = materialprojectmapRepository
				.findByNmaterialcodeOrderByMaterialprojectcode(material.getNmaterialcode());
		return new ResponseEntity<>(assignedproject, HttpStatus.OK);
	}

	public ResponseEntity<Object> uploadLinkforMaterial(MaterialLinks materiallink) throws ParseException {
		materiallink.setCreateddate(commonfunction.getCurrentUtcTime());
		materiallinksrepository.save(materiallink);
		return new ResponseEntity<>(materiallink, HttpStatus.OK);
	}

	public ResponseEntity<Object> getLinksOnMaterial(MaterialLinks materiallink) {
		List<MaterialLinks> objFilels = materiallinksrepository.findByNmaterialcode(materiallink.getNmaterialcode());
		return new ResponseEntity<>(objFilels, HttpStatus.OK);
	}

	public ResponseEntity<Object> deleteLinkforMaterial(MaterialLinks materiallink) {
		materiallinksrepository.delete(materiallink);
		return new ResponseEntity<>(materiallink, HttpStatus.OK);
	}

	public ResponseEntity<Object> updatematerialprojecthistory(MaterialProjectHistory[] materiallist) {
		List<MaterialProjectHistory> history = Arrays.asList(materiallist);
		history.forEach(his -> {
			try {
				his.setCreateddate(commonfunction.getCurrentUtcTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		materialprojecthistoryrepository.save(history);
		return new ResponseEntity<>(materiallist, HttpStatus.OK);
	}


	public ResponseEntity<Object> getSampleList(ElnresultUsedSample inputMap) throws ParseException {
		Integer screenType = Integer.parseInt(inputMap.getCustomobject().get("ScreenType").toString());
		Map<String, Object> objMap = new LinkedHashMap<>();
		List<Sample> lstSample = new ArrayList<>();
		Integer siteCode = inputMap.getSitemaster().getSitecode();
		Date fromDate = inputMap.getFromdate();
		Date toDate = inputMap.getTodate();
		Integer transactionStatus = 28;
		Boolean isFilter = (Boolean) inputMap.getCustomobject().get("isFilter");
//		List<Integer> sampleCodes = new ArrayList<>();
		ObjectMapper obj = new ObjectMapper();
		LSprojectmaster project = null;
		if (inputMap.getCustomobject().get("project") != null) {
			Object projectData = inputMap.getCustomobject().get("project");
			project = !projectData.toString().isEmpty() ? obj.convertValue(projectData, LSprojectmaster.class)
					: null;

		}
		if (!isFilter) {
			if (screenType != 1) {
				if (screenType == 2) {
					if(project!=null) {
						lstSample = SampleRepository
								.findElnmaterialsWithProjectMaps(siteCode,
										fromDate, toDate, transactionStatus,project);
					}
				} else {
					screenType = 1;
					if(project!=null) {
						lstSample = SampleRepository
								.findElnmaterialsWithProjectMaps(siteCode,
										fromDate, toDate, transactionStatus,project);
					}
				}
			}
			if (screenType == 1) {
				lstSample = SampleRepository
						.findElnmaterialsWithoutProjectMaps(siteCode,
								fromDate, toDate, transactionStatus);

			}
		} else if (isFilter) {
			ObjectMapper objMaper = new ObjectMapper();
			if (inputMap.getCustomobject().get("sampletype") != null
					&& inputMap.getCustomobject().get("samplecategories") != null) {
				Object sampletype = inputMap.getCustomobject().get("sampletype");
				SampleType objSampleType = objMaper.convertValue(sampletype, new TypeReference<SampleType>() {
				});
				Object samplecat = inputMap.getCustomobject().get("samplecategories");
				SampleCategory objSampleCategory = objMaper.convertValue(samplecat,
						new TypeReference<SampleCategory>() {
						});

				if (screenType != 1) {
					if (screenType == 2) {
						if(project!=null) {
						lstSample = SampleRepository
								.findElnmaterialsWithProjectMapsforfilter(
										siteCode, fromDate, toDate, transactionStatus, project, objSampleType,
										objSampleCategory);
						}
					} else {
						screenType = 1;
						if(project!=null) {
							lstSample.addAll(SampleRepository
									.findElnmaterialsWithProjectMapsforfilter(
											siteCode, fromDate, toDate, transactionStatus, project, objSampleType,
											objSampleCategory));
						}
					}
				}
				if (screenType == 1) {
					lstSample.addAll(SampleRepository
							.findElnmaterialsWithoutProjectMapsforfilter(
									siteCode, fromDate, toDate, transactionStatus, objSampleType, objSampleCategory));
				}

			} else if (inputMap.getCustomobject().get("sampletype") != null) {
				Object sampletype = inputMap.getCustomobject().get("sampletype");
				SampleType objSampleType = objMaper.convertValue(sampletype, new TypeReference<SampleType>() {
				});

				if (screenType != 1) {
					if (screenType == 2) {
						if(project!=null) {
						lstSample = SampleRepository
								.findElnmaterialsWithProjectMapsforfiltersampletype(
										siteCode, fromDate, toDate, transactionStatus, project, objSampleType);
						}
					} else {
						screenType = 1;
						if(project!=null) {

							lstSample.addAll(SampleRepository
									.findElnmaterialsWithProjectMapsforfiltersampletype(
											siteCode, fromDate, toDate, transactionStatus, project, objSampleType));
						}
					}
				}

				if (screenType == 1) {
					lstSample.addAll(SampleRepository
							.findElnmaterialsWithoutProjectMapsforfilterforsampletype(
									siteCode, fromDate, toDate, transactionStatus, objSampleType));

				}

			} else if (inputMap.getCustomobject().get("samplecategories") != null) {
				Object samplecat = inputMap.getCustomobject().get("samplecategories");
				SampleCategory objSampleCategory = objMaper.convertValue(samplecat,
						new TypeReference<SampleCategory>() {
						});

				if (screenType != 1) {
					if (screenType == 2) {
						if(project!=null) {
						lstSample = SampleRepository
								.findElnmaterialsWithProjectMapsforfilterforsamplecat(
										siteCode, fromDate, toDate, transactionStatus, project, objSampleCategory);
						}
					} else {
						screenType = 1;
						if(project!=null) {
							lstSample.addAll(SampleRepository
									.findElnmaterialsWithProjectMapsforfilterforsamplecat(
											siteCode, fromDate, toDate, transactionStatus, project, objSampleCategory));
						}
					}
				}

				if (screenType == 1) {
					lstSample = SampleRepository
							.findElnmaterialsWithoutProjectMapsforfilterforsamplecat(
									siteCode, fromDate, toDate, transactionStatus, objSampleCategory);
				}
			}

		}

		objMap.put("lstSample", lstSample);
		return new ResponseEntity<>(objMap, HttpStatus.OK);
	}

	private List<LSprojectmaster> deserializeProjects(Object projectObj) {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.convertValue(projectObj, new TypeReference<List<LSprojectmaster>>() {
		});
	}

}