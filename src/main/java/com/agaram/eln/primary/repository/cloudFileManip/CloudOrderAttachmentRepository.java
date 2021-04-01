package com.agaram.eln.primary.repository.cloudFileManip;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.cloudFileManip.CloudOrderAttachment;
public interface CloudOrderAttachmentRepository extends JpaRepository<CloudOrderAttachment, Integer> {
	public CloudOrderAttachment findById(int Id);
	public Long deleteById(int Id);
}
