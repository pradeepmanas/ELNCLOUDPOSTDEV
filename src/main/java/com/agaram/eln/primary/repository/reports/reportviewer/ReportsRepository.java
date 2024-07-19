package com.agaram.eln.primary.repository.reports.reportviewer;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.reports.reportviewer.ReportViewerStructure;
import com.agaram.eln.primary.model.reports.reportviewer.Reports;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;

public interface ReportsRepository extends JpaRepository<Reports, Long> {
	public List<Reports> findByReportviewerstructure(ReportViewerStructure reportviewerstructure);

	public Optional<Reports> findByReportnameIgnoreCase(String reportname);

	public List<Reports> findBySitemasterAndViewoptionAndTemplatetypeAndCreatedbyInAndDatecreatedBetweenOrSitemasterAndViewoptionAndTemplatetypeAndCreatedbyAndDatecreatedBetweenOrSitemasterAndViewoptionAndTemplatetypeAndCreatedbyInAndDatecreatedBetweenOrderByReportcodeDesc(
			LSSiteMaster sitemaster, int i, Integer templatetype, List<LSuserMaster> lstuserMaster, Date fromdate,
			Date todate, LSSiteMaster sitemaster2, int j, Integer templatetype2, LSuserMaster createdby, Date fromdate2,
			Date todate2, LSSiteMaster sitemaster3, int k, Integer templatetype3, List<LSuserMaster> lstuserMaster2,
			Date fromdate3, Date todate3);

	public Reports findTopByReportnameIgnoreCaseAndSitemaster(String reportname, LSSiteMaster sitemaster);
}