package com.agaram.eln.primary.model.instrumentDetails;

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

import org.bson.types.Binary;

import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;

@Entity(name = "LsOrderattachments")
@Table(name = "LsOrderattachments")
public class LsOrderattachments {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "attachmentcode")
	private Long attachmentcode;
	
	@Column(columnDefinition = "varchar(250)", name = "filename")
	private String filename;
	
	@Column(columnDefinition = "varchar(10)", name = "fileextension")
	private String fileextension;
	
	@Column(columnDefinition = "varchar(250)", name = "fileid")
	private String fileid;
	
	@Column(columnDefinition = "numeric(17,0)",name = "batchcode") 
	private Long batchcode;
	
	private Integer islargefile;
	
//	@Column(columnDefinition = "date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdate;
	
//	@Column(columnDefinition = "date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifieddate;
	
	@ManyToOne 
	private LSuserMaster createby;
	
	@ManyToOne 
	private LSuserMaster modifiedby;
	
	@Transient
	private Binary file;
	
	@Transient
	private Response response;
	
	@Transient
	LScfttransaction objsilentaudit;
	
	public LScfttransaction getObjsilentaudit() {
		return objsilentaudit;
	}

	public void setObjsilentaudit(LScfttransaction objsilentaudit) {
		this.objsilentaudit = objsilentaudit;
	}

	public Long getAttachmentcode() {
		return attachmentcode;
	}

	public void setAttachmentcode(Long attachmentcode) {
		this.attachmentcode = attachmentcode;
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

	public Long getBatchcode() {
		return batchcode;
	}

	public void setBatchcode(Long batchcode) {
		this.batchcode = batchcode;
	}

	public Integer getIslargefile() {
		return islargefile;
	}

	public void setIslargefile(Integer islargefile) {
		this.islargefile = islargefile;
	}

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
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

	public Binary getFile() {
		return file;
	}

	public void setFile(Binary file) {
		this.file = file;
	}

	public Response getResponse() {
		return response;
	}

	public void setResponse(Response response) {
		this.response = response;
	}
	
}
