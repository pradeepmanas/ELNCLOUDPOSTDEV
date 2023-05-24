package com.agaram.eln.primary.controller.instrumentsetup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.agaram.eln.primary.service.instrumentsetup.FileSettingsService;

/**
 * @author ATE153
 * @version 1.0.0
 * @since   05- Dec- 2019
 */
@RestController
public class FileSettingsController {
	
	@Autowired
	FileSettingsService fileService;
	
//	 /**
//     * This method is used to retrieve list of FileSettings
//     * @return list of FileSettings.
//     */
//    @PostMapping(value = "/getFileSettings")
//    public List<FileSettings> getFileSettings() {
//        return  fileService.getFileSettings();
//    }
	
//	/**
//     * This method is used to add new entry of instrument master to file settings.
//     * @param fileSettings FileSettings -  holding details of all fields in 'FileSettings' Pojo.
//     * @return newly added file settings for instrument master
//     */
//    @PostMapping(value = "/createFileSettings")
//    public ResponseEntity<Object> createFileSettings(//@PathVariable(value = "instmastkey") Integer instMastKey,    		
////    		@Valid @RequestBody FileSettings fileSettings) {
////        return  fileService.createFileSettings(fileSettings);
//    		@Valid @RequestBody Map<String, Object> mapObject){    	
//    		  final ObjectMapper mapper = new ObjectMapper();		
//			  final FileSettings fileSettings = mapper.convertValue(mapObject.get("fileSettings"), FileSettings.class);
//			  final Boolean saveAuditTrail = mapper.convertValue(mapObject.get("saveAuditTrail"), Boolean.class);
//			  final Page page = mapper.convertValue(mapObject.get("modulePage"), Page.class);
//			  
//			  return fileService.createFileSettings(fileSettings, saveAuditTrail, page);
//    }

//    /**
//     * This method is used to update the selected 'file settings' details based on provided 
//     * primary key id and details of the  updated fields.
//     * @param id 'file settings' primary code to update
//     * @param filesettings Object with details to update
//     * @return updated 'filesettings' object
//     */
//    @PostMapping(value = "/updateFileSettings/{filemastkey}/{instmastkey}")
//    public ResponseEntity<Object> updateFileSettings(@PathVariable(value = "filemastkey") Integer fileMastKey,
//    		@PathVariable(value = "instmastkey") Integer instMastKey, 		
//    		@Valid @RequestBody FileSettings fileSettings) {
//        return  fileService.updateFileSettings(fileSettings, fileMastKey, instMastKey);
//    }   

}
