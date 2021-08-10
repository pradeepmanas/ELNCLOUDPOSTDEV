package com.agaram.eln.primary.model.methodsetup;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class holds list of ParserTechnique Objects
 * @author ATE153
 * @version 1.0.0
 * @since   29- Mar- 2020
 */
@XmlRootElement(name = "parsertechniques")
public class ParserTechniques
{
	private List<ParserTechnique> parsertechniques = null;

	@XmlElement(name = "parsertechnique")	
	public List<ParserTechnique> getParsertechniques() {
		return parsertechniques;
	}

	public void setParsertechniques(List<ParserTechnique> parsertechniques) {
		this.parsertechniques = parsertechniques;
	}

}


