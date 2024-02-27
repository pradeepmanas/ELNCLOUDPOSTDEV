package com.agaram.eln.primary.service.usermanagement;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.agaram.eln.primary.commonfunction.commonfunction;
import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.usermanagement.LSMultisites;
import com.agaram.eln.primary.model.usermanagement.LSMultiusergroup;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.model.usermanagement.LSusergroup;
import com.agaram.eln.primary.model.usermanagement.Lsusersettings;
import com.agaram.eln.primary.repository.usermanagement.LSMultisitesRepositery;
import com.agaram.eln.primary.repository.usermanagement.LSMultiusergroupRepositery;
import com.agaram.eln.primary.repository.usermanagement.LSSiteMasterRepository;
import com.agaram.eln.primary.repository.usermanagement.LSuserMasterRepository;
import com.agaram.eln.primary.repository.usermanagement.LsusersettingsRepository;

@Service
public class FreeUserService {
	
	@Autowired
	private LSuserMasterRepository lsuserMasterRepository;
	
	@Autowired
	private LSSiteMasterRepository lSSiteMasterRepository;
	
	@Autowired
	private LsusersettingsRepository LsusersettingsRepository;
	
	@Autowired
    private UserService userService;
	
	@Autowired
	private LSMultisitesRepositery lsmultisitesrepositery;
	
	@Autowired
	private LSMultiusergroupRepositery lsmultiusergroupRepositery;
	
	public LSuserMaster Createuser( LSuserMaster objuser) throws Exception {
		Long usercount = lsuserMasterRepository.countByUsernameAndAutenticatefrom(objuser.getUsername(),objuser.getAutenticatefrom());
		if(usercount <=0)
		{
			LSSiteMaster site = lSSiteMasterRepository.save(objuser.getLssitemaster());
			
			
			
			String unifieduser = objuser.getUsername().toLowerCase().replaceAll("[^a-zA-Z0-9]", "") + "u"
					+ objuser.getUsercode() + "s" + objuser.getLssitemaster().getSitecode() + objuser.getUnifieduserid();

			objuser.setUnifieduserid(unifieduser);
			try {
				objuser.setCreateddate(commonfunction.getCurrentUtcTime());
				objuser.setModifieddate(commonfunction.getCurrentUtcTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(objuser.getAutenticatefrom()!=0)
			{
				objuser.setPassword(objuser.getSubcode());
			}
			
			lsuserMasterRepository.save(objuser);
			
			LSMultisites lmultisites = new LSMultisites();
			lmultisites.setDefaultsiteMaster(objuser.getLssitemaster().getSitecode());
			lmultisites.setLssiteMaster(objuser.getLssitemaster());
			lmultisites.setUsercode(objuser.getUsercode());
			
			lsmultisitesrepositery.save(lmultisites);
			
			LSMultiusergroup lsmultiusergroup = new LSMultiusergroup();
			lsmultiusergroup.setDefaultusergroup(1);
			lsmultiusergroup.setUsercode(objuser.getUsercode());
			LSusergroup objusergroup =  new LSusergroup();
			objusergroup.setUsergroupcode(1);
			lsmultiusergroup.setLsusergroup(objusergroup);
			
			lsmultiusergroupRepositery.save(lsmultiusergroup);
			
			Response objResponse = new Response();
			objResponse.setStatus(true);
			objResponse.setInformation("User registered successfully");
			objuser.setResponse(objResponse);
			
			Lsusersettings getUserPreference = new Lsusersettings();
			getUserPreference.setDFormat("Mon-DD-YYYY HH:mm:ss");
			getUserPreference.setUsercode(objuser.getUsercode());
			LsusersettingsRepository.save(getUserPreference);
			
			if(objuser.getAutenticatefrom()==0)
			{
				userService.Usersendpasswormail(objuser);
			}
		}
		else
		{
			Response objResponse = new Response();
			objResponse.setStatus(false);
			objResponse.setInformation("User exists");
			objuser.setResponse(objResponse);
		}
		return objuser;
	}
	
	public Map<String, Object> Loginfreeuser(@RequestBody LSuserMaster objuser) throws Exception {
		Map<String, Object> loginobject = new HashMap<>();
		Long usercount = lsuserMasterRepository.countByUsernameAndAutenticatefrom(objuser.getUsername(),objuser.getAutenticatefrom());
		if(usercount >0)
		{
			objuser = lsuserMasterRepository.findByUsernameAndAutenticatefrom(objuser.getUsername(),objuser.getAutenticatefrom());
			Response objResponse = new Response();
			objResponse.setStatus(true);
			objResponse.setInformation("User exists");
			objuser.setObjResponse(objResponse);
		}
		else
		{
			Response objResponse = new Response();
			objResponse.setStatus(false);
			objResponse.setInformation("User not exists");
			objuser.setObjResponse(objResponse);
		}
		loginobject.put("user", objuser);
		loginobject.put("Logintime", commonfunction.getCurrentUtcTime());
		return loginobject;
	}
}
