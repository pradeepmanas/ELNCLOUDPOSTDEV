package com.agaram.eln.primary.model.instrumentDetails;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity(name = "LSlimsorder")
@Table(name = "LogiLABLIMSOrder")
public class LSlimsorder {
	@Id
	//columnDefinition = "numeric(17,0)",
	@Column(name = "OrderID",columnDefinition = "numeric(17,0)") 
	private Long orderid;
	//columnDefinition = "varchar(250)",
	@Column(columnDefinition = "varchar(250)",name = "BatchID")
	private String batchid;
	//columnDefinition = "varchar(250)",
	@Column(columnDefinition = "varchar(250)",name = "SampleID") 
	private String sampleid;
	//columnDefinition = "varchar(100)",
	@Column(columnDefinition = "varchar(100)",name = "ReplicateID")
	private String replicateid;
	//columnDefinition = "varchar(100)",
	@Column(columnDefinition = "varchar(100)",name = "TestCode") 
	private String testcode;
	//columnDefinition = "varchar(100)",
	@Column(columnDefinition = "varchar(100)",name = "MethodCode")
	private String methodcode;
	//columnDefinition = "varchar(100)",
	@Column(columnDefinition = "varchar(100)",name = "InstrumentCode")
	private String instrumentcode;
	//columnDefinition = "varchar(100)",
	@Column(columnDefinition = "varchar(100)",name = "InstrumentName")
	private String instrumentname;
	//columnDefinition = "char(10)",
	@Column(columnDefinition = "char(10)",name = "OrderFlag")
	private String orderflag;
	//columnDefinition = "char(10)",
	@Column(columnDefinition = "char(10)",name = "ParserFlag")
	private String parserflag;
	//columnDefinition = "date",
	@Column(name = "CreatedTimeStamp")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdtimestamp;
	//columnDefinition = "date",
	@Column(name = "CompletedTimeStamp")
	@Temporal(TemporalType.TIMESTAMP)
	private Date completedtimestamp;
	
	@Transient
	private String testname;
	
	@Transient
	private String nbatchcode;
	
	public String getNbatchcode() {
		return nbatchcode;
	}
	public void setNbatchcode(String nbatchcode) {
		this.nbatchcode = nbatchcode;
	}
	public String getTestname() {
		return testname;
	}
	public void setTestname(String testname) {
		this.testname = testname;
	} 
	public Long getOrderid() {
		return orderid;
	}
	public void setOrderid(Long orderid) {
		this.orderid = orderid;
	}
	public String getBatchid() {
		return batchid;
	}
	public void setBatchid(String batchid) {
		this.batchid = batchid;
	}
	public String getSampleid() {
		return sampleid;
	}
	public void setSampleid(String sampleid) {
		this.sampleid = sampleid;
	}
	public String getReplicateid() {
		return replicateid;
	}
	public void setReplicateid(String replicateid) {
		this.replicateid = replicateid;
	}
	public String getTestcode() {
		return testcode;
	}
	public void setTestcode(String testcode) {
		this.testcode = testcode;
	}
	public String getMethodcode() {
		return methodcode;
	}
	public void setMethodcode(String methodcode) {
		this.methodcode = methodcode;
	}
	public String getInstrumentcode() {
		return instrumentcode;
	}
	public void setInstrumentcode(String instrumentcode) {
		this.instrumentcode = instrumentcode;
	}
	public String getInstrumentname() {
		return instrumentname;
	}
	public void setInstrumentname(String instrumentname) {
		this.instrumentname = instrumentname;
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
	
}
