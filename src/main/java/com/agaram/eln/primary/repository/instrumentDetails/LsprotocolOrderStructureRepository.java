package com.agaram.eln.primary.repository.instrumentDetails;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.agaram.eln.primary.model.instrumentDetails.Lsprotocolorderstructure;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;


public interface LsprotocolOrderStructureRepository extends JpaRepository<Lsprotocolorderstructure, Long> {

	Lsprotocolorderstructure findByDirectorycodeAndParentdircodeAndDirectorynameNot(Long directorycode, Long parentdircode,
			String directoryname);

	Lsprotocolorderstructure findByParentdircodeAndDirectoryname(Long parentdircode, String directoryname);

	@Transactional
	@Modifying
	@Query("update Lsprotocolorderstructure o set o.parentdircode = ?1, o.path = ?2, o.directoryname = ?4 where o.directorycode = ?3")
	void updatedirectory(Long parentdircode , String path, Long directorycode, String directoryname);

//	List<Lsprotocolorderstructure> findBySitemasterAndViewoptionOrCreatedbyAndViewoptionOrderByDirectorycode(
//			LSSiteMaster lssitemaster, int i, LSuserMaster lsuserMaster, int j);

	List<Lsprotocolorderstructure> findByDirectorycodeIn(List<Long> lstfoldersid);

	List<Lsprotocolorderstructure> findBySitemasterAndViewoptionOrCreatedbyAndViewoptionOrderByDirectorycode(
			LSSiteMaster lssitemaster, int i, LSuserMaster lsuserMaster, int j);

	List<Lsprotocolorderstructure> findBySitemasterAndViewoptionOrCreatedbyAndViewoptionOrSitemasterAndViewoptionAndCreatedbyInOrderByDirectorycode(
			LSSiteMaster lssitemaster, int i, LSuserMaster lsuserMaster, int j, LSSiteMaster lssitemaster2, int k,
			List<LSuserMaster> lstuserMaster);

	Lsprotocolorderstructure findByParentdircodeAndDirectorynameIgnoreCase(Long parentdircode, String directoryname);

	List<Lsprotocolorderstructure> findBySitemasterAndViewoptionOrCreatedbyAndViewoptionOrCreatedbyAndViewoptionOrderByDirectorycode(
			LSSiteMaster lssitemaster, int i, LSuserMaster lsuserMaster, int j, LSuserMaster lsuserMaster2, int k);

	List<Lsprotocolorderstructure> findBySitemasterAndViewoptionAndDirectorynameLikeIgnoreCaseOrCreatedbyAndViewoptionAndDirectorynameLikeIgnoreCaseOrCreatedbyAndViewoptionAndDirectorynameLikeIgnoreCaseOrderByDirectorycode(
			LSSiteMaster lssitemaster, int i, String search_Key, LSuserMaster objusermaster, int j, String search_Key2,
			LSuserMaster objusermaster2, int k, String search_Key3, Pageable pageable);

	List<Lsprotocolorderstructure> findBySitemasterAndViewoptionAndDirectorynameLikeIgnoreCaseOrCreatedbyAndViewoptionAndDirectorynameLikeIgnoreCaseOrSitemasterAndViewoptionAndCreatedbyInAndDirectorynameLikeIgnoreCaseOrderByDirectorycode(
			LSSiteMaster lssitemaster, int i, String search_Key, LSuserMaster objusermaster, int j, String search_Key2,
			LSSiteMaster lssitemaster2, int k, List<LSuserMaster> usernotify, String search_Key3, Pageable pageable);


	

}
