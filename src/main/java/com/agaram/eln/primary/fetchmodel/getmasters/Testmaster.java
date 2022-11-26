package com.agaram.eln.primary.fetchmodel.getmasters;

import java.util.Date;

import com.agaram.eln.primary.model.usermanagement.LSuserMaster;

public class Testmaster {
	private Integer testcode;
	private String testname;
	private String taskcategory;
	private Date createdate;
	private LSuserMaster createby;
	private String description;
	
	public Testmaster(Integer testcode, String testname,String taskcategory,Date createdate,LSuserMaster createby, String description)
	{
		this.testcode = testcode;
		this.testname = testname;
		this.taskcategory=taskcategory;
		this.createdate=createdate;
		this.createby=createby;
		 this.description=description;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTaskcategory() {
		return taskcategory;
	}

	public void setTaskcategory(String taskcategory) {
		this.taskcategory = taskcategory;
	}

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public LSuserMaster getCreateby() {
		return createby;
	}

	public void setCreateby(LSuserMaster createby) {
		this.createby = createby;
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
