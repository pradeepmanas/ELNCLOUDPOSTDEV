package com.agaram.eln.primary.model.instrumentDetails;

import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Type;


@Entity
@Table(name = "Lselninstrumentfields")
public class Lselninstrumentfields {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "fieldcode")
	private Integer fieldcode;
//	@Column(name = "fieldkey",columnDefinition = "varchar(12)")
	@Column(name = "fieldkey")
	private String fieldkey;
//	@Column(name = "instrumentid",columnDefinition = "varchar(50)")
	@Column(name = "instrumentid")
	private String instrumentid;
//	@Column(name = "methodname",columnDefinition = "varchar(50)")
	@Column(name = "methodname")
	private String methodname;
//	@Column(name = "parsername",columnDefinition = "varchar(30)")
	@Column(name = "parsername")
	private String parsername;
//	@Column(name = "fieldname",columnDefinition = "varchar(30)")\
	@Column(name = "fieldname")
	private String fieldname;
//	@Column(name = "datatype",columnDefinition = "varchar(12)")
	@Column(name = "datatype")
	private String datatype;
//	@Column(name = "format",columnDefinition = "varchar(25)")
	@Column(name = "format")
	private String format;
	//@Column(name = "bytevalue", columnDefinition = "SMALLINT")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private Boolean bytevalue;
	//@Column(name = "bytedeleteable", columnDefinition = "SMALLINT")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private Boolean bytedeleteable;
	//@Column(name = "bytesequence", columnDefinition = "SMALLINT")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private Boolean bytesequence;
	//@Column(name = "byteselected", columnDefinition = "SMALLINT")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private Boolean byteselected;
	//@Column(name = "bytesellloginrpt", columnDefinition = "SMALLINT")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private Boolean bytesellloginrpt;
	@Column(name = "sortsequence", columnDefinition = "SMALLINT")
	@Type(type = "org.hibernate.type.ShortType")
	private short sortsequence;
	@Column(name = "byteallign", columnDefinition = "SMALLINT")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private Boolean byteallign;
	@Column(name = "bytedimension", columnDefinition = "SMALLINT")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private Boolean bytedimension;
	@Column(name = "limsfieldname",columnDefinition = "varchar(30)")
//	@Column(name = "limsfieldname")
	private String limsfieldname;
	@Column(name = "elnfieldname",columnDefinition = "varchar(30)")
//	@Column(name = "elnfieldname")
	private String elnfieldname;
	@Column(name = "bytecoltype", columnDefinition = "SMALLINT")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private Boolean bytecoltype;
	
	private Integer instrumentcode;
	
	@OneToMany
	@JoinColumn(name="fieldcode")
	private List<Lselninstfieldmapping> lselninstfieldmapping;
	
	public Integer getFieldcode() {
		return fieldcode;
	}
	public void setFieldcode(Integer fieldcode) {
		this.fieldcode = fieldcode;
	}
	public String getFieldkey() {
		return fieldkey;
	}
	public void setFieldkey(String fieldkey) {
		this.fieldkey = fieldkey;
	}
	public String getInstrumentid() {
		return instrumentid;
	}
	public void setInstrumentid(String instrumentid) {
		this.instrumentid = instrumentid;
	}
	public String getMethodname() {
		return methodname;
	}
	public void setMethodname(String methodname) {
		this.methodname = methodname;
	}
	public String getParsername() {
		return parsername;
	}
	public void setParsername(String parsername) {
		this.parsername = parsername;
	}
	public String getFieldname() {
		return fieldname;
	}
	public void setFieldname(String fieldname) {
		this.fieldname = fieldname;
	}
	public String getDatatype() {
		return datatype;
	}
	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public Boolean getBytevalue() {
		return bytevalue;
	}
	public void setBytevalue(Boolean bytevalue) {
		this.bytevalue = bytevalue;
	}
	public Boolean getBytedeleteable() {
		return bytedeleteable;
	}
	public void setBytedeleteable(Boolean bytedeleteable) {
		this.bytedeleteable = bytedeleteable;
	}
	public Boolean getBytesequence() {
		return bytesequence;
	}
	public void setBytesequence(Boolean bytesequence) {
		this.bytesequence = bytesequence;
	}
	public Boolean getByteselected() {
		return byteselected;
	}
	public void setByteselected(Boolean byteselected) {
		this.byteselected = byteselected;
	}
	public Boolean getBytesellloginrpt() {
		return bytesellloginrpt;
	}
	public void setBytesellloginrpt(Boolean bytesellloginrpt) {
		this.bytesellloginrpt = bytesellloginrpt;
	}
	public short getSortsequence() {
		return sortsequence;
	}
	public void setSortsequence(short sortsequence) {
		this.sortsequence = sortsequence;
	}
	public Boolean getByteallign() {
		return byteallign;
	}
	public void setByteallign(Boolean byteallign) {
		this.byteallign = byteallign;
	}
	public Boolean getBytedimension() {
		return bytedimension;
	}
	public void setBytedimension(Boolean bytedimension) {
		this.bytedimension = bytedimension;
	}
	public String getLimsfieldname() {
		return limsfieldname;
	}
	public void setLimsfieldname(String limsfieldname) {
		this.limsfieldname = limsfieldname;
	}
	public String getElnfieldname() {
		return elnfieldname;
	}
	public void setElnfieldname(String elnfieldname) {
		this.elnfieldname = elnfieldname;
	}
	public Boolean getBytecoltype() {
		return bytecoltype;
	}
	public void setBytecoltype(Boolean bytecoltype) {
		this.bytecoltype = bytecoltype;
	}
	public Integer getInstrumentcode() {
		return instrumentcode;
	}
	public void setInstrumentcode(Integer instrumentcode) {
		this.instrumentcode = instrumentcode;
	}
	public List<Lselninstfieldmapping> getLselninstfieldmapping() {
		return lselninstfieldmapping;
	}
	public void setLselninstfieldmapping(List<Lselninstfieldmapping> lselninstfieldmapping) {
		this.lselninstfieldmapping = lselninstfieldmapping;
	}
	
	
}
