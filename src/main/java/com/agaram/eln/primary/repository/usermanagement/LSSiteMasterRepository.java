package com.agaram.eln.primary.repository.usermanagement;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;

public interface LSSiteMasterRepository extends JpaRepository<LSSiteMaster, Integer> {
	public LSSiteMaster findBysitecode(Integer sitecode);
	public LSSiteMaster findBySitenameAndIstatus(String sitename,Integer status);
	public List<LSSiteMaster> findBySitenameNot(String sitename);
	public List<LSSiteMaster> findByIstatus(Integer status);
	public List<LSSiteMaster> findByOrderBySitecodeDesc();
	public List<LSSiteMaster> findBySitenameIgnoreCaseAndIstatus(String sitename, Integer status);
}
