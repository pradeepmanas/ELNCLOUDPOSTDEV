package com.agaram.eln.primary.model.material;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.instrumentDetails.LsOrderattachments;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "elnmaterial")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Elnmaterial implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "nmaterialcode")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer nmaterialcode;
	
	@ManyToOne
	private MaterialCategory materialcategory;
	
	@ManyToOne
	private MaterialType materialtype;
	
	@ManyToOne
	private Unit unit;
	
	@ManyToOne
	private Section section;
	
	@ManyToOne
	private LSuserMaster createby;
	
	@Column(name = "nstatus")
	private Integer nstatus;
	
	private String sprefix;
	
	private Date createddate;

	@Transient
	private LScfttransaction objsilentaudit;
	
	@Transient
	public String info;
	
	@Transient
	private LScfttransaction objmanualaudit;
	
	private String remarks;
	
	private Integer nsitecode;
	private Integer expirytype;
	
	private String expirypolicyvalue;
	private String expirypolicyperiod;
	
	private Boolean quarantine;
//	private Boolean barcode;
	private Boolean openexpiry;
	private Boolean reusable;
	private String openexpiryvalue;
	private String openexpiryperiod;
	
	private Integer barcodetype;
	
	@Transient
	private Response response;
	
	@Type(type = "jsonb")
	@Column(name = "jsondata", columnDefinition = "jsonb")
	private String jsondata;	
	
	@Column(name = "smaterialname")
	private String smaterialname;
	
	@OneToMany
	@JoinColumn(name = "nmaterialcode")
	private List<LsOrderattachments> lsOrderattachments;
	
	@OneToMany
	@JoinColumn(name="nmaterialcode")
	private List<MaterialAttachments> lsmaterialAttachments;
	
	@OneToMany
	@JoinColumn(name="nmaterialcode")
	private List<ElnmaterialChemDiagRef> elnmaterialchemdiagref;
	
	@Transient
	private String expiryTypeValue;
	
	@Transient
	private String openexpiryneedvalue;
	@Transient
	private String quarantinevalue;
	@Transient
	private String displaystatus;
	
	private Integer samplecode;
	
	@Column(columnDefinition = "TEXT")
	private String assignedtasks;
	
	@Column(columnDefinition = "TEXT")
	private String assignedproject;
	
	public String getAssignedproject() {
		return assignedproject;
	}

	public void setAssignedproject(String assignedproject) {
		this.assignedproject = assignedproject;
	}
	
	public String getAssignedtasks() {
		return assignedtasks;
	}

	public void setAssignedtasks(String assignedtasks) {
		this.assignedtasks = assignedtasks;
	}
	
	public List<MaterialAttachments> getlsMaterialAttachments() {
		return lsmaterialAttachments;
	}

	public void setlsMaterialAttachments(List<MaterialAttachments> lsmaterialAttachments) {
		this.lsmaterialAttachments = lsmaterialAttachments;
	}
	
	public List<ElnmaterialChemDiagRef> getElnmaterialchemdiagref() {
		return elnmaterialchemdiagref;
	}

	public void setElnmaterialchemdiagref(List<ElnmaterialChemDiagRef> elnmaterialchemdiagref) {
		this.elnmaterialchemdiagref = elnmaterialchemdiagref;
	}

	public Integer getSamplecode() {
		return samplecode;
	}

	public void setSamplecode(Integer samplecode) {
		this.samplecode = samplecode;
	}

	public String getExpiryTypeValue() {
		return this.expirytype == 1 ? "Expiry Date" : (this.expirytype == 0 ? "No Expiry" : "Open Expiry");
	}

	public void setExpiryTypeValue(String expiryTypeValue) {
		this.expiryTypeValue = expiryTypeValue;
	}

	public Boolean getReusable() {
		return reusable;
	}

	public void setReusable(Boolean reusable) {
		this.reusable = reusable;
	}

	public String getDisplaystatus() {
		return displaystatus;
	}

	public void setDisplaystatus(String displaystatus) {
		this.displaystatus = displaystatus;
	}

	public String getOpenexpiryneedvalue() {
		return openexpiryneedvalue;
	}

	public void setOpenexpiryneedvalue(String openexpiryneedvalue) {
		this.openexpiryneedvalue = openexpiryneedvalue;
	}

	public String getQuarantinevalue() {
		return quarantinevalue;
	}

	public void setQuarantinevalue(String quarantinevalue) {
		this.quarantinevalue = quarantinevalue;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Response getResponse() {
		return response;
	}

	public void setResponse(Response response) {
		this.response = response;
	}

	public Boolean getQuarantine() {
		return quarantine;
	}

	public void setQuarantine(Boolean quarantine) {
		this.quarantine = quarantine;
	}

	public Integer getNmaterialcode() {
		return nmaterialcode;
	}

	public void setNmaterialcode(Integer nmaterialcode) {
		this.nmaterialcode = nmaterialcode;
	}

	public Integer getNstatus() {
		return nstatus;
	}

	public void setNstatus(Integer nstatus) {
		this.nstatus = nstatus;
	}

	public String getSprefix() {
		return sprefix;
	}

	public void setSprefix(String sprefix) {
		this.sprefix = sprefix;
	}

	public Date getCreateddate() {
		return createddate;
	}

	public void setCreateddate(Date createddate) {
		this.createddate = createddate;
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

	public Integer getNsitecode() {
		return nsitecode;
	}

	public void setNsitecode(Integer nsitecode) {
		this.nsitecode = nsitecode;
	}

	public Integer getExpirytype() {
		return expirytype;
	}

	public void setExpirytype(Integer expirytype) {
		this.expirytype = expirytype;
	}

	public String getExpirypolicyvalue() {
		return expirypolicyvalue;
	}

	public void setExpirypolicyvalue(String expirypolicyvalue) {
		this.expirypolicyvalue = expirypolicyvalue;
	}

	public String getExpirypolicyperiod() {
		return expirypolicyperiod;
	}

	public void setExpirypolicyperiod(String expirypolicyperiod) {
		this.expirypolicyperiod = expirypolicyperiod;
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

	public String getJsondata() {
		return jsondata;
	}

	public void setJsondata(String jsondata) {
		this.jsondata = jsondata;
	}

	public String getSmaterialname() {
		return smaterialname;
	}

	public void setSmaterialname(String smaterialname) {
		this.smaterialname = smaterialname;
	}

	public List<LsOrderattachments> getLsOrderattachments() {
		return lsOrderattachments;
	}

	public void setLsOrderattachments(List<LsOrderattachments> lsOrderattachments) {
		this.lsOrderattachments = lsOrderattachments;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public MaterialCategory getMaterialcategory() {
		return materialcategory;
	}

	public void setMaterialcategory(MaterialCategory materialcategory) {
		this.materialcategory = materialcategory;
	}

	public MaterialType getMaterialtype() {
		return materialtype;
	}

	public void setMaterialtype(MaterialType materialtype) {
		this.materialtype = materialtype;
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	public Section getSection() {
		return section;
	}

	public void setSection(Section section) {
		this.section = section;
	}

	public LSuserMaster getCreateby() {
		return createby;
	}

	public void setCreateby(LSuserMaster createby) {
		this.createby = createby;
	}

//	public Boolean getBarcode() {
//		return barcode;
//	}
//
//	public void setBarcode(Boolean barcode) {
//		this.barcode = barcode;
//	}

	public Integer getBarcodetype() {
		return barcodetype;
	}

	public void setBarcodetype(Integer barcodetype) {
		this.barcodetype = barcodetype;
	}
}