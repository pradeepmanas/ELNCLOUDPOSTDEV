package com.agaram.eln.primary.model.material;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "materialgrade")
public class MaterialGrade {
	
	@Id
	@Column(name = "nmaterialgradecode")
	private Integer nmaterialgradecode;
	@Type(type = "jsonb")
	@Column(name = "jsondata", columnDefinition = "jsonb")
	private String jsondata;
	@Column(name = "ndefaultstatus")
	private Integer ndefaultstatus;
	@Column(name = "nsitecode")
	private Integer nsitecode;
	@Column(name = "nstatus")
	private Integer nstatus;
	
	public Integer getNmaterialgradecode() {
		return nmaterialgradecode;
	}
	public void setNmaterialgradecode(Integer nmaterialgradecode) {
		this.nmaterialgradecode = nmaterialgradecode;
	}
	public String getJsondata() {
		return jsondata;
	}
	public void setJsondata(String jsondata) {
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
