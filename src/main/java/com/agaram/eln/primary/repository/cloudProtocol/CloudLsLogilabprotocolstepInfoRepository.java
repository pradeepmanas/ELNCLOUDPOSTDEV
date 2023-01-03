package com.agaram.eln.primary.repository.cloudProtocol;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.agaram.eln.primary.model.cloudProtocol.CloudLsLogilabprotocolstepInfo;

public interface CloudLsLogilabprotocolstepInfoRepository extends JpaRepository<CloudLsLogilabprotocolstepInfo, Integer>{

	CloudLsLogilabprotocolstepInfo findById(Integer protocolorderstepcode);
	
	
	@Query(value = "SELECT * FROM LsLogilabprotocolstepInfoCloud WHERE id = ?2 and text(lsprotocolstepInfo) like %?1% ", nativeQuery = true)
	public CloudLsLogilabprotocolstepInfo findByContentvaluesequal(String value,Integer id);
	

}
