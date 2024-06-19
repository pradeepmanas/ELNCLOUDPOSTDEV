package com.agaram.eln.primary.repository.usermanagement;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.agaram.eln.primary.fetchmodel.getmasters.Projectmaster;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.usermanagement.LSprojectmaster;
import com.agaram.eln.primary.model.usermanagement.LSusersteam;

public interface LSprojectmasterRepository extends JpaRepository<LSprojectmaster, Integer> {
	public LSprojectmaster findByProjectname(String projectname);

	public List<LSprojectmaster> findBystatus(Integer status);

	public LSprojectmaster findByProjectnameAndStatus(String projectname, Integer status);

	public LSprojectmaster findByProjectnameAndStatusAndProjectcodeNot(String projectname, Integer status,
			Integer Projectcode);

	public List<LSprojectmaster> findByLsusersteamIn(List<LSusersteam> lsusersteam);

	public List<Integer> findProjectcodeByLsusersteamIn(List<LSusersteam> lsusersteam);

	public List<LSprojectmaster> findByLsusersteam(LSusersteam lsusersteam);

	public List<LSprojectmaster> findProjectcodeAndProjectnameBystatusAndLssitemaster(Integer status,
			LSSiteMaster lssitemaster);

	public List<Projectmaster> findBystatusAndLssitemaster(Integer status, LSSiteMaster lssitemaster);

	public LSprojectmaster findByProjectnameAndStatusAndLssitemaster(String projectname, Integer status,
			LSSiteMaster lssitemaster);

	public LSprojectmaster findByProjectnameAndStatusAndProjectcodeNotAndLssitemaster(String projectname,
			Integer status, Integer Projectcode, LSSiteMaster lssitemaster);

	public LSprojectmaster findByProjectnameIgnoreCaseAndStatusAndLssitemaster(String projectname, Integer status,
			LSSiteMaster lssitemaster);

	public LSprojectmaster findByProjectnameIgnoreCaseAndStatusAndProjectcodeNotAndLssitemaster(String projectname,
			Integer status, Integer projectcode, LSSiteMaster lssitemaster);

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

	public List<LSprojectmaster> findByLsusersteamInAndStatusAndLssitemaster(
			List<LSusersteam> findByLsuserteammappingInAndStatus, int i, LSSiteMaster lssitemaster);
	public interface ProjectOrTaskOrMaterialView {
	    String getSourceTable();
	    String getName();
	}

	@Transactional
    @Query(value = "SELECT  projectname AS name, 'lsprojectmaster' AS sourceTable FROM lsprojectmaster WHERE LOWER(projectname) LIKE LOWER(?1) AND lssitemaster_sitecode = ?2 AND status=1"
                 + "UNION "
                 + "SELECT testname AS name,'lstestmasterlocal' AS sourceTable FROM lstestmasterlocal WHERE LOWER(testname) LIKE LOWER(?1) AND lssitemaster_sitecode = ?2 AND teststatus='A' "
                 + "UNION "
                 + "SELECT smaterialname AS name,'elnmaterial' AS sourceTable  FROM elnmaterial WHERE LOWER(smaterialname) LIKE LOWER(?1) AND nsitecode = ?2 AND nstatus=1", 
           nativeQuery = true)
    List<ProjectOrTaskOrMaterialView> getProjectOrTaskOrMaterialSearchBased(String searchkey, Integer sitecode);

	public List<LSprojectmaster> findByLsusersteamInAndStatusAndLssitemasterOrderByProjectcodeDesc(
			List<LSusersteam> findByLsuserteammappingInAndStatusAndLssitemaster, int i, LSSiteMaster lssitemaster);
}
