package com.notification.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.firebase.FirebaseApp;
import com.notification.model.Device;
import com.notification.service.DeviceService;

@RestController
public class DeviceController {
	
    @Autowired
    DeviceService deviceService;
	 
    @Autowired
    GrupController grupController;
    
	@PostMapping("/addDevice")
	public ResponseEntity<Device> addDevice(@RequestBody Map<String,String> addDevice) {
		String token=addDevice.get("token");
		String mail=addDevice.get("mail");
		Device device=deviceService.addDevice(token,mail);		
	    return new ResponseEntity<Device>(device,HttpStatus.OK);
		
	
	}

	@GetMapping("/getAllDevices")
	public  ResponseEntity<?>getAllDevices(){
		
		return new ResponseEntity<>(deviceService.getAllDevices(),HttpStatus.OK);	
		
	}
	

	@DeleteMapping("/deleteDevice/{token}")
	public void deleteDevice(@PathVariable("token")String token){
		
		deviceService.deleteDevice(token);

	}	
     @PostMapping("/sendNotificationToOneDevice")
	 public void sendMessageToOneDevice(@RequestBody Map<String,Object>notification) {

		 String  mail=(String)notification.get("mail");
		 String title=(String)notification.get("title");
		 String body=(String)notification.get("body");
		    if(FirebaseApp.getApps().size()!=0) {
	        	  try {
	           grupController.fcmSignin();
				} catch (IOException e) {
				
					e.printStackTrace();
				}
	          }
		 Map<String,String>dataMap=(Map<String,String>)notification.get("data");	 
		 deviceService.sendMessageToOneDevice(mail, title, body,dataMap);
		 
	 }
    
    
     @GetMapping("/getToken/{mail}")
     public String getTokenByMail(@PathVariable("mail")String mail) {
    	 
    	 return deviceService.getTokenByMail(mail);
    	  
    	 
    }


 
     


}
