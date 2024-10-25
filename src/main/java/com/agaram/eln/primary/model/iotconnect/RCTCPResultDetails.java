package com.agaram.eln.primary.model.iotconnect;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.Range;

import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.instrumentsetup.InstrumentMaster;
import com.agaram.eln.primary.model.methodsetup.Method;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;

@Entity
@Table(name = "RCTCPResultDetails")
public class RCTCPResultDetails {

private static final long serialVersionUID = 1L;
	
	@Id 
	@Column(name = "resultid", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer resultid;
	
//	@Column(name = "methodkey")
//	private Integer methodkey;	
	
	@Column(name = "parserfieldkey")
	private Integer parserfieldkey;	
	
	@Column(name = "parserblockkey")
	private Integer parserblockkey;
	
	@Column(name = "fieldname")
	private String fieldname;
	
	@Column(name = "seqnumber")
	private Integer seqnumber;
	
	@Column(name = "results")
	private String results;
	
//	@Column(name = "instmastkey")
//	private Integer instmastkey;
	
	
	
	@Range(min=-1, max=1)
	@Column(name = "status")
	private int status=1;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "usercode", nullable = false)
	private LSuserMaster createdby;
	
	@Column(name = "createddate")
	private Date createddate;	
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "sitecode", nullable = false)
	private LSSiteMaster site;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "methodkey", nullable = false)
	private Method method;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "instmasterkey", nullable = false)
	private InstrumentMaster instrument;
	
	@Transient
	LScfttransaction objsilentaudit;
	
	@OneToMany
	@JoinColumn(name="resultid")
	private List<RCTCPResultFieldValues> RCTCPResultFieldValues;

	public Integer getResultid() {
		return resultid;
	}

	public void setResultid(Integer resultid) {
		this.resultid = resultid;
	}

	public Integer getParserfieldkey() {
		return parserfieldkey;
	}

	public void setParserfieldkey(Integer parserfieldkey) {
		this.parserfieldkey = parserfieldkey;
	}

	public Integer getParserblockkey() {
		return parserblockkey;
	}

	public void setParserblockkey(Integer parserblockkey) {
		this.parserblockkey = parserblockkey;
	}

	public String getFieldname() {
		return fieldname;
	}

	public void setFieldname(String fieldname) {
		this.fieldname = fieldname;
	}

	public Integer getSeqnumber() {
		return seqnumber;
	}

	public void setSeqnumber(Integer seqnumber) {
		this.seqnumber = seqnumber;
	}

	public String getResults() {
		return results;
	}

	public void setResults(String results) {
		this.results = results;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public LSuserMaster getCreatedby() {
		return createdby;
	}

	public void setCreatedby(LSuserMaster createdby) {
		this.createdby = createdby;
	}

	public Date getCreateddate() {
		return createddate;
	}

	public void setCreateddate(Date createddate) {
		this.createddate = createddate;
	}

	public LSSiteMaster getSite() {
		return site;
	}

	public void setSite(LSSiteMaster site) {
		this.site = site;
	}

	public LScfttransaction getObjsilentaudit() {
		return objsilentaudit;
	}

	public void setObjsilentaudit(LScfttransaction objsilentaudit) {
		this.objsilentaudit = objsilentaudit;
	}

	public List<RCTCPResultFieldValues> getRCTCPResultFieldValues() {
		return RCTCPResultFieldValues;
	}

	public void setRCTCPResultFieldValues(List<RCTCPResultFieldValues> rCTCPResultFieldValues) {
		RCTCPResultFieldValues = rCTCPResultFieldValues;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public InstrumentMaster getInstrument() {
		return instrument;
	}

	public void setInstrument(InstrumentMaster instrument) {
		this.instrument = instrument;
	}

	
}
