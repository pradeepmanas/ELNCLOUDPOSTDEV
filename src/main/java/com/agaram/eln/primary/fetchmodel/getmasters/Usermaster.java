package com.agaram.eln.primary.fetchmodel.getmasters;

public class Usermaster {
	private Integer usercode;
	private String username;
	private String password;
	private Integer passwordstatus;
	private Integer userretirestatus;
	
	public Usermaster(Integer usercode, String username,String password,Integer passwordstatus,
			Integer userretirestatus)
	{
		this.usercode = usercode;
		this.username = username;
		this.password = password != null ? "-": password;
		this.passwordstatus = passwordstatus;
		this.userretirestatus = userretirestatus;
	}
	
	public Integer getUsercode() {
		return usercode;
	}
	public void setUsercode(Integer usercode) {
		this.usercode = usercode;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Integer getPasswordstatus() {
		return passwordstatus;
	}
	public void setPasswordstatus(Integer passwordstatus) {
		this.passwordstatus = passwordstatus;
	}
	public Integer getUserretirestatus() {
		return userretirestatus;
	}
	public void setUserretirestatus(Integer userretirestatus) {
		this.userretirestatus = userretirestatus;
	}
	
	
	
}
