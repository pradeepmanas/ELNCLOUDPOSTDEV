package com.agaram.eln.primary.service.material;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.agaram.eln.primary.global.Enumeration;
import com.agaram.eln.primary.model.material.Section;
import com.agaram.eln.primary.repository.material.SectionRepository;

@Service
public class SectionService {

	@Autowired
	SectionRepository sectionRepository;

	public ResponseEntity<Object> getSection() {
		List<Section> lstSection = sectionRepository.findByNstatusOrderByNsectioncode(1);

		return new ResponseEntity<>(lstSection, HttpStatus.OK);
	}

	public ResponseEntity<Object> createSection(Section section) {
		final Section sectionObjByName = sectionRepository.findBySsectionnameAndNstatus(section.getSsectionname(), 1);

		if (sectionObjByName == null) {

			sectionRepository.save(section);

			return getSection();

		} else {
			return new ResponseEntity<>(Enumeration.ReturnStatus.ALREADYEXISTS.getreturnstatus(), HttpStatus.CONFLICT);
		}
	}

	public Section getActiveSectionById(int nsectionCode) {
		Section objSection = sectionRepository.findByNsectioncode(nsectionCode);
		return objSection;
	}

	public ResponseEntity<Object> updateSection(Section section) {
		final Section objSetion = getActiveSectionById(section.getNsectioncode());

		if (objSetion == null) {
			return new ResponseEntity<>(Enumeration.ReturnStatus.ALREADYDELETED.getreturnstatus(),
					HttpStatus.EXPECTATION_FAILED);
		} else {

			sectionRepository.save(section);
			return getSection();

		}
	}

	public ResponseEntity<Object> deleteSection(Section section) {
		final Section objSetion = getActiveSectionById(section.getNsectioncode());

		if (objSetion == null) {

			return new ResponseEntity<>(Enumeration.ReturnStatus.ALREADYDELETED.getreturnstatus(),
					HttpStatus.EXPECTATION_FAILED);
		} else {

			objSetion.setNstatus(Enumeration.TransactionStatus.DELETED.gettransactionstatus());
			sectionRepository.save(objSetion);
			return getSection();

		}
	}

}