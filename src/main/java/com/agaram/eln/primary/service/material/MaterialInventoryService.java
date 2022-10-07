package com.agaram.eln.primary.service.material;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.JSONArray;
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
import com.agaram.eln.primary.model.material.MaterialInventoryType;
import com.agaram.eln.primary.model.material.MaterialType;
import com.agaram.eln.primary.model.material.TransactionStatus;
import com.agaram.eln.primary.repository.material.MappedTemplateFieldPropsMaterialRepository;
import com.agaram.eln.primary.repository.material.MaterialCategoryRepository;
import com.agaram.eln.primary.repository.material.MaterialConfigRepository;
import com.agaram.eln.primary.repository.material.MaterialInventoryRepository;
import com.agaram.eln.primary.repository.material.MaterialInventoryTransactionRepository;
import com.agaram.eln.primary.repository.material.MaterialInventoryTypeRepository;
import com.agaram.eln.primary.repository.material.MaterialRepository;
import com.agaram.eln.primary.repository.material.MaterialTypeRepository;
import com.agaram.eln.primary.repository.material.TransactionStatusRepository;
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

	public ResponseEntity<Object> getMaterialInventory() throws Exception {
		Map<String, Object> objmap = new LinkedHashMap<String, Object>();

		List<MaterialType> lstMaterialType = materialTypeRepository.findByNstatus(1);
		objmap.put("MaterialType", lstMaterialType);

		List<MaterialCategory> lstMaterialCategory = materialCategoryRepository
				.findByNmaterialtypecode(lstMaterialType.get(0).getNmaterialtypecode());

		List<MaterialType> lstMaterialType1 = new ArrayList<MaterialType>();

		lstMaterialType1.add(lstMaterialType.get(0));

		objmap.put("SelectedMaterialType", lstMaterialType1);

		if (!lstMaterialCategory.isEmpty()) {
			objmap.put("MaterialCategoryMain", lstMaterialCategory);
			objmap.put("nmaterialcatcode", lstMaterialCategory.get(0).getNmaterialcatcode());

			List<Material> lstMaterial = materialRepository
					.findByNmaterialcatcode(lstMaterialCategory.get(0).getNmaterialcatcode());

			objmap.put("MaterialCombo", lstMaterial);

			List<MaterialConfig> lstMaterialConfig = materialConfigRepository
					.findByNmaterialtypecodeAndNformcode(lstMaterialType.get(0).getNmaterialtypecode(), 138);
			objmap.put("selectedTemplate", lstMaterialConfig);

			objmap.put("nmaterialtypecode", lstMaterialType.get(0).getNmaterialtypecode());
			objmap.put("nmaterialcatcode", lstMaterialCategory.get(0).getNmaterialcatcode());
			if (!lstMaterial.isEmpty()) {
				objmap.put("nmaterialcode", lstMaterial.get(0).getNmaterialcode());
			}

			objmap.putAll((Map<String, Object>) getMaterialInventoryByID(objmap).getBody());
		}
		return new ResponseEntity<>(objmap, HttpStatus.OK);
	}

	@SuppressWarnings("unchecked")
	public ResponseEntity<Object> getMaterialInventorycombo(Integer nmaterialtypecode, Integer nmaterialcatcode) {
		Map<String, Object> objmap = new LinkedHashMap<String, Object>();

		List<MaterialCategory> lstMaterialCategory = materialCategoryRepository
				.findByNmaterialtypecode(nmaterialtypecode);

		List<Map<String, Object>> lstMatObject = new ArrayList<Map<String, Object>>();

		if (!lstMaterialCategory.isEmpty()) {

			List<Material> lstMaterial = materialRepository
					.findByNmaterialcatcodeAndNmaterialtypecodeAndNstatus(nmaterialcatcode, nmaterialtypecode, 1);

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
	public ResponseEntity<Object> getMaterialInventoryByID(Map<String, Object> inputMap) {
		ObjectMapper objmapper = new ObjectMapper();
		Map<String, Object> objmap = new LinkedHashMap<String, Object>();
		List<Map<String, Object>> lstMaterialInventory = new ArrayList<Map<String, Object>>();
		String comboget = "";
		String strquery2 = "";
		if (inputMap.containsKey("nmaterialcode")) {

			List<MaterialInventory> objLstMaterialInventory = materialInventoryRepository
					.findByNmaterialcodeAndNmaterialcatcodeAndNmaterialtypecode((Integer) inputMap.get("nmaterialcode"),
							(Integer) inputMap.get("nmaterialcatcode"), (Integer) inputMap.get("nmaterialtypecode"));

			objLstMaterialInventory.stream().peek(f -> {

				try {

					Map<String, Object> resObj = new ObjectMapper().readValue(f.getJsonuidata(), Map.class);

					lstMaterialInventory.add(resObj);

				} catch (IOException e) {

					e.printStackTrace();
				}

			}).collect(Collectors.toList());

			objmap.put("MaterialInventory", lstMaterialInventory);

			if (!lstMaterialInventory.isEmpty()) {

				if (!(inputMap.containsKey("nflag"))) {

					objmap.put("SelectedMaterialInventory", lstMaterialInventory.get(lstMaterialInventory.size() - 1));
//					inputMap.put("nsectioncode",lstMaterialInventory.get(lstMaterialInventory.size() - 1).get("nsectioncode"));
//
//					objmap.putAll((Map<String, Object>) getQuantityTransactionByMaterialInvCode((int) lstMaterialInventory
//									.get(lstMaterialInventory.size() - 1).get("nmaterialinventorycode"), inputMap,
//									userInfo).getBody());
//					objmap.putAll((Map<String, Object>) getMaterialFile((int) lstMaterialInventory
//							.get(lstMaterialInventory.size() - 1).get("nmaterialinventorycode"), userInfo));
//					objmap.putAll((Map<String, Object>) getResultUsedMaterial((int) lstMaterialInventory
//							.get(lstMaterialInventory.size() - 1).get("nmaterialinventorycode"), userInfo).getBody());
//					objmap.putAll((Map<String, Object>) getMaterialInventoryhistory((int) lstMaterialInventory
//							.get(lstMaterialInventory.size() - 1).get("nmaterialinventorycode"), userInfo));

				} else {
//					String query3 = "select json_agg(a.jsonuidata) from  (select mi.nmaterialinventorycode,mi.jsonuidata||json_build_object('nmaterialinventorycode',mi.nmaterialinventorycode"
//							+ " ,'status'" + "	,(ts.jsondata->'stransdisplaystatus'->>'"
//							+ userInfo.getSlanguagetypecode()
//							+ "'),'Available Quantity',mt.jsonuidata->'Available Quantity')::jsonb " + " "
//							+ "as jsonuidata  from materialinventory mi,transactionstatus ts,materialinventorytransaction mt"
//							+ " where  mi.nmaterialinventorycode=" + inputMap.get("nmaterialinventorycode")
//							+ " and mt.nmaterialinventorycode=mi.nmaterialinventorycode"
//							+ " AND ts.ntranscode = ( mi.jsondata ->> 'ntranscode' ) :: INT " + " and mi.nstatus="
//							+ Enumeration.TransactionStatus.ACTIVE.gettransactionstatus() + ")a";
//
//					strMaterialInventory = jdbcTemplate.queryForObject(query3, String.class);
//					List<Map<String, Object>> lstMaterialInventory1 = new ArrayList<Map<String, Object>>();
//
//					lstMaterialInventory1 = objmapper.convertValue(getSiteLocalTimeFromUTCForRegTmplate(
//							strMaterialInventory, userInfo, true,
//							(int) inputMap.get("nmaterialtypecode") == Enumeration.TransactionStatus.STANDARDTYPE
//									.gettransactionstatus()
//											? 6
//											: (int) inputMap.get(
//													"nmaterialtypecode") == Enumeration.TransactionStatus.VOLUMETRICTYPE
//															.gettransactionstatus() ? 7 : 8,
//							(int) inputMap.get("nmaterialtypecode") == Enumeration.TransactionStatus.STANDARDTYPE
//									.gettransactionstatus()
//											? "MaterialInvStandard"
//											: (int) inputMap.get(
//													"nmaterialtypecode") == Enumeration.TransactionStatus.VOLUMETRICTYPE
//															.gettransactionstatus() ? "MaterialInvVolumetric"
//																	: "MaterialInvMaterialInventory"),
//							new TypeReference<List<Map<String, Object>>>() {
//							});
//					// Commented For JSONUIDATA
//					objmap.put("SelectedMaterialInventory", lstMaterialInventory1.get(0));
//					inputMap.put("nsectioncode", lstMaterialInventory1.get(0).get("nsectioncode"));
//					objmap.putAll((Map<String, Object>) getQuantityTransactionByMaterialInvCode(
//							(int) lstMaterialInventory1.get(0).get("nmaterialinventorycode"), inputMap, userInfo)
//									.getBody());
//					objmap.putAll((Map<String, Object>) getResultUsedMaterial(
//							(int) lstMaterialInventory1.get(0).get("nmaterialinventorycode"), userInfo).getBody());
//					objmap.putAll((Map<String, Object>) getMaterialFile(
//							(int) lstMaterialInventory1.get(0).get("nmaterialinventorycode"), userInfo));
//					objmap.putAll((Map<String, Object>) getMaterialInventoryhistory(
//							(int) lstMaterialInventory1.get(0).get("nmaterialinventorycode"), userInfo));

				}
			} else {
				objmap.put("SelectedMaterialInventory", lstMaterialInventory);
			}
		}

		List<MaterialType> lstMaterialType = materialTypeRepository
				.findByNmaterialtypecodeAndNstatusOrderByNmaterialtypecode((Integer) inputMap.get("nmaterialtypecode"), 1);

		objmap.put("SelectedMaterialType", lstMaterialType);

		List<MaterialCategory> lstMaterialCategory = new ArrayList<MaterialCategory>();

		if (inputMap.containsKey("nmaterialcatcode")) {
			
			MaterialCategory objMaterialCategory = materialCategoryRepository.findByNmaterialcatcodeAndNstatus((Integer) inputMap.get("nmaterialcatcode"), 1);

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
 
			Material lstMaterialCategory1 = materialRepository.findByNstatusAndNmaterialcode(1,(Integer) inputMap.get("nmaterialcode"));
			
			if (lstMaterialCategory1 != null) {
//				if (inputMap.containsKey("nmaterialcode")) {
//					objmap.put("SelectedMaterialCrumb", lstMaterialCategory1.get(0));
//				} else {
					objmap.put("SelectedMaterialCrumb", crumObjectMaterialCreated(lstMaterialCategory1));
//				}
			}
		}
//		objmap.putAll((Map<String, Object>) getQuantityTransactionTemplate(userInfo).getBody());
//		objmap.putAll((Map<String, Object>) getMaterialInventoryAdd((int) inputMap.get("nmaterialtypecode"), userInfo)
//				.getBody());
		
		objmap.put("DesignMappedFeilds", getTemplateDesignForMaterial(2, 138));
		
		List<Integer> lstParams = new ArrayList<Integer>();
//		List<Map<String, Object>> lstTransactionStatus = new ArrayList<Map<String, Object>>();
		
		lstParams.add(47);
		lstParams.add(48);
		
		List<TransactionStatus> lstTransStatus = transactionStatusRepository.findByNstatusAndNtranscodeIn(1, lstParams);
		
//		lstTransStatus.stream().peek(f -> {
//			
//			try {
//				Map<String, Object> resObj = new ObjectMapper().convertValue(f, Map.class);
//				
//				resObj.put("jsondata", new ObjectMapper().readValue(f.getJsondata(), Map.class));
//				
//				lstTransactionStatus.add(resObj);
//				
//			} catch (Exception e) {
//				
//				e.printStackTrace();
//			}
//
//		}).collect(Collectors.toList());
		
		objmap.put("TransactionType", lstTransStatus);
		lstParams = new ArrayList<Integer>();
		
		lstParams.add(1);
		lstParams.add(2);
		
		List<MaterialInventoryType> lstMaterialInventoryType = 
				materialInventoryTypeRepository.findByNstatusAndNinventorytypecodeIn(1, lstParams);
		
		System.out.println(lstMaterialInventoryType);
		objmap.put("MaterialInventoryType", lstMaterialInventoryType);
		
//		objmap.put("DesignMappedFeildsQuantityTransaction", getTemplateDesignForMaterial(9, userInfo.getNformcode()));

		
		
		return new ResponseEntity<>(objmap, HttpStatus.OK);

	}
	
	public Map<String, Object> getTemplateDesignForMaterial(int nmaterialconfigcode, int nformcode) {

		List<MappedTemplateFieldPropsMaterial> lstMappedTemplate = mappedTemplateFieldPropsMaterialRepository
				.findByNmaterialconfigcode(1);

		Map<String, Object> designObject = new HashMap<String, Object>();

		Map<String, Object> designChildObject = new HashMap<String, Object>();

		designChildObject.put("type", "jsonb");
		designChildObject.put("null", true);
		designChildObject.put("value", lstMappedTemplate.get(0).getJsondata());

		designObject.put("jsondata", designChildObject);

		return designObject;
	}
	
	public Map<String,Object> crumObjectMaterialCreated(Material objmaterial){
		
		Map<String,Object> rtnObj = new HashMap<>();
		
		Map<String, Object> objContent = commonfunction.getInventoryValuesFromJsonString(objmaterial.getJsondata(),"Material Name");
		
		String materialName = (String) objContent.get("rtnObj");
		
		objContent = commonfunction.getInventoryValuesFromJsonString(objmaterial.getJsondata(),"Basic Unit");
		objContent.put("isExpiryNeed", "No Expiry");
		objContent.put("nmaterialcode", objmaterial.getNmaterialcode());
		objContent.put("Material Name", materialName);
		objContent.put("nunitcode", objContent.get("rtnObj"));
		
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

		List<String> lstDateField = (List<String>) inputMap.get("DateList");
		JSONObject jsonObject = new JSONObject(inputMap.get("materialInventoryJson").toString());
		JSONObject jsonObjectInvTrans = new JSONObject(inputMap.get("MaterialInventoryTrans").toString());

		JSONObject jsonuidata = new JSONObject(inputMap.get("jsonuidata").toString());
		JSONObject jsonuidataTrans = new JSONObject(inputMap.get("jsonuidataTrans").toString());
		
		objmap.putAll((Map<String, Object>) getQuantityTransactionTemplate(138,-1).getBody());
		
		Date receiveDate = null;
		Date expiryDate = null;
		Date manufDate = null;
		String strcheck = "";
		String strformat = "";
		String updatestr = "";
		String insmat = "";
		boolean nflag = false;
		
		Material objGetMaterialJSON = materialRepository.findByNstatusAndNmaterialcode(1, (Integer) inputMap.get("nmaterialcode"));
		MaterialType objGetMaterialTypeJSON = materialTypeRepository.findByNmaterialtypecodeAndNstatus((Integer) jsonObject.get("nmaterialtypecode"), 1);
		
		String sformattype = "{yyyy}/{99999}";
		
		String strPrefix = (String) commonfunction.getInventoryValuesFromJsonString(objGetMaterialJSON.getJsondata(), "Prefix").get("rtnObj");
		
		String strtypePrefix = (String) commonfunction.getInventoryValuesFromJsonString(objGetMaterialTypeJSON.getJsondata(), "prefix").get("rtnObj");
		
//		final String dtransactiondate = getCurrentDateTime(objUserInfo).truncatedTo(ChronoUnit.SECONDS).toString()
//				.replace("T", " ").replace("Z", "");
		
//		if (!lstDateField.isEmpty()) {
//			jsonObject = (JSONObject) convertInputDateToUTCByZone(jsonObject, lstDateField, false, objUserInfo);
//		}
		
//		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		
//		String isExpiryNeed = jdbcTemplate.queryForObject("select (jsondata->>'Expiry Validations') from "
//				+ " material where nmaterialcode=" + (Integer) inputMap.get("nmaterialcode"), String.class);
//		
//		if (isExpiryNeed != null) {
//			if (isExpiryNeed.equals("Expiry date") && !jsonObject.has("Received Date & Time")
//					&& !jsonObject.has("Expiry Date & Time")) {
//				return new ResponseEntity<>(
//						commonFunction.getMultilingualMessage("IDS_ATLEASTADDEXPIRYDATEORRECEIVEDDATE",
//								userInfo.getSlanguagefilename()),
//						HttpStatus.CONFLICT);
//			} else if (isExpiryNeed.equals("Expiry policy") && !jsonObject.has("Received Date & Time")
//					&& !jsonObject.has("Expiry Date & Time")) {
//				return new ResponseEntity<>(
//						commonFunction.getMultilingualMessage("IDS_ATLEASTADDRECEIVEDDATEORRECEIVEDDATE",
//								userInfo.getSlanguagefilename()),
//						HttpStatus.CONFLICT);
//			}
//		}
//		if (jsonObject.has("Received Date & Time")) {
//			receiveDate = df.parse(jsonObject.get("Received Date & Time").toString());
//			if (receiveDate != null && expiryDate != null) {
//				if (receiveDate.compareTo(expiryDate) == 1) {
//					return new ResponseEntity<>(commonFunction.getMultilingualMessage("IDS_RECEIVEDATELESSTHANEXPEDATE",
//							userInfo.getSlanguagefilename()), HttpStatus.CONFLICT);
//				}
//			}
//			if (receiveDate != null && manufDate != null) {
//				if (receiveDate.compareTo(manufDate) == -1) {
//					return new ResponseEntity<>(
//							commonFunction.getMultilingualMessage("IDS_RECEIVEDATEGREATERTHANMANUFEDATE",
//									userInfo.getSlanguagefilename()),
//							HttpStatus.CONFLICT);
//				}
//			}
//		}
//		if (jsonObject.has("Expiry Date & Time")) {
//			expiryDate = df.parse(jsonObject.get("Expiry Date & Time").toString());
//			if (receiveDate != null && expiryDate != null) {
//				if (expiryDate.compareTo(receiveDate) == -1) {
//					return new ResponseEntity<>(commonFunction.getMultilingualMessage(
//							"IDS_EXPDATEGREATERTHANRECEIVEDATE", userInfo.getSlanguagefilename()), HttpStatus.CONFLICT);
//				}
//			}
//			if (expiryDate != null && manufDate != null) {
//				if (expiryDate.compareTo(manufDate) == -1) {
//					return new ResponseEntity<>(commonFunction.getMultilingualMessage("IDS_EXPDATEGREATERTHANMANUFDATE",
//							userInfo.getSlanguagefilename()), HttpStatus.CONFLICT);
//				}
//			}
//		}
//		if (jsonObject.has("Date Of Manufacturer")) {
//			manufDate = df.parse(jsonObject.get("Date Of Manufacturer").toString());
//			if (manufDate != null && expiryDate != null) {
//				if (manufDate.compareTo(expiryDate) == 1) {
//					return new ResponseEntity<>(commonFunction.getMultilingualMessage("IDS_MANUFDATELESSTHANEXPEDATE",
//							userInfo.getSlanguagefilename()), HttpStatus.CONFLICT);
//				}
//			}
//			if (manufDate != null && receiveDate != null) {
//				if (manufDate.compareTo(receiveDate) == 1) {
//					return new ResponseEntity<>(commonFunction.getMultilingualMessage(
//							"IDS_MANUFDATELESSTHANRECEIVEEDATE", userInfo.getSlanguagefilename()), HttpStatus.CONFLICT);
//				}
//			}
//		}

//		int seqnomaterialinv = getJdbcTemplate().queryForObject(
//				"select nsequenceno from  seqnomaterialmanagement where stablename='materialinventory'", Integer.class);
//		int seqnomaterialinvtrans = getJdbcTemplate().queryForObject(
//				"select nsequenceno from  seqnomaterialmanagement where stablename='materialinventorytransaction'",
//				Integer.class);
//		seqnomaterialinv++;
//		seqnomaterialinvtrans++;
//		inputMap.put("nmaterialinventorycode", seqnomaterialinv);
		
		JSONObject objmat = new JSONObject(objGetMaterialJSON.getJsondata());
		
		if (objmat.has("Reusable")) {
			
//			int reusableNeed = getJdbcTemplate()
//					.queryForObject("select (jsondata->'Reusable')::int from  material where nmaterialcode="
//							+ inputMap.get("nmaterialcode"), Integer.class);
			
			int reusableNeed = (int) commonfunction.getInventoryValuesFromJsonString(objGetMaterialJSON.getJsondata(), "Reusable").get("rtnObj");
			
			if (reusableNeed == Enumeration.TransactionStatus.YES.gettransactionstatus()) {
				
				nflag = true;
				
//				int matseq = getJdbcTemplate()
//						.queryForObject("select nsequenceno from seqnomaterialinventory where stablename= '"
//								+ (int) inputMap.get("nmaterialcode") + "'", Integer.class);
				
				int reusableCount = jsonObject.getInt("Received Quantity");
				
//				jsonObjectInvTrans.put("Transaction Date & Time", dtransactiondate);
//				jsonObjectInvTrans.put("noffsetTransaction Date & Time",
//						getCurrentDateTimeOffset(userInfo.getStimezoneid()));
				jsonObjectInvTrans.put("ntransactiontype",
						Enumeration.TransactionStatus.RECEIVED.gettransactionstatus());
				jsonObjectInvTrans.put("ninventorytranscode",
						Enumeration.TransactionStatus.OUTSIDE.gettransactionstatus());
				jsonObjectInvTrans.put("Received Quantity",
						Enumeration.TransactionStatus.ACTIVE.gettransactionstatus());
				jsonObjectInvTrans.put("nqtyissued", 0);
				/*
				 * jsonObjectInvTrans.put("namountleft",
				 * jdbcTemplate.queryForObject("select Cast(" +
				 * jsonObjectInvTrans.get("Received Quantity") + " as decimal (" +
				 * objmap.get("sprecision") + "))", String.class));
				 */
				
				jsonObject.put("Received Quantity", Enumeration.TransactionStatus.ACTIVE.gettransactionstatus());
				jsonObject.put("nqtyissued", 0);
				/*
				 * jsonObject.put("namountleft", jdbcTemplate.queryForObject("select Cast(" +
				 * jsonObject.get("Received Quantity") + " as decimal (" +
				 * objmap.get("sprecision") + "))", String.class));
				 */
				jsonObject.put("ntransactiontype", Enumeration.TransactionStatus.RECEIVED.gettransactionstatus());
				jsonObject = getmaterialquery(jsonObject, 1);

				jsonuidata.put("Received Quantity", Enumeration.TransactionStatus.ACTIVE.gettransactionstatus());
				jsonuidata.put("nqtyissued", 0);
				/*
				 * jsonuidata.put("namountleft", jdbcTemplate.queryForObject("select Cast(" +
				 * jsonuidata.get("Received Quantity") + " as decimal (" +
				 * objmap.get("sprecision") + "))", String.class));
				 */
				jsonuidata.put("ntransactiontype", Enumeration.TransactionStatus.RECEIVED.gettransactionstatus());
				jsonuidata = getmaterialquery(jsonuidata, 1);

//				jsonuidataTrans.put("Transaction Date & Time", dtransactiondate);
//				jsonuidataTrans.put("noffsetTransaction Date & Time",
//						getCurrentDateTimeOffset(userInfo.getStimezoneid()));

				jsonuidataTrans.put("ntransactiontype", Enumeration.TransactionStatus.RECEIVED.gettransactionstatus());
				jsonuidataTrans.put("ninventorytranscode",
						Enumeration.TransactionStatus.OUTSIDE.gettransactionstatus());
				jsonuidataTrans.put("Received Quantity", Enumeration.TransactionStatus.ACTIVE.gettransactionstatus());
				jsonuidataTrans.put("nqtyissued", 0);
				/*
				 * jsonuidataTrans.put("namountleft", jdbcTemplate.queryForObject("select Cast("
				 * + jsonuidataTrans.get("Received Quantity") + " as decimal (" +
				 * objmap.get("sprecision") + "))", String.class));
				 */

				SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:SS");
				Date resultdate = new Date(System.currentTimeMillis());

				for (int i = 0; i < reusableCount; i++) {
					
					MaterialInventory objSaveMaterialInventory = new MaterialInventory();
					
					objSaveMaterialInventory.setJsondata(jsonObject.toString());
					objSaveMaterialInventory.setJsonuidata(jsonuidata.toString());
					objSaveMaterialInventory.setNstatus(Enumeration.TransactionStatus.ACTIVE.gettransactionstatus());
					objSaveMaterialInventory.setNtransactionstatus((Integer) inputMap.get("ntransactionstatus"));
					objSaveMaterialInventory.setNsectioncode((Integer) inputMap.get("nsectioncode"));
					objSaveMaterialInventory.setNmaterialcode((Integer) inputMap.get("nmaterialcode"));
					
					objSaveMaterialInventory = materialInventoryRepository.save(objSaveMaterialInventory);
					
					if (strPrefix != null && !strPrefix.equals("")) {
						strformat = strtypePrefix + "/" + strPrefix + "/" + getfnFormat(objSaveMaterialInventory.getNmaterialinventorycode(), sformattype);
					} else {
						strformat = strtypePrefix + "/" + getfnFormat(objSaveMaterialInventory.getNmaterialinventorycode(), sformattype);
					}
					
					jsonObject.put("Inventory ID", strformat);
					jsonuidata.put("Inventory ID", strformat);

					jsonObjectInvTrans.put("Inventory ID", strformat);
					jsonuidataTrans.put("Inventory ID", strformat);
					
					if ((int) inputMap.get("needsectionwise") == Enumeration.TransactionStatus.YES
							.gettransactionstatus()) {
						jsonuidataTrans.put("Description","received");
					} else {
						jsonuidataTrans.put("Description","received");
					}
					
//					getJdbcTemplate().execute("INSERT INTO public.materialinventorytransaction("
//							+ "	nmaterialinventtranscode, nmaterialinventorycode,ninventorytranscode"
//							+ ",ntransactiontype,nsectioncode, nresultusedmaterialcode,nqtyreceived,nqtyissued, jsondata,jsonuidata, nsitecode, nstatus)"
//							+ "	VALUES (" + seqnomaterialinvtrans + ", " + seqnomaterialinv + ","
//							+ jsonObjectInvTrans.get("ninventorytranscode") + ","
//							+ jsonObjectInvTrans.get("ntransactiontype") + "," + jsonObjectInvTrans.get("nsectioncode")
//							+ ", -1,Cast(" + jsonObjectInvTrans.get("Received Quantity") + " as decimal ("
//							+ objmap.get("sprecision") + ")),Cast(" + jsonObjectInvTrans.get("nqtyissued")
//							+ " as decimal (" + objmap.get("sprecision") + ")), '"
//							+ ReplaceQuote(jsonObjectInvTrans.toString()) + "'::jsonb,'"
//							+ ReplaceQuote(jsonuidataTrans.toString()) + "'::jsonb, " + userInfo.getNtranssitecode()
//							+ ", " + Enumeration.TransactionStatus.ACTIVE.gettransactionstatus() + ");");
//					getJdbcTemplate().execute(insmat);
					
					MaterialInventoryTransaction objTransaction = new MaterialInventoryTransaction();
					
					objTransaction.setJsondata(jsonObjectInvTrans.toString());
					objTransaction.setJsonuidata(jsonuidataTrans.toString());
					objTransaction.setNstatus(Enumeration.TransactionStatus.ACTIVE.gettransactionstatus());
					objTransaction.setNmaterialinventorycode(objSaveMaterialInventory.getNmaterialinventorycode());
					objTransaction.setNinventorytranscode((Integer) jsonObjectInvTrans.get("ninventorytranscode"));
					objTransaction.setNtransactiontype((Integer) jsonObjectInvTrans.get("ntransactiontype"));
					objTransaction.setNsectioncode((Integer) jsonObjectInvTrans.get("nsectioncode"));
					
					materialInventoryTransactionRepository.save(objTransaction);

//					seqnomaterialinv++;
//					seqnomaterialinvtrans++;
					
					jsonUidataarray.put(new JSONObject(jsonuidata.toString()));
					jsonUidataarrayTrans.put(new JSONObject(jsonuidataTrans.toString()));
				}
				inputMap.put("reusableCount", reusableCount);
				inputMap.put("ntranscode", inputMap.get("ntransactionstatus"));

//				createMaterialInventoryhistory(inputMap);
//				
//				getJdbcTemplate().execute("update seqnomaterialinventory set nsequenceno=" + matseq
//						+ "  where stablename='" + (int) inputMap.get("nmaterialcode") + "' ");
			}
		}
	
//		if (nflag == false) {
//			
//			int matseq = getJdbcTemplate()
//					.queryForObject("select nsequenceno from seqnomaterialinventory where stablename= '"
//							+ (int) inputMap.get("nmaterialcode") + "'", Integer.class);
//			matseq++;
//			if (strPrefix != null && !strPrefix.equals("")) {
//				strformat = strtypePrefix + "/" + strPrefix + "/" + getfnFormat(matseq, sformattype);
//			} else {
//				strformat = strtypePrefix + "/" + getfnFormat(matseq, sformattype);
//			}
//			jsonObject.put("Inventory ID", strformat);
//			jsonObject.put("ntransactiontype", Enumeration.TransactionStatus.RECEIVED.gettransactionstatus());
//			jsonObject = getmaterialquery(jsonObject, 1);
//
//			jsonuidata.put("Inventory ID", strformat);
//			jsonuidata.put("ntransactiontype", Enumeration.TransactionStatus.RECEIVED.gettransactionstatus());
//			jsonuidata = getmaterialquery(jsonuidata, 1);
//
//			getJdbcTemplate().execute("update seqnomaterialinventory set nsequenceno=" + matseq + "  where stablename='"
//					+ (int) inputMap.get("nmaterialcode") + "' ");
//
//			insmat = "INSERT INTO materialinventory("
//					+ " nmaterialinventorycode,nmaterialcode,ntransactionstatus,nsectioncode,jsondata,jsonuidata, nstatus)"
//					+ " VALUES (" + seqnomaterialinv + "," + inputMap.get("nmaterialcode") + ", "
//					+ inputMap.get("ntransactionstatus") + "," + inputMap.get("nsectioncode") + ",'"
//					+ ReplaceQuote(jsonObject.toString()) + "'::jsonb, '" + ReplaceQuote(jsonuidata.toString())
//					+ "'::jsonb, " + Enumeration.TransactionStatus.ACTIVE.gettransactionstatus() + ");";
//			inputMap.put("nmaterialinventorycode", seqnomaterialinv);
//			inputMap.put("ntranscode", inputMap.get("ntransactionstatus"));
//			createMaterialInventoryhistory(inputMap, userInfo);
//			jsonObjectInvTrans.put("Inventory ID", strformat);
//			jsonObjectInvTrans.put("ntransactiontype", Enumeration.TransactionStatus.RECEIVE.gettransactionstatus());
//			jsonObjectInvTrans.put("Transaction Date & Time", dtransactiondate);
//			jsonObjectInvTrans.put("noffsetTransaction Date & Time",
//					getCurrentDateTimeOffset(userInfo.getStimezoneid()));
//
//			jsonObjectInvTrans.put("ninventorytranscode", Enumeration.TransactionStatus.OUTSIDE.gettransactionstatus());
//			jsonObjectInvTrans.put("nqtyissued", 0);
//			
//			jsonObjectInvTrans.put("namountleft",
//					jdbcTemplate.queryForObject("select Cast(" + jsonObjectInvTrans.get("Received Quantity")
//							+ " as decimal (" + objmap.get("sprecision") + "))", String.class));
//
//			jsonuidataTrans.put("Inventory ID", strformat);
//			jsonuidataTrans.put("ntransactiontype", Enumeration.TransactionStatus.RECEIVE.gettransactionstatus());
//			jsonuidataTrans.put("Transaction Date & Time", dtransactiondate);
//			jsonuidataTrans.put("noffsetTransaction Date & Time", getCurrentDateTimeOffset(userInfo.getStimezoneid()));
//
//			jsonuidataTrans.put("ninventorytranscode", Enumeration.TransactionStatus.OUTSIDE.gettransactionstatus());
//			jsonuidataTrans.put("nqtyissued", 0);
//
//			jsonuidataTrans.put("namountleft",
//					jdbcTemplate.queryForObject("select Cast(" + jsonuidataTrans.get("Received Quantity")
//							+ " as decimal (" + objmap.get("sprecision") + "))", String.class));
//
//			
//			if ((int) inputMap.get("needsectionwise") == Enumeration.TransactionStatus.YES.gettransactionstatus()) {
//				jsonuidataTrans.put("Description",
//						commonFunction.getMultilingualMessage("IDS_RECEIVED", userInfo.getSlanguagefilename()) + " "
//								+ commonFunction.getMultilingualMessage("IDS_BY", userInfo.getSlanguagefilename()) + " "
//								+ new JSONObject(jsonObjectInvTrans.get("Section").toString()).get("label"));
//			} else {
//				jsonuidataTrans.put("Description",
//						commonFunction.getMultilingualMessage("IDS_RECEIVED", userInfo.getSlanguagefilename()) + " "
//								+ commonFunction.getMultilingualMessage("IDS_BY", userInfo.getSlanguagefilename()) + " "
//								+ commonFunction.getMultilingualMessage(userInfo.getSsitename(),
//										userInfo.getSlanguagefilename()));
//			}
//			getJdbcTemplate().execute("INSERT INTO public.materialinventorytransaction("
//					+ "	nmaterialinventtranscode, nmaterialinventorycode,ninventorytranscode"
//					+ ",ntransactiontype,nsectioncode, nresultusedmaterialcode,nqtyreceived,nqtyissued, jsondata,jsonuidata, nsitecode, nstatus)"
//					+ "	VALUES (" + seqnomaterialinvtrans + ", " + seqnomaterialinv + ","
//					+ jsonObjectInvTrans.get("ninventorytranscode") + "," + jsonObjectInvTrans.get("ntransactiontype")
//					+ "," + jsonObjectInvTrans.get("nsectioncode") + ", -1,Cast("
//					+ jsonObjectInvTrans.get("Received Quantity") + " as decimal (" + objmap.get("sprecision")
//					+ ")),Cast(" + jsonObjectInvTrans.get("nqtyissued") + " as decimal (" + objmap.get("sprecision")
//					+ ")), '" + ReplaceQuote(jsonObjectInvTrans.toString()) + "'::jsonb,'"
//					+ ReplaceQuote(jsonuidataTrans.toString()) + "'::jsonb, " + userInfo.getNtranssitecode() + ", "
//					+ Enumeration.TransactionStatus.ACTIVE.gettransactionstatus() + ");");
//			// }
//			getJdbcTemplate().execute(insmat);
//			jsonUidataarray.put(jsonuidata);
//			jsonUidataarrayTrans.put(jsonuidataTrans);
//		}
//		updatestr = "update seqnomaterialmanagement set nsequenceno=" + seqnomaterialinv
//				+ "  where stablename='materialinventory' ;";
//		updatestr += "update seqnomaterialmanagement set nsequenceno=" + seqnomaterialinvtrans
//				+ "  where stablename='materialinventorytransaction' ;";


//		getJdbcTemplate().execute(updatestr);
		objmap.putAll((Map<String, Object>) getMaterialInventoryByID(inputMap).getBody());

		objmap.put("nregtypecode", -1);
		objmap.put("nregsubtypecode", -1);
		objmap.put("ndesigntemplatemappingcode", jsonuidata.get("nmaterialconfigcode"));
		actionType.put("materialinventory", "IDS_ADDMATERIALINVENTORY");
		actionType.put("materialinventorytransaction", "IDS_ADDMATERIALINVENTORYTRANSACTION");
		
		return new ResponseEntity<>(objmap, HttpStatus.OK);

	}

	@SuppressWarnings({ "unchecked" })
	public ResponseEntity<Object> getQuantityTransactionTemplate(Integer formcode,Integer materialtypecode) throws Exception {
		String siteLabelName = "";
		String sprecision = "";
		Map<String, Object> objmap = new LinkedHashMap<String, Object>();

		MaterialConfig objMaterialConfig = materialConfigRepository.findByNmaterialtypecodeAndNformcodeAndNstatus(materialtypecode, formcode,1);
		
		JSONArray Layout = new JSONArray(objMaterialConfig.getJsondata());
		
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
		JSONObject objMatNextvalidationperiod = null;
		JSONObject objMatOpenExpiryPeriod = null;
		JSONObject objMatExpiryPolicyperiod = null;
		
		List<Material> lstMaterial = new ArrayList<Material>();
		Material objMaterial = materialRepository.findByNstatusAndNmaterialcode(1, (Integer) objMaterialInventory.get("nmaterialcode"));
		
		lstMaterial.add(objMaterial);
		
		JSONObject objMat = new JSONObject(lstMaterial.get(0).getJsondata());
		if (objMat.has("Next Validation Period")) {
			objMatNextvalidationperiod = new JSONObject(objMat.get("Next Validation Period").toString());
		}
		if (objMat.has("Open Expiry Period")) {
			objMatOpenExpiryPeriod = new JSONObject(objMat.get("Open Expiry Period").toString());
		}
		if (objMat.has("Expiry Policy Period")) {
			objMatExpiryPolicyperiod = new JSONObject(objMat.get("Expiry Policy Period").toString());
		}
		if (lstMaterial != null && lstMaterial.size() > 0) {
			// SET THE NEXT VALIDATION DATE
			if (nflag == 1) {
				if (objMat.has("Next Validation")) {
					if (!objMat.get("Next Validation").equals("")) {
						if ((int) objMat.get("Next Validation") > 0) {
							DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							if (objMaterialInventory.has("Received Date & Time")) {
								if (!objMaterialInventory.get("Received Date & Time").toString().equals("")) {
									Date date1 = df.parse(objMaterialInventory.get("Received Date & Time").toString());
									if (date1 != null) {
										Calendar cal1 = Calendar.getInstance();
										cal1.setTime(date1);
										if (objMatNextvalidationperiod != null) {
											if ((int) objMatNextvalidationperiod
													.get("value") == Enumeration.TransactionStatus.DAYPERIOD
															.gettransactionstatus())
												cal1.add(Calendar.DATE, (int) objMat.get("Next Validation"));
											if ((int) objMatNextvalidationperiod
													.get("value") == Enumeration.TransactionStatus.MONTHPERIOD
															.gettransactionstatus())
												cal1.add(Calendar.MONTH, (int) objMat.get("Next Validation"));
											if ((int) objMatNextvalidationperiod
													.get("value") == Enumeration.TransactionStatus.YEARPERIOD
															.gettransactionstatus())
												cal1.add(Calendar.YEAR, (int) objMat.get("Next Validation"));
											date1 = cal1.getTime();
										}
										objMaterialInventory.put("dretestdate", df.format(date1));
									}
								}
							}
						}
					}
					/*
					 * else { objMaterialInventory.put("dretestdate", "-"); }
					 */
				}
				// SET THE POLICY EXPIRY DATE
				if (objMat.has("Expiry Policy Days")) {
					if (!objMat.get("Expiry Policy Days").equals("")) {
						if ((int) objMat.get("Expiry Policy Days") > 0) {
							DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							if (objMaterialInventory.has("Expiry Date & Time")) {
								if (!objMaterialInventory.get("Expiry Date & Time").toString().equals("")) {
									Date date2 = df.parse(objMaterialInventory.get("Expiry Date & Time").toString());
									if (date2 != null) {
										Calendar cal2 = Calendar.getInstance();
										cal2.setTime(date2);
										if (objMatExpiryPolicyperiod != null) {
											if ((int) objMatExpiryPolicyperiod
													.get("value") == Enumeration.TransactionStatus.DAYPERIOD
															.gettransactionstatus())
												cal2.add(Calendar.DATE, (int) objMat.get("Expiry Policy Days"));
											if ((int) objMatExpiryPolicyperiod
													.get("value") == Enumeration.TransactionStatus.MONTHPERIOD
															.gettransactionstatus())
												cal2.add(Calendar.MONTH, (int) objMat.get("Expiry Policy Days"));
											if ((int) objMatExpiryPolicyperiod
													.get("value") == Enumeration.TransactionStatus.YEARPERIOD
															.gettransactionstatus())
												cal2.add(Calendar.YEAR, (int) objMat.get("Expiry Policy Days"));
											date2 = cal2.getTime();
										}
										objMaterialInventory.put("dexpirypolicydate", df.format(date2));
									}
								}
							}
						}
					}
					/*
					 * else { objMaterialInventory.put("dexpirypolicydate", "-"); }
					 */

				}
			}
			if (nflag == 2) {
				// SET THE POLICY EXPIRY DATE
				if (objMat.has("Open Expiry")) {
					if (!objMat.get("Open Expiry").equals("")) {
						if ((int) objMat.get("Open Expiry") > 0) {
							DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							if (objMaterialInventory.has("Open Date")) {
								if (!objMaterialInventory.get("Open Date").toString().equals("")) {
									Date date2 = df.parse(objMaterialInventory.get("Open Date").toString());
									if (date2 != null) {
										Calendar cal2 = Calendar.getInstance();
										cal2.setTime(date2);
										if (objMatOpenExpiryPeriod != null) {
											if ((int) objMatOpenExpiryPeriod
													.get("value") == Enumeration.TransactionStatus.DAYPERIOD
															.gettransactionstatus())
												cal2.add(Calendar.DATE, (int) objMat.get("Open Expiry"));
											if ((int) objMatOpenExpiryPeriod
													.get("value") == Enumeration.TransactionStatus.MONTHPERIOD
															.gettransactionstatus())
												cal2.add(Calendar.MONTH, (int) objMat.get("Open Expiry"));
											if ((int) objMatOpenExpiryPeriod
													.get("value") == Enumeration.TransactionStatus.YEARPERIOD
															.gettransactionstatus())
												cal2.add(Calendar.YEAR, (int) objMat.get("Open Expiry"));
											date2 = cal2.getTime();
										}
										objMaterialInventory.put("dopenexpirydate", df.format(date2));
									}
								}
							}
						}
					}
					/*
					 * else { objMaterialInventory.put("dopenexpirydate", "-"); }
					 */

				}
			}
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
}
