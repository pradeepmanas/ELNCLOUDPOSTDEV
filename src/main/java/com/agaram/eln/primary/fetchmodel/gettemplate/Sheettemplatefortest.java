package com.agaram.eln.primary.fetchmodel.gettemplate;

import java.util.List;

import com.agaram.eln.primary.model.sheetManipulation.LSfiletest;

public class Sheettemplatefortest {
	private Integer filecode;
	private String filenameuser;
	private List<LSfiletest> lstest;
	
	public Sheettemplatefortest(Integer filecode, String filenameuser, List<LSfiletest> lstest)
	{
		this.filecode = filecode;
		this.filenameuser = filenameuser;
		this.lstest = lstest;
	}
	
	public Integer getFilecode() {
		return filecode;
	}
	public void setFilecode(Integer filecode) {
		this.filecode = filecode;
	}
	public String getFilenameuser() {
		return filenameuser;
	}
	public void setFilenameuser(String filenameuser) {
		this.filenameuser = filenameuser;
	}
	public List<LSfiletest> getLstest() {
		return lstest;
	}
	public void setLstest(List<LSfiletest> lstest) {
		this.lstest = lstest;
	}
	
}
