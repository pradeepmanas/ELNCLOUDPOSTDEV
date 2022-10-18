package com.agaram.eln.primary.repository.material;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.material.MaterialInventoryTransaction;

public interface MaterialInventoryTransactionRepository  extends JpaRepository<MaterialInventoryTransaction, Integer>{

//	MaterialInventoryTransaction findByNmaterialinventorycode(int nmaterialinventorycode);

	List<MaterialInventoryTransaction> findByNmaterialinventorycodeOrderByNmaterialinventtranscodeDesc(Integer integer);

	List<MaterialInventoryTransaction> findByNmaterialinventorycodeOrderByNmaterialinventtranscode(Integer integer);

}