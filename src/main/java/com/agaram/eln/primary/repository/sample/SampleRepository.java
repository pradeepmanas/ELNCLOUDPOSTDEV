package com.agaram.eln.primary.repository.sample;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.agaram.eln.primary.fetchmodel.inventory.Sampleget;
import com.agaram.eln.primary.model.material.Elnmaterial;
import com.agaram.eln.primary.model.sample.Sample;
import com.agaram.eln.primary.model.sample.SampleCategory;
import com.agaram.eln.primary.model.sample.SampleType;
import com.agaram.eln.primary.model.usermanagement.LSprojectmaster;

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


	List<Sample> findBySamplecategoryAndNsitecodeAndCreateddateBetweenOrderBySamplecodeDesc(
			SampleCategory objsamplecat, Integer nsiteInteger, Date fromdate, Date todate);

	List<Sample> findByNsitecodeAndCreateddateBetweenAndNtransactionstatusAndAssignedprojectAndTrackconsumptionNotAndQuantityGreaterThanOrNsitecodeAndCreateddateBetweenAndNtransactionstatusAndAssignedprojectIsNullAndTrackconsumptionNotAndQuantityGreaterThanOrNsitecodeAndCreateddateBetweenAndNtransactionstatusAndAssignedprojectAndTrackconsumptionAndQuantityGreaterThanOrNsitecodeAndCreateddateBetweenAndNtransactionstatusAndAssignedprojectIsNullAndTrackconsumptionAndQuantityGreaterThanOrderBySamplecodeDesc(
			Integer siteCode, Date fromDate, Date toDate, Integer transactionStatus, String string, int i, int j,
			Integer siteCode2, Date fromDate2, Date toDate2, Integer transactionStatus2, int k, int l,
			Integer siteCode3, Date fromDate3, Date toDate3, Integer transactionStatus3, String string2, int m, int n,
			Integer siteCode4, Date fromDate4, Date toDate4, Integer transactionStatus4, int o, int p);

	List<Sample> findByNsitecodeAndCreateddateBetweenAndNtransactionstatusAndAssignedprojectAndTrackconsumptionNotAndQuantityGreaterThanAndSampletypeOrNsitecodeAndCreateddateBetweenAndNtransactionstatusAndAssignedprojectIsNullAndTrackconsumptionNotAndQuantityGreaterThanAndSampletypeOrNsitecodeAndCreateddateBetweenAndNtransactionstatusAndAssignedprojectAndTrackconsumptionAndQuantityGreaterThanAndSampletypeOrNsitecodeAndCreateddateBetweenAndNtransactionstatusAndAssignedprojectIsNullAndTrackconsumptionAndQuantityGreaterThanAndSampletypeOrderBySamplecodeDesc(
			Integer siteCode, Date fromDate, Date toDate, Integer transactionStatus, String string, int i, int j,
			SampleType objSampleType, Integer siteCode2, Date fromDate2, Date toDate2, Integer transactionStatus2,
			int k, int l, SampleType objSampleType2, Integer siteCode3, Date fromDate3, Date toDate3,
			Integer transactionStatus3, String string2, int m, int n, SampleType objSampleType3, Integer siteCode4,
			Date fromDate4, Date toDate4, Integer transactionStatus4, int o, int p, SampleType objSampleType4);

	List<Sample> findByNsitecodeAndCreateddateBetweenAndNtransactionstatusAndAssignedprojectAndTrackconsumptionNotAndQuantityGreaterThanAndSamplecategoryOrNsitecodeAndCreateddateBetweenAndNtransactionstatusAndAssignedprojectIsNullAndTrackconsumptionNotAndQuantityGreaterThanAndSamplecategoryOrNsitecodeAndCreateddateBetweenAndNtransactionstatusAndAssignedprojectAndTrackconsumptionAndQuantityGreaterThanAndSamplecategoryOrNsitecodeAndCreateddateBetweenAndNtransactionstatusAndAssignedprojectIsNullAndTrackconsumptionAndQuantityGreaterThanAndSamplecategoryOrderBySamplecodeDesc(
			Integer siteCode, Date fromDate, Date toDate, Integer transactionStatus, String string, int i, int j,
			SampleCategory objSampleCategory, Integer siteCode2, Date fromDate2, Date toDate2,
			Integer transactionStatus2, int k, int l, SampleCategory objSampleCategory2, Integer siteCode3,
			Date fromDate3, Date toDate3, Integer transactionStatus3, String string2, int m, int n,
			SampleCategory objSampleCategory3, Integer siteCode4, Date fromDate4, Date toDate4,
			Integer transactionStatus4, int o, int p, SampleCategory objSampleCategory4);


	List<Sample> findByNsitecodeAndCreateddateBetweenAndNtransactionstatusAndAssignedprojectAndTrackconsumptionNotAndQuantityGreaterThanAndSampletypeAndSamplecategoryOrNsitecodeAndCreateddateBetweenAndNtransactionstatusAndAssignedprojectIsNullAndTrackconsumptionNotAndQuantityGreaterThanAndSampletypeAndSamplecategoryOrNsitecodeAndCreateddateBetweenAndNtransactionstatusAndAssignedprojectAndTrackconsumptionAndQuantityGreaterThanAndSampletypeAndSamplecategoryOrNsitecodeAndCreateddateBetweenAndNtransactionstatusAndAssignedprojectIsNullAndTrackconsumptionAndQuantityGreaterThanAndSampletypeAndSamplecategoryOrderBySamplecodeDesc(
			Integer siteCode, Date fromDate, Date toDate, Integer transactionStatus, String string, int i, int j,
			SampleType objSampleType, SampleCategory objSampleCategory, Integer siteCode2, Date fromDate2, Date toDate2,
			Integer transactionStatus2, int k, int l, SampleType objSampleType2, SampleCategory objSampleCategory2,
			Integer siteCode3, Date fromDate3, Date toDate3, Integer transactionStatus3, String string2, int m, int n,
			SampleType objSampleType3, SampleCategory objSampleCategory3, Integer siteCode4, Date fromDate4,
			Date toDate4, Integer transactionStatus4, int o, int p, SampleType objSampleType4,
			SampleCategory objSampleCategory4);

	List<Sampleget> findByNstatusAndNsitecodeOrderBySamplecodeDesc(Integer status, Integer nsitecode);

	@Transactional
	@Query(value = "select s.* from sample s "
			+ "where s.samplecategory_nsamplecatcode = ?1 and s.nsitecode = ?2 and "
			+ "s.createddate BETWEEN ?3 AND ?4 and (select count(*) from sampleprojectmap where samplecode = s.samplecode) = 0 Order By samplecode Desc",nativeQuery = true)
	List<Sample> getSampleOnGeneralProjects(Integer objMaterialCategory, Integer nsiteInteger, Date fromDate, Date toDate);

	@Transactional
	@Query(value = "select s.* from sample s "
			+ "where s.samplecategory_nsamplecatcode = ?1 and s.nsitecode = ?2 and "
			+ "s.createddate BETWEEN ?3 AND ?4 and (select count(*) from sampleprojectmap where samplecode = s.samplecode and lsproject_projectcode in (?5)) > 0 Order By samplecode Desc",nativeQuery = true)
	List<Sample> getSampleOnProjects(Integer objMaterialCategory, Integer nsiteInteger, Date fromDate, Date toDate, List<Integer> projectcode);

	List<Sample> findByNsitecodeAndCreateddateBetweenAndNtransactionstatusAndSamplecodeNotInAndSamplecategoryOrderBySamplecodeDesc(
			Integer siteCode, Date fromDate, Date toDate, Integer transactionStatus, List<Integer> sampleCodes,
			SampleCategory objSampleCategory);
	
	@Query("SELECT e FROM Sample e WHERE e.nsitecode = :nsitecode "
	        + "AND e.createddate BETWEEN :fromDate AND :toDate "
	        + "AND e.ntransactionstatus = :ntransactionstatus "
	        + "AND e.samplecode NOT IN (SELECT m.samplecode FROM SampleProjectMap m) "
	        + "AND e.sampletype.nsampletypecode IN "
	        + "(SELECT c.nsampletypecode FROM SampleType c WHERE c.nstatus = 1) "
	        + "AND e.samplecategory.nsamplecatcode IN "
	        + "(SELECT t.nsamplecatcode FROM SampleCategory t WHERE t.nstatus = 1) "
	        + "ORDER BY e.samplecode DESC")
	List<Sample> findElnmaterialsWithoutProjectMaps(
	        @Param("nsitecode") Integer nsitecode,
	        @Param("fromDate") Date fromDate,
	        @Param("toDate") Date toDate,
	        @Param("ntransactionstatus") Integer transactionStatus);
	
	
	@Query("SELECT e FROM Sample e WHERE e.nsitecode = :nsitecode "
	        + "AND e.createddate BETWEEN :fromDate AND :toDate "
	        + "AND e.ntransactionstatus = :ntransactionstatus "
	        + "AND e.samplecode IN (SELECT m.samplecode FROM SampleProjectMap m WHERE lsproject =:project)"
	        + "AND e.sampletype.nsampletypecode IN "
	        + "(SELECT c.nsampletypecode FROM SampleType c WHERE c.nstatus = 1) "
	        + "AND e.samplecategory.nsamplecatcode IN "
	        + "(SELECT t.nsamplecatcode FROM SampleCategory t WHERE t.nstatus = 1) "
	        + "ORDER BY e.samplecode DESC")
	List<Sample> findElnmaterialsWithProjectMaps(
	        @Param("nsitecode") Integer nsitecode,
	        @Param("fromDate") Date fromDate,
	        @Param("toDate") Date toDate,
	        @Param("ntransactionstatus") Integer transactionStatus,@Param("project") LSprojectmaster project);

	@Query("SELECT e FROM Sample e WHERE e.nsitecode = :nsitecode "
	        + "AND e.createddate BETWEEN :fromDate AND :toDate "
	        + "AND e.ntransactionstatus = :ntransactionstatus "
	        + "AND e.samplecode IN (SELECT m.samplecode FROM SampleProjectMap m WHERE lsproject =:project)"
	        + "AND e.sampletype.nsampletypecode IN "
	        + "(SELECT c.nsampletypecode FROM SampleType c WHERE c.nstatus = 1) "
	        + "AND e.samplecategory.nsamplecatcode IN "
	        + "(SELECT t.nsamplecatcode FROM SampleCategory t WHERE t.nstatus = 1) "
	        + "AND e.sampletype = :objSampleType "
	        + "AND e.samplecategory = :objSampleCategory "
	        + "ORDER BY e.samplecode DESC")
	List<Sample> findElnmaterialsWithProjectMapsforfilter(
	        @Param("nsitecode") Integer nsitecode,
	        @Param("fromDate") Date fromDate,
	        @Param("toDate") Date toDate,
	        @Param("ntransactionstatus") Integer transactionStatus,@Param("project") LSprojectmaster project,@Param("objSampleType") SampleType objSampleType,
	        @Param("objSampleCategory") SampleCategory objSampleCategory);
	
	
	@Query("SELECT e FROM Sample e WHERE e.nsitecode = :nsitecode "
	        + "AND e.createddate BETWEEN :fromDate AND :toDate "
	        + "AND e.ntransactionstatus = :ntransactionstatus "
	        + "AND e.samplecode NOT IN (SELECT m.samplecode FROM SampleProjectMap m) "
	        + "AND e.sampletype.nsampletypecode IN "
	        + "(SELECT c.nsampletypecode FROM SampleType c WHERE c.nstatus = 1) "
	        + "AND e.samplecategory.nsamplecatcode IN "
	        + "(SELECT t.nsamplecatcode FROM SampleCategory t WHERE t.nstatus = 1) "
	        + "AND e.sampletype = :objSampleType "
	        + "AND e.samplecategory = :objSampleCategory "
	        + "ORDER BY e.samplecode DESC")
	List<Sample> findElnmaterialsWithoutProjectMapsforfilter(
	        @Param("nsitecode") Integer nsitecode,
	        @Param("fromDate") Date fromDate,
	        @Param("toDate") Date toDate,
	        @Param("ntransactionstatus") Integer transactionStatus,@Param("objSampleType") SampleType objSampleType,
	        @Param("objSampleCategory") SampleCategory objSampleCategory);
	
	
	@Query("SELECT e FROM Sample e WHERE e.nsitecode = :nsitecode "
	        + "AND e.createddate BETWEEN :fromDate AND :toDate "
	        + "AND e.ntransactionstatus = :ntransactionstatus "
	        + "AND e.samplecode IN (SELECT m.samplecode FROM SampleProjectMap m WHERE lsproject =:project)"
	        + "AND e.sampletype.nsampletypecode IN "
	        + "(SELECT c.nsampletypecode FROM SampleType c WHERE c.nstatus = 1) "
	        + "AND e.samplecategory.nsamplecatcode IN "
	        + "(SELECT t.nsamplecatcode FROM SampleCategory t WHERE t.nstatus = 1) "
	        + "AND e.sampletype = :objSampleType "
	        + "ORDER BY e.samplecode DESC")
	List<Sample> findElnmaterialsWithProjectMapsforfiltersampletype(
	        @Param("nsitecode") Integer nsitecode,
	        @Param("fromDate") Date fromDate,
	        @Param("toDate") Date toDate,
	        @Param("ntransactionstatus") Integer transactionStatus,@Param("project") LSprojectmaster project,@Param("objSampleType") SampleType objSampleType
	       );

	@Query("SELECT e FROM Sample e WHERE e.nsitecode = :nsitecode "
	        + "AND e.createddate BETWEEN :fromDate AND :toDate "
	        + "AND e.ntransactionstatus = :ntransactionstatus "
	        + "AND e.samplecode NOT IN (SELECT m.samplecode FROM SampleProjectMap m) "
	        + "AND e.sampletype.nsampletypecode IN "
	        + "(SELECT c.nsampletypecode FROM SampleType c WHERE c.nstatus = 1) "
	        + "AND e.samplecategory.nsamplecatcode IN "
	        + "(SELECT t.nsamplecatcode FROM SampleCategory t WHERE t.nstatus = 1) "
	        + "AND e.sampletype = :objSampleType "
	        + "ORDER BY e.samplecode DESC")
	List<Sample> findElnmaterialsWithoutProjectMapsforfilterforsampletype(
	        @Param("nsitecode") Integer nsitecode,
	        @Param("fromDate") Date fromDate,
	        @Param("toDate") Date toDate,
	        @Param("ntransactionstatus") Integer transactionStatus,@Param("objSampleType") SampleType objSampleType
	        );
	
	@Query("SELECT e FROM Sample e WHERE e.nsitecode = :nsitecode "
	        + "AND e.createddate BETWEEN :fromDate AND :toDate "
	        + "AND e.ntransactionstatus = :ntransactionstatus "
	        + "AND e.samplecode IN (SELECT m.samplecode FROM SampleProjectMap m WHERE lsproject =:project)"
	        + "AND e.sampletype.nsampletypecode IN "
	        + "(SELECT c.nsampletypecode FROM SampleType c WHERE c.nstatus = 1) "
	        + "AND e.samplecategory.nsamplecatcode IN "
	        + "(SELECT t.nsamplecatcode FROM SampleCategory t WHERE t.nstatus = 1) "
	        + "AND e.samplecategory = :objSampleCategory "
	        + "ORDER BY e.samplecode DESC")
	List<Sample> findElnmaterialsWithProjectMapsforfilterforsamplecat(
	        @Param("nsitecode") Integer nsitecode,
	        @Param("fromDate") Date fromDate,
	        @Param("toDate") Date toDate,
	        @Param("ntransactionstatus") Integer transactionStatus,@Param("project") LSprojectmaster project,
	        @Param("objSampleCategory") SampleCategory objSampleCategory);
	
	
	@Query("SELECT e FROM Sample e WHERE e.nsitecode = :nsitecode "
	        + "AND e.createddate BETWEEN :fromDate AND :toDate "
	        + "AND e.ntransactionstatus = :ntransactionstatus "
	        + "AND e.samplecode NOT IN (SELECT m.samplecode FROM SampleProjectMap m) "
	        + "AND e.sampletype.nsampletypecode IN "
	        + "(SELECT c.nsampletypecode FROM SampleType c WHERE c.nstatus = 1) "
	        + "AND e.samplecategory.nsamplecatcode IN "
	        + "(SELECT t.nsamplecatcode FROM SampleCategory t WHERE t.nstatus = 1) "
	        + "AND e.samplecategory = :objSampleCategory "
	        + "ORDER BY e.samplecode DESC")
	List<Sample> findElnmaterialsWithoutProjectMapsforfilterforsamplecat(
	        @Param("nsitecode") Integer nsitecode,
	        @Param("fromDate") Date fromDate,
	        @Param("toDate") Date toDate,
	        @Param("ntransactionstatus") Integer transactionStatus,
	        @Param("objSampleCategory") SampleCategory objSampleCategory);
}
