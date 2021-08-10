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
import com.agaram.eln.primary.model.methodsetup.MethodDelimiter;
import com.agaram.eln.primary.model.methodsetup.ParserBlock;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * This class is used to map the fields of 'parserfield' table of the Database.
 * @author ATE153
 * @version 1.0.0
 * @since   18- Mar- 2020
 */
@JsonIgnoreProperties(value = {"hibernateLazyInitializer"})
@XmlRootElement  (name = "parserfield")
@XmlType(propOrder = { "parserfieldkey", "parserfieldname", "fieldid", "parserblock", "methoddelimiter",
		"status", "createdby", "createddate"})
@Entity
@Table(name = "parserfield")
public class ParserField implements Serializable, Diffable<ParserField>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id 
	@Column(name = "parserfieldkey", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer parserfieldkey;
	
	@Column(name = "parserfieldname")
	private String parserfieldname;	
	
	@Column(name = "fieldid")
	private String fieldid;	

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "parserblockkey", nullable = false)
	private ParserBlock parserblock;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "methoddelimiterkey", nullable = false)
	private MethodDelimiter methoddelimiter;

	@Range(min=-1, max=1)
	@Column(name = "status")
	private int status=1;	

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "usercode", nullable = false)
	private LSuserMaster createdby;
	
	@Column(name = "createddate")
	private Date createddate;	
			
	@XmlAttribute	
	public Integer getParserfieldkey() {
		return parserfieldkey;
	}

	public void setParserfieldkey(Integer parserfieldkey) {
		this.parserfieldkey = parserfieldkey;
	}

	@XmlElement	
	public String getParserfieldname() {
		return parserfieldname;
	}

	public void setParserfieldname(String parserfieldname) {
		this.parserfieldname = parserfieldname;
	}
	
	@XmlElement	
	public String getFieldid() {
		return fieldid;
	}

	public void setFieldid(String fieldid) {
		this.fieldid = fieldid;
	}

	@XmlElement	
	public ParserBlock getParserblock() {
		return parserblock;
	}

	public void setParserblock(ParserBlock parserblock) {
		this.parserblock = parserblock;
	}

	@XmlElement	
	public MethodDelimiter getMethoddelimiter() {
		return methoddelimiter;
	}

	public void setMethoddelimiter(MethodDelimiter methoddelimiter) {
		this.methoddelimiter = methoddelimiter;
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
	public DiffResult diff(ParserField obj) {
		
	     return new DiffBuilder(this, obj, ToStringStyle.SHORT_PREFIX_STYLE)
	       .append("parserfieldname", this.parserfieldname, obj.parserfieldname)
	       .append("fieldid", this.fieldid, obj.fieldid)
	       .append("parserblock", this.parserblock.getParserblockname(), obj.parserblock.getParserblockname())
	       .append("methoddelimiter", this.methoddelimiter.getDelimiter().getDelimitername(),obj.methoddelimiter.getDelimiter().getDelimitername()) 
	       .append("status",this.status, obj.status)
	       .append("createdby", this.createdby.getUsername(), obj.createdby.getUsername())
	       .append("createddate", this.createddate, obj.createddate)
	       .build();
	}

	/**
	 * Creation of parameterized constructor makes this
	 * default constructor also mandatory for a pojo
	 */
	public ParserField() {
	}

	/**
	 * This constructor is mandatory for a pojo class to perform deep copy of
	 * object
	 * @param parserField [ParserField]
	 */
	public ParserField(ParserField parserField) {		
		this.parserfieldkey = parserField.parserfieldkey;
		this.parserfieldname = parserField.parserfieldname;
		this.fieldid = parserField.fieldid;
		this.parserblock = parserField.parserblock;
		this.methoddelimiter = parserField.methoddelimiter;
		this.status = parserField.status;
		this.createdby = parserField.createdby;
		this.createddate = parserField.createddate;
	}	
	
}
