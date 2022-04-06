package com.agaram.eln.primary.repository.usermanagement;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.usermanagement.LSMultiusergroup;
import com.agaram.eln.primary.model.usermanagement.LSusergroup;

public interface LSMultiusergroupRepositery extends JpaRepository<LSMultiusergroup, Integer> {

	public LSMultiusergroup findBymultiusergroupcode(Integer multiusergroupcode);
	
	@Transactional
	public void deleteByusercode(Integer usercode);

	public List<LSMultiusergroup> findByusercode(Integer usercode);

	public List<LSMultiusergroup> findBylsusergroupIn(List<LSusergroup> usergroupcodelist);



	

}
