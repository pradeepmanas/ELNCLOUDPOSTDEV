package com.agaram.eln.primary.model.instrumentDetails;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "LsOrderSampleUpdate")
public class LsOrderSampleUpdate {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Basic(optional = false)
	public Integer ordersamplecode;
	public String ordersampletype;
	public String ordersample;
	public String ordersampleusedDetail;
	@Column(columnDefinition = "text")
	public String ordersampleinfo;
	@Column(columnDefinition = "numeric(17,0)",name = "batchcode") 
	public Long batchcode;
	@Temporal(TemporalType.TIMESTAMP)
	public Date createddate;
	public Integer usercode;
	public Integer repositorycode;
	public Integer repositorydatacode;
	public Integer quantityused=0;
	public String historydetails;
	public String createdbyusername;
	public String screenmodule;
	
	public String getHistorydetails() {
		return historydetails;
	}
	public String getCreatedbyusername() {
		return createdbyusername;
	}
	public String getScreenmodule() {
		return screenmodule;
	}
	public void setHistorydetails(String historydetails) {
		this.historydetails = historydetails;
	}
	public void setCreatedbyusername(String createdbyusername) {
		this.createdbyusername = createdbyusername;
	}
	public void setScreenmodule(String screenmodule) {
		this.screenmodule = screenmodule;
	}
	public Integer getOrdersamplecode() {
		return ordersamplecode;
	}
	public void setOrdersamplecode(Integer ordersamplecode) {
		this.ordersamplecode = ordersamplecode;
	}
	public String getOrdersampletype() {
		return ordersampletype;
	}
	public void setOrdersampletype(String ordersampletype) {
		this.ordersampletype = ordersampletype;
	}
	public String getOrdersample() {
		return ordersample;
	}
	public void setOrdersample(String ordersample) {
		this.ordersample = ordersample;
	}
	public String getOrdersampleusedDetail() {
		return ordersampleusedDetail;
	}
	public void setOrdersampleusedDetail(String ordersampleusedDetail) {
		this.ordersampleusedDetail = ordersampleusedDetail;
	}
	public String getOrdersampleinfo() {
		return ordersampleinfo;
	}
	public void setOrdersampleinfo(String ordersampleinfo) {
		this.ordersampleinfo = ordersampleinfo;
	}
	public Long getBatchcode() {
		return batchcode;
	}
	public void setBatchcode(Long batchcode) {
		this.batchcode = batchcode;
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
	public Integer getRepositorycode() {
		return repositorycode;
	}
	public void setRepositorycode(Integer repositorycode) {
		this.repositorycode = repositorycode;
	}
	public Integer getRepositorydatacode() {
		return repositorydatacode;
	}
	public void setRepositorydatacode(Integer repositorydatacode) {
		this.repositorydatacode = repositorydatacode;
	}
	public Integer getQuantityused() {
		return quantityused;
	}
	public void setQuantityused(Integer quantityused) {
		this.quantityused = quantityused;
	}	
	
}
