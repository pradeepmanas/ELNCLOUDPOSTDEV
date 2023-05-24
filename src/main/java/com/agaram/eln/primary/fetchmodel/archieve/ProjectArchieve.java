package com.agaram.eln.primary.fetchmodel.archieve;

import java.util.List;

import com.agaram.eln.primary.model.instrumentDetails.LSlogilablimsorderdetail;
import com.agaram.eln.primary.model.protocols.LSlogilabprotocoldetail;
import com.agaram.eln.primary.model.usermanagement.LSprojectmaster;

public class ProjectArchieve {
	LSprojectmaster lsprojectmaster;
	List<LSlogilablimsorderdetail> lstlslogilablimsorderdetail;
	List<LSlogilabprotocoldetail> lstlslogilabprotocoldetail;
	
	public LSprojectmaster getLsprojectmaster() {
		return lsprojectmaster;
	}
	public void setLsprojectmaster(LSprojectmaster lsprojectmaster) {
		this.lsprojectmaster = lsprojectmaster;
	}
	public List<LSlogilablimsorderdetail> getLstlslogilablimsorderdetail() {
		return lstlslogilablimsorderdetail;
	}
	public void setLstlslogilablimsorderdetail(List<LSlogilablimsorderdetail> lstlslogilablimsorderdetail) {
		this.lstlslogilablimsorderdetail = lstlslogilablimsorderdetail;
	}
	public List<LSlogilabprotocoldetail> getLstlslogilabprotocoldetail() {
		return lstlslogilabprotocoldetail;
	}
	public void setLstlslogilabprotocoldetail(List<LSlogilabprotocoldetail> lstlslogilabprotocoldetail) {
		this.lstlslogilabprotocoldetail = lstlslogilabprotocoldetail;
	}
	
	
}
