package com.agaram.eln.primary.model.cfr;

import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.agaram.eln.primary.model.cfr.embeddable.AudittrailconfigurationId;
import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.model.usermanagement.LoggedUser;

@Entity
@Table(name = "LSaudittrailconfiguration")
@IdClass(AudittrailconfigurationId.class)
public class LSaudittrailconfiguration {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "auditcofigcode")
	private Integer auditcofigcode;
	@Transient
	private Response response;
	public Response getResponse() {
		return response;
	}
	public void setResponse(Response response) {
		this.response = response;
	}
	@ManyToOne
	private LSuserMaster lsusermaster;
	
	@ManyToOne
	private LSSiteMaster lssitemaster;
	
	@Transient
	private LSuserMaster lsuserMaster;
	
	public LSuserMaster getLsuserMaster() {
		return lsuserMaster;
	}
	public void setLsuserMaster(LSuserMaster lsuserMaster) {
		this.lsuserMaster = lsuserMaster;
	}
	private String modulename;
	private String screenname;
	private String taskname;
	private Integer manualaudittrail;
	
	@Transient
	List<LScfttransaction> objsilentauditlst;
	
	public List<LScfttransaction> getObjsilentauditlst() {
		return objsilentauditlst;
	}
	public void setObjsilentauditlst(List<LScfttransaction> objsilentauditlst) {
		this.objsilentauditlst = objsilentauditlst;
	}
	@Transient
	LScfttransaction objsilentaudit;
	
	@Transient
	LScfttransaction objmanualaudit;
	@Transient
	private Integer serialno;
	@Transient
	LoggedUser objuser;
	
	public LoggedUser getObjuser() {
		return objuser;
	}
	public void setObjuser(LoggedUser objuser) {
		this.objuser = objuser;
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
	public LSuserMaster getLsusermaster() {
		return lsusermaster;
	}
	public Integer getAuditcofigcode() {
		return auditcofigcode;
	}
	public void setAuditcofigcode(Integer auditcofigcode) {
		this.auditcofigcode = auditcofigcode;
	}
	public void setLsusermaster(LSuserMaster lsusermaster) {
		this.lsusermaster = lsusermaster;
	}
	public String getModulename() {
		return modulename;
	}
	public void setModulename(String modulename) {
		this.modulename = modulename;
	}
	public String getScreenname() {
		return screenname;
	}
	public void setScreenname(String screenname) {
		this.screenname = screenname;
	}
	public String getTaskname() {
		return taskname;
	}
	public void setTaskname(String taskname) {
		this.taskname = taskname;
	}
	public Integer getManualaudittrail() {
		return manualaudittrail;
	}
	public void setManualaudittrail(Integer manualaudittrail) {
		this.manualaudittrail = manualaudittrail;
	}
	
	public LSSiteMaster getLssitemaster() {
		return lssitemaster;
	}
	public void setLssitemaster(LSSiteMaster lssitemaster) {
		this.lssitemaster = lssitemaster;
	}
	public Integer getSerialno() {
		return this.getAuditcofigcode();
	}
	public void setSerialno(Integer serialno) {
		this.serialno = serialno;
	}
	
	private Integer ordersequnce;
	
	public Integer getOrdersequnce() {
		return ordersequnce;
	}
	public void setOrdersequnce(Integer ordersequnce) {
		this.ordersequnce = ordersequnce;
	}
	public void operate() {
		// TODO Auto-generated method stub
		
	}
}
