package com.agaram.eln.primary.model.masters;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="LSlogbooksampleupdates")
public class LSlogbooksampleupdates {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Basic(optional = false)
	public Integer logbooksamplecode;
	public String  logbooksampletype;
	public String  logbooksample;
	public Integer logbookcode;
	public String logbooksampleusedDetail;
	public Integer indexof;
	public Integer usedquantity;
	public Integer repositorydatacode;
	@Temporal(TemporalType.TIMESTAMP)
	public Date createddate;
	public Integer usercode;
	public Integer status;
	public String consumefieldkey;
	public String unit;
	public String createdbyusername;
	
	public Integer getLogbooksamplecode() {
		return logbooksamplecode;
	}
	public void setLogbooksamplecode(Integer logbooksamplecode) {
		this.logbooksamplecode = logbooksamplecode;
	}
	public String getLogbooksampletype() {
		return logbooksampletype;
	}
	public void setLogbooksampletype(String logbooksampletype) {
		this.logbooksampletype = logbooksampletype;
	}
	public String getLogbooksample() {
		return logbooksample;
	}
	public void setLogbooksample(String logbooksample) {
		this.logbooksample = logbooksample;
	}
	public Integer getLogbookcode() {
		return logbookcode;
	}
	public void setLogbookcode(Integer logbookcode) {
		this.logbookcode = logbookcode;
	}
	public Integer getIndexof() {
		return indexof;
	}
	public void setIndexof(Integer indexof) {
		this.indexof = indexof;
	}
	public Integer getUsedquantity() {
		return usedquantity;
	}
	public void setUsedquantity(Integer usedquantity) {
		this.usedquantity = usedquantity;
	}
	public Integer getRepositorydatacode() {
		return repositorydatacode;
	}
	public void setRepositorydatacode(Integer repositorydatacode) {
		this.repositorydatacode = repositorydatacode;
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
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getConsumefieldkey() {
		return consumefieldkey;
	}
	public void setConsumefieldkey(String consumefieldkey) {
		this.consumefieldkey = consumefieldkey;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getLogbooksampleusedDetail() {
		return logbooksampleusedDetail;
	}
	public void setLogbooksampleusedDetail(String logbooksampleusedDetail) {
		this.logbooksampleusedDetail = logbooksampleusedDetail;
	}
	public String getCreatedbyusername() {
		return createdbyusername;
	}
	public void setCreatedbyusername(String createdbyusername) {
		this.createdbyusername = createdbyusername;
	}

	

}
