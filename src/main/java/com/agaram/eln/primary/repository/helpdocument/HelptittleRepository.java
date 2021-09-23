package com.agaram.eln.primary.repository.helpdocument;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.agaram.eln.primary.model.helpdocument.Helptittle;

public interface HelptittleRepository extends JpaRepository <Helptittle,Integer >{

	public List<Helptittle> findByOrderByNodecodeAsc();
	public List<Helptittle> findByTextAndParentcode(String text, Integer parentcode);
	public List<Helptittle> findByTextAndParentcodeAndNodecodeNot(String text, Integer parentcode, Integer nodecode);
	public List<Helptittle> findByOrderByNodeindexAsc();
	@Transactional
	@Modifying
	 @Query("delete from Helptittle u where u.nodecode in (?1)")
	void deleteByNodecode(List<Integer> lstnodecode);
	@Transactional
	public void deleteByNodecode(Integer nodecode);
	
	public Helptittle findFirst1ByPageOrderByNodecodeDesc(String page);
}
