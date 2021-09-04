package com.agaram.eln.primary.repository.usermanagement;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.agaram.eln.primary.model.usermanagement.LSusergroup;
import com.agaram.eln.primary.model.usermanagement.LSusergrouprights;


public interface LSusergrouprightsRepository  extends JpaRepository<LSusergrouprights, Integer>{
	public List<LSusergrouprights> findByModulename(String modulename);
	public List<LSusergrouprights> findByUsergroupid(LSusergroup lsusergroup);
	
	 @Query("SELECT DISTINCT r.modulename FROM LSusergrouprights r where modulename !='User Group' and modulename !='User Master'")
	// @Query("SELECT DISTINCT r.modulename FROM LSusergrouprights r")
	  List<String> findDistinctmodulename();

}
