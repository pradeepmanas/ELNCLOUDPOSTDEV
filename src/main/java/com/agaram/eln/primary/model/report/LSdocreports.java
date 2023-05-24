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
@Table(name = "LSdocreports")
public class LSdocreports {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Basic(optional = false)
	private Integer docReportsCode;
	private Integer docdirectorycode;
	@Column(columnDefinition = "varchar(10)")
	private String extention;
	@Column(columnDefinition = "varchar(120)")
	private String fileHashName;
	@Column(columnDefinition = "varchar(120)")
	private String fileName;
	private Integer isTemplate = 0;
	private Integer isdraft = 0;
//	@ManyToOne
//	private LSSiteMaster lssitemaster;
	@Column(columnDefinition = "varchar(120)")
	private String streamid;
	private Integer isreport;
	private Integer ismultiplesheet;
	private Integer status;
	private String sheetfilecodeString = "";
	private Integer versionno;
	private Integer createdBy;
	@Column(columnDefinition = "date")
	@Temporal(TemporalType.DATE)
	private Date createdate;
	
	
	public String getStreamid() {
		return streamid;
	}
	public void setStreamid(String streamid) {
		this.streamid = streamid;
	}
	public Integer getIsreport() {
		return isreport;
	}
	public void setIsreport(Integer isreport) {
		this.isreport = isreport;
	}
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
	public Integer getDocReportsCode() {
		return docReportsCode;
	}
	public void setDocReportsCode(Integer docReportsCode) {
		this.docReportsCode = docReportsCode;
	}
	public Integer getDocdirectorycode() {
		return docdirectorycode;
	}
	public void setDocdirectorycode(Integer docdirectorycode) {
		this.docdirectorycode = docdirectorycode;
	}
	public String getExtention() {
		return extention;
	}
	public void setExtention(String extention) {
		this.extention = extention;
	}
	public String getFileHashName() {
		return fileHashName;
	}
	public void setFileHashName(String fileHashName) {
		this.fileHashName = fileHashName;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}
	public Date getCreatedate() {
		return createdate;
	}
	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}
	public Integer getVersionno() {
		return versionno;
	}
	public Integer getIsTemplate() {
		return isTemplate;
	}
	public void setIsTemplate(Integer isTemplate) {
		this.isTemplate = isTemplate;
	}
	public Integer getIsdraft() {
		return isdraft;
	}
	public void setIsdraft(Integer isdraft) {
		this.isdraft = isdraft;
	}
	public String getSheetfilecodeString() {
		return sheetfilecodeString;
	}
	public void setSheetfilecodeString(String sheetfilecodeString) {
		this.sheetfilecodeString = sheetfilecodeString;
	}
	public void setVersionno(Integer versionno) {
		this.versionno = versionno;
	}
	public Integer getIsmultiplesheet() {
		return ismultiplesheet;
	}
	public void setIsmultiplesheet(Integer ismultiplesheet) {
		this.ismultiplesheet = ismultiplesheet;
	}

}
