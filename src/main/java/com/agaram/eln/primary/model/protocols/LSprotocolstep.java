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
import javax.persistence.Transient;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import com.agaram.eln.primary.model.general.Response;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonStringType;

@Entity
@Table(name="LSprotocolstep")
@TypeDefs({
    @TypeDef(name = "json", typeClass = JsonStringType.class),
    @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
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
//	@Column(columnDefinition = "nvarchar(255)")
	private String modifiedusername;
//	public String modifiedusername;
	public Integer sitecode;
	
	@Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
	public String timer;
	public String getTimer() {
		return timer;
	}
	public void setTimer(String timer) {
		this.timer = timer;
	}

	@Transient
	private Integer newStep;
	
	@Transient
	private LSprotocolmaster lsprotocolmaster;
	
	@Transient
	private Boolean isversion;
	
	@Transient
	private Integer versionno;
	@Transient
	public String lsprotocolstepInformation;
	
	@Transient
	private Response response;
	
	

	public Boolean getIsversion() {
		return isversion;
	}
	public void setIsversion(Boolean isversion) {
		this.isversion = isversion;
	}
	public LSprotocolmaster getLsprotocolmaster() {
		return lsprotocolmaster;
	}
	public void setLsprotocolmaster(LSprotocolmaster lsprotocolmaster) {
		this.lsprotocolmaster = lsprotocolmaster;
	}
	public String getModifiedusername() {
		return modifiedusername;
	}
	public void setModifiedusername(String modifiedusername) {
		this.modifiedusername = modifiedusername;
	}
	public Response getResponse() {
		return response;
	}
	public void setResponse(Response response) {
		this.response = response;
	}
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
