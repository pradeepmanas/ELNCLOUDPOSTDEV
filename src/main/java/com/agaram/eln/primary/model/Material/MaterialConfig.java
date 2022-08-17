package com.agaram.eln.primary.model.material;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "materialconfig")
public class MaterialConfig implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "nmaterialconfigcode")
	private Integer nmaterialconfigcode;
	@Column(name = "nformcode")
	private Integer nformcode;
	@Column(name = "nmaterialtypecode")
	private Integer nmaterialtypecode;
	@Type(type = "jsonb")
	@Column(name = "jsondata", columnDefinition = "jsonb")
	private List<Object> jsondata;

	@Column(name = "nstatus")
	private short nstatus;

	public Integer getNformcode() {
		return nformcode;
	}

	public void setNformcode(Integer nformcode) {
		this.nformcode = nformcode;
	}

	public Integer getNmaterialtypecode() {
		return nmaterialtypecode;
	}

	public void setNmaterialtypecode(Integer nmaterialtypecode) {
		this.nmaterialtypecode = nmaterialtypecode;
	}

	public Integer getNmaterialconfigcode() {
		return nmaterialconfigcode;
	}

	public void setNmaterialconfigcode(Integer nmaterialconfigcode) {
		this.nmaterialconfigcode = nmaterialconfigcode;
	}

	public List<Object> getJsondata() {
		return jsondata;
	}

	public void setJsondata(List<Object> jsondata) {
		this.jsondata = jsondata;
	}

	public short getNstatus() {
		return nstatus;
	}

	public void setNstatus(short nstatus) {
		this.nstatus = nstatus;
	}
}