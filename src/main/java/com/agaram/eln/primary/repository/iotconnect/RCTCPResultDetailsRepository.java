package com.agaram.eln.primary.repository.iotconnect;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.agaram.eln.primary.model.equipment.Equipment;
import com.agaram.eln.primary.model.instrumentsetup.InstrumentMaster;
import com.agaram.eln.primary.model.iotconnect.RCTCPResultDetails;
import com.agaram.eln.primary.model.methodsetup.Method;

@Repository
public interface RCTCPResultDetailsRepository extends JpaRepository<RCTCPResultDetails, Integer>{

	List<RCTCPResultDetails> findByMethodAndEquipment(Method method, Equipment equipment);

	@Transactional
	List<RCTCPResultDetails> findByMethodMethodkeyInAndEquipmentNequipmentcodeIn(List<Integer> methodkeys,
			List<Integer> instkeys);
	
//	@Transactional
//	List<RCTCPResultDetails> findByMethodMethodkeyInAndEquipmentNequipmentcodeInAndCreateddateBetween(
//			List<Integer> methodkeys, List<Integer> instkeys, Date fromdate, Date todate);

	@Transactional
	List<RCTCPResultDetails> findByResultidIn(List<Integer> selectedProductsList);

	List<RCTCPResultDetails> findByMethodMethodkeyInAndEquipmentNequipmentcodeInAndCreateddateBetweenAndValueloadedNot(
			List<Integer> methodkeys, List<Integer> instkeys, Date startOfDayFrom, Date endOfDayTo,
			Integer valueloaded);
	
	

}
