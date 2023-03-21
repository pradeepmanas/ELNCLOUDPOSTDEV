package com.agaram.eln.primary.repository.material;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.material.MaterialGrade;

public interface MaterialGradeRepository  extends JpaRepository<MaterialGrade, Integer>{
	public List<Object> findByNstatusAndNsitecode(Integer nstatus, Integer nsitecode);
	public List<MaterialGrade> findByNstatusAndNsitecodeOrderByNmaterialgradecode(Integer nstatus, Integer nsitecode);
	public MaterialGrade findBySmaterialgradenameAndNsitecode(String smaterialgradename, Integer nsitecode);
	public MaterialGrade findByNmaterialgradecode(int nunitCode);
	public List<MaterialGrade> findByNstatusAndNsitecodeOrderByNmaterialgradecodeDesc(int i, Integer nsiteInteger);
}