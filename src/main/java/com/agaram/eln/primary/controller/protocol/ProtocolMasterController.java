package com.agaram.eln.primary.controller.protocol;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.agaram.eln.primary.model.protocols.LSlogilabprotocoldetail;
import com.agaram.eln.primary.service.protocol.ProtocolMasterService;

@RestController
@RequestMapping(value="/protocolMaster", method=RequestMethod.POST)
public class ProtocolMasterController {
	
	@Autowired
	ProtocolMasterService ProtocolMasterService;
	
	@RequestMapping(value="/addProtocolMasterDetail")
	protected Map<String, Object> addProtocolMasterDetail(@RequestBody LSlogilabprotocoldetail LSlogilabprotocoldetail){
		
		return ProtocolMasterService.addProtocolMasterDetail(LSlogilabprotocoldetail);
		
	}

}
