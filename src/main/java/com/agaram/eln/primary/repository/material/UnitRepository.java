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

	public Unit findByNstatusAndSunitnameAndNsitecode(int i, String sunitname, Integer nsitecode);

	public List<Unit> findByNstatusAndNsitecodeOrderByNunitcodeDesc(int i, Integer nsiteInteger);

	public List<Object> findByNstatusAndNsitecode(int i, Integer nsiteInteger);

	public List<Object> findByNstatusAndNsitecodeAndNunitcodeOrderByNunitcode(int i, Integer nsiteInteger,
			int parseInt);

	public List<Unit> findBySunitnameInAndNstatusAndNsitecode(List<String> lstUnitname, int i, Integer sitecode);

	public List<Unit> findByNsitecodeOrderByNunitcodeDesc(int i, Integer nsiteInteger);

	public Unit findBySunitnameAndNsitecode(String sunitname, Integer nsitecode);

	public List<Unit> findByNsitecodeOrderByNunitcodeDesc(Integer nsiteInteger);

	public Unit findByNunitcodeAndNsitecode(Integer nunitcode, Integer nsitecode);

	public Unit findByNunitcode(Integer nunitcode);

	public Unit findByNunitcodeAndNsitecodeAndNstatus(Integer nunitcode, Integer nsitecode, int i);

	public Unit findBySunitnameIgnoreCaseAndNsitecode(String sunitname, Integer nsitecode);

	public List<Unit> findByNsitecodeAndNstatusOrderByNunitcodeDesc(Integer nsiteInteger, int i);

}
