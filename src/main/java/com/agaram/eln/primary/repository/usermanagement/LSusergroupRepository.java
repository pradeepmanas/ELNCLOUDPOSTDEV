package com.agaram.eln.primary.repository.usermanagement;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.usermanagement.LSusergroup;

public interface LSusergroupRepository extends JpaRepository<LSusergroup, Integer> {
	public List<LSusergroup> findByusergroupnameNotOrderByUsergroupcodeDesc(String usergroupname);

	public List<LSusergroup> findByusergroupstatusAndUsergroupnameNot(String usergroupstatus, String usergroupname);

//	public List<LSusergroup> findBylssitemasterAndUsergroupnameNot(LSSiteMaster lssitemaster,String usergroupname);
	public List<LSusergroup> findBylssitemasterAndUsergroupnameNot(Integer lssitemaster, String usergroupname);

	public LSusergroup findByusergroupname(String usergroupname);

//	public LSusergroup findByusergroupnameAndLssitemaster(String usergroupname, LSSiteMaster lssitemaster);
	public LSusergroup findByusergroupnameAndLssitemaster(String usergroupname, Integer lssitemaster);

	public List<LSusergroup> findByOrderByUsergroupcodeDesc();

//	public List<LSusergroup> findBylssitemasterAndUsergroupnameNotOrderByUsergroupcodeDesc(LSSiteMaster objclass,
//			String string);
	public List<LSusergroup> findBylssitemasterAndUsergroupnameNotOrderByUsergroupcodeDesc(Integer objclass,
			String string);

	public LSusergroup findByusergroupnameIgnoreCaseAndLssitemaster(String usergroupname, Integer lssitemaster);

	public List<LSusergroup> findByusergroupnameNotOrderByUsergroupcodeAsc(String usergroupname);

	public List<LSusergroup> findBylssitemasterAndUsergroupnameNotOrderByUsergroupcodeAsc(Integer sitecode,
			String usergroupname);

	public List<LSusergroup> findByusergroupstatusAndUsergroupnameNotOrderByUsergroupcodeAsc(String usergroupstatus,
			String usergroupname);

	public List<LSusergroup> findByUsergroupnameNotOrderByUsergroupcodeAsc(String usergroupname);

	public List<LSusergroup> findBylssitemasterAndUsergroupstatusAndUsergroupnameNotOrderByUsergroupcodeAsc(
			Integer sitecode, String string, String string2);

	public Object findByusergroupnameIgnoreCaseAndUsergroupcodeNotAndLssitemaster(String usergroupname,
			Integer usergroupcode, Integer lssitemaster);

	public List<LSusergroup> findByUsergroupnameNotOrderByUsergroupcodeDesc(String string);

	public List<LSusergroup> findBylssitemasterOrderByUsergroupcodeDesc(Integer sitecode);

}
