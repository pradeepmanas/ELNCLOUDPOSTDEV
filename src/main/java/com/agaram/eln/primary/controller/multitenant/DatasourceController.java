package com.agaram.eln.primary.controller.multitenant;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.agaram.eln.config.AESEncryption;
import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.multitenant.CustomerSubscription;
import com.agaram.eln.primary.model.multitenant.DataSourceConfig;
import com.agaram.eln.primary.model.multitenant.Invoice;
import com.agaram.eln.primary.model.usermanagement.LoggedUser;
import com.agaram.eln.primary.service.multitenant.DatasourceService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping(value = "/multitenant", method = RequestMethod.POST)
public class DatasourceController {

	@Autowired
	private DatasourceService datasourceService;

	@PostMapping("/Validatetenant")
	public DataSourceConfig Validatetenant(@RequestBody DataSourceConfig Tenantname) {
		return datasourceService.Validatetenant(Tenantname);
	}

	@PostMapping("/Registertenant")
	public DataSourceConfig Registertenant(@RequestBody DataSourceConfig Tenantname)
			throws MessagingException, IOException {

		return datasourceService.Registertenant(Tenantname);
	}

	@PostMapping("/Registertenantid")
	public DataSourceConfig Registertenantid(MultipartHttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ObjectMapper objMap = new ObjectMapper();
		Map<String, Object> argObj = objMap.readValue(request.getParameter("tenantID"),
				new TypeReference<Map<String, Object>>() {
				});

		Response objres = new Response();

		DataSourceConfig Tenantname = objMap.convertValue(argObj, new TypeReference<DataSourceConfig>() {
		});

		CustomerSubscription CustomerSubscription = objMap.convertValue(argObj.get("CustomerSubscription"),
				new TypeReference<CustomerSubscription>() {
				});
		Tenantname.setCustomerSubscription(CustomerSubscription);

		String password = "agaram";
		String tenantID = AESEncryption.decrypt(request.getHeader("password"));
		if (password.equals(tenantID)) {
			return datasourceService.Registertenantid(Tenantname);
		} else {
			boolean check = false;
			objres.setStatus(check);
			objres.setInformation("Authentication failed");
			Tenantname.setObjResponse(objres);
			return Tenantname;
		}

	}

	@PostMapping("/Registercustomersubscription")
	public CustomerSubscription Registercustomersubscription(MultipartHttpServletRequest request,
			HttpServletResponse response) throws Exception {

		ObjectMapper objMap = new ObjectMapper();
		Map<String, Object> argObj = objMap.readValue(request.getParameter("customer_subscription_id"),
				new TypeReference<Map<String, Object>>() {
				});

		Response objres = new Response();

		CustomerSubscription CustomerSubscription = objMap.convertValue(argObj,
				new TypeReference<CustomerSubscription>() {
				});

		String password = "agaram";
		String tenantID = AESEncryption.decrypt(request.getHeader("password"));
		if (password.equals(tenantID)) {
			return datasourceService.Registercustomersubscription(CustomerSubscription);
		} else {
			boolean check = false;
			objres.setStatus(check);
			objres.setInformation("Authentication failed");
			CustomerSubscription.setObjResponse(objres);
			return CustomerSubscription;
		}

//		
	}

	@PostMapping("/Registerinvoice")
	public Invoice Registerinvoice(MultipartHttpServletRequest request, HttpServletResponse response) throws Exception {

		ObjectMapper objMap = new ObjectMapper();
		Map<String, Object> argObj = objMap.readValue(request.getParameter("Registerinvoice"),
				new TypeReference<Map<String, Object>>() {
				});

		Response objres = new Response();

		Invoice Invoice = objMap.convertValue(argObj, new TypeReference<Invoice>() {
		});
		CustomerSubscription CustomerSubscription = objMap.convertValue(argObj.get("CustomerSubscription"),
				new TypeReference<CustomerSubscription>() {
				});

		Invoice.setCustomerSubscription(CustomerSubscription);

		String password = "agaram";
		String tenantID = AESEncryption.decrypt(request.getHeader("password"));
		if (password.equals(tenantID)) {
			return datasourceService.Registerinvoice(Invoice);
		} else {
			boolean check = false;
			objres.setStatus(check);
			objres.setInformation("Authentication failed");
			Invoice.setObjResponse(objres);
			return Invoice;
		}
	}

	@PostMapping("/Getalltenant")
	public List<DataSourceConfig> Getalltenant(@RequestBody DataSourceConfig Tenantname) {
		return datasourceService.Getalltenant();
	}

	@PostMapping("/Gettenantonid")
	public DataSourceConfig Gettenantonid(@RequestBody DataSourceConfig Tenant) {
		return datasourceService.Gettenantonid(Tenant);
	}

	@PostMapping("/Updatetenant")
	public DataSourceConfig Updatetenant(@RequestBody DataSourceConfig Tenant) {
		return datasourceService.Updatetenant(Tenant);
	}

	@PostMapping("/Initiatetenant")
	public DataSourceConfig Initiatetenant(@RequestBody DataSourceConfig Tenant) {
		return datasourceService.Initiatetenant(Tenant);
	}

	@PostMapping("/Updaprofiletetenant")
	public int Updaprofiletetenant(@RequestBody DataSourceConfig Tenant) {
		return datasourceService.Updaprofiletetenant(Tenant);
	}

	@PostMapping("/login")
	public Map<String, Object> login(@RequestBody LoggedUser objuser) {

		return datasourceService.login(objuser);

	}

	@PostMapping("/checktenantid")
	public Map<String, Object> checktenantid(@RequestBody DataSourceConfig DataSourceConfig) throws MessagingException {

		return datasourceService.checktenantid(DataSourceConfig);
	}

	@PostMapping("/tenantlogin")
	public DataSourceConfig tenantlogin(@RequestBody DataSourceConfig objtenant) throws MessagingException {

		return datasourceService.tenantlogin(objtenant);
	}

	@PostMapping("/sendotp")
	public DataSourceConfig sendotp(@RequestBody DataSourceConfig objtenant) throws MessagingException, IOException {

		return datasourceService.sendotp(objtenant);
	}

	@PostMapping("/otpvarification")
	public DataSourceConfig otpvarification(@RequestBody DataSourceConfig objtenant) throws MessagingException {

		return datasourceService.otpvarification(objtenant);
	}
	
	@PostMapping("/afterotpverified")
	public DataSourceConfig afterotpverified(@RequestBody DataSourceConfig objtenant) throws MessagingException {

		return datasourceService.afterotpverified(objtenant);
	}

	@PostMapping("/checkusermail")
	public Map<String, Object> checkusermail(@RequestBody DataSourceConfig DataSourceConfig) throws MessagingException {

		return datasourceService.checkusermail(DataSourceConfig);
	}

	@PostMapping("/tenantcontactno")
	public Map<String, Object> tenantcontactno(@RequestBody DataSourceConfig DataSourceConfig)
			throws MessagingException {

		return datasourceService.tenantcontactno(DataSourceConfig);
	}

	@PostMapping("/Completeregistration")
	public DataSourceConfig Completeregistration(@RequestBody DataSourceConfig DataSourceConfig) {

		return datasourceService.Completeregistration(DataSourceConfig);
	}

	@PostMapping("/updatetenantadminpassword")
	public DataSourceConfig updatetenantadminpassword(@RequestBody DataSourceConfig Tenant)
			throws MessagingException, IOException {
		return datasourceService.updatetenantadminpassword(Tenant);
	}

	@PostMapping("/ValidatetenantByID")
	public DataSourceConfig ValidatetenantByID(@RequestBody DataSourceConfig TenantID) {
		return datasourceService.ValidatetenantByID(TenantID);
	}

	@PostMapping("/ValidatetenantByName")
	public DataSourceConfig ValidatetenantByName(@RequestBody DataSourceConfig TenantID) {
		return datasourceService.ValidatetenantByName(TenantID);
	}

	@PostMapping("/Remindertenant")
	public DataSourceConfig Remindertenant(@RequestBody DataSourceConfig Tenantname)
			throws MessagingException, IOException {
		return datasourceService.Remindertenant(Tenantname);
	}
	
	@PostMapping("/updateTenantPlan")
	public DataSourceConfig updateTenantPlan(@RequestBody DataSourceConfig tenantDetails)
			throws Exception {
		return datasourceService.updateTenantPlan(tenantDetails);
	}
}
