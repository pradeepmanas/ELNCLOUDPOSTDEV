package com.agaram.eln.primary.repository.instrumentDetails;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.instrumentDetails.Lselninstrumentmaster;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;

public interface LselninstrumentmasterRepository extends JpaRepository<Lselninstrumentmaster, Integer> {
	public List<Lselninstrumentmaster> findBystatusOrderByInstrumentcodeDesc(Integer status);
	public List<Lselninstrumentmaster> findBylssitemasterAndStatusOrderByInstrumentcodeDesc(LSSiteMaster sitemaster,Integer status);
	public Lselninstrumentmaster findByInstrumentnameAndStatus(String instrumentname, Integer status);
	public Lselninstrumentmaster findByInstrumentnameAndStatusAndInstrumentcodeNot(String instrumentname, Integer status, Integer instrumentcode);
	public Lselninstrumentmaster findByInstrumentnameIgnoreCaseAndStatus(String instrumentname, Integer status);
	public Lselninstrumentmaster findByInstrumentnameIgnoreCaseAndStatusAndInstrumentcodeNot(String instrumentname, Integer status,
			Integer instrumentcode);
}
