package com.agaram.eln.primary.fetchmodel.getorders;

import java.util.Date;

import com.agaram.eln.primary.model.instrumentDetails.LSOrdernotification;
import com.agaram.eln.primary.model.instrumentDetails.LsAutoregister;
import com.agaram.eln.primary.model.masters.Lsrepositories;
import com.agaram.eln.primary.model.masters.Lsrepositoriesdata;
import com.agaram.eln.primary.model.material.Elnmaterial;
import com.agaram.eln.primary.model.material.MaterialInventory;
import com.agaram.eln.primary.model.sheetManipulation.LSfile;
import com.agaram.eln.primary.model.sheetManipulation.LSsamplefile;
import com.agaram.eln.primary.model.sheetManipulation.LSsamplemaster;
import com.agaram.eln.primary.model.sheetManipulation.LStestmasterlocal;
import com.agaram.eln.primary.model.sheetManipulation.LSworkflow;
import com.agaram.eln.primary.model.usermanagement.LSprojectmaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;

public class Logilaborders extends Logilabordermaster {

	private String orderflag;
	private Integer approvelstatus;
	private Integer lockeduser;
	private Integer testcode;
	@SuppressWarnings("unused")
	private String batchid;
	private String keyword;
	private LSsamplemaster lssamplemaster;
	private LSprojectmaster lsprojectmaster;
	private Integer filecode;
	private LSuserMaster lsuserMaster;
	private Integer filetype;
	private LSsamplefile lssamplefile;
	private String repositoryitemname;
	private String materialname;
	private String materialinventoryname;
	private LSuserMaster assignedto;
	private String repositoryname;
	private Long batchcode;
	private Long directorycode;
	private Integer ordercancell;
	private Integer viewoption;
	private LSOrdernotification lsordernotification;
	private Integer ordersaved;
	private Boolean repeat;
	private LsAutoregister lsautoregisterorders;
	private Boolean sentforapprovel;
	private String approvelaccept;
	private Integer autoregistercount;
	
	public Logilaborders(Long batchcode, String batchid, String orderflag, Integer approvelstatus,
			Integer lockeduser, Integer testcode, String testname, LSsamplemaster lssamplemaster,
			LSprojectmaster lsprojectmaster, LSfile lsfile, Integer filetype, LSuserMaster lsuserMaster,LSuserMaster assignedto,
			LSsamplefile lssamplefile, LSworkflow lsworkflow, Date createdtimestamp,Date completedtimestamp,
			Lsrepositoriesdata lsrepositoriesdata,Lsrepositories lsrepositories,String keyword, Long directorycode,LStestmasterlocal lstestmasterlocal,
			Integer ordercancell,Integer viewoption,Elnmaterial elnmaterial,MaterialInventory materialinventory,Integer approved,LSOrdernotification lsordernotification, 
			Integer ordersaved,Boolean repeat,LsAutoregister lsautoregisterorders,Boolean sentforapprovel,String approvelaccept,Integer autoregistercount) {
		
		super(batchcode, batchid, lsworkflow, testname, lsfile, lssamplemaster, lsprojectmaster, filetype, orderflag,assignedto, createdtimestamp,completedtimestamp,keyword,
				lstestmasterlocal, ordercancell,viewoption,lsuserMaster,testcode, approvelstatus,lsordernotification, ordersaved,repeat,lsautoregisterorders,sentforapprovel,approvelaccept,autoregistercount, elnmaterial);
		 
		if(lssamplefile != null)
		{
			LSsamplefile objSampleFile = new LSsamplefile();
			objSampleFile.setFilesamplecode(lssamplefile.getFilesamplecode());
			objSampleFile.setModifieddate(lssamplefile.getModifieddate());
			objSampleFile.setCreatedate(lssamplefile.getCreatedate());
			this.lssamplefile = objSampleFile;
		}
		
		this.orderflag = orderflag;
		this.approvelstatus = approvelstatus;
		this.lockeduser = lockeduser;
		this.testcode = testcode;
		this.lssamplemaster = lssamplemaster;
		this.lsprojectmaster =lsprojectmaster!=null?new LSprojectmaster(lsprojectmaster.getCreatedby(),lsprojectmaster.getProjectcode(),lsprojectmaster.getProjectname(),lsprojectmaster.getProjectstatus(),lsprojectmaster.getStatus(),lsprojectmaster.getTeamname()):lsprojectmaster;
		this.filecode = lsfile != null ? lsfile.getFilecode() : -1;
		this.filetype = filetype;
		this.lsuserMaster = lsuserMaster!=null? new LSuserMaster(lsuserMaster.getUsercode(),lsuserMaster.getUsername(),lsuserMaster.getLssitemaster()):null;
		this.batchid=batchid;
		this.batchcode = batchcode;
		this.keyword=keyword != null ? keyword :"";
		
		this.repositoryitemname =lsrepositoriesdata !=null ?lsrepositoriesdata.getRepositoryitemname():null;
		this.assignedto =assignedto;
		this.repositoryname =lsrepositories !=null ?lsrepositories.getRepositoryname():null;
		this.materialname=elnmaterial!=null?elnmaterial.getSmaterialname():null;
		this.materialinventoryname=materialinventory!=null?materialinventory.getSinventoryid():null;
		this.directorycode = directorycode;
		this.ordercancell=ordercancell;
		this.viewoption=viewoption;
        this.lsordernotification=lsordernotification != null ? lsordernotification :null;
        this.ordersaved = ordersaved;
        this.repeat=repeat != null ? repeat : null;
        this.lsautoregisterorders=lsautoregisterorders != null ? lsautoregisterorders : null;
        this.sentforapprovel=sentforapprovel!=null?sentforapprovel:null;
        this.approvelaccept=approvelaccept!=null?approvelaccept:null;
        this.autoregistercount = autoregistercount != null ? autoregistercount:null;
	}
	
	
	public Integer getViewoption() {
		return viewoption;
	}


	public void setViewoption(Integer viewoption) {
		this.viewoption = viewoption;
	}


	public Integer getOrdercancell() {
		return ordercancell;
	}


	public void setOrdercancell(Integer ordercancell) {
		this.ordercancell = ordercancell;
	}


	public String getRepositoryitemname() {
		return repositoryitemname;
	}

	public LSuserMaster getAssignedto() {
		return assignedto;
	}

	public String getRepositoryname() {
		return repositoryname;
	}

	public void setRepositoryitemname(String repositoryitemname) {
		this.repositoryitemname = repositoryitemname;
	}

	public void setAssignedto(LSuserMaster assignedto) {
		this.assignedto = assignedto;
	}

	public void setRepositoryname(String repositoryname) {
		this.repositoryname = repositoryname;
	}

	public LSuserMaster getLsuserMaster() {
		return lsuserMaster;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Long getBatchcode() {
		return batchcode;
	}

	public void setBatchcode(Long batchcode) {
		this.batchcode = batchcode;
	}

	public void setLsuserMaster(LSuserMaster lsuserMaster) {
		this.lsuserMaster = lsuserMaster;
	}

	
	public Integer getFiletype() {
		return filetype;
	}

	public void setFiletype(Integer filetype) {
		this.filetype = filetype;
	}

	public LSsamplefile getLssamplefile() {
		return lssamplefile;
	}

	public void setLssamplefile(LSsamplefile lssamplefile) {
		this.lssamplefile = lssamplefile;
	}

	public String getOrderflag() {
		return orderflag;
	}

	public void setOrderflag(String orderflag) {
		this.orderflag = orderflag;
	}

	public Integer getApprovelstatus() {
		return approvelstatus;
	}

	public void setApprovelstatus(Integer approvelstatus) {
		this.approvelstatus = approvelstatus;
	}

	public Integer getLockeduser() {
		return lockeduser;
	}

	public void setLockeduser(Integer lockeduser) {
		this.lockeduser = lockeduser;
	}

	public Integer getTestcode() {
		return testcode;
	}

	public void setTestcode(Integer testcode) {
		this.testcode = testcode;
	}

	public LSsamplemaster getLssamplemaster() {
		return lssamplemaster;
	}

	public void setLssamplemaster(LSsamplemaster lssamplemaster) {
		this.lssamplemaster = lssamplemaster;
	}

	public LSprojectmaster getLsprojectmaster() {
		return lsprojectmaster;
	}

	public void setLsprojectmaster(LSprojectmaster lsprojectmaster) {
		this.lsprojectmaster = lsprojectmaster;
	}

	public Integer getFilecode() {
		return filecode;
	}

	public void setFilecode(Integer filecode) {
		this.filecode = filecode;
	}

	public String getBatchid() {
		if(this.batchid != null) {
			return this.batchid;
			}else {
				String Batchid = "ELN" + this.batchcode;

				if (this.filetype == 3) {
					Batchid = "RESEARCH" + this.batchcode;
				} else if (this.filetype == 4) {
					Batchid = "EXCEL" + this.batchcode;
				} else if (this.filetype == 5) {
					Batchid = "VALIDATE" + this.batchcode;
				} else if (this.filetype == 0) {
					Batchid = batchid;
				}
				return Batchid;	
			}
		
	}

	public void setBatchid(String batchid) {
		this.batchid = batchid;
	}

	public Long getDirectorycode() {
		return directorycode;
	}

	public void setDirectorycode(Long directorycode) {
		this.directorycode = directorycode;
	}


	public String getMaterialname() {
		return materialname;
	}


	public void setMaterialname(String materialname) {
		this.materialname = materialname;
	}


	public String getMaterialinventoryname() {
		return materialinventoryname;
	}


	public void setMaterialinventoryname(String materialinventoryname) {
		this.materialinventoryname = materialinventoryname;
	}


	public LSOrdernotification getLsordernotification() {
		return lsordernotification;
	}


	public void setLsordernotification(LSOrdernotification lsordernotification) {
		this.lsordernotification = lsordernotification;
	}

	public Integer getOrdersaved() {
		return ordersaved;
	}


	public void setOrdersaved(Integer ordersaved) {
		this.ordersaved = ordersaved;
	}

	public Boolean getRepeat() {
		return repeat;
	}

	public void setRepeat(Boolean repeat) {
		this.repeat = repeat;
	}


	public LsAutoregister getLsautoregisterorders() {
		return lsautoregisterorders;
	}


	public void setLsautoregisterorders(LsAutoregister lsautoregisterorders) {
		this.lsautoregisterorders = lsautoregisterorders;
	}


	public Boolean getSentforapprovel() {
		return sentforapprovel;
	}


	public void setSentforapprovel(Boolean sentforapprovel) {
		this.sentforapprovel = sentforapprovel;
	}


	public String getApprovelaccept() {
		return approvelaccept;
	}


	public void setApprovelaccept(String approvelaccept) {
		this.approvelaccept = approvelaccept;
	}


	public Integer getAutoregistercount() {
		return autoregistercount;
	}


	public void setAutoregistercount(Integer autoregistercount) {
		this.autoregistercount = autoregistercount;
	}

	
}