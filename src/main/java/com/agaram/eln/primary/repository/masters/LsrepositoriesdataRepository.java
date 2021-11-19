package com.agaram.eln.primary.repository.masters;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
	@Transactional
	@Modifying
	@Query( "select o from Lsrepositoriesdata o where repositorydatacode in :repositorydatacode" )
	public List<Lsrepositoriesdata> getRepositorydatacode(@Param("repositorydatacode") List<Integer> repositorydatacode);

	public List<Lsrepositoriesdata> findByRepositorydatacode(Integer lsrepositoriesdatacode);
	
//	@Query(value = "select * from events where type_id in :types", nativeQuery = true)
//	List<Lsrepositoriesdata> findEventsByType(@Param("types") List<Integer> types);
	
//	@Transactional
//	@Modifying
//	@Query("select l.Lsrepositoriesdata from Lsrepositoriesdata l where l.repositorydatacode in (:repositorydatacode)")
//	public List<Lsrepositoriesdata> getRepositorydatacode(@Param("repositorydatacode")  List<Lsrepositoriesdata> Lsrepositoriesdata);

//	public List<Lsrepositoriesdata> findByRepositorydatacode(Integer repositorydatacode);
}
