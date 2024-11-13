package com.agaram.eln.primary.repository.reports.reportdesigner;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.agaram.eln.primary.model.reports.reportdesigner.ReportDesignerStructure;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;

public interface ReportDesignerStructureRepository extends JpaRepository<ReportDesignerStructure,Long> {
	ReportDesignerStructure findByDirectorycodeAndParentdircodeAndDirectorynameNot(Long directorycode, Long parentdircode,
			String directoryname);
	ReportDesignerStructure findByParentdircodeAndDirectoryname(Long parentdircode, String directoryname);
	List<ReportDesignerStructure> findBySitemasterAndViewoptionOrCreatedbyAndViewoptionOrderByDirectorycode(LSSiteMaster site,Integer siteviewopt,LSuserMaster createduser,Integer userviewopt);
	
	List<ReportDesignerStructure> findBySitemasterAndViewoptionOrCreatedbyAndViewoptionOrSitemasterAndViewoptionAndCreatedbyInOrderByDirectorycode(
			LSSiteMaster lssitemaster, int i, LSuserMaster lsuserMaster, int j, LSSiteMaster lssitemaster2, int k,
			List<LSuserMaster> lstuserMaster);
	@Transactional
	@Modifying
	@Query("update ReportDesignerStructure o set o.parentdircode = ?1, o.path = ?2, o.directoryname = ?4 where o.directorycode = ?3")
	void updatedirectory(Long parentdircode, String path, Long directorycode, String directoryname);
	
	@Transactional
	@Modifying
	@Query("update ReportDesignerStructure o set o.directorycode = ?1 where o.directorycode = ?2")
	void updateparentdirectory(Long dircodetomove, Long directorycode);
	ReportDesignerStructure findByDirectorycodeAndParentdircodeAndDirectorynameIgnoreCaseNot(Long directorycode,
			Long parentdircode, String directoryname);
	ReportDesignerStructure findByParentdircodeAndDirectorynameIgnoreCase(Long parentdircode, String directoryname);
}
