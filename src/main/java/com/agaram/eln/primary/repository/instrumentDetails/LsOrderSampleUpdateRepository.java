package com.agaram.eln.primary.repository.instrumentDetails;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.instrumentDetails.LsOrderSampleUpdate;

public interface LsOrderSampleUpdateRepository extends JpaRepository<LsOrderSampleUpdate, String> {
//	public List<LsOrderSampleUpdate> findByBatchcode(Long Batchcode);

	public List<LsOrderSampleUpdate> findByOrdersampleusedDetail(String ordersampleusedDetail);



	public List<LsOrderSampleUpdate> findByRepositorycodeAndRepositorydatacodeAndQuantityusedNotAndHistorydetailsNotNullOrderByOrdersamplecodeDesc(
			Integer repositorycode, Integer repositorydatacode, int i);

}
