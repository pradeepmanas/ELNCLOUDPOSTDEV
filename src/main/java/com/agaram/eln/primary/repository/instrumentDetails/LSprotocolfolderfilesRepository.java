package com.agaram.eln.primary.repository.instrumentDetails;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.agaram.eln.primary.model.instrumentDetails.LSprotocolfolderfiles;

public interface LSprotocolfolderfilesRepository extends JpaRepository<LSprotocolfolderfiles, Integer> {

	public Long countByDirectorycodeAndFileforAndFilename(Long directory, String filefor, String filename);

	public List<LSprotocolfolderfiles> findByDirectorycodeAndFileforOrderByFolderfilecode(Long directorycode,
			String filefor);

	public Object findByUuid(String uid);

	@Transactional
	public Long deleteByUuid(String uuid);

	@Transactional
	@Modifying
	@Query("update LSprotocolfolderfiles o set o.directorycode = ?1 where o.folderfilecode in (?2)")
	void updatedirectory(Long directorycode, List<Integer> folderfilecode);

	 @Transactional
		@Modifying
		@Query("update LSprotocolfolderfiles o set o.directorycode = ?1 where o.folderfilecode = ?2")
		void updatedirectory(Long directorycode , Integer folderfilecode);

}
