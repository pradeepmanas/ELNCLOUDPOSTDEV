package com.agaram.eln.primary.model.instrumentDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.agaram.eln.primary.model.webParser.Lswebparsermethod;

@Entity(name = "LsMappedFields")
@Table(name = "LsMappedFields")
//@Table(name = "T23FIELDS")
public class LsMappedFields {
	
	@Id
	@Column(name = "sFieldKey",columnDefinition = "varchar(12)")
	private String sFieldKey;
	
	@Column(name = "sInstrumentID",columnDefinition = "varchar(50)", nullable=false)
	private String sInstrumentID;
	
	@Column(name = "sMethodName",columnDefinition = "varchar(50)", nullable=false)
	private String sMethodName;
	
	@Column(name = "sParserName",columnDefinition = "varchar(30)", nullable=false)
	private String sParserName;
	
	@Column(name = "sFieldName",columnDefinition = "varchar(30)", nullable=true)
	private String sFieldName;
	
	@Column(name = "sDataType",columnDefinition = "varchar(12)", nullable=true)
	private String sDataType;
	
	@Column(name = "sFormat",columnDefinition = "varchar(25)", nullable=true)
	private String sFormat;
	
	@Column(name = "sFieldType",columnDefinition = "varchar(30)", nullable=true)
	private String sFieldType;
	
	@Column(name = "sELNFieldName",columnDefinition = "varchar(30)", nullable=true)
	private String sELNFieldName;
	
	@Column(name = "sLIMSFieldName",columnDefinition = "varchar(30)", nullable=true)
	private String sLIMSFieldName;

	public String getsFieldKey() {
		return sFieldKey;
	}

	public void setsFieldKey(String sFieldKey) {
		this.sFieldKey = sFieldKey;
	}

	public String getsInstrumentID() {
		return sInstrumentID;
	}

	public void setsInstrumentID(String sInstrumentID) {
		this.sInstrumentID = sInstrumentID;
	}

	public String getsMethodName() {
		return sMethodName;
	}

	public void setsMethodName(String sMethodName) {
		this.sMethodName = sMethodName;
	}

	public String getsParserName() {
		return sParserName;
	}

	public void setsParserName(String sParserName) {
		this.sParserName = sParserName;
	}

	public String getsFieldName() {
		return sFieldName;
	}

	public void setsFieldName(String sFieldName) {
		this.sFieldName = sFieldName;
	}

	public String getsDataType() {
		return sDataType;
	}

	public void setsDataType(String sDataType) {
		this.sDataType = sDataType;
	}

	public String getsFormat() {
		return sFormat;
	}

	public void setsFormat(String sFormat) {
		this.sFormat = sFormat;
	}

	public String getsFieldType() {
		return sFieldType;
	}

	public void setsFieldType(String sFieldType) {
		this.sFieldType = sFieldType;
	}

	public String getsELNFieldName() {
		return sELNFieldName;
	}

	public void setsELNFieldName(String sELNFieldName) {
		this.sELNFieldName = sELNFieldName;
	}

	public String getsLIMSFieldName() {
		return sLIMSFieldName;
	}

	public void setsLIMSFieldName(String sLIMSFieldName) {
		this.sLIMSFieldName = sLIMSFieldName;
	}	
	
	
	public LsMappedFields(Integer parserfieldkey,String parserfieldname,String fieldid, String datatype,Lswebparsermethod method)
	{
		
		if(parserfieldkey != null) {
			this.sFieldKey = parserfieldkey.toString();
		}
		if(parserfieldname != null) {
			this.sFieldName = parserfieldname;
		}
		if(method != null) {
			
			this.sInstrumentID = method.getInstmaster().getInstrumentcode();
			this.sMethodName = method.getMethodname();
		}
		
		if(datatype != null)
		{
			this.sDataType = datatype;
		}
	}
	
	public LsMappedFields()
	{
		
	}
}