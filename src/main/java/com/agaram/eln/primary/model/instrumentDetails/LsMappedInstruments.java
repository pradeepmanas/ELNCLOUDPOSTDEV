package com.agaram.eln.primary.model.instrumentDetails;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.agaram.eln.primary.model.webParser.Lswebparsermethod;

@Entity(name = "LsMappedInstruments")
@Table(name = "LsMappedInstruments")
//@Table(name = "T06INSTRUMENTS")
public class LsMappedInstruments {

	@Id
	@Column(name = "sInstrumentID",columnDefinition = "varchar(50)")
	private String sInstrumentID;

	@Column(name = "sInstrumentName",columnDefinition = "varchar(50)", nullable=true)
	private String sInstrumentName;
	
	@Column(name = "sInstrumentAliasName",columnDefinition = "varchar(50)", nullable=true)
	private String sInstrumentAliasName;

	@Column(name = "sInstrumentMake",columnDefinition = "varchar(50)", nullable=true)
	private String sInstrumentMake;
	
	@Column(name = "sInstrumentModel",columnDefinition = "varchar(50)", nullable=true)
	private String sInstrumentModel;

	@Column(name = "sLockType",columnDefinition = "varchar(50)", nullable=true)
	private String sLockType;

	@Column(name="nParserType")
	private Integer nParserType;
	
	@Column(name="nInterfaceStatus")
	private Integer nInterfaceStatus;
	
	@Column(name="nInstrumentStatus")
	private Integer nInstrumentStatus;
	
	@Column(name="nCommunicationType")
	private Integer nCommunicationType;
	
	public String getsInstrumentID() {
		return sInstrumentID;
	}

	public void setsInstrumentID(String sInstrumentID) {
		this.sInstrumentID = sInstrumentID;
	}

	public String getsInstrumentName() {
		return sInstrumentName;
	}

	public void setsInstrumentName(String sInstrumentName) {
		this.sInstrumentName = sInstrumentName;
	}

	public String getsInstrumentAliasName() {
		return sInstrumentAliasName;
	}

	public void setsInstrumentAliasName(String sInstrumentAliasName) {
		this.sInstrumentAliasName = sInstrumentAliasName;
	}

	public String getsInstrumentMake() {
		return sInstrumentMake;
	}

	public void setsInstrumentMake(String sInstrumentMake) {
		this.sInstrumentMake = sInstrumentMake;
	}

	public String getsInstrumentModel() {
		return sInstrumentModel;
	}

	public void setsInstrumentModel(String sInstrumentModel) {
		this.sInstrumentModel = sInstrumentModel;
	}

	public String getsLockType() {
		return sLockType;
	}

	public void setsLockType(String sLockType) {
		this.sLockType = sLockType;
	}
	
	public Integer getnParserType() {
		return nParserType;
	}

	public void setnParserType(Integer nParserType) {
		this.nParserType = nParserType;
	}

	public Integer getnInterfaceStatus() {
		return nInterfaceStatus;
	}

	public void setnInterfaceStatus(Integer nInterfaceStatus) {
		this.nInterfaceStatus = nInterfaceStatus;
	}

	public Integer getnInstrumentStatus() {
		return nInstrumentStatus;
	}

	public void setnInstrumentStatus(Integer nInstrumentStatus) {
		this.nInstrumentStatus = nInstrumentStatus;
	}

	public Integer getnCommunicationType() {
		return nCommunicationType;
	}

	public void setnCommunicationType(Integer nCommunicationType) {
		this.nCommunicationType = nCommunicationType;
	}

	public LsMappedInstruments(Lswebparsermethod method)
	{
		
		if(method != null) {
			
			this.sInstrumentID = method.getInstmaster().getInstrumentcode();
			this.sInstrumentName = method.getInstmaster().getInstrumentname();
		}
		
	}
	
	public LsMappedInstruments()
	{
		
	}
}