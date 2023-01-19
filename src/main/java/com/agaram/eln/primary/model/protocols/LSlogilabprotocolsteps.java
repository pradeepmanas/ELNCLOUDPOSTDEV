package com.agaram.eln.primary.model.protocols;

import java.util.Date;
import java.util.List;

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
@Table(name="LSlogilabprotocolsteps")
@TypeDefs({
    @TypeDef(name = "json", typeClass = JsonStringType.class),
    @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
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
	public String modifiedusername;
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
	
	@Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
	public String timer;
	public String getTimer() {
		return timer;
	}
	public void setTimer(String timer) {
		this.timer = timer;
	}
	
	public Integer skipdata;
	public Integer getSkipdata() {
		return skipdata;
	}
	public void setSkipdata(Integer skipdata) {
		this.skipdata = skipdata;
	}
	@Transient
	private Integer versionno;
	
	@Transient
	public String lsprotocolstepInformation;
	
	@Transient
	private Response response;
	
	@Transient
	private List<LSprotocolorderimages> lsprotocolorderimages;
	
	@Transient
	private Integer multitenent;
	
	@Transient
	private LSprotocolmaster lsprotocolmaster;
	
	@Transient
	private Boolean isversion;
	
	
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
	public Integer getMultitenent() {
		return multitenent;
	}
	public void setMultitenent(Integer multitenent) {
		this.multitenent = multitenent;
	}
	public String getModifiedusername() {
		return modifiedusername;
	}
	public void setModifiedusername(String modifiedusername) {
		this.modifiedusername = modifiedusername;
	}
	public List<LSprotocolorderimages> getLsprotocolorderimages() {
		return lsprotocolorderimages;
	}
	public void setLsprotocolorderimages(List<LSprotocolorderimages> lsprotocolorderimages) {
		this.lsprotocolorderimages = lsprotocolorderimages;
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
	@Transient 
	private Integer protocolstepversioncode;
	
	public Integer getProtocolstepversioncode() {
		return protocolstepversioncode;
	}
	public void setProtocolstepversioncode(Integer protocolstepversioncode) {
		this.protocolstepversioncode = protocolstepversioncode;
	}
	
	public Integer getVersionno() {
		return versionno;
	}
	public void setVersionno(Integer versionno) {
		this.versionno = versionno;
	}
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
