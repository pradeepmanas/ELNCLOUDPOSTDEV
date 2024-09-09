package com.agaram.eln.primary.model.barcode;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.general.ScreenMaster;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;

@Entity
@Table(name = "BarcodeMaster")
public class BarcodeMaster {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Basic(optional = false)
	private Integer barcodeno;
	
	private String barcodename;
	private Integer status;
	@ManyToOne
	private LSuserMaster createdby;
	@ManyToOne
	private ScreenMaster screen;
	private Date createdon;
	private String barcodefileid;
	private String barcodefilename;
	@ManyToOne
	private LSSiteMaster lssitemaster;
	
	public Integer getBarcodeno() {
		return barcodeno;
	}
	public void setBarcodeno(Integer barcodeno) {
		this.barcodeno = barcodeno;
	}
	public String getBarcodename() {
		return barcodename;
	}
	public void setBarcodename(String barcodename) {
		this.barcodename = barcodename;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public LSuserMaster getCreatedby() {
		return createdby;
	}
	public void setCreatedby(LSuserMaster createdby) {
		this.createdby = createdby;
	}
	public Date getCreatedon() {
		return createdon;
	}
	public void setCreatedon(Date createdon) {
		this.createdon = createdon;
	}
	public String getBarcodefileid() {
		return barcodefileid;
	}
	public void setBarcodefileid(String barcodefileid) {
		this.barcodefileid = barcodefileid;
	}
	public ScreenMaster getScreen() {
		return screen;
	}
	public void setScreen(ScreenMaster screen) {
		this.screen = screen;
	}
	public LSSiteMaster getLssitemaster() {
		return lssitemaster;
	}
	public void setLssitemaster(LSSiteMaster lssitemaster) {
		this.lssitemaster = lssitemaster;
	}
	public String getBarcodefilename() {
		return barcodefilename;
	}
	public void setBarcodefilename(String barcodefilename) {
		this.barcodefilename = barcodefilename;
	}

	@Transient
	private Response response;
	
	public Response getResponse() {
		return response;
	}
	public void setResponse(Response response) {
		this.response = response;
	}
	
	

	
}
