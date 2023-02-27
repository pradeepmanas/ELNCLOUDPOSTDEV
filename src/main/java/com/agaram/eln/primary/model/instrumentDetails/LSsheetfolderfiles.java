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
import javax.persistence.Transient;

import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;

@Entity(name = "LSsheetfolderfiles")
@Table(name = "LSsheetfolderfiles")
public class LSsheetfolderfiles {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "folderfilecode")
	private Integer folderfilecode;
	
	@Column(name = "directorycode",columnDefinition = "numeric(17,0)") 
	private Long directorycode;
	
	private String filefor;
	
	private String filename;
	
	private Long filesize;
	
	private String uuid;
	
	private Date createdtimestamp;
	
	@ManyToOne 
	private LSuserMaster createby;
	
	@ManyToOne 
	private LSSiteMaster lssitemaster;
	
	@Transient
	private Date fromdate;
	
	@Transient
	private Date todate;
	
	public Date getFromdate() {
		return fromdate;
	}

	public void setFromdate(Date fromdate) {
		this.fromdate = fromdate;
	}

	public Date getTodate() {
		return todate;
	}

	public void setTodate(Date todate) {
		this.todate = todate;
	}

	private Integer fileviewfor;

	public Integer getFolderfilecode() {
		return folderfilecode;
	}

	public void setFolderfilecode(Integer folderfilecode) {
		this.folderfilecode = folderfilecode;
	}

	public Long getDirectorycode() {
		return directorycode;
	}

	public void setDirectorycode(Long directorycode) {
		this.directorycode = directorycode;
	}

	public String getFilefor() {
		return filefor;
	}

	public void setFilefor(String filefor) {
		this.filefor = filefor;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public Long getFilesize() {
		return filesize;
	}

	public void setFilesize(Long filesize) {
		this.filesize = filesize;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Date getCreatedtimestamp() {
		return createdtimestamp;
	}

	public void setCreatedtimestamp(Date createdtimestamp) {
		this.createdtimestamp = createdtimestamp;
	}

	public LSuserMaster getCreateby() {
		return createby;
	}

	public void setCreateby(LSuserMaster createby) {
		this.createby = createby;
	}

	public LSSiteMaster getLssitemaster() {
		return lssitemaster;
	}

	public void setLssitemaster(LSSiteMaster lssitemaster) {
		this.lssitemaster = lssitemaster;
	}

	public Integer getFileviewfor() {
		return fileviewfor;
	}

	public void setFileviewfor(Integer fileviewfor) {
		this.fileviewfor = fileviewfor;
	}
	
	
}
