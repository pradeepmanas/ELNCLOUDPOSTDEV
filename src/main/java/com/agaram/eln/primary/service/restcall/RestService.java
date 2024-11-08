package com.agaram.eln.primary.service.restcall;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
//import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import com.agaram.eln.primary.commonfunction.commonfunction;
import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.general.Response;
//import com.agaram.eln.primary.model.instrumentDetails.LSlimsorder;
import com.agaram.eln.primary.model.instrumentDetails.LSlogilablimsorder;
import com.agaram.eln.primary.model.instrumentDetails.LSlogilablimsorderdetail;
import com.agaram.eln.primary.model.instrumentDetails.LSlogilablimsordergroup;
import com.agaram.eln.primary.model.instrumentDetails.LsMappedFields;
import com.agaram.eln.primary.model.instrumentDetails.LsMappedInstruments;
import com.agaram.eln.primary.model.instrumentDetails.Lsbatchdetails;
import com.agaram.eln.primary.model.inventory.LSinstrument;
import com.agaram.eln.primary.model.inventory.LSinstrumentcategory;
import com.agaram.eln.primary.model.inventory.LSinstrumentsection;
import com.agaram.eln.primary.model.inventory.LSmaterial;
import com.agaram.eln.primary.model.inventory.LSmaterialcategory;
import com.agaram.eln.primary.model.inventory.LSmaterialgrade;
import com.agaram.eln.primary.model.inventory.LSmaterialinventory;
import com.agaram.eln.primary.model.inventory.LSmaterialinventorytransaction;
import com.agaram.eln.primary.model.inventory.LSmaterialsection;
import com.agaram.eln.primary.model.inventory.LSmaterialtype;
import com.agaram.eln.primary.model.inventory.LSsection;
import com.agaram.eln.primary.model.inventory.LSunit;
import com.agaram.eln.primary.model.sheetManipulation.LSsampleresult;
import com.agaram.eln.primary.model.sheetManipulation.LStestmaster;
import com.agaram.eln.primary.model.sheetManipulation.LStestparameter;
import com.agaram.eln.primary.model.usermanagement.LoggedUser;
import com.agaram.eln.primary.repository.cfr.LScfttransactionRepository;
//import com.agaram.eln.primary.repository.instrumentDetails.LSlimsorderRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LSlogilablimsorderRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LSlogilablimsorderdetailRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LSlogilablimsordergroupRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LsMappedFieldsRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LsMappedInstrumentsRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LsbatchdetailsRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LselninstfieldmappingRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LselninstrumentmappingRepository;
import com.agaram.eln.primary.repository.inventory.LSinstrumentRepository;
import com.agaram.eln.primary.repository.inventory.LSinstrumentcategoryRepository;
import com.agaram.eln.primary.repository.inventory.LSinstrumentsectionRepository;
import com.agaram.eln.primary.repository.inventory.LSmaterialRepository;
import com.agaram.eln.primary.repository.inventory.LSmaterialcategoryRepository;
import com.agaram.eln.primary.repository.inventory.LSmaterialgradeRepository;
import com.agaram.eln.primary.repository.inventory.LSmaterialinventoryRepository;
import com.agaram.eln.primary.repository.inventory.LSmaterialinventorytransactionRepository;
import com.agaram.eln.primary.repository.inventory.LSmaterialsectionRepository;
import com.agaram.eln.primary.repository.inventory.LSmaterialtypeRepository;
import com.agaram.eln.primary.repository.inventory.LSsectionRepository;
import com.agaram.eln.primary.repository.inventory.LSunitRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LSsampleresultRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LStestmasterRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LStestparameterRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LSworkflowRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class RestService {

	@Autowired
    private LStestmasterRepository LStestmasterRepository;
	@Autowired
	private LStestparameterRepository LStestparameterRepository;
//	@Autowired
//    private LSlimsorderRepository LSlimsorderRepository;
	@Autowired
    private LSlogilablimsorderRepository lslogilablimsorderRepository;
	@Autowired
    private LSmaterialRepository LSmaterialRepository;
	@Autowired
    private LSmaterialgradeRepository LSmaterialgradeRepository;
	@Autowired
	private LSinstrumentsectionRepository LSinstrumentsectionRepository;
	@Autowired
	private LSmaterialsectionRepository LSmaterialsectionRepository;
	@Autowired
	private LSmaterialtypeRepository LSmaterialtypeRepository;
	@Autowired
	private LSunitRepository LSunitRepository;
	@Autowired
	private LSsectionRepository LSsectionRepository;
	@Autowired
	private LSinstrumentRepository LSinstrumentRepository;
	@Autowired
	private LSinstrumentcategoryRepository LSinstrumentcategoryRepository;
	@Autowired
	private LSmaterialcategoryRepository LSmaterialcategoryRepository;
	@Autowired
	private LSmaterialinventoryRepository LSmaterialinventoryRepository;
	@Autowired
	private LSmaterialinventorytransactionRepository LSmaterialinventorytransactionRepository;
	@Autowired
	private LSworkflowRepository lsworkflowRepository;
	@Autowired
	private LSlogilablimsorderdetailRepository LSlogilablimsorderdetailRepository;
	@Autowired
	private LSsampleresultRepository LSsampleresultRepository;
	@Autowired
	private LScfttransactionRepository lscfttransactionRepository;
	@Autowired
	private LsbatchdetailsRepository LsbatchdetailsRepository;
	@Autowired
	private LsMappedInstrumentsRepository lsMappedInstrumentsRepository;
	@Autowired
	private LsMappedFieldsRepository lsMappedFieldsRepository;
	@Autowired
	private LSlogilablimsordergroupRepository lslogilablimsordergroupRepository;
	@Autowired
	private LselninstrumentmappingRepository lselninstrumentmappingRepository;
	@Autowired
	private LselninstfieldmappingRepository lselninstfieldmappingRepository;
	
	@Autowired
	private Environment env;
	
	public String ImportLimsTest(String str) throws Exception {
		
		Map<String, Object> map = new HashMap<>();
		
		ObjectMapper mapper = new ObjectMapper();
		
		String result = "";
		
		String isAPICalling = env.getProperty("limsbaseservice.serviceapi");
		
		if (isAPICalling.equals("true")) {
			
			String getTest=limsService("eln/getLimsTests");	   
			List<LStestmaster> mapTest = mapper.readValue(getTest,new TypeReference<List<LStestmaster>>() {});
			
			String getParam=limsService("eln/getLimsTestParameters");	   
			List<LStestparameter> mapParam = mapper.readValue(getParam,new TypeReference<List<LStestparameter>>() {});
		    
		    map.put("TestMaster", mapTest);
		    map.put("TestParameter", mapParam);
		    
		} else {
			
			String getTest=limsService("lslimsService/lslimsserviceget");	   
			List<LStestmaster> mapTest = mapper.readValue(getTest,new TypeReference<List<LStestmaster>>() {});
			
			String getParam=limsService("lslimsService/getlsLimsTestParameter");	   
			List<LStestparameter> mapParam = mapper.readValue(getParam,new TypeReference<List<LStestparameter>>() {});
		    
		    map.put("TestMaster", mapTest);
		    map.put("TestParameter", mapParam);
		    
		}
	    
	    boolean bool=insertTestMaster(map);
	    
	    if(bool) {
	    	result="success";
	    }
	    else {
	    	result="Failure";
	    }
		return result;
	}
	
	@SuppressWarnings("unchecked")
	private boolean insertTestMaster(Map<String, Object> mapObj) throws Exception {
        boolean bool = false;
        try {
            List<LStestmaster> lstTest = new ArrayList<LStestmaster>();
            lstTest = (List<LStestmaster>) mapObj.get("TestMaster");
            List<LStestparameter> lstparam = new ArrayList<LStestparameter>();
            lstparam = (List<LStestparameter>) mapObj.get("TestParameter");
            int i = 0;
            List<LStestparameter> lstparamJson = new ArrayList<LStestparameter>();
            if (lstparam.size() > 0) {
                LStestparameterRepository.deleteAll();
            }
            if (lstTest.size() > 0) {
                LStestmasterRepository.deleteAll();
            }
            for (int paramCode = 1000; lstTest.size() > i; ++i, ++paramCode) {
                LStestparameter testParam = new LStestparameter();
                testParam.setNtestparametercode(Integer.valueOf(paramCode));
                testParam.setNisadhocparameter(Integer.valueOf(0));
                testParam.setNisvisible(Integer.valueOf(0));
                testParam.setNmasterauditcode((Integer)null);
                testParam.setNparametertypecode(Integer.valueOf(paramCode));
                testParam.setNroundingdigits(Integer.valueOf(0));
                testParam.setNstatus(Integer.valueOf(0));
                testParam.setNtestcode(lstTest.get(i).getNtestcode());
                testParam.setNunitcode(Integer.valueOf(-1));
                testParam.setSparametername("ordercontent");
                testParam.setSparametersynonym("ordercontent");
                testParam.setNsitecode(lstTest.get(i).getNsitecode());
                testParam.setStestname(lstTest.get(i).getStestname());
                lstparamJson.add(testParam);
                LStestmasterRepository.save(lstTest.get(i));
            }
            for (i = 0; lstparam.size() > i; ++i) {
                LStestparameterRepository.save(lstparam.get(i));
            }
            if (lstparamJson.size() > 0) {
                LStestparameterRepository.save(lstparamJson);
            }
            bool = true;
        }
        catch (Exception e) {
            bool = false;
            e.getMessage();
        }
        return bool;
    }
	
	public String ImportLimsOrder(String str) throws Exception{
		
		Map<String, Object> map = new HashMap<>();
		
		String isAPICalling = env.getProperty("limsbaseservice.serviceapi");	
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
		String result = "";
		ObjectMapper mapper = new ObjectMapper();
		boolean bool = false;
		
		if (isAPICalling.equals("true")) {
			final String url = env.getProperty("limsbaseservice.url")+"eln/getLimsSDMSRecords";
			map.put("elnsitecode", str);
		    result = restTemplate.postForObject(url, map, String.class);
			
		    List<LSlogilablimsordergroup> mapLimsOrder = mapper.readValue(result,
					new TypeReference<List<LSlogilablimsordergroup>>() {
					});
		    
		    map.put("LimsOrderGroup", mapLimsOrder);
		    
		    bool = insertLimsOrderGroup(map);

		}
		else {
		
			final String url = env.getProperty("limsbaseservice.url")+"lslimsService/getsdmslabsheetmaster";
			result = restTemplate.postForObject(url, map, String.class);
			
			List<LSlogilablimsorder> mapLimsOrder = mapper.readValue(result,
					new TypeReference<List<LSlogilablimsorder>>() {
					});
			
			List<LSlogilablimsorderdetail> mapOrderDetail = mapper.readValue(result,
					new TypeReference<List<LSlogilablimsorderdetail>>() {
					});
			
			map.put("LimsOrder", mapLimsOrder);
		    map.put("LimsOrderDetail", mapOrderDetail);
		    
		    bool = insertLimsOrder(map);
		    
		    bool = InsertLimsOrderDetail(map);
		    
		    if(bool) {
		    	bool = InsertBatchOrderDetail(map);	    
		    }
		}
	    
	    if(bool) {
	    	result="success";
//	    	final LSlogilablimsorderdetail objLSlogilablimsorder = (LSlogilablimsorderdetail) mapOrderDetail;
//
//			new Thread(() -> {
//				try {
//					System.out.println("inside the thread SDMS order call");
//					createLogilabLIMSOrder4SDMS(objLSlogilablimsorder);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}).start();
	    }
	    else {
	    	result="Failure";
	    }
	    
		return result;
	}
    
    public String forSyncOrderFromLims() throws Exception{
		
		Map<String, Object> map = new HashMap<>();
		
		final String url = env.getProperty("limsbaseservice.url")+"lslimsService/getsdmslabsheetmaster";		

	    RestTemplate restTemplate = new RestTemplate();
	    
	    String result = restTemplate.postForObject(url, map, String.class);
	    
	    ObjectMapper mapper = new ObjectMapper();

		List<LSlogilablimsorder> mapLimsOrder = mapper.readValue(result,
				new TypeReference<List<LSlogilablimsorder>>() {
				});
		
		List<LSlogilablimsorderdetail> mapOrderDetail = mapper.readValue(result,
				new TypeReference<List<LSlogilablimsorderdetail>>() {
				});
	    
	    map.put("LimsOrder", mapLimsOrder);
	    map.put("LimsOrderDetail", mapOrderDetail);
	    
	    boolean bool = insertLimsOrder(map);
	    
	    bool = InsertLimsOrderDetail(map);
	    
	    if(bool) {
	    	result="success";
	    }
	    else {
	    	result="Failure";
	    }
	    
		return result;
	}
    
    @SuppressWarnings("unchecked")
	private boolean insertLimsOrderGroup(Map<String, Object> mapObj) {
		boolean bool=false;
		try {
			List<LSlogilablimsordergroup> lstOrder = new ArrayList<LSlogilablimsordergroup>();
			lstOrder=(List<LSlogilablimsordergroup>) mapObj.get("LimsOrderGroup");
			List<LSlogilablimsorderdetail> lstOrderDetail = new ArrayList<LSlogilablimsorderdetail>();
			List<LSlogilablimsorder> lstLimsOrders = new ArrayList<LSlogilablimsorder>();

			int i=0;
			
			while(lstOrder.size()>i) {
				
				LSlogilablimsorderdetail orderDetail = new LSlogilablimsorderdetail();
				LSlogilablimsorder lstLims = new LSlogilablimsorder();
				
				if(lslogilablimsordergroupRepository.findByLimsprimarycode(lstOrder.get(i).getLimsprimarycode()) == null) {
					lstOrder.get(i).setBatchid(lstOrder.get(i).getBatchid());
					lstOrder.get(i).setStestname(lstOrder.get(i).getStestname());
					lslogilablimsordergroupRepository.save(lstOrder.get(i));
				}
				
				if(LSlogilablimsorderdetailRepository.findByBatchid(lstOrder.get(i).getGroupid()) == null) {	
					if(!lstOrder.get(i).getNbatchmastercode().equals(-1)) {
						orderDetail.setNbatchcode(String.valueOf(lstOrder.get(i).getNbatchmastercode()));
					}
					orderDetail.setBatchid(lstOrder.get(i).getGroupid());
					orderDetail.setOrderflag("N");
					orderDetail.setTestcode(lstOrder.get(i).getNtestcode());
					orderDetail.setTestname(lstOrder.get(i).getStestname());
					orderDetail.setCreatedtimestamp(commonfunction.getCurrentUtcTime());
//					orderDetail.setNbatchcode(String.valueOf(lstOrder.get(i).getNbatchmastercode()));
					orderDetail.setLsworkflow(lsworkflowRepository.findTopByOrderByWorkflowcodeAsc());
					orderDetail.setFiletype(0);
					lstOrderDetail.add(orderDetail);
					LSlogilablimsorderdetailRepository.save(lstOrderDetail);
				}
				
				if(lslogilablimsorderRepository.findByBatchid(lstOrder.get(i).getGroupid()) == null) {
					if(!lstOrder.get(i).getNbatchmastercode().equals(-1)) {
						lstLims.setNbatchcode(orderDetail.getNbatchcode());
					}
					String Limsorder = orderDetail.getBatchcode().toString();
					lstLims.setOrderid(Long.parseLong(Limsorder.concat("00")));
					lstLims.setBatchid(orderDetail.getBatchid());
					lstLims.setOrderflag(orderDetail.getOrderflag());
					lstLims.setTestcode(Integer.toString(orderDetail.getTestcode()));
//					lstLims.setNbatchcode(orderDetail.getNbatchcode());
					lstLims.setCreatedtimestamp(commonfunction.getCurrentUtcTime());
					lstLims.setCompletedtimestamp(null);
					lstLimsOrders.add(lstLims);
					lslogilablimsorderRepository.save(lstLimsOrders);
				}
				
				if(!lstOrder.get(i).getNbatchmastercode().equals(-1)) {
					LSlogilablimsorder lsOrder = lslogilablimsorderRepository.findFirstByBatchidOrderByOrderidDesc(lstOrder.get(i).getGroupid());
					LSlogilablimsorderdetail lsDetail = LSlogilablimsorderdetailRepository.findByBatchid(lstOrder.get(i).getGroupid());
					
					Lsbatchdetails lsBatch = new Lsbatchdetails();
					
					lsBatch.setBatchcode(lsDetail.getBatchcode());
					lsBatch.setSampleid(lstOrder.get(i).getSsamplearno());
					lsBatch.setOrderID(lsOrder.getOrderid());
					lsBatch.setLimsorderID(lstOrder.get(i).getNtransactiontestcode());
					lsBatch.setLimsprimarycode(lstOrder.get(i).getLimsprimarycode());
					
					LsbatchdetailsRepository.save(lsBatch);
				}
				i++;
			}
			bool=updateLIMSGroupMaster(mapObj);
			if(bool) {
				bool=true;
			}else {
				bool=false;
			}
		}
		catch (Exception e) {
			bool=false;
			e.getMessage();
		}
		return bool;
	}

	@SuppressWarnings("unchecked")
	private boolean updateLIMSGroupMaster(Map<String, Object> mapObj) throws Exception{
		@SuppressWarnings("unused")
		String result="";
		boolean bool=false;
		try {
			Map<String, Object> map = new HashMap<>();
			
			List<LSlogilablimsordergroup> lstOrder = new ArrayList<LSlogilablimsordergroup>();
			
			lstOrder=(List<LSlogilablimsordergroup>) mapObj.get("LimsOrderGroup");
			
			String getSql = "";

			int i = 0;
			while (i < lstOrder.size()) {
				if (i == 0) {
					getSql += lstOrder.get(i).getLimsprimarycode();
				} else {
					getSql += "," + lstOrder.get(i).getLimsprimarycode();
				}
				i++;
			}
			
			map.put("limsprimarycode", getSql);
			
			final String url = env.getProperty("limsbaseservice.url")+"/eln/updateLimsSDMSInprogress";
			
		    RestTemplate restTemplate = new RestTemplate();
		    
		    result = restTemplate.postForObject(url, map, String.class);
		    
		    bool=true;
		}
		catch (Exception e) {
			result=e.getMessage();
			bool=false;
		} 
		return bool;
	}
	
	@SuppressWarnings("unchecked")
	private boolean insertLimsOrder(Map<String, Object> mapObj) {
		boolean bool=false;
		try {
			List<LSlogilablimsorder> lstOrder = new ArrayList<LSlogilablimsorder>();
			lstOrder=(List<LSlogilablimsorder>) mapObj.get("LimsOrder");
			
			int i=0;
			
			while(lstOrder.size()>i) {
				if(lslogilablimsorderRepository.findByBatchid(lstOrder.get(i).getBatchid()) == null) {
					lstOrder.get(i).setCreatedtimestamp(commonfunction.getCurrentUtcTime());
					lstOrder.get(i).setCompletedtimestamp(null);
					lslogilablimsorderRepository.save(lstOrder.get(i));
				}
				i++;
			}
			bool=updatesdsmaster(mapObj);
			if(bool) {
				bool=true;
			}else {
				bool=false;
			}
		}
		catch (Exception e) {
			bool=false;
			e.getMessage();
		}
		return bool;
	}
	
	@SuppressWarnings("unchecked")
	private boolean InsertLimsOrderDetail(Map<String, Object> mapObj) throws Exception{
		List<LSlogilablimsorderdetail> lstOrder = new ArrayList<LSlogilablimsorderdetail>();
		lstOrder=(List<LSlogilablimsorderdetail>) mapObj.get("LimsOrderDetail");
//		List<LSlogilablimsorder> limsOrder = new ArrayList<LSlogilablimsorder>();
//		limsOrder=(List<LSlogilablimsorder>) mapObj.get("LimsOrder");
		
		int i=0;
		
		while(lstOrder.size()>i) {
			lstOrder.get(i).setLsworkflow(lsworkflowRepository.findTopByOrderByWorkflowcodeAsc());
			lstOrder.get(i).setFiletype(0);
			
			if(LSlogilablimsorderdetailRepository.findByBatchid(lstOrder.get(i).getBatchid()) == null) {
				lstOrder.get(i).setCompletedtimestamp(null);
				LSlogilablimsorderdetailRepository.save(lstOrder.get(i));
			}
			i++;
		}
//		i=0;
//		while(lstOrder.size()>i) {
//			
//			if(lstOrder.get(i).getNbatchcode() != null) {
//				LSlogilablimsorder lsOrder = lslogilablimsorderRepository.findFirstByBatchidOrderByOrderidDesc(lstOrder.get(i).getBatchid());
//				LSlogilablimsorderdetail lsDetail = LSlogilablimsorderdetailRepository.findByBatchid(lstOrder.get(i).getBatchid());
//				
//				Lsbatchdetails lsBatch = new Lsbatchdetails();
//				
//				lsBatch.setBatchcode(lsDetail.getBatchcode());
//				lsBatch.setSampleid(lstOrder.get(i).getSampleid());
//				lsBatch.setOrderID(lsOrder.getOrderid());
//				lsBatch.setLimsorderID(limsOrder.get(i).getOrderid());
//				
//				LsbatchdetailsRepository.save(lsBatch);
//			}
//			i++;
//		}
		return true;
	}
	
	@SuppressWarnings("unchecked")
	private boolean InsertBatchOrderDetail(Map<String, Object> mapObj) throws Exception{
		List<LSlogilablimsorderdetail> lstOrder = new ArrayList<LSlogilablimsorderdetail>();
		lstOrder=(List<LSlogilablimsorderdetail>) mapObj.get("LimsOrderDetail");
		List<LSlogilablimsorder> limsOrder = new ArrayList<LSlogilablimsorder>();
		limsOrder=(List<LSlogilablimsorder>) mapObj.get("LimsOrder");
		int i=0;
		while(lstOrder.size()>i) {
			
			if(lstOrder.get(i).getNbatchcode() != null) {
				LSlogilablimsorder lsOrder = lslogilablimsorderRepository.findFirstByBatchidOrderByOrderidDesc(lstOrder.get(i).getBatchid());
				LSlogilablimsorderdetail lsDetail = LSlogilablimsorderdetailRepository.findByBatchid(lstOrder.get(i).getBatchid());
				
				Lsbatchdetails lsBatch = new Lsbatchdetails();
				
				lsBatch.setBatchcode(lsDetail.getBatchcode());
				lsBatch.setSampleid(lstOrder.get(i).getSampleid());
				lsBatch.setOrderID(lsOrder.getOrderid());
				lsBatch.setLimsorderID(limsOrder.get(i).getOrderid());
				
				LsbatchdetailsRepository.save(lsBatch);
			}
			i++;
		}
		return true;
	}
	
	@SuppressWarnings("unchecked")
	private boolean updatesdsmaster(Map<String, Object> mapObj) throws Exception{
		@SuppressWarnings("unused")
		String result="";
		boolean bool=false;
		try {
			Map<String, Object> map = new HashMap<>();
			
			List<LSlogilablimsorder> lstOrder = new ArrayList<LSlogilablimsorder>();
			
			lstOrder=(List<LSlogilablimsorder>) mapObj.get("LimsOrder");
			
			String getSql = "";

			int i = 0;
			while (i < lstOrder.size()) {
				if (i == 0) {
					getSql += lstOrder.get(i).getOrderid();
				} else {
					getSql += "," + lstOrder.get(i).getOrderid();
				}
				i++;
			}
			
			map.put("ntransactiontestcode", getSql);
			
			final String url = env.getProperty("limsbaseservice.url")+"lslimsService/updateSdmsLabsheet";
			
		    RestTemplate restTemplate = new RestTemplate();
		    
		    result = restTemplate.postForObject(url, map, String.class);
		    
		    bool=true;
		}
		catch (Exception e) {
			result=e.getMessage();
			bool=false;
		} 
		return bool;
	}

	public String limsService(String Service) {
		
		Map<String, Object> map = new HashMap<>();
		
		final String url = env.getProperty("limsbaseservice.url")+Service;

	    RestTemplate restTemplate = new RestTemplate();
	    restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
	    
	    String result = restTemplate.postForObject(url, map, String.class);
	    
	    return result;
	}

	public String importLIMSMaterial() throws Exception{
		
		Map<String, Object> map = new HashMap<>();
		ObjectMapper mapper = new ObjectMapper();
		
		//LSmaterial
		String getMaterial=limsService("materialController/getMateriallst");
		List<LSmaterial> mapMaterial = mapper.readValue(getMaterial,new TypeReference<List<LSmaterial>>() {});	
		//LSmaterialgrade
		String getGrade=limsService("materialController/getMaterialGrade");
		List<LSmaterialgrade> mapGrade = mapper.readValue(getGrade,new TypeReference<List<LSmaterialgrade>>() {});
		//LSsection
		String getSec=limsService("materialController/getSection");
		List<LSsection> mapSec = mapper.readValue(getSec,new TypeReference<List<LSsection>>() {});
		
		//LSinstrumentsection
		String getInsSec=limsService("materialController/getInstrumentSection");
		List<LSinstrumentsection> mapInsSec = mapper.readValue(getInsSec,new TypeReference<List<LSinstrumentsection>>() {});
		//LSmaterialsection
		String getMatSec=limsService("materialController/getMaterialSection");
		List<LSmaterialsection> mapMatSec = mapper.readValue(getMatSec,new TypeReference<List<LSmaterialsection>>() {});
		//LSmaterialtype
		String getMatTp=limsService("materialController/getMaterialtype");
		List<LSmaterialtype> mapMatTp = mapper.readValue(getMatTp,new TypeReference<List<LSmaterialtype>>() {});
		
		//LSunit
		String getUnit=limsService("materialController/getUnit");
		List<LSunit> mapUnit = mapper.readValue(getUnit,new TypeReference<List<LSunit>>() {});
		//LSinstrument
		String getIns=limsService("materialController/getInstrument");
		List<LSinstrument> mapIns = mapper.readValue(getIns,new TypeReference<List<LSinstrument>>() {});
		//LSinstrumentcategory
		String getInsCat=limsService("materialController/getInstrumentCategory");
		List<LSinstrumentcategory> mapInsCat = mapper.readValue(getInsCat,new TypeReference<List<LSinstrumentcategory>>() {});
		
		//LSmaterialcategory
		String getMatCat=limsService("materialController/getMaterialCategory");
		List<LSmaterialcategory> mapMatCat = mapper.readValue(getMatCat,new TypeReference<List<LSmaterialcategory>>() {});
		//LSmaterialinventory
		String getMatInv=limsService("materialController/getMaterialInvent");
		List<LSmaterialinventory> mapMatInv = mapper.readValue(getMatInv,new TypeReference<List<LSmaterialinventory>>() {});
		//LSmanufacturer
//		String getManf=limsService("materialController/getManufacturer");
//		List<LSmanufacturer> mapManf = mapper.readValue(getManf,new TypeReference<List<LSmanufacturer>>() {});
		
		map.put("LSmaterial", mapMaterial);
		map.put("LSmaterialgrade", mapGrade);
		map.put("LSsection",mapSec);
		
		map.put("LSinstrumentsection",mapInsSec);
		map.put("LSmaterialsection",mapMatSec);
		map.put("LSmaterialtype",mapMatTp);
		
		map.put("LSunit",mapUnit);
		map.put("LSinstrument",mapIns);
		map.put("LSinstrumentcategory",mapInsCat);
		
		map.put("LSmaterialcategory",mapMatCat);
		map.put("LSmaterialinventory",mapMatInv);
//		map.put("LSmanufacturer",mapManf);
		
		boolean bool=insertLIMSTable(map);
		
		if(bool) {
			return "Success";
		}else {
			return "Failure";
		}
	}

	@SuppressWarnings("unchecked")
	private boolean insertLIMSTable(Map<String, Object> mapObj) throws Exception {
		
		boolean bool=false;
		
		try {
			List<LSmaterial> lstMat = new ArrayList<LSmaterial>();
			lstMat=(List<LSmaterial>) mapObj.get("LSmaterial");
			List<LSmaterialgrade> lstMatGd = new ArrayList<LSmaterialgrade>();
			lstMatGd=(List<LSmaterialgrade>) mapObj.get("LSmaterialgrade");	
			List<LSinstrumentsection> lstInsSec = new ArrayList<LSinstrumentsection>();
			lstInsSec=(List<LSinstrumentsection>) mapObj.get("LSinstrumentsection");
			
			List<LSmaterialsection> lstMatsec = new ArrayList<LSmaterialsection>();
			lstMatsec=(List<LSmaterialsection>) mapObj.get("LSmaterialsection");
			List<LSmaterialtype> lstMatType = new ArrayList<LSmaterialtype>();
			lstMatType=(List<LSmaterialtype>) mapObj.get("LSmaterialtype");
			List<LSunit> lstUnit = new ArrayList<LSunit>();
			lstUnit=(List<LSunit>) mapObj.get("LSunit");
			
			List<LSsection> lstSec = new ArrayList<LSsection>();
			lstSec=(List<LSsection>) mapObj.get("LSsection");
			List<LSinstrument> lstIns = new ArrayList<LSinstrument>();
			lstIns=(List<LSinstrument>) mapObj.get("LSinstrument");
			List<LSinstrumentcategory> lstInscat = new ArrayList<LSinstrumentcategory>();
			lstInscat=(List<LSinstrumentcategory>) mapObj.get("LSinstrumentcategory");
			
			List<LSmaterialcategory> lstMatCat = new ArrayList<LSmaterialcategory>();
			lstMatCat=(List<LSmaterialcategory>) mapObj.get("LSmaterialcategory");
			List<LSmaterialinventory> lstMatInv = new ArrayList<LSmaterialinventory>();
			lstMatInv=(List<LSmaterialinventory>) mapObj.get("LSmaterialinventory");
//			List<LSmanufacturer> lstManf = new ArrayList<LSmanufacturer>();
//			lstManf=(List<LSmanufacturer>) mapObj.get("LSmanufacturer");
			
			int lsMatCount=(int) LSmaterialRepository.count();
			int lsMatGdCount=(int) LSmaterialgradeRepository.count();
			int lsInsSecCount=(int) LSinstrumentsectionRepository.count();
			
			int lsMatSecCount=(int) LSmaterialsectionRepository.count();
			int lsMatTpCount=(int) LSmaterialtypeRepository.count();
			int lsUnitCount=(int) LSunitRepository.count();
			
			int lsSecCount=(int) LSsectionRepository.count();
			int lsInsCount=(int) LSinstrumentRepository.count();
			int lsInsCatCount=(int) LSinstrumentcategoryRepository.count();
			
			int lsMatcatCount=(int) LSmaterialcategoryRepository.count();
			int lsMatInvCount=(int) LSmaterialinventoryRepository.count();
//			int lsManCount=(int) LSmanufacturerRepository.count();
				
			int i=0;
			if(lstMat.size()>lsMatCount && lstMat.size()==lsMatCount)
				i=lsMatCount;
            else {
            	LSmaterialRepository.deleteAll();
            }
			while(lstMat.size()>i) {
			
				LSmaterialRepository.save(lstMat.get(i));
				
				LScfttransaction silentAudit=new LScfttransaction();
				
				silentAudit.setModuleName("Basemaster");
				silentAudit.setComments("Added LIMS Material");
				silentAudit.setActions("Insert");
				silentAudit.setSystemcoments("System Generated");
				silentAudit.setTableName("LSmaterial");
				silentAudit.setTransactiondate(commonfunction.getCurrentUtcTime());
	    		lscfttransactionRepository.save(silentAudit);
				
				i++;
			}
			i=0;
			if(lstMatGd.size()>lsMatGdCount  && lstMatGd.size()==lsMatGdCount)
				i=lsMatGdCount;
			else {
				LSmaterialgradeRepository.deleteAll();
			}
			while(lstMatGd.size()>i) {
				
				LSmaterialgradeRepository.save(lstMatGd.get(i));
				
				LScfttransaction silentAudit=new LScfttransaction();
				
				silentAudit.setModuleName("Basemaster");
				silentAudit.setComments("Added LIMS MaterialGrade");
				silentAudit.setActions("Insert");
				silentAudit.setSystemcoments("System Generated");
				silentAudit.setTableName("LSmaterialgrade");
				silentAudit.setTransactiondate(commonfunction.getCurrentUtcTime());
	    		lscfttransactionRepository.save(silentAudit);
				
				i++;
			}
			i=0;
			if(lstInsSec.size()>lsInsSecCount && lstInsSec.size()==lsInsSecCount)
				i=lsInsSecCount;
			else {
				LSinstrumentsectionRepository.deleteAll();
			}
			while(lstInsSec.size()>i) {
				
				LSinstrumentsectionRepository.save(lstInsSec.get(i));
				
				LScfttransaction silentAudit=new LScfttransaction();
				
				silentAudit.setModuleName("Basemaster");
				silentAudit.setComments("Added LIMS Instrumentsection");
				silentAudit.setActions("Insert");
				silentAudit.setSystemcoments("System Generated");
				silentAudit.setTableName("LSinstrument");
				silentAudit.setTransactiondate(commonfunction.getCurrentUtcTime());
	    		lscfttransactionRepository.save(silentAudit);
				
				i++;
			}
			
			i=0;
			if(lstMatsec.size()>lsMatSecCount && lstMatsec.size()==lsMatSecCount)
				i=lsMatSecCount;
			else {
				LSmaterialsectionRepository.deleteAll();
			}
			while(lstMatsec.size()>i) {
				
				LSmaterialsectionRepository.save(lstMatsec.get(i));
				
				LScfttransaction silentAudit=new LScfttransaction();
				
				silentAudit.setModuleName("Basemaster");
				silentAudit.setComments("Added LIMS MaterialSection");
				silentAudit.setActions("Insert");
				silentAudit.setSystemcoments("System Generated");
				silentAudit.setTableName("LSmaterialsection");
				silentAudit.setTransactiondate(commonfunction.getCurrentUtcTime());
	    		lscfttransactionRepository.save(silentAudit);
				
				i++;
			}
			i=0;
			if(lstMatType.size()>lsMatTpCount && lstMatType.size()==lsMatTpCount)
				i=lsMatTpCount;
			else
			{
				LSmaterialtypeRepository.deleteAll();
			}
			while(lstMatType.size()>i) {
				
				LSmaterialtypeRepository.save(lstMatType.get(i));
				
				LScfttransaction silentAudit=new LScfttransaction();
				
				silentAudit.setModuleName("Basemaster");
				silentAudit.setComments("Added LIMS MaterialType");
				silentAudit.setActions("Insert");
				silentAudit.setSystemcoments("System Generated");
				silentAudit.setTableName("LSmaterialtype");
				silentAudit.setTransactiondate(commonfunction.getCurrentUtcTime());
	    		lscfttransactionRepository.save(silentAudit);
				
				i++;
			}
			i=0;
			if(lstUnit.size()>lsUnitCount && lstUnit.size()==lsUnitCount)
				i=lsUnitCount;
			else
			{
				LSunitRepository.deleteAll();
			}
			while(lstUnit.size()>i) {
				
				LSunitRepository.save(lstUnit.get(i));
				
				LScfttransaction silentAudit=new LScfttransaction();
				
				silentAudit.setModuleName("Basemaster");
				silentAudit.setComments("Added LIMS Unit");
				silentAudit.setActions("Insert");
				silentAudit.setSystemcoments("System Generated");
				silentAudit.setTableName("LSunit");
				silentAudit.setTransactiondate(commonfunction.getCurrentUtcTime());
	    		lscfttransactionRepository.save(silentAudit);
				
				i++;
			}
			
			i=0;
			if(lstSec.size()>lsSecCount && lstSec.size()==lsSecCount)
				i=lsSecCount;
			else
			{
				LSsectionRepository.deleteAll();
			}
			while(lstSec.size()>i) {
				
				LSsectionRepository.save(lstSec.get(i));
				
				LScfttransaction silentAudit=new LScfttransaction();
				
				silentAudit.setModuleName("Basemaster");
				silentAudit.setComments("Added LIMS Section Master");
				silentAudit.setActions("Insert");
				silentAudit.setSystemcoments("System Generated");
				silentAudit.setTableName("LSsection");
				silentAudit.setTransactiondate(commonfunction.getCurrentUtcTime());
	    		lscfttransactionRepository.save(silentAudit);
				
				i++;
			}
			i=0;
			if(lstIns.size()>lsInsCount && lstIns.size()==lsInsCount)
				i=lsInsCount;
			else
			{
				LSinstrumentRepository.deleteAll();
			}
			while(lstIns.size()>i) {
				
				LSinstrumentRepository.save(lstIns.get(i));
				
				LScfttransaction silentAudit=new LScfttransaction();
				
				silentAudit.setModuleName("Basemaster");
				silentAudit.setComments("Added LIMS instrument");
				silentAudit.setActions("Insert");
				silentAudit.setSystemcoments("System Generated");
				silentAudit.setTableName("LSinstrument");
				silentAudit.setTransactiondate(commonfunction.getCurrentUtcTime());
	    		lscfttransactionRepository.save(silentAudit);
				
				i++;
			}
			i=0;
			if(lstIns.size()>lsInsCatCount && lstIns.size()==lsInsCatCount)
				i=lsInsCatCount;
			else {
				LSinstrumentcategoryRepository.deleteAll();
			}
			while(lstInscat.size()>i) {
				
				LSinstrumentcategoryRepository.save(lstInscat.get(i));
				
				LScfttransaction silentAudit=new LScfttransaction();
				
				silentAudit.setModuleName("Basemaster");
				silentAudit.setComments("Added LIMS instrument category");
				silentAudit.setActions("Insert");
				silentAudit.setSystemcoments("System Generated");
				silentAudit.setTableName("LSinstrumentcategory");
				silentAudit.setTransactiondate(commonfunction.getCurrentUtcTime());
	    		lscfttransactionRepository.save(silentAudit);
				
				i++;
			}
			
			i=0;
			if(lstMatCat.size()>lsMatcatCount && lstMatCat.size()==lsMatcatCount)
				i=lsMatcatCount;
			else {
				LSmaterialcategoryRepository.deleteAll();
			}
			while(lstMatCat.size()>i) {
				
				LSmaterialcategoryRepository.save(lstMatCat.get(i));
				
				LScfttransaction silentAudit=new LScfttransaction();
				
				silentAudit.setModuleName("Basemaster");
				silentAudit.setComments("Added LIMS Material category");
				silentAudit.setActions("Insert");
				silentAudit.setSystemcoments("System Generated");
				silentAudit.setTableName("LSmaterialcategory");
				silentAudit.setTransactiondate(commonfunction.getCurrentUtcTime());
	    		lscfttransactionRepository.save(silentAudit);
				
				i++;
			}
			i=0;
			if(lstMatInv.size()>lsMatInvCount && lstMatInv.size()==lsMatInvCount)
				i=lsMatInvCount;
			else {
				LSmaterialinventoryRepository.deleteAll();
			}
			while(lstMatInv.size()>i) {
				
				LSmaterialinventoryRepository.save(lstMatInv.get(i));
				
				i++;
			}
//			i=0;
//			if(lstManf.size()>lsManCount && lstManf.size()==lsManCount)
//				i=lsManCount;
//			else {
//				LSmanufacturerRepository.deleteAll();
//			}
//			while(lstManf.size()>i) {
//				
//				LSmanufacturerRepository.save(lstManf.get(i));
//				
//				LScfttransaction silentAudit=new LScfttransaction();
//				
//				silentAudit.setModuleName("Basemaster");
//				silentAudit.setComments("Added LIMS Manufacturer");
//				silentAudit.setActions("Insert");
//				silentAudit.setSystemcoments("System Generated");
//				silentAudit.setTableName("LSmanufacturer");
//	    		lscfttransactionRepository.save(silentAudit);
//				
//				i++;
//			}
				
			bool=true;
		}
		catch (Exception e) {
			bool=false;
			e.getMessage();
		}
		return bool;
	}

	public String importLIMSMaterialTrans() throws Exception{
		Map<String, Object> map = new HashMap<>();
		ObjectMapper mapper = new ObjectMapper();
		
		String getMatTr=limsService("materialController/getMaterialInventTrans");
		List<LSmaterialinventorytransaction> mapMatTr = mapper.readValue(getMatTr,new TypeReference<List<LSmaterialinventorytransaction>>() {});	
		
		map.put("LSmaterialinventorytransaction", mapMatTr);
		
		boolean bool=insertInventory(map);
		if(bool)
			return "Success";
		else
			return "Failure";
	}

	@SuppressWarnings("unchecked")
	private boolean insertInventory(Map<String, Object> mapObj) throws Exception{
		boolean bool=false;
		
		try {
			List<LSmaterialinventorytransaction> lstInv = new ArrayList<LSmaterialinventorytransaction>();
			
			lstInv=(List<LSmaterialinventorytransaction>) mapObj.get("LSmaterialinventorytransaction");
			
			int i=0;
			
			while(lstInv.size()>i) {
			
				LSmaterialinventorytransactionRepository.save(lstInv.get(i));
				
				LScfttransaction silentAudit=new LScfttransaction();
				
				silentAudit.setModuleName("Sheet View");
				silentAudit.setComments("Added LIMS material consumbtion");
				silentAudit.setActions("Insert");
				silentAudit.setSystemcoments("System Generated");
				silentAudit.setTableName("LSmaterialinventorytransaction");
				silentAudit.setTransactiondate(commonfunction.getCurrentUtcTime());
	    		lscfttransactionRepository.save(silentAudit);
				
				i++;
			}
			bool=true;
		}
		catch (Exception e) {
			bool=false;
			e.getMessage();
		}
		return bool;
	}
	
	public Response getUpdateSdmslabsheetDetail(Map<String, Object> objMap) throws Exception {
		
 		Response res=new Response();
		
		if (objMap.containsKey("batchcode")) {
			
			Map<String, Object> map = new HashMap<>();
			
			long batchcode = ((Number) objMap.get("batchcode")).longValue();
			
			LSlogilablimsorderdetail objorder = LSlogilablimsorderdetailRepository.findOne(batchcode);
			
			String Batchid=(String) objMap.get("Batch");
			
			LSlogilablimsorder limsOrder=lslogilablimsorderRepository.findFirstByBatchidOrderByOrderidDesc(Batchid);
		 	
		 	Long order = limsOrder.getOrderid();
		 	Integer testcode = Integer.valueOf(objMap.get("testcode").toString());
		 	
		 	List<LSsampleresult> lstResult = LSsampleresultRepository.findByBatchcodeAndTestcode(batchcode, testcode);
		 	
			System.out.print("parameter : " + lstResult);
		 	
		 	if(!lstResult.isEmpty()) {
		 		
		 		int i = 0;

		 		List<Map<String, Object>> lssampleresult =  new ArrayList<Map<String, Object>>();
		 		String isAPICalling = env.getProperty("limsbaseservice.serviceapi");
		 		String result = "";
		 		RestTemplate restTemplate = new RestTemplate();
				
		 		Map<String, Object> jsMap = new HashMap<>();
		 		
				if (isAPICalling.equals("true")) {
					
					List<LSlogilablimsordergroup> orderGroup = lslogilablimsordergroupRepository.findByGroupid(Batchid);
					
					while (i < lstResult.size()) {
						int j = 0;
						while (j < (orderGroup.size() + 1)) {
							
							if(orderGroup != null && !lstResult.get(i).getResult().isEmpty() && j < orderGroup.size() &&lstResult.get(i).getParametercode().equals(orderGroup.get(j).getNtestparametercode())
									&& (lstResult.get(i).getOrderid() == 0 || lstResult.get(i).getOrderid() == orderGroup.get(j).getNtransactiontestcode())) {
								Map<String, Object> lstMap = new HashMap<>();
								String sresult = lstResult.get(i).getResult();
								Long orderid = orderGroup.get(j).getLimsprimarycode();
								lstMap.put("sresult",sresult);
								lstMap.put("limsprimarycode",orderid);
								lssampleresult.add(lstMap);
								break;
							} else if(lstResult.get(i).getOrderid() == -1) {
								jsMap.put("jsondata", lstResult.get(i).getResult());
							}
							
							j++;
						}
						i++;
						
					}
					
					map.put("lssampleresult", lssampleresult);
					map.put("batcharno", objorder.getBatchid());
					map.put("ntestcode", objorder.getTestcode());
					map.put("jsondata", jsMap.get("jsondata"));
					
					final String url = env.getProperty("limsbaseservice.url")+"eln/updateLimsSDMSComplete";
				    
				    result = restTemplate.postForObject(url, map, String.class);
					
				} else {
					while (i < lstResult.size()) {
	
						Map<String, Object> lstMap = new HashMap<>();
						
						Integer ntestcode = lstResult.get(i).getTestcode();
						Integer ntestparametercode = lstResult.get(i).getParametercode();
						String sresult = lstResult.get(i).getResult();
						Long orderid = lstResult.get(i).getOrderid() == 0 ? order : lstResult.get(i).getOrderid();
						
						lstMap.put("ntestcode", ntestcode);
						lstMap.put("ntestparametercode",ntestparametercode);
						lstMap.put("sresult",sresult);
						lstMap.put("orderid",orderid);
	
						lssampleresult.add(i, lstMap);
						
						i++;
					}
					
					map.put("lssampleresult", lssampleresult);
					
					final String url = env.getProperty("limsbaseservice.url")+"lslimsService/updateSdmsLabsheetDetail";
				    
				    result = restTemplate.postForObject(url, map, String.class);
				}
				
			    if(result.equals("success") || result.equals("Success")) {
			    	res.setInformation("success");
			    	res.setStatus(true);
			    	
			    	return res;
			    }else {
			    	res.setInformation("IDS_MSG_SENDTOLIMSFAILED");
			    	res.setStatus(false);
			    	
			    	return res;
			    }
		 	}
		 	else {
		 		
		 		if(objorder.getOrderflag().trim().equals("N"))
		 		{
		 			res.setInformation("IDS_MSG_NOTCOMPLETE");
		 		}
		 		else
		 		{
		 			res.setInformation("IDS_MSG_NOPARAMETERS");
		 		}
		    	res.setStatus(false);
		    	
		    	return res;
		 	}
		}
		
		ObjectMapper objMapper= new ObjectMapper();
		LScfttransaction cfttransaction;
		if(objMap.containsKey("objsilentaudit")) {
			cfttransaction = objMapper.convertValue(objMap.get("objsilentaudit"), LScfttransaction.class);
			cfttransaction.setTransactiondate(commonfunction.getCurrentUtcTime());
			lscfttransactionRepository.save(cfttransaction);
		}
		if(objMap.containsKey("objuser")) {
			LoggedUser objuser=objMapper.convertValue(objMap.get("objuser"), LoggedUser.class);
			LScfttransaction manualcfrt = objMapper.convertValue(objMap.get("objmanualaudit"), LScfttransaction.class);
			manualcfrt.setComments(objuser.getComments());
			manualcfrt.setTransactiondate(commonfunction.getCurrentUtcTime());
			lscfttransactionRepository.save(manualcfrt);
		}
		res.setInformation("ID_INVALIDORDERCODE");
    	res.setStatus(false);
    	
    	return res;
	}
	
	@SuppressWarnings("unchecked")
	public String ImportSDMSTest(Map<String, Object> objMap) throws Exception {
		
		ObjectMapper mapper = new ObjectMapper();
		
		String result = "";
		
		String getInst=sdmsService("IntegrationSDMS/getInstrumentList");
		Map<String, Object> mapInstObj = mapper.readValue(getInst,new TypeReference<Map<String, Object>>() {});
		List<LsMappedInstruments> instrumentLst = mapper.convertValue(mapInstObj.get("InstrumentList"),new TypeReference<List<LsMappedInstruments>>() {});
//		List<LsMappedInstruments> instrumentLst = (List<LsMappedInstruments>) new ObjectMapper().readValue(getInst, Map.class).get("InstrumentList");
		
		String getFields=sdmsService("IntegrationSDMS/getMasterFieldsByMethod");
		List<LsMappedFields> fieldsLst = (List<LsMappedFields>) new ObjectMapper().readValue(getFields, new TypeReference<List<LsMappedFields>>() {});
//		List<LsMappedFields> mapParam = mapper.readValue(getFields,new TypeReference<List<LsMappedFields>>() {});
	    
		boolean bool;
		
		try {
			if(!instrumentLst.isEmpty()) {
				lselninstrumentmappingRepository.deleteAll();
				lsMappedInstrumentsRepository.deleteAll();
				lsMappedInstrumentsRepository.save(instrumentLst);
			}

			if(!fieldsLst.isEmpty()) {
				lselninstfieldmappingRepository.deleteAll();
				lsMappedFieldsRepository.deleteAll();
				lsMappedFieldsRepository.save(fieldsLst);
			}
			
			bool=true;
		}
		catch (Exception e) {
			bool=false;
			e.getMessage();
		}
		
	    
	    if(bool) {
	    	result="Success";
	    }
	    else {
	    	result="Failure";
	    }
		return result;
	}
	
	public String sdmsService(String Service) {
		
		Map<String, Object> map = new HashMap<>();
		
		final String url = env.getProperty("sdms.template.service.url")+Service;

	    RestTemplate restTemplate = new RestTemplate();
	    restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
	    
	    String result = restTemplate.postForObject(url, map, String.class);
	    
	    return result;
	}
	
	public boolean CheckLIMS() throws Exception {
		
		String getLIMS=limsService("materialController/getLimsStructure");
		
		if(getLIMS.equalsIgnoreCase("2")) {
			return true;
		}else {
			importLIMSMaterial();
			importLIMSMaterialTrans();
			
			return true;
		}
	}
	
	public boolean syncLimsMasters(Map<String, Object> objMap) throws Exception {
		ImportLimsTest("");
		String isAPICalling = env.getProperty("limsbaseservice.serviceapi");
		if (isAPICalling.equals("false")) {
			CheckLIMS();
		}
		//importLIMSMaterial();
		//importLIMSMaterialTrans();
		return true;
	}
	
	public String getlinkedinuserprofile(Map<String, Object> objMap) throws Exception {
		
		String url = "https://www.linkedin.com/oauth/v2/accessToken?"
                + "client_id=863mwqkv9hnwex&client_secret=yoR5dCFdmsVrFsR2&"
                + "code=" + objMap.get("code") + "&grant_type=authorization_code&"
                + "redirect_uri=http://localhost:3000/linkedin";
		
		RestTemplate restTemplate = new RestTemplate();
		@SuppressWarnings("unused")
		String token ="";
		String result = restTemplate.getForObject(url, String.class);
		try {
		     JSONObject jsonObject = new JSONObject(result);
		     token = (String) jsonObject.get("access_token");
		}catch (JSONException err){
		     
		}
		
		url ="https://api.linkedin.com/v2/userinfo";
		HttpHeaders headers = new HttpHeaders();
	headers.set("Authorization", "Bearer "+token);
		

		HttpEntity<String> entity = new HttpEntity<String>(headers);

		ResponseEntity<String> profile = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
		
		
		return profile.getBody();
	}
}
