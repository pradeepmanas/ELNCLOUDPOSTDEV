package com.agaram.eln.primary.repository.masters;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.masters.Lsrepositoriesdata;

public interface LsrepositoriesdataRepository extends JpaRepository<Lsrepositoriesdata, Integer> {
	public List<Lsrepositoriesdata> findByRepositorycodeAndSitecodeAndItemstatusOrderByRepositorydatacodeDesc(
			Integer repositorycode, Integer sitecode, Integer itemstatus);

	public Lsrepositoriesdata findByRepositorycodeAndRepositoryitemnameAndSitecode(Integer repositorycode,
			String repositoryitemname, Integer sitecode);

	public Lsrepositoriesdata findByRepositorycodeAndRepositoryitemnameAndSitecodeAndRepositorydatacodeNot(
			Integer repositorycode, String repositoryitemname, Integer sitecode, Integer repositorydatacode);

	public List<Lsrepositoriesdata> findByRepositorycodeAndItemstatusOrderByRepositorydatacodeDesc(
			Integer repositorycode, Integer itemstatus);
	
	public List<Lsrepositoriesdata> findByRepositorycodeAndItemstatusAndAddedonBetweenOrderByRepositorydatacodeDesc
	(Integer repositorycode, Integer itemstatus,Date fromdate,Date todate);
}
