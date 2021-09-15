package com.agaram.eln.primary.commonfunction;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
}