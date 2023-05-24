package com.agaram.eln.primary.model.instrumentsetup;

import com.agaram.eln.primary.model.instrumentsetup.InstrumentMaster;

/**
 * THis projection class holds essential properties of InstrumentRights entity.
 * @author ATE153
 * @version 1.0.0
 * @since   11- Nov- 2019
 */
public class InstRightsEssential {
	
	private Integer instrightskey;
	private InstrumentMaster master;
	private Integer status;	
	
	public InstRightsEssential() {}
	
	public InstRightsEssential(InstrumentMaster master, Integer instrightskey, Integer status) {
		super();
		this.instrightskey = instrightskey;
		this.master = master;
		this.status = status;
	}
	
	public Integer getInstrightskey() {
		return instrightskey;
	}
	public void setInstrightskey(Integer instrightskey) {
		this.instrightskey = instrightskey;
	}
	public InstrumentMaster getMaster() {
		return master;
	}
	public void setMaster(InstrumentMaster master) {
		this.master = master;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
}
