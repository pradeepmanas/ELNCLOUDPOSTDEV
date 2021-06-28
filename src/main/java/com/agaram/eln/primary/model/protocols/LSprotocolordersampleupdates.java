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

@Entity
@Table(name="LSprotocolordersampleupdates")
public class LSprotocolordersampleupdates {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Basic(optional = false)
	public Integer protocolsamplecode;
	public String protocolsampletype;
	public String protocolsample;
	public String protocolsampleusedDetail;
	public Integer protocolstepcode;
	public Integer protocolmastercode;
	@Temporal(TemporalType.TIMESTAMP)
	public Date createddate;
	public Integer usercode;
	@Column(columnDefinition = "numeric(17,0)",name = "Protocolordercode") 
	private Long protocolordercode;
	
	public Integer getProtocolsamplecode() {
		return protocolsamplecode;
	}
	public void setProtocolsamplecode(Integer protocolsamplecode) {
		this.protocolsamplecode = protocolsamplecode;
	}
	public String getProtocolsampletype() {
		return protocolsampletype;
	}
	public void setProtocolsampletype(String protocolsampletype) {
		this.protocolsampletype = protocolsampletype;
	}
	public String getProtocolsample() {
		return protocolsample;
	}
	public void setProtocolsample(String protocolsample) {
		this.protocolsample = protocolsample;
	}
	public String getProtocolsampleusedDetail() {
		return protocolsampleusedDetail;
	}
	public void setProtocolsampleusedDetail(String protocolsampleusedDetail) {
		this.protocolsampleusedDetail = protocolsampleusedDetail;
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
	public Date getCreateddate() {
		return createddate;
	}
	public void setCreateddate(Date createddate) {
		this.createddate = createddate;
	}
	public Integer getUsercode() {
		return usercode;
	}
	public void setUsercode(Integer usercode) {
		this.usercode = usercode;
	}
	public Long getProtocolordercode() {
		return protocolordercode;
	}
	public void setProtocolordercode(Long protocolordercode) {
		this.protocolordercode = protocolordercode;
	}
	
	
}
