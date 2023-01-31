package com.agaram.eln.primary.model.material;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Entity
@Table(name = "period")
public class Period {

	@Id
	@Column(name = "nperiodcode")
	private Integer nperiodcode;
	@Type(type = "jsonb")
	@Column(name = "jsondata", columnDefinition = "jsonb")
	private String jsondata;
	@Column(name = "ndefaultstatus")
	private Integer ndefaultstatus;
	@Column(name = "nsitecode")
	private Integer nsitecode;
	@Column(name = "nstatus")
	private Integer nstatus;
	private String speriodname;

	public String getSperiodname() {
		return speriodname;
	}

	public void setSperiodname(String speriodname) {
		this.speriodname = speriodname;
	}

	public Integer getNperiodcode() {
		return nperiodcode;
	}

	public void setNperiodcode(Integer nperiodcode) {
		this.nperiodcode = nperiodcode;
	}

	public Map<String, Object> getJsondata() throws JsonParseException, JsonMappingException, IOException {

		if (jsondata.isEmpty()) {
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

	public Integer getNdefaultstatus() {
		return ndefaultstatus;
	}

	public void setNdefaultstatus(Integer ndefaultstatus) {
		this.ndefaultstatus = ndefaultstatus;
	}

	public Integer getNsitecode() {
		return nsitecode;
	}

	public void setNsitecode(Integer nsitecode) {
		this.nsitecode = nsitecode;
	}

	public Integer getNstatus() {
		return nstatus;
	}

	public void setNstatus(Integer nstatus) {
		this.nstatus = nstatus;
	}
}