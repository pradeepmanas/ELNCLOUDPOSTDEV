package com.agaram.eln.primary.repository.inventory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.inventory.LSinstrument;

public interface LSinstrumentRepository extends JpaRepository<LSinstrument, Integer>{

	public List<LSinstrument> findByinstrumentcatcode(Integer instrumentcatcode);
}
