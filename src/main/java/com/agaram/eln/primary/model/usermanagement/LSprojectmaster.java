package com.agaram.eln.primary.model.usermanagement;

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
	
	private String projectid;
	
	private Integer status;
	@Column(columnDefinition = "varchar(100)")
	private String createdby;
	// private String createdon;

//	@Column(columnDefinition = "date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdon;

	@Column(columnDefinition = "varchar(20)")
	private String projectstatus;
	private String duedate;
	@Temporal(TemporalType.TIMESTAMP)
	private Date startdate;
	@Temporal(TemporalType.TIMESTAMP)
	private Date enddate;
	public Date getCreatedon() {
		return createdon;
	}

	public void setCreatedon(Date createdon) {
		this.createdon = createdon;
	}

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
	
	@Transient
	private Integer usercode;
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifieddate;
	
	@Column(name = "modifiedbyuser")
	private String modifiedbyuser;
	
	
	public String getModifiedbyuser() {
		return modifiedbyuser;
	}
	public void setModifiedbyuser(String modifiedbyuser) {
		this.modifiedbyuser = modifiedbyuser;
	}
	public Date getModifieddate() {
		return modifieddate;
	}
	public void setModifieddate(Date modifieddate) {
		this.modifieddate = modifieddate;
	}

	public Integer getUsercode() {
		return usercode;
	}

	public void setUsercode(Integer usercode) {
		this.usercode = usercode;
	}

	@ManyToOne
	private LSuserMaster modifiedby;

	public LSprojectmaster() {

	}

	public LSprojectmaster(String createdby, Integer projectcode, String projectname, String projectstatus,
			Integer status, String teamname) {
		this.createdby = createdby;
		this.projectcode = projectcode != null ? projectcode : -1;
		this.projectname = projectname;
		this.projectstatus = projectstatus;
		this.status = status != null ? status : -1;
		this.teamname = teamname;
	}

	public LSSiteMaster getLssitemaster() {
		return lssitemaster;
	}

	public void setLssitemaster(LSSiteMaster lssitemaster) {
		this.lssitemaster = lssitemaster;
	}

	public String getProjectstatus() {
		if (projectstatus != null) {
			return projectstatus.trim().equals("A") ? "Active" : "Retired";
		} else {
			return "";
		}
	}

	public void setProjectstatus(String projectstatus) {
		this.projectstatus = projectstatus;
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

	public String getCreatedby() {
		return createdby;
	}

	public void setCreatedby(String createdby) {
		this.createdby = createdby;
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

	public String getDuedate() {
		return duedate;
	}

	public void setDuedate(String duedate) {
		this.duedate = duedate;
	}

	public Date getStartdate() {
		return startdate;
	}

	public void setStartdate(Date startdate) {
		this.startdate = startdate;
	}

	public Date getEnddate() {
		return enddate;
	}

	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}

	public String getProjectid() {
		return projectid;
	}

	public void setProjectid(String projectid) {
		this.projectid = projectid;
	}

}
