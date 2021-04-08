package com.agaram.eln.primary.controller.multitenant;

import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.agaram.eln.primary.model.multitenant.DataSourceConfig;
import com.agaram.eln.primary.model.usermanagement.LoggedUser;
import com.agaram.eln.primary.service.multitenant.DatasourceService;

@RestController
@RequestMapping(value="/multitenant", method=RequestMethod.POST)
public class DatasourceController {
	
	@Autowired
	private DatasourceService datasourceService;
	
	@PostMapping("/Validatetenant")
	public DataSourceConfig Validatetenant(@RequestBody DataSourceConfig Tenantname)
	{
		return datasourceService.Validatetenant(Tenantname);
	}
	
	@PostMapping("/Registertenant")
	public DataSourceConfig Registertenant(@RequestBody DataSourceConfig Tenantname) throws MessagingException
	{
		return datasourceService.Registertenant(Tenantname);
	}

	@PostMapping("/Getalltenant")
	public List<DataSourceConfig> Getalltenant(@RequestBody DataSourceConfig Tenantname)
	{
		return datasourceService.Getalltenant();
	}
	
	@PostMapping("/Gettenantonid")
	public DataSourceConfig Gettenantonid(@RequestBody DataSourceConfig Tenant)
	{
		return datasourceService.Gettenantonid(Tenant);
	}
	
	@PostMapping("/Updatetenant")
	public DataSourceConfig Updatetenant(@RequestBody DataSourceConfig Tenant)
	{
		return datasourceService.Updatetenant(Tenant);
	}
	
	@PostMapping("/Initiatetenant")
	public DataSourceConfig Initiatetenant(@RequestBody DataSourceConfig Tenant)
	{
		return datasourceService.Initiatetenant(Tenant);
	}
	
	@PostMapping("/Updaprofiletetenant")
	public int Updaprofiletetenant(@RequestBody DataSourceConfig Tenant)
	{
		return datasourceService.Updaprofiletetenant(Tenant);
	}
	
	
	@PostMapping("/login")
	public Map<String, Object> login(@RequestBody LoggedUser objuser)
	{
		
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
	public DataSourceConfig sendotp(@RequestBody DataSourceConfig objtenant) throws MessagingException {
		
	
		return datasourceService.sendotp(objtenant);
	}
	
	@PostMapping("/otpvarification")
	public DataSourceConfig otpvarification(@RequestBody DataSourceConfig objtenant) throws MessagingException {
		
	
		return datasourceService.otpvarification(objtenant);
	}
	
	@PostMapping("/checkusermail")
	public Map<String, Object> checkusermail(@RequestBody DataSourceConfig DataSourceConfig) throws MessagingException {
	
		return datasourceService.checkusermail(DataSourceConfig);
	}
	
	@PostMapping("/tenantcontactno")
	public Map<String, Object> tenantcontactno(@RequestBody DataSourceConfig DataSourceConfig) throws MessagingException {
	
		return datasourceService.tenantcontactno(DataSourceConfig);
	}
	
	@PostMapping("/Completeregistration")
	public DataSourceConfig Completeregistration(@RequestBody DataSourceConfig DataSourceConfig) {
	
		return datasourceService.Completeregistration(DataSourceConfig);
	}
	
	@PostMapping("/updatetenantadminpassword")
	public DataSourceConfig updatetenantadminpassword(@RequestBody DataSourceConfig Tenant) throws MessagingException
	{
		return datasourceService.updatetenantadminpassword(Tenant);
	}
	
	@PostMapping("/ValidatetenantByID")
	public DataSourceConfig ValidatetenantByID(@RequestBody DataSourceConfig TenantID)
	{
		return datasourceService.ValidatetenantByID(TenantID);
	}
	
	@PostMapping("/ValidatetenantByName")
	public DataSourceConfig ValidatetenantByName(@RequestBody DataSourceConfig TenantID)
	{
		return datasourceService.ValidatetenantByName(TenantID);
	}
	
	@PostMapping("/Remindertenant")
	public DataSourceConfig Remindertenant(@RequestBody DataSourceConfig Tenantname) throws MessagingException
	{
		return datasourceService.Remindertenant(Tenantname);
	}
}
