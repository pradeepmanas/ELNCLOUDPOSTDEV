package com.agaram.eln.primary.repository.masters;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.masters.Lslogbooks;

public interface LslogbooksRepository extends JpaRepository<Lslogbooks, Integer>{
//	public Lslogbooks findByLogbooknameAndSitecode(String logbookname,Integer sitecode);
	public Lslogbooks findByLogbooknameAndSitecodeAndLogbookcodeNot(String logbookname,Integer sitecode, Integer logbookcode);
	public List<Lslogbooks> findBySitecodeOrderByLogbookcodeAsc(Integer sitecode);
//	public Lslogbooks findByLogbooknameAndSitecodeAndLogbookcodeNot(String logbookname, Integer sitecode,
//			Long logbookcode);
	public List<Lslogbooks> findBySitecodeOrderByLogbookcodeDesc(Integer sitecode);
	public Lslogbooks findByLogbooknameIgnoreCaseAndSitecodeAndLogbookcodeNot(String logbookname, Integer sitecode,
			Long logbookcode);
	public Lslogbooks findByLogbooknameIgnoreCaseAndSitecode(String trim, Integer sitecode);
}
