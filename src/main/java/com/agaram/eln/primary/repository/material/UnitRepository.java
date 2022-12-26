package com.agaram.eln.primary.repository.material;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


import com.agaram.eln.primary.model.material.Unit;

public interface UnitRepository extends JpaRepository<Unit, Integer>{
	
	public List<Object> findByNstatus(Integer nstatus);
	
	public List<Unit> findByNstatusOrderByNunitcodeDesc(Integer nstatus);

	public List<Unit> findBySunitnameAndNstatus(String sunitname, Integer nstatus);

	public Unit findByNunitcodeAndNstatus(int nunitCode, int i);

	public Unit findByNunitcodeAndSunitnameAndNstatus(Integer nunitcode, String sunitname, int i);

	public List<Object> findByNstatusAndNunitcodeOrderByNunitcode(int i, int parseInt);

	public Unit findByNstatusAndSunitname(int i, String sunitname);

}
