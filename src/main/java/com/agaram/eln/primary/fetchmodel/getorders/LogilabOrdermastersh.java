package com.agaram.eln.primary.fetchmodel.getorders;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.agaram.eln.primary.model.instrumentDetails.LSOrdernotification;
import com.agaram.eln.primary.model.instrumentDetails.LsAutoregister;
import com.agaram.eln.primary.model.material.Elnmaterial;
import com.agaram.eln.primary.model.sheetManipulation.LSfile;
import com.agaram.eln.primary.model.sheetManipulation.LSsamplemaster;
import com.agaram.eln.primary.model.sheetManipulation.LStestmasterlocal;
import com.agaram.eln.primary.model.sheetManipulation.LSworkflow;
import com.agaram.eln.primary.model.usermanagement.LSprojectmaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;

public class LogilabOrdermastersh implements Comparable<LogilabOrdermastersh> {
	private Long bc;
	private String bi;
	List<LSworkflow> lw;
	private Integer wc;
	private boolean cp;
	private String tn;
	private String fn;
	private String pn;
	private String sn;
	private Integer ft;
	private String of;
	private Date ct;
	private Date cot;
	private LSworkflow lsw;
	private String kw;
	private Integer oc;
	private Integer fc;
	private LSuserMaster at;
	private Integer vo;
	private LSuserMaster cb;
	private Integer tc;
	private Integer as;
	private LSOrdernotification on;
	private Integer os;
	private Boolean r;
	private LsAutoregister ar;
	private Boolean sfa;
	private String aa;
	private Integer arc;
	private String mn;
	private String loc;

	public LogilabOrdermastersh(Long batchcode, String batchid, LSworkflow lsworkflow, String testname, LSfile lsfile,
			LSsamplemaster lssamplemaster, LSprojectmaster lsprojectmaster, Integer filetype, String orderflag,
			LSuserMaster assignedto, Date createdtimestamp, Date completedtimestamp, String keyword,
			LStestmasterlocal lstestmasterlocal, Integer ordercancell, Integer viewoption, LSuserMaster lsuserMaster,
			Integer testcode, Integer approvelstatus, LSOrdernotification lsordernotification, Integer ordersaved,
			Boolean repeat, LsAutoregister lsautoregisterorders, Boolean sentforapprovel, String approvelaccept,
			Integer autoregistercount, Elnmaterial elnmaterial, String lockedusername) {
		this.bc = batchcode;
		this.bi = batchid;
		this.wc = lsworkflow != null ? lsworkflow.getWorkflowcode() : null;
		this.tn = lstestmasterlocal != null ? lstestmasterlocal.getTestname() : testname;
		this.fn = lsfile != null ? lsfile.getFilenameuser() : null;
		this.pn = lsprojectmaster != null ? lsprojectmaster.getProjectname() : null;
		this.sn = lssamplemaster != null ? lssamplemaster.getSamplename() : null;
		this.ft = filetype;
		this.fc = lsfile != null ? lsfile.getFilecode() : -1;
		this.of = orderflag;
		this.ct = createdtimestamp;
		this.cot = completedtimestamp;
		this.kw = keyword;
		this.lsw = lsworkflow != null ? new LSworkflow(lsworkflow.getWorkflowcode(), lsworkflow.getWorkflowname())
				: null;
		this.oc = ordercancell;
		this.at = assignedto;
		this.tc = testcode;
		this.vo = viewoption;
		this.cb = lsuserMaster;
		this.as = approvelstatus;
		this.on = lsordernotification;
		this.os = ordersaved;
		this.r = repeat;
		this.ar = lsautoregisterorders;
		this.sfa = sentforapprovel;
		this.aa = approvelaccept;
		this.arc = autoregistercount;
		this.mn = elnmaterial != null ? elnmaterial.getSmaterialname() : null;
		this.loc = lockedusername;
	}

	public String getLoc() {
		return loc;
	}

	public void setLoc(String loc) {
		this.loc = loc;
	}

	public Long getBc() {
		return bc;
	}

	public void setBc(Long bc) {
		this.bc = bc;
	}

	public String getBi() {
		if (this.bi != null) {
			return this.bi;
		} else {
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

	public List<LSworkflow> getLw() {
		return lw;
	}

	public void setLw(List<LSworkflow> lstworkflow) {
		if (lstworkflow != null && this.wc != null && lstworkflow.size() > 0) {
			// if(lstworkflow.contains(this.lsworkflow))

			List<Integer> lstworkflowcode = new ArrayList<Integer>();
			if (lstworkflow != null && lstworkflow.size() > 0) {
				lstworkflowcode = lstworkflow.stream().map(LSworkflow::getWorkflowcode).collect(Collectors.toList());

				if (lstworkflowcode.contains(this.wc)) {
					this.setCp(true);
				} else {
					this.setCp(false);
				}
			} else {
				this.setCp(false);
			}
		} else {
			this.setCp(false);
		}
		this.lw = null;
	}

	public Integer getWc() {
		return wc;
	}

	public void setWc(Integer wc) {
		this.wc = wc;
	}

	public boolean isCp() {
		return cp;
	}

	public void setCp(boolean cp) {
		this.cp = cp;
	}

	public String getTn() {
		return tn;
	}

	public void setTn(String tn) {
		this.tn = tn;
	}

	public String getFn() {
		return fn;
	}

	public void setFn(String fn) {
		this.fn = fn;
	}

	public String getPn() {
		return pn;
	}

	public void setPn(String pn) {
		this.pn = pn;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public Integer getFt() {
		return ft;
	}

	public void setFt(Integer ft) {
		this.ft = ft;
	}

	public String getOf() {
		return of;
	}

	public void setOf(String of) {
		this.of = of;
	}

	public Date getCt() {
		return ct;
	}

	public void setCt(Date ct) {
		this.ct = ct;
	}

	public Date getCot() {
		return cot;
	}

	public void setCot(Date cot) {
		this.cot = cot;
	}

	public LSworkflow getLsw() {
		return lsw;
	}

	public void setLsw(LSworkflow lsw) {
		this.lsw = lsw;
	}

	public String getKw() {
		return kw;
	}

	public void setKw(String kw) {
		this.kw = kw;
	}

	public Integer getOc() {
		return oc;
	}

	public void setOc(Integer oc) {
		this.oc = oc;
	}

	public Integer getFc() {
		return fc;
	}

	public void setFc(Integer fc) {
		this.fc = fc;
	}

	public LSuserMaster getAt() {
		return at;
	}

	public void setAt(LSuserMaster at) {
		this.at = at;
	}

	public Integer getVo() {
		return vo;
	}

	public void setVo(Integer vo) {
		this.vo = vo;
	}

	public LSuserMaster getCb() {
		return cb;
	}

	public void setCb(LSuserMaster cb) {
		this.cb = cb;
	}

	public Integer getTc() {
		return tc;
	}

	public void setTc(Integer tc) {
		this.tc = tc;
	}

	public Integer getAs() {
		return as;
	}

	public void setAs(Integer as) {
		this.as = as;
	}

	public LSOrdernotification getOn() {
		return on;
	}

	public void setOn(LSOrdernotification on) {
		this.on = on;
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

	public LsAutoregister getAr() {
		return ar;
	}

	public void setAr(LsAutoregister ar) {
		this.ar = ar;
	}

	public Boolean getSfa() {
		return sfa;
	}

	public void setSfa(Boolean sfa) {
		this.sfa = sfa;
	}

	public String getAa() {
		return aa;
	}

	public void setAa(String aa) {
		this.aa = aa;
	}

	public Integer getArc() {
		return arc;
	}

	public void setArc(Integer arc) {
		this.arc = arc;
	}

	public String getMn() {
		return mn;
	}

	public void setMn(String mn) {
		this.mn = mn;
	}

	@Override
	public int compareTo(LogilabOrdermastersh o) {
		return this.getBc().compareTo(o.getBc());
	}
}
