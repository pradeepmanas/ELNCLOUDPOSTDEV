package com.agaram.eln.primary.repository.iotconnect;

import org.springframework.data.jpa.repository.JpaRepository;


import org.springframework.stereotype.Repository;

import com.agaram.eln.primary.model.iotconnect.RCTCPResultFieldValues;

@Repository
public interface RCTCPResultFieldValuesRepository  extends JpaRepository<RCTCPResultFieldValues, Integer>{
	
}
