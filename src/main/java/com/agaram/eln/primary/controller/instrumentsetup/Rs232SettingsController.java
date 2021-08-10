package com.agaram.eln.primary.controller.instrumentsetup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.agaram.eln.primary.service.instrumentsetup.Rs232SettingsService;

/**
 * @author ATE153
 * @version 1.0.0
 * @since   05- Dec- 2019
 */
@RestController
public class Rs232SettingsController {

	@Autowired
	Rs232SettingsService rs232Service;
	
//	 /**
//     * This method is used to retrieve list of Rs232Settings
//     * @return list of Rs232Settings.
//     */
//    @PostMapping(value = "/getRs232Settings")
//    public List<Rs232Settings> getRs232Settings() {
//        return  rs232Service.getRs232Settings();
//    }
//	
//	/**
//     * This method is used to add new entry of instrument master to Rs232 settings.
//     * @param rs232Settings Rs232Settings -  holding details of all fields in 'Rs232Settings' Pojo.
//     * @return newly added Rs232 settings for instrument master
//     */
//    @PostMapping(value = "/createRs232Settings")
//    public ResponseEntity<Object> createRs232Settings(//@PathVariable(value = "instmastkey") Integer instMastKey,    		
//    		@Valid @RequestBody Rs232Settings rs232Settings) {
//        return  rs232Service.createRs232Settings(rs232Settings);
    		
//    		@Valid @RequestBody Map<String, Object> mapObject){    	
//    		  final ObjectMapper mapper = new ObjectMapper();		
//			  final Rs232Settings rs232Settings = mapper.convertValue(mapObject.get("rs232Settings"), Rs232Settings.class);
//			  final Boolean saveAuditTrail = mapper.convertValue(mapObject.get("saveAuditTrail"), Boolean.class);
//			  final Page page = mapper.convertValue(mapObject.get("modulePage"), Page.class);
//			  
//			  return rs232Service.createRs232Settings(rs232Settings, saveAuditTrail, page);
//    }

//    /**
//     * This method is used to update the selected 'Rs232 settings' details based on provided 
//     * primary key id and details of the  updated fields.
//     * @param id 'Rs232 settings' primary code to update
//     * @param rs232settings Object with details to update
//     * @return updated 'Rs232settings' object
//     */
//    @PostMapping(value = "/updateRs232Settings/{rs232mastkey}/{instmastkey}")
//    public ResponseEntity<Object> updateRs232Settings(@PathVariable(value = "rs232mastkey") Integer rs232MastKey,
//    		@PathVariable(value = "instmastkey") Integer instMastKey, 		
//    		@Valid @RequestBody Rs232Settings rs232Settings) {
//        return  rs232Service.updateRs232Settings(rs232Settings, rs232MastKey, instMastKey);
//    }   
}
