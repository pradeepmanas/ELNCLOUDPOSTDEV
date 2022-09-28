package com.agaram.eln.primary.repository.material;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.material.TransactionStatus;;

public interface TransactionStatusRepository extends JpaRepository<TransactionStatus, Integer>{

	public List<TransactionStatus> findByNstatusAndNtranscodeIn(Integer nstatus,List<Integer> lstNtranscode);
}
