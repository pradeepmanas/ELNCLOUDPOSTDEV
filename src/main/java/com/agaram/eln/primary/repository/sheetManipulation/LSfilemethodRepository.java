package com.agaram.eln.primary.repository.sheetManipulation;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.agaram.eln.primary.model.sheetManipulation.LSfilemethod;

public interface LSfilemethodRepository extends JpaRepository<LSfilemethod, Integer> {
	@Transactional
	@Modifying
	@Query("delete from LSfilemethod where filecode = ?1")
	public void deleteByfilecode(Integer filecode);
	
	public LSfilemethod findByFilecode(Integer filecode);
	
	public List<LSfilemethod> findByFilecodeOrderByFilemethodcode(Integer filecode);
}
