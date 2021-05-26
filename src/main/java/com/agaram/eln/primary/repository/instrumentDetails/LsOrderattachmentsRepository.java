package com.agaram.eln.primary.repository.instrumentDetails;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.instrumentDetails.LsOrderattachments;

public interface LsOrderattachmentsRepository extends JpaRepository<LsOrderattachments, Long> {
	@Transactional
	public Long deleteByAttachmentcode(Long attachmentcode);

	public LsOrderattachments findFirst1ByfileidOrderByAttachmentcodeDesc(String fileid);
}
