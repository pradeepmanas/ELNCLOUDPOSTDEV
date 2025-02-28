package com.agaram.eln.primary.model.sample;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Type;

import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.material.MaterialAttachments;
import com.agaram.eln.primary.model.material.Unit;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "sample")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Sample implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "samplecode")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer samplecode;
	
	@ManyToOne
	private SampleCategory samplecategory;
	
	@ManyToOne
	private SampleType sampletype;
	
	@ManyToOne
	private Unit unit;
	
	@ManyToOne
	private LSuserMaster createby;
	
	@Column(name = "nstatus")
	private Integer nstatus;
	
	private Date createddate;
	
	private Integer nsitecode;
	private Long sitesequence;
	
	@Type(type = "jsonb")
	@Column(name = "jsondata", columnDefinition = "jsonb")
	private String jsondata;
	
	@Column(name = "samplename")
	private String samplename;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifieddate;
	
	@Column(name = "modifiedby")
	private String modifiedby;
	
	@Column(name = "applicationsequence")
	private Long applicationsequence;
	
	
	@Column(name = "sequenceid")
	private String sequenceid;
	
	@Transient
	private Long previousstatus;

	public Long getPreviousstatus() {
		return previousstatus;
	}

	public void setPreviousstatus(Long previousstatus) {
		this.previousstatus = previousstatus;
	}
	
	@OneToMany
	@JoinColumn(name="samplecode")
	private List<DerivedSamples> parentsamples;
	
	@OneToMany
	@JoinColumn(name="samplecode")
	private List<SampleAttachments> lssampleAttachments;
	
	private Integer derivedtype;
	private Integer expirytype;
	@Transient
	private String expiryTypeValue;
	private Boolean quarantine;
	private Boolean openexpiry;
	private String openexpiryvalue;
	private String openexpiryperiod;
	private Integer ntransactionstatus; 
	private Double nqtynotification;
	private Boolean openexpiryselect;
	
	@Column(name = "trackconsumption")
	private Integer trackconsumption;
	@Transient
	private String openexpiryneedvalue;
	@Transient
	private String quarantinevalue;
	
	@Column(name = "storagecondition")
	private String storagecondition;
	@Column(name = "quantity")
	private Integer quantity;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date expirydate;
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateofcollection;
	
	@ColumnDefault("1")
	@Column(name = "usageoption", nullable = false)
	private Integer usageoption;
	
	@OneToOne
	private SampleStorageMapping samplestoragemapping;

	@Transient
	private LScfttransaction objsilentaudit;
	
	public LScfttransaction getObjsilentaudit() {
		return objsilentaudit;
	}

	public void setObjsilentaudit(LScfttransaction objsilentaudit) {
		this.objsilentaudit = objsilentaudit;
	}

	@OneToMany(cascade = CascadeType.PERSIST)
	@JoinColumn(name="samplecode")
	@OrderBy("sampleprojectcode DESC")
	private List<SampleProjectHistory> sampleprojecthistory;
	
	@OneToMany
	@JoinColumn(name="samplecode")
	@OrderBy("sampleprojectcode DESC")
	private List<SampleProjectMap> sampleprojectmap;
	
	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public Integer getUsageoption() {
		return usageoption;
	}
	
	public void setUsageoption(Integer usageoption) {
		this.usageoption = usageoption;
	}
	
	public Date getExpirydate() {
		return expirydate;
	}

	public void setExpirydate(Date expirydate) {
		this.expirydate = expirydate;
	}
	public String getStoragecondition() {
		return storagecondition;
	}

	public void setStoragecondition(String storagecondition) {
		this.storagecondition = storagecondition;
	}
	
	public Date getDateofcollection() {
		return dateofcollection;
	}

	public void setDateofcollection(Date dateofcollection) {
		this.dateofcollection = dateofcollection;
	}

	public Integer getTrackconsumption() {
		return trackconsumption;
	}
	
	public void setTrackconsumption(Integer trackconsumption) {
		this.trackconsumption = trackconsumption;
	}

	public Boolean getOpenexpiry() {
		return openexpiry;
	}

	public void setOpenexpiry(Boolean openexpiry) {
		this.openexpiry = openexpiry;
	}

	public String getOpenexpiryvalue() {
		return openexpiryvalue;
	}

	public void setOpenexpiryvalue(String openexpiryvalue) {
		this.openexpiryvalue = openexpiryvalue;
	}

	public String getOpenexpiryperiod() {
		return openexpiryperiod;
	}

	public void setOpenexpiryperiod(String openexpiryperiod) {
		this.openexpiryperiod = openexpiryperiod;
	}

	public Boolean getQuarantine() {
		return quarantine;
	}

	public void setQuarantine(Boolean quarantine) {
		this.quarantine = quarantine;
	}
	public String getQuarantinevalue() {
		return quarantinevalue;
	}

	public void setQuarantinevalue(String quarantinevalue) {
		this.quarantinevalue = quarantinevalue;
	}
	
	public String getExpiryTypeValue() {
		return this.expirytype != null && this.expirytype == 1 ? "Expiry Date" : (this.expirytype != null && this.expirytype == 0 ? "No Expiry" : "Open Expiry");
	}

	public void setExpiryTypeValue(String expiryTypeValue) {
		this.expiryTypeValue = expiryTypeValue;
	}
	
	public Integer getExpirytype() {
		return expirytype;
	}

	public void setExpirytype(Integer expirytype) {
		this.expirytype = expirytype;
	}
	
	public List<SampleAttachments> getlsSampleAttachments() {
		return lssampleAttachments;
	}

	public void setlsSampleAttachments(List<SampleAttachments> lssampleAttachments) {
		this.lssampleAttachments = lssampleAttachments;
	}
	
	@Column(columnDefinition = "TEXT")
	private String assignedproject;
	
	public String getAssignedproject() {
		return assignedproject;
	}

	public void setAssignedproject(String assignedproject) {
		this.assignedproject = assignedproject;
	}

	public Integer getSamplecode() {
		return samplecode;
	}

	public void setSamplecode(Integer samplecode) {
		this.samplecode = samplecode;
	}

	public SampleCategory getSamplecategory() {
		return samplecategory;
	}

	public void setSamplecategory(SampleCategory samplecategory) {
		this.samplecategory = samplecategory;
	}

	public SampleType getSampletype() {
		return sampletype;
	}

	public void setSampletype(SampleType sampletype) {
		this.sampletype = sampletype;
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	public LSuserMaster getCreateby() {
		return createby;
	}

	public void setCreateby(LSuserMaster createby) {
		this.createby = createby;
	}

	public Integer getNstatus() {
		return nstatus;
	}

	public void setNstatus(Integer nstatus) {
		this.nstatus = nstatus;
	}

	public Date getCreateddate() {
		return createddate;
	}

	public void setCreateddate(Date createddate) {
		this.createddate = createddate;
	}

	public Integer getNsitecode() {
		return nsitecode;
	}

	public void setNsitecode(Integer nsitecode) {
		this.nsitecode = nsitecode;
	}

	public String getJsondata() {
		return jsondata;
	}

	public void setJsondata(String jsondata) {
		this.jsondata = jsondata;
	}

	public String getSamplename() {
		return samplename;
	}

	public void setSamplename(String samplename) {
		this.samplename = samplename;
	}

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

	public Long getApplicationsequence() {
		return applicationsequence;
	}

	public void setApplicationsequence(Long applicationsequence) {
		this.applicationsequence = applicationsequence;
	}

	public String getSequenceid() {
		return sequenceid;
	}

	public void setSequenceid(String sequenceid) {
		this.sequenceid = sequenceid;
	}
	public Long getSitesequence() {
		return sitesequence;
	}
 
	public void setSitesequence(Long sitesequence) {
		this.sitesequence = sitesequence;
	}
	public List<DerivedSamples> getParentsamples() {
		return parentsamples;
	}

	public void setParentsamples(List<DerivedSamples> parentsamples) {
		this.parentsamples = parentsamples;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Integer getDerivedtype() {
		return derivedtype;
	}

	public void setDerivedtype(Integer derivedtype) {
		this.derivedtype = derivedtype;
	}

	public SampleStorageMapping getSamplestoragemapping() {
		return samplestoragemapping;
	}

	public void setSamplestoragemapping(SampleStorageMapping samplestoragemapping) {
		this.samplestoragemapping = samplestoragemapping;
	}

	public List<SampleProjectHistory> getSampleprojecthistory() {
		return sampleprojecthistory;
	}

	public void setSampleprojecthistory(List<SampleProjectHistory> sampleprojecthistory) {
		this.sampleprojecthistory = sampleprojecthistory;
	}

	public Integer getNtransactionstatus() {
		return ntransactionstatus;
	}

	public void setNtransactionstatus(Integer ntransactionstatus) {
		this.ntransactionstatus = ntransactionstatus;
	}

	public Double getNqtynotification() {
		return nqtynotification;
	}

	public void setNqtynotification(Double nqtynotification) {
		this.nqtynotification = nqtynotification;
	}

	public List<SampleProjectMap> getSampleprojectmap() {
		return sampleprojectmap;
	}

	public void setSampleprojectmap(List<SampleProjectMap> sampleprojectmap) {
		this.sampleprojectmap = sampleprojectmap;
	}

	public Boolean getOpenexpiryselect() {
		return openexpiryselect;
	}

	public void setOpenexpiryselect(Boolean openexpiryselect) {
		this.openexpiryselect = openexpiryselect;
	}

}

