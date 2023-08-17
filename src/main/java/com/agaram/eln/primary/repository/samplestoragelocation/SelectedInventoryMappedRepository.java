package com.agaram.eln.primary.repository.samplestoragelocation;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.material.MaterialInventory;
import com.agaram.eln.primary.model.samplestoragelocation.SelectedInventoryMapped;

public interface SelectedInventoryMappedRepository extends JpaRepository<SelectedInventoryMapped, Integer> {

	SelectedInventoryMapped findByNmaterialinventorycode(MaterialInventory objInventory);

	List<SelectedInventoryMapped> findByNmaterialinventorycodeOrderByMappedidDesc(MaterialInventory objInventory);
	
	List<SelectedInventoryMapped> findByIdOrderByMappedidDesc(String id);
	
	List<SelectedInventoryMapped> findByIdAndNmaterialinventorycodeNotOrderByMappedidDesc(String id,MaterialInventory objInventory);

}
