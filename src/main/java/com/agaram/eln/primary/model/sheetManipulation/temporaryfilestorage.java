package com.agaram.eln.primary.model.sheetManipulation;

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
@Table(name = "temporaryfilestorage")
public class temporaryfilestorage {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "uploadedcode")
	private Integer uploadedcode;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date uploadedbydate;
	
	@Column(columnDefinition = "varchar(100)")
	private String modifieduser;
	
	@Column(columnDefinition = "varchar(100)")
	private String id;
	
	private Integer sitecode;

	public Integer getUploadedcode() {
		return uploadedcode;
	}

	public void setUploadedcode(Integer uploadedcode) {
		this.uploadedcode = uploadedcode;
	}

	public Date getUploadedbydate() {
		return uploadedbydate;
	}

	public void setUploadedbydate(Date uploadedbydate) {
		this.uploadedbydate = uploadedbydate;
	}

	public String getModifieduser() {
		return modifieduser;
	}

	public void setModifieduser(String modifieduser) {
		this.modifieduser = modifieduser;
	}

	public Integer getSitecode() {
		return sitecode;
	}

	public void setSitecode(Integer sitecode) {
		this.sitecode = sitecode;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
