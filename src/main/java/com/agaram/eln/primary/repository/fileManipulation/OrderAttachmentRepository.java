package com.agaram.eln.primary.repository.fileManipulation;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.agaram.eln.primary.model.fileManipulation.OrderAttachment;
public interface OrderAttachmentRepository extends MongoRepository<OrderAttachment, String> {
	public OrderAttachment findById(String Id);
	public Long deleteById(String Id);
}
