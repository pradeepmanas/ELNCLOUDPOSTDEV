package com.agaram.eln.primary.repository.methodsetup;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.agaram.eln.primary.model.methodsetup.SubParserTechnique;
import com.agaram.eln.primary.model.methodsetup.MethodDelimiter;
import com.agaram.eln.primary.model.methodsetup.ParserField;
import com.agaram.eln.primary.model.methodsetup.SubParserField;

/**
 * This interface holds JpaRepository method declarations relevant to SubParserTechnique.
 * @author ATE153
 * @version 1.0.0
 * @since   07- Feb- 2020
 */
@Repository
public interface SubParserTechniqueRepository extends JpaRepository<SubParserTechnique, Integer>{

	/**
	 * This interface declaration is used to fetch SubParserTechnique Entities based on the primary keys and its
	 * status
	 * @param techniqueKeyList [List] primary keys of SubParserTechnique entities
	 * @param status [int] '1'-Active, '-1'-Inactive
	 * @return list of SubParserTechnique Entities
	 */
	List<SubParserTechnique> findBySubparsertechniquekeyInAndStatus(final List<Integer> techniqueKeyList, final int status);

	/**
	 * This interface declaration is used to get list of active SubParserTechnique entities based on Method entity
	 * @param methodKey [int] primary key of Method entity
	 * @return list of active SubParserTechnique Entities
	 */
//	@Query(value = "SELECT spt FROM SubParserTechnique spt JOIN ParserField pf ON pf.parserfieldkey = spt.parserfield.parserfieldkey"
//			 + " JOIN ParserBlock pb ON pb.parserblockkey = pf.parserblock.parserblockkey JOIN Method m "
//			+ " ON m.methodkey = pb.method.methodkey AND m.methodkey = ?1 and spt.status=1")
//	List<SubParserTechnique> getSubParserTechniqueByMethodKey(final int methodKey);
	List<SubParserTechnique> findByParserfieldInAndStatus(final List<ParserField> parserfield, final int status);
	
	/**
	 * This interface declaration is used to find list of SubParserTechnique entities based on MethodDelimiter.
	 * @param methodDelimiter [MethodDelimiter] object for which the list is to be fetched
	 * @param status [int] '1'-Active, '-1'-Inactive
	 * @return list of SubParserTechnique Entities
	 */
	List<SubParserTechnique> findByMethoddelimiterAndStatus(final MethodDelimiter methodDelimiter, final int status);
}