package com.agaram.eln.primary.repository.cfr;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.cfr.LSactivity;

public interface LSactivityRepository extends JpaRepository<LSactivity, Integer> {
	public List<LSactivity> findByOrderByActivitycodeDesc(); 
	public List<LSactivity> findTop20ByOrderByActivitycodeDesc(); 
	public List<LSactivity> findTop20ByActivitycodeLessThanOrderByActivitycodeDesc(Integer activitycode); 
}
