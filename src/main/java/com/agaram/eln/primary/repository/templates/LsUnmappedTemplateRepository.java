package com.agaram.eln.primary.repository.templates;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.agaram.eln.primary.model.templates.LsUnmappedTemplate;

@Repository
public interface LsUnmappedTemplateRepository extends JpaRepository<LsUnmappedTemplate, Integer> {
	public LsUnmappedTemplate findByTemplatename(String templatename);
}