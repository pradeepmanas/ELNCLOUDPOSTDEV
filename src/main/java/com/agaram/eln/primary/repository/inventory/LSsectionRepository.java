package com.agaram.eln.primary.repository.inventory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.inventory.LSsection;


public interface LSsectionRepository extends JpaRepository<LSsection, Integer> {

	public List<LSsection> findBynsectioncodeIn(List<Integer> lstSection);
}
