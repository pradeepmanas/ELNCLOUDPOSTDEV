package com.agaram.eln.primary.model.cfr;

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

import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.model.usermanagement.LoggedUser;

@Entity
@Table(name = "LScfrreasons")
public class LScfrreasons {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "reasoncode") 
	private Integer reasoncode;
	
	@Column(columnDefinition = "varchar(255)")
	private String comments;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date createddate;
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifieddate;

	@Column(name = "status") 
	private Integer status;
	
	@ManyToOne
	private LSuserMaster createdby;
	
	@ManyToOne
	private LSuserMaster modifiedby;
	
	@Transient
	private Response response;
	
	@Transient
	LScfttransaction objsilentaudit;
	

	@Transient
	LScfttransaction objmanualaudit;
	
	@Transient
	LoggedUser objuser;
	
	@Transient
	LSuserMaster LSuserMaster;
	
	@ManyToOne 
	private LSSiteMaster lssitemaster;
	
	public LSSiteMaster getLssitemaster() {
		return lssitemaster;
	}
	public void setLssitemaster(LSSiteMaster lssitemaster) {
		this.lssitemaster = lssitemaster;
	}
	

	public LSuserMaster getCreatedby() {
		return createdby;
	}
	public void setCreatedby(LSuserMaster createdby) {
		this.createdby = createdby;
	}
	
	public Date getCreateddate() {
		return createddate;
	}
	public void setCreateddate(Date createddate) {
		this.createddate = createddate;
	}
	public Date getModifieddate() {
		return modifieddate;
	}
	public void setModifieddate(Date modifieddate) {
		this.modifieddate = modifieddate;
	}
	
	public LoggedUser getObjuser() {
		return objuser;
	}
	public void setObjuser(LoggedUser objuser) {
		this.objuser = objuser;
	}
	public LSuserMaster getLSuserMaster() {
		return LSuserMaster;
	}
	public void setLSuserMaster(LSuserMaster lSuserMaster) {
		LSuserMaster = lSuserMaster;
	}
	public LScfttransaction getObjsilentaudit() {
		return objsilentaudit;
	}
	public void setObjsilentaudit(LScfttransaction objsilentaudit) {
		this.objsilentaudit = objsilentaudit;
	}
	
	public LScfttransaction getObjmanualaudit() {
		return objmanualaudit;
	}
	public void setObjmanualaudit(LScfttransaction objmanualaudit) {
		this.objmanualaudit = objmanualaudit;
	}
	
	
	public Response getResponse() {
		return response;
	}
	public void setResponse(Response response) {
		this.response = response;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getReasoncode() {
		return reasoncode;
	}
	public void setReasoncode(Integer reasoncode) {
		this.reasoncode = reasoncode;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public LSuserMaster getModifiedby() {
		return modifiedby;
	}
	public void setModifiedby(LSuserMaster modifiedby) {
		this.modifiedby = modifiedby;
	}
	
	
}
