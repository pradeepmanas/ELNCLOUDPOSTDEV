package com.agaram.eln.primary.service.general;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.agaram.eln.primary.model.general.ScreenMaster;
import com.agaram.eln.primary.repository.general.ScreenMasterRespository;

@Service
public class ScreenMasterService {
	@Autowired
	private ScreenMasterRespository screenmasterrespository;
	
	public List<ScreenMaster> GetScreens()
	{
		List<ScreenMaster> lstscreens = new ArrayList<ScreenMaster>();
		lstscreens =screenmasterrespository.findAll();
		return lstscreens;
	}
}
