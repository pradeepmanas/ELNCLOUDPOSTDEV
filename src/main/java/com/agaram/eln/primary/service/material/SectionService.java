package com.agaram.eln.primary.service.material;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.agaram.eln.primary.global.Enumeration;
import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.material.Section;
import com.agaram.eln.primary.model.material.Unit;
//import com.agaram.eln.primary.model.material.Unit;
import com.agaram.eln.primary.repository.material.SectionRepository;

@Service
public class SectionService {

	@Autowired
	SectionRepository sectionRepository;

	public ResponseEntity<Object> getSection(Integer nsiteInteger) {
		List<Section> lstSection = sectionRepository.findByNstatusAndNsitecodeOrderByNsectioncodeDesc(1,nsiteInteger);

		return new ResponseEntity<>(lstSection, HttpStatus.OK);
	}

	public ResponseEntity<Object> createSection(Section section) {
		final Section sectionObjByName = sectionRepository.findBySsectionnameAndNstatusAndNsitecode(section.getSsectionname(), 1,section.getNsitecode());

		if (sectionObjByName == null && section.getNsectioncode() == null) {
			section.setNdefaultstatus(1);
			section.setNsitecode(section.getNsitecode());
			section.setNstatus(1);

			sectionRepository.save(section);

			return getSection(section.getNsitecode());
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

		final Section unitobj = sectionRepository.findByNsectioncodeAndNstatus(section.getNsectioncode(), 1);
		if (objSetion == null) {
			return new ResponseEntity<>(Enumeration.ReturnStatus.ALREADYDELETED.getreturnstatus(),
					HttpStatus.EXPECTATION_FAILED);
		} else {

			final Section objSetion1 = sectionRepository.findBySsectionnameAndNstatusAndNsitecode(section.getSsectionname(), 1,section.getNsitecode());

			if (objSetion1 == null || (objSetion.getNsectioncode() == section.getNsectioncode())) {
				unitobj.setObjsilentaudit(section.getObjsilentaudit());
				sectionRepository.save(section);
				return getSection(section.getNsitecode());
			} else {
				return new ResponseEntity<>(Enumeration.ReturnStatus.ALREADYEXISTS.getreturnstatus(),
						HttpStatus.CONFLICT);
			}

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
			return getSection(section.getNsitecode());

		}
	}

}