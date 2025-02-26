package com.agaram.eln.primary.model.material;

import java.util.Date;
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
@Table(name = "materialattachments")
public class MaterialAttachments {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "nmaterialattachcode")
	private Integer nmaterialattachcode;
	
	@Column(name = "nmaterialcode")
	private Integer nmaterialcode;

	@Column(name = "nmaterialcatcode")
	private Integer nmaterialcatcode; 
	
	@Column(name = "nmaterialtypecode")
	private Integer nmaterialtypecode;
	
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
	
	@Column(name = "filesize")
	private String filesize;

	public String getFilesize() {
		return filesize;
	}

	public void setFilesize(String filesize) {
		this.filesize = filesize;
	}
	
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
	public Integer getNmaterialattachcode() {
		return nmaterialattachcode;
	}
	public void setNmaterialattachcode(Integer nmaterialattachcode) {
		this.nmaterialattachcode = nmaterialattachcode;
	}
	
	public Integer getNmaterialtypecode() {
		return nmaterialtypecode;
	}
	public void setNmaterialtypecode(Integer nmaterialtypecode) {
		this.nmaterialtypecode = nmaterialtypecode;
	}
	public Integer getNmaterialcatcode() {
		return nmaterialcatcode;
	}
	public void setNmaterialcatcode(Integer nmaterialcatcode) {
		this.nmaterialcatcode = nmaterialcatcode;
	}
	public Integer getNmaterialcode() {
		return nmaterialcode;
	}
	public void setNmaterialcode(Integer nmaterialcode) {
		this.nmaterialcode = nmaterialcode;
	}
	public Integer getNstatus() {
		return nstatus;
	}
	public void setNstatus(Integer nstatus) {
		this.nstatus = nstatus;
	}
}
