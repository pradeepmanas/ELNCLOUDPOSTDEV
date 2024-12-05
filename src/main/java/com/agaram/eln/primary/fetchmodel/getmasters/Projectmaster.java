package com.agaram.eln.primary.fetchmodel.getmasters;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.agaram.eln.primary.model.usermanagement.LSusersteam;

public class Projectmaster {
	private Integer projectcode;
	private String projectname;
	private LSusersteam lsusersteam;
	private Integer status;
	private String projectstatus;
	
	public Projectmaster(Integer projectcode,String projectname,LSusersteam lsusersteam, Integer status, String projectstatus)
	{
		this.projectcode =  projectcode;
		this.projectname = projectname;
		this.lsusersteam =lsusersteam;
		this.status = status;
		this.projectstatus = projectstatus;
	}
	
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getProjectstatus() {
		if(projectstatus != null)
		{
		return  projectstatus.trim().equals("A")?"Active":"Retired";
		}
		else
		{
			return "";
		}
	}

	public void setProjectstatus(String projectstatus) {
		this.projectstatus = projectstatus;
	}
	
	public LSusersteam getLsusersteam() {
		return lsusersteam;
	}

	public void setLsusersteam(LSusersteam lsusersteam) {
		this.lsusersteam = lsusersteam;
	}

	public Integer getProjectcode() {
		return projectcode;
	}
	public void setProjectcode(Integer projectcode) {
		this.projectcode = projectcode;
	}
	public String getProjectname() {
		return projectname;
	}
	public void setProjectname(String projectname) {
		this.projectname = projectname;
	}
}
