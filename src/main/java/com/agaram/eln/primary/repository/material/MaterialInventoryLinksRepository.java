package com.agaram.eln.primary.repository.material;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.agaram.eln.primary.model.material.MaterilaInventoryLinks;

public interface MaterialInventoryLinksRepository extends JpaRepository<MaterilaInventoryLinks, Integer>{
	
	List<MaterilaInventoryLinks> findByNmaterialinventorycodeAndNstatus(Integer nmaterialcode,Integer nstatus);
}
