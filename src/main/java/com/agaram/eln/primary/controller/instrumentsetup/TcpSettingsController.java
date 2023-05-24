package com.agaram.eln.primary.controller.instrumentsetup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.agaram.eln.primary.service.instrumentsetup.TcpSettingsService;

/**
 * @author ATE153
 * @version 1.0.0
 * @since   05- Dec- 2019
 */
@RestController
public class TcpSettingsController {
	
	@Autowired
	TcpSettingsService tcpSettingsService;
	
//	 /**
//     * This method is used to retrieve list of TcpSettings
//     * @return list of TcpSettings.
//     */
//    @PostMapping(value = "/getTcpSettings")
//    public List<TcpSettings> getTcpSettings() {
//        return  tcpSettingsService.getTcpSettings();
//    }
	
//	/**
//     * This method is used to add new entry of instrument master to Tcp settings.
//     * @param tcpSettings TcpSettings -  holding details of all fields in 'TcpSettings' Pojo.
//     * @return newly added Tcp settings for instrument master
//     */
//    @PostMapping(value = "/createTcpSettings")
//    public ResponseEntity<Object> createTcpSettings(//@PathVariable(value = "instmastkey") Integer instMastKey,    		
//    		@Valid @RequestBody TcpSettings tcpSettings) {
//        return  tcpSettingsService.createTcpSettings(tcpSettings);
//    		
////    		@Valid @RequestBody Map<String, Object> mapObject){    	
////		  final ObjectMapper mapper = new ObjectMapper();		
////		  final TcpSettings tcpSettings = mapper.convertValue(mapObject.get("tcpSettings"), TcpSettings.class);
////		  final Boolean saveAuditTrail = mapper.convertValue(mapObject.get("saveAuditTrail"), Boolean.class);
////		  final Page page = mapper.convertValue(mapObject.get("modulePage"), Page.class);
////		  
////		  return tcpSettingsService.createTcpSettings(tcpSettings, saveAuditTrail, page);
//    }

//    /**
//     * This method is used to update the selected 'Tcp settings' details based on provided 
//     * primary key id and details of the  updated fields.
//     * @param id 'Tcp settings' primary code to update
//     * @param tcpsettings Object with details to update
//     * @return updated 'Rs232settings' object
//     */
//    @PostMapping(value = "/updateTcpSettings/{tcpmastkey}/{instmastkey}")
//    public ResponseEntity<Object> updateTcpSettings(@PathVariable(value = "tcpmastkey") Integer tcpMastKey,
//    		@PathVariable(value = "instmastkey") Integer instMastKey, 		
//    		@Valid @RequestBody TcpSettings tcpSettings) {
//        return  tcpSettingsService.updateTcpSettings(tcpSettings, tcpMastKey, instMastKey);
//    }   

}
