package com.agaram.eln.primary.service.Material;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.agaram.eln.primary.model.Material.Lsmaterialcategory;
import com.agaram.eln.primary.model.Material.MaterialCategory;
import com.agaram.eln.primary.model.Material.MaterialType;
import com.agaram.eln.primary.repository.Material.MaterialCategoryRepository;
import com.agaram.eln.primary.repository.Material.MaterialTypeRepository;


@Service
@EnableJpaRepositories(basePackageClasses = Lsmaterialcategory.class)
public class MaterialCategoryService {
	
	@Autowired
	MaterialTypeRepository MaterialTypeRepository;
	
	@Autowired
	MaterialCategoryRepository MaterialCategoryRepository; 
	

	public ResponseEntity<Object> getMaterialType(Map<String, Object> inputMap) {
Map<String, Object> objmap = new LinkedHashMap<String, Object>();
		
		List<MaterialType> lstgetMaterialType =MaterialTypeRepository.findAll();
		objmap.put("getMaterialType", lstgetMaterialType);
		return new ResponseEntity<>(objmap, HttpStatus.OK);
	}

	public ResponseEntity<Object> getMaterialCategory(Map<String, Object> inputMap) {
		
		Map<String, Object> objmap = new LinkedHashMap<String, Object>();
		
		List<MaterialCategory> lstgetMaterialCategory =MaterialCategoryRepository.findAll();
		objmap.put("getMaterialCategory", lstgetMaterialCategory);
		return new ResponseEntity<>(objmap, HttpStatus.OK);
	}

}
