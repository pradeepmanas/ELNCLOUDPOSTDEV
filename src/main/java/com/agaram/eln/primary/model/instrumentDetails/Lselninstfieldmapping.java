package com.agaram.eln.primary.model.instrumentDetails;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Lselninstfieldmapping")
public class Lselninstfieldmapping {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "instfieldmapcode")
	private Integer instfieldmapcode;
	
	private Integer fieldcode;
	
//	@ManyToOne 
//	private LsMethodFields lsMethodFields;

	@ManyToOne
	@JoinColumn(columnDefinition = "nvarchar(12)")
	private LsMappedFields lsMappedFields;
	
	public Integer getInstfieldmapcode() {
		return instfieldmapcode;
	}

	public void setInstfieldmapcode(Integer instfieldmapcode) {
		this.instfieldmapcode = instfieldmapcode;
	}

	public Integer getFieldcode() {
		return fieldcode;
	}

	public void setFieldcode(Integer fieldcode) {
		this.fieldcode = fieldcode;
	}

	public LsMappedFields getLsMappedFields() {
		return lsMappedFields;
	}

	public void setLsMappedFields(LsMappedFields lsMappedFields) {
		this.lsMappedFields = lsMappedFields;
	}

//	public LsMethodFields getLsMethodFields() {
//		return lsMethodFields;
//	}
//
//	public void setLsMethodFields(LsMethodFields lsMethodFields) {
//		this.lsMethodFields = lsMethodFields;
//	}
	
	
}
