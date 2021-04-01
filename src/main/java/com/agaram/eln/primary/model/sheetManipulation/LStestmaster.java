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
	
}
