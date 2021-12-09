package com.agaram.eln.primary.controller.protocol;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonProcessingException;
import com.agaram.eln.primary.model.protocols.ProtocolorderImage;
import com.agaram.eln.primary.model.protocols.Protocolordervideos;
import com.agaram.eln.primary.model.protocols.Protocolvideos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.agaram.eln.primary.commonfunction.commonfunction;
import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.instrumentDetails.Lsprotocolordersharedby;
import com.agaram.eln.primary.model.instrumentDetails.Lsprotocolordershareto;
import com.agaram.eln.primary.model.masters.Lsrepositoriesdata;
import com.agaram.eln.primary.model.protocols.LSlogilabprotocoldetail;
import com.agaram.eln.primary.model.protocols.LSlogilabprotocolsteps;
import com.agaram.eln.primary.model.protocols.LSprotocolfiles;
import com.agaram.eln.primary.model.protocols.LSprotocolfilesContent;
import com.agaram.eln.primary.model.protocols.LSprotocolmaster;
import com.agaram.eln.primary.model.protocols.LSprotocolmastertest;
import com.agaram.eln.primary.model.protocols.LSprotocolorderfiles;
import com.agaram.eln.primary.model.protocols.LSprotocolordersampleupdates;
import com.agaram.eln.primary.model.protocols.LSprotocolorderworkflowhistory;
import com.agaram.eln.primary.model.protocols.LSprotocolsampleupdates;
import com.agaram.eln.primary.model.protocols.LSprotocolstep;
import com.agaram.eln.primary.model.protocols.LSprotocolworkflow;
import com.agaram.eln.primary.model.protocols.Lsprotocolsharedby;
import com.agaram.eln.primary.model.protocols.Lsprotocolshareto;
import com.agaram.eln.primary.model.protocols.ProtocolImage;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.service.protocol.ProtocolService;

@RestController
@RequestMapping(value = "/protocol", method = RequestMethod.POST)
public class ProtocolController {

	@Autowired
	ProtocolService ProtocolMasterService;

	@RequestMapping(value = "/getProtocolMasterInit")
	protected Map<String, Object> getProtocolMasterInit(@RequestBody Map<String, Object> argObj) {
		Map<String, Object> objMap = new HashMap<String, Object>();
		objMap = ProtocolMasterService.getProtocolMasterInit(argObj);
		return objMap;
	}

	@RequestMapping(value = "/addProtocolMaster")
	protected Map<String, Object> addProtocolMaster(@RequestBody Map<String, Object> argObj) {

		return ProtocolMasterService.addProtocolMaster(argObj);

	}

	@RequestMapping(value = "/deleteProtocolMaster")
	protected Map<String, Object> deleteProtocolMaster(@RequestBody Map<String, Object> argObj) {

		return ProtocolMasterService.deleteProtocolMaster(argObj);
	}

	@RequestMapping(value = "/getProtocolMasterLst")
	protected Map<String, Object> getProtocolMasterLst(@RequestBody Map<String, Object> argObj) {
		Map<String, Object> objMap = new HashMap<String, Object>();
		objMap = ProtocolMasterService.getLSProtocolMasterLst(argObj);
		return objMap;
	}
	
	@RequestMapping(value = "/getApprovedprotocolLst")
	protected Map<String, Object> getApprovedprotocolLst(@RequestBody LSSiteMaster site) {
		Map<String, Object> objMap = new HashMap<String, Object>();
		objMap = ProtocolMasterService.getApprovedprotocolLst(site);
		return objMap;
	}

	@RequestMapping(value = "/getProtocolStepLst")
	protected Map<String, Object> getProtocolStepLst(@RequestBody Map<String, Object> argObj) {
		Map<String, Object> objMap = new HashMap<String, Object>();
		objMap = ProtocolMasterService.getProtocolStepLst(argObj);
		return objMap;
	}
	
	@RequestMapping(value = "/getProtocolStepLstForShare")
	protected Map<String, Object> getProtocolStepLstForShare(@RequestBody Map<String, Object> argObj) {
		Map<String, Object> objMap = new HashMap<String, Object>();
		objMap = ProtocolMasterService.getProtocolStepLstForShare(argObj);
		return objMap;
	}

	@RequestMapping(value = "/getAllProtocolStepLst")
	protected Map<String, Object> getAllProtocolStepLst(@RequestBody Map<String, Object> argObj) {
		Map<String, Object> objMap = new HashMap<String, Object>();
		objMap = ProtocolMasterService.getAllProtocolStepLst(argObj);
		return objMap;
	}

	@RequestMapping(value = "/getOrdersLinkedToProtocol")
	protected Map<String, Object> getOrdersLinkedToProtocol(@RequestBody Map<String, Object> argObj) {
		Map<String, Object> objMap = new HashMap<String, Object>();
		objMap = ProtocolMasterService.getOrdersLinkedToProtocol(argObj);
		return objMap;
	}

	@RequestMapping(value = "/addProtocolStep")
	protected Map<String, Object> addProtocolStep(@RequestBody Map<String, Object> argObj) {

		return ProtocolMasterService.addProtocolStep(argObj);
	}

	@RequestMapping(value = "/deleteProtocolStep")
	protected Map<String, Object> deleteProtocolStep(@RequestBody Map<String, Object> argObj) {
		Map<String, Object> objMap = new HashMap<String, Object>();
		objMap = ProtocolMasterService.deleteProtocolStep(argObj);
		return objMap;
	}

	@RequestMapping(value = "/sharewithteam")
	protected Map<String, Object> sharewithteam(@RequestBody Map<String, Object> argObj) {

		return ProtocolMasterService.sharewithteam(argObj);
	}

	@RequestMapping(value = "/updateworkflowforProtocol")
	protected Map<String, Object> updateworkflowforProtocol(@RequestBody LSprotocolmaster objClass) {
		Map<String, Object> objMap = new HashMap<String, Object>();
		objMap = ProtocolMasterService.updateworkflowforProtocol(objClass);
		return objMap;
	}
	
	@RequestMapping(value = "/updateworkflowforProtocolorder")
	protected Map<String, Object> updateworkflowforProtocolorder(@RequestBody LSlogilabprotocoldetail objClass) {
		Map<String, Object> objMap = new HashMap<String, Object>();
		objMap = ProtocolMasterService.updateworkflowforProtocolorder(objClass);
		return objMap;
	}

	@PostMapping("/GetProtocolWorkflow")
	public List<LSprotocolworkflow> GetProtocolWorkflow(@RequestBody LSprotocolworkflow objclass) {
		return ProtocolMasterService.GetProtocolWorkflow(objclass);
	}
	


	@PostMapping("/InsertUpdatesheetWorkflow")
	public List<LSprotocolworkflow> InsertUpdatesheetWorkflow(@RequestBody LSprotocolworkflow[] protocolworkflow) {
		return ProtocolMasterService.InsertUpdatesheetWorkflow(protocolworkflow);
	}

	@PostMapping("/Deletesheetworkflow")
	public Response Deletesheetworkflow(@RequestBody LSprotocolworkflow objflow) {
		return ProtocolMasterService.Deletesheetworkflow(objflow);
	}

	@RequestMapping(value = "/getProtocolMasterList")
	protected List<LSprotocolmaster> getProtocolMasterList(@RequestBody LSuserMaster objClass) {

		return ProtocolMasterService.getProtocolMasterList(objClass);
	}

	@RequestMapping(value = "/addProtocolOrder")
	protected Map<String, Object> addProtocolOrder(@RequestBody LSlogilabprotocoldetail LSlogilabprotocoldetail) {

		return ProtocolMasterService.addProtocolOrder(LSlogilabprotocoldetail);

	}
	
	@RequestMapping(value = "/addProtocolOrderafter")
	protected Map<String, Object> addProtocolOrderafter(@RequestBody LSlogilabprotocoldetail LSlogilabprotocoldetail) {

		return ProtocolMasterService.addProtocolOrderafter(LSlogilabprotocoldetail);

	}

	@RequestMapping(value = "/getProtocolOrderList")
	protected Map<String, Object> getProtocolOrderList(@RequestBody LSlogilabprotocoldetail LSlogilabprotocoldetail) {
		return ProtocolMasterService.getProtocolOrderList(LSlogilabprotocoldetail);
	}
	
	
	@RequestMapping(value = "/getProtocolOrderListfortabchange")
	protected Map<String, Object> getProtocolOrderListfortabchange(@RequestBody LSlogilabprotocoldetail LSlogilabprotocoldetail) {
		return ProtocolMasterService.getProtocolOrderListfortabchange(LSlogilabprotocoldetail);
	}
	
	@RequestMapping(value = "/getreminProtocolOrderList")
	protected Map<String, Object> getreminProtocolOrderList(@RequestBody LSlogilabprotocoldetail LSlogilabprotocoldetail) {
		return ProtocolMasterService.getreminProtocolOrderList(LSlogilabprotocoldetail);
	}
	
	@RequestMapping(value = "/getreminProtocolOrderListontab")
	protected Map<String, Object> getreminProtocolOrderListontab(@RequestBody LSlogilabprotocoldetail LSlogilabprotocoldetail) {
		return ProtocolMasterService.getreminProtocolOrderListontab(LSlogilabprotocoldetail);
	}

	@RequestMapping(value = "/updateProtocolOrderStep")
	protected Map<String, Object> updateProtocolOrderStep(@RequestBody Map<String, Object> argObj) {
		return ProtocolMasterService.updateProtocolOrderStep(argObj);
	}

	@RequestMapping(value = "/getProtocolOrderStepLst")
	protected Map<String, Object> getProtocolOrderStepLst(@RequestBody Map<String, Object> argObj) {
		Map<String, Object> objMap = new HashMap<String, Object>();
		objMap = ProtocolMasterService.getProtocolOrderStepLst(argObj);
		return objMap;
	}

	@RequestMapping(value = "/getAllMasters")
	protected Map<String, Object> getAllMasters(@RequestBody LSuserMaster objuser) {
		Map<String, Object> objMap = new HashMap<String, Object>();
		objMap = ProtocolMasterService.getAllMasters(objuser);
		return objMap;
	}

	@RequestMapping(value = "/startStep")
	protected Map<String, Object> startStep(@RequestBody LSuserMaster objuser) {
		Map<String, Object> objMap = new HashMap<String, Object>();
		objMap = ProtocolMasterService.startStep(objuser);
		return objMap;
	}

	@RequestMapping(value = "/updateStepStatus")
	protected Map<String, Object> updateStepStatus(@RequestBody Map<String, Object> argMap) {
		Map<String, Object> objMap = new HashMap<String, Object>();
		objMap = ProtocolMasterService.updateStepStatus(argMap);
		return objMap;
	}

	@RequestMapping(value = "/updateOrderStatus")
	protected Map<String, Object> updateOrderStatus(@RequestBody LSlogilabprotocoldetail argMap) {
		Map<String, Object> objMap = new HashMap<String, Object>();
		objMap = ProtocolMasterService.updateOrderStatus(argMap);
		return objMap;
	}

	@RequestMapping(value = "/getLsrepositoriesLst")
	protected Map<String, Object> getLsrepositoriesLst(@RequestBody Map<String, Object> argMap) {
		Map<String, Object> objMap = new HashMap<String, Object>();
		objMap = ProtocolMasterService.getLsrepositoriesLst(argMap);
		return objMap;
	}

	@RequestMapping(value = "/getLsrepositoriesDataLst")
	protected Map<String, Object> getLsrepositoriesDataLst(@RequestBody Map<String, Object> argMap) {
		Map<String, Object> objMap = new HashMap<String, Object>();
		objMap = ProtocolMasterService.getLsrepositoriesDataLst(argMap);
		return objMap;
	}

	@PostMapping("/GetProtocolTransactionDetails")
	public Map<String, Object> GetProtocolTransactionDetails(@RequestBody LSprotocolmaster LSprotocolmaster) {
		Map<String, Object> objMap = new HashMap<String, Object>();
		objMap = ProtocolMasterService.GetProtocolTransactionDetails(LSprotocolmaster);
		return objMap;
	}
//	@RequestMapping(value="/addProtocolOrderStep")
//	protected Map<String, Object> addProtocolOrderStep(@RequestBody Map<String, Object> argObj){
//		Map<String, Object> objMap = new HashMap<String, Object>();
//		objMap = ProtocolMasterService.addProtocolOrderStep(argObj);
//		return objMap;
//	}

	@RequestMapping(value = "/addProtocolStepforsaveas")
	protected Map<String, Object> addProtocolStepforsaveas(@RequestBody Map<String, Object> argObj) {
		Map<String, Object> objMap = new HashMap<String, Object>();
		objMap = ProtocolMasterService.addProtocolStepforsaveas(argObj);
		return objMap;
	}

	@RequestMapping("/GetProtocolResourcesQuantitylst")
	public Map<String, Object> GetProtocolResourcesQuantitylst(@RequestBody LSprotocolstep LSprotocolstep) {
		return ProtocolMasterService.GetProtocolResourcesQuantitylst(LSprotocolstep);
	}

	@RequestMapping("/GetProtocolVersionDetails")
	public Map<String, Object> GetProtocolVersionDetails(@RequestBody Map<String, Object> argObj) {
		return ProtocolMasterService.GetProtocolVersionDetails(argObj);
	}

	@RequestMapping("/GetProtocolorderResourcesQuantitylst")
	public  Map<String, Object> GetProtocolorderResourcesQuantitylst(
			@RequestBody LSlogilabprotocolsteps LSlogilabprotocolsteps) {
		return ProtocolMasterService.GetProtocolorderResourcesQuantitylst(LSlogilabprotocolsteps);
	}

	@RequestMapping("/GetProtocolTemplateVerionLst")
	public Map<String, Object> GetProtocolTemplateVerionLst(@RequestBody Map<String, Object> argObj) {
		return ProtocolMasterService.GetProtocolTemplateVerionLst(argObj);
	}
	
	@PostMapping("/getprotocols")
	public List <LSprotocolmaster> getprotocols(@RequestBody LSuserMaster objusers)
	{
		return ProtocolMasterService.getprotocol(objusers);
	}
	
	@PostMapping("/getProtocolcount")
	public  Map<String, Object> getProtocolcount(@RequestBody LSuserMaster objusers)
	{
		return ProtocolMasterService.getProtocolcount(objusers);
	}
	
	@PostMapping("/getsingleprotocolorder")
	public LSlogilabprotocoldetail getsingleprotocolorder(@RequestBody LSlogilabprotocoldetail objusers)
	{
		return ProtocolMasterService.getsingleprotocolorder(objusers);
	}
	
	@PostMapping("/Getinitialorders")
	public Map<String, Object> Getinitialorders(@RequestBody LSlogilabprotocoldetail objorder)
	{
		return ProtocolMasterService.Getinitialorders(objorder);
	}
	
	@PostMapping("/Getremainingorders")
	public List<LSlogilabprotocoldetail> Getremainingorders(@RequestBody LSlogilabprotocoldetail objorder)
	{
		return ProtocolMasterService.Getremainingorders(objorder);
	}
	
	@PostMapping("/Getinitialtemplates")
	public Map<String, Object> Getinitialtemplates(@RequestBody LSprotocolmaster objorder)
	{
		return ProtocolMasterService.Getinitialtemplates(objorder);
	}
	
	@PostMapping("/Getremainingtemplates")
	public List<LSprotocolmaster> Getremainingtemplates(@RequestBody LSprotocolmaster objorder)
	{
		return ProtocolMasterService.Getremainingtemplates(objorder);
	}
	
	@PostMapping("/uploadprotocols")
	public Map<String, Object> uploadprotocols(@RequestParam Map<String, Object> body)
	{
		return ProtocolMasterService.uploadprotocols(body);
//		return true;
	}
	
	@PostMapping("/loadprotocolfiles")
	public List<LSprotocolfiles> loadprotocolfiles(@RequestParam Map<String, String> body)
	{
		return ProtocolMasterService.loadprotocolfiles(body);
	}
	
	@PostMapping("/uploadprotocolsfile")
	public Map<String, Object> uploadprotocolsfile(@RequestParam("file") MultipartFile file,
			@RequestParam("protocolstepcode") Integer protocolstepcode, 
			@RequestParam("protocolmastercode") Integer protocolmastercode, 
			@RequestParam("stepno") Integer stepno,
			@RequestParam("protocolstepname") String protocolstepname,
			@RequestParam("originurl") String originurl)
	{
	
		return ProtocolMasterService.uploadprotocolsfile(file, protocolstepcode,protocolmastercode,stepno,protocolstepname,originurl );
	}
	
	@PostMapping("/Uploadprotocolimage")
	public Map<String, Object> Uploadprotocolimage(@RequestParam("file") MultipartFile file,
			@RequestParam("protocolstepcode") Integer protocolstepcode, 
			@RequestParam("protocolmastercode") Integer protocolmastercode, 
			@RequestParam("stepno") Integer stepno,
			@RequestParam("protocolstepname") String protocolstepname,
			@RequestParam("originurl") String originurl)
	{
		return ProtocolMasterService.Uploadprotocolimage(file, protocolstepcode,protocolmastercode,stepno,protocolstepname,originurl );
	}
	
	@RequestMapping(value = "downloadprotocolimage/{fileid}/{tenant}/{filename}/{extension}", method = RequestMethod.GET)
	@GetMapping
	public ResponseEntity<InputStreamResource> downloadprotocolimage(@PathVariable String fileid
			, @PathVariable String tenant, @PathVariable String filename, @PathVariable String extension) throws IllegalStateException, IOException {
		
		ByteArrayInputStream bis = ProtocolMasterService.downloadprotocolimage(fileid, tenant);
		
	    HttpHeaders header = new HttpHeaders();
	    header.setContentType(MediaType.parseMediaType("image/png"));
	    header.set("Content-Disposition", "attachment; filename="+filename+"."+extension);
	    
	    return new ResponseEntity<>(new InputStreamResource(bis), header, HttpStatus.OK);
	}
	
	@RequestMapping(value = "downloadprotocolfile/{fileid}/{tenant}/{filename}/{extension}", method = RequestMethod.GET)
	@GetMapping
	public ResponseEntity<InputStreamResource> downloadprotocolfile(@PathVariable String fileid
			, @PathVariable String tenant, @PathVariable String filename, @PathVariable String extension) throws IllegalStateException, IOException {
		
		ByteArrayInputStream bis = ProtocolMasterService.downloadprotocolfile(fileid, tenant);
		
	    HttpHeaders header = new HttpHeaders();
	    String mediatype = commonfunction.getMIMEtypeonextension(extension);
	    header.setContentType(MediaType.parseMediaType(mediatype));
	    header.set("Content-Disposition", "attachment; filename="+filename+"."+extension);
	    
	    return new ResponseEntity<>(new InputStreamResource(bis), header, HttpStatus.OK);
	}
	
	@PostMapping("/removeprotocolimage")
	public boolean removeprotocolimage(@RequestBody Map<String, String> body)
	{
		return ProtocolMasterService.removeprotocolimage(body);
	}
	
	@PostMapping("/reducecunsumablefield")
	public List<Lsrepositoriesdata> reducecunsumablefield(@RequestBody Lsrepositoriesdata[] lsrepositoriesdata)
	{
		return ProtocolMasterService.reducecunsumablefield(lsrepositoriesdata);
	}
	
	@RequestMapping(value = "/protocolsampleupdates")
	protected Map<String, Object> protocolsampleupdates(@RequestBody LSprotocolsampleupdates lsprotocolsampleupdates) {

		return ProtocolMasterService.protocolsampleupdates(lsprotocolsampleupdates);
	}
	
	@RequestMapping(value = "/protocolordersampleupdates")
	protected Map<String, Object> protocolordersampleupdates(@RequestBody LSprotocolordersampleupdates lsprotocolordersampleupdates) {

		return ProtocolMasterService.protocolordersampleupdates(lsprotocolordersampleupdates);
	}
	
	@RequestMapping(value = "/getrepositoriesdata")
	protected List<Lsrepositoriesdata> getrepositoriesdata(@RequestBody Integer[] lsrepositoriesdata) {

		return ProtocolMasterService.getrepositoriesdata(lsrepositoriesdata);
	}
	
	@RequestMapping(value = "/updateprotocolsampleupdates")
	protected Map<String, Object> updateprotocolsampleupdates(@RequestBody LSprotocolsampleupdates[] lsprotocolsampleupdates) {

		return ProtocolMasterService.updateprotocolsampleupdates(lsprotocolsampleupdates);
	}
	
	@RequestMapping(value = "/updateprotocolordersampleupdates")
	protected Map<String, Object> updateprotocolordersampleupdates(@RequestBody LSprotocolordersampleupdates[] lsprotocolordersampleupdates) {

		return ProtocolMasterService.updateprotocolordersampleupdates(lsprotocolordersampleupdates);
	}
	
	@PostMapping("/uploadprotocolsordersstep")
	public Map<String, Object> uploadprotocolsordersstep(@RequestParam Map<String, Object> body)
	{
		return ProtocolMasterService.uploadprotocolsordersstep(body);
//		return true;
	}
	
	@RequestMapping(value = "downloadprotocolorderimage/{fileid}/{tenant}/{filename}/{extension}", method = RequestMethod.GET)
	@GetMapping
	public ResponseEntity<InputStreamResource> downloadprotocolorderimage(@PathVariable String fileid
			, @PathVariable String tenant, @PathVariable String filename, @PathVariable String extension) throws IllegalStateException, IOException {
		
		ByteArrayInputStream bis = ProtocolMasterService.downloadprotocolorderimage(fileid, tenant);
		
	    HttpHeaders header = new HttpHeaders();
	    header.setContentType(MediaType.parseMediaType("image/png"));
	    header.set("Content-Disposition", "attachment; filename="+filename+"."+extension);
	    
	    return new ResponseEntity<>(new InputStreamResource(bis), header, HttpStatus.OK);
	}
	
	@RequestMapping(value = "downloadprotocolorderfiles/{fileid}/{tenant}/{filename}/{extension}", method = RequestMethod.GET)
	@GetMapping
	public ResponseEntity<InputStreamResource> downloadprotocolorderfiles(@PathVariable String fileid
			, @PathVariable String tenant, @PathVariable String filename, @PathVariable String extension) throws IllegalStateException, IOException {
		
		ByteArrayInputStream bis = ProtocolMasterService.downloadprotocolorderfiles(fileid, tenant);
		
	    HttpHeaders header = new HttpHeaders();
	    header.setContentType(MediaType.parseMediaType("image/png"));
	    header.set("Content-Disposition", "attachment; filename="+filename+"."+extension);
	    
	    return new ResponseEntity<>(new InputStreamResource(bis), header, HttpStatus.OK);
	}

	@PostMapping("/Getprotocollinksignature")
	public Map<String, Object> Getprotocollinksignature(@RequestBody Map<String, String> body)
	{
		return ProtocolMasterService.Getprotocollinksignature(body);
	}
	
	@PostMapping("/Uploadprotocolorderimage")
	public Map<String, Object> Uploadprotocolorderimage(@RequestParam("file") MultipartFile file,
			@RequestParam("protocolorderstepcode") Integer protocolorderstepcode, 
			@RequestParam("protocolordercode") Long protocolordercode, 
			@RequestParam("stepno") Integer stepno,
			@RequestParam("protocolstepname") String protocolstepname,
			@RequestParam("originurl") String originurl)
	{
		return ProtocolMasterService.Uploadprotocolorderimage(file, protocolorderstepcode,protocolordercode,stepno,protocolstepname,originurl );
	}

	@PostMapping("/Uploadprotocolorderimagesql")
	public Map<String, Object> Uploadprotocolorderimagesql(@RequestParam("file") MultipartFile file,
			@RequestParam("protocolorderstepcode") Integer protocolorderstepcode, 
			@RequestParam("protocolordercode") Long protocolordercode, 
			@RequestParam("stepno") Integer stepno,
			@RequestParam("protocolstepname") String protocolstepname,
			@RequestParam("originurl") String originurl) throws IOException 
	{
		return ProtocolMasterService.Uploadprotocolorderimagesql(file, protocolorderstepcode,protocolordercode,stepno,protocolstepname,originurl );
	}
	
	@PostMapping("/uploadprotocolsorderfile")
	public Map<String, Object> uploadprotocolsorderfile(@RequestParam("file") MultipartFile file,
			@RequestParam("protocolorderstepcode") Integer protocolorderstepcode, 
			@RequestParam("protocolordercode") Long protocolordercode, 
			@RequestParam("stepno") Integer stepno,
			@RequestParam("protocolstepname") String protocolstepname,
			@RequestParam("originurl") String originurl)
	{
	
		return ProtocolMasterService.uploadprotocolsorderfile(file, protocolorderstepcode,protocolordercode,stepno,protocolstepname,originurl );
	}
	
	@PostMapping("/uploadprotocolsorderfilesql")
	public Map<String, Object> uploadprotocolsfilesql(@RequestParam("file") MultipartFile file,
			@RequestParam("protocolorderstepcode") Integer protocolorderstepcode, 
			@RequestParam("protocolordercode") Long protocolordercode, 
			@RequestParam("stepno") Integer stepno,
			@RequestParam("protocolstepname") String protocolstepname,
			@RequestParam("originurl") String originurl) throws IOException
	{
	
		return ProtocolMasterService.uploadprotocolsorderfilesql(file, protocolorderstepcode,protocolordercode,stepno,protocolstepname,originurl );
	}
	
	@PostMapping("/removeprotocoorderlimage")
	public boolean removeprotocoorderlimage(@RequestBody Map<String, String> body)
	{
		return ProtocolMasterService.removeprotocoorderlimage(body);
	}
	
	@PostMapping("/loadprotocolorderfiles")
	public List<LSprotocolorderfiles> loadprotocolorderfiles(@RequestParam Map<String, String> body)
	{
		return ProtocolMasterService.loadprotocolorderfiles(body);
	}
	@PostMapping("/Insertshareprotocolorder")
	public Lsprotocolordershareto Insertshareprotocolorder(@RequestBody Lsprotocolordershareto objprotocolordershareto) {
		return ProtocolMasterService.Insertshareprotocolorder(objprotocolordershareto);
	}

	@PostMapping("/Insertshareprotocolorderby")
	public Map<String, Object> Insertshareprotocolorderby(@RequestBody Lsprotocolordersharedby objprotocolordersharedby) {
		return ProtocolMasterService.Insertshareprotocolorderby(objprotocolordersharedby);
	}
	
	@PostMapping("/Getprotocolordersharedbyme")
	public List<Lsprotocolordersharedby> Getprotocolordersharedbyme(@RequestBody Lsprotocolordersharedby objprotocolordersharedby) {
		return ProtocolMasterService.Getprotocolordersharedbyme(objprotocolordersharedby);
	}
	
	@PostMapping("/Getprotocolordersharedtome")
	public List<Lsprotocolordershareto> Getprotocolordersharedtome(@RequestBody Lsprotocolordershareto objprotocolordershareto) {
		return ProtocolMasterService.Getprotocolordersharedtome(objprotocolordershareto);
	}
	
	@PostMapping("/Insertshareprotocol")
	public Lsprotocolshareto Insertshareprotocol(@RequestBody Lsprotocolshareto objprotocolordershareto) {
		return ProtocolMasterService.Insertshareprotocol(objprotocolordershareto);
	}
	
	@PostMapping("/Insertshareprotocolby")
	public Map<String, Object> Insertshareprotocolby(@RequestBody Lsprotocolsharedby objprotocolordersharedby) {
		return ProtocolMasterService.Insertshareprotocolby(objprotocolordersharedby);
	}

	@PostMapping("/Getprotocolsharedbyme")
	public List<Lsprotocolsharedby> Getprotocolsharedbyme(@RequestBody Lsprotocolsharedby objprotocolordersharedby) {
		return ProtocolMasterService.Getprotocolsharedbyme(objprotocolordersharedby);
	}
	
	@PostMapping("/Getprotocolsharedtome")
	public List<Lsprotocolshareto> Getprotocolsharedtome(@RequestBody Lsprotocolshareto objprotocolordershareto) {
		return ProtocolMasterService.Getprotocolsharedtome(objprotocolordershareto);
	}
	
	@PostMapping("/Unshareorderby")
	public Lsprotocolsharedby Unshareprotocolby(@RequestBody Lsprotocolshareto objordershareby) {
		return ProtocolMasterService.Unshareprotocolby(objordershareby);
	}
	
	@PostMapping("/Unshareorderto")
	public Lsprotocolshareto Unshareorderto(@RequestBody Lsprotocolshareto lsordershareto) {
		return ProtocolMasterService.Unshareorderto(lsordershareto);
	}
	
	@PostMapping("/Unshareprotocolorderby")
	public Lsprotocolordersharedby Unshareprotocolorderby(@RequestBody Lsprotocolordersharedby objprotocolordersharedby) {
		return ProtocolMasterService.Unshareprotocolorderby(objprotocolordersharedby);
	}

	@PostMapping("/Unshareprotocolorderto")
	public Lsprotocolordershareto Unshareprotocolorderto(@RequestBody Lsprotocolordershareto objprotocolordershareto) {
		return ProtocolMasterService.Unshareprotocolorderto(objprotocolordershareto);
	}
	
	@PostMapping("/countsherorders")
	public Map<String, Object> countsherorders(@RequestBody Lsprotocolordersharedby Lsprotocolordersharedby) {
		return ProtocolMasterService.countsherorders(Lsprotocolordersharedby);
	}
	
	@PostMapping("/UpdateProtocoltest")
	public LSprotocolmastertest UpdateProtocoltest(@RequestBody LSprotocolmastertest objtest)
	{
		return ProtocolMasterService.UpdateProtocoltest(objtest);
	}
	
	@RequestMapping(value = "/getProtocolOnTestcode")
	protected List<LSprotocolmaster> getProtocolOnTestcode(@RequestBody LSprotocolmastertest objClass) {

		return ProtocolMasterService.getProtocolOnTestcode(objClass);
	}
	
	@PostMapping("/Uploadprotocolimagesql")
	public Map<String, Object> Uploadprotocolimagesql(@RequestParam("file") MultipartFile file,
			@RequestParam("protocolstepcode") Integer protocolstepcode, 
			@RequestParam("protocolmastercode") Integer protocolmastercode, 
			@RequestParam("stepno") Integer stepno,
			@RequestParam("protocolstepname") String protocolstepname,
			@RequestParam("originurl") String originurl) throws IOException
	{
		return ProtocolMasterService.Uploadprotocolimagesql(file, protocolstepcode,protocolmastercode,stepno,protocolstepname,originurl );
	}
	
	@RequestMapping(value = "downloadprotocolimagesql/{fileid}/{filename}/{extension}", method = RequestMethod.GET)
	@GetMapping
	public ResponseEntity<InputStreamResource> downloadprotocolimagesql(@PathVariable String fileid
			, @PathVariable String filename, @PathVariable String extension) throws IllegalStateException, IOException {
		
		ProtocolImage objprofile =  ProtocolMasterService.downloadprotocolimagesql(fileid);
		
		byte[] data = null;
		
		if(objprofile != null)
		{
			data = objprofile.getImage().getData();
			ByteArrayInputStream bis = new ByteArrayInputStream(data);	
		    HttpHeaders header = new HttpHeaders();
		    header.setContentType(MediaType.parseMediaType("image/png"));
		    header.set("Content-Disposition", "attachment; filename="+filename+"."+extension);
		    
		    return new ResponseEntity<>(new InputStreamResource(bis), header, HttpStatus.OK);
		}else {
			
		
		return null;
		}
	
	}
	
	@PostMapping("/uploadprotocolsfilesql")
	public Map<String, Object> uploadprotocolsfilesql(@RequestParam("file") MultipartFile file,
			@RequestParam("protocolstepcode") Integer protocolstepcode, 
			@RequestParam("protocolmastercode") Integer protocolmastercode, 
			@RequestParam("stepno") Integer stepno,
			@RequestParam("protocolstepname") String protocolstepname,
			@RequestParam("originurl") String originurl) throws IOException
	{
	
		return ProtocolMasterService.uploadprotocolsfilesql(file, protocolstepcode,protocolmastercode,stepno,protocolstepname,originurl );
	}
	
	@RequestMapping(value = "downloadprotocolfilesql/{fileid}/{filename}/{extension}", method = RequestMethod.GET)
	@GetMapping
	public ResponseEntity<InputStreamResource> downloadprotocolfilesql(@PathVariable String fileid
		, @PathVariable String filename, @PathVariable String extension) throws IllegalStateException, IOException {
		
		LSprotocolfilesContent lsprotocolfilesContent = ProtocolMasterService.downloadprotocolfilesql(fileid);
		
		byte[] data = null;
		
		if(lsprotocolfilesContent != null)
		{
			data = lsprotocolfilesContent.getFile().getData();
			ByteArrayInputStream bis = new ByteArrayInputStream(data);	
		    HttpHeaders header = new HttpHeaders();
		    header.setContentType(MediaType.parseMediaType("image/png"));
		    header.set("Content-Disposition", "attachment; filename="+filename+"."+extension);
		    
		    return new ResponseEntity<>(new InputStreamResource(bis), header, HttpStatus.OK);
		}else {
			
		
		return null;
		}
	}
	
	@PostMapping("/removeprotocolimagesql")
	public boolean removeprotocolimagesql(@RequestBody Map<String, String> body)
	{
		return ProtocolMasterService.removeprotocolimagesql(body);
	}
	
	@PostMapping("/removeprotocoorderlimagesql")
	public boolean removeprotocoorderlimagesql(@RequestBody Map<String, String> body)
	{
		return ProtocolMasterService.removeprotocoorderlimagesql(body);
	}
	
	@RequestMapping(value = "downloadprotocolorderimagesql/{fileid}/{filename}/{extension}", method = RequestMethod.GET)
	@GetMapping
	public ResponseEntity<InputStreamResource> downloadprotocolorderimagesql(@PathVariable String fileid
			, @PathVariable String filename, @PathVariable String extension) throws IllegalStateException, IOException {
		
		ProtocolorderImage objprofile =  ProtocolMasterService.downloadprotocolorderimagesql(fileid);
		
		byte[] data = null;
		
		if(objprofile != null)
		{
			data = objprofile.getImage().getData();
			ByteArrayInputStream bis = new ByteArrayInputStream(data);	
		    HttpHeaders header = new HttpHeaders();
		    header.setContentType(MediaType.parseMediaType("image/png"));
		    header.set("Content-Disposition", "attachment; filename="+filename+"."+extension);
		    
		    return new ResponseEntity<>(new InputStreamResource(bis), header, HttpStatus.OK);
		}else {
			
		
		return null;
		}
	
	}
	
	@RequestMapping(value = "downloadprotocolorderfilesql/{fileid}/{filename}/{extension}", method = RequestMethod.GET)
	@GetMapping
	public ResponseEntity<InputStreamResource> downloadprotocolorderfilesql(@PathVariable String fileid
		, @PathVariable String filename, @PathVariable String extension) throws IllegalStateException, IOException {
		
		LSprotocolfilesContent lsprotocolfilesContent = ProtocolMasterService.downloadprotocolorderfilesql(fileid);
		
		byte[] data = null;
		
		if(lsprotocolfilesContent != null)
		{
			data = lsprotocolfilesContent.getFile().getData();
			ByteArrayInputStream bis = new ByteArrayInputStream(data);	
		    HttpHeaders header = new HttpHeaders();
		    header.setContentType(MediaType.parseMediaType("image/png"));
		    header.set("Content-Disposition", "attachment; filename="+filename+"."+extension);
		    
		    return new ResponseEntity<>(new InputStreamResource(bis), header, HttpStatus.OK);
		}else {
			
		
		return null;
		}
	}
	
	@RequestMapping(value = "/getProtocolOrderworkflowhistoryList")
	protected List<LSprotocolorderworkflowhistory> getProtocolOrderworkflowhistoryList(@RequestBody LSprotocolorderworkflowhistory lsprotocolorderworkflowhistory) {
		return ProtocolMasterService.getProtocolOrderworkflowhistoryList(lsprotocolorderworkflowhistory);
	}
	
	
	@PostMapping("/uploadvideo")
	public Map<String, Object> uploadvideo(@RequestParam("file") MultipartFile file,
			@RequestParam("protocolstepcode") Integer protocolstepcode, 
			@RequestParam("protocolmastercode") Integer protocolmastercode, 
			@RequestParam("stepno") Integer stepno,
			@RequestParam("protocolstepname") String protocolstepname,
			@RequestParam("originurl") String originurl)
	{
	
		return ProtocolMasterService.uploadvideo(file, protocolstepcode,protocolmastercode,stepno,protocolstepname,originurl );

	}
	
	@PostMapping("/uploadprotocolordervideo")
	public Map<String, Object> uploadprotocolordervideo(@RequestParam("file") MultipartFile file,
			@RequestParam("protocolorderstepcode") Integer protocolorderstepcode, 
			@RequestParam("protocolordercode") Long protocolordercode, 
			@RequestParam("stepno") Integer stepno,
			@RequestParam("protocolstepname") String protocolstepname,
			@RequestParam("originurl") String originurl)
	{
		return ProtocolMasterService.uploadprotocolordervideo(file, protocolorderstepcode,protocolordercode,stepno,protocolstepname,originurl );
	}
	
	
	@PostMapping("/uploadprotocolordervideosql")
	public Map<String, Object> uploadprotocolordervideosql(@RequestParam("file") MultipartFile file,
			@RequestParam("protocolorderstepcode") Integer protocolorderstepcode, 
			@RequestParam("protocolordercode") Long protocolordercode, 
			@RequestParam("stepno") Integer stepno,
			@RequestParam("protocolstepname") String protocolstepname,
			@RequestParam("originurl") String originurl) throws IOException
	{
		return ProtocolMasterService.uploadprotocolordervideosql(file, protocolorderstepcode,protocolordercode,stepno,protocolstepname,originurl );
	}
	
	
	@RequestMapping(value = "downloadprotocolordervideo/{fileid}/{tenant}/{filename}/{extension}", method = RequestMethod.GET)
	@GetMapping
	public ResponseEntity<InputStreamResource> downloadprotocolordervideo(@PathVariable String fileid
			, @PathVariable String tenant, @PathVariable String filename, @PathVariable String extension) throws IllegalStateException, IOException {
		
		ByteArrayInputStream bis = ProtocolMasterService.downloadprotocolordervideo(fileid, tenant);
		
	    HttpHeaders header = new HttpHeaders();
	    header.setContentType(MediaType.parseMediaType("image/png"));
	    header.set("Content-Disposition", "attachment; filename="+filename+"."+extension);
	    
	    return new ResponseEntity<>(new InputStreamResource(bis), header, HttpStatus.OK);
	}
	
	@RequestMapping(value = "downloadprotocolvideo/{fileid}/{tenant}/{filename}/{extension}", method = RequestMethod.GET)
	@GetMapping
	public ResponseEntity<InputStreamResource> downloadprotocolvideo(@PathVariable String fileid
			, @PathVariable String tenant, @PathVariable String filename, @PathVariable String extension) throws IllegalStateException, IOException {
		
		ByteArrayInputStream bis = ProtocolMasterService.downloadprotocolvideo(fileid, tenant);
		
	    HttpHeaders header = new HttpHeaders();
	    String mediatype = commonfunction.getMIMEtypeonextension(extension);
	    header.setContentType(MediaType.parseMediaType(mediatype));
	    header.set("Content-Disposition", "attachment; filename="+filename+"."+extension);
	    
	    return new ResponseEntity<>(new InputStreamResource(bis), header, HttpStatus.OK);
	}
	
	@PostMapping("/removeprotocolvideo")
	public boolean removeprotocolvideo(@RequestBody Map<String, String> body)
	{
		return ProtocolMasterService.removeprotocolvideo(body);
	}
	
	@PostMapping("/removeprotocolordervideo")
	public boolean removeprotocolordervideo(@RequestBody Map<String, String> body)
	{
		return ProtocolMasterService.removeprotocolordervideo(body);
	}
	@PostMapping("/uploadvideosql")
	public Map<String, Object> uploadvideosql(@RequestParam("file") MultipartFile file,
			@RequestParam("protocolstepcode") Integer protocolstepcode, 
			@RequestParam("protocolmastercode") Integer protocolmastercode, 
			@RequestParam("stepno") Integer stepno,
			@RequestParam("protocolstepname") String protocolstepname,
			@RequestParam("originurl") String originurl) throws IOException
	{
		return ProtocolMasterService.uploadvideosql(file, protocolstepcode,protocolmastercode,stepno,protocolstepname,originurl );
	}
	
	
	@RequestMapping(value = "downloadprotocolordervideosql/{fileid}/{filename}/{extension}", method = RequestMethod.GET)
	@GetMapping
	public ResponseEntity<InputStreamResource> downloadprotocolordervideosql(@PathVariable String fileid
			, @PathVariable String filename, @PathVariable String extension) throws IllegalStateException, IOException {
		
		Protocolordervideos objprofile =  ProtocolMasterService.downloadprotocolordervideosql(fileid);
		
		byte[] data = null;
		
		if(objprofile != null)
		{
			data = objprofile.getVideo().getData();
			ByteArrayInputStream bis = new ByteArrayInputStream(data);	
		    HttpHeaders header = new HttpHeaders();
		    header.setContentType(MediaType.parseMediaType("image/png"));
		    header.set("Content-Disposition", "attachment; filename="+filename+"."+extension);
		    
		    return new ResponseEntity<>(new InputStreamResource(bis), header, HttpStatus.OK);
		}else {
			
		
		return null;
		}
	
	}
	
	@RequestMapping(value = "downloadprotocolvideosql/{fileid}/{filename}/{extension}", method = RequestMethod.GET)
	@GetMapping
	public ResponseEntity<InputStreamResource> downloadprotocolvideosql(@PathVariable String fileid
			, @PathVariable String filename, @PathVariable String extension) throws IllegalStateException, IOException {
		
		Protocolvideos objprofile =  ProtocolMasterService.downloadprotocolvideosql(fileid);
		
		byte[] data = null;
		
		if(objprofile != null)
		{
			data = objprofile.getVideo().getData();
			ByteArrayInputStream bis = new ByteArrayInputStream(data);	
		    HttpHeaders header = new HttpHeaders();
		    header.setContentType(MediaType.parseMediaType("image/png"));
		    header.set("Content-Disposition", "attachment; filename="+filename+"."+extension);
		    
		    return new ResponseEntity<>(new InputStreamResource(bis), header, HttpStatus.OK);
		}else {
			
		
		return null;
		}
	
	}
	
	@PostMapping("/removeprotocolvideossql")
	public boolean removeprotocolvideossql(@RequestBody Map<String, String> body)
	{
		return ProtocolMasterService.removeprotocolvideossql(body);
	}

	@PostMapping("/removeprotocolordervideossql")
	public boolean removeprotocolordervideossql(@RequestBody Map<String, String> body)
	{
		return ProtocolMasterService.removeprotocolordervideossql(body);
	}
	
	@RequestMapping(value = "/getprojectteam")
	protected boolean getprojectteam(@RequestBody LSuserMaster objClass) {

		return ProtocolMasterService.getprojectteam(objClass);
	}
}