package com.agaram.eln.primary.service.material;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.agaram.eln.primary.commonfunction.commonfunction;
import com.agaram.eln.primary.global.Enumeration;
import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.material.MappedTemplateFieldPropsMaterial;
import com.agaram.eln.primary.model.material.Material;
import com.agaram.eln.primary.model.material.MaterialCategory;
import com.agaram.eln.primary.model.material.MaterialConfig;
import com.agaram.eln.primary.model.material.MaterialInventory;
import com.agaram.eln.primary.model.material.MaterialInventoryTransaction;
import com.agaram.eln.primary.model.material.MaterialType;
import com.agaram.eln.primary.model.material.ResultUsedMaterial;
import com.agaram.eln.primary.model.sheetManipulation.LStestmasterlocal;
import com.agaram.eln.primary.model.usermanagement.LSnotification;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.repository.material.MappedTemplateFieldPropsMaterialRepository;
import com.agaram.eln.primary.repository.material.MaterialCategoryRepository;
import com.agaram.eln.primary.repository.material.MaterialConfigRepository;
import com.agaram.eln.primary.repository.material.MaterialInventoryRepository;
import com.agaram.eln.primary.repository.material.MaterialInventoryTransactionRepository;
import com.agaram.eln.primary.repository.material.MaterialRepository;
import com.agaram.eln.primary.repository.material.MaterialTypeRepository;
import com.agaram.eln.primary.repository.material.ResultUsedMaterialRepository;
import com.agaram.eln.primary.repository.usermanagement.LSnotificationRepository;
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

	@Autowired
	private LSnotificationRepository lsnotificationRepository;

	@Autowired
	private ResultUsedMaterialRepository resultUsedMaterialRepository;

	public ResponseEntity<Object> getLoadOnInventoryData(Map<String, Object> inputMap) {

		Map<String, Object> rtnMap = new HashMap<String, Object>();

		List<MaterialType> lstTypes = new ArrayList<MaterialType>();
		List<MaterialCategory> lstCategories = new ArrayList<MaterialCategory>();
		List<Material> lstMaterials = new ArrayList<Material>();
		List<MaterialInventory> lstMaterialInventories = new ArrayList<MaterialInventory>();

		Integer nsiteInteger = (Integer) inputMap.get("sitecode");

		if (inputMap.containsKey("nFlag")) {
			/**
			 * in initial load all values by default
			 */
			if ((Integer) inputMap.get("nFlag") == 1) {

//				lstTypes = materialTypeRepository.findByNmaterialtypecodeNotAndNstatusOrderByNmaterialtypecode(-1, 1);

				lstTypes = materialTypeRepository.findByNstatusOrderByNmaterialtypecode(1);

				if (!lstTypes.isEmpty()) {
					lstCategories = materialCategoryRepository
							.findByNmaterialtypecodeAndNsitecodeOrderByNmaterialcatcodeDesc(
									lstTypes.get(0).getNmaterialtypecode(), nsiteInteger);

					if (!lstCategories.isEmpty()) {

						lstMaterials = materialRepository.findByNmaterialcatcodeAndNsitecodeOrderByNmaterialcodeDesc(
								lstCategories.get(0).getNmaterialcatcode(), nsiteInteger);

						if (!lstMaterials.isEmpty()) {
							lstMaterialInventories = materialInventoryRepository
									.findByNmaterialcodeAndNtransactionstatusOrderByNmaterialinventorycodeDesc(
											lstMaterials.get(0).getNmaterialcode(), 28);
						}

					}
				}

			} /**
				 * in initial load all values by nmaterialtypecode
				 */
			else if ((Integer) inputMap.get("nFlag") == 2) {

				Integer nTypeCode = (Integer) inputMap.get("nmaterialtypecode");

				lstCategories = materialCategoryRepository
						.findByNmaterialtypecodeAndNsitecodeOrderByNmaterialcatcodeDesc(nTypeCode, nsiteInteger);

				if (!lstCategories.isEmpty()) {

					lstMaterials = materialRepository.findByNmaterialcatcodeAndNsitecodeOrderByNmaterialcodeDesc(
							lstCategories.get(0).getNmaterialcatcode(), nsiteInteger);

					if (!lstMaterials.isEmpty()) {
						lstMaterialInventories = materialInventoryRepository
								.findByNmaterialcodeAndNtransactionstatusOrderByNmaterialinventorycodeDesc(
										lstMaterials.get(0).getNmaterialcode(), 28);
					}

				}
			} /**
				 * in initial load all values by nmaterialcategorycode
				 */
			else if ((Integer) inputMap.get("nFlag") == 3) {

				Integer nCategoryCode = (Integer) inputMap.get("nmaterialcatcode");

				lstMaterials = materialRepository.findByNmaterialcatcodeOrderByNmaterialcodeDesc(nCategoryCode);

				if (!lstMaterials.isEmpty()) {
					lstMaterialInventories = materialInventoryRepository
							.findByNmaterialcodeAndNtransactionstatusOrderByNmaterialinventorycodeDesc(
									lstMaterials.get(0).getNmaterialcode(), 28);
				}

			} /**
				 * in initial load all values by nmaterialcode
				 */
			else if ((Integer) inputMap.get("nFlag") == 4) {

				Integer nmaterialCode = (Integer) inputMap.get("nmaterialcode");

				lstMaterialInventories = materialInventoryRepository
						.findByNmaterialcodeAndNtransactionstatusOrderByNmaterialinventorycodeDesc(nmaterialCode, 28);

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
		MaterialInventory objInventory = materialInventoryRepository
				.findByNmaterialinventorycodeAndNtransactionstatusNot((Integer) inputMap.get("nmaterialinventorycode"),
						55);

		Map<String, Object> mapJsonData = new ObjectMapper().readValue(lstInventoryTransaction.get(0).getJsonuidata(),
				Map.class);

		Double totalQuantity = Double.parseDouble(
				commonfunction.getInventoryValuesFromJsonString(objInventory.getJsondata(), "Received Quantity")
						.get("rtnObj").toString());

		List<Double> nqtyreceivedlst = lstInventoryTransaction.stream()
				.map(MaterialInventoryTransaction::getNqtyreceived).collect(Collectors.toList());
		List<Double> nqtyissuedLst = lstInventoryTransaction.stream().map(MaterialInventoryTransaction::getNqtyissued)
				.collect(Collectors.toList());

		double nqtyreceived = nqtyreceivedlst.stream().mapToDouble(Double::doubleValue).sum();
		double nqtyissued = nqtyissuedLst.stream().mapToDouble(Double::doubleValue).sum();

		Double availabledQuantity = nqtyreceived - nqtyissued;
		mapJsonData.put("Available Quantity", availabledQuantity);

		if (totalQuantity == availabledQuantity) {
			mapJsonData.put("Issued Quantity", availabledQuantity);
		} else {
			mapJsonData.put("Issued Quantity", totalQuantity - availabledQuantity);
		}

//		List<ResultUsedMaterial> lstUsedMaterials = resultUsedMaterialRepository.findByNinventorycodeOrderByNresultusedmaterialcodeDesc((Integer) inputMap.get("nmaterialinventorycode"));
//
//		if(!lstUsedMaterials.isEmpty()) {
//			mapJsonData.put("Issued Quantity", lstUsedMaterials.get(0).getNqtyleft());
//		}

		mapJsonData.put("Total Quantity", totalQuantity);
		mapJsonData.put("Received Quantity", lstInventoryTransaction.get(0).getNqtyreceived());
		mapJsonData.put("NotificationQty", objInventory.getNqtynotification());

		lstMaterialInventoryTrans.add(mapJsonData);

		return new ResponseEntity<>(lstMaterialInventoryTrans, HttpStatus.OK);
	}

	public ResponseEntity<Object> getResultInventoryTransaction(Map<String, Object> inputMap)
			throws JsonParseException, JsonMappingException, IOException {

		List<Map<String, Object>> lstMaterialInventoryTrans = new ArrayList<Map<String, Object>>();
		List<MaterialInventoryTransaction> lstInventoryTransaction = materialInventoryTransactionRepository
				.findByNmaterialinventorycodeOrderByNmaterialinventtranscodeDesc(
						(Integer) inputMap.get("nmaterialinventorycode"));
		MaterialInventory objInventory = materialInventoryRepository
				.findByNmaterialinventorycodeAndNtransactionstatusNot((Integer) inputMap.get("nmaterialinventorycode"),
						55);

		@SuppressWarnings("unchecked")
		Map<String, Object> mapJsonData = new ObjectMapper().readValue(lstInventoryTransaction.get(0).getJsonuidata(),
				Map.class);

		Double totalQuantity = Double.parseDouble(
				commonfunction.getInventoryValuesFromJsonString(objInventory.getJsondata(), "Received Quantity")
						.get("rtnObj").toString());

		List<Double> nqtyreceivedlst = lstInventoryTransaction.stream()
				.map(MaterialInventoryTransaction::getNqtyreceived).collect(Collectors.toList());
		List<Double> nqtyissuedLst = lstInventoryTransaction.stream().map(MaterialInventoryTransaction::getNqtyissued)
				.collect(Collectors.toList());

		double nqtyreceived = nqtyreceivedlst.stream().mapToDouble(Double::doubleValue).sum();
		double nqtyissued = nqtyissuedLst.stream().mapToDouble(Double::doubleValue).sum();

		Double availabledQuantity = nqtyreceived - nqtyissued;
		mapJsonData.put("Available Quantity", availabledQuantity);

		List<ResultUsedMaterial> lstUsedMaterials = resultUsedMaterialRepository
				.findByNinventorycodeOrderByNresultusedmaterialcodeDesc(
						(Integer) inputMap.get("nmaterialinventorycode"));

		if (!lstUsedMaterials.isEmpty()) {
			mapJsonData.put("Issued Quantity", lstUsedMaterials.get(0).getNqtyleft());
		} else {
			if (totalQuantity == availabledQuantity) {
				mapJsonData.put("Issued Quantity", availabledQuantity);
			} else {
				mapJsonData.put("Issued Quantity", totalQuantity - availabledQuantity);
			}
		}

		mapJsonData.put("Total Quantity", totalQuantity);
		mapJsonData.put("Received Quantity", lstInventoryTransaction.get(0).getNqtyreceived());
		mapJsonData.put("NotificationQty", objInventory.getNqtynotification());

		lstMaterialInventoryTrans.add(mapJsonData);

		return new ResponseEntity<>(lstMaterialInventoryTrans, HttpStatus.OK);
	}

	@SuppressWarnings("unchecked")
	public ResponseEntity<Object> createMaterialInventoryTrans(Map<String, Object> inputMap)
			throws JSONException, Exception {

		Map<String, Object> objmap = new LinkedHashMap<String, Object>();
		ObjectMapper Objmapper = new ObjectMapper();
		JSONObject json = new JSONObject();

		final LScfttransaction cft = Objmapper.convertValue(inputMap.get("objsilentaudit"), LScfttransaction.class);
		final String dtransactiondate = "dd-mm-yy";

		JSONObject insJsonObj = new JSONObject(inputMap.get("MaterialInventoryTrans").toString());
		JSONObject jsonuidata = new JSONObject(inputMap.get("jsonuidata").toString());
		JSONObject jsonTransObj = (JSONObject) jsonuidata.get("Transaction Type");

		int nInventrans = (int) insJsonObj.get("ninventorytranscode");

		String inventTransString = nInventrans == 1 ? "Inhouse" : "Outside";

//		Date objCreatedDate = cft.getTransactiondate();

		LocalDateTime myDateObj = LocalDateTime.now();
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern(dtransactiondate);
		String formattedDate = myDateObj.format(myFormatObj);

		jsonuidata.put("Transaction Type", jsonTransObj.get("transactionname"));
		jsonuidata.put("Inventory Transaction Type", inventTransString);
		insJsonObj.put("Transaction Type", jsonTransObj.get("transactionname"));
		insJsonObj.put("Inventory Transaction Type", inventTransString);

		insJsonObj.put("Transaction Date & Time", formattedDate);
		insJsonObj.put("noffsetTransaction Date & Time", commonfunction.getCurrentDateTimeOffset("Europe/London"));
		jsonuidata.put("Transaction Date & Time", formattedDate);
		jsonuidata.put("noffsetTransaction Date & Time", commonfunction.getCurrentDateTimeOffset("Europe/London"));

		final double aQty = Double.parseDouble(insJsonObj.get("navailableqty").toString());
		final double rQty = Double.parseDouble(insJsonObj.get("Received Quantity").toString());

		if ((int) insJsonObj.get("ntransactiontype") == Enumeration.TransactionStatus.ISSUE.gettransactionstatus()
				|| (int) insJsonObj.get("ntransactiontype") == Enumeration.TransactionStatus.REJECTED
						.gettransactionstatus()) {

			double namountleft = Double.parseDouble(insJsonObj.get("navailableqty").toString())
					- Double.parseDouble(insJsonObj.get("Received Quantity").toString());

			String amtleft = new Double(namountleft).toString();

			insJsonObj.put("namountleft", amtleft);
			jsonuidata.put("namountleft", amtleft);

		} else if ((int) insJsonObj.get("ntransactiontype") == Enumeration.TransactionStatus.RETURN
				.gettransactionstatus()) {

			double availqty = Double.parseDouble(insJsonObj.get("navailableqty").toString());
			double namountleft = availqty + Double.parseDouble(insJsonObj.get("Received Quantity").toString());

			insJsonObj.put("namountleft", new Double(namountleft).toString());
			jsonuidata.put("namountleft", new Double(namountleft).toString());
			insJsonObj.put("navailableqty", new Double(namountleft).toString());
			jsonuidata.put("navailableqty", new Double(namountleft).toString());
		} else {
			double availqty = Double.parseDouble(insJsonObj.get("navailableqty").toString());
			double namountleft = availqty + Double.parseDouble(insJsonObj.get("Received Quantity").toString());

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

		List<MaterialInventoryTransaction> lstsourceSection = materialInventoryTransactionRepository
				.findByNmaterialinventorycodeOrderByNmaterialinventtranscode(
						(Integer) inputMap.get("nmaterialinventorycode"));

		JSONObject insJsonObjcopy = new JSONObject(insJsonObj.toString());
		JSONObject jsonUidataObjcopy = new JSONObject(jsonuidata.toString());

		LSuserMaster objUser = new LSuserMaster();
		objUser.setUsercode(cft.getLsuserMaster());

		insJsonObjcopy.put("Transaction Date & Time", formattedDate);
		insJsonObjcopy.put("noffsetTransaction Date & Time", commonfunction.getCurrentDateTimeOffset("Europe/London"));
		jsonUidataObjcopy.put("Transaction Date & Time", formattedDate);
		jsonUidataObjcopy.put("noffsetTransaction Date & Time",
				commonfunction.getCurrentDateTimeOffset("Europe/London"));

		if ((int) inputMap.get("needsectionwise") == Enumeration.TransactionStatus.NO.gettransactionstatus()) {

			insJsonObjcopy.put("Section", new JSONObject("{\"label\":\"Default\",\"value\":\"-1\"}"));
			jsonUidataObjcopy.put("Section", "Default");
		} else {

			Map<String, Object> map = (Map<String, Object>) commonfunction
					.getInventoryValuesFromJsonString(lstsourceSection.get(0).getJsondata(), "Section").get("rtnObj");

			insJsonObjcopy.put("Section", new JSONObject(Objmapper.writeValueAsString(map)));
			jsonUidataObjcopy.put("Section", new JSONObject(Objmapper.writeValueAsString(map)).get("label"));

		}
		if ((int) insJsonObj.get("ninventorytranscode") == Enumeration.TransactionStatus.INHOUSE
				.gettransactionstatus()) {
			json.put("label", "Received");
			json.put("value", Enumeration.TransactionStatus.RECEIVE.gettransactionstatus());

			if ((int) insJsonObj.get("ntransactiontype") == Enumeration.TransactionStatus.ISSUE
					.gettransactionstatus()) {

				jsonUidataObjcopy.put("Description", cft.getComments());
				jsonuidata.put("Description", cft.getComments());

				MaterialInventoryTransaction objTransaction = new MaterialInventoryTransaction();

				objTransaction.setJsondata(insJsonObjcopy.toString());
				objTransaction.setJsonuidata(jsonUidataObjcopy.toString());
				objTransaction.setNstatus(Enumeration.TransactionStatus.ACTIVE.gettransactionstatus());
				objTransaction.setNmaterialinventorycode((Integer) inputMap.get("nmaterialinventorycode"));
				objTransaction.setNinventorytranscode((Integer) insJsonObj.get("ninventorytranscode"));
				objTransaction.setNtransactiontype((Integer) insJsonObj.get("ntransactiontype"));
				objTransaction.setNsectioncode(-1);
				objTransaction.setNsitecode(-1);
				objTransaction.setNresultusedmaterialcode(-1);
				objTransaction.setNqtyreceived(Double.valueOf((Integer) insJsonObj.get("Received Quantity")));
				objTransaction.setNqtyissued(Double.valueOf((Integer) insJsonObj.get("nqtyissued")));
				objTransaction.setIssuedbyusercode(objUser);
//				objTransaction.setCreateddate(objCreatedDate);
				try {
					objTransaction.setCreateddate(commonfunction.getCurrentUtcTime());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				materialInventoryTransactionRepository.save(objTransaction);

				inputMap.put("nsectioncode", lstsourceSection.get(0).getNsectioncode());
			} else {

				jsonUidataObjcopy.put("Description", cft.getComments());
				jsonuidata.put("Description", cft.getComments());

				MaterialInventoryTransaction objTransaction = new MaterialInventoryTransaction();

				objTransaction.setJsondata(insJsonObjcopy.toString());
				objTransaction.setJsonuidata(jsonUidataObjcopy.toString());
				objTransaction.setNstatus(Enumeration.TransactionStatus.ACTIVE.gettransactionstatus());
				objTransaction.setNmaterialinventorycode((Integer) inputMap.get("nmaterialinventorycode"));
				objTransaction.setNinventorytranscode((Integer) insJsonObj.get("ninventorytranscode"));
				objTransaction.setNtransactiontype((Integer) insJsonObj.get("ntransactiontype"));
				objTransaction.setNsectioncode(-1);
				objTransaction.setNsitecode(-1);
				objTransaction.setNresultusedmaterialcode(-1);
				objTransaction.setNqtyreceived(Double.valueOf((Integer) insJsonObj.get("Received Quantity")));
				objTransaction.setNqtyissued(Double.valueOf((Integer) insJsonObj.get("nqtyissued")));
				objTransaction.setIssuedbyusercode(objUser);
//				objTransaction.setCreateddate(objCreatedDate);
				try {
					objTransaction.setCreateddate(commonfunction.getCurrentUtcTime());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				materialInventoryTransactionRepository.save(objTransaction);

				inputMap.put("nsectioncode", lstsourceSection.get(0).getNsectioncode());
			}

		} else {

			jsonUidataObjcopy.put("Description", cft.getComments());

			MaterialInventoryTransaction objTransaction = new MaterialInventoryTransaction();

			objTransaction.setJsondata(insJsonObjcopy.toString());
			objTransaction.setJsonuidata(jsonUidataObjcopy.toString());
			objTransaction.setNstatus(Enumeration.TransactionStatus.ACTIVE.gettransactionstatus());
			objTransaction.setNmaterialinventorycode((Integer) inputMap.get("nmaterialinventorycode"));
			objTransaction.setNinventorytranscode((Integer) insJsonObj.get("ninventorytranscode"));
			objTransaction.setNtransactiontype((Integer) insJsonObj.get("ntransactiontype"));
			objTransaction.setNsectioncode(-1);
			objTransaction.setNsitecode(-1);
			objTransaction.setNresultusedmaterialcode(-1);
			objTransaction.setNqtyreceived(Double.valueOf((Integer) insJsonObj.get("Received Quantity")));
			objTransaction.setNqtyissued(Double.valueOf((Integer) insJsonObj.get("nqtyissued")));
			objTransaction.setIssuedbyusercode(objUser);
			materialInventoryTransactionRepository.save(objTransaction);

			inputMap.put("nsectioncode", insJsonObj.get("nsectioncode"));
		}
		objmap.put("nregtypecode", -1);
		objmap.put("nregsubtypecode", -1);

		if ((int) insJsonObj.get("ntransactiontype") == Enumeration.TransactionStatus.RETURN.gettransactionstatus()) {
			List<ResultUsedMaterial> objList = resultUsedMaterialRepository
					.findByNinventorycodeOrderByNresultusedmaterialcodeDesc(
							(Integer) inputMap.get("nmaterialinventorycode"));

			if (!objList.isEmpty()) {
				objList.get(0).setNqtyleft(objList.get(0).getNqtyleft() - rQty);
				resultUsedMaterialRepository.save(objList);
			}
		} else if ((int) insJsonObj.get("ntransactiontype") == Enumeration.TransactionStatus.ISSUE
				.gettransactionstatus()) {
			List<ResultUsedMaterial> objList = resultUsedMaterialRepository
					.findByNinventorycodeOrderByNresultusedmaterialcodeDesc(
							(Integer) inputMap.get("nmaterialinventorycode"));

			if (!objList.isEmpty()) {
				objList.get(0).setNqtyleft(objList.get(0).getNqtyleft() + rQty);
				resultUsedMaterialRepository.save(objList);
			}
		}

		if (inputMap.containsKey("notifcationamount") && inputMap.get("notifcationamount") != null) {
			MaterialInventory objInventory = materialInventoryRepository
					.findByNmaterialinventorycode((Integer) inputMap.get("nmaterialinventorycode"));
			objInventory.setNqtynotification(Double.parseDouble(inputMap.get("notifcationamount").toString()));
			materialInventoryRepository.save(objInventory);
		}

		if (inputMap.containsKey("isquarantine") && Integer.parseInt(inputMap.get("isquarantine").toString()) == 1) {
			MaterialInventory objInventory = materialInventoryRepository
					.findByNmaterialinventorycode((Integer) inputMap.get("nmaterialinventorycode"));
			JSONObject invJsonObj = new JSONObject(objInventory.getJsondata());
			JSONObject invJsonuidata = new JSONObject(objInventory.getJsonuidata());

			invJsonObj.put("Received Quantity", aQty + rQty);
			invJsonuidata.put("Received Quantity", aQty + rQty);

			objInventory.setJsondata(invJsonObj.toString());
			objInventory.setJsonuidata(invJsonuidata.toString());

			materialInventoryRepository.save(objInventory);
		}

		objmap.putAll((Map<String, Object>) materialInventoryService
				.getQuantityTransactionByMaterialInvCode((int) inputMap.get("nmaterialinventorycode"), inputMap)
				.getBody());
		return new ResponseEntity<>(objmap, HttpStatus.OK);
	}

	@SuppressWarnings("unchecked")
	public ResponseEntity<Object> createMaterialResultUsed(Map<String, Object> inputMap)
			throws JsonParseException, JsonMappingException, IOException {

		ObjectMapper Objmapper = new ObjectMapper();

		final LScfttransaction cft = Objmapper.convertValue(inputMap.get("silentAudit"), LScfttransaction.class);
		final MaterialInventory objInventoryFromMap = Objmapper.convertValue(inputMap.get("selectedMaterialInventory"),
				MaterialInventory.class);
		final Map<String, Object> objResultMap = (Map<String, Object>) inputMap.get("resultObject");
		final LStestmasterlocal objTest = new LStestmasterlocal();
		objTest.setTestcode((Integer) objResultMap.get("testcode"));

		MaterialInventory objInventory = materialInventoryRepository
				.findByNmaterialinventorycode(objInventoryFromMap.getNmaterialinventorycode());

		Double getIssuedQty = Double.parseDouble(objResultMap.get("issuedQuantity").toString());
		Double getUsedQty = Double.parseDouble(objResultMap.get("usedQuantity").toString());
		Double getQtyLeft = getIssuedQty - Double.parseDouble(objResultMap.get("usedQuantity").toString());

		LSuserMaster objUser = new LSuserMaster();
		objUser.setUsercode(cft.getLsuserMaster());

		ResultUsedMaterial resultUsedMaterial = new ResultUsedMaterial();
		if (objTest.getTestcode() != -1) {
			resultUsedMaterial.setTestcode(objTest);
		}
//		resultUsedMaterial.setCreateddate(cft.getTransactiondate());
		resultUsedMaterial.setCreatedbyusercode(objUser);
		resultUsedMaterial.setNqtyissued(getIssuedQty);
		resultUsedMaterial.setNqtyleft(getQtyLeft);
		resultUsedMaterial.setNqtyused(getUsedQty);
		resultUsedMaterial.setBatchid(objResultMap.get("batchid").toString());
		resultUsedMaterial.setNmaterialcode(objInventory.getNmaterialcode());
		resultUsedMaterial.setNmaterialcategorycode(objInventory.getNmaterialcatcode());
		resultUsedMaterial.setNinventorycode(objInventory.getNmaterialinventorycode());
		resultUsedMaterial.setNmaterialtypecode(objInventory.getNmaterialtypecode());
		resultUsedMaterial.setOrdercode(Long.valueOf(objResultMap.get("ordercode").toString()));
		resultUsedMaterial.setTransactionscreen(Integer.parseInt(objResultMap.get("transactionscreen").toString()));
		resultUsedMaterial.setTemplatecode(Integer.parseInt(objResultMap.get("templatecode").toString()));
		resultUsedMaterial.setJsondata(cft.getComments());
		resultUsedMaterial.setNstatus(1);
		resultUsedMaterial.setResponse(new Response());
		resultUsedMaterial.getResponse().setStatus(true);
		try {
			resultUsedMaterial.setCreateddate(commonfunction.getCurrentUtcTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		resultUsedMaterialRepository.save(resultUsedMaterial);

		if (objInventory.getNqtynotification() != null) {
			if (objInventory.getNqtynotification() <= getQtyLeft ? false : true) {
				List<LSnotification> lstLSnotifications = new ArrayList<LSnotification>();
				lstLSnotifications.addAll(updateNotificationOnInventory(objInventory, "INVENTORYQTYNOTIFICATION", cft, getQtyLeft, new Date()));
				lsnotificationRepository.save(lstLSnotifications);
			}
		}

		return new ResponseEntity<>(resultUsedMaterial, HttpStatus.OK);
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

	public ResponseEntity<Object> getMaterialLst4DashBoard(Map<String, Object> inputMap) {

		Map<String, Object> rtnMap = new HashMap<String, Object>();

		List<Material> lstMaterials = new ArrayList<Material>();
		List<MaterialInventory> lstMaterialInventories = new ArrayList<MaterialInventory>();

		lstMaterials = materialRepository.findByNstatusAndNsitecodeOrderByNmaterialcodeDesc(1,
				(Integer) inputMap.get("sitecode"));

		if (!lstMaterials.isEmpty()) {

			lstMaterialInventories = materialInventoryRepository
//					.findByNmaterialcodeAndNtransactionstatusOrderByNmaterialinventorycode(lstMaterials.get(0).getNmaterialcode(),28);
					.findByNsitecodeAndNtransactionstatusOrderByNmaterialinventorycodeDesc(
							(Integer) inputMap.get("sitecode"), 28);
		}

		rtnMap.put("listedMaterial", lstMaterials);
		rtnMap.put("listedMaterialInventory", lstMaterialInventories);

		return new ResponseEntity<>(rtnMap, HttpStatus.OK);
	}

	private Map<String, Object> getJsonDataFromInventory(String jsonData, String string) {
		Map<String, Object> objReturnMap = new HashMap<String, Object>();
		Map<String, Object> objContent = commonfunction.getInventoryValuesFromJsonString(jsonData, string);
		if (objContent.get("rtnObj") != null) {
			objReturnMap.put(string, objContent.get("rtnObj"));
		}
		return objReturnMap;
	}

	public List<LSnotification>  updateNotificationOnInventory(MaterialInventory objInventory, String task, LScfttransaction cft,
			Double getQtyLeft, Date date) {

		List<LSnotification> lstnotifications = new ArrayList<LSnotification>();
		
		List<MaterialInventoryTransaction> objLstTransactions = objInventory.getMaterialInventoryTransactions();

		List<LSuserMaster> objLstuser = objLstTransactions.stream()
				.map(MaterialInventoryTransaction::getIssuedbyusercode).filter(Objects::nonNull).collect(Collectors.toList());

		if (!objLstuser.isEmpty()) {
			List<Integer> objnotifyuser = objLstuser.stream().map(LSuserMaster::getUsercode).collect(Collectors.toList());

			objnotifyuser = objnotifyuser.stream().distinct().collect(Collectors.toList());
			
			if(objnotifyuser.contains(cft.getLsuserMaster())) {
				
				LSuserMaster objUser = new LSuserMaster();
				objUser.setUsercode(cft.getLsuserMaster());

				String details = "";

				if (task.equals("INVENTORYQTYNOTIFICATION")) {

					details = "{\"inventoryid\":\"" + objInventory.getSinventoryid() + "\",  " + "\"qtyleft\":\""
							+ getQtyLeft + "\",  " + "\"notificationamount\":\"" + objInventory.getNqtynotification() + "\"}";

				} else if (task.equals("EXPIRYDATE")) {
					details = "{\"inventoryid\":\"" + objInventory.getSinventoryid() + "\",  " + "\"daysleft\":\"" + date + "\"}";
				} else {
					details = "{\"inventoryid\":\"" + objInventory.getSinventoryid() + "\",  " + "\"daysleft\":\"" + getQtyLeft + "\"}";
				}

				String notification = details;

				LSnotification objnotify = new LSnotification();

				objnotify.setNotifationto(objUser);
				objnotify.setNotifationfrom(objUser);
				objnotify.setNotificationdate(cft.getTransactiondate());
				objnotify.setNotification(task);
				objnotify.setNotificationdetils(notification);
				objnotify.setIsnewnotification(1);
				objnotify.setNotificationpath("/materialinventory");
				objnotify.setNotificationfor(1);
				
				lstnotifications.add(objnotify);
				
				return lstnotifications;
			}
		}
		return lstnotifications;
	}

	public void updateMaterialInventoryNotification(Map<String, Object> inputMap) throws ParseException {
		final Map<String, Object> threadMap = inputMap;
//		new Thread(() -> {
		updateMaterialInventoryNotificationvia(threadMap);
//		}).start();
	}

	@SuppressWarnings("unchecked")
	public void updateMaterialInventoryNotificationviaThread(Map<String, Object> inputMap) {

		ObjectMapper Objmapper = new ObjectMapper();

		Integer sitecode = (Integer) inputMap.get("sitecode");
		Long timestamp = (Long) inputMap.get("currentDate");
		Date currentDate = new Date(timestamp);
		final LScfttransaction cft = Objmapper.convertValue(inputMap.get("silentAudit"), LScfttransaction.class);
		List<Material> lstMaterials = materialRepository.findByNstatusAndNsitecodeOrderByNmaterialcodeDesc(1, sitecode);

		SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
		LocalDate localCurrentDate = currentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		lstMaterials.stream().peek(objMaterial -> {

			Map<String, Object> objMapMaterial = materialInventoryService.crumObjectMaterialCreated(objMaterial);

			if (objMapMaterial.containsKey("jsondata")) {
				Map<String, Object> objMapJsonData = (Map<String, Object>) objMapMaterial.get("jsondata");

				String isExpiryNeed = (String) objMapJsonData.get("isExpiryNeed");
				String isOpenExpiryNeed = objMapJsonData.get("Open Expiry Need") != null
						? objMapJsonData.get("Open Expiry Need").toString()
						: "4";
				String isNextValidNeed = objMapJsonData.get("Next Validation Need") != null
						? objMapJsonData.get("Next Validation Need").toString()
						: "4";

				boolean isExpiryNotify = isExpiryNeed != null
						? isExpiryNeed.equalsIgnoreCase("Expiry Date") ? true : false
						: false;
				boolean isOpenExpiry = isOpenExpiryNeed != null ? isOpenExpiryNeed.equalsIgnoreCase("3") ? true : false
						: false;
				boolean isNextValidate = isNextValidNeed != null ? isNextValidNeed.equalsIgnoreCase("3") ? true : false
						: false;

				List<MaterialInventory> objInventories = materialInventoryRepository
						.findByNmaterialcodeAndNtransactionstatusOrderByNmaterialinventorycodeDesc(
								objMaterial.getNmaterialcode(), 28);

				if (isExpiryNotify) {

					objInventories.stream().peek(objInventory -> {

						Map<String, Object> objMap = getJsonDataFromInventory(objInventory.getJsondata(),
								"Expiry Date & Time");

						String indoDateStr = (String) objMap.get("Expiry Date & Time");
						try {
							Date date = dateFormat.parse(indoDateStr);
							LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

							if (localCurrentDate.isBefore(localDate) || localDate.equals(localCurrentDate)) {
//								Duration duration = Duration.between(localDate.atStartOfDay(),
//										localCurrentDate.atStartOfDay());
//								long days = duration.toDays();
//								
//								days = Math.abs(days);
//
//								if (days <= 10) {
//									final double day = (double) days;
								updateNotificationOnInventory(objInventory, "EXPIRYDATE", cft, 0.0, date);
//								}
							} else {
								objInventory.setNtransactionstatus(55);
								materialInventoryRepository.save(objInventory);
								updateNotificationOnInventory(objInventory, "EXPIRYREACHED", cft, 0.0, date);
							}

						} catch (ParseException e) {
							e.printStackTrace();
						}
					}).collect(Collectors.toList());
				} else if (isOpenExpiry) {

					objInventories.stream().peek(objInventory -> {
						if (getJsonDataFromInventory(objInventory.getJsondata(), "Open Expiry").get("rtnObj") != null) {

							long openExpiry = Long
									.parseLong(getJsonDataFromInventory(objInventory.getJsondata(), "Open Expiry")
											.get("rtnObj").toString());
							Map<String, Object> objMap = getJsonDataFromInventory(objInventory.getJsondata(),
									"Open Date");
							Map<String, Object> objOpenExp = getJsonDataFromInventory(objMaterial.getJsondata(),
									"Open Expiry Period");

							if (objMap.get("Open Date") != null && objOpenExp != null
									&& objOpenExp.containsKey("Open Expiry Period")) {

								Map<String, Object> objOpenValue = ((JSONObject) objOpenExp.get("Open Expiry Period"))
										.toMap();

								if (!objOpenValue.get("label").toString().equalsIgnoreCase("NA")
										&& !objOpenValue.get("label").toString().equalsIgnoreCase("Never")) {

									String validationType = objOpenValue.get("label").toString();
									long longOpenDate = (long) objMap.get("Open Date");

									Date openDate = new Date(longOpenDate);
									LocalDate localDate = openDate.toInstant().atZone(ZoneId.systemDefault())
											.toLocalDate();

									if (localCurrentDate.isBefore(localDate) || localDate.equals(localCurrentDate)) {

										if (validationType.equalsIgnoreCase("days")) {
											Duration duration = Duration.between(localDate.atStartOfDay(),
													localCurrentDate.atStartOfDay());

											long days = duration.toDays();

											days = Math.abs(days);

											if (days <= openExpiry) {
												final double day = (double) days;

												updateNotificationOnInventory(objInventory, "OPENDATEDAYS", cft, day,
														openDate);
											}
										} else if (validationType.equalsIgnoreCase("weeks")) {

											long weeks = ChronoUnit.WEEKS.between(localDate, localCurrentDate);

											weeks = Math.abs(weeks);

											if (weeks <= openExpiry) {
												final double week = (double) weeks;
												updateNotificationOnInventory(objInventory, "OPENDATEWEEK", cft, week,
														openDate);
											}

										} else if (validationType.equalsIgnoreCase("month")) {

											Period period = Period.between(localDate, localCurrentDate);

											long months = period.getMonths();

											months = Math.abs(months);

											if (months <= openExpiry) {
												final double month = (double) months;
												updateNotificationOnInventory(objInventory, "OPENDATEMONTH", cft, month,
														openDate);
											}

										} else if (validationType.equalsIgnoreCase("years")) {
											Period period = Period.between(localDate, localCurrentDate);

											long years = period.getYears();

											years = Math.abs(years);
											final double year = (double) years;
											updateNotificationOnInventory(objInventory, "OPENDATEYEAR", cft, year,
													openDate);
										} else if (validationType.equalsIgnoreCase("hours")) {

											Duration duration = Duration.between(localDate.atStartOfDay(),
													localCurrentDate.atStartOfDay());
											long days = duration.toDays();
											days = Math.abs(days);
											if (days == 0) {
												long diffInMs = currentDate.getTime() - openDate.getTime();

												long diffInHours = diffInMs / (60 * 60 * 1000);
												if (diffInHours <= openExpiry) {
													final double hour = (double) diffInHours;
													updateNotificationOnInventory(objInventory, "OPENDATEHOURS", cft,
															hour, openDate);
												}
											}

										} else if (validationType.equalsIgnoreCase("minutes")) {

											Duration duration = Duration.between(localDate.atStartOfDay(),
													localCurrentDate.atStartOfDay());
											long days = duration.toDays();
											days = Math.abs(days);
											if (days == 0) {
												long diffInMs = currentDate.getTime() - openDate.getTime();

												long diffInMinutes = diffInMs / (60 * 1000);

												final double minute = (double) diffInMinutes;
												updateNotificationOnInventory(objInventory, "OPENDATEMINUTES", cft,
														minute, openDate);
											}
										}
									}
								}
							}
						}
					}).collect(Collectors.toList());
				} else if (isNextValidate) {

					objInventories.stream().peek(objInventory -> {
						long nextValidation = Long
								.parseLong(getJsonDataFromInventory(objInventory.getJsondata(), "Next Validation")
										.get("rtnObj").toString());

						Map<String, Object> objMap = getJsonDataFromInventory(objInventory.getJsondata(),
								"Last Validation Date & Time");
						Map<String, Object> objNextVal = getJsonDataFromInventory(objMaterial.getJsondata(),
								"Next Validation Period");

						if (objMap.get("Last Validation Date & Time") != null && objNextVal != null
								&& objNextVal.containsKey("Next Validation Period")) {

							Map<String, Object> objOpenValue = ((JSONObject) objNextVal.get("Next Validation Period"))
									.toMap();

							if (!objOpenValue.get("label").toString().equalsIgnoreCase("NA")
									&& !objOpenValue.get("label").toString().equalsIgnoreCase("Never")) {

								String validationType = objOpenValue.get("label").toString();
//								long longOpenDate = (long) objMap.get("");

								String indoDateStr = (String) objMap.get("Last Validation Date & Time");
								Date openDate = null;
								try {
									openDate = dateFormat.parse(indoDateStr);
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

								if (openDate != null) {
									LocalDate localDate = openDate.toInstant().atZone(ZoneId.systemDefault())
											.toLocalDate();

									if (localCurrentDate.isBefore(localDate) || localDate.equals(localCurrentDate)) {

										if (validationType.equalsIgnoreCase("days")) {
											Duration duration = Duration.between(localDate.atStartOfDay(),
													localCurrentDate.atStartOfDay());

											long days = duration.toDays();
											days = Math.abs(days);
											if (days <= nextValidation) {
												final double day = (double) days;

												updateNotificationOnInventory(objInventory, "NEXTVALIDATEDAYS", cft,
														day, openDate);
											}
										} else if (validationType.equalsIgnoreCase("weeks")) {

											long weeks = ChronoUnit.WEEKS.between(localDate, localCurrentDate);
											weeks = Math.abs(weeks);
											if (weeks <= nextValidation) {
												final double week = (double) weeks;
												updateNotificationOnInventory(objInventory, "NEXTVALIDATEWEEK", cft,
														week, openDate);
											}

										} else if (validationType.equalsIgnoreCase("month")) {

											Period period = Period.between(localDate, localCurrentDate);

											long months = period.getMonths();
											months = Math.abs(months);
											if (months <= nextValidation) {
												final double month = (double) months;
												updateNotificationOnInventory(objInventory, "NEXTVALIDATEMONTH", cft,
														month, openDate);
											}

										} else if (validationType.equalsIgnoreCase("years")) {
											Period period = Period.between(localDate, localCurrentDate);

											long years = period.getYears();
											years = Math.abs(years);
											final double year = (double) years;
											updateNotificationOnInventory(objInventory, "NEXTVALIDATEYEAR", cft, year,
													openDate);
										} else if (validationType.equalsIgnoreCase("hours")) {

											Duration duration = Duration.between(localDate.atStartOfDay(),
													localCurrentDate.atStartOfDay());
											long days = duration.toDays();
											days = Math.abs(days);
											if (days == 0) {
												long diffInMs = currentDate.getTime() - openDate.getTime();

												long diffInHours = diffInMs / (60 * 60 * 1000);

												if (diffInHours <= nextValidation) {
													final double hour = (double) diffInHours;
													updateNotificationOnInventory(objInventory, "NEXTVALIDATEHOURS",
															cft, hour, openDate);
												}
											}

										} else if (validationType.equalsIgnoreCase("minutes")) {

											Duration duration = Duration.between(localDate.atStartOfDay(),
													localCurrentDate.atStartOfDay());
											long days = duration.toDays();
											days = Math.abs(days);
											if (days == 0) {
												long diffInMs = currentDate.getTime() - openDate.getTime();

												long diffInMinutes = diffInMs / (60 * 1000);

												final double minute = (double) diffInMinutes;
												updateNotificationOnInventory(objInventory, "NEXTVALIDATEMINUTES", cft,
														minute, openDate);
											}
										}
									}
								}
							}
						}
					}).collect(Collectors.toList());
				}
			}
		}).collect(Collectors.toList());
	}

	public void updateMaterialInventoryNotificationvia(Map<String, Object> inputMap) throws ParseException {

		ObjectMapper Objmapper = new ObjectMapper();

//		Integer sitecode = (Integer) inputMap.get("sitecode");
		Date currentDate = commonfunction.getCurrentUtcTime();
		final LScfttransaction cft = Objmapper.convertValue(inputMap.get("silentAudit"), LScfttransaction.class);
//		LSuserMaster objUser = cft.getLsuserMaster();
//		List<Material> lstMaterials = materialRepository.findByNstatusAndNsitecodeOrderByNmaterialcodeDesc(1, sitecode);
		LocalDate localCurrentDate = currentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		// Create a Calendar instance and set it to the current date
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(currentDate);
		// Add 5 days from the current date
		calendar.add(Calendar.DAY_OF_YEAR, 5);
		// Set the ending time of the day
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		// Get the end date
		Date endDate = calendar.getTime();
		
		calendar.setTime(currentDate);
		// Add -1 days from the previous day of the current date
		calendar.add(Calendar.DAY_OF_YEAR, -1);
		// Set the ending time of the day
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		currentDate = calendar.getTime();

		List<MaterialInventory> objInventories = materialInventoryRepository.findByNsitecodeAndNtransactionstatusAndIsexpiryneedAndExpirydateBetween(cft.getLssitemaster(),28, true, currentDate, endDate);
		List<MaterialInventory> expiredInvent = new ArrayList<MaterialInventory>(); 
		List<LSnotification> lstLSnotifications = new ArrayList<LSnotification>();

		objInventories.stream().peek(objInventory -> {
			if (objInventory.getIsexpiryneed()) {
				Date date = objInventory.getExpirydate();
				LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				if (localCurrentDate.isBefore(localDate)) {
					lstLSnotifications.addAll(updateNotificationOnInventory(objInventory, "EXPIRYDATE", cft, 0.0, date));
				} else {
					objInventory.setNtransactionstatus(55);
					expiredInvent.add(objInventory);
					lstLSnotifications.addAll(updateNotificationOnInventory(objInventory, "EXPIRYREACHED", cft, 0.0, date));
				}
			}
		}).collect(Collectors.toList());
		
		lsnotificationRepository.save(lstLSnotifications);
		materialInventoryRepository.save(expiredInvent);
	}

}