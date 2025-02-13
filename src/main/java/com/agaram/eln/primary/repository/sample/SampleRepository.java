package com.agaram.eln.primary.repository.sample;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.material.Elnmaterial;
import com.agaram.eln.primary.model.material.ElnmaterialInventory;
import com.agaram.eln.primary.model.material.MaterialCategory;
import com.agaram.eln.primary.model.sample.Sample;
import com.agaram.eln.primary.model.sample.SampleCategory;

public interface SampleRepository  extends JpaRepository<Sample,Integer>{

	List<Sample> findBySamplecategoryAndNsitecodeOrderBySamplecodeDesc(SampleCategory objsamplecat,Integer nsiteInteger);
	
	List<Sample> findBysamplecodeInOrderBySamplecodeDesc(List<Integer> samplelist);

	List<Sample> findByNsitecodeOrderBySamplecodeDesc(Integer nsitecode);

	List<Sample> findByNsitecodeAndCreateddateBetweenOrderBySamplecodeDesc(Integer nsiteInteger, Date fromDate,
			Date toDate);

	List<Sample> findBySamplecodeInAndNsitecode(List<String> lstIds, Integer nsiteInteger);

	Sample findBySamplecode(Integer samplecode);

	List<Sample> findBySamplecodeIn(List<Integer> sample);
	
	List<Sample> findByNsitecodeAndNtransactionstatusAndOpenexpiryAndExpirydateBetween(
			Integer lssitemaster, int i, boolean b, Date currentDate, Date endDate);

	List<Sample> findByNsitecodeAndCreateddateBetweenAndNtransactionstatusOrderBySamplecodeDesc(Integer sitecode,
			Date fromdate, Date todate, int i);

	List<Sample> findBySamplecategoryAndNsitecodeAndCreateddateBetweenOrderBySamplecodeDesc(
			SampleCategory objsamplecat, Integer nsiteInteger, Date fromdate, Date todate);

	List<Sample> findByNsitecodeAndCreateddateBetweenAndNtransactionstatusAndSamplecodeInOrderBySamplecodeDesc(
			Integer siteCode, Date fromDate, Date toDate, Integer transactionStatus, List<Integer> sampleCodes);

	List<Sample> findByNsitecodeAndCreateddateBetweenAndNtransactionstatusAndAssignedprojectAndTrackconsumptionNotAndQuantityGreaterThanOrNsitecodeAndCreateddateBetweenAndNtransactionstatusAndAssignedprojectIsNullAndTrackconsumptionNotAndQuantityGreaterThanOrNsitecodeAndCreateddateBetweenAndNtransactionstatusAndAssignedprojectAndTrackconsumptionAndQuantityGreaterThanOrNsitecodeAndCreateddateBetweenAndNtransactionstatusAndAssignedprojectIsNullAndTrackconsumptionAndQuantityGreaterThanOrderBySamplecodeDesc(
			Integer siteCode, Date fromDate, Date toDate, Integer transactionStatus, String string, int i, int j,
			Integer siteCode2, Date fromDate2, Date toDate2, Integer transactionStatus2, int k, int l,
			Integer siteCode3, Date fromDate3, Date toDate3, Integer transactionStatus3, String string2, int m, int n,
			Integer siteCode4, Date fromDate4, Date toDate4, Integer transactionStatus4, int o, int p);





}
