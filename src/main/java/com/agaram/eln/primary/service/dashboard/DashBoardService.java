package com.agaram.eln.primary.service.dashboard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.agaram.eln.primary.fetchmodel.getmasters.Repositorymaster;
import com.agaram.eln.primary.fetchmodel.getorders.Logilabordermaster;
import com.agaram.eln.primary.fetchmodel.getorders.Logilaborders;
import com.agaram.eln.primary.fetchmodel.getorders.Logilabprotocolorders;
import com.agaram.eln.primary.fetchmodel.gettemplate.Protocoltemplateget;
import com.agaram.eln.primary.fetchmodel.gettemplate.Sheettemplateget;
import com.agaram.eln.primary.model.cfr.LSactivity;
import com.agaram.eln.primary.model.instrumentDetails.LSSheetOrderStructure;
import com.agaram.eln.primary.model.instrumentDetails.LSlogilablimsorderdetail;
import com.agaram.eln.primary.model.protocols.LSprotocolmaster;
import com.agaram.eln.primary.model.sheetManipulation.LSparsedparameters;
import com.agaram.eln.primary.model.sheetManipulation.LSsamplefile;
import com.agaram.eln.primary.model.sheetManipulation.LSsamplemaster;
import com.agaram.eln.primary.model.sheetManipulation.LSworkflow;
import com.agaram.eln.primary.model.sheetManipulation.LSworkflowgroupmapping;
import com.agaram.eln.primary.model.usermanagement.LSMultiusergroup;
import com.agaram.eln.primary.model.usermanagement.LSprojectmaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.model.usermanagement.LSusersteam;
import com.agaram.eln.primary.model.usermanagement.LSuserteammapping;
import com.agaram.eln.primary.repository.cfr.LSactivityRepository;
import com.agaram.eln.primary.repository.cfr.LScfttransactionRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LSSheetOrderStructureRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LSlogilablimsorderdetailRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LsordersharedbyRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LsordersharetoRepository;
import com.agaram.eln.primary.repository.masters.LsrepositoriesRepository;
import com.agaram.eln.primary.repository.protocol.LSProtocolMasterRepository;
import com.agaram.eln.primary.repository.protocol.LSlogilabprotocoldetailRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LSfileRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LSfileversionRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LSparsedparametersRespository;
import com.agaram.eln.primary.repository.sheetManipulation.LSsamplefileRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LSsamplemasterRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LSworkflowRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LSworkflowgroupmappingRepository;
import com.agaram.eln.primary.repository.usermanagement.LSMultiusergroupRepositery;
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

	@Autowired
	private LSMultiusergroupRepositery lsMultiusergroupRepositery;

	@Autowired
	private LSfileRepository lsfileRepository;

	@Autowired
	private LSfileversionRepository lsfileversionRepository;

	@Autowired
	private LSlogilabprotocoldetailRepository LSlogilabprotocoldetailRepository;

	@Autowired
	private LSProtocolMasterRepository LSProtocolMasterRepository;

	@Autowired
	private LsordersharedbyRepository lsordersharedbyRepository;

	@Autowired
	private LsordersharetoRepository lsordersharetoRepository;

	@Autowired
	private LsrepositoriesRepository LsrepositoriesRepository;

	@Autowired
	LSuserteammappingRepository LSuserteammappingRepositoryObj;

	@Autowired
	private LSsamplemasterRepository lssamplemasterrepository;

	@Autowired
	private LSSheetOrderStructureRepository lsSheetOrderStructureRepository;

	public Map<String, Object> Getdashboarddetails(LSuserMaster objuser) {

		LSuserMaster objupdateduser = lsuserMasterRepository.findByusercode(objuser.getUsercode());
		Map<String, Object> mapOrders = new HashMap<String, Object>();
		LSMultiusergroup objLSMultiusergroup = new LSMultiusergroup();
		objLSMultiusergroup = lsMultiusergroupRepositery.findBymultiusergroupcode(objuser.getMultiusergroups());
		objupdateduser.setLsusergroup(objLSMultiusergroup.getLsusergroup());
		List<LSsamplefile> lssamplefile = lssamplefileRepository.findByprocessed(1);

		if (objupdateduser.getUsername().equals("Administrator")) {
			mapOrders.put("orders", lslogilablimsorderdetailRepository.count());
			mapOrders.put("pendingorder", lslogilablimsorderdetailRepository.countByOrderflag("N"));
			mapOrders.put("completedorder", lslogilablimsorderdetailRepository.countByOrderflag("R"));
			mapOrders.put("onproces",
					lslogilablimsorderdetailRepository.countByOrderflagAndLssamplefileIn("N", lssamplefile));
			mapOrders.put("activities", lsactivityRepository.findTop20ByOrderByActivitycodeDesc());
			mapOrders.put("activitiescount", lsactivityRepository.count());
			List<LSparsedparameters> lstparsedparam = lsparsedparametersRespository.getallrecords();
			mapOrders.put("ParsedParameters", lstparsedparam);
		} else {
			long lsorder = lslogilablimsorderdetailRepository.countByFiletypeOrderByBatchcodeDesc(0);

			List<LSuserteammapping> lstteammap = lsuserteammappingRepository.findBylsuserMaster(objupdateduser);
			List<LSusersteam> lstteam = lsusersteamRepository.findByLsuserteammappingIn(lstteammap);
			List<LSworkflowgroupmapping> lsworkflowgroupmapping = lsworkflowgroupmappingRepository
					.findBylsusergroup(objupdateduser.getLsusergroup());
			List<LSprojectmaster> lstproject = lsprojectmasterRepository.findByLsusersteamIn(lstteam);

			List<LSworkflow> lsworkflow = lsworkflowRepository
					.findByLsworkflowgroupmappingInOrderByWorkflowcodeDesc(lsworkflowgroupmapping);

			long lstUserorder = lslogilablimsorderdetailRepository
					.countByLsprojectmasterInOrderByBatchcodeDesc(lstproject);

			long lstlimscompleted = lslogilablimsorderdetailRepository
					.countByFiletypeAndOrderflagOrderByBatchcodeDesc(0, "R");
//			long lstCompletedordercount = lslogilablimsorderdetailRepository.countByOrderflagAndLsprojectmasterInOrderByBatchcodeDesc("R",lstproject);
			List<Long> lstCompletedordercount = new ArrayList<Long>();
			if (lstproject != null && lstproject.size() > 0) {

//				lstCompletedordercount = lslogilablimsorderdetailRepository.countByOrderflagAndLsprojectmasterInOrderByBatchcodeDesc("R",lstproject,objuser.getUsercode());
				lstCompletedordercount = lslogilablimsorderdetailRepository
						.countByOrderflagAndLsprojectmasterInOrderByBatchcodeDesc1("R", lstproject);
			}
			List<LSlogilablimsorderdetail> lstCompletedorder = lslogilablimsorderdetailRepository
					.findByOrderflagAndLsprojectmasterInOrderByBatchcodeDesc("R", lstproject);

			List<Long> lstBatchcode = lstCompletedorder.stream().map(LSlogilablimsorderdetail::getBatchcode)
					.collect(Collectors.toList());
			long lstlimsprocess = lslogilablimsorderdetailRepository.countByFiletypeAndOrderflagAndLssamplefileIn(0,
					"N", lssamplefile);
//			long orderinproject = lslogilablimsorderdetailRepository.countByOrderflagAndLssamplefileInAndLsprojectmasterIn("N", lssamplefile,lstproject);
			List<Long> orderinproject = new ArrayList<Long>();
			if (lstproject != null && lssamplefile != null && lstproject.size() > 0 && lssamplefile.size() > 0) {
//				orderinproject = lslogilablimsorderdetailRepository.countByOrderflagAndLssamplefileInAndLsprojectmasterIn("N", lssamplefile,lstproject,objuser.getUsercode());
				orderinproject = lslogilablimsorderdetailRepository
						.countByOrderflagAndLsprojectmasterInOrderByBatchcodeDescInprogress("N", lstproject, 1, 1, "N");
			}
			mapOrders.put("orders", (lsorder + lstUserorder));
			long lstlimspending = lslogilablimsorderdetailRepository.countByFiletypeAndOrderflagOrderByBatchcodeDesc(0,
					"N");
//			long lstpending = lslogilablimsorderdetailRepository.countByOrderflagAndLsprojectmasterInAndLsworkflowInOrderByBatchcodeDesc("N",lstproject, lsworkflow);
			List<Long> lstpending = new ArrayList<Long>();
			if (lstproject != null && lsworkflow != null && lstproject.size() > 0 && lsworkflow.size() > 0) {
//				lstpending = lslogilablimsorderdetailRepository.countByOrderflagAndLsprojectmasterInAndLsworkflowInOrderByBatchcodeDesc("N",lstproject, lsworkflow,objuser.getUsercode());
				lstpending = lslogilablimsorderdetailRepository
						.countByOrderflagAndLsprojectmasterInAndLsworkflowInOrderByBatchcodeDesc("N", lstproject);
			}

			List<LSparsedparameters> lstparsedparam = new ArrayList<LSparsedparameters>();
			if (lstBatchcode != null && lstBatchcode.size() > 0) {
				lstparsedparam = lsparsedparametersRespository.getByBatchcodeIn(lstBatchcode);
			}
			mapOrders.put("pendingorder", (lstlimspending + lstpending.size()));
			mapOrders.put("completedorder", (lstlimscompleted + lstCompletedordercount.size()));
			mapOrders.put("onproces", lstlimsprocess + orderinproject.size());
			mapOrders.put("activities", lsactivityRepository.findTop20ByOrderByActivitycodeDesc());
			mapOrders.put("activitiescount", lsactivityRepository.count());
			mapOrders.put("ParsedParameters", lstparsedparam);
		}

		if (objuser.getObjsilentaudit() != null) {
			objuser.getObjsilentaudit().setTableName("LSlogilablimsorderdetail");
			lscfttransactionRepository.save(objuser.getObjsilentaudit());
		}

		return mapOrders;
	}

	public Map<String, Object> Getdashboarddetailsonfilters(LSuserMaster objuser) {
		Date fromdate = objuser.getObjuser().getFromdate();
		Date todate = objuser.getObjuser().getTodate();
		LSuserMaster objupdateduser = lsuserMasterRepository.findByusercode(objuser.getUsercode());
		Map<String, Object> mapOrders = new HashMap<String, Object>();
		LSMultiusergroup objLSMultiusergroup = new LSMultiusergroup();
		objLSMultiusergroup = lsMultiusergroupRepositery.findBymultiusergroupcode(objuser.getMultiusergroups());
		objupdateduser.setLsusergroup(objLSMultiusergroup.getLsusergroup());
		List<LSsamplefile> lssamplefile = lssamplefileRepository.findByprocessed(1);

		if (objupdateduser.getUsername().equals("Administrator")) {
			mapOrders.put("orders",
					lslogilablimsorderdetailRepository.countByCreatedtimestampBetween(fromdate, todate));
			mapOrders.put("pendingorder", lslogilablimsorderdetailRepository
					.countByOrderflagAndCreatedtimestampBetween("N", fromdate, todate));
			mapOrders.put("completedorder", lslogilablimsorderdetailRepository
					.countByOrderflagAndCompletedtimestampBetween("R", fromdate, todate));
			mapOrders.put("onproces", lslogilablimsorderdetailRepository
					.countByOrderflagAndLssamplefileInAndCreatedtimestampBetween("N", lssamplefile, fromdate, todate));
			mapOrders.put("activities",
					lsactivityRepository.findTop20ByActivityDateBetweenOrderByActivitycodeDesc(fromdate, todate));
			mapOrders.put("activitiescount", lsactivityRepository.countByActivityDateBetween(fromdate, todate));
			List<LSparsedparameters> lstparsedparam = lsparsedparametersRespository.getallrecords();
			mapOrders.put("ParsedParameters", lstparsedparam);

			mapOrders.put("orderlst", lslogilablimsorderdetailRepository
					.findByCreatedtimestampBetweenOrderByBatchcodeDesc(fromdate, todate));
		} else {
			long lsorder = lslogilablimsorderdetailRepository.countByFiletypeAndCreatedtimestampBetween(0, fromdate,
					todate);

			List<LSuserteammapping> lstteammap = lsuserteammappingRepository.findBylsuserMaster(objupdateduser);
			List<LSusersteam> lstteam = lsusersteamRepository.findByLsuserteammappingIn(lstteammap);
			List<LSworkflowgroupmapping> lsworkflowgroupmapping = lsworkflowgroupmappingRepository
					.findBylsusergroup(objupdateduser.getLsusergroup());
			List<LSprojectmaster> lstproject = lsprojectmasterRepository.findByLsusersteamIn(lstteam);

			List<LSworkflow> lsworkflow = lsworkflowRepository
					.findByLsworkflowgroupmappingInOrderByWorkflowcodeDesc(lsworkflowgroupmapping);

			long lstUserorder = lslogilablimsorderdetailRepository
					.countByLsprojectmasterInAndCreatedtimestampBetween(lstproject, fromdate, todate);

			long lstlimscompleted = lslogilablimsorderdetailRepository
					.countByFiletypeAndOrderflagAndCreatedtimestampBetween(0, "R", fromdate, todate);
			long lstCompletedordercount = 0;
			if (lstproject != null && lstproject.size() > 0) {

				lstCompletedordercount = lslogilablimsorderdetailRepository
						.countByOrderflagAndLsprojectmasterInAndCompletedtimestampBetween("R", lstproject, fromdate,
								todate);
			}
			List<LSlogilablimsorderdetail> lstCompletedorder = lslogilablimsorderdetailRepository
					.findByOrderflagAndLsprojectmasterInAndCompletedtimestampBetweenOrderByBatchcodeDesc("R",
							lstproject, fromdate, todate);

			List<Long> lstBatchcode = lstCompletedorder.stream().map(LSlogilablimsorderdetail::getBatchcode)
					.collect(Collectors.toList());
			long lstlimsprocess = lslogilablimsorderdetailRepository
					.countByFiletypeAndOrderflagAndLssamplefileInAndCreatedtimestampBetween(0, "N", lssamplefile,
							fromdate, todate);
			List<Long> orderinproject = new ArrayList<Long>();
			if (lstproject != null && lssamplefile != null && lstproject.size() > 0 && lssamplefile.size() > 0) {
				orderinproject = lslogilablimsorderdetailRepository
						.countByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenOrderByBatchcodeDescInprogress(
								"N", lstproject, 1, 1, "N", fromdate, todate);
			}
			mapOrders.put("orders", (lsorder + lstUserorder));
			long lstlimspending = lslogilablimsorderdetailRepository
					.countByFiletypeAndOrderflagAndCreatedtimestampBetween(0, "N", fromdate, todate);
			long lstpending = 0;
			if (lstproject != null && lsworkflow != null && lstproject.size() > 0 && lsworkflow.size() > 0) {
				lstpending = lslogilablimsorderdetailRepository
						.countByOrderflagAndLsprojectmasterInAndCreatedtimestampBetween("N", lstproject, fromdate,
								todate);
			}

			List<LSparsedparameters> lstparsedparam = new ArrayList<LSparsedparameters>();
			if (lstBatchcode != null && lstBatchcode.size() > 0) {
				lstparsedparam = lsparsedparametersRespository.getByBatchcodeIn(lstBatchcode);
			}
			mapOrders.put("pendingorder", (lstlimspending + lstpending));
			mapOrders.put("completedorder", (lstlimscompleted + lstCompletedordercount));
			mapOrders.put("onproces", lstlimsprocess + orderinproject.size());
			mapOrders.put("activities",
					lsactivityRepository.findTop20ByActivityDateBetweenOrderByActivitycodeDesc(fromdate, todate));
			mapOrders.put("activitiescount", lsactivityRepository.count());
			mapOrders.put("ParsedParameters", lstparsedparam);

			mapOrders.put("orderlst",
					lslogilablimsorderdetailRepository
							.findByLsprojectmasterInAndCreatedtimestampBetweenOrderByBatchcodeDesc(lstproject, fromdate,
									todate));
		}

		if (objuser.getObjsilentaudit() != null) {
			objuser.getObjsilentaudit().setTableName("LSlogilablimsorderdetail");
			lscfttransactionRepository.save(objuser.getObjsilentaudit());
		}

		return mapOrders;
	}

	public Map<String, Object> Getdashboardordercount(LSuserMaster objuser) {
		Date fromdate = objuser.getObjuser().getFromdate();
		Date todate = objuser.getObjuser().getTodate();
		Map<String, Object> mapOrders = new HashMap<String, Object>();
//		List<LSsamplefile> lssamplefile = lssamplefileRepository.findByprocessed(1);
		List<LSprojectmaster> lstproject = objuser.getLstproject();
		Integer testcode = objuser.getTestcode();
		List<Integer> userlist = objuser.getUsernotify()!=null? objuser.getUsernotify().stream().map(LSuserMaster::getUsercode).collect(Collectors.toList()):new ArrayList<Integer>();

		if (objuser.getObjuser().getOrderfor() != 1) {
			if (lstproject != null && lstproject.size() > 0) {

				if (testcode == -1 && objuser.getLstprojectforfilter() == null) {


					mapOrders.put("orders", LSlogilabprotocoldetailRepository
							.countByLsprojectmasterInAndCreatedtimestampBetweenAndSitecodeOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatebyAndCreatedtimestampBetweenOrLsprojectmasterInAndViewoptionAndSitecodeAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndCreatebyInOrderByProtocolordercodeDesc(
									lstproject, fromdate, todate, objuser.getLssitemaster().getSitecode(), 1,
									objuser.getLssitemaster().getSitecode(), fromdate, todate, 2,
									objuser.getLssitemaster().getSitecode(), objuser.getUsercode(), fromdate, todate,
									lstproject, 3, objuser.getLssitemaster().getSitecode(), fromdate, todate,3,objuser.getLssitemaster().getSitecode(), fromdate, todate,userlist));

					mapOrders.put("pendingorder", LSlogilabprotocoldetailRepository
							.countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndOrdercancellIsNullAndRejectedIsNullOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndRejectedIsNullOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndRejectedIsNullOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndRejectedIsNullAndCreatebyInOrderByProtocolordercodeDesc(
									"N", objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject, 
									"N", objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, 
									"N", objuser.getLssitemaster().getSitecode(), 2, objuser.getUsercode(), fromdate, todate,
									"N", objuser.getLssitemaster().getSitecode(), 3, fromdate, todate,userlist));
					mapOrders.put("completedorder", LSlogilabprotocoldetailRepository
							.countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndRejectedIsNullOrOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenOrOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenOrOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyInOrderByProtocolordercodeDesc(
									"R", objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject, "R",
									objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, "R",
									objuser.getLssitemaster().getSitecode(), 2, objuser.getUsercode(), fromdate, todate,
									"R", objuser.getLssitemaster().getSitecode(), 3, fromdate, todate,userlist));


					mapOrders.put("rejectedorder", LSlogilabprotocoldetailRepository
							.countByRejectedAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInOrRejectedAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenOrRejectedAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenOrRejectedAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyInOrderByProtocolordercodeDesc(
									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject, 1,
									objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, 1,
									objuser.getLssitemaster().getSitecode(), 2, objuser.getUsercode(), fromdate, todate,
									1, objuser.getLssitemaster().getSitecode(), 3, fromdate, todate,userlist));
					mapOrders.put("canceledorder", LSlogilabprotocoldetailRepository
							.countByOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyInOrderByProtocolordercodeDesc(
									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject, 1,
									objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, 1,
									objuser.getLssitemaster().getSitecode(), 2, objuser.getUsercode(), fromdate, todate,
									1, objuser.getLssitemaster().getSitecode(), 3, fromdate, todate,userlist));

				} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {

//					mapOrders.put("orders", LSlogilabprotocoldetailRepository
//							.countBySitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndTestcode(
//									objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject, testcode));
//					mapOrders.put("pendingorder", LSlogilabprotocoldetailRepository
//							.countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterInAndOrdercancellIsNullAndTestcode(
//									"N", objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject,
//									testcode));
//
//					mapOrders.put("completedorder", LSlogilabprotocoldetailRepository
//							.countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterInAndTestcodeAndRejectedIsNull(
//									"R", objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject,
//									testcode));
//
//					mapOrders.put("rejectedorder", LSlogilabprotocoldetailRepository
//							.countByRejectedAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndTestcode(1,
//									objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject, testcode));
//
//					mapOrders.put("canceledorder", LSlogilabprotocoldetailRepository
//							.countByOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndTestcode(1,
//									objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject, testcode));

					mapOrders.put("orders", LSlogilabprotocoldetailRepository
							.countByLsprojectmasterInAndCreatedtimestampBetweenAndSitecodeAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatebyAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterInAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndTestcodeOrderByProtocolordercodeDesc(
									lstproject, fromdate, todate, objuser.getLssitemaster().getSitecode(), testcode, 1,
									objuser.getLssitemaster().getSitecode(), fromdate, todate, testcode, 2,
									objuser.getLssitemaster().getSitecode(), objuser.getUsercode(), fromdate, todate,
									testcode, lstproject, 3, objuser.getLssitemaster().getSitecode(), fromdate, todate,
									testcode));
					mapOrders.put("pendingorder", LSlogilabprotocoldetailRepository
							.countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterInAndOrdercancellIsNullAndTestcodeAndRejectedIsNullOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndTestcodeAndRejectedIsNullOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndTestcodeAndRejectedIsNullOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterInAndViewoptionAndCreatedtimestampBetweenAndTestcodeAndRejectedIsNullOrderByProtocolordercodeDesc(
									"N", objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject, testcode, 
									"N", objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, testcode, 
									"N", objuser.getLssitemaster().getSitecode(), 2, fromdate, objuser.getUsercode(), todate, testcode, 
									"N", objuser.getLssitemaster().getSitecode(), lstproject, 3, fromdate, todate, testcode));

					mapOrders.put("completedorder", LSlogilabprotocoldetailRepository
							.countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndRejectedIsNullAndTestcodeOrOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndTestcodeOrOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterInAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrderByProtocolordercodeDesc(
									"R", objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject,
									testcode, "R", objuser.getLssitemaster().getSitecode(), 1, fromdate, todate,
									testcode, "R", objuser.getLssitemaster().getSitecode(), 2, objuser.getUsercode(),
									fromdate, todate, testcode, "R", objuser.getLssitemaster().getSitecode(),
									lstproject, 3, fromdate, todate, testcode));

					mapOrders.put("rejectedorder", LSlogilabprotocoldetailRepository
							.countByRejectedAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndTestcodeOrRejectedAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrRejectedAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndTestcodeOrRejectedAndSitecodeAndLsprojectmasterInAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrderByProtocolordercodeDesc(
									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject, testcode,
									1, objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, testcode, 1,
									objuser.getLssitemaster().getSitecode(), 2, objuser.getUsercode(), fromdate, todate,
									testcode, 1, objuser.getLssitemaster().getSitecode(), lstproject, 3, fromdate,
									todate, testcode));

					mapOrders.put("canceledorder", LSlogilabprotocoldetailRepository
							.countByOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndTestcodeOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndTestcodeOrOrdercancellAndSitecodeAndLsprojectmasterInAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrderByProtocolordercodeDesc(
									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject, testcode,
									1, objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, testcode, 1,
									objuser.getLssitemaster().getSitecode(), 2, objuser.getUsercode(), fromdate, todate,
									testcode, 1, objuser.getLssitemaster().getSitecode(), lstproject, 3, fromdate,
									todate, testcode));

				} else if (testcode == -1 && objuser.getLstprojectforfilter() != null) {
					mapOrders.put("orders",
							LSlogilabprotocoldetailRepository
									.countBySitecodeAndCreatedtimestampBetweenAndLsprojectmaster(
											objuser.getLssitemaster().getSitecode(), fromdate, todate,
											objuser.getLstprojectforfilter()));
					mapOrders.put("pendingorder", LSlogilabprotocoldetailRepository
							.countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterAndOrdercancellIsNullAndRejectedIsNull(
									"N", objuser.getLssitemaster().getSitecode(), fromdate, todate, objuser.getLstprojectforfilter()));

					mapOrders.put("completedorder", LSlogilabprotocoldetailRepository
							.countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterAndRejectedIsNull(
									"R", objuser.getLssitemaster().getSitecode(), fromdate, todate,
									objuser.getLstprojectforfilter()));

					mapOrders.put("rejectedorder",
							LSlogilabprotocoldetailRepository
									.countByRejectedAndSitecodeAndCreatedtimestampBetweenAndLsprojectmaster(1,
											objuser.getLssitemaster().getSitecode(), fromdate, todate,
											objuser.getLstprojectforfilter()));

					mapOrders.put("canceledorder",
							LSlogilabprotocoldetailRepository
									.countByOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmaster(1,
											objuser.getLssitemaster().getSitecode(), fromdate, todate,
											objuser.getLstprojectforfilter()));
				} else {
					mapOrders.put("orders",
							LSlogilabprotocoldetailRepository
									.countBySitecodeAndCreatedtimestampBetweenAndLsprojectmasterAndTestcode(
											objuser.getLssitemaster().getSitecode(), fromdate, todate,
											objuser.getLstprojectforfilter(), testcode));
					mapOrders.put("pendingorder", LSlogilabprotocoldetailRepository
							.countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterAndOrdercancellIsNullAndTestcodeAndRejectedIsNull(
									"N", objuser.getLssitemaster().getSitecode(), fromdate, todate, objuser.getLstprojectforfilter(), testcode));

					mapOrders.put("completedorder", LSlogilabprotocoldetailRepository
							.countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterAndTestcodeAndRejectedIsNull(
									"R", objuser.getLssitemaster().getSitecode(), fromdate, todate,
									objuser.getLstprojectforfilter(), testcode));

					mapOrders.put("rejectedorder",
							LSlogilabprotocoldetailRepository
									.countByRejectedAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterAndTestcode(
											1, objuser.getLssitemaster().getSitecode(), fromdate, todate,
											objuser.getLstprojectforfilter(), testcode));

					mapOrders.put("canceledorder", LSlogilabprotocoldetailRepository
							.countByOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterAndTestcode(1,
									objuser.getLssitemaster().getSitecode(), fromdate, todate,
									objuser.getLstprojectforfilter(), testcode));
				}

				mapOrders.put("onproces", 0);
			} else {

				if (testcode == -1 && objuser.getLstprojectforfilter() == null) {

					mapOrders.put("orders", LSlogilabprotocoldetailRepository
                        .countByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
                            1, objuser.getLssitemaster().getSitecode(), fromdate, todate, 2,
                            objuser.getUsercode(), fromdate, todate, 3, objuser.getUsercode(), fromdate,
                            todate));
					mapOrders.put("pendingorder", LSlogilabprotocoldetailRepository
                        .countByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullAndRejectedIsNullOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullAndRejectedIsNullOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullAndRejectedIsNullOrderByProtocolordercodeDesc(
                            1, objuser.getLssitemaster().getSitecode(), fromdate, todate, "N", 2,
                            objuser.getUsercode(), fromdate, todate, "N", 3, objuser.getUsercode(), fromdate,
                            todate, "N"));
					mapOrders.put("completedorder", LSlogilabprotocoldetailRepository
                        .countByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndOrderflagAndRejectedIsNullOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrderflagAndRejectedIsNullOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrderflagAndRejectedIsNullOrderByProtocolordercodeDesc(
                            1, objuser.getLssitemaster().getSitecode(), fromdate, todate, "R", 2,
                            objuser.getUsercode(), fromdate, todate, "R", 3, objuser.getUsercode(), fromdate,
                            todate, "R"));
					mapOrders.put("rejectedorder", LSlogilabprotocoldetailRepository
                        .countByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndRejectedOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndRejectedOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndRejectedOrderByProtocolordercodeDesc(
                            1, objuser.getLssitemaster().getSitecode(), fromdate, todate, 1, 2,
                            objuser.getUsercode(), fromdate, todate, 1, 3, objuser.getUsercode(), fromdate, todate, 1));
					mapOrders.put("canceledorder", LSlogilabprotocoldetailRepository
                        .countByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndOrdercancellOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrdercancellOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrdercancellOrderByProtocolordercodeDesc(
                            1, objuser.getLssitemaster().getSitecode(), fromdate, todate, 1, 2,
                            objuser.getUsercode(), fromdate, todate, 1, 3, objuser.getUsercode(), fromdate, todate, 1));

				} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {
					mapOrders.put("orders", LSlogilabprotocoldetailRepository
                        .countByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndTestcodeOrderByProtocolordercodeDesc(
                            1, objuser.getLssitemaster().getSitecode(), fromdate, todate, testcode, 2,
                            objuser.getUsercode(), fromdate, todate, testcode, 3, objuser.getUsercode(),
                            fromdate, todate, testcode));
					mapOrders.put("pendingorder", LSlogilabprotocoldetailRepository
                        .countByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullAndTestcodeAndRejectedIsNullOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullAndTestcodeAndRejectedIsNullOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullAndTestcodeAndRejectedIsNullOrderByProtocolordercodeDesc(
                            1, objuser.getLssitemaster().getSitecode(), fromdate, todate, "N", testcode, 2,
                            objuser.getUsercode(), fromdate, todate, "N", testcode, 3, objuser.getUsercode(),
                            fromdate, todate, "N", testcode));

					mapOrders.put("completedorder", LSlogilabprotocoldetailRepository
                        .countByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndOrderflagAndRejectedIsNullAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrderflagAndRejectedIsNullAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrderflagAndRejectedIsNullAndTestcodeOrderByProtocolordercodeDesc(
                            1, objuser.getLssitemaster().getSitecode(), fromdate, todate, "R", testcode, 2,
                            objuser.getUsercode(), fromdate, todate, "R", testcode, 3, objuser.getUsercode(),
                            fromdate, todate, "R", testcode));

					mapOrders.put("rejectedorder", LSlogilabprotocoldetailRepository
                        .countByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndRejectedAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndRejectedAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndRejectedAndTestcodeOrderByProtocolordercodeDesc(
                            1, objuser.getLssitemaster().getSitecode(), fromdate, todate, 1, testcode, 2,
                            objuser.getUsercode(), fromdate, todate, 1, testcode, 3, objuser.getUsercode(), fromdate, todate, 1, testcode));

					mapOrders.put("canceledorder", LSlogilabprotocoldetailRepository
                        .countByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndOrdercancellAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrdercancellAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrdercancellAndTestcodeOrderByProtocolordercodeDesc(
                            1, objuser.getLssitemaster().getSitecode(), fromdate, todate, 1, testcode, 2,
                            objuser.getUsercode(), fromdate, todate, 1, testcode, 3, objuser.getUsercode(), fromdate, todate, 1, testcode));

				} 
				mapOrders.put("onproces", 0);
			}
		} 
//		else if (objuser.getUsername().equals("Administrator") && objuser.getObjuser().getOrderfor() == 1) {
//			if (testcode == -1 && objuser.getLstprojectforfilter() == null) {
//				mapOrders.put("orders",
//						lslogilablimsorderdetailRepository.countByCreatedtimestampBetween(fromdate, todate));
//				mapOrders.put("pendingorder", lslogilablimsorderdetailRepository
//						.countByOrderflagAndCreatedtimestampBetweenAndOrdercancellIsNull("N", fromdate, todate));
////				long lstlimscompleted = 0;
////				lstlimscompleted = lslogilablimsorderdetailRepository
////						.countByOrderflagAndCompletedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNull("R", fromdate, todate, 3);
////
////				lstlimscompleted = lstlimscompleted + lslogilablimsorderdetailRepository
////						.countByOrderflagAndFiletypeAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNull("R", 0, fromdate, todate,3);
////				mapOrders.put("completedorder", lslogilablimsorderdetailRepository
////						.countByOrderflagAndCompletedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNull("R", fromdate, todate, 3)+
////						lslogilablimsorderdetailRepository.countByOrderflagAndCompletedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNull("R", fromdate, todate));
//				
//				mapOrders.put("completedorder", lslogilablimsorderdetailRepository
//						.countByOrderflagAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNull("R", fromdate, todate, 3)+
//						lslogilablimsorderdetailRepository.countByOrderflagAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNull("R", fromdate, todate));
//
//				mapOrders.put("rejectedorder", lslogilablimsorderdetailRepository
//						.countByApprovelstatusAndCreatedtimestampBetween(3, fromdate, todate));
//				mapOrders.put("canceledorder", lslogilablimsorderdetailRepository
//						.countByOrdercancellAndCreatedtimestampBetween(1, fromdate, todate));
//				mapOrders.put("onproces",
//						lslogilablimsorderdetailRepository.countByOrderflagAndLssamplefileInAndCreatedtimestampBetween(
//								"N", lssamplefile, fromdate, todate));
////				mapOrders.put("completedorder", (lstlimscompleted));
//			} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {
//
//				mapOrders.put("orders", lslogilablimsorderdetailRepository
//						.countByCreatedtimestampBetweenAndTestcode(fromdate, todate, testcode));
//				mapOrders.put("pendingorder",
//						lslogilablimsorderdetailRepository
//								.countByOrderflagAndCreatedtimestampBetweenAndOrdercancellIsNullAndTestcode("N",
//										fromdate, todate, testcode));
////				long lstlimscompleted = 0;
////				lstlimscompleted = lslogilablimsorderdetailRepository
////						.countByOrderflagAndCompletedtimestampBetweenAndApprovelstatusNotAndTestcode("R", fromdate,
////								todate, 3, testcode);
////
////				lstlimscompleted = lstlimscompleted + lslogilablimsorderdetailRepository
////						.countByOrderflagAndFiletypeAndCreatedtimestampBetweenAndTestcode("R", 0, fromdate, todate,
////								testcode);
//				mapOrders.put("completedorder",
//						lslogilablimsorderdetailRepository
//								.countByOrderflagAndCreatedtimestampBetweenAndApprovelstatusNotAndTestcodeAndOrdercancellIsNull("R",
//										fromdate, todate, 3, testcode)+
//								lslogilablimsorderdetailRepository.countByOrderflagAndCreatedtimestampBetweenAndTestcodeAndApprovelstatusIsNullAndOrdercancellIsNull("R",
//										fromdate, todate, testcode));
//
//				mapOrders.put("rejectedorder", lslogilablimsorderdetailRepository
//						.countByApprovelstatusAndCreatedtimestampBetweenAndTestcode(3, fromdate, todate, testcode));
//				mapOrders.put("canceledorder", lslogilablimsorderdetailRepository
//						.countByOrdercancellAndCreatedtimestampBetweenAndTestcode(1, fromdate, todate, testcode));
//				mapOrders.put("onproces",
//						lslogilablimsorderdetailRepository
//								.countByOrderflagAndLssamplefileInAndCreatedtimestampBetweenAndTestcode("N",
//										lssamplefile, fromdate, todate, testcode));
////				mapOrders.put("completedorder", (lstlimscompleted));
//
//			} else if (testcode == -1 && objuser.getLstprojectforfilter() != null) {
//
//				mapOrders.put("orders",
//						lslogilablimsorderdetailRepository.countByCreatedtimestampBetweenAndLsprojectmaster(fromdate,
//								todate, objuser.getLstprojectforfilter()));
//				mapOrders.put("pendingorder",
//						lslogilablimsorderdetailRepository
//								.countByOrderflagAndCreatedtimestampBetweenAndOrdercancellIsNullAndLsprojectmaster("N",
//										fromdate, todate, objuser.getLstprojectforfilter()));
////				long lstlimscompleted = 0;
////				lstlimscompleted = lslogilablimsorderdetailRepository
////						.countByOrderflagAndCompletedtimestampBetweenAndApprovelstatusNotAndLsprojectmaster("R",
////								fromdate, todate, 3, objuser.getLstprojectforfilter());
////
////				lstlimscompleted = lstlimscompleted + lslogilablimsorderdetailRepository
////						.countByOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmaster("R", 0, fromdate,
////								todate, objuser.getLstprojectforfilter());
//				mapOrders.put("completedorder",
//						lslogilablimsorderdetailRepository
//								.countByOrderflagAndCreatedtimestampBetweenAndApprovelstatusNotAndLsprojectmasterAndOrdercancellIsNull("R",
//										fromdate, todate, 3, objuser.getLstprojectforfilter()));
//
//				mapOrders.put("rejectedorder",
//						lslogilablimsorderdetailRepository
//								.countByApprovelstatusAndCreatedtimestampBetweenAndLsprojectmaster(3, fromdate, todate,
//										objuser.getLstprojectforfilter()));
//				mapOrders.put("canceledorder",
//						lslogilablimsorderdetailRepository
//								.countByOrdercancellAndCreatedtimestampBetweenAndLsprojectmaster(1, fromdate, todate,
//										objuser.getLstprojectforfilter()));
//				mapOrders.put("onproces",
//						lslogilablimsorderdetailRepository
//								.countByOrderflagAndLssamplefileInAndCreatedtimestampBetweenAndLsprojectmaster("N",
//										lssamplefile, fromdate, todate, objuser.getLstprojectforfilter()));
////				mapOrders.put("completedorder", (lstlimscompleted));
//			} else {
//				mapOrders.put("orders",
//						lslogilablimsorderdetailRepository.countByCreatedtimestampBetweenAndLsprojectmasterAndTestcode(
//								fromdate, todate, objuser.getLstprojectforfilter(), testcode));
//				mapOrders.put("pendingorder", lslogilablimsorderdetailRepository
//						.countByOrderflagAndCreatedtimestampBetweenAndOrdercancellIsNullAndLsprojectmasterAndTestcode(
//								"N", fromdate, todate, objuser.getLstprojectforfilter(), testcode));
////				long lstlimscompleted = 0;
////				lstlimscompleted = lslogilablimsorderdetailRepository
////						.countByOrderflagAndCompletedtimestampBetweenAndApprovelstatusNotAndLsprojectmasterAndTestcodeAndOrdercancellIsNull(
////								"R", fromdate, todate, 3, objuser.getLstprojectforfilter(), testcode);
////
////				lstlimscompleted = lstlimscompleted + lslogilablimsorderdetailRepository
////						.countByOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterAndTestcodeAndOrdercancellIsNull("R", 0,
////								fromdate, todate, objuser.getLstprojectforfilter(), testcode);
//				mapOrders.put("completedorder", lslogilablimsorderdetailRepository
//						.countByOrderflagAndCreatedtimestampBetweenAndApprovelstatusNotAndLsprojectmasterAndTestcodeAndOrdercancellIsNull(
//								"R", fromdate, todate, 3, objuser.getLstprojectforfilter(), testcode));
//
//				mapOrders.put("rejectedorder",
//						lslogilablimsorderdetailRepository
//								.countByApprovelstatusAndCreatedtimestampBetweenAndLsprojectmasterAndTestcode(3,
//										fromdate, todate, objuser.getLstprojectforfilter(), testcode));
//				mapOrders.put("canceledorder",
//						lslogilablimsorderdetailRepository
//								.countByOrdercancellAndCreatedtimestampBetweenAndLsprojectmasterAndTestcode(1, fromdate,
//										todate, objuser.getLstprojectforfilter(), testcode));
//				mapOrders.put("onproces", lslogilablimsorderdetailRepository
//						.countByOrderflagAndLssamplefileInAndCreatedtimestampBetweenAndLsprojectmasterAndTestcode("N",
//								lssamplefile, fromdate, todate, objuser.getLstprojectforfilter(), testcode));
////				mapOrders.put("completedorder", (lstlimscompleted));
//			}
//
//		} 
		else {

			long lstUserorder = 0;
			long lstlimscompleted = 0;
			long lstordersinprogress = 0;
			long lstpending = 0;
			long lstRejected = 0;
			long lstCancelled = 0;
			if (lstproject != null && lstproject.size() > 0) {
				if (testcode == -1 && objuser.getLstprojectforfilter() == null) {
					if (lstproject != null && lstproject.size() > 0) {
//						lstUserorder = lslogilablimsorderdetailRepository
//								.countByLsprojectmasterInAndCreatedtimestampBetweenOrFiletypeAndCreatedtimestampBetween(
//										lstproject, fromdate, todate, 0, fromdate, todate);

						lstUserorder = lslogilablimsorderdetailRepository
								.countByLsprojectmasterInAndCreatedtimestampBetweenOrFiletypeAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenOrViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndLsprojectmasterIsNullOrderByBatchcodeDesc(
										lstproject, fromdate, todate, 0, fromdate, todate, 1, objuser, fromdate, todate,
										2, objuser, fromdate, todate, 3, objuser, fromdate, todate);


						lstlimscompleted = lslogilablimsorderdetailRepository
								.countByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullOrOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndOrdercancellIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndLsprojectmasterInAndOrdercancellIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndLsprojectmasterIsNullAndOrdercancellIsNullOrderByBatchcodeDesc(
										"R", lstproject, fromdate, todate, 3, "R", lstproject, fromdate, todate, "R", 0,
										fromdate, todate, "R", 1, objuser, fromdate, todate, 3, "R", 2, objuser,
										fromdate, todate, 3, "R", 3, objuser, fromdate, todate, 3, lstproject, "R", 1,
										objuser, fromdate, todate, "R", 2, objuser, fromdate, todate, "R", 3, objuser,
										fromdate, todate);
						
						
//						lstlimscompleted = lslogilablimsorderdetailRepository.countByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullOrOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndOrdercancellIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndLsprojectmasterInAndOrdercancellIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndLsprojectmasterInAndOrdercancellIsNullOrderByBatchcodeDesc(
//								"R", lstproject, fromdate, todate, 3, "R", lstproject, fromdate, todate, "R", 0,
//								fromdate, todate, "R", 1, objuser, fromdate, todate, 3, "R", 2, objuser,
//								fromdate, todate, 3, "R", 3, objuser, fromdate, todate, 3, lstproject, "R", 1,
//								objuser, fromdate, todate, "R", 2, objuser, fromdate, todate, "R", 3, objuser,
//								fromdate, todate, lstproject);

						

						lstordersinprogress = lslogilablimsorderdetailRepository
								.countByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndOrdercancellIsNullAndApprovelstatusAndApprovedOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndApprovelstatusAndApprovedOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenAndApprovelstatusAndApprovedOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenAndApprovelstatusAndApprovedOrOrderflagAndLsprojectmasterInAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenAndApprovelstatusAndApprovedOrderByBatchcodeDesc(
										"N", lstproject, fromdate, todate, 1, 1, "N", 0, fromdate, todate, 1, 1, "N", 1,
										objuser, fromdate, todate, 1, 1, "N", 2, objuser, fromdate, todate, 1, 1, "N",
										lstproject, 3, objuser, fromdate, todate, 1, 1);

//						lstpending = lslogilablimsorderdetailRepository
//								.countByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndOrdercancellIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetween(
//										"N", lstproject, fromdate, todate, "N", 0, fromdate, todate);

//						lstpending = lslogilablimsorderdetailRepository
//								.countByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndOrdercancellIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenOrOrderflagAndLsprojectmasterInAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenOrderByBatchcodeDesc(
//										"N", lstproject, fromdate, todate, "N", 0, fromdate, todate, "N", 1, objuser,
//										fromdate, todate, "N", 2, objuser, fromdate, todate, "N", lstproject, 3,
//										objuser, fromdate, todate);
						lstpending = lslogilablimsorderdetailRepository
								.countByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndOrdercancellIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterInAndOrdercancellIsNullAndCreatedtimestampBetweenOrderByBatchcodeDesc(
										"N", lstproject, fromdate, todate, "N", 0, fromdate, todate, "N", 1, objuser,
										fromdate, todate, "N", 2, objuser, fromdate, todate, "N", 3,
										objuser.getUsernotify(), fromdate, todate);

//						lstRejected = lslogilablimsorderdetailRepository
//								.countByApprovelstatusAndLsprojectmasterInAndCreatedtimestampBetween(3, lstproject,
//										fromdate, todate);

						lstRejected = lslogilablimsorderdetailRepository
								.countByApprovelstatusAndLsprojectmasterInAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusAndLsprojectmasterIsNull(
										3, lstproject, fromdate, todate, 1, objuser, fromdate, todate, 3, 2, objuser,
										fromdate, todate, 3, 3, objuser, fromdate, todate, 3, 3, objuser, fromdate,
										todate, 3);
//						lstCancelled = lslogilablimsorderdetailRepository
//								.countByOrdercancellAndLsprojectmasterInAndCreatedtimestampBetween(1, lstproject,
//										fromdate, todate);

						lstCancelled = lslogilablimsorderdetailRepository
								.countByOrdercancellAndLsprojectmasterInAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndLsprojectmasterIsNull(
										1, lstproject, fromdate, todate, 1, objuser, fromdate, todate, 1, 2, objuser,
										fromdate, todate, 1, 3, objuser, fromdate, todate, 1);

					}
				} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {
//		    	  AndLsprojectmaster     AndTestcode
					if (lstproject != null && lstproject.size() > 0) {
						lstUserorder = lslogilablimsorderdetailRepository
								.countByLsprojectmasterInAndCreatedtimestampBetweenAndTestcodeOrFiletypeAndCreatedtimestampBetweenAndTestcodeOrderByBatchcodeDesc(
										lstproject, fromdate, todate, testcode, 0, fromdate, todate, testcode);
						lstlimscompleted = lslogilablimsorderdetailRepository
								.countByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusNotAndTestcodeAndOrdercancellIsNullOrOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusIsNullAndTestcodeAndOrdercancellIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndTestcodeAndOrdercancellIsNullOrderByBatchcodeDesc(
										"R", lstproject, fromdate, todate, 3, testcode, "R", lstproject, fromdate,
										todate, testcode, "R", 0, fromdate, todate, testcode);
						lstordersinprogress = lslogilablimsorderdetailRepository
								.countByOrderflagAndLsprojectmasterInAndApprovelstatusAndApprovedAndCreatedtimestampBetweenAndTestcodeOrderByBatchcodeDesc(
										"N", lstproject, 1, 1, fromdate, todate, testcode);
						lstpending = lslogilablimsorderdetailRepository
								.countByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndOrdercancellIsNullAndTestcodeOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndTestcodeOrderByBatchcodeDesc(
										"N", lstproject, fromdate, todate, testcode, "N", 0, fromdate, todate,
										testcode);
						lstRejected = lslogilablimsorderdetailRepository
								.countByApprovelstatusAndLsprojectmasterInAndCreatedtimestampBetweenAndTestcode(3,
										lstproject, fromdate, todate, testcode);
						lstCancelled = lslogilablimsorderdetailRepository
								.countByOrdercancellAndLsprojectmasterInAndCreatedtimestampBetweenAndTestcode(1,
										lstproject, fromdate, todate, testcode);
					}
				} else if (testcode == -1 && objuser.getLstprojectforfilter() != null) {

					lstUserorder = lslogilablimsorderdetailRepository
							.countByLsprojectmasterAndCreatedtimestampBetweenOrFiletypeAndLsprojectmasterAndCreatedtimestampBetweenOrderByBatchcodeDesc(
									objuser.getLstprojectforfilter(), fromdate, todate, 0,
									objuser.getLstprojectforfilter(), fromdate, todate);
					lstlimscompleted = lslogilablimsorderdetailRepository
							.countByOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullOrOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterAndOrdercancellIsNullOrderByBatchcodeDesc(
									"R", objuser.getLstprojectforfilter(), fromdate, todate, 3, "R",
									objuser.getLstprojectforfilter(), fromdate, todate, "R", 0, fromdate, todate,
									objuser.getLstprojectforfilter());
					lstordersinprogress = lslogilablimsorderdetailRepository
							.countByOrderflagAndLsprojectmasterAndApprovelstatusAndApprovedAndCreatedtimestampBetweenOrderByBatchcodeDesc(
									"N", objuser.getLstprojectforfilter(), 1, 1, fromdate, todate);
					lstpending = lslogilablimsorderdetailRepository
							.countByOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndOrdercancellIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterOrderByBatchcodeDesc(
									"N", objuser.getLstprojectforfilter(), fromdate, todate, "N", 0, fromdate, todate,
									objuser.getLstprojectforfilter());
					lstRejected = lslogilablimsorderdetailRepository
							.countByApprovelstatusAndLsprojectmasterAndCreatedtimestampBetween(3,
									objuser.getLstprojectforfilter(), fromdate, todate);
					lstCancelled = lslogilablimsorderdetailRepository
							.countByOrdercancellAndLsprojectmasterAndCreatedtimestampBetween(1,
									objuser.getLstprojectforfilter(), fromdate, todate);

				} else {
					lstUserorder = lslogilablimsorderdetailRepository
							.countByLsprojectmasterAndCreatedtimestampBetweenAndTestcodeOrFiletypeAndLsprojectmasterAndCreatedtimestampBetweenAndTestcodeOrderByBatchcodeDesc(
									objuser.getLstprojectforfilter(), fromdate, todate, testcode, 0,
									objuser.getLstprojectforfilter(), fromdate, todate, testcode);
					lstlimscompleted = lslogilablimsorderdetailRepository
							.countByOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndApprovelstatusNotAndTestcodeAndOrdercancellIsNullOrOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndTestcodeAndOrdercancellIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterAndTestcodeAndOrdercancellIsNullOrderByBatchcodeDesc(
									"R", objuser.getLstprojectforfilter(), fromdate, todate, 3, testcode, "R",
									objuser.getLstprojectforfilter(), fromdate, todate, testcode, "R", 0, fromdate,
									todate, objuser.getLstprojectforfilter(), testcode);
					lstordersinprogress = lslogilablimsorderdetailRepository
							.countByOrderflagAndLsprojectmasterAndApprovelstatusAndApprovedAndCreatedtimestampBetweenOrderByBatchcodeDesc(
									"N", objuser.getLstprojectforfilter(), 1, 1, fromdate, todate);
					lstpending = lslogilablimsorderdetailRepository
							.countByOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndOrdercancellIsNullAndTestcodeOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterAndTestcodeOrderByBatchcodeDesc(
									"N", objuser.getLstprojectforfilter(), fromdate, todate, testcode, "N", 0, fromdate,
									todate, objuser.getLstprojectforfilter(), testcode);
					lstRejected = lslogilablimsorderdetailRepository
							.countByApprovelstatusAndLsprojectmasterAndCreatedtimestampBetweenAndTestcode(3,
									objuser.getLstprojectforfilter(), fromdate, todate, testcode);
					lstCancelled = lslogilablimsorderdetailRepository
							.countByOrdercancellAndLsprojectmasterAndCreatedtimestampBetweenAndTestcode(1,
									objuser.getLstprojectforfilter(), fromdate, todate, testcode);
				}

			} else {
				List<LSsamplemaster> lstsample = lssamplemasterrepository
						.findByLssitemasterAndStatus(objuser.getLssitemaster(), 1);
				List<LSSheetOrderStructure> lstdir = lsSheetOrderStructureRepository
						.findBySitemasterAndViewoptionOrCreatedbyAndViewoptionOrderByDirectorycode(
								objuser.getLssitemaster(), 1, objuser, 2);
				List<Long> directorycode = lstdir.stream().map(LSSheetOrderStructure::getDirectorycode)
						.collect(Collectors.toList());
				if (testcode == -1 && objuser.getLstprojectforfilter() == null) {
					lstUserorder = lslogilablimsorderdetailRepository
							.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterOrderByBatchcodeDesc(
									lstsample, 1, fromdate, todate, lstsample, 2, fromdate, todate, objuser, 3,
									fromdate, todate, objuser, directorycode, 1, fromdate, todate, directorycode, 2,
									fromdate, todate, objuser, directorycode, 3, fromdate, todate, objuser);
//			
//					lstlimscompleted = lslogilablimsorderdetailRepository
//							.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrderByBatchcodeDesc(
//									lstsample, 1, fromdate, todate, "R", 3, lstsample, 2, fromdate, todate, objuser,
//									"R", 3, 3, fromdate, todate, objuser, "R", 3, directorycode, 1, fromdate, todate,
//									"R", 3, directorycode, 2, fromdate, todate, objuser, "R", 3, directorycode, 3,
//									fromdate, todate, objuser, "R", 3);

					
					lstlimscompleted = lslogilablimsorderdetailRepository.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullOrderByBatchcodeDesc(
							 lstsample, 1, fromdate, todate, "R", 3,lstsample, 1, fromdate, todate, "R",lstsample, 2, fromdate, todate, objuser,"R", 3,lstsample, 2, fromdate, todate, objuser, "R",3, fromdate, todate, objuser, "R", 3, 3, fromdate, todate, objuser, "R",directorycode, 1, fromdate, todate,"R", 3,directorycode, 1, fromdate, todate, "R",directorycode, 2, fromdate, todate, objuser, "R", 3,directorycode, 2, fromdate, todate, objuser, "R",directorycode, 3,fromdate, todate, objuser, "R", 3,directorycode, 3, fromdate, todate, objuser, "R");

					

					lstordersinprogress = lslogilablimsorderdetailRepository
							.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullAndApprovelstatusAndApprovedOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndApprovelstatusAndApprovedOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndApprovelstatusAndApprovedOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullAndApprovelstatusAndApprovedOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndApprovelstatusAndApprovedOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndApprovelstatusAndApprovedOrderByBatchcodeDesc(
									lstsample, 1, fromdate, todate, "N", 1, 1, lstsample, 2, fromdate, todate, objuser,
									"N", 1, 1, 3, fromdate, todate, objuser, "N", 1, 1, directorycode, 1, fromdate,
									todate, "N", 1, 1, directorycode, 2, fromdate, todate, objuser, "N", 1, 1,
									directorycode, 3, fromdate, todate, objuser, "N", 1, 1);

					lstpending = lslogilablimsorderdetailRepository
							.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullOrderByBatchcodeDesc(
									lstsample, 1, fromdate, todate, "N", lstsample, 2, fromdate, todate, objuser, "N",
									3, fromdate, todate, objuser, "N", directorycode, 1, fromdate, todate, "N",
									directorycode, 2, fromdate, todate, objuser, "N", directorycode, 3, fromdate,
									todate, objuser, "N");
					lstRejected = lslogilablimsorderdetailRepository
							.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndApprovelstatusOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndApprovelstatusOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusOrderByBatchcodeDesc(
									lstsample, 1, fromdate, todate, 3, lstsample, 2, fromdate, todate, objuser, 3, 3,
									fromdate, todate, objuser, 3, directorycode, 1, fromdate, todate, 3, directorycode,
									2, fromdate, todate, objuser, 3, directorycode, 3, fromdate, todate, objuser, 3);

					lstCancelled = lslogilablimsorderdetailRepository
							.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrdercancellOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrdercancellOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellOrderByBatchcodeDesc(
									lstsample, 1, fromdate, todate, 1, lstsample, 2, fromdate, todate, objuser, 1, 3,
									fromdate, todate, objuser, 1, directorycode, 1, fromdate, todate, 1, directorycode,
									2, fromdate, todate, objuser, 1, directorycode, 3, fromdate, todate, objuser, 1);
				} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {

					lstUserorder = lslogilablimsorderdetailRepository
							.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTestcodeOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTestcodeOrderByBatchcodeDesc(
									lstsample, 1, fromdate, todate, testcode, lstsample, 2, fromdate, todate, objuser,
									testcode, 3, fromdate, todate, objuser, testcode, directorycode, 1, fromdate,
									todate, testcode, directorycode, 2, fromdate, todate, objuser, testcode,
									directorycode, 3, fromdate, todate, objuser, testcode);
//					lstlimscompleted = lslogilablimsorderdetailRepository
//							.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusNotAndTestcodeAndOrdercancellIsNullOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndTestcodeAndOrdercancellIsNullOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndTestcodeAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusNotAndTestcodeAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndTestcodeAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndTestcodeAndOrdercancellIsNullOrderByBatchcodeDesc(
//									lstsample, 1, fromdate, todate, "R", 3, testcode, lstsample, 2, fromdate, todate,
//									objuser, "R", 3, testcode, 3, fromdate, todate, objuser, "R", 3, testcode,
//									directorycode, 1, fromdate, todate, "R", 3, testcode, directorycode, 2, fromdate,
//									todate, objuser, "R", 3, testcode, directorycode, 3, fromdate, todate, objuser, "R",
//									3, testcode);
					
					
					lstlimscompleted = lslogilablimsorderdetailRepository.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullAndTestcodeOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullAndTestcodeOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullAndTestcodeOrderByBatchcodeDesc(
					    lstsample, 1, fromdate, todate, "R", 3, testcode,
					    lstsample, 1, fromdate, todate, "R", testcode,
					    lstsample, 2, fromdate, todate, objuser, "R", 3, testcode,
					    lstsample, 2, fromdate, todate, objuser, "R", testcode,
					    3, fromdate, todate, objuser, "R", 3, testcode,
					    3, fromdate, todate, objuser, "R", testcode,
					    directorycode, 1, fromdate, todate, "R", 3, testcode,
					    directorycode, 1, fromdate, todate, "R", testcode,
					    directorycode, 2, fromdate, todate, objuser, "R", 3, testcode,
					    directorycode, 2, fromdate, todate, objuser, "R", testcode,
					    directorycode, 3, fromdate, todate, objuser, "R", 3, testcode,
					    directorycode, 3, fromdate, todate, objuser, "R", testcode);
					
					
					

//					lstordersinprogress = lslogilablimsorderdetailRepository
//							.countByOrderflagAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusAndApprovedAndTestcodeOrOrderflagAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusAndApprovedAndTestcodeOrOrderflagAndOrdercancellIsNullAndLsprojectmasterIsNullAndLsuserMasterIsNotAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndApprovelstatusAndApprovedAndTestcodeOrOrderflagAndOrdercancellIsNullAndLsprojectmasterIsNullAndLsuserMasterIsNotAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndApprovelstatusAndApprovedAndTestcodeOrderByBatchcodeDesc(
//									"N", 1, objuser, fromdate, todate, 1, 1, testcode, "N", 2, objuser, fromdate,
//									todate, 1, 1, testcode, "N", objuser, lstsample, 1, fromdate, todate, 1, 1,
//									testcode, "N", objuser, lstsample, 2, fromdate, todate, 1, 1, testcode);

					lstordersinprogress = lslogilablimsorderdetailRepository
							.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullAndApprovelstatusAndApprovedAndTestcodeOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndApprovelstatusAndApprovedAndTestcodeOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndApprovelstatusAndApprovedAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullAndApprovelstatusAndApprovedAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndApprovelstatusAndApprovedAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndApprovelstatusAndApprovedAndTestcodeOrderByBatchcodeDesc(
									lstsample, 1, fromdate, todate, "N", 1, 1, testcode, lstsample, 2, fromdate, todate,
									objuser, "N", 1, 1, testcode, 3, fromdate, todate, objuser, "N", 1, 1, testcode,
									directorycode, 1, fromdate, todate, "N", 1, 1, testcode, directorycode, 2, fromdate,
									todate, objuser, "N", 1, 1, testcode, directorycode, 3, fromdate, todate, objuser,
									"N", 1, 1, testcode);

					lstpending = lslogilablimsorderdetailRepository
							.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndTestcodeOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndTestcodeOrderByBatchcodeDesc(
									lstsample, 1, fromdate, todate, "N", testcode, lstsample, 2, fromdate, todate,
									objuser, "N", testcode, 3, fromdate, todate, objuser, "N", testcode, directorycode,
									1, fromdate, todate, "N", testcode, directorycode, 2, fromdate, todate, objuser,
									"N", testcode, directorycode, 3, fromdate, todate, objuser, "N", testcode);
					lstRejected = lslogilablimsorderdetailRepository
							.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndApprovelstatusAndTestcodeOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusAndTestcodeOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndApprovelstatusAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusAndTestcodeOrderByBatchcodeDesc(
									lstsample, 1, fromdate, todate, 3, testcode, lstsample, 2, fromdate, todate,
									objuser, 3, testcode, 3, fromdate, todate, objuser, 3, testcode, directorycode, 1,
									fromdate, todate, 3, testcode, directorycode, 2, fromdate, todate, objuser, 3,
									testcode, directorycode, 3, fromdate, todate, objuser, 3, testcode);

					lstCancelled = lslogilablimsorderdetailRepository
							.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrdercancellAndTestcodeOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellAndTestcodeOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrdercancellAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellAndTestcodeOrderByBatchcodeDesc(
									lstsample, 1, fromdate, todate, 1, testcode, lstsample, 2, fromdate, todate,
									objuser, 1, testcode, 3, fromdate, todate, objuser, 1, testcode, directorycode, 1,
									fromdate, todate, 1, testcode, directorycode, 2, fromdate, todate, objuser, 1,
									testcode, directorycode, 3, fromdate, todate, objuser, 1, testcode);

				}
			}

			mapOrders.put("orders", (lstUserorder));
			mapOrders.put("pendingorder", (lstpending));
			mapOrders.put("completedorder", (lstlimscompleted));
			mapOrders.put("onproces", lstordersinprogress);
			mapOrders.put("rejectedorder", lstRejected);
			mapOrders.put("canceledorder", lstCancelled);
		}

		return mapOrders;
	}

	public Map<String, Object> Getdashboardorders(LSuserMaster objuser) {
		Date fromdate = objuser.getObjuser().getFromdate();
		Date todate = objuser.getObjuser().getTodate();
		Map<String, Object> mapOrders = new HashMap<String, Object>();
		Integer testcode = objuser.getTestcode();
		List<LSsamplefile> lssamplefile = lssamplefileRepository.findByprocessed(1);
		Pageable pageable = new PageRequest(objuser.getPagesize(), objuser.getPageperorder());
		long count = 0;

//		if (objuser.getUsername().equals("Administrator")) {
//			List<LSlogilablimsorderdetail> obj = new ArrayList<LSlogilablimsorderdetail>();
//			if (objuser.getObjuser().getOrderselectiontype() == 1) {
//				if (testcode == -1 && objuser.getLstprojectforfilter() == null) {
//					obj = lslogilablimsorderdetailRepository.findByCreatedtimestampBetweenOrderByBatchcodeDesc(fromdate,
//							todate, pageable);
//					mapOrders.put("orderlst", obj);
//					count = lslogilablimsorderdetailRepository
//							.countByCreatedtimestampBetweenOrderByBatchcodeDesc(fromdate, todate);
//				} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {
//					obj = lslogilablimsorderdetailRepository
//							.findByCreatedtimestampBetweenAndTestcodeOrderByBatchcodeDesc(fromdate, todate, testcode,
//									pageable);
//					mapOrders.put("orderlst", obj);
//					count = lslogilablimsorderdetailRepository
//							.countByCreatedtimestampBetweenAndTestcodeOrderByBatchcodeDesc(fromdate, todate, testcode);
//				} else if (testcode == -1 && objuser.getLstprojectforfilter() != null) {
//					obj = lslogilablimsorderdetailRepository
//							.findByCreatedtimestampBetweenAndLsprojectmasterOrderByBatchcodeDesc(fromdate, todate,
//									objuser.getLstprojectforfilter(), pageable);
//					mapOrders.put("orderlst", obj);
//					count = lslogilablimsorderdetailRepository
//							.countByCreatedtimestampBetweenAndLsprojectmasterOrderByBatchcodeDesc(fromdate, todate,
//									objuser.getLstprojectforfilter());
//				} else {
//					obj = lslogilablimsorderdetailRepository
//							.findByCreatedtimestampBetweenAndLsprojectmasterAndTestcodeOrderByBatchcodeDesc(fromdate,
//									todate, objuser.getLstprojectforfilter(), testcode, pageable);
//					mapOrders.put("orderlst", obj);
//					count = lslogilablimsorderdetailRepository
//							.countByCreatedtimestampBetweenAndLsprojectmasterAndTestcodeOrderByBatchcodeDesc(fromdate,
//									todate, objuser.getLstprojectforfilter(), testcode);
//				}
//
//			} else if (objuser.getObjuser().getOrderselectiontype() == 2) {
//				List<Logilabordermaster> lstCompleted = new ArrayList<Logilabordermaster>();
//
//				if (testcode == -1 && objuser.getLstprojectforfilter() == null) {
//					lstCompleted = lslogilablimsorderdetailRepository
//							.findByOrderflagAndApprovelstatusNotAndCreatedtimestampBetweenAndOrdercancellIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenOrderByBatchcodeDesc(
//									"R", 3, fromdate, todate, "R", 0, fromdate, todate, pageable);
//
//					count = lslogilablimsorderdetailRepository
//							.countByOrderflagAndApprovelstatusNotAndCreatedtimestampBetweenAndOrdercancellIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenOrderByBatchcodeDesc(
//									"R", 3, fromdate, todate, "R", 0, fromdate, todate);
//
//				} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {
//
//					lstCompleted = lslogilablimsorderdetailRepository
//							.findByOrderflagAndApprovelstatusNotAndCreatedtimestampBetweenAndOrdercancellIsNullAndTestcodeOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndTestcodeOrderByBatchcodeDesc(
//									"R", 3, fromdate, todate, testcode, "R", 0, fromdate, todate, testcode, pageable);
//
//					count = lslogilablimsorderdetailRepository
//							.countByOrderflagAndApprovelstatusNotAndCreatedtimestampBetweenAndOrdercancellIsNullAndTestcodeOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndTestcodeOrderByBatchcodeDesc(
//									"R", 3, fromdate, todate, testcode, "R", 0, fromdate, todate, testcode);
//				} else if (testcode == -1 && objuser.getLstprojectforfilter() != null) {
//					lstCompleted = lslogilablimsorderdetailRepository
//							.findByOrderflagAndApprovelstatusNotAndCreatedtimestampBetweenAndOrdercancellIsNullAndLsprojectmasterOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterOrderByBatchcodeDesc(
//									"R", 3, fromdate, todate, objuser.getLstprojectforfilter(), "R", 0, fromdate,
//									todate, objuser.getLstprojectforfilter(), pageable);
//
//					count = lslogilablimsorderdetailRepository
//							.countByOrderflagAndApprovelstatusNotAndCreatedtimestampBetweenAndOrdercancellIsNullAndLsprojectmasterOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterOrderByBatchcodeDesc(
//									"R", 3, fromdate, todate, objuser.getLstprojectforfilter(), "R", 0, fromdate,
//									todate, objuser.getLstprojectforfilter());
//				} else {
//					lstCompleted = lslogilablimsorderdetailRepository
//							.findByOrderflagAndApprovelstatusNotAndCreatedtimestampBetweenAndOrdercancellIsNullAndLsprojectmasterAndTestcodeOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterAndTestcodeOrderByBatchcodeDesc(
//									"R", 3, fromdate, todate, objuser.getLstprojectforfilter(), testcode, "R", 0,
//									fromdate, todate, objuser.getLstprojectforfilter(), testcode, pageable);
//
//					count = lslogilablimsorderdetailRepository
//							.countByOrderflagAndApprovelstatusNotAndCreatedtimestampBetweenAndOrdercancellIsNullAndLsprojectmasterAndTestcodeOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterAndTestcodeOrderByBatchcodeDesc(
//									"R", 3, fromdate, todate, objuser.getLstprojectforfilter(), testcode, "R", 0,
//									fromdate, todate, objuser.getLstprojectforfilter(), testcode);
//				}
//				mapOrders.put("orderlst", lstCompleted);
//			} else if (objuser.getObjuser().getOrderselectiontype() == 3) {
//				if (testcode == -1 && objuser.getLstprojectforfilter() == null) {
//					mapOrders.put("orderlst", lslogilablimsorderdetailRepository
//							.findByOrderflagAndCreatedtimestampBetweenAndOrdercancellIsNullOrOrderflagAndCreatedtimestampBetweenAndFiletypeOrderByBatchcodeDesc(
//									"N", fromdate, todate, "N", fromdate, todate, 0, pageable));
//					count = lslogilablimsorderdetailRepository
//							.countByOrderflagAndCreatedtimestampBetweenAndOrdercancellIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenOrderByBatchcodeDesc(
//									"N", fromdate, todate, "N", 0, fromdate, todate);
//				} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {
//					mapOrders.put("orderlst", lslogilablimsorderdetailRepository
//							.findByOrderflagAndCreatedtimestampBetweenAndOrdercancellIsNullAndTestcodeOrOrderflagAndCreatedtimestampBetweenAndFiletypeAndTestcodeOrderByBatchcodeDesc(
//									"N", fromdate, todate, testcode, "N", fromdate, todate, 0, testcode, pageable));
//					count = lslogilablimsorderdetailRepository
//							.countByOrderflagAndCreatedtimestampBetweenAndOrdercancellIsNullAndTestcodeOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndTestcodeOrderByBatchcodeDesc(
//									"N", fromdate, todate, testcode, "N", 0, fromdate, todate, testcode);
//				} else if (testcode == -1 && objuser.getLstprojectforfilter() != null) {
//					mapOrders.put("orderlst", lslogilablimsorderdetailRepository
//							.findByOrderflagAndCreatedtimestampBetweenAndOrdercancellIsNullAndLsprojectmasterOrOrderflagAndCreatedtimestampBetweenAndFiletypeAndLsprojectmasterOrderByBatchcodeDesc(
//									"N", fromdate, todate, objuser.getLstprojectforfilter(), "N", fromdate, todate, 0,
//									objuser.getLstprojectforfilter(), pageable));
//					count = lslogilablimsorderdetailRepository
//							.countByOrderflagAndCreatedtimestampBetweenAndOrdercancellIsNullAndLsprojectmasterOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterOrderByBatchcodeDesc(
//									"N", fromdate, todate, objuser.getLstprojectforfilter(), "N", 0, fromdate, todate,
//									objuser.getLstprojectforfilter());
//
//				} else {
//					mapOrders.put("orderlst", lslogilablimsorderdetailRepository
//							.findByOrderflagAndCreatedtimestampBetweenAndOrdercancellIsNullAndLsprojectmasterAndTestcodeOrOrderflagAndCreatedtimestampBetweenAndFiletypeAndLsprojectmasterAndTestcodeOrderByBatchcodeDesc(
//									"N", fromdate, todate, objuser.getLstprojectforfilter(), testcode, "N", fromdate,
//									todate, 0, objuser.getLstprojectforfilter(), testcode, pageable));
//					count = lslogilablimsorderdetailRepository
//							.countByOrderflagAndCreatedtimestampBetweenAndOrdercancellIsNullAndLsprojectmasterAndTestcodeOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterAndTestcodeOrderByBatchcodeDesc(
//									"N", fromdate, todate, objuser.getLstprojectforfilter(), testcode, "N", 0, fromdate,
//									todate, objuser.getLstprojectforfilter(), testcode);
//				}
//			} else if (objuser.getObjuser().getOrderselectiontype() == 4) {
//				mapOrders.put("orderlst",
//						lslogilablimsorderdetailRepository
//								.findByOrderflagAndLssamplefileInAndCreatedtimestampBetweenOrderByBatchcodeDesc("N",
//										lssamplefile, fromdate, todate, pageable));
//				count = lslogilablimsorderdetailRepository
//						.countByOrderflagAndLssamplefileInAndCreatedtimestampBetweenOrderByBatchcodeDesc("N",
//								lssamplefile, fromdate, todate);
//			} else if (objuser.getObjuser().getOrderselectiontype() == 5) {
//
//				if (testcode == -1 && objuser.getLstprojectforfilter() == null) {
//					mapOrders.put("orderlst",
//							lslogilablimsorderdetailRepository
//									.findByCreatedtimestampBetweenAndApprovelstatusOrderByBatchcodeDesc(fromdate,
//											todate, 3, pageable));
//					count = lslogilablimsorderdetailRepository
//							.countByCreatedtimestampBetweenAndApprovelstatusOrderByBatchcodeDesc(fromdate, todate, 3);
//				} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {
//					mapOrders.put("orderlst",
//							lslogilablimsorderdetailRepository
//									.findByCreatedtimestampBetweenAndApprovelstatusAndTestcodeOrderByBatchcodeDesc(
//											fromdate, todate, 3, testcode, pageable));
//					count = lslogilablimsorderdetailRepository
//							.countByCreatedtimestampBetweenAndApprovelstatusAndTestcodeOrderByBatchcodeDesc(fromdate,
//									todate, 3, testcode);
//				} else if (testcode == -1 && objuser.getLstprojectforfilter() != null) {
//					mapOrders.put("orderlst", lslogilablimsorderdetailRepository
//							.findByCreatedtimestampBetweenAndApprovelstatusAndLsprojectmasterOrderByBatchcodeDesc(
//									fromdate, todate, 3, objuser.getLstprojectforfilter(), pageable));
//					count = lslogilablimsorderdetailRepository
//							.countByCreatedtimestampBetweenAndApprovelstatusAndLsprojectmasterOrderByBatchcodeDesc(
//									fromdate, todate, 3, objuser.getLstprojectforfilter());
//				} else {
//					mapOrders.put("orderlst", lslogilablimsorderdetailRepository
//							.findByCreatedtimestampBetweenAndApprovelstatusAndLsprojectmasterAndTestcodeOrderByBatchcodeDesc(
//									fromdate, todate, 3, objuser.getLstprojectforfilter(), testcode, pageable));
//					count = lslogilablimsorderdetailRepository
//							.countByCreatedtimestampBetweenAndApprovelstatusAndLsprojectmasterAndTestcodeOrderByBatchcodeDesc(
//									fromdate, todate, 3, objuser.getLstprojectforfilter(), testcode);
//
//				}
//			} else if (objuser.getObjuser().getOrderselectiontype() == 6) {
//				if (testcode == -1 && objuser.getLstprojectforfilter() == null) {
//					mapOrders.put("orderlst",
//							lslogilablimsorderdetailRepository
//									.findByCreatedtimestampBetweenAndOrdercancellOrderByBatchcodeDesc(fromdate, todate,
//											1, pageable));
//
//					count = lslogilablimsorderdetailRepository
//							.countByCreatedtimestampBetweenAndOrdercancellOrderByBatchcodeDesc(fromdate, todate, 1);
//				} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {
//					mapOrders.put("orderlst",
//							lslogilablimsorderdetailRepository
//									.findByCreatedtimestampBetweenAndOrdercancellAndTestcodeOrderByBatchcodeDesc(
//											fromdate, todate, 1, testcode, pageable));
//
//					count = lslogilablimsorderdetailRepository
//							.countByCreatedtimestampBetweenAndOrdercancellAndTestcodeOrderByBatchcodeDesc(fromdate,
//									todate, 1, testcode);
//				} else if (testcode == -1 && objuser.getLstprojectforfilter() != null) {
//					mapOrders.put("orderlst",
//							lslogilablimsorderdetailRepository
//									.findByCreatedtimestampBetweenAndOrdercancellAndLsprojectmasterOrderByBatchcodeDesc(
//											fromdate, todate, 1, objuser.getLstprojectforfilter(), pageable));
//					count = lslogilablimsorderdetailRepository
//							.countByCreatedtimestampBetweenAndOrdercancellAndLsprojectmasterOrderByBatchcodeDesc(
//									fromdate, todate, 1, objuser.getLstprojectforfilter());
//
//				} else {
//					mapOrders.put("orderlst", lslogilablimsorderdetailRepository
//							.findByCreatedtimestampBetweenAndOrdercancellAndLsprojectmasterAndTestcodeOrderByBatchcodeDesc(
//									fromdate, todate, 1, objuser.getLstprojectforfilter(), testcode, pageable));
//					count = lslogilablimsorderdetailRepository
//							.countByCreatedtimestampBetweenAndOrdercancellAndLsprojectmasterAndTestcodeOrderByBatchcodeDesc(
//									fromdate, todate, 1, objuser.getLstprojectforfilter(), testcode);
//
//				}
//
//			}
//		} else {

			List<LSprojectmaster> lstproject = objuser.getLstproject();
			List<Logilabordermaster> lstorders = new ArrayList<Logilabordermaster>();
			List<LSworkflow> lstworkflow = objuser.getLstworkflow();
			if (lstproject != null) {

				if (objuser.getObjuser().getOrderselectiontype() == 1) {

					if (testcode == -1 && objuser.getLstprojectforfilter() == null) {
						lstorders = lslogilablimsorderdetailRepository
								.findByLsprojectmasterInAndCreatedtimestampBetweenOrFiletypeAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenOrViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndLsprojectmasterIsNullOrderByBatchcodeDesc(
										lstproject, fromdate, todate, 0, fromdate, todate, 1, objuser, fromdate, todate,
										2, objuser, fromdate, todate, 3, objuser, fromdate, todate,
										pageable);
						count = lslogilablimsorderdetailRepository
								.countByLsprojectmasterInAndCreatedtimestampBetweenOrFiletypeAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenOrViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndLsprojectmasterIsNullOrderByBatchcodeDesc(
										lstproject, fromdate, todate, 0, fromdate, todate, 1, objuser, fromdate, todate,
										2, objuser, fromdate, todate, 3, objuser, fromdate, todate);
					} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {
//						lstorders = lslogilablimsorderdetailRepository
//								.findByLsprojectmasterInAndCreatedtimestampBetweenAndTestcodeOrFiletypeAndCreatedtimestampBetweenAndTestcodeOrderByBatchcodeDesc(
//										lstproject, fromdate, todate, testcode, 0, fromdate, todate, testcode,
//										pageable);
//						count = lslogilablimsorderdetailRepository
//								.countByLsprojectmasterInAndCreatedtimestampBetweenAndTestcodeOrFiletypeAndCreatedtimestampBetweenAndTestcodeOrderByBatchcodeDesc(
//										lstproject, fromdate, todate, testcode, 0, fromdate, todate, testcode);
						lstorders = lslogilablimsorderdetailRepository
								.findByLsprojectmasterInAndCreatedtimestampBetweenAndTestcodeOrFiletypeAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndTestcodeOrderByBatchcodeDesc(
										lstproject, fromdate, todate, testcode, 0, fromdate, todate, testcode, 1,
										objuser, fromdate, todate, testcode, 2, objuser, fromdate, todate, testcode, 3,
										objuser, fromdate, todate, testcode, pageable);
						count = lslogilablimsorderdetailRepository
								.countByLsprojectmasterInAndCreatedtimestampBetweenAndTestcodeOrFiletypeAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndTestcodeOrderByBatchcodeDesc(
										lstproject, fromdate, todate, testcode, 0, fromdate, todate, testcode, 1,
										objuser, fromdate, todate, testcode, 2, objuser, fromdate, todate, testcode, 3,
										objuser, fromdate, todate, testcode);

					} else if (testcode == -1 && objuser.getLstprojectforfilter() != null) {
						lstorders = lslogilablimsorderdetailRepository
								.findByLsprojectmasterAndCreatedtimestampBetweenOrFiletypeAndLsprojectmasterAndCreatedtimestampBetweenOrderByBatchcodeDesc(
										objuser.getLstprojectforfilter(), fromdate, todate, 0,
										objuser.getLstprojectforfilter(), fromdate, todate, pageable);
						count = lslogilablimsorderdetailRepository
								.countByLsprojectmasterAndCreatedtimestampBetweenOrFiletypeAndLsprojectmasterAndCreatedtimestampBetweenOrderByBatchcodeDesc(
										objuser.getLstprojectforfilter(), fromdate, todate, 0,
										objuser.getLstprojectforfilter(), fromdate, todate);

					} else {
						lstorders = lslogilablimsorderdetailRepository
								.findByLsprojectmasterAndCreatedtimestampBetweenAndTestcodeOrFiletypeAndLsprojectmasterAndCreatedtimestampBetweenAndTestcodeOrderByBatchcodeDesc(
										objuser.getLstprojectforfilter(), fromdate, todate, testcode, 0,
										objuser.getLstprojectforfilter(), fromdate, todate, testcode, pageable);
						count = lslogilablimsorderdetailRepository
								.countByLsprojectmasterAndCreatedtimestampBetweenAndTestcodeOrFiletypeAndLsprojectmasterAndCreatedtimestampBetweenAndTestcodeOrderByBatchcodeDesc(
										objuser.getLstprojectforfilter(), fromdate, todate, testcode, 0,
										objuser.getLstprojectforfilter(), fromdate, todate, testcode);

					}

				} else if (objuser.getObjuser().getOrderselectiontype() == 2) {
					if (testcode == -1 && objuser.getLstprojectforfilter() == null) {

//						lstorders = lslogilablimsorderdetailRepository
//								.findByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusNotOrOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenOrderByBatchcodeDesc(
//										"R", lstproject, fromdate, todate, 3, "R", lstproject, fromdate, todate, "R", 0,
//										fromdate, todate, pageable);
//						count = lslogilablimsorderdetailRepository
//								.countByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusNotOrOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenOrderByBatchcodeDesc(
//										"R", lstproject, fromdate, todate, 3, "R", lstproject, fromdate, todate, "R", 0,
//										fromdate, todate);

						lstorders = lslogilablimsorderdetailRepository
								.findByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullOrOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndLsprojectmasterInAndOrdercancellIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndLsprojectmasterIsNullAndOrdercancellIsNullOrderByBatchcodeDesc(
										"R", lstproject, fromdate, todate, 3, "R", lstproject, fromdate, todate, "R", 0,
										fromdate, todate, "R", 1, objuser, fromdate, todate, 3, "R", 2, objuser,
										fromdate, todate, 3, "R", 3, objuser, fromdate, todate, 3, lstproject, "R", 1,
										objuser, fromdate, todate, "R", 2, objuser, fromdate, todate, "R", 3, objuser,
										fromdate, todate, pageable);
						count = lslogilablimsorderdetailRepository
								.countByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullOrOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndLsprojectmasterInAndOrdercancellIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndLsprojectmasterIsNullAndOrdercancellIsNullOrderByBatchcodeDesc(
										"R", lstproject, fromdate, todate, 3, "R", lstproject, fromdate, todate, "R", 0,
										fromdate, todate, "R", 1, objuser, fromdate, todate, 3, "R", 2, objuser,
										fromdate, todate, 3, "R", 3, objuser, fromdate, todate, 3, lstproject, "R", 1,
										objuser, fromdate, todate, "R", 2, objuser, fromdate, todate, "R", 3, objuser,
										fromdate, todate);

					} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {

						lstorders = lslogilablimsorderdetailRepository
								.findByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusNotAndTestcodeAndOrdercancellIsNullOrOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusIsNullAndTestcodeAndOrdercancellIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndTestcodeAndOrdercancellIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndTestcodeAndOrdercancellIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndTestcodeAndOrdercancellIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndLsprojectmasterInAndTestcodeAndOrdercancellIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndTestcodeAndOrdercancellIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndTestcodeAndOrdercancellIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndLsprojectmasterInAndTestcodeAndOrdercancellIsNullOrderByBatchcodeDesc(
										"R", lstproject, fromdate, todate, 3, testcode, "R", lstproject, fromdate,
										todate, testcode, "R", 0, fromdate, todate, testcode, "R", 1, objuser, fromdate,
										todate, 3, testcode, "R", 2, objuser, fromdate, todate, 3, testcode, "R", 3,
										objuser, fromdate, todate, 3, lstproject, testcode, "R", 1, objuser, fromdate,
										todate, testcode, "R", 2, objuser, fromdate, todate, testcode, "R", 3, objuser,
										fromdate, todate, lstproject, testcode, pageable);
						count = lslogilablimsorderdetailRepository
								.countByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusNotAndTestcodeAndOrdercancellIsNullOrOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusIsNullAndTestcodeAndOrdercancellIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndTestcodeAndOrdercancellIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndTestcodeAndOrdercancellIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndTestcodeAndOrdercancellIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndLsprojectmasterInAndTestcodeAndOrdercancellIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndTestcodeAndOrdercancellIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndTestcodeAndOrdercancellIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndLsprojectmasterInAndTestcodeAndOrdercancellIsNullOrderByBatchcodeDesc(
										"R", lstproject, fromdate, todate, 3, testcode, "R", lstproject, fromdate,
										todate, testcode, "R", 0, fromdate, todate, testcode, "R", 1, objuser, fromdate,
										todate, 3, testcode, "R", 2, objuser, fromdate, todate, 3, testcode, "R", 3,
										objuser, fromdate, todate, 3, lstproject, testcode, "R", 1, objuser, fromdate,
										todate, testcode, "R", 2, objuser, fromdate, todate, testcode, "R", 3, objuser,
										fromdate, todate, lstproject, testcode);

//						lstorders = lslogilablimsorderdetailRepository
//								.findByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusNotAndTestcodeOrOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusIsNullAndTestcodeOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullOrderByBatchcodeDesc(
//										"R", lstproject, fromdate, todate, 3,testcode, "R", lstproject, fromdate, todate,testcode, "R", 0,
//										fromdate, todate,testcode, "R", 1, objuser, fromdate, todate, 3,testcode, "R", 2, objuser,
//										fromdate, todate, 3,testcode, "R", 1, objuser, fromdate, todate,testcode, "R", 1, objuser,
//										fromdate, todate, pageable);
//						count = lslogilablimsorderdetailRepository
//								.countByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusNotAndTestcodeOrOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusIsNullAndTestcodeOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullOrderByBatchcodeDesc(
//										"R", lstproject, fromdate, todate, 3,testcode, "R", lstproject, fromdate, todate,testcode, "R", 0,
//										fromdate, todate,testcode, "R", 1, objuser, fromdate, todate, 3,testcode, "R", 2, objuser,
//										fromdate, todate, 3,testcode, "R", 1, objuser, fromdate, todate,testcode, "R", 1, objuser,
//										fromdate, todate);

					} else if (testcode == -1 && objuser.getLstprojectforfilter() != null) {
						lstorders = lslogilablimsorderdetailRepository
								.findByOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullOrOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterAndOrdercancellIsNullOrderByBatchcodeDesc(
										"R", objuser.getLstprojectforfilter(), fromdate, todate, 3, "R",
										objuser.getLstprojectforfilter(), fromdate, todate, "R", 0, fromdate, todate,
										objuser.getLstprojectforfilter(), pageable);
						count = lslogilablimsorderdetailRepository
								.countByOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullOrOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterAndOrdercancellIsNullOrderByBatchcodeDesc(
										"R", objuser.getLstprojectforfilter(), fromdate, todate, 3, "R",
										objuser.getLstprojectforfilter(), fromdate, todate, "R", 0, fromdate, todate,
										objuser.getLstprojectforfilter());
					} else {
						lstorders = lslogilablimsorderdetailRepository
								.findByOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndApprovelstatusNotAndTestcodeAndOrdercancellIsNullOrOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndTestcodeAndOrdercancellIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterAndTestcodeAndOrdercancellIsNullOrderByBatchcodeDesc(
										"R", objuser.getLstprojectforfilter(), fromdate, todate, 3, testcode, "R",
										objuser.getLstprojectforfilter(), fromdate, todate, testcode, "R", 0, fromdate,
										todate, objuser.getLstprojectforfilter(), testcode, pageable);
						count = lslogilablimsorderdetailRepository
								.countByOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndApprovelstatusNotAndTestcodeAndOrdercancellIsNullOrOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndTestcodeAndOrdercancellIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterAndTestcodeAndOrdercancellIsNullOrderByBatchcodeDesc(
										"R", objuser.getLstprojectforfilter(), fromdate, todate, 3, testcode, "R",
										objuser.getLstprojectforfilter(), fromdate, todate, testcode, "R", 0, fromdate,
										todate, objuser.getLstprojectforfilter(), testcode);
					}
				} else if (objuser.getObjuser().getOrderselectiontype() == 3) {
					if (testcode == -1 && objuser.getLstprojectforfilter() == null) {

//						lstorders = lslogilablimsorderdetailRepository
//								.findByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndOrdercancellIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenOrOrderflagAndLsprojectmasterInAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenOrderByBatchcodeDesc(
//										"N", lstproject, fromdate, todate, "N", 0, fromdate, todate, "N", 1, objuser,
//										fromdate, todate, "N", 2, objuser, fromdate, todate, "N", lstproject, 3,
//										objuser, fromdate, todate, pageable);
						lstorders = lslogilablimsorderdetailRepository
							    .findByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndOrdercancellIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenOrOrderflagAndLsprojectmasterInAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenAndLsuserMasterInOrderByBatchcodeDesc(
							        "N", lstproject, fromdate, todate, "N", 0, fromdate, todate, "N", 1, objuser,
							        fromdate, todate, "N", 2, objuser, fromdate, todate, "N", lstproject, 3,
							        objuser, fromdate, todate, "N", 3,
							        objuser, fromdate, todate,objuser.getUsernotify(), pageable);
						count = lslogilablimsorderdetailRepository
								.countByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndOrdercancellIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenOrOrderflagAndLsprojectmasterInAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenAndLsuserMasterInOrderByBatchcodeDesc(
								        "N", lstproject, fromdate, todate, "N", 0, fromdate, todate, "N", 1, objuser,
								        fromdate, todate, "N", 2, objuser, fromdate, todate, "N", lstproject, 3,
								        objuser, fromdate, todate, "N", 3,
								        objuser, fromdate, todate,objuser.getUsernotify());
//						lstorders = lslogilablimsorderdetailRepository
//								.findByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndOrdercancellIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenOrderByBatchcodeDesc(
//										"N", lstproject, fromdate, todate, "N", 0, fromdate, todate, pageable);
//						count = lslogilablimsorderdetailRepository
//								.countByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndOrdercancellIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenOrderByBatchcodeDesc(
//										"N", lstproject, fromdate, todate, "N", 0, fromdate, todate);

					} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {

						lstorders = lslogilablimsorderdetailRepository
								.findByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndOrdercancellIsNullAndTestcodeOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenAndTestcodeOrOrderflagAndLsprojectmasterInAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenAndTestcodeOrderByBatchcodeDesc(
										"N", lstproject, fromdate, todate, testcode, "N", 0, fromdate, todate, testcode,
										"N", 1, objuser, fromdate, todate, testcode, "N", 2, objuser, fromdate, todate,
										testcode, "N", lstproject, 3, objuser, fromdate, todate, testcode, pageable);
						count = lslogilablimsorderdetailRepository
								.countByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndOrdercancellIsNullAndTestcodeOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenAndTestcodeOrOrderflagAndLsprojectmasterInAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenAndTestcodeOrderByBatchcodeDesc(
										"N", lstproject, fromdate, todate, testcode, "N", 0, fromdate, todate, testcode,
										"N", 1, objuser, fromdate, todate, testcode, "N", 2, objuser, fromdate, todate,
										testcode, "N", lstproject, 3, objuser, fromdate, todate, testcode);

//						lstorders = lslogilablimsorderdetailRepository
//								.findByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndOrdercancellIsNullAndTestcodeOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndTestcodeOrderByBatchcodeDesc(
//										"N", lstproject, fromdate, todate, testcode, "N", 0, fromdate, todate, testcode,
//										pageable);
//						count = lslogilablimsorderdetailRepository
//								.countByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndOrdercancellIsNullAndTestcodeOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndTestcodeOrderByBatchcodeDesc(
//										"N", lstproject, fromdate, todate, testcode, "N", 0, fromdate, todate,
//										testcode);

					} else if (testcode == -1 && objuser.getLstprojectforfilter() != null) {
						lstorders = lslogilablimsorderdetailRepository
								.findByOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndOrdercancellIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterOrderByBatchcodeDesc(
										"N", objuser.getLstprojectforfilter(), fromdate, todate, "N", 0, fromdate,
										todate, objuser.getLstprojectforfilter(), pageable);
						count = lslogilablimsorderdetailRepository
								.countByOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndOrdercancellIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterOrderByBatchcodeDesc(
										"N", objuser.getLstprojectforfilter(), fromdate, todate, "N", 0, fromdate,
										todate, objuser.getLstprojectforfilter());

					} else {
						lstorders = lslogilablimsorderdetailRepository
								.findByOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndOrdercancellIsNullAndTestcodeOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterAndTestcodeOrderByBatchcodeDesc(
										"N", objuser.getLstprojectforfilter(), fromdate, todate, testcode, "N", 0,
										fromdate, todate, objuser.getLstprojectforfilter(), testcode, pageable);
						count = lslogilablimsorderdetailRepository
								.countByOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndOrdercancellIsNullAndTestcodeOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterAndTestcodeOrderByBatchcodeDesc(
										"N", objuser.getLstprojectforfilter(), fromdate, todate, testcode, "N", 0,
										fromdate, todate, objuser.getLstprojectforfilter(), testcode);

					}

				} else if (objuser.getObjuser().getOrderselectiontype() == 4) {
					lstorders = lslogilablimsorderdetailRepository
							.findByOrderflagAndLssamplefileInAndCreatedtimestampBetween("N", lssamplefile, fromdate,
									todate, pageable);
					count = lslogilablimsorderdetailRepository
							.countByOrderflagAndLssamplefileInAndCreatedtimestampBetween("N", lssamplefile, fromdate,
									todate);
				} else if (objuser.getObjuser().getOrderselectiontype() == 5) {
					if (testcode == -1 && objuser.getLstprojectforfilter() == null) {

						lstorders = lslogilablimsorderdetailRepository
								.findByApprovelstatusAndLsprojectmasterInAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusAndLsprojectmasterIsNull(
										3, lstproject, fromdate, todate, 1, objuser, fromdate, todate, 3, 2, objuser,
										fromdate, todate, 3, 3, objuser, fromdate, todate, 3, 3, objuser, fromdate,
										todate, 3, pageable);
						count = lslogilablimsorderdetailRepository
								.countByApprovelstatusAndLsprojectmasterInAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusAndLsprojectmasterIsNull(
										3, lstproject, fromdate, todate, 1, objuser, fromdate, todate, 3, 2, objuser,
										fromdate, todate, 3, 3, objuser, fromdate, todate, 3, 3, objuser, fromdate,
										todate, 3);

//						lstorders = lslogilablimsorderdetailRepository
//								.findByApprovelstatusAndLsprojectmasterInAndCreatedtimestampBetween(3, lstproject,
//										fromdate, todate, pageable);
//						count = lslogilablimsorderdetailRepository
//								.countByApprovelstatusAndLsprojectmasterInAndCreatedtimestampBetween(3, lstproject,
//										fromdate, todate);

					} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {

						lstorders = lslogilablimsorderdetailRepository
								.findByApprovelstatusAndLsprojectmasterInAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusAndLsprojectmasterInAndTestcode(
										3, lstproject, fromdate, todate, testcode, 1, objuser, fromdate, todate, 3,
										testcode, 2, objuser, fromdate, todate, 3, testcode, 3, objuser, fromdate,
										todate, 3, testcode, 3, objuser, fromdate, todate, 3, lstproject, testcode,
										pageable);
						count = lslogilablimsorderdetailRepository
								.countByApprovelstatusAndLsprojectmasterInAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusAndLsprojectmasterInAndTestcode(
										3, lstproject, fromdate, todate, testcode, 1, objuser, fromdate, todate, 3,
										testcode, 2, objuser, fromdate, todate, 3, testcode, 3, objuser, fromdate,
										todate, 3, testcode, 3, objuser, fromdate, todate, 3, lstproject, testcode);
//						lstorders = lslogilablimsorderdetailRepository
//								.findByApprovelstatusAndLsprojectmasterInAndCreatedtimestampBetweenAndTestcode(3,
//										lstproject, fromdate, todate, testcode, pageable);
//						count = lslogilablimsorderdetailRepository
//								.countByApprovelstatusAndLsprojectmasterInAndCreatedtimestampBetweenAndTestcode(3,
//										lstproject, fromdate, todate, testcode);

					} else if (testcode == -1 && objuser.getLstprojectforfilter() != null) {
						lstorders = lslogilablimsorderdetailRepository
								.findByApprovelstatusAndLsprojectmasterAndCreatedtimestampBetween(3,
										objuser.getLstprojectforfilter(), fromdate, todate, pageable);
						count = lslogilablimsorderdetailRepository
								.countByApprovelstatusAndLsprojectmasterAndCreatedtimestampBetween(3,
										objuser.getLstprojectforfilter(), fromdate, todate);
					} else {
						lstorders = lslogilablimsorderdetailRepository
								.findByApprovelstatusAndLsprojectmasterAndCreatedtimestampBetweenAndTestcode(3,
										objuser.getLstprojectforfilter(), fromdate, todate, testcode, pageable);
						count = lslogilablimsorderdetailRepository
								.countByApprovelstatusAndLsprojectmasterAndCreatedtimestampBetweenAndTestcode(3,
										objuser.getLstprojectforfilter(), fromdate, todate, testcode);

					}

				} else if (objuser.getObjuser().getOrderselectiontype() == 6) {
					if (testcode == -1 && objuser.getLstprojectforfilter() == null) {

						lstorders = lslogilablimsorderdetailRepository
								.findByOrdercancellAndLsprojectmasterInAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndLsprojectmasterIsNull(
										1, lstproject, fromdate, todate, 1, objuser, fromdate, todate, 1, 2, objuser,
										fromdate, todate, 1, 3, objuser, fromdate, todate, 1, pageable);
						count = lslogilablimsorderdetailRepository
								.countByOrdercancellAndLsprojectmasterInAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndLsprojectmasterIsNull(
										1, lstproject, fromdate, todate, 1, objuser, fromdate, todate, 1, 2, objuser,
										fromdate, todate, 1, 3, objuser, fromdate, todate, 1);

//						lstorders = lslogilablimsorderdetailRepository
//								.findByOrdercancellAndLsprojectmasterInAndCreatedtimestampBetween(1, lstproject,
//										fromdate, todate, pageable);
//						count = lslogilablimsorderdetailRepository
//								.countByOrdercancellAndLsprojectmasterInAndCreatedtimestampBetween(1, lstproject,
//										fromdate, todate);
					} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {

						lstorders = lslogilablimsorderdetailRepository
								.findByOrdercancellAndLsprojectmasterInAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndLsprojectmasterInAndTestcode(
										1, lstproject, fromdate, todate, testcode, 1, objuser, fromdate, todate, 1,
										testcode, 2, objuser, fromdate, todate, 1, testcode, 3, objuser, fromdate,
										todate, 1, lstproject, testcode, pageable);
						count = lslogilablimsorderdetailRepository
								.countByOrdercancellAndLsprojectmasterInAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndLsprojectmasterInAndTestcode(
										1, lstproject, fromdate, todate, testcode, 1, objuser, fromdate, todate, 1,
										testcode, 2, objuser, fromdate, todate, 1, testcode, 3, objuser, fromdate,
										todate, 1, lstproject, testcode);

//						lstorders = lslogilablimsorderdetailRepository
//								.findByOrdercancellAndLsprojectmasterInAndCreatedtimestampBetweenAndTestcode(1,
//										lstproject, fromdate, todate, testcode, pageable);
//						count = lslogilablimsorderdetailRepository
//								.countByOrdercancellAndLsprojectmasterInAndCreatedtimestampBetweenAndTestcode(1,
//										lstproject, fromdate, todate, testcode);

					} else if (testcode == -1 && objuser.getLstprojectforfilter() != null) {
						lstorders = lslogilablimsorderdetailRepository
								.findByOrdercancellAndLsprojectmasterAndCreatedtimestampBetween(1,
										objuser.getLstprojectforfilter(), fromdate, todate, pageable);
						count = lslogilablimsorderdetailRepository
								.countByOrdercancellAndLsprojectmasterAndCreatedtimestampBetween(1,
										objuser.getLstprojectforfilter(), fromdate, todate);

					} else {
						lstorders = lslogilablimsorderdetailRepository
								.findByOrdercancellAndLsprojectmasterAndCreatedtimestampBetweenAndTestcode(1,
										objuser.getLstprojectforfilter(), fromdate, todate, testcode, pageable);
						count = lslogilablimsorderdetailRepository
								.countByOrdercancellAndLsprojectmasterAndCreatedtimestampBetweenAndTestcode(1,
										objuser.getLstprojectforfilter(), fromdate, todate, testcode);

					}

				}

				lstorders.forEach(objorder -> objorder.setLstworkflow(lstworkflow));
			} else {
				List<LSsamplemaster> lstsample = lssamplemasterrepository
						.findByLssitemasterAndStatus(objuser.getLssitemaster(), 1);
				List<LSSheetOrderStructure> lstdir = lsSheetOrderStructureRepository
						.findBySitemasterAndViewoptionOrCreatedbyAndViewoptionOrderByDirectorycode(
								objuser.getLssitemaster(), 1, objuser, 2);
				List<Long> directorycode = lstdir.stream().map(LSSheetOrderStructure::getDirectorycode)
						.collect(Collectors.toList());
				if (objuser.getObjuser().getOrderselectiontype() == 1) {

					if (testcode == -1 && objuser.getLstprojectforfilter() == null) {
						lstorders = lslogilablimsorderdetailRepository
								.findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterOrderByBatchcodeDesc(
										lstsample, 1, fromdate, todate, lstsample, 2, fromdate, todate, objuser, 3,
										fromdate, todate, objuser, directorycode, 1, fromdate, todate, directorycode, 2,
										fromdate, todate, objuser, directorycode, 3, fromdate, todate, objuser,
										pageable);

						count = lslogilablimsorderdetailRepository
								.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterOrderByBatchcodeDesc(
										lstsample, 1, fromdate, todate, lstsample, 2, fromdate, todate, objuser, 3,
										fromdate, todate, objuser, directorycode, 1, fromdate, todate, directorycode, 2,
										fromdate, todate, objuser, directorycode, 3, fromdate, todate, objuser);
//								.findByLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetween
//								OrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetween
//								OrLsprojectmasterIsNullAndLsuserMasterIsNotAndLssamplemasterInAndViewoptionAndCreatedtimestampBetween
//								OrLsprojectmasterIsNullAndLsuserMasterIsNotAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenOrderByBatchcodeDesc(
//										1, objuser, fromdate, todate, 2, objuser, fromdate, todate, objuser, lstsample,
//										1, fromdate, todate, objuser, lstsample, 2, fromdate, todate, pageable);
//						count = lslogilablimsorderdetailRepository
//								.countByLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndLsuserMasterIsNotAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndLsuserMasterIsNotAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenOrderByBatchcodeDesc(
//										1, objuser, fromdate, todate, 2, objuser, fromdate, todate, objuser, lstsample,
//										1, fromdate, todate, objuser, lstsample, 2, fromdate, todate);
					} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {

						lstorders = lslogilablimsorderdetailRepository
								.findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTestcodeOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTestcodeOrderByBatchcodeDesc(
										lstsample, 1, fromdate, todate, testcode, lstsample, 2, fromdate, todate,
										objuser, testcode, 3, fromdate, todate, objuser, testcode, directorycode, 1,
										fromdate, todate, testcode, directorycode, 2, fromdate, todate, objuser,
										testcode, directorycode, 3, fromdate, todate, objuser, testcode, pageable);

						count = lslogilablimsorderdetailRepository
								.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTestcodeOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTestcodeOrderByBatchcodeDesc(
										lstsample, 1, fromdate, todate, testcode, lstsample, 2, fromdate, todate,
										objuser, testcode, 3, fromdate, todate, objuser, testcode, directorycode, 1,
										fromdate, todate, testcode, directorycode, 2, fromdate, todate, objuser,
										testcode, directorycode, 3, fromdate, todate, objuser, testcode);

					}

				} else if (objuser.getObjuser().getOrderselectiontype() == 2) {
					if (testcode == -1 && objuser.getLstprojectforfilter() == null) {
						lstorders = lslogilablimsorderdetailRepository.
								findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullOrderByBatchcodeDesc(
										 lstsample, 1, fromdate, todate, "R", 3,lstsample, 1, fromdate, todate, "R",lstsample, 2, fromdate, todate, objuser,"R", 3,lstsample, 2, fromdate, todate, objuser, "R",3, fromdate, todate, objuser, "R", 3, 3, fromdate, todate, objuser, "R",directorycode, 1, fromdate, todate,"R", 3,directorycode, 1, fromdate, todate, "R",directorycode, 2, fromdate, todate, objuser, "R", 3,directorycode, 2, fromdate, todate, objuser, "R",directorycode, 3,fromdate, todate, objuser, "R", 3,directorycode, 3, fromdate, todate, objuser, "R",pageable);

								
//								.findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrderByBatchcodeDesc(
//										lstsample, 1, fromdate, todate, "R", 3, lstsample, 2, fromdate, todate, objuser,
//										"R", 3, 3, fromdate, todate, objuser, "R", 3, directorycode, 1, fromdate,
//										todate, "R", 3, directorycode, 2, fromdate, todate, objuser, "R", 3,
//										directorycode, 3, fromdate, todate, objuser, "R", 3, pageable);

//						count = lslogilablimsorderdetailRepository
//								.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrderByBatchcodeDesc(
//										lstsample, 1, fromdate, todate, "R", 3, lstsample, 2, fromdate, todate, objuser,
//										"R", 3, 3, fromdate, todate, objuser, "R", 3, directorycode, 1, fromdate,
//										todate, "R", 3, directorycode, 2, fromdate, todate, objuser, "R", 3,
//										directorycode, 3, fromdate, todate, objuser, "R", 3);
						count = lslogilablimsorderdetailRepository.
								countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullOrderByBatchcodeDesc(
										 lstsample, 1, fromdate, todate, "R", 3,lstsample, 1, fromdate, todate, "R",lstsample, 2, fromdate, todate, objuser,"R", 3,lstsample, 2, fromdate, todate, objuser, "R",3, fromdate, todate, objuser, "R", 3, 3, fromdate, todate, objuser, "R",directorycode, 1, fromdate, todate,"R", 3,directorycode, 1, fromdate, todate, "R",directorycode, 2, fromdate, todate, objuser, "R", 3,directorycode, 2, fromdate, todate, objuser, "R",directorycode, 3,fromdate, todate, objuser, "R", 3,directorycode, 3, fromdate, todate, objuser, "R");

								
						

					} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {

						lstorders = lslogilablimsorderdetailRepository
								.findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullAndTestcodeOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullAndTestcodeOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullAndTestcodeOrderByBatchcodeDesc(
									    lstsample, 1, fromdate, todate, "R", 3, testcode,
									    lstsample, 1, fromdate, todate, "R", testcode,
									    lstsample, 2, fromdate, todate, objuser, "R", 3, testcode,
									    lstsample, 2, fromdate, todate, objuser, "R", testcode,
									    3, fromdate, todate, objuser, "R", 3, testcode,
									    3, fromdate, todate, objuser, "R", testcode,
									    directorycode, 1, fromdate, todate, "R", 3, testcode,
									    directorycode, 1, fromdate, todate, "R", testcode,
									    directorycode, 2, fromdate, todate, objuser, "R", 3, testcode,
									    directorycode, 2, fromdate, todate, objuser, "R", testcode,
									    directorycode, 3, fromdate, todate, objuser, "R", 3, testcode,
									    directorycode, 3, fromdate, todate, objuser, "R", testcode,pageable);

						count = lslogilablimsorderdetailRepository.
								countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullAndTestcodeOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullAndTestcodeOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullAndTestcodeOrderByBatchcodeDesc(
									    lstsample, 1, fromdate, todate, "R", 3, testcode,
									    lstsample, 1, fromdate, todate, "R", testcode,
									    lstsample, 2, fromdate, todate, objuser, "R", 3, testcode,
									    lstsample, 2, fromdate, todate, objuser, "R", testcode,
									    3, fromdate, todate, objuser, "R", 3, testcode,
									    3, fromdate, todate, objuser, "R", testcode,
									    directorycode, 1, fromdate, todate, "R", 3, testcode,
									    directorycode, 1, fromdate, todate, "R", testcode,
									    directorycode, 2, fromdate, todate, objuser, "R", 3, testcode,
									    directorycode, 2, fromdate, todate, objuser, "R", testcode,
									    directorycode, 3, fromdate, todate, objuser, "R", 3, testcode,
									    directorycode, 3, fromdate, todate, objuser, "R", testcode);
//								.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusNotAndTestcodeAndOrdercancellIsNullOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndTestcodeAndOrdercancellIsNullOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndTestcodeAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusNotAndTestcodeAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndTestcodeAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndTestcodeAndOrdercancellIsNullOrderByBatchcodeDesc(
//										lstsample, 1, fromdate, todate, "R", 3, testcode, lstsample, 2, fromdate,
//										todate, objuser, "R", 3, testcode, 3, fromdate, todate, objuser, "R", 3,
//										testcode, directorycode, 1, fromdate, todate, "R", 3, testcode, directorycode,
//										2, fromdate, todate, objuser, "R", 3, testcode, directorycode, 3, fromdate,
//										todate, objuser, "R", 3, testcode);

					}

				} else if (objuser.getObjuser().getOrderselectiontype() == 3) {
					if (testcode == -1 && objuser.getLstprojectforfilter() == null) {

						lstorders = lslogilablimsorderdetailRepository
								.findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullOrderByBatchcodeDesc(
										lstsample, 1, fromdate, todate, "N", lstsample, 2, fromdate, todate, objuser,
										"N", 3, fromdate, todate, objuser, "N", directorycode, 1, fromdate, todate, "N",
										directorycode, 2, fromdate, todate, objuser, "N", directorycode, 3, fromdate,
										todate, objuser, "N", pageable);

//						lstorders = lslogilablimsorderdetailRepository
//								.findByOrderflagAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenOrOrderflagAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenOrOrderflagAndOrdercancellIsNullAndLsprojectmasterIsNullAndLsuserMasterIsNotAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenOrOrderflagAndOrdercancellIsNullAndLsprojectmasterIsNullAndLsuserMasterIsNotAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenOrderByBatchcodeDesc(
//										"N", 1, objuser, fromdate, todate, "N", 2, objuser, fromdate, todate, "N",
//										objuser, lstsample, 1, fromdate, todate, "N", objuser, lstsample, 2, fromdate,
//										todate, pageable);
						count = lslogilablimsorderdetailRepository
								.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullOrderByBatchcodeDesc(
										lstsample, 1, fromdate, todate, "N", lstsample, 2, fromdate, todate, objuser,
										"N", 3, fromdate, todate, objuser, "N", directorycode, 1, fromdate, todate, "N",
										directorycode, 2, fromdate, todate, objuser, "N", directorycode, 3, fromdate,
										todate, objuser, "N");

					} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {

						lstorders = lslogilablimsorderdetailRepository
								.findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndTestcodeOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndTestcodeOrderByBatchcodeDesc(
										lstsample, 1, fromdate, todate, "N", testcode, lstsample, 2, fromdate, todate,
										objuser, "N", testcode, 3, fromdate, todate, objuser, "N", testcode,
										directorycode, 1, fromdate, todate, "N", testcode, directorycode, 2, fromdate,
										todate, objuser, "N", testcode, directorycode, 3, fromdate, todate, objuser,
										"N", testcode, pageable);

//						lstorders = lslogilablimsorderdetailRepository
//								.findByOrderflagAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndTestcodeOrOrderflagAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndTestcodeOrOrderflagAndOrdercancellIsNullAndLsprojectmasterIsNullAndLsuserMasterIsNotAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrOrderflagAndOrdercancellIsNullAndLsprojectmasterIsNullAndLsuserMasterIsNotAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrderByBatchcodeDesc(
//										"N", 1, objuser, fromdate, todate, testcode, "N", 2, objuser, fromdate, todate,
//										testcode, "N", objuser, lstsample, 1, fromdate, todate, testcode, "N", objuser,
//										lstsample, 2, fromdate, todate, testcode, pageable);
						count = lslogilablimsorderdetailRepository
								.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndTestcodeOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndTestcodeOrderByBatchcodeDesc(
										lstsample, 1, fromdate, todate, "N", testcode, lstsample, 2, fromdate, todate,
										objuser, "N", testcode, 3, fromdate, todate, objuser, "N", testcode,
										directorycode, 1, fromdate, todate, "N", testcode, directorycode, 2, fromdate,
										todate, objuser, "N", testcode, directorycode, 3, fromdate, todate, objuser,
										"N", testcode);
					}

				} else if (objuser.getObjuser().getOrderselectiontype() == 4) {
					lstorders = lslogilablimsorderdetailRepository
							.findByOrderflagAndLssamplefileInAndCreatedtimestampBetween("N", lssamplefile, fromdate,
									todate, pageable);
					count = lslogilablimsorderdetailRepository
							.countByOrderflagAndLssamplefileInAndCreatedtimestampBetween("N", lssamplefile, fromdate,
									todate);
				} else if (objuser.getObjuser().getOrderselectiontype() == 5) {
					if (testcode == -1 && objuser.getLstprojectforfilter() == null) {
						lstorders = lslogilablimsorderdetailRepository
								.findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndApprovelstatusOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndApprovelstatusOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusOrderByBatchcodeDesc(
										lstsample, 1, fromdate, todate, 3, lstsample, 2, fromdate, todate, objuser, 3,
										3, fromdate, todate, objuser, 3, directorycode, 1, fromdate, todate, 3,
										directorycode, 2, fromdate, todate, objuser, 3, directorycode, 3, fromdate,
										todate, objuser, 3, pageable);

//						lstorders = lslogilablimsorderdetailRepository
//								.findByApprovelstatusAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenOrApprovelstatusAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenOrApprovelstatusAndLsprojectmasterIsNullAndLsuserMasterIsNotAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenOrApprovelstatusAndLsprojectmasterIsNullAndLsuserMasterIsNotAndLssamplemasterInAndViewoptionAndCreatedtimestampBetween(
//										3, 1, objuser, fromdate, todate, 3, 2, objuser, fromdate, todate, 3, objuser,
//										lstsample, 1, fromdate, todate, 3, objuser, lstsample, 1, fromdate, todate,
//										pageable);

						count = lslogilablimsorderdetailRepository
								.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndApprovelstatusOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndApprovelstatusOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusOrderByBatchcodeDesc(
										lstsample, 1, fromdate, todate, 3, lstsample, 2, fromdate, todate, objuser, 3,
										3, fromdate, todate, objuser, 3, directorycode, 1, fromdate, todate, 3,
										directorycode, 2, fromdate, todate, objuser, 3, directorycode, 3, fromdate,
										todate, objuser, 3);

					} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {
						lstorders = lslogilablimsorderdetailRepository
								.findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndApprovelstatusAndTestcodeOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusAndTestcodeOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndApprovelstatusAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusAndTestcodeOrderByBatchcodeDesc(
										lstsample, 1, fromdate, todate, 3, testcode, lstsample, 2, fromdate, todate,
										objuser, 3, testcode, 3, fromdate, todate, objuser, 3, testcode, directorycode,
										1, fromdate, todate, 3, testcode, directorycode, 2, fromdate, todate, objuser,
										3, testcode, directorycode, 3, fromdate, todate, objuser, 3, testcode,
										pageable);
//						lstorders = lslogilablimsorderdetailRepository
//								.findByApprovelstatusAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndTestcodeOrApprovelstatusAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndTestcodeOrApprovelstatusAndLsprojectmasterIsNullAndLsuserMasterIsNotAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrApprovelstatusAndLsprojectmasterIsNullAndLsuserMasterIsNotAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndTestcode(
//										3, 1, objuser, fromdate, todate, testcode, 3, 2, objuser, fromdate, todate,
//										testcode, 3, objuser, lstsample, 1, fromdate, todate, testcode, 3, objuser,
//										lstsample, 1, fromdate, todate, testcode, pageable);

						count = lslogilablimsorderdetailRepository
								.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndApprovelstatusAndTestcodeOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusAndTestcodeOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndApprovelstatusAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusAndTestcodeOrderByBatchcodeDesc(
										lstsample, 1, fromdate, todate, 3, testcode, lstsample, 2, fromdate, todate,
										objuser, 3, testcode, 3, fromdate, todate, objuser, 3, testcode, directorycode,
										1, fromdate, todate, 3, testcode, directorycode, 2, fromdate, todate, objuser,
										3, testcode, directorycode, 3, fromdate, todate, objuser, 3, testcode);

					}
				} else if (objuser.getObjuser().getOrderselectiontype() == 6) {
					if (testcode == -1 && objuser.getLstprojectforfilter() == null) {
						lstorders = lslogilablimsorderdetailRepository
								.findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrdercancellOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrdercancellOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellOrderByBatchcodeDesc(
										lstsample, 1, fromdate, todate, 1, lstsample, 2, fromdate, todate, objuser, 1,
										3, fromdate, todate, objuser, 1, directorycode, 1, fromdate, todate, 1,
										directorycode, 2, fromdate, todate, objuser, 1, directorycode, 3, fromdate,
										todate, objuser, 1, pageable);

//						lstorders = lslogilablimsorderdetailRepository
//								.findByOrdercancellAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenOrOrdercancellAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenOrOrdercancellAndLsprojectmasterIsNullAndLsuserMasterIsNotAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenOrOrdercancellAndLsprojectmasterIsNullAndLsuserMasterIsNotAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenOrderByBatchcodeDesc(
//										1, 1, objuser, fromdate, todate, 1, 2, objuser, fromdate, todate, 1, objuser,
//										lstsample, 1, fromdate, todate, 1, objuser, lstsample, 1, fromdate, todate,
//										pageable);

						count = lslogilablimsorderdetailRepository
								.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrdercancellOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrdercancellOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellOrderByBatchcodeDesc(
										lstsample, 1, fromdate, todate, 1, lstsample, 2, fromdate, todate, objuser, 1,
										3, fromdate, todate, objuser, 1, directorycode, 1, fromdate, todate, 1,
										directorycode, 2, fromdate, todate, objuser, 1, directorycode, 3, fromdate,
										todate, objuser, 1);

					} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {
						lstorders = lslogilablimsorderdetailRepository
								.findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrdercancellAndTestcodeOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellAndTestcodeOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrdercancellAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellAndTestcodeOrderByBatchcodeDesc(
										lstsample, 1, fromdate, todate, 1, testcode, lstsample, 2, fromdate, todate,
										objuser, 1, testcode, 3, fromdate, todate, objuser, 1, testcode, directorycode,
										1, fromdate, todate, 1, testcode, directorycode, 2, fromdate, todate, objuser,
										1, testcode, directorycode, 3, fromdate, todate, objuser, 1, testcode,
										pageable);

//								.findByOrdercancellAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndTestcodeOrOrdercancellAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndTestcodeOrOrdercancellAndLsprojectmasterIsNullAndLsuserMasterIsNotAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrOrdercancellAndLsprojectmasterIsNullAndLsuserMasterIsNotAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrderByBatchcodeDesc(
//										1, 1, objuser, fromdate, todate, testcode, 1, 2, objuser, fromdate, todate,
//										testcode, 1, objuser, lstsample, 1, fromdate, todate, testcode, 1, objuser,
//										lstsample, 1, fromdate, todate, testcode, pageable);

						count = lslogilablimsorderdetailRepository
								.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrdercancellAndTestcodeOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellAndTestcodeOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrdercancellAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellAndTestcodeOrderByBatchcodeDesc(
										lstsample, 1, fromdate, todate, 1, testcode, lstsample, 2, fromdate, todate,
										objuser, 1, testcode, 3, fromdate, todate, objuser, 1, testcode, directorycode,
										1, fromdate, todate, 1, testcode, directorycode, 2, fromdate, todate, objuser,
										1, testcode, directorycode, 3, fromdate, todate, objuser, 1, testcode);

					}

				}
				lstorders.forEach(objorder -> objorder.setLstworkflow(lstworkflow));
			}
			Collections.sort(lstorders, Collections.reverseOrder());
			mapOrders.put("orderlst", lstorders);

//		}
		mapOrders.put("count", count);
		if (objuser.getObjsilentaudit() != null) {
			objuser.getObjsilentaudit().setTableName("LSlogilablimsorderdetail");
			lscfttransactionRepository.save(objuser.getObjsilentaudit());
		}

		return mapOrders;
	}

	public Map<String, Object> Getdashboardprotocolorders(LSuserMaster objuser) {
		Date fromdate = objuser.getObjuser().getFromdate();
		Date todate = objuser.getObjuser().getTodate();
		Map<String, Object> mapOrders = new HashMap<String, Object>();
		long count = 0;
		Pageable pageable = new PageRequest(objuser.getPagesize(), objuser.getPageperorder());
		List<Logilabprotocolorders> lstorders = new ArrayList<Logilabprotocolorders>();
		List<LSprojectmaster> lstproject = objuser.getLstproject();
		Integer testcode = objuser.getTestcode();
		List<Integer> userlist = objuser.getUsernotify()!=null? objuser.getUsernotify().stream().map(LSuserMaster::getUsercode).collect(Collectors.toList()):new ArrayList<Integer>();
		if (lstproject != null) {

			if (objuser.getObjuser().getOrderselectiontype() == 1) {
				if (testcode == -1 && objuser.getLstprojectforfilter() == null) {
					lstorders = LSlogilabprotocoldetailRepository
							.findByLsprojectmasterInAndCreatedtimestampBetweenAndSitecodeOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatebyAndCreatedtimestampBetweenOrLsprojectmasterInAndViewoptionAndSitecodeAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndCreatebyInOrderByProtocolordercodeDesc(
									lstproject, fromdate, todate, objuser.getLssitemaster().getSitecode(), 1,
									objuser.getLssitemaster().getSitecode(), fromdate, todate, 2,
									objuser.getLssitemaster().getSitecode(), objuser.getUsercode(), fromdate, todate,
									lstproject, 3, objuser.getLssitemaster().getSitecode(), fromdate, todate,3,objuser.getLssitemaster().getSitecode(), fromdate, todate,userlist, pageable);

//					lstorders = LSlogilabprotocoldetailRepository
//							.findByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterInAndOrdercancellIsNullOrOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterInAndRejectedIsNullOrRejectedAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInOrOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInOrderByProtocolordercodeDesc(
//									"N", objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject, "R",
//									objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject, 1,
//									objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject, 1,
//									objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject, pageable);
					count = LSlogilabprotocoldetailRepository
							.countByLsprojectmasterInAndCreatedtimestampBetweenAndSitecodeOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatebyAndCreatedtimestampBetweenOrLsprojectmasterInAndViewoptionAndSitecodeAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndCreatebyInOrderByProtocolordercodeDesc(
									lstproject, fromdate, todate, objuser.getLssitemaster().getSitecode(), 1,
									objuser.getLssitemaster().getSitecode(), fromdate, todate, 2,
									objuser.getLssitemaster().getSitecode(), objuser.getUsercode(), fromdate, todate,
									lstproject, 3, objuser.getLssitemaster().getSitecode(), fromdate, todate,3,objuser.getLssitemaster().getSitecode(), fromdate, todate,userlist);

				} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {

					lstorders = LSlogilabprotocoldetailRepository
							.findByLsprojectmasterInAndCreatedtimestampBetweenAndSitecodeAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatebyAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterInAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndTestcodeOrderByProtocolordercodeDesc(
									lstproject, fromdate, todate, objuser.getLssitemaster().getSitecode(), testcode, 1,
									objuser.getLssitemaster().getSitecode(), fromdate, todate, testcode, 2,
									objuser.getLssitemaster().getSitecode(), objuser.getUsercode(), fromdate, todate,
									testcode, lstproject, 3, objuser.getLssitemaster().getSitecode(), fromdate, todate,
									testcode, pageable);
					count = LSlogilabprotocoldetailRepository
							.countByLsprojectmasterInAndCreatedtimestampBetweenAndSitecodeAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatebyAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterInAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndTestcodeOrderByProtocolordercodeDesc(
									lstproject, fromdate, todate, objuser.getLssitemaster().getSitecode(), testcode, 1,
									objuser.getLssitemaster().getSitecode(), fromdate, todate, testcode, 2,
									objuser.getLssitemaster().getSitecode(), objuser.getUsercode(), fromdate, todate,
									testcode, lstproject, 3, objuser.getLssitemaster().getSitecode(), fromdate, todate,
									testcode);
				} else if (testcode == -1 && objuser.getLstprojectforfilter() != null) {
					lstorders = LSlogilabprotocoldetailRepository
							.findByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterAndOrdercancellIsNullOrOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterAndRejectedIsNullOrRejectedAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterOrOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterOrderByProtocolordercodeDesc(
									"N", objuser.getLssitemaster().getSitecode(), fromdate, todate,
									objuser.getLstprojectforfilter(), "R", objuser.getLssitemaster().getSitecode(),
									fromdate, todate, objuser.getLstprojectforfilter(), 1,
									objuser.getLssitemaster().getSitecode(), fromdate, todate,
									objuser.getLstprojectforfilter(), 1, objuser.getLssitemaster().getSitecode(),
									fromdate, todate, objuser.getLstprojectforfilter(), pageable);
					count = LSlogilabprotocoldetailRepository
							.countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterAndOrdercancellIsNullOrOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterAndRejectedIsNullOrRejectedAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterOrOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmaster(
									"N", objuser.getLssitemaster().getSitecode(), fromdate, todate,
									objuser.getLstprojectforfilter(), "R", objuser.getLssitemaster().getSitecode(),
									fromdate, todate, objuser.getLstprojectforfilter(), 1,
									objuser.getLssitemaster().getSitecode(), fromdate, todate,
									objuser.getLstprojectforfilter(), 1, objuser.getLssitemaster().getSitecode(),
									fromdate, todate, objuser.getLstprojectforfilter());

				} else {
					lstorders = LSlogilabprotocoldetailRepository
							.findByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterAndOrdercancellIsNullAndTestcodeOrOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterAndRejectedIsNullAndTestcodeOrRejectedAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterAndTestcodeOrOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterAndTestcodeOrderByProtocolordercodeDesc(
									"N", objuser.getLssitemaster().getSitecode(), fromdate, todate,
									objuser.getLstprojectforfilter(), testcode, "R",
									objuser.getLssitemaster().getSitecode(), fromdate, todate,
									objuser.getLstprojectforfilter(), testcode, 1,
									objuser.getLssitemaster().getSitecode(), fromdate, todate,
									objuser.getLstprojectforfilter(), testcode, 1,
									objuser.getLssitemaster().getSitecode(), fromdate, todate,
									objuser.getLstprojectforfilter(), testcode, pageable);
					count = LSlogilabprotocoldetailRepository
							.countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterAndOrdercancellIsNullAndTestcodeOrOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterAndRejectedIsNullAndTestcodeOrRejectedAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterAndTestcodeOrOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterAndTestcode(
									"N", objuser.getLssitemaster().getSitecode(), fromdate, todate,
									objuser.getLstprojectforfilter(), testcode, "R",
									objuser.getLssitemaster().getSitecode(), fromdate, todate,
									objuser.getLstprojectforfilter(), testcode, 1,
									objuser.getLssitemaster().getSitecode(), fromdate, todate,
									objuser.getLstprojectforfilter(), testcode, 1,
									objuser.getLssitemaster().getSitecode(), fromdate, todate,
									objuser.getLstprojectforfilter(), testcode);

				}
			} else if (objuser.getObjuser().getOrderselectiontype() == 2) {
				if (testcode == -1 && objuser.getLstprojectforfilter() == null) {

					lstorders = LSlogilabprotocoldetailRepository
							.findByOrderflagAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndRejectedIsNullOrOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenOrOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenOrOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyInOrderByProtocolordercodeDesc(
									"R", objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject, "R",
									objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, "R",
									objuser.getLssitemaster().getSitecode(), 2, objuser.getUsercode(), fromdate, todate,
									"R", objuser.getLssitemaster().getSitecode(), 3, fromdate, todate,userlist,
									pageable);

//					lstorders = LSlogilabprotocoldetailRepository
//							.findByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterInAndRejectedIsNullOrderByProtocolordercodeDesc(
//									"R", objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject,
//									pageable);
//
					count = LSlogilabprotocoldetailRepository
							.countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndRejectedIsNullOrOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenOrOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenOrOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyInOrderByProtocolordercodeDesc(
									"R", objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject, "R",
									objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, "R",
									objuser.getLssitemaster().getSitecode(), 2, objuser.getUsercode(), fromdate, todate,
									"R", objuser.getLssitemaster().getSitecode(), 3, fromdate, todate,userlist);
				} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {

					lstorders = LSlogilabprotocoldetailRepository
							.findByOrderflagAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndRejectedIsNullAndTestcodeOrOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndTestcodeOrOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterInAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrderByProtocolordercodeDesc(
									"R", objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject,
									testcode, "R", objuser.getLssitemaster().getSitecode(), 1, fromdate, todate,
									testcode, "R", objuser.getLssitemaster().getSitecode(), 2, objuser.getUsercode(),
									fromdate, todate, testcode, "R", objuser.getLssitemaster().getSitecode(),
									lstproject, 3, fromdate, todate, testcode, pageable);
					count = LSlogilabprotocoldetailRepository
							.countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndRejectedIsNullAndTestcodeOrOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndTestcodeOrOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterInAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrderByProtocolordercodeDesc(
									"R", objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject,
									testcode, "R", objuser.getLssitemaster().getSitecode(), 1, fromdate, todate,
									testcode, "R", objuser.getLssitemaster().getSitecode(), 2, objuser.getUsercode(),
									fromdate, todate, testcode, "R", objuser.getLssitemaster().getSitecode(),
									lstproject, 3, fromdate, todate, testcode);

//					lstorders = LSlogilabprotocoldetailRepository
//							.findByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterInAndRejectedIsNullAndTestcodeOrderByProtocolordercodeDesc(
//									"R", objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject,
//									testcode, pageable);
//
//					count = LSlogilabprotocoldetailRepository
//							.countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterInAndTestcodeAndRejectedIsNull(
//									"R", objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject,
//									testcode);
				} else if (testcode == -1 && objuser.getLstprojectforfilter() != null) {
					lstorders = LSlogilabprotocoldetailRepository
							.findByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterAndRejectedIsNullOrderByProtocolordercodeDesc(
									"R", objuser.getLssitemaster().getSitecode(), fromdate, todate,
									objuser.getLstprojectforfilter(), pageable);

					count = LSlogilabprotocoldetailRepository
							.countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterAndRejectedIsNull(
									"R", objuser.getLssitemaster().getSitecode(), fromdate, todate,
									objuser.getLstprojectforfilter());

				} else {
					lstorders = LSlogilabprotocoldetailRepository
							.findByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterAndRejectedIsNullAndTestcodeOrderByProtocolordercodeDesc(
									"R", objuser.getLssitemaster().getSitecode(), fromdate, todate,
									objuser.getLstprojectforfilter(), testcode, pageable);

					count = LSlogilabprotocoldetailRepository
							.countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterAndRejectedIsNullAndTestcode(
									"R", objuser.getLssitemaster().getSitecode(), fromdate, todate,
									objuser.getLstprojectforfilter(), testcode);
				}

			} else if (objuser.getObjuser().getOrderselectiontype() == 3) {

				if (testcode == -1 && objuser.getLstprojectforfilter() == null) {
					lstorders = LSlogilabprotocoldetailRepository
							.findByOrderflagAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndOrdercancellIsNullAndRejectedIsNullOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndRejectedIsNullOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndRejectedIsNullOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndRejectedIsNullAndCreatebyInOrderByProtocolordercodeDesc(
									"N", objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject,
									"N", objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, 
									"N", objuser.getLssitemaster().getSitecode(), 2, objuser.getUsercode(), fromdate, todate,
									"N", objuser.getLssitemaster().getSitecode(), 3, fromdate, todate,userlist, pageable);


					count = LSlogilabprotocoldetailRepository
							.countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndOrdercancellIsNullAndRejectedIsNullOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndRejectedIsNullOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndRejectedIsNullOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyInOrderByProtocolordercodeDesc(
									"N", objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject, 
									"N", objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, 
									"N",objuser.getLssitemaster().getSitecode(), 2, objuser.getUsercode(), fromdate, todate,
									"N", objuser.getLssitemaster().getSitecode(), 3, fromdate, todate,userlist);
				} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {

					lstorders = LSlogilabprotocoldetailRepository
							.findByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterInAndOrdercancellIsNullAndTestcodeAndRejectedIsNullOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndTestcodeAndRejectedIsNullOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndTestcodeAndRejectedIsNullOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterInAndViewoptionAndCreatedtimestampBetweenAndTestcodeAndRejectedIsNullOrderByProtocolordercodeDesc(
									"N", objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject,	testcode, 
									"N", objuser.getLssitemaster().getSitecode(), 1, fromdate, todate,testcode, 
									"N", objuser.getLssitemaster().getSitecode(), 2, fromdate,objuser.getUsercode(), todate, testcode, 
									"N", objuser.getLssitemaster().getSitecode(), lstproject, 3, fromdate, todate, testcode,pageable);
					count = LSlogilabprotocoldetailRepository
							.countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterInAndOrdercancellIsNullAndTestcodeAndRejectedIsNullOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndTestcodeAndRejectedIsNullOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndTestcodeAndRejectedIsNullOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterInAndViewoptionAndCreatedtimestampBetweenAndTestcodeAndRejectedIsNullOrderByProtocolordercodeDesc(
									"N", objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject,testcode, 
									"N", objuser.getLssitemaster().getSitecode(), 1, fromdate, todate,testcode, 
									"N", objuser.getLssitemaster().getSitecode(), 2, fromdate,objuser.getUsercode(), todate, testcode, 
									"N",objuser.getLssitemaster().getSitecode(), lstproject, 3, fromdate, todate, testcode);
				} else if (testcode == -1 && objuser.getLstprojectforfilter() != null) {
					lstorders = LSlogilabprotocoldetailRepository
							.findByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterAndOrdercancellIsNullAndRejectedIsNullOrderByProtocolordercodeDesc(
									"N", objuser.getLssitemaster().getSitecode(), fromdate, todate, objuser.getLstprojectforfilter(), pageable);

					count = LSlogilabprotocoldetailRepository
							.countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterAndOrdercancellIsNullAndRejectedIsNull(
									"N", objuser.getLssitemaster().getSitecode(), fromdate, todate, objuser.getLstprojectforfilter());

				} else {
					lstorders = LSlogilabprotocoldetailRepository
							.findByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterAndOrdercancellIsNullAndTestcodeAndRejectedIsNullOrderByProtocolordercodeDesc(
									"N", objuser.getLssitemaster().getSitecode(), fromdate, todate, objuser.getLstprojectforfilter(), testcode, pageable);

					count = LSlogilabprotocoldetailRepository
							.countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterAndOrdercancellIsNullAndTestcodeAndRejectedIsNull(
									"N", objuser.getLssitemaster().getSitecode(), fromdate, todate, objuser.getLstprojectforfilter(), testcode);

				}
			} else if (objuser.getObjuser().getOrderselectiontype() == 5) {
				if (testcode == -1 && objuser.getLstprojectforfilter() == null) {
					lstorders = LSlogilabprotocoldetailRepository
							.findByRejectedAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInOrRejectedAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenOrRejectedAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenOrRejectedAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyInOrderByProtocolordercodeDesc(
									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject, 1,
									objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, 1,
									objuser.getLssitemaster().getSitecode(), 2, objuser.getUsercode(), fromdate, todate,
									1, objuser.getLssitemaster().getSitecode(), 3, fromdate, todate,userlist,
									pageable);

					count = LSlogilabprotocoldetailRepository
							.countByRejectedAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInOrRejectedAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenOrRejectedAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenOrRejectedAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyInOrderByProtocolordercodeDesc(
									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject, 1,
									objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, 1,
									objuser.getLssitemaster().getSitecode(), 2, objuser.getUsercode(), fromdate, todate,
									1, objuser.getLssitemaster().getSitecode(), 3, fromdate, todate,userlist);
				} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {

					lstorders = LSlogilabprotocoldetailRepository
							.findByRejectedAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndTestcodeOrRejectedAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrRejectedAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndTestcodeOrRejectedAndSitecodeAndLsprojectmasterInAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrderByProtocolordercodeDesc(
									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject, testcode,
									1, objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, testcode, 1,
									objuser.getLssitemaster().getSitecode(), 2, objuser.getUsercode(), fromdate, todate,
									testcode, 1, objuser.getLssitemaster().getSitecode(), lstproject, 3, fromdate,
									todate, testcode, pageable);

					count = LSlogilabprotocoldetailRepository
							.countByRejectedAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndTestcodeOrRejectedAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrRejectedAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndTestcodeOrRejectedAndSitecodeAndLsprojectmasterInAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrderByProtocolordercodeDesc(
									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject, testcode,
									1, objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, testcode, 1,
									objuser.getLssitemaster().getSitecode(), 2, objuser.getUsercode(), fromdate, todate,
									testcode, 1, objuser.getLssitemaster().getSitecode(), lstproject, 3, fromdate,
									todate, testcode);

//					lstorders = LSlogilabprotocoldetailRepository
//							.findByRejectedAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndTestcodeOrderByProtocolordercodeDesc(
//									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject, testcode,
//									pageable);
//
//					count = LSlogilabprotocoldetailRepository
//							.countByRejectedAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndTestcode(1,
//									objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject, testcode);
				} else if (testcode == -1 && objuser.getLstprojectforfilter() != null) {
					lstorders = LSlogilabprotocoldetailRepository
							.findByRejectedAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterOrderByProtocolordercodeDesc(
									1, objuser.getLssitemaster().getSitecode(), fromdate, todate,
									objuser.getLstprojectforfilter(), pageable);

					count = LSlogilabprotocoldetailRepository
							.countByRejectedAndSitecodeAndCreatedtimestampBetweenAndLsprojectmaster(1,
									objuser.getLssitemaster().getSitecode(), fromdate, todate,
									objuser.getLstprojectforfilter());

				} else {
					lstorders = LSlogilabprotocoldetailRepository
							.findByRejectedAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterAndTestcodeOrderByProtocolordercodeDesc(
									1, objuser.getLssitemaster().getSitecode(), fromdate, todate,
									objuser.getLstprojectforfilter(), testcode, pageable);

					count = LSlogilabprotocoldetailRepository
							.countByRejectedAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterAndTestcode(1,
									objuser.getLssitemaster().getSitecode(), fromdate, todate,
									objuser.getLstprojectforfilter(), testcode);
				}

			} else if (objuser.getObjuser().getOrderselectiontype() == 6) {
				if (testcode == -1 && objuser.getLstprojectforfilter() == null) {

					lstorders = LSlogilabprotocoldetailRepository
							.findByOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyInOrderByProtocolordercodeDesc(
									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject, 1,
									objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, 1,
									objuser.getLssitemaster().getSitecode(), 2, objuser.getUsercode(), fromdate, todate,
									1, objuser.getLssitemaster().getSitecode(), 3, fromdate, todate,userlist,
									pageable);
//					lstorders = LSlogilabprotocoldetailRepository
//							.findByOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInOrderByProtocolordercodeDesc(
//									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject, pageable);
//
					count = LSlogilabprotocoldetailRepository
							.countByOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyInOrderByProtocolordercodeDesc(
									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject, 1,
									objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, 1,
									objuser.getLssitemaster().getSitecode(), 2, objuser.getUsercode(), fromdate, todate,
									1, objuser.getLssitemaster().getSitecode(), 3, fromdate, todate,userlist);
				} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {
//			    	  AndTestcode   AndLsprojectmaster
					lstorders = LSlogilabprotocoldetailRepository
							.findByOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndTestcodeOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndTestcodeOrOrdercancellAndSitecodeAndLsprojectmasterInAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrderByProtocolordercodeDesc(
									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject, testcode,
									1, objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, testcode, 1,
									objuser.getLssitemaster().getSitecode(), 2, objuser.getUsercode(), fromdate, todate,
									testcode, 1, objuser.getLssitemaster().getSitecode(), lstproject, 3, fromdate,
									todate, testcode, pageable);

					count = LSlogilabprotocoldetailRepository
							.countByOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndTestcodeOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndTestcodeOrOrdercancellAndSitecodeAndLsprojectmasterInAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrderByProtocolordercodeDesc(
									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject, testcode,
									1, objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, testcode, 1,
									objuser.getLssitemaster().getSitecode(), 2, objuser.getUsercode(), fromdate, todate,
									testcode, 1, objuser.getLssitemaster().getSitecode(), lstproject, 3, fromdate,
									todate, testcode);
				} else if (testcode == -1 && objuser.getLstprojectforfilter() != null) {
					lstorders = LSlogilabprotocoldetailRepository
							.findByOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterOrderByProtocolordercodeDesc(
									1, objuser.getLssitemaster().getSitecode(), fromdate, todate,
									objuser.getLstprojectforfilter(), pageable);

					count = LSlogilabprotocoldetailRepository
							.countByOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmaster(1,
									objuser.getLssitemaster().getSitecode(), fromdate, todate,
									objuser.getLstprojectforfilter());
				} else {
					lstorders = LSlogilabprotocoldetailRepository
							.findByOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterAndTestcodeOrderByProtocolordercodeDesc(
									1, objuser.getLssitemaster().getSitecode(), fromdate, todate,
									objuser.getLstprojectforfilter(), testcode, pageable);

					count = LSlogilabprotocoldetailRepository
							.countByOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterAndTestcode(1,
									objuser.getLssitemaster().getSitecode(), fromdate, todate,
									objuser.getLstprojectforfilter(), testcode);
				}

			}
		} else {
			if (objuser.getObjuser().getOrderselectiontype() == 1) {
				if (testcode == -1 && objuser.getLstprojectforfilter() == null) {
					lstorders = LSlogilabprotocoldetailRepository
							.findByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, 2,
									objuser.getUsercode(), fromdate, todate, 3, objuser.getUsercode(), fromdate, todate,
									pageable);
//							.findByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetween(
//									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, 2,
//									objuser.getUsercode(), fromdate, todate, pageable);

					count = LSlogilabprotocoldetailRepository
							.countByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, 2,
									objuser.getUsercode(), fromdate, todate, 3, objuser.getUsercode(), fromdate,
									todate);
				} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {
					lstorders = LSlogilabprotocoldetailRepository
							.findByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndTestcodeOrderByProtocolordercodeDesc(
									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, testcode, 2,
									objuser.getUsercode(), fromdate, todate, testcode, 3, objuser.getUsercode(),
									fromdate, todate, testcode, pageable);
					count = LSlogilabprotocoldetailRepository
							.countByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndTestcodeOrderByProtocolordercodeDesc(
									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, testcode, 2,
									objuser.getUsercode(), fromdate, todate, testcode, 3, objuser.getUsercode(),
									fromdate, todate, testcode);
				}
			} else if (objuser.getObjuser().getOrderselectiontype() == 2) {
				if (testcode == -1 && objuser.getLstprojectforfilter() == null) {
					lstorders = LSlogilabprotocoldetailRepository
							.findByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndOrderflagAndRejectedIsNullOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrderflagAndRejectedIsNullOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrderflagAndRejectedIsNullOrderByProtocolordercodeDesc(
									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, "R", 2,
									objuser.getUsercode(), fromdate, todate, "R", 3, objuser.getUsercode(), fromdate,
									todate, "R", pageable);

					count = LSlogilabprotocoldetailRepository
							.countByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndOrderflagAndRejectedIsNullOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrderflagAndRejectedIsNullOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrderflagAndRejectedIsNullOrderByProtocolordercodeDesc(
									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, "R", 2,
									objuser.getUsercode(), fromdate, todate, "R", 3, objuser.getUsercode(), fromdate,
									todate, "R");
				} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {
					lstorders = LSlogilabprotocoldetailRepository
							.findByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndOrderflagAndRejectedIsNullAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrderflagAndRejectedIsNullAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrderflagAndRejectedIsNullAndTestcodeOrderByProtocolordercodeDesc(
									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, "R", testcode, 2,
									objuser.getUsercode(), fromdate, todate, "R", testcode, 3, objuser.getUsercode(),
									fromdate, todate, "R", testcode, pageable);
					count = LSlogilabprotocoldetailRepository
							.countByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndOrderflagAndRejectedIsNullAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrderflagAndRejectedIsNullAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrderflagAndRejectedIsNullAndTestcodeOrderByProtocolordercodeDesc(
									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, "R", testcode, 2,
									objuser.getUsercode(), fromdate, todate, "R", testcode, 3, objuser.getUsercode(),
									fromdate, todate, "R", testcode);
				}

			} else if (objuser.getObjuser().getOrderselectiontype() == 3) {

				if (testcode == -1 && objuser.getLstprojectforfilter() == null) {
					lstorders = LSlogilabprotocoldetailRepository
							.findByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullOrderByProtocolordercodeDesc(
									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, "N", 2,
									objuser.getUsercode(), fromdate, todate, "N", 3, objuser.getUsercode(), fromdate,
									todate, "N", pageable);
					count = LSlogilabprotocoldetailRepository
							.countByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullOrderByProtocolordercodeDesc(
									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, "N", 2,
									objuser.getUsercode(), fromdate, todate, "N", 3, objuser.getUsercode(), fromdate,
									todate, "N");
				} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {
					lstorders = LSlogilabprotocoldetailRepository
							.findByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullAndTestcodeOrderByProtocolordercodeDesc(
									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, "N", testcode, 2,
									objuser.getUsercode(), fromdate, todate, "N", testcode, 3, objuser.getUsercode(),
									fromdate, todate, "N", testcode, pageable);
					count = LSlogilabprotocoldetailRepository
							.countByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullAndTestcodeOrderByProtocolordercodeDesc(
									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, "N", testcode, 2,
									objuser.getUsercode(), fromdate, todate, "N", testcode, 3, objuser.getUsercode(),
									fromdate, todate, "N", testcode);
				}
			} else if (objuser.getObjuser().getOrderselectiontype() == 5) {
				if (testcode == -1 && objuser.getLstprojectforfilter() == null) {
					lstorders = LSlogilabprotocoldetailRepository
						    .findByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndRejectedOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndRejectedOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndRejectedOrderByProtocolordercodeDesc(
						        1, objuser.getLssitemaster().getSitecode(), fromdate, todate, 1, 2,
						        objuser.getUsercode(), fromdate, todate, 1, 3, objuser.getUsercode(), fromdate, todate, 1, pageable);
					count = LSlogilabprotocoldetailRepository
							.countByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndRejectedOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndRejectedOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndRejectedOrderByProtocolordercodeDesc(
							        1, objuser.getLssitemaster().getSitecode(), fromdate, todate, 1, 2,
							        objuser.getUsercode(), fromdate, todate, 1, 3, objuser.getUsercode(), fromdate, todate, 1);
				} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {
					lstorders = LSlogilabprotocoldetailRepository
						    .findByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndRejectedAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndRejectedAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndRejectedAndTestcodeOrderByProtocolordercodeDesc(
						        1, objuser.getLssitemaster().getSitecode(), fromdate, todate, 1, testcode, 2,
						        objuser.getUsercode(), fromdate, todate, 1, testcode, 3, objuser.getUsercode(), fromdate, todate, 1, testcode, pageable);
					count = LSlogilabprotocoldetailRepository
							.countByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndRejectedAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndRejectedAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndRejectedAndTestcodeOrderByProtocolordercodeDesc(
							        1, objuser.getLssitemaster().getSitecode(), fromdate, todate, 1, testcode, 2,
							        objuser.getUsercode(), fromdate, todate, 1, testcode, 3, objuser.getUsercode(), fromdate, todate, 1, testcode);

				}
			} else if (objuser.getObjuser().getOrderselectiontype() == 6) {
				if (testcode == -1 && objuser.getLstprojectforfilter() == null) {
					lstorders = LSlogilabprotocoldetailRepository
						    .findByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndOrdercancellOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrdercancellOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrdercancellOrderByProtocolordercodeDesc(
						        1, objuser.getLssitemaster().getSitecode(), fromdate, todate, 1, 2,
						        objuser.getUsercode(), fromdate, todate, 1, 3, objuser.getUsercode(), fromdate, todate, 1, pageable);
					count = LSlogilabprotocoldetailRepository
							.countByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndOrdercancellOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrdercancellOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrdercancellOrderByProtocolordercodeDesc(
							        1, objuser.getLssitemaster().getSitecode(), fromdate, todate, 1, 2,
							        objuser.getUsercode(), fromdate, todate, 1, 3, objuser.getUsercode(), fromdate, todate, 1);
				} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {
					lstorders = LSlogilabprotocoldetailRepository
						    .findByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndOrdercancellAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrdercancellAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrdercancellAndTestcodeOrderByProtocolordercodeDesc(
						        1, objuser.getLssitemaster().getSitecode(), fromdate, todate, 1, testcode, 2,
						        objuser.getUsercode(), fromdate, todate, 1, testcode, 3, objuser.getUsercode(), fromdate, todate, 1, testcode, pageable);
					count = LSlogilabprotocoldetailRepository
							.countByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndOrdercancellAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrdercancellAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrdercancellAndTestcodeOrderByProtocolordercodeDesc(
							        1, objuser.getLssitemaster().getSitecode(), fromdate, todate, 1, testcode, 2,
							        objuser.getUsercode(), fromdate, todate, 1, testcode, 3, objuser.getUsercode(), fromdate, todate, 1, testcode);
				}

			}
		}

		lstorders.forEach(objorderDetail -> objorderDetail.setLstworkflow(objuser.getLstworkflow()));
		Collections.sort(lstorders, Collections.reverseOrder());
		mapOrders.put("orderlst", lstorders);
		mapOrders.put("count", count);
		return mapOrders;
	}

	public Map<String, Object> Getdashboardsheets(LSuserMaster objuser) {
		Date fromdate = objuser.getObjuser().getFromdate();
		Date todate = objuser.getObjuser().getTodate();
		Map<String, Object> mapSheets = new HashMap<String, Object>();
		List<LSuserMaster> lstteamuser = objuser.getObjuser().getTeamusers();
		List<Sheettemplateget> lstfile = new ArrayList<>();
		Pageable pageable = new PageRequest(objuser.getPagesize(), objuser.getPageperorder());
		long count = 0;
		if (objuser.getUsername().equals("Administrator")) {
			lstfile = lsfileRepository.findByCreatedateBetweenAndFilecodeGreaterThanAndLssitemasterAndViewoptionOrCreatedateBetweenAndFilecodeGreaterThanAndCreatebyInAndViewoptionOrderByFilecodeDesc(fromdate,
					todate, 1,objuser.getLssitemaster(),1,fromdate, todate,1,objuser,2, pageable);

			count = lsfileRepository.countByCreatedateBetweenAndFilecodeGreaterThanAndLssitemasterAndViewoptionOrCreatedateBetweenAndFilecodeGreaterThanAndCreatebyInAndViewoptionOrderByFilecodeDesc(fromdate,
					todate, 1,objuser.getLssitemaster(),1,fromdate, todate,1,objuser,2);
			lstfile.forEach(
					objFile -> objFile.setVersioncout(lsfileversionRepository.countByFilecode(objFile.getFilecode())));
//			mapSheets.put("Sheets",lstfile);
		} else {

			if (lstteamuser != null && lstteamuser.size() > 0) {
				lstteamuser.add(objuser);
				lstfile = lsfileRepository
						.findByFilecodeGreaterThanAndLssitemasterAndCreatedateBetweenAndViewoptionOrFilecodeGreaterThanAndCreatebyAndCreatedateBetweenAndViewoptionOrFilecodeGreaterThanAndCreatebyInAndCreatedateBetweenAndViewoptionOrderByFilecodeDesc(
								1, objuser.getLssitemaster(), fromdate, todate, 1, 1, objuser, fromdate, todate, 2, 1,
								lstteamuser, fromdate, todate, 3, pageable);

				count = lsfileRepository
						.countByFilecodeGreaterThanAndLssitemasterAndCreatedateBetweenAndViewoptionOrFilecodeGreaterThanAndCreatebyAndCreatedateBetweenAndViewoptionOrFilecodeGreaterThanAndCreatebyInAndCreatedateBetweenAndViewoptionOrderByFilecodeDesc(
								1, objuser.getLssitemaster(), fromdate, todate, 1, 1, objuser, fromdate, todate, 2, 1,
								lstteamuser, fromdate, todate, 3);

			} else {
				lstfile = lsfileRepository
						.findByFilecodeGreaterThanAndLssitemasterAndCreatedateBetweenAndViewoptionOrFilecodeGreaterThanAndCreatebyAndCreatedateBetweenAndViewoptionOrderByFilecodeDesc(
								1, objuser.getLssitemaster(), fromdate, todate, 1, 1, objuser, fromdate, todate, 2,
								pageable);
				count = lsfileRepository
						.countByFilecodeGreaterThanAndLssitemasterAndCreatedateBetweenAndViewoptionOrFilecodeGreaterThanAndCreatebyAndCreatedateBetweenAndViewoptionOrderByFilecodeDesc(
								1, objuser.getLssitemaster(), fromdate, todate, 1, 1, objuser, fromdate, todate, 2);
			}
			lstfile.forEach(
					objFile -> objFile.setVersioncout(lsfileversionRepository.countByFilecode(objFile.getFilecode())));

		}
		mapSheets.put("Sheets", lstfile);
		mapSheets.put("count", count);
		return mapSheets;
	}

	public Map<String, Object> getCountFromSheetTemplate(LSuserMaster objuser) {
		Date fromdate = objuser.getObjuser().getFromdate();
		Date todate = objuser.getObjuser().getTodate();
		Map<String, Object> mapSheets = new HashMap<String, Object>();
		List<LSuserMaster> lstteamuser = objuser.getObjuser().getTeamusers();

		if (objuser.getUsername().equals("Administrator")) {
			mapSheets.put("rejectedTemplate",
					lsfileRepository.countByCreatedateBetweenAndFilecodeGreaterThanAndRejectedOrderByFilecodeDesc(
							fromdate, todate, 1, 1));
			mapSheets.put("approvedTemplate",
					lsfileRepository
							.countByCreatedateBetweenAndFilecodeGreaterThanAndApprovedAndRejectedNotOrderByFilecodeDesc(
									fromdate, todate, 1, 1, 1));
			mapSheets.put("createdTemplate", lsfileRepository
					.countByCreatedateBetweenAndFilecodeGreaterThanAndApprovedIsNullAndRejectedNotOrderByFilecodeDesc(
							fromdate, todate, 1, 1));
			mapSheets.put("inprogressTemplate",
					lsfileRepository
							.countByCreatedateBetweenAndFilecodeGreaterThanAndApprovedAndRejectedOrderByFilecodeDesc(
									fromdate, todate, 1, 0, 0));
		} else {
			if (lstteamuser != null && lstteamuser.size() > 0) {
				lstteamuser.add(objuser);
				mapSheets.put("rejectedTemplate", lsfileRepository
						.countByFilecodeGreaterThanAndLssitemasterAndCreatedateBetweenAndViewoptionAndRejectedOrFilecodeGreaterThanAndCreatebyAndCreatedateBetweenAndViewoptionAndRejectedOrFilecodeGreaterThanAndCreatebyInAndCreatedateBetweenAndViewoptionAndRejectedOrderByFilecodeDesc(
								1, objuser.getLssitemaster(), fromdate, todate, 1, 1, 1, objuser, fromdate, todate, 2,
								1, 1, lstteamuser, fromdate, todate, 3, 1));
				mapSheets.put("approvedTemplate", lsfileRepository
						.countByFilecodeGreaterThanAndLssitemasterAndCreatedateBetweenAndViewoptionAndApprovedAndRejectedNotOrFilecodeGreaterThanAndCreatebyAndCreatedateBetweenAndViewoptionAndApprovedAndRejectedNotOrFilecodeGreaterThanAndCreatebyInAndCreatedateBetweenAndViewoptionAndApprovedAndRejectedNotOrderByFilecodeDesc(
								1, objuser.getLssitemaster(), fromdate, todate, 1, 1, 1, 1, objuser, fromdate, todate,
								2, 1, 1, 1, lstteamuser, fromdate, todate, 3, 1, 1));
				mapSheets.put("createdTemplate", lsfileRepository
						.countByFilecodeGreaterThanAndLssitemasterAndCreatedateBetweenAndViewoptionAndApprovedIsNullAndRejectedNotOrFilecodeGreaterThanAndCreatebyAndCreatedateBetweenAndViewoptionAndApprovedIsNullAndRejectedNotOrFilecodeGreaterThanAndCreatebyInAndCreatedateBetweenAndViewoptionAndApprovedIsNullAndRejectedNotOrderByFilecodeDesc(
								1, objuser.getLssitemaster(), fromdate, todate, 1, 1, 1, objuser, fromdate, todate, 2,
								1, 1, lstteamuser, fromdate, todate, 3, 1));
				mapSheets.put("inprogressTemplate", lsfileRepository
						.countByFilecodeGreaterThanAndLssitemasterAndCreatedateBetweenAndViewoptionAndApprovedAndRejectedNotOrFilecodeGreaterThanAndCreatebyAndCreatedateBetweenAndViewoptionAndApprovedAndRejectedNotOrFilecodeGreaterThanAndCreatebyInAndCreatedateBetweenAndViewoptionAndApprovedAndRejectedNotOrderByFilecodeDesc(
								1, objuser.getLssitemaster(), fromdate, todate, 1, 0, 1, 1, objuser, fromdate, todate,
								2, 0, 1, 1, lstteamuser, fromdate, todate, 3, 0, 1));

			} else {
				mapSheets.put("rejectedTemplate", lsfileRepository
						.countByFilecodeGreaterThanAndLssitemasterAndCreatedateBetweenAndViewoptionAndRejectedOrFilecodeGreaterThanAndCreatebyAndCreatedateBetweenAndViewoptionAndRejectedOrderByFilecodeDesc(
								1, objuser.getLssitemaster(), fromdate, todate, 1, 1, 1, objuser, fromdate, todate, 2,
								1));
				mapSheets.put("approvedTemplate", lsfileRepository
						.countByFilecodeGreaterThanAndLssitemasterAndCreatedateBetweenAndViewoptionAndApprovedAndRejectedNotOrFilecodeGreaterThanAndCreatebyAndCreatedateBetweenAndViewoptionAndApprovedAndRejectedNotOrderByFilecodeDesc(
								1, objuser.getLssitemaster(), fromdate, todate, 1, 1, 1, 1, objuser, fromdate, todate,
								2, 1, 1));
				mapSheets.put("inprogressTemplate", lsfileRepository
						.countByFilecodeGreaterThanAndLssitemasterAndCreatedateBetweenAndViewoptionAndApprovedAndRejectedNotOrFilecodeGreaterThanAndCreatebyAndCreatedateBetweenAndViewoptionAndApprovedAndRejectedNotOrderByFilecodeDesc(
								1, objuser.getLssitemaster(), fromdate, todate, 1, 0, 1, 1, objuser, fromdate, todate,
								2, 0, 1));
				mapSheets.put("createdTemplate", lsfileRepository
						.countByFilecodeGreaterThanAndLssitemasterAndCreatedateBetweenAndViewoptionAndApprovedIsNullAndRejectedNotOrFilecodeGreaterThanAndCreatebyAndCreatedateBetweenAndViewoptionAndApprovedIsNullAndRejectedNotOrderByFilecodeDesc(
								1, objuser.getLssitemaster(), fromdate, todate, 1, 1, 1, objuser, fromdate, todate, 2,
								1));
			}
		}
		return mapSheets;
	}

	public Map<String, Object> Getdashboardprotocoltemplate(LSuserMaster objuser) {
		Date fromdate = objuser.getObjuser().getFromdate();
		Date todate = objuser.getObjuser().getTodate();
		Map<String, Object> mapSheets = new HashMap<String, Object>();
		List<LSuserMaster> lstteamuser = objuser.getObjuser().getTeamusers();
		Pageable pageable = new PageRequest(objuser.getPagesize(), objuser.getPageperorder());
		long count = 0;
		if (objuser.getUsername().equals("Administrator")) {
			mapSheets.put("Sheets", LSProtocolMasterRepository.findByLssitemasterAndStatusAndCreatedateBetween(
					objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, pageable));
			mapSheets.put("count", LSProtocolMasterRepository.countByLssitemasterAndStatusAndCreatedateBetween(
					objuser.getLssitemaster().getSitecode(), 1, fromdate, todate));
		} else {

			List<Protocoltemplateget> lstprotocolmaster = new ArrayList<>();
			if (lstteamuser != null && lstteamuser.size() > 0) {
				lstteamuser.add(objuser);
				List<Integer> usercodelist = lstteamuser.stream().map(LSuserMaster::getUsercode)
						.collect(Collectors.toList());
				lstprotocolmaster = LSProtocolMasterRepository
						.findByLssitemasterAndStatusAndCreatedateBetweenAndViewoptionOrCreatedbyAndStatusAndCreatedateBetweenAndViewoptionOrCreatedbyInAndStatusAndCreatedateBetweenAndViewoptionOrderByProtocolmastercodeDesc(
								objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, 1, objuser.getUsercode(),
								1, fromdate, todate, 2, usercodelist, 1, fromdate, todate, 3, pageable);
				count = LSProtocolMasterRepository
						.countByLssitemasterAndStatusAndCreatedateBetweenAndViewoptionOrCreatedbyAndStatusAndCreatedateBetweenAndViewoptionOrCreatedbyInAndStatusAndCreatedateBetweenAndViewoptionOrderByProtocolmastercodeDesc(
								objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, 1, objuser.getUsercode(),
								1, fromdate, todate, 2, usercodelist, 1, fromdate, todate, 3);

			} else {
				lstprotocolmaster = LSProtocolMasterRepository
						.findByLssitemasterAndStatusAndCreatedateBetweenAndViewoptionOrCreatedbyAndStatusAndCreatedateBetweenAndViewoptionOrderByProtocolmastercodeDesc(
								objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, 1, objuser.getUsercode(),
								1, fromdate, todate, 2, pageable);

				count = LSProtocolMasterRepository
						.countByLssitemasterAndStatusAndCreatedateBetweenAndViewoptionOrCreatedbyAndStatusAndCreatedateBetweenAndViewoptionOrderByProtocolmastercodeDesc(
								objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, 1, objuser.getUsercode(),
								1, fromdate, todate, 2);
			}

			lstprotocolmaster = lstprotocolmaster.stream().distinct().collect(Collectors.toList());
			mapSheets.put("count", count);
			mapSheets.put("Sheets", lstprotocolmaster);
		}

		return mapSheets;
	}

	public Map<String, Object> getCountFromProtocolTemplate(LSuserMaster objuser) {
		Date fromdate = objuser.getObjuser().getFromdate();
		Date todate = objuser.getObjuser().getTodate();
		Map<String, Object> mapSheets = new HashMap<String, Object>();
		List<LSuserMaster> lstteamuser = objuser.getObjuser().getTeamusers();
		if (objuser.getUsername().equals("Administrator")) {
			mapSheets.put("rejectedTemplate",
					LSProtocolMasterRepository.countByStatusAndLssitemasterAndCreatedateBetweenAndRejected(1,
							objuser.getLssitemaster().getSitecode(), fromdate, todate, 1));
			mapSheets.put("approvedTemplate",
					LSProtocolMasterRepository
							.countByStatusAndLssitemasterAndCreatedateBetweenAndRejectedNotAndApproved(1,
									objuser.getLssitemaster().getSitecode(), fromdate, todate, 1, 1));
			mapSheets.put("createdTemplate",
					LSProtocolMasterRepository
							.countByStatusAndLssitemasterAndCreatedateBetweenAndRejectedNotAndApprovedIsNull(1,
									objuser.getLssitemaster().getSitecode(), fromdate, todate, 1));
			mapSheets.put("inprogressTemplate",
					LSProtocolMasterRepository
							.countByStatusAndLssitemasterAndCreatedateBetweenAndRejectedNotAndApproved(1,
									objuser.getLssitemaster().getSitecode(), fromdate, todate, 1, 0));
		} else {

			List<Protocoltemplateget> lstprotocolmaster = new ArrayList<>();
			long count = 0;
			if (lstteamuser != null && lstteamuser.size() > 0) {
				lstteamuser.add(objuser);
				List<Integer> usercodelist = lstteamuser.stream().map(LSuserMaster::getUsercode)
						.collect(Collectors.toList());

				count = LSProtocolMasterRepository
						.countByLssitemasterAndStatusAndCreatedateBetweenAndViewoptionAndRejectedOrCreatedbyAndStatusAndCreatedateBetweenAndViewoptionAndRejectedOrCreatedbyInAndStatusAndCreatedateBetweenAndViewoptionAndRejectedOrderByProtocolmastercodeDesc(
								objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, 1, 1,
								objuser.getUsercode(), 1, fromdate, todate, 2, 1, usercodelist, 1, fromdate, todate, 3,
								1);
//				lstprotocolmaster = lstprotocolmaster.stream().distinct().collect(Collectors.toList());
				mapSheets.put("rejectedTemplate", count);

				count = LSProtocolMasterRepository
						.countByLssitemasterAndStatusAndCreatedateBetweenAndViewoptionAndApprovedAndRejectedIsNullOrCreatedbyAndStatusAndCreatedateBetweenAndViewoptionAndApprovedAndRejectedIsNullOrCreatedbyInAndStatusAndCreatedateBetweenAndViewoptionAndApprovedAndRejectedIsNullOrderByProtocolmastercodeDesc(
								objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, 1, 1,
								objuser.getUsercode(), 1, fromdate, todate, 2, 1, usercodelist, 1, fromdate, todate, 3,
								1);
//				lstprotocolmaster = lstprotocolmaster.stream().distinct().collect(Collectors.toList());
				mapSheets.put("approvedTemplate", count);

				count = LSProtocolMasterRepository
						.countByLssitemasterAndStatusAndCreatedateBetweenAndViewoptionAndApprovedIsNullAndRejectedIsNullOrCreatedbyAndStatusAndCreatedateBetweenAndViewoptionAndApprovedIsNullAndRejectedIsNullOrCreatedbyInAndStatusAndCreatedateBetweenAndViewoptionAndApprovedIsNullAndRejectedIsNullOrderByProtocolmastercodeDesc(
								objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, 1, objuser.getUsercode(),
								1, fromdate, todate, 2, usercodelist, 1, fromdate, todate, 3);
//				lstprotocolmaster = lstprotocolmaster.stream().distinct().collect(Collectors.toList());
				mapSheets.put("createdTemplate", count);

				count = LSProtocolMasterRepository
						.countByLssitemasterAndStatusAndCreatedateBetweenAndViewoptionAndApprovedAndRejectedIsNullOrCreatedbyAndStatusAndCreatedateBetweenAndViewoptionAndApprovedAndRejectedIsNullOrCreatedbyInAndStatusAndCreatedateBetweenAndViewoptionAndApprovedAndRejectedIsNullOrderByProtocolmastercodeDesc(
								objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, 1, 0,
								objuser.getUsercode(), 1, fromdate, todate, 2, 0, usercodelist, 1, fromdate, todate, 3,
								0);
				lstprotocolmaster = lstprotocolmaster.stream().distinct().collect(Collectors.toList());
				mapSheets.put("inprogressTemplate", count);

			} else {
				count = LSProtocolMasterRepository
						.countByLssitemasterAndStatusAndCreatedateBetweenAndViewoptionAndRejectedOrCreatedbyAndStatusAndCreatedateBetweenAndViewoptionAndRejectedOrderByProtocolmastercodeDesc(
								objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, 1, 1,
								objuser.getUsercode(), 1, fromdate, todate, 2, 1);
//				lstprotocolmaster = lstprotocolmaster.stream().distinct().collect(Collectors.toList());
				mapSheets.put("rejectedTemplate", count);

				count = LSProtocolMasterRepository
						.countByLssitemasterAndStatusAndCreatedateBetweenAndViewoptionAndApprovedAndRejectedNotOrCreatedbyAndStatusAndCreatedateBetweenAndViewoptionAndApprovedAndRejectedNotOrderByProtocolmastercodeDesc(
								objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, 1, 1, 1,
								objuser.getUsercode(), 1, fromdate, todate, 2, 1, 1);
//				lstprotocolmaster = lstprotocolmaster.stream().distinct().collect(Collectors.toList());
				mapSheets.put("approvedTemplate", count);

				count = LSProtocolMasterRepository
						.countByLssitemasterAndStatusAndCreatedateBetweenAndViewoptionAndApprovedIsNullAndRejectedNotOrCreatedbyAndStatusAndCreatedateBetweenAndViewoptionAndApprovedIsNullAndRejectedNotOrderByProtocolmastercodeDesc(
								objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, 1, 1,
								objuser.getUsercode(), 1, fromdate, todate, 2, 1);
//				lstprotocolmaster = lstprotocolmaster.stream().distinct().collect(Collectors.toList());
				mapSheets.put("createdTemplate", count);

				count = LSProtocolMasterRepository
						.countByLssitemasterAndStatusAndCreatedateBetweenAndViewoptionAndApprovedAndRejectedNotOrCreatedbyAndStatusAndCreatedateBetweenAndViewoptionAndApprovedAndRejectedNotOrderByProtocolmastercodeDesc(
								objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, 1, 0, 1,
								objuser.getUsercode(), 1, fromdate, todate, 2, 0, 1);
//				lstprotocolmaster = lstprotocolmaster.stream().distinct().collect(Collectors.toList());
				mapSheets.put("inprogressTemplate", count);
			}
		}
		return mapSheets;
	}

	public Map<String, Object> Getdashboardparameters(LSuserMaster objuser) {
		Date fromdate = objuser.getObjuser().getFromdate();
		Date todate = objuser.getObjuser().getTodate();
		LSuserMaster objupdateduser = lsuserMasterRepository.findByusercode(objuser.getUsercode());
		Map<String, Object> mapOrders = new HashMap<String, Object>();

		if (objupdateduser.getUsername().equals("Administrator")) {
			List<LSparsedparameters> lstparsedparam = lsparsedparametersRespository.getallrecords();
			mapOrders.put("ParsedParameters", lstparsedparam);
		} else {
			List<LSuserteammapping> lstteammap = lsuserteammappingRepository.findBylsuserMaster(objupdateduser);
			List<LSusersteam> lstteam = lsusersteamRepository.findByLsuserteammappingIn(lstteammap);
			List<LSprojectmaster> lstproject = lsprojectmasterRepository.findByLsusersteamIn(lstteam);

			List<LSlogilablimsorderdetail> lstCompletedorder = lslogilablimsorderdetailRepository
					.findByOrderflagAndLsprojectmasterInAndCompletedtimestampBetweenOrderByBatchcodeDesc("R",
							lstproject, fromdate, todate);

			List<Long> lstBatchcode = lstCompletedorder.stream().map(LSlogilablimsorderdetail::getBatchcode)
					.collect(Collectors.toList());

			List<LSparsedparameters> lstparsedparam = new ArrayList<LSparsedparameters>();
			if (lstBatchcode != null && lstBatchcode.size() > 0) {
				lstparsedparam = lsparsedparametersRespository.getByBatchcodeIn(lstBatchcode);
			}

			mapOrders.put("ParsedParameters", lstparsedparam);

		}

		if (objuser.getObjsilentaudit() != null) {
			objuser.getObjsilentaudit().setTableName("LSlogilablimsorderdetail");
			lscfttransactionRepository.save(objuser.getObjsilentaudit());
		}

		return mapOrders;
	}

	public Map<String, Object> Getdashboardactivities(LSuserMaster objuser) {
		Date fromdate = objuser.getObjuser().getFromdate();
		Date todate = objuser.getObjuser().getTodate();
		Map<String, Object> mapOrders = new HashMap<String, Object>();

		if (objuser.getUsername().equals("Administrator")) {
			mapOrders.put("activities",
					lsactivityRepository.findTop20ByActivityDateBetweenOrderByActivitycodeDesc(fromdate, todate));
			mapOrders.put("activitiescount", lsactivityRepository.countByActivityDateBetween(fromdate, todate));
		} else {
			mapOrders.put("activities",
					lsactivityRepository.findTop20ByActivityDateBetweenOrderByActivitycodeDesc(fromdate, todate));
			mapOrders.put("activitiescount", lsactivityRepository.count());
		}

		if (objuser.getObjsilentaudit() != null) {
			objuser.getObjsilentaudit().setTableName("LSlogilablimsorderdetail");
			lscfttransactionRepository.save(objuser.getObjsilentaudit());
		}

		return mapOrders;
	}

	public Map<String, Object> Getordersharebyme(LSuserMaster objuser) {
		Date fromdate = objuser.getObjuser().getFromdate();
		Date todate = objuser.getObjuser().getTodate();
		Map<String, Object> mapOrders = new HashMap<String, Object>();
		mapOrders.put("orders", lsordersharedbyRepository
				.findBySharebyunifiedidAndSharedonBetweenOrSharebyunifiedidAndUnsharedonBetween(
						objuser.getUnifieduserid(), fromdate, todate, objuser.getUnifieduserid(), fromdate, todate));

		return mapOrders;
	}

	public Map<String, Object> Getordersharetome(LSuserMaster objuser) {
		Date fromdate = objuser.getObjuser().getFromdate();
		Date todate = objuser.getObjuser().getTodate();
		Map<String, Object> mapOrders = new HashMap<String, Object>();
		mapOrders.put("orders", lsordersharetoRepository.findBySharetounifiedidAndSharedonBetweenAndSharestatus(
				objuser.getUnifieduserid(), fromdate, todate, 1));

		return mapOrders;
	}

	public List<LSactivity> GetActivitiesonLazy(LSactivity objactivities) {
		List<LSactivity> lstactivities = new ArrayList<LSactivity>();
		lstactivities = lsactivityRepository
				.findTop20ByActivitycodeLessThanOrderByActivitycodeDesc(objactivities.getActivitycode());
		return lstactivities;
	}

	public Logilabordermaster Getorder(LSlogilablimsorderdetail objorder) {
		Logilaborders objupdatedorder = lslogilablimsorderdetailRepository.findByBatchcode(objorder.getBatchcode());
		objupdatedorder.setLstworkflow(objorder.getLstworkflow());
		return objupdatedorder;
	}

	public Map<String, Object> Getordersinuserworkflow(LSuserMaster objuser) {
		List<LSprojectmaster> lstproject = objuser.getLstproject();
		List<LSworkflow> lstworkflow = objuser.getLstworkflow();

		Date fromdate = objuser.getObjuser().getFromdate();
		Date todate = objuser.getObjuser().getTodate();
		Map<String, Object> mapOrders = new HashMap<String, Object>();

		if (lstproject != null && lstworkflow != null) {
			List<Logilabordermaster> lstorders = lslogilablimsorderdetailRepository
					.findByOrderflagAndLsprojectmasterInAndLsworkflowInAndCreatedtimestampBetween("N", lstproject,
							lstworkflow, fromdate, todate);
			lstorders.forEach(objorder -> objorder.setLstworkflow(lstworkflow));
			mapOrders.put("orders", lstorders);
		} else {
			mapOrders.put("orders", new ArrayList<Logilabordermaster>());
		}

		return mapOrders;
	}

	public List<Repositorymaster> Getallrepositories(LSuserMaster objuser) {

		Date fromdate = objuser.getObjuser().getFromdate();
		Date todate = objuser.getObjuser().getTodate();

//		return LsrepositoriesRepository.findBysitecodeOrderByRepositorycodeAsc(objuser.getLssitemaster().getSitecode());

		return LsrepositoriesRepository.findBySitecodeAndAddedonBetweenOrderByRepositorycodeAsc(
				objuser.getLssitemaster().getSitecode(), fromdate, todate);
	}

	public Map<String, Object> Getapprovedsheet(Integer[] lsfile) {
		ArrayList<Integer> listobjfilecode = new ArrayList<>(lsfile.length);
//		listobjfilecode.ass
		Collections.addAll(listobjfilecode, lsfile);
		Map<String, Object> obj = new HashMap<String, Object>();
		if (listobjfilecode.get(0) == 1) {
			listobjfilecode.remove(0);
			List<Sheettemplateget> fileobj = lsfileRepository.findByFilecodeIn(listobjfilecode);
			fileobj.forEach(
					objFile -> objFile.setVersioncout(lsfileversionRepository.countByFilecode(objFile.getFilecode())));

			obj.put("Sheet", fileobj);
		} else {
			listobjfilecode.remove(0);
			List<LSprotocolmaster> protocolobj = LSProtocolMasterRepository.findByProtocolmastercodeIn(listobjfilecode);
			obj.put("Protocol", protocolobj);
		}

		return obj;
	}
}
