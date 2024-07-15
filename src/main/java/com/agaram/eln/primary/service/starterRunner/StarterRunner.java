package com.agaram.eln.primary.service.starterRunner;

import java.io.BufferedReader;
import java.util.Set;
import java.util.HashSet;
import java.util.Timer;
import java.util.TimerTask;
import java.sql.SQLException;
import java.text.ParseException;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
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
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import com.agaram.eln.primary.commonfunction.commonfunction;
import com.agaram.eln.primary.fetchtenantsource.Datasourcemaster;
import com.agaram.eln.primary.model.cloudFileManip.CloudSheetCreation;
import com.agaram.eln.primary.model.dashboard.LsActiveWidgets;
import com.agaram.eln.primary.model.general.OrderCreation;
import com.agaram.eln.primary.model.general.SheetCreation;
import com.agaram.eln.primary.model.instrumentDetails.LSOrdernotification;
import com.agaram.eln.primary.model.instrumentDetails.LSlogilablimsorderdetail;
import com.agaram.eln.primary.model.instrumentDetails.LsAutoregister;
import com.agaram.eln.primary.model.masters.Lsrepositories;
import com.agaram.eln.primary.model.masters.Lsrepositoriesdata;
import com.agaram.eln.primary.model.material.Elnmaterial;
import com.agaram.eln.primary.model.material.ElnmaterialInventory;
import com.agaram.eln.primary.model.protocols.ElnprotocolTemplateworkflow;
import com.agaram.eln.primary.model.protocols.Elnprotocolworkflow;
import com.agaram.eln.primary.model.protocols.LSlogilabprotocoldetail;
import com.agaram.eln.primary.model.protocols.LSprotocolmaster;
import com.agaram.eln.primary.model.protocols.LSprotocolstep;
import com.agaram.eln.primary.model.protocols.LSprotocolworkflow;
import com.agaram.eln.primary.model.sheetManipulation.LSfile;
import com.agaram.eln.primary.model.sheetManipulation.LSsamplefile;
import com.agaram.eln.primary.model.sheetManipulation.LSsamplefileversion;
import com.agaram.eln.primary.model.sheetManipulation.LSsamplemaster;
import com.agaram.eln.primary.model.sheetManipulation.LSsheetworkflow;
import com.agaram.eln.primary.model.sheetManipulation.LStestmasterlocal;
import com.agaram.eln.primary.model.sheetManipulation.LSworkflow;
import com.agaram.eln.primary.model.sheetManipulation.Notification;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.usermanagement.LSprojectmaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.repository.multitenant.DataSourceConfigRepository;
import com.agaram.eln.primary.service.cloudFileManip.CloudFileManipulationservice;
import com.mongodb.gridfs.GridFSDBFile;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;


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
	
//	@Autowired
//	private InstrumentService instservice;
	
    private final ConcurrentMap<Integer, TimerTask> scheduledTasks = new ConcurrentHashMap<>();

    private static final int MAX_POOL_SIZE = 10;
    private static final int MIN_IDLE = 5;
    private static final int CONNECTION_TIMEOUT = 120000;
    private static final int CONNECTION_THRESHOLD = 120000;

    Date currentdate = null;
    Date gettoDate=null;
    Date getfromDate=null;
    private static String defaultContent = "{\"activeSheet\":\"Sheet1\",\"sheets\":[{\"name\":\"Sheet1\",\"rows\":[],\"columns\":[],\"selection\":\"A1:A1\",\"activeCell\":\"A1:A1\",\"frozenRows\":0,\"frozenColumns\":0,\"showGridLines\":true,\"gridLinesColor\":null,\"mergedCells\":[],\"hyperlinks\":[],\"defaultCellStyle\":{\"fontFamily\":\"Arial\",\"fontSize\":\"12\"},\"drawings\":[]}],\"names\":[],\"columnWidth\":64,\"rowHeight\":20,\"images\":[],\"charts\":[],\"tags\":[],\"fieldcount\":0,\"Batchcoordinates\":{\"resultdirection\":1,\"parameters\":[]}}";
	
    public void executeOnStartup() throws Exception {

        System.out.println("Task executed on startup");
        new Thread(() -> {
			try {
				System.out.println("auto register for sheet");
				checkAndScheduleautoOrderRegister();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();
        
        new Thread(() -> {
			try {
				System.out.println("reminder alert concept");
				checkAndScheduleReminders();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();
    
        
        new Thread(() -> {
			try {
				System.out.println("reminder for orders");
				checkAndScheduleRemindersforOrders();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();
        
      
        new Thread(() -> {
			try {
				System.out.println("auto register for protocol");
				checkAndScheduleProtocolautoRegister();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();
        
        //checkAndScheduleReminders();
        //checkAndScheduleRemindersforOrders();
        //checkAndScheduleautoOrderRegister();
       // checkAndScheduleProtocolautoRegister();      
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
    	
    	ordernot.setApprovelaccept(rs.getString("approvelaccept"));
    	ordernot.setSentforapprovel(rs.getBoolean("sentforapprovel"));
    	//ordernot.setLockedusername(rs.getString("lockedusername"));
    	ordernot.setRepeat(rs.getBoolean("repeat"));
    	ordernot.setAutoregistercount(rs.getInt("autoregistercount")-1);
    	
        return ordernot;
    }
    public LSlogilablimsorderdetail mapResultSetToLslogilabOrder (ResultSet rs) throws SQLException {
    	LSlogilablimsorderdetail orderobj = new LSlogilablimsorderdetail();
    	orderobj.setBatchcode(rs.getLong("batchcode"));
    	orderobj.setBatchid(rs.getString("batchid"));
    	orderobj.setApprovelaccept(rs.getString("approvelaccept"));
    	orderobj.setSentforapprovel(rs.getBoolean("sentforapprovel"));
    	orderobj.setApprovelstatus(rs.getInt("approvelstatus"));
    	orderobj.setApproved(rs.getInt("approved"));
    	orderobj.setRepeat(rs.getBoolean("repeat"));
    	orderobj.setFiletype(rs.getInt("filetype"));
    	
    	LSworkflow lstworkflow = new LSworkflow();
    	lstworkflow.setWorkflowcode(rs.getInt("lsworkflow_workflowcode"));
    	orderobj.setLsworkflow(lstworkflow);
    	
    	//orderobj.setLockeduser(rs.getInt("lockeduser"));
    	
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
    	//orderobj.setLockedusername(rs.getString("lockedusername"));
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
    	orderobj.setAutoregistercount(rs.getInt("autoregistercount")-1);
    	
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
   
    public void getCurrentUTCDate() throws Exception {
    	currentdate = commonfunction.getCurrentUtcTime();
	}
    
    public void gettofromdate() {

        LocalDateTime localDateTime = LocalDateTime.now();
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
         gettoDate = Date.from(instant);
         
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY , +12);
        gettoDate=calendar.getTime();
      
//        LocalDateTime  previousDate = localDateTime.minusDays(1);
//        Instant preinstant = previousDate.atZone(ZoneId.systemDefault()).toInstant();
 //       getfromDate=Date.from(preinstant);  
        
        Calendar fromcalendar = Calendar.getInstance();
        fromcalendar.add(Calendar.HOUR_OF_DAY, -12);
        getfromDate=fromcalendar.getTime();
    }
    
    public void checkAndScheduleProtocolautoRegister() throws Exception {
    	List<Datasourcemaster> configList = configRepo.findByinitialize(true);

        for (Datasourcemaster objData : configList) {
            HikariConfig configuration = createHikariConfig(objData);
            try (HikariDataSource dataSource = new HikariDataSource(configuration);
                    Connection con = dataSource.getConnection()) {
            	
            	String checkrepeat = "SELECT * FROM lslogilabprotocoldetail WHERE repeat = true and autoregistercount > 0";
            	 try (PreparedStatement pst = con.prepareStatement(checkrepeat)) {
                     
                 	// pst.setTimestamp(1, new Timestamp(getfromDate.getTime()));
                   //  pst.setTimestamp(2, new Timestamp(gettoDate.getTime()));

                     try (ResultSet rs = pst.executeQuery()) {
                         while (rs.next()) {
                        	 LSlogilabprotocoldetail orderobj = mapResultSetToOrderLSlogilabprotocoldetail(rs);
                             //scheduleProtocolAutoRegisteration(orderobj, configuration,currentdate);
                        	 checkinprotorange(orderobj, configuration);
                         }
                     } catch (SQLException e) {
 		                e.printStackTrace(); // Consider logging this properly
 		            }
                 }
            	 con.close();
            }	
        }
    }
    private void checkinprotorange(LSlogilabprotocoldetail orderobj, HikariConfig configuration) throws Exception {
    	
    	gettofromdate();
    	getCurrentUTCDate();
    	
    	try (HikariDataSource dataSource = new HikariDataSource(configuration);
                Connection con = dataSource.getConnection()) {

    		 LsAutoregister objlsauto = null ;	
    		 String autoregquery = "SELECT * FROM Lsautoregister WHERE batchcode=? and screen=?";

             try (PreparedStatement pst = con.prepareStatement(autoregquery)) {
                
             	pst.setLong(1, orderobj.getProtocolordercode());
             	pst.setString(2, "Protocol_Order");
              
                 try (ResultSet rs = pst.executeQuery()) {
                     while (rs.next()) {
                     	 objlsauto = mapResultSetToLsAutoregister(rs);
                     	 orderobj.setLsautoregisterorder(objlsauto);
                     	 orderobj.setLsautoregister(objlsauto);      
                     	 
                     	Date Autoregdate = objlsauto.getAutocreatedate();
            	    	Instant auto = Autoregdate.toInstant();
            	        LocalDateTime autoTime = LocalDateTime.ofInstant(auto, ZoneId.systemDefault());
            	        
            	        Instant fromdate = getfromDate.toInstant();
            	        LocalDateTime fromdateTime = LocalDateTime.ofInstant(fromdate, ZoneId.systemDefault());
            	        
            	        Instant todate = gettoDate.toInstant();
            	        LocalDateTime todateTime = LocalDateTime.ofInstant(todate, ZoneId.systemDefault());
            	
                     	 if(autoTime.isAfter(fromdateTime) && autoTime.isBefore(todateTime)) {
                     			//if (dateTime.isAfter(startDateTime) && dateTime.isBefore(endDateTime)) { 	
                     		scheduleProtocolAutoRegisteration(orderobj, configuration,currentdate,autoTime);
                     	 }
                     
                     	    Autoregdate=null;
		            	    auto=null;
		            	    autoTime=null;
                      }    
                 } catch (SQLException e) {
		                e.printStackTrace(); // Consider logging this properly
		            }
             }
             con.close();             
    	}
    }
    private void scheduleProtocolAutoRegisteration(LSlogilabprotocoldetail orderobj, HikariConfig configuration , Date currentdate,LocalDateTime autoTime) throws SQLException, IOException {	 
    	
    	  LSprotocolmaster protocolmasterobj = null;

    	 try (HikariDataSource dataSource = new HikariDataSource(configuration);
                 Connection con = dataSource.getConnection()) {

               
                String deftemppromast = "SELECT * FROM LSProtocolMaster WHERE protocolmastercode = ?";
				
       	 		try (PreparedStatement pst = con.prepareStatement(deftemppromast)) {
                
       	 		pst.setInt(1, orderobj.getLsprotocolmaster().getProtocolmastercode());
       	 		
       	 			try (ResultSet rs = pst.executeQuery()) {
                    while (rs.next()) {
                    	protocolmasterobj = mapResultSetToLsprotocolmaster(rs);
                    	orderobj.setLsprotocolmaster(protocolmasterobj);
                    	 //protocolSteps.add(protostepobj);
                    }
                } catch (SQLException e) {
	                e.printStackTrace(); // Consider logging this properly
	            }
              }
       	 	con.close();
    	 }
    	if(orderobj.getLsautoregister() != null) { 
//	    	Date Autoregdate = orderobj.getLsautoregister().getAutocreatedate();
//	    	Instant auto = Autoregdate.toInstant();
//	        LocalDateTime autoTime = LocalDateTime.ofInstant(auto, ZoneId.systemDefault());
	        
	        LocalDateTime currentTime = LocalDateTime.now();
	
	        if (autoTime.isAfter(currentTime)) {
	            Duration duration = Duration.between(currentTime, autoTime);
	            long delay = duration.toMillis();
	            scheduleForProtocolAutoRegOrders(orderobj, delay, configuration,currentdate);
	        }else {
	        	
	        	
//	        	TimerTask task = new TimerTask() {
//		            @SuppressWarnings("unlikely-arg-type")
//					@Override
//		            public void run() {
//		                try {
//		                	ExecuteProtocolAutoRegistration(orderobj, configuration,currentdate);
//		                }  catch (SQLException | ParseException | IOException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						} catch (InterruptedException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//		                scheduledTasks.remove(orderobj.getProtocolordercode());
//		            }
//		        };
//		        Timer timer = new Timer();
//		        timer.schedule(task, 45000);
//		        scheduledTasks.put(orderobj.getProtocolordercode().intValue(), task);
	        	
	        	try {
					ExecuteProtocolAutoRegistration(orderobj, configuration,currentdate);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        
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
		 con.close();
		} 
	}
    
    public void checkAndScheduleautoOrderRegister() throws Exception {
    	List<Datasourcemaster> configList = configRepo.findByinitialize(true);
        for (Datasourcemaster objData : configList) {
        
            HikariConfig configuration = createHikariConfig(objData);
            try (HikariDataSource dataSource = new HikariDataSource(configuration);
                    Connection con = dataSource.getConnection()) {
            	
            	//String checkrepeat = "SELECT * FROM LSlogilablimsorderdetail WHERE repeat = true and createdtimestamp BETWEEN ? AND ?";
            	String checkrepeat = "SELECT * FROM LSlogilablimsorderdetail WHERE repeat = true and autoregistercount > 0";
            	
            	 try (PreparedStatement pst = con.prepareStatement(checkrepeat)) {
                     
                 	// pst.setTimestamp(1, new Timestamp(getfromDate.getTime()));
                   //  pst.setTimestamp(2, new Timestamp(gettoDate.getTime()));

                     try (ResultSet rs = pst.executeQuery()) {
                    	
                        	 while (rs.next()) {
//                        		 Integer autoregisterCount =rs.getInt("autoregistercount");
//                                if (autoregisterCount != null && autoregisterCount > 0) {
                            	 LSlogilablimsorderdetail orderobj = mapResultSetToLslogilabOrder(rs);
                            	 
                                 //scheduleAutoRegisteration(orderobj, configuration,currentdate);
                            	 checkinsheetrange(orderobj, configuration);
//                                 }
                             }
                        
                        
                     } catch (SQLException e) {
 		                e.printStackTrace(); // Consider logging this properly
 		            }
                 }
            	 con.close();
            }	
        }
    }

    private void checkinsheetrange(LSlogilablimsorderdetail orderobj, HikariConfig configuration) throws Exception {
    	gettofromdate();
    	getCurrentUTCDate();
    	
    	LsAutoregister objlsauto = null ;
    	try (HikariDataSource dataSource = new HikariDataSource(configuration);
                Connection con = dataSource.getConnection()) {
    		
   		 String autoregquery = "SELECT * FROM Lsautoregister WHERE batchcode=? and screen=?";

            try (PreparedStatement pst = con.prepareStatement(autoregquery)) {
               
            	pst.setLong(1, orderobj.getBatchcode());
            	pst.setString(2, "Sheet_Order");
             
                try (ResultSet rs = pst.executeQuery()) {
                    while (rs.next()) {
                    	 objlsauto = mapResultSetToLsAutoregister(rs);
                    	 orderobj.setLsautoregisterorders(objlsauto);
                       
                    	 Date Autoregdate = objlsauto.getAutocreatedate();
             	    	Instant auto = Autoregdate.toInstant();
             	        LocalDateTime autoTime = LocalDateTime.ofInstant(auto, ZoneId.systemDefault());
             	        
             	        Instant fromdate = getfromDate.toInstant();
             	        LocalDateTime fromdateTime = LocalDateTime.ofInstant(fromdate, ZoneId.systemDefault());
             	        
             	        Instant todate = gettoDate.toInstant();
             	        LocalDateTime todateTime = LocalDateTime.ofInstant(todate, ZoneId.systemDefault());
             	
                      	 if(autoTime.isAfter(fromdateTime) && autoTime.isBefore(todateTime)) {
                      		scheduleAutoRegisteration(orderobj, configuration,currentdate,autoTime);
                      	 }	 
                    }
                } catch (SQLException e) {
		                e.printStackTrace(); // Consider logging this properly
		            }
            }
            con.close();
    	}
    }
    private void scheduleAutoRegisteration(LSlogilablimsorderdetail orderobj, HikariConfig configuration,Date currentdate,LocalDateTime autoTime) throws SQLException, IOException {
    	
    	LSuserMaster objuser = null;
    	 try (HikariDataSource dataSource = new HikariDataSource(configuration);
                 Connection con = dataSource.getConnection()) {

                
               String lsuserquery = "SELECT * FROM Lsusermaster WHERE usercode=? ";
               try (PreparedStatement pst = con.prepareStatement(lsuserquery)) {
                   
               	pst.setInt(1, orderobj.getLsuserMaster().getUsercode());
                
                   try (ResultSet rs = pst.executeQuery()) {
                       while (rs.next()) {
                    	   objuser = mapResultsetToLsUsermaster(rs);
                       	   orderobj.setLsuserMaster(objuser);
                       }
                   } catch (SQLException e) {
		                e.printStackTrace(); // Consider logging this properly
		            }
               }
               con.close();
    	 }
    	if(orderobj.getLsautoregisterorders() != null) { 
	        LocalDateTime currentTime = LocalDateTime.now();
	
	        if (autoTime.isAfter(currentTime)) {
	            Duration duration = Duration.between(currentTime, autoTime);
	            long delay = duration.toMillis();
	            scheduleForAutoRegOrders(orderobj, delay, configuration,currentdate);
	        }else {
	       
	        	try {
					ExecuteAutoRegistration(orderobj, configuration,currentdate);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
					
//					 String overduedaysquery = "SELECT * FROM lsordernotification WHERE (Isduedateexhausted = true and Iscompleted = false)"
//					 		+ "OR"
//					 		+ " Isduedateexhausted = true and Iscompleted isnull";
//
//					 try (PreparedStatement pst = con.prepareStatement(overduedaysquery)) {
//						
//						    try (ResultSet rs = pst.executeQuery()) {
//						        while (rs.next()) {
//						            LSOrdernotification objNotification = mapResultSetToOrderNotification(rs);
//						            notifyoverduedays(objNotification, configuration);
//						        }
//						    } catch (ParseException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
//						}
//					 
//		             catch (SQLException e) {
//		                e.printStackTrace(); // Consider logging this properly
//		            }
			   con.close();	
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
                con.close();
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
						} catch (ParseException | InterruptedException | SQLException e) {
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
						} catch (ParseException | InterruptedException | SQLException e) {
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
					} catch (ParseException | SQLException | InterruptedException e) {
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
	                } catch (ParseException | InterruptedException | SQLException e) {
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

    private void scheduleForAutoRegOrders(LSlogilablimsorderdetail orderobj, long delay, HikariConfig configuration,Date currentdate) {
    	Set<Integer> runningTasks = new HashSet<>();
    	
    	int batchcode = orderobj.getBatchcode().intValue();

//    	if (scheduledTasks.containsKey(batchcode)) {
//            System.out.println("Task already scheduled for batch ID: " + batchcode);
//            return;
//        }
    	
    	 synchronized (runningTasks) {
    		 if (runningTasks.contains(batchcode)) {
                 System.out.println("Task already scheduled or running for batch ID: " + batchcode);
                 return;
             }
    		 
    	if((orderobj.getRepeat()!=null || orderobj.getRepeat() != false) && orderobj.getLsautoregisterorders()!=null) {
    		TimerTask task = new TimerTask() {
	            @SuppressWarnings("unlikely-arg-type")
				@Override
	            public void run() {
	                try {
	                	ExecuteAutoRegistration(orderobj, configuration,currentdate);
	                }  catch (SQLException | ParseException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}finally {
                        synchronized (runningTasks) {
                            runningTasks.remove(batchcode);
                        }
                        scheduledTasks.remove(batchcode);
                    }
	            }
	        };
	        runningTasks.add(batchcode);
	        Timer timer = new Timer();
	        timer.schedule(task, delay);
	        scheduledTasks.put(batchcode, task);
    	}
      }
  
    }
    
    private void scheduleForProtocolAutoRegOrders(LSlogilabprotocoldetail orderobj, long delay, HikariConfig configuration,Date currentdate) {
    	
    	int protocolordercode = orderobj.getProtocolordercode().intValue();

    	if (scheduledTasks.containsKey(protocolordercode)) {
            System.out.println("Task already scheduled for protocolordercode: " + protocolordercode);
            return;
        }
    	
    	if((orderobj.getRepeat()!=null || orderobj.getRepeat() != false) && orderobj.getLsautoregister()!=null) {
    		TimerTask task = new TimerTask() {
	            @SuppressWarnings("unlikely-arg-type")
				@Override
	            public void run() {
	                try {
	                	ExecuteProtocolAutoRegistration(orderobj, configuration,currentdate);
	                }  catch (SQLException | ParseException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}finally {
	                scheduledTasks.remove(protocolordercode);
					}
	            }
	        };
	        Timer timer = new Timer();
	        timer.schedule(task, delay);
	        scheduledTasks.put(protocolordercode, task);
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
	                } catch (ParseException | InterruptedException | SQLException e) {
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
	                } catch (ParseException | SQLException | InterruptedException e) {
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
    
    public LsAutoregister getautoregisterdetails (LsAutoregister lsautoregister , HikariConfig configuration , Date currentdate,String screen) throws SQLException, InterruptedException {
    	try (HikariDataSource dataSource = new HikariDataSource(configuration);
                Connection con = dataSource.getConnection()) {
    	
	    	LsAutoregister autoobj = new LsAutoregister();
	    	long generatedregcode = 0;
	    	if(lsautoregister.getTimespan().equals("Days")) {
				
				 Calendar calendar = Calendar.getInstance();
			        calendar.setTime(currentdate);
			        calendar.add(Calendar.DAY_OF_MONTH, lsautoregister.getInterval());
			        Date futureDate = calendar.getTime();  
			        autoobj.setAutocreatedate(futureDate);
			   
			 }else if(lsautoregister.getTimespan().equals("Week")) {
	
				    Calendar calendar = Calendar.getInstance();
			        calendar.setTime(currentdate);
			        calendar.add(Calendar.DAY_OF_MONTH, (lsautoregister.getInterval()*7));
	
			        Date futureDate = calendar.getTime();   
			        autoobj.setAutocreatedate(futureDate);
			 }else {
				
//				    Calendar calendar = Calendar.getInstance();
//			        calendar.setTime(currentdate);
//			        calendar.add(Calendar.HOUR_OF_DAY,(lsautoregister.getInterval()));
//			        Date futureDate = calendar.getTime();   
//			        autoobj.setAutocreatedate(futureDate);
			        
				    Calendar calendar = Calendar.getInstance();
			        calendar.setTime(currentdate);
			       // calendar.add(Calendar.HOUR_OF_DAY,(autoorder.get(0).getInterval()));
			        calendar.add(Calendar.MINUTE , (2));
			        Date futureDate = calendar.getTime();   
			        autoobj.setAutocreatedate(futureDate);
			 }
			
			autoobj.setScreen(screen);
			autoobj.setIsautoreg(true);
			autoobj.setInterval(lsautoregister.getInterval());
			autoobj.setTimespan(lsautoregister.getTimespan());
			autoobj.setIsmultitenant(lsautoregister.getIsmultitenant());
			autoobj.setRepeat(true);
			autoobj.setRegcode(null);
			
			Calendar autocalendar = Calendar.getInstance();
	        autocalendar.setTime(autoobj.getAutocreatedate());
	        Date autocreateddate = autocalendar.getTime(); 
	        
//	        (SELECT COALESCE(MAX(regcode), 0) + 1 FROM lsautoregister),
	        
	        String autoregtablequery = "Insert into lsautoregister (autocreatedate,interval,isautoreg,"
				+ "ismultitenant,repeat,screen,timespan) Values (?,?,?,?,?,?,?)";
	
//	        , Statement.RETURN_GENERATED_KEYS
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
		             
		       	   ResultSet rs = pst.getGeneratedKeys();
		              if (rs.next()) {
		                  generatedregcode = rs.getLong(1);
		                  System.out.println("Inserted record's regcode: " + generatedregcode);
		                  autoobj.setRegcode(generatedregcode);
		                  
		              }
		          } else {
		              System.out.println("No record inserted.");
		          }
		          
//		          Thread.sleep(30000);
//		          String updateSequenceSQL = "SELECT setval('lsautoregister_seq', ?)";
//		          try (PreparedStatement pstmt = con.prepareStatement(updateSequenceSQL)) {
//		              pstmt.setLong(1, generatedregcode);
//		              pstmt.execute();
//		              Thread.sleep(15000);
//		              System.out.println("Sequence updated successfully.");
//		          } catch (Exception e) {
//		              e.printStackTrace();
//		          }
		   	   }
	        con.close();
		    return autoobj;
		 
    	}
    }
 
    public void ExecuteProtocolAutoRegistration(LSlogilabprotocoldetail lSlogilabprotocoldetail , HikariConfig configuration, Date currentdate)throws ParseException, SQLException, IOException, InterruptedException {
    	   
    	try (HikariDataSource dataSource = new HikariDataSource(configuration);
                Connection con = dataSource.getConnection()) {

    		LSlogilabprotocoldetail lSlogilabprotocoldetail1=null;
    		String checkrepeat = "SELECT * FROM lslogilabprotocoldetail WHERE protocolordercode=?";
       	    try (PreparedStatement pst = con.prepareStatement(checkrepeat)) {
                
            	 pst.setLong(1, lSlogilabprotocoldetail.getProtocolordercode());
              //  pst.setTimestamp(2, new Timestamp(gettoDate.getTime()));

                try (ResultSet rs = pst.executeQuery()) {
                    while (rs.next()) {
                   	  lSlogilabprotocoldetail1 = mapResultSetToOrderLSlogilabprotocoldetail(rs);
                     
                    }
                } catch (SQLException e) {
	                e.printStackTrace(); // Consider logging this properly
	            }	    		
                checkrepeat="";
       	     }
       	    
       	 lSlogilabprotocoldetail1.setLsprotocolmaster(lSlogilabprotocoldetail.getLsprotocolmaster());
       	 lSlogilabprotocoldetail1.setLsautoregister(lSlogilabprotocoldetail.getLsautoregister());
   		 LSprotocolmaster protomasterobj=null;
   		 
   		
   		Long originalProtocolOrderCode = lSlogilabprotocoldetail1.getProtocolordercode();
        Long clonedProtocolOrderCode = new Long(originalProtocolOrderCode);
        
      
        
		if(lSlogilabprotocoldetail1.getLsautoregister()!= null) {
			
			LsAutoregister auditregdetails =  getautoregisterdetails(lSlogilabprotocoldetail1.getLsautoregister(),configuration,currentdate,"Protocol_Order");
			lSlogilabprotocoldetail1.setLsautoregister(auditregdetails);	
			
		}
	
			lSlogilabprotocoldetail1.setCreatedtimestamp(currentdate);

		if (lSlogilabprotocoldetail1 != null && lSlogilabprotocoldetail1.getLsautoregister()!= null) {
			lSlogilabprotocoldetail1.setVersionno(0);

			if (lSlogilabprotocoldetail1.getProtocoltype() == 2
					&& lSlogilabprotocoldetail1.getLsprotocolmaster().getProtocolmastercode() == -2) {
				
				String deftempromaster = "SELECT * FROM LSprotocolmaster WHERE defaulttemplate =1";
          	 		try (PreparedStatement pst = con.prepareStatement(deftempromaster)) {
                   
          	 			try (ResultSet rs = pst.executeQuery()) {
	                       while (rs.next()) {
	                       	 protomasterobj = mapResultSetToLsprotocolmaster(rs);
	                       	 lSlogilabprotocoldetail1.setLsprotocolmaster(protomasterobj);
	                       }
		                   } catch (SQLException e) {
				                e.printStackTrace(); // Consider logging this properly
				          }
          	 			deftempromaster = "";
                      }	
				
				if (protomasterobj == null) {
					LSprotocolmaster lsprotocolmaster = new LSprotocolmaster();
					lsprotocolmaster.setProtocolmastername("Default Protocol");
					lsprotocolmaster.setStatus(0);
					lsprotocolmaster.setCreatedby(lSlogilabprotocoldetail1.getCreateby());
					lsprotocolmaster.setCreatedate(lSlogilabprotocoldetail1.getCreatedtimestamp());
					lsprotocolmaster.setLssitemaster(lSlogilabprotocoldetail1.getSitecode());
					
					String updateString = "INSERT INTO lsprotocolmaster (protocolmastername,status,createdby,createdate,lssitemaster) VALUES (?,?,?,?,?)";
		
					 Calendar protocalendar = Calendar.getInstance();
					 protocalendar.setTime(lsprotocolmaster.getCreatedate());
				     Date protocalendardate = protocalendar.getTime(); 
				        
	               try (PreparedStatement pst = con.prepareStatement(updateString, Statement.RETURN_GENERATED_KEYS)) {
	                   pst.setString(1, lsprotocolmaster.getProtocolmastername());
	                   pst.setInt(2, lsprotocolmaster.getStatus());
	                   pst.setInt(3, lsprotocolmaster.getCreatedby());
	                   pst.setTimestamp(4, new Timestamp(protocalendardate.getTime()));
	                   pst.setInt(5,lSlogilabprotocoldetail1.getSitecode());
	                   pst.setLong(6, lSlogilabprotocoldetail1.getProtocolordercode());
	                   pst.executeUpdate();
	                   
	               }
	                updateString="";
					lSlogilabprotocoldetail1.setLsprotocolmaster(lsprotocolmaster);
				} else {
					lSlogilabprotocoldetail1.setLsprotocolmaster(protomasterobj);
				}

			   }
	
				lSlogilabprotocoldetail1.setProtoclordername(null);
				lSlogilabprotocoldetail1.setProtocolordercode(null);
			

//				String deftem = "update LSlogilabprotocoldetail set repeat=false WHERE protocolordercode=?";
//			 		try (PreparedStatement pst = con.prepareStatement(deftem)) {
//		      
//			 			pst.setLong(1, clonedProtocolOrderCode);
//			 			pst.executeUpdate();
//			 			lSlogilabprotocoldetail.setRepeat(false);
//		         }
//			 		deftem="";
			 		
			String updateString = "INSERT INTO lSlogilabprotocoldetail (testcode,createdtimestamp,keyword,protocoltype,lsprojectmaster_projectcode,"
					+ "lsusermaster_usercode,approved,versionno,createby,sitecode,viewoption,"
					+ "fileuid,fileuri,containerstored,lsprotocolmaster_protocolmastercode,repeat,"
					+ "orderflag,elnprotocolworkflow_workflowcode,lsautoregister_regcode,"
					+ "elnmaterial_nmaterialcode,elnmaterialinventory_nmaterialinventorycode,autoregistercount) "
					+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
					
		           try (PreparedStatement pst = con.prepareStatement(updateString, Statement.RETURN_GENERATED_KEYS)) {
		               pst.setInt(1, lSlogilabprotocoldetail1.getTestcode());
		               pst.setTimestamp(2, new Timestamp(currentdate.getTime()));
		               pst.setString(3, lSlogilabprotocoldetail1.getKeyword());
		               pst.setInt(4 , lSlogilabprotocoldetail1.getProtocoltype());
		               pst.setInt(5,lSlogilabprotocoldetail1.getLsprojectmaster().getProjectcode());
		              // pst.setInt(6, lSlogilabprotocoldetail.getLssamplemaster().getSamplecode());
		               pst.setInt(6,lSlogilabprotocoldetail1.getLsuserMaster().getUsercode());
		               pst.setInt(7,lSlogilabprotocoldetail1.getApproved());
		               pst.setInt(8, lSlogilabprotocoldetail1.getVersionno());
		               pst.setInt(9, lSlogilabprotocoldetail1.getCreateby());
		               pst.setInt(10,lSlogilabprotocoldetail1.getSitecode());
		               pst.setInt(11,lSlogilabprotocoldetail1.getViewoption());
		               pst.setString(12, lSlogilabprotocoldetail1.getFileuid());
		               pst.setString(13, lSlogilabprotocoldetail1.getFileuri());
		               pst.setInt(14, lSlogilabprotocoldetail1.getContainerstored());
		               pst.setInt(15,lSlogilabprotocoldetail1.getLsprotocolmaster().getProtocolmastercode());
		               if(lSlogilabprotocoldetail1.getAutoregistercount()==0) {
	            		   pst.setBoolean(16, false);
	            	   }else {
	            		   pst.setBoolean(16, true);
	            	   }
		               pst.setObject(22, lSlogilabprotocoldetail1.getAutoregistercount());
		               pst.setString(17, "N");
		               pst.setInt(18,lSlogilabprotocoldetail1.getElnprotocolworkflow().getWorkflowcode());
		               pst.setLong(19, lSlogilabprotocoldetail1.getLsautoregister().getRegcode());
		             //  pst.setInt(20, lSlogilabprotocoldetail.getMaterial().getNmaterialcode());
		               pst.setObject(20, lSlogilabprotocoldetail1.getElnmaterial().getNmaterialcode());
		               pst.setObject(21,lSlogilabprotocoldetail1.getElnmaterialinventory().getNmaterialinventorycode());
		               
		               int affectedRows=pst.executeUpdate();
	                   
	                   if (affectedRows > 0) {
	                       // Retrieve the generated keys
	                	   ResultSet rs = pst.getGeneratedKeys();
	                       if (rs.next()) {
	                           long generateprotocolordercode = rs.getLong(1);
	                           System.out.println("Inserted record's protocolordercode: " + generateprotocolordercode);
	                           lSlogilabprotocoldetail1.setProtocolordercode(generateprotocolordercode);
	                       }
	                   } else {
	                       System.out.println("No record inserted.");
	                   }
	                   updateString="";
		           }
          
		           String deftemp = "update lsautoregister set batchcode = ? where regcode=?;"+"update LSlogilabprotocoldetail set repeat=false WHERE protocolordercode=?";
					
		   	 		try (PreparedStatement pst = con.prepareStatement(deftemp)) {
		            
		   	 		pst.setLong(1, lSlogilabprotocoldetail1.getProtocolordercode());
		   	 		pst.setLong(2, lSlogilabprotocoldetail1.getLsautoregister().getRegcode());
				    pst.setLong(3, clonedProtocolOrderCode); 	
		   	 	    pst.executeUpdate();
		   	 	    lSlogilabprotocoldetail1.getLsautoregister().setBatchcode(lSlogilabprotocoldetail1.getProtocolordercode());

		   	 		}
		   	 	   lSlogilabprotocoldetail1.setRepeat(true);
		           if (lSlogilabprotocoldetail1.getProtocolordercode() != null) {

						String ProtocolOrderName = "ELN" + lSlogilabprotocoldetail1.getProtocolordercode();

						lSlogilabprotocoldetail1.setProtoclordername(ProtocolOrderName);

						lSlogilabprotocoldetail1.setOrderflag("N");

						updateprotoorderdatacontent(lSlogilabprotocoldetail1,configuration);
						
						if(lSlogilabprotocoldetail1.getRepeat() == true && lSlogilabprotocoldetail1.getLsautoregister()!= null) {
					    	Date Autoregdate = lSlogilabprotocoldetail1.getLsautoregister().getAutocreatedate();
					    	Instant auto = Autoregdate.toInstant();
					        LocalDateTime autoTime = LocalDateTime.ofInstant(auto, ZoneId.systemDefault());
							
							scheduleProtocolAutoRegisteration(lSlogilabprotocoldetail1 , configuration,currentdate,autoTime);
						}
						
							String comments = "order: "+lSlogilabprotocoldetail1.getProtocolordercode()+" is now autoregistered";
				            String screen ="IDS_SCN_PROTOCOLORDERS";
				            int site=lSlogilabprotocoldetail1.getSitecode();
				            int usercode=lSlogilabprotocoldetail1.getLsuserMaster().getUsercode();
				            
				            insertaudit(comments,screen,site , usercode , currentdate , configuration);  

				               comments="";
				               screen="";
						}
			
			 }
		    con.close();
	     }
      }
    public void updatesheetordercontent(LSlogilablimsorderdetail objorder,CloudSheetCreation cloudobject,HikariConfig configuration,Date currentdate) throws IOException, SQLException, ParseException {
    	//int generatedsampleversioncode=0;
    	try (HikariDataSource dataSource = new HikariDataSource(configuration);
                Connection con = dataSource.getConnection()) {
			String Content = "";
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
	    	 if (Content == null || Content.equals("")) {
					Content = defaultContent;
				}
	    	 
	    	 //if (objorder.getLssamplefile().getLssamplefileversion() != null) {

	 			String Contentversion = Content;
	 			
	 			LSsamplefileversion versobj = new LSsamplefileversion();
	 			versobj.setCreatedate(currentdate);
	 			versobj.setVersionname("version_1");
	 			versobj.setVersionno(1);
		 			LSuserMaster obj = new LSuserMaster();
		 			obj.setCreateddate(currentdate);
	 			versobj.setCreatebyuser(obj);
	 			
	 			String insertversion = "INSERT INTO LSsamplefileversion(createdate,versionno,versionname,createbyuser_usercode,batchcode)"
	 					+ " VALUES (?,1,'version_1',?,0) ";   
	 			try (PreparedStatement pst = con.prepareStatement(insertversion, Statement.RETURN_GENERATED_KEYS)) {
	                pst.setTimestamp(1, new Timestamp(currentdate.getTime()));
	                pst.setInt(2, objorder.getLsuserMaster().getUsercode());
	                
	                int affectedRows=pst.executeUpdate();
	                   
	                   if (affectedRows > 0) {
	                	   ResultSet rs = pst.getGeneratedKeys();
	                       if (rs.next()) {
	                           int sampleversioncode = rs.getInt(1);
	                           System.out.println("Inserted record's sampleversioncode: " + sampleversioncode);
	                           versobj.setFilesamplecodeversion(sampleversioncode);
	                       }
	                   } else {
	                       System.out.println("No record inserted.");
	                   }
	 			}
	 			insertversion="";
	 			//lssamplefileversionRepository.save(objorder.getLssamplefile().getLssamplefileversion());
	 			updateorderversioncontent(Contentversion, versobj,
	 					objorder.getLsautoregisterorders().getIsmultitenant(),configuration);

	 			Contentversion = null;
	 		//}
	 			
	 			List<LSsamplefileversion> versionlist = new ArrayList<>();
	 			versionlist.add(versobj);
	    	 
	    	 
	    		 LSsamplefile sampobj = new LSsamplefile();
	    		 sampobj.setCreatedate(currentdate);
	    		 sampobj.setVersionno(1);
	    		 sampobj.setCreatebyuser(obj);
	    		 sampobj.setBatchcode(0);
	    		 sampobj.setProcessed(0);
	    		 sampobj.setLssamplefileversion(versionlist);
	    		 
	    		 objorder.setLssamplefile(sampobj);
	 			
	    		 String insertsample = "INSERT INTO LSsamplefile(createdate,versionno,createbyuser_usercode,batchcode,processed)"
		 					+ " VALUES (?,1,?,0,0) ";   
		 			try (PreparedStatement pst = con.prepareStatement(insertsample, Statement.RETURN_GENERATED_KEYS)) {
		                pst.setTimestamp(1, new Timestamp(currentdate.getTime()));
		                pst.setInt(2, objorder.getLsuserMaster().getUsercode());
		                
		                int affectedRows=pst.executeUpdate();
		                   
		                   if (affectedRows > 0) {
		                	   ResultSet rs = pst.getGeneratedKeys();
		                       if (rs.next()) {
		                           int filesamplecode = rs.getInt(1);
		                           System.out.println("Inserted record's filesamplecode: " + filesamplecode);
		                           sampobj.setFilesamplecode(filesamplecode);
		                       }
		                   } else {
		                       System.out.println("No record inserted.");
		                   }
		 			}
		 			objorder.setLssamplefile(sampobj);		
		 			
		 			if (objorder.getLssamplefile() != null) {
			       		updateordercontent(Content, objorder.getLssamplefile(), objorder.getLsautoregisterorders().getIsmultitenant(),configuration);
			        }
		 con.close();
    	}
    }
  
    public void updateorderversioncontent(String Content, LSsamplefileversion objfile, Integer ismultitenant ,HikariConfig configuration)
			throws IOException, SQLException {
    	long id=0;
    	try (HikariDataSource dataSource = new HikariDataSource(configuration);
                Connection con = dataSource.getConnection()) {
		if (ismultitenant == 1 || ismultitenant == 2) {
			Map<String, Object> objMap = objCloudFileManipulationservice.storecloudSheetsreturnwithpreUUID(Content,
					commonfunction.getcontainername(ismultitenant, (String) dataSource.getDataSourceProperties().getProperty("tenantName")) + "orderversion");
			String fileUUID = (String) objMap.get("uuid");
			String fileURI = objMap.get("uri").toString();

			
			if (objfile.getFilesamplecodeversion() != null) {
					id=objfile.getFilesamplecodeversion();
			} else {
					id=1;
			}
//			objsavefile.setFileuri(fileURI);
//			objsavefile.setFileuid(fileUUID);
//			objsavefile.setContainerstored(1);

			//cloudOrderVersionRepository.save(objsavefile);
			String insertsample = "INSERT INTO LSOrderVersionfiles(id,fileuid,fileuri,containerstored)"
 					+ " VALUES (?,?,?,1) ";   
			try (PreparedStatement pst = con.prepareStatement(insertsample)) {
				pst.setLong(1, id);
				pst.setString(2, fileUUID);
				pst.setString(3, fileURI);
                
                pst.executeUpdate();
			}
			
		} else {

			GridFSDBFile largefile = gridFsTemplate.findOne(
					new Query(Criteria.where("filename").is("orderversion_" + objfile.getFilesamplecodeversion())));
			if (largefile != null) {
				gridFsTemplate.delete(
						new Query(Criteria.where("filename").is("orderversion_" + objfile.getFilesamplecodeversion())));
			}
			gridFsTemplate.store(new ByteArrayInputStream(Content.getBytes(StandardCharsets.UTF_8)),
					"orderversion_" + objfile.getFilesamplecodeversion(), StandardCharsets.UTF_16);

		   }
		  con.close();
    	}
	}
    
    public void updateordercontent(String Content, LSsamplefile objfile, Integer ismultitenant,HikariConfig configuration) throws IOException, SQLException {

		String contentParams = "";
		String contentValues = "";
		try (HikariDataSource dataSource = new HikariDataSource(configuration);
                Connection con = dataSource.getConnection()) {
			Map<String, Object> objContent = commonfunction.getParamsAndValues(Content);
	
			contentValues = (String) objContent.get("values");
			contentParams = (String) objContent.get("parameters");
	
			if (ismultitenant == 1 || ismultitenant == 2) {
	
				Map<String, Object> objMap = objCloudFileManipulationservice.storecloudSheetsreturnwithpreUUID(Content,
						commonfunction.getcontainername(ismultitenant, (String) dataSource.getDataSourceProperties().getProperty("tenantName")) + "ordercreation");
				String fileUUID = (String) objMap.get("uuid");
				String fileURI = objMap.get("uri").toString();
	
//				CloudOrderCreation objsavefile = new CloudOrderCreation();
//				objsavefile.setId((long) objfile.getFilesamplecode());
//				objsavefile.setFileuri(fileURI);
//				objsavefile.setFileuid(fileUUID);
//				objsavefile.setContainerstored(1);
//				objsavefile.setContentvalues(contentValues);
//				objsavefile.setContentparameter(contentParams);
	
				String insertsample = "INSERT INTO LSOrderCreationfiles(id,fileuid,fileuri,containerstored,contentvalues,contentparameter)"
	 					+ " VALUES (?,?,?,1,?::jsonb, ?::jsonb) ";   
				try (PreparedStatement pst = con.prepareStatement(insertsample)) {
					pst.setLong(1, (long) objfile.getFilesamplecode());
					pst.setString(2, fileUUID);
					pst.setString(3, fileURI);
	                pst.setString(4, contentValues);
	                pst.setString(5, contentParams);
	                pst.executeUpdate();
				}
				insertsample="";
				//cloudOrderCreationRepository.save(objsavefile);
	
//				/objsavefile = null;
			} else {
				OrderCreation objsavefile = new OrderCreation();
				objsavefile.setId((long) objfile.getFilesamplecode());
				objsavefile.setContentvalues(contentValues);
				objsavefile.setContentparameter(contentParams);
	
				Query query = new Query(Criteria.where("id").is(objsavefile.getId()));
	
				Boolean recordcount = mongoTemplate.exists(query, OrderCreation.class);
	
				if (!recordcount) {
					mongoTemplate.insert(objsavefile);
				} else {
					Update update = new Update();
					update.set("contentvalues", contentValues);
					update.set("contentparameter", contentParams);
	
					mongoTemplate.upsert(query, update, OrderCreation.class);
				}
	
				GridFSDBFile largefile = gridFsTemplate
						.findOne(new Query(Criteria.where("filename").is("order_" + objfile.getFilesamplecode())));
				if (largefile != null) {
					gridFsTemplate.delete(new Query(Criteria.where("filename").is("order_" + objfile.getFilesamplecode())));
				}
				gridFsTemplate.store(new ByteArrayInputStream(Content.getBytes(StandardCharsets.UTF_8)),
						"order_" + objfile.getFilesamplecode(), StandardCharsets.UTF_16);
	
				objsavefile = null;
			}
	
			contentParams = null;
			contentValues = null;
			objContent = null;
			con.close();
		}
	}
    
	public void updateprotoorderdatacontent ( LSlogilabprotocoldetail lSlogilabprotocoldetail,HikariConfig configuration) throws SQLException {
    	try (HikariDataSource dataSource = new HikariDataSource(configuration);
                Connection con = dataSource.getConnection()) {
		String Content = "";
		
    	if (lSlogilabprotocoldetail.getLsautoregister().getIsmultitenant() == 1
				|| lSlogilabprotocoldetail.getLsautoregister().getIsmultitenant() == 2) {
			if (lSlogilabprotocoldetail.getLsprotocolmaster().getContainerstored() == null
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
							lSlogilabprotocoldetail.getLsprotocolmaster().getFileuid(),
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
					.is("protocol_" + lSlogilabprotocoldetail.getLsprotocolmaster().getProtocolmastercode())));
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
    	con.close();
		}
    }
    public void getlsfiledata(LSlogilablimsorderdetail objorder , HikariConfig configuration ) throws SQLException {
    	LSfile lsfileobj = null;
    	try (HikariDataSource dataSource = new HikariDataSource(configuration);
                Connection con = dataSource.getConnection()) {
    	 String lsfilequery = "SELECT * FROM LSfile WHERE filecode = ? ";

         try (PreparedStatement pst = con.prepareStatement(lsfilequery)) {
            
         	pst.setInt(1, objorder.getLsfile().getFilecode());
            
             try (ResultSet rs = pst.executeQuery()) {
                 while (rs.next()) {
                	 lsfileobj = mapResultsetToLSFile(rs);
                	  objorder.setLsfile(lsfileobj);
                 }
             } catch (SQLException e) {
	                e.printStackTrace(); // Consider logging this properly
	            }
            }
         lsfilequery="";
         con.close();
    	}
    	//return lsfileobj;
    }
    
    public CloudSheetCreation getsheetcreationdata(LSlogilablimsorderdetail objorder , HikariConfig configuration )throws SQLException {
    	CloudSheetCreation cloudobject=null;
    	try (HikariDataSource dataSource = new HikariDataSource(configuration);
                Connection con = dataSource.getConnection()) {
    		
    	    String cloudquery = "SELECT * FROM LSSheetCreationfiles WHERE id = ? ";

            try (PreparedStatement pst = con.prepareStatement(cloudquery)) {
                   
                	pst.setLong(1, objorder.getLsfile().getFilecode());
                   
                    try (ResultSet rs = pst.executeQuery()) {
                        while (rs.next()) {
                   	     cloudobject = mapResultsetToCloudSheet(rs);
                   	     
                        }
                    } catch (SQLException e) {
		                e.printStackTrace(); // Consider logging this properly
		            }
                }
            cloudquery="";
            con.close();
    	}
    	return cloudobject;
    }
    
    public void insertaudit(String comments,String screen,int site,int usercode,Date currentdate , HikariConfig configuration) throws SQLException, InterruptedException {
    	try (HikariDataSource dataSource = new HikariDataSource(configuration);
                Connection con = dataSource.getConnection()) {
    	String auditquery = "INSERT INTO LScfttransaction(moduleName,actions,manipulatetype,transactiondate,comments,lssitemaster_sitecode,systemcoments,lsusermaster_usercode)"
				+ " VALUES (?,?,?,?,?,?,?,?) "; 
    	int generatedserialno = 0;
//    	String auditquery = "INSERT INTO lscfttransaction (serialno, moduleName,actions,manipulatetype,transactiondate,comments,lssitemaster_sitecode,systemcoments,lsusermaster_usercode) " +
//                "VALUES ((SELECT COALESCE(MAX(serialno), 0) + 1 FROM lscfttransaction), ?,?,?,?,?,?,?,?)";
 	
    	String systemcomments = "Audittrail.Audittrailhistory.Audittype.IDS_AUDIT_SYSTEMGENERATED";         
//    	, Statement.RETURN_GENERATED_KEYS
           try (PreparedStatement pst = con.prepareStatement(auditquery)) {
               pst.setString(1, screen);
               pst.setString(2, "IDS_TSK_REGISTERED");
               pst.setString(3, "IDS_AUDIT_INSERTORDERS");
               pst.setTimestamp(4, new Timestamp(currentdate.getTime()));
               pst.setString(5, comments);
               pst.setInt(6, site);
               pst.setString(7, systemcomments);
               pst.setInt(8, usercode);
               
               pst.executeUpdate();
               
//               int affectedRows=pst.executeUpdate();
//               if (affectedRows > 0) {
//  	             
//    	       	   ResultSet rs = pst.getGeneratedKeys();
//    	              if (rs.next()) {
//    	                  generatedserialno = rs.getInt(1);
//    	                  System.out.println("Inserted record's serialno: " + generatedserialno);
//    	                  //autoobj.setRegcode(generatedregcode);
//    	                  
//    	              }
//    	          } else {
//    	              System.out.println("No record inserted.");
//    	          }
               
//               Thread.sleep(45000);
//	 	          String updateSequenceSQL = "SELECT setval('lscfttransaction_sequence', ?)";
//	 	          try (PreparedStatement pstmt = con.prepareStatement(updateSequenceSQL)) {
//	 	              pstmt.setLong(1, generatedserialno);
//	 	              pstmt.execute();
//	 	              Thread.sleep(15000);
//	 	              System.out.println("audit Sequence updated successfully.");
//	 	          } catch (Exception e) {
//	 	              e.printStackTrace();
//	 	          }
 	          
           }
           auditquery="";
           systemcomments="";
           con.close();
    	}
    }
    public LSlogilablimsorderdetail getcurrentsheetrecord(HikariConfig configuration) throws SQLException{
    	LSlogilablimsorderdetail objorder = null;
    	
    	try (HikariDataSource dataSource = new HikariDataSource(configuration);
                Connection con = dataSource.getConnection()) {

    		String oldrecord = "SELECT * FROM lslogilablimsorderdetail WHERE batchcode=?";
       	    try (PreparedStatement pst = con.prepareStatement(oldrecord)) {
                
            	 pst.setLong(1, objorder.getBatchcode());
              //  pst.setTimestamp(2, new Timestamp(gettoDate.getTime()));

                try (ResultSet rs = pst.executeQuery()) {
                    while (rs.next()) {
                    	 objorder = mapResultSetToLslogilabOrder(rs);
                     
                    }
                } catch (SQLException e) {
	                e.printStackTrace(); // Consider logging this properly
	            }	    		
                
       	     }
       	 oldrecord="";
    	}
		return objorder;
    	   
    }
    public void ExecuteAutoRegistration(LSlogilablimsorderdetail objorder , HikariConfig configuration,Date currentdate)throws ParseException, SQLException, IOException, InterruptedException {
    	
    		LSlogilablimsorderdetail objorder1 = null;
    		
	    	try (HikariDataSource dataSource = new HikariDataSource(configuration);
	                Connection con = dataSource.getConnection()) {
	
	    		objorder1=getcurrentsheetrecord(configuration);
           	 
		        objorder1.setLsuserMaster(objorder.getLsuserMaster());
		        objorder1.setLsautoregisterorders(objorder.getLsautoregisterorders());
           	    	

           	    if((objorder1.getRepeat()!=null || objorder1.getRepeat() != false) && objorder1.getLsautoregisterorders()!=null) {
					if(objorder1.getLsautoregisterorders()!= null) {	
						LsAutoregister auditregdetails =  getautoregisterdetails(objorder1.getLsautoregisterorders(),configuration,currentdate,"Sheet_Order");
						objorder1.setLsautoregisterorders(auditregdetails);		
					}
					
					Long Previousbatch = objorder1.getBatchcode();
			        Long clonedbatchcode = new Long(Previousbatch);
			   
			       
//                String deftem = "update LSlogilablimsorderdetail set repeat=false WHERE batchcode=?";
//      	 		try (PreparedStatement pst = con.prepareStatement(deftem)) {
//      	 			pst.setLong(1, objorder.getBatchcode());
//      	 			pst.executeUpdate();
//              }
				
	    		if (objorder1.getLsfile() != null && objorder1.getLsautoregisterorders()!= null) {
	    			
	    			getlsfiledata(objorder1,configuration);
	    			CloudSheetCreation cloudobject=getsheetcreationdata(objorder1,configuration);
	    		
	                 updatesheetordercontent(objorder1,cloudobject,configuration,currentdate);
	                 
//	                 try {
//	         			objorder.setCreatedtimestamp(commonfunction.getCurrentUtcTime());
//	         		} catch (ParseException e) {
//	         			// TODO Auto-generated catch block
//	         			e.printStackTrace();
//	         		}
//	                 Calendar calendar = Calendar.getInstance();
//				        calendar.setTime(objorder.getCreatedtimestamp());
//				        Date createddate = calendar.getTime(); 
				        
//	                 String updateString = "INSERT INTO lslogilablimsorderdetail (approvelaccept ,sentforapprovel,approvelstatus"
//	                 		+ "approved,repeat,filetype,lsworkflow_workflowcode,lsusermaster_usercode,"
//	                 		+ "lsfile_filecode,lsprojectmaster_projectcode,lssamplemaster_samplecode,lssamplefile_filesamplecode,"
//	                 		+ "lsrepositoriesdata_repositorydatacode,filecode,keyword,lockedusername,directorycode,orderdisplaytype,"
//	                 		+ "lstestmasterlocal_testcode,viewoption,ordercancell,teamcode,material_nmaterialcode,materialinventory_nmaterialinventorycode,"
//	                 		+ "elnmaterial_nmaterialcode,lsordernotification_notificationcode,lsAutoregisterorders_regcode) "
//	                 		+ "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
	                 objorder1.setBatchcode(null);
	                 objorder1.setBatchid(null);
	                 
	                 String updateString = "INSERT INTO lslogilablimsorderdetail ("
		                 		+ "repeat,filetype,lsworkflow_workflowcode,lsusermaster_usercode,"
		                 		+ "lsfile_filecode,lsprojectmaster_projectcode,lssamplefile_filesamplecode,"
		                 		+ "filecode,keyword,lockedusername,directorycode,orderdisplaytype,"
		                 		+ "lstestmasterlocal_testcode,viewoption,ordercancell,teamcode,createdtimestamp,orderflag,"
		                 		+ "lsautoregisterorders_regcode,testcode,testname,"
		                 		+ "elnmaterialinventory_nmaterialinventorycode,elnmaterial_nmaterialcode,autoregistercount) "
		                 		+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	                		 
	                		 
		               try (PreparedStatement pst = con.prepareStatement(updateString, Statement.RETURN_GENERATED_KEYS)) {
		            	   if(objorder1.getAutoregistercount()==0) {
		            		   pst.setBoolean(1, false);
		            	   }else {
		            		   pst.setBoolean(1, true);
		            	   }
		                  
		                   pst.setInt(2, objorder1.getFiletype());
		                   pst.setInt(3, objorder1.getLsworkflow().getWorkflowcode());
		                   pst.setInt(4, objorder1.getLsuserMaster().getUsercode());
		                   pst.setInt(5,objorder1.getLsfile().getFilecode());
		                   pst.setInt(6,objorder1.getLsprojectmaster().getProjectcode());
		                  // pst.setInt(7, objorder.getLssamplemaster().getSamplecode());
		                   pst.setInt(7, objorder1.getLssamplefile().getFilesamplecode());
		                   //pst.setInt(9, objorder.getLsrepositoriesdata().getRepositorydatacode());
		                   pst.setInt(8, objorder1.getFilecode());
		                   pst.setString(9,objorder1.getKeyword());
		                   pst.setString(10, objorder1.getLockedusername());
		                   pst.setFloat(11, objorder1.getDirectorycode());
		                   pst.setInt(12, objorder1.getOrderdisplaytype());
		                   pst.setInt(13,objorder1.getLstestmasterlocal().getTestcode());
		                   pst.setInt(14,objorder1.getViewoption());
//		                   pst.setObject(15,objorder.getOrdercancell());
		                   pst.setObject(15,null);
		                   pst.setInt(16, objorder1.getTeamcode());
		                   pst.setTimestamp(17, new Timestamp(currentdate.getTime()));
		                   pst.setString(18, "N");
		                   pst.setLong(19, objorder1.getLsautoregisterorders().getRegcode());
		                   //pst.setObject(20, objorder.getMaterial().getNmaterialcode());
		                   pst.setInt(20,objorder1.getTestcode());
		                   pst.setString(21, objorder1.getTestname());
		                   pst.setObject(22, objorder1.getElnmaterialinventory().getNmaterialinventorycode());
		                   pst.setObject(23, objorder1.getElnmaterial().getNmaterialcode());
		                   pst.setObject(24, objorder1.getAutoregistercount());
		                   int affectedRows=pst.executeUpdate();
		                   
		                   if (affectedRows > 0) {
		                	   ResultSet rs = pst.getGeneratedKeys();
		                       if (rs.next()) {
		                           long generatedBatchcode = rs.getLong(1);
		                           System.out.println("Inserted record's batchcode: " + generatedBatchcode);
		                           objorder1.setBatchcode(generatedBatchcode);

		                       }
		                   } else {
		                       System.out.println("No record inserted.");
		                   }
		               }
		               updateString="";
		               String Batchid = "ELN" + objorder1.getBatchcode();
		       		if (objorder1.getFiletype() == 3) {
		       			Batchid = "RESEARCH" + objorder1.getBatchcode();
		       		} else if (objorder1.getFiletype() == 4) {
		       			Batchid = "EXCEL" + objorder1.getBatchcode();
		       		} else if (objorder1.getFiletype() == 5) {
		       			Batchid = "VALIDATE" + objorder1.getBatchcode();
		       		}
		       		
		       		objorder1.setBatchid(Batchid);
		       		objorder1.setRepeat(true);

		       		String updateString2 = "UPDATE lslogilablimsorderdetail SET Batchid = ? WHERE batchcode = ? ;"
		       				+ "UPDATE lsautoregister set Batchcode = ? where regcode=?;"+
		       				"update LSlogilablimsorderdetail set repeat=false WHERE batchcode=?";
		
		               try (PreparedStatement pst = con.prepareStatement(updateString2)) {
		            	   pst.setString(1, objorder1.getBatchid());
		            	   pst.setLong(2, objorder1.getBatchcode());
		            	   pst.setLong(3,objorder1.getBatchcode());
		            	   pst.setLong(4, objorder1.getLsautoregisterorders().getRegcode());
		            	   pst.setLong(5, clonedbatchcode);
		            	   pst.executeUpdate();
		               }
		               updateString2="";
		              
		               if(objorder1.getRepeat() == true && objorder1.getLsautoregisterorders()!=null) {
		            	   
		            		Date Autoregdate = objorder1.getLsautoregisterorders().getAutocreatedate();
		        	    	Instant auto = Autoregdate.toInstant();
		        	        LocalDateTime autoTime = LocalDateTime.ofInstant(auto, ZoneId.systemDefault());
		            	    scheduleAutoRegisteration(objorder1 , configuration,currentdate,autoTime);
		            	    
		            	    Autoregdate=null;
		            	    auto=null;
		            	    autoTime=null;
		               }
		            
						String comments = "order: "+objorder1.getBatchcode()+" is now autoregistered";
						String screen="IDS_SCN_SHEETORDERS";
						int sitecode=1;
						int usercode = objorder1.getLsuserMaster().getUsercode();
						insertaudit(comments,screen,sitecode,usercode,currentdate,configuration);
						
						 comments="";
						 screen="";
			
	    		}
	    		con.close();
	    	}
    	}
    }
    
   
    
    public void executecautiondatenotification(LSOrdernotification objNotification, HikariConfig configuration) throws ParseException, InterruptedException, SQLException {
    	
    	LSlogilablimsorderdetail order = null;
    	LSlogilabprotocoldetail protocolorder = null;
    	
    	try (HikariDataSource dataSource = new HikariDataSource(configuration);
                Connection con = dataSource.getConnection()) {
    		
    		if(objNotification.getScreen().equals("sheetorder")) {
	    		String orderobj = "SELECT * FROM lslogilablimsorderdetail WHERE batchcode=?";
	       	    try (PreparedStatement pst = con.prepareStatement(orderobj)) {
	                
	            	 pst.setLong(1, objNotification.getBatchcode());
	              //  pst.setTimestamp(2, new Timestamp(gettoDate.getTime()));
	
	                try (ResultSet rs = pst.executeQuery()) {
	                    while (rs.next()) {
	                    	order = mapResultSetToLslogilabOrder(rs);
	                     
	                    }
	                } catch (SQLException e) {
		                e.printStackTrace(); // Consider logging this properly
		            }	    		
	                orderobj="";
	       	     }
    		}else {
       	    
		       	 String protoobj = "SELECT * FROM lslogilabprotocoldetail WHERE protocolordercode=?";
		    	    try (PreparedStatement pst = con.prepareStatement(protoobj)) {
		             
		         	 pst.setLong(1, objNotification.getBatchcode());
		           //  pst.setTimestamp(2, new Timestamp(gettoDate.getTime()));
		
		             try (ResultSet rs = pst.executeQuery()) {
		                 while (rs.next()) {
		                	 protocolorder = mapResultSetToOrderLSlogilabprotocoldetail(rs);
		                  
		                 }
		             } catch (SQLException e) {
			                e.printStackTrace(); // Consider logging this properly
			            }	    		
		             
		    	     }
		    	    protoobj="";
    		  }
    		con.close();
    	}
    	
    	int cancel;
    	int approvelstatus;
    	int completed;
    	
    	if(order==null) {
    		 cancel = protocolorder.getOrdercancell() == null ? 0 : protocolorder.getOrdercancell();
    		 approvelstatus = protocolorder.getApprovelstatus()== null ? 0 :protocolorder.getApprovelstatus();
    		 completed=protocolorder.getCompletedtimestamp() == null ? 0 : 1;
    	}else {
    	   cancel = order.getOrdercancell() == null ? 0 : order.getOrdercancell();
    	   approvelstatus = order.getApprovelstatus()== null ? 0 :order.getApprovelstatus();
    	   completed=order.getCompletedtimestamp() == null ? 0 : 1;
    	}
    	
    	if(completed ==0 && (cancel == 0) && (approvelstatus != 3)){
    		
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
	
	               String notification="ORDERCAUTIONALERT";
	               String updateString = 
	                       "UPDATE LSORDERNOTIFICATION SET cautionstatus = 0 WHERE notificationcode = ?";
	
	               try (PreparedStatement pst = con.prepareStatement(updateString)) {
//	                   pst.setTimestamp(1, new Timestamp(cDate.getTime()));
//	                   pst.setString(2, Details);
//	                   pst.setString(3, path);
//	                   pst.setInt(4, objNotification.getUsercode());
//	                   pst.setInt(5, objNotification.getUsercode());
	                   pst.setLong(1, objNotification.getNotificationcode());
	
	                   pst.executeUpdate();
	               }
	            catch (SQLException e) {
	               e.printStackTrace(); // Consider logging this properly
	           }
	             insernotification(configuration,Details,notification,path,objNotification.getUsercode(),cDate);
	             con.close();
	    	}
    	}
    	//notifyoverduedays(objNotification,configuration);
    }
    
    public void executeduedatenotification(LSOrdernotification objNotification, HikariConfig configuration) throws ParseException, InterruptedException, SQLException {
    	
    	LSlogilablimsorderdetail order = null;
    	LSlogilabprotocoldetail protocolorder = null;
    	
    	try (HikariDataSource dataSource = new HikariDataSource(configuration);
                Connection con = dataSource.getConnection()) {

    		if(objNotification.getScreen().equals("sheetorder")) {
	    		String orderobj = "SELECT * FROM lslogilablimsorderdetail WHERE batchcode=?";
	       	    try (PreparedStatement pst = con.prepareStatement(orderobj)) {
	                
	            	 pst.setLong(1, objNotification.getBatchcode());
	              //  pst.setTimestamp(2, new Timestamp(gettoDate.getTime()));
	
	                try (ResultSet rs = pst.executeQuery()) {
	                    while (rs.next()) {
	                    	order = mapResultSetToLslogilabOrder(rs);
	                     
	                    }
	                } catch (SQLException e) {
		                e.printStackTrace(); // Consider logging this properly
		            }	    		
	                orderobj="";
	       	     }
    		}else {
	       	    
	       	 String protoobj = "SELECT * FROM lslogilabprotocoldetail WHERE protocolordercode=?";
	    	    try (PreparedStatement pst = con.prepareStatement(protoobj)) {
	             
	         	 pst.setLong(1, objNotification.getBatchcode());
	           //  pst.setTimestamp(2, new Timestamp(gettoDate.getTime()));
	
	             try (ResultSet rs = pst.executeQuery()) {
	                 while (rs.next()) {
	                	 protocolorder = mapResultSetToOrderLSlogilabprotocoldetail(rs);
	                  
	                 }
	             } catch (SQLException e) {
		                e.printStackTrace(); // Consider logging this properly
		            }	    		
	             
	    	     }
	    	    protoobj="";
    		}
	    	   
	    	    con.close();
    	}
    	
    	int cancel;
    	int approvelstatus;
    	int completed;
    	
    	if(order==null) {
    		 cancel = protocolorder.getOrdercancell() == null ? 0 : protocolorder.getOrdercancell();
    		 approvelstatus = protocolorder.getApprovelstatus()== null ? 0 :protocolorder.getApprovelstatus();
    		 completed=protocolorder.getCompletedtimestamp() == null ? 0 : 1;
    	}else {
    	   cancel = order.getOrdercancell() == null ? 0 : order.getOrdercancell();
    	   approvelstatus = order.getApprovelstatus()== null ? 0 :order.getApprovelstatus();
    	   completed=order.getCompletedtimestamp() == null ? 0 : 1;
    	}
    	
    	if(completed ==0 && (cancel == 0) && (approvelstatus != 3)){
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
	               String notification = "ORDERONDUEALERT";
	               String updateString ="UPDATE LSORDERNOTIFICATION SET duestatus = 0 WHERE notificationcode = ?";
	
	               try (PreparedStatement pst = con.prepareStatement(updateString)) {
//	                   pst.setTimestamp(1, new Timestamp(cDate.getTime()));
//	                   pst.setString(2, Details);
//	                   pst.setString(3, path);
//	                   pst.setInt(4, objNotification.getUsercode());
//	                   pst.setInt(5, objNotification.getUsercode());
	                   pst.setLong(1, objNotification.getNotificationcode());
	
	                   pst.executeUpdate();
	               }
	            catch (SQLException e) {
	               e.printStackTrace(); // Consider logging this properly
	           }
	    	insernotification(configuration,Details,notification,path,objNotification.getUsercode(),cDate);
	    	con.close();
    	}
      }
    }
    
    public void insernotification(HikariConfig configuration,String Details,String notification,String path,int usercode,Date cDate) throws InterruptedException {
    	//int generatednotificationcode=0;
//    	(SELECT COALESCE(MAX(notificationcode), 0) + 1 FROM lsnotification)
    	try (HikariDataSource dataSource = new HikariDataSource(configuration);
                Connection con = dataSource.getConnection()) {
    	String updateString = "INSERT INTO public.lsnotification( isnewnotification, notification, " +
                "createdtimestamp, notificationdetils, notificationpath, notifationfrom_usercode, " +
                "notifationto_usercode, repositorycode, repositorydatacode, notificationfor) VALUES "
                + "(1, ?, ?, ?, ?, ?, ?, 0, 0, 1)"; 
               
        
        try (PreparedStatement pst = con.prepareStatement(updateString)) {
        	pst.setString(1, notification);
            pst.setTimestamp(2, new Timestamp(cDate.getTime()));
            pst.setString(3, Details);
            pst.setString(4, path);
            pst.setInt(5, usercode);
            pst.setInt(6, usercode);
            //pst.setLong(6, objNotification.getNotificationcode());

           // int affectedRows=
            		pst.executeUpdate();
            
//            if (affectedRows > 0) {
//	             
// 	       	   ResultSet rs = pst.getGeneratedKeys();
// 	              if (rs.next()) {
// 	            	 generatednotificationcode = rs.getInt(1);
// 	                  System.out.println("Inserted record's notificationcode: " + generatednotificationcode);
// 	                  //autoobj.setRegcode(generatedregcode);
// 	                  
// 	              }
// 	          } else {
// 	              System.out.println("No record inserted.");
// 	          }
            
//            Thread.sleep(15000);
//	 	          String updateSequenceSQL = "SELECT setval('notification_sequence', ?)";
//	 	          try (PreparedStatement pstmt = con.prepareStatement(updateSequenceSQL)) {
//	 	              pstmt.setLong(1, generatednotificationcode);
//	 	              pstmt.execute();
//	 	              Thread.sleep(15000);
//	 	              System.out.println("Sequence updated successfully.");
//	 	          } catch (Exception e) {
//	 	              e.printStackTrace();
//	 	          }
	          
        }
        con.close();
    } catch (SQLException e) {
        e.printStackTrace(); // Consider logging this properly
    
    }
    
}
    public void executeoverduenotification(LSOrdernotification objNotification, HikariConfig configuration) throws ParseException, SQLException, InterruptedException {

    	LSlogilablimsorderdetail order = null;
    	LSlogilabprotocoldetail protocolorder = null;
    	
    	try (HikariDataSource dataSource = new HikariDataSource(configuration);
                Connection con = dataSource.getConnection()) {
    		if(objNotification.getScreen().equals("sheetorder")) {

	    		String orderobj = "SELECT * FROM lslogilablimsorderdetail WHERE batchcode=?";
	       	    try (PreparedStatement pst = con.prepareStatement(orderobj)) {
	                
	            	 pst.setLong(1, objNotification.getBatchcode());
	              //  pst.setTimestamp(2, new Timestamp(gettoDate.getTime()));
	
	                try (ResultSet rs = pst.executeQuery()) {
	                    while (rs.next()) {
	                    	order = mapResultSetToLslogilabOrder(rs);
	                     
	                    }
	                } catch (SQLException e) {
		                e.printStackTrace(); // Consider logging this properly
		            }	    		
	                orderobj="";
	       	     }
    		}else {  
	       	    
	       	 String protoobj = "SELECT * FROM lslogilabprotocoldetail WHERE protocolordercode=?";
	    	    try (PreparedStatement pst = con.prepareStatement(protoobj)) {
	             
	         	 pst.setLong(1, objNotification.getBatchcode());
	           //  pst.setTimestamp(2, new Timestamp(gettoDate.getTime()));
	
	             try (ResultSet rs = pst.executeQuery()) {
	                 while (rs.next()) {
	                	 protocolorder = mapResultSetToOrderLSlogilabprotocoldetail(rs);
	                  
	                 }
	             } catch (SQLException e) {
		                e.printStackTrace(); // Consider logging this properly
		            }	    		
	             
	    	     }
	    	    protoobj="";
    		}
	    	    con.close();
	    	}
    	
    	int cancel;
    	int approvelstatus;
    	int completed;
    	
    	if(order==null) {
    		 cancel = protocolorder.getOrdercancell() == null ? 0 : protocolorder.getOrdercancell();
    		 approvelstatus = protocolorder.getApprovelstatus()== null ? 0 :protocolorder.getApprovelstatus();
    		 completed=protocolorder.getCompletedtimestamp() == null ? 0 : 1;
    	}else {
    	   cancel = order.getOrdercancell() == null ? 0 : order.getOrdercancell();
    	   approvelstatus = order.getApprovelstatus()== null ? 0 :order.getApprovelstatus();
    	   completed=order.getCompletedtimestamp() == null ? 0 : 1;
    	}
    	
    	if(completed ==0 && (cancel == 0) && (approvelstatus != 3)){
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
                   String notification ="ORDEROVERDUEALERT";
	                String update = "UPDATE LSORDERNOTIFICATION SET overduestatus = 0 , isduedateexhausted = true WHERE notificationcode = ?";

	                try (PreparedStatement pst = con.prepareStatement(update)) {
	                   
	                    pst.setLong(1, objNotification.getNotificationcode());
	                    pst.executeUpdate();
	                } catch (SQLException e) {
	                e.printStackTrace(); // Consider logging this properly
	            
	                }
	    	    insernotification(configuration,Details,notification,path,objNotification.getUsercode(),cDate);
	    	    con.close();
	    	}
    	}
    	//notifyoverduedays(objNotification,configuration);
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
            con.close();
        } catch (SQLException e) {
            e.printStackTrace(); // Consider logging this properly
        }
    }
   
}