package com.agaram.eln.primary.repository.cfr;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.agaram.eln.primary.model.cfr.LSpreferences;

public interface LSpreferencesRepository extends JpaRepository<LSpreferences,Integer>{
	public List<LSpreferences> findByserialnoIn(List<Integer> lstserailno );

	public LSpreferences findBySerialno(int i);
	
	public LSpreferences findByTasksettings(String Task);

	public LSpreferences findByTasksettingsAndValuesettings(String string, String string2);	
	
	@Transactional
	@Modifying
	@Query("update LSpreferences o set o.valuesettings = ?1 where serialno= ?2 ")
	public void setPlantypeConCurrentUser(String string, int i);
	@Transactional
	@Modifying
	@Query("update LSpreferences o set o.valuesettings = ?1 where serialno = ?2 ")
	public void setPlantypeMainFormUser(String string, int i);
}

