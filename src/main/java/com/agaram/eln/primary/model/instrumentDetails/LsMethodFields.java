package com.agaram.eln.primary.model.instrumentDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.agaram.eln.primary.model.webParser.Lswebparsermethod;

@Entity(name = "LsMethodFields")
@Table(name = "T23FIELDS")
public class LsMethodFields {
	
	@Id
	//,columnDefinition = "varchar(12)"
	@Column(name = "T23_STRFIELDKEY",columnDefinition = "varchar(12)")
	private String fieldkey;
	//,columnDefinition = "varchar(50)"
	@Column(name = "T23_STRINSTRUMENTID", nullable=false,columnDefinition = "varchar(50)")
	private String instrumentid;
	//,columnDefinition = "varchar(50)"
	@Column(name = "T23_STRMETHODNAME", nullable=false,columnDefinition = "varchar(50)")
	private String methodname;
	//,columnDefinition = "varchar(30)"
	@Column(name = "T23_STRPARSERNAME", nullable=false,columnDefinition = "varchar(30)")
	private String parsername;
	//,columnDefinition = "varchar(30)"
	@Column(name = "T23_STRFIELDNAME", nullable=true,columnDefinition = "varchar(30)")
	private String fieldname;
	//,columnDefinition = "varchar(12)"
	@Column(name = "T23_STRDATATYPE", nullable=true,columnDefinition = "varchar(12)")
	private String datatype;
	//,columnDefinition = "varchar(25)"
	@Column(name = "T23_STRFORMAT", nullable=true,columnDefinition = "varchar(25)")
	private String format;
	//, columnDefinition = "SMALLINT"
	@Column(name = "T23_BYTEVALUE", nullable = true, columnDefinition = "SMALLINT")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private Boolean bytevalue;
	//, columnDefinition = "SMALLINT"
	@Column(name = "T23_BYTEDELETABLE", nullable = true, columnDefinition = "SMALLINT")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private Boolean bytedeleteable;
	//, columnDefinition = "SMALLINT"
	@Column(name = "T23_BYTESEQUENCE", nullable = true, columnDefinition = "SMALLINT")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private Boolean bytesequence;
	//, columnDefinition = "SMALLINT"
	@Column(name = "T23_BYTESELECTED", nullable = true, columnDefinition = "SMALLINT")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private Boolean byteselected;
	//, columnDefinition = "SMALLINT"
	@Column(name = "T23_BYTESELLOGINRPT", nullable = true, columnDefinition = "SMALLINT")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private Boolean bytesellloginrpt;
	//, columnDefinition = "SMALLINT"
	@Column(name = "T23_INTSORTSEQUENCE", nullable = true, columnDefinition = "SMALLINT")
	@Type(type = "org.hibernate.type.ShortType")
	private short sortsequence;
	//, columnDefinition = "SMALLINT"
	@Column(name = "T23_BYTEALIGN", nullable = true, columnDefinition = "SMALLINT")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private Boolean byteallign;
	//, columnDefinition = "SMALLINT"
	@Column(name = "T23_BYTEDIMENSION", nullable = true, columnDefinition = "SMALLINT")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private Boolean bytedimension;
	//,columnDefinition = "varchar(30)"
	@Column(name = "T23_STRLIMSFIELDNAME", nullable=true,columnDefinition = "varchar(30)")
	private String limsfieldname;
	//,columnDefinition = "varchar(30)"
	@Column(name = "T23_STRELNFIELDNAME", nullable=true,columnDefinition = "varchar(30)")
	private String elnfieldname;
	//, columnDefinition = "SMALLINT"
	@Column(name = "T23_BYTECOLTYPE", nullable = true, columnDefinition = "SMALLINT")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private Boolean bytecoltype;
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
	
	public LsMethodFields(Integer parserfieldkey,String parserfieldname,String fieldid,String datatype,Lswebparsermethod method)
	{
		
		if(parserfieldkey != null) {
			this.fieldkey = parserfieldkey.toString();
		}
		if(parserfieldname != null) {
			this.fieldname = parserfieldname;
		}
		if(method != null) {
			
			this.instrumentid = method.getInstmaster().getInstrumentcode();
			this.methodname = method.getMethodname();
		}
		
		if(datatype != null)
		{
			this.datatype = datatype;
		}
		
		
	}
	
	public LsMethodFields()
	{
		
	}
}
