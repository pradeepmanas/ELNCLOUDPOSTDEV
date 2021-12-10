package com.agaram.eln.primary.repository.usermanagement;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.usermanagement.LSuserActions;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.model.usermanagement.LSusergroup;

public interface LSuserMasterRepository extends JpaRepository<LSuserMaster, Integer> {
	 public LSuserMaster findByusernameAndLssitemaster(String username, LSSiteMaster lssitemaster);
	 
	 public LSuserMaster findByusercode(Integer usercode);
	 
	 public List<LSuserMaster> findByUsernameAndLssitemaster(String username, LSSiteMaster lssitemaster);
	 
	 public List<LSuserMaster> findByLssitemasterAndLsusergroup(LSSiteMaster lssitemaster, LSusergroup lsusergroup);
	 
	 @Transactional
	 @Modifying
	 @Query("update LSuserMaster u set u.password = ?1 where u.usercode = ?2")
	 void setpasswordByusercode(String password, Integer usercode);
	 
	 public List<LSuserMaster> findByusernameNot(String username);
	 public List<LSuserMaster> findByusernameNotAndUserretirestatusNot(String username,Integer userretirestatue);
	 public LSuserMaster findByUsernameAndPassword(String username, String password);
	 public List<LSuserMaster> findByUsernameNotAndLssitemaster(String username, LSSiteMaster lssitemaster);
	 public List<LSuserMaster> findByUsernameNotAndUserretirestatusNotAndLssitemaster(String username,Integer userretirestatue, LSSiteMaster lssitemaster);
	 public Long countByusercodeNot(Integer usercode);
	 
	 public Long countByusercodeNotAndUserretirestatusNot(Integer usercode,Integer userretirestatus);

	 public LSuserMaster findByusernameIgnoreCase(String username);
	 
	 public LSuserMaster findByUsernameIgnoreCaseAndLoginfrom(String username, String loginform);
	 public LSuserMaster findByUsernameIgnoreCaseAndLoginfromAndUserretirestatusNot(String username, String loginform,Integer userretirestatus);
	 
	 public LSuserMaster findByUsernameIgnoreCaseAndLssitemaster(String username, LSSiteMaster lssitemaster);
	 
	 public LSuserMaster findByUsernameIgnoreCaseAndLssitemasterAndUserretirestatusNot(String username, LSSiteMaster lssitemaster,Integer userretirestatus);
	 
	 public LSuserMaster findByUsernameIgnoreCaseAndLoginfromAndLssitemaster(String username, String loginform, LSSiteMaster lssitemaster);
	
	 @Transactional
	 @Modifying
	 @Query("update LSuserMaster u set u.lsuserActions = ?1 where u.usercode = ?2")
	 void setuseractionByusercode(LSuserActions lsuserActions, Integer usercode);
	 
	 @Transactional
	 @Modifying
	 @Query("update LSuserMaster u set u.password = ?1,u.passwordstatus = ?2 where u.usercode = ?3")
	 void setpasswordandpasswordstatusByusercode(String password,Integer integer, Integer usercode);
	 
	 public List<LSuserMaster> findByLssitemasterAndUsercodeNotInAndUserretirestatusAndUnifieduseridNotNullOrderByUsercodeDesc(LSSiteMaster lssitemaster,List<Integer> usercode, Integer userretirestatus);

	public Object findByLssitemasterAndUsernameIgnoreCase(LSSiteMaster lssitemaster, String username);

	public LSuserMaster findByUsernameIgnoreCaseAndLssitemasterAndLoginfromAndUserretirestatusNot(String username,
			LSSiteMaster objsite, String string, int i);

	public LSuserMaster findByUsernameIgnoreCaseAndLssitemasterAndLoginfrom(String username, LSSiteMaster objsite,
			String string);

	public LSuserMaster findByusernameIgnoreCaseAndLssitemaster(String username, LSSiteMaster objsiteobj);

	public List<LSuserMaster> findByUsernameNotAndUserretirestatusNotAndLssitemasterOrderByCreateddateDesc(
			String string, int i, LSSiteMaster lssitemaster);

	public List<LSuserMaster> findByusernameNotAndUserretirestatusNotOrderByCreateddateDesc(String string, int i);


}
