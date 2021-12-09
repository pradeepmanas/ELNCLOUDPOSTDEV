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

@Entity(name = "LSprotocolorderworkflowhistory")
@Table(name = "LSprotocolorderworkflowhistory")
public class LSprotocolorderworkflowhistory {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "historycode")
	private Integer historycode;
	
	@Column(name = "approvelstatus") 
	private Integer approvelstatus;
	
	@Column(name = "protocolordercode")
	private Integer protocolordercode;
	
	@Column(columnDefinition = "varchar(250)")
	private String action;
	
	@Column(columnDefinition = "varchar(250)")
	private String comment;
	
	
	@ManyToOne 
	private LSuserMaster createby;
	
	//@Column(columnDefinition = "datetime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdate;
	
	@ManyToOne
	private LSprotocolworkflow currentworkflow;

	
	@Transient
	LScfttransaction objsilentaudit;


	public Integer getHistorycode() {
		return historycode;
	}


	public Integer getApprovelstatus() {
		return approvelstatus;
	}


	public Integer getProtocolordercode() {
		return protocolordercode;
	}


	public String getAction() {
		return action;
	}


	public String getComment() {
		return comment;
	}


	public LSuserMaster getCreateby() {
		return createby;
	}


	public Date getCreatedate() {
		return createdate;
	}


	public LSprotocolworkflow getCurrentworkflow() {
		return currentworkflow;
	}


	public LScfttransaction getObjsilentaudit() {
		return objsilentaudit;
	}


	public void setHistorycode(Integer historycode) {
		this.historycode = historycode;
	}


	public void setApprovelstatus(Integer approvelstatus) {
		this.approvelstatus = approvelstatus;
	}


	public void setProtocolordercode(Integer protocolordercode) {
		this.protocolordercode = protocolordercode;
	}


	public void setAction(String action) {
		this.action = action;
	}


	public void setComment(String comment) {
		this.comment = comment;
	}


	public void setCreateby(LSuserMaster createby) {
		this.createby = createby;
	}


	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}


	public void setCurrentworkflow(LSprotocolworkflow currentworkflow) {
		this.currentworkflow = currentworkflow;
	}


	public void setObjsilentaudit(LScfttransaction objsilentaudit) {
		this.objsilentaudit = objsilentaudit;
	}
}
