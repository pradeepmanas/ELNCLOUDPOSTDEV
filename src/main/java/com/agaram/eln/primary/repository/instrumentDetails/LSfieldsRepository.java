package com.agaram.eln.primary.repository.instrumentDetails;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.instrumentDetails.LSfields;

public interface LSfieldsRepository extends JpaRepository<LSfields, Integer> {

	public List<LSfields> findByisactive(Integer isactive);

	public List<LSfields> findBymethodname(String methodname);
}
