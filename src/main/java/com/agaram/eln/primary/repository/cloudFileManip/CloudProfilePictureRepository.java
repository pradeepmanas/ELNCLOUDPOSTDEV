package com.agaram.eln.primary.repository.cloudFileManip;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.cloudFileManip.CloudProfilePicture;

public interface CloudProfilePictureRepository extends JpaRepository<CloudProfilePicture, Integer> {
 public CloudProfilePicture findById(Integer Id);
 @Transactional
 public Long deleteById(Integer Id);
}
