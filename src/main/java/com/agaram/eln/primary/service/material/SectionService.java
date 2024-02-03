package com.agaram.eln.primary.service.material;

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.agaram.eln.primary.commonfunction.commonfunction;
import com.agaram.eln.primary.global.Enumeration;
import com.agaram.eln.primary.model.general.Response;
import com.agaram.eln.primary.model.material.Section;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
//import com.agaram.eln.primary.model.material.Unit;
import com.agaram.eln.primary.repository.material.SectionRepository;

@Service
public class SectionService {

	@Autowired
	SectionRepository sectionRepository;

	public ResponseEntity<Object> getSection(Integer nsiteInteger) {
		List<Section> lstSection = sectionRepository.findByNsitecodeOrderByNsectioncodeDesc(nsiteInteger);

		return new ResponseEntity<>(lstSection, HttpStatus.OK);
	}

	public ResponseEntity<Object> createSection(Section section) {
		section.setResponse(new Response());
		final Section sectionObjByName = sectionRepository.findBySsectionnameIgnoreCaseAndNsitecodeAndNstatus(section.getSsectionname(),section.getNsitecode(),1);
		LSuserMaster objMaster = new LSuserMaster();
		objMaster.setUsercode(section.getObjsilentaudit().getLsuserMaster());
		if (sectionObjByName == null && section.getNsectioncode() == null) {
			section.setNdefaultstatus(1);
			section.setNsitecode(section.getNsitecode());
			section.setNstatus(1);
			section.setCreateby(objMaster);
			try {
				section.setCreatedate(commonfunction.getCurrentUtcTime());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			sectionRepository.save(section);
			section.getResponse().setStatus(true);
			section.getResponse().setInformation("IDS_SUCCESS");
			return new ResponseEntity<>(section, HttpStatus.OK);
		} else {
			section.getResponse().setStatus(false);
			section.getResponse().setInformation("IDS_ALREADYEXIST");
			return new ResponseEntity<>(section, HttpStatus.OK);
		}
	}

	public Section getActiveSectionById(int nsectionCode) {
		Section objSection = sectionRepository.findByNsectioncode(nsectionCode);
		return objSection;
	}

	
	public ResponseEntity<Object> updateSection(Section section) {
		section.setResponse(new Response());
		final Section sectionobj = sectionRepository.findByNsectioncode(section.getNsectioncode());
		if (sectionobj == null) {
			section.getResponse().setStatus(false);
			section.getResponse().setInformation("IDS_ALREADYDELETED");
			return new ResponseEntity<>(section, HttpStatus.OK);
		} else {
			final Section objSetion1 = sectionRepository.findBySsectionnameIgnoreCaseAndNsitecodeAndNstatus(section.getSsectionname(),section.getNsitecode(),1);
			if (objSetion1 == null || (objSetion1.getNsectioncode().equals(sectionobj.getNsectioncode()))) {
				sectionRepository.save(section);
				section.setObjsilentaudit(section.getObjsilentaudit());
				section.getResponse().setStatus(true);
				section.getResponse().setInformation("IDS_SUCCESS");
				return new ResponseEntity<>(section, HttpStatus.OK);
			} else {
				section.getResponse().setStatus(false);
				section.getResponse().setInformation("IDS_ALREADYEXIST");
				return new ResponseEntity<>(section, HttpStatus.OK);
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