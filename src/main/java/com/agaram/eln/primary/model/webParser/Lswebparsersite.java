package com.agaram.eln.primary.model.webParser;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Lswebparsersite")
public class Lswebparsersite {
	
	@Id 
	@Column(name = "sitekey", nullable = false)
	private Integer sitekey;
	
	private Integer status;
	
	private String sitename;

	public Integer getSitekey() {
		return sitekey;
	}

	public void setSitekey(Integer sitekey) {
		this.sitekey = sitekey;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getSitename() {
		return sitename;
	}

	public void setSitename(String sitename) {
		this.sitename = sitename;
	}
	
	

}
