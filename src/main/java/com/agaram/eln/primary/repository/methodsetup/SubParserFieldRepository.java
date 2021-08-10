package com.agaram.eln.primary.repository.methodsetup;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.agaram.eln.primary.model.methodsetup.ParserField;
import com.agaram.eln.primary.model.methodsetup.SubParserField;

/**
 * This interface holds JpaRepository method declarations relevant to SubParserField.
 * @author ATE153
 * @version 1.0.0
 * @since   18- Mar- 2020
 */
@Repository
public interface SubParserFieldRepository extends JpaRepository<SubParserField, Integer>{
	/**
	 * This interface declaration is used to fetch SubParserField Entities based on the primary keys and its
	 * status
	 * @param subParserKeyList [List] primary keys of SubParserField entities
	 * @param status [int] '1'-Active, '-1'-Inactive
	 * @return list of SubParserField Entities
	 */
	List<SubParserField> findBySubparserfieldkeyInAndStatus(final List<Integer> subParserKeyList, final int status);

	/**
	 * This interface declaration is used to get list of active SubParserField entities based on Method entity
	 * @param methodKey [int] primary key of Method entity
	 * @return list of active SubParserField Entities
	 */
//	@Query(value = "SELECT spf FROM SubParserField spf JOIN ParserField pf ON pf.parserfieldkey = spf.parserfield.parserfieldkey"
//			 + " JOIN ParserBlock pb ON pb.parserblockkey = pf.parserblock.parserblockkey JOIN Method m "
//			+ " ON m.methodkey = pb.method.methodkey AND m.methodkey = ?1 and spf.status=1" )
//	List<SubParserField> getSubParserFieldByMethodKey(final int methodKey);
	
	List<SubParserField> findByParserfieldInAndStatus(final List<ParserField> parserfield, final int status);           
}
