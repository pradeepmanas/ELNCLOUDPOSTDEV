package com.agaram.eln.primary.repository.helpdocument;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.agaram.eln.primary.model.helpdocument.Helpdocument;
import com.mongodb.util.JSON;

public interface HelpdocumentRepository extends JpaRepository <Helpdocument,Integer >{

	@Transactional
	@Modifying
	 @Query("update Helpdocument u set u.lshelpdocumentcontent = ?1,u.documentname =?2 where u.id = ?3")
	int setlshelpdocumentcontentanddocumentnameByid(String lshelpdocumentcontent,String documentname,Integer id);
	
	
	public Helpdocument findByNodecode(Integer nodecode);
}
