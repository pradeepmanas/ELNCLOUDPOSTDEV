package com.agaram.eln.primary.model.inventory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="LSinstrumentsection")
public class LSinstrumentsection {
	@Id
	@Column(name = "instrumentsectioncode")
	private Integer ninstrumentsectioncode;
	@Column(name = "sectioncode")
	private Integer nsectioncode;
	@Column(name = "instrumentcode")
	private Integer ninstrumentcode;
	@Column(name = "status")
	private Integer nstatus;
	@Column(name = "masterauditcode")
	private Integer nmasterauditcode;
	@Column(name = "defaultstatus")
	private Integer ndefaultstatus;
	@Column(name = "usercode")
	private Integer nusercode;
	
	//@Column(columnDefinition = "nvarchar(100)") 
	private transient String username;
	//@Column(columnDefinition = "nvarchar(100)") 
	transient private String sectionname;
	//@Column(columnDefinition = "nvarchar(100)") 
	transient private String sdefaultstatus;
	transient private Integer sitecode;
	
	public Integer getNinstrumentsectioncode() {
		return ninstrumentsectioncode;
	}
	public void setNinstrumentsectioncode(Integer ninstrumentsectioncode) {
		this.ninstrumentsectioncode = ninstrumentsectioncode;
	}
	public Integer getNsectioncode() {
		return nsectioncode;
	}
	public void setNsectioncode(Integer nsectioncode) {
		this.nsectioncode = nsectioncode;
	}
	public Integer getNinstrumentcode() {
		return ninstrumentcode;
	}
	public void setNinstrumentcode(Integer ninstrumentcode) {
		this.ninstrumentcode = ninstrumentcode;
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
	public Integer getNdefaultstatus() {
		return ndefaultstatus;
	}
	public void setNdefaultstatus(Integer ndefaultstatus) {
		this.ndefaultstatus = ndefaultstatus;
	}
	public Integer getNusercode() {
		return nusercode;
	}
	public void setNusercode(Integer nusercode) {
		this.nusercode = nusercode;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getSectionname() {
		return sectionname;
	}
	public void setSectionname(String sectionname) {
		this.sectionname = sectionname;
	}
	public String getSdefaultstatus() {
		return sdefaultstatus;
	}
	public void setSdefaultstatus(String sdefaultstatus) {
		this.sdefaultstatus = sdefaultstatus;
	}
	public Integer getSitecode() {
		return sitecode;
	}
	public void setSitecode(Integer sitecode) {
		this.sitecode = sitecode;
	}
	
	
}
