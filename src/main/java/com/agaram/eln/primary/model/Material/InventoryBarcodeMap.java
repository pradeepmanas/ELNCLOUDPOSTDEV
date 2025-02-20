package com.agaram.eln.primary.model.material;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.agaram.eln.primary.model.barcode.BarcodeMaster;

@Entity
@Table(name = "inventorybarcodemap")
public class InventoryBarcodeMap {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "barcodemapid")
	private Integer barcodemapid;
	
	private Integer nmaterialtypecode;
	
	@ManyToOne
	private BarcodeMaster barcode;

	public Integer getBarcodemapid() {
		return barcodemapid;
	}

	public void setBarcodemapid(Integer barcodemapid) {
		this.barcodemapid = barcodemapid;
	}

	public Integer getNmaterialtypecode() {
		return nmaterialtypecode;
	}

	public void setNmaterialtypecode(Integer nmaterialtypecode) {
		this.nmaterialtypecode = nmaterialtypecode;
	}

	public BarcodeMaster getBarcode() {
		return barcode;
	}

	public void setBarcode(BarcodeMaster barcode) {
		this.barcode = barcode;
	}
	
	
}
