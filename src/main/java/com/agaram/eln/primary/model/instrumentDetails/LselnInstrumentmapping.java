package com.agaram.eln.primary.model.instrumentDetails;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "LselnInstrumentmapping")
public class LselnInstrumentmapping {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "instrumentmapcode")
	private Integer instrumentmapcode;
	
	private Integer instrumentcode;
	
	@ManyToOne 
	private LSinstruments lsinstruments;

	public Integer getInstrumentmapcode() {
		return instrumentmapcode;
	}

	public void setInstrumentmapcode(Integer instrumentmapcode) {
		this.instrumentmapcode = instrumentmapcode;
	}

	public Integer getInstrumentcode() {
		return instrumentcode;
	}

	public void setInstrumentcode(Integer instrumentcode) {
		this.instrumentcode = instrumentcode;
	}

	public LSinstruments getLsinstruments() {
		return lsinstruments;
	}

	public void setLsinstruments(LSinstruments lsinstruments) {
		this.lsinstruments = lsinstruments;
	}
}
