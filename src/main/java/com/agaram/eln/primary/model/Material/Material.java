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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.instrumentDetails.LsOrderattachments;

@Entity
@Table(name = "material")
public class Material implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "nmaterialcode")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer nmaterialcode;
	
	private Integer nmaterialcatcode;
	private Integer ntransactionstatus;
	
	@Column(name = "nstatus")
	private Integer nstatus;
	
	@Column(name = "nmaterialtypecode")
	private Integer nmaterialtypecode;
	
	private String sprefix;
	
	private Date createddate;

	public Date getCreateddate() {
		return createddate;
	}
	public void setCreateddate(Date createddate) {
		this.createddate = createddate;
	}
	@Transient
	private LScfttransaction objsilentaudit;
	
	@Transient
	public String info;
	
	@Transient
	private LScfttransaction objmanualaudit;
	
	private Integer nsitecode;
	
	private Boolean expirypolicy;
	private String expirypolicyvalue;
	private String expirypolicyperiod;
	private Boolean openexpiry;
	private String openexpiryvalue;
	private String openexpiryperiod;
	private Boolean nextvalidation;
	private String nextvalidationvalue;
	private String nextvalidationperiod;

	public Boolean isExpirypolicy() {
		return expirypolicy;
	}

	public void setExpirypolicy(Boolean expirypolicy) {
		this.expirypolicy = expirypolicy;
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

	public Boolean isOpenexpiry() {
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

	public Boolean isNextvalidation() {
		return nextvalidation;
	}

	public void setNextvalidation(Boolean nextvalidation) {
		this.nextvalidation = nextvalidation;
	}

	public String getNextvalidationvalue() {
		return nextvalidationvalue;
	}

	public void setNextvalidationvalue(String nextvalidationvalue) {
		this.nextvalidationvalue = nextvalidationvalue;
	}

	public String getNextvalidationperiod() {
		return nextvalidationperiod;
	}

	public void setNextvalidationperiod(String nextvalidationperiod) {
		this.nextvalidationperiod = nextvalidationperiod;
	}
	
	public Integer getNsitecode() {
		return nsitecode;
	}
	public void setNsitecode(Integer nsitecode) {
		this.nsitecode = nsitecode;
	}
	public String getSprefix() {
		return sprefix;
	}
	public void setSprefix(String sprefix) {
		this.sprefix = sprefix;
	}
	@Type(type = "jsonb")
	@Column(name = "jsondata", columnDefinition = "jsonb")
	private String jsondata;
	
	@Type(type = "jsonb")
	@Column(name = "jsonuidata", columnDefinition = "jsonb")
	private String jsonuidata;
	
	@Column(name = "smaterialname")
	private String smaterialname;
	@OneToMany
	@JoinColumn(name = "nmaterialcode")
	private List<LsOrderattachments> lsOrderattachments;	
	
	
	public List<LsOrderattachments> getLsOrderattachments() {
		return lsOrderattachments;
	}
	public void setLsOrderattachments(List<LsOrderattachments> lsOrderattachments) {
		this.lsOrderattachments = lsOrderattachments;
	}
	private transient String sunitname;
	private transient short needsection;
	private transient int nproductcode;
	private transient String sproductname;
	
	public Integer getNmaterialtypecode() {
		return nmaterialtypecode;
	}
	public void setNmaterialtypecode(Integer nmaterialtypecode) {
		this.nmaterialtypecode = nmaterialtypecode;
	}
	public Integer getNtransactionstatus() {
		return ntransactionstatus;
	}
	public void setNtransactionstatus(Integer ntransactionstatus) {
		this.ntransactionstatus = ntransactionstatus;
	}
	public Integer getNmaterialcatcode() {
		return nmaterialcatcode;
	}
	public void setNmaterialcatcode(Integer nmaterialcatcode) {
		this.nmaterialcatcode = nmaterialcatcode;
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
	public String getJsondata() {
		return jsondata;
	}
	public void setJsondata(String jsondata) {
		this.jsondata = jsondata;
	}
	public String getJsonuidata() {
		return jsonuidata;
	}
	public void setJsonuidata(String jsonuidata) {
		this.jsonuidata = jsonuidata;
	}
	
	public String getSunitname() {
		return sunitname;
	}
	public void setSunitname(String sunitname) {
		this.sunitname = sunitname;
	}
	public short getNeedsection() {
		return needsection;
	}
	public void setNeedsection(short needsection) {
		this.needsection = needsection;
	}
	public int getNproductcode() {
		return nproductcode;
	}
	public void setNproductcode(int nproductcode) {
		this.nproductcode = nproductcode;
	}
	public String getSproductname() {
		return sproductname;
	}
	public void setSproductname(String sproductname) {
		this.sproductname = sproductname;
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
	public String getSmaterialname() {
		return smaterialname;
	}
	public void setSmaterialname(String smaterialname) {
		this.smaterialname = smaterialname;
	}
	
}
