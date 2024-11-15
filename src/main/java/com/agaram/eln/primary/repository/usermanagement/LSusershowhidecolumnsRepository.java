package com.agaram.eln.primary.repository.usermanagement;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.usermanagement.LSusershowhidecolumns;

public interface LSusershowhidecolumnsRepository extends JpaRepository<LSusershowhidecolumns, Integer>{
	public LSusershowhidecolumns findFirstByUsercodeAndSitecodeAndGridname(Integer usercode, Integer sitecode, String gridname);
}
