package com.agaram.eln.primary.service.material;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.agaram.eln.primary.repository.material.MaterialCategoryRepository;
import com.agaram.eln.primary.repository.material.MaterialGradeRepository;
import com.agaram.eln.primary.repository.material.PeriodRepository;
import com.agaram.eln.primary.repository.material.SectionRepository;
import com.agaram.eln.primary.repository.material.UnitRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;



@Service
public class DynamicPreRegDesignService {

	@Autowired
	MaterialCategoryRepository materialCategoryRepository;
	@Autowired
	UnitRepository unitRepository;
	@Autowired
	SectionRepository sectionRepository;
	@Autowired
	PeriodRepository periodRepository;
	@Autowired
	MaterialGradeRepository materialGradeRepository;

	@SuppressWarnings({ "unchecked", "unused" })
	public ResponseEntity<Object> getComboValues(Map<String, Object> inputMap) throws JsonProcessingException {
		String tableName = "";
		List<Map<String, Object>> srcData = (List<Map<String, Object>>) inputMap.get("parentcolumnlist");

		Map<String, Object> childData = (Map<String, Object>) inputMap.get("childcolumnlist");
		Map<String, Object> parameters = (Map<String, Object>) inputMap.get("parameters");
		String getJSONKeysQuery = "";

		@SuppressWarnings("unused")
		List<Map<String, Object>> filterQueryComponentsQueries = null;

		Map<String, Object> returnObject = new HashMap<>();
		for (int i = 0; i < srcData.size(); i++) {
			String sourceName = (String) srcData.get(i).get("source");
			String conditionString = srcData.get(i).containsKey("conditionstring")
					? (String) srcData.get(i).get("conditionstring")
					: "";
			String Keysofparam = "";

			while (conditionString.contains("P$")) {
				StringBuilder sb = new StringBuilder(conditionString);
				int firstindex = sb.indexOf("P$");
				int lastindex = sb.indexOf("$P");
				Keysofparam = sb.substring(firstindex + 2, lastindex);
				if (Keysofparam.contains(".")) {
					int index = Keysofparam.indexOf(".");
					String tablename = Keysofparam.substring(0, index);
					String columnName = Keysofparam.substring(index + 1, Keysofparam.length());
					if (inputMap.containsKey(tablename)) {
						Map<String, Object> userInfoMap = (Map<String, Object>) inputMap.get(tablename);
						if (userInfoMap.containsKey(columnName)) {
							sb.replace(firstindex, lastindex + 2, userInfoMap.get(columnName).toString());
						}
					}
				}
				conditionString = sb.toString();

			}

			tableName = sourceName.toLowerCase();

			List<Object> data = new ArrayList<Object>();

			switch (tableName) {
			case "unit":
				data = unitRepository.findByNstatus(1);
				break;
			case "section":
				data = sectionRepository.findByNstatus(1);
				break;
			case "materialgrade":
				data = materialGradeRepository.findByNstatus(1);
				break;
//			case "period":
//				data = periodRepository.findByNstatus(1);
//				break;
			}
			System.out.println(data);

			String label = (String) srcData.get(i).get("label");

			List<Map<String, Object>> lstJsonData = new ArrayList<Map<String, Object>>();

			if (!data.isEmpty()) {

				data.stream().peek(obj -> {
					Map<String, Object> childValue = new HashMap<>();

					childValue.put("jsondata", obj);
					
					lstJsonData.add(childValue);

				}).collect(Collectors.toList());
			}

			returnObject.put(label, lstJsonData);
		}

		return new ResponseEntity<>(returnObject, HttpStatus.OK);
	}

	@SuppressWarnings({ "unchecked", "unused" })
	public ResponseEntity<Object> getChildValues(Map<String, Object> inputMap) throws JsonProcessingException {
		String tableName = "";
		final ObjectMapper objmapper = new ObjectMapper();
		List<Map<String, Object>> srcData = (List<Map<String, Object>>) inputMap.get("parentcolumnlist");
		Map<String, Object> childData = (Map<String, Object>) inputMap.get("childcolumnlist");
		Map<String, Object> parameters = (Map<String, Object>) inputMap.get("parameters");
		String getJSONKeysQuery = "";

		List<Map<String, Object>> filterQueryComponentsQueries = null;

		Map<String, Object> returnObject = new HashMap<>();
		for (int i = 0; i < srcData.size(); i++) {
			String sourceName = (String) srcData.get(i).get("source");
			String conditionString = srcData.get(i).containsKey("conditionstring")
					? (String) srcData.get(i).get("conditionstring")
					: "";
			String Keysofparam = "";

			while (conditionString.contains("P$")) {
				StringBuilder sb = new StringBuilder(conditionString);
				int firstindex = sb.indexOf("P$");
				int lastindex = sb.indexOf("$P");
				Keysofparam = sb.substring(firstindex + 2, lastindex);
				if (Keysofparam.contains(".")) {
					int index = Keysofparam.indexOf(".");
					String tablename = Keysofparam.substring(0, index);
					String columnName = Keysofparam.substring(index + 1, Keysofparam.length());
					if (inputMap.containsKey(tablename)) {
						Map<String, Object> userInfoMap = (Map<String, Object>) inputMap.get(tablename);
						if (userInfoMap.containsKey(columnName)) {
							sb.replace(firstindex, lastindex + 2, userInfoMap.get(columnName).toString());
						}
					}
				}
				conditionString = sb.toString();

			}

			tableName = sourceName.toLowerCase();

			List<Object> data = new ArrayList<Object>();

			switch (tableName) {
			case "materialcategory":
				data = materialCategoryRepository.findByNstatus(1);
				break;
			}

			List<Map<String, Object>> lstJsonData = new ArrayList<Map<String, Object>>();

			if (!data.isEmpty()) {

				data.stream().peek(obj -> {
					Map<String, Object> childValue = new HashMap<>();

					childValue.put("jsondata", obj);

					lstJsonData.add(childValue);

				}).collect(Collectors.toList());
			}

			String label = (String) srcData.get(i).get("label");

			returnObject.put(label, lstJsonData);
		}

		return new ResponseEntity<>(returnObject, HttpStatus.OK);
	}

}
