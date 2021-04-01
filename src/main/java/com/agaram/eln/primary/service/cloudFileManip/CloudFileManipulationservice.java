package com.agaram.eln.primary.service.cloudFileManip;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.agaram.eln.primary.config.TenantContext;
import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.cloudFileManip.CloudOrderAttachment;
import com.agaram.eln.primary.model.cloudFileManip.CloudProfilePicture;
import com.agaram.eln.primary.model.instrumentDetails.LsOrderattachments;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.repository.cfr.LScfttransactionRepository;
import com.agaram.eln.primary.repository.cloudFileManip.CloudOrderAttachmentRepository;
import com.agaram.eln.primary.repository.cloudFileManip.CloudProfilePictureRepository;
import com.agaram.eln.primary.repository.usermanagement.LSuserMasterRepository;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.OperationContext;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.BlobContainerPublicAccessType;
import com.microsoft.azure.storage.blob.BlobRequestOptions;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;

@Service
@Transactional 
public class CloudFileManipulationservice {
	
	@Autowired
	private CloudOrderAttachmentRepository cloudOrderAttachmentRepository;
 	
	@Autowired
    private CloudProfilePictureRepository cloudProfilePictureRepository;

	@Autowired
	private LScfttransactionRepository lscfttransactionRepository;
 	
	@Autowired
    private LSuserMasterRepository lsuserMasterRepository;
	
	final String storageConnectionString = "DefaultEndpointsProtocol=https;AccountName=lleln;AccountKey=0ShoGnoRZFQ6ozTyv65CRRaaywEA/4d2LJRWznrwMn1+di5ExZ3BjovCC8nZpnyCnXvTpFmgkIy19atmAM7wnQ==;EndpointSuffix=core.windows.net";
    
	public CloudProfilePicture addPhoto(Integer usercode, MultipartFile file,Date currentdate) throws IOException { 
    	
    	LSuserMaster username=lsuserMasterRepository.findByusercode(usercode);
    	String name=username.getUsername();
       	LScfttransaction list= new LScfttransaction();
    	list.setModuleName("UserManagement");
    	list.setComments(name+" "+"Uploaded the profile picture successfully");
    	list.setActions("View / Load");
    	list.setSystemcoments("System Generated");
    	list.setTableName("profile");
    	list.setTransactiondate(currentdate);
    	list.setLsuserMaster(usercode);
		lscfttransactionRepository.save(list);
		deletePhoto(usercode,list);
		
		CloudProfilePicture profile = new CloudProfilePicture(); 
    	profile.setId(usercode);
    	profile.setName(file.getName());
    	profile.setImage(
          new Binary(BsonBinarySubType.BINARY, file.getBytes())); 
    	profile = cloudProfilePictureRepository.save(profile); 
 
    	return profile; 
    }
	
	public CloudProfilePicture getPhoto(Integer id) { 
		   
        return cloudProfilePictureRepository.findById(id); 
    }
    
    public Long deletePhoto(Integer id,LScfttransaction list) { 
    	list.setTableName("ProfilePicture");
    	lscfttransactionRepository.save(list);
        return cloudProfilePictureRepository.deleteById(id); 
    }
    
    public CloudOrderAttachment storeattachment(MultipartFile file) throws IOException
    {
    	CloudOrderAttachment objattachment = new CloudOrderAttachment();
    	objattachment.setFile(
          new Binary(BsonBinarySubType.BINARY, file.getBytes()));
    	
    	objattachment = cloudOrderAttachmentRepository.save(objattachment);
    	
    	return objattachment;
    }
    
    public String storeLargeattachment(String title, MultipartFile file) throws IOException { 
       
    	String bloburi="";
		CloudStorageAccount storageAccount;
		CloudBlobClient blobClient = null;
		CloudBlobContainer container=null;

		try {    
			// Parse the connection string and create a blob client to interact with Blob storage
			storageAccount = CloudStorageAccount.parse(storageConnectionString);
			blobClient = storageAccount.createCloudBlobClient();
			container = blobClient.getContainerReference(TenantContext.getCurrentTenant());

			// Create the container if it does not exist with public access.
			System.out.println("Creating container: " + container.getName());
			container.createIfNotExists(BlobContainerPublicAccessType.CONTAINER, new BlobRequestOptions(), new OperationContext());		    

			File convFile = new File(System.getProperty("java.io.tmpdir")+"/"+title);
			file.transferTo(convFile);

			//Getting a blob reference
			CloudBlockBlob blob = container.getBlockBlobReference(convFile.getName());

			//Creating blob and uploading file to it
			System.out.println("Uploading the sample file ");
			blob.uploadFromFile(convFile.getAbsolutePath());
			
			bloburi = blob.getName();

		} 
		catch (StorageException ex)
		{
			System.out.println(String.format("Error returned from the service. Http code: %d and error code: %s", ex.getHttpStatusCode(), ex.getErrorCode()));
		}
		catch (Exception ex) 
		{
			System.out.println(ex.getMessage());
		}
		finally 
		{
			System.out.println("The program has completed successfully.");
			System.out.println("Press the 'Enter' key while in the console to delete the sample files, example container, and exit the application.");

		
		}
       
        return bloburi; //id.toString(); 
    }
    
    public CloudOrderAttachment retrieveFile(LsOrderattachments objattachment){
    
    	CloudOrderAttachment objfile = cloudOrderAttachmentRepository.findById(Integer.parseInt(objattachment.getFileid()));
	
        return objfile;
	}
    
    public InputStream retrieveLargeFile(String fileid) throws IOException{
    	
	    CloudStorageAccount storageAccount;
		CloudBlobClient blobClient = null;
		CloudBlobContainer container=null;
		CloudBlockBlob blob = null;
		try {    
			// Parse the connection string and create a blob client to interact with Blob storage
			storageAccount = CloudStorageAccount.parse(storageConnectionString);
			blobClient = storageAccount.createCloudBlobClient();
			container = blobClient.getContainerReference(TenantContext.getCurrentTenant());
			
			blob = container.getBlockBlobReference(fileid);
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
    
    public Long deleteattachments(String id) { 
        return cloudOrderAttachmentRepository.deleteById(Integer.parseInt(id)); 
    }
    
    public void deletelargeattachments(String id) { 
    	CloudStorageAccount storageAccount;
		CloudBlobClient blobClient = null;
		CloudBlobContainer container=null;
		CloudBlockBlob blob = null;
		try {    
			// Parse the connection string and create a blob client to interact with Blob storage
			storageAccount = CloudStorageAccount.parse(storageConnectionString);
			blobClient = storageAccount.createCloudBlobClient();
			container = blobClient.getContainerReference(TenantContext.getCurrentTenant());
			
			blob = container.getBlockBlobReference(id);
			blob.deleteIfExists();
		}
		catch (StorageException ex)
		{
			System.out.println(String.format("Error returned from the service. Http code: %d and error code: %s", ex.getHttpStatusCode(), ex.getErrorCode()));
		}
		catch (Exception ex) 
		{
			System.out.println(ex.getMessage());
		}
    }
    
    

    public String storeReportFile(String title, File file) throws IOException { 
        
    	String bloburi="";
		CloudStorageAccount storageAccount;
		CloudBlobClient blobClient = null;
		CloudBlobContainer container=null;

		try {    
			// Parse the connection string and create a blob client to interact with Blob storage
			storageAccount = CloudStorageAccount.parse(storageConnectionString);
			blobClient = storageAccount.createCloudBlobClient();
			container = blobClient.getContainerReference(TenantContext.getCurrentTenant());

			// Create the container if it does not exist with public access.
			System.out.println("Creating container: " + container.getName());
			container.createIfNotExists(BlobContainerPublicAccessType.CONTAINER, new BlobRequestOptions(), new OperationContext());		    

//			File convFile = new File(System.getProperty("java.io.tmpdir")+"/"+title);
//			file.transferTo(convFile);

			
			//Getting a blob reference
			CloudBlockBlob blob = container.getBlockBlobReference(file.getName());

			//Creating blob and uploading file to it
			System.out.println("Uploading the sample file ");
			blob.uploadFromFile(file.getAbsolutePath());
			
			bloburi = blob.getName();

		} 
		catch (StorageException ex)
		{
			System.out.println(String.format("Error returned from the service. Http code: %d and error code: %s", ex.getHttpStatusCode(), ex.getErrorCode()));
		}
		catch (Exception ex) 
		{
			System.out.println(ex.getMessage());
		}
		finally 
		{
			System.out.println("The program has completed successfully.");
			System.out.println("Press the 'Enter' key while in the console to delete the sample files, example container, and exit the application.");

		
		}
       
        return bloburi; //id.toString(); 
    }
    
    public InputStream retrieveReportFiles(String fileid) throws IOException{
    	
	    CloudStorageAccount storageAccount;
		CloudBlobClient blobClient = null;
		CloudBlobContainer container=null;
		CloudBlockBlob blob = null;
		try {    
			// Parse the connection string and create a blob client to interact with Blob storage
			storageAccount = CloudStorageAccount.parse(storageConnectionString);
			blobClient = storageAccount.createCloudBlobClient();
			container = blobClient.getContainerReference(TenantContext.getCurrentTenant());
			
			blob = container.getBlockBlobReference(fileid);
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
    
    public void deleteReportFile(String id) { 
    	CloudStorageAccount storageAccount;
		CloudBlobClient blobClient = null;
		CloudBlobContainer container=null;
		CloudBlockBlob blob = null;
		try {    
			// Parse the connection string and create a blob client to interact with Blob storage
			storageAccount = CloudStorageAccount.parse(storageConnectionString);
			blobClient = storageAccount.createCloudBlobClient();
			container = blobClient.getContainerReference(TenantContext.getCurrentTenant());
			
			blob = container.getBlockBlobReference(id);
			blob.deleteIfExists();
		}
		catch (StorageException ex)
		{
			System.out.println(String.format("Error returned from the service. Http code: %d and error code: %s", ex.getHttpStatusCode(), ex.getErrorCode()));
		}
		catch (Exception ex) 
		{
			System.out.println(ex.getMessage());
		}
    }
}
