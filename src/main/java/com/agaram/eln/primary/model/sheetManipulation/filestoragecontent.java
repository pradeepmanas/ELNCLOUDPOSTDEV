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
@Table(name = "filestoragecontent")
public class filestoragecontent {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "storagecode")
	private Integer storagecode;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifieddate;
	
	@Column(columnDefinition = "varchar(100)")
	private String modifieduser;
	
	@Column(columnDefinition = "varchar(100)")
	private String id;
	
	@Column(columnDefinition = "varchar(100)")
	private String imguniqueid;
	
	private Integer filecode;
	
	private Integer versionno;
	
	private Integer sitecode;

	public Integer getStoragecode() {
		return storagecode;
	}

	public void setStoragecode(Integer storagecode) {
		this.storagecode = storagecode;
	}

	public Date getModifieddate() {
		return modifieddate;
	}

	public void setModifieddate(Date modifieddate) {
		this.modifieddate = modifieddate;
	}

	public String getModifieduser() {
		return modifieduser;
	}

	public void setModifieduser(String modifieduser) {
		this.modifieduser = modifieduser;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getImguniqueid() {
		return imguniqueid;
	}

	public void setImguniqueid(String imguniqueid) {
		this.imguniqueid = imguniqueid;
	}

	public Integer getFilecode() {
		return filecode;
	}

	public void setFilecode(Integer filecode) {
		this.filecode = filecode;
	}

	public Integer getVersionno() {
		return versionno;
	}

	public void setVersionno(Integer versionno) {
		this.versionno = versionno;
	}

	public Integer getSitecode() {
		return sitecode;
	}

	public void setSitecode(Integer sitecode) {
		this.sitecode = sitecode;
	}
}
