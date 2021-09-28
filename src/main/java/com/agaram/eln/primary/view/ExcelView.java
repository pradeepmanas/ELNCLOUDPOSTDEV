package com.agaram.eln.primary.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import com.agaram.eln.primary.util.MapUtil;

/**
 * This class to use to create xlsx file with provided input data
 * @author ATE153
 * @version 1.0.0
 * @since   07- Jan- 2020
 */
public class ExcelView extends AbstractXlsxView{

	private static final int EXCEL_ROW_MAX = 1000000;
	
    @SuppressWarnings("unchecked")
	@Override
    protected void buildExcelDocument(Map<String, Object> model,
                                      Workbook workbook,
                                      HttpServletRequest request,
                                      HttpServletResponse response) throws Exception {

        // change the file name
        response.setHeader("Content-Disposition", "attachment; filename=\"my-xls-file.xlsx\"");
        
        List<Map<String, Object>> dataList = (List<Map<String, Object>>) model.get("exportDataList");        
        Map<String, Object> deepDataObject = (Map<String, Object>)  model.get("deepDataObject");
        String sheetName = (String) model.get("title");        

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
   	    
     	// Create a Workbook
     	/* CreationHelper helps us create instances of various things like DataFormat, 
        Hyperlink, RichTextString etc, in a format (HSSF, XSSF) independent way */
     	CreationHelper createHelper = workbook.getCreationHelper();

     	// Create a Sheet
     	Sheet sheet = workbook.createSheet(sheetName);

	     // Create a Font for styling header cells
	     Font headerFont = workbook.createFont();
	     headerFont.setBold(true);
	     headerFont.setFontHeightInPoints((short) 12);
	     headerFont.setColor(IndexedColors.BLUE.getIndex());

	     // Create a CellStyle with the font
	     CellStyle headerCellStyle = workbook.createCellStyle();
	     headerCellStyle.setFont(headerFont);  
	     
	     // Create a Font for styling header cells
	     Font tabHeaderFont = workbook.createFont();
	     tabHeaderFont.setBold(true);
	     tabHeaderFont.setFontHeightInPoints((short) 12);
	     tabHeaderFont.setColor(IndexedColors.GREEN.getIndex());

	     // Create a CellStyle with the font
	     CellStyle tabHeaderCellStyle = workbook.createCellStyle();
	     tabHeaderCellStyle.setFont(tabHeaderFont);  
	     Font boldFont = workbook.createFont();
         boldFont.setBold(true);
         
         // Create a Font for styling header cells
         Font gridHeaderFont = workbook.createFont();
         gridHeaderFont.setFontHeightInPoints((short) 12);
         gridHeaderFont.setColor(IndexedColors.RED.getIndex());

         // Create a CellStyle with the font
         CellStyle gridHeaderCellStyle = workbook.createCellStyle();
         gridHeaderCellStyle.setFont(gridHeaderFont);      

         //Create first row with header columns
         createHeaderRow(sheet, headerCellStyle, columnList);
         
         CellStyle wrapCellStyle = workbook.createCellStyle();
         wrapCellStyle.setWrapText(true);

         // Create Cell Style for formatting Date
         CellStyle dateCellStyle = workbook.createCellStyle();        
         dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy HH:mm:ss"));

         // Create Other rows and cells with data
         int rowNum = 1;
         
         final String pattern = "dd-MM-yyyy HH:mm:ss";
    	 @SuppressWarnings("unused")
		final SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);	
       
         for (Map<String, Object> data : dataList)
         {          	
         	 Map<String, Object> sheetObject = validateSheetRow(workbook, sheet, rowNum, sheetName, 
          			 headerCellStyle, columnList);
         	 rowNum = (int) sheetObject.get("RowNumber");
         	 sheet = (Sheet) sheetObject.get("Sheet");
         	 
         	 Row row = sheet.createRow(rowNum++);        	      	 
         	 int cellNum = 0;
         	 Map<String, Object> xmlData = new HashMap<String, Object>();
         	 
         	 for(Map.Entry<String, Object> dataEntry: data.entrySet())
         	 {           	
         		 if (!dataEntry.getKey().equalsIgnoreCase("parsedXML") &&
	    				 !dataEntry.getKey().equalsIgnoreCase("xmlrecords")
	    				 && !dataEntry.getKey().equalsIgnoreCase("detailedData")) 
         		 {          			 
         			 if (dataEntry.getValue() instanceof Integer)
	       			 {
         				 row.createCell(cellNum).setCellValue((Integer) dataEntry.getValue());	       				 
	       			 }
	       			 else  if (dataEntry.getValue() instanceof java.util.LinkedHashMap)
	       			 {	      
	       				 String value ="";
	       				 if(deepDataObject.containsKey(dataEntry.getKey()))
        				 {
        					 List<String> dataValueMap = (List<String>) deepDataObject.get(dataEntry.getKey());	        					 
    						 value = new MapUtil().getNestedValue((Map<String, Object>)dataEntry.getValue(), dataValueMap);
//        				
        				 }
        				 
	       				row.createCell(cellNum).setCellValue(value);
	       			 }
	       			 else
	       			 {
	       				 if (dataEntry.getKey().equalsIgnoreCase("createddate"))
	      				 { 	      						 
      						 Cell cell =  row.createCell(cellNum);
      	        			 cell.setCellStyle(dateCellStyle);
      	        			cell.setCellValue((String)dataEntry.getValue());
	      				 }
	       				 else
	       				 {
	       					 if(!dataEntry.getKey().equalsIgnoreCase("select")) {
	       						 Cell cell =  row.createCell(cellNum);
		        				 cell.setCellValue((String)dataEntry.getValue());
	       					 }
	       				 }
	       			 }	     	
  	        		
  	        		 cellNum++;
         		 } 
         		 if (dataEntry.getKey().equalsIgnoreCase("parsedXML")||
         				 dataEntry.getKey().equalsIgnoreCase("detailedData"))
      			 {      
         			xmlData = (Map<String, Object>)dataEntry.getValue();
      			 }
      			
        	 } 
         	 
         	 if (xmlData != null && xmlData.size() >0)
     		 {
         		 Map<String, Object> mapObject = copyXMLToExcel(xmlData ,sheet, rowNum++, tabHeaderCellStyle,
     					 gridHeaderCellStyle, headerCellStyle, columnList, workbook, sheetName, deepDataObject);
     			 sheet = (Sheet)mapObject.get("Sheet");
     			 rowNum = (int)mapObject.get("RowNumber");
     		 }           	 
         }

  		// Resize all columns to fit the content size
          for(int i = 0; i < columnList.size(); i++) 
          {
	         	for (int j=0; j< workbook.getNumberOfSheets() ;j++)
	         	{
	         		workbook.getSheetAt(j).autoSizeColumn(i);
	         	}
          }  

    }
    
    /**
     * 
     * @param workbook  [Workbook] object to be created
     * @param sheet  [Sheet] Object of the workbook
     * @param rowNum  [int]  row number in the sheet
     * @param sheetName [String] Name of the sheet
     * @param headerCellStyle [CellStyle] Cell style for header content
     * @param columnList [List] header column list
     * @return  map object with sheet and the completed row number in the sheet
     */
    private Map<String, Object> validateSheetRow(Workbook workbook, Sheet sheet, int rowNum,
    		String sheetName, CellStyle headerCellStyle, final List<String> columnList)
    {
    	Map<String, Object> mapObject = new HashMap<String, Object>();
    	if (sheet.getPhysicalNumberOfRows() >= EXCEL_ROW_MAX)
    	{
    		final int sheetNo = workbook.getNumberOfSheets() +1;
    		sheet = workbook.createSheet(sheetName.concat("_"+sheetNo));
    		rowNum = 1;
    		createHeaderRow(sheet, headerCellStyle, columnList);
    		mapObject.put("Sheet",  sheet);
    		mapObject.put("RowNumber", rowNum);
    	}
    	else
    	{
    		mapObject.put("Sheet",  sheet);
    		mapObject.put("RowNumber", rowNum);
    	}
    	return mapObject;
    }
    
    /**
     * 
     * @param sheet  [Sheet] Object of the workbook
     * @param headerCellStyle [CellStyle] Cell style for header content
     * @param columnList [List] header column list
     */
    private void createHeaderRow(Sheet sheet, CellStyle headerCellStyle, List<String> columnList)
	{   	  
		// Create a Row
		Row headerRow = sheet.createRow(0);  
		headerRow.setHeightInPoints((2 * sheet.getDefaultRowHeightInPoints()));
		headerRow.setRowStyle(headerCellStyle);
		// Create cells
		for(int i = 0; i < columnList.size(); i++) {
			if (!columnList.get(i).equalsIgnoreCase("parsedXML")
					&& !columnList.get(i).equalsIgnoreCase("detailedData")) {
		        Cell cell = headerRow.createCell(i);
		        cell.setCellValue(columnList.get(i).toUpperCase());
		        cell.setCellStyle(headerCellStyle);
			}
		}
	} 	
    
    /**
     * This method is used to organize the xml formatted data or the map object of detailed content to a new row.
     * @param dataMap [Map] object with content
     * @param sheet [Sheet] Object of the workbook
     * @param rowNum [int]  row number in the sheet
     * @param tabHeaderCellStyle [CellStyle] Cell Style of detaileddata / xmlrecord content
     * @param gridHeaderCellStyle [CellStyle] Cell style for grid content
     * @param headerCellStyle [CellStyle] Cell style for header content
     * @param columnList [List] header column list
     * @param workbook [Workbook] object to be created
     * @param name [String] Name of the sheet
     * @param deepDataObject  [Map] object containing 'values to access deeply nested map object
     * @return map object with sheet and the completed row number in the sheet
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	private Map<String,Object> copyXMLToExcel(Map<String, Object> dataMap, Sheet sheet, int rowNum,
	    		CellStyle tabHeaderCellStyle, CellStyle gridHeaderCellStyle,
	    		CellStyle headerCellStyle, List<String> columnList, Workbook workbook,
	    		String name, Map<String, Object> deepDataObject)
    {    	
    	 int rowNumber = rowNum;    	
    
    	 for (Map.Entry<String,Object> entry : dataMap.entrySet()) 
    	 {  
    		 Map<String, Object> sheetObject = validateSheetRow(workbook, sheet, rowNumber, name, 
         			 headerCellStyle, columnList);
        	 rowNumber = (int) sheetObject.get("RowNumber");
        	 sheet = (Sheet) sheetObject.get("Sheet");
        	 
			 Row row = sheet.createRow(rowNumber);	
			 Cell cell =  row.createCell(1);
			 cell.setCellStyle(tabHeaderCellStyle);
			 cell.setCellValue(entry.getKey().toUpperCase());
		 		 
			 List<Map<String, Object>> mapList = (ArrayList)entry.getValue();
			 			 
			 int loopIndex = 1;
			 for (Map<String, Object> map : mapList) 
			 {
				 rowNumber++;

				 sheetObject = validateSheetRow(workbook, sheet, rowNumber, name, 
	         			 headerCellStyle, columnList);
	        	 rowNumber = (int) sheetObject.get("RowNumber");
	        	 sheet = (Sheet) sheetObject.get("Sheet");
				 row = sheet.createRow(rowNumber);	
				 int cellNum = 2; 		
				 if (loopIndex == 1)
				 {						 
					 for (String keyEntry : map.keySet()) {  
						 
						 cell =  row.createCell(cellNum);
						 cell.setCellValue(keyEntry.toUpperCase());
						 cell.setCellStyle(gridHeaderCellStyle);
						 cellNum++;
					 }
					 cellNum = 2;
					 rowNumber++;
						 
					 sheetObject = validateSheetRow(workbook, sheet, rowNumber, name, 
		         			 headerCellStyle, columnList);
		        	 rowNumber = (int) sheetObject.get("RowNumber");
		        	 sheet = (Sheet) sheetObject.get("Sheet");
		        	 
					 row = sheet.createRow(rowNumber);	
				  }
				 
     			 for (Map.Entry<String,Object> valueEntry : map.entrySet()) {  
			
					 cell =  row.createCell(cellNum);						
					 if (valueEntry.getValue() instanceof Integer)
	       			 {
						 cell.setCellValue((Integer) valueEntry.getValue());	       				 
	       			 }					 
					 else if (valueEntry.getValue() instanceof java.util.LinkedHashMap)
	       			 {
	       				 String value ="";

        				 if(deepDataObject.containsKey(valueEntry.getKey()))
        				 {
        					 List<String> dataValueMap = (List<String>) deepDataObject.get(valueEntry.getKey());	        					 
    						 value = new MapUtil().getNestedValue((Map<String, Object>)valueEntry.getValue(), dataValueMap);
       				
        				 }
        				 
	       				cell.setCellValue(value);
	       			 }
					 else
					 {
						 cell.setCellValue((String)valueEntry.getValue());
					 }					 
					 
					 cellNum++;
				 }
				 loopIndex++;
			 }
			 rowNumber++;			 	
			
    	 }  
    	 Map<String, Object> mapObject = new HashMap<String, Object>();
    	 mapObject.put("Sheet", sheet);
    	 mapObject.put("RowNumber",rowNumber);
    	 return mapObject;
    }

}
