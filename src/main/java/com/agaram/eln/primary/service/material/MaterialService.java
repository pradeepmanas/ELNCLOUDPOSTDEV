package com.agaram.eln.primary.service.material;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.agaram.eln.primary.model.material.MaterialCategory;
import com.agaram.eln.primary.model.material.MaterialConfig;
import com.agaram.eln.primary.model.material.MaterialType;
import com.agaram.eln.primary.repository.material.MaterialCategoryRepository;
import com.agaram.eln.primary.repository.material.MaterialConfigRepository;
import com.agaram.eln.primary.repository.material.MaterialTypeRepository;

@Service
public class MaterialService {

	@Autowired
	MaterialTypeRepository materialTypeRepository;
	@Autowired
	MaterialCategoryRepository materialCategoryRepository;
	@Autowired
	MaterialConfigRepository materialConfigRepository;

	public ResponseEntity<Object> getMaterialcombo(Integer nmaterialtypecode) {

		Map<String, Object> objmap = new LinkedHashMap<String, Object>();

		List<MaterialCategory> lstMaterialCategory = materialCategoryRepository .findByNmaterialtypecode(nmaterialtypecode);
		
		objmap.put("MaterialCategoryMain", lstMaterialCategory);
		return new ResponseEntity<>(objmap, HttpStatus.OK);
	}

	public ResponseEntity<Object> getMaterialType() {
		Map<String, Object> objmap = new LinkedHashMap<String, Object>();

		List<MaterialType> lstMaterialType = materialTypeRepository.findAll();

		objmap.put("MaterialType", lstMaterialType);
		objmap.put("SelectedMaterialType", lstMaterialType);

		List<MaterialCategory> lstMaterialCategory = materialCategoryRepository
				.findByNmaterialtypecode(lstMaterialType.get(0).getNmaterialtypecode());

		if (!lstMaterialCategory.isEmpty()) {
			objmap.put("MaterialCategoryMain", lstMaterialCategory);
			objmap.put("nmaterialcatcode", lstMaterialCategory.get(0).getNmaterialcatcode());
		}
		objmap.put("nmaterialtypecode", lstMaterialType.get(0).getNmaterialtypecode());
//		objmap.putAll((Map<String, Object>) getMaterialByTypeCode(objmap, objUserInfo).getBody());
//		objmap.putAll((Map<String, Object>) getMaterialAdd((short) objmap.get("nmaterialtypecode"), ).getBody());

		return new ResponseEntity<>(objmap, HttpStatus.OK);
	}

	public ResponseEntity<Object> getMaterialByTypeCode(Map<String, Object> inputMap) {
		
		Map<String, Object> objmap = new LinkedHashMap<String, Object>();
		List<Map<String, Object>> lstMaterial = new ArrayList<Map<String, Object>>();

		if (inputMap.containsKey("nmaterialcatcode")) {

			objmap.put("Material", lstMaterial);

			objmap.put("tabScreen", "IDS_MATERIALSECTION");

			if (!lstMaterial.isEmpty()) {
				objmap.put("SelectedMaterial", lstMaterial.get(lstMaterial.size() - 1));
				objmap.put("nmaterialcode", (int) lstMaterial.get(lstMaterial.size() - 1).get("nmaterialcode"));

			} else {
				objmap.put("SelectedMaterial", lstMaterial);
			}

			List<MaterialType> lstMaterialType = materialTypeRepository
					.findByNmaterialtypecode((Integer) inputMap.get("nmaterialtypecode"));
			objmap.put("SelectedMaterialType", lstMaterialType);

			List<MaterialCategory> objLstMaterialCategory = new ArrayList<>();

			if (inputMap.containsKey("nmaterialcatcode")) {
				objLstMaterialCategory = materialCategoryRepository
						.findByNmaterialcatcode((Integer) inputMap.get("nmaterialcatcode"));
			} else {
				objLstMaterialCategory = materialCategoryRepository
						.findByNmaterialtypecode((Integer) inputMap.get("nmaterialtypecode"));
			}
			List<MaterialCategory> lstMaterialCategory = objLstMaterialCategory;

			if (!lstMaterialCategory.isEmpty()) {
				if (inputMap.containsKey("nmaterialcatcode")) {
					objmap.put("SelectedMaterialCategory", lstMaterialCategory.get(0));
				} else {
					objmap.put("SelectedMaterialCategory", lstMaterialCategory.get(0));
				}
			}

		} else {

			List<MaterialType> lstMaterialType = materialTypeRepository
					.findByNmaterialtypecode((Integer) inputMap.get("nmaterialtypecode"));
			List<MaterialType> lstActiontype = new ArrayList<MaterialType>();

			objmap.put("SelectedMaterialType", lstMaterialType);
			objmap.put("Material", lstActiontype);
			objmap.put("SelectedMaterial", lstActiontype);
			objmap.put("SelectedMaterialCategory", lstActiontype);
		}

		List<MaterialConfig> lstMaterialConfig = materialConfigRepository.findByNformcode(40);
		objmap.put("selectedTemplate", lstMaterialConfig);
		
		return new ResponseEntity<>(objmap, HttpStatus.OK);
	}
	
}
