package com.agaram.eln.primary.model.usermanagement;

import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.general.Response;

@Entity
@Table(name = "LSusersteam")
public class LSusersteam {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "teamcode")
	private Integer teamcode;
	@Column(columnDefinition = "varchar(100)")
	private String teamname;
	private Integer status;
	@ManyToOne 
	private LSSiteMaster lssitemaster;
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name="teamcode")
	private List<LSuserteammapping> lsuserteammapping;
	
	@Transient
	private List<LSuserMaster> lsuserMaster;
	
	@Transient
	LScfttransaction Objmanualaudit;
	
	
	@Transient
	private LSuserMaster selectedrecord;
	

	public LSuserMaster getSelectedrecord() {
		return selectedrecord;
	}

	public void setSelectedrecord(LSuserMaster selectedrecord) {
		this.selectedrecord = selectedrecord;
	}

	@Column(columnDefinition = "date")
	private Date createdate;

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	@Column(columnDefinition = "date")
	private Date modifieddate;

	@ManyToOne
	private LSuserMaster createby;
	
	@ManyToOne
	private LSuserMaster modifiedby;
	
	public LScfttransaction getObjmanualaudit() {
		return Objmanualaudit;
	}

	public void setObjmanualaudit(LScfttransaction objmanualaudit) {
		Objmanualaudit = objmanualaudit;
	}
	
	public LSSiteMaster getLssitemaster() {
		return lssitemaster;
	}

	public void setLssitemaster(LSSiteMaster lssitemaster) {
		this.lssitemaster = lssitemaster;
	}

	@Column(columnDefinition = "varchar(20)")
	private String projectteamstatus;
	
	public String getProjectteamstatus() {
		if(projectteamstatus != null)
		{
		return  projectteamstatus.trim().equals("A")?"Active":"Retired";
		}
		else
		{
			return "";
		}
	}

	public void setProjectteamstatus(String projectteamstatus) {
		this.projectteamstatus = projectteamstatus;
	}
	
	@Transient
	LoggedUser objuser;
	
	@Transient
	LSuserMaster modifieduserMaster;
	
	@Transient
	LScfttransaction objsilentaudit;

	public LSuserMaster getModifieduserMaster() {
		return modifieduserMaster;
	}

	public void setModifieduserMaster(LSuserMaster modifieduserMaster) {
		this.modifieduserMaster = modifieduserMaster;
	}

	public LoggedUser getObjuser() {
		return objuser;
	}

	public void setObjuser(LoggedUser objuser) {
		this.objuser = objuser;
	}

	public Response getResponse() {
		return response;
	}

	public void setResponse(Response response) {
		this.response = response;
	}
	@Transient
	private Response response;
	
	public LScfttransaction getObjsilentaudit() {
		return objsilentaudit;
	}

	public void setObjsilentaudit(LScfttransaction objsilentaudit) {
		this.objsilentaudit = objsilentaudit;
	}
	
	public Integer getTeamcode() {
		return teamcode;
	}
	public void setTeamcode(Integer teamcode) {
		this.teamcode = teamcode;
	}
	public String getTeamname() {
		return teamname;
	}
	public void setTeamname(String teamname) {
		this.teamname = teamname;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public List<LSuserMaster> getLsuserMaster() {
		return lsuserMaster;
	}
	public void setLsuserMaster(List<LSuserMaster> lsuserMaster) {
		this.lsuserMaster = lsuserMaster;
	}
	public List<LSuserteammapping> getLsuserteammapping() {
		return lsuserteammapping;
	}
	public void setLsuserteammapping(List<LSuserteammapping> lsuserteammapping) {
		this.lsuserteammapping = lsuserteammapping;
	}

	
	public Date getModifieddate() {
		return modifieddate;
	}

	public void setModifieddate(Date modifieddate) {
		this.modifieddate = modifieddate;
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

	public LSMultiusergroup getObjLoggeduser() {
		// TODO Auto-generated method stub
		return null;
	}

	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	public LSusersteam get(int i) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
