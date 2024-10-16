package com.agaram.eln.primary.model.methodsetup;

import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class is used to apply sample split techniques for the raw data file of a method which is provided
 * as input to the Parser Setup.
 * It also holds method that is used to parse  the datablock to extract values from a single or multi valued
 * in Parser Setup.
 * @author ATE153
 * @version 1.0.0
 * @since  23- Apr- 2020
 */
public class CommonFunction {
	
	/**
	 * This method is used to apply techniques that are used to remove unwanted text or lines
	 * of code in a raw data file based on provided technique.
	 * If the technique is from SampleTextSplit, then the data between the 'begintext' and end text will be removed.
	 * While removing data whether to remove the begin text and the end text also is based on the 'excludebegintext'
	 * and 'excludeendtext' values. We can also repeat this technique within this file for any number of times
	 * if 'repeattext' is set to 1.
	 * If the technique is from SampleLineSplit, then the data above  and below the 'centertext' will be removed.
	 * While removing data whether to remove the centertext also is based on the 'excludecentertext'
	 * value. We can also repeat this technique within this file for any number of times
	 * if 'repeatlines' is set to 1. The number of lines to be deleted above or below is determined by 'toplines' and 'bottomlines'.
	 * @param rawData [String] string formatted raw data file content
	 * @param textSplitList [List] object holding SampleTextSplit entities to remove unwanted lines of 
	 * code between the chosen begin text and end text
	 * @param lineSplitList [List] object holding SampleLineSplit entities to remove unwanted lines of 
	 * code above and below the chosen center text
	 * @return text data after applying remove techniques
	 */
	public String removeRawData(final String rawData, final List<SampleTextSplit> textSplitList,
			final List<SampleLineSplit> lineSplitList) {
		
		final Map<Integer, String> rawDataMap = convertStringToMap(rawData);
	    final List<RemoveItem> removeItemList = new ArrayList<RemoveItem>();
	    textSplitList.forEach(item ->{
	    	
	    	 if (item.getRepeattext() == 1) {
	    		  int beginTextIndex = -1;
                  int endTextIndex = -1;
                  for (Map.Entry<Integer, String> mapItem: rawDataMap.entrySet()) {
                	  if (mapItem.getValue().contains(item.getBegintext())) {
                		  beginTextIndex = mapItem.getKey();
                	  }                	  
                	  if (mapItem.getValue().contains(item.getEndtext())) {
                		  endTextIndex = mapItem.getKey();
                	  }                	  

                      if (beginTextIndex != -1 && endTextIndex != -1){
                    	  int startIndex = item.getExcludebegintext() == 1 ? beginTextIndex+1 : beginTextIndex;
                          int endIndex = item.getExcludeendtext() == 1 ? endTextIndex-1 : endTextIndex;    
                          removeItemList.add(new RemoveItem(-999, startIndex,endIndex));

                          beginTextIndex = -1;
                          endTextIndex = -1;                                               
                      }                      
                  }
	    	 }
	    	 else {              
                 int beginTextIndex = -1;
                 int endTextIndex = -1;
                 int beginTextOccurrenceNo = 0;
                 int endTextOccurrenceNo = 0;

                 for (Map.Entry<Integer, String> mapItem: rawDataMap.entrySet()) {
                	 if (mapItem.getValue().contains(item.getBegintext())) {                    
                         beginTextOccurrenceNo = beginTextOccurrenceNo+1;
                         if (beginTextOccurrenceNo == item.getBegintextoccurrenceno())
                             beginTextIndex = mapItem.getKey();
                     }
                	 if (mapItem.getValue().contains(item.getEndtext())){
                         endTextOccurrenceNo = endTextOccurrenceNo+1;
                         if (endTextOccurrenceNo == item.getEndtextoccurrenceno())
                             endTextIndex = mapItem.getKey();
                     }
                     if ( beginTextIndex !=-1 && endTextIndex != -1){
                         int startIndex = item.getExcludebegintext() == 1 ? beginTextIndex+1 : beginTextIndex;
                         int endIndex = item.getExcludeendtext() == 1 ? endTextIndex-1 : endTextIndex;  
                         
                         removeItemList.add(new RemoveItem(-999, startIndex,endIndex));
                         beginTextIndex = -1;
                         endTextIndex = -1;
                         break;    
                     }
                 }      
             }
	    	
	    });
	    
	    lineSplitList.forEach(item ->{
	    	 if (item.getRepeatlines() == 1) {
	    		   for (Map.Entry<Integer, String> mapItem: rawDataMap.entrySet()) {
	                	  if (mapItem.getValue().contains(item.getCentertext())) {
	                		  int centerTextIndex =  item.getExcludecentertext () ==1 ? mapItem.getKey() : -999;
	                          int startIndex =  mapItem.getKey() - item.getToplines();
	                          int endIndex = mapItem.getKey() + item.getBottomlines();
	                          if (startIndex >= 0 && endIndex < rawDataMap.size()) {
	                        	  removeItemList.add(new RemoveItem(centerTextIndex, startIndex,endIndex));
	                          }
	                          else {
	                        	  removeItemList.add(new RemoveItem(centerTextIndex, startIndex,rawDataMap.size()-2));
	                          }
                        }                         
	    		   }
	    	 }
	    	 else {
                 int centerTextOccurrenceNo = 0;
                 for (Map.Entry<Integer, String> mapItem: rawDataMap.entrySet()) {
                     if (mapItem.getValue().contains(item.getCentertext())){                        
                         centerTextOccurrenceNo = centerTextOccurrenceNo+1;
                         if (centerTextOccurrenceNo == item.getCentertextoccurrenceno()){
                             int centerTextIndex =  item.getExcludecentertext () ==1 ? mapItem.getKey() : -999;                 
                             int startIndex = mapItem.getKey() - item.getToplines();
                             int endIndex = mapItem.getKey() + item.getBottomlines();
                             if (startIndex >= 0 && endIndex < rawDataMap.size() ) {
                                 removeItemList.add(new RemoveItem(centerTextIndex, startIndex,endIndex));
                                 break;
                             }
                             else {
                                 break;
                             }
                         }                        
                     }
                 }                     
             }
	    });
	  
	    removeItemList.forEach(item -> {
	        for(int i = item.getStartIndex(); i<= item.getEndIndex(); i++) {           
	            if (i != item.getExcludeIndex()) 
	                rawDataMap.remove(i);
	        }
	    });
	    
	    return rawDataMap.values().stream().map(Object::toString).collect(Collectors.joining("\n"));
	}
	
	/**
	 * This method is used to extract essential data using SampleTextSplit entity.
	 * If SampleTextSplit.removeorextracttext = 3 it is used to extract for header data
	 * If SampleTextSplit.removeorextracttext = 2 it is used to extract for footer data
	 * If SampleTextSplit.removeorextracttext = 1 it is used to extract for block data
	 * @param rawData [String] content for which the extract techniques are to be applied
	 * @param sampleTextSplit [SampleTextSplit] object which provides technique to extract essential data as blocks
	 * @param originalFileData [String] original file data to be used for header, footer extract
	 * @return data extracted through SampleTextSplit technique
	 */
	public String extractRawDataBySTS(final String rawData, final SampleTextSplit sampleTextSplit,
			final String originalFileData) {
		
		String dataString = "";
	    if(sampleTextSplit.getRemoveorextracttext() == 1) {	     
	        dataString =  rawData;
	    }
	    else{
	        dataString =  originalFileData;
	    }
		    
		final Map<Integer, String> rawDataMap = convertStringToMap(dataString);
		
	    final List<String> extractBlock = new ArrayList<String>();
		 
        int beginTextIndex = -1;
        int endTextIndex = -1;
        int beginTextOccurrenceNo = 0;
        int endTextOccurrenceNo = 0;

        for (Map.Entry<Integer, String> mapItem: rawDataMap.entrySet()) {
            if (mapItem.getValue().contains(sampleTextSplit.getBegintext())){                        
                beginTextOccurrenceNo = beginTextOccurrenceNo+1;
                
                if (beginTextOccurrenceNo ==  sampleTextSplit.getBegintextoccurrenceno())
                    beginTextIndex = mapItem.getKey();
            }
            if (mapItem.getValue().contains(sampleTextSplit.getEndtext())){
                endTextOccurrenceNo = endTextOccurrenceNo+1;
                
                if (endTextOccurrenceNo == sampleTextSplit.getEndtextoccurrenceno())
                    endTextIndex = mapItem.getKey();
            }

            if (beginTextIndex !=-1 && endTextIndex != -1){
                int startIndex = sampleTextSplit.getExcludebegintext() == 1 ? beginTextIndex+1 : beginTextIndex;
                int endIndex = sampleTextSplit.getExcludeendtext() == 1 ? endTextIndex-1 : endTextIndex;  
                for(int i = startIndex; i <= endIndex; i++){                   
                    extractBlock.add(rawDataMap.get(i));
                }
                beginTextIndex = -1;
                endTextIndex = -1;
                break;    
	            }
	        }     
		         
        return extractBlock.stream().map(Object::toString).collect(Collectors.joining("\n"));
	}
	
	/**
	 * This method is used to extract essential data using SampleLineSplit entity.
	 * If SampleLineSplit.removeorextractlines = 3 it is used to extract for header data
	 * If SampleLineSplit.removeorextractlines = 2 it is used to extract for footer data
	 * If SampleLineSplit.removeorextractlines = 1 it is used to extract for block data
	 * @param rawData [String] content for which the extract techniques are to be applied
	 * @param sampleLineSplit [SampleLineSplit] object which provides technique to extract essential data as blocks
	 * @param originalFileData [String] original file data to be used for header, footer extract
	 * @return data extracted through SampleLineSplit technique
	 */
	public String extractRawDataBySLS(final String rawData, final SampleLineSplit sampleLineSplit,
			final String originalFileData) {
		
		String dataString = "";
	    if(sampleLineSplit.getRemoveorextractlines() == 1) {	     
	        dataString =  rawData;
	    }
	    else{
	        dataString =  originalFileData;
	    }
	    
		final Map<Integer, String> rawDataMap = convertStringToMap(dataString);
		
	    final List<String> extractBlock = new ArrayList<String>();
		  
		int centerTextOccurrenceNo = 0;
		for (Map.Entry<Integer, String> mapItem: rawDataMap.entrySet()) {
            if (mapItem.getValue().contains(sampleLineSplit.getCentertext())){                        
                centerTextOccurrenceNo = centerTextOccurrenceNo+1;
                if (centerTextOccurrenceNo == sampleLineSplit.getCentertextoccurrenceno()){                   
                    int startIndex = mapItem.getKey() - sampleLineSplit.getToplines();
                    int endIndex = mapItem.getKey() + sampleLineSplit.getBottomlines();
                    if (startIndex >= 0 && endIndex < rawDataMap.size()) {
                        for(int i = startIndex; i <= endIndex; i++){
                            if ((i != mapItem.getKey()) || (i == mapItem.getKey() && sampleLineSplit.getExcludecentertext() != 1))
                                extractBlock.add(rawDataMap.get(i));
                        }
                        break;  
                    }
                    else {
                        break;
                    }
                }                        
            }
        }                     
		           
		return extractBlock.stream().map(Object::toString).collect(Collectors.joining("\n"));
	}
	
	/**
	 * This method is used to convert the string data to Map object based on new line splitting.
	 * @param rawData [String] data to convert
	 * @return map object of string content
	 */
	public Map<Integer, String> convertStringToMap(final String rawData){
		final String[] rawDataArray =  rawData.split("\n");
	    final Map<Integer, String> rawDataMap = new HashMap<Integer, String>();
	    int index = 0;
	    for(String data : Arrays.asList(rawDataArray))
	    {
	    	rawDataMap.put(index, data);
	    	index++;
	    }
	    return rawDataMap;
	}
	
	/**
	 * This method is used to extract samples from the extracted block based on the provided
	 * match text.
	 * @param processData [String] extracted block data to split into samples
	 * @param matchTextTechnique [SampleExtract] technique to extract samples
	 * @return list of extracted samples
	 */
	public List<String> applyMatchTextExtract (final String processData, final SampleExtract matchTextTechnique)  {
	    final String[] processDataArray =  processData.split("\n");  
	    final List<Integer> indexes = new ArrayList<Integer>();
	    for (int i = 0; i < processDataArray.length; i++) {
	        if (processDataArray[i].contains(matchTextTechnique.getMatchtext())) {
	            indexes.add(i);
	        }
	        if (i == (processDataArray.length - 2)) {
	            indexes.add(processDataArray.length-1);
	        }
	    }   
	    
	    final List<String> sampleArray = new ArrayList<String>();
	    for (int j = 0; j < indexes.size(); j++) {
	    	  if (j == indexes.size()-1)
		        	break;
	    
	    	String dat =  Arrays.asList(processDataArray)
        			.subList(indexes.get(j), indexes.get(j + 1))
        			.stream().map(Object::toString)
        			.collect(Collectors.joining("\n"));
	    	
	        sampleArray.add("$$BEGINSAMPLE$$\n"+dat+"\n$$ENDSAMPLE$$");
	      
	    }
	    return sampleArray;
	}
	
	/**
	 * This method is used to extract samples from the extracted block based on the provided absolute number of lines.
	 * @param processData [String] extracted block data to split into samples
	 * @param matchTextTechnique [SampleExtract] technique to extract samples
	 * @return list of extracted samples
	 */
	public List<String> applyAbsoluteLinesExtract (final String processData, final SampleExtract matchTextTechnique){
		final String[] processDataArray =  processData.split("\n");    
		final List<String> sampleArray = new ArrayList<String>();  
	    int noOfSet = processDataArray.length / matchTextTechnique.getAbsolutelines();  
	    int startIndex = 0;
	    for (int i = 0; i < noOfSet; i++) {
	    	final List<String> tempArray = new ArrayList<String>(); 
	        int endIndex = startIndex + matchTextTechnique.getAbsolutelines();
	        for(int j = startIndex; j < endIndex; j++)              
	            tempArray.add(processDataArray[j]);
	         sampleArray.add("$$BEGINSAMPLE$$\n"+tempArray.stream().map(Object::toString)
	        			.collect(Collectors.joining("\n"))+"\n$$ENDSAMPLE$$");   
	        startIndex = endIndex;
	    }  
	    return sampleArray ;
	}

	/**
	 * This method is used to replace new line, form feed and carriage return characters.
	 * @param stringData [String] content in which characters are to be replaced
	 * @return replaced content
	 */
//	private String replaceEOL(final String stringData) {		 
//		 return stringData
//		            .replace("\n","â†µâ†µ")
//		            .replace("\r", "â†µâ†µ")
//		            .replace("\f", "Ã¢â„¢â‚¬");
//	}
	
	private String replaceEOL(final String stringData) {	
		 return stringData
		            .replace("\n","↵")
		            .replace("\r", "↵")
		            .replace("\f", "♀");
	}
	/**
	 * This method is used to create list of characters with data on each row of input data.
	 * @param blockData [String] data to split into individual characters
	 * @return list of characters on each row.
	 */
	public List<List<String>> splittingRawdataAsArray (final String blockData){
	   
	    final List<List<String>> sampleSplitData = new ArrayList<List<String>>();
	   
	    final String[] splittedList = blockData.split("\\r?\\n");
	    for(String splittedData : splittedList) {
		   final String replacedStringwithNL = replaceEOL(splittedData + "\r\n");		  
		   String[] splittedCharArray = replacedStringwithNL.split(""); 
		   sampleSplitData.add(Arrays.asList(splittedCharArray));		  
	    }	
	 
	    return sampleSplitData;
	}
	
	/**
	 * This method is used to get the index position of identification text in the raw data block
	 * @param rawDataBlockArray [List] holding array of extracted block data
	 * @param idText [String] identification text for which index is to be found
	 * @param rows [int]
	 * @return index value of identification text in list
	 * @throws Exception 
	 */
	private int getIndex(List<String> rawDataBlockArray, final String idText, final int rows){
	//	public ResponseEntity<Object> getIndex(List<String> rawDataBlockArray, final String idText, final int rows ,final HttpServletRequest request)
			
			
			
		 String blockText = String.join("\n",rawDataBlockArray).trim();	
	
		// blockText= blockText.replaceAll("\"", "");
		final int index = blockText.indexOf(idText);
	
	//	String idTexts=idText.replaceAll(" ", "\t");

//		if (index == -1) {
//			//throw new Exception ("Wrong File");
//			return index;
//		}
//		
//		else {   
//		final String d = '"'+ blockText.substring(0, index) +'"';	
//
//	    final String[] d1 = d.split("\n");
//	    final int realIndex = d1.length-1; 
//		return realIndex;
//		}

		 final String d;
		 int realIndex=0;
		// if(blockText.indexOf(idTexts) > 0)
		//    {
			d = '"'+ blockText.substring(0, blockText.indexOf(idText)) +'"';	
		    final String[] d1 = d.trim().split("\n");
		    realIndex = d1.length-1;
		//    }
			return realIndex;
		  
		   
	}

	/**
	 * This method is used to parse the selected data block to extract content.	 
	 * @param parsingTechniques [List] list of ParserTechniques to extract data block
	 * @param blockData [String] data on which parser techniques are to be applied
	 * @param delimiter [String] delimiter string based on which  the data block is to be splitted
	 * @param ignoreList [List] list  of characters from ParserIgnoreChars to be ignored in parsed data
	 * @return list of sample data
	 * @throws Exception 
	 */
	public List<List<String>> getMvfData (final List<ParserTechnique> parsingTechniques, final String blockData,

			final String delimiter, final List<ParserIgnoreChars> ignoreList){
		final List<List<String>> splittedDataArray = splittingRawdataAsArray(blockData);
		

	    List<List<String>> indexfailure = new ArrayList<List<String>>() ;
    	final List<List<String>> newstrList = new ArrayList<List<String>>();        	

    	//final List<List<String>> strList1 = new ArrayList<List<String>>();        	
    	//strList.add(String.join("", Arrays.asList(rawDataBlockArray.get(row).split("")).subList(dataStartIndex, dataEndIndex)).trim());
    //	Collection<? extends List<java.lang.String>> mm = null;
   // 	newstrList.addAll(mm);
    	indexfailure = newstrList;      

//    	rowData.length();
//		splittedrowData.length;

		List<String> rawDataBlockArray = new ArrayList<String>();

		for (List<String> strList :splittedDataArray){
			final String rowData = String.join("", strList);
			rawDataBlockArray.add(rowData);
	
		}	
	
	    List<List<String>> dataBlock = new ArrayList<List<String>>() ;
	    if (parsingTechniques.size() > 0) {

	    int actualRowEnd = 0;
	    int realRowEnd = 0;	 
        int actualColStart = parsingTechniques.get(0).getCol();
        int actualColEnd = parsingTechniques.get(0).getCol() + parsingTechniques.get(0).getCols();
        int dataBlockRowStart = parsingTechniques.get(0).getRow();      
	    int actualRowStart = parsingTechniques.get(1).getRow();	   
	    int realRowStart   = getIndex(rawDataBlockArray, parsingTechniques.get(1).getIdentificationtext(), parsingTechniques.get(0).getRows());
	    
	    int col;
	    int cols;
	
	    if (parsingTechniques.get(2).getRow() == actualRowStart) {
            actualRowEnd = parsingTechniques.get(2).getRow();
            realRowEnd = realRowStart;
        } 
	    else {
            actualRowEnd = parsingTechniques.get(2).getRow();
            realRowEnd = getIndex(rawDataBlockArray, parsingTechniques.get(2).getIdentificationtext(), parsingTechniques.get(0).getRows());
         //   realRowEnd = parsingTechniques.get(2).getRow();
        }
	                                                                                                                        
    
        if (parsingTechniques.size() == 4 && parsingTechniques.get(3) != null) {
            if (actualRowStart == realRowStart) {
                actualColEnd = rawDataBlockArray.get(dataBlockRowStart)
                		 .indexOf(parsingTechniques.get(3).getIdentificationtext());
//            	actualColEnd = actualColEnd;
            }
            else if (realRowStart > actualRowStart) {
                int diffStartRow = realRowStart - actualRowStart;
                actualColEnd = rawDataBlockArray.get(dataBlockRowStart + diffStartRow)
                		.indexOf(parsingTechniques.get(3).getIdentificationtext());
            }
            //newly added scinario
//            else{
////           	 int diffStartRow = actualRowStart - realRowStart ;
////                actualColEnd = rawDataBlockArray.get(dataBlockRowStart + diffStartRow)
////                		.indexOf(parsingTechniques.get(3).getIdentificationtext());
//                actualColEnd =col+cols;
//           }
        }
   
        int row = parsingTechniques.get(0).getRow();   
        int rows = parsingTechniques.get(0).getRows();
	           
        
        
         if (actualRowStart == realRowStart && actualRowEnd == realRowEnd && realRowStart == realRowEnd)
         {
        	//Single value field
        	final int dataStartIndex = rawDataBlockArray.get(row).indexOf(parsingTechniques.get(1).getIdentificationtext().trim())
        								+ parsingTechniques.get(1).getIdentificationtext().length();	        	
        	final int dataEndIndex = rawDataBlockArray.get(row).indexOf(parsingTechniques.get(2).getIdentificationtext().trim());
        	
        	final List<String> strList = new ArrayList<String>();
        	final List<List<String>> strList1 = new ArrayList<List<String>>();        	
        	strList.add(String.join("", Arrays.asList(rawDataBlockArray.get(row).split("")).subList(dataStartIndex, dataEndIndex)).trim());
           	strList1.add(strList);
        	dataBlock = strList1;      
    	
         } 
         else 
         {
        	//all cases of MutiValue field and 1 single value field with different row start and row end
        	
            if (actualRowStart == realRowStart && actualRowEnd == realRowEnd) {	
            	     	
                dataBlock = getDataBlock(rawDataBlockArray, row, row+rows, actualColStart, actualColEnd, 
                		delimiter, parsingTechniques, ignoreList);
            } 
            //multiple fields ---- newly added scinario
            else if (actualRowStart > realRowStart && actualRowEnd == realRowEnd) {
            	dataBlock = getDataBlock(rawDataBlockArray, row, row+rows, actualColStart, actualColEnd, 
                		delimiter, parsingTechniques, ignoreList);
            }
            
            else if (actualRowStart == realRowStart) {
                        	
            	if (actualRowEnd >= realRowEnd - (actualRowEnd - (row+rows))) {
            		dataBlock = getDataBlock(rawDataBlockArray, row, row+(realRowEnd-row), 
            				actualColStart, actualColEnd, delimiter, parsingTechniques, ignoreList); 
            	}
            	else {
            		dataBlock = getDataBlock(rawDataBlockArray, row, realRowEnd,
            					actualColStart, actualColEnd, delimiter, parsingTechniques, ignoreList); 
            	}
                
            }
            else 
            {
            	   	            	
                dataBlock = getDataBlock(rawDataBlockArray, realRowStart + (row - actualRowStart), 
                			realRowEnd - (actualRowEnd - (row + rows)),                		
                		actualColStart, actualColEnd, delimiter, parsingTechniques, ignoreList); 
            }
	       }
	      }
	    else {
	    	return indexfailure;
	       }
	 //   }
	    return dataBlock;
	}
	
	
	/**
	 * This method is used to parse data from the data block in cases where there has no end column match.
	 * @param rawDataBlockArray [List] of rows of the data block
	 * @param row [int] defining row start index
	 * @param rows [int] defining row end index
	 * @param actualColStart [int] defining the column start index of the data
	 * @param actualColEnd [int] defining the column end index of the data
	 * @param delimiter [String] based on which the field data is to be splitted
	 * @param ignoreList [List] holding ParserIgnoreChars entity list that will be ignored in parsed data
	 * @return nested list of string data containing parsed data
	 */
	
	private  List<List<String>> getDataBlock (List<String> rawDataBlockArray, final int row, final int rows, 
			 final int actualColStart, final int actualColEnd, final String delimiter, 
			 final List<ParserTechnique> parsingTechniques,	 final List<ParserIgnoreChars> ignoreList){
		 
		 List<List<String>> outputList = new ArrayList<List<String>>();
		 List<List<String>> finaloutputList = new ArrayList<List<String>>();
		
		 int dataStartIndex = 0;
		 if (parsingTechniques.get(0).getRow() == parsingTechniques.get(1).getRow()) {
	 
			 dataStartIndex = rawDataBlockArray.get(row).indexOf(parsingTechniques.get(1).getIdentificationtext().trim())
						+ parsingTechniques.get(1).getIdentificationtext().length();
		 }
		 else {
			 dataStartIndex = actualColStart;
		 }

		 
			 
		 for (String data:  rawDataBlockArray.subList(row, rows)) {		
		 
		     int endIndex = actualColEnd <= data.length() ?actualColEnd :data.length();
		 
			 if (dataStartIndex<=endIndex) {
				
			 String extractedString = data.substring(dataStartIndex, endIndex);
			 System.out.println("getdatablock-extractedString:"+extractedString);
			 
			 for (ParserIgnoreChars ignoreCharacter :ignoreList) {
				 extractedString = extractedString.replace(ignoreCharacter.getIgnorechars(), "");
				 extractedString = extractedString.trim();
			 }
			 String trimmedData = extractedString.trim();
			 System.out.println("getdatablock-trimmeddata:"+trimmedData);
			 if (trimmedData.length() > 0) {
				 
				 if(delimiter.equalsIgnoreCase("None")) {
					 System.out.println("delimiter: none");
					 List<String> List=Arrays.asList(trimmedData);
					 System.out.println("List:"+ List);
					 finaloutputList.add(List);
					 
					 System.out.println("getdatablock-finaloutputList:"+finaloutputList);
					 outputList = finaloutputList;
					 System.out.println("getdatablock-outputList:"+outputList);
					
				 }else if (delimiter.equalsIgnoreCase(",")) {        //adding Empty string due to trailing comma used only for comma delimiter
					    String[] str = trimmedData.split(delimiter , -1);
						System.out.println("getdatablock-str:"+str);
						outputList.add(Arrays.asList(str));	 
				 }
				 else {
					 System.out.println("delimiter:"+delimiter);
					 System.out.println("splitteddata:"+trimmedData.split(delimiter));
					String[] str = trimmedData.split(delimiter!= "None" ? delimiter: "ChummaKizhi");
					System.out.println("getdatablock-str:"+str);
					outputList.add(Arrays.asList(str));	 
				 }
			 }	
		   }
		 }
		 System.out.println("getdatablock-outputList:"+outputList);
		 return outputList;
    }
	
	
	
	    // Generic function to add elements of a Stream into an existing list
    private static<T> void addToList(List<T> target, Stream<T> source)
    {
        source.collect(Collectors.toCollection(() -> target));
    }	
	
	/**
	 * This method is used to apply 'Merge' sub parsing technique to the data block
	 * @param dataBlock [List] nested list of string data on which technique is to be applied
	 * @param row [int] 0
	 * @param col [int] 0
	 * @param sheetIndex [int] 0
	 * @param currentKey [int] 0
	 * @param subParserTechnique [SubParserTechnique] holding field details and delimiter to apply merge
	 * @param variableRC [String] column, row number of sub parser field position
	 * @return nested list of string data after  applying technique
	 */
 	
    public List<List<String>> mergeFields (final List<List<String>> dataBlock, final SubParserTechnique subParserTechnique)
	{

		final String delimiterChar = subParserTechnique.getMethoddelimiter().getDelimiter().getActualdelimiter();
		List<List<String>> dataBlockWithMergedFields = new ArrayList<List<String>>();
		
		final List<Integer> rowColumnCount =  dataBlock.stream().map((item) -> item.size()).collect(Collectors.toList());
		Integer max = rowColumnCount.stream().max(Integer::compare).get();
		
		for(List<String> rowValues : dataBlock) {
			
			List<String> currentRowValues = new ArrayList<>(rowValues);
			StringJoiner mergeFields = new StringJoiner(delimiterChar);

			for (String extractColumnIndex : subParserTechnique.getInputfields().split(",")) {
				mergeFields.add(rowValues.get(Integer.parseInt(extractColumnIndex)).toString());
	        }

			if (rowValues.size() != max) {
	            for (int i=rowValues.size(); i<max; i++) {
	            	addToList(currentRowValues, Stream.of(" "));
	            }
	        };
	        addToList(currentRowValues, Stream.of(mergeFields.toString()));
	        dataBlockWithMergedFields.add(currentRowValues);
		}

		return dataBlockWithMergedFields;

	}

	
	
	/**
	 * This method is used to apply 'Split' sub parsing technique to the data block
	 * @param dataBlock [List] nested list of string data on which technique is to be applied
	 * @param row [int] 0
	 * @param col [int] 0
	 * @param sheetIndex [int] 0
	 * @param currentKey [int] 0
	 * @param subParserTechnique [SubParserTechnique] holding field details and delimiter to apply Split
	 * @param variableRC [String] column, row number of sub parser field position
	 * @return nested list of string data after  applying technique
	 */
 
    public List<List<String>> splitField (final List<List<String>> dataBlock, 
			final SubParserTechnique subParserTechnique){
	    
		final String delimiterChar = subParserTechnique.getMethoddelimiter().getDelimiter().getActualdelimiter();
		List<List<String>> dataBlockWithSplittedFields = new ArrayList<List<String>>();
		
		final List<Integer> rowColumnCount =  dataBlock.stream().map((item) -> item.size()).collect(Collectors.toList());
		Integer max = rowColumnCount.stream().max(Integer::compare).get();
		
		List<String> extractColumn = dataBlock.stream().map(item -> item.get(Integer.parseInt(subParserTechnique.getInputfields()))).collect(Collectors.toList());
		List<String[]> splitFieldArray = extractColumn.stream().map((item) -> item.split(delimiterChar)).collect(Collectors.toList());
		
		int idx=0;
		for(List<String> rowValues : dataBlock) {
			
			List<String> splitFields = Arrays.asList(splitFieldArray.get(idx));
			List<String> currentRowValues = new ArrayList<>(rowValues);
			
			if (rowValues.size() != max) {
	            for (int i=rowValues.size(); i<max; i++) {
	            	addToList(currentRowValues, Stream.of(" "));
	            }
	        };
	        splitFields.forEach((item) -> addToList(currentRowValues, Stream.of(item)));
			idx++;
			dataBlockWithSplittedFields.add(currentRowValues);
		}

		return dataBlockWithSplittedFields;
	}
  
  
	
	/**
	 * This method is used to apply 'Shift' sub parsing technique to the data block
	 * @param dataBlock nested list of string data on which technique is to be applied
	 * @param row [int] 0
	 * @param col [int] 0
	 * @param sheetIndex [int] 0
	 * @param shiftField [String] holding forward and reverse columns of the field to be shifted
	 * @param variableType [String] col/cell
	 * @param variableName [String] sub parser field name
	 * @param variableRC [String] column, row number of sub parser field position
	 * @param previewMode boolean value
	 * @return nested list of string data after  applying technique
	 */
	public List<List<String>> shiftFieldParserFunction (final List<List<String>> dataBlock, final int row, final int col, 
			final int sheetIndex, final String shiftField, final String variableType, 
			final String variableName, final int variableRC, final boolean previewMode) {
	   
		List<List<String>> dataToBind = new ArrayList<List<String>>();
	
	    List<Integer> columnArray = generateShiftFieldColumns(shiftField);
	
	    for(List<String> rowData : dataBlock) {
	    
	    	 List<String> tempLine = rowData;
		     List<String> reOrderedArray = new ArrayList<String>();
		     int ncount = 0;
		     int nNegaCount = 0;
		     
		     for (int i = 0; i <= columnArray.size(); i++) {		    
		         String value = "";
		        
		        if (i == columnArray.size()) {
	                if (nNegaCount == 0) {
	                    value = String.join(" ", tempLine.subList(ncount, rowData.size())).trim();
	                } else {
	                    value = String.join(" ",
	                    		   tempLine.subList(ncount, rowData.size() - Math.abs(ncount - Math.abs(nNegaCount))))
	                    		.trim();
	                }
	            }
		        else {
	                int seq = columnArray.get(i);
	                if (seq >= 0) {
	                    ncount++;
	                    value = tempLine.get(seq);
	                } else {
	                    if (seq < nNegaCount) {
	                        nNegaCount = seq;
	                    }
	                    value = tempLine.get(tempLine.size() + ncount - Math.abs(seq));
	                }
	            }
		        if (previewMode) {
	                reOrderedArray.add(value);
	            } else {
	                if (i == variableRC) {
	                    reOrderedArray.add(value);
	                }
	            }
		        
		     }
		     dataToBind.add(reOrderedArray);	    	
	    }	  
	    return dataToBind;
	}
	
	/**
	 * This method generates list of integer from the forward and reverse columns defined for the
	 * field to be shifted
	 * @param shiftField [String] holding forward and reverse columns of the field to be shifted
	 * @return list of integers holding forward and reverse column numbers
	 */
	private List<Integer> generateShiftFieldColumns(final String shiftField) {
	    final String[] shiftArray = shiftField.split(",");
	    List<Integer> columnList = new ArrayList<Integer>() ; 
	    String[] columnArray = shiftArray[0].split(":");
	    for (int i = Integer.parseInt(columnArray[0]); i <= Integer.parseInt(columnArray[1]); i++) {
	        columnList.add(i);
	    }
	    columnArray = shiftArray[1].split(":");
	    for (int i = Integer.parseInt(columnArray[0]); i <= Integer.parseInt(columnArray[1]); i++) {
	        columnList.add(i);
	    }    
	    return columnList;
	}

	
	
}
