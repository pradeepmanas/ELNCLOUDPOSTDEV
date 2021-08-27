package com.agaram.eln.primary.repository.sheetManipulation;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.fetchmodel.getmasters.Samplemaster;
import com.agaram.eln.primary.model.sheetManipulation.LSsamplemaster;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;

public interface LSsamplemasterRepository extends JpaRepository<LSsamplemaster, Integer>{
	public LSsamplemaster findBySamplename(String samplename);
	public List<LSsamplemaster> findBystatus(Integer status);
	public LSsamplemaster findBySamplenameAndStatus(String samplename, Integer status);
	public LSsamplemaster findBySamplenameAndStatusAndSamplecodeNot(String samplename, Integer status, Integer samplecode);
	
	public List<Samplemaster> findBystatusAndLssitemaster(Integer status,LSSiteMaster lssitemaster);
	public LSsamplemaster findBySamplenameAndStatusAndLssitemaster(String samplename, Integer status,LSSiteMaster lssitemaster);
	public LSsamplemaster findBySamplenameAndStatusAndSamplecodeNotAndLssitemaster(String samplename, Integer status, Integer samplecode,LSSiteMaster lssitemaster);
	public LSsamplemaster findBySamplenameIgnoreCaseAndStatusAndSamplecodeNotAndLssitemaster(String samplename,Integer status,
			Integer samplecode, LSSiteMaster lssitemaster);
	public LSsamplemaster findBySamplenameIgnoreCaseAndStatusAndLssitemaster(String samplename, Integer status,
			LSSiteMaster lssitemaster);
}
