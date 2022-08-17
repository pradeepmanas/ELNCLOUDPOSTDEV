package com.agaram.eln.primary.repository.material;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.material.Section;

public interface SectionRepository  extends JpaRepository<Section, Integer>{
	public List<Object> findByNstatus(Integer nstatus);
}
