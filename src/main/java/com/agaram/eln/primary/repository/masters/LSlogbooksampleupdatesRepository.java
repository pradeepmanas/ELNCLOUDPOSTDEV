package com.agaram.eln.primary.repository.masters;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.masters.LSlogbooksampleupdates;
//import com.agaram.eln.primary.model.protocols.LSprotocolsampleupdates;

public interface LSlogbooksampleupdatesRepository extends JpaRepository<LSlogbooksampleupdates, Integer> {


	List<LSlogbooksampleupdates> findByLogbookcodeAndIndexofIsNotNullAndStatus(Integer logbookcode, int i);

	List<LSlogbooksampleupdates> findByRepositorydatacodeAndUsedquantityNotAndStatusOrderByLogbooksamplecodeDesc(
			Integer repositorydatacode, int i, int j);

}
