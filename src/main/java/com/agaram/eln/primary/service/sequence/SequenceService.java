package com.agaram.eln.primary.service.sequence;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.agaram.eln.primary.fetchmodel.sequence.SequenceTablesh;
import com.agaram.eln.primary.model.sequence.SequenceTable;
import com.agaram.eln.primary.repository.sequence.SequenceTableRepository;

@Service
public class SequenceService {
	@Autowired
	private SequenceTableRepository sequencetableRepository;
	
	public List<SequenceTablesh> getAllSequence()
	{
		return sequencetableRepository.findBySequencecodeNotOrderBySequencecode(-1);
	}
	
	public SequenceTable updatesequence(SequenceTable objClass)
	{
		 sequencetableRepository.updatesequencedata(objClass.getResetperiod(),objClass.getSequenceview(),
				 objClass.getSequenceformat(),objClass.getSeperator(),objClass.getSequencecode());
		 return objClass;
	}
}
