package com.agaram.eln.primary.repository.instrumentDetails;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.agaram.eln.primary.model.instrumentDetails.LSsheetfolderfiles;

public interface LSsheetfolderfilesRepository  extends JpaRepository<LSsheetfolderfiles,Integer>{
 public List<LSsheetfolderfiles> findByDirectorycodeAndFileforOrderByFolderfilecode(Long directory, String filefor);
 public Long countByDirectorycodeAndFileforAndFilename(Long directory, String filefor, String filename);
 
 @Transactional
 public Long deleteByUuid(String uuid);
 
 public LSsheetfolderfiles findByUuid(String uuid);
 public List<LSsheetfolderfiles> findByUuidInOrderByFolderfilecode(List<String> lstuuid);
 
 @Transactional
	@Modifying
	@Query("update LSsheetfolderfiles o set o.directorycode = ?1 where o.folderfilecode = ?2")
	void updatedirectory(Long directorycode , Integer folderfilecode);
 
 @Transactional
	@Modifying
	@Query("update LSsheetfolderfiles o set o.directorycode = ?1 where o.folderfilecode in (?2)")
	void updatedirectory(Long directorycode , List<Integer> folderfilecode);

 @Transactional 
public void deleteByUuidIn(List<String> lstfilesid);
public List<LSsheetfolderfiles> findByDirectorycodeAndFileforAndCreatedtimestampBetweenOrderByFolderfilecode(
		Long directorycode, String filefor, Date fromdate, Date todate);
@Transactional
	@Modifying
	@Query("delete from LSsheetfolderfiles where uuid in(?1)")
public void removeForFile(List<String> uuid);
public List<LSsheetfolderfiles> findByDirectorycode(long directorycode);
public List<LSsheetfolderfiles> findByDirectorycodeAndFilefor(long directorycode, String filefor);
public List<LSsheetfolderfiles> findByDirectorycodeInAndFilenameLikeIgnoreCaseOrderByFolderfilecode(
		List<Long> directoryCode_Sheet, String search_Key);
public long countByDirectorycodeInAndFilenameLikeIgnoreCaseOrderByFolderfilecode(List<Long> directoryCode_Sheet,
		String search_Key);

public LSsheetfolderfiles findFirst1ByDirectorycodeAndFilenameOrderByFolderfilecode(Long directorycode, String filename);
 

}
