package com.agaram.eln.primary.controller.usermanagement;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
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
}
