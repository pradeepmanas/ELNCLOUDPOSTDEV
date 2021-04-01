package com.agaram.eln.primary.model.cfr;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "LSaudittrailconfigmaster")
public class LSaudittrailconfigmaster {
	
	@Id
	private Integer serialno;
	private Integer manualaudittrail;
	@Column(columnDefinition = "varchar(255)")
	private String modulename;
	@Column(columnDefinition = "varchar(255)")
	private String screenname;
	@Column(columnDefinition = "varchar(255)")
	private String taskname;
	
	@Column(nullable = false)
	private Integer ordersequnce;
	
	public Integer getOrdersequnce() {
		return ordersequnce;
	}
	public void setOrdersequnce(Integer ordersequnce) {
		this.ordersequnce = ordersequnce;
	}
	public Integer getManualaudittrail() {
		return manualaudittrail;
	}
	public void setManualaudittrail(Integer manualaudittrail) {
		this.manualaudittrail = manualaudittrail;
	}
	public Integer getSerialno() {
		return serialno;
	}
	public void setSerialno(Integer serialno) {
		this.serialno = serialno;
	}
	public String getModulename() {
		return modulename;
	}
	public void setModulename(String modulename) {
		this.modulename = modulename;
	}
	public String getScreenname() {
		return screenname;
	}
	public void setScreenname(String screenname) {
		this.screenname = screenname;
	}
	public String getTaskname() {
		return taskname;
	}
	public void setTaskname(String taskname) {
		this.taskname = taskname;
	}
	
	
	
}
