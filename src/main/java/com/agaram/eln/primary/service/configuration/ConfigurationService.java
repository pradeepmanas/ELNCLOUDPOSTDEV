package com.agaram.eln.primary.service.configuration;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Service;

import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.configuration.LSConfiguration;
import com.agaram.eln.primary.repository.cfr.LScfttransactionRepository;
import com.agaram.eln.primary.repository.configuration.LSconfigurationRepository;
import com.agaram.eln.primary.service.report.ReportsService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@EnableJpaRepositories(basePackageClasses = LSConfiguration.class)
public class ConfigurationService {

	@Autowired
	LSconfigurationRepository LSconfigurationRepositoryObj;

	@Autowired
	private LScfttransactionRepository lscfttransactionRepository;

	static final Logger logger = Logger.getLogger(ReportsService.class.getName());

	public Map<String, Object> getAllConfigurations() {
		Map<String, Object> rtnObj = new HashedMap<String, Object>();
		List<LSConfiguration> listLSconfiguration = LSconfigurationRepositoryObj.findAllByStatus(1);
		rtnObj.put("configuration", listLSconfiguration);
		return rtnObj;
	}

	public Map<String, Object> getConfigurationsbyID() {
		Map<String, Object> rtnObj = new HashedMap<String, Object>();
		try {
			LSConfiguration objLSconfiguration = LSconfigurationRepositoryObj.findBySerialnoAndStatus(1, 1);
			rtnObj.put("configuration", objLSconfiguration);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		}
		return rtnObj;
	}

	public Map<String, Object> getConfigurationForFTP() {
		Map<String, Object> rtnObj = new HashedMap<String, Object>();
		LSConfiguration objLSconfiguration = LSconfigurationRepositoryObj
				.findByConfiggrouptypeAndConfigactiveAndStatus("ftp", 1, 1);
		rtnObj.put("configuration", objLSconfiguration);
		return rtnObj;
	}

	public LSConfiguration getConfigurationForDocsPath() {
		LSConfiguration objLSconfiguration = LSconfigurationRepositoryObj
				.findByConfiggrouptypeAndConfigactiveAndStatus("docxpath", 1, 1);
		return objLSconfiguration;
	}

	private boolean copyImgToDest(String fileName, String destFolderPath) {
		boolean status = false;
		try {
			String srcFolderPath = "";
			logger.info("copyImgToDest() Started ");
			if (new File(new File("").getAbsolutePath() + "\\webapps").exists()) {
				srcFolderPath = new File("").getAbsolutePath()
						+ "\\webapps\\ELN-0.0.1-SNAPSHOT\\WEB-INF\\classes\\images";
			} else {
				srcFolderPath = new File("").getAbsolutePath() + "\\src\\main\\resources\\images";
			}
			logger.info("copyImgToDest() Logo srcFolderPath: " + srcFolderPath);
			File destFile = new File(destFolderPath);
			File srcFile = new File(srcFolderPath + "\\" + fileName);
			if (srcFile.exists()) {
				if (!destFile.exists()) {
					destFile.mkdir();
				}
				destFile = new File(destFolderPath + "\\" + fileName);
				InputStream instream = new FileInputStream(srcFile);
				FileOutputStream outStream = new FileOutputStream(destFile);
				byte[] buffer = new byte[1024];
				int Length;
				while ((Length = instream.read(buffer)) > 0) {
					outStream.write(buffer, 0, Length);
				}
				instream.close();
				outStream.close();
			} else {
				logger.info("copyImgToDest() Logo images not found in  URL: " + srcFile.getAbsolutePath());
			}
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		}
		return status;
	}

	public Map<String, Object> getConfigurationForDocxInit() {
		Map<String, Object> rtnObj = new HashedMap<String, Object>();
		LSConfiguration objLSConfiguration = getConfigurationForDocsPath();
		if (objLSConfiguration == null) {
			String filePath = new File("").getAbsolutePath() + "\\webapps\\ROOT\\ELNdocuments";
			logger.info("getConfigurationForDocxInit() Logo filePath" + filePath);
			if (!new File(filePath + "\\favicon.ico").exists()) {
				copyImgToDest("favicon.ico", filePath);
				copyImgToDest("Logilab_ELN_vertical.png", filePath);
				String tempFile = new File("").getAbsolutePath() + "\\webapps\\ROOT\\ELNdocuments\\link.txt";
				File newFile = new File(tempFile);
				if (!newFile.exists()) {
					try {
						newFile.createNewFile();
						if (newFile.exists()) {
							FileWriter fw = new FileWriter(tempFile);
							fw.write("working");
							fw.close();
						}
					} catch (IOException e) {
						logger.info(e.getLocalizedMessage());
					}
				}
				logger.info("getConfigurationForDocxInit() Logo copied");
			} else {
				logger.info("getConfigurationForDocxInit() Logo present");
			}
		} else {
			String filePath = objLSConfiguration.getConfigpath();
			logger.info("getConfigurationForDocxInit() Logo filePath" + filePath);
			if (!new File(filePath + "\\favicon.ico").exists()) {
				copyImgToDest("favicon.ico", filePath);
				copyImgToDest("Logilab_ELN_vertical.png", filePath);
				String tempFile = filePath + "\\link.txt";
				File newFile = new File(tempFile);
				if (!newFile.exists()) {
					try {
						newFile.createNewFile();
						if (newFile.exists()) {
							FileWriter fw = new FileWriter(tempFile);
							fw.write("working");
							fw.close();
						}
					} catch (IOException e) {
						logger.info(e.getLocalizedMessage());
					}
				}
				logger.info("getConfigurationForDocxInit() Logo copied");
			} else {
				logger.info("getConfigurationForDocxInit() Logo present");
			}
		}
		LSConfiguration onlyofficeapiConfigObj = LSconfigurationRepositoryObj
				.findByConfiggrouptypeAndConfigactiveAndStatus("onlyofficeapi", 1, 1);
		LSConfiguration docxurlConfigObj = LSconfigurationRepositoryObj
				.findByConfiggrouptypeAndConfigactiveAndStatus("docxurl", 1, 1);
		LSConfiguration docxFTPConfigObj = LSconfigurationRepositoryObj
				.findByConfiggrouptypeAndConfigactiveAndStatus("ftp", 1, 1);
		if (onlyofficeapiConfigObj != null) {
			rtnObj.put("onlyofficeapiConfigObj", onlyofficeapiConfigObj);
			logger.info(
					"getConfigurationForDocxInit() onlyofficeapiConfigObj: " + onlyofficeapiConfigObj.getConfigpath());
		}
		if (docxurlConfigObj != null) {
			rtnObj.put("docxurlConfigObj", docxurlConfigObj);
			logger.info("getConfigurationForDocxInit() docxurlConfigObj: " + docxurlConfigObj.getConfigpath());
		}
		if (docxFTPConfigObj != null) {
			rtnObj.put("docxFTPConfigObj", docxFTPConfigObj);
			logger.info("getConfigurationForDocxInit() docxFTPConfigObj: " + docxFTPConfigObj.getConfigpath());
		}

		return rtnObj;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> AddAllConfiguration(Map<String, Object> obj) {
		Map<String, Object> rtnObj = new HashedMap<String, Object>();
		try {
			Map<String, Object> availableRecords = getConfigurationForDocxInit();
			boolean changesMade = false;
			ObjectMapper objMapper = new ObjectMapper();
			if (obj.containsKey("onlyofficeapiConfigObj")) {
				boolean canOnlyofficeapiConfigObj = false;
				LSConfiguration ConfigObj = (LSConfiguration) availableRecords.get("onlyofficeapiConfigObj");
				LSConfiguration ConfigObjNew = objMapper.convertValue(
						(Map<String, Object>) obj.get("onlyofficeapiConfigObj"), new TypeReference<LSConfiguration>() {
						});
				if (ConfigObj != null) {
					if (ConfigObj.getConfigactive() == 1) {
						if (ConfigObj.getConfigpath().equals(ConfigObjNew.getConfigpath())) {
							canOnlyofficeapiConfigObj = false;
						} else {
							ConfigObj.setConfigactive(0);
							insertConfigurations(ConfigObj);
							canOnlyofficeapiConfigObj = true;
						}
					}
				} else {
					canOnlyofficeapiConfigObj = true;
				}
				if (canOnlyofficeapiConfigObj) {
					changesMade = true;
					Map<String, Object> argMap = new HashMap<String, Object>();
					argMap.put("url", ConfigObjNew.getConfigpath());
					Map<String, Object> statusObj = testConnection(argMap);
					if (statusObj.containsKey("status")) {
						if (statusObj.get("status").equals("pass")) {
							ConfigObjNew.setConnectionstatus(1);
						} else {
							ConfigObjNew.setConnectionstatus(0);
						}
					}
					Map<String, Object> tempOBJ = insertConfigurations(ConfigObjNew);
					rtnObj.put("onlyofficeapiConfigObj", tempOBJ.get("configuration"));
				} else {
					rtnObj.put("onlyofficeapiConfigObj", ConfigObj);
				}
			} else {
				LSConfiguration ConfigObj = (LSConfiguration) availableRecords.get("onlyofficeapiConfigObj");
				if (ConfigObj != null) {
					rtnObj.put("onlyofficeapiConfigObj", ConfigObj);
				}
			}
			if (obj.containsKey("docxurlConfigObj")) {
				boolean canDocxurlConfigObj = false;
				LSConfiguration ConfigObj = (LSConfiguration) availableRecords.get("docxurlConfigObj");
				LSConfiguration ConfigObjNew = objMapper.convertValue((Map<String, Object>) obj.get("docxurlConfigObj"),
						new TypeReference<LSConfiguration>() {
						});
				if (ConfigObj != null) {
					if (ConfigObj.getConfigactive() == 1) {
						if (ConfigObj.getConfigpath().equals(ConfigObjNew.getConfigpath())) {
							canDocxurlConfigObj = false;
						} else {
							ConfigObj.setConfigactive(0);
							insertConfigurations(ConfigObj);
							canDocxurlConfigObj = true;
						}
					}
				} else {
					canDocxurlConfigObj = true;
				}
				if (canDocxurlConfigObj) {
					changesMade = true;
					Map<String, Object> LinkStatus = testUrlConnection((Map<String, Object>) obj.get("docxurlConfigObj"));
					if(LinkStatus.get("status").equals("pass")) {
						ConfigObjNew.setConnectionstatus(1);
					}else {
						ConfigObjNew.setConnectionstatus(0);
					}
					Map<String, Object> tempOBJ = insertConfigurations(ConfigObjNew);
					rtnObj.put("docxurlConfigObj", tempOBJ.get("configuration"));
				} else {
					rtnObj.put("docxurlConfigObj", ConfigObj);
				}
			} else {
				LSConfiguration ConfigObj = (LSConfiguration) availableRecords.get("docxurlConfigObj");
				if (ConfigObj != null) {
					rtnObj.put("onlyofficeapiConfigObj", ConfigObj);
				}
			}
			if (obj.containsKey("docxFTPConfigObj")) {
				LSConfiguration ConfigObj = (LSConfiguration) availableRecords.get("docxFTPConfigObj");
				LSConfiguration ConfigObjNew = objMapper.convertValue((Map<String, Object>) obj.get("docxFTPConfigObj"),
						new TypeReference<LSConfiguration>() {
						});
				boolean canDocxFTPConfigObj = false;
				if (ConfigObj != null) {
					if (ConfigObj.getConfigactive() == 1) {
						if (ConfigObj.getConfigpath().equals(ConfigObjNew.getConfigpath())
								&& ConfigObj.getConfigname().equals(ConfigObjNew.getConfigname())
								&& ConfigObj.getConfiguserpass().equals(ConfigObjNew.getConfiguserpass())) {
							canDocxFTPConfigObj = false;
						} else {
							ConfigObj.setConfigactive(0);
							insertConfigurations(ConfigObj);
							canDocxFTPConfigObj = true;
						}
					}
				} else {
					canDocxFTPConfigObj = true;
				}
//				if (ConfigObj != null) {
//					ConfigObj.setConfigactive(0);
//					insertConfigurations(ConfigObj);
//				}

				if (canDocxFTPConfigObj) {
					changesMade = true;
					Map<String, Object> argMap = new HashMap<String, Object>();
					argMap.put("configFTPPath", ConfigObjNew.getConfigpath());
					argMap.put("configFTPUser", ConfigObjNew.getConfigusername());
					argMap.put("configFTPPass", ConfigObjNew.getConfiguserpass());
					Map<String, Object> statusObj = testFTPConnection(argMap);
					if (statusObj.containsKey("status")) {
						if (statusObj.get("status").equals("pass")) {
							ConfigObjNew.setConnectionstatus(1);
						} else {
							ConfigObjNew.setConnectionstatus(0);
						}
					}
					Map<String, Object> tempOBJ = insertConfigurations(ConfigObjNew);
					rtnObj.put("docxFTPConfigObj", tempOBJ.get("configuration"));
				} else {
					rtnObj.put("docxFTPConfigObj", ConfigObj);
				}
			} else {
				LSConfiguration ConfigObj = (LSConfiguration) availableRecords.get("docxFTPConfigObj");
				if (ConfigObj != null) {
					rtnObj.put("onlyofficeapiConfigObj", ConfigObj);
				}
			}
			if (changesMade) {
				if (obj.containsKey("objsilentaudit")) {
					if (obj.get("objsilentaudit") != null) {
						LScfttransaction LScfttransactionobj = new ObjectMapper()
								.convertValue(obj.get("objsilentaudit"), new TypeReference<LScfttransaction>() {
								});
						// LScfttransactionobj.setModuleName("Reports");
						// LScfttransactionobj.setComments("Configurations Created/Updated");
						// LScfttransactionobj.setActions("Creation");
						LScfttransactionobj.setSystemcoments("System Generated");
						LScfttransactionobj.setTableName("LSConfiguration");
						lscfttransactionRepository.save(LScfttransactionobj);
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		}
		return rtnObj;
	}

	public Map<String, Object> insertConfigurations(LSConfiguration objLSconfiguration) {
		Map<String, Object> rtnObj = new HashedMap<String, Object>();
		objLSconfiguration.setStatus(1);
		rtnObj.put("configuration", LSconfigurationRepositoryObj.save(objLSconfiguration));
		return rtnObj;
	}

	TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
		}

		public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
		}
	} };

	public Map<String, Object> testConnection(Map<String, Object> argMap) throws IOException {
		Map<String, Object> rtnObj = new HashMap<String, Object>();
		try {
			String downloadUri = (String) argMap.get("url");
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
			BufferedInputStream in = new BufferedInputStream(stream);
			BufferedReader r = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
			r.readLine();
			String line1 = r.readLine();
			String line2 = r.readLine();
			if (line1.contains("Ascensio System SIA")) {
				rtnObj.put("status", "pass");
			} else if (line2.contains("Ascensio System SIA")) {
				rtnObj.put("status", "pass");
			}
			if (downloadUri.contains("https")) {
				connectionSSL.disconnect();
			} else {
				connection.disconnect();
			}
			if (argMap.containsKey("objsilentaudit")) {
				if (argMap.get("objsilentaudit") != null) {
					LScfttransaction LScfttransactionobj = new ObjectMapper().convertValue(argMap.get("objsilentaudit"),
							new TypeReference<LScfttransaction>() {
							});
					// LScfttransactionobj.setModuleName("Reports");
					// LScfttransactionobj.setComments("Docx Api connection test Status: " +
					// rtnObj.get("status"));
					// LScfttransactionobj.setActions("Load");
					LScfttransactionobj.setSystemcoments("System Generated");
					LScfttransactionobj.setTableName("LSConfiguration");
					lscfttransactionRepository.save(LScfttransactionobj);
				}
			}
		} catch (MalformedURLException | NoSuchAlgorithmException | KeyManagementException e) {
			logger.error(e.getLocalizedMessage());
			rtnObj.put("status", "fail");
		}
		return rtnObj;
	}

	public Map<String, Object> testFTPConnection(Map<String, Object> argMap) throws IOException {
		Map<String, Object> rtnObj = new HashMap<String, Object>();
		try {
			String configFTPPath = (String) argMap.get("configFTPPath");
			String configFTPUser = (String) argMap.get("configFTPUser");
			String configFTPPass = (String) argMap.get("configFTPPass");
			String[] splitString = configFTPPath.split(":");
			FTPClient client = new FTPClient();
			client.connect(splitString[0], Integer.parseInt(splitString[1]));
			boolean loginStatus = client.login(configFTPUser, configFTPPass);
			if (loginStatus) {
				rtnObj.put("status", "pass");
			} else {
				rtnObj.put("status", "fail");
			}
			logger.info("testFTPConnection() FTP Connection Status: " + loginStatus);
			if (argMap.containsKey("objsilentaudit")) {
				if (argMap.get("objsilentaudit") != null) {
					LScfttransaction LScfttransactionobj = new ObjectMapper().convertValue(argMap.get("objsilentaudit"),
							new TypeReference<LScfttransaction>() {
							});
					// LScfttransactionobj.setModuleName("Reports");
					// LScfttransactionobj.setComments("FTP connect test status: " + loginStatus);
					// LScfttransactionobj.setActions("Load");
					LScfttransactionobj.setSystemcoments("System Generated");
					LScfttransactionobj.setTableName("LSConfiguration");
					lscfttransactionRepository.save(LScfttransactionobj);
				}
			}
		} catch (MalformedURLException e) {
			logger.error("testFTPConnection(): " + e.getLocalizedMessage());
			rtnObj.put("status", "fail");
		}
		return rtnObj;
	}

	public Map<String, Object> testUrlConnection(Map<String, Object> argMap) {
		Map<String, Object> ObjMap = new HashMap<>();
		String status = "";
		try {
			String downloadUri = "";
			if(argMap.containsKey("configpath")) {
				downloadUri = (String) argMap.get("configpath");
			}else {
				downloadUri = (String) argMap.get("url");
			}
			downloadUri += "/link.txt";
			InputStream stream;
			HttpsURLConnection connectionSSL = null;
			HttpURLConnection connection = null;
			logger.info("saveDocxsReport() downloadUri: " + downloadUri);
			if (downloadUri.contains("https")) {
				SSLContext sc = SSLContext.getInstance("SSL");
				sc.init(null, trustAllCerts, new java.security.SecureRandom());
				HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
				URL url = new URL(downloadUri);
				connectionSSL = (HttpsURLConnection) url.openConnection();
				stream = connectionSSL.getInputStream();
				String fileContent = new BufferedReader(new InputStreamReader(stream)).lines()
						.collect(Collectors.joining("\n"));
				logger.info(fileContent);
				if (fileContent.equals("working")) {
					status = "pass";
				} else {
					status = "fail";
				}
			} else {
				URL url = new URL(downloadUri);
				connection = (HttpURLConnection) url.openConnection();
				stream = connection.getInputStream();
				String fileContent = new BufferedReader(new InputStreamReader(stream)).lines()
						.collect(Collectors.joining("\n"));
				logger.info(fileContent);
				if (fileContent.equals("working")) {
					status = "pass";
				} else {
					status = "fail";
				}
			}
			if (downloadUri.contains("https")) {
				connectionSSL.disconnect();
			} else {
				connection.disconnect();
			}

		} catch (Exception e) {
			logger.info(e.getLocalizedMessage());
			status = "fail";
			ObjMap.put("errorMsg", e.getLocalizedMessage() + " URL not found");
		}
		ObjMap.put("status", status);
		return ObjMap;
	}
}
