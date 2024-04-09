package com.agaram.eln.primary.model.dashboard;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class LsActiveWidgets  {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer activewidgetscode ;
	
	public String screenname;	
	
    public String activityType;
	
    public Integer userId; 
    
    public String Activewidgetsdetails ; 
    
    public Long Activewidgetsdetailscode; 

    public Date activedatatimestamp; 

    public String description;
    
    public Integer parentcode; 
    

	public Integer getParentcode() {
		return parentcode;
	}

	public void setParentcode(Integer parentcode) {
		this.parentcode = parentcode;
	}

	public Integer getActivewidgetscode() {
		return activewidgetscode;
	}

	public void setActivewidgetscode(Integer activewidgetscode) {
		this.activewidgetscode = activewidgetscode;
	}

	public String getScreenname() {
		return screenname;
	}

	public void setScreenname(String screenname) {
		this.screenname = screenname;
	}

	public String getActivityType() {
		return activityType;
	}

	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getActivewidgetsdetails() {
		return Activewidgetsdetails;
	}

	public void setActivewidgetsdetails(String activewidgetsdetails) {
		Activewidgetsdetails = activewidgetsdetails;
	}

	public Long getActivewidgetsdetailscode() {
		return Activewidgetsdetailscode;
	}

	public void setActivewidgetsdetailscode(Long activewidgetsdetailscode) {
		Activewidgetsdetailscode = activewidgetsdetailscode;
	}

	public Date getActivedatatimestamp() {
		return activedatatimestamp;
	}

	public void setActivedatatimestamp(Date activedatatimestamp) {
		this.activedatatimestamp = activedatatimestamp;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	} 
    
    
	
	
}
