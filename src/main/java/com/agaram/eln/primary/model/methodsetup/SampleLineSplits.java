package com.agaram.eln.primary.model.methodsetup;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class holds list of SampleLineSplit Objects
 * @author ATE153
 * @version 1.0.0
 * @since   20- Feb- 2020
 */
@XmlRootElement(name = "samplelinesplits")
public class SampleLineSplits
{
	private List<SampleLineSplit> samplelinesplits = null;

	@XmlElement(name = "samplelinesplit")	
	public List<SampleLineSplit> getSamplelinesplits() {
		return samplelinesplits;
	}

	public void setSamplelinesplits(List<SampleLineSplit> samplelinesplits) {
		this.samplelinesplits = samplelinesplits;
	}
}