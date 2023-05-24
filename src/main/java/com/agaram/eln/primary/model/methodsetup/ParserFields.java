package com.agaram.eln.primary.model.methodsetup;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class holds list of ParserField Objects
 * @author ATE153
 * @version 1.0.0
 * @since   29- Mar- 2020
 */
@XmlRootElement(name = "parserfields")
public class ParserFields
{
	private List<ParserField> parserfields = null;

	@XmlElement(name = "parserfield")	
	public List<ParserField> getParserfields() {
		return parserfields;
	}

	public void setParserfields(List<ParserField> parserfields) {
		this.parserfields = parserfields;
	}
}

