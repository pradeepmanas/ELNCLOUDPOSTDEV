package com.agaram.eln.primary.service.cloudFileManip;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.agaram.eln.primary.commonfunction.commonfunction;
import com.agaram.eln.primary.config.TenantContext;
import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.cloudFileManip.CloudOrderAttachment;
import com.agaram.eln.primary.model.cloudFileManip.CloudProfilePicture;
import com.agaram.eln.primary.model.cloudFileManip.CloudUserSignature;
import com.agaram.eln.primary.model.instrumentDetails.LsOrderattachments;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.repository.cfr.LScfttransactionRepository;
import com.agaram.eln.primary.repository.cloudFileManip.CloudOrderAttachmentRepository;
import com.agaram.eln.primary.repository.cloudFileManip.CloudProfilePictureRepository;
import com.agaram.eln.primary.repository.cloudFileManip.CloudUserSignatureRepository;
import com.agaram.eln.primary.repository.usermanagement.LSuserMasterRepository;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.OperationContext;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.BlobContainerPublicAccessType;
import com.microsoft.azure.storage.blob.BlobRequestOptions;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.microsoft.azure.storage.blob.ListBlobItem;

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

	@Autowired
	private CloudUserSignatureRepository cloudusersignatureRepository;

	@Autowired
	private Environment env;

	public CloudProfilePicture addPhoto(Integer usercode, MultipartFile file, Date currentdate) throws IOException {

		LSuserMaster username = lsuserMasterRepository.findByusercode(usercode);
		String name = username.getUsername();
		LScfttransaction list = new LScfttransaction();
		list.setModuleName("UserManagement");
		list.setComments(name + " " + "Uploaded the profile picture successfully");
		list.setActions("View / Load");
		list.setSystemcoments("System Generated");
		list.setTableName("profile");
//		list.setTransactiondate(currentdate);
		list.setLsuserMaster(usercode);
		try {
			list.setTransactiondate(commonfunction.getCurrentUtcTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		lscfttransactionRepository.save(list);
		deletePhoto(usercode, list);

		CloudProfilePicture profile = new CloudProfilePicture();
		profile.setId(usercode);
		profile.setName(file.getName());
		profile.setImage(new Binary(BsonBinarySubType.BINARY, file.getBytes()));
		profile = cloudProfilePictureRepository.save(profile);

		return profile;
	}

	public CloudUserSignature addusersignature(Integer usercode, String username, MultipartFile file, Date currentdate)
			throws IOException {
		CloudUserSignature usersignature = new CloudUserSignature();
		usersignature.setId(usercode);
		usersignature.setName(file.getName());
		usersignature.setImage(new Binary(BsonBinarySubType.BINARY, file.getBytes()));
		usersignature = cloudusersignatureRepository.save(usersignature);
		return usersignature;
	}

	public CloudProfilePicture getPhoto(Integer id) {

		return cloudProfilePictureRepository.findById(id);
	}

	public CloudUserSignature getSignature(Integer id) {

		return cloudusersignatureRepository.findOne(id);
	}

	public Long deletePhoto(Integer id, LScfttransaction list) {
		list.setTableName("ProfilePicture");
		try {
			list.setTransactiondate(commonfunction.getCurrentUtcTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		lscfttransactionRepository.save(list);
		return cloudProfilePictureRepository.deleteById(id);
	}

	public CloudOrderAttachment storeattachment(MultipartFile file) throws IOException {
		UUID objGUID = UUID.randomUUID();
		String randomUUIDString = objGUID.toString();

		CloudOrderAttachment objattachment = new CloudOrderAttachment();
		objattachment.setFile(new Binary(BsonBinarySubType.BINARY, file.getBytes()));
		objattachment.setFileid(randomUUIDString);

		objattachment = cloudOrderAttachmentRepository.save(objattachment);

		return objattachment;
	}

	public String storeLargeattachment(String title, MultipartFile file) throws IOException {

		String bloburi = "";
		CloudStorageAccount storageAccount;
		CloudBlobClient blobClient = null;
		CloudBlobContainer container = null;
		String storageConnectionString = env.getProperty("azure.storage.ConnectionString");

		UUID objGUID = UUID.randomUUID();
		String randomUUIDString = objGUID.toString();

		try {
			// Parse the connection string and create a blob client to interact with Blob
			// storage
			storageAccount = CloudStorageAccount.parse(storageConnectionString);
			blobClient = storageAccount.createCloudBlobClient();
			container = blobClient.getContainerReference(TenantContext.getCurrentTenant());

			// Create the container if it does not exist with public access.
			System.out.println("Creating container: " + container.getName());
			container.createIfNotExists(BlobContainerPublicAccessType.CONTAINER, new BlobRequestOptions(),
					new OperationContext());

			File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + randomUUIDString);
			file.transferTo(convFile);

			// Getting a blob reference
			CloudBlockBlob blob = container.getBlockBlobReference(convFile.getName());

			// Creating blob and uploading file to it
			System.out.println("Uploading the sample file ");
			blob.uploadFromFile(convFile.getAbsolutePath());

			bloburi = blob.getName();

		} catch (StorageException ex) {
			System.out.println(String.format("Error returned from the service. Http code: %d and error code: %s",
					ex.getHttpStatusCode(), ex.getErrorCode()));
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		} finally {
			System.out.println("The program has completed successfully.");
			System.out.println(
					"Press the 'Enter' key while in the console to delete the sample files, example container, and exit the application.");

		}

		return bloburi; // id.toString();
	}

	public String storeFileWithTags(String title, MultipartFile file, String tagName) throws IOException {

		String bloburi = "";
		CloudStorageAccount storageAccount;
		CloudBlobClient blobClient = null;
		CloudBlobContainer container = null;
		String storageConnectionString = env.getProperty("azure.storage.ConnectionString");

		UUID objGUID = UUID.randomUUID();
		String randomUUIDString = objGUID.toString();

		try {
			// Parse the connection string and create a blob client to interact with Blob
			// storage
			storageAccount = CloudStorageAccount.parse(storageConnectionString);
			blobClient = storageAccount.createCloudBlobClient();
			container = blobClient.getContainerReference(TenantContext.getCurrentTenant());

			// Create the container if it does not exist with public access.
			System.out.println("Creating container: " + container.getName());
			container.createIfNotExists(BlobContainerPublicAccessType.CONTAINER, new BlobRequestOptions(),
					new OperationContext());

			File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + randomUUIDString);
			file.transferTo(convFile);

			// Getting a blob reference
			CloudBlockBlob blob = container.getBlockBlobReference(convFile.getName());

			Map<String, Object> metadata = new HashMap<>();
			metadata.put(tagName, convFile);
//			metadata.put(tagName, "value1");

			// Convert metadata HashMap to Map<String, String>
			HashMap<String, String> metadataString = new HashMap<>();
			for (Map.Entry<String, Object> entry : metadata.entrySet()) {
				metadataString.put(entry.getKey(), entry.getValue().toString());
			}

			blob.setMetadata(metadataString);

			blob.uploadFromFile(convFile.getAbsolutePath());

			bloburi = blob.getName();

		} catch (StorageException ex) {
			System.out.println(String.format("Error returned from the service. Http code: %d and error code: %s",
					ex.getHttpStatusCode(), ex.getErrorCode()));
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		} finally {
			System.out.println("The program has completed successfully.");
			System.out.println(
					"Press the 'Enter' key while in the console to delete the sample files, example container, and exit the application.");
		}

		return bloburi; // id.toString();
	}

	public CloudOrderAttachment retrieveFile(LsOrderattachments objattachment) {

		CloudOrderAttachment objfile = cloudOrderAttachmentRepository.findByFileid(objattachment.getFileid());

		if (objfile == null) {
			cloudOrderAttachmentRepository.findById(Integer.parseInt(objattachment.getFileid()));
		}

		return objfile;
	}

	public InputStream retrieveLargeFile(String fileid) throws IOException {

		CloudStorageAccount storageAccount;
		CloudBlobClient blobClient = null;
		CloudBlobContainer container = null;
		CloudBlockBlob blob = null;
		String storageConnectionString = env.getProperty("azure.storage.ConnectionString");
		try {
			// Parse the connection string and create a blob client to interact with Blob
			// storage
			storageAccount = CloudStorageAccount.parse(storageConnectionString);
			blobClient = storageAccount.createCloudBlobClient();
			container = blobClient.getContainerReference(TenantContext.getCurrentTenant());

			blob = container.getBlockBlobReference(fileid);
			return blob.openInputStream();
		} catch (StorageException ex) {
			System.out.println(String.format("Error returned from the service. Http code: %d and error code: %s",
					ex.getHttpStatusCode(), ex.getErrorCode()));
			throw new IOException(String.format("Error returned from the service. Http code: %d and error code: %s",
					ex.getHttpStatusCode(), ex.getErrorCode()));
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		return null;

	}

	public InputStream retrieveLargeFileByTag(String fileid) throws IOException {

		CloudStorageAccount storageAccount;
		CloudBlobClient blobClient = null;
		CloudBlobContainer container = null;
//		CloudBlockBlob blob = null;
		String storageConnectionString = env.getProperty("azure.storage.ConnectionString");
		try {
			// Parse the connection string and create a blob client to interact with Blob
			// storage
			storageAccount = CloudStorageAccount.parse(storageConnectionString);
			blobClient = storageAccount.createCloudBlobClient();
			container = blobClient.getContainerReference(TenantContext.getCurrentTenant());

//			blob = container.getBlockBlobReference(fileid);
//			return blob.openInputStream();
			// Get all blobs in the container
			Iterable<ListBlobItem> blobs = container.listBlobs();

			// Search for blob based on tag name
			for (ListBlobItem blobItem : blobs) {
				if (blobItem instanceof CloudBlockBlob) {
					CloudBlockBlob blob = (CloudBlockBlob) blobItem;
					String tagName = "your_tag_name";
					String tagValue = blob.getMetadata().get(tagName);
					if (tagValue != null && tagValue.equals("your_tag_value")) {
						// Found the blob with the specified tag, return its input stream
						return blob.openInputStream();
					}
				}
			}
		} catch (StorageException ex) {
			System.out.println(String.format("Error returned from the service. Http code: %d and error code: %s",
					ex.getHttpStatusCode(), ex.getErrorCode()));
			throw new IOException(String.format("Error returned from the service. Http code: %d and error code: %s",
					ex.getHttpStatusCode(), ex.getErrorCode()));
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		return null;

	}

	public Long deleteattachments(String id) {
		Long count = cloudOrderAttachmentRepository.deleteByFileid(id);
		if (count <= 0) {
			count = cloudOrderAttachmentRepository.deleteById(Integer.parseInt(id));
		}
		return count;
	}

	public void deletelargeattachments(String id) {
		CloudStorageAccount storageAccount;
		CloudBlobClient blobClient = null;
		CloudBlobContainer container = null;
		CloudBlockBlob blob = null;
		String storageConnectionString = env.getProperty("azure.storage.ConnectionString");
		try {
			// Parse the connection string and create a blob client to interact with Blob
			// storage
			storageAccount = CloudStorageAccount.parse(storageConnectionString);
			blobClient = storageAccount.createCloudBlobClient();
			container = blobClient.getContainerReference(TenantContext.getCurrentTenant());

			blob = container.getBlockBlobReference(id);
			blob.deleteIfExists();
		} catch (StorageException ex) {
			System.out.println(String.format("Error returned from the service. Http code: %d and error code: %s",
					ex.getHttpStatusCode(), ex.getErrorCode()));
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	public String storeReportFile(String title, File file) throws IOException {

		String bloburi = "";
		CloudStorageAccount storageAccount;
		CloudBlobClient blobClient = null;
		CloudBlobContainer container = null;
		String storageConnectionString = env.getProperty("azure.storage.ConnectionString");

		try {
			// Parse the connection string and create a blob client to interact with Blob
			// storage
			storageAccount = CloudStorageAccount.parse(storageConnectionString);
			blobClient = storageAccount.createCloudBlobClient();
			container = blobClient.getContainerReference(TenantContext.getCurrentTenant());

			// Create the container if it does not exist with public access.
			System.out.println("Creating container: " + container.getName());
			container.createIfNotExists(BlobContainerPublicAccessType.CONTAINER, new BlobRequestOptions(),
					new OperationContext());

//			File convFile = new File(System.getProperty("java.io.tmpdir")+"/"+title);
//			file.transferTo(convFile);

			// Getting a blob reference
			CloudBlockBlob blob = container.getBlockBlobReference(file.getName());

			// Creating blob and uploading file to it
			System.out.println("Uploading the sample file ");
			blob.uploadFromFile(file.getAbsolutePath());

			bloburi = blob.getName();

		} catch (StorageException ex) {
			System.out.println(String.format("Error returned from the service. Http code: %d and error code: %s",
					ex.getHttpStatusCode(), ex.getErrorCode()));
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		} finally {
			System.out.println("The program has completed successfully.");
			System.out.println(
					"Press the 'Enter' key while in the console to delete the sample files, example container, and exit the application.");

		}

		return bloburi; // id.toString();
	}

	public InputStream retrieveReportFiles(String fileid) throws IOException {

		CloudStorageAccount storageAccount;
		CloudBlobClient blobClient = null;
		CloudBlobContainer container = null;
		CloudBlockBlob blob = null;
		String storageConnectionString = env.getProperty("azure.storage.ConnectionString");
		try {
			// Parse the connection string and create a blob client to interact with Blob
			// storage
			storageAccount = CloudStorageAccount.parse(storageConnectionString);
			blobClient = storageAccount.createCloudBlobClient();
			container = blobClient.getContainerReference(TenantContext.getCurrentTenant());

			blob = container.getBlockBlobReference(fileid);
			return blob.openInputStream();
		} catch (StorageException ex) {
			System.out.println(String.format("Error returned from the service. Http code: %d and error code: %s",
					ex.getHttpStatusCode(), ex.getErrorCode()));
			throw new IOException(String.format("Error returned from the service. Http code: %d and error code: %s",
					ex.getHttpStatusCode(), ex.getErrorCode()));
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		return null;

	}

	public void deleteReportFile(String id) {
		CloudStorageAccount storageAccount;
		CloudBlobClient blobClient = null;
		CloudBlobContainer container = null;
		CloudBlockBlob blob = null;
		String storageConnectionString = env.getProperty("azure.storage.ConnectionString");
		try {
			// Parse the connection string and create a blob client to interact with Blob
			// storage
			storageAccount = CloudStorageAccount.parse(storageConnectionString);
			blobClient = storageAccount.createCloudBlobClient();
			container = blobClient.getContainerReference(TenantContext.getCurrentTenant());

			blob = container.getBlockBlobReference(id);
			blob.deleteIfExists();
		} catch (StorageException ex) {
			System.out.println(String.format("Error returned from the service. Http code: %d and error code: %s",
					ex.getHttpStatusCode(), ex.getErrorCode()));
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	public String storecloudfilesreturnUUID(MultipartFile file, String containername) throws IOException {

		System.out.print("entering storecloudfilesreturnUUID function");
//		int multitenant = Integer.parseInt(env.getProperty("ismultitenant"));
		// String bloburi="";
		CloudStorageAccount storageAccount;
		CloudBlobClient blobClient = null;
		CloudBlobContainer container = null;
		String storageConnectionString = env.getProperty("azure.storage.ConnectionString");

		UUID objGUID = UUID.randomUUID();
		String randomUUIDString = objGUID.toString();

		try {
			// Parse the connection string and create a blob client to interact with Blob
			// storage
			storageAccount = CloudStorageAccount.parse(storageConnectionString);
			blobClient = storageAccount.createCloudBlobClient();
			container = blobClient.getContainerReference(TenantContext.getCurrentTenant() + containername);

			// Create the container if it does not exist with public access.
			System.out.println("Creating container: " + container.getName());
			container.createIfNotExists(BlobContainerPublicAccessType.CONTAINER, new BlobRequestOptions(),
					new OperationContext());

			File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + randomUUIDString);
			file.transferTo(convFile);

			// Getting a blob reference
			CloudBlockBlob blob = container.getBlockBlobReference(convFile.getName());

			// Creating blob and uploading file to it
			System.out.println("Uploading the sample file in container " + container.getName());
			blob.uploadFromFile(convFile.getAbsolutePath());

			// bloburi = blob.getName();

		} catch (StorageException ex) {
			System.out.println(String.format("Error returned from the service. Http code: %d and error code: %s",
					ex.getHttpStatusCode(), ex.getErrorCode()));
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		} finally {
			System.out.println("The program has completed successfully.");
			System.out.println(
					"Press the 'Enter' key while in the console to delete the sample files, example container, and exit the application.");

		}

		return randomUUIDString; // id.toString();
	}

	public String storecloudfilesreturnwithpreUUID(MultipartFile file, String containername, String uuid,
			Integer ismultitenant) throws IOException {

		// String bloburi="";
		CloudStorageAccount storageAccount;
		CloudBlobClient blobClient = null;
		CloudBlobContainer container = null;
		String storageConnectionString = env.getProperty("azure.storage.ConnectionString");

		try {
			// Parse the connection string and create a blob client to interact with Blob
			// storage
			storageAccount = CloudStorageAccount.parse(storageConnectionString);
			blobClient = storageAccount.createCloudBlobClient();
			container = blobClient.getContainerReference(
					commonfunction.getcontainername(ismultitenant, TenantContext.getCurrentTenant()) + containername);

			// Create the container if it does not exist with public access.
			System.out.println("Creating container: " + container.getName());
			container.createIfNotExists(BlobContainerPublicAccessType.CONTAINER, new BlobRequestOptions(),
					new OperationContext());

			File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + uuid);
			file.transferTo(convFile);

			// Getting a blob reference
			CloudBlockBlob blob = container.getBlockBlobReference(convFile.getName());

			// Creating blob and uploading file to it
			System.out.println("Uploading the sample file ");
			blob.uploadFromFile(convFile.getAbsolutePath());

			// bloburi = blob.getName();

		} catch (StorageException ex) {
			System.out.println(String.format("Error returned from the service. Http code: %d and error code: %s",
					ex.getHttpStatusCode(), ex.getErrorCode()));
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		} finally {
			System.out.println("The program has completed successfully.");
			System.out.println(
					"Press the 'Enter' key while in the console to delete the sample files, example container, and exit the application.");

		}

		return uuid; // id.toString();
	}

	public InputStream retrieveCloudFile(String fileid, String containername) throws IOException {

		CloudStorageAccount storageAccount;
		CloudBlobClient blobClient = null;
		CloudBlobContainer container = null;
		CloudBlockBlob blob = null;
		String storageConnectionString = env.getProperty("azure.storage.ConnectionString");
		try {
			// Parse the connection string and create a blob client to interact with Blob
			// storage
			storageAccount = CloudStorageAccount.parse(storageConnectionString);
			blobClient = storageAccount.createCloudBlobClient();
			container = blobClient.getContainerReference(containername);

			blob = container.getBlockBlobReference(fileid);
			return blob.openInputStream();
		} catch (StorageException ex) {
			System.out.println(String.format("Error returned from the service. Http code: %d and error code: %s",
					ex.getHttpStatusCode(), ex.getErrorCode()));
			throw new IOException(String.format("Error returned from the service. Http code: %d and error code: %s",
					ex.getHttpStatusCode(), ex.getErrorCode()));
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		return null;

	}

	public InputStream updateversionCloudFile(String fileid, String containername, String newfieldid)
			throws IOException {

		CloudStorageAccount storageAccount;
		CloudBlobClient blobClient = null;
		CloudBlobContainer container = null;
		CloudBlockBlob blob = null;
		String storageConnectionString = env.getProperty("azure.storage.ConnectionString");
		try {
			// Parse the connection string and create a blob client to interact with Blob
			// storage
			storageAccount = CloudStorageAccount.parse(storageConnectionString);
			blobClient = storageAccount.createCloudBlobClient();
			container = blobClient.getContainerReference(containername);

			blob = container.getBlockBlobReference(fileid);

			File targetFile = new File(System.getProperty("java.io.tmpdir") + "/" + newfieldid);

			FileUtils.copyInputStreamToFile(blob.openInputStream(), targetFile);

			// Getting a blob reference
			CloudBlockBlob blob1 = container.getBlockBlobReference(targetFile.getName());

			// Creating blob and uploading file to it
			System.out.println("Uploading the sample file ");
			blob1.uploadFromFile(targetFile.getAbsolutePath());

			return blob.openInputStream();
		} catch (StorageException ex) {
			System.out.println(String.format("Error returned from the service. Http code: %d and error code: %s",
					ex.getHttpStatusCode(), ex.getErrorCode()));
			throw new IOException(String.format("Error returned from the service. Http code: %d and error code: %s",
					ex.getHttpStatusCode(), ex.getErrorCode()));
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		return null;

	}

	public InputStream movestreamstocontainer(InputStream InputStream, String containername, String fileid)
			throws IOException {

		CloudStorageAccount storageAccount;
		CloudBlobClient blobClient = null;
		CloudBlobContainer container = null;
		CloudBlockBlob blob = null;
		String storageConnectionString = env.getProperty("azure.storage.ConnectionString");
		try {
			// Parse the connection string and create a blob client to interact with Blob
			// storage
			storageAccount = CloudStorageAccount.parse(storageConnectionString);
			blobClient = storageAccount.createCloudBlobClient();
			container = blobClient.getContainerReference(containername);

			System.out.println("Creating container: " + container.getName());
			container.createIfNotExists(BlobContainerPublicAccessType.CONTAINER, new BlobRequestOptions(),
					new OperationContext());

			File targetFile = new File(System.getProperty("java.io.tmpdir") + "/" + fileid);

			FileUtils.copyInputStreamToFile(InputStream, targetFile);

			// Getting a blob reference
			blob = container.getBlockBlobReference(targetFile.getName());

			// Creating blob and uploading file to it
			System.out.println("Uploading the sample file ");
			blob.uploadFromFile(targetFile.getAbsolutePath());

			return blob.openInputStream();
		} catch (StorageException ex) {
			System.out.println(String.format("Error returned from the service. Http code: %d and error code: %s",
					ex.getHttpStatusCode(), ex.getErrorCode()));
			throw new IOException(String.format("Error returned from the service. Http code: %d and error code: %s",
					ex.getHttpStatusCode(), ex.getErrorCode()));
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		return null;

	}

	public boolean movefiletoanothercontainerandremove(String fromcontainer, String tocontainer, String fileid) {
		try {
			InputStream stream = retrieveCloudFile(fileid, fromcontainer);
			movestreamstocontainer(stream, tocontainer, fileid);
			deleteFile(fileid, fromcontainer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;
	}

	public void deletecloudFile(String id, String containername) {
		CloudStorageAccount storageAccount;
		CloudBlobClient blobClient = null;
		CloudBlobContainer container = null;
		CloudBlockBlob blob = null;
		String storageConnectionString = env.getProperty("azure.storage.ConnectionString");
		try {
			// Parse the connection string and create a blob client to interact with Blob
			// storage
			storageAccount = CloudStorageAccount.parse(storageConnectionString);
			blobClient = storageAccount.createCloudBlobClient();
			container = blobClient.getContainerReference(TenantContext.getCurrentTenant() + containername);

			blob = container.getBlockBlobReference(id);
			blob.deleteIfExists();
		} catch (StorageException ex) {
			System.out.println(String.format("Error returned from the service. Http code: %d and error code: %s",
					ex.getHttpStatusCode(), ex.getErrorCode()));
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	public void deleteFile(String id, String containername) {
		CloudStorageAccount storageAccount;
		CloudBlobClient blobClient = null;
		CloudBlobContainer container = null;
		CloudBlockBlob blob = null;
		String storageConnectionString = env.getProperty("azure.storage.ConnectionString");
		try {
			// Parse the connection string and create a blob client to interact with Blob
			// storage
			storageAccount = CloudStorageAccount.parse(storageConnectionString);
			blobClient = storageAccount.createCloudBlobClient();
			container = blobClient.getContainerReference(containername);

			blob = container.getBlockBlobReference(id);
			blob.deleteIfExists();
		} catch (StorageException ex) {
			System.out.println(String.format("Error returned from the service. Http code: %d and error code: %s",
					ex.getHttpStatusCode(), ex.getErrorCode()));
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	public Map<String, Object> storecloudReportfile(byte[] documentBytes, String containerName, String documentName)
	        throws IOException {
	    Map<String, Object> objMap = new HashMap<>();
	    CloudStorageAccount storageAccount;
	    CloudBlobClient blobClient;
	    CloudBlobContainer container;
	    String storageConnectionString = env.getProperty("azure.storage.ConnectionString");
	    try {
	        storageAccount = CloudStorageAccount.parse(storageConnectionString);
	        blobClient = storageAccount.createCloudBlobClient();
	        container = blobClient.getContainerReference(containerName);
	        System.out.println("Creating container: " + container.getName());
	        container.createIfNotExists();
	        CloudBlockBlob blob = container.getBlockBlobReference(documentName);
	        InputStream dataStream = new ByteArrayInputStream(documentBytes);
	        blob.getProperties().setContentType("application/json");
	        blob.upload(dataStream, documentBytes.length);
	        System.out.println("File uploaded to Azure Blob Storage successfully.");
	        objMap.put("blobName", documentName);
	        objMap.put("blobUri", blob.getUri());
	    } catch (StorageException ex) {
	        System.out.println(String.format("Error returned from the service. Http code: %d and error code: %s",
	                ex.getHttpStatusCode(), ex.getErrorCode()));
	    } catch (Exception ex) {
	        System.out.println(ex.getMessage());
	    }

	    return objMap;
	
	}

	public Map<String, Object> storecloudSheetsreturnwithpreUUID(String Content, String containerName)
			throws IOException {

		Map<String, Object> objMap = new HashMap<String, Object>();

		CloudStorageAccount storageAccount;
		CloudBlobClient blobClient = null;
		CloudBlobContainer container = null;

		String storageConnectionString = env.getProperty("azure.storage.ConnectionString");
		String randomUUIDString = UUID.randomUUID().toString();

		try {
			// Parse the connection string and create a blob client to interact with Blob
			storageAccount = CloudStorageAccount.parse(storageConnectionString);
			blobClient = storageAccount.createCloudBlobClient();
			container = blobClient.getContainerReference(containerName);

			// Create the container if it does not exist with public access.
			System.out.println("Creating container: " + container.getName());
			container.createIfNotExists(BlobContainerPublicAccessType.CONTAINER, new BlobRequestOptions(),
					new OperationContext());

			// Getting a blob reference
			CloudBlockBlob blob = container.getBlockBlobReference(randomUUIDString);

			// uploading text
			blob.uploadText(Content);

			objMap.put("uuid", randomUUIDString);
			objMap.put("uri", blob.getUri());

			System.out.println("Uploading the file ");

		} catch (StorageException ex) {
			System.out.println(String.format("Error returned from the service. Http code: %d and error code: %s",
					ex.getHttpStatusCode(), ex.getErrorCode()));
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		} finally {
			System.out.println("The program has completed successfully.");
			System.out.println(
					"Press the 'Enter' key while in the console to delete the sample files, example container, and exit the application.");

		}

		return objMap;
	}

	public String retrieveCloudSheets(String fileid, String containername) throws IOException {

		CloudStorageAccount storageAccount;
		CloudBlobClient blobClient = null;
		CloudBlobContainer container = null;
		CloudBlockBlob blob = null;
		String storageConnectionString = env.getProperty("azure.storage.ConnectionString");
		try {
			// Parse the connection string and create a blob client to interact with Blob
			// storage
			storageAccount = CloudStorageAccount.parse(storageConnectionString);
			blobClient = storageAccount.createCloudBlobClient();
			container = blobClient.getContainerReference(containername);
			blob = container.getBlockBlobReference(fileid);
			return blob.downloadText();
		} catch (StorageException ex) {
			System.out.println(String.format("Error returned from the service. Http code: %d and error code: %s",
					ex.getHttpStatusCode(), ex.getErrorCode()));
			throw new IOException(String.format("Error returned from the service. Http code: %d and error code: %s",
					ex.getHttpStatusCode(), ex.getErrorCode()));
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		return null;

	}

	public boolean tocopyoncontainertoanothercontainer(List<String> fileIds, String sourceContainerName,
			String destinationContainerName) throws IOException {
		CloudStorageAccount storageAccount;
		CloudBlobClient blobClient = null;
		CloudBlobContainer sourceContainer = null;
		CloudBlobContainer destinationContainer = null;
		String storageConnectionString = env.getProperty("azure.storage.ConnectionString");
		try {
			// Parse the connection string and create a blob client to interact with Blob
			// storage
			storageAccount = CloudStorageAccount.parse(storageConnectionString);
			blobClient = storageAccount.createCloudBlobClient();
			sourceContainer = blobClient.getContainerReference(sourceContainerName);
			destinationContainer = blobClient.getContainerReference(destinationContainerName);
			destinationContainer.createIfNotExists(BlobContainerPublicAccessType.CONTAINER, new BlobRequestOptions(),
					new OperationContext());

			for (String fileId : fileIds) {
				CloudBlockBlob sourceBlob = sourceContainer.getBlockBlobReference(fileId);
				CloudBlockBlob destinationBlob = destinationContainer.getBlockBlobReference(fileId);
				destinationBlob.startCopy(sourceBlob);
			}
			return true;
		} catch (StorageException ex) {
			System.out.println(String.format("Error returned from the service. Http code: %d and error code: %s",
					ex.getHttpStatusCode(), ex.getErrorCode()));
//		        throw new IOException(String.format("Error returned from the service. Http code: %d and error code: %s",
//		                ex.getHttpStatusCode(), ex.getErrorCode()));
			return false;
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			return false;
		}
//			return storageConnectionString;

	}

	public byte[] retrieveCloudReportFile(String containerName, String documentName) {
        CloudStorageAccount storageAccount;
        CloudBlobClient blobClient;
        CloudBlobContainer container;
        byte[] documentBytes = null;
    	String storageConnectionString = env.getProperty("azure.storage.ConnectionString");
        try {
            storageAccount = CloudStorageAccount.parse(storageConnectionString);
            blobClient = storageAccount.createCloudBlobClient();
            container = blobClient.getContainerReference(containerName);
            CloudBlockBlob blob = container.getBlockBlobReference(documentName);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            blob.download(outputStream);
            documentBytes = outputStream.toByteArray();
            System.out.println("File downloaded from Azure Blob Storage successfully.");

        } catch (StorageException ex) {
            System.out.println(String.format("Error returned from the service. Http code: %d and error code: %s",
                    ex.getHttpStatusCode(), ex.getErrorCode()));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return documentBytes;

	}
}