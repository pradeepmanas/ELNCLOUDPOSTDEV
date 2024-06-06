package com.agaram.eln.primary.service.starterRunner;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

import java.util.stream.Collectors;

import java.util.concurrent.ConcurrentMap;


import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.agaram.eln.primary.commonfunction.commonfunction;
import com.agaram.eln.primary.config.TenantContext;
import com.agaram.eln.primary.fetchtenantsource.Datasourcemaster;
import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.cloudFileManip.CloudSheetCreation;
import com.agaram.eln.primary.model.cloudProtocol.CloudLsLogilabprotocolstepInfo;
import com.agaram.eln.primary.model.cloudProtocol.LSprotocolstepInformation;
import com.agaram.eln.primary.model.dashboard.LsActiveWidgets;
import com.agaram.eln.primary.model.general.SheetCreation;
import com.agaram.eln.primary.model.instrumentDetails.LSOrdernotification;
import com.agaram.eln.primary.model.instrumentDetails.LSlogilablimsorderdetail;
import com.agaram.eln.primary.model.instrumentDetails.LsAutoregister;
import com.agaram.eln.primary.model.masters.Lsrepositories;
import com.agaram.eln.primary.model.masters.Lsrepositoriesdata;
import com.agaram.eln.primary.model.material.Elnmaterial;
import com.agaram.eln.primary.model.material.ElnmaterialInventory;
import com.agaram.eln.primary.model.material.Material;
import com.agaram.eln.primary.model.material.MaterialInventory;
import com.agaram.eln.primary.model.protocols.ElnprotocolTemplateworkflow;
import com.agaram.eln.primary.model.protocols.Elnprotocolworkflow;
import com.agaram.eln.primary.model.protocols.LSlogilabprotocoldetail;
import com.agaram.eln.primary.model.protocols.LSlogilabprotocolsteps;
import com.agaram.eln.primary.model.protocols.LSprotocolfiles;
import com.agaram.eln.primary.model.protocols.LSprotocolimages;
import com.agaram.eln.primary.model.protocols.LSprotocolmaster;
import com.agaram.eln.primary.model.protocols.LSprotocolordersampleupdates;
import com.agaram.eln.primary.model.protocols.LSprotocolsampleupdates;
import com.agaram.eln.primary.model.protocols.LSprotocolstep;
import com.agaram.eln.primary.model.protocols.LSprotocolstepInfo;
import com.agaram.eln.primary.model.protocols.LSprotocolvideos;
import com.agaram.eln.primary.model.protocols.LSprotocolworkflow;
import com.agaram.eln.primary.model.protocols.LsLogilabprotocolstepInfo;
import com.agaram.eln.primary.model.sheetManipulation.LSfile;
import com.agaram.eln.primary.model.sheetManipulation.LSsamplefile;
import com.agaram.eln.primary.model.sheetManipulation.LSsamplemaster;
import com.agaram.eln.primary.model.sheetManipulation.LSsheetworkflow;
import com.agaram.eln.primary.model.sheetManipulation.LStestmasterlocal;
import com.agaram.eln.primary.model.sheetManipulation.LSworkflow;
import com.agaram.eln.primary.model.sheetManipulation.Notification;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.usermanagement.LSprojectmaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.model.usermanagement.LoggedUser;
import com.agaram.eln.primary.repository.multitenant.DataSourceConfigRepository;
import com.agaram.eln.primary.service.cloudFileManip.CloudFileManipulationservice;
import com.aspose.pdf.internal.eps.postscript.l0f;
import com.google.gson.Gson;
import com.mongodb.gridfs.GridFSDBFile;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.data.mongodb.core.MongoTemplate;



@Service
public class StarterRunner {
	
	@Autowired
    private DataSourceConfigRepository configRepo;
	
	@Autowired
	private CloudFileManipulationservice objCloudFileManipulationservice;

	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	private GridFsTemplate gridFsTemplate;
	
   // private Map<Integer, TimerTask> scheduledTasks = new ConcurrentHashMap<>();
    private final ConcurrentMap<Integer, TimerTask> scheduledTasks = new ConcurrentHashMap<>();

    private static final int MAX_POOL_SIZE = 10;
    private static final int MIN_IDLE = 5;
    private static final int CONNECTION_TIMEOUT = 120000;
    private static final int CONNECTION_THRESHOLD = 120000;

    public void executeOnStartup() throws SQLException, IOException {
        System.out.println("Task executed on startup");
        checkAndScheduleReminders();
        checkAndScheduleRemindersforOrders();
        checkAndScheduleautoOrderRegister();
        checkAndScheduleProtocolautoRegister();      
    }
    public LsActiveWidgets mapResultSetToLsactivewidgets (ResultSet rs)throws SQLException{
    	LsActiveWidgets lsactivewidgets = new LsActiveWidgets();
    	lsactivewidgets.setActivewidgetsdetails(rs.getString("activewidgetsdetails"));
    	lsactivewidgets.setActivedatatimestamp(rs.getTimestamp("activedatatimestamp"));
    	lsactivewidgets.setActivewidgetscode(rs.getInt("activewidgetscode"));
    	lsactivewidgets.setScreenname(rs.getString("screenname"));
    	return lsactivewidgets;
    }
    
    public LSlogilabprotocoldetail mapResultSetToOrderLSlogilabprotocoldetail(ResultSet rs) throws SQLException {
    	LSlogilabprotocoldetail ordernot = new LSlogilabprotocoldetail();
       
    	ordernot.setProtoclordername(rs.getString("Protocolordername"));
    	ordernot.setProtocolordercode(rs.getLong("protocolordercode"));
    	ordernot.setTestcode(rs.getInt("testcode"));
    	ordernot.setCompletedtimestamp(rs.getTimestamp("completedtimestamp"));
    	ordernot.setCreatedtimestamp(rs.getTimestamp("createdtimestamp"));
    	ordernot.setKeyword(rs.getString("keyword"));
        ordernot.setProtocoltype(rs.getInt("protocoltype"));
        
        LSprojectmaster lsprojectmaster = new LSprojectmaster();
    	lsprojectmaster.setProjectcode(rs.getInt("lsprojectmaster_projectcode"));
    	ordernot.setLsprojectmaster(lsprojectmaster);
    	
    	LSsamplemaster lssamplemaster = new LSsamplemaster();
    	lssamplemaster.setSamplecode(rs.getInt("lssamplemaster_samplecode"));
    	ordernot.setLssamplemaster(lssamplemaster);
        
    	LSuserMaster lsusermaster1 = new LSuserMaster();
    	lsusermaster1.setUsercode(rs.getInt("lsusermaster_usercode"));
    	ordernot.setLsuserMaster(lsusermaster1);
    	
    	LSprotocolmaster lsprotocolmaster = new LSprotocolmaster();
    	lsprotocolmaster.setProtocolmastercode(rs.getInt("lsprotocolmaster_protocolmastercode"));
    	ordernot.setLsprotocolmaster(lsprotocolmaster);
    	
    	LSOrdernotification lsordernotification = new LSOrdernotification();
    	lsordernotification.setNotificationcode(rs.getLong("lsordernotification_notificationcode"));
    	ordernot.setLsordernotification(lsordernotification);
    	
    	LSprotocolworkflow lsprotocolworkflow = new LSprotocolworkflow();
    	lsprotocolworkflow.setWorkflowcode(rs.getInt("lsprotocolworkflow_workflowcode"));
    	ordernot.setlSprotocolworkflow(lsprotocolworkflow);
    	
    	ordernot.setApproved(rs.getInt("approved"));
    	ordernot.setRejected(rs.getInt("rejected"));
    	ordernot.setSitecode(rs.getInt("sitecode"));
    	ordernot.setCreateby(rs.getInt("createby"));
    	//ordernot.setVersionno(rs.getInt("versiono"));
    	
    	
    	LsAutoregister lsAutoregister = new LsAutoregister();
    	lsAutoregister.setRegcode(rs.getLong("lsAutoregister_regcode"));
    	ordernot.setLsautoregister(lsAutoregister);
    	
    	ordernot.setOrderflag(rs.getString("orderflag"));
    	
    	LSuserMaster lsusermaster2 = new LSuserMaster();
    	lsusermaster2.setUsercode(rs.getInt("assignedto_usercode"));
    	ordernot.setAssignedto(lsusermaster2);
    	
    	Lsrepositoriesdata lsrepositoriesdata = new Lsrepositoriesdata();
    	lsrepositoriesdata.setRepositorycode(rs.getInt("lsrepositoriesdata_repositorydatacode"));
    	ordernot.setLsrepositoriesdata(lsrepositoriesdata);
    	
    	Lsrepositories lsrepositories = new Lsrepositories();
    	lsrepositories.setRepositorycode(rs.getInt("lsrepositories_repositorycode"));
    	ordernot.setLsrepositories(lsrepositories);
    	
    	ordernot.setDirectorycode(rs.getLong("directorycode"));
    	ordernot.setOrderdisplaytype(rs.getInt("orderdisplaytype"));
    	
    	LSworkflow lstworkflow = new LSworkflow();
    	lstworkflow.setWorkflowcode(rs.getInt("lsworkflow_workflowcode"));
    	ordernot.setLsworkflow(lstworkflow);
    	
    	ordernot.setViewoption(rs.getInt("viewoption"));
    	
    	rs.getString("ordercancell");
    	ordernot.setOrdercancell(rs.getString("ordercancell") != null ? rs.getInt("ordercancell") : null);
    	
    	ordernot.setTeamcode(rs.getInt("teamcode"));
    	ordernot.setApprovelstatus(rs.getInt("approvelstatus"));
    	
    	//ordernot.setTestname(rs.getString("testname"));
    	
//    	Material material = new Material();
//    	material.setNmaterialcode(rs.getInt("material_nmaterialcode"));
//    	ordernot.setMaterial(material);
//    	
//    	MaterialInventory materialinventory = new MaterialInventory();
//    	materialinventory.setNmaterialinventorycode(rs.getInt("materialinventory_nmaterialinventorycode"));
//    	ordernot.setMaterialinventory(materialinventory);
    	
    	ordernot.setFileuid(rs.getString("fileuid"));
    	ordernot.setFileuri(rs.getString("fileuri"));
    	ordernot.setContainerstored(rs.getInt("containerstored"));
    	ordernot.setOrderstarted(rs.getInt("orderstarted"));
    	
    	LSuserMaster lsusermaster3 = new LSuserMaster();
    	lsusermaster3.setUsercode(rs.getInt("orderstartedby_usercode"));
    	ordernot.setAssignedto(lsusermaster3);
    
    	rs.getString("elnmaterial_nmaterialcode");
    	Elnmaterial elnmaterial = new Elnmaterial();
    	elnmaterial.setNmaterialcode(rs.getString("elnmaterial_nmaterialcode") != null ? rs.getInt("elnmaterial_nmaterialcode"):null);
    	ordernot.setElnmaterial(elnmaterial);
    	
    	rs.getString("elnmaterialinventory_nmaterialinventorycode");
    	ElnmaterialInventory elnmaterialinventory = new ElnmaterialInventory();
    	elnmaterialinventory.setNmaterialinventorycode(rs.getString("elnmaterialinventory_nmaterialinventorycode") != null ? rs.getInt("elnmaterialinventory_nmaterialinventorycode") : null);
    	ordernot.setElnmaterialinventory(elnmaterialinventory);
    	
    	
    	Elnprotocolworkflow elnprotocolworkflow = new Elnprotocolworkflow();
    	elnprotocolworkflow.setWorkflowcode(rs.getInt("elnprotocolworkflow_workflowcode"));
    	ordernot.setElnprotocolworkflow(elnprotocolworkflow);
    	
    	ordernot.setApprovelaccept(rs.getBoolean("approvelaccept"));
    	ordernot.setSentforapprovel(rs.getBoolean("sentforapprovel"));
    	ordernot.setLockedusername(rs.getString("lockedusername"));
    	ordernot.setRepeat(rs.getBoolean("repeat"));
    	
        return ordernot;
    }
    public LSlogilablimsorderdetail mapResultSetToLslogilabOrder (ResultSet rs) throws SQLException {
    	LSlogilablimsorderdetail orderobj = new LSlogilablimsorderdetail();
    	orderobj.setBatchcode(rs.getLong("batchcode"));
    	orderobj.setBatchid(rs.getString("batchid"));
    	orderobj.setApprovelaccept(rs.getBoolean("approvelaccept"));
    	orderobj.setSentforapprovel(rs.getBoolean("sentforapprovel"));
    	orderobj.setApprovelstatus(rs.getInt("approvelstatus"));
    	orderobj.setApproved(rs.getInt("approved"));
    	orderobj.setRepeat(rs.getBoolean("repeat"));
    	orderobj.setFiletype(rs.getInt("filetype"));
    	
    	LSworkflow lstworkflow = new LSworkflow();
    	lstworkflow.setWorkflowcode(rs.getInt("lsworkflow_workflowcode"));
    	orderobj.setLsworkflow(lstworkflow);
    	
    	orderobj.setLockeduser(rs.getInt("lockeduser"));
    	
    	LSuserMaster lsusermaster1 = new LSuserMaster();
    	lsusermaster1.setUsercode(rs.getInt("lsusermaster_usercode"));
    	orderobj.setLsuserMaster(lsusermaster1);
    	
    	LSuserMaster lsusermaster2 = new LSuserMaster();
    	lsusermaster2.setUsercode(rs.getInt("assignedto_usercode"));
    	orderobj.setAssignedto(lsusermaster2);
    	
    	LSfile lsfile = new LSfile();
    	lsfile.setFilecode(rs.getInt("lsfile_filecode"));
    	orderobj.setLsfile(lsfile);
    	
    	//orderobj.getLsfile().setFilecode(rs.getInt("filecode"));
    	LSprojectmaster lsprojectmaster = new LSprojectmaster();
    	lsprojectmaster.setProjectcode(rs.getInt("lsprojectmaster_projectcode"));
    	orderobj.setLsprojectmaster(lsprojectmaster);
    	
    	//orderobj.getLsprojectmaster().setProjectcode(rs.getInt("projectcode"));
    	LSsamplemaster lssamplemaster = new LSsamplemaster();
    	lssamplemaster.setSamplecode(rs.getInt("lssamplemaster_samplecode"));
    	orderobj.setLssamplemaster(lssamplemaster);
    	
    	//orderobj.getLssamplemaster().setSamplecode(rs.getInt("samplecode"));
    	LSsamplefile lssamplefile = new LSsamplefile();
    	lssamplefile.setFilesamplecode(rs.getInt("lssamplefile_filesamplecode"));
    	orderobj.setLssamplefile(lssamplefile);
    	//orderobj.getLssamplefile().setFilesamplecode(rs.getInt("filesamplecode"));
    	
    	//orderobj.getLsuserMaster().setUsercode(rs.getInt("usercode"));
    	//orderobj.setLsuserMaster(lsusermaster);
    	
    	Lsrepositoriesdata lsrepositoriesdata = new Lsrepositoriesdata();
    	lsrepositoriesdata.setRepositorycode(rs.getInt("lsrepositoriesdata_repositorydatacode"));
    	orderobj.setLsrepositoriesdata(lsrepositoriesdata);
    	
    	//orderobj.getLsrepositoriesdata().setRepositorycode(rs.getInt("repositorydatacode"));
    	orderobj.setFilecode(rs.getInt("filecode"));
    	orderobj.setKeyword(rs.getString("keyword"));
    	orderobj.setLockedusername(rs.getString("lockedusername"));
    	orderobj.setDirectorycode(rs.getLong("directorycode"));
    	orderobj.setOrderdisplaytype(rs.getInt("orderdisplaytype"));
    	
    	LStestmasterlocal lstestmasterlocal = new LStestmasterlocal();
    	lstestmasterlocal.setTestcode(rs.getInt("lstestmasterlocal_testcode"));
    	orderobj.setLstestmasterlocal(lstestmasterlocal);
    	
    	//orderobj.getLstestmasterlocal().setTestcode(rs.getInt("testcode"));
    	orderobj.setViewoption(rs.getInt("viewoption"));
    	rs.getString("ordercancell");
    	orderobj.setOrdercancell(rs.getString("ordercancell") != null ? rs.getInt("ordercancell") : null);
    	orderobj.setTeamcode(rs.getInt("teamcode"));
    	
//    	Material material = new Material();
//    	material.setNmaterialcode(rs.getInt("material_nmaterialcode"));
//    	orderobj.setMaterial(material);
    	
//    	rs.getString("materialinventory_nmaterialinventorycode");
//    	MaterialInventory materialinventory = new MaterialInventory();
//    	materialinventory.setNmaterialinventorycode(rs.getString("materialinventory_nmaterialinventorycode") != null ? rs.getInt("materialinventory_nmaterialinventorycode") : null);
//    	orderobj.setMaterialinventory(materialinventory);
    	
    	orderobj.setActiveuser(rs.getInt("activeuser"));
    	
    	rs.getString("elnmaterial_nmaterialcode");
    	Elnmaterial elnmaterial = new Elnmaterial();
    	elnmaterial.setNmaterialcode(rs.getString("elnmaterial_nmaterialcode") != null ? rs.getInt("elnmaterial_nmaterialcode"):null);
    	orderobj.setElnmaterial(elnmaterial);
    	
    	rs.getString("elnmaterialinventory_nmaterialinventorycode");
    	ElnmaterialInventory elnmaterialinventory = new ElnmaterialInventory();
    	elnmaterialinventory.setNmaterialinventorycode(rs.getString("elnmaterialinventory_nmaterialinventorycode") != null ? rs.getInt("elnmaterialinventory_nmaterialinventorycode") : null);
    	orderobj.setElnmaterialinventory(elnmaterialinventory);
    	
    	//orderobj.getElnmaterial().setNmaterialcode(rs.getInt("nmaterialcode"));
    	LSOrdernotification lsordernotification = new LSOrdernotification();
    	lsordernotification.setNotificationcode(rs.getLong("lsordernotification_notificationcode"));
    	orderobj.setLsordernotification(lsordernotification);
    	
    	//orderobj.getLsordernotification().setNotificationcode(rs.getLong("notificiationcode"));
    	
    	LsAutoregister lsAutoregister = new LsAutoregister();
    	lsAutoregister.setRegcode(rs.getLong("lsAutoregisterorders_regcode"));
    	orderobj.setLsautoregisterorders(lsAutoregister);
    	orderobj.setOrderflag(rs.getString("orderflag"));
    	
    	orderobj.setTestcode(rs.getInt("testcode"));
    	orderobj.setTestname(rs.getString("testname"));
    	//orderobj.getLsautoregisterorders().setRegcode(rs.getLong("regcode"));
    	return orderobj;
    	
    }

    public LSfile mapResultsetToLSFile(ResultSet rs) throws SQLException{
    	LSfile fileobj = new LSfile();
    	fileobj.setApproved(rs.getInt("approved"));
    	fileobj.setFilecode(rs.getInt("filecode"));
    	fileobj.setFilecontent(rs.getString("filecontent"));
    	
//    	LSSiteMaster lssitemaster = new LSSiteMaster();
//    	lssitemaster.setSitecode(rs.getInt("sitecode"));
//    	
//    	fileobj.setLssitemaster(lssitemaster);
    	fileobj.setFilenameuser(rs.getString("filenameuser"));
    	return fileobj;
    }
    
    public LSworkflow mapResultsetToLSworkflow(ResultSet rs) throws SQLException {
    	LSworkflow lsworkflow = new LSworkflow();
    	
    	LSSiteMaster lssitemaster = new LSSiteMaster();
    	lssitemaster.setSitecode(rs.getInt("sitecode"));
    	lsworkflow.setLssitemaster(lssitemaster);
    	
    	LSuserMaster lsusermaster = new LSuserMaster();
    	lsusermaster.setUsercode(rs.getInt("usercode"));
    	lsworkflow.setLSuserMaster(lsusermaster);
    	
    	lsworkflow.setWorkflowcode(rs.getInt("workflowcode"));
    	lsworkflow.setWorkflowname(rs.getString("workflowname"));
    	
    //	lsworkflow.setObjuser((LoggedUser) rs.getObject("LoggedUser"));
		return lsworkflow; 	
    }
    
    public LSuserMaster  mapResultsetToLsUsermaster(ResultSet rs) throws SQLException {
    	LSuserMaster lsusermaster = new LSuserMaster();
    	lsusermaster.setUsercode(rs.getInt("usercode"));
    	lsusermaster.setUsername(rs.getString("username"));
    	lsusermaster.setUserstatus(rs.getString("userstatus"));
		return lsusermaster;
    }
    
    public CloudSheetCreation mapResultsetToCloudSheet(ResultSet rs) throws SQLException {
    	
    	 CloudSheetCreation cloudSheetCreation = new CloudSheetCreation();
         cloudSheetCreation.setId(rs.getLong("id"));
         cloudSheetCreation.setContent(rs.getString("content"));
         cloudSheetCreation.setFileuid(rs.getString("fileuid"));
         cloudSheetCreation.setContainerstored(rs.getInt("containerstored"));
         cloudSheetCreation.setFileuri(rs.getString("fileuri"));
    	
		return cloudSheetCreation;
    	
    }
	public LSOrdernotification mapResultSetToOrderNotification(ResultSet rs) throws SQLException {
        LSOrdernotification ordernot = new LSOrdernotification();
        ordernot.setBatchcode(rs.getLong("batchcode"));
        ordernot.setBatchid(rs.getString("batchid"));
        ordernot.setCautiondate(rs.getTimestamp("cautiondate"));
        ordernot.setCautionstatus(rs.getInt("cautionstatus"));
        ordernot.setDuedate(rs.getTimestamp("duedate"));
        ordernot.setDuestatus(rs.getInt("duestatus"));
        ordernot.setIscompleted(rs.getBoolean("iscompleted"));
        ordernot.setNotificationcode(rs.getLong("notificationcode"));
        ordernot.setOverduedays(rs.getString("overduedays"));
        ordernot.setOverduestatus(rs.getInt("overduestatus"));
        ordernot.setPeriod(rs.getString("period"));
        ordernot.setScreen(rs.getString("screen"));
        ordernot.setUsercode(rs.getInt("usercode"));
        return ordernot;
    }
	
	public LsAutoregister mapResultSetToLsAutoregister(ResultSet rs) throws SQLException {
		LsAutoregister lsautoregister = new LsAutoregister();
		lsautoregister.setAutocreatedate(rs.getTimestamp("autocreatedate"));
		lsautoregister.setBatchcode(rs.getLong("batchcode"));
		lsautoregister.setInterval(rs.getInt("interval"));
		lsautoregister.setIsautoreg(rs.getBoolean("isautoreg"));
		lsautoregister.setIsmultitenant(rs.getInt("ismultitenant"));
		lsautoregister.setRegcode(rs.getLong("regcode"));
		lsautoregister.setRepeat(rs.getBoolean("repeat"));
		lsautoregister.setScreen(rs.getString("screen"));
		lsautoregister.setTimespan(rs.getString("timespan"));
		return lsautoregister;
		
	}

	public LSprotocolmaster mapResultSetToLsprotocolmaster (ResultSet rs )throws SQLException {
		LSprotocolmaster lsprotocolmaster = new LSprotocolmaster();
		lsprotocolmaster.setProtocolmastercode(rs.getInt("protocolmastercode"));
		lsprotocolmaster.setApproved(rs.getInt("approved"));
		lsprotocolmaster.setCreatedate(rs.getTimestamp("createdate"));
		lsprotocolmaster.setCreatedbyusername(rs.getString("createdbyusername"));
		
		lsprotocolmaster.setLssitemaster(rs.getInt("lssitemaster_sitecode"));
		lsprotocolmaster.setProtocolmastername(rs.getString("protocolmastername"));
		lsprotocolmaster.setProtocolstatus(rs.getInt("protocolstatus"));
		lsprotocolmaster.setRejected(rs.getInt("rejected"));
		lsprotocolmaster.setSharewithteam(rs.getInt("sharewithteam"));
		lsprotocolmaster.setStatus(rs.getInt("status"));

		LSprotocolworkflow lsprotocolworkflow = new LSprotocolworkflow();
    	lsprotocolworkflow.setWorkflowcode(rs.getInt("lsprotocolworkflow_workflowcode"));
    	lsprotocolmaster.setlSprotocolworkflow(lsprotocolworkflow);
		
    	lsprotocolmaster.setVersionno(rs.getInt("versionno"));
    	lsprotocolmaster.setViewoption(rs.getInt("viewoption"));
    	
    	LSsheetworkflow lssheetworkflow = new LSsheetworkflow();
    	lssheetworkflow.setWorkflowcode(rs.getInt("lssheetworkflow_workflowcode"));
    	lsprotocolmaster.setLssheetworkflow(lssheetworkflow);
    	
    	lsprotocolmaster.setFileuid(rs.getString("fileuid"));
    	lsprotocolmaster.setFileuri(rs.getString("fileuri"));
    	lsprotocolmaster.setContainerstored(rs.getInt("containerstored"));
    	
    	ElnprotocolTemplateworkflow elnprototemwork = new ElnprotocolTemplateworkflow();
    	elnprototemwork.setWorkflowcode(rs.getInt("elnprotocoltemplateworkflow_workflowcode"));
    	lsprotocolmaster.setElnprotocoltemplateworkflow(elnprototemwork);
    	
    	lsprotocolmaster.setRetirestatus(rs.getInt("retirestatus"));
		
		return lsprotocolmaster;
	
	}
	
    public Notification mapResultSetToNotification(ResultSet rs) throws SQLException {
        Notification notification = new Notification();
        notification.setNotificationid(rs.getLong("notificationid"));
        notification.setAddedon(rs.getTimestamp("addedon"));
        notification.setOrderid(rs.getLong("orderid"));
        notification.setAddedby(rs.getString("addedby"));
        notification.setDuedate(rs.getTimestamp("duedate"));
        notification.setIntervals(rs.getString("intervals"));
        notification.setScreen(rs.getString("screen"));
        notification.setCautiondate(rs.getTimestamp("cautiondate"));
        notification.setDescription(rs.getString("description"));
        notification.setUsercode(rs.getInt("usercode"));
        notification.setStatus(rs.getInt("status"));
        notification.setCurrentdate(rs.getTimestamp("currentdate"));
        notification.setBatchid(rs.getString("batchid"));

        return notification;
    }
    
    public LSSiteMaster mapResultSetToLSiteMaster (ResultSet rs) throws SQLException {
    	LSSiteMaster lssitemaster = new LSSiteMaster();
    	lssitemaster.setSitecode(rs.getInt("sitecode"));
    	lssitemaster.setSitename(rs.getString("sitename"));
    	lssitemaster.setIstatus(rs.getInt("istatus"));
		return lssitemaster;
    }
    
    public Elnprotocolworkflow mapResultSetToElnprotocolworkflow(ResultSet rs) throws SQLException {
    	Elnprotocolworkflow elnprotocolworkflow = new Elnprotocolworkflow();
    	elnprotocolworkflow.setWorkflowcode(rs.getInt("workflowcode"));
    	elnprotocolworkflow.setWorkflowname(rs.getString("workflowname"));
    	
    	LSSiteMaster lssitemaster = new LSSiteMaster();
    	lssitemaster.setSitecode(rs.getInt("lssitemaster_sitecode"));
    	elnprotocolworkflow.setLssitemaster(lssitemaster);
    	
    	return elnprotocolworkflow;
    }
    
    public LSprotocolstep mapResultSetToLsprotocolstep(ResultSet rs)throws SQLException {
    	LSprotocolstep lsprotocolstep = new LSprotocolstep();
    	lsprotocolstep.setProtocolstepcode(rs.getInt("protocolstepcode"));
    	lsprotocolstep.setProtocolmastercode(rs.getInt("protocolmastercode"));
    	lsprotocolstep.setStepno(rs.getInt("stepno"));
    	lsprotocolstep.setProtocolstepname(rs.getString("protocolstepname"));
    	lsprotocolstep.setStatus(rs.getInt("status"));
    	lsprotocolstep.setSitecode(rs.getInt("sitecode"));
    	
		return lsprotocolstep;
    	
    }
    public void checkAndScheduleProtocolautoRegister() throws SQLException, IOException {
    	List<Datasourcemaster> configList = configRepo.findByinitialize(true);

        LocalDateTime localDateTime = LocalDateTime.now();
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();

        Date toDate = Date.from(instant);
      
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY , +12);
        toDate=calendar.getTime();
      
        LocalDateTime  previousDate = localDateTime.minusDays(1);
        Instant preinstant = previousDate.atZone(ZoneId.systemDefault()).toInstant();
        Date fromDate=Date.from(preinstant);  

        
        for (Datasourcemaster objData : configList) {
            HikariConfig configuration = createHikariConfig(objData);
            try (HikariDataSource dataSource = new HikariDataSource(configuration);
                    Connection con = dataSource.getConnection()) {
            	
            	String checkrepeat = "SELECT * FROM lslogilabprotocoldetail WHERE repeat = true and createdtimestamp BETWEEN ? AND ?";
            	 try (PreparedStatement pst = con.prepareStatement(checkrepeat)) {
                     
                 	 pst.setTimestamp(1, new Timestamp(fromDate.getTime()));
                     pst.setTimestamp(2, new Timestamp(toDate.getTime()));

                     try (ResultSet rs = pst.executeQuery()) {
                         while (rs.next()) {
                        	 LSlogilabprotocoldetail orderobj = mapResultSetToOrderLSlogilabprotocoldetail(rs);
                             scheduleProtocolAutoRegisteration(orderobj, configuration);
                         }
                     } catch (SQLException e) {
 		                e.printStackTrace(); // Consider logging this properly
 		            }
                 }
            }	
        }
    }
    
    private void scheduleProtocolAutoRegisteration(LSlogilabprotocoldetail orderobj, HikariConfig configuration) throws SQLException, IOException {
    	 
    	LsAutoregister objlsauto = null ;
    	LSuserMaster objuser = null;
    	 try (HikariDataSource dataSource = new HikariDataSource(configuration);
                 Connection con = dataSource.getConnection()) {

                String autoregquery = "SELECT * FROM Lsautoregister WHERE batchcode=? ";

                try (PreparedStatement pst = con.prepareStatement(autoregquery)) {
                   
                	pst.setLong(1, orderobj.getProtocolordercode());
                 
                    try (ResultSet rs = pst.executeQuery()) {
                        while (rs.next()) {
                        	 objlsauto = mapResultSetToLsAutoregister(rs);
                        	 orderobj.setLsautoregisterorder(objlsauto);
                        	 orderobj.setLsautoregister(objlsauto);
                           
                        }
                    } catch (SQLException e) {
		                e.printStackTrace(); // Consider logging this properly
		            }
                }
    	 }
    	Date Autoregdate = objlsauto.getAutocreatedate();
    	Instant auto = Autoregdate.toInstant();
        LocalDateTime autoTime = LocalDateTime.ofInstant(auto, ZoneId.systemDefault());
        LocalDateTime currentTime = LocalDateTime.now();

        if (autoTime.isAfter(currentTime)) {
            Duration duration = Duration.between(currentTime, autoTime);
            long delay = duration.toMillis();
            scheduleForProtocolAutoRegOrders(orderobj, delay, configuration);
        }else {
        	try {
				ExecuteProtocolAutoRegistration(orderobj,configuration);
			} catch (ParseException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
        }
   }
    
    public void updateProtocolOrderContent(String Content, LSlogilabprotocoldetail objOrder, Integer ismultitenant,HikariConfig configuration)
			throws IOException, SQLException {
    	try (HikariDataSource dataSource = new HikariDataSource(configuration);
                Connection con = dataSource.getConnection()) {
		if (ismultitenant == 1 || ismultitenant == 2) {

			Map<String, Object> objMap = objCloudFileManipulationservice.storecloudSheetsreturnwithpreUUID(Content,
					commonfunction.getcontainername(ismultitenant, (String) dataSource.getDataSourceProperties().getProperty("tenantName")) + "protocolorder");
			String fileUUID = (String) objMap.get("uuid");
			String fileURI = objMap.get("uri").toString();

			objOrder.setFileuri(fileURI);
			objOrder.setFileuid(fileUUID);
			objOrder.setContainerstored(1);
			
			String deftemp = "update LSlogilabprotocoldetail set fileuri = ? , fileuid = ? , containerstored = ? , protocolordername = ? WHERE protocolordercode = ?";
							
   	 		try (PreparedStatement pst = con.prepareStatement(deftemp)) {
            
   	 		pst.setString(1, objOrder.getFileuri());
   	 		pst.setString(2, objOrder.getFileuid());
		   	pst.setInt(3, objOrder.getContainerstored());
		   	pst.setString(4, objOrder.getProtoclordername());
		   	pst.setLong(5, objOrder.getProtocolordercode());
		   	
		   	
		   	pst.executeUpdate();
//   	 			try (ResultSet rs = pst.executeQuery()) {
//                while (rs.next()) {
//                	 LSlogilabprotocoldetail orderobj  = mapResultSetToOrderLSlogilabprotocoldetail(rs);
//                	 //protocolSteps.add(protostepobj);
//                }
//            } catch (SQLException e) {
//                e.printStackTrace(); // Consider logging this properly
//            }
        }
   	 		
			//LSlogilabprotocoldetailRepository.save(objOrder);
		  }
		} 
	}
    
    public void checkAndScheduleautoOrderRegister() throws SQLException, IOException {
    	List<Datasourcemaster> configList = configRepo.findByinitialize(true);

        LocalDateTime localDateTime = LocalDateTime.now();
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();

        Date toDate = Date.from(instant);
        
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY , +12);
        toDate=calendar.getTime();
        
        LocalDateTime  previousDate = localDateTime.minusDays(1);
        Instant preinstant = previousDate.atZone(ZoneId.systemDefault()).toInstant();
        Date fromDate=Date.from(preinstant);  
        
        for (Datasourcemaster objData : configList) {
            HikariConfig configuration = createHikariConfig(objData);
            try (HikariDataSource dataSource = new HikariDataSource(configuration);
                    Connection con = dataSource.getConnection()) {
            	
            	String checkrepeat = "SELECT * FROM LSlogilablimsorderdetail WHERE repeat = true and createdtimestamp BETWEEN ? AND ?";
            	 try (PreparedStatement pst = con.prepareStatement(checkrepeat)) {
                     
                 	 pst.setTimestamp(1, new Timestamp(fromDate.getTime()));
                     pst.setTimestamp(2, new Timestamp(toDate.getTime()));

                     try (ResultSet rs = pst.executeQuery()) {
                         while (rs.next()) {
                        	 LSlogilablimsorderdetail orderobj = mapResultSetToLslogilabOrder(rs);
                             scheduleAutoRegisteration(orderobj, configuration);
                         }
                     } catch (SQLException e) {
 		                e.printStackTrace(); // Consider logging this properly
 		            }
                 }
            }	
        }
    }

    private void scheduleAutoRegisteration(LSlogilablimsorderdetail orderobj, HikariConfig configuration) throws SQLException, IOException {
    	LsAutoregister objlsauto = null ;
    	LSuserMaster objuser = null;
    	 try (HikariDataSource dataSource = new HikariDataSource(configuration);
                 Connection con = dataSource.getConnection()) {

                String autoregquery = "SELECT * FROM Lsautoregister WHERE batchcode=? ";

                try (PreparedStatement pst = con.prepareStatement(autoregquery)) {
                   
                	pst.setLong(1, orderobj.getBatchcode());
                 
                    try (ResultSet rs = pst.executeQuery()) {
                        while (rs.next()) {
                        	 objlsauto = mapResultSetToLsAutoregister(rs);
                        	 orderobj.setLsautoregisterorders(objlsauto);
                           // scheduleNotificationForCaution(objNotification, configuration);
                        }
                    } catch (SQLException e) {
		                e.printStackTrace(); // Consider logging this properly
		            }
                }
                
               String lsuserquery = "SELECT * FROM Lsusermaster WHERE usercode=? ";
               try (PreparedStatement pst = con.prepareStatement(lsuserquery)) {
                   
               	pst.setInt(1, orderobj.getLsuserMaster().getUsercode());
                
                   try (ResultSet rs = pst.executeQuery()) {
                       while (rs.next()) {
                    	   objuser = mapResultsetToLsUsermaster(rs);
                       	   orderobj.setLsuserMaster(objuser);
                       	   //orderobj.setAssignedto(objuser);
                          // scheduleNotificationForCaution(objNotification, configuration);
                       }
                   } catch (SQLException e) {
		                e.printStackTrace(); // Consider logging this properly
		            }
               }
    	 }
    	Date Autoregdate = objlsauto.getAutocreatedate();
    	Instant auto = Autoregdate.toInstant();
        LocalDateTime autoTime = LocalDateTime.ofInstant(auto, ZoneId.systemDefault());
        LocalDateTime currentTime = LocalDateTime.now();

        if (autoTime.isAfter(currentTime)) {
            Duration duration = Duration.between(currentTime, autoTime);
            long delay = duration.toMillis();
            scheduleForAutoRegOrders(orderobj, delay, configuration);
        }else {
        	try {
				ExecuteAutoRegistration(orderobj,configuration);
			} catch (ParseException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
        }
    }
    
    public void checkAndScheduleRemindersforOrders() throws SQLException {
        List<Datasourcemaster> configList = configRepo.findByinitialize(true);

        LocalDateTime localDateTime = LocalDateTime.now();
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();

        Date toDate = Date.from(instant);
        
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY , +12);
        toDate=calendar.getTime();
       
        calendar.add(Calendar.HOUR_OF_DAY , -24);
        Date fromDate = calendar.getTime();

        calendar.add(Calendar.HOUR_OF_DAY , -23);
        Date overduefromDate = calendar.getTime();
        
        for (Datasourcemaster objData : configList) {
            HikariConfig configuration = createHikariConfig(objData);
            try (HikariDataSource dataSource = new HikariDataSource(configuration);
                 Connection con = dataSource.getConnection()) {

                String cautionquery = "SELECT * FROM lsordernotification WHERE (cautionstatus = 1 and cautiondate BETWEEN ? AND ?) ";

                try (PreparedStatement pst = con.prepareStatement(cautionquery)) {
                   
                	pst.setTimestamp(1, new Timestamp(fromDate.getTime()));
                    pst.setTimestamp(2, new Timestamp(toDate.getTime()));

                    try (ResultSet rs = pst.executeQuery()) {
                        while (rs.next()) {
                            LSOrdernotification objNotification = mapResultSetToOrderNotification(rs);
                            scheduleNotificationForCaution(objNotification, configuration);
                        }
                    } catch (SQLException e) {
		                e.printStackTrace(); // Consider logging this properly
		            }
                }
            
	                String duequery = "SELECT * FROM lsordernotification WHERE (duestatus = 1 and duedate BETWEEN ? AND ?) ";
	
	                try (PreparedStatement pst = con.prepareStatement(duequery)) {
	           
			        	pst.setTimestamp(1, new Timestamp(fromDate.getTime()));
			            pst.setTimestamp(2, new Timestamp(toDate.getTime()));
			        	
			
			            try (ResultSet rs = pst.executeQuery()) {
			                while (rs.next()) {
			                    LSOrdernotification objNotification = mapResultSetToOrderNotification(rs);
			                    scheduleNotificationForDue(objNotification, configuration);
			                }
			            }catch (SQLException e) {
			                e.printStackTrace(); // Consider logging this properly
			            }
	                }
        
			        String overduequery = "SELECT * FROM lsordernotification WHERE (overduestatus = 1 and duedate BETWEEN ? AND ?) ";
			        		
					try (PreparedStatement pst = con.prepareStatement(overduequery)) {
					   
						pst.setTimestamp(1, new Timestamp(overduefromDate.getTime()));
					    pst.setTimestamp(2, new Timestamp(toDate.getTime()));
					
					
					    try (ResultSet rs = pst.executeQuery()) {
					        while (rs.next()) {
					            LSOrdernotification objNotification = mapResultSetToOrderNotification(rs);
					            scheduleNotificationForOver(objNotification, configuration);
					        }
					    }
				       catch (SQLException e) {
		                e.printStackTrace(); // Consider logging this properly
		              }
					}
					
					 String overduedaysquery = "SELECT * FROM lsordernotification WHERE (Isduedateexhausted = true and Iscompleted = false)"
					 		+ "OR"
					 		+ " Isduedateexhausted = true and Iscompleted isnull";

					 try (PreparedStatement pst = con.prepareStatement(overduedaysquery)) {
						
						    try (ResultSet rs = pst.executeQuery()) {
						        while (rs.next()) {
						            LSOrdernotification objNotification = mapResultSetToOrderNotification(rs);
						            notifyoverduedays(objNotification, configuration);
						        }
						    } catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					 
		             catch (SQLException e) {
		                e.printStackTrace(); // Consider logging this properly
		            }
            }
        }
    }

    
    public void checkAndScheduleReminders() throws SQLException {
        List<Datasourcemaster> configList = configRepo.findByinitialize(true);

        // Get current date and time
        LocalDateTime localDateTime = LocalDateTime.now();
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();

        // Convert to java.sql.Date
        Date toDate = Date.from(instant);

        // Get the current date and subtract one day
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(toDate);
        calendar.add(Calendar.DAY_OF_MONTH, -1);  // Subtract one day
        Date fromDate = new Date(calendar.getTimeInMillis());
        
        for (Datasourcemaster objData : configList) {
            HikariConfig configuration = createHikariConfig(objData);
            try (HikariDataSource dataSource = new HikariDataSource(configuration);
                 Connection con = dataSource.getConnection()) {

                String query = "SELECT * FROM Notification WHERE status = 1 and cautiondate BETWEEN ? AND ?";
                try (PreparedStatement pst = con.prepareStatement(query)) {
                   
                	pst.setTimestamp(1, new Timestamp(fromDate.getTime()));
                    pst.setTimestamp(2, new Timestamp(toDate.getTime()));

                    try (ResultSet rs = pst.executeQuery()) {
                        while (rs.next()) {
                            Notification objNotification = mapResultSetToNotification(rs);
                            scheduleNotificationIfDue(objNotification, configuration);
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace(); // Consider logging this properly
            }
        }
    }
    
    private void scheduleNotificationForCaution(LSOrdernotification objNotification, HikariConfig configuration) {
        Date cautionDate = objNotification.getCautiondate();
        Instant caution = cautionDate.toInstant();
        LocalDateTime cautionTime = LocalDateTime.ofInstant(caution, ZoneId.systemDefault());
        LocalDateTime currentTime = LocalDateTime.now();

       
        if (cautionTime.isAfter(currentTime)) {
            Duration duration = Duration.between(currentTime, cautionTime);
            long delay = duration.toMillis();
            scheduleCautionNotification(objNotification, delay, configuration);
        }else {
	        TimerTask task = new TimerTask() {
	            @SuppressWarnings("unlikely-arg-type")
				@Override
	            public void run() {
	             
	                    try {
							executecautiondatenotification(objNotification, configuration);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	               
	                scheduledTasks.remove(objNotification.getNotificationcode());
	            }
	        };
	        Timer timer = new Timer();
	        timer.schedule(task, 10000);
	        scheduledTasks.put(objNotification.getNotificationcode().intValue(), task);
        }
    }

    
    private void scheduleNotificationForDue(LSOrdernotification objNotification, HikariConfig configuration) {
    	Date dueDate = objNotification.getDuedate();
        Instant due = dueDate.toInstant();
        
        LocalDateTime dueTime = LocalDateTime.ofInstant(due, ZoneId.systemDefault());
        LocalDateTime currentTime = LocalDateTime.now();

       
        if (dueTime.isAfter(currentTime)) {
            Duration duration = Duration.between(currentTime, dueTime);
            long delay = duration.toMillis();
            scheduleDueNotification(objNotification, delay, configuration);
        }else {
	        TimerTask task = new TimerTask() {
	            @SuppressWarnings("unlikely-arg-type")
				@Override
	            public void run() {
	                	try {
							executeduedatenotification(objNotification, configuration);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	                scheduledTasks.remove(objNotification.getNotificationcode());
	            }
	        };
	        Timer timer = new Timer();
	        timer.schedule(task, 20000);
	        scheduledTasks.put(objNotification.getNotificationcode().intValue(), task);
        }
    }

    private void scheduleNotificationForOver(LSOrdernotification objNotification, HikariConfig configuration) {
    	Date overDueDate = objNotification.getDuedate();
    	overDueDate.setMinutes(overDueDate.getMinutes()+5);
        Instant overdue = overDueDate.toInstant();
        
        LocalDateTime overdueTime = LocalDateTime.ofInstant(overdue, ZoneId.systemDefault());
        LocalDateTime currentTime = LocalDateTime.now();

        
        if (overdueTime.isAfter(currentTime)) {
            Duration duration = Duration.between(currentTime, overdueTime);
            long delay = duration.toMillis();
            scheduleOverdueNotification(objNotification, delay, configuration);
        }else {
        TimerTask task = new TimerTask() {
            @SuppressWarnings("unlikely-arg-type")
			@Override
            public void run() {
                
                	try {
						executeoverduenotification(objNotification, configuration);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
               
                scheduledTasks.remove(objNotification.getNotificationcode());
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 30000);
        scheduledTasks.put(objNotification.getNotificationcode().intValue(), task);
        }
    }

    private HikariConfig createHikariConfig(Datasourcemaster objData) {
        HikariConfig configuration = new HikariConfig();
        configuration.setDriverClassName("org.postgresql.Driver");
        configuration.setJdbcUrl(objData.getUrl());
        configuration.setUsername(objData.getUsername());
        configuration.setPassword(objData.getPassword());
        configuration.setMaximumPoolSize(MAX_POOL_SIZE);
        configuration.setPoolName(objData.getUrl());
        configuration.setMinimumIdle(MIN_IDLE);
        configuration.setConnectionTestQuery("SELECT 1");
        configuration.setConnectionTimeout(CONNECTION_TIMEOUT);
        configuration.setLeakDetectionThreshold(CONNECTION_THRESHOLD);
        configuration.addDataSourceProperty("tenantName", objData.getName());

        return configuration;
    }

    private void scheduleNotificationIfDue(Notification objNotification, HikariConfig configuration) throws SQLException {
        Date cautionDate = objNotification.getCautiondate();
        Instant caution = cautionDate.toInstant();
        LocalDateTime cautionTime = LocalDateTime.ofInstant(caution, ZoneId.systemDefault());
        LocalDateTime currentTime = LocalDateTime.now();

        if (cautionTime.isAfter(currentTime)) {
            Duration duration = Duration.between(currentTime, cautionTime);
            long delay = duration.toMillis();
            scheduleNotification(objNotification, delay, configuration);
        }else {
        	executeNotificationPop(objNotification, configuration);
        }
    }
    
//    private final Timer timer = new Timer();

    public void scheduleNotification(final Notification objNotification, long delay, HikariConfig configuration) {
        int notificationId = objNotification.getNotificationid().intValue();

        // Check if a task is already scheduled for this notification ID
        if (scheduledTasks.containsKey(notificationId)) {
            System.out.println("Task already scheduled for notification ID: " + notificationId);
            return;
        }

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                	executeNotificationPop(objNotification, configuration);
                } catch (SQLException e) {
                    e.printStackTrace(); // Consider logging this properly
                }
                scheduledTasks.remove(notificationId);
            }
        };

        // Schedule the task and add it to the scheduledTasks map
        Timer timer = new Timer();
        timer.schedule(task, delay);
        scheduledTasks.put(notificationId, task);
    }

    private void scheduleCautionNotification(LSOrdernotification objNotification, long delay, HikariConfig configuration) {
    	if(objNotification.getIscompleted() == null || objNotification.getIscompleted() == false){
	        TimerTask task = new TimerTask() {
	            @SuppressWarnings("unlikely-arg-type")
				@Override
	            public void run() {
	                try {
	                	executecautiondatenotification(objNotification, configuration);
	                } catch (ParseException e) {
	                    e.printStackTrace(); // Consider logging this properly
	                }
	                scheduledTasks.remove(objNotification.getNotificationcode());
	            }
	        };
	        Timer timer = new Timer();
	        timer.schedule(task, delay);
	        scheduledTasks.put(objNotification.getNotificationcode().intValue(), task);
    	}
    }

    private void scheduleForAutoRegOrders(LSlogilablimsorderdetail orderobj, long delay, HikariConfig configuration) {
    	
    	if((orderobj.getRepeat()!=null || orderobj.getRepeat() != false) && orderobj.getLsautoregisterorders()!=null) {
    		TimerTask task = new TimerTask() {
	            @SuppressWarnings("unlikely-arg-type")
				@Override
	            public void run() {
	                try {
	                	ExecuteAutoRegistration(orderobj, configuration);
	                }  catch (SQLException | ParseException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	                scheduledTasks.remove(orderobj.getBatchcode());
	            }
	        };
	        Timer timer = new Timer();
	        timer.schedule(task, delay);
	        scheduledTasks.put(orderobj.getBatchcode().intValue(), task);
    	}
  
    }
    
private void scheduleForProtocolAutoRegOrders(LSlogilabprotocoldetail orderobj, long delay, HikariConfig configuration) {
    	
    	if((orderobj.getRepeat()!=null || orderobj.getRepeat() != false) && orderobj.getLsautoregister()!=null) {
    		TimerTask task = new TimerTask() {
	            @SuppressWarnings("unlikely-arg-type")
				@Override
	            public void run() {
	                try {
	                	ExecuteProtocolAutoRegistration(orderobj, configuration);
	                }  catch (SQLException | ParseException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	                scheduledTasks.remove(orderobj.getProtocolordercode());
	            }
	        };
	        Timer timer = new Timer();
	        timer.schedule(task, delay);
	        scheduledTasks.put(orderobj.getProtocolordercode().intValue(), task);
    	}
  
    }

    private void scheduleDueNotification(LSOrdernotification objNotification, long delay, HikariConfig configuration) {
    	if(objNotification.getIscompleted() == null || objNotification.getIscompleted() == false){
	        TimerTask task = new TimerTask() {
	            @SuppressWarnings("unlikely-arg-type")
				@Override
	            public void run() {
	                try {
	                	executeduedatenotification(objNotification, configuration);
	                } catch (ParseException e) {
	                    e.printStackTrace(); // Consider logging this properly
	                }
	                scheduledTasks.remove(objNotification.getNotificationcode());
	            }
	        };
	        Timer timer = new Timer();
	        timer.schedule(task, delay);
	        scheduledTasks.put(objNotification.getNotificationcode().intValue(), task);
    	}
    }
    
    private void scheduleOverdueNotification(LSOrdernotification objNotification, long delay, HikariConfig configuration) {
    	if(objNotification.getIscompleted() == null || objNotification.getIscompleted() == false){
	        TimerTask task = new TimerTask() {
	            @SuppressWarnings("unlikely-arg-type")
				@Override
	            public void run() {
	                try {
	                	executeoverduenotification(objNotification, configuration);
	                } catch (ParseException e) {
	                    e.printStackTrace(); // Consider logging this properly
	                }
	                scheduledTasks.remove(objNotification.getNotificationcode());
	            }
	        };
	        Timer timer = new Timer();
	        timer.schedule(task, delay);
	        scheduledTasks.put(objNotification.getNotificationcode().intValue(), task);
    	}
    }
    
    public void ExecuteProtocolAutoRegistration(LSlogilabprotocoldetail lSlogilabprotocoldetail , HikariConfig configuration)throws ParseException, SQLException, IOException {
    	   
    	try (HikariDataSource dataSource = new HikariDataSource(configuration);
                Connection con = dataSource.getConnection()) {

   		 LSprotocolmaster protomasterobj=null;
   		 LSprotocolstep protostepobj=null;
   		 
   	  //  Map<String, Object> mapObj = new HashMap<String, Object>();
		String Content = "";
		
		LsAutoregister autoobj = new LsAutoregister();
		
		if(lSlogilabprotocoldetail.getLsautoregister()!= null) {
			
			Date currentdate = commonfunction.getCurrentUtcTime();
			
			if(lSlogilabprotocoldetail.getLsautoregister().getTimespan().equals("Days")) {
				//Date autodate=lSlogilabprotocoldetail.getLsautoregister().getAutocreatedate();
				
				 Calendar calendar = Calendar.getInstance();
			        calendar.setTime(currentdate);
			        calendar.add(Calendar.DAY_OF_MONTH, lSlogilabprotocoldetail.getLsautoregister().getInterval());
			        Date futureDate = calendar.getTime();  
			        autoobj.setAutocreatedate(futureDate);
			   
			 }else if(lSlogilabprotocoldetail.getLsautoregister().getTimespan().equals("Week")) {
				// Date autodate=lSlogilabprotocoldetail.getLsautoregister().getAutocreatedate();
					
				    Calendar calendar = Calendar.getInstance();
			        calendar.setTime(currentdate);
			        calendar.add(Calendar.DAY_OF_MONTH, (lSlogilabprotocoldetail.getLsautoregister().getInterval()*7));

			        // Convert back to Date (if necessary)
			        Date futureDate = calendar.getTime();   
			        //autoordersfilter.get(0).setAutocreatedate(futureDate);
			        autoobj.setAutocreatedate(futureDate);
			 }else {
				
				    Calendar calendar = Calendar.getInstance();
			        calendar.setTime(currentdate);
			        calendar.add(Calendar.HOUR_OF_DAY,(lSlogilabprotocoldetail.getLsautoregister().getInterval()));
			        Date futureDate = calendar.getTime();   
			        autoobj.setAutocreatedate(futureDate);
//				 Calendar calendar = Calendar.getInstance();
//			        calendar.setTime(currentdate);
//			       // calendar.add(Calendar.HOUR_OF_DAY,(autoorder.get(0).getInterval()));
//			        calendar.add(Calendar.MINUTE , (10));
//			        Date futureDate = calendar.getTime();   
//			        autoobj.setAutocreatedate(futureDate);
			 }
			
			autoobj.setScreen("IDS_PROTOCOLORDERS");
			autoobj.setIsautoreg(true);
			autoobj.setInterval(lSlogilabprotocoldetail.getLsautoregister().getInterval());
			autoobj.setTimespan(lSlogilabprotocoldetail.getLsautoregister().getTimespan());
			autoobj.setIsmultitenant(lSlogilabprotocoldetail.getLsautoregister().getIsmultitenant());
			autoobj.setRepeat(true);
			lSlogilabprotocoldetail.setLsautoregister(autoobj);	
		}
			
		
			 Calendar autocalendar = Calendar.getInstance();
		        autocalendar.setTime(autoobj.getAutocreatedate());
		        Date autocreateddate = autocalendar.getTime(); 
		        
			String autoregtablequery = "Insert into lsautoregister (autocreatedate,interval,isautoreg,"
					+ "ismultitenant,repeat,screen,timespan) Values (?,?,?,?,?,?,?)";
	
	       try (PreparedStatement pst = con.prepareStatement(autoregtablequery, Statement.RETURN_GENERATED_KEYS)) {
	          
	       	pst.setTimestamp(1, new Timestamp(autocreateddate.getTime()));
	       	pst.setInt(2, autoobj.getInterval());
	       	pst.setBoolean(3, autoobj.getIsautoreg());
	       	pst.setInt(4, autoobj.getIsmultitenant());
	       	pst.setBoolean(5, autoobj.getRepeat());
	       	pst.setString(6, autoobj.getScreen());
	       	pst.setString(7, autoobj.getTimespan());
	          
	       	int affectedRows=pst.executeUpdate();
	              
	              if (affectedRows > 0) {
	                  // Retrieve the generated keys
	           	   ResultSet rs = pst.getGeneratedKeys();
	                  if (rs.next()) {
	                      long generatedregcode = rs.getLong(1);
	                      System.out.println("Inserted record's regcode: " + generatedregcode);
	                      lSlogilabprotocoldetail.getLsautoregister().setRegcode(generatedregcode);
	                      // Use the generated key for future use
	                  }
	              } else {
	                  System.out.println("No record inserted.");
	              }
	       }
		

			String deftem = "update LSlogilabprotocoldetail set repeat=false WHERE protocolordercode=?";
 	 		try (PreparedStatement pst = con.prepareStatement(deftem)) {
          
 	 			pst.setLong(1, lSlogilabprotocoldetail.getProtocolordercode());
 	 			pst.executeUpdate();
 	 			lSlogilabprotocoldetail.setRepeat(false);
 	 			
// 	 			try (ResultSet rs = pst.executeQuery()) {
//              while (rs.next()) {
//           	   LSlogilabprotocoldetail protoobj = mapResultSetToOrderLSlogilabprotocoldetail(rs);
//              	   lSlogilabprotocoldetail.setRepeat(protoobj.getRepeat());;
//              }
//          } catch (SQLException e) {
//               e.printStackTrace(); // Consider logging this properly
//           }
         }
 	 		
		try {
			lSlogilabprotocoldetail.setCreatedtimestamp(commonfunction.getCurrentUtcTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (lSlogilabprotocoldetail != null) {
			lSlogilabprotocoldetail.setVersionno(0);

			if (lSlogilabprotocoldetail.getProtocoltype() == 2
					&& lSlogilabprotocoldetail.getLsprotocolmaster().getProtocolmastercode() == -2) {
				
				String deftempromaster = "SELECT * FROM LSprotocolmaster WHERE defaulttemplate =1";
          	 		try (PreparedStatement pst = con.prepareStatement(deftempromaster)) {
                   
          	 			try (ResultSet rs = pst.executeQuery()) {
                       while (rs.next()) {
                       	 protomasterobj = mapResultSetToLsprotocolmaster(rs);
                       	 lSlogilabprotocoldetail.setLsprotocolmaster(protomasterobj);
                       }
                   } catch (SQLException e) {
		                e.printStackTrace(); // Consider logging this properly
		            }
               }	
				
				//LSprotocolmaster lsprotocolmasterobj = LSProtocolMasterRepositoryObj.findByDefaulttemplate(1);
				if (protomasterobj == null) {
					LSprotocolmaster lsprotocolmaster = new LSprotocolmaster();
					lsprotocolmaster.setProtocolmastername("Default Protocol");
					lsprotocolmaster.setStatus(0);
					lsprotocolmaster.setCreatedby(lSlogilabprotocoldetail.getCreateby());
					lsprotocolmaster.setCreatedate(lSlogilabprotocoldetail.getCreatedtimestamp());
					lsprotocolmaster.setLssitemaster(lSlogilabprotocoldetail.getSitecode());
					
					String updateString = "INSERT INTO lsprotocolmaster (protocolmastername,status,createdby,createdate,lssitemaster) "
							+ "VALUES (?,?,?,?,?)";
               		 
					 Calendar protocalendar = Calendar.getInstance();
					 protocalendar.setTime(lsprotocolmaster.getCreatedate());
				     Date protocalendardate = protocalendar.getTime(); 
				        
	               try (PreparedStatement pst = con.prepareStatement(updateString, Statement.RETURN_GENERATED_KEYS)) {
	                   pst.setString(1, lsprotocolmaster.getProtocolmastername());
	                   pst.setInt(2, lsprotocolmaster.getStatus());
	                   pst.setInt(3, lsprotocolmaster.getCreatedby());
	                   pst.setTimestamp(4, new Timestamp(protocalendardate.getTime()));
	                   pst.setInt(5,lSlogilabprotocoldetail.getSitecode());
	                   pst.executeUpdate();
	               }
	           
					lSlogilabprotocoldetail.setLsprotocolmaster(lsprotocolmaster);
				} else {
					lSlogilabprotocoldetail.setLsprotocolmaster(protomasterobj);
				}

			   }
	
			lSlogilabprotocoldetail.setProtoclordername(null);
			lSlogilabprotocoldetail.setProtocolordercode(null);
			
				LSSiteMaster siteobj=null;
				String sitequery = "SELECT * FROM lssitemaster WHERE sitecode = ?";
	   	 		try (PreparedStatement pst = con.prepareStatement(sitequery)) {
	            
	   	 		pst.setInt(1, lSlogilabprotocoldetail.getSitecode());
	   	 		
	   	 			try (ResultSet rs = pst.executeQuery()) {
	                while (rs.next()) {
	                	 siteobj = mapResultSetToLSiteMaster(rs);
	             
	                	 //protocolSteps.add(protostepobj);
	                }
	            } catch (SQLException e) {
	                e.printStackTrace(); // Consider logging this properly
	            	}
	   	 		}
	
		   	     List<Elnprotocolworkflow> proworkflowlist= new ArrayList<>();
		   	     Elnprotocolworkflow workflowobj = new Elnprotocolworkflow();
	   	     
				String workflowquery = "SELECT * FROM elnprotocolworkflow WHERE lssitemaster_sitecode = ?";
			 		try (PreparedStatement pst = con.prepareStatement(workflowquery)) {
		        
			 		pst.setInt(1, lSlogilabprotocoldetail.getSitecode());
			 		
			 			try (ResultSet rs = pst.executeQuery()) {
		            while (rs.next()) {
		            	 workflowobj = mapResultSetToElnprotocolworkflow(rs);
		            	 proworkflowlist.add(workflowobj);
		            	 //protocolSteps.add(protostepobj);
		            }
			        } catch (SQLException e) {
			            e.printStackTrace(); // Consider logging this properly
			        }
		      }
		 	    lSlogilabprotocoldetail.setElnprotocolworkflow(workflowobj);
		 	    lSlogilabprotocoldetail.setLstelnprotocolworkflow(proworkflowlist);

			String updateString = "INSERT INTO lSlogilabprotocoldetail (testcode,createdtimestamp,keyword,protocoltype,lsprojectmaster_projectcode,"
					+ "lsusermaster_usercode,approved,versionno,createby,sitecode,viewoption,"
					+ "fileuid,fileuri,containerstored,lsprotocolmaster_protocolmastercode,repeat,"
					+ "orderflag,elnprotocolworkflow_workflowcode,lsautoregister_regcode,"
					+ "elnmaterial_nmaterialcode,elnmaterialinventory_nmaterialinventorycode) "
					+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
       		 
			Calendar calendar = Calendar.getInstance();
	        calendar.setTime(lSlogilabprotocoldetail.getCreatedtimestamp());
	        Date createddate = calendar.getTime(); 
        
		           try (PreparedStatement pst = con.prepareStatement(updateString, Statement.RETURN_GENERATED_KEYS)) {
		               pst.setInt(1, lSlogilabprotocoldetail.getTestcode());
		               pst.setTimestamp(2, new Timestamp(createddate.getTime()));
		               pst.setString(3, lSlogilabprotocoldetail.getKeyword());
		               pst.setInt(4 , lSlogilabprotocoldetail.getProtocoltype());
		               pst.setInt(5,lSlogilabprotocoldetail.getLsprojectmaster().getProjectcode());
		              // pst.setInt(6, lSlogilabprotocoldetail.getLssamplemaster().getSamplecode());
		               pst.setInt(6,lSlogilabprotocoldetail.getLsuserMaster().getUsercode());
		               pst.setInt(7,lSlogilabprotocoldetail.getApproved());
		               pst.setInt(8, lSlogilabprotocoldetail.getVersionno());
		               pst.setInt(9, lSlogilabprotocoldetail.getCreateby());
		               pst.setInt(10,lSlogilabprotocoldetail.getSitecode());
		               pst.setInt(11,lSlogilabprotocoldetail.getViewoption());
		               pst.setString(12, lSlogilabprotocoldetail.getFileuid());
		               pst.setString(13, lSlogilabprotocoldetail.getFileuri());
		               pst.setInt(14, lSlogilabprotocoldetail.getContainerstored());
		               pst.setInt(15,lSlogilabprotocoldetail.getLsprotocolmaster().getProtocolmastercode());
		               pst.setBoolean(16, true);
		               pst.setString(17, "N");
		               pst.setInt(18,lSlogilabprotocoldetail.getElnprotocolworkflow().getWorkflowcode());
		               pst.setLong(19, lSlogilabprotocoldetail.getLsautoregister().getRegcode());
		             //  pst.setInt(20, lSlogilabprotocoldetail.getMaterial().getNmaterialcode());
		               pst.setObject(20, lSlogilabprotocoldetail.getElnmaterial().getNmaterialcode());
		               pst.setObject(21,lSlogilabprotocoldetail.getElnmaterialinventory().getNmaterialinventorycode());
		               
		               int affectedRows=pst.executeUpdate();
	                   
	                   if (affectedRows > 0) {
	                       // Retrieve the generated keys
	                	   ResultSet rs = pst.getGeneratedKeys();
	                       if (rs.next()) {
	                           long generateprotocolordercode = rs.getLong(1);
	                           System.out.println("Inserted record's protocolordercode: " + generateprotocolordercode);
	                           lSlogilabprotocoldetail.setProtocolordercode(generateprotocolordercode);
	                       }
	                   } else {
	                       System.out.println("No record inserted.");
	                   }
		           }
          
		           String deftemp = "update lsautoregister set batchcode = ? where regcode=?";
					
		   	 		try (PreparedStatement pst = con.prepareStatement(deftemp)) {
		            
		   	 		pst.setLong(1, lSlogilabprotocoldetail.getProtocolordercode());
		   	 		pst.setLong(2, lSlogilabprotocoldetail.getLsautoregister().getRegcode());
				     	
		   	 	    pst.executeUpdate();
		   	 	    lSlogilabprotocoldetail.getLsautoregister().setBatchcode(lSlogilabprotocoldetail.getProtocolordercode());

		   	 		}
		           if (lSlogilabprotocoldetail.getProtocolordercode() != null) {

						String ProtocolOrderName = "ELN" + lSlogilabprotocoldetail.getProtocolordercode();

						lSlogilabprotocoldetail.setProtoclordername(ProtocolOrderName);

						lSlogilabprotocoldetail.setOrderflag("N");

						//List<LSprotocolstep> protocolSteps = new ArrayList<LSprotocolstep>();
                       LSprotocolmaster protocolmasterobj = null;
//						String deftem = "SELECT * FROM LSProtocolStep WHERE protocolmastercode = ? and status = 1";
//	           	 		try (PreparedStatement pst = con.prepareStatement(deftem)) {
//	                    
//	           	 		pst.setInt(1, lSlogilabprotocoldetail.getLsprotocolmaster().getProtocolmastercode());
//	           	 		
//	           	 			try (ResultSet rs = pst.executeQuery()) {
//	                        while (rs.next()) {
//	                        	 protostepobj = mapResultSetToLsprotocolstep(rs);
//	                        	 protocolSteps.add(protostepobj);
//	                        }
//	                    } catch (SQLException e) {
//			                e.printStackTrace(); // Consider logging this properly
//			            }
//	                }
		               
						List<CloudLsLogilabprotocolstepInfo> objinfo = new ArrayList<CloudLsLogilabprotocolstepInfo>();
						List<LsLogilabprotocolstepInfo> objmongoinfo = new ArrayList<LsLogilabprotocolstepInfo>();
			
						String deftemppromast = "SELECT * FROM LSProtocolMaster WHERE protocolmastercode = ?";
	           	 		try (PreparedStatement pst = con.prepareStatement(deftemppromast)) {
	                    
	           	 		pst.setInt(1, lSlogilabprotocoldetail.getLsprotocolmaster().getProtocolmastercode());
	           	 		
	           	 			try (ResultSet rs = pst.executeQuery()) {
	                        while (rs.next()) {
	                        	protocolmasterobj = mapResultSetToLsprotocolmaster(rs);
	                        	 //protocolSteps.add(protostepobj);
	                        }
	                    } catch (SQLException e) {
			                e.printStackTrace(); // Consider logging this properly
			            }
	                  }
			
						if (lSlogilabprotocoldetail.getLsautoregister().getIsmultitenant() == 1
								|| lSlogilabprotocoldetail.getLsautoregister().getIsmultitenant() == 2) {
							if (protocolmasterobj.getContainerstored() == null
									&& lSlogilabprotocoldetail.getContent() != null
									&& !lSlogilabprotocoldetail.getContent().isEmpty()) {
								
								try {
									JSONObject protocolJson = new JSONObject(lSlogilabprotocoldetail.getContent());
									protocolJson.put("protocolname", lSlogilabprotocoldetail.getProtoclordername());
									updateProtocolOrderContent(protocolJson.toString(), lSlogilabprotocoldetail,
											lSlogilabprotocoldetail.getIsmultitenant(),configuration);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							} else {
								try {
									Content = objCloudFileManipulationservice.retrieveCloudSheets(
											protocolmasterobj.getFileuid(),
											commonfunction.getcontainername(lSlogilabprotocoldetail.getLsautoregister().getIsmultitenant(),
													(String) dataSource.getDataSourceProperties().getProperty("tenantName")) + "protocol");
									JSONObject protocolJson = new JSONObject(Content);
									protocolJson.put("protocolname", lSlogilabprotocoldetail.getProtoclordername());
									updateProtocolOrderContent(protocolJson.toString(), lSlogilabprotocoldetail,
											lSlogilabprotocoldetail.getLsautoregister().getIsmultitenant(),configuration);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						} else {
							GridFSDBFile data = gridFsTemplate.findOne(new Query(Criteria.where("filename")
									.is("protocol_" + protocolmasterobj.getProtocolmastercode())));
							if(data == null && lSlogilabprotocoldetail.getContent() != null && !lSlogilabprotocoldetail.getContent().isEmpty()) {
								JSONObject protocolJson = new JSONObject(lSlogilabprotocoldetail.getContent());
								protocolJson.put("protocolname", lSlogilabprotocoldetail.getProtoclordername());
								Content = protocolJson.toString();
							} else {
								Content = new BufferedReader(
										new InputStreamReader(data.getInputStream(), StandardCharsets.UTF_8)).lines()
										.collect(Collectors.joining("\n"));								
							}
							
							if (gridFsTemplate.findOne(new Query(Criteria.where("filename")
									.is("protocolorder_" + lSlogilabprotocoldetail.getProtocolordercode()))) == null) {
								try {
									gridFsTemplate.store(new ByteArrayInputStream(Content.getBytes(StandardCharsets.UTF_8)),
											"protocolorder_" + lSlogilabprotocoldetail.getProtocolordercode(),
											StandardCharsets.UTF_16);
								} catch (Exception e) {
									System.out.println("error protocoldata lslogilabprotocoldetail content update mongodb"
											+ lSlogilabprotocoldetail.getProtocolordercode());
								}
							}
						}
			
//						LsActiveWidgets lsActiveWidgets = null;
//					
//						Calendar widgetcalendar = Calendar.getInstance();
//						widgetcalendar.setTime(commonfunction.getCurrentUtcTime());
//				        Date widgetdate = widgetcalendar.getTime(); 
//						
//						String activewidgets = "Insert into LsActiveWidgets (activewidgetsdetails,activedatatimestamp,activewidgetsdetailscode,screenname)"
//								+ "values(?,?,?,?) ";
//	           	 		try (PreparedStatement pst = con.prepareStatement(activewidgets)) {
//	                    
//	           	 		pst.setString(1,lSlogilabprotocoldetail.getProtoclordername() );
//	           	 		pst.setTimestamp(2, new Timestamp(widgetdate.getTime()));
//	           	 		pst.setLong(3, lSlogilabprotocoldetail.getProtocolordercode());
//	           	 		pst.setString(4, "protocolorder");
//	           	 		
//	           	 	    pst.executeUpdate();
//	           	 }
						if(lSlogilabprotocoldetail.getRepeat() == true) {
							scheduleProtocolAutoRegisteration(lSlogilabprotocoldetail , configuration);
						}
						
							Calendar transcalendar = Calendar.getInstance();
					        calendar.setTime(commonfunction.getCurrentUtcTime());
					        Date transcalendardate = transcalendar.getTime(); 

							String comments = "order: "+lSlogilabprotocoldetail.getProtocolordercode()+" is now autoregistered";
							String systemcomments = "Audittrail.Audittrailhistory.Audittype.IDS_AUDIT_SYSTEMGENERATED";
									
							String auditquery = "INSERT INTO LScfttransaction(moduleName,actions,manipulatetype,transactiondate,comments,lssitemaster_sitecode,systemcoments,lsusermaster_usercode)"
									+ " VALUES (?,?,?,?,?,?,?,?) "; 
				                      
				
				               try (PreparedStatement pst = con.prepareStatement(auditquery)) {
				                   pst.setString(1, "IDS_SCN_PROTOCOLORDERS");
				                   pst.setString(2, "IDS_TSK_REGISTERED");
				                   pst.setString(3, "IDS_AUDIT_INSERTORDERS");
				                   pst.setTimestamp(4, new Timestamp(transcalendardate.getTime()));
				                   pst.setString(5, comments);
				                   pst.setInt(6, lSlogilabprotocoldetail.getSitecode());
				                   pst.setString(7, systemcomments);
				                   pst.setInt(8, lSlogilabprotocoldetail.getLsuserMaster().getUsercode());
				                   
				                   pst.executeUpdate();
				               }
						}
			
			 }
	     }
      }
   // }
    public void ExecuteAutoRegistration(LSlogilablimsorderdetail objorder , HikariConfig configuration)throws ParseException, SQLException, IOException {
    	if((objorder.getRepeat()!=null || objorder.getRepeat() != false) && objorder.getLsautoregisterorders()!=null) {
    		LSworkflow lsworkflow = null;
    		
	    	try (HikariDataSource dataSource = new HikariDataSource(configuration);
	                Connection con = dataSource.getConnection()) {
	    		
//	    		 String lsworkflowquery = "SELECT * FROM lsworkflow WHERE sitecode = ? ";
//
//	    		try (PreparedStatement pst = con.prepareStatement(lsworkflowquery)) {
//                    
//                 	pst.setInt(1, objorder.getLsuserMaster().getLssitemaster().getSitecode());
//                    
//                     try (ResultSet rs = pst.executeQuery()) {
//                         while (rs.next()) {
//                             //LSOrdernotification objNotification = mapResultSetToOrderNotification(rs);
//                             //scheduleNotificationForCaution(objNotification, configuration);
//                        	 lsworkflow = mapResultsetToLSworkflow(rs);
//                        	 objorder.setLsworkflow(lsworkflow);
//                         }
//                     } catch (SQLException e) {
// 		                e.printStackTrace(); // Consider logging this properly
// 		            }
//                 }
	    		
	    		
	    		objorder.setOrderflag("N");

	    		String Content = "";
	    		String defaultContent = "{\"activeSheet\":\"Sheet1\",\"sheets\":[{\"name\":\"Sheet1\",\"rows\":[],\"columns\":[],\"selection\":\"A1:A1\",\"activeCell\":\"A1:A1\",\"frozenRows\":0,\"frozenColumns\":0,\"showGridLines\":true,\"gridLinesColor\":null,\"mergedCells\":[],\"hyperlinks\":[],\"defaultCellStyle\":{\"fontFamily\":\"Arial\",\"fontSize\":\"12\"},\"drawings\":[]}],\"names\":[],\"columnWidth\":64,\"rowHeight\":20,\"images\":[],\"charts\":[],\"tags\":[],\"fieldcount\":0,\"Batchcoordinates\":{\"resultdirection\":1,\"parameters\":[]}}";
	    		LSfile fileobj = null;
	    		CloudSheetCreation cloudobject=null;
	    		
	    		LsAutoregister autoobj = new LsAutoregister();
				List<LsAutoregister> listauto = new ArrayList<LsAutoregister>();
				
					if(objorder.getLsautoregisterorders()!= null) {
				
						Date currentdate = commonfunction.getCurrentUtcTime();
						
						if(objorder.getLsautoregisterorders().getTimespan().equals("Days")) {
							//Date autodate=objorder.getLsautoregisterorders().getAutocreatedate();
							
							 Calendar calendar = Calendar.getInstance();
						        calendar.setTime(currentdate);
						        calendar.add(Calendar.DAY_OF_MONTH, objorder.getLsautoregisterorders().getInterval());

						        // Convert back to Date (if necessary)
						        Date futureDate = calendar.getTime();   
						        //autoordersfilter.get(0).setAutocreatedate(futureDate);
						        autoobj.setAutocreatedate(futureDate);
						 }else if(objorder.getLsautoregisterorders().getTimespan().equals("Week")) {
							 //Date autodate=objorder.getLsautoregisterorders().getAutocreatedate();
								
							    Calendar calendar = Calendar.getInstance();
						        calendar.setTime(currentdate);
						        calendar.add(Calendar.DAY_OF_MONTH, (objorder.getLsautoregisterorders().getInterval()*7));

						      
						        Date futureDate = calendar.getTime();   
						        
						        autoobj.setAutocreatedate(futureDate);
						 }else {
							
							    Calendar calendar = Calendar.getInstance();
						        calendar.setTime(currentdate);
						        calendar.add(Calendar.HOUR_OF_DAY,(objorder.getLsautoregisterorders().getInterval()));
						        Date futureDate = calendar.getTime(); 
						        autoobj.setAutocreatedate(futureDate);
						        
//							    Calendar calendar = Calendar.getInstance();
//						        calendar.setTime(currentdate);
//						        calendar.add(Calendar.MINUTE , (10));
//						        Date futureDate = calendar.getTime();   
//						        autoobj.setAutocreatedate(futureDate);
						 }
						
						autoobj.setScreen("IDS_SHEETORDERS");
						autoobj.setIsautoreg(true);
						autoobj.setInterval(objorder.getLsautoregisterorders().getInterval());
						autoobj.setTimespan(objorder.getLsautoregisterorders().getTimespan());
						autoobj.setIsmultitenant(objorder.getLsautoregisterorders().getIsmultitenant());
						autoobj.setRepeat(true);
						objorder.setLsautoregisterorders(autoobj);
						
					}
			
				//lsautoregisterrepo.save(autoobj);
					
					 Calendar autocalendar = Calendar.getInstance();
				        autocalendar.setTime(autoobj.getAutocreatedate());
				        Date autocreateddate = autocalendar.getTime(); 
				        
				String autoregtablequery = "Insert into lsautoregister (autocreatedate,interval,isautoreg,"
						+ "ismultitenant,repeat,screen,timespan) Values (?,?,?,?,?,?,?)";

                try (PreparedStatement pst = con.prepareStatement(autoregtablequery, Statement.RETURN_GENERATED_KEYS)) {
                   
                	pst.setTimestamp(1, new Timestamp(autocreateddate.getTime()));
                	pst.setInt(2, autoobj.getInterval());
                	pst.setBoolean(3, autoobj.getIsautoreg());
                	pst.setInt(4, autoobj.getIsmultitenant());
                	pst.setBoolean(5, autoobj.getRepeat());
                	pst.setString(6, autoobj.getScreen());
                	pst.setString(7, autoobj.getTimespan());
                   
                	int affectedRows=pst.executeUpdate();
	                   
	                   if (affectedRows > 0) {
	                       // Retrieve the generated keys
	                	   ResultSet rs = pst.getGeneratedKeys();
	                       if (rs.next()) {
	                           long generatedregcode = rs.getLong(1);
	                           System.out.println("Inserted record's regcode: " + generatedregcode);
	                           objorder.getLsautoregisterorders().setRegcode(generatedregcode);

	                           // Use the generated key for future use
	                       }
	                   } else {
	                       System.out.println("No record inserted.");
	                   }
                }
                
                String deftem = "update LSlogilablimsorderdetail set repeat=false WHERE batchcode=?";
      	 		try (PreparedStatement pst = con.prepareStatement(deftem)) {
      	 			pst.setLong(1, objorder.getBatchcode());
      	 			pst.executeUpdate();
              }
				
	    		if (objorder.getLsfile() != null) {
	    			 String lsfilequery = "SELECT * FROM LSfile WHERE filecode = ? ";

	                 try (PreparedStatement pst = con.prepareStatement(lsfilequery)) {
	                    
	                 	pst.setInt(1, objorder.getLsfile().getFilecode());
	                    
	                     try (ResultSet rs = pst.executeQuery()) {
	                         while (rs.next()) {
	                        	  fileobj = mapResultsetToLSFile(rs);
	                        	  objorder.setLsfile(fileobj);
	                         }
	                     } catch (SQLException e) {
	 		                e.printStackTrace(); // Consider logging this properly
	 		            }
	                 }
	                 
	                 String cloudquery = "SELECT * FROM LSSheetCreationfiles WHERE id = ? ";

	                 try (PreparedStatement pst = con.prepareStatement(cloudquery)) {
		                    
		                 	pst.setLong(1, fileobj.getFilecode());
		                    
		                     try (ResultSet rs = pst.executeQuery()) {
		                         while (rs.next()) {
		                    	     cloudobject = mapResultsetToCloudSheet(rs);
		                    	     
		                         }
		                     } catch (SQLException e) {
		 		                e.printStackTrace(); // Consider logging this properly
		 		            }
		                 }
	                 
	                 
	                 if ((objorder.getLsfile().getApproved() != null && objorder.getLsfile().getApproved() == 1)
	     					|| (objorder.getFiletype() == 5)) {
	                	 if (objorder.getLsautoregisterorders().getIsmultitenant() == 1 || objorder.getLsautoregisterorders().getIsmultitenant() == 2) {

	     					if (cloudobject != null) {
	     						if (cloudobject.getContainerstored() == 0) {
	     							Content = cloudobject.getContent();
	     						} else {
	     							Content = objCloudFileManipulationservice.retrieveCloudSheets(cloudobject.getFileuid(),
	     									commonfunction.getcontainername(objorder.getLsautoregisterorders().getIsmultitenant(), (String) dataSource.getDataSourceProperties().getProperty("tenantName"))
	     									 + "sheetcreation");
	     						}
	     					} else {
	     						Content = objorder.getLsfile().getFilecontent();
	     					}
	     				} 
	                	 else 
	     				{

	     					GridFSDBFile largefile = gridFsTemplate.findOne(
	     							new Query(Criteria.where("filename").is("file_" + objorder.getLsfile().getFilecode())));
	     					if (largefile == null) {
	     						largefile = gridFsTemplate.findOne(
	     								new Query(Criteria.where("_id").is("file_" + objorder.getLsfile().getFilecode())));
	     					}

	     					if (largefile != null) {
	     						Content = new BufferedReader(
	     								new InputStreamReader(largefile.getInputStream(), StandardCharsets.UTF_8)).lines()
	     								.collect(Collectors.joining("\n"));
	     					} else {
	     						if (mongoTemplate.findById(objorder.getLsfile().getFilecode(), SheetCreation.class) != null) {
	     							Content = mongoTemplate.findById(objorder.getLsfile().getFilecode(), SheetCreation.class)
	     									.getContent();
	     						} else {
	     							Content = objorder.getLsfile().getFilecontent();
	     						}
	     					}

	     				}
	                	 
	                 }else {
	     				if (objorder.getFiletype() != 4 && objorder.getLsfile().getFilecode() != 1) {
	    					Integer lastapprovedvesrion = objorder.getLsfile().getVersionno() > 1
	    							? (objorder.getLsfile().getVersionno() - 1)
	    							: objorder.getLsfile().getVersionno();
	    					objorder.getLsfile().setVersionno(lastapprovedvesrion);
	    					objorder.getLsfile().setIsmultitenant(objorder.getIsmultitenant());
	    					objorder.getLsfile().setIsmultitenant(objorder.getIsmultitenant());
	    					//Content = fileService.GetfileverContent(objorder.getLsfile());
	    				}
	    			}
	                 
	                 try {
	         			objorder.setCreatedtimestamp(commonfunction.getCurrentUtcTime());
	         		} catch (ParseException e) {
	         			// TODO Auto-generated catch block
	         			e.printStackTrace();
	         		}
	                 Calendar calendar = Calendar.getInstance();
				        calendar.setTime(objorder.getCreatedtimestamp());
				        Date createddate = calendar.getTime(); 
				        
//	                 String updateString = "INSERT INTO lslogilablimsorderdetail (approvelaccept ,sentforapprovel,approvelstatus"
//	                 		+ "approved,repeat,filetype,lsworkflow_workflowcode,lsusermaster_usercode,"
//	                 		+ "lsfile_filecode,lsprojectmaster_projectcode,lssamplemaster_samplecode,lssamplefile_filesamplecode,"
//	                 		+ "lsrepositoriesdata_repositorydatacode,filecode,keyword,lockedusername,directorycode,orderdisplaytype,"
//	                 		+ "lstestmasterlocal_testcode,viewoption,ordercancell,teamcode,material_nmaterialcode,materialinventory_nmaterialinventorycode,"
//	                 		+ "elnmaterial_nmaterialcode,lsordernotification_notificationcode,lsAutoregisterorders_regcode) "
//	                 		+ "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
	                 objorder.setBatchcode(null);
	                 objorder.setBatchid(null);
	                 
	                 String updateString = "INSERT INTO lslogilablimsorderdetail ("
		                 		+ "repeat,filetype,lsworkflow_workflowcode,lsusermaster_usercode,"
		                 		+ "lsfile_filecode,lsprojectmaster_projectcode,lssamplefile_filesamplecode,"
		                 		+ "filecode,keyword,lockedusername,directorycode,orderdisplaytype,"
		                 		+ "lstestmasterlocal_testcode,viewoption,ordercancell,teamcode,createdtimestamp,orderflag,"
		                 		+ "lsautoregisterorders_regcode,testcode,testname,"
		                 		+ "elnmaterialinventory_nmaterialinventorycode,elnmaterial_nmaterialcode) "
		                 		+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	                		 
	                		 
		               try (PreparedStatement pst = con.prepareStatement(updateString, Statement.RETURN_GENERATED_KEYS)) {
		                   pst.setBoolean(1, true);
		                   pst.setInt(2, objorder.getFiletype());
		                   pst.setInt(3, objorder.getLsworkflow().getWorkflowcode());
		                   pst.setInt(4, objorder.getLsuserMaster().getUsercode());
		                   pst.setInt(5,objorder.getLsfile().getFilecode());
		                   pst.setInt(6,objorder.getLsprojectmaster().getProjectcode());
		                  // pst.setInt(7, objorder.getLssamplemaster().getSamplecode());
		                   pst.setInt(7, objorder.getLssamplefile().getFilesamplecode());
		                   //pst.setInt(9, objorder.getLsrepositoriesdata().getRepositorydatacode());
		                   pst.setInt(8, objorder.getFilecode());
		                   pst.setString(9,objorder.getKeyword());
		                   pst.setString(10, objorder.getLockedusername());
		                   pst.setFloat(11, objorder.getDirectorycode());
		                   pst.setInt(12, objorder.getOrderdisplaytype());
		                   pst.setInt(13,objorder.getLstestmasterlocal().getTestcode());
		                   pst.setInt(14,objorder.getViewoption());
		                   pst.setObject(15,objorder.getOrdercancell());
		                   pst.setInt(16, objorder.getTeamcode());
		                   pst.setTimestamp(17, new Timestamp(createddate.getTime()));
		                   pst.setString(18, objorder.getOrderflag());
		                   pst.setLong(19, objorder.getLsautoregisterorders().getRegcode());
		                   //pst.setObject(20, objorder.getMaterial().getNmaterialcode());
		                   pst.setInt(20,objorder.getTestcode());
		                   pst.setString(21, objorder.getTestname());
		                   pst.setObject(22, objorder.getElnmaterialinventory().getNmaterialinventorycode());
		                   pst.setObject(23, objorder.getElnmaterial().getNmaterialcode());
		                   
		                   
		
		                   int affectedRows=pst.executeUpdate();
		                   
		                   if (affectedRows > 0) {
		                       // Retrieve the generated keys
		                	   ResultSet rs = pst.getGeneratedKeys();
		                       if (rs.next()) {
		                           long generatedBatchcode = rs.getLong(1);
		                           System.out.println("Inserted record's batchcode: " + generatedBatchcode);
		                           objorder.setBatchcode(generatedBatchcode);

		                           // Use the generated key for future use
		                       }
		                   } else {
		                       System.out.println("No record inserted.");
		                   }
		               }
		               String Batchid = "ELN" + objorder.getBatchcode();
		       		if (objorder.getFiletype() == 3) {
		       			Batchid = "RESEARCH" + objorder.getBatchcode();
		       		} else if (objorder.getFiletype() == 4) {
		       			Batchid = "EXCEL" + objorder.getBatchcode();
		       		} else if (objorder.getFiletype() == 5) {
		       			Batchid = "VALIDATE" + objorder.getBatchcode();
		       		}
		       		
		       		objorder.setBatchid(Batchid);

		       		String updateString2 = "UPDATE lslogilablimsorderdetail SET Batchid = ? WHERE batchcode = ? ;"
		       				+ "UPDATE lsautoregister set Batchcode = ? where regcode=?";
		
		               try (PreparedStatement pst = con.prepareStatement(updateString2)) {
		            	   pst.setString(1, objorder.getBatchid());
		            	   pst.setLong(2, objorder.getBatchcode());
		            	   pst.setLong(3,objorder.getBatchcode());
		            	   pst.setLong(4, objorder.getLsautoregisterorders().getRegcode());
		            	   
		            	   pst.executeUpdate();
		               }
		               
		               if(objorder.getRepeat() == true) {
		            	   scheduleAutoRegisteration(objorder , configuration);
		               }
		               
		               Calendar transcalendar = Calendar.getInstance();
				        calendar.setTime(commonfunction.getCurrentUtcTime());
				        Date transcalendardate = transcalendar.getTime(); 

						String comments = "order: "+objorder.getBatchcode()+" is now autoregistered";
						String systemcomments = "Audittrail.Audittrailhistory.Audittype.IDS_AUDIT_SYSTEMGENERATED";
								
						String auditquery = "INSERT INTO LScfttransaction(moduleName,actions,manipulatetype,transactiondate,comments,lssitemaster_sitecode,systemcoments,lsusermaster_usercode)"
								+ " VALUES (?,?,?,?,?,?,?,?) "; 
			                      
			
			               try (PreparedStatement pst = con.prepareStatement(auditquery)) {
			                   pst.setString(1, "IDS_SCN_SHEETORDERS");
			                   pst.setString(2, "IDS_TSK_REGISTERED");
			                   pst.setString(3, "IDS_AUDIT_INSERTORDERS");
			                   pst.setTimestamp(4, new Timestamp(transcalendardate.getTime()));
			                   pst.setString(5, comments);
			                   pst.setInt(6, 1);
			                   pst.setString(7, systemcomments);
			                   pst.setInt(8, objorder.getLsuserMaster().getUsercode());
			                   
			                   pst.executeUpdate();
			               }
	    		}
	    		
	    	}
    	}
    }
    
    public void executecautiondatenotification(LSOrdernotification objNotification, HikariConfig configuration) throws ParseException {
    	if(objNotification.getIscompleted() == null || objNotification.getIscompleted() == false) {
	    	try (HikariDataSource dataSource = new HikariDataSource(configuration);
	                Connection con = dataSource.getConnection()) {
	
		    	   LSuserMaster LSuserMaster = new LSuserMaster();
		    	   LSuserMaster.setUsercode(objNotification.getUsercode());
	    		
	               LocalDateTime localDateTime = LocalDateTime.now();
	               Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
	               Date cDate = Date.from(instant);
	
	               Date cautionDate = objNotification.getCautiondate();
	   			   Instant caution = cautionDate.toInstant();
	   			
		   		   LocalDateTime cautionTime = LocalDateTime.ofInstant(caution, ZoneId.systemDefault());
		   		   LocalDate cautiondate = cautionTime.toLocalDate();
	               
	               String Details = "{\"ordercode\" :\"" + objNotification.getBatchid() 
			        + "\",\"order\" :\"" + objNotification.getBatchid() 
			        + "\",\"date\" :\"" + cautiondate 
			        + "\",\"screen\":\"" + objNotification.getScreen() 
					+ "\"}";
	               String path = objNotification.getScreen().equals("sheetorder") ? "/registertask" : "/Protocolorder";
	
	               String updateString = "INSERT INTO lsnotification(isnewnotification, notification, " +
	                       "createdtimestamp, notificationdetils, notificationpath, notifationfrom_usercode, " +
	                       "notifationto_usercode, repositorycode, repositorydatacode, notificationfor) VALUES (1, 'ORDERCAUTIONALERT', ?, ?, ?, ?, ?, 0, 0, 1); " 
	                       +
	                       "UPDATE LSORDERNOTIFICATION SET cautionstatus = 0 WHERE notificationcode = ?";
	
	               try (PreparedStatement pst = con.prepareStatement(updateString)) {
	                   pst.setTimestamp(1, new Timestamp(cDate.getTime()));
	                   pst.setString(2, Details);
	                   pst.setString(3, path);
	                   pst.setInt(4, objNotification.getUsercode());
	                   pst.setInt(5, objNotification.getUsercode());
	                   pst.setLong(6, objNotification.getNotificationcode());
	
	                   pst.executeUpdate();
	               }
	           } catch (SQLException e) {
	               e.printStackTrace(); // Consider logging this properly
	           }
    	}
    	//notifyoverduedays(objNotification,configuration);
    }
    
    public void executeduedatenotification(LSOrdernotification objNotification, HikariConfig configuration) throws ParseException {
    	if(objNotification.getIscompleted() == null || objNotification.getIscompleted() == false) {
	    	try (HikariDataSource dataSource = new HikariDataSource(configuration);
	                Connection con = dataSource.getConnection()) {
	
	               LocalDateTime localDateTime = LocalDateTime.now();
	               Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
	               Date cDate = Date.from(instant);
	
	               Date dueDate = objNotification.getDuedate();
	           	   Instant due = dueDate.toInstant();
	           	
	           	   LocalDateTime dueTime = LocalDateTime.ofInstant(due, ZoneId.systemDefault());
	           	   LocalDate duedate = dueTime.toLocalDate();
	           	
	               String Details = "{\"ordercode\" :\"" + objNotification.getBatchid() 
			        + "\",\"order\" :\"" + objNotification.getBatchid() 
			        + "\",\"date\" :\"" + duedate 
			        + "\",\"screen\":\"" + objNotification.getScreen() 
					+ "\"}";
	               
	               String path = objNotification.getScreen().equals("sheetorder") ? "/registertask" : "/Protocolorder";
	
	               String updateString = "INSERT INTO public.lsnotification( isnewnotification, notification, " +
	                       "createdtimestamp, notificationdetils, notificationpath, notifationfrom_usercode, " +
	                       "notifationto_usercode, repositorycode, repositorydatacode, notificationfor) VALUES ( 1, 'ORDERONDUEALERT', ?, ?, ?, ?, ?, 0, 0, 1); " +
	                       "UPDATE LSORDERNOTIFICATION SET duestatus = 0 WHERE notificationcode = ?";
	
	               try (PreparedStatement pst = con.prepareStatement(updateString)) {
	                   pst.setTimestamp(1, new Timestamp(cDate.getTime()));
	                   pst.setString(2, Details);
	                   pst.setString(3, path);
	                   pst.setInt(4, objNotification.getUsercode());
	                   pst.setInt(5, objNotification.getUsercode());
	                   pst.setLong(6, objNotification.getNotificationcode());
	
	                   pst.executeUpdate();
	               }
	           } catch (SQLException e) {
	               e.printStackTrace(); // Consider logging this properly
	           }
    	}
    	
    }
    public void executeoverduenotification(LSOrdernotification objNotification, HikariConfig configuration) throws ParseException {
    	if(objNotification.getIscompleted() == null || objNotification.getIscompleted() == false) {
	    	try (HikariDataSource dataSource = new HikariDataSource(configuration);
	                Connection con = dataSource.getConnection()) {
	
	               LocalDateTime localDateTime = LocalDateTime.now();
	               Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
	               Date cDate = Date.from(instant);
	
	               Date dueDate = objNotification.getDuedate();
	           	   Instant due = dueDate.toInstant();
	           	
	           	   LocalDateTime dueTime = LocalDateTime.ofInstant(due, ZoneId.systemDefault());
	           	   LocalDate duedate = dueTime.toLocalDate();
	           	
	               String Details = "{\"ordercode\" :\"" + objNotification.getBatchid() 
			        + "\",\"order\" :\"" + objNotification.getBatchid()
			        + "\",\"days\" :\"" + objNotification.getOverduedays()
			        + "\",\"date\" :\"" + duedate
			        + "\",\"screen\":\"" + objNotification.getScreen() 
					+ "\"}";
	               
	               String path = objNotification.getScreen().equals("sheetorder") ? "/registertask" : "/Protocolorder";
	
	               String updateString = "INSERT INTO public.lsnotification( isnewnotification, notification, " +
	                       "createdtimestamp, notificationdetils, notificationpath, notifationfrom_usercode, " +
	                       "notifationto_usercode, repositorycode, repositorydatacode, notificationfor) VALUES (1, 'ORDEROVERDUEALERT', ?, ?, ?, ?, ?, 0, 0, 1); " +
	                       "UPDATE LSORDERNOTIFICATION SET overduestatus = 0 , isduedateexhausted = true WHERE notificationcode = ?";
	
	               
	               try (PreparedStatement pst = con.prepareStatement(updateString)) {
	                   pst.setTimestamp(1, new Timestamp(cDate.getTime()));
	                   pst.setString(2, Details);
	                   pst.setString(3, path);
	                   pst.setInt(4, objNotification.getUsercode());
	                   pst.setInt(5, objNotification.getUsercode());
	                   pst.setLong(6, objNotification.getNotificationcode());
	
	                   pst.executeUpdate();
	               }
	           } catch (SQLException e) {
	               e.printStackTrace(); // Consider logging this properly
	           }
    	}
    	notifyoverduedays(objNotification,configuration);
    }
    
    public void notifyoverduedays(LSOrdernotification objNotification, HikariConfig configuration) throws ParseException {
    	
    	if(objNotification.getIscompleted() == null || objNotification.getIscompleted() == false) {
    			Date Date1=objNotification.getDuedate();
    			Date Date2=new Date();
    	 
    	        Instant instant1 = Date1.toInstant();
    	        Instant instant2 = Date2.toInstant();		        
    	        LocalDateTime dateTime1 = LocalDateTime.ofInstant(instant1, ZoneId.systemDefault());
    	        LocalDateTime dateTime2 = LocalDateTime.ofInstant(instant2, ZoneId.systemDefault());
    	        
    	        Duration duration = Duration.between(dateTime1, dateTime2);
    	        
    	        long totalHours = duration.toHours();
    	        long days = totalHours / 24;
    	        long hours = totalHours % 24;

    	        System.out.println("Difference: " + days + " days and " + hours + " hours");
    	  
    	        if(days == 0 && hours!=0) {
    		        String diffdays=(int)hours+" Hours";
    		        objNotification.setOverduedays(diffdays);
    	        }else if(days == 0 && hours==0) {
    	        	String diffdays="<1hour";
    	        	objNotification.setOverduedays(diffdays);
    	        }
    	        else {
    	        	String diffdays=(int)days+"Days "+(int)hours+"Hours ";

    	        	objNotification.setOverduedays(diffdays);
    	        }
    	   
    	        try (HikariDataSource dataSource = new HikariDataSource(configuration);
    	                Connection con = dataSource.getConnection()) {
    	       
    	        	String updateString =  "UPDATE lsordernotification SET overduedays = ? WHERE notificationcode = ?";
    	        	try (PreparedStatement pst = con.prepareStatement(updateString)) {
    	            //    pst.setTimestamp(1, new Timestamp(cDate.getTime()));
    	                pst.setString(1, objNotification.getOverduedays());
    	                pst.setLong(2, objNotification.getNotificationcode());
    	                pst.executeUpdate();
    	            }
    	        }
    	        catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    	}
    }
    
    public void executeNotificationPop(Notification notification, HikariConfig configuration) throws SQLException {
        try (HikariDataSource dataSource = new HikariDataSource(configuration);
             Connection con = dataSource.getConnection()) {

            LocalDateTime localDateTime = LocalDateTime.now();
            Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
            Date cDate = Date.from(instant);

            String details = "{\"ordercode\" :\"" + notification.getOrderid() + "\",\"order\" :\""
                    + notification.getBatchid() + "\",\"description\":\"" + notification.getDescription()
                    + "\",\"screen\":\"" + notification.getScreen() + "\"}";
            String path = notification.getScreen().equals("Sheet Order") ? "/registertask" : "/Protocolorder";

            String updateString = "INSERT INTO public.lsnotification(isnewnotification, notification, " +
                    "createdtimestamp, notificationdetils, notificationpath, notifationfrom_usercode, " +
                    "notifationto_usercode, repositorycode, repositorydatacode, notificationfor) VALUES ( 1, 'CAUTIONALERT', ?, ?, ?, ?, ?, 0, 0, 1); " +
                    "UPDATE Notification SET status = 0 WHERE notificationid = ?";

            try (PreparedStatement pst = con.prepareStatement(updateString)) {
                pst.setTimestamp(1, new Timestamp(cDate.getTime()));
                pst.setString(2, details);
                pst.setString(3, path);
                pst.setInt(4, notification.getUsercode());
                pst.setInt(5, notification.getUsercode());
                pst.setLong(6, notification.getNotificationid());

                pst.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Consider logging this properly
        }
    }
}