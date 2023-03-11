package com.agaram.eln.primary.model.instrumentDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity(name = "LSlogilablimsordergroup")
@Table(name = "LSlogilablimsordergroup")
public class LSlogilablimsordergroup {
	@Id
	@Column(name = "groupid",columnDefinition = "varchar(250)") 
	private String groupid;
	
	@Column(columnDefinition = "varchar(250)",name = "batchid")
	private String batchid;
	
	@Column(name = "testcode") 
	private Integer ntestcode;
	
	@Column(name = "ntransactiontestcode") 
	private Integer ntransactiontestcode;
	
	@Column(columnDefinition = "varchar(100)",name = "testname")
	private String stestname;
	
	@Column(columnDefinition = "numeric(20,0)",name = "limsprimarycode") 
	private Long limsprimarycode;
	
	@Column(columnDefinition = "varchar(100)",name = "arno")
	private String sarno;
	
	@Column(columnDefinition = "varchar(100)",name = "samplearno")
	private String ssamplearno;
	
//	@Column(columnDefinition = "nvarchar(100)",name = "InstrumentName")
//	private String instrumentname;
//	
//	@Column(columnDefinition = "nchar(10)",name = "OrderFlag")
//	private String orderflag;
//	
//	@Column(columnDefinition = "nchar(10)",name = "ParserFlag")
//	private String parserflag;
	
//	@Column(columnDefinition = "datetime",name = "createdtimestamp")
//	private Date createdtimestamp;

	public String getGroupid() {
		return groupid;
	}

	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}
	
	public String getBatchid() {
		return batchid;
	}

	public void setBatchid(String batchid) {
		this.batchid = batchid;
	}

	public Integer getNtestcode() {
		return ntestcode;
	}

	public void setNtestcode(Integer ntestcode) {
		this.ntestcode = ntestcode;
	}

	public String getStestname() {
		return stestname;
	}

	public void setStestname(String stestname) {
		this.stestname = stestname;
	}

	public Long getLimsprimarycode() {
		return limsprimarycode;
	}

	public void setLimsprimarycode(Long limsprimarycode) {
		this.limsprimarycode = limsprimarycode;
	}

	public String getSarno() {
		return sarno;
	}

	public void setSarno(String sarno) {
		this.sarno = sarno;
	}

	public String getSsamplearno() {
		return ssamplearno;
	}

	public void setSsamplearno(String ssamplearno) {
		this.ssamplearno = ssamplearno;
	}

//	public Date getCreatedtimestamp() {
//		return createdtimestamp;
//	}
//
//	public void setCreatedtimestamp(Date createdtimestamp) {
//		this.createdtimestamp = createdtimestamp;
//	}
	
	@Transient
	private Integer ntransactionresultcode;
	
	@Transient
	private Integer npreregno;
	
	@Transient
	private Integer ntransactionsamplecode;
	
	
	
	@Transient
	private Integer ntestgrouptestcode;
	
	@Transient
	private Integer nretestno;
	
	@Transient
	private Integer ntestrepeatcount;
	
	@Transient
	private Integer ntestgrouptestparametercode;
	
	@Transient
	private Integer ntestparametercode;
	
	@Transient
	private Integer nparametertypecode;
	
	@Transient
	private Integer nroundingdigits;
	
	@Transient
	private Integer nlinkcode;
	
	@Transient
	private Integer nattachedlink;
	
	@Transient
	private Integer nsitecode;
	
	@Transient
	private Integer nstatus;
	
	@Transient
	private Integer nelnsitecode;
	
	@Transient
	private Integer nlimssitecode;
	
	@Transient
	private String sresult;
	
	@Transient
	private String sllinterstatus;
	
	@Transient
	private String sfileid;
	
	@Transient
	private String suuid;
	
	@Transient
	private String sparametername;
	
	@Transient
	private String testsynonym;
	
	@Transient
	private String testparametersynonym;

	public Integer getNtransactionresultcode() {
		return ntransactionresultcode;
	}

	public void setNtransactionresultcode(Integer ntransactionresultcode) {
		this.ntransactionresultcode = ntransactionresultcode;
	}

	public Integer getNpreregno() {
		return npreregno;
	}

	public void setNpreregno(Integer npreregno) {
		this.npreregno = npreregno;
	}

	public Integer getNtransactionsamplecode() {
		return ntransactionsamplecode;
	}

	public void setNtransactionsamplecode(Integer ntransactionsamplecode) {
		this.ntransactionsamplecode = ntransactionsamplecode;
	}

	public Integer getNtransactiontestcode() {
		return ntransactiontestcode;
	}

	public void setNtransactiontestcode(Integer ntransactiontestcode) {
		this.ntransactiontestcode = ntransactiontestcode;
	}

	public Integer getNtestgrouptestcode() {
		return ntestgrouptestcode;
	}

	public void setNtestgrouptestcode(Integer ntestgrouptestcode) {
		this.ntestgrouptestcode = ntestgrouptestcode;
	}

	public Integer getNretestno() {
		return nretestno;
	}

	public void setNretestno(Integer nretestno) {
		this.nretestno = nretestno;
	}

	public Integer getNtestrepeatcount() {
		return ntestrepeatcount;
	}

	public void setNtestrepeatcount(Integer ntestrepeatcount) {
		this.ntestrepeatcount = ntestrepeatcount;
	}

	public Integer getNtestgrouptestparametercode() {
		return ntestgrouptestparametercode;
	}

	public void setNtestgrouptestparametercode(Integer ntestgrouptestparametercode) {
		this.ntestgrouptestparametercode = ntestgrouptestparametercode;
	}

	public Integer getNtestparametercode() {
		return ntestparametercode;
	}

	public void setNtestparametercode(Integer ntestparametercode) {
		this.ntestparametercode = ntestparametercode;
	}

	public Integer getNparametertypecode() {
		return nparametertypecode;
	}

	public void setNparametertypecode(Integer nparametertypecode) {
		this.nparametertypecode = nparametertypecode;
	}

	public Integer getNroundingdigits() {
		return nroundingdigits;
	}

	public void setNroundingdigits(Integer nroundingdigits) {
		this.nroundingdigits = nroundingdigits;
	}

	public Integer getNlinkcode() {
		return nlinkcode;
	}

	public void setNlinkcode(Integer nlinkcode) {
		this.nlinkcode = nlinkcode;
	}

	public Integer getNattachedlink() {
		return nattachedlink;
	}

	public void setNattachedlink(Integer nattachedlink) {
		this.nattachedlink = nattachedlink;
	}

	public Integer getNsitecode() {
		return nsitecode;
	}

	public void setNsitecode(Integer nsitecode) {
		this.nsitecode = nsitecode;
	}

	public Integer getNstatus() {
		return nstatus;
	}

	public void setNstatus(Integer nstatus) {
		this.nstatus = nstatus;
	}

	public Integer getNelnsitecode() {
		return nelnsitecode;
	}

	public void setNelnsitecode(Integer nelnsitecode) {
		this.nelnsitecode = nelnsitecode;
	}

	public Integer getNlimssitecode() {
		return nlimssitecode;
	}

	public void setNlimssitecode(Integer nlimssitecode) {
		this.nlimssitecode = nlimssitecode;
	}

	public String getSresult() {
		return sresult;
	}

	public void setSresult(String sresult) {
		this.sresult = sresult;
	}

	public String getSllinterstatus() {
		return sllinterstatus;
	}

	public void setSllinterstatus(String sllinterstatus) {
		this.sllinterstatus = sllinterstatus;
	}

	public String getSfileid() {
		return sfileid;
	}

	public void setSfileid(String sfileid) {
		this.sfileid = sfileid;
	}

	public String getSuuid() {
		return suuid;
	}

	public void setSuuid(String suuid) {
		this.suuid = suuid;
	}

	public String getSparametername() {
		return sparametername;
	}

	public void setSparametername(String sparametername) {
		this.sparametername = sparametername;
	}

	public String getTestsynonym() {
		return testsynonym;
	}

	public void setTestsynonym(String testsynonym) {
		this.testsynonym = testsynonym;
	}

	public String getTestparametersynonym() {
		return testparametersynonym;
	}

	public void setTestparametersynonym(String testparametersynonym) {
		this.testparametersynonym = testparametersynonym;
	}
}
