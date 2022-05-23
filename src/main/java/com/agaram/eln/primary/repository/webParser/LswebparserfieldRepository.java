package com.agaram.eln.primary.repository.webParser;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.agaram.eln.primary.model.instrumentDetails.LSinstruments;
import com.agaram.eln.primary.model.instrumentDetails.LsMethodFields;
import com.agaram.eln.primary.model.webParser.Lswebparserfield;

public interface LswebparserfieldRepository extends JpaRepository<Lswebparserfield, Integer>{
	public List<LsMethodFields> findByOrderByParserfieldkeyDesc();
	
	 @Transactional
	 @Modifying
	 @Query("select new com.agaram.eln.primary.model.instrumentDetails.LsMethodFields (parserfieldkey ,parserfieldname ,fieldid, datatype, method) from Lswebparserfield c")
	 public List<LsMethodFields> getMethoFeilds();
	 
	 @Transactional
	 @Modifying
	 @Query("select new com.agaram.eln.primary.model.instrumentDetails.LSinstruments (method) from Lswebparserfield c")
	 public List<LSinstruments> getInstruments();
}
