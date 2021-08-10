package com.agaram.eln.primary.model.methodsetup;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class holds list of SubParserField Objects
 * @author ATE153
 * @version 1.0.0
 * @since   29- Mar- 2020
 */
@XmlRootElement(name = "subparserfields")
public class SubParserFields
{
	private List<SubParserField> subparserfields = null;

	@XmlElement(name = "subparserfield")	
	public List<SubParserField> getSubparserfields() {
		return subparserfields;
	}

	public void setSubparserfields(List<SubParserField> subparserfields) {
		this.subparserfields = subparserfields;
	}
}