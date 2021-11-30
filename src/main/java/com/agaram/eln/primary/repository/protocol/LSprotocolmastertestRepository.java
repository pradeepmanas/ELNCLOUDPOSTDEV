package com.agaram.eln.primary.repository.protocol;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.protocols.LSprotocolmastertest;;

public interface LSprotocolmastertestRepository extends JpaRepository<LSprotocolmastertest, Integer>{

	List<LSprotocolmastertest> findByTestcodeAndTesttype(Integer testcode, Integer testtype);

}
