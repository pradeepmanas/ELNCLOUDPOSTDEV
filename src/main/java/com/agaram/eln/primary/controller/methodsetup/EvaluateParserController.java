package com.agaram.eln.primary.controller.methodsetup;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.payload.Response;
import com.agaram.eln.primary.repository.usermanagement.LSSiteMasterRepository;
import com.agaram.eln.primary.service.fileuploaddownload.FileStorageService;
import com.agaram.eln.primary.service.methodsetup.EvaluateParserService;
import com.agaram.eln.primary.service.methodsetup.MethodService;
import com.fasterxml.jackson.databind.ObjectMapper;

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


	/**
	 * This method is used to  evaluate parser  for the specified method and site.
	 * @param request [HttpServletRequest] Request object
	 * @param mapObject  [Map] object with keys 'site', 'rawdata' and 'methodKey'. 			
	 * @return response object with list of fields and values corresponding to the fields of the
	 * selected method.
	 */
	@PostMapping(value = "/evaluateParser")
	public ResponseEntity<Object> evaluateParser(final HttpServletRequest request, 
			@Valid @RequestBody Map<String, Object> mapObject){
		final ObjectMapper mapper = new ObjectMapper();		 
		final int methodKey = mapper.convertValue(mapObject.get("methodKey"), Integer.class);
		final LSSiteMaster site = mapper.convertValue(mapObject.get("site"), LSSiteMaster.class);
//		mapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
		final String rawData =  mapper.convertValue(mapObject.get("rawData"), String.class);
//		System.out.println("rawData:"+ rawData);
		
		return parserService.evaluateParser(methodKey, site, rawData);
	}
	
	@PostMapping("/uploadFileandevaluateParser")
    public ResponseEntity<Object> uploadFileandevaluateParser(@RequestParam("file") MultipartFile file, @RequestParam("method") String method, @RequestParam("site") String sitecode) {
        String fileName = fileStorageService.storeFile(file);
        
        final ObjectMapper mapper = new ObjectMapper();		 
		final int methodKey = Integer.parseInt(method);
		final LSSiteMaster site = lssiteMasterRepository.findOne(Integer.parseInt(sitecode));
//		mapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
		final String rawData =  methodservice.getFileData(fileName);
        
        return parserService.evaluateParser(methodKey, site, rawData);
	}
	
	/**
	   * This method is used to retrieve list of active methods for which parsing
	   * is done for the specified site
	   * @param mapObject  [Map] map object with site detail for which the list is to be fetched
	   * @return response object with list of active methods based on the input criteria
	   */
	@PostMapping(value = "/getLabSheetMethodList")
	public ResponseEntity<Object> getLabSheetMethodList(@Valid @RequestBody Map<String, Object> mapObject) {	
		 final ObjectMapper mapper = new ObjectMapper();		
		 final LSSiteMaster site = mapper.convertValue(mapObject.get("site"), LSSiteMaster.class);
		 return parserService.getLabSheetMethodList(site);
	}
	
	/**
	 * This method is used to retrieve list of active fields (Instrument fields, customs fields and
	 * general fields) that are to be listed in the specified method.
	 * @param mapObject  [Map] map object with site detail, method key for which the list is to be fetched
	 * @return response object with list of active fields that are to be listed in the specified method
	 */
	@PostMapping(value = "/getMethodFieldList")
	public ResponseEntity<Object> getMethodFieldList(@Valid @RequestBody Map<String, Object> mapObject) {	
		 final ObjectMapper mapper = new ObjectMapper();		
		 final LSSiteMaster site = mapper.convertValue(mapObject.get("site"), LSSiteMaster.class);
		 final Integer methodKey = mapper.convertValue(mapObject.get("methodKey"), Integer.class);
		 return parserService.getMethodFieldList(methodKey, site, null);
	}
}
