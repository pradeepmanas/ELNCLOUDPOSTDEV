package com.agaram.eln.primary.repository.instrumentDetails;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.instrumentDetails.Lsordersharedby;

public interface LsordersharedbyRepository extends JpaRepository<Lsordersharedby, Long>{

	public List<Lsordersharedby> findBySharebyunifiedidAndOrdertypeAndSharestatusOrderBySharedbycodeDesc(String unifiedid, Integer ordertype, Integer sharestatus);
	public Lsordersharedby findBySharebyunifiedidAndSharetounifiedidAndOrdertypeAndSharebatchcode(String sharebyunifiedid, String sharetounifiedid, Integer ordertype, Long sharebatchcode);
	
	public long countBySharebyunifiedidAndOrdertypeAndSharestatusOrderBySharedbycodeDesc(String unifiedid, Integer ordertype, Integer sharestatus);
	
	public List<Lsordersharedby> findBySharebyunifiedidAndSharedonBetweenOrUnsharedonBetween(String unifiedid, Date fromdate, Date todate, Date unshrfromdate, Date unshrtodate);
	
	public List<Lsordersharedby> findBySharebyunifiedidAndSharedonBetweenOrSharebyunifiedidAndUnsharedonBetween(String unifiedid, Date fromdate, Date todate,String sharebyunifiedid, Date unshrfromdate, Date unshrtodate);
}
