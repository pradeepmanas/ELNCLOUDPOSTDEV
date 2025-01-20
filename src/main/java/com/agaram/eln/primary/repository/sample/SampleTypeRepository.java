package com.agaram.eln.primary.repository.sample;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.sample.SampleType;

public interface SampleTypeRepository extends JpaRepository<SampleType,Integer> {

	List<SampleType> findBySsampletypenameIgnoreCaseAndNsitecodeOrderByNsampletypecode(String ssampletypename,
			Integer nsitecode);

	List<SampleType> findBySsampletypenameIgnoreCaseAndNsitecodeAndNsampletypecodeNot(String ssampletypename,
			Integer nsitecode, Integer nsampletypecode);

	SampleType findByNsampletypecodeAndNstatus(Integer nsampletypecode, int i);

	List<SampleType> findByNsampletypecodeNotAndNsitecodeOrNsampletypecodeNotAndNdefaultstatusOrderByNsampletypecodeDesc(
			int i, Integer nsitecode, int j, int k);

	List<SampleType> findByNsampletypecodeNotAndNstatusAndNsitecodeOrNsampletypecodeNotAndNstatusAndNdefaultstatus(
			int i, int j, Integer nsitecode, int k, int l, int m);

}
