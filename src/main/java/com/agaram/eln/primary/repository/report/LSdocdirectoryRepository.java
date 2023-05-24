package com.agaram.eln.primary.repository.report;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.report.LSdocdirectory;



public interface LSdocdirectoryRepository extends JpaRepository<LSdocdirectory, Integer>{
	
	@SuppressWarnings("unchecked")
	LSdocdirectory save(LSdocdirectory ObjLSDocDirectory);
	
	List<LSdocdirectory> findAll();
	
	List<LSdocdirectory> findByStatus(Integer status);
	
//	List<LSdocdirectory> findByStatusAndDirectorynameIsNotNULL(Integer status);
	
	List<LSdocdirectory> findByDirectorynameAndStatus(String directoryname, Integer status);
	List<LSdocdirectory> findByDirectorynameAndParentdirectoryAndStatus(String directoryname, Integer parentdir, Integer status);
	
	List<LSdocdirectory> findByDirectorynameAndCreatedbyAndStatus(String directoryname, Integer createdby, Integer status);
	List<LSdocdirectory> findByDirectorynameAndParentdirectoryAndCreatedbyAndStatus(String directoryname, Integer parentdir, Integer createdby, Integer status);

	LSdocdirectory findFirstByDirectorynameAndStatus(String directoryname, Integer status);
	
	LSdocdirectory findByDocdirectorycode(Integer code);
	
//	List<LSdocdirectory> findByStatusAndLssitemaster(Integer status, LSSiteMaster lssite);
	List<LSdocdirectory> findByStatusAndLssitemaster(Integer status, Integer lssite);
	
}
