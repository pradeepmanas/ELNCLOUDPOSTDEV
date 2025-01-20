package com.agaram.eln.primary.model.sample;

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
@Table(name = "samplecategory")
public class SampleCategory {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "nsamplecatcode")
	private Integer nsamplecatcode;

	@Column(name = "nsampletypecode", nullable = false)
	private Integer nsampletypecode = -1;

	@Column(name = "needSectionwise")
	private Integer needSectionwise;

	@Column(name = "nuserrolecode", nullable = false)
	private Integer nuserrolecode;

	@ColumnDefault("-1")
	@Column(name = "nbarcode", nullable = false)
	private Integer nbarcode = -1;

	@ColumnDefault("4")
	@Column(name = "ncategorybasedflow", nullable = false)
	private Integer ncategorybasedflow = 4;

	@ColumnDefault("1")
	@Column(name = "nactivestatus", nullable = false)
	private Integer nactivestatus;

	@Column(name = "ssamplecatname", length = 100, nullable = false)
	private String ssamplecatname;

	@Column(name = "sdescription", length = 255)
	private String sdescription;

	@ColumnDefault("4")
	@Column(name = "ndefaultstatus", nullable = false)
	private Integer ndefaultstatus = 4;

	@ColumnDefault("-1")
	@Column(name = "nsitecode", nullable = false)
	private Integer nsitecode = -1;

	@Transient
	private LScfttransaction objsilentaudit;
	
	@Transient
	public String info;
	
	@Transient
	private LScfttransaction objmanualaudit;
	
	@Transient
	private Response response;
	
	@ManyToOne
	private LSuserMaster createby;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdate;
	
	@ColumnDefault("1")
	@Column(name = "nstatus", nullable = false)
	private Integer nstatus = 1;
	
	private String ssampletypename;
	
	private transient String mDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifieddate;
	
	@Column(name = "modifiedby")
	private String modifiedby;
	
	public String getmDate() {
		return mDate;
	}
	public void setmDate(String mDate) {
		this.mDate = mDate;
	}
	public String getModifiedby() {
		return modifiedby;
	}
	public void setModifiedby(String modifiedby) {
		this.modifiedby = modifiedby;
	}
	public Date getModifieddate() {
		return modifieddate;
	}
	public void setModifieddate(Date modifieddate) {
		this.modifieddate = modifieddate;
	}

	public Integer getNsamplecatcode() {
		return nsamplecatcode;
	}

	public void setNsamplecatcode(Integer nsamplecatcode) {
		this.nsamplecatcode = nsamplecatcode;
	}

	public Integer getNsampletypecode() {
		return nsampletypecode;
	}

	public void setNsampletypecode(Integer nsampletypecode) {
		this.nsampletypecode = nsampletypecode;
	}

	public Integer getNeedSectionwise() {
		return needSectionwise;
	}

	public void setNeedSectionwise(Integer needSectionwise) {
		this.needSectionwise = needSectionwise;
	}

	public Integer getNuserrolecode() {
		return nuserrolecode;
	}

	public void setNuserrolecode(Integer nuserrolecode) {
		this.nuserrolecode = nuserrolecode;
	}

	public Integer getNbarcode() {
		return nbarcode;
	}

	public void setNbarcode(Integer nbarcode) {
		this.nbarcode = nbarcode;
	}

	public Integer getNcategorybasedflow() {
		return ncategorybasedflow;
	}

	public void setNcategorybasedflow(Integer ncategorybasedflow) {
		this.ncategorybasedflow = ncategorybasedflow;
	}

	public Integer getNactivestatus() {
		return nactivestatus;
	}

	public void setNactivestatus(Integer nactivestatus) {
		this.nactivestatus = nactivestatus;
	}

	public String getSsamplecatname() {
		return ssamplecatname;
	}

	public void setSsamplecatname(String ssamplecatname) {
		this.ssamplecatname = ssamplecatname;
	}

	public String getSdescription() {
		return sdescription;
	}

	public void setSdescription(String sdescription) {
		this.sdescription = sdescription;
	}

	public Integer getNdefaultstatus() {
		return ndefaultstatus;
	}

	public void setNdefaultstatus(Integer ndefaultstatus) {
		this.ndefaultstatus = ndefaultstatus;
	}

	public Integer getNsitecode() {
		return nsitecode;
	}

	public void setNsitecode(Integer nsitecode) {
		this.nsitecode = nsitecode;
	}

	public LScfttransaction getObjsilentaudit() {
		return objsilentaudit;
	}

	public void setObjsilentaudit(LScfttransaction objsilentaudit) {
		this.objsilentaudit = objsilentaudit;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public LScfttransaction getObjmanualaudit() {
		return objmanualaudit;
	}

	public void setObjmanualaudit(LScfttransaction objmanualaudit) {
		this.objmanualaudit = objmanualaudit;
	}

	public Response getResponse() {
		return response;
	}

	public void setResponse(Response response) {
		this.response = response;
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

	public Integer getNstatus() {
		return nstatus;
	}

	public void setNstatus(Integer nstatus) {
		this.nstatus = nstatus;
	}

	public String getSsampletypename() {
		return ssampletypename;
	}

	public void setSsampletypename(String ssampletypename) {
		this.ssampletypename = ssampletypename;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
