package com.notification.controller;

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

import com.notification.model.Device;
import com.notification.service.DeviceService;

@RestController
public class DeviceController {
	
    @Autowired
    DeviceService deviceService;
	
    
	@PostMapping("/addDevice")
	public ResponseEntity<Device> addDevice(@RequestBody Map<String,String> addDevice) {
		String token=addDevice.get("token");
		String mail=addDevice.get("mail");
		Device device=new Device();
		device.setToken(token);
		device.setMail(mail);
		deviceService.addDevice(device);		
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
	 public void sendMessageToOneDevice(@RequestBody Map<String,String>notification) {

		 String  mail=notification.get("mail");
		 String title=notification.get("title");
		 String body=notification.get("body");
		 deviceService.sendMessageToOneDevice(mail, title, body);

	 }

    


}
