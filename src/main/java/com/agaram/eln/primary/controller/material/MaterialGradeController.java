package com.agaram.eln.primary.controller.material;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.agaram.eln.primary.model.material.MaterialGrade;
import com.agaram.eln.primary.service.material.MaterialGradeService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/grade")
public class MaterialGradeController {

	ObjectMapper objMapper = new ObjectMapper();
	
	@Autowired
	private MaterialGradeService materialGradeService;
	
	@RequestMapping(value = "/getGrade", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getGrade(@RequestBody Map<String, Object> inputMap) throws Exception {
		Integer nsiteInteger = (Integer) inputMap.get("nsitecode");
		return (ResponseEntity<Object>) materialGradeService.getGrade(nsiteInteger);
	}
	
	@RequestMapping(value = "/createGrade", method = RequestMethod.POST)
	public ResponseEntity<Object> createGrade(@RequestBody Map<String, Object> inputMap) throws Exception {
		MaterialGrade objGrade = objMapper.convertValue(inputMap.get("grade"), new TypeReference<MaterialGrade>() {});
		return materialGradeService.createGrade(objGrade);
	}

	@RequestMapping(value = "/updateGrade", method = RequestMethod.POST)
	public ResponseEntity<Object> updateGrade(@RequestBody Map<String, Object> inputMap) throws Exception {
		MaterialGrade objGrade = objMapper.convertValue(inputMap.get("grade"), new TypeReference<MaterialGrade>() {	});
		return materialGradeService.updateGrade(objGrade);
	}

	@RequestMapping(value = "/deleteGrade", method = RequestMethod.POST)
	public ResponseEntity<Object> deleteGrade(@RequestBody Map<String, Object> inputMap) throws Exception {
		MaterialGrade objGrade = objMapper.convertValue(inputMap.get("grade"), new TypeReference<MaterialGrade>() { });
		return materialGradeService.deleteGrade(objGrade);
	}

	@RequestMapping(value = "/getActiveGradeById", method = RequestMethod.POST)
	public MaterialGrade getActiveGradeById(@RequestBody Map<String, Object> inputMap) throws Exception {
		int nunitCode = (int) inputMap.get("nmaterialgradecode");
		return materialGradeService.getActiveGradeById(nunitCode);
	}
}