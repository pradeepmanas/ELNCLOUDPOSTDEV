package com.agaram.eln.primary.repository.cloudProtocol;

import org.springframework.data.jpa.repository.JpaRepository;
import com.agaram.eln.primary.model.cloudProtocol.CloudLsLogilabprotocolstepInfo;

public interface CloudLsLogilabprotocolstepInfoRepository extends JpaRepository<CloudLsLogilabprotocolstepInfo, Integer>{

	CloudLsLogilabprotocolstepInfo findById(Integer protocolorderstepcode);

}
