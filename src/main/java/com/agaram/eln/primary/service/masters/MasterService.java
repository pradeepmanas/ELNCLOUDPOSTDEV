package com.agaram.eln.primary.service.masters;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Service;

import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.instrumentDetails.LsOrderSampleUpdate;
import com.agaram.eln.primary.model.masters.Lsrepositories;
import com.agaram.eln.primary.model.masters.Lsrepositoriesdata;
import com.agaram.eln.primary.model.usermanagement.LSnotification;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.repository.instrumentDetails.LsOrderSampleUpdateRepository;
import com.agaram.eln.primary.repository.masters.LsrepositoriesRepository;
import com.agaram.eln.primary.repository.masters.LsrepositoriesdataRepository;
import com.agaram.eln.primary.repository.usermanagement.LSnotificationRepository;
import com.agaram.eln.primary.service.instrumentDetails.InstrumentService;

@Service
@EnableJpaRepositories(basePackageClasses = LsrepositoriesRepository.class)
public class MasterService {

	static final Logger logger = Logger.getLogger(InstrumentService.class.getName());
	@Autowired
	private LsrepositoriesRepository lsrepositoriesRepository;

	@Autowired
	private LsrepositoriesdataRepository lsrepositoriesdataRepository;

	@Autowired
	private LsOrderSampleUpdateRepository LsOrderSampleUpdateRepository;

	@Autowired
	private LSnotificationRepository lsnotificationRepository;

	public List<Lsrepositories> Getallrepositories(Lsrepositories lsrepositories) {
		return lsrepositoriesRepository.findBySitecodeOrderByRepositorycodeAsc(lsrepositories.getSitecode());
	}

	public Lsrepositories Saverepository(Lsrepositories lsrepositories) {
		Response objResponse = new Response();
		Lsrepositories objrepo = null;
		if (lsrepositories.getRepositorycode() != null) {
			objrepo = lsrepositoriesRepository.findByRepositorynameAndSitecodeAndRepositorycodeNot(
					lsrepositories.getRepositoryname(), lsrepositories.getSitecode(),
					lsrepositories.getRepositorycode());
		} else {
			objrepo = lsrepositoriesRepository.findByRepositorynameAndSitecode(lsrepositories.getRepositoryname(),
					lsrepositories.getSitecode());
		}

		if (objrepo != null) {
			objResponse.setStatus(false);
			objResponse.setInformation("Repository already exists");
		} else {
			objResponse.setStatus(true);
			lsrepositoriesRepository.save(lsrepositories);
		}

		lsrepositories.setObjResponse(objResponse);
		return lsrepositories;
	}

	public List<Lsrepositoriesdata> Getallrepositoriesdata(Lsrepositoriesdata lsrepositoriesdata) {
		return lsrepositoriesdataRepository.findByRepositorycodeAndSitecodeAndItemstatusOrderByRepositorydatacodeDesc(
				lsrepositoriesdata.getRepositorycode(), lsrepositoriesdata.getSitecode(), 1);
	}

	public Lsrepositoriesdata Saverepositorydata(Lsrepositoriesdata lsrepositoriesdata) {
		Response objResponse = new Response();
		@SuppressWarnings("unused")
		Lsrepositoriesdata lsrepodata = null;

		if (lsrepositoriesdata.getRepositorydatacode() != null) {
			lsrepodata = lsrepositoriesdataRepository
					.findByRepositorycodeAndRepositoryitemnameAndSitecodeAndRepositorydatacodeNot(
							lsrepositoriesdata.getRepositorycode(), lsrepositoriesdata.getRepositoryitemname(),
							lsrepositoriesdata.getSitecode(), lsrepositoriesdata.getRepositorydatacode());
		} else {
			lsrepodata = lsrepositoriesdataRepository.findByRepositorycodeAndRepositoryitemnameAndSitecode(
					lsrepositoriesdata.getRepositorycode(), lsrepositoriesdata.getRepositoryitemname(),
					lsrepositoriesdata.getSitecode());

		}

//		if(lsrepodata != null)
//		{
//			objResponse.setStatus(false);
//			objResponse.setInformation("Iteam name already exists in the repositroy");
//		}
//		else
//		{
		objResponse.setStatus(true);
		lsrepositoriesdataRepository.save(lsrepositoriesdata);
//		}

		lsrepositoriesdata.setObjResponse(objResponse);
		return lsrepositoriesdata;
	}

	public Lsrepositoriesdata GetupdatedRepositorydata(Lsrepositoriesdata lsrepositoriesdata) {
		lsrepositoriesdata = lsrepositoriesdataRepository.findOne(lsrepositoriesdata.getRepositorydatacode());
		return lsrepositoriesdata;
	}

	public Lsrepositoriesdata DeleteRepositorydata(Lsrepositoriesdata lsrepositoriesdata) {
		lsrepositoriesdata = lsrepositoriesdataRepository.findOne(lsrepositoriesdata.getRepositorydatacode());
		lsrepositoriesdata.setItemstatus(0);
		lsrepositoriesdataRepository.save(lsrepositoriesdata);

		return lsrepositoriesdata;
	}

	public List<LsOrderSampleUpdate> getinventoryhistory(LsOrderSampleUpdate lsordersamplUpdate) {
//		List<LsOrderSampleUpdate>	lsordersamplUpdateobj =LsOrderSampleUpdateRepository.findByRepositorycodeAndRepositorydatacodeAndQuantityusedNotAndHistorydetailsNotNull(lsordersamplUpdate.getRepositorycode(),lsordersamplUpdate.getRepositorydatacode(),0);
		List<LsOrderSampleUpdate> lsordersamplUpdateobj = LsOrderSampleUpdateRepository
				.findByRepositorycodeAndRepositorydatacodeAndQuantityusedNotAndHistorydetailsNotNull(
						lsordersamplUpdate.getRepositorycode(), lsordersamplUpdate.getRepositorydatacode(), 0);

		return lsordersamplUpdateobj;
	}

	public Response pushnotificationforinventory(List<Lsrepositoriesdata> lsrepositoriesdata) {

		try {
			String Details = "";
			String Notifiction = "";
			LSnotification obj = new LSnotification();
			Response Response = new Response();
			List<LSnotification> lstnotifications = new ArrayList<LSnotification>();
			for (int i = 0; i < lsrepositoriesdata.size(); i++) {
				obj = lsnotificationRepository.findByRepositorycodeAndRepositorydatacode(
						lsrepositoriesdata.get(i).getRepositorycode(),
						lsrepositoriesdata.get(i).getRepositorydatacode());
				if (lsrepositoriesdata.get(i).getRepositorycode() != null
						&& lsrepositoriesdata.get(i).getRepositorydatacode() != null && obj == null) {

					Notifiction = "EXPIREDINVENTORY";

					Details = "{\"repositorycode\":\"" + lsrepositoriesdata.get(i).getRepositorycode()
							+ "\", \"repositorydatacode\":\"" + lsrepositoriesdata.get(i).getRepositorydatacode()
							+ "\", \"usercode\":\"" + lsrepositoriesdata.get(i).getUsercode()
							+ "\", \"repositoryitemname\":\"" + lsrepositoriesdata.get(i).getRepositoryitemname()
							+ "\", \"repositoryuniqueid\":\"" + lsrepositoriesdata.get(i).getRepositoryuniqueid()
							+ "\", \"expireddatecount\":\"" + lsrepositoriesdata.get(i).getExpireddatecount() + "\"}";

					Date date = new Date();
					LSuserMaster LSuserMaster = new LSuserMaster();
					LSuserMaster.setUsercode(lsrepositoriesdata.get(i).getUsercode());
					LSnotification objnotify = new LSnotification();

					objnotify.setNotifationfrom(LSuserMaster);
					objnotify.setNotifationto(LSuserMaster);
					objnotify.setNotificationdate(date);
					objnotify.setNotification(Notifiction);
					objnotify.setNotificationdetils(Details);
					objnotify.setIsnewnotification(1);
					objnotify.setNotificationpath("/masters");
					objnotify.setRepositorycode(lsrepositoriesdata.get(i).getRepositorycode());
					objnotify.setRepositorydatacode(lsrepositoriesdata.get(i).getRepositorydatacode());

					lstnotifications.add(objnotify);
				}

			}
			if (!lstnotifications.isEmpty()) {
				lsnotificationRepository.save(lstnotifications);
				Response.setInformation("Successfully added");
				Response.setStatus(true);
			}

		}

		catch (Exception e) {
			logger.error("updatenotificationfororder : " + e.getMessage());
		}
		return null;
	}
}
