package com.agaram.eln.primary.model.methodsetup;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class holds list of ParserBlock Objects
 * @author ATE153
 * @version 1.0.0
 * @since   29- Mar- 2020
 */
@XmlRootElement(name = "parserblocks")
public class ParserBlocks
{
	private List<ParserBlock> parserblocks = null;

	@XmlElement(name = "parserblock")	
	public List<ParserBlock> getParserblocks() {
		return parserblocks;
	}

	public void setParserblocks(List<ParserBlock> parserblocks) {
		this.parserblocks = parserblocks;
	}
}
