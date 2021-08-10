package com.agaram.eln.primary.repository.methodsetup;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.agaram.eln.primary.model.methodsetup.Method;
import com.agaram.eln.primary.model.methodsetup.ParserBlock;

/**
 * This interface holds JpaRepository method declarations relevant to ParserBlock.
 * @author ATE153
 * @version 1.0.0
 * @since   18- Mar - 2020
 */
@Repository
public interface ParserBlockRepository extends JpaRepository<ParserBlock, Integer>{

	/**
	 * This interface declaration is used to fetch ParserBlock Entities based on the primary keys and its
	 * status
	 * @param parserBlockKeyList [List] list of primary key of ParserBlock entities
	 * @param status [int] '1'-Active, '-1'-Inactive
	 * @return list of active ParserBlock Entities
	 */
	List<ParserBlock> findByParserblockkeyInAndStatus(final List<Integer> parserBlockKeyList, final int status);

	/**
	 * This interface declaration is used to get list of active ParserBlock entities based on Method entity
	 * @param methodKey [int] primary key of Method entity
	 * @return list of active ParserBlock Entities
	 */
//	@Query(value = "SELECT pb FROM ParserBlock pb JOIN Method m "
//			+ " ON m.methodkey = pb.method.methodkey AND m.methodkey = ?1 and pb.status=1" )
//	List<ParserBlock> getParserBlockByMethodKey(final int methodKey);
	List<ParserBlock> findByMethodAndStatus(final Method methodKey, int status);
	
	
}
