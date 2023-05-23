package com.eln.ELNsampleproject.Primary.service;

import org.springframework.stereotype.Service;

@Service
public class ElnperformanceSerive {

	public Integer gettocheckforloop() {
		int retunvalues=0;
		for (int index=0;index<=10000000;index++) {
			System.out.println("Loop no:" + index);
			retunvalues=index;
		}
		return retunvalues;
		
		
	}

	public Integer tocheckforloopforlimi() {
		int retunvalues=0;
		for (int index=0;index<=1000000;index++) {
			System.out.println("Loop no:" + index);
			retunvalues=index;
		}
		return retunvalues;
	}

}
