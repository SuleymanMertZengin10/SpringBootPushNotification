package com.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.notification.model.Grup;

@Repository
public interface GrupRepository extends JpaRepository<Grup,Integer>{
	
	public Grup findFirstByGrupName(String grupName);

}
