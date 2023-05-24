package com.agaram.eln.primary.model.sheetManipulation;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "LSMultidocument")
public class LSMultiDocument {
	@Id
	private Integer filecode;
	@Column(columnDefinition = "varchar(100)")
	private String filename;
	@Column(columnDefinition = "varchar(100)")
	private String userid;
	private Integer versionno;
	@Column(columnDefinition = "date")
	private String createddate;
	private Integer filetypecode;
	private Integer Stepcount;
	public Integer getFilecode() {
		return filecode;
	}
	public void setFilecode(Integer filecode) {
		this.filecode = filecode;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public Integer getVersionno() {
		return versionno;
	}
	public void setVersionno(Integer versionno) {
		this.versionno = versionno;
	}
	public String getCreateddate() {
		return createddate;
	}
	public void setCreateddate(String createddate) {
		this.createddate = createddate;
	}
	public Integer getFiletypecode() {
		return filetypecode;
	}
	public void setFiletypecode(Integer filetypecode) {
		this.filetypecode = filetypecode;
	}
	public Integer getStepcount() {
		return Stepcount;
	}
	public void setStepcount(Integer stepcount) {
		Stepcount = stepcount;
	}
}
