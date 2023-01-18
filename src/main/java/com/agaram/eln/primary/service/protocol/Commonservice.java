package com.agaram.eln.primary.service.protocol;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.agaram.eln.primary.model.cloudFileManip.CloudSheetCreation;
import com.agaram.eln.primary.model.sheetManipulation.LSfile;
import com.agaram.eln.primary.model.sheetManipulation.LSsheetworkflow;
import com.agaram.eln.primary.model.usermanagement.LSnotification;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.model.usermanagement.LSusersteam;
import com.agaram.eln.primary.model.usermanagement.LSuserteammapping;
import com.agaram.eln.primary.repository.cloudFileManip.CloudSheetCreationRepository;
//import com.agaram.eln.primary.repository.usermanagement.LSMultiusergroupRepositery;
import com.agaram.eln.primary.repository.usermanagement.LSnotificationRepository;
import com.agaram.eln.primary.repository.usermanagement.LSusersteamRepository;
import com.agaram.eln.primary.repository.usermanagement.LSuserteammappingRepository;
import com.mongodb.gridfs.GridFSDBFile;

@Service
public class Commonservice {

//	@Autowired
//	private LSMultiusergroupRepositery lsMultiusergroupRepositery;
	
	
	@Autowired
	private CloudSheetCreationRepository cloudSheetCreationRepository;
	
	@Autowired
	private GridFsTemplate gridFsTemplate;
	
	@Autowired
	private LSusersteamRepository LSusersteamRepository;
	
	@Autowired
	private LSuserteammappingRepository lsuserteammappingRepository;
	
	@Autowired
	LSnotificationRepository lsnotificationRepository;
	
	
	@Async
	public CompletableFuture<List<LSfile>> updatefilecontentcheck(String Content, LSfile objfile, Boolean Isnew) {
		// Document Doc = Document.parse(objfile.getFilecontent());
				
		if (objfile.getIsmultitenant() == 1) {
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
		List<LSfile> obj =new ArrayList<>();
		obj.add(objfile);
		return CompletableFuture.completedFuture(obj);

	}

	@Async
	public CompletableFuture<List<LSfile>> updatenotificationforsheetthread(LSfile objFile, Boolean isNew, LSsheetworkflow previousworkflow,
			Boolean IsNewsheet) {
		List<LSnotification> lstnotifications = new ArrayList<LSnotification>();
		List<LSuserteammapping> objteam = lsuserteammappingRepository
				.findByTeamcodeNotNullAndLsuserMaster(objFile.getLSuserMaster());

		if (objteam != null && objteam.size() > 0) {
			String Details = "";
			String Notifiction = "";

			if (!isNew) {

				if (objFile.getApproved() == 0) {
					Notifiction = "SHEETMOVED";
				}
				if (objFile.getApproved() == 2 && objFile.getRejected() != 1) {
					Notifiction = "SHEETRETURNED";
				} else if (objFile.getApproved() == 1) {
					Notifiction = "SHEETAPPROVED";
				} else if (objFile.getRejected() == 1) {
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

					List<LSuserteammapping> lstusers = objteam1.getLsuserteammapping();

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
								objnotify.setNotificationdate(objFile.getModifieddate());
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

					List<LSuserteammapping> lstusers = objteam1.getLsuserteammapping();

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

			lsnotificationRepository.save(lstnotifications);

			Details = null;
			Notifiction = null;
		}

		objteam = null;
		lstnotifications = null;
		List<LSfile> obj =new ArrayList<>();
		obj.add(objFile);
		return CompletableFuture.completedFuture(obj);
	}

}
