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
//import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.Range;

import com.agaram.eln.primary.model.methodsetup.InstantDateAdapter;
import com.agaram.eln.primary.model.methodsetup.ControlType;
import com.agaram.eln.primary.model.methodsetup.Method;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * This class is used to map the fields of 'customfield' table of the Database.
 * @author ATE153
 * @version 1.0.0
 * @since   01- Apr- 2020
 */
@JsonIgnoreProperties(value = {"hibernateLazyInitializer"})
@XmlRootElement  (name = "customfield")
@XmlType(propOrder = { "customfieldkey", "method", "customfieldname", "controltype", "fieldid",
		 "status", "createdby", "createddate"})
@Entity
@Table(name = "customfield")
public class CustomField implements Serializable, Diffable<CustomField>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id 
	@Column(name = "customfieldkey", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer customfieldkey;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "methodkey", nullable = false)
	private Method method;
	
	@Column(name = "customfieldname")
	private String customfieldname;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "controltypekey", nullable = false)
	private ControlType controltype;
	
	@Column(name = "fieldid")
	private String fieldid;	
	
	@Range(min=-1, max=1)
	@Column(name = "status")
	private int status=1;	

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "usercode", nullable = false)
	private LSuserMaster createdby;
	
	@Column(name = "createddate")
	private Date createddate;

	@XmlAttribute
	public Integer getCustomfieldkey() {
		return customfieldkey;
	}

	public void setCustomfieldkey(Integer customfieldkey) {
		this.customfieldkey = customfieldkey;
	}	
	
	@XmlElement	
	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}
	
	@XmlElement	
	public String getCustomfieldname() {
		return customfieldname;
	}

	public void setCustomfieldname(String customfieldname) {
		this.customfieldname = customfieldname;
	}

	@XmlElement	
	public ControlType getControltype() {
		return controltype;
	}

	public void setControltype(ControlType controltype) {
		this.controltype = controltype;
	}

	@XmlElement
	public String getFieldid() {
		return fieldid;
	}

	public void setFieldid(String fieldid) {
		this.fieldid = fieldid;
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
	public CustomField() {
	}

	/**
	 * This constructor is mandatory for a pojo class to perform deep copy of
	 * object
	 * @param customField [CustomField]
	 */
	public CustomField(CustomField customField) {
		this.customfieldkey = customField.customfieldkey;
		this.method = customField.method;
		this.customfieldname = customField.customfieldname;
		this.controltype = customField.controltype;
		this.fieldid = customField.fieldid;
		this.status = customField.status;
		this.createdby = customField.createdby;
		this.createddate = customField.createddate;
	}

	/**
	 * To find difference between two entity objects by implementing Diffable interface  
	 */
	@Override
	public DiffResult diff(CustomField obj) {
		
	     return new DiffBuilder(this, obj, ToStringStyle.SHORT_PREFIX_STYLE)
	       .append("method", this.method.getMethodname(), obj.method.getMethodname())
	       .append("customfieldname", this.customfieldname, obj.customfieldname)
	       .append("fieldid", this.fieldid, obj.fieldid)
	       .append("controltype", this.controltype.getControltypename(), obj.controltype.getControltypename())
	       .append("status",this.status, obj.status)
	       .append("createdby", this.createdby.getUsername(), obj.createdby.getUsername())
	       .append("createddate", this.createddate, obj.createddate)
	       .build();
	}
}
