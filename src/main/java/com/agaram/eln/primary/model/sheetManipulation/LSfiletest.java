package com.agaram.eln.primary.model.sheetManipulation;

import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.model.usermanagement.LoggedUser;

@Entity
@Table(name = "LSfiletest")
public class LSfiletest {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "filetestcode")
	private int filetestcode;
	
	private Integer filecode;
	
	private Integer testtype;
	
	private Integer testcode;
	
	@Transient
	private Integer ismultitenant;

	@Transient
	private List<LSfileparameter> LSfileparameter;
	
	@Transient
	LScfttransaction objsilentaudit;
	
	@Transient
	LScfttransaction objmanualaudit;
	
	public LScfttransaction getObjmanualaudit() {
		return objmanualaudit;
	}

	public void setObjmanualaudit(LScfttransaction objmanualaudit) {
		this.objmanualaudit = objmanualaudit;
	}

	@Transient
	private Response response;
	
	@Transient
	LSuserMaster objLoggeduser;
	
	public Response getResponse() {
		return response;
	}

	public void setResponse(Response response) {
		this.response = response;
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

	@Transient
	LoggedUser objuser;
	
	@Transient
	LSuserMaster LSuserMaster;
	
	public LScfttransaction getObjsilentaudit() {
		return objsilentaudit;
	}

	public void setObjsilentaudit(LScfttransaction objsilentaudit) {
		this.objsilentaudit = objsilentaudit;
	}

	public int getFiletestcode() {
		return filetestcode;
	}

	public void setFiletestcode(int filetestcode) {
		this.filetestcode = filetestcode;
	}

	public Integer getFilecode() {
		return filecode;
	}

	public void setFilecode(Integer filecode) {
		this.filecode = filecode;
	}

	public Integer getTesttype() {
		return testtype;
	}

	public void setTesttype(Integer testtype) {
		this.testtype = testtype;
	}

	public Integer getTestcode() {
		return testcode;
	}

	public void setTestcode(Integer testcode) {
		this.testcode = testcode;
	}

	public List<LSfileparameter> getLSfileparameter() {
		return LSfileparameter;
	}

	public void setLSfileparameter(List<LSfileparameter> lSfileparameter) {
		LSfileparameter = lSfileparameter;
	}

	public LSuserMaster getObjLoggeduser() {
		return objLoggeduser;
	}

	public void setObjLoggeduser(LSuserMaster objLoggeduser) {
		this.objLoggeduser = objLoggeduser;
	}

	public Integer getIsmultitenant() {
		return ismultitenant;
	}

	public void setIsmultitenant(Integer ismultitenant) {
		this.ismultitenant = ismultitenant;
	}
	

}
