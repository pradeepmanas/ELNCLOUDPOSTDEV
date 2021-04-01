package com.agaram.eln.primary.model.templates;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "LsUnmappedTags")
public class LsUnmappedTags {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "tagcode") 
	private Integer tagcode;
	@Column(name = "TagID") 
	private Integer tagid;
	@Column(name = "templatecode")
	private Integer templatecode;
	@Column(name = "TagName",columnDefinition = "varchar(100)") 
	private String tagname;
	@Column(name = "NonHierarchyStatus") 
	private Integer nonhierarchystatus;
	@Transient
	@Column(name = "sSiteCode",columnDefinition = "char(10)") 
	private String sitecode;
	public Integer getTagcode() {
		return tagcode;
	}
	public void setTagcode(Integer tagcode) {
		this.tagcode = tagcode;
	}
	public Integer getTagid() {
		return tagid;
	}
	public void setTagid(Integer tagid) {
		this.tagid = tagid;
	}
	public Integer getTemplatecode() {
		return templatecode;
	}
	public void setTemplatecode(Integer templatecode) {
		this.templatecode = templatecode;
	}
	public String getTagname() {
		return tagname;
	}
	public void setTagname(String tagname) {
		this.tagname = tagname;
	}
	public Integer getNonhierarchystatus() {
		return nonhierarchystatus;
	}
	public void setNonhierarchystatus(Integer nonhierarchystatus) {
		this.nonhierarchystatus = nonhierarchystatus;
	}
	public String getSitecode() {
		return sitecode;
	}
	public void setSitecode(String sitecode) {
		this.sitecode = sitecode;
	}
}
