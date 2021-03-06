package com.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.notification.model.Device;

@Repository
public interface DeviceRepository extends JpaRepository<Device, String>{
	
	public Device findFirstByMail(String mail);
	
	public Device findFirstByToken(String token);
	
	
}
