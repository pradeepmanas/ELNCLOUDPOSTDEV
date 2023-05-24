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

import com.agaram.eln.primary.model.methodsetup.InstantDateAdapter;

@Entity
@Table(name = "Lswebparserinstrument")
public class Lswebparserinstrument implements Serializable, Diffable<Lswebparserinstrument>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id 
	@Column(name = "instmasterkey", nullable = false)
	private Integer instmastkey;
	
	@Column(name = "instrumentcode")
	private String instrumentcode;
	
	@Column(name = "instrumentname")
	private String instrumentname;	
	
	@Column(name = "instused")
	private int instused;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "insttypekey", nullable = false)
	private Lswebparserinstrumenttype insttype;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "instcategorykey", nullable = false)
	private Lswebparserinstrumentcategory instcategory;
	
	@Column(name = "instmake")
	private String instmake;
	
	@Column(name = "instmodel")
	private String instmodel;
	
	@Column(name = "instiopno")
	private String instiopno;
	
	@Column(name = "electrodeno")
	private String electrodeno;	
		
	@Column(name = "status")
	private int status=1;
	
	@Transient
	private Lswebparseruser createdby;
	
	@Transient
	private Lswebparsersite site;
	
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
	public Lswebparserinstrumenttype getInsttype() {
		return insttype;
	}

	public void setInsttype(Lswebparserinstrumenttype insttype) {
		this.insttype = insttype;
	}

	@XmlElement
	public Lswebparserinstrumentcategory getInstcategory() {
		return instcategory;
	}

	public void setInstcategory(Lswebparserinstrumentcategory instcategory) {
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
	public Lswebparsersite getSite() {
		return site;
	}

	public void setSite(Lswebparsersite site) {
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
	public DiffResult diff(Lswebparserinstrument obj) {
	     return new DiffBuilder(this, obj, ToStringStyle.SHORT_PREFIX_STYLE)
	       .append("instrumentcode", this.instrumentcode, obj.instrumentcode) 
	       .append("instrumentname", this.instrumentname, obj.instrumentname)
	       .append("instused", this.instused, obj.instused)	
	       .append("insttype", this.insttype.getInsttypename(), obj.insttype.getInsttypename())	 
	       .append("instcategory", this.instcategory.getInstcatname(), obj.instcategory.getInstcatname())	 
	       .append("instmake", this.instmake, obj.instmake)	
	       .append("instmodel", this.instmodel, obj.instmodel)	
	       .append("instiopno", this.instiopno, obj.instiopno)	
	       .append("electrodeno", this.electrodeno, obj.electrodeno)	  
	       .append("status",this.status, obj.status)
	       .append("createdby", this.createdby.getFirstname(), obj.createdby.getFirstname())
	       .append("createddate", this.createddate, obj.createddate)
	       .build();
	}

	/**
	 * This constructor is mandatory for a pojo class to perform deep copy of
	 * object
	 * @param instMaster [InstrumentMaster]
	 */
	public Lswebparserinstrument(Lswebparserinstrument instMaster)
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
	}
	
	/**
	 * Creation of parameterized constructor makes this
	 * default constructor also mandatory for a pojo
	 */
	public Lswebparserinstrument() {}


}
