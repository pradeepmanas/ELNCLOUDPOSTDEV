package com.agaram.eln.primary.repository.inventory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.inventory.LSmaterialcategory;

public interface LSmaterialcategoryRepository extends JpaRepository<LSmaterialcategory, Integer>{
	public List<LSmaterialcategory>  findByNmaterialtypecode(Integer nmaterialtypecode);
}
