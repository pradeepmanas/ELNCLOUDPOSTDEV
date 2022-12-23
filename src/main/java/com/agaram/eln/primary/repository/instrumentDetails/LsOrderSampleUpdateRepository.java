package com.agaram.eln.primary.repository.instrumentDetails;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.agaram.eln.primary.model.instrumentDetails.LSlogilablimsorderdetail;
import com.agaram.eln.primary.model.instrumentDetails.LsOrderSampleUpdate;
import com.agaram.eln.primary.model.protocols.LSlogilabprotocoldetail;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;

public interface LsOrderSampleUpdateRepository extends JpaRepository<LsOrderSampleUpdate, String> {
//	public List<LsOrderSampleUpdate> findByBatchcode(Long Batchcode);

	public List<LsOrderSampleUpdate> findByOrdersampleusedDetail(String ordersampleusedDetail);



	public List<LsOrderSampleUpdate> findByRepositorycodeAndRepositorydatacodeAndQuantityusedNotAndHistorydetailsNotNullOrderByOrdersamplecodeDesc(
			Integer repositorycode, Integer repositorydatacode, int i);


	

	public LSuserMaster findByUsercode(LSuserMaster lsuserMaster);

	public interface UserProjection {
	    String getBatchcode();
	    String getBatchname();
	    Integer getUsercode();
//	    String getComment();
	}

	@Transactional
	@Modifying
	@Query(value = "select DISTINCT ls.batchcode ,(select batchid from lslogilablimsorderdetail  where batchcode=ls.batchcode ) as batchname,usercode from LsOrderSampleUpdate ls where repositorydatacode=?", nativeQuery = true)
public List<UserProjection> getDistinctRepositorydatacode(Integer repositorydatacode);


	
	

	


	
	

	
	//public List<LSlogilablimsorderdetail> findByRepositorydatacode(Integer repositorydatacode);




}
