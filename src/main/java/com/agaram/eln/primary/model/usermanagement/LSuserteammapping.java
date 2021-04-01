package com.agaram.eln.primary.model.usermanagement;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
@Entity
@Table(name = "LSuserteammapping")
public class LSuserteammapping {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "userteammapcode")
	private Integer userteammapcode;
			
	private Integer teamcode;
	
	@ManyToOne
	private LSuserMaster lsuserMaster;
	public Integer getUserteammapcode() {
		return userteammapcode;
	}
	public void setUserteammapcode(Integer userteammapcode) {
		this.userteammapcode = userteammapcode;
	}
	
	public LSuserMaster getLsuserMaster() {
		return lsuserMaster;
	}
	public void setLsuserMaster(LSuserMaster lsuserMaster) {
		this.lsuserMaster = lsuserMaster;
	}
	public Integer getTeamcode() {
		return teamcode;
	}
	public void setTeamcode(Integer teamcode) {
		this.teamcode = teamcode;
	}
	
}
