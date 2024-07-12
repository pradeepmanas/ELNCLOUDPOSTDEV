package com.agaram.eln.primary.repository.reports.reportdesigner;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.reports.reportdesigner.ReportDesignerStructure;
import com.agaram.eln.primary.model.reports.reportdesigner.Reporttemplate;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;

public interface ReporttemplateRepository extends JpaRepository<Reporttemplate, Long> {
	public List<Reporttemplate> findByReportdesignstructure(ReportDesignerStructure reportdirstructure);

	public Optional<Reporttemplate> findByTemplatenameIgnoreCase(String templatename);

	Reporttemplate findByTemplatenameIgnoreCaseAndSitemaster(String templatename, LSSiteMaster sitemaster);

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


}
