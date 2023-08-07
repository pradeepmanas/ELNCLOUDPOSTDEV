package com.agaram.eln.primary.repository.reports.reportdesigner;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.reports.reportdesigner.ReportDesignerStructure;
import com.agaram.eln.primary.model.reports.reportdesigner.Reporttemplate;

public interface ReporttemplateRepository extends JpaRepository<Reporttemplate, Long> {
	public List<Reporttemplate> findByReportdesignstructure(ReportDesignerStructure reportdirstructure);

	public Optional<Reporttemplate> findByTemplatenameIgnoreCase(String templatename);
}
