package com.notification.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.notification.model.Device;
import com.notification.model.Grup;
import com.notification.repository.DeviceRepository;
import com.notification.repository.GrupRepository;


@Service
@Transactional
public class DeviceService{
    
	@Autowired
	DeviceRepository deviceRepository;
	
	@Autowired
	GrupRepository grupRepository;
	

	public Device addDevice(Device device) {
		
		return  deviceRepository.save(device);
	}

	
	public List<Device> getAllDevices() {
		
		return deviceRepository.findAll();
	}
	
	public void  deleteDevice(String token) {
		Device device=deviceRepository.getOne(token);
	
		List<String>tokenNameList=new  ArrayList<String>();
		tokenNameList.add(token);
		
		List<Grup>deviceGrupList=device.getGrups();
		for(int i=0;i<deviceGrupList.size();i++) {
			  Grup g= deviceGrupList.get(i);
			  List<Device> grupToken=g.getDevices();
			  grupToken.remove(device);
			  g.setDevices(grupToken);
			  grupRepository.save(g);	
			  try {
				FirebaseMessaging.getInstance().unsubscribeFromTopic(tokenNameList, g.getGrupName());
			} catch (FirebaseMessagingException e) {
				
				e.printStackTrace();
			} 
		}
		deviceRepository.deleteById(token);
		
	
	}
	public void sendMessageToOneDevice(String mail, String title, String body,Map<String, String> map) {
		
		 Device device=deviceRepository.findFirstByMail(mail);
		
		Message message = Message.builder()
			    .putAllData(map)
				.setNotification(new Notification(title, body))
			    .setToken(device.getToken())
			    .build();

			try {
				 FirebaseMessaging.getInstance().send(message);
			} catch (FirebaseMessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	public String getTokenByMail(String mail) {
		 Device device=deviceRepository.findFirstByMail(mail);
			
		 return device.getToken();
	}

}
