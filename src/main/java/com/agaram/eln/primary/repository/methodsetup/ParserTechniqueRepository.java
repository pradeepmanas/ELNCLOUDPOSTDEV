package com.agaram.eln.primary.repository.methodsetup;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.agaram.eln.primary.model.methodsetup.ParserField;
import com.agaram.eln.primary.model.methodsetup.ParserTechnique;

/**
 * This interface holds JpaRepository method declarations relevant to ParserTechnique.
 * @author ATE153
 * @version 1.0.0
 * @since   15- Mar- 2020
 */
@Repository
public interface ParserTechniqueRepository extends JpaRepository<ParserTechnique, Integer>{

	/**
	 * This interface declaration is used to fetch ParserTechnique Entities based on the primary keys and its
	 * status
	 * @param parserTechniqueKeyList [List] primary keys of ParserTechnique entities
	 * @param status [int] '1'-Active, '-1'-Inactive
	 * @return list of ParserTechnique Entities
	 */
	List<ParserTechnique> findByParsertechniquekeyInAndStatus(final List<Integer> parserTechniqueKeyList, final int status);
	
	/**
	 * This interface declaration is used to get list of active ParserTechnique entities based on Method entity
	 * @param methodKey [int] primary key of Method entity
	 * @return list of active ParserTechnique Entities
	 */
//	@Query(value = "SELECT pt FROM ParserTechnique pt JOIN ParserField pf ON pf.parserfieldkey = pt.parserfield.parserfieldkey"
//			 + " JOIN ParserBlock pb ON pb.parserblockkey = pf.parserblock.parserblockkey "//AND pb.methodkey = ?1")
//			 + "JOIN Method m  ON m.methodkey = pb.method.methodkey AND m.methodkey = ?1 and pt.status=1")
//	List<ParserTechnique> getParserTechniqueByMethodKey(final int methodKey);
	
	List<ParserTechnique> findByParserfieldInAndStatus(List<ParserField> lstparsefield, int Status);
}
