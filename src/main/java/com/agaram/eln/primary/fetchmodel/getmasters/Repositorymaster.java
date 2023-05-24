package com.agaram.eln.primary.fetchmodel.getmasters;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class Repositorymaster {

	private Integer repositorycode;

	private String repositoryname;

	private String isonexpireddatefield;

	private boolean isexpiredate = false;

	private Date addedon;

	private String jsonFieldString;

	private List<Map<String, Object>> lstFields;
	
//	private String lstFieldsString;

	public Repositorymaster(Integer repositorycode, String repositoryname, String isonexpireddatefield,
			boolean isexpiredate, Date addedon) {

		this.repositorycode = repositorycode;
		this.repositoryname = repositoryname;
		this.isonexpireddatefield = isonexpireddatefield;
		this.isexpiredate = isexpiredate;
		this.addedon = addedon;
	}

	public Integer getRepositorycode() {
		return repositorycode;
	}

	public void setRepositorycode(Integer repositorycode) {
		this.repositorycode = repositorycode;
	}

	public String getRepositoryname() {
		return repositoryname;
	}

	public void setRepositoryname(String repositoryname) {
		this.repositoryname = repositoryname;
	}

	public String getIsonexpireddatefield() {
		return isonexpireddatefield;
	}

	public void setIsonexpireddatefield(String isonexpireddatefield) {
		this.isonexpireddatefield = isonexpireddatefield;
	}

	public boolean isIsexpiredate() {
		return isexpiredate;
	}

	public void setIsexpiredate(boolean isexpiredate) {
		this.isexpiredate = isexpiredate;
	}

	public Date getAddedon() {
		return addedon;
	}

	public void setAddedon(Date addedon) {
		this.addedon = addedon;
	}

	public String getJsonFieldString() {
		return jsonFieldString;
	}

	public void setJsonFieldString(String jsonFieldString) {
		if (jsonFieldString != "") {
			JSONObject jsonObject = new JSONObject(jsonFieldString);
			JSONArray jsonArrayDynamic = jsonObject.getJSONArray("inventoryFields");

			List<Map<String, Object>> lstObj = new ArrayList<>();

			jsonArrayDynamic.forEach(rowitem -> {
				JSONObject rowobj = (JSONObject) rowitem;
				Map<String, Object> mapObj = new HashMap<>();

				if (rowobj.get("fieldname").equals("Inventory id") || rowobj.get("fieldname").equals("Name")
						|| rowobj.get("fieldname").equals("Added by") || rowobj.get("fieldname").equals("Added on")||
						rowobj.get("fieldname").equals("Inventory ID") || rowobj.get("fieldname").equals("Inventory Name")
						|| rowobj.get("fieldname").equals("Azure ID")) {

					

				} else {
					mapObj.put("fieldname", (String) rowobj.get("fieldname"));
					mapObj.put("fieldkey", (String) rowobj.get("fieldkey"));

					lstObj.add(mapObj);
				}

			});
			
//			lstObj.toString();
			
//			this.setLstFieldsString(lstObj.toString());
			
//			JSONObject json = new JSONObject(lstObj.toString()); 
//			
//			System.out.print(json);

			this.setLstFields(lstObj);

		}
		this.jsonFieldString = jsonFieldString;
	}

	public List<Map<String, Object>> getLstFields() {
		return lstFields;
	}

	public void setLstFields(List<Map<String, Object>> lstFields) {
		this.lstFields = lstFields;
	}

//	public String getLstFieldsString() {
//		return lstFieldsString;
//	}
//
//	public void setLstFieldsString(String lstFieldsString) {
//		this.lstFieldsString = lstFieldsString;
//	}
}