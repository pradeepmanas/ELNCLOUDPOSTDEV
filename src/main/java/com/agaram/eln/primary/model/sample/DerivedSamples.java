package com.agaram.eln.primary.model.sample;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "derivedsamples")
public class DerivedSamples {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer derivedsamplecode;
	
	private Integer samplecode;
	
	@ManyToOne
	private Sample parentsample;

	public Integer getDerivedsamplecode() {
		return derivedsamplecode;
	}

	public void setDerivedsamplecode(Integer derivedsamplecode) {
		this.derivedsamplecode = derivedsamplecode;
	}

	public Integer getSamplecode() {
		return samplecode;
	}

	public void setSamplecode(Integer samplecode) {
		this.samplecode = samplecode;
	}

	public Sample getParentsample() {
		return parentsample;
	}

	public void setParentsample(Sample parentsample) {
		this.parentsample = parentsample;
	}
	
	
}
