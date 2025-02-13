package com.agaram.eln.primary.model.sample;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.instrumentDetails.LsOrderattachments;
import com.agaram.eln.primary.model.material.Elnmaterial;
import com.agaram.eln.primary.model.material.ElnresultUsedMaterial;
import com.agaram.eln.primary.model.material.Manufacturer;
import com.agaram.eln.primary.model.material.MaterialCategory;
import com.agaram.eln.primary.model.material.MaterialGrade;
import com.agaram.eln.primary.model.material.MaterialType;
import com.agaram.eln.primary.model.material.Section;
import com.agaram.eln.primary.model.material.Supplier;
import com.agaram.eln.primary.model.material.Unit;
import com.agaram.eln.primary.model.samplestoragelocation.SelectedInventoryMapped;
import com.agaram.eln.primary.model.sheetManipulation.LStestmasterlocal;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="elnresultusedsample")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ElnresultUsedSample {
	
	@Id
	@Column(name = "nelnresultusedsamplecode") 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer nelnresultusedsamplecode;

	@Column(name = "ordercode", nullable = false)
	private Long ordercode;

	@Column(name = "transactionscreen", nullable = false)//1 protocol template,2 sheet order,3 protocol order
	private Integer transactionscreen;
	
	@Column(name = "templatecode")
	private Integer templatecode;

	@Column(name = "nmaterialtypecode")
	private Integer nmaterialtypecode;

	@Column(name = "nmaterialcategorycode")
	private Integer nmaterialcategorycode;

	@Column(name = "nmaterialcode")
	private Integer nmaterialcode;

	@Column(name = "ninventorycode")
	private Integer ninventorycode;
	
	private Double nqtyissued;
	
	private Double nqtyused;

	private Double nqtyleft;
	
	@Column(name = "nstatus", nullable = false)
	private Integer nstatus = 1;
	
	private Integer isreturn;
	
	@Column(name = "jsondata")
	private String jsondata;
	
	private String batchid;
	
	private String samlename;
	
	private String samlesequenceid;
	
	@Transient
	private Map<String, Object> customobject;
	
	public Map<String, Object> getCustomobject() {
		return customobject;
	}

	public void setCustomobject(Map<String, Object> customobject) {
		this.customobject = customobject;
	}

	public String getSamlesequenceid() {
		return samlesequenceid;
	}

	public void setSamlesequenceid(String samlesequenceid) {
		this.samlesequenceid = samlesequenceid;
	}

	public String getSamlename() {
		return samlename;
	}

	public void setSamlename(String samlename) {
		this.samlename = samlename;
	}

	@ManyToOne
	private LSuserMaster createdbyusercode;
	
	@ManyToOne
	private LStestmasterlocal testcode;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date createddate;
	
	@Transient
	Response response;
	
	@Transient
	private String qtyleft;
	
	@Transient
	private List<Integer> inventorycode;
	
	@Transient
	private Date fromdate;

	@Transient
	private Date todate;

	public LSSiteMaster getSitemaster() {
		return sitemaster;
	}

	public void setSitemaster(LSSiteMaster sitemaster) {
		this.sitemaster = sitemaster;
	}

	@Transient
	LSSiteMaster sitemaster;
	
	private Integer samplecode;


	public Integer getSamplecode() {
		return samplecode;
	}

	public void setSamplecode(Integer samplecode) {
		this.samplecode = samplecode;
	}

	public Date getFromdate() {
		return fromdate;
	}

	public void setFromdate(Date fromdate) {
		this.fromdate = fromdate;
	}

	public Date getTodate() {
		return todate;
	}

	public void setTodate(Date todate) {
		this.todate = todate;
	}

	public List<Integer> getInventorycode() {
		return inventorycode;
	}

	public void setInventorycode(List<Integer> inventorycode) {
		this.inventorycode = inventorycode;
	}

	public Integer getIsreturn() {
		return isreturn;
	}

	public void setIsreturn(Integer isreturn) {
		this.isreturn = isreturn;
	}

	public String getQtyleft() {
		return qtyleft;
	}

	public void setQtyleft(String qtyleft) {
		this.qtyleft = qtyleft;
	}


	public Long getOrdercode() {
		return ordercode;
	}

	public void setOrdercode(Long ordercode) {
		this.ordercode = ordercode;
	}

	public Integer getTransactionscreen() {
		return transactionscreen;
	}

	public void setTransactionscreen(Integer transactionscreen) {
		this.transactionscreen = transactionscreen;
	}

	public Integer getTemplatecode() {
		return templatecode;
	}

	public void setTemplatecode(Integer templatecode) {
		this.templatecode = templatecode;
	}

	public Integer getNmaterialtypecode() {
		return nmaterialtypecode;
	}

	public void setNmaterialtypecode(Integer nmaterialtypecode) {
		this.nmaterialtypecode = nmaterialtypecode;
	}

	public Integer getNmaterialcategorycode() {
		return nmaterialcategorycode;
	}

	public void setNmaterialcategorycode(Integer nmaterialcategorycode) {
		this.nmaterialcategorycode = nmaterialcategorycode;
	}

	public Integer getNmaterialcode() {
		return nmaterialcode;
	}

	public void setNmaterialcode(Integer nmaterialcode) {
		this.nmaterialcode = nmaterialcode;
	}

	public Integer getNinventorycode() {
		return ninventorycode;
	}

	public void setNinventorycode(Integer ninventorycode) {
		this.ninventorycode = ninventorycode;
	}

	public Double getNqtyissued() {
		return nqtyissued;
	}

	public void setNqtyissued(Double nqtyissued) {
		this.nqtyissued = nqtyissued;
	}

	public Double getNqtyused() {
		return nqtyused;
	}

	public void setNqtyused(Double nqtyused) {
		this.nqtyused = nqtyused;
	}

	public Double getNqtyleft() {
		return nqtyleft;
	}

	public void setNqtyleft(Double nqtyleft) {
		this.nqtyleft = nqtyleft;
	}

	public Integer getNstatus() {
		return nstatus;
	}

	public void setNstatus(Integer nstatus) {
		this.nstatus = nstatus;
	}

	public String getJsondata() {
		return jsondata;
	}

	public void setJsondata(String jsondata) {
		this.jsondata = jsondata;
	}

	public String getBatchid() {
		return batchid;
	}

	public void setBatchid(String batchid) {
		this.batchid = batchid;
	}

	public LSuserMaster getCreatedbyusercode() {
		return createdbyusercode;
	}

	public void setCreatedbyusercode(LSuserMaster createdbyusercode) {
		this.createdbyusercode = createdbyusercode;
	}

	public LStestmasterlocal getTestcode() {
		return testcode;
	}

	public void setTestcode(LStestmasterlocal testcode) {
		this.testcode = testcode;
	}

	public Date getCreateddate() {
		return createddate;
	}

	public void setCreateddate(Date createddate) {
		this.createddate = createddate;
	}

	public Response getResponse() {
		return response;
	}

	public void setResponse(Response response) {
		this.response = response;
	}

	public Integer getNelnresultusedsamplecode() {
		return nelnresultusedsamplecode;
	}

	public void setNelnresultusedsamplecode(Integer nelnresultusedsamplecode) {
		this.nelnresultusedsamplecode = nelnresultusedsamplecode;
	}
}
