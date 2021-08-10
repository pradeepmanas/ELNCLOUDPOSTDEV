package com.agaram.eln.primary.model.methodsetup;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class holds list of SampleTextSplit Objects
 * @author ATE153
 * @version 1.0.0
 * @since   20- Feb- 2020
 */
@XmlRootElement(name = "sampletextsplits")
public class SampleTextSplits
{
	private List<SampleTextSplit> sampletextsplits = null;

	@XmlElement(name = "sampletextsplit")	
	public List<SampleTextSplit> getSampletextsplits() {
		return sampletextsplits;
	}

	public void setSampletextsplits(List<SampleTextSplit> sampletextsplits) {
		this.sampletextsplits = sampletextsplits;
	}
}