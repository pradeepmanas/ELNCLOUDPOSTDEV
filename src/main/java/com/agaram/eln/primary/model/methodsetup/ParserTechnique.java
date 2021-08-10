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
import com.agaram.eln.primary.model.methodsetup.ParserMethod;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * This class is used to map the fields of '"parsertechnique' table of the Database.
 * @author ATE153
 * @version 1.0.0
 * @since   15- Mar- 2020
 */
@JsonIgnoreProperties(value = {"hibernateLazyInitializer"})
@XmlRootElement  (name = "parsertechnique")
@XmlType(propOrder = { "parsertechniquekey", "parserfield", "parsermethod", "row", "col",
		"rows", "cols",  "identificationtext", "status", "createdby", "createddate"})
@Entity
@Table(name = "parsertechnique")
public class ParserTechnique  implements Serializable, Diffable<ParserTechnique>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id 
	@Column(name = "parsertechniquekey", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer parsertechniquekey;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "parserfieldkey", nullable = false)
	private  ParserField parserfield;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "parsermethodkey", nullable = false)
	private ParserMethod parsermethod;
	
	@Column(name = "row")
	private Integer row;
	
	@Column(name = "col")
	private Integer col;
	
	@Column(name = "rows")
	private Integer rows;
	
	@Column(name = "cols")
	private Integer cols;
	
	@Column(name = "identificationtext")
	private String identificationtext;
	
	@Range(min=-1, max=1)
	@Column(name = "status")
	private int status=1;	

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "usercode", nullable = false)
	private LSuserMaster createdby;
	
	@Column(name = "createddate")
	private Date createddate;	

	@XmlAttribute	
	public Integer getParsertechniquekey() {
		return parsertechniquekey;
	}

	public void setParsertechniquekey(Integer parsertechniquekey) {
		this.parsertechniquekey = parsertechniquekey;
	}

	@XmlElement	
	public ParserField getParserfield() {
		return parserfield;
	}

	public void setParserfield(ParserField parserfield) {
		this.parserfield = parserfield;
	}

	@XmlElement	
	public ParserMethod getParsermethod() {
		return parsermethod;
	}

	public void setParsermethod(ParserMethod parsermethod) {
		this.parsermethod = parsermethod;
	}

	@XmlElement	
	public Integer getRow() {
		return row;
	}

	public void setRow(Integer row) {
		this.row = row;
	}

	@XmlElement	
	public Integer getCol() {
		return col;
	}

	public void setCol(Integer col) {
		this.col = col;
	}

	@XmlElement	
	public Integer getRows() {
		return rows;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}

	@XmlElement	
	public Integer getCols() {
		return cols;
	}

	public void setCols(Integer cols) {
		this.cols = cols;
	}

	@XmlElement	
	public String getIdentificationtext() {
		return identificationtext;
	}

	public void setIdentificationtext(String identificationtext) {
		this.identificationtext = identificationtext;
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
	 * Creation of parameterized constructor makes this
	 * default constructor also mandatory for a pojo
	 */
	public ParserTechnique() {}
	
	/**
	 * This constructor is mandatory for a pojo class to perform deep copy of
	 * object
	 * @param parserTechnique [ParserTechnique]
	 */
	public ParserTechnique(ParserTechnique parserTechnique) {
		this.col = parserTechnique.col;
		this.cols = parserTechnique.cols;		
		this.parserfield = parserTechnique.parserfield;
		this.identificationtext = parserTechnique.identificationtext;
		this.parsermethod = parserTechnique.parsermethod;
		this.parsertechniquekey = parserTechnique.parsertechniquekey;
		this.row = parserTechnique.row;
		this.rows = parserTechnique.rows;
		this.status = parserTechnique.status;
		this.createdby = parserTechnique.createdby;
		this.createddate = parserTechnique.createddate;
	}
	
	/**
	 * To find difference between two entity objects by implementing Diffable interface  
	 */
	@Override
	public DiffResult diff(ParserTechnique obj) {
		
	     return new DiffBuilder(this, obj, ToStringStyle.SHORT_PREFIX_STYLE)
	       .append("col", this.col, obj.col) 
	       .append("cols", this.cols, obj.cols)
	       .append("row", this.row, obj.row)
	       .append("rows", this.rows, obj.rows)
	       .append("identificationtext", this.identificationtext, obj.identificationtext)
	       .append("parsermethod", this.parsermethod.getParsermethodname(), obj.parsermethod.getParsermethodname())
	       .append("parserfield", this.parserfield.getParserfieldname(), obj.parserfield.getParserfieldname())
	       .append("status",this.status, obj.status)
	       .append("createdby", this.createdby.getUsername(), obj.createdby.getUsername())
	       .append("createddate", this.createddate, obj.createddate)
	       .build();
	}
	
}
