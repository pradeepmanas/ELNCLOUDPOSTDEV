package com.agaram.eln.primary.model.sample;

import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;

@Entity
@Table(name = "sampleattachments")
public class SampleAttachments {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "nsampleattachcode")
	private Integer nsampleattachcode;

	@Column(name = "samplecode")
	private Integer samplecode;

	@Column(name = "nsamplecatcode")
	private Integer nsamplecatcode;

	@Column(name = "nsampletypecode")
	private Integer nsampletypecode;

	@Column(name = "nstatus")
	private Integer nstatus;

	@ManyToOne
	private LSuserMaster createby;

	private Date createddate;

	private Integer nsitecode;

	@Column(columnDefinition = "varchar(250)", name = "filename")
	private String filename;

	@Column(columnDefinition = "varchar(10)", name = "fileextension")
	private String fileextension;

	@Column(columnDefinition = "varchar(250)", name = "fileid")
	private String fileid;

	public LSuserMaster getCreateby() {
		return createby;
	}

	public void setCreateby(LSuserMaster createby) {
		this.createby = createby;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getFileextension() {
		return fileextension;
	}

	public void setFileextension(String fileextension) {
		this.fileextension = fileextension;
	}

	public String getFileid() {
		return fileid;
	}

	public void setFileid(String fileid) {
		this.fileid = fileid;
	}

	public Date getCreateddate() {
		return createddate;
	}

	public void setCreateddate(Date createddate) {
		this.createddate = createddate;
	}

	public Integer getNsitecode() {
		return nsitecode;
	}

	public void setNsitecode(Integer nsitecode) {
		this.nsitecode = nsitecode;
	}

	public Integer getNsampleattachcode() {
		return nsampleattachcode;
	}

	public void setNsampleattachcode(Integer nsampleattachcode) {
		this.nsampleattachcode = nsampleattachcode;
	}

	public Integer getNsampletypecode() {
		return nsampletypecode;
	}

	public void setNsampletypecode(Integer nsampletypecode) {
		this.nsampletypecode = nsampletypecode;
	}

	public Integer getNsamplecatcode() {
		return nsamplecatcode;
	}

	public void setNsamplecatcode(Integer nsamplecatcode) {
		this.nsamplecatcode = nsamplecatcode;
	}

	public Integer getSamplecode() {
		return samplecode;
	}

	public void setSamplecode(Integer samplecode) {
		this.samplecode = samplecode;
	}

	public Integer getNstatus() {
		return nstatus;
	}

	public void setNstatus(Integer nstatus) {
		this.nstatus = nstatus;
	}
}
