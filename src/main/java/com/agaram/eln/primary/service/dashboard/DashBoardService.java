package com.agaram.eln.primary.service.dashboard;

import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.agaram.eln.primary.commonfunction.commonfunction;
import com.agaram.eln.primary.fetchmodel.getmasters.Projectmaster;
import com.agaram.eln.primary.fetchmodel.getmasters.Repositorymaster;
import com.agaram.eln.primary.fetchmodel.getorders.LogilabOrdermastersh;
import com.agaram.eln.primary.fetchmodel.getorders.LogilabProtocolOrderssh;
import com.agaram.eln.primary.fetchmodel.getorders.Logilabordermaster;
import com.agaram.eln.primary.fetchmodel.getorders.Logilaborders;
import com.agaram.eln.primary.fetchmodel.getorders.Logilaborderssh;
import com.agaram.eln.primary.fetchmodel.getorders.Logilabprotocolorders;
import com.agaram.eln.primary.fetchmodel.getorders.ProtocolOrdersDashboard;
import com.agaram.eln.primary.fetchmodel.gettemplate.Protocoltemplateget;
import com.agaram.eln.primary.fetchmodel.gettemplate.Sheettemplateget;
import com.agaram.eln.primary.model.cfr.LSactivity;
import com.agaram.eln.primary.model.dashboard.LsActiveWidgets;
import com.agaram.eln.primary.model.instrumentDetails.LSSelectedTeam;
import com.agaram.eln.primary.model.instrumentDetails.LSSheetOrderStructure;
import com.agaram.eln.primary.model.instrumentDetails.LSlogilablimsorderdetail;
import com.agaram.eln.primary.model.instrumentDetails.LSprotocolfolderfiles;
import com.agaram.eln.primary.model.instrumentDetails.LSsheetfolderfiles;
import com.agaram.eln.primary.model.instrumentDetails.Lsprotocolorderstructure;
import com.agaram.eln.primary.model.masters.Lslogbooks;
import com.agaram.eln.primary.model.material.Elnmaterial;
import com.agaram.eln.primary.model.material.ElnmaterialInventory;
import com.agaram.eln.primary.model.protocols.Elnprotocolworkflow;
import com.agaram.eln.primary.model.protocols.LSlogilabprotocoldetail;
import com.agaram.eln.primary.model.protocols.LSlogilabprotocoldetail.Protocolorder;
import com.agaram.eln.primary.model.protocols.LSprotocolmaster;
import com.agaram.eln.primary.model.protocols.LSprotocolselectedteam;
import com.agaram.eln.primary.model.sheetManipulation.LSfile;
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
import com.agaram.eln.primary.repository.dashboard.LsActiveWidgetsRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LSSelectedTeamRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LSSheetOrderStructureRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LSlogilablimsorderdetailRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LSprotocolfolderfilesRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LSsheetfolderfilesRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LogilablimsorderdetailsRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LsordersharedbyRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LsordersharetoRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LsprotocolOrderStructureRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LsprotocolordersharetoRepository;
import com.agaram.eln.primary.repository.masters.LslogbooksRepository;
import com.agaram.eln.primary.repository.masters.LsrepositoriesRepository;
import com.agaram.eln.primary.repository.material.ElnmaterialInventoryRepository;
import com.agaram.eln.primary.repository.protocol.LSProtocolMasterRepository;
import com.agaram.eln.primary.repository.protocol.LSlogilabprotocoldetailRepository;
import com.agaram.eln.primary.repository.protocol.LSprotocolselectedteamRepository;
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

import javassist.expr.NewArray;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

@EnableScheduling
@Service
public class DashBoardService {
	@Autowired
	private LSlogilablimsorderdetailRepository lslogilablimsorderdetailRepository;

	@Autowired
	private LogilablimsorderdetailsRepository LogilablimsorderdetailsRepository;

//	@Autowired
//    private DataSourceConfigRepository configRepo;

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

//	@Autowired
//	private LsresultforordersRepository lsresultforordersRepository;

	@Autowired
	private LSMultiusergroupRepositery lsMultiusergroupRepositery;

	@Autowired
	private LSSelectedTeamRepository LSSelectedTeamRepository;

	@Autowired
	private LSfileRepository lsfileRepository;

	@Autowired
	private LSfileversionRepository lsfileversionRepository;

	@Autowired
	private LSlogilabprotocoldetailRepository LSlogilabprotocoldetailRepository;

	@Autowired
	private LSprotocolselectedteamRepository lsprotoselectedteamRepo;

	@Autowired
	private LSProtocolMasterRepository LSProtocolMasterRepository;

	@Autowired
	private LsordersharedbyRepository lsordersharedbyRepository;

	@Autowired
	private LsordersharetoRepository lsordersharetoRepository;

	@Autowired
	private LsprotocolordersharetoRepository lsprotocolordersharetoRepository;

	@Autowired
	private LsrepositoriesRepository LsrepositoriesRepository;

	@Autowired
	LSuserteammappingRepository LSuserteammappingRepositoryObj;

	@Autowired
	private LSsamplemasterRepository lssamplemasterrepository;

	@Autowired
	private LSSheetOrderStructureRepository lsSheetOrderStructureRepository;

	@Autowired
	private Environment env;

	@Autowired
	private LsprotocolOrderStructureRepository lsprotocolorderStructurerepository;

//	@Autowired
//	private InstrumentService instrumentService;

	@Autowired
	private LSsheetfolderfilesRepository lssheetfolderfilesRepository;

	@Autowired
	private LSprotocolfolderfilesRepository lsprotocolfolderfilesRepository;

	@Autowired
	private LslogbooksRepository lslogbooksRepository;

	@Autowired
	ElnmaterialInventoryRepository elnmaterialInventoryReppository;

	@Autowired
	private LsActiveWidgetsRepository lsActiveWidgetsRepository;

//	@Autowired
//	private NotificationRepository notificationRepository;

//	private Map<Integer, TimerTask> scheduledTasks = new HashMap<>();

	@PersistenceContext
	private EntityManager entityManager;

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
			List<LSusersteam> lstteam = lsusersteamRepository.findByLsuserteammappingInAndLssitemaster(lstteammap,
					objupdateduser.getLssitemaster());
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
			try {
				objuser.getObjsilentaudit().setTransactiondate(commonfunction.getCurrentUtcTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
			List<LSusersteam> lstteam = lsusersteamRepository.findByLsuserteammappingInAndLssitemaster(lstteammap,
					objupdateduser.getLssitemaster());
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
			try {
				objuser.getObjsilentaudit().setTransactiondate(commonfunction.getCurrentUtcTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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

		List<LSuserteammapping> teammapping = LSuserteammappingRepositoryObj
				.findByLsuserMasterAndTeamcodeNotNull(objuser);
		List<LSusersteam> userteamlist = lsusersteamRepository.findByLsuserteammappingIn(teammapping);

		if (objuser.getObjuser().getOrderfor() != 1) {
			long orders = 0;
			long pendingOrders = 0;
			long completedOrders = 0;
			long rejectedOrders = 0;
			long cancelledOrders = 0;

			List<LSprotocolselectedteam> selectedteamorders = lsprotoselectedteamRepo
					.findByUserteamInAndCreatedtimestampBetween(userteamlist, fromdate, todate);
			List<Long> selectedteamprotcolorderList = (selectedteamorders != null && !selectedteamorders.isEmpty())
					? selectedteamorders.stream().map(LSprotocolselectedteam::getProtocolordercode)
							.filter(Objects::nonNull).distinct().collect(Collectors.toList())
					: Collections.singletonList(-1L);

			if (lstproject != null && lstproject.size() > 0) {

				if (testcode == -1 && objuser.getLstprojectforfilter() == null) {

//				mapOrders.put("orders", LSlogilabprotocoldetailRepository
//						.countByLsprojectmasterInAndCreatedtimestampBetweenAndSitecodeOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatebyAndCreatedtimestampBetweenOrLsprojectmasterInAndViewoptionAndSitecodeAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndCreatebyInOrderByProtocolordercodeDesc(
//								lstproject, fromdate, todate, objuser.getLssitemaster().getSitecode(), 1,
//								objuser.getLssitemaster().getSitecode(), fromdate, todate, 2,
//								objuser.getLssitemaster().getSitecode(), objuser.getUsercode(), fromdate, todate,
//								lstproject, 3, objuser.getLssitemaster().getSitecode(), fromdate, todate,3,objuser.getLssitemaster().getSitecode(), fromdate, todate,userlist));

//					orders = LSlogilabprotocoldetailRepository
//							.countByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatebyAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndCreatebyIn(
//									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, 2,
//									objuser.getLssitemaster().getSitecode(), objuser.getUsercode(), fromdate, todate, 3,
//									objuser.getLssitemaster().getSitecode(), fromdate, todate, userlist);
//
//					orders = orders + LSlogilabprotocoldetailRepository
//							.countByLsprojectmasterInAndViewoptionAndSitecodeAndCreatedtimestampBetween(lstproject, 3,
//									objuser.getLssitemaster().getSitecode(), fromdate, todate);
//
//					orders = orders + LSlogilabprotocoldetailRepository
//							.countByLsprojectmasterInAndCreatedtimestampBetweenAndSitecode(lstproject, fromdate, todate,
//									objuser.getLssitemaster().getSitecode());
//
//					mapOrders.put("orders", orders);

//					mapOrders.put("pendingorder", LSlogilabprotocoldetailRepository
//							.countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndOrdercancellIsNullAndRejectedIsNullOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndRejectedIsNullOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndRejectedIsNullOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndRejectedIsNullAndCreatebyInOrderByProtocolordercodeDesc(
//									"N", objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject, "N",
//									objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, "N",
//									objuser.getLssitemaster().getSitecode(), 2, objuser.getUsercode(), fromdate, todate,
//									"N", objuser.getLssitemaster().getSitecode(), 3, fromdate, todate, userlist));

					mapOrders.put("pendingorder", LSlogilabprotocoldetailRepository
							.countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndOrdercancellIsNullAndRejectedIsNullOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndRejectedIsNullOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndRejectedIsNullOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndRejectedIsNullAndCreatebyInAndTeamselectedOrTeamselectedAndProtocolordercodeInAndOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndRejectedIsNullAndCreatebyInOrderByProtocolordercodeDesc(
									"N", objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject, "N",
									objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, "N",
									objuser.getLssitemaster().getSitecode(), 2, objuser.getUsercode(), fromdate, todate,
									"N", objuser.getLssitemaster().getSitecode(), 3, fromdate, todate, userlist, false,
									true, selectedteamprotcolorderList, "N", objuser.getLssitemaster().getSitecode(), 3,
									fromdate, todate, userlist));

//					mapOrders.put("completedorder", LSlogilabprotocoldetailRepository
//							.countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndRejectedIsNullOrOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenOrOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenOrOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyInOrderByProtocolordercodeDesc(
//									"R", objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject, "R",
//									objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, "R",
//									objuser.getLssitemaster().getSitecode(), 2, objuser.getUsercode(), fromdate, todate,
//									"R", objuser.getLssitemaster().getSitecode(), 3, fromdate, todate, userlist));

					mapOrders.put("completedorder", LSlogilabprotocoldetailRepository
							.countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndRejectedIsNullOrOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenOrOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenOrOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyInAndTeamselectedOrTeamselectedAndProtocolordercodeInAndOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyInOrderByProtocolordercodeDesc(
									"R", objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject, "R",
									objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, "R",
									objuser.getLssitemaster().getSitecode(), 2, objuser.getUsercode(), fromdate, todate,
									"R", objuser.getLssitemaster().getSitecode(), 3, fromdate, todate, userlist, false,
									true, selectedteamprotcolorderList, "R", objuser.getLssitemaster().getSitecode(), 3,
									fromdate, todate, userlist));

//					mapOrders.put("rejectedorder", LSlogilabprotocoldetailRepository
//							.countByRejectedAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInOrRejectedAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenOrRejectedAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenOrRejectedAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyInOrderByProtocolordercodeDesc(
//									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject, 1,
//									objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, 1,
//									objuser.getLssitemaster().getSitecode(), 2, objuser.getUsercode(), fromdate, todate,
//									1, objuser.getLssitemaster().getSitecode(), 3, fromdate, todate, userlist));

					mapOrders.put("rejectedorder", LSlogilabprotocoldetailRepository
							.countByRejectedAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInOrRejectedAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenOrRejectedAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenOrRejectedAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyInAndTeamselectedOrTeamselectedAndProtocolordercodeInAndRejectedAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyInOrderByProtocolordercodeDesc(
									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject, 1,
									objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, 1,
									objuser.getLssitemaster().getSitecode(), 2, objuser.getUsercode(), fromdate, todate,
									1, objuser.getLssitemaster().getSitecode(), 3, fromdate, todate, userlist, false,
									true, selectedteamprotcolorderList, 1, objuser.getLssitemaster().getSitecode(), 3,
									fromdate, todate, userlist));

//					mapOrders.put("canceledorder", LSlogilabprotocoldetailRepository
//							.countByOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyInOrderByProtocolordercodeDesc(
//									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject, 1,
//									objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, 1,
//									objuser.getLssitemaster().getSitecode(), 2, objuser.getUsercode(), fromdate, todate,
//									1, objuser.getLssitemaster().getSitecode(), 3, fromdate, todate, userlist));

					mapOrders.put("canceledorder", LSlogilabprotocoldetailRepository
							.countByOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyInAndTeamselectedOrTeamselectedAndProtocolordercodeInAndOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyInOrderByProtocolordercodeDesc(
									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject, 1,
									objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, 1,
									objuser.getLssitemaster().getSitecode(), 2, objuser.getUsercode(), fromdate, todate,
									1, objuser.getLssitemaster().getSitecode(), 3, fromdate, todate, userlist, false,
									true, selectedteamprotcolorderList, 1, objuser.getLssitemaster().getSitecode(), 3,
									fromdate, todate, userlist));

				} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {

//					orders = LSlogilabprotocoldetailRepository
//							.countByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatebyAndCreatedtimestampBetweenAndTestcode(
//									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, testcode, 2,
//									objuser.getLssitemaster().getSitecode(), objuser.getUsercode(), fromdate, todate,
//									testcode);
//
//					orders = orders + LSlogilabprotocoldetailRepository
//							.countByLsprojectmasterInAndCreatedtimestampBetweenAndSitecodeAndTestcode(lstproject,
//									fromdate, todate, objuser.getLssitemaster().getSitecode(), testcode);
//
//					orders = orders + LSlogilabprotocoldetailRepository
//							.countByLsprojectmasterInAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndTestcode(
//									lstproject, 3, objuser.getLssitemaster().getSitecode(), fromdate, todate, testcode);

//				mapOrders.put("pendingorder", LSlogilabprotocoldetailRepository
//						.countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterInAndOrdercancellIsNullAndTestcodeAndRejectedIsNullOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndTestcodeAndRejectedIsNullOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndTestcodeAndRejectedIsNullOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterInAndViewoptionAndCreatedtimestampBetweenAndTestcodeAndRejectedIsNullOrderByProtocolordercodeDesc(
//								"N", objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject, testcode, 
//								"N", objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, testcode, 
//								"N", objuser.getLssitemaster().getSitecode(), 2, fromdate, objuser.getUsercode(), todate, testcode, 
//								"N", objuser.getLssitemaster().getSitecode(), lstproject, 3, fromdate, todate, testcode));

					pendingOrders = LSlogilabprotocoldetailRepository
							.countByOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndTestcodeAndRejectedIsNullOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndTestcodeAndRejectedIsNull(
									"N", objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, testcode, "N",
									objuser.getLssitemaster().getSitecode(), 2, fromdate, objuser.getUsercode(), todate,
									testcode);

					pendingOrders = pendingOrders + LSlogilabprotocoldetailRepository
							.countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterInAndOrdercancellIsNullAndTestcodeAndRejectedIsNull(
									"N", objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject,
									testcode);

					pendingOrders = pendingOrders + LSlogilabprotocoldetailRepository
							.countByOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterInAndViewoptionAndCreatedtimestampBetweenAndTestcodeAndRejectedIsNull(
									"N", objuser.getLssitemaster().getSitecode(), lstproject, 3, fromdate, todate,
									testcode);

//				mapOrders.put("completedorder", LSlogilabprotocoldetailRepository
//						.countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndRejectedIsNullAndTestcodeOrOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndTestcodeOrOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterInAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrderByProtocolordercodeDesc(
//								"R", objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject,
//								testcode, "R", objuser.getLssitemaster().getSitecode(), 1, fromdate, todate,
//								testcode, "R", objuser.getLssitemaster().getSitecode(), 2, objuser.getUsercode(),
//								fromdate, todate, testcode, "R", objuser.getLssitemaster().getSitecode(),
//								lstproject, 3, fromdate, todate, testcode));

					completedOrders = LSlogilabprotocoldetailRepository
							.countByOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndTestcode(
									"R", objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, testcode, "R",
									objuser.getLssitemaster().getSitecode(), 2, objuser.getUsercode(), fromdate, todate,
									testcode);

					completedOrders = completedOrders + LSlogilabprotocoldetailRepository
							.countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndRejectedIsNullAndTestcode(
									"R", objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject,
									testcode);

					completedOrders = completedOrders + LSlogilabprotocoldetailRepository
							.countByOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterInAndViewoptionAndCreatedtimestampBetweenAndTestcode(
									"R", objuser.getLssitemaster().getSitecode(), lstproject, 3, fromdate, todate,
									testcode);

//				mapOrders.put("rejectedorder", LSlogilabprotocoldetailRepository
//						.countByRejectedAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndTestcodeOrRejectedAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrRejectedAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndTestcodeOrRejectedAndSitecodeAndLsprojectmasterInAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrderByProtocolordercodeDesc(
//								1, objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject, testcode,
//								1, objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, testcode, 1,
//								objuser.getLssitemaster().getSitecode(), 2, objuser.getUsercode(), fromdate, todate,
//								testcode, 1, objuser.getLssitemaster().getSitecode(), lstproject, 3, fromdate,
//								todate, testcode));

					rejectedOrders = LSlogilabprotocoldetailRepository
							.countByRejectedAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrRejectedAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndTestcode(
									1, objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, testcode, 1,
									objuser.getLssitemaster().getSitecode(), 2, objuser.getUsercode(), fromdate, todate,
									testcode);

					rejectedOrders = rejectedOrders + LSlogilabprotocoldetailRepository
							.countByRejectedAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndTestcode(1,
									objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject, testcode);

					rejectedOrders = rejectedOrders + LSlogilabprotocoldetailRepository
							.countByRejectedAndSitecodeAndLsprojectmasterInAndViewoptionAndCreatedtimestampBetweenAndTestcode(
									1, objuser.getLssitemaster().getSitecode(), lstproject, 3, fromdate, todate,
									testcode);

//				mapOrders.put("canceledorder", LSlogilabprotocoldetailRepository
//						.countByOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndTestcodeOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndTestcodeOrOrdercancellAndSitecodeAndLsprojectmasterInAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrderByProtocolordercodeDesc(
//								1, objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject, testcode,
//								1, objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, testcode, 1,
//								objuser.getLssitemaster().getSitecode(), 2, objuser.getUsercode(), fromdate, todate,
//								testcode, 1, objuser.getLssitemaster().getSitecode(), lstproject, 3, fromdate,
//								todate, testcode));

					cancelledOrders = LSlogilabprotocoldetailRepository
							.countByOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndTestcode(
									1, objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, testcode, 1,
									objuser.getLssitemaster().getSitecode(), 2, objuser.getUsercode(), fromdate, todate,
									testcode);

					cancelledOrders = cancelledOrders + LSlogilabprotocoldetailRepository
							.countByOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndTestcode(1,
									objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject, testcode);

					cancelledOrders = cancelledOrders + LSlogilabprotocoldetailRepository
							.countByOrdercancellAndSitecodeAndLsprojectmasterInAndViewoptionAndCreatedtimestampBetweenAndTestcode(
									1, objuser.getLssitemaster().getSitecode(), lstproject, 3, fromdate, todate,
									testcode);

					mapOrders.put("orders", orders);
					mapOrders.put("pendingorder", pendingOrders);
					mapOrders.put("completedorder", completedOrders);
					mapOrders.put("rejectedorder", rejectedOrders);
					mapOrders.put("canceledorder", cancelledOrders);

				} else if (testcode == -1 && objuser.getLstprojectforfilter() != null) {
//					mapOrders.put("orders",
//							LSlogilabprotocoldetailRepository
//									.countBySitecodeAndCreatedtimestampBetweenAndLsprojectmaster(
//											objuser.getLssitemaster().getSitecode(), fromdate, todate,
//											objuser.getLstprojectforfilter()));
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
//					mapOrders.put("orders",
//							LSlogilabprotocoldetailRepository
//									.countBySitecodeAndCreatedtimestampBetweenAndLsprojectmasterAndTestcode(
//											objuser.getLssitemaster().getSitecode(), fromdate, todate,
//											objuser.getLstprojectforfilter(), testcode));
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

//					mapOrders.put("orders", LSlogilabprotocoldetailRepository
//							.countByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
//									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, 2,
//									objuser.getUsercode(), fromdate, todate, 3, objuser.getUsercode(), fromdate,
//									todate));
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
//					mapOrders.put("orders", LSlogilabprotocoldetailRepository
//							.countByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndTestcodeOrderByProtocolordercodeDesc(
//									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, testcode, 2,
//									objuser.getUsercode(), fromdate, todate, testcode, 3, objuser.getUsercode(),
//									fromdate, todate, testcode));
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
			AtomicLong lstlimscompleted = new AtomicLong(0);
			AtomicLong lstordersinprogress = new AtomicLong(0);
			AtomicLong lstpending = new AtomicLong(0);
			AtomicLong lstRejected = new AtomicLong(0);
			AtomicLong lstCancelled = new AtomicLong(0);
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

			List<LSSelectedTeam> selectedorders = LSSelectedTeamRepository
					.findByUserteamInAndCreatedtimestampBetween(userteamlist, fromdate, todate);
			List<Long> selectedteambatchCodeList = new ArrayList<>();
			selectedteambatchCodeList = selectedteambatchCodeList.isEmpty() ? Collections.singletonList(-1L)
					: selectedteambatchCodeList;
			if (selectedorders != null && !selectedorders.isEmpty()) {
				selectedteambatchCodeList = selectedorders.stream().map(LSSelectedTeam::getBatchcode)
						.filter(Objects::nonNull).distinct().collect(Collectors.toList());
			}

			long countforsample = 0L;
			int chunkSize = Integer.parseInt(env.getProperty("lssamplecount"));
			int totalSamples = lstsample1.size();
//			List<Logilabordermaster> lstorderobj = new ArrayList<Logilabordermaster>();

			if (lstproject != null && lstproject.size() > 0) {
				if (testcode == -1 && objuser.getLstprojectforfilter() == null) {
					if (lstproject != null && lstproject.size() > 0) {

						lstlimscompleted.addAndGet(lslogilablimsorderdetailRepository
								.countByOrderflagAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndTeamselectedAndApprovelstatusIsNullAndLsprojectmasterIsNullAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullOrOrderflagAndAssignedtoAndCreatedtimestampBetweenOrderByBatchcodeDesc(
										"R", 0, fromdate, todate, "R", 1, objuser, fromdate, todate, 3, "R", 2, objuser,
										fromdate, todate, 3, "R", 1, objuser, fromdate, todate, "R", 2, objuser,
										fromdate, todate, "R", 3, objuser, fromdate, todate, false, "R", objuser,
										objuser, fromdate, todate, "R", objuser, fromdate, todate));
						/* for project team selection */
						lstlimscompleted.addAndGet(LogilablimsorderdetailsRepository
								.countByOrderflagAndTeamselectedAndLsprojectmasterIsNullAndViewoptionAndBatchcodeInAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullAndAssignedtoIsNullOrderByBatchcodeDesc(
										"R", true, 3, selectedteambatchCodeList, fromdate, todate));

						lstlimscompleted.addAndGet(lslogilablimsorderdetailRepository
								.countByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullAndAssignedtoIsNull(
										"R", lstproject, fromdate, todate, 3));

						lstlimscompleted.addAndGet(lslogilablimsorderdetailRepository
								.countByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullAndAssignedtoIsNull(
										"R", lstproject, fromdate, todate));


//						lstpending.addAndGet(LogilablimsorderdetailsRepository
//								.countByOrderflagAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndApprovelstatusNotOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndApprovelstatusIsNull(
//										"N", 0, fromdate, todate, 3, "N", 0, fromdate, todate));
//
//						lstpending.addAndGet(LogilablimsorderdetailsRepository
//								.countByOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenAndAssignedtoIsNullAndApprovelstatusNotOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenAndAssignedtoIsNullAndApprovelstatusIsNull(
//										"N", 1, objuser, fromdate, todate, 3, "N", 1, objuser, fromdate, todate));
//
//						lstpending.addAndGet(LogilablimsorderdetailsRepository
//								.countByOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenAndAssignedtoIsNullAndApprovelstatusNotOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenAndAssignedtoIsNullAndApprovelstatusIsNull(
//										"N", 2, objuser, fromdate, todate, 3, "N", 2, objuser, fromdate, todate));
//
//						lstpending.addAndGet(LogilablimsorderdetailsRepository
//								.countByOrderflagAndLsprojectmasterIsNullAndViewoptionAndOrdercancellIsNullAndCreatedtimestampBetweenAndLsuserMasterInAndAssignedtoIsNullAndApprovelstatusNotOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndOrdercancellIsNullAndCreatedtimestampBetweenAndLsuserMasterInAndAssignedtoIsNullAndApprovelstatusIsNull(
//										"N", 3, fromdate, todate, objuser.getUsernotify(), 3, "N", 3, fromdate, todate,
//										objuser.getUsernotify()));
//
//						lstpending.addAndGet(LogilablimsorderdetailsRepository
//								.countByOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndApprovelstatusNotOrOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndApprovelstatusIsNull(
//										"N", objuser, objuser, fromdate, todate, 3, "N", objuser, objuser, fromdate,
//										todate));
//
//						lstpending.addAndGet(LogilablimsorderdetailsRepository
//								.countByOrderflagAndAssignedtoAndCreatedtimestampBetweenAndApprovelstatusNotOrOrderflagAndAssignedtoAndCreatedtimestampBetweenAndApprovelstatusIsNull(
//										"N", objuser, fromdate, todate, 3, "N", objuser, fromdate, todate));
//
//						lstpending.addAndGet(LogilablimsorderdetailsRepository
//								.countByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndOrdercancellIsNullAndAssignedtoIsNullAndApprovelstatusNotOrOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndOrdercancellIsNullAndAssignedtoIsNullAndApprovelstatusIsNull(
//										"N", lstproject, fromdate, todate, 3, "N", lstproject, fromdate, todate));
//
//						lstpending.addAndGet(LogilablimsorderdetailsRepository
//								.countByOrderflagAndLsprojectmasterInAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenAndAssignedtoIsNullAndApprovelstatusNotOrOrderflagAndLsprojectmasterInAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenAndAssignedtoIsNullAndApprovelstatusIsNull(
//										"N", lstproject, 3, objuser, fromdate, todate, 3, "N", lstproject, 3, objuser,
//										fromdate, todate));
						lstpending.addAndGet(
								lslogilablimsorderdetailRepository.countLSlogilablimsorderdetaildashboardforpending("N",
										0, fromdate, todate, objuser, 1, 2, 3, objuser.getUsernotify(),
										objuser.getLssitemaster(), 3, selectedteambatchCodeList));
//						System.out.println(kumu);

						lstRejected.addAndGet(lslogilablimsorderdetailRepository
								.countByLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusAndAssignedtoIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusAndAssignedtoIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusAndAssignedtoIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusAndLsprojectmasterIsNullAndAssignedtoIsNullOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndApprovelstatusOrAssignedtoAndCreatedtimestampBetweenAndApprovelstatus(
										1, objuser, fromdate, todate, 3, 2, objuser, fromdate, todate, 3, 3, objuser,
										fromdate, todate, 3, 3, objuser, fromdate, todate, 3, objuser, objuser,
										fromdate, todate, 3, objuser, fromdate, todate, 3));
						lstRejected.addAndGet(lslogilablimsorderdetailRepository
								.countByApprovelstatusAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNull(
										3, lstproject, fromdate, todate));

						lstCancelled.addAndGet(lslogilablimsorderdetailRepository
								.countByOrdercancellAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndAssignedtoIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndAssignedtoIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndTeamselectedAndLsprojectmasterIsNullAndAssignedtoIsNullOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndOrdercancellOrAssignedtoAndCreatedtimestampBetweenAndOrdercancell(
										1, lstproject, fromdate, todate, 1, objuser, fromdate, todate, 1, 2, objuser,
										fromdate, todate, 1, 3, objuser, fromdate, todate, 1, false, objuser, objuser,
										fromdate, todate, 1, objuser, fromdate, todate, 1));
						/* for project team selection */
						lstCancelled.addAndGet(LogilablimsorderdetailsRepository
								.countByLsprojectmasterIsNullAndViewoptionAndTeamselectedAndBatchcodeInAndCreatedtimestampBetweenAndOrdercancellAndLsprojectmasterIsNullAndAssignedtoIsNull(
										3, true, selectedteambatchCodeList, fromdate, todate, 1));

						long countcompleted = lslogilablimsorderdetailRepository
								.getLSlogilablimsorderdetaildashboardforcount("R", 3, fromdate, todate, objuser,
										objuser.getLssitemaster());
						lstlimscompleted.addAndGet(countcompleted);
//						lstpending.addAndGet(
//								lslogilablimsorderdetailRepository.getLSlogilablimsorderdetaildashboardforpendingcount(
//										"N", objuser.getLssitemaster(), fromdate, todate, objuser));
						lstRejected.addAndGet(
								lslogilablimsorderdetailRepository.getLSlogilablimsorderdetaildashboardforrejectcount(3,
										fromdate, todate, objuser, objuser.getLssitemaster()));

					}
				} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {
					if (lstproject != null && lstproject.size() > 0) {

						lstlimscompleted.addAndGet(lslogilablimsorderdetailRepository
								.countByOrderflagAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndLsprojectmasterInAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndLsprojectmasterIsNullAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndTestcodeOrOrderflagAndAssignedtoAndCreatedtimestampBetweenAndTestcode(
										"R", 0, fromdate, todate, testcode, "R", 1, objuser, fromdate, todate, 3,
										testcode, "R", 2, objuser, fromdate, todate, 3, testcode, "R", 3, objuser,
										fromdate, todate, 3, lstproject, testcode, "R", 1, objuser, fromdate, todate,
										testcode, "R", 2, objuser, fromdate, todate, testcode, "R", 3, objuser,
										fromdate, todate, testcode, "R", objuser, objuser, fromdate, todate, testcode,
										"R", objuser, fromdate, todate, testcode));

						lstlimscompleted.addAndGet(lslogilablimsorderdetailRepository
								.countByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullAndAssignedtoIsNullAndTestcode(
										"R", lstproject, fromdate, todate, 3, testcode));

						lstpending.addAndGet(lslogilablimsorderdetailRepository
								.countByOrderflagAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterInAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndOrdercancellIsNullAndCreatedtimestampBetweenAndLsuserMasterInAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndTestcodeOrOrderflagAndAssignedtoAndCreatedtimestampBetweenAndTestcode(
										"N", 0, fromdate, todate, testcode, "N", 1, objuser, fromdate, todate, testcode,
										"N", 2, objuser, fromdate, todate, testcode, "N", lstproject, 3, objuser,
										fromdate, todate, testcode, "N", 3, fromdate, todate, objuser.getUsernotify(),
										testcode, "N", objuser, objuser, fromdate, todate, testcode, "N", objuser,
										fromdate, todate, testcode));

						lstpending.addAndGet(lslogilablimsorderdetailRepository
								.countByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndOrdercancellIsNullAndAssignedtoIsNullAndTestcode(
										"N", lstproject, fromdate, todate, testcode));

						lstRejected.addAndGet(lslogilablimsorderdetailRepository
								.countByLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusAndAssignedtoIsNullAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusAndAssignedtoIsNullAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusAndAssignedtoIsNullAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusAndLsprojectmasterIsNullAndAssignedtoIsNullAndTestcodeOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndApprovelstatusAndTestcodeOrAssignedtoAndCreatedtimestampBetweenAndApprovelstatusAndTestcode(
										1, objuser, fromdate, todate, 3, testcode, 2, objuser, fromdate, todate, 3,
										testcode, 3, objuser, fromdate, todate, 3, testcode, 3, objuser, fromdate,
										todate, 3, testcode, objuser, objuser, fromdate, todate, 3, testcode, objuser,
										fromdate, todate, 3, testcode));

						lstRejected.addAndGet(lslogilablimsorderdetailRepository
								.countByApprovelstatusAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullAndTestcode(
										3, lstproject, fromdate, todate, testcode));

						lstCancelled.addAndGet(lslogilablimsorderdetailRepository
								.countByOrdercancellAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndAssignedtoIsNullAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndAssignedtoIsNullAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndLsprojectmasterIsNullAndAssignedtoIsNullAndTestcodeOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndOrdercancellAndTestcodeOrAssignedtoAndCreatedtimestampBetweenAndOrdercancellAndTestcode(
										1, lstproject, fromdate, todate, testcode, 1, objuser, fromdate, todate, 1,
										testcode, 2, objuser, fromdate, todate, 1, testcode, 3, objuser, fromdate,
										todate, 1, testcode, objuser, objuser, fromdate, todate, 1, testcode, objuser,
										fromdate, todate, 1, testcode));

						lstlimscompleted.addAndGet(lslogilablimsorderdetailRepository
								.getLSlogilablimsorderdetaildashboardforcompletecountfilter("R", 3, fromdate, todate,
										objuser, objuser.getLssitemaster(), testcode));
						lstpending.addAndGet(lslogilablimsorderdetailRepository
								.getLSlogilablimsorderdetaildashboardforpendingcountfilter("N",
										objuser.getLssitemaster(), fromdate, todate, objuser, testcode));
						lstRejected.addAndGet(lslogilablimsorderdetailRepository
								.getLSlogilablimsorderdetaildashboardforrejectcountfilter(3, fromdate, todate, objuser,
										objuser.getLssitemaster(), testcode));

					}
				} else if (testcode == -1 && objuser.getLstprojectforfilter() != null) {

					lstlimscompleted.addAndGet(lslogilablimsorderdetailRepository
							.countByOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndLsprojectmasterOrOrderflagAndAssignedtoAndCreatedtimestampBetweenAndLsprojectmasterOrderByBatchcodeDesc(
									"R", objuser.getLstprojectforfilter(), fromdate, todate, 3, "R",
									objuser.getLstprojectforfilter(), fromdate, todate, "R", 0, fromdate, todate,
									objuser.getLstprojectforfilter(), "R", objuser, objuser, fromdate, todate,
									objuser.getLstprojectforfilter(), "R", objuser, fromdate, todate,
									objuser.getLstprojectforfilter()));
					;

					lstpending.addAndGet(lslogilablimsorderdetailRepository
							.countByOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterAndAssignedtoIsNullOrOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndLsprojectmasterOrOrderflagAndAssignedtoAndCreatedtimestampBetweenAndLsprojectmasterOrderByBatchcodeDesc(
									"N", objuser.getLstprojectforfilter(), fromdate, todate, "N", 0, fromdate, todate,
									objuser.getLstprojectforfilter(), "N", objuser, objuser, fromdate, todate,
									objuser.getLstprojectforfilter(), "N", objuser, fromdate, todate,
									objuser.getLstprojectforfilter()));

					lstRejected.addAndGet(lslogilablimsorderdetailRepository
							.countByApprovelstatusAndLsprojectmasterAndCreatedtimestampBetweenAndAssignedtoIsNullOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndApprovelstatusAndLsprojectmasterOrAssignedtoAndCreatedtimestampBetweenAndApprovelstatusAndLsprojectmaster(
									3, objuser.getLstprojectforfilter(), fromdate, todate, objuser, objuser, fromdate,
									todate, 3, objuser.getLstprojectforfilter(), objuser, fromdate, todate, 3,
									objuser.getLstprojectforfilter()));

					lstCancelled.addAndGet(lslogilablimsorderdetailRepository
							.countByOrdercancellAndLsprojectmasterAndCreatedtimestampBetweenAndAssignedtoIsNullOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndOrdercancellAndLsprojectmasterOrAssignedtoAndCreatedtimestampBetweenAndOrdercancellAndLsprojectmaster(
									1, objuser.getLstprojectforfilter(), fromdate, todate, objuser, objuser, fromdate,
									todate, 1, objuser.getLstprojectforfilter(), objuser, fromdate, todate, 1,
									objuser.getLstprojectforfilter()));

				} else {
//					lstUserorder = lslogilablimsorderdetailRepository
//							.countByLsprojectmasterAndCreatedtimestampBetweenAndTestcodeAndAssignedtoIsNullOrFiletypeAndLsprojectmasterAndCreatedtimestampBetweenAndTestcodeAndAssignedtoIsNullOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndTestcodeAndLsprojectmasterOrAssignedtoAndCreatedtimestampBetweenAndTestcodeAndLsprojectmasterOrderByBatchcodeDesc(
//									objuser.getLstprojectforfilter(), fromdate, todate, testcode, 0,
//									objuser.getLstprojectforfilter(), fromdate, todate, testcode, objuser, objuser,
//									fromdate, todate, testcode, objuser.getLstprojectforfilter(), objuser, fromdate,
//									todate, testcode, objuser.getLstprojectforfilter());
//							.countByLsprojectmasterAndCreatedtimestampBetweenAndTestcodeOrFiletypeAndLsprojectmasterAndCreatedtimestampBetweenAndTestcodeOrderByBatchcodeDesc(
//									objuser.getLstprojectforfilter(), fromdate, todate, testcode, 0,
//									objuser.getLstprojectforfilter(), fromdate, todate, testcode);
					lstlimscompleted.addAndGet(lslogilablimsorderdetailRepository
							.countByOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndApprovelstatusNotAndTestcodeAndOrdercancellIsNullOrOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndTestcodeAndOrdercancellIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterAndTestcodeAndOrdercancellIsNullOrOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndLsprojectmasterAndTestcodeOrOrderflagAndAssignedtoAndCreatedtimestampBetweenAndLsprojectmasterAndTestcodeOrderByBatchcodeDesc(
									"R", objuser.getLstprojectforfilter(), fromdate, todate, 3, testcode, "R",
									objuser.getLstprojectforfilter(), fromdate, todate, testcode, "R", 0, fromdate,
									todate, objuser.getLstprojectforfilter(), testcode, "R", objuser, objuser, fromdate,
									todate, objuser.getLstprojectforfilter(), testcode, "R", objuser, fromdate, todate,
									objuser.getLstprojectforfilter(), testcode));

//							.countByOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndApprovelstatusNotAndTestcodeAndOrdercancellIsNullOrOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndTestcodeAndOrdercancellIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterAndTestcodeAndOrdercancellIsNullOrderByBatchcodeDesc(
//									"R", objuser.getLstprojectforfilter(), fromdate, todate, 3, testcode, "R",
//									objuser.getLstprojectforfilter(), fromdate, todate, testcode, "R", 0, fromdate,
//									todate, objuser.getLstprojectforfilter(), testcode);
//					lstordersinprogress = lslogilablimsorderdetailRepository
//							.countByOrderflagAndLsprojectmasterAndApprovelstatusAndApprovedAndCreatedtimestampBetweenOrderByBatchcodeDesc(
//									"N", objuser.getLstprojectforfilter(), 1, 1, fromdate, todate);
//					lstpending = lslogilablimsorderdetailRepository
//							.countByOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndOrdercancellIsNullAndTestcodeAndAssignedtoIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterAndTestcodeAndAssignedtoIsNullOrOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndLsprojectmasterAndTestcodeOrOrderflagAndAssignedtoAndCreatedtimestampBetweenAndLsprojectmasterAndTestcodeOrderByBatchcodeDesc(
//									"N", objuser.getLstprojectforfilter(), fromdate, todate, testcode, "N", 0, fromdate,
//									todate, objuser.getLstprojectforfilter(), testcode, "N", objuser, objuser, fromdate,
//									todate, objuser.getLstprojectforfilter(), testcode, "N", objuser, fromdate, todate,
//									objuser.getLstprojectforfilter(), testcode);

					lstpending.addAndGet(lslogilablimsorderdetailRepository
							.countByOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndOrdercancellIsNullAndTestcodeAndAssignedtoIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterAndTestcodeAndAssignedtoIsNullOrOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndLsprojectmasterAndTestcodeOrOrderflagAndAssignedtoAndCreatedtimestampBetweenAndLsprojectmasterAndTestcodeOrderByBatchcodeDesc(
									"N", objuser.getLstprojectforfilter(), fromdate, todate, testcode, "N", 0, fromdate,
									todate, objuser.getLstprojectforfilter(), testcode, "N", objuser, objuser, fromdate,
									todate, objuser.getLstprojectforfilter(), testcode, "N", objuser, fromdate, todate,
									objuser.getLstprojectforfilter(), testcode));

					lstRejected.addAndGet(lslogilablimsorderdetailRepository
							.countByApprovelstatusAndLsprojectmasterAndCreatedtimestampBetweenAndTestcodeAndAssignedtoIsNullOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndApprovelstatusAndLsprojectmasterAndTestcodeOrAssignedtoAndCreatedtimestampBetweenAndApprovelstatusAndLsprojectmasterAndTestcode(
									3, objuser.getLstprojectforfilter(), fromdate, todate, testcode, objuser, objuser,
									fromdate, todate, 3, objuser.getLstprojectforfilter(), testcode, objuser, fromdate,
									todate, 3, objuser.getLstprojectforfilter(), testcode));

//							.countByApprovelstatusAndLsprojectmasterAndCreatedtimestampBetweenAndTestcode(3,
//									objuser.getLstprojectforfilter(), fromdate, todate, testcode);
					lstCancelled.addAndGet(lslogilablimsorderdetailRepository
							.countByOrdercancellAndLsprojectmasterAndCreatedtimestampBetweenAndTestcodeAndAssignedtoIsNullOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndOrdercancellAndLsprojectmasterAndTestcodeOrAssignedtoAndCreatedtimestampBetweenAndOrdercancellAndLsprojectmasterAndTestcode(
									1, objuser.getLstprojectforfilter(), fromdate, todate, testcode, objuser, objuser,
									fromdate, todate, 1, objuser.getLstprojectforfilter(), testcode, objuser, fromdate,
									todate, 1, objuser.getLstprojectforfilter(), testcode));
				}

//				countforsample = IntStream.range(0, (totalSamples + chunkSize - 1) / chunkSize).parallel()
//						.mapToLong(i -> {
//							int startIndex = i * chunkSize;
//							int endIndex = Math.min(startIndex + chunkSize, totalSamples);
//							List<LSsamplemaster> currentChunk = lstsample1.subList(startIndex, endIndex);
//							Long countobj = 0L;
////							if (testcode == -1 && objuser.getLstprojectforfilter() == null) {
////								lstlimscompleted.addAndGet(lslogilablimsorderdetailRepository
////										.countByOrderflagAndLsprojectmasterIsNullAndLssamplemasterInAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullAndAssignedtoIsNullAndLsuserMasterNotOrderByBatchcodeDesc(
////												"R", currentChunk, fromdate, todate, objuser));
////								lstlimscompleted.addAndGet(lslogilablimsorderdetailRepository
////										.countByOrderflagAndLsprojectmasterIsNullAndLssamplemasterInAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullAndAssignedtoIsNullAndLsuserMasterNot(
////												"R", currentChunk, fromdate, todate, 3, objuser));
////
////								lstpending.addAndGet(lslogilablimsorderdetailRepository
////										.countByOrderflagAndLsprojectmasterIsNullAndLssamplemasterInAndCreatedtimestampBetweenAndOrdercancellIsNullAndAssignedtoIsNullAndLsuserMasterNotOrderByBatchcodeDesc(
////												"N", currentChunk, fromdate, todate, objuser));
////								lstRejected.addAndGet(lslogilablimsorderdetailRepository
////										.countByApprovelstatusAndLsprojectmasterIsNullAndLssamplemasterInAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsuserMasterNot(
////												3, currentChunk, fromdate, todate, objuser));
////							} else
//								if (testcode != -1 && objuser.getLstprojectforfilter() == null) {
//								lstlimscompleted.addAndGet(lslogilablimsorderdetailRepository
//										.countByOrderflagAndLsprojectmasterIsNullAndLssamplemasterInAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeAndLsuserMasterNot(
//												"R", currentChunk, fromdate, todate, 3, testcode, objuser));
//
//								lstlimscompleted.addAndGet(lslogilablimsorderdetailRepository
//										.countByOrderflagAndLsprojectmasterIsNullAndLssamplemasterInAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeAndLsuserMasterNot(
//												"R", currentChunk, fromdate, todate, testcode, objuser));
//								lstpending.addAndGet(lslogilablimsorderdetailRepository
//										.countByOrderflagAndLsprojectmasterIsNullAndLssamplemasterInAndCreatedtimestampBetweenAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeAndLsuserMasterNot(
//												"N", currentChunk, fromdate, todate, testcode, objuser));
//								lstRejected.addAndGet(lslogilablimsorderdetailRepository
//										.countByApprovelstatusAndLsprojectmasterIsNullAndLssamplemasterInAndCreatedtimestampBetweenAndAssignedtoIsNullAndTestcodeAndLsuserMasterNot(
//												3, currentChunk, fromdate, todate, testcode, objuser));
//							}
//							return countobj;
//						}).sum();
			} else {
//				List<LSsamplemaster> lstsample1 = lssamplemasterrepository
//				.findByLssitemasterAndStatus(objuser.getLssitemaster(), 1);
				List<LSSheetOrderStructure> lstdir = lsSheetOrderStructureRepository
						.findBySitemasterAndViewoptionOrCreatedbyAndViewoptionOrderByDirectorycode(
								objuser.getLssitemaster(), 1, objuser, 2);
				List<Long> directorycode = lstdir.stream().map(LSSheetOrderStructure::getDirectorycode)
						.collect(Collectors.toList());
				if (testcode == -1 && objuser.getLstprojectforfilter() == null) {
//			lstUserorder = lslogilablimsorderdetailRepository
//					.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterOrderByBatchcodeDesc(
//							lstsample1, 1, fromdate, todate, lstsample1, 2, fromdate, todate, objuser, 3,
//							fromdate, todate, objuser, directorycode, 1, fromdate, todate, directorycode, 2,
//							fromdate, todate, objuser, directorycode, 3, fromdate, todate, objuser);
//			lstlimscompleted = lslogilablimsorderdetailRepository
//					.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullOrderByBatchcodeDesc(
//							lstsample1, 1, fromdate, todate, "R", 3, lstsample1, 1, fromdate, todate, "R",
//							lstsample1, 2, fromdate, todate, objuser, "R", 3, lstsample1, 2, fromdate, todate,
//							objuser, "R", 3, fromdate, todate, objuser, "R", 3, 3, fromdate, todate, objuser,
//							"R", directorycode, 1, fromdate, todate, "R", 3, directorycode, 1, fromdate, todate,
//							"R", directorycode, 2, fromdate, todate, objuser, "R", 3, directorycode, 2,
//							fromdate, todate, objuser, "R", directorycode, 3, fromdate, todate, objuser, "R", 3,
//							directorycode, 3, fromdate, todate, objuser, "R");

					// existing
//					lstlimscompleted.addAndGet(lslogilablimsorderdetailRepository
//							.countByViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNull(
//									3, fromdate, todate, objuser, "R", 3, 3, fromdate, todate, objuser, "R",
//									directorycode, 1, fromdate, todate, "R", 3, directorycode, 1, fromdate, todate, "R",
//									directorycode, 2, fromdate, todate, objuser, "R", 3, directorycode, 2, fromdate,
//									todate, objuser, "R", directorycode, 3, fromdate, todate, objuser, "R", 3,
//									directorycode, 3, fromdate, todate, objuser, "R"));

					lstlimscompleted.addAndGet(lslogilablimsorderdetailRepository
							.countByViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullAndTeamselectedOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullAndTeamselected(
									3, fromdate, todate, objuser, "R", 3, 3, fromdate, todate, objuser, "R", false,
									directorycode, 1, fromdate, todate, "R", 3, directorycode, 1, fromdate, todate, "R",
									directorycode, 2, fromdate, todate, objuser, "R", 3, directorycode, 2, fromdate,
									todate, objuser, "R", directorycode, 3, fromdate, todate, objuser, "R", 3,
									directorycode, 3, fromdate, todate, objuser, "R", false));

					lstlimscompleted.addAndGet(LogilablimsorderdetailsRepository
							.countByViewoptionAndTeamselectedAndBatchcodeInAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndTeamselectedAndBatchcodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNull(
									3, true, selectedteambatchCodeList, fromdate, todate, objuser, "R", directorycode,
									true, selectedteambatchCodeList, 3, fromdate, todate, objuser, "R"));
//			lstordersinprogress = lslogilablimsorderdetailRepository
//					.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullAndApprovelstatusAndApprovedOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndApprovelstatusAndApprovedOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndApprovelstatusAndApprovedOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullAndApprovelstatusAndApprovedOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndApprovelstatusAndApprovedOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndApprovelstatusAndApprovedOrderByBatchcodeDesc(
//							lstsample1, 1, fromdate, todate, "N", 1, 1, lstsample1, 2, fromdate, todate,
//							objuser, "N", 1, 1, 3, fromdate, todate, objuser, "N", 1, 1, directorycode, 1,
//							fromdate, todate, "N", 1, 1, directorycode, 2, fromdate, todate, objuser, "N", 1, 1,
//							directorycode, 3, fromdate, todate, objuser, "N", 1, 1);

//			lstpending = lslogilablimsorderdetailRepository
//					.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullOrderByBatchcodeDesc(
//							lstsample1, 1, fromdate, todate, "N", lstsample1, 2, fromdate, todate, objuser, "N",
//							3, fromdate, todate, objuser, "N", directorycode, 1, fromdate, todate, "N",
//							directorycode, 2, fromdate, todate, objuser, "N", directorycode, 3, fromdate,
//							todate, objuser, "N");
					// existing
//					lstpending.addAndGet(lslogilablimsorderdetailRepository
//							.countByViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullOrderByBatchcodeDesc(
//									3, fromdate, todate, objuser, "N", directorycode, 1, fromdate, todate, "N",
//									directorycode, 2, fromdate, todate, objuser, "N", directorycode, 3, fromdate,
//									todate, objuser, "N"));

					lstpending.addAndGet(lslogilablimsorderdetailRepository
							.countByViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndTeamselectedOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndTeamselectedOrViewoptionAndTeamselectedAndBatchcodeInAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndTeamselectedAndBatchcodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullOrderByBatchcodeDesc(
									3, fromdate, todate, objuser, "N", false, directorycode, 1, fromdate, todate, "N",
									directorycode, 2, fromdate, todate, objuser, "N", directorycode, 3, fromdate,
									todate, objuser, "N", false, 3, true, selectedteambatchCodeList, fromdate, todate,
									objuser, "N", directorycode, true, selectedteambatchCodeList, 3, fromdate, todate,
									objuser, "N"));

//			lstRejected = lslogilablimsorderdetailRepository
//					.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndApprovelstatusOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndApprovelstatusOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusOrderByBatchcodeDesc(
//							lstsample1, 1, fromdate, todate, 3, lstsample1, 2, fromdate, todate, objuser, 3, 3,
//							fromdate, todate, objuser, 3, directorycode, 1, fromdate, todate, 3, directorycode,
//							2, fromdate, todate, objuser, 3, directorycode, 3, fromdate, todate, objuser, 3);
					lstRejected.addAndGet(lslogilablimsorderdetailRepository
							.countByViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndApprovelstatusOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusOrderByBatchcodeDesc(
									3, fromdate, todate, objuser, 3, directorycode, 1, fromdate, todate, 3,
									directorycode, 2, fromdate, todate, objuser, 3, directorycode, 3, fromdate, todate,
									objuser, 3));

//			lstCancelled = lslogilablimsorderdetailRepository
//					.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrdercancellOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrdercancellOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellOrderByBatchcodeDesc(
//							lstsample1, 1, fromdate, todate, 1, lstsample1, 2, fromdate, todate, objuser, 1, 3,
//							fromdate, todate, objuser, 1, directorycode, 1, fromdate, todate, 1, directorycode,
//							2, fromdate, todate, objuser, 1, directorycode, 3, fromdate, todate, objuser, 1);
					// existing
//					lstCancelled.addAndGet(lslogilablimsorderdetailRepository
//							.countByViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrdercancellOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellOrderByBatchcodeDesc(
//									3, fromdate, todate, objuser, 1, directorycode, 1, fromdate, todate, 1,
//									directorycode, 2, fromdate, todate, objuser, 1, directorycode, 3, fromdate, todate,
//									objuser, 1));

					lstCancelled.addAndGet(lslogilablimsorderdetailRepository
							.countByViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellAndTeamselectedOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrdercancellOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellAndTeamselectedOrderByBatchcodeDesc(
									3, fromdate, todate, objuser, 1, false, directorycode, 1, fromdate, todate, 1,
									directorycode, 2, fromdate, todate, objuser, 1, directorycode, 3, fromdate, todate,
									objuser, 1, false));

					lstCancelled.addAndGet(LogilablimsorderdetailsRepository
							.countByViewoptionAndTeamselectedAndBatchcodeInAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellOrLsprojectmasterIsNullAndDirectorycodeInAndTeamselectedAndBatchcodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellOrderByBatchcodeDesc(
									3, true, selectedteambatchCodeList, fromdate, todate, objuser, 1, directorycode,
									true, selectedteambatchCodeList, 3, fromdate, todate, objuser, 1));

				} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {

//			lstUserorder = lslogilablimsorderdetailRepository
//					.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTestcodeOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTestcodeOrderByBatchcodeDesc(
//							lstsample1, 1, fromdate, todate, testcode, lstsample1, 2, fromdate, todate, objuser,
//							testcode, 3, fromdate, todate, objuser, testcode, directorycode, 1, fromdate,
//							todate, testcode, directorycode, 2, fromdate, todate, objuser, testcode,
//							directorycode, 3, fromdate, todate, objuser, testcode);
//					lstUserorder = lslogilablimsorderdetailRepository
//							.countByViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTestcode(
//									3, fromdate, todate, objuser, testcode, directorycode, 1, fromdate, todate,
//									testcode, directorycode, 2, fromdate, todate, objuser, testcode, directorycode, 3,
//									fromdate, todate, objuser, testcode);
//					lstUserorder = lstUserorder + lslogilablimsorderdetailRepository
//							.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndTestcodeAndDirectorycodeIsNull(
//									lstsample1, 1, fromdate, todate, testcode);
//					lstUserorder = lstUserorder + lslogilablimsorderdetailRepository
//							.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTestcodeAndDirectorycodeIsNull(
//									lstsample1, 2, fromdate, todate, objuser, testcode);

//			lstlimscompleted = lslogilablimsorderdetailRepository
//					.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullAndTestcodeOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullAndTestcodeOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullAndTestcodeOrderByBatchcodeDesc(
//							lstsample1, 1, fromdate, todate, "R", 3, testcode, lstsample1, 1, fromdate, todate,
//							"R", testcode, lstsample1, 2, fromdate, todate, objuser, "R", 3, testcode,
//							lstsample1, 2, fromdate, todate, objuser, "R", testcode, 3, fromdate, todate,
//							objuser, "R", 3, testcode, 3, fromdate, todate, objuser, "R", testcode,
//							directorycode, 1, fromdate, todate, "R", 3, testcode, directorycode, 1, fromdate,
//							todate, "R", testcode, directorycode, 2, fromdate, todate, objuser, "R", 3,
//							testcode, directorycode, 2, fromdate, todate, objuser, "R", testcode, directorycode,
//							3, fromdate, todate, objuser, "R", 3, testcode, directorycode, 3, fromdate, todate,
//							objuser, "R", testcode);
					lstlimscompleted.addAndGet(lslogilablimsorderdetailRepository
							.countByViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullAndTestcodeOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullAndTestcode(
									3, fromdate, todate, objuser, "R", 3, testcode, 3, fromdate, todate, objuser, "R",
									testcode, directorycode, 1, fromdate, todate, "R", 3, testcode, directorycode, 1,
									fromdate, todate, "R", testcode, directorycode, 2, fromdate, todate, objuser, "R",
									3, testcode, directorycode, 2, fromdate, todate, objuser, "R", testcode,
									directorycode, 3, fromdate, todate, objuser, "R", 3, testcode, directorycode, 3,
									fromdate, todate, objuser, "R", testcode));

//			lstordersinprogress = lslogilablimsorderdetailRepository
//					.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullAndApprovelstatusAndApprovedAndTestcodeOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndApprovelstatusAndApprovedAndTestcodeOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndApprovelstatusAndApprovedAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullAndApprovelstatusAndApprovedAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndApprovelstatusAndApprovedAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndApprovelstatusAndApprovedAndTestcodeOrderByBatchcodeDesc(
//							lstsample1, 1, fromdate, todate, "N", 1, 1, testcode, lstsample1, 2, fromdate,
//							todate, objuser, "N", 1, 1, testcode, 3, fromdate, todate, objuser, "N", 1, 1,
//							testcode, directorycode, 1, fromdate, todate, "N", 1, 1, testcode, directorycode, 2,
//							fromdate, todate, objuser, "N", 1, 1, testcode, directorycode, 3, fromdate, todate,
//							objuser, "N", 1, 1, testcode);

//			lstpending = lslogilablimsorderdetailRepository
//					.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndTestcodeOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndTestcodeOrderByBatchcodeDesc(
//							lstsample1, 1, fromdate, todate, "N", testcode, lstsample1, 2, fromdate, todate,
//							objuser, "N", testcode, 3, fromdate, todate, objuser, "N", testcode, directorycode,
//							1, fromdate, todate, "N", testcode, directorycode, 2, fromdate, todate, objuser,
//							"N", testcode, directorycode, 3, fromdate, todate, objuser, "N", testcode);

//			lstRejected = lslogilablimsorderdetailRepository
//					.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndApprovelstatusAndTestcodeOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusAndTestcodeOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndApprovelstatusAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusAndTestcodeOrderByBatchcodeDesc(
//							lstsample1, 1, fromdate, todate, 3, testcode, lstsample1, 2, fromdate, todate,
//							objuser, 3, testcode, 3, fromdate, todate, objuser, 3, testcode, directorycode, 1,
//							fromdate, todate, 3, testcode, directorycode, 2, fromdate, todate, objuser, 3,
//							testcode, directorycode, 3, fromdate, todate, objuser, 3, testcode);

					lstRejected.addAndGet(lslogilablimsorderdetailRepository
							.countByViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndApprovelstatusAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusAndTestcodeOrderByBatchcodeDesc(
									3, fromdate, todate, objuser, 3, testcode, directorycode, 1, fromdate, todate, 3,
									testcode, directorycode, 2, fromdate, todate, objuser, 3, testcode, directorycode,
									3, fromdate, todate, objuser, 3, testcode));

//			lstCancelled = lslogilablimsorderdetailRepository
//					.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrdercancellAndTestcodeOrLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellAndTestcodeOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrdercancellAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellAndTestcodeOrderByBatchcodeDesc(
//							lstsample1, 1, fromdate, todate, 1, testcode, lstsample1, 2, fromdate, todate,
//							objuser, 1, testcode, 3, fromdate, todate, objuser, 1, testcode, directorycode, 1,
//							fromdate, todate, 1, testcode, directorycode, 2, fromdate, todate, objuser, 1,
//							testcode, directorycode, 3, fromdate, todate, objuser, 1, testcode);
//					lstCancelled = lslogilablimsorderdetailRepository
//							.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellAndTestcodeOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrdercancellAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellAndTestcodeOrderByBatchcodeDesc(
//									lstsample1, 2, fromdate, todate, objuser, 1, testcode, 3, fromdate, todate, objuser,
//									1, testcode, directorycode, 1, fromdate, todate, 1, testcode, directorycode, 2,
//									fromdate, todate, objuser, 1, testcode, directorycode, 3, fromdate, todate, objuser,
//									1, testcode);

					lstCancelled.addAndGet(lslogilablimsorderdetailRepository
							.countByViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrdercancellAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellAndTestcodeOrderByBatchcodeDesc(
									3, fromdate, todate, objuser, 1, testcode, directorycode, 1, fromdate, todate, 1,
									testcode, directorycode, 2, fromdate, todate, objuser, 1, testcode, directorycode,
									3, fromdate, todate, objuser, 1, testcode));

				}

				countforsample = IntStream.range(0, (totalSamples + chunkSize - 1) / chunkSize).parallel()
						.mapToLong(i -> {
							int startIndex = i * chunkSize;
							int endIndex = Math.min(startIndex + chunkSize, totalSamples);
							List<LSsamplemaster> currentChunk = lstsample1.subList(startIndex, endIndex);
							Long countobj = 0L;
							if (testcode == -1 && objuser.getLstprojectforfilter() == null) {
								lstlimscompleted.addAndGet(lslogilablimsorderdetailRepository
										.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullAndDirectorycodeIsNull(
												currentChunk, 1, fromdate, todate, "R", 3));
								lstlimscompleted.addAndGet(lslogilablimsorderdetailRepository
										.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullAndDirectorycodeIsNull(
												currentChunk, 1, fromdate, todate, "R"));
								lstlimscompleted.addAndGet(lslogilablimsorderdetailRepository
										.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullAndDirectorycodeIsNull(
												currentChunk, 2, fromdate, todate, objuser, "R", 3));
								lstlimscompleted.addAndGet(lslogilablimsorderdetailRepository
										.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullAndDirectorycodeIsNull(
												currentChunk, 2, fromdate, todate, objuser, "R"));
								lstpending.addAndGet(lslogilablimsorderdetailRepository
										.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullAndDirectorycodeIsNullOrderByBatchcodeDesc(
												currentChunk, 1, fromdate, todate, "N"));
								lstpending.addAndGet(lslogilablimsorderdetailRepository
										.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndDirectorycodeIsNullOrderByBatchcodeDesc(
												currentChunk, 2, fromdate, todate, objuser, "N"));
								lstRejected.addAndGet(lslogilablimsorderdetailRepository
										.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndApprovelstatusAndDirectorycodeIsNullOrderByBatchcodeDesc(
												currentChunk, 1, fromdate, todate, 3));

								lstRejected.addAndGet(lslogilablimsorderdetailRepository
										.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusAndDirectorycodeIsNullOrderByBatchcodeDesc(
												currentChunk, 2, fromdate, todate, objuser, 3));
								lstCancelled.addAndGet(lslogilablimsorderdetailRepository
										.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrdercancellAndDirectorycodeIsNullOrderByBatchcodeDesc(
												currentChunk, 1, fromdate, todate, 1));

								lstCancelled.addAndGet(lslogilablimsorderdetailRepository
										.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellAndDirectorycodeIsNullOrderByBatchcodeDesc(
												currentChunk, 2, fromdate, todate, objuser, 1));
							} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {
								lstlimscompleted.addAndGet(lslogilablimsorderdetailRepository
										.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullAndTestcodeAndDirectorycodeIsNull(
												currentChunk, 1, fromdate, todate, "R", 3, testcode));

								lstlimscompleted.addAndGet(lslogilablimsorderdetailRepository
										.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullAndTestcodeAndDirectorycodeIsNull(
												currentChunk, 1, fromdate, todate, "R", testcode));

								lstlimscompleted.addAndGet(lslogilablimsorderdetailRepository
										.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullAndTestcodeAndDirectorycodeIsNull(
												currentChunk, 2, fromdate, todate, objuser, "R", 3, testcode));

								lstlimscompleted.addAndGet(lslogilablimsorderdetailRepository
										.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullAndTestcodeAndDirectorycodeIsNull(
												currentChunk, 2, fromdate, todate, objuser, "R", testcode));

								lstRejected.addAndGet(lslogilablimsorderdetailRepository
										.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndApprovelstatusAndTestcodeAndDirectorycodeIsNullAndLsuserMasterNotOrderByBatchcodeDesc(
												currentChunk, 1, fromdate, todate, 3, testcode, objuser));

								lstRejected.addAndGet(lslogilablimsorderdetailRepository
										.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusAndTestcodeAndDirectorycodeIsNullOrderByBatchcodeDesc(
												currentChunk, 2, fromdate, todate, objuser, 3, testcode));
								lstCancelled.addAndGet(lslogilablimsorderdetailRepository
										.countByLsprojectmasterIsNullAndTestcodeAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrdercancellAndDirectorycodeIsNull(
												1, currentChunk, 1, fromdate, todate, testcode));
							}
							return countobj;
						}).sum();

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

	private Map<String, Object> getallorders(List<LSprojectmaster> lstproject, Date fromdate, Date todate,
			LSuserMaster objuser, Integer testcode, Pageable pageable, List<LSsamplemaster> lstsample1,
			List<LSworkflow> lstworkflow, List<Long> selectedteambatchCodeList) {

		List<LogilabOrdermastersh> lstorders = new ArrayList<LogilabOrdermastersh>();
		List<LogilabOrdermastersh> lstorderobj = new ArrayList<LogilabOrdermastersh>();
		Map<String, Object> mapOrders = new HashMap<>();
		long count = 0;
		int chunkSize = Integer.parseInt(env.getProperty("lssamplecount"));
		int totalSamples = lstsample1.size();
		if (testcode == -1 && objuser.getLstprojectforfilter() == null) {

//			if (env.getProperty("app.datasource.eln.url").toLowerCase().contains("postgres")) {

//				lstorders = lslogilablimsorderdetailRepository
//						.findByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndLsprojectmasterInAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndLsprojectmasterIsNullAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullOrOrderflagAndAssignedtoAndCreatedtimestampBetweenOrOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenAndAssignedtoIsNullOrOrderflagAndLsprojectmasterInAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenAndAssignedtoIsNullOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndOrdercancellIsNullAndCreatedtimestampBetweenAndLsuserMasterInAndAssignedtoIsNullOrOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullOrOrderflagAndAssignedtoAndCreatedtimestampBetweenOrApprovelstatusAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusAndAssignedtoIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusAndAssignedtoIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusAndAssignedtoIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusAndLsprojectmasterIsNullAndAssignedtoIsNullOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndApprovelstatusOrAssignedtoAndCreatedtimestampBetweenAndApprovelstatusOrOrdercancellAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndAssignedtoIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndAssignedtoIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndLsprojectmasterIsNullAndAssignedtoIsNullOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndOrdercancellOrAssignedtoAndCreatedtimestampBetweenAndOrdercancellOrderByBatchcodeDesc(
//								"R", lstproject, fromdate, todate, 3, "R", lstproject, fromdate, todate, "R", 0,
//								fromdate, todate, "R", 1, objuser, fromdate, todate, 3, "R", 2, objuser, fromdate,
//								todate, 3, "R", 3, objuser, fromdate, todate, 3, lstproject, "R", 1, objuser, fromdate,
//								todate, "R", 2, objuser, fromdate, todate, "R", 3, objuser, fromdate, todate, "R",
//								objuser, objuser, fromdate, todate, "R", objuser, fromdate, todate, "N", lstproject,
//								fromdate, todate, "N", 0, fromdate, todate, "N", 1, objuser, fromdate, todate, "N", 2,
//								objuser, fromdate, todate, "N", lstproject, 3, objuser, fromdate, todate, "N", 3,
//								fromdate, todate, objuser.getUsernotify(), "N", objuser, objuser, fromdate, todate, "N",
//								objuser, fromdate, todate, 3, lstproject, fromdate, todate, 1, objuser, fromdate,
//								todate, 3, 2, objuser, fromdate, todate, 3, 3, objuser, fromdate, todate, 3, 3, objuser,
//								fromdate, todate, 3, objuser, objuser, fromdate, todate, 3, objuser, fromdate, todate,
//								3, 1, lstproject, fromdate, todate, 1, objuser, fromdate, todate, 1, 2, objuser,
//								fromdate, todate, 1, 3, objuser, fromdate, todate, 1, objuser, objuser, fromdate,
//								todate, 1, objuser, fromdate, todate, 1, pageable);

//				List<LSlogilablimsorderdetail> lstordersdem = lslogilablimsorderdetailRepository
//						.getLSlogilablimsorderdetaildashboardformaterial("R", 3, fromdate, todate, objuser,
//								objuser.getLssitemaster());
//				lstordersdem.addAll(lslogilablimsorderdetailRepository.getLSlogilablimsorderdetaildashboardformaterial(
//						"N", objuser.getLssitemaster(), fromdate, todate, objuser));
//				lstordersdem.addAll(
//						lslogilablimsorderdetailRepository.getLSlogilablimsorderdetaildashboardforrejectmaterial(3,
//								fromdate, todate, objuser, objuser.getLssitemaster()));

			List<LSlogilablimsorderdetail> lstordersdem = LogilablimsorderdetailsRepository
					.getLSlogilablimsorderdetaildashboardforallorders("N", 0, fromdate, todate, objuser, 1, 2, 3,
							objuser.getUsernotify(), objuser.getLssitemaster(),
							objuser.getPagesize() * objuser.getPageperorder(), objuser.getPageperorder(), 3, "R",
							selectedteambatchCodeList);

//				if(lstordersdem.size()<objuser.getPageperorder()) {
//					Pageable pageableobj = new PageRequest(0, Integer.MAX_VALUE);
//					 lstorders = lslogilablimsorderdetailRepository
//								.findByLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusAndAssignedtoIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusAndAssignedtoIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusAndAssignedtoIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusAndLsprojectmasterIsNullAndAssignedtoIsNullOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndApprovelstatusOrAssignedtoAndCreatedtimestampBetweenAndApprovelstatus(
//										1, objuser, fromdate, todate, 3, 2, objuser, fromdate, todate, 3, 3, objuser,
//										fromdate, todate, 3, 3, objuser, fromdate, todate, 3, objuser, objuser, fromdate,
//										todate, 3, objuser, fromdate, todate, 3, pageableobj);
//
//						lstorders.addAll(lslogilablimsorderdetailRepository
//								.findByApprovelstatusAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNull(3,
//										lstproject, fromdate, todate, pageableobj));
//
//
//						lstorders.addAll(lslogilablimsorderdetailRepository
//								.LsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndAssignedtoIsNullOrOrdercancellAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndAssignedtoIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndLsprojectmasterIsNullAndAssignedtoIsNullOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndOrdercancellOrAssignedtoAndCreatedtimestampBetweenAndOrdercancell(
//										1, objuser, fromdate, todate, 1, 1, lstproject, fromdate, todate, 2, objuser,
//										fromdate, todate, 1, 3, objuser, fromdate, todate, 1, objuser, objuser, fromdate,
//										todate, 1, objuser, fromdate, todate, 1, pageableobj));
//				}

			if (!lstordersdem.isEmpty()) {
				lstorders.addAll(lstordersdem.stream().map(lsOrderDetail -> new LogilabOrdermastersh(
						lsOrderDetail.getBatchcode(), lsOrderDetail.getBatchid(), lsOrderDetail.getLsworkflow(),
						lsOrderDetail.getTestname(), lsOrderDetail.getLsfile(), lsOrderDetail.getLssamplemaster(),
						lsOrderDetail.getLsprojectmaster(), lsOrderDetail.getFiletype(), lsOrderDetail.getOrderflag(),
						lsOrderDetail.getAssignedto(), lsOrderDetail.getCreatedtimestamp(),
						lsOrderDetail.getCompletedtimestamp(), lsOrderDetail.getKeyword(),
						lsOrderDetail.getLstestmasterlocal(), lsOrderDetail.getOrdercancell(),
						lsOrderDetail.getViewoption(), lsOrderDetail.getLsuserMaster(), lsOrderDetail.getTestcode(),
						lsOrderDetail.getApprovelstatus(), lsOrderDetail.getLsordernotification(),
						lsOrderDetail.getOrdersaved(), lsOrderDetail.getRepeat(),
						lsOrderDetail.getLsautoregisterorders(), lsOrderDetail.getSentforapprovel(),
						lsOrderDetail.getApprovelaccept(), lsOrderDetail.getAutoregistercount(),
						lsOrderDetail.getElnmaterial(), lsOrderDetail.getLockedusername(),
						lsOrderDetail.getApplicationsequence(), lsOrderDetail.getSitesequence(),
						lsOrderDetail.getProjectsequence(), lsOrderDetail.getTasksequence(),
						lsOrderDetail.getOrdertypesequence(), lsOrderDetail.getSequenceid(),
						lsOrderDetail.getTeamselected(), lsOrderDetail.getModifieddate()))
						.collect(Collectors.toList()));
			}
//			} else {
//				List<LSlogilablimsorderdetail> lstordersdem = lslogilablimsorderdetailRepository
//						.getLSlogilablimsorderdetaildashboard("R", 0, fromdate, todate, objuser, 3, 1, 2, 3, "N",
//								objuser.getUsernotify(), 1, objuser.getLssitemaster(),
//								objuser.getPagesize() * objuser.getPageperorder(), objuser.getPageperorder());
//				lstorders = lstordersdem.stream()
//						.map(lsOrderDetail -> new LogilabOrdermastersh(lsOrderDetail.getBatchcode(),
//								lsOrderDetail.getBatchid(), lsOrderDetail.getLsworkflow(), lsOrderDetail.getTestname(),
//								lsOrderDetail.getLsfile(), lsOrderDetail.getLssamplemaster(),
//								lsOrderDetail.getLsprojectmaster(), lsOrderDetail.getFiletype(),
//								lsOrderDetail.getOrderflag(), lsOrderDetail.getAssignedto(),
//								lsOrderDetail.getCreatedtimestamp(), lsOrderDetail.getCompletedtimestamp(),
//								lsOrderDetail.getKeyword(), lsOrderDetail.getLstestmasterlocal(),
//								lsOrderDetail.getOrdercancell(), lsOrderDetail.getViewoption(),
//								lsOrderDetail.getLsuserMaster(), lsOrderDetail.getTestcode(),
//								lsOrderDetail.getApprovelstatus(), lsOrderDetail.getLsordernotification(),
//								lsOrderDetail.getOrdersaved(), lsOrderDetail.getRepeat(),
//								lsOrderDetail.getLsautoregisterorders(), lsOrderDetail.getSentforapprovel(),
//								lsOrderDetail.getApprovelaccept(), lsOrderDetail.getAutoregistercount(),
//								lsOrderDetail.getElnmaterial(), lsOrderDetail.getLockedusername()))
//						.collect(Collectors.toList());
//			}

		} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {
//			-----------------completed order ---------------------------------

			lstorders = lslogilablimsorderdetailRepository
					.findByOrderflagAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndLsprojectmasterInAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndLsprojectmasterIsNullAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndTestcodeOrOrderflagAndAssignedtoAndCreatedtimestampBetweenAndTestcodeOrderByBatchcodeDesc(
							"R", 0, fromdate, todate, testcode, "R", 1, objuser, fromdate, todate, 3, testcode, "R", 2,
							objuser, fromdate, todate, 3, testcode, "R", 3, objuser, fromdate, todate, 3, lstproject,
							testcode, "R", 1, objuser, fromdate, todate, testcode, "R", 2, objuser, fromdate, todate,
							testcode, "R", 3, objuser, fromdate, todate, testcode, "R", objuser, objuser, fromdate,
							todate, testcode, "R", objuser, fromdate, todate, testcode, pageable);

			lstorders.addAll(lslogilablimsorderdetailRepository
					.findByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeOrderByBatchcodeDesc(
							"R", lstproject, fromdate, todate, 3, testcode, pageable));

			lstorders.addAll(lslogilablimsorderdetailRepository
					.findByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeOrderByBatchcodeDesc(
							"R", lstproject, fromdate, todate, testcode, pageable));

//			-------------------------------conmpeted order end ----------------------------------------

//			------------------------------ pending orders ----------------------------------------------

			lstorders.addAll(lslogilablimsorderdetailRepository
					.findByOrderflagAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterInAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndOrdercancellIsNullAndCreatedtimestampBetweenAndLsuserMasterInAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndTestcodeOrOrderflagAndAssignedtoAndCreatedtimestampBetweenAndTestcodeOrderByBatchcodeDesc(
							"N", 0, fromdate, todate, testcode, "N", 1, objuser, fromdate, todate, testcode, "N", 2,
							objuser, fromdate, todate, testcode, "N", lstproject, 3, objuser, fromdate, todate,
							testcode, "N", 3, fromdate, todate, objuser.getUsernotify(), testcode, "N", objuser,
							objuser, fromdate, todate, testcode, "N", objuser, fromdate, todate, testcode, pageable));

			lstorders.addAll(lslogilablimsorderdetailRepository
					.findByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeOrderByBatchcodeDesc(
							"N", lstproject, fromdate, todate, testcode, pageable));

//			------------------------------pending orders end ---------------------------
//          ------------------------------ Rejected orders start ----------------------------

			lstorders.addAll(lslogilablimsorderdetailRepository
					.findByLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusAndAssignedtoIsNullAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusAndAssignedtoIsNullAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusAndAssignedtoIsNullAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusAndLsprojectmasterIsNullAndAssignedtoIsNullAndTestcodeOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndApprovelstatusAndTestcodeOrAssignedtoAndCreatedtimestampBetweenAndApprovelstatusAndTestcode(
							1, objuser, fromdate, todate, 3, testcode, 2, objuser, fromdate, todate, 3, testcode, 3,
							objuser, fromdate, todate, 3, testcode, 3, objuser, fromdate, todate, 3, testcode, objuser,
							objuser, fromdate, todate, 3, testcode, objuser, fromdate, todate, 3, testcode, pageable));

			lstorders.addAll(lslogilablimsorderdetailRepository
					.findByApprovelstatusAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullAndTestcode(3,
							lstproject, fromdate, todate, testcode, pageable));

//          ------------------------------ Rejected orders end  ----------------------------

			lstorders.addAll(lslogilablimsorderdetailRepository
					.findByOrdercancellAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndAssignedtoIsNullAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndAssignedtoIsNullAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndLsprojectmasterIsNullAndAssignedtoIsNullAndTestcodeOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndOrdercancellAndTestcodeOrAssignedtoAndCreatedtimestampBetweenAndOrdercancellAndTestcode(
							1, lstproject, fromdate, todate, testcode, 1, objuser, fromdate, todate, 1, testcode, 2,
							objuser, fromdate, todate, 1, testcode, 3, objuser, fromdate, todate, 1, testcode, objuser,
							objuser, fromdate, todate, 1, testcode, objuser, fromdate, todate, 1, testcode, pageable));

			lstorderobj = IntStream.range(0, (totalSamples + chunkSize - 1) / chunkSize).parallel().mapToObj(i -> {
				int startIndex = i * chunkSize;
				int endIndex = Math.min(startIndex + chunkSize, totalSamples);
				List<LSsamplemaster> currentChunk = lstsample1.subList(startIndex, endIndex);

				List<LogilabOrdermastersh> orderChunk = new ArrayList<>();
				orderChunk.addAll(lslogilablimsorderdetailRepository
						.findByApprovelstatusAndLsprojectmasterIsNullAndLssamplemasterInAndCreatedtimestampBetweenAndAssignedtoIsNullAndTestcodeAndLsuserMasterNot(
								3, currentChunk, fromdate, todate, testcode, objuser, pageable));
				orderChunk.addAll(lslogilablimsorderdetailRepository
						.findByOrderflagAndLsprojectmasterIsNullAndLssamplemasterInAndCreatedtimestampBetweenAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeAndLsuserMasterNotOrderByBatchcodeDesc(
								"N", currentChunk, fromdate, todate, testcode, objuser, pageable));
				orderChunk.addAll(lslogilablimsorderdetailRepository
						.findByOrderflagAndLsprojectmasterIsNullAndLssamplemasterInAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeAndLsuserMasterNotOrderByBatchcodeDesc(
								"R", currentChunk, fromdate, todate, 3, testcode, objuser, pageable));

				orderChunk.addAll(lslogilablimsorderdetailRepository
						.findByOrderflagAndLsprojectmasterIsNullAndLssamplemasterInAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeAndLsuserMasterNotOrderByBatchcodeDesc(
								"R", currentChunk, fromdate, todate, testcode, objuser, pageable));
				return orderChunk;
			}).flatMap(List::stream).collect(Collectors.toList());

			lstorders.addAll(lstorderobj);

		} else if (testcode == -1 && objuser.getLstprojectforfilter() != null) {// project filter is choosen

			lstorders = lslogilablimsorderdetailRepository
					.findByOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndLsprojectmasterOrOrderflagAndAssignedtoAndCreatedtimestampBetweenAndLsprojectmasterOrderByBatchcodeDesc(
							"R", objuser.getLstprojectforfilter(), fromdate, todate, 3, "R",
							objuser.getLstprojectforfilter(), fromdate, todate, "R", 0, fromdate, todate,
							objuser.getLstprojectforfilter(), "R", objuser, objuser, fromdate, todate,
							objuser.getLstprojectforfilter(), "R", objuser, fromdate, todate,
							objuser.getLstprojectforfilter(), pageable);
//			count = lslogilablimsorderdetailRepository
//					.countByOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndLsprojectmasterOrOrderflagAndAssignedtoAndCreatedtimestampBetweenAndLsprojectmasterOrderByBatchcodeDesc(
//							"R", objuser.getLstprojectforfilter(), fromdate, todate, 3, "R",
//							objuser.getLstprojectforfilter(), fromdate, todate, "R", 0, fromdate, todate,
//							objuser.getLstprojectforfilter(), "R", objuser, objuser, fromdate, todate,
//							objuser.getLstprojectforfilter(), "R", objuser, fromdate, todate,
//							objuser.getLstprojectforfilter());

			lstorders.addAll(lslogilablimsorderdetailRepository
					.findByOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterAndAssignedtoIsNullOrOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndLsprojectmasterOrOrderflagAndAssignedtoAndCreatedtimestampBetweenAndLsprojectmasterOrderByBatchcodeDesc(
							"N", objuser.getLstprojectforfilter(), fromdate, todate, "N", 0, fromdate, todate,
							objuser.getLstprojectforfilter(), "N", objuser, objuser, fromdate, todate,
							objuser.getLstprojectforfilter(), "N", objuser, fromdate, todate,
							objuser.getLstprojectforfilter(), pageable));
//			count = count + lslogilablimsorderdetailRepository
//					.countByOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterAndAssignedtoIsNullOrOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndLsprojectmasterOrOrderflagAndAssignedtoAndCreatedtimestampBetweenAndLsprojectmasterOrderByBatchcodeDesc(
//							"N", objuser.getLstprojectforfilter(), fromdate, todate, "N", 0, fromdate, todate,
//							objuser.getLstprojectforfilter(), "N", objuser, objuser, fromdate, todate,
//							objuser.getLstprojectforfilter(), "N", objuser, fromdate, todate,
//							objuser.getLstprojectforfilter());

			lstorders.addAll(lslogilablimsorderdetailRepository
					.findByApprovelstatusAndLsprojectmasterAndCreatedtimestampBetweenAndAssignedtoIsNullOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndApprovelstatusAndLsprojectmasterOrAssignedtoAndCreatedtimestampBetweenAndApprovelstatusAndLsprojectmaster(
							3, objuser.getLstprojectforfilter(), fromdate, todate, objuser, objuser, fromdate, todate,
							3, objuser.getLstprojectforfilter(), objuser, fromdate, todate, 3,
							objuser.getLstprojectforfilter(), pageable));
//			count = count + lslogilablimsorderdetailRepository
//					.countByApprovelstatusAndLsprojectmasterAndCreatedtimestampBetweenAndAssignedtoIsNullOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndApprovelstatusAndLsprojectmasterOrAssignedtoAndCreatedtimestampBetweenAndApprovelstatusAndLsprojectmaster(
//							3, objuser.getLstprojectforfilter(), fromdate, todate, objuser, objuser, fromdate, todate,
//							3, objuser.getLstprojectforfilter(), objuser, fromdate, todate, 3,
//							objuser.getLstprojectforfilter());

			lstorders.addAll(lslogilablimsorderdetailRepository
					.findByOrdercancellAndLsprojectmasterAndCreatedtimestampBetweenAndAssignedtoIsNullOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndOrdercancellAndLsprojectmasterOrAssignedtoAndCreatedtimestampBetweenAndOrdercancellAndLsprojectmaster(
							1, objuser.getLstprojectforfilter(), fromdate, todate, objuser, objuser, fromdate, todate,
							1, objuser.getLstprojectforfilter(), objuser, fromdate, todate, 1,
							objuser.getLstprojectforfilter(), pageable));
//			count = count + lslogilablimsorderdetailRepository
//					.countByOrdercancellAndLsprojectmasterAndCreatedtimestampBetweenAndAssignedtoIsNullOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndOrdercancellAndLsprojectmasterOrAssignedtoAndCreatedtimestampBetweenAndOrdercancellAndLsprojectmaster(
//							1, objuser.getLstprojectforfilter(), fromdate, todate, objuser, objuser, fromdate, todate,
//							1, objuser.getLstprojectforfilter(), objuser, fromdate, todate, 1,
//							objuser.getLstprojectforfilter());
		} else {
			lstorders = lslogilablimsorderdetailRepository
					.findByOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndApprovelstatusNotAndTestcodeAndOrdercancellIsNullOrOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndTestcodeAndOrdercancellIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterAndTestcodeAndOrdercancellIsNullOrOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndLsprojectmasterAndTestcodeOrOrderflagAndAssignedtoAndCreatedtimestampBetweenAndLsprojectmasterAndTestcodeOrderByBatchcodeDesc(
							"R", objuser.getLstprojectforfilter(), fromdate, todate, 3, testcode, "R",
							objuser.getLstprojectforfilter(), fromdate, todate, testcode, "R", 0, fromdate, todate,
							objuser.getLstprojectforfilter(), testcode, "R", objuser, objuser, fromdate, todate,
							objuser.getLstprojectforfilter(), testcode, "R", objuser, fromdate, todate,
							objuser.getLstprojectforfilter(), testcode, pageable);

//			count = lslogilablimsorderdetailRepository
//					.countByOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndApprovelstatusNotAndTestcodeAndOrdercancellIsNullOrOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndTestcodeAndOrdercancellIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterAndTestcodeAndOrdercancellIsNullOrOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndLsprojectmasterAndTestcodeOrOrderflagAndAssignedtoAndCreatedtimestampBetweenAndLsprojectmasterAndTestcodeOrderByBatchcodeDesc(
//							"R", objuser.getLstprojectforfilter(), fromdate, todate, 3, testcode, "R",
//							objuser.getLstprojectforfilter(), fromdate, todate, testcode, "R", 0, fromdate, todate,
//							objuser.getLstprojectforfilter(), testcode, "R", objuser, objuser, fromdate, todate,
//							objuser.getLstprojectforfilter(), testcode, "R", objuser, fromdate, todate,
//							objuser.getLstprojectforfilter(), testcode);

			lstorders.addAll(lslogilablimsorderdetailRepository
					.findByOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndOrdercancellIsNullAndTestcodeAndAssignedtoIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterAndTestcodeAndAssignedtoIsNullOrOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndLsprojectmasterAndTestcodeOrOrderflagAndAssignedtoAndCreatedtimestampBetweenAndLsprojectmasterAndTestcodeOrderByBatchcodeDesc(
							"N", objuser.getLstprojectforfilter(), fromdate, todate, testcode, "N", 0, fromdate, todate,
							objuser.getLstprojectforfilter(), testcode, "N", objuser, objuser, fromdate, todate,
							objuser.getLstprojectforfilter(), testcode, "N", objuser, fromdate, todate,
							objuser.getLstprojectforfilter(), testcode, pageable));
//			count = count + lslogilablimsorderdetailRepository
//					.countByOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndOrdercancellIsNullAndTestcodeAndAssignedtoIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterAndTestcodeAndAssignedtoIsNullOrOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndLsprojectmasterAndTestcodeOrOrderflagAndAssignedtoAndCreatedtimestampBetweenAndLsprojectmasterAndTestcodeOrderByBatchcodeDesc(
//							"N", objuser.getLstprojectforfilter(), fromdate, todate, testcode, "N", 0, fromdate, todate,
//							objuser.getLstprojectforfilter(), testcode, "N", objuser, objuser, fromdate, todate,
//							objuser.getLstprojectforfilter(), testcode, "N", objuser, fromdate, todate,
//							objuser.getLstprojectforfilter(), testcode);
			lstorders.addAll(lslogilablimsorderdetailRepository
					.findByApprovelstatusAndLsprojectmasterAndCreatedtimestampBetweenAndTestcodeAndAssignedtoIsNullOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndApprovelstatusAndLsprojectmasterAndTestcodeOrAssignedtoAndCreatedtimestampBetweenAndApprovelstatusAndLsprojectmasterAndTestcode(
							3, objuser.getLstprojectforfilter(), fromdate, todate, testcode, objuser, objuser, fromdate,
							todate, 3, objuser.getLstprojectforfilter(), testcode, objuser, fromdate, todate, 3,
							objuser.getLstprojectforfilter(), testcode, pageable));

//			count = count + lslogilablimsorderdetailRepository
//					.countByApprovelstatusAndLsprojectmasterAndCreatedtimestampBetweenAndTestcodeAndAssignedtoIsNullOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndApprovelstatusAndLsprojectmasterAndTestcodeOrAssignedtoAndCreatedtimestampBetweenAndApprovelstatusAndLsprojectmasterAndTestcode(
//							3, objuser.getLstprojectforfilter(), fromdate, todate, testcode, objuser, objuser, fromdate,
//							todate, 3, objuser.getLstprojectforfilter(), testcode, objuser, fromdate, todate, 3,
//							objuser.getLstprojectforfilter(), testcode);

			lstorders.addAll(lslogilablimsorderdetailRepository
					.findByOrdercancellAndLsprojectmasterAndCreatedtimestampBetweenAndTestcodeAndAssignedtoIsNullOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndOrdercancellAndLsprojectmasterAndTestcodeOrAssignedtoAndCreatedtimestampBetweenAndOrdercancellAndLsprojectmasterAndTestcode(
							1, objuser.getLstprojectforfilter(), fromdate, todate, testcode, objuser, objuser, fromdate,
							todate, 1, objuser.getLstprojectforfilter(), testcode, objuser, fromdate, todate, 1,
							objuser.getLstprojectforfilter(), testcode, pageable));
//			count = count + lslogilablimsorderdetailRepository
//					.countByOrdercancellAndLsprojectmasterAndCreatedtimestampBetweenAndTestcodeAndAssignedtoIsNullOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndOrdercancellAndLsprojectmasterAndTestcodeOrAssignedtoAndCreatedtimestampBetweenAndOrdercancellAndLsprojectmasterAndTestcode(
//							1, objuser.getLstprojectforfilter(), fromdate, todate, testcode, objuser, objuser, fromdate,
//							todate, 1, objuser.getLstprojectforfilter(), testcode, objuser, fromdate, todate, 1,
//							objuser.getLstprojectforfilter(), testcode);
		}
		if (env.getProperty("client") != null && env.getProperty("client").equals("HPCL")) {
			Collections.sort(lstorders, (order1, order2) -> order2.getCt().compareTo(order1.getCt()));
		} else {
			lstorders.sort(Comparator.comparing(LogilabOrdermastersh::getBc).reversed());
//			lstorders = lstorders == null ? new ArrayList<>() : lstorders.stream()
//				    .filter(Objects::nonNull)  
//				    .sorted(Comparator.comparing(
//				        Logilabordermaster::getBatchcode, 
//				        Comparator.nullsLast(Comparator.naturalOrder())  
//				    ).reversed())
//				    .collect(Collectors.toList());
		}
		lstorders.forEach(objorder -> objorder.setLw(lstworkflow));
		mapOrders.put("orderlst", lstorders);
		mapOrders.put("count", count);
		return mapOrders;
	}

	public Map<String, Object> Getdashboardorders(LSuserMaster objuser) throws ParseException {
		Date fromdate = objuser.getObjuser().getFromdate();
		Date todate = objuser.getObjuser().getTodate();
		Map<String, Object> mapOrders = new HashMap<String, Object>();
		Integer testcode = objuser.getTestcode();
		Pageable pageable = new PageRequest(objuser.getPagesize(), objuser.getPageperorder());

		List<LSprojectmaster> lstproject = objuser.getLstproject();
		List<LogilabOrdermastersh> lstorders = new ArrayList<LogilabOrdermastersh>();
		List<LSworkflow> lstworkflow = objuser.getLstworkflow();
		List<Integer> lstsampleint = lssamplemasterrepository
				.getDistinctByLssitemasterSitecodeAndStatus(objuser.getLssitemaster().getSitecode(), 1);

		List<LSuserteammapping> teammapping = LSuserteammappingRepositoryObj
				.findByLsuserMasterAndTeamcodeNotNull(objuser);
		List<LSusersteam> userteamlist = lsusersteamRepository.findByLsuserteammappingIn(teammapping);
		List<LSSelectedTeam> selectedorders = LSSelectedTeamRepository
				.findByUserteamInAndCreatedtimestampBetween(userteamlist, fromdate, todate);
		// List<LSSelectedTeam> selectedorders =
		// LSSelectedTeamRepository.findByUserteamIn(userteamlist);

		List<Long> selectedteambatchCodeList = new ArrayList<>();
		selectedteambatchCodeList = selectedteambatchCodeList.isEmpty() ? Collections.singletonList(-1L)
				: selectedteambatchCodeList;
		if (selectedorders != null && !selectedorders.isEmpty()) {
			selectedteambatchCodeList = selectedorders.stream().map(LSSelectedTeam::getBatchcode)
					.filter(Objects::nonNull).distinct().collect(Collectors.toList());
		}

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

		int chunkSize = Integer.parseInt(env.getProperty("lssamplecount"));
		int totalSamples = lstsample1.size();
		List<LSlogilablimsorderdetail> lstordersdem = new ArrayList<LSlogilablimsorderdetail>();
		List<LogilabOrdermastersh> lstorderobj = new ArrayList<LogilabOrdermastersh>();
		if (lstproject != null) {
			objuser.getUsernotify().add(objuser);
			if (objuser.getObjuser().getOrderselectiontype() == 1) {

				return mapOrders = getallorders(lstproject, fromdate, todate, objuser, testcode, pageable, lstsample1,
						lstworkflow, selectedteambatchCodeList);

			} else if (objuser.getObjuser().getOrderselectiontype() == 2) {
				if (testcode == -1 && objuser.getLstprojectforfilter() == null) {

//					lstordersdem = LogilablimsorderdetailsRepository.getLSlogilablimsorderdetaildashboardforcompleted(
//							"R", 0, fromdate, todate, objuser, 3, 1, 2, 3, objuser.getLssitemaster(),
//							objuser.getPagesize() * objuser.getPageperorder(), objuser.getPageperorder());

					lstordersdem = LogilablimsorderdetailsRepository.getLSlogilablimsorderdetaildashboardforcompleted(
							"R", 0, fromdate, todate, objuser, 3, 1, 2, 3,
							objuser.getPagesize() * objuser.getPageperorder(), objuser.getPageperorder(),
							selectedteambatchCodeList,lstproject);

				} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {

					lstorders = lslogilablimsorderdetailRepository
							.findByOrderflagAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusNotAndLsprojectmasterInAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndLsprojectmasterIsNullAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndTestcodeOrOrderflagAndAssignedtoAndCreatedtimestampBetweenAndTestcodeOrderByBatchcodeDesc(
									"R", 0, fromdate, todate, testcode, "R", 1, objuser, fromdate, todate, 3, testcode,
									"R", 2, objuser, fromdate, todate, 3, testcode, "R", 3, objuser, fromdate, todate,
									3, lstproject, testcode, "R", 1, objuser, fromdate, todate, testcode, "R", 2,
									objuser, fromdate, todate, testcode, "R", 3, objuser, fromdate, todate, testcode,
									"R", objuser, objuser, fromdate, todate, testcode, "R", objuser, fromdate, todate,
									testcode, pageable);

					lstorders.addAll(lslogilablimsorderdetailRepository
							.findByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeOrderByBatchcodeDesc(
									"R", lstproject, fromdate, todate, 3, testcode, pageable));

					lstorders.addAll(lslogilablimsorderdetailRepository
							.findByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeOrderByBatchcodeDesc(
									"R", lstproject, fromdate, todate, testcode, pageable));

				} else if (testcode == -1 && objuser.getLstprojectforfilter() != null) {

					lstorders = lslogilablimsorderdetailRepository
							.findByOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndLsprojectmasterOrOrderflagAndAssignedtoAndCreatedtimestampBetweenAndLsprojectmasterOrderByBatchcodeDesc(
									"R", objuser.getLstprojectforfilter(), fromdate, todate, 3, "R",
									objuser.getLstprojectforfilter(), fromdate, todate, "R", 0, fromdate, todate,
									objuser.getLstprojectforfilter(), "R", objuser, objuser, fromdate, todate,
									objuser.getLstprojectforfilter(), "R", objuser, fromdate, todate,
									objuser.getLstprojectforfilter(), pageable);

				} else {

					lstorders = lslogilablimsorderdetailRepository
							.findByOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndApprovelstatusNotAndTestcodeAndOrdercancellIsNullOrOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndApprovelstatusIsNullAndTestcodeAndOrdercancellIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterAndTestcodeAndOrdercancellIsNullOrOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndLsprojectmasterAndTestcodeOrOrderflagAndAssignedtoAndCreatedtimestampBetweenAndLsprojectmasterAndTestcodeOrderByBatchcodeDesc(
									"R", objuser.getLstprojectforfilter(), fromdate, todate, 3, testcode, "R",
									objuser.getLstprojectforfilter(), fromdate, todate, testcode, "R", 0, fromdate,
									todate, objuser.getLstprojectforfilter(), testcode, "R", objuser, objuser, fromdate,
									todate, objuser.getLstprojectforfilter(), testcode, "R", objuser, fromdate, todate,
									objuser.getLstprojectforfilter(), testcode, pageable);

				}

				lstorderobj = IntStream.range(0, (totalSamples + chunkSize - 1) / chunkSize).parallel().mapToObj(i -> {

					int startIndex = i * chunkSize;

					int endIndex = Math.min(startIndex + chunkSize, totalSamples);

					List<LSsamplemaster> currentChunk = lstsample1.subList(startIndex, endIndex);

					List<LogilabOrdermastersh> orderChunk = new ArrayList<>();

					if (testcode != -1 && objuser.getLstprojectforfilter() == null) {
						orderChunk.addAll(lslogilablimsorderdetailRepository
								.findByOrderflagAndLsprojectmasterIsNullAndLssamplemasterInAndCreatedtimestampBetweenAndApprovelstatusNotAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeAndLsuserMasterNotOrderByBatchcodeDesc(
										"R", currentChunk, fromdate, todate, 3, testcode, objuser, pageable));

						orderChunk.addAll(lslogilablimsorderdetailRepository
								.findByOrderflagAndLsprojectmasterIsNullAndLssamplemasterInAndCreatedtimestampBetweenAndApprovelstatusIsNullAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeAndLsuserMasterNotOrderByBatchcodeDesc(
										"R", currentChunk, fromdate, todate, testcode, objuser, pageable));
					}

					return orderChunk;

				}).flatMap(List::stream).collect(Collectors.toList());
				lstorders.addAll(lstorderobj);

			} else if (objuser.getObjuser().getOrderselectiontype() == 3) {
				if (testcode == -1 && objuser.getLstprojectforfilter() == null) {
					lstordersdem.addAll(
							lslogilablimsorderdetailRepository.getLSlogilablimsorderdetaildashboardforpending("N", 0,
									fromdate, todate, objuser, 1, 2, 3, objuser.getUsernotify(),
									objuser.getLssitemaster(), objuser.getPagesize() * objuser.getPageperorder(),
									objuser.getPageperorder(), 3, selectedteambatchCodeList));

//					lstordersdem.addAll(lslogilablimsorderdetailRepository.getLSlogilablimsorderdetaildashboardforpendingview(
//							"N", 0, fromdate, todate, objuser, 1, 2, 3, objuser.getUsernotify(),3,selectedteambatchCodeList));

				} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {

					lstorders = lslogilablimsorderdetailRepository
							.findByOrderflagAndFiletypeAndCreatedtimestampBetweenAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterInAndViewoptionAndLsuserMasterAndOrdercancellIsNullAndCreatedtimestampBetweenAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsprojectmasterIsNullAndViewoptionAndOrdercancellIsNullAndCreatedtimestampBetweenAndLsuserMasterInAndAssignedtoIsNullAndTestcodeOrOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndTestcodeOrOrderflagAndAssignedtoAndCreatedtimestampBetweenAndTestcodeOrderByBatchcodeDesc(
									"N", 0, fromdate, todate, testcode, "N", 1, objuser, fromdate, todate, testcode,
									"N", 2, objuser, fromdate, todate, testcode, "N", lstproject, 3, objuser, fromdate,
									todate, testcode, "N", 3, fromdate, todate, objuser.getUsernotify(), testcode, "N",
									objuser, objuser, fromdate, todate, testcode, "N", objuser, fromdate, todate,
									testcode, pageable);

					lstorders.addAll(lslogilablimsorderdetailRepository
							.findByOrderflagAndLsprojectmasterInAndCreatedtimestampBetweenAndOrdercancellIsNullAndAssignedtoIsNullAndTestcodeOrderByBatchcodeDesc(
									"N", lstproject, fromdate, todate, testcode, pageable));

				} else if (testcode == -1 && objuser.getLstprojectforfilter() != null) {
					lstorders = lslogilablimsorderdetailRepository
							.findByOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndOrdercancellIsNullAndAssignedtoIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterAndAssignedtoIsNullOrOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndLsprojectmasterOrOrderflagAndAssignedtoAndCreatedtimestampBetweenAndLsprojectmasterOrderByBatchcodeDesc(
									"N", objuser.getLstprojectforfilter(), fromdate, todate, "N", 0, fromdate, todate,
									objuser.getLstprojectforfilter(), "N", objuser, objuser, fromdate, todate,
									objuser.getLstprojectforfilter(), "N", objuser, fromdate, todate,
									objuser.getLstprojectforfilter(), pageable);

				} else {
					lstorders = lslogilablimsorderdetailRepository
							.findByOrderflagAndLsprojectmasterAndCreatedtimestampBetweenAndOrdercancellIsNullAndTestcodeAndAssignedtoIsNullOrOrderflagAndFiletypeAndCreatedtimestampBetweenAndLsprojectmasterAndTestcodeAndAssignedtoIsNullOrOrderflagAndLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndLsprojectmasterAndTestcodeOrOrderflagAndAssignedtoAndCreatedtimestampBetweenAndLsprojectmasterAndTestcodeOrderByBatchcodeDesc(
									"N", objuser.getLstprojectforfilter(), fromdate, todate, testcode, "N", 0, fromdate,
									todate, objuser.getLstprojectforfilter(), testcode, "N", objuser, objuser, fromdate,
									todate, objuser.getLstprojectforfilter(), testcode, "N", objuser, fromdate, todate,
									objuser.getLstprojectforfilter(), testcode, pageable);
				}

			} else if (objuser.getObjuser().getOrderselectiontype() == 5) {
				if (testcode == -1 && objuser.getLstprojectforfilter() == null) {

					lstorders = lslogilablimsorderdetailRepository
							.findByLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusAndAssignedtoIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusAndAssignedtoIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusAndAssignedtoIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusAndLsprojectmasterIsNullAndAssignedtoIsNullOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndApprovelstatusOrAssignedtoAndCreatedtimestampBetweenAndApprovelstatus(
									1, objuser, fromdate, todate, 3, 2, objuser, fromdate, todate, 3, 3, objuser,
									fromdate, todate, 3, 3, objuser, fromdate, todate, 3, objuser, objuser, fromdate,
									todate, 3, objuser, fromdate, todate, 3, pageable);

					lstorders.addAll(lslogilablimsorderdetailRepository
							.findByApprovelstatusAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNull(3,
									lstproject, fromdate, todate, pageable));
				} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {

					lstorders.addAll(lslogilablimsorderdetailRepository
							.findByLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusAndAssignedtoIsNullAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusAndAssignedtoIsNullAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusAndAssignedtoIsNullAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndApprovelstatusAndLsprojectmasterIsNullAndAssignedtoIsNullAndTestcodeOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndApprovelstatusAndTestcodeOrAssignedtoAndCreatedtimestampBetweenAndApprovelstatusAndTestcode(
									1, objuser, fromdate, todate, 3, testcode, 2, objuser, fromdate, todate, 3,
									testcode, 3, objuser, fromdate, todate, 3, testcode, 3, objuser, fromdate, todate,
									3, testcode, objuser, objuser, fromdate, todate, 3, testcode, objuser, fromdate,
									todate, 3, testcode, pageable));

					lstorders.addAll(lslogilablimsorderdetailRepository
							.findByApprovelstatusAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullAndTestcode(
									3, lstproject, fromdate, todate, testcode, pageable));

				} else if (testcode == -1 && objuser.getLstprojectforfilter() != null) {

					lstorders = lslogilablimsorderdetailRepository
							.findByApprovelstatusAndLsprojectmasterAndCreatedtimestampBetweenAndAssignedtoIsNullOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndApprovelstatusAndLsprojectmasterOrAssignedtoAndCreatedtimestampBetweenAndApprovelstatusAndLsprojectmaster(
									3, objuser.getLstprojectforfilter(), fromdate, todate, objuser, objuser, fromdate,
									todate, 3, objuser.getLstprojectforfilter(), objuser, fromdate, todate, 3,
									objuser.getLstprojectforfilter(), pageable);

				} else {

					lstorders = lslogilablimsorderdetailRepository
							.findByApprovelstatusAndLsprojectmasterAndCreatedtimestampBetweenAndTestcodeAndAssignedtoIsNullOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndApprovelstatusAndLsprojectmasterAndTestcodeOrAssignedtoAndCreatedtimestampBetweenAndApprovelstatusAndLsprojectmasterAndTestcode(
									3, objuser.getLstprojectforfilter(), fromdate, todate, testcode, objuser, objuser,
									fromdate, todate, 3, objuser.getLstprojectforfilter(), testcode, objuser, fromdate,
									todate, 3, objuser.getLstprojectforfilter(), testcode, pageable);

				}

				lstorderobj = IntStream.range(0, (totalSamples + chunkSize - 1) / chunkSize).parallel().mapToObj(i -> {

					int startIndex = i * chunkSize;
					int endIndex = Math.min(startIndex + chunkSize, totalSamples);
					List<LSsamplemaster> currentChunk = lstsample1.subList(startIndex, endIndex);
					List<LogilabOrdermastersh> orderChunk = new ArrayList<>();
					if (testcode == -1 && objuser.getLstprojectforfilter() == null) {
						orderChunk.addAll(lslogilablimsorderdetailRepository
								.findByApprovelstatusAndLsprojectmasterIsNullAndLssamplemasterInAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsuserMasterNot(
										3, currentChunk, fromdate, todate, objuser, pageable));
					} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {
						orderChunk.addAll(lslogilablimsorderdetailRepository
								.findByApprovelstatusAndLsprojectmasterIsNullAndLssamplemasterInAndCreatedtimestampBetweenAndAssignedtoIsNullAndTestcodeAndLsuserMasterNot(
										3, currentChunk, fromdate, todate, testcode, objuser, pageable));

					}

					return orderChunk;

				}).flatMap(List::stream).collect(Collectors.toList());

				lstorders.addAll(lstorderobj);

			} else if (objuser.getObjuser().getOrderselectiontype() == 6) { // cancelled orders
				if (testcode == -1 && objuser.getLstprojectforfilter() == null) {
//existing
//					lstorders = LogilablimsorderdetailsRepository
//							.LsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndAssignedtoIsNullOrOrdercancellAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndAssignedtoIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndLsprojectmasterIsNullAndAssignedtoIsNullOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndOrdercancellOrAssignedtoAndCreatedtimestampBetweenAndOrdercancell(
//									1, objuser, fromdate, todate, 1, 1, lstproject, fromdate, todate, 2, objuser,
//									fromdate, todate, 1, 3, objuser, fromdate, todate, 1, objuser, objuser, fromdate,
//									todate, 1, objuser, fromdate, todate, 1, pageable);

					lstorders = LogilablimsorderdetailsRepository
							.findByLsprojectmasterIsNullAndViewoptionAndTeamselectedAndBatchcodeInAndCreatedtimestampBetweenAndOrdercancellAndLsprojectmasterIsNullAndAssignedtoIsNull(
									3, true, selectedteambatchCodeList, fromdate, todate, 1);

					lstorders.addAll(lslogilablimsorderdetailRepository
							.LsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndAssignedtoIsNullOrOrdercancellAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndAssignedtoIsNullOrLsprojectmasterIsNullAndViewoptionAndTeamselectedAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndLsprojectmasterIsNullAndAssignedtoIsNullOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndOrdercancellOrAssignedtoAndCreatedtimestampBetweenAndOrdercancell(
									1, objuser, fromdate, todate, 1, 1, lstproject, fromdate, todate, 2, objuser,
									fromdate, todate, 1, 3, false, objuser, fromdate, todate, 1, objuser, objuser,
									fromdate, todate, 1, objuser, fromdate, todate, 1));

				} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {

					lstorders = lslogilablimsorderdetailRepository
							.findByOrdercancellAndLsprojectmasterInAndCreatedtimestampBetweenAndAssignedtoIsNullAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndAssignedtoIsNullAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndAssignedtoIsNullAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndLsuserMasterAndCreatedtimestampBetweenAndOrdercancellAndLsprojectmasterIsNullAndAssignedtoIsNullAndTestcodeOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndOrdercancellAndTestcodeOrAssignedtoAndCreatedtimestampBetweenAndOrdercancellAndTestcode(
									1, lstproject, fromdate, todate, testcode, 1, objuser, fromdate, todate, 1,
									testcode, 2, objuser, fromdate, todate, 1, testcode, 3, objuser, fromdate, todate,
									1, testcode, objuser, objuser, fromdate, todate, 1, testcode, objuser, fromdate,
									todate, 1, testcode, pageable);

				} else if (testcode == -1 && objuser.getLstprojectforfilter() != null) {

					lstorders = lslogilablimsorderdetailRepository
							.findByOrdercancellAndLsprojectmasterAndCreatedtimestampBetweenAndAssignedtoIsNullOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndOrdercancellAndLsprojectmasterOrAssignedtoAndCreatedtimestampBetweenAndOrdercancellAndLsprojectmaster(
									1, objuser.getLstprojectforfilter(), fromdate, todate, objuser, objuser, fromdate,
									todate, 1, objuser.getLstprojectforfilter(), objuser, fromdate, todate, 1,
									objuser.getLstprojectforfilter(), pageable);

				} else {
					lstorders = lslogilablimsorderdetailRepository
							.findByOrdercancellAndLsprojectmasterAndCreatedtimestampBetweenAndTestcodeAndAssignedtoIsNullOrLsuserMasterAndAssignedtoNotAndCreatedtimestampBetweenAndAssignedtoNotNullAndOrdercancellAndLsprojectmasterAndTestcodeOrAssignedtoAndCreatedtimestampBetweenAndOrdercancellAndLsprojectmasterAndTestcode(
									1, objuser.getLstprojectforfilter(), fromdate, todate, testcode, objuser, objuser,
									fromdate, todate, 1, objuser.getLstprojectforfilter(), testcode, objuser, fromdate,
									todate, 1, objuser.getLstprojectforfilter(), testcode, pageable);
				}

			}
			if (lstordersdem.size() > 0) {
				lstorders = lstordersdem.parallelStream().map(lsOrderDetail -> new LogilabOrdermastersh(
						lsOrderDetail.getBatchcode(), lsOrderDetail.getBatchid(), lsOrderDetail.getLsworkflow(),
						lsOrderDetail.getTestname(), lsOrderDetail.getLsfile(), lsOrderDetail.getLssamplemaster(),
						lsOrderDetail.getLsprojectmaster(), lsOrderDetail.getFiletype(), lsOrderDetail.getOrderflag(),
						lsOrderDetail.getAssignedto(), lsOrderDetail.getCreatedtimestamp(),
						lsOrderDetail.getCompletedtimestamp(), lsOrderDetail.getKeyword(),
						lsOrderDetail.getLstestmasterlocal(), lsOrderDetail.getOrdercancell(),
						lsOrderDetail.getViewoption(), lsOrderDetail.getLsuserMaster(), lsOrderDetail.getTestcode(),
						lsOrderDetail.getApprovelstatus(), lsOrderDetail.getLsordernotification(),
						lsOrderDetail.getOrdersaved(), lsOrderDetail.getRepeat(),
						lsOrderDetail.getLsautoregisterorders(), lsOrderDetail.getSentforapprovel(),
						lsOrderDetail.getApprovelaccept(), lsOrderDetail.getAutoregistercount(),
						lsOrderDetail.getElnmaterial(), lsOrderDetail.getLockedusername(),
						lsOrderDetail.getApplicationsequence(), lsOrderDetail.getSitesequence(),
						lsOrderDetail.getProjectsequence(), lsOrderDetail.getTasksequence(),
						lsOrderDetail.getOrdertypesequence(), lsOrderDetail.getSequenceid(),
						lsOrderDetail.getTeamselected(), lsOrderDetail.getModifieddate()

				)).collect(Collectors.toList());
			}
			if (env.getProperty("client") != null && env.getProperty("client").equals("HPCL")) {
				Collections.sort(lstorders, (order1, order2) -> order2.getCt().compareTo(order1.getCt()));
			} else {
				lstorders.sort(Comparator.comparing(LogilabOrdermastersh::getBc).reversed());
			}
			lstorders.forEach(objorder -> objorder.setLw(lstworkflow));
		} else {

			List<LSSheetOrderStructure> lstdir = lsSheetOrderStructureRepository
					.findBySitemasterAndViewoptionOrCreatedbyAndViewoptionOrderByDirectorycode(
							objuser.getLssitemaster(), 1, objuser, 2);
			List<Long> directorycode = lstdir.stream().map(LSSheetOrderStructure::getDirectorycode)
					.collect(Collectors.toList());
			if (objuser.getObjuser().getOrderselectiontype() == 1) {// total orders

				if (testcode == -1 && objuser.getLstprojectforfilter() == null) {
//					lstorders = lslogilablimsorderdetailRepository
//							.findByViewoptionAndCreatedtimestampBetweenAndLsuserMasterOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterOrderByBatchcodeDesc(
//									3, fromdate, todate, objuser, directorycode, 
//									1, fromdate, todate, directorycode, 
//									2, fromdate, todate, objuser, directorycode, 
//									3, fromdate, todate, objuser, pageable);

					lstorders = LogilablimsorderdetailsRepository
							.findByViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTeamselectedOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTeamselectedOrViewoptionAndTeamselectedAndBatchcodeInAndCreatedtimestampBetweenAndLsuserMasterOrLsprojectmasterIsNullAndDirectorycodeInAndTeamselectedAndBatchcodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterOrderByBatchcodeDesc(
									3, fromdate, todate, objuser, false, directorycode, 1, fromdate, todate,
									directorycode, 2, fromdate, todate, objuser, directorycode, 3, fromdate, todate,
									objuser, false, 3, true, selectedteambatchCodeList, fromdate, todate, objuser,
									directorycode, true, selectedteambatchCodeList, 3, fromdate, todate, objuser,
									pageable);

//					lstorders.addAll(LogilablimsorderdetailsRepository
//							.findByViewoptionAndTeamselectedAndBatchcodeInAndCreatedtimestampBetweenAndLsuserMasterOrLsprojectmasterIsNullAndDirectorycodeInAndTeamselectedAndBatchcodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterOrderByBatchcodeDesc(
//									3,true,selectedteambatchCodeList, fromdate, todate, objuser, 
//									directorycode, true,selectedteambatchCodeList,3, fromdate, todate, objuser, pageable));

				} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {
//					lstorders = lslogilablimsorderdetailRepository
//							.findByViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTestcodeOrderByBatchcodeDesc(
//									3, fromdate, todate, objuser, testcode, directorycode, 1, fromdate, todate,
//									testcode, directorycode, 2, fromdate, todate, objuser, testcode, directorycode, 3,
//									fromdate, todate, objuser, testcode, pageable);

					lstorders = LogilablimsorderdetailsRepository
							.findByViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTestcodeAndTeamselectedOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTestcodeAndTeamselectedOrViewoptionAndTeamselectedAndBatchcodeInAndCreatedtimestampBetweenAndLsuserMasterAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndTeamselectedAndBatchcodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTestcodeOrderByBatchcodeDesc(
									3, fromdate, todate, objuser, testcode, false, directorycode, 1, fromdate, todate,
									testcode, directorycode, 2, fromdate, todate, objuser, testcode, directorycode, 3,
									fromdate, todate, objuser, testcode, false, 3, true, selectedteambatchCodeList,
									fromdate, todate, objuser, testcode, directorycode, true, selectedteambatchCodeList,
									3, fromdate, todate, objuser, testcode, pageable);

//					lstorders.addAll(LogilablimsorderdetailsRepository
//							.findByViewoptionAndTeamselectedAndBatchcodeInAndCreatedtimestampBetweenAndLsuserMasterAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndTeamselectedAndBatchcodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTestcodeOrderByBatchcodeDesc(3,true,selectedteambatchCodeList, fromdate, todate, objuser, testcode, directorycode,true,selectedteambatchCodeList, 3,fromdate, todate, objuser, testcode, pageable));

				}

				lstorderobj = IntStream.range(0, (totalSamples + chunkSize - 1) / chunkSize).parallel().mapToObj(i -> {

					int startIndex = i * chunkSize;

					int endIndex = Math.min(startIndex + chunkSize, totalSamples);

					List<LSsamplemaster> currentChunk = lstsample1.subList(startIndex, endIndex);

					List<LogilabOrdermastersh> orderChunk = new ArrayList<>();

					if (testcode == -1 && objuser.getLstprojectforfilter() == null) {
						orderChunk.addAll(lslogilablimsorderdetailRepository
								.findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndDirectorycodeIsNullOrderByBatchcodeDesc(
										currentChunk, 1, fromdate, todate, pageable));
						orderChunk.addAll(lslogilablimsorderdetailRepository
								.findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndDirectorycodeIsNullOrderByBatchcodeDesc(
										currentChunk, 2, fromdate, todate, objuser, pageable));
						orderChunk.addAll(lslogilablimsorderdetailRepository
								.findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndTestcodeAndDirectorycodeIsNullOrderByBatchcodeDesc(
										currentChunk, 1, fromdate, todate, testcode, pageable));
						orderChunk.addAll(lslogilablimsorderdetailRepository
								.findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTestcodeAndDirectorycodeIsNullOrderByBatchcodeDesc(
										currentChunk, 2, fromdate, todate, objuser, testcode, pageable));
					} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {

					}

					return orderChunk;

				}).flatMap(List::stream).collect(Collectors.toList());

				lstorders.addAll(lstorderobj);

			} else if (objuser.getObjuser().getOrderselectiontype() == 2) {// completed orders
				if (testcode == -1 && objuser.getLstprojectforfilter() == null) {
//					lstorders = lslogilablimsorderdetailRepository
//							.findByViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullOrderByBatchcodeDesc(
//									3, fromdate, todate, objuser, "R", 3, 3, fromdate, todate, objuser, "R",
//									directorycode, 1, fromdate, todate, "R", 3, directorycode, 1, fromdate, todate, "R",
//									directorycode, 2, fromdate, todate, objuser, "R", 3, directorycode, 2, fromdate,
//									todate, objuser, "R", directorycode, 3, fromdate, todate, objuser, "R", 3,
//									directorycode, 3, fromdate, todate, objuser, "R", pageable);

					lstorders = lslogilablimsorderdetailRepository
							.findByViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullAndTeamselectedOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndTeamselectedAndOrdercancellIsNullOrViewoptionAndTeamselectedAndBatchcodeInAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndTeamselectedAndBatchcodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullOrderByBatchcodeDesc(
									3, fromdate, todate, objuser, "R", 3, 3, fromdate, todate, objuser, "R", false,
									directorycode, 1, fromdate, todate, "R", 3, directorycode, 1, fromdate, todate, "R",
									directorycode, 2, fromdate, todate, objuser, "R", 3, directorycode, 2, fromdate,
									todate, objuser, "R", directorycode, 3, fromdate, todate, objuser, "R", 3,
									directorycode, 3, fromdate, todate, objuser, "R", false, 3, true,
									selectedteambatchCodeList, fromdate, todate, objuser, "R", directorycode, true,
									selectedteambatchCodeList, 3, fromdate, todate, objuser, "R", pageable);

				} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {
					lstorders = lslogilablimsorderdetailRepository
							.findByViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullAndTestcodeOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullAndTestcodeOrderByBatchcodeDesc(
									3, fromdate, todate, objuser, "R", 3, testcode, 3, fromdate, todate, objuser, "R",
									testcode, directorycode, 1, fromdate, todate, "R", 3, testcode, directorycode, 1,
									fromdate, todate, "R", testcode, directorycode, 2, fromdate, todate, objuser, "R",
									3, testcode, directorycode, 2, fromdate, todate, objuser, "R", testcode,
									directorycode, 3, fromdate, todate, objuser, "R", 3, testcode, directorycode, 3,
									fromdate, todate, objuser, "R", testcode, pageable);
				}

				lstorderobj = IntStream.range(0, (totalSamples + chunkSize - 1) / chunkSize).parallel().mapToObj(i -> {

					int startIndex = i * chunkSize;

					int endIndex = Math.min(startIndex + chunkSize, totalSamples);

					List<LSsamplemaster> currentChunk = lstsample1.subList(startIndex, endIndex);

					List<LogilabOrdermastersh> orderChunk = new ArrayList<>();

					if (testcode == -1 && objuser.getLstprojectforfilter() == null) {
						orderChunk.addAll(lslogilablimsorderdetailRepository
								.findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullAndDirectorycodeIsNullOrderByBatchcodeDesc(
										currentChunk, 1, fromdate, todate, "R", 3, pageable));
						orderChunk.addAll(lslogilablimsorderdetailRepository
								.findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullAndDirectorycodeIsNullOrderByBatchcodeDesc(
										currentChunk, 1, fromdate, todate, "R", pageable));
						orderChunk.addAll(lslogilablimsorderdetailRepository
								.findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullAndDirectorycodeIsNullOrderByBatchcodeDesc(
										currentChunk, 2, fromdate, todate, objuser, "R", 3, pageable));
						orderChunk.addAll(lslogilablimsorderdetailRepository
								.findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullAndDirectorycodeIsNullOrderByBatchcodeDesc(
										currentChunk, 2, fromdate, todate, objuser, "R", pageable));
					} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {
						orderChunk.addAll(lslogilablimsorderdetailRepository
								.findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullAndTestcodeAndDirectorycodeIsNullOrderByBatchcodeDesc(
										currentChunk, 1, fromdate, todate, "R", 3, testcode, pageable));
						orderChunk.addAll(lslogilablimsorderdetailRepository
								.findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullAndTestcodeAndDirectorycodeIsNullOrderByBatchcodeDesc(
										currentChunk, 1, fromdate, todate, "R", testcode, pageable));
						orderChunk.addAll(lslogilablimsorderdetailRepository
								.findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusNotAndOrdercancellIsNullAndTestcodeAndDirectorycodeIsNullOrderByBatchcodeDesc(
										currentChunk, 2, fromdate, todate, objuser, "R", 3, testcode, pageable));
						orderChunk.addAll(lslogilablimsorderdetailRepository
								.findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndApprovelstatusIsNullAndOrdercancellIsNullAndTestcodeAndDirectorycodeIsNullOrderByBatchcodeDesc(
										currentChunk, 2, fromdate, todate, objuser, "R", testcode, pageable));

					}

					return orderChunk;

				}).flatMap(List::stream).collect(Collectors.toList());

				lstorders.addAll(lstorderobj);

			} else if (objuser.getObjuser().getOrderselectiontype() == 3) {// last upadte
				if (testcode == -1 && objuser.getLstprojectforfilter() == null) {
//					lstorders = lslogilablimsorderdetailRepository
//							.findByViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullOrderByBatchcodeDesc(
//									3, fromdate, todate, objuser, "N", directorycode,
//									1, fromdate, todate, "N",directorycode, 
//									2, fromdate, todate, objuser, "N", directorycode, 
//									3, fromdate,todate, objuser, "N", pageable);

					lstorders = LogilablimsorderdetailsRepository
							.findByViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndTeamselectedAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndTeamselectedAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullOrViewoptionAndTeamselectedAndBatchcodeInAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndTeamselectedAndBatchcodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullOrderByBatchcodeDesc(
									3, fromdate, todate, objuser, "N", false, directorycode, 1, fromdate, todate, "N",
									directorycode, 2, fromdate, todate, objuser, "N", directorycode, false, 3, fromdate,
									todate, objuser, "N", 3, true, selectedteambatchCodeList, fromdate, todate, objuser,
									"N", directorycode, true, selectedteambatchCodeList, 3, fromdate, todate, objuser,
									"N", pageable);

				} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {

					lstorders = lslogilablimsorderdetailRepository
							.findByViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndTestcodeOrderByBatchcodeDesc(
									3, fromdate, todate, objuser, "N", testcode, directorycode, 1, fromdate, todate,
									"N", testcode, directorycode, 2, fromdate, todate, objuser, "N", testcode,
									directorycode, 3, fromdate, todate, objuser, "N", testcode, pageable);

//					lstorders = LogilablimsorderdetailsRepository
//							.findByViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndTestcodeAndTeamselectedOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndTestcodeAndTeamselectedOrderByBatchcodeDesc(
//									3, fromdate, todate, objuser, "N", testcode,false,
//									directorycode, 1, fromdate, todate,"N", testcode,
//									directorycode, 2, fromdate, todate, objuser, "N", testcode,
//									directorycode, 3, fromdate, todate, objuser, "N", testcode, false,pageable);
//
//					lstorders.addAll(LogilablimsorderdetailsRepository
//							.findByViewoptionAndTeamselectedAndBatchcodeInAndAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndTeamselectedAndBatchcodeInAndAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndTestcodeOrderByBatchcodeDesc(
//									3, true,selectedteambatchCodeList, fromdate, todate, objuser, "N", testcode,
//									directorycode,true,selectedteambatchCodeList,  3, fromdate, todate, objuser, "N", testcode, pageable));

				}

				lstorderobj = IntStream.range(0, (totalSamples + chunkSize - 1) / chunkSize).parallel().mapToObj(i -> {

					int startIndex = i * chunkSize;

					int endIndex = Math.min(startIndex + chunkSize, totalSamples);

					List<LSsamplemaster> currentChunk = lstsample1.subList(startIndex, endIndex);

					List<LogilabOrdermastersh> orderChunk = new ArrayList<>();

					if (testcode == -1 && objuser.getLstprojectforfilter() == null) {

						orderChunk.addAll(lslogilablimsorderdetailRepository
								.findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullAndDirectorycodeIsNullOrderByBatchcodeDesc(
										currentChunk, 1, fromdate, todate, "N", pageable));
						orderChunk.addAll(lslogilablimsorderdetailRepository
								.findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrderflagAndOrdercancellIsNullAndDirectorycodeIsNullOrderByBatchcodeDesc(
										currentChunk, 2, fromdate, todate, objuser, "N", pageable));
					} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {

						orderChunk.addAll(lslogilablimsorderdetailRepository
								.findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullAndTestcodeOrderByBatchcodeDesc(
										currentChunk, 1, fromdate, todate, "N", testcode, pageable));
					}

					return orderChunk;

				}).flatMap(List::stream).collect(Collectors.toList());

				lstorders.addAll(lstorderobj);

			} else if (objuser.getObjuser().getOrderselectiontype() == 5) {
				if (testcode == -1 && objuser.getLstprojectforfilter() == null) {

					lstorders = lslogilablimsorderdetailRepository
							.findByViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndApprovelstatusOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusOrderByBatchcodeDesc(
									3, fromdate, todate, objuser, 3, directorycode, 1, fromdate, todate, 3,
									directorycode, 2, fromdate, todate, objuser, 3, directorycode, 3, fromdate, todate,
									objuser, 3, pageable);

				} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {

					lstorders = lslogilablimsorderdetailRepository
							.findByViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndApprovelstatusAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusAndTestcodeOrderByBatchcodeDesc(
									3, fromdate, todate, objuser, 3, testcode, directorycode, 1, fromdate, todate, 3,
									testcode, directorycode, 2, fromdate, todate, objuser, 3, testcode, directorycode,
									3, fromdate, todate, objuser, 3, testcode, pageable);

				}

				lstorderobj = IntStream.range(0, (totalSamples + chunkSize - 1) / chunkSize).parallel().mapToObj(i -> {
					int startIndex = i * chunkSize;
					int endIndex = Math.min(startIndex + chunkSize, totalSamples);
					List<LSsamplemaster> currentChunk = lstsample1.subList(startIndex, endIndex);
					List<LogilabOrdermastersh> orderChunk = new ArrayList<>();
					if (testcode == -1 && objuser.getLstprojectforfilter() == null) {
						orderChunk.addAll(lslogilablimsorderdetailRepository
								.findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndApprovelstatusAndDirectorycodeIsNullOrderByBatchcodeDesc(
										currentChunk, 1, fromdate, todate, 3, pageable));
						orderChunk.addAll(lslogilablimsorderdetailRepository
								.findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusAndDirectorycodeIsNullOrderByBatchcodeDesc(
										currentChunk, 2, fromdate, todate, objuser, 3, pageable));
					} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {
						orderChunk.addAll(lslogilablimsorderdetailRepository
								.findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndApprovelstatusAndTestcodeAndDirectorycodeIsNullOrderByBatchcodeDesc(
										currentChunk, 2, fromdate, todate, objuser, 3, testcode, pageable));

						orderChunk.addAll(lslogilablimsorderdetailRepository
								.findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndApprovelstatusAndTestcodeAndDirectorycodeIsNullAndLsuserMasterNotOrderByBatchcodeDesc(
										lstsample1, 1, fromdate, todate, 3, testcode, objuser, pageable));

					}
					return orderChunk;
				}).flatMap(List::stream).collect(Collectors.toList());
				lstorders.addAll(lstorderobj);

			} else if (objuser.getObjuser().getOrderselectiontype() == 6) {// cancelled orders
				if (testcode == -1 && objuser.getLstprojectforfilter() == null) {
//					lstorders = lslogilablimsorderdetailRepository
//							.findByViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrdercancellOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellOrderByBatchcodeDesc(
//									3, fromdate, todate, objuser, 1, 
//									directorycode, 1, fromdate, todate, 1,
//									directorycode, 2, fromdate, todate, objuser, 1, 
//									directorycode, 3, fromdate, todate,objuser, 1, pageable);

					lstorders = lslogilablimsorderdetailRepository
							.findByViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellAndTeamselectedOrViewoptionAndTeamselectedAndBatchcodeInAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrdercancellOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellAndTeamselectedOrLsprojectmasterIsNullAndDirectorycodeInAndTeamselectedAndBatchcodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellOrderByBatchcodeDesc(
									3, fromdate, todate, objuser, 1, false, 3, true, selectedteambatchCodeList,
									fromdate, todate, objuser, 1, directorycode, 1, fromdate, todate, 1, directorycode,
									2, fromdate, todate, objuser, 1, directorycode, 3, fromdate, todate, objuser, 1,
									false, directorycode, true, selectedteambatchCodeList, 3, fromdate, todate, objuser,
									1, pageable);

				} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {

					lstorders = lslogilablimsorderdetailRepository
							.findByViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndOrdercancellAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellAndTestcodeOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellAndTestcodeOrderByBatchcodeDesc(
									3, fromdate, todate, objuser, 1, testcode, directorycode, 1, fromdate, todate, 1,
									testcode, directorycode, 2, fromdate, todate, objuser, 1, testcode, directorycode,
									3, fromdate, todate, objuser, 1, testcode, pageable);

				}
				lstorderobj = IntStream.range(0, (totalSamples + chunkSize - 1) / chunkSize).parallel().mapToObj(i -> {
					int startIndex = i * chunkSize;
					int endIndex = Math.min(startIndex + chunkSize, totalSamples);
					List<LSsamplemaster> currentChunk = lstsample1.subList(startIndex, endIndex);
					List<LogilabOrdermastersh> orderChunk = new ArrayList<>();
					if (testcode == -1 && objuser.getLstprojectforfilter() == null) {
						orderChunk.addAll(lslogilablimsorderdetailRepository
								.findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrdercancellAndDirectorycodeIsNullOrderByBatchcodeDesc(
										currentChunk, 1, fromdate, todate, 1, pageable));
						orderChunk.addAll(lslogilablimsorderdetailRepository
								.findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellAndDirectorycodeIsNullOrderByBatchcodeDesc(
										currentChunk, 2, fromdate, todate, objuser, 1, pageable));
					} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {
						orderChunk.addAll(lslogilablimsorderdetailRepository
								.findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndOrdercancellAndTestcodeAndDirectorycodeIsNullOrderByBatchcodeDesc(
										currentChunk, 1, fromdate, todate, 1, testcode, pageable));

						orderChunk.addAll(lslogilablimsorderdetailRepository
								.findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndOrdercancellAndTestcode(
										currentChunk, 2, fromdate, todate, objuser, 1, testcode, pageable));

					}
					return orderChunk;
				}).flatMap(List::stream).collect(Collectors.toList());
				lstorders.addAll(lstorderobj);

			}
			if (env.getProperty("client") != null && env.getProperty("client").equals("HPCL")) {
				Collections.sort(lstorders, (order1, order2) -> order2.getCt().compareTo(order1.getCt()));
			} else {
				lstorders.sort(Comparator.comparing(LogilabOrdermastersh::getBc).reversed());
			}
			lstorders.forEach(objorder -> objorder.setLw(lstworkflow));
		}
		Collections.sort(lstorders, Collections.reverseOrder());
		mapOrders.put("orderlst", lstorders);

		if (objuser.getObjsilentaudit() != null) {
			objuser.getObjsilentaudit().setTableName("LSlogilablimsorderdetail");
			objuser.getObjsilentaudit().setTransactiondate(commonfunction.getCurrentUtcTime());
			lscfttransactionRepository.save(objuser.getObjsilentaudit());
		}

		return mapOrders;
	}

//	public Map<String, Object> Getdashboardprotocolorders(LSuserMaster objuser) {
//		Date fromdate = objuser.getObjuser().getFromdate();
//		Date todate = objuser.getObjuser().getTodate();
//		Map<String, Object> mapOrders = new HashMap<String, Object>();
////		long count = 0;
//		Pageable pageable = new PageRequest(objuser.getPagesize(), objuser.getPageperorder());
//		List<ProtocolOrdersDashboard> lstorders = new ArrayList<ProtocolOrdersDashboard>();
//		List<LSprojectmaster> lstproject = objuser.getLstproject();
//		Integer testcode = objuser.getTestcode();
////		List<ProtocolOrdersDashboard> lstorders1 = new ArrayList<ProtocolOrdersDashboard>();
//		List<Integer> userlist = objuser.getUsernotify() != null
//				? objuser.getUsernotify().stream().map(LSuserMaster::getUsercode).collect(Collectors.toList())
//				: new ArrayList<Integer>();
//		if (lstproject != null) {
//
//			if (objuser.getObjuser().getOrderselectiontype() == 1) {
//				if (testcode == -1 && objuser.getLstprojectforfilter() == null) {
//
//					if (env.getProperty("app.datasource.eln.url").toLowerCase().contains("postgres")) {
//						lstorders = LSlogilabprotocoldetailRepository
//								.findByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatebyAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndCreatebyInOrLsprojectmasterInAndViewoptionAndSitecodeAndCreatedtimestampBetweenOrLsprojectmasterInAndCreatedtimestampBetweenAndSitecodeOrderByProtocolordercodeDesc(
//										1, objuser.getLssitemaster().getSitecode(), fromdate, todate, 2,
//										objuser.getLssitemaster().getSitecode(), objuser.getUsercode(), fromdate,
//										todate, 3, objuser.getLssitemaster().getSitecode(), fromdate, todate, userlist,
//										lstproject, 3, objuser.getLssitemaster().getSitecode(), fromdate, todate,
//										lstproject, fromdate, todate, objuser.getLssitemaster().getSitecode(),
//										pageable);
//
//					} else {
//						lstorders = LSlogilabprotocoldetailRepository
//								.findByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatebyAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndCreatebyInOrderByProtocolordercodeDesc(
//										1, objuser.getLssitemaster().getSitecode(), fromdate, todate, 2,
//										objuser.getLssitemaster().getSitecode(), objuser.getUsercode(), fromdate,
//										todate, 3, objuser.getLssitemaster().getSitecode(), fromdate, todate, userlist,
//										pageable);
//
//						lstorders.addAll(LSlogilabprotocoldetailRepository
//								.findByLsprojectmasterInAndViewoptionAndSitecodeAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
//										lstproject, 3, objuser.getLssitemaster().getSitecode(), fromdate, todate,
//										pageable));
//
//						lstorders.addAll(LSlogilabprotocoldetailRepository
//								.findByLsprojectmasterInAndCreatedtimestampBetweenAndSitecodeOrderByProtocolordercodeDesc(
//										lstproject, fromdate, todate, objuser.getLssitemaster().getSitecode(),
//										pageable));
//					}
//
////					count = LSlogilabprotocoldetailRepository
////							.countByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatebyAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndCreatebyIn(
////									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, 2,
////									objuser.getLssitemaster().getSitecode(), objuser.getUsercode(), fromdate, todate, 3,
////									objuser.getLssitemaster().getSitecode(), fromdate, todate, userlist);
////
////					count = count + LSlogilabprotocoldetailRepository
////							.countByLsprojectmasterInAndViewoptionAndSitecodeAndCreatedtimestampBetween(lstproject, 3,
////									objuser.getLssitemaster().getSitecode(), fromdate, todate);
////
////					count = count + LSlogilabprotocoldetailRepository
////							.countByLsprojectmasterInAndCreatedtimestampBetweenAndSitecode(lstproject, fromdate, todate,
////									objuser.getLssitemaster().getSitecode());
//
//				} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {
//
//					lstorders = LSlogilabprotocoldetailRepository
//							.findByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatebyAndCreatedtimestampBetweenAndTestcodeOrderByProtocolordercodeDesc(
//									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, testcode, 2,
//									objuser.getLssitemaster().getSitecode(), objuser.getUsercode(), fromdate, todate,
//									testcode, pageable);
//
//					lstorders.addAll(LSlogilabprotocoldetailRepository
//							.findByLsprojectmasterInAndCreatedtimestampBetweenAndSitecodeAndTestcodeOrderByProtocolordercodeDesc(
//									lstproject, fromdate, todate, objuser.getLssitemaster().getSitecode(), testcode,
//									pageable));
//
//					lstorders.addAll(LSlogilabprotocoldetailRepository
//							.findByLsprojectmasterInAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndTestcodeOrderByProtocolordercodeDesc(
//									lstproject, 3, objuser.getLssitemaster().getSitecode(), fromdate, todate, testcode,
//									pageable));
//
////					count = LSlogilabprotocoldetailRepository
////							.countByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatebyAndCreatedtimestampBetweenAndTestcode(
////									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, testcode, 2,
////									objuser.getLssitemaster().getSitecode(), objuser.getUsercode(), fromdate, todate,
////									testcode);
////
////					count = count + LSlogilabprotocoldetailRepository
////							.countByLsprojectmasterInAndCreatedtimestampBetweenAndSitecodeAndTestcode(lstproject,
////									fromdate, todate, objuser.getLssitemaster().getSitecode(), testcode);
////
////					count = count + LSlogilabprotocoldetailRepository
////							.countByLsprojectmasterInAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndTestcode(
////									lstproject, 3, objuser.getLssitemaster().getSitecode(), fromdate, todate, testcode);
//
//				} else if (testcode == -1 && objuser.getLstprojectforfilter() != null) {
//					lstorders = LSlogilabprotocoldetailRepository
//							.findByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterAndOrdercancellIsNullOrOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterAndRejectedIsNullOrRejectedAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterOrOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterOrderByProtocolordercodeDesc(
//									"N", objuser.getLssitemaster().getSitecode(), fromdate, todate,
//									objuser.getLstprojectforfilter(), "R", objuser.getLssitemaster().getSitecode(),
//									fromdate, todate, objuser.getLstprojectforfilter(), 1,
//									objuser.getLssitemaster().getSitecode(), fromdate, todate,
//									objuser.getLstprojectforfilter(), 1, objuser.getLssitemaster().getSitecode(),
//									fromdate, todate, objuser.getLstprojectforfilter(), pageable);
////					count = LSlogilabprotocoldetailRepository
////							.countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterAndOrdercancellIsNullOrOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterAndRejectedIsNullOrRejectedAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterOrOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmaster(
////									"N", objuser.getLssitemaster().getSitecode(), fromdate, todate,
////									objuser.getLstprojectforfilter(), "R", objuser.getLssitemaster().getSitecode(),
////									fromdate, todate, objuser.getLstprojectforfilter(), 1,
////									objuser.getLssitemaster().getSitecode(), fromdate, todate,
////									objuser.getLstprojectforfilter(), 1, objuser.getLssitemaster().getSitecode(),
////									fromdate, todate, objuser.getLstprojectforfilter());
//
//				} else {
//					lstorders = LSlogilabprotocoldetailRepository
//							.findByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterAndOrdercancellIsNullAndTestcodeOrOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterAndRejectedIsNullAndTestcodeOrRejectedAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterAndTestcodeOrOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterAndTestcodeOrderByProtocolordercodeDesc(
//									"N", objuser.getLssitemaster().getSitecode(), fromdate, todate,
//									objuser.getLstprojectforfilter(), testcode, "R",
//									objuser.getLssitemaster().getSitecode(), fromdate, todate,
//									objuser.getLstprojectforfilter(), testcode, 1,
//									objuser.getLssitemaster().getSitecode(), fromdate, todate,
//									objuser.getLstprojectforfilter(), testcode, 1,
//									objuser.getLssitemaster().getSitecode(), fromdate, todate,
//									objuser.getLstprojectforfilter(), testcode, pageable);
////					count = LSlogilabprotocoldetailRepository
////							.countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterAndOrdercancellIsNullAndTestcodeOrOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterAndRejectedIsNullAndTestcodeOrRejectedAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterAndTestcodeOrOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterAndTestcode(
////									"N", objuser.getLssitemaster().getSitecode(), fromdate, todate,
////									objuser.getLstprojectforfilter(), testcode, "R",
////									objuser.getLssitemaster().getSitecode(), fromdate, todate,
////									objuser.getLstprojectforfilter(), testcode, 1,
////									objuser.getLssitemaster().getSitecode(), fromdate, todate,
////									objuser.getLstprojectforfilter(), testcode, 1,
////									objuser.getLssitemaster().getSitecode(), fromdate, todate,
////									objuser.getLstprojectforfilter(), testcode);
//
//				}
//			} else if (objuser.getObjuser().getOrderselectiontype() == 2) {
//				if (testcode == -1 && objuser.getLstprojectforfilter() == null) {
//
//					lstorders = LSlogilabprotocoldetailRepository
//							.findByOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenOrOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenOrOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyInOrderByProtocolordercodeDesc(
//									"R", objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, "R",
//									objuser.getLssitemaster().getSitecode(), 2, objuser.getUsercode(), fromdate, todate,
//									"R", objuser.getLssitemaster().getSitecode(), 3, fromdate, todate, userlist,
//									pageable);
//
//					lstorders.addAll(LSlogilabprotocoldetailRepository
//							.findByOrderflagAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndRejectedIsNullOrderByProtocolordercodeDesc(
//									"R", objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject,
//									pageable));
//
////					count = LSlogilabprotocoldetailRepository
////							.countByOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenOrOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenOrOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyIn(
////									"R", objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, "R",
////									objuser.getLssitemaster().getSitecode(), 2, objuser.getUsercode(), fromdate, todate,
////									"R", objuser.getLssitemaster().getSitecode(), 3, fromdate, todate, userlist);
////
////					count = count + LSlogilabprotocoldetailRepository
////							.countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndRejectedIsNull(
////									"R", objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject);
//
//				} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {
//
//					lstorders = LSlogilabprotocoldetailRepository
//							.findByOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndTestcodeOrderByProtocolordercodeDesc(
//									"R", objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, testcode, "R",
//									objuser.getLssitemaster().getSitecode(), 2, objuser.getUsercode(), fromdate, todate,
//									testcode, pageable);
//
//					lstorders.addAll(LSlogilabprotocoldetailRepository
//							.findByOrderflagAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndRejectedIsNullAndTestcodeOrderByProtocolordercodeDesc(
//									"R", objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject,
//									testcode, pageable));
//
//					lstorders.addAll(LSlogilabprotocoldetailRepository
//							.findByOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterInAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrderByProtocolordercodeDesc(
//									"R", objuser.getLssitemaster().getSitecode(), lstproject, 3, fromdate, todate,
//									testcode, pageable));
//
////					count = LSlogilabprotocoldetailRepository
////							.countByOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndTestcode(
////									"R", objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, testcode, "R",
////									objuser.getLssitemaster().getSitecode(), 2, objuser.getUsercode(), fromdate, todate,
////									testcode);
////
////					count = count + LSlogilabprotocoldetailRepository
////							.countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndRejectedIsNullAndTestcode(
////									"R", objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject,
////									testcode);
////
////					count = count + LSlogilabprotocoldetailRepository
////							.countByOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterInAndViewoptionAndCreatedtimestampBetweenAndTestcode(
////									"R", objuser.getLssitemaster().getSitecode(), lstproject, 3, fromdate, todate,
////									testcode);
//
//				} else if (testcode == -1 && objuser.getLstprojectforfilter() != null) {
//					lstorders = LSlogilabprotocoldetailRepository
//							.findByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterAndRejectedIsNullOrderByProtocolordercodeDesc(
//									"R", objuser.getLssitemaster().getSitecode(), fromdate, todate,
//									objuser.getLstprojectforfilter(), pageable);
//
////					count = LSlogilabprotocoldetailRepository
////							.countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterAndRejectedIsNull(
////									"R", objuser.getLssitemaster().getSitecode(), fromdate, todate,
////									objuser.getLstprojectforfilter());
//
//				} else {
//					lstorders = LSlogilabprotocoldetailRepository
//							.findByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterAndRejectedIsNullAndTestcodeOrderByProtocolordercodeDesc(
//									"R", objuser.getLssitemaster().getSitecode(), fromdate, todate,
//									objuser.getLstprojectforfilter(), testcode, pageable);
//
////					count = LSlogilabprotocoldetailRepository
////							.countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterAndRejectedIsNullAndTestcode(
////									"R", objuser.getLssitemaster().getSitecode(), fromdate, todate,
////									objuser.getLstprojectforfilter(), testcode);
//				}
//
//			} else if (objuser.getObjuser().getOrderselectiontype() == 3) {
//
//				if (testcode == -1 && objuser.getLstprojectforfilter() == null) {
//
//					if (env.getProperty("app.datasource.eln.url").toLowerCase().contains("postgres")) {
//						lstorders = LSlogilabprotocoldetailRepository
//								.findByOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndRejectedIsNullOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndRejectedIsNullOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndRejectedIsNullAndCreatebyInOrOrderflagAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndOrdercancellIsNullAndRejectedIsNullOrderByProtocolordercodeDesc(
//										"N", objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, "N",
//										objuser.getLssitemaster().getSitecode(), 2, objuser.getUsercode(), fromdate,
//										todate, "N", objuser.getLssitemaster().getSitecode(), 3, fromdate, todate,
//										userlist, "N", objuser.getLssitemaster().getSitecode(), fromdate, todate,
//										lstproject, pageable);
//					} else {
//						lstorders = LSlogilabprotocoldetailRepository
//								.findByOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndRejectedIsNullOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndRejectedIsNullOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndRejectedIsNullAndCreatebyInOrderByProtocolordercodeDesc(
//										"N", objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, "N",
//										objuser.getLssitemaster().getSitecode(), 2, objuser.getUsercode(), fromdate,
//										todate, "N", objuser.getLssitemaster().getSitecode(), 3, fromdate, todate,
//										userlist, pageable);
//
//						lstorders.addAll(LSlogilabprotocoldetailRepository
//								.findByOrderflagAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndOrdercancellIsNullAndRejectedIsNullOrderByProtocolordercodeDesc(
//										"N", objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject,
//										pageable));
//					}
//
////					count = LSlogilabprotocoldetailRepository
////							.countByOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndRejectedIsNullOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndRejectedIsNullOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndRejectedIsNullAndCreatebyIn(
////									"N", objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, "N",
////									objuser.getLssitemaster().getSitecode(), 2, objuser.getUsercode(), fromdate, todate,
////									"N", objuser.getLssitemaster().getSitecode(), 3, fromdate, todate, userlist);
////
////					count = count + LSlogilabprotocoldetailRepository
////							.countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndOrdercancellIsNullAndRejectedIsNullOrderByProtocolordercodeDesc(
////									"N", objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject);
//
//				} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {
//
//					lstorders = LSlogilabprotocoldetailRepository
//							.findByOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndTestcodeAndRejectedIsNullOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndTestcodeAndRejectedIsNullOrderByProtocolordercodeDesc(
//									"N", objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, testcode, "N",
//									objuser.getLssitemaster().getSitecode(), 2, fromdate, objuser.getUsercode(), todate,
//									testcode, pageable);
//
//					lstorders.addAll(LSlogilabprotocoldetailRepository
//							.findByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterInAndOrdercancellIsNullAndTestcodeAndRejectedIsNullOrderByProtocolordercodeDesc(
//									"N", objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject,
//									testcode, pageable));
//
//					lstorders.addAll(LSlogilabprotocoldetailRepository
//							.findByOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterInAndViewoptionAndCreatedtimestampBetweenAndTestcodeAndRejectedIsNullOrderByProtocolordercodeDesc(
//									"N", objuser.getLssitemaster().getSitecode(), lstproject, 3, fromdate, todate,
//									testcode, pageable));
//
////					count = LSlogilabprotocoldetailRepository
////							.countByOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndTestcodeAndRejectedIsNullOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndTestcodeAndRejectedIsNull(
////									"N", objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, testcode, "N",
////									objuser.getLssitemaster().getSitecode(), 2, fromdate, objuser.getUsercode(), todate,
////									testcode);
////
////					count = count + LSlogilabprotocoldetailRepository
////							.countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterInAndOrdercancellIsNullAndTestcodeAndRejectedIsNull(
////									"N", objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject,
////									testcode);
////
////					count = count + LSlogilabprotocoldetailRepository
////							.countByOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterInAndViewoptionAndCreatedtimestampBetweenAndTestcodeAndRejectedIsNull(
////									"N", objuser.getLssitemaster().getSitecode(), lstproject, 3, fromdate, todate,
////									testcode);
//
//				} else if (testcode == -1 && objuser.getLstprojectforfilter() != null) {
//					lstorders = LSlogilabprotocoldetailRepository
//							.findByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterAndOrdercancellIsNullAndRejectedIsNullOrderByProtocolordercodeDesc(
//									"N", objuser.getLssitemaster().getSitecode(), fromdate, todate,
//									objuser.getLstprojectforfilter(), pageable);
//
////					count = LSlogilabprotocoldetailRepository
////							.countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterAndOrdercancellIsNullAndRejectedIsNull(
////									"N", objuser.getLssitemaster().getSitecode(), fromdate, todate,
////									objuser.getLstprojectforfilter());
//
//				} else {
//					lstorders = LSlogilabprotocoldetailRepository
//							.findByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterAndOrdercancellIsNullAndTestcodeAndRejectedIsNullOrderByProtocolordercodeDesc(
//									"N", objuser.getLssitemaster().getSitecode(), fromdate, todate,
//									objuser.getLstprojectforfilter(), testcode, pageable);
//
////					count = LSlogilabprotocoldetailRepository
////							.countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterAndOrdercancellIsNullAndTestcodeAndRejectedIsNull(
////									"N", objuser.getLssitemaster().getSitecode(), fromdate, todate,
////									objuser.getLstprojectforfilter(), testcode);
//
//				}
//			} else if (objuser.getObjuser().getOrderselectiontype() == 5) {
//				if (testcode == -1 && objuser.getLstprojectforfilter() == null) {
//
//					lstorders = LSlogilabprotocoldetailRepository
//							.findByRejectedAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenOrRejectedAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenOrRejectedAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyInOrderByProtocolordercodeDesc(
//									1, objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, 1,
//									objuser.getLssitemaster().getSitecode(), 2, objuser.getUsercode(), fromdate, todate,
//									1, objuser.getLssitemaster().getSitecode(), 3, fromdate, todate, userlist,
//									pageable);
//
//					lstorders.addAll(LSlogilabprotocoldetailRepository
//							.findByRejectedAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInOrderByProtocolordercodeDesc(
//									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject,
//									pageable));
//
////					count = LSlogilabprotocoldetailRepository
////							.countByRejectedAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenOrRejectedAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenOrRejectedAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyIn(
////									1, objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, 1,
////									objuser.getLssitemaster().getSitecode(), 2, objuser.getUsercode(), fromdate, todate,
////									1, objuser.getLssitemaster().getSitecode(), 3, fromdate, todate, userlist);
////
////					count = count + LSlogilabprotocoldetailRepository
////							.countByRejectedAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterIn(1,
////									objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject);
//
//				} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {
//
//					lstorders = LSlogilabprotocoldetailRepository
//							.findByRejectedAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrRejectedAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndTestcodeOrderByProtocolordercodeDesc(
//									1, objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, testcode, 1,
//									objuser.getLssitemaster().getSitecode(), 2, objuser.getUsercode(), fromdate, todate,
//									testcode, pageable);
//
//					lstorders.addAll(LSlogilabprotocoldetailRepository
//							.findByRejectedAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndTestcodeOrderByProtocolordercodeDesc(
//									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject, testcode,
//									pageable));
//
//					lstorders.addAll(LSlogilabprotocoldetailRepository
//							.findByRejectedAndSitecodeAndLsprojectmasterInAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrderByProtocolordercodeDesc(
//									1, objuser.getLssitemaster().getSitecode(), lstproject, 3, fromdate, todate,
//									testcode, pageable));
//
////					count = LSlogilabprotocoldetailRepository
////							.countByRejectedAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrRejectedAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndTestcode(
////									1, objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, testcode, 1,
////									objuser.getLssitemaster().getSitecode(), 2, objuser.getUsercode(), fromdate, todate,
////									testcode);
////
////					count = count + LSlogilabprotocoldetailRepository
////							.countByRejectedAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndTestcode(1,
////									objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject, testcode);
////
////					count = count + LSlogilabprotocoldetailRepository
////							.countByRejectedAndSitecodeAndLsprojectmasterInAndViewoptionAndCreatedtimestampBetweenAndTestcode(
////									1, objuser.getLssitemaster().getSitecode(), lstproject, 3, fromdate, todate,
////									testcode);
//
//				} else if (testcode == -1 && objuser.getLstprojectforfilter() != null) {
//					lstorders = LSlogilabprotocoldetailRepository
//							.findByRejectedAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterOrderByProtocolordercodeDesc(
//									1, objuser.getLssitemaster().getSitecode(), fromdate, todate,
//									objuser.getLstprojectforfilter(), pageable);
//
////					count = LSlogilabprotocoldetailRepository
////							.countByRejectedAndSitecodeAndCreatedtimestampBetweenAndLsprojectmaster(1,
////									objuser.getLssitemaster().getSitecode(), fromdate, todate,
////									objuser.getLstprojectforfilter());
//
//				} else {
//					lstorders = LSlogilabprotocoldetailRepository
//							.findByRejectedAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterAndTestcodeOrderByProtocolordercodeDesc(
//									1, objuser.getLssitemaster().getSitecode(), fromdate, todate,
//									objuser.getLstprojectforfilter(), testcode, pageable);
//
////					count = LSlogilabprotocoldetailRepository
////							.countByRejectedAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterAndTestcode(1,
////									objuser.getLssitemaster().getSitecode(), fromdate, todate,
////									objuser.getLstprojectforfilter(), testcode);
//				}
//
//			} else if (objuser.getObjuser().getOrderselectiontype() == 6) {
//				if (testcode == -1 && objuser.getLstprojectforfilter() == null) {
//
//					lstorders = LSlogilabprotocoldetailRepository
//							.findByOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyInOrderByProtocolordercodeDesc(
//									1, objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, 1,
//									objuser.getLssitemaster().getSitecode(), 2, objuser.getUsercode(), fromdate, todate,
//									1, objuser.getLssitemaster().getSitecode(), 3, fromdate, todate, userlist,
//									pageable);
//
//					lstorders.addAll(LSlogilabprotocoldetailRepository
//							.findByOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInOrderByProtocolordercodeDesc(
//									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject,
//									pageable));
//
////					count = LSlogilabprotocoldetailRepository
////							.countByOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyIn(
////									1, objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, 1,
////									objuser.getLssitemaster().getSitecode(), 2, objuser.getUsercode(), fromdate, todate,
////									1, objuser.getLssitemaster().getSitecode(), 3, fromdate, todate, userlist);
////
////					count = count + LSlogilabprotocoldetailRepository
////							.countByOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterIn(1,
////									objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject);
//
//				} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {
//
//					lstorders = LSlogilabprotocoldetailRepository
//							.findByOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndTestcodeOrderByProtocolordercodeDesc(
//									1, objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, testcode, 1,
//									objuser.getLssitemaster().getSitecode(), 2, objuser.getUsercode(), fromdate, todate,
//									testcode, pageable);
//
//					lstorders.addAll(LSlogilabprotocoldetailRepository
//							.findByOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndTestcodeOrderByProtocolordercodeDesc(
//									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject, testcode,
//									pageable));
//
//					lstorders.addAll(LSlogilabprotocoldetailRepository
//							.findByOrdercancellAndSitecodeAndLsprojectmasterInAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrderByProtocolordercodeDesc(
//									1, objuser.getLssitemaster().getSitecode(), lstproject, 3, fromdate, todate,
//									testcode, pageable));
//
////					count = LSlogilabprotocoldetailRepository
////							.countByOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndTestcode(
////									1, objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, testcode, 1,
////									objuser.getLssitemaster().getSitecode(), 2, objuser.getUsercode(), fromdate, todate,
////									testcode);
////
////					count = count + LSlogilabprotocoldetailRepository
////							.countByOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndTestcode(1,
////									objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject, testcode);
////
////					count = count + LSlogilabprotocoldetailRepository
////							.countByOrdercancellAndSitecodeAndLsprojectmasterInAndViewoptionAndCreatedtimestampBetweenAndTestcode(
////									1, objuser.getLssitemaster().getSitecode(), lstproject, 3, fromdate, todate,
////									testcode);
//
//				} else if (testcode == -1 && objuser.getLstprojectforfilter() != null) {
//					lstorders = LSlogilabprotocoldetailRepository
//							.findByOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterOrderByProtocolordercodeDesc(
//									1, objuser.getLssitemaster().getSitecode(), fromdate, todate,
//									objuser.getLstprojectforfilter(), pageable);
//
////					count = LSlogilabprotocoldetailRepository
////							.countByOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmaster(1,
////									objuser.getLssitemaster().getSitecode(), fromdate, todate,
////									objuser.getLstprojectforfilter());
//				} else {
//					lstorders = LSlogilabprotocoldetailRepository
//							.findByOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterAndTestcodeOrderByProtocolordercodeDesc(
//									1, objuser.getLssitemaster().getSitecode(), fromdate, todate,
//									objuser.getLstprojectforfilter(), testcode, pageable);
//
////					count = LSlogilabprotocoldetailRepository
////							.countByOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterAndTestcode(1,
////									objuser.getLssitemaster().getSitecode(), fromdate, todate,
////									objuser.getLstprojectforfilter(), testcode);
//				}
//
//			}
//		} else {
//			if (objuser.getObjuser().getOrderselectiontype() == 1) {
//				if (testcode == -1 && objuser.getLstprojectforfilter() == null) {
//					lstorders = LSlogilabprotocoldetailRepository
//							.findByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
//									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, 2,
//									objuser.getUsercode(), fromdate, todate, 3, objuser.getUsercode(), fromdate, todate,
//									pageable);
//
////					count = LSlogilabprotocoldetailRepository
////							.countByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
////									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, 2,
////									objuser.getUsercode(), fromdate, todate, 3, objuser.getUsercode(), fromdate,
////									todate);
//				} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {
//					lstorders = LSlogilabprotocoldetailRepository
//							.findByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndTestcodeOrderByProtocolordercodeDesc(
//									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, testcode, 2,
//									objuser.getUsercode(), fromdate, todate, testcode, 3, objuser.getUsercode(),
//									fromdate, todate, testcode, pageable);
////					count = LSlogilabprotocoldetailRepository
////							.countByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndTestcodeOrderByProtocolordercodeDesc(
////									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, testcode, 2,
////									objuser.getUsercode(), fromdate, todate, testcode, 3, objuser.getUsercode(),
////									fromdate, todate, testcode);
//				}
//			} else if (objuser.getObjuser().getOrderselectiontype() == 2) {
//				if (testcode == -1 && objuser.getLstprojectforfilter() == null) {
//					lstorders = LSlogilabprotocoldetailRepository
//							.findByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndOrderflagAndRejectedIsNullOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrderflagAndRejectedIsNullOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrderflagAndRejectedIsNullOrderByProtocolordercodeDesc(
//									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, "R", 2,
//									objuser.getUsercode(), fromdate, todate, "R", 3, objuser.getUsercode(), fromdate,
//									todate, "R", pageable);
//
////					count = LSlogilabprotocoldetailRepository
////							.countByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndOrderflagAndRejectedIsNullOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrderflagAndRejectedIsNullOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrderflagAndRejectedIsNullOrderByProtocolordercodeDesc(
////									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, "R", 2,
////									objuser.getUsercode(), fromdate, todate, "R", 3, objuser.getUsercode(), fromdate,
////									todate, "R");
//				} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {
//					lstorders = LSlogilabprotocoldetailRepository
//							.findByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndOrderflagAndRejectedIsNullAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrderflagAndRejectedIsNullAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrderflagAndRejectedIsNullAndTestcodeOrderByProtocolordercodeDesc(
//									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, "R", testcode, 2,
//									objuser.getUsercode(), fromdate, todate, "R", testcode, 3, objuser.getUsercode(),
//									fromdate, todate, "R", testcode, pageable);
////					count = LSlogilabprotocoldetailRepository
////							.countByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndOrderflagAndRejectedIsNullAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrderflagAndRejectedIsNullAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrderflagAndRejectedIsNullAndTestcodeOrderByProtocolordercodeDesc(
////									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, "R", testcode, 2,
////									objuser.getUsercode(), fromdate, todate, "R", testcode, 3, objuser.getUsercode(),
////									fromdate, todate, "R", testcode);
//				}
//
//			} else if (objuser.getObjuser().getOrderselectiontype() == 3) {
//
//				if (testcode == -1 && objuser.getLstprojectforfilter() == null) {
//					lstorders = LSlogilabprotocoldetailRepository
//							.findByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullOrderByProtocolordercodeDesc(
//									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, "N", 2,
//									objuser.getUsercode(), fromdate, todate, "N", 3, objuser.getUsercode(), fromdate,
//									todate, "N", pageable);
////					count = LSlogilabprotocoldetailRepository
////							.countByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullOrderByProtocolordercodeDesc(
////									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, "N", 2,
////									objuser.getUsercode(), fromdate, todate, "N", 3, objuser.getUsercode(), fromdate,
////									todate, "N");
//				} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {
//					lstorders = LSlogilabprotocoldetailRepository
//							.findByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullAndTestcodeOrderByProtocolordercodeDesc(
//									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, "N", testcode, 2,
//									objuser.getUsercode(), fromdate, todate, "N", testcode, 3, objuser.getUsercode(),
//									fromdate, todate, "N", testcode, pageable);
////					count = LSlogilabprotocoldetailRepository
////							.countByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullAndTestcodeOrderByProtocolordercodeDesc(
////									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, "N", testcode, 2,
////									objuser.getUsercode(), fromdate, todate, "N", testcode, 3, objuser.getUsercode(),
////									fromdate, todate, "N", testcode);
//				}
//			} else if (objuser.getObjuser().getOrderselectiontype() == 5) {
//				if (testcode == -1 && objuser.getLstprojectforfilter() == null) {
//					lstorders = LSlogilabprotocoldetailRepository
//							.findByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndRejectedOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndRejectedOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndRejectedOrderByProtocolordercodeDesc(
//									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, 1, 2,
//									objuser.getUsercode(), fromdate, todate, 1, 3, objuser.getUsercode(), fromdate,
//									todate, 1, pageable);
////					count = LSlogilabprotocoldetailRepository
////							.countByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndRejectedOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndRejectedOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndRejectedOrderByProtocolordercodeDesc(
////									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, 1, 2,
////									objuser.getUsercode(), fromdate, todate, 1, 3, objuser.getUsercode(), fromdate,
////									todate, 1);
//				} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {
//					lstorders = LSlogilabprotocoldetailRepository
//							.findByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndRejectedAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndRejectedAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndRejectedAndTestcodeOrderByProtocolordercodeDesc(
//									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, 1, testcode, 2,
//									objuser.getUsercode(), fromdate, todate, 1, testcode, 3, objuser.getUsercode(),
//									fromdate, todate, 1, testcode, pageable);
////					count = LSlogilabprotocoldetailRepository
////							.countByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndRejectedAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndRejectedAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndRejectedAndTestcodeOrderByProtocolordercodeDesc(
////									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, 1, testcode, 2,
////									objuser.getUsercode(), fromdate, todate, 1, testcode, 3, objuser.getUsercode(),
////									fromdate, todate, 1, testcode);
//
//				}
//			} else if (objuser.getObjuser().getOrderselectiontype() == 6) {
//				if (testcode == -1 && objuser.getLstprojectforfilter() == null) {
//					lstorders = LSlogilabprotocoldetailRepository
//							.findByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndOrdercancellOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrdercancellOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrdercancellOrderByProtocolordercodeDesc(
//									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, 1, 2,
//									objuser.getUsercode(), fromdate, todate, 1, 3, objuser.getUsercode(), fromdate,
//									todate, 1, pageable);
////					count = LSlogilabprotocoldetailRepository
////							.countByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndOrdercancellOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrdercancellOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrdercancellOrderByProtocolordercodeDesc(
////									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, 1, 2,
////									objuser.getUsercode(), fromdate, todate, 1, 3, objuser.getUsercode(), fromdate,
////									todate, 1);
//				} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {
//					lstorders = LSlogilabprotocoldetailRepository
//							.findByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndOrdercancellAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrdercancellAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrdercancellAndTestcodeOrderByProtocolordercodeDesc(
//									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, 1, testcode, 2,
//									objuser.getUsercode(), fromdate, todate, 1, testcode, 3, objuser.getUsercode(),
//									fromdate, todate, 1, testcode, pageable);
////					count = LSlogilabprotocoldetailRepository
////							.countByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndOrdercancellAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrdercancellAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrdercancellAndTestcodeOrderByProtocolordercodeDesc(
////									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, 1, testcode, 2,
////									objuser.getUsercode(), fromdate, todate, 1, testcode, 3, objuser.getUsercode(),
////									fromdate, todate, 1, testcode);
//				}
//
//			}
//		}
//
////		lstorders.forEach(objorderDetail -> objorderDetail.setLstworkflow(objuser.getLstworkflow()));
//		lstorders.forEach(
//				objorderDetail -> objorderDetail.setLstelnprotocolworkflow(objuser.getLstelnprotocolworkflow()));
//		Collections.sort(lstorders, Collections.reverseOrder());
//		mapOrders.put("orderlst", lstorders);
////		mapOrders.put("count", count);
//		return mapOrders;
//	}

	public Map<String, Object> Getdashboardprotocolorders(LSuserMaster objuser) {
		Date fromdate = objuser.getObjuser().getFromdate();
		Date todate = objuser.getObjuser().getTodate();
		Map<String, Object> mapOrders = new HashMap<String, Object>();
//		long count = 0;
		Pageable pageable = new PageRequest(objuser.getPagesize(), objuser.getPageperorder());
		List<ProtocolOrdersDashboard> lstorders = new ArrayList<ProtocolOrdersDashboard>();
		List<LSprojectmaster> lstproject = objuser.getLstproject();
		Integer testcode = objuser.getTestcode();
//		List<ProtocolOrdersDashboard> lstorders1 = new ArrayList<ProtocolOrdersDashboard>();
		List<Integer> userlist = objuser.getUsernotify() != null
				? objuser.getUsernotify().stream().map(LSuserMaster::getUsercode).collect(Collectors.toList())
				: new ArrayList<Integer>();

		List<LSuserteammapping> lstteammap = lsuserteammappingRepository.findBylsuserMaster(objuser);
		List<LSusersteam> lstteam = lsusersteamRepository.findByLsuserteammappingInAndLssitemaster(lstteammap,
				objuser.getLssitemaster());

		List<LSprotocolselectedteam> selectedteamorders = lsprotoselectedteamRepo
				.findByUserteamInAndCreatedtimestampBetween(lstteam, fromdate, todate);

		List<Long> selectedteamprotcolorderList = (selectedteamorders != null && !selectedteamorders.isEmpty())
				? selectedteamorders.stream().map(LSprotocolselectedteam::getProtocolordercode).filter(Objects::nonNull)
						.distinct().collect(Collectors.toList())
				: Collections.singletonList(-1L);

		if (lstproject != null) {

			if (objuser.getObjuser().getOrderselectiontype() == 1) {// total orders
				if (testcode == -1 && objuser.getLstprojectforfilter() == null) {

					if (env.getProperty("app.datasource.eln.url").toLowerCase().contains("postgres")) {
//						lstorders = LSlogilabprotocoldetailRepository
//								.findByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatebyAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndCreatebyInOrLsprojectmasterInAndViewoptionAndSitecodeAndCreatedtimestampBetweenOrLsprojectmasterInAndCreatedtimestampBetweenAndSitecodeOrderByProtocolordercodeDesc(
//										1, objuser.getLssitemaster().getSitecode(), fromdate, todate, 2,
//										objuser.getLssitemaster().getSitecode(), objuser.getUsercode(), fromdate,
//										todate, 3, objuser.getLssitemaster().getSitecode(), fromdate, todate, userlist,
//										lstproject, 3, objuser.getLssitemaster().getSitecode(), fromdate, todate,
//										lstproject, fromdate, todate, objuser.getLssitemaster().getSitecode(),
//										pageable);
						/** change for p.team selection */
						lstorders = LSlogilabprotocoldetailRepository
								.findByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatebyAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndCreatebyInAndTeamselectedOrTeamselectedAndProtocolordercodeInAndLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndCreatebyInOrLsprojectmasterInAndViewoptionAndSitecodeAndCreatedtimestampBetweenOrLsprojectmasterInAndCreatedtimestampBetweenAndSitecodeOrderByProtocolordercodeDesc(
										1, objuser.getLssitemaster().getSitecode(), fromdate, todate, 2,
										objuser.getLssitemaster().getSitecode(), objuser.getUsercode(), fromdate,
										todate, 3, objuser.getLssitemaster().getSitecode(), fromdate, todate, userlist,
										false, true, selectedteamprotcolorderList, 3,
										objuser.getLssitemaster().getSitecode(), fromdate, todate, userlist, lstproject,
										3, objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject,
										fromdate, todate, objuser.getLssitemaster().getSitecode(), pageable);

					} else {
						lstorders = LSlogilabprotocoldetailRepository
								.findByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatebyAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndCreatebyInOrderByProtocolordercodeDesc(
										1, objuser.getLssitemaster().getSitecode(), fromdate, todate, 2,
										objuser.getLssitemaster().getSitecode(), objuser.getUsercode(), fromdate,
										todate, 3, objuser.getLssitemaster().getSitecode(), fromdate, todate, userlist,
										pageable);

						lstorders.addAll(LSlogilabprotocoldetailRepository
								.findByLsprojectmasterInAndViewoptionAndSitecodeAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
										lstproject, 3, objuser.getLssitemaster().getSitecode(), fromdate, todate,
										pageable));

						lstorders.addAll(LSlogilabprotocoldetailRepository
								.findByLsprojectmasterInAndCreatedtimestampBetweenAndSitecodeOrderByProtocolordercodeDesc(
										lstproject, fromdate, todate, objuser.getLssitemaster().getSitecode(),
										pageable));
					}

//					count = LSlogilabprotocoldetailRepository
//							.countByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatebyAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndCreatebyIn(
//									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, 2,
//									objuser.getLssitemaster().getSitecode(), objuser.getUsercode(), fromdate, todate, 3,
//									objuser.getLssitemaster().getSitecode(), fromdate, todate, userlist);
//
//					count = count + LSlogilabprotocoldetailRepository
//							.countByLsprojectmasterInAndViewoptionAndSitecodeAndCreatedtimestampBetween(lstproject, 3,
//									objuser.getLssitemaster().getSitecode(), fromdate, todate);
//
//					count = count + LSlogilabprotocoldetailRepository
//							.countByLsprojectmasterInAndCreatedtimestampBetweenAndSitecode(lstproject, fromdate, todate,
//									objuser.getLssitemaster().getSitecode());

				} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {

					lstorders = LSlogilabprotocoldetailRepository
							.findByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatebyAndCreatedtimestampBetweenAndTestcodeOrderByProtocolordercodeDesc(
									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, testcode, 2,
									objuser.getLssitemaster().getSitecode(), objuser.getUsercode(), fromdate, todate,
									testcode, pageable);

					lstorders.addAll(LSlogilabprotocoldetailRepository
							.findByLsprojectmasterInAndCreatedtimestampBetweenAndSitecodeAndTestcodeOrderByProtocolordercodeDesc(
									lstproject, fromdate, todate, objuser.getLssitemaster().getSitecode(), testcode,
									pageable));

					lstorders.addAll(LSlogilabprotocoldetailRepository
							.findByLsprojectmasterInAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndTestcodeOrderByProtocolordercodeDesc(
									lstproject, 3, objuser.getLssitemaster().getSitecode(), fromdate, todate, testcode,
									pageable));

//					count = LSlogilabprotocoldetailRepository
//							.countByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatebyAndCreatedtimestampBetweenAndTestcode(
//									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, testcode, 2,
//									objuser.getLssitemaster().getSitecode(), objuser.getUsercode(), fromdate, todate,
//									testcode);
//
//					count = count + LSlogilabprotocoldetailRepository
//							.countByLsprojectmasterInAndCreatedtimestampBetweenAndSitecodeAndTestcode(lstproject,
//									fromdate, todate, objuser.getLssitemaster().getSitecode(), testcode);
//
//					count = count + LSlogilabprotocoldetailRepository
//							.countByLsprojectmasterInAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndTestcode(
//									lstproject, 3, objuser.getLssitemaster().getSitecode(), fromdate, todate, testcode);

				} else if (testcode == -1 && objuser.getLstprojectforfilter() != null) {
					lstorders = LSlogilabprotocoldetailRepository
							.findByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterAndOrdercancellIsNullOrOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterAndRejectedIsNullOrRejectedAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterOrOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterOrderByProtocolordercodeDesc(
									"N", objuser.getLssitemaster().getSitecode(), fromdate, todate,
									objuser.getLstprojectforfilter(), "R", objuser.getLssitemaster().getSitecode(),
									fromdate, todate, objuser.getLstprojectforfilter(), 1,
									objuser.getLssitemaster().getSitecode(), fromdate, todate,
									objuser.getLstprojectforfilter(), 1, objuser.getLssitemaster().getSitecode(),
									fromdate, todate, objuser.getLstprojectforfilter(), pageable);
//					count = LSlogilabprotocoldetailRepository
//							.countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterAndOrdercancellIsNullOrOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterAndRejectedIsNullOrRejectedAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterOrOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmaster(
//									"N", objuser.getLssitemaster().getSitecode(), fromdate, todate,
//									objuser.getLstprojectforfilter(), "R", objuser.getLssitemaster().getSitecode(),
//									fromdate, todate, objuser.getLstprojectforfilter(), 1,
//									objuser.getLssitemaster().getSitecode(), fromdate, todate,
//									objuser.getLstprojectforfilter(), 1, objuser.getLssitemaster().getSitecode(),
//									fromdate, todate, objuser.getLstprojectforfilter());

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
//					count = LSlogilabprotocoldetailRepository
//							.countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterAndOrdercancellIsNullAndTestcodeOrOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterAndRejectedIsNullAndTestcodeOrRejectedAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterAndTestcodeOrOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterAndTestcode(
//									"N", objuser.getLssitemaster().getSitecode(), fromdate, todate,
//									objuser.getLstprojectforfilter(), testcode, "R",
//									objuser.getLssitemaster().getSitecode(), fromdate, todate,
//									objuser.getLstprojectforfilter(), testcode, 1,
//									objuser.getLssitemaster().getSitecode(), fromdate, todate,
//									objuser.getLstprojectforfilter(), testcode, 1,
//									objuser.getLssitemaster().getSitecode(), fromdate, todate,
//									objuser.getLstprojectforfilter(), testcode);

				}
			} else if (objuser.getObjuser().getOrderselectiontype() == 2) {// completed orders
				if (testcode == -1 && objuser.getLstprojectforfilter() == null) {

//					lstorders = LSlogilabprotocoldetailRepository
//							.findByOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenOrOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenOrOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyInOrderByProtocolordercodeDesc(
//									"R", objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, "R",
//									objuser.getLssitemaster().getSitecode(), 2, objuser.getUsercode(), fromdate, todate,
//									"R", objuser.getLssitemaster().getSitecode(), 3, fromdate, todate, userlist,
//									pageable);

					lstorders = LSlogilabprotocoldetailRepository
							.findByOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenOrOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenOrOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyInAndTeamselectedOrTeamselectedAndProtocolordercodeInAndOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyInOrderByProtocolordercodeDesc(
									"R", objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, "R",
									objuser.getLssitemaster().getSitecode(), 2, objuser.getUsercode(), fromdate, todate,
									"R", objuser.getLssitemaster().getSitecode(), 3, fromdate, todate, userlist, false,
									true, selectedteamprotcolorderList, "R", objuser.getLssitemaster().getSitecode(), 3,
									fromdate, todate, userlist, pageable);

					lstorders.addAll(LSlogilabprotocoldetailRepository
							.findByOrderflagAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndRejectedIsNullOrderByProtocolordercodeDesc(
									"R", objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject,
									pageable));

//					count = LSlogilabprotocoldetailRepository
//							.countByOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenOrOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenOrOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyIn(
//									"R", objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, "R",
//									objuser.getLssitemaster().getSitecode(), 2, objuser.getUsercode(), fromdate, todate,
//									"R", objuser.getLssitemaster().getSitecode(), 3, fromdate, todate, userlist);
//
//					count = count + LSlogilabprotocoldetailRepository
//							.countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndRejectedIsNull(
//									"R", objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject);

				} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {

					lstorders = LSlogilabprotocoldetailRepository
							.findByOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndTestcodeOrderByProtocolordercodeDesc(
									"R", objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, testcode, "R",
									objuser.getLssitemaster().getSitecode(), 2, objuser.getUsercode(), fromdate, todate,
									testcode, pageable);

					lstorders.addAll(LSlogilabprotocoldetailRepository
							.findByOrderflagAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndRejectedIsNullAndTestcodeOrderByProtocolordercodeDesc(
									"R", objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject,
									testcode, pageable));

					lstorders.addAll(LSlogilabprotocoldetailRepository
							.findByOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterInAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrderByProtocolordercodeDesc(
									"R", objuser.getLssitemaster().getSitecode(), lstproject, 3, fromdate, todate,
									testcode, pageable));

//					count = LSlogilabprotocoldetailRepository
//							.countByOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndTestcode(
//									"R", objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, testcode, "R",
//									objuser.getLssitemaster().getSitecode(), 2, objuser.getUsercode(), fromdate, todate,
//									testcode);
//
//					count = count + LSlogilabprotocoldetailRepository
//							.countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndRejectedIsNullAndTestcode(
//									"R", objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject,
//									testcode);
//
//					count = count + LSlogilabprotocoldetailRepository
//							.countByOrderflagAndSitecodeAndRejectedIsNullAndLsprojectmasterInAndViewoptionAndCreatedtimestampBetweenAndTestcode(
//									"R", objuser.getLssitemaster().getSitecode(), lstproject, 3, fromdate, todate,
//									testcode);

				} else if (testcode == -1 && objuser.getLstprojectforfilter() != null) {
					lstorders = LSlogilabprotocoldetailRepository
							.findByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterAndRejectedIsNullOrderByProtocolordercodeDesc(
									"R", objuser.getLssitemaster().getSitecode(), fromdate, todate,
									objuser.getLstprojectforfilter(), pageable);

//					count = LSlogilabprotocoldetailRepository
//							.countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterAndRejectedIsNull(
//									"R", objuser.getLssitemaster().getSitecode(), fromdate, todate,
//									objuser.getLstprojectforfilter());

				} else {
					lstorders = LSlogilabprotocoldetailRepository
							.findByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterAndRejectedIsNullAndTestcodeOrderByProtocolordercodeDesc(
									"R", objuser.getLssitemaster().getSitecode(), fromdate, todate,
									objuser.getLstprojectforfilter(), testcode, pageable);

//					count = LSlogilabprotocoldetailRepository
//							.countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterAndRejectedIsNullAndTestcode(
//									"R", objuser.getLssitemaster().getSitecode(), fromdate, todate,
//									objuser.getLstprojectforfilter(), testcode);
				}

			} else if (objuser.getObjuser().getOrderselectiontype() == 3) {// pending order

				if (testcode == -1 && objuser.getLstprojectforfilter() == null) {

					if (env.getProperty("app.datasource.eln.url").toLowerCase().contains("postgres")) {
//						lstorders = LSlogilabprotocoldetailRepository
//								.findByOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndRejectedIsNullOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndRejectedIsNullOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndRejectedIsNullAndCreatebyInOrOrderflagAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndOrdercancellIsNullAndRejectedIsNullOrderByProtocolordercodeDesc(
//										"N", objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, "N",
//										objuser.getLssitemaster().getSitecode(), 2, objuser.getUsercode(), fromdate,
//										todate, "N", objuser.getLssitemaster().getSitecode(), 3, fromdate, todate,
//										userlist, "N", objuser.getLssitemaster().getSitecode(), fromdate, todate,
//										lstproject, pageable);

						lstorders = LSlogilabprotocoldetailRepository
								.findByOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndRejectedIsNullOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndRejectedIsNullOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndRejectedIsNullAndCreatebyInAndTeamselectedOrTeamselectedAndProtocolordercodeInAndOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndRejectedIsNullAndCreatebyInOrOrderflagAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndOrdercancellIsNullAndRejectedIsNullOrderByProtocolordercodeDesc(
										"N", objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, "N",
										objuser.getLssitemaster().getSitecode(), 2, objuser.getUsercode(), fromdate,
										todate, "N", objuser.getLssitemaster().getSitecode(), 3, fromdate, todate,
										userlist, false, true, selectedteamprotcolorderList, "N",
										objuser.getLssitemaster().getSitecode(), 3, fromdate, todate, userlist, "N",
										objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject,
										pageable);
					} else {
//						lstorders = LSlogilabprotocoldetailRepository
//								.findByOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndRejectedIsNullOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndRejectedIsNullOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndRejectedIsNullAndCreatebyInOrderByProtocolordercodeDesc(
//										"N", objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, "N",
//										objuser.getLssitemaster().getSitecode(), 2, objuser.getUsercode(), fromdate,
//										todate, "N", objuser.getLssitemaster().getSitecode(), 3, fromdate, todate,
//										userlist, pageable);

						lstorders = LSlogilabprotocoldetailRepository
								.findByOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndRejectedIsNullOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndRejectedIsNullOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndRejectedIsNullAndCreatebyInAndTeamselectedOrTeamselectedAndProtocolordercodeInAndOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndRejectedIsNullAndCreatebyInOrderByProtocolordercodeDesc(
										"N", objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, "N",
										objuser.getLssitemaster().getSitecode(), 2, objuser.getUsercode(), fromdate,
										todate, "N", objuser.getLssitemaster().getSitecode(), 3, fromdate, todate,
										userlist, false, true, selectedteamprotcolorderList, "N",
										objuser.getLssitemaster().getSitecode(), 3, fromdate, todate, userlist,
										pageable);

						lstorders.addAll(LSlogilabprotocoldetailRepository
								.findByOrderflagAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndOrdercancellIsNullAndRejectedIsNullOrderByProtocolordercodeDesc(
										"N", objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject,
										pageable));
					}

//					count = LSlogilabprotocoldetailRepository
//							.countByOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndRejectedIsNullOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndRejectedIsNullOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndRejectedIsNullAndCreatebyIn(
//									"N", objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, "N",
//									objuser.getLssitemaster().getSitecode(), 2, objuser.getUsercode(), fromdate, todate,
//									"N", objuser.getLssitemaster().getSitecode(), 3, fromdate, todate, userlist);
//
//					count = count + LSlogilabprotocoldetailRepository
//							.countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndOrdercancellIsNullAndRejectedIsNullOrderByProtocolordercodeDesc(
//									"N", objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject);

				} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {

					lstorders = LSlogilabprotocoldetailRepository
							.findByOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndTestcodeAndRejectedIsNullOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndTestcodeAndRejectedIsNullOrderByProtocolordercodeDesc(
									"N", objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, testcode, "N",
									objuser.getLssitemaster().getSitecode(), 2, fromdate, objuser.getUsercode(), todate,
									testcode, pageable);

					lstorders.addAll(LSlogilabprotocoldetailRepository
							.findByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterInAndOrdercancellIsNullAndTestcodeAndRejectedIsNullOrderByProtocolordercodeDesc(
									"N", objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject,
									testcode, pageable));

					lstorders.addAll(LSlogilabprotocoldetailRepository
							.findByOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterInAndViewoptionAndCreatedtimestampBetweenAndTestcodeAndRejectedIsNullOrderByProtocolordercodeDesc(
									"N", objuser.getLssitemaster().getSitecode(), lstproject, 3, fromdate, todate,
									testcode, pageable));

//					count = LSlogilabprotocoldetailRepository
//							.countByOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndTestcodeAndRejectedIsNullOrOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndTestcodeAndRejectedIsNull(
//									"N", objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, testcode, "N",
//									objuser.getLssitemaster().getSitecode(), 2, fromdate, objuser.getUsercode(), todate,
//									testcode);
//
//					count = count + LSlogilabprotocoldetailRepository
//							.countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterInAndOrdercancellIsNullAndTestcodeAndRejectedIsNull(
//									"N", objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject,
//									testcode);
//
//					count = count + LSlogilabprotocoldetailRepository
//							.countByOrderflagAndSitecodeAndOrdercancellIsNullAndLsprojectmasterInAndViewoptionAndCreatedtimestampBetweenAndTestcodeAndRejectedIsNull(
//									"N", objuser.getLssitemaster().getSitecode(), lstproject, 3, fromdate, todate,
//									testcode);

				} else if (testcode == -1 && objuser.getLstprojectforfilter() != null) {
					lstorders = LSlogilabprotocoldetailRepository
							.findByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterAndOrdercancellIsNullAndRejectedIsNullOrderByProtocolordercodeDesc(
									"N", objuser.getLssitemaster().getSitecode(), fromdate, todate,
									objuser.getLstprojectforfilter(), pageable);

//					count = LSlogilabprotocoldetailRepository
//							.countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterAndOrdercancellIsNullAndRejectedIsNull(
//									"N", objuser.getLssitemaster().getSitecode(), fromdate, todate,
//									objuser.getLstprojectforfilter());

				} else {
					lstorders = LSlogilabprotocoldetailRepository
							.findByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterAndOrdercancellIsNullAndTestcodeAndRejectedIsNullOrderByProtocolordercodeDesc(
									"N", objuser.getLssitemaster().getSitecode(), fromdate, todate,
									objuser.getLstprojectforfilter(), testcode, pageable);

//					count = LSlogilabprotocoldetailRepository
//							.countByOrderflagAndSitecodeAndCreatedtimestampBetweenAndAssignedtoIsNullAndLsprojectmasterAndOrdercancellIsNullAndTestcodeAndRejectedIsNull(
//									"N", objuser.getLssitemaster().getSitecode(), fromdate, todate,
//									objuser.getLstprojectforfilter(), testcode);

				}
			} else if (objuser.getObjuser().getOrderselectiontype() == 5) {// rejected orders
				if (testcode == -1 && objuser.getLstprojectforfilter() == null) {

//					lstorders = LSlogilabprotocoldetailRepository
//							.findByRejectedAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenOrRejectedAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenOrRejectedAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyInOrderByProtocolordercodeDesc(
//									1, objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, 1,
//									objuser.getLssitemaster().getSitecode(), 2, objuser.getUsercode(), fromdate, todate,
//									1, objuser.getLssitemaster().getSitecode(), 3, fromdate, todate, userlist,
//									pageable);

					lstorders = LSlogilabprotocoldetailRepository
							.findByRejectedAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenOrRejectedAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenOrRejectedAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyInAndTeamselectedOrTeamselectedAndProtocolordercodeInAndRejectedAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyInOrderByProtocolordercodeDesc(
									1, objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, 1,
									objuser.getLssitemaster().getSitecode(), 2, objuser.getUsercode(), fromdate, todate,
									1, objuser.getLssitemaster().getSitecode(), 3, fromdate, todate, userlist, false,
									true, selectedteamprotcolorderList, 1, objuser.getLssitemaster().getSitecode(), 3,
									fromdate, todate, userlist, pageable);

					lstorders.addAll(LSlogilabprotocoldetailRepository
							.findByRejectedAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInOrderByProtocolordercodeDesc(
									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject,
									pageable));

//					count = LSlogilabprotocoldetailRepository
//							.countByRejectedAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenOrRejectedAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenOrRejectedAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyIn(
//									1, objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, 1,
//									objuser.getLssitemaster().getSitecode(), 2, objuser.getUsercode(), fromdate, todate,
//									1, objuser.getLssitemaster().getSitecode(), 3, fromdate, todate, userlist);
//
//					count = count + LSlogilabprotocoldetailRepository
//							.countByRejectedAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterIn(1,
//									objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject);

				} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {

					lstorders = LSlogilabprotocoldetailRepository
							.findByRejectedAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrRejectedAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndTestcodeOrderByProtocolordercodeDesc(
									1, objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, testcode, 1,
									objuser.getLssitemaster().getSitecode(), 2, objuser.getUsercode(), fromdate, todate,
									testcode, pageable);

					lstorders.addAll(LSlogilabprotocoldetailRepository
							.findByRejectedAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndTestcodeOrderByProtocolordercodeDesc(
									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject, testcode,
									pageable));

					lstorders.addAll(LSlogilabprotocoldetailRepository
							.findByRejectedAndSitecodeAndLsprojectmasterInAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrderByProtocolordercodeDesc(
									1, objuser.getLssitemaster().getSitecode(), lstproject, 3, fromdate, todate,
									testcode, pageable));

//					count = LSlogilabprotocoldetailRepository
//							.countByRejectedAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrRejectedAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndTestcode(
//									1, objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, testcode, 1,
//									objuser.getLssitemaster().getSitecode(), 2, objuser.getUsercode(), fromdate, todate,
//									testcode);
//
//					count = count + LSlogilabprotocoldetailRepository
//							.countByRejectedAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndTestcode(1,
//									objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject, testcode);
//
//					count = count + LSlogilabprotocoldetailRepository
//							.countByRejectedAndSitecodeAndLsprojectmasterInAndViewoptionAndCreatedtimestampBetweenAndTestcode(
//									1, objuser.getLssitemaster().getSitecode(), lstproject, 3, fromdate, todate,
//									testcode);

				} else if (testcode == -1 && objuser.getLstprojectforfilter() != null) {
					lstorders = LSlogilabprotocoldetailRepository
							.findByRejectedAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterOrderByProtocolordercodeDesc(
									1, objuser.getLssitemaster().getSitecode(), fromdate, todate,
									objuser.getLstprojectforfilter(), pageable);

//					count = LSlogilabprotocoldetailRepository
//							.countByRejectedAndSitecodeAndCreatedtimestampBetweenAndLsprojectmaster(1,
//									objuser.getLssitemaster().getSitecode(), fromdate, todate,
//									objuser.getLstprojectforfilter());

				} else {
					lstorders = LSlogilabprotocoldetailRepository
							.findByRejectedAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterAndTestcodeOrderByProtocolordercodeDesc(
									1, objuser.getLssitemaster().getSitecode(), fromdate, todate,
									objuser.getLstprojectforfilter(), testcode, pageable);

//					count = LSlogilabprotocoldetailRepository
//							.countByRejectedAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterAndTestcode(1,
//									objuser.getLssitemaster().getSitecode(), fromdate, todate,
//									objuser.getLstprojectforfilter(), testcode);
				}

			} else if (objuser.getObjuser().getOrderselectiontype() == 6) {// cancelled order
				if (testcode == -1 && objuser.getLstprojectforfilter() == null) {

//					lstorders = LSlogilabprotocoldetailRepository
//							.findByOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyInOrderByProtocolordercodeDesc(
//									1, objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, 1,
//									objuser.getLssitemaster().getSitecode(), 2, objuser.getUsercode(), fromdate, todate,
//									1, objuser.getLssitemaster().getSitecode(), 3, fromdate, todate, userlist,
//									pageable);

					lstorders = LSlogilabprotocoldetailRepository
							.findByOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyInAndTeamselectedOrTeamselectedAndProtocolordercodeInAndOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyInOrderByProtocolordercodeDesc(
									1, objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, 1,
									objuser.getLssitemaster().getSitecode(), 2, objuser.getUsercode(), fromdate, todate,
									1, objuser.getLssitemaster().getSitecode(), 3, fromdate, todate, userlist, false,
									true, selectedteamprotcolorderList, 1, objuser.getLssitemaster().getSitecode(), 3,
									fromdate, todate, userlist, pageable);

					lstorders.addAll(LSlogilabprotocoldetailRepository
							.findByOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInOrderByProtocolordercodeDesc(
									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject,
									pageable));

//					count = LSlogilabprotocoldetailRepository
//							.countByOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndCreatebyIn(
//									1, objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, 1,
//									objuser.getLssitemaster().getSitecode(), 2, objuser.getUsercode(), fromdate, todate,
//									1, objuser.getLssitemaster().getSitecode(), 3, fromdate, todate, userlist);
//
//					count = count + LSlogilabprotocoldetailRepository
//							.countByOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterIn(1,
//									objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject);

				} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {

					lstorders = LSlogilabprotocoldetailRepository
							.findByOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndTestcodeOrderByProtocolordercodeDesc(
									1, objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, testcode, 1,
									objuser.getLssitemaster().getSitecode(), 2, objuser.getUsercode(), fromdate, todate,
									testcode, pageable);

					lstorders.addAll(LSlogilabprotocoldetailRepository
							.findByOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndTestcodeOrderByProtocolordercodeDesc(
									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject, testcode,
									pageable));

					lstorders.addAll(LSlogilabprotocoldetailRepository
							.findByOrdercancellAndSitecodeAndLsprojectmasterInAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrderByProtocolordercodeDesc(
									1, objuser.getLssitemaster().getSitecode(), lstproject, 3, fromdate, todate,
									testcode, pageable));

//					count = LSlogilabprotocoldetailRepository
//							.countByOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatedtimestampBetweenAndTestcodeOrOrdercancellAndSitecodeAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndTestcode(
//									1, objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, testcode, 1,
//									objuser.getLssitemaster().getSitecode(), 2, objuser.getUsercode(), fromdate, todate,
//									testcode);
//
//					count = count + LSlogilabprotocoldetailRepository
//							.countByOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterInAndTestcode(1,
//									objuser.getLssitemaster().getSitecode(), fromdate, todate, lstproject, testcode);
//
//					count = count + LSlogilabprotocoldetailRepository
//							.countByOrdercancellAndSitecodeAndLsprojectmasterInAndViewoptionAndCreatedtimestampBetweenAndTestcode(
//									1, objuser.getLssitemaster().getSitecode(), lstproject, 3, fromdate, todate,
//									testcode);

				} else if (testcode == -1 && objuser.getLstprojectforfilter() != null) {
					lstorders = LSlogilabprotocoldetailRepository
							.findByOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterOrderByProtocolordercodeDesc(
									1, objuser.getLssitemaster().getSitecode(), fromdate, todate,
									objuser.getLstprojectforfilter(), pageable);

//					count = LSlogilabprotocoldetailRepository
//							.countByOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmaster(1,
//									objuser.getLssitemaster().getSitecode(), fromdate, todate,
//									objuser.getLstprojectforfilter());
				} else {
					lstorders = LSlogilabprotocoldetailRepository
							.findByOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterAndTestcodeOrderByProtocolordercodeDesc(
									1, objuser.getLssitemaster().getSitecode(), fromdate, todate,
									objuser.getLstprojectforfilter(), testcode, pageable);

//					count = LSlogilabprotocoldetailRepository
//							.countByOrdercancellAndSitecodeAndCreatedtimestampBetweenAndLsprojectmasterAndTestcode(1,
//									objuser.getLssitemaster().getSitecode(), fromdate, todate,
//									objuser.getLstprojectforfilter(), testcode);
				}

			}
		} else {
			if (objuser.getObjuser().getOrderselectiontype() == 1) {
				if (testcode == -1 && objuser.getLstprojectforfilter() == null) {
//					lstorders = LSlogilabprotocoldetailRepository
//							.findByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
//									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, 2,
//									objuser.getUsercode(), fromdate, todate, 3, objuser.getUsercode(), fromdate, todate,
//									pageable);

					lstorders = LSlogilabprotocoldetailRepository
							.findByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndTeamselectedOrTeamselectedAndProtocolordercodeInAndLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, 2,
									objuser.getUsercode(), fromdate, todate, 3, objuser.getUsercode(), fromdate, todate,
									false, true, selectedteamprotcolorderList, 3, objuser.getUsercode(), fromdate,
									todate, pageable);

//					count = LSlogilabprotocoldetailRepository
//							.countByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenOrderByProtocolordercodeDesc(
//									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, 2,
//									objuser.getUsercode(), fromdate, todate, 3, objuser.getUsercode(), fromdate,
//									todate);
				} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {
					lstorders = LSlogilabprotocoldetailRepository
							.findByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndTestcodeOrderByProtocolordercodeDesc(
									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, testcode, 2,
									objuser.getUsercode(), fromdate, todate, testcode, 3, objuser.getUsercode(),
									fromdate, todate, testcode, pageable);
//					count = LSlogilabprotocoldetailRepository
//							.countByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndTestcodeOrderByProtocolordercodeDesc(
//									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, testcode, 2,
//									objuser.getUsercode(), fromdate, todate, testcode, 3, objuser.getUsercode(),
//									fromdate, todate, testcode);
				}
			} else if (objuser.getObjuser().getOrderselectiontype() == 2) {
				if (testcode == -1 && objuser.getLstprojectforfilter() == null) {
					lstorders = LSlogilabprotocoldetailRepository
							.findByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndOrderflagAndRejectedIsNullOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrderflagAndRejectedIsNullOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrderflagAndRejectedIsNullOrderByProtocolordercodeDesc(
									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, "R", 2,
									objuser.getUsercode(), fromdate, todate, "R", 3, objuser.getUsercode(), fromdate,
									todate, "R", pageable);

//					count = LSlogilabprotocoldetailRepository
//							.countByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndOrderflagAndRejectedIsNullOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrderflagAndRejectedIsNullOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrderflagAndRejectedIsNullOrderByProtocolordercodeDesc(
//									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, "R", 2,
//									objuser.getUsercode(), fromdate, todate, "R", 3, objuser.getUsercode(), fromdate,
//									todate, "R");
				} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {
					lstorders = LSlogilabprotocoldetailRepository
							.findByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndOrderflagAndRejectedIsNullAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrderflagAndRejectedIsNullAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrderflagAndRejectedIsNullAndTestcodeOrderByProtocolordercodeDesc(
									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, "R", testcode, 2,
									objuser.getUsercode(), fromdate, todate, "R", testcode, 3, objuser.getUsercode(),
									fromdate, todate, "R", testcode, pageable);
//					count = LSlogilabprotocoldetailRepository
//							.countByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndOrderflagAndRejectedIsNullAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrderflagAndRejectedIsNullAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrderflagAndRejectedIsNullAndTestcodeOrderByProtocolordercodeDesc(
//									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, "R", testcode, 2,
//									objuser.getUsercode(), fromdate, todate, "R", testcode, 3, objuser.getUsercode(),
//									fromdate, todate, "R", testcode);
				}

			} else if (objuser.getObjuser().getOrderselectiontype() == 3) {

				if (testcode == -1 && objuser.getLstprojectforfilter() == null) {
					lstorders = LSlogilabprotocoldetailRepository
							.findByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullOrderByProtocolordercodeDesc(
									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, "N", 2,
									objuser.getUsercode(), fromdate, todate, "N", 3, objuser.getUsercode(), fromdate,
									todate, "N", pageable);
//					count = LSlogilabprotocoldetailRepository
//							.countByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullOrderByProtocolordercodeDesc(
//									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, "N", 2,
//									objuser.getUsercode(), fromdate, todate, "N", 3, objuser.getUsercode(), fromdate,
//									todate, "N");
				} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {
					lstorders = LSlogilabprotocoldetailRepository
							.findByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullAndTestcodeOrderByProtocolordercodeDesc(
									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, "N", testcode, 2,
									objuser.getUsercode(), fromdate, todate, "N", testcode, 3, objuser.getUsercode(),
									fromdate, todate, "N", testcode, pageable);
//					count = LSlogilabprotocoldetailRepository
//							.countByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrderflagAndOrdercancellIsNullAndTestcodeOrderByProtocolordercodeDesc(
//									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, "N", testcode, 2,
//									objuser.getUsercode(), fromdate, todate, "N", testcode, 3, objuser.getUsercode(),
//									fromdate, todate, "N", testcode);
				}
			} else if (objuser.getObjuser().getOrderselectiontype() == 5) {
				if (testcode == -1 && objuser.getLstprojectforfilter() == null) {
					lstorders = LSlogilabprotocoldetailRepository
							.findByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndRejectedOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndRejectedOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndRejectedOrderByProtocolordercodeDesc(
									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, 1, 2,
									objuser.getUsercode(), fromdate, todate, 1, 3, objuser.getUsercode(), fromdate,
									todate, 1, pageable);
//					count = LSlogilabprotocoldetailRepository
//							.countByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndRejectedOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndRejectedOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndRejectedOrderByProtocolordercodeDesc(
//									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, 1, 2,
//									objuser.getUsercode(), fromdate, todate, 1, 3, objuser.getUsercode(), fromdate,
//									todate, 1);
				} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {
					lstorders = LSlogilabprotocoldetailRepository
							.findByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndRejectedAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndRejectedAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndRejectedAndTestcodeOrderByProtocolordercodeDesc(
									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, 1, testcode, 2,
									objuser.getUsercode(), fromdate, todate, 1, testcode, 3, objuser.getUsercode(),
									fromdate, todate, 1, testcode, pageable);
//					count = LSlogilabprotocoldetailRepository
//							.countByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndRejectedAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndRejectedAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndRejectedAndTestcodeOrderByProtocolordercodeDesc(
//									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, 1, testcode, 2,
//									objuser.getUsercode(), fromdate, todate, 1, testcode, 3, objuser.getUsercode(),
//									fromdate, todate, 1, testcode);

				}
			} else if (objuser.getObjuser().getOrderselectiontype() == 6) {
				if (testcode == -1 && objuser.getLstprojectforfilter() == null) {
					lstorders = LSlogilabprotocoldetailRepository
							.findByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndOrdercancellOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrdercancellOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrdercancellOrderByProtocolordercodeDesc(
									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, 1, 2,
									objuser.getUsercode(), fromdate, todate, 1, 3, objuser.getUsercode(), fromdate,
									todate, 1, pageable);
//					count = LSlogilabprotocoldetailRepository
//							.countByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndOrdercancellOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrdercancellOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrdercancellOrderByProtocolordercodeDesc(
//									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, 1, 2,
//									objuser.getUsercode(), fromdate, todate, 1, 3, objuser.getUsercode(), fromdate,
//									todate, 1);
				} else if (testcode != -1 && objuser.getLstprojectforfilter() == null) {
					lstorders = LSlogilabprotocoldetailRepository
							.findByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndOrdercancellAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrdercancellAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrdercancellAndTestcodeOrderByProtocolordercodeDesc(
									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, 1, testcode, 2,
									objuser.getUsercode(), fromdate, todate, 1, testcode, 3, objuser.getUsercode(),
									fromdate, todate, 1, testcode, pageable);
//					count = LSlogilabprotocoldetailRepository
//							.countByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndOrdercancellAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrdercancellAndTestcodeOrLsprojectmasterIsNullAndViewoptionAndCreatebyAndCreatedtimestampBetweenAndOrdercancellAndTestcodeOrderByProtocolordercodeDesc(
//									1, objuser.getLssitemaster().getSitecode(), fromdate, todate, 1, testcode, 2,
//									objuser.getUsercode(), fromdate, todate, 1, testcode, 3, objuser.getUsercode(),
//									fromdate, todate, 1, testcode);
				}

			}
		}

//		lstorders.forEach(objorderDetail -> objorderDetail.setLstworkflow(objuser.getLstworkflow()));
		lstorders.forEach(
				objorderDetail -> objorderDetail.setLstelnprotocolworkflow(objuser.getLstelnprotocolworkflow()));
		Collections.sort(lstorders, Collections.reverseOrder());
		mapOrders.put("orderlst", lstorders);
//		mapOrders.put("count", count);
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
			lstfile = lsfileRepository
					.findByCreatedateBetweenAndFilecodeGreaterThanAndLssitemasterAndViewoptionOrCreatedateBetweenAndFilecodeGreaterThanAndCreatebyInAndViewoptionOrderByFilecodeDesc(
							fromdate, todate, 1, objuser.getLssitemaster(), 1, fromdate, todate, 1, objuser, 2,
							pageable);

			count = lsfileRepository
					.countByCreatedateBetweenAndFilecodeGreaterThanAndLssitemasterAndViewoptionOrCreatedateBetweenAndFilecodeGreaterThanAndCreatebyInAndViewoptionOrderByFilecodeDesc(
							fromdate, todate, 1, objuser.getLssitemaster(), 1, fromdate, todate, 1, objuser, 2);
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
			mapSheets.put("rejectedTemplate", lsfileRepository
					.countByCreatedateBetweenAndRetirestatusAndFilecodeGreaterThanAndRejectedOrderByFilecodeDesc(
							fromdate, todate, 0, 1, 1));
			mapSheets.put("approvedTemplate", lsfileRepository
					.countByCreatedateBetweenAndRetirestatusAndFilecodeGreaterThanAndApprovedAndRejectedNotOrderByFilecodeDesc(
							fromdate, todate, 0, 1, 1, 1));
			mapSheets.put("createdTemplate", lsfileRepository
					.countByCreatedateBetweenAndRetirestatusAndFilecodeGreaterThanAndApprovedIsNullAndRejectedNotOrderByFilecodeDesc(
							fromdate, todate, 0, 1, 1));
			mapSheets.put("inprogressTemplate", lsfileRepository
					.countByCreatedateBetweenAndRetirestatusAndFilecodeGreaterThanAndApprovedAndRejectedOrderByFilecodeDesc(
							fromdate, todate, 0, 1, 0, 0));
			mapSheets.put("retiredTemplate",
					lsfileRepository.countByCreatedateBetweenAndFilecodeGreaterThanAndRetirestatusOrderByFilecodeDesc(
							fromdate, todate, 1, 1));
		} else {
			if (lstteamuser != null && lstteamuser.size() > 0) {
				lstteamuser.add(objuser);
				mapSheets.put("rejectedTemplate", lsfileRepository
						.countByFilecodeGreaterThanAndLssitemasterAndCreatedateBetweenAndRetirestatusAndViewoptionAndRejectedOrFilecodeGreaterThanAndCreatebyAndCreatedateBetweenAndRetirestatusAndViewoptionAndRejectedOrFilecodeGreaterThanAndCreatebyInAndCreatedateBetweenAndRetirestatusAndViewoptionAndRejectedOrderByFilecodeDesc(
								1, objuser.getLssitemaster(), fromdate, todate, 0, 1, 1, 1, objuser, fromdate, todate,
								0, 2, 1, 1, lstteamuser, fromdate, todate, 0, 3, 1));
				mapSheets.put("approvedTemplate", lsfileRepository
						.countByFilecodeGreaterThanAndLssitemasterAndCreatedateBetweenAndRetirestatusAndViewoptionAndApprovedAndRejectedNotOrFilecodeGreaterThanAndCreatebyAndCreatedateBetweenAndRetirestatusAndViewoptionAndApprovedAndRejectedNotOrFilecodeGreaterThanAndCreatebyInAndCreatedateBetweenAndRetirestatusAndViewoptionAndApprovedAndRejectedNotOrderByFilecodeDesc(
								1, objuser.getLssitemaster(), fromdate, todate, 0, 1, 1, 1, 1, objuser, fromdate,
								todate, 0, 2, 1, 1, 1, lstteamuser, fromdate, todate, 0, 3, 1, 1));
				mapSheets.put("createdTemplate", lsfileRepository
						.countByFilecodeGreaterThanAndLssitemasterAndCreatedateBetweenAndRetirestatusAndViewoptionAndApprovedIsNullAndRejectedNotOrFilecodeGreaterThanAndCreatebyAndCreatedateBetweenAndRetirestatusAndViewoptionAndApprovedIsNullAndRejectedNotOrFilecodeGreaterThanAndCreatebyInAndCreatedateBetweenAndRetirestatusAndViewoptionAndApprovedIsNullAndRejectedNotOrderByFilecodeDesc(
								1, objuser.getLssitemaster(), fromdate, todate, 0, 1, 1, 1, objuser, fromdate, todate,
								0, 2, 1, 1, lstteamuser, fromdate, todate, 0, 3, 1));
				mapSheets.put("inprogressTemplate", lsfileRepository
						.countByFilecodeGreaterThanAndLssitemasterAndCreatedateBetweenAndRetirestatusAndViewoptionAndApprovedAndRejectedNotOrFilecodeGreaterThanAndCreatebyAndCreatedateBetweenAndRetirestatusAndViewoptionAndApprovedAndRejectedNotOrFilecodeGreaterThanAndCreatebyInAndCreatedateBetweenAndRetirestatusAndViewoptionAndApprovedAndRejectedNotOrderByFilecodeDesc(
								1, objuser.getLssitemaster(), fromdate, todate, 0, 1, 0, 1, 1, objuser, fromdate,
								todate, 0, 2, 0, 1, 1, lstteamuser, fromdate, todate, 0, 3, 0, 1));
				mapSheets.put("retiredTemplate", lsfileRepository
						.countByFilecodeGreaterThanAndLssitemasterAndCreatedateBetweenAndViewoptionAndRetirestatusOrFilecodeGreaterThanAndCreatebyAndCreatedateBetweenAndViewoptionAndRetirestatusOrFilecodeGreaterThanAndCreatebyInAndCreatedateBetweenAndRetirestatusAndViewoptionOrderByFilecodeDesc(
								1, objuser.getLssitemaster(), fromdate, todate, 1, 1, 1, objuser, fromdate, todate, 2,
								1, 1, lstteamuser, fromdate, todate, 1, 3));

			} else {
				mapSheets.put("rejectedTemplate", lsfileRepository
						.countByFilecodeGreaterThanAndLssitemasterAndCreatedateBetweenAndRetirestatusAndViewoptionAndRejectedOrFilecodeGreaterThanAndCreatebyAndCreatedateBetweenAndRetirestatusAndViewoptionAndRejectedOrderByFilecodeDesc(
								1, objuser.getLssitemaster(), fromdate, todate, 0, 1, 1, 1, objuser, fromdate, todate,
								0, 2, 1));
				mapSheets.put("approvedTemplate", lsfileRepository
						.countByFilecodeGreaterThanAndLssitemasterAndCreatedateBetweenAndRetirestatusAndViewoptionAndApprovedAndRejectedNotOrFilecodeGreaterThanAndCreatebyAndCreatedateBetweenAndRetirestatusAndViewoptionAndApprovedAndRejectedNotOrderByFilecodeDesc(
								1, objuser.getLssitemaster(), fromdate, todate, 0, 1, 1, 1, 1, objuser, fromdate,
								todate, 0, 2, 1, 1));
				mapSheets.put("inprogressTemplate", lsfileRepository
						.countByFilecodeGreaterThanAndLssitemasterAndCreatedateBetweenAndRetirestatusAndViewoptionAndApprovedAndRejectedNotOrFilecodeGreaterThanAndCreatebyAndCreatedateBetweenAndRetirestatusAndViewoptionAndApprovedAndRejectedNotOrderByFilecodeDesc(
								1, objuser.getLssitemaster(), fromdate, todate, 0, 1, 0, 1, 1, objuser, fromdate,
								todate, 0, 2, 0, 1));
				mapSheets.put("createdTemplate", lsfileRepository
						.countByFilecodeGreaterThanAndLssitemasterAndCreatedateBetweenAndRetirestatusAndViewoptionAndApprovedIsNullAndRejectedNotOrFilecodeGreaterThanAndCreatebyAndCreatedateBetweenAndRetirestatusAndViewoptionAndApprovedIsNullAndRejectedNotOrderByFilecodeDesc(
								1, objuser.getLssitemaster(), fromdate, todate, 0, 1, 1, 1, objuser, fromdate, todate,
								0, 2, 1));
				mapSheets.put("retiredTemplate", lsfileRepository
						.countByFilecodeGreaterThanAndLssitemasterAndCreatedateBetweenAndRetirestatusAndViewoptionOrFilecodeGreaterThanAndCreatebyAndCreatedateBetweenAndRetirestatusAndViewoptionOrderByFilecodeDesc(
								1, objuser.getLssitemaster(), fromdate, todate, 1, 1, 1, objuser, fromdate, todate, 1,
								2));

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
						.findByLssitemasterAndStatusAndCreatedateBetweenAndViewoptionOrCreatedbyAndStatusAndCreatedateBetweenAndViewoptionAndLssitemasterOrCreatedbyInAndStatusAndCreatedateBetweenAndViewoptionAndLssitemasterOrderByProtocolmastercodeDesc(
								objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, 1, objuser.getUsercode(),
								1, fromdate, todate, 2, objuser.getLssitemaster().getSitecode(), usercodelist, 1,
								fromdate, todate, 3, objuser.getLssitemaster().getSitecode(), pageable);
				count = LSProtocolMasterRepository
						.countByLssitemasterAndStatusAndCreatedateBetweenAndViewoptionOrCreatedbyAndStatusAndCreatedateBetweenAndViewoptionAndLssitemasterOrCreatedbyInAndStatusAndCreatedateBetweenAndViewoptionAndLssitemasterOrderByProtocolmastercodeDesc(
								objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, 1, objuser.getUsercode(),
								1, fromdate, todate, 2, objuser.getLssitemaster().getSitecode(), usercodelist, 1,
								fromdate, todate, 3, objuser.getLssitemaster().getSitecode());

			} else {
				lstprotocolmaster = LSProtocolMasterRepository
						.findByLssitemasterAndStatusAndCreatedateBetweenAndViewoptionOrCreatedbyAndStatusAndCreatedateBetweenAndViewoptionAndLssitemasterOrderByProtocolmastercodeDesc(
								objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, 1, objuser.getUsercode(),
								1, fromdate, todate, 2, objuser.getLssitemaster().getSitecode(), pageable);

				count = LSProtocolMasterRepository
						.countByLssitemasterAndStatusAndCreatedateBetweenAndViewoptionOrCreatedbyAndStatusAndCreatedateBetweenAndViewoptionAndLssitemasterOrderByProtocolmastercodeDesc(
								objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, 1, objuser.getUsercode(),
								1, fromdate, todate, 2, objuser.getLssitemaster().getSitecode());
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
					LSProtocolMasterRepository
							.countByStatusAndLssitemasterAndCreatedateBetweenAndRetirestatusAndRejected(1,
									objuser.getLssitemaster().getSitecode(), fromdate, todate, 0, 1));
			mapSheets.put("approvedTemplate",
					LSProtocolMasterRepository
							.countByStatusAndLssitemasterAndCreatedateBetweenAndRetirestatusAndRejectedNotAndApproved(1,
									objuser.getLssitemaster().getSitecode(), fromdate, todate, 0, 1, 1));
			mapSheets.put("createdTemplate", LSProtocolMasterRepository
					.countByStatusAndLssitemasterAndCreatedateBetweenAndRetirestatusAndRejectedNotAndApprovedIsNull(1,
							objuser.getLssitemaster().getSitecode(), fromdate, todate, 0, 1));
			mapSheets.put("inprogressTemplate",
					LSProtocolMasterRepository
							.countByStatusAndLssitemasterAndCreatedateBetweenAndRetirestatusAndRejectedNotAndApproved(1,
									objuser.getLssitemaster().getSitecode(), fromdate, todate, 0, 1, 0));
			mapSheets.put("retiredTemplate",
					LSProtocolMasterRepository.countByStatusAndLssitemasterAndCreatedateBetweenAndRetirestatus(1,
							objuser.getLssitemaster().getSitecode(), fromdate, todate, 1));
		} else {

			List<Protocoltemplateget> lstprotocolmaster = new ArrayList<>();
			long count = 0;
			if (lstteamuser != null && lstteamuser.size() > 0) {
				lstteamuser.add(objuser);
				List<Integer> usercodelist = lstteamuser.stream().map(LSuserMaster::getUsercode)
						.collect(Collectors.toList());

				count = LSProtocolMasterRepository
						.countByLssitemasterAndStatusAndCreatedateBetweenAndRetirestatusAndViewoptionAndRejectedOrCreatedbyAndStatusAndCreatedateBetweenAndRetirestatusAndViewoptionAndRejectedOrCreatedbyInAndStatusAndCreatedateBetweenAndRetirestatusAndViewoptionAndRejectedOrderByProtocolmastercodeDesc(
								objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, 0, 1, 1,
								objuser.getUsercode(), 1, fromdate, todate, 0, 2, 1, usercodelist, 1, fromdate, todate,
								0, 3, 1);
//				lstprotocolmaster = lstprotocolmaster.stream().distinct().collect(Collectors.toList());
				mapSheets.put("rejectedTemplate", count);

				count = LSProtocolMasterRepository
						.countByLssitemasterAndStatusAndCreatedateBetweenAndRetirestatusAndViewoptionAndApprovedAndRejectedIsNullOrCreatedbyAndStatusAndCreatedateBetweenAndRetirestatusAndViewoptionAndApprovedAndRejectedIsNullOrCreatedbyInAndStatusAndCreatedateBetweenAndRetirestatusAndViewoptionAndApprovedAndRejectedIsNullOrderByProtocolmastercodeDesc(
								objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, 0, 1, 1,
								objuser.getUsercode(), 1, fromdate, todate, 0, 2, 1, usercodelist, 1, fromdate, todate,
								0, 3, 1);
//				lstprotocolmaster = lstprotocolmaster.stream().distinct().collect(Collectors.toList());
				mapSheets.put("approvedTemplate", count);

				count = LSProtocolMasterRepository
						.countByLssitemasterAndStatusAndCreatedateBetweenAndRetirestatusAndViewoptionAndApprovedIsNullAndRejectedIsNullOrCreatedbyAndStatusAndCreatedateBetweenAndRetirestatusAndViewoptionAndApprovedIsNullAndRejectedIsNullOrCreatedbyInAndStatusAndCreatedateBetweenAndRetirestatusAndViewoptionAndApprovedIsNullAndRejectedIsNullOrderByProtocolmastercodeDesc(
								objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, 0, 1,
								objuser.getUsercode(), 1, fromdate, todate, 0, 2, usercodelist, 1, fromdate, todate, 0,
								3);
//				lstprotocolmaster = lstprotocolmaster.stream().distinct().collect(Collectors.toList());
				mapSheets.put("createdTemplate", count);

				count = LSProtocolMasterRepository
						.countByLssitemasterAndStatusAndCreatedateBetweenAndRetirestatusAndViewoptionAndApprovedAndRejectedIsNullOrCreatedbyAndStatusAndCreatedateBetweenAndRetirestatusAndViewoptionAndApprovedAndRejectedIsNullOrCreatedbyInAndStatusAndCreatedateBetweenAndRetirestatusAndViewoptionAndApprovedAndRejectedIsNullOrderByProtocolmastercodeDesc(
								objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, 0, 1, 0,
								objuser.getUsercode(), 1, fromdate, todate, 0, 2, 0, usercodelist, 1, fromdate, todate,
								0, 3, 0);
				lstprotocolmaster = lstprotocolmaster.stream().distinct().collect(Collectors.toList());
				mapSheets.put("inprogressTemplate", count);
				count = LSProtocolMasterRepository
						.countByLssitemasterAndStatusAndCreatedateBetweenAndRetirestatusAndViewoptionOrCreatedbyAndStatusAndCreatedateBetweenAndRetirestatusAndViewoptionOrCreatedbyInAndStatusAndCreatedateBetweenAndRetirestatusAndViewoptionOrderByProtocolmastercodeDesc(
								objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, 1, 1,
								objuser.getUsercode(), 1, fromdate, todate, 1, 2, usercodelist, 1, fromdate, todate, 1,
								3);
				mapSheets.put("retiredTemplate", count);

			} else {
				count = LSProtocolMasterRepository
						.countByLssitemasterAndStatusAndCreatedateBetweenAndRetirestatusAndViewoptionAndRejectedOrCreatedbyAndStatusAndCreatedateBetweenAndRetirestatusAndViewoptionAndRejectedOrderByProtocolmastercodeDesc(
								objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, 0, 1, 1,
								objuser.getUsercode(), 1, fromdate, todate, 0, 2, 1);
//				lstprotocolmaster = lstprotocolmaster.stream().distinct().collect(Collectors.toList());
				mapSheets.put("rejectedTemplate", count);

				count = LSProtocolMasterRepository
						.countByLssitemasterAndStatusAndCreatedateBetweenAndRetirestatusAndViewoptionAndApprovedAndRejectedNotOrCreatedbyAndStatusAndCreatedateBetweenAndRetirestatusAndViewoptionAndApprovedAndRejectedNotOrderByProtocolmastercodeDesc(
								objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, 0, 1, 1, 1,
								objuser.getUsercode(), 1, fromdate, todate, 0, 2, 1, 1);
//				lstprotocolmaster = lstprotocolmaster.stream().distinct().collect(Collectors.toList());
				mapSheets.put("approvedTemplate", count);

				count = LSProtocolMasterRepository
						.countByLssitemasterAndStatusAndCreatedateBetweenAndRetirestatusAndViewoptionAndApprovedIsNullAndRejectedNotOrCreatedbyAndStatusAndCreatedateBetweenAndRetirestatusAndViewoptionAndApprovedIsNullAndRejectedNotOrderByProtocolmastercodeDesc(
								objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, 0, 1, 1,
								objuser.getUsercode(), 1, fromdate, todate, 0, 2, 1);
//				lstprotocolmaster = lstprotocolmaster.stream().distinct().collect(Collectors.toList());
				mapSheets.put("createdTemplate", count);

				count = LSProtocolMasterRepository
						.countByLssitemasterAndStatusAndCreatedateBetweenAndRetirestatusAndViewoptionAndApprovedAndRejectedNotOrCreatedbyAndStatusAndCreatedateBetweenAndRetirestatusAndViewoptionAndApprovedAndRejectedNotOrderByProtocolmastercodeDesc(
								objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, 0, 1, 0, 1,
								objuser.getUsercode(), 1, fromdate, todate, 0, 2, 0, 1);
//				lstprotocolmaster = lstprotocolmaster.stream().distinct().collect(Collectors.toList());
				mapSheets.put("inprogressTemplate", count);
				count = LSProtocolMasterRepository
						.countByLssitemasterAndStatusAndCreatedateBetweenAndRetirestatusAndViewoptionOrCreatedbyAndStatusAndCreatedateBetweenAndRetirestatusAndViewoptionOrderByProtocolmastercodeDesc(
								objuser.getLssitemaster().getSitecode(), 1, fromdate, todate, 1, 1, 1,
								objuser.getUsercode(), 1, fromdate, todate, 1, 2, 1);
				mapSheets.put("retiredTemplate", count);

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
			List<LSusersteam> lstteam = lsusersteamRepository.findByLsuserteammappingInAndLssitemaster(lstteammap,
					objupdateduser.getLssitemaster());
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
			try {
				objuser.getObjsilentaudit().setTransactiondate(commonfunction.getCurrentUtcTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
			try {
				objuser.getObjsilentaudit().setTransactiondate(commonfunction.getCurrentUtcTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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

		mapOrders.put("Protocolorders",
				lsprotocolordersharetoRepository.findBySharetounifiedidAndSharedonBetweenAndSharestatus(
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

		Date fromdate = objuser.getObjuser().getFromdate();
		Date todate = objuser.getObjuser().getTodate();
		Map<String, Object> mapOrders = new HashMap<String, Object>();
		List<LSworkflow> lstworkflow = objuser.getLstworkflow();
		List<Elnprotocolworkflow> lstworkflow_protocol = objuser.getLstelnprotocolworkflow();

		if (lstproject != null && lstworkflow != null) {
			List<LSlogilablimsorderdetail.ordersinterface> lstorders = lslogilablimsorderdetailRepository
					.findByOrderflagAndLsprojectmasterInAndLsworkflowInAndAssignedtoIsNullAndCreatedtimestampBetweenOrAssignedtoAndCreatedtimestampBetweenOrderByBatchcodeDesc(
							"N", lstproject, lstworkflow, fromdate, todate, objuser, fromdate, todate);

//			lstorders.addAll(lslogilablimsorderdetailRepository
//					.findByAssignedtoAndCreatedtimestampBetweenOrderByBatchcodeDesc(objuser, fromdate, todate));
//			lstorders.forEach(objorder -> objorder.setLstworkflow(lstworkflow));
			mapOrders.put("orders", lstorders);
		} else {
			mapOrders.put("orders", new ArrayList<Logilabordermaster>());
		}

		if (lstproject != null && objuser.getLstelnprotocolworkflow() != null) {

			List<Protocolorder> lstorders = LSlogilabprotocoldetailRepository
					.findByOrderflagAndLsprojectmasterInAndElnprotocolworkflowInAndAssignedtoIsNullAndCreatedtimestampBetweenOrSitecodeAndAssignedtoAndCreatedtimestampBetweenOrderByCreatedtimestampDesc(
							"N", lstproject, lstworkflow_protocol, fromdate, todate,
							objuser.getLssitemaster().getSitecode(), objuser, fromdate, todate);

//			lstorders.addAll(LSlogilabprotocoldetailRepository
//					.findBySitecodeAndAssignedtoAndCreatedtimestampBetweenOrderByCreatedtimestampDesc(
//							objuser.getLssitemaster().getSitecode(), objuser, fromdate, todate));
//			lstorders.forEach(objorderDetail -> objorderDetail.setLstelnprotocolworkflow(lstworkflow_protocol));
			mapOrders.put("protocolorders", lstorders);
		} else {
			mapOrders.put("protocolorders", new ArrayList<Logilabprotocolorders>());
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

	public Query globalSearchQueryprepare(String Query, LSuserMaster objuser, List<Long> selectedteambatchCodeList,
			List<Integer> usercodeList) {
		String sql = "SELECT * FROM LSlogilablimsorderdetail o "
				+ "INNER JOIN lsfile l ON l.filecode = o.lsfile_filecode " + "WHERE ("
				+ "    o.lsprojectmaster_projectcode IN ("
				+ "        SELECT lsprojectmaster_projectcode FROM LSlogilablimsorderdetail "
				+ "        WHERE lsprojectmaster_projectcode IN ("
				+ "            SELECT projectcode FROM LSprojectmaster " + "            WHERE lsusersteam_teamcode IN ("
				+ "                SELECT teamcode FROM LSuserteammapping WHERE lsuserMaster_usercode = :userCode"
				+ "            ) AND status = 1" + "        )" + "    ) " + "    AND (" + Query + ")" + ") " + "OR ("
				+ "    o.filetype = 0 " + "    AND (" + Query + ")" + ") " + "OR ("
				+ "    o.lsprojectmaster_projectcode IS NULL " + "    AND o.lssamplemaster_samplecode IN ("
				+ "        SELECT DISTINCT m.samplecode FROM LSsamplemaster m "
				+ "        JOIN lslogilablimsorderdetail d ON m.samplecode = d.lssamplemaster_samplecode "
				+ "        WHERE m.lssitemaster_sitecode = :sitecode AND m.status = 1" + "    ) " + "    AND (" + Query
				+ ")" + ") " + "OR (" + "    o.lsprojectmaster_projectcode IS NULL " + "    AND o.viewoption = 1 "
				+ "    AND o.lsusermaster_usercode = :userCode " + "    AND (" + Query + ")" + ") " + "OR ("
				+ "    o.lsprojectmaster_projectcode IS NULL " + "    AND o.viewoption = 2 "
				+ "    AND o.lsusermaster_usercode = :userCode " + "    AND (" + Query + ")" + ") " + "OR ("
				+ "    o.viewoption = 3 " + "    AND o.lsusermaster_usercode = :userCode "
				+ "    AND o.lsprojectmaster_projectcode IN ("
				+ "        SELECT lsprojectmaster_projectcode FROM LSlogilablimsorderdetail "
				+ "        WHERE lsprojectmaster_projectcode IN ("
				+ "            SELECT projectcode FROM LSprojectmaster " + "            WHERE lsusersteam_teamcode IN ("
				+ "                SELECT teamcode FROM LSuserteammapping WHERE lsuserMaster_usercode = :userCode"
				+ "            ) AND status = 1" + "        )" + "    ) " + "    AND (" + Query + ")" + ") " + "OR ("
				+ "    o.viewoption = 3 " + "    AND o.lsusermaster_usercode IN (:userCodes) "
				+ "    AND o.teamselected = false " + "    AND o.lsprojectmaster_projectcode IS NULL " + "    AND ("
				+ Query + ")" + ") " + "OR (" + "    o.viewoption = 3 " + "    AND o.teamselected = true "
				+ "    AND o.batchcode IN (:selectedteambatchCodes) " + "    AND o.lsprojectmaster_projectcode IS NULL "
				+ "    AND (" + Query + ")" + ") " + "ORDER BY batchcode DESC "
				+ "OFFSET :offset ROWS FETCH NEXT :pageSize ROWS ONLY";
//		System.out.println(sql);
		Query query = entityManager.createNativeQuery(sql, LSlogilablimsorderdetail.class);
		query.setParameter("userCode", objuser.getUsercode());
//		List<Integer> usercodeList = objuser.getUsernotify().stream().map(LSuserMaster::getUsercode)
//				.collect(Collectors.toList());
		query.setParameter("sitecode", objuser.getLssitemaster().getSitecode());
		query.setParameter("userCodes", usercodeList);
		query.setParameter("selectedteambatchCodes", selectedteambatchCodeList);
		query.setParameter("offset", objuser.getPagesize() * objuser.getPageperorder());
		return query.setParameter("pageSize", objuser.getPageperorder());
	}

	public Query globalSearchQueryprepareTotalCount(String Query, LSuserMaster objuser,
			List<Long> selectedteambatchCodeList, List<Integer> usercodeList) {
		String sql = "SELECT COUNT(*) FROM LSlogilablimsorderdetail o "
				+ "INNER JOIN lsfile l ON l.filecode = o.lsfile_filecode " + "WHERE ("
				+ "    o.lsprojectmaster_projectcode IN ("
				+ "        SELECT lsprojectmaster_projectcode FROM LSlogilablimsorderdetail "
				+ "        WHERE lsprojectmaster_projectcode IN ("
				+ "            SELECT projectcode FROM LSprojectmaster " + "            WHERE lsusersteam_teamcode IN ("
				+ "                SELECT teamcode FROM LSuserteammapping WHERE lsuserMaster_usercode = :userCode"
				+ "            ) AND status = 1" + "        )" + "    ) " + "    AND (" + Query + ")" + ") " + "OR ("
				+ "    o.filetype = 0 " + "    AND (" + Query + ")" + ") " + "OR ("
				+ "    o.lsprojectmaster_projectcode IS NULL " + "    AND o.lssamplemaster_samplecode IN ("
				+ "        SELECT DISTINCT m.samplecode FROM LSsamplemaster m "
				+ "        JOIN lslogilablimsorderdetail d ON m.samplecode = d.lssamplemaster_samplecode "
				+ "        WHERE m.lssitemaster_sitecode = :sitecode AND m.status = 1" + "    ) " + "    AND (" + Query
				+ ")" + ") " + "OR (" + "    o.lsprojectmaster_projectcode IS NULL " + "    AND o.viewoption = 1 "
				+ "    AND o.lsusermaster_usercode = :userCode " + "    AND (" + Query + ")" + ") " + "OR ("
				+ "    o.lsprojectmaster_projectcode IS NULL " + "    AND o.viewoption = 2 "
				+ "    AND o.lsusermaster_usercode = :userCode " + "    AND (" + Query + ")" + ") " + "OR ("
				+ "    o.viewoption = 3 " + "    AND o.lsusermaster_usercode = :userCode "
				+ "    AND o.lsprojectmaster_projectcode IN ("
				+ "        SELECT lsprojectmaster_projectcode FROM LSlogilablimsorderdetail "
				+ "        WHERE lsprojectmaster_projectcode IN ("
				+ "            SELECT projectcode FROM LSprojectmaster " + "            WHERE lsusersteam_teamcode IN ("
				+ "                SELECT teamcode FROM LSuserteammapping WHERE lsuserMaster_usercode = :userCode"
				+ "            ) AND status = 1" + "        )" + "    ) " + "    AND (" + Query + ")" + ") " + "OR ("
				+ "    o.viewoption = 3 " + "    AND o.lsusermaster_usercode IN (:userCodes) "
				+ "    AND o.teamselected = false " + "    AND o.lsprojectmaster_projectcode IS NULL " + "    AND ("
				+ Query + ")" + ") " + "OR (" + "    o.viewoption = 3 " + "    AND o.teamselected = true "
				+ "    AND o.batchcode IN (:selectedteambatchCodes) " + "    AND o.lsprojectmaster_projectcode IS NULL "
				+ "    AND (" + Query + ")" + ") ";
		Query query = entityManager.createNativeQuery(sql);
		query.setParameter("userCode", objuser.getUsercode());
		query.setParameter("sitecode", objuser.getLssitemaster().getSitecode());
		query.setParameter("userCodes", usercodeList);
		return query.setParameter("selectedteambatchCodes", selectedteambatchCodeList);
	}

	private Query getprotocolordercustomQueryCount(String Query, Integer usercode, List<Integer> userlist,
			List<Lsprotocolorderstructure> lstdir, LSuserMaster objuser, List<Long> selectedteamprotcolorderList) {
		String query = "SELECT COUNT(*) FROM lslogilabprotocoldetail o "
				+ "INNER JOIN lsprotocolmaster l ON o.lsprotocolmaster_protocolmastercode = l.protocolmastercode "
				+ "INNER JOIN lstestmasterlocal m ON m.testcode = o.testcode " + "WHERE ("
				+ "  o.lsprojectmaster_projectcode IN (" + "    SELECT lsprojectmaster_projectcode "
				+ "    FROM LSlogilablimsorderdetail " + "    WHERE lsprojectmaster_projectcode IN ("
				+ "      SELECT projectcode FROM LSprojectmaster " + "      WHERE lsusersteam_teamcode IN ("
				+ "        SELECT teamcode FROM LSuserteammapping WHERE lsuserMaster_usercode = :usercode" + "      )"
				+ "    )" + "  )" + "  AND o.ordercancell IS NULL " + "  AND (" + Query + "  )" + ")" + " OR ("
				+ "  o.lsprojectmaster_projectcode IS NULL " + "  AND o.assignedto_usercode IS NULL "
				+ "  AND o.elnmaterial_nmaterialcode IN (SELECT nmaterialcode FROM elnmaterial WHERE nsitecode = :sitecode) "
				+ "  AND (" + Query + "  )" + "  AND o.viewoption = :viewoption2 " + "  AND o.ordercancell IS NULL"
				+ ")" + " OR (" + "(" + "  o.lsprojectmaster_projectcode IS NULL "
				+ "  AND o.assignedto_usercode IS NULL "
				+ "  AND o.elnmaterial_nmaterialcode IN (SELECT nmaterialcode FROM elnmaterial WHERE nsitecode = :sitecode) "
				+ "  AND (" + Query + "  )" + "  AND o.viewoption = :viewoption3 " + "  AND o.teamselected = false "
				+ "  AND o.createby = :usercode " + "  AND o.ordercancell IS NULL" + ")" + "OR" + "("
				+ "  o.lsprojectmaster_projectcode IS NULL " + "  AND o.assignedto_usercode IS NULL "
				+ "  AND o.elnmaterial_nmaterialcode IN (SELECT nmaterialcode FROM elnmaterial WHERE nsitecode = :sitecode) "
				+ "  AND (" + Query + "  )" + "  AND o.viewoption = :viewoption3 " + "  AND o.teamselected = true "
				+ "  AND o.protocolordercode IN (:protocolordercode)" + "  AND o.createby = :usercode "
				+ "  AND o.ordercancell IS NULL" + ")" + ")" + " OR (" + "("
				+ "  o.lsprojectmaster_projectcode IS NULL " + "  AND o.assignedto_usercode IS NULL "
				+ "  AND o.elnmaterial_nmaterialcode IN (SELECT nmaterialcode FROM elnmaterial WHERE nsitecode = :sitecode) "
				+ "  AND (" + Query + "  )" + "  AND o.viewoption = :viewoption3 " + "  AND o.teamselected = false "
				+ "  AND o.createby IN (:userlist) " + "  AND o.ordercancell IS NULL" + ")" + "OR" + "("
				+ "  o.lsprojectmaster_projectcode IS NULL " + "  AND o.assignedto_usercode IS NULL "
				+ "  AND o.elnmaterial_nmaterialcode IN (SELECT nmaterialcode FROM elnmaterial WHERE nsitecode = :sitecode) "
				+ "  AND (" + Query + "  )" + "  AND o.viewoption = :viewoption3 " + "  AND o.teamselected = true "
				+ "  AND o.protocolordercode IN (:protocolordercode)" + "  AND o.createby IN (:userlist) "
				+ "  AND o.ordercancell IS NULL" + ")" + ")" + " OR (" + "  o.directorycode IN (:lstdir) "
				+ "  AND o.viewoption = :viewoption1 " + "  AND o.lsprojectmaster_projectcode IS NULL "
				+ "  AND o.assignedto_usercode IS NULL " + "  AND o.ordercancell IS NULL " + "  AND (" + Query + "  )"
				+ ")" + " OR (" + "  o.directorycode IN (:lstdir) " + "  AND o.viewoption = :viewoption2 "
				+ "  AND o.lsuserMaster_usercode = :usercode " + "  AND o.lsprojectmaster_projectcode IS NULL "
				+ "  AND o.assignedto_usercode IS NULL " + "  AND o.ordercancell IS NULL " + "  AND (" + Query + "  )"
				+ ")" + " OR (" + "  (" + "  o.directorycode IN (:lstdir) " + "  AND o.viewoption = :viewoption3 "
				+ "  AND o.teamselected = false" + "  AND o.createby IN (:userlist) "
				+ "  AND o.lsprojectmaster_projectcode IS NULL " + "  AND o.assignedto_usercode IS NULL "
				+ "  AND o.ordercancell IS NULL " + "  AND (" + Query + "  )" + "  )" + "OR" + "  ("
				+ "  o.directorycode IN (:lstdir) " + "  AND o.viewoption = :viewoption3 "
				+ "  AND o.teamselected = true" + "  AND o.protocolordercode IN (:protocolordercode)"
				+ "  AND o.createby IN (:userlist) " + "  AND o.lsprojectmaster_projectcode IS NULL "
				+ "  AND o.assignedto_usercode IS NULL " + "  AND o.ordercancell IS NULL " + "  AND (" + Query + "  )"
				+ "  ))";
//		        + "ORDER BY protocolordercode DESC OFFSET :offset ROWS FETCH NEXT :pageSize ROWS ONLY ";

		Query customquery = entityManager.createNativeQuery(query);
		customquery.setParameter("usercode", objuser.getUsercode());
		customquery.setParameter("viewoption1", 1);
		customquery.setParameter("viewoption2", 2);
		customquery.setParameter("viewoption3", 3);
		customquery.setParameter("userlist", userlist);
		customquery.setParameter("lstdir", lstdir);
		customquery.setParameter("sitecode", objuser.getLssitemaster().getSitecode());
//		customquery.setParameter("offset", objuser.getPagesize() * objuser.getPageperorder());
//		customquery.setParameter("pageSize", objuser.getPageperorder());
		return customquery.setParameter("protocolordercode", selectedteamprotcolorderList);

	}

	private Query getprotocolordercustomQuery(String Query, Integer usercode, List<Integer> userlist,
			List<Lsprotocolorderstructure> lstdir, LSuserMaster objuser, List<Long> selectedteamprotcolorderList) {
		String query = "SELECT * FROM lslogilabprotocoldetail o "
				+ "INNER JOIN lsprotocolmaster l ON o.lsprotocolmaster_protocolmastercode = l.protocolmastercode "
				+ "INNER JOIN lstestmasterlocal m ON m.testcode = o.testcode " + "WHERE ("
				+ "  o.lsprojectmaster_projectcode IN (" + "    SELECT lsprojectmaster_projectcode "
				+ "    FROM LSlogilablimsorderdetail " + "    WHERE lsprojectmaster_projectcode IN ("
				+ "      SELECT projectcode FROM LSprojectmaster " + "      WHERE lsusersteam_teamcode IN ("
				+ "        SELECT teamcode FROM LSuserteammapping WHERE lsuserMaster_usercode = :usercode" + "      )"
				+ "    )" + "  )" + "  AND o.ordercancell IS NULL " + "  AND (" + Query + "  )" + ")" + " OR ("
				+ "  o.lsprojectmaster_projectcode IS NULL " + "  AND o.assignedto_usercode IS NULL "
				+ "  AND o.elnmaterial_nmaterialcode IN (SELECT nmaterialcode FROM elnmaterial WHERE nsitecode = :sitecode) "
				+ "  AND (" + Query + "  )" + "  AND o.viewoption = :viewoption2 " + "  AND o.ordercancell IS NULL"
				+ ")" + " OR (" + "(" + "  o.lsprojectmaster_projectcode IS NULL "
				+ "  AND o.assignedto_usercode IS NULL "
				+ "  AND o.elnmaterial_nmaterialcode IN (SELECT nmaterialcode FROM elnmaterial WHERE nsitecode = :sitecode) "
				+ "  AND (" + Query + "  )" + "  AND o.viewoption = :viewoption3 " + "  AND o.teamselected = false "
				+ "  AND o.createby = :usercode " + "  AND o.ordercancell IS NULL" + ")" + "OR" + "("
				+ "  o.lsprojectmaster_projectcode IS NULL " + "  AND o.assignedto_usercode IS NULL "
				+ "  AND o.elnmaterial_nmaterialcode IN (SELECT nmaterialcode FROM elnmaterial WHERE nsitecode = :sitecode) "
				+ "  AND (" + Query + "  )" + "  AND o.viewoption = :viewoption3 " + "  AND o.teamselected = true "
				+ "  AND o.protocolordercode IN (:protocolordercode)" + "  AND o.createby = :usercode "
				+ "  AND o.ordercancell IS NULL" + ")" + ")" + " OR (" + "("
				+ "  o.lsprojectmaster_projectcode IS NULL " + "  AND o.assignedto_usercode IS NULL "
				+ "  AND o.elnmaterial_nmaterialcode IN (SELECT nmaterialcode FROM elnmaterial WHERE nsitecode = :sitecode) "
				+ "  AND (" + Query + "  )" + "  AND o.viewoption = :viewoption3 " + "  AND o.teamselected = false "
				+ "  AND o.createby IN (:userlist) " + "  AND o.ordercancell IS NULL" + ")" + "OR" + "("
				+ "  o.lsprojectmaster_projectcode IS NULL " + "  AND o.assignedto_usercode IS NULL "
				+ "  AND o.elnmaterial_nmaterialcode IN (SELECT nmaterialcode FROM elnmaterial WHERE nsitecode = :sitecode) "
				+ "  AND (" + Query + "  )" + "  AND o.viewoption = :viewoption3 " + "  AND o.teamselected = true "
				+ "  AND o.protocolordercode IN (:protocolordercode)" + "  AND o.createby IN (:userlist) "
				+ "  AND o.ordercancell IS NULL" + ")" + ")" + " OR (" + "  o.directorycode IN (:lstdir) "
				+ "  AND o.viewoption = :viewoption1 " + "  AND o.lsprojectmaster_projectcode IS NULL "
				+ "  AND o.assignedto_usercode IS NULL " + "  AND o.ordercancell IS NULL " + "  AND (" + Query + "  )"
				+ ")" + " OR (" + "  o.directorycode IN (:lstdir) " + "  AND o.viewoption = :viewoption2 "
				+ "  AND o.lsuserMaster_usercode = :usercode " + "  AND o.lsprojectmaster_projectcode IS NULL "
				+ "  AND o.assignedto_usercode IS NULL " + "  AND o.ordercancell IS NULL " + "  AND (" + Query + "  )"
				+ ")" + " OR (" + "  (" + "  o.directorycode IN (:lstdir) " + "  AND o.viewoption = :viewoption3 "
				+ "  AND o.teamselected = false" + "  AND o.createby IN (:userlist) "
				+ "  AND o.lsprojectmaster_projectcode IS NULL " + "  AND o.assignedto_usercode IS NULL "
				+ "  AND o.ordercancell IS NULL " + "  AND (" + Query + "  )" + "  )" + "OR" + "  ("
				+ "  o.directorycode IN (:lstdir) " + "  AND o.viewoption = :viewoption3 "
				+ "  AND o.teamselected = true" + "  AND o.protocolordercode IN (:protocolordercode)"
				+ "  AND o.createby IN (:userlist) " + "  AND o.lsprojectmaster_projectcode IS NULL "
				+ "  AND o.assignedto_usercode IS NULL " + "  AND o.ordercancell IS NULL " + "  AND (" + Query + "  )"
				+ "  )" + ")ORDER BY protocolordercode DESC OFFSET :offset ROWS FETCH NEXT :pageSize ROWS ONLY ";

		Query customquery = entityManager.createNativeQuery(query, LSlogilabprotocoldetail.class);
		customquery.setParameter("usercode", objuser.getUsercode());
		customquery.setParameter("viewoption1", 1);
		customquery.setParameter("viewoption2", 2);
		customquery.setParameter("viewoption3", 3);
		customquery.setParameter("userlist", userlist);
		customquery.setParameter("lstdir", lstdir);
		customquery.setParameter("sitecode", objuser.getLssitemaster().getSitecode());
		customquery.setParameter("offset", objuser.getPagesize() * objuser.getPageperorder());
		customquery.setParameter("pageSize", objuser.getPageperorder());
		return customquery.setParameter("protocolordercode", selectedteamprotcolorderList);

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

		List<LSuserteammapping> teammapping = LSuserteammappingRepositoryObj
				.findByLsuserMasterAndTeamcodeNotNull(objuser);
		List<LSusersteam> userteamlist = lsusersteamRepository.findByLsuserteammappingIn(teammapping);
		List<LSSelectedTeam> selectedorders = LSSelectedTeamRepository
				.findByUserteamInAndCreatedtimestampBetween(userteamlist, fromdate, todate);
		List<Long> selectedteambatchCodeList = new ArrayList<>();
		selectedteambatchCodeList = selectedteambatchCodeList.isEmpty() ? Collections.singletonList(-1L)
				: selectedteambatchCodeList;
		if (selectedorders != null && !selectedorders.isEmpty()) {
			selectedteambatchCodeList = selectedorders.stream().map(LSSelectedTeam::getBatchcode)
					.filter(Objects::nonNull).distinct().collect(Collectors.toList());
		}
		List<LSprotocolselectedteam> selectedteamorders = lsprotoselectedteamRepo
				.findByUserteamInAndCreatedtimestampBetween(userteamlist, fromdate, todate);

		List<Long> selectedteamprotcolorderList = (selectedteamorders != null && !selectedteamorders.isEmpty())
				? selectedteamorders.stream().map(LSprotocolselectedteam::getProtocolordercode).filter(Objects::nonNull)
						.distinct().collect(Collectors.toList())
				: Collections.singletonList(-1L);

		List<Integer> userlist = objuser.getUsernotify() != null
				? objuser.getUsernotify().stream().map(LSuserMaster::getUsercode).collect(Collectors.toList())
				: new ArrayList<Integer>();
		if (lstproject != null) {
			objuser.getUsernotify().add(objuser);
			if (objuser.getObjuser().getOrderselectiontype() == 1) {

				if (testcode == -1 && objuser.getLstprojectforfilter() == null) {
					List<String> searchKeyword = Arrays.stream(searchkeywords.split(" ")).map(String::trim)
							.filter(item -> !item.isEmpty()).map(word -> "%" + word.toLowerCase() + "%")
							.collect(Collectors.toList());
					String Query = "";
					if (objuser.getObjuser().getOrderfor() == 1) {
//						
						List<String> searchFields = new ArrayList<>(Arrays.asList("o.batchid", "o.sequenceid",
								"o.tittle", "o.testname", "o.keyword", "l.filenameuser"));
						for (String name : searchKeyword) {
							for (String columnName : searchFields) {
								if (!Query.isEmpty()) {
									Query += " OR LOWER(" + columnName + ") LIKE LOWER('" + name + "') AND sitecode =:sitecode";
								} else {
									Query += "LOWER(" + columnName + ") LIKE LOWER('" + name + "') AND sitecode =:sitecode";
								}
							}
						}

						Query query = globalSearchQueryprepare(Query, objuser, selectedteambatchCodeList, userlist);
						Query querycount = globalSearchQueryprepareTotalCount(Query, objuser, selectedteambatchCodeList,
								userlist);
						List<LSlogilablimsorderdetail> lsResults = query.getResultList();
						BigInteger countValues = (BigInteger) querycount.getSingleResult();
						count = countValues.longValue();
//						List<LSlogilablimsorderdetail> lsResults = lslogilablimsorderdetailRepository
//								.getLSlogilablimsorderdetailsearchrecords("%" + searchkeywords.toLowerCase() + "%",
//										objuser.getUsercode(), objuser.getUsernotify(),
//										objuser.getLssitemaster().getSitecode(),
//										objuser.getPagesize() * objuser.getPageperorder(), objuser.getPageperorder(),
//										selectedteambatchCodeList);
//						count = lslogilablimsorderdetailRepository.countLSlogilablimsorderdetail(
//								"%" + searchkeywords.toLowerCase() + "%", objuser.getUsercode(),
//								objuser.getUsernotify(), objuser.getLssitemaster().getSitecode());
						lstorders = lsResults.stream()
								.map(lsOrderDetail -> new Logilabordermaster(lsOrderDetail.getBatchcode(),
										lsOrderDetail.getBatchid(), lsOrderDetail.getLsworkflow(),
										lsOrderDetail.getTestname(), lsOrderDetail.getLsfile(),
										lsOrderDetail.getLssamplemaster(), lsOrderDetail.getLsprojectmaster(),
										lsOrderDetail.getFiletype(), lsOrderDetail.getOrderflag(),
										lsOrderDetail.getAssignedto(), lsOrderDetail.getCreatedtimestamp(),
										lsOrderDetail.getCompletedtimestamp(), lsOrderDetail.getKeyword(),
										lsOrderDetail.getLstestmasterlocal(), lsOrderDetail.getOrdercancell(),
										lsOrderDetail.getViewoption(), lsOrderDetail.getLsuserMaster(),
										lsOrderDetail.getTestcode(), lsOrderDetail.getApprovelstatus(),
										lsOrderDetail.getLsordernotification(), lsOrderDetail.getOrdersaved(),
										lsOrderDetail.getRepeat(), lsOrderDetail.getLsautoregisterorders(),
										lsOrderDetail.getSentforapprovel(), lsOrderDetail.getApprovelaccept(),
										lsOrderDetail.getAutoregistercount(), lsOrderDetail.getElnmaterial(),
										 lsOrderDetail.getTeamselected(),lsOrderDetail.getSequenceid()))
								.collect(Collectors.toList());
						rtnobj.put("orders", lstorders);

					} else {

						List<Lsprotocolorderstructure> lstdir;
						List<Long> immutableNegativeValues = Arrays.asList(-3L, -22L);
						if (objuser.getUsernotify() == null) {
							lstdir = lsprotocolorderStructurerepository
									.findBySitemasterAndViewoptionAndDirectorycodeNotInOrCreatedbyAndViewoptionOrCreatedbyAndViewoptionOrderByDirectorycode(
											objuser.getLssitemaster(), 1, immutableNegativeValues, objuser, 2, objuser,
											3);
						} else {
							lstdir = lsprotocolorderStructurerepository
									.findBySitemasterAndViewoptionAndDirectorycodeNotInOrCreatedbyAndViewoptionOrSitemasterAndViewoptionAndCreatedbyInOrderByDirectorycode(
											objuser.getLssitemaster(), 1, immutableNegativeValues, objuser, 2,
											objuser.getLssitemaster(), 3, objuser.getUsernotify());
						}
						lstdir.addAll(
								lsprotocolorderStructurerepository.findByDirectorycodeIn(immutableNegativeValues));
						List<Long> Directory_Code = lstdir.stream().map(Lsprotocolorderstructure::getDirectorycode)
								.collect(Collectors.toList());

						List<String> searchFields = new ArrayList<>(Arrays.asList("o.protocolordername", "m.testname",
								"o.tittle", "o.keyword", "o.sequenceid", "l.protocolmastername"));
						for (String name : searchKeyword) {
							for (String columnName : searchFields) {
								if (!Query.isEmpty()) {
									Query += " OR LOWER(" + columnName + ") LIKE LOWER('" + name + "')";
								} else {
									Query += "LOWER(" + columnName + ") LIKE LOWER('" + name + "')";
								}
							}
						}

						Query customquery = getprotocolordercustomQuery(Query, objuser.getUsercode(), userlist, lstdir,
								objuser, selectedteamprotcolorderList);
						List<LSlogilabprotocoldetail> lsResults = customquery.getResultList();
						customquery = getprotocolordercustomQueryCount(Query, objuser.getUsercode(), userlist, lstdir,
								objuser, selectedteamprotcolorderList);
						BigInteger countValues = (BigInteger) customquery.getSingleResult();
						count = countValues.longValue();

//						List<LSlogilabprotocoldetail> objt = LSlogilabprotocoldetailRepository.getSearchedRecords(
//								objuser.getUsercode(), "%" + searchkeywords.toLowerCase() + "%", 1, 2, 3, userlist,
//								lstdir, objuser.getPagesize() * objuser.getPageperorder(), objuser.getPageperorder(),
//								selectedteamprotcolorderList);
//						count = LSlogilabprotocoldetailRepository.getcountSearchedRecords(objuser.getUsercode(),
//								"%" + searchkeywords.toLowerCase() + "%", 1, 2, 3, userlist, lstdir);

						lstordersprotocol = lsResults.stream()
								.map(protocolrecord -> new Logilabprotocolorders(protocolrecord.getProtocolordercode(),
										protocolrecord.getTestcode(), protocolrecord.getProtoclordername(),
										protocolrecord.getOrderflag(), protocolrecord.getProtocoltype(),
										protocolrecord.getCreatedtimestamp(), protocolrecord.getCompletedtimestamp(),
										protocolrecord.getLsprotocolmaster(), protocolrecord.getlSprotocolworkflow(),
										protocolrecord.getLssamplemaster(), protocolrecord.getLsprojectmaster(),
										protocolrecord.getKeyword(), protocolrecord.getDirectorycode(),
										protocolrecord.getCreateby(), protocolrecord.getAssignedto(),
										protocolrecord.getLsrepositoriesdata(), protocolrecord.getLsrepositories(),
										protocolrecord.getElnmaterial(), protocolrecord.getElnmaterialinventory(),
										protocolrecord.getApproved(), protocolrecord.getRejected(),
										protocolrecord.getOrdercancell(), protocolrecord.getViewoption(),
										protocolrecord.getOrderstarted(), protocolrecord.getOrderstartedby(),
										protocolrecord.getOrderstartedon(), protocolrecord.getLockeduser(),
										protocolrecord.getLockedusername(), protocolrecord.getVersionno(),
										protocolrecord.getElnprotocolworkflow(),
										protocolrecord.getLsordernotification(), protocolrecord.getLsautoregister(),
										protocolrecord.getRepeat(), protocolrecord.getSentforapprovel(),
										protocolrecord.getApprovelaccept(), protocolrecord.getAutoregistercount(),
										protocolrecord.getLsuserMaster(), protocolrecord.getSequenceid(),
										protocolrecord.getModifiedby(), protocolrecord.getModifieddate()))
								.collect(Collectors.toList());

//						lstordersprotocol = LSlogilabprotocoldetailRepository
//								.findByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndProtoclordernameContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndProtoclordernameNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatebyAndCreatedtimestampBetweenAndProtoclordernameContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatebyAndCreatedtimestampBetweenAndProtoclordernameNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndCreatebyInAndProtoclordernameContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndCreatebyInAndProtoclordernameNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseOrderByProtocolordercodeDesc(
//										1, objuser.getLssitemaster().getSitecode(), fromdate, todate, searchkeywords,
//										searchkeywords, 1, objuser.getLssitemaster().getSitecode(), fromdate, todate,
//										searchkeywords, searchkeywords, 2, objuser.getLssitemaster().getSitecode(),
//										objuser.getUsercode(), fromdate, todate, searchkeywords, searchkeywords, 2,
//										objuser.getLssitemaster().getSitecode(), objuser.getUsercode(), fromdate,
//										todate, searchkeywords, searchkeywords, 3,
//										objuser.getLssitemaster().getSitecode(), fromdate, todate, userlist,
//										searchkeywords, searchkeywords, 3, objuser.getLssitemaster().getSitecode(),
//										fromdate, todate, userlist, searchkeywords, searchkeywords, pageable);
//
//						lstordersprotocol.addAll(LSlogilabprotocoldetailRepository
//								.findByLsprojectmasterInAndCreatedtimestampBetweenAndSitecodeAndProtoclordernameContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseOrderByProtocolordercodeDesc(
//										lstproject, fromdate, todate, objuser.getLssitemaster().getSitecode(),
//										searchkeywords, searchkeywords, pageable));
//						lstordersprotocol.addAll(LSlogilabprotocoldetailRepository
//								.findByLsprojectmasterInAndCreatedtimestampBetweenAndSitecodeAndProtoclordernameNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseOrderByProtocolordercodeDesc(
//										lstproject, fromdate, todate, objuser.getLssitemaster().getSitecode(),
//										searchkeywords, searchkeywords, pageable));
//						lstordersprotocol.addAll(LSlogilabprotocoldetailRepository
//								.findByLsprojectmasterInAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndProtoclordernameContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseOrderByProtocolordercodeDesc(
//										lstproject, 3, objuser.getLssitemaster().getSitecode(), fromdate, todate,
//										searchkeywords, searchkeywords, pageable));
//						lstordersprotocol.addAll(LSlogilabprotocoldetailRepository
//								.findByLsprojectmasterInAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndProtoclordernameNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseOrderByProtocolordercodeDesc(
//										lstproject, 3, objuser.getLssitemaster().getSitecode(), fromdate, todate,
//										searchkeywords, searchkeywords, pageable));
//						count = LSlogilabprotocoldetailRepository
//								.countByLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndProtoclordernameContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndProtoclordernameNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatebyAndCreatedtimestampBetweenAndProtoclordernameContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatebyAndCreatedtimestampBetweenAndProtoclordernameNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndCreatebyInAndProtoclordernameContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseOrLsprojectmasterIsNullAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndCreatebyInAndProtoclordernameNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseOrderByProtocolordercodeDesc(
//										1, objuser.getLssitemaster().getSitecode(), fromdate, todate, searchkeywords,
//										searchkeywords, 1, objuser.getLssitemaster().getSitecode(), fromdate, todate,
//										searchkeywords, searchkeywords, 2, objuser.getLssitemaster().getSitecode(),
//										objuser.getUsercode(), fromdate, todate, searchkeywords, searchkeywords, 2,
//										objuser.getLssitemaster().getSitecode(), objuser.getUsercode(), fromdate,
//										todate, searchkeywords, searchkeywords, 3,
//										objuser.getLssitemaster().getSitecode(), fromdate, todate, userlist,
//										searchkeywords, searchkeywords, 3, objuser.getLssitemaster().getSitecode(),
//										fromdate, todate, userlist, searchkeywords, searchkeywords);
//
//						count = count + LSlogilabprotocoldetailRepository
//								.countByLsprojectmasterInAndCreatedtimestampBetweenAndSitecodeAndProtoclordernameContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseOrderByProtocolordercodeDesc(
//										lstproject, fromdate, todate, objuser.getLssitemaster().getSitecode(),
//										searchkeywords, searchkeywords);
//						count = count + LSlogilabprotocoldetailRepository
//								.countByLsprojectmasterInAndCreatedtimestampBetweenAndSitecodeAndProtoclordernameNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseOrderByProtocolordercodeDesc(
//										lstproject, fromdate, todate, objuser.getLssitemaster().getSitecode(),
//										searchkeywords, searchkeywords);
//						count = count + LSlogilabprotocoldetailRepository
//								.countByLsprojectmasterInAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndProtoclordernameContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseOrderByProtocolordercodeDesc(
//										lstproject, 3, objuser.getLssitemaster().getSitecode(), fromdate, todate,
//										searchkeywords, searchkeywords);
//						count = count + LSlogilabprotocoldetailRepository
//								.countByLsprojectmasterInAndViewoptionAndSitecodeAndCreatedtimestampBetweenAndProtoclordernameNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseOrderByProtocolordercodeDesc(
//										lstproject, 3, objuser.getLssitemaster().getSitecode(), fromdate, todate,
//										searchkeywords, searchkeywords);

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
										3, fromdate, todate, objuser, searchkeywords, searchkeywords, searchkeywords, 3,
										fromdate, todate, objuser, searchkeywords, directorycode, 1, fromdate, todate,
										searchkeywords, searchkeywords, searchkeywords, directorycode, 1, fromdate,
										todate, searchkeywords, directorycode, 2, fromdate, todate, objuser,
										searchkeywords, searchkeywords, searchkeywords, directorycode, 2, fromdate,
										todate, objuser, searchkeywords, directorycode, 3, fromdate, todate, objuser,
										searchkeywords, searchkeywords, searchkeywords, directorycode, 3, fromdate,
										todate, objuser, searchkeywords, pageable));
						lstorders.addAll(lslogilablimsorderdetailRepository
								.findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndBatchidNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseAndLsuserMasterNotOrderByBatchcodeDesc(
										lstsample, 1, fromdate, todate, searchkeywords, searchkeywords, searchkeywords,
										objuser, pageable));
						lstorders.addAll(lslogilablimsorderdetailRepository
								.findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndKeywordContainingIgnoreCaseAndTestnameIsNullAndLsuserMasterNotOrderByBatchcodeDesc(
										lstsample, 1, fromdate, todate, searchkeywords, objuser, pageable));
						lstorders.addAll(lslogilablimsorderdetailRepository
								.findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndBatchidNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseOrderByBatchcodeDesc(
										lstsample, 2, fromdate, todate, objuser, searchkeywords, searchkeywords,
										searchkeywords, pageable));
						lstorders.addAll(lslogilablimsorderdetailRepository
								.findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndKeywordContainingIgnoreCaseAndTestnameIsNullOrderByBatchcodeDesc(
										lstsample, 2, fromdate, todate, objuser, searchkeywords, pageable));

						count = count + lslogilablimsorderdetailRepository
								.countByViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndBatchidNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndKeywordContainingIgnoreCaseAndTestnameIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndBatchidNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndKeywordContainingIgnoreCaseAndTestnameIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndBatchidNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndKeywordContainingIgnoreCaseAndTestnameIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndBatchidNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndKeywordContainingIgnoreCaseAndTestnameIsNullOrderByBatchcodeDesc(
										3, fromdate, todate, objuser, searchkeywords, searchkeywords, searchkeywords, 3,
										fromdate, todate, objuser, searchkeywords, directorycode, 1, fromdate, todate,
										searchkeywords, searchkeywords, searchkeywords, directorycode, 1, fromdate,
										todate, searchkeywords, directorycode, 2, fromdate, todate, objuser,
										searchkeywords, searchkeywords, searchkeywords, directorycode, 2, fromdate,
										todate, objuser, searchkeywords, directorycode, 3, fromdate, todate, objuser,
										searchkeywords, searchkeywords, searchkeywords, directorycode, 3, fromdate,
										todate, objuser, searchkeywords);

						count = count + lslogilablimsorderdetailRepository
								.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndBatchidNotContainingIgnoreCaseAndKeywordContainingIgnoreCaseAndTestnameNotContainingIgnoreCaseAndLsuserMasterNotOrderByBatchcodeDesc(
										lstsample, 1, fromdate, todate, searchkeywords, searchkeywords, searchkeywords,
										objuser);
						count = count + lslogilablimsorderdetailRepository
								.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndKeywordContainingIgnoreCaseAndTestnameIsNullAndLsuserMasterNotOrderByBatchcodeDesc(
										lstsample, 1, fromdate, todate, searchkeywords, objuser);

						count = count + lslogilablimsorderdetailRepository
								.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndKeywordContainingIgnoreCaseAndTestnameIsNullOrderByBatchcodeDesc(
										lstsample, 2, fromdate, todate, objuser, searchkeywords);

						lstorders.addAll(lslogilablimsorderdetailRepository
								.findByViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndBatchidNotContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameContainingIgnoreCaseOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTestnameContainingIgnoreCaseAndKeywordIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndBatchidNotContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameContainingIgnoreCaseOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndTestnameContainingIgnoreCaseAndKeywordIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndBatchidNotContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameContainingIgnoreCaseOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTestnameContainingIgnoreCaseAndKeywordIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndBatchidNotContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameContainingIgnoreCaseOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTestnameContainingIgnoreCaseAndKeywordIsNullOrderByBatchcodeDesc(
										3, fromdate, todate, objuser, searchkeywords, searchkeywords, searchkeywords, 3,
										fromdate, todate, objuser, searchkeywords, directorycode, 1, fromdate, todate,
										searchkeywords, searchkeywords, searchkeywords, directorycode, 1, fromdate,
										todate, searchkeywords, directorycode, 2, fromdate, todate, objuser,
										searchkeywords, searchkeywords, searchkeywords, directorycode, 2, fromdate,
										todate, objuser, searchkeywords, directorycode, 3, fromdate, todate, objuser,
										searchkeywords, searchkeywords, searchkeywords, directorycode, 3, fromdate,
										todate, objuser, searchkeywords, pageable));
						lstorders.addAll(lslogilablimsorderdetailRepository
								.findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndBatchidNotContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameContainingIgnoreCaseAndLsuserMasterNotOrderByBatchcodeDesc(
										lstsample, 1, fromdate, todate, searchkeywords, searchkeywords, searchkeywords,
										objuser, pageable));
						lstorders.addAll(lslogilablimsorderdetailRepository
								.findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndTestnameContainingIgnoreCaseAndKeywordIsNullAndLsuserMasterNotOrderByBatchcodeDesc(
										lstsample, 1, fromdate, todate, searchkeywords, objuser, pageable));
						lstorders.addAll(lslogilablimsorderdetailRepository
								.findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndBatchidNotContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameContainingIgnoreCaseOrderByBatchcodeDesc(
										lstsample, 2, fromdate, todate, objuser, searchkeywords, searchkeywords,
										searchkeywords, pageable));
						lstorders.addAll(lslogilablimsorderdetailRepository
								.findByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTestnameContainingIgnoreCaseAndKeywordIsNullOrderByBatchcodeDesc(
										lstsample, 2, fromdate, todate, objuser, searchkeywords, pageable));

						count = count + lslogilablimsorderdetailRepository
								.countByViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndBatchidNotContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameContainingIgnoreCaseOrViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTestnameContainingIgnoreCaseAndKeywordIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndBatchidNotContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameContainingIgnoreCaseOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndTestnameContainingIgnoreCaseAndKeywordIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndBatchidNotContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameContainingIgnoreCaseOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTestnameContainingIgnoreCaseAndKeywordIsNullOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndBatchidNotContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameContainingIgnoreCaseOrLsprojectmasterIsNullAndDirectorycodeInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTestnameContainingIgnoreCaseAndKeywordIsNullOrderByBatchcodeDesc(
										3, fromdate, todate, objuser, searchkeywords, searchkeywords, searchkeywords, 3,
										fromdate, todate, objuser, searchkeywords, directorycode, 1, fromdate, todate,
										searchkeywords, searchkeywords, searchkeywords, directorycode, 1, fromdate,
										todate, searchkeywords, directorycode, 2, fromdate, todate, objuser,
										searchkeywords, searchkeywords, searchkeywords, directorycode, 2, fromdate,
										todate, objuser, searchkeywords, directorycode, 3, fromdate, todate, objuser,
										searchkeywords, searchkeywords, searchkeywords, directorycode, 3, fromdate,
										todate, objuser, searchkeywords);
						count = count + lslogilablimsorderdetailRepository
								.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndBatchidNotContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameContainingIgnoreCaseAndLsuserMasterNotOrderByBatchcodeDesc(
										lstsample, 1, fromdate, todate, searchkeywords, searchkeywords, searchkeywords,
										objuser);
						count = count + lslogilablimsorderdetailRepository
								.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndTestnameContainingIgnoreCaseAndKeywordIsNullAndLsuserMasterNotOrderByBatchcodeDesc(
										lstsample, 1, fromdate, todate, searchkeywords, objuser);
						count = count + lslogilablimsorderdetailRepository
								.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndBatchidNotContainingIgnoreCaseAndKeywordNotContainingIgnoreCaseAndTestnameContainingIgnoreCaseOrderByBatchcodeDesc(
										lstsample, 2, fromdate, todate, objuser, searchkeywords, searchkeywords,
										searchkeywords);
						count = count + lslogilablimsorderdetailRepository
								.countByLsprojectmasterIsNullAndLssamplemasterInAndViewoptionAndCreatedtimestampBetweenAndLsuserMasterAndTestnameContainingIgnoreCaseAndKeywordIsNullOrderByBatchcodeDesc(
										lstsample, 2, fromdate, todate, objuser, searchkeywords);

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

	public Query setparameters(Query query, LSuserMaster objuser, List<Integer> usercodelist, boolean count,
			boolean usercode, boolean viewoption3) {
		query.setParameter("sitecode", objuser.getLssitemaster().getSitecode());
		query.setParameter("viewoption1", 1);
		query.setParameter("usecodelist", usercodelist);
		query.setParameter("viewoption2", 2);
		if (!count) {
			query.setParameter("offset", objuser.getPagesize() * objuser.getPageperorder());
			query.setParameter("pageSize", objuser.getPageperorder());
		}
		if (usercode) {
			query.setParameter("usercode", objuser.getUsercode());
		}
		if (viewoption3) {
			query.setParameter("viewoption3", 3);
		}
		return query;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> Getglobalsearchforsheettemplate(LSuserMaster objuser) {
		Date fromdate = objuser.getObjuser().getFromdate();
		Date todate = objuser.getObjuser().getTodate();
		Map<String, Object> mapSheets = new HashMap<String, Object>();
		List<LSuserMaster> lstteamuser = objuser.getObjuser().getTeamusers();
		List<Sheettemplateget> lstfile = new ArrayList<>();
		Pageable pageable = new PageRequest(objuser.getPagesize(), objuser.getPageperorder());
		long count = 0;
		String CustomLikeQuery = "";
//		String Search_Key = "%" + objuser.getToken() + "%"; // search key porpose kumu
		List<String> SearchKey = Arrays.stream(objuser.getToken().split(" ")).map(String::trim)
				.filter(item -> !item.isEmpty()).map(word -> "%" + word.toLowerCase() + "%")
				.collect(Collectors.toList());
		List<String> searchFields = new ArrayList<>(Arrays.asList("o.category", "o.filenameuser"));
		for (String name : SearchKey) {
			for (String columnName : searchFields) {
				if (!CustomLikeQuery.isEmpty()) {
					CustomLikeQuery += " OR LOWER(" + columnName + ") LIKE LOWER('" + name + "')";
				} else {
					CustomLikeQuery += "LOWER(" + columnName + ") LIKE LOWER('" + name + "')";
				}
			}
		}
		String customQuery = "";
		List<Integer> usercodelist = new ArrayList<>();
		if (objuser.getUsername().equals("Administrator")) {
			usercodelist.add(objuser.getUsercode());
			customQuery = "SELECT * FROM LSFILE o WHERE "
					+ "( filecode > 1 AND lssitemaster_sitecode = :sitecode AND VIEWOPTION = :viewoption1 " + "AND ("
					+ CustomLikeQuery + ")) " + "OR ( (" + CustomLikeQuery + ") "
					+ "AND createby_usercode IN(:usecodelist) AND VIEWOPTION = :viewoption2 ) "
					+ "ORDER BY filecode DESC OFFSET :offset ROWS FETCH NEXT :pageSize ROWS ONLY";
			Query query = entityManager.createNativeQuery(customQuery, LSfile.class);
			query = setparameters(query, objuser, usercodelist, false, false, false);
			lstfile = query.getResultList();
//			lstfile = lsfileRepository
//					.findByFilecodeGreaterThanAndLssitemasterAndViewoptionAndFilenameuserLikeOrFilecodeGreaterThanAndCreatebyInAndViewoptionAndFilenameuserLikeOrderByFilecodeDesc(
//							1, objuser.getLssitemaster(), 1, Search_Key, 1, objuser, 2, Search_Key, pageable);

			customQuery = "SELECT COUNT(*) FROM LSFILE o WHERE "
					+ "( filecode > 1 AND lssitemaster_sitecode = :sitecode AND VIEWOPTION = :viewoption1 " + "AND ("
					+ CustomLikeQuery + ")) " + "OR ( (" + CustomLikeQuery + ") "
					+ "AND createby_usercode IN(:usecodelist) AND VIEWOPTION = :viewoption2 ) ";
			query = entityManager.createNativeQuery(customQuery);
			query = setparameters(query, objuser, usercodelist, true, false, false);
			BigInteger countValues = (BigInteger) query.getSingleResult();
			count = countValues.longValue();
//			count = lsfileRepository
//					.countByFilecodeGreaterThanAndLssitemasterAndViewoptionAndFilenameuserLikeOrFilecodeGreaterThanAndCreatebyInAndViewoptionAndFilenameuserLikeOrderByFilecodeDesc(
//							1, objuser.getLssitemaster(), 1, Search_Key, 1, objuser, 2, Search_Key);
//			lstfile.forEach(
//					objFile -> objFile.setVersioncout(lsfileversionRepository.countByFilecode(objFile.getFilecode())));
		} else {

			if (lstteamuser != null && lstteamuser.size() > 0) {
				lstteamuser.add(objuser);
				usercodelist = lstteamuser.stream().map(LSuserMaster::getUsercode).collect(Collectors.toList());
				customQuery = "SELECT * FROM LSFILE o WHERE "
						+ "(filecode > 1 AND lssitemaster_sitecode = :sitecode AND VIEWOPTION = :viewoption1 AND ("
						+ CustomLikeQuery + ")) "
						+ "OR (filecode > 1 AND createby_usercode = :usercode AND VIEWOPTION = :viewoption2 AND ("
						+ CustomLikeQuery + ")) "
						+ "OR (filecode > 1 AND createby_usercode IN(:usecodelist) AND VIEWOPTION = :viewoption3 AND ("
						+ CustomLikeQuery + ")) "
						+ "ORDER BY filecode DESC OFFSET :offset ROWS FETCH NEXT :pageSize ROWS ONLY";

				Query query = entityManager.createNativeQuery(customQuery, LSfile.class);
				query = setparameters(query, objuser, usercodelist, false, true, true);
				lstfile = query.getResultList();

				customQuery = "SELECT COUNT(*) FROM LSFILE o WHERE "
						+ "(filecode > 1 AND lssitemaster_sitecode = :sitecode AND VIEWOPTION = :viewoption1 AND ("
						+ CustomLikeQuery + ")) "
						+ "OR (filecode > 1 AND createby_usercode = :usercode AND VIEWOPTION = :viewoption2 AND ("
						+ CustomLikeQuery + ")) "
						+ "OR (filecode > 1 AND createby_usercode IN(:usecodelist) AND VIEWOPTION = :viewoption3 AND ("
						+ CustomLikeQuery + ")) ";
				query = entityManager.createNativeQuery(customQuery);
				query = setparameters(query, objuser, usercodelist, true, true, true);
				BigInteger countValues = (BigInteger) query.getSingleResult();
				count = countValues.longValue();
//				lstfile = lsfileRepository
//						.findByFilecodeGreaterThanAndLssitemasterAndViewoptionAndFilenameuserLikeOrFilecodeGreaterThanAndCreatebyAndViewoptionAndFilenameuserLikeOrFilecodeGreaterThanAndCreatebyInAndViewoptionAndFilenameuserLikeOrderByFilecodeDesc(
//								1, objuser.getLssitemaster(), 1, Search_Key, 1, objuser, 2, Search_Key, 1, lstteamuser,
//								3, Search_Key, pageable);
//
//				count = lsfileRepository
//						.countByFilecodeGreaterThanAndLssitemasterAndViewoptionAndFilenameuserLikeOrFilecodeGreaterThanAndCreatebyAndViewoptionAndFilenameuserLikeOrFilecodeGreaterThanAndCreatebyInAndViewoptionAndFilenameuserLikeOrderByFilecodeDesc(
//								1, objuser.getLssitemaster(), 1, Search_Key, 1, objuser, 2, Search_Key, 1, lstteamuser,
//								3, Search_Key);

			} else {
				customQuery = "SELECT * FROM LSFILE o WHERE "
						+ "(filecode > 1 AND lssitemaster_sitecode = :sitecode AND VIEWOPTION = :viewoption1 AND ("
						+ CustomLikeQuery + ")) "
						+ "OR (filecode > 1 AND createby_usercode = :usercode AND VIEWOPTION = :viewoption2 AND ("
						+ CustomLikeQuery + ")) "
						+ "ORDER BY filecode DESC OFFSET :offset ROWS FETCH NEXT :pageSize ROWS ONLY";
				Query query = entityManager.createNativeQuery(customQuery, LSfile.class);
				query = setparameters(query, objuser, usercodelist, false, false, false);
				lstfile = query.getResultList();
//				lstfile = lsfileRepository
//						.findByFilecodeGreaterThanAndLssitemasterAndViewoptionAndFilenameuserLikeOrFilecodeGreaterThanAndCreatebyAndViewoptionAndFilenameuserLikeOrderByFilecodeDesc(
//								1, objuser.getLssitemaster(), 1, objuser.getToken(), 1, objuser, 2, Search_Key,
//								pageable);
				customQuery = "SELECT * FROM LSFILE o WHERE "
						+ "(filecode > 1 AND lssitemaster_sitecode = :sitecode AND VIEWOPTION = :viewoption1 AND ("
						+ CustomLikeQuery + ")) "
						+ "OR (filecode > 1 AND createby_usercode = :usercode AND VIEWOPTION = :viewoption2 AND ("
						+ CustomLikeQuery + ")) ";
				query = entityManager.createNativeQuery(customQuery);
				query = setparameters(query, objuser, usercodelist, true, true, true);
				BigInteger countValues = (BigInteger) query.getSingleResult();
				count = countValues.longValue();
//				count = lsfileRepository
//						.countByFilecodeGreaterThanAndLssitemasterAndViewoptionAndFilenameuserLikeOrFilecodeGreaterThanAndCreatebyAndViewoptionAndFilenameuserLikeOrderByFilecodeDesc(
//								1, objuser.getLssitemaster(), 1, objuser.getToken(), 1, objuser, 2, Search_Key);
			}
//			lstfile.forEach(
//					objFile -> objFile.setVersioncout(lsfileversionRepository.countByFilecode(objFile.getFilecode())));

		}
		mapSheets.put("Sheets", lstfile);
		mapSheets.put("count", count);
		return mapSheets;
	}

	private Query setparametersforprotocol(Query query,Integer sitecode, boolean viewoption1, boolean viewoption2, boolean viewoption3, Integer usercode,
			List<Integer> usercodelist, boolean status,boolean count,LSuserMaster objuser) {
		if(sitecode!=null) {
			query.setParameter("sitecode", sitecode);
		}
		if(viewoption1) {
			query.setParameter("viewoption1", 1);
		}
		if(usercode!=null) {
			query.setParameter("createdby", usercode);
		}
		if(usercodelist!=null) {
			query.setParameter("createdbylist", usercodelist);
		}
		if(viewoption2) {
			query.setParameter("viewoption2", 2);
		}
		if(viewoption3) {
			query.setParameter("viewoption3", 3);
		}
		if(status) {
			query.setParameter("status", 1);
		}
		if (!count) {
			query.setParameter("offset", objuser.getPagesize() * objuser.getPageperorder());
			query.setParameter("pageSize", objuser.getPageperorder());
		}
		return query;
	}

	public Map<String, Object> Getglobalsearchforprotocoltemplate(LSuserMaster objuser) {
		Date fromdate = objuser.getObjuser().getFromdate();
		Date todate = objuser.getObjuser().getTodate();
		Map<String, Object> mapSheets = new HashMap<String, Object>();
		List<LSuserMaster> lstteamuser = objuser.getObjuser().getTeamusers();
		Pageable pageable = new PageRequest(objuser.getPagesize(), objuser.getPageperorder());
		String Search_Key = "%" + objuser.getToken() + "%"; // search key porpose kumu

		String CustomLikeQuery = "";
		List<String> SearchKey = Arrays.stream(objuser.getToken().split(" ")).map(String::trim)
				.filter(item -> !item.isEmpty()).map(word -> "%" + word.toLowerCase() + "%")
				.collect(Collectors.toList());
		List<String> searchFields = new ArrayList<>(Arrays.asList("o.category", "o.protocolmastername"));
		for (String name : SearchKey) {
			for (String columnName : searchFields) {
				if (!CustomLikeQuery.isEmpty()) {
					CustomLikeQuery += " OR LOWER(" + columnName + ") LIKE LOWER('" + name + "')";
				} else {
					CustomLikeQuery += "LOWER(" + columnName + ") LIKE LOWER('" + name + "')";
				}
			}
		}
		String customQuery = "";

		long count = 0;
		List<Protocoltemplateget> lstprotocolmaster = new ArrayList<>();
		if (objuser.getUsername().equals("Administrator")) {

//			usercodelist.add(objuser.getUsercode());
			customQuery = "SELECT * FROM lsprotocolmaster o WHERE lssitemaster_sitecode =:sitecode AND status =:status AND "
					+ CustomLikeQuery
					+ "ORDER BY protocolmastercode DESC OFFSET :offset ROWS FETCH NEXT :pageSize ROWS ONLY";
			Query query = entityManager.createNativeQuery(customQuery, LSprotocolmaster.class);
			query = setparametersforprotocol(query, objuser.getLssitemaster().getSitecode(), false, false, false,
					objuser.getUsercode(), null, true, false, objuser);
			lstprotocolmaster = query.getResultList();

			customQuery = "SELECT COUNT(*) FROM lsprotocolmaster o WHERE lssitemaster_sitecode =:sitecode AND status =1 AND "
					+ CustomLikeQuery + "";

			query = entityManager.createNativeQuery(customQuery);
			query = setparametersforprotocol(query, objuser.getLssitemaster().getSitecode(), false, false, false,
					objuser.getUsercode(), null, false, false, objuser);
			BigInteger countValues = (BigInteger) query.getSingleResult();
			count = countValues.longValue();
//			query = entityManager.createNativeQuery(customQuery);
//			query = setparameters(query, objuser, usercodelist, true, true, true);
//			BigInteger countValues = (BigInteger) query.getSingleResult();
//			count = countValues.longValue();

//			mapSheets.put("Protocol", LSProtocolMasterRepository.findByLssitemasterAndStatusAndProtocolmasternameLike(
//					objuser.getLssitemaster().getSitecode(), 1, Search_Key, pageable));
//			mapSheets.put("count", LSProtocolMasterRepository.countByLssitemasterAndStatusAndProtocolmasternameLike(
//					objuser.getLssitemaster().getSitecode(), 1, Search_Key));
			mapSheets.put("count", count);
			mapSheets.put("Protocol", lstprotocolmaster);
		} else {

		
			if (lstteamuser != null && lstteamuser.size() > 0) {
				lstteamuser.add(objuser);
				List<Integer> usercodelist = lstteamuser.stream().map(LSuserMaster::getUsercode)
						.collect(Collectors.toList());

//				
				customQuery = "SELECT * FROM lsprotocolmaster o WHERE  (lssitemaster_sitecode=:sitecode AND viewoption =:viewoption1 AND ("
						+ CustomLikeQuery
						+ ")) OR (createdby =:createdby AND status =:status AND viewoption =:viewoption2 AND lssitemaster_sitecode=:sitecode AND ("
						+ CustomLikeQuery
						+ "))OR (createdby IN(:createdbylist)AND status =:status AND viewoption =:viewoption3 AND lssitemaster_sitecode=:sitecode AND ("
						+ CustomLikeQuery + "))ORDER BY protocolmastercode DESC OFFSET :offset ROWS FETCH NEXT :pageSize ROWS ONLY";
				Query query = entityManager.createNativeQuery(customQuery, LSprotocolmaster.class);
				query = setparametersforprotocol(query, objuser.getLssitemaster().getSitecode(), true, true, true,
						objuser.getUsercode(), usercodelist, true, false, objuser);
				lstprotocolmaster = query.getResultList();
				
				
				customQuery = "SELECT COUNT(*) FROM lsprotocolmaster o WHERE  (lssitemaster_sitecode=:sitecode AND viewoption =:viewoption1 AND ("
						+ CustomLikeQuery
						+ ")) OR (createdby =:createdby AND status =:status AND viewoption =:viewoption2 AND lssitemaster_sitecode=:sitecode AND ("
						+ CustomLikeQuery
						+ "))OR (createdby IN(:createdbylist)AND status =:status AND viewoption =:viewoption3 AND lssitemaster_sitecode=:sitecode AND ("
						+ CustomLikeQuery + "))";
				
				query = entityManager.createNativeQuery(customQuery);
				query = setparametersforprotocol(query, objuser.getLssitemaster().getSitecode(), true, true, true,
						objuser.getUsercode(), usercodelist, true, true, objuser);
				BigInteger countValues = (BigInteger) query.getSingleResult();
				count = countValues.longValue();

//				lstprotocolmaster = LSProtocolMasterRepository
//						.findByLssitemasterAndStatusAndViewoptionAndProtocolmasternameLikeOrCreatedbyAndStatusAndViewoptionAndLssitemasterAndProtocolmasternameLikeOrCreatedbyInAndStatusAndViewoptionAndLssitemasterAndProtocolmasternameLikeOrderByProtocolmastercodeDesc(
//								objuser.getLssitemaster().getSitecode(), 1, 1, Search_Key, objuser.getUsercode(), 1, 2,
//								objuser.getLssitemaster().getSitecode(), Search_Key, usercodelist, 1, 3,
//								objuser.getLssitemaster().getSitecode(), Search_Key, pageable);
//				count = LSProtocolMasterRepository
//						.countByLssitemasterAndStatusAndViewoptionAndProtocolmasternameLikeOrCreatedbyAndStatusAndViewoptionAndLssitemasterAndProtocolmasternameLikeOrCreatedbyInAndStatusAndViewoptionAndLssitemasterAndProtocolmasternameLikeOrderByProtocolmastercodeDesc(
//								objuser.getLssitemaster().getSitecode(), 1, 1, Search_Key, objuser.getUsercode(), 1, 2,
//								objuser.getLssitemaster().getSitecode(), Search_Key, usercodelist, 1, 3,
//								objuser.getLssitemaster().getSitecode(), Search_Key);

			} else {
				
				customQuery = "SELECT * FROM lsprotocolmaster o WHERE  (lssitemaster_sitecode=:sitecode AND status =:status AND viewoption =:viewoption1 AND ("
						+ CustomLikeQuery
						+ ")) OR (createdby =:createdby AND status =:status AND viewoption =:viewoption2 AND lssitemaster_sitecode=:sitecode AND ("
						+ CustomLikeQuery
						+ ")))ORDER BY protocolmastercode DESC OFFSET :offset ROWS FETCH NEXT :pageSize ROWS ONLY";
				Query query = entityManager.createNativeQuery(customQuery, LSprotocolmaster.class);
				query = setparametersforprotocol(query, objuser.getLssitemaster().getSitecode(), true, true, false,
						objuser.getUsercode(), null, true, false, objuser);
				lstprotocolmaster = query.getResultList();
				

				customQuery = "SELECT COUNT(*) FROM lsprotocolmaster o WHERE  (lssitemaster_sitecode=:sitecode AND status =:status AND viewoption =:viewoption1 AND ("
						+ CustomLikeQuery
						+ ")) OR (createdby =:createdby AND status =:status AND viewoption =:viewoption2 AND lssitemaster_sitecode=:sitecode AND ("
						+ CustomLikeQuery
						+ ")))";
				
				query = entityManager.createNativeQuery(customQuery);
				query = setparametersforprotocol(query, objuser.getLssitemaster().getSitecode(), true, true, false,
						objuser.getUsercode(), null, true, false, objuser);
				BigInteger countValues = (BigInteger) query.getSingleResult();
				count = countValues.longValue();
//				lstprotocolmaster = LSProtocolMasterRepository
//						.findByLssitemasterAndStatusAndViewoptionAndProtocolmasternameLikeOrCreatedbyAndStatusAndViewoptionAndLssitemasterAndProtocolmasternameLikeOrderByProtocolmastercodeDesc(
//								objuser.getLssitemaster().getSitecode(), 1, 1, Search_Key, objuser.getUsercode(), 1, 2,
//								objuser.getLssitemaster().getSitecode(), Search_Key, pageable);
//
//				count = LSProtocolMasterRepository
//						.countByLssitemasterAndStatusAndViewoptionAndProtocolmasternameLikeOrCreatedbyAndStatusAndViewoptionAndLssitemasterAndProtocolmasternameLikeOrderByProtocolmastercodeDesc(
//								objuser.getLssitemaster().getSitecode(), 1, 1, Search_Key, objuser.getUsercode(), 1, 2,
//								objuser.getLssitemaster().getSitecode(), Search_Key);
			}

			lstprotocolmaster = lstprotocolmaster.stream().distinct().collect(Collectors.toList());
			mapSheets.put("count", count);
			mapSheets.put("Protocol", lstprotocolmaster);
		}

		return mapSheets;
	}

	public Map<String, Object> Getglobalsearchfolder(LSuserMaster objusermaster) {
		Map<String, Object> mapfolders = new HashMap<String, Object>();
		List<Long> immutableNegativeValues = Arrays.asList(-3L, -22L);
		if (objusermaster.getResponse().getInformation().equals("Protocol")) {
			List<Lsprotocolorderstructure> lstdir = new ArrayList<Lsprotocolorderstructure>();
			if (objusermaster.getUsernotify() == null) {
				lstdir = lsprotocolorderStructurerepository
						.findBySitemasterAndViewoptionAndDirectorycodeNotInOrCreatedbyAndViewoptionOrCreatedbyAndViewoptionOrderByDirectorycode(
								objusermaster.getLssitemaster(), 1, immutableNegativeValues, objusermaster, 2,
								objusermaster, 3);

			} else {
				lstdir = lsprotocolorderStructurerepository
						.findBySitemasterAndViewoptionAndDirectorycodeNotInOrCreatedbyAndViewoptionOrSitemasterAndViewoptionAndCreatedbyInOrderByDirectorycode(
								objusermaster.getLssitemaster(), 1, immutableNegativeValues, objusermaster, 2,
								objusermaster.getLssitemaster(), 3, objusermaster.getUsernotify());

			}
			lstdir.addAll(lsprotocolorderStructurerepository.findByDirectorycodeIn(immutableNegativeValues));
			mapfolders.put("Folders_Protocol", lstdir);
		} else if (objusermaster.getResponse().getInformation().equals("Sheet")) {
			List<LSSheetOrderStructure> lstdir = new ArrayList<LSSheetOrderStructure>();
			if (objusermaster.getUsernotify() == null) {
				lstdir = lsSheetOrderStructureRepository
						.findBySitemasterAndViewoptionAndDirectorycodeNotInOrCreatedbyAndViewoptionOrCreatedbyAndViewoptionOrderByDirectorycode(
								objusermaster.getLssitemaster(), 1, immutableNegativeValues, objusermaster, 2,
								objusermaster, 3);

			} else {
				lstdir = lsSheetOrderStructureRepository
						.findBySitemasterAndViewoptionAndDirectorycodeNotInOrCreatedbyAndViewoptionOrSitemasterAndViewoptionAndCreatedbyInOrderByDirectorycode(
								objusermaster.getLssitemaster(), 1, immutableNegativeValues, objusermaster, 2,
								objusermaster.getLssitemaster(), 3, objusermaster.getUsernotify());

			}

			lstdir.addAll(lsSheetOrderStructureRepository.findByDirectorycodeIn(immutableNegativeValues));
			mapfolders.put("Folders_Sheet", lstdir);
		}

		return mapfolders;
	}

	public Map<String, Object> Getglobalsearchforfile(LSuserMaster objusermaster) {
		Map<String, Object> mapfolders = new HashMap<String, Object>();
		String Search_Key = "%" + objusermaster.getToken() + "%"; // search key porpose kumu
		long Count_Directory_File = 0;
//		Pageable pageable = new PageRequest(objusermaster.getPagesize(), objusermaster.getPageperorder());
		objusermaster.setActiveusercode(objusermaster.getResponse().getInformation().equals("Protocol") ? 2 : 1);
		List<Long> immutableNegativeValues = Arrays.asList(-3L, -22L);

		List<Lsprotocolorderstructure> lstdirpro;
		if (objusermaster.getResponse().getInformation().equals("Protocol")) {
			if (objusermaster.getUsernotify() == null) {
				lstdirpro = lsprotocolorderStructurerepository
						.findBySitemasterAndViewoptionAndDirectorycodeNotInOrCreatedbyAndViewoptionOrCreatedbyAndViewoptionOrderByDirectorycode(
								objusermaster.getLssitemaster(), 1, immutableNegativeValues, objusermaster, 2,
								objusermaster, 3);
			} else {
				lstdirpro = lsprotocolorderStructurerepository
						.findBySitemasterAndViewoptionAndDirectorycodeNotInOrCreatedbyAndViewoptionOrSitemasterAndViewoptionAndCreatedbyInOrderByDirectorycode(
								objusermaster.getLssitemaster(), 1, immutableNegativeValues, objusermaster, 2,
								objusermaster.getLssitemaster(), 3, objusermaster.getUsernotify());
			}
			lstdirpro.addAll(lsprotocolorderStructurerepository.findByDirectorycodeIn(immutableNegativeValues));
//			List<Lsprotocolorderstructure> lsprotocolorderstructure = new ObjectMapper().convertValue(lstdirpro, new TypeReference<Lsprotocolorderstructure>() {});
			List<Long> directoryCode_Protocol = lstdirpro.stream().map(Lsprotocolorderstructure::getDirectorycode)
					.collect(Collectors.toList());
			List<LSprotocolfolderfiles> lstfiles = lsprotocolfolderfilesRepository
					.findByDirectorycodeInAndFilenameLikeIgnoreCaseOrderByFolderfilecode(directoryCode_Protocol,
							Search_Key);
			List<LSprotocolfolderfiles> result = lstfiles.stream().map(path -> {
				List<Lsprotocolorderstructure> filteredFiles = lstdirpro.stream()
						.filter(file -> file.getDirectorycode().equals(path.getDirectorycode()))
						.collect(Collectors.toList());

				if (!filteredFiles.isEmpty()) {
					path.setFolderpath(filteredFiles.get(0).getPath());
//			                filteredFiles.get(0).setFolderpath(path.getPath());
				}

				return path;
			}).collect(Collectors.toList());
			Count_Directory_File = lsprotocolfolderfilesRepository
					.countByDirectorycodeInAndFilenameLikeIgnoreCaseOrderByFolderfilecode(directoryCode_Protocol,
							Search_Key);
			mapfolders.put("Files", result);
			mapfolders.put("Count_Directory_File", Count_Directory_File);

		} else {
			List<LSSheetOrderStructure> lstdir;

			if (objusermaster.getUsernotify() == null) {
				lstdir = lsSheetOrderStructureRepository
						.findBySitemasterAndViewoptionAndDirectorycodeNotInOrCreatedbyAndViewoptionOrCreatedbyAndViewoptionOrderByDirectorycode(
								objusermaster.getLssitemaster(), 1, immutableNegativeValues, objusermaster, 2,
								objusermaster, 3);
			} else {
				lstdir = lsSheetOrderStructureRepository
						.findBySitemasterAndViewoptionAndDirectorycodeNotInOrCreatedbyAndViewoptionOrSitemasterAndViewoptionAndCreatedbyInOrderByDirectorycode(
								objusermaster.getLssitemaster(), 1, immutableNegativeValues, objusermaster, 2,
								objusermaster.getLssitemaster(), 3, objusermaster.getUsernotify());
			}
			lstdir.addAll(lsSheetOrderStructureRepository.findByDirectorycodeIn(immutableNegativeValues));
			List<Long> directoryCode_Sheet = lstdir.stream().map(LSSheetOrderStructure::getDirectorycode)
					.collect(Collectors.toList());

			List<LSsheetfolderfiles> lstfiles = lssheetfolderfilesRepository
					.findByDirectorycodeInAndFilenameLikeIgnoreCaseOrderByFolderfilecode(directoryCode_Sheet,
							Search_Key);
			List<LSsheetfolderfiles> result = lstfiles.stream().map(path -> {
				List<LSSheetOrderStructure> filteredFiles = lstdir.stream()
						.filter(file -> file.getDirectorycode().equals(path.getDirectorycode()))
						.collect(Collectors.toList());

				if (!filteredFiles.isEmpty()) {
					path.setFolderpath(filteredFiles.get(0).getPath());
//			                filteredFiles.get(0).setFolderpath(path.getPath());
				}

				return path;
			}).collect(Collectors.toList());

			Count_Directory_File = lssheetfolderfilesRepository
					.countByDirectorycodeInAndFilenameLikeIgnoreCaseOrderByFolderfilecode(directoryCode_Sheet,
							Search_Key);
			mapfolders.put("Files", result);
			mapfolders.put("Count_Directory_File", Count_Directory_File);
		}
		return mapfolders;
	}

	public Map<String, Object> Getglobalsearchforlogbook(LSuserMaster objusermaster) {
		Pageable pageable = new PageRequest(objusermaster.getPagesize(), objusermaster.getPageperorder());
		String Search_Key = "%" + objusermaster.getToken() + "%"; // search key porpose kumu
		long count = 0;
		List<Lslogbooks> objreview;
		Map<String, Object> rtnobject = new HashMap<>();
		if (objusermaster.getLssitemaster().getSitecode() == 0) {
			objreview = lslogbooksRepository
					.findByLogbooknameLikeIgnoreCaseOrLogbookcategoryLikeIgnoreCaseOrLogbookidLikeIgnoreCaseOrderByLogbookcodeAsc(
							Search_Key, Search_Key, Search_Key, pageable);
			count = lslogbooksRepository
					.countByLogbooknameLikeIgnoreCaseOrLogbookcategoryLikeIgnoreCaseOrLogbookidLikeIgnoreCaseOrderByLogbookcodeAsc(
							Search_Key, Search_Key, Search_Key);
		} else {
			objreview = lslogbooksRepository
					.findBySitecodeAndLogbooknameLikeIgnoreCaseOrSitecodeAndLogbookcategoryLikeIgnoreCaseOrSitecodeAndLogbookidLikeIgnoreCaseOrderByLogbookcodeAsc(
							objusermaster.getLssitemaster().getSitecode(), Search_Key,
							objusermaster.getLssitemaster().getSitecode(), Search_Key,
							objusermaster.getLssitemaster().getSitecode(), Search_Key, pageable);
			count = lslogbooksRepository
					.countBySitecodeAndLogbooknameLikeIgnoreCaseOrSitecodeAndLogbookcategoryLikeIgnoreCaseOrSitecodeAndLogbookidLikeIgnoreCaseOrderByLogbookcodeAsc(
							objusermaster.getLssitemaster().getSitecode(), Search_Key,
							objusermaster.getLssitemaster().getSitecode(), Search_Key,
							objusermaster.getLssitemaster().getSitecode(), Search_Key);
		}
		rtnobject.put("Logbooks", objreview);
		rtnobject.put("Count", count);
		return rtnobject;
	}

	public Map<String, Object> Getglobalsearchforinventory(LSuserMaster objusermaster) {
		String Search_Key = "%" + objusermaster.getToken() + "%"; // search key porpose kumu
		long count = 0;
		List<Lslogbooks> objreview;
		Map<String, Object> rtnobject = new HashMap<>();
		List<ElnmaterialInventory> lstElnInventories = elnmaterialInventoryReppository
				.getNsitecodeAndSinventoryidLikeIgnoreCaseOrderByNmaterialinventorycodeDesc(
						objusermaster.getLssitemaster().getSitecode(), Search_Key,
						objusermaster.getPagesize() * objusermaster.getPageperorder(), objusermaster.getPageperorder());
		count = elnmaterialInventoryReppository
				.getcounrNsitecodeAndSinventoryidLikeIgnoreCaseOrderByNmaterialinventorycodeDesc(
						objusermaster.getLssitemaster().getSitecode(), Search_Key);
		rtnobject.put("Material", lstElnInventories);
		rtnobject.put("Count", count);
		return rtnobject;
	}

	public List<LsActiveWidgets> getActivewidgetsdata(LSuserMaster objusermaster) {
		System.out.println("Before Delete --------------------------");
		lsActiveWidgetsRepository.deleteCustomRows(50, objusermaster.getUsercode());
		System.out.println("After Delete --------------------------");
		List<LsActiveWidgets> rtnibj = lsActiveWidgetsRepository
				.findFirst50ByUserIdOrderByActivewidgetscodeDesc(objusermaster.getUsercode());
		System.out.println("After fetch --------------------------");
		return rtnibj;
	}

	public LsActiveWidgets Onsaveactivewidgetsdata(LsActiveWidgets activeWidgets) throws ParseException {
		activeWidgets.setActivedatatimestamp(commonfunction.getCurrentUtcTime());
		lsActiveWidgetsRepository.save(activeWidgets);
		return activeWidgets;
	}

	public List<LSprojectmaster> Getprojectsonuser(LSuserMaster objuser) {
		List<LSprojectmaster> lstprojectmaster = new ArrayList<LSprojectmaster>();
		if (objuser.getUsercode() != null) {

			List<LSuserteammapping> lstteammap = lsuserteammappingRepository
					.findByLsuserMasterAndTeamcodeNotNull(objuser);
			List<LSusersteam> lstteam = lsusersteamRepository.findByLsuserteammappingInAndLssitemaster(lstteammap,
					objuser.getLssitemaster());
			lstprojectmaster = lsprojectmasterRepository.findByLsusersteamInAndStatusOrderByProjectcodeDesc(lstteam, 1);

		}
		return lstprojectmaster;
	}

	public List<LSprojectmaster> Getprojectsoverdueonuser(LSuserMaster objuser) {
		List<LSprojectmaster> lstprojectmaster = new ArrayList<LSprojectmaster>();

		if (objuser.getUsercode() != null) {
			List<LSuserteammapping> lstteammap = lsuserteammappingRepository
					.findByLsuserMasterAndTeamcodeNotNull(objuser);
			List<LSusersteam> lstteam = lsusersteamRepository.findByLsuserteammappingInAndLssitemaster(lstteammap,
					objuser.getLssitemaster());

			lstprojectmaster = lsprojectmasterRepository.findByLsusersteamInAndStatusOrderByProjectcodeDesc(lstteam, 1);

			lstprojectmaster = lstprojectmaster.stream().filter(project -> "1".equals(project.getDuedate()))
					.collect(Collectors.toList());
		}

		return lstprojectmaster;
	}

	public List<LogilabOrdermastersh> Getorderonproject(LSprojectmaster objproject) throws Exception {
		return lslogilablimsorderdetailRepository.findByLsprojectmasterOrderByBatchcodeDesc(objproject);
	}

	public List<LogilabProtocolOrderssh> Getprotocolorderonproject(LSprojectmaster objproject) throws Exception {
		return LSlogilabprotocoldetailRepository.findByLsprojectmasterOrderByProtocolordercodeDesc(objproject);
	}

	public Map<String, Object> Getprojectscountonuser(LSuserMaster objuser) {
		Map<String, Object> rtnobject = new HashMap<>();
		if (objuser.getUsercode() != null) {

			List<LSuserteammapping> lstteammap = lsuserteammappingRepository
					.findByLsuserMasterAndTeamcodeNotNull(objuser);
			List<LSusersteam> lstteam = lsusersteamRepository.findByLsuserteammappingInAndLssitemaster(lstteammap,
					objuser.getLssitemaster());
			rtnobject.put("count", lsprojectmasterRepository.countByLsusersteamInAndStatus(lstteam, 1));
		}
		return rtnobject;
	}

	public List<LSprojectmaster> GetInitprojectsonuser(LSuserMaster objuser) throws ParseException {
		List<LSprojectmaster> lstprojectmaster = new ArrayList<LSprojectmaster>();

		if (objuser.getUsercode() != null) {
			Date currentdate = commonfunction.getCurrentUtcTime();

			List<LSuserteammapping> lstteammap = lsuserteammappingRepository
					.findByLsuserMasterAndTeamcodeNotNull(objuser);
			List<LSusersteam> lstteam = lsusersteamRepository.findByLsuserteammappingInAndLssitemaster(lstteammap,
					objuser.getLssitemaster());

			lstprojectmaster = lsprojectmasterRepository
					.findByLsusersteamInAndStatusAndStartdateGreaterThanOrderByProjectcodeDesc(lstteam, 1, currentdate);

			lstprojectmaster = lstprojectmaster.stream().filter(project -> "1".equals(project.getDuedate()))
					.collect(Collectors.toList());
		}

		return lstprojectmaster;
	}

	public List<LSprojectmaster> Getinprogressprojectsonuser(LSuserMaster objuser) throws ParseException {
		List<LSprojectmaster> lstprojectmaster = new ArrayList<LSprojectmaster>();
		if (objuser.getUsercode() != null) {
			Date currentdate = commonfunction.getCurrentUtcTime();

			List<LSuserteammapping> lstteammap = lsuserteammappingRepository
					.findByLsuserMasterAndTeamcodeNotNull(objuser);

			List<LSusersteam> lstteam = lsusersteamRepository.findByLsuserteammappingInAndLssitemaster(lstteammap,
					objuser.getLssitemaster());

			lstprojectmaster = lsprojectmasterRepository
					.findByLsusersteamInAndStatusAndStartdateBeforeAndEnddateAfterOrderByProjectcodeDesc(lstteam, 1,
							currentdate, currentdate);
			lstprojectmaster = lstprojectmaster.stream().filter(project -> "1".equals(project.getDuedate()))
					.collect(Collectors.toList());
		}
		return lstprojectmaster;
	}

	public List<LSprojectmaster> Getcomprojectsonuser(LSuserMaster objuser) throws ParseException {
		List<LSprojectmaster> lstprojectmaster = new ArrayList<LSprojectmaster>();
		if (objuser.getUsercode() != null) {
			Date currentdate = commonfunction.getCurrentUtcTime();
			List<LSuserteammapping> lstteammap = lsuserteammappingRepository
					.findByLsuserMasterAndTeamcodeNotNull(objuser);
			List<LSusersteam> lstteam = lsusersteamRepository.findByLsuserteammappingInAndLssitemaster(lstteammap,
					objuser.getLssitemaster());
			lstprojectmaster = lsprojectmasterRepository
					.findByLsusersteamInAndStatusAndEnddateLessThanOrderByProjectcodeDesc(lstteam, 1, currentdate);
			lstprojectmaster = lstprojectmaster.stream().filter(project -> "1".equals(project.getDuedate()))
					.collect(Collectors.toList());

		}
		return lstprojectmaster;
	}

	public List<LSprojectmaster> GetOverdueprojectsonuser(LSuserMaster objuser) throws ParseException {
		List<LSprojectmaster> lstprojectmaster = new ArrayList<LSprojectmaster>();
		List<LSprojectmaster> lstrtnprojectmaster = new ArrayList<LSprojectmaster>();
		List<LSlogilablimsorderdetail> lstorder = new ArrayList<LSlogilablimsorderdetail>();
		if (objuser.getUsercode() != null) {
			Date currentdate = commonfunction.getCurrentUtcTime();

			List<LSuserteammapping> lstteammap = lsuserteammappingRepository
					.findByLsuserMasterAndTeamcodeNotNull(objuser);
			List<LSusersteam> lstteam = lsusersteamRepository.findByLsuserteammappingInAndLssitemaster(lstteammap,
					objuser.getLssitemaster());

			lstprojectmaster = lsprojectmasterRepository
					.findByLsusersteamInAndStatusAndEnddateLessThanOrderByProjectcodeDesc(lstteam, 1, currentdate);
			lstorder = lslogilablimsorderdetailRepository.findByOrderflagAndLsprojectmasterIn("R", lstprojectmaster);
			if (lstorder != null) {
				lstrtnprojectmaster = lstorder.stream().map(LSlogilablimsorderdetail::getLsprojectmaster)
						.collect(Collectors.toList());
			}
			lstprojectmaster = lstprojectmaster.stream().filter(project -> "1".equals(project.getDuedate()))
					.collect(Collectors.toList());
		}

		return lstrtnprojectmaster;
	}

	public Map<String, Object> Getintprojectscountonuser(LSuserMaster objuser) throws ParseException {
		Map<String, Object> rtnobject = new HashMap<>();
		if (objuser.getUsercode() != null) {
			Date currentdate = commonfunction.getCurrentUtcTime();
			List<LSuserteammapping> lstteammap = lsuserteammappingRepository
					.findByLsuserMasterAndTeamcodeNotNull(objuser);
			List<LSusersteam> lstteam = lsusersteamRepository.findByLsuserteammappingInAndLssitemaster(lstteammap,
					objuser.getLssitemaster());
			rtnobject.put("count", lsprojectmasterRepository
					.countByLsusersteamInAndStatusAndStartdateGreaterThan(lstteam, 1, currentdate));
		}
		return rtnobject;
	}

	public Map<String, Object> Getinprojectscountonuser(LSuserMaster objuser) throws ParseException {
		Map<String, Object> rtnobject = new HashMap<>();
		if (objuser.getUsercode() != null) {
			Date currentdate = commonfunction.getCurrentUtcTime();
			List<LSuserteammapping> lstteammap = lsuserteammappingRepository
					.findByLsuserMasterAndTeamcodeNotNull(objuser);
			List<LSusersteam> lstteam = lsusersteamRepository.findByLsuserteammappingInAndLssitemaster(lstteammap,
					objuser.getLssitemaster());
			rtnobject.put("count",
					lsprojectmasterRepository.countByLsusersteamInAndStatusAndStartdateBeforeAndEnddateAfter(lstteam, 1,
							currentdate, currentdate));
		}
		return rtnobject;
	}

	public Map<String, Object> Getcomprojectscountonuser(LSuserMaster objuser) throws ParseException {
		Map<String, Object> rtnobject = new HashMap<>();
		if (objuser.getUsercode() != null) {
			Date currentdate = commonfunction.getCurrentUtcTime();
			List<LSuserteammapping> lstteammap = lsuserteammappingRepository
					.findByLsuserMasterAndTeamcodeNotNull(objuser);
			List<LSusersteam> lstteam = lsusersteamRepository.findByLsuserteammappingInAndLssitemaster(lstteammap,
					objuser.getLssitemaster());
			rtnobject.put("count",
					lsprojectmasterRepository.countByLsusersteamInAndStatusAndEnddateLessThan(lstteam, 1, currentdate));
		}
		return rtnobject;
	}

	public Map<String, Object> Getoverdueprojectscountonuser(LSuserMaster objuser) throws ParseException {
		Map<String, Object> rtnobject = new HashMap<>();

		if (objuser.getUsercode() != null) {
			Date currentdate = commonfunction.getCurrentUtcTime();
			List<LSuserteammapping> lstteammap = lsuserteammappingRepository
					.findByLsuserMasterAndTeamcodeNotNull(objuser);
			List<LSusersteam> lstteam = lsusersteamRepository.findByLsuserteammappingInAndLssitemaster(lstteammap,
					objuser.getLssitemaster());
			List<LSprojectmaster> lstprojectmaster = new ArrayList<LSprojectmaster>();
			lstprojectmaster = lsprojectmasterRepository
					.findByLsusersteamInAndStatusAndEnddateLessThanOrderByProjectcodeDesc(lstteam, 1, currentdate);
			rtnobject.put("count",
					lslogilablimsorderdetailRepository.countByOrderflagAndLsprojectmasterIn("R", lstprojectmaster));
		}
		return rtnobject;
	}

	public Map<String, Object> Getprojectsoverduecountonuser(LSuserMaster objuser) throws ParseException {
		Map<String, Object> rtnobject = new HashMap<>();
		List<LSprojectmaster> lstprojectmaster = new ArrayList<>();

		if (objuser.getUsercode() != null) {
			Date currentdate = commonfunction.getCurrentUtcTime();

			List<LSuserteammapping> lstteammap = lsuserteammappingRepository
					.findByLsuserMasterAndTeamcodeNotNull(objuser);

			List<LSusersteam> lstteam = lsusersteamRepository.findByLsuserteammappingInAndLssitemaster(lstteammap,
					objuser.getLssitemaster());

			long count = lsprojectmasterRepository.countByLsusersteamInAndStatusAndDuedate(lstteam, 1, "1");
			rtnobject.put("count", count);

			lstprojectmaster = lsprojectmasterRepository.findByLsusersteamInAndStatusAndDuedate(lstteam, 1, "1");

			lstprojectmaster = lstprojectmaster.stream().filter(project -> "1".equals(project.getDuedate()))
					.collect(Collectors.toList());
			rtnobject.put("projects", lstprojectmaster);
		}

		return rtnobject;
	}

}
