package com.agaram.eln.primary.controller.basemaster;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.agaram.eln.primary.fetchmodel.getmasters.Projectmaster;
import com.agaram.eln.primary.fetchmodel.getmasters.Samplemaster;
import com.agaram.eln.primary.fetchmodel.getmasters.Testmaster;
import com.agaram.eln.primary.model.instrumentDetails.Lselninstrumentmaster;
import com.agaram.eln.primary.model.sheetManipulation.LSsamplemaster;
import com.agaram.eln.primary.model.sheetManipulation.LStestmaster;
import com.agaram.eln.primary.model.sheetManipulation.LStestmasterlocal;
import com.agaram.eln.primary.model.usermanagement.LSprojectmaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.service.basemaster.BaseMasterService;

@RestController
@RequestMapping(value = "/Basemaster", method = RequestMethod.POST)
public class BaseMasterController {
	
	@Autowired
    private BaseMasterService masterService;
	
	/**
	 *For Get Masters
	 * 
	 * @param objMap
	 * @return List<class>
	 */
	
	@RequestMapping("/getTestmaster")
	public List<Testmaster> getTestmaster(@RequestBody LSuserMaster objClass) {
		return masterService.getTestmaster(objClass);
	}
	
	@RequestMapping("/getTestwithsheet")
	public Map<String, Object> getTestwithsheet(@RequestBody LSuserMaster objClass) {
		return masterService.getTestwithsheet(objClass);
	}
	
	@RequestMapping("/getLimsTestMaster")
	public List<LStestmaster> getLimsTestMaster(@RequestBody LSuserMaster objClass) {
		return masterService.getLimsTestMaster(objClass);
	}
	
	@RequestMapping("/getSamplemaster")
	public List<Samplemaster> getsamplemaster(@RequestBody LSuserMaster objClass) {
		return masterService.getsamplemaster(objClass);
	}
	
	@RequestMapping("/getProjectmaster")
	public List<Projectmaster> getProjectmaster(@RequestBody LSuserMaster objClass) {
		return masterService.getProjectmaster(objClass);
	}
	
	/**
	 *For Insert/Update/Delete Masters
	 * 
	 * @param Class
	 * @return List<class>
	 */
	
	@PostMapping("/InsertupdateTest")
	public LStestmasterlocal InsertupdateTest(@RequestBody LStestmasterlocal objClass)
	{
		return masterService.InsertupdateTest(objClass);
	}
	
	@PostMapping("/InsertupdateSample")
	public LSsamplemaster InsertupdateSample(@RequestBody LSsamplemaster objClass)
	{
		return masterService.InsertupdateSample(objClass);
	}
	
	@PostMapping("/InsertupdateProject")
	public LSprojectmaster InsertupdateProject(@RequestBody LSprojectmaster objClass)
	{
		return masterService.InsertupdateProject(objClass);
	}
	
	@PostMapping("/GetMastersforTestMaster")
	public Map<String, Object> GetMastersforTestMaster(@RequestBody LSuserMaster objuser)
	{
		return masterService.GetMastersforTestMaster(objuser);
	}
	
	@PostMapping("/InsertupdateInstrument")
	public Lselninstrumentmaster InsertupdateInstrument(@RequestBody Lselninstrumentmaster objClass) {
               
		return masterService.InsertupdateInstrument(objClass);
	}
	
	@PostMapping("/GetInstrument")
	public Map<String, Object> GetInstrument(@RequestBody Lselninstrumentmaster objClass) {
		return masterService.GetInstrument(objClass);
	}
	
	@PostMapping("/GetTestonID")
	public LStestmaster GetTestonID(@RequestBody LStestmaster objtest)
	{
		return masterService.GetTestonID(objtest);
	}
}