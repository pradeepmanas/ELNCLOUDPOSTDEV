package com.agaram.eln.primary.repository.sheetManipulation;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.agaram.eln.primary.model.sheetManipulation.Lsfilesharedby;

public interface LsfilesharedbyRepository extends JpaRepository<Lsfilesharedby, Integer>{

	Lsfilesharedby findBySharebyunifiedidAndSharetounifiedidAndSharefilecode(String sharebyunifiedid,
			String sharetounifiedid, Long sharefilecode);


	List<Lsfilesharedby> findBySharebyunifiedidAndSharestatusOrderBySharedbytofilecodeDesc(String sharebyunifiedid,
			int i);


	Object countBySharebyunifiedidAndSharestatus(String sharebyunifiedid, int i);


	Lsfilesharedby findBySharedbytofilecode(Long sharedbytofilecode);




	List<Lsfilesharedby> findBySharefilecode(Long filecode);

	@Transactional
	@Modifying
	@Query(value = "update Lsfilesharedby set retirestatus =1 where sharefilecode = ?1", nativeQuery = true)
	void updateRetirestatus(Integer filecode);

}
