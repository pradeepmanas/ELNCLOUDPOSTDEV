package com.agaram.eln.primary.model.archieve;

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

@Entity
@Table(name = "lsprojectarchieve")
public class LsProjectarchieve {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "projectarchievecode") 
	private Integer projectarchievecode;

	@Temporal(TemporalType.TIMESTAMP)
	private Date modifieddate;
	
	@Column(columnDefinition = "varchar(100)")
	private String filenameuuid;
	
	@Column(columnDefinition = "varchar(100)")
	private String projectname;
	
	@ManyToOne 
	private LSuserMaster archieveby;
	
	@ManyToOne 
	private LSSiteMaster lssitemaster;
	
	@Transient
	private Response response;

	public Integer getProjectarchievecode() {
		return projectarchievecode;
	}

	public void setProjectarchievecode(Integer projectarchievecode) {
		this.projectarchievecode = projectarchievecode;
	}

	public Date getModifieddate() {
		return modifieddate;
	}

	public void setModifieddate(Date modifieddate) {
		this.modifieddate = modifieddate;
	}

	public String getFilenameuuid() {
		return filenameuuid;
	}

	public void setFilenameuuid(String filenameuuid) {
		this.filenameuuid = filenameuuid;
	}

	public LSuserMaster getArchieveby() {
		return archieveby;
	}

	public void setArchieveby(LSuserMaster archieveby) {
		this.archieveby = archieveby;
	}

	public String getProjectname() {
		return projectname;
	}

	public void setProjectname(String projectname) {
		this.projectname = projectname;
	}

	public Response getResponse() {
		return response;
	}

	public void setResponse(Response response) {
		this.response = response;
	}

	public LSSiteMaster getLssitemaster() {
		return lssitemaster;
	}

	public void setLssitemaster(LSSiteMaster lssitemaster) {
		this.lssitemaster = lssitemaster;
	}
	
	
}
