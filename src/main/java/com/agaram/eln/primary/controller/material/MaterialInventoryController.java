package com.agaram.eln.primary.controller.material;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.agaram.eln.primary.global.FileDTO;
import com.agaram.eln.primary.model.fileManipulation.Fileimages;
import com.agaram.eln.primary.model.instrumentDetails.LsOrderLinks;
import com.agaram.eln.primary.model.material.Elnmaterial;
import com.agaram.eln.primary.model.material.ElnmaterialChemDiagRef;
import com.agaram.eln.primary.model.material.ElnmaterialInventory;
import com.agaram.eln.primary.model.material.ElnresultUsedMaterial;
import com.agaram.eln.primary.model.material.MaterialInventory;
import com.agaram.eln.primary.model.material.MaterilaInventoryLinks;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.service.material.MaterialInventoryService;
import com.agaram.eln.primary.service.material.MaterialService;

@SuppressWarnings("unused")
@RestController
@RequestMapping(value = "materialinventory")
public class MaterialInventoryController {

	@Autowired
	private MaterialInventoryService materialInventoryService;
	
	@Autowired
	private MaterialService materialService;

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
	
	@RequestMapping(value = "/getMaterialInventoryIDByDate", method = RequestMethod.POST)
	public ResponseEntity<Object> getMaterialInventoryIDByDate(@RequestBody Map<String, Object> inputMap) throws Exception {

		return materialInventoryService.getMaterialInventoryIDByDate(inputMap);
	}
	
	@RequestMapping(value = "/getMaterialInvCombo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getMaterialInvCombo(@RequestBody Map<String, Object> inputMap) throws Exception {

		Integer ntypecode = (Integer) inputMap.get("nmaterialtypecode");
		Integer nmaterialcatcode = (Integer) inputMap.get("nmaterialcatcode");
		Integer nflag = (Integer) inputMap.get("nflag");
		
		if(nflag == 1) {
			return (ResponseEntity<Object>) materialInventoryService.getMaterialInvCombo(ntypecode,nflag);
		}else {
			return (ResponseEntity<Object>) materialInventoryService.getMaterialInvCombo(nmaterialcatcode,nflag);
		}
	}
	
	@RequestMapping(value = "/getMaterialTypeDesign", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getMaterialTypeDesign(@RequestBody Map<String, Object> inputMap) throws Exception {
		
		return (ResponseEntity<Object>) materialInventoryService.getMaterialTypeDesign(inputMap);
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
	public ElnmaterialInventory CloudUploadattachments(@RequestParam("file") MultipartFile file,
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
	
	@RequestMapping(value = "/getMaterialInventorytransDetails", method = RequestMethod.POST)
	public ResponseEntity<Object> getMaterialInventorytransDetails(@RequestBody Map<String, Object> inputMap)
			throws Exception {
		return materialInventoryService.getMaterialInventorytransDetails(inputMap);
	}
	
	@RequestMapping(value = "/getElnMaterialInventoryByIdBarCode", method = RequestMethod.POST)
	public ResponseEntity<Object> getElnMaterialInventoryByIdBarCode(@RequestBody Map<String, Object> inputMap)
			throws Exception {
		return materialInventoryService.getElnMaterialInventoryByIdBarCode(inputMap);
	}
	
	@RequestMapping(value = "/getElnMaterialInventoryByIdBarCodeFilter", method = RequestMethod.POST)
	public ResponseEntity<Object> getElnMaterialInventoryByIdBarCodeFilter(@RequestBody Map<String, Object> inputMap)
			throws Exception {
		return materialInventoryService.getElnMaterialInventoryByIdBarCodeFilter(inputMap);
	}
	
	/**
	 * Added by sathishkumar chandrasekar for new inventory changes 
	 * dated on 18-10-2023
	 */
	
	@RequestMapping(value = "/getElnMaterialInventory", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getElnMaterialInventory(@RequestBody Map<String, Object> inputMap) throws Exception {
		
		return (ResponseEntity<Object>) materialInventoryService.getElnMaterialInventory(inputMap);
	}
	@RequestMapping(value = "/getElnMaterialInventoryonprotocol", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getElnMaterialInventoryonprotocol(@RequestBody Map<String, Object> inputMap) throws Exception {
		
		return (ResponseEntity<Object>) materialInventoryService.getElnMaterialInventoryonprotocol(inputMap);
	}
	
	@RequestMapping(value = "/getElnMaterialInventoryByFilter", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getElnMaterialInventoryByFilter(@RequestBody Map<String, Object> inputMap) throws Exception {
		
		return (ResponseEntity<Object>) materialInventoryService.getElnMaterialInventoryByFilter(inputMap);
	}
	@RequestMapping(value = "/getElnMaterialInventoryByFilterForprotocol", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getElnMaterialInventoryByFilterForprotocol(@RequestBody Map<String, Object> inputMap) throws Exception {
		
		return (ResponseEntity<Object>) materialInventoryService.getElnMaterialInventoryByFilterForprotocol(inputMap);
	}
	
	@RequestMapping(value = "/getElnMaterialByFilter", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getElnMaterialByFilter(@RequestBody Map<String, Object> inputMap) throws Exception {
		
		return (ResponseEntity<Object>) materialService.getElnMaterialByFilter(inputMap);
	}
	
	@RequestMapping(value = "/getElnMaterialInventoryByStorage", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getElnMaterialInventoryByStorage(@RequestBody Map<String, Object> inputMap) throws Exception {
		
		return (ResponseEntity<Object>) materialInventoryService.getElnMaterialInventoryByStorage(inputMap);
	}
	
	@RequestMapping(value = "/getELNInventoryProps", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getELNInventoryProps(@RequestBody Map<String, Object> inputMap) throws Exception {

		Integer nsiteInteger = (Integer) inputMap.get("nsitecode");
		
		return (ResponseEntity<Object>) materialInventoryService.getELNInventoryProps(nsiteInteger);
	}
	
	@RequestMapping(value = "/getELNMaterialTypeBasedCat", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getMaterialTypeBasedCat(@RequestBody Map<String, Object> inputMap) throws Exception {
		
		return (ResponseEntity<Object>) materialInventoryService.getMaterialTypeBasedCat(inputMap);
	}
	
	@RequestMapping(value = "/getELNMaterialCatBasedMaterial", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getELNMaterialCatBasedMaterial(@RequestBody Map<String, Object> inputMap) throws Exception {
		
		return (ResponseEntity<Object>) materialInventoryService.getELNMaterialCatBasedMaterial(inputMap);
	}
	
	@RequestMapping(value = "/createElnMaterialInventory", method = RequestMethod.POST)
	public ResponseEntity<Object> createElnMaterialInventory(@RequestBody Map<String, Object> inputMap) throws Exception {
		return materialInventoryService.createElnMaterialInventory(inputMap);
	}
	
	@RequestMapping(value = "/updateElnMaterialInventory", method = RequestMethod.POST)
	public ResponseEntity<Object> updateElnMaterialInventory(@RequestBody ElnmaterialInventory objElnmaterialInventory) throws Exception {
		return materialInventoryService.updateElnMaterialInventory(objElnmaterialInventory);
	}
	
	@RequestMapping(value = "/updateElnMaterialInventoryStock", method = RequestMethod.POST)
	public ResponseEntity<Object> updateElnMaterialInventoryStock(@RequestBody ElnmaterialInventory objElnmaterialInventory) throws Exception {
		return materialInventoryService.updateElnMaterialInventoryStock(objElnmaterialInventory);
	}
	
	@RequestMapping(value = "/getPathOnInventory", method = RequestMethod.POST)
	public ResponseEntity<Object> getPathOnInventory(@RequestBody Integer objElnmaterialInventory) throws Exception {
		return materialInventoryService.getPathOnInventory(objElnmaterialInventory);
	}
	
	@RequestMapping(value = "/OsearchElnMaterialInventory", method = RequestMethod.POST)
	public ResponseEntity<Object> OsearchElnMaterialInventory(@RequestBody Map<String, Object> inputMap) throws Exception {
		return materialInventoryService.OsearchElnMaterialInventory(inputMap);
	}
	
	@RequestMapping(value = "/getELNMaterialBySearchField", method = RequestMethod.POST)
	public ResponseEntity<Object> getELNMaterialBySearchField(@RequestBody Map<String, Object> inputMap) throws Exception {

		return materialInventoryService.getELNMaterialBySearchField(inputMap);
	}
	
	@RequestMapping(value = "/getElnMaterialInventoryById", method = RequestMethod.POST)
	public ResponseEntity<Object> getElnMaterialInventoryById(@RequestBody Map<String, Object> inputMap) throws Exception {
		return materialInventoryService.getElnMaterialInventoryById(inputMap);
	}
	
	@RequestMapping(value = "/getElnMaterialInventoryByMaterial", method = RequestMethod.POST)
	public ResponseEntity<Object> getElnMaterialInventoryByMaterial(@RequestBody List<Integer> lstMaterial) throws Exception {
		return materialInventoryService.getElnMaterialInventoryByMaterial(lstMaterial);
	}
	
	@RequestMapping(value = "/getInventorytransactionhistory", method = RequestMethod.POST)
	public ResponseEntity<Object> getInventorytransactionhistory(@RequestBody ElnresultUsedMaterial resultusedmaterial) {
		return materialInventoryService.getInventorytransactionhistory(resultusedmaterial);
	}
	
	@PostMapping("/uploadinvimages")
	public Map<String, Object> uploadInvimages(@RequestParam("file") MultipartFile file,
			@RequestParam("originurl") String originurl, @RequestParam("username") String username,
			@RequestParam("sitecode") String sitecode,@RequestParam("selectedMaterial") Integer nmaterialcatcode,
			@RequestParam("usercode") Integer usercode,@RequestParam("smiles") String smiles
			,@RequestParam("moljson") String moljson)throws Exception {
		return materialInventoryService.uploadInvimages(file, originurl, username, sitecode,nmaterialcatcode,usercode,smiles,moljson);
	}
	
	@PostMapping("/updateinvimages")
	public Map<String, Object> updateinvimages(@RequestParam("file") MultipartFile file,
			@RequestParam("fileid") String fileid, @RequestParam("username") String username,
			@RequestParam("sitecode") String sitecode,@RequestParam("selectedMaterial") Integer nmaterialcatcode,
			@RequestParam("usercode") Integer usercode,@RequestParam("smiles") String smiles
			,@RequestParam("moljson") String moljson)throws Exception {
		return materialInventoryService.updateinvimages(file, fileid, username, sitecode,nmaterialcatcode,usercode,smiles,moljson);
	}
	
	@RequestMapping(value = "downloadinvimagesFileDTO")
	public List<FileDTO> downloadinvimagesFileDTO(@RequestBody Map<String, Object> inputMap)
			throws IllegalStateException, IOException {
		
		String tenant = inputMap.get("tenant").toString();
		Integer nmaterialcode = Integer.parseInt(inputMap.get("nmaterialcode").toString());
		
		List<FileDTO> returnObj = materialInventoryService.downloadinvimagesFileDTO(tenant,nmaterialcode);

		return returnObj;
	}
	
	@PostMapping("/deleteinvimages")
	public void deleteinvimages(@RequestBody Map<String, Object> inputMap)throws Exception {
		String fileName = inputMap.get("fileName").toString();
		materialInventoryService.deleteinvimages(fileName);
	}
	
	@PostMapping("/uploadInvimagesSql")
	public Map<String, Object> uploadInvimagesSql(@RequestParam("file") MultipartFile file,
			@RequestParam("originurl") String originurl, @RequestParam("username") String username,
			@RequestParam("sitecode") String sitecode,@RequestParam("selectedMaterial") Integer nmaterialcatcode,
			@RequestParam("usercode") Integer usercode,@RequestParam("smiles") String smiles
			,@RequestParam("moljson") String moljson) throws IOException {
		return materialInventoryService.uploadInvimagesSql(file, originurl, username, sitecode,nmaterialcatcode,usercode,smiles,moljson);
	}
	
	@PostMapping("/updateinvimagesSql")
	public Map<String, Object> updateinvimagesSql(@RequestParam("file") MultipartFile file,
			@RequestParam("fileid") String fileid, @RequestParam("username") String username,
			@RequestParam("sitecode") String sitecode,@RequestParam("selectedMaterial") Integer nmaterialcatcode,
			@RequestParam("usercode") Integer usercode,@RequestParam("smiles") String smiles
			,@RequestParam("moljson") String moljson)throws Exception {
		return materialInventoryService.updateinvimagesSql(file, fileid, username, sitecode,nmaterialcatcode,usercode,smiles,moljson);
	}
	
	@RequestMapping(value = "downloadinvimagesSQLFileDTO")
	public List<FileDTO> downloadinvimagesSQLFileDTO(@RequestBody Map<String, Object> inputMap) throws IllegalStateException, IOException {

		Integer nmaterialcode = Integer.parseInt(inputMap.get("nmaterialcode").toString());
		
		List<FileDTO> returnObj = materialInventoryService.downloadinvimagesSQLFileDTO(nmaterialcode);
		
		return returnObj;
	}
	
	@PostMapping("/deleteinvimagesSQL")
	public void deleteinvimagesSQL(@RequestBody Map<String, Object> inputMap)throws Exception {
		String fileName = inputMap.get("fileName").toString();
		materialInventoryService.deleteinvimagesSQL(fileName);
	}
	@PostMapping("/insertLinkforInvertory")
	public ResponseEntity<Object> insertLinkforInvertory(@RequestBody MaterilaInventoryLinks objInv) throws Exception {
		return materialInventoryService.insertLinkforInvertory(objInv);
	}

	@PostMapping("/getLinksForInventory")
	public ResponseEntity<Object> getLinksForInventory(@RequestBody MaterilaInventoryLinks objInv) throws Exception {
		return materialInventoryService.getLinksforInvertory(objInv);
	}
	
	@PostMapping("/deleteLinkforInventory")
	public ResponseEntity<Object> deleteLinkforInvertory(@RequestBody MaterilaInventoryLinks objInv) throws Exception {
		return materialInventoryService.deleteLinkforInvertory(objInv);
	}
	
	@RequestMapping(value = "/getElnMaterialInventoryCount", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public long getElnMaterialInventoryCount(@RequestBody LSSiteMaster inputMap) throws Exception {
		
		return  materialInventoryService.getElnMaterialInventoryCount(inputMap);
	}
	
	@RequestMapping(value = "/getElnMateriallInventoryByFilter", method = RequestMethod.POST)
	public ResponseEntity<Object> getElnMateriallInventoryByFilter(@RequestBody Map<String, Object> inputMap) throws Exception {
		return materialInventoryService.getElnMateriallInventoryByFilter(inputMap);
	}
}