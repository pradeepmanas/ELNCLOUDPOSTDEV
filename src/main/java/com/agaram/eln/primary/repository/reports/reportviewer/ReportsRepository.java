package com.agaram.eln.primary.repository.reports.reportviewer;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.reports.reportdesigner.Reporttemplate;
import com.agaram.eln.primary.model.reports.reportviewer.ReportViewerStructure;
import com.agaram.eln.primary.model.reports.reportviewer.Reports;

public interface ReportsRepository extends JpaRepository<Reports, Long> {
	public List<Reports> findByReportviewerstructure(ReportViewerStructure reportviewerstructure);

	public Optional<Reports> findByReportnameIgnoreCase(String reportname);
}