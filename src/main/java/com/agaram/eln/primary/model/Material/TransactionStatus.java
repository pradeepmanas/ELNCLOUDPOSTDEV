package com.agaram.eln.primary.model.material;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Entity
@Table(name = "transactionStatus")
public class TransactionStatus {
	
	@Id
	@Column(name = "ntranscode")
	private Integer ntranscode;

	@Column(name = "stransstatus", length = 50, nullable = false)
	private String stransstatus;

	private transient String stransdisplaystatus;

	private transient String sactiondisplaystatus;

	private transient String salertdisplaystatus;
	
	@Column(name = "jsondata", columnDefinition = "jsonb")
	private String jsondata;

	@Column(name = "nstatus", nullable = false)
	@ColumnDefault("1")
	private Integer nstatus;

	private transient String svalidationtypename;
	private transient int napprovalconfigcode;
	private transient String scolorname;
	private transient int ntransactiontype;
	private transient int nactiontype;
	private transient int ntranscode1;
	private transient int nsorter;
	private transient Integer ntransactionstatus;
	private transient String sfilterstatus;
	private transient String sdefaultname;
	public Integer getNtranscode() {
		return ntranscode;
	}
	public void setNtranscode(Integer ntranscode) {
		this.ntranscode = ntranscode;
	}
	public String getStransstatus() {
		return stransstatus;
	}
	public void setStransstatus(String stransstatus) {
		this.stransstatus = stransstatus;
	}
	public String getStransdisplaystatus() {
		return stransdisplaystatus;
	}
	public void setStransdisplaystatus(String stransdisplaystatus) {
		this.stransdisplaystatus = stransdisplaystatus;
	}
	public String getSactiondisplaystatus() {
		return sactiondisplaystatus;
	}
	public void setSactiondisplaystatus(String sactiondisplaystatus) {
		this.sactiondisplaystatus = sactiondisplaystatus;
	}
	public String getSalertdisplaystatus() {
		return salertdisplaystatus;
	}
	public void setSalertdisplaystatus(String salertdisplaystatus) {
		this.salertdisplaystatus = salertdisplaystatus;
	}
	public Map<String,Object> getJsondata() throws JsonParseException, JsonMappingException, IOException {
		
		if(jsondata.isEmpty()) {
			Map<String, Object> resObj = new HashMap<String, Object>();
			
			return resObj;
		}
		
		@SuppressWarnings("unchecked")
		Map<String, Object> resObj = new ObjectMapper().readValue(jsondata, Map.class);
		
		return resObj;
	}
	public void setJsondata(String jsondata) {
		this.jsondata = jsondata;
	}
	public Integer getNstatus() {
		return nstatus;
	}
	public void setNstatus(Integer nstatus) {
		this.nstatus = nstatus;
	}
	public String getSvalidationtypename() {
		return svalidationtypename;
	}
	public void setSvalidationtypename(String svalidationtypename) {
		this.svalidationtypename = svalidationtypename;
	}
	public int getNapprovalconfigcode() {
		return napprovalconfigcode;
	}
	public void setNapprovalconfigcode(int napprovalconfigcode) {
		this.napprovalconfigcode = napprovalconfigcode;
	}
	public String getScolorname() {
		return scolorname;
	}
	public void setScolorname(String scolorname) {
		this.scolorname = scolorname;
	}
	public int getNtransactiontype() {
		return ntransactiontype;
	}
	public void setNtransactiontype(int ntransactiontype) {
		this.ntransactiontype = ntransactiontype;
	}
	public int getNactiontype() {
		return nactiontype;
	}
	public void setNactiontype(int nactiontype) {
		this.nactiontype = nactiontype;
	}
	public int getNtranscode1() {
		return ntranscode1;
	}
	public void setNtranscode1(int ntranscode1) {
		this.ntranscode1 = ntranscode1;
	}
	public int getNsorter() {
		return nsorter;
	}
	public void setNsorter(int nsorter) {
		this.nsorter = nsorter;
	}
	public Integer getNtransactionstatus() {
		return ntransactionstatus;
	}
	public void setNtransactionstatus(Integer ntransactionstatus) {
		this.ntransactionstatus = ntransactionstatus;
	}
	public String getSfilterstatus() {
		return sfilterstatus;
	}
	public void setSfilterstatus(String sfilterstatus) {
		this.sfilterstatus = sfilterstatus;
	}
	public String getSdefaultname() {
		return sdefaultname;
	}
	public void setSdefaultname(String sdefaultname) {
		this.sdefaultname = sdefaultname;
	}
}