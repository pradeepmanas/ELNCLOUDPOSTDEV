package com.agaram.eln.primary.model.inventory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name = "LSintrumentresultmap")
public class LSintrumentresultmap {
	@Id
	private Integer instrumentcode;

	private Integer testcode;
	@Column(columnDefinition = "varchar(150)")
	private String instrumentid;
	public Integer getInstrumentcode() {
		return instrumentcode;
	}
	public void setInstrumentcode(Integer instrumentcode) {
		this.instrumentcode = instrumentcode;
	}
	public Integer getTestcode() {
		return testcode;
	}
	public void setTestcode(Integer testcode) {
		this.testcode = testcode;
	}
	public String getInstrumentid() {
		return instrumentid;
	}
	public void setInstrumentid(String instrumentid) {
		this.instrumentid = instrumentid;
	}
	
	
}
