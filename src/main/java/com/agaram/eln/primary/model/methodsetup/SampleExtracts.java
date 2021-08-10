package com.agaram.eln.primary.model.methodsetup;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class holds list of SampleExtractSplit Objects
 * @author ATE153
 * @version 1.0.0
 * @since   20- Feb- 2020
 */
@XmlRootElement(name = "sampleextracts")
public class SampleExtracts
{
	private List<SampleExtract> sampleextracts = null;

	@XmlElement(name = "sampleextract")	
	public List<SampleExtract> getSampleextracts() {
		return sampleextracts;
	}

	public void setSampleextracts(List<SampleExtract> sampleextracts) {
		this.sampleextracts = sampleextracts;
	}

}