package com.agaram.eln.primary.model.sheetManipulation;

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

import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "LSsamplefileversion")
public class LSsamplefileversion {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "filesamplecodeversion")
	private Integer filesamplecodeversion;
	
	@JsonIgnore
	@ManyToOne
	private LSsamplefile filesamplecode;
	
	//@Lob
	@Column(columnDefinition = "text")
	private String filecontent;
	@Column(columnDefinition = "numeric(17,0)")
	private long batchcode;
	
	private Integer testid;
	@Column(columnDefinition = "varchar(150)")
	private String resetflag;
	
	private Integer versionno;
	@SuppressWarnings("unused")
	private String versionname;
	
	private Integer createby;
//	@Column(columnDefinition = "date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdate;
//	@Column(columnDefinition = "date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifieddate;
	private Integer processed;
	
	@ManyToOne 
	private LSuserMaster createbyuser;
	
	@ManyToOne 
	private LSuserMaster modifiedby;
	
	public LSsamplefileversion()
	{
		
	}
	
	public LSsamplefileversion(Integer filesamplecodeversion,Integer testid,Integer versionno, String versionname,Date createdate, Date modifieddate,LSuserMaster createbyuser)
	{
		this.filesamplecodeversion = filesamplecodeversion;
		this.testid = testid;
		this.versionno = versionno;
		this.versionname = versionname;
		this.createdate = createdate;
		this.modifieddate = modifieddate;
		this.createbyuser = createbyuser;
	}
	
	public Integer getFilesamplecodeversion() {
		return filesamplecodeversion;
	}
	public void setFilesamplecodeversion(Integer filesamplecodeversion) {
		this.filesamplecodeversion = filesamplecodeversion;
	}
	
	public String getFilecontent() {
		return filecontent;
	}
	public void setFilecontent(String filecontent) {
		this.filecontent = filecontent;
	}
	public long getBatchcode() {
		return batchcode;
	}
	public void setBatchcode(long batchcode) {
		this.batchcode = batchcode;
	}
	public Integer getTestid() {
		return testid;
	}
	public void setTestid(Integer testid) {
		this.testid = testid;
	}
	public String getResetflag() {
		return resetflag;
	}
	public void setResetflag(String resetflag) {
		this.resetflag = resetflag;
	}
	public Integer getVersionno() {
		return versionno;
	}
	public void setVersionno(Integer versionno) {
		this.versionno = versionno;
	}
	public String getVersionname() {
		return "version_"+this.getVersionno();
	}
	public void setVersionname(String versionname) {
		this.versionname = versionname;
	}
	public Integer getCreateby() {
		return createby;
	}
	public void setCreateby(Integer createby) {
		this.createby = createby;
	}
	public Date getCreatedate() {
		return createdate;
	}
	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}
	public Integer getProcessed() {
		return processed;
	}
	public void setProcessed(Integer processed) {
		this.processed = processed;
	}
	public Date getModifieddate() {
		return modifieddate;
	}
	public void setModifieddate(Date modifieddate) {
		this.modifieddate = modifieddate;
	}
	public LSuserMaster getCreatebyuser() {
		return createbyuser;
	}
	public void setCreatebyuser(LSuserMaster createbyuser) {
		this.createbyuser = createbyuser;
	}
	public LSuserMaster getModifiedby() {
		return modifiedby;
	}
	public void setModifiedby(LSuserMaster modifiedby) {
		this.modifiedby = modifiedby;
	}
	public LSsamplefile getFilesamplecode() {
		return filesamplecode;
	}
	public void setFilesamplecode(LSsamplefile filesamplecode) {
		this.filesamplecode = filesamplecode;
	}
	
}
