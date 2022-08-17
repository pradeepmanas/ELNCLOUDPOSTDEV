package com.agaram.eln.primary.service.material;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.agaram.eln.primary.global.Enumeration;
import com.agaram.eln.primary.model.material.Material;
import com.agaram.eln.primary.model.material.MaterialCategory;
import com.agaram.eln.primary.model.material.MaterialConfig;
import com.agaram.eln.primary.model.material.MaterialType;
import com.agaram.eln.primary.repository.material.MaterialCategoryRepository;
import com.agaram.eln.primary.repository.material.MaterialConfigRepository;
import com.agaram.eln.primary.repository.material.MaterialRepository;
import com.agaram.eln.primary.repository.material.MaterialTypeRepository;
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
	

	public ResponseEntity<Object> getMaterialInventory() throws Exception {
		Map<String, Object> objmap = new LinkedHashMap<String, Object>();
		String strquery2 = "";
		
		List<MaterialType> lstMaterialType = materialTypeRepository.findByNstatus(1);
		objmap.put("MaterialType", lstMaterialType);
		
		List<MaterialCategory> lstMaterialCategory = materialCategoryRepository.findByNmaterialtypecode(lstMaterialType.get(0).getNmaterialtypecode());
		
		List<MaterialType> lstMaterialType1 = new ArrayList<MaterialType>();
		
		lstMaterialType1.add(lstMaterialType.get(0));
		
		objmap.put("SelectedMaterialType", lstMaterialType1);
		
		if (!lstMaterialCategory.isEmpty()) {
			objmap.put("MaterialCategoryMain", lstMaterialCategory);
			objmap.put("nmaterialcatcode", lstMaterialCategory.get(0).getNmaterialcatcode());
			
//			strquery2 = "select nmaterialcode,json_build_object ('nmaterialcode',nmaterialcode,'Material Name',jsondata->'Material Name') as jsondata  from material where "
//					+ "   (jsondata->'nmaterialcatcode')::int=" + lstMaterialCategory.get(0).getNmaterialcatcode()
//					+ " and nstatus=1 order by nmaterialcode";
		

			List<Material> lstMaterial = materialRepository.findByNmaterialcatcode(lstMaterialCategory.get(0).getNmaterialcatcode());
			
			objmap.put("MaterialCombo", lstMaterial);
			
			List<MaterialConfig> lstMaterialConfig = materialConfigRepository.findByNmaterialtypecodeAndNformcode(lstMaterialType.get(0).getNmaterialtypecode(), 138);
			objmap.put("selectedTemplate", lstMaterialConfig);
			
			objmap.put("nmaterialtypecode", lstMaterialType.get(0).getNmaterialtypecode());
			objmap.put("nmaterialcatcode", lstMaterialCategory.get(0).getNmaterialcatcode());
			if (!lstMaterial.isEmpty()) {
				objmap.put("nmaterialcode", lstMaterial.get(0).getNmaterialcode());
			}
			
//			objmap.putAll((Map<String, Object>) getMaterialInventoryByID(objmap).getBody());
		}
		return new ResponseEntity<>(objmap, HttpStatus.OK);
	}

//	public ResponseEntity<Object> getMaterialInventoryByID(Map<String, Object> inputMap)
//			throws Exception {
//		ObjectMapper objmapper = new ObjectMapper();
//		Map<String, Object> objmap = new LinkedHashMap<String, Object>();
//		List<Map<String, Object>> lstMaterialInventory = new ArrayList<Map<String, Object>>();
//		String comboget = "";
//		String strquery2 = "";
//		if (inputMap.containsKey("nmaterialcode")) {
//			
//			String query1 = "select json_agg(a.jsonuidata) from  "
//					+ "(select mi.nmaterialinventorycode,mi.jsonuidata||json_build_object('nmaterialinventorycode',mi.nmaterialinventorycode,'status'"
//					+ ",(ts.jsondata->'stransdisplaystatus'->>'en-US')" + " )::jsonb "
//					+ " " + "as jsonuidata  from materialinventory mi,materialcategory mc,material m,"
//					+ " transactionstatus ts  where " + " (mi.jsondata->'nmaterialcode')::int="
//					+ inputMap.get("nmaterialcode") + "" + "  and (mi.jsondata->'nmaterialtypecode')::int="
//					+ inputMap.get("nmaterialtypecode") + " and  (mi.jsondata->'nmaterialcatcode')::int="
//					+ inputMap.get("nmaterialcatcode") + " AND ts.ntranscode = (select   ntransactionstatus "
//					+ " from  materialinventoryhistory where "
//					+ " nmaterialinventoryhistorycode=(select   max(nmaterialinventoryhistorycode) "
//					+ " from  materialinventoryhistory where "
//					+ " nmaterialinventorycode=mi.nmaterialinventorycode) ) " + " and mi.nstatus="
//					+ Enumeration.TransactionStatus.ACTIVE.gettransactionstatus() + " AND mc.nstatus = "
//					+ Enumeration.TransactionStatus.ACTIVE.gettransactionstatus() + " and m.nstatus = "
//					+ Enumeration.TransactionStatus.ACTIVE.gettransactionstatus()
//					+ " and (m.jsondata->'nmaterialcatcode')::INT=mc.nmaterialcatcode"
//					+ " and m.nmaterialcode=mi.nmaterialcode" + " order by mi.nmaterialinventorycode)a";
//			
//			String strMaterialInventory = jdbcTemplate.queryForObject(query1, String.class);
//			
//			if (strMaterialInventory != null)
//				lstMaterialInventory = objmapper.convertValue(getSiteLocalTimeFromUTCForRegTmplate(strMaterialInventory,
//						userInfo, true,
//						(int) inputMap.get("nmaterialtypecode") == Enumeration.TransactionStatus.STANDARDTYPE
//								.gettransactionstatus()
//										? 6
//										: (int) inputMap.get(
//												"nmaterialtypecode") == Enumeration.TransactionStatus.VOLUMETRICTYPE
//														.gettransactionstatus() ? 7 : 8,
//						(int) inputMap.get("nmaterialtypecode") == Enumeration.TransactionStatus.STANDARDTYPE
//								.gettransactionstatus()
//										? "MaterialInvStandard"
//										: (int) inputMap.get(
//												"nmaterialtypecode") == Enumeration.TransactionStatus.VOLUMETRICTYPE
//														.gettransactionstatus() ? "MaterialInvVolumetric"
//																: "MaterialInvMaterialInventory"),
//						new TypeReference<List<Map<String, Object>>>() {
//						});
//			objmap.put("MaterialInventory", lstMaterialInventory);
//			if (!lstMaterialInventory.isEmpty()) {
//				if (!(inputMap.containsKey("nflag"))) {
//					
//					objmap.put("SelectedMaterialInventory", lstMaterialInventory.get(lstMaterialInventory.size() - 1));
//					inputMap.put("nsectioncode",
//							lstMaterialInventory.get(lstMaterialInventory.size() - 1).get("nsectioncode"));
//
//					objmap.putAll(
//							(Map<String, Object>) getQuantityTransactionByMaterialInvCode((int) lstMaterialInventory
//									.get(lstMaterialInventory.size() - 1).get("nmaterialinventorycode"), inputMap,
//			).getBody());
//					objmap.putAll((Map<String, Object>) getMaterialFile((int) lstMaterialInventory
//							.get(lstMaterialInventory.size() - 1).get("nmaterialinventorycode"), userInfo));
//					objmap.putAll((Map<String, Object>) getResultUsedMaterial((int) lstMaterialInventory
//							.get(lstMaterialInventory.size() - 1).get("nmaterialinventorycode"), userInfo).getBody());
//					objmap.putAll((Map<String, Object>) getMaterialInventoryhistory((int) lstMaterialInventory
//							.get(lstMaterialInventory.size() - 1).get("nmaterialinventorycode"), userInfo));
//
//				} else {
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
//					// List<MaterialInventory> lstMaterialInventory1 = jdbcTemplate.query(query3,
//					// new MaterialInventory());
//					strMaterialInventory = jdbcTemplate.queryForObject(query3, String.class);
//					List<Map<String, Object>> lstMaterialInventory1 = new ArrayList<Map<String, Object>>();
//					// lstMaterialInventory1 = (List<MaterialInventory>)
//					// dynamicConvertedDate(lstMaterialInventory1.get(0),
//					// inputMap, userInfo);
//					// Commented For JSONUIDATA
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
//
//				}
//			} else {
//				objmap.put("SelectedMaterialInventory", lstMaterialInventory);
//			}
//		}
//		String query2 = "select nmaterialtypecode,jsondata->'smaterialtypename'->>'" + userInfo.getSlanguagetypecode()
//				+ "' " + "as smaterialtypename,jsondata  from materialtype where nmaterialtypecode="
//				+ inputMap.get("nmaterialtypecode") + " and nstatus=1";
//
//		List<MaterialType> lstMaterialType = jdbcTemplate.query(query2, new MaterialType());
//		objmap.put("SelectedMaterialType", lstMaterialType);
//
//		if (inputMap.containsKey("nmaterialcatcode")) {
//			comboget = "select nmaterialcatcode,smaterialcatname,needsectionwise from materialcategory where nmaterialcatcode="
//					+ inputMap.get("nmaterialcatcode") + " and nstatus=1";
//		} else {
//			comboget = "select nmaterialcatcode,smaterialcatname,needsectionwise from materialcategory where nmaterialtypecode="
//					+ inputMap.get("nmaterialtypecode") + " and nstatus=1";
//		}
//		List<MaterialCategory> lstMaterialCategory = jdbcTemplate.query(comboget, new MaterialCategory());
//		if (!lstMaterialCategory.isEmpty()) {
//			if (inputMap.containsKey("nmaterialcatcode")) {
//				objmap.put("SelectedMaterialCategory", lstMaterialCategory.get(0));
//			} else {
//				objmap.put("SelectedMaterialCategory", lstMaterialCategory.get(0));
//			}
//		}
//
//		if (inputMap.containsKey("nmaterialcode")) {
//			/*
//			 * if ((int) inputMap.get("nmaterialtypecode") == 1) { strquery2 =
//			 * "select json_build_object ('nmaterialcode',nmaterialcode,'smaterialname',jsondata->'IDS_STANDARDMATERIALNAME','nunitcode',"
//			 * +
//			 * " jsondata->'IDS_BASICUOM','ntranscode',jsondata->'IDS_QUARANTINE','isExpiryNeed',jsondata->>'IDS_EXPIRY',"
//			 * +
//			 * " 'IDS_OPENEXIPIRYNEED',jsondata->>'IDS_OPENEXIPIRYNEED','needsectionwise',"
//			 * + lstMaterialCategory.get(0).getNeedSectionwise() + ") " +
//			 * " as jsondata  from material where " + "   nmaterialcode=" +
//			 * inputMap.get("nmaterialcode") + " and nstatus=1"; } else if ((int)
//			 * inputMap.get("nmaterialtypecode") == 2) { strquery2 =
//			 * "select json_build_object ('nmaterialcode',nmaterialcode,'smaterialname',jsondata->'IDS_VOLUMETRICMATERIALNAME' ,'nunitcode',"
//			 * +
//			 * " jsondata->'IDS_BASICUOM' ,'ntranscode',jsondata->'IDS_QUARANTINE','isExpiryNeed',jsondata->>'IDS_EXPIRY',"
//			 * +
//			 * "	 'IDS_OPENEXIPIRYNEED',jsondata->>'IDS_OPENEXIPIRYNEED','needsectionwise',"
//			 * + lstMaterialCategory.get(0).getNeedSectionwise() +
//			 * ") as jsondata  from material where " + "   nmaterialcode=" +
//			 * inputMap.get("nmaterialcode") + " and nstatus=1"; } else if ((int)
//			 * inputMap.get("nmaterialtypecode") == 3) {
//			 */
//			strquery2 = "select json_build_object ('nmaterialcode',m.nmaterialcode,'Material Name',m.jsondata->'Material Name','nunitcode',"
//					+ " m.jsondata->'Basic Unit' ,'ntranscode',m.jsondata->'Quarantine','isExpiryNeed',m.jsondata->>'Expiry Validations'"
//					+ "	, 'Open Expiry Need',m.jsondata->>'Open Expiry Need','needsectionwise',"
//					+ lstMaterialCategory.get(0).getNeedSectionwise()
//					+ " ,'sdisplaystatus',ts.jsondata->'stransdisplaystatus'->>'" + userInfo.getSlanguagetypecode()
//					+ "') ::jsonb  as jsondata  from material m,transactionstatus ts where " + "   m.nmaterialcode="
//					+ inputMap.get("nmaterialcode")
//					+ " and ts.ntranscode= ( case when (m.jsondata->'Quarantine')::int=3 then 37 else 28 end)::int and  m.nstatus=1";
//			// }
//			/*
//			 * else { comboget =
//			 * "select nmaterialcatcode,smaterialcatname from materialcategory where nmaterialtypecode="
//			 * + inputMap.get("nmaterialtypecode") + " and nstatus=1"; }
//			 */
//			List<Material> lstMaterialCategory1 = jdbcTemplate.query(strquery2, new Material());
//			if (!lstMaterialCategory1.isEmpty()) {
//				if (inputMap.containsKey("nmaterialcode")) {
//					objmap.put("SelectedMaterialCrumb", lstMaterialCategory1.get(0));
//				} else {
//					objmap.put("SelectedMaterialCrumb", lstMaterialCategory1.get(0));
//				}
//			}
//		}
//		objmap.putAll((Map<String, Object>) getQuantityTransactionTemplate(userInfo).getBody());
//		objmap.putAll((Map<String, Object>) getMaterialInventoryAdd((int) inputMap.get("nmaterialtypecode"), userInfo)
//				.getBody());
//		objmap.put("DesignMappedFeilds", getTemplateDesignForMaterial(
//				((List<MaterialConfig>) objmap.get("selectedTemplate")).get(0).getNmaterialconfigcode(),
//				userInfo.getNformcode()));
//		List<TransactionStatus> lstTransactionStatus = jdbcTemplate.query(
//				"select ntranscode,jsondata from   transactionstatus where ntranscode in (47,48) order by 1",
//				new TransactionStatus());
//		objmap.put("TransactionType", lstTransactionStatus);
//		List<MaterialInventoryType> lstMaterialInventoryType = jdbcTemplate.query(
//				"select ninventorytypecode,jsondata from   materialinventorytype where ninventorytypecode in (1,2) order by 1",
//				new MaterialInventoryType());
//		objmap.put("MaterialInventoryType", lstMaterialInventoryType);
//		objmap.put("DesignMappedFeildsQuantityTransaction", getTemplateDesignForMaterial(9, userInfo.getNformcode()));
//
//		return new ResponseEntity<>(objmap, HttpStatus.OK);
//
//	}

}
