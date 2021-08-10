package com.agaram.eln.primary.model.instrumentsetup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * This class holds list of InstrumentRights Objects
 * @author ATE153
 * @version 1.0.0
 * @since   12- Nov- 2019
 */
@XmlRootElement(name = "instrumentrightscollection")
@XmlSeeAlso(ArrayList.class)
public class InstrumentRightsCollection implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<InstrumentRights> instRightsCollection = null;

	@XmlElement(name = "instrumentrights")
	public List<InstrumentRights> getInstRightsCollection() {
		return instRightsCollection;
	}

	public void setInstRightsCollection(List<InstrumentRights> instRightsCollection) {
		this.instRightsCollection = instRightsCollection;
	}	
  
}
