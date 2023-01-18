package com.agaram.eln.primary.service.templates;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.templates.LsMappedTags;
import com.agaram.eln.primary.model.templates.LsMappedTemplate;
import com.agaram.eln.primary.model.templates.LsUnmappedTags;
import com.agaram.eln.primary.model.templates.LsUnmappedTemplate;
import com.agaram.eln.primary.repository.templates.LsMappedTagsRepository;
import com.agaram.eln.primary.repository.templates.LsMappedTemplateRepository;
import com.agaram.eln.primary.repository.templates.LsUnmappedTagsRepository;
import com.agaram.eln.primary.repository.templates.LsUnmappedTemplateRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class TemplateMasterService {
	
	@Autowired
    private LsMappedTemplateRepository LsMappedTemplateRepository;
	
	@Autowired
    private LsUnmappedTemplateRepository LsUnmappedTemplateRepository;
	
	@Autowired
    private LsMappedTagsRepository LsMappedTagsRepository;
	
	@Autowired
    private LsUnmappedTagsRepository LsUnmappedTagsRepository;
	
	@Autowired
	private Environment env;
	
	static final Logger logger = Logger.getLogger(TemplateMasterService.class.getName());
	
	ObjectMapper mapper = new ObjectMapper();
	

	public Response ImportTemplate() throws Exception {
		
		Response res = new Response();
		
		try {
			String getTemplate=SDMSservice("Template/getTemplateMasterWithTags");
			
			Map<String, Object> mapTemplateObj = mapper.readValue(getTemplate,new TypeReference<Map<String, Object>>() {});
			
			logger.info("Template Objeect : "+mapTemplateObj);
			if(mapTemplateObj.containsKey("MappedTemplate")) {
				
				if(mapTemplateObj.get("MappedTemplate") != null) {
					
					Map<String, Object> mappedTemplateObj = mapper.convertValue(mapTemplateObj.get("MappedTemplate"),
							new TypeReference<Map<String, Object>>() {});
					
					int i = 0;
					while(mappedTemplateObj.size()>i) {
						logger.info("keyset Objeect : "+mappedTemplateObj.keySet().toArray()[i]);
						
						LsMappedTemplate getTemp = new LsMappedTemplate();
						List<LsMappedTags> lstags = new ArrayList<LsMappedTags>();
						List<Map<String, Object>> lstTag = mapper.convertValue(mappedTemplateObj.get(mappedTemplateObj.keySet().toArray()[i]),
								new TypeReference<List<Map<String, Object>>>() {});
						
						getTemp = LsMappedTemplateRepository.findByTemplatename((String) mappedTemplateObj.keySet().toArray()[i]);
						
						if(getTemp != null) {
							LsMappedTagsRepository.deleteByTemplatecode(getTemp.getTemplatecode());
							int n = 0;
							while(lstTag.size() > n) {
								
								LsMappedTags tag = new LsMappedTags();
								
								tag.setNonhierarchystatus((Integer) lstTag.get(n).get("NonHierarchyStatus"));
								tag.setTagid((Integer) lstTag.get(n).get("TagID"));
								tag.setTagname((String) lstTag.get(n).get("TagName"));
								tag.setTemplatecode(getTemp.getTemplatecode());
								
								LsMappedTagsRepository.save(tag);
								
								lstags.add(tag);
								n++;
							}
							
							getTemp.setLstags(lstags);
							LsMappedTemplateRepository.save(getTemp);
						}
						else {
							LsMappedTemplate template = new LsMappedTemplate();
							
							template.setTemplatename((String) mappedTemplateObj.keySet().toArray()[i]);
							LsMappedTemplateRepository.save(template);
							
							int n = 0;
							while(lstTag.size() > n) {
								
								LsMappedTags tag = new LsMappedTags();
								
								tag.setNonhierarchystatus((Integer) lstTag.get(n).get("NonHierarchyStatus"));
								tag.setTagid((Integer) lstTag.get(n).get("TagID"));
								tag.setTagname((String) lstTag.get(n).get("TagName"));
								tag.setTemplatecode(template.getTemplatecode());
								
								LsMappedTagsRepository.save(tag);
								
								lstags.add(tag);
								n++;
							}
							
							template.setLstags(lstags);
							LsMappedTemplateRepository.save(template);
						}
						
						i++;
					}	
				}
			}
			if(mapTemplateObj.containsKey("UnMappedTemplate")) {
				
				if(mapTemplateObj.get("UnMappedTemplate") != null) {
					
					Map<String, Object> unMappedTemplateObj = mapper.convertValue(mapTemplateObj.get("UnMappedTemplate"),
							new TypeReference<Map<String, Object>>() {});
					
					int i = 0;
					while(unMappedTemplateObj.size()>i) {
						logger.info("keyset Objeect : "+unMappedTemplateObj.keySet().toArray()[i]);
						
						LsUnmappedTemplate getTemp = new LsUnmappedTemplate();
						List<LsUnmappedTags> lstags = new ArrayList<LsUnmappedTags>();
						List<Map<String, Object>> lstTag = mapper.convertValue(unMappedTemplateObj.get(unMappedTemplateObj.keySet().toArray()[i]),
								new TypeReference<List<Map<String, Object>>>() {});
						
						getTemp = LsUnmappedTemplateRepository.findByTemplatename((String) unMappedTemplateObj.keySet().toArray()[i]);
						
						if(getTemp != null) {
							LsUnmappedTagsRepository.deleteByTemplatecode(getTemp.getTemplatecode());
							int n = 0;
							while(lstTag.size() > n) {
								
								LsUnmappedTags tag = new LsUnmappedTags();
								
								tag.setNonhierarchystatus((Integer) lstTag.get(n).get("NonHierarchyStatus"));
								tag.setTagid((Integer) lstTag.get(n).get("TagID"));
								tag.setTagname((String) lstTag.get(n).get("TagName"));
								tag.setTemplatecode(getTemp.getTemplatecode());
								
								LsUnmappedTagsRepository.save(tag);
								
								lstags.add(tag);
								n++;
							}
							
							getTemp.setLstags(lstags);
							LsUnmappedTemplateRepository.save(getTemp);
						}
						else {
							LsUnmappedTemplate template = new LsUnmappedTemplate();
							
							template.setTemplatename((String) unMappedTemplateObj.keySet().toArray()[i]);
							LsUnmappedTemplateRepository.save(template);
							
							int n = 0;
							while(lstTag.size() > n) {
								
								LsUnmappedTags tag = new LsUnmappedTags();
								
								tag.setNonhierarchystatus((Integer) lstTag.get(n).get("NonHierarchyStatus"));
								tag.setTagid((Integer) lstTag.get(n).get("TagID"));
								tag.setTagname((String) lstTag.get(n).get("TagName"));
								tag.setTemplatecode(template.getTemplatecode());
								
								LsUnmappedTagsRepository.save(tag);
								
								lstags.add(tag);
								n++;
							}
							
							template.setLstags(lstags);
							LsUnmappedTemplateRepository.save(template);
						}
						
						i++;
					}
				}
			}
			res.setInformation("success");
			res.setStatus(true);
			return res;
		}
		catch(Exception e) {
			logger.error("Error SDMSservice : "+e.getMessage());
			res.setInformation("failure");
			res.setStatus(false);
			return res;
		}
	}
	
	public String SDMSservice(String Service) throws Exception {
		String result ="";
		try {
			Map<String, Object> map = new HashMap<>();
			
			final String url = env.getProperty("sdms.template.service.url")+Service;
			
			logger.info("Template service Url : "+url);

		    RestTemplate restTemplate = new RestTemplate();
		    
		    result = restTemplate.postForObject(url, map, String.class);
		    
		    return result;	
		}
		catch(Exception e) {
			logger.error("Error SDMSservice : "+e.getMessage());
		}
		return result;
	}

}
