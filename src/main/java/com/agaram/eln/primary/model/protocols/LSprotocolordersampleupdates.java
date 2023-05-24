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
@Table(name = "LSprotocolordersampleupdates")
public class LSprotocolordersampleupdates {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
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
	public Integer repositorydatacode;
	@Column(columnDefinition = "numeric(17,0)", name = "Protocolordercode")
	private Long protocolordercode;
	public Integer indexof;
	public Integer status;
	public String consumefieldkey;
	public Integer usedquantity;
	public String unit;
	@Transient
	public Integer inventoryconsumed;
	@Transient
	public String inventoryid;

	public String getInventoryid() {
		return inventoryid;
	}

	public void setInventoryid(String inventoryid) {
		this.inventoryid = inventoryid;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Integer getRepositorydatacode() {
		return repositorydatacode;
	}

	public void setRepositorydatacode(Integer repositorydatacode) {
		this.repositorydatacode = repositorydatacode;
	}

	public Integer getIndexof() {
		return indexof;
	}

	public Integer getStatus() {
		return status;
	}

	public String getConsumefieldkey() {
		return consumefieldkey;
	}

	public Integer getUsedquantity() {
		return usedquantity;
	}

	public void setIndexof(Integer indexof) {
		this.indexof = indexof;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public void setConsumefieldkey(String consumefieldkey) {
		this.consumefieldkey = consumefieldkey;
	}

	public void setUsedquantity(Integer usedquantity) {
		this.usedquantity = usedquantity;
	}

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

	public Integer getInventoryconsumed() {
		return inventoryconsumed;
	}

	public void setInventoryconsumed(Integer inventoryconsumed) {
		this.inventoryconsumed = inventoryconsumed;
	}

}
