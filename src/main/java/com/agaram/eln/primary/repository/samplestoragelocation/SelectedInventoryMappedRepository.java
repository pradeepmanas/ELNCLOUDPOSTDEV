package com.agaram.eln.primary.repository.samplestoragelocation;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.samplestoragelocation.SampleStorageLocation;
import com.agaram.eln.primary.model.samplestoragelocation.SelectedInventoryMapped;

public interface SelectedInventoryMappedRepository extends JpaRepository<SelectedInventoryMapped, Integer> {

	SelectedInventoryMapped findByNmaterialinventorycode(Integer objInventory);

	List<SelectedInventoryMapped> findByNmaterialinventorycodeOrderByMappedidDesc(Integer objInventory);
	
	List<SelectedInventoryMapped> findByIdOrderByMappedidDesc(String id);
	
	List<SelectedInventoryMapped> findBySamplestoragelocationkey(SampleStorageLocation objClass);
	
	List<SelectedInventoryMapped> findByIdIn(List<String> id);
	
	List<SelectedInventoryMapped> findByIdAndNmaterialinventorycodeNotOrderByMappedidDesc(String id,Integer objInventory);

	List<SelectedInventoryMapped> findBySamplestoragelocationkeyOrderByMappedidDesc(SampleStorageLocation objLocation);

}
