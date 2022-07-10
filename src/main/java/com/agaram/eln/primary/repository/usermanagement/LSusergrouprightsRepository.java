package com.agaram.eln.primary.repository.usermanagement;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.agaram.eln.primary.model.usermanagement.LSusergroup;
import com.agaram.eln.primary.model.usermanagement.LSusergrouprights;


public interface LSusergrouprightsRepository  extends JpaRepository<LSusergrouprights, Integer>{
	public List<LSusergrouprights> findByModulename(String modulename);
	public List<LSusergrouprights> findByUsergroupid(LSusergroup lsusergroup);
	
	 @Query("SELECT DISTINCT r.modulename FROM LSusergrouprights r where modulename !='User Group' and modulename !='User Master'")
	// @Query("SELECT DISTINCT r.modulename FROM LSusergrouprights r")
	  List<String> findDistinctmodulename();
	 
	 @Transactional
	@Modifying
	@Query("select o from LSusergrouprights o where o.usergroupid= ?1 ORDER BY o.sequenceorder DESC")
	 public List<LSusergrouprights> getrightsonUsergroupid(LSusergroup lsusergroup);

}
