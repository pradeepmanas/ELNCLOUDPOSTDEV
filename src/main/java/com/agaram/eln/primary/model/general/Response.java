package com.agaram.eln.primary.model.general;

import java.util.Date;

public class Response {
	private Boolean Status;
	private String Information;
	private Date Currentutcdate;
	public Boolean getStatus() {
		return Status;
	}
	public void setStatus(Boolean status) {
		Status = status;
	}
	public String getInformation() {
		return Information;
	}
	public void setInformation(String information) {
		Information = information;
	}
	public Date getCurrentutcdate() {
		return Currentutcdate;
	}
	public void setCurrentutcdate(Date currentutcdate) {
		Currentutcdate = currentutcdate;
	}
	
}
