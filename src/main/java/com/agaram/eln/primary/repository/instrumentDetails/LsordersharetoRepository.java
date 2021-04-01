package com.agaram.eln.primary.repository.instrumentDetails;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.instrumentDetails.Lsordershareto;

public interface LsordersharetoRepository extends JpaRepository<Lsordershareto,Long>{
	public List<Lsordershareto> findBySharetounifiedidAndOrdertypeAndSharestatusOrderBySharetocodeDesc(String unifiedid, Integer ordertype, Integer sharestatus);
	public Lsordershareto findBySharebyunifiedidAndSharetounifiedidAndOrdertypeAndSharebatchcode(String sharebyunifiedid, String sharetounifiedid, Integer ordertype, Long sharebatchcode);
	
	public long countBySharetounifiedidAndOrdertypeAndSharestatusOrderBySharetocodeDesc(String unifiedid, Integer ordertype, Integer sharestatus);
}
