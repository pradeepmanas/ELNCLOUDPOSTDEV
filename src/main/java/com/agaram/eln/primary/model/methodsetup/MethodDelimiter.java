package com.agaram.eln.primary.model.methodsetup;
import javax.persistence.Transient;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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
import com.agaram.eln.primary.model.cfr.LScfttransaction;
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
@XmlType(propOrder = { "methoddelimiterkey", "parsermethod", "delimiter", "status", "createdby", "createddate","transactiondate",
		"screenname","displayvalue","defaultvalue","objsilentaudit","objmanualaudit"})
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
	
	@Transient
	private String info;
	
	@Transient
	private String username;
	
	@Transient
	private LScfttransaction objsilentaudit;
	
	@Transient
	private LScfttransaction objmanualaudit;
	
	@Transient
	@Temporal(TemporalType.TIMESTAMP)
	Date transactiondate;
	
	@Transient
	@Column(name = "displayvalue")
	private String displayvalue;

	@Transient
	@Column(name = "screenname")
	private String screenname;


	@Column(name = "defaultvalue")
	private Integer defaultvalue;	
		
	
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

	
	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
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
		
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	
	public Date getTransactiondate() {
		return transactiondate;
	}

	public void setTransactiondate(Date transactiondate) {
		this.transactiondate = transactiondate;
	}
	

	public String getDisplayvalue() {
		return displayvalue;
	}

	public void setDisplayvalue(String displayvalue) {
		this.displayvalue = displayvalue;
	}

	public String getScreenname() {
		return screenname;
	}

	public void setScreenname(String screenname) {
		this.screenname = screenname;
	}

	
	
	public Integer getDefaultvalue() {
		return defaultvalue;
	}

	public void setDefaultvalue(Integer defaultvalue) {
		this.defaultvalue = defaultvalue;
	}
	public LScfttransaction getObjsilentaudit() {
		return objsilentaudit;
	}

	public void setObjsilentaudit(LScfttransaction objsilentaudit) {
		this.objsilentaudit = objsilentaudit;
	}

	public LScfttransaction getObjmanualaudit() {
		return objmanualaudit;
	}

	public void setObjmanualaudit(LScfttransaction objmanualaudit) {
		this.objmanualaudit = objmanualaudit;
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
	       .append("username", this.username, obj.username)
	       .append("transactiondate", this.transactiondate, obj.transactiondate)
	       .append("displayvalue", this.displayvalue, obj.displayvalue)
	       .append("screenname", this.screenname, obj.screenname)
	       .append("defaultvalue", this.defaultvalue, obj.defaultvalue)
	       .append("objsilentaudit", this.objsilentaudit, obj.objsilentaudit)
           .append("objmanualaudit", this.objmanualaudit, obj.objmanualaudit)
           .append("info", this.info, obj.info)


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
		this.username = methodDelimiter.username;
		this.transactiondate = methodDelimiter.transactiondate;
		this.displayvalue = methodDelimiter.displayvalue;
        this.screenname = methodDelimiter.screenname;
        this.defaultvalue = methodDelimiter.defaultvalue;
    	this.objsilentaudit = methodDelimiter.objsilentaudit;
    	this.objmanualaudit = methodDelimiter.objmanualaudit;
    	this.info = methodDelimiter.info;
      


	}
	
	/**
	 * Creation of parameterized constructor makes this
	 * default constructor also mandatory for a pojo
	 */
	public MethodDelimiter() {}

}
