package com.agaram.eln.primary.model.instrumentDetails;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity(name = "Lsbatchdetails")
@Table(name = "Lsbatchdetails")
public class Lsbatchdetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Basic(optional = false)
	private Long batchdetailcode;
	
	@Column(columnDefinition = "numeric(17,0)",name = "batchcode") 
	private Long batchcode;
	
	@Column(columnDefinition = "numeric(17,0)",name = "orderID") 
	private Long orderID;
	
	@Column(columnDefinition = "numeric(17,0)",name = "limsorderID") 
	private Long limsorderID;

	@Column(columnDefinition = "varchar(250)",name = "SampleID") 
	private String sampleid;
	
	@Column(columnDefinition = "char(10)",name = "OrderFlag")
	@Transient
	private String orderflag;
	@Column(columnDefinition = "char(10)",name = "ParserFlag")
	@Transient
	private String parserflag;
	@Column(columnDefinition = "date",name = "CreatedTimeStamp")
	@Transient
	private Date createdtimestamp;
	@Column(columnDefinition = "date",name = "CompletedTimeStamp")
	@Transient
	private Date completedtimestamp;
	@Transient
	private String nbatchcode;

	public String getNbatchcode() {
		return nbatchcode;
	}

	public void setNbatchcode(String nbatchcode) {
		this.nbatchcode = nbatchcode;
	}

	public String getOrderflag() {
		return orderflag;
	}

	public void setOrderflag(String orderflag) {
		this.orderflag = orderflag;
	}

	public String getParserflag() {
		return parserflag;
	}

	public void setParserflag(String parserflag) {
		this.parserflag = parserflag;
	}

	public Date getCreatedtimestamp() {
		return createdtimestamp;
	}

	public void setCreatedtimestamp(Date createdtimestamp) {
		this.createdtimestamp = createdtimestamp;
	}

	public Date getCompletedtimestamp() {
		return completedtimestamp;
	}

	public void setCompletedtimestamp(Date completedtimestamp) {
		this.completedtimestamp = completedtimestamp;
	}

	public String getTestname() {
		return testname;
	}

	public void setTestname(String testname) {
		this.testname = testname;
	}

	@Transient
	private String testname;

	public Long getBatchdetailcode() {
		return batchdetailcode;
	}

	public void setBatchdetailcode(Long batchdetailcode) {
		this.batchdetailcode = batchdetailcode;
	}

	public Long getBatchcode() {
		return batchcode;
	}

	public void setBatchcode(Long batchcode) {
		this.batchcode = batchcode;
	}

	public Long getOrderID() {
		return orderID;
	}

	public void setOrderID(Long orderID) {
		this.orderID = orderID;
	}

	public Long getLimsorderID() {
		return limsorderID;
	}

	public void setLimsorderID(Long limsorderID) {
		this.limsorderID = limsorderID;
	}

	public String getSampleid() {
		return sampleid;
	}

	public void setSampleid(String sampleid) {
		this.sampleid = sampleid;
	}	
}