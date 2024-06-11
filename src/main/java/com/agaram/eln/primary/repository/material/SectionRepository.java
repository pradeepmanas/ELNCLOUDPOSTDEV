package com.agaram.eln.primary.repository.material;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.agaram.eln.primary.model.material.Section;

public interface SectionRepository  extends JpaRepository<Section, Integer>{
	public List<Object> findByNstatus(Integer nstatus);

	public List<Section> findByNstatusOrderByNsectioncode(int i);

	public Section findByNsectioncode(int nsectionCode);

	public Section findBySsectionnameAndNstatus(String ssectionname, int i);

	public Section findBySsectionnameAndNstatusAndNsitecode(String ssectionname, int i, Integer nsitecode);

	public List<Section> findByNstatusAndNsitecodeOrderByNsectioncodeDesc(int i, Integer nsiteInteger);

	public List<Object> findByNstatusAndNsitecode(int i, Integer nsiteInteger);

	public Section findByNsectioncodeAndNstatus(Integer nsectioncode, int i);

	public List<Section> findBySsectionnameInAndNstatusAndNsitecode(List<String> lstSectionname, int i,
			Integer sitecode);

	public Section findBySsectionnameAndNsitecode(String ssectionname, Integer nsitecode);

	public List<Section> findByNsitecodeOrderByNsectioncodeDesc(Integer nsiteInteger);

	public Section findBySsectionnameAndNsitecodeAndNstatus(String ssectionname, Integer nsitecode, int i);

	public Section findBySsectionnameIgnoreCaseAndNsitecode(String ssectionname, Integer nsitecode);

	public List<Section> findByNsitecodeAndNstatusOrderByNsectioncodeDesc(Integer nsiteInteger, int i);

	public List<Section> findByNstatusAndNsitecodeOrderByNsectioncode(int i, Integer nsiteInteger);

	public Section findBySsectionnameIgnoreCaseAndNsitecodeAndNstatus(String ssectionname, Integer nsitecode, int i);
	@Query(value ="select * from section where ssectionname = ?1 and nsitecode = ?2",nativeQuery = true)
	public Section getSectionDetails(String string, Number sitecode);
}
