package com.agaram.eln.primary.model.sequence;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SequenceTableProjectLevel")
public class SequenceTableProjectLevel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	private Integer sequencecodeprojectlevel;
	private Long projectsequence;
	private Integer projectcode;
	
	public Integer getSequencecodeprojectlevel() {
		return sequencecodeprojectlevel;
	}
	public void setSequencecodeprojectlevel(Integer sequencecodeprojectlevel) {
		this.sequencecodeprojectlevel = sequencecodeprojectlevel;
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
