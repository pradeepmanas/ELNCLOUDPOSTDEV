package com.agaram.eln.primary.service.helpdocument;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import java.util.List;


import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Service;

import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.helpdocument.Helpdocument;
import com.agaram.eln.primary.model.helpdocument.Helptittle;
import com.agaram.eln.primary.repository.helpdocument.HelpdocumentRepository;
import com.agaram.eln.primary.repository.helpdocument.HelptittleRepository;




@Service
@EnableJpaRepositories(basePackageClasses = HelpdocumentRepository.class)
public class helpdocumentservice {
	@Autowired
	HelpdocumentRepository HelpdocumentRepository;
	
	@Autowired
	HelptittleRepository helptittleRepository;
	
	public Map<String, Object> adddocument(Map<String, Object> obj) {
		Helpdocument Helpdocument = new Helpdocument();
		Helpdocument.setLshelpdocumentcontent(new JSONObject(obj).toString());
		Helpdocument.setDocumentname((String) obj.get("documentname"));
		Map<String, Object> object = new HashMap<String, Object>();
		List<Helpdocument> objMap=HelpdocumentRepository.findAll();
		if(objMap.size() ==0) {
			Helpdocument =HelpdocumentRepository.save(Helpdocument);
		}else if(objMap.size() ==1) {
			int count =HelpdocumentRepository.setlshelpdocumentcontentanddocumentnameByid(Helpdocument.getLshelpdocumentcontent(),Helpdocument.getDocumentname(),objMap.get(0).getId());
			object.put("success",count);
		}
	
		object.put("success",Helpdocument);
		return object;
		
	}
	
	public Map<String, Object> getdocumentcontent() {
		Map<String, Object> object = new HashMap<String, Object>();
		try {
		List<Helpdocument> objMap=HelpdocumentRepository.findAll();
		
		object.put("Helpdocument", objMap);
		}
		catch (Exception e) {
			object.put("Helpdocument", "notset");
		}
		return object;
	}
	
	public Helptittle savenode(Helptittle objhelp)
	{
		Response objresponse = new Response();
		
		List<Helptittle> lstnode = new ArrayList<Helptittle>();
		if(objhelp.getNodecode() != null)
		{
			lstnode = helptittleRepository.findByTextAndParentcodeAndNodecodeNot(objhelp.getText(),objhelp.getParentcode() ,objhelp.getNodecode());
		}
		else
		{
			lstnode = helptittleRepository.findByTextAndParentcode(objhelp.getText(), objhelp.getParentcode() );
		}
			
		if(lstnode != null && lstnode.size()>0)
		{
			objresponse.setStatus(false);
			objresponse.setInformation("Node already exists");
			objhelp.setObjResponse(objresponse);
			return objhelp;
		}
		
		objhelp = helptittleRepository.save(objhelp);
		
		objresponse.setStatus(true);
		objhelp.setObjResponse(objresponse);
		
		return objhelp;
	}

	public List<Helptittle> gethelpnodes( Helptittle objhelp)
	{
		return helptittleRepository.findByOrderByNodeindexAsc();
	}
	
	public Helpdocument getdocumentonid(Helpdocument objhelp)
	{
		return HelpdocumentRepository.findByNodecode(objhelp.getNodecode());
	}
	
	public Helpdocument savedocument(Helpdocument objhelp)
	{
		return HelpdocumentRepository.save(objhelp);
	}
	
	public List<Helptittle>  sortNodesforhelp(List<Helptittle>  objhelp)
	{
		objhelp = helptittleRepository.save(objhelp);
		return objhelp;
	}
}
