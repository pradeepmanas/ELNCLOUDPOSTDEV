
package com.agaram.eln.primary.fetchmodel.getorders;

import java.util.Date;
import java.util.List;

import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.instrumentDetails.LSOrdernotification;
import com.agaram.eln.primary.model.instrumentDetails.LSlogilablimsorder;
import com.agaram.eln.primary.model.instrumentDetails.LsAutoregister;
import com.agaram.eln.primary.model.instrumentDetails.LsOrderattachments;
import com.agaram.eln.primary.model.instrumentDetails.Lsbatchdetails;
import com.agaram.eln.primary.model.instrumentDetails.Lsorderworkflowhistory;
import com.agaram.eln.primary.model.masters.Lsrepositories;
import com.agaram.eln.primary.model.masters.Lsrepositoriesdata;
import com.agaram.eln.primary.model.material.Elnmaterial;
import com.agaram.eln.primary.model.material.MaterialInventory;
import com.agaram.eln.primary.model.methodsetup.ELNFileAttachments;
import com.agaram.eln.primary.model.sheetManipulation.LSfile;
import com.agaram.eln.primary.model.sheetManipulation.LSsamplefile;
import com.agaram.eln.primary.model.sheetManipulation.LSsamplemaster;
import com.agaram.eln.primary.model.sheetManipulation.LStestmasterlocal;
import com.agaram.eln.primary.model.sheetManipulation.LStestparameter;
import com.agaram.eln.primary.model.sheetManipulation.LSworkflow;
import com.agaram.eln.primary.model.usermanagement.LScentralisedUsers;
import com.agaram.eln.primary.model.usermanagement.LSprojectmaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;

public class LogilabOrderDetails extends Logilaborders {

	private String lockedusername;
	private Response response;
	private Integer activeuser;
	private Integer isLock;
	private List<LSlogilablimsorder> lsLSlogilablimsorder;
	List<LStestparameter> lstestparameter;
	private Integer isLockbycurrentuser;
	private Integer isFinalStep;
	private LScentralisedUsers lscentralisedusers;
	private LSfile lsfile;
	private Integer approved;
	private LSsamplefile lssamplefile;
	List<Lsorderworkflowhistory> lsorderworkflowhistory;
	private LStestmasterlocal lstestmasterlocal;
	private LSworkflow lsworkflow;
	private List<Lsbatchdetails> lsbatchdetails;
	private List<LsOrderattachments> lsOrderattachments;
    private List<ELNFileAttachments> elnfileAttachments;

	

	public LogilabOrderDetails(Long batchcode, String batchid, String orderflag, Integer approvelstatus,
			Integer lockeduser, Integer testcode, String testname, LSsamplemaster lssamplemaster,
			LSprojectmaster lsprojectmaster, LSfile lsfile, Integer filetype, LSuserMaster lsuserMaster,
			LSuserMaster assignedto, LSsamplefile lssamplefile, LSworkflow lsworkflow, Date createdtimestamp,
			Date completedtimestamp, Lsrepositoriesdata lsrepositoriesdata, Lsrepositories lsrepositories,
			String keyword, Long directorycode, LStestmasterlocal lstestmasterlocal, Integer ordercancell,
			Integer viewoption, Elnmaterial elnmaterial, MaterialInventory materialinventory, String lockedusername,
			Integer activeuser, Integer approved,LSOrdernotification lsordernotification, 
			Integer ordersaved,Boolean repeat,LsAutoregister lsautoregisterorders,Boolean sentforapprovel,
			String approvelaccept,Integer autoregistercount,String sequenceid
//			,
//			List<Lsorderworkflowhistory> lsorderworkflowhistory
	) {
		super(batchcode, batchid, orderflag, approvelstatus, lockeduser, testcode, testname, lssamplemaster,
				lsprojectmaster, lsfile, filetype, lsuserMaster, assignedto, lssamplefile, lsworkflow, createdtimestamp,
				completedtimestamp, lsrepositoriesdata, lsrepositories, keyword, directorycode, lstestmasterlocal,
				ordercancell, viewoption, elnmaterial, materialinventory, approved, lsordernotification, ordersaved,
				repeat, lsautoregisterorders, sentforapprovel, approvelaccept, autoregistercount,sequenceid);

		this.lockedusername = lockedusername;
		this.activeuser = activeuser;
		this.lsfile = lsfile;
		this.approved = approved;
		this.lssamplefile = lssamplefile;
		this.lstestmasterlocal = lstestmasterlocal;
		this.lsworkflow =lsworkflow ;
//		this.lsorderworkflowhistory.addAll(lsorderworkflowhistory);
	}

	public LSworkflow getLsworkflow() {
		return lsworkflow;
	}

	public void setLsworkflow(LSworkflow lsworkflow) {
		this.lsworkflow = lsworkflow;
	}

	public LStestmasterlocal getLstestmasterlocal() {
		return lstestmasterlocal;
	}

	public void setLstestmasterlocal(LStestmasterlocal lstestmasterlocal) {
		this.lstestmasterlocal = lstestmasterlocal;
	}

	public List<Lsorderworkflowhistory> getLsorderworkflowhistory() {
		return lsorderworkflowhistory;
	}

	public void setLsorderworkflowhistory(List<Lsorderworkflowhistory> lsorderworkflowhistory) {
		this.lsorderworkflowhistory = lsorderworkflowhistory;
	}

	public LSsamplefile getLssamplefile() {
		return lssamplefile;
	}

	public void setLssamplefile(LSsamplefile lssamplefile) {
		this.lssamplefile = lssamplefile;
	}

	public Integer getApproved() {
		return approved;
	}

	public void setApproved(Integer approved) {
		this.approved = approved;
	}

	public LSfile getLsfile() {
		return lsfile;
	}

	public void setLsfile(LSfile lsfile) {
		this.lsfile = lsfile;
	}

	public Integer getIsLockbycurrentuser() {
		return isLockbycurrentuser;
	}

	public void setIsLockbycurrentuser(Integer isLockbycurrentuser) {
		this.isLockbycurrentuser = isLockbycurrentuser;
	}

	public Integer getIsFinalStep() {
		return isFinalStep;
	}

	public void setIsFinalStep(Integer isFinalStep) {
		this.isFinalStep = isFinalStep;
	}

	public List<LStestparameter> getLstestparameter() {
		return lstestparameter;
	}

	public void setLstestparameter(List<LStestparameter> lstestparameter) {
		this.lstestparameter = lstestparameter;
	}

	public Integer getIsLock() {
		return isLock;
	}

	public LScentralisedUsers getLscentralisedusers() {
		return lscentralisedusers;
	}

	public void setLscentralisedusers(LScentralisedUsers lscentralisedusers) {
		this.lscentralisedusers = lscentralisedusers;
	}

	public void setIsLock(Integer isLock) {
		this.isLock = isLock;
	}

	public List<LSlogilablimsorder> getLsLSlogilablimsorder() {
		return lsLSlogilablimsorder;
	}

	public void setLsLSlogilablimsorder(List<LSlogilablimsorder> lsLSlogilablimsorder) {
		this.lsLSlogilablimsorder = lsLSlogilablimsorder;
	}

	public List<ELNFileAttachments> getElnfileAttachments() {
		return elnfileAttachments;
	}

	public void setElnfileAttachments(List<ELNFileAttachments> elnfileAttachments) {
		this.elnfileAttachments = elnfileAttachments;
	}
	
	public Integer getActiveuser() {
		return activeuser;
	}

	public void setActiveuser(Integer activeuser) {
		this.activeuser = activeuser;
	}

	public Response getResponse() {
		return response;
	}

	public void setResponse(Response response) {
		this.response = response;
	}

	public String getLockedusername() {
		return lockedusername;
	}

	public void setLockedusername(String lockedusername) {
		this.lockedusername = lockedusername;
	}
	
	public List<Lsbatchdetails> getLsbatchdetails() {
		return lsbatchdetails;
	}

	public void setLsbatchdetails(List<Lsbatchdetails> lsbatchdetails) {
		this.lsbatchdetails = lsbatchdetails;
	}

	public List<LsOrderattachments> getLsOrderattachments() {
		return lsOrderattachments;
	}

	public void setLsOrderattachments(List<LsOrderattachments> lsOrderattachments) {
		this.lsOrderattachments = lsOrderattachments;
	}

}
