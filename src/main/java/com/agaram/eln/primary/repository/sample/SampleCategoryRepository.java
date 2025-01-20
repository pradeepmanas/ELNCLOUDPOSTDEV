package com.agaram.eln.primary.repository.sample;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.sample.SampleCategory;

public interface SampleCategoryRepository extends JpaRepository<SampleCategory, Integer> {

	List<SampleCategory> findByNsitecodeOrNdefaultstatusOrderByNsamplecatcodeDesc(Integer nsitecode, int i);

	List<SampleCategory> findBySsamplecatnameIgnoreCaseAndNsitecode(String ssamplecatname, Integer nsitecode);

	public SampleCategory findByNsamplecatcode(Integer nsamplecatcode);

	public SampleCategory findByNsitecodeAndSsamplecatnameIgnoreCase(Integer nsitecode, String ssamplecatname);

}
