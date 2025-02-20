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

	List<ElnresultUsedMaterial> findByNinventorycodeInAndCreateddateBetweenOrderByNresultusedmaterialcodeDesc(
			List<Integer> inventorycode, Date fromdate, Date todate);

	List<ElnresultUsedMaterial> findByNinventorycodeInAndCreateddateBetweenAndTransactionscreenOrderByNresultusedmaterialcodeDesc(
			List<Integer> inventorycode, Date fromdate, Date todate, Integer transactionscreen);

	List<ElnresultUsedMaterial> findByNinventorycodeInAndCreateddateBetweenAndShowfullcommentIsNullOrderByNresultusedmaterialcodeDesc(
			List<Integer> inventorycode, Date fromdate, Date todate);

	List<ElnresultUsedMaterial> findByNinventorycodeInAndCreateddateBetweenAndShowfullcommentIsNotNull(
			List<Integer> inventorycode, Date fromdate, Date todate);

	List<ElnresultUsedMaterial> findByNinventorycodeInAndCreateddateBetweenAndShowfullcommentIsNotNullOrNinventorycodeInAndCreateddateBetweenAndShowfullcommentIsNullAndTransactionscreen(
			List<Integer> inventorycode, Date fromdate, Date todate, List<Integer> inventorycode2, Date fromdate2,
			Date todate2, Integer transactionscreen);


}
