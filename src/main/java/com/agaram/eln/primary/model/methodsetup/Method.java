package com.agaram.eln.primary.model.methodsetup;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
import com.agaram.eln.primary.model.instrumentsetup.InstrumentMaster;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * This class is used to map the fields of 'method' table of the Database.
 * @author ATE153
 * @version 1.0.0
 * @since   04- Feb- 2020
 */
@JsonIgnoreProperties(value = {"hibernateLazyInitializer"})
@XmlRootElement  (name = "method")
@XmlType(propOrder = { "methodkey", "methodname", "instmaster", "instrawdataurl",
		"samplesplit", "parser", "site", "status", "createdby", "createddate","username","transactiondate","filename","displayvalue",
		"screenname","objsilentaudit","objmanualaudit","info","converterstatus"})
@Entity
@Table(name = "method")
public class Method implements Serializable, Diffable<Method>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id 
	@Column(name = "methodkey", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer methodkey;
	
	@Column(name = "methodname")
	private String methodname;	

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "instmasterkey", nullable = false)
	private InstrumentMaster instmaster;	
	
	@Column(name = "instrawdataurl")
	private String instrawdataurl;	
	
	public Integer version=1;

	private Integer converterstatus;
	
	@Transient
	private LSSiteMaster lssitemaster;
	
	@Column(name = "methodstatus")
	private String methodstatus;
	
	@OneToMany
	@JoinColumn(name="methodkey")
	private List<MethodVersion> methodversion;

	@Transient
	@Column(name = "displayvalue")
	private String displayvalue;

	@Transient
	@Column(name = "screenname")
	private String screenname;

	@Transient
	private LScfttransaction objsilentaudit;
	
	@Transient
	private String info;
	
	@Transient
	private LScfttransaction objmanualaudit;
	
	@Transient
	private String tenantid;
	
	@Column(columnDefinition = "varchar(255)")
	private String filename;	
	
	
	@Column(name = "samplesplit")
	private Integer samplesplit;
	
	@Column(name = "parser")
	private Integer parser;
		
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "sitecode", nullable = false)
	private LSSiteMaster site;
	
	@Range(min=-1, max=1)
	@Column(name = "status")
	private int status=1;	

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "usercode", nullable = false)
	private LSuserMaster createdby;
	
	@Column(name = "createddate")
	private Date createddate;	
			
	@XmlAttribute	
	public Integer getMethodkey() {
		return methodkey;
	}

	public void setMethodkey(Integer methodkey) {
		this.methodkey = methodkey;
	}

	@XmlElement	
	public String getMethodname() {
		return methodname;
	}

	public void setMethodname(String methodname) {
		this.methodname = methodname;
	}

	@XmlElement	(nillable=true)
	public InstrumentMaster getInstmaster() {
		return instmaster;
	}

	@Transient
	private String username;
	
	@Transient
	@Temporal(TemporalType.TIMESTAMP)
	Date transactiondate;
	
	public void setInstmaster(InstrumentMaster instmaster) {
		this.instmaster = instmaster;
	}

	@XmlElement	
	public String getInstrawdataurl() {
		return instrawdataurl;
	}

	public void setInstrawdataurl(String instrawdataurl) {
		this.instrawdataurl = instrawdataurl;
	}

	@XmlElement	(nillable=true)
	public Integer getSamplesplit() {
		return samplesplit;
	}

	public void setSamplesplit(Integer samplesplit) {
		this.samplesplit = samplesplit;
	}
	
	@XmlElement	(nillable=true)	
	public Integer getParser() {
		return parser;
	}

	public void setParser(Integer parser) {
		this.parser = parser;
	}

	@XmlElement
	public LSSiteMaster getSite() {
		return site;
	}

	public void setSite(LSSiteMaster site) {
		this.site = site;
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
	
	
	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
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

	public String getTenantid() {
		return tenantid;
	}

	public void setTenantid(String tenantid) {
		this.tenantid = tenantid;
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

	
	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	
	public List<MethodVersion> getMethodversion() {
		return methodversion;
	}

	public void setMethodversion(List<MethodVersion> methodversion) {
		this.methodversion = methodversion;
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

	public Integer getConverterstatus() {
		return converterstatus;
	}

	public void setConverterstatus(Integer converterstatus) {
		this.converterstatus = converterstatus;
	}

	public String getMethodstatus() {
		//return methodstatus;
		if(methodstatus != null)
		{
//		return  methodstatus.trim().equals("A")?"Active":"Retired";
			return  status == 1 ?"Active":"Retired";
		}
		else
		{
			return "";
		}
	}

	public void setMethodstatus(String methodstatus) {
		this.methodstatus = methodstatus;
	}

	/**
	 * To find difference between two entity objects by implementing Diffable interface  
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public DiffResult diff(Method obj) {
		
	     return new DiffBuilder(this, obj, ToStringStyle.SHORT_PREFIX_STYLE)
	       .append("methodname", this.methodname, obj.methodname) 
	       .append("instmaster", getInstCode(this.instmaster), getInstCode(obj.instmaster))	     
	       .append("instrawdataurl", this.instrawdataurl, obj.instrawdataurl)
	       .append("samplesplit", this.samplesplit, obj.samplesplit)
	       .append("parser", this.parser, obj.parser)
	       .append("site", this.getSite().getSitename(), obj.getSite().getSitename())
	       .append("status",this.status, obj.status)
	       .append("createdby", this.createdby.getUsername(), obj.createdby.getUsername())
	       .append("createddate", this.createddate, obj.createddate)
	       .append("username", this.username, obj.username)
	       .append("transactiondate", this.transactiondate, obj.transactiondate)
	       .append("filename", this.filename, obj.filename)
	       .append("displayvalue", this.displayvalue, obj.displayvalue)
	       .append("screenname", this.screenname, obj.screenname)

	       .append("tenantid", this.tenantid, obj.tenantid)
           .append("objsilentaudit", this.objsilentaudit, obj.objsilentaudit)
           .append("objmanualaudit", this.objmanualaudit, obj.objmanualaudit)
           .append("version", this.version, obj.version)
           .append("methodversion", this.getMethodversion(), obj.getMethodversion())
           .append("info", this.getInfo(), obj.getInfo())
           .append("converterstatus", this.getConverterstatus(), obj.getConverterstatus())
	       .build();
	}

	private Object getInstCode(InstrumentMaster instrumentMaster)
	{
		if (instrumentMaster == null)
		{
			return null;
		}
		else {
			return instrumentMaster.getInstrumentcode();
		}		
	}

	/**
	 * This constructor is mandatory for a pojo class to perform deep copy of
	 * object
	 * @param method [Method]
	 */
	public Method(Method method)
	{
		this.methodkey = method.methodkey;
		this.methodname = method.methodname;
		this.instmaster = method.instmaster;
		this.instrawdataurl = method.instrawdataurl;
		this.samplesplit = method.samplesplit;
		this.parser = method.parser;
		this.site = method.site;
		this.status = method.status;
		this.createdby = method.createdby;
		this.createddate = method.createddate;
		this.username = method.username;
		this.transactiondate=method.transactiondate;
		this.filename=method.filename;
		this.displayvalue=method.displayvalue;
        this.screenname=method.screenname;

        this.tenantid=method.tenantid;
		this.objsilentaudit = method.objsilentaudit;
		this.objmanualaudit = method.objmanualaudit;
		
		this.version = method.version;
		this.methodversion=method.methodversion;
		this.info=method.info;
		this.converterstatus=method.converterstatus;


	}
	
	/**
	 * Creation of parameterized constructor makes this
	 * default constructor also mandatory for a pojo
	 */
	public Method() {}

	
}
