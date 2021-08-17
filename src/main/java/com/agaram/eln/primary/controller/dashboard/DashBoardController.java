package com.agaram.eln.primary.controller.dashboard;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.agaram.eln.primary.model.cfr.LSactivity;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.service.dashboard.DashBoardService;

@RestController
@RequestMapping(value = "/DashBoard", method = RequestMethod.POST)
public class DashBoardController {
	@Autowired
    private DashBoardService dashBoardService;
	
	@PostMapping("/Getdashboarddetails")
	public Map<String, Object> Getdashboarddetails(@RequestBody LSuserMaster objuser)
	{
		if(objuser.getObjuser() != null && objuser.getObjuser().getFromdate() != null 
				&& objuser.getObjuser().getTodate() != null && objuser.getObjuser().getFiltertype() != null)
		{
			return dashBoardService.Getdashboarddetailsonfilters(objuser);
		}
		else
		{
			return dashBoardService.Getdashboarddetails(objuser);
		}
	}
	
	@PostMapping("/GetActivitiesonLazy")
	public List<LSactivity> GetActivitiesonLazy(@RequestBody LSactivity objactivities)
	{
		return dashBoardService.GetActivitiesonLazy(objactivities);
	}

}
