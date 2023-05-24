package com.agaram.eln.primary.model.inventory;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "LSequipmentmap")
public class LSequipmentmap {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "equipmentcode")
	private Integer equipmentcode;
	private Integer testcode;
	private Integer instrumentcode;
	
	@ManyToOne
	private LSinstrument LSinstrument;
	
	public LSinstrument getLSinstrument() {
		return LSinstrument;
	}
	public void setLSinstrument(LSinstrument lSinstrument) {
		LSinstrument = lSinstrument;
	}
	public Integer getEquipmentcode() {
		return equipmentcode;
	}
	public void setEquipmentcode(Integer equipmentcode) {
		this.equipmentcode = equipmentcode;
	}
	public Integer getTestcode() {
		return testcode;
	}
	public void setTestcode(Integer testcode) {
		this.testcode = testcode;
	}
	public Integer getInstrumentcode() {
		return instrumentcode;
	}
	public void setInstrumentcode(Integer instrumentcode) {
		this.instrumentcode = instrumentcode;
	}
	
	
	
}
