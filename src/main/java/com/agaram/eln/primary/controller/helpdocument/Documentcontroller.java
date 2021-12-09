package com.agaram.eln.primary.controller.helpdocument;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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

import com.agaram.eln.primary.model.helpdocument.Helpdocument;
import com.agaram.eln.primary.model.helpdocument.Helptittle;
import com.agaram.eln.primary.service.helpdocument.helpdocumentservice;
import com.mongodb.gridfs.GridFSDBFile;
import com.nimbusds.oauth2.sdk.http.HTTPRequest;



@RestController
@RequestMapping(value = "/helpdocument",method = RequestMethod.POST)
public class Documentcontroller {

	@Autowired
	helpdocumentservice helpdocumentservice;
	
	@RequestMapping(value="/adddocument")
	public Map<String, Object> adddocument(@RequestBody Map<String, Object> argObj ) {
		
		return helpdocumentservice.adddocument(argObj);
//		return argObj;
		
	}
	
	@GetMapping(value="/getdocumentcontent")
	public Map<String, Object> getdocumentcontent(HttpServletRequest request){
		
		return helpdocumentservice.getdocumentcontent();
	
		
	}
	
	@GetMapping(value="/savenode")
	public Helptittle savenode(@RequestBody Helptittle objhelp)
	{
		return helpdocumentservice.savenode(objhelp);
	}

	@GetMapping(value="/gethelpnodes")
	public List<Helptittle> gethelpnodes(@RequestBody Helptittle objhelp)
	{
		return helpdocumentservice.gethelpnodes(objhelp);
	}
	
	@GetMapping(value="/getdocumentonid")
	public Helpdocument getdocumentonid(@RequestBody Helpdocument objhelp)
	{
		return helpdocumentservice.getdocumentonid(objhelp);
	}
	
	@GetMapping(value="/savedocument")
	public Helpdocument savedocument(@RequestBody Helpdocument objhelp)
	{
		return helpdocumentservice.savedocument(objhelp);
	}
	
	@GetMapping(value="/sortNodesforhelp")
	public List<Helptittle> sortNodesforhelp(@RequestBody List<Helptittle> objhelp)
	{
		return helpdocumentservice.sortNodesforhelp(objhelp);
	}
	
	@PostMapping(value="/deletenode")
	public List<Helptittle> deletenode(@RequestBody List<Helptittle> objhelp)
	{
		return helpdocumentservice.deletenode(objhelp);
		
	}
	
	@PostMapping(value="/deletechildnode")
	public Helptittle deletechildnode(@RequestBody Helptittle objhelp)
	{
		return helpdocumentservice.deletechildnode(objhelp);
		
	}

	@PostMapping("/storevideoforhelp")
    public Helpdocument storevideoforhelp(@RequestParam("nodecode") Integer nodecode, @RequestParam("file") MultipartFile file) throws IOException
    {
		return helpdocumentservice.storevideoforhelp(nodecode, file);
    }
	
	@PostMapping("/storevideoforhelponprim")
    public Helpdocument storevideoforhelponprim(@RequestParam("nodecode") Integer nodecode, @RequestParam("file") MultipartFile file) throws IOException
    {
		return helpdocumentservice.storevideoforhelponprim(nodecode, file);
    }
	
	@RequestMapping(path = "/helpdownload/{fileid}", method = RequestMethod.GET)
	public ResponseEntity<InputStreamResource> helpdownload(@PathVariable String fileid) throws IOException {
		return helpdocumentservice.helpdownload(fileid);
	}
	
	@PostMapping("/getnodeonpage")
	public Helptittle getnodeonpage(@RequestBody Helptittle objhelp)
	{
		return helpdocumentservice.getnodeonpage(objhelp.getPage());
	}
	
	@PostMapping("/uploadhelpimages")
	public Map<String, Object> uploadhelpimages(@RequestParam("file") MultipartFile file,
			@RequestParam("originurl") String originurl, @RequestParam String ismultitenant)
	{
		return helpdocumentservice.uploadhelpimages(file, originurl, ismultitenant);
	}
	
	@RequestMapping(value = "downloadhelpimage/{fileid}/{tenant}/{filename}/{extension}", method = RequestMethod.GET)
	@GetMapping
	public ResponseEntity<InputStreamResource> downloadhelpimage(@PathVariable String fileid
			, @PathVariable String tenant, @PathVariable String filename, @PathVariable String extension ) throws IllegalStateException, IOException {
		
		ByteArrayInputStream bis = helpdocumentservice.downloadhelpimage(fileid, tenant);
		
	    HttpHeaders header = new HttpHeaders();
	    header.setContentType(MediaType.parseMediaType("image/png"));
	    header.set("Content-Disposition", "attachment; filename="+filename+"."+extension);
	    
	    return new ResponseEntity<>(new InputStreamResource(bis), header, HttpStatus.OK);
	}
	
	@RequestMapping(value = "downloadhelpimageonprim/{fileid}/{tenant}/{filename}/{extension}", method = RequestMethod.GET)
	@GetMapping
	public ResponseEntity<InputStreamResource> downloadhelpimageonprim(@PathVariable String fileid
			, @PathVariable String tenant, @PathVariable String filename, @PathVariable String extension)
			throws IllegalStateException, IOException {
		GridFSDBFile gridFsFile = helpdocumentservice.downloadhelpimageonprim(fileid);

		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.parseMediaType(gridFsFile.getContentType()));
		header.setContentLength(gridFsFile.getLength());
		header.set("Content-Disposition", "attachment; filename="+filename+"."+extension+"");

		return new ResponseEntity<>(new InputStreamResource(gridFsFile.getInputStream()), header, HttpStatus.OK);
	}
	
	@PostMapping("/removehelpimage")
	public boolean removehelpimage(@RequestBody Map<String, String> body)
	{
		return helpdocumentservice.removehelpimage(body);
	}
}
