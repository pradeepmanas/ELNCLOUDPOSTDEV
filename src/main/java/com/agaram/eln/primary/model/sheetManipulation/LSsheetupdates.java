package com.agaram.eln.primary.model.sheetManipulation;

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
@Table(name = "LSsheetupdates")
public class LSsheetupdates {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "sheetcode")
	private Integer sheetcode;
	@Column(columnDefinition = "text")
	private String sheetcomment;
	@Temporal(TemporalType.TIMESTAMP)
	private Date sheetmodifiedDate;
	@Column(name = "filecode")
	private Integer filecode;
	
	@ManyToOne 
	private LSuserMaster modifiedby;

	@Transient
	LScfttransaction objsilentaudit;
	
	public LScfttransaction getObjsilentaudit() {
		return objsilentaudit;
	}

	public void setObjsilentaudit(LScfttransaction objsilentaudit) {
		this.objsilentaudit = objsilentaudit;
	}

	public Integer getSheetcode() {
		return sheetcode;
	}

	public void setSheetcode(Integer sheetcode) {
		this.sheetcode = sheetcode;
	}

	public String getSheetcomment() {
		return sheetcomment;
	}

	public void setSheetcomment(String sheetcomment) {
		this.sheetcomment = sheetcomment;
	}

	public Date getSheetmodifiedDate() {
		return sheetmodifiedDate;
	}

	public void setSheetmodifiedDate(Date sheetmodifiedDate) {
		this.sheetmodifiedDate = sheetmodifiedDate;
	}

	public Integer getFilecode() {
		return filecode;
	}

	public void setFilecode(Integer filecode) {
		this.filecode = filecode;
	}

	public LSuserMaster getModifiedby() {
		return modifiedby;
	}

	public void setModifiedby(LSuserMaster modifiedby) {
		this.modifiedby = modifiedby;
	}
	
}
