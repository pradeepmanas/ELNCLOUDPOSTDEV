package com.agaram.eln.primary.service.dashboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.agaram.eln.primary.model.cfr.LSactivity;
import com.agaram.eln.primary.model.instrumentDetails.LSlogilablimsorderdetail;
import com.agaram.eln.primary.model.sheetManipulation.LSparsedparameters;
import com.agaram.eln.primary.model.sheetManipulation.LSsamplefile;
import com.agaram.eln.primary.model.sheetManipulation.LSworkflow;
import com.agaram.eln.primary.model.sheetManipulation.LSworkflowgroupmapping;
import com.agaram.eln.primary.model.usermanagement.LSprojectmaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.model.usermanagement.LSusersteam;
import com.agaram.eln.primary.model.usermanagement.LSuserteammapping;
import com.agaram.eln.primary.repository.cfr.LSactivityRepository;
import com.agaram.eln.primary.repository.cfr.LScfttransactionRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LSlogilablimsorderdetailRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LSparsedparametersRespository;
import com.agaram.eln.primary.repository.sheetManipulation.LSsamplefileRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LSworkflowRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LSworkflowgroupmappingRepository;
import com.agaram.eln.primary.repository.usermanagement.LSprojectmasterRepository;
import com.agaram.eln.primary.repository.usermanagement.LSuserMasterRepository;
import com.agaram.eln.primary.repository.usermanagement.LSusersteamRepository;
import com.agaram.eln.primary.repository.usermanagement.LSuserteammappingRepository;

@Service
public class DashBoardService {
	@Autowired
    private LSlogilablimsorderdetailRepository lslogilablimsorderdetailRepository;
	
	@Autowired
	private LSactivityRepository lsactivityRepository;
	
	@Autowired
	private LSsamplefileRepository lssamplefileRepository;
	
	@Autowired
	private LSparsedparametersRespository lsparsedparametersRespository;
	
	@Autowired
	private LScfttransactionRepository lscfttransactionRepository;
	
	@Autowired
	private LSusersteamRepository lsusersteamRepository;
	
	@Autowired
	private LSprojectmasterRepository lsprojectmasterRepository;
	
	@Autowired
	private LSworkflowgroupmappingRepository lsworkflowgroupmappingRepository;
	
	@Autowired
	private LSuserteammappingRepository lsuserteammappingRepository;
	
	@Autowired
	private LSworkflowRepository lsworkflowRepository;
	
	@Autowired
    private LSuserMasterRepository lsuserMasterRepository;
	
	public Map<String, Object> Getdashboarddetails(LSuserMaster objuser)
	{
		
		LSuserMaster objupdateduser = lsuserMasterRepository.findByusercode(objuser.getUsercode());
		Map<String, Object> mapOrders = new HashMap<String, Object>();
		
		List<LSsamplefile> lssamplefile = lssamplefileRepository.findByprocessed(1);
		
		if(objupdateduser.getUsername().equals("Administrator"))
		{
			mapOrders.put("orders", lslogilablimsorderdetailRepository.count());
			mapOrders.put("pendingorder", lslogilablimsorderdetailRepository.countByOrderflag("N"));
			mapOrders.put("completedorder", lslogilablimsorderdetailRepository.countByOrderflag("R"));
			mapOrders.put("onproces", lslogilablimsorderdetailRepository.countByOrderflagAndLssamplefileIn("N", lssamplefile));
			mapOrders.put("activities", lsactivityRepository.findTop20ByOrderByActivitycodeDesc());
			mapOrders.put("activitiescount",lsactivityRepository.count());
			List<LSparsedparameters> lstparsedparam = lsparsedparametersRespository.getallrecords();
			mapOrders.put("ParsedParameters", lstparsedparam);
		}
		else
		{
			long lsorder = lslogilablimsorderdetailRepository.countByFiletypeOrderByBatchcodeDesc(0);
			
			List<LSuserteammapping> lstteammap = lsuserteammappingRepository.findBylsuserMaster(objupdateduser);
			List<LSusersteam> lstteam = lsusersteamRepository.findByLsuserteammappingIn(lstteammap);
			List<LSworkflowgroupmapping> lsworkflowgroupmapping = lsworkflowgroupmappingRepository.findBylsusergroup(objupdateduser.getLsusergroup());
			List<LSprojectmaster> lstproject = lsprojectmasterRepository.findByLsusersteamIn(lstteam);
			List<LSworkflow> lsworkflow = lsworkflowRepository.findByLsworkflowgroupmappingIn(lsworkflowgroupmapping);
			
			long lstUserorder = lslogilablimsorderdetailRepository.countByLsprojectmasterInOrderByBatchcodeDesc(lstproject);
			
			long lstlimscompleted = lslogilablimsorderdetailRepository.countByFiletypeAndOrderflagOrderByBatchcodeDesc(0, "R");
//			long lstCompletedordercount = lslogilablimsorderdetailRepository.countByOrderflagAndLsprojectmasterInOrderByBatchcodeDesc("R",lstproject);
			List<Long> lstCompletedordercount = new ArrayList<Long>();
			if(lstproject != null && lstproject.size() >0)
			{
//				lstCompletedordercount = lslogilablimsorderdetailRepository.countByOrderflagAndLsprojectmasterInOrderByBatchcodeDesc("R",lstproject,objuser.getUsercode());
				lstCompletedordercount = lslogilablimsorderdetailRepository.countByOrderflagAndLsprojectmasterInOrderByBatchcodeDesc1("R",lstproject);
			}
			List<LSlogilablimsorderdetail> lstCompletedorder =  lslogilablimsorderdetailRepository.findByOrderflagAndLsprojectmasterInOrderByBatchcodeDesc("R",lstproject);
			
			List<Long> lstBatchcode = lstCompletedorder.stream().map(LSlogilablimsorderdetail::getBatchcode).collect(Collectors.toList());
			long lstlimsprocess = lslogilablimsorderdetailRepository.countByFiletypeAndOrderflagAndLssamplefileIn(0,"N", lssamplefile);
//			long orderinproject = lslogilablimsorderdetailRepository.countByOrderflagAndLssamplefileInAndLsprojectmasterIn("N", lssamplefile,lstproject);
			List<Long> orderinproject = new ArrayList<Long>();
			if(lstproject != null && lssamplefile != null && lstproject.size() >0 && lssamplefile.size()>0)
			{
//				orderinproject = lslogilablimsorderdetailRepository.countByOrderflagAndLssamplefileInAndLsprojectmasterIn("N", lssamplefile,lstproject,objuser.getUsercode());
				orderinproject = lslogilablimsorderdetailRepository.countByOrderflagAndLsprojectmasterInOrderByBatchcodeDescInprogress("N",lstproject,1,1,"N");
			}
			mapOrders.put("orders", (lsorder+lstUserorder));
			long lstlimspending = lslogilablimsorderdetailRepository.countByFiletypeAndOrderflagOrderByBatchcodeDesc(0, "N");
//			long lstpending = lslogilablimsorderdetailRepository.countByOrderflagAndLsprojectmasterInAndLsworkflowInOrderByBatchcodeDesc("N",lstproject, lsworkflow);
			List<Long> lstpending = new ArrayList<Long>();
			if(lstproject != null && lsworkflow != null && lstproject.size() >0 && lsworkflow.size()>0)
			{
//				lstpending = lslogilablimsorderdetailRepository.countByOrderflagAndLsprojectmasterInAndLsworkflowInOrderByBatchcodeDesc("N",lstproject, lsworkflow,objuser.getUsercode());
				lstpending = lslogilablimsorderdetailRepository.countByOrderflagAndLsprojectmasterInAndLsworkflowInOrderByBatchcodeDesc("N",lstproject);
			}
			
			List<LSparsedparameters> lstparsedparam = new ArrayList<LSparsedparameters>(); 
			if(lstBatchcode != null && lstBatchcode.size()>0)
			{
				lstparsedparam = lsparsedparametersRespository.getByBatchcodeIn(lstBatchcode);
			}
			mapOrders.put("pendingorder", (lstlimspending+lstpending.size()));
			mapOrders.put("completedorder", (lstlimscompleted+lstCompletedordercount.size()));
			mapOrders.put("onproces", lstlimsprocess+orderinproject.size());
			mapOrders.put("activities", lsactivityRepository.findTop20ByOrderByActivitycodeDesc());
			mapOrders.put("activitiescount",lsactivityRepository.count());
			mapOrders.put("ParsedParameters", lstparsedparam);
		}
		
		if(objuser.getObjsilentaudit() != null)
    	{
			objuser.getObjsilentaudit().setTableName("LSlogilablimsorderdetail");
    		lscfttransactionRepository.save(objuser.getObjsilentaudit());
    	}
		
		return mapOrders;
	}
	
	public List<LSactivity> GetActivitiesonLazy(LSactivity objactivities)
	{
		List<LSactivity> lstactivities = new ArrayList<LSactivity>();  
		lstactivities = lsactivityRepository.findTop20ByActivitycodeLessThanOrderByActivitycodeDesc(objactivities.getActivitycode());
		return lstactivities;
	}
}
