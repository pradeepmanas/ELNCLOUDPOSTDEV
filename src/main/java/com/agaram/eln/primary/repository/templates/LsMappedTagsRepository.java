package com.agaram.eln.primary.repository.templates;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.agaram.eln.primary.model.templates.LsMappedTags;

public interface LsMappedTagsRepository extends JpaRepository<LsMappedTags,Integer>{
	
	List<LsMappedTags> findByTemplatecode(Integer templatecode);
	
	@Transactional
	@Modifying
	@Query("delete LsMappedTags where templatecode = ?1")
	void deleteByTemplatecode(Integer templatecode);

}
