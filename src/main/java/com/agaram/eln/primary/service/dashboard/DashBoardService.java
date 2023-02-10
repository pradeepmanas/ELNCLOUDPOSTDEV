package com.agaram.eln.primary.service.dashboard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.agaram.eln.primary.fetchmodel.getmasters.Repositorymaster;
import com.agaram.eln.primary.fetchmodel.getorders.Logilabordermaster;
import com.agaram.eln.primary.fetchmodel.getorders.Logilaborders;
import com.agaram.eln.primary.fetchmodel.getorders.Logilabprotocolorders;
import com.agaram.eln.primary.fetchmodel.gettemplate.Protocoltemplateget;
import com.agaram.eln.primary.fetchmodel.gettemplate.Sheettemplateget;
import com.agaram.eln.primary.model.cfr.LSactivity;
import com.agaram.eln.primary.model.instrumentDetails.LSlogilablimsorderdetail;
import com.agaram.eln.primary.model.protocols.LSprotocolmaster;
import com.agaram.eln.primary.model.sheetManipulation.LSparsedparameters;
import com.agaram.eln.primary.model.sheetManipulation.LSsamplefile;
import com.agaram.eln.primary.model.sheetManipulation.LSworkflow;
import com.agaram.eln.primary.model.sheetManipulation.LSworkflowgroupmapping;
import com.agaram.eln.primary.model.usermanagement.LSMultiusergroup;
import com.agaram.eln.primary.model.usermanagement.LSprojectmaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.model.usermanagement.LSusersteam;
import com.agaram.eln.primary.model.usermanagement.LSuserteammapping;
import com.agaram.eln.primary.repository.cfr.LSactivityRepository;
import com.agaram.eln.primary.repository.cfr.LScfttransactionRepository;
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
		List<LSsamplefile> lssamplefile = lssamplefileRepository.findByprocessed(1);
		List<LSprojectmaster> lstproject = objuser.getLstproject();
		if (objuser.getObjuser().getOrderfor() != 1) {
			if (lstproject != null && lstproject.size() > 0) {
				mapOrders.put("orders",
						LSlogilabprotocoldetailRepository.countBySitecodeAndCreatedtimestampBetweenAndLsprojectmasterIn(
								objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject));
				mapOrders.put("pendingorder", LSlogilabprotocoldetailRepository
						.countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndOrdercancellIsNull(
								"N", objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject));

				mapOrders.put("completedorder",
						LSlogilabprotocoldetailRepository
								.countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterIn("R",
										objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject));

				mapOrders.put("rejectedorder",
						LSlogilabprotocoldetailRepository
								.countByRejectedAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterIn(1,
										objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject));

				mapOrders.put("canceledorder",
						LSlogilabprotocoldetailRepository
								.countByOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterIn(1,
										objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject));

				mapOrders.put("onproces", 0);
			} else {
				mapOrders.put("orders", 0);
				mapOrders.put("pendingorder", 0);
				mapOrders.put("completedorder", 0);
				mapOrders.put("onproces", 0);
				mapOrders.put("rejectedorder", 0);
				mapOrders.put("canceledorder", 0);
			}
		} else if (objuser.getUsername().equals("Administrator") && objuser.getObjuser().getOrderfor() == 1) {
			mapOrders.put("orders",
					lslogilablimsorderdetailRepository.countByCreatedtimestampBetween(fromdate, todate));
			mapOrders.put("pendingorder", lslogilablimsorderdetailRepository
					.countByOrderflagAndOrdercancellIsNullAndCreatedtimestampBetween(
							"N", fromdate, todate));
			mapOrders.put("completedorder", lslogilablimsorderdetailRepository
					.countByOrderflagAndCompletedtimestampBetweenAndApprovelstatusNot("R", fromdate, todate, 3));
			
			mapOrders.put("rejectedorder", lslogilablimsorderdetailRepository
					.countByApprovelstatusAndCreatedtimestampBetween(3, fromdate, todate));
			mapOrders.put("canceledorder", lslogilablimsorderdetailRepository
					.countByOrdercancellAndCreatedtimestampBetween(1, fromdate, todate));
			mapOrders.put("onproces", lslogilablimsorderdetailRepository
					.countByOrderflagAndLssamplefileInAndCreatedtimestampBetween("N", lssamplefile, fromdate, todate));

		} else {

			long lstUserorder = 0;
			if (lstproject != null && lstproject.size() > 0) {
				lstUserorder = lslogilablimsorderdetailRepository
						.countByLsprojectmasterInAndCreatedtimestampBetween(lstproject, fromdate, todate);
				
				lstUserorder = lstUserorder +lslogilablimsorderdetailRepository
						.countByFiletypeAndCreatedtimestampBetween(0, fromdate, todate);

			}

			long lstlimscompleted = 0;
			if (lstproject != null && lstproject.size() > 0) {
				lstlimscompleted = lslogilablimsorderdetailRepository
						.countByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusNot("R", lstproject, fromdate,
								todate, 3);
				
				lstlimscompleted = lstlimscompleted + lslogilablimsorderdetailRepository
						.countByOrderflagAndFiletypeAndCreatedtimestampBetween("R", 0, fromdate,
								todate);
			}

			long lstordersinprogress = 0;
			if (lstproject != null && lssamplefile != null && lstproject.size() > 0 && lssamplefile.size() > 0) {

				lstordersinprogress = lslogilablimsorderdetailRepository
						.countByOrderflagAndLsprojectmasterInAndApprovelstatusAndApprovedAndCreatedtimestampBetweenOrderByBatchcodeDesc(
								"N", lstproject, 1, 1, fromdate, todate);

			}

			long lstpending = 0;
			if (lstproject != null && lstproject.size() > 0) {
				lstpending = lslogilablimsorderdetailRepository
						.countByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndOrdercancellIsNull(
								"N", lstproject, fromdate, todate);
				
				lstpending = lstpending + lslogilablimsorderdetailRepository
						.countByOrderflagAndFiletypeAndCreatedtimestampBetween(
								"N", 0, fromdate, todate);
			}

			long lstRejected = 0;
			if (lstproject != null && lstproject.size() > 0) {
				lstRejected = lslogilablimsorderdetailRepository
						.countByApprovelstatusAndLsprojectmasterInAndCreatedtimestampBetween(3, lstproject, fromdate,
								todate);
			}

			long lstCancelled = 0;
			if (lstproject != null && lstproject.size() > 0) {
				lstCancelled = lslogilablimsorderdetailRepository
						.countByOrdercancellAndLsprojectmasterInAndCreatedtimestampBetween(1, lstproject, fromdate,
								todate);
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

		List<LSsamplefile> lssamplefile = lssamplefileRepository.findByprocessed(1);

		if (objuser.getUsername().equals("Administrator")) {

			if (objuser.getObjuser().getOrderselectiontype() == 1) {
				mapOrders.put("orderlst", lslogilablimsorderdetailRepository
						.findByCreatedtimestampBetweenOrderByBatchcodeDesc(fromdate, todate));
			} else if (objuser.getObjuser().getOrderselectiontype() == 2) {
				mapOrders.put("orderlst", lslogilablimsorderdetailRepository
						.findByOrderflagAndApprovelstatusNotAndCreatedtimestampBetweenAndOrdercancellIsNullOrderByBatchcodeDesc(
								"R", 3, fromdate, todate));
			} else if (objuser.getObjuser().getOrderselectiontype() == 3) {
				mapOrders.put("orderlst", lslogilablimsorderdetailRepository
						.findByOrderflagAndCreatedtimestampBetweenAndApprovelstatusNotOrApprovelstatusIsNullAndOrdercancellIsNullOrderByBatchcodeDesc(
								"N", fromdate, todate, 3));
			} else if (objuser.getObjuser().getOrderselectiontype() == 4) {
				mapOrders.put("orderlst",
						lslogilablimsorderdetailRepository
								.findByOrderflagAndLssamplefileInAndCreatedtimestampBetweenOrderByBatchcodeDesc("N",
										lssamplefile, fromdate, todate));
			} else if (objuser.getObjuser().getOrderselectiontype() == 5) {
				mapOrders.put("orderlst", lslogilablimsorderdetailRepository
						.findByCreatedtimestampBetweenAndApprovelstatusOrderByBatchcodeDesc(fromdate, todate, 3));
			} else if (objuser.getObjuser().getOrderselectiontype() == 6) {
				mapOrders.put("orderlst", lslogilablimsorderdetailRepository
						.findByCreatedtimestampBetweenAndOrdercancellOrderByBatchcodeDesc(fromdate, todate, 1));
			}

		} else {

			List<LSprojectmaster> lstproject = objuser.getLstproject();
			List<Logilabordermaster> lstorders = new ArrayList<Logilabordermaster>();

			if (lstproject != null) {
				List<LSworkflow> lstworkflow = objuser.getLstworkflow();

				if (objuser.getObjuser().getOrderselectiontype() == 1) {
					lstorders = lslogilablimsorderdetailRepository
							.findByLsprojectmasterInAndCreatedtimestampBetween(lstproject, fromdate, todate);
					
					lstorders.addAll(lslogilablimsorderdetailRepository.findByFiletypeAndCreatedtimestampBetween(0, fromdate, todate));

				} else if (objuser.getObjuser().getOrderselectiontype() == 2) {
					lstorders = lslogilablimsorderdetailRepository
							.findByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusNotOrderByBatchcodeDesc("R",
									lstproject, fromdate, todate,3);
					
					lstorders.addAll(lslogilablimsorderdetailRepository.findByOrderflagAndFiletypeAndCreatedtimestampBetween("R", 0, fromdate, todate));

				} else if (objuser.getObjuser().getOrderselectiontype() == 3) {
					lstorders = lslogilablimsorderdetailRepository
							.findByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndOrdercancellIsNullOrderByBatchcodeDesc("N",
									lstproject, fromdate, todate);
					lstorders.addAll(lslogilablimsorderdetailRepository.findByOrderflagAndFiletypeAndCreatedtimestampBetween("N", 0, fromdate, todate));

				} else if (objuser.getObjuser().getOrderselectiontype() == 4) {

					lstorders = lslogilablimsorderdetailRepository
							.findByOrderflagAndLssamplefileInAndCreatedtimestampBetween("N", lssamplefile, fromdate,
									todate);

				} else if (objuser.getObjuser().getOrderselectiontype() == 5) {
					lstorders = lslogilablimsorderdetailRepository
							.findByApprovelstatusAndLsprojectmasterInAndCreatedtimestampBetween(3, lstproject, fromdate,
									todate);
				} else if (objuser.getObjuser().getOrderselectiontype() == 6) {
					lstorders = lslogilablimsorderdetailRepository
							.findByOrdercancellAndLsprojectmasterInAndCreatedtimestampBetween(1, lstproject, fromdate,
									todate);
				}

				lstorders.forEach(objorder -> objorder.setLstworkflow(lstworkflow));
			}
			Collections.sort(lstorders, Collections.reverseOrder());
			mapOrders.put("orderlst", lstorders);
		}

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

		List<Logilabprotocolorders> lstorders = new ArrayList<Logilabprotocolorders>();
		List<LSprojectmaster> lstproject = objuser.getLstproject();
		if (lstproject != null) {
			if (objuser.getObjuser().getOrderselectiontype() == 1) {
				lstorders = LSlogilabprotocoldetailRepository
						.findBySitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterIn(
								objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject);

			} else if (objuser.getObjuser().getOrderselectiontype() == 2) {
				lstorders = LSlogilabprotocoldetailRepository
						.findByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterIn(
								"R", objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject);

			} else if (objuser.getObjuser().getOrderselectiontype() == 3) {
				lstorders = LSlogilabprotocoldetailRepository
						.findByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterIn(
								"N", objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject);
			} else if (objuser.getObjuser().getOrderselectiontype() == 5) {
				lstorders = LSlogilabprotocoldetailRepository
						.findByRejectedAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterIn(1,
								objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject);
			} else if (objuser.getObjuser().getOrderselectiontype() == 6) {
				lstorders = LSlogilabprotocoldetailRepository
						.findByOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterIn(1,
								objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject);
			}
		}

		lstorders.forEach(objorderDetail -> objorderDetail.setLstworkflow(objuser.getLstworkflow()));
		Collections.sort(lstorders, Collections.reverseOrder());
		mapOrders.put("orderlst", lstorders);

		return mapOrders;
	}

	public Map<String, Object> Getdashboardsheets(LSuserMaster objuser) {
		Date fromdate = objuser.getObjuser().getFromdate();
		Date todate = objuser.getObjuser().getTodate();
		Map<String, Object> mapSheets = new HashMap<String, Object>();
		List<LSuserMaster> lstteamuser = objuser.getObjuser().getTeamusers();
		List<Sheettemplateget> lstfile=new ArrayList<>();
		if (objuser.getUsername().equals("Administrator")) {
			lstfile= lsfileRepository
					.findByCreatedateBetweenAndFilecodeGreaterThanOrderByFilecodeDesc(fromdate, todate, 1);
			lstfile.forEach(
					objFile -> objFile.setVersioncout(lsfileversionRepository.countByFilecode(objFile.getFilecode())));
			mapSheets.put("Sheets",lstfile);
		} else {
//			List<Sheettemplateget> lstsheets = new ArrayList<Sheettemplateget>();

			if (lstteamuser != null && lstteamuser.size() > 0) {
				lstteamuser.add(objuser);
				lstfile = lsfileRepository
						.findByFilecodeGreaterThanAndLssitemasterAndCreatedateBetweenAndViewoptionOrFilecodeGreaterThanAndCreatebyAndCreatedateBetweenAndViewoptionOrFilecodeGreaterThanAndCreatebyInAndCreatedateBetweenAndViewoptionOrderByFilecodeDesc(
								1, objuser.getLssitemaster(), fromdate, todate, 1, 1, objuser, fromdate, todate, 2, 1,
								lstteamuser, fromdate, todate, 3);

			} else {
				lstfile = lsfileRepository
						.findByFilecodeGreaterThanAndLssitemasterAndCreatedateBetweenAndViewoptionOrFilecodeGreaterThanAndCreatebyAndCreatedateBetweenAndViewoptionOrderByFilecodeDesc(
								1, objuser.getLssitemaster(), fromdate, todate, 1, 1, objuser, fromdate, todate, 2);

			}
			lstfile.forEach(
					objFile -> objFile.setVersioncout(lsfileversionRepository.countByFilecode(objFile.getFilecode())));
			mapSheets.put("Sheets", lstfile);
		}

		return mapSheets;
	}
	
	public Map<String, Object> getCountFromSheetTemplate(LSuserMaster objuser) {
		Date fromdate = objuser.getObjuser().getFromdate();
		Date todate = objuser.getObjuser().getTodate();
		Map<String, Object> mapSheets = new HashMap<String, Object>();
		List<LSuserMaster> lstteamuser = objuser.getObjuser().getTeamusers();
		
		if (objuser.getUsername().equals("Administrator")) {
			mapSheets.put("rejectedTemplate", lsfileRepository
					.countByCreatedateBetweenAndFilecodeGreaterThanAndRejectedOrderByFilecodeDesc(fromdate, todate, 1,1));
			mapSheets.put("approvedTemplate", lsfileRepository
					.countByCreatedateBetweenAndFilecodeGreaterThanAndApprovedAndRejectedNotOrderByFilecodeDesc(fromdate, todate, 1,1,1));
			mapSheets.put("createdTemplate", lsfileRepository
					.countByCreatedateBetweenAndFilecodeGreaterThanAndApprovedNotAndRejectedNotOrderByFilecodeDesc(fromdate, todate, 1,1,1));
		} else {
			if (lstteamuser != null && lstteamuser.size() > 0) {
				lstteamuser.add(objuser);
				mapSheets.put("rejectedTemplate",lsfileRepository
						.countByFilecodeGreaterThanAndLssitemasterAndCreatedateBetweenAndViewoptionAndRejectedOrFilecodeGreaterThanAndCreatebyAndCreatedateBetweenAndViewoptionAndRejectedOrFilecodeGreaterThanAndCreatebyInAndCreatedateBetweenAndViewoptionAndRejectedOrderByFilecodeDesc(
								1, objuser.getLssitemaster(), fromdate, todate, 1, 1
								,1, objuser, fromdate, todate, 2, 1,
								1,lstteamuser, fromdate, todate, 3,1));
				mapSheets.put("approvedTemplate",lsfileRepository
						.countByFilecodeGreaterThanAndLssitemasterAndCreatedateBetweenAndViewoptionAndApprovedAndRejectedNotOrFilecodeGreaterThanAndCreatebyAndCreatedateBetweenAndViewoptionAndApprovedAndRejectedNotOrFilecodeGreaterThanAndCreatebyInAndCreatedateBetweenAndViewoptionAndApprovedAndRejectedNotOrderByFilecodeDesc(
								1, objuser.getLssitemaster(), fromdate, todate, 1, 1,1
								,1, objuser, fromdate, todate, 2, 1,1,
								1,lstteamuser, fromdate, todate, 3,1,1));
				mapSheets.put("createdTemplate",lsfileRepository
						.countByFilecodeGreaterThanAndLssitemasterAndCreatedateBetweenAndViewoptionAndApprovedNotAndRejectedNotOrFilecodeGreaterThanAndCreatebyAndCreatedateBetweenAndViewoptionAndApprovedNotAndRejectedNotOrFilecodeGreaterThanAndCreatebyInAndCreatedateBetweenAndViewoptionAndApprovedNotAndRejectedNotOrderByFilecodeDesc(
								1, objuser.getLssitemaster(), fromdate, todate, 1, 1,1
								,1, objuser, fromdate, todate, 2, 1,1,
								1,lstteamuser, fromdate, todate, 3,1,1));

			} else {
				mapSheets.put("rejectedTemplate",lsfileRepository
						.findByFilecodeGreaterThanAndLssitemasterAndCreatedateBetweenAndViewoptionAndRejectedOrFilecodeGreaterThanAndCreatebyAndCreatedateBetweenAndViewoptionAndRejectedOrderByFilecodeDesc(
								1, objuser.getLssitemaster(), fromdate, todate, 1,1,
								1, objuser, fromdate, todate, 2,1));
				mapSheets.put("rejectedTemplate",lsfileRepository
						.findByFilecodeGreaterThanAndLssitemasterAndCreatedateBetweenAndViewoptionAndApprovedAndRejectedNotOrFilecodeGreaterThanAndCreatebyAndCreatedateBetweenAndViewoptionAndApprovedAndRejectedNotOrderByFilecodeDesc(
								1, objuser.getLssitemaster(), fromdate, todate, 1,1,1,
								1, objuser, fromdate, todate, 2,1,1));
				mapSheets.put("rejectedTemplate",lsfileRepository
						.findByFilecodeGreaterThanAndLssitemasterAndCreatedateBetweenAndViewoptionAndApprovedNotAndRejectedNotOrFilecodeGreaterThanAndCreatebyAndCreatedateBetweenAndViewoptionAndApprovedNotAndRejectedNotOrderByFilecodeDesc(
								1, objuser.getLssitemaster(), fromdate, todate, 1,1,1,
								1, objuser, fromdate, todate, 2,1,1));
			}
		}
		return mapSheets;
	}

	public Map<String, Object> Getdashboardprotocoltemplate(LSuserMaster objuser) {
		Date fromdate = objuser.getObjuser().getFromdate();
		Date todate = objuser.getObjuser().getTodate();
		Map<String, Object> mapSheets = new HashMap<String, Object>();
		List<LSuserMaster> lstteamuser = objuser.getObjuser().getTeamusers();
		if (objuser.getUsername().equals("Administrator")) {
			mapSheets.put("Sheets", LSProtocolMasterRepository.findByStatusAndLssitemasterAndCreatedateBetween(1,
					objuser.getLssitemaster().getSitecode(), fromdate, todate));
		} else {

			List<Protocoltemplateget> lstprotocolmaster = new ArrayList<>();
			if (lstteamuser != null && lstteamuser.size() > 0) {
				lstteamuser.add(objuser);
				List<Integer> usercodelist = lstteamuser.stream().map(LSuserMaster::getUsercode)
						.collect(Collectors.toList());
				lstprotocolmaster = LSProtocolMasterRepository
						.findByLssitemasterAndStatusAndCreatedateBetweenAndViewoptionOrCreatedbyAndStatusAndCreatedateBetweenAndViewoptionOrCreatedbyInAndStatusAndCreatedateBetweenAndViewoptionOrderByProtocolmastercodeDesc(
								objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, 1, objuser.getUsercode(),
								1, fromdate, todate, 2, usercodelist, 1, fromdate, todate, 3);

			} else {
				lstprotocolmaster = LSProtocolMasterRepository
						.findByLssitemasterAndStatusAndCreatedateBetweenAndViewoptionOrCreatedbyAndStatusAndCreatedateBetweenAndViewoptionOrderByProtocolmastercodeDesc(
								objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, 1, objuser.getUsercode(),
								1, fromdate, todate, 2);
			}

			lstprotocolmaster = lstprotocolmaster.stream().distinct().collect(Collectors.toList());

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
			mapSheets.put("rejectedTemplate", LSProtocolMasterRepository.countByStatusAndLssitemasterAndCreatedateBetweenAndRejected(1,
							objuser.getLssitemaster().getSitecode(), fromdate, todate,1));
			mapSheets.put("approvedTemplate", LSProtocolMasterRepository
					.countByStatusAndLssitemasterAndCreatedateBetweenAndRejectedNotAndApproved(1,
							objuser.getLssitemaster().getSitecode(), fromdate, todate,1,1));
			mapSheets.put("createdTemplate", LSProtocolMasterRepository
					.countByStatusAndLssitemasterAndCreatedateBetweenAndRejectedNotAndApprovedNot(1,
							objuser.getLssitemaster().getSitecode(), fromdate, todate,1,1));
		} else {

			List<Protocoltemplateget> lstprotocolmaster = new ArrayList<>();
			if (lstteamuser != null && lstteamuser.size() > 0) {
				lstteamuser.add(objuser);
				List<Integer> usercodelist = lstteamuser.stream().map(LSuserMaster::getUsercode)
						.collect(Collectors.toList());
				
				lstprotocolmaster = LSProtocolMasterRepository
						.findByLssitemasterAndStatusAndCreatedateBetweenAndViewoptionAndRejectedOrCreatedbyAndStatusAndCreatedateBetweenAndViewoptionAndRejectedOrCreatedbyInAndStatusAndCreatedateBetweenAndViewoptionAndRejectedOrderByProtocolmastercodeDesc(
								objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, 1,1,
								objuser.getUsercode(),1, fromdate, todate, 2,1, 
								usercodelist, 1, fromdate, todate, 3,1);
				lstprotocolmaster = lstprotocolmaster.stream().distinct().collect(Collectors.toList());
				mapSheets.put("rejectedTemplate",lstprotocolmaster.size());
				lstprotocolmaster = LSProtocolMasterRepository
						.findByLssitemasterAndStatusAndCreatedateBetweenAndViewoptionAndApprovedAndRejectedIsNullOrCreatedbyAndStatusAndCreatedateBetweenAndViewoptionAndApprovedAndRejectedIsNullOrCreatedbyInAndStatusAndCreatedateBetweenAndViewoptionAndApprovedAndRejectedIsNullOrderByProtocolmastercodeDesc(
								objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, 1,1,
								objuser.getUsercode(),1, fromdate, todate, 2,1, 
								usercodelist, 1, fromdate, todate, 3,1);
				
				lstprotocolmaster = lstprotocolmaster.stream().distinct().collect(Collectors.toList());
				mapSheets.put("approvedTemplate",lstprotocolmaster.size());
				lstprotocolmaster = LSProtocolMasterRepository
						.findByLssitemasterAndStatusAndCreatedateBetweenAndViewoptionAndApprovedAndRejectedIsNullOrCreatedbyAndStatusAndCreatedateBetweenAndViewoptionAndApprovedAndRejectedIsNullOrCreatedbyInAndStatusAndCreatedateBetweenAndViewoptionAndApprovedAndRejectedIsNullOrderByProtocolmastercodeDesc(
								objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, 1,0,
								objuser.getUsercode(),1, fromdate, todate, 2,0, 
								usercodelist, 1, fromdate, todate, 3,0);
				lstprotocolmaster.addAll(LSProtocolMasterRepository
						.findByLssitemasterAndStatusAndCreatedateBetweenAndViewoptionAndApprovedIsNullAndRejectedIsNullOrCreatedbyAndStatusAndCreatedateBetweenAndViewoptionAndApprovedIsNullAndRejectedIsNullOrCreatedbyInAndStatusAndCreatedateBetweenAndViewoptionAndApprovedIsNullAndRejectedIsNullOrderByProtocolmastercodeDesc(
								objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, 1,
								objuser.getUsercode(),1, fromdate, todate, 2, 
								usercodelist, 1, fromdate, todate, 3));
				
//				LSProtocolMasterRepository.findByStatusAndLssitemasterAndCreatedateBetweenAndApprovedAndRejected(1,objuser.getLssitemaster().getSitecode(),fromdate, todate,0,0);
				
				lstprotocolmaster = lstprotocolmaster.stream().distinct().collect(Collectors.toList());
				mapSheets.put("createdTemplate",lstprotocolmaster.size());

			} else {
				lstprotocolmaster = LSProtocolMasterRepository
						.findByLssitemasterAndStatusAndCreatedateBetweenAndViewoptionAndRejectedOrCreatedbyAndStatusAndCreatedateBetweenAndViewoptionAndRejectedOrderByProtocolmastercodeDesc(
								objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, 1, 1,
								objuser.getUsercode(),1, fromdate, todate, 2,1);
				lstprotocolmaster = lstprotocolmaster.stream().distinct().collect(Collectors.toList());
				mapSheets.put("rejectedTemplate",lstprotocolmaster.size());
				lstprotocolmaster = LSProtocolMasterRepository
						.findByLssitemasterAndStatusAndCreatedateBetweenAndViewoptionAndApprovedAndRejectedNotOrCreatedbyAndStatusAndCreatedateBetweenAndViewoptionAndApprovedAndRejectedNotOrderByProtocolmastercodeDesc(
								objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, 1, 1,1,
								objuser.getUsercode(),1, fromdate, todate, 2,1,1);
				lstprotocolmaster = lstprotocolmaster.stream().distinct().collect(Collectors.toList());
				mapSheets.put("approvedTemplate",lstprotocolmaster.size());
				lstprotocolmaster = LSProtocolMasterRepository
						.findByLssitemasterAndStatusAndCreatedateBetweenAndViewoptionAndApprovedNotAndRejectedNotOrCreatedbyAndStatusAndCreatedateBetweenAndViewoptionAndApprovedNotAndRejectedNotOrderByProtocolmastercodeDesc(
								objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, 1, 1,1,
								objuser.getUsercode(),1, fromdate, todate, 2,1,1);
				lstprotocolmaster = lstprotocolmaster.stream().distinct().collect(Collectors.toList());
				mapSheets.put("createdTemplate",lstprotocolmaster.size());
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
