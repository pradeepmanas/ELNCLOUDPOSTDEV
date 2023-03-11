package com.agaram.eln.primary.model.sheetManipulation;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.agaram.eln.primary.model.cfr.LScfttransaction;

@Entity
@Table(name = "LStestmaster")
public class LStestmaster {
	@Id
	@Column(name="testcode")
	private Integer ntestcode;
	
	@Column(name="testcategorycode")
	private Integer ntestcategorycode;
	
	@Column(name="accredited")
	private Integer naccredited;
	
	@Column(name="ncost")
	private float ncost;
	
	@Column(name="transactionstatus")
	private Integer ntransactionstatus;
	
	@Column(name="status")
	private Integer nstatus;
	
	@Column(name="masterauditcode")
	private Integer nmasterauditcode;
	
	@Column(name="sitecode")
	private Integer nsitecode;
	
	//columnDefinition = "nvarchar(255)",
	@Column(name="testsynonym")
	private String stestsynonym;
	//columnDefinition = "nvarchar(100)",
	@Column(name="testname")
	private String stestname;
	//columnDefinition = "nvarchar(255)",
	@Column(name="description")
	private String sdescription;
	//columnDefinition = "datetime",
	@Column(name="modifieddate")
	private String dmodifieddate;
	
	@Column(name="checklistversioncode")
	private Integer nchecklistversioncode;
	
	@Transient
	LScfttransaction objsilentaudit;
	
	@OneToMany
	@JoinColumn(name="testcode")
	List<LStestparameter> lstestparameter;
	
	
	public LScfttransaction getObjsilentaudit() {
		return objsilentaudit;
	}

	public void setObjsilentaudit(LScfttransaction objsilentaudit) {
		this.objsilentaudit = objsilentaudit;
	}

	public Integer getNtestcode() {
		return ntestcode;
	}

	public void setNtestcode(Integer ntestcode) {
		this.ntestcode = ntestcode;
	}

	public Integer getNtestcategorycode() {
		return ntestcategorycode;
	}

	public void setNtestcategorycode(Integer ntestcategorycode) {
		this.ntestcategorycode = ntestcategorycode;
	}

	public Integer getNaccredited() {
		return naccredited;
	}

	public void setNaccredited(Integer naccredited) {
		this.naccredited = naccredited;
	}

	public float getNcost() {
		return ncost;
	}

	public void setNcost(float ncost) {
		this.ncost = ncost;
	}

	public Integer getNtransactionstatus() {
		return ntransactionstatus;
	}

	public void setNtransactionstatus(Integer ntransactionstatus) {
		this.ntransactionstatus = ntransactionstatus;
	}

	public Integer getNstatus() {
		return nstatus;
	}

	public void setNstatus(Integer nstatus) {
		this.nstatus = nstatus;
	}

	public Integer getNmasterauditcode() {
		return nmasterauditcode;
	}

	public void setNmasterauditcode(Integer nmasterauditcode) {
		this.nmasterauditcode = nmasterauditcode;
	}

	public Integer getNsitecode() {
		return nsitecode;
	}

	public void setNsitecode(Integer nsitecode) {
		this.nsitecode = nsitecode;
	}

	public String getStestsynonym() {
		return stestsynonym;
	}

	public void setStestsynonym(String stestsynonym) {
		this.stestsynonym = stestsynonym;
	}

	public String getStestname() {
		return stestname;
	}

	public void setStestname(String stestname) {
		this.stestname = stestname;
	}

	public String getSdescription() {
		return sdescription;
	}

	public void setSdescription(String sdescription) {
		this.sdescription = sdescription;
	}

	public String getDmodifieddate() {
		return dmodifieddate;
	}

	public void setDmodifieddate(String dmodifieddate) {
		this.dmodifieddate = dmodifieddate;
	}

	public Integer getNchecklistversioncode() {
		return nchecklistversioncode;
	}

	public void setNchecklistversioncode(Integer nchecklistversioncode) {
		this.nchecklistversioncode = nchecklistversioncode;
	}

	public List<LStestparameter> getLstestparameter() {
		return lstestparameter;
	}

	public void setLstestparameter(List<LStestparameter> lstestparameter) {
		this.lstestparameter = lstestparameter;
	}
	
	@Transient
	private String stestcategoryname;
	
	@Transient
	private String sshortname;
	
	@Transient
	private String stestplatform;
	
	@Transient
	private String schecklistname;
	
	@Transient
	private String stransactionstatus;
	
	@Transient
	private String saccredited;
	
	@Transient
	private String sparametername;
	
	@Transient
	private String statperiodname;
	
	@Transient
	private String sdeltaunitname;
	
	@Transient
	private String sinterfacetypename;
	
	@Transient
	private String strainingneed;
	
	@Transient
	private String smodifieddate;
	
	@Transient
	private Integer ntat;
	
	@Transient
	private Integer ntatperiodcode;
	
	@Transient
	private Integer ntrainingneed;
	
	@Transient
	private Integer ninterfacetypecode;
	
	@Transient
	private Integer ncomponentcode;
	
	@Transient
	private Integer nproductcatcode;


	public String getStestcategoryname() {
		return stestcategoryname;
	}

	public void setStestcategoryname(String stestcategoryname) {
		this.stestcategoryname = stestcategoryname;
	}

	public String getSshortname() {
		return sshortname;
	}

	public void setSshortname(String sshortname) {
		this.sshortname = sshortname;
	}

	public String getStestplatform() {
		return stestplatform;
	}

	public void setStestplatform(String stestplatform) {
		this.stestplatform = stestplatform;
	}

	public String getSchecklistname() {
		return schecklistname;
	}

	public void setSchecklistname(String schecklistname) {
		this.schecklistname = schecklistname;
	}

	public String getStransactionstatus() {
		return stransactionstatus;
	}

	public void setStransactionstatus(String stransactionstatus) {
		this.stransactionstatus = stransactionstatus;
	}

	public String getSaccredited() {
		return saccredited;
	}

	public void setSaccredited(String saccredited) {
		this.saccredited = saccredited;
	}

	public String getSparametername() {
		return sparametername;
	}

	public void setSparametername(String sparametername) {
		this.sparametername = sparametername;
	}

	public String getStatperiodname() {
		return statperiodname;
	}

	public void setStatperiodname(String statperiodname) {
		this.statperiodname = statperiodname;
	}

	public String getSdeltaunitname() {
		return sdeltaunitname;
	}

	public void setSdeltaunitname(String sdeltaunitname) {
		this.sdeltaunitname = sdeltaunitname;
	}

	public String getSinterfacetypename() {
		return sinterfacetypename;
	}

	public void setSinterfacetypename(String sinterfacetypename) {
		this.sinterfacetypename = sinterfacetypename;
	}

	public String getStrainingneed() {
		return strainingneed;
	}

	public void setStrainingneed(String strainingneed) {
		this.strainingneed = strainingneed;
	}

	public String getSmodifieddate() {
		return smodifieddate;
	}

	public void setSmodifieddate(String smodifieddate) {
		this.smodifieddate = smodifieddate;
	}

	public Integer getNtat() {
		return ntat;
	}

	public void setNtat(Integer ntat) {
		this.ntat = ntat;
	}

	public Integer getNtatperiodcode() {
		return ntatperiodcode;
	}

	public void setNtatperiodcode(Integer ntatperiodcode) {
		this.ntatperiodcode = ntatperiodcode;
	}

	public Integer getNtrainingneed() {
		return ntrainingneed;
	}

	public void setNtrainingneed(Integer ntrainingneed) {
		this.ntrainingneed = ntrainingneed;
	}

	public Integer getNinterfacetypecode() {
		return ninterfacetypecode;
	}

	public void setNinterfacetypecode(Integer ninterfacetypecode) {
		this.ninterfacetypecode = ninterfacetypecode;
	}

	public Integer getNcomponentcode() {
		return ncomponentcode;
	}

	public void setNcomponentcode(Integer ncomponentcode) {
		this.ncomponentcode = ncomponentcode;
	}

	public Integer getNproductcatcode() {
		return nproductcatcode;
	}

	public void setNproductcatcode(Integer nproductcatcode) {
		this.nproductcatcode = nproductcatcode;
	}
	
}
