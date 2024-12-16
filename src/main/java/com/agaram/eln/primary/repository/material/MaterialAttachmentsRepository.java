package com.agaram.eln.primary.repository.material;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.material.MaterialAttachments;

public interface MaterialAttachmentsRepository extends JpaRepository<MaterialAttachments, Integer> {

	List<MaterialAttachments> findByNmaterialcode(Integer nmaterialcatcode);

	public MaterialAttachments findByFileid(String fileid);

}
