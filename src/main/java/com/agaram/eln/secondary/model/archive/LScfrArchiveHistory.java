package com.agaram.eln.secondary.model.archive;

import java.util.Date;
import java.util.List;

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

import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.usermanagement.LoggedUser;

@Entity
@Table(name = "LScfrArchiveHistory")
public class LScfrArchiveHistory {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "archivecode")
	private Integer archivecode;
	
//	@Column(columnDefinition = "date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date archivedate;
	
	@Column(columnDefinition = "varchar(255)")
	private String discription;
	
	private int archiveusercode;
	
	@Column(columnDefinition = "varchar(255)")
	private String archiveusername;
	
//	@OneToMany
//	@JoinColumn(name="archivecode")
	@Transient
	private List<LScfrachivetransaction> lscfrachivetransaction;
	
	@Transient
	LScfttransaction objsilentaudit;

	@Transient
	LScfttransaction objmanualaudit;
	@Transient
	LoggedUser objuser;
	
	public LoggedUser getObjuser() {
		return objuser;
	}
	public void setObjuser(LoggedUser objuser) {
		this.objuser = objuser;
	}
	
	public Integer getArchivecode() {
		return archivecode;
	}

	public void setArchivecode(Integer archivecode) {
		this.archivecode = archivecode;
	}

	public Date getArchivedate() {
		return archivedate;
	}

	public void setArchivedate(Date archivedate) {
		this.archivedate = archivedate;
	}

	public String getDiscription() {
		return discription;
	}

	public void setDiscription(String discription) {
		this.discription = discription;
	}

	public int getArchiveusercode() {
		return archiveusercode;
	}

	public void setArchiveusercode(int archiveusercode) {
		this.archiveusercode = archiveusercode;
	}

	public String getArchiveusername() {
		return archiveusername;
	}

	public void setArchiveusername(String archiveusername) {
		this.archiveusername = archiveusername;
	}
	
	public List<LScfrachivetransaction> getLscfrachivetransaction() {
		return lscfrachivetransaction;
	}

	public void setLscfrachivetransaction(List<LScfrachivetransaction> lscfrachivetransaction) {
		this.lscfrachivetransaction = lscfrachivetransaction;
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

}
