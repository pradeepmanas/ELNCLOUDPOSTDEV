package com.agaram.eln.primary.model.sheetManipulation;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "LSfileparametermapping")
public class LSfileparametermapping {
	@Id
	private Integer fileparametermappingcode;
	private Integer versionno;
	private Integer fileparametercode;
	private Integer nparametercode;
	private Integer isactive;
	private Integer createby;
	@Column(columnDefinition = "date")
	private String createdate;
	private Integer filecode;
	public Integer getFileparametermappingcode() {
		return fileparametermappingcode;
	}
	public void setFileparametermappingcode(Integer fileparametermappingcode) {
		this.fileparametermappingcode = fileparametermappingcode;
	}
	public Integer getVersionno() {
		return versionno;
	}
	public void setVersionno(Integer versionno) {
		this.versionno = versionno;
	}
	public Integer getFileparametercode() {
		return fileparametercode;
	}
	public void setFileparametercode(Integer fileparametercode) {
		this.fileparametercode = fileparametercode;
	}
	public Integer getNparametercode() {
		return nparametercode;
	}
	public void setNparametercode(Integer nparametercode) {
		this.nparametercode = nparametercode;
	}
	public Integer getIsactive() {
		return isactive;
	}
	public void setIsactive(Integer isactive) {
		this.isactive = isactive;
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
	public Integer getFilecode() {
		return filecode;
	}
	public void setFilecode(Integer filecode) {
		this.filecode = filecode;
	}
	
}
