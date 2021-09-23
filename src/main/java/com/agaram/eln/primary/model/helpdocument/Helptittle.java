package com.agaram.eln.primary.model.helpdocument;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.agaram.eln.primary.model.general.Response;

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
	
	private Integer nodeindex;
	
	private String page;
	
	@Transient
	Response objResponse;

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

	public Response getObjResponse() {
		return objResponse;
	}

	public void setObjResponse(Response objResponse) {
		this.objResponse = objResponse;
	}

	public Integer getNodeindex() {
		return nodeindex;
	}

	public void setNodeindex(Integer nodeindex) {
		this.nodeindex = nodeindex;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}
	
	
}
