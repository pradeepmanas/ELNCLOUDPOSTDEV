package com.agaram.eln.primary.service.fileManipulation;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.agaram.eln.primary.commonfunction.commonfunction;
import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.fileManipulation.OrderAttachment;
import com.agaram.eln.primary.model.fileManipulation.ProfilePicture;
import com.agaram.eln.primary.model.fileManipulation.ResultorderlimsRefrence;
import com.agaram.eln.primary.model.fileManipulation.SheetorderlimsRefrence;
import com.agaram.eln.primary.model.fileManipulation.UserSignature;
import com.agaram.eln.primary.model.instrumentDetails.LsOrderattachments;
import com.agaram.eln.primary.model.instrumentDetails.LsResultlimsOrderrefrence;
import com.agaram.eln.primary.model.instrumentDetails.LsSheetorderlimsrefrence;
import com.agaram.eln.primary.model.methodsetup.ELNFileAttachments;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.repository.cfr.LScfttransactionRepository;
import com.agaram.eln.primary.repository.fileManipulation.OrderAttachmentRepository;
import com.agaram.eln.primary.repository.fileManipulation.ProfilePictureRepository;
import com.agaram.eln.primary.repository.fileManipulation.ResultorderlimsRefrenceRepository;
import com.agaram.eln.primary.repository.fileManipulation.SheetorderlimsRefrenceRepository;
import com.agaram.eln.primary.repository.fileManipulation.UserSignatureRepository;
import com.agaram.eln.primary.repository.usermanagement.LSuserMasterRepository;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSDBFile;

@Service
public class FileManipulationservice {
	@Autowired
	private ProfilePictureRepository profilePictureRepository;

	@Autowired
	private OrderAttachmentRepository orderAttachmentRepository;

	@Autowired
	private SheetorderlimsRefrenceRepository sheetorderlimsRefrenceRepository;

	@Autowired
	private GridFsTemplate gridFsTemplate;

	@Autowired
	private LScfttransactionRepository lscfttransactionRepository;

	@Autowired
	private LSuserMasterRepository lsuserMasterRepository;
	
	@Autowired
	private ResultorderlimsRefrenceRepository ResultorderlimsRefrenceRepository;

	@Autowired
	private UserSignatureRepository UserSignatureRepository;
	
	public ProfilePicture addPhoto(Integer usercode, MultipartFile file, Date currentdate) throws IOException {

		LSuserMaster username = lsuserMasterRepository.findByusercode(usercode);
		String name = username.getUsername();
		LScfttransaction list = new LScfttransaction();
		list.setModuleName("UserManagement");
		list.setComments(name + " " + "Uploaded the profile picture successfully");
		list.setActions("View / Load");
		list.setSystemcoments("System Generated");
		list.setTableName("profile");
//		list.setTransactiondate(currentdate);
//	    	list.setLsuserMaster(lsuserMasterRepository.findByusercode(usercode));
		list.setLsuserMaster(usercode);			try {
			list.setTransactiondate(commonfunction.getCurrentUtcTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		lscfttransactionRepository.save(list);
		deletePhoto(usercode, list);

		ProfilePicture profile = new ProfilePicture();
		profile.setId(usercode);
		profile.setName(file.getName());
		profile.setImage(new Binary(BsonBinarySubType.BINARY, file.getBytes()));
		profile = profilePictureRepository.insert(profile);

		return profile;
	}

	
	public UserSignature addsignature(Integer usercode, MultipartFile file, Date currentdate) throws IOException {

//		LSuserMaster username = lsuserMasterRepository.findByusercode(usercode);
//		String name = username.getUsername();
		LScfttransaction list = new LScfttransaction();
		list.setModuleName("UserManagement");
		//list.setComments(name + " " + "Uploaded the profile picture successfully");
		list.setActions("View / Load");
		list.setSystemcoments("System Generated");
		list.setTableName("profile");
//		list.setTransactiondate(currentdate);
//	    	list.setLsuserMaster(lsuserMasterRepository.findByusercode(usercode));
		list.setLsuserMaster(usercode);
		try {
			list.setTransactiondate(commonfunction.getCurrentUtcTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		lscfttransactionRepository.save(list);
		deleteSignature(usercode, list);

		UserSignature Signature = new UserSignature();
		Signature.setId(usercode);
		Signature.setName(file.getName());
		Signature.setImage(new Binary(BsonBinarySubType.BINARY, file.getBytes()));
		Signature = UserSignatureRepository.insert(Signature);

		return Signature;
	}
	
	
	public Long deleteSignature(Integer id, LScfttransaction list) {
		list.setTableName("UserSignature");
		try {
			list.setTransactiondate(commonfunction.getCurrentUtcTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		lscfttransactionRepository.save(list);
		return UserSignatureRepository.deleteById(id);
	}

	public UserSignature getsignature(Integer id) {

		return UserSignatureRepository.findById(id);
	}
	
	public ProfilePicture getPhoto(Integer id) {

		return profilePictureRepository.findById(id);
	}

	public Long deletePhoto(Integer id, LScfttransaction list) {
		list.setTableName("ProfilePicture");
		try {
			list.setTransactiondate(commonfunction.getCurrentUtcTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		lscfttransactionRepository.save(list);
		return profilePictureRepository.deleteById(id);
	}

	public OrderAttachment storeattachment(MultipartFile file) throws IOException {
		UUID objGUID = UUID.randomUUID();
		String randomUUIDString = objGUID.toString();

		OrderAttachment objattachment = new OrderAttachment();
		objattachment.setId(randomUUIDString);
		objattachment.setFile(new Binary(BsonBinarySubType.BINARY, file.getBytes()));

		objattachment = orderAttachmentRepository.insert(objattachment);

		return objattachment;
	}

	public String storeLargeattachment(String title, MultipartFile file) throws IOException {
		DBObject metaData = new BasicDBObject();
		metaData.put("title", title);

		UUID objGUID = UUID.randomUUID();
		String randomUUIDString = objGUID.toString();

		gridFsTemplate.store(file.getInputStream(), randomUUIDString, file.getContentType(), metaData);

		return randomUUIDString;
	}
	
	public String storeLargeattachmentBarcode(String title, MultipartFile file,String randomUUIDString) throws IOException {
		DBObject metaData = new BasicDBObject();
		metaData.put("title", title);
		gridFsTemplate.store(file.getInputStream(), randomUUIDString, file.getContentType(), metaData);
		return randomUUIDString;
	}
	
	public String storeLargeattachmentwithpreuid(String title, MultipartFile file, String uuid) throws IOException {
		DBObject metaData = new BasicDBObject();
		metaData.put("title", title);

		gridFsTemplate.store(file.getInputStream(), uuid, file.getContentType(), metaData);

		return uuid;
	}

	public OrderAttachment retrieveFile(LsOrderattachments objattach) {

		OrderAttachment objfile = orderAttachmentRepository.findById(objattach.getFileid());

		return objfile;
	}
	public OrderAttachment retrieveFile(ELNFileAttachments objattach) {

		OrderAttachment objfile = orderAttachmentRepository.findById(objattach.getFileid());

		return objfile;
	}
	public GridFSDBFile retrieveLargeFile(String fileid) throws IllegalStateException, IOException {
		GridFSDBFile file = gridFsTemplate.findOne(new Query(Criteria.where("filename").is(fileid)));
		if (file == null) {
			file = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(fileid)));
		}
		return file;
	}

	public Long deleteattachments(String id) {
		return orderAttachmentRepository.deleteById(id);
	}

	public void deletelargeattachments(String id) {
		gridFsTemplate.delete(Query.query(Criteria.where("filename").is(id)));
		gridFsTemplate.delete(Query.query(Criteria.where("_id").is(id)));
	}

	public SheetorderlimsRefrence storeLimsSheetRefrence(MultipartFile file) throws IOException {
		UUID objGUID = UUID.randomUUID();
		String randomUUIDString = objGUID.toString();

		SheetorderlimsRefrence objattachment = new SheetorderlimsRefrence();
		objattachment.setId(randomUUIDString);
		objattachment.setFile(new Binary(BsonBinarySubType.BINARY, file.getBytes()));

		objattachment = sheetorderlimsRefrenceRepository.insert(objattachment);

		return objattachment;
	}
	
	public ResultorderlimsRefrence storeResultLimsSheetRefrence(MultipartFile file) throws IOException {
		UUID objGUID = UUID.randomUUID();
		String randomUUIDString = objGUID.toString();

		ResultorderlimsRefrence objattachment = new ResultorderlimsRefrence();
		objattachment.setId(randomUUIDString);
		objattachment.setFile(new Binary(BsonBinarySubType.BINARY, file.getBytes()));

		objattachment = ResultorderlimsRefrenceRepository.insert(objattachment);

		return objattachment;
	}

	public SheetorderlimsRefrence LimsretrieveELNsheet(LsSheetorderlimsrefrence objattachment) {

		SheetorderlimsRefrence objfile = sheetorderlimsRefrenceRepository.findById(objattachment.getFileid());

		return objfile;
	}
	
	public ResultorderlimsRefrence LimsretrieveResultELNsheet(LsResultlimsOrderrefrence objattachment) {

		ResultorderlimsRefrence objfile = ResultorderlimsRefrenceRepository.findById(objattachment.getFileid());

		return objfile;
	}
	
	public List<GridFSDBFile> retrieveLargeFileinlist(List<String> fileid) throws IllegalStateException, IOException {
		Query query = new Query();
		query.addCriteria(Criteria.where("filename").in(fileid));
		List<GridFSDBFile> result = gridFsTemplate.find(query);
		return result;
	}
}
