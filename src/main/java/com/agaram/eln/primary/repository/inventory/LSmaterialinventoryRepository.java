package com.agaram.eln.primary.repository.inventory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.inventory.LSmaterialinventory;

public interface LSmaterialinventoryRepository extends JpaRepository<LSmaterialinventory, Integer>{
	public List<LSmaterialinventory> findByNmaterialcodeAndNtransactionstatus(Integer nmaterialcode,Integer ntransac);

	public LSmaterialinventory findBynmaterialinventorycode(Integer nmaterialinventorycode);
	
	public List<LSmaterialinventory> findByNmaterialinventorycode(Integer nmaterialinventorycode);
}
