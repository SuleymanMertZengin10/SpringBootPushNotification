package com.notification.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.notification.controller.GrupController;
import com.notification.model.Device;
import com.notification.model.Grup;
import com.notification.repository.DeviceRepository;
import com.notification.repository.GrupRepository;

@Service
@Transactional
public class GrupService {
	@Autowired
    GrupController grupController;
	
    @Autowired
    GrupRepository grupRepository;
    
    @Autowired
    DeviceRepository  deviceRepository;
    
  
    public Grup addGrup(Grup grup) {
    	
    	return  grupRepository.save(grup);
    }
    
    public List<Grup>getAllGrup(){
    	
    	return grupRepository.findAll();
    	
    }
    public void updateGrupDevices(List<String>grupTokenList,String grupName) throws FirebaseMessagingException {
   	 
		 try {
			grupController.initApp();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
    	Grup grup= grupRepository.findFirstByGrupName(grupName);

   	 List<Device>grupDeviceList=new ArrayList<>();
   	 
      if(grup.getDevices().isEmpty()) {
  
   	  for(int i=0;i<grupTokenList.size();i++) {
   	         String token= grupTokenList.get(i);
   	         Device device=deviceRepository.findFirstByToken(token);
   	       
   	         grupDeviceList.add(device);
   	            
          } 
   		 FirebaseMessaging.getInstance().subscribeToTopic(grupTokenList, grupName);
   		 grup.setDevices(grupDeviceList);
   		 grupRepository.save(grup);
   	

   	 }
   	 else {
   		 
   		 List<Device>deviceList=grup.getDevices();
   		 List<String>existingTokens=new ArrayList<>();
   		 for(int i=0;i<deviceList.size();i++) {
   			 existingTokens.add(deviceList.get(i).getToken());
   		 }
   		 System.out.println(existingTokens);
   		 
   		 List<String>unsubscribeTokens=new ArrayList<>(existingTokens); 
   		 unsubscribeTokens.removeAll(grupTokenList);

   		 if(unsubscribeTokens.isEmpty()) {
   			 System.out.println("silinecek eleman yok");
   		 }
   		 else {
   			 FirebaseMessaging.getInstance().unsubscribeFromTopic(unsubscribeTokens, grupName);
   		 }
   		

   		 List<String>subscribeTokens=new ArrayList<>(grupTokenList);
   		 subscribeTokens.removeAll(existingTokens);
   		 if(subscribeTokens.isEmpty()) {
   			 System.out.println("eklenecek eleman yok");
   		 }
   		 else {
   			 FirebaseMessaging.getInstance().subscribeToTopic(subscribeTokens, grupName);
   		 }

   		 for(int i=0;i<grupTokenList.size();i++) {
   	         String token= grupTokenList.get(i);
   	         Device device=deviceRepository.findFirstByToken(token);
   	         grupDeviceList.add(device);
   	    	 } 
   		 
   		 grup.setDevices(grupDeviceList);
   		 grupRepository.save(grup);
   	 }
    
    }


    public Grup getGrupByGrupName(String grupName) {
    	
    	return grupRepository.findFirstByGrupName(grupName);    	
    }


	public void sendMessageToGroup(String grupName,String title,String body) {
		 
		 try {
			grupController.initApp();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
          
		  Message message = Message.builder()
			     
				  .setAndroidConfig(AndroidConfig.builder().setPriority(AndroidConfig.Priority.NORMAL)
							.setNotification(AndroidNotification.builder()
									.setTitle(title)
									.setBody(body)
									.build())
							.build())
					
					.setTopic(grupName)
					.build();
		
		try {
			FirebaseMessaging.getInstance().send(message);
		} catch (FirebaseMessagingException e) {

			e.printStackTrace();
		}
	}
}
