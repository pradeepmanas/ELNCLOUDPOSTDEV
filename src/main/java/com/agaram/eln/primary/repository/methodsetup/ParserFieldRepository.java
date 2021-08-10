package com.agaram.eln.primary.repository.methodsetup;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.agaram.eln.primary.model.methodsetup.ParserField;
import com.agaram.eln.primary.model.methodsetup.MethodDelimiter;
import com.agaram.eln.primary.model.methodsetup.ParserBlock;

/**
 * This interface holds JpaRepository method declarations relevant to ParserField.
 * @author ATE153
 * @version 1.0.0
 * @since   18- Mar - 2020
 */
@Repository
public interface ParserFieldRepository extends JpaRepository<ParserField, Integer>{

	/**
	 * This interface declaration is used to fetch ParserField Entities based on the primary keys and its
	 * status
	 * @param parserFieldKeyList [List] primary keys of ParserField entities
	 * @param status [int] '1'-Active, '-1'-Inactive
	 * @return list of ParserField Entities
	 */
	List<ParserField> findByParserfieldkeyInAndStatus(final List<Integer> parserFieldKeyList, final int status);

	/**
	 * This interface declaration is used to get list of active ParserField entities based on Method entity
	 * @param methodKey [int] primary key of Method entity
	 * @return list of active ParserField Entities
	 */
//	@Query(value = "SELECT pf FROM ParserField pf JOIN ParserBlock pb ON "
//			+ " pb.parserblockkey = pf.parserblock.parserblockkey JOIN Method m "
//			+ " ON m.methodkey = pb.method.methodkey AND m.methodkey = ?1 and pf.status=1" )
//	List<ParserField> getParserFieldByMethodKey(final int methodKey);
	
	//List<ParserField> findByParserblockInAndStatus(List<ParserBlock> parserblock, int status);
	
	/**
	 * This interface declaration is used to list of active ParserField entities based on MethodDelimiter entity
	 * and status.
	 * @param methodDelimiter [MethodDelimiter] object for which the list is to be fetched
	 * @param status [int] '1'-Active, '-1'-Inactive
	 * @return list of active ParserField Entities
	 */
	List<ParserField> findByMethoddelimiterAndStatus(final MethodDelimiter methodDelimiter, final int status);
	
//	@Query(value = "SELECT pf FROM ParserField pf where pf.parserblock.parserblockkey in ?1  and pf.status=1" )
//	List<ParserField> getParserFieldByParserblock(List<Integer> parserBlockList, final int status);
	
	List<ParserField> findByParserblockInAndStatus(List<ParserBlock> parserBlockList, final int status);
}
