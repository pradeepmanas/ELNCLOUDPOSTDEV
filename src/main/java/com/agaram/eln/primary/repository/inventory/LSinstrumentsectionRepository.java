package com.agaram.eln.primary.repository.inventory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.agaram.eln.primary.model.inventory.LSinstrumentsection;

public interface LSinstrumentsectionRepository extends JpaRepository<LSinstrumentsection, Integer>{
	public List<LSinstrumentsection> findByninstrumentcode(Integer ninstrumentcode);

	@Query("select l.nsectioncode from LSinstrumentsection l where l.ninstrumentcode = :instrument")
	public List<Integer> getNsectioncodeByNinstrumentcode(@Param("instrument") Integer ninstrumentcode);
}
