package com.agaram.eln.primary.model.webParser;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name = "Lswebparserinstrumentcategory")
public class Lswebparserinstrumentcategory implements Serializable, Diffable<Lswebparserinstrumentcategory>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id 
	@Column(name = "instcategorykey", nullable = false)
	private Integer instcatkey;
	
	@Column(name = "instcategoryname")
	private String instcatname;
	
	@Column(name = "status")
	private int status=1;
	
	@Transient
	private Lswebparseruser createdby;
	
	@Transient
	private Lswebparsersite site;
	
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
	public DiffResult diff(Lswebparserinstrumentcategory obj) {
	     return new DiffBuilder(this, obj, ToStringStyle.SHORT_PREFIX_STYLE)
	       .append("instcatname", this.instcatname, obj.instcatname)	    
	       .append("status",this.status, obj.status)
	       .append("createdby", this.createdby.getFirstname(), obj.createdby.getFirstname())
	       .append("createddate", this.createddate, obj.createddate)
	       .build();
	}

	/**
	 * This constructor is mandatory for a pojo class to perform deep copy of
	 * object
	 * @param instrumentCategory [Role]
	 */
	public Lswebparserinstrumentcategory(Lswebparserinstrumentcategory instrumentCategory)
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
	public Lswebparserinstrumentcategory() {}

}
