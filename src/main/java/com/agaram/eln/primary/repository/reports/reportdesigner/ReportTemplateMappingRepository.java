package com.agaram.eln.primary.repository.reports.reportdesigner;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.reports.reportdesigner.ReportTemplateMapping;
import com.agaram.eln.primary.model.usermanagement.LSprojectmaster;

public interface ReportTemplateMappingRepository extends JpaRepository<ReportTemplateMapping, Long>{
	@Transactional
	void deleteByTemplatecode(Long templatecode);

	List<ReportTemplateMapping> findByLsprojectmaster(LSprojectmaster lSprojectmaster);

}
