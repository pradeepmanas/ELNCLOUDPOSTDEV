package com.agaram.eln.primary.repository.templates;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.agaram.eln.primary.model.templates.LsMappedTemplate;

@Repository
public interface LsMappedTemplateRepository extends JpaRepository<LsMappedTemplate, Integer>{
	
	public LsMappedTemplate findByTemplatename(String templatename);
	
}
