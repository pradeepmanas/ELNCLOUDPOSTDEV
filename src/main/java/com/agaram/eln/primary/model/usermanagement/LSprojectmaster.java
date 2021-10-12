package com.agaram.eln.primary.model.usermanagement;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.general.Response;

@Entity
@Table(name = "LSprojectmaster")
public class LSprojectmaster {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "projectcode")
	private Integer projectcode;

	@Column(columnDefinition = "varchar(100)")
	private String projectname;
	private Integer status;
	@ManyToOne
	private LSSiteMaster lssitemaster;

	@ManyToOne
	private LSusersteam lsusersteam;

	@Transient
	private String teamname;

	@Transient
	private Response response;

	@Transient
	LScfttransaction objsilentaudit;

	@Transient
	LScfttransaction objmanualaudit;

	@Transient
	LoggedUser objuser;

	@Transient
	LSuserMaster lsusermaster;

	@ManyToOne
	private LSuserMaster modifiedby;
	
	public LSSiteMaster getLssitemaster() {
		return lssitemaster;
	}

	public void setLssitemaster(LSSiteMaster lssitemaster) {
		this.lssitemaster = lssitemaster;
	}
	
	public LScfttransaction getObjmanualaudit() {
		return objmanualaudit;
	}

	public void setObjmanualaudit(LScfttransaction objmanualaudit) {
		this.objmanualaudit = objmanualaudit;
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

	public Integer getProjectcode() {
		return projectcode;
	}

	public void setProjectcode(Integer projectcode) {
		this.projectcode = projectcode;
	}

	public String getProjectname() {
		return projectname;
	}

	public void setProjectname(String projectname) {
		this.projectname = projectname;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public LSusersteam getLsusersteam() {
		return lsusersteam;
	}

	public void setLsusersteam(LSusersteam lsusersteam) {
		this.lsusersteam = lsusersteam;
	}

	public String getTeamname() {
		if (this.lsusersteam != null && this.lsusersteam.getTeamname() != null) {
			return this.lsusersteam.getTeamname();
		}
		return "";
	}

	public void setTeamname(String teamname) {
		this.teamname = teamname;
	}

	public LSuserMaster getModifiedby() {
		return modifiedby;
	}

	public void setModifiedby(LSuserMaster modifiedby) {
		this.modifiedby = modifiedby;
	}

}
