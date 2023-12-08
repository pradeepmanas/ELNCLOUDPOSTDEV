package com.agaram.eln.primary.model.protocols;

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
import javax.persistence.Transient;

@Entity
@Table(name="LSprotocolorderversion")
public class LSprotocolorderversion {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "protocolorderversioncode")
	private Integer protocolorderversioncode;
	private Long protocolordercode;
//	public String protocolstepname;
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdate;
//	public Integer sharewithteam = 0;
//	@Column(columnDefinition = "varchar(120)")
	public String createdbyusername;
	
	public Integer status;
	
	private Integer versionno;
	
	private String versionname;
	
	public Integer createdby;

	public Integer getCreatedby() {
		return createdby;
	}

	public void setCreatedby(Integer createdby) {
		this.createdby = createdby;
	}

	public Integer getProtocolorderversioncode() {
		return protocolorderversioncode;
	}

	public Long getProtocolordercode() {
		return protocolordercode;
	}


	public Date getCreatedate() {
		return createdate;
	}

	public String getCreatedbyusername() {
		return createdbyusername;
	}

	public Integer getStatus() {
		return status;
	}

	public Integer getVersionno() {
		return versionno;
	}

	public String getVersionname() {
		return versionname;
	}

	public void setProtocolorderversioncode(Integer protocolorderversioncode) {
		this.protocolorderversioncode = protocolorderversioncode;
	}

	public void setProtocolordercode(Long protocolordercode) {
		this.protocolordercode = protocolordercode;
	}


	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public void setCreatedbyusername(String createdbyusername) {
		this.createdbyusername = createdbyusername;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public void setVersionno(Integer versionno) {
		this.versionno = versionno;
	}

	public void setVersionname(String versionname) {
		this.versionname = versionname;
	}
	
	@Transient
	@Temporal(TemporalType.TIMESTAMP)
	private Date fromdate; 
	
	@Transient
	@Temporal(TemporalType.TIMESTAMP)
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
	
	private String fileuid;
	
	private String fileuri;

	public String getFileuid() {
		return fileuid;
	}

	public void setFileuid(String fileuid) {
		this.fileuid = fileuid;
	}

	public String getFileuri() {
		return fileuri;
	}

	public void setFileuri(String fileuri) {
		this.fileuri = fileuri;
	}
	
}
