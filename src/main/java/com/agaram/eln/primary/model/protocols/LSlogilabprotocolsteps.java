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
@Table(name="LSlogilabprotocolsteps")
public class LSlogilabprotocolsteps {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer protocolorderstepcode;
	private Long protocolordercode;
	public Integer protocolstepcode;
	public Integer protocolmastercode;
	public Integer stepno;
	public String protocolstepname;
	public Integer createdby;
	@Temporal(TemporalType.TIMESTAMP)
	public Date createddate;
	public Integer status;
	@Transient
	public String lsprotocolstepInfo;
	@Transient
	private Integer ismultitenant;
	public String createdbyusername;
	@Transient
	private Integer newStep;
	private Integer sitecode;
	private String orderstepflag;
	public Integer getProtocolorderstepcode() {
		return protocolorderstepcode;
	}
	public void setProtocolorderstepcode(Integer protocolorderstepcode) {
		this.protocolorderstepcode = protocolorderstepcode;
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
	public Integer getIsmultitenant() {
		return ismultitenant;
	}
	public void setIsmultitenant(Integer ismultitenant) {
		this.ismultitenant = ismultitenant;
	}
	public String getCreatedbyusername() {
		return createdbyusername;
	}
	public void setCreatedbyusername(String createdbyusername) {
		this.createdbyusername = createdbyusername;
	}
	public Integer getNewStep() {
		return newStep;
	}
	public void setNewStep(Integer newStep) {
		this.newStep = newStep;
	}
	public Long getProtocolordercode() {
		return protocolordercode;
	}
	public void setProtocolordercode(Long protocolordercode) {
		this.protocolordercode = protocolordercode;

	}
	public Integer getSitecode() {
		return sitecode;
	}
	public void setSitecode(Integer sitecode) {
		this.sitecode = sitecode;
	}
	public String getOrderstepflag() {
		return orderstepflag;
	}
	public void setOrderstepflag(String orderstepflag) {
		this.orderstepflag = orderstepflag;
	}
	
}
