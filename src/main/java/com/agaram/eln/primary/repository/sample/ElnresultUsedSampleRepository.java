package com.agaram.eln.primary.repository.sample;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.sample.ElnresultUsedSample;

public interface ElnresultUsedSampleRepository extends JpaRepository<ElnresultUsedSample, Integer> {

	List<ElnresultUsedSample> findBySamplecode(Integer samplecode);

    List<ElnresultUsedSample> findBySamplecodeInAndCreateddateBetweenAndTransactionscreenOrderByNelnresultusedsamplecodeDesc(
			Integer samplecode, Date fromdate, Date todate, Integer transactionscreen);

	 List<ElnresultUsedSample> findBySamplecodeInAndCreateddateBetweenOrderByNelnresultusedsamplecodeDesc(Integer samplecode,
			Date fromdate, Date todate);


//	List<ElnresultUsedSample> findByNinventorycodeInAndCreateddateBetweenAndShowfullcommentIsNotNull(
//			List<Integer> inventorycode, Date fromdate, Date todate);

//	List<ElnresultUsedSample> findBySamplecodeInAndCreateddateBetweenAndShowfullcommentIsNullOrderByNelnresultusedsamplecodeDesc(
//			Integer samplecode, Date fromdate, Date todate);


}
