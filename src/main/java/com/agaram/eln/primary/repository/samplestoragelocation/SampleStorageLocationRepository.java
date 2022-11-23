package com.agaram.eln.primary.repository.samplestoragelocation;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.samplestoragelocation.SampleStorageLocation;

public interface SampleStorageLocationRepository extends JpaRepository<SampleStorageLocation, Integer> {

	Optional<SampleStorageLocation> findByStatusAndSamplestoragelocationkey(final int Status, final int Samplestoragelocationkey);

	List<SampleStorageLocation> findByStatus(final int Status, final Sort sort);

	List<SampleStorageLocation> findByStatus(int i);
	
	public List<Object> findByStatusOrderBySamplestoragelocationkeyDesc(Integer nstatus);
	
//	List<SampleStorageLocation> findByStatusAndApprovalstatus(final int Status, final int Approvalstatus,final Sort sort);	
}
