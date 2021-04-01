package com.agaram.eln.primary.model.instrumentDetails;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = "LSfields")
public class LSfields {
	
	@Id
	private Integer fieldcode;
	
	private Integer fieldtypecode;
	private Integer fieldorderno;
	@Column(name = "level01code", columnDefinition = "varchar(50)")
//	@Column(name = "level01code")
	private String instrumentid;
	@Column(name = "level01name", columnDefinition = "varchar(120)")
//	@Column(name = "level01name")
	private String strinstrumentname;
	@Column(name = "level02code", columnDefinition = "varchar(120)")
//	@Column(name = "level02code")
	private String level02code;
	@Column(name = "level02name", columnDefinition = "varchar(120)")
//	@Column(name = "level02name")
	private String methodname;
	@Column(name = "level03code", columnDefinition = "varchar(120)")
//	@Column(name = "level03code")
	private Integer level03code;
	@Column(name = "level03name", columnDefinition = "varchar(120)")
//	@Column(name = "level03name")
	private String parsername;
	@Column(name = "level04code", columnDefinition = "varchar(120)")
//	@Column(name = "level04code")
	private String fieldkey;
	@Column(name = "level04name", columnDefinition = "varchar(120)")
//	@Column(name = "level04name")
	private String fieldname;
	@Column(name = "isactive", columnDefinition = "int")
	private Integer isactive;
	@Column(name = "siteID", columnDefinition = "int")
	private Integer siteID;
	@Column(name = "createby", columnDefinition = "int")
	private Integer createby;
	@Column(name = "createdate", columnDefinition = "date")
//	@Column(name = "createdate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdate;
	
	@Transient
	private String datatype;

	public Integer getFieldcode() {
		return fieldcode;
	}

	public void setFieldcode(Integer fieldcode) {
		this.fieldcode = fieldcode;
	}

	public Integer getFieldtypecode() {
		return fieldtypecode;
	}

	public void setFieldtypecode(Integer fieldtypecode) {
		this.fieldtypecode = fieldtypecode;
	}

	public Integer getFieldorderno() {
		return fieldorderno;
	}

	public void setFieldorderno(Integer fieldorderno) {
		this.fieldorderno = fieldorderno;
	}

	public String getInstrumentid() {
		return instrumentid;
	}

	public void setInstrumentid(String instrumentid) {
		this.instrumentid = instrumentid;
	}

	public String getStrinstrumentname() {
		return strinstrumentname;
	}

	public void setStrinstrumentname(String strinstrumentname) {
		this.strinstrumentname = strinstrumentname;
	}

	public String getLevel02code() {
		return level02code;
	}

	public void setLevel02code(String level02code) {
		this.level02code = level02code;
	}

	public String getMethodname() {
		return methodname;
	}

	public void setMethodname(String methodname) {
		this.methodname = methodname;
	}

	public Integer getLevel03code() {
		return level03code;
	}

	public void setLevel03code(Integer level03code) {
		this.level03code = level03code;
	}

	public String getParsername() {
		return parsername;
	}

	public void setParsername(String parsername) {
		this.parsername = parsername;
	}

	public String getFieldkey() {
		return fieldkey;
	}

	public void setFieldkey(String fieldkey) {
		this.fieldkey = fieldkey;
	}

	public String getFieldname() {
		return fieldname;
	}

	public void setFieldname(String fieldname) {
		this.fieldname = fieldname;
	}

	public Integer getIsactive() {
		return isactive;
	}

	public void setIsactive(Integer isactive) {
		this.isactive = isactive;
	}

	public Integer getSiteID() {
		return siteID;
	}

	public void setSiteID(Integer siteID) {
		this.siteID = siteID;
	}

	public Integer getCreateby() {
		return createby;
	}

	public void setCreateby(Integer createby) {
		this.createby = createby;
	}

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public String getDatatype() {
		return datatype;
	}

	public void setDatatype(String datatype) {
		this.datatype = datatype;
		this.datatype = "String";
	}
	
	
	
	

}
