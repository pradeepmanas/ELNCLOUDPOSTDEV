package com.agaram.eln.primary.model.templates;

import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

//import lombok.Data;

@Entity
//@Data
@DynamicUpdate
@Table(name = "LsMappedTemplate")
public class LsMappedTemplate {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "templatecode") 
	private Integer templatecode;
	@Column(name = "templatename",columnDefinition = "varchar(100)") 
	private String templatename;
	@OneToMany
	@JoinColumn(name="templatecode")
	private List<LsMappedTags> lstags;
	
	public List<LsMappedTags> getLstags() {
		return lstags;
	}
	public void setLstags(List<LsMappedTags> lstags) {
		this.lstags = lstags;
	}
	public Integer getTemplatecode() {
		return templatecode;
	}
	public void setTemplatecode(Integer templatecode) {
		this.templatecode = templatecode;
	}
	public String getTemplatename() {
		return templatename;
	}
	public void setTemplatename(String templatename) {
		this.templatename = templatename;
	}
}