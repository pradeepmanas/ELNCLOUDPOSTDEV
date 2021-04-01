package com.agaram.eln.primary.model.cfr;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.model.usermanagement.LoggedUser;

@Entity
@Table(name = "LSpreferences")
public class LSpreferences {
	//unique = true
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Basic(optional = false)
	private Integer serialno;
	@Column(columnDefinition = "varchar(100)")
	private String tasksettings;
	@Column(columnDefinition = "varchar(100)")
	private String valuesettings;
	
	@Transient
	LScfttransaction objsilentaudit;
	
	@Transient
	private Response response;
	
	@Transient
	LoggedUser objuser;
	
	@Transient
	private LSuserMaster lsusermaster;
	public LScfttransaction getObjsilentaudit() {
		return objsilentaudit;
	}
	public void setObjsilentaudit(LScfttransaction objsilentaudit) {
		this.objsilentaudit = objsilentaudit;
	}
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

	public LSuserMaster getLsusermaster() {
		return lsusermaster;
	}
	public void setLsusermaster(LSuserMaster lsusermaster) {
		this.lsusermaster = lsusermaster;
	}
	public Integer getSerialno() {
		return serialno;
	}
	public void setSerialno(Integer serialno) {
		this.serialno = serialno;
	}
	public String getTasksettings() {
		return tasksettings;
	}
	public void setTasksettings(String tasksettings) {
		this.tasksettings = tasksettings;
	}
	public String getValuesettings() {
		return valuesettings;
	}
	public void setValuesettings(String valuesettings) {
		this.valuesettings = valuesettings;
	}
	
	
}
