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
import com.agaram.eln.primary.model.methodsetup.Delimiter;
import com.agaram.eln.primary.model.methodsetup.ParserMethod;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * This class is used to map the fields of 'methoddelimiter ' table of the Database.
 * @author ATE153
 * @version 1.0.0
 * @since   18- Mar- 2020
 */
@JsonIgnoreProperties(value = {"hibernateLazyInitializer"})
@XmlRootElement  (name = "methoddelimiter")
@XmlType(propOrder = { "methoddelimiterkey", "parsermethod", "delimiter", "status", "createdby", "createddate"})
@Entity
@Table(name = "methoddelimiter")
public class MethodDelimiter  implements Serializable, Diffable<MethodDelimiter>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id 
	@Column(name = "methoddelimiterkey", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer methoddelimiterkey;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "parsermethodkey", nullable = true)
	private ParserMethod parsermethod;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "delimiterkey", nullable = false)
	private  Delimiter delimiter;	
	
	@Range(min=-1, max=1)
	@Column(name = "status")
	private int status=1;	

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "usercode", nullable = false)
	private LSuserMaster createdby;
	
	@Column(name = "createddate")
	private Date createddate;

	@XmlAttribute	
	public Integer getMethoddelimiterkey() {
		return methoddelimiterkey;
	}

	public void setMethoddelimiterkey(Integer methoddelimiterkey) {
		this.methoddelimiterkey = methoddelimiterkey;
	}

	@XmlElement	
	public Delimiter getDelimiter() {
		return delimiter;
	}

	public void setDelimiter(Delimiter delimiter) {
		this.delimiter = delimiter;
	}

	@XmlElement	
	public ParserMethod getParsermethod() {
		return parsermethod;
	}

	public void setParsermethod(ParserMethod parsermethod) {
		this.parsermethod = parsermethod;
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
	public DiffResult diff(MethodDelimiter obj) {
	     return new DiffBuilder(this, obj, ToStringStyle.SHORT_PREFIX_STYLE)
	       .append("parsermethod", this.parsermethod.getParsermethodname(), obj.parsermethod.getParsermethodname()) 
	       .append("delimiter", this.delimiter.getDelimitername(), obj.delimiter.getDelimitername())	      
	       .append("status",this.status, obj.status)
	       .append("createdby", this.createdby.getUsername(), obj.createdby.getUsername())
	       .append("createddate", this.createddate, obj.createddate)
	       .build();
	}

	/**
	 * This constructor is mandatory for a pojo class to perform deep copy of
	 * object
	 * @param methodDelimiter [MethodDelimiter]
	 */
	public MethodDelimiter(MethodDelimiter methodDelimiter)
	{
		this.methoddelimiterkey = methodDelimiter.methoddelimiterkey;
		this.parsermethod = methodDelimiter.parsermethod;
		this.delimiter = methodDelimiter.delimiter;
		this.status = methodDelimiter.status;
		this.createdby = methodDelimiter.createdby;
		this.createddate = methodDelimiter.createddate;
	}
	
	/**
	 * Creation of parameterized constructor makes this
	 * default constructor also mandatory for a pojo
	 */
	public MethodDelimiter() {}

}
