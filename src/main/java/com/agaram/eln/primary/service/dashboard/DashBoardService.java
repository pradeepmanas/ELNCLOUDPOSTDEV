package com.agaram.eln.primary.service.dashboard;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.agaram.eln.primary.commonfunction.commonfunction;
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

	public Map<String, Object> Getdashboarddetails(LSuserMaster objuser) throws ParseException {

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
			objuser.getObjsilentaudit().setTransactiondate(commonfunction.getCurrentUtcTime());
			lscfttransactionRepository.save(objuser.getObjsilentaudit());
		}

		return mapOrders;
	}

	public Map<String, Object> Getdashboarddetailsonfilters(LSuserMaster objuser) throws ParseException {
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
			objuser.getObjsilentaudit().setTransactiondate(commonfunction.getCurrentUtcTime());
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
		List<Integer> userlist = objuser.getUsernotify() != null
				? objuser.getUsernotify().stream().map(LSuserMaster::getUsercode).collect(Collectors.toList())
				: new ArrayList<Integer>();

		if (objuser.getObjuser().getOrderfor() != 1) {
			if (lstproject != null && lstproject.size() > 0) {

				if (testcode == -1 && objuser.getLstprojectforfilter() == null) {

					mapOrders.put("orders", LSlogilabprotocoldetailRepository
							.countByLsprojectmasterInAndCreatedtimestampBetweenAndSitecodeOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatebyAndCreatedtimestampBetweenOrLsprojectmasterInAndViewoptionAndSitecodeAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndCreatebyInOrderByProtocolordercodeDesc(
									lstproject, fromdate, todate, objuser.getLssitemaster().getSitecode(), 1,
									objuser.getLssitemaster().getSitecode(), fromdate, todate, 2,
									objuser.getLssitemaster().getSitecode(), objuser.getUsercode(), fromdate, todate,
									lstproject, 3, objuser.getLssitemaster().getSitecode(), fromdate, todate, 3,
									objuser.getLssitemaster().getSitecode(), fromdate, todate, userlist));

					mapOrders.put("pendingorder", LSlogilabprotocoldetailRepository
							.countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndOrdercancellIsNullAndRejectedIsNullOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndRejectedIsNullOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndRejectedIsNullOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndRejectedIsNullAndCreatebyInOrderByProtocolordercodeDesc(
									"N", objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject, "N",
									objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, "N",
									objuser.getLssitemaster().getSitecode(), 2, objuser.getUsercode(), fromdate, todate,
									"N", objuser.getLssitemaster().getSitecode(), 3, fromdate, todate, userlist));
					mapOrders.put("completedorder", LSlogilabprotocoldetailRepository
							.countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndRejectedIsNullOrOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenOrOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenOrOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyInOrderByProtocolordercodeDesc(
									"R", objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject, "R",
									objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, "R",
									objuser.getLssitemaster().getSitecode(), 2, objuser.getUsercode(), fromdate, todate,
									"R", objuser.getLssitemaster().getSitecode(), 3, fromdate, todate, userlist));

					mapOrders.put("rejectedorder", LSlogilabprotocoldetailRepository
							.countByRejectedAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInOrRejectedAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenOrRejectedAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenOrRejectedAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyInOrderByProtocolordercodeDesc(
									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject, 1,
									objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, 1,
									objuser.getLssitemaster().getSitecode(), 2, objuser.getUsercode(), fromdate, todate,
									1, objuser.getLssitemaster().getSitecode(), 3, fromdate, todate, userlist));
					mapOrders.put("canceledorder", LSlogilabprotocoldetailRepository
							.countByOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyInOrderByProtocolordercodeDesc(
									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject, 1,
									objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, 1,
									objuser.getLssitemaster().getSitecode(), 2, objuser.getUsercode(), fromdate, todate,
									1, objuser.getLssitemaster().getSitecode(), 3, fromdate, todate, userlist));

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
									"N", objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject,
									testcode, "N", objuser.getLssitemaster().getSitecode(), 1, fromdate, todate,
									testcode, "N", objuser.getLssitemaster().getSitecode(), 2, fromdate,
									objuser.getUsercode(), todate, testcode, "N",
									objuser.getLssitemaster().getSitecode(), lstproject, 3, fromdate, todate,
									testcode));

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
									"N", objuser.getLssitemaster().getSitecode(), fromdate, todate,
									objuser.getLstprojectforfilter()));

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
									"N", objuser.getLssitemaster().getSitecode(), fromdate, todate,
									objuser.getLstprojectforfilter(), testcode));

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
									objuser.getUsercode(), fromdate, todate, 1, 3, objuser.getUsercode(), fromdate,
									todate, 1));
					mapOrders.put("canceledorder", LSlogilabprotocoldetailRepository
							.countByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndOrdercancellOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrdercancellOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrdercancellOrderByProtocolordercodeDesc(
									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, 1, 2,
									objuser.getUsercode(), fromdate, todate, 1, 3, objuser.getUsercode(), fromdate,
									todate, 1));

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
									objuser.getUsercode(), fromdate, todate, 1, testcode, 3, objuser.getUsercode(),
									fromdate, todate, 1, testcode));

					mapOrders.put("canceledorder", LSlogilabprotocoldetailRepository
							.countByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndOrdercancellAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrdercancellAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrdercancellAndTestcodeOrderByProtocolordercodeDesc(
									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, 1, testcode, 2,
									objuser.getUsercode(), fromdate, todate, 1, testcode, 3, objuser.getUsercode(),
									fromdate, todate, 1, testcode));

				}
				mapOrders.put("onproces", 0);
			}
		}

		else {

			long lstUserorder = 0;
			long lstlimscompleted = 0;
			long lstordersinprogress = 0;
			long lstpending = 0;
			long lstRejected = 0;
			long lstCancelled = 0;
			List<Integer> lstsampleint = lssamplemasterrepository
					.getDistinctByLssitemasterSitecodeAndStatus(objuser.getLssitemaster().getSitecode(), 1);
			List<LSsamplemaster> lstsample1 = new ArrayList<>();
			LSsamplemaster sample = null;
			if (lstsampleint.size() > 0) {
				for (Integer item : lstsampleint) {
					sample = new LSsamplemaster();
					sample.setSamplecode(item);
					lstsample1.add(sample);
					sample = null; // Set sample to null after adding it to the list
				}
			}
//			List<LSsamplemaster> lstsample1 = lssamplemasterrepository
//					.findByLssitemasterAndStatus(objuser.getLssitemaster(), 1);
			if (lstproject != null && lstproject.size() > 0) {
				if (testcode == -1 && objuser.getLstprojectforfilter() == null) {
					if (lstproject != null && lstproject.size() > 0) {
//						lstUserorder = lslogilablimsorderdetailRepository
//								.countByLsprojectmasterInAndCreatedtimestampBetweenOrFiletypeAndCreatedtimestampBetween(
//										lstproject, fromdate, todate, 0, fromdate, todate);

//						lstUserorder = lslogilablimsorderdetailRepository
//								.countByLsprojectmasterInAndCreatedtimestampBetweenOrFiletypeAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenOrViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndLsprojectmasterIsNullOrderByBatchcodeDesc(
//										lstproject, fromdate, todate, 
//										0, fromdate, todate, 
//										1, objuser, fromdate, todate,
//										2, objuser, fromdate, todate, 
//										3, objuser, fromdate, todate);

//						lstUserorder = lslogilablimsorderdetailRepository
//								.countByLsprojectmasterInAndCreatedtimestampBetweenOrFiletypeAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndLssamplemasterInAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenOrViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndLsprojectmasterInOrViewoptionAndLsuserMasterInAndCreatedtimestampBetweenAndLsprojectmasterIsNullOrderByBatchcodeDesc(
//								lstproject, fromdate, todate, 
//								0, fromdate, todate, 
//								lstsample1,fromdate, todate,
//								1, objuser, fromdate, todate,
//								2, objuser, fromdate, todate, 
//								3,objuser,fromdate, todate,lstproject,
//                                3, objuser.getUsernotify(), fromdate, todate);
						lstUserorder = lslogilablimsorderdetailRepository
								.countByLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullOrFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullOrLsprojectmasterIsNullAndLssamplemasterInAndCreatedtimestampBetweenAndAssignedtoIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndAssignedtoIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndAssignedtoIsNullOrViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndLsprojectmasterInAndAssignedtoIsNullOrViewoptionAndLsuserMasterInAndCreatedtimestampBetweenAndLsprojectmasterIsNullAndAssignedtoIsNullOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullOrAssignedtoAndCreatedtimestampBetweenOrderByBatchcodeDesc(
										lstproject, fromdate, todate, 0, fromdate, todate, lstsample1, fromdate, todate,
										1, objuser, fromdate, todate, 2, objuser, fromdate, todate, 3, objuser,
										fromdate, todate, lstproject, 3, objuser.getUsernotify(), fromdate, todate,
										objuser, objuser, fromdate, todate, objuser, fromdate, todate);

//						lstlimscompleted = lslogilablimsorderdetailRepository
//								.countByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullOrOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndOrdercancellIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndLsprojectmasterInAndOrdercancellIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndLsprojectmasterIsNullAndOrdercancellIsNullOrderByBatchcodeDesc(
//										"R", lstproject, fromdate, todate, 3, "R", lstproject, fromdate, todate, "R", 0,
//										fromdate, todate, "R", 1, objuser, fromdate, todate, 3, "R", 2, objuser,
//										fromdate, todate, 3, "R", 3, objuser, fromdate, todate, 3, lstproject, "R", 1,
//										objuser, fromdate, todate, "R", 2, objuser, fromdate, todate, "R", 3, objuser,
//										fromdate, todate); #
						
						lstlimscompleted = lslogilablimsorderdetailRepository
								.countByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndLssamplemasterInAndCreatedtimestampBetweenAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenAndAssignedtoIsNullOrOrderflagAndLsprojectmasterInAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndOrdercancellIsNullAndCreatedtimestampBetweenAndLsuserMasterInAndAssignedtoIsNull(
									"R", lstproject, fromdate, todate, 
									"R", lstsample1, fromdate, todate, 
									"R", 0, fromdate, todate, 
									"R", 1, objuser, fromdate, todate, 
									"R", 2, objuser, fromdate, todate, 
									"R", lstproject, 3, objuser, fromdate, todate, 
									"R", 3, fromdate, todate, objuser.getUsernotify());

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
						
//						lstpending = lslogilablimsorderdetailRepository
//								.countByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndOrdercancellIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenOrOrderflagAndLsprojectmasterIsNullAndLssamplemasterInAndCreatedtimestampBetweenOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenOrOrderflagAndLsprojectmasterInAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterInAndOrdercancellIsNullAndCreatedtimestampBetweenOrderByBatchcodeDesc(
//										"N", lstproject, fromdate, todate, "N", 0, fromdate, todate, "N", lstsample1,
//										fromdate, todate, "N", 1, objuser, fromdate, todate, "N", 2, objuser, fromdate,
//										todate, "N", lstproject, 3, objuser, fromdate, todate, "N", 3,
//										objuser.getUsernotify(), fromdate, todate); #
						
//						lstpending = lslogilablimsorderdetailRepository
//								.countByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndLssamplemasterInAndCreatedtimestampBetweenAndAssignedtoIsNullOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenAndAssignedtoIsNullOrOrderflagAndLsprojectmasterInAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterInAndOrdercancellIsNullAndCreatedtimestampBetweenAndAssignedtoIsNull(
//										"N", lstproject, fromdate, todate,
//										"N", 0, fromdate, todate,
//										"N", lstsample1,fromdate, todate,
//										"N", 1, objuser, fromdate, todate,
//										"N", 2, objuser, fromdate,todate,
//										"N", lstproject, 3, objuser, fromdate, todate,
//										"N", 3,objuser.getUsernotify(), fromdate, todate);
						
						lstpending = lslogilablimsorderdetailRepository
								.countByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndLssamplemasterInAndCreatedtimestampBetweenAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenAndAssignedtoIsNullOrOrderflagAndLsprojectmasterInAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndOrdercancellIsNullAndCreatedtimestampBetweenAndLsuserMasterInAndAssignedtoIsNull(
									"N", lstproject, fromdate, todate, 
									"N", lstsample1, fromdate, todate, 
									"N", 0, fromdate, todate, 
									"N", 1, objuser, fromdate, todate, 
									"N", 2, objuser, fromdate, todate, 
									"N", lstproject, 3, objuser, fromdate, todate, 
									"N", 3, fromdate, todate, objuser.getUsernotify());
						
						
//						lstpending = lslogilablimsorderdetailRepository
//								.countByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndOrdercancellIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterInAndOrdercancellIsNullAndCreatedtimestampBetweenOrderByBatchcodeDesc(
//										"N", lstproject, fromdate, todate, "N", 0, fromdate, todate, "N", 1, objuser,
//										fromdate, todate, "N", 2, objuser, fromdate, todate, "N", 3,
//										objuser.getUsernotify(), fromdate, todate);

//						lstRejected = lslogilablimsorderdetailRepository
//								.countByApprovelstatusAndLsprojectmasterInAndCreatedtimestampBetween(3, lstproject,
//										fromdate, todate);

						lstRejected = lslogilablimsorderdetailRepository
								.countByApprovelstatusAndLsprojectmasterInAndCreatedtimestampBetweenOrApprovelstatusAndLsprojectmasterIsNullAndLssamplemasterInAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusAndLsprojectmasterIsNull(
										3, lstproject, fromdate, todate, 3, lstsample1, fromdate, todate, 1, objuser,
										fromdate, todate, 3, 2, objuser, fromdate, todate, 3, 3, objuser, fromdate,
										todate, 3, 3, objuser, fromdate, todate, 3);
//						lstCancelled = lslogilablimsorderdetailRepository
//								.countByOrdercancellAndLsprojectmasterInAndCreatedtimestampBetween(1, lstproject,
//										fromdate, todate);

						lstCancelled = lslogilablimsorderdetailRepository
								.countByOrdercancellAndLsprojectmasterInAndCreatedtimestampBetweenOrOrdercancellAndLsprojectmasterIsNullAndLssamplemasterInAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndLsprojectmasterIsNull(
										1, lstproject, fromdate, todate, 1, lstsample1, fromdate, todate, 1, objuser,
										fromdate, todate, 1, 2, objuser, fromdate, todate, 1, 3, objuser, fromdate,
										todate, 1);

					}
				} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {
//		    	  AndLsprojectmaster     AndTestcode
					if (lstproject != null && lstproject.size() > 0) {
//						lstUserorder = lslogilablimsorderdetailRepository
//								.countByLsprojectmasterInAndCreatedtimestampBetweenAndTestcodeOrFiletypeAndCreatedtimestampBetweenAndTestcodeOrderByBatchcodeDesc(
//										lstproject, fromdate, todate, testcode, 0, fromdate, todate, testcode);
						lstUserorder = lslogilablimsorderdetailRepository
								.countByLsprojectmasterInAndCreatedtimestampBetweenAndTestcodeAndAssignedtoIsNullOrFiletypeAndCreatedtimestampBetweenAndTestcodeAndAssignedtoIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndTestcodeAndAssignedtoIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndTestcodeAndAssignedtoIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndTestcodeAndAssignedtoIsNullOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndTestcodeOrAssignedtoAndCreatedtimestampBetweenAndTestcodeOrderByBatchcodeDesc(
										lstproject, fromdate, todate, testcode, 0, fromdate, todate, testcode, 1,
										objuser, fromdate, todate, testcode, 2, objuser, fromdate, todate, testcode, 3,
										objuser, fromdate, todate, testcode, objuser, objuser, fromdate, todate,
										testcode, objuser, fromdate, todate, testcode);

						lstlimscompleted = lslogilablimsorderdetailRepository
								.countByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndLssamplemasterInAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndLssamplemasterInAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndLsprojectmasterInAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndLsprojectmasterIsNullAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullOrOrderflagAndAssignedtoAndCreatedtimestampBetweenOrderByBatchcodeDesc(
										"R", lstproject, fromdate, todate, 3, testcode, "R", lstproject, fromdate,
										todate, testcode, "R", lstsample1, fromdate, todate, 3, testcode, "R",
										lstsample1, fromdate, todate, testcode, "R", 0, fromdate, todate, testcode, "R",
										1, objuser, fromdate, todate, 3, testcode, "R", 2, objuser, fromdate, todate, 3,
										testcode, "R", 3, objuser, fromdate, todate, 3, lstproject, testcode, "R", 1,
										objuser, fromdate, todate, testcode, "R", 2, objuser, fromdate, todate,
										testcode, "R", 3, objuser, fromdate, todate, testcode, "R", objuser, objuser,
										fromdate, todate, testcode, "R", objuser, fromdate, todate);
//								.countByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusNotAndTestcodeAndOrdercancellIsNullOrOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusIsNullAndTestcodeAndOrdercancellIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndTestcodeAndOrdercancellIsNullOrderByBatchcodeDesc(
//										"R", lstproject, fromdate, todate, 3, testcode, "R", lstproject, fromdate,
//										todate, testcode, "R", 0, fromdate, todate, testcode);
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
							.countByLsprojectmasterAndCreatedtimestampBetweenAndAssignedtoIsNullOrFiletypeAndLsprojectmasterAndCreatedtimestampBetweenAndAssignedtoIsNullOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndLsprojectmasterOrAssignedtoAndCreatedtimestampBetweenAndLsprojectmasterOrderByBatchcodeDesc(
									objuser.getLstprojectforfilter(), fromdate, todate, 0,
									objuser.getLstprojectforfilter(), fromdate, todate, objuser, objuser, fromdate,
									todate, objuser.getLstprojectforfilter(), objuser, fromdate, todate,
									objuser.getLstprojectforfilter());
//							.countByLsprojectmasterAndCreatedtimestampBetweenOrFiletypeAndLsprojectmasterAndCreatedtimestampBetweenOrderByBatchcodeDesc(
//									objuser.getLstprojectforfilter(), fromdate, todate, 0,
//									objuser.getLstprojectforfilter(), fromdate, todate);
					lstlimscompleted = lslogilablimsorderdetailRepository
							.countByOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndLsprojectmasterOrOrderflagAndAssignedtoAndCreatedtimestampBetweenAndLsprojectmasterOrderByBatchcodeDesc(
									"R", objuser.getLstprojectforfilter(), fromdate, todate, 3, "R",
									objuser.getLstprojectforfilter(), fromdate, todate, "R", 0, fromdate, todate,
									objuser.getLstprojectforfilter(), "R", objuser, objuser, fromdate, todate,
									objuser.getLstprojectforfilter(), "R", objuser, fromdate, todate,
									objuser.getLstprojectforfilter());
//							.countByOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullOrOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterAndOrdercancellIsNullOrderByBatchcodeDesc(
//									"R", objuser.getLstprojectforfilter(), fromdate, todate, 3, "R",
//									objuser.getLstprojectforfilter(), fromdate, todate, "R", 0, fromdate, todate,
//									objuser.getLstprojectforfilter());
					lstordersinprogress = lslogilablimsorderdetailRepository
							.countByOrderflagAndLsprojectmasterAndApprovelstatusAndApprovedAndCreatedtimestampBetweenOrderByBatchcodeDesc(
									"N", objuser.getLstprojectforfilter(), 1, 1, fromdate, todate);
					lstpending = lslogilablimsorderdetailRepository
							.countByOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterAndAssignedtoIsNullOrOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndLsprojectmasterOrOrderflagAndAssignedtoAndCreatedtimestampBetweenAndLsprojectmaster(
									"N", objuser.getLstprojectforfilter(), fromdate, todate, "N", 0, fromdate, todate,
									objuser.getLstprojectforfilter(), objuser, objuser, fromdate, todate,
                                objuser.getLstprojectforfilter(), "N", objuser, objuser, fromdate, todate, objuser.getLstprojectforfilter(), "N", objuser, fromdate, todate,
									objuser.getLstprojectforfilter());

//							.countByOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndOrdercancellIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterOrderByBatchcodeDesc(
//									"N", objuser.getLstprojectforfilter(), fromdate, todate, "N", 0, fromdate, todate,
//									objuser.getLstprojectforfilter());
					lstRejected = lslogilablimsorderdetailRepository
							.countByApprovelstatusAndLsprojectmasterAndCreatedtimestampBetween(3,
									objuser.getLstprojectforfilter(), fromdate, todate);
					lstCancelled = lslogilablimsorderdetailRepository
							.countByOrdercancellAndLsprojectmasterAndCreatedtimestampBetween(1,
									objuser.getLstprojectforfilter(), fromdate, todate);

				} else {
					lstUserorder = lslogilablimsorderdetailRepository
							.countByLsprojectmasterAndCreatedtimestampBetweenAndTestcodeAndAssignedtoIsNullOrFiletypeAndLsprojectmasterAndCreatedtimestampBetweenAndTestcodeAndAssignedtoIsNullOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndTestcodeAndLsprojectmasterOrAssignedtoAndCreatedtimestampBetweenAndTestcodeAndLsprojectmasterOrderByBatchcodeDesc(
									objuser.getLstprojectforfilter(), fromdate, todate, testcode, 0,
									objuser.getLstprojectforfilter(), fromdate, todate, testcode, objuser, objuser,
									fromdate, todate, testcode, objuser.getLstprojectforfilter(), objuser, fromdate,
									testcode, objuser.getLstprojectforfilter(), todate);
//							.countByLsprojectmasterAndCreatedtimestampBetweenAndTestcodeOrFiletypeAndLsprojectmasterAndCreatedtimestampBetweenAndTestcodeOrderByBatchcodeDesc(
//									objuser.getLstprojectforfilter(), fromdate, todate, testcode, 0,
//									objuser.getLstprojectforfilter(), fromdate, todate, testcode);
					lstlimscompleted = lslogilablimsorderdetailRepository
							.countByOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndApprovelstatusNotAndTestcodeAndOrdercancellIsNullOrOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndTestcodeAndOrdercancellIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterAndTestcodeAndOrdercancellIsNullOrOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndLsprojectmasterAndTestcodeOrOrderflagAndAssignedtoAndCreatedtimestampBetweenAndLsprojectmasterAndTestcodeOrderByBatchcodeDesc(
									"R", objuser.getLstprojectforfilter(), fromdate, todate, 3, testcode, "R",
									objuser.getLstprojectforfilter(), fromdate, todate, testcode, "R", 0, fromdate,
									todate, objuser.getLstprojectforfilter(), testcode, "R", objuser, objuser, fromdate,
									todate, objuser.getLstprojectforfilter(), testcode, "R", objuser, fromdate, todate,
									objuser.getLstprojectforfilter(), testcode);

//							.countByOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndApprovelstatusNotAndTestcodeAndOrdercancellIsNullOrOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndTestcodeAndOrdercancellIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterAndTestcodeAndOrdercancellIsNullOrderByBatchcodeDesc(
//									"R", objuser.getLstprojectforfilter(), fromdate, todate, 3, testcode, "R",
//									objuser.getLstprojectforfilter(), fromdate, todate, testcode, "R", 0, fromdate,
//									todate, objuser.getLstprojectforfilter(), testcode);
					lstordersinprogress = lslogilablimsorderdetailRepository
							.countByOrderflagAndLsprojectmasterAndApprovelstatusAndApprovedAndCreatedtimestampBetweenOrderByBatchcodeDesc(
									"N", objuser.getLstprojectforfilter(), 1, 1, fromdate, todate);
					lstpending = lslogilablimsorderdetailRepository
							.countByOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndOrdercancellIsNullAndTestcodeAndAssignedtoIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterAndTestcodeAndAssignedtoIsNullOrOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndLsprojectmasterAndTestcodeOrOrderflagAndAssignedtoAndCreatedtimestampBetweenAndLsprojectmasterAndTestcodeOrderByBatchcodeDesc(
									"N", objuser.getLstprojectforfilter(), fromdate, todate, testcode, "N", 0, fromdate,
									todate, objuser.getLstprojectforfilter(), testcode, "N", objuser, objuser, fromdate,
									todate, objuser.getLstprojectforfilter(), testcode, "N", objuser, fromdate, todate,
									objuser.getLstprojectforfilter(), testcode);
//							.countByOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndOrdercancellIsNullAndTestcodeOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterAndTestcodeOrderByBatchcodeDesc(
//									"N", objuser.getLstprojectforfilter(), fromdate, todate, testcode, "N", 0, fromdate,
//									todate, objuser.getLstprojectforfilter(), testcode);
					lstRejected = lslogilablimsorderdetailRepository
							.countByApprovelstatusAndLsprojectmasterAndCreatedtimestampBetweenAndTestcode(3,
									objuser.getLstprojectforfilter(), fromdate, todate, testcode);
					lstCancelled = lslogilablimsorderdetailRepository
							.countByOrdercancellAndLsprojectmasterAndCreatedtimestampBetweenAndTestcode(1,
									objuser.getLstprojectforfilter(), fromdate, todate, testcode);
				}

			} else {
//				List<LSsamplemaster> lstsample1 = lssamplemasterrepository
//						.findByLssitemasterAndStatus(objuser.getLssitemaster(), 1);
				List<LSSheetOrderStructure> lstdir = lsSheetOrderStructureRepository
						.findBySitemasterAndViewoptionOrCreatedbyAndViewoptionOrderByDirectorycode(
								objuser.getLssitemaster(), 1, objuser, 2);
				List<Long> directorycode = lstdir.stream().map(LSSheetOrderStructure::getDirectorycode)
						.collect(Collectors.toList());
				if (testcode == -1 && objuser.getLstprojectforfilter() == null) {
					lstUserorder = lslogilablimsorderdetailRepository
							.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterOrderByBatchcodeDesc(
									lstsample1, 1, fromdate, todate, lstsample1, 2, fromdate, todate, objuser, 3,
									fromdate, todate, objuser, directorycode, 1, fromdate, todate, directorycode, 2,
									fromdate, todate, objuser, directorycode, 3, fromdate, todate, objuser);
//			
//					lstlimscompleted = lslogilablimsorderdetailRepository
//							.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrderByBatchcodeDesc(
//									lstsample1, 1, fromdate, todate, "R", 3, lstsample1, 2, fromdate, todate, objuser,
//									"R", 3, 3, fromdate, todate, objuser, "R", 3, directorycode, 1, fromdate, todate,
//									"R", 3, directorycode, 2, fromdate, todate, objuser, "R", 3, directorycode, 3,
//									fromdate, todate, objuser, "R", 3);

					lstlimscompleted = lslogilablimsorderdetailRepository
							.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullOrderByBatchcodeDesc(
									lstsample1, 1, fromdate, todate, "R", 3, lstsample1, 1, fromdate, todate, "R",
									lstsample1, 2, fromdate, todate, objuser, "R", 3, lstsample1, 2, fromdate, todate,
									objuser, "R", 3, fromdate, todate, objuser, "R", 3, 3, fromdate, todate, objuser,
									"R", directorycode, 1, fromdate, todate, "R", 3, directorycode, 1, fromdate, todate,
									"R", directorycode, 2, fromdate, todate, objuser, "R", 3, directorycode, 2,
									fromdate, todate, objuser, "R", directorycode, 3, fromdate, todate, objuser, "R", 3,
									directorycode, 3, fromdate, todate, objuser, "R");

					lstordersinprogress = lslogilablimsorderdetailRepository
							.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullAndApprovelstatusAndApprovedOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndApprovelstatusAndApprovedOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndApprovelstatusAndApprovedOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullAndApprovelstatusAndApprovedOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndApprovelstatusAndApprovedOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndApprovelstatusAndApprovedOrderByBatchcodeDesc(
									lstsample1, 1, fromdate, todate, "N", 1, 1, lstsample1, 2, fromdate, todate,
									objuser, "N", 1, 1, 3, fromdate, todate, objuser, "N", 1, 1, directorycode, 1,
									fromdate, todate, "N", 1, 1, directorycode, 2, fromdate, todate, objuser, "N", 1, 1,
									directorycode, 3, fromdate, todate, objuser, "N", 1, 1);

					lstpending = lslogilablimsorderdetailRepository
							.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullOrderByBatchcodeDesc(
									lstsample1, 1, fromdate, todate, "N", lstsample1, 2, fromdate, todate, objuser, "N",
									3, fromdate, todate, objuser, "N", directorycode, 1, fromdate, todate, "N",
									directorycode, 2, fromdate, todate, objuser, "N", directorycode, 3, fromdate,
									todate, objuser, "N");
					lstRejected = lslogilablimsorderdetailRepository
							.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndApprovelstatusOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndApprovelstatusOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusOrderByBatchcodeDesc(
									lstsample1, 1, fromdate, todate, 3, lstsample1, 2, fromdate, todate, objuser, 3, 3,
									fromdate, todate, objuser, 3, directorycode, 1, fromdate, todate, 3, directorycode,
									2, fromdate, todate, objuser, 3, directorycode, 3, fromdate, todate, objuser, 3);

					lstCancelled = lslogilablimsorderdetailRepository
							.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrdercancellOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrdercancellOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellOrderByBatchcodeDesc(
									lstsample1, 1, fromdate, todate, 1, lstsample1, 2, fromdate, todate, objuser, 1, 3,
									fromdate, todate, objuser, 1, directorycode, 1, fromdate, todate, 1, directorycode,
									2, fromdate, todate, objuser, 1, directorycode, 3, fromdate, todate, objuser, 1);
				} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {

					lstUserorder = lslogilablimsorderdetailRepository
							.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTestcodeOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTestcodeOrderByBatchcodeDesc(
									lstsample1, 1, fromdate, todate, testcode, lstsample1, 2, fromdate, todate, objuser,
									testcode, 3, fromdate, todate, objuser, testcode, directorycode, 1, fromdate,
									todate, testcode, directorycode, 2, fromdate, todate, objuser, testcode,
									directorycode, 3, fromdate, todate, objuser, testcode);
//					lstlimscompleted = lslogilablimsorderdetailRepository
//							.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusNotAndTestcodeAndOrdercancellIsNullOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndTestcodeAndOrdercancellIsNullOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndTestcodeAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusNotAndTestcodeAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndTestcodeAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndTestcodeAndOrdercancellIsNullOrderByBatchcodeDesc(
//									lstsample1, 1, fromdate, todate, "R", 3, testcode, lstsample1, 2, fromdate, todate,
//									objuser, "R", 3, testcode, 3, fromdate, todate, objuser, "R", 3, testcode,
//									directorycode, 1, fromdate, todate, "R", 3, testcode, directorycode, 2, fromdate,
//									todate, objuser, "R", 3, testcode, directorycode, 3, fromdate, todate, objuser, "R",
//									3, testcode);

					lstlimscompleted = lslogilablimsorderdetailRepository
							.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullAndTestcodeOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullAndTestcodeOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullAndTestcodeOrderByBatchcodeDesc(
									lstsample1, 1, fromdate, todate, "R", 3, testcode, lstsample1, 1, fromdate, todate,
									"R", testcode, lstsample1, 2, fromdate, todate, objuser, "R", 3, testcode,
									lstsample1, 2, fromdate, todate, objuser, "R", testcode, 3, fromdate, todate,
									objuser, "R", 3, testcode, 3, fromdate, todate, objuser, "R", testcode,
									directorycode, 1, fromdate, todate, "R", 3, testcode, directorycode, 1, fromdate,
									todate, "R", testcode, directorycode, 2, fromdate, todate, objuser, "R", 3,
									testcode, directorycode, 2, fromdate, todate, objuser, "R", testcode, directorycode,
									3, fromdate, todate, objuser, "R", 3, testcode, directorycode, 3, fromdate, todate,
									objuser, "R", testcode);

//					lstordersinprogress = lslogilablimsorderdetailRepository
//							.countByOrderflagAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusAndApprovedAndTestcodeOrOrderflagAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusAndApprovedAndTestcodeOrOrderflagAndOrdercancellIsNullAndLsprojectmasterIsNullAndLsuserMasterIsNotAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndApprovelstatusAndApprovedAndTestcodeOrOrderflagAndOrdercancellIsNullAndLsprojectmasterIsNullAndLsuserMasterIsNotAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndApprovelstatusAndApprovedAndTestcodeOrderByBatchcodeDesc(
//									"N", 1, objuser, fromdate, todate, 1, 1, testcode, "N", 2, objuser, fromdate,
//									todate, 1, 1, testcode, "N", objuser, lstsample1, 1, fromdate, todate, 1, 1,
//									testcode, "N", objuser, lstsample1, 2, fromdate, todate, 1, 1, testcode);

					lstordersinprogress = lslogilablimsorderdetailRepository
							.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullAndApprovelstatusAndApprovedAndTestcodeOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndApprovelstatusAndApprovedAndTestcodeOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndApprovelstatusAndApprovedAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullAndApprovelstatusAndApprovedAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndApprovelstatusAndApprovedAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndApprovelstatusAndApprovedAndTestcodeOrderByBatchcodeDesc(
									lstsample1, 1, fromdate, todate, "N", 1, 1, testcode, lstsample1, 2, fromdate,
									todate, objuser, "N", 1, 1, testcode, 3, fromdate, todate, objuser, "N", 1, 1,
									testcode, directorycode, 1, fromdate, todate, "N", 1, 1, testcode, directorycode, 2,
									fromdate, todate, objuser, "N", 1, 1, testcode, directorycode, 3, fromdate, todate,
									objuser, "N", 1, 1, testcode);

					lstpending = lslogilablimsorderdetailRepository
							.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndTestcodeOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndTestcodeOrderByBatchcodeDesc(
									lstsample1, 1, fromdate, todate, "N", testcode, lstsample1, 2, fromdate, todate,
									objuser, "N", testcode, 3, fromdate, todate, objuser, "N", testcode, directorycode,
									1, fromdate, todate, "N", testcode, directorycode, 2, fromdate, todate, objuser,
									"N", testcode, directorycode, 3, fromdate, todate, objuser, "N", testcode);
					lstRejected = lslogilablimsorderdetailRepository
							.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndApprovelstatusAndTestcodeOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusAndTestcodeOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndApprovelstatusAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusAndTestcodeOrderByBatchcodeDesc(
									lstsample1, 1, fromdate, todate, 3, testcode, lstsample1, 2, fromdate, todate,
									objuser, 3, testcode, 3, fromdate, todate, objuser, 3, testcode, directorycode, 1,
									fromdate, todate, 3, testcode, directorycode, 2, fromdate, todate, objuser, 3,
									testcode, directorycode, 3, fromdate, todate, objuser, 3, testcode);

					lstCancelled = lslogilablimsorderdetailRepository
							.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrdercancellAndTestcodeOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellAndTestcodeOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrdercancellAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellAndTestcodeOrderByBatchcodeDesc(
									lstsample1, 1, fromdate, todate, 1, testcode, lstsample1, 2, fromdate, todate,
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

	public Map<String, Object> Getdashboardorders(LSuserMaster objuser) throws ParseException {	Date fromdate = objuser.getObjuser().getFromdate();
	Date todate = objuser.getObjuser().getTodate();
	Map<String, Object> mapOrders = new HashMap<String, Object>();
	Integer testcode = objuser.getTestcode();
	List<LSsamplefile> lssamplefile = lssamplefileRepository.findByprocessed(1);
	Pageable pageable = new PageRequest(objuser.getPagesize(), objuser.getPageperorder());
	long count = 0;

//	if (objuser.getUsername().equals("Administrator")) {
//		List<LSlogilablimsorderdetail> obj = new ArrayList<LSlogilablimsorderdetail>();
//		if (objuser.getObjuser().getOrderselectiontype() == 1) {
//			if (testcode == -1 && objuser.getLstprojectforfilter() == null) {
//				obj = lslogilablimsorderdetailRepository.findByCreatedtimestampBetweenOrderByBatchcodeDesc(fromdate,
//						todate, pageable);
//				mapOrders.put("orderlst", obj);
//				count = lslogilablimsorderdetailRepository
//						.countByCreatedtimestampBetweenOrderByBatchcodeDesc(fromdate, todate);
//			} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {
//				obj = lslogilablimsorderdetailRepository
//						.findByCreatedtimestampBetweenAndTestcodeOrderByBatchcodeDesc(fromdate, todate, testcode,
//								pageable);
//				mapOrders.put("orderlst", obj);
//				count = lslogilablimsorderdetailRepository
//						.countByCreatedtimestampBetweenAndTestcodeOrderByBatchcodeDesc(fromdate, todate, testcode);
//			} else if (testcode == -1 && objuser.getLstprojectforfilter() != null) {
//				obj = lslogilablimsorderdetailRepository
//						.findByCreatedtimestampBetweenAndLsprojectmasterOrderByBatchcodeDesc(fromdate, todate,
//								objuser.getLstprojectforfilter(), pageable);
//				mapOrders.put("orderlst", obj);
//				count = lslogilablimsorderdetailRepository
//						.countByCreatedtimestampBetweenAndLsprojectmasterOrderByBatchcodeDesc(fromdate, todate,
//								objuser.getLstprojectforfilter());
//			} else {
//				obj = lslogilablimsorderdetailRepository
//						.findByCreatedtimestampBetweenAndLsprojectmasterAndTestcodeOrderByBatchcodeDesc(fromdate,
//								todate, objuser.getLstprojectforfilter(), testcode, pageable);
//				mapOrders.put("orderlst", obj);
//				count = lslogilablimsorderdetailRepository
//						.countByCreatedtimestampBetweenAndLsprojectmasterAndTestcodeOrderByBatchcodeDesc(fromdate,
//								todate, objuser.getLstprojectforfilter(), testcode);
//			}
//
//		} else if (objuser.getObjuser().getOrderselectiontype() == 2) {
//			List<Logilabordermaster> lstCompleted = new ArrayList<Logilabordermaster>();
//
//			if (testcode == -1 && objuser.getLstprojectforfilter() == null) {
//				lstCompleted = lslogilablimsorderdetailRepository
//						.findByOrderflagAndApprovelstatusNotAndCreatedtimestampBetweenAndOrdercancellIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenOrderByBatchcodeDesc(
//								"R", 3, fromdate, todate, "R", 0, fromdate, todate, pageable);
//
//				count = lslogilablimsorderdetailRepository
//						.countByOrderflagAndApprovelstatusNotAndCreatedtimestampBetweenAndOrdercancellIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenOrderByBatchcodeDesc(
//								"R", 3, fromdate, todate, "R", 0, fromdate, todate);
//
//			} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {
//
//				lstCompleted = lslogilablimsorderdetailRepository
//						.findByOrderflagAndApprovelstatusNotAndCreatedtimestampBetweenAndOrdercancellIsNullAndTestcodeOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndTestcodeOrderByBatchcodeDesc(
//								"R", 3, fromdate, todate, testcode, "R", 0, fromdate, todate, testcode, pageable);
//
//				count = lslogilablimsorderdetailRepository
//						.countByOrderflagAndApprovelstatusNotAndCreatedtimestampBetweenAndOrdercancellIsNullAndTestcodeOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndTestcodeOrderByBatchcodeDesc(
//								"R", 3, fromdate, todate, testcode, "R", 0, fromdate, todate, testcode);
//			} else if (testcode == -1 && objuser.getLstprojectforfilter() != null) {
//				lstCompleted = lslogilablimsorderdetailRepository
//						.findByOrderflagAndApprovelstatusNotAndCreatedtimestampBetweenAndOrdercancellIsNullAndLsprojectmasterOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterOrderByBatchcodeDesc(
//								"R", 3, fromdate, todate, objuser.getLstprojectforfilter(), "R", 0, fromdate,
//								todate, objuser.getLstprojectforfilter(), pageable);
//
//				count = lslogilablimsorderdetailRepository
//						.countByOrderflagAndApprovelstatusNotAndCreatedtimestampBetweenAndOrdercancellIsNullAndLsprojectmasterOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterOrderByBatchcodeDesc(
//								"R", 3, fromdate, todate, objuser.getLstprojectforfilter(), "R", 0, fromdate,
//								todate, objuser.getLstprojectforfilter());
//			} else {
//				lstCompleted = lslogilablimsorderdetailRepository
//						.findByOrderflagAndApprovelstatusNotAndCreatedtimestampBetweenAndOrdercancellIsNullAndLsprojectmasterAndTestcodeOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterAndTestcodeOrderByBatchcodeDesc(
//								"R", 3, fromdate, todate, objuser.getLstprojectforfilter(), testcode, "R", 0,
//								fromdate, todate, objuser.getLstprojectforfilter(), testcode, pageable);
//
//				count = lslogilablimsorderdetailRepository
//						.countByOrderflagAndApprovelstatusNotAndCreatedtimestampBetweenAndOrdercancellIsNullAndLsprojectmasterAndTestcodeOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterAndTestcodeOrderByBatchcodeDesc(
//								"R", 3, fromdate, todate, objuser.getLstprojectforfilter(), testcode, "R", 0,
//								fromdate, todate, objuser.getLstprojectforfilter(), testcode);
//			}
//			mapOrders.put("orderlst", lstCompleted);
//		} else if (objuser.getObjuser().getOrderselectiontype() == 3) {
//			if (testcode == -1 && objuser.getLstprojectforfilter() == null) {
//				mapOrders.put("orderlst", lslogilablimsorderdetailRepository
//						.findByOrderflagAndCreatedtimestampBetweenAndOrdercancellIsNullOrOrderflagAndCreatedtimestampBetweenAndFiletypeOrderByBatchcodeDesc(
//								"N", fromdate, todate, "N", fromdate, todate, 0, pageable));
//				count = lslogilablimsorderdetailRepository
//						.countByOrderflagAndCreatedtimestampBetweenAndOrdercancellIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenOrderByBatchcodeDesc(
//								"N", fromdate, todate, "N", 0, fromdate, todate);
//			} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {
//				mapOrders.put("orderlst", lslogilablimsorderdetailRepository
//						.findByOrderflagAndCreatedtimestampBetweenAndOrdercancellIsNullAndTestcodeOrOrderflagAndCreatedtimestampBetweenAndFiletypeAndTestcodeOrderByBatchcodeDesc(
//								"N", fromdate, todate, testcode, "N", fromdate, todate, 0, testcode, pageable));
//				count = lslogilablimsorderdetailRepository
//						.countByOrderflagAndCreatedtimestampBetweenAndOrdercancellIsNullAndTestcodeOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndTestcodeOrderByBatchcodeDesc(
//								"N", fromdate, todate, testcode, "N", 0, fromdate, todate, testcode);
//			} else if (testcode == -1 && objuser.getLstprojectforfilter() != null) {
//				mapOrders.put("orderlst", lslogilablimsorderdetailRepository
//						.findByOrderflagAndCreatedtimestampBetweenAndOrdercancellIsNullAndLsprojectmasterOrOrderflagAndCreatedtimestampBetweenAndFiletypeAndLsprojectmasterOrderByBatchcodeDesc(
//								"N", fromdate, todate, objuser.getLstprojectforfilter(), "N", fromdate, todate, 0,
//								objuser.getLstprojectforfilter(), pageable));
//				count = lslogilablimsorderdetailRepository
//						.countByOrderflagAndCreatedtimestampBetweenAndOrdercancellIsNullAndLsprojectmasterOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterOrderByBatchcodeDesc(
//								"N", fromdate, todate, objuser.getLstprojectforfilter(), "N", 0, fromdate, todate,
//								objuser.getLstprojectforfilter());
//
//			} else {
//				mapOrders.put("orderlst", lslogilablimsorderdetailRepository
//						.findByOrderflagAndCreatedtimestampBetweenAndOrdercancellIsNullAndLsprojectmasterAndTestcodeOrOrderflagAndCreatedtimestampBetweenAndFiletypeAndLsprojectmasterAndTestcodeOrderByBatchcodeDesc(
//								"N", fromdate, todate, objuser.getLstprojectforfilter(), testcode, "N", fromdate,
//								todate, 0, objuser.getLstprojectforfilter(), testcode, pageable));
//				count = lslogilablimsorderdetailRepository
//						.countByOrderflagAndCreatedtimestampBetweenAndOrdercancellIsNullAndLsprojectmasterAndTestcodeOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterAndTestcodeOrderByBatchcodeDesc(
//								"N", fromdate, todate, objuser.getLstprojectforfilter(), testcode, "N", 0, fromdate,
//								todate, objuser.getLstprojectforfilter(), testcode);
//			}
//		} else if (objuser.getObjuser().getOrderselectiontype() == 4) {
//			mapOrders.put("orderlst",
//					lslogilablimsorderdetailRepository
//							.findByOrderflagAndLssamplefileInAndCreatedtimestampBetweenOrderByBatchcodeDesc("N",
//									lssamplefile, fromdate, todate, pageable));
//			count = lslogilablimsorderdetailRepository
//					.countByOrderflagAndLssamplefileInAndCreatedtimestampBetweenOrderByBatchcodeDesc("N",
//							lssamplefile, fromdate, todate);
//		} else if (objuser.getObjuser().getOrderselectiontype() == 5) {
//
//			if (testcode == -1 && objuser.getLstprojectforfilter() == null) {
//				mapOrders.put("orderlst",
//						lslogilablimsorderdetailRepository
//								.findByCreatedtimestampBetweenAndApprovelstatusOrderByBatchcodeDesc(fromdate,
//										todate, 3, pageable));
//				count = lslogilablimsorderdetailRepository
//						.countByCreatedtimestampBetweenAndApprovelstatusOrderByBatchcodeDesc(fromdate, todate, 3);
//			} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {
//				mapOrders.put("orderlst",
//						lslogilablimsorderdetailRepository
//								.findByCreatedtimestampBetweenAndApprovelstatusAndTestcodeOrderByBatchcodeDesc(
//										fromdate, todate, 3, testcode, pageable));
//				count = lslogilablimsorderdetailRepository
//						.countByCreatedtimestampBetweenAndApprovelstatusAndTestcodeOrderByBatchcodeDesc(fromdate,
//								todate, 3, testcode);
//			} else if (testcode == -1 && objuser.getLstprojectforfilter() != null) {
//				mapOrders.put("orderlst", lslogilablimsorderdetailRepository
//						.findByCreatedtimestampBetweenAndApprovelstatusAndLsprojectmasterOrderByBatchcodeDesc(
//								fromdate, todate, 3, objuser.getLstprojectforfilter(), pageable));
//				count = lslogilablimsorderdetailRepository
//						.countByCreatedtimestampBetweenAndApprovelstatusAndLsprojectmasterOrderByBatchcodeDesc(
//								fromdate, todate, 3, objuser.getLstprojectforfilter());
//			} else {
//				mapOrders.put("orderlst", lslogilablimsorderdetailRepository
//						.findByCreatedtimestampBetweenAndApprovelstatusAndLsprojectmasterAndTestcodeOrderByBatchcodeDesc(
//								fromdate, todate, 3, objuser.getLstprojectforfilter(), testcode, pageable));
//				count = lslogilablimsorderdetailRepository
//						.countByCreatedtimestampBetweenAndApprovelstatusAndLsprojectmasterAndTestcodeOrderByBatchcodeDesc(
//								fromdate, todate, 3, objuser.getLstprojectforfilter(), testcode);
//
//			}
//		} else if (objuser.getObjuser().getOrderselectiontype() == 6) {
//			if (testcode == -1 && objuser.getLstprojectforfilter() == null) {
//				mapOrders.put("orderlst",
//						lslogilablimsorderdetailRepository
//								.findByCreatedtimestampBetweenAndOrdercancellOrderByBatchcodeDesc(fromdate, todate,
//										1, pageable));
//
//				count = lslogilablimsorderdetailRepository
//						.countByCreatedtimestampBetweenAndOrdercancellOrderByBatchcodeDesc(fromdate, todate, 1);
//			} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {
//				mapOrders.put("orderlst",
//						lslogilablimsorderdetailRepository
//								.findByCreatedtimestampBetweenAndOrdercancellAndTestcodeOrderByBatchcodeDesc(
//										fromdate, todate, 1, testcode, pageable));
//
//				count = lslogilablimsorderdetailRepository
//						.countByCreatedtimestampBetweenAndOrdercancellAndTestcodeOrderByBatchcodeDesc(fromdate,
//								todate, 1, testcode);
//			} else if (testcode == -1 && objuser.getLstprojectforfilter() != null) {
//				mapOrders.put("orderlst",
//						lslogilablimsorderdetailRepository
//								.findByCreatedtimestampBetweenAndOrdercancellAndLsprojectmasterOrderByBatchcodeDesc(
//										fromdate, todate, 1, objuser.getLstprojectforfilter(), pageable));
//				count = lslogilablimsorderdetailRepository
//						.countByCreatedtimestampBetweenAndOrdercancellAndLsprojectmasterOrderByBatchcodeDesc(
//								fromdate, todate, 1, objuser.getLstprojectforfilter());
//
//			} else {
//				mapOrders.put("orderlst", lslogilablimsorderdetailRepository
//						.findByCreatedtimestampBetweenAndOrdercancellAndLsprojectmasterAndTestcodeOrderByBatchcodeDesc(
//								fromdate, todate, 1, objuser.getLstprojectforfilter(), testcode, pageable));
//				count = lslogilablimsorderdetailRepository
//						.countByCreatedtimestampBetweenAndOrdercancellAndLsprojectmasterAndTestcodeOrderByBatchcodeDesc(
//								fromdate, todate, 1, objuser.getLstprojectforfilter(), testcode);
//
//			}
//
//		}
//	} else {

	List<LSprojectmaster> lstproject = objuser.getLstproject();
	List<Logilabordermaster> lstorders = new ArrayList<Logilabordermaster>();
	List<LSworkflow> lstworkflow = objuser.getLstworkflow();
//		List<LSsamplemaster> lstsample1 = lssamplemasterrepository
//				.findByLssitemasterAndStatus(objuser.getLssitemaster(), 1);
	List<Integer> lstsampleint = lssamplemasterrepository
			.getDistinctByLssitemasterSitecodeAndStatus(objuser.getLssitemaster().getSitecode(), 1);
	List<LSsamplemaster> lstsample1 = new ArrayList<>();
	LSsamplemaster sample = null;
	if (lstsampleint.size() > 0) {
		for (Integer item : lstsampleint) {
			sample = new LSsamplemaster();
			sample.setSamplecode(item);
			lstsample1.add(sample);
			sample = null; // Set sample to null after adding it to the list
		}
	}
	if (lstproject != null) {
		objuser.getUsernotify().add(objuser);
		if (objuser.getObjuser().getOrderselectiontype() == 1) {

			if (testcode == -1 && objuser.getLstprojectforfilter() == null) {
				lstorders = lslogilablimsorderdetailRepository
						.findByLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullOrFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullOrLsprojectmasterIsNullAndLssamplemasterInAndCreatedtimestampBetweenAndAssignedtoIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndAssignedtoIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndAssignedtoIsNullOrViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndLsprojectmasterInAndAssignedtoIsNullOrViewoptionAndLsuserMasterInAndCreatedtimestampBetweenAndLsprojectmasterIsNullAndAssignedtoIsNullOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullOrAssignedtoAndCreatedtimestampBetweenOrderByBatchcodeDesc(
								lstproject, fromdate, todate, 0, fromdate, todate, lstsample1, fromdate, todate, 1,
								objuser, fromdate, todate, 2, objuser, fromdate, todate, 3, objuser, fromdate,
								todate, lstproject, 3, objuser.getUsernotify(), fromdate, todate, objuser, objuser,
								fromdate, todate, objuser, fromdate, todate, pageable);
				count = lslogilablimsorderdetailRepository
						.countByLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullOrFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullOrLsprojectmasterIsNullAndLssamplemasterInAndCreatedtimestampBetweenAndAssignedtoIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndAssignedtoIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndAssignedtoIsNullOrViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndLsprojectmasterInAndAssignedtoIsNullOrViewoptionAndLsuserMasterInAndCreatedtimestampBetweenAndLsprojectmasterIsNullAndAssignedtoIsNullOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullOrAssignedtoAndCreatedtimestampBetweenOrderByBatchcodeDesc(
								lstproject, fromdate, todate, 0, fromdate, todate, lstsample1, fromdate, todate, 1,
								objuser, fromdate, todate, 2, objuser, fromdate, todate, 3, objuser, fromdate,
								todate, lstproject, 3, objuser.getUsernotify(), fromdate, todate, objuser, objuser,
								fromdate, todate, objuser, fromdate, todate);

//					lstorders = lslogilablimsorderdetailRepository
//							.findByLsprojectmasterInAndCreatedtimestampBetweenOrFiletypeAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndLssamplemasterInAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenOrViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndLsprojectmasterInOrViewoptionAndLsuserMasterInAndCreatedtimestampBetweenAndLsprojectmasterIsNullOrderByBatchcodeDesc(
//									lstproject, fromdate, todate, 
//									0, fromdate, todate, 
//									lstsample1,fromdate, todate,
//									1, objuser, fromdate, todate,
//									2, objuser, fromdate, todate, 
//									3,objuser,fromdate, todate,lstproject,
//                                    3, objuser.getUsernotify(), fromdate, todate,pageable);
//					count = lslogilablimsorderdetailRepository
//							.countByLsprojectmasterInAndCreatedtimestampBetweenOrFiletypeAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndLssamplemasterInAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenOrViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndLsprojectmasterInOrViewoptionAndLsuserMasterInAndCreatedtimestampBetweenAndLsprojectmasterIsNullOrderByBatchcodeDesc(
//									lstproject, fromdate, todate, 
//									0, fromdate, todate, 
//									lstsample1,fromdate, todate,
//									1, objuser, fromdate, todate,
//									2, objuser, fromdate, todate, 
//									3,objuser,fromdate, todate,lstproject,
//                                    3, objuser.getUsernotify(), fromdate, todate);

			} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {

//					lstorders = lslogilablimsorderdetailRepository
//							.findByLsprojectmasterInAndCreatedtimestampBetweenAndTestcodeOrFiletypeAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndTestcodeOrderByBatchcodeDesc(
//									lstproject, fromdate, todate, testcode, 0, fromdate, todate, testcode, 1,
//									objuser, fromdate, todate, testcode, 2, objuser, fromdate, todate, testcode, 3,
//									objuser, fromdate, todate, testcode, pageable);
//					count = lslogilablimsorderdetailRepository
//							.countByLsprojectmasterInAndCreatedtimestampBetweenAndTestcodeOrFiletypeAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndTestcodeOrderByBatchcodeDesc(
//									lstproject, fromdate, todate, testcode, 0, fromdate, todate, testcode, 1,
//									objuser, fromdate, todate, testcode, 2, objuser, fromdate, todate, testcode, 3,
//									objuser, fromdate, todate, testcode);

				lstorders = lslogilablimsorderdetailRepository
						.findByLsprojectmasterInAndCreatedtimestampBetweenAndTestcodeAndAssignedtoIsNullOrFiletypeAndCreatedtimestampBetweenAndTestcodeAndAssignedtoIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndTestcodeAndAssignedtoIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndTestcodeAndAssignedtoIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndTestcodeAndAssignedtoIsNullOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndTestcodeOrAssignedtoAndCreatedtimestampBetweenAndTestcodeOrderByBatchcodeDesc(
								lstproject, fromdate, todate, testcode, 0, fromdate, todate, testcode, 1, objuser,
								fromdate, todate, testcode, 2, objuser, fromdate, todate, testcode, 3, objuser,
								fromdate, todate, testcode, objuser, objuser, fromdate, todate, testcode, objuser,
								fromdate, todate, testcode, pageable);
				count = lslogilablimsorderdetailRepository
						.countByLsprojectmasterInAndCreatedtimestampBetweenAndTestcodeAndAssignedtoIsNullOrFiletypeAndCreatedtimestampBetweenAndTestcodeAndAssignedtoIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndTestcodeAndAssignedtoIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndTestcodeAndAssignedtoIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndTestcodeAndAssignedtoIsNullOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndTestcodeOrAssignedtoAndCreatedtimestampBetweenAndTestcodeOrderByBatchcodeDesc(
								lstproject, fromdate, todate, testcode, 0, fromdate, todate, testcode, 1, objuser,
								fromdate, todate, testcode, 2, objuser, fromdate, todate, testcode, 3, objuser,
								fromdate, todate, testcode, objuser, objuser, fromdate, todate, testcode, objuser,
								fromdate, todate, testcode);
			} else if (testcode == -1 && objuser.getLstprojectforfilter() != null) {
				lstorders = lslogilablimsorderdetailRepository
						.findByLsprojectmasterAndCreatedtimestampBetweenAndAssignedtoIsNullOrFiletypeAndLsprojectmasterAndCreatedtimestampBetweenAndAssignedtoIsNullOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndLsprojectmasterOrAssignedtoAndCreatedtimestampBetweenAndLsprojectmasterOrderByBatchcodeDesc(
								objuser.getLstprojectforfilter(), fromdate, todate, 0,
								objuser.getLstprojectforfilter(), fromdate, todate, objuser, objuser, fromdate,
								todate, objuser.getLstprojectforfilter(), objuser, fromdate, todate,
								objuser.getLstprojectforfilter(), pageable);
				count = lslogilablimsorderdetailRepository
						.countByLsprojectmasterAndCreatedtimestampBetweenAndAssignedtoIsNullOrFiletypeAndLsprojectmasterAndCreatedtimestampBetweenAndAssignedtoIsNullOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndLsprojectmasterOrAssignedtoAndCreatedtimestampBetweenAndLsprojectmasterOrderByBatchcodeDesc(
								objuser.getLstprojectforfilter(), fromdate, todate, 0,
								objuser.getLstprojectforfilter(), fromdate, todate, objuser, objuser, fromdate,
								todate, objuser.getLstprojectforfilter(), objuser, fromdate, todate,
								objuser.getLstprojectforfilter());

//					lstorders = lslogilablimsorderdetailRepository
//							.findByLsprojectmasterAndCreatedtimestampBetweenOrFiletypeAndLsprojectmasterAndCreatedtimestampBetweenOrderByBatchcodeDesc(
//									objuser.getLstprojectforfilter(), fromdate, todate, 0,
//									objuser.getLstprojectforfilter(), fromdate, todate, pageable);
//					count = lslogilablimsorderdetailRepository
//							.countByLsprojectmasterAndCreatedtimestampBetweenOrFiletypeAndLsprojectmasterAndCreatedtimestampBetweenOrderByBatchcodeDesc(
//									objuser.getLstprojectforfilter(), fromdate, todate, 0,
//									objuser.getLstprojectforfilter(), fromdate, todate);

			} else {
//					lstorders = lslogilablimsorderdetailRepository
//							.findByLsprojectmasterAndCreatedtimestampBetweenAndTestcodeOrFiletypeAndLsprojectmasterAndCreatedtimestampBetweenAndTestcodeOrderByBatchcodeDesc(
//									objuser.getLstprojectforfilter(), fromdate, todate, testcode, 0,
//									objuser.getLstprojectforfilter(), fromdate, todate, testcode, pageable);
//					count = lslogilablimsorderdetailRepository
//							.countByLsprojectmasterAndCreatedtimestampBetweenAndTestcodeOrFiletypeAndLsprojectmasterAndCreatedtimestampBetweenAndTestcodeOrderByBatchcodeDesc(
//									objuser.getLstprojectforfilter(), fromdate, todate, testcode, 0,
//									objuser.getLstprojectforfilter(), fromdate, todate, testcode);
				lstorders = lslogilablimsorderdetailRepository
						.findByLsprojectmasterAndCreatedtimestampBetweenAndTestcodeAndAssignedtoIsNullOrFiletypeAndLsprojectmasterAndCreatedtimestampBetweenAndTestcodeAndAssignedtoIsNullOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndTestcodeAndLsprojectmasterOrAssignedtoAndCreatedtimestampBetweenAndTestcodeAndLsprojectmasterOrderByBatchcodeDesc(
								objuser.getLstprojectforfilter(), fromdate, todate, testcode, 0,
								objuser.getLstprojectforfilter(), fromdate, todate, testcode, objuser, objuser,
								fromdate, todate, testcode, objuser.getLstprojectforfilter(), objuser, fromdate,
								testcode, objuser.getLstprojectforfilter(), todate, pageable);
				count = lslogilablimsorderdetailRepository
						.countByLsprojectmasterAndCreatedtimestampBetweenAndTestcodeAndAssignedtoIsNullOrFiletypeAndLsprojectmasterAndCreatedtimestampBetweenAndTestcodeAndAssignedtoIsNullOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndTestcodeAndLsprojectmasterOrAssignedtoAndCreatedtimestampBetweenAndTestcodeAndLsprojectmasterOrderByBatchcodeDesc(
								objuser.getLstprojectforfilter(), fromdate, todate, testcode, 0,
								objuser.getLstprojectforfilter(), fromdate, todate, testcode, objuser, objuser,
								fromdate, todate, testcode, objuser.getLstprojectforfilter(), objuser, fromdate,
								testcode, objuser.getLstprojectforfilter(), todate);
			}

		} else if (objuser.getObjuser().getOrderselectiontype() == 2) {
			if (testcode == -1 && objuser.getLstprojectforfilter() == null) {

//					lstorders = lslogilablimsorderdetailRepository
//							.findByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullOrOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullOrOrderflagAndLsprojectmasterIsNullAndLssamplemasterInAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullOrOrderflagAndLsprojectmasterIsNullAndLssamplemasterInAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndLsprojectmasterInAndOrdercancellIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndLsprojectmasterIsNullAndOrdercancellIsNullOrderByBatchcodeDesc(
//									"R", lstproject, fromdate, todate, 3, 
//									"R", lstproject, fromdate, todate, 
//									"R", lstsample1, fromdate, todate, 3, 
//									"R", lstsample1, fromdate, todate, 
//									"R", 0,fromdate, todate, 
//									"R", 1, objuser, fromdate, todate, 3, 
//									"R", 2, objuser,fromdate, todate, 3, 
//									"R", 3, objuser, fromdate, todate, 3, lstproject, 
//									"R", 1,objuser, fromdate, todate, 
//									"R", 2, objuser, fromdate, todate, 
//									"R", 3, objuser,fromdate, todate, pageable);
//					count = lslogilablimsorderdetailRepository
//							.countByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullOrOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullOrOrderflagAndLsprojectmasterIsNullAndLssamplemasterInAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullOrOrderflagAndLsprojectmasterIsNullAndLssamplemasterInAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndLsprojectmasterInAndOrdercancellIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndLsprojectmasterIsNullAndOrdercancellIsNullOrderByBatchcodeDesc(
//									"R", lstproject, fromdate, todate, 3, 
//									"R", lstproject, fromdate, todate, 
//									"R", lstsample1, fromdate, todate, 3, 
//									"R", lstsample1, fromdate, todate, 
//									"R", 0,fromdate, todate, 
//									"R", 1, objuser, fromdate, todate, 3, 
//									"R", 2, objuser,fromdate, todate, 3, 
//									"R", 3, objuser, fromdate, todate, 3, lstproject, 
//									"R", 1,objuser, fromdate, todate, 
//									"R", 2, objuser, fromdate, todate, 
//									"R", 3, objuser,fromdate, todate);
				lstorders = lslogilablimsorderdetailRepository
						.findByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndLssamplemasterInAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndLssamplemasterInAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndLsprojectmasterInAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndLsprojectmasterIsNullAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullOrOrderflagAndAssignedtoAndCreatedtimestampBetweenOrderByBatchcodeDesc(
								"R", lstproject, fromdate, todate, 3, "R", lstproject, fromdate, todate, "R",
								lstsample1, fromdate, todate, 3, "R", lstsample1, fromdate, todate, "R", 0,
								fromdate, todate, "R", 1, objuser, fromdate, todate, 3, "R", 2, objuser, fromdate,
								todate, 3, "R", 3, objuser, fromdate, todate, 3, lstproject, "R", 1, objuser,
								fromdate, todate, "R", 2, objuser, fromdate, todate, "R", 3, objuser, fromdate,
								todate, "R", objuser, objuser, fromdate, todate, "R", objuser, fromdate, todate,
								pageable);

				count = lslogilablimsorderdetailRepository
						.countByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndLssamplemasterInAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndLssamplemasterInAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndLsprojectmasterInAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndLsprojectmasterIsNullAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullOrOrderflagAndAssignedtoAndCreatedtimestampBetweenOrderByBatchcodeDesc(
								"R", lstproject, fromdate, todate, 3, "R", lstproject, fromdate, todate, "R",
								lstsample1, fromdate, todate, 3, "R", lstsample1, fromdate, todate, "R", 0,
								fromdate, todate, "R", 1, objuser, fromdate, todate, 3, "R", 2, objuser, fromdate,
								todate, 3, "R", 3, objuser, fromdate, todate, 3, lstproject, "R", 1, objuser,
								fromdate, todate, "R", 2, objuser, fromdate, todate, "R", 3, objuser, fromdate,
								todate, "R", objuser, objuser, fromdate, todate, "R", objuser, fromdate, todate);

			} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {

//					lstorders = lslogilablimsorderdetailRepository
//							.findByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusNotAndTestcodeAndOrdercancellIsNullOrOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusIsNullAndTestcodeAndOrdercancellIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndTestcodeAndOrdercancellIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndTestcodeAndOrdercancellIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndTestcodeAndOrdercancellIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndLsprojectmasterInAndTestcodeAndOrdercancellIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndTestcodeAndOrdercancellIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndTestcodeAndOrdercancellIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndLsprojectmasterInAndTestcodeAndOrdercancellIsNullOrderByBatchcodeDesc(
//									"R", lstproject, fromdate, todate, 3, testcode, "R", lstproject, fromdate,
//									todate, testcode, "R", 0, fromdate, todate, testcode, "R", 1, objuser, fromdate,
//									todate, 3, testcode, "R", 2, objuser, fromdate, todate, 3, testcode, "R", 3,
//									objuser, fromdate, todate, 3, lstproject, testcode, "R", 1, objuser, fromdate,
//									todate, testcode, "R", 2, objuser, fromdate, todate, testcode, "R", 3, objuser,
//									fromdate, todate, lstproject, testcode, pageable);
//					count = lslogilablimsorderdetailRepository
//							.countByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusNotAndTestcodeAndOrdercancellIsNullOrOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusIsNullAndTestcodeAndOrdercancellIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndTestcodeAndOrdercancellIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndTestcodeAndOrdercancellIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndTestcodeAndOrdercancellIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndLsprojectmasterInAndTestcodeAndOrdercancellIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndTestcodeAndOrdercancellIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndTestcodeAndOrdercancellIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndLsprojectmasterInAndTestcodeAndOrdercancellIsNullOrderByBatchcodeDesc(
//									"R", lstproject, fromdate, todate, 3, testcode, "R", lstproject, fromdate,
//									todate, testcode, "R", 0, fromdate, todate, testcode, "R", 1, objuser, fromdate,
//									todate, 3, testcode, "R", 2, objuser, fromdate, todate, 3, testcode, "R", 3,
//									objuser, fromdate, todate, 3, lstproject, testcode, "R", 1, objuser, fromdate,
//									todate, testcode, "R", 2, objuser, fromdate, todate, testcode, "R", 3, objuser,
//									fromdate, todate, lstproject, testcode);
				lstorders = lslogilablimsorderdetailRepository
						.findByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndLssamplemasterInAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndLssamplemasterInAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndLsprojectmasterInAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndLsprojectmasterIsNullAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullOrOrderflagAndAssignedtoAndCreatedtimestampBetweenOrderByBatchcodeDesc(
								"R", lstproject, fromdate, todate, 3, testcode, "R", lstproject, fromdate, todate,
								testcode, "R", lstsample1, fromdate, todate, 3, testcode, "R", lstsample1, fromdate,
								todate, testcode, "R", 0, fromdate, todate, testcode, "R", 1, objuser, fromdate,
								todate, 3, testcode, "R", 2, objuser, fromdate, todate, 3, testcode, "R", 3,
								objuser, fromdate, todate, 3, lstproject, testcode, "R", 1, objuser, fromdate,
								todate, testcode, "R", 2, objuser, fromdate, todate, testcode, "R", 3, objuser,
								fromdate, todate, testcode, "R", objuser, objuser, fromdate, todate, testcode, "R",
								objuser, fromdate, todate, pageable);

				count = lslogilablimsorderdetailRepository
						.countByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndLssamplemasterInAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndLssamplemasterInAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndLsprojectmasterInAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndLsprojectmasterIsNullAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullOrOrderflagAndAssignedtoAndCreatedtimestampBetweenOrderByBatchcodeDesc(
								"R", lstproject, fromdate, todate, 3, testcode, "R", lstproject, fromdate, todate,
								testcode, "R", lstsample1, fromdate, todate, 3, testcode, "R", lstsample1, fromdate,
								todate, testcode, "R", 0, fromdate, todate, testcode, "R", 1, objuser, fromdate,
								todate, 3, testcode, "R", 2, objuser, fromdate, todate, 3, testcode, "R", 3,
								objuser, fromdate, todate, 3, lstproject, testcode, "R", 1, objuser, fromdate,
								todate, testcode, "R", 2, objuser, fromdate, todate, testcode, "R", 3, objuser,
								fromdate, todate, testcode, "R", objuser, objuser, fromdate, todate, testcode, "R",
								objuser, fromdate, todate);

			} else if (testcode == -1 && objuser.getLstprojectforfilter() != null) {
//					lstorders = lslogilablimsorderdetailRepository
//							.findByOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullOrOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterAndOrdercancellIsNullOrderByBatchcodeDesc(
//									"R", objuser.getLstprojectforfilter(), fromdate, todate, 3, "R",
//									objuser.getLstprojectforfilter(), fromdate, todate, "R", 0, fromdate, todate,
//									objuser.getLstprojectforfilter(), pageable);
//					count = lslogilablimsorderdetailRepository
//							.countByOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullOrOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterAndOrdercancellIsNullOrderByBatchcodeDesc(
//									"R", objuser.getLstprojectforfilter(), fromdate, todate, 3, "R",
//									objuser.getLstprojectforfilter(), fromdate, todate, "R", 0, fromdate, todate,
//									objuser.getLstprojectforfilter());
				lstorders = lslogilablimsorderdetailRepository
						.findByOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndLsprojectmasterOrOrderflagAndAssignedtoAndCreatedtimestampBetweenAndLsprojectmasterOrderByBatchcodeDesc(
								"R", objuser.getLstprojectforfilter(), fromdate, todate, 3, "R",
								objuser.getLstprojectforfilter(), fromdate, todate, "R", 0, fromdate, todate,
								objuser.getLstprojectforfilter(), "R", objuser, objuser, fromdate, todate,
								objuser.getLstprojectforfilter(), "R", objuser, fromdate, todate,
								objuser.getLstprojectforfilter(), pageable);
				count = lslogilablimsorderdetailRepository
						.countByOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndLsprojectmasterOrOrderflagAndAssignedtoAndCreatedtimestampBetweenAndLsprojectmasterOrderByBatchcodeDesc(
								"R", objuser.getLstprojectforfilter(), fromdate, todate, 3, "R",
								objuser.getLstprojectforfilter(), fromdate, todate, "R", 0, fromdate, todate,
								objuser.getLstprojectforfilter(), "R", objuser, objuser, fromdate, todate,
								objuser.getLstprojectforfilter(), "R", objuser, fromdate, todate,
								objuser.getLstprojectforfilter());
			} else {
//					lstorders = lslogilablimsorderdetailRepository
//							.findByOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndApprovelstatusNotAndTestcodeAndOrdercancellIsNullOrOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndTestcodeAndOrdercancellIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterAndTestcodeAndOrdercancellIsNullOrderByBatchcodeDesc(
//									"R", objuser.getLstprojectforfilter(), fromdate, todate, 3, testcode, "R",
//									objuser.getLstprojectforfilter(), fromdate, todate, testcode, "R", 0, fromdate,
//									todate, objuser.getLstprojectforfilter(), testcode, pageable);
//					count = lslogilablimsorderdetailRepository
//							.countByOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndApprovelstatusNotAndTestcodeAndOrdercancellIsNullOrOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndTestcodeAndOrdercancellIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterAndTestcodeAndOrdercancellIsNullOrderByBatchcodeDesc(
//									"R", objuser.getLstprojectforfilter(), fromdate, todate, 3, testcode, "R",
//									objuser.getLstprojectforfilter(), fromdate, todate, testcode, "R", 0, fromdate,
//									todate, objuser.getLstprojectforfilter(), testcode);
				lstorders = lslogilablimsorderdetailRepository
						.findByOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndApprovelstatusNotAndTestcodeAndOrdercancellIsNullOrOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndTestcodeAndOrdercancellIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterAndTestcodeAndOrdercancellIsNullOrOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndLsprojectmasterAndTestcodeOrOrderflagAndAssignedtoAndCreatedtimestampBetweenAndLsprojectmasterAndTestcodeOrderByBatchcodeDesc(
								"R", objuser.getLstprojectforfilter(), fromdate, todate, 3, testcode, "R",
								objuser.getLstprojectforfilter(), fromdate, todate, testcode, "R", 0, fromdate,
								todate, objuser.getLstprojectforfilter(), testcode, "R", objuser, objuser, fromdate,
								todate, objuser.getLstprojectforfilter(), testcode, "R", objuser, fromdate, todate,
								objuser.getLstprojectforfilter(), testcode, pageable);

				count = lslogilablimsorderdetailRepository
						.countByOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndApprovelstatusNotAndTestcodeAndOrdercancellIsNullOrOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndTestcodeAndOrdercancellIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterAndTestcodeAndOrdercancellIsNullOrOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndLsprojectmasterAndTestcodeOrOrderflagAndAssignedtoAndCreatedtimestampBetweenAndLsprojectmasterAndTestcodeOrderByBatchcodeDesc(
								"R", objuser.getLstprojectforfilter(), fromdate, todate, 3, testcode, "R",
								objuser.getLstprojectforfilter(), fromdate, todate, testcode, "R", 0, fromdate,
								todate, objuser.getLstprojectforfilter(), testcode, "R", objuser, objuser, fromdate,
								todate, objuser.getLstprojectforfilter(), testcode, "R", objuser, fromdate, todate,
								objuser.getLstprojectforfilter(), testcode);

			}
		} else if (objuser.getObjuser().getOrderselectiontype() == 3) {
			if (testcode == -1 && objuser.getLstprojectforfilter() == null) {

				lstorders = lslogilablimsorderdetailRepository
						.findByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndLssamplemasterInAndCreatedtimestampBetweenAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenAndAssignedtoIsNullOrOrderflagAndLsprojectmasterInAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndOrdercancellIsNullAndCreatedtimestampBetweenAndLsuserMasterInAndAssignedtoIsNullOrOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullOrOrderflagAndAssignedtoAndCreatedtimestampBetweenOrderByBatchcodeDesc(
								"N", lstproject, fromdate, todate, "N", lstsample1, fromdate, todate, "N", 0,
								fromdate, todate, "N", 1, objuser, fromdate, todate, "N", 2, objuser, fromdate,
								todate, "N", lstproject, 3, objuser, fromdate, todate, "N", 3, fromdate, todate,
								objuser.getUsernotify(), "N", objuser, objuser, fromdate, todate, "N", objuser,
								fromdate, todate, pageable);

				count = lslogilablimsorderdetailRepository
						.countByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndLssamplemasterInAndCreatedtimestampBetweenAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenAndAssignedtoIsNullOrOrderflagAndLsprojectmasterInAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndOrdercancellIsNullAndCreatedtimestampBetweenAndLsuserMasterInAndAssignedtoIsNullOrOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullOrOrderflagAndAssignedtoAndCreatedtimestampBetween(
								"N", lstproject, fromdate, todate, "N", lstsample1, fromdate, todate, "N", 0,
								fromdate, todate, "N", 1, objuser, fromdate, todate, "N", 2, objuser, fromdate,
								todate, "N", lstproject, 3, objuser, fromdate, todate, "N", 3, fromdate, todate,
								objuser.getUsernotify(), "N", objuser, objuser, fromdate, todate, "N", objuser,
								fromdate, todate);

//					lstorders = lslogilablimsorderdetailRepository
//						    .findByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndOrdercancellIsNullOrOrderflagAndLsprojectmasterIsNullAndLssamplemasterInAndCreatedtimestampBetweenAndOrdercancellIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenOrOrderflagAndLsprojectmasterInAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndOrdercancellIsNullAndCreatedtimestampBetweenAndLsuserMasterInOrderByBatchcodeDesc(
//						        "N", lstproject, fromdate, todate, 
//						        "N", lstsample1, fromdate, todate, 
//						        "N", 0, fromdate, todate, 
//						        "N", 1, objuser,fromdate, todate, 
//						        "N", 2, objuser, fromdate, todate, 
//						        "N", lstproject, 3,objuser, fromdate, todate, 
//						        "N", 3,fromdate, todate,objuser.getUsernotify(), pageable);
//					count = lslogilablimsorderdetailRepository
//							.countByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndOrdercancellIsNullOrOrderflagAndLsprojectmasterIsNullAndLssamplemasterInAndCreatedtimestampBetweenOrOrderflagAndFiletypeAndCreatedtimestampBetweenOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenOrOrderflagAndLsprojectmasterInAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndOrdercancellIsNullAndCreatedtimestampBetweenAndLsuserMasterInOrderByBatchcodeDesc(
//							        "N", lstproject, fromdate, todate, 
//							        "N", lstsample1, fromdate, todate, 
//							        "N", 0, fromdate, todate, 
//							        "N", 1, objuser, fromdate, todate, 
//							        "N", 2, objuser, fromdate, todate, 
//							        "N", lstproject, 3,objuser, fromdate, todate,
//							        "N", 3,fromdate, todate,objuser.getUsernotify());

			} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {

//					lstorders = lslogilablimsorderdetailRepository
//							.findByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndOrdercancellIsNullAndTestcodeOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenAndTestcodeOrOrderflagAndLsprojectmasterInAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenAndTestcodeOrderByBatchcodeDesc(
//									"N", lstproject, fromdate, todate, testcode, 
//									"N", 0, fromdate, todate, testcode,
//									"N", 1, objuser, fromdate, todate, testcode, 
//									"N", 2, objuser, fromdate, todate, testcode, 
//									"N", lstproject, 3, objuser, fromdate, todate, testcode, pageable);
//					count = lslogilablimsorderdetailRepository
//							.countByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndOrdercancellIsNullAndTestcodeOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenAndTestcodeOrOrderflagAndLsprojectmasterInAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenAndTestcodeOrderByBatchcodeDesc(
//									"N", lstproject, fromdate, todate, testcode,
//									"N", 0, fromdate, todate, testcode,
//									"N", 1, objuser, fromdate, todate, testcode, 
//									"N", 2, objuser, fromdate, todate,testcode, 
//									"N", lstproject, 3, objuser, fromdate, todate, testcode);

				lstorders = lslogilablimsorderdetailRepository
						.findByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndLssamplemasterInAndCreatedtimestampBetweenAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterInAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndOrdercancellIsNullAndCreatedtimestampBetweenAndLsuserMasterInAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndTestcodeOrOrderflagAndAssignedtoAndCreatedtimestampBetweenAndTestcodeOrderByBatchcodeDesc(
								"N", lstproject, fromdate, todate, testcode, "N", lstsample1, fromdate, todate,
								testcode, "N", 0, fromdate, todate, testcode, "N", 1, objuser, fromdate, todate,
								testcode, "N", 2, objuser, fromdate, todate, testcode, "N", lstproject, 3, objuser,
								fromdate, todate, testcode, "N", 3, fromdate, todate, objuser.getUsernotify(),
								testcode, "N", objuser, objuser, fromdate, todate, testcode, "N", objuser, fromdate,
								todate,testcode, pageable);

				count = lslogilablimsorderdetailRepository
						.countByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndLssamplemasterInAndCreatedtimestampBetweenAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterInAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndOrdercancellIsNullAndCreatedtimestampBetweenAndLsuserMasterInAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndTestcodeOrOrderflagAndAssignedtoAndCreatedtimestampBetweenAndTestcodeOrderByBatchcodeDesc(
								"N", lstproject, fromdate, todate, testcode, "N", lstsample1, fromdate, todate,
								testcode, "N", 0, fromdate, todate, testcode, "N", 1, objuser, fromdate, todate,
								testcode, "N", 2, objuser, fromdate, todate, testcode, "N", lstproject, 3, objuser,
								fromdate, todate, testcode, "N", 3, fromdate, todate, objuser.getUsernotify(),
								testcode, "N", objuser, objuser, fromdate, todate, testcode, "N", objuser, fromdate,
								todate,testcode);

			} else if (testcode == -1 && objuser.getLstprojectforfilter() != null) {
				lstorders = lslogilablimsorderdetailRepository
						.findByOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterAndAssignedtoIsNullOrOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndLsprojectmasterOrOrderflagAndAssignedtoAndCreatedtimestampBetweenAndLsprojectmasterOrderByBatchcodeDesc(
								"N", objuser.getLstprojectforfilter(), fromdate, todate, "N", 0, fromdate, todate,
								objuser.getLstprojectforfilter(), objuser, objuser, fromdate, todate,
                            objuser.getLstprojectforfilter(), "N", objuser, objuser, fromdate, todate, objuser.getLstprojectforfilter(), "N", objuser, fromdate, todate,
								objuser.getLstprojectforfilter(), pageable);
				count = lslogilablimsorderdetailRepository
						.countByOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterAndAssignedtoIsNullOrOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndLsprojectmasterOrOrderflagAndAssignedtoAndCreatedtimestampBetweenAndLsprojectmaster(
								"N", objuser.getLstprojectforfilter(), fromdate, todate, "N", 0, fromdate, todate,
								objuser.getLstprojectforfilter(), objuser, objuser, fromdate, todate,
                            objuser.getLstprojectforfilter(), "N", objuser, objuser, fromdate, todate, objuser.getLstprojectforfilter(), "N", objuser, fromdate, todate,
								objuser.getLstprojectforfilter());

				// lstorders = lslogilablimsorderdetailRepository
//							.findByOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndOrdercancellIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterOrderByBatchcodeDesc(
//									"N", objuser.getLstprojectforfilter(), fromdate, todate, "N", 0, fromdate,
//									todate, objuser.getLstprojectforfilter(), pageable);
//					count = lslogilablimsorderdetailRepository
//							.countByOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndOrdercancellIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterOrderByBatchcodeDesc(
//									"N", objuser.getLstprojectforfilter(), fromdate, todate, "N", 0, fromdate,
//									todate, objuser.getLstprojectforfilter());

			} else {
//					lstorders = lslogilablimsorderdetailRepository
//							.findByOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndOrdercancellIsNullAndTestcodeOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterAndTestcodeOrderByBatchcodeDesc(
//									"N", objuser.getLstprojectforfilter(), fromdate, todate, testcode, "N", 0,
//									fromdate, todate, objuser.getLstprojectforfilter(), testcode, pageable);
//					count = lslogilablimsorderdetailRepository
//							.countByOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndOrdercancellIsNullAndTestcodeOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterAndTestcodeOrderByBatchcodeDesc(
//									"N", objuser.getLstprojectforfilter(), fromdate, todate, testcode, "N", 0,
//									fromdate, todate, objuser.getLstprojectforfilter(), testcode);
				lstorders = lslogilablimsorderdetailRepository
						.findByOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndOrdercancellIsNullAndTestcodeAndAssignedtoIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterAndTestcodeAndAssignedtoIsNullOrOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndLsprojectmasterAndTestcodeOrOrderflagAndAssignedtoAndCreatedtimestampBetweenAndLsprojectmasterAndTestcodeOrderByBatchcodeDesc(
								"N", objuser.getLstprojectforfilter(), fromdate, todate, testcode, "N", 0, fromdate,
								todate, objuser.getLstprojectforfilter(), testcode, "N", objuser, objuser, fromdate,
								todate, objuser.getLstprojectforfilter(), testcode, "N", objuser, fromdate, todate,
								objuser.getLstprojectforfilter(), testcode, pageable);
				count = lslogilablimsorderdetailRepository
						.countByOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndOrdercancellIsNullAndTestcodeAndAssignedtoIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterAndTestcodeAndAssignedtoIsNullOrOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndLsprojectmasterAndTestcodeOrOrderflagAndAssignedtoAndCreatedtimestampBetweenAndLsprojectmasterAndTestcodeOrderByBatchcodeDesc(
								"N", objuser.getLstprojectforfilter(), fromdate, todate, testcode, "N", 0, fromdate,
								todate, objuser.getLstprojectforfilter(), testcode, "N", objuser, objuser, fromdate,
								todate, objuser.getLstprojectforfilter(), testcode, "N", objuser, fromdate, todate,
								objuser.getLstprojectforfilter(), testcode);

			}

		} else if (objuser.getObjuser().getOrderselectiontype() == 4) {
			lstorders = lslogilablimsorderdetailRepository
					.findByOrderflagAndLssamplefileInAndCreatedtimestampBetween("N", lssamplefile, fromdate, todate,
							pageable);
			count = lslogilablimsorderdetailRepository.countByOrderflagAndLssamplefileInAndCreatedtimestampBetween(
					"N", lssamplefile, fromdate, todate);
		} else if (objuser.getObjuser().getOrderselectiontype() == 5) {
			if (testcode == -1 && objuser.getLstprojectforfilter() == null) {

				lstorders = lslogilablimsorderdetailRepository
						.findByApprovelstatusAndLsprojectmasterInAndCreatedtimestampBetweenOrApprovelstatusAndLsprojectmasterIsNullAndLssamplemasterInAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusAndLsprojectmasterIsNull(
								3, lstproject, fromdate, todate, 3, lstsample1, fromdate, todate, 1, objuser,
								fromdate, todate, 3, 2, objuser, fromdate, todate, 3, 3, objuser, fromdate, todate,
								3, 3, objuser, fromdate, todate, 3, pageable);
				count = lslogilablimsorderdetailRepository
						.countByApprovelstatusAndLsprojectmasterInAndCreatedtimestampBetweenOrApprovelstatusAndLsprojectmasterIsNullAndLssamplemasterInAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusAndLsprojectmasterIsNull(
								3, lstproject, fromdate, todate, 3, lstsample1, fromdate, todate, 1, objuser,
								fromdate, todate, 3, 2, objuser, fromdate, todate, 3, 3, objuser, fromdate, todate,
								3, 3, objuser, fromdate, todate, 3);

//					lstorders = lslogilablimsorderdetailRepository
//							.findByApprovelstatusAndLsprojectmasterInAndCreatedtimestampBetween(3, lstproject,
//									fromdate, todate, pageable);
//					count = lslogilablimsorderdetailRepository
//							.countByApprovelstatusAndLsprojectmasterInAndCreatedtimestampBetween(3, lstproject,
//									fromdate, todate);

			} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {

				lstorders = lslogilablimsorderdetailRepository
						.findByApprovelstatusAndLsprojectmasterInAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusAndLsprojectmasterInAndTestcode(
								3, lstproject, fromdate, todate, testcode, 1, objuser, fromdate, todate, 3,
								testcode, 2, objuser, fromdate, todate, 3, testcode, 3, objuser, fromdate, todate,
								3, testcode, 3, objuser, fromdate, todate, 3, lstproject, testcode, pageable);
				count = lslogilablimsorderdetailRepository
						.countByApprovelstatusAndLsprojectmasterInAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusAndLsprojectmasterInAndTestcode(
								3, lstproject, fromdate, todate, testcode, 1, objuser, fromdate, todate, 3,
								testcode, 2, objuser, fromdate, todate, 3, testcode, 3, objuser, fromdate, todate,
								3, testcode, 3, objuser, fromdate, todate, 3, lstproject, testcode);
//					lstorders = lslogilablimsorderdetailRepository
//							.findByApprovelstatusAndLsprojectmasterInAndCreatedtimestampBetweenAndTestcode(3,
//									lstproject, fromdate, todate, testcode, pageable);
//					count = lslogilablimsorderdetailRepository
//							.countByApprovelstatusAndLsprojectmasterInAndCreatedtimestampBetweenAndTestcode(3,
//									lstproject, fromdate, todate, testcode);

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

//					lstorders = lslogilablimsorderdetailRepository
//							.findByOrdercancellAndLsprojectmasterInAndCreatedtimestampBetween(1, lstproject,
//									fromdate, todate, pageable);
//					count = lslogilablimsorderdetailRepository
//							.countByOrdercancellAndLsprojectmasterInAndCreatedtimestampBetween(1, lstproject,
//									fromdate, todate);
			} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {

				lstorders = lslogilablimsorderdetailRepository
						.findByOrdercancellAndLsprojectmasterInAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndLsprojectmasterInAndTestcode(
								1, lstproject, fromdate, todate, testcode, 1, objuser, fromdate, todate, 1,
								testcode, 2, objuser, fromdate, todate, 1, testcode, 3, objuser, fromdate, todate,
								1, lstproject, testcode, pageable);
				count = lslogilablimsorderdetailRepository
						.countByOrdercancellAndLsprojectmasterInAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndLsprojectmasterInAndTestcode(
								1, lstproject, fromdate, todate, testcode, 1, objuser, fromdate, todate, 1,
								testcode, 2, objuser, fromdate, todate, 1, testcode, 3, objuser, fromdate, todate,
								1, lstproject, testcode);

//					lstorders = lslogilablimsorderdetailRepository
//							.findByOrdercancellAndLsprojectmasterInAndCreatedtimestampBetweenAndTestcode(1,
//									lstproject, fromdate, todate, testcode, pageable);
//					count = lslogilablimsorderdetailRepository
//							.countByOrdercancellAndLsprojectmasterInAndCreatedtimestampBetweenAndTestcode(1,
//									lstproject, fromdate, todate, testcode);

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
//			List<LSsamplemaster> lstsample1 = lssamplemasterrepository
//					.findByLssitemasterAndStatus(objuser.getLssitemaster(), 1);
		List<LSSheetOrderStructure> lstdir = lsSheetOrderStructureRepository
				.findBySitemasterAndViewoptionOrCreatedbyAndViewoptionOrderByDirectorycode(
						objuser.getLssitemaster(), 1, objuser, 2);
		List<Long> directorycode = lstdir.stream().map(LSSheetOrderStructure::getDirectorycode)
				.collect(Collectors.toList());
		if (objuser.getObjuser().getOrderselectiontype() == 1) {

			if (testcode == -1 && objuser.getLstprojectforfilter() == null) {
				lstorders = lslogilablimsorderdetailRepository
						.findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterOrderByBatchcodeDesc(
								lstsample1, 1, fromdate, todate, lstsample1, 2, fromdate, todate, objuser, 3,
								fromdate, todate, objuser, directorycode, 1, fromdate, todate, directorycode, 2,
								fromdate, todate, objuser, directorycode, 3, fromdate, todate, objuser, pageable);

				count = lslogilablimsorderdetailRepository
						.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterOrderByBatchcodeDesc(
								lstsample1, 1, fromdate, todate, lstsample1, 2, fromdate, todate, objuser, 3,
								fromdate, todate, objuser, directorycode, 1, fromdate, todate, directorycode, 2,
								fromdate, todate, objuser, directorycode, 3, fromdate, todate, objuser);
//							.findByLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetween
//							OrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetween
//							OrLsprojectmasterIsNullAndLsuserMasterIsNotAndLssamplemasterInAndViewoptionAndCreatedtimestampBetween
//							OrLsprojectmasterIsNullAndLsuserMasterIsNotAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenOrderByBatchcodeDesc(
//									1, objuser, fromdate, todate, 2, objuser, fromdate, todate, objuser, lstsample1,
//									1, fromdate, todate, objuser, lstsample1, 2, fromdate, todate, pageable);
//					count = lslogilablimsorderdetailRepository
//							.countByLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndLsuserMasterIsNotAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndLsuserMasterIsNotAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenOrderByBatchcodeDesc(
//									1, objuser, fromdate, todate, 2, objuser, fromdate, todate, objuser, lstsample1,
//									1, fromdate, todate, objuser, lstsample1, 2, fromdate, todate);
			} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {

				lstorders = lslogilablimsorderdetailRepository
						.findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTestcodeOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTestcodeOrderByBatchcodeDesc(
								lstsample1, 1, fromdate, todate, testcode, lstsample1, 2, fromdate, todate, objuser,
								testcode, 3, fromdate, todate, objuser, testcode, directorycode, 1, fromdate,
								todate, testcode, directorycode, 2, fromdate, todate, objuser, testcode,
								directorycode, 3, fromdate, todate, objuser, testcode, pageable);

				count = lslogilablimsorderdetailRepository
						.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTestcodeOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTestcodeOrderByBatchcodeDesc(
								lstsample1, 1, fromdate, todate, testcode, lstsample1, 2, fromdate, todate, objuser,
								testcode, 3, fromdate, todate, objuser, testcode, directorycode, 1, fromdate,
								todate, testcode, directorycode, 2, fromdate, todate, objuser, testcode,
								directorycode, 3, fromdate, todate, objuser, testcode);

			}

		} else if (objuser.getObjuser().getOrderselectiontype() == 2) {
			if (testcode == -1 && objuser.getLstprojectforfilter() == null) {
				lstorders = lslogilablimsorderdetailRepository
						.findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullOrderByBatchcodeDesc(
								lstsample1, 1, fromdate, todate, "R", 3, lstsample1, 1, fromdate, todate, "R",
								lstsample1, 2, fromdate, todate, objuser, "R", 3, lstsample1, 2, fromdate, todate,
								objuser, "R", 3, fromdate, todate, objuser, "R", 3, 3, fromdate, todate, objuser,
								"R", directorycode, 1, fromdate, todate, "R", 3, directorycode, 1, fromdate, todate,
								"R", directorycode, 2, fromdate, todate, objuser, "R", 3, directorycode, 2,
								fromdate, todate, objuser, "R", directorycode, 3, fromdate, todate, objuser, "R", 3,
								directorycode, 3, fromdate, todate, objuser, "R", pageable);

//							.findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrderByBatchcodeDesc(
//									lstsample1, 1, fromdate, todate, "R", 3, lstsample1, 2, fromdate, todate, objuser,
//									"R", 3, 3, fromdate, todate, objuser, "R", 3, directorycode, 1, fromdate,
//									todate, "R", 3, directorycode, 2, fromdate, todate, objuser, "R", 3,
//									directorycode, 3, fromdate, todate, objuser, "R", 3, pageable);

//					count = lslogilablimsorderdetailRepository
//							.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrderByBatchcodeDesc(
//									lstsample1, 1, fromdate, todate, "R", 3, lstsample1, 2, fromdate, todate, objuser,
//									"R", 3, 3, fromdate, todate, objuser, "R", 3, directorycode, 1, fromdate,
//									todate, "R", 3, directorycode, 2, fromdate, todate, objuser, "R", 3,
//									directorycode, 3, fromdate, todate, objuser, "R", 3);
				count = lslogilablimsorderdetailRepository
						.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullOrderByBatchcodeDesc(
								lstsample1, 1, fromdate, todate, "R", 3, lstsample1, 1, fromdate, todate, "R",
								lstsample1, 2, fromdate, todate, objuser, "R", 3, lstsample1, 2, fromdate, todate,
								objuser, "R", 3, fromdate, todate, objuser, "R", 3, 3, fromdate, todate, objuser,
								"R", directorycode, 1, fromdate, todate, "R", 3, directorycode, 1, fromdate, todate,
								"R", directorycode, 2, fromdate, todate, objuser, "R", 3, directorycode, 2,
								fromdate, todate, objuser, "R", directorycode, 3, fromdate, todate, objuser, "R", 3,
								directorycode, 3, fromdate, todate, objuser, "R");

			} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {

				lstorders = lslogilablimsorderdetailRepository
						.findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullAndTestcodeOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullAndTestcodeOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullAndTestcodeOrderByBatchcodeDesc(
								lstsample1, 1, fromdate, todate, "R", 3, testcode, lstsample1, 1, fromdate, todate,
								"R", testcode, lstsample1, 2, fromdate, todate, objuser, "R", 3, testcode,
								lstsample1, 2, fromdate, todate, objuser, "R", testcode, 3, fromdate, todate,
								objuser, "R", 3, testcode, 3, fromdate, todate, objuser, "R", testcode,
								directorycode, 1, fromdate, todate, "R", 3, testcode, directorycode, 1, fromdate,
								todate, "R", testcode, directorycode, 2, fromdate, todate, objuser, "R", 3,
								testcode, directorycode, 2, fromdate, todate, objuser, "R", testcode, directorycode,
								3, fromdate, todate, objuser, "R", 3, testcode, directorycode, 3, fromdate, todate,
								objuser, "R", testcode, pageable);

				count = lslogilablimsorderdetailRepository
						.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullAndTestcodeOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullAndTestcodeOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullAndTestcodeOrderByBatchcodeDesc(
								lstsample1, 1, fromdate, todate, "R", 3, testcode, lstsample1, 1, fromdate, todate,
								"R", testcode, lstsample1, 2, fromdate, todate, objuser, "R", 3, testcode,
								lstsample1, 2, fromdate, todate, objuser, "R", testcode, 3, fromdate, todate,
								objuser, "R", 3, testcode, 3, fromdate, todate, objuser, "R", testcode,
								directorycode, 1, fromdate, todate, "R", 3, testcode, directorycode, 1, fromdate,
								todate, "R", testcode, directorycode, 2, fromdate, todate, objuser, "R", 3,
								testcode, directorycode, 2, fromdate, todate, objuser, "R", testcode, directorycode,
								3, fromdate, todate, objuser, "R", 3, testcode, directorycode, 3, fromdate, todate,
								objuser, "R", testcode);
//							.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusNotAndTestcodeAndOrdercancellIsNullOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndTestcodeAndOrdercancellIsNullOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndTestcodeAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusNotAndTestcodeAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndTestcodeAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndTestcodeAndOrdercancellIsNullOrderByBatchcodeDesc(
//									lstsample1, 1, fromdate, todate, "R", 3, testcode, lstsample1, 2, fromdate,
//									todate, objuser, "R", 3, testcode, 3, fromdate, todate, objuser, "R", 3,
//									testcode, directorycode, 1, fromdate, todate, "R", 3, testcode, directorycode,
//									2, fromdate, todate, objuser, "R", 3, testcode, directorycode, 3, fromdate,
//									todate, objuser, "R", 3, testcode);

			}

		} else if (objuser.getObjuser().getOrderselectiontype() == 3) {
			if (testcode == -1 && objuser.getLstprojectforfilter() == null) {

				lstorders = lslogilablimsorderdetailRepository
						.findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullOrderByBatchcodeDesc(
								lstsample1, 1, fromdate, todate, "N", lstsample1, 2, fromdate, todate, objuser, "N",
								3, fromdate, todate, objuser, "N", directorycode, 1, fromdate, todate, "N",
								directorycode, 2, fromdate, todate, objuser, "N", directorycode, 3, fromdate,
								todate, objuser, "N", pageable);

//					lstorders = lslogilablimsorderdetailRepository
//							.findByOrderflagAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenOrOrderflagAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenOrOrderflagAndOrdercancellIsNullAndLsprojectmasterIsNullAndLsuserMasterIsNotAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenOrOrderflagAndOrdercancellIsNullAndLsprojectmasterIsNullAndLsuserMasterIsNotAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenOrderByBatchcodeDesc(
//									"N", 1, objuser, fromdate, todate, "N", 2, objuser, fromdate, todate, "N",
//									objuser, lstsample1, 1, fromdate, todate, "N", objuser, lstsample1, 2, fromdate,
//									todate, pageable);
				count = lslogilablimsorderdetailRepository
						.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullOrderByBatchcodeDesc(
								lstsample1, 1, fromdate, todate, "N", lstsample1, 2, fromdate, todate, objuser, "N",
								3, fromdate, todate, objuser, "N", directorycode, 1, fromdate, todate, "N",
								directorycode, 2, fromdate, todate, objuser, "N", directorycode, 3, fromdate,
								todate, objuser, "N");

			} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {

				lstorders = lslogilablimsorderdetailRepository
						.findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndTestcodeOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndTestcodeOrderByBatchcodeDesc(
								lstsample1, 1, fromdate, todate, "N", testcode, lstsample1, 2, fromdate, todate,
								objuser, "N", testcode, 3, fromdate, todate, objuser, "N", testcode, directorycode,
								1, fromdate, todate, "N", testcode, directorycode, 2, fromdate, todate, objuser,
								"N", testcode, directorycode, 3, fromdate, todate, objuser, "N", testcode,
								pageable);

//					lstorders = lslogilablimsorderdetailRepository
//							.findByOrderflagAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndTestcodeOrOrderflagAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndTestcodeOrOrderflagAndOrdercancellIsNullAndLsprojectmasterIsNullAndLsuserMasterIsNotAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrOrderflagAndOrdercancellIsNullAndLsprojectmasterIsNullAndLsuserMasterIsNotAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrderByBatchcodeDesc(
//									"N", 1, objuser, fromdate, todate, testcode, "N", 2, objuser, fromdate, todate,
//									testcode, "N", objuser, lstsample1, 1, fromdate, todate, testcode, "N", objuser,
//									lstsample1, 2, fromdate, todate, testcode, pageable);
				count = lslogilablimsorderdetailRepository
						.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndTestcodeOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndTestcodeOrderByBatchcodeDesc(
								lstsample1, 1, fromdate, todate, "N", testcode, lstsample1, 2, fromdate, todate,
								objuser, "N", testcode, 3, fromdate, todate, objuser, "N", testcode, directorycode,
								1, fromdate, todate, "N", testcode, directorycode, 2, fromdate, todate, objuser,
								"N", testcode, directorycode, 3, fromdate, todate, objuser, "N", testcode);
			}

		} else if (objuser.getObjuser().getOrderselectiontype() == 4) {
			lstorders = lslogilablimsorderdetailRepository
					.findByOrderflagAndLssamplefileInAndCreatedtimestampBetween("N", lssamplefile, fromdate, todate,
							pageable);
			count = lslogilablimsorderdetailRepository.countByOrderflagAndLssamplefileInAndCreatedtimestampBetween(
					"N", lssamplefile, fromdate, todate);
		} else if (objuser.getObjuser().getOrderselectiontype() == 5) {
			if (testcode == -1 && objuser.getLstprojectforfilter() == null) {
				lstorders = lslogilablimsorderdetailRepository
						.findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndApprovelstatusOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndApprovelstatusOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusOrderByBatchcodeDesc(
								lstsample1, 1, fromdate, todate, 3, lstsample1, 2, fromdate, todate, objuser, 3, 3,
								fromdate, todate, objuser, 3, directorycode, 1, fromdate, todate, 3, directorycode,
								2, fromdate, todate, objuser, 3, directorycode, 3, fromdate, todate, objuser, 3,
								pageable);

//					lstorders = lslogilablimsorderdetailRepository
//							.findByApprovelstatusAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenOrApprovelstatusAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenOrApprovelstatusAndLsprojectmasterIsNullAndLsuserMasterIsNotAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenOrApprovelstatusAndLsprojectmasterIsNullAndLsuserMasterIsNotAndLssamplemasterInAndViewoptionAndCreatedtimestampBetween(
//									3, 1, objuser, fromdate, todate, 3, 2, objuser, fromdate, todate, 3, objuser,
//									lstsample1, 1, fromdate, todate, 3, objuser, lstsample1, 1, fromdate, todate,
//									pageable);

				count = lslogilablimsorderdetailRepository
						.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndApprovelstatusOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndApprovelstatusOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusOrderByBatchcodeDesc(
								lstsample1, 1, fromdate, todate, 3, lstsample1, 2, fromdate, todate, objuser, 3, 3,
								fromdate, todate, objuser, 3, directorycode, 1, fromdate, todate, 3, directorycode,
								2, fromdate, todate, objuser, 3, directorycode, 3, fromdate, todate, objuser, 3);

			} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {
				lstorders = lslogilablimsorderdetailRepository
						.findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndApprovelstatusAndTestcodeOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusAndTestcodeOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndApprovelstatusAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusAndTestcodeOrderByBatchcodeDesc(
								lstsample1, 1, fromdate, todate, 3, testcode, lstsample1, 2, fromdate, todate,
								objuser, 3, testcode, 3, fromdate, todate, objuser, 3, testcode, directorycode, 1,
								fromdate, todate, 3, testcode, directorycode, 2, fromdate, todate, objuser, 3,
								testcode, directorycode, 3, fromdate, todate, objuser, 3, testcode, pageable);
//					lstorders = lslogilablimsorderdetailRepository
//							.findByApprovelstatusAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndTestcodeOrApprovelstatusAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndTestcodeOrApprovelstatusAndLsprojectmasterIsNullAndLsuserMasterIsNotAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrApprovelstatusAndLsprojectmasterIsNullAndLsuserMasterIsNotAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndTestcode(
//									3, 1, objuser, fromdate, todate, testcode, 3, 2, objuser, fromdate, todate,
//									testcode, 3, objuser, lstsample1, 1, fromdate, todate, testcode, 3, objuser,
//									lstsample1, 1, fromdate, todate, testcode, pageable);

				count = lslogilablimsorderdetailRepository
						.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndApprovelstatusAndTestcodeOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusAndTestcodeOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndApprovelstatusAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusAndTestcodeOrderByBatchcodeDesc(
								lstsample1, 1, fromdate, todate, 3, testcode, lstsample1, 2, fromdate, todate,
								objuser, 3, testcode, 3, fromdate, todate, objuser, 3, testcode, directorycode, 1,
								fromdate, todate, 3, testcode, directorycode, 2, fromdate, todate, objuser, 3,
								testcode, directorycode, 3, fromdate, todate, objuser, 3, testcode);

			}
		} else if (objuser.getObjuser().getOrderselectiontype() == 6) {
			if (testcode == -1 && objuser.getLstprojectforfilter() == null) {
				lstorders = lslogilablimsorderdetailRepository
						.findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrdercancellOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrdercancellOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellOrderByBatchcodeDesc(
								lstsample1, 1, fromdate, todate, 1, lstsample1, 2, fromdate, todate, objuser, 1, 3,
								fromdate, todate, objuser, 1, directorycode, 1, fromdate, todate, 1, directorycode,
								2, fromdate, todate, objuser, 1, directorycode, 3, fromdate, todate, objuser, 1,
								pageable);

//					lstorders = lslogilablimsorderdetailRepository
//							.findByOrdercancellAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenOrOrdercancellAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenOrOrdercancellAndLsprojectmasterIsNullAndLsuserMasterIsNotAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenOrOrdercancellAndLsprojectmasterIsNullAndLsuserMasterIsNotAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenOrderByBatchcodeDesc(
//									1, 1, objuser, fromdate, todate, 1, 2, objuser, fromdate, todate, 1, objuser,
//									lstsample1, 1, fromdate, todate, 1, objuser, lstsample1, 1, fromdate, todate,
//									pageable);

				count = lslogilablimsorderdetailRepository
						.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrdercancellOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrdercancellOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellOrderByBatchcodeDesc(
								lstsample1, 1, fromdate, todate, 1, lstsample1, 2, fromdate, todate, objuser, 1, 3,
								fromdate, todate, objuser, 1, directorycode, 1, fromdate, todate, 1, directorycode,
								2, fromdate, todate, objuser, 1, directorycode, 3, fromdate, todate, objuser, 1);

			} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {
				lstorders = lslogilablimsorderdetailRepository
						.findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrdercancellAndTestcodeOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellAndTestcodeOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrdercancellAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellAndTestcodeOrderByBatchcodeDesc(
								lstsample1, 1, fromdate, todate, 1, testcode, lstsample1, 2, fromdate, todate,
								objuser, 1, testcode, 3, fromdate, todate, objuser, 1, testcode, directorycode, 1,
								fromdate, todate, 1, testcode, directorycode, 2, fromdate, todate, objuser, 1,
								testcode, directorycode, 3, fromdate, todate, objuser, 1, testcode, pageable);

//							.findByOrdercancellAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndTestcodeOrOrdercancellAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndTestcodeOrOrdercancellAndLsprojectmasterIsNullAndLsuserMasterIsNotAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrOrdercancellAndLsprojectmasterIsNullAndLsuserMasterIsNotAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrderByBatchcodeDesc(
//									1, 1, objuser, fromdate, todate, testcode, 1, 2, objuser, fromdate, todate,
//									testcode, 1, objuser, lstsample1, 1, fromdate, todate, testcode, 1, objuser,
//									lstsample1, 1, fromdate, todate, testcode, pageable);

				count = lslogilablimsorderdetailRepository
						.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrdercancellAndTestcodeOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellAndTestcodeOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrdercancellAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellAndTestcodeOrderByBatchcodeDesc(
								lstsample1, 1, fromdate, todate, 1, testcode, lstsample1, 2, fromdate, todate,
								objuser, 1, testcode, 3, fromdate, todate, objuser, 1, testcode, directorycode, 1,
								fromdate, todate, 1, testcode, directorycode, 2, fromdate, todate, objuser, 1,
								testcode, directorycode, 3, fromdate, todate, objuser, 1, testcode);

			}

		}
		lstorders.forEach(objorder -> objorder.setLstworkflow(lstworkflow));
	}
	Collections.sort(lstorders, Collections.reverseOrder());
	mapOrders.put("orderlst", lstorders);

//	}
	mapOrders.put("count", count);
	if (objuser.getObjsilentaudit() != null) {
		objuser.getObjsilentaudit().setTableName("LSlogilablimsorderdetail");
		objuser.getObjsilentaudit().setTransactiondate(commonfunction.getCurrentUtcTime());
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

	public Map<String, Object> Getdashboardparameters(LSuserMaster objuser) throws ParseException {
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
			objuser.getObjsilentaudit().setTransactiondate(commonfunction.getCurrentUtcTime());
			lscfttransactionRepository.save(objuser.getObjsilentaudit());
		}

		return mapOrders;
	}

	public Map<String, Object> Getdashboardactivities(LSuserMaster objuser) throws ParseException {
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
			objuser.getObjsilentaudit().setTransactiondate(commonfunction.getCurrentUtcTime());
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
	public Map<String, Object> Getglobalsearchorders(Map<String, Object> obj) {
		Map<String, Object> rtnobj = new HashMap<>();
		ObjectMapper objectmapper = new ObjectMapper();
		LSuserMaster objuser = objectmapper.convertValue(obj.get("lsusermaster"), new TypeReference<LSuserMaster>() {
		});
		Date fromdate = objuser.getObjuser().getFromdate();
		Date todate = objuser.getObjuser().getTodate();
		Integer testcode = objuser.getTestcode();
		Pageable pageable = new PageRequest(objuser.getPagesize(), objuser.getPageperorder());
		long count = 0;
		List<Logilabprotocolorders> lstordersprotocol = new ArrayList<Logilabprotocolorders>();
		String searchkeywords = (String) obj.get("searchkeywords");
		List<LSprojectmaster> lstproject = objuser.getLstproject();
		List<Logilabordermaster> lstorders = new ArrayList<Logilabordermaster>();
		List<Integer> lstsampleint = lssamplemasterrepository
				.getDistinctByLssitemasterSitecodeAndStatus(objuser.getLssitemaster().getSitecode(), 1);
		List<LSsamplemaster> lstsample = new ArrayList<>();
		LSsamplemaster sample = null;
		if (lstsampleint.size() > 0) {
			for (Integer item : lstsampleint) {
				sample = new LSsamplemaster();
				sample.setSamplecode(item);
				lstsample.add(sample);
				sample = null;
			}
		}
		List<Integer> userlist = objuser.getUsernotify() != null
				? objuser.getUsernotify().stream().map(LSuserMaster::getUsercode).collect(Collectors.toList())
				: new ArrayList<Integer>();
		if (lstproject != null) {
			objuser.getUsernotify().add(objuser);
			if (objuser.getObjuser().getOrderselectiontype() == 1) {

				if (testcode == -1 && objuser.getLstprojectforfilter() == null) {
					if (objuser.getObjuser().getOrderfor() == 1) {
//						lstorders = lslogilablimsorderdetailRepository
//								.findByLsprojectmasterInAndBatchidContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseOrLsprojectmasterInAndBatchidContainingIgnoreCaseAndKeywordIsNullOrLsprojectmasterInAndBatchidContainingIgnoreCaseAndAndTestnameIsNullOrFiletypeAndBatchidContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseOrFiletypeAndBatchidContainingIgnoreCaseAndKeywordIsNullOrFiletypeAndBatchidContainingIgnoreCaseAndTestnameIsNullOrLsprojectmasterIsNullAndLssamplemasterInAndBatchidContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseOrLsprojectmasterIsNullAndLssamplemasterInAndBatchidContainingIgnoreCaseAndKeywordIsNullOrLsprojectmasterIsNullAndLssamplemasterInAndBatchidContainingIgnoreCaseAndTestnameIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndBatchidContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndBatchidContainingIgnoreCaseAndKeywordIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndBatchidContainingIgnoreCaseAndTestnameIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndBatchidContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndBatchidContainingIgnoreCaseAndKeywordIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndBatchidContainingIgnoreCaseAndTestnameIsNullOrViewoptionAndLsuserMasterAndLsprojectmasterInAndBatchidContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseOrViewoptionAndLsuserMasterAndLsprojectmasterInAndBatchidContainingIgnoreCaseAndKeywordIsNullOrViewoptionAndLsuserMasterAndLsprojectmasterInAndBatchidContainingIgnoreCaseAndTestnameIsNullOrViewoptionAndLsuserMasterInAndLsprojectmasterIsNullAndBatchidContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseOrViewoptionAndLsuserMasterInAndLsprojectmasterIsNullAndBatchidContainingIgnoreCaseAndKeywordIsNullOrViewoptionAndLsuserMasterInAndLsprojectmasterIsNullAndBatchidContainingIgnoreCaseAndTestnameIsNullOrderByBatchcodeDesc(
//										lstproject, searchkeywords, searchkeywords, searchkeywords, lstproject,
//										searchkeywords, lstproject, searchkeywords, 0, searchkeywords, searchkeywords,
//										searchkeywords, 0, searchkeywords, 0, searchkeywords, lstsample, searchkeywords,
//										searchkeywords, searchkeywords, lstsample, searchkeywords, lstsample,
//										searchkeywords, 1, objuser, searchkeywords, searchkeywords, searchkeywords, 1,
//										objuser, searchkeywords, 1, objuser, searchkeywords, 2, objuser, searchkeywords,
//										searchkeywords, searchkeywords, 2, objuser, searchkeywords, 2, objuser,
//										searchkeywords, 3, objuser, lstproject, searchkeywords, searchkeywords,
//										searchkeywords, 3, objuser, lstproject, searchkeywords, 3, objuser, lstproject,
//										searchkeywords, 3, objuser.getUsernotify(), searchkeywords, searchkeywords,
//										searchkeywords, 3, objuser.getUsernotify(), searchkeywords, 3,
//										objuser.getUsernotify(), searchkeywords, pageable);
//						count = lslogilablimsorderdetailRepository
//								.countByLsprojectmasterInAndBatchidContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseOrLsprojectmasterInAndBatchidContainingIgnoreCaseAndKeywordIsNullOrLsprojectmasterInAndBatchidContainingIgnoreCaseAndAndTestnameIsNullOrFiletypeAndBatchidContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseOrFiletypeAndBatchidContainingIgnoreCaseAndKeywordIsNullOrFiletypeAndBatchidContainingIgnoreCaseAndTestnameIsNullOrLsprojectmasterIsNullAndLssamplemasterInAndBatchidContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseOrLsprojectmasterIsNullAndLssamplemasterInAndBatchidContainingIgnoreCaseAndKeywordIsNullOrLsprojectmasterIsNullAndLssamplemasterInAndBatchidContainingIgnoreCaseAndTestnameIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndBatchidContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndBatchidContainingIgnoreCaseAndKeywordIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndBatchidContainingIgnoreCaseAndTestnameIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndBatchidContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndBatchidContainingIgnoreCaseAndKeywordIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndBatchidContainingIgnoreCaseAndTestnameIsNullOrViewoptionAndLsuserMasterAndLsprojectmasterInAndBatchidContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseOrViewoptionAndLsuserMasterAndLsprojectmasterInAndBatchidContainingIgnoreCaseAndKeywordIsNullOrViewoptionAndLsuserMasterAndLsprojectmasterInAndBatchidContainingIgnoreCaseAndTestnameIsNullOrViewoptionAndLsuserMasterInAndLsprojectmasterIsNullAndBatchidContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseOrViewoptionAndLsuserMasterInAndLsprojectmasterIsNullAndBatchidContainingIgnoreCaseAndKeywordIsNullOrViewoptionAndLsuserMasterInAndLsprojectmasterIsNullAndBatchidContainingIgnoreCaseAndTestnameIsNullOrderByBatchcodeDesc(
//										lstproject, searchkeywords, searchkeywords, searchkeywords, lstproject,
//										searchkeywords, lstproject, searchkeywords, 0, searchkeywords, searchkeywords,
//										searchkeywords, 0, searchkeywords, 0, searchkeywords, lstsample, searchkeywords,
//										searchkeywords, searchkeywords, lstsample, searchkeywords, lstsample,
//										searchkeywords, 1, objuser, searchkeywords, searchkeywords, searchkeywords, 1,
//										objuser, searchkeywords, 1, objuser, searchkeywords, 2, objuser, searchkeywords,
//										searchkeywords, searchkeywords, 2, objuser, searchkeywords, 2, objuser,
//										searchkeywords, 3, objuser, lstproject, searchkeywords, searchkeywords,
//										searchkeywords, 3, objuser, lstproject, searchkeywords, 3, objuser, lstproject,
//										searchkeywords, 3, objuser.getUsernotify(), searchkeywords, searchkeywords,
//										searchkeywords, 3, objuser.getUsernotify(), searchkeywords, 3,
//										objuser.getUsernotify(), searchkeywords);

						lstorders = lslogilablimsorderdetailRepository
								.findByLsprojectmasterInAndBatchidContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseOrLsprojectmasterInAndBatchidContainingIgnoreCaseAndKeywordIsNullOrLsprojectmasterInAndBatchidContainingIgnoreCaseAndAndTestnameIsNullOrFiletypeAndBatchidContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseOrFiletypeAndBatchidContainingIgnoreCaseAndKeywordIsNullOrFiletypeAndBatchidContainingIgnoreCaseAndTestnameIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndBatchidContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndBatchidContainingIgnoreCaseAndKeywordIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndBatchidContainingIgnoreCaseAndTestnameIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndBatchidContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndBatchidContainingIgnoreCaseAndKeywordIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndBatchidContainingIgnoreCaseAndTestnameIsNullOrViewoptionAndLsuserMasterAndLsprojectmasterInAndBatchidContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseOrViewoptionAndLsuserMasterAndLsprojectmasterInAndBatchidContainingIgnoreCaseAndKeywordIsNullOrViewoptionAndLsuserMasterAndLsprojectmasterInAndBatchidContainingIgnoreCaseAndTestnameIsNullOrViewoptionAndLsuserMasterInAndLsprojectmasterIsNullAndBatchidContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseOrViewoptionAndLsuserMasterInAndLsprojectmasterIsNullAndBatchidContainingIgnoreCaseAndKeywordIsNullOrViewoptionAndLsuserMasterInAndLsprojectmasterIsNullAndBatchidContainingIgnoreCaseAndTestnameIsNullOrderByBatchcodeDesc(
										lstproject, searchkeywords, searchkeywords, searchkeywords, lstproject,
										searchkeywords, lstproject, searchkeywords, 0, searchkeywords, searchkeywords,
										searchkeywords, 0, searchkeywords, 0, searchkeywords, 1, objuser,
										searchkeywords, searchkeywords, searchkeywords, 1, objuser, searchkeywords, 1,
										objuser, searchkeywords, 2, objuser, searchkeywords, searchkeywords,
										searchkeywords, 2, objuser, searchkeywords, 2, objuser, searchkeywords, 3,
										objuser, lstproject, searchkeywords, searchkeywords, searchkeywords, 3, objuser,
										lstproject, searchkeywords, 3, objuser, lstproject, searchkeywords, 3,
										objuser.getUsernotify(), searchkeywords, searchkeywords, searchkeywords, 3,
										objuser.getUsernotify(), searchkeywords, 3, objuser.getUsernotify(),
										searchkeywords, pageable);

						lstorders.addAll(lslogilablimsorderdetailRepository
								.findByLsprojectmasterIsNullAndLssamplemasterInAndBatchidContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseAndLsuserMasterNotOrderByBatchcodeDesc(
										lstsample, searchkeywords, searchkeywords, searchkeywords, objuser, pageable));
						lstorders.addAll(lslogilablimsorderdetailRepository
								.findByLsprojectmasterIsNullAndLssamplemasterInAndBatchidContainingIgnoreCaseAndKeywordIsNullAndLsuserMasterNotOrderByBatchcodeDesc(
										lstsample, searchkeywords, objuser, pageable));
						lstorders.addAll(lslogilablimsorderdetailRepository
								.findByLsprojectmasterIsNullAndLssamplemasterInAndBatchidContainingIgnoreCaseAndTestnameIsNullAndLsuserMasterNotOrderByBatchcodeDesc(
										lstsample, searchkeywords, objuser, pageable));

						count = lslogilablimsorderdetailRepository
								.countByLsprojectmasterInAndBatchidContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseOrLsprojectmasterInAndBatchidContainingIgnoreCaseAndKeywordIsNullOrLsprojectmasterInAndBatchidContainingIgnoreCaseAndAndTestnameIsNullOrFiletypeAndBatchidContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseOrFiletypeAndBatchidContainingIgnoreCaseAndKeywordIsNullOrFiletypeAndBatchidContainingIgnoreCaseAndTestnameIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndBatchidContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndBatchidContainingIgnoreCaseAndKeywordIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndBatchidContainingIgnoreCaseAndTestnameIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndBatchidContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndBatchidContainingIgnoreCaseAndKeywordIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndBatchidContainingIgnoreCaseAndTestnameIsNullOrViewoptionAndLsuserMasterAndLsprojectmasterInAndBatchidContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseOrViewoptionAndLsuserMasterAndLsprojectmasterInAndBatchidContainingIgnoreCaseAndKeywordIsNullOrViewoptionAndLsuserMasterAndLsprojectmasterInAndBatchidContainingIgnoreCaseAndTestnameIsNullOrViewoptionAndLsuserMasterInAndLsprojectmasterIsNullAndBatchidContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseOrViewoptionAndLsuserMasterInAndLsprojectmasterIsNullAndBatchidContainingIgnoreCaseAndKeywordIsNullOrViewoptionAndLsuserMasterInAndLsprojectmasterIsNullAndBatchidContainingIgnoreCaseAndTestnameIsNullOrderByBatchcodeDesc(
										lstproject, searchkeywords, searchkeywords, searchkeywords, lstproject,
										searchkeywords, lstproject, searchkeywords, 0, searchkeywords, searchkeywords,
										searchkeywords, 0, searchkeywords, 0, searchkeywords, 1, objuser,
										searchkeywords, searchkeywords, searchkeywords, 1, objuser, searchkeywords, 1,
										objuser, searchkeywords, 2, objuser, searchkeywords, searchkeywords,
										searchkeywords, 2, objuser, searchkeywords, 2, objuser, searchkeywords, 3,
										objuser, lstproject, searchkeywords, searchkeywords, searchkeywords, 3, objuser,
										lstproject, searchkeywords, 3, objuser, lstproject, searchkeywords, 3,
										objuser.getUsernotify(), searchkeywords, searchkeywords, searchkeywords, 3,
										objuser.getUsernotify(), searchkeywords, 3, objuser.getUsernotify(),
										searchkeywords);

						count = count + lslogilablimsorderdetailRepository
								.countByLsprojectmasterIsNullAndLssamplemasterInAndBatchidContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseAndLsuserMasterNotOrderByBatchcodeDesc(
										lstsample, searchkeywords, searchkeywords, searchkeywords, objuser);

						count = count + lslogilablimsorderdetailRepository
								.countByLsprojectmasterIsNullAndLssamplemasterInAndBatchidContainingIgnoreCaseAndKeywordIsNullAndLsuserMasterNotOrderByBatchcodeDesc(
										lstsample, searchkeywords, objuser);
						count = count + lslogilablimsorderdetailRepository
								.countByLsprojectmasterIsNullAndLssamplemasterInAndBatchidContainingIgnoreCaseAndTestnameIsNullAndLsuserMasterNotOrderByBatchcodeDesc(
										lstsample, searchkeywords, objuser);

						lstorders.addAll(lslogilablimsorderdetailRepository
								.findByLsprojectmasterInAndCreatedtimestampBetweenAndBatchidNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseOrLsprojectmasterInAndCreatedtimestampBetweenAndKeywordContainingIgnoreCaseAndTestnameIsNullOrFiletypeAndCreatedtimestampBetweenAndBatchidNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseOrFiletypeAndCreatedtimestampBetweenAndKeywordContainingIgnoreCaseAndTestnameIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndBatchidNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndKeywordContainingIgnoreCaseAndTestnameIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndBatchidNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndKeywordContainingIgnoreCaseAndTestnameIsNullOrViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndLsprojectmasterInAndBatchidNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseOrViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndLsprojectmasterInAndKeywordContainingIgnoreCaseAndTestnameIsNullOrViewoptionAndLsuserMasterInAndCreatedtimestampBetweenAndLsprojectmasterIsNullAndBatchidNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseOrViewoptionAndLsuserMasterInAndCreatedtimestampBetweenAndLsprojectmasterIsNullAndKeywordContainingIgnoreCaseAndTestnameIsNullOrderByBatchcodeDesc(
										lstproject, fromdate, todate, searchkeywords, searchkeywords, searchkeywords,
										lstproject, fromdate, todate, searchkeywords, 0, fromdate, todate,
										searchkeywords, searchkeywords, searchkeywords, 0, fromdate, todate,
										searchkeywords, 1, objuser, fromdate, todate, searchkeywords, searchkeywords,
										searchkeywords, 1, objuser, fromdate, todate, searchkeywords, 2, objuser,
										fromdate, todate, searchkeywords, searchkeywords, searchkeywords, 2, objuser,
										fromdate, todate, searchkeywords, 3, objuser, fromdate, todate, lstproject,
										searchkeywords, searchkeywords, searchkeywords, 3, objuser, fromdate, todate,
										lstproject, searchkeywords, 3, objuser.getUsernotify(), fromdate, todate,
										searchkeywords, searchkeywords, searchkeywords, 3, objuser.getUsernotify(),
										fromdate, todate, searchkeywords, pageable));
						lstorders.addAll(lslogilablimsorderdetailRepository
								.findByLsprojectmasterIsNullAndLssamplemasterInAndCreatedtimestampBetweenAndBatchidNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseAndLsuserMasterNotOrderByBatchcodeDesc(
										lstsample, fromdate, todate, searchkeywords, searchkeywords, searchkeywords,
										objuser, pageable));
						lstorders.addAll(lslogilablimsorderdetailRepository
								.findByLsprojectmasterIsNullAndLssamplemasterInAndCreatedtimestampBetweenAndKeywordContainingIgnoreCaseAndTestnameIsNullAndLsuserMasterNotOrderByBatchcodeDesc(
										lstsample, fromdate, todate, searchkeywords, objuser, pageable));

						count = count + lslogilablimsorderdetailRepository
								.countByLsprojectmasterInAndCreatedtimestampBetweenAndBatchidNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseOrLsprojectmasterInAndCreatedtimestampBetweenAndKeywordContainingIgnoreCaseAndTestnameIsNullOrFiletypeAndCreatedtimestampBetweenAndBatchidNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseOrFiletypeAndCreatedtimestampBetweenAndKeywordContainingIgnoreCaseAndTestnameIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndBatchidNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndKeywordContainingIgnoreCaseAndTestnameIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndBatchidNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndKeywordContainingIgnoreCaseAndTestnameIsNullOrViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndLsprojectmasterInAndBatchidNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseOrViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndLsprojectmasterInAndKeywordContainingIgnoreCaseAndTestnameIsNullOrViewoptionAndLsuserMasterInAndCreatedtimestampBetweenAndLsprojectmasterIsNullAndBatchidNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseOrViewoptionAndLsuserMasterInAndCreatedtimestampBetweenAndLsprojectmasterIsNullAndKeywordContainingIgnoreCaseAndTestnameIsNullOrderByBatchcodeDesc(
										lstproject, fromdate, todate, searchkeywords, searchkeywords, searchkeywords,
										lstproject, fromdate, todate, searchkeywords, 0, fromdate, todate,
										searchkeywords, searchkeywords, searchkeywords, 0, fromdate, todate,
										searchkeywords, 1, objuser, fromdate, todate, searchkeywords, searchkeywords,
										searchkeywords, 1, objuser, fromdate, todate, searchkeywords, 2, objuser,
										fromdate, todate, searchkeywords, searchkeywords, searchkeywords, 2, objuser,
										fromdate, todate, searchkeywords, 3, objuser, fromdate, todate, lstproject,
										searchkeywords, searchkeywords, searchkeywords, 3, objuser, fromdate, todate,
										lstproject, searchkeywords, 3, objuser.getUsernotify(), fromdate, todate,
										searchkeywords, searchkeywords, searchkeywords, 3, objuser.getUsernotify(),
										fromdate, todate, searchkeywords);

						count = count + lslogilablimsorderdetailRepository
								.countByLsprojectmasterIsNullAndLssamplemasterInAndCreatedtimestampBetweenAndBatchidNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseAndLsuserMasterNotOrderByBatchcodeDesc(
										lstsample, fromdate, todate, searchkeywords, searchkeywords, searchkeywords,
										objuser);
						count = count + lslogilablimsorderdetailRepository
								.countByLsprojectmasterIsNullAndLssamplemasterInAndCreatedtimestampBetweenAndKeywordContainingIgnoreCaseAndTestnameIsNullAndLsuserMasterNotOrderByBatchcodeDesc(
										lstsample, fromdate, todate, searchkeywords, objuser);

						lstorders.addAll(lslogilablimsorderdetailRepository
								.findByLsprojectmasterInAndCreatedtimestampBetweenAndBatchidNotContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameContainingIgnoreCaseOrLsprojectmasterInAndCreatedtimestampBetweenAndTestnameContainingIgnoreCaseAndKeywordIsNullOrFiletypeAndCreatedtimestampBetweenAndBatchidNotContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameContainingIgnoreCaseOrFiletypeAndCreatedtimestampBetweenAndTestnameContainingIgnoreCaseAndKeywordIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndBatchidNotContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameContainingIgnoreCaseOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndTestnameContainingIgnoreCaseAndKeywordIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndBatchidNotContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameContainingIgnoreCaseOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndTestnameContainingIgnoreCaseAndKeywordIsNullOrViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndLsprojectmasterInAndBatchidNotContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameContainingIgnoreCaseOrViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndLsprojectmasterInAndTestnameContainingIgnoreCaseAndKeywordIsNullOrViewoptionAndLsuserMasterInAndCreatedtimestampBetweenAndLsprojectmasterIsNullAndBatchidNotContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameContainingIgnoreCaseOrViewoptionAndLsuserMasterInAndCreatedtimestampBetweenAndLsprojectmasterIsNullAndTestnameContainingIgnoreCaseAndKeywordIsNullOrderByBatchcodeDesc(
										lstproject, fromdate, todate, searchkeywords, searchkeywords, searchkeywords,
										lstproject, fromdate, todate, searchkeywords, 0, fromdate, todate,
										searchkeywords, searchkeywords, searchkeywords, 0, fromdate, todate,
										searchkeywords, 1, objuser, fromdate, todate, searchkeywords, searchkeywords,
										searchkeywords, 1, objuser, fromdate, todate, searchkeywords, 2, objuser,
										fromdate, todate, searchkeywords, searchkeywords, searchkeywords, 2, objuser,
										fromdate, todate, searchkeywords, 3, objuser, fromdate, todate, lstproject,
										searchkeywords, searchkeywords, searchkeywords, 3, objuser, fromdate, todate,
										lstproject, searchkeywords, 3, objuser.getUsernotify(), fromdate, todate,
										searchkeywords, searchkeywords, searchkeywords, 3, objuser.getUsernotify(),
										fromdate, todate, searchkeywords, pageable));

						lstorders.addAll(lslogilablimsorderdetailRepository
								.findByLsprojectmasterIsNullAndLssamplemasterInAndCreatedtimestampBetweenAndBatchidNotContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameContainingIgnoreCaseAndLsuserMasterNotOrderByBatchcodeDesc(
										lstsample, fromdate, todate, searchkeywords, searchkeywords, searchkeywords,
										pageable, objuser));
						lstorders.addAll(lslogilablimsorderdetailRepository
								.findByLsprojectmasterIsNullAndLssamplemasterInAndCreatedtimestampBetweenAndTestnameContainingIgnoreCaseAndKeywordIsNullAndLsuserMasterNotOrderByBatchcodeDesc(
										lstsample, fromdate, todate, searchkeywords, pageable, objuser));

						count = count + lslogilablimsorderdetailRepository
								.countByLsprojectmasterInAndCreatedtimestampBetweenAndBatchidNotContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameContainingIgnoreCaseOrLsprojectmasterInAndCreatedtimestampBetweenAndTestnameContainingIgnoreCaseAndKeywordIsNullOrFiletypeAndCreatedtimestampBetweenAndBatchidNotContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameContainingIgnoreCaseOrFiletypeAndCreatedtimestampBetweenAndTestnameContainingIgnoreCaseAndKeywordIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndBatchidNotContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameContainingIgnoreCaseOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndTestnameContainingIgnoreCaseAndKeywordIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndBatchidNotContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameContainingIgnoreCaseOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndTestnameContainingIgnoreCaseAndKeywordIsNullOrViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndLsprojectmasterInAndBatchidNotContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameContainingIgnoreCaseOrViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndLsprojectmasterInAndTestnameContainingIgnoreCaseAndKeywordIsNullOrViewoptionAndLsuserMasterInAndCreatedtimestampBetweenAndLsprojectmasterIsNullAndBatchidNotContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameContainingIgnoreCaseOrViewoptionAndLsuserMasterInAndCreatedtimestampBetweenAndLsprojectmasterIsNullAndTestnameContainingIgnoreCaseAndKeywordIsNullOrderByBatchcodeDesc(
										lstproject, fromdate, todate, searchkeywords, searchkeywords, searchkeywords,
										lstproject, fromdate, todate, searchkeywords, 0, fromdate, todate,
										searchkeywords, searchkeywords, searchkeywords, 0, fromdate, todate,
										searchkeywords, 1, objuser, fromdate, todate, searchkeywords, searchkeywords,
										searchkeywords, 1, objuser, fromdate, todate, searchkeywords, 2, objuser,
										fromdate, todate, searchkeywords, searchkeywords, searchkeywords, 2, objuser,
										fromdate, todate, searchkeywords, 3, objuser, fromdate, todate, lstproject,
										searchkeywords, searchkeywords, searchkeywords, 3, objuser, fromdate, todate,
										lstproject, searchkeywords, 3, objuser.getUsernotify(), fromdate, todate,
										searchkeywords, searchkeywords, searchkeywords, 3, objuser.getUsernotify(),
										fromdate, todate, searchkeywords);

						count = count + lslogilablimsorderdetailRepository
								.countByLsprojectmasterIsNullAndLssamplemasterInAndCreatedtimestampBetweenAndBatchidNotContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameContainingIgnoreCaseAndLsuserMasterNotOrderByBatchcodeDesc(
										lstsample, fromdate, todate, searchkeywords, searchkeywords, searchkeywords,
										objuser);

						count = count + lslogilablimsorderdetailRepository
								.countByLsprojectmasterIsNullAndLssamplemasterInAndCreatedtimestampBetweenAndTestnameContainingIgnoreCaseAndKeywordIsNullAndLsuserMasterNotOrderByBatchcodeDesc(
										lstsample, fromdate, todate, searchkeywords, objuser);

						rtnobj.put("orders", lstorders);

					} else {
						lstordersprotocol = LSlogilabprotocoldetailRepository
								.findByLsprojectmasterInAndCreatedtimestampBetweenAndSitecodeAndProtoclordernameContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseOrLsprojectmasterInAndCreatedtimestampBetweenAndSitecodeAndProtoclordernameNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndProtoclordernameContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndProtoclordernameNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatebyAndCreatedtimestampBetweenAndProtoclordernameContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatebyAndCreatedtimestampBetweenAndProtoclordernameNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseOrLsprojectmasterInAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndProtoclordernameContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseOrLsprojectmasterInAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndProtoclordernameNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndCreatebyInAndProtoclordernameContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndCreatebyInAndProtoclordernameNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseOrderByProtocolordercodeDesc(
										lstproject, fromdate, todate, objuser.getLssitemaster().getSitecode(),
										searchkeywords, searchkeywords, lstproject, fromdate, todate,
										objuser.getLssitemaster().getSitecode(), searchkeywords, searchkeywords, 1,
										objuser.getLssitemaster().getSitecode(), fromdate, todate, searchkeywords,
										searchkeywords, 1, objuser.getLssitemaster().getSitecode(), fromdate, todate,
										searchkeywords, searchkeywords, 2, objuser.getLssitemaster().getSitecode(),
										objuser.getUsercode(), fromdate, todate, searchkeywords, searchkeywords, 2,
										objuser.getLssitemaster().getSitecode(), objuser.getUsercode(), fromdate,
										todate, searchkeywords, searchkeywords, lstproject, 3,
										objuser.getLssitemaster().getSitecode(), fromdate, todate, searchkeywords,
										searchkeywords, lstproject, 3, objuser.getLssitemaster().getSitecode(),
										fromdate, todate, searchkeywords, searchkeywords, 3,
										objuser.getLssitemaster().getSitecode(), fromdate, todate, userlist,
										searchkeywords, searchkeywords, 3, objuser.getLssitemaster().getSitecode(),
										fromdate, todate, userlist, searchkeywords, searchkeywords, pageable);

						count = LSlogilabprotocoldetailRepository
								.countByLsprojectmasterInAndCreatedtimestampBetweenAndSitecodeAndProtoclordernameContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseOrLsprojectmasterInAndCreatedtimestampBetweenAndSitecodeAndProtoclordernameNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndProtoclordernameContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndProtoclordernameNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatebyAndCreatedtimestampBetweenAndProtoclordernameContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatebyAndCreatedtimestampBetweenAndProtoclordernameNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseOrLsprojectmasterInAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndProtoclordernameContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseOrLsprojectmasterInAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndProtoclordernameNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndCreatebyInAndProtoclordernameContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndCreatebyInAndProtoclordernameNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseOrderByProtocolordercodeDesc(
										lstproject, fromdate, todate, objuser.getLssitemaster().getSitecode(),
										searchkeywords, searchkeywords, lstproject, fromdate, todate,
										objuser.getLssitemaster().getSitecode(), searchkeywords, searchkeywords, 1,
										objuser.getLssitemaster().getSitecode(), fromdate, todate, searchkeywords,
										searchkeywords, 1, objuser.getLssitemaster().getSitecode(), fromdate, todate,
										searchkeywords, searchkeywords, 2, objuser.getLssitemaster().getSitecode(),
										objuser.getUsercode(), fromdate, todate, searchkeywords, searchkeywords, 2,
										objuser.getLssitemaster().getSitecode(), objuser.getUsercode(), fromdate,
										todate, searchkeywords, searchkeywords, lstproject, 3,
										objuser.getLssitemaster().getSitecode(), fromdate, todate, searchkeywords,
										searchkeywords, lstproject, 3, objuser.getLssitemaster().getSitecode(),
										fromdate, todate, searchkeywords, searchkeywords, 3,
										objuser.getLssitemaster().getSitecode(), fromdate, todate, userlist,
										searchkeywords, searchkeywords, 3, objuser.getLssitemaster().getSitecode(),
										fromdate, todate, userlist, searchkeywords, searchkeywords);
						rtnobj.put("orders", lstordersprotocol);
					}
				}
			}
		} else {
			List<LSSheetOrderStructure> lstdir = lsSheetOrderStructureRepository
					.findBySitemasterAndViewoptionOrCreatedbyAndViewoptionOrderByDirectorycode(
							objuser.getLssitemaster(), 1, objuser, 2);
			List<Long> directorycode = lstdir.stream().map(LSSheetOrderStructure::getDirectorycode)
					.collect(Collectors.toList());
			if (objuser.getObjuser().getOrderselectiontype() == 1) {

				if (testcode == -1 && objuser.getLstprojectforfilter() == null) {
					if (objuser.getObjuser().getOrderfor() == 1) {
						lstorders = lslogilablimsorderdetailRepository
								.findByViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndBatchidContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndBatchidContainingIgnoreCaseAndKeywordIsNullAndTestnameIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndBatchidContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndBatchidContainingIgnoreCaseAndKeywordIsNullAndTestnameIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndBatchidContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndBatchidContainingIgnoreCaseAndKeywordIsNullAndTestnameIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndBatchidContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndBatchidContainingIgnoreCaseAndKeywordIsNullAndTestnameIsNullOrderByBatchcodeDesc(
										3, fromdate, todate, objuser, searchkeywords, searchkeywords, searchkeywords, 3,
										fromdate, todate, objuser, searchkeywords, directorycode, 1, fromdate, todate,
										searchkeywords, searchkeywords, searchkeywords, directorycode, 1, fromdate,
										todate, searchkeywords, directorycode, 2, fromdate, todate, objuser,
										searchkeywords, searchkeywords, searchkeywords, directorycode, 2, fromdate,
										todate, objuser, searchkeywords, directorycode, 3, fromdate, todate, objuser,
										searchkeywords, searchkeywords, searchkeywords, directorycode, 3, fromdate,
										todate, objuser, searchkeywords, pageable);
						lstorders.addAll(lslogilablimsorderdetailRepository
								.findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndBatchidContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseAndLsuserMasterNotOrderByBatchcodeDesc(
										lstsample, 1, fromdate, todate, searchkeywords, searchkeywords, searchkeywords,
										objuser, pageable));
						lstorders.addAll(lslogilablimsorderdetailRepository
								.findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndBatchidContainingIgnoreCaseAndKeywordIsNullAndTestnameIsNullAndLsuserMasterNotOrderByBatchcodeDesc(
										lstsample, 1, fromdate, todate, searchkeywords, objuser, pageable));
						lstorders.addAll(lslogilablimsorderdetailRepository
								.findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndBatchidContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseOrderByBatchcodeDesc(
										lstsample, 2, fromdate, todate, objuser, searchkeywords, searchkeywords,
										searchkeywords, pageable));
						lstorders.addAll(lslogilablimsorderdetailRepository
								.findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndBatchidContainingIgnoreCaseAndKeywordIsNullAndTestnameIsNullOrderByBatchcodeDesc(
										lstsample, 2, fromdate, todate, objuser, searchkeywords, pageable));
						count = count + lslogilablimsorderdetailRepository
								.countByViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndBatchidContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndBatchidContainingIgnoreCaseAndKeywordIsNullAndTestnameIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndBatchidContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndBatchidContainingIgnoreCaseAndKeywordIsNullAndTestnameIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndBatchidContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndBatchidContainingIgnoreCaseAndKeywordIsNullAndTestnameIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndBatchidContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndBatchidContainingIgnoreCaseAndKeywordIsNullAndTestnameIsNullOrderByBatchcodeDesc(
										3, fromdate, todate, objuser, searchkeywords, searchkeywords, searchkeywords, 3,
										fromdate, todate, objuser, searchkeywords, directorycode, 1, fromdate, todate,
										searchkeywords, searchkeywords, searchkeywords, directorycode, 1, fromdate,
										todate, searchkeywords, directorycode, 2, fromdate, todate, objuser,
										searchkeywords, searchkeywords, searchkeywords, directorycode, 2, fromdate,
										todate, objuser, searchkeywords, directorycode, 3, fromdate, todate, objuser,
										searchkeywords, searchkeywords, searchkeywords, directorycode, 3, fromdate,
										todate, objuser, searchkeywords);
						count = count + lslogilablimsorderdetailRepository
								.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndBatchidContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseAndLsuserMasterNotOrderByBatchcodeDesc(
										lstsample, 1, fromdate, todate, searchkeywords, searchkeywords, searchkeywords,
										objuser);
						count = count + lslogilablimsorderdetailRepository
								.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndBatchidContainingIgnoreCaseAndKeywordIsNullAndTestnameIsNullAndLsuserMasterNotOrderByBatchcodeDesc(
										lstsample, 1, fromdate, todate, searchkeywords, objuser);

						count = count + lslogilablimsorderdetailRepository
								.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndBatchidContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseOrderByBatchcodeDesc(
										lstsample, 2, fromdate, todate, objuser, searchkeywords, searchkeywords,
										searchkeywords);

						count = count + lslogilablimsorderdetailRepository
								.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndBatchidContainingIgnoreCaseAndKeywordIsNullAndTestnameIsNullOrderByBatchcodeDesc(
										lstsample, 2, fromdate, todate, objuser, searchkeywords);
//						lstorders.addAll(lslogilablimsorderdetailRepository
//								.findByViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndBatchidNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndKeywordContainingIgnoreCaseAndTestnameIsNullLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndBatchidNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndKeywordContainingIgnoreCaseAndTestnameIsNullLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndBatchidNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndKeywordContainingIgnoreCaseAndTestnameIsNullLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndBatchidNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndKeywordContainingIgnoreCaseAndTestnameIsNullderByBatchcodeDesc(
//										3, fromdate, todate, objuser, searchkeywords, searchkeywords, searchkeywords, 3,
//										fromdate, todate, objuser, searchkeywords, directorycode, 1, fromdate, todate,
//										searchkeywords, searchkeywords, searchkeywords, directorycode, 1, fromdate,
//										todate, searchkeywords, directorycode, 2, fromdate, todate, objuser,
//										searchkeywords, searchkeywords, searchkeywords, directorycode, 2, fromdate,
//										todate, objuser, searchkeywords, directorycode, 3, fromdate, todate, objuser,
//										searchkeywords, searchkeywords, searchkeywords, directorycode, 3, fromdate,
//										todate, objuser, searchkeywords, pageable));
						lstorders.addAll(lslogilablimsorderdetailRepository
								.findByViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndBatchidNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndKeywordContainingIgnoreCaseAndTestnameIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndBatchidNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndKeywordContainingIgnoreCaseAndTestnameIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndBatchidNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndKeywordContainingIgnoreCaseAndTestnameIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndBatchidNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndKeywordContainingIgnoreCaseAndTestnameIsNullOrderByBatchcodeDesc(
                                           3, fromdate, todate, objuser, searchkeywords,
										searchkeywords, searchkeywords, 3, fromdate, todate, objuser, searchkeywords,
										directorycode, 1, fromdate, todate, searchkeywords, searchkeywords,
										searchkeywords, directorycode, 1, fromdate, todate, searchkeywords,
										directorycode, 2, fromdate, todate, objuser, searchkeywords, searchkeywords,
										searchkeywords, directorycode, 2, fromdate, todate, objuser, searchkeywords,
										directorycode, 3, fromdate, todate, objuser, searchkeywords, searchkeywords,
										searchkeywords, directorycode, 3, fromdate, todate, objuser, searchkeywords,
										pageable));
						lstorders.addAll(lslogilablimsorderdetailRepository
								.findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndBatchidNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseAndLsuserMasterNotOrderByBatchcodeDesc(
										lstsample, 1, fromdate, todate, searchkeywords, searchkeywords, searchkeywords,objuser,
										pageable));
						lstorders.addAll(lslogilablimsorderdetailRepository
								.findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndKeywordContainingIgnoreCaseAndTestnameIsNullAndLsuserMasterNotOrderByBatchcodeDesc(
										lstsample, 1, fromdate, todate, searchkeywords,objuser, pageable));
						lstorders.addAll(lslogilablimsorderdetailRepository
								.findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndBatchidNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseOrderByBatchcodeDesc(
										lstsample, 2, fromdate, todate, objuser, searchkeywords, searchkeywords,
										searchkeywords, pageable));
						lstorders.addAll(lslogilablimsorderdetailRepository
								.findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndKeywordContainingIgnoreCaseAndTestnameIsNullOrderByBatchcodeDesc(
										lstsample, 2, fromdate, todate, objuser, searchkeywords, pageable));

						count = count + lslogilablimsorderdetailRepository
								.countByViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndBatchidNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndKeywordContainingIgnoreCaseAndTestnameIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndBatchidNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndKeywordContainingIgnoreCaseAndTestnameIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndBatchidNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndKeywordContainingIgnoreCaseAndTestnameIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndBatchidNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndKeywordContainingIgnoreCaseAndTestnameIsNullOrderByBatchcodeDesc(
                                        3, fromdate, todate, objuser, searchkeywords,
										searchkeywords, searchkeywords, 3, fromdate, todate, objuser, searchkeywords,
										directorycode, 1, fromdate, todate, searchkeywords, searchkeywords,
										searchkeywords, directorycode, 1, fromdate, todate, searchkeywords,
										directorycode, 2, fromdate, todate, objuser, searchkeywords, searchkeywords,
										searchkeywords, directorycode, 2, fromdate, todate, objuser, searchkeywords,
										directorycode, 3, fromdate, todate, objuser, searchkeywords, searchkeywords,
										searchkeywords, directorycode, 3, fromdate, todate, objuser, searchkeywords
										);

						count = count + lslogilablimsorderdetailRepository
								.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndBatchidNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseAndLsuserMasterNotOrderByBatchcodeDesc(
										lstsample, 1, fromdate, todate, searchkeywords, searchkeywords, searchkeywords,objuser);
						count = count + lslogilablimsorderdetailRepository
								.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndKeywordContainingIgnoreCaseAndTestnameIsNullAndLsuserMasterNotOrderByBatchcodeDesc(
										lstsample, 1, fromdate, todate, searchkeywords,objuser);

						count = count + lslogilablimsorderdetailRepository
								.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndKeywordContainingIgnoreCaseAndTestnameIsNullOrderByBatchcodeDesc(
										lstsample, 2, fromdate, todate, objuser, searchkeywords);
						
						
						lstorders.addAll(lslogilablimsorderdetailRepository
								.findByViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndBatchidNotContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameContainingIgnoreCaseOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTestnameContainingIgnoreCaseAndKeywordIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndBatchidNotContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameContainingIgnoreCaseOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndTestnameContainingIgnoreCaseAndKeywordIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndBatchidNotContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameContainingIgnoreCaseOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTestnameContainingIgnoreCaseAndKeywordIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndBatchidNotContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameContainingIgnoreCaseOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTestnameContainingIgnoreCaseAndKeywordIsNullOrderByBatchcodeDesc(
                                         3, fromdate, todate, objuser, searchkeywords,searchkeywords, searchkeywords,
                                          3, fromdate, todate, objuser, searchkeywords,
										directorycode, 1, fromdate, todate, searchkeywords, searchkeywords,searchkeywords, 
                                        directorycode, 1, fromdate, todate, searchkeywords,
										directorycode, 2, fromdate, todate, objuser, searchkeywords, searchkeywords,searchkeywords, 
                                        directorycode, 2, fromdate, todate, objuser, searchkeywords,
										directorycode, 3, fromdate, todate, objuser, searchkeywords, searchkeywords,searchkeywords,
                                         directorycode, 3, fromdate, todate, objuser, searchkeywords,
										pageable));
                                lstorders.addAll(lslogilablimsorderdetailRepository
								.findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndBatchidNotContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameContainingIgnoreCaseAndLsuserMasterNotOrderByBatchcodeDesc(
                                lstsample, 1, fromdate, todate, searchkeywords, searchkeywords, searchkeywords,objuser,pageable));
                                lstorders.addAll(lslogilablimsorderdetailRepository
								.findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndTestnameContainingIgnoreCaseAndKeywordIsNullAndLsuserMasterNotOrderByBatchcodeDesc(
                                lstsample, 1, fromdate, todate, searchkeywords,objuser,pageable));
                                lstorders.addAll(lslogilablimsorderdetailRepository
								.findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndBatchidNotContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameContainingIgnoreCaseOrderByBatchcodeDesc(
                                lstsample, 2, fromdate, todate,objuser, searchkeywords, searchkeywords, searchkeywords, pageable));
                                lstorders.addAll(lslogilablimsorderdetailRepository
								.findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTestnameContainingIgnoreCaseAndKeywordIsNullOrderByBatchcodeDesc(
                                lstsample, 2, fromdate,todate, objuser, searchkeywords, pageable));

                        count = count + lslogilablimsorderdetailRepository
						.countByViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndBatchidNotContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameContainingIgnoreCaseOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTestnameContainingIgnoreCaseAndKeywordIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndBatchidNotContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameContainingIgnoreCaseOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndTestnameContainingIgnoreCaseAndKeywordIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndBatchidNotContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameContainingIgnoreCaseOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTestnameContainingIgnoreCaseAndKeywordIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndBatchidNotContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameContainingIgnoreCaseOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTestnameContainingIgnoreCaseAndKeywordIsNullOrderByBatchcodeDesc(
                                 3, fromdate, todate, objuser, searchkeywords,searchkeywords, searchkeywords,
                                  3, fromdate, todate, objuser, searchkeywords,
								directorycode, 1, fromdate, todate, searchkeywords, searchkeywords,searchkeywords, 
                                directorycode, 1, fromdate, todate, searchkeywords,
								directorycode, 2, fromdate, todate, objuser, searchkeywords, searchkeywords,searchkeywords, 
                                directorycode, 2, fromdate, todate, objuser, searchkeywords,
								directorycode, 3, fromdate, todate, objuser, searchkeywords, searchkeywords,searchkeywords,
                                 directorycode, 3, fromdate, todate, objuser, searchkeywords
								);
                        count = count + lslogilablimsorderdetailRepository
						.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndBatchidNotContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameContainingIgnoreCaseAndLsuserMasterNotOrderByBatchcodeDesc(
                        lstsample, 1, fromdate, todate, searchkeywords, searchkeywords, searchkeywords,objuser);
                        count = count + lslogilablimsorderdetailRepository
						.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndTestnameContainingIgnoreCaseAndKeywordIsNullAndLsuserMasterNotOrderByBatchcodeDesc(
                        lstsample, 1, fromdate, todate, searchkeywords,objuser);
                        count = count + lslogilablimsorderdetailRepository
						.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndBatchidNotContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameContainingIgnoreCaseOrderByBatchcodeDesc(
                        lstsample, 2, fromdate, todate,objuser, searchkeywords, searchkeywords, searchkeywords);
                        count = count + lslogilablimsorderdetailRepository
						.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTestnameContainingIgnoreCaseAndKeywordIsNullOrderByBatchcodeDesc(
                        lstsample, 2, fromdate,todate, objuser, searchkeywords);

						rtnobj.put("orders", lstorders);
					} else {
						lstordersprotocol = LSlogilabprotocoldetailRepository
								.findByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndProtoclordernameContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndProtoclordernameNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndProtoclordernameContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndProtoclordernameNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndProtoclordernameContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndProtoclordernameNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseOrderByProtocolordercodeDesc(
										1, objuser.getLssitemaster().getSitecode(), fromdate, todate, searchkeywords,
										searchkeywords, 1, objuser.getLssitemaster().getSitecode(), fromdate, todate,
										searchkeywords, searchkeywords, 2, objuser.getUsercode(), fromdate, todate,
										searchkeywords, searchkeywords, 2, objuser.getUsercode(), fromdate, todate,
										searchkeywords, searchkeywords, 3, objuser.getUsercode(), fromdate, todate,
										searchkeywords, searchkeywords, 3, objuser.getUsercode(), fromdate, todate,
										searchkeywords, searchkeywords, pageable);

						count = LSlogilabprotocoldetailRepository
								.countByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndProtoclordernameContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndProtoclordernameNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndProtoclordernameContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndProtoclordernameNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndProtoclordernameContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndProtoclordernameNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseOrderByProtocolordercodeDesc(
										1, objuser.getLssitemaster().getSitecode(), fromdate, todate, searchkeywords,
										searchkeywords, 1, objuser.getLssitemaster().getSitecode(), fromdate, todate,
										searchkeywords, searchkeywords, 2, objuser.getUsercode(), fromdate, todate,
										searchkeywords, searchkeywords, 2, objuser.getUsercode(), fromdate, todate,
										searchkeywords, searchkeywords, 3, objuser.getUsercode(), fromdate, todate,
										searchkeywords, searchkeywords, 3, objuser.getUsercode(), fromdate, todate,
										searchkeywords, searchkeywords);
						rtnobj.put("orders", lstordersprotocol);
					}
				}
			}
		}
		rtnobj.put("count", count);
		return rtnobj;
	}

}
