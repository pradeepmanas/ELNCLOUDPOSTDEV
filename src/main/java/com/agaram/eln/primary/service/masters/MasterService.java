package com.agaram.eln.primary.service.masters;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Service;

import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.masters.Lsrepositories;
import com.agaram.eln.primary.model.masters.Lsrepositoriesdata;
import com.agaram.eln.primary.repository.masters.LsrepositoriesRepository;
import com.agaram.eln.primary.repository.masters.LsrepositoriesdataRepository;

@Service
@EnableJpaRepositories(basePackageClasses = LsrepositoriesRepository.class)
public class MasterService {
	
	@Autowired
	private LsrepositoriesRepository lsrepositoriesRepository;
	
	@Autowired
	private LsrepositoriesdataRepository lsrepositoriesdataRepository;
	
	public List<Lsrepositories> Getallrepositories(Lsrepositories lsrepositories)
	{
		return lsrepositoriesRepository.findBySitecodeOrderByRepositorycodeAsc(lsrepositories.getSitecode());
	}
	
	public Lsrepositories Saverepository(Lsrepositories lsrepositories)
	{
		Response objResponse = new Response();
		Lsrepositories objrepo = null;
		if(lsrepositories.getRepositorycode() != null)
		{
			objrepo = lsrepositoriesRepository.findByRepositorynameAndSitecodeAndRepositorycodeNot(lsrepositories.getRepositoryname(), lsrepositories.getSitecode(),lsrepositories.getRepositorycode() ); 
		}
		else
		{
			objrepo = lsrepositoriesRepository.findByRepositorynameAndSitecode(lsrepositories.getRepositoryname(), lsrepositories.getSitecode()); 
		}
		
		if(objrepo != null)
		{
			objResponse.setStatus(false);
			objResponse.setInformation("Repository already exists");
		}
		else
		{
			objResponse.setStatus(true);
			lsrepositoriesRepository.save(lsrepositories);
		}
		
		lsrepositories.setObjResponse(objResponse);
		return lsrepositories;
	}
	
	public List<Lsrepositoriesdata> Getallrepositoriesdata(Lsrepositoriesdata lsrepositoriesdata)
	{
		return lsrepositoriesdataRepository.findByRepositorycodeAndSitecodeAndItemstatusOrderByRepositorydatacodeDesc(lsrepositoriesdata.getRepositorycode(), lsrepositoriesdata.getSitecode(),1);
	}
	
	public Lsrepositoriesdata Saverepositorydata(Lsrepositoriesdata lsrepositoriesdata)
	{
		Response objResponse = new Response();
		Lsrepositoriesdata lsrepodata = null;
		
		if(lsrepositoriesdata.getRepositorydatacode() != null)
		{
			lsrepodata = lsrepositoriesdataRepository.findByRepositorycodeAndRepositoryitemnameAndSitecodeAndRepositorydatacodeNot(
					lsrepositoriesdata.getRepositorycode(), lsrepositoriesdata.getRepositoryitemname(), lsrepositoriesdata.getSitecode(), lsrepositoriesdata.getRepositorydatacode());
		}
		else
		{
			lsrepodata = lsrepositoriesdataRepository.findByRepositorycodeAndRepositoryitemnameAndSitecode(
			lsrepositoriesdata.getRepositorycode(), lsrepositoriesdata.getRepositoryitemname(), lsrepositoriesdata.getSitecode());
				
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
	
	public Lsrepositoriesdata GetupdatedRepositorydata(Lsrepositoriesdata lsrepositoriesdata)
	{
		lsrepositoriesdata = lsrepositoriesdataRepository.findOne(lsrepositoriesdata.getRepositorydatacode());
		return lsrepositoriesdata;
	}
	
	public Lsrepositoriesdata DeleteRepositorydata (Lsrepositoriesdata lsrepositoriesdata)
	{
		lsrepositoriesdata = lsrepositoriesdataRepository.findOne(lsrepositoriesdata.getRepositorydatacode());
		lsrepositoriesdata.setItemstatus(0);
		lsrepositoriesdataRepository.save(lsrepositoriesdata);
		
		return lsrepositoriesdata;
	}
}
