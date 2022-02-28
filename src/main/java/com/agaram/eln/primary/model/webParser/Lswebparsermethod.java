package com.agaram.eln.primary.model.webParser;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.Diffable;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.validator.constraints.Range;

import com.agaram.eln.primary.model.methodsetup.InstantDateAdapter;

@Entity
@Table(name = "Lswebparsermethod")
public class Lswebparsermethod implements Serializable, Diffable<Lswebparsermethod>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id 
	@Column(name = "methodkey", nullable = false)
	private Integer methodkey;
	
	@Column(name = "methodname")
	private String methodname;	

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "instmasterkey", nullable = false)
	private Lswebparserinstrument instmaster;	
	
	@Column(name = "instrawdataurl")
	private String instrawdataurl;	
	
	@Column(name = "samplesplit")
	private Integer samplesplit;
	
	@Column(name = "parser")
	private Integer parser;
		
	@Transient
	private Lswebparseruser createdby;
	
	@Transient
	private Lswebparsersite site;
	
	@Range(min=-1, max=1)
	@Column(name = "status")
	private int status=1;	
	
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
	public Lswebparserinstrument getInstmaster() {
		return instmaster;
	}

	public void setInstmaster(Lswebparserinstrument instmaster) {
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
	public Lswebparsersite getSite() {
		return site;
	}

	public void setSite(Lswebparsersite site) {
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
	public Lswebparseruser getCreatedby() {
		return createdby;
	}

	public void setCreatedby(Lswebparseruser createdby) {
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
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public DiffResult diff(Lswebparsermethod obj) {
		
	     return new DiffBuilder(this, obj, ToStringStyle.SHORT_PREFIX_STYLE)
	       .append("methodname", this.methodname, obj.methodname) 
	       .append("instmaster", getInstCode(this.instmaster), getInstCode(obj.instmaster))	     
	       .append("instrawdataurl", this.instrawdataurl, obj.instrawdataurl)
	       .append("samplesplit", this.samplesplit, obj.samplesplit)
	       .append("parser", this.parser, obj.parser)
	       .append("site", this.getSite().getSitename(), obj.getSite().getSitename())
	       .append("status",this.status, obj.status)
	       .append("createdby", this.createdby.getFirstname(), obj.createdby.getFirstname())
	       .append("createddate", this.createddate, obj.createddate)
	       .build();
	}

	private Object getInstCode(Lswebparserinstrument instrumentMaster)
	{
		if (instrumentMaster == null)
		{
			return null;
		}
		else {
			return instrumentMaster.getInstrumentcode();
		}		
	}

//	/**
//	 * This constructor is mandatory for a pojo class to perform deep copy of
//	 * object
//	 * @param method [Method]
//	 */
	public Lswebparsermethod(Lswebparsermethod method)
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
	
	
	public Lswebparsermethod(Integer methodkey)
	{
		this.methodkey = methodkey;
	}
	
	/**
	 * Creation of parameterized constructor makes this
	 * default constructor also mandatory for a pojo
	 */
	public Lswebparsermethod() {}
}
