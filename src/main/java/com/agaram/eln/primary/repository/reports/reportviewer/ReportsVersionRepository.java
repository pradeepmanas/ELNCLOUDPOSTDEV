package com.agaram.eln.primary.repository.reports.reportviewer;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.reports.reportviewer.ReportsVersion;

public interface ReportsVersionRepository extends JpaRepository<ReportsVersion, Long> {

	ReportsVersion findByVersionnoAndReportcode(Integer versionno, Long reportcode);

}
