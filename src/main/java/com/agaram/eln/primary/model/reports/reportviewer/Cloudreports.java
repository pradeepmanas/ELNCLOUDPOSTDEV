package com.agaram.eln.primary.model.reports.reportviewer;

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
@Table(name = "Cloudreports")
@TypeDefs({
    @TypeDef(name = "json", typeClass = JsonStringType.class),
    @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
public class Cloudreports {
	@Id
	private Long reportcode;
	@Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
	private String reporttemplatecontent;
	public Long getReportcode() {
		return reportcode;
	}
	public void setReportcode(Long reportcode) {
		this.reportcode = reportcode;
	}
	public String getReporttemplatecontent() {
		return reporttemplatecontent;
	}
	public void setReporttemplatecontent(String reporttemplatecontent) {
		this.reporttemplatecontent = reporttemplatecontent;
	}
	
	
}
