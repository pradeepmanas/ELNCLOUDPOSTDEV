package com.agaram.eln.primary.repository.sheetManipulation;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.agaram.eln.primary.fetchmodel.getmasters.Samplemaster;
import com.agaram.eln.primary.model.sheetManipulation.LSsamplemaster;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;

public interface LSsamplemasterRepository extends JpaRepository<LSsamplemaster, Integer>{
	public LSsamplemaster findBySamplename(String samplename);
	public List<LSsamplemaster> findBystatus(Integer status);
	public LSsamplemaster findBySamplenameAndStatus(String samplename, Integer status);
	public LSsamplemaster findBySamplenameAndStatusAndSamplecodeNot(String samplename, Integer status, Integer samplecode);
	
	public List<Samplemaster> findBystatusAndLssitemaster(Integer status,LSSiteMaster lssitemaster);
	public List<LSsamplemaster> findSamplecodeAndSamplenameBystatusAndLssitemaster(Integer status,LSSiteMaster lssitemaster);
	public LSsamplemaster findBySamplenameAndStatusAndLssitemaster(String samplename, Integer status,LSSiteMaster lssitemaster);
	public LSsamplemaster findBySamplenameAndStatusAndSamplecodeNotAndLssitemaster(String samplename, Integer status, Integer samplecode,LSSiteMaster lssitemaster);
	public LSsamplemaster findBySamplenameIgnoreCaseAndStatusAndSamplecodeNotAndLssitemaster(String samplename,Integer status,
			Integer samplecode, LSSiteMaster lssitemaster);
	public LSsamplemaster findBySamplenameIgnoreCaseAndStatusAndLssitemaster(String samplename, Integer status,
			LSSiteMaster lssitemaster);
	public List<Samplemaster> findBystatusAndLssitemasterOrderBySamplecodeDesc(int i, LSSiteMaster lssitemaster);
//	public List<LSsamplemaster> findByLssitemasterAndStatus(LSSiteMaster lssitemaster, int i);
	public List<Samplemaster> findByLssitemasterOrderBySamplecodeDesc(LSSiteMaster lssitemaster);
	public List<Samplemaster> findBySamplenameIgnoreCaseAndLssitemaster(String trim, LSSiteMaster lssitemaster);
	public List<Samplemaster> findBySamplenameIgnoreCaseAndSamplecodeNotAndLssitemaster(String trim, Integer samplecode,
			LSSiteMaster lssitemaster);
	
	@Transactional
	@Modifying
	 @Query(value = "SELECT DISTINCT m.samplecode  " +
             "FROM LSsamplemaster m " +
             "JOIN lslogilablimsorderdetail d ON m.samplecode = d.lssamplemaster_samplecode " +
             "WHERE m.lssitemaster_sitecode = ?1 " +
             "AND m.status = ?2", nativeQuery = true)
List<Integer> getDistinctByLssitemasterSitecodeAndStatus(int siteCode, int status);

}
