package com.agaram.eln.primary.repository.usermanagement;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.usermanagement.LSusergrouprightsmaster;

public interface LSusergrouprightsmasterRepository extends JpaRepository<LSusergrouprightsmaster, Integer> {

	List<LSusergrouprightsmaster> findByOrderBySequenceorder();

}
