package com.agaram.eln.primary.model.usermanagement;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "LSMultiusergroup")
public class LSMultiusergroup {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "multiusergroupcode") 
	private int multiusergroupcode;

	@Column(name = "usercode")
//	@JsonBackReference
	private Integer usercode;
	@ManyToOne 
	private LSusergroup lsusergroup;
	@Column(name = "defaultusergroup")
	private Integer defaultusergroup;
	public Integer getDefaultusergroup() {
		return defaultusergroup;
	}
	public void setDefaultusergroup(Integer defaultusergroup) {
		this.defaultusergroup = defaultusergroup;
	}

	public Integer getUsercode() {
		return usercode;
	}
	public int getMultiusergroupcode() {
		return multiusergroupcode;
	}
	public void setMultiusergroupcode(int multiusergroupcode) {
		this.multiusergroupcode = multiusergroupcode;
	}
	public void setUsercode(Integer usercode) {
		this.usercode = usercode;
	}
	public LSusergroup getLsusergroup() {
		return lsusergroup;
	}
	public void setLsusergroup(LSusergroup lsusergroup) {
		this.lsusergroup = lsusergroup;
	}
}
