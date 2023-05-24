package com.agaram.eln.primary.repository.templates;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.agaram.eln.primary.model.templates.LsUnmappedTags;

@Repository
public interface LsUnmappedTagsRepository extends JpaRepository<LsUnmappedTags, Integer>{
	List<LsUnmappedTags> findByTemplatecode(Integer templatecode);
	
	@Transactional
	@Modifying
	@Query("delete LsUnmappedTags where templatecode = ?1")
	void deleteByTemplatecode(Integer templatecode);
}
