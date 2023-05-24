package com.agaram.eln.primary.model.material;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.ColumnDefault;

import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.general.Response;

@Entity
@Table(name = "supplier")
public class Supplier {
	
	@Id
	@Column(name = "nsuppliercode")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer nsuppliercode;

	@Column(name = "ssuppliername", length = 100, nullable = false)
	private String ssuppliername;

	@Column(name = "saddress1", length = 255, nullable = false)
	private String saddress1;

	@Column(name = "saddress2", length = 255)
	private String saddress2;

	@Column(name = "saddress3", length = 255)
	private String saddress3;

	@Column(name = "sphoneno", length = 50)
	private String sphoneno;

	@Column(name = "smobileno", length = 50, nullable = false)
	private String smobileno;

	@Column(name = "sfaxno", length = 50)
	private String sfaxno;

	@Column(name = "semail", length = 50, nullable = false)
	private String semail;

	@Column(name = "ntransactionstatus", nullable = false)
	@ColumnDefault("8")
	private Integer ntransactionstatus = 8;

	@Column(name = "nsitecode", nullable = false)
	@ColumnDefault("-1")
	private Integer nsitecode = -1;

	@Column(name = "nstatus", nullable = false)
	@ColumnDefault("1")
	private Integer nstatus = 1;

	private transient String sdisplaystatus;
	private String scountryname;
	
	@Transient
	public String info;
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	
	@Transient
	private Response response;
	public Response getResponse() {
		return response;
	}
	public void setResponse(Response response) {
		this.response = response;
	}
	
	@Transient
	private LScfttransaction objsilentaudit;
	public LScfttransaction getObjsilentaudit() {
		return objsilentaudit;
	}
	public void setObjsilentaudit(LScfttransaction objsilentaudit) {
		this.objsilentaudit = objsilentaudit;
	}
	
	public Integer getNsuppliercode() {
		return nsuppliercode;
	}
	public void setNsuppliercode(Integer nsuppliercode) {
		this.nsuppliercode = nsuppliercode;
	}
	public String getSsuppliername() {
		return ssuppliername;
	}
	public void setSsuppliername(String ssuppliername) {
		this.ssuppliername = ssuppliername;
	}
	public String getSaddress1() {
		return saddress1;
	}
	public void setSaddress1(String saddress1) {
		this.saddress1 = saddress1;
	}
	public String getSaddress2() {
		return saddress2;
	}
	public void setSaddress2(String saddress2) {
		this.saddress2 = saddress2;
	}
	public String getSaddress3() {
		return saddress3;
	}
	public void setSaddress3(String saddress3) {
		this.saddress3 = saddress3;
	}
	public String getSphoneno() {
		return sphoneno;
	}
	public void setSphoneno(String sphoneno) {
		this.sphoneno = sphoneno;
	}
	public String getSmobileno() {
		return smobileno;
	}
	public void setSmobileno(String smobileno) {
		this.smobileno = smobileno;
	}
	public String getSfaxno() {
		return sfaxno;
	}
	public void setSfaxno(String sfaxno) {
		this.sfaxno = sfaxno;
	}
	public String getSemail() {
		return semail;
	}
	public void setSemail(String semail) {
		this.semail = semail;
	}
	public Integer getNtransactionstatus() {
		return ntransactionstatus;
	}
	public void setNtransactionstatus(Integer ntransactionstatus) {
		this.ntransactionstatus = ntransactionstatus;
	}
	public Integer getNsitecode() {
		return nsitecode;
	}
	public void setNsitecode(Integer nsitecode) {
		this.nsitecode = nsitecode;
	}
	public Integer getNstatus() {
		return nstatus;
	}
	public void setNstatus(Integer nstatus) {
		this.nstatus = nstatus;
	}
	public String getSdisplaystatus() {
		return sdisplaystatus;
	}
	public void setSdisplaystatus(String sdisplaystatus) {
		this.sdisplaystatus = sdisplaystatus;
	}
	public String getScountryname() {
		return scountryname;
	}
	public void setScountryname(String scountryname) {
		this.scountryname = scountryname;
	}
}
