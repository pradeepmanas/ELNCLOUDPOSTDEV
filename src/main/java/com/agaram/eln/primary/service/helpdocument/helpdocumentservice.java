package com.agaram.eln.primary.service.helpdocument;

import java.util.HashMap;
import java.util.Map;

import java.util.ArrayList;
import java.util.List;


import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Service;

import com.agaram.eln.primary.model.helpdocumentmodel.Helpdocument;
import com.agaram.eln.primary.repository.helpdocument.HelpdocumentRepository;




@Service
@EnableJpaRepositories(basePackageClasses = HelpdocumentRepository.class)
public class helpdocumentservice {
	@Autowired
	HelpdocumentRepository HelpdocumentRepository;
	
	public Map<String, Object> adddocument(Map<String, Object> obj) {
		Helpdocument Helpdocument = new Helpdocument();
		String obj1=(String) obj.get("filedata");
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
		List<Helpdocument> objMap=HelpdocumentRepository.findAll();
		Map<String, Object> object = new HashMap<String, Object>();
		object.put("Helpdocument", objMap);
		return object;
	}

}
