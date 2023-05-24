package com.agaram.eln.primary.model.material;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Entity
@Table(name = "materialinventorytype")
public class MaterialInventoryType {
	@Id
	@Column(name = "ninventorytypecode")
	private Integer ninventorytypecode;
	@Column(name = "jsondata", columnDefinition = "jsonb")
	private String jsondata;
	@Column(name = "nstatus")
	private Integer nstatus;
	@Column(name = "ndefaultstatus")
	private Integer ndefaultstatus;
	
	public Integer getNinventorytypecode() {
		return ninventorytypecode;
	}
	public void setNinventorytypecode(Integer ninventorytypecode) {
		this.ninventorytypecode = ninventorytypecode;
	}
	public Map<String,Object> getJsondata() throws JsonParseException, JsonMappingException, IOException {
		
		if(jsondata.isEmpty()) {
			Map<String, Object> resObj = new HashMap<String, Object>();
			
			return resObj;
		}
		@SuppressWarnings("unchecked")
		Map<String, Object> resObj = new ObjectMapper().readValue(jsondata, Map.class);
		
		return resObj;
	}
	
	public void setJsondata(String jsondata) {
		this.jsondata = jsondata;
	}
	public Integer getNstatus() {
		return nstatus;
	}
	public void setNstatus(Integer nstatus) {
		this.nstatus = nstatus;
	}
	public Integer getNdefaultstatus() {
		return ndefaultstatus;
	}
	public void setNdefaultstatus(Integer ndefaultstatus) {
		this.ndefaultstatus = ndefaultstatus;
	}
	
	
}
