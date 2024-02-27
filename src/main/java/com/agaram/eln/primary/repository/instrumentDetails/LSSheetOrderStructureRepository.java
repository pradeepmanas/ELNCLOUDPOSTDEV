package com.agaram.eln.primary.repository.instrumentDetails;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.agaram.eln.primary.model.instrumentDetails.LSSheetOrderStructure;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;

public interface LSSheetOrderStructureRepository extends JpaRepository<LSSheetOrderStructure, Long> {
	@Transactional
	@Modifying
	@Query("update LSSheetOrderStructure o set o.parentdircode = ?1, o.path = ?2, o.directoryname = ?4 where o.directorycode = ?3")
	void updatedirectory(Long parentdircode, String path, Long directorycode, String directoryname);

	@Transactional
	@Modifying
	@Query("delete from LSSheetOrderStructure o where o.directorycode = ?1")
	void deletedirectory(Long directorycode);

	LSSheetOrderStructure findByParentdircodeAndDirectoryname(Long parentdircode, String directoryname);

	LSSheetOrderStructure findByDirectorycodeAndParentdircodeAndDirectorynameNot(Long directorycode, Long parentdircode,
			String directoryname);

	List<LSSheetOrderStructure> findBySitemasterAndViewoptionOrCreatedbyAndViewoptionOrderByDirectorycode(LSSiteMaster site,Integer siteviewopt,LSuserMaster createduser,Integer userviewopt);
	
	List<LSSheetOrderStructure> findByDirectorycodeIn(List<Long> directorycode);

	List<LSSheetOrderStructure> findBySitemasterAndViewoptionOrCreatedbyAndViewoptionOrSitemasterAndViewoptionAndCreatedbyInOrderByDirectorycode(
			LSSiteMaster lssitemaster, int i, LSuserMaster lsuserMaster, int j, LSSiteMaster lssitemaster2, int k,
			List<LSuserMaster> lstuserMaster);

	LSSheetOrderStructure findByParentdircodeAndDirectorynameIgnoreCase(Long parentdircode, String directoryname);

	LSSheetOrderStructure findByDirectorynameIgnoreCaseAndParentdircode(String directoryname, Long parentdircode);

	List<LSSheetOrderStructure> findBySitemasterAndViewoptionOrCreatedbyAndViewoptionOrCreatedbyAndViewoptionOrderByDirectorycode(
			LSSiteMaster lssitemaster, int i, LSuserMaster lsuserMaster, int j, LSuserMaster lsuserMaster2, int k);

	List<LSSheetOrderStructure> findBySitemasterAndViewoptionAndDirectorynameLikeIgnoreCaseOrCreatedbyAndViewoptionAndDirectorynameLikeIgnoreCaseOrCreatedbyAndViewoptionAndDirectorynameLikeIgnoreCaseOrderByDirectorycode(
			LSSiteMaster lssitemaster, int i, String search_Key, LSuserMaster objusermaster, int j, String search_Key2,
			LSuserMaster objusermaster2, int k, String search_Key3, Pageable pageable);

	List<LSSheetOrderStructure> findBySitemasterAndViewoptionAndDirectorynameLikeIgnoreCaseOrCreatedbyAndViewoptionAndDirectorynameLikeIgnoreCaseOrSitemasterAndViewoptionAndCreatedbyInAndDirectorynameLikeIgnoreCaseOrderByDirectorycode(
			LSSiteMaster lssitemaster, int i, String search_Key, LSuserMaster objusermaster, int j, String search_Key2,
			LSSiteMaster lssitemaster2, int k, List<LSuserMaster> usernotify, String search_Key3, Pageable pageable);

	long countBySitemasterAndViewoptionAndDirectorynameLikeIgnoreCaseOrCreatedbyAndViewoptionAndDirectorynameLikeIgnoreCaseOrCreatedbyAndViewoptionAndDirectorynameLikeIgnoreCaseOrderByDirectorycode(
			LSSiteMaster lssitemaster, int i, String search_Key, LSuserMaster objusermaster, int j, String search_Key2,
			LSuserMaster objusermaster2, int k, String search_Key3);

	long countBySitemasterAndViewoptionAndDirectorynameLikeIgnoreCaseOrCreatedbyAndViewoptionAndDirectorynameLikeIgnoreCaseOrSitemasterAndViewoptionAndCreatedbyInAndDirectorynameLikeIgnoreCaseOrderByDirectorycode(
			LSSiteMaster lssitemaster, int i, String search_Key, LSuserMaster objusermaster, int j, String search_Key2,
			LSSiteMaster lssitemaster2, int k, List<LSuserMaster> usernotify, String search_Key3);


	
}