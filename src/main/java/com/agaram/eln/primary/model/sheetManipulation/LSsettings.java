package com.agaram.eln.primary.model.sheetManipulation;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "LSsettings")
public class LSsettings {
	@Id
	private Integer settingid;
	@Column(columnDefinition = "varchar(100)")
	private String settingname;
	@Column(columnDefinition = "text")
	private String settingstring;
	private Integer status;
	public Integer getSettingid() {
		return settingid;
	}
	public void setSettingid(Integer settingid) {
		this.settingid = settingid;
	}
	public String getSettingname() {
		return settingname;
	}
	public void setSettingname(String settingname) {
		this.settingname = settingname;
	}
	public String getSettingstring() {
		return settingstring;
	}
	public void setSettingstring(String settingstring) {
		this.settingstring = settingstring;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	
}
