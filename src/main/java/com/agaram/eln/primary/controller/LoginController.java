package com.agaram.eln.primary.controller;




import javax.servlet.http.HttpServletRequest;


import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/Login", method = RequestMethod.POST)
public class LoginController {

	
	@GetMapping("/validate")
	public Integer loadSite(HttpServletRequest request)throws Exception {
		int retunvalues=0;
		for(int i=0; i<10000;i++)
		{
			System.out.println(i);
			retunvalues=i;
		}
		return retunvalues;
	}
	
    @GetMapping("/tocheckforloop")
    public Integer tocheckforloop(HttpServletRequest request) throws Exception {
    	int retunvalues=0;
		for (int index=0;index<=10000000;index++) {
			System.out.println("Loop no:" + index);
			retunvalues=index;
		}
		return retunvalues;
    }
    
    @GetMapping("/tocheckforloopforlimi")
    public Integer tocheckforloopforlimi(HttpServletRequest request) throws Exception {
    	int retunvalues=0;
		for (int index=0;index<=1000000;index++) {
			System.out.println("Loop no:" + index);
			retunvalues=index;
		}
		return retunvalues;
    }

	
}
