package com.agaram.eln.primary.model.sheetManipulation;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;

@Entity
@Table(name = "LSsampleresult")
public class LSsampleresult {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "sampleresultcode")
	private Integer sampleresultcode;
	
	
	private Integer filesamplecode;
	
	private Integer testcode;
	private Integer parametercode;
	@Column(columnDefinition = "numeric(18,0)")
	private Integer parametertypecode;
	@Column(columnDefinition = "varchar(100)")
	private String result;
	private Integer isactive;
	@Column(columnDefinition = "numeric(17,0)")
	private long orderid;
	@ManyToOne
	private LSSiteMaster lssitemaster;
	private String createby;
//	@Column(columnDefinition = "date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdate;
	@Column(columnDefinition = "numeric(17,0)",name = "batchcode") 
	private Long batchcode;
	private Integer fileparametercode;
	
	public Integer getSampleresultcode() {
		return sampleresultcode;
	}
	public void setSampleresultcode(Integer sampleresultcode) {
		this.sampleresultcode = sampleresultcode;
	}
	
	
	public Integer getFilesamplecode() {
		return filesamplecode;
	}
	public void setFilesamplecode(Integer filesamplecode) {
		this.filesamplecode = filesamplecode;
	}
	public Integer getTestcode() {
		return testcode;
	}
	public void setTestcode(Integer testcode) {
		this.testcode = testcode;
	}
	public Integer getParametercode() {
		return parametercode;
	}
	public void setParametercode(Integer parametercode) {
		this.parametercode = parametercode;
	}
	public Integer getParametertypecode() {
		return parametertypecode;
	}
	public void setParametertypecode(Integer parametertypecode) {
		this.parametertypecode = parametertypecode;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public Integer getIsactive() {
		return isactive;
	}
	public void setIsactive(Integer isactive) {
		this.isactive = isactive;
	}
	public long getOrderid() {
		return orderid;
	}
	public void setOrderid(long orderid) {
		this.orderid = orderid;
	}
	
	
	public String getCreateby() {
		return createby;
	}
	public void setCreateby(String createby) {
		this.createby = createby;
	}
	public LSSiteMaster getLssitemaster() {
		return lssitemaster;
	}
	public void setLssitemaster(LSSiteMaster lssitemaster) {
		this.lssitemaster = lssitemaster;
	}
	public Date getCreatedate() {
		return createdate;
	}
	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}
	public Long getBatchcode() {
		return batchcode;
	}
	public void setBatchcode(Long batchcode) {
		this.batchcode = batchcode;
	}
	public Integer getFileparametercode() {
		return fileparametercode;
	}
	public void setFileparametercode(Integer fileparametercode) {
		this.fileparametercode = fileparametercode;
	}
	
	
}
