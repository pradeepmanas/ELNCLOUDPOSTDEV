package com.agaram.eln.primary.model.sheetManipulation;

import javax.persistence.Transient;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;


@Entity
@Table(name="Notification")

public class Notification {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
		
	private Long notificationid;

	@Temporal(TemporalType.TIMESTAMP)
	private Date addedon;
	
	//private Integer orderid;
	
	@Column(columnDefinition = "numeric(17,0)",name = "orderid") 
	private Long orderid;
	
	private String addedby;
		
	private Date duedate;
		
	private String intervals;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date cautiondate;
	
	private String description;
		
	private Integer usercode;
		
	private Integer status;

	@Temporal(TemporalType.TIMESTAMP)
	private Date currentdate;
	
	@Transient
	private LSuserMaster lsusermaster;

	@Transient
	private Integer sitecode;
	
	@Transient
	private LSsheetworkflow lssheetworkflow;
	
	
	@Column(columnDefinition = "varchar(250)",name = "BatchID") 
	private String batchid;

	
	public Long getNotificationid() {
		return notificationid;
	}

	public void setNotificationid(Long notificationid) {
		this.notificationid = notificationid;
	}

	public Date getAddedon() {
		return addedon;
	}

	public void setAddedon(Date addedon) {
		this.addedon = addedon;
	}
	


	public Long getOrderid() {
		return orderid;
	}

	public void setOrderid(Long orderid) {
		this.orderid = orderid;
	}

	public String getAddedby() {
		return addedby;
	}

	public void setAddedby(String addedby) {
		this.addedby = addedby;
	}
	
	public String getIntervals() {
		return intervals;
	}

	public void setIntervals(String intervals) {
		this.intervals = intervals;
	}
	
	public Date getDuedate() {
		return duedate;
	}

	public void setDuedate(Date duedate) {
		this.duedate = duedate;
	}

	public Date getCautiondate() {
		return cautiondate;
	}

	public void setCautiondate(Date cautiondate) {
		this.cautiondate = cautiondate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public Date getCurrentdate() {
		return currentdate;
	}

	public void setCurrentdate(Date currentdate) {
		this.currentdate = currentdate;
	}

	public LSuserMaster getLsusermaster() {
		return lsusermaster;
	}

	public void setLsusermaster(LSuserMaster lsusermaster) {
		this.lsusermaster = lsusermaster;
	}

	public Integer getSitecode() {
		return sitecode;
	}

	public void setSitecode(Integer sitecode) {
		this.sitecode = sitecode;
	}

	public LSsheetworkflow getLssheetworkflow() {
		return lssheetworkflow;
	}

	public void setLssheetworkflow(LSsheetworkflow lssheetworkflow) {
		this.lssheetworkflow = lssheetworkflow;
	}

	public String getBatchid() {
		return batchid;
	}

	public void setBatchid(String batchid) {
		this.batchid = batchid;
	}



	
}
