package com.agaram.eln.primary.model.sheetManipulation;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.agaram.eln.primary.model.instrumentsetup.InstrumentMaster;
import com.agaram.eln.primary.model.methodsetup.Method;

@Entity
@Table(name = "LSfileelnmethod")
public class LSfileelnmethod {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "fileelnmethodcode")
	private Integer fileelnmethodcode;
	
	private Integer filecode;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "methodkey", nullable = false)
	private Method method;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "instmasterkey", nullable = false)
	private InstrumentMaster instrument;

	public Integer getFileelnmethodcode() {
		return fileelnmethodcode;
	}

	public void setFileelnmethodcode(Integer fileelnmethodcode) {
		this.fileelnmethodcode = fileelnmethodcode;
	}

	public Integer getFilecode() {
		return filecode;
	}

	public void setFilecode(Integer filecode) {
		this.filecode = filecode;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public InstrumentMaster getInstrument() {
		return instrument;
	}

	public void setInstrument(InstrumentMaster instrument) {
		this.instrument = instrument;
	}

}
