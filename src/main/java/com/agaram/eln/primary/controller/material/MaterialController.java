package com.agaram.eln.primary.controller.material;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.agaram.eln.primary.model.material.Material;
import com.agaram.eln.primary.service.material.MaterialService;

@RestController
@RequestMapping("/material")
public class MaterialController {

	@Autowired
	private MaterialService objMaterialService;

	@RequestMapping(value = "/getMaterial", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getMaterialType(@RequestBody Map<String, Object> inputMap) throws Exception {

		Integer nsiteInteger = (Integer) inputMap.get("nsitecode");
		
		return (ResponseEntity<Object>) objMaterialService.getMaterialType(nsiteInteger);
	}

	@RequestMapping(value = "/getMaterialByTypeCode", method = RequestMethod.POST)
	public ResponseEntity<Object> getMaterialByTypeCode(@RequestBody Map<String, Object> inputMap) throws Exception {

		return objMaterialService.getMaterialByTypeCode(inputMap);
	}
	
	@RequestMapping(value = "/getMaterialByTypeCodeByDate", method = RequestMethod.POST)
	public ResponseEntity<Object> getMaterialByTypeCodeByDate(@RequestBody Map<String, Object> inputMap) throws Exception {

		return objMaterialService.getMaterialByTypeCodeByDate(inputMap);
	}

	@RequestMapping(value = "/getMaterialcombo", method = RequestMethod.POST)
	public ResponseEntity<Object> getMaterialcombo(@RequestBody Map<String, Object> inputMap) throws Exception {
		Integer nmaterialtypecode = (Integer) inputMap.get("nmaterialtypecode");
		Integer nsiteInteger = (Integer) inputMap.get("nsitecode");
		return (ResponseEntity<Object>) objMaterialService.getMaterialcombo(nmaterialtypecode,nsiteInteger);
	}

	@RequestMapping(value = "/createMaterial", method = RequestMethod.POST)
	public ResponseEntity<Object> createMaterial(@RequestBody Map<String, Object> inputMap) throws Exception {

		return objMaterialService.createMaterial(inputMap);
	}

	@RequestMapping(value = "/getMaterialDetails", method = RequestMethod.POST)
	public ResponseEntity<Object> getMaterialDetails(@RequestBody Map<String, Object> inputMap) throws Exception {
		return objMaterialService.getMaterialDetails(inputMap);
	}

	@RequestMapping(value = "/deleteMaterial", method = RequestMethod.POST)
	public ResponseEntity<Object> deleteMaterial(@RequestBody Map<String, Object> inputMap) throws Exception {

		return objMaterialService.deleteMaterial(inputMap);
	}

	@RequestMapping(value = "/getMaterialEdit", method = RequestMethod.POST)
	public ResponseEntity<Object> getMaterialEdit(@RequestBody Map<String, Object> inputMap) throws Exception {

		return objMaterialService.getMaterialEdit(inputMap);
	}

	@RequestMapping(value = "/getMaterialByID", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getMaterialByID(@RequestBody Map<String, Object> inputMap) throws Exception {
		
		return (ResponseEntity<Object>) objMaterialService.getMaterialByID(inputMap);
	}
	
	@RequestMapping(value = "/updateMaterial", method = RequestMethod.POST)
	public ResponseEntity<Object> UpdateMaterial(@RequestBody Map<String, Object> inputMap) throws Exception {
		return objMaterialService.UpdateMaterial(inputMap);
	}
	
	@PostMapping("/CloudUploadattachments")
	public Material CloudUploadattachments(@RequestParam("file") MultipartFile file,
			@RequestParam("order") Integer nmaterialcatcode, @RequestParam("filename") String filename,
			@RequestParam("fileexe") String fileexe, @RequestParam("usercode") Integer usercode,
			@RequestParam("date") Date currentdate,@RequestParam("isMultitenant") Integer isMultitenant)
			throws IOException {
		return objMaterialService.CloudUploadattachments(file, nmaterialcatcode, filename, fileexe, usercode, currentdate,isMultitenant);
	}
	
	@RequestMapping(value = "/getAttachments", method = RequestMethod.POST)
	public Map<String, Object> getAttachments(@RequestBody Map<String, Object> inputMap) throws Exception {

		return objMaterialService.getAttachments(inputMap);
	}
	
	@PostMapping("/cloudUploadFilesWithTags")
	public Material cloudUploadFilesWithTags(@RequestParam("file") MultipartFile file,
			@RequestParam("order") Integer nmaterialcatcode, @RequestParam("filename") String filename,
			@RequestParam("fileexe") String fileexe, @RequestParam("usercode") Integer usercode,
			@RequestParam("date") Date currentdate,@RequestParam("isMultitenant") Integer isMultitenant)
			throws IOException {
		return objMaterialService.cloudUploadFilesWithTags(file, nmaterialcatcode, filename, fileexe, usercode, currentdate,isMultitenant);
	}
}
