package com.agaram.eln.primary.service.sheetManipulation;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.agaram.eln.primary.commonfunction.commonfunction;
import com.agaram.eln.primary.config.TenantContext;
import com.agaram.eln.primary.fetchmodel.getmasters.Testmaster;
import com.agaram.eln.primary.fetchmodel.getorders.LogilabOrderDetails;
//import com.agaram.eln.primary.commonfunction.commonfunction;
import com.agaram.eln.primary.fetchmodel.gettemplate.Sheettemplatefortest;
import com.agaram.eln.primary.fetchmodel.gettemplate.Sheettemplateget;
import com.agaram.eln.primary.model.cloudFileManip.CloudSheetCreation;
import com.agaram.eln.primary.model.cloudFileManip.CloudSheetVersion;
import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.general.SheetCreation;
import com.agaram.eln.primary.model.general.SheetVersion;
import com.agaram.eln.primary.model.instrumentDetails.LSlogilablimsorderdetail;
import com.agaram.eln.primary.model.instrumentDetails.LsSheetorderlimsrefrence;
import com.agaram.eln.primary.model.masters.Lsrepositories;
import com.agaram.eln.primary.model.protocols.ElnprotocolTemplateworkflow;
import com.agaram.eln.primary.model.protocols.ElnprotocolTemplateworkflowgroupmap;
import com.agaram.eln.primary.model.protocols.Elnprotocolworkflow;
import com.agaram.eln.primary.model.protocols.LSlogilabprotocoldetail;
import com.agaram.eln.primary.model.sheetManipulation.LSfile;
import com.agaram.eln.primary.model.sheetManipulation.LSfileparameter;
import com.agaram.eln.primary.model.sheetManipulation.LSfiletest;
import com.agaram.eln.primary.model.sheetManipulation.LSfileversion;
import com.agaram.eln.primary.model.sheetManipulation.LSsheetupdates;
import com.agaram.eln.primary.model.sheetManipulation.LSsheetworkflow;
import com.agaram.eln.primary.model.sheetManipulation.LSworkflow;
import com.agaram.eln.primary.model.sheetManipulation.Lsfilesharedby;
import com.agaram.eln.primary.model.sheetManipulation.Lsfileshareto;
import com.agaram.eln.primary.model.sheetManipulation.Lsresultfortemplate;
import com.agaram.eln.primary.model.sheetManipulation.Lssheetworkflowhistory;
import com.agaram.eln.primary.model.sheetManipulation.Lstagfortemplate;
import com.agaram.eln.primary.model.sheetManipulation.Notification;
import com.agaram.eln.primary.model.usermanagement.LSMultiusergroup;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.usermanagement.LSnotification;
import com.agaram.eln.primary.model.usermanagement.LSprojectmaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.model.usermanagement.LSusersteam;
import com.agaram.eln.primary.model.usermanagement.LSuserteammapping;
import com.agaram.eln.primary.repository.cfr.LSactivityRepository;
import com.agaram.eln.primary.repository.cfr.LScfttransactionRepository;
import com.agaram.eln.primary.repository.cloudFileManip.CloudSheetCreationRepository;
import com.agaram.eln.primary.repository.cloudFileManip.CloudSheetVersionRepository;
import com.agaram.eln.primary.repository.dashboard.LsActiveWidgetsRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LSlogilablimsorderdetailRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LogilablimsorderdetailsRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LsSheetorderlimsrefrenceRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LsorderworkflowhistoryRepositroy;
import com.agaram.eln.primary.repository.protocol.ElnprotocolTemplateworkflowRepository;
import com.agaram.eln.primary.repository.protocol.ElnprotocolTemplateworkflowgroupmapRepository;
import com.agaram.eln.primary.repository.protocol.ElnprotocolworkflowRepository;
import com.agaram.eln.primary.repository.protocol.ElnprotocolworkflowgroupmapRepository;
import com.agaram.eln.primary.repository.protocol.LSProtocolMasterRepository;
import com.agaram.eln.primary.repository.protocol.LSlogilabprotocoldetailRepository;
import com.agaram.eln.primary.repository.protocol.LSprotocolorderworkflowhistoryRepository;
import com.agaram.eln.primary.repository.protocol.LSprotocolworkflowhistoryRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LSfileRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LSfilemethodRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LSfileparameterRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LSfiletestRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LSfileversionRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LSresultfortemplateRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LSsheetupdatesRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LSsheetworkflowRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LSsheetworkflowgroupmapRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LSworkflowRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LSworkflowgroupmappingRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LsfilesharedbyRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LsfilesharetoRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LssheetworkflowhistoryRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LstagfortemplateRepository;
import com.agaram.eln.primary.repository.sheetManipulation.NotificationRepository;
import com.agaram.eln.primary.repository.usermanagement.LSMultiusergroupRepositery;
import com.agaram.eln.primary.repository.usermanagement.LSnotificationRepository;
import com.agaram.eln.primary.repository.usermanagement.LSprojectmasterRepository;
import com.agaram.eln.primary.repository.usermanagement.LSuserMasterRepository;
import com.agaram.eln.primary.repository.usermanagement.LSusersteamRepository;
import com.agaram.eln.primary.repository.usermanagement.LSuserteammappingRepository;
import com.agaram.eln.primary.service.basemaster.BaseMasterService;
import com.agaram.eln.primary.service.cloudFileManip.CloudFileManipulationservice;
import com.agaram.eln.primary.service.fileManipulation.FileManipulationservice;
import com.agaram.eln.primary.service.masters.MasterService;
import com.agaram.eln.primary.service.material.TransactionService;
import com.agaram.eln.primary.service.protocol.Commonservice;
//import com.agaram.eln.primary.service.protocol.ProtocolService;
import com.mongodb.gridfs.GridFSDBFile;

@Service
@EnableJpaRepositories(basePackageClasses = LSfileRepository.class)
public class FileService {

	@Autowired
	TransactionService transactionService;
	@Autowired
	private LSuserMasterRepository lsusermasterRepository;
	@Autowired
	private LSfileRepository lSfileRepository;
	@Autowired
	private LSfilemethodRepository lSfilemethodRepository;
	@Autowired
	private LSfileparameterRepository lSfileparameterRepository;
	@Autowired
	private LSfiletestRepository lSfiletestRepository;
	@Autowired
	private LSusersteamRepository LSusersteamRepository;
	@Autowired
	private LSworkflowRepository lsworkflowRepository;
	@Autowired
	private CloudFileManipulationservice objCloudFileManipulationservice;
	@Autowired
	private LSnotificationRepository LSnotificationRepository;
	@Autowired
	private LSresultfortemplateRepository LSresultfortemplateRepository;
	@Autowired
	private LstagfortemplateRepository LstagfortemplateRepository;
	@Autowired
	private LSworkflowgroupmappingRepository lsworkflowgroupmappingRepository;

	@Autowired
	private LSactivityRepository lsactivityRepository;

	@Autowired
	private LScfttransactionRepository lscfttransactionRepository;

	@Autowired
	private BaseMasterService masterService;
	@Autowired
	private LSMultiusergroupRepositery lsMultiusergroupRepositery;

	@Autowired
	private LSsheetworkflowRepository lssheetworkflowRepository;

	@Autowired
	private LSsheetworkflowgroupmapRepository lssheetworkflowgroupmapRepository;

	@Autowired
	private LSuserteammappingRepository lsuserteammappingRepository;

	@Autowired
	private LSlogilablimsorderdetailRepository LSlogilablimsorderdetailRepository;

	@Autowired
	private LSfileversionRepository lsfileversionRepository;

	@Autowired
	private LssheetworkflowhistoryRepository lssheetworkflowhistoryRepository;
	@Autowired
	private LSprotocolworkflowhistoryRepository lsprotocolworkflowhistoryRepository;

	@Autowired
	private LsorderworkflowhistoryRepositroy lsorderworkflowhistoryRepositroy;
	@Autowired
	private LSprotocolorderworkflowhistoryRepository lsprotocolorderworkflowhistoryRepository;

	@Autowired
	private LSsheetupdatesRepository lssheetupdatesRepository;

	@Autowired
	private CloudSheetCreationRepository cloudSheetCreationRepository;

	@Autowired
	private CloudSheetVersionRepository cloudSheetVersionRepository;

	@Autowired
	private LsfilesharetoRepository LsfilesharetoRepository;

	@Autowired
	private LsfilesharedbyRepository LsfilesharedbyRepository;

	@Autowired
	GridFsOperations gridFsOps;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private MasterService inventoryservice;

	@Autowired
	private NotificationRepository NotificationRepository;

	@Autowired
	private LsSheetorderlimsrefrenceRepository lssheetorderlimsrefrenceRepository;

	@Autowired
	private FileManipulationservice fileManipulationservice;

	@Autowired
	private GridFsTemplate gridFsTemplate;

	@Autowired
	private LSprojectmasterRepository lsprojectrepo;

	@Autowired
	Commonservice commonservice;

	@Autowired
	private ElnprotocolworkflowRepository elnprotocolworkflowRepository;

	@Autowired
	private ElnprotocolworkflowgroupmapRepository elnprotocolworkflowgroupmapRepository;

	@Autowired
	private LSlogilabprotocoldetailRepository LSlogilabprotocoldetailRepository;

	@Autowired
	private ElnprotocolTemplateworkflowRepository elnprotocoltemplateworkflowRepository;

	@Autowired
	private ElnprotocolTemplateworkflowgroupmapRepository elnprotocolTemplateworkflowgroupmapRepository;

	@Autowired
	private LSProtocolMasterRepository lSProtocolMasterRepository;
	
	@Autowired
	private LsActiveWidgetsRepository LsActiveWidgetsRepository;
	
	@Autowired
	private LogilablimsorderdetailsRepository logilablimsorderdetailsRepository;
	
//	@Autowired
//	private BarcodeMasterRepository barcodeMasterRepository;

	public LSfile InsertupdateSheet(LSfile objfile) throws IOException {

		Boolean Isnew = false;

		String Content = "";

		if (objfile.getIsmultitenant() == 1 || objfile.getIsmultitenant() == 2) {
			Content = objfile.getFilecontent();
		} else {
			byte[] bytes = objfile.getFilecontent().getBytes(StandardCharsets.UTF_16);
			Content = new String(bytes, StandardCharsets.UTF_16);
		}

		List<LSfile> filename  = lSfileRepository.findByfilenameuserIgnoreCaseAndLssitemasterAndRetirestatus(objfile.getFilenameuser().trim(),
				objfile.getLssitemaster(),0);
		
		if (objfile.getFilecode() == null &&  filename!= null &&  filename.size() > 0 && objfile.getRetirestatus() == null) {

			objfile.setResponse(new Response());
			objfile.getResponse().setStatus(false);
			objfile.getResponse().setInformation("ID_SHEET");

			return objfile;
		}

		if (objfile.getLssheetworkflow() == null) {
			objfile.setLssheetworkflow(lssheetworkflowRepository
					.findTopByAndLssitemasterAndStatusOrderByWorkflowcodeAsc(objfile.getLssitemaster(), 1));
		}

		if (objfile.getFilecode() != null && objfile.getFilecode() > 0) {
			UpdateSheetversion(objfile, Content);

			lSfilemethodRepository.deleteByfilecode(objfile.getFilecode());
			List<Integer> lstestparamcode = new ArrayList<Integer>();
			for (LSfileparameter param : objfile.getLsparameter()) {
				if (param.getFileparametercode() != null) {
					lstestparamcode.add(param.getFileparametercode());
				}
			}

			if (lstestparamcode.size() > 0) {
				lSfileparameterRepository.deleteByFilecodeAndFileparametercodeNotIn(objfile.getFilecode(),
						lstestparamcode);
			}

			Isnew = false;
			try {
				objfile.setModifieddate(commonfunction.getCurrentUtcTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {

			Isnew = true;
			try {
				objfile.setCreatedate(commonfunction.getCurrentUtcTime());
				objfile.setModifieddate(commonfunction.getCurrentUtcTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (objfile.getLstest().size() > 0) {
			lSfiletestRepository.save(objfile.getLstest());
		}

		if (objfile.getLsmethods().size() > 0) {
			lSfilemethodRepository.save(objfile.getLsmethods());
		}

		if (objfile.getLsparameter().size() > 0) {
			lSfileparameterRepository.save(objfile.getLsparameter());
		}

		if (objfile.getObjActivity() != null) {
			try {
				objfile.getObjActivity().setActivityDate(commonfunction.getCurrentUtcTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			lsactivityRepository.save(objfile.getObjActivity());
		}
		if (objfile.getModifiedlist() != null) {

			lssheetupdatesRepository.save(objfile.getModifiedlist());
		}
		
		if(objfile.getFilecode() != null){
			LSfile clsFile = lSfileRepository.findByfilecode(objfile.getFilecode());
			objfile.setResultsheet(clsFile.getResultsheet());
		}

		objfile.setFilecontent(null);
		objfile.setRetirestatus(0);
		lSfileRepository.save(objfile);

		if (Isnew) {
			UpdateSheetversion(objfile, Content);
		}
		commonservice.updatefilecontentcheck(Content, objfile, Isnew);
		commonservice.updatenotificationforsheetthread(objfile, true, null, objfile.getIsnewsheet());

		objfile.setResponse(new Response());
		objfile.getResponse().setStatus(true);
		objfile.getResponse().setInformation("ID_SHEETMSG");

		objfile.setVersionno((int) lsfileversionRepository.countByFilecode(objfile.getFilecode()));

		Isnew = null;
//		bytes = null;
		Content = null;

		return objfile;
	}

	public LSfile updateTemplateOnBatch(LSfile objfile) throws IOException {

		Boolean Isnew = true;

		LSfile fileObj = getfileoncode(objfile);

		String fileOriginalContent = fileObj.getFilecontent();

		String Content = commonfunction.getBatchValues(fileOriginalContent, objfile.getFilecontent());

		LSfileversion objLatestversion = lsfileversionRepository
				.findFirstByFilecodeOrderByVersionnoDesc(objfile.getFilecode());

		updatefileversioncontent(Content, objLatestversion, objfile.getIsmultitenant());

		commonservice.updatefilecontentcheck(Content, objfile, Isnew);

		return objfile;
	}

	public void updatefilecontent(String Content, LSfile objfile, Boolean Isnew) {
		// Document Doc = Document.parse(objfile.getFilecontent());
		if (objfile.getIsmultitenant() == 1 || objfile.getIsmultitenant() == 2) {
			CloudSheetCreation objsavefile = new CloudSheetCreation();
			objsavefile.setId((long) objfile.getFilecode());
			objsavefile.setContent(Content);
			cloudSheetCreationRepository.save(objsavefile);

			objsavefile = null;
		} else {

//			String fileid = "file_" + objfile.getFilecode();
			GridFSDBFile largefile = gridFsTemplate
					.findOne(new Query(Criteria.where("filename").is("file_" + objfile.getFilecode())));
			if (largefile != null) {
				gridFsTemplate.delete(new Query(Criteria.where("filename").is("file_" + objfile.getFilecode())));
			}
			gridFsTemplate.store(new ByteArrayInputStream(Content.getBytes(StandardCharsets.UTF_8)),
					"file_" + objfile.getFilecode(), StandardCharsets.UTF_16);
		}

	}

	public List<LSfile> GetSheets(LSuserMaster objuser) {

		if (objuser.getUsername() != null && objuser.getUsername().equals("Administrator")) {
			return lSfileRepository.getsheetGreaterthanone();
		} else {
			return GetSheetsbyuser(objuser);
		}
	}

	public List<LSfile> GetSheetsbyuser(LSuserMaster objuser) {
//		List<LSfile> lstfile = new ArrayList<LSfile>();
//		List<LSuserMaster> lstteamuser = objuser.getObjuser().getTeamusers();

		if (objuser.getObjuser().getTeamusers() != null && objuser.getObjuser().getTeamusers().size() > 0) {
			objuser.getObjuser().getTeamusers().add(objuser);
			return lSfileRepository.getsheetGreaterthanOneAndCreatedByUserIN(objuser.getObjuser().getTeamusers());
		} else {
			objuser.getObjuser().getTeamusers().add(objuser);
			return lSfileRepository.getsheetGreaterthanOneAndCreatedByUserIN(objuser.getObjuser().getTeamusers());
		}
//		return lstfile;
	}

	public List<Sheettemplateget> GetSheetsbyuseronDetailview(LSuserMaster objuser) {
		List<Sheettemplateget> lstfile = new ArrayList<Sheettemplateget>();
		List<LSuserMaster> lstteamuser = objuser.getObjuser().getTeamusers();

		if (lstteamuser != null && lstteamuser.size() > 0) {
			lstteamuser.add(objuser);
			lstfile = lSfileRepository
					.findByFilecodeGreaterThanAndLssitemasterAndViewoptionOrFilecodeGreaterThanAndCreatebyAndViewoptionOrFilecodeGreaterThanAndCreatebyInAndViewoptionOrderByFilecodeDesc(
							1, objuser.getLssitemaster(), 1, 1, objuser, 2, 1, lstteamuser, 3);
		} else {
			lstfile = lSfileRepository
					.findByFilecodeGreaterThanAndLssitemasterAndViewoptionOrFilecodeGreaterThanAndCreatebyInAndViewoptionOrderByFilecodeDesc(
							1, objuser.getLssitemaster(), 1, 1, objuser, 2);
		}

		lstfile.forEach(
				objFile -> objFile.setVersioncout(lsfileversionRepository.countByFilecode(objFile.getFilecode())));

		lstteamuser = null;

		return lstfile;
	}

	public Map<String, Object> getSheetscount(LSuserMaster objusers) {

		Map<String, Object> mapObj = new HashMap<String, Object>();

//		List<LSuserMaster> lstteamuser = objusers.getObjuser().getTeamusers();

		List<LSuserMaster> lstteamuser = objusers.getObjuser().getTeamusers();

		if (lstteamuser != null && lstteamuser.size() > 0) {
			lstteamuser.add(objusers);
			mapObj.put("templatecount", lSfileRepository
					.countByFilecodeGreaterThanAndLssitemasterAndViewoptionOrFilecodeGreaterThanAndCreatebyAndViewoptionOrFilecodeGreaterThanAndCreatebyInAndViewoptionOrderByFilecodeDesc(
							1, objusers.getLssitemaster(), 1, 1, objusers, 2, 1, lstteamuser, 3));
		} else {
			mapObj.put("templatecount", lSfileRepository
					.countByFilecodeGreaterThanAndLssitemasterAndViewoptionOrFilecodeGreaterThanAndCreatebyInAndViewoptionOrderByFilecodeDesc(
							1, objusers.getLssitemaster(), 1, 1, objusers, 2));
		}

//		if (objusers.getObjuser().getTeamusers() != null) {
//			mapObj.put("templatecount", lSfileRepository.countByCreatebyIn(objusers.getObjuser().getTeamusers()));
//		}

		mapObj.put("sharedbyme",
				LsfilesharedbyRepository.countBySharebyunifiedidAndSharestatus(objusers.getSharebyunifiedid(), 1));
		mapObj.put("sharedtome",
				LsfilesharetoRepository.countBySharetounifiedidAndSharestatus(objusers.getSharetounifiedid(), 1));

		return mapObj;
	}

	public List<LSfile> GetApprovedSheets(Integer approvelstatus, LSuserMaster objuser) {
		if (objuser.getUsername().equals("Administrator")) {
			return lSfileRepository.getsheetGreaterthanoneandapprovel(objuser.getLssitemaster().getSitecode());
		} else {
			return GetApprovedSheetsbyuser(approvelstatus, objuser);
		}
	}

	public List<LSfile> GetApprovedSheetswithtestmap(Integer approvelstatus, LSuserMaster objuser) {

		List<LSfile> lstfile = new ArrayList<LSfile>();

		if (objuser.getUsername().equals("Administrator")) {
			lstfile = lSfileRepository
					.findByFilecodeGreaterThanAndLssitemasterAndApprovedAndRetirestatusOrFilecodeGreaterThanAndRetirestatusAndVersionnoGreaterThanOrderByFilecodeDesc(
							-1, objuser.getLssitemaster(), approvelstatus,0, -1, 0,1);
		} else {

//			List<Integer> lstteammap = lsuserteammappingRepository.getTeamcodeByLsuserMaster(objuser.getUsercode());
//			if (lstteammap.size() > 0) {
//				List<LSuserMaster> lstteamuser = lsuserteammappingRepository.getLsuserMasterByTeamcode(lstteammap);
//				lstteamuser.add(objuser);
//				lstfile = lSfileRepository
//						.findByFilecodeGreaterThanAndCreatebyInAndRejectedAndApprovedOrFilecodeGreaterThanAndCreatebyInAndRejectedAndVersionnoGreaterThanOrderByFilecodeDesc(
//								-1, lstteamuser, 0, approvelstatus, -1, lstteamuser, 0, 1);
//			} else {
//				List<LSuserMaster> lstteamuser = new ArrayList<LSuserMaster>();
//				lstteamuser.add(objuser);
//				lstfile = lSfileRepository
//						.findByFilecodeGreaterThanAndCreatebyInAndRejectedAndApprovedOrFilecodeGreaterThanAndCreatebyInAndRejectedAndVersionnoGreaterThanOrderByFilecodeDesc(
//								-1, lstteamuser, 0, approvelstatus, -1, lstteamuser, 0, 1);
//			}

			List<Integer> lstteammap = lsuserteammappingRepository.getTeamcodeByLsuserMaster(objuser.getUsercode());
			if (lstteammap != null && lstteammap.size() > 0) {
				List<LSuserMaster> lstteamuser = lsuserteammappingRepository.getLsuserMasterByTeamcode(lstteammap);
				lstteamuser.add(objuser);

				lstfile = lSfileRepository
						.findByFilecodeGreaterThanAndRetirestatusAndLssitemasterAndViewoptionAndRejectedAndApprovedOrFilecodeGreaterThanAndRetirestatusAndCreatebyAndViewoptionAndRejectedAndApprovedOrFilecodeGreaterThanAndRetirestatusAndCreatebyInAndViewoptionAndRejectedAndApprovedOrderByFilecodeDesc(
								-1,0, objuser.getLssitemaster(), 1, 0, approvelstatus, -1,0, objuser, 2, 0, approvelstatus,
								-1,0, lstteamuser, 3, 0, approvelstatus);
			} else {
				List<LSuserMaster> lstteamuser = new ArrayList<LSuserMaster>();
				lstteamuser.add(objuser);
				lstfile = lSfileRepository
						.findByFilecodeGreaterThanAndRetirestatusAndLssitemasterAndViewoptionAndRejectedAndApprovedOrFilecodeGreaterThanAndRetirestatusAndCreatebyInAndViewoptionAndRejectedAndApprovedOrderByFilecodeDesc(
								-1,0, objuser.getLssitemaster(), 1, 0, approvelstatus, -1,0, lstteamuser, 2, 0,
								approvelstatus);
			}

		}

		return lstfile;
	}

	public List<LSfile> GetApprovedSheetsbyuser(Integer approvelstatus, LSuserMaster objuser) {
		List<LSfile> lstfile = new ArrayList<LSfile>();
//		List<Integer> lstteammap = lsuserteammappingRepository.getTeamcodeByLsuserMaster(objuser.getUsercode());

		if (lsuserteammappingRepository.getTeamcodeByLsuserMaster(objuser.getUsercode()).size() > 0) {
			List<LSuserMaster> lstteamuser = lsuserteammappingRepository.getLsuserMasterByTeamcode(
					lsuserteammappingRepository.getTeamcodeByLsuserMaster(objuser.getUsercode()));
			lstteamuser.add(objuser);
//			lstfile = lSfileRepository.getsheetGreaterthanoneandapprovelanduserIn(approvelstatus, lstteamuser,
//					objuser.getLssitemaster().getSitecode());
			lstfile = lSfileRepository.getsheetapprovelanduserIn(lstteamuser, objuser.getLssitemaster().getSitecode());

			lstteamuser = null;
		} else {
			List<LSuserMaster> lstteamuser = new ArrayList<LSuserMaster>();
			lstteamuser.add(objuser);
//			lstfile = lSfileRepository.getsheetGreaterthanoneandapprovelanduserIn(approvelstatus, lstteamuser,
//					objuser.getLssitemaster().getSitecode());

			lstfile = lSfileRepository.getsheetapprovelanduserIn(lstteamuser, objuser.getLssitemaster().getSitecode());

			lstteamuser = null;
		}
		return lstfile;
	}

	public LSfiletest UpdateFiletest(LSfiletest objtest) {

		if (objtest.getLSfileparameter() != null) {
			lSfileparameterRepository.save(objtest.getLSfileparameter());
		}

		lSfiletestRepository.save(objtest);

		objtest.setResponse(new Response());
		objtest.getResponse().setStatus(true);
		objtest.getResponse().setInformation("ID_SHEETGRP");
		return objtest;
	}

	public List<Sheettemplateget> GetfilesOnTestcode(LSfiletest objtest) {
		List<Sheettemplateget> lsfiles = new ArrayList<Sheettemplateget>();
		List<LSfiletest> lsfiletest = lSfiletestRepository.findByTestcodeAndTesttype(objtest.getTestcode(),
				objtest.getTesttype());

		if (objtest.getObjLoggeduser().getUsername().trim().toLowerCase().equals("administrator")) {
			lsfiles = lSfileRepository.findBylstestInAndApprovedAndRetirestatus(lsfiletest, 1,0);
		} else {
			List<Integer> lstteammap = lsuserteammappingRepository
					.getTeamcodeByLsuserMaster(objtest.getObjLoggeduser().getUsercode());

//			if (lstteammap.size() > 0) {
			List<LSuserMaster> lstteamuser = new ArrayList<LSuserMaster>();
			if(lstteammap != null && lstteammap.size() >0)
			{
				lstteamuser = lsuserteammappingRepository.getLsuserMasterByTeamcode(lstteammap);
			}
				lstteamuser.add(objtest.getObjLoggeduser());
				lsfiles = lSfileRepository
						.findByLssitemasterAndLstestInAndFilecodeGreaterThanAndRetirestatusAndViewoptionAndApprovedOrCreatebyInAndLstestInAndFilecodeGreaterThanAndRetirestatusAndViewoptionAndApprovedOrCreatebyAndLstestInAndFilecodeGreaterThanAndRetirestatusAndViewoptionAndApprovedOrCreatebyInAndLstestInAndFilecodeGreaterThanAndRetirestatusAndViewoptionAndApprovedOrderByFilecodeDesc(
								objtest.getLssitemaster(), lsfiletest, 1,0, 1, 1,
								lstteamuser, lsfiletest, 1,0, 1, 1,
								objtest.getObjLoggeduser(), lsfiletest, 1, 0,2, 1,
								lstteamuser, lsfiletest, 1,0, 3, 1);
				lsfiles.addAll(lSfileRepository
						.findByLssitemasterAndLstestInAndFilecodeGreaterThanAndRetirestatusAndViewoptionAndApprovedAndVersionnoGreaterThanOrCreatebyInAndLstestInAndFilecodeGreaterThanAndRetirestatusAndViewoptionAndApprovedAndVersionnoGreaterThanOrCreatebyAndLstestInAndFilecodeGreaterThanAndRetirestatusAndViewoptionAndApprovedAndVersionnoGreaterThanOrCreatebyInAndLstestInAndFilecodeGreaterThanAndRetirestatusAndViewoptionAndApprovedAndVersionnoGreaterThanOrderByFilecodeDesc(
								objtest.getLssitemaster(), lsfiletest, 1,0, 1, 0, 1,
								lstteamuser, lsfiletest, 1,0, 1, 0, 1,
								objtest.getObjLoggeduser(), lsfiletest, 1,0, 2, 0, 1,
								lstteamuser, lsfiletest, 1,0, 3, 0, 1));
//			} else {
//				List<LSuserMaster> lstteamuser = new ArrayList<LSuserMaster>();
//				lstteamuser.add(objtest.getObjLoggeduser());
//				lsfiles = lSfileRepository
//						.findByCreatebyInAndLstestInAndFilecodeGreaterThanAndRetirestatusAndViewoptionAndApprovedOrCreatebyAndLstestInAndFilecodeGreaterThanAndRetirestatusAndViewoptionAndApprovedOrCreatebyInAndLstestInAndFilecodeGreaterThanAndRetirestatusAndViewoptionAndApproved(
//								lstteamuser, lsfiletest, 1,0, 1, 1, objtest.getObjLoggeduser(), lsfiletest, 1,0, 2, 1,
//								lstteamuser, lsfiletest, 1,0, 3, 1);
//				lsfiles.addAll(lSfileRepository
//						.findByCreatebyInAndLstestInAndFilecodeGreaterThanAndRetirestatusAndViewoptionAndApprovedAndVersionnoGreaterThanOrCreatebyAndLstestInAndFilecodeGreaterThanAndRetirestatusAndViewoptionAndApprovedAndVersionnoGreaterThanOrCreatebyInAndLstestInAndFilecodeGreaterThanAndRetirestatusAndViewoptionAndApprovedAndVersionnoGreaterThan(
//								lstteamuser, lsfiletest, 1,0, 1, 0, 1, objtest.getObjLoggeduser(), lsfiletest, 1,0, 2, 0, 1,
//								lstteamuser, lsfiletest, 1,0, 3, 0, 1));
//			}
		}

		lsfiletest = null;

		return lsfiles;
	}

	public List<LSworkflow> InsertUpdateWorkflow(LSworkflow[] workflow) {
		List<LSworkflow> lstworkflow1 = Arrays.asList(workflow);
		for (LSworkflow flow : lstworkflow1) {
			lsworkflowgroupmappingRepository.save(flow.getLsworkflowgroupmapping());
			lsworkflowRepository.save(flow);
		}

		lstworkflow1.get(0).setResponse(new Response());
		lstworkflow1.get(0).getResponse().setStatus(true);
		lstworkflow1.get(0).getResponse().setInformation("ID_SHEETMSG");

		return lstworkflow1;
	}

	public List<LSworkflow> GetWorkflow(LSworkflow objflow) {

		if (objflow.getObjsilentaudit() != null) {
			objflow.getObjsilentaudit().setTableName("LSworkflow");
			try {
				objflow.getObjsilentaudit().setTransactiondate(commonfunction.getCurrentUtcTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			lscfttransactionRepository.save(objflow.getObjsilentaudit());
		}

		return lsworkflowRepository.findByLssitemasterOrderByWorkflowcodeAsc(objflow.getLssitemaster());
	}

	public Response Deleteworkflow(LSworkflow objflow) {
		Response response = new Response();

		long onprocess = LSlogilablimsorderdetailRepository.countByLsworkflowAndOrderflag(objflow, "N");
		if (onprocess > 0) {
			response.setStatus(false);

		} else {
			LSlogilablimsorderdetailRepository.setWorkflownullforcompletedorder(objflow);
			lsorderworkflowhistoryRepositroy.setWorkflownullforHistory(objflow);
			lsworkflowRepository.delete(objflow);
			lsworkflowgroupmappingRepository.delete(objflow.getLsworkflowgroupmapping());
			response.setStatus(true);

		}

		return response;
	}

	public Map<String, Object> GetMastersfororders(LSuserMaster objuser) {
		Map<String, Object> mapOrders = new HashMap<String, Object>();

		mapOrders.put("test", masterService.getTestmaster(objuser));
		mapOrders.put("sample", masterService.getsamplemaster(objuser));
		mapOrders.put("project", masterService.getProjectmaster(objuser));
		mapOrders.put("sheets", GetApprovedSheets(0, objuser));
		Lsrepositories lsrepositories = new Lsrepositories();
		lsrepositories.setSitecode(objuser.getLssitemaster().getSitecode());
		mapOrders.put("inventories", inventoryservice.Getallrepositories(lsrepositories));

		lsrepositories = null;

		return mapOrders;
	}

	public Map<String, Object> GetMastersforordercreate(LSuserMaster objuser) {

		Map<String, Object> mapOrders = new HashMap<String, Object>();
		Map<String, Object> mapReq4Material = new HashMap<String, Object>();
		mapOrders.put("test", masterService.getTestmaster(objuser));
		mapOrders.put("sample", masterService.getsamplemaster(objuser));
		List<LSprojectmaster> prolist = lsprojectrepo.findByLsusersteamInAndStatusAndLssitemasterOrderByProjectcodeDesc(
				LSusersteamRepository.findByLsuserteammappingInAndStatusAndLssitemaster(lsuserteammappingRepository.findBylsuserMaster(objuser), 1,objuser.getLssitemaster())
				, 1,objuser.getLssitemaster());
		mapOrders.put("project", prolist);
		Lsrepositories lsrepositories = new Lsrepositories();
		lsrepositories.setSitecode(objuser.getLssitemaster().getSitecode());
		mapOrders.put("inventories", inventoryservice.Getallrepositories(lsrepositories));
		mapOrders.put("sheets", GetApprovedSheets(0, objuser));
		mapReq4Material.put("sitecode", objuser.getLssitemaster().getSitecode());
		mapOrders.put("limsInventory", transactionService.getMaterialLst4DashBoard(mapReq4Material));
		mapOrders.put("material", transactionService.getMaterials(objuser));
		lsrepositories = null;
		return mapOrders;
	}

	public Map<String, Object> GetMastersforsheetsetting(LSuserMaster objuser) {
		Map<String, Object> mapOrders = new HashMap<String, Object>();

		mapOrders.put("limstest", masterService.getLimsTestMaster(objuser));
		mapOrders.put("test", masterService.getTestmaster(objuser));

		List<LSfile> lstfiles = GetApprovedSheetswithtestmap(1, objuser);
		List<Sheettemplatefortest> listfiles = new ArrayList<Sheettemplatefortest>();

		if (lstfiles != null && lstfiles.size() > 0) {
			listfiles = lstfiles.stream().map(lsfile -> new Sheettemplatefortest(lsfile.getFilecode(),
					lsfile.getFilenameuser(), lsfile.getCreatedate(), lsfile.getLstest(),lsfile.getTagsheet())).collect(Collectors.toList());
		}

		mapOrders.put("sheets", listfiles);

		lstfiles = null;
		listfiles = null;

		return mapOrders;
	}

	public List<LSsheetworkflow> InsertUpdatesheetWorkflow(LSsheetworkflow[] sheetworkflow) {

		List<LSsheetworkflow> lSsheetworkflow = Arrays.asList(sheetworkflow);
		for (LSsheetworkflow flow : lSsheetworkflow) {
			lssheetworkflowgroupmapRepository.save(flow.getLssheetworkflowgroupmap());
			lssheetworkflowRepository.save(flow);
		}

		lSsheetworkflow.get(0).setResponse(new Response());
		lSsheetworkflow.get(0).getResponse().setStatus(true);
		lSsheetworkflow.get(0).getResponse().setInformation("ID_SHEETMSG");
		return lSsheetworkflow;
	}

	public List<LSsheetworkflow> GetsheetWorkflow(LSsheetworkflow objuser) {

		if (objuser.getObjsilentaudit() != null) {
			objuser.getObjsilentaudit().setTableName("LSsheetworkflow");
			try {
				objuser.getObjsilentaudit().setTransactiondate(commonfunction.getCurrentUtcTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			lscfttransactionRepository.save(objuser.getObjsilentaudit());
		}

		return lssheetworkflowRepository.findBylssitemasterAndStatusOrderByWorkflowcodeAsc(objuser.getLssitemaster(),
				1);
	}

	public Response Deletesheetworkflow(LSsheetworkflow objflow) {
		Response response = new Response();

		long onprocess = lSfileRepository.countByLssheetworkflowAndApprovedOrApprovedIsNull(objflow, 0);
		if (onprocess > 0) {
			response.setStatus(false);
		} else {
//			lSfileRepository.setWorkflownullforApprovedfile(objflow, 1);
			lssheetworkflowhistoryRepository.setWorkflownullforHistory(objflow);
			lsfileversionRepository.setWorkflownullforHistory(objflow);
			objflow.setStatus(-1);
			lssheetworkflowRepository.save(objflow);
			lssheetworkflowgroupmapRepository.save(objflow.getLssheetworkflowgroupmap());
			response.setStatus(true);
			if (objflow.getObjsilentaudit() != null) {
				objflow.getObjsilentaudit().setTableName("LSsheetworkflow");
				try {
					objflow.getObjsilentaudit().setTransactiondate(commonfunction.getCurrentUtcTime());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				lscfttransactionRepository.save(objflow.getObjsilentaudit());
			}
		}
		return response;
	}

	public LSfile updateworkflowforFile(LSfile objfile) {

		LSfile objcurrentfile = lSfileRepository.findByfilecode(objfile.getFilecode());

		if (objfile.getViewoption() == null || objfile.getViewoption() != null && objfile.getViewoption() != 2) {
			updatenotificationforsheet(objfile, false, objcurrentfile.getLssheetworkflow(), false);
		}
		for (int k = 0; k < objfile.getLssheetworkflowhistory().size(); k++) {
			if (objfile.getLssheetworkflowhistory().get(k).getHistorycode() == null) {
				try {
					objfile.getLssheetworkflowhistory().get(k).setCreatedate(commonfunction.getCurrentUtcTime());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		lssheetworkflowhistoryRepository.save(objfile.getLssheetworkflowhistory());
		lSfileRepository.updateFileWorkflow(objfile.getLssheetworkflow(), objfile.getApproved(), objfile.getRejected(),
				objfile.getFilecode());

		if (objfile.getLssheetworkflowhistory().get(objfile.getLssheetworkflowhistory().size() - 1)
				.getObjsilentaudit() != null) {
			objfile.getLssheetworkflowhistory().get(objfile.getLssheetworkflowhistory().size() - 1).getObjsilentaudit()
					.setTableName("LSfile");
			try {
				objfile.getLssheetworkflowhistory().get(objfile.getLssheetworkflowhistory().size() - 1)
						.getObjsilentaudit().setTransactiondate(commonfunction.getCurrentUtcTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			lscfttransactionRepository.save(objfile.getLssheetworkflowhistory()
					.get(objfile.getLssheetworkflowhistory().size() - 1).getObjsilentaudit());
		}
		if (objfile.getViewoption() == null || objfile.getViewoption() != null && objfile.getViewoption() != 2) {
			if (objfile.getFilenameuser() != null) {
				LSworkflow objlastworkflow = lsworkflowRepository
						.findTopByAndLssitemasterOrderByWorkflowcodeDesc(objfile.getObjLoggeduser().getLssitemaster());
				if (objlastworkflow != null
						&& objfile.getCurrentStep().getWorkflowcode() == objlastworkflow.getWorkflowcode()) {
					objfile.setIsfinalstep(1);
				} else {
					objfile.setIsfinalstep(0);
				}
			}
			updatenotificationforsheetworkflowapproval(objfile, false, objcurrentfile.getLssheetworkflow(), false);
		}
		objcurrentfile = null;

		return objfile;
	}

	private void updatenotificationforsheetworkflowapproval(LSfile objfile, Boolean isNew,
			LSsheetworkflow previousworkflow, Boolean IsNewsheet) {
		List<LSuserteammapping> objteam = lsuserteammappingRepository
				.findByTeamcodeNotNullAndLsuserMaster(objfile.getLSuserMaster());

		try {
			if (objteam != null && objteam.size() > 0) {
				String Details = "";
				String Notifiction = "";
				List<LSnotification> lstnotifications = new ArrayList<LSnotification>();
				LSuserMaster createby = lsusermasterRepository.findByusercode(objfile.getCreateby().getUsercode());
				List<Integer> notifiedUsers = new ArrayList<Integer>();

				if (!createby.getUsercode().equals(objfile.getObjLoggeduser().getUsercode())) {
					LSnotification notify = new LSnotification();
					if (objfile.getApproved() == 1) {
						Notifiction = "SHEETAPPROVALSENT";
					} else if (objfile.getRejected() == 1) {
						Notifiction = "SHEETAPPROVALREJECT";
					} else if (objfile.getApproved() == 2) {
						Notifiction = "SHEETRETURN";
					} else if (objfile.getApproved() == 0) {
						Notifiction = "SHEETAPPROVALSENTNEXT";
					}

					notify.setNotifationto(createby);
					Details = "{\"ordercode\":\"" + objfile.getFilecode() + "\", \"order\":\""
							+ objfile.getFilenameuser() + "\", \"currentworkflow\":\""
							+ previousworkflow.getWorkflowname() + "\", \"username\":\""
							+ objfile.getLSuserMaster().getUsername() + "\"}";
					notify.setNotifationfrom(objfile.getObjLoggeduser());
					notify.setNotificationdate(objfile.getNotificationdate());
					notify.setNotification(Notifiction);
					notify.setNotificationdetils(Details);
					notify.setIsnewnotification(1);
					notify.setNotificationpath("/sheetcreation");
					notify.setNotificationfor(1);

					LSnotificationRepository.save(notify);
				}

				for (int k = 0; k < objfile.getLssheetworkflow().getLssheetworkflowgroupmap().size(); k++) {
					List<LSMultiusergroup> userobj = lsMultiusergroupRepositery.findBylsusergroup(
							objfile.getLssheetworkflow().getLssheetworkflowgroupmap().get(k).getLsusergroup());

					List<Integer> objnotifyuser = userobj.stream().map(LSMultiusergroup::getUsercode)
							.collect(Collectors.toList());
					List<LSuserMaster> objuser = lsusermasterRepository
							.findByUsercodeInAndUserretirestatusNot(objnotifyuser, 1);

					for (int i = 0; i < objuser.size(); i++) {
						if (objfile.getApproved() != null && objfile.getApproved() != 1
								&& objfile.getIsfinalstep() != 1) {
							if (!objuser.get(i).getUsercode().equals(objfile.getObjLoggeduser().getUsercode())
									&& !notifiedUsers.contains(objuser.get(i).getUsercode())) {
								LSnotification objnotify = new LSnotification();
								if (objfile.getApproved() == 0 && objfile.getRejected() != 1
										&& objfile.getApproved() != 2) {
									Notifiction = "SHEETAPPROVAL";

									Details = "{\"ordercode\":\"" + objfile.getFilecode() + "\", \"order\":\""
											+ objfile.getFilenameuser() + "\", \"username\":\""
											+ objfile.getLSuserMaster().getUsername() + "\"}";

									objnotify.setNotifationto(objuser.get(i));
									objnotify.setNotifationfrom(objfile.getObjLoggeduser());
									objnotify.setNotificationdate(objfile.getNotificationdate());
									objnotify.setNotification(Notifiction);
									objnotify.setNotificationdetils(Details);
									objnotify.setIsnewnotification(1);
									objnotify.setNotificationpath("/sheetcreation");
									objnotify.setNotificationfor(1);

									lstnotifications.add(objnotify);
									notifiedUsers.add(objuser.get(i).getUsercode());
								}
							}
						}
					}

				}
				LSnotificationRepository.save(lstnotifications);
			}
		} catch (Exception e) {

		}

		objteam = null;
	}

	public CompletableFuture<LSfile> updatenotificationforsheet(LSfile objFile, Boolean isNew,
			LSsheetworkflow previousworkflow, Boolean IsNewsheet) {
		List<LSnotification> lstnotifications = new ArrayList<LSnotification>();
		List<LSuserteammapping> objteam = lsuserteammappingRepository
				.findByTeamcodeNotNullAndLsuserMaster(objFile.getLSuserMaster());

		if (objteam != null && objteam.size() > 0) {
			String Details = "";
			String Notifiction = "";

			if (!isNew) {

				if (objFile.getApproved() != null && objFile.getApproved() == 0) {
					Notifiction = "SHEETMOVED";
				}
				if (objFile.getApproved() != null && objFile.getApproved() == 2 && objFile.getRejected() != 1) {
					Notifiction = "SHEETRETURNED";
				} else if (objFile.getApproved() != null && objFile.getApproved() == 1) {
					Notifiction = "SHEETAPPROVED";
				} else if (objFile.getRejected() != null && objFile.getRejected() == 1) {
					Notifiction = "SHEETREJECTED";
				}

				int perviousworkflowcode = previousworkflow != null ? previousworkflow.getWorkflowcode() : -1;
				String previousworkflowname = previousworkflow != null ? previousworkflow.getWorkflowname() : "";

				Details = "{\"ordercode\":\"" + objFile.getFilecode() + "\", \"order\":\"" + objFile.getFilenameuser()
						+ "\", \"previousworkflow\":\"" + previousworkflowname + "\", \"previousworkflowcode\":\""
						+ perviousworkflowcode + "\", \"currentworkflow\":\""
						+ objFile.getLssheetworkflow().getWorkflowname() + "\", \"currentworkflowcode\":\""
						+ objFile.getLssheetworkflow().getWorkflowcode() + "\"}";

				List<LSuserMaster> lstnotified = new ArrayList<LSuserMaster>();

				for (int i = 0; i < objteam.size(); i++) {
					LSusersteam objteam1 = LSusersteamRepository.findByteamcode(objteam.get(i).getTeamcode());

//					List<LSuserteammapping> lstusers = objteam1.getLsuserteammapping();
					List<LSuserteammapping> lstusers = lsuserteammappingRepository.findByteamcode(objteam1.getTeamcode());

					for (int j = 0; j < lstusers.size(); j++) {

						if (objFile.getObjLoggeduser().getUsercode() != lstusers.get(j).getLsuserMaster()
								.getUsercode()) {
							if (lstnotified.contains(lstusers.get(j).getLsuserMaster()))
								continue;

							lstnotified.add(lstusers.get(j).getLsuserMaster());
							LSnotification objnotify = new LSnotification();
							if (IsNewsheet) {
								objnotify.setNotificationdate(objFile.getCreatedate());
							} else if (!IsNewsheet) {
								objnotify.setNotificationdate(objFile.getNotificationdate());
							} else {
								objnotify.setNotificationdate(objFile.getCreatedate());
							}
							objnotify.setNotifationfrom(objFile.getLSuserMaster());
							objnotify.setNotifationto(lstusers.get(j).getLsuserMaster());
//								objnotify.setNotificationdate(objFile.getCreatedate());
							objnotify.setNotification(Notifiction);
							objnotify.setNotificationdetils(Details);
							objnotify.setIsnewnotification(1);
							objnotify.setNotificationpath("/sheetcreation");
							objnotify.setNotificationfor(2);

							lstnotifications.add(objnotify);
						}
					}
					objteam1 = null;
					lstusers = null;
				}
				lstnotified = null;
			} else {
				Notifiction = IsNewsheet == true ? "SHEETCREATED" : "SHEETMODIFIED";
				Details = "{\"ordercode\":\"" + objFile.getFilecode() + "\", \"order\":\"" + objFile.getFilenameuser()
						+ "\", \"previousworkflow\":\"" + "" + "\", \"previousworkflowcode\":\"" + -1
						+ "\", \"currentworkflow\":\"" + objFile.getLssheetworkflow().getWorkflowname()
						+ "\", \"currentworkflowcode\":\"" + objFile.getLssheetworkflow().getWorkflowcode() + "\"}";

				List<LSuserMaster> lstnotified = new ArrayList<LSuserMaster>();

				for (int i = 0; i < objteam.size(); i++) {
					LSusersteam objteam1 = LSusersteamRepository.findByteamcode(objteam.get(i).getTeamcode());

//					List<LSuserteammapping> lstusers = objteam1.getLsuserteammapping();
					List<LSuserteammapping> lstusers = lsuserteammappingRepository.findByteamcode(objteam1.getTeamcode());

					for (int j = 0; j < lstusers.size(); j++) {

						if (objFile.getLSuserMaster().getUsercode() != lstusers.get(j).getLsuserMaster()
								.getUsercode()) {
							if (lstnotified.contains(lstusers.get(j).getLsuserMaster()))
								continue;

							lstnotified.add(lstusers.get(j).getLsuserMaster());
							LSnotification objnotify = new LSnotification();
							if (IsNewsheet) {
								objnotify.setNotificationdate(objFile.getCreatedate());
							} else if (!IsNewsheet) {
								objnotify.setNotificationdate(objFile.getModifieddate());
							} else {
								objnotify.setNotificationdate(objFile.getCreatedate());
							}
							objnotify.setNotifationfrom(objFile.getLSuserMaster());
							objnotify.setNotifationto(lstusers.get(j).getLsuserMaster());
							objnotify.setNotificationdate(objFile.getCreatedate());
							objnotify.setNotification(Notifiction);
							objnotify.setNotificationdetils(Details);
							objnotify.setIsnewnotification(1);
							objnotify.setNotificationpath("/sheetcreation");
							objnotify.setNotificationfor(2);

							lstnotifications.add(objnotify);
						}
					}

					objteam1 = null;
					lstusers = null;
				}
				lstnotified = null;
			}

			LSnotificationRepository.save(lstnotifications);

			Details = null;
			Notifiction = null;
		}

		objteam = null;
		lstnotifications = null;
		return null;
	}

	public Map<String, Object> lockorder(Map<String, Object> objMap) throws Exception {

		Long BatchID = null;
		int usercode = 0;
		String username = "";
		;
		if (objMap.containsKey("Batch")) {
			BatchID = Long.valueOf((Integer) objMap.get("Batch"));
		}
		if (objMap.containsKey("usercode")) {
			usercode = (Integer) objMap.get("usercode");
		}
		if (objMap.containsKey("username")) {
			username = (String) objMap.get("username");
		}
		LSlogilablimsorderdetail orderDetail = LSlogilablimsorderdetailRepository.findOne(BatchID);
		Integer sLockeduser = 0;
		if (orderDetail != null) {
			sLockeduser = orderDetail.getLockeduser();

			if (sLockeduser == null || sLockeduser == 0) {

				orderDetail.setLockeduser(usercode);
				orderDetail.setLockedusername(username);

				LSlogilablimsorderdetailRepository.save(orderDetail);

				orderDetail.setResponse(new Response());
				orderDetail.getResponse().setStatus(true);
				orderDetail.getResponse().setInformation("ID_LOCKMSG");

			} else {
				orderDetail.setResponse(new Response());
				orderDetail.getResponse().setStatus(false);
				orderDetail.getResponse().setInformation("ID_LOCKFAIL");
			}

			objMap.put("response", orderDetail);

			return objMap;
		}
		username = null;

		return objMap;
	}

	@SuppressWarnings("null")
	public Map<String, Object> unlockorder(Map<String, Object> objMap) throws Exception {
		Long BatchID = null;

		if (objMap.containsKey("Batch")) {
			BatchID = Long.valueOf((Integer) objMap.get("Batch"));
		}

		Integer userCode = Integer.parseInt(objMap.get("usercode").toString());
		LogilabOrderDetails orderDetail = logilablimsorderdetailsRepository.findByBatchcode(BatchID);

		if (orderDetail != null) {

//			orderDetail.setLockeduser(null);
//			orderDetail.setLockedusername(null);
//			orderDetail.setActiveuser(null);
//
//			LSlogilablimsorderdetailRepository.save(orderDetail);
//
//			orderDetail.setResponse(new Response());
//			orderDetail.getResponse().setStatus(true);
//			orderDetail.getResponse().setInformation("ID_UNLOCKMSG");
			if (userCode != null && orderDetail.getLockeduser() != null
					&& userCode.equals(orderDetail.getLockeduser())) {
				orderDetail.setLockeduser(null);
				orderDetail.setLockedusername(null);
				orderDetail.setActiveuser(null);
				logilablimsorderdetailsRepository.UpdateOrderOnunlockData(orderDetail.getBatchcode());
//				LSlogilablimsorderdetailRepository.save(orderDetail);

				orderDetail.setResponse(new Response());
				orderDetail.getResponse().setStatus(true);
				orderDetail.getResponse().setInformation("ID_UNLOCKMSG");
			} else {
				orderDetail.setResponse(new Response());
				orderDetail.getResponse().setStatus(true);
				orderDetail.getResponse().setInformation("IDS_ORDER_LOCKED_BY_DIFF_USER");
			}
			objMap.put("response", orderDetail);
		} else {
			orderDetail.setResponse(new Response());
			orderDetail.getResponse().setStatus(false);
			orderDetail.getResponse().setInformation("ID_UNLOCKFAIL");
		}
		orderDetail = null;

		return objMap;
	}

	@SuppressWarnings("null")
	public Map<String, Object> unlockorderOnViewClose(Map<String, Object> objMap) throws Exception {
		Long BatchID = null;
		Long protocolordercode = null;
		LSlogilabprotocoldetail Protocol_Order = new LSlogilabprotocoldetail();
		if (objMap.containsKey("Batch")) {
			BatchID = Long.valueOf((Integer) objMap.get("Batch"));
		}
		if (objMap.containsKey("protocolordercode")) {
			protocolordercode = Long.valueOf((Integer) objMap.get("protocolordercode"));
		}

		Integer userCode = Integer.parseInt(objMap.get("usercode").toString());
		LSlogilablimsorderdetail orderDetail=new LSlogilablimsorderdetail();
		if(BatchID!=null) {
	    orderDetail = LSlogilablimsorderdetailRepository.findOne(BatchID);
		}
		
		if (protocolordercode != null) {
			Protocol_Order = LSlogilabprotocoldetailRepository.findOne(protocolordercode);
		}

		if (orderDetail.getBatchcode() != null) {

			if (userCode != null && orderDetail.getLockeduser() != null
					&& userCode.equals(orderDetail.getLockeduser())) {
				orderDetail.setLockeduser(null);
				orderDetail.setLockedusername(null);
				orderDetail.setActiveuser(null);

				LSlogilablimsorderdetailRepository.save(orderDetail);

				orderDetail.setResponse(new Response());
				orderDetail.getResponse().setStatus(true);
				orderDetail.getResponse().setInformation("ID_UNLOCKMSG");
			} else {
				orderDetail.setResponse(new Response());
				orderDetail.getResponse().setStatus(true);
				orderDetail.getResponse().setInformation("IDS_ORDER_LOCKED_BY_DIFF_USER");
			}

			objMap.put("response", orderDetail);
		} else {
			orderDetail.setResponse(new Response());
			orderDetail.getResponse().setStatus(false);
			orderDetail.getResponse().setInformation("ID_UNLOCKFAIL");
		}

		if(Protocol_Order.getProtocolordercode() != null) {
			if (userCode != null && Protocol_Order.getLockeduser() != null
					&& userCode.equals(Protocol_Order.getLockeduser())) {
				Protocol_Order.setLockeduser(null);
				Protocol_Order.setLockedusername(null);
				Protocol_Order.setActiveuser(null);
				LSlogilabprotocoldetailRepository.save(Protocol_Order);
			}
		}
		orderDetail = null;

		return objMap;
	}

	public boolean UpdateSheetversion(LSfile objfile, String orginalcontent) throws IOException {
		int Versionnumber = 0;
		String Content = "";
		LSfile objesixting = lSfileRepository.findByfilecode(objfile.getFilecode());
		LSfileversion objLatestversion = lsfileversionRepository
				.findFirstByFilecodeOrderByVersionnoDesc(objfile.getFilecode());

		if (objLatestversion != null) {
			Versionnumber = objLatestversion.getVersionno();
		}

		if (objesixting == null) {
			objesixting = objfile;
		}

		if (objesixting.getApproved() != null && objesixting.getApproved() == 1 && objfile.getApproved() == 1) {
			Versionnumber++;

			LSsheetworkflow objfirstworkflow = lssheetworkflowRepository
					.findTopByAndLssitemasterOrderByWorkflowcodeAsc(objfile.getLssitemaster());
			objfile.setApproved(0);
			objfile.setLssheetworkflow(objfirstworkflow);

			if (objfile.getObjsheetworkflowhistory() != null) {
				objfile.getObjsheetworkflowhistory().setCurrentworkflow(objfirstworkflow);
				objfile.getObjsheetworkflowhistory().setFilecode(objfile.getFilecode());

				if (objfile.getLssheetworkflowhistory() != null) {
					objfile.getLssheetworkflowhistory().add(objfile.getObjsheetworkflowhistory());
					lssheetworkflowhistoryRepository.save(objfile.getLssheetworkflowhistory());
				}
			}

			LSfileversion objversion = new LSfileversion();

			objversion.setApproved(objesixting.getApproved());
			objversion.setCreateby(objesixting.getCreateby());
			objversion.setCreatedate(objesixting.getCreatedate());
			objversion.setExtension(objesixting.getExtension());
			objversion.setFilecode(objesixting.getFilecode());
			objversion.setFilenameuser(objesixting.getFilenameuser());
			objversion.setFilenameuuid(objesixting.getFilenameuuid());
			objversion.setIsactive(objesixting.getIsactive());
			objversion.setLssheetworkflow(objesixting.getLssheetworkflow());
			objversion.setLssitemaster(objesixting.getLssitemaster());
			objversion.setModifiedby(objfile.getModifiedby());
			objversion.setModifieddate(objfile.getModifieddate());
			objversion.setRejected(objesixting.getRejected());
			objversion.setVersionno(Versionnumber);

			if (objfile.getLsfileversion() != null) {
				objfile.getLsfileversion().add(objversion);
			} else {
				List<LSfileversion> lstversion = new ArrayList<LSfileversion>();
				lstversion.add(objversion);
				objfile.setLsfileversion(lstversion);
			}

			lsfileversionRepository.save(objfile.getLsfileversion());

			if (objfile.getIsmultitenant() == 1||objfile.getIsmultitenant() == 2) {
				
				CloudSheetCreation objCreation = cloudSheetCreationRepository.findById((long) objfile.getFilecode());

				if (objCreation != null && objCreation.getContainerstored() == 0) {
					Content = cloudSheetCreationRepository.findById((long) objfile.getFilecode()).getContent();
				} else {
					Content = objCloudFileManipulationservice.retrieveCloudSheets(objCreation.getFileuid(),
							commonfunction.getcontainername(objfile.getIsmultitenant(), TenantContext.getCurrentTenant())
							+ "sheetcreation");
				}
			} else {
				GridFSDBFile largefile = gridFsTemplate
						.findOne(new Query(Criteria.where("filename").is("file_" + objfile.getFilecode())));
				if (largefile == null) {
					largefile = gridFsTemplate
							.findOne(new Query(Criteria.where("_id").is("file_" + objfile.getFilecode())));
				}

				if (largefile != null) {
					Content = new BufferedReader(
							new InputStreamReader(largefile.getInputStream(), StandardCharsets.UTF_8)).lines()
							.collect(Collectors.joining("\n"));
				} else {
					if (mongoTemplate.findById(objfile.getFilecode(), SheetCreation.class) != null) {
						Content = mongoTemplate.findById(objfile.getFilecode(), SheetCreation.class).getContent();
					}
				}
			}

			updatefileversioncontent(Content, objLatestversion, objfile.getIsmultitenant());
			updatefileversioncontent(orginalcontent, objversion, objfile.getIsmultitenant());
		} else if (Versionnumber == 0) {
			Versionnumber++;
			LSfileversion objversion = new LSfileversion();

			objversion.setApproved(objesixting.getApproved());
			objversion.setCreateby(objesixting.getCreateby());
			objversion.setCreatedate(objesixting.getCreatedate());
			objversion.setExtension(objesixting.getExtension());
			if (objesixting.getFilecode() != null) {
				objversion.setFilecode(objesixting.getFilecode());
			}
			objversion.setFilenameuser(objesixting.getFilenameuser());
			objversion.setFilenameuuid(objesixting.getFilenameuuid());
			objversion.setIsactive(objesixting.getIsactive());
			objversion.setLssheetworkflow(objesixting.getLssheetworkflow());
			objversion.setLssitemaster(objesixting.getLssitemaster());
			objversion.setModifiedby(objfile.getModifiedby());
			objversion.setModifieddate(objfile.getModifieddate());
			objversion.setRejected(objesixting.getRejected());
			objversion.setVersionno(Versionnumber);

			if (objfile.getLsfileversion() != null) {
				objfile.getLsfileversion().add(objversion);
			} else {
				List<LSfileversion> lstversion = new ArrayList<LSfileversion>();
				lstversion.add(objversion);
				objfile.setLsfileversion(lstversion);
			}

			lsfileversionRepository.save(objfile.getLsfileversion());

			updatefileversioncontent(orginalcontent, objversion, objfile.getIsmultitenant());
		} else {
			updatefileversioncontent(orginalcontent, objLatestversion, objfile.getIsmultitenant());
		}

		objfile.setVersionno(Versionnumber);

		Content = null;
		objesixting = null;
		objLatestversion = null;

		return true;
	}

	public void updatefileversioncontent(String Content, LSfileversion objfile, Integer ismultitenant)
			throws IOException {
		if (ismultitenant == 1 || ismultitenant == 2) {

			String tenant = TenantContext.getCurrentTenant();
			if(ismultitenant == 2)
			{
				tenant = "freeusers";
			}
			
			Map<String, Object> objMap = objCloudFileManipulationservice.storecloudSheetsreturnwithpreUUID(Content,
					tenant + "sheetversion");
			String fileUUID = (String) objMap.get("uuid");
			String fileURI = objMap.get("uri").toString();

			CloudSheetVersion objsavefile = new CloudSheetVersion();
			if (objfile.getFileversioncode() != null) {
				objsavefile.setId((long) objfile.getFileversioncode());
			} else {
				objsavefile.setId(1);
			}
			objsavefile.setFileuri(fileURI);
			objsavefile.setFileuid(fileUUID);
			objsavefile.setContainerstored(1);
			cloudSheetVersionRepository.save(objsavefile);

			objsavefile = null;
		} else {

			GridFSDBFile largefile = gridFsTemplate
					.findOne(new Query(Criteria.where("filename").is("fileversion_" + objfile.getFileversioncode())));
			if (largefile != null) {
				gridFsTemplate.delete(
						new Query(Criteria.where("filename").is("fileversion_" + objfile.getFileversioncode())));
			}
			gridFsTemplate.store(new ByteArrayInputStream(Content.getBytes(StandardCharsets.UTF_8)),
					"fileversion_" + objfile.getFileversioncode(), StandardCharsets.UTF_16);

		}
	}

	public List<LSfileversion> Getfileversions(LSfile objfile) {
		return lsfileversionRepository.findByFilecodeOrderByVersionnoDesc(objfile.getFilecode());
	}

	public List<Lssheetworkflowhistory> Getfilehistory(LSfile objfile) {
		return lssheetworkflowhistoryRepository.findByFilecode(objfile.getFilecode());
	}

	public String GetfileverContent(LSfile objfile) throws IOException {
		String Content = "";

		if (objfile.getVersionno() == 0) {
			LSfile objesixting = lSfileRepository.findByfilecode(objfile.getFilecode());
			Content = objesixting.getFilecontent();
			if (objfile != null) {
				if (objfile.getIsmultitenant() == 1 || objfile.getIsmultitenant() == 2) {
					CloudSheetCreation objCreation = cloudSheetCreationRepository
							.findById((long) objfile.getFilecode());

					if (objCreation != null && objCreation.getContainerstored() == 0) {
						Content = cloudSheetCreationRepository.findById((long) objfile.getFilecode()).getContent();
					} else {
						Content = objCloudFileManipulationservice.retrieveCloudSheets(objCreation.getFileuid(),
								commonfunction.getcontainername(objfile.getIsmultitenant(), TenantContext.getCurrentTenant())
								 + "sheetcreation");
					}
				} else {
					GridFSDBFile largefile = gridFsTemplate
							.findOne(new Query(Criteria.where("filename").is("file_" + objfile.getFilecode())));
					if (largefile == null) {
						largefile = gridFsTemplate
								.findOne(new Query(Criteria.where("_id").is("file_" + objfile.getFilecode())));
					}

					if (largefile != null) {
						Content = new BufferedReader(
								new InputStreamReader(largefile.getInputStream(), StandardCharsets.UTF_8)).lines()
								.collect(Collectors.joining("\n"));
					} else {
						if (mongoTemplate.findById(objfile.getFilecode(), SheetCreation.class) != null) {
							Content = mongoTemplate.findById(objfile.getFilecode(), SheetCreation.class).getContent();
						}
					}

				}
			}
			objesixting = null;
		} else {
			LSfileversion objVersion = lsfileversionRepository
					.findByFilecodeAndVersionnoOrderByVersionnoDesc(objfile.getFilecode(), objfile.getVersionno());
			Content = objVersion.getFilecontent();
			if (objVersion != null) {
				if (objfile.getIsmultitenant() == 1 || objfile.getIsmultitenant() == 2) {
					CloudSheetVersion objCreation = cloudSheetVersionRepository
							.findById((long) objVersion.getFileversioncode());

					if (objCreation != null && objCreation.getContainerstored() == 0) {
						Content = cloudSheetVersionRepository.findById((long) objVersion.getFileversioncode())
								.getContent();
					} else {
						Content = objCloudFileManipulationservice.retrieveCloudSheets(objCreation.getFileuid(),
								commonfunction.getcontainername(objfile.getIsmultitenant(), TenantContext.getCurrentTenant())
								 + "sheetversion");
					}
				} else {
					GridFSDBFile largefile = gridFsTemplate.findOne(
							new Query(Criteria.where("filename").is("fileversion_" + objVersion.getFileversioncode())));
					if (largefile == null) {
						largefile = gridFsTemplate.findOne(
								new Query(Criteria.where("_id").is("fileversion_" + objVersion.getFileversioncode())));
					}
					if (largefile != null) {
						Content = new BufferedReader(
								new InputStreamReader(largefile.getInputStream(), StandardCharsets.UTF_8)).lines()
								.collect(Collectors.joining("\n"));
					} else {
						if (mongoTemplate.findById(objVersion.getFileversioncode(), SheetVersion.class) != null) {
							Content = mongoTemplate.findById(objVersion.getFileversioncode(), SheetVersion.class)
									.getContent();
						}
					}

				}
			}
			objVersion = null;
		}

		return Content;
	}

	public List<LSlogilablimsorderdetail> getSheetOrder(LSlogilablimsorderdetail objClass) {
		return LSlogilablimsorderdetailRepository.findAll();
	}

	public LSfile getFileDetails(LSfile objfile) throws IOException {
		LSfile objreturnfile = lSfileRepository.findByfilecode(objfile.getFilecode());
		return objreturnfile;
	}

	public LSfile getfileoncode(LSfile objfile) throws IOException {
		LSfile objreturnfile = lSfileRepository.findByfilecode(objfile.getFilecode());

		objreturnfile.setModifiedlist(new ArrayList<LSsheetupdates>());
		objreturnfile.getModifiedlist().addAll(lssheetupdatesRepository.findByfilecode(objfile.getFilecode()));
		if (objreturnfile != null) {
			if (objfile.getIsmultitenant() == 1 || objfile.getIsmultitenant() == 2) {
				
				CloudSheetCreation objCreation = cloudSheetCreationRepository.findById((long) objfile.getFilecode());

				if (objCreation != null && objCreation.getContainerstored() == 0) {
					objreturnfile.setFilecontent(
							cloudSheetCreationRepository.findById((long) objfile.getFilecode()).getContent());
				} else {
					objreturnfile.setFilecontent(objCloudFileManipulationservice.retrieveCloudSheets(
							objCreation.getFileuid(), commonfunction.getcontainername(objfile.getIsmultitenant(), TenantContext.getCurrentTenant()) + "sheetcreation"));
				}
			} else {
				GridFSDBFile largefile = gridFsTemplate
						.findOne(new Query(Criteria.where("filename").is("file_" + objfile.getFilecode())));
				if (largefile == null) {
					largefile = gridFsTemplate
							.findOne(new Query(Criteria.where("_id").is("file_" + objfile.getFilecode())));
				}

				if (largefile != null) {
					objreturnfile.setFilecontent(new BufferedReader(
							new InputStreamReader(largefile.getInputStream(), StandardCharsets.UTF_8)).lines()
							.collect(Collectors.joining("\n")));
				} else {
					if (mongoTemplate.findById(objfile.getFilecode(), SheetCreation.class) != null) {
						objreturnfile.setFilecontent(
								mongoTemplate.findById(objfile.getFilecode(), SheetCreation.class).getContent());
					}
				}

			}
		}

		return objreturnfile;
	}

	public Sheettemplateget getfilemasteroncode(LSfile objfile) {
		Sheettemplateget objreturnfile = lSfileRepository.findByFilecode(objfile.getFilecode());

		objreturnfile.setVersioncout(lsfileversionRepository.countByFilecode(objfile.getFilecode()));

		return objreturnfile;
	}

	public Map<String, Object> Getinitialsheet(LSfile objfile) {
		Map<String, Object> mapOrders = new HashMap<String, Object>();
		if (objfile.getLSuserMaster().getUsername().trim().toLowerCase().equals("administrator")) {
			mapOrders.put("template", Getadministratorsheets(objfile));
			mapOrders.put("templatecount", lSfileRepository.countByFilecodeGreaterThan(1));
		} else {
			if (objfile.getLSuserMaster().getObjuser().getTeamusers() != null
					&& objfile.getLSuserMaster().getObjuser().getTeamusers().size() > 0) {
				objfile.getLSuserMaster().getObjuser().getTeamusers().add(objfile.getLSuserMaster());
				mapOrders.put("templatecount",
						lSfileRepository.countByCreatebyIn(objfile.getLSuserMaster().getObjuser().getTeamusers()));
			} else {
				mapOrders.put("templatecount", lSfileRepository.countByCreateby(objfile.getLSuserMaster()));
			}
			mapOrders.put("template", Getusersheets(objfile));

		}
		return mapOrders;
	}

	public List<Sheettemplateget> Getremainingsheets(LSfile objfile) {
		if (objfile.getLSuserMaster().getUsername().trim().toLowerCase().equals("administrator")) {
			return Getadministratorsheets(objfile);
		} else {
			return Getusersheets(objfile);
		}
	}

	public List<Sheettemplateget> Getadministratorsheets(LSfile objfile) {
		List<Sheettemplateget> lstsheets = new ArrayList<Sheettemplateget>();
		if (objfile.getFilecode() == 0) {
			lstsheets = lSfileRepository.findFirst20ByFilecodeGreaterThanOrderByFilecodeDesc(1);
		} else {
			lstsheets = lSfileRepository.findFirst20ByFilecodeGreaterThanAndFilecodeLessThanOrderByFilecodeDesc(1,
					objfile.getFilecode());
		}
		return lstsheets;
	}

	public List<Sheettemplateget> Getusersheets(LSfile objfile) {
		List<Sheettemplateget> lstsheets = new ArrayList<Sheettemplateget>();
		List<LSuserMaster> lstteamuser = objfile.getLSuserMaster().getObjuser().getTeamusers();
		if (objfile.getFilecode() == 0) {
			if (lstteamuser != null && lstteamuser.size() > 0) {
				lstteamuser.add(objfile.getLSuserMaster());
				lstsheets = lSfileRepository.findFirst20ByCreatebyInOrderByFilecodeDesc(lstteamuser);
			} else {
				lstsheets = lSfileRepository.findFirst20ByCreatebyOrderByFilecodeDesc(objfile.getLSuserMaster());
			}
		} else {
			if (lstteamuser != null && lstteamuser.size() > 0) {
				lstteamuser.add(objfile.getLSuserMaster());
				lstsheets = lSfileRepository.findFirst20ByFilecodeLessThanAndCreatebyInOrderByFilecodeDesc(
						objfile.getFilecode(), lstteamuser);
			} else {
				lstsheets = lSfileRepository.findFirst20ByFilecodeLessThanAndCreatebyOrderByFilecodeDesc(
						objfile.getFilecode(), objfile.getLSuserMaster());
			}
		}

		return lstsheets;
	}

	public LSfile UpdateFilecontent(LSfile objfile) {

		updatefilecontent(objfile.getFilecontent(), objfile, false);

		return null;
	}

	public Lsfileshareto Insertsharefile(Lsfileshareto objprotocolordershareto) {

		Lsfileshareto existingshare = LsfilesharetoRepository.findBySharebyunifiedidAndSharetounifiedidAndSharefilecode(
				objprotocolordershareto.getSharebyunifiedid(), objprotocolordershareto.getSharetounifiedid(),
				objprotocolordershareto.getSharefilecode());

		if (existingshare != null) {
			objprotocolordershareto.setRetirestatus(objprotocolordershareto.getRetirestatus());
			objprotocolordershareto.setSharetofilecode(existingshare.getSharetofilecode());
		}

		LsfilesharetoRepository.save(objprotocolordershareto);
		updatenotificationforsheetshare(objprotocolordershareto);
		existingshare = null;
		return objprotocolordershareto;
	}

	public Lsfilesharedby Insertsharefileby(Lsfilesharedby objprotocolordersharedby) {

		Lsfilesharedby existingshare = LsfilesharedbyRepository
				.findBySharebyunifiedidAndSharetounifiedidAndSharefilecode(
						objprotocolordersharedby.getSharebyunifiedid(), objprotocolordersharedby.getSharetounifiedid(),
						objprotocolordersharedby.getSharefilecode());

		if (existingshare != null) {
			objprotocolordersharedby.setSharedbytofilecode(existingshare.getSharedbytofilecode());
		}

		LsfilesharedbyRepository.save(objprotocolordersharedby);

		return existingshare;
	}

	private void updatenotificationforsheetshare(Lsfileshareto objprotocolordershareto) {

		String Details = "";
		String Notifiction = "";

		Notifiction = "SHEETSHARE";
		Details = "{\"shareduser\":\"" + objprotocolordershareto.getSharebyusername() + "\", \"privileges\":\""
				+ objprotocolordershareto.getSharerights() + "\", \"sheet\":\""
				+ objprotocolordershareto.getSharefilename() + "\"}";

		LSnotification objnotify = new LSnotification();
		objnotify.setNotifationfrom(objprotocolordershareto.getObjLoggeduser());
		objnotify.setNotificationdate(objprotocolordershareto.getSharedon());
		objnotify.setNotifationto(objprotocolordershareto.getSharetousercode());
		objnotify.setNotification(Notifiction);
		objnotify.setNotificationdetils(Details);
		objnotify.setIsnewnotification(1);
		objnotify.setNotificationpath("/sheetcreation");
		objnotify.setNotificationfor(1);

		LSnotificationRepository.save(objnotify);
		Details = null;
		Notifiction = null;
		objnotify = null;
	}

	public List<Lsfilesharedby> Getfilesharedbyme(Lsfilesharedby lsordersharedby) {
		return LsfilesharedbyRepository.findBySharebyunifiedidAndSharestatusOrderBySharedbytofilecodeDesc(
				lsordersharedby.getSharebyunifiedid(), 1);
	}

	public List<Lsfileshareto> Getfilesharetome(Lsfileshareto lsordershareto) {
		List<Lsfileshareto> lstReturn = LsfilesharetoRepository
				.findBySharetounifiedidAndSharestatusOrderBySharetofilecodeDesc(lsordershareto.getSharetounifiedid(),
						1);
		if (lstReturn.size() > 0) {
			lstReturn.stream().peek(f -> {
				if(f.getSharefilecode()!=null) {
					LSfile objFile = lSfileRepository.findByfilecode(Long.valueOf(f.getSharefilecode()).intValue());
					JSONObject jsonObject = new JSONObject(f.getShareitemdetails());
					jsonObject.put("fileversioncount", objFile.getVersionno());
					f.setShareitemdetails(jsonObject.toString());
				}
				
			}).collect(Collectors.toList());
		}
		return lstReturn;
	}

	public Lsfilesharedby Unsharefileby(Lsfilesharedby objordershareby) {

		Lsfilesharedby existingshare = LsfilesharedbyRepository
				.findBySharedbytofilecode(objordershareby.getSharedbytofilecode());

		existingshare.setSharestatus(0);
		existingshare.setUnsharedon(objordershareby.getUnsharedon());
		LsfilesharedbyRepository.save(existingshare);

		return existingshare;
	}

	public Lsfileshareto Unsharefileto(Lsfileshareto lsordershareto) {

		Lsfileshareto existingshare = LsfilesharetoRepository
				.findBySharetofilecode(lsordershareto.getSharetofilecode());

		existingshare.setSharestatus(0);
		existingshare.setUnsharedon(lsordershareto.getUnsharedon());
		existingshare.setSharedbytofilecode(lsordershareto.getSharedbytofilecode());
		LsfilesharetoRepository.save(existingshare);

		return existingshare;
	}

	public Boolean updateSharedFile(Lsfilesharedby lsordersharedby) {
		Lsfilesharedby lsfile = LsfilesharedbyRepository
				.findBySharedbytofilecode(lsordersharedby.getSharedbytofilecode());

		if (lsfile != null) {

			lsfile.setShareitemdetails(lsordersharedby.getShareitemdetails());

			LsfilesharedbyRepository.save(lsfile);

		}

		lsfile = null;

		return true;
	}

	public Boolean updateSharedToFile(Lsfileshareto lsordersharedby) {

		Lsfileshareto lsfile = LsfilesharetoRepository.findBySharetofilecode(lsordersharedby.getSharetofilecode());

		if (lsfile != null) {

			lsfile.setShareitemdetails(lsordersharedby.getShareitemdetails());

			LsfilesharetoRepository.save(lsfile);

		}
		lsfile = null;

		return true;
	}

	public void ValidateNotification(Notification objnotification) throws ParseException {
		NotificationRepository.save(objnotification);
		scheduleNotification(objnotification);
	}
	
	public void scheduleNotification(Notification objnotification) throws ParseException {
		
		Date cautionDate = objnotification.getCautiondate();
		Instant caution = cautionDate.toInstant();
		
		LocalDateTime cautionTime = LocalDateTime.ofInstant(caution, ZoneId.systemDefault());
		
		LocalDateTime currentTime = LocalDateTime.now();
		
		if(cautionTime.isAfter(currentTime) && objnotification != null) {
			Duration duration = Duration.between(currentTime, cautionTime);
			long delay = duration.toMillis();
			scheduleNotification(objnotification ,delay);
		}
		
	}
	
	private Map<Integer, TimerTask> scheduledTasks = new HashMap<>();

//	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	
	private void scheduleNotification(Notification objNotification , long delay) {
		TimerTask task = new TimerTask() {
			@SuppressWarnings("unlikely-arg-type")
			public void run() {
				try {
					executeNotificationPop(objNotification);
				} catch (Exception e) {
					e.printStackTrace();
				}
				scheduledTasks.remove(objNotification.getNotificationid());
			}
		};
		Timer timer = new Timer();
		timer.schedule(task, delay);
		scheduledTasks.put(Integer.parseInt(objNotification.getNotificationid().toString()), task);
		
//		scheduler.schedule(new Runnable() {
//			@Override
//			public void run() {
//				try {
//					executeNotificationPop(objNotification);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		}, delay, TimeUnit.MILLISECONDS);
	}


	protected void executeNotificationPop(Notification objNotification) throws ParseException {
		LocalDateTime localDateTime = LocalDateTime.now();
	    Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
		Date cDate = Date.from(instant);
		
		objNotification.setCurrentdate(cDate);
		
		loginnotification(objNotification);
	}
	
	public void loginnotification(Notification objNotification) throws ParseException {

		LSuserMaster LSuserMaster = new LSuserMaster();
		LSuserMaster.setUsercode(objNotification.getUsercode());

		LSnotification LSnotification = new LSnotification();

		String Details = "{\"ordercode\" :\"" + objNotification.getOrderid() + "\",\"order\" :\""
				+ objNotification.getBatchid() + "\",\"description\":\"" + objNotification.getDescription()
				+ "\",\"screen\":\"" + objNotification.getScreen() + "\"}";

		LSnotification.setIsnewnotification(1);
		LSnotification.setNotification("CAUTIONALERT");
		LSnotification.setNotificationdate(objNotification.getCurrentdate());
		LSnotification.setNotificationdetils(Details);
		LSnotification.setNotificationpath(objNotification.getScreen().equals("Sheet Order") ? "/registertask" : "/Protocolorder");
		LSnotification.setNotifationfrom(LSuserMaster);
		LSnotification.setNotifationto(LSuserMaster);
		LSnotification.setRepositorycode(0);
		LSnotification.setRepositorydatacode(0);
		LSnotification.setNotificationfor(1);

		objNotification.setStatus(0);
		LSnotificationRepository.save(LSnotification);
		NotificationRepository.save(objNotification);
		
	}

	public Map<String, Object> UploadLimsFile(MultipartFile file, Long batchcode, String filename) throws IOException {

		System.out.print("Inside UploadLimsFile");

		Map<String, Object> mapObj = new HashMap<String, Object>();

		LsSheetorderlimsrefrence objattachment = new LsSheetorderlimsrefrence();

		if (fileManipulationservice.storeLimsSheetRefrence(file) != null) {
			objattachment.setFileid(fileManipulationservice.storeLimsSheetRefrence(file).getId());

			LSfile classFile = lSfileRepository.findByfilecode(batchcode.intValue());
			classFile.setFilenameuuid(fileManipulationservice.storeLimsSheetRefrence(file).getId());
			classFile.setExtension(".pdf");

			lSfileRepository.save(classFile);

			classFile = null;
		}

		objattachment.setFilename(filename);
		objattachment.setBatchcode(batchcode);

		lssheetorderlimsrefrenceRepository.save(objattachment);

		mapObj.put("elnSheet", objattachment);

		objattachment = null;

		return mapObj;
	}

	public LSfile updatefilename(LSfile objfile) {

		LSfile fileByName = lSfileRepository.findByfilecode(objfile.getFilecode());
		if (lSfileRepository.findByFilecodeNotAndLssitemasterAndFilenameuserIgnoreCase(objfile.getFilecode(),
				objfile.getLssitemaster(), objfile.getFilenameuser()).isEmpty()) {
			if (fileByName.getFilecode() != null) {
				fileByName.setFilenameuser(objfile.getFilenameuser());
				fileByName.setCategory(objfile.getCategory());
				fileByName.setModifiedby(objfile.getModifiedby());
				fileByName.setModifieddate(objfile.getModifieddate());
				fileByName.setViewoption(objfile.getViewoption());
				lSfileRepository.save(fileByName);
				fileByName.setResponse(new Response());
				fileByName.getResponse().setStatus(true);
				return fileByName;

			} else {
				objfile.setResponse(new Response());
				objfile.getResponse().setStatus(false);
				objfile.getResponse().setInformation("EDIT IN SOME PROBLEMS");

				return objfile;
			}
		} else {
			objfile.setResponse(new Response());
			objfile.getResponse().setStatus(false);
			objfile.getResponse().setInformation("IDS_MSG_SHEETNAMEEXIST");

			return objfile;
		}
	}

	public List<Elnprotocolworkflow> GetProtocolOrderWorkflow(Elnprotocolworkflow objflow) {

		if (objflow.getObjsilentaudit() != null) {
			objflow.getObjsilentaudit().setTableName("Elnprotocolworkflow");
			try {
				objflow.getObjsilentaudit().setTransactiondate(commonfunction.getCurrentUtcTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			lscfttransactionRepository.save(objflow.getObjsilentaudit());
		}

		return elnprotocolworkflowRepository.findByLssitemasterOrderByWorkflowcodeAsc(objflow.getLssitemaster());

	}

	public List<Elnprotocolworkflow> InsertUpdateprotocolorderWorkflow(Elnprotocolworkflow[] workflow) {
		List<Elnprotocolworkflow> lstworkflow1 = Arrays.asList(workflow);
		for (Elnprotocolworkflow flow : lstworkflow1) {
			elnprotocolworkflowgroupmapRepository.save(flow.getElnprotocolworkflowgroupmap());
			elnprotocolworkflowRepository.save(flow);
		}

		lstworkflow1.get(0).setResponse(new Response());
		lstworkflow1.get(0).getResponse().setStatus(true);
		lstworkflow1.get(0).getResponse().setInformation("ID_SHEETMSG");

		return lstworkflow1;
	}

	public Response Deleteprotocolorderworkflow(Elnprotocolworkflow objflow) {
		Response response = new Response();

		long onprocess = LSlogilabprotocoldetailRepository.countByelnprotocolworkflowAndOrderflag(objflow, "N");
		if (onprocess > 0) {
			response.setStatus(false);

		} else {
			LSlogilabprotocoldetailRepository.setWorkflownullforcompletedorder(objflow);
			lsprotocolorderworkflowhistoryRepository.setWorkflownullforHistory(objflow);
			elnprotocolworkflowRepository.delete(objflow);
			elnprotocolworkflowgroupmapRepository.delete(objflow.getElnprotocolworkflowgroupmap());
			response.setStatus(true);

		}

		return response;
	}

	public List<ElnprotocolTemplateworkflow> GetProtocoltempleteWorkflow(ElnprotocolTemplateworkflow objuser) {

		if (objuser.getObjsilentaudit() != null) {
			objuser.getObjsilentaudit().setTableName("LSsheetworkflow");
			try {
				objuser.getObjsilentaudit().setTransactiondate(commonfunction.getCurrentUtcTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			lscfttransactionRepository.save(objuser.getObjsilentaudit());
		}

		return elnprotocoltemplateworkflowRepository
				.findBylssitemasterAndStatusOrderByWorkflowcodeAsc(objuser.getLssitemaster(), 1);
	}

	public List<ElnprotocolTemplateworkflow> InsertUpdateprotocoltemplateWorkflow(
			ElnprotocolTemplateworkflow[] elnprotocolTemplateworkflow) {
		long  obj=0;
		obj=elnprotocoltemplateworkflowRepository.count();
		List<ElnprotocolTemplateworkflow> elnprotocolTemplateworkflowobj = Arrays.asList(elnprotocolTemplateworkflow);
		Integer ismultitenant=elnprotocolTemplateworkflowobj.get(0).getIsmultitenant();
		for (ElnprotocolTemplateworkflow flow : elnprotocolTemplateworkflowobj) {
			
			if(ismultitenant==1) {
				if (flow.getWorkflowcode() == 0) {
					if(obj==0) {
						flow.setWorkflowcode(1);
					}else {
						int workflowcode = elnprotocoltemplateworkflowRepository.getlargeworkflowecode();
						flow.setWorkflowcode(workflowcode + 1);
					}
					elnprotocoltemplateworkflowRepository.customInsertElnprotocolTemplateworkflow(flow.getWorkflowcode(),
							flow.getStatus(), flow.getWorkflowname(), flow.getLssitemaster().getSitecode());
					List<ElnprotocolTemplateworkflowgroupmap> updatedGroupMapList = flow
							.getElnprotocoltemplateworkflowgroupmap().stream()
							.peek(groupMap -> groupMap.setWorkflowcode(flow.getWorkflowcode()))
							.collect(Collectors.toList());
					elnprotocolTemplateworkflowgroupmapRepository.save(updatedGroupMapList);

				} else {
					elnprotocolTemplateworkflowgroupmapRepository.save(flow.getElnprotocoltemplateworkflowgroupmap());
					elnprotocoltemplateworkflowRepository.save(flow);
				}
			}else {
				elnprotocolTemplateworkflowgroupmapRepository.save(flow.getElnprotocoltemplateworkflowgroupmap());
				elnprotocoltemplateworkflowRepository.save(flow);
			}

		

//			
		}
//		List<ElnprotocolTemplateworkflow> inobj = elnprotocolTemplateworkflowobj.stream()
//				.filter(items -> items.getWorkflowcode() == 0).collect(Collectors.toList());
//		if (!inobj.isEmpty()) {
//			for (ElnprotocolTemplateworkflow flow : inobj) {
//				elnprotocolTemplateworkflowgroupmapRepository.save(flow.getElnprotocoltemplateworkflowgroupmap());
//				int workflowcode= elnprotocoltemplateworkflowRepository.getlargeworkflowecode();
//				flow.setWorkflowcode(workflowcode+1);
//				elnprotocoltemplateworkflowRepository.save(flow);
//			}
//		}
		elnprotocolTemplateworkflowobj.get(0).setResponse(new Response());
		elnprotocolTemplateworkflowobj.get(0).getResponse().setStatus(true);
		elnprotocolTemplateworkflowobj.get(0).getResponse().setInformation("ID_SHEETMSG");
		return elnprotocolTemplateworkflowobj;
	}

	public Response DeleteprotocolTemplateworkflow(ElnprotocolTemplateworkflow objflow) {
		Response response = new Response();

		long onprocess = lSProtocolMasterRepository.countByElnprotocoltemplateworkflowAndApprovedOrApprovedIsNull(objflow, 0);
		if (onprocess > 0) {
			response.setStatus(false);
		} else {

			lsprotocolworkflowhistoryRepository.setWorkflownullforHistory(objflow.getWorkflowcode());
			objflow.setStatus(-1);
			elnprotocoltemplateworkflowRepository.save(objflow);
			elnprotocolTemplateworkflowgroupmapRepository.save(objflow.getElnprotocoltemplateworkflowgroupmap());
			response.setStatus(true);
			if (objflow.getObjsilentaudit() != null) {
				objflow.getObjsilentaudit().setTableName("ElnprotocolTemplateworkflow");
				try {
					objflow.getObjsilentaudit().setTransactiondate(commonfunction.getCurrentUtcTime());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				lscfttransactionRepository.save(objflow.getObjsilentaudit());
			}
		}
		return response;
	}

	public List<ElnprotocolTemplateworkflow> GetprotocoltemplateWorkflow(ElnprotocolTemplateworkflow objuser) {

		if (objuser.getObjsilentaudit() != null) {
			objuser.getObjsilentaudit().setTableName("ElnprotocolTemplateworkflow");
			try {
				objuser.getObjsilentaudit().setTransactiondate(commonfunction.getCurrentUtcTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			lscfttransactionRepository.save(objuser.getObjsilentaudit());
		}

		return elnprotocoltemplateworkflowRepository
				.findBylssitemasterAndStatusOrderByWorkflowcodeAsc(objuser.getLssitemaster(), 1);
	}

	public LSfile lsfileRetire(LSfile objfile) {
		LSfile file = lSfileRepository.findByfilecode(objfile.getFilecode());
		file.setRetirestatus(objfile.getRetirestatus());
		lSfileRepository.save(file);		
		
		//shareto retirestatus update				
		LsfilesharetoRepository.updateRetirestatus(objfile.getFilecode());
		
		//shareby retirestatus update				
		LsfilesharedbyRepository.updateRetirestatus(objfile.getFilecode());
		
		//Dashboard lsactivewidgets update
		LsActiveWidgetsRepository.updateRetirestatus(objfile.getFilecode());
		return objfile;
	}

	public LSsheetupdates updatetSheetTemplateTransaction(LSsheetupdates objsheetupdates) throws ParseException {
		if (objsheetupdates.getFilecode() != null) {
			if (objsheetupdates.getSheetmodifiedDate() != null) {
				objsheetupdates.setSheetmodifiedDate(commonfunction.getCurrentUtcTime());
			}
			lssheetupdatesRepository.save(objsheetupdates);
		}
		return objsheetupdates;
	}

	public Map<String, Object> GetSheetTransactionDetails(LSfile objfile) {
		Map<String, Object> mapObj = new HashMap<String, Object>();
		List<LSsheetupdates> modifiedlist = lssheetupdatesRepository.findByfilecode(objfile.getFilecode());
		mapObj.put("modifiedlist", modifiedlist);
		return mapObj;
	}
	
	public boolean Validatesheetcountforfreeuser(LSSiteMaster lssitemaster)
	{
		boolean countexceeded = false;
		long sheetcount = lSfileRepository.countByLssitemaster(lssitemaster);
		if(sheetcount < 10)
		{
			countexceeded = false;
		}
		else
		{
			countexceeded = true;
		}
		return countexceeded;
	}

	public Map<String, Object> getApprovedTemplates(LSuserMaster objuser) {
		Map<String, Object> mapOrders = new HashMap<String, Object>();
		mapOrders.put("sheets", getApprovedTemplatesForResult(0, objuser));
		return mapOrders;
	}
	
	public List<LSfile> getApprovedTemplatesForResult(Integer approvelstatus, LSuserMaster objuser) {
//		if (objuser.getUsername().equals("Administrator")) {
//			return lSfileRepository.getsheetGreaterthanoneandapprovel(objuser.getLssitemaster().getSitecode());
//		} else {
			return GetApprovedTemplatesbyuser(approvelstatus, objuser);
//		}
	}
	
	public List<LSfile> GetApprovedTemplatesbyuser(Integer approvelstatus, LSuserMaster objuser) {
		List<LSfile> lstfile = new ArrayList<LSfile>();

		if (lsuserteammappingRepository.getTeamcodeByLsuserMaster(objuser.getUsercode()).size() > 0) {
			
			List<LSuserMaster> lstteamuser = lsuserteammappingRepository.getLsuserMasterByTeamcode(lsuserteammappingRepository.getTeamcodeByLsuserMaster(objuser.getUsercode()));
			lstteamuser.add(objuser);
			lstfile = lSfileRepository.gettemplateapprovelanduserIn(lstteamuser, objuser.getLssitemaster().getSitecode());

			lstteamuser = null;
		} else {
			List<LSuserMaster> lstteamuser = new ArrayList<LSuserMaster>();
			lstteamuser.add(objuser);
			lstfile = lSfileRepository.gettemplateapprovelanduserIn(lstteamuser, objuser.getLssitemaster().getSitecode());

			lstteamuser = null;
		}
		return lstfile;
	}

	public void updateResultForTemplate(LSfile lsfile) {
		LSfile objLSfile = lSfileRepository.findByfilecode(lsfile.getFilecode());
		objLSfile.setResultsheet(1);
		lSfileRepository.save(objLSfile);
	}

	public Map<String, Object> getApprovedTemplatesWithTask(LSuserMaster objuser) {
		Map<String, Object> mapOrders = new HashMap<String, Object>();
		List<Testmaster> lstTestmasters = masterService.getTestmaster(objuser);
		mapOrders.put("task", lstTestmasters);
		
		if(!lstTestmasters.isEmpty()) {
			
			List<LSfile> lstFile = new ArrayList<LSfile>();
			
			if (objuser.getUsername().equals("Administrator")) {
				lstFile = lSfileRepository.getsheetGreaterthanoneandapprovel(objuser.getLssitemaster().getSitecode());
			} else {
				lstFile = GetApprovedSheetsbyuser(1, objuser);
			}
			
			List<LSfiletest> lstMappedTask = lSfiletestRepository.findByTestcodeAndTesttype(lstTestmasters.get(0).getTestcode(), 1);
			
			if(!lstMappedTask.isEmpty()) {
				// Extract filecodes from lsfiletestList where testtype is 1
		        Set<Integer> filecodesWithTestType1 = lstMappedTask.stream()
		                .filter(lsfiletest -> lsfiletest.getTesttype() == 1)
		                .map(LSfiletest::getFilecode)
		                .collect(Collectors.toSet());

		        // Filter lsfileList based on the extracted filecodes
		        List<LSfile> filteredLsfileList = lstFile.stream()
		                .filter(lsfile -> filecodesWithTestType1.contains(lsfile.getFilecode()))
		                .collect(Collectors.toList());
		        
		        mapOrders.put("template", filteredLsfileList);
			}else {
				mapOrders.put("template",new ArrayList<Testmaster>());
			}
			
		}else {
			mapOrders.put("template",new ArrayList<Testmaster>());
		}
		
		return mapOrders;
	}

	public Map<String, Object> getApprovedTemplatesByTask(LSuserMaster objuser) {
		Map<String, Object> mapOrders = new HashMap<String, Object>();

		List<LSfile> lstFile = GetApprovedTemplatesbyuser(1, objuser);
		
		List<LSfiletest> lstMappedTask = lSfiletestRepository.findByTestcodeAndTesttype(objuser.getTestcode(), 1);

		if (!lstMappedTask.isEmpty()) {
			Set<Integer> filecodesWithTestType1 = lstMappedTask.stream()
					.filter(lsfiletest -> lsfiletest.getTesttype() == 1).map(LSfiletest::getFilecode)
					.collect(Collectors.toSet());

			List<LSfile> filteredLsfileList = lstFile.stream()
					.filter(lsfile -> filecodesWithTestType1.contains(lsfile.getFilecode()))
					.collect(Collectors.toList());

			mapOrders.put("template", filteredLsfileList);
		} else {
			mapOrders.put("template", new ArrayList<Testmaster>());
		}

		return mapOrders;
	}

	public void updateResultKeys(LSfile objfile) {
		String resultvalues = objfile.getResultvalues();
		if (resultvalues != null && resultvalues != "" && !resultvalues.equalsIgnoreCase("[]")) {
			updateTemplateResultvalues(objfile, resultvalues);
		}
	}	
	
	private void updateTemplateResultvalues(LSfile objfile, String tagvalues) {
		Lsresultfortemplate objreport = new Lsresultfortemplate();
		objreport.setId((long) objfile.getFilecode());
		objreport.setContent(tagvalues);
		objreport.setContentstored(1);
		LSresultfortemplateRepository.save(objreport);
		objreport = null;
	}

	public void updateTagKeys(LSfile objfile) {
		String resultvalues = objfile.getResultvalues();
		if (resultvalues != null && resultvalues != "" && !resultvalues.equalsIgnoreCase("[]")) {
			updateTemplateTagvalues(objfile, resultvalues);
		}
	}
	
	private void updateTemplateTagvalues(LSfile objfile, String tagvalues) {
		Lstagfortemplate objreport = new Lstagfortemplate();
		objreport.setId((long) objfile.getFilecode());
		objreport.setContent(tagvalues);
		objreport.setContentstored(1);
		LstagfortemplateRepository.save(objreport);
		objreport = null;
	}
	
	public Map<String, Object> onGetResultTagFromTemplate(LSfile objOrder) {

		Map<String, Object> mapRtnObj = new HashMap<String, Object>();
		mapRtnObj.put("result", LSresultfortemplateRepository.findById(objOrder.getFilecode()));
		mapRtnObj.put("tag", LstagfortemplateRepository.findById(objOrder.getFilecode()));
		return mapRtnObj;
	}

	public void updateTagForTemplate(LSfile lsfile) {
		LSfile objLSfile = lSfileRepository.findByfilecode(lsfile.getFilecode());
		objLSfile.setTagsheet(1);
		lSfileRepository.save(objLSfile);
	}

	
}