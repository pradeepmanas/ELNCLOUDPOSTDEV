package com.agaram.eln.primary.model.sequence;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sequencetableordertype")
public class SequenceTableOrderType {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	private Integer sequencecodeordertype;
	private Integer sequencecode;
	private Long ordertypesequence;
	private Integer ordertype;
	
	public Integer getSequencecodeordertype() {
		return sequencecodeordertype;
	}
	public void setSequencecodeordertype(Integer sequencecodeordertype) {
		this.sequencecodeordertype = sequencecodeordertype;
	}
	public Integer getSequencecode() {
		return sequencecode;
	}
	public void setSequencecode(Integer sequencecode) {
		this.sequencecode = sequencecode;
	}
	public Long getOrdertypesequence() {
		return ordertypesequence;
	}
	public void setOrdertypesequence(Long ordertypesequence) {
		this.ordertypesequence = ordertypesequence;
	}
	public Integer getOrdertype() {
		return ordertype;
	}
	public void setOrdertype(Integer ordertype) {
		this.ordertype = ordertype;
	}
	
	
}
