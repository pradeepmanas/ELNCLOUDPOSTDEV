package com.agaram.eln.primary.repository.reports.reportdesigner;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.reports.reportdesigner.ReportTemplateVersion;

public interface ReportTemplateVersionRepository extends JpaRepository<ReportTemplateVersion, Integer>{

}
