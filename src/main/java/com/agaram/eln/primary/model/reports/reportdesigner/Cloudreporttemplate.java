package com.agaram.eln.primary.model.reports.reportdesigner;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonStringType;

@Entity
@Table(name = "Cloudreporttemplate")
@TypeDefs({
    @TypeDef(name = "json", typeClass = JsonStringType.class),
    @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
public class Cloudreporttemplate {
	@Id
	private Long templatecode;
	@Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
	private String templatecontent;
	
	public Long getTemplatecode() {
		return templatecode;
	}
	public void setTemplatecode(Long templatecode) {
		this.templatecode = templatecode;
	}
	public String getTemplatecontent() {
		return templatecontent;
	}
	public void setTemplatecontent(String templatecontent) {
		this.templatecontent = templatecontent;
	}
	
	
}
