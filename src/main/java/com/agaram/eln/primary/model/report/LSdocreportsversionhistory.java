package com.agaram.eln.primary.model.report;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="LSdocreportsversionhistory")
public class LSdocreportsversionhistory {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Basic(optional = false)
	private Integer docReportsversionhistoryCode;
	private Integer docReportsCode;
	@Column(columnDefinition = "varchar(120)")
	private String fileName;
	private Integer parentversionNo;
	private Integer versionNo;
	private Integer status;
	public Integer getDocReportsversionhistoryCode() {
		return docReportsversionhistoryCode;
	}
	public void setDocReportsversionhistoryCode(Integer docReportsversionhistoryCode) {
		this.docReportsversionhistoryCode = docReportsversionhistoryCode;
	}
	public Integer getDocReportsCode() {
		return docReportsCode;
	}
	public void setDocReportsCode(Integer docReportsCode) {
		this.docReportsCode = docReportsCode;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getVersionNo() {
		return versionNo;
	}
	public void setVersionNo(Integer versionNo) {
		this.versionNo = versionNo;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public Integer getParentversionNo() {
		return parentversionNo;
	}
	public void setParentversionNo(Integer parentversionNo) {
		this.parentversionNo = parentversionNo;
	}
	
}
