package com.agaram.eln.primary.repository.samplestoragelocation;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.agaram.eln.primary.model.samplestoragelocation.SampleStorageLocation;
import com.agaram.eln.primary.model.samplestoragelocation.SampleStorageVersion;


public interface SampleStorageVersionRepository extends JpaRepository<SampleStorageVersion, Integer>{

	Optional<SampleStorageVersion>  findBySampleStorageLocationAndVersionno(final SampleStorageLocation Samplestoragelocation, final int Versionno);
	
	List<SampleStorageVersion>  findBySampleStorageLocationAndApprovalstatus(final SampleStorageLocation Samplestoragelocation, final int Approvalstatus);
	
	List<SampleStorageVersion>  findBySampleStorageLocationAndVersionnoNotIn(final SampleStorageLocation Samplestoragelocation, final List<Integer> Versionno);
	
	List<SampleStorageVersion>  findFirstBySampleStorageLocationOrderBySamplestorageversionkeyDesc(final SampleStorageLocation Samplestoragelocation);	
	
	@Query( value = " select max(versionno) + 1 from SampleStorageVersion where samplestoragelocationkey = ?1 ", nativeQuery = true) 
	
	int getMaxVersionNo(final int sampleStorageLocationKey);

}
