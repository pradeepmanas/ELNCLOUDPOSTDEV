package com.agaram.eln.primary.model.instrumentDetails;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity(name = "LSresultdetails")
@Table(name = "LLResultDetails")
public class LSresultdetails {
	@Id
	//columnDefinition = "numeric(20,0)",
	@Column(columnDefinition = "numeric(20,0)",name = "ResultID")
	private String resultid;
	//columnDefinition = "varchar(200)",
	@Column(columnDefinition = "varchar(200)",name = "LIMSReferenceCode")
	private String limsreferencecode;
	//columnDefinition = "varchar(max)",
	@Column(columnDefinition = "text",name = "BatchID")
	private String batchid;
	//columnDefinition = "varchar(100)",
	@Column(columnDefinition = "varchar(100)",name = "SampleID")
	private String sampleid;
	//columnDefinition = "varchar(100)",
	@Column(columnDefinition = "varchar(100)",name = "ReplicateID")
	private String replicateid;
	//columnDefinition = "varchar(250)",
	@Column(columnDefinition = "varchar(250)",name = "LIMSTestName")
	private String limstestname;
	//columnDefinition = "varchar(250)",
	@Column(columnDefinition = "varchar(250)",name = "LimsInstrumentName")
	private String limsinstrumentname;
	//columnDefinition = "varchar(250)",
	@Column(columnDefinition = "varchar(250)",name = "LIMSParamName")
	private String limsparamName;
	@Column(name = "SplitParserSeqNumber")
	private Integer splitparserseqNumber;
	//columnDefinition = "varchar(250)",
	@Column(columnDefinition = "varchar(250)",name = "ParserSplitSequence")
	private String parsersplitsequence;
	//columnDefinition = "varchar(max)",
	@Column(columnDefinition = "text",name = "Result")
	private String result;
	//columnDefinition = "varchar(250)",
	@Column(columnDefinition = "varchar(250)",name = "AIIFReport")
	private String aiifreport;
	//columnDefinition = "varchar(250)",
	@Column(columnDefinition = "varchar(250)",name = "FileRef")
	private String fileref;
	
	@OneToMany
//	@JoinColumn(name="RESULTDETID")
	private List<LSresultvalues> lsresultvalues;
	
	
	
	public String getLimsreferencecode() {
		return limsreferencecode;
	}
	
	public String getResultid() {
		return resultid;
	}

	public void setResultid(String resultid) {
		this.resultid = resultid;
	}

	public void setLimsreferencecode(String limsreferencecode) {
		this.limsreferencecode = limsreferencecode;
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
	public String getLimstestname() {
		return limstestname;
	}
	public void setLimstestname(String limstestname) {
		this.limstestname = limstestname;
	}
	public String getLimsinstrumentname() {
		return limsinstrumentname;
	}
	public void setLimsinstrumentname(String limsinstrumentname) {
		this.limsinstrumentname = limsinstrumentname;
	}
	public String getLimsparamName() {
		return limsparamName;
	}
	public void setLimsparamName(String limsparamName) {
		this.limsparamName = limsparamName;
	}
	public Integer getSplitparserseqNumber() {
		return splitparserseqNumber;
	}
	public void setSplitparserseqNumber(Integer splitparserseqNumber) {
		this.splitparserseqNumber = splitparserseqNumber;
	}
	public String getParsersplitsequence() {
		return parsersplitsequence;
	}
	public void setParsersplitsequence(String parsersplitsequence) {
		this.parsersplitsequence = parsersplitsequence;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getAiifreport() {
		return aiifreport;
	}
	public void setAiifreport(String aiifreport) {
		this.aiifreport = aiifreport;
	}
	public String getFileref() {
		return fileref;
	}
	public void setFileref(String fileref) {
		this.fileref = fileref;
	}
	public List<LSresultvalues> getLsresultvalues() {
		return lsresultvalues;
	}
	public void setLsresultvalues(List<LSresultvalues> lsresultvalues) {
		this.lsresultvalues = lsresultvalues;
	}
	
}
