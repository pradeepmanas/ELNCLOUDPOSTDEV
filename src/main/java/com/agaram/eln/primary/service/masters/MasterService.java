package com.agaram.eln.primary.service.masters;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.agaram.eln.primary.commonfunction.commonfunction;
import com.agaram.eln.primary.config.TenantContext;
import com.agaram.eln.primary.fetchmodel.archieve.ProjectArchieve;
import com.agaram.eln.primary.fetchmodel.getmasters.Repositorymaster;
import com.agaram.eln.primary.model.archieve.LsProjectarchieve;
import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.instrumentDetails.LSlogilablimsorderdetail;
import com.agaram.eln.primary.model.instrumentDetails.LsOrderSampleUpdate;
import com.agaram.eln.primary.model.masters.Lsrepositories;
import com.agaram.eln.primary.model.masters.Lsrepositoriesdata;
import com.agaram.eln.primary.model.protocols.LSlogilabprotocoldetail;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.usermanagement.LSnotification;
import com.agaram.eln.primary.model.usermanagement.LSprojectmaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.repository.archieve.LsProjectarchieveRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LSlogilablimsorderdetailRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LsOrderSampleUpdateRepository;
import com.agaram.eln.primary.repository.masters.LsrepositoriesRepository;
import com.agaram.eln.primary.repository.masters.LsrepositoriesdataRepository;
import com.agaram.eln.primary.repository.protocol.LSlogilabprotocoldetailRepository;
import com.agaram.eln.primary.repository.usermanagement.LSnotificationRepository;
import com.agaram.eln.primary.repository.usermanagement.LSprojectmasterRepository;
import com.agaram.eln.primary.service.instrumentDetails.InstrumentService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.OperationContext;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.BlobContainerPublicAccessType;
import com.microsoft.azure.storage.blob.BlobRequestOptions;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;

@Service
@EnableJpaRepositories(basePackageClasses = LsrepositoriesRepository.class)
public class MasterService {

	static final Logger logger = Logger.getLogger(InstrumentService.class.getName());
	@Autowired
	private LsrepositoriesRepository lsrepositoriesRepository;

	@Autowired
	private LsrepositoriesdataRepository lsrepositoriesdataRepository;

	@Autowired
	private LsOrderSampleUpdateRepository LsOrderSampleUpdateRepository;

	@Autowired
	private LSnotificationRepository lsnotificationRepository;
	
	@Autowired
	private LSlogilablimsorderdetailRepository lslogilablimsorderdetailRepository;
	
	@Autowired
	private LsProjectarchieveRepository LsProjectarchieveRepository;
	
	@Autowired
	private LSlogilabprotocoldetailRepository lslogilabprotocoldetailRepository;
	
	@Autowired
	private LSprojectmasterRepository lsprojectmasterRepository;
	
	@Autowired
    private Environment env;

	public List<Lsrepositories> Getallrepositories(Lsrepositories lsrepositories) {
		return lsrepositoriesRepository.
				findBySitecodeOrderByRepositorycodeAsc
				(lsrepositories.getSitecode());
	}
	
	public List<Lsrepositories> Getallrepositoriesondashboard(Lsrepositories lsrepositories) {
		return lsrepositoriesRepository.
				findBysitecodeAndAddedonBetweenOrderByRepositorycodeAsc
				(lsrepositories.getSitecode(),lsrepositories.getFromdate(),lsrepositories.getTodate());
	}

	public Lsrepositories Saverepository(Lsrepositories lsrepositories) {
		Response objResponse = new Response();
		Lsrepositories objrepo = null;
		if (lsrepositories.getRepositorycode() != null) {
			objrepo = lsrepositoriesRepository.findByRepositorynameAndSitecodeAndRepositorycodeNot(
					lsrepositories.getRepositoryname(), lsrepositories.getSitecode(),
					lsrepositories.getRepositorycode());
		} else {
			objrepo = lsrepositoriesRepository.findByRepositorynameAndSitecode(lsrepositories.getRepositoryname(),
					lsrepositories.getSitecode());
		}

		if (objrepo != null) {
			objResponse.setStatus(false);
			objResponse.setInformation("Repository already exists");
		} else {
			objResponse.setStatus(true);
			lsrepositoriesRepository.save(lsrepositories);
		}

		lsrepositories.setObjResponse(objResponse);
		return lsrepositories;
	}

	public List<Lsrepositoriesdata> Getallrepositoriesdata(Lsrepositoriesdata lsrepositoriesdata) {
		return lsrepositoriesdataRepository.findByRepositorycodeAndSitecodeAndItemstatusOrderByRepositorydatacodeDesc(
				lsrepositoriesdata.getRepositorycode(), lsrepositoriesdata.getSitecode(), 1);
	}

	public Lsrepositoriesdata Saverepositorydata(Lsrepositoriesdata lsrepositoriesdata) {
		Response objResponse = new Response();
		@SuppressWarnings("unused")
		Lsrepositoriesdata lsrepodata = null;

		if (lsrepositoriesdata.getRepositorydatacode() != null) {
			lsrepodata = lsrepositoriesdataRepository
					.findByRepositorycodeAndRepositoryitemnameAndSitecodeAndRepositorydatacodeNot(
							lsrepositoriesdata.getRepositorycode(), lsrepositoriesdata.getRepositoryitemname(),
							lsrepositoriesdata.getSitecode(), lsrepositoriesdata.getRepositorydatacode());
		} else {
			lsrepodata = lsrepositoriesdataRepository.findByRepositorycodeAndRepositoryitemnameAndSitecode(
					lsrepositoriesdata.getRepositorycode(), lsrepositoriesdata.getRepositoryitemname(),
					lsrepositoriesdata.getSitecode());

		}

//		if(lsrepodata != null)
//		{
//			objResponse.setStatus(false);
//			objResponse.setInformation("Iteam name already exists in the repositroy");
//		}
//		else
//		{
		objResponse.setStatus(true);
		lsrepositoriesdataRepository.save(lsrepositoriesdata);
//		}

		lsrepositoriesdata.setObjResponse(objResponse);
		return lsrepositoriesdata;
	}

	public Lsrepositoriesdata GetupdatedRepositorydata(Lsrepositoriesdata lsrepositoriesdata) {
		lsrepositoriesdata = lsrepositoriesdataRepository.findOne(lsrepositoriesdata.getRepositorydatacode());
		return lsrepositoriesdata;
	}

	public Lsrepositoriesdata DeleteRepositorydata(Lsrepositoriesdata lsrepositoriesdata) {
		lsrepositoriesdata = lsrepositoriesdataRepository.findOne(lsrepositoriesdata.getRepositorydatacode());
		lsrepositoriesdata.setItemstatus(0);
		lsrepositoriesdataRepository.save(lsrepositoriesdata);

		return lsrepositoriesdata;
	}

	public List<LsOrderSampleUpdate> getinventoryhistory(LsOrderSampleUpdate lsordersamplUpdate) {
//		List<LsOrderSampleUpdate>	lsordersamplUpdateobj =LsOrderSampleUpdateRepository.findByRepositorycodeAndRepositorydatacodeAndQuantityusedNotAndHistorydetailsNotNull(lsordersamplUpdate.getRepositorycode(),lsordersamplUpdate.getRepositorydatacode(),0);
		List<LsOrderSampleUpdate> lsordersamplUpdateobj = LsOrderSampleUpdateRepository
				.findByRepositorycodeAndRepositorydatacodeAndQuantityusedNotAndHistorydetailsNotNull(
						lsordersamplUpdate.getRepositorycode(), lsordersamplUpdate.getRepositorydatacode(), 0);

		return lsordersamplUpdateobj;
	}

	public Response pushnotificationforinventory(List<Lsrepositoriesdata> lsrepositoriesdata) {

		try {
			String Details = "";
			String Notifiction = "";
			LSnotification obj = new LSnotification();
			Response Response = new Response();
			List<LSnotification> lstnotifications = new ArrayList<LSnotification>();
			for (int i = 0; i < lsrepositoriesdata.size(); i++) {
				Details = "{\"repositorycode\":\"" + lsrepositoriesdata.get(i).getRepositorycode()
						+ "\", \"repositorydatacode\":\"" + lsrepositoriesdata.get(i).getRepositorydatacode()
						+ "\", \"usercode\":\"" + lsrepositoriesdata.get(i).getUsercode()
						+ "\", \"repositoryitemname\":\"" + lsrepositoriesdata.get(i).getRepositoryitemname()
						+ "\", \"repositoryuniqueid\":\"" + lsrepositoriesdata.get(i).getRepositoryuniqueid()
						+ "\", \"expireddatecount\":\"" + lsrepositoriesdata.get(i).getExpireddatecount() + "\"}";
				obj = lsnotificationRepository.findByRepositorycodeAndRepositorydatacodeAndNotificationdetils(
						lsrepositoriesdata.get(i).getRepositorycode(),
						lsrepositoriesdata.get(i).getRepositorydatacode(),Details);
				if (lsrepositoriesdata.get(i).getRepositorycode() != null
						&& lsrepositoriesdata.get(i).getRepositorydatacode() != null && obj == null) {

					Notifiction = "EXPIREDINVENTORY";

					

					Date date = new Date();
					LSuserMaster LSuserMaster = new LSuserMaster();
					LSuserMaster.setUsercode(lsrepositoriesdata.get(i).getUsercode());
					LSnotification objnotify = new LSnotification();

					objnotify.setNotifationfrom(LSuserMaster);
					objnotify.setNotifationto(LSuserMaster);
					objnotify.setNotificationdate(date);
					objnotify.setNotification(Notifiction);
					objnotify.setNotificationdetils(Details);
					objnotify.setIsnewnotification(1);
					objnotify.setNotificationpath("/masters");
					objnotify.setRepositorycode(lsrepositoriesdata.get(i).getRepositorycode());
					objnotify.setRepositorydatacode(lsrepositoriesdata.get(i).getRepositorydatacode());

					lstnotifications.add(objnotify);
				}

			}
			if (!lstnotifications.isEmpty()) {
				lsnotificationRepository.save(lstnotifications);
				Response.setInformation("Successfully added");
				Response.setStatus(true);
			}

		}

		catch (Exception e) {
			logger.error("updatenotificationfororder : " + e.getMessage());
		}
		return null;
	}

	public Map<String, Object> getrepositoryfields(Lsrepositories repositorymaster) {
		
		Lsrepositories repository = lsrepositoriesRepository.findByRepositorycode(repositorymaster.getRepositorycode());
		
		Map<String, Object> rMap = new HashMap<>();
		
		String jsonFieldstring = repository.getRepositoryfields();
		
		String jsonFieldReturnstring = commonfunction.getJSONFieldsoninventory(jsonFieldstring);
		
		Repositorymaster repomaster = lsrepositoriesRepository.findByrepositorycode(repositorymaster.getRepositorycode());
		
		repomaster.setJsonFieldString(jsonFieldReturnstring);
		
		rMap.put("inventoryMaster",repomaster);
		
		rMap.put("repositoryData",lsrepositoriesdataRepository.findByRepositorycodeAndItemstatusOrderByRepositorydatacodeDesc(
				repositorymaster.getRepositorycode(), 1));
		
		return rMap;
	}

	public List<Lsrepositoriesdata> GetrepositoriesdataonFilter(Lsrepositoriesdata lsrepositoriesdata) {
		
		return lsrepositoriesdataRepository.findByRepositorycodeAndItemstatusAndAddedonBetweenOrderByRepositorydatacodeDesc(
				lsrepositoriesdata.getRepositorycode(), 1,lsrepositoriesdata.getFromdate(),lsrepositoriesdata.getTodate());
	}
	
	@Transactional
	public LsProjectarchieve archiveproject(LSprojectmaster lsprojectmaster)
	{
		CloudStorageAccount storageAccount;
		CloudBlobClient blobClient = null;
		CloudBlobContainer container=null;
		String storageConnectionString = env.getProperty("azure.storage.ConnectionString");

		LsProjectarchieve objarchieve = new LsProjectarchieve();
		Response objresponse = new Response();
		objresponse.setStatus(true);
		objarchieve.setResponse(objresponse);
		UUID objGUID = UUID.randomUUID();
        String randomUUIDString = objGUID.toString();
		Gson gson = new GsonBuilder().create();
		try {
			
			// Parse the connection string and create a blob client to interact with Blob storage
			storageAccount = CloudStorageAccount.parse(storageConnectionString);
			blobClient = storageAccount.createCloudBlobClient();
			container = blobClient.getContainerReference(TenantContext.getCurrentTenant()+"projectarchieve");

			container.createIfNotExists(BlobContainerPublicAccessType.CONTAINER, new BlobRequestOptions(), new OperationContext());		    

			Writer writer = new FileWriter(System.getProperty("java.io.tmpdir")+"/"+randomUUIDString+".json");
			
			ProjectArchieve archieveproject = Getprojectdetails(lsprojectmaster);
			
	        gson.toJson(archieveproject, writer);
	        writer.flush(); //flush data to file   <---
	        writer.close(); 
			
			//Getting a blob reference
			CloudBlockBlob blob = container.getBlockBlobReference(randomUUIDString+".json");

			blob.uploadFromFile(System.getProperty("java.io.tmpdir")+"/"+randomUUIDString+".json");
			
			lslogilablimsorderdetailRepository.deleteByLsprojectmaster(lsprojectmaster);
			lslogilabprotocoldetailRepository.deleteByLsprojectmaster(lsprojectmaster);
			lsprojectmasterRepository.delete(lsprojectmaster.getProjectcode());
			
			objarchieve.setArchieveby(lsprojectmaster.getLsusermaster());
			objarchieve.setFilenameuuid(randomUUIDString+".json");
			objarchieve.setModifieddate(lsprojectmaster.getObjuser().getTodate());
			objarchieve.setProjectname(lsprojectmaster.getProjectname());
			objarchieve.setLssitemaster(lsprojectmaster.getLsusermaster().getLssitemaster());
			
			LsProjectarchieveRepository.save(objarchieve);
		} 
		catch (StorageException ex)
		{
			System.out.println(String.format("Error returned from the service. Http code: %d and error code: %s", ex.getHttpStatusCode(), ex.getErrorCode()));
			objresponse.setStatus(false);
			objarchieve.setResponse(objresponse);
		}
		catch (Exception ex) 
		{
			System.out.println(ex.getMessage());
			objresponse.setStatus(false);
			objarchieve.setResponse(objresponse);
		}
		finally 
		{
			System.out.println("The program has completed successfully.");
			System.out.println("Press the 'Enter' key while in the console to delete the sample files, example container, and exit the application.");

		
		}
		
		return objarchieve;
	}
	
	
	public ProjectArchieve Getprojectdetails(LSprojectmaster lsprojectmaster)
	{
		ProjectArchieve objProjectArchieve = new ProjectArchieve();
		
		objProjectArchieve.setLsprojectmaster(lsprojectmasterRepository.findOne(lsprojectmaster.getProjectcode()));
		
		objProjectArchieve.setLstlslogilablimsorderdetail(lslogilablimsorderdetailRepository.findByLsprojectmasterOrderByBatchcodeDesc(lsprojectmaster));
		
		objProjectArchieve.setLstlslogilabprotocoldetail(lslogilabprotocoldetailRepository.findByLsprojectmasterOrderByProtocolordercodeDesc(lsprojectmaster));
		
		return objProjectArchieve;
	}
	
	@Transactional
	public LsProjectarchieve Importprojectarchieve(LsProjectarchieve lsprojectarchieve) throws IOException
	{
		
		Gson gson = new GsonBuilder().create();
		 CloudStorageAccount storageAccount;
			CloudBlobClient blobClient = null;
			CloudBlobContainer container=null;
			CloudBlockBlob blob = null;
			Response objresponse = new Response();
			objresponse.setStatus(true);
			lsprojectarchieve.setResponse(objresponse);
			
			String storageConnectionString = env.getProperty("azure.storage.ConnectionString");
			try {    
				// Parse the connection string and create a blob client to interact with Blob storage
				storageAccount = CloudStorageAccount.parse(storageConnectionString);
				blobClient = storageAccount.createCloudBlobClient();
				container = blobClient.getContainerReference(TenantContext.getCurrentTenant()+"projectarchieve");
				
				blob = container.getBlockBlobReference(lsprojectarchieve.getFilenameuuid());
				blob.openInputStream();
				
				String result = IOUtils.toString(blob.openInputStream(), StandardCharsets.UTF_8);
				
				ProjectArchieve objproject = new ProjectArchieve();
				
				objproject = gson.fromJson(result, objproject.getClass());

				LSprojectmaster objprojetc = lsprojectmasterRepository.save(objproject.getLsprojectmaster());
				List<LSlogilablimsorderdetail> lstsheetorders = objproject.getLstlslogilablimsorderdetail();
				if(lstsheetorders != null && lstsheetorders.size() >0)
				{
					lstsheetorders.forEach(objorder -> objorder.setLsprojectmaster(objprojetc));
					lslogilablimsorderdetailRepository.save(lstsheetorders);
				}
				List<LSlogilabprotocoldetail> lstprotocolorders = objproject.getLstlslogilabprotocoldetail();
				if(lstprotocolorders != null && lstprotocolorders.size() >0)
				{
					lstprotocolorders.forEach(objorder -> objorder.setLsprojectmaster(objprojetc));
					lslogilabprotocoldetailRepository.save(lstprotocolorders);
				}
				
				LsProjectarchieveRepository.delete(lsprojectarchieve.getProjectarchievecode());
				System.out.println(objproject.getLsprojectmaster().getProjectname());
				
			}
			catch (StorageException ex)
			{
				System.out.println(String.format("Error returned from the service. Http code: %d and error code: %s", ex.getHttpStatusCode(), ex.getErrorCode()));
				objresponse.setStatus(false);
				lsprojectarchieve.setResponse(objresponse);
				throw new IOException(String.format("Error returned from the service. Http code: %d and error code: %s", ex.getHttpStatusCode(), ex.getErrorCode()));
			}
			catch (Exception ex) 
			{
				System.out.println(ex.getMessage());
				objresponse.setStatus(false);
				lsprojectarchieve.setResponse(objresponse);
			}
		
		return lsprojectarchieve;
	}
	
	public List<LsProjectarchieve> GetArchievedprojectsonsite(LSSiteMaster lssitemaster)
	{
		return LsProjectarchieveRepository.findByLssitemasterOrderByProjectarchievecodeDesc(lssitemaster);
	}
	
	public InputStream Downloadarchievedproject(LsProjectarchieve lsprojectarchieve) throws IOException
	{
		 CloudStorageAccount storageAccount;
			CloudBlobClient blobClient = null;
			CloudBlobContainer container=null;
			CloudBlockBlob blob = null;
			String storageConnectionString = env.getProperty("azure.storage.ConnectionString");
			try {    
				// Parse the connection string and create a blob client to interact with Blob storage
				storageAccount = CloudStorageAccount.parse(storageConnectionString);
				blobClient = storageAccount.createCloudBlobClient();
				container = blobClient.getContainerReference(TenantContext.getCurrentTenant()+"projectarchieve");
				
				blob = container.getBlockBlobReference(lsprojectarchieve.getFilenameuuid());
				return blob.openInputStream();
			}
			catch (StorageException ex)
			{
				System.out.println(String.format("Error returned from the service. Http code: %d and error code: %s", ex.getHttpStatusCode(), ex.getErrorCode()));
				throw new IOException(String.format("Error returned from the service. Http code: %d and error code: %s", ex.getHttpStatusCode(), ex.getErrorCode()));
			}
			catch (Exception ex) 
			{
				System.out.println(ex.getMessage());
			}
			return null;
	}
}
