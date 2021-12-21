package com.agaram.eln.primary.commonfunction;


import java.text.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.agaram.eln.config.AESEncryption;

public class commonfunction {

	
	public static boolean checkuseronmanualaudit(String myKey,String auditPassword) {
		
		String encryptedpassword = AESEncryption.decrypt(myKey);
		
		String getPasscode = AESEncryption.decrypt(encryptedpassword.split("_", 0)[0]);
		
		if(auditPassword.equals(getPasscode)) {
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
	
	public static String getMIMEtypeonextension(String extension)
	{
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
}