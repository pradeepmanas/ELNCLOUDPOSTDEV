package com.agaram.eln.secondary.service.archive;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.repository.cfr.LScfttransactionRepository;
import com.agaram.eln.primary.repository.usermanagement.LSuserMasterRepository;
import com.agaram.eln.secondary.model.archive.LScfrArchiveHistory;
import com.agaram.eln.secondary.model.archive.LScfrachivetransaction;
import com.agaram.eln.secondary.repository.archive.LScfrArchiveHistoryRepository;
import com.agaram.eln.secondary.repository.archive.LScfrachivetransactionRepository;

@Service
//@EnableJpaRepositories(basePackageClasses = LScfrArchiveHistoryRepository.class)
public class ArchiveService {
	
	@Autowired
    private LScfrachivetransactionRepository lscfrachivetransactionRepository;
	
	@Autowired
    private LScfttransactionRepository lscfttransactionRepository;
	
	@Autowired
    private LScfrArchiveHistoryRepository lscfrArchiveHistoryRepository;

	@Autowired
    private LSuserMasterRepository lsuserMasterRepository;
	
	public List<LScfttransaction> CreateArchive(LScfrArchiveHistory objcfrArchiveHistory)
	{
		List<LScfttransaction> lstcfrtransaction = lscfttransactionRepository.findAll();
		
		List<LSuserMaster> usermasterobj = lsuserMasterRepository.findAll();
		
		//objcfrArchiveHistory.setLscfrachivetransaction(lsarchive);
		lscfrArchiveHistoryRepository.save(objcfrArchiveHistory);
		lstcfrtransaction.stream().forEach(obj1->{ usermasterobj.stream().filter(obj2->{ return obj2.getUsercode().equals(obj1.getLsuserMaster());})
			.limit(1)
			.forEach(obj2->{obj1.setUsername(obj2.getUsername());}); });
		
		
		List<LScfrachivetransaction> lsarchive = lstcfrtransaction.stream()
		        .map(lscfrtransaction -> new LScfrachivetransaction(lscfrtransaction.getSerialno(),lscfrtransaction.getModuleName(),lscfrtransaction.getReason(),
		        		lscfrtransaction.getComments(), lscfrtransaction.getActions(),lscfrtransaction.getSystemcoments(),lscfrtransaction.getTransactiondate(),
		        		lscfrtransaction.getManipulatetype(), lscfrtransaction.getTableName(), lscfrtransaction.getModifiedData(), lscfrtransaction.getRequestedclientid(),
		        		lscfrtransaction.getAffectedclientid(), lscfrtransaction.getInstrumentid(),lscfrtransaction.getReviewedstatus(), lscfrtransaction.getUsername(), objcfrArchiveHistory))
		        .collect(Collectors.toList());
		lscfrachivetransactionRepository.save(lsarchive);
		
		
		lscfttransactionRepository.deleteAll();
		
		if(objcfrArchiveHistory.getObjsilentaudit() != null)
    	{
//			objcfrArchiveHistory.getObjsilentaudit().setModuleName("Audit Trail History");
//			objcfrArchiveHistory.getObjsilentaudit().setComments(objcfrArchiveHistory.getDiscription());
//			objcfrArchiveHistory.getObjsilentaudit().setActions("Archive");
//			objcfrArchiveHistory.getObjsilentaudit().setSystemcoments("System Generated");
			objcfrArchiveHistory.getObjsilentaudit().setTableName("LScfrArchiveHistory");
//			lscfttransactionRepository.save(objcfrArchiveHistory.getObjsilentaudit());
    	}
		
		return lscfttransactionRepository.findAll();
	}
	
	public List<LScfttransaction> GetAllArchiveList(LSuserMaster objuser)
	{
		List<LScfttransaction> lstcfrtransaction = lscfttransactionRepository.findAll();
		if(objuser.getObjsilentaudit() != null)
    	{
//			objcfrArchiveHistory.getObjsilentaudit().setModuleName("Audit Trail History");
//			objcfrArchiveHistory.getObjsilentaudit().setComments(objcfrArchiveHistory.getDiscription());
//			objcfrArchiveHistory.getObjsilentaudit().setActions("Archive");
//			objcfrArchiveHistory.getObjsilentaudit().setSystemcoments("System Generated");
			objuser.getObjsilentaudit().setTableName("LScfrArchiveHistory");
			lscfttransactionRepository.save(objuser.getObjsilentaudit());
    	}
		return lstcfrtransaction;
	}
	
	public List<LScfrArchiveHistory> GetAllArchiveHistory(LSuserMaster objuser)
	{
		List<LScfrArchiveHistory> lstarchivehistory = lscfrArchiveHistoryRepository.findByOrderByArchivecodeDesc();
		if(objuser.getObjsilentaudit() != null)
    	{
//			objcfrArchiveHistory.getObjsilentaudit().setModuleName("Audit Trail History");
//			objcfrArchiveHistory.getObjsilentaudit().setComments(objcfrArchiveHistory.getDiscription());
//			objcfrArchiveHistory.getObjsilentaudit().setActions("Archive");
//			objcfrArchiveHistory.getObjsilentaudit().setSystemcoments("System Generated");
			objuser.getObjsilentaudit().setTableName("LScfrArchiveHistory");
//			lscfttransactionRepository.save(objuser.getObjsilentaudit());
    	}
		return lstarchivehistory;
	}
	
	public List<LScfrachivetransaction> GetArchiveDataonHistory(LScfrArchiveHistory objHistroy)
	{
		List<LScfrachivetransaction> lstarchivedata = lscfrachivetransactionRepository.findByLscfrArchiveHistoryOrderBySerialnoDesc(objHistroy);
		if(objHistroy.getObjsilentaudit() != null)
    	{
//			objcfrArchiveHistory.getObjsilentaudit().setModuleName("Audit Trail History");
//			objcfrArchiveHistory.getObjsilentaudit().setComments(objcfrArchiveHistory.getDiscription());
//			objcfrArchiveHistory.getObjsilentaudit().setActions("Archive");
//			objcfrArchiveHistory.getObjsilentaudit().setSystemcoments("System Generated");
			objHistroy.getObjsilentaudit().setTableName("LScfrArchiveHistory");
			//lscfttransactionRepository.save(objHistroy.getObjsilentaudit());
    	}
		if(objHistroy.getObjuser() != null)
    	{
			objHistroy.getObjmanualaudit().setActions("OpenArchive");
			//objHistroy.getObjmanualaudit().setComments(objHistroy.getObjuser().getComments());
			objHistroy.getObjmanualaudit().setTableName("LScfrArchiveHistory");
			//lscfttransactionRepository.save(objHistroy.getObjmanualaudit());
    	}
		return lstarchivedata;
	}
}
