package com.agaram.eln.primary.model.protocols;
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
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;

@Entity
@Table(name = "LSprotocolupdates")
public class LSprotocolupdates {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "protocolcode")
	private Integer protocolcode;
	@Column(columnDefinition = "text")
	private String protocolcomment;
	@Temporal(TemporalType.TIMESTAMP)
	private Date protocolmodifiedDate;
	@Column(name = "protocolmastercode")
	private Integer protocolmastercode;
	
	@ManyToOne 
	private LSuserMaster modifiedby;

	@Transient
	LScfttransaction objsilentaudit;

	public Integer getProtocolcode() {
		return protocolcode;
	}

	public void setProtocolcode(Integer protocolcode) {
		this.protocolcode = protocolcode;
	}

	public String getProtocolcomment() {
		return protocolcomment;
	}

	public void setProtocolcomment(String protocolcomment) {
		this.protocolcomment = protocolcomment;
	}

	public Date getProtocolmodifiedDate() {
		return protocolmodifiedDate;
	}

	public void setProtocolmodifiedDate(Date protocolmodifiedDate) {
		this.protocolmodifiedDate = protocolmodifiedDate;
	}

	public Integer getProtocolmastercode() {
		return protocolmastercode;
	}

	public void setProtocolmastercode(Integer protocolmastercode) {
		this.protocolmastercode = protocolmastercode;
	}

	public LSuserMaster getModifiedby() {
		return modifiedby;
	}

	public void setModifiedby(LSuserMaster modifiedby) {
		this.modifiedby = modifiedby;
	}

	public LScfttransaction getObjsilentaudit() {
		return objsilentaudit;
	}

	public void setObjsilentaudit(LScfttransaction objsilentaudit) {
		this.objsilentaudit = objsilentaudit;
	}

	
}
