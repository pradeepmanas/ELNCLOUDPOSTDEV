package com.agaram.eln.primary.service.cfr;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Service;

import com.agaram.eln.config.AESEncryption;
import com.agaram.eln.primary.model.cfr.LSaudittrailconfigmaster;
import com.agaram.eln.primary.model.cfr.LSaudittrailconfiguration;
import com.agaram.eln.primary.model.cfr.LScfrreasons;
import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.cfr.LSpreferences;
import com.agaram.eln.primary.model.cfr.LSreviewdetails;
import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.model.usermanagement.LoggedUser;
import com.agaram.eln.primary.repository.cfr.LSaudittrailconfigmasterRepository;
import com.agaram.eln.primary.repository.cfr.LSaudittrailconfigurationRepository;
import com.agaram.eln.primary.repository.cfr.LScfrreasonsRepository;
import com.agaram.eln.primary.repository.cfr.LScfttransactionRepository;
import com.agaram.eln.primary.repository.cfr.LSpreferencesRepository;
import com.agaram.eln.primary.repository.cfr.LSreviewdetailsRepository;
import com.agaram.eln.primary.repository.usermanagement.LSSiteMasterRepository;
import com.agaram.eln.primary.repository.usermanagement.LSuserMasterRepository;
import com.agaram.eln.primary.repository.usermanagement.LSusergrouprightsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@EnableJpaRepositories(basePackageClasses = LScfrreasonsRepository.class)
public class AuditService {
	@PersistenceContext
	private EntityManager em;

	@SuppressWarnings("unused")
	@Autowired
	private Environment env;
	@Autowired
	private LScfrreasonsRepository LScfrreasonsRepository;
	@Autowired
	private LSuserMasterRepository LSuserMasterRepository;
	@Autowired
	private LSaudittrailconfigurationRepository LSaudittrailconfigurationRepository;
	@Autowired
	private LSaudittrailconfigmasterRepository LSaudittrailconfigmasterRepository;
	@Autowired
	private LScfttransactionRepository lscfttransactionRepository;
	@Autowired
	private LSuserMasterRepository lSuserMasterRepository;
	@Autowired
	private LSreviewdetailsRepository LSreviewdetailsRepository;
	@Autowired
	private LSusergrouprightsRepository LSusergrouprightsRepository;

	@Autowired
	private LSSiteMasterRepository lSSiteMasterRepository;
	@SuppressWarnings("unused")
	@Autowired
	private LSpreferencesRepository LSpreferencesRepository;

	public List<LScfrreasons> getreasons(Map<String, Object> objMap) {
		List<LScfrreasons> result = new ArrayList<LScfrreasons>();
		LScfrreasonsRepository.findAll().forEach(result::add);
		return result;
	}

	public List<LSuserMaster> CFRTranUsername() {
		List<LSuserMaster> result = new ArrayList<LSuserMaster>();
		LSuserMasterRepository.findAll().forEach(result::add);
		return result;
	}

	public List<String> CFRTranModuleName() {
		List<String> result = LSusergrouprightsRepository.findDistinctmodulename();

		LSpreferences objpref = LSpreferencesRepository.findByTasksettingsAndValuesettings("ELNparser", "0");

		if (objpref != null) {

			result.removeIf(x -> x.contains("Parser"));

			return result;
		} else {
			return result;
		}
	}

	public LScfrreasons InsertupdateReasons(LScfrreasons objClass) {

		objClass.setResponse(new Response());
		if (objClass.getReasoncode() == null
				&& LScfrreasonsRepository.findByCommentsIgnoreCase(objClass.getComments()) != null) {
			objClass.getResponse().setStatus(false);
			objClass.getResponse().setInformation("ID_CFREXIST");

			return objClass;

		} else if (objClass.getReasoncode() != null && objClass.getStatus() != null
				&& LScfrreasonsRepository.findByComments(objClass.getComments()) != null) {
			LScfrreasonsRepository.delete(LScfrreasonsRepository.findByComments(objClass.getComments()));
			objClass.getResponse().setStatus(true);
			objClass.getResponse().setInformation("ID_DELETEMSG");

		} else {
			LScfrreasonsRepository.save(objClass);
			objClass.getResponse().setStatus(true);
			objClass.getResponse().setInformation("ID_INSERT");
		}

		return objClass;
	}

	@SuppressWarnings("rawtypes")
	public List<LScfttransaction> GetCFRTransactions(Map<String, Object> objCFRFilter) throws ParseException {
		List<LScfttransaction> list = new ArrayList<LScfttransaction>();

		if (objCFRFilter.containsKey("user") && objCFRFilter.containsKey("module") && objCFRFilter.containsKey("system")
				&& objCFRFilter.containsKey("fromdate") && objCFRFilter.containsKey("todate")) {

			@SuppressWarnings("unchecked")
			Map<String, Object> objuser = (Map<String, Object>) objCFRFilter.get("user");

			Integer Usercode = (Integer) objuser.get("usercode");
			Integer site = (Integer) objCFRFilter.get("sitecode");
			String module = (String) objCFRFilter.get("module");
			@SuppressWarnings("unchecked")
			Map<String, String> system = (Map) objCFRFilter.get("system");

			String Audit = (String) system.get("Audit");
			if (system.get("Audit").equals("All") || system.get("Audit").equals("User Generated")
					|| system.get("Audit").equals("System Generated")) {
				Audit = (String) system.get("Audit");
			} else {
				Audit = (String) system.get("name");
			}

			Date fromdate = new SimpleDateFormat("dd/MM/yyyy").parse((String) objCFRFilter.get("fromdate"));
			Date todate = new SimpleDateFormat("dd/MM/yyyy").parse((String) objCFRFilter.get("todate"));

			if (Usercode == -1 && module.equals("All") && Audit.equals("All")) {
				list = lscfttransactionRepository.findByLssitemasterAndTransactiondateBetweenOrderBySerialnoDesc(site,
						fromdate, todate);
			}

			else if (Usercode != -1 && module.equals("All") && Audit.equals("All"))// user code filter
			{
				LSuserMaster objuserselected = lSuserMasterRepository.findByusercode(Usercode);
				list = lscfttransactionRepository
						.findByLssitemasterAndLsuserMasterAndTransactiondateBetweenOrderBySerialnoDesc(site,
								objuserselected.getUsercode(), fromdate, todate);

			} else if (Usercode == -1 && !module.equals("All") && Audit.equals("All"))// module filter
			{
				list = lscfttransactionRepository
						.findByLssitemasterAndModuleNameAndTransactiondateBetweenOrderBySerialnoDesc(site, module,
								fromdate, todate);
			} else if (Usercode != -1 && !module.equals("All") && Audit.equals("All"))// user code and moduel name
			{
				LSuserMaster objuserselected = lSuserMasterRepository.findByusercode(Usercode);
				list = lscfttransactionRepository
						.findByLssitemasterAndModuleNameAndLsuserMasterAndTransactiondateBetweenOrderBySerialnoDesc(
								site, module, objuserselected.getUsercode(), fromdate, todate);
			}

			else if (Usercode == -1 && !Audit.equals("All") && module.equals("All"))// Audit type filter
			{

				list = lscfttransactionRepository
						.findByLssitemasterAndSystemcomentsAndTransactiondateBetweenOrderBySerialnoDesc(site, Audit,
								fromdate, todate);
			} else if (Usercode == -1 && !Audit.equals("All") && !module.equals("All"))// audit+module
			{
				list = lscfttransactionRepository
						.findByLssitemasterAndSystemcomentsAndModuleNameAndTransactiondateBetweenOrderBySerialnoDesc(
								site, Audit, module, fromdate, todate);

			} else if (Usercode != -1 && !Audit.equals("All") && module.equals("All")) {
				LSuserMaster objuserselected = lSuserMasterRepository.findByusercode(Usercode);
				list = lscfttransactionRepository
						.findByLssitemasterAndSystemcomentsAndLsuserMasterAndTransactiondateBetweenOrderBySerialnoDesc(
								site, Audit, objuserselected.getUsercode(), fromdate, todate);

			} else if (Usercode != -1 && !Audit.equals("All") && !module.equals("All")) {
				LSuserMaster objuserselected = lSuserMasterRepository.findByusercode(Usercode);
				list = lscfttransactionRepository
						.findByLssitemasterAndSystemcomentsAndModuleNameAndLsuserMasterAndTransactiondateBetweenOrderBySerialnoDesc(
								site, Audit, module, objuserselected.getUsercode(), fromdate, todate);
			}
		}

		if (list.size() > 0) {
			int i = 0;

			List<LSuserMaster> userDetails = lSuserMasterRepository.findAll();

			while (list.size() > i) {
				int k = 0;
				while (userDetails.size() > k) {
					if (list.get(i).getLsuserMaster() != null
							&& list.get(i).getLsuserMaster().equals(userDetails.get(k).getUsercode())) {
						String username = userDetails.get(k).getUsername();
						list.get(i).setUsername(username);
					}
					k++;
				}
				i++;
			}
		}

		return list;
	}

	public Map<String, Object> GetAuditconfigUser(LSaudittrailconfiguration LSaudittrailconfiguration) {

		Map<String, Object> maprAuditConfig = new HashMap<String, Object>();

		LSpreferences objpref = LSpreferencesRepository.findByTasksettingsAndValuesettings("ELNparser", "0");

		List<LSaudittrailconfiguration> lstAudit = LSaudittrailconfigurationRepository
				.findByLsusermaster(LSaudittrailconfiguration.getLsusermaster());
		List<LSaudittrailconfigmaster> lstAuditmaster = LSaudittrailconfigmasterRepository.findByOrderByOrdersequnce();

		if (lstAudit != null && lstAudit.size() > 0) {
			maprAuditConfig.put("new", false);
			if (objpref != null) {

				List<LSaudittrailconfiguration> remLst = new ArrayList<LSaudittrailconfiguration>();

				lstAudit.stream().filter(item -> item.getModulename().equalsIgnoreCase("Parser")).forEach(item -> {
					item.operate();
					remLst.add(item);
				});

				lstAudit.removeAll(remLst);

				List<LSaudittrailconfigmaster> remMasLst = new ArrayList<LSaudittrailconfigmaster>();

				lstAuditmaster.stream().filter(item -> item.getModulename().equalsIgnoreCase("Parser"))
						.forEach(item -> {
							item.operate();
							remMasLst.add(item);
						});

				lstAuditmaster.removeAll(remMasLst);

				maprAuditConfig.put("AuditConfig", lstAudit);
				maprAuditConfig.put("AuditConfigmaster", lstAuditmaster);
			} else {
				maprAuditConfig.put("AuditConfig", lstAudit);
				maprAuditConfig.put("AuditConfigmaster", lstAuditmaster);
			}
		} else {
			List<LSaudittrailconfigmaster> lstAuditConfigmaster = LSaudittrailconfigmasterRepository
					.findByOrderByOrdersequnce();
			maprAuditConfig.put("new", true);
			if (objpref != null) {

				List<LSaudittrailconfigmaster> remMasLst = new ArrayList<LSaudittrailconfigmaster>();

				lstAuditConfigmaster.stream().filter(item -> item.getModulename().equalsIgnoreCase("Parser"))
						.forEach(item -> {
							item.operate();
							remMasLst.add(item);
						});

				lstAuditConfigmaster.removeAll(remMasLst);

				maprAuditConfig.put("AuditConfig", lstAuditConfigmaster);
			} else {
				maprAuditConfig.put("AuditConfig", lstAuditConfigmaster);
			}
		}

		return maprAuditConfig;
	}

	public List<LSaudittrailconfiguration> SaveAuditconfigUser(LSaudittrailconfiguration[] lsAuditary) {
		List<LSaudittrailconfiguration> lsAudit = Arrays.asList(lsAuditary);
		LSaudittrailconfigurationRepository.save(lsAudit);
//		lscfttransactionRepository.save(lsAudit.get(0).getObjsilentauditlst());
		if (lsAudit.get(0).getObjuser() != null) {
			lsAudit.get(0).getObjmanualaudit().setComments(lsAudit.get(0).getObjuser().getComments());
		}
		lsAudit.get(0).setResponse(new Response());
		lsAudit.get(0).getResponse().setStatus(true);
		lsAudit.get(0).getResponse().setInformation("");
		return lsAudit;
	}

	public LSuserMaster CheckUserPassWord(LoggedUser objuser) {

		LSuserMaster objExitinguser = new LSuserMaster();
		String username = objuser.getsUsername();
		LSSiteMaster objsite = lSSiteMasterRepository.findBysitecode(Integer.parseInt(objuser.getsSiteCode()));
		objExitinguser = lSuserMasterRepository.findByusernameAndLssitemaster(username, objsite);

		if (objExitinguser != null) {
			String Password = "";
			if (objExitinguser.getLoginfrom().equals("1")) {
				Password = objExitinguser.getPassword();
			} else {
				Password = AESEncryption.decrypt(objExitinguser.getPassword());
			}
			objExitinguser.setObjResponse(new Response());

			if (Password.equals(objuser.getsPassword().trim())) {
				objExitinguser.getObjResponse().setInformation("Valid user and password");
				objExitinguser.getObjResponse().setStatus(true);
			} else {
				objExitinguser.getObjResponse().setInformation("Invalid password");
				objExitinguser.getObjResponse().setStatus(false);
			}
		} else {
			objExitinguser = new LSuserMaster();
			objExitinguser.setObjResponse(new Response());
			objExitinguser.getObjResponse().setInformation("Invalid user");
			objExitinguser.getObjResponse().setStatus(false);
		}
		return objExitinguser;
	}

	public LSuserMaster CheckUserPassWord(LSuserMaster objuser) {

		LSuserMaster objExitinguser = new LSuserMaster();
		String username = objuser.getObjuser().getsUsername();
		objExitinguser = lSuserMasterRepository.findByusernameAndLssitemaster(username, objuser.getLssitemaster());

		if (objExitinguser != null) {
			String Password = "";
			if (objExitinguser.getLoginfrom().equals("1")) {
				Password = objExitinguser.getPassword();
			} else {
				Password = AESEncryption.decrypt(objExitinguser.getPassword());
			}
			objExitinguser.setObjResponse(new Response());

			if (Password.equals(objuser.getObjuser().getsPassword().trim())) {
				objExitinguser.getObjResponse().setInformation("Valid user and password");
				objExitinguser.getObjResponse().setStatus(true);
			} else {
				objExitinguser.getObjResponse().setInformation("Invalid password");
				objExitinguser.getObjResponse().setStatus(false);
			}
		} else {
			objExitinguser = new LSuserMaster();
			objExitinguser.setObjResponse(new Response());
			objExitinguser.getObjResponse().setInformation("Invalid user");
			objExitinguser.getObjResponse().setStatus(false);
		}
		return objExitinguser;
	}

	public List<LSreviewdetails> ReviewBtnValidation(LSreviewdetails[] objreview1) {

		List<LSreviewdetails> objreview = Arrays.asList(objreview1);

		LSreviewdetailsRepository.save(objreview);
		List<Integer> lstserailno = objreview.stream().map(LSreviewdetails::getSerialno).collect(Collectors.toList());
		if (lstserailno != null && lstserailno.size() > 0) {
			lscfttransactionRepository.updateReviewedstatus(lstserailno);
		}

		return objreview;
	}

	public List<LSreviewdetails> GetReviewDetails(List<LSreviewdetails> objreviewdetails) {

		List<Integer> lstserailno = objreviewdetails.stream().map(LSreviewdetails::getAuditserialno)
				.collect(Collectors.toList());
		if (objreviewdetails.get(0).getObjuser() != null) {

		}
		return LSreviewdetailsRepository.findByAuditserialnoIn(lstserailno);
	}

	public Map<String, Object> GetReviewDetails12(LSreviewdetails[] objreview1) {

		List<LSreviewdetails> objreviewdetails = Arrays.asList(objreview1);

		List<LScfttransaction> cfttDeatils = lscfttransactionRepository
				.findByserialnoIn(objreviewdetails.get(0).getSerialno());
		Map<String, Object> map = new HashMap<>();

		map.put("reviewDetails", GetReviewDetails(objreviewdetails));
		map.put("cfttDeatils", cfttDeatils);

		return map;
	}

	public Map<String, Object> exportData(LSuserMaster objuser) {
		Map<String, Object> map = new HashMap<>();
		LScfttransaction cfttDeatils = new LScfttransaction();
		if (objuser.getObjsilentaudit() != null) {
			objuser.getObjsilentaudit().setTableName("LScfttransaction");
			cfttDeatils = lscfttransactionRepository.save(objuser.getObjsilentaudit());
			cfttDeatils.setObjResponse(new Response());
			cfttDeatils.getObjResponse().setStatus(true);
			map.put("cfttDeatils", cfttDeatils);
		}

		objuser.setResponse(new Response());
		objuser.getResponse().setStatus(true);
		map.put("objuser", objuser);
		return map;
	}

	public LScfttransaction AuditConfigurationrecord(Map<String, Object> objaudit) {

		ObjectMapper objMapper = new ObjectMapper();
		LScfttransaction cfttransaction = new LScfttransaction();
		LoggedUser objUser = new LoggedUser();

//		silent audit
		if (objaudit.containsKey("objsilentaudit")) {
			cfttransaction = objMapper.convertValue(objaudit.get("objsilentaudit"), LScfttransaction.class);
//			cfttransaction.setActions("Warning");
			cfttransaction.setTableName("LScfttransaction");
			if (objaudit.containsKey("username")) {
				String username = objMapper.convertValue(objaudit.get("username"), String.class);
				Integer sitecode = cfttransaction.getLssitemaster();
				LSSiteMaster objsite = lSSiteMasterRepository.findBysitecode(sitecode);
				LSuserMaster objuser = lSuserMasterRepository.findByUsernameIgnoreCaseAndLssitemaster(username,
						objsite);
				cfttransaction.setLsuserMaster(objuser.getUsercode());
				cfttransaction.setLssitemaster(objuser.getLssitemaster().getSitecode());
				cfttransaction.setUsername(username);
			}
			lscfttransactionRepository.save(cfttransaction);
		}
//		manual audit
		if (objaudit.containsKey("objUser")) {
			objUser = objMapper.convertValue(objaudit.get("objUser"), LoggedUser.class);
			if (objaudit.containsKey("objmanualaudit")) {
				LScfttransaction objmanualaudit = new LScfttransaction();
				objmanualaudit = objMapper.convertValue(objaudit.get("objmanualaudit"), LScfttransaction.class);
				objmanualaudit.setActions("Warning");
				objmanualaudit.setTableName("LScfttransaction");
				objmanualaudit.setComments(objUser.getComments());
				lscfttransactionRepository.save(objmanualaudit);
			}
		}
		cfttransaction.setObjResponse(new Response());
		cfttransaction.getObjResponse().setStatus(false);
		cfttransaction.getObjResponse().setInformation("");

		return cfttransaction;
	}

	public LScfttransaction silentandmanualRecordHandler(Map<String, Object> mapObj) {

		ObjectMapper objMapper = new ObjectMapper();
		LScfttransaction cfttransaction = new LScfttransaction();

//		silent audit
		if (mapObj.containsKey("objsilentaudit")) {
			cfttransaction = objMapper.convertValue(mapObj.get("objsilentaudit"), LScfttransaction.class);
			lscfttransactionRepository.save(cfttransaction);
		}
//		manual audit
		if (mapObj.containsKey("objmanualaudit")) {
			LScfttransaction objmanualaudit = new LScfttransaction();
			objmanualaudit = objMapper.convertValue(mapObj.get("objmanualaudit"), LScfttransaction.class);
			lscfttransactionRepository.save(objmanualaudit);
		}

		cfttransaction.setObjResponse(new Response());
		cfttransaction.getObjResponse().setStatus(true);
		cfttransaction.getObjResponse().setInformation("");

		return cfttransaction;
	}

	@SuppressWarnings("rawtypes")
	public List<LScfttransaction> GetCFRTransactionsdid(Map<String, Object> objCFRFilter) throws ParseException {
		List<LScfttransaction> list = new ArrayList<LScfttransaction>();

		if (objCFRFilter.containsKey("user") && objCFRFilter.containsKey("module") && objCFRFilter.containsKey("system")
				&& objCFRFilter.containsKey("fromdate") && objCFRFilter.containsKey("todate")) {
			@SuppressWarnings("unchecked")
			Map<String, Object> objuser = (Map<String, Object>) objCFRFilter.get("user");
			ObjectMapper objMapper = new ObjectMapper();
			LScfttransaction lscfttransaction = objMapper.convertValue(objCFRFilter.get("LScfttransaction"),
					LScfttransaction.class);
			Integer Usercode = (Integer) objuser.get("usercode");
			Integer site = (Integer) objCFRFilter.get("sitecode");
			String module = (String) objCFRFilter.get("module");
			@SuppressWarnings("unchecked")
			Map<String, String> system = (Map) objCFRFilter.get("system");

			String Audit = (String) system.get("Audit");
			if (system.get("Audit").equals("All") || system.get("Audit").equals("User Generated")
					|| system.get("Audit").equals("System Generated")) {
				Audit = (String) system.get("Audit");
			} else {
				Audit = (String) system.get("name");
			}

			if (Usercode == -1 && module.equals("All") && Audit.equals("All")) {
				list = lscfttransactionRepository.findByLssitemasterAndTransactiondateBetweenOrderBySerialnoDesc(site,
						lscfttransaction.getFromdate(), lscfttransaction.getTodate());
			}

			else if (Usercode != -1 && module.equals("All") && Audit.equals("All"))// user code filter
			{
				LSuserMaster objuserselected = lSuserMasterRepository.findByusercode(Usercode);
				list = lscfttransactionRepository
						.findByLssitemasterAndLsuserMasterAndTransactiondateBetweenOrderBySerialnoDesc(site,
								objuserselected.getUsercode(), lscfttransaction.getFromdate(),
								lscfttransaction.getTodate());
			} else if (Usercode == -1 && !module.equals("All") && Audit.equals("All"))// module filter
			{
				list = lscfttransactionRepository
						.findByLssitemasterAndModuleNameAndTransactiondateBetweenOrderBySerialnoDesc(site, module,
								lscfttransaction.getFromdate(), lscfttransaction.getTodate());
			} else if (Usercode != -1 && !module.equals("All") && Audit.equals("All"))// user code and moduel name
			{
				LSuserMaster objuserselected = lSuserMasterRepository.findByusercode(Usercode);
				list = lscfttransactionRepository
						.findByLssitemasterAndModuleNameAndLsuserMasterAndTransactiondateBetweenOrderBySerialnoDesc(
								site, module, objuserselected.getUsercode(), lscfttransaction.getFromdate(),
								lscfttransaction.getTodate());
			}

			else if (Usercode == -1 && !Audit.equals("All") && module.equals("All"))// Audit type filter
			{

				list = lscfttransactionRepository
						.findByLssitemasterAndSystemcomentsAndTransactiondateBetweenOrderBySerialnoDesc(site, Audit,
								lscfttransaction.getFromdate(), lscfttransaction.getTodate());
			} else if (Usercode == -1 && !Audit.equals("All") && !module.equals("All"))// audit+module
			{
				list = lscfttransactionRepository
						.findByLssitemasterAndSystemcomentsAndModuleNameAndTransactiondateBetweenOrderBySerialnoDesc(
								site, Audit, module, lscfttransaction.getFromdate(), lscfttransaction.getTodate());
			} else if (Usercode != -1 && !Audit.equals("All") && module.equals("All")) {
				LSuserMaster objuserselected = lSuserMasterRepository.findByusercode(Usercode);
				list = lscfttransactionRepository
						.findByLssitemasterAndSystemcomentsAndLsuserMasterAndTransactiondateBetweenOrderBySerialnoDesc(
								site, Audit, objuserselected.getUsercode(), lscfttransaction.getFromdate(),
								lscfttransaction.getTodate());
			} else if (Usercode != -1 && !Audit.equals("All") && !module.equals("All")) {
				LSuserMaster objuserselected = lSuserMasterRepository.findByusercode(Usercode);
				list = lscfttransactionRepository
						.findByLssitemasterAndSystemcomentsAndModuleNameAndLsuserMasterAndTransactiondateBetweenOrderBySerialnoDesc(
								site, Audit, module, objuserselected.getUsercode(), lscfttransaction.getFromdate(),
								lscfttransaction.getTodate());
			}
		}

		if (list.size() > 0) {
			int i = 0;

			List<LSuserMaster> userDetails = lSuserMasterRepository.findAll();

			while (list.size() > i) {
				int k = 0;
				while (userDetails.size() > k) {
					if (list.get(i).getLsuserMaster() != null
							&& list.get(i).getLsuserMaster().equals(userDetails.get(k).getUsercode())) {
						String username = userDetails.get(k).getUsername();
						list.get(i).setUsername(username);
					}
					k++;
				}
				i++;
			}
		}

		return list;
	}
}
