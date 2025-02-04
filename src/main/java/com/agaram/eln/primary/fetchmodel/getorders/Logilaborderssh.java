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

public class Logilaborderssh extends LogilabOrdermastersh {

	private String of;
	private Integer as;
	private Integer lu;
	private Integer tc;
	@SuppressWarnings("unused")
	private String bi;
	private String kw;
	private LSsamplemaster lsm;
	private LSprojectmaster lpm;
	private Integer fc;
	private LSuserMaster lum;
	private Integer ft;
	private LSsamplefile lsf;
	private String rin;
	private String mn;
	private String min;
	private LSuserMaster at;
	private String rn;
	private Long bc;
	private Long dc;
	private Integer oc;
	private Integer vo;
	private LSOrdernotification lon;
	private Integer os;
	private Boolean r;
	private LsAutoregister laro;
	private Boolean sfa;
	private String ac;
	private Integer arc;
	private Boolean ts;
	
	public Logilaborderssh(Long batchcode, String batchid, String orderflag, Integer approvelstatus,
			Integer lockeduser, Integer testcode, String testname, LSsamplemaster lssamplemaster,
			LSprojectmaster lsprojectmaster, LSfile lsfile, Integer filetype, LSuserMaster lsuserMaster,LSuserMaster assignedto,
			LSsamplefile lssamplefile, LSworkflow lsworkflow, Date createdtimestamp,Date completedtimestamp,
			Lsrepositoriesdata lsrepositoriesdata,Lsrepositories lsrepositories,String keyword, Long directorycode,LStestmasterlocal lstestmasterlocal,
			Integer ordercancell,Integer viewoption,Elnmaterial elnmaterial,MaterialInventory materialinventory,Integer approved,LSOrdernotification lsordernotification, 
			Integer ordersaved,Boolean repeat,LsAutoregister lsautoregisterorders,Boolean sentforapprovel,String approvelaccept,Integer autoregistercount, String lockedusername, Long applicationsequence,
			Long sitesequence, Long projectsequence, Long tasksequence, Long ordertypesequence, String sequenceid,Boolean teamselected) {
		
		super(batchcode, batchid, lsworkflow, testname, lsfile, lssamplemaster, lsprojectmaster, filetype, orderflag,assignedto, createdtimestamp,completedtimestamp,keyword,
				lstestmasterlocal, ordercancell,viewoption,lsuserMaster,testcode, approvelstatus,lsordernotification, ordersaved,repeat,lsautoregisterorders,sentforapprovel,
				approvelaccept,autoregistercount, elnmaterial,lockedusername,applicationsequence,sitesequence,projectsequence,tasksequence,ordertypesequence,sequenceid,teamselected);
		 
		if(lssamplefile != null)
		{
			LSsamplefile objSampleFile = new LSsamplefile();
			objSampleFile.setFilesamplecode(lssamplefile.getFilesamplecode());
			objSampleFile.setModifieddate(lssamplefile.getModifieddate());
			objSampleFile.setCreatedate(lssamplefile.getCreatedate());
			this.lsf = objSampleFile;
		}
		
		this.of = orderflag;
		this.as = approvelstatus;
		this.lu = lockeduser;
		this.tc = testcode;
		this.lsm = lssamplemaster;
		this.lpm =lsprojectmaster!=null?new LSprojectmaster(lsprojectmaster.getCreatedby(),lsprojectmaster.getProjectcode(),lsprojectmaster.getProjectname(),lsprojectmaster.getProjectstatus(),lsprojectmaster.getStatus(),lsprojectmaster.getTeamname(), lsprojectmaster.getProjectid()):lsprojectmaster;
		this.fc = lsfile != null ? lsfile.getFilecode() : -1;
		this.ft = filetype;
		this.lum = lsuserMaster!=null? new LSuserMaster(lsuserMaster.getUsercode(),lsuserMaster.getUsername(),lsuserMaster.getLssitemaster()):null;
		this.bi=batchid;
		this.bc = batchcode;
		this.kw=keyword != null ? keyword :"";
		
		this.rin =lsrepositoriesdata !=null ?lsrepositoriesdata.getRepositoryitemname():null;
		this.at =assignedto;
		this.rn =lsrepositories !=null ?lsrepositories.getRepositoryname():null;
		this.mn=elnmaterial!=null?elnmaterial.getSmaterialname():null;
		this.min=materialinventory!=null?materialinventory.getSinventoryid():null;
		this.dc = directorycode;
		this.oc=ordercancell;
		this.vo=viewoption;
        this.lon=lsordernotification != null ? lsordernotification :null;
        this.os = ordersaved;
        this.r=repeat != null ? repeat : null;
        this.laro=lsautoregisterorders != null ? lsautoregisterorders : null;
        this.sfa=sentforapprovel!=null?sentforapprovel:null;
        this.ac=approvelaccept!=null?approvelaccept:null;
        this.arc = autoregistercount != null ? autoregistercount:null;
        this.ts = teamselected;
	}

	public String getOf() {
		return of;
	}

	public void setOf(String of) {
		this.of = of;
	}

	public Integer getAs() {
		return as;
	}

	public void setAs(Integer as) {
		this.as = as;
	}

	public Integer getLu() {
		return lu;
	}

	public void setLu(Integer lu) {
		this.lu = lu;
	}

	public Integer getTc() {
		return tc;
	}

	public void setTc(Integer tc) {
		this.tc = tc;
	}

	public String getBi() {
		if(this.bi != null) {
			return this.bi;
			}else {
				String Batchid = "ELN" + this.bc;

				if (this.ft == 3) {
					Batchid = "RESEARCH" + this.bc;
				} else if (this.ft == 4) {
					Batchid = "EXCEL" + this.bc;
				} else if (this.ft == 5) {
					Batchid = "VALIDATE" + this.bc;
				} else if (this.ft == 0) {
					Batchid = bi;
				}
				return Batchid;	
			}
	}

	public void setBi(String bi) {
		this.bi = bi;
	}

	public String getKw() {
		return kw;
	}

	public void setKw(String kw) {
		this.kw = kw;
	}

	public LSsamplemaster getLsm() {
		return lsm;
	}

	public void setLsm(LSsamplemaster lsm) {
		this.lsm = lsm;
	}

	public LSprojectmaster getLpm() {
		return lpm;
	}

	public void setLpm(LSprojectmaster lpm) {
		this.lpm = lpm;
	}

	public Integer getFc() {
		return fc;
	}

	public void setFc(Integer fc) {
		this.fc = fc;
	}

	public LSuserMaster getLum() {
		return lum;
	}

	public void setLum(LSuserMaster lum) {
		this.lum = lum;
	}

	public Integer getFt() {
		return ft;
	}

	public void setFt(Integer ft) {
		this.ft = ft;
	}

	public LSsamplefile getLsf() {
		return lsf;
	}

	public void setLsf(LSsamplefile lsf) {
		this.lsf = lsf;
	}

	public String getRin() {
		return rin;
	}

	public void setRin(String rin) {
		this.rin = rin;
	}

	public String getMn() {
		return mn;
	}

	public void setMn(String mn) {
		this.mn = mn;
	}

	public String getMin() {
		return min;
	}

	public void setMin(String min) {
		this.min = min;
	}

	public LSuserMaster getAt() {
		return at;
	}

	public void setAt(LSuserMaster at) {
		this.at = at;
	}

	public String getRn() {
		return rn;
	}

	public void setRn(String rn) {
		this.rn = rn;
	}

	public Long getBc() {
		return bc;
	}

	public void setBc(Long bc) {
		this.bc = bc;
	}

	public Long getDc() {
		return dc;
	}

	public void setDc(Long dc) {
		this.dc = dc;
	}

	public Integer getOc() {
		return oc;
	}

	public void setOc(Integer oc) {
		this.oc = oc;
	}

	public Integer getVo() {
		return vo;
	}

	public void setVo(Integer vo) {
		this.vo = vo;
	}

	public LSOrdernotification getLon() {
		return lon;
	}

	public void setLon(LSOrdernotification lon) {
		this.lon = lon;
	}

	public Integer getOs() {
		return os;
	}

	public void setOs(Integer os) {
		this.os = os;
	}

	public Boolean getR() {
		return r;
	}

	public void setR(Boolean r) {
		this.r = r;
	}

	public LsAutoregister getLaro() {
		return laro;
	}

	public void setLaro(LsAutoregister laro) {
		this.laro = laro;
	}

	public Boolean getSfa() {
		return sfa;
	}

	public void setSfa(Boolean sfa) {
		this.sfa = sfa;
	}

	public String getAc() {
		return ac;
	}

	public void setAc(String ac) {
		this.ac = ac;
	}

	public Integer getArc() {
		return arc;
	}

	public void setArc(Integer arc) {
		this.arc = arc;
	}

	public Boolean getTs() {
		return ts;
	}

	public void setTs(Boolean ts) {
		this.ts = ts;
	}
	
	
}
