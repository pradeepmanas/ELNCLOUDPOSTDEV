package com.agaram.eln.primary.repository.instrumentDetails;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.instrumentDetails.Lsordershareto;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;

public interface LsordersharetoRepository extends JpaRepository<Lsordershareto,Long>{
	public List<Lsordershareto> findBySharetounifiedidAndOrdertypeAndSharestatusOrderBySharetocodeDesc(String unifiedid, Integer ordertype, Integer sharestatus);
	public Lsordershareto findBySharebyunifiedidAndSharetounifiedidAndOrdertypeAndSharebatchcode(String sharebyunifiedid, String sharetounifiedid, Integer ordertype, Long sharebatchcode);
	
	public long countBySharetounifiedidAndOrdertypeAndSharestatusOrderBySharetocodeDesc(String unifiedid, Integer ordertype, Integer sharestatus);
	
	public List<Lsordershareto> findBySharetounifiedidAndSharedonBetweenAndSharestatus(String unifiedid, Date fromdate, Date todate, Integer sharestatus);
	
	public List<Lsordershareto> findByUsersharedonAndSharestatusAndSharedonBetweenOrderBySharetocodeDesc(LSuserMaster lssharedto, Integer sharestatus, Date fromdate, Date todate);
	public List<Lsordershareto> findByUsersharedonAndOrdertypeAndSharestatusAndSharedonBetweenOrderBySharetocodeDesc(LSuserMaster lsselecteduser, Integer filetype, int i, Date fromdate, Date todate);
			
}
