package com.agaram.eln.primary.model.report;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "LSdocdirectory")
public class LSdocdirectory {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Basic(optional = false)
	private Integer docdirectorycode;
	@Column(columnDefinition = "varchar(120)")
	private String directoryname;
	private Integer directorytype;
	private Integer parentdirectory;
	private Integer createdby;
//	@Column(columnDefinition = "date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdate;
	private Integer status;
//	@ManyToOne
//	private LSSiteMaster lssitemaster;
//	public LSSiteMaster getLssitemaster() {
//		return lssitemaster;
//	}
//	public void setLssitemaster(LSSiteMaster lssitemaster) {
//		this.lssitemaster = lssitemaster;
//	}
	@Column(name="lssitemaster_sitecode")
	private Integer lssitemaster;
	
	public Integer getLssitemaster() {
		return lssitemaster;
	}
	public void setLssitemaster(Integer lssitemaster) {
		this.lssitemaster = lssitemaster;
	}
	public Integer getDocdirectorycode() {
		return docdirectorycode;
	}
	public void setDocdirectorycode(Integer docdirectorycode) {
		this.docdirectorycode = docdirectorycode;
	}
	public String getDirectoryname() {
		return directoryname;
	}
	public void setDirectoryname(String directoryname) {
		this.directoryname = directoryname;
	}
	public Integer getDirectorytype() {
		return directorytype;
	}
	public void setDirectorytype(Integer directorytype) {
		this.directorytype = directorytype;
	}
	public Integer getParentdirectory() {
		return parentdirectory;
	}
	public void setParentdirectory(Integer parentdirectory) {
		this.parentdirectory = parentdirectory;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getCreatedby() {
		return createdby;
	}
	public void setCreatedby(Integer createdby) {
		this.createdby = createdby;
	}
	public Date getCreatedate() {
		return createdate;
	}
	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}
	
}
