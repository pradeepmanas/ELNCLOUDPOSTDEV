package com.agaram.eln.primary.repository.fileManipulation;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.agaram.eln.primary.model.fileManipulation.ProfilePicture;

public interface ProfilePictureRepository extends MongoRepository<ProfilePicture, String> {
 public ProfilePicture findById(Integer Id);
 public Long deleteById(Integer Id);
}
