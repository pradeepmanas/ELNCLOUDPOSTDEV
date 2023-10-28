package com.agaram.eln.primary.repository.material;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.material.Period;

public interface PeriodRepository extends JpaRepository<Period, Integer>{
	public List<Object> findByNstatus(Integer nstatus);
	
	public List<Period> findByNstatusOrderByNperiodcode(Integer nstatus);
}
