package com.agaram.eln.primary.repository.instrumentDetails;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.agaram.eln.primary.model.instrumentDetails.LSsheetfolderfiles;

public interface LSsheetfolderfilesRepository  extends JpaRepository<LSsheetfolderfiles,Integer>{
 public List<LSsheetfolderfiles> findByDirectorycodeAndFileforOrderByFolderfilecode(Long directory, String filefor);
 public Long countByDirectorycodeAndFileforAndFilename(Long directory, String filefor, String filename);
 
 @Transactional
 public Long deleteByUuid(String uuid);
 
 public LSsheetfolderfiles findByUuid(String uuid);
 public List<LSsheetfolderfiles> findByUuidInOrderByFolderfilecode(List<String> lstuuid);
}
