package com.agaram.eln.primary.model.usermanagement;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.agaram.eln.primary.model.cfr.LScfttransaction;


@Entity
@Table(name = "LSactiveuser")
public class LSactiveUser {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "activeusercode")
	private Integer activeusercode;
	@Column(columnDefinition = "varchar(255)")
	private String clientname;
	@Transient
	private String username;
	@Temporal(TemporalType.TIMESTAMP)
	private Date timestamp;
	
	@ManyToOne 
	private LSuserMaster lsusermaster;
	
	
	@ManyToOne
	private LSSiteMaster lssitemaster;
	
	@Transient
	LScfttransaction objsilentaudit;
	
	
	public LScfttransaction getObjsilentaudit() {
		return objsilentaudit;
	}

	public void setObjsilentaudit(LScfttransaction objsilentaudit) {
		this.objsilentaudit = objsilentaudit;
	}

	public Integer getActiveusercode() {
		return activeusercode;
	}

	public void setActiveusercode(Integer activeusercode) {
		this.activeusercode = activeusercode;
	}
	
	public String getClientname() {
		return clientname;
	}

	public void setClientname(String clientname) {
		this.clientname = clientname;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public LSuserMaster getLsusermaster() {
		return lsusermaster;
	}

	public void setLsusermaster(LSuserMaster lsusermaster) {
		this.lsusermaster = lsusermaster;
	}

	public LSSiteMaster getLssitemaster() {
		return lssitemaster;
	}

	public void setLssitemaster(LSSiteMaster lssitemaster) {
		this.lssitemaster = lssitemaster;
	}

	public String getUsername() {
//		String username = "";
		if(this.lsusermaster!=null)
		{
			this.username = this.lsusermaster.getUsername();
		}
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}	
	
}
