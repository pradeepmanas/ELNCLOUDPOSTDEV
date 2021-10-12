package com.agaram.eln.primary.repository.archieve;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.archieve.LsProjectarchieve;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;

public interface LsProjectarchieveRepository extends JpaRepository<LsProjectarchieve, Integer>{

	public List<LsProjectarchieve> findByLssitemasterOrderByProjectarchievecodeDesc(LSSiteMaster lssitemaster);
}
