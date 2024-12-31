package com.agaram.eln.primary.model.sequence;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SequenceTableTaskLevel")
public class SequenceTableTaskLevel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	private Integer sequencecodetasklevel;
	private Long tasksequence;
	private Integer testcode;
	
	public Integer getSequencecodetasklevel() {
		return sequencecodetasklevel;
	}
	public void setSequencecodetasklevel(Integer sequencecodetasklevel) {
		this.sequencecodetasklevel = sequencecodetasklevel;
	}
	public Long getTasksequence() {
		return tasksequence;
	}
	public void setTasksequence(Long tasksequence) {
		this.tasksequence = tasksequence;
	}
	public Integer getTestcode() {
		return testcode;
	}
	public void setTestcode(Integer testcode) {
		this.testcode = testcode;
	}
	
	
}
