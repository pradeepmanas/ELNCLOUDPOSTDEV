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

import com.agaram.eln.primary.model.methodsetup.InstantDateAdapter;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;


/**
 * This class is used to map the fields of 'instrumentcategory' table of the Database.
 * @author ATE153
 * @version 1.0.0
 * @since   05- Dec- 2019
 */
@XmlRootElement  (name = "instrumentcategory")
@XmlType(propOrder = { "instcatkey", "instcatname",
		 "status", "createdby", "createddate"})
@Entity
@Table(name = "instrumentcategory")
public class InstrumentCategory implements Serializable, Diffable<InstrumentCategory>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id 
	@Column(name = "instcategorykey", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer instcatkey;
	
	@Column(name = "instcategoryname")
	private String instcatname;
	
	@Column(name = "status")
	private int status=1;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "usercode", nullable = false)
	private LSuserMaster createdby;
	
	@Column(name = "createddate")
	private Date createddate;

	@XmlAttribute	
	public Integer getInstcatkey() {
		return instcatkey;
	}

	public void setInstcatkey(Integer instcatkey) {
		this.instcatkey = instcatkey;
	}

	@XmlElement
	public String getInstcatname() {
		return instcatname;
	}

	public void setInstcatname(String instcatname) {
		this.instcatname = instcatname;
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
	public DiffResult diff(InstrumentCategory obj) {
	     return new DiffBuilder(this, obj, ToStringStyle.SHORT_PREFIX_STYLE)
	       .append("instcatname", this.instcatname, obj.instcatname)	    
	       .append("status",this.status, obj.status)
	       .append("createdby", this.createdby.getUsername(), obj.createdby.getUsername())
	       .append("createddate", this.createddate, obj.createddate)
	       .build();
	}

	/**
	 * This constructor is mandatory for a pojo class to perform deep copy of
	 * object
	 * @param instrumentCategory [Role]
	 */
	public InstrumentCategory(InstrumentCategory instrumentCategory)
	{
		this.instcatkey = instrumentCategory.instcatkey;
		this.instcatname = instrumentCategory.instcatname;		
		this.status = instrumentCategory.status;
		this.createdby = instrumentCategory.createdby;
		this.createddate = instrumentCategory.createddate;
	}
	
	/**
	 * Creation of parameterized constructor makes this
	 * default constructor also mandatory for a pojo
	 */
	public InstrumentCategory() {}

}
