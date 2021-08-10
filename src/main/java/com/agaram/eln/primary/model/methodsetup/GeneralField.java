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
import com.agaram.eln.primary.model.methodsetup.AppMaster;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * This class is used to map the fields of 'generalfield' table of the Database.
 * @author ATE153
 * @version 1.0.0
 * @since   01- Apr- 2020
 */
@JsonIgnoreProperties(value = {"hibernateLazyInitializer"})
@XmlRootElement  (name = "generalfield")
@XmlType(propOrder = { "generalfieldkey",  "appmaster", "generalfieldname", "fieldid", "site",
		 "status", "createdby", "createddate"})
@Entity
@Table(name = "generalfield")
public class GeneralField implements Serializable, Diffable<GeneralField>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id 
	@Column(name = "generalfieldkey", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer generalfieldkey;	
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "appmasterkey", nullable = false)
	private AppMaster appmaster;
	
	@Column(name = "generalfieldname")
	private String generalfieldname;
	
	@Column(name = "fieldid")
	private String fieldid;	
	
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
	public Integer getGeneralfieldkey() {
		return generalfieldkey;
	}

	public void setGeneralfieldkey(Integer generalfieldkey) {
		this.generalfieldkey = generalfieldkey;
	}

	@XmlElement
	public String getGeneralfieldname() {
		return generalfieldname;
	}

	public void setGeneralfieldname(String generalfieldname) {
		this.generalfieldname = generalfieldname;
	}

	@XmlElement
	public AppMaster getAppmaster() {
		return appmaster;
	}

	public void setAppmaster(AppMaster appmaster) {
		this.appmaster = appmaster;
	}

	@XmlElement
	public String getFieldid() {
		return fieldid;
	}

	public void setFieldid(String fieldid) {
		this.fieldid = fieldid;
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

	/**
	 * This constructor is mandatory for a pojo class to perform deep copy of
	 * object
	 * @param generalField [GeneralField]
	 */
	public GeneralField(GeneralField generalField) {
		this.generalfieldkey = generalField.generalfieldkey;
		this.generalfieldname = generalField.generalfieldname;
		this.appmaster = generalField.appmaster;
		this.fieldid = generalField.fieldid;
		this.site = generalField.site;
		this.status = generalField.status;
		this.createdby = generalField.createdby;
		this.createddate = generalField.createddate;
	}

	/**
	 * Creation of parameterized constructor makes this
	 * default constructor also mandatory for a pojo
	 */
	public GeneralField() {
	}
	
	/**
	 * To find difference between two entity objects by implementing Diffable interface  
	 */
	@Override
	public DiffResult diff(GeneralField obj) {
		
	     return new DiffBuilder(this, obj, ToStringStyle.SHORT_PREFIX_STYLE)
	       .append("generalfieldname", this.generalfieldname, obj.generalfieldname)
	       .append("fieldid", this.fieldid, obj.fieldid)
	       .append("appmaster", this.appmaster.getAppmastername(), obj.appmaster.getAppmastername())
	       .append("site", this.getSite().getSitename(), obj.getSite().getSitename())
	       .append("status",this.status, obj.status)
	       .append("createdby", this.createdby.getUsername(), obj.createdby.getUsername())
	       .append("createddate", this.createddate, obj.createddate)
	       .build();
	}
}
