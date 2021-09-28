package com.agaram.eln.primary.view;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import org.apache.poi.ss.usermodel.Cell;
import org.supercsv.io.CsvListWriter;
import org.supercsv.io.ICsvListWriter;
import org.supercsv.prefs.CsvPreference;

import com.agaram.eln.primary.util.MapUtil;

/**
 * This class to use to create CSV file with provided input data
 * @author ATE153
 * @version 1.0.0
 * @since   07- Jan- 2020
 */
public class CsvView extends AbstractCsvView {

    @SuppressWarnings("unchecked")
	@Override
    protected void buildCsvDocument(Map<String, Object> model, HttpServletRequest request, HttpServletResponse
            response) throws Exception {

        response.setHeader("Content-Disposition", "attachment; filename=\"my-csv-file.csv\"");

        List<Map<String, Object>> dataList = (List<Map<String, Object>>) model.get("exportDataList");        
        Map<String, Object> deepDataObject = (Map<String, Object>)  model.get("deepDataObject");
     
        final List<String> columnList = new ArrayList<String>();	    	
    	
   	    for(Map.Entry<String, Object> dataEntry: dataList.get(0).entrySet())
		{
	   		if (!dataEntry.getKey().equalsIgnoreCase("parsedXML") &&
	   				 !dataEntry.getKey().equalsIgnoreCase("xmlrecords")
	   				 && !dataEntry.getKey().equalsIgnoreCase("detailedData")) {
	   			if(!dataEntry.getKey().equalsIgnoreCase("select")) {
		   			 columnList.add(dataEntry.getKey());
		   		}
	   		 }
		}
   	    
   	    final String pattern = "dd-MM-yyyy HH:mm:ss";
   	 	final SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);	
   	    
   	    ICsvListWriter csvWriter = new  CsvListWriter(response.getWriter(),
                CsvPreference.STANDARD_PREFERENCE);
        csvWriter.write(columnList.stream().collect(Collectors.joining(",")));	
        
        for (Map<String, Object> data : dataList)
        {   
       	 	 Map<String, Object> xmlData = new HashMap<String, Object>();
        	 List<String> objList = new ArrayList<String>();
        	 for(Map.Entry<String, Object> dataEntry: data.entrySet())
        	 {           	
        		 if (!dataEntry.getKey().equalsIgnoreCase("parsedXML") &&
	    				 !dataEntry.getKey().equalsIgnoreCase("xmlrecords")
	    				 && !dataEntry.getKey().equalsIgnoreCase("detailedData")) 
        		 {          			 
        			 if (dataEntry.getValue() instanceof Integer)
	       			 {
        				 objList.add(Integer.toString((Integer) dataEntry.getValue()));	       				 
	       			 }
	       			 else  if (dataEntry.getValue() instanceof java.util.LinkedHashMap)
	       			 {
	       				 String value ="";
	       				 if(deepDataObject.containsKey(dataEntry.getKey()))
	       				 {
	       					 List<String> dataValueMap = (List<String>) deepDataObject.get(dataEntry.getKey());	        					 
	   						 value = new MapUtil().getNestedValue((Map<String, Object>)dataEntry.getValue(), dataValueMap);
    				
	       				 }       				 
	       				objList.add(value);
	       			 }
	       			 else
	       			 {
	       				 if (dataEntry.getKey().equalsIgnoreCase("createddate"))
	      				 { 	      						 
	       					objList.add((String)dataEntry.getValue());
	      				 }
	       				 else
	       				 {
	       					if(!dataEntry.getKey().equalsIgnoreCase("select")) {
	         					objList.add((String)dataEntry.getValue());
	       					 }
	       				 }
	       			 }	 
        		 } 
        		 if (dataEntry.getKey().equalsIgnoreCase("parsedXML")||
         				 dataEntry.getKey().equalsIgnoreCase("detailedData"))
     			 {      
        			xmlData = (Map<String, Object>)dataEntry.getValue();
     			 }     			
        	 } 

        	 csvWriter.write(objList.stream().collect(Collectors.joining(",")));
        	 if (xmlData != null && xmlData.size() >0)
    		 {
        		 copyXMLToCSV(xmlData, csvWriter, dateFormat, deepDataObject);
    		
    		 }           	 
       }
       csvWriter.close();

    }
    
    /**
     * This method is used to organize the xml formatted data or the map object of detailed content to a new row.
     * @param dataMap [Map] object with content
     * @param csvWriter [ICsvListWriter] object to write content as csv
     * @param dateFormat [SimpleDateFormat] object to format date field
     * @param deepDataObject [Map] object containing 'values to access deeply nested map object
     * @throws IOException thrown if caught on document writing
     */
    @SuppressWarnings({ "rawtypes", "unchecked"})
	private void copyXMLToCSV(Map<String, Object> dataMap,
	    		ICsvListWriter csvWriter, SimpleDateFormat dateFormat,
	    		Map<String, Object> deepDataObject) throws IOException
    {        	 
    	
    	 for (Map.Entry<String,Object> entry : dataMap.entrySet()) 
    	 {        	 
    		 csvWriter.write("\t");
    		 csvWriter.write(entry.getKey().toUpperCase());
		 		 
			 List<Map<String, Object>> mapList = (ArrayList)entry.getValue();
			 			 
			 int loopIndex = 1;
			 for (Map<String, Object> map : mapList) 
			 {				 
				 if (loopIndex == 1)
				 {		
					 List<String> objList = new ArrayList<String>();
					 for (String keyEntry : map.keySet()) {					 
						 objList.add(keyEntry.toUpperCase());						
					 }	
					 csvWriter.write(objList.stream().collect(Collectors.joining(",")));					 
				}
				 List<String> objList1 = new ArrayList<String>();
     			 for (Map.Entry<String, Object> valueEntry : map.entrySet()) {  
			
     				 if (valueEntry.getKey().equalsIgnoreCase("createddate"))
					 {
     					objList1.add((String)valueEntry.getValue());
					 }
					 else
					 {								 
						 if (valueEntry.getValue() instanceof Integer)
		       			 {
							 objList1.add(Integer.toString((Integer) valueEntry.getValue()));	       				 
		       			 }					 
						 else if (valueEntry.getValue() instanceof java.util.LinkedHashMap)
		       			 {
		       				 String value ="";
	        				 if(deepDataObject.containsKey(valueEntry.getKey()))
	        				 {
	        					 List<String> dataValueMap = (List<String>) deepDataObject.get(valueEntry.getKey());	        					 
	    						 value = new MapUtil().getNestedValue((Map<String, Object>)valueEntry.getValue(), dataValueMap);
	        				
	        				 }	        				 
	        				 objList1.add(value);
		       			 }
						 else
						 {
							 objList1.add((String)valueEntry.getValue());	
						 }
					 }
     							
				 }
     			 csvWriter.write(objList1.stream().collect(Collectors.joining(",")));
				 loopIndex++;
			 }		 	
			
    	 }  
    	
    }
}

