package com.agaram.eln.primary.repository.iotconnect;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.agaram.eln.primary.model.iotconnect.RCTCPFileDetails;


@Repository
public interface RCTCPFileDetailsRepository extends JpaRepository<RCTCPFileDetails, Integer> {

}
