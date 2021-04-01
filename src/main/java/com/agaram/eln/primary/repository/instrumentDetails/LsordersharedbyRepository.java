package com.agaram.eln.primary.repository.instrumentDetails;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.instrumentDetails.Lsordersharedby;

public interface LsordersharedbyRepository extends JpaRepository<Lsordersharedby, Long>{

	public List<Lsordersharedby> findBySharebyunifiedidAndOrdertypeAndSharestatusOrderBySharedbycodeDesc(String unifiedid, Integer ordertype, Integer sharestatus);
	public Lsordersharedby findBySharebyunifiedidAndSharetounifiedidAndOrdertypeAndSharebatchcode(String sharebyunifiedid, String sharetounifiedid, Integer ordertype, Long sharebatchcode);
	
	public long countBySharebyunifiedidAndOrdertypeAndSharestatusOrderBySharedbycodeDesc(String unifiedid, Integer ordertype, Integer sharestatus);
}
