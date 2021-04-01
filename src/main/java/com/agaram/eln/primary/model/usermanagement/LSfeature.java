package com.agaram.eln.primary.model.usermanagement;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "LSfeature")
public class LSfeature {
	@Id
	private Integer modulecode;

	@Column(columnDefinition = "char(10)")
	private String moduleid;
	@Column(columnDefinition = "varchar(150)")
	private String moduleName;
	@Column(columnDefinition = "varchar(150)")
	private String senum;
	private Integer status;
	private Integer norder;
	
	public Integer getModulecode() {
		return modulecode;
	}
	public void setModulecode(Integer modulecode) {
		this.modulecode = modulecode;
	}
	public String getModuleid() {
		return moduleid;
	}
	public void setModuleid(String moduleid) {
		this.moduleid = moduleid;
	}
	public String getModuleName() {
		return moduleName;
	}
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	public String getSenum() {
		return senum;
	}
	public void setSenum(String senum) {
		this.senum = senum;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getNorder() {
		return norder;
	}
	public void setNorder(Integer norder) {
		this.norder = norder;
	}

}
