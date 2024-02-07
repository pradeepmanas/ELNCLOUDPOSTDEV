package com.agaram.eln.primary.repository.material;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.material.Manufacturer;

public interface ManufacturerRepository  extends JpaRepository<Manufacturer, Integer>{
	List<Manufacturer> findByNstatusAndNsitecodeOrderByNmanufcodeDesc(int i, Integer nsiteInteger);
	Manufacturer findBySmanufnameAndNsitecode(String ssuppliername, Integer nsitecode);
	Manufacturer findByNmanufcode(int nunitCode);
	List<Object> findByNstatusAndNsitecode(int i, Integer nsiteInteger);
	List<Manufacturer> findByNsitecodeOrderByNmanufcodeDesc(Integer nsiteInteger);
	Manufacturer findBySmanufnameIgnoreCaseAndNsitecode(String smanufname, Integer nsitecode);
	List<Manufacturer> findByNstatusAndNsitecodeOrderByNmanufcode(int i, Integer nsiteInteger);
	Manufacturer findBySmanufnameIgnoreCaseAndNsitecodeAndNstatus(String smanufname, Integer nsitecode, int i);
	Manufacturer findByNmanufcodeAndNstatus(int nunitCode, int i);
}