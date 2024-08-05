package com.agaram.eln.primary.repository.reports.reportdesigner;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.agaram.eln.primary.model.reports.reportdesigner.ReportDesignerStructure;
import com.agaram.eln.primary.model.reports.reportdesigner.Reporttemplate;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;

public interface ReporttemplateRepository extends JpaRepository<Reporttemplate, Long> {
	public List<Reporttemplate> findByReportdesignstructure(ReportDesignerStructure reportdirstructure);

	public List<Reporttemplate> findBySitemasterAndViewoptionAndTemplatetypeAndCreatedbyInAndDateCreatedBetweenOrSitemasterAndViewoptionAndTemplatetypeAndCreatedbyAndDateCreatedBetweenOrSitemasterAndViewoptionAndTemplatetypeAndCreatedbyInAndDateCreatedBetweenOrderByTemplatecodeDesc(
			LSSiteMaster sitemaster, int i, Integer templatetype, List<LSuserMaster> lstuserMaster, Date fromdate,
			Date todate, LSSiteMaster sitemaster2, int j, Integer templatetype2, LSuserMaster createdby, Date fromdate2,
			Date todate2, LSSiteMaster sitemaster3, int k, Integer templatetype3, List<LSuserMaster> lstuserMaster2,
			Date fromdate3, Date todate3);

	public List<Reporttemplate> findBySitemasterAndViewoptionAndTemplatetypeAndCreatedbyInOrSitemasterAndViewoptionAndTemplatetypeAndCreatedbyOrSitemasterAndViewoptionAndTemplatetypeAndCreatedbyInOrderByTemplatecodeDesc(
			LSSiteMaster sitemaster, int i, Integer templatetype, List<LSuserMaster> lstuserMaster,
			LSSiteMaster sitemaster2, int j, Integer templatetype2, LSuserMaster createdby, LSSiteMaster sitemaster3,
			int k, Integer templatetype3, List<LSuserMaster> lstuserMaster2);

	public Reporttemplate findByTemplatecode(long l);

	public Reporttemplate findTopByTemplatenameIgnoreCaseAndSitemaster(String templatename, LSSiteMaster sitemaster);

	public List<Reporttemplate> findByTemplatecodeInAndTemplatetypeOrderByTemplatecodeDesc(List<Long> lstTempCode,
			int i);

	@Transactional
	@Modifying
	@Query(value="update Reporttemplate o set o.reportdesignstructure_directorycode = ?1 where o.templatecode in (?2)",nativeQuery=true)
	public void updatedirectory(Long directorycode, List<Long> lstfilesid);

	@Transactional
	@Modifying
	@Query(value="update Reporttemplate o set o.reportdesignstructure_directorycode = ?1 where o.templatecode = ?2",nativeQuery=true)
	void updatedirectoryonsinglefile(Long directorycode, Long long1);

	public Optional<Reporttemplate> findByTemplatenameIgnoreCaseAndSitemaster(String templatename,
			LSSiteMaster sitemaster);
	@Transactional
	@Modifying
	@Query(value = "UPDATE reporttemplate SET templatename = ?2, datemodified = ?3, reportdesignstructure_directorycode = ?4 WHERE templatecode = ?1", nativeQuery = true)
	public void updateTemplateDetails(Long templatecode, String templatename, Date currentUtcTime, Long directoryCode);

	public List<Reporttemplate> findBySitemasterAndViewoptionAndTemplatetypeAndCreatedbyInAndDateCreatedBetweenAndReportdesignstructureOrSitemasterAndViewoptionAndTemplatetypeAndCreatedbyAndDateCreatedBetweenAndReportdesignstructureOrSitemasterAndViewoptionAndTemplatetypeAndCreatedbyInAndDateCreatedBetweenAndReportdesignstructureOrderByTemplatecodeDesc(
			LSSiteMaster sitemaster, int i, Integer templatetype, List<LSuserMaster> lstuserMaster, Date fromdate,
			Date todate, ReportDesignerStructure objdir, LSSiteMaster sitemaster2, int j, Integer templatetype2,
			LSuserMaster createdby, Date fromdate2, Date todate2, ReportDesignerStructure objdir2,
			LSSiteMaster sitemaster3, int k, Integer templatetype3, List<LSuserMaster> lstuserMaster2, Date fromdate3,
			Date todate3, ReportDesignerStructure objdir3);

	public List<Reporttemplate> findBySitemasterAndViewoptionAndCreatedbyInAndDateCreatedBetweenAndReportdesignstructureOrSitemasterAndViewoptionAndCreatedbyAndDateCreatedBetweenAndReportdesignstructureOrSitemasterAndViewoptionAndCreatedbyInAndDateCreatedBetweenAndReportdesignstructureOrderByTemplatecodeDesc(
			LSSiteMaster sitemaster, int i, List<LSuserMaster> lstuserMaster, Date fromdate, Date todate,
			ReportDesignerStructure objdir, LSSiteMaster sitemaster2, int j, LSuserMaster createdby, Date fromdate2,
			Date todate2, ReportDesignerStructure objdir2, LSSiteMaster sitemaster3, int k,
			List<LSuserMaster> lstuserMaster2, Date fromdate3, Date todate3, ReportDesignerStructure objdir3);

	public List<Reporttemplate> findBySitemasterAndViewoptionAndCreatedbyInAndDateCreatedBetweenAndReportdesignstructureAndKeywordContainingIgnoreCaseOrSitemasterAndViewoptionAndCreatedbyAndDateCreatedBetweenAndReportdesignstructureAndKeywordContainingIgnoreCaseOrSitemasterAndViewoptionAndCreatedbyInAndDateCreatedBetweenAndReportdesignstructureAndKeywordContainingIgnoreCaseOrderByTemplatecodeDesc(
			LSSiteMaster sitemaster, int i, List<LSuserMaster> lstuserMaster, Date fromdate, Date todate,
			ReportDesignerStructure objdir, String searchKeyword, LSSiteMaster sitemaster2, int j,
			LSuserMaster createdby, Date fromdate2, Date todate2, ReportDesignerStructure objdir2,
			String searchKeyword2, LSSiteMaster sitemaster3, int k, List<LSuserMaster> lstuserMaster2, Date fromdate3,
			Date todate3, ReportDesignerStructure objdir3, String searchKeyword3);


}
