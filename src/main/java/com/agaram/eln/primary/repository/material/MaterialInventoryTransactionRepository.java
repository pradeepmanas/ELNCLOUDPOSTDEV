package com.agaram.eln.primary.repository.material;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.material.MaterialInventoryTransaction;

public interface MaterialInventoryTransactionRepository  extends JpaRepository<MaterialInventoryTransaction, Integer>{

	MaterialInventoryTransaction findByNmaterialinventorycode(int nmaterialinventorycode);

}
