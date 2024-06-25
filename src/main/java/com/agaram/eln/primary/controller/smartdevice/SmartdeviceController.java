package com.agaram.eln.primary.controller.smartdevice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.agaram.eln.primary.model.Smartdevice;
import com.agaram.eln.primary.model.sheetManipulation.LSfile;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.service.smartdevice.SmartdeviceService;

@RestController
@RequestMapping(value = "/smartdevice")
public class SmartdeviceController {
	
	@Autowired
	private SmartdeviceService smartdeviceService;

	@PostMapping("/Getdata")
	public List<LSSiteMaster> Getdata(@RequestBody Smartdevice smartdevice)
	{
		return smartdeviceService.Getdata(smartdevice);
	}
}
