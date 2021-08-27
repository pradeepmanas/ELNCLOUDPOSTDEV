package com.agaram.eln.primary.fetchmodel.getmasters;

public class Testmaster {
	private Integer testcode;
	private String testname;
	
	public Testmaster(Integer testcode, String testname)
	{
		this.testcode = testcode;
		this.testname = testname;
	}
	
	public Integer getTestcode() {
		return testcode;
	}
	public void setTestcode(Integer testcode) {
		this.testcode = testcode;
	}
	public String getTestname() {
		return testname;
	}
	public void setTestname(String testname) {
		this.testname = testname;
	}
	
	
}
