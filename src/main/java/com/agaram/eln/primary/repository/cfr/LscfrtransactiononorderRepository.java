package com.agaram.eln.primary.repository.cfr;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.agaram.eln.primary.model.cfr.Lscfrtransactiononorder;

public interface LscfrtransactiononorderRepository  extends JpaRepository<Lscfrtransactiononorder, Integer> {
	List<Lscfrtransactiononorder> findByBatchcodeOrderBySerialnoDesc(String string);
}