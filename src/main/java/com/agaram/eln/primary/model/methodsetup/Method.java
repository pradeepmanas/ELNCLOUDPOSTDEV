package com.agaram.eln.primary.model.methodsetup;

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

import com.agaram.eln.primary.model.instrumentsetup.InstrumentMaster;
import com.agaram.eln.primary.model.methodsetup.InstantDateAdapter;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * This class is used to map the fields of 'method' table of the Database.
 * @author ATE153
 * @version 1.0.0
 * @since   04- Feb- 2020
 */
@JsonIgnoreProperties(value = {"hibernateLazyInitializer"})
@XmlRootElement  (name = "method")
@XmlType(propOrder = { "methodkey", "methodname", "instmaster", "instrawdataurl",
		"samplesplit", "parser", "site", "status", "createdby", "createddate"})
@Entity
@Table(name = "method")
public class Method implements Serializable, Diffable<Method>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id 
	@Column(name = "methodkey", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer methodkey;
	
	@Column(name = "methodname")
	private String methodname;	

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "instmasterkey", nullable = false)
	private InstrumentMaster instmaster;	
	
	@Column(name = "instrawdataurl")
	private String instrawdataurl;	
	
	@Column(name = "samplesplit")
	private Integer samplesplit;
	
	@Column(name = "parser")
	private Integer parser;
		
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "sitecode", nullable = false)
	private LSSiteMaster site;
	
	@Range(min=-1, max=1)
	@Column(name = "status")
	private int status=1;	

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "usercode", nullable = false)
	private LSuserMaster createdby;
	
	@Column(name = "createddate")
	private Date createddate;	
			
	@XmlAttribute	
	public Integer getMethodkey() {
		return methodkey;
	}

	public void setMethodkey(Integer methodkey) {
		this.methodkey = methodkey;
	}

	@XmlElement	
	public String getMethodname() {
		return methodname;
	}

	public void setMethodname(String methodname) {
		this.methodname = methodname;
	}

	@XmlElement	(nillable=true)
	public InstrumentMaster getInstmaster() {
		return instmaster;
	}

	public void setInstmaster(InstrumentMaster instmaster) {
		this.instmaster = instmaster;
	}

	@XmlElement	
	public String getInstrawdataurl() {
		return instrawdataurl;
	}

	public void setInstrawdataurl(String instrawdataurl) {
		this.instrawdataurl = instrawdataurl;
	}

	@XmlElement	(nillable=true)
	public Integer getSamplesplit() {
		return samplesplit;
	}

	public void setSamplesplit(Integer samplesplit) {
		this.samplesplit = samplesplit;
	}
	
	@XmlElement	(nillable=true)	
	public Integer getParser() {
		return parser;
	}

	public void setParser(Integer parser) {
		this.parser = parser;
	}

	@XmlElement
	public LSSiteMaster getSite() {
		return site;
	}

	public void setSite(LSSiteMaster site) {
		this.site = site;
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

	/**
	 * To find difference between two entity objects by implementing Diffable interface  
	 */
	@Override
	public DiffResult diff(Method obj) {
		
	     return new DiffBuilder(this, obj, ToStringStyle.SHORT_PREFIX_STYLE)
	       .append("methodname", this.methodname, obj.methodname) 
	       .append("instmaster", getInstCode(this.instmaster), getInstCode(obj.instmaster))	     
	       .append("instrawdataurl", this.instrawdataurl, obj.instrawdataurl)
	       .append("samplesplit", this.samplesplit, obj.samplesplit)
	       .append("parser", this.parser, obj.parser)
	       .append("site", this.getSite().getSitename(), obj.getSite().getSitename())
	       .append("status",this.status, obj.status)
	       .append("createdby", this.createdby.getUsername(), obj.createdby.getUsername())
	       .append("createddate", this.createddate, obj.createddate)
	       .build();
	}

	private Object getInstCode(InstrumentMaster instrumentMaster)
	{
		if (instrumentMaster == null)
		{
			return null;
		}
		else {
			return instrumentMaster.getInstrumentcode();
		}		
	}

	/**
	 * This constructor is mandatory for a pojo class to perform deep copy of
	 * object
	 * @param method [Method]
	 */
	public Method(Method method)
	{
		this.methodkey = method.methodkey;
		this.methodname = method.methodname;
		this.instmaster = method.instmaster;
		this.instrawdataurl = method.instrawdataurl;
		this.samplesplit = method.samplesplit;
		this.parser = method.parser;
		this.site = method.site;
		this.status = method.status;
		this.createdby = method.createdby;
		this.createddate = method.createddate;
	}
	
	/**
	 * Creation of parameterized constructor makes this
	 * default constructor also mandatory for a pojo
	 */
	public Method() {}
}
