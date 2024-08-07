package com.agaram.eln.primary.service.report;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.apache.xmlbeans.XmlCursor;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHMerge;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVMerge;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.agaram.eln.primary.config.TenantContext;
import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.cloudFileManip.CloudOrderCreation;
import com.agaram.eln.primary.model.cloudFileManip.CloudSheetCreation;
import com.agaram.eln.primary.model.configuration.LSConfiguration;
import com.agaram.eln.primary.model.general.OrderCreation;
import com.agaram.eln.primary.model.general.SheetCreation;
import com.agaram.eln.primary.model.instrumentDetails.LSlogilablimsorderdetail;
import com.agaram.eln.primary.model.report.LSdocdirectory;
import com.agaram.eln.primary.model.report.LSdocreports;
import com.agaram.eln.primary.model.report.LSdocreportsversionhistory;
import com.agaram.eln.primary.model.sheetManipulation.LSfile;
import com.agaram.eln.primary.model.sheetManipulation.LSsamplefile;
import com.agaram.eln.primary.model.sheetManipulation.LSworkflow;
import com.agaram.eln.primary.model.sheetManipulation.LSworkflowgroupmapping;
import com.agaram.eln.primary.model.usermanagement.LSMultiusergroup;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.usermanagement.LSprojectmaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.model.usermanagement.LSusersteam;
import com.agaram.eln.primary.model.usermanagement.LSuserteammapping;
import com.agaram.eln.primary.repository.cfr.LScfttransactionRepository;
import com.agaram.eln.primary.repository.cloudFileManip.CloudOrderCreationRepository;
import com.agaram.eln.primary.repository.cloudFileManip.CloudSheetCreationRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LSlogilablimsorderdetailRepository;
import com.agaram.eln.primary.repository.report.LSdocdirectoryRepository;
import com.agaram.eln.primary.repository.report.LSdocreportsRepository;
import com.agaram.eln.primary.repository.report.LSdocreportsversionHistoryRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LSfileRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LSworkflowRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LSworkflowgroupmappingRepository;
import com.agaram.eln.primary.repository.usermanagement.LSMultiusergroupRepositery;
import com.agaram.eln.primary.repository.usermanagement.LSprojectmasterRepository;
import com.agaram.eln.primary.repository.usermanagement.LSuserMasterRepository;
import com.agaram.eln.primary.repository.usermanagement.LSusersteamRepository;
import com.agaram.eln.primary.repository.usermanagement.LSuserteammappingRepository;
import com.agaram.eln.primary.service.cloudFileManip.CloudFileManipulationservice;
import com.agaram.eln.primary.service.configuration.ConfigurationService;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSDBFile;

import io.jsonwebtoken.lang.Arrays;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;

@Service
@EnableJpaRepositories(basePackageClasses = LSdocreportsRepository.class)
public class ReportsService {



	@Autowired
	private LSdocreportsRepository LSdocreportsRepositoryObj;

	@Autowired
	private LSdocdirectoryRepository LSdocdirectoryRepositoryObj;

	@Autowired
	private LSuserMasterRepository LSuserMasterRepositoryObj;

	@Autowired
	private LSlogilablimsorderdetailRepository lslogilablimsorderdetailRepository;

	@Autowired
	private LSfileRepository LSfileRepositoryObj;

	@Autowired
	private LSdocreportsversionHistoryRepository LSdocreportsversionHistoryRepositoryObj;

	@Autowired
	private LScfttransactionRepository lscfttransactionRepository;

	@Autowired
	private LSuserteammappingRepository lsuserteammappingRepository;

	@Autowired
	private LSusersteamRepository lsusersteamRepository;

	@Autowired
	private LSprojectmasterRepository lsprojectmasterRepository;

	@Autowired
	private LSworkflowgroupmappingRepository lsworkflowgroupmappingRepository;

	@Autowired
	private LSworkflowRepository lsworkflowRepository;

	@Autowired
	private ConfigurationService ObjConfigurationService;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private GridFsTemplate gridFsTemplate;

	@Autowired
	private Environment env;

	@Autowired
	private CloudSheetCreationRepository cloudSheetCreationRepository;

	@Autowired
	private CloudFileManipulationservice CloudFileManipulationservice;

	@Autowired
	private CloudOrderCreationRepository CloudOrderCreationRepository;

	@Autowired
	private CloudOrderCreationRepository cloudOrderCreationRepository;
	@Autowired
	private LSMultiusergroupRepositery LSMultiusergroupRepositery;
	
	@Autowired
	private CloudFileManipulationservice objCloudFileManipulationservice;

	LSConfiguration FTPConfig = new LSConfiguration();

	TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
		}

		public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
		}
	} };

	private boolean isftpAvailable() {
		boolean bisftpAvailable = false;
		Map<String, Object> mapObj = ObjConfigurationService.getConfigurationForFTP();
		if (mapObj.get("configuration") == null) {
			bisftpAvailable = false;
		} else {
			FTPConfig = (LSConfiguration) mapObj.get("configuration");
			bisftpAvailable = true;
		}
		return bisftpAvailable;
	}

	private String getDocxAbsolutePath() {
//		LSConfiguration objLSConfiguration = ObjConfigurationService.getConfigurationForDocsPath();
		String filePath = "";
		if (filePath == "") {
			if (System.getProperty("os.name").contains("Linux") || System.getProperty("os.name").contains("LINUX")) {
				System.out.print("reportgetAbsolutePath()" + new File("").getAbsolutePath().toString());
			
				filePath = "/home/site/wwwroot/webapps/ELNdocuments";
				File newFile = new File(filePath);
				if (!newFile.exists()) {
					newFile.mkdir();
				}
			} else {
				if (env.getProperty("DocsPath") != null && env.getProperty("DocsPath") != "") {
					filePath = env.getProperty("DocsPath");
				} else {
					filePath = new File("").getAbsolutePath() + "/webapps/ROOT/ELNdocuments";
				}
			}
		}
		File newFile = new File(filePath);
		if (!newFile.exists()) {
			newFile.mkdir();
		}
		filePath += "/reports";
		newFile = new File(filePath);
		if (!newFile.exists()) {
			newFile.mkdir();
		}
		return filePath;
	}

	int uploadSingleFile(String FileAbsolutePath, int doctype) throws IOException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);

		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
		File file = new File(FileAbsolutePath);
		body.add("file", new FileSystemResource(file));
		body.add("type", doctype);

		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
		String fileReceiver = env.getProperty("fileReceiver");
		String serverUrl = fileReceiver + "singlefileupload/";
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.postForEntity(serverUrl, requestEntity, String.class);
		System.out.println("Response code: " + response.getStatusCode());
		return response.getStatusCode().value();
	}

	public Map<String, Object> handleFTP(String filename, String type, String FolderName) {
		Map<String, Object> rtnObj = new HashMap<String, Object>();
		FTPClient client = new FTPClient();
		FileInputStream fis = null;
		FileOutputStream fos = null;
		boolean status = false;
		String errMsg = "";
		boolean loginStatus = false;
		filename += ".docx";
		try {
			String FtpUrl = FTPConfig.getConfigpath();
			String FtpUserName = FTPConfig.getConfigusername();
			String FtpPass = FTPConfig.getConfiguserpass();
			String[] splitString = FtpUrl.split(":");
			client.connect(splitString[0], Integer.parseInt(splitString[1]));
			loginStatus = client.login(FtpUserName, FtpPass);
			if (loginStatus) {
				client.changeWorkingDirectory("ELNDocuments");
				if (client.getReplyCode() == 550) {
					client.makeDirectory("ELNDocuments");
					client.changeWorkingDirectory("ELNDocuments");
				}
				client.changeWorkingDirectory(FolderName);
				if (client.getReplyCode() == 550) {
					client.makeDirectory(FolderName);
					client.changeWorkingDirectory(FolderName);
				}
				client.setFileType(FTP.BINARY_FILE_TYPE);
				client.setAutodetectUTF8(true);
			
				String filePath = getDocxAbsolutePath();
				File absolutePathFile = new File(filePath, filename);
				if (type.equals("load")) {
					FTPFile[] FileLst = client.listFiles();
					FTPFile sFTPFile = new FTPFile();
					for (FTPFile SingleFile : FileLst) {
						
						if (((String) SingleFile.getName()).equals(filename)) {
							sFTPFile = SingleFile;
							break;
						}
					}
					if (sFTPFile.isValid()) {
						status = true;
						
						if (absolutePathFile.exists()) {
							
							absolutePathFile.delete();
							
						}
						fos = new FileOutputStream(absolutePathFile);
						client.retrieveFile(filename, fos);
						fos.close();
					
					} else {
						errMsg = "Requested file not found in FTP";
					}
				} else if (type.equals("save")) {
					if (absolutePathFile.exists()) {
					
						fis = new FileInputStream(absolutePathFile);
						client.storeFile(filename, fis);
						fis.close();
						absolutePathFile.delete();
						status = true;
					} else {
						errMsg = "Requested file not found";
					
						status = false;
					}
				}
				client.logout();
				client.disconnect();
			} else {
				
				status = false;
			}
		} catch (IOException e) {
		
			status = false;
		}
		rtnObj.put("status", status);
		rtnObj.put("errMsg", errMsg);
		return rtnObj;
	}

	public Map<String, Object> uploadAndRetriveDoc(Map<String, Object> FIleInfo, String type) {
		Map<String, Object> rtnObj = new HashMap<String, Object>();
		if (type.equals("retrive")) {
			GridFSDBFile file = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(FIleInfo.get("id"))));
			File path = (File) FIleInfo.get("filePath");
			try (FileOutputStream out = new FileOutputStream(path)) {
				int read;
				final byte[] bytes = new byte[1024];
				InputStream stream = file.getInputStream();
				while ((read = stream.read(bytes)) != -1) {
					out.write(bytes, 0, read);
				}
				out.flush();
				out.close();
			} catch (Exception e) {
			
			}

			rtnObj.put("status", "success");
			rtnObj.put("file", file);
		} else if (type.equals("upload")) {
			DBObject metaData = new BasicDBObject();
			metaData.put("title", FIleInfo.get("title"));
			String contentType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
			Object id = gridFsTemplate.store((InputStream) FIleInfo.get("getInputStream"),
					(String) FIleInfo.get("Name"), contentType, metaData).getId();
			rtnObj.put("status", "success");
			rtnObj.put("id", id.toString());
		}

		return rtnObj;
	}

	public Map<String, Object> checkLinkAvail(String downloadUri, String FileType) {
		Map<String, Object> rtnObj = new HashMap<String, Object>();
		boolean status = false;
		String statusMsg = "";
		InputStream stream = null;
		HttpsURLConnection connectionSSL = null;
		HttpURLConnection connection = null;
		try {
			try {
				if (downloadUri.contains("https")) {
					SSLContext sc = SSLContext.getInstance("SSL");
					sc.init(null, trustAllCerts, new java.security.SecureRandom());
					HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
					URL url = new URL(downloadUri);
					connectionSSL = (HttpsURLConnection) url.openConnection();
					stream = connectionSSL.getInputStream();
				} else {
					URL url = new URL(downloadUri);
					connection = (HttpURLConnection) url.openConnection();
					stream = connection.getInputStream();
				}
			} catch (FileNotFoundException e) {
				
				String FIlePath = getDocxAbsolutePath();
				if (new File(FIlePath).exists()) {
					File newFile = new File(FIlePath + "/link.txt");
					if (!newFile.exists()) {
						try {
							newFile.createNewFile();
							if (newFile.exists()) {
								FileWriter fw = new FileWriter(FIlePath + "/link.txt");
								fw.write("working");
								fw.close();
								status = true;
							}
						} catch (IOException ex) {
						
							status = false;
							System.out.print("report service IDS_MSG_DOCXSURLNOTFOUND 418");
							statusMsg = "IDS_MSG_DOCXSURLNOTFOUND";
						}
					}
				}
			}
			if (FileType.equals("api")) {
				BufferedInputStream in = new BufferedInputStream(stream);
				BufferedReader r = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
				r.readLine();
				String line1 = r.readLine();
				String line2 = r.readLine();
				if (line1.contains("Ascensio System SIA")) {
					status = true;
				} else if (line2.contains("Ascensio System SIA")) {
					status = true;
				} else {
					status = false;
					statusMsg = "IDS_MSG_DOCXSAPINOTFOUND";
				}
			} else if (FileType.equals("url")) {
				String fileContent = new BufferedReader(new InputStreamReader(stream)).lines()
						.collect(Collectors.joining("\n"));
			
				if (fileContent.contains("working") || fileContent.contains("WORKING")) {
					status = true;
					String filePath = "";
					if (System.getProperty("os.name").contains("Linux")
							|| System.getProperty("os.name").contains("LINUX")) {
						filePath = new File("").getAbsolutePath();
					} else {
						filePath = getDocxAbsolutePath() + "/link.txt";
					}
					File linkFile = new File(filePath);
					if (linkFile.exists()) {
//						fileContent = new BufferedReader(new InputStreamReader(new FileInputStream(linkFile))).lines()
//								.collect(Collectors.joining("\n"));
//						
//						
//						if (fileContent.contains("working")||fileContent.contains("WORKING")) {
						status = true;
//						} else {
//							status = false;
//							System.out.print("report service ID_DOCXSURLNOTFOUND 458");
//							statusMsg = "ID_DOCXSURLNOTFOUND 2";
//						}
					} else {
						status = false;
						System.out.print("report service IDS_MSG_DOCXSURLNOTFOUND 463");
					}
				} else {
					String FIlePath = getDocxAbsolutePath();
					if (new File(FIlePath).exists()) {
						File newFile = new File(FIlePath + "/link.txt");
						if (!newFile.exists()) {
							try {
								newFile.createNewFile();
								if (newFile.exists()) {
									FileWriter fw = new FileWriter(FIlePath + "/link.txt");
									fw.write("working");
									fw.close();
									status = true;
								}
							} catch (IOException e) {
							
								status = false;
								System.out.print("report service IDS_MSG_DOCXSURLNOTFOUND 482");
							}
							if (status) {
								fileContent = new BufferedReader(new InputStreamReader(stream)).lines()
										.collect(Collectors.joining("\n"));
							
								if (fileContent.equals("working")) {
									status = true;
								} else {
									status = false;
									System.out.print("report service IDS_MSG_DOCXSURLNOTFOUND 493");
								}
							}
						}
					} else {
						status = false;
					}
				}
			}

			if (downloadUri.contains("https")) {
				connectionSSL.disconnect();
			} else {
				connection.disconnect();
			}
		} catch (NoSuchAlgorithmException | KeyManagementException | IOException e) {
			
			status = false;
			if (FileType.equals("api")) {
				statusMsg = "IDS_MSG_DOCXSAPINOTFOUND";
			} else if (FileType.equals("url")) {
				System.out.print("report service IDS_MSG_DOCXSURLNOTFOUND 509");
				statusMsg = "IDS_MSG_DOCXSURLNOTFOUND";
			}
		}
		rtnObj.put("status", status);
		rtnObj.put("statusMsg", statusMsg);
		return rtnObj;
	}

	public Map<String, Object> getDownloadReportsInitRequest(Map<String, Object> argObj) {
		Map<String, Object> rtnObj = new HashMap<String, Object>();
		LScfttransaction LScfttransactionobj = new LScfttransaction();
		if (argObj.containsKey("objsilentaudit")) {
			if (argObj.get("objsilentaudit") != null) {
				LScfttransactionobj = new ObjectMapper().convertValue(argObj.get("objsilentaudit"),
						new TypeReference<LScfttransaction>() {
						});
			}
		}
		List<Integer> teamCode = lsuserteammappingRepository
				.getTeamcodeByLsuserMaster(LScfttransactionobj.getLsuserMaster());
		if (teamCode.size() > 0) {
			List<LSuserMaster> LstLSuserMaster = lsuserteammappingRepository.getLsuserMasterByTeamcode(teamCode);
			List<Integer> userCodeLst = new ArrayList<Integer>();
			for (LSuserMaster ObjLSuserMaster : LstLSuserMaster) {
				userCodeLst.add(ObjLSuserMaster.getUsercode());
			}
			rtnObj.put("LstLSuserMaster", userCodeLst);
		} else {
			List<Integer> userCodeLst = new ArrayList<Integer>();
			rtnObj.put("LstLSuserMaster", userCodeLst);
		}
		Map<String, Object> ConfigObj = new HashMap<String, Object>();
		if (env.getProperty("DocxApi") != null && !env.getProperty("DocxApi").isEmpty()
				|| env.getProperty("DocxUrl") != null && !env.getProperty("DocxUrl").isEmpty()) {
			String apiUri = env.getProperty("DocxApi");
			String doxUri = env.getProperty("DocxUrl");
			Map<String, Object> apiStatus = checkLinkAvail(apiUri, "api");
			Map<String, Object> urlStatus = new HashMap<String, Object>();
			if (env.getProperty("fileReceiver") == null) {
				urlStatus = checkLinkAvail(doxUri + "/reports/link.txt", "url");
			} else {
				urlStatus = checkLinkAvail(doxUri + "/link.txt", "url");
			}
//			boolean urlStatus = true;
			if ((boolean) apiStatus.get("status")) {
				ConfigObj.put("DocxApi", apiUri);
			} else {
				ConfigObj.put("status", apiStatus.get("statusMsg"));
			}
			if ((boolean) urlStatus.get("status")) {
				ConfigObj.put("DocxUrl", doxUri);
			} else {
				ConfigObj.put("status", urlStatus.get("statusMsg"));
			}
		} else {
			ConfigObj.put("status", "IDS_DOCXAPIORURlISMISSING.");
		}

		rtnObj.put("config", ConfigObj);
//		rtnObj.put("config", ObjConfigurationService.getConfigurationForDocxInit());
		rtnObj.put("DocxDirectoryLst", getDocxDirectoryLst(LScfttransactionobj));
		rtnObj.put("DocxReportLst", getLSdocreportsLst("all", LScfttransactionobj));

//		if (LScfttransactionobj != null) {
//			LScfttransactionobj = new ObjectMapper().convertValue(argObj.get("objsilentaudit"),
//					new TypeReference<LScfttransaction>() {
//					});
//			LScfttransactionobj.setTableName("LSdocreports");
////			lscfttransactionRepository.save(LScfttransactionobj);
//		}
		if (argObj.containsKey("objmanualaudit")) {
			if (argObj.get("objmanualaudit") != null) {
				LScfttransaction LScfttransactionManualobj = new ObjectMapper()
						.convertValue(argObj.get("objmanualaudit"), new TypeReference<LScfttransaction>() {
						});
				LScfttransactionobj.setTableName("LSdocreports");
				lscfttransactionRepository.save(LScfttransactionManualobj);
			}
		}
		return rtnObj;
	}

	@SuppressWarnings("resource")
	public Map<String, Object> createNewReportDocx(Map<String, Object> argObj) throws FileNotFoundException {
		Map<String, Object> objMap = new HashMap<String, Object>();
		String Status = "";
		try {
			int docType = 0;
			String sheetfilecodeString = "";

			LScfttransaction LScfttransactionobj = new LScfttransaction();

			if (argObj.containsKey("type")) {
				if ((int) argObj.get("type") == 1) {
					docType = 1;
					sheetfilecodeString = (String) argObj.get("FileCode");
				}
			}
			String uniquefilename = UUID.randomUUID().toString();
			String filename = uniquefilename + "." + "docx";
			String filePath = getDocxAbsolutePath();
			if (argObj.containsKey("objsilentaudit")) {
				if (argObj.get("objsilentaudit") != null) {
					LScfttransactionobj = new ObjectMapper().convertValue(argObj.get("objsilentaudit"),
							new TypeReference<LScfttransaction>() {
							});
				}
			}
			if (docType == 1) {
				if (System.getProperty("os.name").contains("Linux")
						|| System.getProperty("os.name").contains("LINUX")) {
					filePath += "/templates";
				} else {
					filePath += "\\templates";
				}
			}
			File newFile = new File(filePath);
			if (!newFile.exists()) {
				newFile.mkdir();
			}
			// newFile = new File(filePath, filename);
			newFile = new File(filePath, filename);
//			if(System.getProperty("os.name").contains("Linux")) {
////				newFile.createNewFile();
//				Files.createFile(newFile.toPath());
//			}else {
			FileOutputStream fos = new FileOutputStream(newFile);
			new XWPFDocument().write(fos);
			fos.close();
//			}
			if (env.getProperty("fileReceiver") != null) {
				int httpfileStatus = uploadSingleFile(newFile.getAbsolutePath(), docType);
				if (httpfileStatus == 200) {
					objMap.put("fileFullPath", "");
				} else {
					objMap.put("fileFullPath", newFile.getAbsolutePath());
				}
			} else {
				objMap.put("fileFullPath", newFile.getAbsolutePath());
			}
			objMap.put("fileName", "New Document.docx");
			if (docType == 1) {
				objMap.put("fileOriginalPath", "reports/templates/" + filename);
			} else {
				objMap.put("fileOriginalPath", "reports/" + filename);
			}
			objMap.put("hashKey", uniquefilename);
			objMap.put("type", docType);
			objMap.put("SheetFileCode", sheetfilecodeString);
			LSdocreports LSDocReportobj = new LSdocreports();
			if (argObj.containsKey("lssitecode")) {
				if (argObj.get("lssitecode") != null) {
//				lsusermaster = new ObjectMapper().convertValue(argObj.get("Lsusermaster"),new TypeReference<LSuserMaster>(){});
//				LSDocReportobj.setLssitemaster(lsusermaster.getLssitemaster().getSitecode());

					String sSitecode = (String) argObj.get("lssitecode");

					int site = Integer.parseInt(sSitecode);

					LSDocReportobj.setLssitemaster(site);
				}
			}
			LSDocReportobj.setExtention("docx");
			LSDocReportobj.setFileName(null);
			LSDocReportobj.setCreatedate(new Date());
			LSDocReportobj.setCreatedBy(LScfttransactionobj.getLsuserMaster());
			LSDocReportobj.setFileHashName(uniquefilename);
			LSDocReportobj.setIsTemplate(docType);
			LSDocReportobj.setSheetfilecodeString(sheetfilecodeString);
			LSDocReportobj.setStatus(1);
			LSDocReportobj.setIsmultiplesheet(0);
			LSDocReportobj.setDocdirectorycode(0);
			
			LSdocreportsRepositoryObj.save(LSDocReportobj);
			Status = "success";
			if (argObj.containsKey("objsilentaudit")) {
				if (argObj.get("objsilentaudit") != null) {
					LScfttransactionobj.setSystemcoments("System Generated");
					LScfttransactionobj.setTableName("LSdocreports");
					lscfttransactionRepository.save(LScfttransactionobj);
				}
			}
			if (argObj.containsKey("objmanualaudit")) {
				if (argObj.get("objmanualaudit") != null) {
					LScfttransaction LScfttransactionManualobj = new ObjectMapper()
							.convertValue(argObj.get("objmanualaudit"), new TypeReference<LScfttransaction>() {
							});
					if (argObj.containsKey("objuser")) {
						@SuppressWarnings("unchecked")
						Map<String, Object> objuser = (Map<String, Object>) argObj.get("objuser");
						LScfttransactionManualobj.setComments((String) objuser.get("comments"));
					}
					LScfttransactionManualobj.setTableName("LSdocreports");
					//lscfttransactionRepository.save(LScfttransactionManualobj);
				}
			}
			objMap.put("status", Status);
		
		} catch (IOException e) {
			e.printStackTrace();
			Status = "failed";
			objMap.put("error", e.getMessage());
			
			objMap.put("status", e.getMessage());
		}
		return objMap;
	}

	@SuppressWarnings({ "unchecked", "resource" })
	public void saveDocxsReport(HttpServletRequest request, HttpServletResponse response) throws IOException {
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(request.getInputStream()).useDelimiter("\\A");
		String body = scanner.hasNext() ? scanner.next() : "";
		JSONObject jsonObj = null;
		PrintWriter writer = response.getWriter();
		try {
			jsonObj = (JSONObject) new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE).parse(body);
			String suserCode = "";
			LSuserMaster LSuserMasterobj = new LSuserMaster();
			LScfttransaction LScfttransactionobj = new LScfttransaction();
			if (jsonObj.containsKey("users")) {
				List<String> users = (List<String>) jsonObj.get("users");
				suserCode = users.get(0);
				LSuserMasterobj = LSuserMasterRepositoryObj.findByusercode(Integer.parseInt(suserCode));
				LScfttransactionobj = lscfttransactionRepository
						.findFirstByModuleNameAndLsuserMasterOrderBySerialnoDesc("Reports",
								LSuserMasterobj.getUsercode());
			}
			if ((int) jsonObj.get("status") == 2 || (int) jsonObj.get("status") == 4) {
				String filePath = getDocxAbsolutePath();
				String sKey = (String) jsonObj.get("key");
				String originalFilePath = "";
				String fileReceiver = env.getProperty("fileReceiver");
				if (fileReceiver != null) {
					HttpHeaders headers = new HttpHeaders();
					headers.setContentType(MediaType.MULTIPART_FORM_DATA);

					MultiValueMap<String, Object> reqBodyData = new LinkedMultiValueMap<>();
					reqBodyData.add("fileName", sKey);
					reqBodyData.add("type", 0); // LSDocReportsObj.getIsTemplate()

					HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(reqBodyData, headers);

					String serverUrl = fileReceiver + "delete/";
					RestTemplate restTemplate = new RestTemplate();
					ResponseEntity<String> fileDeleteRes = restTemplate.postForEntity(serverUrl, requestEntity,
							String.class);
					System.out.println("Response code: " + fileDeleteRes.getStatusCode());
				}
				LSdocreports LSDocReportsObj = LSdocreportsRepositoryObj.findFirstByFileHashNameAndStatus(sKey, 1);
				if (LSDocReportsObj != null) {
					if (LSDocReportsObj.getIsTemplate() == 1) {
						if (System.getProperty("os.name").contains("Linux")
								|| System.getProperty("os.name").contains("LINUX")) {
							filePath += "/templates";
						} else {
							filePath += "\\templates";
						}
						if (!(new File(filePath)).exists()) {
							(new File(filePath)).mkdir();
						}
						if (LScfttransactionobj != null)
							LScfttransactionobj
									.setComments("Saving document template : " + LSDocReportsObj.getFileName());
					} else {
						if (LScfttransactionobj != null)
							LScfttransactionobj.setComments("Saving document : " + LSDocReportsObj.getFileName());
					}

				}
				originalFilePath = filePath + '/' + sKey + ".docx";
				if (LSDocReportsObj.getFileName() == null && (int) jsonObj.get("status") == 2) {
					List<LSdocreports> LSDocReportsLst = LSdocreportsRepositoryObj.findByIsdraftAndStatus(1, 1);
					LSdocdirectory LSdocdirectoryObj = LSdocdirectoryRepositoryObj
							.findFirstByDirectorynameAndStatus("Draft Reports", 1);
					String name = "";
					if (LSDocReportsLst.size() > 0) {
						String[] temp = LSDocReportsLst.get(LSDocReportsLst.size() - 1).getFileName().split("_");
						name = "draft_" + (Integer.parseInt(temp[1]) + 1);
					} else {
						name = "draft_1";
					}
					LSDocReportsObj.setFileName(name);
					LSDocReportsObj.setIsdraft(1);
					if (LSdocdirectoryObj.getDocdirectorycode() != null)
						LSDocReportsObj.setDocdirectorycode(LSdocdirectoryObj.getDocdirectorycode());
				}
				if ((int) jsonObj.get("status") == 2) {
				
					String downloadUri = (String) jsonObj.get("url");
				
					InputStream stream;
					HttpsURLConnection connectionSSL = null;
					HttpURLConnection connection = null;
			
					if (downloadUri.contains("https")) {
						SSLContext sc = SSLContext.getInstance("SSL");
						sc.init(null, trustAllCerts, new java.security.SecureRandom());
						HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
						URL url = new URL(downloadUri);
						connectionSSL = (HttpsURLConnection) url.openConnection();
						stream = connectionSSL.getInputStream();
					} else {
						URL url = new URL(downloadUri);
						connection = (HttpURLConnection) url.openConnection();
						stream = connection.getInputStream();
					}

					if (!originalFilePath.isEmpty()) {
						
						if (LSDocReportsObj.getStreamid() != null) {
							if (!LSDocReportsObj.getStreamid().isEmpty())
								gridFsTemplate
										.delete(Query.query(Criteria.where("_id").is(LSDocReportsObj.getStreamid())));
						}
						if (LSDocReportsObj.getIsTemplate() == 1 && LSDocReportsObj.getIsmultiplesheet() == 0) {
							File savedFile = new File(originalFilePath);
							try (FileOutputStream out = new FileOutputStream(savedFile)) {
								int read;
								final byte[] bytes = new byte[1024];
								while ((read = stream.read(bytes)) != -1) {
									out.write(bytes, 0, read);
								}
								out.flush();
								out.close();
							}
							stream = new FileInputStream(originalFilePath);
							List<String> tagLst = new ArrayList<String>();
							XWPFDocument document = new XWPFDocument(stream);
							for (XWPFParagraph para : document.getParagraphs()) {
								tagLst.addAll(getTagsfromDocx(para));
							}
							for (XWPFTable tbl : document.getTables()) {
								for (XWPFTableRow row : tbl.getRows()) {
									for (XWPFTableCell cell : row.getTableCells()) {
										for (XWPFParagraph para : cell.getParagraphs()) {
											tagLst.addAll(getTagsfromDocx(para));
										}
									}
								}
							}
							int ismultisheet = 0;
							for (String tempTagStr : tagLst) {
								if (ismultisheet == 0 && tempTagStr.startsWith("<<$") && tempTagStr.endsWith("$>>")) {
									ismultisheet = 1;
									break;
								}
							}
							if (ismultisheet == 1) {
								LSDocReportsObj.setIsmultiplesheet(1);
							}
							stream.close();
							stream = new FileInputStream(originalFilePath);
						}
						Map<String, Object> FileInfo = new HashMap<String, Object>();
						FileInfo.put("Name", sKey);
						FileInfo.put("getInputStream", stream);
						Map<String, Object> FileStatus = uploadAndRetriveDoc(FileInfo, "upload");
						LSDocReportsObj.setStreamid((String) FileStatus.get("id"));
						LSdocreportsRepositoryObj.save(LSDocReportsObj);
						stream.close();
						if (downloadUri.contains("https")) {
							connectionSSL.disconnect();
						} else {
							connection.disconnect();
						}

						if (((String) FileStatus.get("status")).equals("success") && LScfttransactionobj != null) {
							File toBeDeleted = new File(originalFilePath);
						
							if (toBeDeleted.exists()) {
								toBeDeleted.delete();
							}
							lscfttransactionRepository.save(LScfttransactionobj);
						}
					}
				} else if ((int) jsonObj.get("status") == 4) {
					if (LSDocReportsObj != null) {
						if (LSDocReportsObj.getFileName() == null) {
							filePath = getDocxAbsolutePath();
							File toBeDeleted = new File(filePath, jsonObj.get("key") + ".docx");
							
							if (toBeDeleted.exists()) {
//								boolean FileStatus = 
										toBeDeleted.delete();
								LSdocreportsRepositoryObj.delete(LSDocReportsObj);
					
							}
						} else {
							if (LSDocReportsObj.getIsreport() == 1 && LSDocReportsObj.getStreamid() == null) {
								File newFile = new File(filePath, jsonObj.get("key") + ".docx");
								if (newFile.exists()) {
									InputStream fis = new FileInputStream(newFile);
									Map<String, Object> FileInfo = new HashMap<String, Object>();
									FileInfo.put("Name", sKey);
									FileInfo.put("getInputStream", fis);
									Map<String, Object> FileStatus = uploadAndRetriveDoc(FileInfo, "upload");
									LSDocReportsObj.setStreamid((String) FileStatus.get("id"));
									LSdocreportsRepositoryObj.save(LSDocReportsObj);
									if (((String) FileStatus.get("status")).equals("success")) {
										fis.close();
										newFile.delete();
									
									}
								}
							} else {
								File newFile = new File(filePath, jsonObj.get("key") + ".docx");
								if (newFile.exists()) {
									newFile.delete();
									
								} else {
								
								}
							}
						}
					}
				}
			} else {
				writer.write("{\"error\":0}");
			}
		} catch (Exception e) {
			e.printStackTrace();
		
		}
	}

	@SuppressWarnings({ "resource", "unchecked" })
	public void cloudsaveDocxsReport(HttpServletRequest request, HttpServletResponse response) throws IOException {
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(request.getInputStream()).useDelimiter("\\A");
		String body = scanner.hasNext() ? scanner.next() : "";
		JSONObject jsonObj = null;
		PrintWriter writer = response.getWriter();
		try {
			jsonObj = (JSONObject) new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE).parse(body);
			String suserCode = "";
			LSuserMaster LSuserMasterobj = new LSuserMaster();
			LScfttransaction LScfttransactionobj = new LScfttransaction();
			if (jsonObj.containsKey("users")) {
				List<String> users = (List<String>) jsonObj.get("users");
				suserCode = users.get(0);
				LSuserMasterobj = LSuserMasterRepositoryObj.findByusercode(Integer.parseInt(suserCode));
				LScfttransactionobj = lscfttransactionRepository
						.findFirstByModuleNameAndLsuserMasterOrderBySerialnoDesc("Reports",
								LSuserMasterobj.getUsercode());
			}
			if ((int) jsonObj.get("status") == 2 || (int) jsonObj.get("status") == 4) {
				String filePath = getDocxAbsolutePath();
				String sKey = (String) jsonObj.get("key");
				String originalFilePath = "";
				String fileReceiver = env.getProperty("fileReceiver");
				if (fileReceiver != null) {
					HttpHeaders headers = new HttpHeaders();
					headers.setContentType(MediaType.MULTIPART_FORM_DATA);

					MultiValueMap<String, Object> reqBodyData = new LinkedMultiValueMap<>();
					reqBodyData.add("fileName", sKey);
					reqBodyData.add("type", 0); // LSDocReportsObj.getIsTemplate()

					HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(reqBodyData, headers);

					String serverUrl = fileReceiver + "delete/";
					RestTemplate restTemplate = new RestTemplate();
					ResponseEntity<String> fileDeleteRes = restTemplate.postForEntity(serverUrl, requestEntity,
							String.class);
					System.out.println("Response code: " + fileDeleteRes.getStatusCode());
				}
				LSdocreports LSDocReportsObj = LSdocreportsRepositoryObj.findFirstByFileHashNameAndStatus(sKey, 1);
				if (LSDocReportsObj != null) {
					if (LSDocReportsObj.getIsTemplate() == 1) {
						if (System.getProperty("os.name").contains("Linux")
								|| System.getProperty("os.name").contains("LINUX")) {
							filePath += "/templates";
						} else {
							filePath += "\\templates";
						}
						if (!(new File(filePath)).exists()) {
							(new File(filePath)).mkdir();
						}
						if (LScfttransactionobj != null)
							LScfttransactionobj
									.setComments("Saving document template : " + LSDocReportsObj.getFileName());
					} else {
						if (LScfttransactionobj != null)
							LScfttransactionobj.setComments("Saving document : " + LSDocReportsObj.getFileName());
					}

					originalFilePath = filePath + "/" + sKey + ".docx";
					if (LSDocReportsObj.getFileName() == null && (int) jsonObj.get("status") == 2) {
						List<LSdocreports> LSDocReportsLst = LSdocreportsRepositoryObj.findByIsdraftAndStatus(1, 1);
						LSdocdirectory LSdocdirectoryObj = LSdocdirectoryRepositoryObj
								.findFirstByDirectorynameAndStatus("Draft Reports", 1);
						String name = "";
						if (LSDocReportsLst.size() > 0) {
							String[] temp = LSDocReportsLst.get(LSDocReportsLst.size() - 1).getFileName().split("_");
							name = "draft_" + (Integer.parseInt(temp[1]) + 1);
						} else {
							name = "draft_1";
						}
						LSDocReportsObj.setFileName(name);
						LSDocReportsObj.setIsdraft(1);
						if (LSdocdirectoryObj.getDocdirectorycode() != null)
							LSDocReportsObj.setDocdirectorycode(LSdocdirectoryObj.getDocdirectorycode());
					}
					if ((int) jsonObj.get("status") == 2) {
					
						String downloadUri = (String) jsonObj.get("url");
					
						InputStream stream;
						HttpsURLConnection connectionSSL = null;
						HttpURLConnection connection = null;
						
						if (downloadUri.contains("https")) {
							SSLContext sc = SSLContext.getInstance("SSL");
							sc.init(null, trustAllCerts, new java.security.SecureRandom());
							HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
							URL url = new URL(downloadUri);
							connectionSSL = (HttpsURLConnection) url.openConnection();
							stream = connectionSSL.getInputStream();
						} else {
							URL url = new URL(downloadUri);
							connection = (HttpURLConnection) url.openConnection();
							stream = connection.getInputStream();
						}

						if (!originalFilePath.isEmpty()) {
							
							if (LSDocReportsObj.getStreamid() != null) {
								if (!LSDocReportsObj.getStreamid().isEmpty())
									// gridFsTemplate.delete(Query.query(Criteria.where("_id").is(LSDocReportsObj.getStreamid())));
									CloudFileManipulationservice.deleteReportFile(LSDocReportsObj.getStreamid());
							}
							File savedFile = new File(originalFilePath);
							if (LSDocReportsObj.getIsTemplate() == 1 && LSDocReportsObj.getIsmultiplesheet() == 0) {
								try (FileOutputStream out = new FileOutputStream(savedFile)) {
									int read;
									final byte[] bytes = new byte[1024];
									while ((read = stream.read(bytes)) != -1) {
										out.write(bytes, 0, read);
									}
									out.flush();
									out.close();
								}
								stream = new FileInputStream(originalFilePath);
								List<String> tagLst = new ArrayList<String>();
								XWPFDocument document = new XWPFDocument(stream);
								for (XWPFParagraph para : document.getParagraphs()) {
									tagLst.addAll(getTagsfromDocx(para));
								}
								for (XWPFTable tbl : document.getTables()) {
									for (XWPFTableRow row : tbl.getRows()) {
										for (XWPFTableCell cell : row.getTableCells()) {
											for (XWPFParagraph para : cell.getParagraphs()) {
												tagLst.addAll(getTagsfromDocx(para));
											}
										}
									}
								}
								int ismultisheet = 0;
								for (String tempTagStr : tagLst) {
									if (ismultisheet == 0 && tempTagStr.startsWith("<<$")
											&& tempTagStr.endsWith("$>>")) {
										ismultisheet = 1;
										break;
									}
								}
								if (ismultisheet == 1) {
									LSDocReportsObj.setIsmultiplesheet(1);
								}
							}
							stream.close();
							String streamId = CloudFileManipulationservice.storeReportFile(sKey, savedFile);
							LSDocReportsObj.setStreamid(streamId);
							// Map<String, Object> FileInfo = new HashMap<String, Object>();
							// FileInfo.put("Name", sKey);
							// FileInfo.put("getInputStream", stream);
							// Map<String, Object> FileStatus = uploadAndRetriveDoc(FileInfo, "upload");
							// LSDocReportsObj.setStreamid((String) FileStatus.get("id"));
							// CloudFileManipulationservice.storeReportFile(sKey, savedFile)
							LSdocreportsRepositoryObj.save(LSDocReportsObj);
							// stream.close();
							if (downloadUri.contains("https")) {
								connectionSSL.disconnect();
							} else {
								connection.disconnect();
							}

							if (!LSDocReportsObj.getStreamid().equals("") && LScfttransactionobj != null) {
								File toBeDeleted = new File(originalFilePath);
						
								if (toBeDeleted.exists()) {
									toBeDeleted.delete();
								}
								lscfttransactionRepository.save(LScfttransactionobj);
							}
						}
					} else if ((int) jsonObj.get("status") == 4) {
						if (LSDocReportsObj != null) {
							if (LSDocReportsObj.getFileName() == null) {
								filePath = getDocxAbsolutePath();
								File toBeDeleted = new File(filePath, jsonObj.get("key") + ".docx");
							
								if (toBeDeleted.exists()) {
//									boolean FileStatus = 
											toBeDeleted.delete();
									LSdocreportsRepositoryObj.delete(LSDocReportsObj);
								
								}
							} else {
								if (LSDocReportsObj.getIsreport() == 1 && LSDocReportsObj.getStreamid() == null) {
									File newFile = new File(filePath, jsonObj.get("key") + ".docx");
									if (newFile.exists()) {
										InputStream fis = new FileInputStream(newFile);
										Map<String, Object> FileInfo = new HashMap<String, Object>();
										FileInfo.put("Name", sKey);
										FileInfo.put("getInputStream", fis);
										Map<String, Object> FileStatus = uploadAndRetriveDoc(FileInfo, "upload");
										LSDocReportsObj.setStreamid((String) FileStatus.get("id"));
										LSdocreportsRepositoryObj.save(LSDocReportsObj);
										if (((String) FileStatus.get("status")).equals("success")) {
											fis.close();
											newFile.delete();
										
										}
									}
								} else {
									File newFile = new File(filePath, jsonObj.get("key") + ".docx");
									if (newFile.exists()) {
										newFile.delete();
									
									} else {
										
									}
								}
							}
						}
					}
				}
			} else {
				writer.write("{\"error\":0}");
			}
		} catch (Exception e) {
			e.printStackTrace();
		
		}
	}

//	public Map<String, Object> uploadDocxFile(MultipartHttpServletRequest request, HttpServletResponse response)
	@SuppressWarnings("unchecked")
	public Map<String, Object> uploadDocxFile(MultipartHttpServletRequest request, Map<String, Object> argObj)
			throws IOException {
		Map<String, Object> map = new HashMap<>();
//		MultipartHttpServletRequest request = (MultipartHttpServletRequest) argObj.get("file");
		if (request != null) {
			boolean isMultipart = ServletFileUpload.isMultipartContent(request);
			Iterator<String> itrator = request.getFileNames();
			MultipartFile multiFile = request.getFile(itrator.next());
			String uniquefilename = UUID.randomUUID().toString();
			if (isMultipart) {
				
				System.out.println("File Length:" + multiFile.getBytes().length);
				System.out.println("File Type:" + multiFile.getContentType());
				String fileName = multiFile.getOriginalFilename();
				System.out.println("File Name:" + fileName);
				byte[] bytes = multiFile.getBytes();

				String filePath = getDocxAbsolutePath();
				File directory = new File(filePath);
				if (!directory.exists()) {
					directory.mkdirs();
				}
				File file = new File(
						directory.getAbsolutePath() + System.getProperty("file.separator") + uniquefilename + ".docx");
				if (file.exists()) {
					file.delete();
					BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(file));
					stream.write(bytes);
					stream.close();
				} else {
					BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(file));
					stream.write(bytes);
					stream.close();
				}

				int pos = fileName.lastIndexOf(".");
				String fileNameTemp = fileName.substring(0, pos);

				LSdocreports LSDocReportobj = new LSdocreports();
				LSDocReportobj.setExtention("docx");
				LSDocReportobj.setFileName(fileNameTemp);
				LSDocReportobj.setFileHashName(uniquefilename);
				LSDocReportobj.setIsTemplate(0);
				LSDocReportobj.setStatus(1);
				LSDocReportobj.setDocdirectorycode(0);
				
//				hibernateInsertSingleTable(LSDocReportobj);
				LSdocreportsRepositoryObj.save(LSDocReportobj);
				if (argObj.containsKey("objsilentaudit")) {
					if (argObj.get("objsilentaudit") != null) {
						LScfttransaction LScfttransactionobj = new ObjectMapper()
								.convertValue(argObj.get("objsilentaudit"), new TypeReference<LScfttransaction>() {
								});
						LScfttransactionobj.setSystemcoments("System Generated");
						LScfttransactionobj.setTableName("LSdocreports");
						lscfttransactionRepository.save(LScfttransactionobj);
					}
				}
				if (argObj.containsKey("objmanualaudit")) {
					if (argObj.get("objmanualaudit") != null) {
						LScfttransaction LScfttransactionManualobj = new ObjectMapper()
								.convertValue(argObj.get("objmanualaudit"), new TypeReference<LScfttransaction>() {
								});
						if (argObj.containsKey("objuser")) {
							Map<String, Object> objuser = (Map<String, Object>) argObj.get("objuser");
							LScfttransactionManualobj.setComments((String) objuser.get("comments"));
						}
						LScfttransactionManualobj.setTableName("LSdocreports");
						lscfttransactionRepository.save(LScfttransactionManualobj);
					}
				}
				if (env.getProperty("fileReceiver") != null) {
					int httpfileStatus = uploadSingleFile(file.getAbsolutePath(), 0);
					if (httpfileStatus == 200) {
						map.put("fileFullPath", "");
					} else {
						map.put("fileFullPath", file.getAbsolutePath());
					}
				} else {
					map.put("fileFullPath", file.getAbsolutePath());
				}
				map.put("rtnStatus", "Success");
				map.put("fileFullPath", file.getAbsolutePath());
				map.put("fileName", fileName);
				map.put("fileOriginalPath", "reports/" + uniquefilename + "." + "docx");
				map.put("hashKey", uniquefilename);

			}
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> updateReportDocxName(Map<String, Object> obj) {
		Map<String, Object> rtnobjMap = new HashMap<String, Object>();
		try {
//			List<LSdocreports> LSDocReportsCheckLst = LSdocreportsRepositoryObj.findAllByFileNameAndStatus((String) obj.get("fileName"), 1);
			List<LSdocreports> LSDocReportsCheckLst = LSdocreportsRepositoryObj
					.findAllByFileNameAndDocdirectorycodeAndStatus((String) obj.get("fileName"),
							(int) obj.get("docdirectorycode"), 1);
			if (LSDocReportsCheckLst.size() > 0) {
				rtnobjMap.put("status", "already exist");
			} else {
				LSdocreports LSDocReportObj = LSdocreportsRepositoryObj
						.findFirstByFileHashNameAndStatus((String) obj.get("fileHashName"), 1);
				String NewFIleName = (String) obj.get("fileName");
//				String oldFIleHashName = "";
				String haskKey = LSDocReportObj.getFileHashName();
				boolean savedSuccessfully = false;
				LScfttransaction LScfttransactionobj = new LScfttransaction();
				if (obj.containsKey("objsilentaudit")) {
					if (obj.get("objsilentaudit") != null) {
						LScfttransactionobj = new ObjectMapper().convertValue(obj.get("objsilentaudit"),
								new TypeReference<LScfttransaction>() {
								});
					}
				}
			
				File newFile;
				if (LSDocReportObj != null) {
//					oldFIleHashName = LSDocReportObj.getFileHashName();
					String filePath = getDocxAbsolutePath();
					String saveAsHashKey = UUID.randomUUID().toString();
					if (obj.containsKey("SaveAs")) {
						LSdocreports LSDocReportObj1 = new LSdocreports();
						LSDocReportObj1.setExtention(LSDocReportObj.getExtention());
						LSDocReportObj1.setFileHashName(haskKey);
						LSDocReportObj1.setDocdirectorycode((int) obj.get("docdirectorycode"));
						LSDocReportObj1.setFileName((String) obj.get("fileName"));
						LSDocReportObj1.setSheetfilecodeString(LSDocReportObj.getSheetfilecodeString());
						LSDocReportObj1.setIsTemplate(LSDocReportObj.getIsTemplate());
						LSDocReportObj1.setStatus(1);
						LSDocReportObj1.setVersionno(1);
						LSDocReportObj1.setIsdraft(0);
						LSDocReportObj1.setCreatedBy(LScfttransactionobj.getLsuserMaster());
						LSDocReportObj1.setLssitemaster(LScfttransactionobj.getLssitemaster());
						LSDocReportObj1.setCreatedate(new Date());
						LSdocreportsRepositoryObj.save(LSDocReportObj1);
						
						LSDocReportObj.setFileHashName(saveAsHashKey);
						LSdocreportsRepositoryObj.save(LSDocReportObj);
						if (LSDocReportObj.getIsTemplate() == 1) {
							if (System.getProperty("os.name").contains("Linux")
									|| System.getProperty("os.name").contains("LINUX")) {
								filePath += "/templates";
							} else {
								filePath += "\\templates";
							}
						}
						File oldFile = new File(filePath, haskKey + ".docx");
						newFile = new File(filePath, saveAsHashKey + ".docx");
						FileInputStream fis = new FileInputStream(oldFile);
						FileOutputStream fos = new FileOutputStream(newFile);
						XWPFDocument document = new XWPFDocument(fis);
						document.write(fos);
						document.close();
						fis.close();
						fos.close();
						newFile.delete();
						savedSuccessfully = true;
					} else {
						LSDocReportObj.setIsdraft(0);
						LSDocReportObj.setFileName((String) obj.get("fileName"));
						LSDocReportObj.setDocdirectorycode((int) obj.get("docdirectorycode"));
					
						LSdocreportsRepositoryObj.save(LSDocReportObj);
						savedSuccessfully = true;
						newFile = new File(filePath, haskKey + ".docx");
					}
					if (savedSuccessfully) {
//						if (obj.containsKey("objsilentaudit")) {
//							if (obj.get("objsilentaudit") != null) {
////								LScfttransaction LScfttransactionobj = new ObjectMapper()
////										.convertValue(obj.get("objsilentaudit"), new TypeReference<LScfttransaction>() {
////										});
//								LScfttransactionobj.setSystemcoments("System Generated");
//								LScfttransactionobj.setTableName("LSdocreports");
//								lscfttransactionRepository.save(LScfttransactionobj);
//							}
//						}
						if (obj.containsKey("objmanualaudit")) {
							if (obj.get("objmanualaudit") != null) {
								LScfttransaction LScfttransactionManualobj = new ObjectMapper()
										.convertValue(obj.get("objmanualaudit"), new TypeReference<LScfttransaction>() {
										});
								if (obj.containsKey("objuser")) {
									Map<String, Object> objuser = (Map<String, Object>) obj.get("objuser");
									LScfttransactionManualobj.setComments((String) objuser.get("comments"));
								}
								LScfttransactionManualobj.setTableName("LSdocreports");
								//lscfttransactionRepository.save(LScfttransactionManualobj);
							}
						}

						Map<String, Object> objMap = new HashMap<String, Object>();
						objMap.put("fileFullPath", newFile.getAbsolutePath());
						objMap.put("fileName", NewFIleName + ".docx");
						objMap.put("fileOriginalPath", "reports/" + haskKey + ".docx");
						objMap.put("hashKey", haskKey);
						rtnobjMap.put("newDocx", objMap);
						rtnobjMap.putAll(getDocxDirectoryLst());
						rtnobjMap.putAll(getLSdocreportsLst("all"));
						rtnobjMap.put("status", "success");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		return rtnobjMap;
	}

	public Map<String, Object> getLSdocreportsLst(String type) {
		Map<String, Object> objMap = new HashMap<String, Object>();
		try {
			List<LSdocreports> LSdocreports = new ArrayList<LSdocreports>();
			if (type.equals("all")) {
				LSdocreports = LSdocreportsRepositoryObj.findByStatus(1);
			} else if (type.equals("isTemplate")) {
				LSdocreports = LSdocreportsRepositoryObj.findByIsTemplateAndStatus(1, 1);
			}
			objMap.put("LSDocReportsLst", LSdocreports);
		
		} catch (Exception e) {
			
		}
		return objMap;
	}

	public Map<String, Object> getLSdocreportsLst(String type, LScfttransaction LScfttransactionobj) {
		Map<String, Object> objMap = new HashMap<String, Object>();
		try {
			List<LSdocreports> LSdocreports = new ArrayList<LSdocreports>();
			if (type.equals("all")) {
//				LSdocreports = LSdocreportsRepositoryObj.findByStatus(1);
				LSdocreports = LSdocreportsRepositoryObj.findByStatusAndLssitemaster(1,
						LScfttransactionobj.getLssitemaster());
			} else if (type.equals("isTemplate")) {
//				LSdocreports = LSdocreportsRepositoryObj.findByIsTemplateAndStatus(1, 1);
				LSdocreports = LSdocreportsRepositoryObj.findByIsTemplateAndStatusAndLssitemaster(1, 1,
						LScfttransactionobj.getLssitemaster());
			}
			objMap.put("LSDocReportsLst", LSdocreports);
			
		} catch (Exception e) {
			
		}
		return objMap;
	}

	public Map<String, Object> getDocxDirectoryLst() {
		Map<String, Object> objMap = new HashMap<String, Object>();
		try {
			List<LSdocdirectory> LSdocdirectoryLst = LSdocdirectoryRepositoryObj.findByStatus(1);
//			List<LSdocdirectory> LSdocdirectoryLst = LSdocdirectoryRepositoryObj.findByStatusAndDirectorynameIsNotNULL(1);
//			if (LSdocdirectoryLst.size() <= 2) {
//				LSdocdirectory LSdocdirectoryObj = new LSdocdirectory();
//				LSdocdirectoryObj = LSdocdirectoryRepositoryObj.findFirstByDirectorynameAndStatus("root", 1);
//				if(LSdocdirectoryObj == null) {
//					LSdocdirectoryObj.setDocdirectorycode(1);
//					LSdocdirectoryObj.setDirectoryname("root");
//					LSdocdirectoryObj.setDirectorytype(0);
//					LSdocdirectoryObj.setParentdirectory(null);
//					LSdocdirectoryObj.setStatus(1);
//					LSdocdirectoryRepositoryObj.save(LSdocdirectoryObj);
//				}
//				LSdocdirectoryLst.add(LSdocdirectoryObj);
//				LSdocdirectoryObj = new LSdocdirectory();
//				LSdocdirectoryObj = LSdocdirectoryRepositoryObj.findFirstByDirectorynameAndStatus("Generated Reports", 1);
//				if(LSdocdirectoryObj == null) {
//					LSdocdirectoryObj.setDocdirectorycode(2);
//					LSdocdirectoryObj.setDirectoryname("Generated Reports");
//					LSdocdirectoryObj.setDirectorytype(1);
//					LSdocdirectoryObj.setParentdirectory(1);
//					LSdocdirectoryObj.setStatus(1);
//					LSdocdirectoryRepositoryObj.save(LSdocdirectoryObj);
//				}
//				LSdocdirectoryLst.add(LSdocdirectoryObj);
//				LSdocdirectoryObj = new LSdocdirectory();
//				LSdocdirectoryObj = LSdocdirectoryRepositoryObj.findFirstByDirectorynameAndStatus("Draft Reports", 1);
//				if(LSdocdirectoryObj == null) {
//					LSdocdirectoryObj.setDocdirectorycode(3);
//					LSdocdirectoryObj.setDirectoryname("Draft Reports");
//					LSdocdirectoryObj.setDirectorytype(1);
//					LSdocdirectoryObj.setParentdirectory(1);
//					LSdocdirectoryObj.setStatus(1);
//					LSdocdirectoryRepositoryObj.save(LSdocdirectoryObj);
//				}
//				LSdocdirectoryLst.add(LSdocdirectoryObj);
//				LSdocdirectoryObj = LSdocdirectoryRepositoryObj.findFirstByDirectorynameAndStatus("My Space", 1);
//				if(LSdocdirectoryObj == null) {
//					LSdocdirectoryObj.setDocdirectorycode(4);
//					LSdocdirectoryObj.setDirectoryname("My Space");
//					LSdocdirectoryObj.setDirectorytype(1);
//					LSdocdirectoryObj.setParentdirectory(1);
//					LSdocdirectoryObj.setStatus(1);
//					LSdocdirectoryRepositoryObj.save(LSdocdirectoryObj);
//				}
//				LSdocdirectoryLst.add(LSdocdirectoryObj);
//				LSdocdirectoryObj = LSdocdirectoryRepositoryObj.findFirstByDirectorynameAndStatus("Team Space", 1);
//				if(LSdocdirectoryObj == null) {
//					LSdocdirectoryObj.setDocdirectorycode(5);
//					LSdocdirectoryObj.setDirectoryname("Team Space");
//					LSdocdirectoryObj.setDirectorytype(1);
//					LSdocdirectoryObj.setParentdirectory(1);
//					LSdocdirectoryObj.setStatus(1);
//					LSdocdirectoryRepositoryObj.save(LSdocdirectoryObj);
//				}
//				LSdocdirectoryLst.add(LSdocdirectoryObj);
//			}
			objMap.put("LSDocDirectoryLst", LSdocdirectoryLst);
			
		} catch (Exception e) {
			
		}
		return objMap;
	}

	public Map<String, Object> getDocxDirectoryLst(LScfttransaction LScfttransactionobj) {
		Map<String, Object> objMap = new HashMap<String, Object>();
		try {
			List<LSdocdirectory> LSdocdirectoryLst = LSdocdirectoryRepositoryObj.findByStatusAndLssitemaster(1,
					LScfttransactionobj.getLssitemaster());

			Integer Defaultsite = 1;
			if (LScfttransactionobj.getLssitemaster() != Defaultsite) {
				LSdocdirectoryLst.add(0, LSdocdirectoryRepositoryObj.findByDocdirectorycode(1));
				LSdocdirectoryLst.add(1, LSdocdirectoryRepositoryObj.findByDocdirectorycode(2));
				LSdocdirectoryLst.add(2, LSdocdirectoryRepositoryObj.findByDocdirectorycode(3));
				LSdocdirectoryLst.add(3, LSdocdirectoryRepositoryObj.findByDocdirectorycode(4));
				LSdocdirectoryLst.add(4, LSdocdirectoryRepositoryObj.findByDocdirectorycode(5));
			}

			objMap.put("LSDocDirectoryLst", LSdocdirectoryLst);
			
		} catch (Exception e) {
		
		}
		return objMap;
	}

	public Map<String, Object> addDocxDirectory(Map<String, Object> objMap) {
		Map<String, Object> rtnobjMap = new HashMap<String, Object>();
		try {
			LScfttransaction LScfttransactionobj = new LScfttransaction();
			if (objMap.containsKey("objsilentaudit")) {
				if (objMap.get("objsilentaudit") != null) {
					LScfttransactionobj = new ObjectMapper().convertValue(objMap.get("objsilentaudit"),
							new TypeReference<LScfttransaction>() {
							});
				}
			}
			List<LSdocdirectory> lSdocdirectoyLst = new ArrayList<LSdocdirectory>();
			if ((int) objMap.get("parentdirectory") == 2 || (int) objMap.get("parentdirectory") == 3
					|| (int) objMap.get("parentdirectory") == 4 || (int) objMap.get("parentdirectory") == 5) {
				lSdocdirectoyLst = LSdocdirectoryRepositoryObj
						.findByDirectorynameAndParentdirectoryAndCreatedbyAndStatus(
								(String) objMap.get("directoryname"), (int) objMap.get("parentdirectory"),
								LScfttransactionobj.getLsuserMaster(), 1);
			} else {
				lSdocdirectoyLst = LSdocdirectoryRepositoryObj.findByDirectorynameAndParentdirectoryAndStatus(
						(String) objMap.get("directoryname"), (int) objMap.get("parentdirectory"), 1);
			}
			if (lSdocdirectoyLst.size() > 0) {
				rtnobjMap.put("status", "already exist");
			} else {
//				LScfttransaction LScfttransactionobj = new LScfttransaction();
//				if (objMap.containsKey("objsilentaudit")) {
//					if (objMap.get("objsilentaudit") != null) {
//						LScfttransactionobj = new ObjectMapper().convertValue(objMap.get("objsilentaudit"),
//								new TypeReference<LScfttransaction>() {
//								});
//					}
//				}
				LSdocdirectory ObjLSDocDirectory = new LSdocdirectory();
				ObjLSDocDirectory.setDirectorytype((int) objMap.get("directorytype"));
				ObjLSDocDirectory.setDirectoryname((String) objMap.get("directoryname"));
				ObjLSDocDirectory.setParentdirectory((int) objMap.get("parentdirectory"));
				ObjLSDocDirectory.setStatus(1);
				ObjLSDocDirectory.setCreatedate(new Date());
				ObjLSDocDirectory.setCreatedby(LScfttransactionobj.getLsuserMaster());
				ObjLSDocDirectory.setLssitemaster(LScfttransactionobj.getLssitemaster());
				
				LSdocdirectoryRepositoryObj.save(ObjLSDocDirectory);
				rtnobjMap.put("status", "success");

				if (objMap.containsKey("objmanualaudit")) {
					if (objMap.get("objmanualaudit") != null) {
						LScfttransaction LScfttransactionManualobj = new ObjectMapper()
								.convertValue(objMap.get("objmanualaudit"), new TypeReference<LScfttransaction>() {
								});
						if (objMap.containsKey("objuser")) {
							@SuppressWarnings("unchecked")
							Map<String, Object> objuser = (Map<String, Object>) objMap.get("objuser");
							LScfttransactionManualobj.setComments((String) objuser.get("comments"));
						}
						LScfttransactionManualobj.setTableName("LSdocreports");
						//lscfttransactionRepository.save(LScfttransactionManualobj);
					}
				}
				if (LScfttransactionobj != null) {
//					LScfttransaction LScfttransactionobj = new ObjectMapper().convertValue(objMap.get("objsilentaudit"),new TypeReference<LScfttransaction>() {});
					// LScfttransactionobj.setModuleName("Reports");
					// LScfttransactionobj.setComments("New Directory created: "+(String)
					// objMap.get("directoryname"));
					// LScfttransactionobj.setActions("Creation");
					LScfttransactionobj.setSystemcoments("System Generated");
					LScfttransactionobj.setTableName("LSdocdirectory");
					lscfttransactionRepository.save(LScfttransactionobj);
				}
			}
			rtnobjMap.putAll(getDocxDirectoryLst());
			rtnobjMap.putAll(getLSdocreportsLst("all"));
		} catch (Exception e) {
			
		}
		return rtnobjMap;
	}

	public Map<String, Object> updateDocxDirectory(Map<String, Object> objMap) {
		Map<String, Object> rtnobjMap = new HashMap<String, Object>();
		try {
			LScfttransaction LScfttransactionobj = new LScfttransaction();
			if (objMap.containsKey("objsilentaudit")) {
				if (objMap.get("objsilentaudit") != null) {
					LScfttransactionobj = new ObjectMapper().convertValue(objMap.get("objsilentaudit"),
							new TypeReference<LScfttransaction>() {
							});
				}
			}
			@SuppressWarnings("unchecked")
			Map<String, Object> tempObj = (Map<String, Object>) objMap.get("rtnObj");
			tempObj.remove("showData");
			tempObj.remove("indent");
			LSdocdirectory ObjLSDocDirectory = new ObjectMapper().convertValue(tempObj,
					new TypeReference<LSdocdirectory>() {
					});
			
			LSdocdirectoryRepositoryObj.save(ObjLSDocDirectory);
			rtnobjMap.put("status", "success");

			if (objMap.containsKey("objmanualaudit")) {
				if (objMap.get("objmanualaudit") != null) {
					LScfttransaction LScfttransactionManualobj = new ObjectMapper()
							.convertValue(objMap.get("objmanualaudit"), new TypeReference<LScfttransaction>() {
							});
					if (objMap.containsKey("objuser")) {
						@SuppressWarnings("unchecked")
						Map<String, Object> objuser = (Map<String, Object>) objMap.get("objuser");
						LScfttransactionManualobj.setComments((String) objuser.get("comments"));
					}
					LScfttransactionManualobj.setTableName("LSdocdirectory");
					lscfttransactionRepository.save(LScfttransactionManualobj);
				}
			}
			if (LScfttransactionobj != null) {
				LScfttransactionobj.setSystemcoments("System Generated");
				LScfttransactionobj.setTableName("LSdocdirectory");
				lscfttransactionRepository.save(LScfttransactionobj);
			}
			rtnobjMap.putAll(getDocxDirectoryLst());
			rtnobjMap.putAll(getLSdocreportsLst("all"));
		} catch (Exception e) {
			
		}
		return rtnobjMap;
	}

	public Map<String, Object> renameDeleteDocxDirectory(Map<String, Object> objMap) {
		Map<String, Object> rtnobjMap = new HashMap<String, Object>();
		try {
			LScfttransaction LScfttransactionobj = new LScfttransaction();
			if (objMap.containsKey("objsilentaudit")) {
				if (objMap.get("objsilentaudit") != null) {
					LScfttransactionobj = new ObjectMapper().convertValue(objMap.get("objsilentaudit"),
							new TypeReference<LScfttransaction>() {
							});
				}
			}
			@SuppressWarnings("unchecked")
			Map<String, Object> tempObj = (Map<String, Object>) objMap.get("rtnObj");
			tempObj.remove("showData");
			tempObj.remove("indent");
			LSdocreports ObjLSdocreports = new ObjectMapper().convertValue(tempObj, new TypeReference<LSdocreports>() {
			});
			
			LSdocreportsRepositoryObj.save(ObjLSdocreports);
			rtnobjMap.put("status", "success");

			if (objMap.containsKey("objmanualaudit")) {
				if (objMap.get("objmanualaudit") != null) {
					LScfttransaction LScfttransactionManualobj = new ObjectMapper()
							.convertValue(objMap.get("objmanualaudit"), new TypeReference<LScfttransaction>() {
							});
					if (objMap.containsKey("objuser")) {
						@SuppressWarnings("unchecked")
						Map<String, Object> objuser = (Map<String, Object>) objMap.get("objuser");
						LScfttransactionManualobj.setComments((String) objuser.get("comments"));
					}
					LScfttransactionManualobj.setTableName("LSdocreports");
					lscfttransactionRepository.save(LScfttransactionManualobj);
				}
			}
			if (LScfttransactionobj != null) {
				LScfttransactionobj.setSystemcoments("System Generated");
				LScfttransactionobj.setTableName("ObjLSdocreports");
				lscfttransactionRepository.save(LScfttransactionobj);
			}
			rtnobjMap.putAll(getDocxDirectoryLst());
			rtnobjMap.putAll(getLSdocreportsLst("all"));
		} catch (Exception e) {
			
		}
		return rtnobjMap;
	}

	public Map<String, Object> getReportDocxInfo(Map<String, Object> objMap) {
		Map<String, Object> rtnobjMap = new HashMap<String, Object>();
		try {
			LSdocreports lSdocreportsObj = LSdocreportsRepositoryObj
					.findByDocReportsCode((int) objMap.get("docReportsCode"));
			List<LSdocreportsversionhistory> LSdocreportsversionhistorylst = LSdocreportsversionHistoryRepositoryObj
					.findAllByDocReportsCodeAndStatus(lSdocreportsObj.getDocReportsCode(), 1);
			@SuppressWarnings("unused")
			Map<String, Object> ftpstatus = null;
//			boolean canLoad = false;
			String FileName = lSdocreportsObj.getFileName();
			String HashKey = lSdocreportsObj.getFileHashName();

//			if (canLoad) {
			String filePath = getDocxAbsolutePath();
			if (lSdocreportsObj.getIsTemplate() == 1 && !isftpAvailable()) {
				if (System.getProperty("os.name").contains("Linux")
						|| System.getProperty("os.name").contains("LINUX")) {
					filePath += "/templates";
				} else {
					filePath += "\\templates";
				}
			}
			boolean filePresent = false;
			File directory = new File(filePath);
			if (directory.exists()) {
				File requestedFile = new File(filePath, HashKey + ".docx");
				if (!requestedFile.exists()) {
					Map<String, Object> fileInfo = new HashMap<String, Object>();
					fileInfo.put("id", lSdocreportsObj.getStreamid());
					fileInfo.put("filePath", requestedFile);
					Map<String, Object> fileStatus = uploadAndRetriveDoc(fileInfo, "retrive");
					if (fileStatus.get("status") == "success") {
						filePresent = true;
					} else {
						filePresent = false;
					}
				} else {
					filePresent = true;
				}
				if (filePresent) {
					if (env.getProperty("fileReceiver") != null) {
						int httpfileStatus = uploadSingleFile(requestedFile.getAbsolutePath(),
								lSdocreportsObj.getIsTemplate());
						if (httpfileStatus == 200) {
							rtnobjMap.put("fileFullPath", "");
						} else {
							rtnobjMap.put("fileFullPath", requestedFile.getAbsolutePath());
						}
					} else {
						rtnobjMap.put("fileFullPath", requestedFile.getAbsolutePath());
					}
					// lSdocreportsObj.setFileHashName(HashKey);
					if (LSdocreportsversionhistorylst.size() > 0) {
						lSdocreportsObj.setVersionno(LSdocreportsversionhistorylst
								.get(LSdocreportsversionhistorylst.size() - 1).getVersionNo());
					} else {
						lSdocreportsObj.setVersionno(1);
					}
					LSdocreportsRepositoryObj.save(lSdocreportsObj);
					rtnobjMap.put("status", "success");
//						rtnobjMap.put("fileFullPath", requestedFile.getAbsolutePath());
					rtnobjMap.put("fileName", FileName + ".docx");
					if (lSdocreportsObj.getIsTemplate() == 1 && !isftpAvailable()) {
						rtnobjMap.put("fileOriginalPath", "reports/templates/" + HashKey + "." + "docx");
					} else {
						rtnobjMap.put("fileOriginalPath", "reports/" + HashKey + "." + "docx");
					}
					rtnobjMap.put("hashKey", HashKey);
					rtnobjMap.put("isdraft", lSdocreportsObj.getIsdraft());
					rtnobjMap.put("LSdocreportsversionhistorylst", LSdocreportsversionhistorylst);
					if (objMap.containsKey("objsilentaudit")) {
						if (objMap.get("objsilentaudit") != null) {
							LScfttransaction LScfttransactionobj = new ObjectMapper()
									.convertValue(objMap.get("objsilentaudit"), new TypeReference<LScfttransaction>() {
									});
							LScfttransactionobj.setSystemcoments("System Generated");
							LScfttransactionobj.setTableName("LSdocreports");
							lscfttransactionRepository.save(LScfttransactionobj);
						}
					}
				} else {
					rtnobjMap.put("status", "ID_FILENOTFOUNDEITHERDELETEDORMOVED");
				}
			}
//			}
		} catch (Exception e) {
		
		}
		return rtnobjMap;
	}

	public Map<String, Object> getCloudReportDocxInfo(Map<String, Object> objMap) {
		Map<String, Object> rtnobjMap = new HashMap<String, Object>();
		try {
			LSdocreports lSdocreportsObj = LSdocreportsRepositoryObj
					.findByDocReportsCode((int) objMap.get("docReportsCode"));
			List<LSdocreportsversionhistory> LSdocreportsversionhistorylst = LSdocreportsversionHistoryRepositoryObj
					.findAllByDocReportsCodeAndStatus(lSdocreportsObj.getDocReportsCode(), 1);
			@SuppressWarnings("unused")
			Map<String, Object> ftpstatus = null;
//			boolean canLoad = false;
			String FileName = lSdocreportsObj.getFileName();
			String HashKey = lSdocreportsObj.getFileHashName();

//			if (canLoad) {
			String filePath = getDocxAbsolutePath();
			if (lSdocreportsObj.getIsTemplate() == 1 && !isftpAvailable()) {
				if (System.getProperty("os.name").contains("Linux")
						|| System.getProperty("os.name").contains("LINUX")) {
					filePath += "/templates";
				} else {
					filePath += "\\templates";
				}
			}
			boolean filePresent = false;
			boolean status = false;
			File directory = new File(filePath);
			if (directory.exists()) {
				File requestedFile = new File(filePath, HashKey + ".docx");
				if (!requestedFile.exists()) {
//						Map<String, Object> fileInfo = new HashMap<String, Object>();
//						fileInfo.put("id", lSdocreportsObj.getStreamid());
//						fileInfo.put("filePath", requestedFile);
					String FileID = lSdocreportsObj.getStreamid();
					try (FileOutputStream out = new FileOutputStream(requestedFile)) {
						InputStream stream = CloudFileManipulationservice.retrieveReportFiles(FileID);
						int read;
						final byte[] bytes = new byte[1024];
						while ((read = stream.read(bytes)) != -1) {
							out.write(bytes, 0, read);
						}
						out.flush();
						out.close();
						status = true;
					} catch (Exception e) {
						
					}
					if (status) {
						filePresent = true;
					} else {
						filePresent = false;
					}
				} else {
					filePresent = true;
				}
				if (filePresent) {
					if (env.getProperty("fileReceiver") != null) {
						int httpfileStatus = uploadSingleFile(requestedFile.getAbsolutePath(),
								lSdocreportsObj.getIsTemplate());
						if (httpfileStatus == 200) {
							rtnobjMap.put("fileFullPath", "");
						} else {
							rtnobjMap.put("fileFullPath", requestedFile.getAbsolutePath());
						}
					} else {
						rtnobjMap.put("fileFullPath", requestedFile.getAbsolutePath());
					}
					// lSdocreportsObj.setFileHashName(HashKey);
					if (LSdocreportsversionhistorylst.size() > 0) {
						lSdocreportsObj.setVersionno(LSdocreportsversionhistorylst
								.get(LSdocreportsversionhistorylst.size() - 1).getVersionNo());
					} else {
						lSdocreportsObj.setVersionno(1);
					}
					LSdocreportsRepositoryObj.save(lSdocreportsObj);
					rtnobjMap.put("status", "success");
					rtnobjMap.put("fileFullPath", requestedFile.getAbsolutePath());
					rtnobjMap.put("fileName", FileName + ".docx");
					if (lSdocreportsObj.getIsTemplate() == 1 && !isftpAvailable()) {
						rtnobjMap.put("fileOriginalPath", "reports/templates/" + HashKey + "." + "docx");
					} else {
						rtnobjMap.put("fileOriginalPath", "reports/" + HashKey + "." + "docx");
					}
					rtnobjMap.put("hashKey", HashKey);
					rtnobjMap.put("isdraft", lSdocreportsObj.getIsdraft());
					rtnobjMap.put("LSdocreportsversionhistorylst", LSdocreportsversionhistorylst);
					if (objMap.containsKey("objsilentaudit")) {
						if (objMap.get("objsilentaudit") != null) {
							LScfttransaction LScfttransactionobj = new ObjectMapper()
									.convertValue(objMap.get("objsilentaudit"), new TypeReference<LScfttransaction>() {
									});
							LScfttransactionobj.setSystemcoments("System Generated");
							LScfttransactionobj.setTableName("LSdocreports");
							lscfttransactionRepository.save(LScfttransactionobj);
						}
					}
				} else {
					rtnobjMap.put("status", "ID_FILENOTFOUNDEITHERDELETEDORMOVED");
				}
			}
//			}
		} catch (Exception e) {
			
		}
		return rtnobjMap;
	}

	public void insertSilentAudit(Map<String, Object> argObj) {
		if (argObj.containsKey("objsilentaudit")) {
			if (argObj.get("objsilentaudit") != null) {
				LScfttransaction LScfttransactionobj = new ObjectMapper().convertValue(argObj.get("objsilentaudit"),
						new TypeReference<LScfttransaction>() {
						});
				// LScfttransactionobj.setModuleName("Reports");
				// LScfttransactionobj.setComments("To get Document list");
				// LScfttransactionobj.setActions("Load");
				LScfttransactionobj.setSystemcoments("System Generated");
				LScfttransactionobj.setTableName("LSdocreports");
				lscfttransactionRepository.save(LScfttransactionobj);
			}
		}
	}

	@SuppressWarnings({ "resource", "null" })
	public Map<String, Object> updateDocxReportOrder(Map<String, Object> obj) {
		Map<String, Object> objMap = new HashMap<String, Object>();
		try {
			try {
			
				java.util.concurrent.TimeUnit.SECONDS.sleep(2);
			
			} catch (InterruptedException e) {
				
			}
			ObjectMapper mapper = new ObjectMapper();
			List<LSlogilablimsorderdetail> lstSelectedData = mapper.convertValue(obj.get("selectedData"),
					new TypeReference<List<LSlogilablimsorderdetail>>() {
					});
			String FileName = "";
			if (((String) obj.get("onlyOfficeDocxName")).length() > 0) {
				if (((String) obj.get("onlyOfficeDocxName")).contains("/")) {
					FileName = (((String) obj.get("onlyOfficeDocxName")).split("/")[1]).split("\\.")[0];
				} else {
					FileName = ((String) obj.get("onlyOfficeDocxName")).split("\\.")[0];
				}
			} else {
				FileName = (String) obj.get("Hashkey");
			}
			String filePath = getDocxAbsolutePath();
		
			LSdocreports LSDocReportobj = LSdocreportsRepositoryObj
					.findFirstByFileHashNameAndStatus((String) obj.get("Hashkey"), 1);
		
			String NewFileName = "";
			String NewHashKey = UUID.randomUUID().toString();
			LSdocdirectory LSdocdirectoryObj = LSdocdirectoryRepositoryObj
					.findFirstByDirectorynameAndStatus("Generated Reports", 1);
			if (LSDocReportobj == null) {
				LSDocReportobj.setFileHashName(NewHashKey);
				LSDocReportobj.setExtention("docx");
				LSDocReportobj.setDocdirectorycode(LSdocdirectoryObj.getDocdirectorycode());
			} else {
				LSDocReportobj.setFileHashName(NewHashKey);
			}
		
			File newFile = null;
			if (lstSelectedData.size() == 1 && LSDocReportobj.getFileName() == null) {
				NewFileName = lstSelectedData.get(0).getBatchid();
			} else if (LSDocReportobj.getFileName() != null && !LSDocReportobj.getFileName().isEmpty()) {
				NewFileName = LSDocReportobj.getFileName();
			} else {
//				NewFileName = (String) obj.get("Hashkey") ;
				NewFileName = NewHashKey;
			}
			
			if ((new File(filePath, FileName + ".docx")).exists() && !FileName.equals(NewFileName)) {
				File oldFile = new File(filePath, FileName + ".docx");
				newFile = new File(filePath, NewFileName + ".docx");
				oldFile.renameTo(newFile);
			
				if (oldFile.exists()) {
					oldFile.delete();
				
				}
			} else {
				newFile = new File(filePath, NewFileName + ".docx");
				if (!newFile.exists()) {
					FileOutputStream fos = new FileOutputStream(newFile);
					new XWPFDocument().write(fos);
					fos.close();
					File oldFile = new File(filePath, FileName + ".docx");
					if (oldFile.exists()) {
						oldFile.delete();
					
					}
				
				}
			}
			XWPFDocument document = new XWPFDocument(new FileInputStream(newFile));
			FileOutputStream out = new FileOutputStream(newFile);
			for (LSlogilablimsorderdetail SelectedDataObj : lstSelectedData) {
				LSsamplefile LsSampleFiles = (LSsamplefile) SelectedDataObj.getLssamplefile();// QueryForList(squery);
			
				if (!LsSampleFiles.getFilecontent().isEmpty()) {
					String blobData = LsSampleFiles.getFilecontent();
					Map<String, Object> sheetProps = getTaggedDataFromSheet(blobData);
					updateSheetDatainDocx(sheetProps, newFile, document);
				}
			}
			document.createParagraph();
			document.write(out);
			out.close();
	
			objMap.put("fileFullPath", newFile.getAbsolutePath());
			objMap.put("fileName", NewFileName);
			objMap.put("fileOriginalPath", "reports/" + NewFileName + ".docx");
			objMap.put("hashKey", NewHashKey);
//			objMap.put("hashKey", (String) obj.get("Hashkey"));

			if (NewFileName == NewHashKey) {
				LSDocReportobj.setFileName(null);
			} else {
				LSDocReportobj.setFileName(NewFileName);
			}
			
			LSdocreportsRepositoryObj.save(LSDocReportobj);

		

		} catch (Exception e) {
			
		}
		return objMap;
	}

	@SuppressWarnings({ "unchecked" })
	public Map<String, Object> handleOrderandTemplate(Map<String, Object> obj) {
		Map<String, Object> objMap = new HashMap<String, Object>();
		try {
			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> templateData = (Map<String, Object>) obj.get("templateData");
			List<LSlogilablimsorderdetail> lstSelectedData = mapper.convertValue(obj.get("OrderData"),
					new TypeReference<List<LSlogilablimsorderdetail>>() {
					});
		
			String fileName = "";
			String templateName = (String) templateData.get("fileName");
			String filePath = getDocxAbsolutePath();
			File newFile = null;
			boolean fileLoaded = false;
			String HashKey = UUID.randomUUID().toString();
			if (lstSelectedData.size() == 1) {
				fileName = templateName + "_" + lstSelectedData.get(0).getBatchid();
				newFile = new File(filePath, fileName + ".docx");
				if (newFile.exists()) {
					fileName = HashKey;
				}
			} else {
				fileName = HashKey;
			}
	
			File loadFile = new File(filePath, templateName + ".docx");
			if (!loadFile.exists()) {
				if (isftpAvailable()) {
					Map<String, Object> FTPStatus = handleFTP(templateName, "load", "templates");
					if ((boolean) FTPStatus.get("status")) {
						fileLoaded = true;
					}
				} else {
					loadFile = new File(filePath + "\\templates\\" + templateName + ".docx");
					if (loadFile.exists()) {
						fileLoaded = true;
					} else {
						fileLoaded = false;
					}
				}
			} else {
				fileLoaded = true;
			}
			if (fileLoaded) {
				newFile = new File(filePath, fileName + ".docx");
				FileInputStream fis = new FileInputStream(loadFile);
				XWPFDocument document = new XWPFDocument(fis);
			
				for (LSlogilablimsorderdetail SelectedDataObj : lstSelectedData) {
					String SheetName = SelectedDataObj.getLsfile().getFilenameuser();
					LSsamplefile LsSampleFiles = (LSsamplefile) SelectedDataObj.getLssamplefile();// QueryForList(squery);
				
					if (!LsSampleFiles.getFilecontent().isEmpty()) {
						List<Map<String, Object>> TagLst = getTagInfofromSheet(LsSampleFiles.getFilecontent());
					
						for (Map<String, Object> SingleTag : TagLst) {
							replaceDocxTagWithCell(TagLst, document, SheetName, SingleTag);
						}
					}
				}
				FileOutputStream fos = new FileOutputStream(newFile);
				document.write(fos);
				document.close();
				fos.close();
				fis.close();
				if (isftpAvailable()) {
					loadFile.delete();
				}
//				newFile.setReadOnly();
				LSdocdirectory LSdocdirectoryObj = LSdocdirectoryRepositoryObj
						.findFirstByDirectorynameAndStatus("Generated Reports", 1);
				LSdocreports LSDocReportobj = new LSdocreports();
				LSDocReportobj.setExtention("docx");
				LSDocReportobj.setFileName(fileName);
				LSDocReportobj.setFileHashName(HashKey);
				LSDocReportobj.setDocdirectorycode(LSdocdirectoryObj.getDocdirectorycode());
				LSDocReportobj.setIsTemplate(0);
				LSDocReportobj.setStatus(1);
			
				LSdocreportsRepositoryObj.save(LSDocReportobj);

				objMap.put("fileFullPath", newFile.getAbsolutePath());
				objMap.put("fileName", fileName);
				objMap.put("fileOriginalPath", "reports/" + fileName + ".docx");
				objMap.put("hashKey", HashKey);
			} else {
				
				objMap.put("status", "ID_REQUESTEDTEMPLATENOTFOUND");
			}
		
		} catch (Exception e) {
		
		}
		return objMap;
	}

	Map<String, List<Map<String, Object>>> sequenceTagList = new HashMap<String, List<Map<String, Object>>>();

	@SuppressWarnings("unchecked")
	public Map<String, Object> cloudHandleOrderTemplate(Map<String, Object> obj) {
		Map<String, Object> objMap = new HashMap<String, Object>();
		sequenceTagList = new HashMap<String, List<Map<String, Object>>>();
		try {
			ObjectMapper mapper = new ObjectMapper();
			List<LSlogilablimsorderdetail> lstSelectedData = mapper.convertValue(obj.get("OrderData"),
					new TypeReference<List<LSlogilablimsorderdetail>>() {
					});
//			long batchcode = mapper.convertValue(obj.get("batchcode"), long.class);
//			String batchid = mapper.convertValue(obj.get("batchid"), String.class);
//			List<LSlogilablimsorderdetail> lstSelectedData = lslogilablimsorderdetailRepository
//					.findByBatchcodeAndBatchid(batchcode, batchid);

			LSdocreports LSdocreportsObj = mapper.convertValue(obj.get("templateRecords"),
					new TypeReference<LSdocreports>() {
					});
			LSuserMaster lsusermaster = new LSuserMaster();
//			if (obj.containsKey("Lsusermaster")) {
//				if (obj.get("Lsusermaster") != null) {
//				lsusermaster = new ObjectMapper().convertValue(obj.get("Lsusermaster"),new TypeReference<LSuserMaster>(){});
//				}
//			}

			if (obj.containsKey("usercode")) {
				if (obj.get("usercode") != null) {
					int usercode = new ObjectMapper().convertValue(obj.get("usercode"), Integer.class);
					lsusermaster = LSuserMasterRepositoryObj.findByusercode(usercode);
//				lsusermaster = new ObjectMapper().convertValue(obj.get("Lsusermaster"),new TypeReference<LSuserMaster>(){});
				}
			}
			if (LSdocreportsObj != null) {
			
				String fileName = "";
				@SuppressWarnings("unused")
				String templateName = LSdocreportsObj.getFileName();
				String templateHashName = LSdocreportsObj.getFileHashName();
				String filePath = getDocxAbsolutePath();
				File newFile = null;
				boolean fileLoaded = false;
				String HashKey = UUID.randomUUID().toString();
//				if (lstSelectedData.size() == 1) {
				Integer site = mapper.convertValue(obj.get("sitecode"), Integer.class);

				List<LSdocreports> LSdocreportsLst = LSdocreportsRepositoryObj.findByIsreportAndLssitemaster(1, site);

//					fileName = templateName + "_" + lstSelectedData.get(0).getBatchid();
				fileName = "Report_";
				if (LSdocreportsLst.size() < 10) {
					fileName += "0000";
				} else if (LSdocreportsLst.size() < 100) {
					fileName += "000";
				} else if (LSdocreportsLst.size() < 1000) {
					fileName += "00";
				} else if (LSdocreportsLst.size() < 10000) {
					fileName += "0";
				}
				fileName += (LSdocreportsLst.size() + 1);
				newFile = new File(filePath, fileName + ".docx");
				if (newFile.exists()) {
					fileName = HashKey;
				}
//				} else {
//					fileName = HashKey;
//				}
			
				String path = filePath + "\\templates";
				if (System.getProperty("os.name").contains("Linux")
						|| System.getProperty("os.name").contains("LINUX")) {
					path = filePath + "/templates";
				}

				File loadFile = new File(path);
				if (!loadFile.exists()) {
					loadFile.mkdir();
				}
				loadFile = new File(path, templateHashName + ".docx");
				if (!loadFile.exists()) {
//					Map<String, Object> fileInfo = new HashMap<String, Object>();
//					fileInfo.put("id", LSdocreportsObj.getStreamid());
//					fileInfo.put("filePath", loadFile);
//					Map<String, Object> fileStatus = uploadAndRetriveDoc(fileInfo, "retrive");
//					boolean status = false;
					String FileID = LSdocreportsObj.getStreamid();
					try (FileOutputStream out = new FileOutputStream(loadFile)) {
						InputStream stream = CloudFileManipulationservice.retrieveReportFiles(FileID);
						int read;
						final byte[] bytes = new byte[1024];
						while ((read = stream.read(bytes)) != -1) {
							out.write(bytes, 0, read);
						}
						out.flush();
						out.close();
						fileLoaded = true;
					} catch (Exception e) {
					
					}
//					if(fileStatus.get("status") == "success") {
//						fileLoaded = true;
//					}else {
//						fileLoaded = false;
//					}
				} else {
					if (loadFile.length() == 0) {
						loadFile.delete();
//						Map<String, Object> fileInfo = new HashMap<String, Object>();
//						fileInfo.put("id", LSdocreportsObj.getStreamid());
//						fileInfo.put("filePath", loadFile);
//						Map<String, Object> fileStatus = uploadAndRetriveDoc(fileInfo, "retrive");
//						boolean status = false;
						String FileID = LSdocreportsObj.getStreamid();
						try (FileOutputStream out = new FileOutputStream(loadFile)) {
							InputStream stream = CloudFileManipulationservice.retrieveReportFiles(FileID);
							int read;
							final byte[] bytes = new byte[1024];
							while ((read = stream.read(bytes)) != -1) {
								out.write(bytes, 0, read);
							}
							out.flush();
							out.close();
							fileLoaded = true;
						} catch (Exception e) {
							
						}
//						if(status) {
//							fileLoaded = true;
//						}else {
//							fileLoaded = false;
//						}
					}
					fileLoaded = true;
				}
				if (fileLoaded) {
					newFile = new File(filePath, HashKey + ".docx");
					FileInputStream fis = new FileInputStream(loadFile);
					XWPFDocument document = new XWPFDocument(fis);
				
					for (LSlogilablimsorderdetail SelectedDataObj : lstSelectedData) {
						String SheetName = SelectedDataObj.getLsfile().getFilenameuser();
						LSsamplefile LsSampleFiles = (LSsamplefile) SelectedDataObj.getLssamplefile();// QueryForList(squery);
					
						String excelData = "";
						if (LsSampleFiles.getFilecontent() != null) {
							excelData = LsSampleFiles.getFilecontent();
						} else if ((int) obj.get("isMultitenant") == 1) {

//							CloudOrderCreation file = CloudOrderCreationRepository
//									.findById((long) SelectedDataObj.getLssamplefile().getFilesamplecode());
//							excelData = file.getContent();
							CloudOrderCreation objCreation = cloudOrderCreationRepository
									.findById((long) SelectedDataObj.getLssamplefile().getFilesamplecode());
							if (objCreation != null && objCreation.getContainerstored() == 0) {
								excelData=objCreation.getContent();
							} else {
								excelData=objCloudFileManipulationservice.retrieveCloudSheets(objCreation.getFileuid(),TenantContext.getCurrentTenant() + "ordercreation");
							}
						} else {
//							OrderCreation file = mongoTemplate.findById(
//							SelectedDataObj.getLssamplefile().getFilesamplecode(), OrderCreation.class);
//					excelData = file.getContent();

					String fileid = "order_" + SelectedDataObj.getLssamplefile().getFilesamplecode();
					GridFSDBFile largefile = gridFsTemplate.findOne(new Query(Criteria.where("filename").is(fileid)));
					if (largefile == null) {
						largefile = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(fileid)));
					}
//					String filecontent;
					if (largefile != null) {
						String filecontent = new BufferedReader(
								new InputStreamReader(largefile.getInputStream(), StandardCharsets.UTF_8)).lines()
										.collect(Collectors.joining("\n"));
						excelData = filecontent;

					} else {
						OrderCreation file = mongoTemplate.findById(
								SelectedDataObj.getLssamplefile().getFilesamplecode(), OrderCreation.class);
						excelData = file.getContent();
						
					}
//				
					
				}
						if (!excelData.isEmpty()) {
							List<Map<String, Object>> TagLst = getTagInfofromSheet(excelData);
						
//							for (Map<String, Object> SingleTag : TagLst) {
//								replaceDocxTagWithCell(SingleTag, document, SheetName);
//							}
							for (Map<String, Object> SingleTag : TagLst) {
								replaceDocxTagWithCell(TagLst, document, SheetName, SingleTag);
							}
						}
					}

					FileOutputStream fos = new FileOutputStream(newFile);
					document.write(fos);
					document.close();
					fos.close();
					fis.close();
					loadFile.delete();
					LScfttransaction LScfttransactionobj = new LScfttransaction();
					if (obj.containsKey("objsilentaudit")) {
						if (obj.get("objsilentaudit") != null) {
							LScfttransactionobj = new ObjectMapper().convertValue(obj.get("objsilentaudit"),
									new TypeReference<LScfttransaction>() {
									});
						}
					}
					LSdocdirectory LSdocdirectoryObj = LSdocdirectoryRepositoryObj
							.findFirstByDirectorynameAndStatus("Generated Reports", 1);
					LSdocreports LSDocReportobj = new LSdocreports();
//					if (obj.containsKey("Lsusermaster")) {
//						if (obj.get("Lsusermaster") != null) {
//						lsusermaster = new ObjectMapper().convertValue(obj.get("Lsusermaster"),new TypeReference<LSuserMaster>(){});
//						LSDocReportobj.setLssitemaster(lsusermaster.getLssitemaster().getSitecode());
//						}
//					}
					if (obj.containsKey("usercode")) {
						if (obj.get("usercode") != null) {
							int usercode = new ObjectMapper().convertValue(obj.get("usercode"), Integer.class);
							lsusermaster = LSuserMasterRepositoryObj.findByusercode(usercode);
//						lsusermaster = new ObjectMapper().convertValue(obj.get("Lsusermaster"),new TypeReference<LSuserMaster>(){});
							LSDocReportobj.setLssitemaster(lsusermaster.getLssitemaster().getSitecode());
						}
					}
					// silent audit
					LScfttransactionobj.setComments("Report generated successfully");
					LScfttransactionobj.setActions("update");
					LScfttransactionobj.setModuleName("Reports");
					LScfttransactionobj.setTransactiondate(new Date());
					LScfttransactionobj.setTableName("LSdocreports");

					LSDocReportobj.setExtention("docx");
					LSDocReportobj.setFileName(fileName);
					LSDocReportobj.setFileHashName(HashKey);
					LSDocReportobj.setDocdirectorycode(LSdocdirectoryObj.getDocdirectorycode());
					LSDocReportobj.setIsTemplate(0);
					LSDocReportobj.setIsreport(1);
					LSDocReportobj.setCreatedBy(LScfttransactionobj.getLsuserMaster());
					LSDocReportobj.setCreatedate(new Date());
					LSDocReportobj.setSheetfilecodeString("");
					LSDocReportobj.setStatus(1);
				
					LSdocreportsRepositoryObj.save(LSDocReportobj);

					Map<String, Object> newDocx = new HashMap<String, Object>();
					if (env.getProperty("fileReceiver") != null) {
						int httpfileStatus = uploadSingleFile(newFile.getAbsolutePath(), 0);
						if (httpfileStatus == 200) {
							newDocx.put("fileFullPath", "");
						} else {
							newDocx.put("fileFullPath", newFile.getAbsolutePath());
						}
					} else {
						newDocx.put("fileFullPath", newFile.getAbsolutePath());
					}
					objMap.put("DocxDirectoryLst", getDocxDirectoryLst());
					objMap.put("DocxReportLst", getLSdocreportsLst("all"));

					objMap.put("status", "success");
					newDocx.put("fileFullPath", newFile.getAbsolutePath());
					newDocx.put("fileName", fileName);
					newDocx.put("fileOriginalPath", "reports/" + HashKey + ".docx");
					newDocx.put("hashKey", HashKey);
					objMap.put("newDocx", newDocx);
					if (LScfttransactionobj != null) {
						LScfttransactionobj = new ObjectMapper().convertValue(obj.get("objsilentaudit"),
								new TypeReference<LScfttransaction>() {
								});
						LScfttransactionobj.setTableName("LSdocreports");
						lscfttransactionRepository.save(LScfttransactionobj);
					}
					if (obj.containsKey("objmanualaudit")) {
						if (obj.get("objmanualaudit") != null) {
							LScfttransactionobj = new ObjectMapper().convertValue(obj.get("objmanualaudit"),
									new TypeReference<LScfttransaction>() {
									});
							if (obj.containsKey("objuser")) {
								Map<String, Object> objuser = (Map<String, Object>) obj.get("objuser");
								LScfttransactionobj.setComments((String) objuser.get("comments"));
							}
							LScfttransactionobj.setTableName("LSdocreports");
							lscfttransactionRepository.save(LScfttransactionobj);
						}
					}
				} else {
				
					objMap.put("status", "ID_REQUESTEDTEMPLATENOTFOUND");
				}
			
			} else {
				
				objMap.put("status", "ID_TEMPLATENOTFOUNDFORSELECTEDORDER");
			}
		} catch (Exception e) {
		
		}
		return objMap;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> handleOrderTemplate(Map<String, Object> obj) {
		Map<String, Object> objMap = new HashMap<String, Object>();
		sequenceTagList = new HashMap<String, List<Map<String, Object>>>();
		try {
			ObjectMapper mapper = new ObjectMapper();
			long batchcode = mapper.convertValue(obj.get("batchcode"), long.class);
			String batchid = mapper.convertValue(obj.get("batchid"), String.class);
			List<LSlogilablimsorderdetail> lstSelectedData = lslogilablimsorderdetailRepository
					.findByBatchcodeAndBatchid(batchcode, batchid);
//			List<LSlogilablimsorderdetail> lstSelectedData = mapper.convertValue(obj.get("OrderData"),
//					new TypeReference<List<LSlogilablimsorderdetail>>() {
//					});
			LSdocreports LSdocreportsObj = mapper.convertValue(obj.get("templateRecords"),
					new TypeReference<LSdocreports>() {
					});
			LSuserMaster lsusermaster = new LSuserMaster();
//			if (obj.containsKey("Lsusermaster")) {
//				if (obj.get("Lsusermaster") != null) {
//				lsusermaster = new ObjectMapper().convertValue(obj.get("Lsusermaster"),new TypeReference<LSuserMaster>(){});
//				}
//			}

			if (obj.containsKey("usercode")) {
				if (obj.get("usercode") != null) {
					int usercode = new ObjectMapper().convertValue(obj.get("usercode"), Integer.class);
					lsusermaster = LSuserMasterRepositoryObj.findByusercode(usercode);
//				lsusermaster = new ObjectMapper().convertValue(obj.get("Lsusermaster"),new TypeReference<LSuserMaster>(){});
				}
			}
			if (LSdocreportsObj != null) {
			
				String fileName = "";
				@SuppressWarnings("unused")
				String templateName = LSdocreportsObj.getFileName();
				String templateHashName = LSdocreportsObj.getFileHashName();
				String filePath = getDocxAbsolutePath();
				File newFile = null;
				boolean fileLoaded = false;
				String HashKey = UUID.randomUUID().toString();
				if (lstSelectedData.size() == 1) {
					List<LSdocreports> LSdocreportsLst = LSdocreportsRepositoryObj.findByIsreportAndLssitemaster(1,
							lsusermaster.getLssitemaster().getSitecode());
//					fileName = templateName + "_" + lstSelectedData.get(0).getBatchid();
					fileName = "Report_";
					if (LSdocreportsLst.size() < 10) {
						fileName += "0000";
					} else if (LSdocreportsLst.size() < 100) {
						fileName += "000";
					} else if (LSdocreportsLst.size() < 1000) {
						fileName += "00";
					} else if (LSdocreportsLst.size() < 10000) {
						fileName += "0";
					}
					fileName += (LSdocreportsLst.size() + 1);
					newFile = new File(filePath, fileName + ".docx");
					if (newFile.exists()) {
						fileName = HashKey;
					}
				} else {
					fileName = HashKey;
				}
				
				File loadFile = new File(filePath + "\\templates");
				if (!loadFile.exists()) {
					loadFile.mkdir();
				}
				loadFile = new File(filePath + "\\templates\\" + templateHashName + ".docx");
				if (!loadFile.exists()) {
					Map<String, Object> fileInfo = new HashMap<String, Object>();
					fileInfo.put("id", LSdocreportsObj.getStreamid());
					fileInfo.put("filePath", loadFile);
					Map<String, Object> fileStatus = uploadAndRetriveDoc(fileInfo, "retrive");
					if (fileStatus.get("status") == "success") {
						fileLoaded = true;
					} else {
						fileLoaded = false;
					}
				} else {
					if (loadFile.length() == 0) {
						loadFile.delete();
						Map<String, Object> fileInfo = new HashMap<String, Object>();
						fileInfo.put("id", LSdocreportsObj.getStreamid());
						fileInfo.put("filePath", loadFile);
						Map<String, Object> fileStatus = uploadAndRetriveDoc(fileInfo, "retrive");
						if (fileStatus.get("status") == "success") {
							fileLoaded = true;
						} else {
							fileLoaded = false;
						}
					}
					fileLoaded = true;
				}
				if (fileLoaded) {
					newFile = new File(filePath, HashKey + ".docx");
					FileInputStream fis = new FileInputStream(loadFile);
					XWPFDocument document = new XWPFDocument(fis);
				
					for (LSlogilablimsorderdetail SelectedDataObj : lstSelectedData) {
						String SheetName = SelectedDataObj.getLsfile().getFilenameuser();
						LSsamplefile LsSampleFiles = (LSsamplefile) SelectedDataObj.getLssamplefile();// QueryForList(squery);
					
						String excelData = "";
						if (LsSampleFiles.getFilecontent() != null) {
							excelData = LsSampleFiles.getFilecontent();
						} else {
//							OrderCreation file = mongoTemplate.findById(
//							SelectedDataObj.getLssamplefile().getFilesamplecode(), OrderCreation.class);
//					excelData = file.getContent();

					String fileid = "order_" + SelectedDataObj.getLssamplefile().getFilesamplecode();
					GridFSDBFile largefile = gridFsTemplate.findOne(new Query(Criteria.where("filename").is(fileid)));
					if (largefile == null) {
						largefile = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(fileid)));
					}
//					String filecontent;
					if (largefile != null) {
						String filecontent = new BufferedReader(
								new InputStreamReader(largefile.getInputStream(), StandardCharsets.UTF_8)).lines()
										.collect(Collectors.joining("\n"));
						excelData = filecontent;

					} else {
						OrderCreation file = mongoTemplate.findById(
								SelectedDataObj.getLssamplefile().getFilesamplecode(), OrderCreation.class);
						excelData = file.getContent();
						
					}
//				
					
				}
						if (!excelData.isEmpty()) {
							List<Map<String, Object>> TagLst = getTagInfofromSheet(excelData);
						
							for (Map<String, Object> SingleTag : TagLst) {
								replaceDocxTagWithCell(TagLst, document, SheetName, SingleTag);
							}
						}
					}

					FileOutputStream fos = new FileOutputStream(newFile);
					document.write(fos);
					document.close();
					fos.close();
					fis.close();
					loadFile.delete();
					LScfttransaction LScfttransactionobj = new LScfttransaction();
					if (obj.containsKey("objsilentaudit")) {
						if (obj.get("objsilentaudit") != null) {
							LScfttransactionobj = new ObjectMapper().convertValue(obj.get("objsilentaudit"),
									new TypeReference<LScfttransaction>() {
									});
						}
					}
					LSdocdirectory LSdocdirectoryObj = LSdocdirectoryRepositoryObj
							.findFirstByDirectorynameAndStatus("Generated Reports", 1);
					LSdocreports LSDocReportobj = new LSdocreports();
//					if (obj.containsKey("Lsusermaster")) {
//						if (obj.get("Lsusermaster") != null) {
//						lsusermaster = new ObjectMapper().convertValue(obj.get("Lsusermaster"),new TypeReference<LSuserMaster>(){});
//						LSDocReportobj.setLssitemaster(lsusermaster.getLssitemaster().getSitecode());
//						}
//					}

					if (obj.containsKey("usercode")) {
						if (obj.get("usercode") != null) {
							int usercode = new ObjectMapper().convertValue(obj.get("usercode"), Integer.class);
							lsusermaster = LSuserMasterRepositoryObj.findByusercode(usercode);
//						lsusermaster = new ObjectMapper().convertValue(obj.get("Lsusermaster"),new TypeReference<LSuserMaster>(){});
							LSDocReportobj.setLssitemaster(lsusermaster.getLssitemaster().getSitecode());
						}
					}
					// silent audit
					LScfttransactionobj.setComments("Report generated successfully");
					LScfttransactionobj.setActions("update");
					LScfttransactionobj.setModuleName("Reports");
					LScfttransactionobj.setTransactiondate(new Date());
					LScfttransactionobj.setTableName("LSdocreports");

					LSDocReportobj.setExtention("docx");
					LSDocReportobj.setFileName(fileName);
					LSDocReportobj.setFileHashName(HashKey);
					LSDocReportobj.setDocdirectorycode(LSdocdirectoryObj.getDocdirectorycode());
					LSDocReportobj.setIsTemplate(0);
					LSDocReportobj.setIsreport(1);
					LSDocReportobj.setCreatedBy(LScfttransactionobj.getLsuserMaster());
					LSDocReportobj.setCreatedate(new Date());
					LSDocReportobj.setSheetfilecodeString("");
					LSDocReportobj.setStatus(1);
				
					LSdocreportsRepositoryObj.save(LSDocReportobj);
					Map<String, Object> newDocx = new HashMap<String, Object>();
					if (env.getProperty("fileReceiver") != null) {
						int httpfileStatus = uploadSingleFile(newFile.getAbsolutePath(), 0);
						if (httpfileStatus == 200) {
							newDocx.put("fileFullPath", "");
						} else {
							newDocx.put("fileFullPath", newFile.getAbsolutePath());
						}
					} else {
						newDocx.put("fileFullPath", newFile.getAbsolutePath());
					}
					objMap.put("DocxDirectoryLst", getDocxDirectoryLst());
					objMap.put("DocxReportLst", getLSdocreportsLst("all"));

					objMap.put("status", "success");
					newDocx.put("fileFullPath", newFile.getAbsolutePath());
					newDocx.put("fileName", fileName);
					newDocx.put("fileOriginalPath", "reports/" + HashKey + ".docx");
					newDocx.put("hashKey", HashKey);
					objMap.put("newDocx", newDocx);
					if (LScfttransactionobj != null) {
						LScfttransactionobj = new ObjectMapper().convertValue(obj.get("objsilentaudit"),
								new TypeReference<LScfttransaction>() {
								});
						LScfttransactionobj.setTableName("LSdocreports");
						lscfttransactionRepository.save(LScfttransactionobj);
					}
					if (obj.containsKey("objmanualaudit")) {
						if (obj.get("objmanualaudit") != null) {
							LScfttransactionobj = new ObjectMapper().convertValue(obj.get("objmanualaudit"),
									new TypeReference<LScfttransaction>() {
									});
							if (obj.containsKey("objuser")) {
								Map<String, Object> objuser = (Map<String, Object>) obj.get("objuser");
								LScfttransactionobj.setComments((String) objuser.get("comments"));
							}
							LScfttransactionobj.setTableName("LSdocreports");
							lscfttransactionRepository.save(LScfttransactionobj);
						}
					}
				} else {
					
					objMap.put("status", "ID_REQUESTEDTEMPLATENOTFOUND");
				}
			
			} else {
			
				objMap.put("status", "ID_TEMPLATENOTFOUNDFORSELECTEDORDER");
			}
		} catch (Exception e) {
		
		}
		return objMap;
	}

	@SuppressWarnings("unchecked")
	public void replaceDocxTagWithSequenceData(Map<String, List<Map<String, Object>>> sequenceDataTag,
			XWPFDocument document) {
		boolean isDone = false;
		if (!isDone) {
			for (Map.Entry<String, List<Map<String, Object>>> sequenceDataTagObj : sequenceDataTag.entrySet()) {
			
				String tagname = sequenceDataTagObj.getKey();
				List<Map<String, Object>> tagListObj = sequenceDataTagObj.getValue();
				for (XWPFParagraph para : document.getParagraphs()) {
					List<XWPFRun> runs = para.getRuns();
					if (runs != null) {
						for (XWPFRun r : runs) {
							String text = r.getText(0);
							if (text != null) {
								if (text.contains(tagname)) {
									if (tagListObj.size() == 1) {
										Map<String, Object> dataObj = (Map<String, Object>) tagListObj.get(0)
												.get("0_0"); // (Map<String, Object>) tagData.get("0_0");
									
										String value = "";
										if (dataObj.get("value") instanceof String) {
											value = (String) dataObj.get("value");
										} else if (dataObj.get("value") instanceof Integer) {
											value = Integer.toString((int) dataObj.get("value"));
										} else if (dataObj.get("value") instanceof Double) {
											value = Double.toString((double) dataObj.get("value"));
										} else if (dataObj.get("value") instanceof Date) {
											DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
											value = dateFormat.format(dataObj.get("value"));
										}
										text = text.replace(tagname, value);
										r.setText(text, 0);
									} else {
										XmlCursor cursor = para.getCTP().newCursor();// this is the key!
										XWPFTable table = document.insertNewTbl(cursor);
										if (table != null) {
											int rowCount = 0;
											XWPFTableRow sheetRow = null;
											for (Map<String, Object> tagListObjEach : tagListObj) {
												if (rowCount == 0) {
													sheetRow = table.getRow(0);
												} else {
													sheetRow = table.createRow();
												}
												rowCount++;
												XWPFTableCell sheetRowCell = sheetRow.getCell(0);
												Map<String, Object> dataObj = (Map<String, Object>) tagListObjEach
														.get("0_0");
										
												String value = "";
												if (dataObj.get("value") instanceof String) {
													value = (String) dataObj.get("value");
												} else if (dataObj.get("value") instanceof Integer) {
													value = Integer.toString((int) dataObj.get("value"));
												} else if (dataObj.get("value") instanceof Double) {
													value = Double.toString((double) dataObj.get("value"));
												} else if (dataObj.get("value") instanceof Date) {
													DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
													value = dateFormat.format(dataObj.get("value"));
												}
												sheetRowCell.setText(value);
											}

										}
									}
									text = text.replace(tagname, "");
									r.setText(text, 0);
								}
							}
						}
					}
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void replaceDocxTagWithCell(List<Map<String, Object>> SingleTagObj1, XWPFDocument document, String SheetName,
			Map<String, Object> SingleTag) {
		boolean isDone = false;
		Map<String, Object> SingleTagObj = new HashMap<String, Object>();
		if (!isDone) {
			for (XWPFParagraph para : document.getParagraphs()) {
				// String Tag = getTagsfromDocx(para);
				List<String> TagLst = getTagsfromDocx(para);

//				logger.info("replaceDocxTagWithCell TagLst: "+ TagLst.toString()+" "+SingleTagObj.get("tagname"));
				for (String Tag : TagLst) {

					for (Map<String, Object> SingleTagObj2 : SingleTagObj1) {
						String stringtagname = (String) SingleTagObj2.get("tagname");
						stringtagname = stringtagname.replace(" ", "_");
						stringtagname = SheetName + "_" + stringtagname;

						if (Tag.equals("<<" + stringtagname + ">>")) {
							SingleTagObj.putAll(SingleTagObj2);

						} else if (Tag.equals("<<$" + stringtagname + "$>>")) {
							SingleTagObj.putAll(SingleTagObj2);
						}
					}
					System.out.println("replaceDocxTagWithCell" + Tag);
					if (!Tag.isEmpty()) {
						String TagNameST = (String) SingleTagObj.get("tagname");
						TagNameST = TagNameST.replace(" ", "_");
						TagNameST = SheetName + "_" + TagNameST;
						if (Tag.equals("<<" + TagNameST + ">>")) {
							isDone = true;
							replaceTagWithString(document, para, Tag, SingleTagObj, SheetName, false);
						} else if (Tag.equals("<<$" + TagNameST + "$>>")) {
							isDone = true;
							List<Map<String, Object>> SequenceTagLstTemp = new ArrayList<Map<String, Object>>();
							String newKey = "<<$" + TagNameST + "$>>";
							if (sequenceTagList.containsKey(newKey)) {
								SequenceTagLstTemp = sequenceTagList.get(newKey);
							}
							Map<String, Object> tagData = (Map<String, Object>) SingleTagObj.get("Data");
//							Boolean alreadyPresent = false;
//							for(Map<String, Object> SequenceTagObjTemp : SequenceTagLstTemp) {
//								Map<String, Object> sequenveLstCr0_0 = (Map<String, Object>) SequenceTagObjTemp.get("0_0");
//								Map<String, Object> tagData0_0 = (Map<String, Object>) tagData.get("0_0");
//								if(!alreadyPresent && tagData0_0.get("value").equals(sequenveLstCr0_0.get("value"))) {
//									alreadyPresent = true;
//									break;
//								}
//								logger.info("replaceDocxTagWithCell tagData0_0: "+ tagData0_0.get("value"));
//								
//							}
//							String tagData0_0 = (String) tagData.get("0_0");
//							if(!alreadyPresent) {
							SequenceTagLstTemp.add(tagData);
							sequenceTagList.put(newKey, SequenceTagLstTemp);
//							}
							if (!sequenceTagList.isEmpty()) {
								replaceDocxTagWithSequenceData(sequenceTagList, document);
							}
						}
					}
				}
			}
		}
		if (!isDone) {
			for (XWPFTable tbl : document.getTables()) {
				for (XWPFTableRow row : tbl.getRows()) {
					for (XWPFTableCell cell : row.getTableCells()) {
						for (XWPFParagraph para : cell.getParagraphs()) {
							// String Tag = getTagsfromDocx(para);
							List<String> TagLst = getTagsfromDocx(para);
							for (String Tag : TagLst) {

								for (Map<String, Object> SingleTagObj2 : SingleTagObj1) {
									String stringtagname = (String) SingleTagObj2.get("tagname");
									stringtagname = stringtagname.replace(" ", "_");
									stringtagname = SheetName + "_" + stringtagname;
									if (Tag.equals("<<" + stringtagname + ">>")) {
										SingleTagObj.putAll(SingleTagObj2);
									} else if (Tag.equals("<<$" + stringtagname + "$>>")) {
										SingleTagObj.putAll(SingleTagObj2);
									}
								}

								System.out.println("replaceDocxTagWithCell" + Tag);
								if (!Tag.isEmpty()) {
									String TagNameST = (String) SingleTagObj.get("tagname");
									TagNameST = TagNameST.replace(" ", "_");
									TagNameST = SheetName + "_" + TagNameST;
									if (Tag.equals("<<" + TagNameST + ">>")) {
										isDone = true;
										replaceTagWithString(document, para, Tag, SingleTagObj, SheetName, false);
									} else if (Tag.equals("<<$" + TagNameST + "$>>")) {
										isDone = true;
										List<Map<String, Object>> SequenceTagLstTemp = new ArrayList<Map<String, Object>>();
										String newKey = "<<$" + TagNameST + "$>>";
										if (sequenceTagList.containsKey(newKey)) {
											SequenceTagLstTemp = sequenceTagList.get(newKey);
										}
										Map<String, Object> tagData = (Map<String, Object>) SingleTagObj.get("Data");
//										Boolean alreadyPresent = false;
//										for(Map<String, Object> SequenceTagObjTemp : SequenceTagLstTemp) {
//											Map<String, Object> sequenveLstCr0_0 = (Map<String, Object>) SequenceTagObjTemp.get("0_0");
//											Map<String, Object> tagData0_0 = (Map<String, Object>) tagData.get("0_0");
//											if(!alreadyPresent && tagData0_0.get("value") == tagData0_0.get("value")) {
//												alreadyPresent = true;
//												break;
//											}
//											logger.info("replaceDocxTagWithCell tagData0_0: "+ tagData0_0.get("value"));
//											
//										}
//										String tagData0_0 = (String) tagData.get("0_0");
//										if(!alreadyPresent) {
										SequenceTagLstTemp.add(tagData);
										sequenceTagList.put(newKey, SequenceTagLstTemp);
//										}
										if (!sequenceTagList.isEmpty()) {
											replaceDocxTagWithSequenceData(sequenceTagList, document);
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}

	public List<String> getTagsfromDocx(XWPFParagraph para) {
//		String TagString = "";
		List<String> TagString = new ArrayList<String>();
		String[] WordsInPara = para.getText().split(" ");
		String wordWithSpace = "";
		boolean continueConcat = false;
		for (String perWord : WordsInPara) {
			if (perWord.startsWith("<<") && perWord.endsWith(">>")) {
				TagString.add(perWord);
			} else if (perWord.contains("<<") && perWord.contains(">>") && wordWithSpace.length() == 0) {
				TagString.add(perWord.substring(perWord.indexOf("<<"), perWord.indexOf(">>") + 2));
			} else if (perWord.startsWith("<<") && !continueConcat) {
				continueConcat = true;
				wordWithSpace = perWord;
			} else if (perWord.endsWith(">>") && continueConcat) {
				wordWithSpace += " " + perWord;
				TagString.add(wordWithSpace);
				continueConcat = false;
			} else if (perWord.contains(">><<") && continueConcat) {
				wordWithSpace += " " + perWord.substring(0, perWord.indexOf(">>") + 2);
				TagString.add(wordWithSpace);
				wordWithSpace = perWord.substring(perWord.indexOf("<<"), perWord.length());
			} else if (perWord.contains("<<") && !continueConcat) {
				continueConcat = true;
				wordWithSpace = perWord.substring(perWord.indexOf("<<"), perWord.length());
			} else if (perWord.contains(">>") && continueConcat) {
				wordWithSpace += " " + perWord.substring(0, perWord.indexOf(">>") + 2);
				TagString.add(wordWithSpace);
				continueConcat = false;
			} else if (continueConcat) {
				wordWithSpace += " " + perWord;
			}
		}
		return TagString;
	}

	@SuppressWarnings("unchecked")
	public void replaceTagWithString(XWPFDocument document, XWPFParagraph para, String Tag,
			Map<String, Object> SingleTagObj, String SheetName, boolean isForTable) {
		List<XWPFRun> runs = para.getRuns();
		int tagleftcol = (int) SingleTagObj.get("tagleftcol");
		int tagtoprow = (int) SingleTagObj.get("tagtoprow");
		int tagbottomrow = (int) SingleTagObj.get("tagbottomrow");
		int tagrightcol = (int) SingleTagObj.get("tagrightcol");
		String tagname = (String) SingleTagObj.get("tagname");
		List<Map<String, Integer>> MergeArrayObj = new ArrayList<Map<String, Integer>>();
		if (SingleTagObj.containsKey("MergeArrayObj")) {
			MergeArrayObj = (List<Map<String, Integer>>) SingleTagObj.get("MergeArrayObj");
		}
		tagname = tagname.replace(" ", "_");
		tagname = SheetName + "_" + tagname;
		Map<String, Object> tagData = (Map<String, Object>) SingleTagObj.get("Data");
		boolean isTable = false;
		if (tagleftcol < tagrightcol || tagtoprow < tagbottomrow) {
			if (MergeArrayObj.size() > 0) {
				if (MergeArrayObj.get(0).containsKey("isTable")) {
					if (MergeArrayObj.get(0).get("isTable") == 0) {
						if (isForTable) {
							isTable = true;
						} else {
							isTable = false;
						}
					}
				} else {
					isTable = true;
				}
			} else {
				isTable = true;
			}
		}
		if (runs != null) {
			for (XWPFRun r : runs) {
				String text = r.getText(0);
				if (text != null) {
					if (text.contains(Tag) && Tag.equals("<<" + tagname + ">>")) {
						System.out.println(text);
//						System.out.println(Tag);
						if (isTable) {
							XmlCursor cursor = para.getCTP().newCursor();// this is the key!
							XWPFTable table = document.insertNewTbl(cursor);
							if (table != null) {
								int columnCount = 1;
								int rowCount = 1;
								int totalCol = tagrightcol - tagleftcol;
								int totalRow = tagbottomrow - tagtoprow;
								XWPFTableRow sheetRow = null;
								for (int rowIndex = 0; rowIndex <= totalRow; rowIndex++) {
									if (rowIndex < rowCount) {
										sheetRow = table.getRow(rowIndex);
									} else {
										sheetRow = table.createRow();
										rowCount++;
									}
									XWPFTableCell sheetRowCell = null;
									for (int cellIndex = 0; cellIndex <= totalCol; cellIndex++) {
										if (cellIndex < columnCount) {
											sheetRowCell = sheetRow.getCell(cellIndex);
										} else {
											sheetRowCell = sheetRow.createCell();
											columnCount++;
										}
										String key = rowIndex + "_" + cellIndex;
										Map<String, Object> dataObj = (Map<String, Object>) tagData.get(key);
									
										String value = "";
										if (dataObj.get("value") instanceof String) {
											value = (String) dataObj.get("value");
										} else if (dataObj.get("value") instanceof Integer) {
											value = Integer.toString((int) dataObj.get("value"));
										} else if (dataObj.get("value") instanceof Double) {
											value = Double.toString((double) dataObj.get("value"));
										} else if (dataObj.get("value") instanceof Date) {
											DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
											value = dateFormat.format(dataObj.get("value"));
										}
										if (!MergeArrayObj.isEmpty()) {
											for (int MergeArrayObjIndex = 0; MergeArrayObjIndex < MergeArrayObj
													.size(); MergeArrayObjIndex++) {
												Map<String, Integer> MergeArrayEachObj = MergeArrayObj
														.get(MergeArrayObjIndex);
												if (rowIndex == MergeArrayEachObj.get("startingRow")
														&& cellIndex == MergeArrayEachObj.get("startingCol")) {
													sheetRowCell.setText(value);
												} else if (rowIndex != MergeArrayEachObj.get("startingRow")) {
													sheetRowCell.setText(value);
												}
											}
										} else {
											sheetRowCell.setText(value);
										}
										// if(dataObj.containsKey("bgcolor")) {
										// sheetRowCell.getCTTc().addNewTcPr().addNewShd().setFill(dataObj.containsKey("bgcolor"));
										// }
										// sheetRowCell.getCTTc().getTcPr().addNewGridSpan();
										// sheetRowCell.getCTTc().getTcPr().getGridSpan().setVal(BigInteger.valueOf((long)span));
									}
								}
								if (!MergeArrayObj.isEmpty()) {
									for (int MergeArrayObjIndex = 0; MergeArrayObjIndex < MergeArrayObj
											.size(); MergeArrayObjIndex++) {
										Map<String, Integer> MergeArrayEachObj = MergeArrayObj.get(MergeArrayObjIndex);

//										if(MergeArrayEachObj.get("startingCol") < MergeArrayEachObj.get("endingCol")) {
										if (MergeArrayEachObj.get("startingRow") < MergeArrayEachObj.get("endingRow")
												&& MergeArrayEachObj.get("startingCol") < MergeArrayEachObj
														.get("endingCol")) {
											mergeCellsBothHorizonalandVertical(table,
													MergeArrayEachObj.get("startingCol"),
													MergeArrayEachObj.get("endingCol"),
													MergeArrayEachObj.get("startingRow"),
													MergeArrayEachObj.get("endingRow"));
										} else if (MergeArrayEachObj.get("startingRow") < MergeArrayEachObj
												.get("endingRow")) {
											mergeCellVertically(table, MergeArrayEachObj.get("startingCol"),
													MergeArrayEachObj.get("startingRow"),
													MergeArrayEachObj.get("endingRow"));
										} else if (MergeArrayEachObj.get("startingCol") < MergeArrayEachObj
												.get("endingCol")) {
											mergeCellsHorizonal(table, MergeArrayEachObj.get("startingRow"),
													MergeArrayEachObj.get("startingCol"),
													MergeArrayEachObj.get("endingCol"));
										}
									}
								}
								text = text.replace(Tag, "");
								r.setText(text, 0);
							}
						} else {
							Map<String, Object> dataObj = (Map<String, Object>) tagData.get("0_0");
						
							String value = "";
							if (dataObj.get("value") instanceof String) {
								value = (String) dataObj.get("value");
							} else if (dataObj.get("value") instanceof Integer) {
								value = Integer.toString((int) dataObj.get("value"));
							} else if (dataObj.get("value") instanceof Double) {
								value = Double.toString((double) dataObj.get("value"));
							} else if (dataObj.get("value") instanceof Date) {
								DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
								value = dateFormat.format(dataObj.get("value"));
							}
							text = text.replace(Tag, value);
							r.setText(text, 0);
						}
					}
				}
			}
		}
	}

	public void mergeCellsBothHorizonalandVertical(XWPFTable table, int fromCol, int toCol, int fromRow, int toRow) {
		for (int rowIndex = fromRow; rowIndex <= toRow; rowIndex++) {
			CTVMerge vmerge = CTVMerge.Factory.newInstance();
			for (int colIndex = fromCol; colIndex <= toCol; colIndex++) {
				XWPFTableCell cell = table.getRow(rowIndex).getCell(colIndex);
				if (rowIndex == fromRow) {
					// The first merged cell is set with RESTART merge value
					vmerge.setVal(STMerge.RESTART);
				} else {
					// Cells which join (merge) the first one, are set with CONTINUE
					vmerge.setVal(STMerge.CONTINUE);
					// and the content should be removed
					for (int i = cell.getParagraphs().size(); i > 0; i--) {
						cell.removeParagraph(0);
					}
					cell.addParagraph();
				}
				// Try getting the TcPr. Not simply setting an new one every time.
				CTTcPr tcPr = cell.getCTTc().getTcPr();
				if (tcPr == null)
					tcPr = cell.getCTTc().addNewTcPr();
				tcPr.setVMerge(vmerge);

				CTHMerge hMerge = tcPr.addNewHMerge();
				if (colIndex == fromCol) {
					// The first merged cell is set with RESTART merge value
					hMerge.setVal(STMerge.RESTART);
				} else {
					// Cells which join (merge) the first one, are set with CONTINUE
					hMerge.setVal(STMerge.CONTINUE);
				}
			}
		}

	}

	public void mergeCellsHorizonal(XWPFTable table, int row, int fromCol, int toCol) {
		for (int colIndex = fromCol; colIndex <= toCol; colIndex++) {
			XWPFTableCell cell = table.getRow(row).getCell(colIndex);
			CTTcPr tcPr = cell.getCTTc().getTcPr();
			if (null == tcPr)
				tcPr = cell.getCTTc().addNewTcPr();
			CTHMerge hMerge = tcPr.addNewHMerge();
			if (colIndex == fromCol) {
				// The first merged cell is set with RESTART merge value
				hMerge.setVal(STMerge.RESTART);
			} else {
				// Cells which join (merge) the first one, are set with CONTINUE
				hMerge.setVal(STMerge.CONTINUE);
			}
		}
	}

	public void mergeCellVertically(XWPFTable table, int col, int fromRow, int toRow) {
		for (int rowIndex = fromRow; rowIndex <= toRow; rowIndex++) {
			XWPFTableCell cell = table.getRow(rowIndex).getCell(col);
			CTVMerge vmerge = CTVMerge.Factory.newInstance();
			if (rowIndex == fromRow) {
				// The first merged cell is set with RESTART merge value
				vmerge.setVal(STMerge.RESTART);
			} else {
				// Cells which join (merge) the first one, are set with CONTINUE
				vmerge.setVal(STMerge.CONTINUE);
				// and the content should be removed
				for (int i = cell.getParagraphs().size(); i > 0; i--) {
					cell.removeParagraph(0);
				}
				cell.addParagraph();
			}
			// Try getting the TcPr. Not simply setting an new one every time.
			CTTcPr tcPr = cell.getCTTc().getTcPr();
			if (tcPr == null)
				tcPr = cell.getCTTc().addNewTcPr();
			tcPr.setVMerge(vmerge);
		}
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getTagInfofromSheet(String excelData) {
		List<Map<String, Object>> rtnTagData = new ArrayList<Map<String, Object>>();
		Object objMap = null;
		try {
			objMap = new JSONParser(JSONParser.MODE_PERMISSIVE).parse(excelData);
		
			JSONObject sheetsDataObj = (JSONObject) objMap;
			JSONArray tags = new JSONArray();
			JSONArray sheets = (JSONArray) sheetsDataObj.get("sheets");
			List<Map<String, Integer>> MergeArrayObj = new ArrayList<Map<String, Integer>>();
			for (int sheetsIndex = 0; sheetsIndex < sheets.size(); sheetsIndex++) {
				JSONObject selectedSheet = (JSONObject) sheets.get(sheetsIndex);
				if (selectedSheet.containsKey("mergedCells")) {
					List<String> mergedCellsArray = (List<String>) selectedSheet.get("mergedCells");
					for (int mergeArrayIndex = 0; mergeArrayIndex < mergedCellsArray.size(); mergeArrayIndex++) {
						String[] splitAddresses = ((String) mergedCellsArray.get(mergeArrayIndex)).split(":");
						Map<String, Integer> mergeObj = new HashMap<String, Integer>();
						for (int splitAddressesIndex = 0; splitAddressesIndex < splitAddresses.length; splitAddressesIndex++) {
							String splitAdd = splitAddresses[splitAddressesIndex];
							String strData = "";
							int mergeColIndex = 0;
							int mergeRowIndex = 0;
							for (int charIndex = 0; charIndex <= splitAdd.length(); charIndex++) {
								if (Character.isDigit(splitAdd.charAt(charIndex))) {
									charIndex = splitAdd.length();
									mergeRowIndex = (Integer.parseInt(splitAdd.replace(strData, "")) - 1);
									break;
								} else {
									strData += splitAdd.charAt(charIndex);
									mergeColIndex = mergeColIndex + (((int) splitAdd.charAt(charIndex)) - 65);
								}
							}
							if (splitAddressesIndex == 0) {
								mergeObj.put("startingRow", mergeRowIndex);
								mergeObj.put("startingCol", mergeColIndex);
							} else {
								mergeObj.put("endingRow", mergeRowIndex);
								mergeObj.put("endingCol", mergeColIndex);
							}
						}
						MergeArrayObj.add(mergeObj);
					}
				}
			}
		
			if (sheetsDataObj.containsKey("tags")) {
				tags = (JSONArray) sheetsDataObj.get("tags");
//				sheets = (JSONArray) sheetsDataObj.get("sheets");
				int tagsCount = tags.size();
				for (int tagId = 0; tagId < tagsCount; tagId++) {
					Map<String, Object> eachTag = (Map<String, Object>) tags.get(tagId);
					int tagleftcol = (int) eachTag.get("tagleftcol");
					int tagtoprow = (int) eachTag.get("tagtoprow");
					int tagbottomrow = (int) eachTag.get("tagbottomrow");
					int tagrightcol = (int) eachTag.get("tagrightcol");
					int tagsheetindex = (int) eachTag.get("tagsheetindex");
					JSONObject selectedSheet = (JSONObject) sheets.get(tagsheetindex);
					JSONArray rowsArray = (JSONArray) selectedSheet.get("rows");
					// Sort the JSONArray based on the "age" key
					Collections.sort(rowsArray, Comparator.comparing(o -> ((JSONObject) o).get("index").toString()));					
					Map<String, Object> tagData = new HashMap<String, Object>();
					int rowCount = 0;
					for (int rowIndex = 0; rowIndex < rowsArray.size(); rowIndex++) {
						JSONObject rowData = (JSONObject) rowsArray.get(rowIndex);
						int originalRowIndex = (int) rowData.get("index");
						if (tagtoprow <= originalRowIndex && originalRowIndex <= tagbottomrow) {
							JSONArray cellsArray = (JSONArray) rowData.get("cells");
							
//							System.out.println("getTagInfofromSheet : cellsArray" + cellsArray);
							int cellCount = 0;
							for (int cellIndex = 0; cellIndex < cellsArray.size(); cellIndex++) {
								JSONObject cellObject = (JSONObject) cellsArray.get(cellIndex);
//								System.out.println("getTagInfofromSheet : cellObject" + cellObject);
								int originalCellIndex = (int) cellObject.get("index");
								if (tagleftcol <= originalCellIndex && originalCellIndex <= tagrightcol) {
//									System.out.println("getTagInfofromSheet : cellObject Value " + cellObject.get("value"));
									Map<String, Object> cellData = new HashMap<String, Object>();
									String  format =null;
									if(cellObject.get("value") != null) {
										if(cellObject.get("format") != null && cellObject.get("format").equals("0.00")) {
											format=String.format("%,.2f", cellObject.get("value"));
										}else if(cellObject.get("format") != null && cellObject.get("format").equals("0.000"))	{
											format=String.format("%,.3f", cellObject.get("value"));
										}
									}
									cellData.put("rowIndex", rowCount);
									cellData.put("colIndex", cellCount);
									if(format != null) {
										cellData.put("value",format);	
									}else {
										cellData.put("value", cellObject.get("value"));	
									}
									
									if (cellObject.containsKey("background")) {
										cellData.put("bgcolor",
												((String) cellObject.get("background")).replace("#", ""));
									}
									tagData.put(rowCount + "_" + cellCount, cellData);
									cellCount++;
								}
							}
							rowCount++;
						}
					}
					if (!MergeArrayObj.isEmpty()) {
						List<Map<String, Integer>> tempMergeArray = new ArrayList<Map<String, Integer>>();
						for (int MergeArrayObjIndex = 0; MergeArrayObjIndex < MergeArrayObj
								.size(); MergeArrayObjIndex++) {
							Map<String, Integer> MergeArrayEachObj = MergeArrayObj.get(MergeArrayObjIndex);
							if (MergeArrayEachObj.get("startingRow") == tagtoprow
									&& MergeArrayEachObj.get("startingCol") == tagleftcol) {
								if (tagtoprow != tagbottomrow) {
									if (tagtoprow <= MergeArrayEachObj.get("startingRow")
											&& tagbottomrow >= MergeArrayEachObj.get("endingRow")
											&& tagleftcol <= MergeArrayEachObj.get("startingCol")
											&& tagrightcol >= MergeArrayEachObj.get("endingCol")) {
										Map<String, Integer> tempMergeObj = new HashMap<String, Integer>();
										tempMergeObj.put("startingRow",
												MergeArrayEachObj.get("startingRow") - tagtoprow);
										tempMergeObj.put("endingRow", MergeArrayEachObj.get("endingRow") - tagtoprow);
										tempMergeObj.put("startingCol",
												MergeArrayEachObj.get("startingCol") - tagleftcol);
										tempMergeObj.put("endingCol", MergeArrayEachObj.get("endingCol") - tagleftcol);
										tempMergeArray.add(tempMergeObj);
										// eachTag.put("MergeArrayObj", MergeArrayEachObj);
									}
								} else {
									Map<String, Integer> tempMergeObj = new HashMap<String, Integer>();
									tempMergeObj.put("isTable", 0);
									tempMergeArray.add(tempMergeObj);
								}
							}
						}
						eachTag.put("MergeArrayObj", tempMergeArray);
					}
					eachTag.put("Data", tagData);
					rtnTagData.add(eachTag);
				}
//				logger.info("getTagInfofromSheet : rtnTagData" + rtnTagData);
//				logger.info("getTaggedDataFromSheet : " + sheetsDataObj);
//				logger.info("getTaggedDataFromSheet : " + tags);
//				logger.info("getTaggedDataFromSheet : " + tagsCount);
//				
			}
		} catch (Exception e) {
			
		}
		return rtnTagData;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getTaggedDataFromSheet(String SheetData) {
		Map<String, Object> sheetProps = new HashMap<>();
		Map<Integer, Object> CellDatas = new HashMap<>();
		Object objMap = null;
		try {
			objMap = new JSONParser(JSONParser.MODE_PERMISSIVE).parse(SheetData);
			JSONObject sheetDataObj = (JSONObject) objMap;
			JSONArray sheets = (JSONArray) sheetDataObj.get("sheets");
			boolean withTag = false;
			int sheetsCount = sheets.size();
			if (sheetDataObj.containsKey("tags")) {
				withTag = true;
				JSONArray tags = (JSONArray) sheetDataObj.get("tags");
				int tagsCount = tags.size();
			
				for (int i = 0; i < sheetsCount; i++) {
					JSONObject sheetObj = (JSONObject) sheets.get(i);
					for (int tagId = 0; tagId < tagsCount; tagId++) {
						JSONObject eactTag = (JSONObject) tags.get(tagId);
						if ((int) eactTag.get("tagsheetindex") + 1 == i) {
							int tagleftcol = (int) eactTag.get("tagleftcol");
							int tagtoprow = (int) eactTag.get("tagtoprow");
							int tagbottomrow = (int) eactTag.get("tagbottomrow");
							int tagrightcol = (int) eactTag.get("tagrightcol");
							JSONArray rows = (JSONArray) sheetObj.get("rows");
							for (int rowIndex = tagtoprow; rowIndex <= (tagtoprow + (tagbottomrow - tagtoprow)
									+ 1); rowIndex++) {
								JSONObject rowObj = (JSONObject) rows.get(rowIndex - 1);
								JSONArray cells = (JSONArray) rowObj.get("cells");
								for (int colIndex = tagleftcol; colIndex <= (tagleftcol
										+ (tagrightcol - tagleftcol)); colIndex++) {
									JSONObject cellObj = (JSONObject) cells.get(colIndex);
									Map<Integer, Object> SheetVal = new HashMap<>();
									Map<Integer, Object> tagSet = new HashMap<>();
									Map<Integer, Object> RowVal = new HashMap<>();
									if (CellDatas.containsKey(i)) {
										SheetVal = (Map<Integer, Object>) CellDatas.get(i);
										if (SheetVal.containsKey(tagId)) {
											tagSet = (Map<Integer, Object>) SheetVal.get(tagId);
											if (tagSet.containsKey(rowIndex)) {
												RowVal = (Map<Integer, Object>) tagSet.get(rowIndex);
											}
										}
									}
									Map<String, Object> cellProps = new HashMap<>();
									if (!String.valueOf(cellObj.get("value")).isEmpty()
											&& cellObj.get("value") != null) {
										cellProps.put("value", cellObj.get("value"));
									} else {
										cellProps.put("value", null);
									}
									if (cellObj.containsKey("background")) {
										cellProps.put("background", cellObj.get("background"));
									}
									RowVal.put(colIndex, cellProps);
									tagSet.put(rowIndex, RowVal);
									SheetVal.put(tagId, tagSet);
									CellDatas.put(i, SheetVal);
								}
							}
						}
					}
				}
			} else {
				withTag = false;
				for (int i = 0; i < sheetsCount; i++) {
					JSONObject sheetObj = (JSONObject) sheets.get(i);
					JSONArray rows = (JSONArray) sheetObj.get("rows");
					int rowsCount = rows.size();
					sheetProps.put("rowCount", rowsCount);

					for (int j = 0; j < rowsCount; j++) {
						JSONObject rowObj = (JSONObject) rows.get(j);
						JSONArray cells = (JSONArray) rowObj.get("cells");
						int cellsCount = cells.size();
						sheetProps.put("colCount", cellsCount);
						for (int k = 0; k < cellsCount; k++) {
							JSONObject cellObj = (JSONObject) cells.get(k);
							if (cellObj.containsKey("value")) {
								if (!String.valueOf(cellObj.get("value")).isEmpty() && cellObj.get("value") != null) {
									Map<Integer, Object> SheetVal = new HashMap<>();
									Map<Integer, Object> RowVal = new HashMap<>();
									if (CellDatas.containsKey(i)) {
										SheetVal = (Map<Integer, Object>) CellDatas.get(i);
										if (SheetVal.containsKey(j)) {
											RowVal = (Map<Integer, Object>) SheetVal.get(j);
										}
									}
									Map<String, Object> cellProps = new HashMap<>();
									cellProps.put("value", cellObj.get("value"));
									if (cellObj.containsKey("background")) {
										cellProps.put("background", cellObj.get("background"));
									}
									RowVal.put(k, cellProps);
									SheetVal.put(j, RowVal);
									CellDatas.put(i, SheetVal);
//								logger.info("getDataFromSheet Value " +ObjRowsCells.get("value"));
								}
							}
						}
					}
				}
			}
		
			sheetProps.put("CellData", CellDatas);
			sheetProps.put("withTag", withTag);
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		return sheetProps;
	}

	@SuppressWarnings({ "unchecked" })
	private void updateSheetDatainDocx(Map<String, Object> sheetProps, File fileData, XWPFDocument document)
			throws FileNotFoundException {
		try {
			Map<Integer, Object> sheets = (Map<Integer, Object>) sheetProps.get("CellData");
			boolean withTag = (boolean) sheetProps.get("withTag");
			if (withTag) {
		
				for (Entry<Integer, Object> SheetEntry : sheets.entrySet()) {

					Map<Integer, Object> tagSet = (Map<Integer, Object>) SheetEntry.getValue();
					for (Entry<Integer, Object> tagSetEntry : tagSet.entrySet()) {
						Map<Integer, Object> rows = (Map<Integer, Object>) tagSetEntry.getValue();
						document.createParagraph();
						XWPFTable table = document.createTable();
						int totalCellIndex = 0;
						int rowIndex = 0;
						for (Entry<Integer, Object> rowsEntry : rows.entrySet()) {
							XWPFTableRow sheetRow = null;
							if (rowIndex == 0) {
								sheetRow = table.getRow(0);
								rowIndex++;
							} else {
								sheetRow = table.createRow();
							}
							Map<Integer, Object> cells = (Map<Integer, Object>) rowsEntry.getValue();
							int cellIndex = 0;
							for (Entry<Integer, Object> cellEntry : cells.entrySet()) {
								XWPFTableCell sheetRowCell = null;
								if (cellIndex <= totalCellIndex) {
									sheetRowCell = sheetRow.getCell(cellIndex);
									cellIndex++;
								} else {
									sheetRowCell = sheetRow.addNewTableCell();
									cellIndex++;
									totalCellIndex++;
								}
								Map<String, Object> cellData = (Map<String, Object>) cellEntry.getValue();
						
								if (cellData.get("value") instanceof String) {
									sheetRowCell.setText((String) cellData.get("value"));
								} else if (cellData.get("value") != null) {
									sheetRowCell.setText(String.valueOf(cellData.get("value")));
								}
							}
						}
					}
				}
			} else {
		
				for (Entry<Integer, Object> SheetEntry : sheets.entrySet()) {
					document.createParagraph();
					XWPFTable table = document.createTable();
					int totalCellIndex = 0;
					Map<Integer, Object> rows = (Map<Integer, Object>) SheetEntry.getValue();
					int rowIndex = 0;
					for (Entry<Integer, Object> rowsEntry : rows.entrySet()) {
						XWPFTableRow sheetRow = null;
						if (rowIndex == 0) {
							sheetRow = table.getRow(0);
							rowIndex++;
						} else {
							sheetRow = table.createRow();
						}
						Map<Integer, Object> cells = (Map<Integer, Object>) rowsEntry.getValue();
						int cellIndex = 0;
						for (Entry<Integer, Object> cellEntry : cells.entrySet()) {
							XWPFTableCell sheetRowCell = null;
							if (cellIndex <= totalCellIndex) {
								sheetRowCell = sheetRow.getCell(cellIndex);
								cellIndex++;
							} else {
								sheetRowCell = sheetRow.addNewTableCell();
								totalCellIndex++;
							}
							Map<String, Object> cellData = (Map<String, Object>) cellEntry.getValue();
							if (cellData.get("value") instanceof String) {
								sheetRowCell.setText((String) cellData.get("value"));
							} else if (cellData.get("value") instanceof Double) {
								sheetRowCell.setText(String.valueOf(cellData.get("value")));
							}
						}
					}
				}
			}
		} catch (Exception e) {
			
		}
	}

	public Map<String, Object> Getorderbytype(Map<String, Object> objorder) {
		ObjectMapper objm = new ObjectMapper();

		Map<String, Object> mapOrders = new HashMap<String, Object>();
		LSuserMaster LsuserMasterObj = LSuserMasterRepositoryObj
				.findByusercode((int) objorder.get("lsuserMasterUserCode"));
		if (LsuserMasterObj.getUsername().equals("Administrator")) {

			List<LSlogilablimsorderdetail> Pending = lslogilablimsorderdetailRepository
					.findByFiletypeAndOrderflagOrderByBatchcodeDesc((int) objorder.get("filetype"), "N");
			Collections.reverse(Pending);

			List<LSlogilablimsorderdetail> Completed = lslogilablimsorderdetailRepository
					.findByFiletypeAndOrderflagOrderByBatchcodeAsc((int) objorder.get("filetype"), "R");

			Collections.reverse(Completed);
			mapOrders.put("Pending", Pending);
			mapOrders.put("Completed", Completed);
		} else {
			List<LSuserteammapping> lstteammap = lsuserteammappingRepository.findBylsuserMaster(LsuserMasterObj);
			List<LSusersteam> lstteam = lsusersteamRepository.findByLsuserteammappingInAndLssitemaster(lstteammap,LsuserMasterObj.getLssitemaster());
			List<LSMultiusergroup> objtemp = LSMultiusergroupRepositery.findByusercode(LsuserMasterObj.getUsercode());
			List<LSworkflowgroupmapping> lsworkflowgroupmapping = lsworkflowgroupmappingRepository
					.findBylsusergroup(objtemp.get(0).getLsusergroup());

			List<LSprojectmaster> lstproject = lsprojectmasterRepository.findByLsusersteamIn(lstteam);
			List<LSworkflow> lsworkflow = lsworkflowRepository
					.findByLsworkflowgroupmappingInOrderByWorkflowcodeDesc(lsworkflowgroupmapping);

			List<LSlogilablimsorderdetail> Pending = new ArrayList<LSlogilablimsorderdetail>();
			List<LSlogilablimsorderdetail> Completed = new ArrayList<LSlogilablimsorderdetail>();
			if ((int) objorder.get("filetype") == 0) {
				Pending = lslogilablimsorderdetailRepository
						.findByFiletypeAndOrderflagOrderByBatchcodeDesc((int) objorder.get("filetype"), "N");

				Completed = lslogilablimsorderdetailRepository
						.findByFiletypeAndOrderflagOrderByBatchcodeDesc((int) objorder.get("filetype"),
								 "R");

			} else {
				Pending = lslogilablimsorderdetailRepository
						.findByFiletypeAndOrderflagAndLsprojectmasterInAndLsworkflowInOrderByBatchcodeDesc(
								(int) objorder.get("filetype"), "N", lstproject, lsworkflow);

				Completed = lslogilablimsorderdetailRepository
						.findByFiletypeAndApprovelstatusAndOrderflagAndLsprojectmasterInOrderByBatchcodeAsc(
								(int) objorder.get("filetype"), 1, "R", lstproject);
			}

			Collections.reverse(Pending);
			Collections.reverse(Completed);

			if (objorder.containsKey("isMultitenant")) {
				int multitenent = objm.convertValue(objorder.get("isMultitenant"), Integer.class);
				for (LSlogilablimsorderdetail LSlogilablimsorderdetail : Completed) {

					if (multitenent == 1) {

						CloudOrderCreation file = cloudOrderCreationRepository
								.findById((long) LSlogilablimsorderdetail.getLssamplefile().getFilesamplecode());

						LSlogilablimsorderdetail.getLssamplefile().setFilecontent(file.getContent());

					} else {
						OrderCreation file = mongoTemplate.findById(
								LSlogilablimsorderdetail.getLssamplefile().getFilesamplecode(), OrderCreation.class);
						LSlogilablimsorderdetail.getLssamplefile().setFilecontent(file.getContent());
					}
				}
			}
			mapOrders.put("Pending", Pending);

			mapOrders.put("Completed", Completed);
		}

		return mapOrders;
	}

	public Map<String, Object> getSheetLSfileLst(Map<String, Object> argMap) throws IOException, InvalidKeyException, URISyntaxException, StorageException {
		Map<String, Object> MapObj = new HashMap<String, Object>();

		LSSiteMaster objSite = new LSSiteMaster();

		Integer site = (Integer) argMap.get("sitecode");

		objSite.setSitecode(site);

		List<LSfile> LSfileLst = LSfileRepositoryObj
				.findByFilecodeGreaterThanAndApprovedAndLssitemasterOrderByCreatedateDesc(1, 1, objSite);
		
		


		CloudStorageAccount storageAccount;
		CloudBlobClient blobClient = null;
		CloudBlobContainer container = null;
		CloudBlockBlob blob = null;
//		String storageConnectionString = env.getProperty("azure.storage.ConnectionString");
		storageAccount = CloudStorageAccount.parse(env.getProperty("azure.storage.ConnectionString"));
		blobClient = storageAccount.createCloudBlobClient();

//		return null;

		container = (blobClient.getContainerReference(TenantContext.getCurrentTenant()+"sheetcreation"));
//		ArrayList<Long> longList = new ArrayList<Long>();
//        
//        // Add elements to the list
//        longList.add((long) 10);
//        longList.add((long) 9);
//        longList.add((long) 8);
//        longList.add((long) 5);
//        longList.add((long) 4);
		for (LSfile LSfileObj : LSfileLst) {

			if ((int) argMap.get("isMultitenant") == 1) {
				
				CloudSheetCreation file = cloudSheetCreationRepository.findById((long) LSfileObj.getFilecode());
				
				if (file != null && file.getContainerstored() == 0) {
					LSfileObj.setFilecontent(cloudSheetCreationRepository.findById((long) LSfileObj.getFilecode()).getContent());
				}else {
					try {
						// Parse the connection string and create a blob client to interact with Blob
						// storage
//						storageAccount = CloudStorageAccount.parse(storageConnectionString);
//						blobClient = storageAccount.createCloudBlobClient();
						
				        
						blob = container.getBlockBlobReference(file.getFileuid());
						LSfileObj.setFilecontent(blob.downloadText());
					} catch (StorageException ex) {
						System.out.println(String.format("Error returned from the service. Http code: %d and error code: %s",
								ex.getHttpStatusCode(), ex.getErrorCode()));
						throw new IOException(String.format("Error returned from the service. Http code: %d and error code: %s",
								ex.getHttpStatusCode(), ex.getErrorCode()));
					} catch (Exception ex) {
						System.out.println(ex.getMessage());
					}
					LSfileObj.setFilecontent(objCloudFileManipulationservice.retrieveCloudSheets(file.getFileuid(),TenantContext.getCurrentTenant()+"sheetcreation"));
				}
				
//				CloudSheetCreation file = cloudSheetCreationRepository.findById((long) LSfileObj.getFilecode());
//				if (file != null) {
//					LSfileObj.setFilecontent(file.getContent());
//				}
			} else {

				String fileid = "file_" + LSfileObj.getFilecode();
				GridFSDBFile largefile = gridFsTemplate.findOne(new Query(Criteria.where("filename").is(fileid)));
				if (largefile == null) {
					largefile = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(fileid)));
				}

				if (largefile != null) {
					String filecontent = new BufferedReader(
							new InputStreamReader(largefile.getInputStream(), StandardCharsets.UTF_8)).lines()
									.collect(Collectors.joining("\n"));
					LSfileObj.setFilecontent(filecontent);
				} else {

					SheetCreation file = mongoTemplate.findById(LSfileObj.getFilecode(), SheetCreation.class);
					if (file != null) {
						LSfileObj.setFilecontent(file.getContent());
					}
				}

			}

		}
		
		MapObj.put("filelst", LSfileLst);

		return MapObj;
	}

	public Map<String, Object> getSheetLSfileUsingFilecode(Map<String, Object> argMap) throws IOException {
		Map<String, Object> MapObj = new HashMap<String, Object>();
//		int fileCode = (int) argMap.get("SheetFileCode");
		List<LSfile> LSfilelst = new ArrayList<LSfile>();
		String fileCode = (String) argMap.get("SheetFileCode");
		String multitenent = (String) argMap.get("isMultitenant");
		int isMultitenant = Integer.parseInt(multitenent);

		if (fileCode.contains(",")) {

			if (isMultitenant == 1) {
				String[] fileCodeArray = fileCode.split(", ");
				for (int arrayIndex = 0; arrayIndex < fileCodeArray.length; arrayIndex++) {
//					LSfilelst.add(
//							LSfileRepositoryObj.findByFilecodeAndApproved(Integer.parseInt(fileCodeArray[arrayIndex]), 1));
					LSfile objFile = LSfileRepositoryObj
							.findByFilecodeAndApproved(Integer.parseInt(fileCodeArray[arrayIndex]), 1);

					CloudSheetCreation objCreation = cloudSheetCreationRepository.findById((long) objFile.getFilecode());
					
					if (objCreation != null && objCreation.getContainerstored() == 0) {
						objFile.setFilecontent(cloudSheetCreationRepository.findById((long) objFile.getFilecode()).getContent());
					}else {
						objFile.setFilecontent(objCloudFileManipulationservice.retrieveCloudSheets(objCreation.getFileuid(),TenantContext.getCurrentTenant()+"sheetcreation"));
					}
//					CloudSheetCreation file = cloudSheetCreationRepository
//							.findById((long) Integer.parseInt(fileCodeArray[arrayIndex]));
//					if (file != null) {
//						objFile.setFilecontent(file.getContent());
//					}

					LSfilelst.add(objFile);

				}
			} else {
				String[] fileCodeArray = fileCode.split(", ");
				for (int arrayIndex = 0; arrayIndex < fileCodeArray.length; arrayIndex++) {

					LSfile objFile = LSfileRepositoryObj
							.findByFilecodeAndApproved(Integer.parseInt(fileCodeArray[arrayIndex]), 1);
					
					String fileid = "file_" + fileCode;
					GridFSDBFile largefile = gridFsTemplate.findOne(new Query(Criteria.where("filename").is(fileid)));
					if (largefile == null) {
						largefile = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(fileid)));
					}

					if (largefile != null) {
						String filecontent = new BufferedReader(
								new InputStreamReader(largefile.getInputStream(), StandardCharsets.UTF_8)).lines()
										.collect(Collectors.joining("\n"));
						objFile.setFilecontent(filecontent);
					} 
					
					else {

						SheetCreation file = mongoTemplate.findById(objFile.getFilecode(), SheetCreation.class);
						if (file != null) {
							objFile.setFilecontent(file.getContent());
						}
					}

//					SheetCreation sheetcreationObj = mongoTemplate.findById(Integer.parseInt(fileCodeArray[arrayIndex]),
//							SheetCreation.class);
//					if (sheetcreationObj != null) {
//						objFile.setFilecontent(sheetcreationObj.getContent());
//					}

					LSfilelst.add(objFile);

				}
			}

		} else {
			if (isMultitenant == 1) {
				LSfile objFile = LSfileRepositoryObj.findByFilecodeAndApproved(Integer.parseInt(fileCode), 1);
				CloudSheetCreation objCreation = cloudSheetCreationRepository.findById((long) objFile.getFilecode());
				
				if (objCreation != null && objCreation.getContainerstored() == 0) {
					objFile.setFilecontent(cloudSheetCreationRepository.findById((long) objFile.getFilecode()).getContent());
				}else {
					objFile.setFilecontent(objCloudFileManipulationservice.retrieveCloudSheets(objCreation.getFileuid(),TenantContext.getCurrentTenant()+"sheetcreation"));
				}
//				CloudSheetCreation file = cloudSheetCreationRepository.findById((long) objFile.getFilecode());
//				if (file != null) {
//					objFile.setFilecontent(file.getContent());
//				}
				LSfilelst.add(objFile);
			} else {

				LSfile objFile = LSfileRepositoryObj.findByFilecodeAndApproved(Integer.parseInt(fileCode), 1);
				String fileid = "file_" + objFile.getFilecode();
				GridFSDBFile largefile = gridFsTemplate.findOne(new Query(Criteria.where("filename").is(fileid)));
				if (largefile == null) {
					largefile = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(fileid)));
				}

				if (largefile != null) {
					String filecontent = new BufferedReader(
							new InputStreamReader(largefile.getInputStream(), StandardCharsets.UTF_8)).lines()
									.collect(Collectors.joining("\n"));
					objFile.setFilecontent(filecontent);
				} else 
				{
					SheetCreation sheetcreationObj = mongoTemplate.findById(objFile.getFilecode(), SheetCreation.class);
					if (sheetcreationObj != null) {
						objFile.setFilecontent(sheetcreationObj.getContent());
					}
//					LSfilelst.add(objFile);
				}

				LSfilelst.add(objFile);
//				
//				SheetCreation sheetcreationObj = mongoTemplate.findById(objFile.getFilecode(), SheetCreation.class);
//				if (sheetcreationObj != null) {
//					objFile.setFilecontent(sheetcreationObj.getContent());
//				}
//				LSfilelst.add(objFile);
			}

		}

		MapObj.put("fileObj", LSfilelst);
		return MapObj;
	}

	public Map<String, Object> getTemplatesLst(Map<String, Object> argMap) {
		Map<String, Object> MapObj = new HashMap<String, Object>();
		String sheetCode = (String) argMap.get("FileCodeString");
		int ismultiplesheet = (int) argMap.get("ismultiplesheet");
		String sSitecode = (String) argMap.get("lssitecode");

		int sitecode = Integer.parseInt(sSitecode);
		List<LSdocreports> LSDocReportsLst = LSdocreportsRepositoryObj
				.findAllByIsTemplateAndStatusAndIsdraftAndSheetfilecodeStringAndIsmultiplesheetAndLssitemasterAndFileNameIsNotNullOrderByCreatedateDesc(
						1,1, 0, sheetCode, ismultiplesheet, sitecode);

		MapObj.put("LSDocReportsLst", LSDocReportsLst);
		return MapObj;
	}

	public Map<String, Object> getReportDocxonVersion(Map<String, Object> argMap) {
		Map<String, Object> objMap = new HashMap<String, Object>();
		String hashKey = (String) argMap.get("hashKey");
		int versionNo = (int) argMap.get("versionNo");
		ObjectMapper newObjMapper = new ObjectMapper();
		String filePath = getDocxAbsolutePath();
		String FIleVersionName = "";
		List<LSdocreportsversionhistory> LSdocreportsversionhistorylst = newObjMapper.convertValue(
				argMap.get("LSdocreportsversionhistorylst"), new TypeReference<List<LSdocreportsversionhistory>>() {
				});
		LSdocreports LSdocreportsObj = LSdocreportsRepositoryObj
				.findByDocReportsCode(LSdocreportsversionhistorylst.get(0).getDocReportsCode());
		if (versionNo < LSdocreportsversionhistorylst.size()) {
			FIleVersionName = hashKey + "_Ver" + versionNo;
			LSdocreports hashKeyLSdocreportsObj = LSdocreportsRepositoryObj
					.findFirstByFileHashNameAndStatus(FIleVersionName, 1);
			if (hashKeyLSdocreportsObj == null) {
				LSdocreports LSdocreportsObj1 = new LSdocreports();
				LSdocreportsObj1.setDocdirectorycode(LSdocreportsObj.getDocdirectorycode());
				LSdocreportsObj1.setCreatedate(LSdocreportsObj.getCreatedate());
				LSdocreportsObj1.setCreatedBy(LSdocreportsObj.getCreatedBy());
				LSdocreportsObj1.setExtention(LSdocreportsObj.getExtention());
				LSdocreportsObj1.setFileHashName(FIleVersionName);
				LSdocreportsObj1.setVersionno(versionNo);
				LSdocreportsObj1.setFileName(LSdocreportsObj.getFileName());
				LSdocreportsObj1.setIsdraft(0);
				LSdocreportsObj1.setIsTemplate(LSdocreportsObj.getIsTemplate());
				LSdocreportsObj1.setStatus(LSdocreportsObj.getStatus());
				LSdocreportsObj1.setSheetfilecodeString(LSdocreportsObj.getSheetfilecodeString());
			
				LSdocreportsRepositoryObj.save(LSdocreportsObj1);
			}
		} else {
			FIleVersionName = hashKey;
		}
		if (LSdocreportsObj.getIsTemplate() == 1) {
			if (System.getProperty("os.name").contains("Linux") || System.getProperty("os.name").contains("LINUX")) {
				filePath += "/templates";
			} else {
				filePath += "\\templates";
			}
			if (!(new File(filePath)).exists()) {
				(new File(filePath)).mkdir();
			}
		}
		String originalFilePath = filePath + "/" + FIleVersionName + ".docx";
		File vFile = new File(originalFilePath);
		objMap.put("fileFullPath", vFile.getAbsolutePath());
		objMap.put("fileName", LSdocreportsObj.getFileName() + ".docx");
		objMap.put("fileOriginalPath", "reports/" + FIleVersionName + ".docx");
		objMap.put("hashKey", FIleVersionName);
		objMap.put("type", LSdocreportsObj.getIsTemplate());
		objMap.put("SheetFileCode", LSdocreportsObj.getSheetfilecodeString());
		objMap.put("LSdocreportsversionhistorylst", LSdocreportsversionhistorylst);
		if (argMap.containsKey("objsilentaudit")) {
			if (argMap.get("objsilentaudit") != null) {
				LScfttransaction LScfttransactionobj = new ObjectMapper().convertValue(argMap.get("objsilentaudit"),
						new TypeReference<LScfttransaction>() {
						});
				// LScfttransactionobj.setModuleName("Reports");
				// LScfttransactionobj.setComments(FileName + " Document version : "+versionNo+"
				// loaded");
				// LScfttransactionobj.setActions("Load");
				LScfttransactionobj.setSystemcoments("System Generated");
				LScfttransactionobj.setTableName("LSdocreports");
				lscfttransactionRepository.save(LScfttransactionobj);
			}
		}
		return objMap;
	}

	public List<OrderCreation> getFilecontentforSheet(List<LSlogilablimsorderdetail> lstSelectedData) {
		List<OrderCreation> fileContentLst = new ArrayList<OrderCreation>();
		for (LSlogilablimsorderdetail SelectedDataObj : lstSelectedData) {
			OrderCreation file = mongoTemplate.findById(SelectedDataObj.getLssamplefile().getFilesamplecode(),
					OrderCreation.class);
			fileContentLst.add(file);
		}
		return fileContentLst;
	}

	public void createFIle() {
//		String URLString = "http://localhost:8081/ELNdocuments/";
//		String FilePath = "C:/Program Files/Apache Software Foundation/Tomcat 8.5/webapps/ROOT/ELNdocuments/reports/link.txt";
//		File newFIle = new File(FilePath);
//		try {
//			InputStream fileInputStream = new FileInputStream(newFIle);
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} 
		URLConnection urlconnection = null;
		try {
			File file = new File(
					"C:/Program Files/Apache Software Foundation/Tomcat 8.5/webapps/ROOT/ELNdocuments/reports/link.txt");
			URL url = new URL("http://localhost:8081/Testing/link.txt");
			urlconnection = url.openConnection();
			urlconnection.setDoOutput(true);
			urlconnection.setDoInput(true);

			if (urlconnection instanceof HttpURLConnection) {
				((HttpURLConnection) urlconnection).setRequestMethod("PUT");
				((HttpURLConnection) urlconnection).setRequestProperty("Content-type", "text/plain");
				((HttpURLConnection) urlconnection).connect();
			}

			BufferedOutputStream bos = new BufferedOutputStream(urlconnection.getOutputStream());
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
			int i;
			// read byte by byte until end of stream
			while ((i = bis.read()) > -1) {
				bos.write(i);
			}
			bis.close();
			bos.close();
			System.out.println(((HttpURLConnection) urlconnection).getResponseMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {

			InputStream inputStream;
			int responseCode = ((HttpURLConnection) urlconnection).getResponseCode();
			if ((responseCode >= 200) && (responseCode <= 202)) {
				inputStream = ((HttpURLConnection) urlconnection).getInputStream();
				int j;
				while ((j = inputStream.read()) > 0) {
					System.out.println(j);
				}

			} else {
				inputStream = ((HttpURLConnection) urlconnection).getErrorStream();
			}
			((HttpURLConnection) urlconnection).disconnect();
			System.out.println(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
