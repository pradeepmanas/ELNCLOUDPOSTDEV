package com.eln.ELNsampleproject.Primary.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.eln.ELNsampleproject.Primary.service.ElnperformanceSerive;
@RestController
@RequestMapping(value = "/ElnperformanceController", method = RequestMethod.GET)
public class ElnperformanceController {
    
    @Autowired
    ElnperformanceSerive Elnperformanceservice;
    
    @GetMapping("/tocheckforloop")
    public Integer tocheckforloop(HttpServletRequest request) throws Exception {
        return Elnperformanceservice.gettocheckforloop();
    }
    
    @GetMapping("/tocheckforloopforlimi")
    public Integer tocheckforloopforlimi(HttpServletRequest request) throws Exception {
        return Elnperformanceservice.tocheckforloopforlimi();
    }
}
