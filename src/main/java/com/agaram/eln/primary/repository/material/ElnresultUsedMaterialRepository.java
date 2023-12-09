package com.agaram.eln.primary.repository.material;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.material.ElnresultUsedMaterial;

public interface ElnresultUsedMaterialRepository extends JpaRepository<ElnresultUsedMaterial, Integer>{

	List<ElnresultUsedMaterial> findByNinventorycodeOrderByNresultusedmaterialcodeDesc(Integer integer);

	List<ElnresultUsedMaterial> findByNinventorycodeAndCreateddateBetweenOrderByNresultusedmaterialcodeDesc(
			Integer ninventorycode, Date fromDate, Date toDate);

	List<ElnresultUsedMaterial> findByNinventorycodeAndTransactionscreenAndCreateddateBetweenOrderByNresultusedmaterialcodeDesc(
			Integer ninventorycode, Integer screencode, Date fromDate, Date toDate);


}
