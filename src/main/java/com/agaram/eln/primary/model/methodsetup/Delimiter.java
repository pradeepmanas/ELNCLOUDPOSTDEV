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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
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

import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * This class is used to map the fields of 'delimiter' table of the Database.
 * @author ATE153
 * @version 1.0.0
 * @since   04- Feb- 2020
 */
@JsonIgnoreProperties(value = {"hibernateLazyInitializer"})
@XmlRootElement  (name = "delimiter")
@XmlType(propOrder = { "delimiterkey", "delimitername", "actualdelimiter", 
		 "status", "createdby", "createddate" , "username","transactiondate","defaultvalue","displayvalue","screenname","objsilentaudit","objmanualaudit"})
@Entity
@Table(name = "delimiter")
public class Delimiter implements Serializable, Diffable<Delimiter>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id 
	@Column(name = "delimiterkey", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer delimiterkey;
	
	@Column(name = "delimitername")
	private String delimitername;	
	
	@Column(name = "actualdelimiter")
	private String actualdelimiter;	
	
	@Column(name = "delimiterstatus")
	private String delimiterstatus;
	
	@Range(min=-1, max=1)
	@Column(name = "status")
	private int status=1;	

	@Transient
	private String username;

    @ManyToOne
	private LSSiteMaster lssitemaster;
    
	@Column(name = "defaultvalue")
	//public int defaultvalue;	
	public Integer defaultvalue;
	
	@Transient
	private LScfttransaction objsilentaudit;
	
	@Transient
	public String info;
	
	@Transient
	private LScfttransaction objmanualaudit;
	
	@Transient
	@Temporal(TemporalType.TIMESTAMP)
	Date transactiondate;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "usercode", nullable = false)
	private LSuserMaster createdby;
	
	@Column(name = "createddate")
	private Date createddate;	
		
	@Transient
	@Column(name = "displayvalue")
	private String displayvalue;

	@Transient
	@Column(name = "screenname")
	private String screenname;

	@XmlAttribute	
	public Integer getDelimiterkey() {
		return delimiterkey;
	}

	public void setDelimiterkey(Integer delimiterkey) {
		this.delimiterkey = delimiterkey;
	}

	@XmlElement	
	public String getDelimitername() {
		return delimitername;
	}

	public void setDelimitername(String delimitername) {
		this.delimitername = delimitername;
	}

	@XmlElement	
	public String getActualdelimiter() {
		return actualdelimiter;
	}

	public void setActualdelimiter(String actualdelimiter) {
		this.actualdelimiter = actualdelimiter;
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

	public Integer getDefaultvalue() {
		return defaultvalue;
	}

	public void setDefaultvalue(Integer defaultvalue) {
		this.defaultvalue = defaultvalue;
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

	
	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	
	public LSSiteMaster getLssitemaster() {
		return lssitemaster;
	}

	public void setLssitemaster(LSSiteMaster lssitemaster) {
		this.lssitemaster = lssitemaster;
	}

	
	public String getDelimiterstatus() {
		if(delimiterstatus != null)
		{
		//return  delimiterstatus.trim().equals("A")?"Active":"Retired";
			return  status == 1 ?"Active":"Retired";
		}
		else
		{
			return "";
		}

	}

	public void setDelimiterstatus(String delimiterstatus) {
		this.delimiterstatus = delimiterstatus;
	}

	/**
	 * 
	 * To find difference between two entity objects by implementing Diffable interface  
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public DiffResult diff(Delimiter obj) {
	     return new DiffBuilder(this, obj, ToStringStyle.SHORT_PREFIX_STYLE)
	       .append("delimitername", this.delimitername, obj.delimitername) 
	       .append("actualdelimiter", this.actualdelimiter, obj.actualdelimiter)	      
	       .append("status",this.status, obj.status)
	       .append("createdby", this.createdby.getUsername(), obj.createdby.getUsername())
	       .append("createddate", this.createddate, obj.createddate)
	       .append("username", this.username, obj.username)
	       .append("transactiondate", this.transactiondate, obj.transactiondate)
	       .append("defaultvalue", this.defaultvalue, obj.defaultvalue)
	       .append("displayvalue", this.displayvalue, obj.displayvalue)
           .append("screenname", this.screenname, obj.screenname)
           .append("objsilentaudit", this.objsilentaudit, obj.objsilentaudit)
           .append("objmanualaudit", this.objmanualaudit, obj.objmanualaudit)
           .append("info", this.info, obj.info)
           
           .build();
	}

	/**
	 * This constructor is mandatory for a pojo class to perform deep copy of
	 * object
	 * @param delimiter [Delimiter]
	 */
	public Delimiter(Delimiter delimiter)
	{
		this.delimiterkey = delimiter.delimiterkey;
		this.delimitername = delimiter.delimitername;
		this.actualdelimiter = delimiter.actualdelimiter;
		this.status = delimiter.status;
		this.createdby = delimiter.createdby;
		this.createddate = delimiter.createddate;
		this.username = delimiter.username;
		this.transactiondate = delimiter.transactiondate;
		this.defaultvalue = delimiter.defaultvalue;
		this.displayvalue = delimiter.displayvalue;
		this.screenname = delimiter.screenname;
		
		this.objsilentaudit = delimiter.objsilentaudit;
		this.objmanualaudit = delimiter.objmanualaudit;
		this.info = delimiter.info;

	}
	
	/**
	 * Creation of parameterized constructor makes this
	 * default constructor also mandatory for a pojo
	 */
	public Delimiter() {}
}
