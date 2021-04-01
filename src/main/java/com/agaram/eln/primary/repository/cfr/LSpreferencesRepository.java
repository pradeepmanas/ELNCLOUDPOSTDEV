package com.agaram.eln.primary.repository.cfr;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.cfr.LSpreferences;

public interface LSpreferencesRepository extends JpaRepository<LSpreferences,Integer>{
	public List<LSpreferences> findByserialnoIn(List<Integer> lstserailno );
}

