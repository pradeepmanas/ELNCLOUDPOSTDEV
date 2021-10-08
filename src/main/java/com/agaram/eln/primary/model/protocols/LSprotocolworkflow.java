package com.agaram.eln.primary.model.protocols;

import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
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
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.model.usermanagement.LoggedUser;

@Entity
@Table(name = "lsprotocolworkflow")
public class LSprotocolworkflow {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "workflowcode") 
	private int workflowcode;;

	@Column(columnDefinition = "varchar(120)")
	private String workflowname;
	
	@OneToMany
	@JoinColumn(name="workflowcode")
	private List<LSprotocolworkflowgroupmap> lsprotocolworkflowgroupmap;
	
	@Transient
	private LScfttransaction objsilentaudit;
	
	@Transient
	private LScfttransaction objmanualaudit;
	
	@Transient
	private Response response;
	
	@ManyToOne 
	private LSSiteMaster lssitemaster;
	
	@Transient
	private String sitename;
	
	@Transient 
	private LSuserMaster lsusermaster;
	
	public LSuserMaster getLsusermaster() {
		return lsusermaster;
	}

	public void setLsusermaster(LSuserMaster lsusermaster) {
		this.lsusermaster = lsusermaster;
	}

	public LSSiteMaster getLssitemaster() {
		return lssitemaster;
	}

	public void setLssitemaster(LSSiteMaster lssitemaster) {
		this.lssitemaster = lssitemaster;
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

	public int getWorkflowcode() {
		return workflowcode;
	}

	public void setWorkflowcode(int workflowcode) {
		this.workflowcode = workflowcode;
	}

	public String getWorkflowname() {
		return workflowname;
	}

	public void setWorkflowname(String workflowname) {
		this.workflowname = workflowname;
	}

	public List<LSprotocolworkflowgroupmap> getLsprotocolworkflowgroupmap() {
		return lsprotocolworkflowgroupmap;
	}

	public void setLsprotocolworkflowgroupmap(List<LSprotocolworkflowgroupmap> lsprotocolworkflowgroupmap) {
		this.lsprotocolworkflowgroupmap = lsprotocolworkflowgroupmap;
	}

	public String getSitename() {
		if(this.lssitemaster != null)
		{
			return this.lssitemaster.getSitename();
		}
		else
		{
			return "";
		}
	}
	public void setSitename(String sitename) {
		this.sitename = sitename;
	}
	public LScfttransaction getObjmanualaudit() {
		return objmanualaudit;
	}

	public void setObjmanualaudit(LScfttransaction objmanualaudit) {
		this.objmanualaudit = objmanualaudit;
	}
}
