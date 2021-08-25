package com.agaram.eln.primary.controller.masters;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.helpdocument.Helptittle;
import com.agaram.eln.primary.model.instrumentDetails.LsOrderSampleUpdate;
import com.agaram.eln.primary.model.masters.Lsrepositories;
import com.agaram.eln.primary.model.masters.Lsrepositoriesdata;
import com.agaram.eln.primary.service.masters.MasterService;

@RestController
@RequestMapping(value = "/Master", method = RequestMethod.POST)
public class MasterController {
	@Autowired
	MasterService masterService;
	
	@RequestMapping("/Getallrepositories")
	public List<Lsrepositories> Getallrepositories(@RequestBody Lsrepositories lsrepositories)
	{
		return masterService.Getallrepositories(lsrepositories);
	}
	
	@RequestMapping("/Saverepository")
	public Lsrepositories Saverepository(@RequestBody Lsrepositories lsrepositories)
	{
		return masterService.Saverepository(lsrepositories);
	}
	
	@RequestMapping("/Getallrepositoriesdata")
	public List<Lsrepositoriesdata> Getallrepositoriesdata(@RequestBody Lsrepositoriesdata lsrepositoriesdata)
	{
		return masterService.Getallrepositoriesdata(lsrepositoriesdata);
	}
	
	@RequestMapping("/Saverepositorydata")
	public Lsrepositoriesdata Saverepositorydata(@RequestBody Lsrepositoriesdata lsrepositoriesdata)
	{
		return masterService.Saverepositorydata(lsrepositoriesdata);
	}
	
	@RequestMapping("/GetupdatedRepositorydata")
	public Lsrepositoriesdata GetupdatedRepositorydata(@RequestBody Lsrepositoriesdata lsrepositoriesdata)
	{
		return masterService.GetupdatedRepositorydata(lsrepositoriesdata);
	}
	
	@RequestMapping("/DeleteRepositorydata")
	public Lsrepositoriesdata DeleteRepositorydata(@RequestBody Lsrepositoriesdata lsrepositoriesdata)
	{
		return masterService.DeleteRepositorydata(lsrepositoriesdata);
	}
	
	@RequestMapping("/getinventoryhistory")
	public List<LsOrderSampleUpdate> getinventoryhistory(@RequestBody LsOrderSampleUpdate lsinventoryhistory)
	{
		return masterService.getinventoryhistory(lsinventoryhistory);
	}
	
	@RequestMapping("/pushnotificationforinventory")
	public Response pushnotificationforinventory(@RequestBody List<Lsrepositoriesdata>  lsrepositoriesdata)
	{
		return masterService.pushnotificationforinventory(lsrepositoriesdata);
	}
}
