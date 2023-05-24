package com.agaram.eln.primary.model.inventory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "LSmaterialsection")
public class LSmaterialsection {
	@Id
	@Column(name = "materialsectioncode")
	private Integer nmaterialsectioncode;
	@Column(name = "materialcode")
	private Integer nmaterialcode;
	@Column(name = "sectioncode")
	private Integer nsectioncode;
	@Column(name = "reorderlevel")
	private Integer nreorderlevel;
	@Column(name = "status")
	private Integer nstatus;
	
	public Integer getNmaterialsectioncode() {
		return nmaterialsectioncode;
	}
	public void setNmaterialsectioncode(Integer nmaterialsectioncode) {
		this.nmaterialsectioncode = nmaterialsectioncode;
	}
	public Integer getNmaterialcode() {
		return nmaterialcode;
	}
	public void setNmaterialcode(Integer nmaterialcode) {
		this.nmaterialcode = nmaterialcode;
	}
	public Integer getNsectioncode() {
		return nsectioncode;
	}
	public void setNsectioncode(Integer nsectioncode) {
		this.nsectioncode = nsectioncode;
	}
	public Integer getNreorderlevel() {
		return nreorderlevel;
	}
	public void setNreorderlevel(Integer nreorderlevel) {
		this.nreorderlevel = nreorderlevel;
	}
	public Integer getNstatus() {
		return nstatus;
	}
	public void setNstatus(Integer nstatus) {
		this.nstatus = nstatus;
	}
}
