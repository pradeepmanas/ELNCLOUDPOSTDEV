package com.agaram.eln.primary.model.usermanagement;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Lsusersettings")
public class Lsusersettings {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "userid")
	private Integer userid;
	
	private Integer usercode;
	
	private String DFormat;
	
//	@OneToOne
//	@JoinColumn(name = "usercode")
//	private LSuserMaster lsusermaster;
//
//	public LSuserMaster getLsusermaster() {
//		return lsusermaster;
//	}
//
//	public void setLsusermaster(LSuserMaster lsusermaster) {
//		this.lsusermaster = lsusermaster;
//	}

	public Integer getUsercode() {
		return usercode;
	}

	public void setUsercode(Integer usercode) {
		this.usercode = usercode;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public String getDFormat() {
		return DFormat;
	}

	public void setDFormat(String dFormat) {
		DFormat = dFormat;
	}
}