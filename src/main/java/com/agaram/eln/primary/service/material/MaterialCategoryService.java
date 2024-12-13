package com.agaram.eln.primary.service.material;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.agaram.eln.primary.commonfunction.commonfunction;
import com.agaram.eln.primary.global.Enumeration;
import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.material.Elnmaterial;
import com.agaram.eln.primary.model.material.MaterialCategory;
import com.agaram.eln.primary.model.material.MaterialType;
import com.agaram.eln.primary.model.material.Unit;
import com.agaram.eln.primary.model.usermanagement.LSMultiusergroup;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.repository.material.ElnmaterialRepository;
import com.agaram.eln.primary.repository.material.MaterialCategoryRepository;
import com.agaram.eln.primary.repository.material.MaterialTypeRepository;
import com.agaram.eln.primary.repository.material.UnitRepository;
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
	    List<String> materialCatNames = (List<String>) inputMap.get("MaterialCatName");
	    List<String> materialTypeNames = (List<String>) inputMap.get("MaterialName");
	    List<String> UnitName = (List<String>) inputMap.get("UnitName");

	    List<MaterialCategory> materialCategoriesExist = MaterialCategoryRepository
	            .findByNsitecodeAndSmaterialcatnameIgnoreCaseIn(siteCode, materialCatNames);
	    Map<String, MaterialCategory> materialCatMap = materialCategoriesExist.stream()
	            .collect(Collectors.toMap(MaterialCategory::getSmaterialcatname, cat -> cat));

	    List<MaterialType> materialTypes = obj.convertValue(inputMap.get("Materialcategory_MaterialType"),
	            new TypeReference<List<MaterialType>>() {
	            });
	    List<MaterialType> materialTypeValues = MaterialTypeRepository
	            .findByNsitecodeAndSmaterialtypenameIgnoreCaseIn(siteCode, materialTypeNames);
	    Map<String, MaterialType> materialTypeMap = materialTypeValues.stream()
	            .collect(Collectors.toMap(MaterialType::getSmaterialtypename, materialType -> materialType));

	    Date currentDate = commonfunction.getCurrentUtcTime();


	    materialTypes = materialTypes.stream()
	            .filter(item -> materialCatMap.get(item.getSmaterialcatname()) == null)
	            .map(item -> {
	                MaterialType matchedMaterialType = materialTypeMap.get(item.getSmaterialtypename());
	                if (matchedMaterialType != null) {
	                    return matchedMaterialType;
	                } else {
	                    item.setCreatedate(currentDate);
	                    return item;
	                }
	            })
	            .filter(Objects::nonNull)
	            .collect(Collectors.toList());

	    MaterialTypeRepository.save(materialTypes);

	    List<MaterialCategory> materialCategories = obj.convertValue(inputMap.get("Materialcategory"),
	            new TypeReference<List<MaterialCategory>>() {
	            });

	    Map<String, MaterialType> materialTypeMapFull = materialTypes.stream()
	            .collect(Collectors.toMap(MaterialType::getSmaterialtypename, materialType -> materialType));

	    List<MaterialCategory> updatedMaterialCategories = materialCategories.stream()
	            .filter(item -> materialCatMap.get(item.getSmaterialcatname()) == null)
	            .map(item -> {
	                MaterialType matchedMaterialType = materialTypeMapFull.get(item.getSmaterialtypename());
	                if (matchedMaterialType != null && matchedMaterialType.getNmaterialtypecode() != null) {
	                    item.setNmaterialtypecode(matchedMaterialType.getNmaterialtypecode());
	                }
	                item.setCreatedate(currentDate);
	                return item;
	            })
	            .filter(Objects::nonNull)
	            .collect(Collectors.toList());

	    MaterialCategoryRepository.save(updatedMaterialCategories);

	    Map<String, MaterialCategory> materialCatMapaftersave=updatedMaterialCategories.stream().collect(Collectors.toMap(MaterialCategory::getSmaterialcatname, materialcategory ->materialcategory));
	    
	    //unit -------------------------------------
	    
	    List<Unit> unitvalues = obj.convertValue(inputMap.get("Unit"),new TypeReference<List<Unit>>() {});
	    if(!UnitName.isEmpty()) {
	    	List<Unit> storedUnitList = UnitRepository
	    		    .findByNsitecodeAndSunitnameIgnoreCaseIn(siteCode, UnitName);
	    	    Map<String, Unit> unitMapFull = storedUnitList.stream()
	            .collect(Collectors.toMap(Unit::getSunitname, unit -> unit));	    	    
	    	    unitvalues = unitvalues.stream()
	    	    	    .filter(item -> materialCatMap.get(item.getSmaterialcatname()) == null)
	    	    	    .map(item -> {
	    	    	        Unit unitsingledata = unitMapFull.get(item.getSunitname());
	    	    	        if (unitsingledata == null) {
	    	    	            item.setCreatedate(currentDate);
	    	    	            return item;
	    	    	        } else {
	    	    	            return unitsingledata;
	    	    	        }
	    	    	    })
	    	    	    .collect(Collectors.toMap(
		    	    	        Unit::getSunitname,
		    	    	        item -> item,
		    	    	        (existing, replacement) -> existing
		    	    	    ))
	    	    	    .values()
	    	    	    .stream()
	    	    	    .filter(Objects::nonNull)
	    	    	    .collect(Collectors.toList());

	    	    UnitRepository.save(unitvalues);

	    }
	    
	    Map<String, Object> responseMap = new HashMap<>();
	    //material -------------------------------------------------------
	    List<Elnmaterial> materialDatas = obj.convertValue(inputMap.get("Elnmaterial"),new TypeReference<List<Elnmaterial>>() {});
	    List<String> ElnmaterialName = (List<String>) inputMap.get("ElnmaterialName");
	    if(!materialDatas.isEmpty()) {
	    	  Map<String ,Elnmaterial> materialExistdata=ElnmaterialRepository.findByNsitecodeAndSmaterialnameIgnoreCaseIn(siteCode,ElnmaterialName).stream().collect(Collectors.toMap(Elnmaterial::getSmaterialname, elnmaterila -> elnmaterila));
	    	  Map<String, Unit> unitfinalmap=unitvalues.stream().collect(Collectors.toMap(Unit::getSunitname, unit ->unit));
	    	  materialDatas=materialDatas.stream()
	    	    .filter(item -> materialCatMap.get(item.getMaterialcategory().getSmaterialcatname()) == null)
	    		.map(itemsv ->{
	    		  MaterialCategory unitmaterialcategory =materialCatMapaftersave.get(itemsv.getMaterialcategory().getSmaterialcatname());
	    		  MaterialType materialtype=materialTypeMapFull.get(itemsv.getMaterialtype().getSmaterialtypename());
	    		  if(unitmaterialcategory!=null && unitmaterialcategory.getNmaterialcatcode()!=null && materialtype!=null && materialtype.getNmaterialtypecode()!=null) {
	    			  itemsv.setMaterialcategory(unitmaterialcategory);
	    			  itemsv.setMaterialtype(materialtype);
	    			  if(itemsv.getUnit().getSunitname()!=null && !unitfinalmap.isEmpty() && unitfinalmap.get(itemsv.getUnit().getSunitname())!=null) {
	    				  itemsv.setUnit(unitfinalmap.get(itemsv.getUnit().getSunitname()));
	    			  }
	    		  }
	    		  if(materialExistdata.get(itemsv.getSmaterialname())!=null) {
	    			  return materialExistdata.get(itemsv.getSmaterialname());
	    		  }else {
	    			  itemsv.setCreateddate(currentDate) ;
	    		  }
	    		 return itemsv; 
	    	  }) .filter(Objects::nonNull).collect(Collectors.toList());
	    	  
	    	  ElnmaterialRepository.save(materialDatas); 
	    	  responseMap.put("materialDatas", materialDatas);
	    }
	    
	    responseMap.put("materialCatMapaftersave", materialCatMapaftersave);
	    responseMap.put("materialTypeMapFull", materialTypeMapFull);
	   
	   
	    return new ResponseEntity<>(responseMap, HttpStatus.OK);
	}


}