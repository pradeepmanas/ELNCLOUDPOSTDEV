package com.agaram.eln.primary.controller.methodsetup;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.agaram.eln.primary.model.methodsetup.Method;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.repository.methodsetup.MethodRepository;
import com.agaram.eln.primary.repository.methodsetup.MethodVersionRepository;
import com.agaram.eln.primary.service.methodsetup.MethodService;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This controller is used to dispatch the input request to its relevant method
 * to access the Method Service methods.
 * 
 * @author ATE153
 * @version 1.0.0
 * @since 07- Feb- 2020
 */
@RestController
public class MethodController {

	@Autowired
	MethodService methodService;

	@Autowired
	MethodVersionRepository methodversionrepository;
	
	@Autowired
	MethodRepository methodrepo;

	/**
	 * This method is used to retrieve list of active methods for the specified
	 * Site. Eg: Input Format { "site": { "sitekey": 1, "sitename": "Chennai",
	 * "status": 1 }}
	 * 
	 * @param mapObject [Map] Object with 'Site' object as a key
	 * @return list of active methods for the specified Site.
	 */
	@PostMapping(value = "/getMethod")
	public ResponseEntity<Object> getActiveMethodBySite(@RequestBody Map<String, Object> mapObject)
			throws Exception {

		final ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> obj = (Map<String, Object>) mapObject.get("inputData");
		if (obj == null) {
		final LSSiteMaster site = mapper.convertValue(mapObject.get("sitecode"), LSSiteMaster.class);
		return methodService.getActiveMethodBySite(site);
		}else {
			final Integer site = (Integer) obj.get("sitecode");
			LSSiteMaster siteobj = new LSSiteMaster();
			siteobj.setSitecode(site);
			return methodService.getActiveMethodBySite(siteobj);
		}
	}
	
	@PostMapping(value = "/findByStatus")
	public ResponseEntity<Object> findByStatus(@RequestBody Map<String, Object> mapObject)
			throws Exception {
	  final ObjectMapper mapper = new ObjectMapper();
	  Map<String, Object> obj = (Map<String, Object>) mapObject.get("site");

		if (obj == null) {
	        final Integer status = mapper.convertValue(mapObject.get("status"), Integer.class);
		    return methodService.findByStatus(status);
		}else{
		 	final Integer status = (Integer) obj.get("status");
			return methodService.findByStatus(status);
		}
	}

	@SuppressWarnings("unchecked")
	@PostMapping(value = "/getmethodversion")
	public ResponseEntity<Object> getmethodversion(@Valid @RequestBody Map<String, Object> mapObject) throws Exception {
//		final ObjectMapper mapper = new ObjectMapper();
		// final LSSiteMaster site = mapper.convertValue(mapObject.get("site"),
		// LSSiteMaster.class);
		Map<String, Object> obj = (Map<String, Object>) mapObject.get("inputData");

		final int methodKey = (Integer) obj.get("methodKey");
		// final MethodVersion methodkey =
		// mapper.convertValue(mapObject.get("methodKey"), MethodVersion.class);

		return methodService.getmethodversion(methodKey);
	}

	/**
	 * This method is used to add new Method entity. The method name can be a
	 * duplicate name of any other method. Any active instrument of the site can be
	 * associated to the method. The input raw data file can be a pdf/txt/csv file
	 * 
	 * @param request   [HttpServletRequest] Request object
	 * @param mapObject [Map] object with keys 'method'- [Method] Entity with
	 *                  'methodname'(m), 'instrawdataurl'(m),
	 *                  createdby(m),'saveAuditTrail' and 'modulePage'.
	 * @return response entity of Newly added Method entity
	 */
	@PostMapping(value = "/createMethod")
	public ResponseEntity<Object> createMethod(final HttpServletRequest request,
			@Valid @RequestBody Map<String, Object> mapObject) throws Exception {
		final ObjectMapper mapper = new ObjectMapper();
		final Method method = mapper.convertValue(mapObject.get("methodmaster"), Method.class);
		// final Boolean saveAuditTrail =
		// mapper.convertValue(mapObject.get("saveAuditTrail"), Boolean.class);
		final LSSiteMaster site = mapper.convertValue(mapObject.get("site"), LSSiteMaster.class);
		final Method auditdetails = mapper.convertValue(mapObject.get("auditdetails"), Method.class);

		return methodService.createMethod(method, site, request, auditdetails);
	}

	/**
	 * This method is used to update selected Method master's detail for the
	 * specified site. The method name can be updated any time. The associated
	 * instrument can be changed only with any other instrument from the same
	 * instrument category. The raw data file can be updated only before sample
	 * splitting /parsing is done for the method.
	 * 
	 * @param request   [HttpServletRequest] Request object
	 * @param mapObject [Map] object to update
	 * @return response entity of updated Method entity
	 */
	@PostMapping(value = "/updateMethod")
	public ResponseEntity<Object> updateMethod(final HttpServletRequest request,
			@Valid @RequestBody Map<String, Object> mapObject) throws Exception {
		final ObjectMapper mapper = new ObjectMapper();
		final Method method = mapper.convertValue(mapObject.get("methodmaster"), Method.class);
//		final Boolean saveAuditTrail = mapper.convertValue(mapObject.get("saveAuditTrail"), Boolean.class);
		final LSSiteMaster site = mapper.convertValue(mapObject.get("site"), LSSiteMaster.class);
		String someValue = (String) mapObject.get("doneByUserKey");
		final int doneByUserKey = Integer.parseInt(someValue);
		// final String comments = mapper.convertValue(mapObject.get("comments"),
		// String.class);
		final Method auditdetails = mapper.convertValue(mapObject.get("auditdetails"), Method.class);

		return methodService.updateMethod(method, site, doneByUserKey, request, auditdetails);
	}

	/**
	 * This method is used to delete the Site's method details.
	 * 
	 * @param request   [HttpServletRequest] Request object
	 * @param mapObject [Map] Object with the selected methodkey -to delete,
	 *                  saveAuditTrail, modulePage, comments and doneByUserKey.
	 * @return Response Entity relevant to delete Method entity
	 */
	@PostMapping(value = "/updateMethodStatus")
	public ResponseEntity<Object> deleteMethod(final HttpServletRequest request,
			@Valid @RequestBody Map<String, Object> mapObject) throws Exception {
		final ObjectMapper mapper = new ObjectMapper();

		final int methodKey = mapper.convertValue(mapObject.get("methodkey"), Integer.class);
//		final Boolean saveAuditTrail = mapper.convertValue(mapObject.get("saveAuditTrail"), Boolean.class);
		final LSSiteMaster site = mapper.convertValue(mapObject.get("lssitemaster"), LSSiteMaster.class);
		String someValue = (String) mapObject.get("doneByUserKey");
		final int doneByUserKey = Integer.parseInt(someValue);
		final String comments = mapper.convertValue(mapObject.get("comments"), String.class);
		final Method otherdetails = mapper.convertValue(mapObject.get("otherdetails"), Method.class);
		// final Method methodmaster =
		// mapper.convertValue(mapObject.get("methodmaster"), Method.class);
		final Method auditdetails = mapper.convertValue(mapObject.get("auditdetails"), Method.class);

		return methodService.deleteMethod(methodKey, site, comments, doneByUserKey, request, otherdetails,
				auditdetails);

//		  return methodService.deleteMethod(methodKey, site, comments, doneByUserKey,
//				  saveAuditTrail, request);
	}

	/**
	 * This method is used to fetch list of active instruments that are not yet
	 * assocaited with methods.
	 * 
	 * @param siteObj [Map] map object with site detail for which the list is to be
	 *                fetched
	 * @return response entity with list of active instruments in the site
	 */
	@PostMapping(value = "/getInstListToAssociateMethod")
	public ResponseEntity<Object> getInstListToAssociateMethod(@Valid @RequestBody Map<String, LSSiteMaster> siteObj)
			throws Exception {
		return methodService.getInstListToAssociateMethod(siteObj.get("site"));
	}

	/**
	 * This method is used to extract content of input files as txt file content.
	 * 
	 * @param mapObject [Map] object with "rawDataFileName" as key holding file name
	 *                  of pdf/txt/csv files
	 * @return byte array of txt file
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = "/getFileData")
	public String getFileData(@Valid @RequestBody Map<String, Object> mapObject) throws Exception {

		Map<String, Object> objinput = (Map<String, Object>) mapObject.get("inputData");
		final String fileName = (String) objinput.get("rawDataFileName");
		String tenant = (String) objinput.get("X-TenantID");
		Integer isMultitenant = (Integer) objinput.get("isMultitenant");

		List<Method> methodobj = methodrepo.findByInstrawdataurl(fileName);
		Integer methodkey = methodobj.get(0).getMethodkey();
		
		if (isMultitenant != 0) {
			return methodService.getFileData(fileName, tenant,methodkey);
		} else {
			return methodService.getSQLFileData(fileName,methodkey);
		}

		// return new ResponseEntity<>("Sample Split Done - "+ objinput, HttpStatus.OK);
	}

	/**
	 * This method is used to copy the selected 'Method', its sampling techniques,
	 * parsing techniques and custom fields to a new instrument that has not yet
	 * associated with that Method before. This copying is to be done only if the
	 * selected Method has either sample splitting techniques or parsing techniques
	 * 
	 * @param request   [HttpServletRequest] Request object
	 * @param mapObject [Map] object with selected Method to be loaded and the
	 *                  instrument key for which the Method is to be loaded.
	 * @return response object with copied Method entity.
	 */
	@PostMapping(value = "/createCopyMethod")
	public ResponseEntity<Object> createCopyMethod(final HttpServletRequest request,
			@Valid @RequestBody Map<String, Object> mapObject) throws Exception {

		String strUserKey = (String) mapObject.get("doneByUserKey");

		final int doneByUserKey = Integer.parseInt(strUserKey);

		return methodService.createCopyMethod(request, mapObject, doneByUserKey);
	}

	/**
	 * This method is used to validate whether the instrument is already associated
	 * with the specified Method.
	 * 
	 * @param mapObject [Map] object with Method and instrument key which is to be
	 *                  validated
	 * @return boolean value of validated response.
	 */
//	@PostMapping(value = "/getMethodByInstrument")
//	public ResponseEntity<Object> getMethodByInstrument(@Valid @RequestBody Map<String, Object> mapObject)
//			throws Exception {
//		final ObjectMapper mapper = new ObjectMapper();
//		final Method method = mapper.convertValue(mapObject.get("method"), Method.class);
//		final int instMasterKey = (Integer) mapObject.get("instMasterKey");
//
////		return methodService.getMethodByInstrument(method, instMasterKey);
//		return methodService.getMethodContainingByInstrument(method, instMasterKey);
//	}

	@PostMapping(value = "/getMethodByEquipment")
	public ResponseEntity<Object> getMethodByEquipment(@Valid @RequestBody Map<String, Object> mapObject)
			throws Exception {
		final ObjectMapper mapper = new ObjectMapper();
		final Method method = mapper.convertValue(mapObject.get("method"), Method.class);
		final int nequipmentcode = (Integer) mapObject.get("nequipmentcode");

		return methodService.getMethodContainingByEquipment(method, nequipmentcode);
	}
}
