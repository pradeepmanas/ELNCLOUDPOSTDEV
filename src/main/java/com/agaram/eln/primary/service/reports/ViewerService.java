package com.agaram.eln.primary.service.reports;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties.Env;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.agaram.eln.primary.config.TenantContext;
import com.agaram.eln.primary.model.cloudFileManip.CloudOrderCreation;
import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.instrumentDetails.LSlogilablimsorderdetail;
import com.agaram.eln.primary.model.reports.reportdesigner.Reporttemplate;
import com.agaram.eln.primary.model.reports.reportviewer.Cloudreports;
import com.agaram.eln.primary.model.reports.reportviewer.ReportViewerStructure;
import com.agaram.eln.primary.model.reports.reportviewer.Reports;
import com.agaram.eln.primary.model.sheetManipulation.LSsamplefile;
import com.agaram.eln.primary.model.usermanagement.LSprojectmaster;
import com.agaram.eln.primary.repository.cloudFileManip.CloudOrderCreationRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LSlogilablimsorderdetailRepository;
import com.agaram.eln.primary.repository.reports.reportviewer.CloudreportsRepository;
import com.agaram.eln.primary.repository.reports.reportviewer.ReportViewerStructureRepository;
import com.agaram.eln.primary.repository.reports.reportviewer.ReportsRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LSsamplefileRepository;
import com.agaram.eln.primary.repository.usermanagement.LSprojectmasterRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlob;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.microsoft.azure.storage.blob.ListBlobItem;

@Service
public class ViewerService {
	@Autowired
	private LSlogilablimsorderdetailRepository lslogilablimsorderdetailRepository;
	
	@Autowired
	private CloudOrderCreationRepository cloudOrderCreationRepository;

	@Autowired
	private LSprojectmasterRepository lsprojectmasterRepository;
	
	@Autowired
	private ReportViewerStructureRepository reportViewerstructureRepository;
	
	@Autowired
	private ReportsRepository reportsRepository;
	
	@Autowired
	private CloudreportsRepository cloudreportsRepository;
	@Autowired
	private LSsamplefileRepository lssamplefileRepository;
	@Autowired
	private Environment env;
	
	public Map<String, Object> getreportdata(Reports report)
	{
		Map<String, Object> rtnObj = new HashMap<>();
		List<LSlogilablimsorderdetail> lstorder = lslogilablimsorderdetailRepository.findByCreatedtimestampBetweenOrderByBatchcodeDesc(
		        report.getFromdate(), report.getTodate());

		String connectionString =env.getProperty("azure.storage.ConnectionString");
		CloudStorageAccount storageAccount = null;
		try {
		    storageAccount = CloudStorageAccount.parse(connectionString);
		    CloudBlobClient blobClient = storageAccount.createCloudBlobClient();
		    CloudBlobContainer container = blobClient.getContainerReference(TenantContext.getCurrentTenant() + "ordercreation");

		    if (lstorder != null && !lstorder.isEmpty()) {
		        lstorder.parallelStream()
		                .filter(order -> order.getLssamplefile() != null && order.getLssamplefile().getFilesamplecode() != null)
		                .forEach(order -> setFileContentFromBlob(container, order));
		    }
		} catch (InvalidKeyException | URISyntaxException | StorageException e) {
		    e.printStackTrace();
		}

		rtnObj.put("sheetorder", lstorder);
		return rtnObj;

	}
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

	
	public String getordercontent(LSsamplefile lssample)
	{
		if(lssample !=null&& lssample.getFilesamplecode() != null)
		{
			CloudOrderCreation file = cloudOrderCreationRepository
					.findById((long) lssample.getFilesamplecode());
			if (file != null) {
				return file.getContent();
			}
		}
		return "";
	}
	
	public Map<String, Object> getremainingorderdata(LSlogilablimsorderdetail objorder)
	{
		Map<String, Object> rtnObj = new HashMap<String, Object>();
		
		List<LSlogilablimsorderdetail> lstorder = new ArrayList<LSlogilablimsorderdetail>();
		lstorder = lslogilablimsorderdetailRepository.findFirst2ByBatchcodeLessThanAndCreatedtimestampBetweenOrderByBatchcodeDesc(
				objorder.getBatchcode(), objorder.getFromdate(),objorder.getTodate());
		if(lstorder != null && lstorder.size()>0)
		{
			lstorder.forEach((order) -> {
				if(order.getLssamplefile()!=null&& order.getLssamplefile().getFilesamplecode() != null)
				{
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
	
	public Map<String, Object> getfolders(ReportViewerStructure objdir)
	{
		Map<String, Object> rtnObj = new HashMap<String, Object>();
		List<ReportViewerStructure> lstdir = new ArrayList<ReportViewerStructure>();
		if (objdir.getLstuserMaster().size() == 0) {
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
	
	public ReportViewerStructure insertdirectory( ReportViewerStructure objdir)
	{
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
	
	public ReportViewerStructure insertnewdirectory(ReportViewerStructure objdir)throws Exception {
		return reportViewerstructureRepository.save(objdir);
	}
	
	public Reports savereport(Reports report) throws ServletException, IOException {
		Cloudreports objcloudreport = new Cloudreports();
		objcloudreport.setReporttemplatecontent(report.getReporttemplatecontent());
		
		 final Optional<Reports> templateByName = reportsRepository
 				 .findByReportnameIgnoreCase(report.getReportname());
		
		 if(templateByName.isPresent()) {
			 report.setResponse(new Response());
			 report.getResponse().setStatus(true);
			 report.getResponse().setInformation("ID_EXIST");
			 
			   return report;
		 }
		 else {
		
		reportsRepository.save(report);
		objcloudreport.setReportcode(report.getReportcode());
		cloudreportsRepository.save(objcloudreport);
		 }
		return report;
	}
	
	public List<Reports> getreportsonfolder(ReportViewerStructure reportstructure)throws Exception {
		List<Reports> lstreports = new ArrayList<Reports>();
		lstreports = reportsRepository.findByReportviewerstructure(reportstructure);
		return lstreports;
	}
	
	public Cloudreports getreportcontent(Reports report) throws Exception {
		return cloudreportsRepository.findOne(report.getReportcode());
	}
}
