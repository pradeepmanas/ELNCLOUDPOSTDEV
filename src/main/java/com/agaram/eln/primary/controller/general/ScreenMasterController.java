package com.agaram.eln.primary.controller.general;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.agaram.eln.primary.model.general.ScreenMaster;
import com.agaram.eln.primary.service.general.ScreenMasterService;

@RestController
@RequestMapping(value = "/general", method = RequestMethod.POST)
public class ScreenMasterController {
	
	@Autowired
	private ScreenMasterService screenmasterservice;
	
	@PostMapping("/GetScreens")
	public List<ScreenMaster> GetScreens()
	{
		return screenmasterservice.GetScreens();
	}
}
