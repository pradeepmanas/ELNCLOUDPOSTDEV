package com.agaram.eln.primary.model.material;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.ColumnDefault;

import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;

@Entity
@Table(name = "manufacturer")
public class Manufacturer {
	
	@Id
	@Column(name="nmanufcode")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer nmanufcode;
	@Column(name ="smanufname",length =100, nullable=false)private String smanufname;
	@Column(name ="sdescription",length = 255)private String sdescription;
	@ColumnDefault("1")
	@Column(name="ntransactionstatus", nullable=false) private Integer ntransactionstatus = 1;
	@ColumnDefault("-1")
	@Column(name="nsitecode", nullable=false) private Integer nsitecode = -1;
	@ColumnDefault("1")
	@Column(name="nstatus", nullable=false) private Integer nstatus = 1;
	
	@ManyToOne
	private LSuserMaster createby;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdate;
	
	private transient String sDate;

	@Temporal(TemporalType.TIMESTAMP)
	private Date modifieddate;

	@Column(name = "modifiedby")
	private String modifiedby;
	
	public String getsDate() {
		return sDate;
	}
	public void setsDate(String sDate) {
		this.sDate = sDate;
	}
	
	public LSuserMaster getCreateby() {
		return createby;
	}
	public void setCreateby(LSuserMaster createby) {
		this.createby = createby;
	}
	public Date getCreatedate() {
		return createdate;
	}
	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}
	
	transient private  String smanufsitename;
	transient private Integer nmanufsitecode;
	transient private String sofficialmanufname;
	transient private String stransdisplaystatus;
	transient private String sproductgroupname;
	transient private Integer nproductmanufcode;
	transient private String seprotocolname;
	
	@Transient
	public String info;
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	
	@Transient
	private Response response;
	public Response getResponse() {
		return response;
	}
	public void setResponse(Response response) {
		this.response = response;
	}
	
	@Transient
	private LScfttransaction objsilentaudit;
	
	@Transient
	private LScfttransaction objmanualaudit;
	
	public Date getModifieddate() {
		return modifieddate;
	}

	public void setModifieddate(Date modifieddate) {
		this.modifieddate = modifieddate;
	}

	public String getModifiedby() {
		return modifiedby;
	}

	public void setModifiedby(String modifiedby) {
		this.modifiedby = modifiedby;
	}
	public LScfttransaction getObjmanualaudit() {
		return objmanualaudit;
	}
	public void setObjmanualaudit(LScfttransaction objmanualaudit) {
		this.objmanualaudit = objmanualaudit;
	}
	public LScfttransaction getObjsilentaudit() {
		return objsilentaudit;
	}
	public void setObjsilentaudit(LScfttransaction objsilentaudit) {
		this.objsilentaudit = objsilentaudit;
	}
	public Integer getNmanufcode() {
		return nmanufcode;
	}
	public void setNmanufcode(Integer nmanufcode) {
		this.nmanufcode = nmanufcode;
	}
	public String getSmanufname() {
		return smanufname;
	}
	public void setSmanufname(String smanufname) {
		this.smanufname = smanufname;
	}
	public String getSdescription() {
		return sdescription;
	}
	public void setSdescription(String sdescription) {
		this.sdescription = sdescription;
	}
	public Integer getNtransactionstatus() {
		return ntransactionstatus;
	}
	public void setNtransactionstatus(Integer ntransactionstatus) {
		this.ntransactionstatus = ntransactionstatus;
	}
	public Integer getNsitecode() {
		return nsitecode;
	}
	public void setNsitecode(Integer nsitecode) {
		this.nsitecode = nsitecode;
	}
	public Integer getNstatus() {
		return nstatus;
	}
	public void setNstatus(Integer nstatus) {
		this.nstatus = nstatus;
	}
	public String getSmanufsitename() {
		return smanufsitename;
	}
	public void setSmanufsitename(String smanufsitename) {
		this.smanufsitename = smanufsitename;
	}
	public Integer getNmanufsitecode() {
		return nmanufsitecode;
	}
	public void setNmanufsitecode(Integer nmanufsitecode) {
		this.nmanufsitecode = nmanufsitecode;
	}
	public String getSofficialmanufname() {
		return sofficialmanufname;
	}
	public void setSofficialmanufname(String sofficialmanufname) {
		this.sofficialmanufname = sofficialmanufname;
	}
	public String getStransdisplaystatus() {
		return stransdisplaystatus;
	}
	public void setStransdisplaystatus(String stransdisplaystatus) {
		this.stransdisplaystatus = stransdisplaystatus;
	}
	public String getSproductgroupname() {
		return sproductgroupname;
	}
	public void setSproductgroupname(String sproductgroupname) {
		this.sproductgroupname = sproductgroupname;
	}
	public Integer getNproductmanufcode() {
		return nproductmanufcode;
	}
	public void setNproductmanufcode(Integer nproductmanufcode) {
		this.nproductmanufcode = nproductmanufcode;
	}
	public String getSeprotocolname() {
		return seprotocolname;
	}
	public void setSeprotocolname(String seprotocolname) {
		this.seprotocolname = seprotocolname;
	}
}