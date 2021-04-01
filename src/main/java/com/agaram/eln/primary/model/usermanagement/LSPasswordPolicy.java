package com.agaram.eln.primary.model.usermanagement;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.general.Response;


@Entity
@Table(name = "LSpasswordpolicy")
//@IdClass(PolicyId.class)
public class LSPasswordPolicy {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "policycode")
	private Integer policycode;
	
	private Integer minpasswrdlength;
	private Integer maxpasswrdlength;
	//@Column(name = "npasswordhistory")
	private Integer passwordhistory;
	//@Column(name = "npasswordexpiry")
	private Integer passwordexpiry;
	//@Column(name = "nmincapitalchar")
	private Integer mincapitalchar;
	//@Column(name = "nlockpolicy")
	private Integer lockpolicy;
	//@Column(name = "nminsmallchar")
	private Integer minsmallchar;
	//@Column(name = "nmnnumericchar")
	private Integer minnumericchar;
	//@Column(name = "nminspecialchar")
	private Integer minspecialchar;
	//@Column(name = "ncomplexpasswrd")
	private Integer complexpasswrd;
	//@Column(name = "ndbbased")
	private Integer dbbased;
	@Transient
	LScfttransaction objmanualaudit;
	
	public LScfttransaction getObjmanualaudit() {
		return objmanualaudit;
	}

	public void setObjmanualaudit(LScfttransaction objmanualaudit) {
		this.objmanualaudit = objmanualaudit;
	}
	@ManyToOne
	private LSSiteMaster lssitemaster; 
	
	public LSSiteMaster getLssitemaster() {
		return lssitemaster;
	}

	public void setLssitemaster(LSSiteMaster lssitemaster) {
		this.lssitemaster = lssitemaster;
	}

	@ManyToOne
	private LSuserMaster lsusermaster;
	
	@Transient
	LScfttransaction objsilentaudit;
	
	@Transient
	private Response response;
	
	
	@Transient
	Response objResponse;
	
	public LoggedUser getObjuser() {
		return objuser;
	}

	public void setObjuser(LoggedUser objuser) {
		this.objuser = objuser;
	}

	@Transient
	LoggedUser objuser;
	
	public Response getResponse() {
	    return response;
	}

	public void setResponse(Response response) {
		this.response = response;
	}
	public Response getObjResponse() {
		return objResponse;
	}

	public void setObjResponse(Response objResponse) {
		this.objResponse = objResponse;
	}


	public LScfttransaction getObjsilentaudit() {
		return objsilentaudit;
	}

	public void setObjsilentaudit(LScfttransaction objsilentaudit) {
		this.objsilentaudit = objsilentaudit;
	}

	public Integer getPolicycode() {
		return policycode;
	}

	public void setPolicycode(Integer policycode) {
		this.policycode = policycode;
	}

	public Integer getMinpasswrdlength() {
		return minpasswrdlength;
	}

	public void setMinpasswrdlength(Integer minpasswrdlength) {
		this.minpasswrdlength = minpasswrdlength;
	}

	public Integer getMaxpasswrdlength() {
		return maxpasswrdlength;
	}

	public void setMaxpasswrdlength(Integer maxpasswrdlength) {
		this.maxpasswrdlength = maxpasswrdlength;
	}

	public Integer getPasswordhistory() {
		return passwordhistory;
	}

	public void setPasswordhistory(Integer passwordhistory) {
		this.passwordhistory = passwordhistory;
	}

	public Integer getPasswordexpiry() {
		return passwordexpiry;
	}

	public void setPasswordexpiry(Integer passwordexpiry) {
		this.passwordexpiry = passwordexpiry;
	}

	public Integer getMincapitalchar() {
		return mincapitalchar;
	}

	public void setMincapitalchar(Integer mincapitalchar) {
		this.mincapitalchar = mincapitalchar;
	}

	public Integer getLockpolicy() {
		return lockpolicy;
	}

	public void setLockpolicy(Integer lockpolicy) {
		this.lockpolicy = lockpolicy;
	}

	public Integer getMinsmallchar() {
		return minsmallchar;
	}

	public void setMinsmallchar(Integer minsmallchar) {
		this.minsmallchar = minsmallchar;
	}

	public Integer getMinnumericchar() {
		return minnumericchar;
	}

	public void setMinnumericchar(Integer minnumericchar) {
		this.minnumericchar = minnumericchar;
	}

	public Integer getMinspecialchar() {
		return minspecialchar;
	}

	public void setMinspecialchar(Integer minspecialchar) {
		this.minspecialchar = minspecialchar;
	}

	public Integer getComplexpasswrd() {
		return complexpasswrd;
	}

	public void setComplexpasswrd(Integer complexpasswrd) {
		this.complexpasswrd = complexpasswrd;
	}

	public Integer getDbbased() {
		return dbbased;
	}

	public void setDbbased(Integer dbbased) {
		this.dbbased = dbbased;
	}

	public LSuserMaster getLsusermaster() {
		return lsusermaster;
	}

	public void setLsusermaster(LSuserMaster lsusermaster) {
		this.lsusermaster = lsusermaster;
	}

	
	
}
