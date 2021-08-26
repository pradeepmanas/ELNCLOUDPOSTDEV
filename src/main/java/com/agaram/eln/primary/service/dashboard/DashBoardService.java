package com.agaram.eln.primary.service.dashboard;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.agaram.eln.primary.model.cfr.LSactivity;
import com.agaram.eln.primary.model.getorders.Logilaborders;
import com.agaram.eln.primary.model.getsheetdetails.Sheettemplateget;
import com.agaram.eln.primary.model.instrumentDetails.LSlogilablimsorderdetail;
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
import com.agaram.eln.primary.repository.sheetManipulation.LSfileRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LSparsedparametersRespository;
import com.agaram.eln.primary.repository.sheetManipulation.LSsamplefileRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LSworkflowRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LSworkflowgroupmappingRepository;
import com.agaram.eln.primary.repository.usermanagement.LSMultiusergroupRepositery;
import com.agaram.eln.primary.repository.usermanagement.LSprojectmasterRepository;
import com.agaram.eln.primary.repository.usermanagement.LSuserMasterRepository;
import com.agaram.eln.primary.repository.usermanagement.LSusersteamRepository;
import com.agaram.eln.primary.repository.usermanagement.LSuserteammappingRepository;
import com.agaram.eln.primary.service.instrumentDetails.InstrumentService;

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
	private InstrumentService instrumentService;

	@Autowired
	private LSfileRepository lsfileRepository;

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

			List<LSworkflow> lsworkflow = lsworkflowRepository.findByLsworkflowgroupmappingIn(lsworkflowgroupmapping);

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

			List<LSworkflow> lsworkflow = lsworkflowRepository.findByLsworkflowgroupmappingIn(lsworkflowgroupmapping);

			long lstUserorder = lslogilablimsorderdetailRepository
					.countByLsprojectmasterInAndCreatedtimestampBetween(lstproject, fromdate, todate);

			long lstlimscompleted = lslogilablimsorderdetailRepository
					.countByFiletypeAndOrderflagAndCreatedtimestampBetween(0, "R", fromdate, todate);
//			long lstCompletedordercount = lslogilablimsorderdetailRepository.countByOrderflagAndLsprojectmasterInOrderByBatchcodeDesc("R",lstproject);
			List<Long> lstCompletedordercount = new ArrayList<Long>();
			if (lstproject != null && lstproject.size() > 0) {

//				lstCompletedordercount = lslogilablimsorderdetailRepository.countByOrderflagAndLsprojectmasterInOrderByBatchcodeDesc("R",lstproject,objuser.getUsercode());
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
//			long orderinproject = lslogilablimsorderdetailRepository.countByOrderflagAndLssamplefileInAndLsprojectmasterIn("N", lssamplefile,lstproject);
			List<Long> orderinproject = new ArrayList<Long>();
			if (lstproject != null && lssamplefile != null && lstproject.size() > 0 && lssamplefile.size() > 0) {
//				orderinproject = lslogilablimsorderdetailRepository.countByOrderflagAndLssamplefileInAndLsprojectmasterIn("N", lssamplefile,lstproject,objuser.getUsercode());
				orderinproject = lslogilablimsorderdetailRepository
						.countByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenOrderByBatchcodeDescInprogress(
								"N", lstproject, 1, 1, "N", fromdate, todate);
			}
			mapOrders.put("orders", (lsorder + lstUserorder));
			long lstlimspending = lslogilablimsorderdetailRepository
					.countByFiletypeAndOrderflagAndCreatedtimestampBetween(0, "N", fromdate, todate);
//			long lstpending = lslogilablimsorderdetailRepository.countByOrderflagAndLsprojectmasterInAndLsworkflowInOrderByBatchcodeDesc("N",lstproject, lsworkflow);
			List<Long> lstpending = new ArrayList<Long>();
			if (lstproject != null && lsworkflow != null && lstproject.size() > 0 && lsworkflow.size() > 0) {
//				lstpending = lslogilablimsorderdetailRepository.countByOrderflagAndLsprojectmasterInAndLsworkflowInOrderByBatchcodeDesc("N",lstproject, lsworkflow,objuser.getUsercode());
				lstpending = lslogilablimsorderdetailRepository
						.countByOrderflagAndLsprojectmasterInAndCreatedtimestampBetween("N", lstproject, fromdate,
								todate);
			}

			List<LSparsedparameters> lstparsedparam = new ArrayList<LSparsedparameters>();
			if (lstBatchcode != null && lstBatchcode.size() > 0) {
				lstparsedparam = lsparsedparametersRespository.getByBatchcodeIn(lstBatchcode);
			}
			mapOrders.put("pendingorder", (lstlimspending + lstpending.size()));
			mapOrders.put("completedorder", (lstlimscompleted + lstCompletedordercount.size()));
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

		} else {
			long lsorder = lslogilablimsorderdetailRepository.countByFiletypeAndCreatedtimestampBetween(0, fromdate,
					todate);

			List<LSuserteammapping> lstteammap = lsuserteammappingRepository.findBylsuserMaster(objupdateduser);
			List<LSusersteam> lstteam = lsusersteamRepository.findByLsuserteammappingIn(lstteammap);
			List<LSworkflowgroupmapping> lsworkflowgroupmapping = lsworkflowgroupmappingRepository
					.findBylsusergroup(objupdateduser.getLsusergroup());
			List<LSprojectmaster> lstproject = lsprojectmasterRepository.findByLsusersteamIn(lstteam);

			List<LSworkflow> lsworkflow = lsworkflowRepository.findByLsworkflowgroupmappingIn(lsworkflowgroupmapping);

			long lstUserorder = lslogilablimsorderdetailRepository
					.countByLsprojectmasterInAndCreatedtimestampBetween(lstproject, fromdate, todate);

			long lstlimscompleted = lslogilablimsorderdetailRepository
					.countByFiletypeAndOrderflagAndCreatedtimestampBetween(0, "R", fromdate, todate);
			List<Long> lstCompletedordercount = new ArrayList<Long>();
			if (lstproject != null && lstproject.size() > 0) {
				lstCompletedordercount = lslogilablimsorderdetailRepository
						.countByOrderflagAndLsprojectmasterInAndCompletedtimestampBetween("R", lstproject, fromdate,
								todate);
			}

			long lstlimsprocess = lslogilablimsorderdetailRepository
					.countByFiletypeAndOrderflagAndLssamplefileInAndCreatedtimestampBetween(0, "N", lssamplefile,
							fromdate, todate);
//			List<Long> orderinproject = new ArrayList<Long>();
			List<Logilaborders> lstordersinprogress = new ArrayList<Logilaborders>();
			if (lstproject != null && lssamplefile != null && lstproject.size() > 0 && lssamplefile.size() > 0) {
//				orderinproject = lslogilablimsorderdetailRepository
//						.countByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenOrderByBatchcodeDescInprogress(
//								"N", lstproject, 1, 1, "N", fromdate, todate);
				lstordersinprogress = lslogilablimsorderdetailRepository
						.findByOrderflagAndLsprojectmasterInAndApprovelstatusAndApprovedAndCreatedtimestampBetweenOrderByBatchcodeDesc(
								"N", lstproject, 1, 1, fromdate, todate);
			}
			mapOrders.put("orders", (lsorder + lstUserorder));
			long lstlimspending = lslogilablimsorderdetailRepository
					.countByFiletypeAndOrderflagAndCreatedtimestampBetween(0, "N", fromdate, todate);
			List<Long> lstpending = new ArrayList<Long>();
			if (lstproject != null && lsworkflow != null && lstproject.size() > 0 && lsworkflow.size() > 0) {
				lstpending = lslogilablimsorderdetailRepository
						.countByOrderflagAndLsprojectmasterInAndCreatedtimestampBetween("N", lstproject, fromdate,
								todate);
			}

			mapOrders.put("pendingorder", (lstlimspending + lstpending.size()));
			mapOrders.put("completedorder", (lstlimscompleted + lstCompletedordercount.size()));
			mapOrders.put("onproces", lstlimsprocess + lstordersinprogress.size());

		}

		if (objuser.getObjsilentaudit() != null) {
			objuser.getObjsilentaudit().setTableName("LSlogilablimsorderdetail");
			lscfttransactionRepository.save(objuser.getObjsilentaudit());
		}

		return mapOrders;
	}

	public Map<String, Object> Getdashboardorders(LSuserMaster objuser) {
		Date fromdate = objuser.getObjuser().getFromdate();
		Date todate = objuser.getObjuser().getTodate();
		Map<String, Object> mapOrders = new HashMap<String, Object>();
		LSMultiusergroup objLSMultiusergroup = new LSMultiusergroup();
		objLSMultiusergroup = lsMultiusergroupRepositery.findBymultiusergroupcode(objuser.getMultiusergroups());
		objuser.setLsusergroup(objLSMultiusergroup.getLsusergroup());

//		objuser.getObjuser().getOrderselectiontype()

		if (objuser.getUsername().equals("Administrator")) {

			if (objuser.getObjuser().getOrderselectiontype() == 1) {
				mapOrders.put("orderlst", lslogilablimsorderdetailRepository
						.findByCreatedtimestampBetweenOrderByBatchcodeDesc(fromdate, todate));
			} else if (objuser.getObjuser().getOrderselectiontype() == 2) {
				mapOrders.put("orderlst", lslogilablimsorderdetailRepository
						.findByOrderflagAndCreatedtimestampBetween("R", fromdate, todate));
			} else if (objuser.getObjuser().getOrderselectiontype() == 3) {
				mapOrders.put("orderlst", lslogilablimsorderdetailRepository
						.findByOrderflagAndCreatedtimestampBetween("N", fromdate, todate));
			} else if (objuser.getObjuser().getOrderselectiontype() == 4) {
				mapOrders.put("orderlst", lslogilablimsorderdetailRepository
						.findByOrderflagAndApprovelstatusAndApprovedAndCreatedtimestampBetweenOrderByBatchcodeDesc("N",
								1, 1, fromdate, todate));
			}

		} else {

			List<LSuserteammapping> lstteammap = lsuserteammappingRepository.findBylsuserMaster(objuser);
			List<LSusersteam> lstteam = lsusersteamRepository.findByLsuserteammappingIn(lstteammap);
			List<LSprojectmaster> lstproject = lsprojectmasterRepository.findByLsusersteamIn(lstteam);

			List<LSworkflow> lstworkflow = instrumentService.GetWorkflowonuser(
					lsMultiusergroupRepositery.findBymultiusergroupcode(objuser.getMultiusergroups()));

			List<Logilaborders> lstorders = new ArrayList<Logilaborders>();

			if (objuser.getObjuser().getOrderselectiontype() == 1) {
				lstorders = lslogilablimsorderdetailRepository
						.findByLsprojectmasterInAndCreatedtimestampBetweenOrderByBatchcodeDesc(lstproject, fromdate,
								todate);
			} else if (objuser.getObjuser().getOrderselectiontype() == 2) {
				lstorders = lslogilablimsorderdetailRepository
						.findByOrderflagAndLsprojectmasterInAndCreatedtimestampBetween("R", lstproject, fromdate,
								todate);
			} else if (objuser.getObjuser().getOrderselectiontype() == 3) {
				lstorders = lslogilablimsorderdetailRepository
						.findByOrderflagAndLsprojectmasterInAndCreatedtimestampBetween("N", lstproject, fromdate,
								todate);
			} else if (objuser.getObjuser().getOrderselectiontype() == 4) {
				lstorders = lslogilablimsorderdetailRepository
						.findByOrderflagAndLsprojectmasterInAndApprovelstatusAndApprovedAndCreatedtimestampBetweenOrderByBatchcodeDesc(
								"N", lstproject, 1, 1, fromdate, todate);
			}

			lstorders.forEach(objorder -> objorder.setLstworkflow(lstworkflow));

			mapOrders.put("orderlst", lstorders);
		}

		if (objuser.getObjsilentaudit() != null) {
			objuser.getObjsilentaudit().setTableName("LSlogilablimsorderdetail");
			lscfttransactionRepository.save(objuser.getObjsilentaudit());
		}

		return mapOrders;
	}

	public Map<String, Object> Getdashboardsheets(LSuserMaster objuser) {
		Date fromdate = objuser.getObjuser().getFromdate();
		Date todate = objuser.getObjuser().getTodate();
		Map<String, Object> mapSheets = new HashMap<String, Object>();

		if (objuser.getUsername().equals("Administrator")) {
			mapSheets.put("Sheets", lsfileRepository
					.findByCreatedateBetweenAndFilecodeGreaterThanOrderByFilecodeDesc(fromdate, todate, 1));
		} else {
			List<Sheettemplateget> lstsheets = new ArrayList<Sheettemplateget>();
			List<Integer> lstteammap = lsuserteammappingRepository
					.getTeamcodeByLsuserMaster4postgressandsql(objuser.getUsercode());

			if (lstteammap.size() > 0) {
				List<LSuserMaster> lstteamuser = lsuserteammappingRepository.getLsuserMasterByTeamcode(lstteammap);
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

	public Map<String, Object> Getdashboardparameters(LSuserMaster objuser) {
		Date fromdate = objuser.getObjuser().getFromdate();
		Date todate = objuser.getObjuser().getTodate();
		LSuserMaster objupdateduser = lsuserMasterRepository.findByusercode(objuser.getUsercode());
		Map<String, Object> mapOrders = new HashMap<String, Object>();
		LSMultiusergroup objLSMultiusergroup = new LSMultiusergroup();
		objLSMultiusergroup = lsMultiusergroupRepositery.findBymultiusergroupcode(objuser.getMultiusergroups());
		objupdateduser.setLsusergroup(objLSMultiusergroup.getLsusergroup());

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
		LSuserMaster objupdateduser = lsuserMasterRepository.findByusercode(objuser.getUsercode());
		Map<String, Object> mapOrders = new HashMap<String, Object>();
		LSMultiusergroup objLSMultiusergroup = new LSMultiusergroup();
		objLSMultiusergroup = lsMultiusergroupRepositery.findBymultiusergroupcode(objuser.getMultiusergroups());
		objupdateduser.setLsusergroup(objLSMultiusergroup.getLsusergroup());

		if (objupdateduser.getUsername().equals("Administrator")) {
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

	public List<LSactivity> GetActivitiesonLazy(LSactivity objactivities) {
		List<LSactivity> lstactivities = new ArrayList<LSactivity>();
		lstactivities = lsactivityRepository
				.findTop20ByActivitycodeLessThanOrderByActivitycodeDesc(objactivities.getActivitycode());
		return lstactivities;
	}
}
