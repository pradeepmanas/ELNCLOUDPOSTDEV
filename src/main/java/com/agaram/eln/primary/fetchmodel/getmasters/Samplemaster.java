package com.agaram.eln.primary.fetchmodel.getmasters;

import java.util.Date;

import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;

public class Samplemaster {
	
	private Integer samplecode;
	private String samplename;
	private LSSiteMaster lssitemaster;
	private String samplecategory;
	private Date createdate;
	private LSuserMaster createby;
	private Integer status;
	private String samplestatus;
	
	public Samplemaster(Integer samplecode, String samplename,LSSiteMaster lssitemaster,String samplecategory,Date createdate,
			LSuserMaster createby, Integer status, String samplestatus)
	{
		this.samplecode = samplecode;
		this.samplename = samplename;
		this.lssitemaster=lssitemaster;
		this.samplecategory=samplecategory;
		this.createdate=createdate;
		this.createby=createby;
		this.status = status;
		this.samplestatus = samplestatus;
	}

	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}	
	
	public String getSamplestatus() {
		if(samplestatus != null)
		{
		return  samplestatus.trim().equals("A")?"Active":"Retired";
		}
		else
		{
			return "";
		}
	}

	public void setSamplestatus(String samplestatus) {
		this.samplestatus = samplestatus;
	}
	
	public LSuserMaster getCreateby() {
		return createby;
	}

	public void setCreateby(LSuserMaster createby) {
		this.createby = createby;
	}

	public Integer getSamplecode() {
		return samplecode;
	}
	public void setSamplecode(Integer samplecode) {
		this.samplecode = samplecode;
	}
	public String getSamplename() {
		return samplename;
	}
	public void setSamplename(String samplename) {
		this.samplename = samplename;
	}

	public LSSiteMaster getLssitemaster() {
		return lssitemaster;
	}

	public void setLssitemaster(LSSiteMaster lssitemaster) {
		this.lssitemaster = lssitemaster;
	}

	public String getSamplecategory() {
		return samplecategory;
	}

	public void setSamplecategory(String samplecategory) {
		this.samplecategory = samplecategory;
	}

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}
	
	

}
