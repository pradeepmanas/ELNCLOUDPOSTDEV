package com.agaram.eln.primary.service.fileManipulation;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.fileManipulation.OrderAttachment;
import com.agaram.eln.primary.model.fileManipulation.ProfilePicture;
import com.agaram.eln.primary.model.fileManipulation.SheetorderlimsRefrence;
import com.agaram.eln.primary.model.instrumentDetails.LsOrderattachments;
import com.agaram.eln.primary.model.instrumentDetails.LsSheetorderlimsrefrence;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.repository.cfr.LScfttransactionRepository;
import com.agaram.eln.primary.repository.fileManipulation.OrderAttachmentRepository;
import com.agaram.eln.primary.repository.fileManipulation.ProfilePictureRepository;
import com.agaram.eln.primary.repository.fileManipulation.SheetorderlimsRefrenceRepository;
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

	public ProfilePicture addPhoto(Integer usercode, MultipartFile file, Date currentdate) throws IOException {

		LSuserMaster username = lsuserMasterRepository.findByusercode(usercode);
		String name = username.getUsername();
		LScfttransaction list = new LScfttransaction();
		list.setModuleName("UserManagement");
		list.setComments(name + " " + "Uploaded the profile picture successfully");
		list.setActions("View / Load");
		list.setSystemcoments("System Generated");
		list.setTableName("profile");
		list.setTransactiondate(currentdate);
//	    	list.setLsuserMaster(lsuserMasterRepository.findByusercode(usercode));
		list.setLsuserMaster(usercode);
		lscfttransactionRepository.save(list);
		deletePhoto(usercode, list);

		ProfilePicture profile = new ProfilePicture();
		profile.setId(usercode);
		profile.setName(file.getName());
		profile.setImage(new Binary(BsonBinarySubType.BINARY, file.getBytes()));
		profile = profilePictureRepository.insert(profile);

		return profile;
	}

	public ProfilePicture getPhoto(Integer id) {

		return profilePictureRepository.findById(id);
	}

	public Long deletePhoto(Integer id, LScfttransaction list) {
		list.setTableName("ProfilePicture");
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

	public OrderAttachment retrieveFile(LsOrderattachments objattachment) {

		OrderAttachment objfile = orderAttachmentRepository.findById(objattachment.getFileid());

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

	public SheetorderlimsRefrence LimsretrieveELNsheet(LsSheetorderlimsrefrence objattachment) {

		SheetorderlimsRefrence objfile = sheetorderlimsRefrenceRepository.findById(objattachment.getFileid());

		return objfile;
	}
}
