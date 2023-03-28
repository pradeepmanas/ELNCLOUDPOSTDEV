package com.agaram.eln.primary.service.material;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
//import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
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
import com.agaram.eln.primary.model.material.MaterialInventory;
import com.agaram.eln.primary.model.material.MaterialInventoryTransaction;
import com.agaram.eln.primary.model.material.MaterialInventoryType;
import com.agaram.eln.primary.model.material.MaterialType;
import com.agaram.eln.primary.model.material.ResultUsedMaterial;
import com.agaram.eln.primary.model.material.TransactionStatus;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.repository.instrumentDetails.LsOrderattachmentsRepository;
import com.agaram.eln.primary.repository.material.MappedTemplateFieldPropsMaterialRepository;
import com.agaram.eln.primary.repository.material.MaterialCategoryRepository;
import com.agaram.eln.primary.repository.material.MaterialConfigRepository;
import com.agaram.eln.primary.repository.material.MaterialInventoryRepository;
import com.agaram.eln.primary.repository.material.MaterialInventoryTransactionRepository;
import com.agaram.eln.primary.repository.material.MaterialInventoryTypeRepository;
import com.agaram.eln.primary.repository.material.MaterialRepository;
import com.agaram.eln.primary.repository.material.MaterialTypeRepository;
import com.agaram.eln.primary.repository.material.ResultUsedMaterialRepository;
import com.agaram.eln.primary.repository.material.TransactionStatusRepository;
import com.agaram.eln.primary.repository.usermanagement.LSuserMasterRepository;
import com.agaram.eln.primary.service.cloudFileManip.CloudFileManipulationservice;
import com.agaram.eln.primary.service.fileManipulation.FileManipulationservice;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class MaterialInventoryService {

	@Autowired
	MaterialRepository materialRepository;
	@Autowired
	MaterialTypeRepository materialTypeRepository;
	@Autowired
	MaterialCategoryRepository materialCategoryRepository;
	@Autowired
	MaterialConfigRepository materialConfigRepository;
	@Autowired
	MaterialInventoryRepository materialInventoryRepository;
	@Autowired
	MappedTemplateFieldPropsMaterialRepository mappedTemplateFieldPropsMaterialRepository;
	@Autowired
	TransactionStatusRepository transactionStatusRepository;
	@Autowired
	MaterialInventoryTypeRepository materialInventoryTypeRepository;
	@Autowired
	MaterialInventoryTransactionRepository materialInventoryTransactionRepository;
	@Autowired
	private ResultUsedMaterialRepository resultUsedMaterialRepository;
	@Autowired
	private LSuserMasterRepository lsuserMasterRepository;
	@Autowired
	private LsOrderattachmentsRepository lsOrderattachmentsRepository;
	@Autowired
	private CloudFileManipulationservice cloudFileManipulationservice;
	@Autowired
	private FileManipulationservice fileManipulationservice;

	@SuppressWarnings("unchecked")
	public ResponseEntity<Object> getMaterialInventory(Integer nsiteInteger) throws Exception {
		Map<String, Object> objmap = new LinkedHashMap<String, Object>();

		List<MaterialType> lstMaterialType = materialTypeRepository
				.findByNmaterialtypecodeNotOrderByNmaterialtypecode(-1);
		objmap.put("MaterialType", lstMaterialType);

		List<MaterialCategory> lstMaterialCategory = materialCategoryRepository
				.findByNmaterialtypecodeAndNsitecode(lstMaterialType.get(0).getNmaterialtypecode(), nsiteInteger);

		List<MaterialType> lstMaterialType1 = new ArrayList<MaterialType>();

		lstMaterialType1.add(lstMaterialType.get(0));

		objmap.put("SelectedMaterialType", lstMaterialType1);

		if (!lstMaterialCategory.isEmpty()) {
			objmap.put("MaterialCategoryMain", lstMaterialCategory);
			objmap.put("nmaterialcatcode", lstMaterialCategory.get(0).getNmaterialcatcode());

			List<Map<String, Object>> lstMatObject = new ArrayList<Map<String, Object>>();
			List<Material> lstMaterial = materialRepository
					.findByNmaterialcatcode(lstMaterialCategory.get(0).getNmaterialcatcode());

			lstMaterial.stream().peek(f -> {

				try {
					Map<String, Object> setObj = new LinkedHashMap<String, Object>();

					Map<String, Object> setUiObj = new LinkedHashMap<String, Object>();

					Map<String, Object> resObj = new ObjectMapper().readValue(f.getJsonuidata(), Map.class);

					setUiObj.put("nmaterialcode", f.getNmaterialcode());
					setUiObj.put("Material Name", resObj.get("Material Name"));

					setObj.put("nmaterialcode", f.getNmaterialcode());
					setObj.put("nstatus", 1);
					setObj.put("jsondata", setUiObj);
					setObj.put("jsonuidata", null);
					setObj.put("smaterialname", null);
					setObj.put("sunitname", null);
					setObj.put("needsection", 0);
					setObj.put("nproductcode", 0);

					lstMatObject.add(setObj);
				} catch (IOException e) {

					e.printStackTrace();
				}

			}).collect(Collectors.toList());

			objmap.put("MaterialCombo", lstMatObject);

			List<MaterialConfig> lstMaterialConfig = materialConfigRepository
					.findByNmaterialtypecodeAndNformcode(lstMaterialType.get(0).getNmaterialtypecode(), 138);

			objmap.put("selectedTemplate", lstMaterialConfig);

			objmap.put("nmaterialtypecode", lstMaterialType.get(0).getNmaterialtypecode());
			objmap.put("nmaterialcatcode", lstMaterialCategory.get(0).getNmaterialcatcode());
			objmap.put("nsitecode", nsiteInteger);
			if (!lstMaterial.isEmpty()) {
				objmap.put("nmaterialcode", lstMaterial.get(0).getNmaterialcode());
			}

			objmap.putAll((Map<String, Object>) getMaterialInventoryByID(objmap).getBody());
		}
		return new ResponseEntity<>(objmap, HttpStatus.OK);
	}

	@SuppressWarnings("unchecked")
	public ResponseEntity<Object> getMaterialInventorycombo(Integer nmaterialtypecode, Integer nmaterialcatcode,
			Integer nsiteInteger) {
		Map<String, Object> objmap = new LinkedHashMap<String, Object>();

		List<MaterialCategory> lstMaterialCategory = new ArrayList<>();

		if (nmaterialcatcode != null) {
			lstMaterialCategory = materialCategoryRepository
					.findByNmaterialtypecodeAndNmaterialcatcodeAndNsitecodeAndNstatusOrderByNmaterialcatcode(
							nmaterialtypecode, nmaterialcatcode, nsiteInteger, 1);
		} else {
			lstMaterialCategory = materialCategoryRepository
					.findByNmaterialtypecodeAndNsitecodeAndNstatusOrderByNmaterialcatcode(nmaterialtypecode,
							nsiteInteger, 1);
		}

		List<Map<String, Object>> lstMatObject = new ArrayList<Map<String, Object>>();

		if (!lstMaterialCategory.isEmpty()) {

			List<Material> lstMaterial = materialRepository.findByNmaterialcatcodeAndNmaterialtypecodeAndNstatus(
					lstMaterialCategory.get(0).getNmaterialcatcode(), nmaterialtypecode, 1);

			lstMaterial.stream().peek(f -> {

				try {
					Map<String, Object> setObj = new LinkedHashMap<String, Object>();

					Map<String, Object> setUiObj = new LinkedHashMap<String, Object>();

					Map<String, Object> resObj = new ObjectMapper().readValue(f.getJsonuidata(), Map.class);

					setUiObj.put("nmaterialcode", f.getNmaterialcode());
					setUiObj.put("Material Name", resObj.get("Material Name"));

					setObj.put("nmaterialcode", f.getNmaterialcode());
					setObj.put("nstatus", 1);
					setObj.put("jsondata", setUiObj);
					setObj.put("jsonuidata", null);
					setObj.put("smaterialname", null);
					setObj.put("sunitname", null);
					setObj.put("needsection", 0);
					setObj.put("nproductcode", 0);

					lstMatObject.add(setObj);
				} catch (IOException e) {

					e.printStackTrace();
				}

			}).collect(Collectors.toList());

			objmap.put("MaterialCombo", lstMatObject);
		} else {
			objmap.put("MaterialCombo", lstMaterialCategory);
		}
		if (nmaterialcatcode == null) {
			objmap.put("MaterialCategoryMain", lstMaterialCategory);
		}
		return new ResponseEntity<>(objmap, HttpStatus.OK);
	}

	@SuppressWarnings("unchecked")
	public ResponseEntity<Object> getMaterialInventoryByID(Map<String, Object> inputMap) throws Exception {

		Map<String, Object> objmap = new HashMap<String, Object>();
		final ObjectMapper objmapper = new ObjectMapper();
		List<Map<String, Object>> lstMaterialInventory = new ArrayList<Map<String, Object>>();

		LScfttransaction cft = new LScfttransaction();
		if (inputMap.containsKey("objsilentaudit")) {
			cft = objmapper.convertValue(inputMap.get("objsilentaudit"), LScfttransaction.class);
		}
		if (inputMap.containsKey("nmaterialcode")) {

			List<MaterialInventory> objLstMaterialInventory = materialInventoryRepository
					.findByNmaterialcodeAndNmaterialcatcodeAndNmaterialtypecodeAndNstatusOrderByNmaterialinventorycodeDesc(
							(Integer) inputMap.get("nmaterialcode"), (Integer) inputMap.get("nmaterialcatcode"),
							(Integer) inputMap.get("nmaterialtypecode"), 1);

			objLstMaterialInventory.stream().peek(f -> {

				try {

					Map<String, Object> resObj = new ObjectMapper().readValue(f.getJsonuidata(), Map.class);

					resObj.put("nmaterialinventorycode", f.getNmaterialinventorycode());
					resObj.put("displaystatus",
							f.getNtransactionstatus() == 28 ? "Released"
									: (f.getNtransactionstatus() == 55 ? "Expired"
											: (f.getNtransactionstatus() == 37 ? "Quarantine" : "Retired")));

					lstMaterialInventory.add(resObj);

				} catch (IOException e) {

					e.printStackTrace();
				}

			}).collect(Collectors.toList());

			objmap.put("MaterialInventory", lstMaterialInventory);

			if (!lstMaterialInventory.isEmpty()) {

				if (!(inputMap.containsKey("nflag"))) {

					objmap.put("SelectedMaterialInventory", lstMaterialInventory.get(0));
					inputMap.put("nsectioncode", lstMaterialInventory.get(0).get("nsectioncode"));

					objmap.putAll((Map<String, Object>) getQuantityTransactionByMaterialInvCode(
							(int) lstMaterialInventory.get(0).get("nmaterialinventorycode"), inputMap).getBody());
//					objmap.putAll((Map<String, Object>) getMaterialFile((int) lstMaterialInventory
//							.get(lstMaterialInventory.size() - 1).get("nmaterialinventorycode")));
					objmap.putAll((Map<String, Object>) getResultUsedMaterial(
							Integer.parseInt(lstMaterialInventory.get(0).get("nmaterialinventorycode").toString()))
									.getBody());
//					objmap.putAll((Map<String, Object>) getMaterialInventoryhistory((int) lstMaterialInventory
//							.get(lstMaterialInventory.size() - 1).get("nmaterialinventorycode")));

				} else {

					MaterialInventory objInventory = materialInventoryRepository
							.findOne((Integer) inputMap.get("nmaterialinventorycode"));

					List<Map<String, Object>> lstMaterialInventory1 = new ArrayList<Map<String, Object>>();

					Map<String, Object> resObj = new ObjectMapper().readValue(objInventory.getJsonuidata(), Map.class);

					resObj.put("nmaterialinventorycode", (Integer) inputMap.get("nmaterialinventorycode"));

					lstMaterialInventory1.add(resObj);

					inputMap.put("nsectioncode", objInventory.getNsectioncode());

					objmap.putAll((Map<String, Object>) getQuantityTransactionByMaterialInvCode(
							(int) lstMaterialInventory1.get(0).get("nmaterialinventorycode"), inputMap).getBody());

					objmap.putAll((Map<String, Object>) getResultUsedMaterial(objInventory.getNmaterialinventorycode())
							.getBody());
//					objmap.putAll((Map<String, Object>) getMaterialFile(
//							(int) lstMaterialInventory1.get(0).get("nmaterialinventorycode")));
//					objmap.putAll((Map<String, Object>) getMaterialInventoryhistory(
//							(int) lstMaterialInventory1.get(0).get("nmaterialinventorycode")));

				}
			} else {
				objmap.put("SelectedMaterialInventory", lstMaterialInventory);
			}
		}

		List<MaterialType> lstMaterialType = materialTypeRepository
				.findByNmaterialtypecodeAndNstatusOrderByNmaterialtypecode((Integer) inputMap.get("nmaterialtypecode"),
						1);

		objmap.put("SelectedMaterialType", lstMaterialType);

		List<MaterialCategory> lstMaterialCategory = new ArrayList<MaterialCategory>();

		if (inputMap.containsKey("nmaterialcatcode")) {

			MaterialCategory objMaterialCategory = materialCategoryRepository
					.findByNmaterialcatcodeAndNstatus((Integer) inputMap.get("nmaterialcatcode"), 1);

			lstMaterialCategory.add(objMaterialCategory);
		} else {
			lstMaterialCategory = materialCategoryRepository
					.findByNmaterialtypecodeAndNstatus((Integer) inputMap.get("nmaterialtypecode"), 1);
		}

		if (!lstMaterialCategory.isEmpty()) {
			if (inputMap.containsKey("nmaterialcatcode")) {
				objmap.put("SelectedMaterialCategory", lstMaterialCategory.get(0));
			} else {
				objmap.put("SelectedMaterialCategory", lstMaterialCategory.get(0));
			}
		}

		if (inputMap.containsKey("nmaterialcode")) {

			Material lstMaterial = materialRepository.findByNstatusAndNmaterialcode(1,
					(Integer) inputMap.get("nmaterialcode"));

			if (lstMaterial != null) {
				objmap.put("SelectedMaterialCrumb", crumObjectMaterialCreated(lstMaterial));
			}
		}
		objmap.putAll(
				(Map<String, Object>) getQuantityTransactionTemplate(138, (Integer) inputMap.get("nmaterialtypecode"))
						.getBody());
		objmap.putAll((Map<String, Object>) getMaterialInventoryAdd((int) inputMap.get("nmaterialtypecode")).getBody());

		List<MaterialConfig> lstConfigs = (List<MaterialConfig>) objmap.get("selectedTemplate");

		Integer configCode = lstConfigs.get(0).getNmaterialconfigcode();

		objmap.put("DesignMappedFeilds", getTemplateDesignForMaterial(configCode, 138));

		List<Integer> lstParams = new ArrayList<Integer>();

		lstParams.add(47);
		lstParams.add(48);

		List<TransactionStatus> lstTransStatus = transactionStatusRepository.findByNstatusAndNtranscodeIn(1, lstParams);

		objmap.put("TransactionType", lstTransStatus);
		lstParams = new ArrayList<Integer>();

		lstParams.add(1);
		lstParams.add(2);

		List<MaterialInventoryType> lstMaterialInventoryType = materialInventoryTypeRepository
				.findByNstatusAndNinventorytypecodeIn(1, lstParams);

		System.out.println(lstMaterialInventoryType);
		objmap.put("MaterialInventoryType", lstMaterialInventoryType);

		objmap.put("DesignMappedFeildsQuantityTransaction", getTemplateDesignForMaterial(9, 138));
		if (cft != null) {
			objmap.put("objsilentaudit", cft);
		}
		return new ResponseEntity<>(objmap, HttpStatus.OK);

	}

	private HttpEntity<Object> getMaterialInventoryAdd(int ntypecode) {
		Map<String, Object> objmap = new HashMap<>();

		List<MaterialConfig> lstMaterialConfig = materialConfigRepository
				.findByNmaterialtypecodeAndNformcodeAndNstatus(ntypecode, 138, 1);

		objmap.put("selectedTemplate", lstMaterialConfig);
		return new ResponseEntity<>(objmap, HttpStatus.OK);

	}

	@SuppressWarnings("unchecked")
	public HttpEntity<Object> getQuantityTransactionByMaterialInvCode(int nmaterialinventorycode,
			Map<String, Object> inputMap) throws JsonParseException, JsonMappingException, IOException {
		Map<String, Object> objmap = new LinkedHashMap<String, Object>();
		List<Map<String, Object>> lstMaterialInventoryTrans = new ArrayList<Map<String, Object>>();

		List<MaterialConfig> lstMaterialConfig = new ArrayList<MaterialConfig>();

		List<MaterialInventoryTransaction> lstInventoryTransaction = materialInventoryTransactionRepository
				.findByNmaterialinventorycodeOrderByNmaterialinventtranscodeDesc((Integer) nmaterialinventorycode);

		lstInventoryTransaction.stream().peek(f -> {

			try {

				Map<String, Object> resObj = new ObjectMapper().readValue(f.getJsonuidata(), Map.class);
				Map<String, Object> objContent = commonfunction.getInventoryValuesFromJsonString(f.getJsonuidata(),
						"namountleft");
				resObj.put("Available Quantity", objContent.get("rtnObj"));
				objContent = commonfunction.getInventoryValuesFromJsonString(f.getJsonuidata(), "nqtyissued");
				resObj.put("Issued Quantity", objContent.get("rtnObj"));
				resObj.put("Received Quantity", f.getNqtyreceived());
				resObj.put("nmaterialinventorycode", f.getNmaterialinventorycode());
				resObj.put("nmaterialinventtranscode", f.getNmaterialinventtranscode());
				resObj.put("issuedby", f.getIssuedbyusercode() != null ? f.getIssuedbyusercode().getUsername() : "-");
				resObj.put("createddate", f.getCreateddate());

				lstMaterialInventoryTrans.add(resObj);

			} catch (IOException e) {

				e.printStackTrace();
			}

		}).collect(Collectors.toList());

		// dont remove
//		MaterialConfig objMaterialConfig = materialConfigRepository.findByNformcodeAndNmaterialtypecodeAndNstatus(138,-1, 1);
//		lstMaterialConfig.add(objMaterialConfig);
//		Collections.sort(lstMaterialInventoryTrans, Collections.reverseOrder());

		objmap.put("QuantityTransactionTemplate", lstMaterialConfig);
		objmap.put("MaterialInventoryTrans", lstMaterialInventoryTrans);
		return new ResponseEntity<>(objmap, HttpStatus.OK);
	}

	public Map<String, Object> getTemplateDesignForMaterial(int nmaterialconfigcode, int nformcode) {

		List<MappedTemplateFieldPropsMaterial> lstMappedTemplate = mappedTemplateFieldPropsMaterialRepository
				.findByNmaterialconfigcode(nmaterialconfigcode);

		Map<String, Object> designObject = new HashMap<String, Object>();

		Map<String, Object> designChildObject = new HashMap<String, Object>();

		designChildObject.put("type", "jsonb");
		designChildObject.put("null", true);
		designChildObject.put("value", lstMappedTemplate.get(0).getJsondata());

		Map<String, Object> objContent = commonfunction
				.getInventoryValuesFromJsonString(lstMappedTemplate.get(0).getJsondata(), "138");

		JSONObject jsonObject = (JSONObject) objContent.get("rtnObj");

		designChildObject.put("value", jsonObject.toString());

		designObject.put("jsondata", designChildObject);

		return designObject;
	}

	public Map<String, Object> crumObjectMaterialCreated(Material objmaterial) {

		Map<String, Object> rtnObj = new HashMap<>();
		Map<String, Object> rtnUnitObj = new HashMap<>();
		Map<String, Object> objContent = new HashMap<>();

		JSONObject jsonUnitObject = (JSONObject) commonfunction
				.getInventoryValuesFromJsonString(objmaterial.getJsondata(), "Basic Unit").get("rtnObj");
		rtnUnitObj.put("label", jsonUnitObject.get("label"));
		rtnUnitObj.put("value", jsonUnitObject.get("value"));

		objContent.put("nmaterialcode", objmaterial.getNmaterialcode());
		objContent.put("nunitcode", rtnUnitObj);
		objContent.put("ntranscode", objmaterial.getNtransactionstatus());
		objContent.put("Material Name", (String) commonfunction
				.getInventoryValuesFromJsonString(objmaterial.getJsondata(), "Material Name").get("rtnObj"));
		objContent.put("isExpiryNeed", (String) commonfunction
				.getInventoryValuesFromJsonString(objmaterial.getJsondata(), "Expiry Validations").get("rtnObj"));
		objContent.put("Next Validation Need", commonfunction
				.getInventoryValuesFromJsonString(objmaterial.getJsondata(), "Next Validation Need").get("rtnObj"));
		objContent.put("Open Expiry Need", commonfunction
				.getInventoryValuesFromJsonString(objmaterial.getJsondata(), "Open Expiry Need").get("rtnObj"));

		if (objmaterial.getNmaterialtypecode() == 3 || objmaterial.getNmaterialtypecode() == 4) {
			Map<String, Object> isReusable = commonfunction.getInventoryValuesFromJsonString(objmaterial.getJsondata(),
					"Reusable");
			objContent.put("isreusable", Integer.parseInt(isReusable.get("rtnObj").toString()) == 3 ? true : false);
		} else {
			objContent.put("isreusable", false);
		}
		objContent.remove("rtnObj");
		rtnObj.put("jsondata", objContent);

		return rtnObj;
	}

	@SuppressWarnings({ "unused", "unchecked" })
	public ResponseEntity<Object> createMaterialInventory(Map<String, Object> inputMap) throws Exception {
		Map<String, Object> objmap = new LinkedHashMap<String, Object>();
		JSONObject actionType = new JSONObject();
		JSONObject jsonAuditObject = new JSONObject();
		JSONArray jsonUidataarray = new JSONArray();
		JSONArray jsonUidataarrayTrans = new JSONArray();
		final ObjectMapper objmapper = new ObjectMapper();
		List<Map<String, Object>> lstAudit = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> lstAudittrans = new ArrayList<Map<String, Object>>();

		final LScfttransaction cft = objmapper.convertValue(inputMap.get("objsilentaudit"), LScfttransaction.class);
		List<String> lstDateField = (List<String>) inputMap.get("DateList");
		JSONObject jsonObject = new JSONObject(inputMap.get("materialInventoryJson").toString());
		JSONObject jsonObjectInvTrans = new JSONObject(inputMap.get("MaterialInventoryTrans").toString());

		JSONObject jsonuidata = new JSONObject(inputMap.get("jsonuidata").toString());
		JSONObject jsonuidataTrans = new JSONObject(inputMap.get("jsonuidataTrans").toString());

		jsonObject.put("currentDateTimeOffset", getCurrentDateTimeOffset("Europe/London"));
		jsonObject.put("timezonecode", "Europe/London");
		jsonuidata.put("currentDateTimeOffset", getCurrentDateTimeOffset("Europe/London"));
		jsonuidata.put("timezonecode", "Europe/London");

		objmap.putAll((Map<String, Object>) getQuantityTransactionTemplate(138, -1).getBody());

		Date receiveDate = null;
		Date expiryDate = null;
		Date manufDate = null;
		String strcheck = "";
		String strformat = "";
		String updatestr = "";
		String insmat = "";
		boolean nflag = false;

		Date objCreatedDate = cft.getTransactiondate();

		Material objGetMaterialJSON = materialRepository.findByNstatusAndNmaterialcode(1,
				(Integer) inputMap.get("nmaterialcode"));

		MaterialType objGetMaterialTypeJSON = materialTypeRepository
				.findByNmaterialtypecodeAndNstatus((Integer) jsonObject.get("nmaterialtypecode"), 1);

		String sformattype = "{yyyy}/{99999}";

		String strPrefix = (String) commonfunction
				.getInventoryValuesFromJsonString(objGetMaterialJSON.getJsondata(), "Prefix").get("rtnObj");

		String strtypePrefix = (String) commonfunction
				.getInventoryValuesFromJsonString(objGetMaterialTypeJSON.getJsondata(), "prefix").get("rtnObj");

		final String dtransactiondate = getCurrentDateTime().truncatedTo(ChronoUnit.SECONDS).toString()
				.replace("T", " ").replace("Z", "");

		if (!lstDateField.isEmpty()) {
			jsonObject = (JSONObject) convertInputDateToUTCByZone(jsonObject, lstDateField, true);
		}

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

		String isExpiryNeed = (String) commonfunction
				.getInventoryValuesFromJsonString(objGetMaterialJSON.getJsondata(), "Expiry Validations").get("rtnObj");

		if (isExpiryNeed != null) {
			Map<String, Object> rtnMap = new HashMap<String, Object>();
			if (isExpiryNeed.equals("Expiry date") && !jsonObject.has("Received Date & Time")
					&& !jsonObject.has("Expiry Date & Time")) {
				rtnMap.put("success", false);
				rtnMap.put("msg", "IDS_ATLEASTADDEXPIRYDATEORRECEIVEDDATE");
				return new ResponseEntity<>(rtnMap, HttpStatus.ACCEPTED);
			} else if (isExpiryNeed.equals("Expiry policy") && !jsonObject.has("Received Date & Time")
					&& !jsonObject.has("Expiry Date & Time")) {
				rtnMap.put("success", false);
				rtnMap.put("msg", "IDS_ATLEASTADDRECEIVEDDATEORRECEIVEDDATE");
				return new ResponseEntity<>(rtnMap, HttpStatus.ACCEPTED);
			}
		}
		if (jsonObject.has("Received Date & Time")) {
			receiveDate = (Date) jsonObject.get("Received Date & Time");
			Map<String, Object> rtnMap = new HashMap<String, Object>();
			if (receiveDate != null && expiryDate != null) {
				if (receiveDate.compareTo(expiryDate) == 1) {
					rtnMap.put("success", false);
					rtnMap.put("msg", "IDS_RECEIVEDATELESSTHANEXPEDATE");
					return new ResponseEntity<>(rtnMap, HttpStatus.ACCEPTED);
				}
			}
			if (receiveDate != null && manufDate != null) {
				if (receiveDate.compareTo(manufDate) == -1) {
					rtnMap.put("success", false);
					rtnMap.put("msg", "IDS_RECEIVEDATEGREATERTHANMANUFEDATE");
					return new ResponseEntity<>(rtnMap, HttpStatus.ACCEPTED);
				}
			}
		}
		if (jsonObject.has("Expiry Date & Time")) {
			expiryDate = (Date) jsonObject.get("Expiry Date & Time");
			Map<String, Object> rtnMap = new HashMap<String, Object>();
			if (receiveDate != null && expiryDate != null) {
				if (expiryDate.compareTo(receiveDate) == -1) {
					rtnMap.put("success", false);
					rtnMap.put("msg", "IDS_EXPDATEGREATERTHANRECEIVEDATE");
					return new ResponseEntity<>(rtnMap, HttpStatus.ACCEPTED);
				}
			}
			if (expiryDate != null && manufDate != null) {
				if (expiryDate.compareTo(manufDate) == -1) {
					rtnMap.put("success", false);
					rtnMap.put("msg", "IDS_EXPDATEGREATERTHANMANUFDATE");
					return new ResponseEntity<>(rtnMap, HttpStatus.ACCEPTED);
				}
			}
		}
		if (jsonObject.has("Date Of Manufacturer")) {
			manufDate = (Date) jsonObject.get("Date Of Manufacturer");
			Map<String, Object> rtnMap = new HashMap<String, Object>();
			if (manufDate != null && expiryDate != null) {
				if (manufDate.compareTo(expiryDate) == 1) {
					rtnMap.put("success", false);
					rtnMap.put("msg", "IDS_MANUFDATELESSTHANEXPEDATE");
					return new ResponseEntity<>(rtnMap, HttpStatus.ACCEPTED);
				}
			}

			if (manufDate != null && receiveDate != null) {
				if (manufDate.compareTo(receiveDate) == 1) {
					rtnMap.put("success", false);
					rtnMap.put("msg", "IDS_MANUFDATELESSTHANRECEIVEEDATE");
					return new ResponseEntity<>(rtnMap, HttpStatus.ACCEPTED);
				}
			}
		}

		JSONObject objmat = new JSONObject(objGetMaterialJSON.getJsondata());

		Material objMaterial = materialRepository.findByNstatusAndNmaterialcode(1,
				(Integer) inputMap.get("nmaterialcode"));
		MaterialCategory objMaterialCategory = materialCategoryRepository
				.findByNmaterialcatcode((Integer) inputMap.get("nmaterialcatcode"));

		LSuserMaster objUser = new LSuserMaster();
		objUser.setUsercode(cft.getLsuserMaster());

		if (objmat.has("Reusable")) {

			int reusableNeed = (int) commonfunction
					.getInventoryValuesFromJsonString(objGetMaterialJSON.getJsondata(), "Reusable").get("rtnObj");

			if (reusableNeed == Enumeration.TransactionStatus.YES.gettransactionstatus()) {

				nflag = true;

				int reusableCount = jsonObject.getInt("Received Quantity");

				jsonObjectInvTrans.put("Transaction Date & Time", dtransactiondate);
				jsonObjectInvTrans.put("noffsetTransaction Date & Time", getCurrentDateTimeOffset("Europe/London"));
				jsonObjectInvTrans.put("ntransactiontype",
						Enumeration.TransactionStatus.RECEIVED.gettransactionstatus());
				jsonObjectInvTrans.put("ninventorytranscode",
						Enumeration.TransactionStatus.OUTSIDE.gettransactionstatus());
				jsonObjectInvTrans.put("Received Quantity",
						Enumeration.TransactionStatus.ACTIVE.gettransactionstatus());
				jsonObjectInvTrans.put("nqtyissued", 0);
				jsonObjectInvTrans.put("namountleft", Double.valueOf((Integer) jsonuidata.get("Received Quantity")));

				jsonObject.put("Received Quantity", Enumeration.TransactionStatus.ACTIVE.gettransactionstatus());
				jsonObject.put("nqtyissued", 0);
				jsonObject.put("namountleft", Double.valueOf((Integer) jsonuidata.get("Received Quantity")));
				jsonObject.put("ntransactiontype", Enumeration.TransactionStatus.RECEIVED.gettransactionstatus());
				jsonObject = getmaterialquery(jsonObject, 1);

				jsonuidata.put("Received Quantity", Enumeration.TransactionStatus.ACTIVE.gettransactionstatus());
				jsonuidata.put("nqtyissued", 0);
				jsonuidata.put("namountleft", Double.valueOf((Integer) jsonuidata.get("Received Quantity")));
				jsonuidata.put("ntransactiontype", Enumeration.TransactionStatus.RECEIVED.gettransactionstatus());
				jsonuidata = getmaterialquery(jsonuidata, 1);

				jsonuidataTrans.put("Transaction Date & Time", dtransactiondate);
				jsonuidataTrans.put("noffsetTransaction Date & Time", getCurrentDateTimeOffset("Europe/London"));

				jsonuidataTrans.put("ntransactiontype", Enumeration.TransactionStatus.RECEIVED.gettransactionstatus());
				jsonuidataTrans.put("ninventorytranscode",
						Enumeration.TransactionStatus.OUTSIDE.gettransactionstatus());
				jsonuidataTrans.put("Received Quantity", Enumeration.TransactionStatus.ACTIVE.gettransactionstatus());
				jsonuidataTrans.put("nqtyissued", 0);
				jsonuidataTrans.put("namountleft", Double.valueOf((Integer) jsonuidata.get("Received Quantity")));

				SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:SS");
				Date resultdate = new Date(System.currentTimeMillis());

				for (int i = 0; i < reusableCount; i++) {

					MaterialInventory objSaveMaterialInventory = new MaterialInventory();

					objSaveMaterialInventory.setJsondata(jsonObject.toString());
					objSaveMaterialInventory.setJsonuidata(jsonuidata.toString());
					objSaveMaterialInventory.setNstatus(Enumeration.TransactionStatus.ACTIVE.gettransactionstatus());
					objSaveMaterialInventory.setNtransactionstatus((Integer) inputMap.get("ntransactionstatus"));
					objSaveMaterialInventory.setNsectioncode(
							(Integer) (inputMap.get("nsectioncode") != null ? (Integer) inputMap.get("nsectioncode")
									: -1));
					objSaveMaterialInventory.setNmaterialcode((Integer) inputMap.get("nmaterialcode"));
					objSaveMaterialInventory.setNmaterialtypecode((Integer) inputMap.get("nmaterialtypecode"));
					objSaveMaterialInventory.setNmaterialcatcode((Integer) inputMap.get("nmaterialcatcode"));

					objSaveMaterialInventory.setObjsilentaudit(cft);
					objSaveMaterialInventory = materialInventoryRepository.save(objSaveMaterialInventory);

					if (strPrefix != null && !strPrefix.equals("")) {
						strformat = strtypePrefix + "/" + strPrefix + "/"
								+ getfnFormat(objSaveMaterialInventory.getNmaterialinventorycode(), sformattype);
					} else {
						strformat = strtypePrefix + "/"
								+ getfnFormat(objSaveMaterialInventory.getNmaterialinventorycode(), sformattype);
					}

					jsonObject.put("Inventory ID", strformat);
					jsonuidata.put("Inventory ID", strformat);

					objSaveMaterialInventory.setJsondata(jsonObject.toString());
					objSaveMaterialInventory.setJsonuidata(jsonuidata.toString());

					materialInventoryRepository.save(objSaveMaterialInventory);

					jsonObjectInvTrans.put("Inventory ID", strformat);
					jsonuidataTrans.put("Inventory ID", strformat);

//					if ((int) inputMap.get("needsectionwise") == Enumeration.TransactionStatus.YES
//							.gettransactionstatus()) {
//						jsonuidataTrans.put("Description", "received");
//					} else {
					jsonuidataTrans.put("Description", "received");
//					}

					MaterialInventoryTransaction objTransaction = new MaterialInventoryTransaction();

					objTransaction.setJsondata(jsonObjectInvTrans.toString());
					objTransaction.setJsonuidata(jsonuidataTrans.toString());
					objTransaction.setNstatus(Enumeration.TransactionStatus.ACTIVE.gettransactionstatus());
					objTransaction.setNmaterialinventorycode(objSaveMaterialInventory.getNmaterialinventorycode());
					objTransaction.setNinventorytranscode((Integer) jsonObjectInvTrans.get("ninventorytranscode"));
					objTransaction.setNtransactiontype((Integer) jsonObjectInvTrans.get("ntransactiontype"));
					objTransaction.setNsectioncode((Integer) (jsonObjectInvTrans.get("nsectioncode") != null
							? (Integer) jsonObjectInvTrans.get("nsectioncode")
							: -1));
					objTransaction.setNsitecode(-1);
					objTransaction.setNresultusedmaterialcode(-1);
					objTransaction
							.setNqtyreceived(Double.valueOf((Integer) jsonObjectInvTrans.get("Received Quantity")));
					objTransaction.setNqtyissued(Double.valueOf((Integer) jsonObjectInvTrans.get("nqtyissued")));
					objTransaction.setCreatedbyusercode(objUser);
					objTransaction.setIssuedbyusercode(objUser);
					objTransaction.setCreateddate(objCreatedDate);

					materialInventoryTransactionRepository.save(objTransaction);

					jsonUidataarray.put(new JSONObject(jsonuidata.toString()));
					jsonUidataarrayTrans.put(new JSONObject(jsonuidataTrans.toString()));
				}
				inputMap.put("reusableCount", reusableCount);
				inputMap.put("ntranscode", inputMap.get("ntransactionstatus"));

//				createMaterialInventoryhistory(inputMap);
			}
		}

		if (nflag == false) {

			MaterialInventory objSaveMaterialInventory = new MaterialInventory();

			objSaveMaterialInventory.setJsondata(jsonObject.toString());
			objSaveMaterialInventory.setJsonuidata(jsonuidata.toString());
			objSaveMaterialInventory.setNstatus(Enumeration.TransactionStatus.ACTIVE.gettransactionstatus());
			objSaveMaterialInventory.setNtransactionstatus((Integer) inputMap.get("ntransactionstatus"));
			objSaveMaterialInventory.setNsectioncode(
					(Integer) (inputMap.get("nsectioncode") != null ? (Integer) inputMap.get("nsectioncode") : -1));
			objSaveMaterialInventory.setNmaterialcode((Integer) inputMap.get("nmaterialcode"));
			objSaveMaterialInventory.setNmaterialtypecode((Integer) inputMap.get("nmaterialtypecode"));
			objSaveMaterialInventory.setNmaterialcatcode((Integer) inputMap.get("nmaterialcatcode"));

			objSaveMaterialInventory = materialInventoryRepository.save(objSaveMaterialInventory);

			if (strPrefix != null && !strPrefix.equals("")) {
				strformat = strtypePrefix + "/" + strPrefix + "/"
						+ getfnFormat(objSaveMaterialInventory.getNmaterialinventorycode(), sformattype);
			} else {
				strformat = strtypePrefix + "/"
						+ getfnFormat(objSaveMaterialInventory.getNmaterialinventorycode(), sformattype);
			}
			jsonObject.put("Inventory ID", strformat);
			jsonObject.put("ntransactiontype", Enumeration.TransactionStatus.RECEIVED.gettransactionstatus());
			jsonObject = getmaterialquery(jsonObject, 1);
			jsonuidata.put("Inventory ID", strformat);
			jsonuidata.put("ntransactiontype", Enumeration.TransactionStatus.RECEIVED.gettransactionstatus());
			jsonuidata = getmaterialquery(jsonuidata, 1);

			objSaveMaterialInventory.setJsondata(jsonObject.toString());
			objSaveMaterialInventory.setJsonuidata(jsonuidata.toString());

			materialInventoryRepository.save(objSaveMaterialInventory);

			inputMap.put("nmaterialinventorycode", objSaveMaterialInventory.getNmaterialinventorycode());
			inputMap.put("ntranscode", inputMap.get("ntransactionstatus"));
//			createMaterialInventoryhistory(inputMap);

			jsonObjectInvTrans.put("Inventory ID", strformat);
			jsonuidataTrans.put("Inventory ID", strformat);

			jsonObjectInvTrans.put("ntransactiontype", Enumeration.TransactionStatus.RECEIVE.gettransactionstatus());
			jsonObjectInvTrans.put("Transaction Date & Time", new Date());
			jsonObjectInvTrans.put("noffsetTransaction Date & Time", getCurrentDateTimeOffset("Europe/London"));
			jsonObjectInvTrans.put("ninventorytranscode", Enumeration.TransactionStatus.OUTSIDE.gettransactionstatus());
			jsonObjectInvTrans.put("nqtyissued", 0);
			jsonObjectInvTrans.put("namountleft",
					Double.valueOf((Integer) jsonObjectInvTrans.get("Received Quantity")));
			jsonuidataTrans.put("ntransactiontype", Enumeration.TransactionStatus.RECEIVE.gettransactionstatus());
			jsonuidataTrans.put("Transaction Date & Time", dtransactiondate);
			jsonuidataTrans.put("Transaction Date & Time", new Date());
			jsonuidataTrans.put("noffsetTransaction Date & Time", getCurrentDateTimeOffset("Europe/London"));

			jsonuidataTrans.put("ninventorytranscode", Enumeration.TransactionStatus.OUTSIDE.gettransactionstatus());
			jsonuidataTrans.put("nqtyissued", 0);
			jsonuidataTrans.put("namountleft", Double.valueOf((Integer) jsonObjectInvTrans.get("Received Quantity")));

			if (inputMap.get("needsectionwise") != null
					&& (int) inputMap.get("needsectionwise") == Enumeration.TransactionStatus.YES
							.gettransactionstatus()) {
				jsonuidataTrans.put("Description", "IDS_RECEIVED");
			} else {
				jsonuidataTrans.put("Description", "IDS_RECEIVED");
			}

			MaterialInventoryTransaction objTransaction = new MaterialInventoryTransaction();

			objTransaction.setJsondata(jsonObjectInvTrans.toString());
			objTransaction.setJsonuidata(jsonuidataTrans.toString());
			objTransaction.setNstatus(Enumeration.TransactionStatus.ACTIVE.gettransactionstatus());
			objTransaction.setNmaterialinventorycode(objSaveMaterialInventory.getNmaterialinventorycode());
			objTransaction.setNinventorytranscode((Integer) jsonObjectInvTrans.get("ninventorytranscode"));
			objTransaction.setNtransactiontype((Integer) jsonObjectInvTrans.get("ntransactiontype"));
			objTransaction.setNsectioncode((Integer) (jsonObjectInvTrans.get("nsectioncode") != null
					? (Integer) jsonObjectInvTrans.get("nsectioncode")
					: -1));
			objTransaction.setNsitecode(-1);
			objTransaction.setNresultusedmaterialcode(-1);
			objTransaction.setNqtyreceived(Double.valueOf((Integer) jsonObjectInvTrans.get("Received Quantity")));
			objTransaction.setNqtyissued(Double.valueOf((Integer) jsonObjectInvTrans.get("nqtyissued")));
			objTransaction.setCreatedbyusercode(objUser);
			objTransaction.setIssuedbyusercode(objUser);
			objTransaction.setCreateddate(objCreatedDate);

			materialInventoryTransactionRepository.save(objTransaction);

			jsonUidataarray.put(jsonuidata);
			jsonUidataarrayTrans.put(jsonuidataTrans);
		}

//		objmap.putAll((Map<String, Object>) getMaterialInventoryByID(inputMap).getBody());
		objmap.putAll((Map<String, Object>) getMaterialInventoryDetails(inputMap).getBody());
		objmap.put("objsilentaudit", cft);
		objmap.put("tabScreen", "IDS_MATERIALSECTION");

		return new ResponseEntity<>(objmap, HttpStatus.OK);

	}

	@SuppressWarnings({ "unchecked", "unused" })
	public ResponseEntity<Object> UpdateMaterialInventory(Map<String, Object> inputMap) throws Exception {

		Map<String, Object> objmap = new LinkedHashMap<String, Object>();

		Date receiveDate = null;
		Date expiryDate = null;
		Date manufDate = null;
		List<String> lstDateField = (List<String>) inputMap.get("DateList");

		final String dtransactiondate = getCurrentDateTime().truncatedTo(ChronoUnit.SECONDS).toString()
				.replace("T", " ").replace("Z", "");

		JSONObject jsonObject = new JSONObject(inputMap.get("materialInventoryJson").toString());
		JSONObject jsonuidata = new JSONObject(inputMap.get("jsonuidata").toString());

		JSONObject jsonObjectTrans = new JSONObject(inputMap.get("MaterialInventoryTrans").toString());
		JSONObject jsonuidataTrans = new JSONObject(inputMap.get("jsonuidataTrans").toString());

		Material objGetMaterialJSON = materialRepository.findByNstatusAndNmaterialcode(1,
				(Integer) inputMap.get("nmaterialcode"));

		if (!lstDateField.isEmpty()) {
			jsonObject = (JSONObject) convertInputDateToUTCByZone(jsonObject, lstDateField, false);
			jsonuidata = (JSONObject) convertInputDateToUTCByZone(jsonuidata, lstDateField, false);
			jsonObjectTrans = (JSONObject) convertInputDateToUTCByZone(jsonObjectTrans, lstDateField, false);
			jsonuidataTrans = (JSONObject) convertInputDateToUTCByZone(jsonuidataTrans, lstDateField, false);
		}
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		String isExpiryNeed = (String) commonfunction
				.getInventoryValuesFromJsonString(objGetMaterialJSON.getJsondata(), "Expiry Validations").get("rtnObj");

		if (isExpiryNeed != null) {
			Map<String, Object> rtnMap = new HashMap<String, Object>();
			if (isExpiryNeed.equals("Expiry date") && !jsonObject.has("Received Date & Time")
					&& !jsonObject.has("Expiry Date & Time")) {
				rtnMap.put("success", false);
				rtnMap.put("msg", "IDS_ATLEASTADDEXPIRYDATEORRECEIVEDDATE");
				return new ResponseEntity<>(rtnMap, HttpStatus.ACCEPTED);
			} else if (isExpiryNeed.equals("Expiry policy") && !jsonObject.has("Received Date & Time")
					&& !jsonObject.has("Expiry Date & Time")) {
				rtnMap.put("success", false);
				rtnMap.put("msg", "IDS_ATLEASTADDRECEIVEDDATEORRECEIVEDDATE");
				return new ResponseEntity<>(rtnMap, HttpStatus.ACCEPTED);
			}
		}
		if (jsonObject.has("Received Date & Time")) {
			receiveDate = (Date) jsonObject.get("Received Date & Time");
			Map<String, Object> rtnMap = new HashMap<String, Object>();
			if (receiveDate != null && expiryDate != null) {
				if (receiveDate.compareTo(expiryDate) == 1) {
					rtnMap.put("success", false);
					rtnMap.put("msg", "IDS_RECEIVEDATELESSTHANEXPEDATE");
					return new ResponseEntity<>(rtnMap, HttpStatus.ACCEPTED);
				}
			}
			if (receiveDate != null && manufDate != null) {
				if (receiveDate.compareTo(manufDate) == -1) {
					rtnMap.put("success", false);
					rtnMap.put("msg", "IDS_RECEIVEDATEGREATERTHANMANUFEDATE");
					return new ResponseEntity<>(rtnMap, HttpStatus.ACCEPTED);
				}
			}
		}
		if (jsonObject.has("Expiry Date & Time")) {
			expiryDate = (Date) jsonObject.get("Expiry Date & Time");
			Map<String, Object> rtnMap = new HashMap<String, Object>();
			if (receiveDate != null && expiryDate != null) {
				if (expiryDate.compareTo(receiveDate) == -1) {
					rtnMap.put("success", false);
					rtnMap.put("msg", "IDS_EXPDATEGREATERTHANRECEIVEDATE");
					return new ResponseEntity<>(rtnMap, HttpStatus.ACCEPTED);
				}
			}
			if (expiryDate != null && manufDate != null) {
				if (expiryDate.compareTo(manufDate) == -1) {
					rtnMap.put("success", false);
					rtnMap.put("msg", "IDS_EXPDATEGREATERTHANMANUFDATE");
					return new ResponseEntity<>(rtnMap, HttpStatus.ACCEPTED);
				}
			}
		}
		if (jsonObject.has("Date Of Manufacturer")) {
			manufDate = (Date) jsonObject.get("Date Of Manufacturer");
			Map<String, Object> rtnMap = new HashMap<String, Object>();
			if (manufDate != null && expiryDate != null) {
				if (manufDate.compareTo(expiryDate) == 1) {
					rtnMap.put("success", false);
					rtnMap.put("msg", "IDS_MANUFDATELESSTHANEXPEDATE");
					return new ResponseEntity<>(rtnMap, HttpStatus.ACCEPTED);
				}
			}

			if (manufDate != null && receiveDate != null) {
				if (manufDate.compareTo(receiveDate) == 1) {
					rtnMap.put("success", false);
					rtnMap.put("msg", "IDS_MANUFDATELESSTHANRECEIVEEDATE");
					return new ResponseEntity<>(rtnMap, HttpStatus.ACCEPTED);
				}
			}
		}

		jsonObject.put("ntransactiontype", Enumeration.TransactionStatus.RECEIVED.gettransactionstatus());
		jsonuidata.put("ntransactiontype", Enumeration.TransactionStatus.RECEIVED.gettransactionstatus());
		
		if (!lstDateField.isEmpty()) {
			jsonObject = (JSONObject) removeISTinDateString(jsonObject, lstDateField, false);
			jsonuidata = (JSONObject) removeISTinDateString(jsonuidata, lstDateField, false);
			jsonObjectTrans = (JSONObject) removeISTinDateString(jsonObjectTrans, lstDateField, false);
			jsonuidataTrans = (JSONObject) removeISTinDateString(jsonuidataTrans, lstDateField, false);
		}

		MaterialInventory objInventory = materialInventoryRepository
				.findByNmaterialinventorycode((Integer) inputMap.get("nmaterialinventorycode"));
		objInventory.setJsondata(ReplaceQuote(jsonObject.toString()));
		objInventory.setJsonuidata(ReplaceQuote(jsonuidata.toString()));
		objInventory
				.setNsectioncode(inputMap.get("nsectioncode") != null ? (Integer) inputMap.get("nsectioncode") : -1);
		materialInventoryRepository.save(objInventory);

		jsonObjectTrans.put("Transaction Date & Time", dtransactiondate);
		jsonObjectTrans.put("ninventorytranscode", Enumeration.TransactionStatus.OUTSIDE.gettransactionstatus());
		jsonObjectTrans.put("nqtyissued", 0);
		jsonObjectTrans.put("namountleft", Double.valueOf((Integer) jsonuidata.get("Received Quantity")));
		jsonuidataTrans.put("namountleft", Double.valueOf((Integer) jsonuidata.get("Received Quantity")));
		jsonuidataTrans.put("Transaction Date & Time", dtransactiondate);
		jsonuidataTrans.put("ninventorytranscode", Enumeration.TransactionStatus.OUTSIDE.gettransactionstatus());
		jsonuidataTrans.put("nqtyissued", 0);

		List<MaterialInventoryTransaction> lstTransactions = materialInventoryTransactionRepository
				.findByNmaterialinventorycodeOrderByNmaterialinventtranscode(
						(Integer) inputMap.get("nmaterialinventorycode"));

		Double recQty = Double.valueOf((Integer) jsonuidata.get("Received Quantity"));
//		Double issQty = Double.valueOf((Integer) jsonuidata.get("nqtyissued"));

		JSONObject jsonObjectLstTrans = jsonObjectTrans;
		JSONObject jsonObjectUiTrans = jsonuidataTrans;

		lstTransactions.stream().peek(objTransaction -> {
			objTransaction.setJsondata(ReplaceQuote(jsonObjectLstTrans.toString()));
			objTransaction.setJsonuidata(ReplaceQuote(jsonObjectUiTrans.toString()));
			objTransaction.setNsectioncode(
					inputMap.get("nsectioncode") != null ? (Integer) inputMap.get("nsectioncode") : -1);
			objTransaction.setNqtyreceived(recQty);
//			objTransaction.setNqtyissued(issQty);
		}).collect(Collectors.toList());

		materialInventoryTransactionRepository.save(lstTransactions);

		inputMap.put("nflag", Enumeration.TransactionStatus.ACTIVE.gettransactionstatus());
		objmap.putAll((Map<String, Object>) getMaterialInventoryByID(inputMap).getBody());

		objmap.put("nregtypecode", -1);
		objmap.put("nregsubtypecode", -1);
		objmap.put("ndesigntemplatemappingcode", jsonuidata.get("nmaterialconfigcode"));

		return new ResponseEntity<>(objmap, HttpStatus.OK);

	}

	@SuppressWarnings({ "unused" })
	public ResponseEntity<Object> getQuantityTransactionTemplate(Integer formcode, Integer materialtypecode)
			throws Exception {

		String siteLabelName = "";
		String sprecision = "";
		Map<String, Object> objmap = new LinkedHashMap<String, Object>();

		List<MaterialConfig> lstMaterialConfig = materialConfigRepository
				.findByNmaterialtypecodeAndNformcodeAndNstatus(materialtypecode, formcode, 1);

		JSONArray Layout = new JSONArray(lstMaterialConfig.get(0).getJsondata());

		for (int L = 0; L < Layout.length(); L++) {
			JSONObject row = new JSONObject(Layout.get(L).toString());
			if (row.has("children")) {
				JSONArray column = new JSONArray(row.get("children").toString());
				for (int c = 0; c < column.length(); c++) {
					JSONObject component = new JSONObject(column.get(c).toString());
					if (component.has("children")) {
						JSONArray maincomponent = new JSONArray(component.get("children").toString());
						for (int m = 0; m < maincomponent.length(); m++) {
							JSONObject feilds = new JSONObject(maincomponent.get(m).toString());
							if (feilds.has("source")) {
								if (feilds.get("source").equals("site")) {
									siteLabelName = (String) feilds.get("label");
								}
							} else if (feilds.has("nprecision")) {
								sprecision = feilds.getString("nprecision");
							}
						}
					}
				}
			}
		}

		return new ResponseEntity<>(objmap, HttpStatus.OK);
	}

	public JSONObject getmaterialquery(JSONObject objMaterialInventory, int nflag) throws Exception {
//		JSONObject objMatNextvalidationperiod = null;
//		JSONObject objMatOpenExpiryPeriod = null;
//		JSONObject objMatExpiryPolicyperiod = null;

		List<Material> lstMaterial = new ArrayList<Material>();
		Material objMaterial = materialRepository.findByNstatusAndNmaterialcode(1,
				(Integer) objMaterialInventory.get("nmaterialcode"));

		lstMaterial.add(objMaterial);

//		JSONObject objMat = new JSONObject(lstMaterial.get(0).getJsondata());

//		if (objMat.has("Next Validation Period")) {
//			objMatNextvalidationperiod = new JSONObject(objMat.get("Next Validation Period").toString());
//		}
//		if (objMat.has("Open Expiry Period")) {
//			objMatOpenExpiryPeriod = new JSONObject(objMat.get("Open Expiry Period").toString());
//		}
//		if (objMat.has("Expiry Policy Period")) {
//			objMatExpiryPolicyperiod = new JSONObject(objMat.get("Expiry Policy Period").toString());
//		}
		if (lstMaterial != null && lstMaterial.size() > 0) {
			// SET THE NEXT VALIDATION DATE
			if (nflag == 1) {
//				if (objMat.has("Next Validation")) {
//					if (!objMat.get("Next Validation").equals("")) {
//						if ((int) objMat.get("Next Validation") > 0) {
//							DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//							if (objMaterialInventory.has("Received Date & Time")) {
//								if (!objMaterialInventory.get("Received Date & Time").toString().equals("")) {
//									Date date1 = df.parse(objMaterialInventory.get("Received Date & Time").toString());
//									if (date1 != null) {
//										Calendar cal1 = Calendar.getInstance();
//										cal1.setTime(date1);
//										if (objMatNextvalidationperiod != null) {
//											if ((int) objMatNextvalidationperiod
//													.get("value") == Enumeration.TransactionStatus.DAYPERIOD
//															.gettransactionstatus())
//												cal1.add(Calendar.DATE, (int) objMat.get("Next Validation"));
//											if ((int) objMatNextvalidationperiod
//													.get("value") == Enumeration.TransactionStatus.MONTHPERIOD
//															.gettransactionstatus())
//												cal1.add(Calendar.MONTH, (int) objMat.get("Next Validation"));
//											if ((int) objMatNextvalidationperiod
//													.get("value") == Enumeration.TransactionStatus.YEARPERIOD
//															.gettransactionstatus())
//												cal1.add(Calendar.YEAR, (int) objMat.get("Next Validation"));
//											date1 = cal1.getTime();
//										}
//										objMaterialInventory.put("dretestdate", df.format(date1));
//									}
//								}
//							}
//						}
//					}
//					/*
//					 * else { objMaterialInventory.put("dretestdate", "-"); }
//					 */
//				}
				// SET THE POLICY EXPIRY DATE
//				if (objMat.has("Expiry Policy Days")) {
//					if (!objMat.get("Expiry Policy Days").equals("")) {
//						if ((int) objMat.get("Expiry Policy Days") > 0) {
//							DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//							if (objMaterialInventory.has("Expiry Date & Time")) {
//								if (!objMaterialInventory.get("Expiry Date & Time").toString().equals("")) {
//									Date date2 = df.parse(objMaterialInventory.get("Expiry Date & Time").toString());
//									if (date2 != null) {
//										Calendar cal2 = Calendar.getInstance();
//										cal2.setTime(date2);
//										if (objMatExpiryPolicyperiod != null) {
//											if ((int) objMatExpiryPolicyperiod
//													.get("value") == Enumeration.TransactionStatus.DAYPERIOD
//															.gettransactionstatus())
//												cal2.add(Calendar.DATE, (int) objMat.get("Expiry Policy Days"));
//											if ((int) objMatExpiryPolicyperiod
//													.get("value") == Enumeration.TransactionStatus.MONTHPERIOD
//															.gettransactionstatus())
//												cal2.add(Calendar.MONTH, (int) objMat.get("Expiry Policy Days"));
//											if ((int) objMatExpiryPolicyperiod
//													.get("value") == Enumeration.TransactionStatus.YEARPERIOD
//															.gettransactionstatus())
//												cal2.add(Calendar.YEAR, (int) objMat.get("Expiry Policy Days"));
//											date2 = cal2.getTime();
//										}
//										objMaterialInventory.put("dexpirypolicydate", df.format(date2));
//									}
//								}
//							}
//						}
//					}
//					/*
//					 * else { objMaterialInventory.put("dexpirypolicydate", "-"); }
//					 */
//
//				}
			}
//			if (nflag == 2) {
//				// SET THE POLICY EXPIRY DATE
//				if (objMat.has("Open Expiry")) {
//					if (!objMat.get("Open Expiry").equals("")) {
//						if ((int) objMat.get("Open Expiry") > 0) {
//							DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//							if (objMaterialInventory.has("Open Date")) {
//								if (!objMaterialInventory.get("Open Date").toString().equals("")) {
//									Date date2 = df.parse(objMaterialInventory.get("Open Date").toString());
//									if (date2 != null) {
//										Calendar cal2 = Calendar.getInstance();
//										cal2.setTime(date2);
//										if (objMatOpenExpiryPeriod != null) {
//											if ((int) objMatOpenExpiryPeriod
//													.get("value") == Enumeration.TransactionStatus.DAYPERIOD
//															.gettransactionstatus())
//												cal2.add(Calendar.DATE, (int) objMat.get("Open Expiry"));
//											if ((int) objMatOpenExpiryPeriod
//													.get("value") == Enumeration.TransactionStatus.MONTHPERIOD
//															.gettransactionstatus())
//												cal2.add(Calendar.MONTH, (int) objMat.get("Open Expiry"));
//											if ((int) objMatOpenExpiryPeriod
//													.get("value") == Enumeration.TransactionStatus.YEARPERIOD
//															.gettransactionstatus())
//												cal2.add(Calendar.YEAR, (int) objMat.get("Open Expiry"));
//											date2 = cal2.getTime();
//										}
//										objMaterialInventory.put("dopenexpirydate", df.format(date2));
//									}
//								}
//							}
//						}
//					}
//					/*
//					 * else { objMaterialInventory.put("dopenexpirydate", "-"); }
//					 */
//
//				}
//			}
		}
		return objMaterialInventory;
	}

	public String getfnFormat(int sequenceno, String sFormat) throws Exception {
		// init(timestamp);

		if (sFormat != null) {
			while (sFormat.contains("{")) {
				int start = sFormat.indexOf('{');
				int end = sFormat.indexOf('}');

				String subString = sFormat.substring(start + 1, end);
				if (subString.equals("yy") || subString.equals("YY")) {
					SimpleDateFormat sdf = new SimpleDateFormat("yy", Locale.getDefault());
					sdf.toPattern();
					Date date = new Date();
					String replaceString = sdf.format(date);
					sFormat = sFormat.replace('{' + subString + '}', replaceString);
				} else if (subString.equals("yyyy") || subString.equals("YYYY")) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy", Locale.getDefault());
					sdf.toPattern();
					Date date = new Date();
					String replaceString = sdf.format(date);
					sFormat = sFormat.replace('{' + subString + '}', replaceString);
				} else if (subString.equals("mm") || subString.equals("MM")) {
					SimpleDateFormat sdf = new SimpleDateFormat("MM", Locale.getDefault());
					sdf.toPattern();
					Date date = new Date();
					String replaceString = sdf.format(date);
					sFormat = sFormat.replace('{' + subString + '}', replaceString);
				} else if (subString.equals("mon") || subString.equals("MON")) {
					SimpleDateFormat sdf = new SimpleDateFormat("MON", Locale.getDefault());
					sdf.toPattern();
					Date date = new Date();
					String replaceString = sdf.format(date);
					sFormat = sFormat.replace('{' + subString + '}', replaceString);
				} else if (subString.equals("dd") || subString.equals("DD")) {
					SimpleDateFormat sdf = new SimpleDateFormat("dd", Locale.getDefault());
					sdf.toPattern();
					Date date = new Date();
					String replaceString = sdf.format(date);
					sFormat = sFormat.replace('{' + subString + '}', replaceString);
				} else if (subString.matches("9+")) {
					String seqPadding = "%0" + subString.length() + "d";
					String replaceString = String.format(seqPadding, sequenceno);
					sFormat = sFormat.replace('{' + subString + '}', replaceString);
				}

				else {
					sFormat = sFormat.replace('{' + subString + '}', subString);
				}
			}
		}
		return sFormat;
	}

	@SuppressWarnings("unchecked")
	public ResponseEntity<Object> getQuantityTransaction(Map<String, Object> inputMap) {
		Map<String, Object> objmap = new LinkedHashMap<String, Object>();
		Map<String, Object> paramObj = new LinkedHashMap<String, Object>();
		List<Map<String, Object>> lstMap = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> lstMapForParentCombo = new ArrayList<Map<String, Object>>();

		String filterQueryComponents = "";
//		ObjectMapper Objmapper = new ObjectMapper();
		String sprecision = "";
		String QtyTransactionpopup = "";
//		String siteLabelName = "";
//		int nprecision = 0;
//		boolean nflag = false;
		int ntransactiontype = 0;

		List<MaterialConfig> lstMaterialConfig = materialConfigRepository
				.findByNformcodeAndNmaterialtypecodeAndNstatusOrderByNmaterialconfigcode(138,
						Enumeration.TransactionStatus.NA.gettransactionstatus(),
						Enumeration.TransactionStatus.ACTIVE.gettransactionstatus());

//		int sourceSection = materialInventoryTransactionRepository.findByNmaterialinventorycodeOrderByNmaterialinventtranscodeDesc((Integer) inputMap.get("nmaterialinventorycode")).get(0).getNsectioncode();

		if ((int) inputMap.get("nflag") == 1) {

			MaterialInventory objInventory = materialInventoryRepository
					.findByNmaterialinventorycode((Integer) inputMap.get("nmaterialinventorycode"));

			int statusCheck = (Integer) commonfunction
					.getInventoryValuesFromJsonString(objInventory.getJsondata(), "ntranscode").get("rtnObj");

			if (statusCheck == Enumeration.TransactionStatus.RELEASED.gettransactionstatus()) {

				JSONArray Layout = new JSONArray(lstMaterialConfig.get(0).getJsondata());

				for (int L = 0; L < Layout.length(); L++) {
					JSONObject row = new JSONObject(Layout.get(L).toString());
					if (row.has("children")) {
						JSONArray column = new JSONArray(row.get("children").toString());
						for (int c = 0; c < column.length(); c++) {
							JSONObject component = new JSONObject(column.get(c).toString());
							if (component.has("children")) {
								JSONArray maincomponent = new JSONArray(component.get("children").toString());
								for (int m = 0; m < maincomponent.length(); m++) {
									JSONObject feilds = new JSONObject(maincomponent.get(m).toString());
									if (feilds.has("parent")) {
										if ((boolean) feilds.get("parent") == true) {

											lstMap.add(feilds.toMap());
										} else if ((boolean) feilds.get("parent") == false) {
											lstMapForParentCombo.add(feilds.toMap());
										}
									}
									if (feilds.has("nprecision")) {
										sprecision = feilds.getString("nprecision");
									}
									if (feilds.has("nsqlquerycode")) {

										filterQueryComponents += feilds.get("nsqlquerycode").toString() + ',';
									}
								}
							}
						}
					}
				}

				List<MappedTemplateFieldPropsMaterial> lstsearchField = new LinkedList<>();
				List<String> lstsearchFields = new LinkedList<>();

				@SuppressWarnings("unused")
				MappedTemplateFieldPropsMaterial objFieldPropsMaterial = mappedTemplateFieldPropsMaterialRepository
						.findByNmaterialconfigcodeAndNstatus(
								Enumeration.TransactionStatus.ACTIVE.gettransactionstatus(), 1);

				lstsearchField = (List<MappedTemplateFieldPropsMaterial>) commonfunction
						.getInventoryValuesFromJsonString(objInventory.getJsondata(), "138").get("rtnObj");

				JSONArray objarray = new JSONArray(lstsearchField.get(0).getJsondata());
				for (int i = 0; i < objarray.length(); i++) {
					JSONObject jsonobject = new JSONObject(objarray.get(i).toString());
					if (jsonobject.has("2")) {
						lstsearchFields.add((String) jsonobject.get("2"));
					}
				}
				objmap.put("DateFeildsProperties", lstsearchFields);

			} else {
				return new ResponseEntity<>("IDS_SELECTRELEASEDINVENTORY", HttpStatus.CONFLICT);
			}
		}
		if ((int) inputMap.get("nflag") == 1) {

//			QtyTransactionpopup = getJdbcTemplate()
//					.queryForObject("select  (COALESCE(Cast(Sum(mt.nqtyreceived)as decimal (" + sprecision + ")), 0) "
//							+ " -COALESCE(Sum(mt.nqtyissued), 0) )::text"
//							+ " as navailableqty from materialinventorytransaction mt,materialinventory mi "
//							+ " where mi.nmaterialinventorycode = mt.nmaterialinventorycode and  mt.nmaterialinventorycode = "
//							+ inputMap.get("nmaterialinventorycode") + " " + " and mi.nstatus = "
//							+ Enumeration.TransactionStatus.ACTIVE.gettransactionstatus() + " and mt.nstatus= "
//							+ Enumeration.TransactionStatus.ACTIVE.gettransactionstatus() + ""
//							+ " and ((mi.jsondata->'ntranscode')::int != "
//							+ Enumeration.TransactionStatus.RETIRED.gettransactionstatus() + " and mt.nsectioncode="
//							+ sourceSection + "	and mt.nsitecode=" + userInfo.getNtranssitecode()
//							+ " and (mi.jsondata->'ntranscode')::int != "
//							+ Enumeration.TransactionStatus.EXPIRED.gettransactionstatus() + ")"
//							+ " and (mi.jsondata->'ntransactiontype')::int !="
//							+ Enumeration.TransactionStatus.USED.gettransactionstatus() + "; ", String.class);

			objmap.put("navailableqty", QtyTransactionpopup);
			objmap.put("parentcolumnlist", lstMap);
			paramObj.put("nmaterialinventorycode", inputMap.get("nmaterialinventorycode"));
			paramObj.put("nsitecode", 1);
			objmap.put("filterQueryComponents", filterQueryComponents.substring(0, filterQueryComponents.length() - 1));
			objmap.put("parameters", paramObj);
			for (int i = 0; i < lstMapForParentCombo.size(); i++) {
				objmap.put("childcolumnlist", lstMapForParentCombo.get(i));
//				objmap.putAll(
//						(Map<String, Object>) objDynamicPreRegDesignDAO.getComboValues(objmap, userInfo).getBody());
			}
			objmap.put("nprecision", sprecision.substring(sprecision.indexOf(",") + 1));
			objmap.put("QuantityTransactionTemplate", lstMaterialConfig);
		} else if ((int) inputMap.get("nflag") == 2) {
			String transtypechange = "";
			if (inputMap.containsKey("ntransactiontype")) {
				ntransactiontype = (int) inputMap.get("ntransactiontype");
			}

			if (ntransactiontype == Enumeration.TransactionStatus.ISSUE.gettransactionstatus()) {

//				transtypechange = getJdbcTemplate().queryForObject(
//						"select  (COALESCE(Cast(Sum(mt.nqtyreceived)as decimal (" + inputMap.get("sprecision")
//								+ ")), 0) " + " -COALESCE(Cast(Sum(mt.nqtyissued)as decimal ("
//								+ inputMap.get("sprecision") + ")), 0))::text"
//								+ " as navailableqty from materialinventorytransaction mt,materialinventory mi "
//								+ " where mi.nmaterialinventorycode = mt.nmaterialinventorycode and  mt.nmaterialinventorycode = "
//								+ inputMap.get("nmaterialinventorycode") + " " + " and mi.nstatus = "
//								+ Enumeration.TransactionStatus.ACTIVE.gettransactionstatus() + " and mt.nstatus= "
//								+ Enumeration.TransactionStatus.ACTIVE.gettransactionstatus() + ""
//								+ " and ((mi.jsondata->'ntranscode')::int != "
//								+ Enumeration.TransactionStatus.RETIRED.gettransactionstatus() + " and mt.nsectioncode="
//								+ sourceSection + "	and mt.nsitecode=" + userInfo.getNtranssitecode()
//								+ " and (mi.jsondata->'ntranscode')::int != "
//								+ Enumeration.TransactionStatus.EXPIRED.gettransactionstatus() + ")"
//								+ " and (mi.jsondata->'ntransactiontype')::int !="
//								+ Enumeration.TransactionStatus.USED.gettransactionstatus() + "; ",
//						String.class);

			} else if (ntransactiontype == Enumeration.TransactionStatus.RETURN.gettransactionstatus()) {

//				transtypechange = getJdbcTemplate().queryForObject(
//						"select  (COALESCE(Cast(Sum(mt.nqtyreceived)as decimal (" + inputMap.get("sprecision")
//								+ ")), 0 ) " + " - COALESCE(Cast(Sum(mt.nqtyissued)as decimal ("
//								+ inputMap.get("sprecision") + ")), 0) )::text"
//								+ " AS navailableqty from materialinventorytransaction mt,materialinventory mi "
//								+ " where mi.nmaterialinventorycode = mt.nmaterialinventorycode and  mt.nmaterialinventorycode = "
//								+ inputMap.get("nmaterialinventorycode") + " "
//								+ " and (mt.jsondata->'ntransactiontype')::int in ("
//								+ Enumeration.TransactionStatus.ISSUE.gettransactionstatus() + ", "
//								+ Enumeration.TransactionStatus.RETURN.gettransactionstatus() + ", "
//								+ Enumeration.TransactionStatus.RECEIVED.gettransactionstatus() + ")"
//								+ " and mi.nstatus = " + Enumeration.TransactionStatus.ACTIVE.gettransactionstatus()
//								+ " and mt.nstatus= " + Enumeration.TransactionStatus.ACTIVE.gettransactionstatus() + ""
//								+ " and (mi.jsondata->'ntranscode')::int != "
//								+ Enumeration.TransactionStatus.RETIRED.gettransactionstatus() + " and mt.nsectioncode="
//								+ inputMap.get("sourceSection") + "	and mt.nsitecode=" + userInfo.getNtranssitecode()
//								+ " and (mi.jsondata->'ntranscode')::int != "
//								+ Enumeration.TransactionStatus.EXPIRED.gettransactionstatus() + ""
//								+ " and (mt.jsondata->'ntransactiontype')::int !="
//								+ Enumeration.TransactionStatus.USED.gettransactionstatus() + "; ",
//						String.class);
			}
			objmap.put("navailableqty", transtypechange);
		} else if ((int) inputMap.get("nflag") == 3) {
			String transtypechange = "";
			if (inputMap.containsKey("ntransactiontype")) {
				ntransactiontype = (int) inputMap.get("ntransactiontype");
			}
			if (ntransactiontype == Enumeration.TransactionStatus.REJECTED.gettransactionstatus()) {
//				transtypechange = getJdbcTemplate().queryForObject(
//						"select  (COALESCE(Cast(Sum(mt.nqtyreceived)as decimal (" + inputMap.get("sprecision")
//								+ ")), 0)" + "	 -COALESCE(Cast(Sum(mt.nqtyissued)as decimal ("
//								+ inputMap.get("sprecision") + ")), 0) )::text"
//								+ " as navailableqty from materialinventorytransaction mt,materialinventory mi "
//								+ " where mi.nmaterialinventorycode = mt.nmaterialinventorycode and  mt.nmaterialinventorycode = "
//								+ inputMap.get("nmaterialinventorycode") + " " + " and mi.nstatus = "
//								+ Enumeration.TransactionStatus.ACTIVE.gettransactionstatus() + " and mt.nstatus= "
//								+ Enumeration.TransactionStatus.ACTIVE.gettransactionstatus() + ""
//								+ " and ((mi.jsondata->'ntranscode')::int != "
//								+ Enumeration.TransactionStatus.RETIRED.gettransactionstatus() + " and mt.nsectioncode="
//								+ sourceSection + "	and mt.nsitecode=" + userInfo.getNtranssitecode()
//								+ " and (mi.jsondata->'ntranscode')::int != "
//								+ Enumeration.TransactionStatus.EXPIRED.gettransactionstatus() + ")"
//								+ " and (mt.jsondata->'ntransactiontype')::int !="
//								+ Enumeration.TransactionStatus.USED.gettransactionstatus() + "; ",
//						String.class);
				objmap.put("navailableqty", transtypechange);

			}
//			transtypechange = getJdbcTemplate().queryForObject("select  (COALESCE(Cast(Sum(mt.nqtyreceived)as decimal ("
//					+ inputMap.get("sprecision") + ")), 0)" + "	 -COALESCE(Cast(Sum(mt.nqtyissued)as decimal ("
//					+ inputMap.get("sprecision") + ")), 0) )::text"
//					+ " as navailableqty from materialinventorytransaction mt,materialinventory mi "
//					+ " where mi.nmaterialinventorycode = mt.nmaterialinventorycode and  mt.nmaterialinventorycode = "
//					+ inputMap.get("nmaterialinventorycode") + " " + " and mi.nstatus = "
//					+ Enumeration.TransactionStatus.ACTIVE.gettransactionstatus() + " and mt.nstatus= "
//					+ Enumeration.TransactionStatus.ACTIVE.gettransactionstatus() + ""
//					+ " and ((mi.jsondata->'ntranscode')::int != "
//					+ Enumeration.TransactionStatus.RETIRED.gettransactionstatus() + " and mt.nsectioncode="
//					+ sourceSection + "	and mt.nsitecode=" + userInfo.getNtranssitecode()
//					+ " and (mi.jsondata->'ntranscode')::int != "
//					+ Enumeration.TransactionStatus.EXPIRED.gettransactionstatus() + ")"
//					+ " and (mt.jsondata->'ntransactiontype')::int !="
//					+ Enumeration.TransactionStatus.USED.gettransactionstatus() + "; ", String.class);
			objmap.put("navailableqtyref", transtypechange);
		}

//		objmap.put("sourceSection", sourceSection);
		objmap.put("QuantityTransactionTemplate", lstMaterialConfig);
		return new ResponseEntity<>(objmap, HttpStatus.OK);
	}

	@SuppressWarnings("unchecked")
	public ResponseEntity<Object> getMaterialInventoryDetails(Map<String, Object> inputMap) throws Exception {

		List<Map<String, Object>> lstMaterialInventory = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> lstMaterialInventory1 = new ArrayList<Map<String, Object>>();

		Map<String, Object> objmap = new LinkedHashMap<String, Object>();

		List<Map<String, Object>> lstMaterialInventory2 = new ArrayList<Map<String, Object>>();

		List<MaterialInventory> objLstMaterialInventory = materialInventoryRepository
				.findByNmaterialcodeAndNmaterialcatcodeAndNmaterialtypecodeAndNstatusOrderByNmaterialinventorycodeDesc(
						(Integer) inputMap.get("nmaterialcode"), (Integer) inputMap.get("nmaterialcatcode"),
						(Integer) inputMap.get("nmaterialtypecode"), 1);

		objLstMaterialInventory.stream().peek(f -> {

			try {

				Map<String, Object> resObj = new ObjectMapper().readValue(f.getJsonuidata(), Map.class);

				resObj.put("nmaterialinventorycode", f.getNmaterialinventorycode());
				resObj.put("displaystatus",
						f.getNtransactionstatus() == 28 ? "Released"
								: (f.getNtransactionstatus() == 55 ? "Expired"
										: (f.getNtransactionstatus() == 37 ? "Quarantine" : "Retired")));
				lstMaterialInventory2.add(resObj);

			} catch (IOException e) {

				e.printStackTrace();
			}

		}).collect(Collectors.toList());

		objmap.put("MaterialInventory", lstMaterialInventory2);

		if (inputMap.get("nmaterialinventorycode") != null) {
			MaterialInventory objInventory = materialInventoryRepository
					.findByNmaterialinventorycode((Integer) inputMap.get("nmaterialinventorycode"));

			objmap.putAll((Map<String, Object>) getResultUsedMaterial((Integer) inputMap.get("nmaterialinventorycode"))
					.getBody());

			Map<String, Object> resObj = new ObjectMapper().readValue(objInventory.getJsonuidata(), Map.class);

			resObj.put("nmaterialinventorycode", (Integer) inputMap.get("nmaterialinventorycode"));

			lstMaterialInventory.add(resObj);

			lstMaterialInventory1.add(new ObjectMapper().readValue(objInventory.getJsondata(), Map.class));

			objmap.put("SelectedMaterialInventory", lstMaterialInventory.get(lstMaterialInventory.size() - 1));
			inputMap.put("nsectioncode", lstMaterialInventory1.get(0).get("nsectioncode"));

			objmap.putAll((Map<String, Object>) getQuantityTransactionByMaterialInvCode(
					(int) inputMap.get("nmaterialinventorycode"), inputMap).getBody());
		}

		return new ResponseEntity<>(objmap, HttpStatus.OK);
	}

	public ResponseEntity<Object> deleteMaterialInventory(Map<String, Object> inputMap) throws Exception {

		MaterialInventory objMaterialInventory = materialInventoryRepository
				.findByNmaterialinventorycodeAndNstatus((Integer) inputMap.get("nmaterialinventorycode"), 1);

		List<MaterialInventoryTransaction> lstTransactions = materialInventoryTransactionRepository
				.findByNmaterialinventorycodeOrderByNmaterialinventtranscode(
						(Integer) inputMap.get("nmaterialinventorycode"));

		if (objMaterialInventory != null) {

			objMaterialInventory.setNstatus(Enumeration.TransactionStatus.DELETED.gettransactionstatus());
			materialInventoryRepository.save(objMaterialInventory);

			lstTransactions.stream().peek(f -> {
				f.setNstatus(Enumeration.TransactionStatus.DELETED.gettransactionstatus());
			});

			materialInventoryTransactionRepository.save(lstTransactions);

			inputMap.put("nregtypecode", -1);
			inputMap.put("nregsubtypecode", -1);
			inputMap.put("ndesigntemplatemappingcode", inputMap.get("nmaterialconfigcode"));
			inputMap.put("tabScreen", "IDS_MATERIALSECTION");

			return getMaterialInventoryByID(inputMap);
		} else {
			return new ResponseEntity<>("IDS_ALREADYDELETED", HttpStatus.CONFLICT);
		}
	}

	@SuppressWarnings({ "unchecked" })
	public ResponseEntity<Object> updateMaterialStatus(Map<String, Object> inputMap) throws Exception {

		ObjectMapper objmapper = new ObjectMapper();
		JSONArray jsonAuditArraynew = new JSONArray();
		String jsonstr = "";
		JSONObject actionType = new JSONObject();
		String sdisplaystatus;
		Map<String, Object> objmap = new LinkedHashMap<String, Object>();
		List<String> lstDateField = new ArrayList<>();
		MaterialInventory objMaterialInventory = materialInventoryRepository
				.findByNmaterialinventorycodeAndNstatus((Integer) inputMap.get("nmaterialinventorycode"), 1);

		final LScfttransaction cft = objmapper.convertValue(inputMap.get("objsilentaudit"), LScfttransaction.class);
		if (objMaterialInventory != null) {

			jsonstr = objMaterialInventory.getJsondata();

			String jsonUistr = objMaterialInventory.getJsonuidata();

			int jsonint = (int) commonfunction.getInventoryValuesFromJsonString(jsonstr, "ntranscode").get("rtnObj");

			JSONObject jsonObject = new JSONObject(jsonstr.toString());
			JSONObject jsonuidata = new JSONObject(jsonUistr.toString());

			MaterialInventory matinv = new MaterialInventory();
			inputMap.put("nsectioncode", jsonuidata.get("nsectioncode"));

			if ((int) inputMap.get("nflag") == 1) {
				if (jsonint != Enumeration.TransactionStatus.EXPIRED.gettransactionstatus()) {
					if (jsonint != Enumeration.TransactionStatus.RETIRED.gettransactionstatus()) {
						if (jsonint != Enumeration.TransactionStatus.RELEASED.gettransactionstatus()) {

							sdisplaystatus = (String) commonfunction
									.getInventoryValuesFromJsonString(jsonstr, "stransdisplaystatus").get("rtnObj");

							jsonObject.put("ntranscode", Enumeration.TransactionStatus.RELEASED.gettransactionstatus());
							jsonuidata.put("ntranscode", Enumeration.TransactionStatus.RELEASED.gettransactionstatus());
							jsonuidata.put("sdisplaystatus", sdisplaystatus);

							objMaterialInventory.setJsondata(jsonObject.toString());
							objMaterialInventory.setJsonuidata(jsonuidata.toString());
							objMaterialInventory.setNtransactionstatus((Integer) jsonObject.get("ntranscode"));

							materialInventoryRepository.save(objMaterialInventory);

							inputMap.put("ntranscode", jsonObject.get("ntranscode"));
//							createMaterialInventoryhistory(inputMap);
							jsonAuditArraynew.put(jsonuidata);
							actionType.put("materialinventory", "IDS_MATERIALINVENTORYRELEASED");
						} else {
							matinv.setInfo("Material Inv : " + objMaterialInventory.getNmaterialinventorycode()
									+ "Already Released");
							matinv.setObjsilentaudit(cft);
//							return new ResponseEntity<>("IDS_ALREADYRELEASED", HttpStatus.ALREADY_REPORTED);
							return new ResponseEntity<>(matinv, HttpStatus.ALREADY_REPORTED);
						}
					} else {

						matinv.setInfo("Material Inv : " + objMaterialInventory.getNmaterialinventorycode()
								+ "Already retired");
						matinv.setObjsilentaudit(cft);
						// return new ResponseEntity<>("IDS_THEINVENTORYALREADYRETIRED",
						// HttpStatus.ALREADY_REPORTED);
						return new ResponseEntity<>(matinv, HttpStatus.ALREADY_REPORTED);

					}
				} else {

					matinv.setInfo(
							"Material Inv : " + objMaterialInventory.getNmaterialinventorycode() + "Already Expired");
					matinv.setObjsilentaudit(cft);
					// return new ResponseEntity<>("IDS_THEINVENTORYALREADYEXPIRED",
					// HttpStatus.ALREADY_REPORTED);
					return new ResponseEntity<>(matinv, HttpStatus.ALREADY_REPORTED);

				}
			} else if ((int) inputMap.get("nflag") == 2) {
				if (jsonint != Enumeration.TransactionStatus.EXPIRED.gettransactionstatus()) {
					if (jsonint != Enumeration.TransactionStatus.RETIRED.gettransactionstatus()) {

//						TransactionStatus objTransactionStatus = transactionStatusRepository
//								.findOne(Enumeration.TransactionStatus.RETIRED.gettransactionstatus());

						sdisplaystatus = "Retired";

						jsonObject.put("ntranscode", Enumeration.TransactionStatus.RETIRED.gettransactionstatus());
						jsonuidata.put("ntranscode", Enumeration.TransactionStatus.RETIRED.gettransactionstatus());
						jsonuidata.put("sdisplaystatus", sdisplaystatus);

						objMaterialInventory.setJsondata(jsonObject.toString());
						objMaterialInventory.setJsonuidata(jsonuidata.toString());
						objMaterialInventory.setNtransactionstatus((Integer) jsonObject.get("ntranscode"));
						objMaterialInventory.setObjsilentaudit(cft);
						materialInventoryRepository.save(objMaterialInventory);

						inputMap.put("ntranscode", jsonObject.get("ntranscode"));

						jsonAuditArraynew.put(jsonuidata);
						actionType.put("materialinventory", "IDS_MATERIALINVENTORYRETIRED");
					} else {

						matinv.setInfo("Material Inv : " + objMaterialInventory.getNmaterialinventorycode()
								+ "Already Retired");
						matinv.setObjsilentaudit(cft);
//						return new ResponseEntity<>("IDS_THEINVENTORYALREADYRETIRED", HttpStatus.ALREADY_REPORTED);
						return new ResponseEntity<>(matinv, HttpStatus.ALREADY_REPORTED);
					}
				} else {
					matinv.setInfo(
							"Material Inv : " + objMaterialInventory.getNmaterialinventorycode() + "Already Released");
					matinv.setObjsilentaudit(cft);

					return new ResponseEntity<>(matinv, HttpStatus.ALREADY_REPORTED);

				}
			} else if ((int) inputMap.get("nflag") == 3) {
				if (jsonint != Enumeration.TransactionStatus.QUARENTINE.gettransactionstatus()) {
					if (jsonint != Enumeration.TransactionStatus.RETIRED.gettransactionstatus()
							&& jsonint != Enumeration.TransactionStatus.EXPIRED.gettransactionstatus()) {
						if (inputMap.containsKey("Open Date")) {
							jsonObject.put("Open Date", inputMap.get("Open Date"));
							jsonObject.put("tzOpen Date", inputMap.get("tzOpen Date"));

							jsonuidata.put("Open Date", inputMap.get("Open Date"));
							jsonuidata.put("tzOpen Date", inputMap.get("tzOpen Date"));

							lstDateField.add("Open Date");
//							jsonObject = (JSONObject) convertInputDateToUTCByZone(jsonObject, lstDateField, false);
//							jsonuidata = (JSONObject) convertInputDateToUTCByZone(jsonuidata, lstDateField, false);
							jsonObject = getmaterialquery(jsonObject, 2);
							jsonuidata = getmaterialquery(jsonuidata, 2);
						}

						objMaterialInventory.setJsondata(jsonObject.toString());
						objMaterialInventory.setJsonuidata(jsonuidata.toString());

						materialInventoryRepository.save(objMaterialInventory);

						actionType.put("materialinventory", "IDS_OPENDATE");
					} else {

						matinv.setInfo("Material Inv : " + objMaterialInventory.getNmaterialinventorycode()
								+ "Already Released");
						matinv.setObjsilentaudit(cft);
						return new ResponseEntity<>(matinv, HttpStatus.ALREADY_REPORTED);
					}
				} else {

					matinv.setInfo("Select Released Record");
					matinv.setObjsilentaudit(cft);
					return new ResponseEntity<>(matinv, HttpStatus.ALREADY_REPORTED);
				}
			}

			objmap.put("nregtypecode", -1);
			objmap.put("nregsubtypecode", -1);
			objmap.put("ndesigntemplatemappingcode", jsonuidata.get("nmaterialconfigcode"));

			objmap.putAll((Map<String, Object>) getMaterialInventoryByID(inputMap).getBody());
			objmap.put("objsilentaudit", cft);
			objmap.put("tabScreen", "IDS_MATERIALSECTION");
			return new ResponseEntity<>(objmap, HttpStatus.OK);
		} else {
			return new ResponseEntity<>("IDS_MATERIALINVENTORYALREADYDELETED", HttpStatus.CONFLICT);
		}
	}

	public ResponseEntity<Object> getResultUsedMaterial(int nmaterialinventorycode) throws Exception {
		Map<String, Object> objmap = new LinkedHashMap<String, Object>();
		List<ResultUsedMaterial> lstResultUsedMaterial = resultUsedMaterialRepository
				.findByNinventorycodeOrderByNresultusedmaterialcodeDesc(nmaterialinventorycode);
		objmap.put("ResultUsedMaterial", lstResultUsedMaterial);
		return new ResponseEntity<>(objmap, HttpStatus.OK);
	}

	@SuppressWarnings({ "unused", "unchecked" })
	public ResponseEntity<Object> getMaterialInventoryEdit(Map<String, Object> inputMap) throws Exception {
		Map<String, Object> objmap = new LinkedHashMap<String, Object>();
		ObjectMapper Objmapper = new ObjectMapper();
		final List<Object> savedMaterialsList = new ArrayList<>();
		List<Map<String, Object>> lstMaterialInventory = new ArrayList<Map<String, Object>>();
		final List<Object> beforeMaterialsList = new ArrayList<>();
		String snhidescreencode = "";
		StringBuffer sB = new StringBuffer();
		final ObjectMapper objmapper = new ObjectMapper();

		MaterialInventory objMaterialInventory = materialInventoryRepository
				.findByNmaterialinventorycodeAndNstatus((Integer) inputMap.get("nmaterialinventorycode"), 1);

		if (objMaterialInventory != null) {

			String objMaterial = objMaterialInventory.getJsondata();

			List<MappedTemplateFieldPropsMaterial> lstsearchField = new LinkedList<>();

			List<String> lstsearchFields = new LinkedList<>();

			MappedTemplateFieldPropsMaterial objFieldPropsMaterial = mappedTemplateFieldPropsMaterialRepository
					.findByNmaterialconfigcodeAndNstatus(Enumeration.TransactionStatus.ACTIVE.gettransactionstatus(),
							1);

			lstsearchField = (List<MappedTemplateFieldPropsMaterial>) commonfunction
					.getInventoryValuesFromJsonString(objMaterialInventory.getJsondata(), "138").get("rtnObj");

			if (lstsearchField != null) {
				JSONArray objarray = new JSONArray(lstsearchField.get(0).getJsondata());
				for (int i = 0; i < objarray.length(); i++) {
					JSONObject jsonobject = new JSONObject(objarray.get(i).toString());
					if (jsonobject.has("2")) {
						lstsearchFields.add((String) jsonobject.get("2"));
					}
				}
				objmap.put("DateFeildsProperties", lstsearchFields);
			}

			List<String> submittedDateFeilds = new LinkedList<String>();

			Map<String, Object> resObj = new ObjectMapper().readValue(objMaterialInventory.getJsonuidata(), Map.class);

			resObj.put("nmaterialinventorycode", (Integer) inputMap.get("nmaterialinventorycode"));

			lstMaterialInventory.add(resObj);

			objmap.put("EditedMaterialInventory", lstMaterialInventory);

			if (!submittedDateFeilds.isEmpty()) {
				objmap.put("MaterialInventoryDateFeild", submittedDateFeilds);
			}
			return new ResponseEntity<>(objmap, HttpStatus.OK);
		} else {
			return new ResponseEntity<>("IDS_ALREADYDELETED", HttpStatus.CONFLICT);
		}
	}

	public Instant getCurrentDateTime() throws Exception {
		Instant date = null;

		date = LocalDateTime.now().toInstant(ZoneOffset.UTC).truncatedTo(ChronoUnit.SECONDS);

		return date;
	}

	public Object convertInputDateToUTCByZone(JSONObject jsonObj, final List<String> inputFieldList,
			final boolean returnAsString) throws Exception {

		DateFormat formatter;
		if (!returnAsString) {
			formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		} else {
			formatter = new SimpleDateFormat("yyyy-MM-dd");
		}

		for (int i = 0; i < inputFieldList.size(); i++) {

			String v_date_str = (String) jsonObj.get(inputFieldList.get(i));

			Date date_temp = formatter.parse(v_date_str);

			jsonObj.put(inputFieldList.get(i), date_temp);

		}

		return jsonObj;
	}
	
	public Object removeISTinDateString(JSONObject jsonObj, final List<String> inputFieldList, final boolean returnAsString) throws Exception {

		for (int i = 0; i < inputFieldList.size(); i++) {
			
			String v_date_str = jsonObj.get(inputFieldList.get(i)).toString().replace("IST","+0530");

			jsonObj.put(inputFieldList.get(i), v_date_str);

		}

		return jsonObj;
	}

	public int getCurrentDateTimeOffset(final String stimezoneid) throws Exception {

		ZoneId zone = ZoneId.of(stimezoneid);
		LocalDateTime dt = LocalDateTime.now();
		ZonedDateTime zdt = dt.atZone(zone);
		ZoneOffset offset = zdt.getOffset();
		int offSet = offset.getTotalSeconds();
		return offSet;
	}

	@SuppressWarnings("unchecked")
	public ResponseEntity<Object> getMaterialInventorySearchByID(Map<String, Object> inputMap)
			throws JsonParseException, JsonMappingException, IOException {

		Map<String, Object> objmap = new LinkedHashMap<String, Object>();
		List<Map<String, Object>> lstMaterialInventory = new ArrayList<Map<String, Object>>();

		MaterialInventory objMaterialInventory = materialInventoryRepository
				.findByNmaterialinventorycode((Integer) inputMap.get("nmaterialinventorycode"));

		Map<String, Object> resObj = new ObjectMapper().readValue(objMaterialInventory.getJsonuidata(), Map.class);

		resObj.put("nmaterialinventorycode", objMaterialInventory.getNmaterialinventorycode());

		lstMaterialInventory.add(resObj);

		objmap.put("SelectedMaterialInventory", lstMaterialInventory.get(lstMaterialInventory.size() - 1));
		inputMap.put("nsectioncode", lstMaterialInventory.get(lstMaterialInventory.size() - 1).get("nsectioncode"));
		objmap.putAll((Map<String, Object>) getQuantityTransactionByMaterialInvCode(
				(int) inputMap.get("nmaterialinventorycode"), inputMap).getBody());
//		objmap.putAll((Map<String, Object>) getMaterialFile((int) inputMap.get("nmaterialinventorycode"), userInfo));
//		objmap.putAll(
//				(Map<String, Object>) getResultUsedMaterial((int) inputMap.get("nmaterialinventorycode"), userInfo)
//						.getBody());
//		objmap.putAll((Map<String, Object>) getMaterialInventoryhistory((int) inputMap.get("nmaterialinventorycode"),
//				userInfo));
		return new ResponseEntity<>(objmap, HttpStatus.OK);
	}

	public MaterialInventory CloudUploadattachments(MultipartFile file, Integer nmaterialinventorycode, String filename,
			String fileexe, Integer usercode, Date currentdate, Integer isMultitenant) throws IOException {
		MaterialInventory objmaterialinv = materialInventoryRepository.findOne(nmaterialinventorycode);
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
		objattachment.setNmaterialinventorycode(objmaterialinv.getNmaterialinventorycode());
		if (objmaterialinv != null && objmaterialinv.getLsOrderattachments() != null) {
			objmaterialinv.getLsOrderattachments().add(objattachment);
		} else {
			objmaterialinv.setLsOrderattachments(new ArrayList<LsOrderattachments>());
			objmaterialinv.getLsOrderattachments().add(objattachment);
		}
		lsOrderattachmentsRepository.save(objmaterialinv.getLsOrderattachments());
		if (isMultitenant != 0) {
			String filenameval = "attach_" + objmaterialinv.getNmaterialcode() + "_"
					+ objmaterialinv.getLsOrderattachments()
							.get(objmaterialinv.getLsOrderattachments().lastIndexOf(objattachment)).getAttachmentcode()
					+ "_" + filename;
			String id = cloudFileManipulationservice.storeLargeattachment(filenameval, file);
			if (id != null) {
				objattachment.setFileid(id);
			}

			lsOrderattachmentsRepository.save(objmaterialinv.getLsOrderattachments());
		}

		return objmaterialinv;
	}

	public Map<String, Object> getAttachments(Map<String, Object> objMap) {
		Map<String, Object> obj = new HashMap<>();
		List<LsOrderattachments> lstattach = lsOrderattachmentsRepository
				.findByNmaterialinventorycodeOrderByAttachmentcodeDesc(objMap.get("nmaterialinventorycode"));
		obj.put("lsOrderattachments", lstattach);
		return obj;

	}

	public String ReplaceQuote(String str) {
		if (str != null) {
			str = str.trim().replace("'", "''");
		}
		return str;
	}
}
