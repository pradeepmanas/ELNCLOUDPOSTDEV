package com.agaram.eln.primary.model.webParser;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Lswebparseruser")
public class Lswebparseruser {

	@Id 
	@Column(name = "userkey", nullable = false)
	private Integer userkey;	
	private String firstname;
	private String lastname;
	private String loginid;
	public Integer getUserkey() {
		return userkey;
	}
	public void setUserkey(Integer userkey) {
		this.userkey = userkey;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getLoginid() {
		return loginid;
	}
	public void setLoginid(String loginid) {
		this.loginid = loginid;
	}
	
	
	
}
