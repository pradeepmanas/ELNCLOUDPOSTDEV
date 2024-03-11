package com.agaram.eln.primary.model.instrumentDetails;

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

import com.agaram.eln.primary.model.usermanagement.LSuserMaster;

@Entity(name = "LSOrdernotification")
@Table(name = "LSOrdernotification")
public class LSOrdernotification {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(columnDefinition = "numeric(17,0)",name = "notificationcode") 
	private Long notificationcode;
	
	@Column(columnDefinition = "numeric(17,0)", name = "batchcode")
	private Long batchcode;

	// columnDefinition = "varchar(250)",
	@Column(columnDefinition = "varchar(250)", name = "BatchID")
	private String batchid;

	@Column(name = "duedate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date duedate;
	
	@Column(name = "cautiondate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date cautiondate;

	@Column(name = "CreatedTimeStamp")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdtimestamp;
	
	private String screen;
	
	private Boolean isduedateexhausted;

	public Boolean getIsduedateexhausted() {
		return isduedateexhausted;
	}

	public void setIsduedateexhausted(Boolean isduedateexhausted) {
		this.isduedateexhausted = isduedateexhausted;
	}

	@Transient
	private LSuserMaster lsusermaster;

	@Transient
	private Integer sitecode;
	
	private Integer usercode;
	
	private Integer status;

	@Transient
	private Date overduedate;
	
	public Date getOverduedate() {
		return overduedate;
	}

	public void setOverduedate(Date overduedate) {
		this.overduedate = overduedate;
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

	public Long getBatchcode() {
		return batchcode;
	}

	public void setBatchcode(Long batchcode) {
		this.batchcode = batchcode;
	}

	public String getBatchid() {
		return batchid;
	}

	public void setBatchid(String batchid) {
		this.batchid = batchid;
	}

	public Date getCreatedtimestamp() {
		return createdtimestamp;
	}

	public void setCreatedtimestamp(Date createdtimestamp) {
		this.createdtimestamp = createdtimestamp;
	}

	public String getScreen() {
		return screen;
	}

	public void setScreen(String screen) {
		this.screen = screen;
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

	public Long getNotificationcode() {
		return notificationcode;
	}

	public void setNotificationcode(Long notificationcode) {
		this.notificationcode = notificationcode;
	}

}
