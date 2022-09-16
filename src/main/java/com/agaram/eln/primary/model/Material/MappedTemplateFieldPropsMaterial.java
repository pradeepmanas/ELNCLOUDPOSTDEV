package com.agaram.eln.primary.model.material;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "MappedTemplateFieldPropsMaterial")
public class MappedTemplateFieldPropsMaterial {
	
	@Id
	@Column(name = "nmappedtemplatefieldpropmaterialcode")
	private Integer nmappedtemplatefieldpropmaterialcode;
	@Column(name = "nmaterialconfigcode")
	private Integer nmaterialconfigcode;
	@Column(name = "jsondata", columnDefinition = "jsonb")
	private String jsondata;
	@Column(name = "nstatus")
	private Integer nstatus;
	
	public Integer getNmappedtemplatefieldpropmaterialcode() {
		return nmappedtemplatefieldpropmaterialcode;
	}
	public void setNmappedtemplatefieldpropmaterialcode(Integer nmappedtemplatefieldpropmaterialcode) {
		this.nmappedtemplatefieldpropmaterialcode = nmappedtemplatefieldpropmaterialcode;
	}
	public Integer getNmaterialconfigcode() {
		return nmaterialconfigcode;
	}
	public void setNmaterialconfigcode(Integer nmaterialconfigcode) {
		this.nmaterialconfigcode = nmaterialconfigcode;
	}
	public String getJsondata() {
		return jsondata;
	}
	public void setJsondata(String jsondata) {
		this.jsondata = jsondata;
	}
	public Integer getNstatus() {
		return nstatus;
	}
	public void setNstatus(Integer nstatus) {
		this.nstatus = nstatus;
	}
}
