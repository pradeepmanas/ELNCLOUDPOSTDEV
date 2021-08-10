package com.agaram.eln.primary.model.methodsetup;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class holds list of CustomField Objects
 * @author ATE153
 * @version 1.0.0
 * @since   07- Apr- 2020
 */
@XmlRootElement(name = "customfields")
public class CustomFields {
	private List<CustomField> customfields = null;

	@XmlElement(name = "customfield")	
	public List<CustomField> getCustomfields() {
		return customfields;
	}

	public void setCustomfields(List<CustomField> customfields) {
		this.customfields = customfields;
	}
	
}
