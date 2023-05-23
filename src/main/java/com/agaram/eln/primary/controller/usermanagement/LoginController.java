package com.agaram.eln.primary.controller.usermanagement;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/Login", method = RequestMethod.POST)
public class LoginController {

	
	@GetMapping("/validate")
	public void loadSite(HttpServletRequest request)throws Exception {
		for(int i=0; i<10000;i++)
		{
			System.out.println(i);
		}
	}

	
}
