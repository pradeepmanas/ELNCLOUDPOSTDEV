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

import com.agaram.eln.primary.model.methodsetup.InstantDateAdapter;
import com.agaram.eln.primary.model.methodsetup.Method;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * This class is used to map the fields of '"parserblock' table of the Database.
 * @author ATE153
 * @version 1.0.0
 * @since   18- Mar- 2020
 */
@JsonIgnoreProperties(value = {"hibernateLazyInitializer"})
@XmlRootElement  (name = "parserblock")
@XmlType(propOrder = { "parserblockkey", "parserblockname", "method", "status", "createdby", "createddate"})
@Entity
@Table(name = "parserblock")
public class ParserBlock implements Serializable, Diffable<ParserBlock>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id 
	@Column(name = "parserblockkey", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer parserblockkey;
	
	@Column(name = "parserblockname")
	private String parserblockname;	
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "methodkey", nullable = false)
	private Method method;
	
	@Range(min=-1, max=1)
	@Column(name = "status")
	private int status=1;	

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "usercode", nullable = false)
	private LSuserMaster createdby;
	
	@Column(name = "createddate")
	private Date createddate;
	
	@XmlAttribute		
	public Integer getParserblockkey() {
		return parserblockkey;
	}	

	public void setParserblockkey(Integer parserblockkey) {
		this.parserblockkey = parserblockkey;
	}

	@XmlElement	
	public String getParserblockname() {
		return parserblockname;
	}

	public void setParserblockname(String parserblockname) {
		this.parserblockname = parserblockname;
	}

	@XmlElement	
	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
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
	public DiffResult diff(ParserBlock obj) {		
	     return new DiffBuilder(this, obj, ToStringStyle.SHORT_PREFIX_STYLE)
	       .append("parserblockname", this.parserblockname, obj.parserblockname) 
	       .append("method", this.method.getMethodname(), obj.method.getMethodname()) 
	       .append("status",this.status, obj.status)
	       .append("createdby", this.createdby.getUsername(), obj.createdby.getUsername())
	       .append("createddate", this.createddate, obj.createddate)
	       .build();
	}

	/**
	 * This constructor is mandatory for a pojo class to perform deep copy of
	 * object
	 * @param parserBlock [ParserBlock]
	 */
	public ParserBlock(ParserBlock parserBlock)
	{
		this.parserblockkey = parserBlock.parserblockkey;
		this.parserblockname = parserBlock.parserblockname;
		this.createdby = parserBlock.createdby;
		this.createddate = parserBlock.createddate;
		this.status = parserBlock.status;
		this.method = parserBlock.method;
	}
	
	/**
	 * Creation of parameterized constructor makes this
	 * default constructor also mandatory for a pojo
	 */
	public ParserBlock() {}
}
