package com.agaram.eln.primary.controller.exportfileasxlsxcsvpdf;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * This class holds method to generate output file with the input
 * content and provided file format.
 * @author ATE128
 *
 */
@CrossOrigin
@RestController
public class ExportController {
    
	/**
	 * This method is used to call appropriate view resolver based on file format
	 * provided in the request header.Once the mvc object is loaded with input content,
	 * the servlet controller will automatically call the appropriate resolver.
	 * @param mapObject [Map] object holding 
	 *  "exportDataList" - data to export,
	 *  "deepDataObject" - to access deeply nested map object
	 *  "title" - title for the pdf file content
	 *  "footerText" - footer message provided in the pdf file
	 *  "pageOrientation" - page orientation for pdf file content
	 * @return response with content to download
	 */
    @PostMapping(value="/exportFile", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ModelAndView exportFile( @RequestBody Map<String, Object> mapObject) {
    	
    	final ObjectMapper mapper = new ObjectMapper();	
    
    	final  List<Map<String, Object>> dataList = mapper.convertValue(mapObject.get("exportDataList"), 
				new TypeReference<List<Map<String, Object>>>() {}); 		
		@SuppressWarnings("unchecked")
		final Map<String, Object> deepDataObject = mapper.convertValue(mapObject.get("deepDataObject"),Map.class);
			
		ModelAndView mav = new ModelAndView();
    	
    	mav.addObject("exportDataList", dataList);
        mav.addObject("deepDataObject", deepDataObject);
        mav.addObject("title", mapper.convertValue(mapObject.get("title"), String.class));
        mav.addObject("footerText", 
        			mapper.convertValue(mapObject.get("footerText"), String.class));
        mav.addObject("pageOrientation", 
        			mapper.convertValue(mapObject.get("pageOrientation"), String.class));	
        mav.addObject("localeCode", 
    			mapper.convertValue(mapObject.get("localeCode"), String.class));
        mav.addObject("timeZone", 
    			mapper.convertValue(mapObject.get("timeZone"), String.class));
        return mav;
    }
}
