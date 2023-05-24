package com.agaram.eln.primary.controller.material;

import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.agaram.eln.primary.model.material.Section;
import com.agaram.eln.primary.service.material.SectionService;

@RestController
@RequestMapping("/section")
public class SectionController {

	@Autowired
	SectionService sectionService;

	@PostMapping(value = "/getSection")
	public ResponseEntity<Object> getSection(@RequestBody Map<String, Object> inputMap) throws Exception {

		Integer nsiteInteger = (Integer) inputMap.get("nsitecode");
		
		return sectionService.getSection(nsiteInteger);
	}

	@PostMapping(value = "/createSection")
	public ResponseEntity<Object> createSection(@RequestBody Map<String, Object> inputMap) throws Exception {

		final ObjectMapper objmapper = new ObjectMapper();
		final Section section = objmapper.convertValue(inputMap.get("section"), Section.class);

		return sectionService.createSection(section);
	}

	@PostMapping(value = "/getActiveSectionById")
	public Section getActiveSectionById(@RequestBody Map<String, Object> inputMap) throws Exception {

		final int nsectionCode = (Integer) inputMap.get("nsectioncode");

		return sectionService.getActiveSectionById(nsectionCode);
	}

	@PostMapping(value = "/updateSection")
	public ResponseEntity<Object> updateSection(@RequestBody Map<String, Object> inputMap) throws Exception {
		final ObjectMapper objmapper = new ObjectMapper();
		final Section section = objmapper.convertValue(inputMap.get("section"), Section.class);

		return sectionService.updateSection(section);
	}

	@PostMapping(value = "/deleteSection")
	public ResponseEntity<Object> deleteSection(@RequestBody Map<String, Object> inputMap) throws Exception {

		final ObjectMapper objmapper = new ObjectMapper();
		final Section section = objmapper.convertValue(inputMap.get("section"), Section.class);
		//final LScfttransaction objsilentaudit = objmapper.convertValue(inputMap.get("objsilentaudit"),LScfttransaction.class);

		return sectionService.deleteSection(section);
	}
}
