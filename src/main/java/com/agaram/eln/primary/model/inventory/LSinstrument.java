package com.agaram.eln.primary.model.inventory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name = "LSinstrument")
public class LSinstrument {
	@Id
	private Integer instrumentcode;
	private Integer instrumentcatcode;
	private Integer masterauditcode;
	private Integer windowsperiodminus;
	private Integer windowsperiodplus;
	private Integer windowsperiodminusunit;
	private Integer windowsperiodplusunit;
	private Integer servicecode;
	private Integer instrumentstatus;
	private Integer suppliercode;
	private Integer status;
	private Integer usercode;
	@Column(name="manufcode")
	private Integer manufaccode;
	private Integer defaultstatus;
	@Column(name="calibrationstatus")
	private Integer calibrationstatuscode;
	@Column(name="maintenancestatus")
	private Integer maintenancestatuscode;
	private Integer sitecode;
	@Column(name="validationstatus")
	private Integer validationstatuscode;
	
	//@Column(columnDefinition = "nvarchar(100)")
	 private String instrumentid;
	
	//@Column(columnDefinition = "nvarchar(100)")
	private String instrumentname;
	
	//@Column(columnDefinition = "nvarchar(100)")
	private String instrumentcatname;
	
	//@Column(columnDefinition = "nvarchar(max)")
	private String description;
	
	//@Column(columnDefinition = "nvarchar(100)")
	private String modelnumber;
	
	//@Column(columnDefinition = "datetime")
	private String manufacdate;
	
	//@Column(columnDefinition = "datetime")
	private String podate;
	
	//@Column(columnDefinition = "datetime")
	private String receiveddate;
	
	//@Column(columnDefinition = "datetime")
	private String installationdate;
	
	//@Column(columnDefinition = "datetime")
	private String expirydate;

	//@Column(columnDefinition = "nvarchar(100)")
	private String serialno;
	
	//@Column(columnDefinition = "nvarchar(max)")
	private String remarks;
	
	//@Column(columnDefinition = "datetime")
	private String maintenanceduedate;
	
	//@Column(columnDefinition = "datetime")
	private String calibrationduedate;
	
	//@Column(columnDefinition = "datetime")
	private String validationdate;
	
	@Column(columnDefinition = "varchar(100)")
	private String manufname;
	
	public String getManufname() {
		return manufname;
	}
	public void setManufname(String manufname) {
		this.manufname = manufname;
	}
	public Integer getInstrumentcode() {
		return instrumentcode;
	}
	public void setInstrumentcode(Integer instrumentcode) {
		this.instrumentcode = instrumentcode;
	}
	public Integer getInstrumentcatcode() {
		return instrumentcatcode;
	}
	public void setInstrumentcatcode(Integer instrumentcatcode) {
		this.instrumentcatcode = instrumentcatcode;
	}
	public String getInstrumentid() {
		return instrumentid;
	}
	public void setInstrumentid(String instrumentid) {
		this.instrumentid = instrumentid;
	}
	public String getInstrumentname() {
		return instrumentname;
	}
	public void setInstrumentname(String instrumentname) {
		this.instrumentname = instrumentname;
	}
	public String getInstrumentcatname() {
		return instrumentcatname;
	}
	public Integer getSitecode() {
		return sitecode;
	}
	public void setSitecode(Integer sitecode) {
		this.sitecode = sitecode;
	}
	public void setInstrumentcatname(String instrumentcatname) {
		this.instrumentcatname = instrumentcatname;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getModelnumber() {
		return modelnumber;
	}
	public void setModelnumber(String modelnumber) {
		this.modelnumber = modelnumber;
	}
	public Integer getSuppliercode() {
		return suppliercode;
	}
	public void setSuppliercode(Integer suppliercode) {
		this.suppliercode = suppliercode;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getManufacdate() {
		return manufacdate;
	}
	public void setManufacdate(String manufacdate) {
		this.manufacdate = manufacdate;
	}
	public String getPodate() {
		return podate;
	}
	public void setPodate(String podate) {
		this.podate = podate;
	}
	public String getReceiveddate() {
		return receiveddate;
	}
	public void setReceiveddate(String receiveddate) {
		this.receiveddate = receiveddate;
	}
	public String getInstallationdate() {
		return installationdate;
	}
	public void setInstallationdate(String installationdate) {
		this.installationdate = installationdate;
	}
	public String getExpirydate() {
		return expirydate;
	}
	public void setExpirydate(String expirydate) {
		this.expirydate = expirydate;
	}
	public Integer getMasterauditcode() {
		return masterauditcode;
	}
	public void setMasterauditcode(Integer masterauditcode) {
		this.masterauditcode = masterauditcode;
	}
	public Integer getWindowsperiodminus() {
		return windowsperiodminus;
	}
	public void setWindowsperiodminus(Integer windowsperiodminus) {
		this.windowsperiodminus = windowsperiodminus;
	}
	public Integer getWindowsperiodplus() {
		return windowsperiodplus;
	}
	public void setWindowsperiodplus(Integer windowsperiodplus) {
		this.windowsperiodplus = windowsperiodplus;
	}
	public Integer getWindowsperiodminusunit() {
		return windowsperiodminusunit;
	}
	public void setWindowsperiodminusunit(Integer windowsperiodminusunit) {
		this.windowsperiodminusunit = windowsperiodminusunit;
	}
	public Integer getWindowsperiodplusunit() {
		return windowsperiodplusunit;
	}
	public void setWindowsperiodplusunit(Integer windowsperiodplusunit) {
		this.windowsperiodplusunit = windowsperiodplusunit;
	}
	public Integer getServicecode() {
		return servicecode;
	}
	public void setServicecode(Integer servicecode) {
		this.servicecode = servicecode;
	}
	public Integer getInstrumentstatus() {
		return instrumentstatus;
	}
	public void setInstrumentstatus(Integer instrumentstatus) {
		this.instrumentstatus = instrumentstatus;
	}
	public String getSerialno() {
		return serialno;
	}
	public void setSerialno(String serialno) {
		this.serialno = serialno;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public Integer getUsercode() {
		return usercode;
	}
	public void setUsercode(Integer usercode) {
		this.usercode = usercode;
	}
	public String getValidationdate() {
		return validationdate;
	}
	public void setValidationdate(String validationdate) {
		this.validationdate = validationdate;
	}
	public Integer getManufaccode() {
		return manufaccode;
	}
	public void setManufaccode(Integer manufaccode) {
		this.manufaccode = manufaccode;
	}
	public Integer getDefaultstatus() {
		return defaultstatus;
	}
	public void setDefaultstatus(Integer defaultstatus) {
		this.defaultstatus = defaultstatus;
	}
	public Integer getCalibrationstatuscode() {
		if(this.calibrationstatuscode == 64)
		{
			return 1;
		}
		else
		{
			return 0;
		}
	}
	public void setCalibrationstatuscode(Integer calibrationstatuscode) {
		this.calibrationstatuscode = calibrationstatuscode;
	}
	public Integer getMaintenancestatuscode() {
		if(this.maintenancestatuscode == 66)
		{
			return 1;
		}
		else
		{
			return 0;
		}
	}
	public void setMaintenancestatuscode(Integer maintenancestatuscode) {
		this.maintenancestatuscode = maintenancestatuscode;
	}
	public Integer getValidationstatuscode() {
		if(this.validationstatuscode == 62)
		{
			return 1;
		}
		else
		{
			return 0;
		}
	}
	public void setValidationstatuscode(Integer validationstatuscode) {
		this.validationstatuscode = validationstatuscode;
	}
	public String getMaintenanceduedate() {
		return maintenanceduedate;
	}
	public void setMaintenanceduedate(String maintenanceduedate) {
		this.maintenanceduedate = maintenanceduedate;
	}
	public String getCalibrationduedate() {
		return calibrationduedate;
	}
	public void setCalibrationduedate(String calibrationduedate) {
		this.calibrationduedate = calibrationduedate;
	}
}
