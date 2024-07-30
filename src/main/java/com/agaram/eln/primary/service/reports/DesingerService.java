package com.agaram.eln.primary.service.reports;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
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
import org.springframework.stereotype.Service;

import com.agaram.eln.primary.commonfunction.commonfunction;
import com.agaram.eln.primary.config.TenantContext;
import com.agaram.eln.primary.model.cloudProtocol.LSprotocolstepInformation;
import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.instrumentDetails.LSlogilablimsorderdetail;
import com.agaram.eln.primary.model.instrumentDetails.LSprotocolfolderfiles;
import com.agaram.eln.primary.model.instrumentDetails.Lsprotocolorderstructure;
import com.agaram.eln.primary.model.protocols.LSlogilabprotocoldetail;
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
				.findByTemplatenameIgnoreCase(template.getTemplatename());

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
				System.out.println(jsonContent);
				objfile.setTemplatecontent(jsonContent);
			} else {
				System.out.println("Failed to retrieve JSON content from Azure Blob Storage.");
			}

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
		if(objdir.getFilefor().equals("RDT")||objdir.getFilefor().equals("RAT")){
			if(objdir.getLstuserMaster()==null) {
				objdir.setLstuserMaster(new ArrayList<LSuserMaster>());
				objdir.getLstuserMaster().add(objdir.getCreatedby());
			}
			
			lsttemplate=reporttemplaterepository.findBySitemasterAndViewoptionAndTemplatetypeAndCreatedbyInAndDateCreatedBetweenOrSitemasterAndViewoptionAndTemplatetypeAndCreatedbyAndDateCreatedBetweenOrSitemasterAndViewoptionAndTemplatetypeAndCreatedbyInAndDateCreatedBetweenOrderByTemplatecodeDesc(objdir.getSitemaster(),1,objdir.getTemplatetype(),objdir.getLstuserMaster(),
					objdir.getFromdate(),objdir.getTodate(),objdir.getSitemaster(),2,objdir.getTemplatetype(),objdir.getCreatedby(),objdir.getFromdate(),objdir.getTodate(),objdir.getSitemaster(),3,objdir.getTemplatetype(),objdir.getLstuserMaster(),objdir.getFromdate(),objdir.getTodate());
		}else if(objdir.getFilefor().equals("DR")) {
			lsttemplate = reporttemplaterepository.findByReportdesignstructure(objdir);	
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
		if(objdir.getLstuserMaster()==null) {
			objdir.setLstuserMaster(new ArrayList<LSuserMaster>());
			objdir.getLstuserMaster().add(objdir.getCreatedby());
		}
		return reporttemplaterepository.findBySitemasterAndViewoptionAndTemplatetypeAndCreatedbyInOrSitemasterAndViewoptionAndTemplatetypeAndCreatedbyOrSitemasterAndViewoptionAndTemplatetypeAndCreatedbyInOrderByTemplatecodeDesc(objdir.getSitemaster(),1,objdir.getTemplatetype(),objdir.getLstuserMaster(),
				objdir.getSitemaster(),2,objdir.getTemplatetype(),objdir.getCreatedby(),objdir.getSitemaster(),1,objdir.getTemplatetype(),objdir.getLstuserMaster());
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
		
		if(getType == 1) {
			List<LSprojectmaster> listProj = lsprojectmasterRepository.findByLssitemasterAndStatusOrderByProjectcodeDesc(objLsSiteMaster, 1);
			
			if(!listProj.isEmpty()) {
				List<ReportTemplateMapping> lstMappedTemp = reportTemplateMappingRepository.findByLsprojectmaster(listProj.get(0));
				List<Long> lstTempCode = lstMappedTemp.stream().map(ReportTemplateMapping::getTemplatecode).collect(Collectors.toList());
				lstTemp = reporttemplaterepository.findByTemplatecodeInAndTemplatetypeOrderByTemplatecodeDesc(lstTempCode,2);
			}
			
			rtnMap.put("project", listProj);
		}else {
			LSprojectmaster objProject = objm.convertValue(objMap.get("project"), LSprojectmaster.class);
			List<ReportTemplateMapping> lstMappedTemp = reportTemplateMappingRepository.findByLsprojectmaster(objProject);
			List<Long> lstTempCode = lstMappedTemp.stream().map(ReportTemplateMapping::getTemplatecode).collect(Collectors.toList());
			lstTemp = reporttemplaterepository.findByTemplatecodeInAndTemplatetypeOrderByTemplatecodeDesc(lstTempCode,2);
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
			List<Long> lstfilesid = lstfile.stream().map(Reporttemplate::getTemplatecode)
					.collect(Collectors.toList());
			reporttemplaterepository.updatedirectory(lstfile.get(0).getReportdesignstructure().getDirectorycode(), lstfilesid);
		}
		return lstfile;
	}

	public Reporttemplate UpdateReporttemplateforsinglefile(Reporttemplate file) {
		reporttemplaterepository.updatedirectoryonsinglefile(file.getReportdesignstructure().getDirectorycode(), file.getTemplatecode());
		return file;
	}

	public ReportDesignerStructure MovedirectoryonReporttemplate(ReportDesignerStructure directory) {
		reportDesignerStructureRepository.updatedirectory(directory.getParentdircode(), directory.getPath(),
				directory.getDirectorycode(), directory.getDirectoryname());
		return directory;
	}
}
