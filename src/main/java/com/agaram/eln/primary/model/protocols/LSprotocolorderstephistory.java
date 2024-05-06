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

@Entity(name = "LSprotocolorderstephistory")
@Table(name = "LSprotocolorderstephistory")
public class LSprotocolorderstephistory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "protocolorderstephistorycode")
	private Integer protocolorderstephistorycode;
	
	public Integer protocolorderstepcode;
	
	private Long protocolordercode;
	
	private Long batchcode;
	
	@ManyToOne 
	private LSuserMaster createby;
	
	@ManyToOne 
	private LSuserMaster modifiedby;
	
	@Temporal(TemporalType.TIMESTAMP)
	public Date stepstartdate;
	
	@Temporal(TemporalType.TIMESTAMP)
	public Date stependdate;
	
	@Temporal(TemporalType.TIMESTAMP)
	public Date stepskipeddate;
	
	@Column(columnDefinition = "varchar(250)")
	private String action;
	
	@Column(columnDefinition = "varchar(250)")
	private String comment;
	
	public Integer stepno;
	
	public Integer viewoption;
	
	@Transient
	LScfttransaction objmanualaudit;

	@Transient
	LScfttransaction objsilentaudit;
	
	@Transient
	private String batchid;

	public String getBatchid() {
		return batchid;
	}

	public void setBatchid(String batchid) {
		this.batchid = batchid;
	}

	public LScfttransaction getObjmanualaudit() {
		return objmanualaudit;
	}

	public void setObjmanualaudit(LScfttransaction objmanualaudit) {
		this.objmanualaudit = objmanualaudit;
	}

	public LScfttransaction getObjsilentaudit() {
		return objsilentaudit;
	}

	public void setObjsilentaudit(LScfttransaction objsilentaudit) {
		this.objsilentaudit = objsilentaudit;
	}

	public Long getBatchcode() {
		return batchcode;
	}

	public void setBatchcode(Long batchcode) {
		this.batchcode = batchcode;
	}

	public Integer getProtocolorderstephistorycode() {
		return protocolorderstephistorycode;
	}

	public void setProtocolorderstephistorycode(Integer protocolorderstephistorycode) {
		this.protocolorderstephistorycode = protocolorderstephistorycode;
	}

	public Integer getProtocolorderstepcode() {
		return protocolorderstepcode;
	}

	public void setProtocolorderstepcode(Integer protocolorderstepcode) {
		this.protocolorderstepcode = protocolorderstepcode;
	}

	public Long getProtocolordercode() {
		return protocolordercode;
	}

	public void setProtocolordercode(Long protocolordercode) {
		this.protocolordercode = protocolordercode;
	}

	public LSuserMaster getCreateby() {
		return createby;
	}

	public void setCreateby(LSuserMaster createby) {
		this.createby = createby;
	}

	public LSuserMaster getModifiedby() {
		return modifiedby;
	}

	public void setModifiedby(LSuserMaster modifiedby) {
		this.modifiedby = modifiedby;
	}

	public Date getStepstartdate() {
		return stepstartdate;
	}

	public void setStepstartdate(Date stepstartdate) {
		this.stepstartdate = stepstartdate;
	}

	public Date getStependdate() {
		return stependdate;
	}

	public void setStependdate(Date stependdate) {
		this.stependdate = stependdate;
	}

	public Date getStepskipeddate() {
		return stepskipeddate;
	}

	public void setStepskipeddate(Date stepskipeddate) {
		this.stepskipeddate = stepskipeddate;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Integer getStepno() {
		return stepno;
	}

	public void setStepno(Integer stepno) {
		this.stepno = stepno;
	}

	public Integer getViewoption() {
		return viewoption;
	}

	public void setViewoption(Integer viewoption) {
		this.viewoption = viewoption;
	}
	
	@Transient
	@Temporal(TemporalType.TIMESTAMP)
	private Date fromdate; 
	
	@Transient
	@Temporal(TemporalType.TIMESTAMP)
	private Date todate;

	public Date getFromdate() {
		return fromdate;
	}

	public void setFromdate(Date fromdate) {
		this.fromdate = fromdate;
	}

	public Date getTodate() {
		return todate;
	}

	public void setTodate(Date todate) {
		this.todate = todate;
	} 
}
