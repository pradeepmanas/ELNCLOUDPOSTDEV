package com.agaram.eln.primary.controller.material;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.agaram.eln.primary.model.material.Elnmaterial;
import com.agaram.eln.primary.model.material.ElnresultUsedMaterial;
import com.agaram.eln.primary.model.material.Material;
import com.agaram.eln.primary.model.material.MaterialLinks;
import com.agaram.eln.primary.model.material.MaterialProjectHistory;
import com.agaram.eln.primary.model.sample.ElnresultUsedSample;
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
	
	@RequestMapping(value = "/getMaterialonCategory", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getMaterialonCategory(@RequestBody Map<String, Object> inputMap) throws Exception {

		return (ResponseEntity<Object>) objMaterialService.getMaterialonCategory(inputMap);
	}
	
	@RequestMapping(value = "/getMaterialDesign", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getMaterialDesign(@RequestBody Map<String, Object> inputMap) throws Exception {

		Integer ntypecode = (Integer) inputMap.get("nmaterialcode");
		
		return (ResponseEntity<Object>) objMaterialService.getMaterialDesign(ntypecode);
	}	
	
	@RequestMapping(value = "/getMaterialTypeDesign", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getMaterialTypeDesign(@RequestBody Map<String, Object> inputMap) throws Exception {

		Integer ntypecode = (Integer) inputMap.get("ntypecode");
		
		return (ResponseEntity<Object>) objMaterialService.getMaterialTypeDesign(ntypecode);
	}	
	
	@RequestMapping(value = "/getMaterialcombo", method = RequestMethod.POST)
	public ResponseEntity<Object> getMaterialcombo(@RequestBody Map<String, Object> inputMap) throws Exception {
		Integer nmaterialtypecode = (Integer) inputMap.get("nmaterialtypecode");
		Integer nsiteInteger = (Integer) inputMap.get("nsitecode");
		return (ResponseEntity<Object>) objMaterialService.getMaterialcombo(nmaterialtypecode,nsiteInteger);
	}

	@RequestMapping(value = "/getMaterialByTypeCode", method = RequestMethod.POST)
	public ResponseEntity<Object> getMaterialByTypeCode(@RequestBody Map<String, Object> inputMap) throws Exception {

		return objMaterialService.getMaterialByTypeCode(inputMap);
	}
	
	@RequestMapping(value = "/getMaterialBySearchField", method = RequestMethod.POST)
	public ResponseEntity<Object> getMaterialBySearchField(@RequestBody Map<String, Object> inputMap) throws Exception {

		return objMaterialService.getMaterialBySearchField(inputMap);
	}
	
	@RequestMapping(value = "/getMaterialByTypeCodeByDate", method = RequestMethod.POST)
	public ResponseEntity<Object> getMaterialByTypeCodeByDate(@RequestBody Map<String, Object> inputMap) throws Exception {

		return objMaterialService.getMaterialByTypeCodeByDate(inputMap);
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
	public Elnmaterial CloudUploadattachments(@RequestParam("file") MultipartFile file,
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
	
	/**
	 * 
	 * done material by Sathishkumar chandrasekar 15-10-2023
	 * 
	 * 
	 */
	
	@RequestMapping(value = "/getElnMaterial", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getElnMaterial(@RequestBody Map<String, Object> inputMap) throws Exception {
		
		return (ResponseEntity<Object>) objMaterialService.getElnMaterial(inputMap);
	}
	
	@RequestMapping(value = "/getElnMaterialByFilter", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getElnMaterialByFilter(@RequestBody Map<String, Object> inputMap) throws Exception {
		
		return (ResponseEntity<Object>) objMaterialService.getElnMaterialByFilter(inputMap);
	}
	
	@RequestMapping(value = "/getELNMaterialPropsForFilter", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getELNMaterialPropsForFilter(@RequestBody Map<String, Object> inputMap) throws Exception {

		Integer nsiteInteger = (Integer) inputMap.get("nsitecode");
		
		return (ResponseEntity<Object>) objMaterialService.getELNMaterialPropsForFilter(nsiteInteger);
	}
	
	@RequestMapping(value = "/getELNPropsAll", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getELNPropsAll(@RequestBody Map<String, Object> inputMap) throws Exception {

		Integer nsiteInteger = (Integer) inputMap.get("nsitecode");
		
		return (ResponseEntity<Object>) objMaterialService.getELNPropsAll(nsiteInteger);
	}
	
	@RequestMapping(value = "/getELNMaterialProps", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getMaterialProps(@RequestBody Map<String, Object> inputMap) throws Exception {

		Integer nsiteInteger = (Integer) inputMap.get("nsitecode");
		
		return (ResponseEntity<Object>) objMaterialService.getMaterialProps(nsiteInteger);
	}
	
	@RequestMapping(value = "/getELNMaterialTypeBasedCat", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getMaterialTypeBasedCat(@RequestBody Map<String, Object> inputMap) throws Exception {
		
		return (ResponseEntity<Object>) objMaterialService.getMaterialTypeBasedCat(inputMap);
	}
	
	@RequestMapping(value = "/createElnMaterial", method = RequestMethod.POST)
	public ResponseEntity<Object> createElnMaterial(@RequestBody Elnmaterial obj) throws Exception {

		return objMaterialService.createElnMaterial(obj);
	}
	
	@RequestMapping(value = "/updateElnMaterial", method = RequestMethod.POST)
	public ResponseEntity<Object> updateElnMaterial(@RequestBody Elnmaterial obj) throws Exception {

		return objMaterialService.updateElnMaterial(obj);
	}
	
	@RequestMapping(value = "/getELNMaterialBySearchField", method = RequestMethod.POST)
	public ResponseEntity<Object> getELNMaterialBySearchField(@RequestBody Map<String, Object> inputMap) throws Exception {

		return objMaterialService.getELNMaterialBySearchField(inputMap);
	}
	
	@RequestMapping(value = "/getElnMaterialOnProtocol", method = RequestMethod.POST)
	public ResponseEntity<Object> getElnMaterialOnProtocol(@RequestBody ElnresultUsedMaterial inputMap) throws Exception {

		return objMaterialService.getElnMaterialOnProtocol(inputMap);
	}
	
	@RequestMapping(value = "/geMaterialtAttachments", method = RequestMethod.POST)
	public Map<String, Object> geMaterialtAttachments(@RequestBody Map<String, Object> inputMap) throws Exception {
		return objMaterialService.geMaterialtAttachments(inputMap);
	}

	@PostMapping("/materialCloudUploadattachments")
	public Elnmaterial materialCloudUploadattachments(@RequestParam("file") MultipartFile file,
			@RequestParam("nmaterialtypecode") Integer nmaterialtypecode,
			@RequestParam("nmaterialcatcode") Integer nmaterialcatcode,
			@RequestParam("nmaterialcode") Integer nmaterialcode, @RequestParam("filename") String filename,
			@RequestParam("fileexe") String fileexe, @RequestParam("usercode") Integer usercode,
			@RequestParam("date") Date currentdate, @RequestParam("isMultitenant") Integer isMultitenant,@RequestParam("nsitecode") Integer nsitecode)
			throws IOException {
		return objMaterialService.materialCloudUploadattachments(file, nmaterialtypecode, nmaterialcatcode,
				nmaterialcode, filename, fileexe, usercode, currentdate, isMultitenant,nsitecode);
	}

	@RequestMapping(path = "/materialView/{param}/{fileid}", method = RequestMethod.GET)
	public ResponseEntity<InputStreamResource> materialView(@PathVariable String param, @PathVariable String fileid)
			throws IOException {
		return objMaterialService.materialView(param, fileid);
	}
	
	@PostMapping("/updateAssignedProjectOnMaterial")
	public void updateAssignedTaskOnMaterial(@RequestBody Map<String, Object> inputMap)throws Exception
	{
		objMaterialService.updateAssignedTaskOnMaterial(inputMap);
	}
	
	@PostMapping("/getAssignedProjectOnMaterial")
	public ResponseEntity<Object> getAssignedTaskOnMaterial(@RequestBody Map<String, Object> inputMap)throws Exception
	{
		return objMaterialService.getAssignedTaskOnMaterial(inputMap);
	}
	
	@PostMapping("/uploadLinkforMaterial")
	public ResponseEntity<Object> uploadLinkforMaterial(@RequestBody MaterialLinks materiallink)throws Exception
	{
		return objMaterialService.uploadLinkforMaterial(materiallink);
	}
	
	@PostMapping("/getLinksOnMaterial")
	public ResponseEntity<Object> getLinksOnMaterial(@RequestBody MaterialLinks materiallink)throws Exception
	{
		return objMaterialService.getLinksOnMaterial(materiallink);
	}
	
	@PostMapping("/deleteLinkforMaterial")
	public ResponseEntity<Object> deleteLinkforMaterial(@RequestBody MaterialLinks materiallink)throws Exception
	{
		return objMaterialService.deleteLinkforMaterial(materiallink);
	}
	
	@PostMapping("/updatematerialprojecthistory")
	public ResponseEntity<Object> updatematerialprojecthistory(@RequestBody MaterialProjectHistory[] materiallist)throws Exception
	{
		return objMaterialService.updatematerialprojecthistory(materiallist);
	}
	@RequestMapping(value = "/geSampleList", method = RequestMethod.POST)
	public ResponseEntity<Object> geSampleList(@RequestBody ElnresultUsedSample inputMap) throws Exception {
		return objMaterialService.getSampleList(inputMap);
	}
}
