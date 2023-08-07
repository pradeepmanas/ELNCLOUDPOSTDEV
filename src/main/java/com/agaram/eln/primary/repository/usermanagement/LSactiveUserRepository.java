package com.agaram.eln.primary.repository.usermanagement;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.usermanagement.LSactiveUser;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;


public interface LSactiveUserRepository extends JpaRepository<LSactiveUser, Integer> {
	public LSactiveUser findBylsusermaster(LSuserMaster lsuserMaster);
	public List<LSactiveUser> findBylssitemaster(LSSiteMaster lsSiteMaster);
	@Transactional
	public Long deleteBylsusermaster(LSuserMaster lsuserMaster);
	@Transactional
	public Long deleteByActiveusercode(Integer activeusercode);
	public List<LSactiveUser> findTop1Bylsusermaster(LSuserMaster objuser);
	public LSactiveUser findByActiveusercode(Integer activerUsercode);
	public List<LSactiveUser> findByLastactivetimeLessThan(Date date);
	public List<LSactiveUser> findByLsusermaster(LSuserMaster user);
	public List<LSactiveUser> findByRemoveinititatedAndLastactivetimeLessThan(boolean b, Date date);
}