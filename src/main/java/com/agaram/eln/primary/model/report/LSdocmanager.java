package com.agaram.eln.primary.model.report;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "LSdocmanager")
public class LSdocmanager {
	
	@Id
	private Integer docmanagercode;
	@Column(columnDefinition = "varchar(120)")
	private String extention;
	@Column(columnDefinition = "varchar(120)")
	private String filehashname;
	@Column(columnDefinition = "varchar(120)")
	private String filename;
	@Column(columnDefinition = "varchar(120)")
	private String fileurl;
	private int Status;
	private int createdby;
	@Column(columnDefinition = "date")
	private String createdate;
	public Integer getDocmanagercode() {
		return docmanagercode;
	}
	public void setDocmanagercode(Integer docmanagercode) {
		this.docmanagercode = docmanagercode;
	}
	public String getExtention() {
		return extention;
	}
	public void setExtention(String extention) {
		this.extention = extention;
	}
	public String getFilehashname() {
		return filehashname;
	}
	public void setFilehashname(String filehashname) {
		this.filehashname = filehashname;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getFileurl() {
		return fileurl;
	}
	public void setFileurl(String fileurl) {
		this.fileurl = fileurl;
	}
	public int getStatus() {
		return Status;
	}
	public void setStatus(int status) {
		Status = status;
	}
	public int getCreatedby() {
		return createdby;
	}
	public void setCreatedby(int createdby) {
		this.createdby = createdby;
	}
	public String getCreatedate() {
		return createdate;
	}
	public void setCreatedate(String createdate) {
		this.createdate = createdate;
	}
	
}
