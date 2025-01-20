package com.agaram.eln.primary.service.material;

import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.persistence.Query;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.agaram.eln.primary.commonfunction.commonfunction;
import com.agaram.eln.primary.global.Enumeration;
import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.material.Elnmaterial;
import com.agaram.eln.primary.model.material.ElnmaterialInventory;
import com.agaram.eln.primary.model.material.MaterialCategory;
import com.agaram.eln.primary.model.material.MaterialType;
import com.agaram.eln.primary.model.material.Unit;
import com.agaram.eln.primary.model.samplestoragelocation.SampleStorageLocation;
import com.agaram.eln.primary.model.samplestoragelocation.SampleStorageVersion;
import com.agaram.eln.primary.model.samplestoragelocation.SelectedInventoryMapped;
import com.agaram.eln.primary.model.usermanagement.LSMultiusergroup;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.repository.material.ElnmaterialInventoryRepository;
import com.agaram.eln.primary.repository.material.ElnmaterialRepository;
import com.agaram.eln.primary.repository.material.MaterialCategoryRepository;
import com.agaram.eln.primary.repository.material.MaterialTypeRepository;
import com.agaram.eln.primary.repository.material.UnitRepository;
import com.agaram.eln.primary.repository.samplestoragelocation.SampleStorageLocationRepository;
import com.agaram.eln.primary.repository.samplestoragelocation.SelectedInventoryMappedRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class MaterialCategoryService {

	@Autowired
	MaterialTypeRepository MaterialTypeRepository;

	@Autowired
	MaterialCategoryRepository MaterialCategoryRepository;

	@Autowired
	UnitRepository UnitRepository;

	@Autowired
	ElnmaterialRepository ElnmaterialRepository;

	@Autowired
	MaterialInventoryService MaterialInventoryService;

	@Autowired
	ElnmaterialInventoryRepository elnmaterialInventoryReppository;

	@Autowired
	SelectedInventoryMappedRepository selectedInventoryMappedRepository;

	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	SampleStorageLocationRepository sampleStorageLocationRepository;

	public ResponseEntity<Object> getMaterialType(Integer nsitecode) {

		List<MaterialType> lstgetMaterialType = MaterialTypeRepository
				.findByNmaterialtypecodeNotAndNstatusAndNsitecodeOrNmaterialtypecodeNotAndNstatusAndNdefaultstatus(-1,
						1, nsitecode, -1, 1, 4);
		return new ResponseEntity<>(lstgetMaterialType, HttpStatus.OK);
	}

	public ResponseEntity<Object> getMaterialCategory(Integer nsitecode) {

		List<MaterialCategory> lstgetMaterialCategory = MaterialCategoryRepository
				.findByNsitecodeOrNdefaultstatusOrderByNmaterialcatcodeDesc(nsitecode, 3);
		return new ResponseEntity<>(lstgetMaterialCategory, HttpStatus.OK);
	}

	public ResponseEntity<Object> createMaterialCategory(Map<String, Object> inputMap) throws Exception {
		ObjectMapper objmapper = new ObjectMapper();
		final MaterialCategory materialCategory = objmapper.convertValue(inputMap.get("materialcategory"),
				MaterialCategory.class);
		materialCategory.setResponse(new Response());

		List<MaterialCategory> lstgetMaterialCategory = MaterialCategoryRepository
				.findBySmaterialcatnameIgnoreCaseAndNsitecode(materialCategory.getSmaterialcatname(),
						materialCategory.getNsitecode());

		LSuserMaster objMaster = new LSuserMaster();
		objMaster.setUsercode(materialCategory.getObjsilentaudit().getLsuserMaster());

		if (lstgetMaterialCategory.isEmpty()) {

			materialCategory.setSmaterialtypename(materialCategory.getSmaterialtypename());
			materialCategory.setNmaterialtypecode(materialCategory.getNmaterialtypecode());
			materialCategory.setSmaterialcatname(materialCategory.getSmaterialcatname());
			materialCategory.setSdescription(materialCategory.getSdescription());
			materialCategory.setNsitecode(materialCategory.getNsitecode());
			materialCategory.setNactivestatus(0);
			materialCategory.setNuserrolecode(0);
			materialCategory.setCreateby(objMaster);
			materialCategory.setCreatedate(commonfunction.getCurrentUtcTime());
			MaterialCategoryRepository.save(materialCategory);

			materialCategory.getResponse().setStatus(true);
			materialCategory.getResponse().setInformation("IDS_SUCCESS");
			return new ResponseEntity<>(materialCategory, HttpStatus.OK);

		} else {
			materialCategory.getResponse().setStatus(false);
			materialCategory.getResponse().setInformation("IDS_ALREADYEXIST");
			return new ResponseEntity<>(materialCategory, HttpStatus.OK);
		}
	}

	public ResponseEntity<Object> deleteMaterialCategory(MaterialCategory materialCategory, LScfttransaction obj) {
		final MaterialCategory objMaterialCategory = MaterialCategoryRepository
				.findByNmaterialcatcode(materialCategory.getNmaterialcatcode());
		if (objMaterialCategory == null) {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		} else {
			objMaterialCategory.setNstatus(Enumeration.TransactionStatus.DELETED.gettransactionstatus());
			MaterialCategoryRepository.save(objMaterialCategory);
			materialCategory.setObjsilentaudit(obj);
			return getMaterialCategory(materialCategory.getNsitecode());
		}
	}

	public ResponseEntity<Object> updateMaterialCategory(MaterialCategory materialCategory) {
		final MaterialCategory objMaterialCategory = MaterialCategoryRepository
				.findByNmaterialcatcode(materialCategory.getNmaterialcatcode());
		materialCategory.setResponse(new Response());
		if (objMaterialCategory == null) {
			materialCategory.getResponse().setStatus(false);
			materialCategory.getResponse().setInformation("IDS_ALREADYDELETED");
			return new ResponseEntity<>(materialCategory, HttpStatus.OK);
		} else {
			final MaterialCategory materialCategoryObj = MaterialCategoryRepository
					.findByNsitecodeAndSmaterialcatnameIgnoreCase(materialCategory.getNsitecode(),
							materialCategory.getSmaterialcatname());

			if (materialCategoryObj == null
					|| (materialCategoryObj.getNmaterialcatcode().equals(materialCategory.getNmaterialcatcode()))) {
				MaterialCategoryRepository.save(materialCategory);
				materialCategory.getResponse().setStatus(true);
				materialCategory.getResponse().setInformation("IDS_SUCCESS");
				return new ResponseEntity<>(materialCategory, HttpStatus.OK);
			} else {
				materialCategory.getResponse().setStatus(false);
				materialCategory.getResponse().setInformation("IDS_ALREADYEXIST");
				return new ResponseEntity<>(materialCategory, HttpStatus.OK);
			}
		}
	}

	@SuppressWarnings({ "unchecked", "unlikely-arg-type" })
	public ResponseEntity<Map<String, Object>> ImportDatatoStore(Map<String, Object> inputMap) throws ParseException {
		ObjectMapper obj = new ObjectMapper();
		Integer siteCode = Integer.parseInt((String) inputMap.get("sitecode"));
//		List<String> materialCatNames = (List<String>) inputMap.get("MaterialCatName");
//		List<String> materialTypeNames = (List<String>) inputMap.get("MaterialName");
		List<String> UnitName = (List<String>) inputMap.get("UnitName");

		Date currentDate = commonfunction.getCurrentUtcTime();
		Map<String, Object> responseMap = new HashMap<>();
		// MaterialType ---------------------------------

//		 List<MaterialType> materialTypeValues = MaterialTypeRepository
//		            .findByNsitecodeAndSmaterialtypenameIgnoreCaseIn(siteCode, materialTypeNames);
//		    Map<String, MaterialType> materialTypeMap = materialTypeValues.stream()
//		            .collect(Collectors.toMap(MaterialType::getSmaterialtypename, materialType -> materialType));
//		List<MaterialType> materialTypes = obj.convertValue(inputMap.get("Materialcategory_MaterialType"),
//				new TypeReference<List<MaterialType>>() {
//				});
//		materialTypes = materialTypes.stream().map(item -> {
//			 MaterialType matchedMaterialType = materialTypeMap.get(item.getSmaterialtypename());
//             if (matchedMaterialType != null) {
//                 return matchedMaterialType;
//             } else {
//                 item.setCreatedate(currentDate);
//                 return item;
//             }
//		}).filter(Objects::nonNull).collect(Collectors.toList());
//
//		MaterialTypeRepository.save(materialTypes);

		// Materialcategory -----------------------------------

//		List<MaterialCategory> materialCategories = obj.convertValue(inputMap.get("Materialcategory"),
//				new TypeReference<List<MaterialCategory>>() {
//				});
//		Map<String, Long> materialCategoriesExistvalues = checkDeblicaterecord(materialCatNames, siteCode,
//				"MaterialCategory", "Smaterialcatname");
//
//		List<MaterialCategory> updatedMaterialCategories = materialCategories.stream().map(item -> {
//			if (materialCategoriesExistvalues.get(item.getSmaterialcatname()) > 0) {
//				Long existcount = materialCategoriesExistvalues.get(item.getSmaterialcatname()) + 1;
//				item.setSmaterialcatname(item.getSmaterialcatname() + "(" + existcount + ")");
//			}
//			item.setCreatedate(currentDate);
//			return item;
//
//		}).filter(Objects::nonNull).collect(Collectors.toList());
//
//		MaterialCategoryRepository.save(updatedMaterialCategories);
//		responseMap.put("materialcategory", updatedMaterialCategories);
//
//		Map<String, MaterialCategory> materialCatMapaftersave = updatedMaterialCategories.stream()
//				.collect(Collectors.toMap(MaterialCategory::getSmaterialcatname, materialcategory -> materialcategory));

//	    //unit -------------------------------------

		List<Unit> unitvalues = obj.convertValue(inputMap.get("Unit"), new TypeReference<List<Unit>>() {
		});

		if (!UnitName.isEmpty()) {
			List<Unit> existunitData = UnitRepository.findByNsitecodeAndSunitnameIgnoreCaseIn(siteCode, UnitName);
			Map<String, Unit> existUnitListvalues = existunitData.stream()
					.collect(Collectors.toMap(Unit::getSunitname, unit -> unit));
			unitvalues = unitvalues.stream().map(item -> {
				Unit existingUnit = existUnitListvalues.get(item.getSunitname());

				if (existingUnit != null) {
					return existingUnit;
				} else {
					item.setCreatedate(currentDate);
					return item;
				}
			}).collect(Collectors.toList());
			
			UnitRepository.save(unitvalues);
		}

		// material -------------------------------------------------------

		List<Elnmaterial> materialDatas = obj.convertValue(inputMap.get("Elnmaterial"),
				new TypeReference<List<Elnmaterial>>() {
				});
		List<String> ElnmaterialName = (List<String>) inputMap.get("ElnmaterialName");
		Map<String, Unit> unitfinalmap = unitvalues.stream()
				.collect(Collectors.toMap(Unit::getSunitname, unit -> unit));
		if (!materialDatas.isEmpty()) {

			Map<String, Long> existMaterialList = checkDeblicaterecord(ElnmaterialName, siteCode, "Elnmaterial",
					"Smaterialname");

			materialDatas = materialDatas.stream().map(itemsv -> {
//				MaterialCategory unitmaterialcategory = materialCatMapaftersave
//						.get(itemsv.getMaterialcategory().getSmaterialcatname());
//				if (materialCategoriesExistvalues.get(itemsv.getMaterialcategory().getSmaterialcatname()) > 0) {
//					Long existcount = materialCategoriesExistvalues
//							.get(itemsv.getMaterialcategory().getSmaterialcatname()) + 1;
//					unitmaterialcategory = materialCatMapaftersave
//							.get(itemsv.getMaterialcategory().getSmaterialcatname() + "(" + existcount + ")");
//				}
//				if (unitmaterialcategory != null && unitmaterialcategory.getNmaterialcatcode() != null) {
//					itemsv.setMaterialcategory(unitmaterialcategory);
					if (itemsv.getUnit().getSunitname() != null && !unitfinalmap.isEmpty()) {
						itemsv.setUnit(unitfinalmap.get(itemsv.getUnit().getSunitname()));
					}
//				}
				if (existMaterialList.get(itemsv.getSmaterialname()) > 0) {
					Long existcount = existMaterialList.get(itemsv.getSmaterialname()) + 1;
					itemsv.setSmaterialname(itemsv.getSmaterialname() + "(" + existcount + ")");
				}
				itemsv.setCreateddate(currentDate);
				return itemsv;
			}).filter(Objects::nonNull).collect(Collectors.toList());

			ElnmaterialRepository.save(materialDatas);
			Map<String, Elnmaterial> materialMapaftersave = materialDatas.stream()
					.collect(Collectors.toMap(Elnmaterial::getSmaterialname, material -> material));

			String sformattype = "{yyyy}/{99999}";

			List<ElnmaterialInventory> objInventory = obj.convertValue(inputMap.get("selectedInventory"),
					new TypeReference<List<ElnmaterialInventory>>() {
					});
			final LScfttransaction cft = obj.convertValue(inputMap.get("objsilentaudit"), LScfttransaction.class);
			List<SelectedInventoryMapped> objStorageLocation = obj.convertValue(inputMap.get("selectedStorageLocation"),
					new TypeReference<List<SelectedInventoryMapped>>() {
					});

			objInventory.forEach(objInv -> {
				try {
					String materialName = objInv.getMaterial().getSmaterialname();
//					String materialCategoryName = objInv.getMaterialcategory().getSmaterialcatname();
//					String materialTypeName = objInv.getMaterialtype().getSmaterialtypename();
					String unitname = objInv.getUnit().getSunitname();
					boolean isExpiry = objInv.getMaterial().getExpirytype() != null
							&& objInv.getMaterial().getExpirytype() == 1;
					Integer ntransStatus = (objInv.getMaterial().getQuarantine() != null
							&& objInv.getMaterial().getQuarantine()) ? 37
									: (objInv.getMaterial().getOpenexpiry() != null
											&& objInv.getMaterial().getOpenexpiry()) ? 22 : 28;
					objInv.setMaterial(updateMaterial(materialName, existMaterialList, materialMapaftersave));
//					objInv.setMaterialcategory(updateMaterial(materialCategoryName, materialCategoriesExistvalues,
//							materialCatMapaftersave));
					if (unitname != null) {
						objInv.setUnit(unitfinalmap.get(unitname));
					}

//					objInv.setMaterialtype(materialTypeMapFull.get(objInv.getMaterialtype().getSmaterialtypename()));
					objInv.setCreateddate(commonfunction.getCurrentUtcTime());
					objInv.setIsexpiry(isExpiry);
					objInv.setNtransactionstatus(ntransStatus);
//					if(objInv.getInventoryname()!=null) {
//						objInv.setInventoryname(objInv.getInventoryname());
//					}
					
					objInv.setCreatedby(objInv.getCreatedby());

				} catch (ParseException e) {
					e.printStackTrace();
				}
			});

			elnmaterialInventoryReppository.save(objInventory);

			objInventory.forEach(objInv -> {
				try {
					String stridformat = MaterialInventoryService
							.returnSubstring(objInv.getMaterialtype().getSmaterialtypename()) + "/"
							+ MaterialInventoryService.returnSubstring(objInv.getMaterial().getSmaterialname()) + "/"
							+ MaterialInventoryService.getfnFormat(objInv.getNmaterialinventorycode(), sformattype);
					objInv.setSinventoryid(stridformat);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});

			elnmaterialInventoryReppository.save(objInventory);

			List<SelectedInventoryMapped> newStorageEntry = new ArrayList<>();
			IntStream.range(0, objStorageLocation.size()).forEach(i -> {
				SelectedInventoryMapped storage = objStorageLocation.get(i);
				SampleStorageLocation objLocation = new SampleStorageLocation();
				ElnmaterialInventory invcode = objInventory.get(i);
				objLocation.setSamplestoragelocationkey(
						storage.getSamplestoragelocationkey().getSamplestoragelocationkey());
				storage.setId(storage.getId());
				storage.setStoragepath(storage.getStoragepath());
				storage.setSamplestoragelocationkey(objLocation);
				storage.setNmaterialinventorycode(invcode.getNmaterialinventorycode());
				newStorageEntry.add(storage);
			});

			selectedInventoryMappedRepository.save(newStorageEntry);
		}
		return new ResponseEntity<>(responseMap, HttpStatus.OK);
	}

	private static <T> T updateMaterial(String name, Map<String, Long> existMap, Map<String, T> afterSaveMap) {
		if (existMap.get(name) != null && existMap.get(name) > 0) {
			Long existCount = existMap.get(name) + 1;
			return afterSaveMap.get(name + "(" + existCount + ")");
		} else {
			return afterSaveMap.get(name);
		}
	}

	public Map<String, Long> checkDeblicaterecord(List<String> materialTypeNames, Integer siteCode, String tableName,
			String Columnanme) {
		Map<String, Long> resultMap = new HashMap<>();
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT ");
		for (int i = 0; i < materialTypeNames.size(); i++) {
			if (i > 0) {
				queryBuilder.append(", ");
			}

			queryBuilder.append("COUNT(CASE WHEN ").append(Columnanme).append(" LIKE ?").append(i + 1)
					.append(" THEN 1 END) AS ").append("pattern" + (i + 1));
		}
		queryBuilder.append(" FROM ").append(tableName).append(" WHERE nsitecode =").append(siteCode);
		Query query = entityManager.createNativeQuery(queryBuilder.toString());

		for (int i = 0; i < materialTypeNames.size(); i++) {
			query.setParameter(i + 1, "%" + materialTypeNames.get(i) + "%");
		}
		List<Object[]> results = query.getResultList();
		if (!results.isEmpty()) {
			Object firstElement = results.get(0);
			if (firstElement instanceof BigInteger) {
				BigInteger bigInt = (BigInteger) firstElement;
				materialTypeNames.forEach(materialType -> resultMap.put(materialType, bigInt.longValue()));
			} else if (firstElement instanceof Object[]) {
				Object[] row = (Object[]) firstElement;
				for (int i = 0; i < materialTypeNames.size(); i++) {
					BigInteger count = (BigInteger) row[i];
					resultMap.put(materialTypeNames.get(i), count.longValue());
				}
			}
		}

		return resultMap;
	}

	public ResponseEntity<Object> getAllActiveSampleStorageLocationforimport(Integer nsiteInteger) {
		List<SampleStorageLocation> sampleStorageLocationList = sampleStorageLocationRepository
				.findBySamplestoragelocationkeyOrSitekeyOrderBySamplestoragelocationkeyDesc(-1,nsiteInteger);

		Map<String, Object> objMap = new LinkedHashMap<String, Object>();

		if (sampleStorageLocationList != null && sampleStorageLocationList.size() > 0) {
			objMap.put("sampleStorageLocation", sampleStorageLocationList);
		}
		return new ResponseEntity<>(objMap, HttpStatus.OK);
	}
}