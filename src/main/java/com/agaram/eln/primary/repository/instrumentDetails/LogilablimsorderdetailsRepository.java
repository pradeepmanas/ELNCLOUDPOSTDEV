package com.agaram.eln.primary.repository.instrumentDetails;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.agaram.eln.primary.fetchmodel.getorders.LogilabOrderDetails;
import com.agaram.eln.primary.model.instrumentDetails.LSlogilablimsorderdetail;

public interface LogilablimsorderdetailsRepository extends JpaRepository<LSlogilablimsorderdetail, Long> {
	
	LogilabOrderDetails findByBatchcode(Long batchcode);	
	
	@Transactional
	@Modifying
	@Query(value="update LSlogilablimsorderdetail set lockeduser=?1 ,lockedusername=?2 ,activeuser=?3 where batchcode=?4",nativeQuery=true)
	void UpdateOrderData(Integer usercode, String username, Integer activeuser, Long long1);

	@Transactional
	@Modifying
	@Query(value = "update LSlogilablimsorderdetail set lockeduser = null, lockedusername = null, activeuser = null where batchcode = ?1", nativeQuery = true)
	void UpdateOrderOnunlockData(Long batchcode);
	
	@Transactional
	@Query(value="select * from LSlogilablimsorderdetail where batchcode= ?1 ", nativeQuery=true)
	List<LSlogilablimsorderdetail> getOrderDetails(Long batchcode);
	
	
}
