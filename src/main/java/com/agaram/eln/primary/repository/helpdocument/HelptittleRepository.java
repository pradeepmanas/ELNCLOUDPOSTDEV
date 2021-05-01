package com.agaram.eln.primary.repository.helpdocument;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.helpdocument.Helptittle;

public interface HelptittleRepository extends JpaRepository <Helptittle,Integer >{

	public List<Helptittle> findByOrderByNodecodeAsc();
	public List<Helptittle> findByTextAndParentcode(String text, Integer parentcode);
	public List<Helptittle> findByTextAndParentcodeAndNodecodeNot(String text, Integer parentcode, Integer nodecode);
	public List<Helptittle> findByOrderByNodeindexAsc();
}
