package com.agaram.eln.primary.commonfunction;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.Timer;
import java.util.stream.IntStream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.agaram.eln.config.AESEncryption;
import com.agaram.eln.primary.model.instrumentDetails.LSlogilablimsorderdetail;

public class commonfunction {

	public static boolean checkuseronmanualaudit(String myKey, String auditPassword) {

		String encryptedpassword = AESEncryption.decrypt(myKey);

		String getPasscode = AESEncryption.decrypt(encryptedpassword.split("_", 0)[0]);

		if (auditPassword.equals(getPasscode)) {
			return true;
		}

		return false;
	}

	public static String getJSONFieldsoninventory(String jsonFieldstring) {

		Map<String, Object> rMap = new HashMap<>();
		String jsonString = "";
		jsonString = jsonFieldstring;
		String jsonReturnString = "";
		try {

			JSONObject jsonObject = new JSONObject(jsonString);
			JSONArray jsonArrayDynamic = jsonObject.getJSONArray("dynamicfields");
			JSONArray jsonArrayStatic = jsonObject.getJSONArray("staticfields");

			List<Map<String, Object>> lstfields = new ArrayList<>();

			jsonArrayStatic.forEach(rowitem -> {
				JSONObject rowobj = (JSONObject) rowitem;

				Map<String, Object> mapObj = new HashMap<>();

				mapObj.put("fieldname", rowobj.get("fieldname"));

				lstfields.add(mapObj);
			});

			jsonArrayDynamic.forEach(rowitem -> {
				JSONObject rowobj = (JSONObject) rowitem;

				Map<String, Object> mapObj = new HashMap<>();

				mapObj.put("fieldname", rowobj.get("fieldname"));
				mapObj.put("fieldkey", rowobj.get("fieldkey"));

				lstfields.add(mapObj);
			});

			rMap.put("inventoryFields", lstfields);

			JSONObject jsonObjectReturnString = new JSONObject();

			jsonObjectReturnString.put("inventoryFields", lstfields);

			jsonReturnString = jsonObjectReturnString.toString();

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return jsonReturnString;
	}

	public static String getServerDateFormat() throws ParseException {

		Date newDate = new SimpleDateFormat("yyyy/dd/MM hh:mm:ss").parse("4444/31/12 23:58:57");

		Locale locale = Locale.getDefault();
		DateFormat datetimeFormatter = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, locale);

		String dateSString = datetimeFormatter.format(newDate);
		dateSString = dateSString.replaceAll("31", "dd");
		dateSString = dateSString.replaceAll("12", "MM");
		dateSString = dateSString.replaceAll("Dec", "MMM");
		dateSString = dateSString.replaceAll("4444", "yyyy");
		dateSString = dateSString.replaceAll("44", "yy");
		dateSString = dateSString.replaceAll("11", "hh");
		dateSString = dateSString.replaceAll("23", "hh");
		dateSString = dateSString.replaceAll("58", "mm");
		dateSString = dateSString.replaceAll("57", "ss");
		dateSString = dateSString.replaceAll(" AM", "");
		dateSString = dateSString.replaceAll(" PM", "");

		dateSString = "MM-dd-yyyy hh:mm:ss";

		return dateSString;
	}

	public static String getMIMEtypeonextension(String extension) {
		String mediatype = "image/jpeg";
		switch (extension) {
		case "jpg":
			mediatype = "image/jpeg";
			break;
		case "docx":
			mediatype = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
			break;
		case "mp3":
			mediatype = "audio/mp3";
			break;
		case "pdf":
			mediatype = "application/pdf";
			break;

		default:
			mediatype = "image/jpeg";
		}

		return mediatype;
	}

	public static boolean isSameDay(Date date1, Date date2) {
		SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
		return fmt.format(date1).equals(fmt.format(date2));
	}

	public static String getsheetdatawithfirstsheet(String jsonString) {
//		try {
//			// JSONParser jsonParser = new JSONParser();
//			JSONObject jsonObject = new JSONObject(jsonString);
//			JSONArray jsonArray = jsonObject.getJSONArray("sheets");
//		
//			jsonObject.put("sheets", new JSONArray("["+jsonArray.get(0).toString()+"]"));
//
//			jsonString = jsonObject.toString();
//
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
		return jsonString;
	}

	public static Map<String, Object> getInventoryValuesFromJsonString(String jsonString, String objectKey) {

		Map<String, Object> rtnMapObj = new HashMap<String, Object>();

		JSONObject jsonObject = new JSONObject(jsonString);

		if (jsonObject.has(objectKey)) {
			rtnMapObj.put("rtnObj", jsonObject.get(objectKey));
		}

		return rtnMapObj;
	}

	public static String getStoargeFromIdJsonString(String jsonString, String objectKey) {
		
		try {
            JSONArray jsonArray = new JSONArray(jsonString);

            IntStream.range(0, jsonArray.length())
                .mapToObj(jsonArray::getJSONObject)
                .forEach(jsonObject -> {
                    String id = jsonObject.getString("id");
                    
                    if(id.equalsIgnoreCase(objectKey)) {
                    	jsonObject.remove("storageseted");
                    }

                    JSONArray itemsArray = jsonObject.optJSONArray("items");
                    if (itemsArray != null) {
                        IntStream.range(0, itemsArray.length())
                            .mapToObj(itemsArray::getJSONObject)
                            .forEach(itemObject -> {
                                String itemId = itemObject.getString("id");
                                if(itemId.equalsIgnoreCase(objectKey)) {
                                	itemObject.remove("storageseted");
                                }
                            });
                    }
                });
            
            return jsonArray.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
	}
	
	private static String findPathRecursive(JSONObject node, String targetId, String currentPath) {
        if (node.getString("id").equals(targetId)) {
            return currentPath + node.getString("text");
        }

        if (node.has("items")) {
            JSONArray items = node.getJSONArray("items");
            for (int i = 0; i < items.length(); i++) {
                JSONObject childNode = items.getJSONObject(i);
                String path = findPathRecursive(childNode, targetId, currentPath + node.getString("text") + " -> ");
                if (!path.isEmpty()) {
                    return path;
                }
            }
        }

        return "";
    }
	
	public static String findPath(String node, String targetId) {
		JSONArray jsonArray = new JSONArray(node);
	    for (int i = 0; i < jsonArray.length(); i++) {
	        JSONObject jsonObject = jsonArray.getJSONObject(i);
	        String path = findPathCall(jsonObject, targetId);
	        if (!path.isEmpty()) {
	        	return path;
	        }
	    }
		return "";
    }
	
	public static String findPathCall(JSONObject root, String targetId) {
        return findPathRecursive(root, targetId, "");
    }
	
	public static Map<String, Object> getParamsAndValues(String jsonString) {

		Map<String, Object> rtnMapObj = new HashMap<String, Object>();

		try {

			List<String> lstValues = new ArrayList<String>();
			List<String> lstParams = new ArrayList<String>();

			JSONObject jsonObject = new JSONObject(jsonString);
			JSONArray jsonArray = jsonObject.getJSONArray("sheets");
			jsonArray.forEach(rowitem -> {

				JSONObject rowobj = (JSONObject) rowitem;
				JSONArray jsonRowArray = rowobj.getJSONArray("rows");

				jsonRowArray.forEach(cellitem -> {

					JSONObject cellObj = (JSONObject) cellitem;
					JSONArray jsonCellArray = cellObj.getJSONArray("cells");
					jsonCellArray.forEach(objCell -> {

						JSONObject objCellValue = (JSONObject) objCell;

						if (objCellValue.has("value")) {
							if (!objCellValue.has("formula")) {
								try {
									Object value = objCellValue.get("value");
									if (value instanceof Integer) {
									    int valInt = (int) value;
									    String valStr = Integer.toString(valInt);
									    lstValues.add(valStr);
									    // handle integer value
									}else if (value instanceof Float) {
									    float valFloat = (float) value;
									    String valStr = Float.toString(valFloat);
									    lstValues.add(valStr);
									    // handle float value
									}  else if (value instanceof Double) {
									    double valDouble = (double) value;
									    String valStr = Double.toString(valDouble);
									    lstValues.add(valStr);
									    // handle double value
									}
									else {
									    String valStr = (String) value;
									    lstValues.add(valStr);
									    // handle string value
									}
//									String val = (String) objCellValue.get("value");
									
								} catch (Exception e) {
									System.out.println(e.getMessage());
								}
							}
						}

						if (objCellValue.has("tag")) {

							JSONObject jsonTagObject = new JSONObject(objCellValue.getString("tag"));

							if (jsonTagObject.has("ParameterName")) {
								try {
									String tag = jsonTagObject.getString("ParameterName");
									lstParams.add(tag);
								} catch (Exception e) {
									System.out.println(e.getMessage());
								}
							}
						}
					});
				});
			});

			String jsonValues = org.json.simple.JSONArray.toJSONString(lstValues);
			String jsonParams = org.json.simple.JSONArray.toJSONString(lstParams);

			rtnMapObj.put("values", jsonValues);
			rtnMapObj.put("parameters", jsonParams);

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return rtnMapObj;
	}

	public static List<String> getTagValues(String jsonString) {

		List<String> lstTags = new ArrayList<String>();

		try {

			JSONObject jsonObject = new JSONObject(jsonString);
			JSONArray jsonArray = jsonObject.getJSONArray("sheets");
			jsonArray.forEach(rowitem -> {

				JSONObject rowobj = (JSONObject) rowitem;
				JSONArray jsonRowArray = rowobj.getJSONArray("rows");

				jsonRowArray.forEach(cellitem -> {

					JSONObject cellObj = (JSONObject) cellitem;
					JSONArray jsonCellArray = cellObj.getJSONArray("cells");
					jsonCellArray.forEach(objCell -> {

						JSONObject objCellValue = (JSONObject) objCell;

						if (objCellValue.has("tag")) {

							String tag = objCellValue.getString("tag");

							lstTags.add(tag);
						}
					});
				});
			});

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return lstTags;
	}

	public static int getCurrentDateTimeOffset(final String stimezoneid) throws Exception {

		ZoneId zone = ZoneId.of(stimezoneid);
		LocalDateTime dt = LocalDateTime.now();
		ZonedDateTime zdt = dt.atZone(zone);
		ZoneOffset offset = zdt.getOffset();

		int offSet = offset.getTotalSeconds();

		return offSet;
	}

	public static int getCurrentDateTimeOffsetFromDate(String Date, final String stimezoneid) throws Exception {

		ZoneId zone = ZoneId.of(stimezoneid);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime dt = LocalDateTime.parse(Date, formatter);
		ZonedDateTime zdt = dt.atZone(zone);
		ZoneOffset offset = zdt.getOffset();

		int offSet = offset.getTotalSeconds();

		return offSet;
	}

	public static Object convertInputDateToUTCByZone(JSONObject jsonObj, final List<String> inputFieldList,
			final boolean returnAsString) throws Exception {
//		final ObjectMapper objMapper = new ObjectMapper();

		final DateTimeFormatter dbPattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		for (int i = 0; i < inputFieldList.size(); i++) {
			String stringDate = (String) jsonObj.get(inputFieldList.get(i));
			if (!stringDate.equals("")) {
				stringDate = LocalDateTime.parse(stringDate, dbPattern).format(dbPattern);
				jsonObj.put(inputFieldList.get(i), stringDate);
			}

		}

		return jsonObj;
	}
	
//	public static Date getCurrentUtcTime() throws ParseException {
//	    LocalDateTime utcDateTime = LocalDateTime.now(ZoneOffset.UTC);
//	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//	    String formattedDateTime = utcDateTime.format(formatter);
//
//	    System.out.print("Current UTC Date and Time: " + formattedDateTime);
//
//	    // Parse the formatted date-time string into a Date object
//	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//	    Date utcDate = sdf.parse(formattedDateTime);
//
//	    return utcDate;
//	}
	
	// create getCurrentUtcTime() method to get the current UTC time  
    public static Date getCurrentUtcTime() throws ParseException {  // handling ParseException  
        // create a thread-local instance of the SimpleDateFormat class
        ThreadLocal<SimpleDateFormat> sdf = ThreadLocal.withInitial(() -> {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            return dateFormat;
        });
        
        // parse the date using the thread-local sdf instance
        return sdf.get().parse(sdf.get().format(new Date()));
//        // create an instance of the SimpleDateFormat class  
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");  
//        // set UTC time zone by using SimpleDateFormat class  
//        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));  
//        //create another instance of the SimpleDateFormat class for local date format  
//        SimpleDateFormat ldf = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");  
//        // declare and initialize a date variable which we return to the main method  
//        Date d1 = null;  
//        // use try catch block to parse date in UTC time zone  
//        try {  
//            // parsing date using SimpleDateFormat class  
//            d1 = ldf.parse( sdf.format(new Date()) );  
//        }   
//        // catch block for handling ParseException  
//        catch (java.text.ParseException e) {  
//            // TODO Auto-generated catch block  
//            e.printStackTrace();  
//            System.out.println(e.getMessage());  
//        }  
//        // pass UTC date to main method.  
//        return d1;  
    }

	public static String getBatchValues(String jsonString, String batString) {
		try {
			JSONObject jsonObject = new JSONObject(jsonString);
			jsonObject.put("Batchcoordinates", new JSONObject(batString));			
			
			return jsonObject.toString();

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}  
	
	public static String getcontainername(Integer multitenant, String tenantname)
	{
		String tenant = tenantname;
		if(multitenant == 2)
		{
			tenant = "freeusers";
		}
		
		return  tenant;
		
	}
	
	
	public static Map<String,Object> getdelaymillisecondforautoregister(String timeType, Integer interval)throws ParseException {
		Map<String,Object> RtnObject=new HashMap<>();
		Date currentDate = getCurrentUtcTime();
		LocalDateTime currentDateTime = currentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
//		String timeType = objorder.getLsautoregisterorderdetail().getTimespan();
//		Integer interval = objorder.getLsautoregisterorderdetail().getInterval();

		LocalDateTime updatedDateTime = currentDateTime;

		if (timeType.equals("Minutes")) {
		    updatedDateTime = updatedDateTime.plus(interval, ChronoUnit.MINUTES);
		} else if (timeType.equals("Hours")) {
		    updatedDateTime = updatedDateTime.plus(interval, ChronoUnit.HOURS);
		} else if (timeType.equals("Days")) {
		    updatedDateTime = updatedDateTime.plus(interval, ChronoUnit.DAYS);
		} else if (timeType.equals("Weeks")) {
		    updatedDateTime = updatedDateTime.plus(interval, ChronoUnit.WEEKS);
		}
		  Date updatedDate =Date.from(updatedDateTime.atZone(ZoneId.systemDefault()).toInstant());
	        long currentMillis = currentDate.getTime();
	        long updatedMillis = updatedDate.getTime();
	        long milliseconds = updatedMillis - currentMillis;
	        
	        RtnObject.put("Date", updatedDate);
	        RtnObject.put("delay", milliseconds);
	        return RtnObject;
	}
	

	public static String getEmptyProtocolContent() {
	    String jsonString = "{\n" +
	        "  \"protocolname\": \"\",\n" +
	        "  \"AI\": {\n" +
	        "    \"value\": {\n" +
	        "      \"data\": []\n" +
	        "    }\n" +
	        "  },\n" +
	        "  \"abstract\": {\n" +
	        "    \"value\": \"\"\n" +
	        "  },\n" +
	        "  \"matrices\": {\n" +
	        "    \"value\": \"\"\n" +
	        "  },\n" +
	        "  \"material\": {\n" +
	        "    \"value\": \"\"\n" +
	        "  },\n" +
	        "  \"sections\": [],\n" +
	        "  \"result\": {}\n" +
	        "}";

	    return jsonString;
	}

	 

}