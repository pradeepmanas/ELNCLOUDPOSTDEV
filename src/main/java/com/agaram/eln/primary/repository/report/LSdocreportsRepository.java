package com.agaram.eln.primary.repository.report;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.report.LSdocreports;

public interface LSdocreportsRepository extends JpaRepository<LSdocreports, Integer>{

	@SuppressWarnings("unchecked")
	LSdocreports save(LSdocreports obj);
	
	void delete(LSdocreports obj);
	
	List<LSdocreports> findAllByFileHashName(String fileHashName);
	
	List<LSdocreports> findAllByFileName(String fileName);
	
	List<LSdocreports> findAll();
	
	List<LSdocreports> findByStatus(Integer status);
	
//	List<LSdocreports> findByStatusAndLssitemaster(Integer status, LSSiteMaster lssitemaster);

	List<LSdocreports> findByStatusAndLssitemaster(Integer status, Integer lssitemaster);
	
	LSdocreports findByDocReportsCode(Integer docReportsCode);
	
	List<LSdocreports> findAllByFileNameAndStatus(String fileName, Integer status);
	
	List<LSdocreports> findAllByFileNameAndDocdirectorycodeAndStatus(String fileName, Integer docdirectorycode, Integer status);
	
	List<LSdocreports> findAllByFileHashNameAndStatus(String fileHashName, Integer status);
	
	LSdocreports findFirstByFileHashNameAndStatus(String fileHashName, Integer status);
	
	List<LSdocreports> findByIsTemplateAndStatus(Integer isTemplate, Integer status);
	
//	List<LSdocreports> findByIsTemplateAndStatusAndLssitemaster(Integer isTemplate, Integer status, LSSiteMaster lssitemaster);
	List<LSdocreports> findByIsTemplateAndStatusAndLssitemaster(Integer isTemplate, Integer status, Integer lssitemaster);
//	List<LSdocreports> findAllByPrevFileNameAndStatus(String prevFileName, Integer status);
	
//	List<LSdocreports> findLastBySheetfilecodeAndStatus(Integer Sheetfilecode, Integer Status);
	
	LSdocreports findFirstBySheetfilecodeStringAndStatusOrderByCreatedateDesc(String SheetfilecodeString, Integer Status);
	
	LSdocreports findFirstBySheetfilecodeStringAndStatusAndStreamidIsNotNullOrderByDocReportsCodeDesc(String SheetfilecodeString, Integer Status);
	
	List<LSdocreports> findByIsdraftAndStatus(Integer isDraft, Integer Status);
	
	List<LSdocreports> findByIsreportAndLssitemaster(Integer isreport, Integer lssitemaster);
	
	List<LSdocreports> findAllByIsTemplateAndIsdraftAndSheetfilecodeStringAndIsmultiplesheetAndLssitemasterAndFileNameIsNotNull(Integer istemplate, Integer isdraft, String SheetfilecodeString, Integer ismultiplesheet, Integer lssite);

	List<LSdocreports> findAllByIsTemplateAndIsdraftAndSheetfilecodeStringAndIsmultiplesheetAndLssitemasterAndFileNameIsNotNullOrderByCreatedateDesc(
			int i, int j, String sheetCode, int ismultiplesheet, int sitecode);
	
//	LSdocreports findFirstByFileHashNameAndStatusOrderByVersionnoDesc(String fileHashName, Integer status);
}
