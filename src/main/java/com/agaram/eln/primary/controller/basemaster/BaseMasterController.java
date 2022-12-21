package com.agaram.eln.primary.controller.basemaster;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.agaram.eln.primary.fetchmodel.getmasters.Samplemaster;
import com.agaram.eln.primary.fetchmodel.getmasters.Testmaster;
import com.agaram.eln.primary.model.instrumentDetails.Lselninstrumentmaster;
import com.agaram.eln.primary.model.masters.LSlogbooksampleupdates;
import com.agaram.eln.primary.model.masters.Lsrepositoriesdata;
import com.agaram.eln.primary.model.material.Unit;
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
	public List<Testmaster> getTestmaster(@RequestBody LSuserMaster objClass)throws Exception {
		return masterService.getTestmaster(objClass);
	}
	
	@RequestMapping("/getTestwithsheet")
	public Map<String, Object> getTestwithsheet(@RequestBody LSuserMaster objClass)throws Exception {
		return masterService.getTestwithsheet(objClass);
	}
	
	@RequestMapping("/getLimsTestMaster")
	public List<LStestmaster> getLimsTestMaster(@RequestBody LSuserMaster objClass)throws Exception {
		return masterService.getLimsTestMaster(objClass);
	}
	
	@RequestMapping("/getSamplemaster")
	public List<Samplemaster> getsamplemaster(@RequestBody LSuserMaster objClass)throws Exception {
		return masterService.getsamplemaster(objClass);
	}
	
	@RequestMapping("/getProjectmaster")
	public List<LSprojectmaster> getProjectmaster(@RequestBody LSuserMaster objClass)throws Exception {
		return masterService.getProjectmaster(objClass);
	}

	
	@PostMapping("/InsertupdateTest")
	public LStestmasterlocal InsertupdateTest(@RequestBody LStestmasterlocal objClass)throws Exception
	{
		return masterService.InsertupdateTest(objClass);
	}
	
	@PostMapping("/InsertupdateSample")
	public LSsamplemaster InsertupdateSample(@RequestBody LSsamplemaster objClass)throws Exception
	{
		return masterService.InsertupdateSample(objClass);
	}
	
	@PostMapping("/InsertupdateProject")
	public LSprojectmaster InsertupdateProject(@RequestBody LSprojectmaster objClass)throws Exception
	{
		return masterService.InsertupdateProject(objClass);
	}
	
	@PostMapping("/GetMastersforTestMaster")
	public Map<String, Object> GetMastersforTestMaster(@RequestBody LSuserMaster objuser)throws Exception
	{
		return masterService.GetMastersforTestMaster(objuser);
	}
	
	@PostMapping("/InsertupdateInstrument")
	public Lselninstrumentmaster InsertupdateInstrument(@RequestBody Lselninstrumentmaster objClass)throws Exception {
               
		return masterService.InsertupdateInstrument(objClass);
	}
	
	@PostMapping("/GetInstrument")
	public Map<String, Object> GetInstrument(@RequestBody Lselninstrumentmaster objClass)throws Exception {
		return masterService.GetInstrument(objClass);
	}
	
	@PostMapping("/GetTestonID")
	public LStestmaster GetTestonID(@RequestBody LStestmaster objtest)throws Exception
	{
		return masterService.GetTestonID(objtest);
	}
	
	@RequestMapping("/getUnitmaster")
	public List<Unit> getUnitmaster(@RequestBody LSuserMaster objClass)throws Exception {
		return masterService.getUnitmaster(objClass);
	}
	
	@PostMapping("/InsertupdateUnit")
	public Unit InsertupdateUnit(@RequestBody Unit objClass)throws Exception
	{
		return masterService.InsertupdateUnit(objClass);
	}
	
	@RequestMapping(value = "/getLsrepositoriesLst")
	protected Map<String, Object> getLsrepositoriesLst(@RequestBody Map<String, Object> argMap)throws Exception {
		Map<String, Object> objMap = new HashMap<String, Object>();
		objMap = masterService.getLsrepositoriesLst(argMap);
		return objMap;
	}
	
	@RequestMapping(value = "/getLsrepositoriesDataLst")
	protected Map<String, Object> getLsrepositoriesDataLst(@RequestBody Map<String, Object> argMap)throws Exception {
		Map<String, Object> objMap = new HashMap<String, Object>();
		objMap = masterService.getLsrepositoriesDataLst(argMap);
		return objMap;
	}
	
	@PostMapping("/reducecunsumablefield")
	public List<Lsrepositoriesdata> reducecunsumablefield(@RequestBody Lsrepositoriesdata[] lsrepositoriesdata)throws Exception
	{
		return masterService.reducecunsumablefield(lsrepositoriesdata);
	}
	
	@RequestMapping(value = "/logbooksampleupdates")
	protected Map<String, Object> logbooksampleupdates(@RequestBody LSlogbooksampleupdates lslogbooksampleupdates)throws Exception {

		return masterService.logbooksampleupdates(lslogbooksampleupdates);
	}
	
	
}