package com.agaram.eln.primary.fetchmodel.getmasters;

import com.agaram.eln.primary.model.usermanagement.LSusersteam;

public class Projectmaster {
	private Integer projectcode;
	private String projectname;
	private LSusersteam lsusersteam;
	
	public Projectmaster(Integer projectcode,String projectname,LSusersteam lsusersteam)
	{
		this.projectcode =  projectcode;
		this.projectname = projectname;
		this.lsusersteam =lsusersteam;
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
