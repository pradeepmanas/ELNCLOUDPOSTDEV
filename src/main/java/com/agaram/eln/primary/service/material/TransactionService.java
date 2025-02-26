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
import java.util.Collection;
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
import com.agaram.eln.primary.fetchmodel.inventory.Materialget;
//import com.agaram.eln.primary.fetchmodel.getmasters.Samplemaster;
import com.agaram.eln.primary.global.Enumeration;
import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.equipment.ElnresultEquipment;
import com.agaram.eln.primary.model.equipment.Equipment;
import com.agaram.eln.primary.model.equipment.EquipmentHistory;
import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.material.Elnmaterial;
import com.agaram.eln.primary.model.material.ElnmaterialInventory;
import com.agaram.eln.primary.model.material.ElnresultUsedMaterial;
import com.agaram.eln.primary.model.material.MappedTemplateFieldPropsMaterial;
import com.agaram.eln.primary.model.material.Material;
import com.agaram.eln.primary.model.material.MaterialCategory;
import com.agaram.eln.primary.model.material.MaterialConfig;
import com.agaram.eln.primary.model.material.MaterialInventory;
import com.agaram.eln.primary.model.material.MaterialInventoryTransaction;
import com.agaram.eln.primary.model.material.MaterialType;
import com.agaram.eln.primary.model.material.ResultUsedMaterial;
import com.agaram.eln.primary.model.material.Unit;
import com.agaram.eln.primary.model.sample.ElnresultUsedSample;
import com.agaram.eln.primary.model.sample.Sample;
import com.agaram.eln.primary.model.sheetManipulation.LStestmasterlocal;
import com.agaram.eln.primary.model.usermanagement.LSnotification;
import com.agaram.eln.primary.model.usermanagement.LSprojectmaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.repository.equipment.ElnresultEquipmentRepository;
import com.agaram.eln.primary.repository.equipment.EquipmentHistoryRepository;
import com.agaram.eln.primary.repository.equipment.EquipmentRepository;
import com.agaram.eln.primary.repository.material.ElnmaterialInventoryRepository;
import com.agaram.eln.primary.repository.material.ElnmaterialRepository;
import com.agaram.eln.primary.repository.material.ElnresultUsedMaterialRepository;
import com.agaram.eln.primary.repository.material.MappedTemplateFieldPropsMaterialRepository;
import com.agaram.eln.primary.repository.material.MaterialCategoryRepository;
import com.agaram.eln.primary.repository.material.MaterialConfigRepository;
import com.agaram.eln.primary.repository.material.MaterialInventoryRepository;
import com.agaram.eln.primary.repository.material.MaterialInventoryTransactionRepository;
import com.agaram.eln.primary.repository.material.MaterialRepository;
import com.agaram.eln.primary.repository.material.MaterialTypeRepository;
import com.agaram.eln.primary.repository.material.ResultUsedMaterialRepository;
import com.agaram.eln.primary.repository.sample.ElnresultUsedSampleRepository;
import com.agaram.eln.primary.repository.sample.SampleRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LStestmasterlocalRepository;
import com.agaram.eln.primary.repository.usermanagement.LSnotificationRepository;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

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

	@Autowired
	private ElnmaterialRepository elnmaterialRepository;

	@Autowired
	private ElnmaterialInventoryRepository elnmaterialInventoryRepository;

	@Autowired
	private ElnresultUsedMaterialRepository elnresultUsedMaterialRepository;

	@Autowired
	private EquipmentRepository equipmentRepository;

	@Autowired
	private ElnresultEquipmentRepository elnresultEquipmentRepository;

	@Autowired
	private EquipmentHistoryRepository equipmentHistoryRepository;

	@Autowired
	private LStestmasterlocalRepository lStestmasterlocalRepository;

	@Autowired
	private SampleRepository SampleRepository;
	@Autowired
	private ElnresultUsedSampleRepository ElnresultUsedSampleRepository;
	@Autowired
	ElnmaterialInventoryRepository elnmaterialInventoryReppository;

	public ResponseEntity<Object> getLoadOnInventoryData(Map<String, Object> inputMap) {

		Map<String, Object> rtnMap = new HashMap<String, Object>();

		List<MaterialType> lstTypes = new ArrayList<MaterialType>();
		List<MaterialCategory> lstCategories = new ArrayList<MaterialCategory>();
		List<Elnmaterial> lstMaterials = new ArrayList<Elnmaterial>();
		List<ElnmaterialInventory> lstMaterialInventories = new ArrayList<ElnmaterialInventory>();

		Integer nsiteInteger = (Integer) inputMap.get("sitecode");

		if (inputMap.containsKey("nFlag")) {
			/**
			 * in initial load all values by default
			 */
			if ((Integer) inputMap.get("nFlag") == 1) {

//				lstTypes = materialTypeRepository.findByNmaterialtypecodeNotAndNstatusOrderByNmaterialtypecode(-1, 1);

				lstTypes = materialTypeRepository
						.findByNmaterialtypecodeNotAndNstatusAndNsitecodeOrNmaterialtypecodeNotAndNstatusAndNdefaultstatusOrderByNmaterialtypecodeDesc(
								-1, 1, nsiteInteger, -1, 1, 4);

				if (!lstTypes.isEmpty()) {
					lstCategories = materialCategoryRepository
							.findByNmaterialtypecodeAndNsitecodeOrderByNmaterialcatcodeDesc(
									lstTypes.get(0).getNmaterialtypecode(), nsiteInteger);

					if (!lstCategories.isEmpty()) {

						lstMaterials = elnmaterialRepository.findByMaterialcategoryAndNsitecodeOrderByNmaterialcodeDesc(
								lstCategories.get(0), nsiteInteger);

						if (!lstMaterials.isEmpty()) {
							lstMaterialInventories = elnmaterialInventoryRepository
									.findByMaterialAndNtransactionstatusOrderByNmaterialinventorycodeDesc(
											lstMaterials.get(0), 28);
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

					lstMaterials = elnmaterialRepository.findByMaterialcategoryAndNsitecodeOrderByNmaterialcodeDesc(
							lstCategories.get(0), nsiteInteger);

					if (!lstMaterials.isEmpty()) {
						lstMaterialInventories = elnmaterialInventoryRepository
								.findByMaterialAndNtransactionstatusOrderByNmaterialinventorycodeDesc(
										lstMaterials.get(0), 28);
					}

				}
			} /**
				 * in initial load all values by nmaterialcategorycode
				 */
			else if ((Integer) inputMap.get("nFlag") == 3) {

				Integer nCategoryCode = (Integer) inputMap.get("nmaterialcatcode");

				MaterialCategory objCategory = new MaterialCategory();
				objCategory.setNmaterialcatcode(nCategoryCode);

				lstMaterials = elnmaterialRepository.findByMaterialcategoryOrderByNmaterialcodeDesc(objCategory);

				if (!lstMaterials.isEmpty()) {
					lstMaterialInventories = elnmaterialInventoryRepository
							.findByMaterialAndNtransactionstatusOrderByNmaterialinventorycodeDesc(lstMaterials.get(0),
									28);
				}

			} /**
				 * in initial load all values by nmaterialcode
				 */
			else if ((Integer) inputMap.get("nFlag") == 4) {

				Integer nmaterialCode = (Integer) inputMap.get("nmaterialcode");

				Elnmaterial objMaterial = new Elnmaterial();
				objMaterial.setNmaterialcode(nmaterialCode);

				lstMaterialInventories = elnmaterialInventoryRepository
						.findByMaterialAndNtransactionstatusOrderByNmaterialinventorycodeDesc(objMaterial, 28);

			} /**
				 * in initial load all values by nmaterialinventorycode
				 */
			else if ((Integer) inputMap.get("nFlag") == 5) {

				Integer nmaterialinventorycode = (Integer) inputMap.get("nmaterialinventorycode");

				ElnmaterialInventory objMaterial = new ElnmaterialInventory();
				objMaterial.setNmaterialinventorycode(nmaterialinventorycode);

				lstMaterialInventories.add(elnmaterialInventoryRepository
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

		MaterialInventory objInventory = materialInventoryRepository
				.findByNmaterialinventorycodeAndNtransactionstatusNot((Integer) inputMap.get("nmaterialinventorycode"),
						55);

		List<MaterialInventoryTransaction> lstInventoryTransaction = materialInventoryTransactionRepository
				.findByNmaterialinventorycodeOrderByNmaterialinventtranscodeDesc(
						(Integer) inputMap.get("nmaterialinventorycode"));

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

		mapJsonData.put("Total Quantity", totalQuantity);
		mapJsonData.put("Received Quantity", lstInventoryTransaction.get(0).getNqtyreceived());
		mapJsonData.put("NotificationQty", objInventory.getNqtynotification());

		lstMaterialInventoryTrans.add(mapJsonData);

		return new ResponseEntity<>(lstMaterialInventoryTrans, HttpStatus.OK);
	}

	public ResponseEntity<Object> getResultInventoryTransaction(Map<String, Object> inputMap)
			throws JsonParseException, JsonMappingException, IOException {

		List<Map<String, Object>> lstMaterialInventoryTrans = new ArrayList<Map<String, Object>>();

		ElnmaterialInventory objInventory = elnmaterialInventoryRepository
				.findByNmaterialinventorycodeAndNtransactionstatus((Integer) inputMap.get("nmaterialinventorycode"),
						28);

		Map<String, Object> mapJsonData = new HashMap<>();

		Double totalQuantity = Double.parseDouble(objInventory.getSreceivedquantity().toString());

		List<ElnresultUsedMaterial> lstUsedMaterials = elnresultUsedMaterialRepository
				.findByNinventorycodeOrderByNresultusedmaterialcodeDesc(
						(Integer) inputMap.get("nmaterialinventorycode"));

		if (!lstUsedMaterials.isEmpty()) {
			mapJsonData.put("Available Quantity", lstUsedMaterials.get(0).getNqtyleft());
		} else {
			mapJsonData.put("Available Quantity", totalQuantity);
		}

		mapJsonData.put("Total Quantity", totalQuantity);
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
		final ElnmaterialInventory objInventoryFromMap = Objmapper
				.convertValue(inputMap.get("selectedMaterialInventory"), ElnmaterialInventory.class);
		final Map<String, Object> objResultMap = (Map<String, Object>) inputMap.get("resultObject");
		final LStestmasterlocal objTest = new LStestmasterlocal();
		LSprojectmaster projectcode = Objmapper.convertValue(objResultMap.get("projectcode"), LSprojectmaster.class);
		objTest.setTestcode((Integer) objResultMap.get("testcode"));

		ElnmaterialInventory objInventory = elnmaterialInventoryRepository
				.findByNmaterialinventorycode(objInventoryFromMap.getNmaterialinventorycode());

//		Double getIssuedQty = Double.parseDouble(objResultMap.get("issuedQuantity").toString());
		Double getIssuedQty = Double.parseDouble(objInventory.getSavailablequantity().toString());
		Double getUsedQty = Double.parseDouble(objResultMap.get("usedQuantity").toString());
		ElnresultUsedMaterial resultUsedMaterial = new ElnresultUsedMaterial();
		if (getIssuedQty <= getUsedQty) {
			resultUsedMaterial.setResponse(new Response());
			resultUsedMaterial.getResponse().setStatus(false);
			return new ResponseEntity<>(resultUsedMaterial, HttpStatus.OK);
		}
		Double getQtyLeft = getIssuedQty - Double.parseDouble(objResultMap.get("usedQuantity").toString());

		LSuserMaster objUser = new LSuserMaster();
		objUser.setUsercode(cft.getLsuserMaster());

		if (objTest.getTestcode() != -1) {
//			resultUsedMaterial.setTestcode(objTest);
			LStestmasterlocal isPresent = lStestmasterlocalRepository.findBytestcode(objTest.getTestcode());
			if (isPresent != null) {
				resultUsedMaterial.setTestcode(objTest);
			} else {
				resultUsedMaterial.setTestcode(null);
			}
		}
		if (projectcode.getProjectcode()!=null) {
			resultUsedMaterial.setProjectcode(projectcode);
		}
//		resultUsedMaterial.setCreateddate(cft.getTransactiondate());
			resultUsedMaterial.setCreatedbyusercode(objUser);
		resultUsedMaterial.setNqtyissued(getIssuedQty);
		resultUsedMaterial.setNqtyleft(getQtyLeft);
		resultUsedMaterial.setNqtyused(getUsedQty);
		resultUsedMaterial.setBatchid(objResultMap.get("batchid").toString());
		resultUsedMaterial.setNmaterialcode(objInventory.getMaterial().getNmaterialcode());
		resultUsedMaterial.setNmaterialcategorycode(objInventory.getMaterialcategory().getNmaterialcatcode());
		resultUsedMaterial.setNinventorycode(objInventory.getNmaterialinventorycode());
		resultUsedMaterial.setNmaterialtypecode(objInventory.getMaterialtype().getNmaterialtypecode());
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

		objInventory.setSavailablequantity(getQtyLeft.toString());

		if (getQtyLeft == 0) {
			objInventory.setNtransactionstatus(-1);
		}

		resultUsedMaterial.setQtyleft(getQtyLeft.toString());
		resultUsedMaterial.setIsreturn(0);

		elnresultUsedMaterialRepository.save(resultUsedMaterial);
		elnmaterialInventoryRepository.save(objInventory);

		if (objInventory.getNqtynotification() != null) {
			if (objInventory.getNqtynotification() <= getQtyLeft ? false : true) {
				List<LSnotification> lstLSnotifications = new ArrayList<LSnotification>();
				lstLSnotifications.addAll(updateNotificationOnInventory(objInventory, "INVENTORYQTYNOTIFICATION", cft,
						getQtyLeft, new Date()));
				lsnotificationRepository.save(lstLSnotifications);
			}
		}

		return new ResponseEntity<>(resultUsedMaterial, HttpStatus.OK);
	}

	@SuppressWarnings("unchecked")
	public ResponseEntity<Object> createMaterialResultUsedReturn(Map<String, Object> inputMap)
			throws JsonParseException, JsonMappingException, IOException {

		ObjectMapper Objmapper = new ObjectMapper();

		final LScfttransaction cft = Objmapper.convertValue(inputMap.get("silentAudit"), LScfttransaction.class);
		final ElnmaterialInventory objInventoryFromMap = Objmapper
				.convertValue(inputMap.get("selectedMaterialInventory"), ElnmaterialInventory.class);
		final Map<String, Object> objResultMap = (Map<String, Object>) inputMap.get("resultObject");
		final LStestmasterlocal objTest = new LStestmasterlocal();

		objTest.setTestcode((Integer) objResultMap.get("testcode"));

		LSprojectmaster projectcode = Objmapper.convertValue(objResultMap.get("projectcode"), LSprojectmaster.class);
		ElnresultUsedMaterial resultUsedMaterial = new ElnresultUsedMaterial();
		if (projectcode.getProjectcode()!=null) {
			resultUsedMaterial.setProjectcode(projectcode);
		}
		
		ElnmaterialInventory objInventory = elnmaterialInventoryRepository
				.findByNmaterialinventorycode(objInventoryFromMap.getNmaterialinventorycode());

		Double getUsedQty = Double.parseDouble(objResultMap.get("usedQuantity").toString());
		Double getIssuedQty;
		LSuserMaster objUser = new LSuserMaster();
		objUser.setUsercode(cft.getLsuserMaster());

		

		if ((Integer.parseInt(objResultMap.get("transactionscreen").toString()) == 2) && getUsedQty != null) {
			getIssuedQty = Double.parseDouble(objInventory.getSavailablequantity()) + getUsedQty;
			objInventory.setSavailablequantity(getIssuedQty.toString());

		} else {
			if (Double.parseDouble(objInventory.getSavailablequantity()) > getUsedQty) {
				getIssuedQty = Double.parseDouble(objInventory.getSavailablequantity()) - getUsedQty;
				objInventory.setSavailablequantity(getIssuedQty.toString());
			} else {
				resultUsedMaterial.setResponse(new Response());
				resultUsedMaterial.getResponse().setStatus(false);
				return new ResponseEntity<>(resultUsedMaterial, HttpStatus.OK);
			}

		}

		resultUsedMaterial.setCreatedbyusercode(objUser);

		resultUsedMaterial.setNqtyused(getUsedQty);
		resultUsedMaterial.setBatchid(objResultMap.get("batchid").toString());
		resultUsedMaterial.setNmaterialcode(objInventory.getMaterial().getNmaterialcode());
		resultUsedMaterial.setNmaterialcategorycode(objInventory.getMaterialcategory().getNmaterialcatcode());
		resultUsedMaterial.setNinventorycode(objInventory.getNmaterialinventorycode());
		resultUsedMaterial.setNmaterialtypecode(objInventory.getMaterialtype().getNmaterialtypecode());
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

		resultUsedMaterial.setIsreturn(1);
		resultUsedMaterial.setQtyleft(getIssuedQty.toString());
		resultUsedMaterial.setNqtyleft(getIssuedQty);
		resultUsedMaterial.setNqtyused(getUsedQty);

		elnresultUsedMaterialRepository.save(resultUsedMaterial);
		elnmaterialInventoryRepository.save(objInventory);

		return new ResponseEntity<>(resultUsedMaterial, HttpStatus.OK);
	}

	@SuppressWarnings("unchecked")
	public ResponseEntity<Object> createMaterialResultUsedReturnChange(Map<String, Object> inputMap)
			throws JsonParseException, JsonMappingException, IOException {

		ObjectMapper Objmapper = new ObjectMapper();

		final LScfttransaction cft = Objmapper.convertValue(inputMap.get("silentAudit"), LScfttransaction.class);
		final ElnmaterialInventory objInventoryFromMap = Objmapper
				.convertValue(inputMap.get("selectedMaterialInventory"), ElnmaterialInventory.class);
		final Map<String, Object> objResultMap = (Map<String, Object>) inputMap.get("resultObject");
		final LStestmasterlocal objTest = new LStestmasterlocal();
//		final Map<String, Object> retObj = (Map<String, Object>) inputMap.get("retObj");

		objTest.setTestcode((Integer) objResultMap.get("testcode"));

		ElnmaterialInventory objInventory = elnmaterialInventoryRepository
				.findByNmaterialinventorycode(objInventoryFromMap.getNmaterialinventorycode());

		Double previousUsedQuantity = Double.parseDouble(inputMap.get("Usedquantity_Copy").toString());

		Double getIssuedQty = Double.parseDouble(objInventory.getSavailablequantity());
		Double savailableQty = previousUsedQuantity + getIssuedQty;

		LSuserMaster objUser = new LSuserMaster();
		objUser.setUsercode(cft.getLsuserMaster());

		ElnresultUsedMaterial resultUsedMaterial = new ElnresultUsedMaterial();

		objInventory.setSavailablequantity(savailableQty.toString());

		resultUsedMaterial.setCreatedbyusercode(objUser);
		resultUsedMaterial.setNqtyused(previousUsedQuantity);
		resultUsedMaterial.setNqtyleft(savailableQty);
		resultUsedMaterial.setQtyleft(savailableQty.toString());
		resultUsedMaterial.setBatchid(objResultMap.get("batchid").toString());
		resultUsedMaterial.setNmaterialcode(objInventory.getMaterial().getNmaterialcode());
		resultUsedMaterial.setNmaterialcategorycode(objInventory.getMaterialcategory().getNmaterialcatcode());
		resultUsedMaterial.setNinventorycode(objInventory.getNmaterialinventorycode());
		resultUsedMaterial.setNmaterialtypecode(objInventory.getMaterialtype().getNmaterialtypecode());
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

		resultUsedMaterial.setIsreturn(1);
		resultUsedMaterial.setNqtyused(previousUsedQuantity);

		elnresultUsedMaterialRepository.save(resultUsedMaterial);
		elnmaterialInventoryRepository.save(objInventory);

		return new ResponseEntity<>(resultUsedMaterial, HttpStatus.OK);
	}

	public List<LSnotification> updateNotificationOnInventory(ElnmaterialInventory objInventory, String task,
			LScfttransaction cft, Double getQtyLeft, Date date) {

		List<LSnotification> lstnotifications = new ArrayList<LSnotification>();

		List<ElnresultUsedMaterial> objLstTransactions = objInventory.getResultusedmaterial();

		List<LSuserMaster> objLstuser = objLstTransactions.stream().map(ElnresultUsedMaterial::getCreatedbyusercode)
				.filter(Objects::nonNull).collect(Collectors.toList());

		if (!objLstuser.isEmpty()) {
			List<Integer> objnotifyuser = objLstuser.stream().map(LSuserMaster::getUsercode)
					.collect(Collectors.toList());

			objnotifyuser = objnotifyuser.stream().distinct().collect(Collectors.toList());

			if (objnotifyuser.contains(cft.getLsuserMaster())) {

				LSuserMaster objUser = new LSuserMaster();
				objUser.setUsercode(cft.getLsuserMaster());

				String details = "";

				if (task.equals("INVENTORYQTYNOTIFICATION")) {

					details = "{\"inventoryid\":\"" + objInventory.getSinventoryid() + "\",  " + "\"qtyleft\":\""
							+ getQtyLeft + "\",  " + "\"notificationamount\":\"" + objInventory.getNqtynotification()
							+ "\"}";

				} else if (task.equals("EXPIRYDATE")) {
					details = "{\"inventoryid\":\"" + objInventory.getSinventoryid() + "\",  " + "\"daysleft\":\""
							+ date + "\"}";
				} else {
					details = "{\"inventoryid\":\"" + objInventory.getSinventoryid() + "\",  " + "\"daysleft\":\""
							+ getQtyLeft + "\"}";
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

	@SuppressWarnings("unchecked")
	public ResponseEntity<Object> createEquipmentResultUsed(Map<String, Object> inputMap)
			throws JsonParseException, JsonMappingException, IOException {

		ObjectMapper Objmapper = new ObjectMapper();

		final LScfttransaction cft = Objmapper.convertValue(inputMap.get("silentAudit"), LScfttransaction.class);
		final Equipment objEquipFromMap = Objmapper.convertValue(inputMap.get("selectedEquipment"), Equipment.class);
		final Map<String, Object> objResultMap = (Map<String, Object>) inputMap.get("resultObject");

		final LStestmasterlocal objTest = new LStestmasterlocal();
		objTest.setTestcode((Integer) objResultMap.get("testcode"));

		Equipment objInventory = equipmentRepository.findOne(objEquipFromMap.getNequipmentcode());

		LSuserMaster objUser = new LSuserMaster();
		objUser.setUsercode(cft.getLsuserMaster());

		ElnresultEquipment resultEquipment = new ElnresultEquipment();
		if (objTest.getTestcode() != -1) {
			LStestmasterlocal isPresent = lStestmasterlocalRepository.findBytestcode(objTest.getTestcode());
			if (isPresent != null) {
				resultEquipment.setLstestmasterlocal(objTest);
			} else {
				resultEquipment.setLstestmasterlocal(null);
			}
		}
		resultEquipment.setCreatedby(objUser);
		resultEquipment.setBatchid(objResultMap.get("batchid").toString());
		resultEquipment.setNequipmentcode(objInventory.getNequipmentcode());
		resultEquipment.setNequipmentcatcode(objInventory.getEquipmentcategory().getNequipmentcatcode());
		resultEquipment.setNequipmenttypecode(objInventory.getEquipmenttype().getNequipmenttypecode());
		resultEquipment.setOrdercode(Long.valueOf(objResultMap.get("ordercode").toString()));
		resultEquipment.setTransactionscreen(Integer.parseInt(objResultMap.get("transactionscreen").toString()));
		resultEquipment.setTemplatecode(Integer.parseInt(objResultMap.get("templatecode").toString()));
		resultEquipment.setNstatus(1);
		resultEquipment.setResponse(new Response());
		resultEquipment.getResponse().setStatus(true);
		try {
			resultEquipment.setCreateddate(commonfunction.getCurrentUtcTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}

		elnresultEquipmentRepository.save(resultEquipment);

		return new ResponseEntity<>(resultEquipment, HttpStatus.OK);
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

		List<Materialget> lstMaterials = new ArrayList<Materialget>();
//		List<MaterialInventory> lstMaterialInventories = new ArrayList<MaterialInventory>();

		lstMaterials = elnmaterialRepository.findByNstatusAndNsitecodeOrderByNmaterialcodeDesc(1,
				(Integer) inputMap.get("sitecode"));

//		if (!lstMaterials.isEmpty()) {
//
//			lstMaterialInventories = materialInventoryRepository
////					.findByNmaterialcodeAndNtransactionstatusOrderByNmaterialinventorycode(lstMaterials.get(0).getNmaterialcode(),28);
//					.findByNsitecodeAndNtransactionstatusOrderByNmaterialinventorycodeDesc(
//							(Integer) inputMap.get("sitecode"), 28);
//		}

		rtnMap.put("listedMaterial", lstMaterials);
//		rtnMap.put("listedMaterialInventory", lstMaterialInventories);

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

	public List<LSnotification> updateNotificationOnInventory(MaterialInventory objInventory, String task,
			LScfttransaction cft, Double getQtyLeft, Date date) {

		List<LSnotification> lstnotifications = new ArrayList<LSnotification>();

		List<MaterialInventoryTransaction> objLstTransactions = objInventory.getMaterialInventoryTransactions();

		List<LSuserMaster> objLstuser = objLstTransactions.stream()
				.map(MaterialInventoryTransaction::getIssuedbyusercode).filter(Objects::nonNull)
				.collect(Collectors.toList());

		if (!objLstuser.isEmpty()) {
			List<Integer> objnotifyuser = objLstuser.stream().map(LSuserMaster::getUsercode)
					.collect(Collectors.toList());

			objnotifyuser = objnotifyuser.stream().distinct().collect(Collectors.toList());

			if (objnotifyuser.contains(cft.getLsuserMaster())) {

				LSuserMaster objUser = new LSuserMaster();
				objUser.setUsercode(cft.getLsuserMaster());

				String details = "";

				if (task.equals("INVENTORYQTYNOTIFICATION")) {

					details = "{\"inventoryid\":\"" + objInventory.getSinventoryid() + "\",  " + "\"qtyleft\":\""
							+ getQtyLeft + "\",  " + "\"notificationamount\":\"" + objInventory.getNqtynotification()
							+ "\"}";

				} else if (task.equals("EXPIRYDATE")) {
					details = "{\"inventoryid\":\"" + objInventory.getSinventoryid() + "\",  " + "\"daysleft\":\""
							+ date + "\"}";
				} else {
					details = "{\"inventoryid\":\"" + objInventory.getSinventoryid() + "\",  " + "\"daysleft\":\""
							+ getQtyLeft + "\"}";
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

//	public void updateMaterialInventoryNotification(Map<String, Object> inputMap) {
//		final Map<String, Object> threadMap = inputMap;
//		new Thread(() -> {
//			updateMaterialInventoryNotificationviaThread(threadMap);
//		}).start();
//	}

	public void updateMaterialInventoryNotification(Map<String, Object> inputMap) throws ParseException {
		final Map<String, Object> threadMap = inputMap;
//		new Thread(() -> {
		updateMaterialInventoryNotificationvia(threadMap);
//		}).start();
	}

	public void updateMaterialInventoryNotificationvia(Map<String, Object> inputMap) throws ParseException {

		ObjectMapper Objmapper = new ObjectMapper();

//		Integer sitecode = (Integer) inputMap.get("sitecode");
		Date currentDate = commonfunction.getCurrentUtcTime();
		final LScfttransaction cft = Objmapper.convertValue(inputMap.get("silentAudit"), LScfttransaction.class);

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
		calendar.add(Calendar.DAY_OF_YEAR, -2);
		// Set the ending time of the day
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		currentDate = calendar.getTime();

		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTime(currentDate);

		List<ElnmaterialInventory> objInventories = elnmaterialInventoryRepository
				.findByNsitecodeAndNtransactionstatusAndIsexpiryAndExpirydateBetween(cft.getLssitemaster(), 28, true,
						currentDate, endDate);
		List<ElnmaterialInventory> expiredInvent = new ArrayList<ElnmaterialInventory>();

		// sample notification
		List<Sample> objsamples = SampleRepository
				.findByNsitecodeAndNtransactionstatusAndOpenexpiryAndExpirydateBetween(cft.getLssitemaster(), 28, true,
						currentDate, endDate);
		List<Sample> expiredsample = new ArrayList<Sample>();

		List<LSnotification> lstLSnotifications = new ArrayList<LSnotification>();

		objInventories.stream().peek(objInventory -> {
			if (objInventory.getIsexpiry()) {
				Date expDate = objInventory.getExpirydate();

				try {
					long millisecondsDate1 = commonfunction.getCurrentUtcTime().getTime();
					long millisecondsDate2 = expDate.getTime();

					if (millisecondsDate1 < millisecondsDate2) {
						System.out.println("Date 1 is before Date 2");
						lstLSnotifications.addAll(
								updateNotificationOnELNInventory(objInventory, "EXPIRYDATE", cft, 0.0, expDate));
					} else if (millisecondsDate1 > millisecondsDate2) {
						System.out.println("Date 1 is after Date 2");
						objInventory.setNtransactionstatus(55);
						expiredInvent.add(objInventory);
						lstLSnotifications.addAll(
								updateNotificationOnELNInventory(objInventory, "EXPIRYREACHED", cft, 0.0, expDate));
					} else {
						System.out.println("Date 1 and Date 2 are equal");
						objInventory.setNtransactionstatus(55);
						expiredInvent.add(objInventory);
						lstLSnotifications.addAll(
								updateNotificationOnELNInventory(objInventory, "EXPIRYREACHED", cft, 0.0, expDate));
					}

				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).collect(Collectors.toList());

		objsamples.stream().peek(objsample -> {
			if (objsample.getOpenexpiry()) {
				Date expDate = objsample.getExpirydate();

				try {
					long millisecondsDate1 = commonfunction.getCurrentUtcTime().getTime();
					long millisecondsDate2 = expDate.getTime();

					if (millisecondsDate1 < millisecondsDate2) {
						System.out.println("Date 1 is before Date 2");
						lstLSnotifications
								.addAll(updateNotificationOnELNSample(objsample, "EXPIRYDATE", cft, 0.0, expDate));
					} else if (millisecondsDate1 > millisecondsDate2) {
						System.out.println("Date 1 is after Date 2");
						objsample.setNtransactionstatus(55);
						expiredsample.add(objsample);
						lstLSnotifications
								.addAll(updateNotificationOnELNSample(objsample, "EXPIRYREACHED", cft, 0.0, expDate));
					} else {
						System.out.println("Date 1 and Date 2 are equal");
						objsample.setNtransactionstatus(55);
						expiredsample.add(objsample);
						lstLSnotifications
								.addAll(updateNotificationOnELNSample(objsample, "EXPIRYREACHED", cft, 0.0, expDate));
					}

				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).collect(Collectors.toList());

		lsnotificationRepository.save(lstLSnotifications);
		elnmaterialInventoryRepository.save(expiredInvent);
		SampleRepository.save(expiredsample);
	}

	public List<LSnotification> updateNotificationOnELNSample(Sample objsample, String task, LScfttransaction cft,
			double getQtyLeft, Date date) {

		List<LSnotification> lstnotifications = new ArrayList<LSnotification>();

		List<ElnresultUsedSample> objLstTransactions = ElnresultUsedSampleRepository
				.findBySamplecode(objsample.getSamplecode());

		List<LSuserMaster> objLstuser = objLstTransactions.stream().map(ElnresultUsedSample::getCreatedbyusercode)
				.filter(Objects::nonNull).collect(Collectors.toList());

		if (!objLstuser.isEmpty()) {
			List<Integer> objnotifyuser = objLstuser.stream().map(LSuserMaster::getUsercode)
					.collect(Collectors.toList());

			objnotifyuser = objnotifyuser.stream().distinct().collect(Collectors.toList());

			if (objnotifyuser.contains(cft.getLsuserMaster())) {

				LSuserMaster objUser = new LSuserMaster();
				objUser.setUsercode(cft.getLsuserMaster());

				String details = "";

				if (task.equals("INVENTORYQTYNOTIFICATION")) {

					details = "{\"inventoryid\":\"" + objsample.getSamplename() + "\",  " + "\"qtyleft\":\""
							+ getQtyLeft + "\",  " + "\"notificationamount\":\"" + objsample.getNqtynotification()
							+ "\"}";

				} else if (task.equals("EXPIRYDATE")) {
					details = "{\"inventoryid\":\"" + objsample.getSamplename() + "\",  " + "\"daysleft\":\"" + date
							+ "\"}";
				} else {
					details = "{\"inventoryid\":\"" + objsample.getSamplename() + "\",  " + "\"daysleft\":\""
							+ getQtyLeft + "\"}";
				}

				String notification = details;

				LSnotification objnotify = new LSnotification();

				objnotify.setNotifationto(objUser);
				objnotify.setNotifationfrom(objUser);
				objnotify.setNotificationdate(cft.getTransactiondate());
				objnotify.setNotification(task);
				objnotify.setNotificationdetils(notification);
				objnotify.setIsnewnotification(1);
				objnotify.setNotificationpath("/sample");
				objnotify.setNotificationfor(1);

				lstnotifications.add(objnotify);

				return lstnotifications;
			}
		} else {
			List<Integer> objnotifyuser = new ArrayList<Integer>();
			Integer getUsrCode = objsample.getCreateby() != null ? objsample.getCreateby().getUsercode() : null;
			if (getUsrCode != null) {
				objnotifyuser.add(getUsrCode);

				if (objnotifyuser.contains(cft.getLsuserMaster())) {

					LSuserMaster objUser = new LSuserMaster();
					objUser.setUsercode(cft.getLsuserMaster());

					String details = "";

					if (task.equals("INVENTORYQTYNOTIFICATION")) {

						details = "{\"inventoryid\":\"" + objsample.getSamplename() + "\",  " + "\"qtyleft\":\""
								+ getQtyLeft + "\",  " + "\"notificationamount\":\"" + objsample.getNqtynotification()
								+ "\"}";

					} else if (task.equals("EXPIRYDATE")) {
						details = "{\"inventoryid\":\"" + objsample.getSamplename() + "\",  " + "\"daysleft\":\"" + date
								+ "\"}";
					} else {
						details = "{\"inventoryid\":\"" + objsample.getSamplename() + "\",  " + "\"daysleft\":\""
								+ getQtyLeft + "\"}";
					}

					String notification = details;

					LSnotification objnotify = new LSnotification();

					objnotify.setNotifationto(objUser);
					objnotify.setNotifationfrom(objUser);
					objnotify.setNotificationdate(cft.getTransactiondate());
					objnotify.setNotification(task);
					objnotify.setNotificationdetils(notification);
					objnotify.setIsnewnotification(1);
					objnotify.setNotificationpath("/sample");
					objnotify.setNotificationfor(1);

					lstnotifications.add(objnotify);

					return lstnotifications;
				}
			}

		}
		return lstnotifications;

	}

	public List<LSnotification> updateNotificationOnELNInventory(ElnmaterialInventory objInventory, String task,
			LScfttransaction cft, Double getQtyLeft, Date date) {

		List<LSnotification> lstnotifications = new ArrayList<LSnotification>();

		List<ElnresultUsedMaterial> objLstTransactions = objInventory.getResultusedmaterial();

		List<LSuserMaster> objLstuser = objLstTransactions.stream().map(ElnresultUsedMaterial::getCreatedbyusercode)
				.filter(Objects::nonNull).collect(Collectors.toList());

		if (!objLstuser.isEmpty()) {
			List<Integer> objnotifyuser = objLstuser.stream().map(LSuserMaster::getUsercode)
					.collect(Collectors.toList());

			objnotifyuser = objnotifyuser.stream().distinct().collect(Collectors.toList());

			if (objnotifyuser.contains(cft.getLsuserMaster())) {

				LSuserMaster objUser = new LSuserMaster();
				objUser.setUsercode(cft.getLsuserMaster());

				String details = "";

				if (task.equals("INVENTORYQTYNOTIFICATION")) {

					details = "{\"inventoryid\":\"" + objInventory.getSinventoryid() + "\",  " + "\"qtyleft\":\""
							+ getQtyLeft + "\",  " + "\"notificationamount\":\"" + objInventory.getNqtynotification()
							+ "\"}";

				} else if (task.equals("EXPIRYDATE")) {
					details = "{\"inventoryid\":\"" + objInventory.getSinventoryid() + "\",  " + "\"daysleft\":\""
							+ date + "\"}";
				} else {
					details = "{\"inventoryid\":\"" + objInventory.getSinventoryid() + "\",  " + "\"daysleft\":\""
							+ getQtyLeft + "\"}";
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
		} else {
			List<Integer> objnotifyuser = new ArrayList<Integer>();
			Integer getUsrCode = objInventory.getCreatedby().getUsercode();
			if (getUsrCode != null) {
				objnotifyuser.add(getUsrCode);

				if (objnotifyuser.contains(cft.getLsuserMaster())) {

					LSuserMaster objUser = new LSuserMaster();
					objUser.setUsercode(cft.getLsuserMaster());

					String details = "";

					if (task.equals("INVENTORYQTYNOTIFICATION")) {

						details = "{\"inventoryid\":\"" + objInventory.getSinventoryid() + "\",  " + "\"qtyleft\":\""
								+ getQtyLeft + "\",  " + "\"notificationamount\":\""
								+ objInventory.getNqtynotification() + "\"}";

					} else if (task.equals("EXPIRYDATE")) {
						details = "{\"inventoryid\":\"" + objInventory.getSinventoryid() + "\",  " + "\"daysleft\":\""
								+ date + "\"}";
					} else {
						details = "{\"inventoryid\":\"" + objInventory.getSinventoryid() + "\",  " + "\"daysleft\":\""
								+ getQtyLeft + "\"}";
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

		}
		return lstnotifications;
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

	@SuppressWarnings("unchecked")
	public ResponseEntity<Object> createMaterialResultUsedForList(Map<String, Object> inputMap) {
		ObjectMapper objectMapper = new ObjectMapper();
		final LScfttransaction cft = objectMapper.convertValue(inputMap.get("silentAudit"), LScfttransaction.class);
		final List<Map<String, Object>> objResultMap = (List<Map<String, Object>>) inputMap.get("resultObject");
		objResultMap.forEach(item -> {
			int nmaterialinventorycodeValue = (Integer) item.get("nmaterialinventorycode");
			ElnmaterialInventory objInventory = elnmaterialInventoryRepository
					.findByNmaterialinventorycode(nmaterialinventorycodeValue);

			LStestmasterlocal objTest = new LStestmasterlocal();
			objTest.setTestcode((Integer) item.get("testcode"));

			Double getIssuedQty = Double.parseDouble(item.get("issuedQuantity").toString());
////	        Double getIssuedQty = Double.parseDouble(objInventory.getSavailablequantity());
			Double getUsedQty = Double.parseDouble(item.get("usedQuantity").toString());
//	        Double getQtyLeft = getIssuedQty - getUsedQty;
			Double getQtyLeft = Double.parseDouble(item.get("issuedQuantity").toString());
			LSuserMaster objUser = new LSuserMaster();
			objUser.setUsercode(cft.getLsuserMaster());
			ElnresultUsedMaterial resultUsedMaterial = new ElnresultUsedMaterial();
			if (objTest.getTestcode() != -1) {
				resultUsedMaterial.setTestcode(objTest);
			}

			resultUsedMaterial.setCreatedbyusercode(objUser);
			resultUsedMaterial.setNqtyissued(getIssuedQty);
			resultUsedMaterial.setNqtyleft(getQtyLeft);
			resultUsedMaterial.setNqtyused(getUsedQty);
			resultUsedMaterial.setBatchid(item.get("batchid").toString());
			resultUsedMaterial.setNmaterialcode(objInventory.getMaterial().getNmaterialcode());
			resultUsedMaterial.setNmaterialcategorycode(objInventory.getMaterialcategory().getNmaterialcatcode());
			resultUsedMaterial.setNinventorycode(objInventory.getNmaterialinventorycode());
			resultUsedMaterial.setNmaterialtypecode(objInventory.getMaterialtype().getNmaterialtypecode());
			resultUsedMaterial.setOrdercode(Long.valueOf(item.get("ordercode").toString()));
			resultUsedMaterial.setTransactionscreen(Integer.parseInt(item.get("transactionscreen").toString()));
			resultUsedMaterial.setTemplatecode(Integer.parseInt(item.get("templatecode").toString()));
			resultUsedMaterial.setJsondata(cft.getComments());
			resultUsedMaterial.setNstatus(1);

			try {
				resultUsedMaterial.setCreateddate(commonfunction.getCurrentUtcTime());
			} catch (ParseException e) {
				// Handle the exception appropriately, e.g., log it or return an error response
				e.printStackTrace();
			}
			objInventory.setSavailablequantity(getQtyLeft.toString());

			elnresultUsedMaterialRepository.save(resultUsedMaterial);
			elnmaterialInventoryRepository.save(objInventory);
		});

		return new ResponseEntity<>("true", HttpStatus.OK);
	}

	public ResponseEntity<Object> getMaterialLst4NewMaterial(Map<String, Object> inputMap)
			throws JsonProcessingException {

		List<Material> lstMaterial = materialRepository.findAll();

		if (!lstMaterial.isEmpty()) {

			List<Elnmaterial> objLstElnmaterials = new ArrayList<Elnmaterial>();

			ObjectMapper mapper = new ObjectMapper();

			// Create an empty JSON object
			ObjectNode emptyObject = mapper.createObjectNode();

			// Serialize it to a JSON string
			String jsonString = mapper.writeValueAsString(emptyObject);

			lstMaterial.stream().peek(objMaterial -> {

				Elnmaterial objElnmaterial = new Elnmaterial();

				JSONObject objUnit = (JSONObject) commonfunction
						.getInventoryValuesFromJsonString(objMaterial.getJsondata(), "Basic Unit").get("rtnObj");

				if (objUnit != null) {
					Integer unitcode = Integer.parseInt(objUnit.get("value").toString());

					Unit newUnit = new Unit();
					newUnit.setNunitcode(unitcode);
					if (unitcode != -1) {
						objElnmaterial.setUnit(newUnit);
					} else {
						objElnmaterial.setUnit(null);
					}
				} else {
					objElnmaterial.setUnit(null);
				}

				MaterialCategory objCategory = new MaterialCategory();
				objCategory.setNmaterialcatcode(objMaterial.getNmaterialcatcode());

				MaterialType objType = new MaterialType();
				objType.setNmaterialtypecode(objMaterial.getNmaterialtypecode());

				LSuserMaster objLSuserMaster = new LSuserMaster();
				objLSuserMaster.setUsercode(1);

				objElnmaterial.setMaterialcategory(objCategory);
				objElnmaterial.setMaterialtype(objType);
				objElnmaterial.setNsitecode(objMaterial.getNsitecode());
				objElnmaterial.setSection(null);
				objElnmaterial.setCreateby(objLSuserMaster);
				try {
					objElnmaterial.setCreateddate(commonfunction.getCurrentUtcTime());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				objElnmaterial.setSmaterialname(objMaterial.getSmaterialname());
				objElnmaterial.setJsondata(jsonString);
				objElnmaterial.setNstatus(objMaterial.getNstatus());
				objElnmaterial.setExpirytype(0);
				objElnmaterial.setQuarantine(false);
				objElnmaterial.setOpenexpiry(false);
				objElnmaterial.setNmaterialcode(objMaterial.getNmaterialcode());
				objElnmaterial.setRemarks("");

				objLstElnmaterials.add(objElnmaterial);
			}).collect(Collectors.toList());

			elnmaterialRepository.save(objLstElnmaterials);

			getMaterialInvLst4NewMaterialInv(inputMap);
		}

		return null;
	}

	public ResponseEntity<Object> getMaterialInvLst4NewMaterialInv(Map<String, Object> inputMap)
			throws JsonProcessingException {

		List<MaterialInventory> lstMaterial = materialInventoryRepository.findAll();

		if (!lstMaterial.isEmpty()) {

			List<ElnmaterialInventory> objLstElnmaterials = new ArrayList<ElnmaterialInventory>();

			ObjectMapper mapper = new ObjectMapper();

			// Create an empty JSON object
			ObjectNode emptyObject = mapper.createObjectNode();

			// Serialize it to a JSON string
			String jsonString = mapper.writeValueAsString(emptyObject);

			lstMaterial.stream().peek(objMaterial -> {

				ElnmaterialInventory objInv = new ElnmaterialInventory();

				String receivedString = commonfunction
						.getInventoryValuesFromJsonString(objMaterial.getJsondata(), "Received Quantity").get("rtnObj")
						.toString();
				String sinventoryid = commonfunction
						.getInventoryValuesFromJsonString(objMaterial.getJsondata(), "Inventory ID").get("rtnObj")
						.toString();

				Elnmaterial objElnmaterial = elnmaterialRepository.findOne(objMaterial.getNmaterialcode());

				MaterialCategory objCategory = new MaterialCategory();
				objCategory.setNmaterialcatcode(objMaterial.getNmaterialcatcode());

				MaterialType objType = new MaterialType();
				objType.setNmaterialtypecode(objMaterial.getNmaterialtypecode());

				LSuserMaster objLSuserMaster = new LSuserMaster();
				objLSuserMaster.setUsercode(1);

				objInv.setSreceivedquantity(receivedString);
				objInv.setSavailablequantity(receivedString);
				objInv.setJsondata(jsonString);
				objInv.setNstatus(objMaterial.getNstatus());
				objInv.setRemarks("");
				objInv.setMaterialcategory(objCategory);
				objInv.setMaterialtype(objType);
				objInv.setMaterial(objElnmaterial);
				objInv.setNsitecode(objMaterial.getNsitecode());
				objInv.setSection(null);
				objInv.setCreatedby(objLSuserMaster);
				objInv.setUnit(objElnmaterial.getUnit());
				objInv.setIsexpiry(objMaterial.getIsexpiryneed());
				objInv.setExpirydate(objMaterial.getExpirydate());
				objInv.setNtransactionstatus(objMaterial.getNtransactionstatus());
				objInv.setSinventoryid(sinventoryid);
				objInv.setNmaterialinventorycode(objMaterial.getNmaterialinventorycode());

				try {
					objInv.setCreateddate(commonfunction.getCurrentUtcTime());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				objLstElnmaterials.add(objInv);

			}).collect(Collectors.toList());

			elnmaterialInventoryRepository.save(objLstElnmaterials);
		}
		return null;
	}

	public ResponseEntity<Object> getTransactionResultsByDate(Map<String, Object> inputMap) {

		Map<String, Object> objmap = new LinkedHashMap<String, Object>();

		long longFromValue = Long.parseLong(inputMap.get("fromdate").toString());
		long longToValue = Long.parseLong(inputMap.get("todate").toString());
		Integer ninventorycode = (Integer) inputMap.get("ninventorycode");
		Integer screencode = (Integer) inputMap.get("selectedScreen");
		Date fromDate = new Date(longFromValue);
		Date toDate = new Date(longToValue);

		if (screencode == -1) {
			List<ElnresultUsedMaterial> lstUsedMaterials = elnresultUsedMaterialRepository
					.findByNinventorycodeAndCreateddateBetweenOrderByNresultusedmaterialcodeDesc(ninventorycode,
							fromDate, toDate);

			objmap.put("resultusedmaterial", lstUsedMaterials);
		} else {
			List<ElnresultUsedMaterial> lstUsedMaterials = elnresultUsedMaterialRepository
					.findByNinventorycodeAndTransactionscreenAndCreateddateBetweenOrderByNresultusedmaterialcodeDesc(
							ninventorycode, screencode, fromDate, toDate);

			objmap.put("resultusedmaterial", lstUsedMaterials);
		}
		return new ResponseEntity<>(objmap, HttpStatus.OK);
	}

	public ResponseEntity<Object> getEquipmentTransactionResult(Equipment objEquipment) {
		Map<String, Object> objmap = new LinkedHashMap<String, Object>();

		List<ElnresultEquipment> objList = elnresultEquipmentRepository
				.findByNequipmentcode(objEquipment.getNequipmentcode());
		List<EquipmentHistory> lstCal = equipmentHistoryRepository
				.findByNequipmentcodeAndHistorytype(objEquipment.getNequipmentcode(), 1);
		List<EquipmentHistory> lstMain = equipmentHistoryRepository
				.findByNequipmentcodeAndHistorytype(objEquipment.getNequipmentcode(), 2);

		objmap.put("resultusedmaterial", objList);
		objmap.put("resultCallibrated", lstCal);
		objmap.put("resultMaintanance", lstMain);

		return new ResponseEntity<>(objmap, HttpStatus.OK);
	}

	public List<Materialget> getMaterials(LSuserMaster objClass) {
		return elnmaterialRepository.findByNstatusAndNsitecodeOrderByNmaterialcodeDesc(1,
				objClass.getLssitemaster().getSitecode());
	}

	public ResponseEntity<Object> QuantityReduceonSample(Map<String, Object> inputMap) {
		ObjectMapper Objmapper = new ObjectMapper();

		final LScfttransaction cft = Objmapper.convertValue(inputMap.get("silentAudit"), LScfttransaction.class);
		final Sample objSample = Objmapper.convertValue(inputMap.get("Sample"), Sample.class);
		final Map<String, Object> objResultMap = (Map<String, Object>) inputMap.get("resultObject");
		final LStestmasterlocal objTest = new LStestmasterlocal();
		Integer ActionType = Integer.parseInt(inputMap.get("ActionType").toString());
		objTest.setTestcode((Integer) objResultMap.get("testcode"));
		LSprojectmaster projectcode = Objmapper.convertValue(objResultMap.get("projectcode"), LSprojectmaster.class);

		Sample objSampleobj = SampleRepository.findBySamplecode(objSample.getSamplecode());

//		Double getIssuedQty = Double.parseDouble(objResultMap.get("issuedQuantity").toString());
		Double getIssuedQty = Double.parseDouble(objSampleobj.getQuantity().toString());
		Double getUsedQty = Double.parseDouble(objResultMap.get("usedQuantity").toString());
		Double getQtyLeft = null;
		Response response = new Response();
		ElnresultUsedSample ElnresultUsedSample = new ElnresultUsedSample();
		if (ActionType == 0) {
			double usedQuantity = Double.parseDouble(objResultMap.get("usedQuantity").toString());
			if (getIssuedQty >= usedQuantity) {
				getQtyLeft = getIssuedQty - usedQuantity;
				response.setStatus(true);
			} else {
				response.setStatus(false);
			}
		} else {
			double usedQuantity = Double.parseDouble(objResultMap.get("usedQuantity").toString());
			getQtyLeft = getIssuedQty + usedQuantity;
			response.setStatus(true);
		}

		ElnresultUsedSample.setResponse(response);
		if (response.getStatus()) {
			int qtyLeftAsInt = getQtyLeft.intValue();
			objSampleobj.setQuantity(qtyLeftAsInt);
			LSuserMaster objUser = new LSuserMaster();
			objUser.setUsercode(cft.getLsuserMaster());
			if (objTest.getTestcode() != -1) {
				LStestmasterlocal isPresent = lStestmasterlocalRepository.findBytestcode(objTest.getTestcode());
				if (isPresent != null) {
					ElnresultUsedSample.setTestcode(objTest);
				} else {
					ElnresultUsedSample.setTestcode(null);
				}
			}
			if(projectcode.getProjectcode()!=null) {
				ElnresultUsedSample.setProjectcode(projectcode);
			}
			if(ActionType!=null) {
				ElnresultUsedSample.setIsreturn(ActionType);
			}
			ElnresultUsedSample.setSamplecode(objSampleobj.getSamplecode());
			ElnresultUsedSample.setSamlename(objSampleobj.getSamplename());
			ElnresultUsedSample.setSamlesequenceid(objSampleobj.getSequenceid());
			ElnresultUsedSample.setCreatedbyusercode(objUser);
			ElnresultUsedSample.setNqtyissued(getIssuedQty);
			ElnresultUsedSample.setNqtyleft(getQtyLeft);
			ElnresultUsedSample.setNqtyused(getUsedQty);
			ElnresultUsedSample.setBatchid(objResultMap.get("batchid").toString());
			ElnresultUsedSample.setOrdercode(Long.valueOf(objResultMap.get("ordercode").toString()));
			ElnresultUsedSample
					.setTransactionscreen(Integer.parseInt(objResultMap.get("transactionscreen").toString()));
			ElnresultUsedSample.setTemplatecode(Integer.parseInt(objResultMap.get("templatecode").toString()));
			ElnresultUsedSample.setJsondata(cft.getComments());
			ElnresultUsedSample.setNstatus(1);
			ElnresultUsedSample.setResponse(new Response());
			ElnresultUsedSample.getResponse().setStatus(true);

			try {
				ElnresultUsedSample.setCreateddate(commonfunction.getCurrentUtcTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ElnresultUsedSample.setQtyleft(getQtyLeft.toString());

			
			ElnresultUsedSampleRepository.save(ElnresultUsedSample);
			SampleRepository.save(objSampleobj);

			if (objSampleobj.getNqtynotification() != null) {
				if (objSampleobj.getNqtynotification() <= getQtyLeft ? false : true) {
					List<LSnotification> lstLSnotifications = new ArrayList<LSnotification>();
					lstLSnotifications.addAll(updateNotificationOnELNSample(objSampleobj, "INVENTORYQTYNOTIFICATION",
							cft, getQtyLeft, new Date()));
					lsnotificationRepository.save(lstLSnotifications);
				}
			}
		} else {
			ElnresultUsedSample.setNqtyleft(getIssuedQty);
		}

		return new ResponseEntity<>(ElnresultUsedSample, HttpStatus.OK);
	}

	public ResponseEntity<Object> getsamplelist(List<Integer> sample) {
		Map<String, Object> objmap = new LinkedHashMap<String, Object>();
		List<Sample> samplelist = SampleRepository.findBySamplecodeIn(sample);
		objmap.put("samplelist", samplelist);
		return new ResponseEntity<>(objmap, HttpStatus.OK);
	}

	public ResponseEntity<Object> updateinventoryreusablecount(Map<String, Object> resultusedmaterial) {
		ObjectMapper Objmapper = new ObjectMapper();
		final ElnmaterialInventory ionvenobj = Objmapper.convertValue(resultusedmaterial.get("elnmaterialinventory"),
				ElnmaterialInventory.class);
		ElnmaterialInventory obj = elnmaterialInventoryReppository
				.findByNmaterialinventorycode(ionvenobj.getNmaterialinventorycode());
		if (ionvenobj.getReusablecount() != null) {
			obj.setReusablecount(ionvenobj.getReusablecount());
			if (ionvenobj.getReusablecount() == 0) {
				obj.setNtransactionstatus(-1);
			}
			elnmaterialInventoryReppository.save(obj);
		}

		final Map<String, Object> objResultMap = (Map<String, Object>) resultusedmaterial.get("resultObject");
		final LScfttransaction cft = Objmapper.convertValue(resultusedmaterial.get("silentAudit"),
				LScfttransaction.class);
		ElnresultUsedMaterial ElnresultUsedMaterial = new ElnresultUsedMaterial();
		LSuserMaster objUser = new LSuserMaster();
		objUser.setUsercode(cft.getLsuserMaster());
		if (ionvenobj.getReusablecount() != null) {
			Double getUsedQty = Double.parseDouble(ionvenobj.getReusablecount().toString());
			ElnresultUsedMaterial.setNqtyleft(getUsedQty);
			ElnresultUsedMaterial.setQtyleft(ionvenobj.getReusablecount().toString());
		}
		ElnresultUsedMaterial.setCreatedbyusercode(objUser);
		ElnresultUsedMaterial.setNqtyissued(1D);

		ElnresultUsedMaterial.setNqtyused(1D);
		ElnresultUsedMaterial.setNmaterialcode(obj.getMaterial().getNmaterialcode());
		ElnresultUsedMaterial.setNmaterialcategorycode(obj.getMaterialcategory().getNmaterialcatcode());
		ElnresultUsedMaterial.setNinventorycode(obj.getNmaterialinventorycode());
		ElnresultUsedMaterial.setNmaterialtypecode(obj.getMaterialtype().getNmaterialtypecode());
		ElnresultUsedMaterial.setBatchid(objResultMap.get("batchid").toString());
		ElnresultUsedMaterial.setOrdercode(Long.valueOf(objResultMap.get("ordercode").toString()));
		ElnresultUsedMaterial.setTransactionscreen(Integer.parseInt(objResultMap.get("transactionscreen").toString()));
		ElnresultUsedMaterial.setTemplatecode(Integer.parseInt(objResultMap.get("templatecode").toString()));
		ElnresultUsedMaterial.setJsondata(cft.getComments());
		ElnresultUsedMaterial.setNstatus(1);
		ElnresultUsedMaterial.setResponse(new Response());
		ElnresultUsedMaterial.getResponse().setStatus(true);
		ElnresultUsedMaterial.setShowfullcomment(1);

		try {
			ElnresultUsedMaterial.setCreateddate(commonfunction.getCurrentUtcTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ElnresultUsedMaterial.setIsreturn(0);
		elnresultUsedMaterialRepository.save(ElnresultUsedMaterial);
		return new ResponseEntity<>("true", HttpStatus.OK);
	}

}