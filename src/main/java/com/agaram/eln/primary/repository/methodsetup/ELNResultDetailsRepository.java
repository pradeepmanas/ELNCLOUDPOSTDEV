package com.agaram.eln.primary.repository.methodsetup;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.agaram.eln.primary.model.methodsetup.MethodDelimiter;
import com.agaram.eln.primary.model.methodsetup.Delimiter;
import com.agaram.eln.primary.model.methodsetup.ParserMethod;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.methodsetup.Method;
import com.agaram.eln.primary.model.methodsetup.ParserBlock;
import com.agaram.eln.primary.model.methodsetup.ELNResultDetails;

/**
 * This interface holds JpaRepository ELNResultDetails declarations relevant to Method.
 * @author ATE113
 * @version 1.0.0
 * @since   20- Jan- 2022
 */
@Repository
public interface ELNResultDetailsRepository extends JpaRepository<ELNResultDetails, Integer>{
	
	/**
	 * This interface declaration is used to get list of ELNResultDetails entities based on its status.
	 * @param status [int] 1 - Active or -1- Inactive
	 * @param sort [Sort] to sort the retrieved list based on 'field' and 'sort direction' provided through
	 *             value=Sort.by(Sort.Direction.DESC, "resultid")
	 * @return list of MethodDelimiter entities
	 */
	//List<ELNResultDetails> findByStatus(final Sort sort);
	
	List<ELNResultDetails> findBySiteAndStatus(final LSSiteMaster site, final int status, final Sort sort);

	List<ELNResultDetails> findBybatchcode(List<Long> batchcode);
	
	
}

