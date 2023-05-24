package com.agaram.eln.primary.model.sheetManipulation;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "LStestparameter")
public class LStestparameter {
	
	@Id
	@Column(name="testparametercode")
	private Integer ntestparametercode;
	@Column(name="testcode",nullable=false)
 	private Integer ntestcode;
	@Column(name="parametertypecode")
	private Integer nparametertypecode;
	@Column(name="roundingdigits")
	private Integer nroundingdigits;
	@Column(name="status")
	private Integer nstatus ;
	@Column(name="masterauditcode")
	private Integer nmasterauditcode ;
	@Column(name="isadhocparameter")
	private Integer nisadhocparameter;
	@Column(name="isvisible")
	private Integer nisvisible;
	
	@Column(columnDefinition = "numeric(17,0)",name="unitcode")
	private Integer nunitcode;
	
	//columnDefinition = "nvarchar(120)",
	@Column(name="parametername")
	private String sparametername;
	//columnDefinition = "nvarchar(255)",
	@Column(name="parametersynonym")
	private String sparametersynonym;
	
	@Column(columnDefinition = "numeric(17,0)",name="sitecode")
	private Integer nsitecode;
	
	@Column(name="testname")
	private String stestname;
	
	public Integer getNisadhocparameter() {
		return nisadhocparameter;
	}
	public void setNisadhocparameter(Integer nisadhocparameter) {
		this.nisadhocparameter = nisadhocparameter;
	}
	public Integer getNisvisible() {
		return nisvisible;
	}
	public void setNisvisible(Integer nisvisible) {
		this.nisvisible = nisvisible;
	}
	public Integer getNtestparametercode() {
		return ntestparametercode;
	}
	public void setNtestparametercode(Integer ntestparametercode) {
		this.ntestparametercode = ntestparametercode;
	}
	public Integer getNtestcode() {
		return ntestcode;
	}
	public void setNtestcode(Integer ntestcode) {
		this.ntestcode = ntestcode;
	}
	public Integer getNparametertypecode() {
		return nparametertypecode;
	}
	public void setNparametertypecode(Integer nparametertypecode) {
		this.nparametertypecode = nparametertypecode;
	}
	public Integer getNroundingdigits() {
		return nroundingdigits;
	}
	public void setNroundingdigits(Integer nroundingdigits) {
		this.nroundingdigits = nroundingdigits;
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
	public Integer getNunitcode() {
		return nunitcode;
	}
	public void setNunitcode(Integer nunitcode) {
		this.nunitcode = nunitcode;
	}
	public String getSparametername() {
		return sparametername;
	}
	public void setSparametername(String sparametername) {
		this.sparametername = sparametername;
	}
	public String getSparametersynonym() {
		return sparametersynonym;
	}
	public void setSparametersynonym(String sparametersynonym) {
		this.sparametersynonym = sparametersynonym;
	}
	
	@Transient
	private String dmodifieddate;
	
	@Transient
	private String objPredefinedParameter;
	
	@Transient
	private String sdeltaunitname;
	
	@Transient
	private String sdisplaystatus;
	
	@Transient
	private String sformulacalculationcode;
	
	@Transient
	private String stestparametersynonym;
	
	@Transient
	private String stransdisplaystatus;
	
	@Transient
	private String sunitname;
	
	@Transient
	private Integer isformula;
	
	@Transient
	private Integer ndeltacheck;
	
	@Transient
	private Integer ndeltacheckframe;
	
	@Transient
	private Integer ndeltachecklimitcode;
	
	@Transient
	private Integer ndeltaunitcode;

	public Integer getNsitecode() {
		return nsitecode;
	}
	public void setNsitecode(Integer nsitecode) {
		this.nsitecode = nsitecode;
	}
	public String getStestname() {
		return stestname;
	}
	public void setStestname(String stestname) {
		this.stestname = stestname;
	}
	public String getDmodifieddate() {
		return dmodifieddate;
	}
	public void setDmodifieddate(String dmodifieddate) {
		this.dmodifieddate = dmodifieddate;
	}
	public String getObjPredefinedParameter() {
		return objPredefinedParameter;
	}
	public void setObjPredefinedParameter(String objPredefinedParameter) {
		this.objPredefinedParameter = objPredefinedParameter;
	}
	public String getSdeltaunitname() {
		return sdeltaunitname;
	}
	public void setSdeltaunitname(String sdeltaunitname) {
		this.sdeltaunitname = sdeltaunitname;
	}
	public String getSdisplaystatus() {
		return sdisplaystatus;
	}
	public void setSdisplaystatus(String sdisplaystatus) {
		this.sdisplaystatus = sdisplaystatus;
	}
	public String getSformulacalculationcode() {
		return sformulacalculationcode;
	}
	public void setSformulacalculationcode(String sformulacalculationcode) {
		this.sformulacalculationcode = sformulacalculationcode;
	}
	public String getStestparametersynonym() {
		return stestparametersynonym;
	}
	public void setStestparametersynonym(String stestparametersynonym) {
		this.stestparametersynonym = stestparametersynonym;
	}
	public String getStransdisplaystatus() {
		return stransdisplaystatus;
	}
	public void setStransdisplaystatus(String stransdisplaystatus) {
		this.stransdisplaystatus = stransdisplaystatus;
	}
	public String getSunitname() {
		return sunitname;
	}
	public void setSunitname(String sunitname) {
		this.sunitname = sunitname;
	}
	public Integer getIsformula() {
		return isformula;
	}
	public void setIsformula(Integer isformula) {
		this.isformula = isformula;
	}
	public Integer getNdeltacheck() {
		return ndeltacheck;
	}
	public void setNdeltacheck(Integer ndeltacheck) {
		this.ndeltacheck = ndeltacheck;
	}
	public Integer getNdeltacheckframe() {
		return ndeltacheckframe;
	}
	public void setNdeltacheckframe(Integer ndeltacheckframe) {
		this.ndeltacheckframe = ndeltacheckframe;
	}
	public Integer getNdeltachecklimitcode() {
		return ndeltachecklimitcode;
	}
	public void setNdeltachecklimitcode(Integer ndeltachecklimitcode) {
		this.ndeltachecklimitcode = ndeltachecklimitcode;
	}
	public Integer getNdeltaunitcode() {
		return ndeltaunitcode;
	}
	public void setNdeltaunitcode(Integer ndeltaunitcode) {
		this.ndeltaunitcode = ndeltaunitcode;
	}
	
}
