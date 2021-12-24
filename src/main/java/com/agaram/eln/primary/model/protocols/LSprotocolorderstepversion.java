package com.agaram.eln.primary.model.protocols;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="LSprotocolorderstepversion")
public class LSprotocolorderstepversion {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer protocolorderstepversioncode;

	public Integer protocolorderstepcode;
	private Long protocolordercode;
	public Integer protocolmastercode;
	public Integer stepno;
	public String protocolstepname;
	
	public Integer status;
	
	private Integer versionno;
	
	private String versionname;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdate;
	public Integer sharewithteam = 0;
	@Column(columnDefinition = "varchar(120)")
	public String createdbyusername;
	
	private Integer approved;
	
	private Integer rejected;

	public String getVersionname() {
		return versionname;
	}

	public Date getCreatedate() {
		return createdate;
	}

	public Integer getSharewithteam() {
		return sharewithteam;
	}

	public String getCreatedbyusername() {
		return createdbyusername;
	}

	public Integer getApproved() {
		return approved;
	}

	public Integer getRejected() {
		return rejected;
	}

	public void setVersionname(String versionname) {
		this.versionname = versionname;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public void setSharewithteam(Integer sharewithteam) {
		this.sharewithteam = sharewithteam;
	}

	public void setCreatedbyusername(String createdbyusername) {
		this.createdbyusername = createdbyusername;
	}

	public void setApproved(Integer approved) {
		this.approved = approved;
	}

	public void setRejected(Integer rejected) {
		this.rejected = rejected;
	}

	public Integer getProtocolorderstepversioncode() {
		return protocolorderstepversioncode;
	}

	public Integer getProtocolorderstepcode() {
		return protocolorderstepcode;
	}

	public Long getProtocolordercode() {
		return protocolordercode;
	}

	public Integer getProtocolmastercode() {
		return protocolmastercode;
	}

	public Integer getStepno() {
		return stepno;
	}

	public String getProtocolstepname() {
		return protocolstepname;
	}

	public Integer getStatus() {
		return status;
	}

	public Integer getVersionno() {
		return versionno;
	}

	public void setProtocolorderstepversioncode(Integer protocolorderstepversioncode) {
		this.protocolorderstepversioncode = protocolorderstepversioncode;
	}

	public void setProtocolorderstepcode(Integer protocolorderstepcode) {
		this.protocolorderstepcode = protocolorderstepcode;
	}

	public void setProtocolordercode(Long protocolordercode) {
		this.protocolordercode = protocolordercode;
	}

	public void setProtocolmastercode(Integer protocolmastercode) {
		this.protocolmastercode = protocolmastercode;
	}

	public void setStepno(Integer stepno) {
		this.stepno = stepno;
	}

	public void setProtocolstepname(String protocolstepname) {
		this.protocolstepname = protocolstepname;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public void setVersionno(Integer versionno) {
		this.versionno = versionno;
	}
}
