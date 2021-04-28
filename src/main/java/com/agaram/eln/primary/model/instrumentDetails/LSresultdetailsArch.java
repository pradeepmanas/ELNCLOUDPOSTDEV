package com.agaram.eln.primary.model.instrumentDetails;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Entity(name = "LSresultdetailsArch")
@Table(name = "LLResultDetails_Arch")
public class LSresultdetailsArch {

	@Id
	@Column(columnDefinition = "numeric(20,0)",name = "ResultID")
	private String resultid;
	
	@Column(columnDefinition = "varchar(200)",name = "LIMSReferenceCode")
	private String limsreferencecode;	
	
	@Column(columnDefinition = "varchar(100)",name = "SampleID")
	private String sampleid;
	@Column(columnDefinition = "varchar(100)",name = "ReplicateID")
	private String replicateid;
	
	@Column(columnDefinition = "varchar(250)",name = "LIMSTestName")
	private String limstestname;
	@Column(columnDefinition = "varchar(250)",name = "LimsInstrumentName")
	private String limsinstrumentname;
	@Column(columnDefinition = "varchar(250)",name = "LIMSParamName")
	private String limsparamName;
	@Column(columnDefinition = "varchar(250)",name = "ParserSplitSequence")
	private String parsersplitsequence;
	@Column(columnDefinition = "varchar(250)",name = "AIIFReport")
	private String aiifreport;
	@Column(columnDefinition = "varchar(250)",name = "FileRef")
	private String fileref;
	
	@Column(name = "SplitParserSeqNumber")
	private Integer splitparserseqNumber;
	
	@Column(columnDefinition = "text",name = "Result")
	private String result;
	@Column(columnDefinition = "text",name = "BatchID")
	private String batchid;
	
	@Column(columnDefinition = "varchar(25)",name = "SDMSTaskID")
	private String sdmstaskid;
	
	@Column(columnDefinition = "varchar(50)",name = "TransferID")
	private String transferid;
	
	@OneToMany
	@JoinColumn(name="RESULTDETID")
	@OrderBy("id.resseqno ASC")
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

	public String getSdmstaskid() {
		return sdmstaskid;
	}

	public void setSdmstaskid(String sdmstaskid) {
		this.sdmstaskid = sdmstaskid;
	}

	public String getTransferid() {
		return transferid;
	}

	public void setTransferid(String transferid) {
		this.transferid = transferid;
	}	

}
