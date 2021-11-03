package com.agaram.eln.primary.repository.masters;

import java.util.List;
import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.fetchmodel.getmasters.Repositorymaster;
import com.agaram.eln.primary.model.masters.Lsrepositories;
public interface LsrepositoriesRepository extends JpaRepository<Lsrepositories, Integer> {
	public List<Lsrepositories> findBySitecodeOrderByRepositorycodeAsc(Integer sitecode);
	public Lsrepositories findByRepositorynameAndSitecode(String repositoryname,Integer sitecode);
	public Lsrepositories findByRepositorynameAndSitecodeAndRepositorycodeNot(String repositoryname,Integer sitecode, Integer repositorycode);
	
	public List<Repositorymaster> findBysitecodeOrderByRepositorycodeAsc(Integer sitecode);
	
	public List<Lsrepositories> findBysitecodeAndAddedonBetweenOrderByRepositorycodeAsc(Integer sitecode,Date fromdate,Date todate);
	
	public List<Repositorymaster> findBySitecodeAndAddedonBetweenOrderByRepositorycodeAsc(Integer sitecode,Date fromdate,Date todate);
	
	public Lsrepositories findByRepositorycode(Integer repositorycode);
	
	public Repositorymaster findByrepositorycode(Integer repositorycode);
}
