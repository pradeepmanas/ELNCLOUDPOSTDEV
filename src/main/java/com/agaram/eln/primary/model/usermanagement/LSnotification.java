package com.agaram.eln.primary.model.usermanagement;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "LSnotification")
public class LSnotification {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(columnDefinition = "numeric(17,0)",name = "notificationcode") 
	private Long notificationcode;
	
	@Column(columnDefinition = "varchar(120)")
	private String notification;
	
	@ManyToOne
	private LSuserMaster notifationto;
	
	@ManyToOne
	private LSuserMaster notifationfrom;
	
	@Column(columnDefinition = "varchar(400)")
	private String notificationdetils;
	
	@Column(columnDefinition = "varchar(400)")
	private String notificationpath;
	
	private int isnewnotification;
	
	@Column(columnDefinition = "date",name = "CreatedTimeStamp")
	private Date notificationdate;
	
	@Column(name = "repositorydatacode")
	private Integer repositorydatacode;
	
	@Column(name = "repositorycode")
	private Integer repositorycode;

	public Integer getRepositorydatacode() {
		return repositorydatacode;
	}

	public Integer getRepositorycode() {
		return repositorycode;
	}

	public void setRepositorydatacode(Integer repositorydatacode) {
		this.repositorydatacode = repositorydatacode;
	}

	public void setRepositorycode(Integer repositorycode) {
		this.repositorycode = repositorycode;
	}

	public Long getNotificationcode() {
		return notificationcode;
	}

	public void setNotificationcode(Long notificationcode) {
		this.notificationcode = notificationcode;
	}

	public String getNotification() {
		return notification;
	}

	public void setNotification(String notification) {
		this.notification = notification;
	}

	public LSuserMaster getNotifationto() {
		return notifationto;
	}

	public void setNotifationto(LSuserMaster notifationto) {
		this.notifationto = notifationto;
	}

	public LSuserMaster getNotifationfrom() {
		return notifationfrom;
	}

	public void setNotifationfrom(LSuserMaster notifationfrom) {
		this.notifationfrom = notifationfrom;
	}

	public String getNotificationdetils() {
		return notificationdetils;
	}

	public void setNotificationdetils(String notificationdetils) {
		this.notificationdetils = notificationdetils;
	}

	public String getNotificationpath() {
		return notificationpath;
	}

	public void setNotificationpath(String notificationpath) {
		this.notificationpath = notificationpath;
	}

	public int getIsnewnotification() {
		return isnewnotification;
	}

	public void setIsnewnotification(int isnewnotification) {
		this.isnewnotification = isnewnotification;
	}

	public Date getNotificationdate() {
		return notificationdate;
	}

	public void setNotificationdate(Date notificationdate) {
		this.notificationdate = notificationdate;
	}

	
}