package com.agaram.eln.primary.repository.usermanagement;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;

public interface LSSiteMasterRepository extends JpaRepository<LSSiteMaster, Integer> {
	public LSSiteMaster findBysitecode(Integer sitecode);
	public LSSiteMaster findBySitenameAndIstatus(String sitename,Integer status);
	public List<LSSiteMaster> findBySitenameNot(String sitename);
	public List<LSSiteMaster> findByIstatus(Integer status);
	public List<LSSiteMaster> findByOrderBySitecodeAsc();
	public LSSiteMaster findBySitenameIgnoreCaseAndIstatus(String sitename, Integer status);
}
