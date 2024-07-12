package com.agaram.eln.primary.repository.reports.reportdesigner;
import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.reports.reportdesigner.ReportTemplateMapping;

public interface ReportTemplateMappingRepository extends JpaRepository<ReportTemplateMapping, Long>{
	@Transactional
	void deleteByTemplatecode(Long templatecode);

}
