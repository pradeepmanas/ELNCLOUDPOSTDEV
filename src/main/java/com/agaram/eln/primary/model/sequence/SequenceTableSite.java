package com.agaram.eln.primary.model.sequence;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sequencetablesite")
public class SequenceTableSite {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	private Integer sequencecodesite;
	private Integer sequencecode;
	private Long sitesequence;
	private Integer sitecode;
	
	public Integer getSequencecodesite() {
		return sequencecodesite;
	}
	public void setSequencecodesite(Integer sequencecodesite) {
		this.sequencecodesite = sequencecodesite;
	}
	public Integer getSequencecode() {
		return sequencecode;
	}
	public void setSequencecode(Integer sequencecode) {
		this.sequencecode = sequencecode;
	}
	public Long getSitesequence() {
		return sitesequence;
	}
	public void setSitesequence(Long sitesequence) {
		this.sitesequence = sitesequence;
	}
	public Integer getSitecode() {
		return sitecode;
	}
	public void setSitecode(Integer sitecode) {
		this.sitecode = sitecode;
	}
	
	
}
