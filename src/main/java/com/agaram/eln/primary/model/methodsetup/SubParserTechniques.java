package com.agaram.eln.primary.model.methodsetup;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class holds list of SubParserTechnique Objects
 * @author ATE153
 * @version 1.0.0
 * @since   29- Mar- 2020
 */
@XmlRootElement(name = "subparsertechniques")
public class SubParserTechniques
{
	private List<SubParserTechnique> subparsertechniques = null;

	@XmlElement(name = "subparsertechnique")	
	public List<SubParserTechnique> getSubparsertechniques() {
		return subparsertechniques;
	}

	public void setSubparsertechniques(List<SubParserTechnique> subparsertechniques) {
		this.subparsertechniques = subparsertechniques;
	}
}
