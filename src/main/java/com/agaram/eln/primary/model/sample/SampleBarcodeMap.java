package com.agaram.eln.primary.model.sample;

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
@Table(name = "samplebarcodemap")
public class SampleBarcodeMap {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "barcodemapid")
	private Integer barcodemapid;
	
	private Integer nsampletypecode;
	
	@ManyToOne
	private BarcodeMaster barcode;

	public Integer getBarcodemapid() {
		return barcodemapid;
	}

	public void setBarcodemapid(Integer barcodemapid) {
		this.barcodemapid = barcodemapid;
	}

	public Integer getNsampletypecode() {
		return nsampletypecode;
	}

	public void setNsampletypecode(Integer nsampletypecode) {
		this.nsampletypecode = nsampletypecode;
	}

	public BarcodeMaster getBarcode() {
		return barcode;
	}

	public void setBarcode(BarcodeMaster barcode) {
		this.barcode = barcode;
	}
	
	
}
