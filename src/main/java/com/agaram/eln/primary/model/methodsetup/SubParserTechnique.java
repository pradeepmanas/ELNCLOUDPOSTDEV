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
import com.agaram.eln.primary.model.methodsetup.ParserField;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * This class is used to map the fields of 'subparsertechnique' table of the Database.
 * @author ATE153
 * @version 1.0.0
 * @since   18- Mar- 2020
 */
@JsonIgnoreProperties(value = {"hibernateLazyInitializer"})
@XmlRootElement  (name = "subparsertechnique")
@XmlType(propOrder = { "subparsertechniquekey", "parserfield", "methoddelimiter", "inputfields", "outputfields",
		 "status", "createdby", "createddate"})
@Entity
@Table(name = "subparsertechnique")
public class SubParserTechnique implements Serializable, Diffable<SubParserTechnique>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id 
	@Column(name = "subparsertechniquekey", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer subparsertechniquekey;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "parserfieldkey", nullable = false)
	private ParserField parserfield;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "methoddelimiterkey", nullable = false)
	private MethodDelimiter methoddelimiter;
	
	@Column(name = "inputfields")
	private String inputfields;
	
	@Column(name = "outputfields")
	private String outputfields;
	
	@Range(min=-1, max=1)
	@Column(name = "status")
	private int status=1;	

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "usercode", nullable = false)
	private LSuserMaster createdby;
	
	@Column(name = "createddate")
	private Date createddate;	

	@XmlAttribute	
	public Integer getSubparsertechniquekey() {
		return subparsertechniquekey;
	}

	public void setSubparsertechniquekey(Integer subparsertechniquekey) {
		this.subparsertechniquekey = subparsertechniquekey;
	}

	@XmlElement
	public ParserField getParserfield() {
		return parserfield;
	}

	public void setParserfield(ParserField parserfield) {
		this.parserfield = parserfield;
	}

	@XmlElement
	public MethodDelimiter getMethoddelimiter() {
		return methoddelimiter;
	}

	public void setMethoddelimiter(MethodDelimiter methoddelimiter) {
		this.methoddelimiter = methoddelimiter;
	}

	@XmlElement
	public String getInputfields() {
		return inputfields;
	}

	public void setInputfields(String inputfields) {
		this.inputfields = inputfields;
	}		
	
	@XmlElement	(nillable=true)
	public String getOutputfields() {
		return outputfields;
	}

	public void setOutputfields(String outputfields) {
		this.outputfields = outputfields;
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
	public DiffResult diff(SubParserTechnique obj) {
		
	     return new DiffBuilder(this, obj, ToStringStyle.SHORT_PREFIX_STYLE)
	       .append("parserfield", this.parserfield, obj.parserfield)
	       .append("methoddelimiter", this.methoddelimiter.getDelimiter().getDelimitername(),obj.methoddelimiter.getDelimiter().getDelimitername()) 
	       .append("inputfields",this.inputfields, obj.inputfields)
	       .append("outputfields",this.outputfields, obj.outputfields)
	       .append("status",this.status, obj.status)
	       .append("createdby", this.createdby.getUsername(), obj.createdby.getUsername())
	       .append("createddate", this.createddate, obj.createddate)
	       .build();
	}

	/**
	 * Creation of parameterized constructor makes this
	 * default constructor also mandatory for a pojo
	 */
	public SubParserTechnique() {
	}

	/**
	 * This constructor is mandatory for a pojo class to perform deep copy of
	 * object
	 * @param subParserTechnique[SubParserTechnique]
	 */
	public SubParserTechnique(SubParserTechnique subParserTechnique) {
		this.subparsertechniquekey = subParserTechnique.subparsertechniquekey;
		this.parserfield = subParserTechnique.parserfield;
		this.methoddelimiter = subParserTechnique.methoddelimiter;
		this.inputfields = subParserTechnique.inputfields;
		this.outputfields = subParserTechnique.outputfields;
		this.status = subParserTechnique.status;
		this.createdby = subParserTechnique.createdby;
		this.createddate = subParserTechnique.createddate;
	}	
}
