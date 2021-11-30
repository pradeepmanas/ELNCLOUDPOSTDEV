package com.agaram.eln.primary.repository.sheetManipulation;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.sheetManipulation.Lsfilesharedby;

public interface LsfilesharedbyRepository extends JpaRepository<Lsfilesharedby, Integer>{

	Lsfilesharedby findBySharebyunifiedidAndSharetounifiedidAndSharefilecode(String sharebyunifiedid,
			String sharetounifiedid, Long sharefilecode);


	List<Lsfilesharedby> findBySharebyunifiedidAndSharestatusOrderBySharedbytofilecodeDesc(String sharebyunifiedid,
			int i);


	Object countBySharebyunifiedidAndSharestatus(String sharebyunifiedid, int i);


	Lsfilesharedby findBySharedbytofilecode(Long sharedbytofilecode);

}
