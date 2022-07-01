package com.agaram.eln.primary.model.instrumentsetup;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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

import com.agaram.eln.primary.model.methodsetup.InstantDateAdapter;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * This class is used to map the fields of 'instrumentmaster' table of the Database.
 * @author ATE153
 * @version 1.0.0
 * @since   05- Dec- 2019
 */
@JsonIgnoreProperties(value = {"hibernateLazyInitializer"})
@XmlRootElement  (name = "instrumentmaster")
@XmlType(propOrder = { "instmastkey", "instrumentcode", "site","instrumentname","instused",
		"insttype","instcategory","instmake","instmodel","instiopno","electrodeno",
		 "status", "createdby", "createddate","username","transactiondate"})
@Entity
@Table(name = "instrumentmaster")
public class InstrumentMaster implements Serializable, Diffable<InstrumentMaster> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id 
	@Column(name = "instmasterkey", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer instmastkey;
	
	@Column(name = "instrumentcode")
	private String instrumentcode;
	
	@ManyToOne( fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "sitecode", nullable = false)
	private LSSiteMaster site;
	
	@Column(name = "instrumentname")
	private String instrumentname;	
	
	@Column(name = "instused")
	private int instused;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "insttypekey", nullable = false)
	private InstrumentType insttype;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "instcategorykey", nullable = false)
	private InstrumentCategory instcategory;
	
	@Column(name = "instmake")
	private String instmake;
		
	@Transient
	private String username;
	
	@Transient
	@Temporal(TemporalType.TIMESTAMP)
	Date transactiondate;
	
	@Column(name = "instmodel")
	private String instmodel;
	
	@Column(name = "instiopno")
	private String instiopno;
	
	@Column(name = "electrodeno")
	private String electrodeno;	
		
	@Column(name = "status")
	private int status=1;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "usercode", nullable = false)
	private LSuserMaster createdby;
	
	@Column(name = "createddate")
	private Date createddate;
	
	@XmlAttribute	
	public Integer getInstmastkey() {
		return instmastkey;
	}

	public void setInstmastkey(Integer instmastkey) {
		this.instmastkey = instmastkey;
	}

	@XmlElement
	public String getInstrumentcode() {
		return instrumentcode;
	}

	public void setInstrumentcode(String instrumentcode) {
		this.instrumentcode = instrumentcode;
	}

	@XmlElement
	public String getInstrumentname() {
		return instrumentname;
	}

	public void setInstrumentname(String instrumentname) {
		this.instrumentname = instrumentname;
	}

	@XmlElement
	public int getInstused() {
		return instused;
	}

	public void setInstused(int instused) {
		this.instused = instused;
	}

	@XmlElement
	public InstrumentType getInsttype() {
		return insttype;
	}

	public void setInsttype(InstrumentType insttype) {
		this.insttype = insttype;
	}

	@XmlElement
	public InstrumentCategory getInstcategory() {
		return instcategory;
	}

	public void setInstcategory(InstrumentCategory instcategory) {
		this.instcategory = instcategory;
	}

	@XmlElement
	public String getInstmake() {
		return instmake;
	}

	public void setInstmake(String instmake) {
		this.instmake = instmake;
	}

	@XmlElement
	public LSSiteMaster getSite() {
		return site;
	}

	public void setSite(LSSiteMaster site) {
		this.site = site;
	}

	@XmlElement
	public String getInstmodel() {
		return instmodel;
	}	

	public void setInstmodel(String instmodel) {
		this.instmodel = instmodel;
	}

	@XmlElement
	public String getInstiopno() {
		return instiopno;
	}

	public void setInstiopno(String instiopno) {
		this.instiopno = instiopno;
	}

	@XmlElement
	public String getElectrodeno() {
		return electrodeno;
	}

	public void setElectrodeno(String electrodeno) {
		this.electrodeno = electrodeno;
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
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	
	public Date getTransactiondate() {
		return transactiondate;
	}

	public void setTransactiondate(Date transactiondate) {
		this.transactiondate = transactiondate;
	}

	/**
	 * To find difference between two entity objects by implementing Diffable interface  
	 */
	@Override
	public DiffResult diff(InstrumentMaster obj) {
	     return new DiffBuilder(this, obj, ToStringStyle.SHORT_PREFIX_STYLE)
	       .append("instrumentcode", this.instrumentcode, obj.instrumentcode) 	         
	       .append("site", this.site.getSitename(), obj.site.getSitename())
	       .append("instrumentname", this.instrumentname, obj.instrumentname)
	       .append("instused", this.instused, obj.instused)	
	       .append("insttype", this.insttype.getInsttypename(), obj.insttype.getInsttypename())	 
	       .append("instcategory", this.instcategory.getInstcatname(), obj.instcategory.getInstcatname())	 
	       .append("instmake", this.instmake, obj.instmake)	
	       .append("instmodel", this.instmodel, obj.instmodel)	
	       .append("instiopno", this.instiopno, obj.instiopno)	
	       .append("electrodeno", this.electrodeno, obj.electrodeno)	  
	       .append("status",this.status, obj.status)
	       .append("createdby", this.createdby.getUsername(), obj.createdby.getUsername())
	       .append("createddate", this.createddate, obj.createddate)
	       .append("username", this.username, obj.username)
	       .append("transactiondate", this.transactiondate, obj.transactiondate)
	       .build();
	}

	/**
	 * This constructor is mandatory for a pojo class to perform deep copy of
	 * object
	 * @param instMaster [InstrumentMaster]
	 */
	public InstrumentMaster(InstrumentMaster instMaster)
	{
		this.electrodeno = instMaster.electrodeno;
		this.instcategory = instMaster.instcategory;
		this.instiopno = instMaster.instiopno;
		this.instmake=instMaster.instmake;
		this.instmastkey = instMaster.instmastkey;
		this.instmodel = instMaster.instmodel;
		this.instrumentcode = instMaster.instrumentcode;
		this.instrumentname = instMaster.instrumentname;
		this.insttype = instMaster.insttype;
		this.instused = instMaster.instused;		
		this.site = instMaster.site;
		this.status = instMaster.status;
		this.createdby = instMaster.createdby;
		this.createddate = instMaster.createddate;
		this.username = instMaster.username;
		this.transactiondate = instMaster.transactiondate;
	}
	
	/**
	 * Creation of parameterized constructor makes this
	 * default constructor also mandatory for a pojo
	 */
	public InstrumentMaster() {}
}
