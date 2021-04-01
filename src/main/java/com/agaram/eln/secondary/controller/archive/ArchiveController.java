package com.agaram.eln.secondary.controller.archive;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.model.usermanagement.LoggedUser;
import com.agaram.eln.primary.service.cfr.AuditService;
import com.agaram.eln.secondary.model.archive.LScfrArchiveHistory;
import com.agaram.eln.secondary.model.archive.LScfrachivetransaction;
import com.agaram.eln.secondary.service.archive.ArchiveService;

@RestController
@RequestMapping(value = "/archive", method = RequestMethod.POST)
public class ArchiveController {
	@Autowired
    private ArchiveService archiveService;
	
	@Autowired
	private AuditService auditService;
	
	@RequestMapping("/CreateArchive")
	public Map<String, Object> CreateArchive(@RequestBody Map<String, Object> objreqmap)
	{
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> objresmap = new HashMap<String, Object>();
		LScfrArchiveHistory objcfrArchiveHistory = new LScfrArchiveHistory();
		LoggedUser objuser = new LoggedUser();
		Response objResponse = new Response();
		if(objreqmap.containsKey("transaction"))
		{
			objcfrArchiveHistory = mapper.convertValue(objreqmap.get("transaction"),LScfrArchiveHistory.class);
		}
		
		if(objreqmap.containsKey("objuser"))
		{
			objuser = mapper.convertValue(objreqmap.get("objuser"),LoggedUser.class);
		}
		
			if(objuser.getsUsername() != null) {
				
				LSuserMaster userClass = auditService.CheckUserPassWord(objuser);
				
				if(userClass.getObjResponse().getStatus()) {
					
					objresmap.put("transaction", archiveService.CreateArchive(objcfrArchiveHistory));
					objResponse.setStatus(true);
					objresmap.put("objResponse",objResponse);
					
					return objresmap;
				}
				else
				{
					objcfrArchiveHistory.getObjsilentaudit().setComments("Entered invalid username and password");
					Map<String, Object> map=new HashMap<>();
				  	map.put("objsilentaudit",objcfrArchiveHistory.getObjsilentaudit());
				  	map.put("objmanualaudit",objcfrArchiveHistory.getObjmanualaudit());
				    map.put("objUser",objuser);
			     	auditService.AuditConfigurationrecord(map);
					objResponse.setStatus(false);
					objResponse.setInformation("ID_VALIDATION");
					objresmap.put("objResponse",objResponse);
					return objresmap;
				}
				
			}
			objresmap.put("transaction", archiveService.CreateArchive(objcfrArchiveHistory));
			objResponse.setStatus(true);
			objresmap.put("objResponse",objResponse);
		return objresmap;
	}
	
	@RequestMapping("/GetAllArchiveList")
	public List<LScfttransaction> GetAllArchiveList(@RequestBody LSuserMaster objuser)
	{
		return archiveService.GetAllArchiveList(objuser);
	}
	
	@RequestMapping("/GetAllArchiveHistory")
	public List<LScfrArchiveHistory> GetAllArchiveHistory(@RequestBody LSuserMaster objuser)
	{
		return archiveService.GetAllArchiveHistory(objuser);
	}
	
	@RequestMapping("/GetArchiveDataonHistory")
	public List<LScfrachivetransaction> GetArchiveDataonHistory(@RequestBody LScfrArchiveHistory objHistroy)
	{
		return archiveService.GetArchiveDataonHistory(objHistroy);
	}
}
