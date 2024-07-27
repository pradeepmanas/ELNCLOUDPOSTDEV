package com.agaram.eln.primary.repository.instrumentDetails;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.instrumentDetails.LsOrderattachments;

public interface LsOrderattachmentsRepository extends JpaRepository<LsOrderattachments, Long> {
	@Transactional
	public Long deleteByAttachmentcode(Long attachmentcode);

	public LsOrderattachments findFirst1ByfileidOrderByAttachmentcodeDesc(String fileid);
	public List<LsOrderattachments> findByBatchcodeOrderByAttachmentcodeDesc(Long batchcode);

	public List<LsOrderattachments> findByNmaterialcodeOrderByAttachmentcodeDesc(Object object);

	public List<LsOrderattachments> findByNmaterialinventorycodeOrderByAttachmentcodeDesc(Object object);
	
	public LsOrderattachments findFirst1ByFilenameAndBatchcodeOrderByAttachmentcodeDesc(String filename,Long batchcode);
}
