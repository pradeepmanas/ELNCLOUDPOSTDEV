package com.agaram.eln.primary.model.cfr;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.agaram.eln.primary.model.general.Response;


@Entity
@Table(name = "LScfttransaction")
public class LScfttransaction {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "serialno")
	private Integer serialno;
	@Column(columnDefinition = "varchar(250)")
	private String moduleName;
	@Column(columnDefinition = "varchar(100)")
	private String reason;
	@Column(columnDefinition = "varchar(250)")
	private String comments;
	@Column(columnDefinition = "varchar(250)")
	private String actions;
	@Column(columnDefinition = "varchar(100)")
	private String systemcoments;
	//@Column(columnDefinition = "datetime")
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
	
//	@ManyToOne 
	@Column(name="lssitemaster_sitecode")
	private Integer lssitemaster;
	
	public Integer getLssitemaster() {
		return lssitemaster;
	}
	public void setLssitemaster(Integer lssitemaster) {
		this.lssitemaster = lssitemaster;
	}
//	@ManyToOne 
//	private LSuserMaster lsuserMaster;
	@Column(name="lsusermaster_usercode")
	private Integer lsuserMaster;
	
	public Integer getLsuserMaster() {
		return lsuserMaster;
	}
	public void setLsuserMaster(Integer lsuserMaster) {
		this.lsuserMaster = lsuserMaster;
	}
	@Transient
	private String username;

//	@ManyToOne 
//	private LSreviewdetails LSreviewdetails;
	
//	@Transient
//	private String ReviewStatus;
	
//	@Transient
//	LoggedUser objuser;
	
	public String getUsername() {
		return username;
	}
	@Transient
	Response objResponse;
	
	public Response getObjResponse() {
		return objResponse;
	}
	public void setObjResponse(Response objResponse) {
		this.objResponse = objResponse;
	}
//	public LoggedUser getObjuser() {
//		return objuser;
//	}
//	public void setObjuser(LoggedUser objuser) {
//		this.objuser = objuser;
//	}
	@Transient
	private boolean select;
	
	public boolean isSelect() {
		return select;
	}
	public void setSelect(boolean select) {
		this.select = select;
	}
	public Integer getSerialno() {
		return serialno;
	}
	public void setSerialno(Integer serialno) {
		this.serialno = serialno;
	}
	
//	public LSuserMaster getLsuserMaster() {
//		return lsuserMaster;
//	}
//	public void setLsuserMaster(LSuserMaster lsuserMaster) {
//		this.lsuserMaster = lsuserMaster;
//	}
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
	
//	public LSSiteMaster getLssitemaster() {
//		return lssitemaster;
//	}
//	public void setLssitemaster(LSSiteMaster lssitemaster) {
//		this.lssitemaster = lssitemaster;
//	}
//	public String getUsername() {
////		if(this.lsuserMaster != null)
////		{
////			return this.lsuserMaster.getUsername();
////		}
////		else
////		{
//			return "";
////		}
//	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getReviewedstatus() {
		return reviewedstatus;
	}
	public void setReviewedstatus(String reviewedstatus) {
		this.reviewedstatus = reviewedstatus;
	}
	
	
}
