package com.agaram.eln.primary.repository.methodsetup;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.methodsetup.ELNFileAttachments;

public interface ELNFileAttachmentsRepository extends JpaRepository<ELNFileAttachments, Long> {
	@Transactional
	public Long deleteByAttachmentcode(Long attachmentcode);

	public ELNFileAttachments findFirst1ByfileidOrderByAttachmentcodeDesc(String fileid) ;
	
	public List<ELNFileAttachments> findByBatchcodeOrderByAttachmentcodeDesc(Long batchcode);
}
