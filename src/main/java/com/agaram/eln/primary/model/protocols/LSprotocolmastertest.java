package com.agaram.eln.primary.model.protocols;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.model.usermanagement.LoggedUser;

@Entity
@Table(name = "LSprotocolmastertest")
public class LSprotocolmastertest {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "protocoltestcode")
	private int protocoltestcode;
	
	private Integer protocolmastercode;
	
	private Integer testtype;
	
	private Integer testcode;
	
	@Transient
	private Response response;
	
	@Transient
	LSuserMaster LSuserMaster;
	
	@Transient
	LoggedUser objuser;
	
	@Transient
	LSuserMaster objLoggeduser;

	public LSuserMaster getObjLoggeduser() {
		return objLoggeduser;
	}

	public void setObjLoggeduser(LSuserMaster objLoggeduser) {
		this.objLoggeduser = objLoggeduser;
	}

	public int getProtocoltestcode() {
		return protocoltestcode;
	}

	public void setProtocoltestcode(int protocoltestcode) {
		this.protocoltestcode = protocoltestcode;
	}

	public Integer getProtocolmastercode() {
		return protocolmastercode;
	}

	public void setProtocolmastercode(Integer protocolmastercode) {
		this.protocolmastercode = protocolmastercode;
	}

	public Integer getTesttype() {
		return testtype;
	}

	public void setTesttype(Integer testtype) {
		this.testtype = testtype;
	}

	public Integer getTestcode() {
		return testcode;
	}

	public void setTestcode(Integer testcode) {
		this.testcode = testcode;
	}

	public Response getResponse() {
		return response;
	}

	public void setResponse(Response response) {
		this.response = response;
	}

	public LSuserMaster getLSuserMaster() {
		return LSuserMaster;
	}

	public void setLSuserMaster(LSuserMaster lSuserMaster) {
		LSuserMaster = lSuserMaster;
	}

	public LoggedUser getObjuser() {
		return objuser;
	}

	public void setObjuser(LoggedUser objuser) {
		this.objuser = objuser;
	}	
}
