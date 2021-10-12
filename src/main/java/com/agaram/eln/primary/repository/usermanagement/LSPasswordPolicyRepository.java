package com.agaram.eln.primary.repository.usermanagement;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.agaram.eln.primary.model.usermanagement.LSPasswordPolicy;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;

public interface LSPasswordPolicyRepository extends JpaRepository<LSPasswordPolicy, Integer> {
	
	public LSPasswordPolicy findBylsusermaster(LSPasswordPolicy lspasswordpolicy);
	
	public List<LSPasswordPolicy> findBylssitemaster(LSSiteMaster LSSiteMaster);
	
	@Transactional
	public Long deleteBylsusermaster(LSPasswordPolicy lspasswordpolicy);
	
	public LSPasswordPolicy findTopByOrderByPolicycodeAsc();
	
	public LSPasswordPolicy findTopByAndLssitemasterOrderByPolicycodeAsc(LSSiteMaster lsSiteMaster);
	
	public LSPasswordPolicy findTopByAndLssitemasterOrderByPolicycodeDesc(LSSiteMaster lsSiteMaster);

	public LSPasswordPolicy findByLssitemaster(LSSiteMaster lsSiteMaster);

	public LSPasswordPolicy findTopByOrderByPolicycodeDesc();
}
