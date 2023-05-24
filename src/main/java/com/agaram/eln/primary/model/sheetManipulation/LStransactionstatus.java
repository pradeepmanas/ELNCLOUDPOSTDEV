package com.agaram.eln.primary.model.sheetManipulation;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "LStransactionstatus")
public class LStransactionstatus {
	@Id 
	private Integer transcode;
	@Column(columnDefinition = "varchar(100)")
	private String transstatus;
	@Column(columnDefinition = "varchar(100)")
	private String transdisplaystatus;
	private Integer status;
	public Integer getTranscode() {
		return transcode;
	}
	public void setTranscode(Integer transcode) {
		this.transcode = transcode;
	}
	public String getTransstatus() {
		return transstatus;
	}
	public void setTransstatus(String transstatus) {
		this.transstatus = transstatus;
	}
	public String getTransdisplaystatus() {
		return transdisplaystatus;
	}
	public void setTransdisplaystatus(String transdisplaystatus) {
		this.transdisplaystatus = transdisplaystatus;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	
}
