package com.agaram.eln.primary.repository.instrumentDetails;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.instrumentDetails.Lsresulttags;

public interface LsresulttagsRepository extends JpaRepository<Lsresulttags, String>{
	List<Lsresulttags> findByOrderidInOrderByIdDesc(List<Long> batchcode);
}
