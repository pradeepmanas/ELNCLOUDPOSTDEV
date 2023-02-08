package com.agaram.eln.primary.model.material;

import java.io.Serializable;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Type;

import com.agaram.eln.primary.commonfunction.commonfunction;

import org.json.JSONObject;

@Entity
@Table(name = "materialtype")
public class MaterialType implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "nmaterialtypecode")	private Integer nmaterialtypecode;

	@Type(type = "jsonb")
	@Column(name = "jsondata", columnDefinition = "jsonb")	private String jsondata;

	@ColumnDefault("4")
	@Column(name = "ndefaultstatus", nullable = false)private Integer ndefaultstatus = 4;

	@ColumnDefault("-1")
	@Column(name = "nsitecode", nullable = false)private Integer nsitecode = -1;

	@ColumnDefault("1")
	@Column(name = "nstatus", nullable = false)	private Integer nstatus = 1;
	
	@Transient
	private String displaystatus;

	private transient String smaterialtypename;
	private transient String sdescription;
	public Integer getNmaterialtypecode() {
		return nmaterialtypecode;
	}
	public void setNmaterialtypecode(Integer nmaterialtypecode) {
		this.nmaterialtypecode = nmaterialtypecode;
	}
	public String getJsondata() {
		return jsondata;
	}
	public void setJsondata(String jsondata) {
		this.jsondata = jsondata;
	}
	public Integer getNdefaultstatus() {
		return ndefaultstatus;
	}
	public void setNdefaultstatus(Integer ndefaultstatus) {
		this.ndefaultstatus = ndefaultstatus;
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
	
	public String getSmaterialtypename() {
		
		Map<String, Object> objContent = commonfunction.getInventoryValuesFromJsonString(this.jsondata,"smaterialtypename");
		
		if(objContent.containsKey("rtnObj")) {
			JSONObject resObj = (JSONObject) objContent.get("rtnObj");
			
			return smaterialtypename = (String) resObj.get("en-US");
		}
		
		return smaterialtypename;
	}
	public void setSmaterialtypename(String smaterialtypename) {
		this.smaterialtypename = smaterialtypename;
	}
	public String getSdescription() {
		return sdescription;
	}
	public void setSdescription(String sdescription) {
		this.sdescription = sdescription;
	}
	// Parameterized Constructor to make a copy of object
//	public MaterialType(final MaterialType materialtype) {
//		this.nmaterialtypecode = materialtype.nmaterialtypecode;
//		this.smaterialtypename = materialtype.smaterialtypename;
//		this.sdescription = materialtype.sdescription;
//		this.ndefaultstatus = materialtype.getNdefaultstatus();
//		this.nsitecode = materialtype.nsitecode;
//		this.nstatus = materialtype.nstatus;
//	}
}

