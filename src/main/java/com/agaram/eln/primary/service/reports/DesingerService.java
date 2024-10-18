package com.agaram.eln.primary.service.reports;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import com.agaram.eln.primary.commonfunction.commonfunction;
import com.agaram.eln.primary.config.TenantContext;
import com.agaram.eln.primary.fetchmodel.getorders.Logilabprotocolorders;
import com.agaram.eln.primary.model.cloudProtocol.LSprotocolstepInformation;
import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.instrumentDetails.LSlogilablimsorderdetail;
import com.agaram.eln.primary.model.instrumentDetails.LSprotocolfolderfiles;
import com.agaram.eln.primary.model.protocols.LSprotocolmaster;
import com.agaram.eln.primary.model.protocols.LSprotocolmastertest;
import com.agaram.eln.primary.model.protocols.LSprotocolstep;
import com.agaram.eln.primary.model.protocols.LSprotocolstepInfo;
import com.agaram.eln.primary.model.protocols.LSprotocolversion;
import com.agaram.eln.primary.model.reports.reportdesigner.Cloudreporttemplate;
import com.agaram.eln.primary.model.reports.reportdesigner.ReportDesignerStructure;
import com.agaram.eln.primary.model.reports.reportdesigner.ReportTemplateMapping;
import com.agaram.eln.primary.model.reports.reportdesigner.ReportTemplateVersion;
import com.agaram.eln.primary.model.reports.reportdesigner.Reporttemplate;
import com.agaram.eln.primary.model.sheetManipulation.LStestmasterlocal;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.usermanagement.LSprojectmaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.repository.cloudProtocol.LSprotocolstepInformationRepository;
import com.agaram.eln.primary.repository.protocol.LSProtocolMasterRepository;
import com.agaram.eln.primary.repository.protocol.LSProtocolStepRepository;
import com.agaram.eln.primary.repository.protocol.LSprotocolmastertestRepository;
import com.agaram.eln.primary.repository.protocol.LSprotocolversionRepository;
import com.agaram.eln.primary.repository.reports.reportdesigner.CloudreporttemplateRepository;
import com.agaram.eln.primary.repository.reports.reportdesigner.ReportDesignerStructureRepository;
import com.agaram.eln.primary.repository.reports.reportdesigner.ReportTemplateMappingRepository;
import com.agaram.eln.primary.repository.reports.reportdesigner.ReporttemplateRepository;
import com.agaram.eln.primary.repository.usermanagement.LSprojectmasterRepository;
import com.agaram.eln.primary.service.cloudFileManip.CloudFileManipulationservice;
import com.mongodb.gridfs.GridFSDBFile;

@Service
public class DesingerService {

	@Autowired
	private ReporttemplateRepository reporttemplaterepository;

	@Autowired
	private CloudreporttemplateRepository cloudreporttemplaterepository;

	@Autowired
	private ReportDesignerStructureRepository reportDesignerStructureRepository;

	@Autowired
	private LSProtocolMasterRepository LSProtocolMasterRepositoryObj;

	@Autowired
	private LSprotocolmastertestRepository lsprotocolmastertestRepository;

	@Autowired
	private LSprojectmasterRepository lsprojectmasterRepository;

	@Autowired
	private LSProtocolStepRepository LSProtocolStepRepositoryObj;

	@Autowired
	private LSprotocolversionRepository lsprotocolversionRepository;

	@Autowired
	private LSprotocolstepInformationRepository lsprotocolstepInformationRepository;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private LSProtocolMasterRepository lsProtocolMasterRepository;

	@Autowired
	private CloudFileManipulationservice objCloudFileManipulationservice;

	@Autowired
	private ReportTemplateMappingRepository reportTemplateMappingRepository;
	
	
	@Autowired
	private GridFsTemplate gridFsTemplate;

	public Map<String, Object> getreportsource(Map<String, Object> argObj) {
		Map<String, Object> rtnObj = new HashMap<String, Object>();

		Integer key = (Integer) argObj.get("key");

		List<Map<String, Object>> lstorder = new ArrayList<Map<String, Object>>();
		Map<String, Object> order = new HashMap<String, Object>();
		order.put("Order ID", "");
		order.put("Order Code", 1);
		order.put("Order Type", "");
		order.put("Date Created", new Date());
		order.put("Date Completed", new Date());
		order.put("Version Number", 0);
		order.put("Order Content",
				"[{\"Sheet Name\":\"\",\"Row Index\":\"1\",\"Column Index\":\"1\",\"Value\":\"\",\"Tag\":{\"Field Key\":\"\",\"Field Name\":\"\",\"Instrument Name\":\"\",\"Method Name\":\"\", \"Label\":\"\", \"Value\":\"\"}}]");

		Map<String, Object> project = new HashMap<String, Object>();
		project.put("Project Code", "");
		project.put("Project Name", "");
		if (key != 2) {
			order.put("Project", project);
		}

		Map<String, Object> sample = new HashMap<String, Object>();
		sample.put("Sample Code", "1");
		sample.put("Sample Name", "sample");
		if (key != 3) {
			order.put("Sample", sample);
		}

		Map<String, Object> task = new HashMap<String, Object>();
		task.put("Task Code", "");
		task.put("Task Name", "");
		if (key != 4) {
			order.put("Task", task);
		}

		lstorder.add(order);
		if (key == 1) {
			rtnObj.put("Sheet Order", lstorder);
		} else if (key == 2) {
			List<Map<String, Object>> lstproject = new ArrayList<Map<String, Object>>();

			project.put("Sheet Order", lstorder);
			lstproject.add(project);

			rtnObj.put("Project", lstproject);
		} else if (key == 3) {
			List<Map<String, Object>> lstsample = new ArrayList<Map<String, Object>>();

			sample.put("Sheet Order", lstorder);
			lstsample.add(sample);

			rtnObj.put("Sample", lstsample);
		} else if (key == 4) {
			List<Map<String, Object>> lsttask = new ArrayList<Map<String, Object>>();

			task.put("Sheet Order", lstorder);
			lsttask.add(task);

			rtnObj.put("Task", lsttask);
		} else {
			rtnObj.put("template", new ArrayList<LSlogilablimsorderdetail>());
		}

		return rtnObj;
	}

	public Reporttemplate savereporttemplate(Reporttemplate template) {
		Cloudreporttemplate objcoludrepo = new Cloudreporttemplate();
		objcoludrepo.setTemplatecontent(template.getTemplatecontent());

		final Optional<Reporttemplate> templateByName = reporttemplaterepository
				.findByTemplatenameIgnoreCaseAndSitemaster(template.getTemplatename(), template.getSitemaster());

		if (templateByName.isPresent()) {
			template.setResponse(new Response());
			template.getResponse().setStatus(true);
			template.getResponse().setInformation("ID_EXIST");

			return template;
		} else {
			reporttemplaterepository.save(template);
			objcoludrepo.setTemplatecode(template.getTemplatecode());

			cloudreporttemplaterepository.save(objcoludrepo);
		}
		return template;
	}

	public Reporttemplate gettemplatedata(Reporttemplate objfile) throws IOException {
		objfile.setResponse(new Response());
//		Reporttemplate Report_Objects=reporttemplaterepository.findByTemplatecode(objfile.getTemplatecode());
//		if(Report_Objects.getLockeduser()==null) {
//			Report_Objects.setLockeduser(objfile.getLockeduser());
//			Report_Objects.setLockedusername(objfile.getLockedusername());
//			reporttemplaterepository.save(Report_Objects);
//			objfile.setIsalreadyLock(false);
//			objfile.getResponse().setStatus(true);
//		}else if(Report_Objects.getLockeduser()!=null && Report_Objects.getLockeduser().equals(objfile.getObjLoggeduser().getUsercode()))
//		{
//			objfile.getResponse().setInformation("IDS_SAME_USER_OPEN");
//			objfile.getResponse().setStatus(false);
//		}else if(Report_Objects.getLockeduser()!=null) {
//			objfile.getResponse().setInformation("ALREADY_LOCKED");
//			objfile.getResponse().setStatus(false);
//			objfile.setLockeduser(Report_Objects.getLockeduser());
//			objfile.setLockedusername(Report_Objects.getLockedusername());
//			objfile.setIsalreadyLock(true);
//		}
		
		if (objfile.getIsmultitenant() == 1 || objfile.getIsmultitenant() == 2) {
			String tenant = TenantContext.getCurrentTenant();
			if (objfile.getIsmultitenant() == 2) {
				tenant = "freeusers";
			}
			String containerName = tenant + "reportdocument";
			String documentName = objfile.getFileuid();
			byte[] documentBytes = objCloudFileManipulationservice.retrieveCloudReportFile(containerName, documentName);
			if (documentBytes != null) {
				String jsonContent = new String(documentBytes, StandardCharsets.UTF_8);
				System.out.println("JSON Content:");
//				System.out.println(jsonContent);
				objfile.setTemplatecontent(jsonContent);
			} else {
				System.out.println("Failed to retrieve JSON content from Azure Blob Storage.");
			}

		}else {
			GridFSDBFile largefile = gridFsTemplate.findOne(new Query(
					Criteria.where("filename").is("ReportTemplate_" + objfile.getTemplatecode())));
			String jsonContent=new BufferedReader(new InputStreamReader(largefile.getInputStream(), StandardCharsets.UTF_8))
							.lines().collect(Collectors.joining("\n"));
			objfile.setTemplatecontent(jsonContent);
		}
		return objfile;
	}

	@SuppressWarnings("resource")
	public String getDocumentContent(byte[] documentBytes) throws IOException {
		InputStream inputStream = new ByteArrayInputStream(documentBytes);
		XWPFDocument document = new XWPFDocument(inputStream);

		StringBuilder documentContent = new StringBuilder();
		document.getParagraphs().forEach(paragraph -> documentContent.append(paragraph.getText()).append("\n"));

		return documentContent.toString();
	}
//	public String convertBytesToString(byte[] bytes) {
//		return new String(bytes, StandardCharsets.UTF_8);
//	}

	public Map<String, Object> getfolders(ReportDesignerStructure objdir) {
		Map<String, Object> rtnObj = new HashMap<String, Object>();
		List<ReportDesignerStructure> lstdir = new ArrayList<ReportDesignerStructure>();

		if (objdir.getLstuserMaster() == null || objdir.getLstuserMaster().size() == 0) {

			lstdir = reportDesignerStructureRepository
					.findBySitemasterAndViewoptionOrCreatedbyAndViewoptionOrderByDirectorycode(
							objdir.getLsuserMaster().getLssitemaster(), 1, objdir.getLsuserMaster(), 2);
		} else {
			lstdir = reportDesignerStructureRepository
					.findBySitemasterAndViewoptionOrCreatedbyAndViewoptionOrSitemasterAndViewoptionAndCreatedbyInOrderByDirectorycode(
							objdir.getLsuserMaster().getLssitemaster(), 1, objdir.getLsuserMaster(), 2,
							objdir.getLsuserMaster().getLssitemaster(), 3, objdir.getLstuserMaster());
		}
		rtnObj.put("directory", lstdir);
		return rtnObj;
	}

	public ReportDesignerStructure insertdirectory(ReportDesignerStructure objdir) {
		Response objResponse = new Response();
		ReportDesignerStructure lstdir = null;
		if (objdir.getDirectorycode() != null) {
			lstdir = reportDesignerStructureRepository.findByDirectorycodeAndParentdircodeAndDirectorynameNot(
					objdir.getDirectorycode(), objdir.getParentdircode(), objdir.getDirectoryname());
		} else {
			lstdir = reportDesignerStructureRepository.findByParentdircodeAndDirectoryname(objdir.getParentdircode(),
					objdir.getDirectoryname());
		}
		if (lstdir != null) {
			objResponse.setStatus(false);
			objResponse.setInformation("IDS_FolderExist");
		} else {
			objResponse.setStatus(true);
			objResponse.setInformation("IDS_FolderAdded");
		}
		objdir.setResponse(objResponse);
		return objdir;
	}

	public ReportDesignerStructure insertnewdirectory(ReportDesignerStructure objdir) throws Exception {
		return reportDesignerStructureRepository.save(objdir);
	}

	public List<Reporttemplate> gettemplateonfolder(ReportDesignerStructure objdir) throws Exception {
		List<Reporttemplate> lsttemplate = new ArrayList<Reporttemplate>();
		if(objdir.getLstuserMaster()==null) {
			List<LSuserMaster> newUserMasterList = new ArrayList<>();
			newUserMasterList.add(objdir.getCreatedby());
			objdir.setLstuserMaster(newUserMasterList);			
		}
		String searchKeyword = objdir.getSearchData().get("searchkeyword");
		String searchtemplatename = objdir.getSearchData().get("searchtemplatename");
		if (objdir.getFilefor().equals("RDT") || objdir.getFilefor().equals("RAT")) {
			if (objdir.getLstuserMaster() == null) {
				objdir.setLstuserMaster(new ArrayList<LSuserMaster>());
				objdir.getLstuserMaster().add(objdir.getCreatedby());
			}
			
			if(searchKeyword != null && searchKeyword.equals("")
					&& searchtemplatename != null && searchtemplatename.equals("")) {
				lsttemplate = reporttemplaterepository
						.findBySitemasterAndViewoptionAndTemplatetypeAndCreatedbyInAndDateCreatedBetweenOrSitemasterAndViewoptionAndTemplatetypeAndCreatedbyAndDateCreatedBetweenOrSitemasterAndViewoptionAndTemplatetypeAndCreatedbyInAndDateCreatedBetweenOrderByTemplatecodeDesc(
								objdir.getSitemaster(), 1, objdir.getTemplatetype(), objdir.getLstuserMaster(),
								objdir.getFromdate(), objdir.getTodate(), objdir.getSitemaster(), 2,
								objdir.getTemplatetype(), objdir.getCreatedby(), objdir.getFromdate(), objdir.getTodate(),
								objdir.getSitemaster(), 3, objdir.getTemplatetype(), objdir.getLstuserMaster(),
								objdir.getFromdate(), objdir.getTodate());
			}else if(searchKeyword != null && !searchKeyword.equals("")
					&& searchtemplatename != null && searchtemplatename.equals("")) {
				lsttemplate = reporttemplaterepository
						.findBySitemasterAndViewoptionAndTemplatetypeAndCreatedbyInAndDateCreatedBetweenAndKeywordContainingIgnoreCaseOrSitemasterAndViewoptionAndTemplatetypeAndCreatedbyAndDateCreatedBetweenAndKeywordContainingIgnoreCaseOrSitemasterAndViewoptionAndTemplatetypeAndCreatedbyInAndDateCreatedBetweenAndKeywordContainingIgnoreCaseOrderByTemplatecodeDesc(
								objdir.getSitemaster(), 1, objdir.getTemplatetype(), objdir.getLstuserMaster(),
								objdir.getFromdate(), objdir.getTodate(),searchKeyword, objdir.getSitemaster(), 2,
								objdir.getTemplatetype(), objdir.getCreatedby(), objdir.getFromdate(), objdir.getTodate(),searchKeyword,
								objdir.getSitemaster(), 3, objdir.getTemplatetype(), objdir.getLstuserMaster(),
								objdir.getFromdate(), objdir.getTodate(),searchKeyword);
			}else if(searchKeyword != null && searchKeyword.equals("")
					&& searchtemplatename != null && !searchtemplatename.equals("")) {
				lsttemplate = reporttemplaterepository
						.findBySitemasterAndViewoptionAndTemplatetypeAndCreatedbyInAndDateCreatedBetweenAndTemplatenameContainingIgnoreCaseOrSitemasterAndViewoptionAndTemplatetypeAndCreatedbyAndDateCreatedBetweenAndTemplatenameContainingIgnoreCaseOrSitemasterAndViewoptionAndTemplatetypeAndCreatedbyInAndDateCreatedBetweenAndTemplatenameContainingIgnoreCaseOrderByTemplatecodeDesc(
								objdir.getSitemaster(), 1, objdir.getTemplatetype(), objdir.getLstuserMaster(),
								objdir.getFromdate(), objdir.getTodate(),searchtemplatename, objdir.getSitemaster(), 2,
								objdir.getTemplatetype(), objdir.getCreatedby(), objdir.getFromdate(), objdir.getTodate(),searchtemplatename,
								objdir.getSitemaster(), 3, objdir.getTemplatetype(), objdir.getLstuserMaster(),
								objdir.getFromdate(), objdir.getTodate(),searchtemplatename);
			}else if(searchKeyword != null && !searchKeyword.equals("")
					&& searchtemplatename != null && !searchtemplatename.equals("")) {
				lsttemplate = reporttemplaterepository
						.findBySitemasterAndViewoptionAndTemplatetypeAndCreatedbyInAndDateCreatedBetweenAndTemplatenameContainingIgnoreCaseAndKeywordContainingIgnoreCaseOrSitemasterAndViewoptionAndTemplatetypeAndCreatedbyAndDateCreatedBetweenAndTemplatenameContainingIgnoreCaseAndKeywordContainingIgnoreCaseOrSitemasterAndViewoptionAndTemplatetypeAndCreatedbyInAndDateCreatedBetweenAndTemplatenameContainingIgnoreCaseAndKeywordContainingIgnoreCaseOrderByTemplatecodeDesc(
								objdir.getSitemaster(), 1, objdir.getTemplatetype(), objdir.getLstuserMaster(),
								objdir.getFromdate(), objdir.getTodate(),searchtemplatename,searchKeyword, objdir.getSitemaster(), 2,
								objdir.getTemplatetype(), objdir.getCreatedby(), objdir.getFromdate(), objdir.getTodate(),searchtemplatename,searchKeyword,
								objdir.getSitemaster(), 3, objdir.getTemplatetype(), objdir.getLstuserMaster(),
								objdir.getFromdate(), objdir.getTodate(),searchtemplatename,searchKeyword);
			}

			
		} else if (objdir.getFilefor().equals("DR")) {
			
			if (objdir.getTemplatetype() == -1 && searchKeyword != null && searchKeyword.equals("")
					&& searchtemplatename != null && searchtemplatename.equals("")) {
				lsttemplate = reporttemplaterepository
						.findBySitemasterAndViewoptionAndCreatedbyInAndDateCreatedBetweenAndReportdesignstructureOrSitemasterAndViewoptionAndCreatedbyAndDateCreatedBetweenAndReportdesignstructureOrSitemasterAndViewoptionAndCreatedbyInAndDateCreatedBetweenAndReportdesignstructureOrderByTemplatecodeDesc(
								objdir.getSitemaster(), 1, objdir.getLstuserMaster(), objdir.getFromdate(),
								objdir.getTodate(), objdir, objdir.getSitemaster(), 2, objdir.getCreatedby(),
								objdir.getFromdate(), objdir.getTodate(), objdir, objdir.getSitemaster(), 3,
								objdir.getLstuserMaster(), objdir.getFromdate(), objdir.getTodate(), objdir);
			} else if (objdir.getTemplatetype() != -1 && searchKeyword != null && searchKeyword.equals("")
					&& searchtemplatename != null && searchtemplatename.equals("")) {
				lsttemplate = reporttemplaterepository
						.findBySitemasterAndViewoptionAndTemplatetypeAndCreatedbyInAndDateCreatedBetweenAndReportdesignstructureOrSitemasterAndViewoptionAndTemplatetypeAndCreatedbyAndDateCreatedBetweenAndReportdesignstructureOrSitemasterAndViewoptionAndTemplatetypeAndCreatedbyInAndDateCreatedBetweenAndReportdesignstructureOrderByTemplatecodeDesc(
								objdir.getSitemaster(), 1, objdir.getTemplatetype(), objdir.getLstuserMaster(),
								objdir.getFromdate(), objdir.getTodate(), objdir, objdir.getSitemaster(), 2,
								objdir.getTemplatetype(), objdir.getCreatedby(), objdir.getFromdate(),
								objdir.getTodate(), objdir, objdir.getSitemaster(), 3, objdir.getTemplatetype(),
								objdir.getLstuserMaster(), objdir.getFromdate(), objdir.getTodate(), objdir);
			} else if (objdir.getTemplatetype() == -1 && searchKeyword != null && !searchKeyword.equals("")
					&& searchtemplatename != null && searchtemplatename.equals("")) {
//				lsttemplate = reporttemplaterepository.findByKeywordContainingIgnoreCase("s");

				lsttemplate = reporttemplaterepository
						.findBySitemasterAndViewoptionAndCreatedbyInAndDateCreatedBetweenAndReportdesignstructureAndKeywordContainingIgnoreCaseOrSitemasterAndViewoptionAndCreatedbyAndDateCreatedBetweenAndReportdesignstructureAndKeywordContainingIgnoreCaseOrSitemasterAndViewoptionAndCreatedbyInAndDateCreatedBetweenAndReportdesignstructureAndKeywordContainingIgnoreCaseOrderByTemplatecodeDesc(
								objdir.getSitemaster(), 1, objdir.getLstuserMaster(), objdir.getFromdate(),
								objdir.getTodate(), objdir, searchKeyword, objdir.getSitemaster(), 2,
								objdir.getCreatedby(), objdir.getFromdate(), objdir.getTodate(), objdir, searchKeyword,
								objdir.getSitemaster(), 3, objdir.getLstuserMaster(), objdir.getFromdate(),
								objdir.getTodate(), objdir, searchKeyword);

			} else if (objdir.getTemplatetype() == -1 && searchKeyword != null && searchKeyword.equals("")
					&& searchtemplatename != null && !searchtemplatename.equals("")) {
				lsttemplate = reporttemplaterepository
						.findBySitemasterAndViewoptionAndCreatedbyInAndDateCreatedBetweenAndReportdesignstructureAndTemplatenameContainingIgnoreCaseOrSitemasterAndViewoptionAndCreatedbyAndDateCreatedBetweenAndReportdesignstructureAndTemplatenameContainingIgnoreCaseOrSitemasterAndViewoptionAndCreatedbyInAndDateCreatedBetweenAndReportdesignstructureAndTemplatenameContainingIgnoreCaseOrderByTemplatecodeDesc(
								objdir.getSitemaster(), 1, objdir.getLstuserMaster(), objdir.getFromdate(),
								objdir.getTodate(), objdir, searchtemplatename, objdir.getSitemaster(), 2,
								objdir.getCreatedby(), objdir.getFromdate(), objdir.getTodate(), objdir,
								searchtemplatename, objdir.getSitemaster(), 3, objdir.getLstuserMaster(),
								objdir.getFromdate(), objdir.getTodate(), objdir, searchtemplatename);
			} else if (objdir.getTemplatetype() != -1 && searchKeyword != null && !searchKeyword.equals("")
					&& searchtemplatename != null && searchtemplatename.equals("")) {

				lsttemplate = reporttemplaterepository
						.findBySitemasterAndViewoptionAndCreatedbyInAndDateCreatedBetweenAndReportdesignstructureAndKeywordContainingIgnoreCaseAndTemplatetypeOrSitemasterAndViewoptionAndCreatedbyAndDateCreatedBetweenAndReportdesignstructureAndKeywordContainingIgnoreCaseAndTemplatetypeOrSitemasterAndViewoptionAndCreatedbyInAndDateCreatedBetweenAndReportdesignstructureAndKeywordContainingIgnoreCaseAndTemplatetypeOrderByTemplatecodeDesc(
								objdir.getSitemaster(), 1, objdir.getLstuserMaster(), objdir.getFromdate(),
								objdir.getTodate(), objdir, searchKeyword,objdir.getTemplatetype(), objdir.getSitemaster(), 2,
								objdir.getCreatedby(), objdir.getFromdate(), objdir.getTodate(), objdir, searchKeyword,objdir.getTemplatetype(),
								objdir.getSitemaster(), 3, objdir.getLstuserMaster(), objdir.getFromdate(),
								objdir.getTodate(), objdir, searchKeyword,objdir.getTemplatetype());
				
			} else if (objdir.getTemplatetype() != -1 && searchKeyword != null && searchKeyword.equals("")
					&& searchtemplatename != null && !searchtemplatename.equals("")) {
				lsttemplate = reporttemplaterepository
						.findBySitemasterAndViewoptionAndTemplatetypeAndCreatedbyInAndDateCreatedBetweenAndReportdesignstructureAndTemplatenameContainingIgnoreCaseOrSitemasterAndViewoptionAndTemplatetypeAndCreatedbyAndDateCreatedBetweenAndReportdesignstructureAndTemplatenameContainingIgnoreCaseOrSitemasterAndViewoptionAndTemplatetypeAndCreatedbyInAndDateCreatedBetweenAndReportdesignstructureAndTemplatenameContainingIgnoreCaseOrderByTemplatecodeDesc(
								objdir.getSitemaster(), 1, objdir.getTemplatetype(), objdir.getLstuserMaster(),
								objdir.getFromdate(), objdir.getTodate(), objdir,searchtemplatename, objdir.getSitemaster(), 2,
								objdir.getTemplatetype(), objdir.getCreatedby(), objdir.getFromdate(),
								objdir.getTodate(), objdir,searchtemplatename, objdir.getSitemaster(), 3, objdir.getTemplatetype(),
								objdir.getLstuserMaster(), objdir.getFromdate(), objdir.getTodate(), objdir,searchtemplatename);
			} else if (objdir.getTemplatetype() == -1 && searchKeyword != null && !searchKeyword.equals("")
					&& searchtemplatename != null && !searchtemplatename.equals("")) {
				
				
				lsttemplate = reporttemplaterepository
						.findBySitemasterAndViewoptionAndCreatedbyInAndDateCreatedBetweenAndReportdesignstructureAndKeywordContainingIgnoreCaseAndTemplatenameContainingIgnoreCaseOrSitemasterAndViewoptionAndCreatedbyAndDateCreatedBetweenAndReportdesignstructureAndKeywordContainingIgnoreCaseAndTemplatenameContainingIgnoreCaseOrSitemasterAndViewoptionAndCreatedbyInAndDateCreatedBetweenAndReportdesignstructureAndKeywordContainingIgnoreCaseAndTemplatenameContainingIgnoreCaseOrderByTemplatecodeDesc(
								objdir.getSitemaster(), 1,objdir.getLstuserMaster(),
								objdir.getFromdate(), objdir.getTodate(), objdir, searchKeyword, searchtemplatename,
								objdir.getSitemaster(), 2, objdir.getCreatedby(),
								objdir.getFromdate(), objdir.getTodate(), objdir, searchKeyword, searchtemplatename,
								objdir.getSitemaster(), 3, objdir.getLstuserMaster(),
								objdir.getFromdate(), objdir.getTodate(), objdir, searchKeyword, searchtemplatename);
			}

			else if (objdir.getTemplatetype() != -1 && searchKeyword != null && !searchKeyword.equals("")
					&& searchtemplatename != null && !searchtemplatename.equals("")) {
				lsttemplate = reporttemplaterepository
						.findBySitemasterAndViewoptionAndTemplatetypeAndCreatedbyInAndDateCreatedBetweenAndReportdesignstructureAndKeywordContainingIgnoreCaseAndTemplatenameContainingIgnoreCaseOrSitemasterAndViewoptionAndTemplatetypeAndCreatedbyAndDateCreatedBetweenAndReportdesignstructureAndKeywordContainingIgnoreCaseAndTemplatenameContainingIgnoreCaseOrSitemasterAndViewoptionAndTemplatetypeAndCreatedbyInAndDateCreatedBetweenAndReportdesignstructureAndKeywordContainingIgnoreCaseAndTemplatenameContainingIgnoreCaseOrderByTemplatecodeDesc(
								objdir.getSitemaster(), 1, objdir.getTemplatetype(), objdir.getLstuserMaster(),
								objdir.getFromdate(), objdir.getTodate(), objdir, searchKeyword, searchtemplatename,
								objdir.getSitemaster(), 2, objdir.getTemplatetype(), objdir.getCreatedby(),
								objdir.getFromdate(), objdir.getTodate(), objdir, searchKeyword, searchtemplatename,
								objdir.getSitemaster(), 3, objdir.getTemplatetype(), objdir.getLstuserMaster(),
								objdir.getFromdate(), objdir.getTodate(), objdir, searchKeyword, searchtemplatename);
			}

//			lsttemplate = reporttemplaterepository.findByReportdesignstructure(objdir);
		}

		return lsttemplate;
	}

	public Map<String, Object> getordersonreport(Map<String, Object> objMap) {

		Map<String, Object> rtnObj = new HashMap<String, Object>();
		ObjectMapper objm = new ObjectMapper();
		Integer sitecode = (Integer) objMap.get("sitecode");
//		LSprojectmaster lsproject = objm.convertValue(objMap.get("lsprojectmaster"), LSprojectmaster.class);

		List<LStestmasterlocal> lstest = new ObjectMapper().convertValue(objMap.get("lstestmasterlocal"),
				new TypeReference<List<LStestmasterlocal>>() {
				});
		List<Integer> testid = null;
		if (lstest != null) {
			testid = lstest.stream().map(LStestmasterlocal::getTestcode).collect(Collectors.toList());
		}

//		List<LSsamplemaster> lssample = new ObjectMapper().convertValue(objMap.get("lssamplemaster"),
//				new TypeReference<List<LSsamplemaster>>() {
//				});

//		List<LSlogilabprotocoldetail> orderList = new ArrayList<LSlogilabprotocoldetail>();
//		if (testid != null) {
//			if (lssample != null) {
//				orderList = lslogilabprotocoldetailRepository
//						.findBylsprojectmasterAndTestcodeInAndLssamplemasterIn(lsproject, testid, lssample);
//			} else {
//				orderList = lslogilabprotocoldetailRepository.findBylsprojectmasterAndTestcodeIn(lsproject, testid);
//			}
//		}

		List<LSprotocolmastertest> LSprotocoltestLst = lsprotocolmastertestRepository.findByTesttypeAndTestcodeIn(1,
				testid);
		List<Integer> lstprotocoltemp = LSprotocoltestLst.stream().map(LSprotocolmastertest::getProtocolmastercode)
				.collect(Collectors.toList());
		List<LSprotocolmaster> lstprotocol = LSProtocolMasterRepositoryObj
				.findByStatusAndApprovedAndLssitemasterAndProtocolmastercodeIn(1, 1, sitecode, lstprotocoltemp);

		List<Integer> lstprotocolcode = lstprotocol.stream().map(LSprotocolmaster::getProtocolmastercode)
				.collect(Collectors.toList());

//		ObjectMapper objm = new ObjectMapper();

		int multitenent = objm.convertValue(objMap.get("ismultitenant"), Integer.class);

		List<LSprotocolstep> LSprotocolsteplst = LSProtocolStepRepositoryObj
				.findByProtocolmastercodeInAndStatus(lstprotocolcode, 1);
		List<LSprotocolstep> LSprotocolstepLst = new ArrayList<LSprotocolstep>();

		List<LSprotocolversion> LSprotocolversionlst = lsprotocolversionRepository
				.findByprotocolmastercodeIn(lstprotocoltemp);

		Collections.sort(LSprotocolversionlst, Collections.reverseOrder());

		for (LSprotocolstep LSprotocolstepObj1 : LSprotocolsteplst) {
			if (multitenent == 1) {
				LSprotocolstepInformation newobj = lsprotocolstepInformationRepository
						.findById(LSprotocolstepObj1.getProtocolstepcode());
//				List<LSprotocolmaster> protocolname = LSProtocolMasterRepositoryObj
//						.findByProtocolmastercode(LSprotocolstepObj1.getProtocolmastercode());
//				String name = protocolname.get(0).getProtocolmastername();
//				LSprotocolstepObj1.setProtocolname(name);
				if (newobj != null) {
					LSprotocolstepObj1.setLsprotocolstepInformation(newobj.getLsprotocolstepInfo());

				}
				LSprotocolstepLst.add(LSprotocolstepObj1);

			} else if (multitenent == 0) {
				LSprotocolstepInfo newLSprotocolstepInfo = mongoTemplate
						.findById(LSprotocolstepObj1.getProtocolstepcode(), LSprotocolstepInfo.class);
				if (newLSprotocolstepInfo != null) {
//						LSprotocolstepObj1.setLsprotocolstepInfo(newLSprotocolstepInfo.getContent());
					LSprotocolstepObj1.setLsprotocolstepInformation(newLSprotocolstepInfo.getContent());

				}
				LSprotocolstepLst.add(LSprotocolstepObj1);
			}

		}
		if (LSprotocolsteplst != null) {
			rtnObj.put("protocolstepLst", LSprotocolstepLst);
			rtnObj.put("LSprotocolversionlst", LSprotocolversionlst);
		} else {
			rtnObj.put("protocolstepLst", new ArrayList<>());
			rtnObj.put("LSprotocolversionlst", new ArrayList<>());
		}
		List<LSprotocolmaster> lsProtocolMaster = lsProtocolMasterRepository
				.findByprotocolmastercodeIn(lstprotocoltemp);
		ArrayList<String> arrayList = new ArrayList<>();
		try {
			for (LSprotocolmaster lstprotocolmaster : lsProtocolMaster) {

				if (lstprotocolmaster.getFileuid() != null) {
					arrayList.add(objCloudFileManipulationservice.retrieveCloudSheets(lstprotocolmaster.getFileuid(),
							TenantContext.getCurrentTenant() + "protocol"));
					rtnObj.put("ProtocolData", arrayList);
				}

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		rtnObj.put("lstprotocoltempl", lstprotocol);
		return rtnObj;
	}

	public Reporttemplate approvereporttemplate(Reporttemplate objdir) throws ParseException {
		objdir.setCompleteddate(commonfunction.getCurrentUtcTime());
		reporttemplaterepository.save(objdir);
		return objdir;
	}

	public List<Reporttemplate> gettemplateonfoldermapping(ReportDesignerStructure objdir) {
		if (objdir.getLstuserMaster() == null) {
			objdir.setLstuserMaster(new ArrayList<LSuserMaster>());
			objdir.getLstuserMaster().add(objdir.getCreatedby());
		}
		return reporttemplaterepository
				.findBySitemasterAndViewoptionAndTemplatetypeAndCreatedbyInOrSitemasterAndViewoptionAndTemplatetypeAndCreatedbyOrSitemasterAndViewoptionAndTemplatetypeAndCreatedbyInOrderByTemplatecodeDesc(
						objdir.getSitemaster(), 1, objdir.getTemplatetype(), objdir.getLstuserMaster(),
						objdir.getSitemaster(), 2, objdir.getTemplatetype(), objdir.getCreatedby(),
						objdir.getSitemaster(), 3, objdir.getTemplatetype(), objdir.getLstuserMaster());
	}

	public Reporttemplate updatereporttemplatemapping(Reporttemplate objdir) {

//		reporttemplaterepository.save(objdir);
		reportTemplateMappingRepository.deleteByTemplatecode(objdir.getTemplatecode());
		reportTemplateMappingRepository.save(objdir.getReportTemplateMappings());
		return objdir;
	}

	public Map<String, Object> onGetReportTemplateBasedOnProject(Map<String, Object> objMap) {

		Map<String, Object> rtnMap = new HashMap<String, Object>();

		ObjectMapper objm = new ObjectMapper();
		LSSiteMaster objLsSiteMaster = objm.convertValue(objMap.get("sitemaster"), LSSiteMaster.class);
		Integer getType = (Integer) objMap.get("gettype");

		List<Reporttemplate> lstTemp = new ArrayList<Reporttemplate>();

		if (getType == 1) {
			List<LSprojectmaster> listProj = lsprojectmasterRepository
					.findByLssitemasterAndStatusOrderByProjectcodeDesc(objLsSiteMaster, 1);

			if (!listProj.isEmpty()) {
				List<ReportTemplateMapping> lstMappedTemp = reportTemplateMappingRepository
						.findByLsprojectmaster(listProj.get(0));
				List<Long> lstTempCode = lstMappedTemp.stream().map(ReportTemplateMapping::getTemplatecode)
						.collect(Collectors.toList());
				lstTemp = reporttemplaterepository
						.findByTemplatecodeInAndTemplatetypeOrderByTemplatecodeDesc(lstTempCode, 2);
			}

			rtnMap.put("project", listProj);
		} else {
			LSprojectmaster objProject = objm.convertValue(objMap.get("project"), LSprojectmaster.class);
			List<ReportTemplateMapping> lstMappedTemp = reportTemplateMappingRepository
					.findByLsprojectmaster(objProject);
			List<Long> lstTempCode = lstMappedTemp.stream().map(ReportTemplateMapping::getTemplatecode)
					.collect(Collectors.toList());
			lstTemp = reporttemplaterepository.findByTemplatecodeInAndTemplatetypeOrderByTemplatecodeDesc(lstTempCode,
					2);
		}

		rtnMap.put("template", lstTemp);

		return rtnMap;
	}

	public ReportTemplateVersion gettemplateversiondata(ReportTemplateVersion objfile) {
		if (objfile.getIsmultitenant() == 1 || objfile.getIsmultitenant() == 2) {
			String tenant = TenantContext.getCurrentTenant();
			if (objfile.getIsmultitenant() == 2) {
				tenant = "freeusers";
			}
			String containerName = tenant + "reporttemplateversion";
			String documentName = objfile.getFileuid();
			byte[] documentBytes = objCloudFileManipulationservice.retrieveCloudReportFile(containerName, documentName);
			if (documentBytes != null) {
				String jsonContent = new String(documentBytes, StandardCharsets.UTF_8);
				System.out.println("JSON Content:");
				System.out.println(jsonContent);
				objfile.setTemplateversioncontent(jsonContent);
			} else {
				System.out.println("Failed to retrieve JSON content from Azure Blob Storage.");
			}

		}else {

			GridFSDBFile largefile = gridFsTemplate.findOne(new Query(
					Criteria.where("filename").is("ReportTemplateVersion_" + objfile.getTemplateversioncode())));
			String jsonContent=new BufferedReader(new InputStreamReader(largefile.getInputStream(), StandardCharsets.UTF_8))
							.lines().collect(Collectors.joining("\n"));
			objfile.setTemplateversioncontent(jsonContent);
		
		}
		return objfile;
	}

	public ReportDesignerStructure UpdateFolderforReportDesignerStructure(ReportDesignerStructure folders) {
		reportDesignerStructureRepository.updatedirectory(folders.getParentdircode(), folders.getPath(),
				folders.getDirectorycode(), folders.getDirectoryname());
		return folders;
	}

	public List<ReportDesignerStructure> DeletedirectoriesonReportDesigner(ReportDesignerStructure[] directories) {
		List<ReportDesignerStructure> lstdirectories = Arrays.asList(directories);

		lstdirectories.forEach(structure -> {
			if (structure.getParentdircode() == -2) {
				reportDesignerStructureRepository.delete(structure.getDirectorycode());
				reportDesignerStructureRepository.updateparentdirectory(structure.getDircodetomove(),
						structure.getDirectorycode());
			} else {
				reportDesignerStructureRepository.updatedirectory(structure.getParentdircode(), structure.getPath(),
						structure.getDirectorycode(), structure.getDirectoryname());
			}
		});

		return lstdirectories;

	}

	public ReportDesignerStructure getMoveDirectoryonReportDesigner(ReportDesignerStructure objdir) {
		Response objResponse = new Response();
		ReportDesignerStructure lstdir = null;
		String dir = objdir.getDirectoryname();
		if (objdir.getDirectorycode() != null) {
			lstdir = reportDesignerStructureRepository.findByDirectorycodeAndParentdircodeAndDirectorynameNot(
					objdir.getDirectorycode(), objdir.getParentdircode(), objdir.getDirectoryname());
		} else {
			lstdir = reportDesignerStructureRepository.findByParentdircodeAndDirectoryname(objdir.getParentdircode(),
					objdir.getDirectoryname());
		}
		while (lstdir != null) {
			if (dir.charAt(dir.length() - 1) == ')') {
				char temp = dir.charAt(dir.length() - 2);
				int n = Character.getNumericValue(temp);
				n = n + 1;
				dir = dir.substring(0, dir.length() - 2) + n + dir.substring(dir.length() - 1);
				objdir.setDirectoryname(dir);
			} else {
				dir = dir + " (2)";
				objdir.setDirectoryname(dir);
			}
			lstdir = reportDesignerStructureRepository.findByParentdircodeAndDirectoryname(objdir.getParentdircode(),
					objdir.getDirectoryname());

		}
		objdir.setResponse(objResponse);
		return objdir;
	}

	public List<Reporttemplate> UpdateReporttemplate(Reporttemplate[] files) {
		List<Reporttemplate> lstfile = Arrays.asList(files);
		if (lstfile.size() > 0) {
			List<Long> lstfilesid = lstfile.stream().map(Reporttemplate::getTemplatecode).collect(Collectors.toList());
			reporttemplaterepository.updatedirectory(lstfile.get(0).getReportdesignstructure().getDirectorycode(),
					lstfilesid);
		}
		return lstfile;
	}

	public Reporttemplate UpdateReporttemplateforsinglefile(Reporttemplate file) {
		reporttemplaterepository.updatedirectoryonsinglefile(file.getReportdesignstructure().getDirectorycode(),
				file.getTemplatecode());
		return file;
	}

	public ReportDesignerStructure MovedirectoryonReporttemplate(ReportDesignerStructure directory) {
		reportDesignerStructureRepository.updatedirectory(directory.getParentdircode(), directory.getPath(),
				directory.getDirectorycode(), directory.getDirectoryname());
		return directory;
	}

	public Reporttemplate RenameReporttemplate(Reporttemplate template) throws ParseException {
		Date ModifiedDate = template.getKeystorevariable().equals("Rename_File") ? commonfunction.getCurrentUtcTime()
				: template.getDateModified();
		reporttemplaterepository.updateTemplateDetails(template.getTemplatecode(), template.getTemplatename(),
				ModifiedDate, template.getReportdesignstructure().getDirectorycode());
		return template;
	}

	public Response TocheckTemplateexist(Reporttemplate template) {
		final Optional<Reporttemplate> IstemplateExist = reporttemplaterepository
				.findByTemplatenameIgnoreCaseAndSitemaster(template.getTemplatename(), template.getSitemaster());
		Response response = new Response();
		if (IstemplateExist.isPresent()) {
			response.setStatus(true);
		} else {
			response.setStatus(false);
		}
		return response;
	}

	public List<Reporttemplate> MoveReportTemplate(Reporttemplate[] template) {
		List<Reporttemplate> rttemplate = Arrays.asList(template);
		List<Long> lstfilesid = rttemplate.stream().map(Reporttemplate::getTemplatecode)
				.collect(Collectors.toList());
		reporttemplaterepository.updateTemplateDetailsMove(lstfilesid,rttemplate.get(0).getDircodetomove());
		rttemplate.get(0).setResponse(new Response());
		rttemplate.get(0).getResponse().setStatus(true);
		
		return rttemplate;
	}

	public List<Reporttemplate> GetUnlockscreendata(LSuserMaster objdir) {
		List<Reporttemplate> telsttemplate;
		if (objdir.getUsername().equalsIgnoreCase("Administrator")) {
			return reporttemplaterepository
					.findBySitemasterAndAndLockeduserIsNotNullOrderByTemplatecodeDesc(
							objdir.getLssitemaster());
		} else {
			telsttemplate = reporttemplaterepository
					.findBySitemasterAndViewoptionAndCreatedbyInAndLockeduserIsNotNullOrSitemasterAndViewoptionAndCreatedbyAndLockeduserIsNotNullOrSitemasterAndViewoptionAndCreatedbyInAndLockeduserIsNotNullOrderByTemplatecodeDesc(
							objdir.getLssitemaster(), 1,objdir.getUsernotify(),
							objdir.getLssitemaster(), 2,
							objdir,
							objdir.getLssitemaster(), 3, objdir.getUsernotify()
							);	
		}
		
		
		return telsttemplate;
	}

	public Boolean UnloackReporttemplate(Long[] reporttemplate) {
		if (reporttemplate.length > 0) {
			List<Long> reporttemplatecode = Arrays.asList(reporttemplate);
			reporttemplaterepository.Updatelockedusersonreporttemplate(reporttemplatecode);
			return true;
		} else {
			return false;
		}
	}
}
