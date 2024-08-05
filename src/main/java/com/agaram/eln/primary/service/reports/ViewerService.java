package com.agaram.eln.primary.service.reports;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.agaram.eln.primary.config.TenantContext;
import com.agaram.eln.primary.model.cloudFileManip.CloudOrderCreation;
import com.agaram.eln.primary.model.cloudProtocol.CloudLsLogilabprotocolstepInfo;
import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.instrumentDetails.LSlogilablimsorderdetail;
import com.agaram.eln.primary.model.protocols.LSlogilabprotocoldetail;
import com.agaram.eln.primary.model.protocols.LSlogilabprotocolsteps;
import com.agaram.eln.primary.model.protocols.LSprotocolmaster;
import com.agaram.eln.primary.model.protocols.LSprotocolorderimages;
import com.agaram.eln.primary.model.protocols.LsLogilabprotocolstepInfo;
import com.agaram.eln.primary.model.reports.lsreportfile;
import com.agaram.eln.primary.model.reports.reportviewer.Cloudreports;
import com.agaram.eln.primary.model.reports.reportviewer.ReportViewerStructure;
import com.agaram.eln.primary.model.reports.reportviewer.Reports;
import com.agaram.eln.primary.model.sheetManipulation.LSsamplefile;
import com.agaram.eln.primary.model.sheetManipulation.LStestmasterlocal;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.repository.cloudFileManip.CloudOrderCreationRepository;
import com.agaram.eln.primary.repository.cloudProtocol.CloudLsLogilabprotocolstepInfoRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LSlogilablimsorderdetailRepository;
import com.agaram.eln.primary.repository.protocol.LSProtocolMasterRepository;
import com.agaram.eln.primary.repository.protocol.LSlogilabprotocoldetailRepository;
import com.agaram.eln.primary.repository.protocol.LSlogilabprotocolstepsRepository;
import com.agaram.eln.primary.repository.protocol.LSprotocolorderimagesRepository;
import com.agaram.eln.primary.repository.reports.ReportfileRepository;
import com.agaram.eln.primary.repository.reports.reportviewer.CloudreportsRepository;
import com.agaram.eln.primary.repository.reports.reportviewer.ReportViewerStructureRepository;
import com.agaram.eln.primary.repository.reports.reportviewer.ReportsRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LSsamplefileRepository;
import com.agaram.eln.primary.service.cloudFileManip.CloudFileManipulationservice;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;

@Service
public class ViewerService {
	@Autowired
	private LSlogilablimsorderdetailRepository lslogilablimsorderdetailRepository;

	@Autowired
	private CloudOrderCreationRepository cloudOrderCreationRepository;

	@Autowired
	private ReportViewerStructureRepository reportViewerstructureRepository;

	@Autowired
	private ReportsRepository reportsRepository;

	@Autowired
	private CloudreportsRepository cloudreportsRepository;
	@Autowired
	private LSsamplefileRepository lssamplefileRepository;

	@Autowired
	private LSlogilabprotocoldetailRepository lslogilabprotocoldetailRepository;
	@Autowired
	private CloudFileManipulationservice objCloudFileManipulationservice;

	@Autowired
	private LSProtocolMasterRepository objLSProtocolMasterRepository;

	@Autowired
	private LSlogilabprotocolstepsRepository lslogilabprotocolstepsRepository;

	@Autowired
	private CloudLsLogilabprotocolstepInfoRepository cloudLsLogilabprotocolstepInfoRepository;

	@Autowired
	private LSprotocolorderimagesRepository lsprotocolorderimagesRepository;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private ReportfileRepository reportfileRepository;

	@SuppressWarnings("unused")
	public Map<String, Object> getreportdata(Reports report) throws URISyntaxException {
		Map<String, Object> rtnObj = new HashMap<>();
		if (report.getReporttemplate().getReporttype() == 1) {

			List<LSlogilablimsorderdetail> lstorder = lslogilablimsorderdetailRepository
					.findByCreatedtimestampBetweenOrderByBatchcodeDesc(report.getFromdate(), report.getTodate());

			if (lstorder != null && !lstorder.isEmpty()) {
				lstorder.parallelStream().filter(
						order -> order.getLssamplefile() != null && order.getLssamplefile().getFilesamplecode() != null)
//							.forEach(order -> setFileContentFromBlob(container, order));
						.forEach(order -> {
							try {
								setFileContentFromTable(order);
							} catch (URISyntaxException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						});
			}
			rtnObj.put("sheetorder", lstorder);
		} else if (report.getReporttemplate().getReporttype() == 2) {

			List<Integer> testid = report.getLstestmasterlocal().stream().map(LStestmasterlocal::getTestcode)
					.collect(Collectors.toList());

			String json = report.getReporttemplatecontent().toString();

			JsonParser jsonParser = new JsonParser();
			JsonObject jsonObject = jsonParser.parse(json).getAsJsonObject();

			JsonArray protocolArray = jsonObject.getAsJsonArray("protocol");
			JsonArray treeselected = jsonObject.getAsJsonArray("content");

			List<String> protocolList = new ArrayList<>();
			if (protocolArray != null) {
				for (int i = 0; i < protocolArray.size(); i++) {
					String protocolName = protocolArray.get(i).getAsString();
					protocolList.add(protocolName);
				}
			}

			List<String> treeList = new ArrayList<>();
			if (treeselected != null) {
				for (int i = 0; i < treeselected.size(); i++) {
					String treeselecte = treeselected.get(i).getAsString();
					treeList.add(treeselecte);
				}
			}

			List<LSprotocolmaster> lstpm = objLSProtocolMasterRepository.findByprotocolmasternameIn(protocolList);
//			List<Integer> lstpmc = lstpm.stream().map(LSprotocolmaster::getProtocolmastercode).collect(Collectors.toList());

			List<LSlogilabprotocoldetail> lstorder = lslogilabprotocoldetailRepository
					.findByLsprotocolmasterInAndTestcodeIn(lstpm, testid);

			List<Long> lstpmc = lstorder.stream().map(LSlogilabprotocoldetail::getProtocolordercode)
					.collect(Collectors.toList());
			ArrayList<String> arrayList = new ArrayList<>();
			if (lstorder != null && !lstorder.isEmpty()) {
				lstorder.parallelStream().forEach(order -> {
					if (order.getFileuid() != null && order.getFileuri() != null) {
						setProtocolFileContentFromBlob(order, rtnObj, arrayList);
					} else {
						List<LSlogilabprotocolsteps> LSprotocolsteplst = new ArrayList<LSlogilabprotocolsteps>();

						LSprotocolsteplst = lslogilabprotocolstepsRepository.findByProtocolordercodeIn(lstpmc);

						List<LSlogilabprotocolsteps> LSprotocolstepLst = new ArrayList<LSlogilabprotocolsteps>();

						if (!LSprotocolsteplst.isEmpty()) {
							for (LSlogilabprotocolsteps LSprotocolstepObj1 : LSprotocolsteplst) {

								if (report.getIsmultitenant() == 1) {

									CloudLsLogilabprotocolstepInfo newLSprotocolstepInfo = cloudLsLogilabprotocolstepInfoRepository
											.findById(LSprotocolstepObj1.getProtocolorderstepcode());
									List<LSprotocolorderimages> lsprotocolorderimages = lsprotocolorderimagesRepository
											.findByProtocolorderstepcode(LSprotocolstepObj1.getProtocolorderstepcode());
									if (newLSprotocolstepInfo != null) {
										LSprotocolstepObj1
												.setLsprotocolstepInfo(newLSprotocolstepInfo.getLsprotocolstepInfo());
									}
									if (lsprotocolorderimages.size() != 0) {
										LSprotocolstepObj1.setLsprotocolorderimages(lsprotocolorderimages);
									}
								} else {

									LsLogilabprotocolstepInfo newLSprotocolstepInfo = mongoTemplate.findById(
											LSprotocolstepObj1.getProtocolorderstepcode(),
											LsLogilabprotocolstepInfo.class);
									if (newLSprotocolstepInfo != null) {
										LSprotocolstepObj1.setLsprotocolstepInfo(newLSprotocolstepInfo.getContent());
									}
								}
								LSprotocolstepLst.add(LSprotocolstepObj1);

							}
						}

						if (LSprotocolsteplst != null) {
							rtnObj.put("protocolstepLst", LSprotocolstepLst);
//							rtnObj.put("countforstep", countforstep);
						} else {
							rtnObj.put("protocolstepLst", new ArrayList<>());
						}
					}
				});

			}

			rtnObj.put("selected", treeList);
			rtnObj.put("lstprotocolordertempl", lstorder);

		}

		return rtnObj;

	}

	private Object setFileContentFromTable(LSlogilablimsorderdetail order) throws URISyntaxException {

		List<Integer> sampleFileCodeList1 = lssamplefileRepository.getFilesamplecodeByBatchcodeIn(order.getBatchcode());
		List<lsreportfile> file = reportfileRepository.getReportFile(sampleFileCodeList1);
		if (!file.isEmpty() && file.get(0) != null) {
			if (file.get(0).getContent() != null) {
				order.getLssamplefile().setFilecontent(file.get(0).getContent());
			}
		}

		return null;
	}

	private Object setProtocolFileContentFromBlob(LSlogilabprotocoldetail order, Map<String, Object> rtnObj,
			ArrayList<String> arrayList) {

		try {
			arrayList.add(objCloudFileManipulationservice.retrieveCloudSheets(order.getFileuid(),
					TenantContext.getCurrentTenant() + "protocolorder"));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rtnObj.put("protocolData", arrayList);
	}

//	private Object setProtocolFileContentFromBlob(CloudBlobContainer container, LSlogilabprotocoldetail order) {
//		ArrayList<String> arrayList = new ArrayList<>();
//		if (order.getFileuid() != null) {
//			arrayList.add(objCloudFileManipulationservice.retrieveCloudSheets(order.getFileuid(),
//					TenantContext.getCurrentTenant() + "protocol"));
//			rtnObj.put("ProtocolData", arrayList);
//		}
//		return arrayList;
//	}

	@SuppressWarnings("unused")
	private void setFileContentFromBlob(CloudBlobContainer container, LSlogilablimsorderdetail order) {
		List<Integer> sampleFileCodeList1 = lssamplefileRepository.getFilesamplecodeByBatchcodeIn(order.getBatchcode());
		List<CloudOrderCreationRepository> fileuuid = cloudOrderCreationRepository.getFileuid(sampleFileCodeList1);
		if (!fileuuid.isEmpty() && fileuuid.get(0) != null) {
			try {
				CloudBlockBlob blob = container.getBlockBlobReference(fileuuid.get(0).toString());
				String orderText = blob.downloadText();
				if (orderText != null) {
					order.getLssamplefile().setFilecontent(orderText);
				}
			} catch (URISyntaxException | StorageException | IOException e) {
				e.printStackTrace();
			}
		}
	}

	public String getordercontent(LSsamplefile lssample) {
		if (lssample != null && lssample.getFilesamplecode() != null) {
			CloudOrderCreation file = cloudOrderCreationRepository.findById((long) lssample.getFilesamplecode());
			if (file != null) {
				return file.getContent();
			}
		}
		return "";
	}

	public Map<String, Object> getremainingorderdata(LSlogilablimsorderdetail objorder) {
		Map<String, Object> rtnObj = new HashMap<String, Object>();

		List<LSlogilablimsorderdetail> lstorder = new ArrayList<LSlogilablimsorderdetail>();
		lstorder = lslogilablimsorderdetailRepository
				.findFirst2ByBatchcodeLessThanAndCreatedtimestampBetweenOrderByBatchcodeDesc(objorder.getBatchcode(),
						objorder.getFromdate(), objorder.getTodate());
		if (lstorder != null && lstorder.size() > 0) {
			lstorder.forEach((order) -> {
				if (order.getLssamplefile() != null && order.getLssamplefile().getFilesamplecode() != null) {
					CloudOrderCreation file = cloudOrderCreationRepository
							.findById((long) order.getLssamplefile().getFilesamplecode());
					if (file != null) {
						order.getLssamplefile().setFilecontent(file.getContent());
					}
				}

			});
		}
		rtnObj.put("sheetorder", lstorder);

		return rtnObj;
	}

	public Map<String, Object> getfolders(ReportViewerStructure objdir) {
		Map<String, Object> rtnObj = new HashMap<String, Object>();
		List<ReportViewerStructure> lstdir = new ArrayList<ReportViewerStructure>();
		if (objdir.getLstuserMaster() != null && objdir.getLstuserMaster().size() == 0) {
			lstdir = reportViewerstructureRepository
					.findBySitemasterAndViewoptionOrCreatedbyAndViewoptionOrderByDirectorycode(
							objdir.getLsuserMaster().getLssitemaster(), 1, objdir.getLsuserMaster(), 2);
		} else {
			lstdir = reportViewerstructureRepository
					.findBySitemasterAndViewoptionOrCreatedbyAndViewoptionOrSitemasterAndViewoptionAndCreatedbyInOrderByDirectorycode(
							objdir.getLsuserMaster().getLssitemaster(), 1, objdir.getLsuserMaster(), 2,
							objdir.getLsuserMaster().getLssitemaster(), 3, objdir.getLstuserMaster());
		}
		rtnObj.put("directory", lstdir);
		return rtnObj;
	}

	public ReportViewerStructure insertdirectory(ReportViewerStructure objdir) {
		Response objResponse = new Response();
		ReportViewerStructure lstdir = null;
		if (objdir.getDirectorycode() != null) {
			lstdir = reportViewerstructureRepository.findByDirectorycodeAndParentdircodeAndDirectorynameNot(
					objdir.getDirectorycode(), objdir.getParentdircode(), objdir.getDirectoryname());
		} else {
			lstdir = reportViewerstructureRepository.findByParentdircodeAndDirectoryname(objdir.getParentdircode(),
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

	public ReportViewerStructure insertnewdirectory(ReportViewerStructure objdir) throws Exception {
		return reportViewerstructureRepository.save(objdir);
	}

	public Reports savereport(Reports report) throws ServletException, IOException {
		Cloudreports objcloudreport = new Cloudreports();
		objcloudreport.setReporttemplatecontent(report.getReporttemplatecontent());

		final Optional<Reports> templateByName = reportsRepository.findByReportnameIgnoreCase(report.getReportname());

		if (templateByName.isPresent()) {
			report.setResponse(new Response());
			report.getResponse().setStatus(true);
			report.getResponse().setInformation("ID_EXIST");

			return report;
		} else {

			reportsRepository.save(report);
			objcloudreport.setReportcode(report.getReportcode());
			cloudreportsRepository.save(objcloudreport);
		}
		return report;
	}

	public List<Reports> getreportsonfolder(ReportViewerStructure objReport) throws Exception {

		List<Reports> lstreports = new ArrayList<Reports>();
		if (objReport.getFilefor().equals("RDT") || objReport.getFilefor().equals("RAT")) {
			if (objReport.getLstuserMaster() == null) {
				objReport.setLstuserMaster(new ArrayList<LSuserMaster>());
				objReport.getLstuserMaster().add(objReport.getCreatedby());
			}

			lstreports = reportsRepository
					.findBySitemasterAndViewoptionAndTemplatetypeAndCreatedbyInAndDatecreatedBetweenOrSitemasterAndViewoptionAndTemplatetypeAndCreatedbyAndDatecreatedBetweenOrSitemasterAndViewoptionAndTemplatetypeAndCreatedbyInAndDatecreatedBetweenOrderByReportcodeDesc(
							objReport.getSitemaster(), 1, objReport.getTemplatetype(), objReport.getLstuserMaster(),
							objReport.getFromdate(), objReport.getTodate(), objReport.getSitemaster(), 2,
							objReport.getTemplatetype(), objReport.getCreatedby(), objReport.getFromdate(),
							objReport.getTodate(), objReport.getSitemaster(), 1, objReport.getTemplatetype(),
							objReport.getLstuserMaster(), objReport.getFromdate(), objReport.getTodate());
			
		} else if (objReport.getFilefor().equals("DR")) {
			lstreports = reportsRepository.findByReportviewerstructure(objReport);
		}

		return lstreports;
	}

	public Cloudreports getreportcontent(Reports report) throws Exception {
		return cloudreportsRepository.findOne(report.getReportcode());
	}
}
