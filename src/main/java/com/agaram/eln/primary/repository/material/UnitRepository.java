package com.agaram.eln.primary.repository.material;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


import com.agaram.eln.primary.model.material.Unit;

public interface UnitRepository extends JpaRepository<Unit, Integer>{
	
	public List<Object> findByNstatus(Integer nstatus);

}
