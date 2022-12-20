package com.agaram.eln.primary.service.material;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.agaram.eln.primary.commonfunction.commonfunction;
import com.agaram.eln.primary.global.Enumeration;
import com.agaram.eln.primary.model.material.MappedTemplateFieldPropsMaterial;
import com.agaram.eln.primary.model.material.Material;
import com.agaram.eln.primary.model.material.MaterialCategory;
import com.agaram.eln.primary.model.material.MaterialConfig;
import com.agaram.eln.primary.model.material.MaterialInventory;
import com.agaram.eln.primary.model.material.MaterialInventoryTransaction;
import com.agaram.eln.primary.model.material.MaterialType;
import com.agaram.eln.primary.repository.material.MappedTemplateFieldPropsMaterialRepository;
import com.agaram.eln.primary.repository.material.MaterialCategoryRepository;
import com.agaram.eln.primary.repository.material.MaterialConfigRepository;
import com.agaram.eln.primary.repository.material.MaterialInventoryRepository;
import com.agaram.eln.primary.repository.material.MaterialInventoryTransactionRepository;
import com.agaram.eln.primary.repository.material.MaterialRepository;
import com.agaram.eln.primary.repository.material.MaterialTypeRepository;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class TransactionService {

	@Autowired
	private MaterialTypeRepository materialTypeRepository;

	@Autowired
	private MaterialConfigRepository materialConfigRepository;
	
	@Autowired
	private MappedTemplateFieldPropsMaterialRepository mappedTemplateFieldPropsMaterialRepository;

	@Autowired
	private MaterialCategoryRepository materialCategoryRepository;

	@Autowired
	private MaterialRepository materialRepository;

	@Autowired
	private MaterialInventoryRepository materialInventoryRepository;

	@Autowired
	private MaterialInventoryTransactionRepository materialInventoryTransactionRepository;

	@Autowired
	private MaterialInventoryService materialInventoryService;

	public ResponseEntity<Object> getLoadOnInventoryData(Map<String, Object> inputMap) {

		Map<String, Object> rtnMap = new HashMap<String, Object>();

		List<MaterialType> lstTypes = new ArrayList<MaterialType>();
		List<MaterialCategory> lstCategories = new ArrayList<MaterialCategory>();
		List<Material> lstMaterials = new ArrayList<Material>();
		List<MaterialInventory> lstMaterialInventories = new ArrayList<MaterialInventory>();

		if (inputMap.containsKey("nFlag")) {
			/**
			 * in initial load all values by default
			 */
			if ((Integer) inputMap.get("nFlag") == 1) {

				lstTypes = materialTypeRepository.findByNstatusOrderByNmaterialtypecode(1);

				if (!lstTypes.isEmpty()) {
					lstCategories = materialCategoryRepository
							.findByNmaterialtypecodeOrderByNmaterialcatcode(lstTypes.get(0).getNmaterialtypecode());

					if (!lstCategories.isEmpty()) {

						lstMaterials = materialRepository
								.findByNmaterialcatcodeOrderByNmaterialcode(lstCategories.get(0).getNmaterialcatcode());

						if (!lstMaterials.isEmpty()) {
							lstMaterialInventories = materialInventoryRepository
									.findByNmaterialcodeAndNtransactionstatusOrderByNmaterialinventorycode(
											lstMaterials.get(0).getNmaterialcode(), 28);
						}

					}
				}

			} /**
				 * in initial load all values by nmaterialtypecode
				 */
			else if ((Integer) inputMap.get("nFlag") == 2) {

				Integer nTypeCode = (Integer) inputMap.get("nmaterialtypecode");

				lstCategories = materialCategoryRepository.findByNmaterialtypecodeOrderByNmaterialcatcode(nTypeCode);

				if (!lstCategories.isEmpty()) {

					lstMaterials = materialRepository
							.findByNmaterialcatcodeOrderByNmaterialcode(lstCategories.get(0).getNmaterialcatcode());

					if (!lstMaterials.isEmpty()) {
						lstMaterialInventories = materialInventoryRepository
								.findByNmaterialcodeAndNtransactionstatusOrderByNmaterialinventorycode(
										lstMaterials.get(0).getNmaterialcode(), 28);
					}

				}
			} /**
				 * in initial load all values by nmaterialcategorycode
				 */
			else if ((Integer) inputMap.get("nFlag") == 3) {

				Integer nCategoryCode = (Integer) inputMap.get("nmaterialtypecode");

				lstMaterials = materialRepository.findByNmaterialcatcodeOrderByNmaterialcode(nCategoryCode);

				if (!lstMaterials.isEmpty()) {
					lstMaterialInventories = materialInventoryRepository
							.findByNmaterialcodeAndNtransactionstatusOrderByNmaterialinventorycode(
									lstMaterials.get(0).getNmaterialcode(), 28);
				}

			} /**
				 * in initial load all values by nmaterialcode
				 */
			else if ((Integer) inputMap.get("nFlag") == 4) {

				Integer nmaterialCode = (Integer) inputMap.get("nmaterialcode");

				lstMaterialInventories = materialInventoryRepository
						.findByNmaterialcodeAndNtransactionstatusOrderByNmaterialinventorycode(nmaterialCode, 28);

			} /**
				 * in initial load all values by nmaterialinventorycode
				 */
			else if ((Integer) inputMap.get("nFlag") == 5) {

				Integer nmaterialinventorycode = (Integer) inputMap.get("nmaterialinventorycode");

				lstMaterialInventories.add(materialInventoryRepository
						.findByNmaterialinventorycodeAndNtransactionstatus(nmaterialinventorycode, 28));

			}
		}

		rtnMap.put("selectedMaterialType", lstTypes);
		rtnMap.put("selectedMaterialCategory", lstCategories);
		rtnMap.put("selectedMaterial", lstMaterials);
		rtnMap.put("selectedMaterialInventory", lstMaterialInventories);
		rtnMap.put("nFalg", (Integer) inputMap.get("nFlag"));

		return new ResponseEntity<>(rtnMap, HttpStatus.OK);
	}

	@SuppressWarnings("unchecked")
	public ResponseEntity<Object> getInventoryTransaction(Map<String, Object> inputMap)
			throws JsonParseException, JsonMappingException, IOException {

		List<Map<String, Object>> lstMaterialInventoryTrans = new ArrayList<Map<String, Object>>();

		List<MaterialInventoryTransaction> lstInventoryTransaction = materialInventoryTransactionRepository
				.findByNmaterialinventorycodeOrderByNmaterialinventtranscodeDesc(
						(Integer) inputMap.get("nmaterialinventorycode"));

		Map<String, Object> mapJsonData = new ObjectMapper().readValue(lstInventoryTransaction.get(0).getJsonuidata(),
				Map.class);

		Map<String, Object> objContent = commonfunction
				.getInventoryValuesFromJsonString(lstInventoryTransaction.get(0).getJsonuidata(), "namountleft");
		mapJsonData.put("Available Quantity", objContent.get("rtnObj"));
		objContent = commonfunction.getInventoryValuesFromJsonString(lstInventoryTransaction.get(0).getJsonuidata(),
				"nqtyissued");
		mapJsonData.put("Issued Quantity", objContent.get("rtnObj"));
		mapJsonData.put("Received Quantity", lstInventoryTransaction.get(0).getNqtyreceived());

		lstMaterialInventoryTrans.add(mapJsonData);

		return new ResponseEntity<>(lstMaterialInventoryTrans, HttpStatus.OK);
	}

	@SuppressWarnings("unchecked")
	public ResponseEntity<Object> createMaterialInventoryTrans(Map<String, Object> inputMap)
			throws JSONException, Exception {
		Map<String, Object> objmap = new LinkedHashMap<String, Object>();
		ObjectMapper Objmapper = new ObjectMapper();
		JSONObject json = new JSONObject();
		String sectionDescription = "";

//		List<String> lstDateField = (List<String>) inputMap.get("DateList");

		List<MaterialInventoryTransaction> lstTransaction = materialInventoryTransactionRepository
				.findByNmaterialinventorycodeOrderByNmaterialinventtranscodeDesc(
						(Integer) inputMap.get("nmaterialinventorycode"));

		final String dtransactiondate = "dd-mm-yy";

		JSONObject insJsonObj = new JSONObject(inputMap.get("MaterialInventoryTrans").toString());
		JSONObject jsonuidata = new JSONObject(inputMap.get("jsonuidata").toString());

		insJsonObj.put("Transaction Date & Time", dtransactiondate);
		insJsonObj.put("noffsetTransaction Date & Time", commonfunction.getCurrentDateTimeOffset("Europe/London"));
		jsonuidata.put("Transaction Date & Time", dtransactiondate);
		jsonuidata.put("noffsetTransaction Date & Time", commonfunction.getCurrentDateTimeOffset("Europe/London"));

		if ((int) insJsonObj.get("ntransactiontype") == Enumeration.TransactionStatus.ISSUE.gettransactionstatus()
				|| (int) insJsonObj.get("ntransactiontype") == Enumeration.TransactionStatus.REJECTED
						.gettransactionstatus()) {
			double namountleft = Double.parseDouble((String) insJsonObj.get("navailableqty"))
					- Double.parseDouble((String) insJsonObj.get("Received Quantity"));

			String amtleft = new Double(namountleft).toString();

			insJsonObj.put("namountleft", amtleft);
			jsonuidata.put("namountleft", amtleft);

		} else if ((int) insJsonObj.get("ntransactiontype") == Enumeration.TransactionStatus.RETURN
				.gettransactionstatus()) {

			double namountleft = new Double(
					(commonfunction.getInventoryValuesFromJsonString(lstTransaction.get(0).getJsondata(), "namountleft")
							.get("rtnObj").toString()));

			String amtleft = new Double(namountleft).toString();

			insJsonObj.put("namountleft", amtleft);
			jsonuidata.put("namountleft", amtleft);
		} else {
			double availqty = Double.parseDouble((String) insJsonObj.get("navailableqty"));
			double namountleft = availqty + Double.parseDouble((String) insJsonObj.get("Received Quantity"));

			insJsonObj.put("namountleft", namountleft);
			jsonuidata.put("namountleft", namountleft);
		}

		if ((int) insJsonObj.get("ntransactiontype") == Enumeration.TransactionStatus.ISSUE.gettransactionstatus()
				|| (int) insJsonObj.get("ntransactiontype") == Enumeration.TransactionStatus.REJECTED
						.gettransactionstatus()) {
			if (insJsonObj.get("Received Quantity").toString().contains(".")) {
				insJsonObj.put("nqtyissued", Double.parseDouble(insJsonObj.get("Received Quantity").toString()));
				jsonuidata.put("nqtyissued", Double.parseDouble(insJsonObj.get("Received Quantity").toString()));
			} else {
				insJsonObj.put("nqtyissued", Integer.parseInt(insJsonObj.get("Received Quantity").toString()));
				jsonuidata.put("nqtyissued", Integer.parseInt(insJsonObj.get("Received Quantity").toString()));
			}
			insJsonObj.put("Received Quantity", 0);
			jsonuidata.put("Received Quantity", 0);
		} else if ((int) insJsonObj.get("ntransactiontype") == Enumeration.TransactionStatus.RECEIVE
				.gettransactionstatus()
				|| (int) insJsonObj.get("ntransactiontype") == Enumeration.TransactionStatus.RETURN
						.gettransactionstatus()) {
			if (insJsonObj.get("Received Quantity").toString().contains(".")) {
				insJsonObj.put("Received Quantity", Double.parseDouble(insJsonObj.get("Received Quantity").toString()));
				jsonuidata.put("Received Quantity", Double.parseDouble(jsonuidata.get("Received Quantity").toString()));
			} else {
				insJsonObj.put("Received Quantity", Integer.parseInt(insJsonObj.get("Received Quantity").toString()));
				jsonuidata.put("Received Quantity", Integer.parseInt(jsonuidata.get("Received Quantity").toString()));
			}
			insJsonObj.put("nqtyissued", 0);
			jsonuidata.put("nqtyissued", 0);

		}
//		insJsonObj = (JSONObject) commonfunction.convertInputDateToUTCByZone(insJsonObj, lstDateField, false);
//		jsonuidata = (JSONObject) commonfunction.convertInputDateToUTCByZone(jsonuidata, lstDateField, false);

		List<MaterialInventoryTransaction> lstsourceSection = materialInventoryTransactionRepository
				.findByNmaterialinventorycodeOrderByNmaterialinventtranscode(
						(Integer) inputMap.get("nmaterialinventorycode"));

		JSONObject insJsonObjcopy = new JSONObject(insJsonObj.toString());
		JSONObject jsonUidataObjcopy = new JSONObject(jsonuidata.toString());

		insJsonObjcopy.put("Transaction Date & Time", dtransactiondate);
		insJsonObjcopy.put("noffsetTransaction Date & Time", commonfunction.getCurrentDateTimeOffset("Europe/London"));
		jsonUidataObjcopy.put("Transaction Date & Time", dtransactiondate);
		jsonUidataObjcopy.put("noffsetTransaction Date & Time",
				commonfunction.getCurrentDateTimeOffset("Europe/London"));

		if ((int) inputMap.get("needsectionwise") == Enumeration.TransactionStatus.NO.gettransactionstatus()) {

			insJsonObjcopy.put("Section", new JSONObject("{\"label\":\"Default\",\"value\":\"-1\"}"));
			jsonUidataObjcopy.put("Section", "Default");
			sectionDescription = "";
		} else {

			Map<String, Object> map = (Map<String, Object>) commonfunction
					.getInventoryValuesFromJsonString(lstsourceSection.get(0).getJsondata(), "Section").get("rtnObj");

			insJsonObjcopy.put("Section", new JSONObject(Objmapper.writeValueAsString(map)));
			jsonUidataObjcopy.put("Section", new JSONObject(Objmapper.writeValueAsString(map)).get("label"));
			sectionDescription = new JSONObject(Objmapper.writeValueAsString(map)).get("label").toString();

		}
		if ((int) insJsonObj.get("ninventorytranscode") == Enumeration.TransactionStatus.INHOUSE
				.gettransactionstatus()) {
			json.put("label", "Received");
			json.put("value", Enumeration.TransactionStatus.RECEIVE.gettransactionstatus());

			if ((int) insJsonObj.get("ntransactiontype") == Enumeration.TransactionStatus.ISSUE
					.gettransactionstatus()) {
				jsonUidataObjcopy.put("Description",
						new JSONObject(insJsonObjcopy.get("Transaction Type").toString()).get("transactionname")
								+ "IDS_FROM " + sectionDescription + "IDS_TO"
								+ new JSONObject(insJsonObj.get("Section").toString()).get("label"));

				jsonuidata.put("Description",
						"IDS_RECEIVED IDS_BY " + new JSONObject(insJsonObj.get("Section").toString()).get("label")
								+ "IDS_FROM " + sectionDescription);

				MaterialInventoryTransaction objTransaction = new MaterialInventoryTransaction();

				objTransaction.setJsondata(insJsonObjcopy.toString());
				objTransaction.setJsonuidata(jsonUidataObjcopy.toString());
				objTransaction.setNstatus(Enumeration.TransactionStatus.ACTIVE.gettransactionstatus());
				objTransaction.setNmaterialinventorycode((Integer) inputMap.get("nmaterialinventorycode"));
				objTransaction.setNinventorytranscode((Integer) insJsonObj.get("ninventorytranscode"));
				objTransaction.setNtransactiontype((Integer) insJsonObj.get("ntransactiontype"));
//				objTransaction.setNsectioncode(
//						(Integer) (insJsonObj.get("nsectioncode") != null ? (Integer) insJsonObj.get("nsectioncode")
//								: -1));
				objTransaction.setNsectioncode(-1);
				objTransaction.setNsitecode(-1);
				objTransaction.setNresultusedmaterialcode(-1);
				objTransaction.setNqtyreceived(Double.valueOf((Integer) insJsonObj.get("Received Quantity")));
				objTransaction.setNqtyissued(Double.valueOf((Integer) insJsonObj.get("nqtyissued")));

				materialInventoryTransactionRepository.save(objTransaction);

				inputMap.put("nsectioncode", lstsourceSection.get(0).getNsectioncode());
//				jsonuidataArray.put(jsonUidataObjcopy);
			} else {
				jsonuidata.put("Description",
						new JSONObject(insJsonObjcopy.get("Transaction Type").toString()).get("transactionname")
								+ " IDS_FROM " + new JSONObject(insJsonObj.get("Section").toString()).get("label") + " "
								+ "IDS_TO  " + sectionDescription);
				jsonUidataObjcopy.put("Description", "IDS_RECEIVED IDS_BY " + sectionDescription + " DS_FROM  "
						+ new JSONObject(insJsonObj.get("Section").toString()).get("label"));

				MaterialInventoryTransaction objTransaction = new MaterialInventoryTransaction();

				objTransaction.setJsondata(insJsonObjcopy.toString());
				objTransaction.setJsonuidata(jsonUidataObjcopy.toString());
				objTransaction.setNstatus(Enumeration.TransactionStatus.ACTIVE.gettransactionstatus());
				objTransaction.setNmaterialinventorycode((Integer) inputMap.get("nmaterialinventorycode"));
				objTransaction.setNinventorytranscode((Integer) insJsonObj.get("ninventorytranscode"));
				objTransaction.setNtransactiontype((Integer) insJsonObj.get("ntransactiontype"));
				objTransaction.setNsectioncode(
						(Integer) (insJsonObj.get("nsectioncode") != null ? (Integer) insJsonObj.get("nsectioncode")
								: -1));
				objTransaction.setNsitecode(-1);
				objTransaction.setNresultusedmaterialcode(-1);
				objTransaction.setNqtyreceived(Double.valueOf((Integer) insJsonObj.get("Received Quantity")));
				objTransaction.setNqtyissued(Double.valueOf((Integer) insJsonObj.get("nqtyissued")));

				materialInventoryTransactionRepository.save(objTransaction);

				inputMap.put("nsectioncode", lstsourceSection.get(0).getNsectioncode());
//				jsonuidataArray.put(jsonUidataObjcopy);
			}

		} else {
			jsonUidataObjcopy.put("Description",
					new JSONObject(insJsonObjcopy.get("Transaction Type").toString()).get("transactionname")
							+ " IDS_BY " + sectionDescription);

			MaterialInventoryTransaction objTransaction = new MaterialInventoryTransaction();

			objTransaction.setJsondata(insJsonObjcopy.toString());
			objTransaction.setJsonuidata(jsonUidataObjcopy.toString());
			objTransaction.setNstatus(Enumeration.TransactionStatus.ACTIVE.gettransactionstatus());
			objTransaction.setNmaterialinventorycode((Integer) inputMap.get("nmaterialinventorycode"));
			objTransaction.setNinventorytranscode((Integer) insJsonObj.get("ninventorytranscode"));
			objTransaction.setNtransactiontype((Integer) insJsonObj.get("ntransactiontype"));
			objTransaction.setNsectioncode(
					(Integer) (insJsonObj.get("nsectioncode") != null ? (Integer) insJsonObj.get("nsectioncode") : -1));
			objTransaction.setNsitecode(-1);
			objTransaction.setNresultusedmaterialcode(-1);
			objTransaction.setNqtyreceived(Double.valueOf((Integer) insJsonObj.get("Received Quantity")));
			objTransaction.setNqtyissued(Double.valueOf((Integer) insJsonObj.get("nqtyissued")));

			materialInventoryTransactionRepository.save(objTransaction);
			inputMap.put("nsectioncode", insJsonObj.get("nsectioncode"));
//			jsonuidataArray.put(jsonUidataObjcopy);
		}
		objmap.put("nregtypecode", -1);
		objmap.put("nregsubtypecode", -1);

		objmap.putAll((Map<String, Object>) materialInventoryService
				.getQuantityTransactionByMaterialInvCode((int) inputMap.get("nmaterialinventorycode"), inputMap)
				.getBody());
		return new ResponseEntity<>(objmap, HttpStatus.OK);
	}

	public ResponseEntity<Object> updateMaterialDynamicTable(MaterialConfig[] objLstClass)
			throws JsonParseException, JsonMappingException, IOException {

		List<MaterialConfig> lstConfig = Arrays.asList(objLstClass);

		lstConfig.stream().peek(f -> {

			MaterialConfig objConfig = new MaterialConfig();

			objConfig.setNformcode(f.getNformcode());
			objConfig.setNmaterialtypecode(f.getNmaterialtypecode());
			objConfig.setNmaterialconfigcode(f.getNmaterialconfigcode());
			objConfig.setNstatus(1);
			objConfig.setJsondata(f.getJsondata());

			materialConfigRepository.save(objConfig);

		}).collect(Collectors.toList());

		return new ResponseEntity<>("Material Configuration updated successfully", HttpStatus.OK);

	}

	public ResponseEntity<Object> updateMappedTemplateFieldPropsMaterialTable(
			MappedTemplateFieldPropsMaterial[] objLstClass) {
		
		List<MappedTemplateFieldPropsMaterial> lstMappedProps = Arrays.asList(objLstClass);
		
		lstMappedProps.stream().peek(f -> {
			
			MappedTemplateFieldPropsMaterial objFieldPropsMaterial = new MappedTemplateFieldPropsMaterial();
			
			objFieldPropsMaterial.setJsondata(f.getJsondata());
			objFieldPropsMaterial.setNstatus(1);
			objFieldPropsMaterial.setNmaterialconfigcode(f.getNmaterialconfigcode());
			objFieldPropsMaterial.setNmappedtemplatefieldpropmaterialcode(f.getNmappedtemplatefieldpropmaterialcode());
			
			mappedTemplateFieldPropsMaterialRepository.save(objFieldPropsMaterial);
			
		}).collect(Collectors.toList());

//		mappedTemplateFieldPropsMaterialRepository.save(lstMappedProps);

		return new ResponseEntity<>("Material Properties updated successfully", HttpStatus.OK);

	}
}