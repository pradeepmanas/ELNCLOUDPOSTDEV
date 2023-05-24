package com.agaram.eln.secondary.model.archive;

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

import com.agaram.eln.primary.model.usermanagement.LoggedUser;


@Entity
@Table(name = "ArchiveTransactions")
public class LScfrachivetransaction {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "serialno")
	private Integer serialno;
	@Column(columnDefinition = "text")
	private String moduleName;
	@Column(columnDefinition = "varchar(100)")
	private String reason;
	@Column(columnDefinition = "text")
	private String comments;
	@Column(columnDefinition = "text")
	private String actions;
	@Column(columnDefinition = "varchar(100)")
	private String systemcoments;
//	@Column(columnDefinition = "date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date transactiondate;
	@Column(columnDefinition = "varchar(100)")
	private String manipulatetype;
	@Column(columnDefinition = "varchar(100)")
	private String tableName;
	@Column(columnDefinition = "varchar(100)")
	private String modifiedData;
	@Column(columnDefinition = "varchar(100)")
	private String requestedclientid;
	@Column(columnDefinition = "varchar(100)")
	private String affectedclientid;
	@Column(columnDefinition = "varchar(100)")
	private String instrumentid;
	@Column(columnDefinition = "varchar(100)")
	private String reviewedstatus;
	
	@ManyToOne 
	private LScfrArchiveHistory lscfrArchiveHistory;
	
	private String username;
	
	public LScfrachivetransaction()
	{
		
	}
	
	public LScfrachivetransaction(Integer serialno,String moduleName,String reason,
			String comments, String actions,String systemcoments,Date transactiondate,
			String manipulatetype, String tableName, String modifiedData, String requestedclientid,
			String affectedclientid, String instrumentid,String reviewedstatus,String username, LScfrArchiveHistory lscfrArchiveHistory)
	{
		this.serialno = serialno;
		this.moduleName = moduleName;
		this.reason = reason;
		this.comments = comments;
		this.actions = actions;
		this.systemcoments = systemcoments;
		this.transactiondate = transactiondate;
		this.manipulatetype = manipulatetype;
		this.tableName = tableName;
		this.modifiedData = modifiedData;
		this.requestedclientid = requestedclientid;
		this.affectedclientid = affectedclientid;
		this.instrumentid = instrumentid;
		this.reviewedstatus = reviewedstatus;
		this.lscfrArchiveHistory= lscfrArchiveHistory;
		this.username= username;
	}
	
	@Transient
	LoggedUser objuser;
	public LoggedUser getObjuser() {
		return objuser;
	}
	public void setObjuser(LoggedUser objuser) {
		this.objuser = objuser;
	}
	@Transient
	private boolean select;
	public Integer getSerialno() {
		return serialno;
	}
	public void setSerialno(Integer serialno) {
		this.serialno = serialno;
	}
	public String getModuleName() {
		return moduleName;
	}
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getActions() {
		return actions;
	}
	public void setActions(String actions) {
		this.actions = actions;
	}
	public String getSystemcoments() {
		return systemcoments;
	}
	public void setSystemcoments(String systemcoments) {
		this.systemcoments = systemcoments;
	}
	public Date getTransactiondate() {
		return transactiondate;
	}
	public void setTransactiondate(Date transactiondate) {
		this.transactiondate = transactiondate;
	}
	public String getManipulatetype() {
		return manipulatetype;
	}
	public void setManipulatetype(String manipulatetype) {
		this.manipulatetype = manipulatetype;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	public String getModifiedData() {
		return modifiedData;
	}

	public void setModifiedData(String modifiedData) {
		this.modifiedData = modifiedData;
	}

	public String getRequestedclientid() {
		return requestedclientid;
	}
	public void setRequestedclientid(String requestedclientid) {
		this.requestedclientid = requestedclientid;
	}
	public String getAffectedclientid() {
		return affectedclientid;
	}
	public void setAffectedclientid(String affectedclientid) {
		this.affectedclientid = affectedclientid;
	}
	public String getInstrumentid() {
		return instrumentid;
	}
	public void setInstrumentid(String instrumentid) {
		this.instrumentid = instrumentid;
	}
	
	public LScfrArchiveHistory getLscfrArchiveHistory() {
		return lscfrArchiveHistory;
	}
	public void setLscfrArchiveHistory(LScfrArchiveHistory lscfrArchiveHistory) {
		this.lscfrArchiveHistory = lscfrArchiveHistory;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public boolean isSelect() {
		return select;
	}
	public void setSelect(boolean select) {
		this.select = select;
	}
	public String getReviewedstatus() {
		return reviewedstatus;
	}
	public void setReviewedstatus(String reviewedstatus) {
		this.reviewedstatus = reviewedstatus;
	}
	
	
}
