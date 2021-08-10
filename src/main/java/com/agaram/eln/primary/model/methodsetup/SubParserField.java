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
import com.agaram.eln.primary.model.methodsetup.ParserField;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * This class is used to map the fields of 'subparserfield' table of the Database.
 * @author ATE153
 * @version 1.0.0
 * @since   18- Mar- 2020
 */
@JsonIgnoreProperties(value = {"hibernateLazyInitializer"})
@XmlRootElement  (name = "subparserfield")
@XmlType(propOrder = { "subparserfieldkey", "parserfield", "subparserfieldname", "fieldid", "subparserfieldtype",
		 "subparserfieldposition", "name", "status", "createdby", "createddate"})
@Entity
@Table(name = "subparserfield")
public class SubParserField implements Serializable, Diffable<SubParserField>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id 
	@Column(name = "subparserfieldkey", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer subparserfieldkey;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "parserfieldkey", nullable = false)
	private ParserField parserfield;
	
	@Column(name = "subparserfieldname")
	private String subparserfieldname;
	
	@Column(name = "fieldid")
	private String fieldid;	
	
	@Column(name = "subparserfieldtype")
	private String subparserfieldtype;
	
	@Column(name = "subparserfieldposition")
	private String subparserfieldposition;
	
	@Column(name = "name")
	private String name;
	
	@Range(min=-1, max=1)
	@Column(name = "status")
	private int status=1;	

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "usercode", nullable = false)
	private LSuserMaster createdby;
	
	@Column(name = "createddate")
	private Date createddate;	

	@XmlAttribute	
	public Integer getSubparserfieldkey() {
		return subparserfieldkey;
	}

	public void setSubparserfieldkey(Integer subparserfieldkey) {
		this.subparserfieldkey = subparserfieldkey;
	}

	@XmlElement
	public String getSubparserfieldname() {
		return subparserfieldname;
	}

	public void setSubparserfieldname(String subparserfieldname) {
		this.subparserfieldname = subparserfieldname;
	}	
	
	@XmlElement
	public ParserField getParserfield() {
		return parserfield;
	}
	
	@XmlElement	
	public String getFieldid() {
		return fieldid;
	}

	public void setFieldid(String fieldid) {
		this.fieldid = fieldid;
	}
	
	public void setParserfield(ParserField parserfield) {
		this.parserfield = parserfield;
	}

	@XmlElement
	public String getSubparserfieldtype() {
		return subparserfieldtype;
	}

	public void setSubparserfieldtype(String subparserfieldtype) {
		this.subparserfieldtype = subparserfieldtype;
	}

	@XmlElement
	public String getSubparserfieldposition() {
		return subparserfieldposition;
	}

	public void setSubparserfieldposition(String subparserfieldposition) {
		this.subparserfieldposition = subparserfieldposition;
	}

	@XmlElement
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
	public DiffResult diff(SubParserField obj) {
		
	     return new DiffBuilder(this, obj, ToStringStyle.SHORT_PREFIX_STYLE)
	       .append("subparserfieldname", this.subparserfieldname, obj.subparserfieldname)
	       .append("fieldid", this.fieldid, obj.fieldid)
	       .append("parserfield", this.parserfield, obj.parserfield)
	       .append("subparserfieldtype", this.subparserfieldtype, obj.subparserfieldtype)
	       .append("subparserfieldposition", this.subparserfieldposition, obj.subparserfieldposition)
	       .append("name", this.name, obj.name)
	       .append("status",this.status, obj.status)
	       .append("createdby", this.createdby.getUsername(), obj.createdby.getUsername())
	       .append("createddate", this.createddate, obj.createddate)
	       .build();
	}

	/**
	 * This constructor is mandatory for a pojo class to perform deep copy of
	 * object
	 * @param subParserField [SubParserField]
	 */
	public SubParserField(SubParserField subParserField) {
		this.subparserfieldkey = subParserField.subparserfieldkey;
		this.parserfield = subParserField.parserfield;
		this.subparserfieldname = subParserField.subparserfieldname;
		this.fieldid = subParserField.fieldid;
		this.subparserfieldposition = subParserField.subparserfieldposition;
		this.subparserfieldtype = subParserField.subparserfieldtype;
		this.name = subParserField.name;
		this.status = subParserField.status;
		this.createdby = subParserField.createdby;
		this.createddate = subParserField.createddate;
	}

	/**
	 * Creation of parameterized constructor makes this
	 * default constructor also mandatory for a pojo
	 */
	public SubParserField() {
	}
	
}
