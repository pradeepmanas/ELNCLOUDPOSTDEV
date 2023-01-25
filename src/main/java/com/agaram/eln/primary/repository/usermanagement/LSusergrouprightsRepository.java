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
	public List<LSusergrouprights> findByDisplaytopic(String displaytopic);
	
	 @Query("SELECT DISTINCT r.modulename FROM LSusergrouprights r where modulename !='User Group' and modulename !='User Master'")
	// @Query("SELECT DISTINCT r.modulename FROM LSusergrouprights r")
	  List<String> findDistinctmodulename();
	 
	 @Transactional
	@Modifying
	@Query("select o from LSusergrouprights o where o.usergroupid= ?1 ORDER BY o.sequenceorder DESC")
	 public List<LSusergrouprights> getrightsonUsergroupid(LSusergroup lsusergroup);
	 @Query("SELECT r.displaytopic FROM LSusergrouprights r")
     public List<String> findBydisplaytopic();
	public List<LSusergrouprights> findByUsergroupid(Integer usergroupcode);
	@Transactional
	@Modifying
	@Query("update com.agaram.eln.primary.model.usermanagement.LSusergrouprights o set o.sallow ='0' where o.displaytopic='IDS_TSK_ADDREPO' or o.displaytopic='IDS_TSK_EDITREPO' or o.displaytopic='IDS_SCN_INVENTORY' or o.displaytopic='IDS_SCN_UNITMASTER'")
	public void setplanrightsonUsergroupid();
	@Transactional
	@Modifying
	@Query("update com.agaram.eln.primary.model.usermanagement.LSusergrouprights o set o.sallow ='0' where o.displaytopic='IDS_SCN_UNITMASTER' or o.displaytopic='IDS_SCN_SECTIONMASTER' or o.displaytopic='IDS_SCN_STORAGELOCATION' or o.displaytopic='IDS_SCN_MATERIALCATEGORY' or o.displaytopic='IDS_SCN_MATERIAL' or o.displaytopic='IDS_SCN_MATERIALINVENTORY' or o.displaytopic='IDS_SCN_MATERIALTYPE'")
	public void setplanrightsonUsergroupidfreestd();
	@Transactional
	@Modifying
	@Query("update com.agaram.eln.primary.model.usermanagement.LSusergrouprights o set o.sallow ='1' where o.usergroupid ='1'")
	public void setplatrightsonamdingroup();

}
