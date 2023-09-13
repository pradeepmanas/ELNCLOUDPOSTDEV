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

import com.agaram.eln.primary.model.material.MaterialInventory;
import com.agaram.eln.primary.service.material.MaterialInventoryService;

@RestController
@RequestMapping(value = "materialinventory")
public class MaterialInventoryController {

	@Autowired
	private MaterialInventoryService materialInventoryService;

	@RequestMapping(value = "/getMaterialInventory", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getMaterialInventory(@RequestBody Map<String, Object> inputMap) throws Exception {
		Integer nsiteInteger = (Integer) inputMap.get("nsitecode");
		return (ResponseEntity<Object>) materialInventoryService.getMaterialInventory(nsiteInteger);
	}

	@RequestMapping(value = "/getMaterialInventorycombo", method = RequestMethod.POST)
	public ResponseEntity<Object> getMaterialInventorycombo(@RequestBody Map<String, Object> inputMap)
			throws Exception {
		Integer nmaterialtypecode = (Integer) inputMap.get("nmaterialtypecode");
		Integer nmaterialcatcode = null;
		Integer nsiteInteger = (Integer) inputMap.get("nsitecode");
		if (inputMap.containsKey("nmaterialcatcode")) {
			nmaterialcatcode = (int) inputMap.get("nmaterialcatcode");
		}
		return (ResponseEntity<Object>) materialInventoryService.getMaterialInventorycombo(nmaterialtypecode,nmaterialcatcode,nsiteInteger);
	}

	@RequestMapping(value = "/getMaterialInventoryByID", method = RequestMethod.POST)
	public ResponseEntity<Object> getMaterialInventoryByID(@RequestBody Map<String, Object> inputMap) throws Exception {

		return materialInventoryService.getMaterialInventoryByID(inputMap);
	}
	
	@RequestMapping(value = "/getMaterialInventoryBySearchField", method = RequestMethod.POST)
	public ResponseEntity<Object> getMaterialInventoryBySearchField(@RequestBody Map<String, Object> inputMap) throws Exception {

		return materialInventoryService.getMaterialInventoryBySearchField(inputMap);
	}
	
	@RequestMapping(value = "/getMaterialInventoryByStorageID", method = RequestMethod.POST)
	public ResponseEntity<Object> getMaterialInventoryByStorageID(@RequestBody Map<String, Object> inputMap) throws Exception {

		return materialInventoryService.getMaterialInventoryByStorageID(inputMap);
	}

	@RequestMapping(value = "/createMaterialInventory", method = RequestMethod.POST)
	public ResponseEntity<Object> createMaterialInventory(@RequestBody Map<String, Object> inputMap) throws Exception {
		return materialInventoryService.createMaterialInventory(inputMap);
	}
	
	@RequestMapping(value = "/updateMaterialInventory", method = RequestMethod.POST)
	public ResponseEntity<Object> UpdateMaterialInventory(@RequestBody Map<String, Object> inputMap) throws Exception {
		return materialInventoryService.UpdateMaterialInventory(inputMap);
	}

	@RequestMapping(value = "/getQuantityTransaction", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getQuantityTransaction(@RequestBody Map<String, Object> inputMap) throws Exception {
		return (ResponseEntity<Object>) materialInventoryService.getQuantityTransaction(inputMap);
	}

	@RequestMapping(value = "/getMaterialInventoryDetails", method = RequestMethod.POST)
	public ResponseEntity<Object> getMaterialInventoryDetails(@RequestBody Map<String, Object> inputMap)
			throws Exception {
		return materialInventoryService.getMaterialInventoryDetails(inputMap);
	}

	@RequestMapping(value = "/deleteMaterialInventory", method = RequestMethod.POST)
	public ResponseEntity<Object> deleteMaterialInventory(@RequestBody Map<String, Object> inputMap) throws Exception {
		return materialInventoryService.deleteMaterialInventory(inputMap);
	}

	@RequestMapping(value = "/updateMaterialStatus", method = RequestMethod.POST)
	public ResponseEntity<Object> updateMaterialStatus(@RequestBody Map<String, Object> inputMap) throws Exception {
		return materialInventoryService.updateMaterialStatus(inputMap);
	}
	
	@RequestMapping(value = "/deleteMaterialStatus", method = RequestMethod.POST)
	public ResponseEntity<Object> deleteMaterialStatus(@RequestBody Map<String, Object> inputMap) throws Exception {
		return materialInventoryService.updateMaterialStatus(inputMap);
	}

	@RequestMapping(value = "/getMaterialInventoryEdit", method = RequestMethod.POST)
	public ResponseEntity<Object> getMaterialInventoryEdit(@RequestBody Map<String, Object> inputMap) throws Exception {

		return materialInventoryService.getMaterialInventoryEdit(inputMap);
	}
	
	@RequestMapping(value = "/getMaterialInventorySearchByID", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getMaterialInventorySearchByID(@RequestBody Map<String, Object> inputMap) throws Exception {
		
		return (ResponseEntity<Object>) materialInventoryService.getMaterialInventorySearchByID(inputMap);
	}
	
	@PostMapping("/CloudUploadattachments")
	public MaterialInventory CloudUploadattachments(@RequestParam("file") MultipartFile file,
			@RequestParam("order") Integer nmaterialinventorycode, @RequestParam("filename") String filename,
			@RequestParam("fileexe") String fileexe, @RequestParam("usercode") Integer usercode,
			@RequestParam("date") Date currentdate,@RequestParam("isMultitenant") Integer isMultitenant)
			throws IOException {
		return materialInventoryService.CloudUploadattachments(file, nmaterialinventorycode, filename, fileexe, usercode, currentdate,isMultitenant);
	}
	
	@RequestMapping(value = "/getAttachments", method = RequestMethod.POST)
	public Map<String, Object> getAttachments(@RequestBody Map<String, Object> inputMap) throws Exception {

		return materialInventoryService.getAttachments(inputMap);
	}
}