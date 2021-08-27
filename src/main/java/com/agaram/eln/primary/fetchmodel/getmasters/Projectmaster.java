package com.agaram.eln.primary.fetchmodel.getmasters;

public class Projectmaster {
	private Integer projectcode;
	private String projectname;
	
	public Projectmaster(Integer projectcode,String projectname)
	{
		this.projectcode =  projectcode;
		this.projectname = projectname;
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
