package com.notification.service;

import java.io.IOException;
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
import com.notification.controller.GrupController;
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
	
	@Autowired
	GrupController grupController;
	

	public Device addDevice(String token, String mail) {
         Device device =deviceRepository.findFirstByMail(mail);
		 if (device !=null) {
			 if(device.getToken()!=token) {
				 device.setToken(token);
				 deviceRepository.save(device);
			 } 
		 }
		 
		  else {
			Device newDevice=new Device();
			newDevice.setToken(token);
			newDevice.setMail(mail);
			deviceRepository.save(newDevice); 
		 }
		 
		return deviceRepository.findFirstByMail(mail); 
	}

	
	public List<Device> getAllDevices() {
		
		return deviceRepository.findAll();
	}
	
	public void  deleteDevice(String mail) {
		 try {
			grupController.initApp();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		Device device=deviceRepository.getOne(mail);
	
		List<String>tokenNameList=new  ArrayList<String>();
		tokenNameList.add(device.getToken());
		
		List<Grup>deviceGrupList=device.getGrups();
		for(int i=0;i<deviceGrupList.size();i++) {
			  Grup g= deviceGrupList.get(i);
			  List<Device> grupDevice=g.getDevices();
			  grupDevice.remove(device);
			  g.setDevices(grupDevice);
			  grupRepository.save(g);	
			  try {
				FirebaseMessaging.getInstance().unsubscribeFromTopic(tokenNameList, g.getGrupName());
			} catch (FirebaseMessagingException e) {
				
				e.printStackTrace();
			} 
		}
		deviceRepository.deleteById(mail);
		
	
	}
		public void sendMessageToOneDevice(String mail, String title, String body,Map<String, String> map) {
		
		 Device device=deviceRepository.getOne(mail);
		
		 Message message;
		 
			 try {
				grupController.initApp();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 
		 
		 if(!map.isEmpty()) {
			  message = Message.builder()
					    .putAllData(map)
						.setNotification(new Notification(title, body))
					    .setToken(device.getToken())
					    .build();
		 }else {
			 message = Message.builder()
						.setNotification(new Notification(title, body))
					    .setToken(device.getToken())
					    .build();
		 }
		
		
			try {
				 FirebaseMessaging.getInstance().send(message);
			} catch (FirebaseMessagingException e) {
				
				e.printStackTrace();
			}
	}
	
	
	public String getTokenByMail(String mail) {
		 Device device=deviceRepository.getOne(mail);
			
		 return device.getToken();
	}

}
