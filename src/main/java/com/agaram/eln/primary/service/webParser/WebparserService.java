package com.agaram.eln.primary.service.webParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.agaram.eln.primary.model.instrumentDetails.LSinstruments;
import com.agaram.eln.primary.model.instrumentDetails.LsMethodFields;
import com.agaram.eln.primary.model.webParser.Lswebparserfield;
import com.agaram.eln.primary.model.webParser.Lswebparserinstrument;
import com.agaram.eln.primary.model.webParser.Lswebparserinstrumentcategory;
import com.agaram.eln.primary.model.webParser.Lswebparsermethod;
import com.agaram.eln.primary.model.webParser.Lswebparsersite;
import com.agaram.eln.primary.model.webParser.Lswebparseruser;
import com.agaram.eln.primary.repository.webParser.LswebparserinstrumentRepository;
import com.agaram.eln.primary.repository.webParser.LswebparserinstrumentcategoryRepository;
import com.agaram.eln.primary.repository.webParser.LswebparserinstrumenttypeRepository;
import com.agaram.eln.primary.repository.webParser.LswebparsermethodRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class WebparserService {

	@Autowired
	private Environment env;

	ObjectMapper mapper = new ObjectMapper();

	@Autowired
	private LswebparsermethodRepository LswebparsermethodRepository;
	@Autowired
	private LswebparserinstrumentRepository LswebparserinstrumentRepository;
	@Autowired
	private LswebparserinstrumentcategoryRepository LswebparserinstrumentcategoryRepository;
	@Autowired
	private LswebparserinstrumenttypeRepository LswebparserinstrumenttypeRepository;

	@Autowired
	private com.agaram.eln.primary.repository.webParser.LswebparserfieldRepository LswebparserfieldRepository;

	public String ImportLimsTest(String str) throws Exception {
		
		ObjectMapper fieldMapper = new ObjectMapper()
				.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		Map<String, Object> map = new HashMap<>();

		String result = "";

		Map<String, Object> childMap = new HashMap<>();

		childMap.put("sitekey", 1);
		childMap.put("sitename", "chennai");
		childMap.put("status", 1);

		map.put("site", childMap);

		String getTest = webparserCalling("getMethod", map);
		List<Lswebparsermethod> lstObj = fieldMapper.readValue(getTest, new TypeReference<List<Lswebparsermethod>>() {
		});

		if (lstObj.size() > 0) {

			Lswebparseruser objUser = new Lswebparseruser();

			objUser.setUserkey(1);

			Lswebparsersite objSite = new Lswebparsersite();

			objSite.setSitekey(1);

			List<Lswebparserfield> lstParser = new ArrayList<Lswebparserfield>();

			lstObj.forEach((obj) -> {

				Lswebparserinstrumentcategory objInstCat = obj.getInstmaster().getInstcategory();
				objInstCat.setCreatedby(objUser);

				Lswebparserinstrument objInst = obj.getInstmaster();
				objInst.setCreatedby(objUser);
				objInst.setSite(objSite);

				LswebparserinstrumentcategoryRepository.save(objInstCat);
				LswebparserinstrumenttypeRepository.save(obj.getInstmaster().getInsttype());
				LswebparserinstrumentRepository.save(objInst);

				obj.setCreatedby(objUser);
				obj.setSite(objSite);
				LswebparsermethodRepository.save(obj);

				Map<String, Object> mapObj = new HashMap<>();
				mapObj.put("methodKey", obj.getMethodkey());

				String getParserField = webparserCalling("getParserFieldByMethodKey", mapObj);
				String getSubParserField = webparserCalling("getSubParserFieldByMethodKey", mapObj);

				try {
					List<Lswebparserfield> lstObjParser = fieldMapper.readValue(getParserField,
							new TypeReference<List<Lswebparserfield>>() {
							});

					List<Lswebparserfield> lstObjSubParser = fieldMapper.readValue(getSubParserField,
							new TypeReference<List<Lswebparserfield>>() {
							});

					lstObjSubParser = lstObjSubParser.stream().peek(f -> {
						f.setParserfieldkey(f.getSubparserfieldkey());
						f.setMethod(obj);
						f.setParserfieldname(f.getSubparserfieldname());
					}).collect(Collectors.toList());
					lstObjParser = lstObjParser.stream().peek(f -> f.setMethod(obj)).collect(Collectors.toList());

					lstParser.addAll(lstObjParser);
					lstParser.addAll(lstObjSubParser);

				} catch (IOException e) {
					
					e.printStackTrace();
				}
			});

			LswebparserfieldRepository.deleteAll();

			LswebparserfieldRepository.save(lstParser);

		}

		return result;
	}

	public String webparserCalling(String Service, Map<String, Object> map) {

		final String url = env.getProperty("webparserservice.url") + Service;

		RestTemplate restTemplate = new RestTemplate();

		String result = restTemplate.postForObject(url, map, String.class);

		return result;
	}

	public List<LsMethodFields> getwebparsemethods() {
		List<LsMethodFields> lstObjMethod = LswebparserfieldRepository.getMethoFeilds();
//		List<LsMethodFields> lstObjMethod = new ArrayList<LsMethodFields>();
		return lstObjMethod;
	}

	public Object getwebparserInstruments() {

		List<LSinstruments> lstObjMethod = LswebparserfieldRepository.getInstruments();
//		List<LSinstruments> lstObjMethod = new ArrayList<LSinstruments>();
		return lstObjMethod;
	}

}
