package com.agaram.eln.primary.model.sequence;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sequencetableproject")
public class SequenceTableProject {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	private Integer sequencecodeproject;
	private Integer sequencecode;
	private Long projectsequence;
	private Integer projectcode;
	
	public Integer getSequencecodeproject() {
		return sequencecodeproject;
	}
	public void setSequencecodeproject(Integer sequencecodeproject) {
		this.sequencecodeproject = sequencecodeproject;
	}
	public Integer getSequencecode() {
		return sequencecode;
	}
	public void setSequencecode(Integer sequencecode) {
		this.sequencecode = sequencecode;
	}
	public Long getProjectsequence() {
		return projectsequence;
	}
	public void setProjectsequence(Long projectsequence) {
		this.projectsequence = projectsequence;
	}
	public Integer getProjectcode() {
		return projectcode;
	}
	public void setProjectcode(Integer projectcode) {
		this.projectcode = projectcode;
	}
	
	
}
