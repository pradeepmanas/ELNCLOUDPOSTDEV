package com.agaram.eln.primary.service.sheetManipulation;

import java.util.ArrayList;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.stereotype.Service;

import com.agaram.eln.primary.fetchmodel.gettemplate.Sheettemplateget;
import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.cloudFileManip.CloudSheetCreation;
import com.agaram.eln.primary.model.cloudFileManip.CloudSheetVersion;
import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.general.SheetCreation;
import com.agaram.eln.primary.model.general.SheetVersion;
import com.agaram.eln.primary.model.instrumentDetails.LSlogilablimsorderdetail;
import com.agaram.eln.primary.model.masters.Lsrepositories;
import com.agaram.eln.primary.model.sheetManipulation.LSfile;
import com.agaram.eln.primary.model.sheetManipulation.LSfileparameter;
import com.agaram.eln.primary.model.sheetManipulation.LSfiletest;
import com.agaram.eln.primary.model.sheetManipulation.LSfileversion;
import com.agaram.eln.primary.model.sheetManipulation.LSsheetupdates;
import com.agaram.eln.primary.model.sheetManipulation.LSsheetworkflow;
import com.agaram.eln.primary.model.sheetManipulation.LSworkflow;
import com.agaram.eln.primary.model.sheetManipulation.Lsfilesharedby;
import com.agaram.eln.primary.model.sheetManipulation.Lsfileshareto;
import com.agaram.eln.primary.model.sheetManipulation.Lssheetworkflowhistory;
import com.agaram.eln.primary.model.sheetManipulation.Notification;
import com.agaram.eln.primary.model.usermanagement.LSnotification;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.model.usermanagement.LSusersteam;
import com.agaram.eln.primary.model.usermanagement.LSuserteammapping;
import com.agaram.eln.primary.repository.cfr.LSactivityRepository;
import com.agaram.eln.primary.repository.cfr.LScfttransactionRepository;
import com.agaram.eln.primary.repository.cloudFileManip.CloudSheetCreationRepository;
import com.agaram.eln.primary.repository.cloudFileManip.CloudSheetVersionRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LSlogilablimsorderdetailRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LsorderworkflowhistoryRepositroy;
import com.agaram.eln.primary.repository.sheetManipulation.LSfileRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LSfilemethodRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LSfileparameterRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LSfiletestRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LSfileversionRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LSsheetupdatesRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LSsheetworkflowRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LSsheetworkflowgroupmapRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LSworkflowRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LSworkflowgroupmappingRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LsfilesharedbyRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LsfilesharetoRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LssheetworkflowhistoryRepository;
import com.agaram.eln.primary.repository.sheetManipulation.NotificationRepository;
import com.agaram.eln.primary.repository.usermanagement.LSnotificationRepository;
import com.agaram.eln.primary.repository.usermanagement.LSusersteamRepository;
import com.agaram.eln.primary.repository.usermanagement.LSuserteammappingRepository;
import com.agaram.eln.primary.service.basemaster.BaseMasterService;
import com.agaram.eln.primary.service.masters.MasterService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@EnableJpaRepositories(basePackageClasses = LSfileRepository.class)
public class FileService {

	static final Logger logger = Logger.getLogger(FileService.class.getName());

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
	private LSnotificationRepository LSnotificationRepository;

	@Autowired
	private LSworkflowgroupmappingRepository lsworkflowgroupmappingRepository;

	@Autowired
	private LSactivityRepository lsactivityRepository;

	@Autowired
	private LScfttransactionRepository lscfttransactionRepository;

	@Autowired
	private BaseMasterService masterService;

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
	private LsorderworkflowhistoryRepositroy lsorderworkflowhistoryRepositroy;

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

	public LSfile InsertupdateSheet(LSfile objfile) {
		Boolean Isnew = false;
		String Content = objfile.getFilecontent();
		if (objfile.getFilecode() == null
				&& lSfileRepository.findByfilenameuserIgnoreCase(objfile.getFilenameuser()) != null) {

			objfile.setResponse(new Response());
			objfile.getResponse().setStatus(false);
			objfile.getResponse().setInformation("ID_SHEET");

			return objfile;

		}

		if (objfile.getLssheetworkflow() == null) {
			objfile.setLssheetworkflow(lssheetworkflowRepository
					.findTopByAndLssitemasterOrderByWorkflowcodeAsc(objfile.getLssitemaster()));
		}

		if (objfile.getFilecode() != null && objfile.getFilecode() > 0) {
			UpdateSheetversion(objfile);

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
		} else {
			UpdateSheetversion(objfile);
			Isnew = true;
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

			lsactivityRepository.save(objfile.getObjActivity());
		}
		if (objfile.getModifiedlist() != null) {

			lssheetupdatesRepository.save(objfile.getModifiedlist());
		}

		objfile.setFilecontent(null);
		lSfileRepository.save(objfile);

		updatefilecontent(Content, objfile, Isnew);

		if (objfile.getObjsilentaudit() != null) {

			objfile.getObjsilentaudit().setTableName("LSfile");
		}

		if (objfile.getObjuser() != null) {
			objfile.getObjmanualaudit().setComments(objfile.getObjuser().getComments());
			objfile.getObjmanualaudit().setTableName("LSfile");
		}

		objfile.setResponse(new Response());
		objfile.getResponse().setStatus(true);
		objfile.getResponse().setInformation("ID_SHEETMSG");

		updatenotificationforsheet(objfile, true, null);

		return objfile;
	}

	public void updatefilecontent(String Content, LSfile objfile, Boolean Isnew) {
		// Document Doc = Document.parse(objfile.getFilecontent());
		if (objfile.getIsmultitenant() == 1) {
			CloudSheetCreation objsavefile = new CloudSheetCreation();
			objsavefile.setId((long) objfile.getFilecode());
			objsavefile.setContent(Content);
			cloudSheetCreationRepository.save(objsavefile);
		} else {
			SheetCreation objsavefile = new SheetCreation();
			objsavefile.setId((long) objfile.getFilecode());
			objsavefile.setContent(Content);

			// build query
			Query query = new Query(Criteria.where("id").is(objsavefile.getId()));

			Boolean recordcount = mongoTemplate.exists(query, SheetCreation.class);

			if (Isnew || !recordcount) {
				mongoTemplate.insert(objsavefile);
			} else {
				Update update = new Update();
				update.set("content", Content);

				mongoTemplate.upsert(query, update, SheetCreation.class);
			}
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
		List<LSfile> lstfile = new ArrayList<LSfile>();
		List<LSuserMaster> lstteamuser = objuser.getObjuser().getTeamusers();

		if (lstteamuser != null && lstteamuser.size() > 0) {
			lstteamuser.add(objuser);
			lstfile = lSfileRepository.getsheetGreaterthanOneAndCreatedByUserIN(lstteamuser);
		} else {
			lstteamuser.add(objuser);
			lstfile = lSfileRepository.getsheetGreaterthanOneAndCreatedByUserIN(lstteamuser);
		}
		return lstfile;
	}

	public List<Sheettemplateget> GetSheetsbyuseronDetailview(LSuserMaster objuser) {
		List<Sheettemplateget> lstfile = new ArrayList<Sheettemplateget>();
		List<LSuserMaster> lstteamuser = objuser.getObjuser().getTeamusers();

		if (lstteamuser != null && lstteamuser.size() > 0) {
			lstteamuser.add(objuser);
			lstfile = lSfileRepository.findByFilecodeGreaterThanAndCreatebyInOrderByFilecodeDesc(1, lstteamuser);
		} else {

			lstfile = lSfileRepository.findByFilecodeGreaterThanAndCreatebyInOrderByFilecodeDesc(1, objuser);
		}
		if (objuser.getObjsilentaudit() != null) {
			objuser.getObjsilentaudit().setTableName("LSfile");

		}

		lstfile.forEach(
				objFile -> objFile.setVersioncout(lsfileversionRepository.countByFilecode(objFile.getFilecode())));

		return lstfile;
	}

	public Map<String, Object> getSheetscount(LSuserMaster objusers) {

		Map<String, Object> mapObj = new HashMap<String, Object>();

		List<LSuserMaster> lstteamuser = objusers.getObjuser().getTeamusers();

		mapObj.put("templatecount", lSfileRepository.countByCreatebyIn(lstteamuser));
		mapObj.put("sharedbyme",
				LsfilesharedbyRepository.countBySharebyunifiedidAndSharestatus(objusers.getSharebyunifiedid(), 1));
		mapObj.put("sharedtome",
				LsfilesharetoRepository.countBySharetounifiedidAndSharestatus(objusers.getSharetounifiedid(), 1));

		return mapObj;
	}

	public List<LSfile> GetApprovedSheets(Integer approvelstatus, LSuserMaster objuser) {
		if (objuser.getUsername().equals("Administrator")) {
			return lSfileRepository.getsheetGreaterthanoneandapprovel(approvelstatus);
		} else {
			return GetApprovedSheetsbyuser(approvelstatus, objuser);
		}
	}

	public List<LSfile> GetApprovedSheetsbyuser(Integer approvelstatus, LSuserMaster objuser) {
		List<LSfile> lstfile = new ArrayList<LSfile>();
		List<Integer> lstteammap = lsuserteammappingRepository.getTeamcodeByLsuserMaster(objuser.getUsercode());

		if (lstteammap.size() > 0) {
			List<LSuserMaster> lstteamuser = lsuserteammappingRepository.getLsuserMasterByTeamcode(lstteammap);
			lstteamuser.add(objuser);
			lstfile = lSfileRepository.getsheetGreaterthanoneandapprovelanduserIn(approvelstatus, lstteamuser);
		} else {
			List<LSuserMaster> lstteamuser = new ArrayList<LSuserMaster>();
			lstteamuser.add(objuser);
			lstfile = lSfileRepository.getsheetGreaterthanoneandapprovelanduserIn(approvelstatus, lstteamuser);
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
			lsfiles = lSfileRepository.findBylstestIn(lsfiletest);
		} else {
			List<Integer> lstteammap = lsuserteammappingRepository
					.getTeamcodeByLsuserMaster(objtest.getObjLoggeduser().getUsercode());

			if (lstteammap.size() > 0) {
				List<LSuserMaster> lstteamuser = lsuserteammappingRepository.getLsuserMasterByTeamcode(lstteammap);
				lstteamuser.add(objtest.getObjLoggeduser());
				lsfiles = lSfileRepository.findByCreatebyInAndLstestInAndFilecodeGreaterThan(lstteamuser, lsfiletest,
						1);
			} else {
				List<LSuserMaster> lstteamuser = new ArrayList<LSuserMaster>();
				lstteamuser.add(objtest.getObjLoggeduser());
				lsfiles = lSfileRepository.findByCreatebyInAndLstestInAndFilecodeGreaterThan(lstteamuser, lsfiletest,
						1);
			}
		}

		if (lsfiles != null && lsfiles.size() == 1) {

			if (objtest.getIsmultitenant() == 1) {
				CloudSheetCreation file = cloudSheetCreationRepository.findById((long) lsfiles.get(0).getFilecode());
				if (file != null) {
					lsfiles.get(0).setFilecontent(file.getContent());
				}
			} else {
				SheetCreation file = mongoTemplate.findById(lsfiles.get(0).getFilecode(), SheetCreation.class);
				if (file != null) {
					lsfiles.get(0).setFilecontent(file.getContent());
				}
			}
		}

		return lsfiles;
	}

	public List<LSworkflow> InsertUpdateWorkflow(LSworkflow[] workflow) {
		List<LSworkflow> lstworkflow1 = Arrays.asList(workflow);
		for (LSworkflow flow : lstworkflow1) {
			lsworkflowgroupmappingRepository.save(flow.getLsworkflowgroupmapping());
			lsworkflowRepository.save(flow);
		}
		// silent audit
//		if (lstworkflow.get(0).getObjsilentaudit() != null) {
//			lstworkflow.get(0).getObjsilentaudit().setTableName("lsworkflow");
//			lscfttransactionRepository.save(lstworkflow.get(0).getObjsilentaudit());
//		}
//		// Manual Audit
//		if (lstworkflow.get(0).getObjuser() != null) {
////			Date date = new Date();
//
//			lstworkflow.get(0).getObjmanualaudit().setComments(lstworkflow.get(0).getObjuser().getComments());
//			lstworkflow.get(0).getObjmanualaudit().setTableName("LSworkflow");
//			lstworkflow.get(0).getObjmanualaudit().setLsuserMaster(lstworkflow.get(0).getLSuserMaster().getUsercode());
//			lstworkflow.get(0).getObjmanualaudit()
//					.setLssitemaster(lstworkflow.get(0).getLSuserMaster().getLssitemaster().getSitecode());
////			lstworkflow.get(0).getObjmanualaudit().setTransactiondate(date);
//			lscfttransactionRepository.save(lstworkflow.get(0).getObjmanualaudit());
//		}
		lstworkflow1.get(0).setResponse(new Response());
		lstworkflow1.get(0).getResponse().setStatus(true);
		lstworkflow1.get(0).getResponse().setInformation("ID_SHEETMSG");

		return lstworkflow1;
	}

	public List<LSworkflow> GetWorkflow(LSworkflow objflow) {

		if (objflow.getObjsilentaudit() != null) {
			objflow.getObjsilentaudit().setTableName("LSuserMaster");
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

			if (objflow.getObjsilentaudit() != null) {
				objflow.getObjsilentaudit().setTableName("LSworkflow");
//				lscfttransactionRepository.save(objflow.getObjsilentaudit());
			}
		}

		return response;
	}

	public Map<String, Object> GetMastersfororders(LSuserMaster objuser) {
		Map<String, Object> mapOrders = new HashMap<String, Object>();

		mapOrders.put("test", masterService.getTestmaster(objuser));
		mapOrders.put("sample", masterService.getsamplemaster(objuser));
		mapOrders.put("project", masterService.getProjectmaster(objuser));
		Lsrepositories lsrepositories = new Lsrepositories();
		lsrepositories.setSitecode(objuser.getLssitemaster().getSitecode());
		mapOrders.put("inventories", inventoryservice.Getallrepositories(lsrepositories));
		mapOrders.put("sheets", GetApprovedSheets(0, objuser));
		if (objuser.getObjsilentaudit() != null) {
			objuser.getObjsilentaudit().setTableName("LSfiletest");
			lscfttransactionRepository.save(objuser.getObjsilentaudit());
		}
		return mapOrders;
	}

	public Map<String, Object> GetMastersforsheetsetting(LSuserMaster objuser) {
		Map<String, Object> mapOrders = new HashMap<String, Object>();

		mapOrders.put("limstest", masterService.getLimsTestMaster(objuser));
		mapOrders.put("test", masterService.getTestmaster(objuser));
		mapOrders.put("sheets", GetApprovedSheets(1, objuser));

//		silent audit
		if (objuser.getObjsilentaudit() != null) {
			objuser.getObjsilentaudit().setTableName("LSfiletest");
			lscfttransactionRepository.save(objuser.getObjsilentaudit());
		}
		return mapOrders;
	}

	public List<LSsheetworkflow> InsertUpdatesheetWorkflow(LSsheetworkflow[] sheetworkflow) {

		List<LSsheetworkflow> lSsheetworkflow = Arrays.asList(sheetworkflow);
		for (LSsheetworkflow flow : lSsheetworkflow) {
			lssheetworkflowgroupmapRepository.save(flow.getLssheetworkflowgroupmap());
			lssheetworkflowRepository.save(flow);
		}

		if (lSsheetworkflow.get(0).getObjsilentaudit() != null) {
			lSsheetworkflow.get(0).getObjsilentaudit().setTableName("LSfiletest");
//			lscfttransactionRepository.save(lSsheetworkflow.get(0).getObjsilentaudit());
		}

//		if (lSsheetworkflow.get(0).getObjuser() != null) {
//
//
//			lSsheetworkflow.get(0).getObjmanualaudit().setComments(lSsheetworkflow.get(0).getObjuser().getComments());
//			lSsheetworkflow.get(0).getObjmanualaudit().setTableName("LSsheetworkflow");
//			lSsheetworkflow.get(0).getObjmanualaudit()
//					.setLsuserMaster(lSsheetworkflow.get(0).getObjuser().getUsercode());
//			lSsheetworkflow.get(0).getObjmanualaudit()
//					.setLssitemaster(lSsheetworkflow.get(0).getLSuserMaster().getLssitemaster().getSitecode());
////			lscfttransactionRepository.save(lSsheetworkflow.get(0).getObjmanualaudit());
//		}
		lSsheetworkflow.get(0).setResponse(new Response());
		lSsheetworkflow.get(0).getResponse().setStatus(true);
		lSsheetworkflow.get(0).getResponse().setInformation("ID_SHEETMSG");
		return lSsheetworkflow;
	}

	public List<LSsheetworkflow> GetsheetWorkflow(LSsheetworkflow objuser) {

		if (objuser.getObjsilentaudit() != null) {
			objuser.getObjsilentaudit().setTableName("LSuserMaster");
			lscfttransactionRepository.save(objuser.getObjsilentaudit());
		}

		return lssheetworkflowRepository.findBylssitemaster(objuser.getLssitemaster());
	}

	public Response Deletesheetworkflow(LSsheetworkflow objflow) {
		Response response = new Response();

		long onprocess = lSfileRepository.countByLssheetworkflowAndApproved(objflow, 0);
		if (onprocess > 0) {
			response.setStatus(false);
		} else {
			lSfileRepository.setWorkflownullforApprovedfile(objflow, 1);
			lssheetworkflowhistoryRepository.setWorkflownullforHistory(objflow);
			lsfileversionRepository.setWorkflownullforHistory(objflow);
			lssheetworkflowRepository.delete(objflow);
			lssheetworkflowgroupmapRepository.delete(objflow.getLssheetworkflowgroupmap());
			response.setStatus(true);
			if (objflow.getObjsilentaudit() != null) {
				objflow.getObjsilentaudit().setTableName("LSsheetworkflow");
				lscfttransactionRepository.save(objflow.getObjsilentaudit());
			}
		}
		return response;
	}

	public LSfile updateworkflowforFile(LSfile objfile) {

		LSfile objcurrentfile = lSfileRepository.findByfilecode(objfile.getFilecode());

		lssheetworkflowhistoryRepository.save(objfile.getLssheetworkflowhistory());
		lSfileRepository.updateFileWorkflow(objfile.getLssheetworkflow(), objfile.getApproved(), objfile.getRejected(),
				objfile.getFilecode());

//		silent audit
		if (objfile.getLssheetworkflowhistory().get(objfile.getLssheetworkflowhistory().size() - 1)
				.getObjsilentaudit() != null) {
			objfile.getLssheetworkflowhistory().get(objfile.getLssheetworkflowhistory().size() - 1).getObjsilentaudit()
					.setTableName("LSfile");
			lscfttransactionRepository.save(objfile.getLssheetworkflowhistory()
					.get(objfile.getLssheetworkflowhistory().size() - 1).getObjsilentaudit());
		}

		updatenotificationforsheet(objfile, false, objcurrentfile.getLssheetworkflow());

		return objfile;
	}

	public void updatenotificationforsheet(LSfile objFile, Boolean isNew, LSsheetworkflow previousworkflow) {
		try {
			List<LSuserteammapping> objteam = lsuserteammappingRepository
					.findByTeamcodeNotNullAndLsuserMaster(objFile.getLSuserMaster());

			logger.info("objteam : " + objteam);
			if (objteam != null && objteam.size() > 0) {
				String Details = "";
				String Notifiction = "";
				List<LSnotification> lstnotifications = new ArrayList<LSnotification>();

				if (!isNew) {

					if (objFile.getApproved() == 0) {
						Notifiction = "SHEETMOVED";
					}
					if (objFile.getApproved() == 2) {
						Notifiction = "SHEETRETURNED";
					} else if (objFile.getApproved() == 1) {
						Notifiction = "SHEETAPPROVED";
					} else if (objFile.getRejected() == 1) {
						Notifiction = "SHEETREJECTED";
					}

					int perviousworkflowcode = previousworkflow != null ? previousworkflow.getWorkflowcode() : -1;
					String previousworkflowname = previousworkflow != null ? previousworkflow.getWorkflowname() : "";

					Details = "{\"ordercode\":\"" + objFile.getFilecode() + "\", \"order\":\""
							+ objFile.getFilenameuser() + "\", \"previousworkflow\":\"" + previousworkflowname
							+ "\", \"previousworkflowcode\":\"" + perviousworkflowcode + "\", \"currentworkflow\":\""
							+ objFile.getLssheetworkflow().getWorkflowname() + "\", \"currentworkflowcode\":\""
							+ objFile.getLssheetworkflow().getWorkflowcode() + "\"}";

					for (int i = 0; i < objteam.size(); i++) {
						LSusersteam objteam1 = LSusersteamRepository.findByteamcode(objteam.get(i).getTeamcode());

						List<LSuserteammapping> lstusers = objteam1.getLsuserteammapping();

						for (int j = 0; j < lstusers.size(); j++) {

							if (objFile.getObjLoggeduser().getUsercode() != lstusers.get(j).getLsuserMaster()
									.getUsercode()) {
								LSnotification objnotify = new LSnotification();
								objnotify.setNotifationfrom(objFile.getLSuserMaster());
								objnotify.setNotifationto(lstusers.get(j).getLsuserMaster());
								objnotify.setNotificationdate(objFile.getCreatedate());
								objnotify.setNotification(Notifiction);
								objnotify.setNotificationdetils(Details);
								objnotify.setIsnewnotification(1);
								objnotify.setNotificationpath("/sheetcreation");

								lstnotifications.add(objnotify);
							}
						}
					}
				} else {
					Notifiction = "SHEETCREATED";
					Details = "{\"ordercode\":\"" + objFile.getFilecode() + "\", \"order\":\""
							+ objFile.getFilenameuser() + "\", \"previousworkflow\":\"" + ""
							+ "\", \"previousworkflowcode\":\"" + -1 + "\", \"currentworkflow\":\""
							+ objFile.getLssheetworkflow().getWorkflowname() + "\", \"currentworkflowcode\":\""
							+ objFile.getLssheetworkflow().getWorkflowcode() + "\"}";

					for (int i = 0; i < objteam.size(); i++) {
						LSusersteam objteam1 = LSusersteamRepository.findByteamcode(objteam.get(i).getTeamcode());

						List<LSuserteammapping> lstusers = objteam1.getLsuserteammapping();

						for (int j = 0; j < lstusers.size(); j++) {

							if (objFile.getLSuserMaster().getUsercode() != lstusers.get(j).getLsuserMaster()
									.getUsercode()) {
								LSnotification objnotify = new LSnotification();
								objnotify.setNotifationfrom(objFile.getLSuserMaster());
								objnotify.setNotifationto(lstusers.get(j).getLsuserMaster());
								objnotify.setNotificationdate(objFile.getCreatedate());
								objnotify.setNotification(Notifiction);
								objnotify.setNotificationdetils(Details);
								objnotify.setIsnewnotification(1);
								objnotify.setNotificationpath("/sheetcreation");

								lstnotifications.add(objnotify);
							}
						}
					}
				}

				LSnotificationRepository.save(lstnotifications);
			}
		} catch (Exception e) {
			logger.error("updatenotificationforsheet : " + e.getMessage());
		}
	}

	public Map<String, Object> lockorder(Map<String, Object> objMap) throws Exception {

		Long BatchID = null;
		int usercode = 0;
		@SuppressWarnings("unused")
		LScfttransaction cfttransaction;
		ObjectMapper objMapper = new ObjectMapper();

		if (objMap.containsKey("Batch")) {
			BatchID = Long.valueOf((Integer) objMap.get("Batch"));
		}
		if (objMap.containsKey("usercode")) {
			usercode = (Integer) objMap.get("usercode");
		}

		if (objMap.containsKey("objsilentaudit")) {

			cfttransaction = objMapper.convertValue(objMap.get("objsilentaudit"), LScfttransaction.class);
//			lscfttransactionRepository.save(cfttransaction);

		}
		LSlogilablimsorderdetail orderDetail = LSlogilablimsorderdetailRepository.findOne(BatchID);
		Integer sLockeduser = 0;
		if (orderDetail != null) {
			sLockeduser = orderDetail.getLockeduser();

			if (sLockeduser == null || sLockeduser == 0) {

				orderDetail.setLockeduser(usercode);

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

		return objMap;
	}

	@SuppressWarnings("null")
	public Map<String, Object> unlockorder(Map<String, Object> objMap) throws Exception {
		Long BatchID = null;
		LScfttransaction cfttransaction;
		ObjectMapper objMapper = new ObjectMapper();

		if (objMap.containsKey("Batch")) {
			BatchID = Long.valueOf((Integer) objMap.get("Batch"));
		}
		if (objMap.containsKey("objsilentaudit")) {

			cfttransaction = objMapper.convertValue(objMap.get("objsilentaudit"), LScfttransaction.class);
			lscfttransactionRepository.save(cfttransaction);

		}
		LSlogilablimsorderdetail orderDetail = LSlogilablimsorderdetailRepository.findOne(BatchID);

		if (orderDetail != null) {

			orderDetail.setLockeduser(null);

			LSlogilablimsorderdetailRepository.save(orderDetail);

			orderDetail.setResponse(new Response());
			orderDetail.getResponse().setStatus(true);
			orderDetail.getResponse().setInformation("ID_UNLOCKMSG");

			objMap.put("response", orderDetail);
		} else {
			orderDetail.setResponse(new Response());
			orderDetail.getResponse().setStatus(false);
			orderDetail.getResponse().setInformation("ID_UNLOCKFAIL");
		}

		return objMap;
	}

	public boolean UpdateSheetversion(LSfile objfile) {
		int Versionnumber = 0;
		LSfileversion objLatestversion = lsfileversionRepository
				.findFirstByFilecodeOrderByVersionnoDesc(objfile.getFilecode());
		if (objLatestversion != null) {
			Versionnumber = objLatestversion.getVersionno();
		}

		// Versionnumber++;

		LSfile objesixting = lSfileRepository.findByfilecode(objfile.getFilecode());
		if (objesixting == null) {
			objesixting = objfile;
		}

		if (objesixting.getApproved() == 1 && objfile.getApproved() == 1) {
			Versionnumber++;

			LSsheetworkflow objfirstworkflow = lssheetworkflowRepository
					.findTopByAndLssitemasterOrderByWorkflowcodeAsc(objfile.getLssitemaster());
			objfile.setApproved(0);
			objfile.setLssheetworkflow(objfirstworkflow);

			if (objfile.getLstest().size() > 0) {
				LSfiletest objlims = objfile.getLstest().get(0);
				objlims.setTestcode(null);
				objfile.getLstest().set(0, objlims);
			}

			if (objfile.getLstest().size() > 1) {
				LSfiletest objelntest = objfile.getLstest().get(1);
				objelntest.setTestcode(null);
				objfile.getLstest().set(1, objelntest);
			}

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
			// objversion.setFilecontent(objesixting.getFilecontent());
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
//			if (objfile.getModifiedlist().get(0).getObjsilentaudit() != null) {
//				// objpwd.getObjsilentaudit().setModuleName("UserManagement");
//				objfile.getModifiedlist().get(0).getObjsilentaudit()
//						.setComments("Sheet" + " " + objfile.getFilenameuser() + " " + " was versioned to version_"
//								+ Versionnumber + " " + "by the user" + " " + objfile.getCreateby().getUsername());
//			
//				objfile.getModifiedlist().get(0).getObjsilentaudit().setTableName("LSfile");
//				lscfttransactionRepository.save(objfile.getModifiedlist().get(0).getObjsilentaudit());
//			}

			String Content = "";

			if (objfile.getIsmultitenant() == 1) {
				CloudSheetCreation file = cloudSheetCreationRepository.findById((long) objfile.getFilecode());
				if (file != null) {
					Content = file.getContent();
				}
			} else {
				SheetCreation file = mongoTemplate.findById(objfile.getFilecode(), SheetCreation.class);
				if (file != null) {
					Content = file.getContent();
				}
			}
			
			updatefileversioncontent(Content, objLatestversion, objfile.getIsmultitenant());
			updatefileversioncontent(objfile.getFilecontent(), objversion, objfile.getIsmultitenant());
		}
		else if(Versionnumber == 0)
		{
			Versionnumber++;
			LSfileversion objversion = new LSfileversion();

			objversion.setApproved(objesixting.getApproved());
			objversion.setCreateby(objesixting.getCreateby());
			objversion.setCreatedate(objesixting.getCreatedate());
			objversion.setExtension(objesixting.getExtension());
			if(objesixting.getFilecode() != null)
			{
				objversion.setFilecode(objesixting.getFilecode());
			}
			// objversion.setFilecontent(objesixting.getFilecontent());
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
			
			updatefileversioncontent(objfile.getFilecontent(), objversion, objfile.getIsmultitenant());
		}
		else
		{
			updatefileversioncontent(objfile.getFilecontent(), objLatestversion, objfile.getIsmultitenant());
		}
		
		objfile.setVersionno(Versionnumber);

		return true;
	}

	public void updatefileversioncontent(String Content, LSfileversion objfile, Integer ismultitenant) {
		if (ismultitenant == 1) {
			CloudSheetVersion objsavefile = new CloudSheetVersion();
			objsavefile.setId((long) objfile.getFileversioncode());
			objsavefile.setContent(Content);
			cloudSheetVersionRepository.save(objsavefile);
		} else {
			SheetVersion objsavefile = new SheetVersion();
			objsavefile.setId((long) objfile.getFileversioncode());
			objsavefile.setContent(Content);

			// build query
			Query query = new Query(Criteria.where("id").is(objsavefile.getId()));

			Boolean recordcount = mongoTemplate.exists(query, SheetVersion.class);

			if (!recordcount) {
				mongoTemplate.insert(objsavefile);
			} else {
				Update update = new Update();
				update.set("content", Content);

				mongoTemplate.upsert(query, update, SheetVersion.class);
			}
		}
		// objfile.setFilecontent(Content);
	}

	public List<LSfileversion> Getfileversions(LSfile objfile) {
		return lsfileversionRepository.findByFilecodeOrderByVersionnoDesc(objfile.getFilecode());
	}

	public List<Lssheetworkflowhistory> Getfilehistory(LSfile objfile) {
		return lssheetworkflowhistoryRepository.findByFilecode(objfile.getFilecode());
	}

	public String GetfileverContent(LSfile objfile) {
		String Content = "";

		if (objfile.getVersionno() == 0) {
			LSfile objesixting = lSfileRepository.findByfilecode(objfile.getFilecode());
			Content = objesixting.getFilecontent();
			if (objfile != null) {
				if (objfile.getIsmultitenant() == 1) {
					CloudSheetCreation file = cloudSheetCreationRepository.findById((long) objfile.getFilecode());
					if (file != null) {
						Content = file.getContent();
					}
				} else {
					SheetCreation file = mongoTemplate.findById(objfile.getFilecode(), SheetCreation.class);
					if (file != null) {
						Content = file.getContent();
					}
				}
			}
		} else {
			LSfileversion objVersion = lsfileversionRepository
					.findByFilecodeAndVersionnoOrderByVersionnoDesc(objfile.getFilecode(), objfile.getVersionno());
			Content = objVersion.getFilecontent();
			if (objVersion != null) {
				if (objfile.getIsmultitenant() == 1) {
					CloudSheetVersion file = cloudSheetVersionRepository
							.findById((long) objVersion.getFileversioncode());
					if (file != null) {
						Content = file.getContent();
					}
				} else {
					SheetVersion file = mongoTemplate.findById(objVersion.getFileversioncode(), SheetVersion.class);
					if (file != null) {
						Content = file.getContent();
					}
				}
			}
		}

		return Content;
	}

	public List<LSlogilablimsorderdetail> getSheetOrder(LSlogilablimsorderdetail objClass) {
		return LSlogilablimsorderdetailRepository.findAll();
	}

	public LSfile getfileoncode(LSfile objfile) {
		if (objfile.getObjsilentaudit() != null) {
			objfile.getObjsilentaudit().setTableName("LSfile");
			lscfttransactionRepository.save(objfile.getObjsilentaudit());
		}

		LSfile objreturnfile = lSfileRepository.findByfilecode(objfile.getFilecode());
		List<LSsheetupdates> list = lssheetupdatesRepository.findByfilecode(objfile.getFilecode());
		objreturnfile.setModifiedlist(new ArrayList<LSsheetupdates>());
		objreturnfile.getModifiedlist().addAll(list);
		if (objreturnfile != null) {
			if (objfile.getIsmultitenant() == 1) {
				CloudSheetCreation file = cloudSheetCreationRepository.findById((long) objfile.getFilecode());
				if (file != null) {
					objreturnfile.setFilecontent(file.getContent());
				}
			} else {
				SheetCreation file = mongoTemplate.findById(objfile.getFilecode(), SheetCreation.class);
				if (file != null) {
					objreturnfile.setFilecontent(file.getContent());
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
			List<LSuserMaster> lstteamuser = objfile.getLSuserMaster().getObjuser().getTeamusers();

			if (lstteamuser != null && lstteamuser.size() > 0) {
				lstteamuser.add(objfile.getLSuserMaster());
				mapOrders.put("templatecount", lSfileRepository.countByCreatebyIn(lstteamuser));
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
			objprotocolordershareto.setSharetofilecode(existingshare.getSharetofilecode());
		}

		LsfilesharetoRepository.save(objprotocolordershareto);

		return objprotocolordershareto;
	}

	public Map<String, Object> Insertsharefileby(Lsfilesharedby objprotocolordersharedby) {
		Map<String, Object> map = new HashMap<>();

		Lsfilesharedby existingshare = LsfilesharedbyRepository
				.findBySharebyunifiedidAndSharetounifiedidAndSharefilecode(
						objprotocolordersharedby.getSharebyunifiedid(), objprotocolordersharedby.getSharetounifiedid(),
						objprotocolordersharedby.getSharefilecode());

		if (existingshare != null) {
			objprotocolordersharedby.setSharedbytofilecode(existingshare.getSharedbytofilecode());
		}

		LsfilesharedbyRepository.save(objprotocolordersharedby);

		return map;
	}

	public List<Lsfilesharedby> Getfilesharedbyme(Lsfilesharedby lsordersharedby) {
		return LsfilesharedbyRepository.findBySharebyunifiedidAndSharestatusOrderBySharedbytofilecodeDesc(
				lsordersharedby.getSharebyunifiedid(), 1);
	}

	public List<Lsfileshareto> Getfilesharetome(Lsfileshareto lsordershareto) {
		return LsfilesharetoRepository.findBySharetounifiedidAndSharestatusOrderBySharetofilecodeDesc(
				lsordershareto.getSharetounifiedid(), 1);
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

		return true;
	}

	public Boolean updateSharedToFile(Lsfileshareto lsordersharedby) {

		Lsfileshareto lsfile = LsfilesharetoRepository.findBySharetofilecode(lsordersharedby.getSharetofilecode());

		if (lsfile != null) {

			lsfile.setShareitemdetails(lsordersharedby.getShareitemdetails());

			LsfilesharetoRepository.save(lsfile);

		}

		return true;
	}

	public Notification ValidateNotification(Notification objnotification) {
		NotificationRepository.save(objnotification);
		return null;
		
	}
}
