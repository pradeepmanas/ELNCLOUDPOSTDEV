package com.agaram.eln.primary.controller.methodsetup;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.usermanagement.LSusergrouprights;
import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.methodsetup.ELNResultDetails;
import com.agaram.eln.primary.model.methodsetup.LSResultFieldValues;
import com.agaram.eln.primary.payload.Response;
import com.agaram.eln.primary.repository.cfr.LScfttransactionRepository;
import com.agaram.eln.primary.repository.usermanagement.LSSiteMasterRepository;
import com.agaram.eln.primary.service.fileuploaddownload.FileStorageService;
import com.agaram.eln.primary.service.methodsetup.EvaluateParserService;
import com.agaram.eln.primary.service.methodsetup.MethodService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;;

/**
 * This controller is used to dispatch the input request to its relevant method to access
 * the EvaluateParser Service methods.
 * @author ATE153
 * @version 1.0.0
 * @since   18- Apr- 2020
 */
@RestController
public class EvaluateParserController {
	
	@Autowired
	EvaluateParserService parserService;
	
	@Autowired
    private FileStorageService fileStorageService; 
	
	@Autowired
    private MethodService methodservice;
	
	@Autowired
    private LSSiteMasterRepository lssiteMasterRepository;

	@Autowired
	LScfttransactionRepository lscfttransactionrepo;
	

	/**
	 * This method is used to  evaluate parser  for the specified method and site.
	 * @param request [HttpServletRequest] Request object
	 * @param mapObject  [Map] object with keys 'site', 'rawdata' and 'methodKey'. 			
	 * @return response object with list of fields and values corresponding to the fields of the
	 * selected method.
	 */
	@PostMapping(value = "/evaluateParser")
	public ResponseEntity<Object> evaluateParser(final HttpServletRequest request, 
			@Valid @RequestBody Map<String, Object> mapObject)throws Exception{
		final ObjectMapper mapper = new ObjectMapper();		 
		final int methodKey = mapper.convertValue(mapObject.get("methodKey"), Integer.class);
		final LSSiteMaster site = mapper.convertValue(mapObject.get("site"), LSSiteMaster.class);
//		mapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
		final String rawData =  mapper.convertValue(mapObject.get("rawData"), String.class);
		final String tenant =  mapper.convertValue(mapObject.get("X-TenantID"), String.class);
		final Integer isMultitenant = mapper.convertValue(mapObject.get("isMultitenant"), Integer.class);
//		System.out.println("rawData:"+ rawData);
		if(isMultitenant != 0 ) {
		return parserService.evaluateParser(methodKey, site, rawData,tenant,isMultitenant,request);
		// parserService.evaluateParser(methodKey, site, rawData,tenant,isMultitenant);
		}
		else
		{
			return parserService.evaluateSQLParser(methodKey, site, rawData,isMultitenant,request);
			}
		// parserService.evaluateSQLParser(methodKey, site, rawD);
	}
	
	@PostMapping("/uploadFileandevaluateParser")
    public ResponseEntity<Object> uploadFileandevaluateParser(@RequestParam("file") MultipartFile file, @RequestParam("method") String method,
    		@RequestParam("site") String sitecode,@RequestParam("X-TenantID") String tenant,@RequestParam ("isMultitenant") Integer isMultitenant,
    		@RequestParam ("originalfilename") String originalfilename,final HttpServletRequest request)throws Exception {
		
	//	if(isMultitenant != 0) {
        String fileName = fileStorageService.storeFile(file,tenant,isMultitenant,originalfilename );
        final ObjectMapper mapper = new ObjectMapper();
        if(method.indexOf(",")>0)
        {
        	method=method.substring(0,method.indexOf(","));
        }
        if(sitecode.indexOf(",")>0)
        {
        	sitecode=sitecode.substring(0,sitecode.indexOf(","));
        }
		final int methodKey = Integer.parseInt(method);
		final LSSiteMaster site = lssiteMasterRepository.findOne(Integer.parseInt(sitecode));
		
		final String rawData =  methodservice.getFileData(fileName,tenant);
		return parserService.evaluateParser(methodKey, site, rawData,tenant,isMultitenant,request);
//		}
//		else {
//		String fileName = fileStorageService.storeSQLFile(file,tenant,isMultitenant,originalfilename);
//		 final ObjectMapper mapper = new ObjectMapper();
//	        if(method.indexOf(",")>0)
//	        {
//	        	method=method.substring(0,method.indexOf(","));
//	        }
//	        if(sitecode.indexOf(",")>0)
//	        {
//	        	sitecode=sitecode.substring(0,sitecode.indexOf(","));
//	        }
//			final int methodKey = Integer.parseInt(method);
//			final LSSiteMaster site = lssiteMasterRepository.findOne(Integer.parseInt(sitecode));
//			final String rawData =  methodservice.getSQLFileData(fileName);
//			return parserService.evaluateParser(methodKey, site, rawData,tenant,isMultitenant);
//		}
   
  
      
	}
	
	
	
	@PostMapping("/uploadELNFileandevaluateParser")
    public ResponseEntity<Object> uploadELNFileandevaluateParser(@RequestParam("file") MultipartFile file, @RequestParam("method") String method,
    		@RequestParam("site") String sitecode,@RequestParam("X-TenantID") String tenant,@RequestParam ("isMultitenant") Integer isMultitenant,
    		@RequestParam ("originalfilename") String originalfilename,final HttpServletRequest request)throws Exception {
		
		//if(isMultitenant != 0) {
//        String fileName = fileStorageService.storeFile(file,tenant,isMultitenant,originalfilename );
//        final ObjectMapper mapper = new ObjectMapper();
//        if(method.indexOf(",")>0)
//        {
//        	method=method.substring(0,method.indexOf(","));
//        }
//        if(sitecode.indexOf(",")>0)
//        {
//        	sitecode=sitecode.substring(0,sitecode.indexOf(","));
//        }
//		final int methodKey = Integer.parseInt(method);
//		final LSSiteMaster site = lssiteMasterRepository.findOne(Integer.parseInt(sitecode));
//		
//		final String rawData =  methodservice.getFileData(fileName,tenant);
//		return parserService.evaluateParser(methodKey, site, rawData,tenant,isMultitenant);
	//	}
		//else {
		String fileName = fileStorageService.storeSQLFile(file,tenant,isMultitenant,originalfilename);
		 final ObjectMapper mapper = new ObjectMapper();
	        if(method.indexOf(",")>0)
	        {
	        	method=method.substring(0,method.indexOf(","));
	        }
	        if(sitecode.indexOf(",")>0)
	        {
	        	sitecode=sitecode.substring(0,sitecode.indexOf(","));
	        }
			final int methodKey = Integer.parseInt(method);
			final LSSiteMaster site = lssiteMasterRepository.findOne(Integer.parseInt(sitecode));
			final String rawData =  methodservice.getSQLFileData(fileName);
			return parserService.evaluateParser(methodKey, site, rawData,tenant,isMultitenant,request);
		}
   
  
      
//	}
	/**
	   * This method is used to retrieve list of active methods for which parsing
	   * is done for the specified site
	   * @param mapObject  [Map] map object with site detail for which the list is to be fetched
	   * @return response object with list of active methods based on the input criteria
	   */
	@PostMapping(value = "/getLabSheetMethodList")
	public ResponseEntity<Object> getLabSheetMethodList(@Valid @RequestBody Map<String, Object> mapObject)throws Exception {	
		 final ObjectMapper mapper = new ObjectMapper();		
		 final LSSiteMaster site = mapper.convertValue(mapObject.get("site"), LSSiteMaster.class);
		 final String tenant = mapper.convertValue(mapObject.get("X-TenantID"), String.class);
		 final Integer isMultitenant = mapper.convertValue(mapObject.get("isMultitenant"), Integer.class);
		 return parserService.getLabSheetMethodList(site,tenant,isMultitenant);
	}
	
	/**
	 * This method is used to retrieve list of active fields (Instrument fields, customs fields and
	 * general fields) that are to be listed in the specified method.
	 * @param mapObject  [Map] map object with site detail, method key for which the list is to be fetched
	 * @return response object with list of active fields that are to be listed in the specified method
	 */
	@PostMapping(value = "/getMethodFieldList")
	public ResponseEntity<Object> getMethodFieldList(@Valid @RequestBody Map<String, Object> mapObject)throws Exception {	
		 final ObjectMapper mapper = new ObjectMapper();		
		 final LSSiteMaster site = mapper.convertValue(mapObject.get("site"), LSSiteMaster.class);
		 final Integer methodKey = mapper.convertValue(mapObject.get("methodKey"), Integer.class);
		 final String tenant =  mapper.convertValue(mapObject.get("X-TenantID"), String.class);
		 
		 final Integer isMultitenant =  mapper.convertValue(mapObject.get("isMultitenant"), Integer.class);
		
		 return parserService.getMethodFieldList(methodKey, site, null,tenant,isMultitenant);
	}
	
//	@PostMapping(value = "/insertELNResultDetails")
//	public ResponseEntity<Object> insertELNResultDetails(@RequestBody List<ELNResultDetails> lstResultDetails) {	
//		 final ObjectMapper mapper = new ObjectMapper();		
//		 //final ELNResultDetails elnresults = mapper.convertValue(mapObject.get("elnresultdetails"), ELNResultDetails.class);
//		 return parserService.insertELNResultDetails(lstResultDetails, null);
//	}
	@PostMapping("/insertELNResultDetails")
	public List<ELNResultDetails> insertELNResultDetails(@RequestBody ELNResultDetails[] lsresultDetails)throws Exception
	{
		return parserService.insertELNResultDetails(lsresultDetails);
	}
	//srimathi
//		@PostMapping("/insertLSResultFieldValues")
//		public List<LSResultFieldValues> insertLSResultFieldValues(@RequestBody LSResultFieldValues[] objresultDetails)
//		{
//			return parserService.insertLSResultFieldValues(objresultDetails);
//		}
//		
		
}
