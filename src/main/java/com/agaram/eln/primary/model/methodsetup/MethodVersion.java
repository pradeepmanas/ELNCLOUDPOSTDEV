package com.agaram.eln.primary.model.methodsetup;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * This class is used to map the fields of 'method' table of the Database.
 * @author ATE153
 * @version 1.0.0
 * @since   04- Feb- 2020
 */
@JsonIgnoreProperties(value = {"hibernateLazyInitializer"})
@XmlRootElement  (name = "methodversion")
@XmlType(propOrder = { "methodkey", "instrawdataurl",
		 "status", "createddate","username","transactiondate","filename",})
@Entity
@Table(name = "methodversion")
public class MethodVersion implements Serializable, Diffable<MethodVersion>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id 
	@Column(name = "mvno", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	
	private Integer mvno;
	private Integer methodkey;
	

	@Column(name = "instrawdataurl")
	private String instrawdataurl;	
	
	public Integer version;

	
//	@Transient
//	private String tenantid;
	
	@Column(name = "blobid")
	private String blobid;	

	@Column(columnDefinition = "varchar(255)")
	private String filename;	
	
	@Range(min=-1, max=1)
	@Column(name = "status")
	private int status=1;	
	@Column(name = "createddate")
	private Date createddate;	
			
	@XmlAttribute	
	public Integer getMethodkey() {
		return methodkey;
	}

	public void setMethodkey(Integer methodkey) {
		this.methodkey = methodkey;
	}

	

	public String getBlobid() {
		return blobid;
	}

	public void setBlobid(String blobid) {
		this.blobid = blobid;
	}



//	@Transient
//	private String username;
	

	@XmlElement	
	public String getInstrawdataurl() {
		return instrawdataurl;
	}

	public void setInstrawdataurl(String instrawdataurl) {
		this.instrawdataurl = instrawdataurl;
	}

	@XmlElement	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}


	public Integer getMvno() {
		return mvno;
	}

	public void setMvno(Integer mvno) {
		this.mvno = mvno;
	}

	@XmlElement	
	@XmlJavaTypeAdapter(InstantDateAdapter.class)
	public Date getCreateddate() {
		return createddate;
	}

	public void setCreateddate(Date createddate) {
		this.createddate = createddate;
	}

		
//	public String getUsername() {
//		return username;
//	}
//
//	public void setUsername(String username) {
//		this.username = username;
//	}


	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
	

//	public String getTenantid() {
//		return tenantid;
//	}
//
//	public void setTenantid(String tenantid) {
//		this.tenantid = tenantid;
//	}


	
	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	/**
	 * To find difference between two entity objects by implementing Diffable interface  
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public DiffResult diff(MethodVersion obj) {
		
	     return new DiffBuilder(this, obj, ToStringStyle.SHORT_PREFIX_STYLE)
	    		 
	  	   .append("mvno", this.mvno, obj.mvno)    

	       .append("methodkey", this.methodkey, obj.methodkey)    
	       .append("instrawdataurl", this.instrawdataurl, obj.instrawdataurl)
	       .append("status",this.status, obj.status)
	       .append("createddate", this.createddate, obj.createddate)
	    //   .append("username", this.username, obj.username)

	       .append("blobid", this.blobid, obj.blobid)
	       .append("filename", this.filename, obj.filename)


	  //     .append("tenantid", this.tenantid, obj.tenantid)
  
           .append("version", this.version, obj.version)




	       .build();
	}

//	private Object getInstCode(InstrumentMaster instrumentMaster)
//	{
//		if (instrumentMaster == null)
//		{
//			return null;
//		}
//		else {
//			return instrumentMaster.getInstrumentcode();
//		}		
//	}

	/**
	 * This constructor is mandatory for a pojo class to perform deep copy of
	 * object
	 * @param method [Method]
	 */
	public MethodVersion(MethodVersion methodversion)
	{
		this.mvno = methodversion.mvno;
		this.methodkey = methodversion.methodkey;

		this.instrawdataurl = methodversion.instrawdataurl;
	
		this.status = methodversion.status;

		this.createddate = methodversion.createddate;
		//this.username = methodversion.username;

		this.filename=methodversion.filename;
		
		this.blobid=methodversion.blobid;

      //  this.tenantid=methodversion.tenantid;
	
		
		this.version = methodversion.version;


	}
	
	/**
	 * Creation of parameterized constructor makes this
	 * default constructor also mandatory for a pojo
	 */
	public MethodVersion() {}

	
}
