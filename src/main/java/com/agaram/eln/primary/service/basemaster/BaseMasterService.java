package com.agaram.eln.primary.service.basemaster;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Service;

import com.agaram.eln.primary.fetchmodel.getmasters.Samplemaster;
import com.agaram.eln.primary.fetchmodel.getmasters.Testmaster;
import com.agaram.eln.primary.fetchmodel.gettemplate.Sheettemplateget;
import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.instrumentDetails.LSlogilablimsorderdetail;
import com.agaram.eln.primary.model.instrumentDetails.Lselninstrumentfields;
import com.agaram.eln.primary.model.instrumentDetails.Lselninstrumentmaster;
import com.agaram.eln.primary.model.inventory.LSequipmentmap;
import com.agaram.eln.primary.model.inventory.LSinstrument;
import com.agaram.eln.primary.model.inventory.LSmaterial;
import com.agaram.eln.primary.model.inventory.LSmaterialmap;
import com.agaram.eln.primary.model.masters.LSlogbooksampleupdates;
import com.agaram.eln.primary.model.masters.Lsrepositories;
import com.agaram.eln.primary.model.masters.Lsrepositoriesdata;
import com.agaram.eln.primary.model.material.Unit;
import com.agaram.eln.primary.model.protocols.LSprotocolmaster;
import com.agaram.eln.primary.model.protocols.LSprotocolmastertest;
import com.agaram.eln.primary.model.sheetManipulation.LSfiletest;
import com.agaram.eln.primary.model.sheetManipulation.LSsamplemaster;
import com.agaram.eln.primary.model.sheetManipulation.LStestmaster;
import com.agaram.eln.primary.model.sheetManipulation.LStestmasterlocal;
import com.agaram.eln.primary.model.usermanagement.LSnotification;
import com.agaram.eln.primary.model.usermanagement.LSprojectmaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.model.usermanagement.LSusersteam;
import com.agaram.eln.primary.model.usermanagement.LSuserteammapping;
//import com.agaram.eln.primary.repository.instrumentDetails.LSinstrumentsRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LSlogilablimsorderdetailRepository;
//import com.agaram.eln.primary.repository.instrumentDetails.LsMethodFieldsRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LselninstfieldmappingRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LselninstrumentfieldsRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LselninstrumentmappingRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LselninstrumentmasterRepository;
import com.agaram.eln.primary.repository.inventory.LSequipmentmapRepository;
import com.agaram.eln.primary.repository.inventory.LSmaterialmapRepository;
import com.agaram.eln.primary.repository.masters.LSlogbooksampleupdatesRepository;
import com.agaram.eln.primary.repository.masters.LsrepositoriesRepository;
import com.agaram.eln.primary.repository.masters.LsrepositoriesdataRepository;
import com.agaram.eln.primary.repository.material.UnitRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LSsamplemasterRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LStestmasterRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LStestmasterlocalRepository;
import com.agaram.eln.primary.repository.usermanagement.LSnotificationRepository;
import com.agaram.eln.primary.repository.usermanagement.LSprojectmasterRepository;
import com.agaram.eln.primary.repository.usermanagement.LSuserMasterRepository;
import com.agaram.eln.primary.service.protocol.ProtocolService;
import com.agaram.eln.primary.service.sheetManipulation.FileService;

@Service
@EnableJpaRepositories(basePackageClasses = LSsamplemasterRepository.class)
public class BaseMasterService {

	/**
	 * For Masters Repository
	 */
//	@Autowired
//	private LSusersteamRepository LSusersteamRepository;
//	@Autowired
//	private LSuserteammappingRepository LSuserteammappingRepository;
	@Autowired
	private LSnotificationRepository LSnotificationRepository;
//	@Autowired
//	private LsMethodFieldsRepository lsMethodFieldsRepository;
	@Autowired
	private LStestmasterlocalRepository lStestmasterlocalRepository;
	@Autowired
	private LSsamplemasterRepository lSsamplemasterRepository;
	@Autowired
	private LSprojectmasterRepository lSprojectmasterRepository;
	@Autowired
	private LSuserMasterRepository lsuserMasterRepository;

	@Autowired

	private LSmaterialmapRepository lSmaterialmapRepository;
	@Autowired
	private LSequipmentmapRepository lSequipmentmapRepository;

	@Autowired
	private LStestmasterRepository lstestmasterRepository;

	@Autowired
	ProtocolService ProtocolMasterService;
	@Autowired
	private UnitRepository unitRepository;

	@Autowired
	private LselninstrumentfieldsRepository lselninstrumentfieldsRepository;

	@Autowired
	private LselninstrumentmasterRepository lselninstrumentmasterRepository;

//	@Autowired
//	private LSinstrumentsRepository lSinstrumentsRepository;

	@Autowired
	private LselninstrumentmappingRepository lselnInstrumentmappingRepository;

	@Autowired
	private LselninstfieldmappingRepository lselninstfieldmappingRepository;

	@Autowired
	private LSlogilablimsorderdetailRepository LSlogilablimsorderdetailRepository;

	@Autowired
	private FileService fileService;

	@Autowired
	private LsrepositoriesRepository LsrepositoriesRepository;

	@Autowired
	private LsrepositoriesdataRepository LsrepositoriesdataRepository;

	@Autowired
	private LSlogbooksampleupdatesRepository LSlogbooksampleupdatesRepository;

	@SuppressWarnings("unused")
	private String ModuleName = "Base Master";

	public List<Testmaster> getTestmaster(LSuserMaster objClass) {

		return lStestmasterlocalRepository.findBystatusAndLssitemasterOrderByTestcodeDesc(1,
				objClass.getLssitemaster());
	}

	public Map<String, Object> getTestwithsheet(LSuserMaster objClass) {
		Map<String, Object> map = new HashMap<String, Object>();

		List<Testmaster> lstTest = lStestmasterlocalRepository.findBystatusAndLssitemaster(1,
				objClass.getLssitemaster());

		map.put("Test", lstTest);

		LSfiletest objTest = new LSfiletest();

		List<Sheettemplateget> lstSheet = new ArrayList<Sheettemplateget>();

		List<LSprotocolmaster> lstP = new ArrayList<LSprotocolmaster>();

		if (lstTest != null && lstTest.size() > 0) {

			objTest.setTestcode(lstTest.get(0).getTestcode());
			objTest.setTesttype(1);
			objTest.setIsmultitenant(objClass.getIsmultitenant());
			objTest.setObjLoggeduser(objClass);

			lstSheet = fileService.GetfilesOnTestcode(objTest);

			LSprotocolmastertest obj1 = new LSprotocolmastertest();

			obj1.setTestcode(lstTest.get(0).getTestcode());
			obj1.setTesttype(1);
			obj1.setObjLoggeduser(objClass);

			lstP = ProtocolMasterService.getProtocolOnTestcode(obj1);

		}
		map.put("Protocol", lstP);
		map.put("Sheet", lstSheet);

		return map;
	}

	public List<LStestmaster> getLimsTestMaster(LSuserMaster objClass) {

		return lstestmasterRepository.findAll();
	}

	public List<Samplemaster> getsamplemaster(LSuserMaster objClass) {

		return lSsamplemasterRepository.findBystatusAndLssitemasterOrderBySamplecodeDesc(1, objClass.getLssitemaster());
	}

	public List<Samplemaster> getAllSampleMaster(LSuserMaster objClass) {

		return lSsamplemasterRepository.findByLssitemasterOrderBySamplecodeDesc(objClass.getLssitemaster());
	}

	public List<LSprojectmaster> getProjectmaster(LSuserMaster objClass) {

//		List<LSprojectmaster> projectlist = lSprojectmasterRepository
//				.findByLssitemasterAndStatusOrderByProjectcodeDesc(objClass.getLssitemaster(), 1);

		List<LSprojectmaster> projectlist = lSprojectmasterRepository
				.findByLssitemasterOrderByProjectcodeDesc(objClass.getLssitemaster());
		return projectlist;
	}

	public LStestmasterlocal InsertupdateTest(LStestmasterlocal objClass) {

		objClass.setResponse(new Response());

//		else if (objClass.getTestcode() != null
//				//&& lStestmasterlocalRepository.findByTestnameIgnoreCaseAndStatusAndTestcodeNotAndLssitemaster(
//				&& lStestmasterlocalRepository.findByTestnameIgnoreCaseAndTaskcategoryIgnoreCaseAndStatusAndTestcodeNotAndLssitemaster(
//						objClass.getTestname(),objClass.getTaskcategory(), 1, objClass.getTestcode(), objClass.getLssitemaster()) != null) {	
//		
//			objClass.getResponse().setStatus(false);
//			objClass.getResponse().setInformation("ID_EXIST");
//			
//
//			return objClass;
//		}

		if (objClass.getStatus() == -1 && objClass.getTestcode() != null) {

			List<LSlogilablimsorderdetail> objOrderLst = LSlogilablimsorderdetailRepository
					.findByTestcode(objClass.getTestcode());

			if (!objOrderLst.isEmpty()) {
				objClass.getResponse().setStatus(false);
				objClass.getResponse().setInformation("IDS_SAMPLETRANSACTION");

				return objClass;
			}

		} else if (objClass.getTestcode() == null
				&& lStestmasterlocalRepository.findByTestnameIgnoreCaseAndLssitemaster(
						objClass.getTestname().trim(), objClass.getLssitemaster()) .size()>0) {
			objClass.getResponse().setStatus(false);
			objClass.getResponse().setInformation("ID_EXIST");

			return objClass;
		} else if (objClass.getTestcode() != null
				&& lStestmasterlocalRepository.findBytestcodeNotAndTestnameIgnoreCaseAndLssitemaster(
						objClass.getTestcode(), objClass.getTestname().trim(), objClass.getLssitemaster()).size()>0 ) {
			objClass.getResponse().setStatus(false);
			objClass.getResponse().setInformation("ID_EXIST");

			return objClass;
		}

		lStestmasterlocalRepository.save(objClass);

		if (objClass.getLSmaterial() != null) {
			for (LSmaterial objMat : objClass.getLSmaterial()) {
				LSmaterialmap objmap = new LSmaterialmap();
				objmap.setLSmaterial(objMat);
				objmap.setTestcode(objClass.getTestcode());
				lSmaterialmapRepository.save(objmap);
			}
		}

		if (objClass.getLSinstrument() != null) {
			for (LSinstrument objIns : objClass.getLSinstrument()) {
				LSequipmentmap objmap = new LSequipmentmap();
				objmap.setLSinstrument(objIns);
				objmap.setTestcode(objClass.getTestcode());
				lSequipmentmapRepository.save(objmap);
			}
		}

		objClass.getResponse().setStatus(true);
		objClass.getResponse().setInformation("");

		return objClass;
	}

	public LSsamplemaster InsertupdateSample(LSsamplemaster objClass) {

		objClass.setResponse(new Response());

		if (objClass.getStatus() == -1 && objClass.getSamplecode() != null) {

			List<LSlogilablimsorderdetail> objOrderLst = LSlogilablimsorderdetailRepository
					.findByLssamplemaster(objClass);

			if (!objOrderLst.isEmpty()) {
				objClass.getResponse().setStatus(false);
				objClass.getResponse().setInformation("IDS_SAMPLETRANSACTION");

				return objClass;
			}
		} else if (objClass.getSamplecode() == null
				&& lSsamplemasterRepository.findBySamplenameIgnoreCaseAndLssitemaster(
						objClass.getSamplename().trim(), objClass.getLssitemaster()) .size()>0) {
			objClass.getResponse().setStatus(false);
			objClass.getResponse().setInformation("ID_EXIST");

			return objClass;
		} else if (objClass.getSamplecode() != null && lSsamplemasterRepository
				.findBySamplenameIgnoreCaseAndSamplecodeNotAndLssitemaster(objClass.getSamplename().trim(),
						objClass.getSamplecode(), objClass.getLssitemaster()).size()>0 ) {
			objClass.getResponse().setStatus(false);
			objClass.getResponse().setInformation("ID_EXIST");

			return objClass;
		}

		lSsamplemasterRepository.save(objClass);
		objClass.getResponse().setStatus(true);
		objClass.getResponse().setInformation("");

		return objClass;
	}

	public LSprojectmaster InsertupdateProject(LSprojectmaster objClass) {

		objClass.setResponse(new Response());

		if (objClass.getProjectcode() == null
				&& lSprojectmasterRepository.findByProjectnameIgnoreCaseAndLssitemaster(objClass.getProjectname(),
						objClass.getLssitemaster()) != null) {
			objClass.getResponse().setStatus(false);
			objClass.getResponse().setInformation("ID_EXIST");
			return objClass;
		}

		else if (objClass.getStatus() == -1
				&& LSlogilablimsorderdetailRepository.findByOrderflagAndLsprojectmaster("N", objClass).size() != 0) {
			objClass.getResponse().setStatus(false);
			objClass.getResponse().setInformation("IDS_PROJECTPROGRESS");
			return objClass;
		}

		else if (objClass.getProjectcode() != null
				&& lSprojectmasterRepository.findByProjectnameIgnoreCaseAndStatusAndProjectcodeNotAndLssitemaster(
						objClass.getProjectname(), 1, objClass.getProjectcode(), objClass.getLssitemaster()) != null) {
			objClass.getResponse().setStatus(false);
			objClass.getResponse().setInformation("ID_EXIST");
			return objClass;
		}

		else if (objClass.getProjectcode() != null
				&& !(LSlogilablimsorderdetailRepository.findByLsprojectmaster(objClass).isEmpty())) {
			LSprojectmaster objLSprojectmaster = lSprojectmasterRepository.findOne(objClass.getProjectcode());
			if (objLSprojectmaster.getLsusersteam() != objClass.getLsusersteam()) {
				objClass.getResponse().setStatus(false);
				objClass.getResponse().setInformation("IDS_PROJECTPROGRESS");
				return objClass;
			}
		}

		lSprojectmasterRepository.save(objClass);
		objClass.getResponse().setStatus(true);
		objClass.getResponse().setInformation("");
		updatenotificationforproject(objClass);
		return objClass;
	}

	@SuppressWarnings("unused")
	private void updatenotificationforproject(LSprojectmaster objClass) {
		String Details = "";
		String Notifiction = "PROJECTCREATED";

		List<LSnotification> lstnotifications = new ArrayList<LSnotification>();
		LSuserMaster createby = lsuserMasterRepository.findByusercode(objClass.getModifiedby().getUsercode());
		List<LSuserteammapping> objteam = new ArrayList<LSuserteammapping>();
		LSusersteam objteam1 = new LSusersteam();

		for (int i = 0; i < objClass.getLsusersteam().getLsuserteammapping().size(); i++) {
			LSnotification notify = new LSnotification();
			if (createby.getUsercode() != objClass.getLsusersteam().getLsuserteammapping().get(i).getLsuserMaster()
					.getUsercode()) {
				Details = "{\"teamname\":\"" + objClass.getTeamname() + "\", \"projectname\":\""
						+ objClass.getProjectname() + "\"}";

				notify.setNotifationfrom(objClass.getModifiedby());
				notify.setNotifationto(objClass.getLsusersteam().getLsuserteammapping().get(i).getLsuserMaster());
				notify.setNotificationdate(new Date());
				notify.setNotification(Notifiction);
				notify.setNotificationdetils(Details);
				notify.setIsnewnotification(1);
				notify.setNotificationpath("/projectmaster");
				notify.setNotificationfor(1);

				lstnotifications.add(notify);
				LSnotificationRepository.save(lstnotifications);

			}
		}

	}

	public Map<String, Object> GetMastersforTestMaster(LSuserMaster objuser) {
		Map<String, Object> mapOrders = new HashMap<String, Object>();

		List<Testmaster> lstTest = lStestmasterlocalRepository
				.findByLssitemasterOrderByTestcodeDesc(objuser.getLssitemaster());
		mapOrders.put("test", lstTest);

//		mapOrders.put("test", getTestmaster(objuser));
		if (objuser.getObjsilentaudit() != null) {
			objuser.getObjsilentaudit().setTableName("LStestmaster");
//			lscfttransactionRepository.save(objuser.getObjsilentaudit());
		}
		return mapOrders;

	}

	public Lselninstrumentmaster InsertupdateInstrument(Lselninstrumentmaster objClass) {

		objClass.setResponse(new Response());

		if (objClass.getInstrumentcode() == null && lselninstrumentmasterRepository
				.findByInstrumentnameIgnoreCaseAndStatus(objClass.getInstrumentname(), 1) != null) {
			objClass.getResponse().setStatus(false);
			objClass.getResponse().setInformation("ID_INSTRUMENTALREADYEXIST");
			if (objClass.getObjsilentaudit() != null) {
				objClass.getObjsilentaudit().setActions("Warning");
				objClass.getObjsilentaudit().setComments(
						objClass.getModifiedby().getUsername() + " " + "made attempt to create existing Instrument");
				objClass.getObjsilentaudit().setTableName("Lselninstrumentmaster");
//				lscfttransactionRepository.save(objClass.getObjsilentaudit());
			}
//			manual audit
			if (objClass.getObjuser() != null) {
				objClass.getObjmanualaudit().setActions("Warning");
				objClass.getObjmanualaudit().setTableName("Lselninstrumentmaster");
				objClass.getObjmanualaudit().setComments(objClass.getObjuser().getComments());
//				lscfttransactionRepository.save(objClass.getObjmanualaudit());
			}
			return objClass;
		} else if (objClass.getInstrumentcode() != null
				&& lselninstrumentmasterRepository.findByInstrumentnameIgnoreCaseAndStatusAndInstrumentcodeNot(
						objClass.getInstrumentname(), 1, objClass.getInstrumentcode()) != null) {
			objClass.getResponse().setStatus(false);
			objClass.getResponse().setInformation("ID_INSTRUMENTALREADYEXIST");
			if (objClass.getObjsilentaudit() != null) {
				objClass.getObjsilentaudit().setActions("Warning");
				objClass.getObjsilentaudit().setComments(
						objClass.getModifiedby().getUsername() + " " + "made attempt to create existing Instrument");
				objClass.getObjsilentaudit().setTableName("Lselninstrumentmaster");
//				lscfttransactionRepository.save(objClass.getObjsilentaudit());
			}
//			manual audit
			if (objClass.getObjuser() != null) {
				objClass.getObjmanualaudit().setActions("Warning");
				objClass.getObjmanualaudit().setTableName("Lselninstrumentmaster");
				objClass.getObjmanualaudit().setComments(objClass.getObjuser().getComments());
//				lscfttransactionRepository.save(objClass.getObjmanualaudit());
			}
			return objClass;
		}

		lselnInstrumentmappingRepository.save(objClass.getLselnInstrumentmapping());
		List<Lselninstrumentfields> objfields = objClass.getLsfields();
		for (Lselninstrumentfields lselninstrumentfields : objfields) {
			lselninstfieldmappingRepository.save(lselninstrumentfields.getLselninstfieldmapping());
			lselninstrumentfieldsRepository.save(lselninstrumentfields);
		}

		objClass.getResponse().setStatus(true);

		Lselninstrumentmaster objinstrument = lselninstrumentmasterRepository.save(objClass);
		objinstrument.setResponse(new Response());
		objinstrument.getResponse().setStatus(true);

//		silent audit
		if (objClass.getObjsilentaudit() != null) {
			objClass.getObjsilentaudit().setTableName("Lselninstrumentmaster");
//			lscfttransactionRepository.save(objClass.getObjsilentaudit());
		}

		// Manual Audit
		if (objClass.getObjuser() != null) {
			objClass.getObjmanualaudit().setComments(objClass.getObjuser().getComments());
			objClass.getObjmanualaudit().setTableName("Lselninstrumentmaster");
//			lscfttransactionRepository.save(objClass.getObjmanualaudit());
		}
		return objinstrument;
	}

	public Map<String, Object> GetInstrument(Lselninstrumentmaster objClass) {
		Map<String, Object> obj = new HashMap<>();
		List<String> lsInst = new ArrayList<String>();
		lsInst.add("INST000");
		lsInst.add("LPRO");
//		obj.put("elninstrument",lselninstrumentmasterRepository.findBystatusOrderByInstrumentcodeDesc(1));
		obj.put("elninstrument", lselninstrumentmasterRepository
				.findBylssitemasterAndStatusOrderByInstrumentcodeDesc(objClass.getLssitemaster(), 1));
//		obj.put("instrument", lSinstrumentsRepository.findAll());
//		obj.put("instrumentfields", lsMethodFieldsRepository.findByinstrumentidNotIn(lsInst));

//		silent audit
		if (objClass.getObjsilentaudit() != null) {
			objClass.getObjsilentaudit().setTableName("Lselninstrumentmaster");
//			lscfttransactionRepository.save(objClass.getObjsilentaudit());
		}
		return obj;
	}

	public LStestmaster GetTestonID(LStestmaster objtest) {
		return lstestmasterRepository.findByntestcode(objtest.getNtestcode());
	}

	public List<Unit> getUnitmaster(LSuserMaster objClass) {

		return unitRepository.findByNstatusOrderByNunitcodeDesc(1);
	}

	public Unit InsertupdateUnit(Unit objClass) {

		if (objClass.getNunitcode() == -1) {

			objClass = unitRepository.save(objClass);

		} else {
			Unit objUnit = unitRepository.findOne(objClass.getNunitcode());

			objUnit.setSunitname(objClass.getSunitname());
			objUnit.setSdescription(objClass.getSdescription());

			objClass = unitRepository.save(objUnit);
		}

		return objClass;
	}

	public Map<String, Object> getLsrepositoriesLst(Map<String, Object> argMap) {
		Map<String, Object> mapObj = new HashMap<String, Object>();
		LScfttransaction LScfttransactionobj = new LScfttransaction();
		if (argMap.containsKey("objsilentaudit")) {
			LScfttransactionobj = new ObjectMapper().convertValue(argMap.get("objsilentaudit"),
					new TypeReference<LScfttransaction>() {
					});
			List<Lsrepositories> LsrepositoriesLst = LsrepositoriesRepository
					.findBySitecodeOrderByRepositorycodeAsc(LScfttransactionobj.getLssitemaster());
			if (LsrepositoriesLst.size() > 0) {
				mapObj.put("LsrepositoriesLst", LsrepositoriesLst);
//				List<Lsrepositoriesdata> LsrepositoriesdataLst=LsrepositoriesdataRepository.findByRepositorycodeAndSitecodeOrderByRepositorydatacodeDesc(LsrepositoriesLst.get(0).getRepositorycode(), LScfttransactionobj.getLssitemaster());
				Map<String, Object> temp = new HashMap<>();
				temp.put("objsilentaudit", LScfttransactionobj);
				temp.put("LsrepositoriesObj", LsrepositoriesLst.get(0));
				mapObj.putAll(getLsrepositoriesDataLst(temp));
			} else {
				mapObj.put("LsrepositoriesLst", LsrepositoriesLst);
				mapObj.put("LsrepositoriesdataLst", new ArrayList<>());
			}

		}
		return mapObj;
	}

	public Map<String, Object> getLsrepositoriesDataLst(Map<String, Object> argMap) {
		Map<String, Object> mapObj = new HashMap<String, Object>();
		LScfttransaction LScfttransactionobj = new LScfttransaction();
		if (argMap.containsKey("objsilentaudit")) {
			LScfttransactionobj = new ObjectMapper().convertValue(argMap.get("objsilentaudit"),
					new TypeReference<LScfttransaction>() {
					});
			Lsrepositories LsrepositoriesLst = new ObjectMapper().convertValue(argMap.get("LsrepositoriesObj"),
					new TypeReference<Lsrepositories>() {
					});
			if (LsrepositoriesLst != null) {
				if (LsrepositoriesLst.getRepositorycode() > 0) {
					List<Lsrepositoriesdata> LsrepositoriesdataLst = LsrepositoriesdataRepository
							.findByRepositorycodeAndSitecodeAndItemstatusOrderByRepositorydatacodeDesc(
									LsrepositoriesLst.getRepositorycode(), LScfttransactionobj.getLssitemaster(), 1);
					mapObj.put("LsrepositoriesdataLst", LsrepositoriesdataLst);
				} else {
					mapObj.put("LsrepositoriesdataLst", new ArrayList<>());
				}
			} else {
				List<Lsrepositoriesdata> LsrepositoriesdataLst = LsrepositoriesdataRepository
						.findByRepositoryitemname(argMap.get("repositoryitemname"));
				mapObj.put("LsrepositoriesdataLst", LsrepositoriesdataLst);
			}

		}
		return mapObj;
	}

	@SuppressWarnings("unused")
	public List<Lsrepositoriesdata> reducecunsumablefield(Lsrepositoriesdata[] lsrepositoriesdata) {
		List<Lsrepositoriesdata> lsrepositoriesdataobj = Arrays.asList(lsrepositoriesdata);
		for (Lsrepositoriesdata data : lsrepositoriesdataobj) {
			LsrepositoriesdataRepository.save(lsrepositoriesdataobj);
		}
		return lsrepositoriesdataobj;
	}

	public Map<String, Object> logbooksampleupdates(LSlogbooksampleupdates lslogbooksampleupdates) {
		Map<String, Object> obj = new HashMap<String, Object>();
		LSlogbooksampleupdatesRepository.save(lslogbooksampleupdates);
		List<LSlogbooksampleupdates> lslogbooksamplelist = LSlogbooksampleupdatesRepository
				.findByLogbookcodeAndIndexofIsNotNullAndStatus(lslogbooksampleupdates.getLogbookcode(), 1);
		obj.put("lslogbooksampleupdates", lslogbooksampleupdates);
		obj.put("lslogbooksamplelist", lslogbooksamplelist);
		return obj;
	}

	public List<LSprojectmaster> getProjectmasteronproject(LSuserMaster objClass) {

//		List<LSprojectmaster> projectlist = lSprojectmasterRepository
//				.findByLssitemasterAndStatusOrderByProjectcodeDesc(objClass.getLssitemaster(), 1);

		List<LSprojectmaster> projectlist = lSprojectmasterRepository
				.findByLssitemasterAndStatusOrderByProjectcodeDesc(objClass.getLssitemaster(),1);
		return projectlist;
	}

}
