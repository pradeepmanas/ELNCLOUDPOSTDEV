package com.agaram.eln.primary.model.instrumentDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "LSinstrumentmaster")
@Table(name = "L11InstrumentMaster")
public class LSinstrumentmaster {
	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	@Basic(optional = false)
	@Column(columnDefinition = "char(20)",name = "L11InstrumentID")
	private String instrumentid;
	
	@Column(columnDefinition = "varchar(200)",name = "L11InstrumentName")	
	private String instrumentname;
	@Column(columnDefinition = "varchar(200)",name = "L11InstrumentAliasName")	
	private String instrumentAliasname;
	@Column(columnDefinition = "varchar(200)",name = "L11InstrumentModel")	
	private String instrumentmodel;
	@Column(columnDefinition = "varchar(200)",name = "L11InstrumentMake")	
	private String instrumentmake;
	@Column(name = "L11InstrumentStatus")
	private Integer instrumentstatus;
	@Column(columnDefinition = "char(40)",name = "L11CreatedBy")	
	private String createdBy;
	@Column(columnDefinition = "date",name = "L11CreatedOn")	
	private String createdOn;
	@Column(columnDefinition = "char(40)",name = "L11ModifiedBy")	
	private String modifiedBy;
	@Column(columnDefinition = "date",name = "L11ModifiedOn")	
	private String modifiedOn;
	@Column(name = "L11InterfaceStatus")
	private Integer interfacestatus;
	//columnDefinition = "char(20)",
	@Column(columnDefinition = "char(20)",name = "L11LLProStatus")	
	private String llprostatus;
	@Column(name = "L11ParserType")
	private Integer parserType;
	//columnDefinition = "char(40)",
	@Column(columnDefinition = "char(40)",name = "L11SiteCode")	
	private String sitecode;

	public String getInstrumentid() {
		return instrumentid;
	}
	public void setInstrumentid(String instrumentid) {
		this.instrumentid = instrumentid;
	}
	public String getInstrumentname() {
		return instrumentname;
	}
	public void setInstrumentname(String instrumentname) {
		this.instrumentname = instrumentname;
	}
	public String getInstrumentAliasname() {
		return instrumentAliasname;
	}
	public void setInstrumentAliasname(String instrumentAliasname) {
		this.instrumentAliasname = instrumentAliasname;
	}
	public String getInstrumentmodel() {
		return instrumentmodel;
	}
	public void setInstrumentmodel(String instrumentmodel) {
		this.instrumentmodel = instrumentmodel;
	}
	public String getInstrumentmake() {
		return instrumentmake;
	}
	public void setInstrumentmake(String instrumentmake) {
		this.instrumentmake = instrumentmake;
	}
	public Integer getInstrumentstatus() {
		return instrumentstatus;
	}
	public void setInstrumentstatus(Integer instrumentstatus) {
		this.instrumentstatus = instrumentstatus;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public String getModifiedOn() {
		return modifiedOn;
	}
	public void setModifiedOn(String modifiedOn) {
		this.modifiedOn = modifiedOn;
	}
	public Integer getInterfacestatus() {
		return interfacestatus;
	}
	public void setInterfacestatus(Integer interfacestatus) {
		this.interfacestatus = interfacestatus;
	}
	public String getLlprostatus() {
		return llprostatus;
	}
	public void setLlprostatus(String llprostatus) {
		this.llprostatus = llprostatus;
	}
	public Integer getParserType() {
		return parserType;
	}
	public void setParserType(Integer parserType) {
		this.parserType = parserType;
	}
	public String getSitecode() {
		return sitecode;
	}
	public void setSitecode(String sitecode) {
		this.sitecode = sitecode;
	}
}
