package com.agaram.eln.primary.model.protocols;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name="LSprotocolstep")
public class LSprotocolstep {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer protocolstepcode;
	public Integer protocolmastercode;
	public Integer stepno;
	public String protocolstepname;
	public Integer createdby;
//	@Column(columnDefinition = "date")
	@Temporal(TemporalType.TIMESTAMP)
	public Date createddate;
	public Integer status;
	@Transient
	public String lsprotocolstepInfo;
	@Transient
	private Integer ismultitenant;
	public String createdbyusername;
	public Integer sitecode;
	@Transient
	private Integer newStep;
	@Transient
	private Integer versionno;
	@Transient
	public String lsprotocolstepInformation;
	
	public String getLsprotocolstepInformation() {
		return lsprotocolstepInformation;
	}
	public void setLsprotocolstepInformation(String lsprotocolstepInformation) {
		this.lsprotocolstepInformation = lsprotocolstepInformation;
	}
	public Integer getVersionno() {
		return versionno;
	}
	public void setVersionno(Integer versionno) {
		this.versionno = versionno;
	}
	public Integer getNewStep() {
		return newStep;
	}
	public void setNewStep(Integer newStep) {
		this.newStep = newStep;
	}
	public Integer getIsmultitenant() {
		return ismultitenant;
	}
	public void setIsmultitenant(Integer ismultitenant) {
		this.ismultitenant = ismultitenant;
	}
	public Integer getProtocolstepcode() {
		return protocolstepcode;
	}
	public void setProtocolstepcode(Integer protocolstepcode) {
		this.protocolstepcode = protocolstepcode;
	}
	public Integer getProtocolmastercode() {
		return protocolmastercode;
	}
	public void setProtocolmastercode(Integer protocolmastercode) {
		this.protocolmastercode = protocolmastercode;
	}
	public Integer getStepno() {
		return stepno;
	}
	public void setStepno(Integer stepno) {
		this.stepno = stepno;
	}
	public String getProtocolstepname() {
		return protocolstepname;
	}
	public void setProtocolstepname(String protocolstepname) {
		this.protocolstepname = protocolstepname;
	}
	public Integer getCreatedby() {
		return createdby;
	}
	public void setCreatedby(Integer createdby) {
		this.createdby = createdby;
	}
	public Date getCreateddate() {
		return createddate;
	}
	public void setCreateddate(Date createddate) {
		this.createddate = createddate;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getLsprotocolstepInfo() {
		return lsprotocolstepInfo;
	}
	public void setLsprotocolstepInfo(String lsprotocolstepInfo) {
		this.lsprotocolstepInfo = lsprotocolstepInfo;
	}
	public String getCreatedbyusername() {
		return createdbyusername;
	}
	public void setCreatedbyusername(String createdbyusername) {
		this.createdbyusername = createdbyusername;
	}
	public Integer getSitecode() {
		return sitecode;
	}
	public void setSitecode(Integer sitecode) {
		this.sitecode = sitecode;
	}
	
	@Transient 
	private Integer protocolstepversioncode;
	
	public Integer getProtocolstepversioncode() {
		return protocolstepversioncode;
	}
	public void setProtocolstepversioncode(Integer protocolstepversioncode) {
		this.protocolstepversioncode = protocolstepversioncode;
	}
	
}
