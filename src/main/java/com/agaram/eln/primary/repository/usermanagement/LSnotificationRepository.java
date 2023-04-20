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
	public Long countByNotifationtoAndIsnewnotificationAndNotificationfor(LSuserMaster lsuserMaster, Integer isnew,Integer notificationfor);
	public List<LSnotification> findFirst20ByNotifationtoOrderByNotificationcodeDesc(LSuserMaster lsuserMaster);
	public List<LSnotification> findFirst20ByNotifationtoAndNotificationforOrderByNotificationcodeDesc(LSuserMaster lsuserMaster,Integer notificationfor);
	public Long countByNotifationtoAndIsnewnotificationAndNotificationcodeGreaterThan(LSuserMaster lsuserMaster, Integer isnew, Long notificationcode);
	public List<LSnotification> findByNotifationtoAndNotificationcodeGreaterThanOrderByNotificationcodeDesc(LSuserMaster lsuserMaster, Long notificationcode);
	public List<LSnotification> findFirst20ByNotifationtoAndNotificationcodeLessThanOrderByNotificationcodeDesc(LSuserMaster lsuserMaster, Long notificationcode);
	public List<LSnotification> findFirst20ByNotifationtoAndNotificationcodeLessThanAndNotificationforOrderByNotificationcodeDesc(LSuserMaster lsuserMaster, Long notificationcode,Integer notificationfor);
	
	@Transactional
	@Modifying
	@Query("update LSnotification o set o.isnewnotification = 0 where o.notifationto = ?1 and notificationcode = ?2")
	void updatenotificationstatus(LSuserMaster lsuserMaster, Long notificationcode);
	
	@Transactional
	@Modifying
	@Query("update LSnotification o set o.isnewnotification = 0 where o.notifationto = ?1 and o.notificationfor = 1")
	void updatereadallnotificationstatusforme(LSuserMaster lsuserMaster);
	
	@Transactional
	@Modifying
	@Query("update LSnotification o set o.isnewnotification = 0 where o.notifationto = ?1 and o.notificationfor = 2")
	void updatereadallnotificationstatusforteam(LSuserMaster lsuserMaster);
	
	public LSnotification findByRepositorycodeAndRepositorydatacode(Integer repositorycode, Integer repositorydatacode);
	public LSnotification findByRepositorycodeAndRepositorydatacodeAndNotificationdetils(Integer repositorycode,
			Integer repositorydatacode, String details);
	public Object countByNotifationtoAndIsnewnotificationAndNotificationforOrNotifationtoAndIsnewnotificationAndNotificationfor(
			LSuserMaster lsuserMaster, int i, int j, LSuserMaster lsuserMaster2, int k, int l);
}
