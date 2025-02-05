package com.agaram.eln.primary.repository.usermanagement;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.agaram.eln.primary.fetchmodel.getmasters.Usermaster;
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

	public List<LSuserMaster> findByusernameNotAndUserretirestatusNot(String username, Integer userretirestatue);

	public LSuserMaster findByUsernameAndPassword(String username, String password);

	public List<LSuserMaster> findByUsernameNotAndLssitemaster(String username, LSSiteMaster lssitemaster);

	public List<LSuserMaster> findByUsernameNotAndUserretirestatusNotAndLssitemaster(String username,
			Integer userretirestatue, LSSiteMaster lssitemaster);

	public Long countByusercodeNot(Integer usercode);

	public Long countByusercodeNotAndUserretirestatusNot(Integer usercode, Integer userretirestatus);

	public LSuserMaster findByusernameIgnoreCase(String username);

	public LSuserMaster findByUsernameIgnoreCaseAndLoginfrom(String username, String loginform);

	public LSuserMaster findByUsernameIgnoreCaseAndLoginfromAndUserretirestatusNot(String username, String loginform,
			Integer userretirestatus);

	public LSuserMaster findByUsernameIgnoreCaseAndLssitemaster(String username, LSSiteMaster lssitemaster);

	public List<LSuserMaster> findByUsernameIgnoreCaseAndLssitemasterAndUserretirestatusNot(String username,
			LSSiteMaster lssitemaster, Integer userretirestatus);

	@Transactional
	public LSuserMaster findTop1ByUsernameIgnoreCaseAndLoginfromAndLssitemaster(String username, String loginform,
			LSSiteMaster lssitemaster);

	@Transactional
	@Modifying
	@Query("update LSuserMaster u set u.lsuserActions = ?1 where u.usercode = ?2")
	void setuseractionByusercode(LSuserActions lsuserActions, Integer usercode);

	@Transactional
	@Modifying
	@Query("update LSuserMaster u set u.password = ?1,u.passwordstatus = ?2 where u.usercode = ?3")
	void setpasswordandpasswordstatusByusercode(String password, Integer integer, Integer usercode);

	@Transactional
	@Modifying
	@Query("update LSuserMaster u set u.password = ?1,u.passwordstatus = ?2,u.forgetstatus = 0 where u.usercode = ?3")
	void setpasswordandpasswordstatusandforgetstatusByusercode(String password, Integer integer, Integer usercode);
	
	public List<LSuserMaster> findByLssitemasterAndUsercodeNotInAndUserstatusAndUserretirestatusAndUnifieduseridNotNullOrderByUsercodeDesc(
			LSSiteMaster lssitemaster, List<Integer> usercode, String string, Integer userretirestatus);

	public Object findByLssitemasterAndUsernameIgnoreCase(LSSiteMaster lssitemaster, String username);

	public List<LSuserMaster> findByUsernameIgnoreCaseAndLssitemasterAndLoginfromAndUserretirestatusNot(String username,
			LSSiteMaster objsite, String string, int i);

	public LSuserMaster findByUsernameIgnoreCaseAndLssitemasterAndLoginfrom(String username, LSSiteMaster objsite,
			String string);

	public LSuserMaster findByusernameIgnoreCaseAndLssitemaster(String username, LSSiteMaster objsiteobj);

	public List<LSuserMaster> findByUsernameNotAndUserretirestatusNotAndLssitemasterOrderByCreateddateDesc(
			String string, int i, LSSiteMaster lssitemaster);

	public List<LSuserMaster> findByUserretirestatusNotAndLssitemasterOrderByCreateddateDesc(int i,
			LSSiteMaster lssitemaster);

	public List<LSuserMaster> findByusernameNotAndUserretirestatusNotOrderByCreateddateDesc(String string, int i);

	public List<LSuserMaster> findByUserretirestatusNotOrderByCreateddateDesc(int i);

	public List<LSuserMaster> findByUsercodeNotInAndUserretirestatusAndUnifieduseridNotNullOrderByUsercodeDesc(
			List<Integer> lstuser, int i);

	public Long countByusercodeNotAndUserretirestatusNotAndLssitemaster(int i, int j, LSSiteMaster objsite);

	public List<LSuserMaster> findByUsercodeInAndUserretirestatusNot(List<Integer> usercode, int i);

	public List<LSuserMaster> findByUsernameIgnoreCaseAndLoginfromAndUserretirestatusNotOrderByCreateddateDesc(
			String username, String string, int i);

	
	public LSuserMaster findByUsercode(LSuserMaster lsuserMaster);
	public LSuserMaster findByusercode(LSuserMaster usercode);

	public List<LSuserMaster> findByLssitemasterOrderByCreateddateDesc(LSSiteMaster lssitemaster);

	public List<LSuserMaster> findByusernameInAndLssitemaster(List<String> usermname,
			LSSiteMaster lssitemaster);

	public List<LSuserMaster> findByUsernameIgnoreCaseAndLoginfromOrderByCreateddateDesc(String username,
			String string);

	public Long countByLssitemasterAndUserstatus(LSSiteMaster sitemaster, String string);

	public List<LSuserMaster> findAllByOrderByCreateddateDesc();
	
	public LSuserMaster findByUsernameIgnoreCaseAndLssitemasterAndLoginfromAndIsadsuser(String username,
			LSSiteMaster objsiteobj, String string, int i);

	public List<LSuserMaster> findByUserretirestatus(int i);

	public Long countByUsercodeNotAndUserretirestatus(int i, int j);


	public List<LSuserMaster> findByUserretirestatusNotAndUsercodeInOrderByCreateddateDesc(int i,
			List<Integer> usercode);

	public List<LSuserMaster> findByUsernameIgnoreCaseAndUsercodeIn(String username, List<Integer> usercode);

	public List<LSuserMaster> findByUsernameIgnoreCaseAndUsercodeInAndUsercodeNot(String username, List<Integer> usercode,
			Integer usercode2);

	public List<LSuserMaster> findByUsernameIgnoreCaseAndUserretirestatusNot(String username, int i);


	public List<LSuserMaster> findByUsernameIgnoreCaseAndUsercodeInAndLoginfromAndUserretirestatusNot(String username,
			List<Integer> usercode, String string, int i);

	public List<LSuserMaster> findByUsernameIgnoreCaseAndUsercodeInAndUserretirestatusNot(String username,
			List<Integer> usercode, int i);

	public List<LSuserMaster> findByusername(String username);

	public LSuserMaster findByUsername(String notifyto);

	public LSuserMaster findByUsercode(Integer usercode);

	public List<LSuserMaster> findByUsercodeNot(int i);

	public Long countByUserstatus(String string);

	public Long countByUserretirestatusNot(int i);

//	@Transactional
//	@Modifying
//	@Query(value = "SELECT distinct usercode as usercode, username as username FROM lsusermaster WHERE usercode IN (?1)", nativeQuery = true)
//	List<Map<String, Object>> getUsernameAndUsercode(List<Integer> usercode);
//
	interface UserProjection {
	    Integer getUsercode();
	    String getUsername();
	}

	@Query(value = "SELECT DISTINCT usercode AS usercode, username AS username FROM lsusermaster WHERE usercode IN (?1)", nativeQuery = true)
	List<UserProjection> getUsernameAndUsercode(List<Integer> usercode);

	public Long countByUsernameIgnoreCaseAndAutenticatefromAndSubcode(String username, Integer autenticatefrom, String subcode);
	
	public LSuserMaster findByUsernameIgnoreCaseAndAutenticatefromAndSubcode(String username, Integer autenticatefrom, String subcode);

	public Usermaster findTop1ByUsernameIgnoreCaseAndAutenticatefrom(String username, Integer autenticatefrom);
	
	public Usermaster findTop1ByUsernameIgnoreCase(String username);
	
	@Transactional
	@Query(value = "SELECT count(*) FROM LSuserMaster u, LSMultisites s where s.usercode = u.usercode and u.userretirestatus <> 1 and s.lssitemaster_sitecode = ?1", nativeQuery = true)
	public Long getactiveusersonsite(Integer sitecode );
	public Long countByUserretirestatus(int i);
	@Transactional
	@Modifying
	@Query(value = "update LSuserMaster set userfullname = ?1 where usercode = ?2 " ,nativeQuery = true)
	public void updateProfile(String profile_name, Integer usercode);
	@Transactional
	@Modifying
	@Query(value = "update LSuserMaster set designationname = ?1 where usercode = ?2" ,nativeQuery = true)
	public void updateDestination(String destination_name, Integer usercode);
	
	@Transactional
	@Query(value = "select count(*) from lsusermaster where lssitemaster_sitecode = ?1 and userretirestatus != 1", nativeQuery = true)
	public Long GetActiveuser(int sitecode);
	
	@Transactional
	@Modifying
	@Query(value = "update LSuserMaster set getstart = ?1 where usercode = ?2" ,nativeQuery = true)
	public void GetStartSkipUpdate(String string, int i);

	public LSuserMaster findByUsercodeAndGetstart(Integer usercode, String string);

	public List<LSuserMaster> findByUsercodeIn(List<Integer> usercode);
	
	public LSuserMaster findTop1ByEmailid(String emailid);

	@Transactional
	@Modifying
	@Query(value = "update LSuserMaster set passwordstatus = ?1, forgetstatus = 1 where usercode = ?2" ,nativeQuery = true)
	public void UpdateForgetPassword(int i, Integer usercode);

	public Long countByUsernameIgnoreCaseAndSubcodeAndEmailidIgnoreCase(String username, String subcode,
			String emailid);

	public LSuserMaster findTop1ByEmailidAndAutenticatefrom(String emailid, int i);

	public LSuserMaster findTop1ByEmailidIgnoreCase(String emailid);

	public LSuserMaster findTop1ByEmailidIgnoreCaseAndAutenticatefrom(String emailid, int i);

	public List<LSuserMaster> findByusernameIn(List<String> userNameList);
	@Transactional
	@Query(value = "SELECT COUNT(DISTINCT u.usercode) \r\n"
			+ "FROM lsusermaster u\r\n"
			+ "JOIN lsmultisites m ON u.usercode = m.usercode\r\n"
			+ "JOIN lssitemaster s ON m.lssitemaster_sitecode = s.sitecode\r\n"
			+ "WHERE u.userretirestatus = 0 \r\n"
			+ "AND s.istatus = 1;", nativeQuery = true)
	public Long getTenantlicenseCount();

}