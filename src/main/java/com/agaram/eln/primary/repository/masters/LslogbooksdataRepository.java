package com.agaram.eln.primary.repository.masters;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.masters.Lslogbooksdata;

public interface LslogbooksdataRepository extends JpaRepository<Lslogbooksdata,Integer>{
	public List<Lslogbooksdata> findByLogbookcodeAndSitecodeAndItemstatusOrderByLogbookdatacodeDesc(Integer logbookcode, Integer sitecode, Integer itemstatus);
	public Lslogbooksdata findByLogbookcodeAndLogbookitemnameAndSitecodeAndLogbookdatacodeNot(Integer logbookcode, String logbookcodeitemname, Integer sitecode, Integer logbookdatacode);
	public Lslogbooksdata findByLogbookcodeAndLogbookitemnameAndSitecode(Integer logbookcode, String logbookcodeitemname, Integer sitecode);
}
