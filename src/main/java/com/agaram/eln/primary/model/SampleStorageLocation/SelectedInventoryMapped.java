package com.agaram.eln.primary.model.samplestoragelocation;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity(name = "selectedinventorymapped")
@Table(name = "selectedinventorymapped")
public class SelectedInventoryMapped {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "mappedid") 
	private Integer mappedid;
	
	private String storagepath;

	public String id;

//	@OneToOne(cascade = CascadeType.ALL)
//	@JoinColumn(name = "nmaterialinventorycode", referencedColumnName = "nmaterialinventorycode")
//	private MaterialInventory nmaterialinventorycode;
	
	private Integer nmaterialinventorycode;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "samplestoragelocationkey", referencedColumnName = "samplestoragelocationkey")
	private SampleStorageLocation samplestoragelocationkey;
	
	public String getStoragepath() {
		return storagepath;
	}

	public void setStoragepath(String storagepath) {
		this.storagepath = storagepath;
	}

	public SampleStorageLocation getSamplestoragelocationkey() {
		return samplestoragelocationkey;
	}

	public void setSamplestoragelocationkey(SampleStorageLocation samplestoragelocationkey) {
		this.samplestoragelocationkey = samplestoragelocationkey;
	}

	public Integer getMappedid() {
		return mappedid;
	}

	public void setMappedid(Integer mappedid) {
		this.mappedid = mappedid;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getNmaterialinventorycode() {
		return nmaterialinventorycode;
	}

	public void setNmaterialinventorycode(Integer nmaterialinventorycode) {
		this.nmaterialinventorycode = nmaterialinventorycode;
	}

	
	
//	public MaterialInventory getNmaterialinventorycode() {
//		return nmaterialinventorycode;
//	}
//
//	public void setNmaterialinventorycode(MaterialInventory nmaterialinventorycode) {
//		this.nmaterialinventorycode = nmaterialinventorycode;
//	}
}