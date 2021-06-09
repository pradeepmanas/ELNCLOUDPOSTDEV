package com.agaram.eln.primary.controller.report;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.general.OrderCreation;
import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.instrumentDetails.LSlogilablimsorderdetail;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.model.usermanagement.LoggedUser;
import com.agaram.eln.primary.service.cfr.AuditService;
import com.agaram.eln.primary.service.report.ReportsService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping(value = "/reports", method = RequestMethod.POST)
public class ReportsController {

	@Autowired
	private ReportsService ObjReportsService;

	@Autowired
	private AuditService auditService;
	
	static final Logger logger = Logger.getLogger(ReportsService.class.getName());

	@RequestMapping(value = "/createNewReportDocx")
	protected Map<String, Object> createNewDocxs(@RequestBody Map<String, Object> argObj) throws ServletException, IOException {
		Map<String, Object> mapObj = new HashMap<String, Object>();
		try {
			LoggedUser objuser = new LoggedUser();
			ObjectMapper mapper = new ObjectMapper();
			Response objResponse = new Response();
			LScfttransaction objsilentaudit = new LScfttransaction();
			LScfttransaction objmanualaudit = new LScfttransaction();
			if(argObj.containsKey("objuser"))
			{
				objuser = mapper.convertValue(argObj.get("objuser"),LoggedUser.class);
			}
			
			if(argObj.containsKey("objsilentaudit"))
			{
				objsilentaudit = mapper.convertValue(argObj.get("objsilentaudit"),LScfttransaction.class);
			}
			if(argObj.containsKey("objmanualaudit"))
			{
				objmanualaudit = mapper.convertValue(argObj.get("objmanualaudit"),LScfttransaction.class);
			}
			
			if(objuser.getsUsername() != null) {
				
				LSuserMaster userClass = auditService.CheckUserPassWord(objuser);
				
				if(userClass.getObjResponse().getStatus()) {
					mapObj = ObjReportsService.createNewReportDocx(argObj);
				}
				else
				{
					objsilentaudit.setComments("Entered invalid username and password");
					Map<String, Object> map=new HashMap<>();
					map.put("objsilentaudit",objsilentaudit);
					map.put("objmanualaudit",objmanualaudit);
					map.put("objUser",objuser);
					auditService.AuditConfigurationrecord(map);
					objResponse.setStatus(false);
					objResponse.setInformation("ID_VALIDATION");
					mapObj.put("objResponse", objResponse);
				}
				
			}else {
			mapObj = ObjReportsService.createNewReportDocx(argObj);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			mapObj.put("error", e.getMessage());
		}
		return mapObj;
	}

	@RequestMapping(value = "/saveDocxsReport", method = RequestMethod.GET)
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			ObjReportsService.saveDocxsReport(request, response);
			logger.info("/saveDocxsReport -> success");
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	@RequestMapping(value = "/cloudsaveDocxsReport", method = RequestMethod.GET)
	protected void clouddoPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			ObjReportsService.cloudsaveDocxsReport(request, response);
			logger.info("/saveDocxsReport -> success");
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/uploadDocxFile")
	protected Map<String, Object> uploadDocxFile(MultipartHttpServletRequest request, HttpServletResponse response) throws Exception {
//	protected Map<String, Object> uploadDocxFile(MultipartHttpServletRequest request,@RequestBody Map<String, Object> argObj) throws Exception {
		Map<String, Object> map = new HashMap<>();
		ObjectMapper objMap = new ObjectMapper();
		Map<String, Object> argObj = objMap.readValue(request.getParameter("serviceObj"), new TypeReference<Map<String, Object>>() {}) ;
		System.out.println(request.getParameter("serviceObj"));
		map = ObjReportsService.uploadDocxFile(request, argObj);
		logger.info("/uploadDocxFile -> success");
		return map;
	}

	@RequestMapping(value = "/updateReportDocxName")
	protected Map<String, Object> updateReportDocxName(@RequestBody Map<String, Object> obj)
			throws ServletException, IOException {
		Map<String, Object> mapObj = new HashMap<String, Object>();
		try {
			LoggedUser objuser = new LoggedUser();
			ObjectMapper mapper = new ObjectMapper();
			Response objResponse = new Response();
			LScfttransaction objsilentaudit = new LScfttransaction();
			LScfttransaction objmanualaudit = new LScfttransaction();
			
			if(obj.containsKey("objsilentaudit"))
			{
				objsilentaudit = mapper.convertValue(obj.get("objsilentaudit"),LScfttransaction.class);
			}
			if(obj.containsKey("objmanualaudit"))
			{
				objmanualaudit = mapper.convertValue(obj.get("objmanualaudit"),LScfttransaction.class);
			}
			if(obj.containsKey("objuser"))
			{
				objuser = mapper.convertValue(obj.get("objuser"),LoggedUser.class);
			}
			if(objuser.getsUsername() != null) {
				
				LSuserMaster userClass = auditService.CheckUserPassWord(objuser);
				
				if(userClass.getObjResponse().getStatus()) {
					mapObj = ObjReportsService.updateReportDocxName(obj);
				}
				else
				{
					objsilentaudit.setComments("Entered invalid username and password");
					Map<String, Object> map=new HashMap<>();
					map.put("objsilentaudit",objsilentaudit);
					map.put("objmanualaudit",objmanualaudit);
					map.put("objUser",objuser);
					auditService.AuditConfigurationrecord(map);
					objResponse.setStatus(false);
					objResponse.setInformation("ID_VALIDATION");
					mapObj.put("objResponse", objResponse);
				}
				
			}else {
			mapObj = ObjReportsService.updateReportDocxName(obj);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			mapObj.put("error", e.getMessage());
		}
		return mapObj;
	}

	@RequestMapping(value = "/getLSdocreportsLst")
	protected Map<String, Object> getLSdocreportsLst(@RequestBody Map<String, Object> argObj) throws IOException {
		Map<String, Object> mapObj = new HashMap<String, Object>();
		mapObj = ObjReportsService.getLSdocreportsLst("all");
		ObjReportsService.insertSilentAudit(argObj);
		return mapObj;
	}

	@RequestMapping(value = "/getDocxDirectoryLst")
	protected Map<String, Object> getDocxDirectoryLst(@RequestBody Map<String, Object> argObj) throws IOException {
		Map<String, Object> mapObj = new HashMap<String, Object>();
		mapObj = ObjReportsService.getDocxDirectoryLst();
//		ObjReportsService.insertSilentAudit(argObj);
		return mapObj;
	}

	@RequestMapping(value = "/getDownloadReportsInitRequest")
	protected Map<String, Object> getDownloadReportsInitRequest(@RequestBody Map<String, Object> argMap) throws IOException {
		Map<String, Object> mapObj = new HashMap<String, Object>();
		mapObj.putAll(ObjReportsService.getDownloadReportsInitRequest(argMap));
		return mapObj;
	}
	
	@RequestMapping(value = "/getDownloadReportsTemplateInitRequest")
	protected Map<String, Object> getDownloadReportsTemplateInitRequest() throws IOException {
		Map<String, Object> mapObj = new HashMap<String, Object>();
		mapObj.put("DocxDirectoryLst", ObjReportsService.getDocxDirectoryLst());
		mapObj.put("DocxReportLst", ObjReportsService.getLSdocreportsLst("isTemplate"));
		return mapObj;
	}

	@RequestMapping(value = "/addDocxDirectory")
	protected Map<String, Object> addDocxDirectory(@RequestBody Map<String, Object> Obj) throws IOException {
		Map<String, Object> mapObj = new HashMap<String, Object>();
		mapObj = ObjReportsService.addDocxDirectory(Obj);
		return mapObj;
	}
	
	@RequestMapping(value = "/updateDocxDirectory")
	protected Map<String, Object> updateDocxDirectory(@RequestBody Map<String, Object> Obj) throws IOException {
		Map<String, Object> mapObj = new HashMap<String, Object>();
		mapObj = ObjReportsService.updateDocxDirectory(Obj);
		return mapObj;
	}

	@RequestMapping(value = "/renameDeleteDocxDirectory")
	protected Map<String, Object> renameDeleteDocxDirectory(@RequestBody Map<String, Object> Obj) throws IOException {
		Map<String, Object> mapObj = new HashMap<String, Object>();
		mapObj = ObjReportsService.renameDeleteDocxDirectory(Obj);
		return mapObj;
	}
	
	@RequestMapping(value = "/getReportDocxInfo")
	protected Map<String, Object> getReportDocxInfo(@RequestBody Map<String, Object> Obj) throws IOException {
		Map<String, Object> mapObj = new HashMap<String, Object>();
		mapObj = ObjReportsService.getReportDocxInfo(Obj);
		return mapObj;
	}
	
	@RequestMapping(value = "/getCloudReportDocxInfo")
	protected Map<String, Object> getCloudReportDocxInfo(@RequestBody Map<String, Object> Obj) throws IOException {
		Map<String, Object> mapObj = new HashMap<String, Object>();
		mapObj = ObjReportsService.getCloudReportDocxInfo(Obj);
		return mapObj;
	}

//	@RequestMapping(value = "/updateDocxReportOrder")
//	public Map<String, Object> updateDocxReportOrder(@RequestBody Map<String, Object> obj) {
//		Map<String, Object> ObjMap = new HashMap<String, Object>();
//		ObjMap = ObjReportsService.updateDocxReportOrder(obj);
//		return ObjMap;
//	}

//	@RequestMapping(value = "/handleOrderandTemplate")
//	public Map<String, Object> handleOrderandTemplate(@RequestBody Map<String, Object> obj) {
//		Map<String, Object> ObjMap = new HashMap<String, Object>();
//		ObjMap = ObjReportsService.handleOrderandTemplate(obj);
//		return ObjMap;
//	}
	
	@RequestMapping(value = "/test")
	protected String simple() throws IOException {
		return "working";
	}

	@RequestMapping(value = "/testloadftp")
	protected String testloadftp() throws IOException {
		Map<String, Object> ObjMap = ObjReportsService.handleFTP("FTP Docx test", "load", "reports");
		if ((boolean)ObjMap.get("status")) {
			return "working";
		} else {
			return "not working";
		}
	}
	
	@RequestMapping(value = "/testsaveftp")
	protected String testsaveftp() throws IOException {
		Map<String, Object> ObjMap = ObjReportsService.handleFTP("test.docx", "save", "reports");
		if ((boolean)ObjMap.get("status")) {
			return "working";
		} else {
			return "not working";
		}
	}

	@PostMapping("/Getorderbytype")
	//public Map<String, Object> Getorderbytype(@RequestBody LSlogilablimsorderdetail objorder)
	public Map<String, Object> Getorderbytype(@RequestBody Map<String, Object> objorder)
	{
		Map<String, Object> mapOrders = ObjReportsService.Getorderbytype(objorder);
		return mapOrders;
	}
	
	@RequestMapping(value = "/getFilecontentforSheet")
	protected List<OrderCreation> getFilecontentforSheet(@RequestBody Map<String, Object> selectedObj) throws IOException {
		List<LSlogilablimsorderdetail> LSlogilablimsorderdetailObj = new ObjectMapper().convertValue(selectedObj.get("getFileContent"), new TypeReference<List<LSlogilablimsorderdetail>>() {});
		List<OrderCreation> ObjMap = ObjReportsService.getFilecontentforSheet(LSlogilablimsorderdetailObj);
		return ObjMap;
	}
	
	@RequestMapping(value = "/getSheetLSfileLst")
	protected Map<String, Object> getSheetLSfileLst(@RequestBody Map<String , Object> argMap) throws IOException {
		Map<String, Object> ObjMap = ObjReportsService.getSheetLSfileLst(argMap);
		return ObjMap;
	}
	
	@RequestMapping(value = "/getSheetLSfileWithFileCode")
	protected Map<String, Object> getSheetLSfileWithFileCode(@RequestBody Map<String , Object> argMap) throws IOException {
		Map<String, Object> ObjMap = ObjReportsService.getSheetLSfileUsingFilecode(argMap);
		return ObjMap;
	}
	
	@RequestMapping(value = "/getTemplatesLst")
	protected Map<String, Object> getTemplatesLst(@RequestBody Map<String , Object> argMap) throws IOException {
		Map<String, Object> ObjMap = ObjReportsService.getTemplatesLst(argMap);
		return ObjMap;
	}
	
	@RequestMapping(value = "/handleOrderTemplate")
	public Map<String, Object> handleOrderTemplate(@RequestBody Map<String, Object> obj) {
		Map<String, Object> ObjMap = new HashMap<String, Object>();
		try {
			Thread.sleep(2000);
			LoggedUser objuser = new LoggedUser();
			ObjectMapper mapper = new ObjectMapper();
			Response objResponse = new Response();
			
			LScfttransaction objsilentaudit = new LScfttransaction();
			LScfttransaction objmanualaudit = new LScfttransaction();
			if(obj.containsKey("objsilentaudit"))
			{
				objsilentaudit = mapper.convertValue(obj.get("objsilentaudit"),LScfttransaction.class);
			}
			if(obj.containsKey("objmanualaudit"))
			{
				objmanualaudit = mapper.convertValue(obj.get("objmanualaudit"),LScfttransaction.class);
			}
			
			if(obj.containsKey("objuser"))
			{
				objuser = mapper.convertValue(obj.get("objuser"),LoggedUser.class);
			}
			if(objuser.getsUsername() != null) {
				
				LSuserMaster userClass = auditService.CheckUserPassWord(objuser);
				
				if(userClass.getObjResponse().getStatus()) {
					ObjMap = ObjReportsService.handleOrderTemplate(obj);
				}
				else
				{
					objsilentaudit.setComments("Entered invalid username and password");
					Map<String, Object> map=new HashMap<>();
					map.put("objsilentaudit",objsilentaudit);
					map.put("objmanualaudit",objmanualaudit);
					map.put("objUser",objuser);
					auditService.AuditConfigurationrecord(map);
					objResponse.setStatus(false);
					objResponse.setInformation("ID_VALIDATION");
					ObjMap.put("objResponse", objResponse);
				}
				
			}else {
			ObjMap = ObjReportsService.handleOrderTemplate(obj);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ObjMap;
	}
	
	@RequestMapping(value = "/cloudHandleOrderTemplate")
	public Map<String, Object> cloudHandleOrderTemplate(@RequestBody Map<String, Object> obj) {
		Map<String, Object> ObjMap = new HashMap<String, Object>();
		try {
			Thread.sleep(2000);
			LoggedUser objuser = new LoggedUser();
			ObjectMapper mapper = new ObjectMapper();
			Response objResponse = new Response();
			
			LScfttransaction objsilentaudit = new LScfttransaction();
			LScfttransaction objmanualaudit = new LScfttransaction();
			if(obj.containsKey("objsilentaudit"))
			{
				objsilentaudit = mapper.convertValue(obj.get("objsilentaudit"),LScfttransaction.class);
			}
			if(obj.containsKey("objmanualaudit"))
			{
				objmanualaudit = mapper.convertValue(obj.get("objmanualaudit"),LScfttransaction.class);
			}
			
			if(obj.containsKey("objuser"))
			{
				objuser = mapper.convertValue(obj.get("objuser"),LoggedUser.class);
			}
			if(objuser.getsUsername() != null) {
				
				LSuserMaster userClass = auditService.CheckUserPassWord(objuser);
				
				if(userClass.getObjResponse().getStatus()) {
					ObjMap = ObjReportsService.cloudHandleOrderTemplate(obj);
				}
				else
				{
					objsilentaudit.setComments("Entered invalid username and password");
					Map<String, Object> map=new HashMap<>();
					map.put("objsilentaudit",objsilentaudit);
					map.put("objmanualaudit",objmanualaudit);
					map.put("objUser",objuser);
					auditService.AuditConfigurationrecord(map);
					objResponse.setStatus(false);
					objResponse.setInformation("ID_VALIDATION");
					ObjMap.put("objResponse", objResponse);
				}
				
			}else {
			ObjMap = ObjReportsService.cloudHandleOrderTemplate(obj);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ObjMap;
	}
	
	@RequestMapping(value = "/getReportDocxonVersion")
	public Map<String, Object> getReportDocxonVersion(@RequestBody Map<String, Object> obj) {
		Map<String, Object> ObjMap = new HashMap<String, Object>();
		ObjMap = ObjReportsService.getReportDocxonVersion(obj);
		return ObjMap;
	}
	
	@RequestMapping(value = "/createFIle")
	public void createFIle() {
		Map<String, Object> ObjMap = new HashMap<String, Object>();
		ObjReportsService.createFIle();
	}
	
//	@RequestMapping(value = "/getversion")
//	protected Map<String, Object> getversion() throws IOException {
//		return ObjReportsService.getversion();
//	}
}
