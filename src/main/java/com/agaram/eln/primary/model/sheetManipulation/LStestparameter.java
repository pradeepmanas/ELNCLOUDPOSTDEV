package com.agaram.eln.primary.model.sheetManipulation;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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
	
}
