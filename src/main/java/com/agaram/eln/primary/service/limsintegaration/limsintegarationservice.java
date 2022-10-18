package com.agaram.eln.primary.service.limsintegaration;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import com.agaram.eln.primary.commonfunction.commonfunction;
import com.agaram.eln.primary.fetchmodel.getorders.Logilaborders;
import com.agaram.eln.primary.fetchmodel.gettemplate.Sheettemplateget;
import com.agaram.eln.primary.model.cloudFileManip.CloudOrderCreation;
import com.agaram.eln.primary.model.fileManipulation.ResultorderlimsRefrence;
import com.agaram.eln.primary.model.fileManipulation.SheetorderlimsRefrence;
import com.agaram.eln.primary.model.general.OrderCreation;
import com.agaram.eln.primary.model.instrumentDetails.LSlimsorder;
import com.agaram.eln.primary.model.instrumentDetails.LSlogilablimsorder;
import com.agaram.eln.primary.model.instrumentDetails.LSlogilablimsorderdetail;
import com.agaram.eln.primary.model.instrumentDetails.LsOrderattachments;
import com.agaram.eln.primary.model.instrumentDetails.LsResultlimsOrderrefrence;
import com.agaram.eln.primary.model.instrumentDetails.LsSheetorderlimsrefrence;
import com.agaram.eln.primary.model.sheetManipulation.LSfileparameter;
import com.agaram.eln.primary.model.sheetManipulation.LSfiletest;
import com.agaram.eln.primary.model.sheetManipulation.LStestmaster;
import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.repository.cloudFileManip.CloudOrderCreationRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LSlogilablimsorderdetailRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LsOrderattachmentsRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LsResultlimsOrderrefrenceRepository;
import com.agaram.eln.primary.repository.instrumentDetails.LsSheetorderlimsrefrenceRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LSfileRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LSfileparameterRepository;
import com.agaram.eln.primary.repository.sheetManipulation.LSfiletestRepository;
import com.agaram.eln.primary.service.fileManipulation.FileManipulationservice;
import com.mongodb.gridfs.GridFSDBFile;

@Service
public class limsintegarationservice {

	@Autowired
	private LSfileRepository lsfileRepository;

	@Autowired
	private LSfiletestRepository LSfiletestRepository;

	@Autowired
	private LsSheetorderlimsrefrenceRepository LsSheetorderlimsrefrenceRepository;

	@Autowired
	private FileManipulationservice fileManipulationservice;

	@Autowired
	private LsResultlimsOrderrefrenceRepository LsResultlimsOrderrefrenceRepository;

	@Autowired
	private LSfileparameterRepository lSfileparameterRepository;

	@Autowired
	private LSlogilablimsorderdetailRepository LSlogilablimsorderdetailRepository;

	@Autowired
	private LsOrderattachmentsRepository LsOrderattachmentsRepository;

	@Autowired
	private CloudOrderCreationRepository cloudOrderCreationRepository;

	@Autowired
	private GridFsTemplate gridFsTemplate;

	@Autowired
	private MongoTemplate mongoTemplate;

	public Map<String, Object> getSheets(LStestmaster objTest) {

		Map<String, Object> map = new HashMap<>();

		List<Sheettemplateget> lstsheets = new ArrayList<Sheettemplateget>();

		LSSiteMaster site = new LSSiteMaster();

		site.setSitecode(1);

		if (objTest.getNtestcode() == -1) {

			lstsheets = lsfileRepository.findByApprovedAndLssitemasterAndFilecodeGreaterThan(1, site, 1);

		} else {

			List<LSfiletest> lstTest = LSfiletestRepository.findByTestcodeAndTesttype(objTest.getNtestcode(), 0);

//			List<LSfiletest> lstTest = LSfiletestRepository.findByTestcode(objTest.getNtestcode());

			if (lstTest.size() > 0) {

				lstsheets = lsfileRepository.findBylstestIn(lstTest);

			}
		}

		map.put("lstSheets", lstsheets);

		return map;

	}

	public LsSheetorderlimsrefrence downloadSheetFromELN(LsSheetorderlimsrefrence objattachments) {

		//System.out.print("Sheet download lims call service " + objattachments);
		/// batch code means file code
		LsSheetorderlimsrefrence objSheet = LsSheetorderlimsrefrenceRepository
				.findFirst1BybatchcodeOrderByRefrencecodeDesc(objattachments.getBatchcode());

		if (objSheet != null) {

			SheetorderlimsRefrence objfile = fileManipulationservice.LimsretrieveELNsheet(objSheet);

			if (objfile != null) {
				objattachments.setFile(objfile.getFile());
			}

		}

		return objattachments;
	}

	public Boolean updateSheetsParameterForELN(List<LSfileparameter> objattachments) {

		if (objattachments.size() > 0) {

			if (objattachments.get(0).getFilecode() != null) {

				lSfileparameterRepository.save(objattachments);
			}

		}

		return true;
	}

	public LsResultlimsOrderrefrence downloadResultSheetFromELN(LsResultlimsOrderrefrence objattachments) {

		System.out.print("Sheet download lims call service " + objattachments);

		LsResultlimsOrderrefrence objSheet = LsResultlimsOrderrefrenceRepository
				.findFirst1BybatchidOrderByRefrencecodeDesc(objattachments.getBatchid());

		if (objSheet != null) {

			ResultorderlimsRefrence objfile = fileManipulationservice.LimsretrieveResultELNsheet(objSheet);

			if (objfile != null) {
				objattachments.setFile(objfile.getFile());
			}

		}

		return objattachments;
	}

//	public List<LsOrderattachments> getAttachmentsForLIMS(LSlimsorder objOrder) {
//
////		LSlimsorder limsOrder = LSlimsorderRepository.findByBatchid(objOrder.getBatchid());
//
//		LSlogilablimsorderdetail orderClass = LSlogilablimsorderdetailRepository.findByBatchid(objOrder.getBatchid());
//
//		List<LsOrderattachments> lstAttachments = new ArrayList<LsOrderattachments>();
//
//		if (orderClass != null) {
//
//			lstAttachments = LsOrderattachmentsRepository
//					.findByBatchcodeOrderByAttachmentcodeDesc(orderClass.getBatchcode());
//
//		}
//
//		return lstAttachments;
//	}
	
	public List<LsOrderattachments> getAttachmentsForLIMS(LSlogilablimsorder objOrder) {

//		LSlimsorder limsOrder = LSlimsorderRepository.findByBatchid(objOrder.getBatchid());

		LSlogilablimsorderdetail orderClass = LSlogilablimsorderdetailRepository.findByBatchid(objOrder.getBatchid());

		List<LsOrderattachments> lstAttachments = new ArrayList<LsOrderattachments>();

		if (orderClass != null) {

			lstAttachments = LsOrderattachmentsRepository
					.findByBatchcodeOrderByAttachmentcodeDesc(orderClass.getBatchcode());

		}

		return lstAttachments;
	}

	public List<Logilaborders> getOrdersFromELN(Map<String, Object> obj) {

		List<Logilaborders> orderlst = new ArrayList<Logilaborders>();

		orderlst = LSlogilablimsorderdetailRepository.findByOrderflagOrderByBatchcodeDesc("R");

		return orderlst;
	}

	public List<String> getOrderTagFromELN(Map<String, Object> obj) {
		
		List<String> objlstStr = new ArrayList<String>();
		
		Long batchcode = Long.valueOf((Integer) obj.get("batchcode"));
		Integer ismultitenant = (Integer) obj.get("ismultitenant");

		LSlogilablimsorderdetail objOrder = LSlogilablimsorderdetailRepository.findOne(batchcode);

		if (objOrder != null) {

			String content = "";

			if (objOrder.getLssamplefile() != null) {
				if (ismultitenant == 1) {
					CloudOrderCreation file = cloudOrderCreationRepository
							.findById((long) objOrder.getLssamplefile().getFilesamplecode());
					if (file != null) {
						content = file.getContent();
					}
				} else {

					String fileid = "order_" + objOrder.getLssamplefile().getFilesamplecode();
					GridFSDBFile largefile = gridFsTemplate.findOne(new Query(Criteria.where("filename").is(fileid)));
					if (largefile == null) {
						largefile = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(fileid)));
					}
					if (largefile != null) {
						String filecontent = new BufferedReader(
								new InputStreamReader(largefile.getInputStream(), StandardCharsets.UTF_8)).lines()
										.collect(Collectors.joining("\n"));
						content = filecontent;

					} else {
						OrderCreation file = mongoTemplate.findById(objOrder.getLssamplefile().getFilesamplecode(),
								OrderCreation.class);
						if (file != null) {
							content = file.getContent();
						}
					}
				}
			}
			
			if(!content.equals("")) {
				
				objlstStr = commonfunction.getTagValues(content);
			}
		}

		return objlstStr;
	}

}