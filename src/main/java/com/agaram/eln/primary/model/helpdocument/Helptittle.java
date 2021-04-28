package com.agaram.eln.primary.model.helpdocument;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Helptittle")
public class Helptittle {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "nodecode")
	private Integer nodecode;
	
	private String text;
	
	private Integer parentcode;

	public Integer getNodecode() {
		return nodecode;
	}

	public void setNodecode(Integer nodecode) {
		this.nodecode = nodecode;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Integer getParentcode() {
		return parentcode;
	}

	public void setParentcode(Integer parentcode) {
		this.parentcode = parentcode;
	}
	
	
}
