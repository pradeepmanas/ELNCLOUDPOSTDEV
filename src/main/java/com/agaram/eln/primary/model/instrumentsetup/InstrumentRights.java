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
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.Diffable;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.agaram.eln.primary.model.instrumentsetup.InstrumentMaster;
import com.agaram.eln.primary.model.methodsetup.InstantDateAdapter;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;

/**
 * This class is used to map the fields of 'instrumentrights' table of the Database.
 * @author ATE153
 * @version 1.0.0
 * @since   05- Dec- 2019
 */
@XmlRootElement  (name = "instrumentrights")
@XmlType(propOrder = { "instrightskey", "usersite", "master",
		 "status", "createdby", "createddate"})
@Entity
@Table(name = "instrumentrights")
public class InstrumentRights implements Serializable, Diffable<InstrumentRights> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id 
	@Column(name = "instrightskey", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer instrightskey;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "sitecode", nullable = false)
	private LSSiteMaster usersite;	
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "instmasterkey", nullable = false)
	private InstrumentMaster master;
	
	@Column(name = "status")
	private Integer status=1;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "usercode", nullable = false)
	private LSuserMaster createdby;
	
	@Column(name = "createddate")
	private Date createddate;
	
	@XmlAttribute	
	public Integer getInstrightskey() {
		return instrightskey;
	}

	public void setInstrightskey(Integer instrightskey) {
		this.instrightskey = instrightskey;
	}

	@XmlElement
	public LSSiteMaster getUsersite() {
		return usersite;
	}

	public void setUsersite(LSSiteMaster usersite) {
		this.usersite = usersite;
	}

	@XmlElement
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@XmlElement
	public InstrumentMaster getMaster() {
		return master;
	}

	public void setMaster(InstrumentMaster master) {
		this.master = master;
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
	public DiffResult diff(InstrumentRights obj) {
	     return new DiffBuilder(this, obj, ToStringStyle.SHORT_PREFIX_STYLE)
	       .append("usersite", this.usersite.getSitename(), obj.usersite.getSitename()) 
	       .append("master", this.master.getInstrumentcode(), obj.master.getInstrumentcode())	     
	       .append("status",this.status, obj.status)
	       .append("createdby", this.createdby.getUsername(), obj.createdby.getUsername())
	       .append("createddate", this.createddate, obj.createddate)
	       .build();
	}

	/**
	 * This constructor is mandatory for a pojo class to perform deep copy of
	 * object
	 * @param instRights [InstrumentRights]
	 */
	public InstrumentRights(InstrumentRights instRights)
	{
		this.instrightskey = instRights.instrightskey;
		this.usersite = instRights.usersite;
		this.master = instRights.master;
		this.status = instRights.status;
		this.createdby = instRights.createdby;
		this.createddate = instRights.createddate;
	}
	
	/**
	 * Creation of parameterized constructor makes this
	 * default constructor also mandatory for a pojo
	 */
	public InstrumentRights() {}
}
