package com.agaram.eln.primary.controller.sample;

import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
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

import com.agaram.eln.primary.model.material.ElnmaterialInventory;
import com.agaram.eln.primary.model.sample.Sample;
import com.agaram.eln.primary.model.sample.SampleCategory;
import com.agaram.eln.primary.model.sample.SampleLinks;
import com.agaram.eln.primary.model.sample.SampleProjectHistory;
import com.agaram.eln.primary.service.sample.SampleService;

@RestController
@RequestMapping("/sample")
public class SampleController {
	
	@Autowired
	private SampleService objSampleService;
	private Map inputMap;
	
	@RequestMapping(value = "/getSampleonCategory", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getSampleonCategory(@RequestBody SampleCategory objsamplecat) throws Exception {

		return (ResponseEntity<Object>) objSampleService.getSampleonCategory(objsamplecat);
	}
	
	@RequestMapping(value = "/getSampleonSite", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getSampleonSite(@RequestBody Sample objsample) throws Exception {

		return (ResponseEntity<Object>) objSampleService.getSampleonSite(objsample);
	}
	
	@RequestMapping(value = "/createSample", method = RequestMethod.POST)
	public ResponseEntity<Object> createSample(@RequestBody Sample obj) throws Exception {

		return objSampleService.createSample(obj);
	}

	@RequestMapping(value = "/updateSample", method = RequestMethod.POST)
	public ResponseEntity<Object> updateSample(@RequestBody Sample obj) throws Exception {

		return objSampleService.updateSample(obj);
	}
	
	@RequestMapping(value = "/getchildsample", method = RequestMethod.POST)
	public ResponseEntity<Object> getchildsample(@RequestBody Sample[] obj) throws Exception {
		return objSampleService.getchildsample(Arrays.asList(obj));
	}

	@RequestMapping(value = "/getSampleAttachments", method = RequestMethod.POST)
	public Map<String, Object> getSampleAttachments(@RequestBody Map<String, Object> inputMap) throws Exception {
		return objSampleService.getSampleAttachments(inputMap);
	}
	@PostMapping("/getLinksOnSample")
	public ResponseEntity<Object> getLinksOnSample(@RequestBody SampleLinks sampleLinks)throws Exception
	{
		return objSampleService.getLinksOnSample(sampleLinks);
	}
	@PostMapping("/sampleCloudUploadattachments")
	public Sample sampleCloudUploadattachments(@RequestParam("file") MultipartFile file,
			@RequestParam("nsampletypecode") Integer nsampletypecode,
			@RequestParam("nsamplecatcode") Integer nsamplecatcode,
			@RequestParam("samplecode") Integer samplecode, @RequestParam("filename") String filename,
			@RequestParam("fileexe") String fileexe, @RequestParam("usercode") Integer usercode,
			@RequestParam("date") Date currentdate, @RequestParam("isMultitenant") Integer isMultitenant,@RequestParam("nsitecode") Integer nsitecode)
			throws IOException {
		return objSampleService.sampleCloudUploadattachments(file, nsampletypecode, nsamplecatcode,
				samplecode, filename, fileexe, usercode, currentdate, isMultitenant,nsitecode);
	}
	@PostMapping("/uploadLinkforSample")
	public ResponseEntity<Object> uploadLinkforSample(@RequestBody SampleLinks SampleLinks) throws ParseException
	{
		return objSampleService.uploadLinkforSample(SampleLinks);
	}
	
	@PostMapping("/deleteLinkforSample")
	public ResponseEntity<Object> deleteLinkforSample(@RequestBody SampleLinks SampleLinks)
	{
		return objSampleService.deleteLinkforSample(SampleLinks);
	}
	@PostMapping("/updateAssignedProjectOnSample")
	public void updateAssignedProjectOnSample(@RequestBody Map<String, Object> inputMap)throws Exception
	{
		objSampleService.updateAssignedProjectOnSample(inputMap);
	}
	@PostMapping("/getAssignedTaskOnSample")
	public ResponseEntity<Object> getAssignedTaskOnSample(@RequestBody Map<String, Object> inputMap)throws Exception
	{
		return objSampleService.getAssignedTaskOnSample(inputMap);
	}
	
	@PostMapping("/updatemsampleprojecthistory")
	public ResponseEntity<Object> updatemsampleprojecthistory(@RequestBody SampleProjectHistory[] samplelist)throws Exception
	{
		return objSampleService.updatemsampleprojecthistory(samplelist);
	}
	@RequestMapping(value = "/getSampleProps", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getSampleProps(@RequestBody Map<String, Object> inputMap) throws Exception {

		Integer nsiteInteger = (Integer) inputMap.get("nsitecode");
		
		return (ResponseEntity<Object>) objSampleService.getSampleProps(nsiteInteger);
	}
	
	@RequestMapping(value = "/getSampleCategoryByIdBarCodeFilter", method = RequestMethod.POST)
	public ResponseEntity<Object> getSampleCategoryByIdBarCodeFilter(@RequestBody Map<String, Object> inputMap)
			throws Exception {
		return objSampleService.getSampleCategoryByIdBarCodeFilter(inputMap);
	}
	
	@RequestMapping(value = "/updateSampleExpiry", method = RequestMethod.POST)
	public ResponseEntity<Object> updateSampleExpiry(@RequestBody Sample objSample) throws Exception {
		return objSampleService.updateSampleExpiry(objSample);
	}
}
