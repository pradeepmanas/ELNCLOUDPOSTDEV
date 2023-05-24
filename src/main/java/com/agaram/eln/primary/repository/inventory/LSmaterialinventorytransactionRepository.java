package com.agaram.eln.primary.repository.inventory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.inventory.LSmaterialinventorytransaction;


public interface LSmaterialinventorytransactionRepository extends JpaRepository<LSmaterialinventorytransaction, Integer>{
	
	public LSmaterialinventorytransaction findByNmaterialinventorycode(Integer nmaterialinventorycode);
	
	public List<LSmaterialinventorytransaction> findBynmaterialinventorycode(Integer nmaterialinventorycode);

}
