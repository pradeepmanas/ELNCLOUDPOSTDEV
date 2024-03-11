package com.agaram.eln.primary.controller.usermanagement;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.agaram.eln.primary.fetchmodel.getmasters.Usermaster;
import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.model.usermanagement.LoggedUser;
import com.agaram.eln.primary.service.usermanagement.FreeUserService;

@RestController
@RequestMapping(value = "/Freeuserlogin", method = RequestMethod.POST)
public class FreeUserController {
	@Autowired
	private FreeUserService freeuserservice;
	
	@PostMapping("/Createuser")
	public LSuserMaster Createuser(@RequestBody LSuserMaster objuser) throws Exception {
		return freeuserservice.Createuser(objuser);
	}
	
	@PostMapping("/Loginfreeuser")
	public Map<String, Object> Loginfreeuser(@RequestBody LSuserMaster objuser) throws Exception {
		return freeuserservice.Loginfreeuser(objuser);
	}
	
	@PostMapping("/Validateuser")
	public Usermaster Validateuser(@RequestBody LSuserMaster objuser) throws Exception {
		return freeuserservice.Validateuser(objuser);
	}
	
	@PostMapping("/Setpassword")
	public Response Setpassword(@RequestBody LoggedUser objuser) throws Exception {
		return freeuserservice.Setpassword(objuser);
	}
	
	@PostMapping("/Resetpassword")
	public Response Resetpassword(@RequestBody LoggedUser objuser) throws Exception {
		return freeuserservice.Resetpassword(objuser);
	}
	
	@PostMapping("/Loginfreeuserwithname")
	public Map<String, Object> Loginfreeuserwithname(@RequestBody LSuserMaster objuser) throws Exception {
		return freeuserservice.Loginfreeuserwithname(objuser);
	}
	
	@PostMapping("/GetUsersaddrestrict")
	public Boolean GetUsersaddrestrict(@RequestBody LSSiteMaster objsite) {
		return freeuserservice.GetUsersaddrestrict(objsite);
	}
}
