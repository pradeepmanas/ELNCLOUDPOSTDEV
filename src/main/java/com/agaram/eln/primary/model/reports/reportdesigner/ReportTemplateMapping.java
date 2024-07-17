package com.agaram.eln.primary.model.reports.reportdesigner;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.agaram.eln.primary.model.usermanagement.LSprojectmaster;
import com.agaram.eln.primary.model.usermanagement.LSusersteam;


@Entity
public class ReportTemplateMapping {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	private Long templatemapid;

	private Long templatecode;

	@ManyToOne
	private LSusersteam lsusersteam;
	
	@ManyToOne
	private LSprojectmaster lsprojectmaster;

	public LSprojectmaster getLsprojectmaster() {
		return lsprojectmaster;
	}

	public void setLsprojectmaster(LSprojectmaster lsprojectmaster) {
		this.lsprojectmaster = lsprojectmaster;
	}

	public Long getTemplatemapid() {
		return templatemapid;
	}

	public void setTemplatemapid(Long templatemapid) {
		this.templatemapid = templatemapid;
	}

	public Long getTemplatecode() {
		return templatecode;
	}

	public void setTemplatecode(Long templatecode) {
		this.templatecode = templatecode;
	}

	public LSusersteam getLsusersteam() {
		return lsusersteam;
	}

	public void setLsusersteam(LSusersteam lsusersteam) {
		this.lsusersteam = lsusersteam;
	}

	

}
