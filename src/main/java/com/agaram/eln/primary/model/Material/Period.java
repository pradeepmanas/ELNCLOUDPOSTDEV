package com.agaram.eln.primary.model.material;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "period")
public class Period {
	
	@Id
	@Column(name = "nperiodcode")
	private Integer nperiodcode;
	@Type(type = "jsonb")
	@Column(name = "jsondata",columnDefinition = "jsonb")
	private Integer jsondata;
	@Column(name = "ndefaultstatus")
	private Integer ndefaultstatus;
	@Column(name = "nsitecode")
	private Integer nsitecode;
	@Column(name = "nstatus")
	private Integer nstatus;
	
	public Integer getNperiodcode() {
		return nperiodcode;
	}
	public void setNperiodcode(Integer nperiodcode) {
		this.nperiodcode = nperiodcode;
	}
	public Integer getJsondata() {
		return jsondata;
	}
	public void setJsondata(Integer jsondata) {
		this.jsondata = jsondata;
	}
	public Integer getNdefaultstatus() {
		return ndefaultstatus;
	}
	public void setNdefaultstatus(Integer ndefaultstatus) {
		this.ndefaultstatus = ndefaultstatus;
	}
	public Integer getNsitecode() {
		return nsitecode;
	}
	public void setNsitecode(Integer nsitecode) {
		this.nsitecode = nsitecode;
	}
	public Integer getNstatus() {
		return nstatus;
	}
	public void setNstatus(Integer nstatus) {
		this.nstatus = nstatus;
	}
}