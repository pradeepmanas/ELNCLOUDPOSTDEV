package com.agaram.eln.primary.service.dashboard;

import java.util.ArrayList;
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
import com.agaram.eln.primary.model.protocols.LSprotocolworkflow;
import com.agaram.eln.primary.model.protocols.LSprotocolworkflowgroupmap;
import com.agaram.eln.primary.model.sheetManipulation.LSparsedparameters;
import com.agaram.eln.primary.model.sheetManipulation.LSsamplefile;
import com.agaram.eln.primary.model.sheetManipulation.LSworkflow;
import com.agaram.eln.primary.model.sheetManipulation.LSworkflowgroupmapping;
import com.agaram.eln.primary.model.usermanagement.LSMultiusergroup;
import com.agaram.eln.primary.model.usermanagement.LSprojectmaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.model.usermanagement.LSusergroup;
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
import com.agaram.eln.primary.repository.protocol.LSprotocolworkflowgroupmapRepository;
import com.agaram.eln.primary.repository.protocol.lSprotocolworkflowRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LSfileRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LSparsedparametersRespository;
import com.agaram.eln.primary.repository.sheetManipulation.LSsamplefileRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LSworkflowRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LSworkflowgroupmappingRepository;
import com.agaram.eln.primary.repository.usermanagement.LSMultiusergroupRepositery;
import com.agaram.eln.primary.repository.usermanagement.LSprojectmasterRepository;
import com.agaram.eln.primary.repository.usermanagement.LSuserMasterRepository;
import com.agaram.eln.primary.repository.usermanagement.LSusergroupRepository;
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
	private LSusergroupRepository LSusergroupRepository;

	@Autowired
	private LSMultiusergroupRepositery lsMultiusergroupRepositery;

	@Autowired
	private LSfileRepository lsfileRepository;

	@Autowired
	private LSlogilabprotocoldetailRepository LSlogilabprotocoldetailRepository;

	@Autowired
	private LSProtocolMasterRepository LSProtocolMasterRepository;

	@Autowired
	private LSprotocolworkflowgroupmapRepository LSprotocolworkflowgroupmapRepository;

	@Autowired
	private lSprotocolworkflowRepository lSprotocolworkflowRepository;

	@Autowired
	private LsordersharedbyRepository lsordersharedbyRepository;

	@Autowired
	private LsordersharetoRepository lsordersharetoRepository;

	@Autowired
	private LsrepositoriesRepository LsrepositoriesRepository;

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

	@SuppressWarnings("unused")
	public Map<String, Object> Getdashboardordercount(LSuserMaster objuser) {
		Date fromdate = objuser.getObjuser().getFromdate();
		Date todate = objuser.getObjuser().getTodate();
		Map<String, Object> mapOrders = new HashMap<String, Object>();
		List<LSsamplefile> lssamplefile = lssamplefileRepository.findByprocessed(1);

		if (objuser.getObjuser().getOrderfor() != 1) {
			mapOrders.put("orders", LSlogilabprotocoldetailRepository.countByCreatedtimestampBetween(fromdate, todate));
			mapOrders.put("pendingorder", LSlogilabprotocoldetailRepository
					.countByOrderflagAndCreatedtimestampBetween("N", fromdate, todate));
			mapOrders.put("completedorder", LSlogilabprotocoldetailRepository
					.countByOrderflagAndCreatedtimestampBetween("R", fromdate, todate));
			mapOrders.put("onproces", 0);
		} else if (objuser.getUsername().equals("Administrator") && objuser.getObjuser().getOrderfor() == 1) {
			mapOrders.put("orders",
					lslogilablimsorderdetailRepository.countByCreatedtimestampBetween(fromdate, todate));
			mapOrders.put("pendingorder", lslogilablimsorderdetailRepository
					.countByOrderflagAndCreatedtimestampBetween("N", fromdate, todate));
			mapOrders.put("completedorder", lslogilablimsorderdetailRepository
					.countByOrderflagAndCompletedtimestampBetween("R", fromdate, todate));
			mapOrders.put("onproces", lslogilablimsorderdetailRepository
					.countByOrderflagAndLssamplefileInAndCreatedtimestampBetween("N", lssamplefile, fromdate, todate));

		} else {

			List<LSprojectmaster> lstproject = objuser.getLstproject();

			long lstUserorder = 0;
			if (lstproject != null && lstproject.size() > 0) {
				lstUserorder = lslogilablimsorderdetailRepository
						.countByLsprojectmasterInAndCreatedtimestampBetween(lstproject, fromdate, todate);

			}
//			else {
//
//				lstUserorder = lslogilablimsorderdetailRepository.countByCreatedtimestampBetween(fromdate, todate);
//			}

			long lstlimscompleted = 0;
			if (lstproject != null && lstproject.size() > 0) {
				lstlimscompleted = lslogilablimsorderdetailRepository
						.countByOrderflagAndLsprojectmasterInAndCreatedtimestampBetween("R", lstproject, fromdate,
								todate);
			}
//			else {
//				lstlimscompleted = lslogilablimsorderdetailRepository.countByOrderflagAndCreatedtimestampBetween("R",
//						fromdate, todate);
//
//			}

			long lstordersinprogress = 0;
			if (lstproject != null && lssamplefile != null && lstproject.size() > 0 && lssamplefile.size() > 0) {

				lstordersinprogress = lslogilablimsorderdetailRepository
						.countByOrderflagAndLsprojectmasterInAndApprovelstatusAndApprovedAndCreatedtimestampBetweenOrderByBatchcodeDesc(
								"N", lstproject, 1, 1, fromdate, todate);

			}

			long lstpending = 0;
			if (lstproject != null && lstproject.size() > 0) {
				lstpending = lslogilablimsorderdetailRepository
						.countByOrderflagAndLsprojectmasterInAndCreatedtimestampBetween("N", lstproject, fromdate,
								todate);
			}
//			else {
//			
//				lstpending = lslogilablimsorderdetailRepository.countByOrderflagAndCreatedtimestampBetween("N",
//						fromdate, todate);
//			}

			mapOrders.put("orders", (lstUserorder));
			mapOrders.put("pendingorder", (lstpending));
			mapOrders.put("completedorder", (lstlimscompleted));
			mapOrders.put("onproces", lstordersinprogress);

		}

		if (objuser.getObjsilentaudit() != null) {
			objuser.getObjsilentaudit().setTableName("LSlogilablimsorderdetail");
			lscfttransactionRepository.save(objuser.getObjsilentaudit());
		}

		return mapOrders;
	}

	@SuppressWarnings("unused")
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
						.findByOrderflagAndCreatedtimestampBetweenOrderByBatchcodeDesc("R", fromdate, todate));
			} else if (objuser.getObjuser().getOrderselectiontype() == 3) {
				mapOrders.put("orderlst", lslogilablimsorderdetailRepository
						.findByOrderflagAndCreatedtimestampBetweenOrderByBatchcodeDesc("N", fromdate, todate));
			} else if (objuser.getObjuser().getOrderselectiontype() == 4) {
				mapOrders.put("orderlst",
						lslogilablimsorderdetailRepository
								.findByOrderflagAndLssamplefileInAndCreatedtimestampBetweenOrderByBatchcodeDesc("N",
										lssamplefile, fromdate, todate));
			}

		} else {

			List<LSprojectmaster> lstproject = objuser.getLstproject();
			List<Logilabordermaster> lstorders = new ArrayList<Logilabordermaster>();
			if (lstproject != null) {
				List<LSworkflow> lstworkflow = objuser.getLstworkflow();

				if (objuser.getObjuser().getOrderselectiontype() == 1) {
					lstorders = lslogilablimsorderdetailRepository
							.findByLsprojectmasterInAndCreatedtimestampBetween(lstproject, fromdate, todate);

				} else if (objuser.getObjuser().getOrderselectiontype() == 2) {
					lstorders = lslogilablimsorderdetailRepository
							.findByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenOrderByBatchcodeDesc("R",
									lstproject, fromdate, todate);

				} else if (objuser.getObjuser().getOrderselectiontype() == 3) {
					lstorders = lslogilablimsorderdetailRepository
							.findByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenOrderByBatchcodeDesc("N",
									lstproject, fromdate, todate);
				} else if (objuser.getObjuser().getOrderselectiontype() == 4) {

					lstorders = lslogilablimsorderdetailRepository
							.findByOrderflagAndLssamplefileInAndCreatedtimestampBetween("N", lssamplefile, fromdate,
									todate);

				}

				lstorders.forEach(objorder -> objorder.setLstworkflow(lstworkflow));
			}

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

		if (objuser.getObjuser().getOrderselectiontype() == 1) {
			lstorders = LSlogilabprotocoldetailRepository.findByCreatedtimestampBetween(fromdate, todate);

		} else if (objuser.getObjuser().getOrderselectiontype() == 2) {
			lstorders = LSlogilabprotocoldetailRepository.findByOrderflagAndCreatedtimestampBetween("R", fromdate,
					todate);

		} else if (objuser.getObjuser().getOrderselectiontype() == 3) {
			lstorders = LSlogilabprotocoldetailRepository.findByOrderflagAndCreatedtimestampBetween("N", fromdate,
					todate);
		}

		LSusergroup userGroup = LSusergroupRepository.findOne(objuser.getObjuser().getMultiusergroupcode());

		List<LSprotocolworkflowgroupmap> lsworkflowgroupmapping = LSprotocolworkflowgroupmapRepository
				.findBylsusergroupAndWorkflowcodeNotNull(userGroup);

		if (lsworkflowgroupmapping != null && lsworkflowgroupmapping.size() > 0) {
			LSprotocolworkflow lsprotocolworkflow = lSprotocolworkflowRepository
					.findByworkflowcode(lsworkflowgroupmapping.get(0).getWorkflowcode());

			lstorders.forEach(objorder -> objorder
					.setCanuserprocess(lsprotocolworkflow.equals(objorder.getlSprotocolworkflow()) ? true : false));
		}
		
		mapOrders.put("orderlst", lstorders);

		return mapOrders;
	}

	public Map<String, Object> Getdashboardsheets(LSuserMaster objuser) {
		Date fromdate = objuser.getObjuser().getFromdate();
		Date todate = objuser.getObjuser().getTodate();
		Map<String, Object> mapSheets = new HashMap<String, Object>();
		List<LSuserMaster> lstteamuser = objuser.getObjuser().getTeamusers();

		if (objuser.getUsername().equals("Administrator")) {
			mapSheets.put("Sheets", lsfileRepository
					.findByCreatedateBetweenAndFilecodeGreaterThanOrderByFilecodeDesc(fromdate, todate, 1));
		} else {
			List<Sheettemplateget> lstsheets = new ArrayList<Sheettemplateget>();

			if (lstteamuser != null && lstteamuser.size() > 0) {
				lstteamuser.add(objuser);
				lstsheets = lsfileRepository
						.findByFilecodeGreaterThanAndCreatebyInAndCreatedateBetweenOrderByFilecodeDesc(1, lstteamuser,
								fromdate, todate);
			} else {
				lstsheets = lsfileRepository
						.findByFilecodeGreaterThanAndCreatebyAndCreatedateBetweenOrderByFilecodeDesc(1, objuser,
								fromdate, todate);
			}

			mapSheets.put("Sheets", lstsheets);
		}

		return mapSheets;
	}

	public Map<String, Object> Getdashboardprotocoltemplate(LSuserMaster objuser) {
		Date fromdate = objuser.getObjuser().getFromdate();
		Date todate = objuser.getObjuser().getTodate();
		Map<String, Object> mapSheets = new HashMap<String, Object>();

		if (objuser.getUsername().equals("Administrator")) {
			mapSheets.put("Sheets", LSProtocolMasterRepository.findByStatusAndLssitemasterAndCreatedateBetween(1,
					objuser.getLssitemaster().getSitecode(), fromdate, todate));
		} else {

			List<Protocoltemplateget> lstprotocolmaster = new ArrayList<>();

			LSprotocolworkflow lsprotocolworkflow = new LSprotocolworkflow();
			List<LSprotocolworkflowgroupmap> lsprotocolworkflowgroupmap = LSprotocolworkflowgroupmapRepository
					.findBylsusergroupAndWorkflowcodeNotNull(objuser.getLsusergroup());

			if (lsprotocolworkflowgroupmap != null && lsprotocolworkflowgroupmap.size() > 0) {
				lsprotocolworkflow = lSprotocolworkflowRepository
						.findByworkflowcode(lsprotocolworkflowgroupmap.get(0).getWorkflowcode());

				List<Protocoltemplateget> lstprotocolmasterInUser = LSProtocolMasterRepository
						.findByCreatedbyOrLSprotocolworkflowAndStatusAndCreatedateBetweenOrderByProtocolmastercodeDesc(
								objuser.getUsercode(), lsprotocolworkflow, 1, fromdate, todate);

				lstprotocolmaster.addAll(lstprotocolmasterInUser);
			}

			lstprotocolmaster = lstprotocolmaster.stream().distinct().collect(Collectors.toList());

			mapSheets.put("Sheets", lstprotocolmaster);
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

		if (lstproject != null) {
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
		
		return LsrepositoriesRepository.findBySitecodeAndAddedonBetweenOrderByRepositorycodeAsc(objuser.getLssitemaster().getSitecode(),fromdate,todate);
	}

}
