package com.agaram.eln.primary.model.instrumentDetails;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;

import com.agaram.eln.primary.model.webParser.Lswebparsermethod;

@Entity(name = "LSinstruments")
@Table(name = "T06INSTRUMENTS")
public class LSinstruments {

	@Id
	//,columnDefinition = "varchar(50)"
	@Column(name = "T06_STRINSTID",columnDefinition = "varchar(50)")
	private String strinstid;
	//, columnDefinition = "SMALLINT"
	@Column(name = "T06_BYTESTATIONID", nullable = false)
	@Type(type = "org.hibernate.type.ShortType")
	private short bytestationid;
	//columnDefinition = "varchar(50)",
	@Column(name = "T06_STRINSTNAME", nullable=true,columnDefinition = "varchar(50)")
	private String strinstrumentname;
	//, columnDefinition = "SMALLINT"
	@Column(name = "T06_BYTESELECTED", nullable = true, columnDefinition = "SMALLINT")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private Boolean byteselected;
	//, columnDefinition = "SMALLINT"
	@Column(name = "T06_BYTEDATASOURCE", nullable = true, columnDefinition = "SMALLINT")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private Boolean bytedatasource;
	//, columnDefinition = "SMALLINT"
	@Column(name = "T06_BYTESID", nullable = true, columnDefinition = "SMALLINT")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private Boolean bytesid;
	//, columnDefinition = "SMALLINT"
	@Column(name = "T06_BYTE_CLIENTBOUND", nullable = true, columnDefinition = "SMALLINT")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private Boolean byteclientbound;
	//, columnDefinition = "SMALLINT"
	@Column(name = "T06_INT_USEDBY", nullable = false, columnDefinition = "SMALLINT")
	@Type(type = "org.hibernate.type.ShortType")
	private short intusedby;
	//, columnDefinition = "SMALLINT"
	@Column(name = "T06_BYTE_PEAKDETECTION", nullable = true, columnDefinition = "SMALLINT")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private Boolean bytepeakdetection;
	//columnDefinition = "varchar(15)",
	@Column(name = "T06_STRMAKE", nullable=true,columnDefinition = "varchar(15)")
	private String strmark;
	//columnDefinition = "varchar(15)",
	@Column(name = "T06_STRMODEL", nullable=true,columnDefinition = "varchar(15)")
	private String strmodel;
	//columnDefinition = "varchar(20)",
	@Column(name = "T06_STRIOPNUMBER", nullable=true,columnDefinition = "varchar(20)")
	private String striopnumber;
	//, columnDefinition = "SMALLINT"
	@Column(name = "T06_BYTECALIBER", nullable = true, columnDefinition = "SMALLINT")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private Boolean bytecaliber;
	//columnDefinition = "varchar(50)",
	@Column(name = "T06_STRCALIBTEST", nullable=true,columnDefinition = "varchar(50)")
	private String strcalibtest;
	//columnDefinition = "date",
	@Column(columnDefinition = "date",name = "T06_LASTCALIBON", nullable=true)
	@Temporal(TemporalType.DATE)
	private Date lastcalibon;
	//columnDefinition = "date",
	@Column(columnDefinition = "date",name = "T06_CALIBDUEDATE", nullable=true)
	@Temporal(TemporalType.DATE)
	private Date calibduedate;
	//columnDefinition = "varchar(10)",
	@Column(name = "T06_STRSCHED1", nullable=true,columnDefinition = "varchar(10)")
	private String strched1;
	//columnDefinition = "varchar(15)",
	@Column(name = "T06_STRSCHED2", nullable=true,columnDefinition = "varchar(15)")
	private String strched2;
	//columnDefinition = "varchar(6)",
	@Column(columnDefinition = "varchar(6)",name = "T06_CALIBTIME", nullable=true)
	private String calibtime;
	//columnDefinition = "varchar(50)",
	@Column(columnDefinition = "varchar(50)",name = "T06_CALIBLASTSETBY", nullable=true)
	private String caliblastsetby;
	//columnDefinition = "date",
	@Column(columnDefinition = "date",name = "T06_CALIBLASTMODIFIEDON", nullable=true)
	@Temporal(TemporalType.DATE)
	private Date caliblastmodifiedon;
	//columnDefinition = "varchar(50)",
	@Column(columnDefinition = "varchar(50)",name = "T06_CALIBSTATUS", nullable=true)
	private String calibstatus;
	//, columnDefinition = "SMALLINT"
	@Column(name = "T06_BYTEMULTISID", nullable = true, columnDefinition = "SMALLINT")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private Boolean bytemultisid;
	//, columnDefinition = "SMALLINT"
	@Column(name = "T06_BYTESPLITSID", nullable = true, columnDefinition = "SMALLINT")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private Boolean byteplitsid;
	//columnDefinition = "varchar(50)",
	@Column(columnDefinition = "varchar(50)",name = "T06_STRELECTRODENO", nullable=true)
	private String strlectrodeno;
	
	public String getStrinstid() {
		return strinstid;
	}
	public void setStrinstid(String strinstid) {
		this.strinstid = strinstid;
	}
	public short getBytestationid() {
		return bytestationid;
	}
	public void setBytestationid(short bytestationid) {
		this.bytestationid = bytestationid;
	}
	public String getStrinstrumentname() {
		return strinstrumentname;
	}
	public void setStrinstrumentname(String strinstrumentname) {
		this.strinstrumentname = strinstrumentname;
	}
	public Boolean getByteselected() {
		return byteselected;
	}
	public void setByteselected(Boolean byteselected) {
		this.byteselected = byteselected;
	}
	public Boolean getBytedatasource() {
		return bytedatasource;
	}
	public void setBytedatasource(Boolean bytedatasource) {
		this.bytedatasource = bytedatasource;
	}
	public Boolean getBytesid() {
		return bytesid;
	}
	public void setBytesid(Boolean bytesid) {
		this.bytesid = bytesid;
	}
	public Boolean getByteclientbound() {
		return byteclientbound;
	}
	public void setByteclientbound(Boolean byteclientbound) {
		this.byteclientbound = byteclientbound;
	}
	public short getIntusedby() {
		return intusedby;
	}
	public void setIntusedby(short intusedby) {
		this.intusedby = intusedby;
	}
	public Boolean getBytepeakdetection() {
		return bytepeakdetection;
	}
	public void setBytepeakdetection(Boolean bytepeakdetection) {
		this.bytepeakdetection = bytepeakdetection;
	}
	public String getStrmark() {
		return strmark;
	}
	public void setStrmark(String strmark) {
		this.strmark = strmark;
	}
	public String getStrmodel() {
		return strmodel;
	}
	public void setStrmodel(String strmodel) {
		this.strmodel = strmodel;
	}
	public String getStriopnumber() {
		return striopnumber;
	}
	public void setStriopnumber(String striopnumber) {
		this.striopnumber = striopnumber;
	}
	public Boolean getBytecaliber() {
		return bytecaliber;
	}
	public void setBytecaliber(Boolean bytecaliber) {
		this.bytecaliber = bytecaliber;
	}
	public String getStrcalibtest() {
		return strcalibtest;
	}
	public void setStrcalibtest(String strcalibtest) {
		this.strcalibtest = strcalibtest;
	}
	public Date getLastcalibon() {
		return lastcalibon;
	}
	public void setLastcalibon(Date lastcalibon) {
		this.lastcalibon = lastcalibon;
	}
	public Date getCalibduedate() {
		return calibduedate;
	}
	public void setCalibduedate(Date calibduedate) {
		this.calibduedate = calibduedate;
	}
	public String getStrched1() {
		return strched1;
	}
	public void setStrched1(String strched1) {
		this.strched1 = strched1;
	}
	public String getStrched2() {
		return strched2;
	}
	public void setStrched2(String strched2) {
		this.strched2 = strched2;
	}
	public String getCalibtime() {
		return calibtime;
	}
	public void setCalibtime(String calibtime) {
		this.calibtime = calibtime;
	}
	public String getCaliblastsetby() {
		return caliblastsetby;
	}
	public void setCaliblastsetby(String caliblastsetby) {
		this.caliblastsetby = caliblastsetby;
	}
	public Date getCaliblastmodifiedon() {
		return caliblastmodifiedon;
	}
	public void setCaliblastmodifiedon(Date caliblastmodifiedon) {
		this.caliblastmodifiedon = caliblastmodifiedon;
	}
	public String getCalibstatus() {
		return calibstatus;
	}
	public void setCalibstatus(String calibstatus) {
		this.calibstatus = calibstatus;
	}
	public Boolean getBytemultisid() {
		return bytemultisid;
	}
	public void setBytemultisid(Boolean bytemultisid) {
		this.bytemultisid = bytemultisid;
	}
	public Boolean getByteplitsid() {
		return byteplitsid;
	}
	public void setByteplitsid(Boolean byteplitsid) {
		this.byteplitsid = byteplitsid;
	}
	public String getStrlectrodeno() {
		return strlectrodeno;
	}
	public void setStrlectrodeno(String strlectrodeno) {
		this.strlectrodeno = strlectrodeno;
	}
	
	public LSinstruments(Lswebparsermethod method)
	{
		
		if(method != null) {
			
			this.strinstid = method.getInstmaster().getInstrumentcode();
			this.strinstrumentname = method.getInstmaster().getInstrumentname();
		}
		
	}
	
	public LSinstruments()
	{
		
	}
}
