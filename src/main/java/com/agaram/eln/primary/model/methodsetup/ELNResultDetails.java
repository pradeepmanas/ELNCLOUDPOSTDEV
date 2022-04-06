package com.agaram.eln.primary.model.methodsetup;

import java.io.Serializable;
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
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.Diffable;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.validator.constraints.Range;

import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * This class is used to map the fields of 'delimiter' table of the Database.
 * @author ATE153
 * @version 1.0.0
 * @since   04- Feb- 2020
 */
//@JsonIgnoreProperties(value = {"hibernateLazyInitializer"})
//@XmlRootElement  (name = "elnresultdetails")
//@XmlType(propOrder = { "resultid", "filerefname", "methodkey", 
//		 "parserblockkey","paramname","seqnumber","results","status", "createdby", "createddate","sitecode"})
@Entity
@Table(name = "elnresultdetails")
public class ELNResultDetails implements Serializable, Diffable<ELNResultDetails>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id 
	@Column(name = "resultid", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer resultid;
	
	@Column(columnDefinition = "numeric(17,0)",name = "batchcode") 
	private Long batchcode;
	
	@Column(name = "filerefname")
	private String filerefname;	
	
	@Column(name = "methodkey")
	private Integer methodkey;	
	
	@Column(name = "parserfieldkey")
	private Integer parserfieldkey;	
	
	@Column(name = "parserblockkey")
	private Integer parserblockkey;
	
	@Column(name = "paramname")
	private String paramname;
	
	@Column(name = "seqnumber")
	private Integer seqnumber;
	
	@Column(name = "results")
	private String results;
	
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
	
	@Transient
	LScfttransaction objsilentaudit;
	
	@OneToMany
	@JoinColumn(name="resultid")
	private List<LSResultFieldValues> lsresultfieldvalues;
	
	public LScfttransaction getObjsilentaudit() {
		return objsilentaudit;
	}

	public void setObjsilentaudit(LScfttransaction objsilentaudit) {
		this.objsilentaudit = objsilentaudit;
	}
	
	@Transient
	LScfttransaction objmanualaudit;
	
	public LScfttransaction getObjmanualaudit() {
		return objmanualaudit;
	}

	public void setObjmanualaudit(LScfttransaction objmanualaudit) {
		this.objmanualaudit = objmanualaudit;
	}
		
	@XmlAttribute	
	public Integer getResultid() {
		return resultid;
	}

	public void setResultid(Integer resultid) {
		this.resultid = resultid;
	}
	
	@XmlElement	
	public Long getBatchcode() {
		return batchcode;
	}

	public void setBatchcode(Long batchcode) {
		this.batchcode = batchcode;
	}

	@XmlElement	
	public String getFilerefname() {
		return filerefname;
	}

	public void setFilerefname(String filerefname) {
		this.filerefname = filerefname;
	}
	
	@XmlElement	
	public Integer getParserfieldkey() {
		return parserfieldkey;
	}

	public void setParserfieldkey(Integer parserfieldkey) {
		this.parserfieldkey = parserfieldkey;
	}

	@XmlElement	
	public Integer getMethodkey() {
		return methodkey;
	}

	public void setMethodkey(Integer methodkey) {
		this.methodkey = methodkey;
	}

	@XmlElement	
	public List<LSResultFieldValues> getLsresultfieldvalues() {
		return lsresultfieldvalues;
	}

	public void setLsresultfieldvalues(List<LSResultFieldValues> lsresultfieldvalues) {
		this.lsresultfieldvalues = lsresultfieldvalues;
	}

	@XmlElement	
	public Integer getParserblockkey() {
		return parserblockkey;
	}

	public void setParserblockkey(Integer parserblockkey) {
		this.parserblockkey = parserblockkey;
	}
	
	@XmlElement	
	public String getParamname() {
		return paramname;
	}

	public void setParamname(String paramname) {
		this.paramname = paramname;
	}
	
	@XmlElement	
	public Integer getSeqnumber() {
		return seqnumber;
	}

	public void setSeqnumber(Integer seqnumber) {
		this.seqnumber = seqnumber;
	}
	
	@XmlElement	
	public String getResults() {
		return results;
	}

	public void setResults(String results) {
		this.results = results;
	}

	@XmlElement	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	@XmlElement	
	public LSuserMaster getCreatedby() {
		return createdby;
	}

	public void setCreatedby(LSuserMaster createdby) {
		this.createdby = createdby;
	}

	@XmlElement
	@XmlJavaTypeAdapter(InstantDateAdapter.class)
	public Date getCreateddate() {
		return createddate;
	}

	public void setCreateddate(Date createddate) {
		this.createddate = createddate;
	}
	
	@XmlElement
	public LSSiteMaster getSite() {
		return site;
	}

	public void setSite(LSSiteMaster site) {
		this.site = site;
	}

	/**
	 * To find difference between two entity objects by implementing Diffable interface  
	 */
	@Override
	public DiffResult diff(ELNResultDetails obj) {
	     return new DiffBuilder(this, obj, ToStringStyle.SHORT_PREFIX_STYLE)
	       .append("filerefname", this.filerefname, obj.filerefname) 
	       .append("batchcode", this.batchcode, obj.batchcode) 
	       .append("methodkey", this.methodkey, obj.methodkey)	      
	       .append("parserblockkey",this.parserblockkey, obj.parserblockkey)
	       .append("paramname",this.paramname, obj.paramname)
	       .append("seqnumber",this.seqnumber, obj.seqnumber)
	       .append("results",this.results, obj.results)
	       .append("status",this.status, obj.status)
	       .append("createdby", this.createdby.getUsername(), obj.createdby.getUsername())
	       .append("createddate", this.createddate, obj.createddate)
	       .append("site", this.getSite().getSitename(), obj.getSite().getSitename())
	       .build();
	}

	/**
	 * This constructor is mandatory for a pojo class to perform deep copy of
	 * object
	 * @param elnresultdetails [ELNResultDetails]
	 */
	public ELNResultDetails(ELNResultDetails elnresultdetails)
	{
		this.resultid = elnresultdetails.resultid;
		this.batchcode = elnresultdetails.batchcode;
		this.filerefname = elnresultdetails.filerefname;
		this.methodkey = elnresultdetails.methodkey;
		this.parserblockkey = elnresultdetails.parserblockkey;
		this.paramname = elnresultdetails.paramname;
		this.seqnumber = elnresultdetails.seqnumber;
		this.results = elnresultdetails.results;
		this.status = elnresultdetails.status;
		this.createdby = elnresultdetails.createdby;
		this.createddate = elnresultdetails.createddate;
		this.site = elnresultdetails.site;
	}
	
	/**
	 * Creation of parameterized constructor makes this
	 * default constructor also mandatory for a pojo
	 */
	public ELNResultDetails() {}
}
