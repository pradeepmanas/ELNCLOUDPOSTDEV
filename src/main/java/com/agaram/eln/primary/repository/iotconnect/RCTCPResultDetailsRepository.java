package com.agaram.eln.primary.repository.iotconnect;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.agaram.eln.primary.model.iotconnect.RCTCPResultDetails;

@Repository
public interface RCTCPResultDetailsRepository extends JpaRepository<RCTCPResultDetails, Integer>{

	
}
