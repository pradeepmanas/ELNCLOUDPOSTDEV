package com.agaram.eln.primary.repository.usermanagement;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.usermanagement.LSdomainMaster;

public interface LSdomainMasterRepository extends JpaRepository<LSdomainMaster, Integer> {
	public List<LSdomainMaster> findBylssitemaster(LSSiteMaster objsite);
	public List<LSdomainMaster> findBycategoriesAndDomainstatus(String categories,Integer domainstatus);
	public LSdomainMaster findByDomainnameAndDomainstatus(String domainname,Integer domainstatus);
	public List<LSdomainMaster> findBylssitemasterAndDomainstatus(LSSiteMaster objsite,Integer domainstatus);
	public LSdomainMaster findByDomainnameIgnoreCaseAndDomainstatus(String domainname, Integer domainstatus);
}
