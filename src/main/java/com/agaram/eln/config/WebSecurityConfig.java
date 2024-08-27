package com.agaram.eln.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

//import antlr.collections.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter implements ApplicationContextAware {


	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

	@Autowired
	private UserDetailsService jwtUserDetailsService;

	@Autowired
	private JwtRequestFilter jwtRequestFilter;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		// configure AuthenticationManager so that it knows from where to load
		// user for matching credentials
		// Use BCryptPasswordEncoder
		auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedOrigins("*")
		.allowedMethods("HEAD", "GET", "PUT", "POST",
		"DELETE", "PATCH").allowedHeaders("*");
	}
	
	
	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
	    // We don't need CSRF for this example
	    httpSecurity
	        .cors().and()
	        .csrf().disable()
	        .headers().frameOptions().deny().and()
	        // don't authenticate these particular requests
	        .authorizeRequests()
	        .antMatchers("/authenticate", "/multitenant/otpvarification", "/multitenant/afterotpverified",
	                "/registerMail", "/multitenant/sendotp", "/Login/LoadSite", "/Login/LoadDomain",
	                "/Login/azureusertokengenrate", "/Login/CheckUserAndPassword", "/Login/updateActiveUserTime",
	                "/Login/createuserforazure", "/Login/UpdatePassword", "/Login/Validateuser", "/Login/LinkLogin",
	                "/User/InsertUpdateUserGroup", "/User/InsertUpdateUser", "/User/getActiveUserCount", "/User/GetUsers",
	                "/AuditTrail/AuditConfigurationrecord", "/User/GetPasswordPolicySitewise", "/User/Createcentraliseduser",
	                "/User/Getallcentraliseduser", "/User/Getcentraliseduserbyid", "/Instrument/GetInstrumentParameters",
	                "/Instrument/Insertshareorder", "/Instrument/GetsharedtomeorderStatus",
	                "/Instrument/GetResultsharedfileverContent", "/Instrument/SaveSharedResultfile",
	                "/Instrument/SharedCloudUploadattachments", "/Instrument/SharedUploadattachments",
	                "/Instrument/SharedClouddeleteattachments", "/Instrument/shareddeleteattachments",
	                "/Instrument/SharedClouddownloadattachments", "/Instrument/Shareddownloadattachments",
	                "/Instrument/Sharedattachment", "/Instrument/Getorderforlink", "/Instrument/getmetatag",
	                "/Instrument/downloadNonCloud/{param}/{tenant}/{fileid}",
	                "/Instrument/downloadparserNonCloud/{param}/{tenant}/{fileid}",
	                "/Instrument/download/{param}/{fileid}", "/Instrument/downloadparser/{param}/{fileid}",
	                "/Instrument/Sharedcloudattachment", "/multitenant/Registertenant", "/multitenant/Registertenantid",
	                "/multitenant/Registercustomersubscription", "/multitenant/Validatetenant",
	                "/multitenant/checktenantid", "/multitenant/tenantlogin", "/multitenant/Getalltenant",
	                "/multitenant/Gettenantonid", "/multitenant/Updatetenant", "/multitenant/Initiatetenant",
	                "/multitenant/Updaprofiletetenant", "/multitenant/checkusermail", "/multitenant/tenantcontactno",
	                "/multitenant/Completeregistration", "/multitenant/updatetenantadminpassword",
	                "/reports/cloudsaveDocxsReport", "/reports/getSheetLSfileWithFileCode",
	                "/reports/getLSdocreportsLst", "/reports/Getorderbytype", "/reports/getFilecontentforSheet",
	                "/reports/Getorderbytype", "/", "/User/profile/*", "/User/Cloudprofile/*", "/Login/Logout",
	                "/Instrument/Unshareorderto", "/User/GetUserslocal", "/multitenant/ValidatetenantByID",
	                "/multitenant/ValidatetenantByName", "/User/getUserOnCode", "/helpdocument/gethelpnodes",
	                "/helpdocument/getdocumentonid", "/helpdocument/getnodeonpage", "/helpdocument/getdocumentcontent",
	                "/helpdocument/helpdownload/{fileid}", "/User/validatemailaddress",
	                "/Login/limsloginusertokengenarate", "/multitenant/Registerinvoice", "/Lims/getSheetsFromELN",
	                "/Lims/downloadSheetFromELN", "/Restcall/forSyncOrderFromLims",
	                "/protocol/downloadprotocolimage/{fileid}/{tenant}/{filename}/{extension}",
	                "/protocol/downloadprotocolfile/{fileid}/{tenant}/{filename}/{extension}",
	                "/Instrument/downloadsheetimages/{fileid}/{tenant}/{filename}/{extension}",
	                "/Instrument/downloadsheetimagestemp/{fileid}/{tenant}/{filename}/{extension}",
	                "/Instrument/downloadsheetimagessql/{fileid}/{filename}/{extension}",
	                "/Instrument/downloadsheetimagessqltempsql/{fileid}/{filename}/{extension}",
	                "/protocol/downloadprotocolorderimage/{fileid}/{tenant}/{filename}/{extension}",
	                "/protocol/downloadprotocolorderfiles/{fileid}/{tenant}/{filename}/{extension}",
	                "/protocol/downloadprotocolimagesql/{fileid}/{filename}/{extension}",
	                "/protocol/downloadprotocolfilesql/{fileid}/{filename}/{extension}",
	                "/protocol/downloadprotocolorderimagesql/{fileid}/{filename}/{extension}",
	                "/protocol/downloadprotocolorderfilesql/{fileid}/{filename}/{extension}",
	                "/helpdocument/downloadhelpimage/{fileid}/{tenant}/{filename}/{extension}",
	                "/Instrument/Getuserworkflow", "/Instrument/Getuserprojects", "/User/GetUserRightsonUser",
	                "/AuditTrail/GetAuditconfigUser", "/protocol/downloadprotocolvideo/{fileid}/{tenant}/{filename}/{extension}",
	                "/protocol/downloadprotocolvideosql/{fileid}/{filename}/{extension}",
	                "/protocol/downloadprotocolordervideo/{fileid}/{tenant}/{filename}/{extension}",
	                "/protocol/downloadprotocolordervideosql/{fileid}/{filename}/{extension}",
	                "/protocol/downloadprotocolordervideosql/{fileid}/{filename}/{extension}",
	                "/protocol/downloadprotocolvideosql/{fileid}/{filename}/{extension}", "/Lims/getOrdersFromELN",
	                "/Lims/getOrderTagFromELN", "/Lims/getUsersFromELN", "/Lims/getSiteFromELN", "/getMethod",
	                "/multitenant/getPremiumTenant", "/transaction/updateMaterialDynamicTable",
	                "/transaction/updateMappedTemplateFieldPropsMaterialTable", "/multitenant/updateplanrights",
	                "/User/getActiveUserCount", "/multitenant/updateplanType","/Login/getlicense","/Login/loadmultisite","/Login/LoadSitewithoutgzip","/Login/Logintenat/{Tenantname}/{Username},",
	                "/Login/importchemdata","/Instrument/downloadsheetimages","/Restcall/getlinkedinuserprofile",
	                "/Freeuserlogin/Createuser","/Freeuserlogin/Loginfreeuser","/Freeuserlogin/Validateuser",
	                "/Freeuserlogin/Setpassword","/Freeuserlogin/Resetpassword","/Freeuserlogin/Loginfreeuserwithname",
	                "/Login/verifyRecaptcha",	                
	                "/DashBoardDemo/Getdashboardordercount",
	                "/DashBoardDemo/Getdashboardorders","/documenteditor/api/wordeditor/Import","/smartdevice/Getdata","/documenteditor/Import"
	                ,"/documenteditor/api/wordeditor/RestrictEditing","/documenteditor/api/wordeditor/SystemClipboard",
	                "/Freeuserlogin/GetStartSkip"
	                
	                )
	        
	        		
	        .permitAll()
			
	        // all other requests need to be authenticated
	        .anyRequest().authenticated()
	        .and()
	        // make sure we use stateless session; session won't be used to store user's state.
	        .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
	        .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	        
	    // Add a filter to validate the tokens with every request
	    httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
	}

//	@Override
//	protected void configure(HttpSecurity httpSecurity) throws Exception {
//		// We don't need CSRF for this example
//		httpSecurity
//		.cors()
//		.and()
//		.csrf()
//		.disable()
//		.headers()
//		.frameOptions()
//		.deny()
//		.and()
//		// dont authenticate this particular request
//		.authorizeRequests().antMatchers("/authenticate").permitAll().
//		antMatchers("/multitenant/otpvarification").permitAll().
//		antMatchers("/multitenant/afterotpverified").permitAll().
//		antMatchers("/registerMail").permitAll().
//		antMatchers("/multitenant/sendotp").permitAll().
//		antMatchers("/Login/LoadSite").permitAll().
//		antMatchers("/Login/LoadDomain").permitAll().
//		antMatchers("/Login/azureusertokengenrate").permitAll().
//		antMatchers("/Login/CheckUserAndPassword").permitAll().
////		antMatchers("/Login/CheckUserPassword").permitAll().
//		antMatchers("/Login/updateActiveUserTime").permitAll().
//		antMatchers("/Login/createuserforazure").permitAll().
//		antMatchers("/Login/UpdatePassword").permitAll().
//		antMatchers("/Login/Validateuser").permitAll().
//		antMatchers("/Login/LinkLogin").permitAll().
//		antMatchers("/User/InsertUpdateUserGroup").permitAll().
//		antMatchers("/User/InsertUpdateUser").permitAll().
//		antMatchers("/User/getActiveUserCount").permitAll().
//		antMatchers("/User/GetUsers").permitAll().
//		antMatchers("/AuditTrail/AuditConfigurationrecord").permitAll().
//		antMatchers("/User/GetPasswordPolicySitewise").permitAll().
//		antMatchers("/User/Createcentraliseduser").permitAll().
//		antMatchers("/User/Getallcentraliseduser").permitAll().
//		antMatchers("/User/Getcentraliseduserbyid").permitAll().
//		antMatchers("/Instrument/GetInstrumentParameters").permitAll().
//		antMatchers("/Instrument/Insertshareorder").permitAll().
//		antMatchers("/Instrument/GetsharedtomeorderStatus").permitAll().
//		antMatchers("/Instrument/GetResultsharedfileverContent").permitAll().
//		antMatchers("/Instrument/SaveSharedResultfile").permitAll().
//		antMatchers("/Instrument/SharedCloudUploadattachments").permitAll().
//		antMatchers("/Instrument/SharedUploadattachments").permitAll().
//		antMatchers("/Instrument/SharedClouddeleteattachments").permitAll().
//		antMatchers("/Instrument/shareddeleteattachments").permitAll().
//		antMatchers("/Instrument/SharedClouddownloadattachments").permitAll().
//		antMatchers("/Instrument/Shareddownloadattachments").permitAll().
//		antMatchers("/Instrument/Sharedattachment").permitAll().
//		antMatchers("/Instrument/Getorderforlink").permitAll().
//		antMatchers("/Instrument/getmetatag").permitAll().
//		antMatchers("/Instrument/downloadNonCloud/{param}/{tenant}/{fileid}").permitAll().
//		antMatchers("/Instrument/downloadparserNonCloud/{param}/{tenant}/{fileid}").permitAll().
//		antMatchers("/Instrument/download/{param}/{fileid}").permitAll().
//		antMatchers("/Instrument/downloadparser/{param}/{fileid}").permitAll().
//		antMatchers("/Instrument/Sharedcloudattachment").permitAll().
//		antMatchers("/multitenant/Registertenant").permitAll().
//		antMatchers("/multitenant/Registertenantid").permitAll().
//		antMatchers("/multitenant/Registercustomersubscription").permitAll().
//		antMatchers("/multitenant/Validatetenant").permitAll().
//		antMatchers("/multitenant/checktenantid").permitAll().
//		antMatchers("/multitenant/tenantlogin").permitAll().
//		antMatchers("/multitenant/Getalltenant").permitAll().
//		antMatchers("/multitenant/Gettenantonid").permitAll().
//		antMatchers("/multitenant/Updatetenant").permitAll().
//		antMatchers("/multitenant/Initiatetenant").permitAll().
//		antMatchers("/multitenant/Updaprofiletetenant").permitAll().
//		antMatchers("/multitenant/checkusermail").permitAll().
//		antMatchers("/multitenant/tenantcontactno").permitAll().
//		antMatchers("/multitenant/Completeregistration").permitAll().
//		antMatchers("/multitenant/updatetenantadminpassword").permitAll().
//		antMatchers("/reports/cloudsaveDocxsReport").permitAll().
//		antMatchers("/reports/getSheetLSfileWithFileCode").permitAll().
//		antMatchers("/reports/getLSdocreportsLst").permitAll().
//		antMatchers("/reports/Getorderbytype").permitAll().
//		antMatchers("/reports/getFilecontentforSheet").permitAll().
//		antMatchers("/reports/Getorderbytype").permitAll().
////		antMatchers("/").permitAll().
//		antMatchers("/User/profile/*").permitAll().
//		antMatchers("/User/Cloudprofile/*").permitAll().
//		antMatchers("/Login/Logout").permitAll().
//		antMatchers("/Instrument/Unshareorderto").permitAll().
//		antMatchers("/User/GetUserslocal").permitAll().
//		antMatchers("/multitenant/ValidatetenantByID").permitAll().
//		antMatchers("/multitenant/ValidatetenantByName").permitAll().
//		antMatchers("/User/getUserOnCode").permitAll().
//		antMatchers("/helpdocument/gethelpnodes").permitAll().
//		antMatchers("/helpdocument/getdocumentonid").permitAll().
//		antMatchers("/helpdocument/getnodeonpage").permitAll().
//		antMatchers("/helpdocument/getdocumentcontent").permitAll().
//		antMatchers("/helpdocument/helpdownload/{fileid}").permitAll().
//		antMatchers("/User/validatemailaddress").permitAll().
//		antMatchers("/Login/limsloginusertokengenarate").permitAll().
//		antMatchers("/multitenant/Registerinvoice").permitAll().
//		antMatchers("/Lims/getSheetsFromELN").permitAll().
//		antMatchers("/Lims/downloadSheetFromELN").permitAll().
//		antMatchers("/Restcall/forSyncOrderFromLims").permitAll().
//		antMatchers("/protocol/downloadprotocolimage/{fileid}/{tenant}/{filename}/{extension}").permitAll().
//		antMatchers("/protocol/downloadprotocolfile/{fileid}/{tenant}/{filename}/{extension}").permitAll().
//		antMatchers("/Instrument/downloadsheetimages/{fileid}/{tenant}/{filename}/{extension}").permitAll().
//		antMatchers("/Instrument/downloadsheetimagestemp/{fileid}/{tenant}/{filename}/{extension}").permitAll().
//		antMatchers("/Instrument/downloadsheetimagessql/{fileid}/{filename}/{extension}").permitAll().
//		antMatchers("/Instrument/downloadsheetimagessqltempsql/{fileid}/{filename}/{extension}").permitAll().
//		antMatchers("/protocol/downloadprotocolorderimage/{fileid}/{tenant}/{filename}/{extension}").permitAll().
//		antMatchers("/protocol/downloadprotocolorderfiles/{fileid}/{tenant}/{filename}/{extension}").permitAll().
//		antMatchers("/protocol/downloadprotocolimagesql/{fileid}/{filename}/{extension}").permitAll().
//		antMatchers("/protocol/downloadprotocolfilesql/{fileid}/{filename}/{extension}").permitAll().
//		antMatchers("/protocol/downloadprotocolorderimagesql/{fileid}/{filename}/{extension}").permitAll().
//		antMatchers("/protocol/downloadprotocolorderfilesql/{fileid}/{filename}/{extension}").permitAll().
//		antMatchers("/helpdocument/downloadhelpimage/{fileid}/{tenant}/{filename}/{extension}").permitAll().
//		antMatchers("/Instrument/Getuserworkflow").permitAll().
//		antMatchers("/Instrument/Getuserprojects").permitAll().
//		antMatchers("/User/GetUserRightsonUser").permitAll().
//		antMatchers("/AuditTrail/GetAuditconfigUser").permitAll().
//		antMatchers("/protocol/downloadprotocolvideo/{fileid}/{tenant}/{filename}/{extension}").permitAll().
//		antMatchers("/protocol/downloadprotocolvideosql/{fileid}/{filename}/{extension}").permitAll().
//		antMatchers("/protocol/downloadprotocolordervideo/{fileid}/{tenant}/{filename}/{extension}").permitAll().
//		antMatchers("/protocol/downloadprotocolordervideosql/{fileid}/{filename}/{extension}").permitAll().
//		antMatchers("/protocol/downloadprotocolordervideosql/{fileid}/{filename}/{extension}").permitAll().
//		antMatchers("/protocol/downloadprotocolvideosql/{fileid}/{filename}/{extension}").permitAll().
//		antMatchers("/Lims/getOrdersFromELN").permitAll().
//		antMatchers("/Lims/getOrderTagFromELN").permitAll().
//		antMatchers("/Lims/getUsersFromELN").permitAll().
//		antMatchers("/Lims/getSiteFromELN").permitAll().
//		antMatchers("/multitenant/getPremiumTenant").permitAll().
//		antMatchers("/transaction/updateMaterialDynamicTable").permitAll().
//		antMatchers("/transaction/updateMappedTemplateFieldPropsMaterialTable").permitAll().
//		antMatchers("/multitenant/updateplanrights").permitAll().
//		antMatchers("/User/getActiveUserCount").permitAll().
//		antMatchers("/multitenant/updateplanType").permitAll().
//
//
//		
//	      
//		// all other requests need to be authenticated
//		anyRequest().authenticated().and().
//		// make sure we use stateless session; session won't be used to
//		// store user's state.
//		exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and().sessionManagement()
//		.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//		// Add a filter to validate the tokens with every request
//		httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
//	}
//	
//@Override
//protected void configure(HttpSecurity http) throws Exception {
//    http
//        // ...
//        .csrf().disable();
//}
//
//    
//    
//  @Bean
//  CorsConfigurationSource corsConfigurationSource() {
//	   List<String> lstheader = new ArrayList<String>();
//	   lstheader.add("X-TenantID");
//      CorsConfiguration configuration = new CorsConfiguration();
//      configuration.setAllowedOrigins(Arrays.asList("https://example.com"));
//      configuration.setAllowedMethods(Arrays.asList("GET","POST"));
//      configuration.setExposedHeaders(lstheader);
//      configuration.setAllowedHeaders(lstheader);
//      UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//      source.registerCorsConfiguration("/**", configuration);
//      return source;
//      
//    
//  }
  
  
  
}
