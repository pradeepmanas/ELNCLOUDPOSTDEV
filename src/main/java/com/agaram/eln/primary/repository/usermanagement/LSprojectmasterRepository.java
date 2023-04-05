package com.agaram.eln.primary.repository.usermanagement;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.fetchmodel.getmasters.Projectmaster;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.usermanagement.LSprojectmaster;
import com.agaram.eln.primary.model.usermanagement.LSusersteam;

public interface LSprojectmasterRepository extends JpaRepository<LSprojectmaster, Integer> {
	public LSprojectmaster findByProjectname(String projectname);
	public List<LSprojectmaster> findBystatus(Integer status);
	public LSprojectmaster findByProjectnameAndStatus(String projectname, Integer status);
	public LSprojectmaster findByProjectnameAndStatusAndProjectcodeNot(String projectname, Integer status, Integer Projectcode);
	public List<LSprojectmaster> findByLsusersteamIn(List<LSusersteam> lsusersteam);
	public List<Integer> findProjectcodeByLsusersteamIn(List<LSusersteam> lsusersteam);
	
	public List<LSprojectmaster> findByLsusersteam(LSusersteam lsusersteam);
	public List<LSprojectmaster> findProjectcodeAndProjectnameBystatusAndLssitemaster(Integer status,LSSiteMaster lssitemaster);
	public List<Projectmaster> findBystatusAndLssitemaster(Integer status,LSSiteMaster lssitemaster);
	public LSprojectmaster findByProjectnameAndStatusAndLssitemaster(String projectname, Integer status,LSSiteMaster lssitemaster);
	public LSprojectmaster findByProjectnameAndStatusAndProjectcodeNotAndLssitemaster(String projectname, Integer status, Integer Projectcode,LSSiteMaster lssitemaster);
	public LSprojectmaster findByProjectnameIgnoreCaseAndStatusAndLssitemaster(String projectname, Integer status,
			LSSiteMaster lssitemaster);
	public LSprojectmaster findByProjectnameIgnoreCaseAndStatusAndProjectcodeNotAndLssitemaster(String projectname, Integer status,
			Integer projectcode, LSSiteMaster lssitemaster);
	public List<LSprojectmaster> findProjectcodeAndProjectnameBystatusAndLssitemaster(int status,
			LSSiteMaster lssitemaster);
	public List<LSprojectmaster> findProjectcodeAndProjectnameByLssitemasterIn(List<LSprojectmaster> lsproject);
	public List<LSprojectmaster> findByLssitemaster(LSSiteMaster lssitemaster);

	public Object findByProjectnameIgnoreCaseAndLssitemaster(String projectname, LSSiteMaster lssitemaster);
	public List<LSprojectmaster> findByLssitemasterAndStatus(LSSiteMaster lssitemaster, int i);
	public List<LSprojectmaster> findByLssitemasterAndStatusOrderByProjectcodeDesc(LSSiteMaster lssitemaster, int i);
	public List<LSprojectmaster> findByLssitemasterOrderByProjectcodeDesc(LSSiteMaster lssitemaster);
	public List<LSprojectmaster> findByLsusersteamInAndStatus(List<LSusersteam> teamlist, int i);
	public LSprojectmaster findByProjectcode(Integer projectcode);
	
	
}
