package com.agaram.eln.primary.repository.material;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.material.MaterialGrade;

public interface MaterialGradeRepository  extends JpaRepository<MaterialGrade, Integer>{
	public List<Object> findByNstatus(Integer nstatus);
}