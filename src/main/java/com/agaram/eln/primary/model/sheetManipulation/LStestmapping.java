package com.agaram.eln.primary.model.sheetManipulation;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name = "LStestmapping")
public class LStestmapping {
	@Id
	private Integer testid;
	private Integer testcodemapping;
	@Column(columnDefinition = "varchar(100)")
	private String testnamemapping;
	private Integer isactive;
	private Integer siteid;
	private Integer createby;
	@Column(columnDefinition = "date")
	private String createdate;
	public Integer getTestid() {
		return testid;
	}
	public void setTestid(Integer testid) {
		this.testid = testid;
	}
	public Integer getTestcodemapping() {
		return testcodemapping;
	}
	public void setTestcodemapping(Integer testcodemapping) {
		this.testcodemapping = testcodemapping;
	}
	public String getTestnamemapping() {
		return testnamemapping;
	}
	public void setTestnamemapping(String testnamemapping) {
		this.testnamemapping = testnamemapping;
	}
	public Integer getIsactive() {
		return isactive;
	}
	public void setIsactive(Integer isactive) {
		this.isactive = isactive;
	}
	public Integer getSiteid() {
		return siteid;
	}
	public void setSiteid(Integer siteid) {
		this.siteid = siteid;
	}
	public Integer getCreateby() {
		return createby;
	}
	public void setCreateby(Integer createby) {
		this.createby = createby;
	}
	public String getCreatedate() {
		return createdate;
	}
	public void setCreatedate(String createdate) {
		this.createdate = createdate;
	}
	
}
