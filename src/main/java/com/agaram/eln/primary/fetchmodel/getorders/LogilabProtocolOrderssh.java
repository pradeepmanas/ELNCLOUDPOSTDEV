package com.agaram.eln.primary.fetchmodel.getorders;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.agaram.eln.primary.model.instrumentDetails.LSOrdernotification;
import com.agaram.eln.primary.model.instrumentDetails.LsAutoregister;
import com.agaram.eln.primary.model.masters.Lsrepositories;
import com.agaram.eln.primary.model.masters.Lsrepositoriesdata;
import com.agaram.eln.primary.model.material.Elnmaterial;
import com.agaram.eln.primary.model.material.ElnmaterialInventory;
import com.agaram.eln.primary.model.protocols.Elnprotocolworkflow;
import com.agaram.eln.primary.model.protocols.LSprotocolmaster;
import com.agaram.eln.primary.model.protocols.LSprotocolworkflow;
import com.agaram.eln.primary.model.sheetManipulation.LSsamplemaster;
import com.agaram.eln.primary.model.sheetManipulation.LStestmasterlocal;
import com.agaram.eln.primary.model.usermanagement.LSprojectmaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;

public class LogilabProtocolOrderssh implements Comparable<LogilabProtocolOrderssh> {

	private Long pc;
	private String pn;

	private String of;
	private Integer pt;

	private Date ct;
	private Date cot;

	private String sn;
	private LSprotocolmaster lpm;
	private String pjn;
	private String pmn;
	private LSprojectmaster lpjm;

	private String kw;
	private Integer tc;
	private String tn;
	private Long dc;
	private Integer cb;

	private String rin;
	private LSuserMaster at;
	private String rn;
	private Integer a;
	private Integer r;
	private Integer vo;

	private String mn;
	private String min;
	private LSprotocolworkflow lpw;
	private Integer wc;
	private boolean cp;
	private LSsamplemaster lsm;
	private Integer oc;
	private Integer os;
	private LSuserMaster osb;
	private Date oso;
	private Integer lu;
	private String lun;
	private Integer vn;
	List<Elnprotocolworkflow> lsepw;
	private Elnprotocolworkflow lepw;
	private LSOrdernotification lon;
	private LsAutoregister lar;
	private Boolean re;
	private Boolean sfa;
	private String aa;
	private Integer arc;
	private LSuserMaster lum;
	private Long ase;
	private Long sse;
	private Long pse;
	private Long tse;
	private Long ose;
	private String sid;
	
	public LogilabProtocolOrderssh(Long protocolordercode, Integer Testcode, String protoclordername, String orderflag,
			Integer protocoltype, Date createdtimestamp, Date completedtimestamp, LSprotocolmaster lsprotocolmaster,
			LSprotocolworkflow lSprotocolworkflow, LSsamplemaster lssamplemaster, LSprojectmaster lsprojectmaster,
			String keyword, Long directorycode, Integer createby, LSuserMaster assignedto,
			Lsrepositoriesdata lsrepositoriesdata, Lsrepositories lsrepositories,
			Elnmaterial elnmaterial,ElnmaterialInventory elnmaterialinventory, Integer approved, Integer rejected,
			Integer ordercancell, Integer viewoption, Integer orderstarted, LSuserMaster orderstartedby,
			Date orderstartedon,Integer lockeduser,String lockedusername, Integer versionno,Elnprotocolworkflow elnprotocolworkflow,
			LSOrdernotification lsordernotification,LsAutoregister lsautoregister,Boolean repeat,
			Boolean sentforapprovel,String approvelaccept,Integer autoregistercount, LSuserMaster lsuserMaster, LStestmasterlocal lstestmasterlocal, 
			Long applicationsequence, Long sitesequence, Long projectsequence, Long tasksequence, Long ordertypesequence,String sequenceid) {

		this.pc = protocolordercode;
		this.tc = Testcode;
		this.pn = protoclordername;
		this.of = orderflag;
		this.wc = elnprotocolworkflow != null ? elnprotocolworkflow.getWorkflowcode() : null;
		this.pt = protocoltype;
		this.ct = createdtimestamp;
		this.cot = completedtimestamp;
		this.pmn = lsprotocolmaster != null ? lsprotocolmaster.getProtocolmastername() : "";
		this.sn = lssamplemaster != null ? lssamplemaster.getSamplename() : "";
		this.pjn = lsprojectmaster != null ? lsprojectmaster.getProjectname() : "";
		this.tn = lstestmasterlocal != null ? lstestmasterlocal.getTestname() : "";
		this.kw = keyword;
		this.dc = directorycode;
		this.lpjm = lsprojectmaster;
		this.cb = createby;
		this.at = assignedto;
		this.lsm = lssamplemaster != null ? lssamplemaster : null;
		this.rin = lsrepositoriesdata != null ? lsrepositoriesdata.getRepositoryitemname() : null;
		this.rn = lsrepositories != null ? lsrepositories.getRepositoryname() : null;
		this.mn = elnmaterial != null ? elnmaterial.getSmaterialname() : null;
		this.min = elnmaterialinventory != null ? elnmaterialinventory.getSinventoryid() : null;
		this.dc = directorycode;
		this.lpm = lsprotocolmaster;
		this.a = approved;
		this.r = rejected;
		this.oc = ordercancell;
		this.vo = viewoption;
		this.os = orderstarted != null && orderstarted == 1 ? orderstarted : 0;
		this.osb = orderstartedby != null ? orderstartedby : null;
		this.oso = orderstartedon != null ? orderstartedon : null;
		this.lu=lockeduser!=null?lockeduser:null;
		this.lun=lockedusername!=null?lockedusername:null;
		this.vn = versionno;
		this.lepw=elnprotocolworkflow;
        this.lon=lsordernotification != null ? lsordernotification :null;
        this.lar=lsautoregister != null ? lsautoregister :null;
        this.re = repeat;
		this.sfa=sentforapprovel;
		this.aa=approvelaccept;
		this.arc=autoregistercount;
		this.lum = lsuserMaster!=null? new LSuserMaster(lsuserMaster.getUsercode(),lsuserMaster.getUsername(),lsuserMaster.getLssitemaster()):null;
		this.ase = applicationsequence;
		this.sse = sitesequence;
		this.pse = projectsequence;
		this.tse = tasksequence;
		this.ose = ordertypesequence;
		this.sid = sequenceid;
	}
	
	@Override
	public int compareTo(LogilabProtocolOrderssh o) {
		return this.getPc().compareTo(o.getPc());
	}
	
	public String getTn() {
		return tn;
	}

	public void setTn(String tn) {
		this.tn = tn;
	}

	public Long getPc() {
		return pc;
	}

	public void setPc(Long pc) {
		this.pc = pc;
	}

	public String getPn() {
		return pn;
	}

	public void setPn(String pn) {
		this.pn = pn;
	}

	public String getOf() {
		return of;
	}

	public void setOf(String of) {
		this.of = of;
	}

	public Integer getPt() {
		return pt;
	}

	public void setPt(Integer pt) {
		this.pt = pt;
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

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public LSprotocolmaster getLpm() {
		return lpm;
	}

	public void setLpm(LSprotocolmaster lpm) {
		this.lpm = lpm;
	}

	public String getPjn() {
		return pjn;
	}

	public void setPjn(String pjn) {
		this.pjn = pjn;
	}

	public String getPmn() {
		return pmn;
	}

	public void setPmn(String pmn) {
		this.pmn = pmn;
	}

	public LSprojectmaster getLpjm() {
		return lpjm;
	}

	public void setLpjm(LSprojectmaster lpjm) {
		this.lpjm = lpjm;
	}

	public String getKw() {
		return kw;
	}

	public void setKw(String kw) {
		this.kw = kw;
	}

	public Integer getTc() {
		return tc;
	}

	public void setTc(Integer tc) {
		this.tc = tc;
	}

	public Long getDc() {
		return dc;
	}

	public void setDc(Long dc) {
		this.dc = dc;
	}

	public Integer getCb() {
		return cb;
	}

	public void setCb(Integer cb) {
		this.cb = cb;
	}

	public String getRin() {
		return rin;
	}

	public void setRin(String rin) {
		this.rin = rin;
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

	public Integer getA() {
		return a;
	}

	public void setA(Integer a) {
		this.a = a;
	}

	public Integer getR() {
		return r;
	}

	public void setR(Integer r) {
		this.r = r;
	}

	public Integer getVo() {
		return vo;
	}

	public void setVo(Integer vo) {
		this.vo = vo;
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

	public LSprotocolworkflow getLpw() {
		return lpw;
	}

	public void setLpw(LSprotocolworkflow lpw) {
		this.lpw = lpw;
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

	public LSsamplemaster getLsm() {
		return lsm;
	}

	public void setLsm(LSsamplemaster lsm) {
		this.lsm = lsm;
	}

	public Integer getOc() {
		return oc;
	}

	public void setOc(Integer oc) {
		this.oc = oc;
	}

	public Integer getOs() {
		return os;
	}

	public void setOs(Integer os) {
		this.os = os;
	}

	public LSuserMaster getOsb() {
		return osb;
	}

	public void setOsb(LSuserMaster osb) {
		this.osb = osb;
	}

	public Date getOso() {
		return oso;
	}

	public void setOso(Date oso) {
		this.oso = oso;
	}

	public Integer getLu() {
		return lu;
	}

	public void setLu(Integer lu) {
		this.lu = lu;
	}

	public String getLun() {
		return lun;
	}

	public void setLun(String lun) {
		this.lun = lun;
	}

	public Integer getVn() {
		return vn;
	}

	public void setVn(Integer vn) {
		this.vn = vn;
	}

	public List<Elnprotocolworkflow> getLsepw() {
		return lsepw;
	}

	public void setLsepw(List<Elnprotocolworkflow> lsepw) {
		if (lsepw != null && this.wc != null && lsepw.size() > 0) {

			List<Integer> lstworkflowcode = new ArrayList<Integer>();
			if (lsepw != null && lsepw.size() > 0) {
				lstworkflowcode = lsepw.stream().map(Elnprotocolworkflow::getWorkflowcode).collect(Collectors.toList());

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
		this.lsepw = null;
	}

	public Elnprotocolworkflow getLepw() {
		return lepw;
	}

	public void setLepw(Elnprotocolworkflow lepw) {
		this.lepw = lepw;
	}

	public LSOrdernotification getLon() {
		return lon;
	}

	public void setLon(LSOrdernotification lon) {
		this.lon = lon;
	}

	public LsAutoregister getLar() {
		return lar;
	}

	public void setLar(LsAutoregister lar) {
		this.lar = lar;
	}

	public Boolean getRe() {
		return re;
	}

	public void setRe(Boolean re) {
		this.re = re;
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

	public LSuserMaster getLum() {
		return lum;
	}

	public void setLum(LSuserMaster lum) {
		this.lum = lum;
	}

	public Long getAse() {
		return ase;
	}

	public void setAse(Long ase) {
		this.ase = ase;
	}

	public Long getSse() {
		return sse;
	}

	public void setSse(Long sse) {
		this.sse = sse;
	}

	public Long getPse() {
		return pse;
	}

	public void setPse(Long pse) {
		this.pse = pse;
	}

	public Long getTse() {
		return tse;
	}

	public void setTse(Long tse) {
		this.tse = tse;
	}

	public Long getOse() {
		return ose;
	}

	public void setOse(Long ose) {
		this.ose = ose;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	
}
