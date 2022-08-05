package com.agaram.eln.primary.service.Material;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.agaram.eln.primary.model.Material.MaterialCategory;
import com.agaram.eln.primary.model.Material.MaterialType;
import com.agaram.eln.primary.repository.Material.MaterialCategoryRepository;
import com.agaram.eln.primary.repository.Material.MaterialTypeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class MaterialService {
	
	@Autowired
	MaterialTypeRepository MaterialTypeRepository;
	@Autowired
	MaterialCategoryRepository MaterialCategoryRepository; 
	

	public ResponseEntity<Object> getMaterialcombo(Integer nmaterialtypecode) {
		
		Map<String, Object> objmap = new LinkedHashMap<String, Object>();
		
		List<MaterialCategory> lstMaterialCategory =MaterialCategoryRepository.findByNmaterialtypecode(nmaterialtypecode);
		objmap.put("MaterialCategoryMain", lstMaterialCategory);
		return new ResponseEntity<>(objmap, HttpStatus.OK);
	}


	public ResponseEntity<Object> getMaterialType() {
		Map<String, Object> objmap = new LinkedHashMap<String, Object>();
		
		List<MaterialType> lstMaterialType = MaterialTypeRepository.findAll();
		
		objmap.put("MaterialType", lstMaterialType);
		objmap.put("SelectedMaterialType", lstMaterialType);
		
		List<MaterialCategory> lstMaterialCategory = MaterialCategoryRepository.findByNmaterialtypecode(lstMaterialType.get(0).getNmaterialtypecode());
		
		if (!lstMaterialCategory.isEmpty()) {
			objmap.put("MaterialCategoryMain", lstMaterialCategory);
			objmap.put("nmaterialcatcode", lstMaterialCategory.get(0).getNmaterialcatcode());
		}
		objmap.put("nmaterialtypecode", lstMaterialType.get(0).getNmaterialtypecode());
//		objmap.putAll((Map<String, Object>) getMaterialByTypeCode(objmap, objUserInfo).getBody());
//		objmap.putAll((Map<String, Object>) getMaterialAdd((short) objmap.get("nmaterialtypecode"), ).getBody());

		return new ResponseEntity<>(objmap, HttpStatus.OK);
	}


//	public ResponseEntity<Object> getMaterialByTypeCode(Map<String, Object> inputMap) {
//		ObjectMapper objmapper = new ObjectMapper();
//		Map<String, Object> objmap = new LinkedHashMap<String, Object>();
//		List<Integer> ismaterialSectionneed = new ArrayList<Integer>();
//		List<Map<String, Object>> lstMaterial = new ArrayList<Map<String, Object>>();
//		String comboget = "";
//
//		if (inputMap.containsKey("nmaterialcatcode")) {
//
//			final String query1 = "select json_agg(a.jsonuidata)  from (select m.nmaterialcode,"
//					+ " m.jsonuidata||json_build_object('nmaterialcode',m.nmaterialcode)::jsonb "
//					+ " as jsonuidata from material m,materialcategory mc where  m.nstatus=1 "
//					+ "and (m.jsondata->'nmaterialtypecode')::int="
//					+ inputMap.get("nmaterialtypecode") + " and (m.jsondata->'nmaterialcatcode')::int="
//					+ inputMap.get("nmaterialcatcode")
//					+ " and mc.nmaterialcatcode= (jsondata -> 'nmaterialcatcode' ) :: INT"
//					+ " order by m.nmaterialcode )a";
//		
//			String strMaterial = jdbcTemplate.queryForObject(query1, String.class);
//
//			if (strMaterial != null)
//				
//				if (inputMap.get("nmaterialtypecode").getClass().getSimpleName().equals("Short")) {
//					lstMaterial = objmapper.convertValue(getSiteLocalTimeFromUTCForRegTmplate(strMaterial, userInfo,
//							true,
//							(short) inputMap.get("nmaterialtypecode") == Enumeration.TransactionStatus.STANDARDTYPE
//									.gettransactionstatus()
//											? 1
//											: (short) inputMap.get(
//													"nmaterialtypecode") == Enumeration.TransactionStatus.VOLUMETRICTYPE
//															.gettransactionstatus() ? 2 : 3,
//							(short) inputMap.get("nmaterialtypecode") == Enumeration.TransactionStatus.STANDARDTYPE
//									.gettransactionstatus()
//											? "MaterialStandard"
//											: (short) inputMap.get(
//													"nmaterialtypecode") == Enumeration.TransactionStatus.VOLUMETRICTYPE
//															.gettransactionstatus() ? "MaterialVolumetric"
//																	: "MaterialMaterialInventory"),
//							new TypeReference<List<Map<String, Object>>>() {
//							});
//				} else {
//					Integer nmaterialtypecode = new Integer((int) inputMap.get("nmaterialtypecode"));
//					lstMaterial = objmapper.convertValue(
//							getSiteLocalTimeFromUTCForRegTmplate(strMaterial, userInfo, true,
//									nmaterialtypecode == Enumeration.TransactionStatus.STANDARDTYPE
//											.gettransactionstatus()
//													? 1
//													: nmaterialtypecode == Enumeration.TransactionStatus.VOLUMETRICTYPE
//															.gettransactionstatus() ? 2 : 3,
//									nmaterialtypecode == Enumeration.TransactionStatus.STANDARDTYPE
//											.gettransactionstatus()
//													? "MaterialStandard"
//													: nmaterialtypecode == Enumeration.TransactionStatus.VOLUMETRICTYPE
//															.gettransactionstatus() ? "MaterialVolumetric"
//																	: "MaterialMaterialInventory"),
//							new TypeReference<List<Map<String, Object>>>() {
//							});
//				}
//
//			/*
//			 * List<Material> lstMaterial = objmapper.convertValue( strMaterial, new
//			 * TypeReference<List<Material>>() { });
//			 */
//			objmap.put("Material", lstMaterial);
//			/*
//			 * ismaterialSectionneed =
//			 * getJdbcTemplate().queryForList("select jsondata->>'ismaterialSectionneed' " +
//			 * "from materialtype where nmaterialtypecode=" +
//			 * inputMap.get("nmaterialtypecode"), Integer.class);
//			 */
//			ismaterialSectionneed = getJdbcTemplate().queryForList("select needsectionwise  "
//					+ "from materialcategory where nmaterialcatcode=" + inputMap.get("nmaterialcatcode"),
//					Integer.class);
//			objmap.put("ismaterialSectionneed", ismaterialSectionneed.get(0));
//			if (ismaterialSectionneed.get(0) == 3) {
//				objmap.put("tabScreen", "IDS_MATERIALSECTION");
//			} else {
//				objmap.put("tabScreen", "IDS_MATERIALMSDSATTACHMENT");
//			}
//
//			if (!lstMaterial.isEmpty()) {
//				objmap.put("SelectedMaterial", lstMaterial.get(lstMaterial.size() - 1));
//
//				objmap.putAll((Map<String, Object>) objMaterialSectionDAO
//						.getMaterialSectionByMaterialCode(
//								(int) lstMaterial.get(lstMaterial.size() - 1).get("nmaterialcode"), inputMap)
//						.getBody());
//				objmap.put("nmaterialcode", (int) lstMaterial.get(lstMaterial.size() - 1).get("nmaterialcode"));
//				objmap.putAll((Map<String, Object>) getMaterialFile(
//						(int) lstMaterial.get(lstMaterial.size() - 1).get("nmaterialcode"), userInfo));
//				objmap.putAll((Map<String, Object>) getMaterialSafetyInstructions(objmap, userInfo).getBody());
//			} else {
//				objmap.put("SelectedMaterial", lstMaterial);
//			}
//
//			String query2 = "select nmaterialtypecode,jsondata->'smaterialtypename'->>'"
//					+ userInfo.getSlanguagetypecode() + "' "
//					+ "as smaterialtypename,jsondata  from materialtype where nmaterialtypecode="
//					+ inputMap.get("nmaterialtypecode") + " and nstatus=1";
//
//			List<MaterialType> lstMaterialType = (List<MaterialType>) jdbcTemplate.query(query2, new MaterialType());
//			objmap.put("SelectedMaterialType", lstMaterialType);
//
//			if (inputMap.containsKey("nmaterialcatcode")) {
//				comboget = "select nmaterialcatcode,smaterialcatname,needSectionwise from materialcategory where nmaterialcatcode="
//						+ inputMap.get("nmaterialcatcode") + " and nstatus=1";
//			} else {
//				comboget = "select nmaterialcatcode,smaterialcatname,needSectionwise from materialcategory where nmaterialtypecode="
//						+ inputMap.get("nmaterialtypecode") + " and nstatus=1";
//			}
//			List<MaterialCategory> lstMaterialCategory = (List<MaterialCategory>) jdbcTemplate.query(comboget,
//					new MaterialCategory());
//			if (!lstMaterialCategory.isEmpty()) {
//				if (inputMap.containsKey("nmaterialcatcode")) {
//					objmap.put("SelectedMaterialCategory", lstMaterialCategory.get(0));
//				} else {
//					objmap.put("SelectedMaterialCategory", lstMaterialCategory.get(0));
//				}
//			}
//			if (inputMap.get("nmaterialtypecode").getClass().getSimpleName().equals("Short")) {
//				objmap.putAll((Map<String, Object>) getMaterialAdd((short) inputMap.get("nmaterialtypecode"), userInfo)
//						.getBody());
//				objmap.put("DesignMappedFeilds", getTemplateDesignForMaterial(
//						/*(short) inputMap.get("nmaterialtypecode") == Enumeration.TransactionStatus.STANDARDTYPE
//								.gettransactionstatus()
//										? 1
//										: (short) inputMap.get(
//												"nmaterialtypecode") == Enumeration.TransactionStatus.VOLUMETRICTYPE
//														.gettransactionstatus() ? 2 : 3*/ 
//						((List<MaterialConfig>)objmap.get("selectedTemplate")).get(0).getNmaterialconfigcode(),
//						userInfo.getNformcode()));
//			} else {
//				Integer nmaterialtypecode = new Integer((int) inputMap.get("nmaterialtypecode"));
//				objmap.putAll((Map<String, Object>) getMaterialAdd(nmaterialtypecode.shortValue(), userInfo).getBody());
//				objmap.put("DesignMappedFeilds", getTemplateDesignForMaterial(
//						/*
//						 * nmaterialtypecode ==
//						 * Enumeration.TransactionStatus.STANDARDTYPE.gettransactionstatus() ? 1 :
//						 * nmaterialtypecode == Enumeration.TransactionStatus.VOLUMETRICTYPE
//						 * .gettransactionstatus() ? 2 : 3,
//						 */
//						((List<MaterialConfig>)objmap.get("selectedTemplate")).get(0).getNmaterialconfigcode(),
//						userInfo.getNformcode()));
//			}
//
//		} else {
//			String query2 = "select nmaterialtypecode,jsondata->'smaterialtypename'->>'"
//					+ userInfo.getSlanguagetypecode() + "' "
//					+ "as smaterialtypename,jsondata  from materialtype where nmaterialtypecode="
//					+ inputMap.get("nmaterialtypecode") + " and nstatus=1";
//
//			List<MaterialType> lstMaterialType = (List<MaterialType>) jdbcTemplate.query(query2, new MaterialType());
//			List<MaterialType> lstActiontype = new ArrayList<MaterialType>();
//			objmap.put("SelectedMaterialType", lstMaterialType);
//			objmap.put("Material", lstActiontype);
//			objmap.put("SelectedMaterial", lstActiontype);
//			objmap.put("SelectedMaterialCategory", lstActiontype);
//		}
//		return new ResponseEntity<>(objmap, HttpStatus.OK);
//
//	}
	
}
