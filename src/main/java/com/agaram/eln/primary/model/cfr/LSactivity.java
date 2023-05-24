package com.agaram.eln.primary.model.cfr;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.agaram.eln.primary.model.usermanagement.LSuserMaster;

@Entity
@Table(name = "LSactivity")
public class LSactivity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "activitycode")
	private Integer activitycode;
	
	@Column(columnDefinition = "varchar(255)")
	private String activity;
	
	@ManyToOne 
	private LSuserMaster activityby;
	
	@Transient
	LScfttransaction objsilentaudit;
	public LScfttransaction getObjsilentaudit() {
		return objsilentaudit;
	}
	public void setObjsilentaudit(LScfttransaction objsilentaudit) {
		this.objsilentaudit = objsilentaudit;
	}
	@Temporal(TemporalType.TIMESTAMP)
	private Date activityDate;
	
	public Integer getActivitycode() {
		return activitycode;
	}
	public void setActivitycode(Integer activitycode) {
		this.activitycode = activitycode;
	}
	public String getActivity() {
		return activity;
	}
	public void setActivity(String activity) {
		this.activity = activity;
	}
	public Date getActivityDate() {
		return activityDate;
	}
	public void setActivityDate(Date activityDate) {
		this.activityDate = activityDate;
	}
	public LSuserMaster getActivityby() {
		return activityby;
	}
	public void setActivityby(LSuserMaster activityby) {
		this.activityby = activityby;
	}
	
	
}
