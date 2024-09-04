package com.agaram.eln.primary.service.protocol;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.agaram.eln.primary.config.TenantContext;
import com.agaram.eln.primary.model.cloudFileManip.CloudSheetCreation;
import com.agaram.eln.primary.model.protocols.LSprotocolmaster;
import com.agaram.eln.primary.model.reports.reportdesigner.ReportTemplateVersion;
import com.agaram.eln.primary.model.reports.reportdesigner.Reporttemplate;
import com.agaram.eln.primary.model.reports.reportviewer.Reports;
import com.agaram.eln.primary.model.reports.reportviewer.ReportsVersion;
import com.agaram.eln.primary.model.sheetManipulation.LSfile;
import com.agaram.eln.primary.model.sheetManipulation.LSsheetworkflow;
import com.agaram.eln.primary.model.usermanagement.LSnotification;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.model.usermanagement.LSusersteam;
import com.agaram.eln.primary.model.usermanagement.LSuserteammapping;
import com.agaram.eln.primary.repository.cloudFileManip.CloudSheetCreationRepository;
import com.agaram.eln.primary.repository.protocol.LSProtocolMasterRepository;
import com.agaram.eln.primary.repository.reports.reportdesigner.ReporttemplateRepository;
//import com.agaram.eln.primary.repository.usermanagement.LSMultiusergroupRepositery;
import com.agaram.eln.primary.repository.usermanagement.LSnotificationRepository;
import com.agaram.eln.primary.repository.usermanagement.LSusersteamRepository;
import com.agaram.eln.primary.repository.usermanagement.LSuserteammappingRepository;
import com.agaram.eln.primary.service.cloudFileManip.CloudFileManipulationservice;
import com.mongodb.gridfs.GridFSDBFile;

@Service
public class Commonservice {

//	@Autowired
//	private LSMultiusergroupRepositery lsMultiusergroupRepositery;

	@Autowired
	private CloudSheetCreationRepository cloudSheetCreationRepository;
	@Autowired
	private CloudFileManipulationservice objCloudFileManipulationservice;

	@Autowired
	private GridFsTemplate gridFsTemplate;

	@Autowired
	private LSusersteamRepository LSusersteamRepository;

	@Autowired
	private LSuserteammappingRepository lsuserteammappingRepository;

	@Autowired
	LSnotificationRepository lsnotificationRepository;

	@Autowired
	private LSProtocolMasterRepository lsProtocolMasterRepository;

	@Autowired
	public ReporttemplateRepository reporttemplateRepository;

//	@Async
	public CompletableFuture<List<LSfile>> updatefilecontentcheck(String Content, LSfile objfile, Boolean Isnew)
			throws IOException {

		if (objfile.getIsmultitenant() == 1 || objfile.getIsmultitenant() == 2) {
			String tenant = TenantContext.getCurrentTenant();
			if (objfile.getIsmultitenant() == 2) {
				tenant = "freeusers";
			}
			Map<String, Object> objMap = objCloudFileManipulationservice.storecloudSheetsreturnwithpreUUID(Content,
					tenant + "sheetcreation");
			String fileUUID = (String) objMap.get("uuid");
			String fileURI = objMap.get("uri").toString();

			CloudSheetCreation objsavefile = new CloudSheetCreation();
			objsavefile.setId((long) objfile.getFilecode());
			objsavefile.setFileuri(fileURI);
			objsavefile.setFileuid(fileUUID);
			objsavefile.setContainerstored(1);
			cloudSheetCreationRepository.save(objsavefile);

			objsavefile = null;
		} else {

			GridFSDBFile largefile = gridFsTemplate
					.findOne(new Query(Criteria.where("filename").is("file_" + objfile.getFilecode())));
			if (largefile != null) {
				gridFsTemplate.delete(new Query(Criteria.where("filename").is("file_" + objfile.getFilecode())));
			}
			gridFsTemplate.store(new ByteArrayInputStream(Content.getBytes(StandardCharsets.UTF_8)),
					"file_" + objfile.getFilecode(), StandardCharsets.UTF_16);
		}
		List<LSfile> obj = new ArrayList<>();
		obj.add(objfile);
		return CompletableFuture.completedFuture(obj);

	}

//	@Async
	public Map<String, Object> uploadToAzureBlobStorage(byte[] documentBytes, Reporttemplate objFile,
			String uniqueDocumentName, String containerName, Integer Type, ReportTemplateVersion reportTemplateVersion,
			boolean isnew_Version) throws IOException {
		Map<String, Object> rtnobject = new HashMap<>();
		if (objFile.getIsmultitenant() == 1 || objFile.getIsmultitenant() == 2) {
			String tenant = TenantContext.getCurrentTenant();
			if (objFile.getIsmultitenant() == 2) {
				tenant = "freeusers";
			}
			Map<String, Object> objMap = objCloudFileManipulationservice.storecloudReportfile(documentBytes,
					tenant + containerName, uniqueDocumentName);
			String fileUUID = (String) objMap.get("blobName");
			String fileURI = objMap.get("blobUri").toString();
			if (isnew_Version) {
				reportTemplateVersion.setFileuid(fileUUID);
				reportTemplateVersion.setFileuri(fileURI);
				rtnobject.put("ReportTemplateVersion", reportTemplateVersion);
			} else {
				objFile.setFileuid(fileUUID);
				objFile.setFileuri(fileURI);
				objFile.setContainerstored(1);
				rtnobject.put("Reporttemplate", objFile);
			}

		}
		return rtnobject;

	}

	
	public Reports uploadToAzureBlobStorage(byte[] documentBytes, Reports objFile, String uniqueDocumentName) throws IOException {
		String tenant = TenantContext.getCurrentTenant();

		Map<String, Object> objMap = objCloudFileManipulationservice.storecloudReportfile(documentBytes, tenant + "report", uniqueDocumentName);
		String fileUUID = (String) objMap.get("blobName");
		String fileURI = objMap.get("blobUri").toString();
		objFile.setFileuid(fileUUID);
		objFile.setFileuri(fileURI);
		objFile.setContainerstored(1);
		return objFile;
	}
	
	public ReportsVersion uploadToAzureBlobStorage(byte[] documentBytes, ReportsVersion objVersion, String uniqueDocumentName) throws IOException {
		String tenant = TenantContext.getCurrentTenant();

		Map<String, Object> objMap = objCloudFileManipulationservice.storecloudReportfile(documentBytes, tenant + "reportversion", uniqueDocumentName);
		String fileUUID = (String) objMap.get("blobName");
		String fileURI = objMap.get("blobUri").toString();
		objVersion.setFileuid(fileUUID);
		objVersion.setFileuri(fileURI);
		objVersion.setContainerstored(1);
		return objVersion;
	}
	
	@Async
	public CompletableFuture<List<LSprotocolmaster>> updateProtocolContent(String Content, LSprotocolmaster objfile)
			throws IOException {

		if (objfile.getIsmultitenant() == 1 || objfile.getIsmultitenant() == 2) {
			String tenant = TenantContext.getCurrentTenant();
			if (objfile.getIsmultitenant() == 2) {
				tenant = "freeusers";
			}
			Map<String, Object> objMap = objCloudFileManipulationservice.storecloudSheetsreturnwithpreUUID(Content,
					tenant + "protocol");
			String fileUUID = (String) objMap.get("uuid");
			String fileURI = objMap.get("uri").toString();

			objfile.setFileuri(fileURI);
			objfile.setFileuid(fileUUID);
			objfile.setContainerstored(1);
			lsProtocolMasterRepository.save(objfile);
		}
		List<LSprotocolmaster> obj = new ArrayList<>();
		obj.add(objfile);
		return CompletableFuture.completedFuture(obj);

	}

	@Async
	public CompletableFuture<List<LSfile>> updatenotificationforsheetthread(LSfile objFile, Boolean isNew,
			LSsheetworkflow previousworkflow, Boolean IsNewsheet) {
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
					List<LSuserteammapping> lstusers = lsuserteammappingRepository
							.findByteamcode(objteam1.getTeamcode());
//					List<LSuserteammapping> lstusers = objteam1.getLsuserteammapping();

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
					List<LSuserteammapping> lstusers = lsuserteammappingRepository
							.findByteamcode(objteam1.getTeamcode());
//					List<LSuserteammapping> lstusers = objteam1.getLsuserteammapping();

					for (int j = 0; j < lstusers.size(); j++) {

						if (objFile.getLSuserMaster().getUsercode().intValue() != lstusers.get(j).getLsuserMaster()
								.getUsercode().intValue()) {
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
		List<LSfile> obj = new ArrayList<>();
		obj.add(objFile);
		return CompletableFuture.completedFuture(obj);
	}
}
