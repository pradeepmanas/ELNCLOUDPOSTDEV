package com.agaram.eln.primary.fetchmodel.getmasters;

import java.util.List;

import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.material.Elnmaterial;
import com.agaram.eln.primary.model.material.Material;
import com.agaram.eln.primary.model.material.Section;
import com.agaram.eln.primary.model.material.Unit;
import com.agaram.eln.primary.model.usermanagement.LSMultiusergroup;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;

public class Listofallmaster {
	
	String screenname;
	List<LSuserMaster> lsusermaster;
	LSSiteMaster lssitemaster;
	List<LSMultiusergroup> multiusergroupcode;
	Response objResponse;
	
	List<Unit> unit;
	List<Section> section;
	List<Material> material;
	
	List<Elnmaterial> elnmaterial;

	public List<Elnmaterial> getElnmaterial() {
		return elnmaterial;
	}

	public void setElnmaterial(List<Elnmaterial> elnmaterial) {
		this.elnmaterial = elnmaterial;
	}

	public List<Material> getMaterial() {
		return material;
	}

	public void setMaterial(List<Material> material) {
		this.material = material;
	}

	public List<Section> getSection() {
		return section;
	}

	public void setSection(List<Section> section) {
		this.section = section;
	}

	public List<Unit> getUnit() {
		return unit;
	}

	public void setUnit(List<Unit> unit) {
		this.unit = unit;
	}

	public Response getObjResponse() {
		return objResponse;
	}

	public void setObjResponse(Response objResponse) {
		this.objResponse = objResponse;
	}

	public List<LSMultiusergroup> getMultiusergroupcode() {
		return multiusergroupcode;
	}

	public void setMultiusergroupcode(List<LSMultiusergroup> multiusergroupcode) {
		this.multiusergroupcode = multiusergroupcode;
	}

	public LSSiteMaster getLssitemaster() {
		return lssitemaster;
	}

	public void setLssitemaster(LSSiteMaster lssitemaster) {
		this.lssitemaster = lssitemaster;
	}

	public List<LSuserMaster> getLsusermaster() {
		return lsusermaster;
	}

	public void setLsusermaster(List<LSuserMaster> lsusermaster) {
		this.lsusermaster = lsusermaster;
	}

	public String getScreenname() {
		return screenname;
	}

	public void setScreenname(String screenname) {
		this.screenname = screenname;
	}

}
