package com.agaram.eln.primary.repository.usermanagement;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.agaram.eln.primary.model.usermanagement.LSnotification;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;

public interface LSnotificationRepository extends JpaRepository<LSnotification, Long> {
	public Long countByNotifationtoAndIsnewnotification(LSuserMaster lsuserMaster, Integer isnew);
	public List<LSnotification> findFirst10ByNotifationtoOrderByNotificationcodeDesc(LSuserMaster lsuserMaster);
	public Long countByNotifationtoAndIsnewnotificationAndNotificationcodeGreaterThan(LSuserMaster lsuserMaster, Integer isnew, Long notificationcode);
	public List<LSnotification> findByNotifationtoAndNotificationcodeGreaterThanOrderByNotificationcodeDesc(LSuserMaster lsuserMaster, Long notificationcode);
	public List<LSnotification> findFirst10ByNotifationtoAndNotificationcodeLessThanOrderByNotificationcodeDesc(LSuserMaster lsuserMaster, Long notificationcode);
	
	@Transactional
	@Modifying
	@Query("update LSnotification o set o.isnewnotification = 0 where o.notifationto = ?1 and notificationcode <= ?2")
	void updatenotificationstatus(LSuserMaster lsuserMaster, Long notificationcode);
	public LSnotification findByRepositorycodeAndRepositorydatacode(Integer repositorycode, Integer repositorydatacode);
	public LSnotification findByRepositorycodeAndRepositorydatacodeAndNotificationdetils(Integer repositorycode,
			Integer repositorydatacode, String details);
}
