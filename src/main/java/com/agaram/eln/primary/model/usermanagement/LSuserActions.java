package com.agaram.eln.primary.model.usermanagement;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "LSuserActions")
public class LSuserActions {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "useractioncode") 
	private Integer useractioncode;

	private Integer usercode;
	private Integer sitecode;
	
	
	private Integer assignedordershowall;
	private Integer assignedordershowpending;
	private Integer assignedordershowcompleted;
	private Integer myordershowall;
	private Integer myordershowpending;
	private Integer myordershowcompleted;
	
	public LSuserActions()
	{
		
	}
	
	public LSuserActions(Integer useractioncode)
	{
		this.useractioncode = useractioncode;
	}
	
	public Integer getUseractioncode() {
		return useractioncode;
	}
	public void setUseractioncode(Integer useractioncode) {
		this.useractioncode = useractioncode;
	}
	public Integer getAssignedordershowall() {
		return assignedordershowall;
	}
	public void setAssignedordershowall(Integer assignedordershowall) {
		this.assignedordershowall = assignedordershowall;
	}
	public Integer getAssignedordershowpending() {
		return assignedordershowpending;
	}
	public void setAssignedordershowpending(Integer assignedordershowpending) {
		this.assignedordershowpending = assignedordershowpending;
	}
	public Integer getAssignedordershowcompleted() {
		return assignedordershowcompleted;
	}
	public void setAssignedordershowcompleted(Integer assignedordershowcompleted) {
		this.assignedordershowcompleted = assignedordershowcompleted;
	}
	public Integer getMyordershowall() {
		return myordershowall;
	}
	public void setMyordershowall(Integer myordershowall) {
		this.myordershowall = myordershowall;
	}
	public Integer getMyordershowpending() {
		return myordershowpending;
	}
	public void setMyordershowpending(Integer myordershowpending) {
		this.myordershowpending = myordershowpending;
	}
	public Integer getMyordershowcompleted() {
		return myordershowcompleted;
	}
	public void setMyordershowcompleted(Integer myordershowcompleted) {
		this.myordershowcompleted = myordershowcompleted;
	}
	
	public Integer getUsercode() {
		return usercode;
	}
	public void setUsercode(Integer usercode) {
		this.usercode = usercode;
	}

	public Integer getSitecode() {
		return sitecode;
	}
	public void setSitecode(Integer sitecode) {
		this.sitecode = sitecode;
	}
	
}
