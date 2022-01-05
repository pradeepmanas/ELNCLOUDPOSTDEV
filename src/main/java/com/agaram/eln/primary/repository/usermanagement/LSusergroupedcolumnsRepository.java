package com.agaram.eln.primary.repository.usermanagement;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.usermanagement.LSusergroupedcolumns;

public interface LSusergroupedcolumnsRepository extends JpaRepository<LSusergroupedcolumns, Integer>{
	public LSusergroupedcolumns findByUsercodeAndSitecodeAndGridname(Integer usercode, Integer sitecode, String gridname);
}
